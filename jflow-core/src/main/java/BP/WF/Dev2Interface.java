package BP.WF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import org.apache.commons.lang.StringUtils;

import com.sun.star.util.DateTime;

import BP.DA.*;
import BP.Port.*;
import BP.Web.*;
import BP.En.*;
import BP.WF.Port.WFEmp;
import BP.WF.Port.WFEmpAttr;
import BP.WF.Port.WFEmps;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.WF.Entity.FrmWorkCheck;
import BP.Sys.*;
import BP.Tools.AesEncodeUtil;
import BP.Tools.ContextHolderUtils;
import BP.Tools.DateUtils;
import BP.Tools.FtpUtil;
import BP.Tools.SftpUtil;
import BP.Tools.StringHelper;

/**
 * 此接口为程序员二次开发使用,在阅读代码前请注意如下事项. 1, CCFlow的对外的接口都是以静态方法来实现的. 2, 以 DB_
 * 开头的是需要返回结果集合的接口. 3, 以 Flow_ 是流程接口. 4, 以 Node_ 是节点接口. 5, 以 Port_ 是组织架构接口. 6, 以
 * DTS_ 是调度. data tranr system. 7, 以 UI_ 是流程的功能窗口 8, 以 WorkOpt_ 用工作处理器相关的接口。
 * 
 */
public class Dev2Interface {
	/**
	 * 写入消息 用途可以处理提醒.
	 * 
	 * @param sendToUserNo
	 *            发送给的操作员ID
	 * @param sendDT
	 *            发送时间，如果null 则表示立刻发送。
	 * @param title
	 *            标题
	 * @param doc
	 *            内容
	 * @param msgFlag
	 *            消息标记
	 * @return 写入成功或者失败.
	 * @throws Exception
	 */
	public static void Port_SendSMS(String tel, String smsDoc, String msgType, String msgGroupFlag, String sender,
			String msgPK, String sendEmpNo) throws Exception {
		Port_SendSMS(tel, smsDoc, msgType, msgGroupFlag, sender, msgPK, sendEmpNo, null, null);
	}

	public static void Port_SendSMS(String tel, String smsDoc, String msgType, String msgGroupFlag, String sender,
			String msgPK) throws Exception {
		Port_SendSMS(tel, smsDoc, msgType, msgGroupFlag, sender, msgPK, null, null, null);
	}

	public static void Port_SendSMS(String tel, String smsDoc, String msgType, String msgGroupFlag, String sender)
			throws Exception {
		Port_SendSMS(tel, smsDoc, msgType, msgGroupFlag, sender, null, null, null, null);
	}

	public static void Port_SendSMS(String tel, String smsDoc, String msgType, String msgGroupFlag) throws Exception {
		Port_SendSMS(tel, smsDoc, msgType, msgGroupFlag, null, null, null, null, null);
	}

	public static boolean WriteToSMS(String sendToUserNo, String sendDT, String title, String doc, String msgFlag)
			throws Exception {
		SMS.SendMsg(sendToUserNo, title, doc, msgFlag, "Info", "");
		return true;
	}

	/**
	 * 待办工作数量
	 * 
	 * @throws Exception
	 * 
	 */
	public static int getTodolist_EmpWorks() throws Exception {
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue()
				+ " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
		String sql;

		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND TaskSta!=1 ";
			} else {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp ";
			}

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			// throw new Exception(ps.SQL);

			// BP.DA.Log.DebugWriteInfo(ps.SQL);
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND TaskSta!=1  ";
			} else {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp ";
			}
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case SpecFlows:
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0   ";
			} else {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows();
			}

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case None:
			// 不是授权状态
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND TaskSta!=1 ";
			} else {
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp ";
			}

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		default:
			throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/**
	 * 抄送数量
	 * 
	 * @throws Exception
	 */
	public static int getTodolist_CCWorks() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT count(MyPK) as Num FROM WF_CCList WHERE CCTo=" + SystemConfig.getAppCenterDBVarStr()
				+ "FK_Emp AND Sta=0";
		ps.Add("FK_Emp", BP.Web.WebUser.getNo());
		return DBAccess.RunSQLReturnValInt(ps, 0);
	}

	/**
	 * 获取抄送人员的
	 * 
	 * @param node
	 * @return
	 */
	/*
	 * public static String GetNode_CCList(WorkNode wn) { String ccers = null;
	 * 
	 * if (wn.getHisNode().getHisCCRole() == CCRole.AutoCC ||
	 * wn.getHisNode().getHisCCRole() == CCRole.HandAndAuto) { try { //如果是自动抄送
	 * CC cc = wn.getHisNode().getHisCC();
	 * 
	 * DataTable table = cc.GenerCCers(wn.rptGe); if (table.Rows.size() > 0) {
	 * String ccMsg = "@消息自动抄送给";
	 * 
	 * for (DataRow dr : table.Rows) { ccers += dr.getValue(0) + ","; } } }
	 * catch (RuntimeException ex) { throw new RuntimeException("@处理操送时出现错误:" +
	 * ex.getMessage()); } }
	 * 
	 * 
	 * ///#region 执行抄送 BySysCCEmps if (wn.getHisNode().getHisCCRole() ==
	 * CCRole.BySysCCEmps) { CC cc = wn.getHisNode().getHisCC();
	 * 
	 * //取出抄送人列表 String temps = wn.rptGe.GetValStrByKey("SysCCEmps"); if
	 * (!StringHelper.isNullOrEmpty(temps)) { String[] cclist =
	 * temps.split("[|]", -1); java.util.Hashtable ht = new
	 * java.util.Hashtable(); for (String item : cclist) { String[] tmp =
	 * item.split("[,]", -1); ccers += tmp[0] + ","; } } } return ccers; }
	 */
	/**
	 * 返回挂起流程数量
	 * 
	 * @throws Exception
	 */
	public static int getTodolist_HungUpNum() throws Exception {
		String sql = "select  COUNT(WorkID) AS Num from WF_GenerWorkFlow where WFState=4 and  WorkID in (SELECT distinct WorkID FROM WF_HungUp WHERE Rec='"
				+ BP.Web.WebUser.getNo() + "')";
		return BP.DA.DBAccess.RunSQLReturnValInt(sql);
	}

	/**
	 * 在途的工作数量
	 * 
	 * @throws Exception
	 */
	public static int getTodolist_Runing() throws Exception {
		String sql;
		int state = WFState.Runing.getValue();
		if (WebUser.getIsAuthorize()) {
			// 如果是授权状态.
			WFEmp emp = new WFEmp(WebUser.getNo());
			sql = "SELECT count( distinct a.WorkID ) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='"
					+ WebUser.getNo()
					+ "' AND A.WFState!=3 AND B.IsEnable=1 AND (B.IsPass=1 OR B.IsPass<0) AND A.FK_Flow IN "
					+ emp.getAuthorFlows();
			return BP.DA.DBAccess.RunSQLReturnValInt(sql);
		} else {
			Paras ps = new Paras();
			ps.SQL = "SELECT count( distinct a.WorkID ) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
					+ SystemConfig.getAppCenterDBVarStr()
					+ "FK_Emp AND B.IsEnable=1 AND A.WFState!=3 AND (B.IsPass=1 OR B.IsPass<0) ";
			ps.Add("FK_Emp", WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}
	}

	/**
	 * 获取草稿箱流程数量
	 * 
	 * @throws Exception
	 */
	public static int getTodolist_Draft() throws Exception {
		// 获取数据.
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		BP.DA.Paras ps = new BP.DA.Paras();
		ps.SQL = "SELECT count(a.WorkID ) as Num FROM WF_GenerWorkFlow A WHERE WFState=1 AND Starter=" + dbStr
				+ "Starter";
		ps.Add(GenerWorkFlowAttr.Starter, BP.Web.WebUser.getNo());
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/**
	 * 获取已经完成流程数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getTodolist_Complete() throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT count(WorkID) Num FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo()
					+ "@%' AND WFState=" + WFState.Complete.getValue();
			return BP.DA.DBAccess.RunSQLReturnValInt(ps, 0);
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT count(*) Num FROM V_FlowData WHERE FlowEmps LIKE '%@" + WebUser.getNo()
					+ "%' AND FID=0 AND WFState=" + WFState.Complete.getValue();
			return BP.DA.DBAccess.RunSQLReturnValInt(ps, 0);
		}
	}

	/**
	 * 共享任务个数
	 * 
	 * @throws Exception
	 */
	public static int getTodolist_Sharing() throws Exception {
		// if (BP.WF.Glo.IsEnableTaskPool == false)
		// return 0
		// throw new Exception("@你必须在Web.config中启用IsEnableTaskPool才可以执行此操作。");

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue()
				+ ") AND TaskSta=" + TaskSta.Sharing.getValue();
		String sql;
		String realSql = null;
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
					+ "FK_Emp AND TaskSta=0";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case SpecFlows:
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
					+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " ";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case None:
			// 不是授权状态
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		default:
			throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	// / <summary>
	// / 申请下来的工作个数
	// / </summary>
	public static int getTodolist_Apply() throws Exception {
		if (BP.WF.Glo.getIsEnableTaskPool() == false)
			return 0;

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue()
				+ ") AND TaskSta=" + TaskSta.Takeback.getValue();
		String sql;
		String realSql;
		if (WebUser.getIsAuthorize() == false) {
			/* 不是授权状态 */
			// ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql +
			// ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY FK_Flow,ADT DESC ";
			// ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql +
			// ") AND FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC ";
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp";

			// ps.SQL = "select v1.*,v2.name,v3.name as ParentName from (" +
			// realSql +
			// ") as v1 left join JXW_Inc v2 on v1.WorkID=v2.OID left join
			// Jxw_Inc V3 on v1.PWorkID = v3.OID ORDER BY v1.ADT DESC";

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnValInt(ps);
		}

		/* 如果是授权状态, 获取当前委托人的信息. */
		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
					+ "FK_Emp AND TaskSta=0";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case SpecFlows:
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
					+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case None:
			throw new Exception("对方(" + WebUser.getNo() + ")已经取消了授权.");
		default:
			throw new Exception("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/**
	 * 处理延期的任务.根据节点属性的设置
	 * 
	 * @return 返回处理的消息
	 */
	public static String DTS_DealDeferredWork() {
		return "";

		/*
		 * //string sql =
		 * "SELECT * FROM WF_EmpWorks WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE OutTimeDeal >0 ) AND SDT <='"
		 * + DataType.CurrentData + "' ORDER BY FK_Emp"; //改成小于号SDT <'" +
		 * DataType.CurrentData String sql =
		 * "SELECT * FROM WF_EmpWorks WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE OutTimeDeal >0 ) AND SDT <'"
		 * + DataType.getCurrentData() + "' ORDER BY FK_Emp"; //string sql =
		 * "SELECT * FROM WF_EmpWorks WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE OutTimeDeal >0 ) AND SDT <='2013-12-30' ORDER BY FK_Emp"
		 * ; DataTable dt = DBAccess.RunSQLReturnTable(sql); String msg = "";
		 * String dealWorkIDs = ""; for (DataRow dr : dt.Rows) { String FK_Emp =
		 * dr.getValue("FK_Emp").toString(); String fk_flow =
		 * dr.getValue("FK_Flow").toString(); int fk_node =
		 * Integer.parseInt(dr.getValue("FK_Node").toString()); long workid =
		 * Long.parseLong(dr.getValue("WorkID").toString()); long fid =
		 * Long.parseLong(dr.getValue("FID").toString());
		 * 
		 * // 方式两个人同时处理一件工作, 一个人处理后，另外一个人还可以处理的情况. if (dealWorkIDs.contains(","
		 * + workid + ",")) { continue; } dealWorkIDs += "," + workid + ",";
		 * 
		 * if (!FK_Emp.equals(WebUser.getNo())) { Emp emp = new Emp(FK_Emp);
		 * BP.Web.WebUser.SignInOfGener(emp); }
		 * 
		 * BP.WF.Node nd = new BP.WF.Node(); nd.setNodeID(fk_node);
		 * nd.Retrieve();
		 * 
		 * // 首先判断是否有启动的表达式, 它是是否自动执行的总阀门。
		 * 
		 * if (StringHelper.isNullOrEmpty(nd.getDoOutTimeCond()) == false) {
		 * Node nodeN = new Node(nd.getNodeID()); Work wk = nodeN.getHisWork();
		 * wk.setOID(workid); wk.Retrieve(); //Object tempVar =
		 * nd.getDoOutTimeCond(); String exp = (String)((tempVar instanceof
		 * String) ? tempVar : null); if (Glo.ExeExp(exp, wk) == false) {
		 * continue; // 不能通过条件的设置. } }
		 * 
		 * switch (nd.getHisOutTimeDeal()) { case None: break; case
		 * AutoTurntoNextStep: //自动转到下一步骤. if
		 * (StringHelper.isNullOrEmpty(nd.getDoOutTime())) {
		 * //如果是空的,没有特定的点允许，就让其它向下执行。 msg +=
		 * BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText(); }
		 * else { int nextNode = Dev2Interface.Node_GetNextStepNode(fk_flow,
		 * workid); if (nd.getDoOutTime().contains((new
		 * Integer(nextNode)).toString())) //如果包含了当前点的ID,就让它执行下去. { msg +=
		 * BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText(); } }
		 * break; case AutoJumpToSpecNode: //自动的跳转下一个节点. if
		 * (StringHelper.isNullOrEmpty(nd.getDoOutTime())) { throw new
		 * RuntimeException("@设置错误,没有设置要跳转的下一步节点."); } int nextNodeID =
		 * Integer.parseInt(nd.getDoOutTime()); msg +=
		 * BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid, null, null,
		 * nextNodeID, null).ToMsgOfText(); break; case AutoShiftToSpecUser:
		 * //移交给指定的人员. msg += BP.WF.Dev2Interface.Node_Shift(fk_flow, fk_node,
		 * workid, fid, nd.getDoOutTime(), "来自ccflow的自动消息:(" +
		 * BP.Web.WebUser.getName() + ")工作未按时处理(" + nd.getName() + "),现在移交给您。");
		 * break; case SendMsgToSpecUser: //向指定的人员发消息.
		 * BP.WF.Dev2Interface.Port_SendMsg(nd.getDoOutTime(), "来自ccflow的自动消息:("
		 * + BP.Web.WebUser.getName() + ")工作未按时处理(" + nd.getName() + ")",
		 * "感谢您选择ccflow.", "SpecEmp" + workid); break; case DeleteFlow: //删除流程.
		 * msg += BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow, workid,
		 * true); break; case RunSQL: msg +=
		 * BP.DA.DBAccess.RunSQL(nd.getDoOutTime()); break; default: throw new
		 * RuntimeException("@错误没有判断的超时处理方式." + nd.getHisOutTimeDeal()); } } Emp
		 * emp1 = new Emp("admin"); BP.Web.WebUser.SignInOfGener(emp1); return
		 * msg;
		 */
	}

	/**
	 * 自动执行开始节点数据 说明:根据自动执行的流程设置，自动启动发起的流程。
	 * 比如：您根据ccflow的自动启动流程的设置，自动启动该流程，不使用ccflow的提供的服务程序，您需要按如下步骤去做。 1,
	 * 写一个自动调度的程序。 2，根据自己的时间需要调用这个接口。
	 * 
	 * @param fl
	 *            流程实体,您可以 new Flow(flowNo); 来传入.
	 * @throws Exception
	 */
	public static void DTS_AutoStarterFlow(Flow fl) throws Exception {

		/// #region 读取数据.
		BP.Sys.MapExt me = new BP.Sys.MapExt();
		int i = me.Retrieve(MapExtAttr.FK_MapData, "ND" + Integer.parseInt(fl.getNo()) + "01", MapExtAttr.ExtType,
				"PageLoadFull");
		if (i == 0) {
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return;
		}

		// 获取从表数据.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.getTag1().split("[*]", -1);
		for (String sql : dtlSQLs) {
			if (StringHelper.isNullOrEmpty(sql)) {
				continue;
			}

			String[] tempStrs = sql.split("[=]", -1);
			String dtlName = tempStrs[0];
			DataTable dtlTable = BP.DA.DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
			dtlTable.TableName = dtlName;
			ds.Tables.add(dtlTable);
		}
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = BP.DA.DBAccess.RunSQLReturnTable(me.getTag());
		if (dtMain.Columns.contains("Starter") == false) {
			errMsg += "@配值的主表中没有Starter列.";
		}

		if (dtMain.Columns.contains("MainPK") == false) {
			errMsg += "@配值的主表中没有MainPK列.";
		}

		if (errMsg.length() > 2) {
			BP.DA.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")的开始节点设置发起数据,不完整." + errMsg);
			return;
		}
		String nodeTable = "ND" + Integer.parseInt(fl.getNo()) + "01";
		MapData md = new MapData(nodeTable);

		for (DataRow dr : dtMain.Rows) {
			String mainPK = dr.getValue("MainPK").toString();
			String sql = "SELECT OID FROM " + md.getPTable() + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0) {
				continue; // 说明已经调度过了
			}

			String starter = dr.getValue("Starter").toString();
			if (!starter.equals(BP.Web.WebUser.getNo())) {
				BP.Web.WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo(starter);
				if (emp.RetrieveFromDBSources() == 0) {
					BP.DA.Log.DefaultLogWriteLineInfo(
							"@数据驱动方式发起流程(" + fl.getName() + ")设置的发起人员:" + emp.getNo() + "不存在。");
					continue;
				}

				BP.Web.WebUser.SignInOfGener(emp);
			}

			/// #region 给值.
			Work wk = fl.NewWork();
			for (DataColumn dc : dtMain.Columns) {
				wk.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0) {
				String refPK = dr.getValue("MainPK").toString();
				MapDtls dtls = wk.getHisNode().getMapData().getMapDtls(); // new
																			// MapDtls(nodeTable);
				for (MapDtl dtl : dtls.ToJavaList()) {
					for (DataTable dt : ds.Tables) {
						if (dt.TableName != dtl.getNo()) {
							continue;
						}

						// 删除原来的数据。
						GEDtl dtlEn = dtl.getHisGEDtl();
						dtlEn.Delete(GEDtlAttr.RefPK, (new Long(wk.getOID())).toString());

						// 执行数据插入。
						for (DataRow drDtl : dt.Rows) {
							if (!drDtl.getValue("RefMainPK").toString().equals(refPK)) {
								continue;
							}

							dtlEn = dtl.getHisGEDtl();

							for (DataColumn dc : dt.Columns) {
								dtlEn.SetValByKey(dc.ColumnName, drDtl.getValue(dc.ColumnName).toString());
							}

							dtlEn.setRefPK((new Long(wk.getOID())).toString());
							dtlEn.Insert();
						}
					}
				}
			}
			// 处理发送信息.
			Node nd = fl.getHisStartNode();
			try {
				WorkNode wn = new WorkNode(wk, nd);
				String msg = wn.NodeSend().ToMsgOfHtml();
			} catch (RuntimeException ex) {
				BP.DA.Log.DefaultLogWriteLineWarning(ex.getMessage());
			}
		}
	}

	// 数据集合接口(如果您想获取一个结果集合的接口，都是以DB_开头的.)
	/**
	 * 获取能发起流程的人员
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public static String GetFlowStarters(String fk_flow) throws NumberFormatException, Exception {
		BP.WF.Node nd = new Node(Integer.parseInt(fk_flow + "01"));
		String sql = "";
		switch (nd.getHisDeliveryWay()) {
		case ByBindEmp: // 按人员
			sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node=" + nd.getNodeID()
					+ ")";
			break;
		case ByDept: // 按部门
			sql = "SELECT * FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node="
					+ nd.getNodeID() + ")";
			break;
		case ByStation: // 按岗位
			sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation()
					+ " WHERE FK_Station IN ( SELECT FK_Station from WF_nodeStation where FK_Node=" + nd.getNodeID()
					+ ")) ";
			break;
		default:
			throw new RuntimeException("@开始节点的人员访问规则错误,不允许在开始节点设置此访问类型:" + nd.getHisDeliveryWay());
		}
		return sql;
	}

	public static String GetFlowStarters(String fk_flow, String fk_dept) throws NumberFormatException, Exception {
		BP.WF.Node nd = new Node(Integer.parseInt(fk_flow + "01"));
		String sql = "";
		switch (nd.getHisDeliveryWay()) {
		case ByBindEmp: // 按人员
			sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node=" + nd.getNodeID()
					+ ") and fk_dept='" + fk_dept + "'";
			break;
		case ByDept: // 按部门
			sql = "SELECT * FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node="
					+ nd.getNodeID() + ") and fk_dept='" + fk_dept + "' ";
			break;
		case ByStation: // 按岗位
			sql = "SELECT * FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation()
					+ " WHERE FK_Station IN ( SELECT FK_Station from WF_nodeStation where FK_Node=" + nd.getNodeID()
					+ ")) and fk_dept='" + fk_dept + "' ";
			break;
		default:
			throw new RuntimeException("@开始节点的人员访问规则错误,不允许在开始节点设置此访问类型:" + nd.getHisDeliveryWay());
		}
		return sql;
	}

	// 与子流程相关.
	/**
	 * 获取流程事例的运行轨迹数据. 说明：使用这些数据可以生成流程的操作日志.
	 * 
	 * @param workid
	 *            工作ID
	 * @return GenerWorkFlows
	 * @throws Exception
	 */
	public static GenerWorkFlows DB_SubFlows(long workid) throws Exception {
		GenerWorkFlows gwf = new GenerWorkFlows();
		gwf.Retrieve(GenerWorkFlowAttr.PWorkID, workid);
		return gwf;
	}

	/**
	 * 获取流程事例的运行轨迹数据. 说明：使用这些数据可以生成流程的操作日志.
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @return 从临时表与轨迹表获取流程轨迹数据.
	 * @throws Exception
	 */
	public static DataSet DB_GenerTrack(String fk_flow, long workid, long fid) throws Exception {
		// 定义变量，把数据都放入这个track里面.
		DataSet ds = new DataSet();

		// 把轨迹表.
		ds.Tables.add(DB_GenerTrackTable(fk_flow, workid, fid));

		// 把抄送信息加入.
		CCLists ens = new CCLists(fk_flow, workid, fid);
		ds.Tables.add(ens.ToDataTableField("WF_CCList"));

		// 把未来节点选择人信息写入里面.
		long wfid = 0;
		if (fid != 0) {
			wfid = fid;
		}

		SelectAccpers accepts = new SelectAccpers(wfid);
		ds.Tables.add(ens.ToDataTableField("WF_SelectAccper"));

		// 把节点信息写入里面.
		BP.WF.Nodes nds = new Nodes();
		nds.Retrieve(NodeAttr.FK_Flow, fk_flow);
		ds.Tables.add(nds.ToDataTableField("WF_Node"));

		// 把方向写入里面.
		Directions dirs = new Directions();
		dirs.Retrieve(NodeAttr.FK_Flow, fk_flow);
		ds.Tables.add(dirs.ToDataTableField("WF_Direction"));
		return ds;
	}

	/**
	 * 区分大小写 获取流程事例的运行轨迹数据. 说明：使用这些数据可以生成流程的操作日志.
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @return 从临时表与轨迹表获取流程轨迹数据.
	 * @throws Exception
	 */
	public static DataSet DB_GenerTrack_2017(String fk_flow, long workid, long fid) throws Exception {
		// 定义变量，把数据都放入这个track里面.
		DataSet ds = new DataSet();

		// 把轨迹表.
		ds.Tables.add(DB_GenerTrackTable(fk_flow, workid, fid));

		// 把抄送信息加入.
		CCLists ens = new CCLists(fk_flow, workid, fid);
		ds.Tables.add(ens.ToDataTableField("WF_CCList"));

		// 把未来节点选择人信息写入里面.
		long wfid = 0;
		if (fid != 0) {
			wfid = fid;
		}
		SelectAccpers accepts = new SelectAccpers(wfid);
		ds.Tables.add(ens.ToDataTableField("WF_SelectAccper"));

		// 把节点信息写入里面.
		BP.WF.Nodes nds = new Nodes();
		nds.Retrieve(NodeAttr.FK_Flow, fk_flow);
		ds.Tables.add(nds.ToDataTableField("WF_Node"));

		// 把方向写入里面.
		Directions dirs = new Directions();
		dirs.Retrieve(NodeAttr.FK_Flow, fk_flow);
		ds.Tables.add(dirs.ToDataTableField("WF_Direction"));
		return ds;
	}

	public static DataTable DB_GenerTrackTable(String fk_flow, long workid, long fid) throws Exception {
		/// #region 获取track数据.
		String sqlOfWhere2 = "";
		String sqlOfWhere1 = "";
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (fid == 0) {
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "WorkID11 OR WorkID=" + dbStr + "WorkID12 )  ";
			ps.Add("WorkID11", workid);
			ps.Add("WorkID12", workid);
		} else {
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "FID11 OR WorkID=" + dbStr + "FID12 ) ";
			ps.Add("FID11", fid);
			ps.Add("FID12", fid);
		}

		String sql = "";
		sql = "SELECT MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan,Msg,NodeData,Exer,Tag FROM ND"
				+ Integer.parseInt(fk_flow) + "Track " + sqlOfWhere1 + " ORDER BY RDT ASC ";
		ps.SQL = sql;
		DataTable dt = null;

		try {
			dt = DBAccess.RunSQLReturnTable(ps);
		} catch (java.lang.Exception e) {
			// 处理track表.
			Track.CreateOrRepairTrackTable(fk_flow);
			dt = DBAccess.RunSQLReturnTable(ps);
		}

		// 把列名转化成区分大小写.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
			dt.Columns.get("WORKTIMESPAN").ColumnName = "Msg";
			dt.Columns.get("NODEDATA").ColumnName = "NodeData";
			dt.Columns.get("EXER").ColumnName = "Exer";
			dt.Columns.get("TAG").ColumnName = "Tag";
		}

		// 把track加入里面去.
		dt.TableName = "Track";
		return dt;
	}

	/**
	 * 获取一个流程
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param userNo
	 *            操作员编号
	 * @return
	 */
	public static DataTable DB_GenerNDxxxRpt(String fk_flow, String userNo) {
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Rpt WHERE FlowStarter=" + dbstr
				+ "FlowStarter  ORDER BY RDT";
		ps.Add(GERptAttr.FlowStarter, userNo);
		return DBAccess.RunSQLReturnTable(ps);
	}

	/**
	 * 获取当前节点可抄送人员列表
	 * 
	 * @param fk_node
	 * @param workid
	 * @return
	 */
	/*
	 * public static DataTable DB_CanCCEmps(int fk_node, long workid) {
	 * DataTable table = new DataTable(); String ccers = null;
	 * 
	 * WorkNode node = new WorkNode(workid, fk_node);
	 * 
	 * if (node.getHisNode().getHisCCRole() == CCRole.AutoCC ||
	 * node.getHisNode().getHisCCRole() == CCRole.HandAndAuto) { try { //如果是自动抄送
	 * CC cc = node.getHisNode().getHisCC();
	 * 
	 * table = cc.GenerCCers(node.rptGe); } catch (RuntimeException ex) { throw
	 * new RuntimeException("@处理操送时出现错误:" + ex.getMessage()); } } if
	 * (node.getHisNode().getHisCCRole() == CCRole.BySysCCEmps) { CC cc =
	 * node.getHisNode().getHisCC();
	 * 
	 * //取出抄送人列表 String temps = node.rptGe.GetValStrByKey("SysCCEmps"); if
	 * (!StringHelper.isNullOrEmpty(temps)) { String[] cclist =
	 * temps.split("[|]", -1);
	 * 
	 * if (!table.Columns.contains("No")) { table.Columns.Add("No"); } if
	 * (!table.Columns.contains("Name")) { table.Columns.Add("Name"); } for
	 * (String item : cclist) { String[] tmp = item.split("[,]", -1);
	 * 
	 * DataRow row = table.NewRow();
	 * 
	 * row.setValue("No",tmp[0]); row.setValue("Name",tmp[1]);
	 * table.Rows.Add(row); } } } return table; }
	 */
	/**
	 * 获取可以执行指定节点人的列表
	 * 
	 * @param fk_node
	 *            节点编号
	 * @param workid
	 *            工作ID
	 * @return
	 */
	public static DataSet DB_CanExecSpecNodeEmps(int fk_node, long workid) {
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
	 * 获得可以抄送的人员列表
	 * 
	 * @param fk_node
	 *            节点编号
	 * @param workid
	 *            工作ID
	 * @return
	 */
	public static DataSet DB_CanCCSpecNodeEmps(int fk_node, long workid) {
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

	/// #endregion 获取能够发送或者抄送人员的列表.

	/// #region 获取操送列表
	/**
	 * 获取指定人员的抄送列表 说明:可以根据这个列表生成指定用户的抄送数据.
	 * 
	 * @param FK_Emp
	 *            人员编号,如果是null,则返回所有的.
	 * @return 返回该人员的所有抄送列表,结构同表WF_CCList.
	 */
	public static DataTable DB_CCList(String FK_Emp) {
		Paras ps = new Paras();
		if (FK_Emp == null) {
			ps.SQL = "SELECT * FROM WF_CCList WHERE 1=1";
		} else {
			ps.SQL = "SELECT a.MyPK,A.Title,A.FK_Flow,A.FlowName,A.WorkID,A.Doc,A.Rec,A.RDT,A.FID,B.FK_Node,B.NodeName FROM WF_CCList A, WF_GenerWorkFlow B WHERE A.CCTo="
					+ SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND B.WorkID=A.WorkID";

			// ps.SQL = "SELECT * FROM WF_CCList WHERE CCTo=" +
			// SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
			ps.Add("FK_Emp", FK_Emp);
		}
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
		return dt;
	}

	public static DataTable DB_CCList(String FK_Emp, CCSta sta) {
		Paras ps = new Paras();
		if (FK_Emp == null) {
			ps.SQL = "SELECT * FROM WF_CCList WHERE Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta";
			ps.Add("Sta", sta.getValue());
		} else {
			ps.SQL = "SELECT * FROM WF_CCList WHERE CCTo=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND Sta="
					+ SystemConfig.getAppCenterDBVarStr() + "Sta";
			ps.Add("FK_Emp", FK_Emp);
			ps.Add("Sta", sta.getValue());
		}
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
		return dt;
	}

	/**
	 * 获取指定人员的抄送列表(未读)
	 * 
	 * @param FK_Emp
	 *            人员编号,如果是null,则返回所有的.
	 * @return 返回该人员的未读的抄送列表
	 */
	public static DataTable DB_CCList_UnRead(String FK_Emp) {
		return DB_CCList(FK_Emp, CCSta.UnRead);
	}

	/**
	 * 获取指定人员的抄送列表(已读)
	 * 
	 * @param FK_Emp
	 *            人员编号
	 * @return 返回该人员的已读的抄送列表
	 */
	public static DataTable DB_CCList_Read(String FK_Emp) {
		return DB_CCList(FK_Emp, CCSta.Read);
	}

	/**
	 * 获取指定人员的抄送列表(已删除)
	 * 
	 * @param FK_Emp
	 *            人员编号
	 * @return 返回该人员的已删除的抄送列表
	 */
	public static DataTable DB_CCList_Delete(String FK_Emp) {
		return DB_CCList(FK_Emp, CCSta.Del);
	}

	/**
	 * 获取指定人员的抄送列表(已回复)
	 * 
	 * @param FK_Emp
	 *            人员编号
	 * @return 返回该人员的已删除的抄送列表
	 */
	public static DataTable DB_CCList_CheckOver(String FK_Emp) {
		return DB_CCList(FK_Emp, CCSta.CheckOver);
	}

	/// #endregion

	/// #region 获取当前操作员可以发起的流程集合
	/**
	 * 获取指定人员能够发起流程的集合. 说明:利用此接口可以生成用户的发起的流程列表.
	 * 
	 * @param userNo
	 *            操作员编号
	 * @return BP.WF.Flows 可发起的流程对象集合,如何使用该方法形成发起工作列表,请参考:\WF\UC\Start.ascx
	 * @throws Exception
	 */
	public static Flows DB_GenerCanStartFlowsOfEntities(String userNo) throws Exception {
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database) {
			String sql = "";
			// 采用新算法.
			if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne) {
				sql = "SELECT FK_Flow FROM V_FlowStarter WHERE FK_Emp='" + userNo + "'";
			} else {
				sql = "SELECT FK_Flow FROM V_FlowStarterBPM WHERE FK_Emp='" + userNo + "'";
			}

			Flows fls = new Flows();
			BP.En.QueryObject qo = new BP.En.QueryObject(fls);
			qo.AddWhereInSQL("No", sql);
			qo.addAnd();
			qo.AddWhere(FlowAttr.IsCanStart, true);
			if (WebUser.getIsAuthorize()) {
				// 如果是授权状态
				qo.addAnd();
				WFEmp wfEmp = new WFEmp(userNo);
				qo.AddWhereIn("No", wfEmp.getAuthorFlows());
			}
			qo.addOrderBy("FK_FlowSort", FlowAttr.Idx);
			qo.DoQuery();
			return fls;
		}

		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices) {
			String sql = "";
			// 按岗位计算.
			sql += "SELECT A.FK_Flow FROM WF_Node A,WF_NodeStation B WHERE A.NodePosType=0 AND ( A.WhoExeIt=0 OR A.WhoExeIt=2 ) AND  A.NodeID=B.FK_Node AND B.FK_Station IN ("
					+ BP.Web.WebUser.getHisStationsStr() + ")";
			sql += " UNION  "; // 按指定的人员计算.
			sql += "SELECT FK_Flow FROM WF_Node WHERE NodePosType=0 AND ( WhoExeIt=0 OR WhoExeIt=2 ) AND NodeID IN ( SELECT FK_Node FROM WF_NodeEmp WHERE FK_Emp='"
					+ userNo + "' ) ";
			sql += " UNION  "; // 按部门计算.
			sql += "SELECT A.FK_Flow FROM WF_Node A,WF_NodeDept B WHERE A.NodePosType=0 AND ( A.WhoExeIt=0 OR A.WhoExeIt=2 ) AND  A.NodeID=B.FK_Node AND B.FK_Dept IN ("
					+ BP.Web.WebUser.getHisDeptsStr() + ") ";

			//// 采用新算法.
			// if (BP.WF.Glo.OSModel == BP.Sys.OSModel.OneOne)
			// sql = "SELECT FK_Flow FROM V_FlowStarter WHERE FK_Emp='" + userNo
			//// + "'";
			// else
			// sql = "SELECT FK_Flow FROM V_FlowStarterBPM WHERE FK_Emp='" +
			//// userNo + "'";

			Flows fls = new Flows();
			BP.En.QueryObject qo = new BP.En.QueryObject(fls);
			qo.AddWhereInSQL("No", sql);
			qo.addAnd();
			qo.AddWhere(FlowAttr.IsCanStart, true);
			if (WebUser.getIsAuthorize()) {
				// 如果是授权状态
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
	
	public static DataTable DB_GenerCanStartFlowsOfDataTable(String userNo) throws Exception
	{
	 return DB_GenerCanStartFlowsOfDataTable(userNo,null);
	}

	/**
	 * 获取指定人员能够发起流程的集合 说明:利用此接口可以生成用户的发起的流程列表.
	 * 
	 * @param userNo
	 *            操作员编号
	 * @return Datatable类型的数据集合,数据结构与表WF_Flow大致相同.
	 *         如何使用该方法形成发起工作列表,请参考:\WF\UC\Start.ascx
	 * @throws Exception
	 */
	public static DataTable DB_GenerCanStartFlowsOfDataTable(String userNo,String domain) throws Exception {

		String sql = "SELECT A.No,A.Name,a.IsBatchStart,a.FK_FlowSort,C.Name AS FK_FlowSortText,A.IsStartInMobile, A.Idx";
		sql += " FROM WF_Flow A, V_FlowStarterBPM B, WF_FlowSort C  ";
		sql += " WHERE A.No=B.FK_Flow AND A.FK_FlowSort=C.No  AND FK_Emp='" + userNo + "' ";
		if (DataType.IsNullOrEmpty(domain)==false)
			sql+=" AND C.Domain='"+domain+"'";
		
		sql += " ORDER BY C.Idx, A.Idx";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("ISBATCHSTART").ColumnName = "IsBatchStart";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("FK_FLOWSORTTEXT").ColumnName = "FK_FlowSortText";
			dt.Columns.get("IDX").ColumnName = "Idx";
			dt.Columns.get("ISSTARTINMOBILE").ColumnName = "IsStartInMobile";
			dt.Columns.get("IDX").ColumnName = "IsStartInMobile";
			 

		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
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

	public static DataTable DB_GenerCanStartFlowsTree(String userNo) throws Exception {
		// 发起.
		DataTable table = DB_GenerCanStartFlowsOfDataTable(userNo,null);
		table.Columns.Add("ParentNo");
		table.Columns.Add("ICON");
		String flowSort = String.format("select No,Name,ParentNo from WF_FlowSort");

		DataTable sortTable = DBAccess.RunSQLReturnTable(flowSort);
		for (DataRow row : sortTable.Rows) {
			DataRow newRow = table.NewRow();
			newRow.setValue("No", row.getValue("No"));
			newRow.setValue("Name", row.getValue("Name"));
			newRow.setValue("ParentNo", row.getValue("ParentNo"));
			newRow.setValue("ICON", "icon-tree_folder");
			table.Rows.add(newRow);
		}

		for (DataRow row : table.Rows) {
			if (StringHelper.isNullOrEmpty(row.getValue("ParentNo").toString())) {
				row.setValue("ParentNo", row.getValue("FK_FlowSort"));
			}
			if (StringHelper.isNullOrEmpty(row.getValue("ICON").toString())) {
				row.setValue("ICON", "icon-4");
			}
		}
		return table;
	}

	/**
	 * 获取(同表单)合流点上的子线程 说明:如果您要想在合流点看到所有的子线程运行的状态.
	 * 
	 * @param nodeIDOfHL
	 *            合流点ID
	 * @param workid
	 *            工作ID
	 * @return 与表WF_GenerWorkerList结构类同的datatable.
	 * @throws Exception
	 */
	public static DataTable DB_GenerHLSubFlowDtl_TB(int nodeIDOfHL, long workid) throws Exception {
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
		qo.AddWhere(GenerWorkerListAttr.FK_Node, nd.getFromNodes().get(0).GetValByKey(NodeAttr.NodeID));

		DataTable dt = qo.DoQueryToTable();
		if (dt.Rows.size() == 1) {
			qo.clear();
			qo.AddWhere(GenerWorkerListAttr.FID, wk.getOID());
			qo.addAnd();
			qo.AddWhere(GenerWorkerListAttr.IsEnable, 1);
			return qo.DoQueryToTable();
		}
		return dt;
	}

	/**
	 * 获取(异表单)合流点上的子线程
	 * 
	 * @param nodeIDOfHL
	 *            合流点ID
	 * @param workid
	 *            工作ID
	 * @return 与表WF_GenerWorkerList结构类同的datatable.
	 * @throws Exception
	 */
	public static DataTable DB_GenerHLSubFlowDtl_YB(int nodeIDOfHL, long workid) throws Exception {
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

	/**
	 * 获取当前操作员的指定流程的流程草稿数据
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @return 返回草稿数据集合,列信息. OID=工作ID,Title=标题,RDT=记录日期,FK_Flow=流程编号,FID=流程ID,
	 *         FK_Node=节点ID
	 * @throws Exception
	 */
	public static DataTable DB_GenerDraftDataTable() throws Exception {
		return DB_GenerDraftDataTable(null);
	}

	public static DataTable DB_GenerDraftDataTable(String flowNo) throws Exception {
		// 获取数据.
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		BP.DA.Paras ps = new BP.DA.Paras();
		if (flowNo == null) {
			ps.SQL = "SELECT WorkID,Title,FK_Flow,FlowName,RDT,FlowNote FROM WF_GenerWorkFlow A WHERE WFState=1 AND Starter="
					+ dbStr + "Starter ORDER BY RDT";
			ps.Add(GenerWorkFlowAttr.Starter, BP.Web.WebUser.getNo());
		} else {
			ps.SQL = "SELECT WorkID,Title,FK_Flow,FlowName,RDT,FlowNote FROM WF_GenerWorkFlow A WHERE WFState=1 AND Starter="
					+ dbStr + "Starter AND FK_Flow=" + dbStr + "FK_Flow ORDER BY RDT";
			ps.Add(GenerWorkFlowAttr.FK_Flow, flowNo);
			ps.Add(GenerWorkFlowAttr.Starter, BP.Web.WebUser.getNo());
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
		}

		return dt;
	}

	/**
	 * 获得我关注的流程列表
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param userNo
	 *            操作员编号
	 * @return 返回当前关注的流程列表.
	 * @throws Exception
	 */
	public static DataTable DB_Focus(String flowNo, String userNo, String orderBy) throws Exception {
		if (("").equals(flowNo)) {
			flowNo = null;
		}
		if (userNo == null) {
			userNo = BP.Web.WebUser.getNo();
		}
		String orderByColumn = " ORDER BY rdt desc";
		if (StringUtils.isNotBlank(orderBy)) {
			orderByColumn = " ORDER BY " + orderBy + " ,rdt desc";
		}
		// 执行sql.
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM WF_GenerWorkFlow WHERE AtPara LIKE  '%F_" + userNo + "=1%'" + orderByColumn;
		if (flowNo != null) {
			ps.SQL = "SELECT * FROM WF_GenerWorkFlow WHERE AtPara LIKE  '%F_" + userNo + "=1%' AND FK_Flow="
					+ SystemConfig.getAppCenterDBVarStr() + "FK_Flow" + orderByColumn;
			ps.Add("FK_Flow", flowNo);
		}
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		// 添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
			dt.Columns.get("CFLOWNO").ColumnName = "CFlowNo";
			dt.Columns.get("CWORKID").ColumnName = "CWorkID";
		}
		return dt;
	}

	public static DataTable DB_Focus(String flowNo, String userNo) throws Exception {
		return DB_Focus(flowNo, userNo, null);
	}

	/**
	 * 获取当前人员待处理的工作
	 * 
	 * @param fk_node
	 *            节点编号
	 * @return 共享工作列表
	 * @throws Exception
	 */
	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, int fk_node) throws Exception {
		// 执行 todolist 调度.
		DTS_GenerWorkFlowTodoSta();

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (fk_node == 0) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND WFState!="
							+ WFState.Batch.getValue() + " ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp  AND WFState!="
							+ WFState.Batch.getValue() + " ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND FK_Node="
							+ dbstr + "FK_Node  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY  ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Node=" + dbstr
							+ "FK_Node  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY  ADT DESC ";
				}

				ps.Add("FK_Node", fk_node);
				ps.Add("FK_Emp", userNo);
			}
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
			// 添加oracle的处理
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
			return dt;
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			if (fk_node == 0) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr
							+ "FK_Emp  AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Node" + dbstr
							+ "FK_Node AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Node" + dbstr
							+ "FK_Node ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
				ps.Add("FK_Node", fk_node);
			}
			break;
		case SpecFlows:
			if (fk_node == 0) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN "
							+ emp.getAuthorFlows() + " AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN "
							+ emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Node" + dbstr
							+ "FK_Node AND FK_Flow IN " + emp.getAuthorFlows()
							+ " AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Node" + dbstr
							+ "FK_Node AND FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
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
		// 添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
		return dt2;
	}

	/**
	 * 获取当前人员待处理的工作
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @return 共享工作列表
	 * @throws Exception
	 */
	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, String fk_flow) throws Exception {
		// 执行 todolist 调度.
		DTS_GenerWorkFlowTodoSta();

		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND WFState!="
							+ WFState.Batch.getValue() + " ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp  AND WFState!="
							+ WFState.Batch.getValue() + " ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0 AND FK_Flow="
							+ dbstr + "FK_Flow  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY  ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Flow=" + dbstr
							+ "FK_Flow  AND WFState!=" + WFState.Batch.getValue() + " ORDER BY  ADT DESC ";
				}

				ps.Add("FK_Flow", fk_flow);
				ps.Add("FK_Emp", userNo);
			}
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr
							+ "FK_Emp  AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Flow" + dbstr
							+ "FK_Flow AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND FK_Flow" + dbstr
							+ "FK_Flow ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
				ps.Add("FK_Flow", fk_flow);
			}
			break;
		case SpecFlows:
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN "
							+ emp.getAuthorFlows() + " AND TaskSta=0 ORDER BY FK_Flow,ADT DESC ";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN "
							+ emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Flow" + dbstr
							+ "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows()
							+ " AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp  AND FK_Flow" + dbstr
							+ "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
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
	 * 根据状态获取当前操作员的共享工作
	 * 
	 * @param wfState
	 *            流程状态
	 * @param fk_flow
	 *            流程编号
	 * @return 表结构与视图WF_EmpWorks一致
	 * @throws Exception
	 */
	public static DataTable DB_GenerEmpWorksOfDataTable(String userNo, WFState wfState, String fk_flow)
			throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND TaskSta=0   ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Emp", userNo);
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND FK_Flow=" + dbstr + "FK_Flow AND TaskSta=0  ORDER BY  ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND FK_Flow=" + dbstr + "FK_Flow ORDER BY  ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Flow", fk_flow);
				ps.Add("FK_Emp", userNo);
			}
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp  AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND FK_Flow" + dbstr + "FK_Flow AND TaskSta=0  ORDER BY FK_Flow,ADT DESC ";
				} else {
					ps.SQL = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND FK_Flow" + dbstr + "FK_Flow ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Emp", BP.Web.WebUser.getNo());
				ps.Add("FK_Flow", fk_flow);
			}
			break;
		case SpecFlows:
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows()
							+ " AND TaskSta=0   ORDER BY FK_Flow,ADT DESC ";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			} else {
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp  AND FK_Flow" + dbstr + "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows()
							+ " AND TaskSta=0   ORDER BY FK_Flow,ADT DESC ";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE WFState=" + dbstr + "WFState AND FK_Emp=" + dbstr
							+ "FK_Emp  AND FK_Flow" + dbstr + "FK_Flow AND FK_Flow IN " + emp.getAuthorFlows()
							+ "  ORDER BY FK_Flow,ADT DESC ";
				}

				ps.Add("WFState", wfState.getValue());
				ps.Add("FK_Emp", BP.Web.WebUser.getNo());
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
	 * 获得待办(包括被授权的待办) 区分是自己的待办，还是被授权的待办通过数据源的 FK_Emp 字段来区分。
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_Todolist(String userNo) throws Exception {
		if (userNo == null) {
			userNo = BP.Web.WebUser.getNo();
			if (WebUser.getIsAuthorize() == false) {
				throw new RuntimeException("@授权登录的模式下不能调用此接口.");
			}
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue()
				+ " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
		// 不是授权状态
		if (BP.WF.Glo.getIsEnableTaskPool() == true) {
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta!=1 ";
		} else {
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
		}

		ps.Add("FK_Emp", userNo);

		// 获取授权给他的人员列表.
		WFEmps emps = new WFEmps();
		emps.Retrieve(WFEmpAttr.Author, userNo);
		for (WFEmp emp : emps.ToJavaList()) {
			switch (emp.getHisAuthorWay()) {
			case All:
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL += " UNION  SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' AND TaskSta!=1  ";
				} else {
					ps.SQL += " UNION  SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' ";
				}
				break;
			case SpecFlows:
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					ps.SQL += " UNION SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0 ";
				} else {
					ps.SQL += " UNION SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' AND  FK_Flow IN " + emp.getAuthorFlows() + "  ";
				}
				break;
			case None: // 非授权状态下.
				continue;
			default:
				throw new RuntimeException("no such way...");
			}
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}

	// ORIGINAL LINE: public static DataTable DB_Todolist(string userNo, int
	// fk_node = 0)
	public static DataTable DB_Todolist(String userNo, int fk_node) {
		String sql = "";
		sql = "SELECT A.* FROM WF_GenerWorkFlow A, WF_FlowSort B, WF_Flow C, WF_GENERWORKERLIST D ";
		sql += " WHERE (WFState=2 OR WFState=5 OR WFState=8)";
		sql += " AND A.FK_FlowSort=B.No ";
		sql += " AND A.FK_Flow=C.No ";
		sql += " AND A.FK_Node=D.FK_Node ";
		sql += " AND A.WorkID=D.WorkID ";
		sql += " AND D.IsPass=0  "; // = 90 是会签主持人.
		sql += " AND D.FK_Emp='" + userNo + "'";

		if (fk_node != 0) {
			sql += " AND A.FK_Node=" + fk_node;
		}

		sql += "  ORDER BY  B.Idx, C.Idx, A.RDT DESC ";

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		// 添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
			// dt.Columns.get("WORKERDEPT").ColumnName = "WorkerDept";
			// dt.Columns.get("RDT").ColumnName = "RDT";
			// dt.Columns.get("ADT").ColumnName = "ADT";
			// dt.Columns.get("SDT").ColumnName = "SDT";
			// dt.Columns.get("FK_EMP").ColumnName = "FK_Emp";
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
			// dt.Columns.get("LISTTYPE").ColumnName = "ListType";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("ATPARA").ColumnName = "AtPara";
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
		}

		return dt;
	}

	/**
	 * 获取当前操作人员的待办信息 数据内容请参考图:WF_EmpWorks
	 * 
	 * @return 返回从视图WF_EmpWorks查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerEmpWorksOfDataTable() throws Exception {
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue()
				+ " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
		String sql;

		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND TaskSta!=1  ORDER BY ADT DESC";
			} else {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp ORDER BY ADT DESC";
			}

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND TaskSta!=1  ORDER BY ADT DESC";
			} else {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp ORDER BY ADT DESC";
			}
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case SpecFlows:
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0    ORDER BY ADT DESC";
			} else {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND  FK_Flow IN " + emp.getAuthorFlows() + "   ORDER BY ADT DESC";
			}

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case None:
			// 不是授权状态
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp AND TaskSta!=1  ORDER BY ADT DESC";
			} else {
				ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr
						+ "FK_Emp ORDER BY ADT DESC";
			}

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);

		// WebUser.setAuth(null); //对方已经取消了授权.
		default:
			throw new RuntimeException("no such way...");
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}

	/**
	 * 获得已完成数据统计列表
	 * 
	 * @param userNo
	 *            操作员编号
	 * @return 具有FlowNo,FlowName,Num三个列的指定人员的待办列表
	 */
	public static DataTable DB_FlowCompleteGroup(String userNo) {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT FK_Flow as No,FlowName,COUNT(*) Num FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + userNo
					+ "@%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " GROUP BY FK_Flow,FlowName";
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		} else {
			throw new RuntimeException("未实现..");
			// Paras ps = new Paras();
			// string dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			// ps.SQL = "SELECT * FROM V_FlowData WHERE FlowEmps LIKE '%@" +
			// userNo + "%' AND FID=0 AND WFState=" + (int)WFState.Complete;
			// return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	/**
	 * 获取指定页面已经完成流程
	 * 
	 * @param userNo
	 *            用户编号
	 * @param flowNo
	 *            流程编号
	 * @param pageSize
	 *            每页的数量
	 * @param pageIdx
	 *            第几页
	 * @return 用户编号
	 * @throws Exception
	 */
	public static DataTable DB_FlowComplete(String userNo, String flowNo, int pageSize, int pageIdx) throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			GenerWorkFlows ens = new GenerWorkFlows();
			QueryObject qo = new QueryObject(ens);
			if (flowNo != null) {
				qo.AddWhere(GenerWorkFlowAttr.FK_Flow, flowNo);
				qo.addAnd();
			}
			qo.AddWhere(GenerWorkFlowAttr.FID, 0);
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", " '%@" + userNo + "@%'");
			// *小周鹏修改-----------------------------START*
			// qo.DoQuery(GenerWorkFlowAttr.WorkID,pageSize, pageIdx);
			qo.DoQuery(GenerWorkFlowAttr.WorkID, pageSize, pageIdx, GenerWorkFlowAttr.RDT, true);
			// *小周鹏修改-----------------------------END*
			return ens.ToDataTableField();
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT *,FlowEndNode FK_Node FROM V_FlowData WHERE FlowEmps LIKE '%@" + userNo
					+ "%' AND   FID=0 AND WFState=" + WFState.Complete.getValue();

			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	/**
	 * 查询指定流程中已完成的流程
	 * 
	 * @param userNo
	 * @param pageCount
	 * @param pageSize
	 * @param pageIdx
	 * @param strFlow
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_FlowComplete(String userNo, int pageCount, int pageSize, int pageIdx, String strFlow)
			throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
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
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT *,FlowEndNode FK_Node FROM V_FlowData WHERE FK_Flow='" + strFlow
					+ "' AND FlowEmps LIKE '%@" + userNo + "%' AND FID=0 AND WFState=" + WFState.Complete.getValue();
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	/**
	 * 查询指定流程中已完成的公告流程
	 * 
	 * @param pageCount
	 *            页数
	 * @param pageSize
	 *            每页条数
	 * @param pageIdx
	 *            页码
	 * @param strFlow
	 *            流程编号
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_FlowComplete(String strFlow, int pageSize, int pageIdx) throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			GenerWorkFlows ens = new GenerWorkFlows();
			QueryObject qo = new QueryObject(ens);
			qo.AddWhere(GenerWorkFlowAttr.FID, 0);
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.FK_Flow, strFlow);
			qo.DoQuery(GenerWorkFlowAttr.WorkID, pageSize, pageIdx);
			return ens.ToDataTableField();
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT *,FlowEndNode FK_Node FROM V_FlowData WHERE FK_Flow='" + strFlow
					+ "' AND FID=0 AND WFState=" + WFState.Complete.getValue();
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	/**
	 * 获取已经完成流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_TongJi_FlowComplete() throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT T.FK_Flow as \"FK_Flow\", T.FlowName as \"FlowName\", COUNT(T.WorkID) as \"Num\" FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@"
					+ WebUser.getNo() + "@%' AND T.FID=0 AND T.WFSta=" + WFSta.Complete.getValue()
					+ " GROUP BY T.FK_Flow,T.FlowName";
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT T.FK_Flow as \"FK_Flow\", T.FlowName as \"FlowName\", COUNT(T.WorkID) as \"Num\" FROM V_FlowData T WHERE T.FlowEmps LIKE '%@"
					+ WebUser.getNo() + "@%'  AND T.FID=0 AND T.WFSta=" + WFSta.Complete.getValue()
					+ "   GROUP BY T.FK_Flow,T.FlowName";
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	/**
	 * 获取已经完成流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_FlowComplete() throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@" + WebUser.getNo()
					+ "@%' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
			//
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				dt.Columns.get("TYPE").setColumnName("Type");
				dt.Columns.get("WORKID").setColumnName("WorkID");
				dt.Columns.get("FK_FLOWSORT").setColumnName("FK_FlowSort");
				dt.Columns.get("SYSTYPE").setColumnName("SysType");
				dt.Columns.get("FK_FLOW").setColumnName("FK_Flow");
				dt.Columns.get("FLOWNAME").setColumnName("FlowName");

				dt.Columns.get("TITLE").setColumnName("Title");
				dt.Columns.get("WFSTA").setColumnName("WFSta");
				dt.Columns.get("WFSTATE").setColumnName("WFState");
				dt.Columns.get("STARTER").setColumnName("Starter");
				dt.Columns.get("STARTERNAME").setColumnName("StarterName");
				dt.Columns.get("SENDER").setColumnName("Sender");
				dt.Columns.get("FK_NODE").setColumnName("FK_Node");
				dt.Columns.get("NODENAME").setColumnName("NodeName");

				dt.Columns.get("FK_DEPT").setColumnName("FK_Dept");
				dt.Columns.get("DEPTNAME").setColumnName("DeptName");
				dt.Columns.get("SDTOFNODE").setColumnName("SDTOfNode");
				dt.Columns.get("SDTOFFLOW").setColumnName("SDTOfFlow");
				dt.Columns.get("PFLOWNO").setColumnName("PflowNo");
				dt.Columns.get("PWORKID").setColumnName("PWorkID");

				dt.Columns.get("PNODEID").setColumnName("PNodeID");
				dt.Columns.get("PEMP").setColumnName("PEmp");
				dt.Columns.get("GUESTNO").setColumnName("GuestNo");
				dt.Columns.get("GUESTNAME").setColumnName("GuestName");
				dt.Columns.get("BILLNO").setColumnName("BillNo");
				dt.Columns.get("FLOWNOTE").setColumnName("FlowNote");

				dt.Columns.get("TODOEMPS").setColumnName("TodoEmps");
				dt.Columns.get("TODOEMPSNUM").setColumnName("TodoEmpsNum");
				dt.Columns.get("TASKSTA").setColumnName("TaskSta");
				dt.Columns.get("ATPARA").setColumnName("AtPara");
				dt.Columns.get("EMPS").setColumnName("Emps");
				dt.Columns.get("DOMAIN").setColumnName("Domain");
				dt.Columns.get("SENDDT").setColumnName("SendDT");
				dt.Columns.get("WEEKNUM").setColumnName("WeekNum");
			}
			return dt;
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM V_FlowData T WHERE T.FlowEmps LIKE '%@" + WebUser.getNo()
					+ "@%' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY RDT DESC";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				dt.Columns.get("TYPE").setColumnName("Type");
				dt.Columns.get("WORKID").setColumnName("WorkID");
				dt.Columns.get("FK_FLOWSORT").setColumnName("FK_FlowSort");
				dt.Columns.get("SYSTYPE").setColumnName("SysType");
				dt.Columns.get("FK_FLOW").setColumnName("FK_Flow");
				dt.Columns.get("FLOWNAME").setColumnName("FlowName");

				dt.Columns.get("TITLE").setColumnName("Title");
				dt.Columns.get("WFSTA").setColumnName("WFSta");
				dt.Columns.get("WFSTATE").setColumnName("WFState");
				dt.Columns.get("STARTER").setColumnName("Starter");
				dt.Columns.get("STARTERNAME").setColumnName("StarterName");
				dt.Columns.get("SENDER").setColumnName("Sender");
				dt.Columns.get("FK_NODE").setColumnName("FK_Node");
				dt.Columns.get("NODENAME").setColumnName("NodeName");

				dt.Columns.get("FK_DEPT").setColumnName("FK_Dept");
				dt.Columns.get("DEPTNAME").setColumnName("DeptName");
				dt.Columns.get("SDTOFNODE").setColumnName("SDTOfNode");
				dt.Columns.get("SDTOFFLOW").setColumnName("SDTOfFlow");
				dt.Columns.get("PFLOWNO").setColumnName("PflowNo");
				dt.Columns.get("PWORKID").setColumnName("PWorkID");

				dt.Columns.get("PNODEID").setColumnName("PNodeID");
				dt.Columns.get("PEMP").setColumnName("PEmp");
				dt.Columns.get("GUESTNO").setColumnName("GuestNo");
				dt.Columns.get("GUESTNAME").setColumnName("GuestName");
				dt.Columns.get("BILLNO").setColumnName("BillNo");
				dt.Columns.get("FLOWNOTE").setColumnName("FlowNote");

				dt.Columns.get("TODOEMPS").setColumnName("TodoEmps");
				dt.Columns.get("TODOEMPSNUM").setColumnName("TodoEmpsNum");
				dt.Columns.get("TASKSTA").setColumnName("TaskSta");
				dt.Columns.get("ATPARA").setColumnName("AtPara");
				dt.Columns.get("EMPS").setColumnName("Emps");
				dt.Columns.get("DOMAIN").setColumnName("Domain");
				dt.Columns.get("SENDDT").setColumnName("SendDT");
				dt.Columns.get("WEEKNUM").setColumnName("WeekNum");
			}
			return dt;
		}
	}

	/**
	 * 获取某一个人已完成的工作
	 * 
	 * @param userNo
	 * @return
	 */
	public static DataTable DB_FlowComplete(String userNo) {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM WF_GenerWorkFlow T WHERE T.Emps LIKE '%@" + userNo
					+ "@%' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM V_FlowData T WHERE T.FlowEmps LIKE '%@" + userNo
					+ "@%' AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY RDT DESC";
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	/**
	 * 获取已经完成
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_FlowCompleteAndCC() throws Exception {
		DataTable dt = DB_FlowComplete();
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());

		try {
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		} catch (RuntimeException e) {

		}

		for (DataRow row : ccDT.Rows) {
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns) {
				for (DataColumn dtColumn : dt.Columns) {
					if (column.ColumnName == dtColumn.ColumnName) {
						newRow.setValue(column.ColumnName, row.getValue(dtColumn.ColumnName));
					}

				}

			}
			newRow.setValue("Type", "CC");
			dt.Rows.add(newRow);
		}
		/*
		 * dt.DefaultView.Sort = "RDT DESC"; return dt.DefaultView.ToTable();
		 */
		return dt;
	}

	public static DataTable DB_FlowComplete2(String fk_flow, String title) throws Exception {
		if (Glo.getIsDeleteGenerWorkFlow() == false) {
			// 如果不是删除流程注册表.
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (StringHelper.isNullOrEmpty(title)) {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo()
							+ "@%' AND FID=0 AND WFState=" + WFState.Complete.getValue()
							+ " and FK_Flow!='010' order by RDT desc";
				} else {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo()
							+ "@%' and Title Like '%" + title + "%' AND FID=0 AND WFState="
							+ WFState.Complete.getValue() + " and FK_Flow!='010' order by RDT desc";
				}
			} else {
				if (StringHelper.isNullOrEmpty(title)) {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo()
							+ "@%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " and FK_Flow='" + fk_flow
							+ "' order by RDT desc";
				} else {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM WF_GenerWorkFlow WHERE Emps LIKE '%@" + WebUser.getNo()
							+ "@%' and Title Like '%" + title + "%' AND FID=0 AND WFState="
							+ WFState.Complete.getValue() + " and FK_Flow='" + fk_flow + "' order by RDT desc";
				}
			}
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		} else {
			Paras ps = new Paras();
			String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (StringHelper.isNullOrEmpty(title)) {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM V_FlowData WHERE FlowEmps LIKE '%@" + WebUser.getNo()
							+ "%' AND FID=0 AND WFState=" + WFState.Complete.getValue()
							+ " and FK_Flow!='010' order by RDT desc";
				} else {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM V_FlowData WHERE FlowEmps LIKE '%@" + WebUser.getNo()
							+ "%' and Title Like '%" + title + "%' AND FID=0 AND WFState=" + WFState.Complete.getValue()
							+ " and FK_Flow!='010' order by RDT desc";
				}
			} else {
				if (StringHelper.isNullOrEmpty(title)) {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM V_FlowData WHERE FlowEmps LIKE '%@" + WebUser.getNo()
							+ "%' AND FID=0 AND WFState=" + WFState.Complete.getValue() + " and FK_Flow='" + fk_flow
							+ "' order by RDT desc";
				} else {
					ps.SQL = "SELECT 'RUNNING' AS Type,* FROM V_FlowData WHERE FlowEmps LIKE '%@" + WebUser.getNo()
							+ "%' and Title Like '%" + title + "%' AND FID=0 AND WFState=" + WFState.Complete.getValue()
							+ " and FK_Flow='" + fk_flow + "' order by RDT desc";
				}
			}
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}
	}

	public static DataTable DB_FlowCompleteAndCC2(String fk_flow, String title) throws Exception {
		DataTable dt = DB_FlowComplete2(fk_flow, title);
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());
		try {
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		} catch (RuntimeException e) {

		}

		for (DataRow row : ccDT.Rows) {
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns) {
				for (DataColumn dtColumn : dt.Columns) {
					if (column.ColumnName == dtColumn.ColumnName) {
						newRow.setValue(column.ColumnName, row.getValue(dtColumn.ColumnName));
						newRow.setValue(column.ColumnName, row.getValue(dtColumn.ColumnName));
					}

				}

			}
			newRow.setValue("Type", "CC");
			dt.Rows.add(newRow);
		}
		/*
		 * dt.DefaultView.Sort = "RDT DESC"; return dt.DefaultView.ToTable();
		 */
		return dt;
	}

	/**
	 * 获得任务池的工作列表
	 * 
	 * @return 任务池的工作列表
	 * @throws Exception
	 */
	public static DataTable DB_TaskPool() throws Exception {
		if (BP.WF.Glo.getIsEnableTaskPool() == false) {
			throw new RuntimeException("@你必须在Web.config中启用IsEnableTaskPool才可以执行此操作。");
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue()
				+ ") AND TaskSta=" + TaskSta.Sharing.getValue();
		String sql;
		String realSql = null;
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp ";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case SpecFlows:
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN "
					+ emp.getAuthorFlows() + " ";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case None:
			throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
		default:
			throw new RuntimeException("no such way...");
		}
		// @杜.翻译
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
		return dt;
	}

	/**
	 * 获得我从任务池里申请下来的工作列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_TaskPoolOfMyApply() throws Exception {
		if (BP.WF.Glo.getIsEnableTaskPool() == false) {
			throw new RuntimeException("@你必须在Web.config中启用IsEnableTaskPool才可以执行此操作。");
		}

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String wfSql = "  (WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
				+ " OR WFState=" + WFState.Shift.getValue() + " OR WFState=" + WFState.ReturnSta.getValue()
				+ ") AND TaskSta=" + TaskSta.Takeback.getValue();
		String sql;
		String realSql;
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			// ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND
			// FK_Emp=" + dbstr + "FK_Emp ORDER BY FK_Flow,ADT DESC ";
			// ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND
			// FK_Emp=" + dbstr + "FK_Emp ORDER BY ADT DESC ";
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp";

			// ps.SQL = "select v1.*,v2.name,v3.name as ParentName from (" +
			// realSql + ") as v1 left join JXW_Inc v2 on v1.WorkID=v2.OID left
			// join Jxw_Inc V3 on v1.PWorkID = v3.OID ORDER BY v1.ADT DESC";

			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			return BP.DA.DBAccess.RunSQLReturnTable(ps);
		}

		// 如果是授权状态, 获取当前委托人的信息.
		WFEmp emp = new WFEmp(WebUser.getNo());
		switch (emp.getHisAuthorWay()) {
		case All:
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND TaskSta=0";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case SpecFlows:
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp=" + dbstr + "FK_Emp AND  FK_Flow IN "
					+ emp.getAuthorFlows() + "";
			ps.Add("FK_Emp", BP.Web.WebUser.getNo());
			break;
		case None:
			throw new RuntimeException("对方(" + WebUser.getNo() + ")已经取消了授权.");
		default:
			throw new RuntimeException("no such way...");
		}
		// @杜.翻译
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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
		return dt;
	}

	/**
	 * 获得所有的流程挂起工作列表
	 * 
	 * @return 返回从视图WF_EmpWorks查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerHungUpList() throws Exception {
		return DB_GenerHungUpList(null);
	}

	/**
	 * 获得指定流程挂起工作列表
	 * 
	 * @param fk_flow
	 *            流程编号,如果编号为空则返回所有的流程挂起工作列表.
	 * @return 返回从视图WF_EmpWorks查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerHungUpList(String fk_flow) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		String sql;
		int state = WFState.HungUp.getValue();
		if (WebUser.getIsAuthorize()) {
			WFEmp emp = new WFEmp(WebUser.getNo());
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state
						+ " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND B.IsEnable=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			} else {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.FK_Flow='" + fk_flow
						+ "' AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND  B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
		} else {
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state
						+ " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1   ";
			} else {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND B.IsEnable=1 ";
			}
		}
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}

	/**
	 * 获得逻辑删除的流程
	 * 
	 * @return 返回从视图WF_EmpWorks查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerDeleteWorkList() throws Exception {
		return DB_GenerDeleteWorkList(WebUser.getNo(), null);
	}

	/**
	 * 获得逻辑删除的流程:根据流程编号
	 * 
	 * @param userNo
	 *            操作员编号
	 * @param fk_flow
	 *            流程编号(可以为空)
	 * @return WF_GenerWorkFlow数据结构的集合
	 * @throws Exception
	 */
	public static DataTable DB_GenerDeleteWorkList(String userNo, String fk_flow) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		String sql;
		int state = WFState.Delete.getValue();
		if (WebUser.getIsAuthorize()) {
			WFEmp emp = new WFEmp(WebUser.getNo());
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state
						+ " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND B.IsEnable=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			} else {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND  B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
		} else {
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.WFState=" + state
						+ " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1   ";
			} else {
				sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WFState=" + state + " AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND B.IsEnable=1 ";
			}
		}
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}

	/// #endregion 获取当前操作员的共享工作

	/// #region 获取流程数据
	/**
	 * 根据流程状态获取指定流程数据
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param sta
	 *            流程状态
	 * @return 数据表OID,Title,RDT,FID
	 * @throws Exception
	 */
	public static DataTable DB_NDxxRpt(String fk_flow, WFState sta) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		Flow fl = new Flow(fk_flow);
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql = "SELECT OID,Title,RDT,FID FROM " + fl.getPTable() + " WHERE WFState=" + sta.getValue()
				+ " AND Rec=" + dbstr + "Rec";
		BP.DA.Paras ps = new BP.DA.Paras();
		ps.SQL = sql;
		ps.Add("Rec", BP.Web.WebUser.getNo());
		return DBAccess.RunSQLReturnTable(ps);
	}

	/// #endregion

	/// #region 工作部件的数据源获取。
	/**
	 * 获取当前节点可以退回的节点
	 * 
	 * @param fk_node
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            FID
	 * @return No节点编号,Name节点名称,Rec记录人,RecName记录人名称
	 * @throws Exception
	 */
	public static DataTable DB_GenerWillReturnNodes(int fk_node, long workid, long fid) throws Exception {
		DataTable dt = new DataTable("obt");
		dt.Columns.Add("No", String.class); // 节点ID
		dt.Columns.Add("Name", String.class); // 节点名称.
		dt.Columns.Add("Rec", String.class); // 被退回节点上的操作员编号.
		dt.Columns.Add("RecName", String.class); // 被退回节点上的操作员名称.
		dt.Columns.Add("IsBackTracking", String.class); // 该节点是否可以退回并原路返回？ 0否,
														// 1是.

		Node nd = new Node(fk_node);
		// 增加退回到父流程节点的设计.
		if (nd.getIsStartNode() == true) {
			// *如果是开始的节点有可能退回到子流程上去.*/@du
			GenerWorkFlow gwf = new GenerWorkFlow(workid);
			if (gwf.getPWorkID() == 0)
				throw new RuntimeException("@当前节点是开始节点，您不能执行退回。");

			GenerWorkerLists gwls = new GenerWorkerLists();
			int i = gwls.Retrieve(GenerWorkerListAttr.WorkID, gwf.getPWorkID());

			String nodes = "";

			for (GenerWorkerList gwl : gwls.ToJavaList()) {

				if (nodes.contains(String.valueOf(gwl.getFK_Node()) + ",") == true)
					continue;

				nodes += String.valueOf(gwl.getFK_Node()) + ",";

				DataRow dr = dt.NewRow();
				dr.setValue("No", String.valueOf(gwl.getFK_Node()));
				dr.setValue("Name", gwl.getFK_NodeText());
				dr.setValue("Rec", gwl.getFK_Emp());
				dr.setValue("RecName", gwl.getFK_EmpText());
				dr.setValue("IsBackTracking", 0);

				dt.Rows.add(dr);

				// dt.Rows.Add(vals)
			}
			return dt;
		}

		if (nd.getHisRunModel() == RunModel.SubThread) {
			// 如果是子线程，它只能退回它的上一个节点，现在写死了，其它的设置不起作用了。
			Nodes nds = nd.getFromNodes();
			for (Node ndFrom : nds.ToJavaList()) {
				Work wk;
				switch (ndFrom.getHisRunModel()) {
				case FL:
				case FHL:
					wk = ndFrom.getHisWork();
					wk.setOID(fid);
					if (wk.RetrieveFromDBSources() == 0) {
						continue;
					}
					break;
				case SubThread:
					wk = ndFrom.getHisWork();
					wk.setOID(workid);
					if (wk.RetrieveFromDBSources() == 0) {
						continue;
					}
					break;
				case Ordinary:
				default:
					throw new RuntimeException("流程设计异常，子线程的上一个节点不能是普通节点。");
				}

				if (ndFrom.getNodeID() == fk_node) {
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue("No", String.valueOf(ndFrom.getNodeID()));
				dr.setValue("Name", ndFrom.getName());

				dr.setValue("Rec", wk.getRec());
				dr.setValue("RecName", wk.getRecText());

				if (ndFrom.getIsBackTracking()) {
					dr.setValue("IsBackTracking", 1);
				} else {
					dr.setValue("IsBackTracking", "0");
				}

				dt.Rows.add(dr);
			}
			if (dt.Rows.size() == 0) {
				throw new RuntimeException("没有获取到应该退回的节点列表.");
			}
			return dt;
		}

		String sql = "";

		WorkNode wn = new WorkNode(workid, fk_node);
		WorkNodes wns = new WorkNodes();
		switch (nd.getHisReturnRole()) {
		case CanNotReturn:
			return dt;
		case ReturnAnyNodes:
			if (nd.getIsHL() || nd.getIsFLHL()) {
				// 如果当前点是分流，或者是分合流，就不按退回规则计算了。
				sql = "SELECT a.FK_Node AS No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND a.FID="
						+ fid + " AND a.WorkID=" + workid + " AND a.FK_Node!=" + fk_node
						+ " AND a.IsPass=1 ORDER BY RDT  ";

			} else {

				if (nd.getTodolistModel() == TodolistModel.Order)
					sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND (a.WorkID="
							+ workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node!=" + fk_node + ") OR (a.FK_Node="
							+ fk_node + " AND a.IsPass <0)  ORDER BY a.RDT";
				else
					sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID="
							+ workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node!=" + fk_node + " ORDER BY a.RDT";
			}

			if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				DataTable dtt = DBAccess.RunSQLReturnTable(sql);
				dtt.Columns.clear();
				dtt.Columns.Add("No", String.class); // 节点ID
				dtt.Columns.Add("Name", String.class); // 节点名称.
				dtt.Columns.Add("Rec", String.class); // 被退回节点上的操作员编号.
				dtt.Columns.Add("RecName", String.class); // 被退回节点上的操作员名称.
				dtt.Columns.Add("IsBackTracking", String.class); // 该节点是否可以退回并原路返回？
																	// 0否, 1是.
				return dtt;
			} else {
				return DBAccess.RunSQLReturnTable(sql);
			}
		case ReturnPreviousNode:
			WorkNode mywnP = wn.GetPreviousWorkNode();

			if (nd.getIsHL() || nd.getIsFLHL()) {
				// 如果当前点是分流，或者是分合流，就不按退回规则计算了。
				sql = "SELECT a.FK_Node AS No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND a.FID="
						+ fid + " AND a.WorkID=" + workid + " AND a.FK_Node=" + mywnP.getHisNode().getNodeID()
						+ " AND a.IsPass=1 ORDER BY RDT  ";
				return DBAccess.RunSQLReturnTable(sql);
			}

			if (nd.getTodolistModel() == TodolistModel.Order) {
				sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a, WF_Node b WHERE a.FK_Node=b.NodeID AND (a.WorkID="
						+ workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node=" + mywnP.getHisNode().getNodeID()
						+ ") OR (a.FK_Node=" + mywnP.getHisNode().getNodeID() + " AND a.IsPass <0)  ORDER BY a.RDT";
			} else {
				sql = "SELECT a.FK_Node as No,a.FK_NodeText as Name, a.FK_Emp as Rec, a.FK_EmpText as RecName, b.IsBackTracking FROM WF_GenerWorkerlist a,WF_Node b WHERE a.FK_Node=b.NodeID AND a.WorkID="
						+ workid + " AND a.IsEnable=1 AND a.IsPass=1 AND a.FK_Node=" + mywnP.getHisNode().getNodeID()
						+ " ORDER BY a.RDT ";
			}

			if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				DataTable dtt = DBAccess.RunSQLReturnTable(sql);
				dtt.Columns.clear();
				dtt.Columns.Add("No", String.class); // 节点ID
				dtt.Columns.Add("Name", String.class); // 节点名称.
				dtt.Columns.Add("Rec", String.class); // 被退回节点上的操作员编号.
				dtt.Columns.Add("RecName", String.class); // 被退回节点上的操作员名称.
				dtt.Columns.Add("IsBackTracking", String.class); // 该节点是否可以退回并原路返回？
																	// 0否, 1是.
				return dtt;
			} else {
				return DBAccess.RunSQLReturnTable(sql);
			}
		case ReturnSpecifiedNodes: // 退回指定的节点。
			if (wns.size() == 0) {
				wns.GenerByWorkID(wn.getHisNode().getHisFlow(), workid);
			}

			NodeReturns rnds = new NodeReturns();
			rnds.Retrieve(NodeReturnAttr.FK_Node, fk_node);
			if (rnds.size() == 0) {
				throw new RuntimeException("@流程设计错误，您设置该节点可以退回指定的节点，但是指定的节点集合为空，请在节点属性设置它的制订节点。");
			}
			for (WorkNode mywn : wns) {
				if (mywn.getHisNode().getNodeID() == fk_node) {
					continue;
				}

				if (rnds.Contains(NodeReturnAttr.ReturnTo, mywn.getHisNode().getNodeID()) == false) {
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue("No", String.valueOf(mywn.getHisNode().getNodeID()));
				dr.setValue("Name", mywn.getHisNode().getName());
				dr.setValue("Rec", mywn.getHisWork().getRec());
				dr.setValue("RecName", mywn.getHisWork().getRecText());
				if (mywn.getHisNode().getIsBackTracking()) {
					dr.setValue("IsBackTracking", 1);
				} else {
					dr.setValue("IsBackTracking", "0");
				}

				dt.Rows.add(dr);
			}
			break;
		case ByReturnLine: // 按照流程图画的退回线执行退回.
			Directions dirs = new Directions();
			dirs.Retrieve(DirectionAttr.Node, fk_node);
			if (dirs.size() == 0) {
				throw new RuntimeException("@流程设计错误:当前节点没有画向后退回的退回线,更多的信息请参考退回规则.");
			}
			for (Direction dir : dirs.ToJavaList()) {
				Node toNode = new Node(dir.getToNode());
				sql = "SELECT a.FK_Emp,a.FK_EmpText FROM WF_GenerWorkerlist a, WF_Node b WHERE   a.FK_Node="
						+ toNode.getNodeID() + " AND a.WorkID=" + workid + " AND a.IsEnable=1 AND a.IsPass=1";
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
				if (dt1.Rows.size() == 0) {
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue("No", new Integer(toNode.getNodeID()));
				dr.setValue("Name", toNode.getName());
				dr.setValue("Rec", dt1.Rows.get(0).get(0));
				dr.setValue("RecName", dt1.Rows.get(0).get(1));

				if (toNode.getIsBackTracking() == true) {
					dr.setValue("IsBackTracking", 1);
				} else {
					dr.setValue("IsBackTracking", "0");
				}
				dt.Rows.add(dr);
			}
			break;
		default:
			throw new RuntimeException("@没有判断的退回类型。");
		}

		if (dt.Rows.size() == 0) {
			throw new RuntimeException("@没有计算出来要退回的节点，请管理员确认节点退回规则是否合理？当前节点名称:" + nd.getName() + ",退回规则:"
					+ nd.getHisReturnRole().toString());
		}
		return dt;
	}

	/**
	 * 获得指定节点的可以选择的接受人
	 * 
	 * @param fk_node
	 *            节点编号
	 * @return 返回No,Name,FK_Dept两个列.
	 * @throws Exception
	 */
	public static DataTable DB_SelectAccepter(int fk_node) throws Exception {
		Selector en = new Selector(fk_node);

		switch (en.getSelectorModel()) {
		case Dept:
			break;
		default:
			break;
		}

		return null;
	}

	/**
	 * 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成) 该接口为在途菜单提供数据,在在途工作中，可以执行撤销发送。
	 * 
	 * @param userNo
	 *            操作员
	 * @param fk_flow
	 *            流程编号
	 * @param isMyStarter
	 *            是否仅仅查询我发起的在途流程
	 * @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerRuning(String userNo, String fk_flow) throws Exception {
		return DB_GenerRuning(userNo, fk_flow, false);
	}

	public static DataTable DB_GenerRuning(String userNo, String fk_flow, boolean isMyStarter) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		// 获取用户当前所在的节点
		String currNode = "";
		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:
			currNode = "(SELECT FK_Node FROM (SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo()
					+ "' Order by RDT DESC ) WHERE rownum=1)";
			break;
		case MySQL:
			currNode = "(SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo()
					+ "' Order by RDT DESC LIMIT 1)";
			break;
		case MSSQL:
			currNode = "(SELECT TOP 1 FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo()
					+ "' Order by RDT DESC)";
			break;
		default:
			break;
		}

		if (WebUser.getIsAuthorize()) {
			WFEmp emp = new WFEmp(userNo);
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (isMyStarter == true) {
					ps.SQL = "SELECT DISTINCT a.*," + currNode
							+ "  AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND A.Starter="
							+ dbStr + "Starter  AND B.FK_Emp=" + dbStr
							+ "FK_Emp AND B.IsEnable=1 AND A.WFState !=3 AND (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN "
							+ emp.getAuthorFlows() + " Order by A.RDT DESC";
					ps.Add("Starter", userNo);
					ps.Add("FK_Emp", userNo);
				} else {
					ps.SQL = "SELECT DISTINCT a.*," + currNode
							+ "  AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
							+ dbStr
							+ "FK_Emp AND B.IsEnable=1 AND A.WFState !=3  AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN "
							+ emp.getAuthorFlows() + " Order by A.RDT DESC";
					ps.Add("FK_Emp", userNo);
				}
			} else {
				if (isMyStarter == true) {
					ps.SQL = "SELECT DISTINCT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE  A.FK_Flow="
							+ dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr
							+ "FK_Emp AND B.IsEnable=1  AND A.WFState !=3 AND  (B.IsPass=1 or B.IsPass < 0) AND  A.Starter="
							+ dbStr + "Starter AND A.FK_Flow IN " + emp.getAuthorFlows() + " Order by A.RDT DESC";
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
					ps.Add("Starter", userNo);
				} else {
					ps.SQL = "SELECT DISTINCT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow="
							+ dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr
							+ "FK_Emp AND B.IsEnable=1  AND A.WFState !=3 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN "
							+ emp.getAuthorFlows() + " Order by A.RDT DESC";
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
				}

			}
		} else {
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				if (isMyStarter == true) {
					ps.SQL = "SELECT DISTINCT a.*," + currNode
							+ "  AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
							+ dbStr
							+ "FK_Emp AND B.IsEnable=1  AND A.WFState !=3 AND  (B.IsPass=1 or B.IsPass < 0) AND  A.Starter="
							+ dbStr + "Starter  Order by A.RDT DESC";
					ps.Add("FK_Emp", userNo);
					ps.Add("Starter", userNo);
				} else {
					ps.SQL = "SELECT DISTINCT a.*," + currNode
							+ "  AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
							+ dbStr
							+ "FK_Emp AND B.IsEnable=1  AND A.WFState !=3 AND  (B.IsPass=1 or B.IsPass < 0)  Order by A.RDT DESC";
					ps.Add("FK_Emp", userNo);
				}
			} else {
				if (isMyStarter == true) {
					ps.SQL = "SELECT DISTINCT a.*," + currNode
							+ "  AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow=" + dbStr
							+ "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr
							+ "FK_Emp AND B.IsEnable=1  AND A.WFState !=3 AND (B.IsPass=1 or B.IsPass < 0 ) AND  A.Starter="
							+ dbStr + "Starter   Order by A.RDT DESC";
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
					ps.Add("Starter", userNo);
				} else {
					ps.SQL = "SELECT DISTINCT a.*," + currNode
							+ "  AS CurrNode FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow=" + dbStr
							+ "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr
							+ "FK_Emp AND B.IsEnable=1  AND A.WFState !=3 AND (B.IsPass=1 or B.IsPass < 0 )  Order by A.RDT DESC";
					ps.Add("FK_Flow", fk_flow);
					ps.Add("FK_Emp", userNo);
				}
			}
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
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

		}
		return dt;
	}

	/**
	 * 在途统计:用于流程查询
	 * 
	 * @return 返回 FK_Flow,FlowName,Num 三个列.
	 * @throws Exception
	 */
	public static DataTable DB_TongJi_Runing() throws Exception {
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (WebUser.getIsAuthorize()) {
			WFEmp emp = new WFEmp(BP.Web.WebUser.getNo());
			ps.SQL = "SELECT a.FK_Flow as \"FK_Flow\" ,a.FlowName as \"FlowName\", Count(a.WorkID) as \"Num\" FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
					+ dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN "
					+ emp.getAuthorFlows() + " GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		} else {
			ps.SQL = "SELECT a.FK_Flow  as \"FK_Flow\" ,a.FlowName as \"FlowName\", Count(a.WorkID) as \"Num\" FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
					+ dbStr
					+ "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0)  GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}

	/**
	 * 统计流程状态
	 * 
	 * @return 返回：流程类别编号，名称，流程编号，流程名称，TodoSta0代办中,TodoSta1预警中,TodoSta2预期中,TodoSta3已办结.
	 * @throws Exception
	 */
	public static DataTable DB_TongJi_TodoSta() throws Exception {
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (WebUser.getIsAuthorize()) {
			WFEmp emp = new WFEmp(BP.Web.WebUser.getNo());
			ps.SQL = "SELECT a.FK_Flow,a.FlowName, Count(a.WorkID) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
					+ dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) AND A.FK_Flow IN "
					+ emp.getAuthorFlows() + " GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		} else {
			ps.SQL = "SELECT a.FK_Flow,a.FlowName, Count(a.WorkID) as Num FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp="
					+ dbStr
					+ "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0)  GROUP BY A.FK_Flow, A.FlowName";
			ps.Add("FK_Emp", WebUser.getNo());
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}

	public static DataTable DB_GenerRuning2(String userNo, String fk_flow, String title) {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		String sql;
		int state = WFState.Runing.getValue();
		if (StringHelper.isNullOrEmpty(fk_flow)) {
			if (StringHelper.isNullOrEmpty(title)) {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='"
						+ userNo + "' AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) and A.FK_Flow!='010'";
			} else {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='"
						+ userNo
						+ "' AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < 0) and A.FK_Flow!='010' and A.Title Like '%"
						+ title + "%'";
			}
		} else {
			if (StringHelper.isNullOrEmpty(title)) {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + userNo
						+ "' AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < 0 )";
			} else {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + userNo
						+ "' AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < 0 ) and A.Title Like '%" + title + "%' ";
			}
		}

		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}

	/**
	 * 在途工作
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_GenerRuningV2() throws Exception {
		String userNo = WebUser.getNo();
		String fk_flow = null;
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		String sql;
		int state = WFState.Runing.getValue();
		if (WebUser.getIsAuthorize()) {
			WFEmp emp = new WFEmp(userNo);
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='"
						+ userNo + "' AND B.IsEnable=1 AND B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			} else {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
						+ "' AND B.IsEnable=1 AND B.IsPass=1 AND A.FK_Flow IN " + emp.getAuthorFlows();
			}
		} else {
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='"
						+ userNo + "' AND B.IsEnable=1 AND B.IsPass=1 ";
			} else {
				sql = "SELECT a.* FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
						+ "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + userNo + "' AND B.IsEnable=1 AND B.IsPass=1 ";
			}
		}
		return DBAccess.RunSQLReturnTable(sql);
	}

	/**
	 * 获取内部系统消息
	 * 
	 * @param myPK
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_GenerPopAlert(String type) throws Exception {
		String sql = "";
		if (type.equals("unRead")) {
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
					+ "* FROM Sys_SMS WHERE SendTo ='" + WebUser.getNo()
					+ "' AND (IsRead = 0 OR IsRead IS NULL)  ORDER BY RDT DESC";
		} else {
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
					+ "* FROM Sys_SMS WHERE SendTo ='" + WebUser.getNo() + "'  ORDER BY RDT DESC";
		}
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}

	/**
	 * 获取外部系统消息
	 * 
	 * @param type
	 * @param No
	 * @return
	 */
	public static DataTable DB_GenerPopAlert(String type, String No) {
		String sql = "";
		if (type.equals("unRead")) {
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
					+ "* FROM Sys_SMS WHERE SendTo ='" + No + "' AND (IsRead = 0 OR IsRead IS NULL)  ORDER BY RDT DESC";
		} else {
			sql = "SELECT LEFT(CONVERT(VARCHAR(20),RDT,120),10) AS SortRDT,Datepart(WEEKDAY, CONVERT(DATETIME,RDT)  + @@DateFirst - 1) AS WeekRDT,"
					+ "* FROM Sys_SMS WHERE SendTo ='" + No + "'  ORDER BY RDT DESC";
		}
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}

	/**
	 * 更新消息状态
	 * 
	 * @param myPK
	 */
	public static DataTable DB_GenerUpdateMsgSta(String myPK) {
		String sql = "";
		sql = " UPDATE Sys_SMS SET IsRead=1 WHERE MyPK='" + myPK + "'";
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}

	/**
	 * 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
	 * 
	 * @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerRuning() throws Exception {
		DataTable dt = DB_GenerRuning(BP.Web.WebUser.getNo(), null);
		/*
		 * dt.Columns.Add("Type");
		 * 
		 * for (DataRow row : dt.Rows) { row.setValue("Type", "RUNNING"); }
		 */

		// dt.DefaultView.Sort = "RDT DESC";
		// return dt.DefaultView.ToTable();
		return dt;
	}

	/**
	 * 获取某一个人的在途（参与、未完成的工作）
	 * 
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_GenerRuning(String userNo) throws Exception {
		DataTable dt = DB_GenerRuning(userNo, null);
		dt.Columns.Add("Type");

		for (DataRow row : dt.Rows) {
			row.setValue("Type", "RUNNING");
		}
		/*
		 * dt.DefaultView.Sort = "RDT DESC"; return dt.DefaultView.ToTable();
		 */
		return dt;
	}

	/**
	 * 把抄送的信息也发送
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_GenerRuningAndCC() throws Exception {
		DataTable dt = DB_GenerRuning();
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());
		try {
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		} catch (RuntimeException e) {

		}

		for (DataRow row : ccDT.Rows) {
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns) {
				for (DataColumn dtColumn : dt.Columns) {
					if (column.ColumnName == dtColumn.ColumnName) {
						newRow.setValue(column.ColumnName, row.getValue(dtColumn.ColumnName));
					}

				}

			}
			newRow.setValue("Type", "CC");
			dt.Rows.add(newRow);
		}
		/*
		 * dt.DefaultView.Sort = "RDT DESC"; return dt.DefaultView.ToTable();
		 */
		return dt;
	}

	/**
	 * 为什么需要这个接口？
	 * 
	 * @param name
	 * @param fk_flow
	 * @param title
	 * @return
	 */
	public static DataTable DB_GenerRuning3(String name, String fk_flow, String title) {
		DataTable dt = DB_GenerRuning2(name, fk_flow, title);

		dt.Columns.Add("Type");

		for (DataRow row : dt.Rows) {
			row.setValue("Type", "RUNNING");
		}

		// dt.DefaultView.Sort = "RDT DESC";
		// return dt.DefaultView.ToTable();
		return dt;
	}

	public static DataTable DB_GenerRuningAndCC2(String name, String fk_flow, String title) throws Exception {
		DataTable dt = DB_GenerRuning3(name, fk_flow, title);
		DataTable ccDT = DB_CCList_CheckOver(WebUser.getNo());
		try {
			dt.Columns.Add("MyPK");
			dt.Columns.Add("Sta");
		} catch (RuntimeException e) {

		}

		for (DataRow row : ccDT.Rows) {
			DataRow newRow = dt.NewRow();

			for (DataColumn column : ccDT.Columns) {
				for (DataColumn dtColumn : dt.Columns) {
					if (column.ColumnName == dtColumn.ColumnName) {
						newRow.setValue(column.ColumnName, row.getValue(dtColumn.ColumnName));
					}

				}

			}
			newRow.setValue("Type", "CC");
			dt.Rows.add(newRow);
		}
		/*
		 * dt.DefaultView.Sort = "RDT DESC"; return dt.DefaultView.ToTable();
		 */
		return dt;
	}

	/**
	 * 获取当前节点的批处理工作
	 * 
	 * @param FK_Node
	 * @return
	 * @throws Exception
	 */
	public static DataTable GetBatch(int FK_Node) throws Exception {

		BP.WF.Node nd = new BP.WF.Node(FK_Node);
		Flow fl = nd.getHisFlow();
		String fromTable = "";

		if (fl.getHisDataStoreModel() == DataStoreModel.ByCCFlow) {
			fromTable = nd.getPTable();
		} else {
			fromTable = fl.getPTable();
		}
		String sql = "SELECT a.*, b.Starter,b.Title as STitle,b.ADT,b.WorkID FROM " + fromTable
				+ " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID()
				+ " AND b.FK_Emp='" + WebUser.getNo() + "'";
		// string sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM
		// WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		return dt;
	}

	/**
	 * 用户登录,此方法是在开发者校验好用户名与密码后执行
	 * 
	 * @param userNo
	 *            用户名
	 * @throws Exception
	 */
	public static String Port_Login(String userNo) throws Exception {
		BP.Port.Emp emp = new BP.Port.Emp(userNo);
		emp.RetrieveFromDBSources();
		WebUser.SignInOfGener(emp, true);
		WebUser.setIsWap(false);
		return Port_GetSID(userNo);
	}

	/**
	 * 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 * 
	 * @param userNo
	 *            用户名
	 * @param SID
	 *            安全ID,请参考流程设计器操作手册
	 * @throws Exception
	 */
	public static void Port_Login(String userNo, String sid) throws Exception {

		if (userNo.equals(WebUser.getNo())) {
			return;
		}

		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database) {
			Paras ps = new Paras();
			ps.SQL = "SELECT SID FROM Port_Emp WHERE No=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("No", userNo);
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0) {
				throw new RuntimeException("用户不存在或者SID错误。");
			}

			if (!dt.Rows.get(0).get("SID").toString().equals(sid)) {
				throw new RuntimeException("用户不存在或者SID错误。");
			}
		}

		BP.Port.Emp emp = new BP.Port.Emp(userNo);
		WebUser.SignInOfGener(emp);
		WebUser.setIsWap(false);
		return;
	}

	/**
	 * 登录
	 * 
	 * @param userNo
	 *            人员编号
	 * @param userName
	 *            名称
	 * @param fk_dept
	 *            所在部门
	 * @param deptName
	 *            部门名称
	 * @return
	 * @throws Exception
	 */
	public static String Port_Login(String userNo, String userName, String deptNo, String deptName, String authNo,
			String authName) throws Exception {
		if (userName == null) {
			// 仅仅传递了人员编号，就按照人员来取.
			BP.Port.Emp emp = new BP.Port.Emp();
			emp.setNo(userNo);
			emp.RetrieveFromDBSources();
			WebUser.SignInOfGener(emp);
			WebUser.setIsWap(false);
			WebUser.setAuth(""); // 设置授权人为空.
			return Port_GetSID(userNo);
		}
		// 执行登录.
		BP.Web.WebUser.SignInOfGener2017(userNo, userName, deptNo, deptName, null, null);
		return null;
	}

	/**
	 * 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 * 
	 * @throws Exception
	 */
	public static String Port_Login(String userNo, boolean isRememberMe) throws Exception {
		isRememberMe = true;
		BP.Port.Emp emp = new BP.Port.Emp(userNo);
		WebUser.SignInOfGener(emp, isRememberMe);
		WebUser.setIsWap(false);
		WebUser.setAuth(""); // 设置授权人为空.
		return Port_GetSID(userNo);
	}

	/**
	 * 注销当前登录
	 */
	public static void Port_SigOut() {
		WebUser.Exit();
	}

	/**
	 * 获取未读的消息 用于消息提醒.
	 * 
	 * @param userNo
	 *            用户ID
	 */
	public static String Port_SMSInfo(String userNo) {
		Paras ps = new Paras();
		ps.SQL = "SELECT MyPK, EmailTitle  FROM sys_sms where SendTo=" + SystemConfig.getAppCenterDBVarStr()
				+ "SendTo AND IsAlert=0";
		ps.Add("SendTo", userNo);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		String strs = "";
		for (DataRow dr : dt.Rows) {
			strs += "@" + dr.getValue(0) + "=" + dr.getValue(1).toString();
		}
		ps = new Paras();
		ps.SQL = "UPDATE  sys_sms SET IsAlert=1 WHERE  SendTo=" + SystemConfig.getAppCenterDBVarStr()
				+ "SendTo AND IsAlert=0";
		ps.Add("SendTo", userNo);
		DBAccess.RunSQL(ps);
		return strs;
	}

	/**
	 * 发送消息
	 * 
	 * @param userNo
	 *            信息接收人
	 * @param msgTitle
	 *            标题
	 * @param msgDoc
	 *            内容
	 * @throws Exception
	 */
	public static void Port_SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag) throws Exception {
		Port_SendMsg(userNo, msgTitle, msgDoc, msgFlag, BP.WF.SMSMsgType.Self, null, 0, 0, 0);
	}

	/**
	 * 获取SID
	 * 
	 * @param userNo
	 *            用户编号
	 * @return SID
	 */
	public static String Port_GetSIDName(String userNo) {
		String info = "";
		Paras ps = new Paras();
		ps.SQL = "SELECT SID, Name FROM Port_Emp WHERE No=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "No";
		ps.Add("No", userNo);
		DataTable table = BP.DA.DBAccess.RunSQLReturnTable(ps);
		info = BP.Tools.FormatToJson.ToJson(table);
		return info;
	}

	/**
	 * 获取SID
	 * 
	 * @param userNo
	 *            用户编号
	 * @return SID
	 */
	public static String Port_GetSID(String userNo) {
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database) {
			Paras ps = new Paras();
			ps.SQL = "SELECT SID FROM Port_Emp WHERE No=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "No";
			ps.Add("No", userNo);
			String sid = BP.DA.DBAccess.RunSQLReturnString(ps);
			if (StringHelper.isNullOrEmpty(sid) == true) {
				try {
					// 判断是否更新的是用户表中的SID
					if (Glo.getUpdataSID().contains("UPDATE Port_Emp SET SID=") == true) {
						// 判断是否视图，如果为视图则不进行修改 @于庆海 需要翻译
						if (BP.DA.DBAccess.IsView("Port_Emp") == true) {
							return sid;
						}
					}
					sid = BP.DA.DBAccess.GenerGUID();
					ps.SQL = Glo.getUpdataSID();
					ps.Add("SID", sid);
					ps.Add("No", userNo);
					BP.DA.DBAccess.RunSQL(ps);
				} catch (java.lang.Exception e) {
					// throw new Exception("@可");
					// 这里有可能是更新失败，是因为用户连接的视图.
				}
			}
			return sid;
		}

		throw new RuntimeException("@没有判断的数据源模式...");
	}

	/**
	 * 验证用户的合法性
	 * 
	 * @param userNo
	 *            用户编号
	 * @param SID
	 *            密钥
	 * @return 是否匹配
	 */
	public static boolean Port_CheckUserLogin(String userNo, String SID) {
		return true;
	}

	/**
	 * 设置SID
	 * 
	 * @param userNo
	 *            用户编号
	 * @param sid
	 *            SID信息
	 * @return SID
	 */
	public static boolean Port_SetSID(String userNo, String sid) {
		// Paras ps = new Paras();
		// ps.SQL = "UPDATE Port_Emp SET SID=" +
		// BP.Sys.SystemConfig.getAppCenterDBVarStr() + "SID WHERE No=" +
		// BP.Sys.SystemConfig.getAppCenterDBVarStr() + "No";
		// ps.Add("SID", sid);
		// ps.Add("No", userNo);

		// 判断是否更新的是用户表中的SID
		if (Glo.getUpdataSID().contains("UPDATE Port_Emp SET SID=") == true) {
			// 判断是否视图，如果为视图则不进行修改 @于庆海 需要翻译
			if (BP.DA.DBAccess.IsView("Port_Emp") == true) {
				return false;
			}
		}

		String sql = "";
		sql = BP.Sys.SystemConfig.GetValByKey("UpdateSID", sql);
		if (sql.equals("")) {
			sql = "UPDATE Port_Emp SET SID=@SID WHERE No=@No";
		}

		sql = sql.replace("@SID", "'" + sid + "'");
		sql = sql.replace("@No", "'" + userNo + "'");

		if (BP.DA.DBAccess.RunSQL(sql) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 发送邮件与消息(如果传入4大流程参数将会增加一个工作链接)
	 * 
	 * @param userNo
	 *            信息接收人
	 * @param title
	 *            标题
	 * @param msgDoc
	 *            内容
	 * @param msgFlag
	 *            消息标志
	 * @param flowNo
	 *            流程编号
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            FID
	 * @throws Exception
	 */
	public static void Port_SendMsg(String userNo, String title, String msgDoc, String msgFlag, String msgType,
			String flowNo, long nodeID, long workID, long fid) throws Exception {
		if (workID != 0) {
			String url = Glo.getHostURL() + "WF/Do.htm?SID=" + userNo + "_" + workID + "_" + nodeID;
			url = url.replace("//", "/");
			url = url.replace("//", "/");
			msgDoc += " <hr>打开工作: " + url;
		}

		String para = "@FK_Flow=" + flowNo + "@WorkID=" + workID + "@FK_Node=" + nodeID + "@Sender="
				+ BP.Web.WebUser.getNo();
		BP.WF.SMS.SendMsg(userNo, title, msgDoc, msgFlag, msgType, para);
	}

	/**
	 * 发送邮件
	 * 
	 * @param mailAddress
	 *            邮件地址
	 * @param emilTitle
	 *            标题
	 * @param emailBody
	 *            内容
	 * @param msgType
	 *            消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...)
	 * @param msgGroupFlag
	 *            分组标记
	 * @param sender
	 *            发送人
	 * @param msgPK
	 *            消息唯一标记，防止发送重复.
	 */
	public static void Port_SendEmail(String mailAddress, String emilTitle, String emailBody, String msgType,
			String msgGroupFlag, String sender, String msgPK, String sendToEmpNo) {
		Port_SendEmail(mailAddress, emilTitle, emailBody, msgType, msgGroupFlag, sender, null, null);
	}

	public static void Port_SendEmail(String mailAddress, String emilTitle, String emailBody, String msgType,
			String msgGroupFlag, String sender, String msgPK, String sendToEmpNo, String paras) throws Exception {
		if (StringHelper.isNullOrEmpty(mailAddress)) {
			return;
		}
		SMS sms = new SMS();
		if (StringHelper.isNullOrEmpty(msgPK) == false) {
			// 如果有唯一标志,就判断是否有该数据，没有该数据就允许插入.
			if (sms.IsExit(SMSAttr.MyPK, msgPK) == true) {
				return;
			}
			sms.setMyPK(msgPK);
		} else {
			sms.setMyPK(DBAccess.GenerGUID());
		}

		sms.setHisEmailSta(MsgSta.UnRun);
		if (sender == null) {
			sms.setSender(WebUser.getNo());
		} else {
			sms.setSender(sender);
		}

		sms.setSendToEmpNo(sendToEmpNo);

		// 邮件地址.
		sms.setEmail(mailAddress);

		// 邮件标题.
		sms.setTitle(emilTitle);
		sms.setDocOfEmail(emailBody);

		// 手机状态禁用.
		sms.setHisMobileSta(MsgSta.Disable);

		// 其他属性.
		sms.setRDT(BP.DA.DataType.getCurrentDataTime());

		// 消息参数.
		sms.setAtPara(paras);

		sms.setMsgFlag(msgGroupFlag); // 消息标志.
		sms.setMsgType(msgType); // 消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...).
		sms.Insert();
	}

	/**
	 * 发送短信
	 * 
	 * @param tel
	 *            电话
	 * @param smsDoc
	 *            短信内容
	 * @param msgType
	 *            消息类型
	 * @param msgGroupFloag
	 *            消息分组
	 * @param sender
	 *            发送人
	 * @param msgPK
	 *            唯一标志,防止发送重复.
	 * @param sendEmpNo
	 *            发送给人员.
	 * @param atParas
	 *            参数.
	 * @throws Exception
	 */
	public static void Port_SendSMS(String tel, String smsDoc, String msgType, String msgGroupFlag, String sender,
			String msgPK, String sendToEmpNo, String atParas, String openUrl) throws Exception {
		// if (string.IsNullOrEmpty(tel))
		// return;

		SMS sms = new SMS();
		if (StringHelper.isNullOrEmpty(msgPK) == false) {
			// 如果有唯一标志,就判断是否有该数据，没有该数据就允许插入.
			if (sms.IsExit(SMSAttr.MyPK, msgPK) == true) {
				return;
			}
			sms.setMyPK(msgPK);
		} else {
			sms.setMyPK(DBAccess.GenerGUID());
		}

		sms.setHisEmailSta(MsgSta.Disable);
		sms.setHisMobileSta(MsgSta.UnRun);

		if (sender == null) {
			sms.setSender(WebUser.getNo());
		} else {
			sms.setSender(sender);
		}
		// 发送给人员ID , 有可能这个人员空的.
		sms.setSendToEmpNo(sendToEmpNo);

		sms.setMobile(tel);
		sms.setMobileInfo(smsDoc);

		// 其他属性.
		sms.setRDT(BP.DA.DataType.getCurrentDataTime());

		sms.setMsgType(msgType); // 消息类型.

		sms.setMsgFlag(msgGroupFlag); // 消息分组标志,用于批量删除.

		sms.setAtPara(atParas);
		sms.SetPara("OpenUrl", openUrl);

		// 先保留本机一份.
		sms.Insert();
	}

	/**
	 * 转化流程Code到流程编号
	 * 
	 * @param FlowMark
	 *            流程编号
	 * @return 返回编码
	 */
	public static String TurnFlowMarkToFlowNo(String flowMark) {
		if (StringHelper.isNullOrEmpty(flowMark)) {
			return "";
		}

		// 如果是编号，就不用转化.
		if (DataType.IsNumStr(flowMark)) {
			return flowMark;
		}

		String s = DBAccess.RunSQLReturnStringIsNull("SELECT No FROM WF_Flow WHERE FlowMark='" + flowMark + "'", null);
		if (s == null) {
			throw new RuntimeException("@FlowMark错误:" + flowMark + ",没有找到它的流程编号.");
		}
		return s;
	}

	/**
	 * 获取最新的消息
	 * 
	 * @param userNo
	 *            用户编号
	 * @param dateLastTime
	 *            上次获取的时间
	 * @return 返回消息：返回两个列的数据源MsgType,Num.
	 */
	public static DataTable Port_GetNewMsg(String userNo, String dateLastTime) {
		Paras ps = new Paras();
		ps.SQL = "SELECT MsgType , Count(*) as Num FROM Sys_SMS WHERE SendTo="
				+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "SendTo AND RDT >"
				+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "RDT Group By MsgType";
		ps.Add(BP.WF.SMSAttr.SendTo, userNo);
		ps.Add(BP.WF.SMSAttr.RDT, dateLastTime);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		return dt;
	}

	/**
	 * 获取最新的消息
	 * 
	 * @param userNo
	 *            用户编号
	 * @return
	 */
	public static DataTable Port_GetNewMsg(String userNo) {
		Paras ps = new Paras();
		ps.SQL = "SELECT MsgType , Count(*) as Num FROM Sys_SMS WHERE SendTo="
				+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "SendTo  Group By MsgType";
		ps.Add(BP.WF.SMSAttr.SendTo, userNo);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		return dt;
	}

	/**
	 * 写入日志
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeFrom
	 *            节点从
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            FID
	 * @param msg
	 *            信息
	 * @param at
	 *            活动类型
	 * @param tag
	 *            参数:用@符号隔开比如, @PWorkID=101@PFlowNo=003
	 * @param cFlowInfo
	 *            子流程信息
	 * @throws Exception
	 */
	public static void WriteTrack(String flowNo, int nodeFrom, long workid, long fid, String msg, ActionType at,
			String tag, String cFlowInfo, String optionMsg) throws Exception {
		if (at == ActionType.CallChildenFlow) {
			if (StringHelper.isNullOrEmpty(cFlowInfo) == true) {
				throw new RuntimeException("@必须输入信息cFlowInfo信息,在 CallChildenFlow 模式下.");
			}
		}

		if (StringHelper.isNullOrEmpty(optionMsg)) {
			optionMsg = Track.GetActionTypeT(at);
		}

		Track t = new Track();
		t.setWorkID(workid);
		t.setFID(fid);
		t.setRDT(DataType.getCurrentDataTime());
		t.setHisActionType(at);
		t.setActionTypeText(optionMsg);

		Node nd = new Node(nodeFrom);
		t.setNDFrom(nodeFrom);
		t.setNDFromT(nd.getName());

		t.setEmpFrom(WebUser.getNo());
		t.setEmpFromT(WebUser.getName());
		t.FK_Flow = flowNo;

		t.setNDTo(nodeFrom);
		t.setNDToT(nd.getName());

		t.setEmpTo(WebUser.getNo());
		t.setEmpToT(WebUser.getName());
		t.setMsg(msg);

		if (tag != null) {
			t.setTag(tag);
		}

		try {
			t.Insert();
		} catch (java.lang.Exception e) {
			t.CheckPhysicsTable();
			t.Insert();
			t.DirectInsert();
		}

		/// #region 特殊判断.
		if (at == ActionType.CallChildenFlow) {
			/* 如果是吊起子流程，就要向它父流程信息里写数据，让父流程可以看到能够发起那些流程数据。 */
			AtPara ap = new AtPara(tag);
			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(ap.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID));
			t.setWorkID(gwf.getWorkID());

			nd = new Node(gwf.getFK_Node());
			t.setNDFrom(gwf.getFK_Node());
			t.setNDFromT(nd.getName());

			t.setNDTo(t.getNDFrom());
			t.setNDToT(t.getNDFromT());

			t.FK_Flow = gwf.getFK_Flow();

			t.setHisActionType(ActionType.StartChildenFlow);
			t.setTag("@CWorkID=" + workid + "@CFlowNo=" + flowNo);
			t.setMsg(cFlowInfo);
			t.Insert();
		}
		/// #endregion 特殊判断.
	}

	public static void WriteTrack(String flowNo, int nodeFrom, long workid, long fid, String msg, ActionType at,
			String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo) throws Exception {
		if (at == ActionType.CallChildenFlow) {
			if (StringHelper.isNullOrEmpty(cFlowInfo) == true) {
				throw new RuntimeException("@必须输入信息cFlowInfo信息,在 CallChildenFlow 模式下.");
			}
		}

		if (StringHelper.isNullOrEmpty(optionMsg)) {
			optionMsg = Track.GetActionTypeT(at);
		}

		Track t = new Track();
		t.setWorkID(workid);
		t.setFID(fid);
		t.setRDT(DataType.getCurrentDataTimess());
		t.setHisActionType(at);
		t.setActionTypeText(optionMsg);

		Node nd = new Node(nodeFrom);
		t.setNDFrom(nodeFrom);
		t.setNDFromT(nd.getName());

		t.setEmpFrom(WebUser.getNo());
		t.setEmpFromT(WebUser.getName());
		t.FK_Flow = flowNo;

		t.setNDTo(nodeFrom);
		t.setNDToT(nd.getName());

		if (empNoTo == null) {
			t.setEmpTo(WebUser.getNo());
			t.setEmpToT(WebUser.getName());
		} else {
			t.setEmpTo(empNoTo);
			t.setEmpToT(empNameTo);
		}

		t.setMsg(msg);

		if (tag != null) {
			t.setTag(tag);
		}

		try {
			t.Insert();
		} catch (java.lang.Exception e) {
			t.CheckPhysicsTable();
			t.Insert();
			t.DirectInsert();
		}
		if (at == ActionType.CallChildenFlow) {
			// 如果是吊起子流程，就要向它父流程信息里写数据，让父流程可以看到能够发起那些流程数据。
			AtPara ap = new AtPara(tag);
			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(ap.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID));
			t.setWorkID(gwf.getWorkID());

			nd = new Node(gwf.getFK_Node());
			t.setNDFrom(gwf.getFK_Node());
			t.setNDFromT(nd.getName());

			t.setNDTo(t.getNDFrom());
			t.setNDToT(t.getNDFromT());

			t.FK_Flow = gwf.getFK_Flow();

			t.setHisActionType(ActionType.StartChildenFlow);
			t.setTag("@CWorkID=" + workid + "@CFlowNo=" + flowNo);
			t.setMsg(cFlowInfo);
			t.Insert();
		}
	}

	/**
	 * 写入日志
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeFrom
	 *            节点从
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            fID
	 * @param msg
	 *            信息
	 * @throws Exception
	 */
	public static void WriteTrackInfo(String flowNo, int nodeFrom, String ndFromName, long workid, long fid, String msg,
			String optionMsg) throws Exception {
		WriteTrack(flowNo, nodeFrom, workid, fid, msg, ActionType.Info, null, null, optionMsg);
	}

	/**
	 * 写入工作审核日志:
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param currNodeID
	 *            当前节点ID
	 * @param workid
	 *            工作ID
	 * @param FID
	 *            FID
	 * @param msg
	 *            审核信息
	 * @param optionName
	 *            操作名称(比如:科长审核、部门经理审批),如果为空就是"审核".
	 * @throws Exception
	 */
	public static void WriteTrackWorkCheck(String flowNo, int currNodeID, long workid, long fid, String msg,
			String optionName) throws Exception {

		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		gwf.RetrieveFromDBSources();

		// 求主键 2017.10.6以前的逻辑.
		String tag = currNodeID + "_" + workid + "_" + fid + "_" + BP.Web.WebUser.getNo();

		// 求当前是否是会签. zhangsan,张三;李四;王五;
		String nodeName = gwf.getNodeName();
		Node nd = new Node(currNodeID);
		if (nd.getIsStartNode() == false) {
			if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
				nodeName = nd.getName() + "(会签)";
		}

		// 待办抢办模式，一个节点只能有一条记录.
		Paras ps = new Paras();
		if (nd.getTodolistModel() == TodolistModel.QiangBan || nd.getTodolistModel() == TodolistModel.Sharing) {
			// 先删除其他人员写入的数据. 此脚本是2016.11.30号的,为了解决柳州的问题，需要扩展.
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  WorkID=" + dbStr
					+ "WorkID  AND NDFrom=" + dbStr + "NDFrom AND ActionType=" + ActionType.WorkCheck.getValue();
			ps.Add(TrackAttr.WorkID, workid);
			ps.Add(TrackAttr.NDFrom, currNodeID);
			DBAccess.RunSQL(ps);

			BP.WF.Dev2Interface.WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg, ActionType.WorkCheck, tag,
					null, optionName, null, null, null, null, null);
			// 写入日志.
			// WriteTrackAdv(flowNo, currNodeID, nodeName, workid, fid, msg,
			// ActionType.WorkCheck, tag, null, optionName);
			return;
		}

		ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET NDFromT=" + dbStr + "NDFromT, Msg=" + dbStr
				+ "Msg, RDT=" + dbStr + "RDT WHERE  Tag=" + dbStr + "Tag ";
		ps.Add(TrackAttr.NDFromT, nodeName);
		ps.Add(TrackAttr.Msg, msg);
		ps.Add(TrackAttr.Tag, tag);
		ps.Add(TrackAttr.RDT, DataType.getCurrentDataTimess());
		int num = DBAccess.RunSQL(ps);

		if (num > 1) {
			ps.clear();
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  Tag=" + dbStr + "Tag ";
			ps.Add(TrackAttr.Tag, tag);
			DBAccess.RunSQL(ps);
			num = 0;
		}

		if (num == 0) {
			// 如果没有更新到，就写入.
			// WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg,
			// ActionType.WorkCheck, tag, null, optionName);
			BP.WF.Dev2Interface.WriteTrack(flowNo, currNodeID, nodeName, workid, fid, msg, ActionType.WorkCheck, tag,
					null, optionName, null, null, null, null, null);
		}

	}

	public static void WriteTrack(String flowNo, int nodeFromID, String nodeFromName, long workid, long fid, String msg,
			ActionType at, String tag, String cFlowInfo, String optionMsg, String empNoTo, String empNameTo,
			String empNoFrom, String empNameFrom, String rdt) throws Exception {

		if (DataType.IsNullOrEmpty(optionMsg))
			optionMsg = Track.GetActionTypeT(at);

		if (DataType.IsNullOrEmpty(optionMsg))
			optionMsg = Track.GetActionTypeT(at);

		Track t = new Track();
		t.setWorkID(workid);
		t.setFID(fid);

		// 记录日期.
		DateTime d;
		if (DataType.IsNullOrEmpty(rdt))
			t.setRDT(DataType.getCurrentDataTimess());
		else
			t.setRDT(rdt);

		t.setHisActionType(at);
		t.setActionTypeText(optionMsg);

		// Node nd = new Node(nodeFrom);
		t.setNDFrom(nodeFromID);
		t.setNDFromT(nodeFromName);

		if (empNoFrom == null)
			t.setEmpFrom(WebUser.getNo());
		else
			t.setEmpFrom(empNoFrom);

		if (empNameFrom == null)
			t.setEmpFromT(WebUser.getName());
		else
			t.setEmpFromT(empNameFrom);

		t.setFK_Flow(flowNo);

		t.setNDTo(nodeFromID);
		t.setNDToT(nodeFromName);

		if (empNoTo == null) {
			t.setEmpTo(WebUser.getNo());
			t.setEmpToT(WebUser.getName());
		} else {
			t.setEmpTo(empNoTo);
			t.setEmpToT(empNameTo);
		}

		t.setMsg(msg);

		if (tag != null)
			t.setTag(tag);

		try {
			t.Insert();
		} catch (Exception ex) {
			t.CheckPhysicsTable();
			t.Insert();
			// t.DirectInsert();
		}

		// #region 特殊判断.
		if (at == ActionType.CallChildenFlow) {
			/* 如果是吊起子流程，就要向它父流程信息里写数据，让父流程可以看到能够发起那些流程数据。 */
			AtPara ap = new AtPara(tag);
			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(ap.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID));
			t.setWorkID(gwf.getWorkID());

			t.setNDFrom(gwf.getFK_Node());
			t.setNDFromT(gwf.getNodeName());

			t.setNDTo(t.getNDFrom());
			t.setNDToT(t.getNDFromT());

			t.setFK_Flow(gwf.getFK_Flow());

			t.setHisActionType(ActionType.StartChildenFlow);
			t.setTag("@CWorkID=" + workid + "@CFlowNo=" + flowNo);
			t.setMsg(cFlowInfo);
			t.Insert();
		}
		// #endregion 特殊判断.

	}

	/**
	 * 写入日志组件
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeFrom
	 * @param workid
	 * @param fid
	 * @param msg
	 * @param optionName
	 * @throws Exception
	 */
	public static void WriteTrackDailyLog(String flowNo, int nodeFrom, long workid, long fid, String msg,
			String optionName) throws Exception {
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String today = BP.DA.DataType.getCurrentDate();

		Paras ps = new Paras();
		ps.SQL = "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET Msg=" + dbStr + "Msg WHERE  RDT LIKE '" + today
				+ "%' AND WorkID=" + dbStr + "WorkID  AND NDFrom=" + dbStr + "NDFrom AND EmpFrom=" + dbStr
				+ "EmpFrom AND ActionType=" + ActionType.WorkCheck.getValue();
		ps.Add(TrackAttr.Msg, msg);
		ps.Add(TrackAttr.WorkID, workid);
		ps.Add(TrackAttr.NDFrom, nodeFrom);
		ps.Add(TrackAttr.EmpFrom, WebUser.getNo());
		if (DBAccess.RunSQL(ps) == 0) {
			// 如果没有更新到，就写入.
			WriteTrack(flowNo, nodeFrom, workid, fid, msg, ActionType.WorkCheck, null, null, optionName);
		}
	}

	/**
	 * 写入周报组件 一旦写入数据,只可更新 每周一次 qin
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeFrom
	 * @param workid
	 * @param fid
	 * @param msg
	 * @param optionName
	 */
	public static void WriteTrackWeekLog(String flowNo, int nodeFrom, long workid, long fid, String msg,
			String optionName) {
		/*
		 * String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		 * 
		 * java.util.Date dTime = new java.util.Date(); java.util.Date startWeek
		 * = dTime.AddDays(1 - Integer.parseInt(String.format("%d",
		 * dTime.DayOfWeek))); //本周第一天
		 * 
		 * java.util.Hashtable ht = new java.util.Hashtable();
		 * //当前日期所属的week包含哪些日期 for (int i = 1; i < 7; i++) { ht.put(i + 1,
		 * startWeek.AddDays(i).ToString("yyyy-MM-dd")); } ht.put(1,
		 * startWeek.ToString("yyyy-MM-dd"));
		 * 
		 * boolean isExitWeek = false; //本周是否已经有插入数据 String insertDate = null;
		 * DataTable dt; String sql = null;
		 * 
		 * for (DictionaryEntry de : ht) { sql = "SELECT * FROM ND" +
		 * Integer.parseInt(flowNo) + "Track  WHERE  RDT LIKE '" +
		 * de.getValue().toString() + "%' AND WorkID=" + workid +
		 * "  AND NDFrom='" + nodeFrom + "' AND EmpFrom='" + WebUser.getNo() +
		 * "' AND ActionType=" + ActionType.WorkCheck.getValue();
		 * 
		 * if (DBAccess.RunSQLReturnCOUNT(sql) != 0) { isExitWeek = true;
		 * insertDate = de.getValue().toString(); break; } }
		 * 
		 * //如果本周已经插入了记录，那么更新 if (isExitWeek) { Paras ps = new Paras(); ps.SQL =
		 * "UPDATE  ND" + Integer.parseInt(flowNo) + "Track SET RDT='" + new
		 * java.util.Date().ToString("yyyy-MM-dd HH:mm:ss") + "',Msg=" + dbStr +
		 * "Msg WHERE  RDT LIKE '" + insertDate + "%' AND WorkID=" + dbStr +
		 * "WorkID  AND NDFrom=" + dbStr + "NDFrom AND EmpFrom=" + dbStr +
		 * "EmpFrom AND ActionType=" + ActionType.WorkCheck.getValue();
		 * ps.Add(TrackAttr.Msg, msg); ps.Add(TrackAttr.WorkID, workid);
		 * ps.Add(TrackAttr.NDFrom, nodeFrom); ps.Add(TrackAttr.EmpFrom,
		 * WebUser.getNo());
		 * 
		 * DBAccess.RunSQL(ps); } else { WriteTrack(flowNo, nodeFrom, workid,
		 * fid, msg, ActionType.WorkCheck, null, null, optionName); }
		 */
	}

	/**
	 * 写入月报组件 同周报一样每月一条记录 qin
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeFrom
	 * @param workid
	 * @param fid
	 * @param msg
	 * @param optionName
	 */
	public static void WriteTrackMonthLog(String flowNo, int nodeFrom, long workid, long fid, String msg,
			String optionName) {
		/*
		 * String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr(); String
		 * today = BP.DA.DataType.getCurrentData();
		 * 
		 * java.util.Date dTime = new java.util.Date(); java.util.Date startDay
		 * = dTime.AddDays(1 - dTime.getDay()); //本月第一天
		 * 
		 * int days = java.util.Date.DaysInMonth(dTime.Year, dTime.Month);
		 * java.util.Hashtable ht = new java.util.Hashtable();
		 * 
		 * for (int i = 1; i < days; i++) { ht.put(i + 1,
		 * startDay.AddDays(i).ToString("yyyy-MM-dd")); } ht.put(1,
		 * startDay.ToString("yyyy-MM-dd"));
		 * 
		 * boolean isExitMonth = false; //本月是否已经有插入数据 String insertDate = null;
		 * DataTable dt; String sql = null;
		 * 
		 * for (DictionaryEntry de : ht) { sql = "SELECT * FROM ND" +
		 * Integer.parseInt(flowNo) + "Track  WHERE  RDT LIKE '" +
		 * de.getValue().toString() + "%' AND WorkID=" + workid +
		 * "  AND NDFrom='" + nodeFrom + "' AND EmpFrom='" + WebUser.getNo() +
		 * "' AND ActionType=" + ActionType.WorkCheck.getValue();
		 * 
		 * if (DBAccess.RunSQLReturnCOUNT(sql) != 0) { isExitMonth = true;
		 * insertDate = de.getValue().toString(); break; } }
		 * 
		 * if (isExitMonth) { Paras ps = new Paras(); ps.SQL = "UPDATE  ND" +
		 * Integer.parseInt(flowNo) + "Track SET RDT='" + new
		 * java.util.Date().ToString("yyyy-MM-dd HH:mm:ss") + "' Msg=" + dbStr +
		 * "Msg WHERE  RDT LIKE '" + insertDate + "%' AND WorkID=" + dbStr +
		 * "WorkID  AND NDFrom=" + dbStr + "NDFrom AND EmpFrom=" + dbStr +
		 * "EmpFrom AND ActionType=" + ActionType.WorkCheck.getValue();
		 * ps.Add(TrackAttr.Msg, msg); ps.Add(TrackAttr.WorkID, workid);
		 * ps.Add(TrackAttr.NDFrom, nodeFrom); ps.Add(TrackAttr.EmpFrom,
		 * WebUser.getNo());
		 * 
		 * DBAccess.RunSQL(ps); } else { WriteTrack(flowNo, nodeFrom, workid,
		 * fid, msg, ActionType.WorkCheck, null, null, optionName); }
		 */
	}

	/**
	 * 修改审核信息标识 比如：在默认的情况下是"审核"，现在要把ActionTypeText 修改成"组长审核。"。
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param nodeFrom
	 *            节点ID
	 * @param label
	 *            要修改成的标签
	 * @return 是否成功
	 */
	public static boolean WriteTrackWorkCheckLabel(String flowNo, long workid, int nodeFrom, String label) {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT MyPK FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND WorkID=" + workid
				+ " And NDTo='0' ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			return false;
		}

		String pk = dt.Rows.get(0).getValue(0).toString();
		sql = "UPDATE " + table + " SET " + TrackAttr.ActionTypeText + "='" + label + "' WHERE MyPK=" + pk;
		BP.DA.DBAccess.RunSQL(sql);
		return true;
	}

	/**
	 * 前进,获取等标签 比如：在默认的情况下是"逻辑删除"，现在要把ActionTypeText 修改成"删除(清场)。"。
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param nodeFrom
	 *            节点ID
	 * @param label
	 *            要修改成的标签
	 * @return 是否成功
	 */
	public static boolean WriteTrackLabel(String flowNo, long workid, int nodeFrom, String label) {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT MyPK FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND WorkID=" + workid
				+ "  ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			return false;
		}

		String pk = dt.Rows.get(0).getValue(0).toString();
		sql = "UPDATE " + table + " SET " + TrackAttr.ActionTypeText + "='" + label + "' WHERE MyPK=" + pk;
		BP.DA.DBAccess.RunSQL(sql);
		return true;
	}

	/**
	 * 获取Track 表中的审核的信息
	 * 
	 * @param flowNo
	 * @param workId
	 * @param nodeFrom
	 * @return
	 * @throws Exception
	 */
	public static String GetCheckInfo(String flowNo, long workId, int nodeFrom) throws Exception {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Msg FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType="
				+ ActionType.WorkCheck.getValue() + " AND EmpFrom='" + WebUser.getNo() + "' AND WorkID=" + workId
				+ " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			// BP.Sys.FrmWorkCheck fwc = new FrmWorkCheck(nodeFrom);
			// return fwc.FWCDefInfo;
			return null;
		}
		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		return checkinfo;
	}

	public static String GetCheckInfo(String flowNo, long workId, int nodeFrom, String isNullAsVal) throws Exception {
		String s = GetCheckInfo(flowNo, workId, nodeFrom);
		if (DataType.IsNullOrEmpty(s) == true)
			return isNullAsVal;
		return s;
	}

	/**
	 * 获取队列节点Track 表中的审核的信息(队列节点中处理人 共享同一处理意见)
	 * 
	 * @param flowNo
	 * @param workId
	 * @param nodeFrom
	 * @return
	 */
	public static String GetOrderCheckInfo(String flowNo, long workId, int nodeFrom) {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Msg FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType="
				+ ActionType.WorkCheck.getValue() + " AND WorkID=" + workId + " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			// BP.Sys.FrmWorkCheck fwc = new FrmWorkCheck(nodeFrom);
			// return fwc.FWCDefInfo;
			return null;
		}
		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		return checkinfo;
	}

	/**
	 * 删除审核信息,用于退回后.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workId
	 *            工作ID
	 * @param nodeFrom
	 *            节点从
	 * @return
	 * @throws Exception
	 */
	public static void DeleteCheckInfo(String flowNo, long workId, int nodeFrom) throws Exception {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "DELETE FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType="
				+ ActionType.WorkCheck.getValue() + " AND EmpFrom='" + WebUser.getNo() + "' AND WorkID=" + workId;
		BP.DA.DBAccess.RunSQL(sql);
	}

	public static String GetAskForHelpReInfo(String flowNo, long workId, int nodeFrom) throws Exception {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";
		String sql = "SELECT Msg FROM " + table + " WHERE NDFrom=" + nodeFrom + " AND ActionType="
				+ ActionType.AskforHelp.getValue() + " AND EmpFrom='" + WebUser.getNo() + "' AND WorkID=" + workId
				+ " ORDER BY RDT DESC ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			return "";
		}
		String checkinfo = dt.Rows.get(0).getValue(0).toString();
		return checkinfo;
	}

	/**
	 * 更新Track信息
	 * 
	 * @param flowNo
	 * @param workId
	 * @param nodeFrom
	 * @param actionType
	 * @param strMsg
	 * @return
	 */
	public static void SetTrackInfo(String flowNo, long workId, int nodeFrom, int actionType, String strMsg) {
		String table = "ND" + Integer.parseInt(flowNo) + "Track";

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + table + " SET Msg=" + dbstr + "Msg  WHERE ActionType=" + dbstr + "ActionType and WorkID="
				+ dbstr + "WorkID and NDFrom=" + dbstr + "NDFrom";
		ps.Add("Msg", strMsg);
		ps.Add("ActionType", actionType);
		ps.Add("WorkID", workId);
		ps.Add("NDFrom", nodeFrom);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/**
	 * 设置BillNo信息
	 * 
	 * @param flowNo
	 * @param workID
	 * @param newBillNo
	 * @throws Exception
	 */
	public static void SetBillNo(String flowNo, long workID, String newBillNo) throws Exception {
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
	 * 设置父流程信息
	 * 
	 * @param subFlowNo
	 *            子流程编号
	 * @param subFlowWorkID
	 *            子流程workid
	 * @param parentFlowNo
	 *            父流程编号
	 * @param parentWorkID
	 *            父流程WorkID
	 * @param parentNodeID
	 *            调用子流程的节点ID
	 * @param parentEmpNo
	 *            调用人
	 * @throws Exception
	 */
	public static void SetParentInfo(String subFlowNo, long subFlowWorkID, String parentFlowNo, long parentWorkID,
			int parentNodeID, String parentEmpNo) throws Exception {
		if (parentWorkID == 0) {
			throw new RuntimeException("@设置的父流程的流程WorkID为 0 ，这是非法的。");
		}
		if (parentFlowNo == null)
			throw new Exception("@设置的父流程的流程编号为 null ，这是非法的。");

		if (parentNodeID == 0)
			throw new Exception("@设置的父流程的节点编号为 0 ，这是非法的。");

		if (StringHelper.isNullOrEmpty(parentEmpNo)) {
			parentEmpNo = WebUser.getNo();
		}

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET PFlowNo=" + dbstr + "PFlowNo, PWorkID=" + dbstr + "PWorkID,PNodeID="
				+ dbstr + "PNodeID,PEmp=" + dbstr + "PEmp WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.PFlowNo, parentFlowNo);
		ps.Add(GenerWorkFlowAttr.PWorkID, parentWorkID);
		ps.Add(GenerWorkFlowAttr.PNodeID, parentNodeID);
		ps.Add(GenerWorkFlowAttr.PEmp, parentEmpNo);
		ps.Add(GenerWorkFlowAttr.WorkID, subFlowWorkID);

		BP.DA.DBAccess.RunSQL(ps);

		Flow fl = new Flow(subFlowNo);
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET PFlowNo=" + dbstr + "PFlowNo, PWorkID=" + dbstr + "PWorkID,PNodeID="
				+ dbstr + "PNodeID, PEmp=" + dbstr + "PEmp WHERE OID=" + dbstr + "OID";
		ps.Add(NDXRptBaseAttr.PFlowNo, parentFlowNo);
		ps.Add(NDXRptBaseAttr.PWorkID, parentWorkID);
		ps.Add(NDXRptBaseAttr.PNodeID, parentNodeID);
		ps.Add(NDXRptBaseAttr.PEmp, parentEmpNo);
		ps.Add(NDXRptBaseAttr.OID, subFlowWorkID);

		BP.DA.DBAccess.RunSQL(ps);
	}

	public static GERpt Flow_GenerGERpt(String flowNo, long workID) throws NumberFormatException, Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt", workID);
		return rpt;
	}

	/**
	 * 产生一个新的工作ID
	 * 
	 * @param flowNo
	 *            流程编号
	 * @return 返回当前操作员创建的工作ID
	 * @throws Exception
	 */
	public static long Flow_GenerWorkID(String flowNo) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		Flow fl = new Flow(flowNo);
		return fl.NewWork().getOID();
	}

	/**
	 * 产生一个新的工作
	 * 
	 * @param flowNo
	 *            流程编号
	 * @return 返回当前操作员创建的工作
	 * @throws Exception
	 */
	public static Work Flow_GenerWork(String flowNo) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		Flow fl = new Flow(flowNo);
		Work wk = fl.NewWork();
		wk.ResetDefaultVal();
		return wk;
	}

	/**
	 * 把流程从非正常运行状态恢复到正常运行状态 比如现在的流程的状态是，删除，挂起，现在恢复成正常运行。
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param msg
	 *            原因
	 * @return 执行信息
	 * @throws Exception
	 */
	public static void Flow_DoComeBackWorkFlow(String flowNo, long workID, String msg) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		WorkFlow wf = new WorkFlow(flowNo, workID);
		wf.DoComeBackWorkFlow(msg);
	}

	/**
	 * 恢复已完成的流程数据到指定的节点，如果节点为0就恢复到最后一个完成的节点上去. 恢复失败抛出异常
	 * 
	 * @param flowNo
	 *            要恢复的流程编号
	 * @param workid
	 *            要恢复的workid
	 * @param backToNodeID
	 *            恢复到的节点编号，如果是0，标示回复到流程最后一个节点上去.
	 * @param note
	 *            恢复的原因，此原因会记录到日志.
	 * @throws Exception
	 */
	public static String Flow_DoRebackWorkFlow(String flowNo, long workid, int backToNodeID, String note)
			throws Exception {
		BP.WF.Template.FlowExt fs = new BP.WF.Template.FlowExt(flowNo);
		return fs.DoRebackFlowData(workid, backToNodeID, note);

	}

	/**
	 * 执行删除流程:彻底的删除流程. 清除的内容如下: 1, 流程引擎中的数据. 2, 节点数据,NDxxRpt数据. 3, 轨迹表数据.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param isDelSubFlow
	 *            是否要删除它的子流程
	 * @return 执行信息
	 * @throws Exception
	 */
	public static String Flow_DoDeleteFlowByReal(String flowNo, long workID, boolean isDelSubFlow) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);
		try {
			WorkFlow.DeleteFlowByReal(flowNo, workID, isDelSubFlow);
		} catch (RuntimeException ex) {
			throw new RuntimeException("@删除前错误，" + ex.getStackTrace());
		}
		return "删除成功";
	}

	public static String Flow_DoDeleteDraft(String flowNo, long workID, boolean isDelSubFlow) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + dbstr + "WorkID";
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL = "DELETE FROM " + fl.getPTable() + " WHERE OID=" + dbstr + "OID";
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		// 删除开始节点数据.
		Node nd = fl.getHisStartNode();
		Work wk = nd.getHisWork();
		ps = new Paras();
		ps.SQL = "DELETE FROM " + wk.getEnMap().getPhysicsTable() + " WHERE OID=" + dbstr + "OID";
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		BP.DA.Log.DefaultLogWriteLineInfo(WebUser.getName() + "删除了FlowNo 为'" + flowNo + "',workID为'" + workID + "'的数据");

		return "删除成功";
	}

	/**
	 * 删除已经完成的流程 注意:它不触发事件.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param isDelSubFlow
	 *            是否删除子流程
	 * @param note
	 *            删除原因
	 * @return 删除过程信息
	 * @throws Exception
	 */
	public static String Flow_DoDeleteWorkFlowAlreadyComplete(String flowNo, long workID, boolean isDelSubFlow,
			String note) throws Exception {
		return WorkFlow.DoDeleteWorkFlowAlreadyComplete(flowNo, workID, isDelSubFlow, note);
	}

	/**
	 * 删除流程并写入日志 清除的内容如下: 1, 流程引擎中的数据. 2, 节点数据,NDxxRpt数据. 并作如下处理: 1, 保留track数据.
	 * 2, 写入流程删除记录表.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param deleteNote
	 *            删除原因
	 * @param isDelSubFlow
	 *            是否要删除它的子流程
	 * @return 执行信息
	 * @throws Exception
	 */
	public static String Flow_DoDeleteFlowByWriteLog(String flowNo, long workID, String deleteNote,
			boolean isDelSubFlow) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);
		WorkFlow wf = new WorkFlow(flowNo, workID);
		return wf.DoDeleteWorkFlowByWriteLog(deleteNote, isDelSubFlow);
	}

	/**
	 * 执行逻辑删除流程:此流程并非真正的删除仅做了流程删除标记 比如:逻辑删除工单.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param msg
	 *            逻辑删除的原因
	 * @param isDelSubFlow
	 *            逻辑删除的原因
	 * @return 执行信息,执行不成功抛出异常.
	 * @throws Exception
	 */
	public static String Flow_DoDeleteFlowByFlag(String flowNo, long workID, String msg, boolean isDelSubFlow)
			throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		WorkFlow wf = new WorkFlow(flowNo, workID);
		wf.DoDeleteWorkFlowByFlag(msg);
		if (isDelSubFlow) {
			// 删除子线程
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.FID, workID);
			for (GenerWorkFlow item : gwfs.ToJavaList()) {
				Flow_DoDeleteFlowByFlag(item.getFK_Flow(), item.getWorkID(), "删除子流程:" + msg, false);
			}

			// 删除子流程
			gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, workID);
			for (GenerWorkFlow item : gwfs.ToJavaList()) {
				Flow_DoDeleteFlowByFlag(item.getFK_Flow(), item.getWorkID(), "删除子流程:" + msg, false);
			}

		}
		return "删除成功";
	}

	/**
	 * 撤销删除流程 说明:如果一个流程处于逻辑删除状态,要回复正常运行状态,就执行此接口.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作流程ID
	 * @param msg
	 *            撤销删除的原因
	 * @return 执行消息,如果撤销不成功则抛出异常.
	 * @throws Exception
	 */
	public static String Flow_DoUnDeleteFlowByFlag(String flowNo, long workID, String msg) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		WorkFlow wf = new WorkFlow(flowNo, workID);
		wf.DoUnDeleteWorkFlowByFlag(msg);
		return "撤销删除成功.";
	}

	/**
	 * 执行-撤销发送 说明:如果流程转入了下一个节点,就会执行失败,就会抛出异常.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @return 返回成功执行信息
	 * @throws Exception
	 */
	public static String Flow_DoUnSend(String flowNo, long workID) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);
		WorkUnSend unSend = new WorkUnSend(flowNo, workID);
		return unSend.DoUnSend();

	}

	public static String Flow_DoUnSend(String flowNo, long workID, int unSendToNode) throws Exception {
		return Flow_DoUnSend(flowNo, workID, unSendToNode, 0);
	}

	public static DataSet DB_JobSchedule(long workID) throws Exception {
		String sql = "";
		DataSet ds = new DataSet();

		/*
		 * 流程控制主表, 可以得到流程状态，停留节点，当前的执行人. 该表里有如下字段是重点: 0. WorkID 流程ID. 1. WFState
		 * 字段用于标识当前流程的状态.. 2. FK_Node 停留节点. 3. NodeName 停留节点名称. 4. TodoEmps
		 * 停留的待办人员.
		 */
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

		/*
		 * 节点信息: 节点信息表,存储每个环节的节点信息数据. NodeID 节点ID. Name 名称. X,Y
		 * 节点图形位置，如果使用进度图就不需要了.
		 */
		Nodes nds = new Nodes(gwf.getFK_Flow());
		ds.Tables.add(nds.ToDataTableField("WF_Node"));

		/*
		 * 节点的连接线.
		 */
		Directions dirs = new Directions(gwf.getFK_Flow());
		ds.Tables.add(dirs.ToDataTableField("WF_Direction"));

		// #region 运动轨迹
		/*
		 * 运动轨迹： 构造的一个表，用与存储运动轨迹.
		 * 
		 */
		DataTable dtHistory = new DataTable();
		dtHistory.TableName = "Track";
		dtHistory.Columns.Add("FK_Node"); // 节点ID.
		dtHistory.Columns.Add("NodeName"); // 名称.
		dtHistory.Columns.Add("EmpNo"); // 人员编号.
		dtHistory.Columns.Add("EmpName"); // 名称
		dtHistory.Columns.Add("RDT"); // 记录日期.
		dtHistory.Columns.Add("SDT"); // 应完成日期(可以不用.)

		// 执行人.
		if (gwf.getWFState() == WFState.Complete) {
			// 历史执行人.
			sql = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + workID
					+ " AND (ActionType=1 OR ActionType=0)  ORDER BY RDT DESC";
			DataTable dtTrack = BP.DA.DBAccess.RunSQLReturnTable(sql);

			for (DataRow drTrack : dtTrack.Rows) {
				DataRow dr = dtHistory.NewRow();
				dr.setValue("FK_Node", drTrack.getValue("NDFrom"));

				dr.setValue("NodeName", drTrack.getValue("NDFromT"));
				dr.setValue("EmpNo", drTrack.getValue("EmpFrom"));
				dr.setValue("EmpName", drTrack.getValue("EmpFromT"));
				dr.setValue("RDT", drTrack.getValue("RDT"));
				// dr.setValue("SDT", drTrack.getValue("NDFrom"));

				dr.setValue("SDT", "");

				// dr["ActionType"] = drTrack["NDFrom"];
				// dr["NodeName"] = drTrack["NDFromT"];
				// dr["EmpNo"] = drTrack["EmpFrom"];
				// dr["EmpName"] = drTrack["EmpFromT"];
				// dr["RDT"] = drTrack["RDT"];
				// dr["SDT"] = drTrack[""];
				dtHistory.Rows.add(dr);
			}
		} else {
			GenerWorkerLists gwls = new GenerWorkerLists(workID);
			for (GenerWorkerList gwl : gwls.ToJavaList()) {
				DataRow dr = dtHistory.NewRow();

				dr.setValue("FK_Node", gwl.getFK_Node());
				dr.setValue("NodeName", gwl.getFK_NodeText());
				dr.setValue("EmpNo", gwl.getFK_Emp());
				dr.setValue("EmpName", gwl.getFK_EmpText());
				dr.setValue("RDT", gwl.getRDT());
				dr.setValue("SDT", gwl.getSDT());

				dtHistory.Rows.add(dr);

			}
		}
		ds.Tables.add(dtHistory);
		// #endregion 运动轨迹

		return ds;
	}

	public static String Flow_ReSend(long workid, int toNodeID, String toEmpIDs, String note) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() == WFState.Complete) {
			return "err@该流程已经运行完成您不能执行调整,可以执行回滚.";
		}

		Node nd = new Node(toNodeID);

		Emps emps = new Emps();

		String[] strs = toEmpIDs.split("[,]", -1);

		String todoEmps = "";
		for (String empID : strs) {
			if (DataType.IsNullOrEmpty(empID) == true) {
				continue;
			}

			BP.Port.Emp emp = new Emp(empID);
			todoEmps += emp.getNo() + "," + emp.getName();

			emps.AddEntity(emp);
		}

		gwf.setTodoEmps(todoEmps);
		gwf.setHuiQianTaskSta(HuiQianTaskSta.None);
		gwf.setWFState(WFState.Runing);

		// 给当前人员产生待办.
		GenerWorkerList gwl = new GenerWorkerList();
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.IsPass, 0);
		if (i == 0) {
			return "err@没有找到当前的待办人员.";
		}

		// 删除当前节点人员信息.
		gwl.Delete(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

		for (Emp item : emps.ToJavaList()) {
			// 插入一条信息，让调整的人员显示待办.
			gwl.setFK_Emp(item.getNo());
			gwl.setFK_EmpText(item.getName());
			gwl.setFK_Node(toNodeID);
			gwl.setIsPassInt(0);
			gwl.setIsRead(false);
			gwl.setWhoExeIt(0);
			try {
				gwl.Insert();
			} catch (java.lang.Exception e) {
				gwl.Update();
			}
		}

		// 更新当前节点状态.
		gwf.setFK_Node(toNodeID);
		gwf.setNodeName(nd.getName());
		gwf.Update();

		return "调整成功,调整到:" + gwf.getNodeName() + " , 调整给:" + todoEmps;
	}

	public static String Flow_DoUnSend(String flowNo, long workID, int unSendToNode, long fid) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		WorkUnSend unSend = new WorkUnSend(flowNo, workID, unSendToNode, fid);
		unSend.UnSendToNode = unSendToNode;

		return unSend.DoUnSend();
	}

	/**
	 * 执行冻结
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            workid
	 * @param msg
	 *            冻结原因
	 * @throws Exception
	 */
	public static String Flow_DoFix(String flowNo, long workid, String msg) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		// 执行冻结.
		WorkFlow wf = new WorkFlow(flowNo, workid);
		return wf.DoFix(msg);
	}

	/**
	 * 执行解除冻结 于挂起的区别是,冻结需要有权限的人才可以执行解除冻结， 挂起是自己的工作可以挂起也可以解除挂起。
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            workid
	 * @param msg
	 *            解除原因
	 * @throws Exception
	 */
	public static String Flow_DoUnFix(String flowNo, long workid, String msg) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		// 执行冻结.
		WorkFlow wf = new WorkFlow(flowNo, workid);
		return wf.DoUnFix(msg);
	}

	/**
	 * 执行流程结束 说明:正常的流程结束.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param msg
	 *            流程结束原因
	 * @return 返回成功执行信息
	 * @throws Exception
	 */
	public static String Flow_DoFlowOver(String flowNo, long workID, String msg) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		WorkFlow wf = new WorkFlow(flowNo, workID);
		Node nd = new Node(wf.getHisGenerWorkFlow().getFK_Node());
		GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt");
		rpt.setOID(workID);
		rpt.RetrieveFromDBSources();
		return wf.DoFlowOver(ActionType.FlowOver, msg, nd, rpt);
	}

	/**
	 * 执行流程结束:强制的流程结束.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param flowNo
	 *            当前节点编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            工作ID
	 * @param msg
	 *            强制流程结束的原因
	 * @return 执行强制结束流程
	 * @throws Exception
	 */
	public static String Flow_DoFlowOverByCoercion(String flowNo, int nodeid, long workID, long fid, String msg)
			throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);
		WorkFlow wf = new WorkFlow(flowNo, workID);

		Node currND = new Node(nodeid);

		Flow fl = new Flow(flowNo);
		GERpt rpt = fl.getHisGERpt();
		rpt.setOID(workID);
		rpt.RetrieveFromDBSources();

		String s = wf.DoFlowOver(ActionType.FlowOverByCoercion, msg, currND, rpt);
		if (StringHelper.isNullOrEmpty(s)) {
			s = "流程已经成功结束.";
		}
		return s;
	}

	/**
	 * 获得执行下一步骤的节点ID，这个功能是在流程未发送前可以预先知道 它就要到达那一个节点上去,以方便在当前节点发送前处理业务逻辑.
	 * 1,首先保证当前人员是可以执行当前节点的工作. 2,其次保证获取下一个节点只有一个.
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @return 下一步骤的所要到达的节点, 如果获取不到就会抛出异常.
	 * @throws Exception
	 */
	public static int Node_GetNextStepNode(String fk_flow, long workid) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		//// 检查当前人员是否可以执行当前工作.
		// if (BP.WF.Dev2Interface.Flow_CheckIsCanDoCurrentWork( workid,
		//// WebUser.getNo()) == false)
		// throw new Exception("@当前人员不能执行此节点上的工作.");

		// 获取当前nodeID.
		int currNodeID = BP.WF.Dev2Interface.Node_GetCurrentNodeID(fk_flow, workid);

		// 获取
		Node nd = new Node(currNodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		WorkNode wn = new WorkNode(wk, nd);
		return wn.NodeSend_GenerNextStepNode().getNodeID();
	}

	/**
	 * 获取指定的workid 在运行到的节点编号
	 * 
	 * @param workID
	 *            需要找到的workid
	 * @return 返回节点编号. 如果没有找到，就会抛出异常.
	 */
	public static int Flow_GetCurrentNode(long workID) {
		Paras ps = new Paras();
		ps.SQL = "SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("WorkID", workID);
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/**
	 * 获取指定节点的Work
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @return 当前工作
	 * @throws Exception
	 */
	public static Work Flow_GetCurrentWork(int nodeID, long workID) throws Exception {
		Node nd = new Node(nodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workID);
		wk.Retrieve();
		return wk;
	}

	/**
	 * 获取当前工作节点的Work
	 * 
	 * @param workID
	 *            工作ID
	 * @return 当前工作节点的Work
	 * @throws Exception
	 */
	public static Work Flow_GetCurrentWork(long workID) throws Exception {
		Node nd = new Node(Flow_GetCurrentNode(workID));
		Work wk = nd.getHisWork();
		wk.setOID(workID);
		wk.Retrieve();
		wk.ResetDefaultVal();
		return wk;
	}

	/**
	 * 指定 workid 当前节点由哪些人可以执行.
	 * 
	 * @param workID
	 *            需要找到的workid
	 * @return 返回当前处理人员列表,数据结构与WF_GenerWorkerList一致.
	 */
	public static DataTable Flow_GetWorkerList(long workID) {
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM WF_GenerWorkerList WHERE IsEnable=1 AND IsPass=0 AND WorkID="
				+ SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("WorkID", workID);
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}

	/**
	 * 检查是否可以发起流程
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param userNo
	 *            用户编号
	 * @return 是否可以发起当前流程
	 * @throws Exception
	 */
	public static boolean Flow_IsCanStartThisFlow(String flowNo, String userNo, String pFlowNo, int pNodeID,
			long pworkID) throws Exception {
		Node nd = new Node(Integer.parseInt(flowNo + "01"));
		if (nd.getIsGuestNode() == true) {
			if (!BP.Web.WebUser.getNo().equals("Guest")) {
				throw new RuntimeException("@当前节点是来宾处理节点，但是目前您{" + BP.Web.WebUser.getNo() + "}不是来宾帐号。");
			}
			return true;
		}

		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		int num = 0;
		if (SystemConfig.getOSDBSrc() == OSDBSrc.Database) {
			switch (nd.getHisDeliveryWay()) {
			case ByStation:
			case ByStationOnly:
				ps.SQL = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeStation A, " + BP.WF.Glo.getEmpStation()
						+ " B WHERE A.FK_Station= B.FK_Station AND  A.FK_Node=" + dbstr + "FK_Node AND B.FK_Emp="
						+ dbstr + "FK_Emp";
				ps.Add("FK_Node", nd.getNodeID());
				ps.Add("FK_Emp", userNo);
				num = DBAccess.RunSQLReturnValInt(ps);
				break;
			case ByDept:
				ps.SQL = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeDept A, " + BP.WF.Glo.getEmpDept()
						+ " B WHERE A.FK_Dept= B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node AND B.FK_Emp=" + dbstr
						+ "FK_Emp";
				ps.Add("FK_Node", nd.getNodeID());
				ps.Add("FK_Emp", userNo);
				num = DBAccess.RunSQLReturnValInt(ps);
				break;
			case ByBindEmp:
				ps.SQL = "SELECT COUNT(*) AS Num FROM WF_NodeEmp WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Node=" + dbstr
						+ "FK_Node";
				ps.Add("FK_Emp", userNo);
				ps.Add("FK_Node", nd.getNodeID());
				num = DBAccess.RunSQLReturnValInt(ps);
				break;
			case ByDeptAndStation:

				String sql = "SELECT COUNT(A.FK_Node) as Num FROM WF_NodeDept A, Port_DeptEmp B, WF_NodeStation C, "
						+ Glo.getEmpStation() + " D";
				sql += " WHERE A.FK_Dept= B.FK_Dept AND  A.FK_Node=" + dbstr + "FK_Node AND B.FK_Emp=" + dbstr
						+ "FK_Emp AND  A.FK_Node=C.FK_Node AND C.FK_Station=D.FK_Station AND D.FK_Emp=" + dbstr
						+ "FK_Emp";
				ps.SQL = sql;
				ps.Add("FK_Node", nd.getNodeID());
				ps.Add("FK_Emp", userNo);
				num = DBAccess.RunSQLReturnValInt(ps);

				break;
			case BySelected:
				num = 1;
				break;
			default:
				throw new RuntimeException("@开始节点不允许设置此访问规则：" + nd.getHisDeliveryWay());
			}
		} else {
			switch (nd.getHisDeliveryWay()) {
			case ByStation:
				break;
			case ByDept:
				throw new RuntimeException("@目前取消支持.");
			case ByBindEmp:
				ps.SQL = "SELECT COUNT(*) AS Num FROM WF_NodeEmp WHERE FK_Emp=" + dbstr + "FK_Emp AND FK_Node=" + dbstr
						+ "FK_Node";
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
		if (num == 0) {
			return false;
		}

		// 增加发起流程判断.
		if (pFlowNo == null)
			return true;

		Flow fl = new Flow(flowNo);
		if (fl.getStartLimitRole() == StartLimitRole.None)
			return true;

		// 只有一个子流程,才能发起.
		if (fl.getStartLimitRole() == StartLimitRole.OnlyOneSubFlow) {
			if (pworkID == 0)
				return true;

			String sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkID + " AND FK_Flow='"
					+ fl.getNo() + "' AND WFState >=2 ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
				return true;

			throw new Exception("该流程只能允许发起一个子流程.");
		}

		return true;

	}

	/**
	 * 获得正在运行中的子流程的数量
	 * 
	 * @param workID
	 *            父流程的workid
	 * @return 获得正在运行中的子流程的数量。如果是0，表示所有的流程的子流程都已经结束。
	 */
	public static int Flow_NumOfSubFlowRuning(long pWorkID) {
		String sql = "SELECT COUNT(*) AS num FROM WF_GenerWorkFlow WHERE WFState!=" + WFState.Complete.getValue()
				+ " AND PWorkID=" + pWorkID;
		return DBAccess.RunSQLReturnValInt(sql);
	}

	/**
	 * 获得正在运行中的子流程的数量
	 * 
	 * @param pWorkID
	 *            父流程的workid
	 * @param currWorkID
	 *            不包含当前的工作节点ID
	 * @param workID
	 *            父流程的workid
	 * @return 获得正在运行中的子流程的数量。如果是0，表示所有的流程的子流程都已经结束。
	 */
	public static int Flow_NumOfSubFlowRuning(long pWorkID, long currWorkID) {
		String sql = "SELECT COUNT(*) AS num FROM WF_GenerWorkFlow WHERE WFState!=" + WFState.Complete.getValue()
				+ " AND WorkID!=" + currWorkID + " AND PWorkID=" + pWorkID;
		return DBAccess.RunSQLReturnValInt(sql);
	}

	public static boolean Flow_IsInGenerWork(long workID) {

		if (workID == 0) {
			return false;
		}

		String sql = "select * from WF_Generworkflow where WorkID='" + workID + "'";
		return DBAccess.RunSQLReturnCOUNT(sql) > 0;
	}

	/**
	 * 检查指定节点上的所有子流程是否完成？ For: 深圳熊伟.
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @return 返回该节点上的子流程是否完成？
	 */
	public static boolean Flow_CheckAllSubFlowIsOver(int nodeID, long workID) {
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE  PNodeID=" + dbstr + "PNodeID AND PWorkID=" + dbstr
				+ "PWorkID AND WFState!=" + dbstr + "WFState ";
		ps.Add(GenerWorkFlowAttr.PNodeID, nodeID);
		ps.Add(GenerWorkFlowAttr.PWorkID, workID);
		ps.Add(GenerWorkFlowAttr.WFState, WFState.Complete.getValue());

		if (BP.DA.DBAccess.RunSQLReturnValInt(ps) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查当前人员是否有权限处理当前的工作
	 * 
	 * @param workID
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public static boolean Flow_IsCanDoCurrentWork(long workID, String userNo) throws Exception {

		if (workID == 0) {
			return true;
		}

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0) {
			return true;
		}

		if (gwf.getTodoEmps().indexOf(userNo + ",") >= 0) {
			GenerWorkerList gwl = new GenerWorkerList();
			int inum = gwl.Retrieve(GenerWorkerListAttr.WorkID, workID, GenerWorkerListAttr.FK_Emp, userNo,
					GenerWorkerListAttr.FK_Node, gwf.getFK_Node());
			if (inum == 1 && gwl.getIsPassInt() == 0) {
				return true;
			}
		}

		/*
		 * if (userNo.equals("admin")) { return true; }
		 */
		// 判断是否是开始节点 .
		String str = (new Integer(gwf.getFK_Node())).toString();

		int len = str.length() - 2;
		if (str.substring(len, len + 2).equals("01")) {
			// 如果是开始节点，如何去判断是否可以处理当前节点的权限.

			String mysql = "SELECT FK_Emp,  IsPass FROM WF_GenerWorkerList WHERE WorkID=" + workID + " AND FK_Node="
					+ str;
			DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
			if (mydt.Rows.size() == 0) {
				return true;
			}

			for (DataRow dr : mydt.Rows) {
				String fk_emp = dr.getValue(0).toString();
				String isPass = dr.getValue(1).toString();
				if (userNo.equals(fk_emp) && (isPass.equals("0") || isPass.equals("80") || isPass.equals("90"))) {
					return true;
				}

			}
			return false;
		}
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		// sunxd
		// 问题:不同类型的数据库执行以下语句所返回的字段名称不一样(oracle 返回的字段名称全部为大写)，导致前端获取时获取不到。
		// 解决:为了保证满足所有不同类型的数据库统一，将别名加上“”号
		ps.SQL = "SELECT c.RunModel \"RunModel\",c.IsGuestNode \"IsGuestNode\", a.GuestNo \"GuestNo\", a.TaskSta \"TaskSta\", a.WFState \"WFState\", IsPass \"IsPass\" FROM WF_GenerWorkFlow a, WF_GenerWorkerlist b, WF_Node c WHERE  b.FK_Node=c.NodeID AND a.WorkID=b.WorkID AND a.FK_Node=b.FK_Node  AND b.FK_Emp="
				+ dbstr + "FK_Emp AND b.IsEnable=1 AND a.WorkID=" + dbstr + "WorkID ";
		ps.Add("FK_Emp", userNo);
		ps.Add("WorkID", workID);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() == 0) {
			return false;
		}

		// 判断是否是待办.
		int myisPass = Integer.parseInt(dt.Rows.get(0).getValue("IsPass").toString());

		// 新增加的标记,=90 就是会签主持人执行会签的状态. 80=吊起子流程.
		if (myisPass == 90 || myisPass == 80) {
			return true;
		}

		if (myisPass != 0) {
			return false;
		}

		WFState wfsta = WFState.forValue(Integer.parseInt(dt.Rows.get(0).getValue("WFState").toString()));
		if (wfsta == WFState.Complete) {
			return false;
		}
		if (wfsta == WFState.Delete) {
			return false;
		}

		// 判断是否是客户处理节点.
		int isGuestNode = Integer.parseInt(dt.Rows.get(0).getValue("IsGuestNode").toString());
		if (isGuestNode == 1) {
			if (dt.Rows.get(0).getValue("GuestNo").toString().equals(BP.Web.GuestUser.getNo())) {
				return true;
			} else {
				return false;
			}
		}

		int i = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());

		RunModel rm = RunModel.forValue(i);
		switch (rm) {
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
	 * 检查当前人员是否有权限处理当前的工作.
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param userNo
	 *            要判断的操作人员
	 * @return 返回指定的人员是否有操作当前工作的权限
	 */
	public static boolean Flow_IsCanDoCurrentWorkGuest(int nodeID, long workID, String userNo) {
		if (workID == 0) {
			return true;
		}

		if (userNo.equals("admin")) {
			return true;
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		// ps.SQL = "SELECT c.RunModel FROM WF_GenerWorkFlow a ,
		// WF_GenerWorkerlist b, WF_Node c WHERE a.FK_Node=" + dbstr + "FK_Node
		// AND b.FK_Node=c.NodeID AND a.WorkID=b.WorkID AND a.FK_Node=b.FK_Node
		// AND b.FK_Emp=" + dbstr + "FK_Emp AND b.IsEnable=1 AND a.workid=" +
		// dbstr + "WorkID";
		// ps.Add("FK_Node", nodeID);
		// ps.Add("FK_Emp", userNo);
		// ps.Add("WorkID", workID);
		String sql = "SELECT c.RunModel, a.TaskSta FROM WF_GenerWorkFlow a , WF_GenerWorkerlist b, WF_Node c WHERE a.FK_Node='"
				+ nodeID + "'  AND b.FK_Node=c.NodeID AND a.WorkID=b.WorkID AND a.FK_Node=b.FK_Node  AND b.GuestNo='"
				+ userNo + "' AND b.IsEnable=1 AND a.WorkID=" + workID;

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			return false;
		}

		int i = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());
		TaskSta TaskStai = TaskSta.forValue(Integer.parseInt(dt.Rows.get(0).getValue(1).toString()));
		if (TaskStai == TaskSta.Sharing) {
			return false;
		}

		RunModel rm = RunModel.forValue(i);
		switch (rm) {
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

		if (DBAccess.RunSQLReturnValInt(ps) == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 是否可以查看流程数据 用于判断是否可以查看流程轨迹图. edit: stone 2015-03-25
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            FID
	 * @return
	 * @throws Exception
	 */
	public static boolean Flow_IsCanViewTruck(String flowNo, long workid, String userNo) throws Exception {

		if (userNo == null)
			userNo = WebUser.getNo();

		if (userNo.equals("admin")) {
			return true;
		}

		// 先从轨迹里判断.
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT count(MyPK) as Num FROM ND" + Integer.parseInt(flowNo) + "Track WHERE (WorkID=" + dbStr
				+ "WorkID OR FID=" + dbStr + "FID) AND (EmpFrom=" + dbStr + "Emp1 OR EmpTo=" + dbStr + "Emp2)";
		ps.Add(BP.WF.TrackAttr.WorkID, workid);
		ps.Add(BP.WF.TrackAttr.FID, workid);
		ps.Add("Emp1", userNo);
		ps.Add("Emp2", userNo);

		if (BP.DA.DBAccess.RunSQLReturnValInt(ps) > 1) {
			return true;
		}

		// 在查看该流程的发起者，与当前人是否在同一个部门，如果是也返回true.
		ps = new Paras();
		ps.SQL = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID=" + dbStr + "WorkID OR WorkID=" + dbStr + "FID";
		ps.Add(BP.WF.TrackAttr.WorkID, workid);
		ps.Add(BP.WF.TrackAttr.FID, workid);

		String fk_dept = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
		if (fk_dept == null) {
			BP.WF.Flow fl = new Flow(flowNo);
			ps.SQL = "SELECT FK_Dept FROM " + fl.getPTable() + " WHERE OID=" + dbStr + "WorkID OR OID=" + dbStr + "FID";
			fk_dept = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (fk_dept == null) {
				throw new RuntimeException("@流程引擎数据被删除.");
			}
		}
		if (fk_dept.equals(BP.Web.WebUser.getFK_Dept())) {
			return true;
		}

		return false;
	}

	/**
	 * 删除子线程
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            子线程的工作ID
	 * @param info
	 *            删除信息
	 * @throws Exception
	 */
	public static String Flow_DeleteSubThread(String flowNo, long workid, String info) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(workid);

		WorkFlow wf = new WorkFlow(flowNo, workid);
		String msg = wf.DoDeleteWorkFlowByReal(false);

		BP.WF.Dev2Interface.WriteTrackInfo(flowNo, gwf.getFK_Node(), gwf.getNodeName(), gwf.getFID(), 0, info, "删除子线程");
		return msg;
	}

	/**
	 * 执行工作催办
	 * 
	 * @param workID
	 *            工作ID
	 * @param msg
	 *            催办消息
	 * @param isPressSubFlow
	 *            是否催办子流程？
	 * @return 返回执行结果
	 * @throws Exception
	 */
	public static String Flow_DoPress(long workID, String msg, boolean isPressSubFlow) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(workID);

		// 找到当前待办的工作人员
		GenerWorkerLists wls = new GenerWorkerLists(workID, gwf.getFK_Node());
		String toEmp = "", toEmpName = "";
		String mailTitle = "催办:" + gwf.getTitle() + ", 发送人:" + WebUser.getName();
		// 如果子线程找不到流转日志并且父流程编号不为空，在父流程进行查找接收人
		if (wls.size() == 0 && gwf.getFID() != 0) {
			wls = new GenerWorkerLists(gwf.getFID(), gwf.getFK_Node());
		}

		for (GenerWorkerList wl : wls.ToJavaList()) {
			if (wl.getIsEnable() == false) {
				continue;
			}

			toEmp += wl.getFK_Emp() + ",";
			toEmpName += wl.getFK_EmpText() + ",";

			// 发消息.
			BP.WF.Dev2Interface.Port_SendMsg(wl.getFK_Emp(), mailTitle, msg, null, BP.WF.SMSMsgType.DoPress,
					gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());

			wl.setPressTimes(wl.getPressTimes() + 1);
			wl.Update();

			// wl.Update(GenerWorkerListAttr.PressTimes, wl.PressTimes + 1);
		}

		// 写入日志.
		WorkNode wn = new WorkNode(workID, gwf.getFK_Node());
		wn.AddToTrack(ActionType.Press, toEmp, toEmpName, gwf.getFK_Node(), gwf.getNodeName(), msg);

		// 如果催办子流程.
		if (isPressSubFlow) {
			String subMsg = "";
			GenerWorkFlows gwfs = gwf.getHisSubFlowGenerWorkFlows();
			for (GenerWorkFlow item : gwfs.ToJavaList()) {
				subMsg += "@已经启动对子线程:" + item.getTitle() + "的催办,消息如下:";
				subMsg += Flow_DoPress(item.getWorkID(), msg, false);
			}
			return "系统已经把您的信息通知给:" + toEmpName + "" + subMsg;
		} else {
			return "系统已经把您的信息通知给:" + toEmpName;
		}
	}

	/**
	 * 重新设置流程标题 可以在节点的任何位置调用它,产生新的标题。
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @return 是否设置成功
	 * @throws Exception
	 */
	public static boolean Flow_ReSetFlowTitle(String flowNo, int nodeID, long workid) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		Node nd = new Node(nodeID);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.RetrieveFromDBSources();
		Flow fl = nd.getHisFlow();
		String title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);
		return Flow_SetFlowTitle(flowNo, workid, title);
	}

	/**
	 * 设置流程参数 该参数，用户可以在流程实例中获得到.
	 * 
	 * @param workid
	 *            工作ID
	 * @param paras
	 *            参数,格式：@GroupMark=xxxx@IsCC=1
	 * @return 是否设置成功
	 * @throws Exception
	 */
	public static boolean Flow_SetFlowParas(String flowNo, long workid, String paras) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("创建流程ID不存在.");
		}

		String[] strs = paras.split("[@]", -1);
		for (String item : strs) {
			if (StringHelper.isNullOrEmpty(item)) {
				continue;
			}
			// GroupMark=xxxx
			String[] mystr = item.split("[=]", -1);
			gwf.SetPara(mystr[0], mystr[1]);
		}
		gwf.Update();
		return true;
	}

	/**
	 * 设置流程标题
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param title
	 *            标题
	 * @return 是否设置成功
	 * @throws Exception
	 */
	public static boolean Flow_SetFlowTitle(String flowNo, long workid, String title, String wfstate) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		String dbstr = SystemConfig.getAppCenterDBVarStr();

		if (wfstate != null && !"".equals(wfstate)) {
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + dbstr + "Title,WFState=" + dbstr + "WFState  WHERE WorkID="
					+ dbstr + "WorkID";
			ps.Add(GenerWorkFlowAttr.Title, title);
			ps.Add(GenerWorkFlowAttr.WFState, wfstate);
			ps.Add(GenerWorkFlowAttr.WorkID, workid);
			DBAccess.RunSQL(ps);
		} else {
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + dbstr + "Title WHERE WorkID=" + dbstr + "WorkID";
			ps.Add(GenerWorkFlowAttr.Title, title);
			ps.Add(GenerWorkFlowAttr.WorkID, workid);
			DBAccess.RunSQL(ps);
		}

		Flow fl = new Flow(flowNo);
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET Title=" + dbstr + "Title WHERE OID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.Title, title);
		ps.Add(GenerWorkFlowAttr.WorkID, workid);
		int num = DBAccess.RunSQL(ps);

		if (fl.getHisDataStoreModel() == DataStoreModel.ByCCFlow) {
			ps = new Paras();
			ps.SQL = "UPDATE ND" + Integer.parseInt(flowNo + "01") + " SET Title=" + dbstr + "Title WHERE OID=" + dbstr
					+ "WorkID";
			ps.Add(GenerWorkFlowAttr.Title, title);
			ps.Add(GenerWorkFlowAttr.WorkID, workid);
			DBAccess.RunSQL(ps);
		}

		if (num == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean Flow_SetFlowTitle(String flowNo, long workid, String title) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

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

		if (fl.getHisDataStoreModel() == DataStoreModel.ByCCFlow) {
			// ps = new Paras();
			// ps.SQL = "UPDATE ND" + int.Parse(flowNo + "01") + " SET Title=" +
			// dbstr + "Title WHERE OID=" + dbstr + "WorkID";
			// ps.Add(GenerWorkFlowAttr.Title, title);
			// ps.Add(GenerWorkFlowAttr.WorkID, workid);
			// DBAccess.RunSQL(ps);
		}

		if (num == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 保存到待办
	 * 
	 * @throws Exception
	 */
	public static void Node_SaveEmpWorks(String flowNo, String title, long workid, String userNo) throws Exception {
		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());
		Emp emp = new Emp(userNo);

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		int i = gwf.RetrieveFromDBSources();

		gwf.setFlowName(fl.getName());
		gwf.setFK_Flow(flowNo);
		gwf.setFK_FlowSort(fl.getFK_FlowSort());

		gwf.setFK_Dept(emp.getFK_Dept());
		gwf.setDeptName(emp.getFK_DeptText());
		gwf.setFK_Node(fl.getStartNodeID());

		gwf.setNodeName(nd.getName());
		gwf.setWFSta(WFSta.Runing);
		gwf.setWFState(WFState.Runing);

		gwf.setTitle(title);

		gwf.setStarter(emp.getNo());
		gwf.setStarterName(emp.getName());
		gwf.setRDT(DataType.getCurrentDataTimess());

		gwf.setPWorkID(0);
		gwf.setPFlowNo(null);
		gwf.setPNodeID(0);
		if (i == 0)
			gwf.Insert();
		else
			gwf.Update();

		// 产生工作列表.
		String sql = "SELECT workid FROM wf_generworkerlist WHERE workid='" + workid + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			GenerWorkerList gwl = new GenerWorkerList();
			gwl.setWorkID(workid);
			if (gwl.RetrieveFromDBSources() == 0) {
				gwl.setFK_Emp(emp.getNo());
				gwl.setFK_EmpText(emp.getName());

				gwl.setFK_Node(nd.getNodeID());
				gwl.setFK_NodeText(emp.getName());
				gwl.setFID(0);

				gwl.setFK_Flow(fl.getNo());
				gwl.setFK_Dept(emp.getFK_Dept());

				gwl.setSDT(DataType.getCurrentDataTimess());
				gwl.setDTOfWarning(DataType.getCurrentDataTime());
				gwl.setRDT(DataType.getCurrentDataTimess());
				gwl.setIsEnable(true);
				gwl.setIsRead(true);
				gwl.setIsPass(false);
				gwl.setSender(userNo);
				gwl.setPRI(gwf.getPRI());
				gwl.Insert();
			}
		}
		// 执行对报表的数据表WFState状态的更新,让它为runing的状态.
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,WFSta=" + dbstr + "WFSta,Title="
				+ dbstr + "Title,FK_Dept=" + dbstr + "FK_Dept,PFlowNo=" + dbstr + "PFlowNo,PWorkID=" + dbstr
				+ "PWorkID WHERE OID=" + dbstr + "OID";

		ps.Add("WFState", WFState.Runing.getValue());
		ps.Add("WFSta", WFSta.Runing.getValue());
		ps.Add("Title", gwf.getTitle());
		ps.Add("FK_Dept", gwf.getFK_Dept());
		ps.Add("PFlowNo", gwf.getPFlowNo());
		ps.Add("PWorkID", gwf.getPWorkID());
		ps.Add("OID", workid);
		DBAccess.RunSQL(ps);

	}

	/**
	 * 调度流程 说明： 1，通常是由admin执行的调度。 2，特殊情况下，需要从一个人的待办调度到另外指定的节点，制定的人员上。
	 * 
	 * @param workid
	 *            工作ID
	 * @param toNodeID
	 *            调度到节点
	 * @param toEmper
	 *            调度到人员
	 * @throws Exception
	 */
	public static String Flow_Schedule(long workid, int toNodeID, String toEmper) throws Exception {
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		Node nd = new Node(toNodeID);
		Emp emp = new Emp(toEmper);

		// 找到GenerWorkFlow,并执行更新.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setWFState(WFState.Runing);
		gwf.setTaskSta(TaskSta.None);
		gwf.setTodoEmps(toEmper);
		gwf.setFK_Node(toNodeID);
		gwf.setNodeName(nd.getName());
		// gwf.StarterName =emp.Name;
		gwf.Update();

		// 让其都设置完成。
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=1 WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.WorkID, workid);
		BP.DA.DBAccess.RunSQL(ps);

		// 更新流程数据信息。
		Flow fl = new Flow(gwf.getFK_Flow());
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET FlowEnder=" + dbstr + "FlowEnder,FlowEndNode=" + dbstr
				+ "FlowEndNode WHERE OID=" + dbstr + "OID";
		ps.Add(NDXRptBaseAttr.FlowEnder, toEmper);
		ps.Add(NDXRptBaseAttr.FlowEndNode, toNodeID);
		ps.Add(NDXRptBaseAttr.OID, workid);
		BP.DA.DBAccess.RunSQL(ps);

		// 执行更新.
		GenerWorkerLists gwls = new GenerWorkerLists(workid);
		GenerWorkerList gwl = (GenerWorkerList) ((gwls.get(gwls.size() - 1) instanceof GenerWorkerList)
				? gwls.get(gwls.size() - 1) : null); // 获得最后一个。
		gwl.setRDT(DataType.getCurrentDataTimess());
		gwl.setWorkID(workid);
		gwl.setFK_Node(toNodeID);
		gwl.setFK_NodeText(nd.getName());
		gwl.setFK_Emp(toEmper);
		gwl.setFK_EmpText(emp.getName());
		gwl.setIsPass(false);
		gwl.setIsEnable(true);
		gwl.setIsRead(false);
		gwl.setWhoExeIt(nd.getWhoExeIt());
		// gwl.Sender = BP.Web.WebUser.getNo();
		gwl.setHungUpTimes(0);
		gwl.setFID(gwf.getFID());
		gwl.setFK_Dept(emp.getFK_Dept());

		if (gwl.Update() == 0) {
			gwl.Insert();
		}

		String sql = "SELECT COUNT(*) FROM WF_EmpWorks where WorkID=" + workid + " and fk_emp='" + toEmper + "'";
		int i = BP.DA.DBAccess.RunSQLReturnValInt(sql);
		if (i == 0) {
			throw new RuntimeException("@调度错误");
		}

		return "该流程(" + gwf.getTitle() + ")，已经调度到(" + nd.getName() + "),分配给(" + emp.getName() + ")";
	}

	/**
	 * 设置流程运行模式 如果是自动流程. 负责人:liuxianchen. 调用地方/WorkOpt/TransferCustom.jsp
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param runType
	 *            是否自动运行？ 如果自动运行，就按照流程设置的规则运行。 非自动运行，就按照用户自己定义的运转顺序计算。
	 * @param paras
	 *            手工运行的参数格式为: @节点ID1`子流程No`处理模式`接受人1,接受人n`抄送人1NO,抄送人nNO`抄送人1Name,抄送人nName@节点ID2`子流程No`处理模式`接受人1,接受人n`抄送人1NO,抄送人nNO`抄送人1Name,抄送人nName
	 * @throws Exception
	 */
	public static void Flow_SetFlowTransferCustom(String flowNo, long workid, TransferCustomType runType, String paras)
			throws Exception {
		// 删除以前存储的参数.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_TransferCustom WHERE WorkID=" + workid);

		// 保存参数.
		// 参数格式为 @104`SubFlow002`1`zhangsan,lisi`wangwu,chenba`王五,陈八@......
		String[] strs = paras.split("[@]", -1);
		int idx = 0, cidx = 0;
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			if (str.contains("`") == false) {
				continue;
			}

			// 处理字符串.
			String[] vals = str.split("[`]", -1);
			int nodeid = Integer.parseInt(vals[0]); // 节点ID.
			String subFlow = vals[1]; // 调用的子流程.
			int todomodel = Integer.parseInt(vals[2]); // 处理模式.

			TransferCustom tc = new TransferCustom();
			tc.setIdx(idx); // 顺序.
			tc.setFK_Node(nodeid); // 节点.
			tc.setWorkID(workid); // 工作ID.
			tc.setWorker(vals[3]); // 工作人员.
			tc.setSubFlowNo(subFlow); // 子流程.
			tc.setMyPK(tc.getFK_Node() + "_" + tc.getWorkID() + "_" + idx);
			tc.setTodolistModel(TodolistModel.forValue(todomodel)); // 处理模式.
			tc.Save();
			idx++;

			// 设置抄送
			String[] ccs = vals[4].split("[,]", -1);
			String[] ccNames = vals[5].split("[,]", -1);
			SelectAccper sa = new SelectAccper();
			sa.Delete(SelectAccperAttr.FK_Node, nodeid, SelectAccperAttr.WorkID, workid, SelectAccperAttr.AccType, 1);

			cidx = 0;
			for (int i = 0; i < ccs.length; i++) {
				if (StringHelper.isNullOrWhiteSpace(ccs[i]) || ccs[i].equals("0")) {
					continue;
				}

				sa = new SelectAccper();
				sa.setMyPK(nodeid + "_" + workid + "_" + ccs[i]);
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
		if (gwf.RetrieveFromDBSources() == 0) {
			gwf.setWFSta(WFSta.Runing);
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
	 * 设置流程运行模式 启用新的接口原来的接口参数格式太复杂,仍然保留.
	 * 标准格式:@NodeID=节点ID;Worker=操作人员1,操作人员2,操作人员n,TodolistModel=多人处理模式;SubFlowNo=可发起的子流程编号;SDT=应完成时间;
	 * 标准简洁格式:@NodeID=节点ID;Worker=操作人员1,操作人员2,操作人员n;@NodeID=节点ID2;Worker=操作人员1,操作人员2,操作人员n;
	 * 完整格式: @NodeID=101;Worker=zhangsan,lisi;@TodolistModel=1;SubFlowNo=001;SDT=2015-12-12;@NodeID=102;Worker=zhangsan,lisi;@TodolistModel=1;SubFlowNo=001;SDT=2015-12-12;
	 * 简洁格式: @NodeID=101;Worker=zhangsan,lisi;@NodeID=102;Worker=wagnwu,zhaoliu;
	 * 
	 * @param flowNo
	 * @param workid
	 * @param runType
	 * @param paras
	 *            格式为:@节点编号1;处理人员1,处理人员2,处理人员n(可选);应处理时间(可选)
	 * @throws Exception
	 */
	public static void Flow_SetFlowTransferCustomV201605(String flowNo, long workid, TransferCustomType runType,
			String paras) throws Exception {

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0) {
			gwf.setWFSta(WFSta.Runing);
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
		if (runType == TransferCustomType.ByCCBPMDefine) {
			return; // 如果是按照设置的模式运行，就要更改状态后退出它.
		}
		// 删除以前存储的参数.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_TransferCustom WHERE WorkID=" + workid);
		// 保存参数.
		// 参数格式为 格式为:@节点编号1;处理人员1,处理人员2,处理人员n;应处理时间(可选)
		// 例如1:
		// @101;zhangsan,lisi,wangwu;2016-05-12;@102;liming,xiaohong,xiaozhang;2016-05-12
		// 例如2:
		// @101;zhangsan,lisi,wangwu;@102;liming,xiaohong,xiaozhang;2016-05-12

		String[] strs = paras.split("[@]", -1);
		int idx = 0, cidx = 0;
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			if (str.contains(";") == false) {
				continue;
			}
			// 处理字符串.
			String[] vals = str.split("[;]", -1);
			int nodeid = Integer.parseInt(vals[0]); // 节点ID.
			String subFlow = vals[1]; // 调用的子流程.
			int todomodel = Integer.parseInt(vals[2]); // 处理模式.
			TransferCustom tc = new TransferCustom();
			tc.setIdx(idx); // 顺序.
			tc.setFK_Node(nodeid); // 节点.
			tc.setWorkID(workid); // 工作ID.
			tc.setWorker(vals[3]); // 工作人员.
			tc.setSubFlowNo(subFlow); // 子流程.
			tc.setMyPK(tc.getFK_Node() + "_" + tc.getWorkID() + "_" + idx);
			tc.setTodolistModel(TodolistModel.forValue(todomodel)); // 处理模式.
			tc.Save();
			idx++;
			// 设置抄送
			String[] ccs = vals[4].split("[,]", -1);
			String[] ccNames = vals[5].split("[,]", -1);
			SelectAccper sa = new SelectAccper();
			sa.Delete(SelectAccperAttr.FK_Node, nodeid, SelectAccperAttr.WorkID, workid, SelectAccperAttr.AccType, 1);

			cidx = 0;
			for (int i = 0; i < ccs.length; i++) {
				if (StringHelper.isNullOrWhiteSpace(ccs[i]) || ccs[i].equals("0")) {
					continue;
				}

				sa = new SelectAccper();
				sa.setMyPK(nodeid + "_" + workid + "_" + ccs[i]);
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
	 * 是否可以删除该流程？
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @return 是否可以删除该流程
	 * @throws Exception
	 */
	public static boolean Flow_IsCanDeleteFlowInstance(String flowNo, long workid, String userNo) throws Exception {
		if (userNo.equals("admin")) {
			return true;
		}

		Flow fl = new Flow(flowNo);
		if (fl.getFlowDeleteRole() == FlowDeleteRole.AdminOnly.getValue()) {
			return false;
		}

		// 是否是用户管理员?
		if (fl.getFlowDeleteRole() == FlowDeleteRole.AdminAppOnly.getValue()) {
			if (userNo.indexOf("admin") == 0) {
				return true; // 这里判断不严谨,如何判断是否是一个应用管理员使用admin+部门编号来确定的. 比如：
								// admin3701
			} else {
				return false;
			}
		}

		// 是否是发起人.
		if (fl.getFlowDeleteRole() == FlowDeleteRole.ByMyStarter.getValue()) {
			Paras ps = new Paras();
			ps.SQL = "SELECT WorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr()
					+ "WorkID AND Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter";
			ps.Add("WorkID", workid);
			ps.Add("Starter", userNo);
			String user = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (user == null) {
				return false;
			}
			return true;
		}

		// 按照节点是否启用删除按钮来计算.
		if (fl.getFlowDeleteRole() == FlowDeleteRole.ByNodeSetting.getValue()) {
			Paras ps = new Paras();
			ps.SQL = "SELECT WorkID FROM WF_GenerWorkerlist A, WF_Node B  WHERE A.FK_Node=B.NodeID  AND B.DelEnable=1  AND A.WorkID="
					+ SystemConfig.getAppCenterDBVarStr() + "WorkID AND A.FK_Emp=" + SystemConfig.getAppCenterDBVarStr()
					+ "FK_Emp";
			ps.Add("WorkID", workid);
			ps.Add("FK_Emp", userNo);
			String user = BP.DA.DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (user == null) {
				return false;
			}
			return true;
		}
		return false;
	}

	/// #region 与流程有关的接口

	/**
	 * 增加一个评论
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            父工作ID
	 * @param msg
	 *            消息
	 * @param empNo
	 *            评论人编号
	 * @param empName
	 *            评论人名称
	 * @return 插入ID主键
	 * @throws Exception
	 */
	public static String Flow_BBSAdd(String flowNo, long workid, long fid, String msg, String empNo, String empName)
			throws Exception {
		return Glo.AddToTrack(ActionType.FlowBBS, flowNo, workid, fid, 0, null, empNo, empName, 0, null, empNo, empName,
				msg, null);
	}

	/**
	 * 删除一个评论.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param mypk
	 *            主键
	 * @return 返回删除信息.AddToTrack
	 */
	public static String Flow_BBSDelete(String flowNo, String mypk, String username) {
		Paras pss = new Paras();
		pss.SQL = "SELECT EMPFROM FROM ND" + Integer.parseInt(flowNo) + "Track WHERE MyPK="
				+ SystemConfig.getAppCenterDBVarStr() + "MyPK ";
		pss.Add("MyPK", mypk);
		String str = BP.DA.DBAccess.RunSQLReturnString(pss);
		if (str.equals(username) || username.equals(str)) {
			Paras ps = new Paras();
			ps.SQL = "DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE MyPK="
					+ SystemConfig.getAppCenterDBVarStr() + "MyPK ";
			ps.Add("MyPK", mypk);
			BP.DA.DBAccess.RunSQL(ps);
			return "删除成功.";
		} else {
			return "删除失败,仅能删除自己评论!";
		}
	}

	/**
	 * 取消设置关注
	 * 
	 * @param workid
	 *            要取消设置的工作ID
	 * @throws Exception
	 */
	public static void Flow_Focus(long workid) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		int i = gwf.RetrieveFromDBSources();
		if (i == 0) {
			throw new RuntimeException("@ 设置关注错误：没有找到 WorkID= " + workid + " 的实例。");
		}
		String isFocus = gwf.GetParaString("F_" + WebUser.getNo(), "0"); // edit
																			// by
																			// liuxc,2016-10-22,修复关注/取消关注逻辑错误

		if (isFocus.equals("0")) {
			gwf.SetPara("F_" + WebUser.getNo(), "1");
		} else {
			gwf.SetPara("F_" + WebUser.getNo(), "0");
		}

		gwf.DirectUpdate();
	}

	/**
	 * 设置委托
	 * 
	 * @param Author
	 *            接收委托人账号
	 * @param AuthorWay
	 *            委托方式：0不授权， 1完全授权，2，指定流程范围授权.
	 * @param AuthorFlows
	 *            委托流程编号，格式：001,002,003
	 * @param AuthorDate
	 *            委托开始时间，默认当前时间
	 * @param AuthorToDate
	 *            委托结束时间
	 * @return 设置结果：成功true,失败 false
	 * @throws Exception
	 */
	public static boolean Flow_AuthorSave(String Author, int AuthorWay, String AuthorFlows, String AuthorDate,
			String AuthorToDate) throws Exception {
		if (WebUser.getNo() == null) {
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}

		WFEmp emp = new WFEmp(WebUser.getNo());
		emp.setAuthor(Author);
		emp.setAuthorWay(AuthorWay);
		emp.setAuthorDate(BP.DA.DataType.getCurrentDate());

		if (!StringHelper.isNullOrEmpty(AuthorFlows)) {
			emp.setAuthorFlows(AuthorFlows);
		}
		if (!StringHelper.isNullOrEmpty(AuthorDate)) {
			emp.setAuthorFlows(AuthorDate);
		}
		if (!StringHelper.isNullOrEmpty(AuthorToDate)) {
			emp.setAuthorToDate(AuthorToDate);
		}
		int i = emp.Save();

		return i >= 0 ? true : false;
	}

	/*
	 * 
	 * */
	public static String Flow_GetFlowNoByFlowMark(String flowMark) {
		String sql = "SELECT No FROM WF_Flow WHERE FlowMark='" + flowMark + "'";
		return DBAccess.RunSQLReturnStringIsNull(sql, null);
	}

	/**
	 * 取消委托当前登录人的委托信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean Flow_AuthorCancel() throws Exception {
		if (WebUser.getNo() == null) {
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
	 * 获取当前登录人的委托人
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_AuthorEmps() throws Exception {
		if (WebUser.getNo() == null) {
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}
		return BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + WebUser.getNo() + "'");
	}

	/**
	 * 获取委托给当前登录人的流程待办信息
	 * 
	 * @param empNo
	 *            授权人员编号
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_AuthorEmpWorks(String empNo) throws Exception {
		if (WebUser.getNo() == null) {
			throw new RuntimeException("@ 非法用户，请执行登录后再试。");
		}

		WFEmp emp = new WFEmp(empNo);
		if (!StringHelper.isNullOrEmpty(emp.getAuthor()) && emp.getAuthor().equals(WebUser.getNo())
				&& emp.getAuthorIsOK() == true) {
			String sql = "";
			String wfSql = "  WFState=" + WFState.Askfor.getValue() + " OR WFState=" + WFState.Runing.getValue()
					+ "  OR WFState=" + WFState.AskForReplay.getValue() + " OR WFState=" + WFState.Shift.getValue()
					+ " OR WFState=" + WFState.ReturnSta.getValue() + " OR WFState=" + WFState.Fix.getValue();
			switch (emp.getHisAuthorWay()) {
			case All:
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' AND TaskSta!=1  ORDER BY ADT DESC";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' ORDER BY ADT DESC";
				}
				break;
			case SpecFlows:
				if (BP.WF.Glo.getIsEnableTaskPool() == true) {
					sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' AND  FK_Flow IN " + emp.getAuthorFlows() + " AND TaskSta!=0    ORDER BY ADT DESC";
				} else {
					sql = "SELECT * FROM WF_EmpWorks WHERE (" + wfSql + ") AND FK_Emp='" + emp.getNo()
							+ "' AND  FK_Flow IN " + emp.getAuthorFlows() + "   ORDER BY ADT DESC";
				}
				break;
			default:
				break;
			}
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
		return null;
	}

	/// #endregion 与流程有关的接口

	/// #endregion 与流程有关的接口

	/// #region get 属性节口
	/**
	 * 获得流程运行过程中的参数
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @return 如果没有就返回null,有就返回@参数名0=参数值0@参数名1=参数值1
	 * @throws Exception
	 */
	public static String GetFlowParas(int nodeID, long workid) throws Exception {
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "SELECT Paras FROM WF_GenerWorkerlist WHERE FK_Node=" + dbstr + "FK_Node AND WorkID=" + dbstr
				+ "WorkID";
		ps.Add(GenerWorkerListAttr.FK_Node, nodeID);
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		return DBAccess.RunSQLReturnStringIsNull(ps, null);
	}

	/**
	 * 发起流程
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param ht
	 *            节点表单:主表数据以Key Value 方式传递(可以为空)
	 * @param workDtls
	 *            节点表单:从表数据，从表名称与从表单的从表编号要对应(可以为空)
	 * @param nextNodeID
	 *            发起后要跳转到的节点(可以为空)
	 * @param nextWorker
	 *            发起后要跳转到的节点并指定的工作人员(可以为空)
	 * @return 发送到第二个节点的执行信息
	 * @throws Exception
	 */
	public static SendReturnObjs Node_StartWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, int nextNodeID,
			String nextWorker) throws Exception {
		return Node_StartWork(flowNo, ht, workDtls, nextNodeID, nextWorker, 0, null);
	}

	/**
	 * 发起流程
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param htWork
	 *            节点表单:主表数据以Key Value 方式传递(可以为空)
	 * @param workDtls
	 *            节点表单:从表数据，从表名称与从表单的从表编号要对应(可以为空)
	 * @param nextNodeID
	 *            发起后要跳转到的节点(可以为空)
	 * @param nextWorker
	 *            发起后要跳转到的节点并指定的工作人员(可以为空)
	 * @param parentWorkID
	 *            父流程的workid，如果没有可以为0
	 * @param parentFlowNo
	 *            父流程的编号，如果没有可以为空
	 * @return 发送到第二个节点的执行信息
	 * @throws Exception
	 */
	public static SendReturnObjs Node_StartWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls,
			int nextNodeID, String nextWorker, long parentWorkID, String parentFlowNo) throws Exception {
		// 给全局变量赋值.
		BP.WF.Glo.setSendHTOfTemp(htWork);

		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		// 父流程编号.
		parentFlowNo = TurnFlowMarkToFlowNo(parentFlowNo);
		Flow fl = new Flow(flowNo);
		Work wk = fl.NewWork();
		long workID = wk.getOID();
		if (htWork != null) {
			for (Object str : htWork.keySet()) {

				if (StartWorkAttr.OID.equals(str) || StartWorkAttr.CDT.equals(str) || StartWorkAttr.MD5.equals(str)
						|| StartWorkAttr.Emps.equals(str) || StartWorkAttr.FID.equals(str)
						|| StartWorkAttr.FK_Dept.equals(str) || StartWorkAttr.PRI.equals(str)
						|| StartWorkAttr.Rec.equals(str) || StartWorkAttr.Title.equals(str)) {
					continue;
				} else {
				}
				wk.SetValByKey(str.toString(), htWork.get(str));
			}
		}

		wk.setOID(workID);
		if (workDtls != null) {
			// 保存从表
			for (DataTable dt : workDtls.Tables) {
				for (MapDtl dtl : wk.getHisMapDtls().ToJavaList()) {
					if (dt.TableName != dtl.getNo()) {
						continue;
					}
					// 获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = (GEDtl) ((daDtls.getGetNewEntity() instanceof GEDtl) ? daDtls.getGetNewEntity()
							: null);
					daDtl.setRefPK(String.valueOf(wk.getOID()));
					// 为从表复制数据.
					for (DataRow dr : dt.Rows) {
						daDtl.ResetDefaultVal();
						daDtl.setRefPK(String.valueOf(wk.getOID()));

						// 明细列.
						for (DataColumn dc : dt.Columns) {
							// 设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); // 插入数据.
					}
				}
			}
		}

		WorkNode wn = new WorkNode(wk, fl.getHisStartNode());

		Node nextNoode = null;
		if (nextNodeID != 0) {
			nextNoode = new Node(nextNodeID);
		}

		SendReturnObjs objs = wn.NodeSend(nextNoode, nextWorker);
		if (parentWorkID != 0) {
			DBAccess.RunSQL("UPDATE WF_GenerWorkFlow SET PWorkID=" + parentWorkID + ",PFlowNo='" + parentFlowNo
					+ "' WHERE WorkID=" + objs.getVarWorkID());
		}

		/// #region 更新发送参数.
		if (htWork != null) {
			String paras = "";
			for (Object key : htWork.keySet()) {
				paras += "@" + key + "=" + htWork.get(key).toString();
			}

			if (StringHelper.isNullOrEmpty(paras) == false) {
				String dbstr = SystemConfig.getAppCenterDBVarStr();
				Paras ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr
						+ "WorkID AND FK_Node=" + dbstr + "FK_Node";
				ps.Add(GenerWorkerListAttr.Paras, paras);
				ps.Add(GenerWorkerListAttr.WorkID, workID);
				ps.Add(GenerWorkerListAttr.FK_Node, Integer.parseInt(flowNo + "01"));
				try {
					DBAccess.RunSQL(ps);
				} catch (java.lang.Exception e) {
					GenerWorkerList gwl = new GenerWorkerList();
					gwl.CheckPhysicsTable();
					DBAccess.RunSQL(ps);
				}
			}
		}
		return objs;
	}

	/**
	 * 创建一个空白的WorkID
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param userNo
	 *            用户编号
	 * @return
	 * @throws Exception
	 */
	public static long Node_CreateBlankWork(String flowNo, String userNo) throws Exception {
		return Node_CreateBlankWork(flowNo, null, null, userNo);
	}

	/**
	 * 创建WorkID
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param ht
	 *            表单参数，可以为null。
	 * @param workDtls
	 *            明细表参数，可以为null。
	 * @param starter
	 *            流程的发起人
	 * @param title
	 *            创建工作时的标题，如果为null，就按设置的规则生成。
	 * @param parentWorkID
	 *            父流程的WorkID,如果没有父流程就传入为0.
	 * @param parentFID
	 *            父流程的FID,如果没有父流程就传入为0.
	 * @param parentFlowNo
	 *            父流程的流程编号,如果没有父流程就传入为null.
	 * @param jumpToNode
	 *            要跳转到的节点,如果没有则为0.
	 * @param jumpToEmp
	 *            要跳转到的人员,如果没有则为null.
	 * @return 为开始节点创建工作后产生的WorkID.
	 */

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title,
			long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp, int jumpToNode)
			throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo,
				parentNodeID, parentEmp, jumpToNode, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title,
			long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp)
			throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo,
				parentNodeID, parentEmp, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title,
			long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID) throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo,
				parentNodeID, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title,
			long parentWorkID, long parentFID, String parentFlowNo) throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, parentFlowNo, 0,
				null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title,
			long parentWorkID, long parentFID) throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, parentFID, null, 0, null, 0,
				null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title,
			long parentWorkID) throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, parentWorkID, 0, null, 0, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter, String title)
			throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, title, 0, 0, null, 0, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String starter)
			throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, starter, null, 0, 0, null, 0, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls) throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, null, null, 0, 0, null, 0, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, Hashtable ht) throws Exception {
		return Node_CreateBlankWork(flowNo, ht, null, null, null, 0, 0, null, 0, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo) throws Exception {
		return Node_CreateBlankWork(flowNo, null, null, null, null, 0, 0, null, 0, null, 0, null);
	}

	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String starter,
			String title, long parentWorkID, long parentFID, String parentFlowNo, int parentNodeID, String parentEmp,
			int jumpToNode, String jumpToEmp) throws Exception {
		// 把一些其他的参数也增加里面去,传递给ccflow.
		java.util.Hashtable<String, Comparable> htPara = new java.util.Hashtable<String, Comparable>();

		if (parentWorkID != 0) {
			htPara.put(StartFlowParaNameList.PWorkID, parentWorkID);
		}
		if (parentFID != 0) {
			htPara.put(StartFlowParaNameList.PFID, parentFID);
		}

		if (parentFlowNo != null) {
			htPara.put(StartFlowParaNameList.PFlowNo, parentFlowNo);
		}
		if (parentNodeID != 0) {
			htPara.put(StartFlowParaNameList.PNodeID, parentNodeID);
		}
		if (parentEmp != null) {
			htPara.put(StartFlowParaNameList.PEmp, parentEmp);
		}

		// 给全局变量赋值.
		if (null != ht) {
			BP.WF.Glo.setSendHTOfTemp(ht);
		}

		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		// 转化成编号
		parentFlowNo = TurnFlowMarkToFlowNo(parentFlowNo);

		if (parentFlowNo == null) {
			parentFlowNo = "";
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();

		if (StringHelper.isNullOrEmpty(starter)) {
			starter = WebUser.getNo();
		}

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());

		// 下一个工作人员。
		Emp empStarter = new Emp(starter);
		Work wk = fl.NewWork(empStarter, htPara);
		long workID = wk.getOID();
		if (ht != null) {
			for (Object str : ht.keySet()) {
				if (StartWorkAttr.OID.equals(str) || StartWorkAttr.CDT.equals(str) || StartWorkAttr.MD5.equals(str)
						|| StartWorkAttr.Emps.equals(str) || StartWorkAttr.FID.equals(str)
						|| StartWorkAttr.FK_Dept.equals(str) || StartWorkAttr.PRI.equals(str)
						|| StartWorkAttr.Rec.equals(str) || StartWorkAttr.Title.equals(str)) {
					continue;
				} else {
				}
				wk.SetValByKey(str.toString(), ht.get(str));
			}
		}
		wk.setOID(workID);
		if (workDtls != null) {
			// 保存从表
			for (DataTable dt : workDtls.Tables) {
				for (MapDtl dtl : wk.getHisMapDtls().ToJavaList()) {
					if (dt.TableName != dtl.getNo()) {
						continue;
					}
					// 获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = (GEDtl) ((daDtls.getGetNewEntity() instanceof GEDtl) ? daDtls.getGetNewEntity()
							: null);
					daDtl.setRefPK(String.valueOf(wk.getOID()));

					// 为从表复制数据.
					for (DataRow dr : dt.Rows) {
						daDtl.ResetDefaultVal();
						daDtl.setRefPK(String.valueOf(wk.getOID()));

						// 明细列.
						for (DataColumn dc : dt.Columns) {
							// 设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); // 插入数据.
					}
				}
			}
		}

		/// #endregion 赋值

		Paras ps = new Paras();
		// 执行对报表的数据表WFState状态的更新,让它为runing的状态.
		if (StringHelper.isNullOrEmpty(title) == false) {
			ps = new Paras();
			ps.SQL = "UPDATE " + fl.getPTable() + " SET PFlowNo=" + dbstr + "PFlowNo,PWorkID=" + dbstr
					+ "PWorkID,WFState=" + dbstr + "WFState,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.PFlowNo, parentFlowNo);
			ps.Add(GERptAttr.PWorkID, parentWorkID);
			// ps.Add(GERptAttr.PFID, parentFID); PFID=" + dbstr + "PFID,

			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.Title, title);
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		} else {
			ps = new Paras();
			ps.SQL = "UPDATE " + fl.getPTable() + " SET PFlowNo=" + dbstr + "PFlowNo,PWorkID=" + dbstr
					+ "PWorkID,WFState=" + dbstr + "WFState,FK_Dept=" + dbstr + "FK_Dept,Title=" + dbstr
					+ "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.PFlowNo, parentFlowNo);
			ps.Add(GERptAttr.PWorkID, parentWorkID);
			// ps.Add(GERptAttr.PFID, parentFID);

			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.FK_Dept, empStarter.getFK_Dept());
			ps.Add(GERptAttr.Title, BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		}

		// 设置父流程信息.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(wk.getOID());
		int i = gwf.RetrieveFromDBSources();

		// 将流程信息提前写入wf_GenerWorkFlow,避免查询不到
		gwf.setFlowName(fl.getName());
		gwf.setFK_Flow(flowNo);
		gwf.setFK_FlowSort(fl.getFK_FlowSort());
		gwf.setSysType(fl.getSysType());

		gwf.setFK_Dept(empStarter.getFK_Dept());
		gwf.setDeptName(empStarter.getFK_DeptText());
		gwf.setFK_Node(fl.getStartNodeID());
		gwf.setNodeName(nd.getName());
		gwf.setWFState(WFState.Blank);
		if (StringHelper.isNullOrEmpty(title)) {
			gwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
		} else {
			gwf.setTitle(title);
		}
		gwf.setStarter(empStarter.getNo());
		gwf.setStarterName(empStarter.getName());
		gwf.setRDT(DataType.getCurrentDataTimess());
		gwf.setPWorkID(parentWorkID);
		gwf.setPFID(parentFID);
		gwf.setPFlowNo(parentFlowNo);
		gwf.setPNodeID(parentNodeID);
		if (gwf.Update() == 0)
			gwf.Insert();

		// 插入待办.
		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setWorkID(wk.getOID());
		gwl.setFK_Node(nd.getNodeID());
		gwl.setFK_Emp(empStarter.getNo());
		i = gwl.RetrieveFromDBSources();

		gwl.setFK_EmpText(empStarter.getName());
		gwl.setFK_NodeText(nd.getName());
		gwl.setFID(0);
		gwl.setFK_Flow(fl.getNo());
		gwl.setFK_Dept(empStarter.getFK_Dept());
		gwl.setSDT(DataType.getCurrentDataTimess());
		gwl.setDTOfWarning(DataType.getCurrentDataTime());
		gwl.setRDT(DataType.getCurrentDataTimess());
		gwl.setIsEnable(true);
		gwl.setIsPass(false);
		gwl.setPRI(gwf.getPRI());
		if (gwl.Update() == 0) {
			gwl.Insert();
		}

		if (parentWorkID != 0) {
			// 设置父流程信息
			BP.WF.Dev2Interface.SetParentInfo(flowNo, wk.getOID(), parentFlowNo, parentWorkID, parentNodeID, parentEmp);
		}

		// 如果有跳转.
		if (jumpToNode != 0) {
			BP.WF.Dev2Interface.Node_SendWork(flowNo, wk.getOID(), null, null, jumpToNode, jumpToEmp);
		}

		return wk.getOID();
	}

	/**
	 * 创建开始节点工作 创建后可以创办人形成一个待办.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param htWork
	 *            表单参数，可以为null。
	 * @param workDtls
	 *            明细表参数，可以为null。
	 * @param flowStarter
	 *            流程的发起人，如果为null就是当前人员。
	 * @param title
	 *            创建工作时的标题，如果为null，就按设置的规则生成。
	 * @param parentWorkID
	 *            父流程的WorkID,如果没有父流程就传入为0.
	 * @param parentFlowNo
	 *            父流程的流程编号,如果没有父流程就传入为null.
	 * @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception
	 */

	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork, DataSet workDtls, String flowStarter,
			String title, long parentWorkID, String parentFlowNo) throws Exception {
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, title, parentWorkID, parentFlowNo, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork, DataSet workDtls, String flowStarter,
			String title, long parentWorkID) throws Exception {
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, title, parentWorkID, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork, DataSet workDtls, String flowStarter)
			throws Exception {
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork, DataSet workDtls) throws Exception {
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, null, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork) throws Exception {
		return Node_CreateStartNodeWork(flowNo, htWork, null, null, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo) throws Exception {
		return Node_CreateStartNodeWork(flowNo, null, null, null, null, 0, null, 0);
	}

	public static long Node_CreateStartNodeWork(String flowNo, java.util.Hashtable htWork, DataSet workDtls,
			String flowStarter, String title, long parentWorkID, String parentFlowNo, int parentNDFrom)
			throws Exception {
		// 给全局变量赋值.
		BP.WF.Glo.setSendHTOfTemp(htWork);

		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		// 转化成编号
		parentFlowNo = TurnFlowMarkToFlowNo(parentFlowNo);

		if (StringHelper.isNullOrEmpty(flowStarter)) {
			flowStarter = WebUser.getNo();
		}

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());

		// 下一个工作人员。
		Emp emp = new Emp(flowStarter);
		Work wk = fl.NewWork(flowStarter);

		/// #region 给各个属性-赋值
		if (htWork != null) {
			for (Object str : htWork.keySet()) {

				// switch (str)
				if (StartWorkAttr.OID.equals(str) || StartWorkAttr.CDT.equals(str) || StartWorkAttr.MD5.equals(str)
						|| StartWorkAttr.Emps.equals(str) || StartWorkAttr.FID.equals(str)
						|| StartWorkAttr.FK_Dept.equals(str) || StartWorkAttr.PRI.equals(str)
						|| StartWorkAttr.Rec.equals(str) || StartWorkAttr.Title.equals(str)) {
					continue;
				} else {
				}
				wk.SetValByKey(str.toString(), htWork.get(str));
			}
			// 将参数保存到业务表
			wk.DirectUpdate();
		}

		if (workDtls != null) {
			// 保存从表
			for (DataTable dt : workDtls.Tables) {
				for (MapDtl dtl : wk.getHisMapDtls().ToJavaList()) {
					if (dt.TableName != dtl.getNo()) {
						continue;
					}
					// 获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = (GEDtl) ((daDtls.getGetNewEntity() instanceof GEDtl) ? daDtls.getGetNewEntity()
							: null);
					daDtl.setRefPK((new Long(wk.getOID())).toString());

					// 为从表复制数据.
					for (DataRow dr : dt.Rows) {
						daDtl.ResetDefaultVal();
						daDtl.setRefPK((new Long(wk.getOID())).toString());

						// 明细列.
						for (DataColumn dc : dt.Columns) {
							// 设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); // 插入数据.
					}
				}
			}
		}
		/// #region 为开始工作创建待办
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(wk.getOID());
		int i = gwf.RetrieveFromDBSources();

		gwf.setFlowName(fl.getName());
		gwf.setFK_Flow(flowNo);
		gwf.setFK_FlowSort(fl.getFK_FlowSort());
		gwf.setSysType(fl.getSysType());

		gwf.setFK_Dept(emp.getFK_Dept());
		gwf.setDeptName(emp.getFK_DeptText());
		gwf.setFK_Node(fl.getStartNodeID());

		gwf.setNodeName(nd.getName());

		// 默认是空白流程
		gwf.setWFSta(WFSta.Etc);
		gwf.setWFState(WFState.Blank);
		// 保存到草稿
		if (fl.getDraftRole() == DraftRole.SaveToDraftList) {
			gwf.setWFState(WFState.Draft);
		} else if (fl.getDraftRole() == DraftRole.SaveToTodolist) {
			// 保存到待办
			gwf.setWFSta(WFSta.Runing);
			gwf.setWFState(WFState.Runing);
		}

		if (StringHelper.isNullOrEmpty(title)) {
			gwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
		} else {
			gwf.setTitle(title);
		}

		gwf.setStarter(emp.getNo());
		gwf.setStarterName(emp.getName());
		gwf.setRDT(DataType.getCurrentDataTime());

		if (htWork != null && htWork.containsKey("PRI") == true) {
			gwf.setPRI(Integer.parseInt(htWork.get("PRI").toString()));
		}

		if (htWork != null && htWork.containsKey("SDTOfNode") == true)
		// 节点应完成时间
		{
			gwf.setSDTOfNode(htWork.get("SDTOfNode").toString());
		}

		if (htWork != null && htWork.containsKey("SDTOfFlow") == true)
		// 流程应完成时间
		{
			gwf.setSDTOfNode(htWork.get("SDTOfFlow").toString());
		}

		gwf.setPWorkID(parentWorkID);
		gwf.setPFlowNo(parentFlowNo);
		gwf.setPNodeID(parentNDFrom);
		if (i == 0) {
			gwf.Insert();
		} else {
			gwf.Update();
		}

		// 产生工作列表.
		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setWorkID(wk.getOID());
		if (gwl.RetrieveFromDBSources() == 0) {
			gwl.setFK_Emp(emp.getNo());
			gwl.setFK_EmpText(emp.getName());

			gwl.setFK_Node(nd.getNodeID());
			gwl.setFK_NodeText(nd.getName());
			gwl.setFID(0);

			gwl.setFK_Flow(fl.getNo());
			gwl.setFK_Dept(emp.getFK_Dept());

			gwl.setSDT(DataType.getCurrentDataTimess());
			gwl.setDTOfWarning(DataType.getCurrentDataTime());
			gwl.setRDT(DataType.getCurrentDataTimess());
			gwl.setIsEnable(true);

			gwl.setIsPass(false);
			// gwl.Sender = WebUser.getNo();
			gwl.setPRI(gwf.getPRI());
			gwl.Insert();
		}

		// 执行对报表的数据表WFState状态的更新
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,WFSta=" + dbstr + "WFSta,Title="
				+ dbstr + "Title,FK_Dept=" + dbstr + "FK_Dept,PFlowNo=" + dbstr + "PFlowNo,PWorkID=" + dbstr
				+ "PWorkID WHERE OID=" + dbstr + "OID";

		// 默认启用草稿
		if (fl.getDraftRole() == DraftRole.None) {
			ps.Add("WFState", WFState.Blank.getValue());
			ps.Add("WFSta", WFSta.Etc.getValue());
		} else if (fl.getDraftRole() == DraftRole.SaveToDraftList) {
			// 保存到草稿
			ps.Add("WFState", WFState.Draft.getValue());
			ps.Add("WFSta", WFSta.Etc.getValue());
		} else if (fl.getDraftRole() == DraftRole.SaveToTodolist) {
			// 保存到待办
			ps.Add("WFState", WFState.Runing.getValue());
			ps.Add("WFSta", WFSta.Runing.getValue());
		}
		ps.Add("Title", gwf.getTitle());
		ps.Add("FK_Dept", gwf.getFK_Dept());

		ps.Add("PFlowNo", gwf.getPFlowNo());
		ps.Add("PWorkID", gwf.getPWorkID());

		ps.Add("OID", wk.getOID());
		DBAccess.RunSQL(ps);

		// 写入日志.
		// #region 更新发送参数.
		if (htWork != null) {
			String paras = "";
			for (Object key : htWork.keySet()) {
				paras += "@" + key + "=" + htWork.get(key).toString();
			}

			if (StringHelper.isNullOrEmpty(paras) == false) {
				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr
						+ "WorkID AND FK_Node=" + dbstr + "FK_Node";
				ps.Add(GenerWorkerListAttr.Paras, paras);
				ps.Add(GenerWorkerListAttr.WorkID, wk.getOID());
				ps.Add(GenerWorkerListAttr.FK_Node, nd.getNodeID());
				DBAccess.RunSQL(ps);
			}
		}
		return wk.getOID();
	}

	public static long Node_CreateStartNodeWork(String flowNo, Hashtable htWork, DataSet workDtls, String flowStarter,
			String title) throws Exception {
		return Node_CreateStartNodeWork(flowNo, htWork, workDtls, flowStarter, title, 0, null, 0);
	}

	/// <summary>
	/// 增加待办人员
	/// </summary>
	/// <param name="workid">工作ID</param>
	/// <param name="todoEmps">要增加的处理人员,多个人员用逗号分开.</param>
	public static void Node_AddTodolist(long workid, String todoEmps) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() == WFState.Complete)
			throw new Exception("流程：" + gwf.getTitle() + "已经完成,您不能增加接受人.");

		// #region 增加待办人员.

		GenerWorkerList gwl = new GenerWorkerList();
		gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

		// String[] emps = todoEmps.split(, ','); //分开字符串.

		String[] emps = todoEmps.split("[,]", -1);
		String tempStrs = ""; // 临时变量，防止重复插入.
		for (String emp : emps) {
			if (DataType.IsNullOrEmpty(emp) == true)
				continue;
			if (tempStrs.contains("," + emp + ",") == true)
				continue;

			// 插入待办.
			gwl = new GenerWorkerList();
			gwl.setWorkID(workid);
			gwl.setFK_Node(gwf.getFK_Node());
			gwl.setFK_Emp(emp);
			int i = gwl.RetrieveFromDBSources();
			if (i == 1)
				continue;

			Emp empEn = new Emp(emp);

			gwl.setFK_EmpText(empEn.getName());
			gwl.setFK_NodeText(gwf.getNodeName());
			// gwl.FID = 0;
			gwl.setFK_Flow(gwf.getFK_Flow());
			gwl.setFK_Dept(empEn.getFK_Dept());
			gwl.setFK_DeptT(empEn.getFK_DeptText());

			// gwl.SDT = "无";
			// gwl.DTOfWarning = DataType.CurrentDataTime;
			gwl.setIsEnable(true);
			gwl.setIsPass(false);
			// gwl.setPRI(gwf.PRI;
			gwl.Save();

			tempStrs += "," + emp + ",";
		}
		// #endregion 增加待办人员.
	}

	/**
	 * 执行工作发送
	 * 
	 * @param fk_flow
	 *            工作编号
	 * @param workID
	 *            工作ID
	 * @param ht
	 *            节点表单数据
	 * @param dsDtl
	 *            节点表单从表数据
	 * @return 返回发送结果
	 * @throws Exception
	 */
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID) throws Exception {
		return Node_SendWork(fk_flow, workID, null, null);
	}

	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, Hashtable ht) throws Exception {
		return Node_SendWork(fk_flow, workID, ht, null);
	}

	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, java.util.Hashtable ht, DataSet dsDtl)
			throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		return Node_SendWork(fk_flow, workID, ht, dsDtl, 0, null);
	}

	/**
	 * 发送工作
	 * 
	 * @param nodeID
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param toNodeID
	 *            发送到的节点编号，如果是0就让ccflow自动计算.
	 * @param toEmps
	 *            发送到的人员,多个人员用逗号分开比如：zhangsan,lisi. 如果是null则表示让ccflow自动计算.
	 * @return 返回执行信息
	 * @throws Exception
	 */
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, int toNodeID, String toEmps)
			throws Exception {
		// 转化成编号.
		// fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		return Node_SendWork(fk_flow, workID, null, null, toNodeID, toEmps);
	}

	/**
	 * 发送工作
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param htWork
	 *            节点表单数据(Hashtable中的key与节点表单的字段名相同,value 就是字段值)
	 * @return 执行信息
	 * @throws Exception
	 */
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, java.util.Hashtable htWork, int toNodeID,
			String nextWorkers) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		return Node_SendWork(fk_flow, workID, htWork, null, toNodeID, nextWorkers, WebUser.getNo(), WebUser.getName(),
				WebUser.getFK_Dept(), WebUser.getFK_DeptName(), null);
	}

	/**
	 * 发送工作
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param htWork
	 *            节点表单数据(Hashtable中的key与节点表单的字段名相同,value 就是字段值)
	 * @param workDtls
	 *            节点表单明从表数据(dataset可以包含多个table，每个table的名称与从表名称相同，列名与从表的字段相同,
	 *            OID,RefPK列需要为空或者null )
	 * @param toNodeID
	 *            到达的节点，如果是0表示让ccflow自动寻找，否则就按照该参数发送。
	 * @param nextWorkers
	 *            下一步的接受人，如果多个人员用逗号分开，比如:zhangsan,lisi,
	 *            如果为空，则标识让ccflow按照节点访问规则自动寻找。
	 * @return 执行信息
	 * @throws Exception
	 */
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, java.util.Hashtable htWork,
			DataSet workDtls, int toNodeID, String nextWorkers) throws Exception {
		return Node_SendWork(fk_flow, workID, htWork, workDtls, toNodeID, nextWorkers, WebUser.getNo(),
				WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName(), null);
	}

	/**
	 * 发送工作
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param htWork
	 *            节点表单数据(Hashtable中的key与节点表单的字段名相同,value 就是字段值)
	 * @param workDtls
	 *            节点表单明从表数据(dataset可以包含多个table，每个table的名称与从表名称相同，列名与从表的字段相同,
	 *            OID,RefPK列需要为空或者null )
	 * @param toNodeID
	 *            到达的节点，如果是0表示让ccflow自动寻找，否则就按照该参数发送。
	 * @param nextWorkers
	 *            下一步的接受人，如果多个人员用逗号分开，比如:zhangsan,lisi,
	 *            如果为空，则标识让ccflow按照节点访问规则自动寻找。
	 * @param execUserNo
	 *            执行人编号
	 * @param execUserName
	 *            执行人名称
	 * @param execUserDeptNo
	 *            执行人部门名称
	 * @param execUserDeptName
	 *            执行人部门编号
	 * @return 发送的结果对象
	 * @throws Exception
	 */
	public static SendReturnObjs Node_SendWork(String fk_flow, long workID, java.util.Hashtable htWork,
			DataSet workDtls, int toNodeID, String nextWorkers, String execUserNo, String execUserName,
			String execUserDeptNo, String execUserDeptName, String title) throws Exception {
		// 给临时的发送变量赋值，解决带有参数的转向。
		Glo.setSendHTOfTemp(htWork);

		// 转化成编号.
		// fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		int currNodeId = Dev2Interface.Node_GetCurrentNodeID(fk_flow, workID);
		if (htWork != null) {
			BP.WF.Dev2Interface.Node_SaveWork(fk_flow, currNodeId, workID, htWork, workDtls);
		}

		// 变量.
		Node nd = new Node(currNodeId);
		// nd.WorkID = workID;
		Work sw = nd.getHisWork();
		sw.setOID(workID);
		sw.RetrieveFromDBSources();

		// @于庆海翻译.
		Node ndOfToNode = null; // 到达节点ID
		if (toNodeID != 0) {
			ndOfToNode = new Node(toNodeID);
		}
		// 补偿性修复.
		if (nd.getHisRunModel() != RunModel.SubThread) {
			if (sw.getFID() != 0) {
				sw.DirectUpdate();
			}
		}

		SendReturnObjs objs;
		// 执行流程发送.
		WorkNode wn = new WorkNode(sw, nd);
		wn.setExecer(execUserNo);
		wn.setExecerName(execUserName);
		wn.title = title; // 设置标题，有可能是从外部传递过来的标题.
		wn.SendHTOfTemp = htWork;

		if (ndOfToNode == null) {
			objs = wn.NodeSend(null, nextWorkers);
		} else {
			objs = wn.NodeSend(new Node(toNodeID), nextWorkers);
		}

		/// #region 更新发送参数.
		if (htWork != null) {
			String dbstr = SystemConfig.getAppCenterDBVarStr();
			Paras ps = new Paras();

			String paras = "";
			for (Object key : htWork.keySet()) {
				paras += "@" + key + "=" + htWork.get(key).toString();
				if (WorkSysFieldAttr.SysSDTOfFlow.equals(key)) {
					ps = new Paras();
					ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfFlow=" + dbstr + "SDTOfFlow WHERE WorkID=" + dbstr
							+ "WorkID";
					ps.Add(GenerWorkFlowAttr.SDTOfFlow, htWork.get(key).toString());
					ps.Add(GenerWorkerListAttr.WorkID, workID);
					DBAccess.RunSQL(ps);

				} else if (WorkSysFieldAttr.SysSDTOfNode.equals(key)) {
					ps = new Paras();
					ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfNode=" + dbstr + "SDTOfNode WHERE WorkID=" + dbstr
							+ "WorkID";
					ps.Add(GenerWorkFlowAttr.SDTOfNode, htWork.get(key).toString());
					ps.Add(GenerWorkerListAttr.WorkID, workID);
					DBAccess.RunSQL(ps);

					ps = new Paras();
					ps.SQL = "UPDATE WF_GenerWorkerlist SET SDT=" + dbstr + "SDT WHERE WorkID=" + dbstr
							+ "WorkID AND FK_Node=" + dbstr + "FK_Node";
					ps.Add(GenerWorkerListAttr.SDT, htWork.get(key).toString());
					ps.Add(GenerWorkerListAttr.WorkID, workID);
					ps.Add(GenerWorkerListAttr.FK_Node, objs.getVarToNodeID());
					DBAccess.RunSQL(ps);
				} else {
				}
			}

			if (StringHelper.isNullOrEmpty(paras) == false && Glo.getIsEnableTrackRec() == true) {
				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr
						+ "WorkID AND FK_Node=" + dbstr + "FK_Node";
				ps.Add(GenerWorkerListAttr.Paras, paras);
				ps.Add(GenerWorkerListAttr.WorkID, workID);
				ps.Add(GenerWorkerListAttr.FK_Node, nd.getNodeID());
				DBAccess.RunSQL(ps);
			}
		}
		return objs;
	}

	/**
	 * 增加在队列工作中增加一个处理人. 这个处理顺序系统已经自动处理了.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeID
	 *            工作ID
	 * @param workid
	 *            workid
	 * @param fid
	 *            fid
	 * @param empNo
	 *            要增加的处理人编号
	 * @param empName
	 *            要增加的处理人名称
	 * @throws Exception
	 */
	public static void Node_InsertOrderEmp(String flowNo, int nodeID, long workid, long fid, String empNo,
			String empName) throws Exception {
		GenerWorkerList gwl = new GenerWorkerList();
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, nodeID);
		if (i == 0) {
			throw new RuntimeException("@没有找到当前工作人员的待办，请检查该流程是否已经运行到该节点上来了。");
		}

		gwl.setIsPassInt(100);
		gwl.setIsEnable(true);
		gwl.setFK_Emp(empNo);
		gwl.setFK_EmpText(empName);

		try {
			gwl.Insert();
		} catch (java.lang.Exception e) {
			throw new RuntimeException("@该人员已经存在处理队列中，您不能增加.");
		}

		// 开始更新他们的顺序, 首先从数据库里获取他们的顺序. lxl职位由小到大
		String sql = "SELECT No,Name FROM Port_Emp WHERE No IN (SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID="
				+ workid + " AND FK_Node=" + nodeID + " AND IsPass >=100 ) ORDER BY IDX desc";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 100;
		for (DataRow dr : dt.Rows) {
			idx++;
			String myEmpNo = dr.getValue(0).toString();
			sql = "UPDATE WF_GenerWorkerList SET IsPass=" + idx + " WHERE FK_Emp='" + myEmpNo + "' AND WorkID=" + workid
					+ " AND FK_Node=" + nodeID;
			BP.DA.DBAccess.RunSQL(sql);
		}
	}

	/**
	 * 把抄送写入待办列表
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param ccToEmpNo
	 *            抄送给
	 * @param ccToEmpName
	 *            抄送给名称
	 * @return
	 * @throws Exception
	 */
	public static String Node_CC_WriteTo_Todolist(int ndFrom, int ndTo, long workID, String ccToEmpNo,
			String ccToEmpName) throws Exception {
		return Node_CC_WriteTo_CClist(ndFrom, ndTo, workID, ccToEmpNo, ccToEmpName, "", "");
	}

	/**
	 * 执行抄送
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param toEmpNo
	 *            抄送人员编号
	 * @param toEmpName
	 *            抄送人员人员名称
	 * @param msgTitle
	 *            标题
	 * @param msgDoc
	 *            内容
	 * @return 执行信息
	 * @throws Exception
	 */
	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String toEmpNo, String toEmpName,
			String msgTitle, String msgDoc) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0) {
			Node nd = new Node(fk_node);
			gwf.setFK_Node(fk_node);
			gwf.setFK_Flow(nd.getFK_Flow());
			gwf.setFlowName(nd.getFlowName());
			gwf.setNodeName(nd.getName());
		}

		Node fromNode = new Node(fk_node);

		CCList list = new CCList();
		list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID + "_"
																	// + fk_node
																	// + "_" +
																	// empNo;
		list.setFK_Flow(gwf.getFK_Flow());
		list.setFlowName(gwf.getFlowName());
		list.setFK_Node(fk_node);
		list.setNodeName(gwf.getNodeName());
		list.setTitle(msgTitle);
		list.setDoc(msgDoc);
		list.setCCTo(toEmpNo);
		list.setCCToName(toEmpName);

		// 增加抄送人部门.
		Emp emp = new Emp(toEmpNo);
		list.setCCToDept(emp.getFK_Dept());
		list.setRDT(DataType.getCurrentDataTime());
		list.setRec(WebUser.getNo());
		list.setWorkID(gwf.getWorkID());
		list.setFID(gwf.getFID());
		list.setPFlowNo(gwf.getPFlowNo());
		list.setPWorkID(gwf.getPWorkID());
		// list.NDFrom = ndFrom;

		// 是否要写入待办.
		if (fromNode.getCCWriteTo() == CCWriteTo.CCList) {
			list.setInEmpWorks(false); // added by liuxc,2015.7.6
		} else {
			list.setInEmpWorks(true); // added by liuxc,2015.7.6
		}

		// 写入待办和写入待办与抄送列表,状态不同
		if (fromNode.getCCWriteTo() == CCWriteTo.All || fromNode.getCCWriteTo() == CCWriteTo.Todolist) {
			list.setHisSta(fromNode.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
		}

		if (fromNode.getIsEndNode() == true) // 结束节点只写入抄送列表
		{
			list.setHisSta(CCSta.UnRead);
			list.setInEmpWorks(false);
		}

		try {
			list.Insert();
		} catch (java.lang.Exception e) {
			list.CheckPhysicsTable();
			list.Update();
		}

		//
		BP.WF.Dev2Interface.Port_SendMsg(toEmpNo, msgTitle, msgDoc, "CC" + gwf.getFK_Node() + "_" + gwf.getWorkID(),
				SMSMsgType.CC, gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());

		// 记录日志.
		Glo.AddToTrack(ActionType.CC, gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), gwf.getNodeName(),
				WebUser.getNo(), WebUser.getName(), gwf.getFK_Node(), gwf.getNodeName(), toEmpNo, toEmpName, msgTitle,
				null);

		return "已经成功的把工作抄送给:" + toEmpNo + "," + toEmpName;
	}

	/**
	 * 执行抄送
	 * 
	 * @param fk_node
	 *            节点
	 * @param workID
	 *            工作ID
	 * @param title
	 *            标题
	 * @param doc
	 *            内容
	 * @param toEmps
	 *            到人员(zhangsan,张三;lisi,李四;wangwu,王五;)
	 * @param toDepts
	 *            到部门，格式:001,002,003
	 * @param toStations
	 *            到岗位 格式:001,002,003
	 * @param toStations
	 *            到权限组 格式:001,002,003
	 * @throws Exception
	 */
	public static String Node_CC_WriteTo_CClist(int ndFrom, int ndTo, long workID, String toEmpNo, String toEmpName,
			String msgTitle, String msgDoc) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0) {
			Node nd = new Node(ndTo);
			gwf.setFK_Node(ndTo);
			gwf.setFK_Flow(nd.getFK_Flow());
			gwf.setFlowName(nd.getFlowName());
			gwf.setNodeName(nd.getName());
		}
		Node fromNode = new Node(ndFrom);
		CCList list = new CCList();
		list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID + "_"
																	// + fk_node
																	// + "_" +
																	// empNo);
		list.setFK_Flow(gwf.getFK_Flow());
		list.setFlowName(gwf.getFlowName());
		list.setFK_Node(ndTo);
		list.setNodeName(gwf.getNodeName());
		list.setTitle(msgTitle);
		list.setDoc(msgDoc);
		list.setCCTo(toEmpNo);
		list.setCCToName(toEmpName);

		// 增加抄送人部门.
		Emp emp = new Emp(toEmpNo);
		list.setCCToDept(emp.getFK_Dept());
		list.setRDT(DataType.getCurrentDataTime());
		list.setRec(WebUser.getNo());
		list.setWorkID(gwf.getWorkID());
		list.setFID(gwf.getFID());
		list.setPFlowNo(gwf.getPFlowNo());
		list.setPWorkID(gwf.getPWorkID());
		list.setNDFrom(ndFrom);
		list.setInEmpWorks(fromNode.getCCWriteTo() == CCWriteTo.CCList ? false : true); // added
																						// by
																						// liuxc,2015.7.6
		// 写入待办和写入待办与抄送列表,状态不同
		if (fromNode.getCCWriteTo() == CCWriteTo.All || fromNode.getCCWriteTo() == CCWriteTo.Todolist) {
			list.setHisSta(fromNode.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
		}
		if (fromNode.getIsEndNode() == true) // 结束节点只写入抄送列表
		{
			list.setHisSta(CCSta.UnRead);
			list.setInEmpWorks(false);
		}

		try {
			list.Insert();
		} catch (java.lang.Exception e) {
			list.CheckPhysicsTable();
			list.Update();
		}
		BP.WF.Dev2Interface.Port_SendMsg(toEmpNo, msgTitle, msgDoc, "CC" + gwf.getFK_Node() + "_" + gwf.getWorkID(),
				SMSMsgType.CC, gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());
		// 记录日志.
		Glo.AddToTrack(ActionType.CC, gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), gwf.getNodeName(),
				WebUser.getNo(), WebUser.getName(), gwf.getFK_Node(), gwf.getNodeName(), toEmpNo, toEmpName, msgTitle,
				null);

		return "已经成功的把工作抄送给:" + toEmpNo + "," + toEmpName;
	}

	public static String Node_CC_WriteTo_CClist(int fk_node, long workID, String title, String doc, String toEmps,
			String toDepts, String toStations, String toGroups) throws Exception {

		Node nd = new Node(fk_node);

		// 计算出来曾经抄送过的人.
		String sql = "SELECT CCTo FROM WF_CCList WHERE FK_Node=" + fk_node + " AND WorkID=" + workID;
		DataTable mydt = DBAccess.RunSQLReturnTable(sql);
		String toAllEmps = ",";
		for (DataRow dr : mydt.Rows) {
			toAllEmps += dr.getValue(0).toString() + ",";
		}

		// 录制本次抄送的人员.
		String ccRec = "";

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0) {
			gwf.setFK_Node(fk_node);
			gwf.setFK_Flow(nd.getFK_Flow());
			gwf.setFlowName(nd.getFlowName());
			gwf.setNodeName(nd.getName());
		}

		/// #region 处理抄送到人员.
		if (toEmps != null) {
			String[] emps = toEmps.split("[;]", -1);
			for (String empStr : emps) {
				if (StringHelper.isNullOrEmpty(empStr) == true || empStr.contains(",") == false) {
					continue;
				}

				String[] strs = empStr.split("[,]", -1);
				String empNo = strs[0];
				String empName = strs[1];

				if (toAllEmps.contains("," + empNo + ",") == true) {
					continue;
				}

				CCList list = new CCList();
				list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID
																			// +
																			// "_"
																			// +
																			// fk_node
																			// +
																			// "_"
																			// +
																			// empNo);
																			// list.setFK_Flow(gwf.getFK_Flow());
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

				// 是否要写入待办.
				if (nd.getCCWriteTo() == CCWriteTo.CCList) {
					list.setInEmpWorks(false); // added by liuxc,2015.7.6
				} else {
					list.setInEmpWorks(true); // added by liuxc,2015.7.6
				}

				// 写入待办和写入待办与抄送列表,状态不同
				if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist) {
					list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
				}

				if (nd.getIsEndNode() == true) // 结束节点只写入抄送列表
				{
					list.setHisSta(CCSta.UnRead);
					list.setInEmpWorks(false);
				}

				try {
					list.Insert();
				} catch (java.lang.Exception e) {
					list.CheckPhysicsTable();
					list.Update();
				}

				ccRec += "" + list.getCCToName() + ";";
				// 人员编号,加入这个集合.
				toAllEmps += empNo + ",";
			}
		}

		/// #endregion 处理抄送到人员.

		/// #region 处理抄送到部门.
		if (toDepts != null) {
			toDepts = toDepts.replace(";", ",");

			String[] depts = toDepts.split("[,]", -1);
			for (String deptNo : depts) {
				if (StringHelper.isNullOrEmpty(deptNo) == true) {
					continue;
				}

				sql = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE FK_Dept='" + deptNo + "'";
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					String empNo = dr.getValue(0).toString();
					String empName = dr.getValue(1).toString();
					if (toAllEmps.contains("," + empNo + ",") == true) {
						continue;
					}

					CCList list = new CCList();
					list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID
																				// +
																				// "_"
																				// +
																				// fk_node
																				// +
																				// "_"
																				// +
																				// empNo);
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

					// 是否要写入待办.
					if (nd.getCCWriteTo() == CCWriteTo.CCList) {
						list.setInEmpWorks(false); // added by liuxc,2015.7.6
					} else {
						list.setInEmpWorks(true); // added by liuxc,2015.7.6
					}

					// 写入待办和写入待办与抄送列表,状态不同
					if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist) {
						list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
					}

					if (nd.getIsEndNode() == true) // 结束节点只写入抄送列表
					{
						list.setHisSta(CCSta.UnRead);
						list.setInEmpWorks(false);
					}

					try {
						list.Insert();
					} catch (java.lang.Exception e2) {
						list.CheckPhysicsTable();
						list.Update();
					}

					// 录制本次抄送到的人员.
					ccRec += "" + list.getCCToName() + ";";

					// 人员编号,加入这个集合.
					toAllEmps += empNo + ",";
				}
			}
		}

		/// #endregion 处理抄送到部门.

		/// #region 处理抄送到岗位.
		if (toStations != null) {
			toStations = toStations.replace(";", ",");
			String[] stas = toStations.split("[,]", -1);
			for (String staNo : stas) {
				if (StringHelper.isNullOrEmpty(staNo) == true) {
					continue;
				}

				sql = "SELECT No,Name, a.FK_Dept FROM Port_Emp a, " + Glo.getEmpStation()
						+ " B  WHERE a.No=B.FK_Emp AND B.FK_Station='" + staNo + "'";

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					String empNo = dr.getValue(0).toString();
					String empName = dr.getValue(1).toString();
					if (toAllEmps.contains("," + empNo + ",") == true) {
						continue;
					}

					CCList list = new CCList();
					list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID
																				// +
																				// "_"
																				// +
																				// fk_node
																				// +
																				// "_"
																				// +
																				// empNo);
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

					// 是否要写入待办.
					if (nd.getCCWriteTo() == CCWriteTo.CCList) {
						list.setInEmpWorks(false); // added by liuxc,2015.7.6
					} else {
						list.setInEmpWorks(true); // added by liuxc,2015.7.6
					}

					// 写入待办和写入待办与抄送列表,状态不同
					if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist) {
						list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
					}

					if (nd.getIsEndNode() == true) // 结束节点只写入抄送列表
					{
						list.setHisSta(CCSta.UnRead);
						list.setInEmpWorks(false);
					}

					try {
						list.Insert();
					} catch (java.lang.Exception e3) {
						list.CheckPhysicsTable();
						list.Update();
					}

					// 录制本次抄送到的人员.
					ccRec += "" + list.getCCToName() + ";";

					// 人员编号,加入这个集合.
					toAllEmps += empNo + ",";
				}
			}
		}

		/// #endregion.

		/// #region 抄送到组.
		if (toGroups != null) {
			toGroups = toGroups.replace(";", ",");
			String[] groups = toGroups.split("[,]", -1);

			for (String group : groups) {
				if (StringHelper.isNullOrEmpty(group) == true) {
					continue;
				}

				// 解决分组下的岗位人员.
				sql = "SELECT a.No,a.Name, A.FK_Dept FROM Port_Emp A, " + Glo.getEmpStation()
						+ " B, GPM_GroupStation C  WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Group='"
						+ group + "'";
				sql += " UNION ";
				sql += "SELECT A.No, A.Name, A.FK_Dept FROM Port_Emp A, GPM_GroupEmp B  WHERE A.No=B.FK_Emp AND B.FK_Group='"
						+ group + "'";

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					String empNo = dr.getValue(0).toString();
					String empName = dr.getValue(1).toString();
					if (toAllEmps.contains("," + empNo + ",") == true) {
						continue;
					}

					CCList list = new CCList();
					list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID
																				// +
																				// "_"
																				// +
																				// fk_node
																				// +
																				// "_"
																				// +
																				// empNo);
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

					// 是否要写入待办.
					if (nd.getCCWriteTo() == CCWriteTo.CCList) {
						list.setInEmpWorks(false); // added by liuxc,2015.7.6
					} else {
						list.setInEmpWorks(true); // added by liuxc,2015.7.6
					}

					// 写入待办和写入待办与抄送列表,状态不同
					if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist) {
						list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
					}

					if (nd.getIsEndNode() == true) // 结束节点只写入抄送列表
					{
						list.setHisSta(CCSta.UnRead);
						list.setInEmpWorks(false);
					}

					try {
						list.Insert();
					} catch (java.lang.Exception e4) {
						list.CheckPhysicsTable();
						list.Update();
					}

					// 录制本次抄送到的人员.
					ccRec += "" + list.getCCToName() + ";";

					// 人员编号,加入这个集合.
					toAllEmps += empNo + ",";
				}
			}
		}

		/// #endregion 抄送到组

		return ccRec;

		//// 记录日志.
		// Glo.AddToTrack(ActionType.CC, gwf.FK_Flow, workID, gwf.FID,
		//// gwf.FK_Node, gwf.NodeName,
		// WebUser.getNo(), WebUser.Name, gwf.FK_Node, gwf.NodeName, toAllEmps,
		//// toAllEmps, title, null);
	}

	/**
	 * 执行删除
	 * 
	 * @param mypk
	 *            删除
	 */
	public static void Node_CC_DoDel(String mypk) {
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM WF_CCList WHERE MyPK=" + SystemConfig.getAppCenterDBVarStr() + "MyPK";
		ps.Add(CCListAttr.MyPK, mypk);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/**
	 * 设置抄送状态
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @param empNo
	 *            人员编号
	 * @param sta
	 *            状态
	 */
	public static void Node_CC_SetSta(int nodeID, long workid, String empNo, CCSta sta) {
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_CCList   SET Sta=" + dbstr + "Sta,CDT=" + dbstr + "CDT WHERE WorkID=" + dbstr
				+ "WorkID AND FK_Node=" + dbstr + "FK_Node AND CCTo=" + dbstr + "CCTo";
		ps.Add(CCListAttr.Sta, sta.getValue());
		ps.Add(CCListAttr.CDT, DataType.getCurrentDataTime());
		ps.Add(CCListAttr.WorkID, workid);
		ps.Add(CCListAttr.FK_Node, nodeID);
		ps.Add(CCListAttr.CCTo, empNo);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/**
	 * 执行读取
	 * 
	 * @param mypk
	 */
	public static void Node_CC_SetRead(String mypk) {
		if (StringHelper.isNullOrEmpty(mypk)) {
			return;
		}

		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_CCList SET Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta  WHERE MyPK="
				+ SystemConfig.getAppCenterDBVarStr() + "MyPK";
		ps.Add(CCListAttr.Sta, CCSta.Read.getValue());
		ps.Add(CCListAttr.MyPK, mypk);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/**
	 * 设置抄送读取
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @param empNo
	 *            读取人员编号
	 */
	public static void Node_CC_SetRead(int nodeID, long workid, String empNo) {
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_CCList SET Sta=" + SystemConfig.getAppCenterDBVarStr() + "Sta  WHERE WorkID="
				+ SystemConfig.getAppCenterDBVarStr() + "WorkID AND FK_Node=" + SystemConfig.getAppCenterDBVarStr()
				+ "FK_Node AND CCTo=" + SystemConfig.getAppCenterDBVarStr() + "CCTo";
		ps.Add(CCListAttr.Sta, CCSta.Read.getValue());
		ps.Add(CCListAttr.WorkID, workid);
		ps.Add(CCListAttr.FK_Node, nodeID);
		ps.Add(CCListAttr.CCTo, empNo);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET IsRead=1 WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr()
				+ "WorkID AND FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND FK_Emp="
				+ SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		ps.Add(GenerWorkerListAttr.FK_Node, nodeID);
		ps.Add(GenerWorkerListAttr.FK_Emp, empNo);

		DBAccess.RunSQL(ps);
	}

	/**
	 * 执行抄送
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param toEmpNo
	 *            抄送给人员编号
	 * @param toEmpName
	 *            抄送给人员名称
	 * @param msgTitle
	 *            消息标题
	 * @param msgDoc
	 *            消息内容
	 * @param pFlowNo
	 *            父流程编号(可以为null)
	 * @param pWorkID
	 *            父流程WorkID(可以为0)
	 * @return
	 * @throws Exception
	 */
	public static String Node_CC(String fk_flow, int fk_node, long workID, String toEmpNo, String toEmpName,
			String msgTitle, String msgDoc, String pFlowNo, long pWorkID) throws Exception {
		Flow fl = new Flow(fk_flow);
		Node nd = new Node(fk_node);

		CCList list = new CCList();
		list.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID())); // workID + "_"
																	// + fk_node
																	// + "_" +
																	// empNo);
		list.setFK_Flow(fk_flow);
		list.setFlowName(fl.getName());
		list.setFK_Node(fk_node);
		list.setNodeName(nd.getName());
		list.setTitle(msgTitle);
		list.setDoc(msgDoc);
		list.setCCTo(toEmpNo);
		list.setCCToName(toEmpName);
		list.setInEmpWorks(nd.getCCWriteTo() == CCWriteTo.CCList ? false : true); // added
																					// by
																					// liuxc,2015.7.6
		// 写入待办和写入待办与抄送列表,状态不同
		if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist) {
			list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
		}
		if (nd.getIsEndNode() == true) // 结束节点只写入抄送列表
		{
			list.setHisSta(CCSta.UnRead);
			list.setInEmpWorks(false);
		}
		// 增加抄送人部门.
		Emp emp = new Emp(toEmpNo);
		list.setCCToDept(emp.getFK_Dept());

		list.setRDT(DataType.getCurrentDataTime());
		list.setRec(WebUser.getNo());
		list.setWorkID(workID);
		list.setFID(0);
		list.setPFlowNo(pFlowNo);
		list.setPWorkID(pWorkID);

		try {
			list.Insert();
		} catch (java.lang.Exception e) {
			list.CheckPhysicsTable();
			list.Update();
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		// 记录日志.
		Glo.AddToTrack(ActionType.CC, fk_flow, workID, 0, nd.getNodeID(), nd.getName(), WebUser.getNo(),
				WebUser.getName(), nd.getNodeID(), nd.getName(), toEmpNo, toEmpName, msgTitle, null);

		// 发送邮件.
		BP.WF.Dev2Interface.Port_SendMsg(toEmpNo, WebUser.getName() + "把工作:" + gwf.getTitle(), "抄送:" + msgTitle,
				"CC" + nd.getNodeID() + "_" + workID + "_", BP.WF.SMSMsgType.CC, gwf.getFK_Flow(), gwf.getFK_Node(),
				gwf.getWorkID(), gwf.getFID());

		return "已经成功的把工作抄送给:" + toEmpNo + "," + toEmpName;

	}

	/**
	 * 删除草稿
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @throws Exception
	 */
	public static void Node_DeleteDraft(String fk_flow, long workID) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		// 设置引擎表.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 1) {
			if (gwf.getFK_Node() != Integer.parseInt(fk_flow + "01")) {
				throw new RuntimeException("@该流程非草稿流程不能删除:" + gwf.getTitle());
			}

			if (gwf.getWFState() != WFState.Draft) {
				throw new RuntimeException("@非草稿状态不能删除");
			}

			gwf.Delete();
		}

		// 删除流程.
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Flow fl = new Flow(fk_flow);
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM " + fl.getPTable() + " WHERE OID=" + dbstr + "OID ";
		ps.Add(GERptAttr.OID, workID);
		DBAccess.RunSQL(ps);
	}

	/**
	 * 把草稿设置待办
	 * 
	 * @param fk_flow
	 * @param workID
	 * @throws Exception
	 */
	public static void Node_SetDraft2Todolist(String fk_flow, long workID) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		// 设置引擎表.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 1
				&& (gwf.getWFState() == WFState.Draft || gwf.getWFState() == WFState.Blank)) {
			if (gwf.getFK_Node() != Integer.parseInt(fk_flow + "01")) {
				throw new RuntimeException("@设置待办错误，只有在开始节点时才能设置待办，现在的节点是:" + gwf.getNodeName());
			}

			gwf.setTodoEmps(BP.Web.WebUser.getNo() + "," + WebUser.getName() + ";");
			gwf.setTodoEmpsNum(1);
			gwf.setWFState(WFState.Runing);
			gwf.Update();
			// 重置标题
			Flow_ReSetFlowTitle(fk_flow, gwf.getFK_Node(), gwf.getWorkID());
		}
	}

	/**
	 * 设置当前工作的应该完成日期.
	 * 
	 * @param workID
	 *            设置的WorkID.
	 * @param sdt
	 *            应完成日期
	 */
	public static void Node_SetSDT(long workID, String sdt) {
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET SDT=" + SystemConfig.getAppCenterDBVarStr() + "SDT WHERE WorkID="
				+ SystemConfig.getAppCenterDBVarStr() + "WorkID AND IsPass=0";
		ps.Add("SDT", sdt);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET SDTOfNode=" + SystemConfig.getAppCenterDBVarStr()
				+ "SDTOfNode WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID ";
		ps.Add("SDTOfNode", sdt);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

	}

	/**
	 * 设置当前工作状态为草稿,如果启用了草稿, 请在开始节点的表单保存按钮下增加上它. 注意:必须是在开始节点时调用.
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @throws Exception
	 */
	public static void Node_SetDraft(String fk_flow, long workID) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		// 设置引擎表.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("@工作丢失..");
		}

		if (gwf.getWFState() == WFState.Blank) {
			if (gwf.getFK_Node() != Integer.parseInt(fk_flow + "01")) {
				throw new RuntimeException("@设置草稿错误，只有在开始节点时才能设置草稿，现在的节点是:" + gwf.getTitle());
			}

			gwf.setTodoEmps(BP.Web.WebUser.getNo() + "," + WebUser.getName() + ";");
			gwf.setTodoEmpsNum(1);
			gwf.setWFState(WFState.Draft);
			gwf.Update();

			GenerWorkerList gwl = new GenerWorkerList();
			gwl.setWorkID(workID);
			gwl.setFK_Node(Integer.parseInt(fk_flow + "01"));
			gwl.setFK_Emp(WebUser.getNo());
			if (gwl.RetrieveFromDBSources() == 0) {
				gwl.setFK_EmpText(WebUser.getName());
				gwl.setIsPassInt(0);
				gwl.setSDT(DataType.getCurrentDataTimess());
				gwl.setDTOfWarning(DataType.getCurrentDataTime());
				gwl.setRDT(DataType.getCurrentDataTimess());
				gwl.setIsEnable(true);
				gwl.setIsRead(true);
				gwl.setIsPass(false);
				gwl.Insert();
			}
		}
	}

	/**
	 * 保存参数，向工作流引擎传入的参数变量.
	 * 
	 * @param workID
	 *            工作ID
	 * @param paras
	 *            参数
	 * @return
	 * @throws Exception
	 */

	public static boolean Flow_SaveParas(long workID, String paras) throws Exception {
		AtPara ap = new AtPara(paras);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		for (String key : ap.getHisHT().keySet()) {
			gwf.SetPara(key, ap.GetValStrByKey(key));
		}
		gwf.Update();
		return true;
	}

	/**
	 * 保存
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @return 返回保存的信息
	 * @throws Exception
	 */
	public static String Node_SaveWork(String fk_flow, int fk_node, long workID) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		return Node_SaveWork(fk_flow, fk_node, workID, new java.util.Hashtable(), null);
	}

	/**
	 * 保存
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            workid
	 * @param wk
	 *            节点表单参数
	 * @return
	 * @throws Exception
	 */
	public static String Node_SaveWork(String fk_flow, int fk_node, long workID, java.util.Hashtable wk)
			throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		return Node_SaveWork(fk_flow, fk_node, workID, wk, null);
	}

	/**
	 * 保存
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param htWork
	 *            工作数据
	 * @return 返回执行信息
	 * @throws Exception
	 */
	public static String Node_SaveWork(String fk_flow, int fk_node, long workID, java.util.Hashtable htWork,
			DataSet dsDtls) throws Exception {
		if (htWork == null) {
			return "参数错误，保存失败。";
		}

		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		try {
			Node nd = new Node(fk_node);
			Work wk = nd.getHisWork();
			if (workID != 0) {
				wk.setOID(workID);
				wk.RetrieveFromDBSources();
			}
			wk.ResetDefaultVal();

			if (htWork != null) {
				for (Object str : htWork.keySet()) {

					if (StartWorkAttr.OID.equals(str) || StartWorkAttr.CDT.equals(str) || StartWorkAttr.MD5.equals(str)
							|| StartWorkAttr.Emps.equals(str) || StartWorkAttr.FID.equals(str)
							|| StartWorkAttr.FK_Dept.equals(str) || StartWorkAttr.PRI.equals(str)
							|| StartWorkAttr.Rec.equals(str) || StartWorkAttr.Title.equals(str)) {
						continue;
					}

					// 处理表单字段 key值大小写
					if (wk.getRow().containsKey(str)) {
						// wk.SetValByKey(str.toString(), htWork.get(str));
						wk.SetValByKey(str.toString(), htWork.get(str));
					} else {
						// wk.getRow().put(str.toString(), htWork.get(str));
						wk.getRow().put(str.toString(), htWork.get(str));
					}
				}
			}
			wk.setRec(WebUser.getNo());
			wk.setRecText(WebUser.getName());
			wk.SetValByKey(StartWorkAttr.FK_Dept, WebUser.getFK_Dept());
			wk.BeforeSave();
			wk.Save();
			/// #region 保存从表
			if (dsDtls != null) {
				// 保存从表
				for (DataTable dt : dsDtls.Tables) {
					for (MapDtl dtl : wk.getHisMapDtls().ToJavaList()) {
						if (dt.TableName != dtl.getNo()) {
							continue;
						}
						// 获取dtls
						GEDtls daDtls = new GEDtls(dtl.getNo());
						daDtls.Delete(GEDtlAttr.RefPK, workID); // 清除现有的数据.

						// 为从表复制数据.
						for (DataRow dr : dt.Rows) {
							GEDtl daDtl = (GEDtl) ((daDtls.getGetNewEntity() instanceof GEDtl)
									? daDtls.getGetNewEntity() : null);
							daDtl.setRefPK((new Long(workID)).toString());
							// 明细列.
							for (DataColumn dc : dt.Columns) {
								// 设置属性.
								daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
							}

							daDtl.ResetDefaultVal();

							daDtl.setRefPK(String.valueOf(wk.getOID()));
							daDtl.setRDT(DataType.getCurrentDataTime());

							// 执行保存.
							daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); // 插入数据.
						}
					}
				}
			}
			/// #region 更新发送参数.
			if (htWork != null) {
				String paras = "";
				for (Object key : htWork.keySet()) {
					paras += "@" + key + "=" + htWork.get(key).toString();
				}

				if (StringHelper.isNullOrEmpty(paras) == false && Glo.getIsEnableTrackRec() == true) {
					String dbstr = SystemConfig.getAppCenterDBVarStr();
					Paras ps = new Paras();
					ps.SQL = "UPDATE WF_GenerWorkerlist SET AtPara=" + dbstr + "Paras WHERE WorkID=" + dbstr
							+ "WorkID AND FK_Node=" + dbstr + "FK_Node";
					ps.Add(GenerWorkerListAttr.Paras, paras);
					ps.Add(GenerWorkerListAttr.WorkID, workID);
					ps.Add(GenerWorkerListAttr.FK_Node, nd.getNodeID());
					DBAccess.RunSQL(ps);
				}
			}

			/// #endregion 更新发送参数.

			if (nd.getSaveModel() == SaveModel.NDAndRpt) {
				// 如果保存模式是节点表与Node与Rpt表.
				WorkNode wn = new WorkNode(wk, nd);
				GERpt rptGe = nd.getHisFlow().getHisGERpt();
				rptGe.SetValByKey("OID", workID);
				wn.rptGe = rptGe;
				if (rptGe.RetrieveFromDBSources() == 0) {
					rptGe.SetValByKey("OID", workID);
					wn.DoCopyRptWork(wk);

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
						rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getNo() + "," + WebUser.getName());
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDOnly) {
						rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getNo());
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly) {
						rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getName());
					}

					rptGe.SetValByKey(GERptAttr.FlowStarter, WebUser.getNo());
					rptGe.SetValByKey(GERptAttr.FlowStartRDT, DataType.getCurrentDataTime());
					rptGe.SetValByKey(GERptAttr.WFState, 0);
					rptGe.SetValByKey(GERptAttr.FK_NY, DataType.getCurrentYearMonth());
					rptGe.SetValByKey(GERptAttr.FK_Dept, WebUser.getFK_Dept());
					rptGe.Insert();
				} else {
					wn.DoCopyRptWork(wk);
					rptGe.Update();
				}
			}
			// 获取表单树的数据
			BP.WF.WorkNode workNode = new WorkNode(workID, fk_node);
			Work treeWork = workNode.CopySheetTree();
			if (treeWork != null) {
				wk.Copy(treeWork);
			}

			// 获取该节点是是否是绑定表单方案, 如果流程节点中的字段与绑定表单的字段相同时赋值
			if (nd.getFormType() == NodeFormType.SheetTree || nd.getFormType() == NodeFormType.RefOneFrmTree) {
				FrmNodes nds = new FrmNodes(fk_flow, fk_node);
				for (FrmNode item : nds.ToJavaList()) {
					GEEntity en = null;
					try {
						en = new GEEntity(item.getFK_Frm());
						en.setPKVal(workID);
						if (en.RetrieveFromDBSources() == 0)
							continue;
					} catch (Exception ex) {
						continue;
					}

					Attrs frmAttrs = en.getEnMap().getAttrs();
					Attrs wkAttrs = wk.getEnMap().getAttrs();
					for (Attr wkattr : wkAttrs) {
						String wkattrKey = wkattr.getKey();
						if (wkattrKey.equals(StartWorkAttr.OID) || wkattrKey.equals(StartWorkAttr.FID)
								|| wkattrKey.equals(StartWorkAttr.CDT) || wkattrKey.equals(StartWorkAttr.RDT)
								|| wkattrKey.equals(StartWorkAttr.MD5) || wkattrKey.equals(StartWorkAttr.Emps)
								|| wkattrKey.equals(StartWorkAttr.FK_Dept) || wkattrKey.equals(StartWorkAttr.PRI)
								|| wkattrKey.equals(StartWorkAttr.Rec) || wkattrKey.equals(StartWorkAttr.Title)
								|| wkattrKey.equals(GERptAttr.FK_NY) || wkattrKey.equals(GERptAttr.FlowEmps)
								|| wkattrKey.equals(GERptAttr.FlowStarter) || wkattrKey.equals(GERptAttr.FlowStartRDT)
								|| wkattrKey.equals(GERptAttr.WFState))
							continue;
						for (Attr attr : frmAttrs) {
							if (wkattrKey.equals(attr.getKey())) {
								wk.SetValByKey(wkattrKey, en.GetValStrByKey(attr.getKey()));
								break;
							}

						}

					}

				}
				wk.Update();
			}

			/// #region 处理保存后事件
			boolean isHaveSaveAfter = false;
			try {
				// 处理表单保存后.
				String s = nd.getMapData().DoEvent(FrmEventList.SaveAfter, wk, null);

				// 执行保存前事件.
				s += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.SaveAfter, nd, wk, null);

				if (s != null) {
					/* 如果不等于null,说明已经执行过数据保存，就让其从数据库里查询一次。 */
					wk.RetrieveFromDBSources();
					isHaveSaveAfter = true;
				}
			} catch (Exception ex) {
				return "err@在执行保存后的事件期间出现错误:" + ex.getMessage();
			}

			/// #region 为开始工作创建待办.
			if (nd.getIsStartNode() == true) {
				GenerWorkFlow gwf = new GenerWorkFlow();
				Flow fl = new Flow(fk_flow);
				if (fl.getDraftRole() == DraftRole.None) {
					return "保存成功";
				}

				// 规则设置为写入待办，将状态置为运行中，其他设置为草稿.
				WFState wfState = WFState.Blank;
				if (fl.getDraftRole() == DraftRole.SaveToDraftList) {
					wfState = WFState.Draft;
				}
				if (fl.getDraftRole() == DraftRole.SaveToTodolist) {
					wfState = WFState.Runing;
				}

				gwf.setWorkID(workID);
				int i = gwf.RetrieveFromDBSources();
				if (i == 0) {
					gwf.setFlowName(fl.getName());
					gwf.setFK_Flow(fk_flow);
					gwf.setFK_FlowSort(fl.getFK_FlowSort());
					gwf.setSysType(fl.getSysType());

					gwf.setFK_Node(fk_node);
					gwf.setNodeName(nd.getName());
					gwf.setWFState(wfState);

					gwf.setFK_Dept(WebUser.getFK_Dept());
					gwf.setDeptName(WebUser.getFK_DeptName());
					gwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
					gwf.setStarter(WebUser.getNo());
					gwf.setStarterName(WebUser.getName());
					gwf.setRDT(DataType.getCurrentDataTimess());
					gwf.Insert();
					// 产生工作列表.
					GenerWorkerList gwl = new GenerWorkerList();
					gwl.setWorkID(workID);
					gwl.setFK_Emp(WebUser.getNo());
					gwl.setFK_EmpText(WebUser.getName());

					gwl.setFK_Node(fk_node);
					gwl.setFK_NodeText(nd.getName());
					gwl.setFID(0);

					gwl.setFK_Flow(fk_flow);
					gwl.setFK_Dept(WebUser.getFK_Dept());
					gwl.setSDT(DataType.getCurrentDataTimess());
					gwl.setDTOfWarning(DataType.getCurrentDataTime());
					gwl.setRDT(DataType.getCurrentDataTimess());
					gwl.setIsEnable(true);

					gwl.setIsPass(false);
					// gwl.Sender = WebUser.getNo();
					gwl.setPRI(gwf.getPRI());
					gwl.Insert();
				} else {
					if (gwf.getWFState() != WFState.ReturnSta) {
						gwf.setWFState(wfState);
						gwf.DirectUpdate();
					}
				}
			}

			/// #endregion 为开始工作创建待办

			/// #endregion
			return "保存成功.";
		} catch (RuntimeException ex) {
			return "保存失败:" + ex.getMessage();
		}
	}

	/**
	 * 保存独立表单
	 * 
	 * @param fk_mapdata
	 *            独立表单ID
	 * @param workID
	 *            工作ID
	 * @param htData
	 *            独立表单数据Key Value 格式存放.
	 * @return 返回执行信息
	 * @throws Exception
	 */
	public static void Node_SaveFlowSheet(String fk_mapdata, long workID, java.util.Hashtable htData) throws Exception {
		Node_SaveFlowSheet(fk_mapdata, workID, htData, null);
	}

	/**
	 * 保存独立表单
	 * 
	 * @param fk_mapdata
	 *            独立表单ID
	 * @param workID
	 *            工作ID
	 * @param htData
	 *            独立表单数据Key Value 格式存放.
	 * @param workDtls
	 *            从表数据
	 * @return 返回执行信息
	 * @throws Exception
	 */
	public static void Node_SaveFlowSheet(String fk_mapdata, long workID, java.util.Hashtable htData, DataSet workDtls)
			throws Exception {
		MapData md = new MapData(fk_mapdata);
		GEEntity en = md.getHisGEEn();
		en.SetValByKey("OID", workID);
		int i = en.RetrieveFromDBSources();

		for (Object key : htData.keySet()) {
			en.SetValByKey(key.toString(), htData.get(key).toString());
		}

		en.SetValByKey("OID", workID);

		FrmEvents fes = md.getFrmEvents();
		fes.DoEventNode(FrmEventList.SaveBefore, en);
		if (i == 0) {
			en.Insert();
		} else {
			en.Update();
		}

		if (workDtls != null) {
			MapDtls dtls = new MapDtls(fk_mapdata);
			// 保存从表
			for (DataTable dt : workDtls.Tables) {
				for (MapDtl dtl : dtls.ToJavaList()) {
					if (dt.TableName != dtl.getNo()) {
						continue;
					}
					// 获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, workID); // 清除现有的数据.

					GEDtl daDtl = (GEDtl) ((daDtls.getGetNewEntity() instanceof GEDtl) ? daDtls.getGetNewEntity()
							: null);
					daDtl.setRefPK(String.valueOf(workID));

					// 为从表复制数据.
					for (DataRow dr : dt.Rows) {
						daDtl.ResetDefaultVal();
						daDtl.setRefPK(String.valueOf(workID));

						// 明细列.
						for (DataColumn dc : dt.Columns) {
							// 设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); // 插入数据.
					}
				}
			}
		}
		fes.DoEventNode(FrmEventList.SaveAfter, en);
	}

	/**
	 * 从任务池里取出来一个子任务
	 * 
	 * @param nodeid
	 *            节点编号
	 * @param workid
	 *            工作ID
	 * @param empNo
	 *            取出来的人员编号
	 * @throws Exception
	 */
	public static boolean Node_TaskPoolTakebackOne(long workid) throws Exception {
		if (Glo.getIsEnableTaskPool() == false) {
			throw new RuntimeException("@配置没有设置成共享任务池的状态。");
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getTaskSta() == TaskSta.None) {
			throw new RuntimeException("@该任务非共享任务。");
		}

		if (gwf.getTaskSta() == TaskSta.Takeback) {
			throw new RuntimeException("@该任务已经被其他人取走。");
		}

		// 更新状态。
		gwf.setTaskSta(TaskSta.Takeback);
		gwf.Update();

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		// 设置已经被取走的状态。
		ps.SQL = "UPDATE WF_GenerWorkerlist SET IsEnable=-1 WHERE IsEnable=1 AND WorkID=" + dbstr
				+ "WorkID AND FK_Node=" + dbstr + "FK_Node AND FK_Emp!=" + dbstr + "FK_Emp ";
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		ps.Add(GenerWorkerListAttr.FK_Node, gwf.getFK_Node());
		ps.Add(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo());
		int i = DBAccess.RunSQL(ps);

		BP.WF.Dev2Interface.WriteTrackInfo(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), workid, 0,
				"任务被" + WebUser.getName() + "从任务池取走.", "获取");
		if (i > 0) {
			Paras ps1 = new Paras();
			// 取走后 将WF_GenerWorkFlow 中的 TodoEmps,TodoEmpsNum 修改下 杨玉慧
			ps1.SQL = "UPDATE WF_GenerWorkFlow SET TodoEmps=" + dbstr + "TodoEmps,TodoEmpsNum=1 WHERE  WorkID=" + dbstr
					+ "WorkID";
			String toDoEmps = BP.Web.WebUser.getNo() + "," + BP.Web.WebUser.getName();
			ps1.Add(GenerWorkFlowAttr.TodoEmps, toDoEmps);
			ps1.Add(GenerWorkerListAttr.WorkID, workid);
			BP.DA.Log.DefaultLogWriteLineInfo(toDoEmps);
			BP.DA.Log.DefaultLogWriteLineInfo(ps1.SQL);
			DBAccess.RunSQL(ps1);
		}

		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 放入一个任务
	 * 
	 * @param nodeid
	 *            节点编号
	 * @param workid
	 *            工作ID
	 * @param empNo
	 *            人员ID
	 * @throws Exception
	 */
	public static void Node_TaskPoolPutOne(long workid) throws Exception {
		if (Glo.getIsEnableTaskPool() == false) {
			throw new RuntimeException("@配置没有设置成共享任务池的状态。");
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		if (gwf.getTaskSta() == TaskSta.None) {
			throw new RuntimeException("@该任务非共享任务。");
		}

		if (gwf.getTaskSta() == TaskSta.Sharing) {
			throw new RuntimeException("@该任务已经是共享状态。");
		}

		// 更新 状态。
		gwf.setTaskSta(TaskSta.Sharing);
		gwf.Update();

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		// 设置已经被取走的状态。
		ps.SQL = "UPDATE WF_GenerWorkerlist SET IsEnable=1 WHERE IsEnable=-1 AND WorkID=" + dbstr + "WorkID ";
		ps.Add(GenerWorkerListAttr.WorkID, workid);
		int i = DBAccess.RunSQL(ps);
		if (i < 0) // 有可能是只有一个人
		{
			throw new RuntimeException("@流程数据错误,不应当更新不到数据。");
		}

		if (i > 0) {
			Paras ps1 = new Paras();
			// 设置已经被取走的状态。
			ps1.SQL = "SELECT FK_Emp,FK_EmpText FROM WF_GenerWorkerlist  WHERE IsEnable=1 AND WorkID=" + dbstr
					+ "WorkID AND FK_Node=" + dbstr + "FK_Node ";
			ps1.Add(GenerWorkerListAttr.WorkID, workid);
			ps1.Add(GenerWorkerListAttr.FK_Node, gwf.getFK_Node());
			ps1.Add(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo());
			DataTable toDoEmpsTable = DBAccess.RunSQLReturnTable(ps1);
			String toDoEmps = "";
			String toDoEmpsNum = "";
			if (toDoEmpsTable == null || toDoEmpsTable.Rows.size() == 0) {
				throw new RuntimeException("@流程数据错误,没有找到需更新的待处理人。");
			}

			toDoEmpsNum = String.valueOf(toDoEmpsTable.Rows.size());
			for (DataRow dr : toDoEmpsTable.Rows) {
				toDoEmps += String.format("%1$s,%2$s", dr.getValue("FK_Emp").toString(),
						dr.getValue("FK_EmpText").toString()) + ";";
			}
			Paras ps2 = new Paras();
			// 将任务放回后 将WF_GenerWorkFlow 中的 TodoEmps,TodoEmpsNum 修改下 杨玉慧
			ps2.SQL = "UPDATE WF_GenerWorkFlow SET TodoEmps=" + dbstr + "TodoEmps,TodoEmpsNum=" + dbstr
					+ "TodoEmpsNum WHERE  WorkID=" + dbstr + "WorkID";
			ps2.Add(GenerWorkFlowAttr.TodoEmps, toDoEmps);
			ps2.Add(GenerWorkFlowAttr.TodoEmpsNum, toDoEmpsNum);
			ps2.Add(GenerWorkerListAttr.WorkID, workid);
			BP.DA.Log.DefaultLogWriteLineInfo(toDoEmps);
			BP.DA.Log.DefaultLogWriteLineInfo(ps2.SQL);
			DBAccess.RunSQL(ps2);
		}

		BP.WF.Dev2Interface.WriteTrackInfo(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), workid, 0,
				"任务被" + WebUser.getName() + "放入了任务池.", "放入");
	}

	/**
	 * 增加下一步骤的接受人(用于当前步骤向下一步骤发送时增加接受人)
	 * 
	 * @param workID
	 *            工作ID
	 * @param formNodeID
	 *            节点ID
	 * @param emps
	 *            如果多个就用逗号分开
	 * @param Del_Selected
	 *            是否删除历史选择
	 * @throws Exception
	 */
	public static void Node_AddNextStepAccepters(long workID, int toNodeID, String emps, boolean del_Selected)
			throws Exception {
		if (emps.contains(";") == true) {
			// 类似与这样的格式. "00000054,严冬梅;00000649,张磊;
			String[] mystrs = emps.split("[;]", -1);
			String result = "";
			for (String str : mystrs) {
				if (str.contains(",") == true) {
					result += str.substring(0, str.indexOf(',') + 1);
				} else {
					result += str;
				}
			}
			emps = result;
		}

		SelectAccper sa = new SelectAccper();
		// 删除历史选择
		if (del_Selected == true) {
			sa.Delete(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, workID);
		}

		emps = emps.replace(" ", "");
		emps = emps.replace(";", ",");
		emps = emps.replace("@", ",");
		String[] strs = emps.split("[,]", -1);

		boolean isPinYin = DBAccess.IsExitsTableCol("Port_Emp", "PinYin");
		String sql = "";
		for (String emp : strs) {
			if (DotNetToJavaStringHelper.isNullOrEmpty(emp)) {
				continue;
			}

			if (isPinYin == true) {
				sql = "SELECT No,Name FROM Port_Emp WHERE No='" + emp + "' OR NAME ='" + emp + "'  OR PinYin LIKE '%,"
						+ emp.toLowerCase() + ",%'";
			} else {
				sql = "SELECT No,Name FROM Port_Emp WHERE No='" + emp + "' OR NAME ='" + emp + "'";
			}

			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() > 12 || dt.Rows.size() == 0) {
				continue;
			}
			int i = 0;
			for (DataRow dr : dt.Rows) {
				String empNo = dr.getValue(0).toString();
				String empName = dr.getValue(1).toString();
				sa.setDeptName("");
				sa.setIdx(i);
				sa.ResetPK();
				sa.setFK_Emp(empNo);
				sa.setEmpName(empName);
				sa.setIdx(i);
				sa.setFK_Node(toNodeID);
				sa.setWorkID(workID);
				sa.ResetPK();
				if (sa.getIsExits() == false) {
					sa.Insert();
				}
				i++;
			}
		}
	}

	/**
	 * 增加下一步骤的接受人(用于当前步骤向下一步骤发送时增加接受人)
	 * 
	 * @param workID
	 *            工作ID
	 * @param formNodeID
	 *            从节点ID
	 * @param emp
	 *            接收人
	 * @param tag
	 *            分组维度，可以为空.是为了分流节点向下发送时候，可能有一个工作人员两个或者两个以上的子线程的情况出现。 tag
	 *            是个维度，这个维度可能是一个类别，一个批次，一个标记，总之它是一个字符串。详细:
	 *            http://bbs.ccflow.org/showtopic-3065.jsp
	 * @throws Exception
	 */
	public static void Node_AddNextStepAccepter(long workID, int formNodeID, String emp, String tag) throws Exception {
		if (DataType.IsNullOrEmpty(emp) == true)
			return;
		SelectAccper sa = new SelectAccper();
		sa.Delete(SelectAccperAttr.FK_Node, formNodeID, SelectAccperAttr.WorkID, workID, SelectAccperAttr.FK_Emp, emp,
				SelectAccperAttr.Tag, tag);

		Emp empEn = new Emp();
		empEn.setNo(emp);
		if (empEn.RetrieveFromDBSources() == 0)
			return;
		sa.setMyPK(formNodeID + "_" + workID + "_" + emp + "_" + tag);
		empEn.setNo(emp);
		sa.setTag(tag);
		sa.setFK_Emp(emp);
		sa.setEmpName(empEn.getName());
		sa.setFK_Node(formNodeID);
		sa.setWorkID(workID);
		sa.Insert();
	}

	/**
	 * 节点工作挂起
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param way
	 *            挂起方式
	 * @param reldata
	 *            解除挂起日期(可以为空)
	 * @param hungNote
	 *            挂起原因
	 * @return 返回执行信息
	 * @throws Exception
	 */
	public static String Node_HungUpWork(String fk_flow, long workid, int wayInt, String reldata, String hungNote)
			throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		HungUpWay way = HungUpWay.forValue(wayInt);
		BP.WF.WorkFlow wf = new WorkFlow(fk_flow, workid);
		return wf.DoHungUp(way, reldata, hungNote);
	}

	/**
	 * 节点工作取消挂起
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param msg
	 *            取消挂起原因
	 * @return 执行信息
	 * @throws Exception
	 */
	public static void Node_UnHungUpWork(String fk_flow, long workid, String msg) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		BP.WF.WorkFlow wf = new WorkFlow(fk_flow, workid);
		wf.DoUnHungUp();
	}

	/**
	 * 获取该节点上的挂起时间
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @return 返回时间串，如果没有挂起的动作就抛出异常.
	 */
	public static Date Node_GetHungUpTimeSpan(String flowNo, int nodeID, long workid) {
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		String instr = ActionType.HungUp.getValue() + "," + ActionType.UnHungUp.getValue();
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + dbstr + "WorkID AND "
				+ TrackAttr.ActionType + " in (" + instr + ")  and  NDFrom=" + dbstr + "NDFrom ";
		ps.Add(TrackAttr.WorkID, workid);
		ps.Add(TrackAttr.NDFrom, nodeID);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		java.util.Date dtStart = new java.util.Date();
		java.util.Date dtEnd = new java.util.Date();
		for (DataRow item : dt.Rows) {
			ActionType at = (ActionType) item.getValue(TrackAttr.ActionType);
			// 挂起时间.
			if (at == ActionType.HungUp) {
				dtStart = DataType.ParseSysDateTime2DateTime(item.getValue(TrackAttr.RDT).toString());
			}

			// 解除挂起时间.
			if (at == ActionType.UnHungUp) {
				dtEnd = DataType.ParseSysDateTime2DateTime(item.getValue(TrackAttr.RDT).toString());
			}
		}

		Date ts = new Date(dtEnd.getTime() - dtStart.getTime());
		return ts;
	}

	/**
	 * 执行加签
	 * 
	 * @param workid
	 *            工作ID
	 * @param askfor
	 *            加签方式
	 * @param askForEmp
	 *            请求人员
	 * @param askForNote
	 *            内容
	 * @return
	 * @throws Exception
	 */
	public static String Node_Askfor(long workid, AskforHelpSta askforSta, String askForEmp, String askForNote)
			throws Exception {
		// 检查人员是否存在.
		Emp emp = new Emp();
		emp.setNo(askForEmp);
		if (emp.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("@要加签的人员编号错误:" + askForEmp);
		}

		// 获得当前流程注册信息.
		BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(workid);

		// 检查当前人员是否开可以执行当前的工作?
		if (Flow_IsCanDoCurrentWork(gwf.getWorkID(), WebUser.getNo()) == false) {
			throw new RuntimeException("@当前的工作已经被别人处理或者您没有处理该工作的权限.");
		}

		// 检查被加签的人是否在当前的队列中.
		GenerWorkerLists gwls = new GenerWorkerLists(workid, gwf.getFK_Node());
		if (gwls.Contains(GenerWorkerListAttr.FK_Emp, askForEmp, GenerWorkerListAttr.IsEnable, 0) == true) {
			throw new RuntimeException("@加签失败，您选择的加签人可以处理当前的工作。");
		}

		gwf.setWFState(WFState.Askfor); // 更新流程为加签状态.
		gwf.Update();

		// 设置当前状态为 2 表示加签状态.
		if (gwls.size() == 0) {
			// 可能是第一个节点.
			GenerWorkerList gwl = new GenerWorkerList();
			gwl.Copy(gwf);
			gwl.setWorkID(workid);
			gwl.setFK_Emp(askForEmp);
			gwl.setFK_Node(gwf.getFK_Node());
			gwl.setFK_NodeText(gwl.getFK_NodeText());
			gwl.setFK_Emp(BP.Web.WebUser.getNo());
			gwl.setFK_EmpText(BP.Web.WebUser.getName());
			gwl.setFK_Dept(BP.Web.WebUser.getFK_Dept());
			gwl.setIsPassInt(askforSta.getValue());
			gwl.Insert();
			// 重新查询.
			gwls = new GenerWorkerLists(workid, gwf.getFK_Node());

			// 设置流程标题.
			if (gwf.getTitle().length() == 0) {
				Flow_SetFlowTitle(gwf.getFK_Flow(), workid, "来自" + WebUser.getName() + "的工作加签.");
			}
		}
		// endWarning.

		// 处理状态.
		for (GenerWorkerList item : gwls.ToJavaList()) {
			if (item.getIsEnable() == false) {
				continue;
			}

			if (item.getFK_Emp().equals(WebUser.getNo())) {
				// GenerWorkerList gwl = gwls[0] as GenerWorkerList;
				item.setIsPassInt(askforSta.getValue());
				item.Update();

				// 更换主键后，执行insert ,让被加签人有代办工作.
				item.setIsPassInt(0);
				item.setFK_Emp(emp.getNo());
				item.setFK_EmpText(emp.getName());
				try {
					item.Insert();
				} catch (java.lang.Exception e) {
					item.Update();
				}
			} else {
				item.Update();
			}
		}

		// 写入日志.
		BP.WF.Dev2Interface.WriteTrack(gwf.getFK_Flow(), gwf.getFK_Node(), workid, gwf.getFID(), askForNote,
				ActionType.AskforHelp, "", null, null, emp.getNo(), emp.getName());

		Flow fl = new Flow(gwf.getFK_Flow());
		// 暂时住掉
		// BP.WF.Dev2Interface.Port_SendMsg(askForEmp, gwf.getTitle(),
		// askForNote,
		// "AK" + gwf.getFK_Node() + "_" + gwf.getWorkID(), SMSMsgType.AskFor,
		// gwf.getFK_Flow(), gwf.getFK_Node(),
		// workid, gwf.getFID());
		// 更新状态.
		DBAccess.RunSQL(
				"UPDATE " + fl.getPTable() + " SET WFState=" + WFState.Askfor.getValue() + " WHERE OID=" + workid);

		// 设置成工作未读。
		BP.WF.Dev2Interface.Node_SetWorkUnRead(workid, askForEmp);

		String msg = "您的工作已经提交给(" + askForEmp + " " + emp.getName() + ")加签了。";

		// 加签后事件
		BP.WF.Node hisNode = new BP.WF.Node(gwf.getFK_Node());
		Work currWK = hisNode.getHisWork();
		currWK.setOID(gwf.getWorkID());
		currWK.Retrieve();

		// 执行加签后的事件.
		msg += fl.DoFlowEventEntity(EventListOfNode.AskerAfter, hisNode, currWK, null);

		return msg;
	}

	/**
	 * 答复加签信息
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            FID
	 * @param replyNote
	 *            答复信息
	 * @return
	 * @throws Exception
	 */
	public static String Node_AskforReply(String fk_flow, int fk_node, long workid, long fid, String replyNote)
			throws Exception {
		// 把回复信息临时的写入 流程注册信息表以便让发送方法获取这个信息写入日志.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setParas_AskForReply(replyNote);
		gwf.Update();

		Node nd = new Node(gwf.getFK_Node());
		String info = "";
		try {
			// 执行发送, 在发送的方法里面已经做了判断了,并且把 回复的信息写入了日志.
			info = BP.WF.Dev2Interface.Node_SendWork(gwf.getFK_Flow(), workid, null, null, 0, null).ToMsgOfHtml();
		} catch (Exception ex) {
			if (ex.getMessage().contains("请选择下一步骤工作") == true || ex.getMessage().contains("用户没有选择发送到的节点") == true) {
				if (nd.getCondModel() == CondModel.ByUserSelected) {
					/* 如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
					return "SelectNodeUrl@./WorkOpt/ToNodes.htm?FK_Flow=" + gwf.getFK_Flow() + "&FK_Node="
							+ gwf.getFK_Node() + "&WorkID=" + gwf.getWorkID() + "&FID=" + gwf.getFID();

				}
				return "err@下一个节点的接收人规则是，当前节点选择来选择，在当前节点属性里您没有启动接受人按钮，系统自动帮助您启动了，请关闭窗口重新打开。" + ex.getMessage();
			}
			return ex.getMessage();
		}
		Node node = new Node(gwf.getFK_Node());
		Work wk = node.getHisWork();
		wk.setOID(workid);
		wk.RetrieveFromDBSources();
		// 恢复加签后执行事件
		info += node.getHisFlow().DoFlowEventEntity(EventListOfNode.AskerReAfter, node, node.getHisWork(), null);
		return info;
	}

	public static String Node_AskforReply(long workid, String replyNote) throws Exception {
		// 把回复信息临时的写入 流程注册信息表以便让发送方法获取这个信息写入日志.
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setParas_AskForReply(replyNote);
		gwf.Update();
		String info = "";
		Node nd = new Node(gwf.getFK_Node());
		try {
			// 执行发送, 在发送的方法里面已经做了判断了,并且把 回复的信息写入了日志.
			info = BP.WF.Dev2Interface.Node_SendWork(gwf.getFK_Flow(), workid).ToMsgOfHtml();
			Node node = new Node(gwf.getFK_Node());
			Work wk = node.getHisWork();
			wk.setOID(workid);
			wk.RetrieveFromDBSources();

			// 恢复加签后执行事件
			info += node.getHisFlow().DoFlowEventEntity(EventListOfNode.AskerReAfter, node, wk, null);
			return info;
		} catch (Exception ex) {
			if (ex.getMessage().contains("请选择下一步骤工作") == true || ex.getMessage().contains("用户没有选择发送到的节点") == true) {
				if (nd.getCondModel() == CondModel.ByUserSelected) {
					/* 如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
					return "SelectNodeUrl@./WorkOpt/ToNodes.htm?FK_Flow=" + gwf.getFK_Flow() + "&FK_Node="
							+ gwf.getFK_Node() + "&WorkID=" + gwf.getWorkID() + "&FID=" + gwf.getFID();

				}
				return "err@下一个节点的接收人规则是，当前节点选择来选择，在当前节点属性里您没有启动接受人按钮，系统自动帮助您启动了，请关闭窗口重新打开。" + ex.getMessage();
			}
			return ex.getMessage();
		}

	}

	/**
	 * 工作移交
	 * 
	 * @param workid
	 *            工作ID
	 * @param toEmp
	 *            移交到人员(只给移交给一个人)
	 * @param msg
	 *            移交消息
	 * @throws Exception
	 */
	public static String Node_Shift(String flowNo, int nodeID, long workID, long fid, String toEmp, String msg)
			throws Exception {
		// 人员.
		Emp emp = new Emp(toEmp);
		Node nd = new Node(nodeID);

		if (nd.getTodolistModel() == TodolistModel.Order || nd.getTodolistModel() == TodolistModel.Teamup
				|| nd.getTodolistModel() == TodolistModel.TeamupGroupLeader) {
			// 如果是队列模式，或者是协作模式.
			try {
				String sql = "UPDATE WF_GenerWorkerlist SET FK_Emp='" + emp.getNo() + "', FK_EmpText='" + emp.getName()
						+ "' WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Node=" + nodeID + " AND WorkID=" + workID;
				BP.DA.DBAccess.RunSQL(sql);
			} catch (java.lang.Exception e) {
				return "@移交失败，您所移交的人员(" + emp.getNo() + " " + emp.getName() + ")已经在代办列表里.";
			}

			// 记录日志.
			Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, fid, nd.getNodeID(), nd.getName(),
					WebUser.getNo(), WebUser.getName(), nd.getNodeID(), nd.getName(), toEmp, emp.getName(), msg, null);

			String info = "@工作移交成功。@您已经成功的把工作移交给：" + emp.getNo() + " , " + emp.getName();

			// 移交后事件
			info += "@" + nd.getHisFlow().DoFlowEventEntity(EventListOfNode.ShitAfter, nd, nd.getHisWork(), null);

			// info += "@<a href='" + Glo.getCCFlowAppPath() +
			// "WF/MyFlowInfo.jsp?DoType=UnShift&FK_Flow=" + nd.getFK_Flow() +
			// "&WorkID=" + workID + "&FK_Node=" + nodeID + "&FID=" + fid + "'
			// ><img src='Img/Action/UnSend.png' border=0 />撤消工作移交</a>.";
			return info;

		}

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() == 0) {
			// 说明开始节点数据表单移交.
			gwf.setWorkID(workID);
			gwf.setTitle("由" + WebUser.getNo() + " ; " + WebUser.getName() + ", 在(" + DataType.getCurrentDataCNOfShort()
					+ ")移交来的工作");
			gwf.setFK_Dept(WebUser.getFK_Dept());
			gwf.setFK_Flow(flowNo);

			Flow fl = new Flow(flowNo);
			gwf.setFK_FlowSort(fl.getFK_FlowSort());
			gwf.setSysType(fl.getSysType());
			gwf.setFK_Node(nodeID);
			gwf.setStarter(emp.getNo());
			gwf.setStarterName(emp.getName());
			gwf.setWFState(WFState.Shift);
			gwf.setTodoEmps(toEmp);
			gwf.setTodoEmpsNum(1);
			gwf.setRDT(DataType.getCurrentDataTimess());
			gwf.setNodeName("");
			gwf.setFlowName(fl.getName());
			gwf.setEmps(toEmp);
			gwf.setDeptName(WebUser.getFK_DeptName());
			gwf.Insert();

			GenerWorkerList gwl = new GenerWorkerList();
			gwl.setWorkID(workID);
			gwl.setFK_Dept(WebUser.getFK_Dept());

			// gwl.FK_DeptT = WebUser.getFK_DeptName();
			gwl.setFK_Node(nodeID);
			gwl.setFK_NodeText(nd.getName());

			gwl.setFK_Emp(toEmp);
			gwl.setFK_EmpText(emp.getName());

			gwl.setFK_Flow(flowNo);

			gwl.setIsPass(false);
			gwl.setIsPassInt(0);
			gwl.setIsRead(false);
			gwl.setPressTimes(0);
			gwl.setRDT(gwf.getRDT());
			gwl.setSDT(gwf.getRDT());
			// gwl.Sender = WebUser.getNo();
			gwl.Insert();
		} else {
			if (gwf.getWFSta() == WFSta.Complete) {
				throw new RuntimeException("@流程已经完成，您不能执行移交了。");
			}

			// 删除当前非配的工作。
			// 已经非配或者自动分配的任务。
			// 设置所有的工作人员为不可处理.
			String dbStr = SystemConfig.getAppCenterDBVarStr();
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerlist SET IsEnable=0  WHERE WorkID=" + dbStr + "WorkID AND FK_Node=" + dbStr
					+ "FK_Node";
			ps.Add(GenerWorkerListAttr.WorkID, workID);
			ps.Add(GenerWorkerListAttr.FK_Node, nodeID);
			DBAccess.RunSQL(ps);

			// 设置被移交人的FK_Emp 为当前处理人，（有可能被移交人不在工作列表里，就返回0.）
			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerlist SET IsEnable=1  WHERE WorkID=" + dbStr + "WorkID AND FK_Node=" + dbStr
					+ "FK_Node AND FK_Emp=" + dbStr + "FK_Emp";
			ps.Add(GenerWorkerListAttr.WorkID, workID);
			ps.Add(GenerWorkerListAttr.FK_Node, nodeID);
			ps.Add(GenerWorkerListAttr.FK_Emp, toEmp);
			int i = DBAccess.RunSQL(ps);

			GenerWorkerLists wls = null;
			GenerWorkerList wl = null;
			if (i == 0) {
				// 说明: 用其它的岗位上的人来处理的，就给他增加共享工作。
				wls = new GenerWorkerLists(workID, nodeID);
				if (wls.size() == 0) {
					if (nd.getIsStartNode() == false) {
						throw new RuntimeException(
								"@流程引擎 GenerWorkerLists 数据丢失, workID=" + workID + ",nodeID=" + nodeID);
					}

					wl = new GenerWorkerList();
					wl.setFK_Dept(BP.Web.WebUser.getFK_Dept());
					// mygwfl.FK_DeptT = BP.Web.WebUser.getFK_DeptName();
					wl.setWorkID(workID);
					wl.setFID(0);
					wl.setFK_Emp(BP.Web.WebUser.getNo());
					wl.setFK_EmpText(BP.Web.WebUser.getName());
					wl.setFK_Node(nodeID);
					wl.setFK_NodeText(nd.getName());
					wl.setSender(BP.Web.WebUser.getNo());
					wl.setRDT(BP.DA.DataType.getCurrentDataTimess());
					wl.setIsPass(false);
					wl.setIsRead(false);
					wl.setIsEnable(true);
					wl.Insert();
					wls.AddEntity(wl);
				} else {
					wl = (GenerWorkerList) ((wls.get(0) instanceof GenerWorkerList) ? wls.get(0) : null);
				}

				wl.setFK_Emp(toEmp.toString());
				wl.setFK_EmpText(emp.getName());
				wl.setIsEnable(true);
				wl.setIsRead(false);
				wl.Insert();

				// 清除工作者，为转发消息所用.
				wls.clear();
				wls.AddEntity(wl);
			}

			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkFlow SET WFState=" + WFState.Shift.getValue() + ", WFSta="
					+ WFSta.Runing.getValue() + "   WHERE WorkID=" + dbStr + "WorkID ";
			ps.Add(GenerWorkerListAttr.WorkID, workID);
			DBAccess.RunSQL(ps);
		}

		ShiftWork sf = new ShiftWork();
		sf.setWorkID(workID);
		sf.setFK_Node(nodeID);
		sf.setToEmp(toEmp);
		sf.setToEmpName(emp.getName());
		sf.setNote(msg);
		sf.setFK_Emp(WebUser.getNo());
		sf.setFK_EmpName(WebUser.getName());
		sf.Insert();
		// 记录日志.
		Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(),
				WebUser.getNo(), WebUser.getName(), nd.getNodeID(), nd.getName(), toEmp, emp.getName(), msg, null);

		// 发送邮件.
		BP.WF.Dev2Interface.Port_SendMsg(emp.getNo(), WebUser.getName() + "向您移交了工作" + gwf.getTitle(), "移交信息:" + msg,
				"SF" + workID + "_" + sf.getFK_Node(), BP.WF.SMSMsgType.Shift, gwf.getFK_Flow(), gwf.getFK_Node(),
				gwf.getWorkID(), gwf.getFID());

		String inf1o = "@工作移交成功。@您已经成功的把工作移交给：" + emp.getNo() + " , " + emp.getName();

		// 移交后事件
		inf1o += "@" + nd.getHisFlow().DoFlowEventEntity(EventListOfNode.ShitAfter, nd, nd.getHisWork(), null);

		// inf1o += "@<a href='" + Glo.getCCFlowAppPath() +
		// "WF/MyFlowInfo.jsp?DoType=UnShift&FK_Flow=" + nd.getFK_Flow() +
		// "&WorkID=" + workID + "&FK_Node=" + nodeID + "&FID=" + fid + "' ><img
		// src='Img/Action/UnSend.png' border=0 />撤消工作移交</a>.";
		return inf1o;
	}

	/**
	 * 撤销移交
	 * 
	 * @param flowNo
	 *            撤销编号
	 * @param workID
	 *            工作ID
	 * @return 返回撤销信息
	 * @throws Exception
	 */
	public static String Node_ShiftUn(String flowNo, long workID) throws Exception {
		WorkFlow mwf = new WorkFlow(flowNo, workID);
		return mwf.DoUnShift();
	}

	/**
	 * 执行工作退回(退回指定的点)
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param currentNodeID
	 *            当前节点ID
	 * @param returnToNodeID
	 *            退回到的工作ID
	 * @param returnToEmp
	 *            退回到人员
	 * @param msg
	 *            退回原因
	 * @param isBackToThisNode
	 *            退回后是否要原路返回？
	 * @return 执行结果，此结果要提示给用户。
	 * @throws Exception
	 */
	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID,
			String returnToEmp, String msg, boolean isBackToThisNode) throws Exception {

		if (DataType.IsNullOrEmpty(msg) == true)
			throw new com.sun.star.uno.Exception("请输入退回意见.");

		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		WorkReturn wr = new WorkReturn(fk_flow, workID, fid, currentNodeID, returnToNodeID, returnToEmp,
				isBackToThisNode, msg);
		return wr.DoIt();
	}

	// 根据浙江莲荷科技要求，将master版本6参数退回方法重新放入新版本
	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID,
			String returnToEmp) throws Exception {
		return Node_ReturnWork(fk_flow, workID, fid, currentNodeID, returnToNodeID, returnToEmp, "无", false);
	}

	/**
	 * 退回
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param currentNodeID
	 *            当前节点
	 * @param returnToNodeID
	 *            退回到节点
	 * @param msg
	 *            退回消息
	 * @param isBackToThisNode
	 *            是否原路返回
	 * @return 退回执行的信息，执行不成功就抛出异常。
	 * @throws Exception
	 */
	public static String Node_ReturnWork(String fk_flow, long workID, long fid, int currentNodeID, int returnToNodeID,
			String msg, boolean isBackToThisNode) throws Exception {
		return Node_ReturnWork(fk_flow, workID, fid, currentNodeID, returnToNodeID, null, msg, isBackToThisNode);
	}

	/**
	 * 获取当前工作的NodeID
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @return 指定工作的NodeID.
	 */
	public static int Node_GetCurrentNodeID(String fk_flow, long workid) {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		int nodeID = BP.DA.DBAccess.RunSQLReturnValInt(
				"SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + workid + " AND FK_Flow='" + fk_flow + "'", 0);
		if (nodeID == 0) {
			return Integer.parseInt(fk_flow + "01");
		}
		return nodeID;
	}

	/**
	 * 删除子线程
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fid
	 *            流程ID
	 * @param workid
	 *            工作ID
	 * @throws Exception
	 */
	public static void Node_FHL_KillSubFlow(String fk_flow, long fid, long workid) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		WorkFlow wkf = new WorkFlow(fk_flow, workid);
		wkf.DoDeleteWorkFlowByReal(true);
	}

	/**
	 * 合流点驳回子线程
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fid
	 *            流程ID
	 * @param workid
	 *            子线程ID
	 * @param msg
	 *            驳回消息
	 * @throws Exception
	 */
	public static String Node_FHL_DoReject(String fk_flow, int NodeSheetfReject, long fid, long workid, String msg)
			throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		WorkFlow wkf = new WorkFlow(fk_flow, workid);
		return wkf.DoReject(fid, NodeSheetfReject, msg);
	}

	/**
	 * 跳转审核取回
	 * 
	 * @param fromNodeID
	 *            从节点ID
	 * @param workid
	 *            工作ID
	 * @param tackToNodeID
	 *            取回到的节点ID
	 * @return
	 * @throws Exception
	 */
	public static String Node_Tackback(int fromNodeID, long workid, int tackToNodeID) throws Exception {
		//
		// * 1,首先检查是否有此权限.
		// * 2, 执行工作跳转.
		// * 3, 执行写入日志.
		//
		Node nd = new Node(tackToNodeID);
		switch (nd.getHisDeliveryWay()) {
		case ByPreviousNodeFormEmpsField:
			break;
		}

		WorkNode wn = new WorkNode(workid, fromNodeID);
		String msg = wn.NodeSend(new Node(tackToNodeID), BP.Web.WebUser.getNo()).ToMsgOfHtml();
		wn.AddToTrack(ActionType.Tackback, WebUser.getNo(), WebUser.getName(), tackToNodeID, nd.getName(),
				"执行跳转审核的取回.");
		return msg;
	}

	/**
	 * 执行抄送已阅
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            流程节点
	 * @param workid
	 *            工作id
	 * @param fid
	 *            流程id
	 * @param checkNote
	 *            填写意见
	 * @throws Exception
	 */
	public static void Node_DoCCCheckNote(String fk_flow, int fk_node, long workid, long fid, String checkNote)
			throws Exception {
		FrmWorkCheck fwc = new FrmWorkCheck(fk_node);

		BP.WF.Dev2Interface.WriteTrackWorkCheck(fk_flow, fk_node, workid, fid, checkNote, fwc.getFWCOpLabel());

		// 设置审核完成.
		BP.WF.Dev2Interface.Node_CC_SetSta(fk_node, workid, BP.Web.WebUser.getNo(), BP.WF.Template.CCSta.CheckOver);

	}

	/**
	 * 设置是此工作为读取状态
	 * 
	 * @param nodeID
	 *            节点编号
	 * @param workid
	 *            工作ID
	 * @throws Exception
	 */
	public static void Node_SetWorkRead(int nodeID, long workid) throws Exception {
		Node_SetWorkRead(nodeID, workid, BP.Web.WebUser.getNo());
	}

	/**
	 * 设置是此工作为读取状态
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            WorkID
	 * @param empNo
	 *            操作员
	 * @throws Exception
	 */
	public static void Node_SetWorkRead(int nodeID, long workid, String empNo) throws Exception {
		Node nd = new Node(nodeID);

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsRead=1 WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr
				+ "FK_Node AND FK_Emp=" + dbstr + "FK_Emp";
		ps.Add("WorkID", workid);
		ps.Add("FK_Node", nodeID);
		ps.Add("FK_Emp", empNo);
		if (DBAccess.RunSQL(ps) == 0) {
			// throw new RuntimeException("@设置的工作不存在，或者当前的登陆人员已经改变。");
		}

		// 判断当前节点的已读回执.
		if (nd.getReadReceipts() == ReadReceipts.None) {
			return;
		}

		boolean isSend = false;
		if (nd.getReadReceipts() == ReadReceipts.Auto) {
			isSend = true;
		}

		if (nd.getReadReceipts() == ReadReceipts.BySysField) {
			// 获取上一个节点ID
			Nodes fromNodes = nd.getFromNodes();
			int fromNodeID = 0;
			for (Node item : fromNodes.ToJavaList()) {
				ps = new Paras();
				ps.SQL = "SELECT FK_Node FROM WF_GenerWorkerlist  WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr
						+ "FK_Node ";
				ps.Add("WorkID", workid);
				ps.Add("FK_Node", item.getNodeID());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0) {
					continue;
				}
				fromNodeID = item.getNodeID();
				break;
			}
			if (fromNodeID == 0) {
				throw new RuntimeException("@没有找到它的上一步工作。");
			}

			try {
				ps = new Paras();
				ps.SQL = "SELECT " + BP.WF.WorkSysFieldAttr.SysIsReadReceipts + " FROM ND" + fromNodeID
						+ "    WHERE OID=" + dbstr + "OID";
				ps.Add("OID", workid);
				DataTable dt1 = DBAccess.RunSQLReturnTable(ps);
				if (dt1.Rows.get(0).get(0).toString().equals("1")) {
					isSend = true;
				}
			} catch (RuntimeException ex) {
				throw new RuntimeException(
						"@流程设计错误:" + ex.getMessage() + " 在当前节点上个您设置了安上一步的表单字段决定是否回执，但是上一个节点表单中没有约定的字段。");
			}
		}

		if (nd.getReadReceipts() == ReadReceipts.BySDKPara) {
			// 如果是按开发者参数

			// 获取上一个节点ID
			Nodes fromNodes = nd.getFromNodes();
			int fromNodeID = 0;
			for (Node item : fromNodes.ToJavaList()) {
				ps = new Paras();
				ps.SQL = "SELECT FK_Node FROM WF_GenerWorkerlist  WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + dbstr
						+ "FK_Node ";
				ps.Add("WorkID", workid);
				ps.Add("FK_Node", item.getNodeID());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0) {
					continue;
				}

				fromNodeID = item.getNodeID();
				break;
			}
			if (fromNodeID == 0) {
				throw new RuntimeException("@没有找到它的上一步工作。");
			}

			String paras = "";
			try {
				paras = BP.WF.Dev2Interface.GetFlowParas(fromNodeID, workid);
			} catch (Exception e) {
				Log.DebugWriteError("Dev2Interface Node_SetWorkRead " + e.getMessage());
				e.printStackTrace();
			}
			if (StringHelper.isNullOrEmpty(paras)
					|| paras.contains("@" + BP.WF.WorkSysFieldAttr.SysIsReadReceipts + "=") == false) {
				throw new RuntimeException("@流程设计错误:在当前节点上个您设置了按开发者参数决定是否回执，但是没有找到该参数。");
			}

			// 开发者参数.
			if (paras.contains("@" + BP.WF.WorkSysFieldAttr.SysIsReadReceipts + "=1") == true) {
				isSend = true;
			}
		}

		if (isSend == true) {
			// 如果是自动的已读回执，就让它发送给当前节点的上一个发送人。

			// 获取流程标题.
			ps = new Paras();
			ps.SQL = "SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + dbstr + "WorkID ";
			ps.Add("WorkID", workid);
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			String title = dt.Rows.get(0).getValue(0).toString();

			// 获取流程的发送人.
			ps = new Paras();
			ps.SQL = "SELECT " + GenerWorkerListAttr.Sender + " FROM WF_GenerWorkerlist WHERE WorkID=" + dbstr
					+ "WorkID AND FK_Node=" + dbstr + "FK_Node ";
			ps.Add("WorkID", workid);
			ps.Add("FK_Node", nodeID);
			dt = DBAccess.RunSQLReturnTable(ps);
			String sender = dt.Rows.get(0).getValue(0).toString();

			// 发送已读回执。
			BP.WF.Dev2Interface.Port_SendMsg(sender, "已读回执:" + title,
					"您发送的工作已经被" + WebUser.getName() + "在" + DataType.getCurrentDataTimeCNOfShort() + " 打开.",
					"RP" + workid + "_" + nodeID, BP.WF.SMSMsgType.Self, nd.getFK_Flow(), nd.getNodeID(), workid, 0);
		}

		// 执行节点打开后事件.
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.RetrieveFromDBSources();
		nd.getHisFlow().DoFlowEventEntity(EventListOfNode.WhenReadWork, nd, wk, null);

	}

	/**
	 * 设置工作未读取
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @param userNo
	 *            要设置的人
	 */
	public static void Node_SetWorkUnRead(long workid, String userNo) {
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsRead=0 WHERE WorkID=" + dbstr + "WorkID AND FK_Emp=" + dbstr
				+ "FK_Emp";
		ps.Add("WorkID", workid);
		ps.Add("FK_Emp", userNo);
		DBAccess.RunSQL(ps);
	}

	/**
	 * 设置工作未读取
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @throws Exception
	 */
	public static void Node_SetWorkUnRead(long workid) throws Exception {
		Node_SetWorkUnRead(workid, BP.Web.WebUser.getNo());
	}

	/**
	 * 更改流程属性
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param attr1
	 *            字段1
	 * @param v1
	 *            值1
	 * @param attr2
	 *            字段2(可为null)
	 * @param v2
	 *            值2(可为null)
	 * @return 执行结果
	 * @throws Exception
	 */
	public static String ChangeAttr_Flow(String fk_flow, String attr1, Object v1, String attr2, Object v2)
			throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		Flow fl = new Flow(fk_flow);
		if (attr1 != null) {
			fl.SetValByKey(attr1, v1);
		}
		if (attr2 != null) {
			fl.SetValByKey(attr2, v2);
		}
		fl.Update();
		return "修改成功";
	}

	/// #region UI 接口
	/**
	 * 获取按钮状态
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            流程ID
	 * @return 返回按钮状态
	 * @throws Exception
	 */
	public static ButtonState UI_GetButtonState(String fk_flow, int fk_node, long workid) throws Exception {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		ButtonState bs = new ButtonState(fk_flow, fk_node, workid);
		return bs;
	}

	/**
	 * 打开退回窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 */
	public static String UI_Window_Return(String fk_flow, int fk_node, long workid, long fid) {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		String url = Glo.getCCFlowAppPath() + "WF/WorkOpt/ReturnWork.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node
				+ "&WorkID=" + workid + "&FID=" + fid;
		// System.Web.HttpContext.Current.Response.Redirect(url, true);
		return url;
	}

	/**
	 * 打开抄送窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @throws IOException
	 */
	public static void UI_Window_CC(String fk_flow, int fk_node, long workid, long fid) throws IOException {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		PubClass.WinOpen(ContextHolderUtils.getResponse(), Glo.getCCFlowAppPath() + "WF/WorkOpt/CC.jsp?FK_Flow="
				+ fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workid + "&FID=" + fid, 800, 600);
	}

	/**
	 * 打开加签窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @throws IOException
	 */
	public static void UI_Window_AskForHelp(String fk_flow, int fk_node, long workid, long fid) throws IOException {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		String tKey = DateUtils.format(new java.util.Date(), "MMddhhmmss");
		String urlr3 = Glo.getCCFlowAppPath() + "WF/WorkOpt/Askfor.jsp?FK_Node=" + fk_node + "&FID=" + fid + "&WorkID="
				+ workid + "&FK_Flow=" + fk_flow + "&s=" + tKey;
		PubClass.WinOpen(ContextHolderUtils.getResponse(), urlr3, 800, 600);
	}

	/**
	 * 打开挂起窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @throws IOException
	 */
	public static void UI_Window_HungUp(String fk_flow, int fk_node, long workid, long fid) throws IOException {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		PubClass.WinOpen(ContextHolderUtils.getResponse(), Glo.getCCFlowAppPath() + "WF/WorkOpt/HungUp.jsp?FK_Flow="
				+ fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workid + "&FID=" + fid, 500, 400);
	}

	/**
	 * 打开催办窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @throws IOException
	 */
	public static void UI_Window_Hurry(String fk_flow, int fk_node, long workid, long fid) throws IOException {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		PubClass.WinOpen(ContextHolderUtils.getResponse(), Glo.getCCFlowAppPath() + "WF/Hurry.jsp?FK_Flow=" + fk_flow
				+ "&FK_Node=" + fk_node + "&WorkID=" + workid + "&FID=" + fid, 500, 400);
	}

	/**
	 * 打开跳转窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @throws IOException
	 */
	public static void UI_Window_JumpWay(String fk_flow, int fk_node, long workid, long fid) throws IOException {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		PubClass.WinOpen(ContextHolderUtils.getResponse(), Glo.getCCFlowAppPath() + "WF/JumpWaySmallSingle.jsp?FK_Flow="
				+ fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workid + "&FID=" + fid, 500, 400);
	}

	/**
	 * 打开流程轨迹窗口
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param nodeID
	 *            当前节点编号
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @throws IOException
	 */
	public static void UI_Window_FlowChartTruck(String fk_flow, int nodeID, long workid, long fid) throws IOException {
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);
		PubClass.WinOpen(ContextHolderUtils.getResponse(), Glo.getCCFlowAppPath()
				+ "WF/WorkOpt/OneWork/ChartTrack.jsp?FK_Flow=" + fk_flow + "&WorkID=" + workid + "&FID=" + fid, 500,
				400);
	}

	/// #region ccform 接口
	/**
	 * 获得指定轨迹的json数据.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param mypk
	 *            流程主键
	 * @return 返回当时的表单json字符串
	 */
	public static String CCFrom_GetFrmDBJson(String flowNo, String mypk) {
		return DBAccess.GetBigTextFromDB("ND" + Integer.parseInt(flowNo) + "Track", "MyPK", mypk, "FrmDB");
	}

	/**
	 * SDK签章接口
	 * 
	 * @param workid
	 *            工作ID
	 * @param nodeid
	 *            签章节点ID
	 * @param deptno
	 *            部门编号
	 * @param stationno
	 *            岗位编号
	 * @return 返回非null值时，为签章失败
	 * @throws Exception
	 */
	public static String CCForm_Seal(long workid, int nodeid, String deptno, String stationno) throws Exception {
		try {
			FrmEleDBs eleDBs = new FrmEleDBs("ND" + nodeid, String.valueOf(workid));

			if (eleDBs.size() > 0) {
				eleDBs.Delete(FrmEleDBAttr.FK_MapData, "ND" + nodeid, FrmEleDBAttr.RefPKVal, workid);
			}

			String sealimg = BP.WF.Glo.getCCFlowAppPath() + "DataUser/Seal/" + deptno + "_" + stationno + ".jpg";

			if ((new java.io.File(BP.Sys.Glo.getRequest().getRealPath(sealimg))).isFile() == false) {
				return "签章文件：" + sealimg + "不存在，请联系管理员！";
			}

			FrmEleDB athDB_N = new FrmEleDB();
			athDB_N.setFK_MapData("ND" + nodeid);
			athDB_N.setRefPKVal(String.valueOf(workid));
			athDB_N.setEleID(String.valueOf(workid));
			athDB_N.GenerPKVal();
			athDB_N.setTag1(sealimg);
			athDB_N.DirectInsert();

			return null;
		} catch (RuntimeException ex) {
			return "签章错误：" + ex.getMessage();
		}
	}

	/// #region 与工作处理器相关的接口

	public static String CCForm_AddAth(int nodeid, String flowid, long workid, String athNo, String frmID,
			String filePath, String fileName, String sort, long fid, long pworkid) throws Exception {
		File item = new File(filePath);
		// 求主键. 如果该表单挂接到流程上.
		String pkVal = String.valueOf(workid);
		// 多附件描述.
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment(athNo);
		MapData mapData = new MapData(frmID);
		if (nodeid != 0 && nodeid != 999999) {
			// 判断表单方案。
			FrmNode fn = new FrmNode(flowid, nodeid, frmID);
			if (fn.getFrmSln() == FrmSln.Readonly)
				throw new Exception("err@不允许上传附件.");

			// 是默认的方案的时候.
			if (fn.getFrmSln() == FrmSln.Default) {
				// 判断当前方案设置的whoIsPk ，让附件集成 whoIsPK 的设置。
				if (fn.getWhoIsPK() == WhoIsPK.FID)
					pkVal = Long.toString(fid);

				if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
					pkVal = Long.toString(pworkid);
			}

			// 自定义方案.
			if (fn.getFrmSln() == FrmSln.Self) {
				athDesc = new FrmAttachment(athNo + "_" + nodeid);
				if (athDesc.getHisCtrlWay() == AthCtrlWay.FID)
					pkVal = Long.toString(fid);

				if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
					pkVal = Long.toString(pworkid);
			}
		}
		// 获取上传文件是否需要加密
		boolean fileEncrypt = SystemConfig.getIsEnableAthEncrypt();
		if (athDesc.getAthSaveWay() == AthSaveWay.WebServer) {

			String savePath = athDesc.getSaveTo();
			if (savePath.contains("@") == true || savePath.contains("*") == true) {
				/* 如果有变量 */
				savePath = savePath.replace("*", "@");
				savePath = BP.WF.Glo.DealExp(savePath, null, null);

				if (savePath.contains("@") && nodeid != 0) {
					/* 如果包含 @ */
					BP.WF.Flow flow = new BP.WF.Flow(flowid);
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(workid);
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true)
					throw new Exception("@路径配置错误,变量没有被正确的替换下来." + savePath);

			} else {
				savePath = athDesc.getSaveTo() + "\\" + pkVal;
			}

			// 替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");
			try {
				if (savePath.indexOf(":") == -1)
					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);

				File fileInfo = new File(savePath);

				if (fileInfo.exists() == false)
					fileInfo.mkdirs();

			} catch (Exception ex) {
				throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath)
						+ "===" + savePath + "@技术问题:" + ex.getMessage());

			}

			String guid = BP.DA.DBAccess.GenerGUID();
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String realSaveTo = savePath + "\\" + guid + "." + fileName;

			realSaveTo = realSaveTo.replace("~", "-");
			realSaveTo = realSaveTo.replace("'", "-");
			realSaveTo = realSaveTo.replace("*", "-");

			String saveTo = realSaveTo;
			if (fileEncrypt == true)
				saveTo = realSaveTo + ".tmp";
			File file = new File(saveTo); // 获取根目录对应的真实物理路径

			try {
				// 构造临时对象
				InputStream is = new FileInputStream(item);
				int buffer = 1024; // 定义缓冲区的大小
				int length = 0;
				byte[] b = new byte[buffer];
				FileOutputStream fos = new FileOutputStream(file);
				while ((length = is.read(b)) != -1) {
					// 计算上传文件的百分比
					fos.write(b, 0, length); // 向文件输出流写读取的数据
				}
				fos.close();
			} catch (RuntimeException ex) {

				throw new RuntimeException("@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage());
			}

			if (fileEncrypt == true) {
				File fileT = new File(saveTo);
				AesEncodeUtil.encryptFile(saveTo, realSaveTo);
				fileT.delete();// 删除临时文件
			}

			File info = new File(realSaveTo);

			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
			dbUpload.setFK_FrmAttachment(athNo);
			dbUpload.setSort(sort);
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFileExts(ext);
			dbUpload.setFID(fid);
			dbUpload.setNodeID(nodeid);
			if (fileEncrypt == true)
				dbUpload.SetPara("IsEncrypt", 1);

			/// #region 处理文件路径，如果是保存到数据库，就存储pk.
			if (athDesc.getAthSaveWay() == AthSaveWay.WebServer) {
				// 文件方式保存
				dbUpload.setFileFullName(realSaveTo);
			}

			if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
				// 保存到数据库
				dbUpload.setFileFullName(dbUpload.getMyPK());
			}
			/// #endregion 处理文件路径，如果是保存到数据库，就存储pk.

			dbUpload.setFileName(fileName);
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(BP.Web.WebUser.getNo());
			dbUpload.setRecName(BP.Web.WebUser.getName());
			dbUpload.setFID(fid);
			dbUpload.setUploadGUID(guid);
			dbUpload.setRefPKVal(pkVal);

			dbUpload.Insert();

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				// 执行文件保存.
				BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK",
						dbUpload.getMyPK(), "FDB");
			}

		}
		/// #endregion 文件上传的iis服务器上 or db数据库里.

		/// #region 保存到数据库 / FTP服务器上.
		if (athDesc.getAthSaveWay() == AthSaveWay.DB || athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
			String guid = BP.DA.DBAccess.GenerGUID();

			// 把文件临时保存到一个位置.
			String temp = SystemConfig.getPathOfTemp() + "/" + "" + guid + ".tmp";

			String tempD = temp;
			if (fileEncrypt == true)
				tempD = SystemConfig.getPathOfTemp() + "/" + "" + guid + "_Desc" + ".tmp";
			File tempFile = new File(tempD);
			InputStream is = null;
			try {
				// 构造临时对象
				is = new FileInputStream(item);
				int buffer = 1024; // 定义缓冲区的大小
				int length = 0;
				byte[] b = new byte[buffer];
				FileOutputStream fos = new FileOutputStream(tempFile);
				while ((length = is.read(b)) != -1) {
					fos.write(b, 0, length); // 向文件输出流写读取的数据
				}
				fos.close();
				is.close();
			} catch (Exception ex) {
				tempFile.delete();
				throw new RuntimeException("@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage());

			}
			if (fileEncrypt == true) {
				File fileTD = new File(tempD);
				AesEncodeUtil.encryptFile(tempD, temp);// 加密
				fileTD.delete();// 删除临时文件
			}

			File info = new File(temp);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(BP.DA.DBAccess.GenerGUID());
			dbUpload.setNodeID(nodeid);
			dbUpload.setFK_FrmAttachment(athDesc.getMyPK());
			dbUpload.setSort(sort);
			dbUpload.setFID(fid); // 流程id.
			if (athDesc.getAthUploadWay() == AthUploadWay.Inherit) {
				/* 如果是继承，就让他保持本地的PK. */
				dbUpload.setRefPKVal(pkVal);
			}

			if (athDesc.getAthUploadWay() == AthUploadWay.Interwork) {
				/* 如果是协同，就让他是PWorkID. */
				String pWorkID = String.valueOf(BP.DA.DBAccess
						.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + pkVal, 0));
				if (pWorkID == null || pWorkID == "0")
					pWorkID = pkVal;
				dbUpload.setRefPKVal(pWorkID);
			}
			String exts = fileName.substring(fileName.lastIndexOf("."));
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(athDesc.getMyPK());
			dbUpload.setFileName(fileName);
			dbUpload.setFileExts(exts);
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(BP.Web.WebUser.getNo());
			dbUpload.setRecName(BP.Web.WebUser.getName());
			if (fileEncrypt == true)
				dbUpload.SetPara("IsEncrypt", 1);

			dbUpload.setUploadGUID(guid);

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				dbUpload.Insert();
				// 把文件保存到指定的字段里.
				dbUpload.SaveFileToDB("FileDB", temp);
			}

			if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM");
				String ny = sdf.format(new Date());

				String workDir = ny + "\\" + athDesc.getFK_MapData() + "\\";

				// 特殊处理文件路径.
				if (SystemConfig.getCustomerNo().equals("BWDA")) {

					sdf = new SimpleDateFormat("yyyy_MM_dd");
					ny = sdf.format(new Date());

					ny = ny.replace("_", "/");
					ny = ny.replace("_", "/");

					workDir = ny + "/" + WebUser.getNo() + "/";
				}

				boolean isOK = false;

				if (SystemConfig.getFTPServerType().equals("FTP")) {

					FtpUtil ftpUtil = BP.WF.Glo.getFtpUtil();

					ftpUtil.changeWorkingDirectory(workDir, true);

					// 把文件放在FTP服务器上去.
					isOK = ftpUtil.uploadFile(guid + "." + dbUpload.getFileExts(), temp);

					ftpUtil.releaseConnection();
				}

				if (SystemConfig.getFTPServerType().equals("SFTP")) {

					SftpUtil ftpUtil = BP.WF.Glo.getSftpUtil();

					ftpUtil.changeWorkingDirectory(workDir, true);
					// 把文件放在FTP服务器上去.
					isOK = ftpUtil.uploadFile(guid + "." + dbUpload.getFileExts(), temp);
					ftpUtil.releaseConnection();
				}

				// 删除临时文件
				tempFile.delete();
				new File(SystemConfig.getPathOfTemp() + "" + guid + "_Desc" + ".tmp").delete();

				// 设置路径.
				dbUpload.setFileFullName(workDir + guid + "." + dbUpload.getFileExts());

				if (isOK == false)
					throw new com.sun.star.uno.Exception("err文件上传失败，请检查ftp服务器配置信息");

				dbUpload.Insert();

			}
		}

		return "附件添加成功";
	}

	/**
	 * 获得一个节点要转向的节点
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param ndFrom
	 *            节点从
	 * @param workid
	 *            工作ID
	 * @return 返回可以到达的节点
	 * @throws Exception
	 */
	public static Nodes WorkOpt_GetToNodes(String flowNo, int ndFrom, long workid, long FID) throws Exception {
		Nodes nds = new Nodes();

		Node nd = new Node(ndFrom);
		Nodes toNDs = nd.getHisToNodes();

		Flow fl = nd.getHisFlow();
		GERpt rpt = fl.getHisGERpt();
		rpt.setOID(FID == 0 ? workid : FID);
		rpt.Retrieve();

		// 首先输出普通的节点
		for (Node mynd : toNDs.ToJavaList()) {
			if (mynd.getHisRunModel() == RunModel.SubThread) {
				continue; // 如果是子线程节点.
			}
			/// #region 判断方向条件,如果设置了方向条件，判断是否可以通过，不能通过的，就不让其显示.
			Cond cond = new Cond();
			int i = cond.Retrieve(CondAttr.FK_Node, nd.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
			// 设置方向条件，就判断它。
			if (i > 0) {
				cond.setWorkID(workid);
				cond.en = rpt;
				if (cond.getIsPassed() == false) {
					continue;
				}
			}

			/// #endregion

			nds.AddEntity(mynd);
		}

		// 同表单子线程.
		for (Node mynd : toNDs.ToJavaList()) {
			if (mynd.getHisRunModel() != RunModel.SubThread) {
				continue; // 如果是子线程节点.
			}

			if (mynd.getHisSubThreadType() == SubThreadType.UnSameSheet) {
				continue; // 如果是异表单的分合流.
			}
			/// #region 判断方向条件,如果设置了方向条件，判断是否可以通过，不能通过的，就不让其显示.
			Cond cond = new Cond();
			int i = cond.Retrieve(CondAttr.FK_Node, nd.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
			// 设置方向条件，就判断它。
			if (i > 0) {
				cond.setWorkID(workid);
				cond.en = rpt;
				if (cond.getIsPassed() == false) {
					continue;
				}
			}
			nds.AddEntity(mynd);
		}

		// 检查是否具有异表单的子线程.
		boolean isHave = false;
		for (Node mynd : toNDs.ToJavaList()) {
			if (mynd.getHisSubThreadType() == SubThreadType.UnSameSheet) {
				isHave = true;
			}
		}

		if (isHave) {
			Node myn1d = new Node();
			myn1d.setNodeID(0);
			myn1d.setName("可以分发启动的节点");
			nds.AddEntity(myn1d);

			// 增加异表单的子线程
			for (Node mynd : toNDs.ToJavaList()) {
				if (mynd.getHisSubThreadType() != SubThreadType.UnSameSheet) {
					continue;
				}

				/// #region 判断方向条件,如果设置了方向条件，判断是否可以通过，不能通过的，就不让其显示.
				Cond cond = new Cond();
				int i = cond.Retrieve(CondAttr.FK_Node, nd.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
				// 设置方向条件，就判断它。
				if (i > 0) {
					cond.setWorkID(workid);
					cond.en = rpt;
					if (cond.getIsPassed() == false) {
						continue;
					}
				}

				/// #endregion

				nds.AddEntity(mynd);
			}
		}
		// 返回它.
		return nds;
	}

	/**
	 * 在节点选择转向功能界面，获得当前人员上一次选择的节点，在界面里让其自动选择。 以改善用户操作体验，就类似于默认记忆上一次的操作功能。
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeID
	 *            当前节点编号
	 * @return 返回上一次当前用户选择的节点,如果没有找到（当前用户第一次发送的情况下找不到）就返回0.
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public static int WorkOpt_ToNodes_GetLasterSelectNodeID(String flowNo, int nodeID)
			throws NumberFormatException, Exception {
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
		case Access:
			sql = "SELECT TOP 1 NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='"
					+ BP.Web.WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType="
					+ ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue()
					+ " OR ActionType=" + ActionType.SubThreadForward.getValue() + ")  ORDER BY RDT DESC";
			break;
		case Oracle:
			sql = "SELECT NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE  RowNum=1 AND EmpFrom='"
					+ BP.Web.WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType="
					+ ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue()
					+ " OR ActionType=" + ActionType.SubThreadForward.getValue() + ")  ORDER BY RDT DESC";
			break;
		case MySQL:
			sql = "SELECT NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='" + BP.Web.WebUser.getNo()
					+ "' AND NDFrom=" + nodeID + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType="
					+ ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue()
					+ ") limit 0,1";
			break;
		case Informix:
			sql = "SELECT first 1 NDTo FROM ND" + Integer.parseInt(flowNo) + "Track WHERE EmpFrom='"
					+ BP.Web.WebUser.getNo() + "' AND NDFrom=" + nodeID + " AND (ActionType="
					+ ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue()
					+ " OR ActionType=" + ActionType.SubThreadForward.getValue() + ")  ORDER BY RDT DESC";
			break;
		default:
			throw new RuntimeException("@没有实现该类型的数据库支持.");
		}
		return BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
	}

	/**
	 * 发送到节点
	 * 
	 * @param flowNo
	 * @param node
	 * @param workid
	 * @param fid
	 * @param toNodes
	 * @throws Exception
	 */
	public static SendReturnObjs WorkOpt_SendToNodes(String flowNo, int nodeID, long workid, long fid, String toNodes)
			throws Exception {
		// 把参数更新到数据库里面.
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
	 * 获得接收人的数据源
	 * 
	 * @param nodeID
	 *            指定节点
	 * @param WorkID
	 *            工作ID
	 * @param FID
	 *            流程ID
	 * @return
	 * @throws Exception
	 */
	public static DataSet WorkOpt_AccepterDB(String FK_Flow, int nodeID, long WorkID, long FID) throws Exception {
		DataSet ds = new DataSet();
		Selector MySelector = new Selector(nodeID);
		switch (MySelector.getSelectorModel()) {
		case Station:
			DataTable dt = WorkOpt_Accepter_ByStation(nodeID);
			dt.TableName = "Port_Emp";
			ds.Tables.add(dt);
			// 部门表
			// string sql = "SELECT * FROM Port_Dept ";
			// DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
			// dt1.TableName = "Port_Dept";
			// ds.Tables.add(dt1);
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

	public static DataSet WorkOpt_AccepterDB(int nodeID, long WorkID, long FID) throws Exception {
		DataSet ds = new DataSet();

		Selector en = new Selector(nodeID);
		switch (en.getSelectorModel()) {
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
	 * 获取节点绑定岗位人员
	 * 
	 * @param nodeID
	 *            指定的节点
	 * @return
	 * @throws Exception
	 */
	private static DataTable WorkOpt_Accepter_ByStation(int nodeID) throws Exception {
		if (nodeID == 0) {
			throw new RuntimeException(
					"@流程设计错误，没有转向的节点。举例说明: 当前是A节点。如果您在A点的属性里启用了[接受人]按钮，那么他的转向节点集合中(就是A可以转到的节点集合比如:A到B，A到C, 那么B,C节点就是转向节点集合)，必须有一个节点是的节点属性的[访问规则]设置为[由上一步发送人员选择]");
		}

		NodeStations stas = new NodeStations(nodeID);
		if (stas.size() == 0) {
			BP.WF.Node toNd = new BP.WF.Node(nodeID);
			throw new RuntimeException("@流程设计错误：设计员没有设计节点[" + toNd.getName() + "]，接受人的岗位范围。");
		}
		// 优先解决本部门的问题。
		String sql = "";
		if (BP.WF.Glo.getOSModel() == OSModel.OneMore) {
			sql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
			sql += "SELECT FK_EMP FROM Port_DeptEmpStation WHERE FK_STATION ";
			sql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") ";
			sql += ") AND a.No IN (SELECT No FROM Port_Emp WHERE FK_Dept ='" + WebUser.getFK_Dept() + "')";
			sql += " ORDER BY B.Idx,B.No,A.Idx,A.No ";
		} else {
			sql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
			sql += "SELECT FK_EMP FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_STATION ";
			sql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") ";
			sql += ") AND a.No IN (SELECT No FROM Port_Emp WHERE FK_Dept ='" + WebUser.getFK_Dept() + "')";
			sql += " ORDER BY A.FK_DEPT,A.No ";
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0) {
			return dt;
		}

		// 组织结构中所有岗位人员
		sql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
		sql += "SELECT FK_EMP FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_STATION ";
		sql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") ";
		sql += ") ORDER BY A.FK_DEPT,A.No ";
		return BP.DA.DBAccess.RunSQLReturnTable(sql);
	}

	/**
	 * 按sql方式
	 * 
	 * @throws Exception
	 * 
	 */
	private static DataSet WorkOpt_Accepter_BySQL(int nodeID) throws Exception {
		DataSet ds = new DataSet();
		Selector MySelector = new Selector(nodeID);
		String sqlGroup = MySelector.getSelectorP1();
		sqlGroup = sqlGroup.replace("WebUser.No", WebUser.getNo());
		sqlGroup = sqlGroup.replace("@WebUser.Name", WebUser.getName());
		sqlGroup = sqlGroup.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		String sqlDB = MySelector.getSelectorP2();
		sqlDB = sqlDB.replace("WebUser.No", WebUser.getNo());
		sqlDB = sqlDB.replace("@WebUser.Name", WebUser.getName());
		sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		DataTable dtGroup = DBAccess.RunSQLReturnTable(sqlGroup);
		dtGroup.TableName = "Port_Dept";
		ds.Tables.add(dtGroup);
		DataTable dtDB = DBAccess.RunSQLReturnTable(sqlDB);
		dtDB.TableName = "Port_Emp";
		ds.Tables.add(dtDB);

		return ds;
	}

	/**
	 * 获取接收人选择器，按部门绑定
	 * 
	 * @param ToNode
	 * @return
	 */
	private static DataSet WorkOpt_Accepter_ByDept(int nodeID) {
		DataSet ds = new DataSet();
		String orderByIdx = BP.WF.Glo.getOSModel() == OSModel.OneMore ? "Idx," : "";
		String sqlGroup = "SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node='"
				+ nodeID + "') ORDER BY " + orderByIdx + "No";
		String sqlDB = "SELECT No,Name, FK_Dept FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node='"
				+ nodeID + "') ORDER BY " + orderByIdx + "No";

		DataTable dtGroup = DBAccess.RunSQLReturnTable(sqlGroup);
		dtGroup.TableName = "Port_Dept";
		ds.Tables.add(dtGroup);

		DataTable dtDB = DBAccess.RunSQLReturnTable(sqlDB);
		dtDB.TableName = "Port_Emp";
		ds.Tables.add(dtDB);

		return ds;
	}

	/**
	 * 按BindByEmp 方式
	 * 
	 */
	private static DataSet WorkOpt_Accepter_ByEmp(int nodeID) {
		String orderByIdx = BP.WF.Glo.getOSModel() == OSModel.OneMore ? "Idx," : "";
		String sqlGroup = "SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM Port_Emp WHERE No in(SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node='"
				+ nodeID + "')) ORDER BY " + orderByIdx + "No";
		String sqlDB = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE No in (SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node='"
				+ nodeID + "') ORDER BY " + orderByIdx + "No";

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
	 * 设置指定的节点接受人
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param emps
	 *            指定的人员集合zhangsan,lisi,wangwu
	 * @param isNextTime
	 *            是否下次自动设置
	 * @throws Exception
	 */
	public static void WorkOpt_SetAccepter(int nodeID, long workid, long fid, String emps, boolean isNextTime)
			throws Exception {
		SelectAccpers ens = new SelectAccpers();
		ens.Delete(SelectAccperAttr.FK_Node, nodeID, SelectAccperAttr.WorkID, workid);

		// 下次是否记忆选择，清空掉。
		String sql = "UPDATE WF_SelectAccper SET " + SelectAccperAttr.IsRemember + " = 0 WHERE Rec='" + WebUser.getNo()
				+ "' AND IsRemember=1 AND FK_Node=" + nodeID;
		BP.DA.DBAccess.RunSQL(sql);

		// 开始执行保存.
		String[] strs = emps.split("[,]", -1);
		for (String str : strs) {
			if (str == null || str.equals("")) {
				continue;
			}

			SelectAccper en = new SelectAccper();
			en.setMyPK(nodeID + "_" + workid + "_" + str);
			en.setFK_Emp(str);
			en.setFK_Node(nodeID);
			en.setWorkID(workid);
			en.setRec(WebUser.getNo());
			en.setIsRemember(isNextTime);
			en.Insert();
		}
	}

	/**
	 * 发送到节点
	 * 
	 * @param flowNo
	 * @param node
	 * @param workid
	 * @param fid
	 * @param toNodes
	 * @throws Exception
	 */
	public static SendReturnObjs WorkOpt_SendToEmps(String flowNo, int nodeID, long workid, long fid, int toNodeID,
			String toEmps, boolean isRememberMe) throws Exception {
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

	/// #region 附件上传
	public static String SaveImageAsFile(byte[] img, String pkval, String fk_Frm_Ele) {
		/*
		 * FrmEle fe = new FrmEle(fk_Frm_Ele); System.Drawing.Image newImage;
		 * MemoryStream ms = new MemoryStream(img, 0, img.length); try {
		 * ms.Write(img, 0, img.length); newImage = Image.FromStream(ms, true);
		 * Bitmap bitmap = new Bitmap(newImage, new Size(fe.WOfInt, fe.HOfInt));
		 * 
		 * if (System.IO.Directory.Exists(fe.HandSigantureSavePath +
		 * "\\" + fe.FK_MapData + "\\") == false) {
		 * System.IO.Directory.CreateDirectory(fe.HandSigantureSavePath +
		 * "\\" + fe.FK_MapData + "\\"); }
		 * 
		 * String saveTo = fe.HandSigantureSavePath +
		 * "\\" + fe.FK_MapData + "\\" + pkval + ".jpg"; bitmap.Save(saveTo,
		 * ImageFormat.Jpeg);
		 * 
		 * String pathFile = BP.Sys.Glo.Request.ApplicationPath +
		 * fe.HandSiganture_UrlPath + fe.FK_MapData + "/" + pkval + ".jpg";
		 * FrmEleDB ele = new FrmEleDB(); ele.FK_MapData = fe.FK_MapData;
		 * ele.EleID = fe.EleID; ele.RefPKVal = pkval; ele.Tag1 =
		 * pathFile.replace("\\\\", "\\"); ele.Tag1 = pathFile.replace("////",
		 * "//");
		 * 
		 * ele.Tag2 = saveTo.replace("\\\\", "\\"); ele.Tag2 =
		 * saveTo.replace("////", "//");
		 * 
		 * ele.GenerPKVal(); ele.Save();
		 * 
		 * return pathFile; } finally { ms.dispose(); }
		 */
		return "";
	}

	/**
	 * 上传文件.
	 * 
	 * @param FileByte
	 * @param fileName
	 * @return
	 */
	public static String UploadFile(byte[] FileByte, String fileName) {
		// String path =
		// System.Web.HttpContext.Current.Request.PhysicalApplicationPath +
		// "\\DataUser\\UploadFile";
		String path = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/")
				+ "/DataUser/UploadFile";
		if (!(new java.io.File(path)).isDirectory()) {
			(new java.io.File(path)).mkdirs();
		}
		String filePath = path + "/" + fileName;
		if ((new java.io.File(filePath)).isFile()) {
			(new java.io.File(filePath)).delete();
		}

		// 这里使用绝对路径来索引
		// FileStream stream = new FileStream(filePath, FileMode.CreateNew);
		// stream.Write(FileByte, 0, FileByte.length);
		// stream.Close();

		return filePath;
	}

	/**
	 * 更新时间状态, 交付给 huangzhimin. 作用：按照当前的时间，每天两次更新WF_GenerWorkFlow 的 TodoSta
	 * 状态字段。 该字段： 0=正常(绿牌), 1=预警(黄牌), 2=逾期(红牌), 3=按时完成(绿牌) , 4=逾期完成(红牌).
	 * 该方法作用是，每天，中午时间段，与下午时间段，执行更新这两个状态，仅仅更新两次。
	 * 
	 * @throws Exception
	 */
	public static void DTS_GenerWorkFlowTodoSta() throws Exception {
		// 中午的更新, 与发送邮件通知.
		boolean isPM = false;
		/// #region 求出是否可以更新状态.
		if (DateUtils.getHour(new Date()) >= 0 && DateUtils.getHour(new Date()) < 12) {
			isPM = true;
			String timeKey = "DTSTodoStaPM" + BP.Tools.DateUtils.getCurrentDate();
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time == null) {
				GloVar var = new GloVar();
				var.setNo(timeKey);
				var.setName("时效调度 WFTodoSta PM 调度");
				var.setGroupKey("WF");
				var.setVal(timeKey); // 更新时间点.
				var.setNote("时效调度PM" + timeKey);
				var.Insert();
				time = var.getVal();
			} else {
				// 如果有数据，就返回，说明已经执行过了。
				return;
			}
		}

		// 下午时间段.
		if (DateUtils.getHour(new Date()) >= 13 && DateUtils.getHour(new Date()) < 24) {
			String timeKey = "DTSTodoStaAM" + BP.Tools.DateUtils.getCurrentDate();
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time == null) {
				GloVar var = new GloVar();
				var.setNo(timeKey);
				var.setName("时效调度 WFTodoSta AM 调度");
				var.setGroupKey("WF");
				var.setVal(timeKey); // 更新时间点.
				var.setNote("时效调度AM" + timeKey);
				var.Insert();
				time = var.getVal();
			} else {
				// 如果有数据，就返回，说明已经执行过了。
				return;
			}
		}

		/// #endregion 求出是否可以更新状态.

		// 系统期望的是，每一个人仅发一条信息. "您有xx个预警工作，yy个预期工作，请及时处理。"

		DataTable dtEmps = new DataTable();
		dtEmps.Columns.Add("EmpNo", String.class);
		dtEmps.Columns.Add("WarningNum", Integer.class);
		dtEmps.Columns.Add("OverTimeNum", Integer.class);

		String timeDT = DateUtils.format(new Date(), "yyyy-MM-dd");
		String sql = "";

		// 查询出预警的工作.
		sql = " SELECT DISTINCT FK_Emp,COUNT(FK_Emp) as Num , 0 as DBType FROM WF_GenerWorkerlist A WHERE a.DTOfWarning =< '"
				+ timeDT + "' AND a.SDT <= '" + timeDT + "' AND A.IsPass=0  ";
		sql += "  UNION ";
		sql += "SELECT DISTINCT FK_Emp,COUNT(FK_Emp) as Num , 1 as DBType FROM WF_GenerWorkerlist A WHERE  a.SDT >'"
				+ timeDT + "' AND A.IsPass=0 ";

		sql = "SELECT * FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT
				+ "' AND A.IsPass=0 ORDER BY FK_Node,FK_Emp ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		// 初始化人员.
		String emps = "";
		for (DataRow dr : dt.Rows) {
			// dtEmps.Rows

		}

		// 向预警的人员发消息.
		Node nd = new Node();
		WFEmp emp = new WFEmp();
		for (DataRow dr : dt.Rows) {
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			int fk_node = Integer.parseInt(dr.getValue("FK_Node").toString());
			String fk_emp = dr.getValue("FK_Emp").toString();

			if (nd.getNodeID() != fk_node) {
				nd.setNodeID(fk_node);
				nd.Retrieve();
			}

			if (nd.getHisCHWay() != CHWay.ByTime) {
				continue; // 非按照时效考核.
			}

			if (nd.getWAlertRole() == CHAlertRole.None) {
				continue;
			}

			// 如果仅仅提醒一次.
			if (nd.getWAlertRole() == CHAlertRole.OneDayOneTime && isPM == true) {

			} else {
				continue;
			}

			if (!fk_emp.equals(emp.getNo())) {
				emp.setNo(fk_emp);
				emp.Retrieve();
			}
		}

		if (dt.Rows.size() >= 1) {
			// 更新预警状态.
			sql = "UPDATE WF_GenerWorkFlow  SET TodoSta=1 ";
			sql += " WHERE WorkID IN (SELECT WorkID FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT
					+ "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ) ";
			sql += " AND WF_GenerWorkFlow.WFState!=3 ";
			sql += " AND WF_GenerWorkFlow.TodoSta=0 ";
			int i = BP.DA.DBAccess.RunSQL(sql);
		}
		// 更新逾期期状态.
		sql = "UPDATE WF_GenerWorkFlow  SET TodoSta=2 ";
		sql += " WHERE WorkID IN (SELECT WorkID FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT
				+ "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ) ";
		sql += " AND WF_GenerWorkFlow.WFState!=3 ";
		sql += " AND WF_GenerWorkFlow.TodoSta=1 ";
		BP.DA.DBAccess.RunSQL(sql);
	}

	/**
	 * 预警与逾期的提醒.
	 * 
	 * @throws Exception
	 */
	private static void DTS_SendMsgToWorker() throws Exception {
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 0
				&& Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) {
			String timeKey = "DTSWarningPM" + BP.Tools.DateUtils.getCurrentDate();
			Paras ps = new Paras();
			ps.SQL = "SELECT Val FROM Sys_GloVar WHERE No='" + timeKey + "'";
			String time = DBAccess.RunSQLReturnStringIsNull(ps, null);
			if (time == null) {
				// 没有数据就说明没有执行过.
				GloVar var = new GloVar();
				var.setNo(timeKey);
				var.setName("预警信息发送 DTSWarningPM 调度");
				var.setGroupKey("WF");
				var.setVal(timeKey); // 更新时间点.
				var.setNote("预警信息发送PM" + timeKey);
				var.Insert();
				// 查找一天预警1次的消息记录，并执行推送。
				String sql = "SELECT A.WorkID, A.Title, A.FlowName, A.TodoSta, B.FK_Emp, b.FK_EmpText, C.WAlertWay  FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Node C  ";
				sql += " WHERE A.WorkID=B.WorkID AND A.FK_Node=C.NodeID AND a.TodoSta=1 AND ( C.WAlertRole=1 OR C.WAlertRole=2 ) ";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					CHAlertWay way = CHAlertWay.forValue(Integer.parseInt(dr.getValue("WAlertWay").toString())); // 提醒方式.
					long workid = Long.parseLong(dr.getValue("WorkID").toString());
					String title = dr.getValue("Title").toString();
					String flowName = dr.getValue("FlowName").toString();
					String empNo = dr.getValue("FK_Emp").toString();
					String empName = dr.getValue("FK_EmpText").toString();

					BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(empNo);
					if (way == CHAlertWay.ByEmail) {
						String titleMail = "";
						String docMail = "";
					}

					if (way == CHAlertWay.BySMS) {
						String titleMail = "";
						String docMail = "";
						// BP.WF.Dev2Interface.Port_SendMsg(emp.Email,
						// titleMail, "");
					}
				}
			}
		}
	}

	/**
	 * 生成工作的 TimeSpan
	 */
	public static void DTS_GenerWorkFlowTimeSpan() {
		// 只能在周1执行.
		// java.util.Date dtNow = new java.util.Date();
		Date dtNow = new Date();
		// 设置为开始的日期为周1.
		// java.util.Date dtBegin = new java.util.Date();
		Date dtBegin = new Date();
		dtBegin = DateUtils.getFirstDayOfWeek(dtBegin);

		for (int i = 0; i < 8; i++) {
			if (dtBegin.getTime() == Calendar.MONDAY) {
				break;
			}
			dtBegin.setTime(Long.parseLong(DateUtils.addDayFromCurrentDate(-1)));
		}

		// 结束日期为当前.
		// java.util.Date dtEnd = dtBegin.plusDays(7);
		Date dtEnd = new Date(DateUtils.addDayFromCurrentDate(-7));

		// 设置为上周.
		String sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.NextWeek.getValue() + " WHERE RDT >= '"
				+ DateUtils.format(dtBegin, DataType.getSysDataFormat()) + " 00:00' AND RDT <= '"
				+ DateUtils.format(dtEnd, DataType.getSysDataFormat()) + " 00:00' AND TSpan=0";
		BP.DA.DBAccess.RunSQL(sql);

		dtBegin = DateUtils.addDay(dtBegin, 7);
		dtEnd = DateUtils.addDay(dtEnd, 7);

		// 把上周的，设置为两个周以前.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.TowWeekAgo.getValue() + " WHERE RDT >= '"
				+ DateUtils.format(dtBegin, DataType.getSysDataFormat()) + " 00:00' AND RDT <= '"
				+ DateUtils.format(dtEnd, DataType.getSysDataFormat()) + " 00:00' AND (TSpan=1 OR TSpan=0) ";
		BP.DA.DBAccess.RunSQL(sql);

		// 把上周的，设置为更早.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.More.getValue() + " WHERE RDT <= '"
				+ DateUtils.format(dtBegin, DataType.getSysDataFormat()) + " 00:00' AND (TSpan=2 OR TSpan=0)";
		BP.DA.DBAccess.RunSQL(sql);
	}

	public static void Flow_Confirm(long workid) throws Exception {
		// TODO Auto-generated method stub
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		int i = gwf.RetrieveFromDBSources();
		if (i == 0) {
			throw new RuntimeException("@ 设置关注错误：没有找到 WorkID= " + workid + " 的实例。");
		}
		String isFocus = gwf.GetParaString("C_" + WebUser.getNo(), "0");

		if (isFocus.equals("0")) {
			gwf.SetPara("C_" + WebUser.getNo(), "1");
		} else {
			gwf.SetPara("C_" + WebUser.getNo(), "0");
		}

		gwf.DirectUpdate();
	}

	/**
	 * 执行工作分配
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeID
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            FID
	 * @param toEmps
	 *            要分配的人，多个人用逗号分开.
	 * @param msg
	 *            分配原因.
	 * @return 分配信息.
	 * @throws Exception
	 */
	public static String Node_Allot(String flowNo, int nodeID, long workID, long fid, String toEmps, String msg)
			throws Exception {
		// 生成实例.
		GenerWorkerLists gwls = new GenerWorkerLists(workID, nodeID);

		// 要分配给的人员.
		String[] emps = toEmps.split("[,]", -1);
		for (String empNo : emps) {
			if (DotNetToJavaStringHelper.isNullOrEmpty(empNo) == true) {
				continue;
			}

			// 人员实体.
			Emp empEmp = new Emp(empNo);

			GenerWorkerList gwl = null; // 接收人

			// 开始找接收人.
			for (GenerWorkerList item : gwls.ToJavaList()) {
				if (empNo.equals(item.getFK_Emp())) {
					gwl = item;
					break;
				}
			}

			// 没有找到的情况, 就获得一个实例，作为数据样本然后把数据insert.
			if (gwl == null) {
				gwl = (GenerWorkerList) ((gwls.get(0) instanceof GenerWorkerList) ? gwls.get(0) : null);
				gwl.setFK_Emp(empEmp.getNo());
				gwl.setFK_EmpText(empEmp.getName());
				gwl.setIsEnable(true);
				gwl.setIsPassInt(0);
				gwl.Insert();
				continue;
			}

			// 如果被禁用了，就启用他.
			if (gwl.getIsEnable() == false) {
				gwl.setIsEnable(true);
				gwl.Update();
			}
		}
		return "分配成功.";
	}

	/**
	 * 获得指定人的流程发起列表
	 * 
	 * @param userNo
	 *            发起人编号
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_StarFlows(String userNo) throws Exception {		
		return DB_StarFlows(userNo,null);
	}
	/**
	 * 获得指定人的流程发起列表
	 * 
	 * @param userNo
	 *            发起人编号
	 * @return
	 * @throws Exception
	 */
	public static DataTable DB_StarFlows(String userNo, String domain) throws Exception {
		DataTable dt = DB_GenerCanStartFlowsOfDataTable(userNo,domain);
		return dt;
	}

	/**
	 * 会签的数量
	 * 
	 * @throws Exception
	 * 
	 */
	public static int getTodolist_HuiQian() throws Exception {
		// 获取数据.
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		BP.DA.Paras ps = new BP.DA.Paras();
		ps.SQL = "SELECT COUNT(workid) as Num FROM WF_GenerWorkerlist WHERE FK_Emp=" + dbStr + "FK_Emp AND IsPass=90";
		ps.Add(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo());
		return BP.DA.DBAccess.RunSQLReturnValInt(ps);
	}

	/**
	 * 获得当前节点上一步发送日志记录
	 * 
	 * @param WorkID
	 *            工作流程ID
	 * @param FK_Node
	 *            当前节点编号
	 * @return 上一节点发送记录
	 * @throws Exception
	 */
	public static DataTable Flow_GetPreviousNodeTrack(long WorkID, int FK_Node) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
		if (gwf.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("没有查询到相关业务实例");
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras pas = new Paras();
		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			pas.SQL = "SELECT TOP 1 * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + dbstr
					+ "WorkID  AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue()
					+ ") ORDER BY RDT DESC";
			break;
		case Oracle:
			pas.SQL = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track  WHERE WorkID=" + dbstr
					+ "WorkID  AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue()
					+ ") AND ROWNUM=1 ORDER BY RDT DESC ";
			break;
		case MySQL:
			pas.SQL = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track  WHERE WorkID=" + dbstr
					+ "WorkID AND NDTo=" + dbstr + "NDTo AND (ActionType=1 OR ActionType=" + ActionType.Skip.getValue()
					+ ") ORDER BY RDT DESC limit 0,1 ";
			break;
		default:
			break;
		}
		pas.Add("WorkID", WorkID);
		pas.Add("NDTo", FK_Node);
		return BP.DA.DBAccess.RunSQLReturnTable(pas);
	}

}