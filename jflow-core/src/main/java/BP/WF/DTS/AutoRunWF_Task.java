package BP.WF.DTS;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Web.WebUser;
import BP.Web.Controls.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.time.*;
import java.util.Date;

/** 
 Method 的摘要说明
*/
public class AutoRunWF_Task extends Method
{
	/** 
	 不带有参数的方法
	*/
	public AutoRunWF_Task()
	{
		this.Title = "自动启动流程，使用扫描WF_Task表的模式.";
		this.Help = "自动启动任务方式的流程, WF_Task";
		this.GroupName = "流程自动执行定时任务";

	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		String info = "";
		String sql = "SELECT * FROM WF_Task WHERE TaskSta=0 ORDER BY Starter";
		DataTable dt = null;
		try
		{
			dt = DBAccess.RunSQLReturnTable(sql);
		}
		catch (java.lang.Exception e)
		{
			Task ta = new Task();
			ta.CheckPhysicsTable();
			dt = DBAccess.RunSQLReturnTable(sql);
		}

		if (dt.Rows.size() == 0)
		{
			return "无任务";
		}


			///#region 自动启动流程
		for (DataRow dr : dt.Rows)
		{
			String mypk = dr.get("MyPK").toString();
			String taskSta = dr.get("TaskSta").toString();
			String paras = dr.get("Paras").toString();
			String starter = dr.get("Starter").toString();
			String fk_flow = dr.get("FK_Flow").toString();

			//获得到达的节点，与接受人。
			String toEmps = dr.get("ToEmps").toString();
			if (DataType.IsNullOrEmpty(toEmps))
			{
				toEmps = null;
			}

			String toNodeStr = dr.get("ToNode").toString();
			int toNodeID = 0;
			if (DataType.IsNullOrEmpty(toNodeStr) == false)
			{
				toNodeID = Integer.parseInt(toNodeStr);
			}

			String startDT = dr.get(TaskAttr.StartDT).toString();
			if (DataType.IsNullOrEmpty(startDT) == false)
			{
				/*如果设置了发起时间,就检查当前时间是否与现在的时间匹配.*/
				if (new Date().compareTo(DateUtils.parse(startDT)) < 0)
				{
					continue;
				}
			}

			Flow fl = new Flow(fk_flow);
			if (fl.getHisFlowAppType() == FlowAppType.PRJ)
			{
				if (paras.contains("PrjNo=") == false || paras.contains("PrjName=") == false)
				{
					info += "err@工程类的流程，没有PrjNo，PrjName参数:" + fl.getName();
					DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=2,Msg='" + info + "' WHERE MyPK='" + mypk + "'");
					continue;
				}
			}

			long workID = 0;
			try
			{
				String fTable = "ND" + fl.getNo() + "01";
				MapData md = new MapData(fTable);
				//sql = "";
				sql = "SELECT * FROM " + md.getPTable() + " WHERE MainPK='" + mypk + "' AND WFState=1";
				try
				{
					if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
					{
						continue;
					}
				}
				catch (java.lang.Exception e2)
				{
					info += "开始节点表单表:" + fTable + "没有设置的默认字段MainPK. " + sql;
					continue;
				}

				if (!starter.equals(WebUser.getNo()))
				{
					WebUser.Exit();
					BP.Port.Emp empadmin = new BP.Port.Emp(starter);
					WebUser.SignInOfGener(empadmin);
				}

				Work wk = fl.NewWork();
				workID = wk.getOID();
				String[] strs = paras.split("[@]", -1);
				for (String str : strs)
				{
					if (DataType.IsNullOrEmpty(str))
					{
						continue;
					}

					if (str.contains("=") == false)
					{
						continue;
					}

					String[] kv = str.split("[=]", -1);
					wk.SetValByKey(kv[0], kv[1]);
				}

				wk.SetValByKey("MainPK", mypk);
				wk.Update();

				if (fl.getHisFlowAppType() == FlowAppType.PRJ)
				{
					String prjNo = wk.GetValStrByKey("PrjNo");
					if (DataType.IsNullOrEmpty(prjNo) == true)
					{
						info += "err@没有找到工程编号：MainPK" + mypk;
						DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=2,Msg='" + info + "' WHERE MyPK='" + mypk + "'");
						continue;
					}
				}

				WorkNode wn = new WorkNode(wk, fl.getHisStartNode());

				String msg = "";

				if (toNodeID == 0)
				{
					msg = wn.NodeSend(null, toEmps).ToMsgOfText();
				}
				else
				{
					msg = wn.NodeSend(new Node(toNodeID), toEmps).ToMsgOfText();
				}

				msg = msg.replace("'", "~");

				DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=1,Msg='" + msg + "' WHERE MyPK='" + mypk + "'");
			}
			catch (RuntimeException ex)
			{
				//删除流程数据
				if (workID != 0)
				{
					BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow, workID);
				}

				//如果发送错误。
				info += ex.getMessage();
				String msg = ex.getMessage();
				try
				{
					DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=2,Msg='" + msg + "' WHERE MyPK='" + mypk + "'");
				}
				catch (java.lang.Exception e3)
				{
					Task TK = new Task();
					TK.CheckPhysicsTable();
				}
			}
		}

			///#endregion 自动启动流程

		return info;
	}
}