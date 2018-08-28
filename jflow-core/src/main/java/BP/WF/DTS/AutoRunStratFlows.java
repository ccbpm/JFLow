package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Method;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Tools.DateUtils;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.SendReturnObjs;
import BP.WF.Work;
import BP.WF.WorkNode;
import BP.Web.WebUser;

public class AutoRunStratFlows extends Method{

	 /// <summary>
    /// 不带有参数的方法
    /// </summary>
    public AutoRunStratFlows()
    {
    	  this.Title = "自动启动流程";
          this.Help = "在流程属性上配置的信息,自动发起流程,按照时间规则....";
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
    public  boolean getIsCanDo()
    {
            return true;      
    }
    /// <summary>
    /// 执行
    /// </summary>
    /// <returns>返回执行结果</returns>
    public  Object Do() throws Exception
    {
    	   Flows fls = new Flows();
           fls.RetrieveAll();

           	//#region 自动启动流程
           for (Flow fl : fls.ToJavaList())
           {
               if (fl.getHisFlowRunWay() == BP.WF.FlowRunWay.HandWork)
                   continue;

               if (DateUtils.getCurrentDate("HH:mm").equals(fl.Tag))
                   continue;

               if (fl.getRunObj() == null || fl.getRunObj() == "")
               {
                   String msg = "您设置自动运行流程错误，没有设置流程内容，流程编号：" + fl.getNo() + ",流程名称:" + fl.getName();
                   BP.DA.Log.DebugWriteError(msg);
                   continue;
               }

               //#region 判断当前时间是否可以运行它。
               String nowStr = DateUtils.getCurrentDate("yyyy-MM-dd,HH:mm");
               String[] strs = fl.getRunObj().split("@"); //破开时间串。
               boolean IsCanRun = false;
               for (String str : strs)
               {
                   if (StringHelper.isNullOrEmpty(str))
                       continue;
                   if (nowStr.contains(str))
                       IsCanRun = true;
               }

               if (IsCanRun == false)
                   continue;

               // 设置时间.
               fl.Tag = DateUtils.getCurrentDate("HH:mm");
               //#endregion 判断当前时间是否可以运行它。

               // 以此用户进入.
               switch (fl.getHisFlowRunWay())
               {
                   case SpecEmp: //指定人员按时运行。
                       String RunObj = fl.getRunObj();
                       String fk_emp = RunObj.substring(0, RunObj.indexOf('@'));

                       BP.Port.Emp emp = new BP.Port.Emp();
                       emp.setNo(fk_emp);
                       if (emp.RetrieveFromDBSources() == 0)
                       {
                           BP.DA.Log.DebugWriteError("启动自动启动流程错误：发起人(" + fk_emp + ")不存在。");
                           continue;
                       }

                       try
                       {
                           //让 userNo 登录.
                           BP.WF.Dev2Interface.Port_Login(emp.getNo());

                           //创建空白工作, 发起开始节点.
                           long workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fl.getNo());

                           //执行发送.
                           SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(fl.getNo(), workID);

                           //string info_send= BP.WF.Dev2Interface.Node_StartWork(fl.No,);
                           BP.DA.Log.DefaultLogWriteLineInfo("流程:" + fl.getNo() + fl.getName() + "的定时任务\t\n -------------- \t\n" + objs.ToMsgOfText());

                       }
                       catch (Exception ex)
                       {
                           BP.DA.Log.DebugWriteError("流程:" + fl.getNo() + fl.getName() + "自动发起错误:\t\n -------------- \t\n" + ex.getMessage());
                       }
                       continue;
                   case DataModel: //按数据集合驱动的模式执行。
                       this.DTS_Flow(fl);
                       continue;
                   default:
                       break;
               }
           }
           if (BP.Web.WebUser.getNo() != "admin")
           {
               BP.Port.Emp empadmin = new BP.Port.Emp("admin");
               BP.Web.WebUser.SignInOfGener(empadmin);
           }
         //  #endregion 发送消息

           return "调度完成..";
    }


    public void DTS_Flow(BP.WF.Flow fl) throws Exception
    {
      //  #region 读取数据.
        BP.Sys.MapExt me = new MapExt();
        me.setMyPK( "ND" + Integer.parseInt(fl.getNo()) + "01" + "_" + MapExtXmlList.StartFlow);
        int i = me.RetrieveFromDBSources();
        if (i == 0)
        {
            BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
            return;
        }
        if (StringHelper.isNullOrEmpty(me.getTag()))
        {
            BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
            return;
        }

        // 获取从表数据.
        DataSet ds = new DataSet();
        String[] dtlSQLs = me.getTag1().split("*");
        for (String sql : dtlSQLs)
        {
            if (StringHelper.isNullOrEmpty(sql))
                continue;

            String[] tempStrs = sql.split("=");
            String dtlName = tempStrs[0];
            DataTable dtlTable = BP.DA.DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
            dtlTable.TableName = dtlName;
            ds.Tables.add(dtlTable);
        }
        // #endregion 读取数据.

        //   #region 检查数据源是否正确.
        String errMsg = "";
        // 获取主表数据.
        DataTable dtMain = BP.DA.DBAccess.RunSQLReturnTable(me.getTag());
        if (dtMain.Rows.size() == 0)
        {
            BP.DA.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")此时无任务.");
            return;
        }

        if (dtMain.Columns.contains("Starter") == false)
            errMsg += "@配值的主表中没有Starter列.";

        if (dtMain.Columns.contains("MainPK") == false)
            errMsg += "@配值的主表中没有MainPK列.";

        if (errMsg.length() > 2)
        {
            BP.DA.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")的开始节点设置发起数据,不完整." + errMsg);
            return;
        }
       // #endregion 检查数据源是否正确.

       //  #region 处理流程发起.
        String nodeTable = "ND" + Integer.parseInt(fl.getNo()) + "01";
        int idx = 0;
        for (DataRow dr : dtMain.Rows)
        {
            idx++;

            String mainPK = dr.getValue("MainPK").toString();
            String sql = "SELECT OID FROM " + nodeTable + " WHERE MainPK='" + mainPK + "'";
            if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
            {
                continue; /*说明已经调度过了*/
            }

            String starter = dr.getValue("Starter").toString();
            if (WebUser.getNo() != starter)
            {
                BP.Web.WebUser.Exit();
                BP.Port.Emp emp = new BP.Port.Emp();
                emp.setNo(starter);
                if (emp.RetrieveFromDBSources() == 0)
                {
                    BP.DA.Log.DefaultLogWriteLineInfo("@数据驱动方式发起流程(" + fl.getName() + ")设置的发起人员:" + emp.getNo() + "不存在。");
                    continue;
                }
                WebUser.SignInOfGener(emp);
            }

           // #region  给值.
            //System.Collections.Hashtable ht = new Hashtable();

            Work wk = fl.NewWork();

            String err = "";
            //#region 检查用户拼写的sql是否正确？
            for (DataColumn dc : dtMain.Columns)
            {
                String f = dc.ColumnName.toLowerCase();
                switch (f)
                {
                    case "starter":
                    case "mainpk":
                    case "refmainpk":
                    case "tonode":
                        break;
                    default:
                        boolean isHave = false;
                        for (Attr attr : wk.getEnMap().getAttrs())
                        {
                            if (attr.getKey().toLowerCase() == f)
                            {
                                isHave = true;
                                break;
                            }
                        }
                        if (isHave == false)
                        {
                            err += " " + f + " ";
                        }
                        break;
                }
            }
            if (StringHelper.isNullOrEmpty(err) == false)
                throw new Exception("您设置的字段:" + err + "不存在开始节点的表单中，设置的sql:" + me.getTag());

           // #endregion 检查用户拼写的sql是否正确？

            for (DataColumn dc : dtMain.Columns)
                wk.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
            
    
            if (ds.Tables.size() != 0)
            {
                // MapData md = new MapData(nodeTable);
                MapDtls dtls = new MapDtls(nodeTable);
                for (MapDtl dtl : dtls.ToJavaList())
                {
                    for (DataTable dt : ds.Tables)
                    {
                        if (dt.TableName != dtl.getNo())
                            continue;

                        //删除原来的数据。
                        GEDtl dtlEn = dtl.getHisGEDtl();
                        dtlEn.Delete(GEDtlAttr.RefPK, String.valueOf(wk.getOID()));

                        // 执行数据插入。
                        for (DataRow drDtl : dt.Rows)
                        {
                            if (!drDtl.getValue("RefMainPK").toString().equals(mainPK))
                                continue;

                            dtlEn = dtl.getHisGEDtl();
                            for (DataColumn dc : dt.Columns)
                                dtlEn.SetValByKey(dc.ColumnName, drDtl.getValue(dc.ColumnName).toString());

                            dtlEn.setRefPK(String.valueOf(wk.getOID()));
                            dtlEn.setOID(0);
                            dtlEn.Insert();
                        }
                    }
                }
            }
           // #endregion  给值.


            int toNodeID = 0;
            try
            {
                toNodeID =Integer.parseInt(dr.getValue("ToNode").toString());
            }catch(Exception e){
                /*有可能在4.5以前的版本中没有tonode这个约定.*/
            }

            // 处理发送信息.
            //  Node nd =new Node();
            String msg = "";
            try
            {
                if (toNodeID == 0)
                {
                    WorkNode wn = new WorkNode(wk, fl.getHisStartNode());
                    msg = wn.NodeSend().ToMsgOfText();
                }

                if (toNodeID == fl.getStartNodeID())
                {
                    /* 发起后让它停留在开始节点上，就是为开始节点创建一个待办。*/
                    long workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fl.getNo(), null, null, WebUser.getNo(), null);
                    if (workID != wk.getOID())
                        throw new Exception("@异常信息:不应该不一致的workid.");
                    else
                        wk.Update();
                    msg = "已经为(" + WebUser.getNo() + ") 创建了开始工作节点. ";
                }

                BP.DA.Log.DefaultLogWriteLineInfo(msg);
            }
            catch (Exception ex)
            {
                BP.DA.Log.DefaultLogWriteLineWarning("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n" + ex.getMessage());
            }
        }
        //#endregion 处理流程发起.
    }
}

