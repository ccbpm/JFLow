package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.Tools.*;
import BP.Web.*;
import BP.WF.Data.*;
import BP.WFEmp;
import BP.WF.Template.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;

/** 
 此接口为程序员二次开发使用,在阅读代码前请注意如下事项.
 1, CCFlow的对外的接口都是以静态方法来实现的.
 2, 以 DB_ 开头的是需要返回结果集合的接口.
 3, 以 Flow_ 是流程接口.
 4, 以 Node_ 是节点接口.
 5, 以 Port_ 是组织架构接口.
 6, 以 DTS_ 是调度. data tranr system.
 7, 以 UI_ 是流程的功能窗口
 8, 以 WorkOpt_ 用工作处理器相关的接口。
*/
public class Dev2Interface
{

		///#region 写入消息表.
	/** 
	 写入消息
	 用途可以处理提醒.
	 
	 @param sendToUserNo 发送给的操作员ID
	 @param sendDT 发送时间，如果null 则表示立刻发送。
	 @param title 标题
	 @param doc 内容
	 @param msgFlag 消息标记
	 @return 写入成功或者失败.
	*/
	public static boolean WriteToSMS(String sendToUserNo, String sendDT, String title, String doc, String msgFlag)
	{
		SMS.SendMsg(sendToUserNo, title, doc, msgFlag, "Info", "");
		return true;
	}

		///#endregion


		///#region 等待要去处理的消息数量.
	/** 
	 待办工作
	*/
	public static int getTodolist_Todolist()
	{
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		if (WebUser.getIsAuthorize() == false)
		{
				/*不是授权状态*/
			if (BP.WF.Glo.getIsEnableTaskPool() == true)
			{
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1 ";
			}
			else
			{
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp ";
			}

			ps.Add("FK_Emp", WebUser.getNo());

				//  BP.DA.Log.DebugWriteInfo(ps.SQL);
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

			/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1  ";
				}
				else
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp ";
				}
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0   ";
				}
				else
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows();
				}

				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
					/*不是授权状态 */
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1 ";
				}
				else
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp ";
				}

				ps.Add("FK_Emp", WebUser.getNo());
				return BP.DA.DBAccess.RunSQLReturnValInt(ps);
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/** 
	 待办工作数量
	 * @throws Exception 
	*/
	public static int getTodolist_EmpWorks() throws Exception
	{
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		if (WebUser.getIsAuthorize() == false)
		{
				/*不是授权状态*/
			if (BP.WF.Glo.getIsEnableTaskPool() == true)
			{
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1 ";
			}
			else
			{
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp ";
			}

			ps.Add("FK_Emp", WebUser.getNo());

				//  BP.DA.Log.DebugWriteInfo(ps.SQL);
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

			/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1  ";
				}
				else
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp ";
				}

				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0   ";
				}
				else
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows();
				}

				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
					/*不是授权状态 */
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1 ";
				}
				else
				{
					ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp ";
				}

				ps.Add("FK_Emp", WebUser.getNo());
				return BP.DA.DBAccess.RunSQLReturnValInt(ps);
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/** 
	 抄送数量
	*/
	public static int getTodolist_CCWorks()
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT count(MyPK) as Num FROM WF_CCList WHERE CCTo=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND Sta=0";
		ps.Add("FK_Emp", WebUser.getNo());
		return DBAccess.RunSQLReturnValInt(ps, 0);
	}
	/** 
	 返回挂起流程数量
	*/
	public static int getTodolist_HungUpNum()
	{
		String sql = "SELECT  COUNT(WorkID) AS Num from WF_GenerWorkFlow where WFState=4 and  WorkID in (SELECT distinct WorkID FROM WF_HungUp WHERE Rec='" + WebUser.getNo() + "')";
		return BP.DA.DBAccess.RunSQLReturnValInt(sql);
	}
	/** 
	 在途的工作数量
	*/
	public static int getTodolist_Runing()
	{
		String sql;
		int state = WFState.Runing.getValue();
		if (WebUser.getIsAuthorize())
		{
				/*如果是授权状态.*/
			WFEmp emp = new WFEmp(WebUser.getNo());
			sql = "SELECT count( distinct A.WorkID ) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND (B.IsPass=1 OR B.IsPass<0) AND A.FK_Flow IN " + emp.getAuthorFlows();
			return BP.DA.DBAccess.RunSQLReturnValInt(sql);
		}
		else
		{
			Paras ps = new Paras();
			ps.SQL = "SELECT count( distinct A.WorkID ) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND B.IsEnable=1 AND (B.IsPass=1 OR B.IsPass<0) ";
			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}
	}

	/** 
	 获取草稿箱流程数量
	*/
	public static int getTodolist_Draft()
	{
			/*获取数据.*/
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		BP.DA.Paras ps = new BP.DA.Paras();
		ps.SQL = "SELECT count(a.WorkID ) as Num FROM WF_GenerWorkFlow A WHERE WFState=1 AND Starter=" + dbStr + "Starter";
		ps.Add(GenerWorkFlowAttr.Starter, WebUser.getNo());
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}
	/** 
	 会签的数量
	*/
	public static int getTodolist_HuiQian()
	{
			/*获取数据.*/
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		BP.DA.Paras ps = new BP.DA.Paras();
		ps.SQL = "SELECT COUNT(workid) as Num FROM WF_GenerWorkerlist WHERE FK_Emp=" + dbStr + "FK_Emp AND IsPass=90";
		ps.Add(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}
	/** 
	 获取已经完成流程数量
	 
	 @return 
	*/
	public static int getTodolist_Complete()
	{

			/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT count(WorkID) Num FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' AND WFState=" + WFState.Complete.getValue();
		return BP.DA.DBAccess.RunSQLReturnValInt(ps, 0);
			//}
			//else
			//{
			//    Paras ps = new Paras();
			//    string dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			//    ps.SQL = "SELECT count(*) Num FROM V_FlowData WHERE FlowEmps LIKE '%@" + WebUser.getNo() + "%' AND FID=0 AND WFState=" + (int)WFState.Complete;
			//    return BP.DA.DBAccess.RunSQLReturnValInt(ps, 0);
			//}
	}
	/** 
	 共享任务个数
	*/
	public static int getTodolist_Sharing()
	{
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + ") AND TaskSta=" + TaskSta.Sharing.getValue();
		String sql;
		String realSql = null;
		if (WebUser.getIsAuthorize() == false)
		{
				/*不是授权状态*/
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

			/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " ";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
					//   WebUser.getIsAuthorize() = false;
					/*不是授权状态*/
				ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
				ps.Add("FK_Emp", WebUser.getNo());
				return BP.DA.DBAccess.RunSQLReturnValInt(ps);
				//throw new Exception("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}
	/** 
	 申请下来的工作个数
	*/
	public static int getTodolist_Apply()
	{
		if (BP.WF.Glo.getIsEnableTaskPool() == false)
		{
			return 0;
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + ") AND TaskSta=" + TaskSta.Takeback.getValue();
		String sql;
		String realSql;
		if (WebUser.getIsAuthorize() == false)
		{
				/*不是授权状态*/
				// ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY FK_Flow,ADT DESC ";
				//ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC ";
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp";

				// ps.SQL = "select v1.*,v2.name,v3.name as ParentName from (" + realSql + ") as v1 left join JXW_Inc v2 on v1.WorkID=v2.OID left join Jxw_Inc V3 on v1.PWorkID = v3.OID ORDER BY v1.ADT DESC";

			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

			/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
				throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

		///#endregion 等待要去处理的消息数量.


		///#region 自动执行
	/** 
	 处理延期的任务.根据节点属性的设置
	 
	 @return 返回处理的消息
	*/
	public static String DTS_DealDeferredWork()
	{
		BP.WF.DTS.DTS_DealDeferredWork en = new BP.WF.DTS.DTS_DealDeferredWork();
		en.Do();

		return "执行成功..";
	}
	/** 
	 自动执行开始节点数据
	 说明:根据自动执行的流程设置，自动启动发起的流程。
	 比如：您根据ccflow的自动启动流程的设置，自动启动该流程，不使用ccflow的提供的服务程序，您需要按如下步骤去做。
	 1, 写一个自动调度的程序。
	 2，根据自己的时间需要调用这个接口。
	 
	 @param fl 流程实体,您可以 new Flow(flowNo); 来传入.
	*/
	public static void DTS_AutoStarterFlow(Flow fl)
	{

			///#region 读取数据.
		BP.Sys.MapExt me = new BP.Sys.MapExt();
		int i = me.Retrieve(MapExtAttr.FK_MapData, "ND" + Integer.parseInt(fl.No) + "01", MapExtAttr.ExtType, "PageLoadFull");
		if (i == 0)
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

			///#endregion 读取数据.


			///#region 检查数据源是否正确.
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = BP.DA.DBAccess.RunSQLReturnTable(me.Tag);
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

			///#endregion 检查数据源是否正确.


			///#region 处理流程发起.

		String nodeTable = "ND" + Integer.parseInt(fl.No) + "01";
		MapData md = new MapData(nodeTable);

		for (DataRow dr : dtMain.Rows)
		{
			String mainPK = dr.get("MainPK").toString();
			String sql = "SELECT OID FROM " + md.PTable + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
			{
				continue; //说明已经调度过了
			}

			String starter = dr.get("Starter").toString();
			if (!starter.equals(WebUser.getNo()))
			{
				WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo (starter;
				if (emp.RetrieveFromDBSources() == 0)
				{
					BP.DA.Log.DefaultLogWriteLineInfo("@数据驱动方式发起流程(" + fl.getName() + ")设置的发起人员:" + emp.No + "不存在。");
					continue;
				}

				WebUser.SignInOfGener(emp);
			}


				///#region  给值.
			Work wk = fl.NewWork();
			for (DataColumn dc : dtMain.Columns)
			{
				wk.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0)
			{
				String refPK = dr.get("MainPK").toString();
				MapDtls dtls = wk.getHisNode().getMapData().MapDtls; // new MapDtls(nodeTable);
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
							if (!drDtl.get("RefMainPK").toString().equals(refPK))
							{
								continue;
							}

							dtlEn = dtl.HisGEDtl;

							for (DataColumn dc : dt.Columns)
							{
								dtlEn.SetValByKey(dc.ColumnName, drDtl.get(dc.ColumnName).toString());
							}

							dtlEn.RefPK = String.valueOf(wk.getOID());
							dtlEn.Insert();
						}
					}
				}
			}

				///#endregion  给值.

			// 处理发送信息.
			Node nd = fl.getHisStartNode();
			try
			{
				WorkNode wn = new WorkNode(wk, nd);
				String msg = wn.NodeSend().ToMsgOfHtml();
				//BP.DA.Log.DefaultLogWriteLineInfo(msg);
			}
			catch (RuntimeException ex)
			{
				BP.DA.Log.DefaultLogWriteLineWarning(ex.getMessage());
			}
		}

			///#endregion 处理流程发起.

	}

		///#endregion


		///#region 数据集合接口(如果您想获取一个结果集合的接口，都是以DB_开头的.)
	/** 
	 获取能发起流程的人员
	 
	 @param fk_flow 流程编号
	 @return 
	*/
	public static String GetFlowStarters(String fk_flow)
	{
		BP.WF.Node nd = new Node(Integer.parseInt(fk_flow + "01"));
		String sql = "";
		switch (nd.getHisDeliveryWay())
		{
			case ByBindEmp: //按人员
				sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node=" + nd.getNodeID() + ")";
				break;
			case ByDept: //按部门
				sql = "SELECT * FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + nd.getNodeID() + ")";
				break;
			case ByStation: //按岗位
			case FindSpecDeptEmpsInStationlist: //按岗位
				sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN ( SELECT FK_Station from WF_nodeStation where FK_Node=" + nd.getNodeID() + ")) ";
				break;
			default:
				throw new RuntimeException("@开始节点的人员访问规则错误,不允许在开始节点设置此访问类型:" + nd.getHisDeliveryWay());
				break;
		}
		return sql;
	}
	public static String GetFlowStarters(String fk_flow, String fk_dept)
	{
		BP.WF.Node nd = new Node(Integer.parseInt(fk_flow + "01"));
		String sql = "";
		switch (nd.getHisDeliveryWay())
		{
			case ByBindEmp: //按人员
				sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node=" + nd.getNodeID() + ") and fk_dept='" + fk_dept + "'";
				break;
			case ByDept: //按部门
				sql = "SELECT * FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + nd.getNodeID() + ") and fk_dept='" + fk_dept + "' ";
				break;
			case ByStation: //按岗位
				sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN ( SELECT FK_Station from WF_nodeStation where FK_Node=" + nd.getNodeID() + ")) and fk_dept='" + fk_dept + "' ";
				break;
			default:
				throw new RuntimeException("@开始节点的人员访问规则错误,不允许在开始节点设置此访问类型:" + nd.getHisDeliveryWay());
				break;
		}
		return sql;
	}


		///#region 与子流程相关.
	/** 
	 获取流程事例的运行轨迹数据.
	 说明：使用这些数据可以生成流程的操作日志.
	 
	 @param workid 工作ID
	 @return GenerWorkFlows
	*/
	public static GenerWorkFlows DB_SubFlows(long workid)
	{
		GenerWorkFlows gwf = new GenerWorkFlows();
		gwf.Retrieve(GenerWorkFlowAttr.PWorkID, workid);
		return gwf;
	}

		///#endregion 获取流程事例的轨迹图


		///#region 获取流程事例的轨迹图

	public static DataTable DB_GenerTrackTable(String fk_flow, long workid, long fid)
	{

			///#region 获取track数据.
		String sqlOfWhere2 = "";
		String sqlOfWhere1 = "";
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (fid == 0)
		{
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "WorkID11 OR WorkID=" + dbStr + "WorkID12 )  ";
			ps.Add("WorkID11", workid);
			ps.Add("WorkID12", workid);
		}
		else
		{
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "FID11 OR WorkID=" + dbStr + "FID12 ) ";
			ps.Add("FID11", fid);
			ps.Add("FID12", fid);
		}

		String sql = "";
		sql = "SELECT MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan,Msg,NodeData,Exer,Tag FROM ND" + Integer.parseInt(fk_flow) + "Track " + sqlOfWhere1 + " ORDER BY RDT ASC ";
		ps.SQL = sql;
		DataTable dt = null;

		try
		{
			dt = DBAccess.RunSQLReturnTable(ps);
		}
		catch (java.lang.Exception e)
		{
			// 处理track表.
			Track.CreateOrRepairTrackTable(fk_flow);
			dt = DBAccess.RunSQLReturnTable(ps);
		}

		//把列名转化成区分大小写.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("MYPK").ColumnName = "MyPK";
			dt.Columns.get("ACTIONTYPE").ColumnName = "ActionType";
			dt.Columns.get("ACTIONTYPETEXT").ColumnName = "ActionTypeText";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("NDFROM").ColumnName = "NDFrom";
			dt.Columns.get("NDFROMT").ColumnName = "NDFromT";
			dt.Columns.get("NDTO").ColumnName = "NDTo";
			dt.Columns.get("NDTOT").ColumnName = "NDToT";
			dt.Columns.get("EMPFROM").ColumnName = "EmpFrom";
			dt.Columns.get("EMPFROMT").ColumnName = "EmpFromT";
			dt.Columns.get("EMPTO").ColumnName = "EmpTo";
			dt.Columns.get("EMPTOT").ColumnName = "EmpToT";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("WORKTIMESPAN").ColumnName = "WorkTimeSpan";
			dt.Columns.get("MSG").ColumnName = "Msg";
			dt.Columns.get("NODEDATA").ColumnName = "NodeData";
			dt.Columns.get("EXER").ColumnName = "Exer";
			dt.Columns.get("TAG").ColumnName = "Tag";
		}

		//把track加入里面去.
		dt.TableName = "Track";
		return dt;

			///#endregion 获取track数据.
	}
	/** 
	 获取一个流程
	 
	 @param fk_flow 流程编号
	 @param userNo 操作员编号
	 @return 
	*/
	public static DataTable DB_GenerNDxxxRpt(String fk_flow, String userNo)
	{
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Rpt WHERE FlowStarter=" + dbstr + "FlowStarter  ORDER BY RDT";
		ps.Add(GERptAttr.FlowStarter, userNo);
		return DBAccess.RunSQLReturnTable(ps);
	}

		///#endregion 获取流程事例的轨迹图


		///#region 获取能够发送或者抄送人员的列表.
	/** 
	 获取可以执行指定节点人的列表
	 
	 @param fk_node 节点编号
	 @param workid 工作ID
	 @return 
	*/
	public static DataSet DB_CanExecSpecNodeEmps(int fk_node, long workid)
	{
		DataSet ds = new DataSet();
		Paras ps = new Paras();
		ps.SQL = "SELECT No,Name,FK_Dept FROM Port_Emp ";
		DataTable dtEmp = DBAccess.RunSQLReturnTable(ps);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);

		ps = new Paras();
		ps.SQL = "SELECT No,Name FROM Port_Dept ";
		DataTable dtDept = DBAccess.RunSQLReturnTable(ps);
		dtDept.TableName = "Depts";
		ds.Tables.add(dtDept);
		return ds;
	}
	/** 
	 获得可以抄送的人员列表
	 
	 @param fk_node 节点编号
	 @param workid 工作ID
	 @return 
	*/
	public static DataSet DB_CanCCSpecNodeEmps(int fk_node, long workid)
	{
		DataSet ds = new DataSet();
		Paras ps = new Paras();
		ps.SQL = "SELECT No,Name,FK_Dept FROM Port_Emp ";
		DataTable dtEmp = DBAccess.RunSQLReturnTable(ps);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);


		ps = new Paras();
		ps.SQL = "SELECT No,Name FROM Port_Dept ";
		DataTable dtDept = DBAccess.RunSQLReturnTable(ps);
		dtDept.TableName = "Depts";
		ds.Tables.add(dtDept);

		return ds;
	}

		///#endregion 获取能够发送或者抄送人员的列表.


		///#region 获取操送列表
	/** 
	 获取指定人员的抄送列表
	 说明:可以根据这个列表生成指定用户的抄送数据.
	 
	 @param FK_Emp 人员编号,如果是null,则返回所有的.
	 @return 返回该人员的所有抄送列表,结构同表WF_CCList.
	*/
	public static DataTable DB_CCList(String FK_Emp)
	{
		Paras ps = new Paras();
		if (FK_Emp == null)
		{
			ps.SQL = "SELECT * FROM WF_CCList WHERE 1=1";
		}
		else
		{
			ps.SQL = "SELECT a.MyPK,A.Title,A.FK_Flow,A.FlowName,A.WorkID,A.Doc,A.Rec,A.RDT,A.FID,B.FK_Node,B.NodeName,B.WFSta FROM WF_CCList A, WF_GenerWorkFlow B WHERE A.CCTo=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND B.WorkID=A.WorkID";
			ps.Add("FK_Emp", FK_Emp);
		}
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("MYPK").ColumnName = "MyPK";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("DOC").ColumnName = "DOC";
			dt.Columns.get("REC").ColumnName = "REC";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("WFSTA").ColumnName = "WFSta";
		}

		return dt;
	}
	public static DataTable DB_CCList(String FK_Emp, CCSta sta)
	{
		Paras ps = new Paras();
		if (FK_Emp == null)
		{
			ps.SQL = "SELECT * FROM WF_CCList WHERE Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta";
			ps.Add("Sta", sta.getValue());
		}
		else
		{
			ps.SQL = "SELECT * FROM WF_CCList WHERE CCTo=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta";
			ps.Add("FK_Emp", FK_Emp);
			ps.Add("Sta", sta.getValue());
		}
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("MYPK").ColumnName = "MyPK";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("DOC").ColumnName = "DOC";
			dt.Columns.get("REC").ColumnName = "REC";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("FID").ColumnName = "FID";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("mypk").ColumnName = "MyPK";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("doc").ColumnName = "DOC";
			dt.Columns.get("rec").ColumnName = "REC";
			dt.Columns.get("rdt").ColumnName = "RDT";
			dt.Columns.get("fid").ColumnName = "FID";
		}
		return dt;
	}
	/** 
	 获取指定人员的抄送列表(未读)
	 
	 @param FK_Emp 人员编号,如果是null,则返回所有的.
	 @return 返回该人员的未读的抄送列表
	*/
	public static DataTable DB_CCList_UnRead(String FK_Emp)
	{
		return DB_CCList(FK_Emp, CCSta.UnRead);
	}
	/** 
	 获取指定人员的抄送列表(已读)
	 
	 @param FK_Emp 人员编号
	 @return 返回该人员的已读的抄送列表
	*/
	public static DataTable DB_CCList_Read(String FK_Emp)
	{
		return DB_CCList(FK_Emp, CCSta.Read);
	}
	/** 
	 获取指定人员的抄送列表(已删除)
	 
	 @param FK_Emp 人员编号
	 @return 返回该人员的已删除的抄送列表
	*/
	public static DataTable DB_CCList_Delete(String FK_Emp)
	{
		return DB_CCList(FK_Emp, CCSta.Del);
	}
	/** 
	 获取指定人员的抄送列表(已回复)
	 
	 @param FK_Emp 人员编号
	 @return 返回该人员的已删除的抄送列表
	*/
	public static DataTable DB_CCList_CheckOver(String FK_Emp)
	{
		return DB_CCList(FK_Emp, CCSta.CheckOver);
	}

		///#endregion


		///#region 获取当前操作员可以发起的流程集合
	/** 
	 获取指定人员能够发起流程的集合.
	 说明:利用此接口可以生成用户的发起的流程列表.
	 
	 @param userNo 操作员编号
	 @return BP.WF.Flows 可发起的流程对象集合,如何使用该方法形成发起工作列表,请参考:\WF\UC\Start.ascx
	*/
	public static Flows DB_GenerCanStartFlowsOfEntities(String userNo)
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database)
		{
			String sql = "";
			// 采用新算法.
			if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne)
			{
				sql = "SELECT FK_Flow FROM V_FlowStarter WHERE FK_Emp='" + userNo + "'";
			}
			else
			{
				sql = "SELECT FK_Flow FROM V_FlowStarterBPM WHERE FK_Emp='" + userNo + "'";
			}

			Flows fls = new Flows();
			BP.En.QueryObject qo = new BP.En.QueryObject(fls);
			qo.AddWhereInSQL("No", sql);
			qo.addAnd();
			qo.AddWhere(FlowAttr.IsCanStart, true);
			if (WebUser.getIsAuthorize())
			{
				/*如果是授权状态*/
				qo.addAnd();
				WFEmp wfEmp = new WFEmp(userNo);
				qo.AddWhereIn("No", wfEmp.getAuthorFlows());
			}
			qo.addOrderBy("FK_FlowSort", FlowAttr.Idx);
			qo.DoQuery();
			return fls;
		}


		throw new RuntimeException("@未判断的类型。");
	}
	/** 
	 获得指定人的流程发起列表
	 
	 @param userNo 发起人编号
	 @return 
	*/

	public static DataTable DB_StarFlows(String userNo)
	{
		return DB_StarFlows(userNo, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_StarFlows(string userNo, string domain = null)
	public static DataTable DB_StarFlows(String userNo, String domain)
	{
		DataTable dt = DB_GenerCanStartFlowsOfDataTable(userNo, domain);
		DataView dv = new DataView(dt);
		dv.Sort = "Idx";
		return dv.Table;
	}

	public static DataTable DB_GenerCanStartFlowsOfDataTable(String userNo)
	{
		return DB_GenerCanStartFlowsOfDataTable(userNo, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_GenerCanStartFlowsOfDataTable(string userNo, string domain = null)
	public static DataTable DB_GenerCanStartFlowsOfDataTable(String userNo, String domain)
	{
		String sql = "SELECT A.No,A.Name,a.IsBatchStart,a.FK_FlowSort,C.Name AS FK_FlowSortText,A.IsStartInMobile, A.Idx";
		sql += " FROM WF_Flow A, V_FlowStarterBPM B, WF_FlowSort C  ";
		sql += " WHERE A.No=B.FK_Flow AND A.IsCanStart=1 AND A.FK_FlowSort=C.No  AND FK_Emp='" + WebUser.getNo() + "' ";
		if (DataType.IsNullOrEmpty(domain) == false)
		{
			sql += " AND C.Domain='" + domain + "'";
		}

		sql += " ORDER BY C.Idx, A.Idx";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("ISBATCHSTART").ColumnName = "IsBatchStart";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("FK_FLOWSORTTEXT").ColumnName = "FK_FlowSortText";
			dt.Columns.get("ISSTARTINMOBILE").ColumnName = "IsStartInMobile";
			dt.Columns.get("IDX").ColumnName = "Idx";

		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("isbatchstart").ColumnName = "IsBatchStart";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
			dt.Columns.get("fk_flowsorttext").ColumnName = "FK_FlowSortText";
			dt.Columns.get("isstartinmobile").ColumnName = "IsStartInMobile";
			dt.Columns.get("idx").ColumnName = "Idx";
		}
		return dt;
	}
	public static DataTable DB_GenerCanStartFlowsTree(String userNo)
	{
		//发起.
		DataTable table = DB_GenerCanStartFlowsOfDataTable(userNo);
		table.Columns.Add("ParentNo");
		table.Columns.Add("ICON");
		String flowSort = String.format("select No,Name,ParentNo from WF_FlowSort");

		DataTable sortTable = DBAccess.RunSQLReturnTable(flowSort);
		for (DataRow row : sortTable.Rows)
		{
			DataRow newRow = table.NewRow();
			newRow.set("No", row.get("No"));
			newRow.set("Name", row.get("Name"));
			newRow.set("ParentNo", row.get("ParentNo"));
			newRow.set("ICON", "icon-tree_folder");
			table.Rows.add(newRow);
		}

		for (DataRow row : table.Rows)
		{
			if (DataType.IsNullOrEmpty(row.get("ParentNo").toString()))
			{
				row.set("ParentNo", row.get("FK_FlowSort"));
			}
			if (DataType.IsNullOrEmpty(row.get("ICON").toString()))
			{
				row.set("ICON", "icon-4");
			}
		}
		return table;
	}


	/** 
	 获取(同表单)合流点上的子线程
	 说明:如果您要想在合流点看到所有的子线程运行的状态.
	 
	 @param nodeIDOfHL 合流点ID
	 @param workid 工作ID
	 @return 与表WF_GenerWorkerList结构类同的datatable.
	*/
	public static DataTable DB_GenerHLSubFlowDtl_TB(int nodeIDOfHL, long workid)
	{
		Node nd = new Node(nodeIDOfHL);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		GenerWorkerLists wls = new GenerWorkerLists();
		QueryObject qo = new QueryObject(wls);
		qo.AddWhere(GenerWorkerListAttr.FID, wk.getOID());
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.IsEnable, 1);
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.FK_Node, nd.getFromNodes()[0].GetValByKey(NodeAttr.NodeID));

		DataTable dt = qo.DoQueryToTable();
		if (dt.Rows.size() == 1)
		{
			qo.clear();
			qo.AddWhere(GenerWorkerListAttr.FID, wk.getOID());
			qo.addAnd();
			qo.AddWhere(GenerWorkerListAttr.IsEnable, 1);
			return qo.DoQueryToTable();
		}
		return dt;
	}
	/** 
	 获取(异表单)合流点上的子线程
	 
	 @param nodeIDOfHL 合流点ID
	 @param workid 工作ID
	 @return 与表WF_GenerWorkerList结构类同的datatable.
	*/
	public static DataTable DB_GenerHLSubFlowDtl_YB(int nodeIDOfHL, long workid)
	{
		Node nd = new Node(nodeIDOfHL);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		GenerWorkerLists wls = new GenerWorkerLists();
		QueryObject qo = new QueryObject(wls);
		qo.AddWhere(GenerWorkerListAttr.FID, wk.getOID());
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.IsEnable, 1);
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.IsPass, 0);
		return qo.DoQueryToTable();
	}

		///#endregion 获取当前操作员可以发起的流程集合


		///#region 流程草稿
	/** 
	 获取当前操作员的指定流程的流程草稿数据
	 
	 @param fk_flow 流程编号
	 @return 返回草稿数据集合,列信息. OID=工作ID,Title=标题,RDT=记录日期,FK_Flow=流程编号,FID=流程ID, FK_Node=节点ID
	*/

	public static DataTable DB_GenerDraftDataTable()
	{
		return DB_GenerDraftDataTable(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_GenerDraftDataTable(string flowNo = null)
	public static DataTable DB_GenerDraftDataTable(String flowNo)
	{
		/*获取数据.*/
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		BP.DA.Paras ps = new BP.DA.Paras();
		if (flowNo == null)
		{
			ps.SQL = "SELECT WorkID,Title,FK_Flow,FlowName,RDT,FlowNote,AtPara FROM WF_GenerWorkFlow A WHERE WFState=1 AND Starter=" + dbStr + "Starter ORDER BY RDT";
			ps.Add(GenerWorkFlowAttr.Starter, WebUser.getNo());
		}
		else
		{
			ps.SQL = "SELECT WorkID,Title,FK_Flow,FlowName,RDT,FlowNote,AtPara FROM WF_GenerWorkFlow A WHERE WFState=1 AND Starter=" + dbStr + "Starter AND FK_Flow=" + dbStr + "FK_Flow ORDER BY RDT";
			ps.Add(GenerWorkFlowAttr.FK_Flow, flowNo);
			ps.Add(GenerWorkFlowAttr.Starter, WebUser.getNo());
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("rdt").ColumnName = "RDT";
			dt.Columns.get("flownote").ColumnName = "FlowNote";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("atpara").ColumnName = "AtPara";
		}
		return dt;
	}

		///#endregion 流程草稿


		///#region 我关注的流程
	/** 
	 获得我关注的流程列表
	 
	 @param flowNo 流程编号
	 @param userNo 操作员编号
	 @return 返回当前关注的流程列表.
	*/

	public static DataTable DB_Focus(String flowNo)
	{
		return DB_Focus(flowNo, null);
	}

	public static DataTable DB_Focus()
	{
		return DB_Focus(null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_Focus(string flowNo = null, string userNo = null)
	public static DataTable DB_Focus(String flowNo, String userNo)
	{
		if (flowNo.equals(""))
		{
			flowNo = null;
		}

		if (userNo == null)
		{
			userNo = WebUser.getNo();
		}

		//执行sql.
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM WF_GenerWorkFlow WHERE AtPara LIKE  '%F_" + userNo + "=1%'";
		if (flowNo != null)
		{
			ps.SQL = "SELECT * FROM WF_GenerWorkFlow WHERE AtPara LIKE  '%F_" + userNo + "=1%' AND FK_Flow=" + SystemConfig.getAppCenterDBVarStr() + "FK_Flow";
			ps.Add("FK_Flow", flowNo);
		}
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		//添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("WFSTA").ColumnName = "WFSta";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("FK_NY").ColumnName = "FK_NY";
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("PRI").ColumnName = "PRI";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("SDTOFFLOW").ColumnName = "SDTOfFlow";
			dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";
			dt.Columns.get("PNODEID").ColumnName = "PNodeID";
			dt.Columns.get("PFID").ColumnName = "PFID";
			dt.Columns.get("PEMP").ColumnName = "PEmp";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("EMPS").ColumnName = "Emps";
			dt.Columns.get("GUID").ColumnName = "GUID";
			dt.Columns.get("WEEKNUM").ColumnName = "WeekNum";
			dt.Columns.get("TSPAN").ColumnName = "TSpan";
			dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt.Columns.get("SYSTYPE").ColumnName = "SysType";

			// dt.Columns.get("CFLOWNO").ColumnName = "CFlowNo";
			// dt.Columns.get("CWORKID").ColumnName = "CWorkID";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("startername").ColumnName = "StarterName";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("wfsta").ColumnName = "WFSta";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("rdt").ColumnName = "RDT";
			dt.Columns.get("billno").ColumnName = "BillNo";
			dt.Columns.get("flownote").ColumnName = "FlowNote";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
			dt.Columns.get("fid").ColumnName = "FID";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("wfstate").ColumnName = "WFState";
			dt.Columns.get("fk_ny").ColumnName = "FK_NY";
			dt.Columns.get("mynum").ColumnName = "MyNum";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("deptname").ColumnName = "DeptName";
			dt.Columns.get("pri").ColumnName = "PRI";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("sdtofflow").ColumnName = "SDTOfFlow";
			dt.Columns.get("pflowno").ColumnName = "PFlowNo";
			dt.Columns.get("pworkid").ColumnName = "PWorkID";
			dt.Columns.get("pnodeid").ColumnName = "PNodeID";
			dt.Columns.get("pfid").ColumnName = "PFID";
			dt.Columns.get("pemp").ColumnName = "PEmp";
			dt.Columns.get("guestno").ColumnName = "GuestNo";
			dt.Columns.get("guestname").ColumnName = "GuestName";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
			dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
			dt.Columns.get("tasksta").ColumnName = "TaskSta";
			dt.Columns.get("atpara").ColumnName = "AtPara";
			dt.Columns.get("emps").ColumnName = "Emps";
			dt.Columns.get("guid").ColumnName = "GUID";
			dt.Columns.get("weeknum").ColumnName = "WeekNum";
			dt.Columns.get("tspan").ColumnName = "TSpan";
			dt.Columns.get("todosta").ColumnName = "TodoSta";
			dt.Columns.get("systype").ColumnName = "SysType";

			// dt.Columns.get("CFLOWNO").ColumnName = "CFlowNo";
			// dt.Columns.get("CWORKID").ColumnName = "CWorkID";
		}
		return dt;
	}

		///#endregion 我关注的流程


		///#region 获取当前操作员的共享工作

	public static DataTable DB_Todolist(String userNo)
	{
		return DB_Todolist(userNo, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_Todolist(string userNo, int fk_node = 0)
	public static DataTable DB_Todolist(String userNo, int fk_node)
	{
		String sql = "";
		sql = "SELECT A.* FROM WF_GenerWorkFlow A, WF_FlowSort B, WF_Flow C, WF_GENERWORKERLIST D ";
		sql += " WHERE (WFState=2 OR WFState=5 OR WFState=8)";
		sql += " AND A.FK_FlowSort=B.No ";
		sql += " AND A.FK_Flow=C.No ";
		sql += " AND A.FK_Node=D.FK_Node ";
		sql += " AND A.WorkID=D.WorkID ";
		sql += " AND D.IsPass=0  "; // = 90 是会签主持人.
		sql += " AND D.FK_Emp='" + userNo + "'";

		if (fk_node != 0)
		{
			sql += " AND A.FK_Node=" + fk_node;
		}

		sql += "  ORDER BY  B.Idx, C.Idx, A.RDT DESC ";


		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		//添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("PRI").ColumnName = "PRI";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			// dt.Columns.get("ISREAD").ColumnName = "IsRead";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";
			dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("pri").ColumnName = "PRI";
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("title").ColumnName = "Title";
			// dt.Columns.get("ISREAD").ColumnName = "IsRead";
			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("startername").ColumnName = "StarterName";
			dt.Columns.get("wfstate").ColumnName = "WFState";
			dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
			dt.Columns.get("deptname").ColumnName = "DeptName";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("pworkid").ColumnName = "PWorkID";
			dt.Columns.get("pflowno").ColumnName = "PFlowNo";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("fid").ColumnName = "FID";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
			dt.Columns.get("systype").ColumnName = "SysType";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("guestno").ColumnName = "GuestNo";
			dt.Columns.get("guestname").ColumnName = "GuestName";
			dt.Columns.get("billno").ColumnName = "BillNo";
			dt.Columns.get("flownote").ColumnName = "FlowNote";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
			dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
			dt.Columns.get("todosta").ColumnName = "TodoSta";
			dt.Columns.get("tasksta").ColumnName = "TaskSta";
			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("atpara").ColumnName = "AtPara";
			dt.Columns.get("mynum").ColumnName = "MyNum";
		}

		return dt;
	}
	/** 
	 获得会签列表
	 
	 @param userNo 人员编号
	 @return 
	*/
	public static DataTable DB_HuiQian(String userNo)
	{

		return null;
		//WebUser.SignInOfGener2017
	}
	/** 
	 获取当前人员待处理的工作
	 
	 @param fk_node 节点编号
	 @return 共享工作列表
	*/

	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, int fk_node)
	{
		return DB_GenerEmpWorksOfDataTable(userNo, fk_node, null);
	}

	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo)
	{
		return DB_GenerEmpWorksOfDataTable(userNo, 0, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_GenerEmpWorksOfDataTable(string userNo, int fk_node = 0, string showWhat = null)
	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, int fk_node, String showWhat)
	{
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			throw new RuntimeException("err@登录信息丢失.");
		}
		String wfStateSql = "";
		if (DataType.IsNullOrEmpty(showWhat) == true)
		{
			wfStateSql = " A.WFState!=" + WFState.Batch.getValue();
		}
		else
		{
			wfStateSql = "  A.WFState=" + showWhat;
		}
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;
		if (WebUser.getIsAuthorize() == false)
		{
			/*不是授权状态*/
			if (fk_node == 0)
			{
				if (BP.Sys.SystemConfig.getCustomerNo().equals("TianYe"))
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT A.* FROM BPM.WF_EmpWorks A, BPM.WF_Flow B, BPM.WF_FlowSort C WHERE A.FK_Flow=B.No AND B.FK_FlowSort=C.No AND A.FK_Emp=" + dbstr + "FK_Emp AND A.TaskSta=0 AND " + wfStateSql + " ORDER BY C.Idx, B.Idx, ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT A.* FROM BPM.WF_EmpWorks A, BPM.WF_Flow B, BPM.WF_FlowSort C WHERE A.FK_Flow=B.No AND B.FK_FlowSort=C.No AND A.FK_Emp=" + dbstr + "FK_Emp  AND " + wfStateSql + " ORDER BY C.Idx,B.Idx, A.ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks A WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND " + wfStateSql + " ORDER BY  ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks A WHERE FK_Emp=" + dbstr + "FK_Emp  AND " + wfStateSql + " ORDER BY ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
				}
			}
			else
			{
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks A WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND FK_Node=" + dbstr + "FK_Node  AND " + wfStateSql + " ORDER BY  ADT DESC ";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks A WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Node=" + dbstr + "FK_Node  AND " + wfStateSql + " ORDER BY  ADT DESC ";
				}

				ps.Add("FK_Node", fk_node);
				ps.Add("FK_Emp", userNo);
			}
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
			//添加oracle的处理
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
			{
				dt.Columns.get("PRI").ColumnName = "PRI";
				dt.Columns.get("WORKID").ColumnName = "WorkID";
				dt.Columns.get("ISREAD").ColumnName = "IsRead";
				dt.Columns.get("STARTER").ColumnName = "Starter";
				dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
				dt.Columns.get("WFSTATE").ColumnName = "WFState";
				dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
				dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
				dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
				dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
				dt.Columns.get("PWORKID").ColumnName = "PWorkID";
				dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
				dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
				dt.Columns.get("NODENAME").ColumnName = "NodeName";
				dt.Columns.get("WORKERDEPT").ColumnName = "WorkerDept";
				dt.Columns.get("TITLE").ColumnName = "Title";
				dt.Columns.get("RDT").ColumnName = "RDT";
				dt.Columns.get("ADT").ColumnName = "ADT";
				dt.Columns.get("SDT").ColumnName = "SDT";
				dt.Columns.get("FK_EMP").ColumnName = "FK_Emp";
				dt.Columns.get("FID").ColumnName = "FID";
				dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
				dt.Columns.get("SYSTYPE").ColumnName = "SysType";
				dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
				dt.Columns.get("PRESSTIMES").ColumnName = "PressTimes";
				dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
				dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
				dt.Columns.get("BILLNO").ColumnName = "BillNo";
				dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
				dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
				dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
				dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
				dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
				dt.Columns.get("LISTTYPE").ColumnName = "ListType";
				dt.Columns.get("SENDER").ColumnName = "Sender";
				dt.Columns.get("ATPARA").ColumnName = "AtPara";
				dt.Columns.get("MYNUM").ColumnName = "MyNum";
			}

			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
				dt.Columns.get("pri").ColumnName = "PRI";
				dt.Columns.get("workid").ColumnName = "WorkID";
				dt.Columns.get("isread").ColumnName = "IsRead";
				dt.Columns.get("starter").ColumnName = "Starter";
				dt.Columns.get("startername").ColumnName = "StarterName";
				dt.Columns.get("wfstate").ColumnName = "WFState";
				dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
				dt.Columns.get("deptname").ColumnName = "DeptName";
				dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
				dt.Columns.get("flowname").ColumnName = "FlowName";
				dt.Columns.get("pworkid").ColumnName = "PWorkID";
				dt.Columns.get("pflowno").ColumnName = "PFlowNo";
				dt.Columns.get("fk_node").ColumnName = "FK_Node";
				dt.Columns.get("nodename").ColumnName = "NodeName";
				dt.Columns.get("workerdept").ColumnName = "WorkerDept";
				dt.Columns.get("title").ColumnName = "Title";
				dt.Columns.get("rdt").ColumnName = "RDT";
				dt.Columns.get("adt").ColumnName = "ADT";
				dt.Columns.get("sdt").ColumnName = "SDT";
				dt.Columns.get("fk_emp").ColumnName = "FK_Emp";
				dt.Columns.get("fid").ColumnName = "FID";
				dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
				dt.Columns.get("systype").ColumnName = "SysType";
				dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
				dt.Columns.get("presstimes").ColumnName = "PressTimes";
				dt.Columns.get("guestno").ColumnName = "GuestNo";
				dt.Columns.get("guestname").ColumnName = "GuestName";
				dt.Columns.get("billno").ColumnName = "BillNo";
				dt.Columns.get("flownote").ColumnName = "FlowNote";
				dt.Columns.get("todoemps").ColumnName = "TodoEmps";
				dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
				dt.Columns.get("todosta").ColumnName = "TodoSta";
				dt.Columns.get("tasksta").ColumnName = "TaskSta";
				dt.Columns.get("listtype").ColumnName = "ListType";
				dt.Columns.get("sender").ColumnName = "Sender";
				dt.Columns.get("atpara").ColumnName = "AtPara";
				dt.Columns.get("mynum").ColumnName = "MyNum";
			}
			return dt;
		}

		/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				if (fk_node == 0)
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND TaskSta=0 ORDER BY ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  ORDER BY ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Node" + dbstr + "FK_Node AND TaskSta=0 ORDER BY ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Node" + dbstr + "FK_Node ORDER BY ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
					ps.Add("FK_Node", fk_node);
				}
				break;
			case SpecFlows:
				if (fk_node == 0)
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta=0 ORDER BY ADT DESC ";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Node" + dbstr + "FK_Node AND FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta=0  ORDER BY ADT DESC ";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Node" + dbstr + "FK_Node AND FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
					ps.Add("FK_Node", fk_node);
				}
				break;
			case None:
				throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}
		DataTable dt2 = BP.DA.DBAccess.RunSQLReturnTable(ps);
		//添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt2.Columns.get("PRI").ColumnName = "PRI";
			dt2.Columns.get("WORKID").ColumnName = "WorkID";
			dt2.Columns.get("ISREAD").ColumnName = "IsRead";
			dt2.Columns.get("STARTER").ColumnName = "Starter";
			dt2.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt2.Columns.get("WFSTATE").ColumnName = "WFState";
			dt2.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt2.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt2.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt2.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt2.Columns.get("PWORKID").ColumnName = "PWorkID";
			dt2.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt2.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt2.Columns.get("NODENAME").ColumnName = "NodeName";
			dt2.Columns.get("WORKERDEPT").ColumnName = "WorkerDept";
			dt2.Columns.get("TITLE").ColumnName = "Title";
			dt2.Columns.get("RDT").ColumnName = "RDT";
			dt2.Columns.get("ADT").ColumnName = "ADT";
			dt2.Columns.get("SDT").ColumnName = "SDT";
			dt2.Columns.get("FK_EMP").ColumnName = "FK_Emp";
			dt2.Columns.get("FID").ColumnName = "FID";
			dt2.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt2.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt2.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt2.Columns.get("PRESSTIMES").ColumnName = "PressTimes";
			dt2.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt2.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt2.Columns.get("BILLNO").ColumnName = "BillNo";
			dt2.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt2.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt2.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt2.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt2.Columns.get("TASKSTA").ColumnName = "TaskSta";
			dt2.Columns.get("LISTTYPE").ColumnName = "ListType";
			dt2.Columns.get("SENDER").ColumnName = "Sender";
			dt2.Columns.get("ATPARA").ColumnName = "AtPara";
			dt2.Columns.get("MYNUM").ColumnName = "MyNum";
		}

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt2.Columns.get("pri").ColumnName = "PRI";
			dt2.Columns.get("workid").ColumnName = "WorkID";
			dt2.Columns.get("isread").ColumnName = "IsRead";
			dt2.Columns.get("starter").ColumnName = "Starter";
			dt2.Columns.get("startername").ColumnName = "StarterName";
			dt2.Columns.get("wfstate").ColumnName = "WFState";
			dt2.Columns.get("fk_dept").ColumnName = "FK_Dept";
			dt2.Columns.get("deptname").ColumnName = "DeptName";
			dt2.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt2.Columns.get("flowname").ColumnName = "FlowName";
			dt2.Columns.get("pworkid").ColumnName = "PWorkID";
			dt2.Columns.get("pflowno").ColumnName = "PFlowNo";
			dt2.Columns.get("fk_node").ColumnName = "FK_Node";
			dt2.Columns.get("nodename").ColumnName = "NodeName";
			dt2.Columns.get("workerdept").ColumnName = "WorkerDept";
			dt2.Columns.get("title").ColumnName = "Title";
			dt2.Columns.get("rdt").ColumnName = "RDT";
			dt2.Columns.get("adt").ColumnName = "ADT";
			dt2.Columns.get("sdt").ColumnName = "SDT";
			dt2.Columns.get("fk_emp").ColumnName = "FK_Emp";
			dt2.Columns.get("fid").ColumnName = "FID";
			dt2.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
			dt2.Columns.get("systype").ColumnName = "SysType";
			dt2.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt2.Columns.get("presstimes").ColumnName = "PressTimes";
			dt2.Columns.get("guestno").ColumnName = "GuestNo";
			dt2.Columns.get("guestname").ColumnName = "GuestName";
			dt2.Columns.get("billno").ColumnName = "BillNo";
			dt2.Columns.get("flownote").ColumnName = "FlowNote";
			dt2.Columns.get("todoemps").ColumnName = "TodoEmps";
			dt2.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
			dt2.Columns.get("todosta").ColumnName = "TodoSta";
			dt2.Columns.get("tasksta").ColumnName = "TaskSta";
			dt2.Columns.get("listtype").ColumnName = "ListType";
			dt2.Columns.get("sender").ColumnName = "Sender";
			dt2.Columns.get("atpara").ColumnName = "AtPara";
			dt2.Columns.get("mynum").ColumnName = "MyNum";
		}
		return dt2;
	}
	/** 
	 获取当前人员待处理的工作
	 
	 @param fk_flow 流程编号
	 @param fk_node 节点编号
	 @return 共享工作列表
	*/

	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo)
	{
		return DB_GenerEmpWorksOfDataTable(userNo, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_GenerEmpWorksOfDataTable(string userNo, string fk_flow = null)
	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, String fk_flow)
	{

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;
		if (WebUser.getIsAuthorize() == false)
		{
			/*不是授权状态*/
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND WFState!=" + WFState.Batch.getValue() + " ORDER BY FK_Flow,ADT DESC ";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			}
			else
			{
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND FK_Flow=" + dbstr + "FK_Flow  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY  ADT DESC ";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Flow=" + dbstr + "FK_Flow  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY  ADT DESC ";
				}

				ps.Add("FK_Flow", fk_flow);
				ps.Add("FK_Emp", userNo);
			}
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				if (DataType.IsNullOrEmpty(fk_flow))
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Flow" + dbstr + "FK_Flow AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Flow" + dbstr + "FK_Flow ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
					ps.Add("FK_Flow", fk_flow);
				}
				break;
			case SpecFlows:
				if (DataType.IsNullOrEmpty(fk_flow))
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Flow" + dbstr + "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Flow" + dbstr + "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("FK_Emp", userNo);
					ps.Add("FK_Flow", fk_flow);
				}
				break;
			case None:
				throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 根据状态获取当前操作员的共享工作
	 
	 @param wfState 流程状态
	 @param fk_flow 流程编号
	 @return 表结构与视图WF_EmpWorks一致
	*/
	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, WFState wfState, String fk_flow)
	{

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;
		if (WebUser.getIsAuthorize() == false)
		{
			/*不是授权状态*/
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0   ORDER BY FK_Flow,ADT DESC ";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Emp", userNo);
			}
			else
			{
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND FK_Flow=" + dbstr + "FK_Flow AND TaskSta=0  ORDER BY  ADT DESC ";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND FK_Flow=" + dbstr + "FK_Flow ORDER BY  ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Flow", fk_flow);
				ps.Add("FK_Emp", userNo);
			}
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				if (DataType.IsNullOrEmpty(fk_flow))
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp  AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("WFState", wfState.getValue());
					ps.Add("FK_Emp", WebUser.getNo());
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND FK_Flow" + dbstr + "FK_Flow AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND FK_Flow" + dbstr + "FK_Flow ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("WFState", wfState.getValue());
					ps.Add("FK_Emp", WebUser.getNo());
					ps.Add("FK_Flow", fk_flow);
				}
				break;
			case SpecFlows:
				if (DataType.IsNullOrEmpty(fk_flow))
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta=0   ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("WFState", wfState.getValue());
					ps.Add("FK_Emp", WebUser.getNo());
				}
				else
				{
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp  AND FK_Flow" + dbstr + "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta=0   ORDER BY FK_Flow,ADT DESC ";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr + "FK_Emp  AND FK_Flow" + dbstr + "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
					}

					ps.Add("WFState", wfState.getValue());
					ps.Add("FK_Emp", WebUser.getNo());
					ps.Add("FK_Flow", fk_flow);
				}
				break;
			case None:
				throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获得待办(包括被授权的待办)
	 区分是自己的待办，还是被授权的待办通过数据源的 FK_Emp 字段来区分。
	 
	 @return 
	*/

	public static DataTable DB_Todolist()
	{
		return DB_Todolist(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_Todolist(string userNo = null)
	public static DataTable DB_Todolist(String userNo)
	{
		if (userNo == null)
		{
			userNo = WebUser.getNo();
			if (WebUser.getIsAuthorize() == false)
			{
				throw new RuntimeException("@授权登录的模式下不能调用此接口.");
			}
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
		/*不是授权状态*/
		if (BP.WF.Glo.getIsEnableTaskPool() == true)
		{
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1 ";
		}
		else
		{
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
		}

		ps.Add("FK_Emp", userNo);

		//获取授权给他的人员列表.
		WFEmps emps = new WFEmps();
		emps.Retrieve(WFEmpAttr.Author, userNo);
		for (WFEmp emp : emps.ToJavaList())
		{
			switch (emp.getHisAuthorWay())
			{
				case All:
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL += " UNION  SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' AND TaskSta!=1  ";
					}
					else
					{
						ps.SQL += " UNION  SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' ";
					}

					break;
				case SpecFlows:
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						ps.SQL += " UNION SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0 ";
					}
					else
					{
						ps.SQL += " UNION SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' AND  FK_Flow IN " + emp.getAuthorFlows() + "  ";
					}

					break;
				case None: //非授权状态下.
					continue;
				default:
					throw new RuntimeException("no such way...");
			}
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取当前操作人员的待办信息
	 数据内容请参考图:WF_EmpWorks
	 
	 @return 返回从视图WF_EmpWorks查询出来的数据.
	*/
	public static DataTable DB_GenerEmpWorksOfDataTable()
	{
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
		String sql;

		if (WebUser.getIsAuthorize() == false)
		{
			/*不是授权状态*/
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC";
			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1  ORDER BY ADT DESC";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC";
				}

				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0    ORDER BY ADT DESC";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "   ORDER BY ADT DESC";
				}

				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
				/*不是授权状态*/
				if (BP.WF.Glo.getIsEnableTaskPool() == true)
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1  ORDER BY ADT DESC";
				}
				else
				{
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC";
				}

				ps.Add("FK_Emp", WebUser.getNo());
				return BP.DA.DBAccess.RunSQLReturnTable(ps);

				WebUser.Auth = null; //对方已经取消了授权.
			default:
				throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获得已完成数据统计列表
	 
	 @param userNo 操作员编号
	 @return 具有FlowNo,FlowName,Num三个列的指定人员的待办列表
	*/
	public static DataTable DB_FlowCompleteGroup(String userNo)
	{
		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT FK_Flow as No,FlowName,COUNT(*) Num FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + userNo + "@%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " GROUP BY FK_Flow,FlowName";
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取指定页面已经完成流程
	 
	 @param userNo 用户编号
	 @param flowNo 流程编号
	 @param pageSize 每页的数量
	 @param pageIdx 第几页
	 @return 用户编号
	*/
	public static DataTable DB_FlowComplete(String userNo, String flowNo, int pageSize, int pageIdx)
	{
		/* 如果不是删除流程注册表. */
		GenerWorkFlows ens = new GenerWorkFlows();
		QueryObject qo = new QueryObject(ens);
		if (flowNo != null)
		{
			qo.AddWhere(GenerWorkFlowAttr.FK_Flow, flowNo);
			qo.addAnd();
		}
		qo.AddWhere(GenerWorkFlowAttr.FID, 0);
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", " '%@" + userNo + "@%'");
		/**小周鹏修改-----------------------------START**/
		// qo.DoQuery(GenerWorkFlowAttr.WorkID,pageSize, pageIdx);
		qo.DoQuery(GenerWorkFlowAttr.WorkID, pageSize, pageIdx, GenerWorkFlowAttr.RDT, true);
		/**小周鹏修改-----------------------------END**/
		return ens.ToDataTableField();

	}
	/** 
	 查询指定流程中已完成的流程
	 
	 @param userNo
	 @param pageCount
	 @param pageSize
	 @param pageIdx
	 @param strFlow
	 @return 
	*/
	public static DataTable DB_FlowComplete(String userNo, int pageCount, int pageSize, int pageIdx, String strFlow)
	{

		/* 如果不是删除流程注册表. */
		GenerWorkFlows ens = new GenerWorkFlows();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere(GenerWorkFlowAttr.FID, 0);
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", " '%@" + userNo + "@%'");
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.FK_Flow, strFlow);
		qo.DoQuery(GenerWorkFlowAttr.WorkID, pageSize, pageIdx);
		return ens.ToDataTableField();

	}
	/** 
	 查询指定流程中已完成的公告流程
	 
	 @param pageCount 页数
	 @param pageSize 每页条数
	 @param pageIdx 页码
	 @param strFlow 流程编号
	 @return 
	 * @throws Exception 
	*/
	public static DataTable DB_FlowComplete(String strFlow, int pageSize, int pageIdx) throws Exception
	{

		/* 如果不是删除流程注册表. */
		GenerWorkFlows ens = new GenerWorkFlows();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere(GenerWorkFlowAttr.FID, 0);
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.FK_Flow, strFlow);
		qo.DoQuery(GenerWorkFlowAttr.WorkID, pageSize, pageIdx);
		return ens.ToDataTableField();

	}
	/** 
	 获取已经完成流程
	 
	 @return 
	*/
	public static DataTable DB_TongJi_FlowComplete()
	{

		DataTable dt = null;

		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT T.FK_Flow, T.FlowName, COUNT(T.WorkID) as Num FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@" + WebUser.getNo() + "@%' AND T.FID=0 AND T.WFSta=" + WFSta.Complete.getValue() + " GROUP BY T.FK_Flow,T.FlowName";
		dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("NUM").ColumnName = "Num";
		}

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("num").ColumnName = "Num";
		}

		return dt;

	}
	/** 
	 获取已经完成流程
	 
	 @return 
	*/
	public static DataTable DB_FlowComplete()
	{
		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@" + WebUser.getNo() + "@%' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

		//@史连雨,需要翻译.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("TYPE").ColumnName = "Type";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("TITLE").ColumnName = "Title";

			dt.Columns.get("WFSTA").ColumnName = "WFSta";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";

			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("SDTOFFLOW").ColumnName = "SDTOfFlow";
			dt.Columns.get("PFLOWNO").ColumnName = "PflowNo";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";

			dt.Columns.get("PNODEID").ColumnName = "PNodeID";
			dt.Columns.get("PEMP").ColumnName = "PEmp";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";

			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("EMPS").ColumnName = "Emps";
			dt.Columns.get("DOMAIN").ColumnName = "Domain";
			dt.Columns.get("SENDDT").ColumnName = "SendDT";
			dt.Columns.get("WEEKNUM").ColumnName = "WeekNum";
		}
		return dt;
	}
	/** 
	 获取某一个人已完成的工作
	 
	 @param userNo
	 @return 
	*/
	public static DataTable DB_FlowComplete(String userNo)
	{

		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@" + userNo + "@%' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
		return BP.DA.DBAccess.RunSQLReturnTable(ps);

	}
	/** 
	 获取某一个人某个流程已完成的工作
	 
	 @param userNo
	 @return 
	*/
	public static DataTable DB_FlowComplete(String userNo, String flowNo)
	{

		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@" + userNo + "@%' AND T.FK_Flow='" + flowNo + "' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取已经完成
	 
	 @return 
	*/
	public static DataTable DB_FlowCompleteAndCC()
	{
		DataTable dt = DB_FlowComplete();
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());

		try
		{
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		}
		catch (RuntimeException e)
		{

		}

		for (DataRow row : ccDT.Rows)
		{
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns)
			{
				for (DataColumn dtColumn : dt.Columns)
				{
					if (column.ColumnName.equals(dtColumn.ColumnName))
					{
						newRow.set(column.ColumnName, row.get(dtColumn.ColumnName));
					}

				}

			}
			newRow.set("Type", "CC");
			dt.Rows.add(newRow);
		}
		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}
	public static DataTable DB_FlowComplete2(String fk_flow, String title)
	{

		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		if (DataType.IsNullOrEmpty(fk_flow))
		{
			if (DataType.IsNullOrEmpty(title))
			{
				ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " and FK_Flow!='010' order by RDT desc";
			}
			else
			{
				ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' and Title Like '%" + title + "%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " and FK_Flow!='010' order by RDT desc";
			}
		}
		else
		{
			if (DataType.IsNullOrEmpty(title))
			{
				ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " and FK_Flow='" + fk_flow + "' order by RDT desc";
			}
			else
			{
				ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' and Title Like '%" + title + "%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " and FK_Flow='" + fk_flow + "' order by RDT desc";
			}
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);

	}

	public static DataTable DB_FlowCompleteAndCC2(String fk_flow, String title)
	{
		DataTable dt = DB_FlowComplete2(fk_flow, title);
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());
		try
		{
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		}
		catch (RuntimeException e)
		{

		}

		for (DataRow row : ccDT.Rows)
		{
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns)
			{
				for (DataColumn dtColumn : dt.Columns)
				{
					if (column.ColumnName.equals(dtColumn.ColumnName))
					{
						newRow.set(column.ColumnName, row.get(dtColumn.ColumnName));
					}

				}

			}
			newRow.set("Type", "CC");
			dt.Rows.add(newRow);
		}
		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}
	/** 
	 获得任务池的工作列表
	 
	 @return 任务池的工作列表
	 * @throws Exception 
	*/
	public static DataTable DB_TaskPool() throws Exception
	{
		if (BP.WF.Glo.getIsEnableTaskPool() == false)
		{
			throw new RuntimeException("@你必须在Web.config中启用IsEnableTaskPool才可以执行此操作。");
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + ") AND TaskSta=" + TaskSta.Sharing.getValue();
		String sql;
		String realSql = null;
		if (WebUser.getIsAuthorize() == false)
		{
			/*不是授权状态*/
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " ";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
				throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}
		//@杜. 这里需要翻译.
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("ISREAD").ColumnName = "IsRead";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";

			dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("WORKERDEPT").ColumnName = "WorkerDept";
			dt.Columns.get("FK_EMP").ColumnName = "FK_Emp";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";

			dt.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";

			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";

			dt.Columns.get("LISTTYPE").ColumnName = "ListType";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
		}

		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("isread").ColumnName = "IsRead";
			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("startername").ColumnName = "StarterName";
			dt.Columns.get("wfstate").ColumnName = "WFState";
			dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
			dt.Columns.get("deptname").ColumnName = "DeptName";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("pworkid").ColumnName = "PWorkID";

			dt.Columns.get("pflowno").ColumnName = "PFlowNo";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("workerdept").ColumnName = "WorkerDept";
			dt.Columns.get("fk_emp").ColumnName = "FK_Emp";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";

			dt.Columns.get("systype").ColumnName = "SysType";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("guestno").ColumnName = "GuestNo";
			dt.Columns.get("guestname").ColumnName = "GuestName";
			dt.Columns.get("billno").ColumnName = "BillNo";

			dt.Columns.get("flownote").ColumnName = "FlowNote";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
			dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
			dt.Columns.get("todosta").ColumnName = "TodoSta";
			dt.Columns.get("tasksta").ColumnName = "TaskSta";

			dt.Columns.get("listtype").ColumnName = "ListType";
			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("atpara").ColumnName = "AtPara";
			dt.Columns.get("mynum").ColumnName = "MyNum";
		}


		return dt;
	}
	/** 
	 获得我从任务池里申请下来的工作列表
	 
	 @return 
	*/
	public static DataTable DB_TaskPoolOfMyApply()
	{
		if (BP.WF.Glo.getIsEnableTaskPool() == false)
		{
			throw new RuntimeException("@你必须在Web.config中启用IsEnableTaskPool才可以执行此操作。");
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + ") AND TaskSta=" + TaskSta.Takeback.getValue();
		String sql;
		String realSql;
		if (WebUser.getIsAuthorize() == false)
		{
			/*不是授权状态*/
			// ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY FK_Flow,ADT DESC ";
			//ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC ";
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp";

			// ps.SQL = "select v1.*,v2.name,v3.name as ParentName from (" + realSql + ") as v1 left join JXW_Inc v2 on v1.WorkID=v2.OID left join Jxw_Inc V3 on v1.PWorkID = v3.OID ORDER BY v1.ADT DESC";

			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		/*如果是授权状态, 获取当前委托人的信息. */
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay())
		{
			case All:
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case SpecFlows:
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "";
				ps.Add("FK_Emp", WebUser.getNo());
				break;
			case None:
				throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
			default:
				throw new RuntimeException("no such way...");
		}

		//@杜. 这里需要翻译.
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("ISREAD").ColumnName = "IsRead";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";

			dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("WORKERDEPT").ColumnName = "WorkerDept";
			dt.Columns.get("FK_EMP").ColumnName = "FK_Emp";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";

			dt.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";

			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";

			dt.Columns.get("LISTTYPE").ColumnName = "ListType";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
		}

		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("isread").ColumnName = "IsRead";
			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("startername").ColumnName = "StarterName";
			dt.Columns.get("wfstate").ColumnName = "WFState";
			dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
			dt.Columns.get("deptname").ColumnName = "DeptName";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("pworkid").ColumnName = "PWorkID";

			dt.Columns.get("pflowno").ColumnName = "PFlowNo";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("workerdept").ColumnName = "WorkerDept";
			dt.Columns.get("fk_emp").ColumnName = "FK_Emp";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";

			dt.Columns.get("systype").ColumnName = "SysType";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("guestno").ColumnName = "GuestNo";
			dt.Columns.get("guestname").ColumnName = "GuestName";
			dt.Columns.get("billno").ColumnName = "BillNo";

			dt.Columns.get("flownote").ColumnName = "FlowNote";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
			dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
			dt.Columns.get("todosta").ColumnName = "TodoSta";
			dt.Columns.get("tasksta").ColumnName = "TaskSta";

			dt.Columns.get("listtype").ColumnName = "ListType";
			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("atpara").ColumnName = "AtPara";
			dt.Columns.get("mynum").ColumnName = "MyNum";
		}

		return dt;
	}
	/** 
	 获得所有的流程挂起工作列表
	 
	 @return 返回从视图WF_EmpWorks查询出来的数据.
	*/
	public static DataTable DB_GenerHungUpList()
	{
		return DB_GenerHungUpList(null);
	}
	/** 
	 获得指定流程挂起工作列表
	 
	 @param fk_flow 流程编号,如果编号为空则返回所有的流程挂起工作列表.
	 @return 返回从视图WF_EmpWorks查询出来的数据.
	*/
	public static DataTable DB_GenerHungUpList(String fk_flow)
	{
		String sql;
		int state = WFState.HungUp.getValue();
		if (WebUser.getIsAuthorize())
		{
			WFEmp emp = new WFEmp(WebUser.getNo());
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
			else
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.FK_Flow='" + fk_flow + "' AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND  B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
		}
		else
		{
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1   ";
			}
			else
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 ";
			}
		}
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}
	/** 
	 获得逻辑删除的流程
	 
	 @return 返回从视图WF_EmpWorks查询出来的数据.
	*/
	public static DataTable DB_GenerDeleteWorkList()
	{
		return DB_GenerDeleteWorkList(WebUser.getNo(), null);
	}
	/** 
	 获得逻辑删除的流程:根据流程编号
	 
	 @param userNo 操作员编号
	 @param fk_flow 流程编号(可以为空)
	 @return WF_GenerWorkFlow数据结构的集合
	*/
	public static DataTable DB_GenerDeleteWorkList(String userNo, String fk_flow)
	{
		String sql;
		int state = WFState.Delete.getValue();
		if (WebUser.getIsAuthorize())
		{
			WFEmp emp = new WFEmp(WebUser.getNo());
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
			else
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND  B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
		}
		else
		{
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1   ";
			}
			else
			{
				sql = "SELECT A.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 ";
			}
		}
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}

		///#endregion 获取当前操作员的共享工作


		///#region 获取流程数据
	/** 
	 根据流程状态获取指定流程数据
	 
	 @param fk_flow 流程编号
	 @param sta 流程状态
	 @return 数据表OID,Title,RDT,FID
	*/
	public static DataTable DB_NDxxRpt(String fk_flow, WFState sta)
	{
		Flow fl = new Flow(fk_flow);
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql = "SELECT OID,Title,RDT,FID FROM " + fl.getPTable() + " WHERE WFState=" + sta.getValue() + " AND Rec=" + dbstr + "Rec";
		BP.DA.Paras ps = new BP.DA.Paras();
		ps.SQL = sql;
		ps.Add("Rec", WebUser.getNo());
		return DBAccess.RunSQLReturnTable(ps);
	}

		///#endregion


		///#region 工作部件的数据源获取。
	/** 
	 获取当前节点可以退回的节点
	 
	 @param fk_node 节点ID
	 @param workid 工作ID
	 @param fid FID
	 @return No节点编号,Name节点名称,Rec记录人,RecName记录人名称
	*/
	public static DataTable DB_GenerWillReturnNodes(int fk_node, long workid, long fid)
	{
		DataTable dt = new DataTable("obt");
		dt.Columns.Add("No", String.class); // 节点ID
		dt.Columns.Add("Name", String.class); // 节点名称.
		dt.Columns.Add("Rec", String.class); // 被退回节点上的操作员编号.
		dt.Columns.Add("RecName", String.class); // 被退回节点上的操作员名称.
		dt.Columns.Add("IsBackTracking", String.class); // 该节点是否可以退回并原路返回？ 0否, 1是.
		dt.Columns.Add("AtPara", String.class); // 该节点是否可以退回并原路返回？ 0否, 1是.

		Node nd = new Node(fk_node);

		//增加退回到父流程节点的设计.
		if (nd.getIsStartNode() == true)
		{
			/*如果是开始的节点有可能退回到子流程上去.*/
			GenerWorkFlow gwf = new GenerWorkFlow(workid);
			if (gwf.getPWorkID() == 0)
			{
				throw new RuntimeException("@当前节点是开始节点并且不是子流程，您不能执行退回。");
			}

			GenerWorkerLists gwls = new GenerWorkerLists();
			int i = gwls.Retrieve(GenerWorkerListAttr.WorkID, gwf.getPWorkID());
			String nodes = "";
			for (GenerWorkerList gwl : gwls.ToJavaList())
			{
				DataRow dr = dt.NewRow();
				dr.set("No", String.valueOf(gwl.getFK_Node()));

				if (nodes.contains(String.valueOf(gwl.getFK_Node()) + ",") == true)
				{
					continue;
				}

				nodes += String.valueOf(gwl.getFK_Node()) + ",";

				dr.set("Name", gwl.getFK_NodeText());
				dr.set("Rec", gwl.getFK_Emp());
				dr.set("RecName", gwl.getFK_EmpText());
				dr.set("IsBackTracking", "0");
				dt.Rows.add(dr);
			}
			return dt;
		}

		if (nd.getHisRunModel() == RunModel.SubThread)
		{
			/*如果是子线程，它只能退回它的上一个节点，现在写死了，其它的设置不起作用了。*/
			Nodes nds = nd.getFromNodes();
			for (Node ndFrom : nds)
			{
				Work wk;
				switch (ndFrom.getHisRunModel())
				{
					case FL:
					case FHL:
						wk = ndFrom.getHisWork();
						wk.setOID(fid);
						if (wk.RetrieveFromDBSources() == 0)
						{
							continue;
						}

						break;
					case SubThread:
						wk = ndFrom.getHisWork();
						wk.setOID(workid);
						if (wk.RetrieveFromDBSources() == 0)
						{
							continue;
						}

						break;
					case Ordinary:
					default:
						throw new RuntimeException("流程设计异常，子线程的上一个节点不能是普通节点。");
				}
				if (ndFrom.getNodeID() == fk_node)
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.set("No", String.valueOf(ndFrom.getNodeID()));
				dr.set("Name", ndFrom.getName());

				dr.set("Rec", wk.getRec());
				dr.set("RecName", wk.getRecText());

				if (ndFrom.getIsBackTracking())
				{
					dr.set("IsBackTracking", "1");
				}
				else
				{
					dr.set("IsBackTracking", "0");
				}

				dt.Rows.add(dr);
			}
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("err@没有获取到应该退回的节点列表.");
			}

			return dt;
		}

		String sql = "";

		WorkNode wn = new WorkNode(workid, fk_node);
		WorkNodes wns = new WorkNodes();
		switch (nd.getHisReturnRole())
		{
			case CanNotReturn:
				return dt;
			case ReturnAnyNodes:
				if (nd.getIsHL() || nd.getIsFLHL())
				{
					/*如果当前点是分流，或者是分合流，就不按退回规则计算了。*/
					sql = "SELECT A.FK_Node AS No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND a.FID=" + fid + " AND a.WorkID=" + workid + " AND a.FK_Node!=" + fk_node + " AND a.IsPass=1 ORDER BY RDT DESC ";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
					{
						dt.Columns.get("NO").ColumnName = "No";
						dt.Columns.get("NAME").ColumnName = "Name";
						dt.Columns.get("REC").ColumnName = "Rec";
						dt.Columns.get("RECNAME").ColumnName = "RecName";
						dt.Columns.get("ISBACKTRACKING").ColumnName = "IsBackTracking";
					}
					if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						dt.Columns.get("no").ColumnName = "No";
						dt.Columns.get("name").ColumnName = "Name";
						dt.Columns.get("rec").ColumnName = "Rec";
						dt.Columns.get("recname").ColumnName = "RecName";
						dt.Columns.get("isbacktracking").ColumnName = "IsBackTracking";
					}

					return dt;
				}

				if (nd.getTodolistModel() == TodolistModel.Order)
				{
					sql = "SELECT A.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking, a.AtPara FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND (a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node!=" + fk_node + ") OR (a.FK_Node=" + fk_node + " AND a.IsPass <0)  ORDER BY a.RDT DESC";
				}
				else
				{
					sql = "SELECT A.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking, a.AtPara FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node!=" + fk_node + " ORDER BY a.RDT DESC";
				}
				//                    sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking, a.AtPara FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node!=" + fk_node + " AND a.AtPara NOT LIKE '%@IsHuiQian=1%' ORDER BY a.RDT DESC";

				// BP.DA.Log.DebugWriteWarning(sql);

				dt = DBAccess.RunSQLReturnTable(sql);

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					dt.Columns.get("NO").ColumnName = "No";
					dt.Columns.get("NAME").ColumnName = "Name";
					dt.Columns.get("REC").ColumnName = "Rec";
					dt.Columns.get("RECNAME").ColumnName = "RecName";
					dt.Columns.get("ISBACKTRACKING").ColumnName = "IsBackTracking";
					dt.Columns.get("ATPARA").ColumnName = "AtPara"; //参数.
				}
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					dt.Columns.get("no").ColumnName = "No";
					dt.Columns.get("name").ColumnName = "Name";
					dt.Columns.get("rec").ColumnName = "Rec";
					dt.Columns.get("recname").ColumnName = "RecName";
					dt.Columns.get("isbacktracking").ColumnName = "IsBackTracking";
					dt.Columns.get("atpara").ColumnName = "AtPara"; //参数.
				}
				return dt;
			case ReturnPreviousNode:
				WorkNode mywnP = wn.GetPreviousWorkNode();

				if (nd.getIsHL() || nd.getIsFLHL())
				{
					/*如果当前点是分流，或者是分合流，就不按退回规则计算了。*/
					sql = "SELECT A.FK_Node AS No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking, a.AtPara FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND a.FID=" + fid + " AND a.WorkID=" + workid + " AND a.FK_Node=" + mywnP.getHisNode().getNodeID() + " AND a.IsPass=1 ORDER BY RDT DESC ";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						dt.Columns.get("NO").ColumnName = "No";
						dt.Columns.get("NAME").ColumnName = "Name";
						dt.Columns.get("REC").ColumnName = "Rec";
						dt.Columns.get("RECNAME").ColumnName = "RecName";
						dt.Columns.get("ISBACKTRACKING").ColumnName = "IsBackTracking";
						dt.Columns.get("ATPARA").ColumnName = "AtPara"; //参数.
					}
					return dt;
				}

				if (nd.getTodolistModel() == TodolistModel.Order)
				{
					sql = "SELECT A.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking,a.AtPara FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND (a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node=" + mywnP.getHisNode().getNodeID() + ") OR (a.FK_Node=" + mywnP.getHisNode().getNodeID() + " AND a.IsPass <0)  ORDER BY a.RDT DESC";
					dt = DBAccess.RunSQLReturnTable(sql);
				}
				else
				{
					sql = "SELECT A.FK_Node as \"No\",a.FK_NodeText as \"Name\", a.FK_Emp as \"Rec\", a.FK_EmpText as \"RecName\", b.IsBackTracking as \"IsBackTracking\", a.AtPara as \"AtPara\"  FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node=" + mywnP.getHisNode().getNodeID() + "  AND a.AtPara NOT LIKE '%@IsHuiQian=1%' ORDER BY a.RDT DESC ";
					DataTable mydt = DBAccess.RunSQLReturnTable(sql);

					if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						dt.Columns.get("NO").ColumnName = "No";
						dt.Columns.get("NAME").ColumnName = "Name";
						dt.Columns.get("REC").ColumnName = "Rec";
						dt.Columns.get("RECNAME").ColumnName = "RecName";
						dt.Columns.get("ISBACKTRACKING").ColumnName = "IsBackTracking";
						dt.Columns.get("ATPARA").ColumnName = "AtPara";
					}

					if (mydt.Rows.size() != 0)
					{
						return mydt;
					}

					//有可能是跳转过来的节点.//edited by liuxc,2017-05-26,改RDT排序为CDT排序，更准确，以避免有时找错上一步节点的情况发生
					if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
					{
						sql = "SELECT top 1 A.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking,a.AtPara FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 ORDER BY a.CDT DESC ";
					}
					else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
					{
						sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking,a.AtPara FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 AND rownum =1  ORDER BY a.CDT DESC ";
					}
					else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
					{
						sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking,a.AtPara FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 ORDER BY a.CDT DESC LIMIT 1";
					}
					else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking,a.AtPara FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1 ORDER BY a.CDT DESC LIMIT 1";
					}
					else
					{
						throw new RuntimeException("获取上一步节点，未涉及的数据库类型");
					}

					dt = DBAccess.RunSQLReturnTable(sql);

					if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						dt.Columns.get("NO").ColumnName = "No";
						dt.Columns.get("NAME").ColumnName = "Name";
						dt.Columns.get("REC").ColumnName = "Rec";
						dt.Columns.get("RECNAME").ColumnName = "RecName";
						dt.Columns.get("ISBACKTRACKING").ColumnName = "IsBackTracking";
						dt.Columns.get("ATPARA").ColumnName = "AtPara";
					}
					return dt;
				}
				break;
			case ReturnSpecifiedNodes: //退回指定的节点。
				if (wns.size() == 0)
				{
					wns.GenerByWorkID(wn.getHisNode().getHisFlow(), workid);
				}

				NodeReturns rnds = new NodeReturns();
				rnds.Retrieve(NodeReturnAttr.FK_Node, fk_node);
				if (rnds.size() == 0)
				{
					throw new RuntimeException("@流程设计错误，您设置该节点可以退回指定的节点，但是指定的节点集合为空，请在节点属性设置它的制订节点。");
				}

				for (NodeReturn item : rnds)
				{
					GenerWorkerLists gwls = new GenerWorkerLists();
					int i = gwls.Retrieve(GenerWorkerListAttr.FK_Node, item.getReturnTo(), GenerWorkerListAttr.WorkID, workid);
					if (i == 0)
					{
						continue;
					}

					for (GenerWorkerList gwl : gwls.ToJavaList())
					{
						DataRow dr = dt.NewRow();
						dr.set("No", String.valueOf(gwl.getFK_Node()));
						dr.set("Name", gwl.getFK_NodeText());
						dr.set("Rec", gwl.getFK_Emp());
						dr.set("RecName", gwl.getFK_EmpText());
						Node mynd = new Node(item.getFK_Node());
						if (mynd.getIsBackTracking()) //是否可以原路返回.
						{
							dr.set("IsBackTracking", "1");
						}
						else
						{
							dr.set("IsBackTracking", "0");
						}

						dt.Rows.add(dr);
					}
				}
				break;
			case ByReturnLine: //按照流程图画的退回线执行退回.
				Directions dirs = new Directions();
				dirs.Retrieve(DirectionAttr.Node, fk_node);
				if (dirs.size() == 0)
				{
					throw new RuntimeException("@流程设计错误:当前节点没有画向后退回的退回线,更多的信息请参考退回规则.");
				}

				for (Direction dir : dirs)
				{
					Node toNode = new Node(dir.getToNode());
					sql = "SELECT a.FK_Emp,a.FK_EmpText FROM WF_GenerWorkerlist a, WF_Node b WHERE   a.FK_Node=" + toNode.getNodeID() + " AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1";
					DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
					if (dt1.Rows.size() == 0)
					{
						continue;
					}

					DataRow dr = dt.NewRow();
					dr.set("No", String.valueOf(toNode.getNodeID()));
					dr.set("Name", toNode.getName());
					dr.set("Rec", dt1.get(0).getValue(0));
					dr.set("RecName", dt1.Rows[0][1]);
					if (toNode.getIsBackTracking() == true)
					{
						dr.set("IsBackTracking", "1");
					}
					else
					{
						dr.set("IsBackTracking", "0");
					}

					dt.Rows.add(dr);
				}
				break;
			default:
				throw new RuntimeException("@没有判断的退回类型。");
		}

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("REC").ColumnName = "Rec";
			dt.Columns.get("RECNAME").ColumnName = "RecName";
			dt.Columns.get("ISBACKTRACKING").ColumnName = "IsBackTracking";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
		}

		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@没有计算出来要退回的节点，请管理员确认节点退回规则是否合理？当前节点名称:" + nd.getName() + ",退回规则:" + nd.getHisReturnRole().toString());
		}

		return dt;
	}

		///#endregion 工作部件的数据源获取


		///#region 获取当前操作员的在途工作

	/** 
	 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
	 该接口为在途菜单提供数据,在在途工作中，可以执行撤销发送。
	 
	 @param userNo 操作员
	 @param fk_flow 流程编号
	 @param isMyStarter 是否仅仅查询我发起的在途流程
	 @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	*/

	public static DataTable DB_GenerRuning(String userNo, String fk_flow, boolean isMyStarter)
	{
		return DB_GenerRuning(userNo, fk_flow, isMyStarter, null);
	}

	public static DataTable DB_GenerRuning(String userNo, String fk_flow)
	{
		return DB_GenerRuning(userNo, fk_flow, false, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataTable DB_GenerRuning(string userNo, string fk_flow, bool isMyStarter = false, string domain = null)
	public static DataTable DB_GenerRuning(String userNo, String fk_flow, boolean isMyStarter, String domain)
	{
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();

		//获取用户当前所在的节点
		String currNode = "";
		switch (DBAccess.AppCenterDBType)
		{
			case DBType.Oracle:
				currNode = "(SELECT FK_Node FROM (SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' Order by RDT DESC ) WHERE rownum=1)";
				break;
			case DBType.MySQL:
			case DBType.PostgreSQL:
				currNode = "(SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' Order by RDT DESC LIMIT 1)";
				break;
			case DBType.MSSQL:
				currNode = "(SELECT TOP 1 FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' Order by RDT DESC)";
				break;
			default:
				break;
		}

		//授权模式.
		if (WebUser.getIsAuthorize() == true)
		{
			WFEmp emp = new WFEmp(userNo);
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				if (isMyStarter == true)
				{
					ps.SQL = "SELECT DISTINCT a.*," + currNode + " AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND A.Starter=" + dbStr + "Starter  AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN " + emp.getAuthorFlows();
					ps.Add("Starter", userNo);
					ps.Add("FK_Emp", userNo);
				}
				else
				{
					ps.SQL = "SELECT DISTINCT a.*," + currNode + " AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN " + emp.getAuthorFlows();
					ps.Add("FK_Emp", userNo);
				}
			}
			else
			{
				if (isMyStarter == true)
				{
					ps.SQL = "SELECT DISTINCT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.FK_Flow=" + dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND  A.Starter=" + dbStr + "Starter AND A.FK_Flow IN " + emp.getAuthorFlows();
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
					ps.Add("Starter", userNo);
				}
				else
				{
					ps.SQL = "SELECT DISTINCT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow=" + dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN " + emp.getAuthorFlows();
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
				}
			}
		}

		//非授权模式，
		if (WebUser.getIsAuthorize() == false)
		{

			if (DataType.IsNullOrEmpty(fk_flow))
			{
				if (isMyStarter == true)
				{
					ps.SQL = "SELECT DISTINCT a.*," + currNode + " AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND  A.Starter=" + dbStr + "Starter ";
					ps.Add("FK_Emp", userNo);
					ps.Add("Starter", userNo);
				}
				else
				{
					ps.SQL = "SELECT DISTINCT a.* ," + currNode + " AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0)";
					ps.Add("FK_Emp", userNo);
				}
			}
			else
			{
				if (isMyStarter == true)
				{
					ps.SQL = "SELECT DISTINCT a.* ," + currNode + " AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow=" + dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < 0 ) AND  A.Starter=" + dbStr + "Starter ";
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
					ps.Add("Starter", userNo);
				}
				else
				{
					ps.SQL = "SELECT DISTINCT a.* ," + currNode + " AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow=" + dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < 0 ) ";
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
				}
			}
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("WFSTA").ColumnName = "WFSta";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("FK_NY").ColumnName = "FK_NY";
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("PRI").ColumnName = "PRI";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("SDTOFFLOW").ColumnName = "SDTOfFlow";
			dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";
			dt.Columns.get("PNODEID").ColumnName = "PNodeID";
			dt.Columns.get("PFID").ColumnName = "PFID";
			dt.Columns.get("PEMP").ColumnName = "PEmp";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("EMPS").ColumnName = "Emps";
			dt.Columns.get("GUID").ColumnName = "GUID";
			dt.Columns.get("WEEKNUM").ColumnName = "WeekNum";
			dt.Columns.get("TSPAN").ColumnName = "TSpan";
			dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt.Columns.get("CURRNODE").ColumnName = "CurrNode";
			//dt.Columns.get("CFLOWNO").ColumnName = "CFlowNo";
			//dt.Columns.get("CWORKID").ColumnName = "CWorkID";

		}
		return dt;
	}
	/** 
	 在途统计:用于流程查询
	 
	 @return 返回 FK_Flow,FlowName,Num 三个列.
	*/
	public static DataTable DB_TongJi_Runing()
	{
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (WebUser.getIsAuthorize())
		{
			WFEmp emp = new WFEmp(WebUser.getNo());
			ps.SQL = "SELECT a.FK_Flow,a.FlowName, Count(a.WorkID) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN " + emp.getAuthorFlows() + " GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		}
		else
		{
			ps.SQL = "SELECT a.FK_Flow,a.FlowName, Count(a.WorkID) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0)  GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("NUM").ColumnName = "Num";
		}
		return dt;
	}
	/** 
	 统计流程状态
	 
	 @return 返回：流程类别编号，名称，流程编号，流程名称，TodoSta0代办中,TodoSta1预警中,TodoSta2预期中,TodoSta3已办结. 
	*/
	public static DataTable DB_TongJi_TodoSta()
	{
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (WebUser.getIsAuthorize())
		{
			BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
			ps.SQL = "SELECT a.FK_Flow,a.FlowName, Count(a.WorkID) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN " + emp.getAuthorFlows() + " GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		}
		else
		{
			ps.SQL = "SELECT a.FK_Flow,a.FlowName, Count(a.WorkID) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0)  GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	public static DataTable DB_GenerRuning2(String userNo, String fk_flow, String titleKey)
	{
		String sql;
		int state = WFState.Runing.getValue();
		if (DataType.IsNullOrEmpty(fk_flow))
		{
			if (DataType.IsNullOrEmpty(titleKey))
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) and A.FK_Flow!='010'";
			}
			else
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) and A.FK_Flow!='010' and A.Title Like '%" + titleKey + "%'";
			}
		}
		else
		{
			if (DataType.IsNullOrEmpty(titleKey))
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < 0 )";
			}
			else
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < 0 ) and A.Title Like '%" + titleKey + "%' ";
			}
		}

		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}
	/** 
	 在途工作
	 
	 @return 
	*/
	public static DataTable DB_GenerRuningV2()
	{
		String userNo = WebUser.getNo();
		String fk_flow = null;

		String sql;
		int state = WFState.Runing.getValue();
		if (WebUser.getIsAuthorize())
		{
			WFEmp emp = new WFEmp(userNo);
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
			else
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
		}
		else
		{
			if (DataType.IsNullOrEmpty(fk_flow))
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND B.IsPass=1 ";
			}
			else
			{
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND B.IsPass=1 ";
			}
		}
		return DBAccess.RunSQLReturnTable(sql);
	}
	/** 
	 获取内部系统消息
	 
	 @param myPK
	 @return 
	*/
	public static DataTable DB_GenerPopAlert(String type)
	{
		String sql = "";
		if (type.equals("unRead"))
		{
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
				+ "* FROM Sys_SMS WHERE SendTo ='" + WebUser.getNo() + "' AND (IsRead = 0 OR IsRead IS NULL)  ORDER BY RDT DESC";
		}
		else
		{
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
				+ "* FROM Sys_SMS WHERE SendTo ='" + WebUser.getNo() + "'  ORDER BY RDT DESC";
		}
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}

	/** 
	 获取外部系统消息
	 
	 @param type
	 @param No
	 @return 
	*/
	public static DataTable DB_GenerPopAlert(String type, String No)
	{
		String sql = "";
		if (type.equals("unRead"))
		{
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
				+ "* FROM Sys_SMS WHERE SendTo ='" + No + "' AND (IsRead = 0 OR IsRead IS NULL)  ORDER BY RDT DESC";
		}
		else
		{
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
				+ "* FROM Sys_SMS WHERE SendTo ='" + No + "'  ORDER BY RDT DESC";
		}
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}
	/** 
	 更新消息状态
	 
	 @param myPK
	*/
	public static DataTable DB_GenerUpdateMsgSta(String myPK)
	{
		String sql = "";
		sql = " UPDATE Sys_SMS SET IsRead=1 WHERE MyPK='" + myPK + "'";
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}
	/** 
	 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
	 
	 @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	*/
	public static DataTable DB_GenerRuning()
	{
		DataTable dt = DB_GenerRuning(WebUser.getNo(), null);

		/*暂时屏蔽type的拼接，拼接后转json会报错 于庆海修改*/
		/*dt.Columns.Add("Type");
		foreach (DataRow row in dt.Rows)
		{
		    row["Type"] = "RUNNING";
		}*/

		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}
	/** 
	 获取某一个人的在途（参与、未完成的工作）
	 
	 @param userNo
	 @return 
	*/
	public static DataTable DB_GenerRuning(String userNo)
	{
		DataTable dt = DB_GenerRuning(userNo, null);
		dt.Columns.Add("Type");

		for (DataRow row : dt.Rows)
		{
			row.set("Type", "RUNNING");
		}

		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}
	/** 
	 把抄送的信息也发送
	 
	 @return 
	*/
	public static DataTable DB_GenerRuningAndCC()
	{
		DataTable dt = DB_GenerRuning();
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());
		try
		{
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		}
		catch (RuntimeException e)
		{

		}

		for (DataRow row : ccDT.Rows)
		{
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns)
			{
				for (DataColumn dtColumn : dt.Columns)
				{
					if (column.ColumnName.equals(dtColumn.ColumnName))
					{
						newRow.set(column.ColumnName, row.get(dtColumn.ColumnName));
					}
				}
			}
			newRow.set("Type", "CC");
			dt.Rows.add(newRow);
		}
		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}
	/** 
	 为什么需要这个接口？
	 
	 @param name
	 @param fk_flow
	 @param title
	 @return 
	*/
	public static DataTable DB_GenerRuning3(String name, String fk_flow, String title)
	{
		DataTable dt = DB_GenerRuning2(name, fk_flow, title);

		dt.Columns.Add("Type");

		for (DataRow row : dt.Rows)
		{
			row.set("Type", "RUNNING");
		}

		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}
	public static DataTable DB_GenerRuningAndCC2(String name, String fk_flow, String title)
	{
		DataTable dt = DB_GenerRuning3(name, fk_flow, title);
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());
		try
		{
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		}
		catch (RuntimeException e)
		{

		}

		for (DataRow row : ccDT.Rows)
		{
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns)
			{
				for (DataColumn dtColumn : dt.Columns)
				{
					if (column.ColumnName.equals(dtColumn.ColumnName))
					{
						newRow.set(column.ColumnName, row.get(dtColumn.ColumnName));
					}

				}

			}
			newRow.set("Type", "CC");
			dt.Rows.add(newRow);
		}
		dt.DefaultView.Sort = "RDT DESC";
		return dt.DefaultView.ToTable();
	}

		///#endregion 获取当前操作员的共享工作


		///#region 获取当前的批处理工作
	/** 
	 获取当前节点的批处理工作
	 
	 @param FK_Node
	 @return 
	*/
	public static DataTable GetBatch(int FK_Node)
	{

		BP.WF.Node nd = new BP.WF.Node(FK_Node);
		Flow fl = nd.getHisFlow();
		String fromTable = "";

		if (fl.getHisDataStoreModel() == DataStoreModel.ByCCFlow)
		{
			fromTable = nd.getPTable();
		}
		else
		{
			fromTable = fl.getPTable();
		}

		String sql = "SELECT a.*, b.Starter,b.Title as STitle,b.ADT,b.WorkID FROM " + fromTable + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		// string sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		return dt;
	}


		///#endregion 获取当前的批处理工作

		///#endregion


		///#region 登陆接口
	/** 
	 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 
	 @param userNo 用户名
	 @param SID 安全ID,请参考流程设计器操作手册
	*/

	public static void Port_LoginBySID(String userNo, String sid)
	{
		Port_LoginBySID(userNo, sid, "PC");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void Port_LoginBySID(string userNo, string sid, string deviceNo = "PC")
	public static void Port_LoginBySID(String userNo, String sid, String deviceNo)
	{
		if (userNo.equals(WebUser.getNo()))
		{
			return;
		}


		WFEmp emp = new WFEmp(userNo);

		String key = "SID_" + deviceNo + userNo;
		String guid = emp.GetParaString(key);
		if (guid.equals(sid) == false)
		{
			throw new RuntimeException("err@非法的sid.");
		}

		BP.Port.Emp myEmp = new BP.Port.Emp(userNo);
		WebUser.SignInOfGener(myEmp);
		return;
	}
	/** 
	 登录
	 
	 @param userNo 人员编号
	 @param userName 名称
	 @param fk_dept 所在部门
	 @param deptName 部门名称
	 @return 
	*/
	public static void Port_Login(String userNo)
	{
		/* 仅仅传递了人员编号，就按照人员来取.*/
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo (userNo;
		emp.RetrieveFromDBSources();
		WebUser.SignInOfGener(emp);

	}
	/** 
	 注销当前登录
	*/
	public static void Port_SigOut()
	{
		WebUser.Exit();
	}
	/** 
	 获取未读的消息
	 用于消息提醒.
	 
	 @param userNo 用户ID
	*/
	public static String Port_SMSInfo(String userNo)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT MyPK, EmailTitle  FROM sys_sms where SendTo=" + SystemConfig.getAppCenterDBVarStr() + "SendTo AND IsAlert=0";
		ps.Add("SendTo", userNo);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += "@" + dr.get(0) + "=" + dr.get(1).toString();
		}
		ps = new Paras();
		ps.SQL = "UPDATE  sys_sms SET IsAlert=1 WHERE  SendTo=" + SystemConfig.getAppCenterDBVarStr() + "SendTo AND IsAlert=0";
		ps.Add("SendTo", userNo);
		DBAccess.RunSQL(ps);
		return strs;
	}
	/** 
	 发送消息
	 
	 @param userNo 信息接收人
	 @param msgTitle 标题
	 @param msgDoc 内容
	*/
	public static void Port_SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag)
	{
		Port_SendMsg(userNo, msgTitle, msgDoc, msgFlag, BP.WF.SMSMsgType.Self, null, 0, 0, 0);
	}
	/** 
	 获取有效的SID
	 
	 @param userNo 用户编号
	 @param logDev 设备编号
	 @param activeMinutes 登录有效时间
	 @return 返回一个新的SID
	*/

	public static String Port_GenerSID(String userNo, String logDev)
	{
		return Port_GenerSID(userNo, logDev, 0);
	}

	public static String Port_GenerSID(String userNo)
	{
		return Port_GenerSID(userNo, "PC", 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Port_GenerSID(string userNo, string logDev = "PC", int activeMinutes = 0)
	public static String Port_GenerSID(String userNo, String logDev, int activeMinutes)
	{
		if (logDev == null)
		{
			logDev = "PC";
		}

		if (activeMinutes == 0)
		{
			activeMinutes = 300; //默认为300分钟.
		}

		String key = "SID_" + logDev;

		WFEmp emp = new WFEmp(userNo);

		//如果第一次登录.
		String myGuid = emp.GetParaString(key);
		String guidOID_Dt = emp.GetParaString(key + "_DT");

		if (DataType.IsNullOrEmpty(myGuid) == true || DataType.IsNullOrEmpty(guidOID_Dt) == true)
		{
			String guid = DBAccess.GenerGUID();
			emp.SetPara(key, guid);

			LocalDateTime dt = LocalDateTime.now();
			dt = dt.plusMinutes(activeMinutes);

			emp.SetPara(key + "_DT", dt.toString("yyyy-MM-dd HH:mm:ss"));
			emp.Update();
			return guid;
		}

		LocalDateTime dtTo = DataType.ParseSysDateTime2DateTime(guidOID_Dt);
		if (dtTo.compareTo(LocalDateTime.now()) < 0)
		{
			LocalDateTime dtUpdate = LocalDateTime.now();
			dtUpdate = dtUpdate.plusMinutes(activeMinutes);

			emp.SetPara(key + "_DT", dtUpdate.toString("yyyy-MM-dd HH:mm:ss"));
			emp.Update();
			return myGuid;
		}

		String guidNew = DBAccess.GenerGUID();
		emp.SetPara(key, guidNew);

		LocalDateTime dtNew = LocalDateTime.now();
		dtNew = dtNew.plusMinutes(activeMinutes);

		emp.SetPara(key + "_DT", dtNew.toString("yyyy-MM-dd HH:mm:ss"));
		emp.Update();
		return guidNew;
	}
	/** 
	 验证用户的合法性
	 
	 @param userNo 用户编号
	 @param SID 密钥
	 @return 是否匹配
	*/
	public static boolean Port_CheckUserLogin(String userNo, String SID)
	{
		return true;

		if (DataType.IsNullOrEmpty(userNo))
		{
			return false;
		}

		if (DataType.IsNullOrEmpty(SID))
		{
			return false;
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT SID FROM Port_Emp WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
		ps.Add("No", userNo);

		String mysid = DBAccess.RunSQLReturnStringIsNull(ps, null);
		if (mysid == null)
		{
			throw new RuntimeException("@没有取得用户(" + userNo + ")的SID.");
		}

		if (SID.equals(mysid))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 设置SID
	 
	 @param userNo 用户编号
	 @param sid SID信息
	 @return SID
	*/
	public static boolean Port_SetSID(String userNo, String sid)
	{
		//判断是否更新的是用户表中的SID
		if (Glo.getUpdataSID().contains("UPDATE Port_Emp SET SID=") == true)
		{
			//判断是否视图，如果为视图则不进行修改 需要翻译
			if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == true)
			{
				return false;
			}
		}

		try
		{
			//替换变量的值
			Paras ps = new Paras();
			ps.SQL = Glo.getUpdataSID();
			ps.Add("SID", sid);
			ps.Add("No", userNo);
			if (BP.DA.DBAccess.RunSQL(ps) == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (RuntimeException ex)
		{
			if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == true)
			{
				throw new RuntimeException("@执行更新SID失败,您在组织结构集成的时候需要配置一个更新SID的SQL, 比如: update MyUserTable SET SID=@SID WHERE BH='@No'");
			}

			throw ex;
		}
	}
	/** 
	 发送邮件与消息(如果传入4大流程参数将会增加一个工作链接)
	 
	 @param userNo 信息接收人
	 @param title 标题
	 @param msgDoc 内容
	 @param msgFlag 消息标志
	 @param flowNo 流程编号
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @param fid FID
	*/
	public static void Port_SendMsg(String userNo, String title, String msgDoc, String msgFlag, String msgType, String flowNo, long nodeID, long workID, long fid)
	{
		if (workID != 0)
		{
			String url = Glo.getHostURL() + "WF/Do.htm?SID=" + userNo + "_" + workID + "_" + nodeID;
			url = url.replace("//", "/");
			url = url.replace("//", "/");
			msgDoc += " <hr>打开工作: " + url;
		}

		String para = "@FK_Flow=" + flowNo + "@WorkID=" + workID + "@FK_Node=" + nodeID + "@Sender=" + WebUser.getNo();
		BP.WF.SMS.SendMsg(userNo, title, msgDoc, msgFlag, msgType, para);
	}


	/** 
	 发送消息
	 
	 @param sendToEmpNo 接收人
	 @param smsDoc 消息内容
	 @param emailTitle 邮件标题
	 @param msgType 消息类型(例如工作到达后、发送成功后)
	 @param msgGroupFlag 消息分组（与消息类型有关联）
	 @param sendEmpNo 发送人
	 @param openUrl 连接URL
	 @param pushModel 可以接受消息的类型(如邮件、短信、丁丁、微信等)
	 @param msgPK 唯一标志,防止发送重复.
	 @param atParas 参数.
	*/

	public static void Port_SendMessage(String sendToEmpNo, String smsDoc, String emailTitle, String msgType, String msgGroupFlag, String sendEmpNo, String openUrl, String pushModel, String msgPK)
	{
		Port_SendMessage(sendToEmpNo, smsDoc, emailTitle, msgType, msgGroupFlag, sendEmpNo, openUrl, pushModel, msgPK, null);
	}

	public static void Port_SendMessage(String sendToEmpNo, String smsDoc, String emailTitle, String msgType, String msgGroupFlag, String sendEmpNo, String openUrl, String pushModel)
	{
		Port_SendMessage(sendToEmpNo, smsDoc, emailTitle, msgType, msgGroupFlag, sendEmpNo, openUrl, pushModel, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void Port_SendMessage(string sendToEmpNo, string smsDoc, string emailTitle, string msgType, string msgGroupFlag, string sendEmpNo, string openUrl, string pushModel, string msgPK = null, string atParas = null)
	public static void Port_SendMessage(String sendToEmpNo, String smsDoc, String emailTitle, String msgType, String msgGroupFlag, String sendEmpNo, String openUrl, String pushModel, String msgPK, String atParas)
	{
		WFEmp emp = new WFEmp(sendToEmpNo);
		SMS sms = new SMS();
		if (DataType.IsNullOrEmpty(msgPK) == false)
		{
			/*如果有唯一标志,就判断是否有该数据，没有该数据就允许插入.*/
			if (sms.IsExit(SMSAttr.MyPK, msgPK) == true)
			{
				return;
			}

			sms.setMyPK( msgPK;
		}
		else
		{
			sms.setMyPK( DBAccess.GenerGUID();
		}

		sms.setHisEmailSta(MsgSta.UnRun);
		sms.setHisMobileSta(MsgSta.UnRun);

		if (sendEmpNo == null)
		{
			sms.setSender(WebUser.getNo());
		}
		else
		{
			sms.setSender(sendEmpNo);
		}

		//发送给人员ID , 有可能这个人员空的.
		sms.setSendToEmpNo(sendToEmpNo);


			///#region 邮件信息
		//邮件地址.
		sms.setEmail(emp.getEmail());
		//邮件标题.
		sms.setTitle(emailTitle);
		sms.setDocOfEmail(smsDoc);

			///#endregion 邮件信息


			///#region 短消息信息
		sms.setMobile(emp.getTel());
		sms.setMobileInfo(smsDoc);
		sms.setTitle(emailTitle);

			///#endregion 短消息信息

		// 其他属性.
		sms.setRDT(BP.DA.DataType.getCurrentDataTime());

		sms.setMsgType(msgType); // 消息类型.

		sms.setMsgFlag(msgGroupFlag); // 消息分组标志,用于批量删除.

		sms.setAtPara(atParas);

		sms.SetPara("OpenUrl", openUrl);
		sms.SetPara("PushModel", pushModel);

		// 先保留本机一份.
		sms.Insert();
	}


	/** 
	 发送邮件
	 
	 @param mailAddress 邮件地址
	 @param emilTitle 标题
	 @param emailBody 内容
	 @param msgType 消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...)
	 @param msgGroupFlag 分组标记
	 @param sender 发送人
	 @param msgPK 消息唯一标记，防止发送重复.
	*/
	//public static void Port_SendEmail(string mailAddress, string emilTitle, string emailBody,
	//    string msgType, string msgGroupFlag = null, string sender = null, string msgPK = null, string sendToEmpNo = null, string paras = null)
	//{
	//    if (DataType.IsNullOrEmpty(mailAddress))
	//        return;

	//    SMS sms = new SMS();
	//    if (DataType.IsNullOrEmpty(msgPK) == false)
	//    {
	//        /*如果有唯一标志,就判断是否有该数据，没有该数据就允许插入.*/
	//        if (sms.IsExit(SMSAttr.MyPK, msgPK) == true)
	//        {
	//            return;
	//        }

	//        sms.setMyPK( msgPK;
	//    }
	//    else
	//    {
	//        sms.setMyPK( DBAccess.GenerGUID();
	//    }

	//    sms.HisEmailSta = MsgSta.UnRun;
	//    if (sender == null)
	//    {
	//        sms.Sender = WebUser.getNo();
	//    }
	//    else
	//    {
	//        sms.Sender = sender;
	//    }

	//    sms.SendToEmpNo = sendToEmpNo;

	//    //邮件地址.
	//    sms.Email = mailAddress;

	//    //邮件标题.
	//    sms.Title = emilTitle;
	//    sms.DocOfEmail = emailBody;





	//    //手机状态禁用.
	//    sms.HisMobileSta = MsgSta.Disable;

	//    // 其他属性.
	//    sms.RDT = BP.DA.DataType.getCurrentDataTime();

	//    //消息参数.
	//    sms.AtPara = paras;

	//    sms.MsgFlag = msgGroupFlag; // 消息标志.
	//    sms.MsgType = msgType;   // 消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...).
	//    sms.Insert();
	//}
	/** 
	 发送短信
	 
	 @param tel 电话
	 @param smsDoc 短信内容
	 @param msgType 消息类型
	 @param msgGroupFloag 消息分组
	 @param sender 发送人
	 @param msgPK 唯一标志,防止发送重复.
	 @param sendEmpNo 发送给人员.
	 @param atParas 参数.
	*/
	//public static void Port_SendSMS(string tel, string smsDoc, string msgType, string msgGroupFlag,
	//    string sender = null, string msgPK = null, string sendToEmpNo = null, string atParas = null, string title = null, string opnUrl = null, string pushModel = null)
	//{
	//    //if (DataType.IsNullOrEmpty(tel))
	//    //    return;

	//    SMS sms = new SMS();
	//    if (DataType.IsNullOrEmpty(msgPK) == false)
	//    {
	//        /*如果有唯一标志,就判断是否有该数据，没有该数据就允许插入.*/
	//        if (sms.IsExit(SMSAttr.MyPK, msgPK) == true)
	//        {
	//            return;
	//        }

	//        sms.setMyPK( msgPK;
	//    }
	//    else
	//    {
	//        sms.setMyPK( DBAccess.GenerGUID();
	//    }

	//    sms.HisEmailSta = MsgSta.Disable;
	//    sms.HisMobileSta = MsgSta.UnRun;

	//    if (sender == null)
	//    {
	//        sms.Sender = WebUser.getNo();
	//    }
	//    else
	//    {
	//        sms.Sender = sender;
	//    }

	//    //发送给人员ID , 有可能这个人员空的.
	//    sms.SendToEmpNo = sendToEmpNo;

	//    sms.Mobile = tel;
	//    sms.MobileInfo = smsDoc;
	//    sms.Title = title;

	//    // 其他属性.
	//    sms.RDT = BP.DA.DataType.getCurrentDataTime();

	//    sms.MsgType = msgType; // 消息类型.

	//    sms.MsgFlag = msgGroupFlag; // 消息分组标志,用于批量删除.

	//    sms.AtPara = atParas;

	//    sms.SetPara("OpenUrl", opnUrl);
	//    sms.SetPara("PushModel", pushModel);

	//    // 先保留本机一份.
	//    sms.Insert();
	//}
	/** 
	 获取最新的消息
	 
	 @param userNo 用户编号
	 @param dateLastTime 上次获取的时间
	 @return 返回消息：返回两个列的数据源MsgType,Num.
	*/
	public static DataTable Port_GetNewMsg(String userNo, String dateLastTime)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT MsgType , Count(*) as Num FROM Sys_SMS WHERE SendTo=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "SendTo AND RDT >" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "RDT Group By MsgType";
		ps.Add(BP.WF.SMSAttr.SendTo, userNo);
		ps.Add(BP.WF.SMSAttr.RDT, dateLastTime);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		return dt;
	}
	/** 
	 获取最新的消息
	 
	 @param userNo 用户编号
	 @return 
	*/
	public static DataTable Port_GetNewMsg(String userNo)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT MsgType , Count(*) as Num FROM Sys_SMS WHERE SendTo=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "SendTo  Group By MsgType";
		ps.Add(BP.WF.SMSAttr.SendTo, userNo);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		return dt;
	}

		///#endregion 登陆接口


		///#region 与流程有关的接口
	/** 
	 写入日志
	 
	 @param flowNo 流程编号
	 @param nodeFrom 节点从
	 @param workid 工作ID
	 @param fid FID
	 @param msg 信息
	 @param at 活动类型
	 @param tag 参数:用@符号隔开比如, @PWorkID=101@PFlowNo=003
	 @param cFlowInfo 子流程信息
	*/

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo, String empNoFrom, String empNameFrom, String rdt)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, optionMsg, empNoTo, empNameTo, empNoFrom, empNameFrom, rdt, null);
	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo, String empNoFrom, String empNameFrom)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, optionMsg, empNoTo, empNameTo, empNoFrom, empNameFrom, null, null);
	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo, String empNoFrom)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, optionMsg, empNoTo, empNameTo, empNoFrom, null, null, null);
	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, optionMsg, empNoTo, empNameTo, null, null, null, null);
	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, optionMsg, empNoTo, null, null, null, null, null);
	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, optionMsg, null, null, null, null, null, null);
	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo)
	{
		WriteTrack(flowNo, nodeFromID, nodeFromName, workid, fid, msg, at, tag, cFlowInfo, null, null, null, null, null, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void WriteTrack(string flowNo, int nodeFromID, string nodeFromName, Int64 workid, Int64 fid, string msg, ActionType at, string tag, string cFlowInfo, string optionMsg = null, string empNoTo = null, string empNameTo = null, string empNoFrom = null, string empNameFrom = null, string rdt = null, string fwcView = null)
	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg, ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo, String empNoFrom, String empNameFrom, String rdt, String fwcView)
	{
		if (at == ActionType.CallChildenFlow)
		{
			if (DataType.IsNullOrEmpty(cFlowInfo) == true)
			{
				throw new RuntimeException("@必须输入信息cFlowInfo信息,在 CallChildenFlow 模式下.");
			}
		}

		if (DataType.IsNullOrEmpty(optionMsg))
		{
			optionMsg = Track.GetActionTypeT(at);
		}

		Track t = new Track();
		t.setWorkID(workid);
		t.setFID(fid);

		//记录日期.
		LocalDateTime d = LocalDateTime.MIN;
		if (DataType.IsNullOrEmpty(rdt))
		{
			t.setRDT(DataType.getCurrentDataTime());
		}
		else
		{
			t.setRDT(rdt);
		}

		t.setHisActionType(at);
		t.setActionTypeText(optionMsg);

		// Node nd = new Node(nodeFrom);
		t.setNDFrom(nodeFromID);
		t.setNDFromT(nodeFromName);

		if (empNoFrom == null)
		{
			t.setEmpFrom(WebUser.getNo());
		}
		else
		{
			t.setEmpFrom(empNoFrom);
		}

		if (empNameFrom == null)
		{
			t.setEmpFromT(WebUser.getName());
		}
		else
		{
			t.setEmpFromT(empNameFrom);
		}

		t.FK_Flow = flowNo;

		t.setNDTo(nodeFromID);
		t.setNDToT(nodeFromName);

		if (empNoTo == null)
		{
			t.setEmpTo(WebUser.getNo());
			t.setEmpToT(WebUser.getName());
		}
		else
		{
			t.setEmpTo(empNoTo);
			t.setEmpToT(empNameTo);
		}


		t.setMsg(msg);

		if (tag != null)
		{
			t.setTag(tag);
		}
		if (fwcView != null)
		{
			t.setTag(t.getTag() + fwcView);
		}

		try
		{
			t.Insert();
		}
		catch (java.lang.Exception e)
		{
			t.CheckPhysicsTable();
			t.Insert();
			//t.DirectInsert();
		}


			///#region 特殊判断.
		if (at == ActionType.CallChildenFlow)
		{
			/* 如果是吊起子流程，就要向它父流程信息里写数据，让父流程可以看到能够发起那些流程数据。*/
			AtPara ap = new AtPara(tag);
			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(ap.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID));
			t.setWorkID(gwf.getWorkID());

			t.setNDFrom(gwf.getFK_Node());
			t.setNDFromT(gwf.getNodeName());

			t.setNDTo(t.getNDFrom());
			t.setNDToT(t.getNDFromT());

			t.FK_Flow = gwf.getFK_Flow();

			t.setHisActionType(ActionType.StartChildenFlow);
			t.setTag("@CWorkID=" + workid + "@CFlowNo=" + flowNo);
			t.setMsg(cFlowInfo);
			t.Insert();
		}

			///#endregion 特殊判断.
	}
	/** 
	 写入日志
	 
	 @param flowNo 流程编号
	 @param nodeFrom 节点从
	 @param workid 工作ID
	 @param fid fID
	 @param msg 信息
	*/
	public static void WriteTrackInfo(String flowNo, int nodeFrom, String ndFromName, long workid, long fid, String msg, String optionMsg)
	{
		WriteTrack(flowNo, nodeFrom, ndFromName, workid, fid, msg, ActionType.Info, null, null, optionMsg);
	}
	/** 
	 写入工作审核日志:
	 
	 @param flowNo 流程编号
	 @param currNodeID 当前节点ID
	 @param workid 工作ID
	 @param FID FID
	 @param msg 审核信息
	 @param optionName 操作名称(比如:科长审核、部门经理审批),如果为空就是"审核".
	*/

	public static void WriteTrackWorkCheck(String flowNo, int currNodeID, long workid, long fid, String msg, String optionName)
	{
		WriteTrackWorkCheck(flowNo, currNodeID, workid, fid, msg, optionName, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void WriteTrackWorkCheck(string flowNo, int currNodeID, Int64 workid, Int64 fid, string msg, string optionName, string fwcView = null)
	public static void WriteTrackWorkCheck(String flowNo, int currNodeID, long workid, long fid, String msg, String optionName, String fwcView)
	{
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		gwf.RetrieveFromDBSources();

		//求主键 2017.10.6以前的逻辑.
		String tag = currNodeID + "_" + workid + "_" + fid + "_" + WebUser.getNo();

		//求当前是否是会签.  zhangsan,张三;李四;王五;
		String nodeName = gwf.getNodeName();
		Node nd = new Node(currNodeID);
		if (nd.getIsStartNode() == false)
		{
			if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
			{
				nodeName = nd.getName() + "(会签)";
			}
		}

		//待办抢办模式，一个节点只能有一条记录.
		Paras ps = new Paras();
		if (nd.getTodolistModel() == TodolistModel.QiangBan || nd.getTodolistModel() == TodolistModel.Sharing)
		{
			//先删除其他人员写入的数据. 此脚本是2016.11.30号的,为了解决柳州的问题，需要扩展.
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  WorkID=" + dbStr + "WorkID  AND NDFrom=" + dbStr + "NDFrom AND ActionType=" + ActionType.WorkCheck.getValue();
			ps.Add(TrackAttr.WorkID, workid);
			ps.Add(TrackAttr.NDFrom, currNodeID);
			DBAccess.RunSQL(ps);

			//写入日志.
			WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg, ActionType.WorkCheck, tag, null, optionName, null, null, null, null, null, fwcView);
			return;
		}

		ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET NDFromT=" + dbStr + "NDFromT, Msg=" + dbStr + "Msg, RDT=" + dbStr +
				 "RDT WHERE  Tag=" + dbStr + "Tag ";
		ps.Add(TrackAttr.NDFromT, nodeName);
		ps.Add(TrackAttr.Msg, msg);
		ps.Add(TrackAttr.Tag, tag);
		ps.Add(TrackAttr.RDT, DataType.getCurrentDataTime());
		int num = DBAccess.RunSQL(ps);

		if (num > 1)
		{
			ps.Clear();
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  Tag=" + dbStr + "Tag ";
			ps.Add(TrackAttr.Tag, tag);
			DBAccess.RunSQL(ps);
			num = 0;
		}

		if (num == 0)
		{
			//如果没有更新到，就写入.
			WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg, ActionType.WorkCheck, tag, null, optionName, null, null, null, null, null, fwcView);
		}
	}

	public static void WriteTrackWorkCheckForTangRenYiYao(String flowNo, int currNodeID, long workid, long fid, String msg, String optionName)
	{
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		//WorkNode wn = new WorkNode(workid, currNodeID);
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		gwf.RetrieveFromDBSources();

		//求主键 2017.10.6以前的逻辑.
		String tag = gwf.getParas_LastSendTruckID() + "_" + currNodeID + "_" + workid + "_" + fid + "_" + WebUser.getNo();

		String nodeName = gwf.getNodeName();
		if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
		{
			nodeName = nodeName + "(会签)";
		}

		Node nd = new Node(currNodeID);
		//待办抢办模式，一个节点只能有一条记录.
		Paras ps = new Paras();
		if (nd.getTodolistModel() == TodolistModel.QiangBan || nd.getTodolistModel() == TodolistModel.Sharing)
		{
			//先删除其他人员写入的数据. 此脚本是2016.11.30号的,为了解决柳州的问题，需要扩展.
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  WorkID=" + dbStr + "WorkID  AND NDFrom=" + dbStr + "NDFrom AND ActionType=" + ActionType.WorkCheck.getValue() + " AND Tag LIKE '" + gwf.getParas_LastSendTruckID() + "%'";
			ps.Add(TrackAttr.WorkID, workid);
			ps.Add(TrackAttr.NDFrom, currNodeID);
			DBAccess.RunSQL(ps);

			////先删除其他人员写入的数据.
			////string sql = "DELETE FROM ND" + int.Parse(flowNo) + "Track WHERE  Tag LIKE '" + gwf.Paras_LastSendTruckID + "%' AND EmpFrom='"+WebUser.getNo()+"' ";
			//string sql = "DELETE FROM ND" + int.Parse(flowNo) + "Track WHERE  Tag LIKE '" + gwf.Paras_LastSendTruckID + "%'";
			//DBAccess.RunSQL(ps);
			//写入日志
			WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg, ActionType.WorkCheck, tag, null, optionName);
		}
		else
		{
			ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET NDFromT=" + dbStr + "NDFromT, Msg=" + dbStr + "Msg,RDT=" + dbStr +
					 "RDT WHERE  Tag=" + dbStr + "Tag";
			ps.Add(TrackAttr.NDFromT, nodeName);
			ps.Add(TrackAttr.Msg, msg);
			ps.Add(TrackAttr.Tag, tag);
			ps.Add(TrackAttr.RDT, DataType.getCurrentDataTime());
			if (DBAccess.RunSQL(ps) == 0)
			{
				//如果没有更新到，就写入.
				WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg, ActionType.WorkCheck, tag, null, optionName, null, null);
			}
		}
	}
	/** 
	 写入日志组件
	 
	 @param flowNo 流程编号
	 @param nodeFrom
	 @param workid
	 @param fid
	 @param msg
	 @param optionName
	*/
	public static void WriteTrackDailyLog(String flowNo, int nodeFrom, String nodeFromName, long workid, long fid, String msg, String optionName)
	{
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String today = BP.DA.DataType.CurrentData;

		Paras ps = new Paras();
		ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET Msg=" + dbStr + "Msg WHERE  RDT LIKE '" + today + "%' AND WorkID=" + dbStr + "WorkID  AND NDFrom=" + dbStr + "NDFrom AND EmpFrom=" + dbStr + "EmpFrom AND ActionType=" + ActionType.WorkCheck.getValue();
		ps.Add(TrackAttr.Msg, msg);
		ps.Add(TrackAttr.WorkID, workid);
		ps.Add(TrackAttr.NDFrom, nodeFrom);
		ps.Add(TrackAttr.EmpFrom, WebUser.getNo());
		if (DBAccess.RunSQL(ps) == 0)
		{
			//如果没有更新到，就写入.
			WriteTrack(flowNo, nodeFrom, nodeFromName, workid, fid, msg, ActionType.WorkCheck, null, null, optionName);
		}
	}
	/** 
	 写入周报组件 一旦写入数据,只可更新   每周一次 qin
	 
	 @param flowNo 流程编号
	 @param nodeFrom
	 @param workid
	 @param fid
	 @param msg
	 @param optionName
	*/
	public static void WriteTrackWeekLog(String flowNo, int nodeFrom, String nodeFromName, long workid, long fid, String msg, String optionName)
	{
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		LocalDateTime dTime = LocalDateTime.now();
		LocalDateTime startWeek = dTime.plusDays(1 - Integer.parseInt(String.format("%d", dTime.getDayOfWeek()))); //本周第一天

		Hashtable ht = new Hashtable(); //当前日期所属的week包含哪些日期
		for (int i = 1; i < 7; i++)
		{
			ht.put(i + 1, startWeek.plusDays(i).toString("yyyy-MM-dd"));
		}
		ht.put(1, startWeek.toString("yyyy-MM-dd"));

		boolean isExitWeek = false; //本周是否已经有插入数据
		String insertDate = null;
		DataTable dt;
		String sql = null;

		for (Map.Entry de : ht.entrySet())
		{
			sql = "SELECT * FROM ND" + Integer.parseInt(flowNo) +
				"Track  WHERE  RDT LIKE '" + de.getValue().toString() + "%' AND WorkID=" + workid + "  AND NDFrom='" +
				nodeFrom + "' AND EmpFrom='" + WebUser.getNo() + "' AND ActionType=" + ActionType.WorkCheck.getValue();

			if (DBAccess.RunSQLReturnCOUNT(sql) != 0)
			{
				isExitWeek = true;
				insertDate = de.getValue().toString();
				break;
			}
		}

		//如果本周已经插入了记录，那么更新 
		if (isExitWeek)
		{
			Paras ps = new Paras();
			ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET RDT='" + LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "',Msg=" + dbStr + "Msg WHERE  RDT LIKE '" + insertDate + "%' AND WorkID=" + dbStr + "WorkID  AND NDFrom=" + dbStr + "NDFrom AND EmpFrom=" + dbStr + "EmpFrom AND ActionType=" + ActionType.WorkCheck.getValue();
			ps.Add(TrackAttr.Msg, msg);
			ps.Add(TrackAttr.WorkID, workid);
			ps.Add(TrackAttr.NDFrom, nodeFrom);
			ps.Add(TrackAttr.EmpFrom, WebUser.getNo());

			DBAccess.RunSQL(ps);
		}
		else
		{
			WriteTrack(flowNo, nodeFrom, nodeFromName, workid, fid, msg, ActionType.WorkCheck, null, null, optionName);
		}
	}
	/** 
	 写入月报组件  同周报一样每月一条记录 qin
	 
	 @param flowNo 流程编号
	 @param nodeFrom
	 @param workid
	 @param fid
	 @param msg
	 @param optionName
	*/
	public static void WriteTrackMonthLog(String flowNo, int nodeFrom, String nodeFromName, long workid, long fid, String msg, String optionName)
	{
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String today = BP.DA.DataType.getCurrentDate();

		LocalDateTime dTime = LocalDateTime.now();
		LocalDateTime startDay = dTime.plusDays(1 - dTime.getDayOfMonth()); //本月第一天

		int days = LocalDateTime.DaysInMonth(dTime.getYear(), dTime.getMonthValue());
		Hashtable ht = new Hashtable();

		for (int i = 1; i < days; i++)
		{
			ht.put(i + 1, startDay.plusDays(i).toString("yyyy-MM-dd"));
		}
		ht.put(1, startDay.toString("yyyy-MM-dd"));

		boolean isExitMonth = false; //本月是否已经有插入数据
		String insertDate = null;
		DataTable dt;
		String sql = null;

		for (Map.Entry de : ht.entrySet())
		{
			sql = "SELECT * FROM ND" + Integer.parseInt(flowNo) +
				"Track  WHERE  RDT LIKE '" + de.getValue().toString() + "%' AND WorkID=" + workid + "  AND NDFrom='" +
				nodeFrom + "' AND EmpFrom='" + WebUser.getNo() + "' AND ActionType=" + ActionType.WorkCheck.getValue();

			if (DBAccess.RunSQLReturnCOUNT(sql) != 0)
			{
				isExitMonth = true;
				insertDate = de.getValue().toString();
				break;
			}
		}

		if (isExitMonth)
		{
			Paras ps = new Paras();
			ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET RDT='" + LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "' Msg=" + dbStr + "Msg WHERE  RDT LIKE '" + insertDate + "%' AND WorkID=" + dbStr + "WorkID  AND NDFrom=" + dbStr + "NDFrom AND EmpFrom=" + dbStr + "EmpFrom AND ActionType=" + ActionType.WorkCheck.getValue();
			ps.Add(TrackAttr.Msg, msg);
			ps.Add(TrackAttr.WorkID, workid);
			ps.Add(TrackAttr.NDFrom, nodeFrom);
			ps.Add(TrackAttr.EmpFrom, WebUser.getNo());

			DBAccess.RunSQL(ps);
		}
		else
		{
			WriteTrack(flowNo, nodeFrom, nodeFromName, workid, fid, msg, ActionType.WorkCheck, null, null, optionName);
		}
	}
	/** 
	 修改审核信息标识
	 比如：在默认的情况下是"审核"，现在要把ActionTypeText 修改成"组长审核。"。
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param nodeFrom 节点ID
	 @param label 要修改成的标签
	 @return 是否成功
	*/
	public static boolean WriteTrackWorkCheckLabel(String flowNo, long workid, int nodeFrom, String label)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT MyPK FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND WorkID=" + workid + " And NDTo='0' ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return false;
		}

		String pk = dt.Rows.get(0).getValue(0).toString();
		sql = "UPDATE " + table + " SET " + TrackAttr.ActionTypeText + "='" + label + "' WHERE MyPK=" + pk;
		BP.DA.DBAccess.RunSQL(sql);
		return true;
	}

	/** 
	 前进,获取等标签
	 比如：在默认的情况下是"逻辑删除"，现在要把ActionTypeText 修改成"删除(清场)。"。
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param nodeFrom 节点ID
	 @param label 要修改成的标签
	 @return 是否成功
	*/
	public static boolean WriteTrackLabel(String flowNo, long workid, int nodeFrom, String label)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT MyPK FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND WorkID=" + workid + "  ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return false;
		}

		String pk = dt.Rows.get(0).getValue(0).toString();
		sql = "UPDATE " + table + " SET " + TrackAttr.ActionTypeText + "='" + label + "' WHERE MyPK=" + pk;
		BP.DA.DBAccess.RunSQL(sql);
		return true;
	}
	/** 
	 获取Track 表中的审核的信息
	 
	 @param flowNo
	 @param workId
	 @param nodeFrom
	 @return 
	*/

	public static String GetCheckInfo(String flowNo, long workId, int nodeFrom)
	{
		return GetCheckInfo(flowNo, workId, nodeFrom, "同意");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string GetCheckInfo(string flowNo, Int64 workId, int nodeFrom, string isNullAsVal = "同意")
	public static String GetCheckInfo(String flowNo, long workId, int nodeFrom, String isNullAsVal)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Msg FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType=" + ActionType.WorkCheck.getValue() + " AND EmpFrom='" + WebUser.getNo() + "' AND WorkID=" + workId + " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return isNullAsVal;
		}
		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		if (DataType.IsNullOrEmpty(checkinfo))
		{
			return isNullAsVal;
		}

		return checkinfo;
	}

	public static String GetCheckTag(String flowNo, long workId, int nodeFrom, String empFrom)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Tag FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType=" + ActionType.WorkCheck.getValue() + " AND EmpFrom='" + empFrom + "' AND WorkID=" + workId + " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "";
		}
		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		if (DataType.IsNullOrEmpty(checkinfo))
		{
			return "";
		}

		return checkinfo;
	}

	/** 
	 获取队列节点Track 表中的审核的信息(队列节点中处理人 共享同一处理意见)
	 
	 @param flowNo
	 @param workId
	 @param nodeFrom
	 @return 
	*/
	public static String GetOrderCheckInfo(String flowNo, long workId, int nodeFrom)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Msg FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType=" + ActionType.WorkCheck.getValue() + " AND WorkID=" + workId + " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			//BP.Sys.FrmWorkCheck fwc = new FrmWorkCheck(nodeFrom);
			//return fwc.FWCDefInfo;
			return null;
		}
		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		return checkinfo;
	}
	/** 
	 删除审核信息,用于退回后.
	 
	 @param flowNo 流程编号
	 @param workId 工作ID
	 @param nodeFrom 节点从
	 @return 删除自己的审核信息
	*/
	public static void DeleteCheckInfo(String flowNo, long workId, int nodeFrom)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "DELETE FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType=" + ActionType.WorkCheck.getValue() + " AND EmpFrom='" + WebUser.getNo() + "' AND WorkID=" + workId;
		BP.DA.DBAccess.RunSQL(sql);
	}
	public static String GetAskForHelpReInfo(String flowNo, long workId, int nodeFrom)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Msg FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType=" + ActionType.AskforHelp.getValue() + " AND EmpFrom='" + WebUser.getNo() + "' AND WorkID=" + workId + " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "";
		}

		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		return checkinfo;
	}

	/** 
	 更新Track信息
	 
	 @param flowNo
	 @param workId
	 @param nodeFrom
	 @param actionType
	 @param strMsg
	 @return 
	*/
	public static void SetTrackInfo(String flowNo, long workId, int nodeFrom, int actionType, String strMsg)
	{
		String table = "ND" + Integer.parseInt(flowNo) + "Track";

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + table + " SET Msg=" + dbstr + "Msg  WHERE ActionType=" + dbstr +
			"ActionType and WorkID=" + dbstr + "WorkID and NDFrom=" + dbstr + "NDFrom";
		ps.Add("Msg", strMsg);
		ps.Add("ActionType", actionType);
		ps.Add("WorkID", workId);
		ps.Add("NDFrom", nodeFrom);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/** 
	 设置BillNo信息
	 
	 @param flowNo
	 @param workID
	 @param newBillNo
	*/
	public static void SetBillNo(String flowNo, long workID, String newBillNo)
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET BillNo=" + dbstr + "BillNo  WHERE WorkID=" + dbstr + "WorkID";
		ps.Add("BillNo", newBillNo);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET BillNo=" + dbstr + "BillNo WHERE OID=" + dbstr + "OID";
		ps.Add("BillNo", newBillNo);
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/** 
	 设置父流程信息 
	 
	 @param subFlowNo 子流程编号
	 @param subFlowWorkID 子流程workid
	 @param parentWorkID 父流程WorkID
	*/

	public static void SetParentInfo(String subFlowNo, long subFlowWorkID, long parentWorkID, String parentEmpNo)
	{
		SetParentInfo(subFlowNo, subFlowWorkID, parentWorkID, parentEmpNo, 0);
	}

	public static void SetParentInfo(String subFlowNo, long subFlowWorkID, long parentWorkID)
	{
		SetParentInfo(subFlowNo, subFlowWorkID, parentWorkID, null, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void SetParentInfo(string subFlowNo, Int64 subFlowWorkID, Int64 parentWorkID, string parentEmpNo = null, int parentNodeID = 0)
	public static void SetParentInfo(String subFlowNo, long subFlowWorkID, long parentWorkID, String parentEmpNo, int parentNodeID)
	{
		//创建父流程.
		GenerWorkFlow pgwf = new GenerWorkFlow(parentWorkID);

		if (parentNodeID != 0)
		{
			pgwf.setFK_Node(parentNodeID);
		}


		if (parentEmpNo == null)
		{
			parentEmpNo = WebUser.getNo();
		}

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET PFlowNo=" + dbstr + "PFlowNo, PWorkID=" + dbstr + "PWorkID,PNodeID=" + dbstr + "PNodeID,PEmp=" + dbstr + "PEmp WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.PFlowNo, pgwf.getFK_Flow());
		ps.Add(GenerWorkFlowAttr.PWorkID, parentWorkID);
		ps.Add(GenerWorkFlowAttr.PNodeID, pgwf.getFK_Node());
		ps.Add(GenerWorkFlowAttr.PEmp, parentEmpNo);
		ps.Add(GenerWorkFlowAttr.WorkID, subFlowWorkID);

		BP.DA.DBAccess.RunSQL(ps);


		Flow fl = new Flow(subFlowNo);
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET PFlowNo=" + dbstr + "PFlowNo, PWorkID=" + dbstr + "PWorkID,PNodeID=" + dbstr + "PNodeID, PEmp=" + dbstr + "PEmp WHERE OID=" + dbstr + "OID";
		ps.Add(NDXRptBaseAttr.PFlowNo, pgwf.getFK_Flow());
		ps.Add(NDXRptBaseAttr.PWorkID, pgwf.getWorkID());
		ps.Add(NDXRptBaseAttr.PNodeID, pgwf.getFK_Node());
		ps.Add(NDXRptBaseAttr.PEmp, parentEmpNo);
		ps.Add(NDXRptBaseAttr.OID, subFlowWorkID);

		BP.DA.DBAccess.RunSQL(ps);
	}

	public static GERpt Flow_GenerGERpt(String flowNo, long workID)
	{
		GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt", workID);
		return rpt;
	}
	/** 
	 产生一个新的工作
	 
	 @param flowNo 流程编号
	 @return 返回当前操作员创建的工作
	 * @throws Exception 
	*/
	public static Work Flow_GenerWork(String flowNo) throws Exception
	{
		Flow fl = new Flow(flowNo);
		Work wk = fl.NewWork();
		wk.ResetDefaultVal();
		return wk;
	}
	/** 
	 把流程从非正常运行状态恢复到正常运行状态
	 比如现在的流程的状态是，删除，挂起，现在恢复成正常运行。
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param msg 原因
	 @return 执行信息
	*/
	public static void Flow_DoComeBackWorkFlow(String flowNo, long workID, String msg)
	{
		WorkFlow wf = new WorkFlow(flowNo, workID);
		wf.DoComeBackWorkFlow(msg);
	}
	/** 
	 恢复已完成的流程数据到指定的节点，如果节点为0就恢复到最后一个完成的节点上去.
	 恢复失败抛出异常
	 
	 @param flowNo 要恢复的流程编号
	 @param workid 要恢复的workid
	 @param backToNodeID 恢复到的节点编号，如果是0，标示回复到流程最后一个节点上去.
	 @param note 恢复的原因，此原因会记录到日志.
	*/
	public static String Flow_DoRebackWorkFlow(String flowNo, long workid, int backToNodeID, String note)
	{
		BP.WF.Template.FlowSheet fs = new BP.WF.Template.FlowSheet(flowNo);
		return fs.DoRebackFlowData(workid, backToNodeID, note);
	}
	/** 
	 执行删除流程:彻底的删除流程.
	 清除的内容如下:
	 1, 流程引擎中的数据.
	 2, 节点数据,NDxxRpt数据.
	 3, 轨迹表数据.
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param isDelSubFlow 是否要删除它的子流程
	 @return 执行信息
	*/

	public static String Flow_DoDeleteFlowByReal(String flowNo, long workID)
	{
		return Flow_DoDeleteFlowByReal(flowNo, workID, false);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Flow_DoDeleteFlowByReal(string flowNo, Int64 workID, bool isDelSubFlow = false)
	public static String Flow_DoDeleteFlowByReal(String flowNo, long workID, boolean isDelSubFlow)
	{
		try
		{
			WorkFlow.DeleteFlowByReal(flowNo, workID, isDelSubFlow);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@删除前错误，" + ex.getStackTrace());
		}
		return "删除成功";
	}
	public static String Flow_DoDeleteDraft(String flowNo, long workID, boolean isDelSubFlow)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		gwf.RetrieveFromDBSources();
		if (!gwf.getStarter().equals(WebUser.getNo()) && WebUser.getIsAdmin() == false)
		{
			return "err@流程不是您发起的，或者您不是管理员所以您不能删除该草稿。";
		}

		//删除流程。
		gwf.Delete();

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		Paras ps = new Paras();

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL = "DELETE FROM " + fl.getPTable() + " WHERE OID=" + dbstr + "OID";
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		//删除开始节点数据.
		Node nd = fl.getHisStartNode();
		Work wk = nd.getHisWork();
		ps = new Paras();
		ps.SQL = "DELETE FROM " + wk.EnMap.PhysicsTable + " WHERE OID=" + dbstr + "OID";
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		BP.DA.Log.DefaultLogWriteLineInfo(WebUser.getName() + "删除了FlowNo 为'" + flowNo + "',workID为'" + workID + "'的数据");

		return "草稿删除成功";
	}
	/** 
	 删除已经完成的流程
	 注意:它不触发事件.
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param isDelSubFlow 是否删除子流程
	 @param note 删除原因
	 @return 删除过程信息
	*/
	public static String Flow_DoDeleteWorkFlowAlreadyComplete(String flowNo, long workID, boolean isDelSubFlow, String note)
	{
		return WorkFlow.DoDeleteWorkFlowAlreadyComplete(flowNo, workID, isDelSubFlow, note);
	}
	/** 
	 删除流程并写入日志
	 清除的内容如下:
	 1, 流程引擎中的数据.
	 2, 节点数据,NDxxRpt数据.
	 并作如下处理:
	 1, 保留track数据.
	 2, 写入流程删除记录表.
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param deleteNote 删除原因
	 @param isDelSubFlow 是否要删除它的子流程
	 @return 执行信息
	*/
	public static String Flow_DoDeleteFlowByWriteLog(String flowNo, long workID, String deleteNote, boolean isDelSubFlow)
	{
		WorkFlow wf = new WorkFlow(flowNo, workID);
		return wf.DoDeleteWorkFlowByWriteLog(deleteNote, isDelSubFlow);
	}
	/** 
	 执行逻辑删除流程:此流程并非真正的删除仅做了流程删除标记
	 比如:逻辑删除工单.
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param msg 逻辑删除的原因
	 @param isDelSubFlow 逻辑删除的原因
	 @return 执行信息,执行不成功抛出异常.
	*/
	public static String Flow_DoDeleteFlowByFlag(String flowNo, long workID, String msg, boolean isDelSubFlow)
	{
		WorkFlow wf = new WorkFlow(flowNo, workID);
		wf.DoDeleteWorkFlowByFlag(msg);
		if (isDelSubFlow)
		{
			//删除子线程
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.FID, workID);
			for (GenerWorkFlow item : gwfs.ToJavaList())
			{
				Flow_DoDeleteFlowByFlag(item.getFK_Flow(), item.getWorkID(), "删除子流程:" + msg, false);
			}
			//删除子流程
			gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, workID);
			for (GenerWorkFlow item : gwfs.ToJavaList())
			{
				Flow_DoDeleteFlowByFlag(item.getFK_Flow(), item.getWorkID(), "删除子流程:" + msg, false);
			}
		}
		return "删除成功";
	}
	/** 
	 撤销删除流程
	 说明:如果一个流程处于逻辑删除状态,要回复正常运行状态,就执行此接口.
	 
	 @param flowNo 流程编号
	 @param workID 工作流程ID
	 @param msg 撤销删除的原因
	 @return 执行消息,如果撤销不成功则抛出异常.
	*/
	public static String Flow_DoUnDeleteFlowByFlag(String flowNo, long workID, String msg)
	{
		WorkFlow wf = new WorkFlow(flowNo, workID);
		wf.DoUnDeleteWorkFlowByFlag(msg);
		return "撤销删除成功.";
	}
	/** 
	 执行-撤销发送
	 说明:如果流程转入了下一个节点,就会执行失败,就会抛出异常.
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @return 返回成功执行信息
	*/

	public static String Flow_DoUnSend(String flowNo, long workID, int unSendToNode)
	{
		return Flow_DoUnSend(flowNo, workID, unSendToNode, 0);
	}

	public static String Flow_DoUnSend(String flowNo, long workID)
	{
		return Flow_DoUnSend(flowNo, workID, 0, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Flow_DoUnSend(string flowNo, Int64 workID, int unSendToNode = 0, Int64 fid = 0)
	public static String Flow_DoUnSend(String flowNo, long workID, int unSendToNode, long fid)
	{

		WorkUnSend unSend = new WorkUnSend(flowNo, workID, unSendToNode, fid);
		unSend.UnSendToNode = unSendToNode;

		return unSend.DoUnSend();
	}
	/** 
	 获得当前节点上一步发送日志记录
	 
	 @param WorkID 工作流程ID
	 @param FK_Node 当前节点编号
	 @return 上一节点发送记录
	*/
	public static DataTable Flow_GetPreviousNodeTrack(long WorkID, int FK_Node)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("没有查询到相关业务实例");
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras pas = new Paras();
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				pas.SQL = "SELECT TOP 1 * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + dbstr + "WorkID  AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue() + ") ORDER BY RDT DESC";
				break;
			case Oracle:
				pas.SQL = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track  WHERE WorkID=" + dbstr + "WorkID  AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue() + ") AND ROWNUM=1 ORDER BY RDT DESC ";
				break;
			case MySQL:
				pas.SQL = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track  WHERE WorkID=" + dbstr + "WorkID AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue() + ") ORDER BY RDT DESC limit 0,1 ";
				break;
			case PostgreSQL:
				pas.SQL = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track  WHERE WorkID=" + dbstr + "WorkID AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue() + ") ORDER BY RDT DESC limit 1 ";
				break;
			default:
				break;
		}
		pas.Add("WorkID", WorkID);
		pas.Add("NDTo", FK_Node);
		return BP.DA.DBAccess.RunSQLReturnTable(pas);
	}

	/** 
	 执行冻结
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param isFixSubFlows 是否冻结子流程？
	 @param msg 冻结原因.
	 @return 冻结的信息.
	*/
	public static String Flow_DoFix(String flowNo, long workid, boolean isFixSubFlows, String msg)
	{
		String info = "";
		try
		{
			// 执行冻结.
			WorkFlow wf = new WorkFlow(flowNo, workid);
			info = wf.DoFix(msg);
		}
		catch (RuntimeException ex)
		{
			info += ex.getMessage();
		}

		if (isFixSubFlows == false)
		{
			return info;
		}

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, workid);

		for (GenerWorkFlow item : gwfs.ToJavaList())
		{
			try
			{
				// 执行冻结.
				WorkFlow wf = new WorkFlow(flowNo, workid);
				info += wf.DoFix(msg);
			}
			catch (RuntimeException ex)
			{
				info += "err@" + ex.getMessage();
			}
		}

		return info;
	}
	/** 
	 执行解除冻结
	 于挂起的区别是,冻结需要有权限的人才可以执行解除冻结，
	 挂起是自己的工作可以挂起也可以解除挂起。
	 
	 @param flowNo 流程编号
	 @param workid workid
	 @param msg 解除原因
	*/
	public static String Flow_DoUnFix(String flowNo, long workid, String msg)
	{
		// 执行冻结.
		WorkFlow wf = new WorkFlow(flowNo, workid);
		return wf.DoUnFix(msg);
	}
	/** 
	 执行流程结束
	 说明:正常的流程结束.
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param msg 流程结束原因
	 @return 返回成功执行信息
	*/

	public static String Flow_DoFlowOver(String flowNo, long workID, String msg)
	{
		return Flow_DoFlowOver(flowNo, workID, msg, 1);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Flow_DoFlowOver(string flowNo, Int64 workID, string msg, int stopFlowType = 1)
	public static String Flow_DoFlowOver(String flowNo, long workID, String msg, int stopFlowType)
	{
		WorkFlow wf = new WorkFlow(flowNo, workID);

		Node nd = new Node(wf.getHisGenerWorkFlow().getFK_Node());
		GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt");
		rpt.setOID(workID);
		rpt.RetrieveFromDBSources();
		msg = wf.DoFlowOver(ActionType.FlowOver, msg, nd, rpt, stopFlowType);

		msg += FlowOverAutoSendParentOrSameLevelFlow(wf.getHisGenerWorkFlow(), wf.getHisFlow());

		return msg;
	}

	/** 
	 流程运行完成后自动运行/结束父流程或者同级子流程
	*/
	public static String FlowOverAutoSendParentOrSameLevelFlow(GenerWorkFlow gwf, Flow flow)
	{
		//判断当前流程是否子流程，是否启用该流程结束后，主流程自动运行到下一节点@yuan
		if (gwf.getPWorkID() != 0)
		{
			long slWorkID = gwf.GetParaInt("SLWorkID");
			if (slWorkID == 0) //启动该流程的是父子流程
			{
				SubFlows subFlows = new SubFlows();
				int count = subFlows.Retrieve(SubFlowAttr.FK_Node, gwf.getPNodeID(), SubFlowAttr.SubFlowNo, flow.No);
				if (count == 0)
				{
					throw new RuntimeException("父子流程关系配置信息丢失，请联系管理员");
				}
				SubFlow subFlow = subFlows[0] instanceof SubFlow ? (SubFlow)subFlows[0] : null;
				if (flow.getIsToParentNextNode() == true || subFlow.getIsAutoSendSubFlowOver() == 1)
				{
					//主流程自动运行到一下节点
					SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(gwf.getPFlowNo(), gwf.getPWorkID());
					String sendSuccess = "父流程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";
					return sendSuccess;
				}
				//结束父流程
				if (subFlow.getIsAutoSendSubFlowOver() == 2)
				{
					Flow fl = new Flow(gwf.getPFlowNo());
					String flowOver = BP.WF.Dev2Interface.Flow_DoFlowOver(gwf.getPFlowNo(), gwf.getPWorkID(), "父流程[" + fl.getName() + "],WorkID为[" + gwf.getPWorkID() + "]成功结束");
					return flowOver;
				}

			}
			else // 启动的是同级子流程
			{
				String slFlowNo = gwf.GetParaString("SLFlowNo");
				int slNodeID = gwf.GetParaInt("SLNodeID");

				SubFlows subFlows = new SubFlows();
				int count = subFlows.Retrieve(SubFlowAttr.FK_Node, slNodeID, SubFlowAttr.SubFlowNo, flow.No);
				if (count == 0)
				{
					throw new RuntimeException("同级子流程关系配置信息丢失，请联系管理员");
				}
				SubFlow subFlow = subFlows[0] instanceof SubFlow ? (SubFlow)subFlows[0] : null;
				Flow fl = new Flow(slFlowNo);
				if (subFlow.getIsAutoSendSLSubFlowOver() == 1)
				{
					//主流程自动运行到一下节点
					SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(slFlowNo, slWorkID);
					String sendSuccess = "同级子流程[" + fl.getName() + "]程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";
					return sendSuccess;
				}
				//结束父流程
				if (subFlow.getIsAutoSendSLSubFlowOver() == 2)
				{
					return BP.WF.Dev2Interface.Flow_DoFlowOver(slFlowNo, slWorkID, "同级子流程流程[" + fl.getName() + "],WorkID为[" + slWorkID + "]成功结束");
				}

			}
		}
		return "";
	}


	/** 
	 获得执行下一步骤的节点ID，这个功能是在流程未发送前可以预先知道
	 它就要到达那一个节点上去,以方便在当前节点发送前处理业务逻辑.
	 1,首先保证当前人员是可以执行当前节点的工作.
	 2,其次保证获取下一个节点只有一个.
	 
	 @param fk_flow 流程编号
	 @param workid 工作ID
	 @return 下一步骤的所要到达的节点, 如果获取不到就会抛出异常.
	*/
	public static int Node_GetNextStepNode(String fk_flow, long workid)
	{
		////检查当前人员是否可以执行当前工作.
		//if (BP.WF.Dev2Interface.Flow_CheckIsCanDoCurrentWork( workid, WebUser.getNo()) == false)
		//    throw new Exception("@当前人员不能执行此节点上的工作.");

		//获取当前nodeID.
		int currNodeID = BP.WF.Dev2Interface.Node_GetCurrentNodeID(fk_flow, workid);

		//获取
		Node nd = new Node(currNodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		WorkNode wn = new WorkNode(wk, nd);
		return wn.NodeSend_GenerNextStepNode().getNodeID();
	}
	/** 
	 获取指定的workid 在运行到的节点编号
	 
	 @param workID 需要找到的workid
	 @return 返回节点编号. 如果没有找到，就会抛出异常.
	*/
	public static int Flow_GetCurrentNode(long workID)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("WorkID", workID);
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}
	/** 
	 获取指定节点的Work
	 
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @return 当前工作
	*/
	public static Work Flow_GetCurrentWork(int nodeID, long workID)
	{
		Node nd = new Node(nodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workID);
		wk.Retrieve();
		return wk;
	}
	/** 
	 获取当前工作节点的Work
	 
	 @param workID 工作ID
	 @return 当前工作节点的Work
	*/
	public static Work Flow_GetCurrentWork(long workID)
	{
		Node nd = new Node(Flow_GetCurrentNode(workID));
		Work wk = nd.getHisWork();
		wk.setOID(workID);
		wk.Retrieve();
		wk.ResetDefaultVal();
		return wk;
	}
	/** 
	 指定 workid 当前节点由哪些人可以执行.
	 
	 @param workID 需要找到的workid
	 @return 返回当前处理人员列表,数据结构与WF_GenerWorkerList一致.
	*/
	public static DataTable Flow_GetWorkerList(long workID)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM WF_GenerWorkerList WHERE IsEnable=1 AND IsPass=0 AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("WorkID", workID);
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 根据流程标记获得流程编号
	 
	 @param flowMark 流程属性的流程标记
	 @return 流程编号
	*/
	public static String Flow_GetFlowNoByFlowMark(String flowMark)
	{
		String sql = "SELECT No FROM WF_Flow WHERE FlowMark='" + flowMark + "'";
		return DBAccess.RunSQLReturnStringIsNull(sql, null);
	}
	/** 
	 检查是否可以发起流程
	 
	 @param flowNo 流程编号
	 @param userNo 用户编号
	 @return 是否可以发起当前流程
	*/

	public static boolean Flow_IsCanStartThisFlow(String flowNo, String userNo, String pFlowNo, int pNodeID)
	{
		return Flow_IsCanStartThisFlow(flowNo, userNo, pFlowNo, pNodeID, 0);
	}

	public static boolean Flow_IsCanStartThisFlow(String flowNo, String userNo, String pFlowNo)
	{
		return Flow_IsCanStartThisFlow(flowNo, userNo, pFlowNo, 0, 0);
	}

	public static boolean Flow_IsCanStartThisFlow(String flowNo, String userNo)
	{
		return Flow_IsCanStartThisFlow(flowNo, userNo, null, 0, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static bool Flow_IsCanStartThisFlow(string flowNo, string userNo, string pFlowNo = null, int pNodeID = 0, Int64 pworkID = 0)
	public static boolean Flow_IsCanStartThisFlow(String flowNo, String userNo, String pFlowNo, int pNodeID, long pworkID)
	{

			///#region 判断开始节点是否可以发起.
		Node nd = new Node(Integer.parseInt(flowNo + "01"));
		if (nd.getIsGuestNode() == true)
		{
			if (!WebUser.getNo().equals("Guest"))
			{
				throw new RuntimeException("@当前节点是来宾处理节点，但是目前您{" + WebUser.getNo() + "}不是来宾帐号。");
			}
			return true;
		}

		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		int num = 0;
		if (SystemConfig.getOSDBSrc() == OSDBSrc.Database)
		{
			switch (nd.getHisDeliveryWay())
			{
				case ByStation:
				case ByStationOnly:
					ps.SQL = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeStation A, " + BP.WF.Glo.getEmpStation() + " B WHERE A.FK_Station= B.FK_Station AND  A.FK_Node=" + dbstr + "FK_Node AND B.FK_Emp=" + dbstr + "FK_Emp";
					ps.Add("FK_Node", nd.getNodeID());
					ps.Add("FK_Emp", userNo);
					num = DBAccess.RunSQLReturnValInt(ps);
					break;
				case ByDept:
					 
						ps.SQL = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeDept A, Port_DeptEmp B WHERE A.FK_Dept= B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node AND B.FK_Emp=" + dbstr + "FK_Emp";
						ps.Add("FK_Node", nd.getNodeID());
						ps.Add("FK_Emp", userNo);
						num = DBAccess.RunSQLReturnValInt(ps);

						if (num == 0)
						{
							ps.Clear();
							ps.SQL = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeDept A, Port_Emp B WHERE A.FK_Dept= B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node AND B.No=" + dbstr + "FK_Emp";
							ps.Add("FK_Node", nd.getNodeID());
							ps.Add("FK_Emp", userNo);
							num = DBAccess.RunSQLReturnValInt(ps);
						}
					 
					break;
				case ByBindEmp:
					ps.SQL = "SELECT COUNT(*) AS Num FROM WF_NodeEmp WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Node=" + dbstr + "FK_Node";
					ps.Add("FK_Emp", userNo);
					ps.Add("FK_Node", nd.getNodeID());
					num = DBAccess.RunSQLReturnValInt(ps);
					break;
				case ByDeptAndStation:

					if (SystemConfig.OSModel == OSModel.OneOne)
					{
						String sql = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeDept A, Port_Emp B, WF_NodeStation C, " + Glo.getEmpStation() + " D";
						sql += " WHERE A.FK_Dept= B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node AND B.No=" + dbstr + "FK_Emp AND  A.FK_Node=C.FK_Node AND C.FK_Station=D.FK_Station AND D.FK_Emp=" + dbstr + "FK_Emp";
						ps.SQL = sql;
						ps.Add("FK_Node", nd.getNodeID());
						ps.Add("FK_Emp", userNo);
						num = DBAccess.RunSQLReturnValInt(ps);
					}
					else
					{
						String sql = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeDept A, Port_DeptEmp B, WF_NodeStation C, " + Glo.getEmpStation() + " D";
						sql += " WHERE A.FK_Dept= B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node AND B.FK_Emp=" + dbstr + "FK_Emp AND  A.FK_Node=C.FK_Node AND C.FK_Station=D.FK_Station AND D.FK_Emp=" + dbstr + "FK_Emp";
						ps.SQL = sql;
						ps.Add("FK_Node", nd.getNodeID());
						ps.Add("FK_Emp", userNo);
						num = DBAccess.RunSQLReturnValInt(ps);
					}
					break;
				case BySelected:
					num = 1;
					break;
				default:
					throw new RuntimeException("@开始节点不允许设置此访问规则：" + nd.getHisDeliveryWay());
			}
		}
		else
		{
			switch (nd.getHisDeliveryWay())
			{
				case ByStation:
					//var obj = BP.DA.DataType.GetPortalInterfaceSoapClientInstance();
					//DataTable mydt = obj.GetEmpHisStations(WebUser.getNo());
					//string mystas = BP.DA.DBAccess.GenerWhereInPKsString(mydt);
					//ps.SQL = "SELECT COUNT(FK_Node) AS Num FROM WF_NodeStation WHERE FK_Node=" + dbstr + "FK_Node AND FK_Station IN(" + mystas + ")";
					//ps.Add("FK_Node", nd.NodeID);
					//num = DBAccess.RunSQLReturnValInt(ps);
					break;
				case ByDept:
					//var objMy = BP.DA.DataType.GetPortalInterfaceSoapClientInstance();
					//DataTable mydtDept = objMy.GetEmpHisDepts(WebUser.getNo());
					//string dtps = BP.DA.DBAccess.GenerWhereInPKsString(mydtDept);

					//ps.SQL = "SELECT COUNT(FK_Node) as Num FROM WF_NodeDept WHERE FK_Dept IN (" + dtps + ") B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node";
					//ps.Add("FK_Node", nd.NodeID);
					//num = DBAccess.RunSQLReturnValInt(ps);
					throw new RuntimeException("@目前取消支持.");
					break;
				case ByBindEmp:
					ps.SQL = "SELECT COUNT(*) AS Num FROM WF_NodeEmp WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Node=" + dbstr + "FK_Node";
					ps.Add("FK_Emp", userNo);
					ps.Add("FK_Node", nd.getNodeID());
					num = DBAccess.RunSQLReturnValInt(ps);
					break;
				case BySelected:
					num = 1;
					break;
				default:
					throw new RuntimeException("@开始节点不允许设置此访问规则：" + nd.getHisDeliveryWay());
			}
		}
		if (num == 0)
		{
			return false;
		}

		if (pFlowNo == null)
		{
			return true;
		}

			///#endregion 判断开始节点是否可以发起.


			///#region 检查流程发起限制规则. 为周大福项目增加判断.
		if (pNodeID == 0)
		{
			return true;
		}

		//当前节点所有配置的子流程.
		SubFlowHands subflows = new SubFlowHands(pNodeID);

		//当前的子流程.
		for (SubFlowHand item : subflows)
		{
			if (item.getSubFlowNo().equals(flowNo) == false)
			{
				continue;
			}

			if (item.getStartOnceOnly() == true)
			{
				String sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkID + " AND FK_Flow='" + flowNo + "' AND WFState >=2 ";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
				{
					// return true; //没有人发起，他可以发起。
				}
				else
				{
					throw new RuntimeException("该流程只能允许发起一次.");
				}
			}

			if (item.getCompleteReStart() == true)
			{
				String sql = "SELECT Starter, RDT,WFState FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkID + " AND FK_Flow='" + flowNo + "' AND WFState != 3";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() != 0)
				{
					if (dt.Rows.size() == 1 && Integer.parseInt(dt.Rows[0]["WFState"].toString()) == 0)
					{

					}
					else
					{
						throw new RuntimeException("该流程已经启动还没有运行结束，不能再次启动.");
					}


				}

			}


			if (item.getIsEnableSpecFlowStart() == true)
			{
				//指定的流程发起之后，才能启动该流程。
				String[] fls = item.getSpecFlowStart().split("[,]", -1);
				for (String flStr : fls)
				{
					if (DataType.IsNullOrEmpty(flStr) == true)
					{
						continue;
					}

					String sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkID + " AND FK_Flow='" + flStr + "' AND WFState >=2 ";
					DataTable dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						BP.WF.Flow myflow = new Flow(flStr);
						throw new RuntimeException("流程:[" + myflow.Name + "]没有发起,您不能启动[" + item.getSubFlowName() + "]。");
					}
				}
			}

			if (item.getIsEnableSpecFlowOver() == true)
			{
				//指定的流程发起之后，才能启动该流程。
				String[] fls = item.getSpecFlowOver().split("[,]", -1);
				for (String flStr : fls)
				{
					if (DataType.IsNullOrEmpty(flStr) == true)
					{
						continue;
					}

					String sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkID + " AND FK_Flow='" + flStr + "' AND WFState =3 ";
					DataTable dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						BP.WF.Flow myflow = new Flow(flStr);
						throw new RuntimeException("流程:[" + myflow.Name + "]没有完成,您不能启动[" + item.getSubFlowName() + "]。");
					}
				}
			}
		}

			///#endregion 检查流程发起限制规则.


			///#region 判断流程属性的规则.
		Flow fl = new Flow(flowNo);
		if (fl.getStartLimitRole() == StartLimitRole.None)
		{
			return true;
		}

		//只有一个子流程,才能发起.
		if (fl.getStartLimitRole() == StartLimitRole.OnlyOneSubFlow)
		{
			if (pworkID == 0)
			{
				return true;
			}

			String sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkID + " AND FK_Flow='" + fl.getNo() + "' AND WFState >=2 ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				return true;
			}

			throw new RuntimeException("该流程只能允许发起一个子流程.");
		}

			///#endregion 判断流程属性的规则.

		return true;
	}
	/** 
	 获得正在运行中的子流程的数量
	 
	 @param workID 父流程的workid
	 @return 获得正在运行中的子流程的数量。如果是0，表示所有的流程的子流程都已经结束。
	*/
	public static int Flow_NumOfSubFlowRuning(long pWorkID)
	{
		String sql = "SELECT COUNT(*) AS num FROM WF_GenerWorkFlow WHERE WFState!=" + WFState.Complete.getValue() + " AND PWorkID=" + pWorkID;
		return DBAccess.RunSQLReturnValInt(sql);
	}
	/** 
	 获得正在运行中的子流程的数量
	 
	 @param pWorkID 父流程的workid
	 @param currWorkID 不包含当前的工作节点ID
	 @param workID 父流程的workid
	 @return 获得正在运行中的子流程的数量。如果是0，表示所有的流程的子流程都已经结束。
	*/
	public static int Flow_NumOfSubFlowRuning(long pWorkID, long currWorkID)
	{
		String sql = "SELECT COUNT(*) AS num FROM WF_GenerWorkFlow WHERE WFState!=" + WFState.Complete.getValue() + " AND WorkID!=" + currWorkID + " AND PWorkID=" + pWorkID;
		return DBAccess.RunSQLReturnValInt(sql);
	}
	public static boolean Flow_IsInGenerWork(long workID)
	{

		if (workID == 0)
		{
			return false;
		}

		String sql = "select * from WF_Generworkflow where WorkID='" + workID + "'";
		return DBAccess.RunSQLReturnCOUNT(sql) > 0;
	}
	/** 
	 检查指定节点上的所有子流程是否完成？
	 For: 深圳熊伟.
	 
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @return 返回该节点上的子流程是否完成？
	*/
	public static boolean Flow_CheckAllSubFlowIsOver(int nodeID, long workID)
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE  PNodeID=" + dbstr + "PNodeID AND PWorkID=" + dbstr + "PWorkID AND WFState!=" + dbstr + "WFState ";
		ps.Add(GenerWorkFlowAttr.PNodeID, nodeID);
		ps.Add(GenerWorkFlowAttr.PWorkID, workID);
		ps.Add(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());

		if (BP.DA.DBAccess.RunSQLReturnValInt(ps) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 检查当前人员是否有权限处理当前的工作
	 
	 @param workID
	 @param userNo
	 @return 
	*/
	public static boolean Flow_IsCanDoCurrentWork(long workID, String userNo)
	{
		if (workID == 0)
		{
			return true;
		}

		GenerWorkFlow mygwf = new GenerWorkFlow(workID);

		if (mygwf.getTodoEmps().indexOf(userNo + ",") >= 0)
		{
			GenerWorkerList gwl = new GenerWorkerList();
			int inum = gwl.Retrieve(GenerWorkerListAttr.WorkID, workID, GenerWorkerListAttr.FK_Emp, userNo, GenerWorkerListAttr.FK_Node, mygwf.getFK_Node());
			if (inum == 1 && gwl.getIsPassInt() == 0)
			{
				return true;
			}
		}



			///#region 判断是否是开始节点.
		/* 判断是否是开始节点 . */
		String str = String.valueOf(mygwf.getFK_Node());
		if (str.endsWith("01") == true)
		{
			String mysql = "SELECT FK_Emp, IsPass FROM WF_GenerWorkerList WHERE WorkID=" + workID + " AND FK_Node=" + mygwf.getFK_Node();
			DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
			if (mydt.Rows.size() == 0)
			{
				return true;
			}

			for (DataRow dr : mydt.Rows)
			{
				String fk_emp = dr.get(0).toString();
				String isPass = dr.get(1).toString();
				if (userNo.equals(fk_emp) && (isPass.equals("0") || isPass.equals("80") || isPass.equals("90")))
				{
					return true;
				}
			}
			return false;
		}

			///#endregion 判断是否是开始节点.

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT c.RunModel,c.IsGuestNode, a.GuestNo, a.TaskSta, a.WFState, IsPass FROM WF_GenerWorkFlow a, WF_GenerWorkerlist b, WF_Node c WHERE  b.FK_Node=c.NodeID AND a.WorkID=b.WorkID AND a.FK_Node=b.FK_Node  AND b.FK_Emp=" + dbstr + "FK_Emp AND (b.IsEnable=1 OR b.IsPass>=70 OR IsPass=0)   AND a.WorkID=" + dbstr + "WorkID ";
		ps.Add("FK_Emp", userNo);
		ps.Add("WorkID", workID);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() == 0)
		{
			return false;
		}

		//判断是否是待办.
		int myisPass = Integer.parseInt(dt.Rows[0]["IsPass"].toString());

		//新增加的标记,=90 就是会签主持人执行会签的状态. 翻译.
		if (myisPass == 90)
		{
			return true;
		}

		if (myisPass == 80)
		{
			return true;
		}

		if (myisPass != 0)
		{
			return false;
		}

		WFState wfsta = WFState.forValue(Integer.parseInt(dt.Rows[0]["WFState"].toString()));
		if (wfsta == WFState.Complete)
		{
			return false;
		}

		if (wfsta == WFState.Delete)
		{
			return false;
		}

		//判断是否是客户处理节点. 
		int isGuestNode = Integer.parseInt(dt.Rows[0]["IsGuestNode"].toString());
		if (isGuestNode == 1)
		{
			if (dt.Rows[0]["GuestNo"].toString().equals(BP.Web.GuestUser.No))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		int i = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());

		RunModel rm = RunModel.forValue(i);
		switch (rm)
		{
			case Ordinary:
				return true;
			case FL:
				return true;
			case HL:
				return true;
			case FHL:
				return true;
			case SubThread:
				return true;
			default:
				break;
		}
		return true;
	}
	/** 
	 检查当前人员是否有权限处理当前的工作.
	 
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @param userNo 要判断的操作人员
	 @return 返回指定的人员是否有操作当前工作的权限
	*/
	public static boolean Flow_IsCanDoCurrentWorkGuest(int nodeID, long workID, String userNo)
	{
		if (workID == 0)
		{
			return true;
		}

		if (userNo.equals("admin"))
		{
			return true;
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		//ps.SQL = "SELECT c.RunModel FROM WF_GenerWorkFlow a , WF_GenerWorkerlist b, WF_Node c WHERE a.FK_Node=" + dbstr + "FK_Node AND b.FK_Node=c.NodeID AND a.WorkID=b.WorkID AND a.FK_Node=b.FK_Node  AND b.FK_Emp=" + dbstr + "FK_Emp AND b.IsEnable=1 AND a.workid=" + dbstr + "WorkID";
		//ps.Add("FK_Node", nodeID);
		//ps.Add("FK_Emp", userNo);
		//ps.Add("WorkID", workID);
		String sql = "SELECT c.RunModel, a.TaskSta FROM WF_GenerWorkFlow a , WF_GenerWorkerlist b, WF_Node c WHERE a.FK_Node='" + nodeID + "'  AND b.FK_Node=c.NodeID AND a.WorkID=b.WorkID AND a.FK_Node=b.FK_Node  AND b.GuestNo='" + userNo + "' AND b.IsEnable=1 AND a.WorkID=" + workID;

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return false;
		}

		int i = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());
		TaskSta TaskStai = TaskSta.forValue(Integer.parseInt(dt.Rows[0][1].toString()));
		if (TaskStai == TaskSta.Sharing)
		{
			return false;
		}

		RunModel rm = RunModel.forValue(i);
		switch (rm)
		{
			case Ordinary:
				return true;
			case FL:
				return true;
			case HL:
				return true;
			case FHL:
				return true;
			case SubThread:
				return true;
			default:
				break;
		}

		if (DBAccess.RunSQLReturnValInt(ps) == 0)
		{
			return false;
		}

		return true;
	}
	/** 
	 是否可以查看流程数据
	 用于判断是否可以查看流程轨迹图.
	 edit: stone 2015-03-25
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param fid FID
	 @return 
	*/

	public static boolean Flow_IsCanViewTruck(String flowNo, long workid)
	{
		return Flow_IsCanViewTruck(flowNo, workid, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static bool Flow_IsCanViewTruck(string flowNo, Int64 workid, string userNo = null)
	public static boolean Flow_IsCanViewTruck(String flowNo, long workid, String userNo)
	{
		if (userNo == null)
		{
			userNo = WebUser.getNo();
		}
		if (userNo.equals("admin"))
		{
			return true;
		}

		//先从轨迹里判断.
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT count(MyPK) as Num FROM ND" + Integer.parseInt(flowNo) + "Track WHERE (WorkID=" + dbStr + "WorkID OR FID=" + dbStr + "FID) AND (EmpFrom=" + dbStr + "Emp1 OR EmpTo=" + dbStr + "Emp2)";
		ps.Add(BP.WF.TrackAttr.WorkID, workid);
		ps.Add(BP.WF.TrackAttr.FID, workid);
		ps.Add("Emp1", WebUser.getNo());
		ps.Add("Emp2", WebUser.getNo());

		if (BP.DA.DBAccess.RunSQLReturnValInt(ps) > 1)
		{
			return true;
		}

		//在查看该流程的发起者，与当前人是否在同一个部门，如果是也返回true.
		ps = new Paras();
		ps.SQL = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID=" + dbStr + "WorkID OR WorkID=" + dbStr + "FID";
		ps.Add(BP.WF.TrackAttr.WorkID, workid);
		ps.Add(BP.WF.TrackAttr.FID, workid);

		String fk_dept = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
		if (fk_dept == null)
		{
			BP.WF.Flow fl = new Flow(flowNo);
			ps.SQL = "SELECT FK_Dept FROM " + fl.getPTable() + " WHERE OID=" + dbStr + "WorkID OR OID=" + dbStr + "FID";
			fk_dept = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (fk_dept == null)
			{
				throw new RuntimeException("@流程引擎数据被删除.");
			}
		}
		if (fk_dept.equals(WebUser.getFK_Dept()))
		{
			return true;
		}

		return false;
	}
	/** 
	 删除子线程
	 
	 @param flowNo 流程编号
	 @param workid 子线程的工作ID
	 @param info 删除信息
	*/
	public static String Flow_DeleteSubThread(String flowNo, long workid, String info)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.SetValByKey(GenerWorkFlowAttr.WorkID, workid);
		if (gwf.RetrieveFromDBSources() > 0)
		{
			WorkFlow wf = new WorkFlow(flowNo, workid);
			String msg = wf.DoDeleteWorkFlowByReal(false);

			BP.WF.Dev2Interface.WriteTrackInfo(flowNo, gwf.getFK_Node(), gwf.getNodeName(), gwf.getFID(), 0, info, "删除子线程");
			return msg;
		}
		return null;
	}
	/** 
	 执行工作催办
	 
	 @param workID 工作ID
	 @param msg 催办消息
	 @param isPressSubFlow 是否催办子流程？
	 @return 返回执行结果
	*/

	public static String Flow_DoPress(long workID, String msg)
	{
		return Flow_DoPress(workID, msg, false);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Flow_DoPress(Int64 workID, string msg, bool isPressSubFlow = false)
	public static String Flow_DoPress(long workID, String msg, boolean isPressSubFlow)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workID);

		/*找到当前待办的工作人员*/
		GenerWorkerLists wls = new GenerWorkerLists(workID, gwf.getFK_Node());
		String toEmp = "", toEmpName = "";
		String mailTitle = "催办:" + gwf.getTitle() + ", 发送人:" + WebUser.getName();
		//如果子线程找不到流转日志并且父流程编号不为空，在父流程进行查找接收人
		if (wls.size() == 0 && gwf.getFID() != 0)
		{
			wls = new GenerWorkerLists(gwf.getFID(), gwf.getFK_Node());
		}

		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.getIsEnable() == false)
			{
				continue;
			}

			toEmp += wl.getFK_Emp() + ",";
			toEmpName += wl.getFK_EmpText() + ",";

			// 发消息.
			BP.WF.Dev2Interface.Port_SendMsg(wl.getFK_Emp(), mailTitle, msg, null, BP.WF.SMSMsgType.DoPress, gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());

			wl.setPressTimes(wl.getPressTimes() + 1);
			wl.Update();

			//wl.Update(GenerWorkerListAttr.PressTimes, wl.PressTimes + 1);
		}

		//写入日志.
		WorkNode wn = new WorkNode(workID, gwf.getFK_Node());
		wn.AddToTrack(ActionType.Press, toEmp, toEmpName, gwf.getFK_Node(), gwf.getNodeName(), msg);

		//如果催办子流程.
		if (isPressSubFlow)
		{
			String subMsg = "";
			GenerWorkFlows gwfs = gwf.getHisSubFlowGenerWorkFlows();
			for (GenerWorkFlow item : gwfs.ToJavaList())
			{
				subMsg += "@已经启动对子线程:" + item.getTitle() + "的催办,消息如下:";
				subMsg += Flow_DoPress(item.getWorkID(), msg, false);
			}
			return "系统已经把您的信息通知给:" + toEmpName + subMsg;
		}
		else
		{
			return "系统已经把您的信息通知给:" + toEmpName;
		}
	}
	/** 
	 重新设置流程标题
	 可以在节点的任何位置调用它,产生新的标题。
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @return 是否设置成功
	*/
	public static boolean Flow_ReSetFlowTitle(String flowNo, int nodeID, long workid)
	{
		Node nd = new Node(nodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.RetrieveFromDBSources();
		Flow fl = nd.getHisFlow();
		String title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);
		return Flow_SetFlowTitle(flowNo, workid, title);
	}
	/** 
	 设置流程参数
	 该参数，用户可以在流程实例中获得到.
	 
	 @param workid 工作ID
	 @param paras 参数,格式：@GroupMark=xxxx@IsCC=1
	 @return 是否设置成功
	*/
	public static boolean Flow_SetFlowParas(String flowNo, long workid, String paras)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("创建流程ID不存在.");
		}

		String[] strs = paras.split("[@]", -1);
		for (String item : strs)
		{
			if (DataType.IsNullOrEmpty(item))
			{
				continue;
			}
			//GroupMark=xxxx
			String[] mystr = item.split("[=]", -1);
			gwf.SetPara(mystr[0], mystr[1]);
		}
		gwf.Update();
		return true;
	}
	/** 
	 设置流程应完成日期.
	 
	 @param workid 工作ID
	 @param sdt 应完成日期
	*/
	public static void Flow_SetSDTOfFlow(long workid, String sdt)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setSDTOfFlow(sdt);
		gwf.Update();
	}
	/** 
	 设置流程标题
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param title 标题
	 @return 是否设置成功
	*/
	public static boolean Flow_SetFlowTitle(String flowNo, long workid, String title)
	{
		//替换标题中出现的英文 ""引号，造成在获取数据时，造成异常
		title = title.replace('"', '“');
		title = title.replace('"', '”');

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + dbstr + "Title WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.Title, title);
		ps.Add(GenerWorkFlowAttr.WorkID, workid);
		DBAccess.RunSQL(ps);

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET Title=" + dbstr + "Title WHERE OID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.Title, title);
		ps.Add(GenerWorkFlowAttr.WorkID, workid);
		int num = DBAccess.RunSQL(ps);


		if (fl.getHisDataStoreModel() == DataStoreModel.ByCCFlow)
		{
			//ps = new Paras();
			//ps.SQL = "UPDATE ND" + int.Parse(flowNo + "01") + " SET Title=" + dbstr + "Title WHERE OID=" + dbstr + "WorkID";
			//ps.Add(GenerWorkFlowAttr.Title, title);
			//ps.Add(GenerWorkFlowAttr.WorkID, workid);
			//DBAccess.RunSQL(ps);
		}

		if (num == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/** 
	 调度流程
	 说明：
	 1，通常是由admin执行的调度。
	 2，特殊情况下，需要从一个人的待办调度到另外指定的节点，制定的人员上。
	 
	 @param workid 工作ID
	 @param toNodeID 调度到节点
	 @param toEmper 调度到人员
	*/
	public static String Flow_Schedule(long workid, int toNodeID, String toEmper)
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		Node nd = new Node(toNodeID);
		Emp emp = new Emp(toEmper);

		// 找到GenerWorkFlow,并执行更新.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setWFState(WFState.Runing);
		gwf.setTaskSta(TaskSta.None);
		gwf.setTodoEmps(toEmper + "," + emp.Name + ";");
		gwf.setFK_Node(toNodeID);
		gwf.setNodeName(nd.getName());
		//gwf.StarterName =emp.Name;
		gwf.Update();

		//让其都设置完成。
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=1 WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.WorkID, workid);
		BP.DA.DBAccess.RunSQL(ps);

		// 更新流程数据信息。
		Flow fl = new Flow(gwf.getFK_Flow());
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET FlowEnder=" + dbstr + "FlowEnder,FlowEndNode=" + dbstr + "FlowEndNode WHERE OID=" + dbstr + "OID";
		ps.Add(NDXRptBaseAttr.FlowEnder, toEmper);
		ps.Add(NDXRptBaseAttr.FlowEndNode, toNodeID);
		ps.Add(NDXRptBaseAttr.OID, workid);
		BP.DA.DBAccess.RunSQL(ps);

		// 执行更新.
		GenerWorkerLists gwls = new GenerWorkerLists(workid);
		GenerWorkerList gwl = gwls[gwls.size() - 1] instanceof GenerWorkerList ? (GenerWorkerList)gwls[gwls.size() - 1] : null; //获得最后一个。
		gwl.setWorkID(workid);
		gwl.setFK_Node(toNodeID);
		gwl.setFK_NodeText(nd.getName());
		gwl.setFK_Emp(toEmper);
		gwl.setFK_EmpText(emp.Name);
		gwl.setIsPass(false);
		gwl.setIsEnable(true);
		gwl.setIsRead(false);
		gwl.setWhoExeIt(nd.getWhoExeIt());
		//  gwl.Sender = WebUser.getNo();
		gwl.setHungUpTimes(0);
		gwl.setFID(gwf.getFID());
		gwl.setFK_Dept(emp.FK_Dept);
		gwl.setFK_DeptT(emp.FK_DeptText);

		if (gwl.Update() == 0)
		{
			gwl.Insert();
		}

		String sql = "SELECT COUNT(*) FROM WF_EmpWorks where WorkID=" + workid + " and fk_emp='" + toEmper + "'";
		int i = BP.DA.DBAccess.RunSQLReturnValInt(sql);
		if (i == 0)
		{
			throw new RuntimeException("@调度错误");
		}

		return "该流程(" + gwf.getTitle() + ")，已经调度到(" + nd.getName() + "),分配给(" + emp.Name + ")";
	}
	/** 
	 设置流程运行模式
	 如果是自动流程. 负责人:liuxianchen.
	 调用地方/WorkOpt/TransferCustom.aspx
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param runType 是否自动运行？ 如果自动运行，就按照流程设置的规则运行。
	 非自动运行，就按照用户自己定义的运转顺序计算。
	 @param paras 手工运行的参数格式为: @节点ID1`子流程No`处理模式`接受人1,接受人n`抄送人1NO,抄送人nNO`抄送人1Name,抄送人nName@节点ID2`子流程No`处理模式`接受人1,接受人n`抄送人1NO,抄送人nNO`抄送人1Name,抄送人nName
	*/
	public static void Flow_SetFlowTransferCustom(String flowNo, long workid, TransferCustomType runType, String paras)
	{
		//删除以前存储的参数.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_TransferCustom WHERE WorkID=" + workid);

		//保存参数.
		// 参数格式为  @104`SubFlow002`1`zhangsan,lisi`wangwu,chenba`王五,陈八@......
		String[] strs = paras.split("[@]", -1);
		int idx = 0, cidx = 0;
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}

			if (str.contains("`") == false)
			{
				continue;
			}

			// 处理字符串.
			String[] vals = str.split("[`]", -1);
			int nodeid = Integer.parseInt(vals[0]); // 节点ID.
			String subFlow = vals[1]; // 调用的子流程.
			int todomodel = Integer.parseInt(vals[2]); //处理模式.

			TransferCustom tc = new TransferCustom();
			tc.setIdx(idx); //顺序.
			tc.setFK_Node(nodeid); // 节点.
			tc.setWorkID(workid); //工作ID.
			tc.setWorker(vals[3]); //工作人员.
			tc.setSubFlowNo(subFlow); //子流程.
			tc.setMyPK( tc.getFK_Node() + "_" + tc.getWorkID() + "_" + idx;
			tc.setTodolistModel(TodolistModel.forValue(todomodel)); //处理模式.
			tc.Save();
			idx++;

			//设置抄送
			String[] ccs = vals[4].split("[,]", -1);
			String[] ccNames = vals[5].split("[,]", -1);
			SelectAccper sa = new SelectAccper();
			sa.Delete(SelectAccperAttr.FK_Node, nodeid, SelectAccperAttr.WorkID, workid, SelectAccperAttr.AccType, 1);

			cidx = 0;
			for (int i = 0; i < ccs.length; i++)
			{
				if (DataType.IsNullOrEmpty(ccs[i]) || ccs[i].equals("0"))
				{
					continue;
				}

				sa = new SelectAccper();
				sa.setMyPK( nodeid + "_" + workid + "_" + ccs[i];
				sa.setFK_Emp(ccs[i].trim());
				sa.setEmpName(ccNames[i].trim());
				sa.setFK_Node(nodeid);

				sa.setWorkID(workid);
				sa.setAccType(1);
				sa.setIdx(cidx);
				sa.Insert();
				cidx++;
			}
		}

		// 设置运行模式.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			//gwf.WFSta = WFSta.Runing;
			gwf.setWFState(WFState.Blank);

			gwf.setStarter(WebUser.getNo());
			gwf.setStarterName(WebUser.getName());

			gwf.setFK_Flow(flowNo);
			BP.WF.Flow fl = new Flow(flowNo);
			gwf.setFK_FlowSort(fl.getFK_FlowSort());
			gwf.setSysType(fl.getSysType());
			gwf.setFK_Dept(WebUser.getFK_Dept());

			gwf.setTransferCustomType(runType);
			gwf.Insert();
			return;
		}
		gwf.setTransferCustomType(runType);
		gwf.Update();
	}
	/** 
	 设置流程运行模式
	 启用新的接口原来的接口参数格式太复杂,仍然保留.
	 标准格式:@NodeID=节点ID;Worker=操作人员1,操作人员2,操作人员n,TodolistModel=多人处理模式;SubFlowNo=可发起的子流程编号;SDT=应完成时间;
	 标准简洁格式:@NodeID=节点ID;Worker=操作人员1,操作人员2,操作人员n;@NodeID=节点ID2;Worker=操作人员1,操作人员2,操作人员n;
	 完整格式: @NodeID=101;Worker=zhangsan,lisi;@TodolistModel=1;SubFlowNo=001;SDT=2015-12-12;@NodeID=102;Worker=zhangsan,lisi;@TodolistModel=1;SubFlowNo=001;SDT=2015-12-12;
	 简洁格式: @NodeID=101;Worker=zhangsan,lisi;@NodeID=102;Worker=wagnwu,zhaoliu;
	 
	 @param flowNo
	 @param workid
	 @param runType
	 @param paras 格式为:@节点编号1;处理人员1,处理人员2,处理人员n(可选);应处理时间(可选)
	*/
	public static void Flow_SetFlowTransferCustomV201605(String flowNo, long workid, TransferCustomType runType, String paras)
	{

			///#region 更新状态.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			//  gwf.WFSta = WFSta.Runing;
			gwf.setWFState(WFState.Blank);

			gwf.setStarter(WebUser.getNo());
			gwf.setStarterName(WebUser.getName());

			gwf.setFK_Flow(flowNo);
			BP.WF.Flow fl = new Flow(flowNo);
			gwf.setFK_FlowSort(fl.getFK_FlowSort());
			gwf.setSysType(fl.getSysType());

			gwf.setFK_Dept(WebUser.getFK_Dept());

			gwf.setTransferCustomType(runType);
			gwf.Insert();
			return;
		}
		gwf.setTransferCustomType(runType);
		gwf.Update();
		if (runType == TransferCustomType.ByCCBPMDefine)
		{
			return; // 如果是按照设置的模式运行，就要更改状态后退出它.
		}

			///#endregion

		//删除以前存储的参数.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_TransferCustom WHERE WorkID=" + workid);

		//保存参数.
		// 参数格式为 格式为:@节点编号1;处理人员1,处理人员2,处理人员n;应处理时间(可选)
		// 例如1: @101;zhangsan,lisi,wangwu;2016-05-12;@102;liming,xiaohong,xiaozhang;2016-05-12
		// 例如2: @101;zhangsan,lisi,wangwu;@102;liming,xiaohong,xiaozhang;2016-05-12

		String[] strs = paras.split("[@]", -1);
		int idx = 0, cidx = 0;
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}

			if (str.contains(";") == false)
			{
				continue;
			}

			// 处理字符串.
			String[] vals = str.split("[;]", -1);
			int nodeid = Integer.parseInt(vals[0]); // 节点ID.
			String subFlow = vals[1]; // 调用的子流程.
			int todomodel = Integer.parseInt(vals[2]); //处理模式.

			TransferCustom tc = new TransferCustom();
			tc.setIdx(idx); //顺序.
			tc.setFK_Node(nodeid); // 节点.
			tc.setWorkID(workid); //工作ID.
			tc.setWorker(vals[3]); //工作人员.
			tc.setSubFlowNo(subFlow); //子流程.
			tc.setMyPK( tc.getFK_Node() + "_" + tc.getWorkID() + "_" + idx);
			tc.setTodolistModel(TodolistModel.forValue(todomodel)); //处理模式.
			tc.Save();
			idx++;

			//设置抄送
			String[] ccs = vals[4].split("[,]", -1);
			String[] ccNames = vals[5].split("[,]", -1);
			SelectAccper sa = new SelectAccper();
			sa.Delete(SelectAccperAttr.FK_Node, nodeid, SelectAccperAttr.WorkID, workid, SelectAccperAttr.AccType, 1);

			cidx = 0;
			for (int i = 0; i < ccs.length; i++)
			{
				if (DataType.IsNullOrEmpty(ccs[i]) || ccs[i].equals("0"))
				{
					continue;
				}

				sa = new SelectAccper();
				sa.setMyPK( nodeid + "_" + workid + "_" + ccs[i];
				sa.setFK_Emp(ccs[i].trim());
				sa.setEmpName(ccNames[i].trim());
				sa.setFK_Node(nodeid);
				sa.setWorkID(workid);
				sa.setAccType(1);
				sa.setIdx(cidx);
				sa.Insert();
				cidx++;
			}
		}
	}
	/** 
	 是否可以删除该流程？
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @return 是否可以删除该流程
	*/
	public static boolean Flow_IsCanDeleteFlowInstance(String flowNo, long workid, String userNo)
	{
		if (userNo.equals("admin"))
		{
			return true;
		}

		Flow fl = new Flow(flowNo);
		if (fl.getFlowDeleteRole() == FlowDeleteRole.AdminOnly)
		{
			return false;
		}

		//是否是用户管理员?
		if (fl.getFlowDeleteRole() == FlowDeleteRole.AdminAppOnly)
		{
			if (userNo.indexOf("admin") == 0)
			{
				return true; // 这里判断不严谨,如何判断是否是一个应用管理员使用admin+部门编号来确定的. 比如： admin3701
			}
			else
			{
				return false;
			}
		}

		//是否是发起人.
		if (fl.getFlowDeleteRole() == FlowDeleteRole.ByMyStarter)
		{
			Paras ps = new Paras();
			ps.SQL = "SELECT WorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter";
			ps.Add("WorkID", workid);
			ps.Add("Starter", userNo);
			String user = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (user == null)
			{
				return false;
			}

			return true;
		}

		//按照节点是否启用删除按钮来计算. 
		if (fl.getFlowDeleteRole() == FlowDeleteRole.ByNodeSetting)
		{
			Paras ps = new Paras();
			ps.SQL = "SELECT WorkID FROM WF_GenerWorkerlist A, WF_Node B  WHERE A.FK_Node=B.NodeID  AND B.DelEnable=1  AND A.WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND A.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
			ps.Add("WorkID", workid);
			ps.Add("FK_Emp", userNo);
			String user = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (user == null)
			{
				return false;
			}

			return true;
		}
		return false;
	}

		///#region 与流程有关的接口

	/** 
	 增加一个评论
	 
	 @param flowNo 流程编号
	 @param workid 工作ID
	 @param fid 父工作ID
	 @param msg 消息
	 @param empNo 评论人编号
	 @param empName 评论人名称
	 @return 插入ID主键
	*/
	public static String Flow_BBSAdd(String flowNo, long workid, long fid, String msg, String empNo, String empName)
	{
		return Glo.AddToTrack(ActionType.FlowBBS, flowNo, workid, fid, 0, null, empNo, empName, 0, null, empNo, empName, msg, null);
	}
	/** 
	 删除一个评论.
	 
	 @param flowNo 流程编号
	 @param mypk 主键
	 @return 返回删除信息.AddToTrack
	*/
	public static String Flow_BBSDelete(String flowNo, String mypk, String username)
	{
		Paras pss = new Paras();
		pss.SQL = "SELECT EMPFROM FROM ND" + Integer.parseInt(flowNo) + "Track WHERE MyPK=" + SystemConfig.getAppCenterDBVarStr() + "MyPK ";
		pss.Add("MyPK", mypk);
		String str = BP.DA.DBAccess.RunSQLReturnString(pss);
		if (str.equals(username) || username.equals(str))
		{
			Paras ps = new Paras();
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE MyPK=" + SystemConfig.getAppCenterDBVarStr() + "MyPK ";
			ps.Add("MyPK", mypk);
			BP.DA.DBAccess.RunSQL(ps);
			return "删除成功.";
		}
		else
		{
			return "删除失败,仅能删除自己评论!";
		}
	}

	/** 
	 取消设置关注
	 
	 @param workid 要取消设置的工作ID
	*/
	public static void Flow_Focus(long workid)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		int i = gwf.RetrieveFromDBSources();
		if (i == 0)
		{
			throw new RuntimeException("@ 设置关注错误：没有找到 WorkID= " + workid + " 的实例。");
		}

		String isFocus = gwf.GetParaString("F_" + WebUser.getNo(), "0"); //edit by liuxc,2016-10-22,修复关注/取消关注逻辑错误

		if (isFocus.equals("0"))
		{
			gwf.SetPara("F_" + WebUser.getNo(), "1");
		}
		else
		{
			gwf.SetPara("F_" + WebUser.getNo(), "0");
		}

		gwf.DirectUpdate();
	}

	/** 
	 调整
	 
	 @param workid 要调整的WorkID
	 @param toNodeID 调整到的节点ID
	 @param toEmpIDs 人员集合
	 @param note 调整原因
	 @return 
	*/
	public static String Flow_ReSend(long workid, int toNodeID, String toEmpIDs, String note)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() == WFState.Complete)
		{
			return "err@该流程已经运行完成您不能执行调整,可以执行回滚.";
		}

		Node nd = new Node(toNodeID);

		Emps emps = new Emps();

		String[] strs = toEmpIDs.split("[,]", -1);

		String todoEmps = "";
		int num = 0;
		for (String empID : strs)
		{
			if (DataType.IsNullOrEmpty(empID) == true)
			{
				continue;
			}

			BP.Port.Emp emp = new Emp(empID);
			todoEmps += emp.No + "," + emp.Name + ";";
			num++;

			emps.AddEntity(emp);
		}

		//设置人员.
		gwf.SetValByKey(GenerWorkFlowAttr.TodoEmps, todoEmps);
		gwf.setTodoEmpsNum(num);


		gwf.setHuiQianTaskSta(HuiQianTaskSta.None);
		gwf.setWFState(WFState.Runing);

		//给当前人员产生待办.
		GenerWorkerList gwl = new GenerWorkerList();
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.IsPass, 0);
		if (i == 0)
		{
			return "err@没有找到当前的待办人员.";
		}

		//删除当前节点人员信息.
		gwl.Delete(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

		for (Emp item : emps.ToJavaList())
		{
			//插入一条信息，让调整的人员显示待办.
			gwl.setFK_Emp(item.No);
			gwl.setFK_EmpText(item.Name);
			gwl.setFK_Node(toNodeID);
			gwl.setIsPassInt(0);
			gwl.setIsRead(false);
			gwl.setWhoExeIt(0);
			try
			{
				gwl.Insert();
			}
			catch (java.lang.Exception e)
			{
				gwl.Update();
			}
		}

		//更新当前节点状态.
		gwf.setFK_Node(toNodeID);
		gwf.setNodeName(nd.getName());
		gwf.Update();

		return "调整成功,调整到:" + gwf.getNodeName() + " , 调整给:" + todoEmps;
	}
	/** 
	 取消、确认.
	 
	 @param workid 要取消设置的工作ID
	*/
	public static void Flow_Confirm(long workid)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		int i = gwf.RetrieveFromDBSources();
		if (i == 0)
		{
			throw new RuntimeException("@ 设置关注错误：没有找到 WorkID= " + workid + " 的实例。");
		}

		String isFocus = gwf.GetParaString("C_" + WebUser.getNo(), "0");

		if (isFocus.equals("0"))
		{
			gwf.SetPara("C_" + WebUser.getNo(), "1");
		}
		else
		{
			gwf.SetPara("C_" + WebUser.getNo(), "0");
		}

		gwf.DirectUpdate();
	}
	/** 
	 获得工作进度-用于展示流程的进度图
	 
	 @param workID workID
	 @return 返回进度数据
	*/
	public static DataSet DB_JobSchedule(long workID)
	{
		String sql = "";
		DataSet ds = new DataSet();

		/*
		 * 流程控制主表, 可以得到流程状态，停留节点，当前的执行人.
		 * 该表里有如下字段是重点:
		 *  0. WorkID 流程ID.
		 *  1. WFState 字段用于标识当前流程的状态..
		 *  2. FK_Node 停留节点.
		 *  3. NodeName 停留节点名称.
		 *  4. TodoEmps 停留的待办人员.
		 */
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));


		/*节点信息: 节点信息表,存储每个环节的节点信息数据.
		 * NodeID 节点ID.
		 * Name 名称.
		 * X,Y 节点图形位置，如果使用进度图就不需要了.
		*/
		NodeSimples nds = new NodeSimples(gwf.getFK_Flow());
		ds.Tables.add(nds.ToDataTableField("WF_Node"));

		/*
		 * 节点的连接线. 
		 */
		Directions dirs = new Directions(gwf.getFK_Flow());
		ds.Tables.add(dirs.ToDataTableField("WF_Direction"));


			///#region 运动轨迹
		/*
		 * 运动轨迹： 构造的一个表，用与存储运动轨迹.
		 * 
		 */
		DataTable dtHistory = new DataTable();
		dtHistory.TableName = "Track";
		dtHistory.Columns.Add("FK_Node"); //节点ID.
		dtHistory.Columns.Add("NodeName"); //名称.
		dtHistory.Columns.Add("RunModel"); //节点类型.
		dtHistory.Columns.Add("EmpNo"); //人员编号.
		dtHistory.Columns.Add("EmpName"); //名称
		dtHistory.Columns.Add("DeptName"); //部门名称
		dtHistory.Columns.Add("RDT"); //记录日期.
		dtHistory.Columns.Add("SDT"); //应完成日期(可以不用.)
		dtHistory.Columns.Add("IsPass"); //是否通过?

		//执行人.

		//历史执行人. 
		sql = "SELECT C.Name AS DeptName,  A.* FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track A, Port_Emp B, Port_Dept C  ";
		sql += " WHERE (A.WorkID=" + workID + " OR A.FID=" + workID + ") AND (A.ActionType=1 OR A.ActionType=0  OR A.ActionType=6  OR A.ActionType=7) AND (A.EmpFrom=B.No) AND (B.FK_Dept=C.No) ";
		sql += " ORDER BY A.RDT ";

		DataTable dtTrack = BP.DA.DBAccess.RunSQLReturnTable(sql);

		for (DataRow drTrack : dtTrack.Rows)
		{
			DataRow dr = dtHistory.NewRow();
			dr.set("FK_Node", drTrack.get("NDFrom"));
			//dr["ActionType"] = drTrack["NDFrom"];
			dr.set("NodeName", drTrack.get("NDFromT"));
			dr.set("EmpNo", drTrack.get("EmpFrom"));
			dr.set("EmpName", drTrack.get("EmpFromT"));
			dr.set("DeptName", drTrack.get("DeptName")); //部门名称.
			dr.set("RDT", drTrack.get("RDT"));
			dr.set("SDT", "");
			dr.set("IsPass", 1); // gwl.IsPassInt; //是否通过.
			dtHistory.Rows.add(dr);
		}

		//如果流程没有完成.
		if (gwf.getWFState() != WFState.Complete && 1 == 2)
		{
			DataRow dr = dtHistory.NewRow();
			dr.set("FK_Node", gwf.getFK_Node());
			//dr["ActionType"] = drTrack["NDFrom"];
			dr.set("NodeName", gwf.getNodeName());
			dr.set("EmpNo", WebUser.getNo());
			dr.set("EmpName", WebUser.getName());
			dr.set("DeptName", WebUser.getFK_DeptName); //部门名称.
			dr.set("RDT", DataType.CurrentData);
			dr.set("SDT", "");
			dr.set("IsPass", 0); // gwl.IsPassInt; //是否通过.
			dtHistory.Rows.add(dr);
		}

		if (dtHistory.Rows.size() == 0)
		{
			DataRow dr = dtHistory.NewRow();
			dr.set("FK_Node", gwf.getFK_Node());
			dr.set("NodeName", gwf.getNodeName());
			dr.set("EmpNo", gwf.getStarter());
			dr.set("EmpName", gwf.getStarterName());
			dr.set("RDT", gwf.getRDT());
			dr.set("SDT", gwf.getSDTOfNode());
			dtHistory.Rows.add(dr);
		}

		// 给 dtHistory runModel 赋值.
		for (NodeSimple nd : nds.ToJavaList())
		{
			int runMode = nd.GetValIntByKey(NodeAttr.RunModel);
			for (DataRow dr : dtHistory.Rows)
			{
				if (Integer.parseInt(dr.get("FK_Node").toString()) == nd.getNodeID())
				{
					dr.set("RunModel", runMode);
				}
			}
		}
		ds.Tables.add(dtHistory);

			///#endregion 运动轨迹


			///#region 游离态
		TransferCustoms tranfs = new TransferCustoms(workID);
		ds.Tables.add(tranfs.ToDataTableField("WF_TransferCustom"));

			///#endregion 游离态

		return ds;
	}
	/** 
	 设置委托
	 
	 @param Author 接收委托人账号
	 @param AuthorWay 委托方式：0不授权， 1完全授权，2，指定流程范围授权. 
	 @param AuthorFlows 委托流程编号，格式：001,002,003
	 @param AuthorDate 委托开始时间，默认当前时间
	 @param AuthorToDate 委托结束时间
	 @return 设置结果：成功true,失败 false
	*/

	public static boolean Flow_AuthorSave(String Author, int AuthorWay, String AuthorFlows, String AuthorDate)
	{
		return Flow_AuthorSave(Author, AuthorWay, AuthorFlows, AuthorDate, null);
	}

	public static boolean Flow_AuthorSave(String Author, int AuthorWay, String AuthorFlows)
	{
		return Flow_AuthorSave(Author, AuthorWay, AuthorFlows, null, null);
	}

	public static boolean Flow_AuthorSave(String Author, int AuthorWay)
	{
		return Flow_AuthorSave(Author, AuthorWay, null, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static bool Flow_AuthorSave(string Author, int AuthorWay, string AuthorFlows = null, string AuthorDate = null, string AuthorToDate = null)
	public static boolean Flow_AuthorSave(String Author, int AuthorWay, String AuthorFlows, String AuthorDate, String AuthorToDate)
	{
		if (WebUser.getNo() == null)
		{
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}

		WFEmp emp = new WFEmp(WebUser.getNo());
		emp.setAuthor(Author);
		emp.setAuthorWay(AuthorWay);
		emp.setAuthorDate(BP.DA.DataType.CurrentData);

		if (!DataType.IsNullOrEmpty(AuthorFlows))
		{
			emp.setAuthorFlows(AuthorFlows);
		}

		if (!DataType.IsNullOrEmpty(AuthorDate))
		{
			emp.setAuthorFlows(AuthorDate);
		}

		if (!DataType.IsNullOrEmpty(AuthorToDate))
		{
			emp.setAuthorToDate(AuthorToDate);
		}

		int i = emp.Save();

		return i >= 0 ? true : false;
	}
	/** 
	 取消委托当前登录人的委托信息
	 
	 @return 
	*/
	public static boolean Flow_AuthorCancel()
	{
		if (WebUser.getNo() == null)
		{
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}

		WFEmp myau = new WFEmp(WebUser.getNo());
		BP.DA.Log.DefaultLogWriteLineInfo("取消授权:" + WebUser.getNo() + "取消了对(" + myau.getAuthor() + ")的授权。");
		myau.setAuthor("");
		myau.setAuthorWay(0);
		myau.setAuthorDate("");
		myau.setAuthorToDate("");
		int i = myau.Update();
		return i >= 0 ? true : false;
	}
	/** 
	 获取当前登录人的委托人
	 
	 @return 
	*/
	public static DataTable DB_AuthorEmps()
	{
		if (WebUser.getNo() == null)
		{
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}

		return BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + WebUser.getNo() + "'");
	}
	/** 
	 获取委托给当前登录人的流程待办信息
	 
	 @param empNo 授权人员编号
	 @return 
	*/
	public static DataTable DB_AuthorEmpWorks(String empNo)
	{
		if (WebUser.getNo() == null)
		{
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}

		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(empNo);
		if (!DataType.IsNullOrEmpty(emp.getAuthor()) && emp.getAuthor().equals(WebUser.getNo()) && emp.getAuthorIsOK() == true)
		{
			String sql = "";
			String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue() + "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
			switch (emp.getHisAuthorWay())
			{
				case All:
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' AND TaskSta!=1  ORDER BY ADT DESC";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' ORDER BY ADT DESC";
					}

					break;
				case SpecFlows:
					if (BP.WF.Glo.getIsEnableTaskPool() == true)
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0    ORDER BY ADT DESC";
					}
					else
					{
						sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.No + "' AND  FK_Flow IN " + emp.getAuthorFlows() + "   ORDER BY ADT DESC";
					}

					break;
			}
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
		return null;
	}

		///#endregion 与流程有关的接口



		///#endregion 与流程有关的接口


		///#region get 属性节口
	/** 
	 获得流程运行过程中的参数
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	 @return 如果没有就返回null,有就返回@参数名0=参数值0@参数名1=参数值1
	*/
	public static String GetFlowParas(int nodeID, long workid)
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT Paras FROM WF_GenerWorkerlist WHERE FK_Node=" + dbstr + "FK_Node AND WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkerListAttr.FK_Node, nodeID);
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		return DBAccess.RunSQLReturnStringIsNull(ps, null);
	}

		///#endregion get 属性节口


		///#region 工作有关接口
	/** 
	 发起流程
	 
	 @param flowNo 流程编号
	 @param ht 节点表单:主表数据以Key Value 方式传递(可以为空)
	 @param workDtls 节点表单:从表数据，从表名称与从表单的从表编号要对应(可以为空)
	 @param nextNodeID 发起后要跳转到的节点(可以为空)
	 @param nextWorker 发起后要跳转到的节点并指定的工作人员(可以为空)
	 @return 发送到第二个节点的执行信息
	*/
	public static SendReturnObjs Node_StartWork(String flowNo, Hashtable ht, DataSet workDtls, int nextNodeID, String nextWorker)
	{
		return Node_StartWork(flowNo, ht, workDtls, nextNodeID, nextWorker, 0, null);
	}
	/** 
	 发起流程
	 
	 @param flowNo 流程编号
	 @param htWork 节点表单:主表数据以Key Value 方式传递(可以为空)
	 @param workDtls 节点表单:从表数据，从表名称与从表单的从表编号要对应(可以为空)
	 @param nextNodeID 发起后要跳转到的节点(可以为空)
	 @param nextWorker 发起后要跳转到的节点并指定的工作人员(可以为空)
	 @param parentWorkID 父流程的workid，如果没有可以为0
	 @param parentFlowNo 父流程的编号，如果没有可以为空
	 @return 发送到第二个节点的执行信息
	*/
	public static SendReturnObjs Node_StartWork(String flowNo, Hashtable htWork, DataSet workDtls, int nextNodeID, String nextWorker, long parentWorkID, String parentFlowNo)
	{
		// 给全局变量赋值.
		BP.WF.Glo.setSendHTOfTemp(htWork);

		Flow fl = new Flow(flowNo);
		Work wk = fl.NewWork();
		long workID = wk.getOID();
		if (htWork != null)
		{
			for (String str : htWork.keySet())
			{
				switch (str)
				{
					case StartWorkAttr.OID:
					case StartWorkAttr.CDT:
					case StartWorkAttr.MD5:
					case StartWorkAttr.Emps:
					case StartWorkAttr.FID:
					case StartWorkAttr.FK_Dept:
					case StartWorkAttr.PRI:
					case StartWorkAttr.Rec:
					case StartWorkAttr.Title:
						continue;
					default:
						break;
				}
				wk.SetValByKey(str, htWork.get(str));
			}
		}

		wk.setOID(workID);
		if (workDtls != null)
		{
			//保存从表
			for (DataTable dt : workDtls.Tables)
			{
				for (MapDtl dtl : wk.getHisMapDtls())
				{
					if (!dt.TableName.equals(dtl.No))
					{
						continue;
					}

					//获取dtls
					GEDtls daDtls = new GEDtls(dtl.No);
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = daDtls.GetNewEntity instanceof GEDtl ? (GEDtl)daDtls.GetNewEntity : null;
					daDtl.RefPK = String.valueOf(wk.getOID());

					// 为从表复制数据.
					for (DataRow dr : dt.Rows)
					{
						daDtl.ResetDefaultVal();

						//明细列.
						for (DataColumn dc : dt.Columns)
						{
							//设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName));
						}

						daDtl.RefPK = String.valueOf(wk.getOID());
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
					}
				}
			}
		}

		WorkNode wn = new WorkNode(wk, fl.getHisStartNode());

		Node nextNoode = null;
		if (nextNodeID != 0)
		{
			nextNoode = new Node(nextNodeID);
		}

		SendReturnObjs objs = wn.NodeSend(nextNoode, nextWorker);
		if (parentWorkID != 0)
		{
			DBAccess.RunSQL("UPDATE WF_GenerWorkFlow SET PWorkID=" + parentWorkID + ",PFlowNo='" + parentFlowNo + "' WHERE WorkID=" + objs.getVarWorkID());
		}


			///#region 更新发送参数.
		if (htWork != null)
		{
			String paras = "";
			for (String key : htWork.keySet())
			{
				paras += "@" + key + "=" + htWork.get(key).toString();
			}

			if (DataType.IsNullOrEmpty(paras) == false && Glo.getIsEnableTrackRec() == true)
			{
				String dbstr = SystemConfig.getAppCenterDBVarStr();
				Paras ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node";
				ps.Add(GenerWorkerListAttr.Paras, paras);
				ps.Add(GenerWorkerListAttr.WorkID, workID);
				ps.Add(GenerWorkerListAttr.FK_Node, Integer.parseInt(flowNo + "01"));
				try
				{
					DBAccess.RunSQL(ps);
				}
				catch (java.lang.Exception e)
				{
					GenerWorkerList gwl = new GenerWorkerList();
					gwl.CheckPhysicsTable();
					DBAccess.RunSQL(ps);
				}
			}
		}

			///#endregion 更新发送参数.

		return objs;
	}

	public static void CopyDataFromParentFlow(String pFlowNo, long pFID, long pWorkID, Work currEnt)
	{
		///#region copy 首先从父流程的NDxxxRpt copy.
		//Int64 pWorkIDReal = 0;
		//Flow pFlow = new Flow(pFlowNo);
		//string pOID = "";
		//if (DataType.IsNullOrEmpty(PFIDStr) == true || PFIDStr == "0")
		//    pOID = PWorkID.ToString();
		//else
		//    pOID = PFIDStr;

		//string sql = "SELECT * FROM " + pFlow.PTable + " WHERE OID=" + pOID;
		//DataTable dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@不应该查询不到父流程的数据[" + sql + "], 可能的情况之一,请确认该父流程的调用节点是子线程，但是没有把子线程的FID参数传递进来。");

		//wk.Copy(dt.Rows[0]);
		//rpt.Copy(dt.Rows[0]);

		////设置单号为空.
		//wk.SetValByKey("BillNo", "");
		//rpt.BillNo = "";
		///#endregion copy 首先从父流程的NDxxxRpt copy.

		///#region 从调用的节点上copy.
		//BP.WF.Node fromNd = new BP.WF.Node(int.Parse(PNodeIDStr));
		//Work wkFrom = fromNd.HisWork;
		//wkFrom.OID = PWorkID;
		//if (wkFrom.RetrieveFromDBSources() == 0)
		//    throw new Exception("@父流程的工作ID不正确，没有查询到数据" + PWorkID);
		////wk.Copy(wkFrom);
		////rpt.Copy(wkFrom);
		///#endregion 从调用的节点上copy.

		///#region 获取web变量.
		//foreach (string k in paras.Keys)
		//{
		//    if (k == "OID")
		//        continue;

		//    wk.SetValByKey(k, paras[k]);
		//    rpt.SetValByKey(k, paras[k]);
		//}
		///#endregion 获取web变量.

		///#region 特殊赋值.
		//wk.OID = newOID;
		//rpt.OID = newOID;

		//// 在执行copy后，有可能这两个字段会被冲掉。
		//if (CopyFormWorkID != null)
		//{
		//    /*如果不是 执行的从已经完成的流程copy.*/

		//    wk.SetValByKey(StartWorkAttr.PFlowNo, PFlowNo);
		//    wk.SetValByKey(StartWorkAttr.PNodeID, PNodeID);
		//    wk.SetValByKey(StartWorkAttr.PWorkID, PWorkID);

		//    rpt.SetValByKey(GERptAttr.PFlowNo, PFlowNo);
		//    rpt.SetValByKey(GERptAttr.PNodeID, PNodeID);
		//    rpt.SetValByKey(GERptAttr.PWorkID, PWorkID);

		//    //忘记了增加这句话.
		//    rpt.SetValByKey(GERptAttr.PEmp, WebUser.getNo());

		//    //要处理单据编号 BillNo .
		//    if (this.BillNoFormat != "")
		//    {
		//        rpt.SetValByKey(GERptAttr.BillNo, BP.WF.WorkFlowBuessRole.GenerBillNo(this.BillNoFormat, rpt.OID, rpt, this.PTable));

		//        //设置单据编号.
		//        wk.SetValByKey(GERptAttr.BillNo, rpt.BillNo);
		//    }

		//    rpt.SetValByKey(GERptAttr.FID, 0);
		//    rpt.SetValByKey(GERptAttr.FlowStartRDT, BP.DA.DataType.getCurrentDataTime());
		//    rpt.SetValByKey(GERptAttr.FlowEnderRDT, BP.DA.DataType.getCurrentDataTime());
		//    rpt.SetValByKey(GERptAttr.MyNum, 0);
		//    rpt.SetValByKey(GERptAttr.WFState, (int)WFState.Blank);
		//    rpt.SetValByKey(GERptAttr.FlowStarter, emp.No);
		//    rpt.SetValByKey(GERptAttr.FlowEnder, emp.No);
		//    rpt.SetValByKey(GERptAttr.FlowEndNode, this.StartNodeID);
		//    rpt.SetValByKey(GERptAttr.FK_Dept, emp.FK_Dept);
		//    rpt.SetValByKey(GERptAttr.FK_NY, DataType.CurrentYearMonth);

		//    if (Glo.UserInfoShowModel == UserInfoShowModel.UserNameOnly)
		//        rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.Name);

		//    if (Glo.UserInfoShowModel == UserInfoShowModel.UserIDUserName)
		//        rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.No);

		//    if (Glo.UserInfoShowModel == UserInfoShowModel.UserIDUserName)
		//        rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.No + "," + emp.Name);

		//}

		//if (rpt.EnMap.PhysicsTable != wk.EnMap.PhysicsTable)
		//    wk.Update(); //更新工作节点数据.
		//rpt.Update(); // 更新流程数据表.
		///#endregion 特殊赋值.

		///#region 复制其他数据..
		////复制明细。
		//MapDtls dtls = wk.HisMapDtls;
		//if (dtls.size() > 0)
		//{
		//    MapDtls dtlsFrom = wkFrom.HisMapDtls;
		//    int idx = 0;
		//    if (dtlsFrom.size() == dtls.size())
		//    {
		//        foreach (MapDtl dtl in dtls)
		//        {
		//            if (dtl.IsCopyNDData == false)
		//                continue;

		//            //new 一个实例.
		//            GEDtl dtlData = new GEDtl(dtl.No);

		//            //检查该明细表是否有数据，如果没有数据，就copy过来，如果有，就说明已经copy过了。
		//            //  sql = "SELECT COUNT(OID) FROM "+dtlData.EnMap.PhysicsTable+" WHERE RefPK="+wk.OID;

		//            //删除以前的数据.
		//            sql = "DELETE FROM " + dtlData.EnMap.PhysicsTable + " WHERE RefPK=" + wk.OID;
		//            DBAccess.RunSQL(sql);


		//            MapDtl dtlFrom = dtlsFrom[idx] as MapDtl;

		//            GEDtls dtlsFromData = new GEDtls(dtlFrom.No);
		//            dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
		//            foreach (GEDtl geDtlFromData in dtlsFromData)
		//            {
		//                dtlData.Copy(geDtlFromData);
		//                dtlData.RefPK = wk.OID.ToString();
		//                if (this.No == PFlowNo)
		//                {
		//                    dtlData.InsertAsNew();
		//                }
		//                else
		//                {
		//                    if (this.StartLimitRole == StartLimitRole.OnlyOneSubFlow)
		//                        dtlData.SaveAsOID(geDtlFromData.OID); //为子流程的时候，仅仅允许被调用1次.
		//                    else
		//                        dtlData.InsertAsNew();
		//                }
		//            }
		//        }
		//    }
		//}

		////复制附件数据。
		//if (wk.HisFrmAttachments.size() > 0)
		//{
		//    if (wkFrom.HisFrmAttachments.size() > 0)
		//    {
		//        int toNodeID = wk.NodeID;

		//        //删除数据。
		//        DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + toNodeID + "' AND RefPKVal='" + wk.OID + "'");
		//        FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + PNodeIDStr, PWorkID.ToString());

		//        foreach (FrmAttachmentDB athDB in athDBs)
		//        {
		//            FrmAttachmentDB athDB_N = new FrmAttachmentDB();
		//            athDB_N.Copy(athDB);
		//            athDB_N.FK_MapData = "ND" + toNodeID;
		//            athDB_N.RefPKVal = wk.OID.ToString();
		//            athDB_N.FK_FrmAttachment = athDB_N.FK_FrmAttachment.Replace("ND" + PNodeIDStr,
		//              "ND" + toNodeID);

		//            if (athDB_N.HisAttachmentUploadType == AttachmentUploadType.Single)
		//            {
		//                /*如果是单附件.*/
		//                athDB_N.setMyPK( athDB_N.FK_FrmAttachment + "_" + wk.OID;
		//                if (athDB_N.IsExits == true)
		//                    continue; /*说明上一个节点或者子线程已经copy过了, 但是还有子线程向合流点传递数据的可能，所以不能用break.*/
		//                athDB_N.Insert();
		//            }
		//            else
		//            {
		//                athDB_N.setMyPK( athDB_N.UploadGUID + "_" + athDB_N.FK_MapData + "_" + wk.OID;
		//                athDB_N.Insert();
		//            }
		//        }
		//    }
		//}
		///#endregion 复制表单其他数据.

		///#region 复制独立表单数据.
		////求出来被copy的节点有多少个独立表单.
		//FrmNodes fnsFrom = new Template.FrmNodes(fromNd.NodeID);
		//if (fnsFrom.size() != 0)
		//{
		//    //求当前节点表单的绑定的表单.
		//    FrmNodes fns = new Template.FrmNodes(nd.NodeID);
		//    if (fns.size() != 0)
		//    {
		//        //开始遍历当前绑定的表单.
		//        foreach (FrmNode fn in fns)
		//        {
		//            foreach (FrmNode fnFrom in fnsFrom)
		//            {
		//                if (fn.FK_Frm != fnFrom.FK_Frm)
		//                    continue;

		//                BP.Sys.GEEntity geEnFrom = new GEEntity(fnFrom.FK_Frm);
		//                geEnFrom.OID = PWorkID;
		//                if (geEnFrom.RetrieveFromDBSources() == 0)
		//                    continue;

		//                //执行数据copy , 复制到本身. 
		//                geEnFrom.CopyToOID(wk.OID);
		//            }
		//        }
		//    }
		//}
		///#endregion 复制独立表单数据.
	}

	/** 
	 创建一个空白的WorkID
	 
	 @param flowNo 流程编号
	 @param userNo 用户编号
	 @return 执行结果
	*/
	public static long Node_CreateBlankWork(String flowNo, String userNo)
	{
		return Node_CreateBlankWork(flowNo, null, null, userNo);
	}
	/** 
	 创建WorkID
	 
	 @param flowNo 流程编号
	 @param ht 表单参数，可以为null。
	 @param workDtls 明细表参数，可以为null。
	 @param starter 流程的发起人
	 @param title 创建工作时的标题，如果为null，就按设置的规则生成。
	 @param parentWorkID 父流程的WorkID,如果没有父流程就传入为0.
	 @param parentFID 父流程的FID,如果没有父流程就传入为0.
	 @param parentFlowNo 父流程的流程编号,如果没有父流程就传入为null.
	 @param jumpToNode 要跳转到的节点,如果没有则为0.
	 @param jumpToEmp 要跳转到的人员,如果没有则为null.
	 @param todoEmps 待办人员,如果没有则为null.
	 @return 为开始节点创建工作后产生的WorkID.
	*/

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp, int jumpToNode, String jumpToEmp, String todoEmps)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, parentNodeID, parentEmp, jumpToNode, jumpToEmp, todoEmps, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp, int jumpToNode, String jumpToEmp)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, parentNodeID, parentEmp, jumpToNode, jumpToEmp, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp, int jumpToNode)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, parentNodeID, parentEmp, jumpToNode, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, parentNodeID, parentEmp, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, parentNodeID, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, null, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, 0, null, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter, String title)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, 0, 0, null, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, null, 0, 0, null, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls)
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, null, null, 0, 0, null, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht)
	{
		return Node_CreateBlankWork(flowNo, ht, null, null, null, 0, 0, null, 0, null, 0, null, null, null);
	}

	public static long Node_CreateBlankWork(String flowNo)
	{
		return Node_CreateBlankWork(flowNo, null, null, null, null, 0, 0, null, 0, null, 0, null, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static Int64 Node_CreateBlankWork(string flowNo, Hashtable ht = null, DataSet workDtls = null, string starter = null, string title = null, Int64 parentWorkID = 0, Int64 parentFID = 0, string parentFlowNo = null, int parentNodeID = 0, string parentEmp = null, int jumpToNode = 0, string jumpToEmp = null, string todoEmps = null, string isStartSameLevelFlow = null)
	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp, int jumpToNode, String jumpToEmp, String todoEmps, String isStartSameLevelFlow)
	{

		//把一些其他的参数也增加里面去,传递给ccflow.
		Hashtable htPara = new Hashtable();

		if (parentWorkID != 0)
		{
			htPara.put(StartFlowParaNameList.PWorkID, parentWorkID);
			htPara.put(StartFlowParaNameList.PFID, parentFID);
			htPara.put(StartFlowParaNameList.PFlowNo, parentFlowNo);
			htPara.put(StartFlowParaNameList.PNodeID, parentNodeID);
			htPara.put(StartFlowParaNameList.PEmp, parentEmp);
		}

		// 给全局变量赋值.
		BP.WF.Glo.setSendHTOfTemp(ht);

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		if (DataType.IsNullOrEmpty(starter))
		{
			starter = WebUser.getNo();
		}

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());

		// 下一个工作人员。
		Emp empStarter = new Emp(starter);
		Work wk = fl.NewWork(empStarter, htPara);
		long workID = wk.getOID();


			///#region 给各个属性-赋值
		if (ht != null)
		{
			for (String str : ht.keySet())
			{
				switch (str)
				{
					case StartWorkAttr.OID:
					case StartWorkAttr.CDT:
					case StartWorkAttr.MD5:
					case StartWorkAttr.Emps:
					case StartWorkAttr.FID:
					case StartWorkAttr.FK_Dept:
					case StartWorkAttr.PRI:
					case StartWorkAttr.Rec:
					case StartWorkAttr.Title:
						continue;
					default:
						break;
				}
				wk.SetValByKey(str, ht.get(str));
			}
		}
		wk.setOID(workID);
		if (workDtls != null)
		{
			//保存从表
			for (DataTable dt : workDtls.Tables)
			{
				for (MapDtl dtl : wk.getHisMapDtls())
				{
					if (!dt.TableName.equals(dtl.No))
					{
						continue;
					}

					//获取dtls
					GEDtls daDtls = new GEDtls(dtl.No);
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = daDtls.GetNewEntity instanceof GEDtl ? (GEDtl)daDtls.GetNewEntity : null;
					daDtl.RefPK = String.valueOf(wk.getOID());

					// 为从表复制数据.
					for (DataRow dr : dt.Rows)
					{
						daDtl.ResetDefaultVal();
						daDtl.RefPK = String.valueOf(wk.getOID());

						//明细列.
						for (DataColumn dc : dt.Columns)
						{
							//设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
					}
				}
			}
		}

			///#endregion 赋值

		Paras ps = new Paras();

		if (DataType.IsNullOrEmpty(title) == true)
		{
			title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);
		}

		//执行对报表的数据表WFState状态的更新,让它为runing的状态.
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=0,FK_Dept=" + dbstr + "FK_Dept,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
		ps.Add(GERptAttr.FK_Dept, empStarter.FK_Dept);
		ps.Add(GERptAttr.Title, title);
		ps.Add(GERptAttr.OID, wk.getOID());
		DBAccess.RunSQL(ps);

		// 设置父流程信息.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(wk.getOID());
		int i = gwf.RetrieveFromDBSources();

		//将流程信息提前写入wf_GenerWorkFlow,避免查询不到
		gwf.setFlowName(fl.Name);
		gwf.setFK_Flow(flowNo);
		gwf.setFK_FlowSort(fl.getFK_FlowSort());
		gwf.setSysType(fl.getSysType());

		gwf.setFK_Dept(empStarter.FK_Dept);
		gwf.setDeptName(empStarter.FK_DeptText);
		gwf.setFK_Node(fl.getStartNodeID());
		gwf.setNodeName(nd.getName());
		gwf.setWFState(WFState.Blank);
		gwf.setTitle(title);

		gwf.setStarter(empStarter.No);
		gwf.setStarterName(empStarter.Name);
		gwf.setRDT(DataType.getCurrentDataTime());
		gwf.setPWorkID(parentWorkID);
		gwf.setPFID(parentFID);
		gwf.setPFlowNo(parentFlowNo);
		gwf.setPNodeID(parentNodeID);
		if (i == 0)
		{
			gwf.Insert();
		}
		else
		{
			gwf.Update();
		}

		if (parentWorkID != 0)
		{
			BP.WF.Dev2Interface.SetParentInfo(flowNo, wk.getOID(), parentWorkID); //设置父流程信息
		}


///#warning 增加是防止手动启动子流程或者平级子流程时关闭子流程页面找不到待办 保存到待办
		if (isStartSameLevelFlow != null)
		{
			BP.WF.Dev2Interface.Node_SaveWork(flowNo, Integer.parseInt(flowNo + "01"), wk.getOID());
		}
		// 如果有跳转.
		if (jumpToNode != 0)
		{
			BP.WF.Dev2Interface.Node_SendWork(flowNo, wk.getOID(), null, null, jumpToNode, jumpToEmp);
		}
		return wk.getOID();
	}
	/** 
	 增加待办人员
	 
	 @param workid 工作ID
	 @param todoEmps 要增加的处理人员,多个人员用逗号分开.
	*/
	public static void Node_AddTodolist(long workid, String todoEmps)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() == WFState.Complete)
		{
			throw new RuntimeException("流程：" + gwf.getTitle() + "已经完成,您不能增加接受人.");
		}


			///#region 增加待办人员.

		GenerWorkerList gwl = new GenerWorkerList();
		gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

		String[] emps = todoEmps.split("[,]", -1); //分开字符串.
		String tempStrs = ""; //临时变量，防止重复插入.
		for (String emp : emps.ToJavaList())
		{
			if (DataType.IsNullOrEmpty(emp) == true)
			{
				continue;
			}
			if (tempStrs.contains("," + emp + ",") == true)
			{
				continue;
			}

			//插入待办.
			gwl = new GenerWorkerList();
			gwl.setWorkID(workid);
			gwl.setFK_Node(gwf.getFK_Node());
			gwl.setFK_Emp(emp);
			int i = gwl.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			Emp empEn = new Emp(emp);

			gwl.setFK_EmpText(empEn.Name);
			gwl.setFK_NodeText(gwf.getNodeName());
			gwl.setFID(0);
			gwl.setFK_Flow(gwf.getFK_Flow());
			gwl.setFK_Dept(empEn.FK_Dept);
			gwl.setFK_DeptT(empEn.FK_DeptText);

			gwl.setSDT("无");
			gwl.setDTOfWarning(DataType.getCurrentDataTime());
			gwl.setIsEnable(true);
			gwl.setIsPass(false);
			gwl.setPRI(gwf.getPRI());
			gwl.Insert();

			tempStrs += "," + emp + ",";
		}

			///#endregion 增加待办人员.

		if (gwf.getWFState() == WFState.Blank)
		{
			gwf.setWFState(WFState.Runing);
			gwf.Update();
		}
	}
	/** 
	 创建开始节点工作
	 创建后可以创办人形成一个待办.
	 
	 @param flowNo 流程编号
	 @param htWork 表单参数，可以为null。
	 @param workDtls 明细表参数，可以为null。
	 @param flowStarter 流程的发起人，如果为null就是当前人员。
	 @param title 创建工作时的标题，如果为null，就按设置的规则生成。
	 @param parentWorkID 父流程的WorkID,如果没有父流程就传入为0.
	 @param parentFlowNo 父流程的流程编号,如果没有父流程就传入为null.
	 @return 为开始节点创建工作后产生的WorkID.
	*/

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls, String flowStarter, String title, long parentWorkID, String parentFlowNo)
	{
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, title, parentWorkID, parentFlowNo, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls, String flowStarter, String title, long parentWorkID)
	{
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, title, parentWorkID, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls, String flowStarter, String title)
	{
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, title, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls, String flowStarter)
	{
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls)
	{
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, null, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork)
	{
		return Node_CreateStartNodeWork(flowNo, htWork, null, null, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo)
	{
		return Node_CreateStartNodeWork(flowNo, null, null, null, null, 0, null, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static Int64 Node_CreateStartNodeWork(string flowNo, Hashtable htWork = null, DataSet workDtls = null, string flowStarter = null, string title = null, Int64 parentWorkID = 0, string parentFlowNo = null, int parentNDFrom = 0)
	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork, DataSet workDtls, String flowStarter, String title, long parentWorkID, String parentFlowNo, int parentNDFrom)
	{
		// 给全局变量赋值.
		BP.WF.Glo.setSendHTOfTemp(htWork);

		if (DataType.IsNullOrEmpty(flowStarter))
		{
			flowStarter = WebUser.getNo();
		}

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());

		// 下一个工作人员。
		Emp emp = new Emp(flowStarter);
		Work wk = fl.NewWork(flowStarter);


			///#region 给各个属性-赋值
		if (htWork != null)
		{
			for (String str : htWork.keySet())
			{
				switch (str)
				{
					case StartWorkAttr.OID:
					case StartWorkAttr.CDT:
					case StartWorkAttr.MD5:
					case StartWorkAttr.Emps:
					case StartWorkAttr.FID:
					case StartWorkAttr.FK_Dept:
					case StartWorkAttr.PRI:
					case StartWorkAttr.Rec:
					case StartWorkAttr.Title:
						continue;
					default:
						break;
				}
				wk.SetValByKey(str, htWork.get(str));
			}
			//将参数保存到业务表
			wk.DirectUpdate();
		}

		if (workDtls != null)
		{
			//保存从表
			for (DataTable dt : workDtls.Tables)
			{
				for (MapDtl dtl : wk.getHisMapDtls())
				{
					if (!dt.TableName.equals(dtl.No))
					{
						continue;
					}
					//获取dtls
					GEDtls daDtls = new GEDtls(dtl.No);
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = daDtls.GetNewEntity instanceof GEDtl ? (GEDtl)daDtls.GetNewEntity : null;
					daDtl.RefPK = String.valueOf(wk.getOID());

					// 为从表复制数据.
					for (DataRow dr : dt.Rows)
					{
						daDtl.ResetDefaultVal();
						daDtl.RefPK = String.valueOf(wk.getOID());

						//明细列.
						for (DataColumn dc : dt.Columns)
						{
							//设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
					}
				}
			}
		}

			///#endregion 赋值


			///#region 为开始工作创建待办
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(wk.getOID());
		int i = gwf.RetrieveFromDBSources();

		gwf.setFlowName(fl.Name);
		gwf.setFK_Flow(flowNo);
		gwf.setFK_FlowSort(fl.getFK_FlowSort());
		gwf.setSysType(fl.getSysType());

		gwf.setFK_Dept(emp.FK_Dept);
		gwf.setDeptName(emp.FK_DeptText);
		gwf.setFK_Node(fl.getStartNodeID());

		gwf.setNodeName(nd.getName());

		//默认是空白流程
		//gwf.WFSta = WFSta.Etc;
		gwf.setWFState(WFState.Blank);
		//保存到草稿
		if (fl.getDraftRole() == DraftRole.SaveToDraftList)
		{
			gwf.setWFState(WFState.Draft);
		}
		else if (fl.getDraftRole() == DraftRole.SaveToTodolist)
		{
			//保存到待办
			//  gwf.WFSta = WFSta.Runing;
			gwf.setWFState(WFState.Runing);
		}

		if (DataType.IsNullOrEmpty(title))
		{
			gwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
		}
		else
		{
			gwf.setTitle(title);
		}

		gwf.setStarter(emp.No);
		gwf.setStarterName(emp.Name);
		gwf.setRDT(DataType.getCurrentDataTime());

		if (htWork != null && htWork.containsKey("PRI") == true)
		{
			gwf.setPRI(Integer.parseInt(htWork.get("PRI").toString()));
		}

		if (htWork != null && htWork.containsKey("SDTOfNode") == true)
		{
			/*节点应完成时间*/
			gwf.setSDTOfNode(htWork.get("SDTOfNode").toString());
		}

		if (htWork != null && htWork.containsKey("SDTOfFlow") == true)
		{
			/*流程应完成时间*/
			gwf.setSDTOfNode(htWork.get("SDTOfFlow").toString());
		}

		gwf.setPWorkID(parentWorkID);
		gwf.setPFlowNo(parentFlowNo);
		gwf.setPNodeID(parentNDFrom);
		if (i == 0)
		{
			gwf.Insert();
		}
		else
		{
			gwf.Update();
		}

		// 产生工作列表.
		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setWorkID(wk.getOID());
		if (gwl.RetrieveFromDBSources() == 0)
		{
			gwl.setFK_Emp(emp.No);
			gwl.setFK_EmpText(emp.Name);

			gwl.setFK_Node(nd.getNodeID());
			gwl.setFK_NodeText(nd.getName());
			gwl.setFID(0);

			gwl.setFK_Flow(fl.No);
			gwl.setFK_Dept(emp.FK_Dept);
			gwl.setFK_DeptT(emp.FK_DeptText);


			gwl.setSDT("无");
			gwl.setDTOfWarning(DataType.getCurrentDataTime());
			gwl.setIsEnable(true);

			gwl.setIsPass(false);
			//     gwl.Sender = WebUser.getNo();
			gwl.setPRI(gwf.getPRI());
			gwl.Insert();
		}

			///#endregion 为开始工作创建待办

		// 执行对报表的数据表WFState状态的更新 
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,WFSta=" + dbstr + "WFSta,Title=" + dbstr + "Title,FK_Dept=" + dbstr + "FK_Dept,PFlowNo=" + dbstr + "PFlowNo,PWorkID=" + dbstr + "PWorkID WHERE OID=" + dbstr + "OID";

		//默认启用草稿
		if (fl.getDraftRole() == DraftRole.None)
		{
			ps.Add("WFState", WFState.Blank.getValue());
			ps.Add("WFSta", WFSta.Etc.getValue());
		}
		else if (fl.getDraftRole() == DraftRole.SaveToDraftList)
		{
			//保存到草稿
			ps.Add("WFState", WFState.Draft.getValue());
			ps.Add("WFSta", WFSta.Etc.getValue());
		}
		else if (fl.getDraftRole() == DraftRole.SaveToTodolist)
		{
			//保存到待办
			ps.Add("WFState", WFState.Runing.getValue());
			ps.Add("WFSta", WFSta.Runing.getValue());
		}
		ps.Add("Title", gwf.getTitle());
		ps.Add("FK_Dept", gwf.getFK_Dept());

		ps.Add("PFlowNo", gwf.getPFlowNo());
		ps.Add("PWorkID", gwf.getPWorkID());

		ps.Add("OID", wk.getOID());
		DBAccess.RunSQL(ps);

		////写入日志.
		//WorkNode wn = new WorkNode(wk, nd);
		//wn.AddToTrack(ActionType.CallSubFlow, flowStarter, emp.Name, nd.NodeID, nd.Name, "来自" + WebUser.getNo() + "," + WebUser.getName()
		//    + "工作发起.");


			///#region 更新发送参数.
		if (htWork != null)
		{
			String paras = "";
			for (String key : htWork.keySet())
			{
				paras += "@" + key + "=" + htWork.get(key).toString();
			}

			if (DataType.IsNullOrEmpty(paras) == false && Glo.getIsEnableTrackRec() == true)
			{
				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node";
				ps.Add(GenerWorkerListAttr.Paras, paras);
				ps.Add(GenerWorkerListAttr.WorkID, wk.getOID());
				ps.Add(GenerWorkerListAttr.FK_Node, nd.getNodeID());
				DBAccess.RunSQL(ps);
			}
		}

			///#endregion 更新发送参数.

		return wk.getOID();
	}
	/** 
	 执行工作发送
	 
	 @param fk_flow 工作编号
	 @param workID 工作ID
	 @param ht 节点表单数据
	 @param dsDtl 节点表单从表数据
	 @return 返回发送结果
	*/

	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, java.util.Hashtable ht)
	{
		return Node_SendWork(fk_flow, workID, ht, null);
	}

	public static SendReturnObjs Node_SendWork(String fk_flow, long workID)
	{
		return Node_SendWork(fk_flow, workID, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static SendReturnObjs Node_SendWork(string fk_flow, Int64 workID, Hashtable ht = null, DataSet dsDtl = null)
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, Hashtable ht, DataSet dsDtl)
	{
		return Node_SendWork(fk_flow, workID, ht, dsDtl, 0, null);
	}
	/** 
	 发送工作
	 
	 @param nodeID 节点编号
	 @param workID 工作ID
	 @param toNodeID 发送到的节点编号，如果是0就让ccflow自动计算.
	 @param toEmps 发送到的人员,多个人员用逗号分开比如：zhangsan,lisi. 如果是null则表示让ccflow自动计算.
	 @return 返回执行信息
	*/
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, int toNodeID, String toEmps)
	{
		return Node_SendWork(fk_flow, workID, null, null, toNodeID, toEmps);
	}
	/** 
	 发送工作
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	 @param htWork 节点表单数据(Hashtable中的key与节点表单的字段名相同,value 就是字段值)
	 @return 执行信息
	*/
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, Hashtable htWork, int toNodeID, String nextWorkers)
	{

		return Node_SendWork(fk_flow, workID, htWork, null, toNodeID, nextWorkers, WebUser.getNo(), WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName, null);
	}
	/** 
	 发送工作
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	 @param htWork 节点表单数据(Hashtable中的key与节点表单的字段名相同,value 就是字段值)
	 @param workDtls 节点表单明从表数据(dataset可以包含多个table，每个table的名称与从表名称相同，列名与从表的字段相同, OID,RefPK列需要为空或者null )
	 @param toNodeID 到达的节点，如果是0表示让ccflow自动寻找，否则就按照该参数发送。
	 @param nextWorkers 下一步的接受人，如果多个人员用逗号分开，比如:zhangsan,lisi,
	 如果为空，则标识让ccflow按照节点访问规则自动寻找。
	 @return 执行信息
	*/
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, Hashtable htWork, DataSet workDtls, int toNodeID, String nextWorkers)
	{
		return Node_SendWork(fk_flow, workID, htWork, workDtls, toNodeID, nextWorkers, WebUser.getNo(), WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName, null);
	}
	/** 
	 发送工作
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	 @param htWork 节点表单数据(Hashtable中的key与节点表单的字段名相同,value 就是字段值)
	 @param workDtls 节点表单明从表数据(dataset可以包含多个table，每个table的名称与从表名称相同，列名与从表的字段相同, OID,RefPK列需要为空或者null )
	 @param toNodeID 到达的节点，如果是0表示让ccflow自动寻找，否则就按照该参数发送。
	 @param nextWorkers 下一步的接受人，如果多个人员用逗号分开，比如:zhangsan,lisi,
	 如果为空，则标识让ccflow按照节点访问规则自动寻找。
	 @param execUserNo 执行人编号
	 @param execUserName 执行人名称
	 @param execUserDeptNo 执行人部门名称
	 @param execUserDeptName 执行人部门编号
	 @return 发送的结果对象
	*/
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, Hashtable htWork, DataSet workDtls, int toNodeID, String toEmps, String execUserNo, String execUserName, String execUserDeptNo, String execUserDeptName, String title)
	{

		//给临时的发送变量赋值，解决带有参数的转向。
		Glo.setSendHTOfTemp(htWork);

		int currNodeId = Dev2Interface.Node_GetCurrentNodeID(fk_flow, workID);
		if (htWork != null)
		{
			BP.WF.Dev2Interface.Node_SaveWork(fk_flow, currNodeId, workID, htWork, workDtls);
		}

		// 变量.
		Node nd = new Node(currNodeId);
		Work sw = nd.getHisWork();
		sw.setOID(workID);
		sw.RetrieveFromDBSources();

		Node ndOfToNode = null; //到达节点ID
		if (toNodeID != 0)
		{
			ndOfToNode = new Node(toNodeID);
		}

		//补偿性修复.
		if (nd.getHisRunModel() != RunModel.SubThread)
		{
			if (sw.getFID() != 0)
			{
				sw.DirectUpdate();
			}
		}

		SendReturnObjs objs;
		//执行流程发送.
		WorkNode wn = new WorkNode(sw, nd);
		wn.setExecer(execUserNo);
		wn.setExecerName(execUserName);
		wn.title = title; // 设置标题，有可能是从外部传递过来的标题.
		wn.SendHTOfTemp = htWork;

		if (ndOfToNode == null)
		{
			objs = wn.NodeSend(null, toEmps);
		}
		else
		{
			objs = wn.NodeSend(ndOfToNode, toEmps);
		}


			///#region 更新发送参数.
		if (htWork != null)
		{
			String dbstr = SystemConfig.getAppCenterDBVarStr();
			Paras ps = new Paras();

			String paras = "";
			for (String key : htWork.keySet())
			{
				paras += "@" + key + "=" + htWork.get(key).toString();
				switch (key)
				{
					case WorkSysFieldAttr.SysSDTOfFlow:
						ps = new Paras();
						ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfFlow=" + dbstr + "SDTOfFlow WHERE WorkID=" + dbstr + "WorkID";
						ps.Add(GenerWorkFlowAttr.SDTOfFlow, htWork.get(key).toString());
						ps.Add(GenerWorkerListAttr.WorkID, workID);
						DBAccess.RunSQL(ps);

						break;
					case WorkSysFieldAttr.SysSDTOfNode:
						ps = new Paras();
						ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfNode=" + dbstr + "SDTOfNode WHERE WorkID=" + dbstr + "WorkID";
						ps.Add(GenerWorkFlowAttr.SDTOfNode, htWork.get(key).toString());
						ps.Add(GenerWorkerListAttr.WorkID, workID);
						DBAccess.RunSQL(ps);

						ps = new Paras();
						ps.SQL = "UPDATE WF_GenerWorkerlist SET SDT=" + dbstr + "SDT WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node";
						ps.Add(GenerWorkerListAttr.SDT, htWork.get(key).toString());
						ps.Add(GenerWorkerListAttr.WorkID, workID);
						ps.Add(GenerWorkerListAttr.FK_Node, objs.getVarToNodeID());
						DBAccess.RunSQL(ps);
						break;
					default:
						break;
				}
			}

			if (DataType.IsNullOrEmpty(paras) == false && Glo.getIsEnableTrackRec() == true)
			{
				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node";
				ps.Add(GenerWorkerListAttr.Paras, paras);
				ps.Add(GenerWorkerListAttr.WorkID, workID);
				ps.Add(GenerWorkerListAttr.FK_Node, nd.getNodeID());
				DBAccess.RunSQL(ps);
			}
		}
		else
		{
			//判断流程是否启动流程时限
			if (nd.getIsStartNode() && wn.getHisGenerWorkFlow().getWFState() != WFState.ReturnSta)
			{
				LocalDateTime dtOfFlow = LocalDateTime.now();
				LocalDateTime dtOfFlowWarning = LocalDateTime.now();
				Part part = new Part();
				part.setMyPK( nd.getFK_Flow() + "_0_DeadLineRole";
				int count = part.RetrieveFromDBSources();
				if (count != 0)
				{
					int tag1 = Integer.parseInt(part.getTag1());
					int tag2 = Integer.parseInt(part.getTag2());
					int tag7 = Integer.parseInt(part.getTag7());
					switch (tag7)
					{
						case 0:
							tag7 = 12;
							break;
						case 1:
							tag7 = 24;
							break;
						case 2:
							tag7 = 48;
							break;
						case 3:
							tag7 = 72;
							break;
						default:
							break;
					}
					//获取时限时间
					dtOfFlow = Glo.AddDayHoursSpan(LocalDateTime.now(), tag1, tag2, Integer.parseInt(part.getTag3()), (TWay)Integer.parseInt(part.getTag4()));
					//计算警告日期.  时限时间-预警时间
					dtOfFlowWarning = Glo.AddDayHoursSpan(LocalDateTime.now(), (tag1 * 24 + tag2 - tag7) / 24, (tag1 * 24 + tag2 - tag7) % 24, Integer.parseInt(part.getTag3()), (TWay)Integer.parseInt(part.getTag4()));
					String dbstr = SystemConfig.getAppCenterDBVarStr();
					Paras ps = new Paras();
					ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfFlow=" + dbstr + "SDTOfFlow,SDTOfFlowWarning=" + dbstr + "SDTOfFlowWarning WHERE WorkID=" + dbstr + "WorkID";
					ps.Add(GenerWorkFlowAttr.SDTOfFlow, dtOfFlow.toString(DataType.getSysDataTimeFormat()));
					ps.Add(GenerWorkFlowAttr.SDTOfFlowWarning, dtOfFlowWarning.toString(DataType.getSysDataTimeFormat()));
					ps.Add(GenerWorkerListAttr.WorkID, workID);
					DBAccess.RunSQL(ps);

				}
			}
		}

			///#endregion 更新发送参数.

		return objs;

	}
	/** 
	 增加在队列工作中增加一个处理人.
	 这个处理顺序系统已经自动处理了.
	 
	 @param flowNo 流程编号
	 @param nodeID 工作ID
	 @param workid workid
	 @param fid fid
	 @param empNo 要增加的处理人编号
	 @param empName 要增加的处理人名称
	*/
	public static void Node_InsertOrderEmp(String flowNo, int nodeID, long workid, long fid, String empNo, String empName)
	{
		GenerWorkerList gwl = new GenerWorkerList();
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, nodeID);
		if (i == 0)
		{
			throw new RuntimeException("@没有找到当前工作人员的待办，请检查该流程是否已经运行到该节点上来了。");
		}

		gwl.setIsPassInt(100);
		gwl.setIsEnable(true);
		gwl.setFK_Emp(empNo);
		gwl.setFK_EmpText(empName);

		try
		{
			gwl.Insert();
		}
		catch (java.lang.Exception e)
		{
			throw new RuntimeException("@该人员已经存在处理队列中，您不能增加.");
		}

		//开始更新他们的顺序, 首先从数据库里获取他们的顺序.     lxl职位由小到大
		String sql = "SELECT No,Name FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + workid + " AND FK_Node=" + nodeID + " AND IsPass >=100 ) ORDER BY IDX desc";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 100;
		for (DataRow dr : dt.Rows)
		{
			idx++;
			String myEmpNo = dr.get(0).toString();
			sql = "UPDATE WF_GenerWorkerList SET IsPass=" + idx + " WHERE FK_Emp='" + myEmpNo + "' AND WorkID=" + workid + " AND FK_Node=" + nodeID;
			BP.DA.DBAccess.RunSQL(sql);
		}
	}
	/** 
	 把抄送写入待办列表
	 
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @param ccToEmpNo 抄送给
	 @param ccToEmpName 抄送给名称
	 @return 
	*/
	public static String Node_CC_WriteTo_Todolist(int fk_node, long workID, String ccToEmpNo, String ccToEmpName)
	{
		return Node_CC_WriteTo_CClist(fk_node, workID, ccToEmpNo, ccToEmpName, "", "");
	}
	/** 
	 执行抄送
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param toEmpNo 抄送人员编号
	 @param toEmpName 抄送人员人员名称
	 @param msgTitle 标题
	 @param msgDoc 内容
	 @return 执行信息
	*/
	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String toEmpNo, String toEmpName, String msgTitle, String msgDoc)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			Node nd = new Node(fk_node);
			gwf.setFK_Node(fk_node);
			gwf.setFK_Flow(nd.getFK_Flow());
			gwf.setFlowName(nd.getFlowName());
			gwf.setNodeName(nd.getName());
		}

		Node fromNode = new Node(fk_node);

		CCList list = new CCList();
		list.setMyPK( DBAccess.GenerOIDByGUID().toString(); // workID + "_" + fk_node + "_" + empNo;
		list.setFK_Flow(gwf.getFK_Flow());
		list.setFlowName(gwf.getFlowName());
		list.setFK_Node(fk_node);
		list.setNodeName(gwf.getNodeName());
		list.setTitle(msgTitle);
		list.setDoc(msgDoc);
		list.setCCTo(toEmpNo);
		list.setCCToName(toEmpName);

		//增加抄送人部门.
		Emp emp = new Emp(toEmpNo);
		list.setCCToDept(emp.FK_Dept);
		list.setRDT(DataType.getCurrentDataTime());
		list.setRec(WebUser.getNo());
		list.setWorkID(gwf.getWorkID());
		list.setFID(gwf.getFID());
		list.setPFlowNo(gwf.getPFlowNo());
		list.setPWorkID(gwf.getPWorkID());
		//  list.NDFrom = ndFrom;

		//是否要写入待办.
		if (fromNode.getCCWriteTo() == CCWriteTo.CCList)
		{
			list.setInEmpWorks(false); //added by liuxc,2015.7.6
		}
		else
		{
			list.setInEmpWorks(true); //added by liuxc,2015.7.6
		}

		//写入待办和写入待办与抄送列表,状态不同
		if (fromNode.getCCWriteTo() == CCWriteTo.All || fromNode.getCCWriteTo() == CCWriteTo.Todolist)
		{
			list.setHisSta(fromNode.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
		}

		if (fromNode.getIsEndNode() == true) //结束节点只写入抄送列表
		{
			list.setHisSta(CCSta.UnRead);
			list.setInEmpWorks(false);
		}

		try
		{
			list.Insert();
		}
		catch (java.lang.Exception e)
		{
			list.CheckPhysicsTable();
			list.Update();
		}

		//
		BP.WF.Dev2Interface.Port_SendMsg(toEmpNo, msgTitle, msgDoc, "CC" + gwf.getFK_Node() + "_" + gwf.getWorkID(), SMSMsgType.CC, gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());

		//记录日志.
		Glo.AddToTrack(ActionType.CC, gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), gwf.getNodeName(), WebUser.getNo(), WebUser.getName(), gwf.getFK_Node(), gwf.getNodeName(), toEmpNo, toEmpName, msgTitle, null);

		return "已经成功的把工作抄送给:" + toEmpNo + "," + toEmpName;
	}
	/** 
	 执行抄送
	 
	 @param fk_node 节点
	 @param workID 工作ID
	 @param title 标题
	 @param doc 内容
	 @param toEmps 到人员(zhangsan,张三;lisi,李四;wangwu,王五;)
	 @param toDepts 到部门，格式:001,002,003
	 @param toStations 到岗位 格式:001,002,003
	 @param toStations 到权限组 格式:001,002,003
	*/

	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String title, String doc, String toEmps, String toDepts, String toStations)
	{
		return Node_CC_WriteTo_CClist(fk_node, workID, title, doc, toEmps, toDepts, toStations, null);
	}

	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String title, String doc, String toEmps, String toDepts)
	{
		return Node_CC_WriteTo_CClist(fk_node, workID, title, doc, toEmps, toDepts, null, null);
	}

	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String title, String doc, String toEmps)
	{
		return Node_CC_WriteTo_CClist(fk_node, workID, title, doc, toEmps, null, null, null);
	}

	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String title, String doc)
	{
		return Node_CC_WriteTo_CClist(fk_node, workID, title, doc, null, null, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Node_CC_WriteTo_CClist(int fk_node, Int64 workID, string title, string doc, string toEmps = null, string toDepts = null, string toStations = null, string toGroups = null)
	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String title, String doc, String toEmps, String toDepts, String toStations, String toGroups)
	{

		Node nd = new Node(fk_node);

		//计算出来曾经抄送过的人.
		String sql = "SELECT CCTo FROM WF_CCList WHERE FK_Node=" + fk_node + " AND WorkID=" + workID;
		DataTable mydt = DBAccess.RunSQLReturnTable(sql);
		String toAllEmps = ",";
		for (DataRow dr : mydt.Rows)
		{
			toAllEmps += dr.get(0).toString() + ",";
		}

		//录制本次抄送的人员.
		String ccRec = "";

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			gwf.setFK_Node(fk_node);
			gwf.setFK_Flow(nd.getFK_Flow());
			gwf.setFlowName(nd.getFlowName());
			gwf.setNodeName(nd.getName());
		}


			///#region 处理抄送到人员.
		if (toEmps != null)
		{
			String[] emps = toEmps.split("[;]", -1);
			for (String empStr : emps.ToJavaList())
			{
				if (DataType.IsNullOrEmpty(empStr) == true || empStr.contains(",") == false)
				{
					continue;
				}

				String[] strs = empStr.split("[,]", -1);
				String empNo = strs[0];
				String empName = strs[1];

				if (toAllEmps.contains("," + empNo + ",") == true)
				{
					continue;
				}

				CCList list = new CCList();
				list.setMyPK( DBAccess.GenerOIDByGUID().toString(); // workID + "_" + fk_node + "_" + empNo;
				list.setFK_Flow(gwf.getFK_Flow());
				list.setFlowName(gwf.getFlowName());
				list.setFK_Node(fk_node);
				list.setNodeName(gwf.getNodeName());
				list.setTitle(title);
				list.setDoc(doc);
				list.setCCTo(empNo);
				list.setCCToName(empName);
				list.setRDT(DataType.getCurrentDataTime());
				list.setRec(WebUser.getNo());
				list.setWorkID(gwf.getWorkID());
				list.setFID(gwf.getFID());
				list.setPFlowNo(gwf.getPFlowNo());
				list.setPWorkID(gwf.getPWorkID());
				// list.NDFrom = ndFrom;

				//是否要写入待办.
				if (nd.getCCWriteTo() == CCWriteTo.CCList)
				{
					list.setInEmpWorks(false); //added by liuxc,2015.7.6
				}
				else
				{
					list.setInEmpWorks(true); //added by liuxc,2015.7.6
				}

				//写入待办和写入待办与抄送列表,状态不同
				if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist)
				{
					list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
				}

				if (nd.getIsEndNode() == true) //结束节点只写入抄送列表
				{
					list.setHisSta(CCSta.UnRead);
					list.setInEmpWorks(false);
				}

				try
				{
					list.Insert();
				}
				catch (java.lang.Exception e)
				{
					list.CheckPhysicsTable();
					list.Update();
				}

				ccRec += "" + list.getCCToName() + ";";
				//人员编号,加入这个集合.
				toAllEmps += empNo + ",";
			}
		}

			///#endregion 处理抄送到人员.


			///#region 处理抄送到部门.
		if (toDepts != null)
		{
			toDepts = toDepts.replace(";", ",");

			String[] depts = toDepts.split("[,]", -1);
			for (String deptNo : depts.ToJavaList())
			{
				if (DataType.IsNullOrEmpty(deptNo) == true)
				{
					continue;
				}

				sql = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE FK_Dept='" + deptNo + "'";
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows)
				{
					String empNo = dr.get(0).toString();
					String empName = dr.get(1).toString();
					if (toAllEmps.contains("," + empNo + ",") == true)
					{
						continue;
					}

					CCList list = new CCList();
					list.setMyPK( DBAccess.GenerOIDByGUID().toString(); // workID + "_" + fk_node + "_" + empNo;
					list.setFK_Flow(gwf.getFK_Flow());
					list.setFlowName(gwf.getFlowName());
					list.setFK_Node(fk_node);
					list.setNodeName(gwf.getNodeName());
					list.setTitle(title);
					list.setDoc(doc);
					list.setCCTo(empNo);
					list.setCCToName(empName);
					list.setRDT(DataType.getCurrentDataTime());
					list.setRec(WebUser.getNo());
					list.setWorkID(gwf.getWorkID());
					list.setFID(gwf.getFID());
					list.setPFlowNo(gwf.getPFlowNo());
					list.setPWorkID(gwf.getPWorkID());
					// list.NDFrom = ndFrom;

					//是否要写入待办.
					if (nd.getCCWriteTo() == CCWriteTo.CCList)
					{
						list.setInEmpWorks(false); //added by liuxc,2015.7.6
					}
					else
					{
						list.setInEmpWorks(true); //added by liuxc,2015.7.6
					}

					//写入待办和写入待办与抄送列表,状态不同
					if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist)
					{
						list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
					}

					if (nd.getIsEndNode() == true) //结束节点只写入抄送列表
					{
						list.setHisSta(CCSta.UnRead);
						list.setInEmpWorks(false);
					}

					try
					{
						list.Insert();
					}
					catch (java.lang.Exception e2)
					{
						list.CheckPhysicsTable();
						list.Update();
					}

					//录制本次抄送到的人员.
					ccRec += "" + list.getCCToName() + ";";

					//人员编号,加入这个集合.
					toAllEmps += empNo + ",";
				}
			}
		}

			///#endregion 处理抄送到部门.


			///#region 处理抄送到岗位.
		if (toStations != null)
		{
			toStations = toStations.replace(";", ",");
			String[] stas = toStations.split("[,]", -1);
			for (String staNo : stas)
			{
				if (DataType.IsNullOrEmpty(staNo) == true)
				{
					continue;
				}

				sql = "SELECT No,Name, a.FK_Dept FROM Port_Emp a, " + Glo.getEmpStation() + " B  WHERE a.No=B.FK_Emp AND B.FK_Station='" + staNo + "'";

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows)
				{
					String empNo = dr.get(0).toString();
					String empName = dr.get(1).toString();
					if (toAllEmps.contains("," + empNo + ",") == true)
					{
						continue;
					}

					CCList list = new CCList();
					list.setMyPK( DBAccess.GenerOIDByGUID().toString(); // workID + "_" + fk_node + "_" + empNo;
					list.setFK_Flow(gwf.getFK_Flow());
					list.setFlowName(gwf.getFlowName());
					list.setFK_Node(fk_node);
					list.setNodeName(gwf.getNodeName());
					list.setTitle(title);
					list.setDoc(doc);
					list.setCCTo(empNo);
					list.setCCToName(empName);
					list.setRDT(DataType.getCurrentDataTime());
					list.setRec(WebUser.getNo());
					list.setWorkID(gwf.getWorkID());
					list.setFID(gwf.getFID());
					list.setPFlowNo(gwf.getPFlowNo());
					list.setPWorkID(gwf.getPWorkID());
					// list.NDFrom = ndFrom;

					//是否要写入待办.
					if (nd.getCCWriteTo() == CCWriteTo.CCList)
					{
						list.setInEmpWorks(false); //added by liuxc,2015.7.6
					}
					else
					{
						list.setInEmpWorks(true); //added by liuxc,2015.7.6
					}

					//写入待办和写入待办与抄送列表,状态不同
					if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist)
					{
						list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
					}

					if (nd.getIsEndNode() == true) //结束节点只写入抄送列表
					{
						list.setHisSta(CCSta.UnRead);
						list.setInEmpWorks(false);
					}

					try
					{
						list.Insert();
					}
					catch (java.lang.Exception e3)
					{
						list.CheckPhysicsTable();
						list.Update();
					}

					//录制本次抄送到的人员.
					ccRec += "" + list.getCCToName() + ";";

					//人员编号,加入这个集合.
					toAllEmps += empNo + ",";
				}
			}
		}

			///#endregion.


			///#region 抄送到组.
		if (toGroups != null)
		{
			toGroups = toGroups.replace(";", ",");
			String[] groups = toGroups.split("[,]", -1);

			for (String group : groups)
			{
				if (DataType.IsNullOrEmpty(group) == true)
				{
					continue;
				}

				//解决分组下的岗位人员.
				sql = "SELECT a.No,a.Name, A.FK_Dept FROM Port_Emp A, " + Glo.getEmpStation() + " B, GPM_GroupStation C  WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Group='" + group + "'";
				sql += " UNION ";
				sql += "SELECT A.No, A.Name, A.FK_Dept FROM Port_Emp A, GPM_GroupEmp B  WHERE A.No=B.FK_Emp AND B.FK_Group='" + group + "'";

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows)
				{
					String empNo = dr.get(0).toString();
					String empName = dr.get(1).toString();
					if (toAllEmps.contains("," + empNo + ",") == true)
					{
						continue;
					}

					CCList list = new CCList();
					list.setMyPK( DBAccess.GenerOIDByGUID().toString(); // workID + "_" + fk_node + "_" + empNo;
					list.setFK_Flow(gwf.getFK_Flow());
					list.setFlowName(gwf.getFlowName());
					list.setFK_Node(fk_node);
					list.setNodeName(gwf.getNodeName());
					list.setTitle(title);
					list.setDoc(doc);
					list.setCCTo(empNo);
					list.setCCToName(empName);
					list.setRDT(DataType.getCurrentDataTime());
					list.setRec(WebUser.getNo());
					list.setWorkID(gwf.getWorkID());
					list.setFID(gwf.getFID());
					list.setPFlowNo(gwf.getPFlowNo());
					list.setPWorkID(gwf.getPWorkID());
					// list.NDFrom = ndFrom;

					//是否要写入待办.
					if (nd.getCCWriteTo() == CCWriteTo.CCList)
					{
						list.setInEmpWorks(false); //added by liuxc,2015.7.6
					}
					else
					{
						list.setInEmpWorks(true); //added by liuxc,2015.7.6
					}

					//写入待办和写入待办与抄送列表,状态不同
					if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist)
					{
						list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
					}

					if (nd.getIsEndNode() == true) //结束节点只写入抄送列表
					{
						list.setHisSta(CCSta.UnRead);
						list.setInEmpWorks(false);
					}

					try
					{
						list.Insert();
					}
					catch (java.lang.Exception e4)
					{
						list.CheckPhysicsTable();
						list.Update();
					}

					//录制本次抄送到的人员.
					ccRec += "" + list.getCCToName() + ";";

					//人员编号,加入这个集合.
					toAllEmps += empNo + ",";
				}
			}
		}

			///#endregion 抄送到组

		return ccRec;

		////记录日志.
		//Glo.AddToTrack(ActionType.CC, gwf.FK_Flow, workID, gwf.FID, gwf.FK_Node, gwf.NodeName,
		//    WebUser.getNo(), WebUser.getName(), gwf.FK_Node, gwf.NodeName, toAllEmps, toAllEmps, title, null);
	}
	/** 
	 执行删除
	 
	 @param mypk 删除
	*/
	public static void Node_CC_DoDel(String mypk)
	{
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM WF_CCList WHERE MyPK=" + SystemConfig.getAppCenterDBVarStr() + "MyPK";
		ps.Add(CCListAttr.MyPK, mypk);
		BP.DA.DBAccess.RunSQL(ps);
	}
	/** 
	 设置抄送状态
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	 @param empNo 人员编号
	 @param sta 状态
	*/
	public static void Node_CC_SetSta(int nodeID, long workid, String empNo, CCSta sta)
	{
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_CCList   SET Sta=" + dbstr + "Sta,CDT=" + dbstr + "CDT WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node AND CCTo=" + dbstr + "CCTo";
		ps.Add(CCListAttr.Sta, sta.getValue());
		ps.Add(CCListAttr.CDT, DataType.getCurrentDataTime());
		ps.Add(CCListAttr.WorkID, workid);
		ps.Add(CCListAttr.FK_Node, nodeID);
		ps.Add(CCListAttr.CCTo, empNo);
		BP.DA.DBAccess.RunSQL(ps);
	}
	/** 
	 执行读取
	 
	 @param mypk
	*/
	public static void Node_CC_SetRead(String mypk)
	{
		if (DataType.IsNullOrEmpty(mypk))
		{
			return;
		}

		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_CCList SET Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta  WHERE MyPK=" + SystemConfig.getAppCenterDBVarStr() + "MyPK";
		ps.Add(CCListAttr.Sta, CCSta.Read.getValue());
		ps.Add(CCListAttr.MyPK, mypk);
		BP.DA.DBAccess.RunSQL(ps);
	}
	/** 
	 设置抄送读取
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	 @param empNo 读取人员编号
	*/
	public static void Node_CC_SetRead(int nodeID, long workid, String empNo)
	{
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_CCList SET Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta  WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND CCTo=" + SystemConfig.getAppCenterDBVarStr() + "CCTo";
		ps.Add(CCListAttr.Sta, CCSta.Read.getValue());
		ps.Add(CCListAttr.WorkID, workid);
		ps.Add(CCListAttr.FK_Node, nodeID);
		ps.Add(CCListAttr.CCTo, empNo);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET IsRead=1 WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		ps.Add(GenerWorkerListAttr.FK_Node, nodeID);
		ps.Add(GenerWorkerListAttr.FK_Emp, empNo);

		DBAccess.RunSQL(ps);
	}
	/** 
	 执行抄送
	 
	 @param fk_flow 流程编号
	 @param fk_node 节点编号
	 @param workID 工作ID
	 @param toEmpNo 抄送给人员编号
	 @param toEmpName 抄送给人员名称
	 @param msgTitle 消息标题
	 @param msgDoc 消息内容
	 @param pFlowNo 父流程编号(可以为null)
	 @param pWorkID 父流程WorkID(可以为0)
	 @return 
	*/
	public static String Node_CC(String fk_flow, int fk_node, long workID, String toEmpNo, String toEmpName, String msgTitle, String msgDoc, String pFlowNo, long pWorkID)
	{
		Flow fl = new Flow(fk_flow);
		Node nd = new Node(fk_node);

		CCList list = new CCList();
		list.setMyPK( DBAccess.GenerOIDByGUID().toString(); // workID + "_" + fk_node + "_" + empNo;
		list.setFK_Flow(fk_flow);
		list.setFlowName(fl.Name);
		list.setFK_Node(fk_node);
		list.setNodeName(nd.getName());
		list.setTitle(msgTitle);
		list.setDoc(msgDoc);
		list.setCCTo(toEmpNo);
		list.setCCToName(toEmpName);
		list.setInEmpWorks(nd.getCCWriteTo() == CCWriteTo.CCList ? false : true); //added by liuxc,2015.7.6
		//写入待办和写入待办与抄送列表,状态不同
		if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist)
		{
			list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
		}
		if (nd.getIsEndNode() == true) //结束节点只写入抄送列表
		{
			list.setHisSta(CCSta.UnRead);
			list.setInEmpWorks(false);
		}
		//增加抄送人部门.
		Emp emp = new Emp(toEmpNo);
		list.setCCToDept(emp.FK_Dept);

		list.setRDT(DataType.getCurrentDataTime());
		list.setRec(WebUser.getNo());
		list.setWorkID(workID);
		list.setFID(0);
		list.setPFlowNo(pFlowNo);
		list.setPWorkID(pWorkID);

		try
		{
			list.Insert();
		}
		catch (java.lang.Exception e)
		{
			list.CheckPhysicsTable();
			list.Update();
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		//记录日志.
		Glo.AddToTrack(ActionType.CC, fk_flow, workID, 0, nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName(), nd.getNodeID(), nd.getName(), toEmpNo, toEmpName, msgTitle, null);

		//发送邮件.
		BP.WF.Dev2Interface.Port_SendMsg(toEmpNo, WebUser.getName() + "把工作:" + gwf.getTitle(), "抄送:" + msgTitle, "CC" + nd.getNodeID() + "_" + workID + "_", BP.WF.SMSMsgType.CC, gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());

		return "已经成功的把工作抄送给:" + toEmpNo + "," + toEmpName;

	}
	/** 
	 删除草稿
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	*/
	public static void Node_DeleteDraft(String fk_flow, long workID)
	{
		//设置引擎表.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 1)
		{
			if (gwf.getFK_Node() != Integer.parseInt(fk_flow + "01"))
			{
				throw new RuntimeException("@该流程非草稿流程不能删除:" + gwf.getTitle());
			}

			if (gwf.getWFState() != WFState.Draft)
			{
				throw new RuntimeException("@非草稿状态不能删除");
			}

			gwf.Delete();
		}

		//删除流程.
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Flow fl = new Flow(fk_flow);
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM " + fl.getPTable() + " WHERE OID=" + dbstr + "OID ";
		ps.Add(GERptAttr.OID, workID);
		DBAccess.RunSQL(ps);


		//删除开始节点数据.
		try
		{
			ps = new Paras();
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(fk_flow + "01") + " WHERE OID=" + dbstr + "OID ";
			ps.Add(GERptAttr.OID, workID);
			DBAccess.RunSQL(ps);
		}
		catch (java.lang.Exception e)
		{
		}

	}
	/** 
	 把草稿设置待办
	 
	 @param fk_flow
	 @param workID
	*/
	public static void Node_SetDraft2Todolist(String fk_flow, long workID)
	{
		//设置引擎表.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 1 && (gwf.getWFState() == WFState.Draft || gwf.getWFState() == WFState.Blank || gwf.getWFState() == WFState.Runing))
		{
			if (gwf.getFK_Node() != Integer.parseInt(fk_flow + "01"))
			{
				throw new RuntimeException("@设置待办错误，只有在开始节点时才能设置待办，现在的节点是:" + gwf.getNodeName());
			}

			gwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName() + ";");
			gwf.setTodoEmpsNum(1);
			gwf.setWFState(WFState.Runing);
			gwf.Update();
			//重置标题
			Flow_ReSetFlowTitle(fk_flow, gwf.getFK_Node(), gwf.getWorkID());
		}
	}
	/** 
	 设置当前工作的应该完成日期.
	 
	 @param workID 设置的WorkID.
	 @param sdt 应完成日期
	*/
	public static void Node_SetSDT(long workID, String sdt)
	{
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET SDT=" + SystemConfig.getAppCenterDBVarStr() + "SDT WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND IsPass=0";
		ps.Add("SDT", sdt);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfNode=" + SystemConfig.getAppCenterDBVarStr() + "SDTOfNode WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID ";
		ps.Add("SDTOfNode", sdt);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

	}
	/** 
	 设置当前工作状态为草稿,如果启用了草稿, 请在开始节点的表单保存按钮下增加上它.
	 注意:必须是在开始节点时调用.
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	*/
	public static void Node_SetDraft(String fk_flow, long workID)
	{
		//设置引擎表.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("@工作丢失..");
		}

		if (gwf.getWFState() == WFState.Blank)
		{
			if (gwf.getFK_Node() != Integer.parseInt(fk_flow + "01"))
			{
				throw new RuntimeException("@设置草稿错误，只有在开始节点时才能设置草稿，现在的节点是:" + gwf.getTitle());
			}

			gwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName() + ";");
			gwf.setTodoEmpsNum(1);
			gwf.setWFState(WFState.Draft);
			gwf.Update();

			GenerWorkerList gwl = new GenerWorkerList();
			gwl.setWorkID(workID);
			gwl.setFK_Node(Integer.parseInt(fk_flow + "01"));
			gwl.setFK_Emp(WebUser.getNo());
			if (gwl.RetrieveFromDBSources() == 0)
			{
				gwl.setFK_EmpText(WebUser.getName());
				gwl.setIsPassInt(0);
				gwl.setSDT(DataType.getCurrentDataTime());
				gwl.setDTOfWarning(DataType.getCurrentDataTime());
				gwl.setIsEnable(true);
				gwl.setIsRead(true);
				gwl.setIsPass(false);
				gwl.Insert();
			}
		}

		Flow fl = new Flow(fk_flow);
		//string sql = "UPDATE "+fl.PTable+" SET WFStarter=1, FlowStater='"+WebUser.getNo()+"' WHERE OID="+workID;

		//@sly .
		String sql = "UPDATE " + fl.getPTable() + " SET  FlowStarter='" + WebUser.getNo() + "',WFState=1 WHERE OID=" + workID;
		DBAccess.RunSQL(sql);
	}
	/** 
	 保存参数，向工作流引擎传入的参数变量.
	 
	 @param workID 工作ID
	 @param paras 参数
	 @return 
	*/
	public static boolean Flow_SaveParas(long workID, String paras)
	{
		AtPara ap = new AtPara(paras);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		for (String key : ap.getHisHT().keySet())
		{
			gwf.SetPara(key, ap.GetValStrByKey(key));
		}
		gwf.Update();
		return true;
	}
	/** 
	 保存
	 
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @return 返回保存的信息
	*/
	public static String Node_SaveWork(String fk_flow, int fk_node, long workID)
	{
		return Node_SaveWork(fk_flow, fk_node, workID, new Hashtable(), null);
	}
	/** 
	 保存
	 
	 @param fk_flow 流程编号
	 @param workID workid
	 @param wk 节点表单参数
	 @return 
	*/
	public static String Node_SaveWork(String fk_flow, int fk_node, long workID, Hashtable wk)
	{
		return Node_SaveWork(fk_flow, fk_node, workID, wk, null);
	}
	/** 
	 保存
	 
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @param htWork 工作数据
	 @return 返回执行信息
	*/
	public static String Node_SaveWork(String fk_flow, int fk_node, long workID, Hashtable htWork, DataSet dsDtls)
	{
		if (htWork == null)
		{
			return "参数错误，保存失败。";
		}

		try
		{
			Node nd = new Node(fk_node);
			Work wk = nd.getHisWork();
			if (workID != 0)
			{
				wk.setOID(workID);
				wk.RetrieveFromDBSources();
			}
			wk.ResetDefaultVal();

			if (htWork != null)
			{
				for (String str : htWork.keySet())
				{
					switch (str)
					{
						case StartWorkAttr.OID:
						case StartWorkAttr.CDT:
						case StartWorkAttr.MD5:
						case StartWorkAttr.Emps:
						case StartWorkAttr.FID:
						case StartWorkAttr.FK_Dept:
						case StartWorkAttr.PRI:
						case StartWorkAttr.Rec:
						case StartWorkAttr.Title:
							continue;
						default:
							break;
					}

					if (wk.Row.ContainsKey(str))
					{
						wk.SetValByKey(str, htWork.get(str));
					}
					else
					{
						wk.Row.Add(str, htWork.get(str));
					}
				}
			}

			wk.setRec(WebUser.getNo());
			wk.setRecText(WebUser.getName());
			wk.SetValByKey(StartWorkAttr.FK_Dept, WebUser.getFK_Dept());
			wk.BeforeSave();
			wk.Save();


				///#region 保存从表
			if (dsDtls != null)
			{
				//保存从表
				for (DataTable dt : dsDtls.Tables)
				{
					for (MapDtl dtl : wk.getHisMapDtls())
					{
						if (!dt.TableName.equals(dtl.No))
						{
							continue;
						}
						//获取dtls
						GEDtls daDtls = new GEDtls(dtl.No);
						daDtls.Delete(GEDtlAttr.RefPK, workID); // 清除现有的数据.

						// 为从表复制数据.
						for (DataRow dr : dt.Rows)
						{
							GEDtl daDtl = daDtls.GetNewEntity instanceof GEDtl ? (GEDtl)daDtls.GetNewEntity : null;
							daDtl.RefPK = String.valueOf(workID);
							//明细列.
							for (DataColumn dc : dt.Columns)
							{
								//设置属性.
								daDtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName));
							}

							daDtl.ResetDefaultVal();

							daDtl.RefPK = String.valueOf(workID);
							daDtl.RDT = DataType.getCurrentDataTime();

							//执行保存.
							daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
						}
					}
				}
			}

				///#endregion 保存从表结束


				///#region 更新发送参数.
			if (htWork != null)
			{
				String paras = "";
				for (String key : htWork.keySet())
				{
					paras += "@" + key + "=" + htWork.get(key).toString();
				}

				if (DataType.IsNullOrEmpty(paras) == false && Glo.getIsEnableTrackRec() == true)
				{
					String dbstr = SystemConfig.getAppCenterDBVarStr();
					Paras ps = new Paras();
					ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node";
					ps.Add(GenerWorkerListAttr.Paras, paras);
					ps.Add(GenerWorkerListAttr.WorkID, workID);
					ps.Add(GenerWorkerListAttr.FK_Node, nd.getNodeID());
					DBAccess.RunSQL(ps);
				}
			}

				///#endregion 更新发送参数.

			if (nd.getSaveModel() == SaveModel.NDAndRpt)
			{
				/* 如果保存模式是节点表与Node与Rpt表. */
				WorkNode wn = new WorkNode(wk, nd);
				GERpt rptGe = nd.getHisFlow().getHisGERpt();
				rptGe.SetValByKey("OID", workID);
				wn.rptGe = rptGe;
				if (rptGe.RetrieveFromDBSources() == 0)
				{
					rptGe.SetValByKey("OID", workID);
					wn.DoCopyRptWork(wk);

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getNo() + "," + WebUser.getName() + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDOnly)
					{
						rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getNo() + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getName() + "@");
					}

					rptGe.SetValByKey(GERptAttr.FlowStarter, WebUser.getNo());
					rptGe.SetValByKey(GERptAttr.FlowStartRDT, DataType.getCurrentDataTime());
					rptGe.SetValByKey(GERptAttr.WFState, 0);
					rptGe.SetValByKey(GERptAttr.FK_NY, DataType.CurrentYearMonth);
					rptGe.SetValByKey(GERptAttr.FK_Dept, WebUser.getFK_Dept());
					rptGe.Insert();
				}
				else
				{
					wn.DoCopyRptWork(wk);
					rptGe.Update();
				}
			}
			//获取表单树的数据
			BP.WF.WorkNode workNode = new WorkNode(workID, fk_node);
			Work treeWork = workNode.CopySheetTree();
			if (treeWork != null)
			{
				wk.Copy(treeWork);
				wk.Update();
			}


			////获取该节点是是否是绑定表单方案, 如果流程节点中的字段与绑定表单的字段相同时赋值 
			//if (nd.FormType == NodeFormType.SheetTree || nd.FormType == NodeFormType.RefOneFrmTree)
			//{
			//    FrmNodes nds = new FrmNodes(fk_flow, fk_node);
			//    foreach (FrmNode item in nds)
			//    {
			//        if (item.FrmEnableRole == FrmEnableRole.Disable)
			//            continue;
			//        if (item.FK_Frm.Equals("ND" + fk_node) == true)
			//            continue;

			//        GEEntity en = null;
			//        try
			//        {
			//            en = new GEEntity(item.FK_Frm);
			//            en.PKVal = workID;
			//            if (en.RetrieveFromDBSources() == 0)
			//            {
			//                continue;
			//            }
			//        }
			//        catch (Exception ex)
			//        {
			//            continue;
			//        }

			//        Attrs frmAttrs = en.getEnMap().getAttrs();
			//        Attrs wkAttrs = wk.getEnMap().getAttrs();
			//        foreach (Attr wkattr in wkAttrs)
			//        {
			//            if (wkattr.Key.Equals(StartWorkAttr.OID) || wkattr.Key.Equals(StartWorkAttr.FID) || wkattr.Key.Equals(StartWorkAttr.CDT)
			//                || wkattr.Key.Equals(StartWorkAttr.RDT) || wkattr.Key.Equals(StartWorkAttr.MD5) || wkattr.Key.Equals(StartWorkAttr.Emps)
			//                || wkattr.Key.Equals(StartWorkAttr.FK_Dept) || wkattr.Key.Equals(StartWorkAttr.PRI) || wkattr.Key.Equals(StartWorkAttr.Rec)
			//                || wkattr.Key.Equals(StartWorkAttr.Title) || wkattr.Key.Equals(GERptAttr.FK_NY) || wkattr.Key.Equals(GERptAttr.FlowEmps)
			//                || wkattr.Key.Equals(GERptAttr.FlowStarter) || wkattr.Key.Equals(GERptAttr.FlowStartRDT) || wkattr.Key.Equals(GERptAttr.WFState))
			//            {
			//                continue;
			//            }

			//            foreach (Attr attr in frmAttrs)
			//            {
			//                if (wkattr.Key.Equals(attr.getKey()))
			//                {
			//                    wk.SetValByKey(wkattr.Key, en.GetValStrByKey(attr.getKey()));
			//                    break;
			//                }

			//            }

			//        }

			//    }
			//    wk.Update();
			//}


				///#region 处理保存后事件
			boolean isHaveSaveAfter = false;
			try
			{
				//处理表单保存后.
				String s = nd.getMapData().DoEvent(FrmEventList.SaveAfter, wk);


				//执行保存前事件.
				s += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.SaveAfter, nd, wk, null);

				if (s != null)
				{
					/*如果不等于null,说明已经执行过数据保存，就让其从数据库里查询一次。*/
					wk.RetrieveFromDBSources();
					isHaveSaveAfter = true;
				}
			}
			catch (RuntimeException ex)
			{
				return "err@在执行保存后的事件期间出现错误:" + ex.getMessage();
			}

				///#endregion


				///#region 为开始工作创建待办.
			if (nd.getIsStartNode() == true)
			{
				GenerWorkFlow gwf = new GenerWorkFlow();
				Flow fl = new Flow(fk_flow);
				if (fl.getDraftRole() == DraftRole.None)
				{
					return "保存成功";
				}

				//规则设置为写入待办，将状态置为运行中，其他设置为草稿.
				WFState wfState = WFState.Blank;
				if (fl.getDraftRole() == DraftRole.SaveToDraftList)
				{
					wfState = WFState.Draft;
				}

				if (fl.getDraftRole() == DraftRole.SaveToTodolist)
				{
					wfState = WFState.Runing;
				}

				//设置标题.
				String title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);

				//修改RPT表的标题
				wk.SetValByKey(GERptAttr.Title, title);
				wk.Update();

				gwf.setWorkID(workID);
				int i = gwf.RetrieveFromDBSources();

				gwf.setTitle(title); //标题.
				if (i == 0)
				{
					gwf.setFlowName(fl.Name);
					gwf.setFK_Flow(fk_flow);
					gwf.setFK_FlowSort(fl.getFK_FlowSort());
					gwf.setSysType(fl.getSysType());

					gwf.setFK_Node(fk_node);
					gwf.setNodeName(nd.getName());
					gwf.setWFState(wfState);

					gwf.setFK_Dept(WebUser.getFK_Dept());
					gwf.setDeptName(WebUser.getFK_DeptName);
					gwf.setStarter(WebUser.getNo());
					gwf.setStarterName(WebUser.getName());
					gwf.setRDT(DataType.getCurrentDataTime());
					gwf.Insert();

					//@sly 这里在代码移动到下面了.

				}
				else
				{
					if (gwf.getWFState() != WFState.ReturnSta)
					{
						gwf.setWFState(wfState);
						gwf.DirectUpdate();
					}
				}

				// 产生工作列表. @sly
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.setWorkID(workID);
				gwl.setFK_Emp(WebUser.getNo());
				gwl.setFK_EmpText(WebUser.getName());

				gwl.setFK_Node(fk_node);
				gwl.setFK_NodeText(nd.getName());
				gwl.setFID(0);

				gwl.setFK_Flow(fk_flow);
				gwl.setFK_Dept(WebUser.getFK_Dept());
				gwl.setFK_DeptT(WebUser.getFK_DeptName);

				gwl.setSDT("无");
				gwl.setDTOfWarning(DataType.getCurrentDataTime());
				gwl.setIsEnable(true);

				gwl.setIsPass(false);
				//  gwl.Sender = WebUser.getNo();
				gwl.setPRI(gwf.getPRI());
				gwl.Save();

			}

				///#endregion 为开始工作创建待办

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@保存失败:" + ex.getMessage();
		}
	}
	/** 
	 保存独立表单
	 
	 @param fk_mapdata 独立表单ID
	 @param workID 工作ID
	 @param htData 独立表单数据Key Value 格式存放.
	 @return 返回执行信息
	*/
	public static void Node_SaveFlowSheet(String fk_mapdata, long workID, Hashtable htData)
	{
		Node_SaveFlowSheet(fk_mapdata, workID, htData, null);
	}
	/** 
	 保存独立表单
	 
	 @param fk_mapdata 独立表单ID
	 @param workID 工作ID
	 @param htData 独立表单数据Key Value 格式存放.
	 @param workDtls 从表数据
	 @return 返回执行信息
	*/
	public static void Node_SaveFlowSheet(String fk_mapdata, long workID, Hashtable htData, DataSet workDtls)
	{
		MapData md = new MapData(fk_mapdata);
		GEEntity en = md.HisGEEn;
		en.SetValByKey("OID", workID);
		int i = en.RetrieveFromDBSources();

		for (String key : htData.keySet())
		{
			en.SetValByKey(key, htData.get(key).toString());
		}

		en.SetValByKey("OID", workID);

		md.DoEvent(FrmEventList.SaveBefore, en);

		if (i == 0)
		{
			en.Insert();
		}
		else
		{
			en.Update();
		}

		md.DoEvent(FrmEventList.SaveAfter, en);


		if (workDtls != null)
		{
			MapDtls dtls = new MapDtls(fk_mapdata);
			//保存从表
			for (DataTable dt : workDtls.Tables)
			{
				for (MapDtl dtl : dtls.ToJavaList())
				{
					if (!dt.TableName.equals(dtl.No))
					{
						continue;
					}
					//获取dtls
					GEDtls daDtls = new GEDtls(dtl.No);
					daDtls.Delete(GEDtlAttr.RefPK, workID); // 清除现有的数据.

					GEDtl daDtl = daDtls.GetNewEntity instanceof GEDtl ? (GEDtl)daDtls.GetNewEntity : null;
					daDtl.RefPK = String.valueOf(workID);

					// 为从表复制数据.
					for (DataRow dr : dt.Rows)
					{
						daDtl.ResetDefaultVal();
						daDtl.RefPK = String.valueOf(workID);

						//明细列.
						for (DataColumn dc : dt.Columns)
						{
							//设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
					}
				}
			}
		}

		md.DoEvent(FrmEventList.SaveAfter, en);
	}
	/** 
	 从任务池里取出来一个子任务
	 
	 @param nodeid 节点编号
	 @param workid 工作ID
	 @param empNo 取出来的人员编号
	*/
	public static boolean Node_TaskPoolTakebackOne(long workid)
	{
		if (Glo.getIsEnableTaskPool() == false)
		{
			throw new RuntimeException("@配置没有设置成共享任务池的状态。");
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getTaskSta() == TaskSta.None)
		{
			throw new RuntimeException("@该任务非共享任务。");
		}

		if (gwf.getTaskSta() == TaskSta.Takeback)
		{
			throw new RuntimeException("@该任务已经被其他人取走。");
		}

		//更新状态。
		gwf.setTaskSta(TaskSta.Takeback);
		gwf.Update();

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		//设置已经被取走的状态。
		ps.SQL = "UPDATE WF_GenerWorkerlist SET IsEnable=-1 WHERE IsEnable=1 AND WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node AND FK_Emp!=" + dbstr + "FK_Emp ";
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		ps.Add(GenerWorkerListAttr.FK_Node, gwf.getFK_Node());
		ps.Add(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
		int i = DBAccess.RunSQL(ps);

		BP.WF.Dev2Interface.WriteTrackInfo(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), workid, 0, "任务被" + WebUser.getName() + "从任务池取走.", "获取");
		if (i > 0)
		{
			Paras ps1 = new Paras();
			//取走后 将WF_GenerWorkFlow 中的 TodoEmps,TodoEmpsNum 修改下  杨玉慧 
			ps1.SQL = "UPDATE WF_GenerWorkFlow SET TodoEmps=" + dbstr + "TodoEmps,TodoEmpsNum=1 WHERE  WorkID=" + dbstr + "WorkID";
			String toDoEmps = WebUser.getNo() + "," + WebUser.getName();
			ps1.Add(GenerWorkFlowAttr.TodoEmps, toDoEmps);
			ps1.Add(GenerWorkerListAttr.WorkID, workid);
			BP.DA.Log.DefaultLogWriteLineInfo(toDoEmps);
			BP.DA.Log.DefaultLogWriteLineInfo(ps1.SQL);
			DBAccess.RunSQL(ps1);
		}

		if (i == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 放入一个任务
	 
	 @param nodeid 节点编号
	 @param workid 工作ID
	 @param empNo 人员ID
	*/
	public static void Node_TaskPoolPutOne(long workid)
	{
		if (Glo.getIsEnableTaskPool() == false)
		{
			throw new RuntimeException("@配置没有设置成共享任务池的状态。");
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getTaskSta() == TaskSta.None)
		{
			throw new RuntimeException("@该任务非共享任务。");
		}

		if (gwf.getTaskSta() == TaskSta.Sharing)
		{
			throw new RuntimeException("@该任务已经是共享状态。");
		}

		// 更新 状态。
		gwf.setTaskSta(TaskSta.Sharing);
		gwf.Update();

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		//设置已经被取走的状态。
		ps.SQL = "UPDATE WF_GenerWorkerlist SET IsEnable=1 WHERE IsEnable=-1 AND WorkID=" + dbstr + "WorkID ";
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		int i = DBAccess.RunSQL(ps);
		if (i < 0) //有可能是只有一个人
		{
			throw new RuntimeException("@流程数据错误,不应当更新不到数据。");
		}

		if (i > 0)
		{
			Paras ps1 = new Paras();
			//设置已经被取走的状态。
			ps1.SQL = "SELECT FK_Emp,FK_EmpText FROM WF_GenerWorkerlist  WHERE IsEnable=1 AND WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node ";
			ps1.Add(GenerWorkerListAttr.WorkID, workid);
			ps1.Add(GenerWorkerListAttr.FK_Node, gwf.getFK_Node());
			ps1.Add(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
			DataTable toDoEmpsTable = DBAccess.RunSQLReturnTable(ps1);
			String toDoEmps = "";
			String toDoEmpsNum = "";
			if (toDoEmpsTable == null || toDoEmpsTable.Rows.size() == 0)
			{
				throw new RuntimeException("@流程数据错误,没有找到需更新的待处理人。");
			}

			toDoEmpsNum = String.valueOf(toDoEmpsTable.Rows.size());
			for (DataRow dr : toDoEmpsTable.Rows)
			{
				toDoEmps += String.format("%1$s,%2$s", dr.get("FK_Emp").toString(), dr.get("FK_EmpText").toString()) + ";";
			}
			Paras ps2 = new Paras();
			//将任务放回后 将WF_GenerWorkFlow 中的 TodoEmps,TodoEmpsNum 修改下  杨玉慧 
			ps2.SQL = "UPDATE WF_GenerWorkFlow SET TodoEmps=" + dbstr + "TodoEmps,TodoEmpsNum=" + dbstr + "TodoEmpsNum WHERE  WorkID=" + dbstr + "WorkID";
			ps2.Add(GenerWorkFlowAttr.TodoEmps, toDoEmps);
			ps2.Add(GenerWorkFlowAttr.TodoEmpsNum, toDoEmpsNum);
			ps2.Add(GenerWorkerListAttr.WorkID, workid);
			BP.DA.Log.DefaultLogWriteLineInfo(toDoEmps);
			BP.DA.Log.DefaultLogWriteLineInfo(ps2.SQL);
			DBAccess.RunSQL(ps2);
		}

		BP.WF.Dev2Interface.WriteTrackInfo(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), workid, 0, "任务被" + WebUser.getName() + "放入了任务池.", "放入");
	}
	/** 
	 增加下一步骤的接受人(用于当前步骤向下一步骤发送时增加接受人)
	 
	 @param workID 工作ID
	 @param toNodeID 到达的节点ID
	 @param emps 如果多个就用逗号分开
	 @param Del_Selected 是否删除历史选择
	*/

	public static void Node_AddNextStepAccepters(long workID, int toNodeID, String fk_emp)
	{
		Node_AddNextStepAccepters(workID, toNodeID, fk_emp, true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void Node_AddNextStepAccepters(Int64 workID, int toNodeID, string fk_emp, bool del_Selected = true)
	public static void Node_AddNextStepAccepters(long workID, int toNodeID, String fk_emp, boolean del_Selected)
	{
		if (DataType.IsNullOrEmpty(fk_emp) == true)
		{
			return;
		}

		SelectAccper sa = new SelectAccper();
		//删除历史选择
		if (del_Selected == true)
		{
			sa.Delete(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, workID);
		}

		//检查是否是单选？
		BP.WF.Template.Selector st = new Selector(toNodeID);
		if (st.getIsSimpleSelector() == true)
		{
			sa.Delete(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, workID);
		}


		String[] emps = fk_emp.split("[,]", -1);
		for (String empNo : emps.ToJavaList())
		{
			if (DataType.IsNullOrEmpty(empNo) == true)
			{
				continue;
			}
			Emp emp = new Emp();
			emp.setNo (empNo;
			if (emp.RetrieveFromDBSources() == 0)
			{
				return;
			}


			sa.setFK_Emp(emp.No);
			sa.setEmpName(emp.Name);
			sa.setDeptName(emp.FK_DeptText);
			sa.setFK_Node(toNodeID);
			sa.setWorkID(workID);
			sa.ResetPK();
			if (sa.IsExits == false)
			{
				sa.Insert();
			}
		}
	}
	/** 
	 增加下一步骤的接受人(用于当前步骤向下一步骤发送时增加接受人)
	 
	 @param workID 工作ID
	 @param formNodeID 从节点ID
	 @param emp 接收人
	 @param tag 分组维度，可以为空.是为了分流节点向下发送时候，可能有一个工作人员两个或者两个以上的子线程的情况出现。
	 tag 是个维度，这个维度可能是一个类别，一个批次，一个标记，总之它是一个字符串。详细: http://bbs.ccflow.org/showtopic-3065.aspx 
	*/
	public static void Node_AddNextStepAccepter(long workID, int formNodeID, String emp, String tag)
	{
		SelectAccper sa = new SelectAccper();
		sa.Delete(SelectAccperAttr.FK_Node, formNodeID, SelectAccperAttr.WorkID, workID, SelectAccperAttr.FK_Emp, emp, SelectAccperAttr.Tag, tag);

		Emp empEn = new Emp();
		sa.setMyPK( formNodeID + "_" + workID + "_" + emp + "_" + tag;
		empEn.No = emp;
		sa.setTag(tag);
		sa.setFK_Emp(emp);
		sa.setEmpName(empEn.Name);
		sa.setFK_Node(formNodeID);

		sa.setWorkID(workID);
		sa.Insert();
	}
	/** 
	 节点工作挂起
	 
	 @param fk_flow 流程编号
	 @param workid 工作ID
	 @param way 挂起方式
	 @param reldata 解除挂起日期(可以为空)
	 @param hungNote 挂起原因
	 @return 返回执行信息
	*/
	public static String Node_HungUpWork(String fk_flow, long workid, int wayInt, String reldata, String hungNote)
	{
		HungUpWay way = HungUpWay.forValue(wayInt);
		BP.WF.WorkFlow wf = new WorkFlow(fk_flow, workid);
		return wf.DoHungUp(way, reldata, hungNote);
	}
	/** 
	 节点工作取消挂起
	 
	 @param fk_flow 流程编号
	 @param workid 工作ID
	 @param msg 取消挂起原因
	 @return 执行信息
	*/
	public static void Node_UnHungUpWork(String fk_flow, long workid, String msg)
	{
		BP.WF.WorkFlow wf = new WorkFlow(fk_flow, workid);
		wf.DoUnHungUp();
	}
	/** 
	 获取该节点上的挂起时间
	 
	 @param flowNo 流程编号
	 @param nodeID 节点ID
	 @param workid 工作ID
	 @return 返回时间串，如果没有挂起的动作就抛出异常.
	*/
	public static TimeSpan Node_GetHungUpTimeSpan(String flowNo, int nodeID, long workid)
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		String instr = ActionType.HungUp.getValue() + "," + ActionType.UnHungUp.getValue();
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + dbstr + "WorkID AND " + TrackAttr.ActionType + " in (" + instr + ")  and  NDFrom=" + dbstr + "NDFrom ";
		ps.Add(TrackAttr.WorkID, workid);
		ps.Add(TrackAttr.NDFrom, nodeID);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		LocalDateTime dtStart = LocalDateTime.now();
		LocalDateTime dtEnd = LocalDateTime.now();
		for (DataRow item : dt.Rows)
		{
			ActionType at = (ActionType)item.get(TrackAttr.ActionType);

			//挂起时间.
			if (at == ActionType.HungUp)
			{
				dtStart = DataType.ParseSysDateTime2DateTime(item.get(TrackAttr.RDT).toString());
			}

			//解除挂起时间.
			if (at == ActionType.UnHungUp)
			{
				dtEnd = DataType.ParseSysDateTime2DateTime(item.get(TrackAttr.RDT).toString());
			}
		}

		TimeSpan ts = dtEnd - dtStart;
		return ts;
	}
	/** 
	 执行加签
	 
	 @param workid 工作ID
	 @param askfor 加签方式
	 @param askForEmp 请求人员
	 @param askForNote 内容
	 @return 
	*/
	public static String Node_Askfor(long workid, AskforHelpSta askforSta, String askForEmp, String askForNote)
	{
		//检查人员是否存在.
		Emp emp = new Emp();
		emp.setNo (askForEmp;
		if (emp.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("@要加签的人员编号错误:" + askForEmp);
		}

		//获得当前流程注册信息.
		BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(workid);

		//检查当前人员是否开可以执行当前的工作?
		if (Flow_IsCanDoCurrentWork(gwf.getWorkID(), WebUser.getNo()) == false)
		{
			throw new RuntimeException("@当前的工作已经被别人处理或者您没有处理该工作的权限.");
		}

		//检查被加签的人是否在当前的队列中.
		GenerWorkerLists gwls = new GenerWorkerLists(workid, gwf.getFK_Node());
		if (gwls.Contains(GenerWorkerListAttr.FK_Emp, askForEmp, GenerWorkerListAttr.IsEnable, 0) == true)
		{
			throw new RuntimeException("@加签失败，您选择的加签人可以处理当前的工作。");
		}

		gwf.setWFState(WFState.Askfor); // 更新流程为加签状态.
		gwf.Update();

		// 设置当前状态为 2 表示加签状态.
		if (gwls.size() == 0)
		{
			/*可能是第一个节点.*/
			GenerWorkerList gwl = new GenerWorkerList();
			gwl.Copy(gwf);
			gwl.setWorkID(workid);
			gwl.setFK_Emp(askForEmp);
			gwl.setFK_Node(gwf.getFK_Node());
			gwl.setFK_NodeText(gwl.getFK_NodeText());
			gwl.setFK_Emp(WebUser.getNo());
			gwl.setFK_EmpText(WebUser.getName());
			gwl.setFK_Dept(WebUser.getFK_Dept());
			gwl.setFK_DeptT(WebUser.getFK_DeptName);

			gwl.setIsPassInt(askforSta.getValue());
			gwl.Insert();
			//重新查询.
			gwls = new GenerWorkerLists(workid, gwf.getFK_Node());

			//设置流程标题.
			if (gwf.getTitle().length() == 0)
			{
				Flow_SetFlowTitle(gwf.getFK_Flow(), workid, "来自" + WebUser.getName() + "的工作加签.");
			}
		}
		// endWarning.


		// 处理状态.
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.getIsEnable() == false)
			{
				continue;
			}

			if (item.getFK_Emp().equals(WebUser.getNo()))
			{
				// GenerWorkerList gwl = gwls[0] as GenerWorkerList;
				item.setIsPassInt(askforSta.getValue());
				item.Update();

				// 更换主键后，执行insert ,让被加签人有代办工作.
				item.setIsPassInt(0);
				item.setFK_Emp(emp.No);
				item.setFK_EmpText(emp.Name);
				try
				{
					item.Insert();
				}
				catch (java.lang.Exception e)
				{
					item.Update();
				}
			}
			else
			{
				item.Update();
			}
		}

		//写入日志.
		BP.WF.Dev2Interface.WriteTrack(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), workid, gwf.getFID(), askForNote, ActionType.AskforHelp, "", null, null, emp.No, emp.Name);

		Flow fl = new Flow(gwf.getFK_Flow());
		BP.WF.Dev2Interface.Port_SendMsg(askForEmp, gwf.getTitle(), askForNote, "AK" + gwf.getFK_Node() + "_" + gwf.getWorkID(), SMSMsgType.AskFor, gwf.getFK_Flow(), gwf.getFK_Node(), workid, gwf.getFID());
		//更新状态.
		DBAccess.RunSQL("UPDATE " + fl.getPTable() + " SET WFState=" + WFState.Askfor.getValue() + " WHERE OID=" + workid);

		//设置成工作未读。
		BP.WF.Dev2Interface.Node_SetWorkUnRead(workid, askForEmp);

		String msg = "您的工作已经提交给(" + askForEmp + " " + emp.Name + ")加签了。";

		//加签后事件
		BP.WF.Node hisNode = new BP.WF.Node(gwf.getFK_Node());
		Work currWK = hisNode.getHisWork();
		currWK.setOID(gwf.getWorkID());
		currWK.Retrieve();

		//执行加签后的事件.
		msg += fl.DoFlowEventEntity(EventListOfNode.AskerAfter, hisNode, currWK, null);

		return msg;
	}
	/** 
	 答复加签信息
	 
	 @param fk_flow 流程编号
	 @param fk_node 节点编号
	 @param workid 工作ID
	 @param fid FID
	 @param replyNote 答复信息
	 @return 
	*/
	public static String Node_AskforReply(long workid, String replyNote)
	{
		// 把回复信息临时的写入 流程注册信息表以便让发送方法获取这个信息写入日志.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setParas_AskForReply(replyNote);
		gwf.Update();

		Node nd = new Node(gwf.getFK_Node());
		String info = "";
		try
		{
			//执行发送, 在发送的方法里面已经做了判断了,并且把 回复的信息写入了日志.
			info = BP.WF.Dev2Interface.Node_SendWork(gwf.getFK_Flow(), workid).ToMsgOfHtml();
		}
		catch (RuntimeException ex)
		{
			if (ex.getMessage().Contains("请选择下一步骤工作") == true || ex.getMessage().Contains("用户没有选择发送到的节点") == true)
			{
				if (nd.getCondModel() == CondModel.ByUserSelected)
				{
					/*如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
					return "SelectNodeUrl@./WorkOpt/ToNodes.htm?FK_Flow=" + gwf.getFK_Flow() + "&FK_Node=" + gwf.getFK_Node() + "&WorkID=" + gwf.getWorkID() + "&FID=" + gwf.getFID();

				}
				return "err@下一个节点的接收人规则是，当前节点选择来选择，在当前节点属性里您没有启动接受人按钮，系统自动帮助您启动了，请关闭窗口重新打开。" + ex.getMessage();
			}
			return ex.getMessage();
		}

		Node node = new Node(gwf.getFK_Node());
		Work wk = node.getHisWork();
		wk.setOID(workid);
		wk.RetrieveFromDBSources();

		//恢复加签后执行事件
		info += node.getHisFlow().DoFlowEventEntity(EventListOfNode.AskerReAfter, node, wk, null);
		return info;
	}
	/** 
	 执行工作分配
	 
	 @param flowNo 流程编号
	 @param nodeID 节点ID
	 @param workID 工作ID
	 @param fid FID
	 @param toEmps 要分配的人，多个人用逗号分开.
	 @param msg 分配原因.
	 @return 分配信息.
	*/
	public static String Node_Allot(String flowNo, int nodeID, long workID, long fid, String toEmps, String msg)
	{
		//生成实例.
		GenerWorkerLists gwls = new GenerWorkerLists(workID, nodeID);

		//要分配给的人员.
		String[] emps = toEmps.split("[,]", -1);
		for (String empNo : emps.ToJavaList())
		{
			if (DataType.IsNullOrEmpty(empNo) == true)
			{
				continue;
			}

			//人员实体.
			Emp empEmp = new Emp(empNo);

			GenerWorkerList gwl = null; //接收人

			//开始找接收人.
			for (GenerWorkerList item : gwls.ToJavaList())
			{
				if (item.getFK_Emp().equals(empNo))
				{
					gwl = item;
					break;
				}
			}

			// 没有找到的情况, 就获得一个实例，作为数据样本然后把数据insert.
			if (gwl == null)
			{
				gwl = gwls[0] instanceof GenerWorkerList ? (GenerWorkerList)gwls[0] : null;
				gwl.setFK_Emp(empEmp.No);
				gwl.setFK_EmpText(empEmp.Name);
				gwl.setIsEnable(true);
				gwl.setIsPassInt(0);
				gwl.Insert();
				continue;
			}

			//如果被禁用了，就启用他.
			if (gwl.getIsEnable() == false)
			{
				gwl.setIsEnable(true);
				gwl.Update();
			}
		}
		return "分配成功.";
	}
	/** 
	 工作移交
	 
	 @param flowNo
	 @param nodeID
	 @param workID
	 @param fid
	 @param toEmp
	 @param msg
	 @return 
	*/
	public static String Node_Shift(String flowNo, int nodeID, long workID, long fid, String toEmp, String msg)
	{
		return Node_Shift(workID, toEmp, msg);
	}
	/** 
	 工作移交
	 
	 @param workID 工作ID
	 @param toEmp 要移交的人
	 @param msg 移交信息
	 @return 执行结果
	*/
	public static String Node_Shift(long workID, String toEmp, String msg)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workID);

		int i = 0;
		//人员.
		Emp emp = new Emp(toEmp);
		Node nd = new Node(gwf.getFK_Node());
		Work work = nd.getHisWork();
		work.setOID(workID);
		if (nd.getTodolistModel() == TodolistModel.Order || nd.getTodolistModel() == TodolistModel.Teamup || nd.getTodolistModel() == TodolistModel.TeamupGroupLeader)
		{
			/*如果是队列模式，或者是协作模式. */
			try
			{
				String sql = "UPDATE WF_GenerWorkerlist SET FK_Emp='" + emp.No + "', FK_EmpText='" + emp.Name + "' WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Node=" + gwf.getFK_Node() + " AND WorkID=" + workID;
				i = BP.DA.DBAccess.RunSQL(sql);
			}
			catch (java.lang.Exception e)
			{
				return "@移交失败，您所移交的人员(" + emp.No + " " + emp.Name + ")已经在代办列表里.";
			}

			//记录日志.
			Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName(), nd.getNodeID(), nd.getName(), toEmp, emp.Name, msg, null);

			String info = "@工作移交成功。@您已经成功的把工作移交给：" + emp.No + " , " + emp.Name;

			//移交后事件 @yuanlina
			String atPara1 = "@SendToEmpIDs=" + emp.No;
			info += "@" + nd.getHisFlow().DoFlowEventEntity(EventListOfNode.ShitAfter, nd, work, atPara1);

			info += "@<a href='" + Glo.getCCFlowAppPath() + "WF/MyFlowInfo.htm?DoType=UnShift&FK_Flow=" + nd.getFK_Flow() + "&WorkID=" + workID + "&FK_Node=" + gwf.getFK_Node() + "&FID=" + gwf.getFID() + "' ><img src='./Img/Action/UnSend.png' border=0 />撤消工作移交</a>.";

			//处理移交后发送的消息事件 @yuanlina
			PushMsgs pms1 = new PushMsgs();
			pms1.Retrieve(PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Event, EventListOfNode.ShitAfter);
			for (PushMsg pm : pms1)
			{
				pm.DoSendMessage(nd, nd.getHisWork(), null, null, null, emp.No);
			}

			return "移交成功.";
		}

		if (gwf.getWFSta() == WFSta.Complete)
		{
			throw new RuntimeException("@流程已经完成，您不能执行移交了。");
		}

		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.WorkID, gwf.getWorkID());
		gwls.Delete(GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.WorkID, gwf.getWorkID());

		for (GenerWorkerList item : gwls.ToJavaList())
		{
			item.setFK_Emp(emp.No);
			item.setFK_EmpText(emp.Name);
			item.setIsEnable(true);
			item.Insert();
			break;
		}

		gwf.setWFState(WFState.Shift);
		gwf.setTodoEmpsNum(1);
		gwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName() + ";");
		gwf.Update();


		ShiftWork sf = new ShiftWork();
		sf.setWorkID(workID);
		sf.setFK_Node(gwf.getFK_Node());
		sf.setToEmp(toEmp);
		sf.setToEmpName(emp.Name);
		sf.setNote(msg);
		sf.setFK_Emp(WebUser.getNo());
		sf.setFK_EmpName(WebUser.getName());
		sf.Insert();

		//记录日志.
		Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName(), nd.getNodeID(), nd.getName(), toEmp, emp.Name, msg, null);


		String inf1o = "@工作移交成功。@您已经成功的把工作移交给：" + emp.No + " , " + emp.Name;
		//移交后事件 @yuanlina
		String atPara = "@SendToEmpIDs=" + emp.No;
		inf1o += "@" + nd.getHisFlow().DoFlowEventEntity(EventListOfNode.ShitAfter, nd, work, atPara);


		return inf1o;
	}
	/** 
	 撤销移交
	 
	 @param flowNo 撤销编号
	 @param workID 工作ID
	 @return 返回撤销信息
	*/
	public static String Node_ShiftUn(String flowNo, long workID)
	{
		WorkFlow mwf = new WorkFlow(flowNo, workID);
		return mwf.DoUnShift();
	}
	/** 
	 执行工作退回(退回指定的点)
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	 @param fid 流程ID
	 @param currentNodeID 当前节点ID
	 @param returnToNodeID 退回到的工作ID
	 @param returnToEmp 退回到人员
	 @param msg 退回原因
	 @param isBackToThisNode 退回后是否要原路返回？
	 @return 执行结果，此结果要提示给用户。
	*/

	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID, String returnToEmp, String msg)
	{
		return Node_ReturnWork(fk_flow, workID, fid, currentNodeID, returnToNodeID, returnToEmp, msg, false);
	}

	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID, String returnToEmp)
	{
		return Node_ReturnWork(fk_flow, workID, fid, currentNodeID, returnToNodeID, returnToEmp, "无", false);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Node_ReturnWork(string fk_flow, Int64 workID, Int64 fid, int currentNodeID, int returnToNodeID, string returnToEmp, string msg = "无", bool isBackToThisNode = false)
	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID, String returnToEmp, String msg, boolean isBackToThisNode)
	{
		WorkReturn wr = new WorkReturn(fk_flow, workID, fid, currentNodeID, returnToNodeID, returnToEmp, isBackToThisNode, msg);
		return wr.DoIt();
	}
	/** 
	 退回
	 
	 @param workID 工作ID
	 @param returnToNodeID 要退回的节点,0 表示上一个节点或者指定的节点.
	 @param msg 退回信息
	 @param isBackToThisNode 是否原路返回
	 @return 执行结果
	*/
	public static String Node_ReturnWork(long workID, int returnToNodeID, String msg, boolean isBackToThisNode)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		return Node_ReturnWork(gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), returnToNodeID, null, msg, isBackToThisNode);
	}
	/** 
	 退回
	 
	 @param fk_flow 流程编号
	 @param workID 工作ID
	 @param fid 流程ID
	 @param currentNodeID 当前节点
	 @param returnToNodeID 退回到节点
	 @param msg 退回消息
	 @param isBackToThisNode 是否原路返回
	 @return 退回执行的信息，执行不成功就抛出异常。
	*/
	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID, String msg, boolean isBackToThisNode)
	{
		return Node_ReturnWork(fk_flow, workID, fid, currentNodeID, returnToNodeID, null, msg, isBackToThisNode);
	}
	/** 
	 获取当前工作的NodeID
	 
	 @param fk_flow 流程编号
	 @param workid 工作ID
	 @return 指定工作的NodeID.
	*/
	public static int Node_GetCurrentNodeID(String fk_flow, long workid)
	{
		int nodeID = BP.DA.DBAccess.RunSQLReturnValInt("SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + workid + " AND FK_Flow='" + fk_flow + "'", 0);
		if (nodeID == 0)
		{
			return Integer.parseInt(fk_flow + "01");
		}

		return nodeID;
	}

	/** 
	 删除子线程
	 
	 @param fk_flow 流程编号
	 @param fid 流程ID
	 @param workid 工作ID
	*/
	public static void Node_FHL_KillSubFlow(String fk_flow, long fid, long workid)
	{
		WorkFlow wkf = new WorkFlow(fk_flow, workid);
		wkf.DoDeleteWorkFlowByReal(true);
	}
	/** 
	 合流点驳回子线程
	 
	 @param fk_flow 流程编号
	 @param fid 流程ID
	 @param workid 子线程ID
	 @param msg 驳回消息
	*/
	public static String Node_FHL_DoReject(String fk_flow, int NodeSheetfReject, long fid, long workid, String msg)
	{
		WorkFlow wkf = new WorkFlow(fk_flow, workid);
		return wkf.DoReject(fid, NodeSheetfReject, msg);
	}

	/** 
	 跳转审核取回
	 
	 @param fromNodeID 从节点ID
	 @param workid 工作ID
	 @param tackToNodeID 取回到的节点ID
	 @return 
	*/

	public static String Node_Tackback(int fromNodeID, long workid, int tackToNodeID)
	{
		return Node_Tackback(fromNodeID, workid, tackToNodeID, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string Node_Tackback(int fromNodeID, Int64 workid, int tackToNodeID, string doMsg = null)
	public static String Node_Tackback(int fromNodeID, long workid, int tackToNodeID, String doMsg)
	{
		if (doMsg == null)
		{
			doMsg = " 执行跳转审核的取回";
		}

		/*
		 * 1,首先检查是否有此权限.
		 * 2, 执行工作跳转.
		 * 3, 执行写入日志.
		 */
		Node nd = new Node(tackToNodeID);
		switch (nd.getHisDeliveryWay())
		{
			case ByPreviousNodeFormEmpsField:
				break;
		}

		WorkNode wn = new WorkNode(workid, fromNodeID);
		String msg = wn.NodeSend(new Node(tackToNodeID), WebUser.getNo()).ToMsgOfHtml();
		wn.AddToTrack(ActionType.Tackback, WebUser.getNo(), WebUser.getName(), tackToNodeID, nd.getName(), doMsg);
		return msg;
	}
	/** 
	 执行抄送已阅
	 
	 @param fk_flow 流程编号
	 @param fk_node 流程节点
	 @param workid 工作id
	 @param fid 流程id
	 @param checkNote 填写意见
	*/
	public static void Node_DoCCCheckNote(String fk_flow, int fk_node, long workid, long fid, String checkNote)
	{
		FrmWorkCheck fwc = new FrmWorkCheck(fk_node);

		BP.WF.Dev2Interface.WriteTrackWorkCheck(fk_flow, fk_node, workid, fid, checkNote, fwc.getFWCOpLabel());

		//设置审核完成.
		BP.WF.Dev2Interface.Node_CC_SetSta(fk_node, workid, WebUser.getNo(), BP.WF.Template.CCSta.CheckOver);

	}
	/** 
	 设置是此工作为读取状态
	 
	 @param nodeID 节点编号
	 @param workid 工作ID
	*/
	public static void Node_SetWorkRead(int nodeID, long workid)
	{
		Node_SetWorkRead(nodeID, workid, WebUser.getNo());
	}
	/** 
	 设置是此工作为读取状态
	 
	 @param nodeID 节点ID
	 @param workid WorkID
	 @param empNo 操作员
	*/
	public static void Node_SetWorkRead(int nodeID, long workid, String empNo)
	{
		Node nd = new Node(nodeID);

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsRead=1 WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node AND FK_Emp=" + dbstr + "FK_Emp";
		ps.Add("WorkID", workid);
		ps.Add("FK_Node", nodeID);
		ps.Add("FK_Emp", empNo);
		if (DBAccess.RunSQL(ps) == 0)
		{
			//throw new Exception("设置的工作不存在，或者当前的登陆人员[" + empNo + "]已经改变，请重新登录。");
		}

		// 判断当前节点的已读回执.
		if (nd.getReadReceipts() == ReadReceipts.None)
		{
			return;
		}

		boolean isSend = false;
		if (nd.getReadReceipts() == ReadReceipts.Auto)
		{
			isSend = true;
		}

		if (nd.getReadReceipts() == ReadReceipts.BySysField)
		{
			/*获取上一个节点ID */
			Nodes fromNodes = nd.getFromNodes();
			int fromNodeID = 0;
			for (Node item : fromNodes)
			{
				ps = new Paras();
				ps.SQL = "SELECT FK_Node FROM WF_GenerWorkerlist WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node ";
				ps.Add("WorkID", workid);
				ps.Add("FK_Node", item.getNodeID());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					continue;
				}

				fromNodeID = item.getNodeID();
				break;
			}
			if (fromNodeID == 0)
			{
				throw new RuntimeException("@没有找到它的上一步工作。");
			}

			try
			{
				ps = new Paras();
				ps.SQL = "SELECT " + BP.WF.WorkSysFieldAttr.SysIsReadReceipts + " FROM ND" + fromNodeID + "    WHERE OID=" + dbstr + "OID";
				ps.Add("OID", workid);
				DataTable dt1 = DBAccess.RunSQLReturnTable(ps);
				if (dt1.get(0).getValue(0).toString().equals("1"))
				{
					isSend = true;
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@流程设计错误:" + ex.getMessage() + " 在当前节点上个您设置了安上一步的表单字段决定是否回执，但是上一个节点表单中没有约定的字段。");
			}
		}

		if (nd.getReadReceipts() == ReadReceipts.BySDKPara)
		{
			/*如果是按开发者参数*/

			/*获取上一个节点ID*/
			Nodes fromNodes = nd.getFromNodes();
			int fromNodeID = 0;
			for (Node item : fromNodes)
			{
				ps = new Paras();
				ps.SQL = "SELECT FK_Node FROM WF_GenerWorkerlist  WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node ";
				ps.Add("WorkID", workid);
				ps.Add("FK_Node", item.getNodeID());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					continue;
				}

				fromNodeID = item.getNodeID();
				break;
			}
			if (fromNodeID == 0)
			{
				throw new RuntimeException("@没有找到它的上一步工作。");
			}

			String paras = BP.WF.Dev2Interface.GetFlowParas(fromNodeID, workid);
			if (DataType.IsNullOrEmpty(paras) || paras.contains("@" + BP.WF.WorkSysFieldAttr.SysIsReadReceipts + "=") == false)
			{
				throw new RuntimeException("@流程设计错误:在当前节点上个您设置了按开发者参数决定是否回执，但是没有找到该参数。");
			}

			// 开发者参数.
			if (paras.contains("@" + BP.WF.WorkSysFieldAttr.SysIsReadReceipts + "=1") == true)
			{
				isSend = true;
			}
		}


		if (isSend == true)
		{
			/*如果是自动的已读回执，就让它发送给当前节点的上一个发送人。*/

			// 获取流程标题.
			ps = new Paras();
			ps.SQL = "SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + dbstr + "WorkID ";
			ps.Add("WorkID", workid);
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			String title = dt.Rows.get(0).getValue(0).toString();

			// 获取流程的发送人.
			ps = new Paras();
			ps.SQL = "SELECT " + GenerWorkerListAttr.Sender + " FROM WF_GenerWorkerlist WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr + "FK_Node ";
			ps.Add("WorkID", workid);
			ps.Add("FK_Node", nodeID);
			dt = DBAccess.RunSQLReturnTable(ps);
			String sender = dt.Rows.get(0).getValue(0).toString();

			//发送已读回执。
			BP.WF.Dev2Interface.Port_SendMsg(sender, "已读回执:" + title, "您发送的工作已经被" + WebUser.getName() + "在" + DataType.getCurrentDataTime()CNOfShort + " 打开.", "RP" + workid + "_" + nodeID, BP.WF.SMSMsgType.Self, nd.getFK_Flow(), nd.getNodeID(), workid, 0);
		}

		//执行节点打开后事件.
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.RetrieveFromDBSources();
		nd.getHisFlow().DoFlowEventEntity(EventListOfNode.WhenReadWork, nd, wk, null);

	}
	/** 
	 设置工作未读取
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	 @param userNo 要设置的人
	*/
	public static void Node_SetWorkUnRead(long workid, String userNo)
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsRead=0 WHERE WorkID=" + dbstr + "WorkID AND FK_Emp=" + dbstr + "FK_Emp";
		ps.Add("WorkID", workid);
		ps.Add("FK_Emp", userNo);
		DBAccess.RunSQL(ps);
	}
	/** 
	 设置工作未读取
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	*/
	public static void Node_SetWorkUnRead(long workid)
	{
		Node_SetWorkUnRead(workid, WebUser.getNo());
	}

		///#endregion 工作有关接口


		///#region 流程属性与节点属性变更接口.
	/** 
	 更改流程属性
	 
	 @param fk_flow 流程编号
	 @param attr1 字段1
	 @param v1 值1
	 @param attr2 字段2(可为null)
	 @param v2 值2(可为null)
	 @return 执行结果
	*/
	public static String ChangeAttr_Flow(String fk_flow, String attr1, Object v1, String attr2, Object v2)
	{
		Flow fl = new Flow(fk_flow);
		if (attr1 != null)
		{
			fl.SetValByKey(attr1, v1);
		}

		if (attr2 != null)
		{
			fl.SetValByKey(attr2, v2);
		}

		fl.Update();
		return "修改成功";
	}

		///#endregion 流程属性与节点属性变更接口.


		///#region ccform 接口

	/** 
	  获得指定轨迹的json数据. 
	 
	 @param flowNo 流程编号
	 @param mypk 流程主键
	 @return 返回当时的表单json字符串
	*/
	public static String CCFrom_GetFrmDBJson(String flowNo, String mypk)
	{
		return DBAccess.GetBigTextFromDB("ND" + Integer.parseInt(flowNo) + "Track", "MyPK", mypk, "FrmDB");
	}
	/** 
	 SDK签章接口
	 
	 @param workid 工作ID
	 @param nodeid 签章节点ID
	 @param deptno 部门编号
	 @param stationno 岗位编号
	 @return 返回非null值时，为签章失败
	*/
	public static String CCForm_Seal(long workid, int nodeid, String deptno, String stationno)
	{
		try
		{
			FrmEleDBs eleDBs = new FrmEleDBs("ND" + nodeid, String.valueOf(workid));

			if (eleDBs.size() > 0)
			{
				eleDBs.Delete(FrmEleDBAttr.FK_MapData, "ND" + nodeid, FrmEleDBAttr.RefPKVal, workid);
			}

			String sealimg = BP.WF.Glo.getCCFlowAppPath() + "DataUser/Seal/" + deptno + "_" + stationno + ".jpg";

			if ((new File(SystemConfig.PathOfWebApp + sealimg)).isFile() == false)
			{
				return "签章文件：" + sealimg + "不存在，请联系管理员！";
			}

			FrmEleDB athDB_N = new FrmEleDB();
			athDB_N.FK_MapData = "ND" + nodeid;
			athDB_N.RefPKVal = String.valueOf(workid);
			athDB_N.EleID = String.valueOf(workid);
			athDB_N.GenerPKVal();
			athDB_N.Tag1 = sealimg;
			athDB_N.DirectInsert();

			return null;
		}
		catch (RuntimeException ex)
		{
			return "签章错误：" + ex.getMessage();
		}
	}
	/** 
	 增加附件
	 
	 @param nodeid 节点ID
	 @param workid 工作ID
	 @param athNo 附件编号，如果当前节点只有一个附件可以为空.
	 @param frmID 表单ID
	 @param filePath 文件路径:比如：c:\\xxx.xls
	 @param fileName 文件名称:比如：我的文档.xls
	 @return 执行结果
	*/
	public static String CCForm_AddAth(int nodeid, long workid, String athNo, String frmID, String filePath, String fileName)
	{
		return "增加成功";
	}

		///#endregion ccform 接口


		///#region 页面.
	/** 
	 附件上传接口
	 
	 @param nodeid 节点ID
	 @param flowid 流程ID
	 @param workid 工作ID
	 @param athNo 附件属性No
	 @param frmID FK_MapData
	 @param filePath 附件路径
	 @param fileName 附件名称
	 @param sort 分类
	 @return 
	*/

	public static String CCForm_AddAth(int nodeid, String flowid, long workid, String athNo, String frmID, String filePath, String fileName, String sort, int fid)
	{
		return CCForm_AddAth(nodeid, flowid, workid, athNo, frmID, filePath, fileName, sort, fid, 0);
	}

	public static String CCForm_AddAth(int nodeid, String flowid, long workid, String athNo, String frmID, String filePath, String fileName, String sort)
	{
		return CCForm_AddAth(nodeid, flowid, workid, athNo, frmID, filePath, fileName, sort, 0, 0);
	}

	public static String CCForm_AddAth(int nodeid, String flowid, long workid, String athNo, String frmID, String filePath, String fileName)
	{
		return CCForm_AddAth(nodeid, flowid, workid, athNo, frmID, filePath, fileName, null, 0, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string CCForm_AddAth(int nodeid, string flowid, Int64 workid, String athNo, string frmID, string filePath, string fileName, string sort = null, Int32 fid = 0, Int32 pworkid = 0)
	public static String CCForm_AddAth(int nodeid, String flowid, long workid, String athNo, String frmID, String filePath, String fileName, String sort, int fid, int pworkid)
	{
		String pkVal = String.valueOf(workid);
		// 多附件描述.
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment(athNo);
		MapData mapData = new MapData(frmID);
		String msg = null;


			///#region 获取表单方案
		//求主键. 如果该表单挂接到流程上.
		if (nodeid != 0)
		{
			//判断表单方案。
			FrmNode fn = new FrmNode(flowid, nodeid, frmID);
			if (fn.getFrmSln() == FrmSln.Readonly)
			{
				return "err@不允许上传附件.";
			}

			//是默认的方案的时候.
			if (fn.getFrmSln() == FrmSln.Default)
			{
				//判断当前方案设置的whoIsPk ，让附件集成 whoIsPK 的设置。
				if (fn.getWhoIsPK() == WhoIsPK.FID)
				{
					pkVal = String.valueOf(fid);
				}

				if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
				{
					pkVal = String.valueOf(pworkid);
				}
			}

			//自定义方案.
			if (fn.getFrmSln() == FrmSln.Self)
			{
				athDesc = new FrmAttachment(athNo + "_" + nodeid);
				if (athDesc.HisCtrlWay == AthCtrlWay.FID)
				{
					pkVal = String.valueOf(fid);
				}

				if (athDesc.HisCtrlWay == AthCtrlWay.PWorkID)
				{
					pkVal = String.valueOf(pworkid);
				}
			}
		}


			///#endregion 获取表单方案

		//获取上传文件是否需要加密
		boolean fileEncrypt = SystemConfig.IsEnableAthEncrypt;


			///#region 文件上传的iis服务器上 or db数据库里.
		if (athDesc.AthSaveWay == AthSaveWay.IISServer)
		{
			String savePath = athDesc.SaveTo;
			if (savePath.contains("@") == true || savePath.contains("*") == true)
			{
				/*如果有变量*/
				savePath = savePath.replace("*", "@");

				if (savePath.contains("@") && nodeid != 0)
				{
					/*如果包含 @ */
					BP.WF.Flow flow = new BP.WF.Flow(flowid);
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(workid);
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true)
				{
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
				}
			}
			else
			{
				savePath = athDesc.SaveTo + "\\" + pkVal;
			}

			//替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");

			try
			{
				savePath = SystemConfig.PathOfWebApp + savePath;
			}
			catch (RuntimeException ex)
			{
				savePath = SystemConfig.PathOfDataUser + "UploadFile\\" + mapData.No + "\\";
				//return "err@获取路径错误" + ex.Message + ",配置的路径是:" + savePath + ",您需要在附件属性上修改该附件的存储路径.";
			}

			try
			{
				if ((new File(savePath)).isDirectory() == false)
				{
					(new File(savePath)).mkdirs();
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("err@创建路径出现错误，可能是没有权限或者路径配置有问题:" + savePath + "@异常信息:" + ex.getMessage());
			}

			String guid = BP.DA.DBAccess.GenerGUID();
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String realSaveTo = savePath + "\\" + guid + "." + fileName;
			realSaveTo = realSaveTo.replace("~", "-");
			realSaveTo = realSaveTo.replace("'", "-");
			realSaveTo = realSaveTo.replace("*", "-");
			if (fileEncrypt == true)
			{
				String strtmp = realSaveTo + ".tmp";
				if ((new File(filePath)).isFile() == true)
				{
					Files.copy(Paths.get(filePath), Paths.get(strtmp), StandardCopyOption.COPY_ATTRIBUTES); //先明文保存到本地(加个后缀名.tmp)
				}
				else
				{
					return "err@需要保存的文件不存在";
				}

				EncHelper.EncryptDES(strtmp, strtmp.replace(".tmp", "")); //加密
				(new File(strtmp)).delete(); //删除临时文件
			}
			else
			{
				//文件保存的路径
				if ((new File(filePath)).isFile() == true)
				{
					Files.copy(Paths.get(filePath), Paths.get(realSaveTo), StandardCopyOption.COPY_ATTRIBUTES);
				}
				else
				{
					return "err@需要保存的文件不存在";
				}
			}

			File info = new File(realSaveTo);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK( guid; // athDesc.FK_MapData + oid.ToString();
			dbUpload.NodeID = String.valueOf(nodeid);
			dbUpload.Sort = sort;
			dbUpload.FK_FrmAttachment = athNo;
			dbUpload.FK_MapData = athDesc.FK_MapData;
			dbUpload.FileExts = ext;
			dbUpload.FID = fid;
			if (fileEncrypt == true)
			{
				dbUpload.SetPara("IsEncrypt", 1);
			}


				///#region 处理文件路径，如果是保存到数据库，就存储pk.
			if (athDesc.AthSaveWay == AthSaveWay.IISServer)
			{
				//文件方式保存
				dbUpload.FileFullName = realSaveTo;
			}

			if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
			{
				//保存到数据库
				dbUpload.FileFullName = dbUpload.MyPK;
			}

				///#endregion 处理文件路径，如果是保存到数据库，就存储pk.

			dbUpload.FileName = fileName;
			dbUpload.FileSize = (float)info.length();
			dbUpload.RDT = DataType.getCurrentDataTime()ss;
			dbUpload.Rec = WebUser.getNo();
			dbUpload.RecName = WebUser.getName();
			dbUpload.RefPKVal = pkVal;

			dbUpload.UploadGUID = guid;
			dbUpload.Insert();

			if (athDesc.AthSaveWay == AthSaveWay.DB)
			{
				//执行文件保存.
				BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.EnMap.PhysicsTable, "MyPK", dbUpload.MyPK, "FDB");
			}
		}

			///#endregion 文件上传的iis服务器上 or db数据库里.


			///#region 保存到数据库 / FTP服务器上.
		if (athDesc.AthSaveWay == AthSaveWay.DB || athDesc.AthSaveWay == AthSaveWay.FTPServer)
		{
			String guid = DBAccess.GenerGUID();

			//把文件临时保存到一个位置.
			String temp = SystemConfig.PathOfTemp + guid + ".tmp";

			if (fileEncrypt == true)
			{

				String strtmp = SystemConfig.PathOfTemp + guid + "_Desc" + ".tmp";
				if ((new File(filePath)).isFile() == true)
				{
					Files.copy(Paths.get(filePath), Paths.get(strtmp), StandardCopyOption.COPY_ATTRIBUTES); //先明文保存到本地(加个后缀名.tmp)
				}
				else
				{
					return "err@需要保存的文件不存在";
				}

				EncHelper.EncryptDES(strtmp, temp); //加密
				(new File(strtmp)).delete(); //删除临时文件
			}
			else
			{
				//文件保存的路径
				if ((new File(filePath)).isFile() == true)
				{
					Files.copy(Paths.get(filePath), Paths.get(temp), StandardCopyOption.COPY_ATTRIBUTES);
				}
				else
				{
					return "err@需要保存的文件不存在";
				}
			}
			String ext = fileName.substring(fileName.lastIndexOf("."));

			File info = new File(temp);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK( BP.DA.DBAccess.GenerGUID();
			dbUpload.Sort = sort;
			dbUpload.NodeID = String.valueOf(nodeid);
			dbUpload.FK_FrmAttachment = athDesc.MyPK;
			dbUpload.FileExts = ext;
			dbUpload.FID = fid; //流程id.
			if (fileEncrypt == true)
			{
				dbUpload.SetPara("IsEncrypt", 1);
			}

			if (athDesc.AthUploadWay == AthUploadWay.Inherit)
			{
				/*如果是继承，就让他保持本地的PK. */
				dbUpload.RefPKVal = pkVal.toString();
			}

			if (athDesc.AthUploadWay == AthUploadWay.Interwork)
			{
				/*如果是协同，就让他是PWorkID. */
				Paras ps = new Paras();
				ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("WorkID", pkVal);
				String pWorkID = BP.DA.DBAccess.RunSQLReturnValInt(ps, 0).toString();
				if (pWorkID == null || pWorkID.equals("0"))
				{
					pWorkID = pkVal;
				}

				dbUpload.RefPKVal = pWorkID;
			}

			dbUpload.FK_MapData = athDesc.FK_MapData;
			dbUpload.FK_FrmAttachment = athDesc.MyPK;
			dbUpload.FileName = fileName;
			dbUpload.FileSize = (float)info.length();
			dbUpload.RDT = DataType.getCurrentDataTime()ss;
			dbUpload.Rec = WebUser.getNo();
			dbUpload.RecName = WebUser.getName();

			dbUpload.UploadGUID = guid;

			if (athDesc.AthSaveWay == AthSaveWay.DB)
			{
				dbUpload.Insert();
				//把文件保存到指定的字段里.
				dbUpload.SaveFileToDB("FileDB", temp);
			}

			if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
			{
				/*保存到fpt服务器上.*/
				FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

				String ny = LocalDateTime.now().toString("yyyy_MM");

				//判断目录年月是否存在.
				if (ftpconn.DirectoryExist(ny) == false)
				{
					ftpconn.CreateDirectory(ny);
				}

				ftpconn.SetCurrentDirectory(ny);

				//判断目录是否存在.
				if (ftpconn.DirectoryExist(athDesc.FK_MapData) == false)
				{
					ftpconn.CreateDirectory(athDesc.FK_MapData);
				}

				//设置当前目录，为操作的目录。
				ftpconn.SetCurrentDirectory(athDesc.FK_MapData);

				//把文件放上去.
				ftpconn.PutFile(temp, guid + "." + dbUpload.FileExts);
				ftpconn.Close();

				//设置路径.
				dbUpload.FileFullName = ny + "//" + athDesc.FK_MapData + "//" + guid + "." + dbUpload.FileExts;
				dbUpload.Insert();
				(new File(temp)).delete();
			}

		}

			///#endregion 保存到数据库.

		return "";
	}
	/** 
	 sdk表单加载初始化信息
	 
	 @param workid 工作ID
	 @return 请参考相关的文档,或者baidu ccbpm sdk表单 SDK_Page_Init
	*/
	public static String SDK_Page_Init(long workid)
	{
		return BP.WF.AppClass.SDK_Page_Init(workid);
	}

		///#endregion 页面.


		///#region 与工作处理器相关的接口
	/** 
	 获得一个节点要转向的节点
	 
	 @param flowNo 流程编号
	 @param ndFrom 节点从
	 @param workid 工作ID
	 @return 返回可以到达的节点
	*/
	public static Nodes WorkOpt_GetToNodes(String flowNo, int ndFrom, long workid, long FID)
	{
		Nodes nds = new Nodes();

		Node nd = new Node(ndFrom);
		Nodes toNDs = nd.getHisToNodes();

		Flow fl = nd.getHisFlow();
		GERpt rpt = fl.getHisGERpt();
		rpt.setOID(FID == 0 ? workid : FID);
		rpt.Retrieve();

		//首先输出普通的节点 
		for (Node mynd : toNDs)
		{
			boolean bIsCanDo = true;
			if (mynd.getHisRunModel() == RunModel.SubThread)
			{
				continue; //如果是子线程节点.
			}


				///#region 判断方向条件,如果设置了方向条件，判断是否可以通过，不能通过的，就不让其显示.
			Conds conds = new Conds();
			int i = conds.Retrieve(CondAttr.FK_Node, nd.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
			// 设置方向条件，就判断它。
			if (i > 0)
			{
				for (Cond cond : conds)
				{
					cond.setWorkID(workid);
					cond.en = rpt;
					//有一个条件成立则成立
					if (cond.getCondOrAnd() == CondOrAnd.ByOr)
					{
						bIsCanDo = false;
						if (cond.getIsPassed() == true)
						{
							bIsCanDo = true;
							break;
						}
					}

					//有一个条件不成立则不成立
					if (cond.getCondOrAnd() == CondOrAnd.ByAnd && cond.getIsPassed() == false)
					{
						bIsCanDo = false;
						break;
					}
				}
			}
			//条件不符合则不通过
			if (bIsCanDo == false)
			{
				continue;
			}


				///#endregion

			nds.AddEntity(mynd);
		}

		//同表单子线程.
		for (Node mynd : toNDs)
		{
			if (mynd.getHisRunModel() != RunModel.SubThread)
			{
				continue; //如果是子线程节点.
			}

			if (mynd.getHisSubThreadType() == SubThreadType.UnSameSheet)
			{
				continue; //如果是异表单的分合流.
			}


				///#region 判断方向条件,如果设置了方向条件，判断是否可以通过，不能通过的，就不让其显示.
			Cond cond = new Cond();
			int i = cond.Retrieve(CondAttr.FK_Node, nd.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
			// 设置方向条件，就判断它。
			if (i > 0)
			{
				cond.setWorkID(workid);
				cond.en = rpt;
				if (cond.getIsPassed() == false)
				{
					continue;
				}
			}

				///#endregion


			nds.AddEntity(mynd);
		}

		// 检查是否具有异表单的子线程.
		boolean isHave = false;
		for (Node mynd : toNDs)
		{
			if (mynd.getHisSubThreadType() == SubThreadType.UnSameSheet)
			{
				isHave = true;
			}
		}

		if (isHave)
		{
			Node myn1d = new Node();
			myn1d.setNodeID(0);
			myn1d.setName("可以分发启动的节点");
			nds.AddEntity(myn1d);

			/*增加异表单的子线程*/
			for (Node mynd : toNDs)
			{
				if (mynd.getHisSubThreadType() != SubThreadType.UnSameSheet)
				{
					continue;
				}


					///#region 判断方向条件,如果设置了方向条件，判断是否可以通过，不能通过的，就不让其显示.
				Cond cond = new Cond();
				int i = cond.Retrieve(CondAttr.FK_Node, nd.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
				// 设置方向条件，就判断它。
				if (i > 0)
				{
					cond.setWorkID(workid);
					cond.en = rpt;
					if (cond.getIsPassed() == false)
					{
						continue;
					}
				}

					///#endregion

				nds.AddEntity(mynd);
			}
		}
		//返回它.
		return nds;
	}
	/** 
	 在节点选择转向功能界面，获得当前人员上一次选择的节点，在界面里让其自动选择。
	 以改善用户操作体验，就类似于默认记忆上一次的操作功能。
	 
	 @param flowNo 流程编号
	 @param nodeID 当前节点编号
	 @return 返回上一次当前用户选择的节点,如果没有找到（当前用户第一次发送的情况下找不到）就返回0.
	*/
	public static int WorkOpt_ToNodes_GetLasterSelectNodeID(String flowNo, int nodeID)
	{
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case DBType.MSSQL:
			case DBType.Access:
				sql = "SELECT TOP 1 NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='" + WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue() + ")  ORDER BY RDT DESC";
				break;
			case DBType.Oracle:
				sql = "SELECT NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  RowNum=1 AND EmpFrom='" + WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue() + ")  ORDER BY RDT DESC";
				break;
			case DBType.MySQL:
				sql = "SELECT NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='" + WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue() + ") limit 0,1";
				break;
			case DBType.Informix:
				sql = "SELECT first 1 NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='" + WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue() + ")  ORDER BY RDT DESC";
				break;
			case DBType.PostgreSQL:
				sql = "SELECT NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='" + WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue() + ") ORDER BY RDT DESC limit 1";
				break;
			default:
				throw new RuntimeException("@没有实现该类型的数据库支持.");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
	}
	/** 
	 发送到节点
	 
	 @param flowNo
	 @param node
	 @param workid
	 @param fid
	 @param toNodes
	*/
	public static SendReturnObjs WorkOpt_SendToNodes(String flowNo, int nodeID, long workid, long fid, String toNodes)
	{
		//把参数更新到数据库里面.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		gwf.RetrieveFromDBSources();
		gwf.setParas_ToNodes(toNodes);
		gwf.Save();

		Node nd = new Node(nodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		// 以下代码是从 MyFlow.htm Send 方法copy 过来的，需要保持业务逻辑的一致性，所以代码需要保持一致.
		WorkNode firstwn = new WorkNode(wk, nd);
		String msg = "";
		SendReturnObjs objs = firstwn.NodeSend();
		return objs;
	}
	/** 
	 获得接收人的数据源
	 
	 @param nodeID 指定节点
	 @param WorkID 工作ID
	 @param FID 流程ID
	 @return 
	*/

	public static DataSet WorkOpt_AccepterDB(int nodeID, long WorkID)
	{
		return WorkOpt_AccepterDB(nodeID, WorkID, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataSet WorkOpt_AccepterDB(int nodeID, Int64 WorkID, Int64 FID = 0)
	public static DataSet WorkOpt_AccepterDB(int nodeID, long WorkID, long FID)
	{
		DataSet ds = new DataSet();

		Selector en = new Selector(nodeID);
		switch (en.getSelectorModel())
		{
			case Station:
				DataTable dt = WorkOpt_Accepter_ByStation(nodeID);
				dt.TableName = "Port_Emp";
				ds.Tables.add(dt);
				break;
			case SQL:
				ds = WorkOpt_Accepter_BySQL(nodeID);
				break;
			case Dept:
				ds = WorkOpt_Accepter_ByDept(nodeID);
				break;
			case Emp:
				ds = WorkOpt_Accepter_ByEmp(nodeID);
				break;
			case Url:
			default:
				break;
		}
		return ds;
	}
	/** 
	 获取节点绑定岗位人员
	 
	 @param nodeID 指定的节点
	 @return 
	*/
	private static DataTable WorkOpt_Accepter_ByStation(int nodeID)
	{
		if (nodeID == 0)
		{
			throw new RuntimeException("@流程设计错误，没有转向的节点。举例说明: 当前是A节点。如果您在A点的属性里启用了[接受人]按钮，那么他的转向节点集合中(就是A可以转到的节点集合比如:A到B，A到C, 那么B,C节点就是转向节点集合)，必须有一个节点是的节点属性的[访问规则]设置为[由上一步发送人员选择]");
		}

		NodeStations stas = new NodeStations(nodeID);
		if (stas.size() == 0)
		{
			BP.WF.Node toNd = new BP.WF.Node(nodeID);
			throw new RuntimeException("@流程设计错误：设计员没有设计节点[" + toNd.getName() + "]，接受人的岗位范围。");
		}
		// 优先解决本部门的问题。
		String sql = "";
		if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
		{
			sql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
			sql += "SELECT FK_EMP FROM Port_DeptEmpStation WHERE FK_STATION ";
			sql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") ";
			sql += ") AND a.No IN (SELECT No FROM Port_Emp WHERE FK_Dept ='" + WebUser.getFK_Dept() + "')";
			sql += " ORDER BY B.Idx,B.No,A.Idx,A.No ";
		}
		else
		{
			sql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
			sql += "SELECT FK_EMP FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_STATION ";
			sql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") ";
			sql += ") AND a.No IN (SELECT No FROM Port_Emp WHERE FK_Dept ='" + WebUser.getFK_Dept() + "')";
			sql += " ORDER BY A.FK_DEPT,A.No ";
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			return dt;
		}

		//组织结构中所有岗位人员
		sql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
		sql += "SELECT FK_EMP FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_STATION ";
		sql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") ";
		sql += ") ORDER BY A.FK_DEPT,A.No ";
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}
	/** 
	 按sql方式
	*/
	private static DataSet WorkOpt_Accepter_BySQL(int nodeID)
	{
		DataSet ds = new DataSet();
		Selector MySelector = new Selector(nodeID);
		String sqlGroup = MySelector.getSelectorP1();
		sqlGroup = sqlGroup.replace("@WebUser.getNo()", WebUser.getNo());
		sqlGroup = sqlGroup.replace("@WebUser.getName()", WebUser.getName());
		sqlGroup = sqlGroup.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		String sqlDB = MySelector.getSelectorP2();
		sqlDB = sqlDB.replace("@WebUser.getNo()", WebUser.getNo());
		sqlDB = sqlDB.replace("@WebUser.getName()", WebUser.getName());
		sqlDB = sqlDB.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		DataTable dtGroup = DBAccess.RunSQLReturnTable(sqlGroup);
		dtGroup.TableName = "Port_Dept";
		ds.Tables.add(dtGroup);
		DataTable dtDB = DBAccess.RunSQLReturnTable(sqlDB);
		dtDB.TableName = "Port_Emp";
		ds.Tables.add(dtDB);

		return ds;
	}

	/** 
	 获取接收人选择器，按部门绑定
	 
	 @param ToNode
	 @return 
	*/
	private static DataSet WorkOpt_Accepter_ByDept(int nodeID)
	{
		DataSet ds = new DataSet();
		String orderByIdx = BP.WF.Glo.getOSModel() == OSModel.OneMore ? "Idx," : "";
		String sqlGroup = "SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node='" + nodeID + "') ORDER BY " + orderByIdx + "No";
		String sqlDB = "SELECT No,Name, FK_Dept FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node='" + nodeID + "') ORDER BY " + orderByIdx + "No";

		DataTable dtGroup = DBAccess.RunSQLReturnTable(sqlGroup);
		dtGroup.TableName = "Port_Dept";
		ds.Tables.add(dtGroup);

		DataTable dtDB = DBAccess.RunSQLReturnTable(sqlDB);
		dtDB.TableName = "Port_Emp";
		ds.Tables.add(dtDB);

		return ds;
	}

	/** 
	 按BindByEmp 方式
	*/
	private static DataSet WorkOpt_Accepter_ByEmp(int nodeID)
	{
		String orderByIdx = BP.WF.Glo.getOSModel() == OSModel.OneMore ? "Idx," : "";
		String sqlGroup = "SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM Port_Emp WHERE No in(SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node='" + nodeID + "')) ORDER BY " + orderByIdx + "No";
		String sqlDB = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE No in (SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node='" + nodeID + "') ORDER BY " + orderByIdx + "No";

		DataSet ds = new DataSet();
		DataTable dtGroup = DBAccess.RunSQLReturnTable(sqlGroup);
		dtGroup.TableName = "Port_Dept";
		ds.Tables.add(dtGroup);

		DataTable dtDB = DBAccess.RunSQLReturnTable(sqlDB);
		dtDB.TableName = "Port_Emp";
		ds.Tables.add(dtDB);
		return ds;
	}

	/** 
	 设置指定的节点接受人
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	 @param fid 流程ID
	 @param emps 指定的人员集合zhangsan,lisi,wangwu
	 @param isNextTime 是否下次自动设置
	*/
	public static void WorkOpt_SetAccepter(int nodeID, long workid, long fid, String emps, boolean isNextTime)
	{
		SelectAccpers ens = new SelectAccpers();
		ens.Delete(SelectAccperAttr.FK_Node, nodeID, SelectAccperAttr.WorkID, workid);

		//下次是否记忆选择，清空掉。
		String sql = "UPDATE WF_SelectAccper SET " + SelectAccperAttr.IsRemember + " = 0 WHERE Rec='" + WebUser.getNo() + "' AND IsRemember=1 AND FK_Node=" + nodeID;
		BP.DA.DBAccess.RunSQL(sql);

		//开始执行保存.
		String[] strs = emps.split("[,]", -1);
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str) == true)
			{
				continue;
			}

			SelectAccper en = new SelectAccper();
			en.setMyPK( nodeID + "_" + workid + "_" + str;
			en.setFK_Emp(str);
			en.setFK_Node(nodeID);

			en.setWorkID(workid);
			en.setRec(WebUser.getNo());
			en.setIsRemember(isNextTime);
			en.Insert();
		}
	}
	/** 
	 发送到节点
	 
	 @param flowNo
	 @param node
	 @param workid
	 @param fid
	 @param toNodes
	*/
	public static SendReturnObjs WorkOpt_SendToEmps(String flowNo, int nodeID, long workid, long fid, int toNodeID, String toEmps, boolean isRememberMe)
	{
		WorkOpt_SetAccepter(toNodeID, workid, fid, toEmps, isRememberMe);

		Node nd = new Node(nodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		// 以下代码是从 MyFlow.htm Send 方法copy 过来的，需要保持业务逻辑的一致性，所以代码需要保持一致.
		WorkNode firstwn = new WorkNode(wk, nd);
		String msg = "";
		SendReturnObjs objs = firstwn.NodeSend();
		return objs;
	}

		///#endregion


		///#region 附件上传
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static string SaveImageAsFile(byte[] img, string pkval, string fk_Frm_Ele)
	public static String SaveImageAsFile(byte[] img, String pkval, String fk_Frm_Ele)
	{
		FrmEle fe = new FrmEle(fk_Frm_Ele);
		System.Drawing.Image newImage;
		try (MemoryStream ms = new MemoryStream(img, 0, img.length))
		{
			ms.Write(img, 0, img.length);
			newImage = Image.FromStream(ms, true);
			Bitmap bitmap = new Bitmap(newImage, new Size(fe.WOfInt, fe.HOfInt));

			if ((new File(fe.HandSigantureSavePath + "\\" + fe.FK_MapData + "\\")).isDirectory() == false)
			{
				(new File(fe.HandSigantureSavePath + "\\" + fe.FK_MapData + "\\")).mkdirs();
			}

			String saveTo = fe.HandSigantureSavePath + "\\" + fe.FK_MapData + "\\" + pkval + ".jpg";
			bitmap.Save(saveTo, ImageFormat.Jpeg);

			String pathFile = HttpContextHelper.RequestApplicationPath + fe.HandSiganture_UrlPath + fe.FK_MapData + "/" + pkval + ".jpg";
			FrmEleDB ele = new FrmEleDB();
			ele.FK_MapData = fe.FK_MapData;
			ele.EleID = fe.EleID;
			ele.RefPKVal = pkval;
			ele.Tag1 = pathFile.replace("\\\\", "\\");
			ele.Tag1 = pathFile.replace("////", "//");

			ele.Tag2 = saveTo.replace("\\\\", "\\");
			ele.Tag2 = saveTo.replace("////", "//");

			ele.GenerPKVal();
			ele.Save();

			return pathFile;
		}
	}

	/** 
	 上传文件.
	 
	 @param FileByte
	 @param fileName
	 @return 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public static string UploadFile(byte[] FileByte, String fileName)
	public static String UploadFile(byte[] FileByte, String fileName)
	{
		String path = HttpContextHelper.RequestApplicationPath + "\\DataUser\\UploadFile";
		if (!(new File(path)).isDirectory())
		{
			(new File(path)).mkdirs();
		}

		String filePath = path + "\\" + fileName;
		if ((new File(filePath)).isFile())
		{
			(new File(filePath)).delete();
		}

		//这里使用绝对路径来索引
		FileOutputStream stream = new FileOutputStream(filePath);
		stream.write(FileByte, 0, FileByte.length);
		stream.close();

		return filePath;
	}


		///#endregion


		///#region 调度相关的操作.
	/** 
	 更新时间状态, 交付给 huangzhimin.
	 作用：按照当前的时间，每天两次更新WF_GenerWorkFlow 的 TodoSta 状态字段。
	 该字段： 0=正常(绿牌), 1=预警(黄牌), 2=逾期(红牌), 3=按时完成(绿牌) , 4=逾期完成(红牌).
	 该方法作用是，每天，中午时间段，与下午时间段，执行更新这两个状态，仅仅更新两次。
	*/
	public static void DTS_GenerWorkFlowTodoSta()
	{
		// 中午的更新, 与发送邮件通知.
		boolean isPM = false;


			///#region 求出是否可以更新状态.
		if (LocalDateTime.now().getHour() >= 9 && LocalDateTime.now().getHour() < 12)
		{
			isPM = true;
			String timeKey = "DTSTodoStaPM" + LocalDateTime.now().toString("yyMMdd");
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time == null)
			{
				GloVar var = new GloVar();
				var.No = timeKey;
				var.Name = "时效调度 WFTodoSta PM 调度";
				var.GroupKey = "WF";
				var.Val = timeKey; //更新时间点.
				var.Note = "时效调度PM" + timeKey;
				var.Insert();
				time = var.Val;
			}
			else
			{
				/*如果有数据，就返回，说明已经执行过了。*/
				return;
			}
		}

		//下午时间段.
		if (LocalDateTime.now().getHour() >= 13 && LocalDateTime.now().getHour() < 18)
		{
			String timeKey = "DTSTodoStaAM" + LocalDateTime.now().toString("yyMMdd");
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time == null)
			{
				GloVar var = new GloVar();
				var.No = timeKey;
				var.Name = "时效调度 WFTodoSta AM 调度";
				var.GroupKey = "WF";
				var.Val = timeKey; //更新时间点.
				var.Note = "时效调度AM" + timeKey;
				var.Insert();
				time = var.Val;
			}
			else
			{
				/*如果有数据，就返回，说明已经执行过了。*/
				return;
			}
		}

			///#endregion 求出是否可以更新状态.


		BP.WF.DTS.DTS_GenerWorkFlowTodoSta en = new BP.WF.DTS.DTS_GenerWorkFlowTodoSta();
		en.Do();

	}
	/** 
	 预警与逾期的提醒.
	*/
	private static void DTS_SendMsgToWorker()
	{

			///#region 处理预警的消息发送.
		if (LocalDateTime.now().getHour() >= 0 && LocalDateTime.now().getHour() < 12)
		{
			String timeKey = "DTSWarningPM" + LocalDateTime.now().toString("yyMMdd");
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time != null)
			{
				return;
			}

			BP.WF.DTS.DTS_SendMsgToWarningWorker en = new BP.WF.DTS.DTS_SendMsgToWarningWorker();
			en.Do();

		}

			///#endregion
	}
	/** 
	 生成工作的 TimeSpan
	*/
	public static void DTS_GenerWorkFlowTimeSpan()
	{
		if (LocalDateTime.now().getHour() >= 8 && LocalDateTime.now().getHour() < 10 && LocalDateTime.Today.getDayOfWeek() == DayOfWeek.MONDAY)
		{
			String timeKey = "DTSTimeSpanPM" + LocalDateTime.now().toString("yyMMdd");
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time == null)
			{
				GloVar var = new GloVar();
				var.No = timeKey;
				var.Name = "设置时间段" + timeKey + "一周执行一次.";
				var.GroupKey = "WF";
				var.Val = timeKey; //更新时间点.
				var.Note = "设置时间段PM" + timeKey;
				var.Insert();
			}
			else
			{
				return;
			}
		}

		//执行调度.
		BP.WF.DTS.DTS_GenerWorkFlowTimeSpan ts = new BP.WF.DTS.DTS_GenerWorkFlowTimeSpan();
		ts.Do();
	}

		///#endregion
}