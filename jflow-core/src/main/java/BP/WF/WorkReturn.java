package BP.WF;

import java.util.Date;

import BP.En.*;
import BP.DA.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.WF.Data.*;

/** 
 处理工作退回
 
*/
public class WorkReturn
{
	/** 
	 从节点
	 
	*/
	private Node HisNode = null;
	/** 
	 退回到节点
	 
	*/
	private Node ReturnToNode = null;
	/** 
	 工作ID
	 
	*/
	private long WorkID = 0;
	/** 
	 流程ID
	 
	*/
	private long FID = 0;
	/** 
	 是否按原路返回?
	 
	*/
	private boolean IsBackTrack = false;
	/** 
	 退回原因
	 
	*/
	private String Msg = "退回原因未填写.";
	/** 
	 当前节点
	 
	*/
	private Work HisWork = null;
	/** 
	 退回到节点
	 
	*/
	private Work ReurnToWork = null;
	private String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
	private Paras ps;
	public String ReturnToEmp = null;

	/** 
	 工作退回
	 
	 @param fk_flow 流程编号
	 @param workID WorkID
	 @param fid 流程ID
	 @param currNodeID 从节点
	 @param ReturnToNodeID 退回到节点
	 @param reutrnToEmp 退回到人
	 @param isBackTrack 是否需要原路返回？
	 @param returnInfo 退回原因
	 * @throws Exception 
	*/
	public WorkReturn(String fk_flow, long workID, long fid, int currNodeID, int ReturnToNodeID, String reutrnToEmp, boolean isBackTrack, String returnInfo) throws Exception
	{
		this.HisNode = new Node(currNodeID);
		this.ReturnToNode = new Node(ReturnToNodeID);
		this.WorkID = workID;
		this.FID = fid;
		this.IsBackTrack = isBackTrack;
		this.Msg = returnInfo;
		this.ReturnToEmp = reutrnToEmp;

		//当前工作.
		this.HisWork = this.HisNode.getHisWork();

		this.HisWork.setOID(workID);
		this.HisWork.RetrieveFromDBSources();

		//退回工作
		this.ReurnToWork = this.ReturnToNode.getHisWork();
		this.ReurnToWork.setOID(workID);
		if (this.ReurnToWork.RetrieveFromDBSources() == 0)
		{
			this.ReurnToWork.setOID(fid);
			this.ReurnToWork.RetrieveFromDBSources();
		}
	}
	/** 
	 删除两个节点之间的业务数据与流程引擎控制数据.
	 
	*/
	private void DeleteSpanNodesGenerWorkerListData()
	{
		if (this.IsBackTrack == true)
		{
			return;
		}

		Paras ps = new Paras();
		String dbStr = SystemConfig.getAppCenterDBVarStr();

		// 删除FH, 不管是否有这笔数据.
		ps.clear();
		ps.SQL = "DELETE FROM WF_GenerFH WHERE FID=" + dbStr + "FID";
		ps.Add("FID", this.WorkID);
		DBAccess.RunSQL(ps);

		//如果不是退回并原路返回，就需要清除 两个节点之间的数据, 包括WF_GenerWorkerList的数据.
		if (this.ReturnToNode.getIsStartNode() == true)
		{
			// 删除其子线程流程.
			ps.clear();
			ps.SQL = "DELETE FROM WF_GenerWorkFlow WHERE FID=" + dbStr + "FID ";
			ps.Add("FID", this.WorkID);
			DBAccess.RunSQL(ps);

			//如果退回到了开始的节点，就删除出开始节点以外的数据，不要删除节点表单数据，这样会导致流程轨迹打不开.
			ps.clear();
			ps.SQL = "DELETE FROM WF_GenerWorkerList WHERE FK_Node!=" + dbStr + "FK_Node AND (WorkID=" + dbStr + "WorkID1 OR FID=" + dbStr + "WorkID2)";
			ps.Add(GenerWorkerListAttr.FK_Node, this.ReturnToNode.getNodeID());
			ps.Add("WorkID1", this.WorkID);
			ps.Add("WorkID2", this.WorkID);
			DBAccess.RunSQL(ps);
			return;
		}

		//找到发送到退回的时间点，把从这个时间点以来的数据都要删除掉.
		ps.clear();
		ps.SQL = "SELECT RDT,ActionType,NDFrom FROM ND" + Integer.parseInt(this.HisNode.getFK_Flow()) + "Track WHERE  NDFrom=" + dbStr + "NDFrom AND WorkID=" + dbStr + "WorkID AND ActionType=" + ActionType.Forward.getValue() + " ORDER BY RDT desc ";
		ps.Add("NDFrom", this.ReturnToNode.getNodeID());
		ps.Add("WorkID", this.WorkID);
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() >= 1)
		{
			String rdt = dt.Rows.get(0).getValue(0).toString();
			
			ps.clear();
			ps.SQL = "SELECT ActionType,NDFrom FROM ND" + Integer.parseInt(this.HisNode.getFK_Flow()) + "Track WHERE   RDT >=" + dbStr + "RDT AND WorkID=" + dbStr + "WorkID ORDER BY RDT ";
			ps.Add("RDT", rdt);
			ps.Add("WorkID", this.WorkID);
			dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

			for (DataRow dr : dt.Rows)
			{
				ActionType at = ActionType.forValue(Integer.parseInt(dr.getValue("ActionType").toString()));
				int nodeid = Integer.parseInt(dr.getValue("NDFrom").toString());
				if (nodeid == this.ReturnToNode.getNodeID())
				{
					continue;
				}

				ps.clear();
				ps.SQL = "DELETE FROM WF_GenerWorkerList WHERE FK_Node=" + dbStr + "FK_Node AND (WorkID=" + dbStr + "WorkID1 OR FID=" + dbStr + "WorkID2) ";
				ps.Add("FK_Node", nodeid);
				ps.Add("WorkID1", this.WorkID);
				ps.Add("WorkID2", this.WorkID);
				DBAccess.RunSQL(ps);
				
				  //删除审核意见, 已经考虑到了  @于庆海翻译。
				ps.clear();
				ps.SQL = "DELETE FROM ND" + Integer.parseInt(this.ReturnToNode.getFK_Flow()) + "Track WHERE NDFrom=" + dbStr + "NDFrom AND  (WorkID=" + dbStr + "WorkID1 OR FID=" + dbStr + "WorkID2) AND ActionType=22";
				ps.Add("NDFrom", nodeid);
				ps.Add("WorkID1", this.WorkID);
				ps.Add("WorkID2", this.WorkID);
				DBAccess.RunSQL(ps);
				
				
				
			}
		}


		//删除当前节点的数据.
		ps.clear();
		ps.SQL = "DELETE FROM WF_GenerWorkerList WHERE FK_Node=" + dbStr + "FK_Node AND (WorkID=" + dbStr + "WorkID1 OR FID=" + dbStr + "WorkID2) ";
		ps.Add("FK_Node", this.HisNode.getNodeID());
		ps.Add("WorkID1", this.WorkID);
		ps.Add("WorkID2", this.WorkID);
		DBAccess.RunSQL(ps);

		//  string sql = "SELECT * FROM ND" + int.Parse(this.HisNode.FK_Flow) + "Track WHERE  NDTo='"+this.ReturnToNode.NodeID+" AND WorkID="+this.WorkID;
		//  ActionType
	}
	/** 
	 队列节点上一个人退回另外一个人.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoOrderReturn() throws Exception
	{
		//退回前事件
		String atPara = "@ToNode=" + this.ReturnToNode.getNodeID();
		String msg = this.HisNode.getHisFlow().DoFlowEventEntity(EventListOfNode.ReturnBefore, this.HisNode, this.HisWork, atPara);

		if (!this.HisNode.getFocusField().equals(""))
		{
			try
			{
				String focusField = "";
				String[] focusFields = this.HisNode.getFocusField().split("[@]", -1);
				if (focusFields.length >= 2)
				{
					focusField = focusFields[1];
				}
				else
				{
					focusField = focusFields[0];
				}



				// 把数据更新它。
				this.HisWork.Update(focusField, "");
			}
			catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLineError("退回时更新焦点字段错误:" + ex.getMessage());
			}
		}

		//退回到人.
		Emp returnToEmp = new Emp(this.ReturnToEmp);

		// 退回状态。
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET WFState=" + dbStr + "WFState,FK_Node=" + dbStr + "FK_Node,NodeName=" + dbStr + "NodeName,TodoEmps=" + dbStr + "TodoEmps, TodoEmpsNum=0 WHERE  WorkID=" + dbStr + "WorkID";
		ps.Add(GenerWorkFlowAttr.WFState, WFState.ReturnSta.getValue());
		ps.Add(GenerWorkFlowAttr.FK_Node, this.ReturnToNode.getNodeID());
		ps.Add(GenerWorkFlowAttr.NodeName, this.ReturnToNode.getName());

		ps.Add(GenerWorkFlowAttr.TodoEmps, returnToEmp.getNo() + "," + returnToEmp.getName() + ";");

		ps.Add(GenerWorkFlowAttr.WorkID, this.WorkID);

		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0,IsRead=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND FK_Emp=" + dbStr + "FK_Emp ";
		ps.Add("FK_Node", this.ReturnToNode.getNodeID());
		ps.Add("WorkID", this.WorkID);
		ps.Add("FK_Emp", this.ReturnToEmp);
		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=1000,IsRead=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND FK_Emp=" + dbStr + "FK_Emp ";
		ps.Add("FK_Node", this.HisNode.getNodeID());
		ps.Add("WorkID", this.WorkID);
		ps.Add("FK_Emp", WebUser.getNo());
		DBAccess.RunSQL(ps);

		//更新流程报表数据.
		ps = new Paras();
		ps.SQL = "UPDATE " + this.HisNode.getHisFlow().getPTable() + " SET  WFState=" + dbStr + "WFState, FlowEnder=" + dbStr + "FlowEnder, FlowEndNode=" + dbStr + "FlowEndNode WHERE OID=" + dbStr + "OID";
		ps.Add("WFState", WFState.ReturnSta.getValue());
		ps.Add("FlowEnder", WebUser.getNo());
		ps.Add("FlowEndNode", ReturnToNode.getNodeID());
		ps.Add("OID", this.WorkID);
		DBAccess.RunSQL(ps);

		////从工作人员列表里找到被退回人的接受人.
		//GenerWorkerList gwl = new GenerWorkerList();
		//gwl.Retrieve(GenerWorkerListAttr.FK_Node, this.ReturnToNode.NodeID, GenerWorkerListAttr.WorkID, this.WorkID);

		// 记录退回轨迹。
		ReturnWork rw = new ReturnWork();
		rw.setWorkID(this.WorkID);
		rw.setReturnToNode(this.ReturnToNode.getNodeID());
		rw.setReturnNodeName(this.HisNode.getName());

		rw.setReturnNode(this.HisNode.getNodeID()); // 当前退回节点.
		rw.setReturnToEmp(this.ReturnToEmp); //退回给。
		rw.setBeiZhu(Msg);

		rw.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
		rw.Insert();

		// 加入track.
		this.AddToTrack(ActionType.Return, returnToEmp.getNo(), returnToEmp.getName(), this.ReturnToNode.getNodeID(), this.ReturnToNode.getName(), Msg);

		try
		{
			// 记录退回日志.
			ReorderLog(this.HisNode, this.ReturnToNode, rw);
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteWarning(ex.getMessage());
		}

		// 以退回到的节点向前数据用递归删除它。
		if (IsBackTrack == false)
		{
			//如果退回不需要原路返回，就删除中间点的数据。

			//#warning 没有考虑两种流程数据存储模式。
			//DeleteToNodesData(this.ReturnToNode.HisToNodes);
		}

		// 向他发送消息。
		if (Glo.getIsEnableSysMessage() == true)
		{
			//   WF.Port.WFEmp wfemp = new WFEmp(wnOfBackTo.HisWork.Rec);
			String title = String.format("工作退回：流程:%1$s.工作:%2$s,退回人:%3$s,需您处理", this.HisNode.getFlowName(), this.ReturnToNode.getName(), WebUser.getName());

			BP.WF.Dev2Interface.Port_SendMsg(returnToEmp.getNo(), title, Msg, "RE" + this.HisNode.getNodeID() + this.WorkID, BP.WF.SMSMsgType.ReturnAfter, ReturnToNode.getFK_Flow(), ReturnToNode.getNodeID(), this.WorkID, this.FID);
		}

		//退回后事件
		String text = this.HisNode.getHisFlow().DoFlowEventEntity(EventListOfNode.ReturnAfter, this.HisNode, this.HisWork, atPara);
		if (text != null && text.length() > 1000)
		{
			text = "退回事件:无返回信息.";
		}
		// 返回退回信息.
		if (this.ReturnToNode.getIsGuestNode())
		{
			GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
			return "工作已经被您退回到(" + this.ReturnToNode.getName() + "),退回给(" + gwf.getGuestNo() + "," + gwf.getGuestName() + ").\n\r" + text;
		}
		else
		{
			return "工作已经被您退回到(" + this.ReturnToNode.getName() + "),退回给(" + returnToEmp.getNo() + "," + returnToEmp.getName() + ").\n\r" + text;
		}
	}
    /// <summary>
    /// 要退回到父流程上去@du.
    /// </summary>
    /// <returns></returns>
    private String ReturnToParentFlow() throws Exception
    {
        //当前 gwf.
        GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);

        //设置子流程信息.
        GenerWorkFlow gwfP = new GenerWorkFlow(gwf.getPWorkID());
        gwfP.setWFState( WFState.ReturnSta);
        gwfP.setFK_Node(this.ReturnToNode.getNodeID());
        gwfP.setNodeName(this.ReturnToNode.getName());

        //启用待办.
        GenerWorkerList gwl = new GenerWorkerList();
        GenerWorkerLists gwls = new GenerWorkerLists();
        gwls.Retrieve(GenerWorkerListAttr.FK_Node, gwfP.getFK_Node(),
        		GenerWorkerListAttr.WorkID, gwfP.getWorkID());
        String toEmps = "";
        for (GenerWorkerList item : gwls.ToJavaList())
        {
            item.setIsPassInt( 0);
            item.Update();
            gwl = item;
            
            toEmps += item.getFK_Emp() + "," + item.getFK_EmpText() + ",";
        }
        gwfP.setTodoEmps(toEmps);
        gwfP.Update();
        
       // #region 写入退回提示.
        // 记录退回轨迹。
        ReturnWork rw = new ReturnWork();
        rw.setWorkID( gwfP.getWorkID());
        rw.setReturnToNode(gwfP.getFK_Node());
        rw.setReturnNodeName(  gwfP.getNodeName());

        rw.setReturnNode( this.HisNode.getNodeID()); // 当前退回节点.
        rw.setReturnToEmp(  gwl.getFK_Emp()); //退回给。
        rw.setBeiZhu(Msg);
         
        // 去掉了 else .
        rw.setIsBackTracking(this.IsBackTrack);
        rw.setMyPK( DBAccess.GenerGUID());
        rw.Insert();


        // 加入track.
        this.AddToTrack(ActionType.Return, gwl.getFK_Emp(), gwl.getFK_EmpText(),
            this.ReturnToNode.getNodeID(), this.ReturnToNode.getName(), Msg);
         

        //删除当前的流程.
        BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(gwf.getFK_Flow(), this.WorkID, true);

        //设置当前为未读的状态.
        BP.WF.Dev2Interface.Node_SetWorkUnRead(gwfP.getWorkID());

        //返回退回信息.
        return "成功的退回到["+gwfP.getFlowName()+" - "+gwfP.getNodeName()+"],退回给["+gwfP.getTodoEmps()+"].";
    }
	/** 
	 执行退回.
	 
	 @return 返回退回信息
	 * @throws Exception 
	*/
	public final String DoIt() throws Exception
	{
		
		   // 增加要退回到父流程上去. by zhoupeng.
        if (!this.ReturnToNode.getFK_Flow().equals(this.HisNode.getFK_Flow()))
        {
            /*子流程要退回到父流程的情况.*/
            return ReturnToParentFlow();
        }
        
		if (this.HisNode.getNodeID() == this.ReturnToNode.getNodeID())
		{
			if (this.HisNode.getTodolistModel() == TodolistModel.Order)
			{
				//一个队列的模式，一个人退回给另外一个人 
				return DoOrderReturn();
			}
		}

		if (this.ReturnToNode.getTodolistModel() == TodolistModel.Order)
		{
			// 当退回到的节点是 队列模式或者是协作模式时. 
			return DoOrderReturn();
		}

//             删除退回选择的信息, forzhuhai: 退回后，删除发送人上次选择的信息.
//             * 
//             * 场景:
//             * 1, a b c d 节点 d节点退回给c 如果d的接收人是c来选择的, 他退回后要把d的选择信息删除掉.
//             * 2, a b c d 节点 d节点退回给a 如果 b c d 的任何一个接受人的范围是有上一步发送人来选择的，就要删除选择人的信息.
//             * 
//             * 

		//是否需要删除中间点. 
		boolean isNeedDeleteSpanNodes = true;
		String sql = "";
		for (Node nd : this.ReturnToNode.getHisToNodes().ToJavaList())
		{
			if (nd.getNodeID() == this.HisNode.getNodeID())
			{
				sql = "DELETE FROM WF_SelectAccper WHERE FK_Node=" + this.HisNode.getNodeID() + " AND WorkID=" + this.WorkID;
				BP.DA.DBAccess.RunSQL(sql);
				isNeedDeleteSpanNodes = false;
			}
		}

		//如果有中间步骤.
		if (isNeedDeleteSpanNodes)
		{
			//获得可以退回的节点，这个节点是有顺序的.
			DataTable dt= BP.WF.Dev2Interface.DB_GenerWillReturnNodes(this.HisNode.getNodeID(), this.WorkID, this.FID);
			boolean isDelBegin = false;
			for (DataRow dr : dt.Rows)
			{
				int nodeID = Integer.parseInt(dr.getValue("No").toString());

				if (nodeID == this.ReturnToNode.getNodeID())
				{
					isDelBegin = true; //如果等于当前的节点，就开始把他们删除掉.
				}

				if (isDelBegin)
				{
					sql = "DELETE FROM WF_SelectAccper WHERE FK_Node=" + nodeID + " AND WorkID=" + this.WorkID;
					BP.DA.DBAccess.RunSQL(sql);
				}
			}

			// 删除当前节点信息.
			sql = "DELETE FROM WF_SelectAccper WHERE FK_Node=" + this.HisNode.getNodeID() + " AND WorkID=" + this.WorkID;
			BP.DA.DBAccess.RunSQL(sql);
		}


		//删除.
		BP.WF.Dev2Interface.DeleteCheckInfo(this.HisNode.getFK_Flow(), this.WorkID, this.HisNode.getNodeID());

		switch (this.HisNode.getHisRunModel())
		{
			case Ordinary: // 1： 普通节点向下发送的
				switch (ReturnToNode.getHisRunModel())
				{
					case Ordinary: //1-1 普通节to普通节点 
						return ExeReturn1_1();
					case FL: // 1-2 普通节to分流点 
						return ExeReturn1_1();
					case HL: //1-3 普通节to合流点 
						return ExeReturn1_1();
					case FHL: //1-4 普通节点to分合流点 
						return ExeReturn1_1();
					case SubThread: //1-5 普通节to子线程点 
					default:
						throw new RuntimeException("@退回错误:非法的设计模式或退回模式.普通节to子线程点");
				}
			case FL: // 2: 分流节点向下发送的
				switch (this.ReturnToNode.getHisRunModel())
				{
					case Ordinary: //2.1 分流点to普通节点 
						return ExeReturn1_1();
					case FL: //2.2 分流点to分流点 
					case HL: //2.3 分流点to合流点,分合流点 
					case FHL:
						return ExeReturn1_1();
					case SubThread: // 2.4 分流点to子线程点 
						return ExeReturn2_4();
					   // throw new Exception("@退回错误:非法的设计模式或退回模式.分流点to子线程点,请反馈给管理员.");
					default:
						throw new RuntimeException("@没有判断的节点类型(" + ReturnToNode.getName() + ")");
				}
			case HL: // 3: 合流节点向下退回 
				switch (this.ReturnToNode.getHisRunModel())
				{
					case Ordinary: //3.1 普通工作节点 
						return ExeReturn1_1();
					case FL: //3.2 合流点向分流点退回 
						return ExeReturn3_2();
					case HL: //3.3 合流点 
					case FHL:
						throw new RuntimeException("@尚未完成.");
					case SubThread: //3.4 合流点向子线程退回 
						return ExeReturn3_4();
					default:
						throw new RuntimeException("@退回错误:非法的设计模式或退回模式.普通节to子线程点");
				}
			case FHL: // 4: 分流节点向下发送的 
				switch (this.ReturnToNode.getHisRunModel())
				{
					case Ordinary: //4.1 普通工作节点 
						return ExeReturn1_1();
					case FL: //4.2 分流点 
					case HL: //4.3 合流点 
					case FHL:
						throw new RuntimeException("@尚未完成.");
					case SubThread: //4.5 子线程
						return ExeReturn3_4();
					default:
						throw new RuntimeException("@没有判断的节点类型(" + this.ReturnToNode.getName() + ")");
				}
			case SubThread: // 5: 子线程节点向下发送的 
				switch (this.ReturnToNode.getHisRunModel())
				{
					case Ordinary: //5.1 普通工作节点 
						throw new RuntimeException("@非法的退回模式,,请反馈给管理员.");
					case FL: //5.2 分流点 
						//子线程退回给分流点.
					   return ExeReturn5_2();
					case HL: //5.3 合流点 
						throw new RuntimeException("@非法的退回模式,请反馈给管理员.");
					case FHL: //5.4 分合流点 
						return ExeReturn5_2();
						//throw new Exception("@目前不支持此场景下的退回,请反馈给管理员.");
					case SubThread: //5.5 子线程
						return ExeReturn1_1();
					default:
						throw new RuntimeException("@没有判断的节点类型(" + ReturnToNode.getName() + ")");
				}
			default:
				throw new RuntimeException("@没有判断的类型:" + this.HisNode.getHisRunModel());
		}
	}
	/** 
	 分流点退回给子线程
	 
	 @return 
	 * @throws Exception 
	*/
	private String ExeReturn2_4() throws Exception
	{
		//更新运动到节点,但是仍然是退回状态.
		GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
		gwf.setFK_Node(this.ReturnToNode.getNodeID());
		gwf.Update();

		//更新退回到的人员信息可见.
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, this.ReturnToNode.getNodeID());
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			item.setIsPassInt(0);
			item.Update();
			this.ReturnToEmp = item.getFK_Emp() + "," + item.getFK_EmpText();
		}

		// 去掉合流节点工作人员的待办.
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, this.HisNode.getNodeID());
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			item.setIsPassInt(0);
			item.setIsRead(false);
			item.Update();
		}

		//把分流节点的待办去掉. 
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FID, this.FID, GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo());
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			item.setIsPassInt(-2);
			item.Update();
		}
		return "成功的把信息退回到：" + this.ReturnToNode.getName() + " , 退回给:(" + this.ReturnToEmp + ")";
	}
	/** 
	 子线程退回给分流点
	 
	 @return 
	 * @throws Exception 
	*/
	private String ExeReturn5_2() throws Exception
	{
		GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
		gwf.setFK_Node(this.ReturnToNode.getNodeID());
		String info = "@工作已经成功的退回到（" + ReturnToNode.getName() + "）退回给：";

		//查询退回到的工作人员列表.
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, this.ReturnToNode.getNodeID());

		String toEmp = "";
		String toEmpName = "";
		if (gwls.size() ==1)
		{
			//有可能多次退回的情况，表示曾经退回过n次。
			for (GenerWorkerList item : gwls.ToJavaList())
			{
				item.setIsPass(false); // 显示待办, 这个是合流节点的工作人员.
				item.setIsRead(false);
				item.Update();
				info += item.getFK_Emp() + "," + item.getFK_EmpText();
				toEmp = item.getFK_Emp();
				toEmpName = item.getFK_EmpText();
				info += "("+item.getFK_Emp()+","+item.getFK_EmpText()+")";
			}
		}
		else
		{
			// 找到合流点的发送人.
			Nodes nds = this.HisNode.getFromNodes();
			gwls = new GenerWorkerLists();
			GenerWorkerList gwl = new GenerWorkerList();
			for (Node nd : nds.ToJavaList())
			{
				gwls.Retrieve(GenerWorkerListAttr.WorkID, this.FID, GenerWorkerListAttr.FK_Node, nd.getNodeID(), GenerWorkerListAttr.IsPass,1);
				if (gwls.size() == 0)
				{
					continue;
				}

				if (gwls.size() != 1)
				{
					throw new RuntimeException("@应该只有一个记录，现在有多个，可能错误。");
				}

				//求出分流节点的发送人.
				gwl = (GenerWorkerList)gwls.get(0);
				toEmp = gwl.getFK_Emp();
				toEmpName = gwl.getFK_EmpText();
				info += "(" + toEmp + "," + toEmpName + ")";
			}

			if (DotNetToJavaStringHelper.isNullOrEmpty(toEmp) == true)
			{
				throw new RuntimeException("@在退回时出现错误，没有找到分流节点的发送人。");
			}

			// 插入一条数据, 行程一个工作人员记录,这个记录就是子线程的延长点. 给合流点上的接受人设置待办.
			gwl.setWorkID(this.WorkID);
			gwl.setFID(this.FID);
			gwl.setIsPass(false);
			if (gwl.getIsExits()==false)
			{
				gwl.Insert();
			}
			else
			{
				gwl.Update();
			}
		}

		// 记录退回轨迹。
		ReturnWork rw = new ReturnWork();
		rw.setWorkID(this.WorkID);
		rw.setReturnToNode(this.ReturnToNode.getNodeID());
		rw.setReturnNodeName(this.HisNode.getName());

		rw.setReturnNode(this.HisNode.getNodeID()); // 当前退回节点.
		rw.setReturnToEmp(toEmp); //退回给。

		rw.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
		rw.setBeiZhu(Msg);
		rw.setIsBackTracking(this.IsBackTrack);
		rw.Insert();

		// 加入track.
		this.AddToTrack(ActionType.Return, toEmp, toEmpName, this.ReturnToNode.getNodeID(), this.ReturnToNode.getName(), Msg);

		BP.WF.Dev2Interface.Port_SendMsg(toEmp, gwf.getTitle(), Msg, "RE" + this.HisNode.getNodeID() + this.WorkID, BP.WF.SMSMsgType.ReturnAfter, ReturnToNode.getFK_Flow(), ReturnToNode.getNodeID(), this.WorkID, this.FID);

		gwf.setWFState(WFState.ReturnSta);
		gwf.setFK_Node(this.ReturnToNode.getNodeID());
		gwf.setNodeName(this.ReturnToNode.getName());
		gwf.setStarter(toEmp);
		gwf.setStarterName(toEmpName);
		gwf.setSender(WebUser.getNo());
		gwf.Update();

		//找到当前的工作数据.
		GenerWorkerList currWorker = new GenerWorkerList();
		int i = currWorker.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, this.HisNode.getNodeID());

		if (i != 1)
		{
			throw new RuntimeException("@当前的工作人员列表数据丢失了,流程引擎错误.");
		}

		//设置当前的工作数据为退回状态,让其不能看到待办, 这个是约定的值.
		currWorker.setIsPassInt(WFState.ReturnSta.getValue());
		currWorker.Update();

		// 返回退回信息.
		return info;
	}
	/** 
	 合流点向子线程退回
	 * @throws Exception 
	 
	*/
	private String ExeReturn3_4() throws Exception
	{
		GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
		gwf.setFK_Node(this.ReturnToNode.getNodeID());

		String info = "@工作已经成功的退回到（" + ReturnToNode.getName() + "）退回给：";
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, this.ReturnToNode.getNodeID());

		String toEmp = "";
		String toEmpName = "";
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			item.setIsPass(false);
			item.setIsRead(false);
			item.Update();
			info += item.getFK_Emp() + "," + item.getFK_EmpText();
			toEmp = item.getFK_Emp();
			toEmpName = item.getFK_EmpText();
		}

		//删除已经发向合流点的汇总数据.
		MapDtls dtls = new MapDtls("ND" + this.HisNode.getNodeID());
		for (MapDtl dtl : dtls.ToJavaList())
		{
			//如果是合流数据
			if (dtl.getIsHLDtl())
			{
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE OID=" + this.WorkID);
			}
		}

		// 记录退回轨迹。
		ReturnWork rw = new ReturnWork();
		rw.setWorkID(this.WorkID);
		rw.setReturnToNode(this.ReturnToNode.getNodeID());
		rw.setReturnNodeName(this.HisNode.getName());

		rw.setReturnNode(this.HisNode.getNodeID()); // 当前退回节点.
		rw.setReturnToEmp(toEmp); //退回给。

		rw.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
		rw.setBeiZhu(Msg);
		rw.setIsBackTracking(this.IsBackTrack);
		rw.Insert();

		// 加入track.
		this.AddToTrack(ActionType.Return, toEmp, toEmpName, this.ReturnToNode.getNodeID(), this.ReturnToNode.getName(), Msg);
		BP.WF.Dev2Interface.Port_SendMsg(toEmp, gwf.getTitle(), Msg, "RE" + this.HisNode.getNodeID() + this.WorkID, BP.WF.SMSMsgType.ReturnAfter, ReturnToNode.getFK_Flow(), ReturnToNode.getNodeID(), this.WorkID, this.FID);

		gwf.setWFState(WFState.ReturnSta);
		gwf.setSender(WebUser.getNo());
		gwf.Update();

		// 返回退回信息.
		return info;
	}
	/** 
	 合流点向分流点退回
	 * @throws Exception 
	 
	*/
	private String ExeReturn3_2() throws Exception
	{
		//删除分流点与合流点之间的子线程数据。
		//if (this.ReturnToNode.IsStartNode == false)
		//    throw new Exception("@没有处理的模式。");

		//删除子线程节点数据。
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkFlowAttr.FID, this.WorkID);

		for (GenerWorkerList item : gwls.ToJavaList())
		{
			// 删除 子线程数据 
			DBAccess.RunSQL("DELETE FROM ND" + item.getFK_Node() + " WHERE OID=" + item.getWorkID());
		}

		//删除流程控制数据。
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FID=" + this.WorkID);
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + this.WorkID);
		DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + this.WorkID);

		return ExeReturn1_1();
	}
	/** 
	 是否原路返回?
	 
	*/
	public boolean IsBackTracking = false;
	/** 
	 普通节点到普通节点的退回
	 
	 @return 
	 * @throws Exception 
	*/
	private String ExeReturn1_1() throws Exception
	{
		//为软通小杨处理rpt变量不能替换的问题.
		GERpt rpt = this.HisNode.getHisFlow().getHisGERpt();
		rpt.setOID(this.WorkID);
		if (rpt.RetrieveFromDBSources() == 0)
		{
			rpt.setOID(this.FID);
			rpt.RetrieveFromDBSources();
		}
		rpt.getRow().put("ReturnMsg", Msg);


		//退回前事件
		String atPara = "@ToNode=" + this.ReturnToNode.getNodeID();
		String msg = this.HisNode.getHisFlow().DoFlowEventEntity(EventListOfNode.ReturnBefore, this.HisNode, rpt, atPara);

		if (!this.HisNode.getFocusField().equals(""))
		{
			try
			{
				String focusField = "";
				String[] focusFields = this.HisNode.getFocusField().split("[@]", -1);
				if (focusFields.length >= 2)
				{
					focusField = focusFields[1];
				}
				else
				{
					focusField = focusFields[0];
				}

				// 把数据更新它。
				this.HisWork.Update(focusField, "");
			}
			catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLineError("退回时更新焦点字段错误:" + ex.getMessage());
			}
		}

		// 改变当前待办工作节点。            
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow  SET WFState=" + dbStr + "WFState,FK_Node=" + dbStr + "FK_Node,NodeName=" + dbStr + "NodeName,Sender=" + dbStr + "Sender  WHERE  WorkID=" + dbStr + "WorkID";
		ps.Add(GenerWorkFlowAttr.WFState, WFState.ReturnSta.getValue());
		ps.Add(GenerWorkFlowAttr.FK_Node, this.ReturnToNode.getNodeID());
		ps.Add(GenerWorkFlowAttr.NodeName, this.ReturnToNode.getName());
		ps.Add(GenerWorkFlowAttr.Sender, WebUser.getNo());
		ps.Add(GenerWorkFlowAttr.WorkID, this.WorkID);
		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0,IsRead=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID";
		ps.Add("FK_Node", this.ReturnToNode.getNodeID());
		ps.Add("WorkID", this.WorkID);
		DBAccess.RunSQL(ps);


		//更新流程报表数据.
		ps = new Paras();
		ps.SQL = "UPDATE " + this.HisNode.getHisFlow().getPTable() + " SET  WFState=" + dbStr + "WFState, FlowEnder=" + dbStr + "FlowEnder, FlowEndNode=" + dbStr + "FlowEndNode WHERE OID=" + dbStr + "OID";
		ps.Add("WFState", WFState.ReturnSta.getValue());
		ps.Add("FlowEnder", WebUser.getNo());
		ps.Add("FlowEndNode", ReturnToNode.getNodeID());

		ps.Add("OID", this.WorkID);
		DBAccess.RunSQL(ps);

		//从工作人员列表里找到被退回人的接受人.
		GenerWorkerList gwl = new GenerWorkerList();
		gwl.Retrieve(GenerWorkerListAttr.FK_Node, this.ReturnToNode.getNodeID(), GenerWorkerListAttr.WorkID, this.WorkID);

		// 记录退回轨迹。
		ReturnWork rw = new ReturnWork();
		rw.setWorkID(this.WorkID);
		rw.setReturnToNode(this.ReturnToNode.getNodeID());
		rw.setReturnNodeName(this.HisNode.getName());

		rw.setReturnNode(this.HisNode.getNodeID()); // 当前退回节点.
		rw.setReturnToEmp(gwl.getFK_Emp()); //退回给。
		rw.setBeiZhu(Msg);
		//杨玉慧 
		Emp emp = new Emp(rw.getReturnToEmp());
		//更新待办人员
		String updateToDoEmpSql = "UPDATE WF_GenerWorkFlow  SET TodoEmps='" + emp.getNo() + "," + emp.getName() + "',TodoEmpsNum=1 WHERE  WorkID=" + this.WorkID;
		//更新WF_GenerWorkFlow 的待办人员
		DBAccess.RunSQL(updateToDoEmpSql);

		if (this.HisNode.getTodolistModel() == TodolistModel.Order || this.HisNode.getTodolistModel() == TodolistModel.Sharing || this.HisNode.getTodolistModel() == TodolistModel.TeamupGroupLeader || this.HisNode.getTodolistModel() == TodolistModel.Teamup)
		{

			// 为软通小杨屏蔽， 共享，顺序，协作模式的退回并原路返回的 问题. 
			//rw.IsBackTracking = true; /*如果是共享，顺序，协作模式，都必须是退回并原路返回.*/

			// 需要更新当前人待办的状态, 把1000作为特殊标记，让其发送时可以找到他.
			String sql = "UPDATE WF_GenerWorkerlist SET IsPass=1000 WHERE FK_Node=" + this.HisNode.getNodeID() + " AND WorkID=" + this.WorkID + " AND FK_Emp='" + WebUser.getNo() + "'";
			if (BP.DA.DBAccess.RunSQL(sql) == 0)
			{
				throw new RuntimeException("@退回错误，没有找到要更新的目标数据.技术信息:" + sql);
			}

			//杨玉慧 将流程的  任务池状态设置为  NONE
			sql = "UPDATE WF_GenerWorkFlow SET TaskSta=0 WHERE  WorkID=" + this.WorkID;
			if (BP.DA.DBAccess.RunSQL(sql) == 0)
			{
				throw new RuntimeException("@退回错误，没有找到要更新的目标数据.技术信息:" + sql);
			}
		}
		//@于庆海   去掉了else
			rw.setIsBackTracking(this.IsBackTrack);

			//调用删除GenerWorkerList数据，不然会导致两个节点之间有垃圾数据，特别遇到中间有分合流时候。
			this.DeleteSpanNodesGenerWorkerListData();
		

		rw.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
		rw.Insert();


		// 为电建增加一个退回并原路返回的日志类型.
		if (IsBackTrack == true)
		{
			// 加入track.
			this.AddToTrack(ActionType.ReturnAndBackWay, gwl.getFK_Emp(), gwl.getFK_EmpText(), this.ReturnToNode.getNodeID(), this.ReturnToNode.getName(), Msg);
		}
		else
		{
			// 加入track.
			this.AddToTrack(ActionType.Return, gwl.getFK_Emp(), gwl.getFK_EmpText(), this.ReturnToNode.getNodeID(), this.ReturnToNode.getName(), Msg);
		}

		try
		{
			// 记录退回日志. this.HisNode, this.ReturnToNode
			ReorderLog(this.ReturnToNode, this.HisNode, rw);
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteWarning(ex.getMessage());
		}

		// 以退回到的节点向前数据用递归删除它。
		if (IsBackTrack == false)
		{
			//如果退回不需要原路返回，就删除中间点的数据。

///#warning 没有考虑两种流程数据存储模式。
			//DeleteToNodesData(this.ReturnToNode.HisToNodes);
		}

		//// 向他发送消息。 删除，该消息写入了事件里。
		//if (Glo.IsEnableSysMessage == true)
		//{
		//    //   WF.Port.WFEmp wfemp = new WFEmp(wnOfBackTo.HisWork.Rec);
		//    string title = string.Format("工作退回：流程:{0}.工作:{1},退回人:{2},需您处理",
		//        this.HisNode.FlowName, this.ReturnToNode.Name, WebUser.Name);
		//    BP.WF.Dev2Interface.Port_SendMsg(gwl.FK_Emp, title, Msg, "RE" + this.HisNode.NodeID + this.WorkID, BP.WF.SMSMsgType.ReturnWork, ReturnToNode.FK_Flow, ReturnToNode.NodeID, this.WorkID, this.FID);
		//}

		//把退回原因加入特殊变量里. 为软通小杨处理rpt变量不能替换的问题.
		String text = this.HisNode.getHisFlow().DoFlowEventEntity(EventListOfNode.ReturnAfter, this.HisNode, rpt, atPara, null, gwl.getFK_Emp());

		if (text != null && text.length() > 1000)
		{
			text = "退回事件:无返回信息.";
		}
		// 返回退回信息.
		if (this.ReturnToNode.getIsGuestNode())
		{
			GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
			return "工作已经被您退回到(" + this.ReturnToNode.getName() + "),退回给(" + gwf.getGuestNo() + "," + gwf.getGuestName() + ").\n\r" + text;
		}
		else
		{
			return "工作已经被您退回到(" + this.ReturnToNode.getName() + "),退回给(" + gwl.getFK_Emp() + "," + gwl.getFK_EmpText() + ").\n\r" + text;
		}
	}
	/** 
	 增加日志
	 
	 @param at 类型
	 @param toEmp 到人员
	 @param toEmpName 到人员名称
	 @param toNDid 到节点
	 @param toNDName 到节点名称
	 @param msg 消息
	 * @throws Exception 
	*/
	public final void AddToTrack(ActionType at, String toEmp, String toEmpName, int toNDid, String toNDName, String msg) throws Exception
	{
		Track t = new Track();
		t.setWorkID(this.WorkID);
		t.FK_Flow = this.HisNode.getFK_Flow();
		t.setFID(this.FID);
		t.setRDT(DataType.getCurrentDataTimess());
		t.setHisActionType(at);

		t.setNDFrom(this.HisNode.getNodeID());
		t.setNDFromT(this.HisNode.getName());

		t.setEmpFrom(WebUser.getNo());
		t.setEmpFromT(WebUser.getName());
		t.FK_Flow = this.HisNode.getFK_Flow();

		if (toNDid == 0)
		{
			toNDid = this.HisNode.getNodeID();
			toNDName = this.HisNode.getName();
		}


		t.setNDTo(toNDid);
		t.setNDToT(toNDName);

		t.setEmpTo(toEmp);
		t.setEmpToT(toEmpName);
		t.setMsg(msg);
		t.Insert();
	}
	private String infoLog = "";
	private void ReorderLog(Node fromND, Node toND, ReturnWork rw) throws Exception
	{
		String filePath = BP.Sys.SystemConfig.getPathOfDataUser() + "ReturnLog/" + this.HisNode.getFK_Flow() + "/";
		if ((new java.io.File(filePath)).isDirectory() == false)
		{
			(new java.io.File(filePath)).mkdirs();
		}

		String file = filePath + "\\" + rw.getMyPK();
		infoLog = "\r\n退回人:" + WebUser.getNo() + "," + WebUser.getName() + " \r\n退回节点:" + fromND.getName() + " \r\n退回到:" + toND.getName();
		infoLog += "\r\n退回时间:" + DataType.getCurrentDataTime();
		infoLog += "\r\n原因:" + rw.getBeiZhu();

		ReorderLog(fromND, toND);
		DataType.WriteFile(file + ".txt", infoLog);
		DataType.WriteFile(file + ".htm", infoLog.replace("\r\n", "<br>"));

		// this.HisWork.Delete();
	}
	private void ReorderLog(Node fromND, Node toND) throws Exception
	{
		//开始遍历到达的节点集合
		for (Node nd : fromND.getHisToNodes().ToJavaList())
		{
			Work wk = nd.getHisWork();
			wk.setOID(this.WorkID);
			if (wk.RetrieveFromDBSources() == 0)
			{
				wk.setFID(this.WorkID);
				if (wk.Retrieve(WorkAttr.FID, this.WorkID) == 0)
				{
					continue;
				}
			}

			if (nd.getIsFL())
			{
				// 如果是分流 
				GenerWorkerLists wls = new GenerWorkerLists();
				QueryObject qo = new QueryObject(wls);
				qo.AddWhere(GenerWorkerListAttr.FID, this.WorkID);
				qo.addAnd();

				String[] ndsStrs = nd.getHisToNDs().split("[@]", -1);
				String inStr = "";
				for (String s : ndsStrs)
				{
					if (s.equals("") || s == null)
					{
						continue;
					}
					inStr += "'" + s + "',";
				}
				inStr = inStr.substring(0, inStr.length() - 1);
				if (inStr.contains(",") == true)
				{
					qo.AddWhere(GenerWorkerListAttr.FK_Node, Integer.parseInt(inStr));
				}
				else
				{
					qo.AddWhereIn(GenerWorkerListAttr.FK_Node, "(" + inStr + ")");
				}

				qo.DoQuery();
				for (GenerWorkerList wl : wls.ToJavaList())
				{
					Node subNd = new Node(wl.getFK_Node());
					Work subWK = subNd.GetWork(wl.getWorkID());

					infoLog += "\r\n*****************************************************************************************";
					infoLog += "\r\n节点ID:" + subNd.getNodeID() + "  工作名称:" + subWK.getEnDesc();
					infoLog += "\r\n处理人:" + subWK.getRec() + " , " + wk.getRecOfEmp().getName();
					infoLog += "\r\n接收时间:" + subWK.getRDT() + " 处理时间:" + subWK.getCDT();
					infoLog += "\r\n ------------------------------------------------- ";

					for (Attr attr : wk.getEnMap().getAttrs())
					{
						if (attr.getUIVisible() == false)
						{
							continue;
						}
						infoLog += "\r\n " + attr.getDesc() + ":" + subWK.GetValStrByKey(attr.getKey());
					}

					//递归调用。 //递归调用。  先把此处注释掉   会造成死循环 杨玉慧
					//ReorderLog(subNd, toND);
				}
			}
			else
			{
				infoLog += "\r\n*****************************************************************************************";
				infoLog += "\r\n节点ID:" + wk.getNodeID() + "  工作名称:" + wk.getEnDesc();
				infoLog += "\r\n处理人:" + wk.getRec() + " , " + wk.getRecOfEmp().getName();
				infoLog += "\r\n接收时间:" + wk.getRDT() + " 处理时间:" + wk.getCDT();
				infoLog += "\r\n ------------------------------------------------- ";

				for (Attr attr : wk.getEnMap().getAttrs())
				{
					if (attr.getUIVisible() == false)
					{
						continue;
					}
					infoLog += "\r\n" + attr.getDesc() + " : " + wk.GetValStrByKey(attr.getKey());
				}
			}

			// 如果到了当前的节点 
			if (nd.getNodeID() == toND.getNodeID())
			{
				break;
			}

			//递归调用。  先把此处注释掉   会造成死循环 杨玉慧
			//ReorderLog(nd, toND);
		}
	}
	/** 
	 递归删除两个节点之间的数据
	 
	 @param nds 到达的节点集合
	 * @throws Exception 
	*/
	public final void DeleteToNodesData(Nodes nds) throws Exception
	{
		//开始遍历到达的节点集合
		for (Node nd : nds.ToJavaList())
		{
			Work wk = nd.getHisWork();
			wk.setOID(this.WorkID);
			if (wk.Delete() == 0)
			{
				wk.setFID(this.WorkID);
				if (wk.Delete(WorkAttr.FID, this.WorkID) == 0)
				{
					continue;
				}
			}


				///#region 删除当前节点数据，删除附件信息。
			// 删除明细表信息。
			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			for (MapDtl dtl : dtls.ToJavaList())
			{
				ps = new Paras();
				ps.SQL = "DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + dbStr + "WorkID";
				ps.Add("WorkID", (new Long(this.WorkID)).toString());
				BP.DA.DBAccess.RunSQL(ps);
			}

			// 删除表单附件信息。
			BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE RefPKVal=" + dbStr + "WorkID AND FK_MapData=" + dbStr + "FK_MapData ", "WorkID", (new Long(this.WorkID)).toString(), "FK_MapData", "ND" + nd.getNodeID());
			// 删除签名信息。
			BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmEleDB WHERE RefPKVal=" + dbStr + "WorkID AND FK_MapData=" + dbStr + "FK_MapData ", "WorkID", (new Long(this.WorkID)).toString(), "FK_MapData", "ND" + nd.getNodeID());

				///#endregion 删除当前节点数据。


			//说明:已经删除该节点数据。
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE (WorkID=" + dbStr + "WorkID1 OR FID=" + dbStr + "WorkID2 ) AND FK_Node=" + dbStr + "FK_Node", "WorkID1", this.WorkID, "WorkID2", this.WorkID, "FK_Node", nd.getNodeID());

			if (nd.getIsFL())
			{
				// 如果是分流 
				GenerWorkerLists wls = new GenerWorkerLists();
				QueryObject qo = new QueryObject(wls);
				qo.AddWhere(GenerWorkerListAttr.FID, this.WorkID);
				qo.addAnd();

				String[] ndsStrs = nd.getHisToNDs().split("[@]", -1);
				String inStr = "";
				for (String s : ndsStrs)
				{
					if (s.equals("") || s == null)
					{
						continue;
					}
					inStr += "'" + s + "',";
				}
				inStr = inStr.substring(0, inStr.length() - 1);
				if (inStr.contains(",") == true)
				{
					qo.AddWhere(GenerWorkerListAttr.FK_Node, Integer.parseInt(inStr));
				}
				else
				{
					qo.AddWhereIn(GenerWorkerListAttr.FK_Node, "(" + inStr + ")");
				}

				qo.DoQuery();
				for (GenerWorkerList wl : wls.ToJavaList())
				{
					Node subNd = new Node(wl.getFK_Node());
					Work subWK = subNd.GetWork(wl.getWorkID());
					subWK.Delete();

					//删除分流下步骤的节点信息.
					DeleteToNodesData(subNd.getHisToNodes());
				}

				DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FID=" + dbStr + "WorkID", "WorkID", this.WorkID);
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + dbStr + "WorkID", "WorkID", this.WorkID);
				DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + dbStr + "WorkID", "WorkID", this.WorkID);
			}
			DeleteToNodesData(nd.getHisToNodes());
		}
	}
	private WorkNode DoReturnSubFlow(int backtoNodeID, String msg, boolean isHiden) throws Exception
	{
		Node nd = new Node(backtoNodeID);
		ps = new Paras();
		ps.SQL = "DELETE  FROM WF_GenerWorkerList WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID  AND FID=" + dbStr + "FID";
		ps.Add("FK_Node", backtoNodeID);
		ps.Add("WorkID", this.HisWork.getOID());
		ps.Add("FID", this.HisWork.getFID());
		BP.DA.DBAccess.RunSQL(ps);

		// 找出分合流点处理的人员.
		ps = new Paras();
		ps.SQL = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "FID";
		ps.Add("FID", this.HisWork.getFID());
		ps.Add("FK_Node", backtoNodeID);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@ system error , this values must be =1");
		}

		String FK_Emp = dt.Rows.get(0).getValue(0).toString();
		// 获取当前工作的信息.
		GenerWorkerList wl = new GenerWorkerList(this.HisWork.getFID(), this.HisNode.getNodeID(), FK_Emp);
		Emp emp = new Emp(FK_Emp);

		// 改变部分属性让它适应新的数据,并显示一条新的待办工作让用户看到。
		wl.setIsPass(false);
		wl.setWorkID(this.HisWork.getOID());
		wl.setFID(this.HisWork.getFID());
		wl.setRDT(DataType.getCurrentDataTime());
		wl.setFK_Emp(FK_Emp);
		wl.setFK_EmpText(emp.getName());

		wl.setFK_Node(backtoNodeID);
		wl.setFK_NodeText(nd.getName());
		wl.setWarningHour(nd.getWarningHour());
		wl.setFK_Dept(emp.getFK_Dept());

		java.util.Date dtNew = new java.util.Date();
		dtNew = DateUtils.addDay(dtNew, (int)nd.getWarningHour());
		wl.setSDT(DateUtils.format(dtNew,DataType.getSysDataTimeFormat())); // DataType.getCurrentDataTime();
		wl.setFK_Flow(this.HisNode.getFK_Flow());
		wl.Insert();

		GenerWorkFlow gwf = new GenerWorkFlow(this.HisWork.getOID());
		gwf.setFK_Node(backtoNodeID);
		gwf.setNodeName(nd.getName());
		gwf.DirectUpdate();

		ps = new Paras();
		ps.Add("FK_Node", backtoNodeID);
		ps.Add("WorkID", this.HisWork.getOID());
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=3 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID";
		BP.DA.DBAccess.RunSQL(ps);

		// 如果是隐性退回。
		BP.WF.ReturnWork rw = new ReturnWork();
		rw.setWorkID(wl.getWorkID());
		rw.setReturnToNode(wl.getFK_Node());
		rw.setReturnNode(this.HisNode.getNodeID());
		rw.setReturnNodeName(this.HisNode.getName());
		rw.setReturnToEmp(FK_Emp);
		rw.setBeiZhu(msg);
		try
		{
			rw.setMyPK(rw.getReturnToNode() + "_" + rw.getWorkID() + "_" + DateUtils.format(new Date(), "yyyyMMddhhmmss"));
			rw.Insert();
		}
		catch (java.lang.Exception e)
		{
			rw.setMyPK(rw.getReturnToNode() + "_" + rw.getWorkID() + "_" + BP.DA.DBAccess.GenerOID());
			rw.Insert();
		}

		// 加入track.
		this.AddToTrack(ActionType.Return, FK_Emp, emp.getName(), backtoNodeID, nd.getName(), msg);

		WorkNode wn = new WorkNode(this.HisWork.getFID(), backtoNodeID);
		if (Glo.getIsEnableSysMessage())
		{
			//  WF.Port.WFEmp wfemp = new WFEmp(wn.HisWork.Rec);
			String title = String.format("工作退回：流程:%1$s.工作:%2$s,退回人:%3$s,需您处理", wn.getHisNode().getFlowName(), wn.getHisNode().getName(), WebUser.getName());

			BP.WF.Dev2Interface.Port_SendMsg(wn.getHisWork().getRec(), title, msg, "RESub" + backtoNodeID + "_" + this.WorkID, BP.WF.SMSMsgType.SendSuccess, nd.getFK_Flow(), nd.getNodeID(), this.WorkID, this.FID);
		}
		return wn;
	}
}