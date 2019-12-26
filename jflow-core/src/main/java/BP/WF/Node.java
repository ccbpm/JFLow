package BP.WF;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Sys.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Web.WebUser;
import java.math.*;

/**
 * 这里存放每个节点的信息.
 */
public class Node extends Entity {
	/**
	 * 方向条件控制规则
	 * 
	 * @throws Exception
	 */
	public final CondModel getCondModel() throws Exception {
		return CondModel.forValue(this.GetValIntByKey(NodeAttr.CondModel));
	}

	public final void setCondModel(CondModel value) throws Exception {
		this.SetValByKey(NodeAttr.CondModel, value.getValue());
	}

	/**
	 * 超时处理方式
	 */
	public final OutTimeDeal getHisOutTimeDeal() throws Exception {
		return OutTimeDeal.forValue(this.GetValIntByKey(NodeAttr.OutTimeDeal));
	}

	public final void setHisOutTimeDeal(OutTimeDeal value) throws Exception {
		this.SetValByKey(NodeAttr.OutTimeDeal, value.getValue());
	}

	/**
	 * 超时处理内容.
	 */
	public final String getDoOutTime() throws Exception {
		return this.GetValStrByKey(NodeAttr.DoOutTime);
	}

	public final void setDoOutTime(String value) throws Exception {
		this.SetValByKey(NodeAttr.DoOutTime, value);
	}

	/**
	 * 子线程类型
	 */
	public final SubThreadType getHisSubThreadType() throws Exception {
		return SubThreadType.forValue(this.GetValIntByKey(NodeAttr.SubThreadType));
	}

	public final void setHisSubThreadType(SubThreadType value) throws Exception {
		this.SetValByKey(NodeAttr.SubThreadType, value.getValue());
	}

	/**
	 * 手工启动的子流程个数
	 */
	public final int getSubFlowHandNum() throws Exception {
		return this.GetParaInt("SubFlowHandNum", 0);
	}

	/**
	 * 自动启动的子流程个数
	 */
	public final int getSubFlowAutoNum() throws Exception {
		return this.GetParaInt("SubFlowAutoNum", 0);
	}

	/**
	 * 延续子流程个数
	 */
	public final int getSubFlowYanXuNum() throws Exception {
		return this.GetParaInt("SubFlowYanXuNum", 0);
	}

	public final CC getHisCC() throws Exception {
		Object tempVar = this.GetRefObject("HisCC");
		CC obj = tempVar instanceof CC ? (CC) tempVar : null;
		if (obj == null) {
			obj = new CC();
			obj.setNodeID(this.getNodeID());
			obj.Retrieve();
			this.SetRefObject("HisCC", obj);
		}
		return obj;
	}

	/**
	 * 他的将要转向的方向集合 如果他没有到转向方向,他就是结束节点. 没有生命周期的概念,全部的节点.
	 */
	public final Nodes getHisToNodes() throws Exception {
		Object tempVar = this.GetRefObject("HisToNodes");
		Nodes obj = tempVar instanceof Nodes ? (Nodes) tempVar : null;
		if (obj == null) {
			obj = new Nodes();
			obj.AddEntities(this.getHisToNDs());
			this.SetRefObject("HisToNodes", obj);
		}
		return obj;
	}

	/**
	 * 他的工作
	 * 
	 * @throws Exception
	 */
	public final Work getHisWork() throws Exception {
		Work wk = null;
		if (this.getIsStartNode()) {
			wk = new BP.WF.GEStartWork(this.getNodeID(), this.getNodeFrmID());
			wk.setHisNode(this);
			wk.setNodeID(this.getNodeID());
			return wk;
		}

		if (this.getFormType() != NodeFormType.FoolTruck || this.WorkID == 0 ) {
			wk = new BP.WF.GEWork(this.getNodeID(), this.getNodeFrmID());
			wk.setHisNode(this);
			wk.setNodeID(this.getNodeID());
			return wk;
		}

		// 如果是累加表单.
		wk = new BP.WF.GEWork(this.getNodeID(), this.getNodeFrmID());

		Map ma = wk.getEnMap();

		/* 求出来走过的表单集合 */
		String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A, WF_Node B ";
		sql += " WHERE A.NDFrom=B.NodeID  ";
		sql += "  AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.Start.getValue()
				+ "  OR ActionType=" + ActionType.Skip.getValue() + ")  ";
		sql += "  AND B.FormType=" + NodeFormType.FoolTruck.getValue() + " "; // 仅仅找累加表单.
		sql += "  AND NDFrom!=" + this.getNodeID() + " "; // 排除当前的表单.

		sql += "  AND (A.WorkID=" + this.WorkID + ") ";
		sql += " ORDER BY A.RDT ";

		// 获得已经走过的节点IDs.
		DataTable dtNodeIDs = DBAccess.RunSQLReturnTable(sql);
		String frmIDs = "";
		if (dtNodeIDs.Rows.size() > 0) {
			// 把所有的节点字段.
			for (DataRow dr : dtNodeIDs.Rows) {
				if (frmIDs.contains("ND" + dr.getValue(0).toString()) == true) {
					continue;
				}
				frmIDs += "'ND" + dr.getValue(0).toString() + "',";
			}

			frmIDs = frmIDs.substring(0, frmIDs.length() - 1);
			wk.HisPassedFrmIDs = frmIDs; // 求出所有的fromID.

			MapAttrs attrs = new MapAttrs();
			QueryObject qo = new QueryObject(attrs);
			qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + frmIDs + ")");
			qo.DoQuery();

			// 设置成不可以用.
			for (MapAttr item : attrs.ToJavaList()) {
				item.setUIIsEnable(false); // 设置为只读的.
				item.setDefValReal(""); // 设置默认值为空.

				ma.getAttrs().Add(item.getHisAttr());
			}

			// 设置为空.
			wk.setSQLCash(null);
		}

		wk.setHisNode(this);
		wk.setNodeID(this.getNodeID());
		wk.setSQLCash(null);

		BP.DA.Cash.getSQL_Cash().remove("ND" + this.getNodeID());
		return wk;

	}

	/**
	 * 他的工作s
	 * 
	 * @throws Exception
	 */
	public final Works getHisWorks() throws Exception {
		Works obj = this.getHisWork().getGetNewEntities() instanceof Works
				? (Works) this.getHisWork().getGetNewEntities() : null;
		return obj;

	}

	/**
	 * 流程
	 * 
	 * @throws Exception
	 */
	public final FrmNodes getHisFrmNodes() throws Exception {
		Object tempVar = this.GetRefObject("FrmNodes");
		FrmNodes obj = tempVar instanceof FrmNodes ? (FrmNodes) tempVar : null;
		if (obj == null) {
			obj = new FrmNodes(this.getNodeID());
			this.SetRefObject("FrmNodes", obj);
		}
		return obj;
	}

	/**
	 * 流程
	 * 
	 * @throws Exception
	 */
	public final Flow getHisFlow() throws Exception {
		Object tempVar = this.GetRefObject("Flow");
		Flow obj = tempVar instanceof Flow ? (Flow) tempVar : null;
		if (obj == null) {
			obj = new Flow(this.getFK_Flow());
			this.SetRefObject("Flow", obj);
		}
		return obj;
	}

	/**
	 * 消息推送.
	 * 
	 * @throws Exception
	 */
	public final PushMsgs getHisPushMsgs() throws Exception {
		Object tempVar = this.GetRefObject("PushMsg");
		PushMsgs obj = tempVar instanceof PushMsgs ? (PushMsgs) tempVar : null;
		if (obj == null) {
			obj = new PushMsgs(this.getNodeID());

			// 检查是否有默认的发送？如果没有就增加上他。
			boolean isHaveSend = false;
			boolean isHaveReturn = false;
			for (PushMsg item : obj.ToJavaList()) {
				if (item.getFK_Event().equals(EventListOfNode.SendSuccess)) {
					isHaveSend = true;
				}

				if (item.getFK_Event().equals(EventListOfNode.ReturnAfter)) {
					isHaveReturn = true;
				}
			}

			if (isHaveSend == false) {
				PushMsg pm = new PushMsg();
				pm.setFK_Event(EventListOfNode.SendSuccess);
				pm.setSMSPushWay(1); // 默认:让其使用短消息提醒.
				pm.setSMSPushModel("Email");
				obj.AddEntity(pm);
			}

			if (isHaveReturn == false) {
				PushMsg pm = new PushMsg();
				pm.setFK_Event(EventListOfNode.ReturnAfter);
				pm.setSMSPushWay(1); // 默认:让其使用短消息提醒.
				pm.setSMSPushModel("Email");
				obj.AddEntity(pm);
			}

			this.SetRefObject("PushMsg", obj);
		}
		return obj;
	}

	/**
	 * HisFrms
	 * 
	 * @throws Exception
	 */
	public final Frms getHisFrms() throws Exception {
		Frms frms = new Frms();
		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getNodeID());
		for (FrmNode fn : fns.ToJavaList()) {
			if (fn.getFrmEnableRole() == FrmEnableRole.Disable) {
				continue;
			}
			frms.AddEntity(fn.getHisFrm());
		}
		return frms;

	}

	/**
	 * 他的将要来自的方向集合 如果他没有到来的方向,他就是开始节点.
	 * 
	 * @throws Exception
	 */
	public final Nodes getFromNodes() throws Exception {
		Object tempVar = this.GetRefObject("HisFromNodes");
		Nodes obj = tempVar instanceof Nodes ? (Nodes) tempVar : null;
		if (obj == null) {
			// 根据方向生成到达此节点的节点。
			Directions ens = new Directions();
			if (this.getIsStartNode()) {
				obj = new Nodes();
			} else {
				obj = ens.GetHisFromNodes(this.getNodeID());
			}
			this.SetRefObject("HisFromNodes", obj);
		}
		return obj;
	}

	public final BillTemplates getBillTemplates() throws Exception {
		Object tempVar = this.GetRefObject("BillTemplates");
		BillTemplates obj = tempVar instanceof BillTemplates ? (BillTemplates) tempVar : null;
		if (obj == null) {
			obj = new BillTemplates(this.getNodeID());
			this.SetRefObject("BillTemplates", obj);
		}
		return obj;
	}

	public final NodeStations getNodeStations() throws Exception {
		Object tempVar = this.GetRefObject("NodeStations");
		NodeStations obj = tempVar instanceof NodeStations ? (NodeStations) tempVar : null;
		if (obj == null) {
			obj = new NodeStations(this.getNodeID());
			this.SetRefObject("NodeStations", obj);
		}
		return obj;
	}

	public final NodeDepts getNodeDepts() throws Exception {
		Object tempVar = this.GetRefObject("NodeDepts");
		NodeDepts obj = tempVar instanceof NodeDepts ? (NodeDepts) tempVar : null;
		if (obj == null) {
			obj = new NodeDepts(this.getNodeID());
			this.SetRefObject("NodeDepts", obj);
		}
		return obj;
	}

	public final NodeEmps getNodeEmps() throws Exception {
		Object tempVar = this.GetRefObject("NodeEmps");
		NodeEmps obj = tempVar instanceof NodeEmps ? (NodeEmps) tempVar : null;
		if (obj == null) {
			obj = new NodeEmps(this.getNodeID());
			this.SetRefObject("NodeEmps", obj);
		}
		return obj;
	}

	public final FrmNodes getFrmNodes() throws Exception {
		Object tempVar = this.GetRefObject("FrmNodes");
		FrmNodes obj = tempVar instanceof FrmNodes ? (FrmNodes) tempVar : null;
		if (obj == null) {
			obj = new FrmNodes(this.getFK_Flow(), this.getNodeID());
			this.SetRefObject("FrmNodes", obj);
		}
		return obj;
	}

	public final MapData getMapData() throws Exception {
		Object tempVar = this.GetRefObject("MapData");
		MapData obj = tempVar instanceof MapData ? (MapData) tempVar : null;
		if (obj == null) {
			obj = new MapData(this.getNodeFrmID());
			this.SetRefObject("MapData", obj);
		}
		return obj;
	}

	/**
	 * 节点事件
	 */
	public final FrmEvents getFrmEvents() throws Exception {
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = tempVar instanceof FrmEvents ? (FrmEvents) tempVar : null;
		if (obj == null) {
			obj = new FrmEvents(this.getNodeID());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}

	@Override
	public String getPK() {
		return "NodeID";
	}

	/**
	 * UI界面上的访问控制
	 * 
	 * @throws Exception
	 */
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin")) {
			uac.IsUpdate = true;
		}
		return uac;
	}

	/**
	 * 初试化全局的
	 * 
	 * @return
	 * @throws Exception
	 */
	public final NodePosType GetHisNodePosType() throws Exception {
		String nodeid = String.valueOf(this.getNodeID());
		if (nodeid.substring(nodeid.length() - 2).equals("01")) {
			return NodePosType.Start;
		}

		if (this.getFromNodes().size() == 0) {
			return NodePosType.Mid;
		}

		if (this.getHisToNodes().size() == 0) {
			return NodePosType.End;
		}
		return NodePosType.Mid;
	}

	public final FWCAth getFWCAth() throws Exception {
		return FWCAth.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCAth));
	}

	public final void setFWCAth(FWCAth value) throws Exception {
		this.SetValByKey(FrmWorkCheckAttr.FWCAth, value.getValue());
	}

	/**
	 * 检查流程，修复必要的计算字段信息.
	 * 
	 * @param fl
	 *            流程
	 * @return 返回检查信息
	 * @throws Exception
	 */
	public static String CheckFlow(Flow fl) throws Exception {
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		if (SystemConfig.getOSDBSrc() == OSDBSrc.Database) {
			// 删除必要的数据.
			DBAccess.RunSQL("DELETE FROM WF_NodeEmp WHERE FK_Emp  NOT IN (SELECT No from Port_Emp)");
			DBAccess.RunSQL("DELETE FROM WF_Emp WHERE NO NOT IN (SELECT No FROM Port_Emp )");
			DBAccess.RunSQL(
					"UPDATE WF_Emp SET Name=(SELECT Name From Port_Emp WHERE Port_Emp.No=WF_Emp.No),FK_Dept=(select FK_Dept from Port_Emp where Port_Emp.No=WF_Emp.No)");
		}

		Nodes nds = new Nodes();
		nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());

		if (nds.size() == 0) {
			return "流程[" + fl.getNo() + fl.getName() + "]中没有节点数据，您需要注册一下这个流程。";
		}

		// 更新是否是有完成条件的节点。
		BP.DA.DBAccess.RunSQL("UPDATE WF_Node SET IsCCFlow=0  WHERE FK_Flow='" + fl.getNo() + "'");
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_Direction WHERE Node=0 OR ToNode=0");
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_Direction WHERE Node  NOT IN (SELECT NODEID FROM WF_Node )");
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_Direction WHERE ToNode  NOT IN (SELECT NODEID FROM WF_Node) ");

		String sql = "";
		DataTable dt = null;

		// 单据信息，岗位，节点信息。
		for (Node nd : nds.ToJavaList()) {
			BP.Sys.MapData md = new BP.Sys.MapData();
			md.setNo("ND" + nd.getNodeID());
			if (md.getIsExits() == false) {
				nd.CreateMap();
			}

			// 工作岗位。
			sql = "SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nd.getNodeID();
			dt = DBAccess.RunSQLReturnTable(sql);
			String strs = "";
			for (DataRow dr : dt.Rows) {
				strs += "@" + dr.getValue(0).toString();
			}
			nd.setHisStas(strs);

			// 工作部门。
			sql = "SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + nd.getNodeID();
			dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows) {
				strs += "@" + dr.getValue(0).toString();
			}
			nd.setHisDeptStrs(strs);

			// 节点方向.
			strs = "";
			Directions dirs = new Directions(nd.getNodeID());
			for (Direction dir : dirs.ToJavaList()) {
				strs += "@" + dir.getToNode();
			}
			nd.setHisToNDs(strs);

			// 单据
			strs = "";
			BillTemplates temps = new BillTemplates(nd);
			for (BillTemplate temp : temps.ToJavaList()) {
				strs += "@" + temp.getNo();
			}
			nd.setHisBillIDs(strs);

			// 检查节点的位置属性。
			nd.setHisNodePosType(nd.GetHisNodePosType());
			try {
				nd.DirectUpdate();
			} catch (RuntimeException ex) {
				throw new RuntimeException("err@" + ex.getMessage() + " node=" + nd.getName());
			}
		}

		// 处理岗位分组.
		sql = "SELECT HisStas, COUNT(*) as NUM FROM WF_Node WHERE FK_Flow='" + fl.getNo() + "' GROUP BY HisStas";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows) {
			String stas = dr.getValue(0).toString();
			String nodes = "";
			for (Node nd : nds.ToJavaList()) {
				if (nd.getHisStas().equals(stas)) {
					nodes += "@" + nd.getNodeID();
				}
			}

			for (Node nd : nds.ToJavaList()) {
				if (nodes.contains("@" + String.valueOf(nd.getNodeID())) == false) {
					continue;
				}

				nd.setGroupStaNDs(nodes);
				nd.DirectUpdate();
			}
		}
		/* 判断流程的类型 */
		sql = "SELECT Name FROM WF_Node WHERE (NodeWorkType=" + NodeWorkType.StartWorkFL.getValue()
				+ " OR NodeWorkType=" + NodeWorkType.WorkFHL.getValue() + " OR NodeWorkType="
				+ NodeWorkType.WorkFL.getValue() + " OR NodeWorkType=" + NodeWorkType.WorkHL.getValue()
				+ ") AND (FK_Flow='" + fl.getNo() + "')";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		fl.DirectUpdate();
		return null;
	}

	@Override
	protected boolean beforeUpdate() throws Exception {
		if (this.getIsStartNode()) {
			this.SetValByKey(BtnAttr.ReturnRole, ReturnRole.CanNotReturn.getValue());
			this.SetValByKey(BtnAttr.ShiftEnable, 0);
			this.SetValByKey(BtnAttr.EndFlowEnable, 0);
		}

		// 给icon设置默认值.
		if (this.GetValStrByKey(NodeAttr.ICON).equals("")) {
			this.setICON("审核.png");
		}

		/// #region 如果是数据合并模式，就要检查节点中是否有子线程，如果有子线程就需要单独的表.
		if (this.getHisRunModel() == RunModel.SubThread) {
			MapData md = new MapData("ND" + this.getNodeID());
			if (!md.getPTable().equals("ND" + this.getNodeID())) {
				md.setPTable("ND" + this.getNodeID());
				md.Update();
			}
		}

		/// #endregion 如果是数据合并模式，就要检查节点中是否有子线程，如果有子线程就需要单独的表.

		// 更新版本号.
		Flow.UpdateVer(this.getFK_Flow());

		/// #region 更新流程判断条件的标记。
		DBAccess.RunSQL("UPDATE WF_Node SET IsCCFlow=0  WHERE FK_Flow='" + this.getFK_Flow() + "'");
		DBAccess.RunSQL(
				"UPDATE WF_Node SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond WHERE CondType=1) AND FK_Flow='"
						+ this.getFK_Flow() + "'");

		/// #endregion

		Flow fl = new Flow(this.getFK_Flow());

		Node.CheckFlow(fl);
		this.setFlowName(fl.getName());

		DBAccess.RunSQL("UPDATE Sys_MapData SET Name='" + this.getName() + "' WHERE No='ND" + this.getNodeID() + "' AND Name=''");
		switch (this.getHisRunModel()) {
		case Ordinary:
			if (this.getIsStartNode()) {
				this.setHisNodeWorkType(NodeWorkType.StartWork);
			} else {
				this.setHisNodeWorkType(NodeWorkType.Work);
			}
			break;
		case FL:
			if (this.getIsStartNode()) {
				this.setHisNodeWorkType(NodeWorkType.StartWorkFL);
			} else {
				this.setHisNodeWorkType(NodeWorkType.WorkFL);
			}
			break;
		case HL:
			break;
		case FHL:
			break;
		case SubThread:
			this.setHisNodeWorkType(NodeWorkType.SubThreadWork);
			break;
		default:
			throw new RuntimeException("eeeee");
		}
		// 创建审核组件附件
		FrmAttachment workCheckAth = new FrmAttachment();
		boolean isHave = workCheckAth.RetrieveByAttr(FrmAttachmentAttr.MyPK, "ND" + this.getNodeID() + "_FrmWorkCheck");
		// 不包含审核组件
		if (isHave == false && this.getFWCAth() == FWCAth.MinAth) {
			workCheckAth = new FrmAttachment();
			/* 如果没有查询到它,就有可能是没有创建. */
			workCheckAth.setMyPK("ND" + this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setFK_MapData("ND" + String.valueOf(this.getNodeID()));
			workCheckAth.setNoOfObj("FrmWorkCheck");
			workCheckAth.setExts("*.*");

			// 存储路径.
			workCheckAth.setSaveTo("/DataUser/UploadFile/");
			workCheckAth.setIsNote(false); // 不显示note字段.
			workCheckAth.setIsVisable(false); // 让其在form 上不可见.

			// 位置.
			workCheckAth.setX((float) 94.09);
			workCheckAth.setY((float) 333.18);
			workCheckAth.setW((float) 626.36);
			workCheckAth.setH((float) 150);

			// 多附件.
			workCheckAth.setUploadType(AttachmentUploadType.Multi);
			workCheckAth.setName("审核组件");
			workCheckAth.SetValByKey("AtPara",
					"@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=");
			workCheckAth.Insert();
		}
		return super.beforeUpdate();
	}

	/**
	 * 清空WFEmp中的StartFlows文件
	 * 
	 * @throws Exception
	 */
	@Override
	protected void afterInsertUpdateAction() throws Exception {
		if (this.getIsStartNode() == true) {
			DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
		}
		super.afterInsertUpdateAction();
	}

	/// #endregion

	/// #region 基本属性
	/**
	 * 是否启动自动运行？
	 * 
	 * @throws Exception
	 */
	public final boolean getAutoRunEnable() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.AutoRunEnable);
	}

	public final void setAutoRunEnable(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.AutoRunEnable, value);
	}

	/**
	 * 自动运行参数
	 */
	public final String getAutoRunParas() throws Exception {
		return this.GetValStringByKey(NodeAttr.AutoRunParas);
	}

	public final void setAutoRunParas(String value) throws Exception {
		this.SetValByKey(NodeAttr.AutoRunParas, value);
	}

	/**
	 * 审核组件
	 */
	public final BP.WF.Template.FrmWorkCheckSta getFrmWorkCheckSta() throws Exception {
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(NodeAttr.FWCSta));
	}

	public final void setFrmWorkCheckSta(BP.WF.Template.FrmWorkCheckSta value) throws Exception {
		this.SetValByKey(NodeAttr.FWCSta, value.getValue());
	}

	/**
	 * 审核组件版本
	 */
	public final int getFWCVer() throws Exception {
		return this.GetValIntByKey(FrmWorkCheckAttr.FWCVer, 0);
	}

	public final void setFWCVer(int value) throws Exception {
		this.SetValByKey(FrmWorkCheckAttr.FWCVer, value);
	}

	/**
	 * 内部编号
	 * 
	 * @throws Exception
	 */
	public final String getNo() throws Exception {
		try {
			return String.valueOf(this.getNodeID()).substring(String.valueOf(this.getNodeID()).length() - 2);
		} catch (RuntimeException ex) {
			Log.DefaultLogWriteLineInfo(ex.getMessage() + " - " + this.getNodeID());
			throw new RuntimeException("@没有获取到它的NodeID = " + this.getNodeID());
		}
	}

	/**
	 * 自动跳转规则0-处理人就是提交人
	 */
	public final boolean getAutoJumpRole0() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole0);
	}

	public final void setAutoJumpRole0(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.AutoJumpRole0, value);
	}

	/**
	 * 自动跳转规则1-处理人已经出现过
	 */
	public final boolean getAutoJumpRole1() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole1);
	}

	public final void setAutoJumpRole1(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.AutoJumpRole1, value);
	}

	/**
	 * 自动跳转规则2-处理人与上一步相同
	 */
	public final boolean getAutoJumpRole2() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole2);
	}

	public final void setAutoJumpRole2(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.AutoJumpRole2, value);
	}

	/**
	 * 启动参数
	 */
	public final String getSubFlowStartParas() throws Exception {
		return this.GetValStringByKey(NodeAttr.SubFlowStartParas);
	}

	public final void setSubFlowStartParas(String value) throws Exception {
		this.SetValByKey(NodeAttr.SubFlowStartParas, value);
	}

	/**
	 * 子线程启动方式
	 */
	public final SubFlowStartWay getSubFlowStartWay() throws Exception {
		return SubFlowStartWay.forValue(this.GetValIntByKey(NodeAttr.SubFlowStartWay));
	}

	public final void setSubFlowStartWay(SubFlowStartWay value) throws Exception {
		this.SetValByKey(NodeAttr.SubFlowStartWay, value.getValue());
	}

	public final NodeFormType getHisFormType() throws Exception {
		return NodeFormType.forValue(this.GetValIntByKey(NodeAttr.FormType));
	}

	public final void setHisFormType(NodeFormType value) throws Exception {
		this.SetValByKey(NodeAttr.FormType, value.getValue());
	}

	public final String getHisFormTypeText() throws Exception {
		if (this.getHisFormType() == NodeFormType.DisableIt) {
			return "树表单";
		}

		if (this.getHisFormType() == NodeFormType.ExcelForm) {
			return "Excel表单";
		}

		if (this.getHisFormType() == NodeFormType.FoolForm) {
			return "傻瓜表单";
		}

		if (this.getHisFormType() == NodeFormType.FoolTruck) {
			return "傻瓜轨迹表单";
		}

		if (this.getHisFormType() == NodeFormType.FreeForm) {
			return "自由表单";
		}

		if (this.getHisFormType() == NodeFormType.SDKForm) {
			return "SDK表单";
		}

		if (this.getHisFormType() == NodeFormType.SelfForm) {
			return "自定义表单";
		}

		if (this.getHisFormType() == NodeFormType.SheetAutoTree) {
			return "动态表单树";
		}

		return "未知";
	}

	/**
	 * 节点编号
	 */
	public final int getNodeID() throws Exception {
		return this.GetValIntByKey(NodeAttr.NodeID);
	}

	public final void setNodeID(int value) throws Exception {
		this.SetValByKey(NodeAttr.NodeID, value);
	}

	public final boolean getIsEnableTaskPool() throws Exception {
		if (this.getTodolistModel() == TodolistModel.Sharing) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 节点头像
	 */
	public final String getICON() throws Exception {
		String s = this.GetValStrByKey(NodeAttr.ICON);
		if (DataType.IsNullOrEmpty(s)) {
			if (this.getIsStartNode()) {
				return "审核.png";
			} else {
				return "前台.png";
			}
		}
		return s;
	}

	public final void setICON(String value) throws Exception {
		this.SetValByKey(NodeAttr.ICON, value);
	}

	/**
	 * FormUrl
	 * 
	 * @throws Exception
	 */
	public final String getFormUrl() throws Exception {
		String str = this.GetValStrByKey(NodeAttr.FormUrl);
		str = str.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());
		return str;
	}

	public final void setFormUrl(String value) throws Exception {
		this.SetValByKey(NodeAttr.FormUrl, value);
	}

	/**
	 * 表单类型
	 */
	public final NodeFormType getFormType() throws Exception {
		return NodeFormType.forValue(this.GetValIntByKey(NodeAttr.FormType));
	}

	public final void setFormType(NodeFormType value) throws Exception {
		this.SetValByKey(NodeAttr.FormType, value.getValue());
	}

	/**
	 * 名称
	 */
	public final String getName() throws Exception {
		return this.GetValStrByKey(EntityOIDNameAttr.Name);
	}

	public final void setName(String value) throws Exception {
		this.SetValByKey(EntityOIDNameAttr.Name, value);
	}

	/**
	 * 限期天
	 */
	public final int getTimeLimit() throws Exception {
		return this.GetValIntByKey(NodeAttr.TimeLimit);
	}

	public final void setTimeLimit(int value) throws Exception {
		this.SetValByKey(NodeAttr.TimeLimit, value);
	}

	/**
	 * 限期小时
	 */
	public final int getTimeLimitHH() throws Exception {
		return this.GetParaInt("TimeLimitHH", 0);
	}

	public final void setTimeLimitHH(int value) throws Exception {
		this.SetPara("TimeLimitHH", value);
	}

	/**
	 * 限期分钟
	 */
	public final int getTimeLimitMM() throws Exception {
		return this.GetParaInt("TimeLimitMM", 0);
	}

	public final void setTimeLimitMM(int value) throws Exception {
		this.SetPara("TimeLimitMM", value);
	}

	/**
	 * 逾期提醒规则
	 */
	public final CHAlertRole getTAlertRole() throws Exception {
		return CHAlertRole.forValue(this.GetValIntByKey(NodeAttr.TAlertRole));
	}

	public final void setTAlertRole(CHAlertRole value) throws Exception {
		this.SetValByKey(NodeAttr.TAlertRole, value.getValue());
	}

	/**
	 * 时间计算方式
	 */
	public final TWay getTWay() throws Exception {
		return TWay.forValue(this.GetValIntByKey(NodeAttr.TWay));
	}

	public final void setTWay(TWay value) throws Exception {
		this.SetValByKey(NodeAttr.TWay, value.getValue());
	}

	/**
	 * 逾期 - 提醒方式
	 */
	public final CHAlertWay getTAlertWay() throws Exception {
		return CHAlertWay.forValue(this.GetValIntByKey(NodeAttr.TAlertWay));
	}

	public final void setTAlertWay(CHAlertWay value) throws Exception {
		this.SetValByKey(NodeAttr.TAlertWay, value.getValue());
	}

	/**
	 * 预警天
	 */
	public final int getWarningDay() throws Exception {
		int i = this.GetValIntByKey(NodeAttr.WarningDay);
		if (i == 0) {
			return 1;
		}
		return i;
	}

	public final void setWarningDay(int value) throws Exception {
		this.SetValByKey(NodeAttr.WarningDay, value);
	}

	/**
	 * 预警 - 提醒规则
	 */
	public final CHAlertRole getWAlertRole() throws Exception {
		return CHAlertRole.forValue(this.GetValIntByKey(NodeAttr.WAlertRole));
	}

	public final void setWAlertRole(CHAlertRole value) throws Exception {
		this.SetValByKey(NodeAttr.WAlertRole, value.getValue());
	}

	/**
	 * 预警 - 提醒方式
	 */
	public final CHAlertWay getWAlertWay() throws Exception {
		return CHAlertWay.forValue(this.GetValIntByKey(NodeAttr.WAlertWay));
	}

	public final void setWAlertWay(CHAlertWay value) throws Exception {
		this.SetValByKey(NodeAttr.WAlertWay, value.getValue());
	}

	/**
	 * 保存方式 @0=仅节点表 @1=节点与NDxxxRtp表.
	 */
	public final SaveModel getSaveModel() throws Exception {
		return SaveModel.forValue(this.GetValIntByKey(NodeAttr.SaveModel));
	}

	public final void setSaveModel(SaveModel value) throws Exception {
		this.SetValByKey(NodeAttr.SaveModel, value.getValue());
	}

	/**
	 * 流程步骤
	 */
	public final int getStep() throws Exception {
		return this.GetValIntByKey(NodeAttr.Step);
	}

	public final void setStep(int value) throws Exception {
		this.SetValByKey(NodeAttr.Step, value);
	}

	/**
	 * 扣分率（分/天）
	 */
	public final float getTCent() throws Exception {
		return this.GetValFloatByKey(NodeAttr.TCent);
	}

	public final void setTCent(float value) throws Exception {
		this.SetValByKey(NodeAttr.TCent, value);
	}

	/**
	 * 是否是客户执行节点？
	 */
	public final boolean getIsGuestNode() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsGuestNode);
	}

	public final void setIsGuestNode(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsGuestNode, value);
	}

	/**
	 * 是否启用子流程运行当前节点，主流程自动运行到下一节点
	 */
	public final boolean getIsToParentNextNode() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsToParentNextNode);
	}

	public final void setIsToParentNextNode(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsToParentNextNode, value);
	}

	/**
	 * 是否是开始节点
	 * 
	 * @throws Exception
	 */
	public final boolean getIsStartNode() throws Exception {
		if (this.getNo().equals("01")) {
			return true;
		}
		return false;

	}

	/**
	 * x
	 * 
	 * @throws Exception
	 */
	public final int getX() throws Exception {
		return this.GetValIntByKey(NodeAttr.X);
	}

	public final void setX(int value) throws Exception {
		if (value <= 0) {
			this.SetValByKey(NodeAttr.X, 5);
		} else {
			this.SetValByKey(NodeAttr.X, value);
		}
	}

	/**
	 * y
	 */
	public final int getY() throws Exception {
		return this.GetValIntByKey(NodeAttr.Y);
	}

	public final void setY(int value) throws Exception {
		if (value <= 0) {
			this.SetValByKey(NodeAttr.Y, 5);
		} else {
			this.SetValByKey(NodeAttr.Y, value);
		}
	}

	/**
	 * 水执行它？
	 */
	public final int getWhoExeIt() throws Exception {
		return this.GetValIntByKey(NodeAttr.WhoExeIt);
	}

	public final void setWhoExeIt(int value) throws Exception {
		this.SetValByKey(NodeAttr.WhoExeIt, value);
	}

	/**
	 * 位置
	 */
	public final NodePosType getNodePosType() throws Exception {
		return NodePosType.forValue(this.GetValIntByKey(NodeAttr.NodePosType));
	}

	public final void setNodePosType(NodePosType value) throws Exception {
		this.SetValByKey(NodeAttr.NodePosType, value.getValue());
	}

	/**
	 * 运行模式
	 */
	public final RunModel getHisRunModel() throws Exception {
		return RunModel.forValue(this.GetValIntByKey(NodeAttr.RunModel));
	}

	public final void setHisRunModel(RunModel value) throws Exception {
		this.SetValByKey(NodeAttr.RunModel, value.getValue());
	}

	/**
	 * 操纵提示
	 */
	public final String getTip() throws Exception {
		return this.GetValStrByKey(NodeAttr.Tip);
	}

	public final void setTip(String value) throws Exception {
		this.SetValByKey(NodeAttr.Tip, value);
	}

	/**
	 * 焦点字段
	 */
	public final String getFocusField() throws Exception {
		return this.GetValStrByKey(NodeAttr.FocusField);
	}

	public final void setFocusField(String value) throws Exception {
		this.SetValByKey(NodeAttr.FocusField, value);
	}

	/**
	 * 被退回节点退回信息地步.
	 */
	public final String getReturnAlert() throws Exception {
		return this.GetValStrByKey(NodeAttr.ReturnAlert);
	}

	public final void setReturnAlert(String value) throws Exception {
		this.SetValByKey(NodeAttr.ReturnAlert, value);
	}

	/**
	 * 退回原因
	 */
	public final String getReturnReasonsItems() throws Exception {
		return this.GetValStrByKey(NodeAttr.ReturnReasonsItems);
	}

	/**
	 * 节点的事务编号
	 */
	public final String getFK_Flow() throws Exception {
		return this.GetValStrByKey(NodeAttr.FK_Flow);
	}

	public final void setFK_Flow(String value) throws Exception {
		SetValByKey(NodeAttr.FK_Flow, value);
	}

	/**
	 * 获取它的上一步的分流点
	 */
	private Node _GetHisPriFLNode(Nodes nds) throws Exception {
		for (Node mynd : nds.ToJavaList()) {
			if (mynd.getIsFL()) {
				return mynd;
			} else {
				return _GetHisPriFLNode(mynd.getFromNodes());
			}
		}
		return null;
	}

	/**
	 * 它的上一步分流节点
	 */
	public final Node getHisPriFLNode() throws Exception {
		return _GetHisPriFLNode(this.getFromNodes());
	}

	public final String getTurnToDealDoc() throws Exception {
		String s = this.GetValStrByKey(NodeAttr.TurnToDealDoc);
		if (this.getHisTurnToDeal() == TurnToDeal.SpecUrl) {
			if (s.contains("1=1")) {
				return s;
			}
			if (s.contains("?")) {
				s += "&1=1";
			} else {
				s += "?1=1";
			}
		}
		s = s.replace("~", "'");
		return s;
	}

	public final void setTurnToDealDoc(String value) throws Exception {
		SetValByKey(NodeAttr.TurnToDealDoc, value);
	}

	/**
	 * 可跳转的节点
	 */
	public final String getJumpToNodes() throws Exception {
		return this.GetValStrByKey(NodeAttr.JumpToNodes);
	}

	public final void setJumpToNodes(String value) throws Exception {
		SetValByKey(NodeAttr.JumpToNodes, value);
	}

	/**
	 * 工作ID
	 */
	public long WorkID = 0;

	/**
	 * 节点表单ID
	 */
	public final String getNodeFrmID() throws Exception {
		String str = this.GetValStrByKey(NodeAttr.NodeFrmID);
		if (DataType.IsNullOrEmpty(str) == true) {
			return "ND" + this.getNodeID();
		}

		if (this.getHisFormType() == NodeFormType.FoolTruck || this.getHisFormType() == NodeFormType.SheetTree) {
			return "ND" + this.getNodeID();
		}

		if (str.equals("Pri") == true
				&& (this.getHisFormType() == NodeFormType.FoolForm || this.getHisFormType() == NodeFormType.FreeForm)) {
			if (this.WorkID == 0) {
				return "ND" + this.getNodeID();
			}
			// throw new Exception("err@获得当前节点的上一个节点表单出现错误,没有给参数WorkID赋值.");

			/* 要引擎上一个节点表单 */
			String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A, WF_Node B ";
			sql += " WHERE A.NDFrom=B.NodeID AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType="
					+ ActionType.Start.getValue() + ")  ";
			sql += "  AND (FormType=0 OR FormType=1) ";

			sql += "  AND (A.WorkID=" + this.WorkID + ") ";

			sql += " ORDER BY A.RDT ";

			int nodeID = DBAccess.RunSQLReturnValInt(sql, 0);
			if (nodeID == 0) {
				throw new RuntimeException("err@没有找到指定的节点.");
			}

			return "ND" + nodeID;
		}
		return str;
	}

	public final void setNodeFrmID(String value) throws Exception {
		SetValByKey(NodeAttr.NodeFrmID, value);
	}

	/**
	 * 要启动的子流程
	 */
	public final String getSFActiveFlows() throws Exception {
		return this.GetValStrByKey(NodeAttr.SFActiveFlows);
	}

	public final void setSFActiveFlows(String value) throws Exception {
		SetValByKey(NodeAttr.SFActiveFlows, value);
	}

	public final String getFlowName() throws Exception {
		return this.GetValStrByKey(NodeAttr.FlowName);
	}

	public final void setFlowName(String value) throws Exception {
		SetValByKey(NodeAttr.FlowName, value);
	}

	/**
	 * 打印方式
	 */
	public final boolean getHisPrintDocEnable() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.PrintDocEnable);
	}

	public final void setHisPrintDocEnable(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.PrintDocEnable, value);
	}

	/**
	 * PDF打印规则
	 */
	public final int getHisPrintPDFModle() throws Exception {
		return this.GetValIntByKey(BtnAttr.PrintPDFModle);
	}

	public final void setHisPrintPDFModle(int value) throws Exception {
		this.SetValByKey(BtnAttr.PrintPDFModle, value);
	}

	/**
	 * 打印水印设置规则
	 */
	public final String getShuiYinModle() throws Exception {
		return this.GetValStringByKey(BtnAttr.ShuiYinModle);
	}

	public final void setShuiYinModle(String value) throws Exception {
		this.SetValByKey(BtnAttr.ShuiYinModle, value);
	}

	/**
	 * 批处理规则
	 * 
	 * @throws Exception
	 */
	public final BatchRole getHisBatchRole() throws Exception {
		return BatchRole.forValue(this.GetValIntByKey(NodeAttr.BatchRole));
	}

	public final void setHisBatchRole(BatchRole value) throws Exception {
		this.SetValByKey(NodeAttr.BatchRole, value.getValue());
	}

	/**
	 * 批量处理规则
	 * 
	 * @throws Exception
	 * @显示的字段.
	 */
	public final String getBatchParas() throws Exception {
		String str = this.GetValStringByKey(NodeAttr.BatchParas);

		// 替换约定的URL.
		str = str.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());
		return str;
	}

	public final void setBatchParas(String value) throws Exception {
		this.SetValByKey(NodeAttr.BatchParas, value);
	}

	/**
	 * 是否是自定义的url,处理批处理.
	 */
	public final boolean getBatchParas_IsSelfUrl() throws Exception {
		if (this.getBatchParas().contains(".aspx") || this.getBatchParas().contains(".jsp")
				|| this.getBatchParas().contains(".htm") || this.getBatchParas().contains("http:")) {
			return true;
		}
		return false;
	}

	/**
	 * 批量审核数量
	 */
	public final int getBatchListCount() throws Exception {
		return this.GetValIntByKey(NodeAttr.BatchListCount);
	}

	public final void setBatchListCount(int value) throws Exception {
		this.SetValByKey(NodeAttr.BatchListCount, value);
	}

	public final String getPTable() throws Exception {

		return "ND" + this.getNodeID();
	}

	public final void setPTable(String value) throws Exception {
		SetValByKey(NodeAttr.PTable, value);
	}

	/**
	 * Doc
	 */
	public final String getDoc() throws Exception {
		return this.GetValStrByKey(NodeAttr.Doc);
	}

	public final void setDoc(String value) throws Exception {
		SetValByKey(NodeAttr.Doc, value);
	}

	public final String getGroupStaNDs() throws Exception {
		return this.GetValStrByKey(NodeAttr.GroupStaNDs);
	}

	public final void setGroupStaNDs(String value) throws Exception {
		this.SetValByKey(NodeAttr.GroupStaNDs, value);
	}

	/**
	 * 到达的节点数量.
	 * 
	 * @throws Exception
	 */
	public final int getHisToNDNum() throws Exception {
		String[] strs = this.getHisToNDs().split("[@]", -1);
		return strs.length - 1;
	}

	public final String getHisToNDs() throws Exception {
		return this.GetValStrByKey(NodeAttr.HisToNDs);
	}

	public final void setHisToNDs(String value) throws Exception {
		this.SetValByKey(NodeAttr.HisToNDs, value);
	}

	public final String getHisDeptStrs() throws Exception {
		return this.GetValStrByKey(NodeAttr.HisDeptStrs);
	}

	public final void setHisDeptStrs(String value) throws Exception {
		this.SetValByKey(NodeAttr.HisDeptStrs, value);
	}

	public final String getHisStas() throws Exception {
		return this.GetValStrByKey(NodeAttr.HisStas);
	}

	public final void setHisStas(String value) throws Exception {
		this.SetValByKey(NodeAttr.HisStas, value);
	}

	/**
	 * 单据IDs
	 */
	public final String getHisBillIDs() throws Exception {
		return this.GetValStrByKey(NodeAttr.HisBillIDs);
	}

	public final void setHisBillIDs(String value) throws Exception {
		this.SetValByKey(NodeAttr.HisBillIDs, value);
	}

	public final String getReturnField() throws Exception {
		return this.GetValStrByKey(BtnAttr.ReturnField);
	}

	public final void setReturnField(String value) throws Exception {
		this.SetValByKey(BtnAttr.ReturnField, value);
	}

	/**
	 * 单节点退回规则
	 */
	public final int getReturnOneNodeRole() throws Exception {
		return this.GetValIntByKey(NodeAttr.ReturnOneNodeRole);
	}

	public final void setReturnOneNodeRole(int value) throws Exception {
		this.SetValByKey(NodeAttr.ReturnOneNodeRole, value);
	}

	/**
	 * 是不是多岗位工作节点.
	 */
	public final boolean getIsMultiStations() throws Exception {
		if (this.getNodeStations().size() > 1) {
			return true;
		}
		return false;
	}

	public final String getHisStationsStr() throws Exception {
		String s = "";
		for (NodeStation ns : this.getNodeStations().ToJavaList()) {
			s += ns.getFK_StationT() + ",";
		}
		return s;
	}

	/**
	 * 得到一个工作data实体
	 * 
	 * @param workId
	 *            工作ID
	 * @return 如果没有就返回null
	 */
	public final Work GetWork(long workId) throws Exception {
		Work wk = this.getHisWork();
		wk.SetValByKey("OID", workId);
		if (wk.RetrieveFromDBSources() == 0)
			return null;
		else
			return wk;
	}

	/**
	 * 转向处理
	 */
	public final TurnToDeal getHisTurnToDeal() throws Exception {
		return TurnToDeal.forValue(this.GetValIntByKey(NodeAttr.TurnToDeal));
	}

	public final void setHisTurnToDeal(TurnToDeal value) throws Exception {
		this.SetValByKey(NodeAttr.TurnToDeal, value.getValue());
	}

	/**
	 * 访问规则
	 */
	public final DeliveryWay getHisDeliveryWay() throws Exception {
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}

	public final void setHisDeliveryWay(DeliveryWay value) throws Exception {
		this.SetValByKey(NodeAttr.DeliveryWay, value.getValue());
	}

	/**
	 * 访问规则Text
	 */
	public final String getHisDeliveryWayText() throws Exception {
		SysEnum se = new SysEnum("DeliveryWay", this.getHisDeliveryWay().getValue());
		return se.getLab();
	}

	/**
	 * 考核规则
	 */
	public final CHWay getHisCHWay() throws Exception {
		return CHWay.forValue(this.GetValIntByKey(NodeAttr.CHWay));
	}

	public final void setHisCHWay(CHWay value) throws Exception {
		this.SetValByKey(NodeAttr.CHWay, value.getValue());
	}

	/**
	 * 抄送规则
	 */
	public final CCRole getHisCCRole() throws Exception {
		return CCRole.forValue(this.GetValIntByKey(NodeAttr.CCRole));
	}

	public final void setHisCCRole(CCRole value) throws Exception {
		this.SetValByKey(BtnAttr.CCRole, value.getValue());
	}

	public final String getHisCCRoleText() throws Exception {
		SysEnum se = new SysEnum(NodeAttr.CCRole, this.getHisCCRole().getValue());
		return se.getLab();
	}

	/**
	 * 删除流程规则
	 */
	public final DelWorkFlowRole getHisDelWorkFlowRole() throws Exception {
		return DelWorkFlowRole.forValue(this.GetValIntByKey(BtnAttr.DelEnable));
	}

	/**
	 * 未找到处理人时是否跳转.
	 */
	public final boolean getHisWhenNoWorker() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.WhenNoWorker);
	}

	public final void setHisWhenNoWorker(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.WhenNoWorker, value);
	}

	/**
	 * 撤销规则
	 */
	public final CancelRole getHisCancelRole() throws Exception {
		return CancelRole.forValue(this.GetValIntByKey(NodeAttr.CancelRole));
	}

	public final void setHisCancelRole(CancelRole value) throws Exception {
		this.SetValByKey(NodeAttr.CancelRole, value.getValue());
	}

	/**
	 * 对方已读不能撤销
	 */
	public final boolean getCancelDisWhenRead() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.CancelDisWhenRead);
	}

	public final void setCancelDisWhenRead(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.CancelDisWhenRead, value);
	}

	/**
	 * 数据写入规则
	 */
	public final CCWriteTo getCCWriteTo() throws Exception {
		return CCWriteTo.forValue(this.GetValIntByKey(NodeAttr.CCWriteTo));
	}

	public final void setCCWriteTo(CCWriteTo value) throws Exception {
		this.SetValByKey(NodeAttr.CCWriteTo, value.getValue());
	}

	/**
	 * Int type
	 */
	public final NodeWorkType getHisNodeWorkType() throws Exception {
		/// #warning 2012-01-24修订,没有自动计算出来属性。
		switch (this.getHisRunModel()) {
		case Ordinary:
			if (this.getIsStartNode()) {
				return NodeWorkType.StartWork;
			} else {
				return NodeWorkType.Work;
			}
		case FL:
			if (this.getIsStartNode()) {
				return NodeWorkType.StartWorkFL;
			} else {
				return NodeWorkType.WorkFL;
			}
		case HL:
			return NodeWorkType.WorkHL;
		case FHL:
			return NodeWorkType.WorkFHL;
		case SubThread:
			return NodeWorkType.SubThreadWork;
		default:
			throw new RuntimeException("@没有判断类型NodeWorkType.");
		}
	}

	public final void setHisNodeWorkType(NodeWorkType value) throws Exception {
		this.SetValByKey(NodeAttr.NodeWorkType, value.getValue());
	}

	public final String getHisRunModelT() throws Exception {
		SysEnum se = new SysEnum(NodeAttr.RunModel, this.getHisRunModel().getValue());
		return se.getLab();
	}

	/// #endregion

	/// #region 推算属性 (对于节点位置的判断)
	/**
	 * 类型
	 * 
	 * @throws Exception
	 */
	public final NodePosType getHisNodePosType() throws Exception {
		if (SystemConfig.getIsDebug()) {
			this.SetValByKey(NodeAttr.NodePosType, this.GetHisNodePosType().getValue());
			return this.GetHisNodePosType();
		}
		return NodePosType.forValue(this.GetValIntByKey(NodeAttr.NodePosType));
	}

	public final void setHisNodePosType(NodePosType value) throws Exception {
		if (value == NodePosType.Start) {
			if (!this.getNo().equals("01")) {
				value = NodePosType.Mid;
			}
		}

		this.SetValByKey(NodeAttr.NodePosType, value.getValue());
	}

	/**
	 * 是不是结束节点
	 */
	public final boolean getIsEndNode() throws Exception {
		if (this.getHisNodePosType() == NodePosType.End) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否允许子线程接受人员重复(对子线程点有效)?
	 */
	public final boolean getIsAllowRepeatEmps() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsAllowRepeatEmps);
	}

	public final void setIsAllowRepeatEmps(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsAllowRepeatEmps, value);
	}

	/**
	 * 是否启用发送短信？
	 * 
	 * @throws Exception
	 */
	public final boolean getIsEnableSMSMessage() throws Exception {
		int i = BP.DA.DBAccess.RunSQLReturnValInt("SELECT SMSEnable FROM Sys_FrmEvent WHERE FK_MapData='ND"
				+ this.getNodeID() + "' AND FK_Event='SendSuccess'", 0);
		if (i == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 是否可以在退回后原路返回？
	 */
	public final boolean getIsBackTracking() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsBackTracking);
	}

	/**
	 * 是否启用自动记忆功能
	 */
	public final boolean getIsRememberMe() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsRM);
	}

	public final void setIsRememberMe(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsRM, value);
	}

	/**
	 * 是否打开即审批
	 */
	public final boolean getIsOpenOver() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsOpenOver);
	}

	public final void setIsOpenOver(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsOpenOver, value);
	}

	/**
	 * 是否可以删除
	 */
	public final boolean getIsCanDelFlow() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsCanDelFlow);
	}

	/**
	 * 普通工作节点处理模式
	 */
	public final TodolistModel getTodolistModel() throws Exception {
		if (this.getIsStartNode() == true) {
			return TodolistModel.QiangBan;
		}

		return TodolistModel.forValue(this.GetValIntByKey(NodeAttr.TodolistModel));
	}

	public final void setTodolistModel(TodolistModel value) throws Exception {
		this.SetValByKey(NodeAttr.TodolistModel, value.getValue());
	}

	/**
	 * 组长确认规则
	 */
	public final TeamLeaderConfirmRole getTeamLeaderConfirmRole() throws Exception {
		return TeamLeaderConfirmRole.forValue(this.GetValIntByKey(NodeAttr.TeamLeaderConfirmRole));
	}

	public final void setTeamLeaderConfirmRole(TeamLeaderConfirmRole value) throws Exception {
		this.SetValByKey(NodeAttr.TeamLeaderConfirmRole, value.getValue());
	}

	/**
	 * 组长确认规则内容.
	 */
	public final String getTeamLeaderConfirmDoc() throws Exception {
		return this.GetValStringByKey(NodeAttr.TeamLeaderConfirmDoc);
	}

	public final void setTeamLeaderConfirmDoc(String value) throws Exception {
		this.SetValByKey(NodeAttr.TeamLeaderConfirmDoc, value);
	}

	public final HuiQianLeaderRole getHuiQianLeaderRole() throws Exception {
		return HuiQianLeaderRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianLeaderRole));
	}

	public final void setHuiQianLeaderRole(HuiQianLeaderRole value) throws Exception {
		this.SetValByKey(BtnAttr.HuiQianLeaderRole, value.getValue());
	}

	/**
	 * 阻塞模式
	 */
	public final BlockModel getBlockModel() throws Exception {
		return BlockModel.forValue(this.GetValIntByKey(NodeAttr.BlockModel));
	}

	public final void setBlockModel(BlockModel value) throws Exception {
		this.SetValByKey(NodeAttr.BlockModel, value.getValue());
	}

	/**
	 * 阻塞的表达式
	 */
	public final String getBlockExp() throws Exception {
		String str = this.GetValStringByKey(NodeAttr.BlockExp);

		if (DataType.IsNullOrEmpty(str)) {
			if (this.getBlockModel() == BlockModel.CurrNodeAll) {
				return "还有子流程没有完成您不能提交,需要等到所有的子流程完成后您才能发送.";
			}

			if (this.getBlockModel() == BlockModel.SpecSubFlow) {
				return "还有子流程没有完成您不能提交,需要等到所有的子流程完成后您才能发送.";
			}
		}
		return str;
	}

	public final void setBlockExp(String value) throws Exception {
		this.SetValByKey(NodeAttr.BlockExp, value);
	}

	/**
	 * 被阻塞时提示信息
	 */
	public final String getBlockAlert() throws Exception {
		return this.GetValStringByKey(NodeAttr.BlockAlert);
	}

	public final void setBlockAlert(String value) throws Exception {
		this.SetValByKey(NodeAttr.BlockAlert, value);
	}

	/**
	 * 子线程删除规则
	 */
	public final ThreadKillRole getThreadKillRole() throws Exception {
		return ThreadKillRole.forValue(this.GetValIntByKey(NodeAttr.ThreadKillRole));
	}

	/**
	 * 完成通过率
	 * 
	 * @throws Exception
	 */
	public final BigDecimal getPassRate() throws Exception {
		BigDecimal val = this.GetValDecimalByKey(NodeAttr.PassRate);
		if (val.intValue() == 0) {
			return new BigDecimal(100);
		}
		return val;
	}

	/**
	 * 是否允许分配工作
	 */
	public final boolean getIsTask() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsTask);
	}

	public final void setIsTask(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsTask, value);
	}

	/**
	 * 是否是业务单元
	 */
	public final boolean getIsBUnit() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsBUnit);
	}

	public final void setIsBUnit(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsBUnit, value);
	}

	/**
	 * 是否可以移交
	 */
	public final boolean getIsHandOver() throws Exception {
		if (this.getIsStartNode()) {
			return false;
		}

		return this.GetValBooleanByKey(NodeAttr.IsHandOver);
	}

	public final void setIsHandOver(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsHandOver, value);
	}

	/**
	 * 是否可以退回？
	 * 
	 * @throws Exception
	 */
	public final boolean getIsCanReturn() throws Exception {
		if (this.getHisReturnRole() == ReturnRole.CanNotReturn) {
			return false;
		}
		return true;
	}

	/**
	 * 已读回执
	 */
	public final ReadReceipts getReadReceipts() throws Exception {
		return ReadReceipts.forValue(this.GetValIntByKey(NodeAttr.ReadReceipts));
	}

	public final void setReadReceipts(ReadReceipts value) throws Exception {
		this.SetValByKey(NodeAttr.ReadReceipts, value.getValue());
	}

	/**
	 * 退回规则
	 */
	public final ReturnRole getHisReturnRole() throws Exception {
		return ReturnRole.forValue(this.GetValIntByKey(NodeAttr.ReturnRole));
	}

	public final void setHisReturnRole(ReturnRole value) throws Exception {
		this.SetValByKey(NodeAttr.ReturnRole, value.getValue());
	}

	/**
	 * 是不是中间节点
	 */
	public final boolean getIsMiddleNode() throws Exception {
		if (this.getHisNodePosType() == NodePosType.Mid) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否是工作质量考核点
	 */
	public final boolean getIsEval() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsEval);
	}

	public final void setIsEval(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsEval, value);
	}

	public final String getHisSubFlows11() throws Exception {
		return this.GetValStringByKey(NodeAttr.HisSubFlows);
	}

	public final void setHisSubFlows11(String value) throws Exception {
		this.SetValByKey(NodeAttr.HisSubFlows, value);
	}

	public final String getFrmAttr() throws Exception {
		return this.GetValStringByKey(NodeAttr.FrmAttr);
	}

	public final void setFrmAttr(String value) throws Exception {
		this.SetValByKey(NodeAttr.FrmAttr, value);
	}

	public final boolean getIsHL() throws Exception {
		switch (this.getHisNodeWorkType()) {
		case WorkHL:
		case WorkFHL:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 是否是分流
	 */
	public final boolean getIsFL() throws Exception {
		switch (this.getHisNodeWorkType()) {
		case WorkFL:
		case WorkFHL:
		case StartWorkFL:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 是否分流合流
	 */
	public final boolean getIsFLHL() throws Exception {
		switch (this.getHisNodeWorkType()) {
		case WorkHL:
		case WorkFL:
		case WorkFHL:
		case StartWorkFL:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 是否有流程完成条件
	 */
	public final boolean getIsCCFlow() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsCCFlow);
	}

	public final void setIsCCFlow(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsCCFlow, value);
	}

	/**
	 * 接受人sql
	 */
	public final String getDeliveryParas() throws Exception {
		String s = this.GetValStringByKey(NodeAttr.DeliveryParas);
		s = s.replace("~", "'");

		if (this.getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormEmpsField && DataType.IsNullOrEmpty(s) == true) {
			return "ToEmps";
		}
		return s;
	}

	public final void setDeliveryParas(String value) throws Exception {
		this.SetValByKey(NodeAttr.DeliveryParas, value);
	}

	/**
	 * 接受人员集合里,是否排除当前操作员?
	 */
	public final boolean getIsExpSender() throws Exception {
		return this.GetValBooleanByKey(NodeAttr.IsExpSender);
	}

	public final void setIsExpSender(boolean value) throws Exception {
		this.SetValByKey(NodeAttr.IsExpSender, value);
	}

	/**
	 * 是不是PC工作节点
	 */
	public final boolean getIsPCNode() {
		return false;
	}

	/**
	 * 工作性质
	 */
	public final String getNodeWorkTypeText() throws Exception {
		return this.getHisNodeWorkType().toString();
	}

	/**
	 * 用户执行动作之后,所要做的工作
	 * 
	 * @return 返回消息,运行的消息
	 */
	public final String AfterDoTask() {
		return "";
	}

	/// #endregion

	/// #region 构造函数
	/**
	 * 节点
	 */
	public Node() {
	}

	/**
	 * 节点
	 * 
	 * @param _oid
	 *            节点ID
	 */
	public Node(int _oid) throws Exception {
		this.setNodeID(_oid);
		this.Retrieve();
	}

	public Node(String ndName) throws Exception {
		ndName = ndName.replace("ND", "");
		this.setNodeID(Integer.parseInt(ndName));

		if (SystemConfig.getIsDebug()) {
			if (this.RetrieveFromDBSources() <= 0) {
				throw new RuntimeException("Node Retrieve 错误没有ID=" + ndName);
			}
		} else {
			if (this.Retrieve() <= 0) {
				throw new RuntimeException("Node Retrieve 错误没有ID=" + ndName);
			}
		}
	}

	public final String getEnName() throws Exception {
		return "ND" + this.getNodeID();
	}

	public final String getEnsName() throws Exception {
		return "ND" + this.getNodeID() + "s";
	}

	/**
	 * 节点意见名称，如果为空则取节点名称.
	 */
	public final String getFWCNodeName() throws Exception {
		String str = this.GetValStringByKey(FrmWorkCheckAttr.FWCNodeName);
		if (DataType.IsNullOrEmpty(str)) {
			return this.getName();
		}
		return str;
	}

	/**
	 * 审核组件里面的工作人员先后顺序排列模式 0= 按照审批时间. 1= 按照接受人员列表(官职大小)
	 */
	public final int getFWCOrderModel() throws Exception {
		return this.GetValIntByKey(FrmWorkCheckAttr.FWCOrderModel);
	}

	public final float getFWC_H() throws Exception {
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_H);
	}

	public final float getFWC_W() throws Exception {
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_W);
	}

	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "节点");

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		/// #region 基本属性.
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 150, 10);
		map.AddTBString(NodeAttr.Tip, null, "操作提示", true, true, 0, 100, 10, false);

		map.AddTBInt(NodeAttr.Step, NodeWorkType.Work.getValue(), "流程步骤", true, false);

		map.AddTBString(NodeAttr.ICON, null, "节点ICON图片路径", true, false, 0, 70, 10);

		map.AddTBInt(NodeAttr.NodeWorkType, 0, "节点类型", false, false);
		map.AddTBInt(NodeAttr.SubThreadType, 0, "子线程ID", false, false);

		map.AddTBString(NodeAttr.FK_Flow, null, "FK_Flow", false, false, 0, 3, 10);
		map.AddTBInt(NodeAttr.IsGuestNode, 0, "是否是客户执行节点", false, false);

		map.AddTBString(NodeAttr.FlowName, null, "流程名", false, true, 0, 200, 10);
		map.AddTBInt(NodeAttr.IsToParentNextNode, 0, "子流程运行到该节点时，让父流程自动运行到下一步", false, false);

		// map.AddTBString(NodeAttr.FK_FlowSort, null, "FK_FlowSort", false,
		// true, 0, 4, 10);
		// map.AddTBString(NodeAttr.FK_FlowSortT, null, "FK_FlowSortT", false,
		// true, 0, 100, 10);

		map.AddTBString(NodeAttr.FrmAttr, null, "FrmAttr", false, true, 0, 300, 10);
		map.AddTBInt(NodeAttr.IsBUnit, 0, "是否是节点模版(业务单元)", true, false);

		/// #endregion 基本属性.

		/// #region 审核组件.
		map.AddTBInt(NodeAttr.FWCSta, 0, "审核组件", false, false);
		map.AddTBFloat(NodeAttr.FWC_H, 0, "审核组件高度", false, true);
		map.AddTBInt(FrmWorkCheckAttr.FWCOrderModel, 0, "协作模式下操作员显示顺序", false, false);
		map.AddTBInt(FrmWorkCheckAttr.FWCVer, 0, "审核组件版本", false, false);
		map.AddTBInt("FWCAth", 0, "审核附件是否启用", false, false);

		/// #endregion 审核组件.

		/// #region 考核属性.
		map.AddTBString(BtnAttr.CHLab, "节点时限", "节点时限", true, false, 0, 50, 10);
		map.AddTBInt(BtnAttr.CHRole, 0, "时限规则", true, false);

		map.AddTBFloat(NodeAttr.TimeLimit, 2, "限期(天)", true, false); // "限期(天)".
		// map.AddTBFloat(NodeAttr.TSpanHour, 0, "小时", true, false); //"限期(分钟)".
		map.AddTBInt(NodeAttr.TWay, 0, "时间计算方式", true, false); // 0=不计算节假日,1=计算节假日.

		map.AddTBInt(NodeAttr.TAlertRole, 0, "逾期提醒规则", false, false); // "限期(天)"
		map.AddTBInt(NodeAttr.TAlertWay, 0, "逾期提醒方式", false, false); // "限期(天)"

		map.AddTBFloat(NodeAttr.WarningDay, 1, "工作预警(天)", true, false); // "警告期限(0不警告)"
		// map.AddTBFloat(NodeAttr.WarningHour, 0, "工作预警(小时)", true, false); //
		// "警告期限(0不警告)"
		// map.SetHelperUrl(NodeAttr.WarningHour,
		// "http://ccbpm.mydoc.io/?v=5404&t=17999");

		map.AddTBInt(NodeAttr.WAlertRole, 0, "预警提醒规则", false, false); // "限期(天)"
		map.AddTBInt(NodeAttr.WAlertWay, 0, "预警提醒方式", false, false); // "限期(天)"

		map.AddTBFloat(NodeAttr.TCent, 2, "扣分(每延期1小时)", false, false);
		map.AddTBInt(NodeAttr.CHWay, 0, "考核方式", false, false); // "限期(天)"

		// 考核相关.
		map.AddTBInt(NodeAttr.IsEval, 0, "是否工作质量考核", true, true);
		map.AddTBInt(NodeAttr.OutTimeDeal, 0, "超时处理方式", false, false);
		map.AddTBString(NodeAttr.DoOutTime, null, "超时处理内容", true, false, 0, 300, 10, true);

		/// #endregion 考核属性.

		map.AddTBString(FrmWorkCheckAttr.FWCNodeName, null, "节点意见名称", true, false, 0, 100, 10);
		map.AddTBString(NodeAttr.Doc, null, "描述", true, false, 0, 100, 10);
		map.AddBoolean(NodeAttr.IsTask, true, "允许分配工作否?", true, true);

		// 退回相关.
		map.AddTBInt(NodeAttr.ReturnRole, 2, "退回规则", true, true);
		map.AddTBString(NodeAttr.ReturnReasonsItems, null, "退回原因", true, false, 0, 50, 10, true);
		map.AddTBString(NodeAttr.ReturnAlert, null, "被退回后信息提示", true, false, 0, 50, 10, true);

		map.AddTBInt(NodeAttr.ReturnOneNodeRole, 0, "单节点退回规则", true, true);
		map.AddTBString(BtnAttr.ReturnField, null, "退回信息填写字段", true, false, 0, 50, 10, true);

		map.AddTBInt(NodeAttr.DeliveryWay, 0, "访问规则", true, true);
		map.AddTBInt(NodeAttr.IsExpSender, 1, "本节点接收人不允许包含上一步发送人", true, true);

		map.AddTBInt(NodeAttr.CancelRole, 0, "撤销规则", true, true);
		map.AddTBInt(NodeAttr.CancelDisWhenRead, 0, "对方已读不能撤销", true, true);

		map.AddTBInt(NodeAttr.WhenNoWorker, 0, "未找到处理人时", true, true);
		map.AddTBString(NodeAttr.DeliveryParas, null, "访问规则设置", true, false, 0, 300, 10);
		map.AddTBString(NodeAttr.NodeFrmID, null, "节点表单ID", true, false, 0, 50, 10);

		map.AddTBInt(NodeAttr.CCRole, 0, "抄送规则", true, true);
		map.AddTBInt(NodeAttr.CCWriteTo, 0, "抄送数据写入规则", true, true);

		map.AddTBInt(BtnAttr.DelEnable, 0, "删除规则", true, true);
		map.AddTBInt(NodeAttr.SaveModel, 0, "保存模式", true, true);

		map.AddTBInt(NodeAttr.IsCanDelFlow, 0, "是否可以删除流程", true, true);

		map.AddTBInt(NodeAttr.ThreadKillRole, 0, "子线程删除方式", true, true);

		map.AddTBInt(NodeAttr.TodolistModel, 0, "多人处理规则", true, true);

		// add.
		map.AddTBInt(NodeAttr.TeamLeaderConfirmRole, 0, "组长确认规则", true, true);
		map.AddTBString(NodeAttr.TeamLeaderConfirmDoc, null, "组长确认设置内容", true, false, 0, 100, 10);
		map.AddTBInt(BtnAttr.HuiQianLeaderRole, 0, "组长会签规则", true, true);

		map.AddTBInt(NodeAttr.IsAllowRepeatEmps, 0, "是否允许子线程接受人员重复(对子线程点有效)?", true, true);
		map.AddTBInt(NodeAttr.IsBackTracking, 1, "是否可以在退回后原路返回(只有启用退回功能才有效)", true, true);
		map.AddTBInt(NodeAttr.IsRM, 1, "是否启用投递路径自动记忆功能?", true, true);
		map.AddTBInt(NodeAttr.IsOpenOver, 0, "是否打开即审批?", true, true);
		map.AddBoolean(NodeAttr.IsHandOver, false, "是否可以移交", true, true);
		map.AddTBDecimal(NodeAttr.PassRate, 100, "通过率", true, true);
		map.AddTBInt(NodeAttr.RunModel, 0, "运行模式(对普通节点有效)", true, true);
		map.AddTBInt(NodeAttr.BlockModel, 0, "阻塞模式", true, true);
		map.AddTBString(NodeAttr.BlockExp, null, "阻塞表达式", true, false, 0, 200, 10);
		map.AddTBString(NodeAttr.BlockAlert, null, "被阻塞提示信息", true, false, 0, 100, 10);

		map.AddTBInt(NodeAttr.WhoExeIt, 0, "谁执行它", true, true);
		map.AddTBInt(NodeAttr.ReadReceipts, 0, "已读回执", true, true);
		map.AddTBInt(NodeAttr.CondModel, 0, "方向条件控制规则", true, true);

		// 自动跳转.
		map.AddTBInt(NodeAttr.AutoJumpRole0, 0, "处理人就是提交人0", false, false);
		map.AddTBInt(NodeAttr.AutoJumpRole1, 0, "处理人已经出现过1", false, false);
		map.AddTBInt(NodeAttr.AutoJumpRole2, 0, "处理人与上一步相同2", false, false);

		// 父子流程.
		map.AddTBString(NodeAttr.SFActiveFlows, null, "启动的子流程", true, false, 0, 100, 10);

		// 批处理.
		map.AddTBInt(NodeAttr.BatchRole, 0, "批处理", true, true);
		map.AddTBInt(NodeAttr.BatchListCount, 12, "批处理数量", true, true);
		map.AddTBString(NodeAttr.BatchParas, null, "参数", true, false, 0, 500, 10);
		map.AddTBInt(NodeAttr.PrintDocEnable, 0, "打印方式", true, true);
		// 打印PDF的处理
		map.AddTBInt(BtnAttr.PrintPDFModle, 0, "PDF打印规则", true, true);
		map.AddTBString(BtnAttr.ShuiYinModle, null, "打印水印规则", true, false, 20, 100, 100, true);

		// 与未来处理人有关系.
		// map.AddTBInt(NodeAttr.IsFullSA, 1, "是否计算未来处理人?", false, false);
		// map.AddTBInt(NodeAttr.IsFullSATime, 0, "是否计算未来接受与处理时间?", false,
		// false);
		// map.AddTBInt(NodeAttr.IsFullSAAlert, 0, "是否接受未来工作到达消息提醒?", false,
		// false);

		// 表单相关.
		map.AddTBInt(NodeAttr.FormType, 1, "表单类型", false, false);
		map.AddTBString(NodeAttr.FormUrl, "http://", "表单URL", true, false, 0, 300, 10);
		map.AddTBInt(NodeAttr.TurnToDeal, 0, "转向处理", false, false);
		map.AddTBString(NodeAttr.TurnToDealDoc, null, "发送后提示信息", true, false, 0, 200, 10, true);
		map.AddTBInt(NodeAttr.NodePosType, 0, "位置", false, false);
		map.AddTBInt(NodeAttr.IsCCFlow, 0, "是否有流程完成条件", false, false);
		map.AddTBString(NodeAttr.HisStas, null, "岗位", false, false, 0, 300, 10);
		map.AddTBString(NodeAttr.HisDeptStrs, null, "部门", false, false, 0, 300, 10);
		map.AddTBString(NodeAttr.HisToNDs, null, "转到的节点", false, false, 0, 50, 10);
		map.AddTBString(NodeAttr.HisBillIDs, null, "单据IDs", false, false, 0, 50, 10);
		// map.AddTBString(NodeAttr.HisEmps, null, "HisEmps", false, false, 0,
		// 3000, 10);
		map.AddTBString(NodeAttr.HisSubFlows, null, "HisSubFlows", false, false, 0, 30, 10);
		map.AddTBString(NodeAttr.PTable, null, "物理表", false, false, 0, 100, 10);

		map.AddTBString(NodeAttr.GroupStaNDs, null, "岗位分组节点", false, false, 0, 200, 10);
		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

		map.AddTBString(NodeAttr.FocusField, null, "焦点字段", false, false, 0, 30, 10);
		map.AddTBString(NodeAttr.JumpToNodes, null, "可跳转的节点", true, false, 0, 100, 10, true);

		map.AddTBString(NodeAttr.RefOneFrmTreeType, "", "独立表单类型", false, false, 0, 100, 10); // RefOneFrmTree

		map.AddTBString(NodeAttr.DoOutTimeCond, null, "执行超时的条件", false, false, 0, 200, 100);

		// 按钮控制部分.
		// map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0,
		// 50, 10, true);
		map.AddTBAtParas(500);

		// 启动子线程参数 2013-01-04
		map.AddTBInt(NodeAttr.SubFlowStartWay, 0, "子线程启动方式", true, false);
		map.AddTBString(NodeAttr.SubFlowStartParas, null, "启动参数", true, false, 0, 100, 10);

		// 启动自动运行. 2013-01-04
		map.AddTBInt(NodeAttr.AutoRunEnable, 0, "是否启动自动运行？", true, false);
		map.AddTBString(NodeAttr.AutoRunParas, null, "自动运行参数", true, false, 0, 100, 10);

		map.AddTBString(NodeAttr.SelfParas, null, "自定义参数(如果太小可以手动扩大)", true, false, 0, 1000, 10);

		/// #region 与参数有关系的属性。

		/// #region 子流程相关的参数
		map.AddTBFloat(FrmSubFlowAttr.SF_H, 300, "高度", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_W, 400, "宽度", true, false);

		/// #endregion 子流程相关的参数

		// map.AddDDLSysEnum(FrmEventAttr.MsgCtrl, 0, "消息发送控制", true, true,
		// FrmEventAttr.MsgCtrl,
		// "@0=不发送@1=按设置的发送范围自动发送@2=由本节点表单系统字段(IsSendEmail,IsSendSMS)来决定@3=由SDK开发者参数(IsSendEmail,IsSendSMS)来决定",
		// true);

		// map.AddBoolean(FrmEventAttr.MailEnable, true,
		// "是否启用邮件发送？(如果启用就要设置邮件模版，支持ccflow表达式。)", true, true, true);
		// map.AddTBString(FrmEventAttr.MailTitle, null, "邮件标题模版", true, false,
		// 0, 200, 20, true);
		// map.AddTBStringDoc(FrmEventAttr.MailDoc, null, "邮件内容模版", true, false,
		// true);

		//// 是否启用手机短信？
		// map.AddBoolean(FrmEventAttr.SMSEnable, false,
		//// "是否启用短信发送？(如果启用就要设置短信模版，支持ccflow表达式。)", true, true, true);
		// map.AddTBStringDoc(FrmEventAttr.SMSDoc, null, "短信内容模版", true, false,
		//// true);
		// map.AddBoolean(FrmEventAttr.MobilePushEnable, true, "是否推送到手机、pad端。",
		//// true, true, true);

		/// #endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

	/**
	 * 我能处理当前的节点吗？
	 * 
	 * @return
	 */
	public final boolean CanIdoIt() {
		return false;
	}

	/// #endregion

	/**
	 * 删除前的逻辑处理.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean beforeDelete() throws Exception {
		int num = 0;
		// 如果是结束节点，则自动结束流程
		if (this.getNodePosType() == NodePosType.End) {
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve("FK_Flow", this.getFK_Flow());
			for (GenerWorkFlow gwf : gwfs.ToJavaList()) {
				BP.WF.Dev2Interface.Flow_DoFlowOver(gwf.getFK_Flow(), gwf.getWorkID(), "流程成功结束");
			}
		}
		// 判断是否可以被删除.
		num = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerlist WHERE FK_Node=" + this.getNodeID());
		if (num != 0) {
			throw new RuntimeException("@该节点[" + this.getNodeID() + "," + this.getName() + "]有待办工作存在，您不能删除它.");
		}

		// 删除它的节点。
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + this.getNodeID());
		md.Delete();

		// 删除分组.
		BP.Sys.GroupFields gfs = new BP.Sys.GroupFields();
		gfs.Delete(BP.Sys.GroupFieldAttr.FrmID, md.getNo());

		// 删除它的明细。
		BP.Sys.MapDtls dtls = new BP.Sys.MapDtls(md.getNo());
		dtls.Delete();

		// 删除框架
		BP.Sys.MapFrames frams = new BP.Sys.MapFrames(md.getNo());
		frams.Delete();

		// 删除扩展
		BP.Sys.MapExts exts = new BP.Sys.MapExts(md.getNo());
		exts.Delete();

		// 删除节点与岗位的对应.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_NodeStation WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_NodeEmp  WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_NodeDept WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_FrmNode  WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_CCEmp  WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_CH  WHERE FK_Node=" + this.getNodeID());

		// 删除附件.
		BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmAttachment  WHERE FK_MapData='" + this.getNodeID() + "'");
		return super.beforeDelete();
	}

	/**
	 * 文书流程
	 * 
	 * @param md
	 * @throws Exception
	 */
	private void AddDocAttr(BP.Sys.MapData md) throws Exception {
		/* 如果是单据流程？ */
		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("KeyWord");
		attr.setName("主题词");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(-99);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("FZ");
		attr.setName("附注");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("DW_SW");
		attr.setName("收文单位");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("DW_FW");
		attr.setName("发文单位");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("DW_BS");
		attr.setName("主报(送)单位");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("DW_CS");
		attr.setName("抄报(送)单位");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("NumPrint");
		attr.setName("印制份数");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(false);
		attr.setMinLen(0);
		attr.setMaxLen(10);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("JMCD");
		attr.setName("机密程度");
		attr.setMyDataType(BP.DA.DataType.AppInt);
		attr.setUIContralType(UIContralType.DDL);
		attr.setLGType(FieldTypeS.Enum);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(false);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setUIBindKey("JMCD");
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("PRI");
		attr.setName("紧急程度");
		attr.setMyDataType(BP.DA.DataType.AppInt);
		attr.setUIContralType(UIContralType.DDL);
		attr.setLGType(FieldTypeS.Enum);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(false);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setUIBindKey("PRI");
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("GWWH");
		attr.setName("公文文号");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(true);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(false);
		attr.setMinLen(0);
		attr.setMaxLen(300);
		attr.setIdx(1);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.Insert();
	}

	/**
	 * 修复map
	 * 
	 * @throws Exception
	 */
	public final String RepareMap(Flow fl) throws Exception {
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + this.getNodeID());
		if (md.RetrieveFromDBSources() == 0) {
			this.CreateMap();
			return "";
		}

		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, md.getNo());

		/// #region 增加节点必要的字段.
		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		if (attrs.Contains(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("WorkID");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(BP.En.EditType.Readonly);
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, "FID", MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("FID");
			attr.setName("FID");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setDefVal("0");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, WorkAttr.RDT, MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.RDT);
			attr.setName("接受时间"); // "接受时间";
			attr.setMyDataType(BP.DA.DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setTag("1");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, WorkAttr.CDT, MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.CDT);
			if (this.getIsStartNode())
				attr.setName("发起时间"); // "发起时间";
			else
				attr.setName("完成时间"); // "完成时间";
			attr.setMyDataType(BP.DA.DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("@RDT");
			attr.setTag("1");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, WorkAttr.Rec, MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.Rec);
			if (this.getIsStartNode() == false)
				attr.setName("记录人"); // "记录人";
			else
				attr.setName("发起人"); // "发起人";

			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(32);
			attr.setMinLen(0);
			attr.setDefVal("@WebUser.No");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, WorkAttr.Emps, MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.Emps);
			attr.setName(WorkAttr.Emps);
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(8000);
			attr.setMinLen(0);
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, StartWorkAttr.FK_Dept, MapAttrAttr.FK_MapData, md.getNo()) == false) {
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(StartWorkAttr.FK_Dept);
			attr.setName("操作员部门"); // "操作员部门";
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			attr.setUIBindKey("BP.Port.Depts");
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.Insert();
		}

		if (fl.getIsMD5()
				&& attrs.Contains(MapAttrAttr.KeyOfEn, WorkAttr.MD5, MapAttrAttr.FK_MapData, md.getNo()) == false) {
			/* 如果是MD5加密流程. */
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(StartWorkAttr.MD5);
			attr.setUIBindKey(attr.getKeyOfEn());
			attr.setName("MD5");
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setUIVisible(false);
			attr.setMinLen(0);
			attr.setMaxLen(40);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (this.getNodePosType() == NodePosType.Start) {

			if (attrs.Contains(MapAttrAttr.KeyOfEn, StartWorkAttr.Title, MapAttrAttr.FK_MapData, md.getNo()) == false) {
				attr = new BP.Sys.MapAttr();
				attr.setFK_MapData(md.getNo());
				attr.setHisEditType(BP.En.EditType.UnDel);
				attr.setKeyOfEn(StartWorkAttr.Title);
				attr.setName("标题"); // "流程标题";
				attr.setMyDataType(BP.DA.DataType.AppString);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(true);
				attr.setUIIsLine(true);
				attr.setUIWidth(251);

				attr.setMinLen(0);
				attr.setMaxLen(200);
				attr.setIdx(-100);
				attr.setX((float) 171.2);
				attr.setY((float) 68.4);
				attr.Insert();
			}

			if (attrs.Contains(MapAttrAttr.KeyOfEn, "FK_NY", MapAttrAttr.FK_MapData, md.getNo()) == false) {
				attr = new BP.Sys.MapAttr();
				attr.setFK_MapData(md.getNo());
				attr.setHisEditType(BP.En.EditType.UnDel);
				attr.setKeyOfEn("FK_NY");
				attr.setName("年月"); // "年月";
				attr.setMyDataType(BP.DA.DataType.AppString);
				attr.setUIContralType(UIContralType.TB);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setLGType(FieldTypeS.Normal);
				// attr.UIBindKey = "BP.Pub.NYs";
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setMinLen(0);
				attr.setMaxLen(7);
				attr.Insert();
			}

			if (attrs.Contains(MapAttrAttr.KeyOfEn, "MyNum", MapAttrAttr.FK_MapData, md.getNo()) == false) {
				attr = new BP.Sys.MapAttr();
				attr.setFK_MapData(md.getNo());
				attr.setHisEditType(BP.En.EditType.UnDel);
				attr.setKeyOfEn("MyNum");
				attr.setName("个数"); // "个数";
				attr.setDefVal("1");
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.Insert();
			}
		}

		/// #endregion 增加节点必要的字段.

		// 表单自检.
		md.RepairMap();

		String msg = "";
		if (!this.getFocusField().equals("")) {
			if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getFocusField(), MapAttrAttr.FK_MapData, md.getNo()) == false) {
				msg += "@焦点字段 " + this.getFocusField() + " 被非法删除了.";
			}
		}
		return msg;
	}

	/**
	 * 建立map
	 * 
	 * @throws Exception
	 */
	public final void CreateMap() throws Exception {
		// 创建节点表单.
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + this.getNodeID());
		md.Delete();

		md.setName(this.getName());

		if (this.getHisFormType() == NodeFormType.FoolForm || this.getHisFormType() == NodeFormType.FoolTruck) {
			md.setHisFrmType(FrmType.FoolForm);
		}

		if (this.getHisFormType() == NodeFormType.FreeForm) {
			md.setHisFrmType(FrmType.FreeFrm);
		}

		if (this.getHisFlow().getHisDataStoreModel() == DataStoreModel.SpecTable) {
			md.setPTable(this.getHisFlow().getPTable());
		}
		md.Insert();

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("OID");
		attr.setName("WorkID");
		attr.setMyDataType(BP.DA.DataType.AppInt);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setDefVal("0");
		attr.setHisEditType(BP.En.EditType.Readonly);
		attr.Insert();

		if (this.getHisFlow().getFlowAppType() == FlowAppType.DocFlow) {
			this.AddDocAttr(md);
		}

		attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("FID");
		attr.setName("FID");
		attr.setMyDataType(BP.DA.DataType.AppInt);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setDefVal("0");
		attr.Insert();

		attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.RDT);
		attr.setName("接受时间"); // "接受时间";
		attr.setMyDataType(BP.DA.DataType.AppDateTime);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setTag("1");
		attr.Insert();

		attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.CDT);
		if (this.getIsStartNode()) {
			attr.setName("发起时间"); // "发起时间";
		} else {
			attr.setName("完成时间"); // "完成时间";
		}

		attr.setMyDataType(BP.DA.DataType.AppDateTime);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setDefVal("@RDT");
		attr.setTag("1");
		attr.Insert();

		attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.Rec);
		if (this.getIsStartNode() == false) {
			attr.setName("记录人"); // "记录人";
		} else {
			attr.setName("发起人"); // "发起人";
		}

		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setMaxLen(32);
		attr.setMinLen(0);
		attr.setDefVal("@WebUser.No");
		attr.Insert();

		attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.Emps);
		attr.setName("Emps");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setMaxLen(8000);
		attr.setMinLen(0);
		attr.Insert();

		attr = new BP.Sys.MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(StartWorkAttr.FK_Dept);
		attr.setName("操作员部门"); // "操作员部门";
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setMinLen(0);
		attr.setMaxLen(50);
		attr.Insert();

		if (this.getNodePosType() == NodePosType.Start) {
			// 开始节点信息.
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.Edit);
			// attr.edit
			attr.setKeyOfEn("Title");
			attr.setName("标题"); // "流程标题";
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setUIWidth(251);
			attr.setMinLen(0);
			attr.setMaxLen(200);
			attr.setIdx(-100);
			attr.setX((float) 174.83);
			attr.setY((float) 54.4);
			attr.Insert();

			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn("FK_NY");
			attr.setName("年月"); // "年月";
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(7);
			attr.Insert();

			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn("MyNum");
			attr.setName("个数"); // "个数";
			attr.setDefVal("1");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.Insert();
		}
	}
}