package BP.WF;

import javax.swing.Spring;

import BP.DA.*;
import BP.Sys.*;
import BP.Tools.StringHelper;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.Template.FrmWorkCheckAttr;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Port.*;

/** 
 这里存放每个节点的信息.	 
 
*/
public class Node extends Entity
{

	/** 
	 事件
	 * @throws Exception 
	 
	*/
	public final FrmEvents getFrmEvents() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = (FrmEvents)((tempVar instanceof FrmEvents) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEvents(this.getNodeID());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
    
		///#region 消息机制.
	private FrmEvent _SendSuccess_FrmEvent = null;
	public final FrmEvent getSendSuccess_FrmEvent() throws Exception
	{
		if (_SendSuccess_FrmEvent == null)
		{
			_SendSuccess_FrmEvent = new FrmEvent();
			_SendSuccess_FrmEvent.setMyPK("ND" + this.getNodeID() + "_SendSuccess");
			int i = _SendSuccess_FrmEvent.RetrieveFromDBSources();
			if (i == 0)
			{
				_SendSuccess_FrmEvent.setMsgCtrl(MsgCtrl.BySet);
				_SendSuccess_FrmEvent.setFK_Event(EventListOfNode.SendSuccess);
				_SendSuccess_FrmEvent.setMailEnable(true);
			}
		}
		return _SendSuccess_FrmEvent;
	}
	 
	public final CondModel getCondModel()
	{
		return CondModel.forValue(this.GetValIntByKey(NodeAttr.CondModel));
	}
	public final void setCondModel(CondModel value)
	{
		this.SetValByKey(NodeAttr.CondModel, value.getValue());
	}
	/** 
	 超时处理方式
	 
	*/
	public final OutTimeDeal getHisOutTimeDeal()
	{
		return OutTimeDeal.forValue(this.GetValIntByKey(NodeAttr.OutTimeDeal));
	}
	public final void setHisOutTimeDeal(OutTimeDeal value)
	{
		this.SetValByKey(NodeAttr.OutTimeDeal, value.getValue());
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
	public final SubThreadType getHisSubThreadType()
	{
		return SubThreadType.forValue(this.GetValIntByKey(NodeAttr.SubThreadType));
	}
	public final void setHisSubThreadType(SubThreadType value)
	{
		this.SetValByKey(NodeAttr.SubThreadType, value.getValue());
	}
	public final CC getHisCC() throws Exception
	{
		Object tempVar = this.GetRefObject("HisCC");
		CC obj = (CC)((tempVar instanceof CC) ? tempVar : null);
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
	 他的将要转向的方向集合
	 如果他没有到转向方向,他就是结束节点.
	 没有生命周期的概念,全部的节点.
	 * @throws Exception 
	*/
	public final Nodes getHisToNodes() throws Exception
	{
		Object tempVar = this.GetRefObject("HisToNodes");
		Nodes obj = (Nodes)((tempVar instanceof Nodes) ? tempVar : null);
		if (obj == null)
		{
			obj = new Nodes();
			obj.AddEntities(this.getHisToNDs());
			this.SetRefObject("HisToNodes", obj);
		}
		return obj;
	}	 
	/** 
	 他的工作
	 * @throws Exception 
	*/
	public final Work getHisWork() throws Exception
	{
		Work obj = null;
		if (this.getIsStartNode())
		{
			obj = new BP.WF.GEStartWork(this.getNodeID(), this.getNodeFrmID());
			obj.setHisNode(this);
			obj.setNodeID(this.getNodeID());
			return obj; 
		}
		
		 if (this.getFormType() != NodeFormType.FoolTruck 
				 || this.WorkID == 0 
				 || this.getIsStartNode() == true)
         {
			obj = new BP.WF.GEWork(this.getNodeID(), this.getNodeFrmID());
			obj.setHisNode(this);
			
			if (this.getFormType() == NodeFormType.FoolTruck )
				obj.setSQLCash(null);
			 
			obj.setNodeID(this.getNodeID());
			return obj;				 
		}
		 
		 //如果是累加表单.
         obj = new BP.WF.GEWork(this.getNodeID(), this.getNodeFrmID());

         Map ma = obj.getEnMap();

         /* 求出来走过的表单集合 */
         String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A, WF_Node B ";
         sql += " WHERE A.NDFrom=B.NodeID  ";
         sql += "  AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.Start.getValue() + ")  ";
         sql += "  AND B.FormType=" + NodeFormType.FoolTruck.getValue() + " "; // 仅仅找累加表单.
         sql += "  AND NDFrom!=" + this.getNodeID() + " "; //排除当前的表单.

         /*
         if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
             sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+CONVERT(varchar(10),B.NodeID) ) ";

         if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
             sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+cast(B.NodeID as varchar(10)) ) ";

         if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
             sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+to_char(B.NodeID) ) ";
             */

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
                 frmIDs += "'ND" + dr.getValue(0).toString() + "',";
             }

             frmIDs = frmIDs.substring(0, frmIDs.length() - 1);
             obj.HisPassedFrmIDs = frmIDs;  //求出所有的fromID.

             MapAttrs attrs = new MapAttrs();
             QueryObject qo = new QueryObject(attrs);
             qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + frmIDs + ")");
             qo.DoQuery();

             //设置成不可以用.
             for (MapAttr item : attrs.ToJavaList())
             {
                 item.setUIIsEnable( false); //设置为只读的.
                 item.setDefValReal("");    //设置默认值为空.
                 ma.getAttrs().Add(item.getHisAttr());
             }

             //设置为空.
             obj.setSQLCash(null) ;
         }
 
         obj.setHisNode(this);
         obj.setNodeID(this.getNodeID());
         
     //    obj.HisNode = this;
       //  obj.NodeID = this.NodeID;
         obj.clearSQLCash(); 
         BP.DA.Cash.getSQL_Cash().remove( "ND"+this.getNodeID());
          
         return obj;
	}
	/** 
	 他的工作s
	 * @throws Exception 
	*/
	public final Works getHisWorks() throws Exception
	{
		Works obj = (Works)((this.getHisWork().getGetNewEntities() instanceof Works) ? this.getHisWork().getGetNewEntities() : null);
		return obj;
			////Works obj = this.GetRefObject("HisWorks") as Works;
			////if (obj == null)
			////{
			//    this.SetRefObject("HisWorks",obj);
			//}
			//return obj;
	}
	/** 
	 流程
	 * @throws Exception 
	*/
	public final Flow getHisFlow() throws Exception
	{
		Object tempVar = this.GetRefObject("Flow");
		Flow obj = (Flow)((tempVar instanceof Flow) ? tempVar : null);
		if (obj == null)
		{
			obj = new Flow(this.getFK_Flow());
			this.SetRefObject("Flow", obj);
		}
		return obj;
	}
	/** 
	 消息推送.
	 * @throws Exception 
	*/
	public final PushMsgs getHisPushMsgs() throws Exception
	{
		Object tempVar = this.GetRefObject("PushMsg");
		PushMsgs obj = (PushMsgs)((tempVar instanceof PushMsgs) ? tempVar : null);
		if (obj == null)
		{
			 obj = new PushMsgs(this.getNodeID()); //这个暂时有问题，应该是当前节点，取的是下一个节点

				//检查是否有默认的发送？如果没有就增加上他。
			 boolean isHaveSend = false;
			 boolean isHaveReturn = false;
			 for (PushMsg item : obj.ToJavaList())
			 {
				 if (item.getFK_Event() == EventListOfNode.SendSuccess)
				 {
					 isHaveSend = true;
				 }

				 if (item.getFK_Event() == EventListOfNode.ReturnAfter)
				 {
					 isHaveReturn = true;
				 }
			 }

			 if (isHaveSend == false)
			 {
				 PushMsg pm = new PushMsg();
				 pm.setFK_Event(EventListOfNode.SendSuccess);
				 pm.setMailPushWay(0); //默认: 让其使用消息提醒.
				 pm.setSMSPushWay(1); //默认:不让其使用短信提醒.
				 obj.AddEntity(pm);
			 }

			 if (isHaveReturn == false)
			 {
				 PushMsg pm = new PushMsg();
				 pm.setFK_Event(EventListOfNode.ReturnAfter);
				 pm.setMailPushWay(0); //默认: 让其使用消息提醒.
				 pm.setSMSPushWay(1); //默认:不让其使用短信提醒.
				 obj.AddEntity(pm);
			 }

			this.SetRefObject("PushMsg", obj);
		}
		return obj;
	}
	/** 
	 HisFrms
	 * @throws Exception 
	 
	*/
	public final Frms getHisFrms() throws Exception
	{
		Frms frms = new Frms();
		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getNodeID());
		for (FrmNode fn : fns.ToJavaList())
		{
			frms.AddEntity(fn.getHisFrm());
		}
		return frms;

			//this.SetRefObject("HisFrms", obj);
			//Frms obj = this.GetRefObject("HisFrms") as Frms;
			//if (obj == null)
			//{
			//    obj = new Frms();
			//    FrmNodes fns = new FrmNodes(this.NodeID);
			//    foreach (FrmNode fn in fns)
			//        obj.AddEntity(fn.HisFrm);
			//    this.SetRefObject("HisFrms", obj);
			//}
			//return obj;
	}
	/** 
	 他的将要来自的方向集合
	 如果他没有到来的方向,他就是开始节点.
	 * @throws Exception 
	*/
	public final Nodes getFromNodes() throws Exception
	{
		Object tempVar = this.GetRefObject("HisFromNodes");
		Nodes obj = (Nodes)((tempVar instanceof Nodes) ? tempVar : null);
		if (obj == null)
		{
				// 根据方向生成到达此节点的节点。
			Directions ens = new Directions();
			if (this.getIsStartNode())
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
	public final BillTemplates getBillTemplates() throws Exception
	{
		Object tempVar = this.GetRefObject("BillTemplates");
		BillTemplates obj = (BillTemplates)((tempVar instanceof BillTemplates) ? tempVar : null);
		if (obj == null)
		{
			obj = new BillTemplates(this.getNodeID());
			this.SetRefObject("BillTemplates", obj);
		}
		return obj;
	}
	public final NodeStations getNodeStations() throws Exception
	{
		Object tempVar = this.GetRefObject("NodeStations");
		NodeStations obj = (NodeStations)((tempVar instanceof NodeStations) ? tempVar : null);
		if (obj == null)
		{
			obj = new NodeStations(this.getNodeID());
			this.SetRefObject("NodeStations", obj);
		}
		return obj;
	}
	public final NodeDepts getNodeDepts() throws Exception
	{
		Object tempVar = this.GetRefObject("NodeDepts");
		NodeDepts obj = (NodeDepts)((tempVar instanceof NodeDepts) ? tempVar : null);
		if (obj == null)
		{
			obj = new NodeDepts(this.getNodeID());
			this.SetRefObject("NodeDepts", obj);
		}
		return obj;
	}
	public final NodeEmps getNodeEmps() throws Exception
	{
		Object tempVar = this.GetRefObject("NodeEmps");
		NodeEmps obj = (NodeEmps)((tempVar instanceof NodeEmps) ? tempVar : null);
		if (obj == null)
		{
			obj = new NodeEmps(this.getNodeID());
			this.SetRefObject("NodeEmps", obj);
		}
		return obj;
	}
	public final FrmNodes getFrmNodes() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmNodes");
		FrmNodes obj = (FrmNodes)((tempVar instanceof FrmNodes) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmNodes(this.getFK_Flow(), this.getNodeID());
			this.SetRefObject("FrmNodes", obj);
		}
		return obj;
	}
	public final MapData getMapData() throws Exception
	{
		Object tempVar = this.GetRefObject("MapData");
		MapData obj = (MapData)((tempVar instanceof MapData) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapData(this.getNodeFrmID());
			this.SetRefObject("MapData", obj);
		}
		return obj;
	}

		///#endregion


		///#region 初试化全局的 Node
	@Override
	public String getPK()
	{
		return "NodeID";
	}
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
		}
		return uac;
	}
	/** 
	 初试化全局的
	 
	 @return 
	 * @throws Exception 
	*/
	public final NodePosType GetHisNodePosType() throws Exception
	{
		String nodeid = (new Integer(this.getNodeID())).toString();
		if (nodeid.substring(nodeid.length() - 2).equals("01"))
		{
			return NodePosType.Start;
		}

		if (this.getFromNodes().size() == 0)
		{
			return NodePosType.Mid;
		}

		if (this.getHisToNodes().size() == 0)
		{
			return NodePosType.End;
		}
		return NodePosType.Mid;
	}
	/** 
	 检查流程，修复必要的计算字段信息.
	 
	 @param fl 流程
	 @return 返回检查信息
	 * @throws Exception 
	*/
	public static String CheckFlow(Flow fl) throws Exception
	{
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		if (SystemConfig.getOSDBSrc() == OSDBSrc.Database)
		{
			// 删除必要的数据.
			DBAccess.RunSQL("DELETE FROM WF_NodeEmp WHERE FK_Emp  NOT IN (SELECT No from Port_Emp)");
			DBAccess.RunSQL("DELETE FROM WF_Emp WHERE NO NOT IN (SELECT No FROM Port_Emp )");
			DBAccess.RunSQL("UPDATE WF_Emp SET Name=(SELECT Name From Port_Emp WHERE Port_emp.No=WF_emp.No),FK_Dept=(select FK_Dept from Port_Emp where Port_emp.No=WF_emp.No)");
		}

		Nodes nds = new Nodes();
		nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());

		//FlowSort fs = new FlowSort();
		
		if (nds.size() == 0)
		{
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
		for (Node nd : nds.ToJavaList())
		{
			BP.Sys.MapData md = new BP.Sys.MapData();
			md.setNo("ND" + nd.getNodeID());
			if (md.getIsExits() == false)
			{
				nd.CreateMap();
			}

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
			Directions dirs = new Directions( nd.getNodeID() );
			for (Direction dir : dirs.ToJavaList())
			{
				strs += "@" + dir.getToNode();
			}
			nd.setHisToNDs(strs);

			// 单据
			strs = "";
			BillTemplates temps = new BillTemplates(nd);
			for (BillTemplate temp : temps.ToJavaList())
			{
				strs += "@" + temp.getNo();
			}
			nd.setHisBillIDs(strs);

			// 检查节点的位置属性。
			nd.setHisNodePosType(nd.GetHisNodePosType());
			nd.DirectUpdate();
		}

		// 处理岗位分组.
		sql = "SELECT HisStas, COUNT(*) as NUM FROM WF_Node WHERE FK_Flow='" + fl.getNo() + "' GROUP BY HisStas";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
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
				if (nodes.contains("@" + (new Integer(nd.getNodeID())).toString()) == false)
				{
					continue;
				}

				nd.setGroupStaNDs(nodes);
				nd.DirectUpdate();
			}
		}

		// 判断流程的类型 
		sql = "SELECT Name FROM WF_Node WHERE (NodeWorkType=" + NodeWorkType.StartWorkFL.getValue() + " OR NodeWorkType=" + NodeWorkType.WorkFHL.getValue() + " OR NodeWorkType=" + NodeWorkType.WorkFL.getValue() + " OR NodeWorkType=" + NodeWorkType.WorkHL.getValue() + ") AND (FK_Flow='" + fl.getNo() + "')";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		fl.DirectUpdate();
		return null;
	}

	@Override
	protected boolean beforeUpdate() throws Exception
	{
		if (this.getIsStartNode())
		{
			this.SetValByKey(BtnAttr.ReturnRole, ReturnRole.CanNotReturn.getValue());
			this.SetValByKey(BtnAttr.ShiftEnable, 0);
			//  this.SetValByKey(BtnAttr.CCRole, 0);
			this.SetValByKey(BtnAttr.EndFlowEnable, 0);
		}

		//给icon设置默认值.
		if (this.GetValStrByKey(NodeAttr.ICON).equals(""))
		{
			this.setICON( "审核.png");
		}



			///#region 如果是数据合并模式，就要检查节点中是否有子线程，如果有子线程就需要单独的表.
		if (this.getHisRunModel() == RunModel.SubThread)
		{
			MapData md = new MapData("ND" + this.getNodeID());
			if ( ! md.getPTable().equals("ND" + this.getNodeID()))
			{
				md.setPTable("ND" + this.getNodeID());
				md.Update();
			}
		}
		//更新版本号.
		Flow.UpdateVer(this.getFK_Flow());
			 

	    ///#region 更新流程判断条件的标记。
		DBAccess.RunSQL("UPDATE WF_Node SET IsCCFlow=0  WHERE FK_Flow='" + this.getFK_Flow() + "'");
		DBAccess.RunSQL("UPDATE WF_Node SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond WHERE CondType=1) AND FK_Flow='" + this.getFK_Flow() + "'");
		Flow fl = new Flow(this.getFK_Flow());

		Node.CheckFlow(fl);
		this.setFlowName(fl.getName());

		DBAccess.RunSQL("UPDATE Sys_MapData SET Name='" + this.getName() + "' WHERE No='ND" + this.getNodeID() + "'");
		switch (this.getHisRunModel())
		{
			case Ordinary:
				if (this.getIsStartNode())
				{
					this.setHisNodeWorkType(NodeWorkType.StartWork);
				}
				else
				{
					this.setHisNodeWorkType(NodeWorkType.Work);
				}
				break;
			case FL:
				if (this.getIsStartNode())
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
		//创建审核组件附件
		FrmAttachment workCheckAth = new FrmAttachment();
		boolean isHave = workCheckAth.RetrieveByAttr(FrmAttachmentAttr.MyPK, this.getNodeID() + "_FrmWorkCheck");
		//不包含审核组件
		if (isHave == false)
		{
			workCheckAth = new FrmAttachment();
			//如果没有查询到它,就有可能是没有创建.
			workCheckAth.setMyPK(this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setFK_MapData((new Integer(this.getNodeID())).toString());
			workCheckAth.setNoOfObj(this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setExts("*.*");

			//存储路径.
			//workCheckAth.setSaveTo("/DataUser/UploadFile/");
			
			workCheckAth.setIsNote(false); //不显示note字段.
			workCheckAth.setIsVisable(false); // 让其在form 上不可见.

			//位置.
			workCheckAth.setX((float)94.09);
			workCheckAth.setY((float)333.18);
			workCheckAth.setW((float)626.36);
			workCheckAth.setH((float)150);

			//多附件.
			workCheckAth.setUploadType(AttachmentUploadType.Multi);
			workCheckAth.setName("审核组件");
			workCheckAth.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=@IsWoEnableTemplete=1");
			workCheckAth.Insert();
		}
		return super.beforeUpdate();
	}
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
	public final BP.WF.Template.FrmWorkCheckSta getFrmWorkCheckSta()
	{
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(NodeAttr.FWCSta));
	}
	public final void setFrmWorkCheckSta(BP.WF.Template.FrmWorkCheckSta value)
	{
		this.SetValByKey(NodeAttr.FWCSta, value.getValue());
	}
	/** 
	 内部编号
	*/
	public final String getNo()
	{
		try
		{
			return String.valueOf(this.getNodeID()).substring(String.valueOf(this.getNodeID()).length() - 2);
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineInfo(ex.getMessage() + " - " + this.getNodeID());
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
	public final void setAutoJumpRole1(boolean value)
	{
		this.SetValByKey(NodeAttr.AutoJumpRole1, value);
	}
	/** 
	 自动跳转规则2-处理人与上一步相同
	*/
	public final boolean getAutoJumpRole2()
	{
		return this.GetValBooleanByKey(NodeAttr.AutoJumpRole2);
	}
	public final void setAutoJumpRole2(boolean value)
	{
		this.SetValByKey(NodeAttr.AutoJumpRole2, value);
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
	public final SubFlowStartWay getSubFlowStartWay()
	{
		return SubFlowStartWay.forValue(this.GetValIntByKey(NodeAttr.SubFlowStartWay));
	}
	public final void setSubFlowStartWay(SubFlowStartWay value)
	{
		this.SetValByKey(NodeAttr.SubFlowStartWay, value.getValue());
	}
	public final NodeFormType getHisFormType()
	{
		return NodeFormType.forValue(this.GetValIntByKey(NodeAttr.FormType));
	}
	public final void setHisFormType(NodeFormType value)
	{
		this.SetValByKey(NodeAttr.FormType, value.getValue());
	}
	public final String getHisFormTypeText() throws Exception
	{
			//switch (this.HisFormType)
			//{
			//    case NodeFormType.DisableIt:
			//        break;
			//    default:
			//        break;
			//}
		SysEnum se = new SysEnum("NodeFormType", this.getHisFormType().getValue());
		return se.getLab();
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
	public final boolean getIsEnableTaskPool()
	{
		if (this.getTodolistModel() == TodolistModel.Sharing)
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
	public final String getICON()
	{
		String s = this.GetValStrByKey(NodeAttr.ICON);
		if (StringHelper.isNullOrEmpty(s))
		{
			if (this.getIsStartNode())
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
	public final String getFormUrl()
	{
		String str = this.GetValStrByKey(NodeAttr.FormUrl);
		str = str.replace("@SDKFromServHost", BP.Sys.SystemConfig.getAppSettings().get("SDKFromServHost").toString());
		return str;
	}
	public final void setFormUrl(String value)
	{
		this.SetValByKey(NodeAttr.FormUrl, value);
	}
	public final NodeFormType getFormType()
	{
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
	public final int getTimeLimit()
	{
		int i= this.GetValIntByKey(NodeAttr.TimeLimit);
		if (i == 0)		
			return 0;
		
		return i;
	}
	public final void setTimeLimit(int value)
	{
		this.SetValByKey(NodeAttr.TimeLimit, value);
	}
	
	/*
	 * 小时
	 * */
	public final int getTimeLimitHH()
	{
		return this.GetParaInt("TimeLimitHH",0);	 
	}
	
	/*
	 * 分钟
	 * */
	public final int getTimeLimitMM()
	{
		return this.GetParaInt("TimeLimitMM",0);	 
	}
	 
	/** 
	 逾期提醒规则
	*/
	public final CHAlertRole getTAlertRole()
	{
		return CHAlertRole.forValue(this.GetValIntByKey(NodeAttr.TAlertRole));
	}
	public final void setTAlertRole(CHAlertRole value)
	{
		this.SetValByKey(NodeAttr.TAlertRole, value.getValue());
	}
	/** 
	 时间计算方式
	*/
	public final TWay getTWay()
	{
		return TWay.forValue(this.GetValIntByKey(NodeAttr.TWay));
	}
	public final void setTWay(TWay value)
	{
		this.SetValByKey(NodeAttr.TWay, value.getValue());
	}
	/** 
	 逾期 - 提醒方式
	*/
	public final CHAlertWay getTAlertWay()
	{
		return CHAlertWay.forValue(this.GetValIntByKey(NodeAttr.TAlertWay));
	}
	public final void setTAlertWay(CHAlertWay value)
	{
		this.SetValByKey(NodeAttr.TAlertWay, value.getValue());
	}
 
	/** 
	 预警天
	*/
	public final int getWarningDay()
	{
		int i = this.GetValIntByKey(NodeAttr.WarningDay);
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
	public final CHAlertRole getWAlertRole()
	{
		return CHAlertRole.forValue(this.GetValIntByKey(NodeAttr.WAlertRole));
	}
	public final void setWAlertRole(CHAlertRole value)
	{
		this.SetValByKey(NodeAttr.WAlertRole, value.getValue());
	}
	/** 
	 预警 - 提醒方式
	*/
	public final CHAlertWay getWAlertWay()
	{
		return CHAlertWay.forValue(this.GetValIntByKey(NodeAttr.WAlertWay));
	}
	public final void setWAlertWay(CHAlertWay value)
	{
		this.SetValByKey(NodeAttr.WAlertWay, value.getValue());
	}
	/** 
	 保存方式 @0=仅节点表 @1=节点与NDxxxRtp表.
	*/
	public final SaveModel getSaveModel()
	{
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
	public final boolean getIsGuestNode()
	{
		return this.GetValBooleanByKey(NodeAttr.IsGuestNode);
	}
	public final void setIsGuestNode(boolean value)
	{
		this.SetValByKey(NodeAttr.IsGuestNode, value);
	}
	/** 
	 是否是开始节点
	*/
	public final boolean getIsStartNode()
	{
		if (this.getNo().equals("01"))
		{
			return true;
		}
		return false;

			//if (this.HisNodePosType == NodePosType.Start)
			//    return true;
			//else
			//    return false;
	}
	/** 
	 x
	*/
	public final int getX()
	{
		return this.GetValIntByKey(NodeAttr.X);
	}
	public final void setX(int value)
	{
		this.SetValByKey(NodeAttr.X, value);
	}

	/** 
	 y
	 
	*/
	public final int getY()
	{
		return this.GetValIntByKey(NodeAttr.Y);
	}
	public final void setY(int value)
	{
		this.SetValByKey(NodeAttr.Y, value);
	}
	/** 
	 水执行它？
	 
	*/
	public final int getWhoExeIt()
	{
		return this.GetValIntByKey(NodeAttr.WhoExeIt);
	}
	public final void setWhoExeIt(int value)
	{
		this.SetValByKey(NodeAttr.WhoExeIt, value);
	}

	/** 
	 位置
	 
	*/
	public final NodePosType getNodePosType()
	{
		return NodePosType.forValue(this.GetValIntByKey(NodeAttr.NodePosType));
	}
	public final void setNodePosType(NodePosType value)
	{
		this.SetValByKey(NodeAttr.NodePosType, value.getValue());
	}
	/** 
	 运行模式
	*/
	public final RunModel getHisRunModel()
	{
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
	{
		SetValByKey(NodeAttr.FK_Flow, value);
	}
	/** 
	 获取它的上一步的分流点
	 * @throws Exception 
	*/
	private Node _GetHisPriFLNode(Nodes nds) throws Exception
	{
		for (Node mynd : nds.ToJavaList())
		{
			if (mynd.getIsFL())
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
	 * @throws Exception 
	*/
	public final Node getHisPriFLNode() throws Exception
	{
		return _GetHisPriFLNode(this.getFromNodes());
	}
	public final String getTurnToDealDoc()
	{
		String s = this.GetValStrByKey(NodeAttr.TurnToDealDoc);
		if (this.getHisTurnToDeal() == TurnToDeal.SpecUrl)
		{
			if (s.contains("?"))
			{
				s += "&1=1";
			}
			else
			{
				s += "?1=1";
			}
		}
		return s;
	}
	public final void setTurnToDealDoc(String value)
	{
		SetValByKey(NodeAttr.TurnToDealDoc, value);
	}
	/** 
	 可跳转的节点
	*/
	public final String getJumpToNodes()
	{
		return this.GetValStrByKey(NodeAttr.JumpToNodes);
	}
	public final void setJumpToNodes(String value)
	{
		SetValByKey(NodeAttr.JumpToNodes, value);
	}
	public long WorkID=0;
	/** 
	 节点表单ID
	 * @throws Exception 
	*/
	public final String getNodeFrmID() throws Exception
	{

		  String str = this.GetValStrByKey(NodeAttr.NodeFrmID);
          if (DataType.IsNullOrEmpty(str))
              return "ND" + this.getNodeID();
          
          if (this.getHisFormType() ==  NodeFormType.FoolTruck 
        		  || this.getHisFormType() ==  NodeFormType.SheetTree)
              return "ND" + this.getNodeID();


          if (str.equals("Pri") == true &&
              (this.getHisFormType() == NodeFormType.FoolForm || this.getHisFormType() == NodeFormType.FreeForm))
          {
              if (this.WorkID == 0)
            	  return "ND" + this.getNodeID();
                 // throw new Exception("err@获得当前节点的上一个节点表单出现错误,没有给参数WorkID赋值.");

              /* 要引擎上一个节点表单 */
              String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A, WF_Node B ";
              sql += " WHERE A.NDFrom=B.NodeID AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.Start.getValue() + ")  ";
              sql += "  AND (FormType=0 OR FormType=1) ";
              
              /*
              if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
                  sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'+CONVERT(varchar(10),B.NodeID) ) ";

              if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
                  sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID=CONCAT( 'ND',cast(B.NodeID as varchar(10)) ) ) ";

              if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
                  sql += "  AND (B.NodeFrmID='' OR B.NodeFrmID IS NULL OR B.NodeFrmID='ND'||to_char(B.NodeID) ) ";
              */
              
              sql += "  AND (A.WorkID="+this.WorkID+") ";

              sql += " ORDER BY A.RDT ";

              int nodeID = DBAccess.RunSQLReturnValInt(sql,0);
              if (nodeID == 0)
                  throw new Exception("err@没有找到指定的节点.");

              return "ND" + nodeID;
          }
          return str;
          
	}
	public final void setNodeFrmID(String value)
	{
		SetValByKey(NodeAttr.NodeFrmID, value);
	}
	/** 
	 要启动的子流程
	*/
	public final String getSFActiveFlows()
	{
		return this.GetValStrByKey(NodeAttr.SFActiveFlows);
	}
	public final void setSFActiveFlows(String value)
	{
		SetValByKey(NodeAttr.SFActiveFlows, value);
	}

	public final String getFlowName()
	{
		return this.GetValStrByKey(NodeAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(NodeAttr.FlowName, value);
	}
	/** 
	 打印方式
	*/
	public final PrintDocEnable getHisPrintDocEnable()
	{
		return PrintDocEnable.forValue(this.GetValIntByKey(NodeAttr.PrintDocEnable));
	}
	public final void setHisPrintDocEnable(PrintDocEnable value)
	{
		this.SetValByKey(NodeAttr.PrintDocEnable, value.getValue());
	}
	/** 
	 批处理规则
	 
	*/
	public final BatchRole getHisBatchRole()
	{
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
	public final String getBatchParas()
	{
		String str = this.GetValStringByKey(NodeAttr.BatchParas);

			//替换约定的URL.
		str = str.replace("@SDKFromServHost", BP.Sys.SystemConfig.getAppSettings().get("SDKFromServHost").toString());
			//if (str.Length <=3)
			//    str="Title,RDT"
		return str;
	}
	public final void setBatchParas(String value)
	{
		this.SetValByKey(NodeAttr.BatchParas, value);
	}
	/** 
	 是否是自定义的url,处理批处理.
	*/
	public final boolean getBatchParas_IsSelfUrl()
	{
		if (this.getBatchParas().contains(".jsp") || this.getBatchParas().contains(".jsp") || this.getBatchParas().contains(".htm") || this.getBatchParas().contains("http:"))
		{
			return true;
		}
		return false;
	}
	/** 
	 批量审核数量
	*/
	public final int getBatchListCount()
	{
		return this.GetValIntByKey(NodeAttr.BatchListCount);
	}
	public final void setBatchListCount(int value)
	{
		this.SetValByKey(NodeAttr.BatchListCount, value);
	}
	public final String getPTable()
	{

		return "ND" + this.getNodeID();
	}
	public final void setPTable(String value)
	{
		SetValByKey(NodeAttr.PTable, value);
	}
	/** 
	 要显示在后面的表单
	*/
	public final String getShowSheets()
	{
		String s = this.GetValStrByKey(NodeAttr.ShowSheets);
		if (s.equals(""))
		{
			return "@";
		}
		return s;
	}
	public final void setShowSheets(String value)
	{
		SetValByKey(NodeAttr.ShowSheets, value);
	}
	/** 
	 Doc
	*/
	public final String getDoc()
	{
		return this.GetValStrByKey(NodeAttr.Doc);
	}
	public final void setDoc(String value)
	{
		SetValByKey(NodeAttr.Doc, value);
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
	public final int getHisToNDNum()
	{
		String[] strs = this.getHisToNDs().split("[@]", -1);
		return strs.length - 1;
	}
	public final String getHisToNDs()
	{
		return this.GetValStrByKey(NodeAttr.HisToNDs);
	}
	public final void setHisToNDs(String value)
	{
		this.SetValByKey(NodeAttr.HisToNDs, value);
	}
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

	public final String getHisBillIDs()
	{
		return this.GetValStrByKey(NodeAttr.HisBillIDs);
	}
	public final void setHisBillIDs(String value)
	{
		this.SetValByKey(NodeAttr.HisBillIDs, value);
	}
	/** 
	 公文左边词语
	 
	*/
	public final String getDocLeftWord()
	{
		return this.GetValStrByKey(NodeAttr.DocLeftWord);
	}
	public final void setDocLeftWord(String value)
	{
		this.SetValByKey(NodeAttr.DocLeftWord, value);
	}
	/** 
	 公文右边词语
	 
	*/
	public final String getDocRightWord()
	{
		return this.GetValStrByKey(NodeAttr.DocRightWord);
	}
	public final void setDocRightWord(String value)
	{
		this.SetValByKey(NodeAttr.DocRightWord, value);
	}

		///#endregion


		
	/** 
	 是不是多岗位工作节点.
	 * @throws Exception 
	 
	*/
	public final boolean getIsMultiStations() throws Exception
	{
		if (this.getNodeStations().size() > 1)
		{
			return true;
		}
		return false;
	}
	public final String getHisStationsStr() throws Exception
	{
		String s = "";
		for (NodeStation ns : this.getNodeStations().ToJavaList())
		{
			s += ns.getFK_StationT() + ",";
		}
		return s;
	}

		///#endregion


		///#region 公共方法
	/** 
	 得到一个工作data实体
	 
	 @param workId 工作ID
	 @return 如果没有就返回null
	 * @throws Exception 
	*/
	public final Work GetWork(long workId) throws Exception
	{
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
		///#region 节点的工作类型
	/** 
	 转向处理
	*/
	public final TurnToDeal getHisTurnToDeal()
	{
		return TurnToDeal.forValue(this.GetValIntByKey(NodeAttr.TurnToDeal));
	}
	public final void setHisTurnToDeal(TurnToDeal value)
	{
		this.SetValByKey(NodeAttr.TurnToDeal, value.getValue());
	}
	/** 
	 访问规则
	 
	*/
	public final DeliveryWay getHisDeliveryWay()
	{
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}
	public final void setHisDeliveryWay(DeliveryWay value)
	{
		this.SetValByKey(NodeAttr.DeliveryWay, value.getValue());
	}
	/** 
	 访问规则Text
	 * @throws Exception 
	*/
	public final String getHisDeliveryWayText() throws Exception
	{
		SysEnum se = new SysEnum("DeliveryWay", this.getHisDeliveryWay().getValue());
		return se.getLab();
	}
	/** 
	 考核规则
	*/
	public final CHWay getHisCHWay()
	{
		return CHWay.forValue(this.GetValIntByKey(NodeAttr.CHWay));
	}
	public final void setHisCHWay(CHWay value)
	{
		this.SetValByKey(NodeAttr.CHWay, value.getValue());
	}
	/** 
	 抄送规则
	*/
	public final CCRole getHisCCRole()
	{
		return CCRole.forValue(this.GetValIntByKey(NodeAttr.CCRole));
	}
	public final void setHisCCRole(CCRole value)
	{
		this.SetValByKey(BtnAttr.CCRole, value.getValue());
	}
	public final String getHisCCRoleText() throws Exception
	{
		SysEnum se = new SysEnum(NodeAttr.CCRole, this.getHisCCRole().getValue());
		return se.getLab();
	}
	/** 
	 删除流程规则
	*/
	public final DelWorkFlowRole getHisDelWorkFlowRole()
	{
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
		this.SetValByKey(NodeAttr.WhenNoWorker,value);
	}
	/** 
	 撤销规则
	*/
	public final CancelRole getHisCancelRole()
	{
		return CancelRole.forValue(this.GetValIntByKey(NodeAttr.CancelRole));
	}
	public final void setHisCancelRole(CancelRole value)
	{
		this.SetValByKey(NodeAttr.CancelRole, value.getValue());
	}
	
	public final boolean getCancelDisWhenRead()
	{
		return this.GetValBooleanByKey(NodeAttr.CancelDisWhenRead);
	}

	/** 
	 数据写入规则
	*/
	public final CCWriteTo getCCWriteTo()
	{
		return CCWriteTo.forValue(this.GetValIntByKey(NodeAttr.CCWriteTo));
	}
	public final void setCCWriteTo(CCWriteTo value)
	{
		this.SetValByKey(NodeAttr.CCWriteTo, value.getValue());
	}

	/** 
	 Int type
	*/
	public final NodeWorkType getHisNodeWorkType()
	{

		switch (this.getHisRunModel())
		{
			case Ordinary:
				if (this.getIsStartNode())
				{
					return NodeWorkType.StartWork;
				}
				else
				{
					return NodeWorkType.Work;
				}
			case FL:
				if (this.getIsStartNode())
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
	public final String getHisRunModelT() throws Exception
	{
		SysEnum se = new SysEnum(NodeAttr.RunModel, this.getHisRunModel().getValue());
		return se.getLab();
	}
		///#region 推算属性 (对于节点位置的判断)

	/** 
	 类型
	 * @throws Exception 
	*/
	public final NodePosType getHisNodePosType() throws Exception
	{
		if (SystemConfig.getIsDebug())
		{
			this.SetValByKey(NodeAttr.NodePosType, this.GetHisNodePosType().getValue());
			return this.GetHisNodePosType();
		}
		return NodePosType.forValue(this.GetValIntByKey(NodeAttr.NodePosType));
	}
	public final void setHisNodePosType(NodePosType value)
	{
		if (value == NodePosType.Start)
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
	 * @throws Exception 
	*/
	public final boolean getIsEndNode() throws Exception
	{
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
	 是否允许子线程接受人员重复(对子线程点有效)?
	*/
	public final boolean getIsAllowRepeatEmps()
	{
		return this.GetValBooleanByKey(NodeAttr.IsAllowRepeatEmps);
	}
	public final void setIsAllowRepeatEmps(boolean value)
	{
		this.SetValByKey(NodeAttr.IsAllowRepeatEmps, value);
	}
	/** 
	 是否启用发送短信？
	*/
	public final boolean getIsEnableSMSMessage()
	{
		int i = BP.DA.DBAccess.RunSQLReturnValInt("SELECT SMSEnable FROM Sys_FrmEvent WHERE FK_MapData='ND" + this.getNodeID() + "' AND FK_Event='SendSuccess'", 0);
		if (i == 0)
		{
			return false;
		}
		return true;
	}
	/** 
	 是否可以在退回后原路返回？
	*/
	public final boolean getIsBackTracking()
	{
		return this.GetValBooleanByKey(NodeAttr.IsBackTracking);
	}
	/** 
	 是否启用自动记忆功能
	*/
	public final boolean getIsRememberMe()
	{
		return this.GetValBooleanByKey(NodeAttr.IsRM);
	}
	public final void setIsRememberMe(boolean value)
	{
		this.SetValByKey(NodeAttr.IsRM, value);
	}
	/** 
	 是否可以删除
	*/
	public final boolean getIsCanDelFlow()
	{
		return this.GetValBooleanByKey(NodeAttr.IsCanDelFlow);
	}

	/** 
	 普通工作节点处理模式
	*/
	public final TodolistModel getTodolistModel()
	{
		return TodolistModel.forValue(this.GetValIntByKey(NodeAttr.TodolistModel));
	}
	public final void setTodolistModel(TodolistModel value)
	{
		this.SetValByKey(NodeAttr.TodolistModel, value.getValue());
	}
	
	/**
	 * 组长确认规则
	 */
    public final TeamLeaderConfirmRole getTeamLeaderConfirmRole()
    {
          return TeamLeaderConfirmRole.forValue(this.GetValIntByKey(NodeAttr.TeamLeaderConfirmRole));
    }
    public final void  setTeamLeaderConfirmRole(TeamLeaderConfirmRole value)
    {
          this.SetValByKey(NodeAttr.TeamLeaderConfirmRole, value.getValue());
    }
    /**
     * 组长确认规则内容.
     */
    public String getTeamLeaderConfirmDoc()
    {
         return this.GetValStringByKey(NodeAttr.TeamLeaderConfirmDoc);
    }
    public void setTeamLeaderConfirmDoc(String value)
    {
        this.SetValByKey(NodeAttr.TeamLeaderConfirmDoc, value);
    }
	/** 
	 阻塞模式
	*/
	public final BlockModel getBlockModel()
	{
		return BlockModel.forValue(this.GetValIntByKey(NodeAttr.BlockModel));
	}
	public final void setBlockModel(BlockModel value)
	{
		this.SetValByKey(NodeAttr.BlockModel, value.getValue());
	}
	/** 
	 阻塞的表达式
	*/
	public final String getBlockExp()
	{
		String str = this.GetValStringByKey(NodeAttr.BlockExp);

		if (StringHelper.isNullOrEmpty(str))
		{
			if (this.getBlockModel() == BlockModel.CurrNodeAll)
			{
				return "还有子流程没有完成您不能提交,需要等到所有的子流程完成后您才能发送.";
			}

			if (this.getBlockModel() == BlockModel.SpecSubFlow)
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
	public final ThreadKillRole getThreadKillRole()
	{
		return ThreadKillRole.forValue(this.GetValIntByKey(NodeAttr.ThreadKillRole));
	}
	/** 
	 是否保密步骤
	*/
	public final boolean getIsSecret()
	{
		return this.GetValBooleanByKey(NodeAttr.IsSecret);
	}
	public final void setIsSecret(boolean value)
	{
		this.SetValByKey(NodeAttr.IsSecret, value);
	}
	/** 
	 完成通过率
	*/
	public final java.math.BigDecimal getPassRate()
	{
		return this.GetValDecimalByKey(NodeAttr.PassRate);
	}
	/** 
	 是否允许分配工作
	*/
	public final boolean getIsTask()
	{
		return this.GetValBooleanByKey(NodeAttr.IsTask);
	}
	public final void setIsTask(boolean value)
	{
		this.SetValByKey(NodeAttr.IsTask, value);
	}
	/** 
	 是否是业务单元
	*/
	public final boolean getIsBUnit()
	{
		return this.GetValBooleanByKey(NodeAttr.IsBUnit);
	}
	public final void setIsBUnit(boolean value)
	{
		this.SetValByKey(NodeAttr.IsBUnit, value);
	}

	public final boolean getIsCanOver()
	{
		return this.GetValBooleanByKey(NodeAttr.IsCanOver);
	}
	public final void setIsCanOver(boolean value)
	{
		this.SetValByKey(NodeAttr.IsCanOver, value);
	}
	 
	/** 
	 是否可以移交
	*/
	public final boolean getIsHandOver()
	{
		if (this.getIsStartNode())
		{
			return false;
		}

		return this.GetValBooleanByKey(NodeAttr.IsHandOver);
	}
	public final void setIsHandOver(boolean value)
	{
		this.SetValByKey(NodeAttr.IsHandOver, value);
	}

	/** 
	 是否可以退回？
	*/
	public final boolean getIsCanReturn()
	{
		if (this.getHisReturnRole() == ReturnRole.CanNotReturn)
		{
			return false;
		}
		return true;
	}
	/** 
	 已读回执
	*/
	public final ReadReceipts getReadReceipts()
	{
		return ReadReceipts.forValue(this.GetValIntByKey(NodeAttr.ReadReceipts));
	}
	public final void setReadReceipts(ReadReceipts value)
	{
		this.SetValByKey(NodeAttr.ReadReceipts, value.getValue());
	}
	/** 
	 退回规则
	*/
	public final ReturnRole getHisReturnRole()
	{
		return ReturnRole.forValue(this.GetValIntByKey(NodeAttr.ReturnRole));
	}
	public final void setHisReturnRole(ReturnRole value)
	{
		this.SetValByKey(NodeAttr.ReturnRole, value.getValue());
	}
	/** 
	 是不是中间节点
	 * @throws Exception 
	 
	*/
	public final boolean getIsMiddleNode() throws Exception
	{
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
	public final boolean getIsEval()
	{
		return this.GetValBooleanByKey(NodeAttr.IsEval);
	}
	public final void setIsEval(boolean value)
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
	public final boolean getIsHL()
	{
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
	public final boolean getIsFL()
	{
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
	public final boolean getIsFLHL()
	{
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
	 是否有流程完成条件
	 
	*/
	public final boolean getIsCCFlow()
	{
		return this.GetValBooleanByKey(NodeAttr.IsCCFlow);
	}
	public final void setIsCCFlow(boolean value)
	{
		this.SetValByKey(NodeAttr.IsCCFlow, value);
	}
	/** 
	 接受人sql
	 
	*/
	public final String getDeliveryParas()
	{
		String s = this.GetValStringByKey(NodeAttr.DeliveryParas);
		s = s.replace("~", "'");

		if (this.getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormEmpsField && StringHelper.isNullOrEmpty(s) == true)
		{
			return "ToEmps";
		}
		return s;
	}
	public final void setDeliveryParas(String value)
	{
		this.SetValByKey(NodeAttr.DeliveryParas, value);
	}
	public final boolean getIsExpSender()
	{
		return this.GetValBooleanByKey(NodeAttr.IsExpSender);
	}
	public final void setIsExpSender(boolean value)
	{
		this.SetValByKey(NodeAttr.IsExpSender, value);
	}

	/** 
	 是不是PC工作节点
	 
	*/
	public final boolean getIsPCNode()
	{
		return false;
	}
	/** 
	 工作性质
	 
	*/
	public final String getNodeWorkTypeText()
	{
		return this.getHisNodeWorkType().toString();
	}

		///#endregion


		///#region 公共方法 (用户执行动作之后,所要做的工作)
	/** 
	 用户执行动作之后,所要做的工作		 
	 
	 @return 返回消息,运行的消息
	*/
	public final String AfterDoTask()
	{
		return "";
	}

		///#endregion


		
	/** 
	 节点
	 
	*/
	public Node()
	{
	}
	/** 
	 节点
	 
	 @param _oid 节点ID	
	 * @throws Exception 
	*/
	public Node(int _oid) throws Exception
	{
		this.setNodeID(_oid);
		if (SystemConfig.getIsDebug())
		{
			if (this.RetrieveFromDBSources() <= 0)
			{
				throw new RuntimeException("Node Retrieve 错误没有ID=" + _oid);
			}
		}
		else
		{
			// 去掉缓存.
			this.RetrieveFromDBSources();
			//if (this.Retrieve() <= 0)
			//    throw new Exception("Node Retrieve 错误没有ID=" + _oid);
		}
	}
	public Node(String ndName) throws Exception
	{
		ndName = ndName.replace("ND", "");
		this.setNodeID(Integer.parseInt(ndName));

		if (SystemConfig.getIsDebug())
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
	public final String getEnName()
	{
		return "ND" + this.getNodeID();
	}
	public final String getEnsName()
	{
		return "ND" + this.getNodeID() + "s";
	}
	/** 
	 节点意见名称，如果为空则取节点名称.
	 
	*/
	public final String getFWCNodeName()
	{
		String str = this.GetValStringByKey(FrmWorkCheckAttr.FWCNodeName);
		if (StringHelper.isNullOrEmpty(str))
		{
			return this.getName();
		}
		return str;
	}

	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}



		Map map = new Map("WF_Node", "节点");

        map.Java_SetDepositaryOfEntity(Depositary.Application);
        map.Java_SetDepositaryOfMap( Depositary.Application);

     //   #region 基本属性.
        map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
        map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 100, 10);
        map.AddTBString(NodeAttr.Tip, null, "操作提示", true, true, 0, 100, 10, false);

        map.AddTBInt(NodeAttr.Step,1, "流程步骤", true, false);

        map.AddTBString(NodeAttr.ICON, null, "节点ICON图片路径", true, false, 0, 70, 10);

        map.AddTBInt(NodeAttr.NodeWorkType, 0, "节点类型", false, false);
        map.AddTBInt(NodeAttr.SubThreadType, 0, "子线程ID", false, false);

        map.AddTBString(NodeAttr.FK_Flow, null, "FK_Flow", false, false, 0, 3, 10);
        map.AddTBInt(NodeAttr.IsGuestNode, 0, "是否是客户执行节点", false, false);

        map.AddTBString(NodeAttr.FlowName, null, "流程名", false, true, 0, 100, 10);

        //map.AddTBString(NodeAttr.FK_FlowSort, null, "FK_FlowSort", false, true, 0, 4, 10);
        //map.AddTBString(NodeAttr.FK_FlowSortT, null, "FK_FlowSortT", false, true, 0, 100, 10);

        map.AddTBString(NodeAttr.FrmAttr, null, "FrmAttr", false, true, 0, 300, 10);
        map.AddTBInt(NodeAttr.IsBUnit, 0, "是否是节点模版(业务单元)", true, false);
      //  #endregion 基本属性.

        //#region 审核组件.
        map.AddTBInt(NodeAttr.FWCSta, 0, "审核组件", false, false);
        map.AddTBFloat(FrmWorkCheckAttr.FWC_H, 0, "审核组件高度", false, true);
        map.AddTBInt(FrmWorkCheckAttr.FWCOrderModel, 0, "协作模式下操作员显示顺序", false, false);
        //#endregion 审核组件.

        //#region 考核属性.
        map.AddTBFloat(NodeAttr.TimeLimit, 2, "限期(天)", true, false); //"限期(天)".
        map.AddTBFloat(NodeAttr.TSpanHour, 0, "小时", true, false); //"限期(天)".
        map.AddTBInt(NodeAttr.TWay, 0, "时间计算方式", true, false); //0=不计算节假日,1=计算节假日.

        map.AddTBInt(NodeAttr.TAlertRole, 0, "逾期提醒规则", false, false); //"限期(天)"
        map.AddTBInt(NodeAttr.TAlertWay, 0, "逾期提醒方式", false, false); //"限期(天)"

        map.AddTBFloat(NodeAttr.WarningDay, 1, "工作预警(天)", true, false);    // "警告期限(0不警告)"
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

        //#endregion 考核属性.

        map.AddTBString(FrmWorkCheckAttr.FWCNodeName, null, "节点意见名称", true, false, 0, 100, 10);
        map.AddTBString(NodeAttr.Doc, null, "描述", true, false, 0, 100, 10);
        map.AddBoolean(NodeAttr.IsTask, true, "允许分配工作否?", true, true);

        //退回相关.
        map.AddTBInt(NodeAttr.ReturnRole, 2, "退回规则", true, true);
        map.AddTBString(NodeAttr.ReturnReasonsItems, null, "退回原因", true, false, 0, 50, 10, true);
        map.AddTBString(NodeAttr.ReturnAlert, null, "被退回后信息提示", true, false, 0, 50, 10, true);
        map.AddBoolean(FrmWorkCheckAttr.FWCIsShowReturnMsg, false, "是否显示退回信息？", true, true, true);


        map.AddTBInt(NodeAttr.DeliveryWay, 0, "访问规则", true, true);
        map.AddTBInt(NodeAttr.IsExpSender, 1, "本节点接收人不允许包含上一步发送人", true, true);

        map.AddTBInt(NodeAttr.CancelRole, 0, "撤销规则", true, true);
        map.AddTBInt(NodeAttr.CancelDisWhenRead, 0, "撤销规则", true, true);
        

        map.AddTBInt(NodeAttr.WhenNoWorker, 0, "未找到处理人时", true, true);
        map.AddTBString(NodeAttr.DeliveryParas, null, "访问规则设置", true, false, 0, 600, 10);
        map.AddTBString(NodeAttr.NodeFrmID, null, "节点表单ID", true, false, 0, 50, 10);

        map.AddTBInt(NodeAttr.CCRole, 0, "抄送规则", true, true);
        map.AddTBInt(NodeAttr.CCWriteTo, 0, "抄送数据写入规则", true, true);

        map.AddTBInt(BtnAttr.DelEnable, 0, "删除规则", true, true);
        map.AddTBInt(NodeAttr.SaveModel, 0, "保存模式", true, true);


         
        map.AddTBInt(NodeAttr.IsCanDelFlow, 0, "是否可以删除流程", true, true);

        map.AddTBInt(NodeAttr.ThreadKillRole, 0, "子线程删除方式", true, true);

        map.AddTBInt(NodeAttr.TodolistModel, 0, "多人处理规则", true, true);

        //add.
        map.AddTBInt(NodeAttr.TeamLeaderConfirmRole, 0, "组长确认规则", true, true);
        map.AddTBString(NodeAttr.TeamLeaderConfirmDoc, null, "组长确认设置内容", true, false, 0, 100, 10);


        map.AddTBInt(NodeAttr.IsAllowRepeatEmps, 0, "是否允许子线程接受人员重复(对子线程点有效)?", true, true);
        map.AddTBInt(NodeAttr.IsBackTracking, 0, "是否可以在退回后原路返回(只有启用退回功能才有效)", true, true);
        map.AddTBInt(NodeAttr.IsRM, 1, "是否启用投递路径自动记忆功能?", true, true);
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

        //父子流程.
        map.AddTBString(NodeAttr.SFActiveFlows, null, "启动的子流程", true, false, 0, 100, 10);

        // 批处理.
        map.AddTBInt(NodeAttr.BatchRole, 0, "批处理", true, true);
        map.AddTBInt(NodeAttr.BatchListCount, 12, "批处理数量", true, true);
        map.AddTBString(NodeAttr.BatchParas, null, "参数", true, false, 0, 500, 10);
        map.AddTBInt(NodeAttr.PrintDocEnable, 0, "打印方式", true, true);


        //与未来处理人有关系.
      //  map.AddTBInt(NodeAttr.IsFullSA, 1, "是否计算未来处理人?", false, false);
        //map.AddTBInt(NodeAttr.IsFullSATime, 0, "是否计算未来接受与处理时间?", false, false);
        //map.AddTBInt(NodeAttr.IsFullSAAlert, 0, "是否接受未来工作到达消息提醒?", false, false);

        //表单相关.
        map.AddTBInt(NodeAttr.FormType, 1, "表单类型", false, false);
        map.AddTBString(NodeAttr.FormUrl, "http://", "表单URL", true, false, 0, 150, 10);
        map.AddTBString(NodeAttr.DeliveryParas, null, "接受人SQL", true, false, 0, 300, 10, true);
        map.AddTBInt(NodeAttr.TurnToDeal, 0, "转向处理", false, false);
        map.AddTBString(NodeAttr.TurnToDealDoc, null, "发送后提示信息", true, false, 0, 200, 10, true);
        map.AddTBInt(NodeAttr.NodePosType, 0, "位置", false, false);
        map.AddTBInt(NodeAttr.IsCCFlow, 0, "是否有流程完成条件", false, false);
        map.AddTBString(NodeAttr.HisStas, null, "岗位", false, false, 0, 3000, 10);
        map.AddTBString(NodeAttr.HisDeptStrs, null, "部门", false, false, 0, 3000, 10);
        map.AddTBString(NodeAttr.HisToNDs, null, "转到的节点", false, false, 0, 50, 10);
        map.AddTBString(NodeAttr.HisBillIDs, null, "单据IDs", false, false, 0, 50, 10);
        //  map.AddTBString(NodeAttr.HisEmps, null, "HisEmps", false, false, 0, 3000, 10);
        map.AddTBString(NodeAttr.HisSubFlows, null, "HisSubFlows", false, false, 0, 30, 10);
        map.AddTBString(NodeAttr.PTable, null, "物理表", false, false, 0, 100, 10);

        map.AddTBString(NodeAttr.ShowSheets, null, "显示的表单", false, false, 0, 100, 10);
        map.AddTBString(NodeAttr.GroupStaNDs, null, "岗位分组节点", false, false, 0, 500, 10);
        map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
        map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

        map.AddTBString(NodeAttr.FocusField, null, "焦点字段", false, false, 0, 30, 10);
        map.AddTBString(NodeAttr.JumpToNodes, null, "可跳转的节点", true, false, 0, 100, 10, true);

        map.AddTBString(NodeAttr.RefOneFrmTreeType, "", "独立表单类型", false, false, 0, 100, 10);//RefOneFrmTree

        //按钮控制部分.
        // map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0, 50, 10, true);
        map.AddTBAtParas(500);

        // 启动子线程参数 2013-01-04
        map.AddTBInt(NodeAttr.SubFlowStartWay, 0, "子线程启动方式", true, false);
        map.AddTBString(NodeAttr.SubFlowStartParas, null, "启动参数", true, false, 0, 100, 10);



        // 启动自动运行. 2013-01-04
        map.AddTBInt(NodeAttr.AutoRunEnable, 0, "是否启动自动运行？", true, false);
        map.AddTBString(NodeAttr.AutoRunParas, null, "自动运行参数", true, false, 0, 100, 10);
		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 我能处理当前的节点吗？
	 
	 @return 
	*/
	public final boolean CanIdoIt()
	{
		return false;
	}

 



	/** 
	 删除前的逻辑处理.
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeDelete() throws Exception
	{
		//判断是否可以被删除. 
		int num = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerlist WHERE FK_Node=" + this.getNodeID());
		if (num != 0)
		{
			throw new RuntimeException("@该节点[" + this.getNodeID() + "," + this.getName() + "]有待办工作存在，您不能删除它.");
		}

		// 删除它的节点。
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + this.getNodeID());
		md.Delete();

		// 删除分组.
		BP.Sys.GroupFields gfs = new BP.Sys.GroupFields();
		gfs.Delete(BP.Sys.GroupFieldAttr.EnName, md.getNo());

		
		try {
			//删除它的明细。
			BP.Sys.MapDtls dtls = new BP.Sys.MapDtls(md.getNo());
			dtls.Delete();
		} catch (Exception e) {
			Log.DebugWriteError("Node beforeDelete()删除它的明细"+ e);
		}

		//删除框架
		try {
			BP.Sys.MapFrames frams = new BP.Sys.MapFrames(md.getNo());
			frams.Delete();
		} catch (Exception e) {
			Log.DebugWriteError("Node beforeDelete() 删除框架"+ e);		
		}

		 
		// 删除扩展
		try {
			BP.Sys.MapExts exts = new BP.Sys.MapExts(md.getNo());
			exts.Delete();
		} catch (Exception e) {
			Log.DebugWriteError("Node beforeDelete() 删除扩展"+ e);		
		}

		//删除节点与岗位的对应.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_NodeStation WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_NodeEmp  WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_NodeDept WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_FrmNode  WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_CCEmp  WHERE FK_Node=" + this.getNodeID());
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_CH  WHERE FK_Node=" + this.getNodeID());


		//删除附件.
		BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmAttachment  WHERE FK_MapData='" + this.getNodeID() + "'");
		return super.beforeDelete();
	}
	/** 
	 文书流程
	 @param md
	 * @throws Exception 
	*/
	private void AddDocAttr(BP.Sys.MapData md) throws Exception
	{
		//如果是单据流程？ 
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
	 修复map
	 * @throws Exception 
	 
	*/
	public final String RepareMap() throws Exception
	{
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + this.getNodeID());
		if (md.RetrieveFromDBSources() == 0)
		{
			this.CreateMap();
			return "";
		}
		//检查分组
		md.RepairMap();

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
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

		if (attr.IsExit(MapAttrAttr.KeyOfEn, "FID", MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
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

		if (attr.IsExit(MapAttrAttr.KeyOfEn, WorkAttr.RDT, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
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

		if (attr.IsExit(MapAttrAttr.KeyOfEn, WorkAttr.CDT, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.CDT);
			if (this.getIsStartNode())
			{
				attr.setName("发起时间"); // "发起时间";
			} else
			{
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
		}


		if (attr.IsExit(MapAttrAttr.KeyOfEn, WorkAttr.Rec, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.Rec);
			if (this.getIsStartNode() == false)
			{
				attr.setName("记录人"); // "记录人";
			} else
			{
				attr.setName("发起人"); // "发起人";
			}
			
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(20);
			attr.setMinLen(0);
			attr.setDefVal("@WebUser.getNo()");
			attr.Insert();
		}


		if (attr.IsExit(MapAttrAttr.KeyOfEn, WorkAttr.Emps, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn(WorkAttr.Emps);
			attr.setName(WorkAttr.Emps);
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(400);
			attr.setMinLen(0);
			attr.Insert();
		}

		if (attr.IsExit(MapAttrAttr.KeyOfEn, StartWorkAttr.FK_Dept, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			attr = new MapAttr();
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

		Flow fl = new Flow(this.getFK_Flow());
		if (fl.getIsMD5() && attr.IsExit(MapAttrAttr.KeyOfEn, WorkAttr.MD5, MapAttrAttr.FK_MapData, md.getNo()) == false)
		{
			// 如果是MD5加密流程. 
			attr = new MapAttr();
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

		if (this.getNodePosType() == NodePosType.Start)
		{

			if (attr.IsExit(MapAttrAttr.KeyOfEn, StartWorkAttr.Title, MapAttrAttr.FK_MapData, md.getNo()) == false)
			{
				attr = new MapAttr();
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

			//if (attr.IsExit(MapAttrAttr.KeyOfEn, "faqiren", MapAttrAttr.FK_MapData, md.getNo()) == false)
			//{
			//    attr = new BP.Sys.MapAttr();
			//    attr.FK_MapData = md.getNo();
			//    attr.HisEditType = BP.En.EditType.Edit;
			//    attr.KeyOfEn = "faqiren";
			//    attr.Name = "发起人"; // "发起人";
			//    attr.MyDataType = BP.DA.DataType.AppString;
			//    attr.UIContralType = UIContralType.TB;
			//    attr.LGType = FieldTypeS.Normal;
			//    attr.UIVisible = true;
			//    attr.UIIsEnable = false;
			//    attr.UIIsLine = false;
			//    attr.MinLen = 0;
			//    attr.MaxLen = 200;
			//    attr.Idx = -100;
			//    attr.DefVal = "WebUser.No";
			//    attr.X = (float)159.2;
			//    attr.Y = (float)102.8;
			//    attr.Insert();
			//}

			//if (attr.IsExit(MapAttrAttr.KeyOfEn, "faqishijian", MapAttrAttr.FK_MapData, md.getNo()) == false)
			//{
			//    attr = new BP.Sys.MapAttr();
			//    attr.FK_MapData = md.getNo();
			//    attr.HisEditType = BP.En.EditType.Edit;
			//    attr.KeyOfEn = "faqishijian";
			//    attr.Name = "发起时间"; //"发起时间";
			//    attr.MyDataType = BP.DA.DataType.AppDateTime;
			//    attr.UIContralType = UIContralType.TB;
			//    attr.LGType = FieldTypeS.Normal;
			//    attr.UIVisible = true;
			//    attr.UIIsEnable = false;
			//    attr.DefVal = "@RDT";
			//    attr.Tag = "1";
			//    attr.X = (float)324;
			//    attr.Y = (float)102.8;
			//    attr.Insert();
			//}


			if (attr.IsExit(MapAttrAttr.KeyOfEn, "FK_NY", MapAttrAttr.FK_MapData, md.getNo()) == false)
			{
				attr = new MapAttr();
				attr.setFK_MapData(md.getNo());
				attr.setHisEditType(BP.En.EditType.UnDel);
				attr.setKeyOfEn("FK_NY");
				attr.setName("年月"); // "年月";
				attr.setMyDataType(BP.DA.DataType.AppString);
				attr.setUIContralType(UIContralType.TB);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setLGType(FieldTypeS.Normal);
				// attr.setUIBindKey("BP.Pub.NYs";
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setMinLen(0);
				attr.setMaxLen(7);
				attr.Insert();
			}


			if (attr.IsExit(MapAttrAttr.KeyOfEn, "MyNum", MapAttrAttr.FK_MapData, md.getNo()) == false)
			{
				attr = new MapAttr();
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
		String msg = "";
		if (!this.getFocusField().equals(""))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getFocusField(), MapAttrAttr.FK_MapData, md.getNo()) == false)
			{
				msg += "@焦点字段 " + this.getFocusField() + " 被非法删除了.";
				//this.FocusField = "";
				//this.DirectUpdate();
			}
		}
		return msg;
	}
	/** 
	 建立map
	 * @throws Exception 
	*/
	public final void CreateMap() throws Exception
	{
		//创建节点表单.
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + this.getNodeID());
		md.Delete();

		md.setName(this.getName());
		if (this.getHisFlow().getHisDataStoreModel() == DataStoreModel.SpecTable)
		{
			md.setPTable(this.getHisFlow().getPTable());
		}
		md.Insert();

		MapAttr attr = new MapAttr();
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

		if (this.getHisFlow().getFlowAppType() == FlowAppType.DocFlow)
		{
			this.AddDocAttr(md);
		}

		attr = new MapAttr();
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

		attr = new MapAttr();
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

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.CDT);
		if (this.getIsStartNode())
		{
			attr.setName("发起时间"); //"发起时间";
		}
		else
		{
			attr.setName("完成时间"); //"完成时间";
		}

		attr.setMyDataType(BP.DA.DataType.AppDateTime);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setDefVal("@RDT");
		attr.setTag("1");
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.Rec);
		if (this.getIsStartNode() == false)
		{
			attr.setName("记录人"); // "记录人";
		}
		else
		{
			attr.setName("发起人"); //"发起人";
		}

		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setMaxLen(20);
		attr.setMinLen(0);
		attr.setDefVal("@WebUser.getNo()");
		attr.Insert();

		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(WorkAttr.Emps);
		attr.setName("Emps");
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setMaxLen(400);
		attr.setMinLen(0);
		attr.Insert();


		attr = new MapAttr();
		attr.setFK_MapData(md.getNo());
		attr.setHisEditType(BP.En.EditType.UnDel);
		attr.setKeyOfEn(StartWorkAttr.FK_Dept);
		attr.setName("操作员部门"); // "操作员部门";
		attr.setMyDataType(BP.DA.DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setLGType(FieldTypeS.Normal);
		// attr.setUIBindKey("BP.Port.Depts";
		attr.setUIVisible(false);
		attr.setUIIsEnable(false);
		attr.setMinLen(0);
		attr.setMaxLen(32);
		attr.Insert();

		if (this.getNodePosType() == NodePosType.Start)
		{
			//开始节点信息.
			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.Edit);
			// attr.setedit
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


			attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn("FK_NY");
			attr.setName("年月"); // "年月";
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setLGType(FieldTypeS.Normal);
			// attr.setUIBindKey("BP.Pub.NYs";
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(7);
			attr.Insert();
			
			attr = new MapAttr();
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