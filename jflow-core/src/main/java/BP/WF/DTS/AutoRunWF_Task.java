package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Method;
import BP.Sys.MapData;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Task;
import BP.WF.TaskAttr;
import BP.WF.Work;
import BP.WF.WorkNode;
import cn.jflow.common.util.DateAndTime;

/// <summary>
/// Method 的摘要说明
/// </summary>
public class AutoRunWF_Task extends Method
{
    /// <summary>
    /// 不带有参数的方法
    /// </summary>
    public AutoRunWF_Task()
    {
        this.Title = "自动启动触发模式发起的流程";
        this.Help = "自动启动任务方式的流程, WF_Task";
    }
    /// <summary>
    /// 设置执行变量
    /// </summary>
    /// <returns></returns>
    public  void Init()
    {
    }
    /// <summary>
    /// 当前的操纵员是否可以执行这个方法
    /// </summary>
    public boolean getIsCanDo()
    {
            return true;
    }
    /// <summary>
    /// 执行
    /// </summary>
    /// <returns>返回执行结果</returns>
    public  Object Do() throws Exception
    {
        String info = "";
        String sql = "SELECT * FROM WF_Task WHERE TaskSta=0 ORDER BY Starter";
        DataTable dt = null;
        try
        {
            dt = DBAccess.RunSQLReturnTable(sql);
        }
        catch(Exception e)
        {
            Task ta = new Task();
            ta.CheckPhysicsTable();
            dt = DBAccess.RunSQLReturnTable(sql);
        }

        if (dt.Rows.size() == 0)
            return "无任务";

        //#region 自动启动流程
        for (DataRow dr : dt.Rows)
        {
            String mypk = dr.getValue("MyPK").toString();
            String taskSta = dr.getValue("TaskSta").toString();
            String paras = dr.getValue("Paras").toString();
            String starter = dr.getValue("Starter").toString();
            String fk_flow = dr.getValue("FK_Flow").toString();

            String startDT = dr.getValue(TaskAttr.StartDT).toString();
            if (StringHelper.isNullOrEmpty(startDT) == false)
            {
                /*如果设置了发起时间,就检查当前时间是否与现在的时间匹配.*/
                if (DateAndTime.getDateNow().contains(startDT) == false)
                    continue;
            }

            Flow fl = new Flow(fk_flow);
            try
            {
                String fTable = "ND" + Integer.parseInt(fl.getNo() + "01");
                MapData md = new MapData(fTable);
                sql = "";
                //   sql = "SELECT * FROM " + md.PTable + " WHERE MainPK='" + mypk + "' AND WFState=1";
                try
                {
                    if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
                        continue;
                }
                catch(Exception e)
                {
                    info += "开始节点表单表:" + fTable + "没有设置的默认字段MainPK. " + sql; ;
                    continue;
                }

                if (BP.Web.WebUser.getNo() != starter)
                {
                    BP.Web.WebUser.Exit();
                    BP.Port.Emp empadmin = new BP.Port.Emp(starter);
                    BP.Web.WebUser.SignInOfGener(empadmin);
                }

                Work wk = fl.NewWork();
                String[] strs = paras.split("@");
                for (String str : strs)
                {
                    if (StringHelper.isNullOrEmpty(str))
                        continue;

                    if (str.contains("=") == false)
                        continue;

                    String[] kv = str.split("=");
                    wk.SetValByKey(kv[0], kv[1]);
                }

                wk.SetValByKey("MainPK", mypk);
                wk.Update();

                WorkNode wn = new WorkNode(wk, fl.getHisStartNode());
                String msg = wn.NodeSend().ToMsgOfText();
                msg = msg.replace("'", "~");
                DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=1,Msg='" + msg + "' WHERE MyPK='" + mypk + "'");
            }
            catch (Exception ex)
            {
                //如果发送错误。
                info += ex.getMessage();
                String msg = ex.getMessage();
                try
                {
                    DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=2,Msg='" + msg + "' WHERE MyPK='" + mypk + "'");
                }
                catch(Exception e)
                {
                    Task TK = new Task();
                    TK.CheckPhysicsTable();
                }
            }
        }
       // #endregion 自动启动流程

        return info;
    }
}