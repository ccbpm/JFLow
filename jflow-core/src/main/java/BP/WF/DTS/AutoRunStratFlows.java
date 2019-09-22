package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.Web.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.time.*;

/** 
 Method 的摘要说明
*/
public class AutoRunStratFlows extends Method
{
	/** 
	 不带有参数的方法
	*/
	public AutoRunStratFlows()
	{
		this.Title = "自动启动流程";
		this.Help = "在流程属性上配置的信息,自动发起流程,按照时间规则....";
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
	*/
	@Override
	public Object Do()
	{
		BP.WF.Flows fls = new Flows();
		fls.RetrieveAll();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 自动启动流程
		for (BP.WF.Flow fl : fls.ToJavaList())
		{
			if (fl.getHisFlowRunWay() == BP.WF.FlowRunWay.HandWork)
			{
				continue;
			}

			if (LocalDateTime.now().toString("HH:mm").equals(fl.Tag))
			{
				continue;
			}

			if (fl.getRunObj() == null || fl.getRunObj().equals(""))
			{
				String msg = "您设置自动运行流程错误，没有设置流程内容，流程编号：" + fl.getNo() + ",流程名称:" + fl.Name;
				BP.DA.Log.DebugWriteError(msg);
				continue;
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 判断当前时间是否可以运行它。
			String nowStr = LocalDateTime.now().toString("yyyy-MM-dd,HH:mm");
			String[] strs = fl.getRunObj().split("[@]", -1); //破开时间串。
			boolean IsCanRun = false;
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}
				if (nowStr.contains(str))
				{
					IsCanRun = true;
				}
			}

			if (IsCanRun == false)
			{
				continue;
			}

			// 设置时间.
			fl.Tag = LocalDateTime.now().toString("HH:mm");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 判断当前时间是否可以运行它。

			// 以此用户进入.
			switch (fl.getHisFlowRunWay())
			{
				case SpecEmp: //指定人员按时运行。
					String RunObj = fl.getRunObj();
					String fk_emp = RunObj.substring(0, RunObj.indexOf('@'));

					BP.Port.Emp emp = new BP.Port.Emp();
					emp.No = fk_emp;
					if (emp.RetrieveFromDBSources() == 0)
					{
						BP.DA.Log.DebugWriteError("启动自动启动流程错误：发起人(" + fk_emp + ")不存在。");
						continue;
					}

					try
					{
						//让 userNo 登录.
						BP.WF.Dev2Interface.Port_Login(emp.No);

						//创建空白工作, 发起开始节点.
						long workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fl.No);

						//执行发送.
						SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(fl.No, workID);

						//string info_send= BP.WF.Dev2Interface.Node_StartWork(fl.No,);
						BP.DA.Log.DefaultLogWriteLineInfo("流程:" + fl.No + fl.Name + "的定时任务\t\n -------------- \t\n" + objs.ToMsgOfText());

					}
					catch (RuntimeException ex)
					{
						BP.DA.Log.DebugWriteError("流程:" + fl.No + fl.Name + "自动发起错误:\t\n -------------- \t\n" + ex.getMessage());
					}
					continue;
				case DataModel: //按数据集合驱动的模式执行。
					this.DTS_Flow(fl);
					continue;
				case InsertModel: //按数据集合驱动的模式执行。
					this.InsertModel(fl);
					continue;
				default:
					break;
			}
		}
		if (!WebUser.getNo().equals("admin"))
		{
			BP.Port.Emp empadmin = new BP.Port.Emp("admin");
			WebUser.SignInOfGener(empadmin);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 发送消息

		return "调度完成..";
	}
	/** 
	 触发模式
	 
	 @param fl
	*/
	public final void InsertModel(BP.WF.Flow fl)
	{
	}

	public final void DTS_Flow(BP.WF.Flow fl)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 读取数据.
		BP.Sys.MapExt me = new MapExt();
		me.setMyPK( "ND" + Integer.parseInt(fl.No) + "01" + "_" + MapExtXmlList.StartFlow;
		int i = me.RetrieveFromDBSources();
		if (i == 0)
		{
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return;
		}
		if (DataType.IsNullOrEmpty(me.Tag))
		{
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return;
		}

		// 获取从表数据.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.Tag1.split("[*]", -1);
		for (String sql : dtlSQLs)
		{
			if (DataType.IsNullOrEmpty(sql))
			{
				continue;
			}

			String[] tempStrs = sql.split("[=]", -1);
			String dtlName = tempStrs[0];
			DataTable dtlTable = BP.DA.DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
			dtlTable.TableName = dtlName;
			ds.Tables.add(dtlTable);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 读取数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据源是否正确.
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = BP.DA.DBAccess.RunSQLReturnTable(me.Tag);
		if (dtMain.Rows.size() == 0)
		{
			BP.DA.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")此时无任务.");
			return;
		}

		if (dtMain.Columns.Contains("Starter") == false)
		{
			errMsg += "@配值的主表中没有Starter列.";
		}

		if (dtMain.Columns.Contains("MainPK") == false)
		{
			errMsg += "@配值的主表中没有MainPK列.";
		}

		if (errMsg.length() > 2)
		{
			BP.DA.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")的开始节点设置发起数据,不完整." + errMsg);
			return;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查数据源是否正确.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理流程发起.
		String nodeTable = "ND" + Integer.parseInt(fl.No) + "01";
		int idx = 0;
		for (DataRow dr : dtMain.Rows)
		{
			idx++;

			String mainPK = dr.get("MainPK").toString();
			String sql = "SELECT OID FROM " + nodeTable + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
			{
				continue; //说明已经调度过了
			}

			String starter = dr.get("Starter").toString();
			if (!starter.equals(WebUser.getNo()))
			{
				WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.No = starter;
				if (emp.RetrieveFromDBSources() == 0)
				{
					BP.DA.Log.DefaultLogWriteLineInfo("@数据驱动方式发起流程(" + fl.getName() + ")设置的发起人员:" + emp.No + "不存在。");
					continue;
				}
				WebUser.SignInOfGener(emp);
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  给值.
			//System.Collections.Hashtable ht = new Hashtable();

			Work wk = fl.NewWork();

			String err = "";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查用户拼写的sql是否正确？
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
						for (Attr attr : wk.EnMap.Attrs)
						{
							if (attr.Key.toLowerCase().equals(f))
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
			if (DataType.IsNullOrEmpty(err) == false)
			{
				throw new RuntimeException("您设置的字段:" + err + "不存在开始节点的表单中，设置的sql:" + me.Tag);
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查用户拼写的sql是否正确？

			for (DataColumn dc : dtMain.Columns)
			{
				wk.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0)
			{
				// MapData md = new MapData(nodeTable);
				MapDtls dtls = new MapDtls(nodeTable);
				for (MapDtl dtl : dtls.ToJavaList())
				{
					for (DataTable dt : ds.Tables)
					{
						if (!dt.TableName.equals(dtl.No))
						{
							continue;
						}

						//删除原来的数据。
						GEDtl dtlEn = dtl.HisGEDtl;
						dtlEn.Delete(GEDtlAttr.RefPK, String.valueOf(wk.getOID()));

						// 执行数据插入。
						for (DataRow drDtl : dt.Rows)
						{
							if (!drDtl.get("RefMainPK").toString().equals(mainPK))
							{
								continue;
							}

							dtlEn = dtl.HisGEDtl;
							for (DataColumn dc : dt.Columns)
							{
								dtlEn.SetValByKey(dc.ColumnName, drDtl.get(dc.ColumnName).toString());
							}

							dtlEn.RefPK = String.valueOf(wk.getOID());
							dtlEn.OID = 0;
							dtlEn.Insert();
						}
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  给值.


			int toNodeID = 0;
			try
			{
				toNodeID = Integer.parseInt(dr.get("ToNode").toString());
			}
			catch (java.lang.Exception e)
			{
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
					long workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fl.No, null, null, WebUser.getNo(), null);
					if (workID != wk.getOID())
					{
						throw new RuntimeException("@异常信息:不应该不一致的workid.");
					}
					else
					{
						wk.Update();
					}
					msg = "已经为(" + WebUser.getNo() + ") 创建了开始工作节点. ";
				}

				BP.DA.Log.DefaultLogWriteLineInfo(msg);
			}
			catch (RuntimeException ex)
			{
				BP.DA.Log.DefaultLogWriteLineWarning("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n" + ex.getMessage());
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理流程发起.
	}
}