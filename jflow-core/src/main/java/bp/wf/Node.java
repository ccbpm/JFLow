package bp.wf;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.template.*;
import bp.wf.template.ccen.*;
import bp.wf.template.sflow.*;
import java.math.*;

/** 
 这里存放每个节点的信息.	 
*/
public class Node extends Entity
{

		///#region 参数属性
	/** 
	 方向条件控制规则
	*/
	public final DirCondModel getCondModel()  {
			//if (this.TodolistModel == TodolistModel.Teamup
			//      && this.IsEndNode == false)
			//    return DirCondModel.ByUserSelected;

		DirCondModel model = DirCondModel.forValue(this.GetValIntByKey(NodeAttr.CondModel));
		return model;
	}
	public final void setCondModel(DirCondModel value)
	{//DirCondModel model = (DirCondModel)value;
			//if (this.TodolistModel == TodolistModel.Teamup
			//  && model == DirCondModel.SendButtonSileSelect
			//  && this.IsEndNode == false)
			//    model = DirCondModel.ByUserSelected;

		this.SetValByKey(NodeAttr.CondModel, value.getValue());
	}
	/** 
	 抢办发送后处理规则
	*/
	public final QiangBanSendAfterRole getQiangBanSendAfterRole()  {
		return QiangBanSendAfterRole.forValue(this.GetParaInt(NodeAttr.QiangBanSendAfterRole, 0));
	}
	/** 
	 超时处理方式
	*/
	public final OutTimeDeal getHisOutTimeDeal()  {
		return OutTimeDeal.forValue(this.GetValIntByKey(NodeAttr.OutTimeDeal));
	}
	public final void setHisOutTimeDeal(OutTimeDeal value)  
	 {
		this.SetValByKey(NodeAttr.OutTimeDeal, value.getValue());
	}
	public final String getDoOutTimeCond() 
	{
		return this.GetValStringByKey(NodeAttr.DoOutTimeCond);
	}
	public final void setDoOutTimeCond(String value)  
	 {
		this.SetValByKey(NodeAttr.DoOutTimeCond, value);
	}
	/** 
	 待办删除规则
	*/
	public final int getGenerWorkerListDelRole()  {
		return this.GetParaInt("GenerWorkerListDelRole", 0);
	}
	public final void setGenerWorkerListDelRole(int value)
	{this.SetPara("GenerWorkerListDelRole", value);
	}
	/** 
	 超时处理内容.
	*/
	public final String getDoOutTime() 
	{
		return this.GetValStrByKey(NodeAttr.DoOutTime);
	}
	public final void setDoOutTime(String value)  
	 {
		this.SetValByKey(NodeAttr.DoOutTime, value);
	}
	/** 
	 子线程类型
	*/
	public final SubThreadType getHisSubThreadType()  {
		return SubThreadType.forValue(this.GetValIntByKey(NodeAttr.SubThreadType));
	}
	public final void setHisSubThreadType(SubThreadType value)  
	 {
		this.SetValByKey(NodeAttr.SubThreadType, value.getValue());
	}
	/** 
	 手工启动的子流程个数
	*/
	public final int getSubFlowHandNum()  {
		return this.GetParaInt("SubFlowHandNum", 0);
	}
	/** 
	 是否是发送返回节点？
	*/
	public final boolean isSendBackNode()  {
		return this.GetParaBoolen(NodeAttr.IsSendBackNode, false);
	}
	public final void setSendBackNode(boolean value)
	{this.SetPara(NodeAttr.IsSendBackNode, value);
	}
	/** 
	 自动启动的子流程个数
	*/
	public final int getSubFlowAutoNum()  {
		return this.GetParaInt("SubFlowAutoNum", 0);
	}
	/** 
	 延续子流程个数
	*/
	public final int getSubFlowYanXuNum()  {
		return this.GetParaInt("SubFlowYanXuNum", 0);
	}

		///#endregion

	public static void ClearNodeAutoNum(int nodeID) throws Exception {
		Node nd = new Node(nodeID);
		nd.ClearAutoNumCash(true);
	}

	/**
	 * 是否是分流
	 *
	 * @throws Exception
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
		///#region 外键属性.
	/** 
	 它的抄送规则.
	*/
	public final CC getHisCC() throws Exception {

		Object tempVar = this.GetRefObject("HisCC");
		CC obj = tempVar instanceof CC ? (CC)tempVar : null;
		if (obj == null)
		{
			obj = new CC();
			obj.setNodeID(this.getNodeID());
			obj.Retrieve();
			this.SetRefObject("HisCC", obj);
		}
		return obj;
	}
	/** 
	 流程完成条件
	*/
	public final Conds getCondsOfFlowComplete() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new Conds(), CondAttr.FK_Node, this.getNodeID(), CondAttr.CondType, CondType.Flow.getValue(), CondAttr.Idx);
		return ens instanceof Conds ? (Conds)ens : null;
	}
	/** 
	 他的将要转向的方向集合
	 如果他没有到转向方向,他就是结束节点.
	 没有生命周期的概念,全部的节点.
	*/
	public final Nodes getHisToNodes() throws Exception {
		Object tempVar = this.GetRefObject("HisToNodes");
		Nodes obj = tempVar instanceof Nodes ? (Nodes)tempVar : null;
		int hisToNDNum = this.getHisToNDNum();
		if (obj == null)
		{
			obj = new Nodes();
			if (hisToNDNum == 0)
			{
				obj = new Nodes();
			}
			if (hisToNDNum == 1)
			{
				obj.AddEntities(this.getHisToNDs());
			}
			if (hisToNDNum > 1)
			{
				String toNodes = this.getHisToNDs().replace("@", ",");
				if (toNodes.length() > 1)
				{
					toNodes = toNodes.substring(1);
				}
				obj.RetrieveInOrderBy("NodeID", toNodes, "Step");
			}
			this.SetRefObject("HisToNodes", obj);
		}
		return obj;
	}
	public final NodeSimples getHisToNodeSimples() throws Exception {
		Object tempVar = this.GetRefObject("HisToNodesSipm");
		NodeSimples obj = tempVar instanceof NodeSimples ? (NodeSimples)tempVar : null;
		int hisToNDNum = this.getHisToNDNum();
		if (obj == null)
		{
			obj = new NodeSimples();
			if (hisToNDNum == 0)
			{
				obj = new NodeSimples();
			}
			if (hisToNDNum == 1)
			{
				obj.AddEntities(this.getHisToNDs());
			}

			if (hisToNDNum > 1)
			{
				String inStrs = this.getHisToNDs().replace('@', ',');
				inStrs = inStrs.substring(1);
				obj.RetrieveIn("NodeID", inStrs);

					//@101@102@103.
				String[] ndStrs = this.getHisToNDs().split("[@]", -1);

				NodeSimples myobjs = new NodeSimples();
				for (String nd : ndStrs)
				{
					if (DataType.IsNullOrEmpty(nd) == true)
					{
						continue;
					}
					Entity mynd = obj.GetEntityByKey("NodeID", Integer.parseInt(nd));
					myobjs.AddEntity(mynd);
				}
				obj = myobjs;
			}
			this.SetRefObject("HisToNodesSipm", obj);
		}
		return obj;
	}
	/** 
	 他的工作
	*/
	public final Work getHisWork() throws Exception {
		Work wk = null;
		if (this.getFormType() != NodeFormType.FoolTruck || this.WorkID == 0 || this.isStartNode() == true)
		{
			wk = new bp.wf.GEWork(this.getNodeID(), this.getNodeFrmID());
			wk.setHisNode(this);
			wk.setNodeID(this.getNodeID());
			return wk;
		}

			//如果是累加表单.
		wk = new bp.wf.GEWork(this.getNodeID(), this.getNodeFrmID());

		Map ma = wk.getEnMap();

			/* 求出来走过的表单集合 */
		String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A, WF_Node B ";
		sql += " WHERE A.NDFrom=B.NodeID  ";
		sql += "  AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.Start.getValue() + "  OR ActionType=" + ActionType.Skip.getValue() + ")  ";
		sql += "  AND B.FormType=" + NodeFormType.FoolTruck.getValue() + " "; // 仅仅找累加表单.
		sql += "  AND NDFrom!=" + this.getNodeID() + " "; //排除当前的表单.

			//if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.MSSQL)
			//    sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+CONVERT(varchar(10),B.NodeID) ) ";

			//if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
			//    sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+cast(B.NodeID as varchar(10)) ) ";

			//if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle)
			//    sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+to_char(B.NodeID) ) ";

		sql += "  AND (A.WorkID=" + this.WorkID + ") ";
		sql += " ORDER BY A.RDT ";

			// 获得已经走过的节点IDs.
		DataTable dtNodeIDs = DBAccess.RunSQLReturnTable(sql);
		String frmIDs = "";
		if (dtNodeIDs.Rows.size() > 0)
		{
				//把所有的节点字段.
			for (DataRow dr : dtNodeIDs.Rows)
			{
				if (frmIDs.contains("ND" + dr.getValue(0).toString()) == true)
				{
					continue;
				}
				frmIDs += "'ND" + dr.getValue(0).toString() + "',";
			}

			frmIDs = frmIDs.substring(0, frmIDs.length() - 1);
			wk.HisPassedFrmIDs = frmIDs; //求出所有的fromID.

			MapAttrs attrs = new MapAttrs();
			QueryObject qo = new QueryObject(attrs);
			qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + frmIDs + ")");
			qo.DoQuery();

				//设置成不可以用.
			for (MapAttr item : attrs.ToJavaList())
			{
				item.setUIIsEnable(false); //设置为只读的.
				item.setDefValReal(""); //设置默认值为空.

				ma.getAttrs().add(item.getHisAttr());
			}

				//设置为空.
			wk.setSQLCash(null);
		}

		wk.setHisNode(this);
		wk.setNodeID(this.getNodeID());
		wk.setSQLCash(null);

		Cash.getSQL_Cash().remove("ND" + this.getNodeID());
		return wk;
			//this.SetRefObject("HisWork", obj);

	}
	/** 
	 他的工作s
	*/
	public final Works getHisWorks() throws Exception {
		bp.en.Entities tempVar = this.getHisWork().getGetNewEntities();
		Works obj = tempVar instanceof Works ? (Works)tempVar : null;
		if(obj!=null)
			obj.clear();
		return obj;
	}
	/** 
	 流程
	*/
	public final FrmNodes getHisFrmNodes() throws Exception {
		Object tempVar = this.GetRefObject("FrmNodes");
		FrmNodes obj = tempVar instanceof FrmNodes ? (FrmNodes)tempVar : null;
		if (obj == null)
		{
			obj = new FrmNodes(this.getNodeID());
			this.SetRefObject("FrmNodes", obj);
		}
		return obj;
	}
	/** 
	 流程
	*/
	public final Flow getHisFlow() throws Exception {
		Object tempVar = this.GetRefObject("Flow");
		Flow obj = tempVar instanceof Flow ? (Flow)tempVar : null;
		if (obj == null)
		{
			obj = new Flow(this.getFK_Flow());
			this.SetRefObject("Flow", obj);
		}
		return obj;
	}
	/** 
	 消息推送.
	*/
	public final PushMsgs getHisPushMsgs() throws Exception {
		Object tempVar = this.GetRefObject("PushMsg");
		PushMsgs obj = tempVar instanceof PushMsgs ? (PushMsgs)tempVar : null;
		if (obj == null)
		{
			Entities ens = this.GetEntitiesAttrFromAutoNumCash(new PushMsgs(), PushMsgAttr.FK_Node, this.getNodeID(), null, null, null);

			obj = ens instanceof PushMsgs ? (PushMsgs)ens : null;

			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{

					//检查是否有默认的发送？如果没有就增加上他。
				boolean isHaveSend = false;
				boolean isHaveReturn = false;
				for (PushMsg item : obj.ToJavaList())
				{
					if (item.getFK_Event().equals(EventListNode.SendSuccess))
					{
						isHaveSend = true;
					}

					if (item.getFK_Event().equals(EventListNode.ReturnAfter))
					{
						isHaveReturn = true;
					}
				}

				if (isHaveSend == false)
				{
					PushMsg pm = new PushMsg();
					pm.setFKEvent(EventListNode.SendSuccess);
					pm.setSMSPushWay(1); //*默认:让其使用短消息提醒.
					pm.setSMSPushModel("Email");
					obj.AddEntity(pm);
				}

				if (isHaveReturn == false)
				{
					PushMsg pm = new PushMsg();
					pm.setFKEvent(EventListNode.ReturnAfter);
					pm.setSMSPushWay(1); //*默认:让其使用短消息提醒.
					pm.setSMSPushModel("Email");
					obj.AddEntity(pm);
				}
			}

			this.SetRefObject("PushMsg", obj);
		}
		return obj;
	}
	/** 
	 他的将要来自的方向集合
	 如果他没有到来的方向,他就是开始节点.
	*/
	public final Nodes getFromNodes() throws Exception {
		Object tempVar = this.GetRefObject("HisFromNodes");
		Nodes obj = tempVar instanceof Nodes ? (Nodes)tempVar : null;
		if (obj == null)
		{
				// 根据方向生成到达此节点的节点。
			Directions ens = new Directions();
			if (this.isStartNode())
			{
				obj = new Nodes();
			}
			else
			{
				obj = ens.GetHisFromNodes(this.getNodeID());
			}
			this.SetRefObject("HisFromNodes", obj);
		}
		return obj;
	}

	public final NodeStations getNodeStations() throws Exception {
		Object tempVar = this.GetRefObject("NodeStations");
		NodeStations obj = tempVar instanceof NodeStations ? (NodeStations)tempVar : null;
		if (obj == null)
		{
			obj = new NodeStations(this.getNodeID());
			this.SetRefObject("NodeStations", obj);
		}
		return obj;
	}
	public final NodeDepts getNodeDepts() throws Exception {
		Object tempVar = this.GetRefObject("NodeDepts");
		NodeDepts obj = tempVar instanceof NodeDepts ? (NodeDepts)tempVar : null;
		if (obj == null)
		{
			obj = new NodeDepts(this.getNodeID());
			this.SetRefObject("NodeDepts", obj);
		}
		return obj;
	}
	public final NodeEmps getNodeEmps() throws Exception {
		Object tempVar = this.GetRefObject("NodeEmps");
		NodeEmps obj = tempVar instanceof NodeEmps ? (NodeEmps)tempVar : null;
		if (obj == null)
		{
			obj = new NodeEmps(this.getNodeID());
			this.SetRefObject("NodeEmps", obj);
		}
		return obj;
	}

	public final FrmNodes getFrmNodes() throws Exception {
		Object tempVar = this.GetRefObject("FrmNodes");
		FrmNodes obj = tempVar instanceof FrmNodes ? (FrmNodes)tempVar : null;
		if (obj == null)
		{
			obj = new FrmNodes(this.getFK_Flow(), this.getNodeID());
			this.SetRefObject("FrmNodes", obj);
		}
		return obj;
	}
	public final MapData getMapData() throws Exception {
		Object tempVar = this.GetRefObject("MapData");
		MapData obj = tempVar instanceof MapData ? (MapData)tempVar : null;
		if (obj == null)
		{
			obj = new MapData(this.getNodeFrmID());
			this.SetRefObject("MapData", obj);
		}
		return obj;
	}
	/** 
	 获得事件
	*/
	public final FrmEvents getFrmEvents() throws Exception {
		Entities ens = this.GetEntitiesAttrFromAutoNumCash(new FrmEvents(), FrmEventAttr.FK_Node, this.getNodeID(), null, null, null);
		return ens instanceof FrmEvents ? (FrmEvents)ens : null;
	}


		///#endregion


		///#region 初试化全局的 Node
	@Override
	public String getPK()  {
		return "NodeID";
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		if (bp.web.WebUser.getNo().equals("admin") == true)
		{
			uac.IsUpdate = true;
		}
		return uac;
	}
	/** 
	 初试化全局的
	 
	 @return 
	*/
	public final NodePosType GetHisNodePosType() throws Exception {
		if (this.isSendBackNode())
		{
			return NodePosType.Mid;
		}

		String nodeid = String.valueOf(this.getNodeID());
		if (nodeid.substring(nodeid.length() - 2).equals("01") == true)
		{
			return NodePosType.Start;
		}

		if (this.getHisToNodes().isEmpty())
		{
			return NodePosType.End;
		}

		return NodePosType.Mid;
	}

	public final FWCAth getFWCAth()  {
		return FWCAth.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCAth));
	}
	public final void setFWCAth(FWCAth value)  
	 {
		this.SetValByKey(NodeWorkCheckAttr.FWCAth, value.getValue());
	}

	/** 
	 检查流程，修复必要的计算字段信息.
	 
	 param flowNo 流程
	 @return 返回检查信息
	*/
	public static String CheckFlow(Nodes nds, String flowNo) throws Exception {
		String sql = "";
		DataTable dt = null;

		// 单据信息，岗位，节点信息。
		for (Node nd : nds.ToJavaList())
		{

			// 工作岗位。
			sql = "SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nd.getNodeID();
			dt = DBAccess.RunSQLReturnTable(sql);
			String strs = "";
			for (DataRow dr : dt.Rows)
			{
				strs += "@" + dr.getValue(0).toString();
			}
			nd.setHisStas(strs);

			// 工作部门。
			sql = "SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + nd.getNodeID();
			dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				strs += "@" + dr.getValue(0).toString();
			}
			nd.setHisDeptStrs(strs);

			// 节点方向.
			strs = "";
			Directions dirs = new Directions(nd.getNodeID());
			for (Direction dir : dirs.ToJavaList())
			{
				strs += "@" + dir.getToNode();
			}
			nd.setHisToNDs(strs);


			// 检查节点的位置属性。
			nd.setHisNodePosType(nd.GetHisNodePosType());
			if (nd.isSendBackNode() == true)
			{
				nd.setNodePosType(NodePosType.Mid);
			}
			try
			{
				nd.Update();
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("err@" + ex.getMessage() + " node=" + nd.getName());
			}
		}

		// 处理岗位分组.
		sql = "SELECT HisStas, COUNT(*) as NUM FROM WF_Node WHERE FK_Flow='" + flowNo + "' GROUP BY HisStas";
		dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			String stas = dr.getValue(0).toString();
			String nodes = "";
			for (Node nd : nds.ToJavaList())
			{
				if (nd.getHisStas().equals(stas))
				{
					nodes += "@" + nd.getNodeID();
				}
			}

			for (Node nd : nds.ToJavaList())
			{
				if (nodes.contains("@" + String.valueOf(nd.getNodeID())) == false)
				{
					continue;
				}

				nd.setGroupStaNDs(nodes);
				nd.DirectUpdate();
			}
		}
		return "检查成功.";
	}

	@Override
	protected boolean beforeUpdate() throws Exception {
		//检查设计流程权限,集团模式下，不是自己创建的流程，不能设计流程.
		TemplateGlo.CheckPower(this.getFK_Flow());

		//删除自动数量的缓存数据.
		this.ClearAutoNumCash(false);


		if (this.isStartNode())
		{
			//this.SetValByKey(BtnAttr.ReturnRole, (int)ReturnRole.CanNotReturn);
			this.SetValByKey(BtnAttr.ShiftEnable, 0);
			this.SetValByKey(BtnAttr.EndFlowEnable, 0);

			Node oldN = new Node(this.getNodeID());
			if (this.getName().equals(oldN.getName()) == false)
			{
				//清空WF_Emp中的StartFlows 的内容
				try
				{
					DBAccess.RunSQL("UPDATE WF_Emp Set StartFlows ='' ");
				}
				catch (RuntimeException e)
				{
					/*如果没有此列，就自动创建此列.*/
					if (DBAccess.IsExitsTableCol("WF_Emp", "StartFlows") == false)
					{
						String sql = "";
						sql = "ALTER TABLE WF_Emp ADD StartFlows text ";

						if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle
							|| bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3
							||bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
						{
							sql = "ALTER TABLE WF_Emp ADD StartFlows blob";
						}

						if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.UX)
						{
							sql = "ALTER TABLE  WF_Emp ADD StartFlows bytea NULL ";
						}

						DBAccess.RunSQL(sql);
					}

				}
			}
			//清空WF_Emp中的StartFlow 
			DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");

		}

		//给icon设置默认值.
		if (this.GetValStrByKey(NodeAttr.ICON).equals(""))
		{
			this.setICON("审核.png");
		}


			///#region 如果是数据合并模式，就要检查节点中是否有子线程，如果有子线程就需要单独的表.
		if (this.getHisRunModel() == RunModel.SubThread)
		{
			MapData md = new MapData("ND" + this.getNodeID());
			if (!md.getPTable().equals("ND" + this.getNodeID()))
			{
				md.setPTable ( "ND" + this.getNodeID());
				md.Update();
			}
		}

			///#endregion 如果是数据合并模式，就要检查节点中是否有子线程，如果有子线程就需要单独的表.

		//更新版本号.
		Flow.UpdateVer(this.getFK_Flow());

		Flow fl = new Flow(this.getFK_Flow());

		this.setFlowName(fl.getName());

		//更新表单名称.
		DBAccess.RunSQL("UPDATE Sys_MapData SET Name='" + this.getName() + "' WHERE No='ND" + this.getNodeID() + "' AND name=''");
		switch (this.getHisRunModel())
		{
			case Ordinary:
				if (this.isStartNode())
				{
					this.setHisNodeWorkType(NodeWorkType.StartWork);
				}
				else
				{
					this.setHisNodeWorkType(NodeWorkType.Work);
				}
				break;
			case FL:
				if (this.isStartNode())
				{
					this.setHisNodeWorkType(NodeWorkType.StartWorkFL);
				}
				else
				{
					this.setHisNodeWorkType(NodeWorkType.WorkFL);
				}
				break;
			case HL:
				//if (this.IsStartNode)
				//    throw new Exception("@您不能设置开始节点为合流节点。");
				//else
				//    this.HisNodeWorkType = NodeWorkType.WorkHL;
				break;
			case FHL:
				//if (this.IsStartNode)
				//    throw new Exception("@您不能设置开始节点为分合流节点。");
				//else
				//    this.HisNodeWorkType = NodeWorkType.WorkFHL;
				break;
			case SubThread:
				this.setHisNodeWorkType(NodeWorkType.SubThreadWork);
				break;
			default:
				throw new RuntimeException("eeeee");

		}
		//断头路节点
		if (this.isSendBackNode() == true)
		{
			this.setNodePosType(NodePosType.Mid);
		}

		//创建审核组件附件
		FrmAttachment workCheckAth = new FrmAttachment();
		boolean isHave = workCheckAth.RetrieveByAttr(FrmAttachmentAttr.MyPK, "ND" + this.getNodeID() + "_FrmWorkCheck");
		//不包含审核组件
		if (isHave == false && this.getFWCAth() == FWCAth.MinAth)
		{
			workCheckAth = new FrmAttachment();
			/*如果没有查询到它,就有可能是没有创建.*/
			workCheckAth.setMyPK("ND" + this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setFK_MapData("ND" + String.valueOf(this.getNodeID()));
			workCheckAth.setNoOfObj("FrmWorkCheck");
			workCheckAth.setExts("*.*");

			//存储路径.
			//  workCheckAth.SaveTo = "/DataUser/UploadFile/";
			workCheckAth.setIsNote( false); //不显示note字段.
			workCheckAth.setIsVisable( false); // 让其在form 上不可见.

			//位置.

			workCheckAth.setH ((float)150);

			//多附件.
			workCheckAth.setUploadType(AttachmentUploadType.Multi);
			workCheckAth.setName("审核组件");
			workCheckAth.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=");
			workCheckAth.Insert();
		}

		return super.beforeUpdate();
	}

		///#endregion

	@Override
	protected void afterInsertUpdateAction() throws Exception {
		Flow fl = new Flow();
		fl.setNo(this.getFK_Flow());
		fl.RetrieveFromDBSources();
		MapData mapData = new MapData();
		mapData.setNo("ND" + this.getNodeID());
		if (mapData.RetrieveFromDBSources() != 0)
		{
			if (this.getHisRunModel() == RunModel.SubThread)
			{
				mapData.setPTable(mapData.getNo());
			}
			else
			{
				mapData.setPTable(fl.getPTable());
			}
			mapData.Update();
		}


		if (this.getFormType() == NodeFormType.RefOneFrmTree)
		{
			GEEntity en = new GEEntity(this.getNodeFrmID());
			if (this.getHisRunModel() == RunModel.SubThread && en.getEnMap().getAttrs().contains("FID") == false)
			{
				MapAttr attr = new MapAttr();
				attr.setFK_MapData(this.getNodeFrmID());
				attr.setKeyOfEn("FID");
				attr.setName("干流程ID");
				attr.setMyDataType(DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal( "0");
				attr.setHisEditType( EditType.Readonly);
				attr.Insert();
			}
			if (en.getEnMap().getAttrs().contains("Rec") == false)
			{
				MapAttr attr = new MapAttr();
				attr.setFK_MapData(this.getNodeFrmID());
				attr.setKeyOfEn("Rec");
				attr.setName("记录人");
				attr.setMyDataType(DataType.AppString);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal( "0");
				attr.setHisEditType( EditType.Readonly);
				attr.Insert();
			}


		}

		super.afterInsertUpdateAction();
	}


		///#region 基本属性
	/** 
	 是否启动自动运行？
	*/
	public final boolean getAutoRunEnable() 
	{
		return this.GetValBooleanByKey(NodeAttr.AutoRunEnable);
	}
	public final void setAutoRunEnable(boolean value)  
	 {
		this.SetValByKey(NodeAttr.AutoRunEnable, value);
	}
	/** 
	 自动运行参数
	*/
	public final String getAutoRunParas() 
	{
		return this.GetValStringByKey(NodeAttr.AutoRunParas);
	}
	public final void setAutoRunParas(String value)  
	 {
		this.SetValByKey(NodeAttr.AutoRunParas, value);
	}
	/** 
	 审核组件
	*/
	public final FrmWorkCheckSta getFrmWorkCheckSta()  {
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(NodeAttr.FWCSta));
	}
	public final void setFrmWorkCheckSta(FrmWorkCheckSta value)  
	 {
		this.SetValByKey(NodeAttr.FWCSta, value.getValue());
	}

	public final FrmSubFlowSta getSFSta()  {
		return FrmSubFlowSta.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFSta));
	}
	public final void setSFSta(FrmSubFlowSta value)  
	 {
		this.SetValByKey(FrmSubFlowAttr.SFSta, value.getValue());
	}
	/** 
	 审核组件版本
	*/
	public final int getFWCVer() 
	{
		return this.GetValIntByKey(NodeWorkCheckAttr.FWCVer, 0);
	}
	public final void setFWCVer(int value)  
	 {
		this.SetValByKey(NodeWorkCheckAttr.FWCVer, value);
	}
	/** 
	 内部编号
	*/
	public final String getNo()  {
		try
		{
			return String.valueOf(this.getNodeID()).substring(String.valueOf(this.getNodeID()).length() - 2);
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getMessage() + " - " + this.getNodeID());
			throw new RuntimeException("@没有获取到它的NodeID = " + this.getNodeID());
		}
	}
	/** 
	 自动跳转规则0-处理人就是提交人
	*/
	public final boolean getAutoJumpRole0() 
	{
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole0);
	}
	public final void setAutoJumpRole0(boolean value)  
	 {
		this.SetValByKey(NodeAttr.AutoJumpRole0, value);
	}
	/** 
	 自动跳转规则1-处理人已经出现过
	*/
	public final boolean getAutoJumpRole1() 
	{
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole1);
	}

	/** 
	 自动跳转规则2-处理人与上一步相同
	*/
	public final boolean getAutoJumpRole2() 
	{
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole2);
	}
	/** 
	 跳转的表达式.
	*/
	public final String getAutoJumpExp() 
	{
		return this.GetValStringByKey(NodeAttr.AutoJumpExp);
	}
	/** 
	 执行跳转时间 0=上节点发送时，1=打开时.
	*/
	public final int getSkipTime() 
	{
		return this.GetValIntByKey(NodeAttr.SkipTime);
	}
	/** 
	 启动参数
	*/
	public final String getSubFlowStartParas() 
	{
		return this.GetValStringByKey(NodeAttr.SubFlowStartParas);
	}
	public final void setSubFlowStartParas(String value)  
	 {
		this.SetValByKey(NodeAttr.SubFlowStartParas, value);
	}
	/** 
	 子线程启动方式
	*/
	public final SubFlowStartWay getSubFlowStartWay()  {
		return SubFlowStartWay.forValue(this.GetValIntByKey(NodeAttr.SubFlowStartWay));
	}
	public final void setSubFlowStartWay(SubFlowStartWay value)  
	 {
		this.SetValByKey(NodeAttr.SubFlowStartWay, value.getValue());
	}
	/** 
	 表单方案类型
	*/
	public final NodeFormType getHisFormType() throws Exception {
		return NodeFormType.forValue(this.GetValIntByKey(NodeAttr.FormType));
	}
	public final void setHisFormType(NodeFormType value)  
	 {
		this.SetValByKey(NodeAttr.FormType, value.getValue());
	}
	public final String getHisFormTypeText() throws Exception {
		if (this.getHisFormType() == NodeFormType.DisableIt)
		{
			return "树表单";
		}

		if (this.getHisFormType() == NodeFormType.ExcelForm)
		{
			return "Excel表单";
		}

		if (this.getHisFormType() == NodeFormType.FoolForm)
		{
			return "傻瓜表单";
		}

		if (this.getHisFormType() == NodeFormType.FoolTruck)
		{
			return "傻瓜轨迹表单";
		}


		if (this.getHisFormType() == NodeFormType.Develop)
		{
			return "开发者表单";
		}

		if (this.getHisFormType() == NodeFormType.SDKForm)
		{
			return "SDK表单";
		}

		if (this.getHisFormType() == NodeFormType.SelfForm)
		{
			return "自定义表单";
		}

		if (this.getHisFormType() == NodeFormType.SheetAutoTree)
		{
			return "动态表单树";
		}

		return "未知";
	}
	/** 
	 节点编号
	*/
	public final int getNodeID() 
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)
	 {
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	public final boolean isEnableTaskPool()  {
		if (this.getTodolistModel() == bp.wf.TodolistModel.Sharing)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 节点头像
	*/
	public final String getICON()  {
		String s = this.GetValStrByKey(NodeAttr.ICON);
		if (DataType.IsNullOrEmpty(s))
		{
			if (this.isStartNode())
			{
				return "审核.png";
			}
			else
			{
				return "前台.png";
			}
		}
		return s;
	}
	public final void setICON(String value)  
	 {
		this.SetValByKey(NodeAttr.ICON, value);
	}
	/** 
	 FormUrl 
	*/
	public final String getFormUrl()  {
		String str = this.GetValStrByKey(NodeAttr.FormUrl);
		str = str.replace("@SDKFromServHost",bp.difference.SystemConfig.getAppSettings().get("SDKFromServHost").toString());
		return str;
	}
	public final void setFormUrl(String value)  
	 {
		this.SetValByKey(NodeAttr.FormUrl, value);
	}
	/** 
	 表单类型
	*/
	public final NodeFormType getFormType()  {
		return NodeFormType.forValue(this.GetValIntByKey(NodeAttr.FormType));
	}
	public final void setFormType(NodeFormType value)  
	 {
		this.SetValByKey(NodeAttr.FormType, value.getValue());
	}

	/** 
	 名称
	*/
	public final String getName() 
	{
		return this.GetValStrByKey(EntityOIDNameAttr.Name);
	}
	public final void setName(String value)  
	 {
		this.SetValByKey(EntityOIDNameAttr.Name, value);
	}
	/** 
	 限期天
	*/
	public final int getTimeLimit()  {
		return (int)this.GetValFloatByKey(NodeAttr.TimeLimit);
	}
	public final void setTimeLimit(int value)  
	 {
		this.SetValByKey(NodeAttr.TimeLimit, value);
	}
	/** 
	 限期小时
	*/
	public final int getTimeLimitHH()  {
		return this.GetParaInt("TimeLimitHH", 0);
	}
	public final void setTimeLimitHH(int value)
	{this.SetPara("TimeLimitHH", value);
	}
	/** 
	 限期分钟
	*/
	public final int getTimeLimitMM()  {
		return this.GetParaInt("TimeLimitMM", 0);
	}
	public final void setTimeLimitMM(int value)
	{this.SetPara("TimeLimitMM", value);
	}

	/** 
	 逾期提醒规则
	*/
	public final CHAlertRole getTAlertRole()  {
		return CHAlertRole.forValue(this.GetValIntByKey(NodeAttr.TAlertRole));
	}
	public final void setTAlertRole(CHAlertRole value)  
	 {
		this.SetValByKey(NodeAttr.TAlertRole, value.getValue());
	}
	/** 
	 时间计算方式
	*/
	public final TWay getTWay()  {
		return TWay.forValue(this.GetValIntByKey(NodeAttr.TWay));
	}
	public final void setTWay(TWay value)  
	 {
		this.SetValByKey(NodeAttr.TWay, value.getValue());
	}
	/** 
	 逾期 - 提醒方式
	*/
	public final CHAlertWay getTAlertWay()  {
		return CHAlertWay.forValue(this.GetValIntByKey(NodeAttr.TAlertWay));
	}
	public final void setTAlertWay(CHAlertWay value)  
	 {
		this.SetValByKey(NodeAttr.TAlertWay, value.getValue());
	}
	/** 
	 预警天
	*/
	public final float getWarningDay()  {
		float i = this.GetValFloatByKey(NodeAttr.WarningDay);
		if (i == 0)
		{
			return 1;
		}
		return i;
	}
	public final void setWarningDay(float value)  
	 {
		this.SetValByKey(NodeAttr.WarningDay, value);
	}

	/** 
	 预警 - 提醒规则
	*/
	public final CHAlertRole getWAlertRole()  {
		return CHAlertRole.forValue(this.GetValIntByKey(NodeAttr.WAlertRole));
	}
	public final void setWAlertRole(CHAlertRole value)  
	 {
		this.SetValByKey(NodeAttr.WAlertRole, value.getValue());
	}
	/** 
	 预警 - 提醒方式
	*/
	public final CHAlertWay getWAlertWay()  {
		return CHAlertWay.forValue(this.GetValIntByKey(NodeAttr.WAlertWay));
	}
	public final void setWAlertWay(CHAlertWay value)  
	 {
		this.SetValByKey(NodeAttr.WAlertWay, value.getValue());
	}
	/** 
	 保存方式 @0=仅节点表 @1=节点与NDxxxRtp表.
	*/
	public final SaveModel getSaveModel()  {
		return SaveModel.forValue(this.GetValIntByKey(NodeAttr.SaveModel));
	}
	public final void setSaveModel(SaveModel value)  
	 {
		this.SetValByKey(NodeAttr.SaveModel, value.getValue());
	}
	/** 
	 流程步骤
	*/
	public final int getStep() 
	{
		return this.GetValIntByKey(NodeAttr.Step);
	}
	public final void setStep(int value)  
	 {
		this.SetValByKey(NodeAttr.Step, value);
	}


	/** 
	 扣分率（分/天）
	*/
	public final float getTCent() 
	{
		return this.GetValFloatByKey(NodeAttr.TCent);
	}
	public final void setTCent(float value)  
	 {
		this.SetValByKey(NodeAttr.TCent, value);
	}
	/** 
	 是否是客户执行节点？
	*/
	public final boolean isGuestNode() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsGuestNode);
	}
	public final void setGuestNode(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsGuestNode, value);
	}


	/** 
	 是否是开始节点
	*/
	public final boolean isStartNode()  {
		if (this.getNo().equals("01"))
		{
			return true;
		}
		return false;
	}
	/** 
	 x
	*/
	public final int getX() 
	{
		return this.GetValIntByKey(NodeAttr.X);
	}
	public final void setX(int value)
	{if (value <= 0)
		{
			this.SetValByKey(NodeAttr.X, 5);
		}
		else
		{
			this.SetValByKey(NodeAttr.X, value);
		}
	}
	/** 
	 y
	*/
	public final int getY() 
	{
		return this.GetValIntByKey(NodeAttr.Y);
	}
	public final void setY(int value)
	{if (value <= 0)
		{
			this.SetValByKey(NodeAttr.Y, 5);
		}
		else
		{
			this.SetValByKey(NodeAttr.Y, value);
		}
	}
	/** 
	 水执行它？
	*/
	public final int getWhoExeIt() 
	{
		return this.GetValIntByKey(NodeAttr.WhoExeIt);
	}
	/** 
	 位置
	*/
	public final NodePosType getNodePosType()  {
		return NodePosType.forValue(this.GetValIntByKey(NodeAttr.NodePosType));
	}
	public final void setNodePosType(NodePosType value)  
	 {
		this.SetValByKey(NodeAttr.NodePosType, value.getValue());
	}
	/** 
	 运行模式
	*/
	public final RunModel getHisRunModel()  {
		return RunModel.forValue(this.GetValIntByKey(NodeAttr.RunModel));
	}
	public final void setHisRunModel(RunModel value)  
	 {
		this.SetValByKey(NodeAttr.RunModel, value.getValue());
	}
	/** 
	 操纵提示
	*/
	public final String getTip() 
	{
		return this.GetValStrByKey(NodeAttr.Tip);
	}
	public final void setTip(String value)  
	 {
		this.SetValByKey(NodeAttr.Tip, value);
	}
	/** 
	 焦点字段
	*/
	public final String getFocusField() 
	{
		return this.GetValStrByKey(NodeAttr.FocusField);
	}
	public final void setFocusField(String value)  
	 {
		this.SetValByKey(NodeAttr.FocusField, value);
	}
	/** 
	 被退回节点退回信息地步.
	*/
	public final String getReturnAlert() 
	{
		return this.GetValStrByKey(NodeAttr.ReturnAlert);
	}
	public final void setReturnAlert(String value)  
	 {
		this.SetValByKey(NodeAttr.ReturnAlert, value);
	}

	public final boolean getReturnCHEnable() 
	{
		return this.GetValBooleanByKey(NodeAttr.ReturnCHEnable);
	}
	public final void setReturnCHEnable(boolean value)  
	 {
		this.SetValByKey(NodeAttr.ReturnCHEnable, value);
	}
	/** 
	 退回原因
	*/
	public final String getReturnReasonsItems() 
	{
		return this.GetValStrByKey(NodeAttr.ReturnReasonsItems);
	}
	/** 
	 节点的事务编号
	*/
	public final String getFK_Flow() 
	{
		return this.GetValStrByKey(NodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{SetValByKey(NodeAttr.FK_Flow, value);
	}
	/** 
	 获取它的上一步的分流点
	*/
	private Node _GetHisPriFLNode(Nodes nds) throws Exception {
		for (Node mynd : nds.ToJavaList())
		{
			if (mynd.isFL())
			{
				return mynd;
			}
			else
			{
				return _GetHisPriFLNode(mynd.getFromNodes());
			}
		}
		return null;
	}
	/** 
	 它的上一步分流节点
	*/
	public final Node getHisPriFLNode() throws Exception {
		return _GetHisPriFLNode(this.getFromNodes());
	}
	public final String getTurnToDealDoc()  {
		String s = this.GetValStrByKey(NodeAttr.TurnToDealDoc);
		if (this.getHisTurnToDeal() == TurnToDeal.SpecUrl)
		{
			if (s.contains("1=1"))
			{
				return s;
			}
			if (s.contains("?"))
			{
				s += "&1=1";
			}
			else
			{
				s += "?1=1";
			}
		}
		s = s.replace("~", "'");
		return s;
	}
	public final void setTurnToDealDoc(String value)
	{SetValByKey(NodeAttr.TurnToDealDoc, value);
	}
	/** 
	 可跳转的节点
	*/
	public final String getJumpToNodes() 
	{
		return this.GetValStrByKey(NodeAttr.JumpToNodes);
	}
	public final void setJumpToNodes(String value)
	{SetValByKey(NodeAttr.JumpToNodes, value);
	}

	public final JumpWay getJumpWay()  {
		return JumpWay.forValue(this.GetValIntByKey(NodeAttr.JumpWay));
	}
	/** 
	 工作ID
	*/
	public long WorkID = 0;
	/** 
	 节点表单ID
	*/
	public final String getNodeFrmID() throws Exception {
		String str = this.GetValStrByKey(NodeAttr.NodeFrmID);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return "ND" + this.getNodeID();
		}

		if (this.getHisFormType() == NodeFormType.FoolTruck || this.getHisFormType() == NodeFormType.SheetTree)
		{
			return "ND" + this.getNodeID();
		}

			//与指定的节点相同 =  Pri 
		if (str.equals("Pri") == true && (this.getHisFormType() == NodeFormType.FoolForm || this.getHisFormType() == NodeFormType.Develop))
		{
			if (this.WorkID == 0)
			{
				return "ND" + this.getNodeID();
			}
				// throw new Exception("err@获得当前节点的上一个节点表单出现错误,没有给参数WorkID赋值.");

				/* 要引擎上一个节点表单 */
			String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A, WF_Node B ";
			sql += " WHERE A.NDFrom=B.NodeID AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.Start.getValue() + ")  ";
			sql += "  AND (FormType=0 OR FormType=1) ";

			sql += "  AND (A.WorkID=" + this.WorkID + ") ";

			sql += " ORDER BY A.RDT ";

			int nodeID = DBAccess.RunSQLReturnValInt(sql, 0);
			if (nodeID == 0)
			{
				throw new RuntimeException("err@没有找到指定的节点.");
			}

			return "ND" + nodeID;
		}

			//返回设置的表单ID.
		return str;
	}
	public final void setNodeFrmID(String value)
	{SetValByKey(NodeAttr.NodeFrmID, value);
	}

	public final String getFlowName() 
	{
		return this.GetValStrByKey(NodeAttr.FlowName);
	}
	public final void setFlowName(String value)
	{SetValByKey(NodeAttr.FlowName, value);
	}
	/** 
	 打印方式
	*/
	public final boolean getHisPrintDocEnable() 
	{
		return this.GetValBooleanByKey(NodeAttr.PrintDocEnable);
	}
	public final void setHisPrintDocEnable(boolean value)  
	 {
		this.SetValByKey(NodeAttr.PrintDocEnable, value);
	}


	/** 
	 PDF打印规则
	*/
	public final int getHisPrintPDFModle() 
	{
		return this.GetValIntByKey(BtnAttr.PrintPDFModle);
	}
	public final void setHisPrintPDFModle(int value)  
	 {
		this.SetValByKey(BtnAttr.PrintPDFModle, (int)value);
	}
	/** 
	 打印水印设置规则
	*/
	public final String getShuiYinModle() 
	{
		return this.GetValStringByKey(BtnAttr.ShuiYinModle);
	}
	public final void setShuiYinModle(String value)  
	 {
		this.SetValByKey(BtnAttr.ShuiYinModle, value);
	}
	/** 
	 批处理规则
	*/
	public final BatchRole getHisBatchRole()  {
		return BatchRole.forValue(this.GetValIntByKey(NodeAttr.BatchRole));
	}
	public final void setHisBatchRole(BatchRole value)  
	 {
		this.SetValByKey(NodeAttr.BatchRole, value.getValue());
	}
	/** 
	 批量处理规则
	 @显示的字段.
	*/
	public final String getBatchParas()  {
		return this.GetParaString("BatchFields");
	}
	public final void setBatchParas(String value)
	{this.SetPara("BatchFields", value);
	}
	/** 
	 批量审核数量
	*/
	public final int getBatchListCount()  {
		return this.GetParaInt("BatchCheckListCount", 0);
	}
	public final void setBatchListCount(int value)
	{this.SetPara("BatchCheckListCount", value);
	}
	public final String getPTable()  {

		return "ND" + this.getNodeID();
	}
	public final void setPTable(String value)
	{SetValByKey(NodeAttr.PTable, value);
	}

	/** 
	 Doc
	*/
	public final String getDoc() 
	{
		return this.GetValStrByKey(NodeAttr.Doc);
	}
	public final void setDoc(String value)
	{SetValByKey(NodeAttr.Doc, value);
	}
	public final String getGroupStaNDs() 
	{
		return this.GetValStrByKey(NodeAttr.GroupStaNDs);
	}
	public final void setGroupStaNDs(String value)  
	 {
		this.SetValByKey(NodeAttr.GroupStaNDs, value);
	}
	/** 
	 到达的节点数量.
	*/
	public final int getHisToNDNum()  {
		String[] strs = this.getHisToNDs().split("[@]", -1);
		return strs.length - 1;
	}
	/** 
	 到达的节点
	*/
	public final String getHisToNDs() 
	{
		return this.GetValStrByKey(NodeAttr.HisToNDs);
	}
	public final void setHisToNDs(String value)
	 {
		this.SetValByKey(NodeAttr.HisToNDs, value);
	}
	/** 
	 部门Strs
	*/
	public final String getHisDeptStrs() 
	{
		return this.GetValStrByKey(NodeAttr.HisDeptStrs);
	}
	public final void setHisDeptStrs(String value)  
	 {
		this.SetValByKey(NodeAttr.HisDeptStrs, value);
	}
	public final String getHisStas() 
	{
		return this.GetValStrByKey(NodeAttr.HisStas);
	}
	public final void setHisStas(String value)  
	 {
		this.SetValByKey(NodeAttr.HisStas, value);
	}
	/** 
	 单据IDs
	*/
	public final String getHisBillIDs() 
	{
		return this.GetValStrByKey(NodeAttr.HisBillIDs);
	}
	public final void setHisBillIDs(String value)  
	 {
		this.SetValByKey(NodeAttr.HisBillIDs, value);
	}

		///#endregion


		///#region 退回信息.
	public final String getReturnField() 
	{
		return this.GetValStrByKey(BtnAttr.ReturnField);
	}
	public final void setReturnField(String value)  
	 {
		this.SetValByKey(BtnAttr.ReturnField, value);
	}
	/** 
	 单节点退回规则
	*/
	public final int getReturnOneNodeRole() 
	{
		return this.GetValIntByKey(NodeAttr.ReturnOneNodeRole);
	}
	public final void setReturnOneNodeRole(int value)  
	 {
		this.SetValByKey(NodeAttr.ReturnOneNodeRole, value);
	}

		///#endregion



		///#region 公共方法
	/** 
	 得到一个工作data实体
	 
	 param workId 工作ID
	 @return 如果没有就返回null
	*/
	public final Work GetWork(long workId) throws Exception {
		Work wk = this.getHisWork();
		wk.SetValByKey("OID", workId);
		if (wk.RetrieveFromDBSources() == 0)
		{
			return null;
		}
		else
		{
			return wk;
		}

	}

		///#endregion


		///#region 节点的工作类型
	/** 
	 转向处理
	*/
	public final TurnToDeal getHisTurnToDeal()  {
		return TurnToDeal.forValue(this.GetValIntByKey(NodeAttr.TurnToDeal));
	}
	public final void setHisTurnToDeal(TurnToDeal value)  
	 {
		this.SetValByKey(NodeAttr.TurnToDeal, value.getValue());
	}
	/** 
	 访问规则
	*/
	public final DeliveryWay getHisDeliveryWay()  {
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}
	public final void setHisDeliveryWay(DeliveryWay value)  
	 {
		this.SetValByKey(NodeAttr.DeliveryWay, value.getValue());
	}
	/** 
	 访问规则Text
	*/
	public final String getHisDeliveryWayText() throws Exception {
		SysEnum se = new SysEnum("DeliveryWay", this.getHisDeliveryWay().getValue());
		return se.getLab();
	}
	/** 
	 考核规则
	*/
	public final CHWay getHisCHWay()  {
		return CHWay.forValue(this.GetValIntByKey(NodeAttr.CHWay));
	}
	public final void setHisCHWay(CHWay value)  
	 {
		this.SetValByKey(NodeAttr.CHWay, value.getValue());
	}
	/** 
	 抄送规则
	*/
	public final CCRole getHisCCRole()  {
		return CCRole.forValue(this.GetValIntByKey(NodeAttr.CCRole));
	}
	public final void setHisCCRole(CCRole value)  
	 {
		this.SetValByKey(BtnAttr.CCRole, value.getValue());
	}
	public final String getHisCCRoleText() throws Exception {
		SysEnum se = new SysEnum(NodeAttr.CCRole, this.getHisCCRole().getValue());
		return se.getLab();
	}
	/** 
	 删除流程规则
	*/
	public final DelWorkFlowRole getHisDelWorkFlowRole()  {
		return DelWorkFlowRole.forValue(this.GetValIntByKey(BtnAttr.DelEnable));
	}
	/** 
	 未找到处理人时是否跳转.
	*/
	public final boolean getHisWhenNoWorker() 
	{
		return this.GetValBooleanByKey(NodeAttr.WhenNoWorker);
	}
	public final void setHisWhenNoWorker(boolean value)  
	 {
		this.SetValByKey(NodeAttr.WhenNoWorker, value);
	}
	/** 
	 撤销规则
	*/
	public final CancelRole getHisCancelRole()  {
		return CancelRole.forValue(this.GetValIntByKey(NodeAttr.CancelRole));
	}
	public final void setHisCancelRole(CancelRole value)  
	 {
		this.SetValByKey(NodeAttr.CancelRole, value.getValue());
	}
	/** 
	 对方已读不能撤销
	*/
	public final boolean getCancelDisWhenRead() 
	{
		return this.GetValBooleanByKey(NodeAttr.CancelDisWhenRead);
	}
	public final void setCancelDisWhenRead(boolean value)  
	 {
		this.SetValByKey(NodeAttr.CancelDisWhenRead, value);
	}
	/** 
	 数据写入规则
	*/
	public final CCWriteTo getCCWriteTo()  {
		return CCWriteTo.forValue(this.GetValIntByKey(NodeAttr.CCWriteTo));
	}
	public final void setCCWriteTo(CCWriteTo value)  
	 {
		this.SetValByKey(NodeAttr.CCWriteTo, value.getValue());
	}

	/**
	 按照岗位智能获取人员模式
	 0=集合模式,1=切片-严谨模式. 2=切片-宽泛模式
	 */
	public final int getDeliveryStationReqEmpsWay()
	{
		int val = this.GetParaInt("StationReqEmpsWay", 0);
		return val;
	}

	/** 
	 Int type
	*/
	public final NodeWorkType getHisNodeWorkType()  {
			///#warning 2012-01-24修订,没有自动计算出来属性。
		switch (this.getHisRunModel())
		{
			case Ordinary:
				if (this.isStartNode())
				{
					return NodeWorkType.StartWork;
				}
				else
				{
					return NodeWorkType.Work;
				}
			case FL:
				if (this.isStartNode())
				{
					return NodeWorkType.StartWorkFL;
				}
				else
				{
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
	public final void setHisNodeWorkType(NodeWorkType value)  
	 {
		this.SetValByKey(NodeAttr.NodeWorkType, value.getValue());
	}
	public final String getHisRunModelT() throws Exception {
		SysEnum se = new SysEnum(NodeAttr.RunModel, this.getHisRunModel().getValue());
		return se.getLab();
	}

		///#endregion


		///#region 推算属性 (对于节点位置的判断)
	/** 
	 类型
	*/
	public final NodePosType getHisNodePosType() throws Exception {
		this.SetValByKey(NodeAttr.NodePosType, this.GetHisNodePosType().getValue());
		return NodePosType.forValue(this.GetValIntByKey(NodeAttr.NodePosType));
	}
	public final void setHisNodePosType(NodePosType value)
	{if (value == NodePosType.Start)
		{
			if (!this.getNo().equals("01"))
			{
				value = NodePosType.Mid;
			}
		}

		this.SetValByKey(NodeAttr.NodePosType, value.getValue());
	}
	/** 
	 是不是结束节点
	*/
	public final boolean isEndNode() throws Exception {
		if (this.isSendBackNode() == true)
		{
			return false;
		}

		if (this.getHisNodePosType() == NodePosType.End)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 异表单子线程WorkID生成规则
	*/
	public final int getUSSWorkIDRole() 
	{
		return this.GetValIntByKey(NodeAttr.USSWorkIDRole);
	}
	public final void setUSSWorkIDRole(int value)  
	 {
		this.SetValByKey(NodeAttr.USSWorkIDRole, value);
	}
	/** 
	 是否可以在退回后原路返回？
	*/
	public final boolean isBackTracking() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsBackTracking);
	}
	/** 
	 原路返回后是否自动计算接收人
	*/
	public final boolean isBackResetAccepter() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsBackResetAccepter);
	}
	public final boolean isSendDraftSubFlow() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsSendDraftSubFlow);
	}
	/** 
	 是否杀掉全部的子线程
	*/
	public final boolean getThreadIsCanDel() 
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanDel);
	}

	public final boolean getThreadIsCanAdd() 
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanAdd);
	}
	/** 
	 是否启用自动记忆功能
	*/
	public final boolean isRememberMe() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsRM);
	}
	public final void setRememberMe(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsRM, value);
	}
	/** 
	 是否打开即审批
	*/
	public final boolean isOpenOver() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsOpenOver);
	}
	public final void setOpenOver(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsOpenOver, value);
	}
	/** 
	 是否可以删除
	*/
	public final boolean isCanDelFlow() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsCanDelFlow);
	}
	/** 
	 普通工作节点处理模式
	*/
	public final TodolistModel getTodolistModel()  {
		if (this.isStartNode() == true)
		{
			return bp.wf.TodolistModel.QiangBan;
		}

		return TodolistModel.forValue(this.GetValIntByKey(NodeAttr.TodolistModel));
	}
	public final void setTodolistModel(TodolistModel value)  
	 {
		this.SetValByKey(NodeAttr.TodolistModel, value.getValue());
	}
	/** 
	 组长确认规则
	*/
	public final TeamLeaderConfirmRole getTeamLeaderConfirmRole()  {
		return TeamLeaderConfirmRole.forValue(this.GetValIntByKey(NodeAttr.TeamLeaderConfirmRole));
	}
	public final void setTeamLeaderConfirmRole(TeamLeaderConfirmRole value)  
	 {
		this.SetValByKey(NodeAttr.TeamLeaderConfirmRole, value.getValue());
	}
	/** 
	 组长确认规则内容.
	*/
	public final String getTeamLeaderConfirmDoc() 
	{
		return this.GetValStringByKey(NodeAttr.TeamLeaderConfirmDoc);
	}
	public final void setTeamLeaderConfirmDoc(String value)  
	 {
		this.SetValByKey(NodeAttr.TeamLeaderConfirmDoc, value);
	}
	public final HuiQianLeaderRole getHuiQianLeaderRole()  {
		return HuiQianLeaderRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianLeaderRole));
	}
	public final void setHuiQianLeaderRole(HuiQianLeaderRole value)  
	 {
		this.SetValByKey(BtnAttr.HuiQianLeaderRole, value.getValue());
	}
	/** 
	 阻塞模式
	*/
	public final BlockModel getBlockModel()  {
		return BlockModel.forValue(this.GetValIntByKey(NodeAttr.BlockModel));
	}
	public final void setBlockModel(BlockModel value)  
	 {
		this.SetValByKey(NodeAttr.BlockModel, value.getValue());
	}
	/** 
	 阻塞的表达式
	*/
	public final String getBlockExp()  {
		String str = this.GetValStringByKey(NodeAttr.BlockExp);

		if (DataType.IsNullOrEmpty(str))
		{
			if (this.getBlockModel() == bp.wf.BlockModel.CurrNodeAll)
			{
				return "还有子流程没有完成您不能提交,需要等到所有的子流程完成后您才能发送.";
			}

			if (this.getBlockModel() == bp.wf.BlockModel.SpecSubFlow)
			{
				return "还有子流程没有完成您不能提交,需要等到所有的子流程完成后您才能发送.";
			}
		}
		return str;
	}
	public final void setBlockExp(String value)  
	 {
		this.SetValByKey(NodeAttr.BlockExp, value);
	}
	/** 
	 被阻塞时提示信息
	*/
	public final String getBlockAlert() 
	{
		return this.GetValStringByKey(NodeAttr.BlockAlert);
	}
	public final void setBlockAlert(String value)  
	 {
		this.SetValByKey(NodeAttr.BlockAlert, value);
	}
	/** 
	 子线程删除规则
	*/
	public final ThreadKillRole getThreadKillRole()  {
		return ThreadKillRole.forValue(this.GetValIntByKey(NodeAttr.ThreadKillRole));
	}
	/** 
	 完成通过率
	*/
	public final BigDecimal getPassRate()  {
		BigDecimal val = this.GetValDecimalByKey(NodeAttr.PassRate, 4);
		if (val.intValue() == 0) {
			return new BigDecimal(100);
		}
		return val;
	}
	/** 
	 是否允许分配工作
	*/
	public final boolean isTask() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsTask);
	}
	public final void setTask(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsTask, value);
	}
	///// <summary>
	///// 是否是业务单元
	///// </summary>
	//public bool IsBUnit
	//{
	//    get
	//    {
	//        return this.GetValBooleanByKey(NodeAttr.IsBUnit);
	//    }
	//    set
	//    {
	//        this.SetValByKey(NodeAttr.IsBUnit, value);
	//    }
	//}
	/** 
	 是否可以移交
	*/
	public final boolean isHandOver()  {
		if (this.isStartNode())
		{
			return false;
		}

		return this.GetValBooleanByKey(NodeAttr.IsHandOver);
	}
	public final void setHandOver(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsHandOver, value);
	}
	/** 
	 是否可以退回？
	*/
	public final boolean isCanReturn()  {
		if (this.getHisReturnRole() == ReturnRole.CanNotReturn)
		{
			return false;
		}
		return true;
	}
	/** 
	 已读回执
	*/
	public final ReadReceipts getReadReceipts()  {
		return ReadReceipts.forValue(this.GetValIntByKey(NodeAttr.ReadReceipts));
	}
	public final void setReadReceipts(ReadReceipts value)  
	 {
		this.SetValByKey(NodeAttr.ReadReceipts, value.getValue());
	}
	/** 
	 退回规则
	*/
	public final ReturnRole getHisReturnRole()  {
		return ReturnRole.forValue(this.GetValIntByKey(NodeAttr.ReturnRole));
	}
	public final void setHisReturnRole(ReturnRole value)  
	 {
		this.SetValByKey(NodeAttr.ReturnRole, value.getValue());
	}
	/** 
	 是不是中间节点
	*/
	public final boolean isMiddleNode() throws Exception {
		if (this.getHisNodePosType() == NodePosType.Mid)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 是否是工作质量考核点
	*/
	public final boolean isEval() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsEval);
	}
	public final void setEval(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsEval, value);
	}
	public final String getHisSubFlows11() 
	{
		return this.GetValStringByKey(NodeAttr.HisSubFlows);
	}
	public final void setHisSubFlows11(String value)  
	 {
		this.SetValByKey(NodeAttr.HisSubFlows, value);
	}
	public final String getFrmAttr() 
	{
		return this.GetValStringByKey(NodeAttr.FrmAttr);
	}
	public final void setFrmAttr(String value)  
	 {
		this.SetValByKey(NodeAttr.FrmAttr, value);
	}
	public final boolean isHL()  {
		switch (this.getHisNodeWorkType())
		{
			case WorkHL:
			case WorkFHL:
				return true;
			default:
				return false;
		}
	}
	/** 
	 是否是分流
	*/
	public final boolean isFL()  {
		switch (this.getHisNodeWorkType())
		{
			case WorkFL:
			case WorkFHL:
			case StartWorkFL:
				return true;
			default:
				return false;
		}
	}
	/** 
	 是否分流合流
	*/
	public final boolean isFLHL()  {
		switch (this.getHisNodeWorkType())
		{
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
	 接受人sql
	*/
	public final String getDeliveryParas()  {
		String s = this.GetValStringByKey(NodeAttr.DeliveryParas);
		s = s.replace("~", "'");

		if (this.getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormEmpsField && DataType.IsNullOrEmpty(s) == true)
		{
			return "ToEmps";
		}
		return s;
	}
	public final void setDeliveryParas(String value)  
	 {
		this.SetValByKey(NodeAttr.DeliveryParas, value);
	}
	/** 
	 接受人员集合里,是否排除当前操作员?
	*/
	public final boolean isExpSender() 
	{
		return this.GetValBooleanByKey(NodeAttr.IsExpSender);
	}
	public final void setExpSender(boolean value)  
	 {
		this.SetValByKey(NodeAttr.IsExpSender, value);
	}
	/** 
	 是不是PC工作节点
	*/
	public final boolean isPCNode()  {
		return false;
	}
	/** 
	 工作性质
	*/
	public final String getNodeWorkTypeText()  {
		return this.getHisNodeWorkType().toString();
	}


	public final int getCHRole() 
	{
		return this.GetValIntByKey(BtnAttr.CHRole);
	}
	public final void setCHRole(int value)  
	 {
		this.SetValByKey(BtnAttr.CHRole, value);
	}

		///#endregion


		///#region 公共方法 (用户执行动作之后,所要做的工作)
	/** 
	 用户执行动作之后,所要做的工作		 
	 
	 @return 返回消息,运行的消息
	*/
	public final String AfterDoTask()  {
		return "";
	}

		///#endregion


		///#region 构造函数
	/** 
	 节点
	*/
	public Node()  {
	}
	/** 
	 节点
	 
	 param _oid 节点ID
	*/
	public Node(int _oid) throws Exception {
		this.setNodeID(_oid);
		this.Retrieve();

		//if (bp.difference.SystemConfig.getIsDebug())
		//{
		//    if (this.RetrieveFromDBSources() <= 0)
		//        throw new Exception("Node Retrieve 错误没有ID=" + _oid);
		//}
		//else
		//{
		//    // 去掉缓存.
		//    int i = this.RetrieveFromDBSources();
		//    if (i == 0)
		//    {
		//        string err = "err@NodeID=" + this.NodeID + "不存在";
		//        err += "可能出现错误的原因如下:";
		//        err += "1.你在FEE中或者SDK模式中使用了节点跳转,跳转到的节点已经不存在.";
		//        throw new Exception(err);
		//    }

		//    //if (this.Retrieve() <= 0)
		//    //    throw new Exception("Node Retrieve 错误没有ID=" + _oid);
		//}
	}
	public Node(String ndName) throws Exception {
		ndName = ndName.replace("ND", "");
		this.setNodeID(Integer.parseInt(ndName));

		if (bp.difference.SystemConfig.getIsDebug())
		{
			if (this.RetrieveFromDBSources() <= 0)
			{
				throw new RuntimeException("Node Retrieve 错误没有ID=" + ndName);
			}
		}
		else
		{
			if (this.Retrieve() <= 0)
			{
				throw new RuntimeException("Node Retrieve 错误没有ID=" + ndName);
			}
		}
	}
	public final String getEnName()  {
		return "ND" + this.getNodeID();
	}
	public final String getEnsName()  {
		return "ND" + this.getNodeID() + "s";
	}
	/** 
	 节点意见名称，如果为空则取节点名称.
	*/
	public final String getFWCNodeName()  {
		String str = this.GetValStringByKey(NodeWorkCheckAttr.FWCNodeName);
		if (DataType.IsNullOrEmpty(str))
		{
			return this.getName();
		}
		return str;
	}
	/** 
	 审核组件里面的工作人员先后顺序排列模式
	 0= 按照审批时间.
	 1= 按照接受人员列表(官职大小)
	*/
	public final int getFWCOrderModel() 
	{
		return this.GetValIntByKey(NodeWorkCheckAttr.FWCOrderModel);
	}
	public final float getFwcH() 
	{
		return this.GetValFloatByKey(NodeWorkCheckAttr.FWC_H);
	}

	public final HuiQianRole getHuiQianRole()  {
		return HuiQianRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianRole));
	}
	public final void setHuiQianRole(HuiQianRole value)  
	 {
		this.SetValByKey(BtnAttr.HuiQianRole, value.getValue());
	}

	public final boolean isResetAccepter()
	{
		return this.GetValBooleanByKey(NodeAttr.IsResetAccepter);
	}

	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "节点");

			//出现 缓存问题.现在把缓存取消了.
		map.setDepositaryOfEntity( Depositary.Application);
		map.setDepositaryOfMap( Depositary.Application);


			///#region 基本属性.
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

			//为铁路局,会签子流程. 增加
		map.AddTBInt(NodeAttr.IsSendDraftSubFlow, 0, "是否发送草稿子流程？", false, false);
		map.AddTBInt(NodeAttr.IsResetAccepter, 0, "可逆节点时是否重新计算接收人", false, false);

			//map.AddTBString(NodeAttr.FK_FlowSort, null, "FK_FlowSort", false, true, 0, 4, 10);
			//map.AddTBString(NodeAttr.FK_FlowSortT, null, "FK_FlowSortT", false, true, 0, 100, 10);

		map.AddTBString(NodeAttr.FrmAttr, null, "FrmAttr", false, true, 0, 300, 10);
			//  map.AddTBInt(NodeAttr.IsBUnit, 0, "是否是节点模版(业务单元)", true, false);

			///#endregion 基本属性.


			///#region 审核组件.
		map.AddTBInt(NodeAttr.FWCSta, 0, "审核组件", false, false);
		map.AddTBFloat(NodeAttr.FWC_H, 0, "审核组件高度", false, true);
		map.AddTBInt(NodeWorkCheckAttr.FWCOrderModel, 0, "协作模式下操作员显示顺序", false, false);
		map.AddTBInt(NodeWorkCheckAttr.FWCVer, 0, "审核组件版本", false, false);
		map.AddTBInt("FWCAth", 0, "审核附件是否启用", false, false);
		map.AddTBString(NodeWorkCheckAttr.CheckField, null, "签批字段", true, false, 0, 50, 10, false);
		map.AddTBString(NodeWorkCheckAttr.FWCDefInfo, null, "默认意见", true, false, 0, 50, 10);

			///#endregion 审核组件.


			///#region 子流程信息
		map.AddTBInt(FrmSubFlowAttr.SFSta, 0, "父子流程组件", false, false);
		map.AddTBInt(NodeAttr.SubFlowX, 0, "子流程设计器位置X", false, false);
		map.AddTBInt(NodeAttr.SubFlowY, 0, "子流程设计器位置Y", false, false);
			///#endregion 子流程信息


			///#region 考核属性.
		map.AddTBString(BtnAttr.CHLab, "节点时限", "节点时限", true, false, 0, 50, 10);
		map.AddTBInt(BtnAttr.CHRole, 0, "时限规则", true, false);

		map.AddTBString(BtnAttr.HelpLab, "帮助提示", "帮助", true, false, 0, 50, 10);
		map.AddTBInt(BtnAttr.HelpRole, 0, "帮助提示规则", true, false);

		map.AddTBFloat(NodeAttr.TimeLimit, 2, "限期(天)", true, false); //"限期(天)".
																		 //  map.AddTBFloat(NodeAttr.TSpanHour, 0, "小时", true, false); //"限期(分钟)".
		map.AddTBInt(NodeAttr.TWay, 0, "时间计算方式", true, false); //0=不计算节假日,1=计算节假日.

		map.AddTBInt(NodeAttr.TAlertRole, 0, "逾期提醒规则", false, false); //"限期(天)"
		map.AddTBInt(NodeAttr.TAlertWay, 0, "逾期提醒方式", false, false); //"限期(天)"

		map.AddTBFloat(NodeAttr.WarningDay, 1, "工作预警(天)", true, false); // "警告期限(0不警告)"
			//  map.AddTBFloat(NodeAttr.WarningHour, 0, "工作预警(小时)", true, false); // "警告期限(0不警告)"
			//  map.SetHelperUrl(NodeAttr.WarningHour, "http://ccbpm.mydoc.io/?v=5404&t=17999");

		map.AddTBInt(NodeAttr.WAlertRole, 0, "预警提醒规则", false, false); //"限期(天)"
		map.AddTBInt(NodeAttr.WAlertWay, 0, "预警提醒方式", false, false); //"限期(天)"

		map.AddTBFloat(NodeAttr.TCent, 2, "扣分(每延期1小时)", false, false);
		map.AddTBInt(NodeAttr.CHWay, 0, "考核方式", false, false); //"限期(天)"

			//考核相关.
		map.AddTBInt(NodeAttr.IsEval, 0, "是否工作质量考核", true, true);
		map.AddTBInt(NodeAttr.OutTimeDeal, 0, "超时处理方式", false, false);
		map.AddTBString(NodeAttr.DoOutTime, null, "超时处理内容", true, false, 0, 300, 10, true);
			//map.AddTBString(NodeAttr.DoOutTime, null, "超时处理内容", true, false, 0, 300, 10, true);
		map.AddTBString(NodeAttr.DoOutTimeCond, null, "执行超时的条件", false, false, 0, 200, 100);

			///#endregion 考核属性.

		map.AddTBString(NodeWorkCheckAttr.FWCNodeName, null, "节点意见名称", true, false, 0, 100, 10);
		map.AddTBString(NodeAttr.Doc, null, "描述", true, false, 0, 100, 10);
		map.AddBoolean(NodeAttr.IsTask, true, "允许分配工作否?", true, true);

			//退回相关.
		map.AddTBInt(NodeAttr.ReturnRole, 2, "退回规则", true, true);
		map.AddTBString(NodeAttr.ReturnReasonsItems, null, "退回原因", true, false, 0, 50, 10, true);
		map.AddTBString(NodeAttr.ReturnAlert, null, "被退回后信息提示", true, false, 0, 50, 10, true);
		map.AddBoolean(NodeAttr.ReturnCHEnable, false, "是否启用退回考核规则", true, true);

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

			//add.
		map.AddTBInt(BtnAttr.HuiQianRole, 0, "会签模式", true, true);
		map.AddTBInt(NodeAttr.TeamLeaderConfirmRole, 0, "组长确认规则", true, true);
		map.AddTBString(NodeAttr.TeamLeaderConfirmDoc, null, "组长确认设置内容", true, false, 0, 100, 10);
		map.AddTBInt(BtnAttr.HuiQianLeaderRole, 0, "组长会签规则", true, true);

		map.AddTBInt(BtnAttr.ScripRole, 0, "小纸条规则", true, true);

		map.AddTBInt(NodeAttr.USSWorkIDRole, 0, "是否允许子线程接受人员重复(对子线程点有效)?", true, true);
		map.AddTBInt(NodeAttr.IsBackTracking, 1, "是否可以在退回后原路返回(只有启用退回功能才有效)", true, true);
		map.AddTBInt(NodeAttr.IsBackResetAccepter, 0, "原路返回后是否自动计算接收人", true, true);
		map.AddTBInt(BtnAttr.ThreadIsCanDel, 0, "是否可以删除子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true);
		map.AddTBInt(BtnAttr.ThreadIsCanAdd, 0, "是否可以增加子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效)？", true, true);

		map.AddTBInt(NodeAttr.IsKillEtcThread, 0, "是否允许删除所有的子线程(对于子线程向分流节点退回有效)", true, true);

		map.AddTBInt(NodeAttr.IsRM, 1, "是否启用投递路径自动记忆功能?", true, true);
		map.AddTBInt(NodeAttr.IsOpenOver, 0, "是否打开即审批?", true, true);
		map.AddBoolean(NodeAttr.IsHandOver, false, "是否可以移交", true, true);
		map.AddTBDecimal(NodeAttr.PassRate, new BigDecimal(100), "通过率", true, true);
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

		map.AddTBString(NodeAttr.AutoJumpExp, null, "表达式", true, false, 0, 200, 10, true);
			//@0=上一个节点发送时@1=当前节点工作打开时.
		map.AddTBInt(NodeAttr.SkipTime, 0, "执行跳转事件", false, false);


			// 批处理规则， 2021.1.20 为福建人寿重构.
			// @0=不启用，1=审核组件模式，2=审核分组字段模式,3=自定义url模式.
		map.AddTBInt(NodeAttr.BatchRole, 0, "批处理", true, true);

			//map.AddTBInt(NodeAttr.BatchListCount, 12, "批处理数量", true, true);
			//map.AddTBString(NodeAttr.BatchParas, null, "参数", true, false, 0, 500, 10);

		map.AddTBInt(NodeAttr.PrintDocEnable, 0, "打印方式", true, true);
			//打印PDF的处理
		map.AddTBInt(BtnAttr.PrintPDFModle, 0, "PDF打印规则", true, true);
		map.AddTBInt(BtnAttr.PRIEnable, 0, "重要性规则", true, true);
		map.AddTBString(BtnAttr.ShuiYinModle, null, "打印水印规则", true, false, 20, 100, 100, true);

			//与未来处理人有关系.
			//map.AddTBInt(NodeAttr.IsFullSA, 1, "是否计算未来处理人?", false, false);
			//map.AddTBInt(NodeAttr.IsFullSATime, 0, "是否计算未来接受与处理时间?", false, false);
			//map.AddTBInt(NodeAttr.IsFullSAAlert, 0, "是否接受未来工作到达消息提醒?", false, false);

			//表单相关.
		map.AddTBInt(NodeAttr.FormType, 1, "表单类型", false, false);
		map.AddTBString(NodeAttr.FormUrl, "http://", "表单URL", true, false, 0, 300, 10);
		map.AddTBInt(NodeAttr.TurnToDeal, 0, "转向处理", false, false);
		map.AddTBString(NodeAttr.TurnToDealDoc, null, "发送后提示信息", true, false, 0, 200, 10, true);
		map.AddTBInt(NodeAttr.NodePosType, 0, "位置", false, false);
		map.AddTBString(NodeAttr.HisStas, null, "岗位", false, false, 0, 300, 10);
		map.AddTBString(NodeAttr.HisDeptStrs, null, "部门", false, false, 0, 300, 10);
		map.AddTBString(NodeAttr.HisToNDs, null, "转到的节点", false, false, 0, 50, 10);
		map.AddTBString(NodeAttr.HisBillIDs, null, "单据IDs", false, false, 0, 50, 10);
			//  map.AddTBString(NodeAttr.HisEmps, null, "HisEmps", false, false, 0, 3000, 10);
		map.AddTBString(NodeAttr.HisSubFlows, null, "HisSubFlows", false, false, 0, 30, 10);
		map.AddTBString(NodeAttr.PTable, null, "物理表", false, false, 0, 100, 10);

		map.AddTBString(NodeAttr.GroupStaNDs, null, "岗位分组节点", false, false, 0, 200, 10);
		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

		map.AddTBString(NodeAttr.FocusField, null, "焦点字段", false, false, 0, 30, 10);
		map.AddTBString(NodeAttr.JumpToNodes, null, "可跳转的节点", true, false, 0, 100, 10, true);
		map.AddTBInt(NodeAttr.JumpWay, 0, "跳转规则", false, false);
		map.AddTBString(NodeAttr.RefOneFrmTreeType, "", "独立表单类型", false, false, 0, 100, 10); //RefOneFrmTree

		map.AddTBString(NodeAttr.DoOutTimeCond, null, "执行超时的条件", false, false, 0, 200, 100);

			//按钮控制部分.
			// map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0, 50, 10, true);
		map.AddTBAtParas(500);

			// 启动子线程参数 2013-01-04
		map.AddTBInt(NodeAttr.SubFlowStartWay, 0, "子线程启动方式", true, false);
		map.AddTBString(NodeAttr.SubFlowStartParas, null, "启动参数", true, false, 0, 100, 10);

			// 启动自动运行. 2013-01-04
		map.AddTBInt(NodeAttr.AutoRunEnable, 0, "是否启动自动运行？", true, false);
		map.AddTBString(NodeAttr.AutoRunParas, null, "自动运行参数", true, false, 0, 100, 10);

		map.AddTBString(NodeAttr.SelfParas, null, "自定义参数(如果太小可以手动扩大)", true, false, 0, 1000, 10);


			///#region 与参数有关系的属性。


			///#region 子流程相关的参数
		map.AddTBFloat(FrmSubFlowAttr.SF_H, 300, "高度", true, false);

			///#endregion 子流程相关的参数

			//map.AddDDLSysEnum(FrmEventAttr.MsgCtrl, 0, "消息发送控制", true, true, FrmEventAttr.MsgCtrl,
			//  "@0=不发送@1=按设置的发送范围自动发送@2=由本节点表单系统字段(IsSendEmail,IsSendSMS)来决定@3=由SDK开发者参数(IsSendEmail,IsSendSMS)来决定", true);

			//map.AddBoolean(FrmEventAttr.MailEnable, true, "是否启用邮件发送？(如果启用就要设置邮件模版，支持ccflow表达式。)", true, true, true);
			//map.AddTBString(FrmEventAttr.MailTitle, null, "邮件标题模版", true, false, 0, 200, 20, true);
			//map.AddTBStringDoc(FrmEventAttr.MailDoc, null, "邮件内容模版", true, false, true);

			////是否启用手机短信？
			//map.AddBoolean(FrmEventAttr.SMSEnable, false, "是否启用短信发送？(如果启用就要设置短信模版，支持ccflow表达式。)", true, true, true);
			//map.AddTBStringDoc(FrmEventAttr.SMSDoc, null, "短信内容模版", true, false, true);
			//map.AddBoolean(FrmEventAttr.MobilePushEnable, true, "是否推送到手机、pad端。", true, true, true);

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 删除前的逻辑处理.
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete() throws Exception {
		//检查设计流程权限,集团模式下，不是自己创建的流程，不能设计流程.
		TemplateGlo.CheckPower(this.getFK_Flow());

		int num = 0;
		//如果是结束节点，则自动结束流程
		if (this.getNodePosType() == NodePosType.End)
		{
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve("FK_Flow", this.getFK_Flow(), null);
			for (GenerWorkFlow gwf :  gwfs.ToJavaList())
			{
				try
				{
					bp.wf.Dev2Interface.Flow_DoFlowOver(gwf.getWorkID(), "流程成功结束");
				}
				catch (RuntimeException ex)
				{
					//删除错误，有可能是删除该流程.
					continue;
				}
			}
		}
		//判断是否可以被删除. 
		num = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerlist WHERE FK_Node=" + this.getNodeID() + " AND IsPass=0 ");
		if (num != 0)
		{
			throw new RuntimeException("@该节点[" + this.getNodeID() + "," + this.getName() + "]有待办工作存在，您不能删除它.");
		}

		// 删除它的节点。
		MapData md = new MapData();
		md.setNo("ND" + this.getNodeID());
		md.Delete();

		// 删除分组.
		GroupFields gfs = new GroupFields();
		gfs.Delete(GroupFieldAttr.FrmID, md.getNo());

		//删除它的明细。
		MapDtls dtls = new MapDtls(md.getNo());
		dtls.Delete();

		//删除框架
		MapFrames frams = new MapFrames(md.getNo());
		frams.Delete();

		// 删除扩展
		MapExts exts = new MapExts(md.getNo());
		exts.Delete();

		//删除节点与岗位的对应.
		DBAccess.RunSQL("DELETE FROM WF_NodeStation WHERE FK_Node=" + this.getNodeID());
		DBAccess.RunSQL("DELETE FROM WF_NodeEmp  WHERE FK_Node=" + this.getNodeID());
		DBAccess.RunSQL("DELETE FROM WF_NodeDept WHERE FK_Node=" + this.getNodeID());
		DBAccess.RunSQL("DELETE FROM WF_FrmNode  WHERE FK_Node=" + this.getNodeID());
		DBAccess.RunSQL("DELETE FROM WF_CCEmp  WHERE FK_Node=" + this.getNodeID());
		DBAccess.RunSQL("DELETE FROM WF_CH  WHERE FK_Node=" + this.getNodeID());

		//删除附件.
		DBAccess.RunSQL("DELETE FROM Sys_FrmAttachment  WHERE FK_MapData='" + this.getNodeID() + "'");

		//删除节点后，把关联该节点表单的ID也要删除掉. 同步过去.
		DBAccess.RunSQL("UPDATE WF_Node SET NodeFrmID='' WHERE NodeFrmID='ND" + this.getNodeID() + "' AND FK_Flow='" + this.getFK_Flow() + "'");


		//写入日志.
		bp.sys.base.Glo.WriteUserLog("删除节点：" + this.getName() + " - " + this.getNodeID(), "通用操作");


		return super.beforeDelete();
	}

	/** 
	 修复map
	*/
	public final String RepareMap(Flow fl) throws Exception {
		MapData md = new MapData();
		md.setNo("ND" + this.getNodeID());
		if (md.RetrieveFromDBSources() == 0)
		{
			this.CreateMap();
			return "";
		}

		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, md.getNo(), null);


			///#region 增加节点必要的字段.
		MapAttr attr = new MapAttr();
		if (attrs.contains(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("WorkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal( "0");
			attr.setHisEditType( EditType.Readonly);
			attr.Insert();
		}

		if (this.getHisRunModel() != RunModel.SubThread)
		{
			return "修复成功.";
		}


		if (attrs.contains(MapAttrAttr.KeyOfEn, "FID", MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("FID");
			attr.setName("FID");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setEditType(EditType.UnDel);
			attr.setDefVal( "0");
			attr.Insert();
		}

		if (attrs.contains(MapAttrAttr.KeyOfEn, GERptAttr.RDT, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.RDT);
			attr.setName("接受时间"); //"接受时间";
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setTag ( "1");
			attr.Insert();
		}

		if (attrs.contains(MapAttrAttr.KeyOfEn, GERptAttr.CDT, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.CDT);
			if (this.isStartNode())
			{
				attr.setName("发起时间"); //"发起时间";
			}
			else
			{
				attr.setName("完成时间"); //"完成时间";
			}

			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal( "@RDT");
			attr.setTag ( "1");
			attr.Insert();
		}

		if (attrs.contains(MapAttrAttr.KeyOfEn, WorkAttr.Rec, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.Rec);
			if (this.isStartNode() == false)
			{
				attr.setName("记录人"); // "记录人";
			}
			else
			{
				attr.setName("发起人"); //"发起人";
			}

			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(32);
			attr.setMinLen(0);
			attr.setDefVal( "@WebUser.No");
			attr.Insert();
		}

		if (attrs.contains(MapAttrAttr.KeyOfEn, WorkAttr.Emps, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.Emps);
			attr.setName(WorkAttr.Emps);
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(8000);
			attr.setMinLen(0);
			attr.Insert();
		}

		if (attrs.contains(MapAttrAttr.KeyOfEn, GERptAttr.FK_Dept, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FK_Dept);
			attr.setName("操作员部门"); //"操作员部门";
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			attr.setUIBindKey("bp.port.Depts");
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.Insert();
		}


		if (fl.isMD5() && attrs.contains(MapAttrAttr.KeyOfEn, WorkAttr.MD5, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			/* 如果是MD5加密流程. */
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.MD5);
			attr.setUIBindKey(attr.getKeyOfEn());
			attr.setName("MD5");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setUIVisible(false);
			attr.setMinLen(0);
			attr.setMaxLen(40);
			attr.setIdx( -100);
			attr.Insert();
		}

		if (this.getNodePosType() == NodePosType.Start)
		{

			if (attrs.contains(MapAttrAttr.KeyOfEn, GERptAttr.Title, MapAttrAttr.FK_MapData, md.getNo()) == false)
			{
				attr = new MapAttr();
				attr.setFK_MapData(md.getNo());
				attr.setEditType(EditType.UnDel);
				attr.setKeyOfEn(GERptAttr.Title);
				attr.setName("标题"); // "流程标题";
				attr.setMyDataType(DataType.AppString);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(true);
				attr.setUIIsLine(true);
				attr.setUIWidth(251);

				attr.setMinLen(0);
				attr.setMaxLen(200);
				attr.setIdx( -100);
				//attr.X = (float)171.2;
				//attr.Y = (float)68.4;
				attr.Insert();
			}
		}

			///#endregion 增加节点必要的字段.

		//表单自检.
		md.RepairMap();

		String msg = "";
		if (!this.getFocusField().equals(""))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getFocusField(), MapAttrAttr.FK_MapData, md.getNo()) == false)
			{
				msg += "@焦点字段 " + this.getFocusField() + " 被非法删除了.";
			}
		}

		return msg;
	}
	/** 
	 建立map
	*/
	public final void CreateMap() throws Exception {
		//创建节点表单.
		MapData md = new MapData();
		md.setNo("ND" + this.getNodeID());
		md.Delete();

		md.setName( this.getName());

		if (this.getHisFormType() == NodeFormType.FoolForm || this.getHisFormType() == NodeFormType.FoolTruck)
		{
			md.setHisFrmType(FrmType.FoolForm);
		}

		if (this.getHisFormType() == NodeFormType.Develop)
		{
			md.setHisFrmType( FrmType.Develop);
		}

		if (this.getHisFlow().getHisDataStoreModel() == DataStoreModel.SpecTable)
		{
			md.setPTable ( this.getHisFlow().getPTable());
		}
		md.Insert();

		MapAttr attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("OID");
		attr.setName("WorkID");
		attr.setMyDataType(DataType.AppInt);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setDefVal( "0");
		attr.setHisEditType( EditType.Readonly);
		attr.Insert();


		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setKeyOfEn("FID");
		attr.setName("FID");
		attr.setMyDataType(DataType.AppInt);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setEditType(EditType.UnDel);
		attr.setDefVal( "0");
		attr.Insert();

		attr = new MapAttr();
		attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
		attr.setEditType(EditType.UnDel);
		attr.SetValByKey(MapAttrAttr.KeyOfEn, "RDT");
		attr.SetValByKey(MapAttrAttr.Name, "接受时间");
		attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppDateTime);
		attr.setUIContralType(UIContralType.TB);
		attr.SetValByKey(MapAttrAttr.UIVisible, false);
		attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
		attr.setLGType(FieldTypeS.Normal);
		attr.SetValByKey(MapAttrAttr.MinLen, 0);
		attr.SetValByKey(MapAttrAttr.MaxLen, 40);
		attr.SetValByKey(MapAttrAttr.DefVal, "@RDT");
		attr.SetValByKey(MapAttrAttr.Tag, "1");
		attr.Insert();

		attr = new MapAttr();
		attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
		attr.setEditType(EditType.UnDel);
		attr.SetValByKey(MapAttrAttr.KeyOfEn, "Rec");

		if (this.isStartNode() == false)
		{
			attr.SetValByKey(MapAttrAttr.Name, "发起时间");
		}
		else
		{
			attr.SetValByKey(MapAttrAttr.Name, "完成时间");
		}

		attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppDateTime);
		attr.setUIContralType(UIContralType.TB);
		attr.SetValByKey(MapAttrAttr.UIVisible, false);
		attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
		attr.setLGType(FieldTypeS.Normal);
		attr.SetValByKey(MapAttrAttr.MinLen, 0);
		attr.SetValByKey(MapAttrAttr.MaxLen, 40);
		attr.SetValByKey(MapAttrAttr.DefVal, "@RDT");
		attr.SetValByKey(MapAttrAttr.Tag, "1");
		attr.Insert();

		attr = new MapAttr();
		attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
		attr.setEditType(EditType.UnDel);
		attr.SetValByKey(MapAttrAttr.KeyOfEn, "Rec");

		if (this.isStartNode() == false)
		{
			attr.SetValByKey(MapAttrAttr.Name, "记录人");
		}
		else
		{
			attr.SetValByKey(MapAttrAttr.Name, "发起人");
		}

		attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.SetValByKey(MapAttrAttr.UIVisible, false);
		attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
		attr.setLGType(FieldTypeS.Normal);
		attr.SetValByKey(MapAttrAttr.MinLen, 0);
		attr.SetValByKey(MapAttrAttr.MaxLen, 8000);
		attr.SetValByKey(MapAttrAttr.DefVal, "@WebUser.No");
		attr.Insert();


		attr = new MapAttr();
		attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
		attr.setEditType(EditType.UnDel);
		attr.SetValByKey(MapAttrAttr.KeyOfEn, "Emps");
		attr.SetValByKey(MapAttrAttr.Name, "Emps");
		attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.SetValByKey(MapAttrAttr.UIVisible, false);
		attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
		attr.setLGType(FieldTypeS.Normal);
		attr.SetValByKey(MapAttrAttr.MinLen, 0);
		attr.SetValByKey(MapAttrAttr.MaxLen, 8000);
		attr.Insert();



		attr = new MapAttr();
		attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
		attr.setEditType(EditType.UnDel);
		attr.SetValByKey(MapAttrAttr.KeyOfEn, "FK_Dept");
		attr.SetValByKey(MapAttrAttr.Name, "操作员部门");
		attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.SetValByKey(MapAttrAttr.UIVisible, false);
		attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
		attr.setLGType(FieldTypeS.Normal);
		attr.SetValByKey(MapAttrAttr.MinLen, 0);
		attr.SetValByKey(MapAttrAttr.MaxLen, 50);
		attr.Insert();

		if (this.getNodePosType() == NodePosType.Start)
		{
			//开始节点信息.
			attr = new MapAttr();

			attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
			attr.setEditType(EditType.Edit);

			attr.SetValByKey(MapAttrAttr.KeyOfEn, "Title");
			attr.SetValByKey(MapAttrAttr.Name, "标题");
			attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.SetValByKey(MapAttrAttr.UIVisible, false);
			attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
			attr.setLGType(FieldTypeS.Normal);
			attr.SetValByKey(MapAttrAttr.MinLen, 0);
			attr.SetValByKey(MapAttrAttr.MaxLen, 200);
			attr.Insert();

			attr = new MapAttr();
			attr.SetValByKey(MapAttrAttr.FK_MapData, md.getNo());
			attr.setEditType(EditType.UnDel);
			attr.SetValByKey(MapAttrAttr.KeyOfEn, "FK_NY");
			attr.SetValByKey(MapAttrAttr.Name, "年月");
			attr.SetValByKey(MapAttrAttr.MyDataType, DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.SetValByKey(MapAttrAttr.UIVisible, false);
			attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
			attr.setLGType(FieldTypeS.Normal);
			attr.SetValByKey(MapAttrAttr.MinLen, 0);
			attr.SetValByKey(MapAttrAttr.MaxLen, 7);
			attr.Insert();
		}
	}
}