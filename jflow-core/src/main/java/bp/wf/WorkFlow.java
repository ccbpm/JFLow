package bp.wf;

import bp.web.*;
import bp.da.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.difference.*;

import java.math.*;

/** 
 WF 的摘要说明。
 工作流
 这里包含了两个方面
 工作的信息．
 流程的信息．
*/
public class WorkFlow
{

		///#region 当前工作统计信息
	/** 
	 正常范围的运行的个数。
	*/
	public static int NumOfRuning(String FK_Emp)
	{
		String sql = "SELECT COUNT(*) FROM V_WF_CURRWROKS WHERE FK_Emp='" + FK_Emp + "' AND WorkTimeState=0";
		return DBAccess.RunSQLReturnValInt(sql);
	}
	/** 
	 进入警告期限的个数
	*/
	public static int NumOfAlert(String FK_Emp)
	{
		String sql = "SELECT COUNT(*) FROM V_WF_CURRWROKS WHERE FK_Emp='" + FK_Emp + "' AND WorkTimeState=1";
		return DBAccess.RunSQLReturnValInt(sql);
	}
	/** 
	 逾期
	*/
	public static int NumOfTimeout(String FK_Emp)
	{
		String sql = "SELECT COUNT(*) FROM V_WF_CURRWROKS WHERE FK_Emp='" + FK_Emp + "' AND WorkTimeState=2";
		return DBAccess.RunSQLReturnValInt(sql);
	}

		///#endregion


		///#region  权限管理
	/** 
	 是不是能够作当前的工作。
	 
	 param empId 工作人员ID
	 @return 是不是能够作当前的工作
	*/
	public final boolean IsCanDoCurrentWork(String empId) throws Exception {
		WorkNode wn = this.GetCurrentWorkNode();
		return bp.wf.Dev2Interface.Flow_IsCanDoCurrentWork(wn.getWorkID(), empId);

			///#region 使用dev2InterFace 中的算法
		//return true;
		// 找到当前的工作节点

		// 判断是不是开始工作节点..
//		if (wn.getHisNode().isStartNode())
//		{
//			// 从物理上判断是不是有这个权限。
//			// return WorkFlow.IsCanDoWorkCheckByEmpStation(wn.HisNode.NodeID, empId);
//			return true;
//		}

		// 判断他的工作生成的工作者.
//		GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID(), wn.getHisNode().getNodeID());
//		if (gwls.size() == 0)
//		{
//			//return true;
//			//throw new Exception("@工作流程定义错误,没有找到能够执行此项工作的人员.相关信息:工作ID="+this.WorkID+",节点ID="+wn.HisNode.NodeID );
//			throw new RuntimeException("@工作流程定义错误,没有找到能够执行此项工作的人员.相关信息:WorkID=" + this.getWorkID() + ",NodeID=" + wn.getHisNode().getNodeID());
//		}
//
//		for (GenerWorkerList en : gwls.ToJavaList())
//		{
//			if (en.getFK_Emp().equals(empId))
//			{
//				return true;
//			}
//		}
//		return false;

			///#endregion
	}

		///#endregion


		///#region 流程公共方法
	/** 
	 执行驳回
	 应用场景:子流程向分合点驳回时
	 
	 param fid
	 param fk_node 被驳回的节点
	 param msg
	 @return 
	*/
	public final String DoHungupReject(long fid, int fk_node, String msg) throws Exception {
		GenerWorkerList wl = new GenerWorkerList();
		int i = wl.Retrieve(GenerWorkerListAttr.FID, fid, GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node, fk_node);

		//if (i == 0)
		//    throw new Exception("系统错误，没有找到应该找到的数据。");

		i = wl.Delete();
		//if (i == 0)
		//    throw new Exception("系统错误，没有删除应该删除的数据。");

		wl = new GenerWorkerList();
		i = wl.Retrieve(GenerWorkerListAttr.FID, fid, GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.IsPass, 3);

		//if (i == 0)
		//    throw new Exception("系统错误，想找到退回的原始起点没有找到。");

		Node nd = new Node(fk_node);
		// 更新当前流程管理表的设置当前的节点。
		DBAccess.RunSQL("UPDATE WF_GenerWorkFlow SET FK_Node=" + fk_node + ", NodeName='" + nd.getName() + "' WHERE WorkID=" + this.getWorkID());

		wl.setIsPass(false);
		wl.Update();

		return "工作已经驳回到(" + wl.getFK_Emp() + " , " + wl.getFK_EmpText() + ")";
		// wl.HisNode
	}
	/** 
	 逻辑删除流程
	 
	 param msg 逻辑删除流程原因，可以为空。
	*/
	public final void DoDeleteWorkFlowByFlag(String msg) throws Exception
	{
		try
		{
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

			bp.wf.Node nd = new Node(gwf.getFK_Node());
			Work wk = nd.getHisWork();
			wk.setOID(this.getWorkID());
			wk.RetrieveFromDBSources();

			//定义workNode.
			WorkNode wn = new WorkNode(wk, nd);

			//调用结束前事件.
			ExecEvent.DoFlow(EventListFlow.BeforeFlowDel, wn, null);

			//记录日志 感谢 itdos and 888 , 提出了这个问题..
			wn.AddToTrack(ActionType.DeleteFlowByFlag, WebUser.getNo(), WebUser.getName() , wn.getHisNode().getNodeID(), wn.getHisNode().getName(), msg);

			//更新-流程数据表的状态. 
			String sql = "UPDATE  " + this.getHisFlow().getPTable() + " SET WFState=" + WFState.Delete.getValue() + " WHERE OID=" + this.getWorkID();
			DBAccess.RunSQL(sql);

			//删除他的工作者，不让其有待办.
			sql = "DELETE FROM WF_GenerWorkerList WHERE WorkID=" + this.getWorkID();
			DBAccess.RunSQL(sql);

			//设置产生的工作流程为.
			gwf.setWFState(bp.wf.WFState.Delete);
			gwf.Update();


			//调用结束后事件.
			ExecEvent.DoFlow(EventListFlow.AfterFlowDel, wn, null);

		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError("@逻辑删除出现错误:" + ex.getMessage());
			throw new RuntimeException("@逻辑删除出现错误:" + ex.getMessage());
		}
	}
	/** 
	 恢复逻辑删除流程
	 
	 param msg 回复原因,可以为空.
	*/
	public final void DoUnDeleteWorkFlowByFlag(String msg) throws Exception
	{
		try
		{
			DBAccess.RunSQL("UPDATE WF_GenerWorkFlow SET WFState=" + WFState.Runing.getValue() + " WHERE  WorkID=" + this.getWorkID());

			//设置产生的工作流程为.
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

			//回复数据.
			bp.wf.Dev2Interface.Flow_DoRebackWorkFlow(gwf.getFK_Flow(), gwf.getWorkID(), gwf.getFK_Node(), msg);


			WorkNode wn = new WorkNode(getWorkID(), gwf.getFK_Node());
			wn.AddToTrack(ActionType.UnDeleteFlowByFlag, WebUser.getNo(), WebUser.getName() , wn.getHisNode().getNodeID(), wn.getHisNode().getName(), msg);
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError("@逻辑删除出现错误:" + ex.getMessage());
			throw new RuntimeException("@逻辑删除出现错误:" + ex.getMessage());
		}
	}
	/** 
	 删除已经完成的流程
	 
	 param flowNo 流程编号
	 param workID 工作ID
	 param isDelSubFlow 是否要删除子流程
	 param note 删除原因
	 @return 删除信息
	*/
	public static Object DoDeleteWorkFlowAlreadyComplete(String flowNo, long workID, boolean isDelSubFlow, String note) throws Exception {
		Log.DebugWriteInfo("开始删除流程:流程编号:" + flowNo + "-WorkID:" + workID + "-" + ". 是否要删除子流程:" + isDelSubFlow + ";删除原因:" + note);

		Flow fl = new Flow(flowNo);


			///#region 记录流程删除日志
		GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt");
		rpt.SetValByKey(GERptAttr.OID, workID);
		rpt.Retrieve();
		WorkFlowDeleteLog log = new WorkFlowDeleteLog();
		log.setOID(workID);
		try
		{
			log.Copy(rpt);
			log.setDeleteDT(DataType.getCurrentDateTime());
			log.setOperDept(WebUser.getFK_Dept());
			log.setOperDeptName(WebUser.getFK_DeptName());
			log.setOper(WebUser.getNo());
			log.setDeleteNote(note);
			log.setOID(workID);
			log.setFK_Flow(flowNo);
			log.setFK_FlowSort(fl.getFK_FlowSort());
			log.InsertAsOID(log.getOID());
		}
		catch (RuntimeException ex)
		{
			log.CheckPhysicsTable();
			log.Delete();
			return ex.getStackTrace();
		}

			///#endregion 记录流程删除日志

		DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + workID);
		DBAccess.RunSQL("DELETE FROM " + fl.getPTable() + " WHERE OID=" + workID);
		DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE  WorkID=" + workID); // 删除质量考核数据。

		String info = "";


			///#region 正常的删除信息.
		String msg = "";
		try
		{
			// 删除单据信息.
			DBAccess.RunSQL("DELETE FROM WF_CCList WHERE WorkID=" + workID);

			// 删除退回.
			DBAccess.RunSQL("DELETE FROM WF_ReturnWork WHERE WorkID=" + workID);
			// 删除移交.
			// DBAccess.RunSQL("DELETE FROM WF_ForwardWork WHERE WorkID=" + workID);

			//删除它的工作.
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE (WorkID=" + workID + " OR FID=" + workID + " ) AND FK_Flow='" + flowNo + "'");
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE (WorkID=" + workID + " OR FID=" + workID + " ) AND FK_Flow='" + flowNo + "'");

			//删除所有节点上的数据.
			Nodes nds = fl.getHisNodes();
			for (Node nd : nds.ToJavaList())
			{
				try
				{
					DBAccess.RunSQL("DELETE FROM ND" + nd.getNodeID() + " WHERE OID=" + workID + " OR FID=" + workID);
				}
				catch (RuntimeException ex)
				{
					msg += "@ delete data error " + ex.getMessage();
				}
			}
			if (!msg.equals(""))
			{
				Log.DebugWriteInfo(msg);
			}
		}
		catch (RuntimeException ex)
		{
			String err = "@删除工作流程 Err " + ex.getStackTrace();
			Log.DebugWriteError(err);
			throw new RuntimeException(err);
		}
		info = "@删除流程删除成功";

			///#endregion 正常的删除信息.


			///#region 删除该流程下面的子流程.
		if (isDelSubFlow)
		{
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, workID, null);
			for (GenerWorkFlow item : gwfs.ToJavaList())
			{
				bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(item.getWorkID(), true);
			}
		}

			///#endregion 删除该流程下面的子流程.

		Log.DebugWriteInfo("@[" + fl.getName() + "]流程被[" + WebUser.getNo() + WebUser.getName() + "]删除，WorkID[" + workID + "]。");
		return "已经完成的流程被您删除成功.";
	}
	/** 
	 执行驳回
	 应用场景:子流程向分合点驳回时
	 
	 param fid
	 param fk_node 被驳回的节点
	 param msg
	 @return 
	*/
	public final String DoReject(long fid, int fk_node, String msg) throws Exception {
		GenerWorkerList wl = new GenerWorkerList();
		int i = wl.Retrieve(GenerWorkerListAttr.FID, fid, GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node, fk_node);

		//if (i == 0)
		//    throw new Exception("系统错误，没有找到应该找到的数据。");

		i = wl.Delete();
		//if (i == 0)
		//    throw new Exception("系统错误，没有删除应该删除的数据。");

		wl = new GenerWorkerList();
		i = wl.Retrieve(GenerWorkerListAttr.FID, fid, GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.IsPass, 3);

		//if (i == 0)
		//    throw new Exception("系统错误，想找到退回的原始起点没有找到。");

		Node nd = new Node(fk_node);
		// 更新当前流程管理表的设置当前的节点。
		DBAccess.RunSQL("UPDATE WF_GenerWorkFlow SET FK_Node=" + fk_node + ", NodeName='" + nd.getName() + "' WHERE WorkID=" + this.getWorkID());

		wl.setIsPass(false);
		wl.Update();

		return "工作已经驳回到(" + wl.getFK_Emp() + " , " + wl.getFK_EmpText() + ")";
		// wl.HisNode
	}
	/** 
	 删除子线程
	 
	 @return 返回删除结果.
	*/
	private String DoDeleteSubThread() throws Exception {
		WorkNode wn = this.GetCurrentWorkNode();
		Emp empOfWorker = new Emp(WebUser.getNo());


			///#region 正常的删除信息.
		String msg = "";
		try
		{
			long workId = this.getWorkID();
			String flowNo = this.getHisFlow().getNo();
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("获取流程的 ID 与流程编号 出现错误。" + ex.getMessage());
		}

		try
		{
			// 删除质量考核信息.
			DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE WorkID=" + this.getWorkID()); // 删除质量考核数据。

			// 删除抄送信息.
			DBAccess.RunSQL("DELETE FROM WF_CCList WHERE WorkID=" + this.getWorkID());

			// 删除退回.
			DBAccess.RunSQL("DELETE FROM WF_ReturnWork WHERE WorkID=" + this.getWorkID());
			// 删除移交.
			// DBAccess.RunSQL("DELETE FROM WF_ForwardWork WHERE WorkID=" + this.WorkID);

			//删除它的工作.
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE (WorkID=" + this.getWorkID() + " ) AND FK_Flow='" + this.getHisFlow().getNo() + "'");
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE (WorkID=" + this.getWorkID() + " ) AND FK_Flow='" + this.getHisFlow().getNo() + "'");

			if (!msg.equals(""))
			{
				Log.DebugWriteInfo(msg);
			}
		}
		catch (RuntimeException ex)
		{
			String err = "@删除工作流程[" + this.getHisGenerWorkFlow().getWorkID() + "," + this.getHisGenerWorkFlow().getTitle() + "] Err " + ex.getMessage();
			Log.DebugWriteError(err);
			throw new RuntimeException(err);
		}
		String info = "@删除流程删除成功";

			///#endregion 正常的删除信息.


			///#region 处理分流程删除的问题完成率的问题。
		if (1 == 2)
		{
			/* 目前还没有必要，因为在分流点,才有计算完成率的需求. */
			String sql = "";
			/* 
			 * 取出来获取停留点,没有获取到说明没有任何子线程到达合流点的位置.
			 */
			sql = "SELECT FK_Node FROM WF_GenerWorkerList WHERE WorkID=" + this.getFID() + " AND IsPass=3";
			int fk_node = DBAccess.RunSQLReturnValInt(sql, 0);
			if (fk_node != 0)
			{
				/* 说明它是待命的状态 */
				Node nextNode = new Node(fk_node);
				if (nextNode.getPassRate().compareTo(BigDecimal.valueOf(0)) > 0)
				{
					/* 找到等待处理节点的上一个点 */
					Nodes priNodes = nextNode.getFromNodes();
					if (priNodes.size() != 1)
					{
						throw new RuntimeException("@没有实现子流程不同线程的需求。");
					}

					Node priNode = (Node)priNodes.get(0);


						///#region 处理完成率
					sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + priNode.getNodeID() + " AND FID=" + this.getFID() + " AND IsPass=1";
					BigDecimal ok = new BigDecimal(DBAccess.RunSQLReturnValInt(sql));
					sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + priNode.getNodeID() + " AND FID=" + this.getFID();
					BigDecimal all = new BigDecimal(DBAccess.RunSQLReturnValInt(sql));
					if (all.compareTo(new BigDecimal(0)) == 0)
					{
						/*说明:所有的子线程都被杀掉了, 就应该整个流程结束。*/
						WorkFlow wf = new WorkFlow(this.getHisFlow(), this.getFID());
						info += "@所有的子线程已经结束。";
						info += "@结束主流程信息。";
						info += "@" + wf.DoFlowOver(ActionType.FlowOver, "合流点流程结束", null, null);
					}

					BigDecimal passRate = ok.divide(all.multiply(BigDecimal.valueOf(100)));
					if (nextNode.getPassRate().compareTo(passRate) <= 0)
					{
						/*说明全部的人员都完成了，就让合流点显示它。*/
						DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0  WHERE IsPass=3  AND WorkID=" + this.getFID() + " AND FK_Node=" + fk_node);
					}

						///#endregion 处理完成率
				}
			} // 结束有待命的状态判断。

			if (fk_node == 0)
			{
				/* 说明:没有找到等待启动工作的合流节点. */
				GenerWorkFlow gwf = new GenerWorkFlow(this.getFID());
				Node fND = new Node(gwf.getFK_Node());
				switch (fND.getHisNodeWorkType())
				{
					case WorkHL: //主流程运行到合流点上了
						break;
					default:
						/*** 解决删除最后一个子流程时要把干流程也要删除。*/
						//sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" +this.HisGenerWorkFlow +" AND FID=" + this.FID;
						//int num = DBAccess.RunSQLReturnValInt(sql);
						//if (num == 0)
						//{
						//    /*说明没有子进程，就要把这个流程执行完成。*/
						//    WorkFlow wf = new WorkFlow(this.HisFlow, this.FID);
						//    info += "@所有的子线程已经结束。";
						//    info += "@结束主流程信息。";
						//    info += "@" + wf.DoFlowOver(ActionType.FlowOver, "主流程结束");
						//}
						break;
				}
			}
		}

			///#endregion


			///#region 写入删除日志.
		wn.AddToTrack(ActionType.DeleteSubThread, empOfWorker.getUserID(), empOfWorker.getName(), wn.getHisNode().getNodeID(), wn.getHisNode().getName(), "子线程被:" + WebUser.getName() + "删除.");

			///#endregion 写入删除日志.

		return "子线程被删除成功.";
	}
	/** 
	 删除已经完成的流程
	 
	 param workid 工作ID
	 param isDelSubFlow 是否删除子流程
	 @return 删除错误会抛出异常
	*/
	public static void DeleteFlowByReal(long workid, boolean isDelSubFlow) throws Exception {
		//检查流程是否完成，如果没有完成就调用workflow流程删除.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		int i = gwf.RetrieveFromDBSources();
		if (i == 0)
		{
			throw new RuntimeException("err@错误：该流程应不存在");
		}

		bp.wf.Flow fl = new Flow(gwf.getFK_Flow());
		String toEmps = gwf.getEmps().replace('@', ','); //流程的所有处理人
		if (i != 0)
		{
			if (gwf.getWFState() != WFState.Complete)
			{
				WorkFlow wf = new WorkFlow(workid);
				//发送退回消息 
				PushMsgs pms1 = new PushMsgs();
				pms1.Retrieve(PushMsgAttr.FK_Node, gwf.getFK_Node(), PushMsgAttr.FK_Event, EventListFlow.AfterFlowDel, null);
				Node node = new Node(gwf.getFK_Node());
				for (PushMsg pm : pms1.ToJavaList())
				{
					Work work = node.getHisWork();
					work.setOID(gwf.getWorkID());
					work.setNodeID(node.getNodeID());
					work.SetValByKey("FK_Dept", WebUser.getFK_Dept());
					pm.DoSendMessage(node, work, null, null, null, toEmps);
				}

				wf.DoDeleteWorkFlowByReal(isDelSubFlow);
				return;
			}
		}


			///#region 删除独立表单的数据.
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, gwf.getFK_Flow(), null);
		String strs = "";
		for (FrmNode frmNode : fns.ToJavaList())
		{
			if (strs.contains("@" + frmNode.getFKFrm()) == true)
			{
				continue;
			}

			strs += "@" + frmNode.getFKFrm() + "@";
			try
			{
				MapData md = new MapData(frmNode.getFKFrm());
				DBAccess.RunSQL("DELETE FROM " + md.getPTable() + " WHERE OID=" + workid);
			}
			catch (java.lang.Exception e)
			{

			}
		}

			///#endregion 删除独立表单的数据.

		//删除流程数据.
		DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + workid);
		DBAccess.RunSQL("DELETE FROM " + fl.getPTable() + " WHERE OID=" + workid);
		DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE  WorkID=" + workid); // 删除质量考核数据。


			///#region 正常的删除信息.
		Log.DebugWriteInfo("@[" + fl.getName() + "]流程被[" + WebUser.getNo() + WebUser.getName() + "]删除，WorkID[" + workid + "]。");
		String msg = "";

		// 删除单据信息.
		DBAccess.RunSQL("DELETE FROM WF_CCList WHERE WorkID=" + workid);
		// 删除退回.
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork WHERE WorkID=" + workid);

		//发送退回消息 
		PushMsgs pms = new PushMsgs();
		pms.Retrieve(PushMsgAttr.FK_Node, gwf.getFK_Node(), PushMsgAttr.FK_Event, EventListFlow.AfterFlowDel, null);
		Node pnd = new Node(gwf.getFK_Node());
		for (PushMsg pm : pms.ToJavaList())
		{
			Work work = pnd.getHisWork();
			work.setOID(gwf.getWorkID());
			work.setNodeID(pnd.getNodeID());
			work.SetValByKey("FK_Dept", WebUser.getFK_Dept());

			pm.DoSendMessage(pnd, work, null, null, null, toEmps);
		}

		//删除它的工作.
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE (WorkID=" + workid + " OR FID=" + workid + " ) AND FK_Flow='" + gwf.getFK_Flow() + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE (WorkID=" + workid + " OR FID=" + workid + " ) AND FK_Flow='" + gwf.getFK_Flow() + "'");

		//删除所有节点上的数据.
		Nodes nodes = new Nodes(gwf.getFK_Flow()); // this.HisFlow.HisNodes;
		for (Node node : nodes.ToJavaList())
		{
			try
			{
				if (DBAccess.IsExitsObject("ND" + node.getNodeID()) == false)
				{
					continue;
				}

				DBAccess.RunSQL("DELETE FROM ND" + node.getNodeID() + " WHERE OID=" + workid + " OR FID=" + workid);
			}
			catch (RuntimeException ex)
			{
				msg += "@ delete data error " + ex.getMessage();
			}

			MapDtls dtls = new MapDtls("ND" + node.getNodeID());
			for (MapDtl dtl : dtls.ToJavaList())
			{
				try
				{
					DBAccess.RunSQL("DELETE FROM " + dtl.getPTable());
				}
				catch (java.lang.Exception e2)
				{
				}
			}
		}

		MapDtls mydtls = new MapDtls("ND" + Integer.parseInt(gwf.getFK_Flow()) + "Rpt");
		for (MapDtl dtl : mydtls.ToJavaList())
		{
			try
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable());
			}
			catch (java.lang.Exception e3)
			{
			}
		}

		if (!msg.equals(""))
		{
			Log.DebugWriteInfo(msg);
		}


			///#endregion 正常的删除信息.
	}
	/** 
	 删除子线程
	 
	 @return 删除的消息
	*/
	public final String DoDeleteSubThread2015() throws Exception {
		if (this.getFID() == 0)
		{
			throw new RuntimeException("@该流程非子线程流程实例，不能执行该方法。");
		}


			///#region 正常的删除信息.
		String msg = "";
		try
		{
			long workId = this.getWorkID();
			String flowNo = this.getHisFlow().getNo();
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("获取流程的 ID 与流程编号 出现错误。" + ex.getMessage());
		}

		try
		{
			// 删除质量考核信息.
			DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE WorkID=" + this.getWorkID()); // 删除质量考核数据。

			// 删除抄送信息.
			DBAccess.RunSQL("DELETE FROM WF_CCList WHERE WorkID=" + this.getWorkID());

			// 删除退回.
			DBAccess.RunSQL("DELETE FROM WF_ReturnWork WHERE WorkID=" + this.getWorkID());
			// 删除移交.
			// DBAccess.RunSQL("DELETE FROM WF_ForwardWork WHERE WorkID=" + this.WorkID);

			//删除它的工作.
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID());
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE WorkID=" + this.getWorkID());

			if (!msg.equals(""))
			{
				Log.DebugWriteInfo(msg);
			}
		}
		catch (RuntimeException ex)
		{
			String err = "@删除工作流程[" + this.getHisGenerWorkFlow().getWorkID() + "," + this.getHisGenerWorkFlow().getTitle() + "] Err " + ex.getMessage();
			Log.DebugWriteError(err);
			throw new RuntimeException(err);
		}
		String info = "@删除流程删除成功";

			///#endregion 正常的删除信息.


			///#region 处理分流程删除的问题完成率的问题。
		if (1 == 2)
		{
			/*
			 * 开发说明：
			 * 1，当前是删除子线程操作,当前的节点就是子线程节点.
			 * 2, 删除子线程的动作，1，合流点。2，分流点。
			 * 3，这里要解决合流节点的完成率的问题.
			 */


///#warning 应该删除一个子线程后，就需要计算完成率的问题。但是目前应用到该场景极少,因为。能够看到河流点信息，说明已经到达了完成率了。

			/* 目前还没有必要，因为在分流点,才有计算完成率的需求. */
			String sql = "";
			/* 
			 * 取出来获取停留点,没有获取到说明没有任何子线程到达合流点的位置.
			 */

			sql = "SELECT FK_Node FROM WF_GenerWorkerList WHERE WorkID=" + this.getFID() + " AND IsPass=3";
			int fk_node = DBAccess.RunSQLReturnValInt(sql, 0);
			if (fk_node != 0)
			{
				/* 说明它是待命的状态 */
				Node nextNode = new Node(fk_node);
				if (nextNode.getPassRate().compareTo(BigDecimal.valueOf(0)) > 0)
				{
					/* 找到等待处理节点的上一个点 */
					Nodes priNodes = nextNode.getFromNodes();
					if (priNodes.size() != 1)
					{
						throw new RuntimeException("@没有实现子流程不同线程的需求。");
					}

					Node priNode = (Node)priNodes.get(0);


						///#region 处理完成率
					sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + priNode.getNodeID() + " AND FID=" + this.getFID() + " AND IsPass=1";
					BigDecimal ok = new BigDecimal(DBAccess.RunSQLReturnValInt(sql));
					sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + priNode.getNodeID() + " AND FID=" + this.getFID();
					BigDecimal all = new BigDecimal(DBAccess.RunSQLReturnValInt(sql));
					if (all.compareTo(BigDecimal.valueOf(0)) == 0)
					{
						/*说明:所有的子线程都被杀掉了, 就应该整个流程结束。*/
						WorkFlow wf = new WorkFlow(this.getHisFlow(), this.getFID());
						info += "@所有的子线程已经结束。";
						info += "@结束主流程信息。";
						info += "@" + wf.DoFlowOver(ActionType.FlowOver, "合流点流程结束", null, null);
					}

					BigDecimal passRate = ok.divide(all.multiply(BigDecimal.valueOf(100)));
					if (nextNode.getPassRate().compareTo(passRate) <= 0)
					{
						/* 说明: 全部的人员都完成了，就让合流点显示它。*/
						DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0  WHERE IsPass=3  AND WorkID=" + this.getFID() + " AND FK_Node=" + fk_node);
					}

						///#endregion 处理完成率
				}
			} // 结束有待命的状态判断。

			if (fk_node == 0)
			{
				/* 说明:没有找到等待启动工作的合流节点. */
				GenerWorkFlow gwf = new GenerWorkFlow(this.getFID());
				Node fND = new Node(gwf.getFK_Node());
				switch (fND.getHisNodeWorkType())
				{
					case WorkHL: //主流程运行到合流点上了
						break;
					default:
						/*** 解决删除最后一个子流程时要把干流程也要删除。*/
						//sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" +this.HisGenerWorkFlow +" AND FID=" + this.FID;
						//int num = DBAccess.RunSQLReturnValInt(sql);
						//if (num == 0)
						//{
						//    /*说明没有子进程，就要把这个流程执行完成。*/
						//    WorkFlow wf = new WorkFlow(this.HisFlow, this.FID);
						//    info += "@所有的子线程已经结束。";
						//    info += "@结束主流程信息。";
						//    info += "@" + wf.DoFlowOver(ActionType.FlowOver, "主流程结束");
						//}
						break;
				}
			}
		}

			///#endregion



		//检查是否是最后一个子线程被删除了？如果是，就需要当分流节点产生待办.
		GenerWorkFlow gwfMain = new GenerWorkFlow(this.getFID());

		/*说明仅仅停留在分流节点,还没有到合流节点上去.
		 * 删除子线程的时候，判断是否是最后一个子线程,如果是，就要把他设置为待办状态。
		 * 1.首先要找到.
		 * 2.xxxx.
		 */
		//  string sql = "SELECT COUNT(*) FROM WF_GenerWorkerList WHERE FK_Node=";
		String mysql = "SELECT COUNT(*)  as Num FROM WF_GenerWorkerList WHERE IsPass=0 AND FID=" + this.getFID();
		int num = DBAccess.RunSQLReturnValInt(mysql);
		if (num == 0)
		{
			/* 说明当前主流程上是分流节点，但是已经没有子线程的待办了。
			 * 就是说，删除子流程的时候，删除到最后已经没有活动或者已经完成的子线程了.
			 * */

			GenerWorkerList gwl = new GenerWorkerList();
			int i = gwl.Retrieve(GenerWorkerListAttr.FK_Node, gwfMain.getFK_Node(), GenerWorkerListAttr.WorkID, gwfMain.getWorkID(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
			if (i == 0)
			{
				Node ndMain = new Node(gwfMain.getFK_Node());
				if (ndMain.isHL() == true)
				{
					/* 有可能是当前节点已经到了合流节点上去了, 要判断合流节点是否有代办？如果没有代办，就撤销到分流节点上去.
					 * 
					 * 就要检查他是否有代办.
					 */
					mysql = "SELECT COUNT(*)  as Num FROM WF_GenerWorkerList WHERE IsPass=0 AND FK_Node=" + gwfMain.getFK_Node();
					num = DBAccess.RunSQLReturnValInt(mysql);
					if (num == 0)
					{
						/*如果没有待办，就说明，当前节点已经运行到合流节点，但是不符合合流节点的完成率，导致合流节点上的人员看不到待办. 
						 * 这种情况，就需要让当前分流节点产生待办.
						 */

						mysql = "SELECT FK_Node FROM WF_GenerWorkerList WHERE FID=0 AND WorkID=" + gwfMain.getWorkID() + " ORDER BY RDT DESC ";
						int fenLiuNodeID = DBAccess.RunSQLReturnValInt(mysql);

						Node nd = new Node(fenLiuNodeID);
						if (nd.isFL() == false)
						{
							throw new RuntimeException("@程序错误，没有找到最近的一个分流节点.");
						}

						GenerWorkerLists gwls = new GenerWorkerLists();
						gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node, fenLiuNodeID, null);
						for (GenerWorkerList item : gwls.ToJavaList())
						{
							item.setRead(false);
							item.setIsPassInt(0);
							item.setSDT(DataType.getCurrentDateTimess());
							item.Update();
						}
					}
				}
			}
			else
			{
				gwl.setRead(false);
				gwl.setIsPassInt(0);
				gwl.setSDT(DataType.getCurrentDateTimess());
				gwl.Update();
				return "子线程被删除成功,这是最后一个删除的子线程已经为您在{" + gwfMain.getNodeName() + "}产生了待办,<a href='/WF/MyFlow.htm?WorkID=" + gwfMain.getWorkID() + "&FK_Flow=" + gwfMain.getFK_Flow() + "'>点击处理工作</a>.";

			}
		}
		return "子线程被删除成功.";
	}

	/** 
	 彻底的删除流程
	 
	 param isDelSubFlow 是否要删除子流程
	 @return 删除的消息
	*/
	public final String DoDeleteWorkFlowByReal(boolean isDelSubFlow)throws Exception
	{
		if (this.getFID() != 0)
		{
			return DoDeleteSubThread2015();
		}

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		if (gwf.RetrieveFromDBSources() == 0)
		{
			return "删除成功.";
		}

		String info = "";
		WorkNode wn = this.GetCurrentWorkNode();

		// 处理删除前事件。
		ExecEvent.DoFlow(EventListFlow.BeforeFlowDel, wn, null);


			///#region 删除独立表单的数据.
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getHisFlow().getNo(), null);
		String strs = "";
		for (FrmNode nd : fns.ToJavaList())
		{
			if (strs.contains("@" + nd.getFKFrm()) == true)
			{
				continue;
			}

			strs += "@" + nd.getFKFrm() + "@";
			try
			{
				MapData md = new MapData(nd.getFKFrm());
				DBAccess.RunSQL("DELETE FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID());
			}
			catch (java.lang.Exception e)
			{
			}
		}

			///#endregion 删除独立表单的数据.

		//删除流程数据.
		DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(this.getHisFlow().getNo()) + "Track WHERE WorkID=" + this.getWorkID());
		DBAccess.RunSQL("DELETE FROM " + this.getHisFlow().getPTable() + " WHERE OID=" + this.getWorkID());
		DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE  WorkID=" + this.getWorkID()); // 删除质量考核数据。


			///#region 正常的删除信息.
		Log.DebugWriteInfo("@[" + this.getHisFlow().getName() + "]流程被[" + WebUser.getNo() + WebUser.getName() + "]删除，WorkID[" + this.getWorkID() + "]。");
		String msg = "";
		try
		{
			long workId = this.getWorkID();
			String flowNo = this.getHisFlow().getNo();
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("获取流程的 ID 与流程编号 出现错误。" + ex.getMessage());
		}

		try
		{
			// 删除单据信息.
			DBAccess.RunSQL("DELETE FROM WF_CCList WHERE WorkID=" + this.getWorkID());
			// 删除退回.
			DBAccess.RunSQL("DELETE FROM WF_ReturnWork WHERE WorkID=" + this.getWorkID());

			//删除它的工作.
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE (WorkID=" + this.getWorkID() + " OR FID=" + this.getWorkID() + " ) AND FK_Flow='" + this.getHisFlow().getNo() + "'");
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE (WorkID=" + this.getWorkID() + " OR FID=" + this.getWorkID() + " ) AND FK_Flow='" + this.getHisFlow().getNo() + "'");

			//删除所有节点上的数据.
			Nodes nds = this.getHisFlow().getHisNodes();
			for (Node nd : nds.ToJavaList())
			{
				MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
				for (MapDtl dtl : dtls.ToJavaList())
				{
					try
					{
						DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPk = " + this.getWorkID());
					}
					catch (java.lang.Exception e2)
					{
					}
				}
				try
				{
					if (DBAccess.IsExitsObject("ND" + nd.getNodeID()) == false)
					{
						continue;
					}

					DBAccess.RunSQL("DELETE FROM ND" + nd.getNodeID() + " WHERE OID=" + this.getWorkID() + " OR FID=" + this.getWorkID());
				}
				catch (RuntimeException ex)
				{
					msg += "@ delete data error " + ex.getMessage();
				}


			}
			if (!msg.equals(""))
			{
				Log.DebugWriteInfo(msg);
			}
		}
		catch (RuntimeException ex)
		{
			String err = "@删除工作流程[" + this.getHisGenerWorkFlow().getWorkID() + "," + this.getHisGenerWorkFlow().getTitle() + "] Err " + ex.getMessage();
			Log.DebugWriteError(err);
			throw new RuntimeException(err);
		}
		info = "@删除流程删除成功";

			///#endregion 正常的删除信息.


			///#region 处理分流程删除的问题完成率的问题。
		if (this.getFID() != 0)
		{
			String sql = "";
			/* 
			 * 取出来获取停留点,没有获取到说明没有任何子线程到达合流点的位置.
			 */
			sql = "SELECT FK_Node FROM WF_GenerWorkerList WHERE WorkID=" + wn.getHisWork().getFID() + " AND IsPass=3";
			int fk_node = DBAccess.RunSQLReturnValInt(sql, 0);
			if (fk_node != 0)
			{
				/* 说明它是待命的状态 */
				Node nextNode = new Node(fk_node);
				if (nextNode.getPassRate().compareTo(BigDecimal.valueOf(0)) > 0)
				{
					/* 找到等待处理节点的上一个点 */
					Nodes priNodes = nextNode.getFromNodes();
					if (priNodes.size() != 1)
					{
						throw new RuntimeException("@没有实现子流程不同线程的需求。");
					}

					Node priNode = (Node)priNodes.get(0);


						///#region 处理完成率
					sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + priNode.getNodeID() + " AND FID=" + wn.getHisWork().getFID() + " AND IsPass=1";
					BigDecimal ok = new BigDecimal(DBAccess.RunSQLReturnValInt(sql));
					sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + priNode.getNodeID() + " AND FID=" + wn.getHisWork().getFID();
					BigDecimal all = new BigDecimal(DBAccess.RunSQLReturnValInt(sql));
					if (all.compareTo(BigDecimal.valueOf(0)) == 0)
					{
						/*说明:所有的子线程都被杀掉了, 就应该整个流程结束。*/
						WorkFlow wf = new WorkFlow(this.getHisFlow(), this.getFID());
						info += "@所有的子线程已经结束。";
						info += "@结束主流程信息。";
						info += "@" + wf.DoFlowOver(ActionType.FlowOver, "合流点流程结束", null, null);
					}

					BigDecimal passRate = ok.divide(all.multiply(BigDecimal.valueOf(100)));
					if (nextNode.getPassRate().compareTo(passRate) <= 0)
					{
						/*说明全部的人员都完成了，就让合流点显示它。*/
						DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0  WHERE IsPass=3  AND WorkID=" + wn.getHisWork().getFID() + " AND FK_Node=" + fk_node);
					}

						///#endregion 处理完成率
				}
			} // 结束有待命的状态判断。

			if (fk_node == 0)
			{
				/* 说明:没有找到等待启动工作的合流节点. */
				gwf = new GenerWorkFlow(this.getFID());
				Node fND = new Node(gwf.getFK_Node());
				switch (fND.getHisNodeWorkType())
				{
					case WorkHL: //主流程运行到合流点上了
						break;
					default:
						/* 解决删除最后一个子流程时要把干流程也要删除。*/
						sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerList WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND FID=" + wn.getHisWork().getFID();
						int num = DBAccess.RunSQLReturnValInt(sql);
						if (num == 0)
						{
							/*说明没有子进程，就要把这个流程执行完成。*/
							WorkFlow wf = new WorkFlow(this.getHisFlow(), this.getFID());
							info += "@所有的子线程已经结束。";
							info += "@结束主流程信息。";
							info += "@" + wf.DoFlowOver(ActionType.FlowOver, "主流程结束", null, null);
						}
						break;
				}
			}
		}

			///#endregion


			///#region 删除该流程下面的子流程.
		if (isDelSubFlow)
		{
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID(), null);

			for (GenerWorkFlow item : gwfs.ToJavaList())
			{
				bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(item.getWorkID(), true);
			}
		}

			///#endregion 删除该流程下面的子流程.

		// 处理删除hou事件。
		ExecEvent.DoFlow(EventListFlow.AfterFlowDel, wn, null);

		return info;
	}

	/** 
	 删除工作流程记录日志，并保留运动轨迹.
	 
	 param isDelSubFlow 是否要删除子流程
	 @return 
	*/
	public final String DoDeleteWorkFlowByWriteLog(String info, boolean isDelSubFlow) throws Exception {
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.getHisFlow().getNo()) + "Rpt", this.getWorkID());
		WorkFlowDeleteLog log = new WorkFlowDeleteLog();
		log.setOID(this.getWorkID());
		try
		{
			log.Copy(rpt);
			log.setDeleteDT(DataType.getCurrentDateTime());
			log.setOperDept(WebUser.getFK_Dept());
			log.setOperDeptName(WebUser.getFK_DeptName());
			log.setOper(WebUser.getNo());
			log.setDeleteNote(info);
			log.setOID(this.getWorkID());
			log.setFK_Flow(this.getHisFlow().getNo());
			log.InsertAsOID(log.getOID());
			return DoDeleteWorkFlowByReal(isDelSubFlow);
		}
		catch (RuntimeException ex)
		{
			log.CheckPhysicsTable();
			log.Delete();
			throw new RuntimeException(String.valueOf(ex.getStackTrace()));
		}
	}


		///#region 流程的强制终止\删除 或者恢复使用流程,
	/** 
	 恢复流程.
	 
	 param msg 回复流程的原因
	*/
	public final void DoComeBackWorkFlow(String msg) throws Exception
	{
		try
		{
			//设置产生的工作流程为
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
			gwf.setWFState(WFState.Runing);
			gwf.DirectUpdate();

			// 增加消息 
			WorkNode wn = this.GetCurrentWorkNode();
			GenerWorkerLists wls = new GenerWorkerLists(wn.getHisWork().getOID(), wn.getHisNode().getNodeID());
			if (wls.size() == 0)
			{
				throw new RuntimeException("@恢复流程出现错误,产生的工作者列表");
			}

			for (GenerWorkerList item : wls.ToJavaList())
			{
				bp.wf.Dev2Interface.Port_SendMsg(item.getFK_Emp(), "流程恢复通知:" + gwf.getTitle(), "该流程[" + gwf.getTitle() + "]，请打开待办处理.", "rback");
			}
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError("@恢复流程出现错误." + ex.getMessage());
			throw new RuntimeException("@恢复流程出现错误." + ex.getMessage());
		}
	}

		///#endregion

	/** 
	 得到当前的进行中的工作。
	 
	 @return 		 
	*/
	public final WorkNode GetCurrentWorkNode() throws Exception {
		int currNodeID = 0;
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		if (gwf.RetrieveFromDBSources() == 0)
		{
			this.DoFlowOver(ActionType.FlowOver, "非正常结束，没有找到当前的流程记录。", null, null);
			throw new RuntimeException("@" + String.format("工作流程%1$s已经完成。", this.getHisGenerWorkFlow().getTitle()));
		}

		Node nd = new Node(gwf.getFK_Node());
		Work work = nd.getHisWork();
		work.setOID(this.getWorkID());
		work.setNodeID(nd.getNodeID());
		work.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		if (work.RetrieveFromDBSources() == 0)
		{
			Log.DebugWriteError("@WorkID=" + this.getWorkID() + ",FK_Node=" + gwf.getFK_Node() + ".不应该出现查询不出来工作."); // 没有找到当前的工作节点的数据，流程出现未知的异常。
			work.setRec(WebUser.getNo());
			try
			{
				work.Insert();
			}
			catch (RuntimeException ex)
			{
				Log.DebugWriteError("@没有找到当前的工作节点的数据，流程出现未知的异常" + ex.getMessage() + ",不应该出现"); // 没有找到当前的工作节点的数据
			}
		}
		work.setFID(gwf.getFID());

		WorkNode wn = new WorkNode(work, nd);
		return wn;
	}
	/** 
	 结束分流的节点
	 
	 param fid
	 @return 
	*/
	public final String DoFlowOverFeiLiu(GenerWorkFlow gwf) throws Exception {
		// 查询出来有少没有完成的流程。
		int i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkFlow WHERE FID=" + gwf.getFID() + " AND WFState!=1");
		switch (i)
		{
			case 0:
				throw new RuntimeException("@不应该的错误。");
			case 1:
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow  WHERE FID=" + gwf.getFID() + " OR WorkID=" + gwf.getFID());
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE FID=" + gwf.getFID() + " OR WorkID=" + gwf.getFID());

				Work wk = this.getHisFlow().getHisStartNode().getHisWork();
				wk.setOID(gwf.getFID());
				wk.Update();

				return "@当前的工作已经完成，该流程上所有的工作都已经完成。";
			default:
				DBAccess.RunSQL("UPDATE WF_GenerWorkFlow SET WFState=1 WHERE WorkID=" + this.getWorkID());
				DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=1 WHERE WorkID=" + this.getWorkID());
				return "@当前的工作已经完成。";
		}
	}
	/** 
	 处理子线程完成.
	 
	 @return 
	*/
	public final String DoFlowThreadOver() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		Node nd = new Node(gwf.getFK_Node());

		//DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow   WHERE WorkID=" + this.WorkID);
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + this.getWorkID());

		String sql = "SELECT count(DISTINCT WorkID) FROM WF_GenerWorkerlist  WHERE  FID=" + this.getFID();
		int num = DBAccess.RunSQLReturnValInt(sql);
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			/*说明这是最后一个*/
			WorkFlow wf = new WorkFlow(this.getFID());
			wf.DoFlowOver(ActionType.FlowOver, "子线程结束", null, null);
			return "@当前子线程已完成，干流程已完成。";
		}
		else
		{
			return "@当前子线程已完成，干流程还有(" + num + ")个子线程未完成。";
		}
	}


	/** 
	 执行流程完成
	 
	 param at
	 param stopMsg
	 param currNode
	 param rpt
	 param stopFlowType 结束类型:自定义参数
	 param empNo
	 param empName
	 @return 
	*/

	public final String DoFlowOver(ActionType at, String stopMsg, Node currNode, bp.wf.data.GERpt rpt, int stopFlowType, String empNo) throws Exception {
		return DoFlowOver(at, stopMsg, currNode, rpt, stopFlowType, empNo, "");
	}

	public final String DoFlowOver(ActionType at, String stopMsg, Node currNode, bp.wf.data.GERpt rpt, int stopFlowType) throws Exception {
		return DoFlowOver(at, stopMsg, currNode, rpt, stopFlowType, "", "");
	}

	public final String DoFlowOver(ActionType at, String stopMsg, Node currNode, bp.wf.data.GERpt rpt) throws Exception {
		return DoFlowOver(at, stopMsg, currNode, rpt, 1, "", "");
	}

	public final String DoFlowOver(ActionType at, String stopMsg, Node currNode, bp.wf.data.GERpt rpt, int stopFlowType, String empNo, String empName) throws Exception {
		if (null == currNode)
		{
			return "err@当前节点为空..";
		}

		if (DataType.IsNullOrEmpty(stopMsg))
		{
			stopMsg += "流程结束";
		}

		//获得当前的节点.
		WorkNode wn = this.GetCurrentWorkNode();
		wn.rptGe = rpt;

		//调用结束前事件.
		String mymsg = ExecEvent.DoFlow(EventListFlow.FlowOverBefore, wn, null);
		if (DataType.IsNullOrEmpty(mymsg)==false && mymsg.equals("null")==false)
		{
			stopMsg += "@" + mymsg;
		}

		String exp = currNode.getFocusField();
		if (DataType.IsNullOrEmpty(exp) == false && exp.length() > 1)
		{
			if (rpt != null)
			{
				stopMsg += Glo.DealExp(exp, rpt, null);
			}
		}

		//IsMainFlow== false 这个位置是子线程
		if (this.isMainFlow() == false)
		{
			/* 处理子线程完成*/
			stopMsg += this.DoFlowThreadOver();
		}


			///#region 处理明细表的汇总.
		this._IsComplete = 1;

			///#endregion 处理明细表的汇总.


			///#region 处理后续的业务.

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (1 == 2)
		{
			// 是否删除流程注册表的数据？
			ps = new Paras();
			ps.SQL = "DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2 ";
			ps.Add("WorkID1", this.getWorkID());
			ps.Add("WorkID2", this.getWorkID());
			DBAccess.RunSQL(ps);
		}


		// 删除子线程产生的 流程注册信息.
		if (this.getFID() == 0)
		{
			ps = new Paras();
			ps.SQL = "DELETE FROM WF_GenerWorkFlow WHERE FID=" + dbstr + "WorkID";
			ps.Add("WorkID", this.getWorkID());
			DBAccess.RunSQL(ps);
		}

		// 清除工作者.
		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2 ";
		ps.Add("WorkID1", this.getWorkID());
		ps.Add("WorkID2", this.getWorkID());
		DBAccess.RunSQL(ps);

		//把当前的人员字符串加入到参与人里面去,以方便查询.
		String emps = WebUser.getNo() + "," + WebUser.getName() + "@";

		// 设置流程完成状态.
		ps = new Paras();
		if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || SystemConfig.getAppCenterDBType( ) == DBType.UX
			|| SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET  FlowEmps= FlowEmps ||'" + emps + "', WFState=:WFState,WFSta=:WFSta WHERE OID=" + dbstr + "OID";
		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET FlowEmps= CONCAT(FlowEmps ,'" + emps + "'), WFState="+dbstr+"WFState,WFSta="+dbstr+"WFSta WHERE OID=" + dbstr + "OID";
		}
		else
		{
			ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET FlowEmps= FlowEmps + '" + emps + "', WFState=" + dbstr + "WFState,WFSta=" + dbstr + "WFSta WHERE OID=" + dbstr + "OID";
		}

		ps.Add("WFState", WFState.Complete.getValue());
		ps.Add("WFSta", WFSta.Complete.getValue());
		ps.Add("OID", this.getWorkID());
		DBAccess.RunSQL(ps);

		//加入轨迹.
		if (DataType.IsNullOrEmpty(empNo) == true)
		{
			empNo = WebUser.getNo();
			empName = WebUser.getName();
		}
		wn.AddToTrack(at, empNo, empName, wn.getHisNode().getNodeID(), wn.getHisNode().getName(), stopMsg);

		//执行流程结束.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		//增加参与的人员
		if (gwf.getEmps().contains("@" + WebUser.getNo() + ",") == false)
		{
			gwf.setEmps(gwf.getEmps() + "@" + WebUser.getNo() + "," + WebUser.getName());
		}

		gwf.setWFState(WFState.Complete);
		gwf.SetPara("StopFlowType", stopFlowType); //结束流程类型.
		gwf.Update();

		//调用结束后事件.
		stopMsg += ExecEvent.DoFlow(EventListFlow.FlowOverAfter, wn, null);

			///#endregion 处理后续的业务.

		//执行最后一个子流程发送后的检查，不管是否成功，都要结束该流程。
		stopMsg += WorkNodePlus.SubFlowEvent(wn);

		//string dbstr =  bp.difference.SystemConfig.getAppCenterDBVarStr();


			///#region 处理审核问题,更新审核组件插入的审核意见中的 到节点，到人员。
		ps = new Paras();
		ps.SQL = "UPDATE ND" + Integer.parseInt(currNode.getFK_Flow()) + "Track SET NDTo=" + dbstr + "NDTo,NDToT=" + dbstr + "NDToT,EmpTo=" + dbstr + "EmpTo,EmpToT=" + dbstr + "EmpToT WHERE NDFrom=" + dbstr + "NDFrom AND EmpFrom=" + dbstr + "EmpFrom AND WorkID=" + dbstr + "WorkID AND ActionType=" + ActionType.WorkCheck.getValue();
		ps.Add(TrackAttr.NDTo, currNode.getNodeID());
		ps.Add(TrackAttr.NDToT, "", false);
		ps.Add(TrackAttr.EmpTo, "", false);
		ps.Add(TrackAttr.EmpToT, "", false);

		ps.Add(TrackAttr.NDFrom, currNode.getNodeID());
		ps.Add(TrackAttr.EmpFrom, WebUser.getNo(), false);
		ps.Add(TrackAttr.WorkID, this.getWorkID());
		DBAccess.RunSQL(ps);

			///#endregion 处理审核问题.

		//如果存在 BillState列，执行更新, 让其可见.
		if (rpt.getEnMap().getAttrs().contains("BillState") == true)
		{
			rpt.SetValByKey("BillState", 100);
		}
		else
		{
			String ptable = "ND" + Integer.parseInt(gwf.getFK_Flow()) + "Rpt";
			if (rpt.getEnMap().getPhysicsTable().equals(ptable) == false && DBAccess.IsExitsTableCol(rpt.getEnMap().getPhysicsTable(), "BillState") == true)
			{
				DBAccess.RunSQL("UPDATE " + rpt.getEnMap().getPhysicsTable() + " SET BillState=100 WHERE OID=" + this.getWorkID());
			}
		}
		return stopMsg;
	}
	public final String GenerFHStartWorkInfo() throws Exception {
		String msg = "";
		DataTable dt = DBAccess.RunSQLReturnTable("SELECT Title,RDT,Rec,OID FROM ND" + this.getStartNodeID() + " WHERE FID=" + this.getFID());
		switch (dt.Rows.size())
		{
			case 0:
				Node nd = new Node(this.getStartNodeID());
				throw new RuntimeException("@没有找到他们开始节点的数据，流程异常。FID=" + this.getFID() + "，节点：" + nd.getName() + "节点ID：" + nd.getNodeID());
			case 1:
				msg = String.format("@发起人： %1$s  日期：%2$s 发起的流程 标题：%3$s ，已经成功完成。", dt.Rows.get(0).getValue("Rec").toString(), dt.Rows.get(0).getValue("RDT").toString(), dt.Rows.get(0).getValue("Title").toString());
				break;
			default:
				msg = "@下列(" + dt.Rows.size() + ")位人员发起的流程已经完成。";
				for (DataRow dr : dt.Rows)
				{
					msg += "<br>发起人：" + dr.getValue("Rec") + " 发起日期：" + dr.getValue("RDT") + " 标题：" + dr.getValue("Title") + "<a href='./../../WF/WFRpt.htm?WorkID=" + dr.getValue("OID") + "&FK_Flow=" + this.getHisFlow().getNo() + "' target=_blank>详细...</a>";
				}
				break;
		}
		return msg;
	}
	public final int getStartNodeID() throws Exception {
		return Integer.parseInt(this.getHisFlow().getNo() + "01");
	}

	/** 
	 执行冻结
	 
	 param msg 冻结原因
	*/
	public final String DoFix(String fixMsg) throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() == WFState.Fix)
		{
			throw new RuntimeException("@当前已经是冻结的状态您不能执行再冻结.");
		}

		if (DataType.IsNullOrEmpty(fixMsg))
		{
			fixMsg = "无";
		}


		/* 获取它的工作者，向他们发送消息。*/
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(), this.getHisFlow().getNo());
		String emps = "";

		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.isEnable() == false)
			{
				continue; //不发送给禁用的人。
			}
			emps += wl.getFK_Emp() + "," + wl.getFK_EmpText() + ";";
			//写入消息。
			bp.wf.Dev2Interface.Port_SendMsg(wl.getFK_Emp(), this.getHisGenerWorkFlow().getTitle(), fixMsg, "Fix" + wl.getWorkID(), "Fix", wl.getFK_Flow(), wl.getFK_Node(), wl.getWorkID(), wl.getFID());
		}

		/* 执行 WF_GenerWorkFlow 冻结. */
		int sta = WFState.Fix.getValue();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET WFState=" + dbstr + "WFState WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.WFState, sta);
		ps.Add(GenerWorkFlowAttr.WorkID, this.getWorkID());
		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=" + dbstr + "IsPass WHERE WorkID=" + dbstr + "WorkID AND FK_Node=" + this.getHisGenerWorkFlow().getFK_Node();
		ps.Add(GenerWorkerListAttr.IsPass, 9);
		ps.Add(GenerWorkerListAttr.WorkID, this.getWorkID());
		DBAccess.RunSQL(ps);


		// 更新流程报表的状态。 
		ps = new Paras();
		ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET WFState=" + dbstr + "WFState WHERE OID=" + dbstr + "OID";
		ps.Add(GERptAttr.WFState, sta);
		ps.Add(GERptAttr.OID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 记录日志..
		//WorkNode wn = new WorkNode(this.WorkID, this.HisGenerWorkFlow.FK_Node);
		//wn.AddToTrack(ActionType.Info, WebUser.getNo(), WebUser.getName() , wn.HisNode.NodeID, wn.HisNode.Name, fixMsg,);

		return this.getWorkID() + "-" + this.getHisFlow().getName() + "已经成功执行冻结";
	}
	/** 
	 执行解除冻结
	 
	 param msg 冻结原因
	*/
	public final String DoUnFix(String unFixMsg) throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() != WFState.Fix)
		{
			throw new RuntimeException("@当前非冻结的状态您不能执行解除冻结.");
		}

		if (DataType.IsNullOrEmpty(unFixMsg))
		{
			unFixMsg = "无";
		}


		/*** 获取它的工作者，向他们发送消息。*/
		//GenerWorkerLists wls = new GenerWorkerLists(this.WorkID, this.HisFlow.No);

		//string url = Glo.ServerIP + "/" + this.VirPath + this.AppType + "/WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Flow=" + this.HisFlow.No + "&WorkID=" + this.WorkID + "&FID=" + this.HisGenerWorkFlow.FID + "&FK_Node=" + this.HisGenerWorkFlow.FK_Node;
		//string mailDoc = "详细信息:<A href='" + url + "'>打开流程轨迹</A>.";
		//string title = "工作:" + this.HisGenerWorkFlow.Title + " 被" + WebUser.getName() + "冻结" + unFixMsg;
		//string emps = "";
		//foreach (GenerWorkerList wl in wls)
		//{
		//    if (wl.IsEnable == false)
		//        continue; //不发送给禁用的人。

		//    emps += wl.FK_Emp + "," + wl.FK_EmpText + ";";

		//    //写入消息。
		//    BP.WF.Dev2Interface.Port_SendMsg(wl.FK_Emp, title, mailDoc, "Fix" + wl.WorkID, BP.Sys.SMSMsgType.Self, wl.FK_Flow, wl.FK_Node, wl.WorkID, wl.FID);
		//}

		/* 执行 WF_GenerWorkFlow 冻结. */
		int sta = WFState.Runing.getValue();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET WFState=" + dbstr + "WFState WHERE WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkFlowAttr.WFState, sta);
		ps.Add(GenerWorkFlowAttr.WorkID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 更新流程报表的状态。 
		ps = new Paras();
		ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET WFState=" + dbstr + "WFState WHERE OID=" + dbstr + "OID";
		ps.Add(GERptAttr.WFState, sta);
		ps.Add(GERptAttr.OID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 记录日志..
		WorkNode wn = new WorkNode(this.getWorkID(), this.getHisGenerWorkFlow().getFK_Node());
		//wn.AddToTrack(ActionType.Info, WebUser.getNo(), WebUser.getName() , wn.HisNode.NodeID, wn.HisNode.Name, unFixMsg);

		return "已经成功执行解除冻结:";
	}

		///#endregion


		///#region 基本属性
	/** 
	 他的节点
	*/
	private Nodes _HisNodes = null;
	/** 
	 节点s
	*/
	public final Nodes getHisNodes() throws Exception {
		if (this._HisNodes == null)
		{
			this._HisNodes = this.getHisFlow().getHisNodes();
		}
		return this._HisNodes;
	}
	/** 
	 工作节点s(普通的工作节点)
	*/
	private WorkNodes _HisWorkNodesOfWorkID = null;
	/** 
	 工作节点s
	*/
	public final WorkNodes getHisWorkNodesOfWorkID() throws Exception {
		if (this._HisWorkNodesOfWorkID == null)
		{
			this._HisWorkNodesOfWorkID = new WorkNodes();
			this._HisWorkNodesOfWorkID.GenerByWorkID(this.getHisFlow(), this.getWorkID());
		}
		return this._HisWorkNodesOfWorkID;
	}
	/** 
	 工作节点s
	*/
	private WorkNodes _HisWorkNodesOfFID = null;
	/** 
	 工作节点s
	*/
	public final WorkNodes getHisWorkNodesOfFID() throws Exception {
		if (this._HisWorkNodesOfFID == null)
		{
			this._HisWorkNodesOfFID = new WorkNodes();
			this._HisWorkNodesOfFID.GenerByFID(this.getHisFlow(), this.getFID());
		}
		return this._HisWorkNodesOfFID;
	}
	/** 
	 工作流程
	*/
	private Flow _HisFlow = null;
	/** 
	 工作流程
	*/
	public final Flow getHisFlow() throws Exception {
		return this._HisFlow;
	}
	private GenerWorkFlow _HisGenerWorkFlow = null;
	public final GenerWorkFlow getHisGenerWorkFlow() throws Exception {
		if (_HisGenerWorkFlow == null)
		{
			_HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
		}
		return _HisGenerWorkFlow;
	}
	public final void setHisGenerWorkFlow(GenerWorkFlow value)throws Exception
	{_HisGenerWorkFlow = value;
	}
	/** 
	 工作ID
	*/
	private long _WorkID = 0;
	/** 
	 工作ID
	*/
	public final long getWorkID() throws Exception {
		return this._WorkID;
	}
	/** 
	 工作ID
	*/
	private long _FID = 0;
	/** 
	 工作ID
	*/
	public final long getFID() throws Exception {
		return this._FID;
	}
	public final void setFID(long value)throws Exception
	{this._FID = value;
	}
	/** 
	 是否是干流
	*/
	public final boolean isMainFlow() throws Exception {
		if (this.getFID() != 0 && this.getFID() != this.getWorkID())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

		///#endregion


		///#region 构造方法
	public WorkFlow(long wkid) throws Exception {
		this.setHisGenerWorkFlow(new GenerWorkFlow());
		this.getHisGenerWorkFlow().RetrieveByAttr(GenerWorkerListAttr.WorkID, wkid);
		this._FID = this.getHisGenerWorkFlow().getFID();
		if (wkid == 0)
		{
			throw new RuntimeException("@没有指定工作ID, 不能创建工作流程.");
		}

		Flow flow = new Flow(this.getHisGenerWorkFlow().getFK_Flow());
		this._HisFlow = flow;
		this._WorkID = wkid;

	}

	public WorkFlow(Flow flow, long wkid) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(wkid);
		gwf.RetrieveFromDBSources();

		this._FID = gwf.getFID();
		if (wkid == 0)
		{
			throw new RuntimeException("@没有指定工作ID, 不能创建工作流程.");
		}
		//Flow flow= new Flow(FlowNo);
		this._HisFlow = flow;
		this._WorkID = wkid;
	}
	/** 
	 建立一个工作流事例
	 
	 param flow 流程No
	 param wkid 工作ID
	*/
	public WorkFlow(Flow flow, long wkid, long fid)
	{
		this._FID = fid;
		if (wkid == 0)
		{
			throw new RuntimeException("@没有指定工作ID, 不能创建工作流程.");
		}
		//Flow flow= new Flow(FlowNo);
		this._HisFlow = flow;
		this._WorkID = wkid;
	}
	public WorkFlow(String FK_flow, long wkid, long fid) throws Exception {
		this._FID = fid;

		Flow flow = new Flow(FK_flow);
		if (wkid == 0)
		{
			throw new RuntimeException("@没有指定工作ID, 不能创建工作流程.");
		}
		//Flow flow= new Flow(FlowNo);
		this._HisFlow = flow;
		this._WorkID = wkid;
	}

		///#endregion


		///#region 运算属性
	public int _IsComplete = -1;
	/** 
	 是不是完成
	*/
	public final boolean isComplete() throws Exception {

			//  bool s = !DBAccess.IsExits("select workid from WF_GenerWorkFlow WHERE WorkID=" + this.WorkID + " AND FK_Flow='" + this.HisFlow.No + "'");

		GenerWorkFlow generWorkFlow = new GenerWorkFlow(this.getWorkID());
		if (generWorkFlow.getWFState() == WFState.Complete)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	/** 
	 是不是完成
	*/
	public final String isCompleteStr() throws Exception {
		if (this.isComplete())
		{
			return "已";
		}
		else
		{
			return "未";
		}
	}

		///#endregion


		///#region 静态方法

	/** 
	 是否这个工作人员能执行这个工作
	 
	 param nodeId 节点
	 param empId 工作人员
	 @return 能不能执行 
	*/
	public static boolean IsCanDoWorkCheckByEmpStation(int nodeId, String empId)
	{
		boolean isCan = false;
		// 判断岗位对应关系是不是能够执行.
		String sql = "SELECT a.FK_Node FROM WF_NodeStation a,  Port_DeptEmpStation b WHERE (a.FK_Station=b.FK_Station) AND (a.FK_Node=" + nodeId + " AND b.FK_Emp='" + empId + "' )";
		isCan = DBAccess.IsExits(sql);
		if (isCan)
		{
			return true;
		}
		// 判断他的主要工作岗位能不能执行它.
		sql = "select FK_Node from WF_NodeStation WHERE FK_Node=" + nodeId + " AND ( FK_Station in (select FK_Station from Port_DeptEmpStation WHERE FK_Emp='" + empId + "') ) ";
		return DBAccess.IsExits(sql);
	}
	/** 
	 是否这个工作人员能执行这个工作
	 
	 param nodeId 节点
	 param dutyNo 工作人员
	 @return 能不能执行 
	*/
	public static boolean IsCanDoWorkCheckByEmpDuty(int nodeId, String dutyNo)
	{
		String sql = "SELECT a.FK_Node FROM WF_NodeDuty  a,  Port_EmpDuty b WHERE (a.FK_Duty=b.FK_Duty) AND (a.FK_Node=" + nodeId + " AND b.FK_Duty=" + dutyNo + ")";
		if (DBAccess.RunSQLReturnTable(sql).Rows.size() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/** 
	 在物理上能构作这项工作的人员。
	 
	 param nodeId 节点ID
	 @return 
	*/
	public static DataTable CanDoWorkEmps(int nodeId)
	{
		String sql = "select a.FK_Node, b.EmpID from WF_NodeStation  a,  Port_DeptEmpStation b WHERE (a.FK_Station=b.FK_Station) AND (a.FK_Node=" + nodeId + " )";
		return DBAccess.RunSQLReturnTable(sql);
	}
	/** 
	 GetEmpsBy
	 
	 param dt
	 @return 
	*/
	public final Emps GetEmpsBy(DataTable dt) throws Exception {
		// 形成能够处理这件事情的用户几何。
		Emps emps = new Emps();
		for (DataRow dr : dt.Rows)
		{
			emps.AddEntity(new Emp(dr.getValue("EmpID").toString()));
		}
		return emps;
	}


		///#endregion


		///#region 流程方法

	private String _AppType = null;
	/** 
	 虚拟目录的路径
	*/
	public final String getAppType() throws Exception {
		if (_AppType == null)
		{
			if (SystemConfig.getIsBSsystem() == false)
			{
				_AppType = "WF";
			}
			else
			{


				_AppType = "WF";

			}
		}
		return _AppType;
	}
	private String _VirPath = null;
	/** 
	 虚拟目录的路径
	*/
	public final String getVirPath() throws Exception {
		if (_VirPath == null)
		{
			if (SystemConfig.getIsBSsystem() && bp.sys.Glo.getRequest() != null) {
				_VirPath = bp.sys.Glo.getRequest().getRemoteAddr();
			} else {
				_VirPath = "";
			}
		}
		return _VirPath;
	}
	/** 
	 执行挂起
	 
	 param way 挂起方式
	 param relData 释放日期
	 param hungNote 挂起原因
	 @return 
	*/
	public final String DoHungup(HungupWay way, String relData, String hungNote) throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() == WFState.HungUp)
		{
			throw new RuntimeException("err@当前已经是挂起的状态您不能执行在挂起.");
		}

		if (DataType.IsNullOrEmpty(hungNote) == true)
		{
			hungNote = "无";
		}

		if (way == HungupWay.SpecDataRel)
		{
			if (relData.length() < 10)
			{
				throw new RuntimeException("err@解除挂起的日期不正确(" + relData + ")");
			}
		}
		if (relData == null)
		{
			relData = "";
		}


		/* 获取它的工作者，向他们发送消息。*/
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(), this.getHisFlow().getNo());
		String url = Glo.getServerIP() + "/" + this.getVirPath() + this.getAppType() + "/MyView.htm?FK_Flow=" + this.getHisFlow().getNo() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getHisGenerWorkFlow().getFID() + "&FK_Node=" + this.getHisGenerWorkFlow().getFK_Node();
		String mailDoc = "详细信息:<A href='" + url + "'>打开流程轨迹</A>.";
		String title = "工作:" + this.getHisGenerWorkFlow().getTitle() + " 被" + WebUser.getName() + "挂起" + hungNote;
		String emps = "";
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.isEnable() == false)
			{
				continue; //不发送给禁用的人。
			}

			//BP.WF.Port.WFEmp emp = new bp.port.WFEmp(wl.FK_Emp);
			emps += wl.getFK_Emp() + "," + wl.getFK_EmpText() + ";";

			//写入消息。
			bp.wf.Dev2Interface.Port_SendMsg(wl.getFK_Emp(), title, mailDoc, "Hungup" + wl.getWorkID(), bp.wf.SMSMsgType.Hungup, wl.getFK_Flow(), wl.getFK_Node(), wl.getWorkID(), wl.getFID());
		}

		/* 执行 WF_GenerWorkFlow 挂起. */
		int hungSta = WFState.HungUp.getValue();
		String dbstr = SystemConfig.getAppCenterDBVarStr();

		//更新挂起状态.
		this.getHisGenerWorkFlow().setWFState(WFState.HungUp);
		this.getHisGenerWorkFlow().SetPara("Hunguper", WebUser.getNo());
		this.getHisGenerWorkFlow().SetPara("HunguperName", WebUser.getName());
		this.getHisGenerWorkFlow().SetPara("HungupWay", way.getValue());
		this.getHisGenerWorkFlow().SetPara("HungupRelDate", relData);
		this.getHisGenerWorkFlow().SetPara("HungupNote", hungNote);
		this.getHisGenerWorkFlow().SetPara("HungupSta", HungupSta.Apply.getValue()); //设置申请状态.
		this.getHisGenerWorkFlow().setHungupTime(DataType.getCurrentDateTime());
		this.getHisGenerWorkFlow().Update();

		// 更新流程报表的状态。 
		Paras ps = new Paras();
		ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET WFState=" + dbstr + "WFState WHERE OID=" + dbstr + "OID";
		ps.Add(GERptAttr.WFState, hungSta);
		ps.Add(GERptAttr.OID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 记录日志..
		WorkNode wn = new WorkNode(this.getWorkID(), this.getHisGenerWorkFlow().getFK_Node());
		wn.AddToTrack(ActionType.Hungup, WebUser.getNo(), WebUser.getName() , wn.getHisNode().getNodeID(), wn.getHisNode().getName(), hungNote);
		return "已经成功执行挂起,并且已经通知给:" + emps;
	}
	/** 
	 同意挂起
	 
	 @return 
	*/
	public final String HungupWorkAgree() throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() != WFState.HungUp)
		{
			throw new RuntimeException("@非挂起状态,您不能解除挂起.");
		}

		this.getHisGenerWorkFlow().SetPara("HungupSta", HungupSta.Agree.getValue()); //同意.
		this.getHisGenerWorkFlow().SetPara("HungupChecker", WebUser.getNo());
		this.getHisGenerWorkFlow().SetPara("HungupCheckerName", WebUser.getName());
		this.getHisGenerWorkFlow().SetPara("HungupCheckRDT", DataType.getCurrentDateTime());
		this.getHisGenerWorkFlow().Update();

		//如果是按照指定的日期解除挂起.
		int way = this.getHisGenerWorkFlow().GetParaInt("HungupWay", 0);
		if (way == 1)
		{
			String relDT = this.getHisGenerWorkFlow().GetParaString("HungupRelDate");
			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET SDT='" + relDT + "' WHERE WorkID=" + this.getWorkID() + " AND FK_Node=" + this.getHisGenerWorkFlow().getFK_Node());
		}
		return "已经同意挂起.";
	}

	/** 
	 新增取消挂起的API
	 
	 @return 
	 @exception Exception
	*/
	public final String CancelHungupWork() throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() != WFState.HungUp)
		{
			throw new RuntimeException("@非挂起状态,您不能取消挂起.");
		}

		/* 执行解除挂起. */
		this.getHisGenerWorkFlow().setWFState(WFState.Runing); //这里仅仅更新大的状态就好，不在处理AtPara的参数状态.
		this.getHisGenerWorkFlow().Update();

		// 更新流程报表的状态。 
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps = new Paras();
		ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET WFState=2 WHERE OID=" + dbstr + "OID";
		ps.Add(GERptAttr.OID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 更新工作者的挂起时间。
		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET  DTOfUnHungup=" + dbstr + "DTOfUnHungup WHERE FK_Node=" + dbstr + "FK_Node AND WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkerListAttr.DTOfUnHungup, DataType.getCurrentDateTime(), false);
		ps.Add(GenerWorkerListAttr.FK_Node, this.getHisGenerWorkFlow().getFK_Node());
		ps.Add(GenerWorkFlowAttr.WorkID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 记录日志..
		WorkNode wn = new WorkNode(this.getWorkID(), this.getHisGenerWorkFlow().getFK_Node());
		wn.AddToTrack(ActionType.UnHungup, WebUser.getNo(), WebUser.getName() , wn.getHisNode().getNodeID(), wn.getHisNode().getName(), "");
		return "您已经取消挂起，该流程实例已经进入了正常运行状态.";
	}
	/** 
	 拒绝挂起
	 
	 @return 
	*/
	public final String HungupWorkReject(String msg) throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() != WFState.HungUp)
		{
			throw new RuntimeException("@非挂起状态,您不能解除挂起.");
		}

		/* 执行解除挂起. */
		this.getHisGenerWorkFlow().setWFState(WFState.Runing);

		this.getHisGenerWorkFlow().SetPara("HungupSta", HungupSta.Reject.getValue()); //不同意.
		this.getHisGenerWorkFlow().SetPara("HungupNodeID", this.getHisGenerWorkFlow().getFK_Node()); //不同意.
		this.getHisGenerWorkFlow().SetPara("HungupCheckMsg", msg); //拒绝原因.
		this.getHisGenerWorkFlow().Update();

		// 更新流程报表的状态。 
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps = new Paras();
		ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET WFState=2 WHERE OID=" + dbstr + "OID";
		ps.Add(GERptAttr.OID, this.getWorkID());
		DBAccess.RunSQL(ps);

		// 更新工作者的挂起时间。
		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET  DTOfUnHungup=" + dbstr + "DTOfUnHungup WHERE FK_Node=" + dbstr + "FK_Node AND WorkID=" + dbstr + "WorkID";
		ps.Add(GenerWorkerListAttr.DTOfUnHungup, DataType.getCurrentDateTime(), false);
		ps.Add(GenerWorkerListAttr.FK_Node, this.getHisGenerWorkFlow().getFK_Node());
		ps.Add(GenerWorkFlowAttr.WorkID, this.getWorkID());
		DBAccess.RunSQL(ps);


		/* 获取它的工作者，向他们发送消息。*/
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID());
		wls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node, this.getHisGenerWorkFlow().getFK_Node(), null);

		String url = Glo.getServerIP() + "/" + this.getVirPath() + this.getAppType() + "/MyFlow.htm?FK_Flow=" + this.getHisFlow().getNo() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getHisGenerWorkFlow().getFID() + "&FK_Node=" + this.getHisGenerWorkFlow().getFK_Node();
		String mailDoc = "详细信息:<A href='" + url + "'>打开流程</A>.";
		mailDoc += " 拒绝原因:" + msg;
		String title = "工作:" + this.getHisGenerWorkFlow().getTitle() + " 被" + WebUser.getName() + "拒绝挂起.";
		String emps = "";
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.isEnable() == false)
			{
				continue; //不发送给禁用的人。
			}

			emps += wl.getFK_Emp() + "," + wl.getFK_EmpText() + ";";

			//写入消息。
			bp.wf.Dev2Interface.Port_SendMsg(wl.getFK_Emp(), title, msg, "RejectHungup" + wl.getFK_Node() + this.getWorkID(), bp.wf.SMSMsgType.RejectHungup, getHisGenerWorkFlow().getFK_Flow(), getHisGenerWorkFlow().getFK_Node(), this.getWorkID(), this.getFID());
		}

		// 记录日志..
		WorkNode wn = new WorkNode(this.getWorkID(), this.getHisGenerWorkFlow().getFK_Node());
		wn.AddToTrack(ActionType.UnHungup, WebUser.getNo(), WebUser.getName() , wn.getHisNode().getNodeID(), wn.getHisNode().getName(), "拒绝挂起，通知给:" + emps);
		return "您不同意对方挂起，该流程实例已经进入了正常运行状态.";
	}

		///#endregion
}