package BP.WF;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Sys.EventListOfNode;
import BP.WF.Port.WFEmp;
import BP.WF.Template.NodeCancel;
import BP.WF.Template.NodeCancelAttr;
import BP.WF.Template.NodeCancels;
import BP.Web.WebUser;

/** 
 撤销发送
*/
public class WorkUnSend
{

	private String _AppType = null;
	/** 
	 虚拟目录的路径
	*/
	public final String getAppType()
	{
		if (_AppType == null)
		{
			if (BP.Sys.SystemConfig.getIsBSsystem() == false)
			{
				_AppType = "WF";
			}
			else
			{
				if (BP.Web.WebUser.getIsWap())
				{
					_AppType = "WF/WAP";
				}
				else
				{
					boolean b = BP.Sys.Glo.getRequest().getRequestURI().toLowerCase().contains("oneflow");
					if (b)
					{
						_AppType = "WF/OneFlow";
					}
					else
					{
						_AppType = "WF";
					}
				}
			}
		}
		return _AppType;
	}
	private String _VirPath = null;
	/** 
	 虚拟目录的路径
	*/
	public final String getVirPath()
	{
		if (_VirPath == null)
		{
			if (BP.Sys.SystemConfig.getIsBSsystem())
			{
				_VirPath = Glo.getCCFlowAppPath(); //BP.Sys.Glo.Request.ApplicationPath;
			}
			else
			{
				_VirPath = "";
			}
		}
		return _VirPath;
	}
	public String FlowNo = null;
	private Flow _HisFlow = null;
	public final Flow getHisFlow()
	{
		if (_HisFlow == null)
		{
			this._HisFlow = new Flow(this.FlowNo);
		}
		return this._HisFlow;
	}
	/** 
	 工作ID
	*/
	public long WorkID = 0;
	/** 
	 FID
	 
	*/
	public long FID = 0;
	/** 
	 是否是干流
	*/
	public final boolean getIsMainFlow()
	{
		if (this.FID != 0 && this.FID != this.WorkID)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/** 
	 撤销发送
	*/
	public WorkUnSend(String flowNo, long workID)
	{
		this.FlowNo = flowNo;
		this.WorkID = workID;
	}
	public WorkUnSend(String flowNo, long workID, int unSendToNode)
	{
		this.FlowNo = flowNo;
		this.WorkID = workID;
		this.UnSendToNode = UnSendToNode; //撤销到节点.
	}
	public int UnSendToNode = 0;
	/** 
	 得到当前的进行中的工作。
	 @return 		 
	*/
	public final WorkNode GetCurrentWorkNode()
	{
		int currNodeID = 0;
		GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
		gwf.setWorkID(this.WorkID);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			// this.DoFlowOver(ActionType.FlowOver, "非正常结束，没有找到当前的流程记录。");
			throw new RuntimeException("@" + String.format("工作流程%1$s已经完成。", this.WorkID));
		}

		Node nd = new Node(gwf.getFK_Node());
		Work work = nd.getHisWork();
		work.setOID(this.WorkID);
		work.setNodeID(nd.getNodeID());
		work.SetValByKey("FK_Dept", BP.Web.WebUser.getFK_Dept());
		if (work.RetrieveFromDBSources() == 0)
		{
			Log.DefaultLogWriteLineError("@WorkID=" + this.WorkID + ",FK_Node=" + gwf.getFK_Node() + ".不应该出现查询不出来工作."); // 没有找到当前的工作节点的数据，流程出现未知的异常。
			work.setRec(BP.Web.WebUser.getNo());
			try
			{
				work.Insert();
			}
			catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLineError("@没有找到当前的工作节点的数据，流程出现未知的异常" + ex.getMessage() + ",不应该出现"); // 没有找到当前的工作节点的数据
			}
		}
		work.setFID(gwf.getFID());
		WorkNode wn = new WorkNode(work, nd);
		return wn;
	}
	/** 
	 执行子线程的撤销.
	 @return 
	*/
	private String DoThreadUnSend()
	{
		//定义当前的节点.
		WorkNode wn = this.GetCurrentWorkNode();

		GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
		Node nd = new Node(gwf.getFK_Node());



			///#region 求的撤销的节点.
		int cancelToNodeID = 0;

		if (nd.getHisCancelRole() == CancelRole.SpecNodes)
		{
			//指定的节点可以撤销,首先判断当前人员是否有权限.
			NodeCancels ncs = new NodeCancels();
			ncs.Retrieve(NodeCancelAttr.FK_Node, wn.getHisNode().getNodeID());
			if (ncs.size() == 0)
			{
				throw new RuntimeException("@流程设计错误, 您设置了当前节点(" + wn.getHisNode().getName() + ")可以让指定的节点人员撤销，但是您没有设置指定的节点.");
			}

			// 查询出来. 
			String sql = "SELECT FK_Node FROM WF_GenerWorkerList WHERE FK_Emp='" + WebUser.getNo() + "' AND IsPass=1 AND IsEnable=1 AND WorkID=" + wn.getHisWork().getOID() + " ORDER BY RDT DESC ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@撤销流程错误,您没有权限执行撤销发送.");
			}

			// 找到将要撤销到的NodeID.
			for (DataRow dr : dt.Rows)
			{
				for (NodeCancel nc : ncs.ToJavaList())
				{
					if (nc.getCancelTo() == Integer.parseInt(dr.getValue(0).toString()))
					{
						cancelToNodeID = nc.getCancelTo();
						break;
					}
				}

				if (cancelToNodeID != 0)
				{
					break;
				}
			}

			if (cancelToNodeID == 0)
			{
				throw new RuntimeException("@撤销流程错误,您没有权限执行撤销发送,没有找到可以撤销的节点.");
			}
		}

		if (nd.getHisCancelRole() == CancelRole.OnlyNextStep)
		{
			//如果仅仅允许撤销上一步骤.
			WorkNode wnPri = wn.GetPreviousWorkNode();

			GenerWorkerList wl = new GenerWorkerList();
			int num = wl.Retrieve(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo(), GenerWorkerListAttr.FK_Node, wnPri.getHisNode().getNodeID());
			if (num == 0)
			{
				throw new RuntimeException("@您不能执行撤消发送，因为当前工作不是您发送的或下一步工作已处理。");
			}
			cancelToNodeID = wnPri.getHisNode().getNodeID();
		}

		if (cancelToNodeID == 0)
		{
			throw new RuntimeException("@没有求出要撤销到的节点.");
		}
		//********* 开始执行撤销. *********************
		Node cancelToNode = new Node(cancelToNodeID);
		WorkNode wnOfCancelTo = new WorkNode(this.WorkID, cancelToNodeID);

		// 调用撤消发送前事件。
		String msg = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneBefore, nd, wn.getHisWork(), null);
			///#region 删除当前节点数据。
		// 删除产生的工作列表。
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Delete(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());
		// 删除工作信息,如果是按照ccflow格式存储的。
		if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
		{
			wn.getHisWork().Delete();
		}
		// 删除附件信息。
		DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + gwf.getFK_Node() + "' AND RefPKVal='" + this.WorkID + "'");
			///#endregion 删除当前节点数据。
		// 更新.
		gwf.setFK_Node(cancelToNode.getNodeID());
		gwf.setNodeName(cancelToNode.getName());
		//如果不启动自动记忆，删除tonodes,用于 选择节点发送。撤消后，可重新选择节点发送
		if (cancelToNode.getIsRememberMe() == false)
		{
			gwf.setParas_ToNodes("");
		}

		if (cancelToNode.getIsEnableTaskPool() && Glo.getIsEnableTaskPool())
		{
			gwf.setTaskSta(TaskSta.Takeback);
		}
		else
		{
			gwf.setTaskSta(TaskSta.None);
		}

		gwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName());
		gwf.Update();

		if (cancelToNode.getIsEnableTaskPool() && Glo.getIsEnableTaskPool())
		{
			//设置全部的人员不可用。
			BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0,  IsEnable=-1 WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node());

			//设置当前人员可用。
			BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0,  IsEnable=1  WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node() + " AND FK_Emp='" + WebUser.getNo() + "'");
		}
		else
		{
			BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0  WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node());
		}

		//更新当前节点，到rpt里面。
		BP.DA.DBAccess.RunSQL("UPDATE " + this.getHisFlow().getPTable() + " SET FlowEndNode=" + gwf.getFK_Node() + " WHERE OID=" + this.WorkID);

		// 记录日志..
		wn.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), cancelToNode.getNodeID(), cancelToNode.getName(), "无");

		// 删除数据.
		if (wn.getHisNode().getIsStartNode())
		{
			DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + this.WorkID);
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + this.WorkID);
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + this.WorkID + " AND FK_Node=" + nd.getNodeID());
		}

		if (wn.getHisNode().getIsEval())
		{
			//如果是质量考核节点，并且撤销了。
			DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND WorkID=" + this.WorkID);
		}
			///#region 恢复工作轨迹，解决工作抢办。
		if (cancelToNode.getIsStartNode() == false && cancelToNode.getIsEnableTaskPool() == false)
		{
			WorkNode ppPri = wnOfCancelTo.GetPreviousWorkNode();
			GenerWorkerList wl = new GenerWorkerList();
			wl.Retrieve(GenerWorkerListAttr.FK_Node, wnOfCancelTo.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID, this.WorkID);
			// BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + backtoNodeID + " AND WorkID=" + this.WorkID);
			RememberMe rm = new RememberMe();
			rm.Retrieve(RememberMeAttr.FK_Node, wnOfCancelTo.getHisNode().getNodeID(), RememberMeAttr.FK_Emp, ppPri.getHisWork().getRec());

			String[] empStrs = rm.getObjs().split("[@]", -1);
			for (String s : empStrs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				if (wl.getFK_Emp().equals(s))
				{
					continue;
				}
				GenerWorkerList wlN = new GenerWorkerList();
				wlN.Copy(wl);
				wlN.setFK_Emp(s);

				WFEmp myEmp = new WFEmp(s);
				wlN.setFK_EmpText(myEmp.getName());

				wlN.Insert();
			}
		}
			///#region 如果是开始节点, 检查此流程是否有子线程，如果有则删除它们。
		if (nd.getIsStartNode())
		{
			//要检查一个是否有 子流程，如果有，则删除它们。
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.WorkID);
			if (gwfs.size() > 0)
			{
				for (GenerWorkFlow item : gwfs.ToJavaList())
				{
					//删除每个子线程.
					BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(item.getFK_Flow(), item.getWorkID(), true);
				}
			}
		}

			///#endregion

		//调用撤消发送后事件。
		msg += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneAfter, nd, wn.getHisWork(), null);

		if (wnOfCancelTo.getHisNode().getIsStartNode())
		{
			return "@撤消执行成功." + msg;
		}
		else
		{
			return "@撤消执行成功." + msg;
		}
	}

	/** 
	 执行撤消
	 
	*/
	public final String DoUnSend()
	{
		GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
		
		//如果是越轨流程状态@du.
        String sql = "SELECT COUNT(*) AS Num FROM WF_GenerWorkerlist WHERE WorkID="+this.WorkID+" AND IsPass=80";

        if (DBAccess.RunSQLReturnValInt(sql, 0) != 0) {
			//求出来越轨子流程workid并把它删除掉.
        	GenerWorkFlow gwfSubFlow = new GenerWorkFlow();
        	int i = gwfSubFlow.Retrieve(GenerWorkFlowAttr.PWorkID,this.WorkID);
        	if (i == 1) {
				Dev2Interface.Flow_DoDeleteFlowByReal(gwfSubFlow.getFK_Flow(), gwfSubFlow.getPWorkID(), true);	
			}
        	//执行恢复当前节点待办
        	sql = "UPDATE WF_GenerWorkerlist SET IsPass=0 WHERE IsPass=80 AND FK_Node="+gwf.getFK_Node()+" AND WorkID="+this.WorkID;
			DBAccess.RunSQL(sql);
			
			return "撤销延续流程执行成功，撤销到["+gwf.getNodeName()+"],撤销给["+gwf.getTodoEmps()+"]";
		}
		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 判断是否是会签状态,是否是会签人做的撤销. 主持人是不能撤销的. @于庆海
		if (gwf.getHuiQianTaskSta() != HuiQianTaskSta.None)
		{
			GenerWorkerList gwl = new GenerWorkerList();
			int i = gwl.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

			//如果没有找到当前会签人.
			if (i == 0)
			{
				return "err@当前节点[" + gwf.getNodeName() + "]是会签状态,[" + gwf.getTodoEmps() + "]在执行会签,您不能执行撤销.";
			}

			//如果是会签人，就让其显示待办.
			gwl.setIsPassInt(0);
			gwl.setIsEnable(true);
			gwl.Update();

			// 在待办人员列表里加入他. 要判断当前人员是否是主持人,如果是主持人的话，主持人是否在发送的时候，
			// 就选择方向与接受人.
			if (gwf.getHuiQianZhuChiRen() == WebUser.getNo())
			{
				gwf.setTodoEmps( WebUser.getNo() + "," + BP.Web.WebUser.getName() + ";" + gwf.getTodoEmps());

			}
			else
			{
				gwf.setTodoEmps( gwf.getTodoEmps() + BP.Web.WebUser.getName() + ";");
			}

			gwf.Update();

			return "会签人撤销成功.";
		}
		// 判断是否是会签状态,是否是会签人做的撤销.
		if (gwf.getFID() != 0)
		{
			//执行子线程的撤销.
			return DoThreadUnSend();
			/***
			// * 说明该流程是一个子线程，如果子线程的撤销，就需要删除该子线程。
			// */
			//BP.WF.Dev2Interface.Flow_DeleteSubThread(this.FlowNo, this.WorkID, "撤销方式删除");
			//return "@子线程已经被删除.";
			
		}

		if (this.UnSendToNode != 0 && gwf.getFK_Node() != this.UnSendToNode)
		{
//                 要撤销的节点是分流节点，并且当前节点不在分流节点而是在合流节点的情况， for:华夏银行.
//                 * 1, 分流节点发送给n个人.
//                 * 2, 其中一个人发送到合流节点，另外一个人退回给分流节点。
//                 * 3，现在分流节点的人接收到一个待办，并且需要撤销整个分流节点的发送.
//                 * 4, UnSendToNode 这个时间没有值，并且当前干流节点的停留的节点与要撤销到的节点不一致。
//                 

		  return DoUnSendInFeiLiuHeiliu(gwf);

		}




		// 如果停留的节点是分合流。
		Node nd = new Node(gwf.getFK_Node());
		if (nd.getHisCancelRole() == CancelRole.None)
		{
			//该节点不允许退回.
			throw new RuntimeException("当前节点，不允许撤销。");
		}



		switch (nd.getHisNodeWorkType())
		{
			case WorkFHL:
				//throw new Exception("@分合流点不允许撤消。");
				return this.DoUnSendFeiLiu(gwf);
			case WorkFL:
			case StartWorkFL:
				return this.DoUnSendFeiLiu(gwf);
			case WorkHL:
				if (this.getIsMainFlow())
				{
					// 首先找到与他最近的一个分流点，并且判断当前的操作员是不是分流点上的工作人员。
					return this.DoUnSendHeiLiu_Main(gwf);
				}
				else
				{
					return this.DoUnSendSubFlow(gwf); //是子流程时.
				}
			case SubThreadWork:
				break;
			default:
				break;
		}

		if (nd.getIsStartNode())
		{
			throw new RuntimeException("当前节点是开始节点，所以您不能撤销。");
		}

		//定义当前的节点.
		WorkNode wn = this.GetCurrentWorkNode();


			///#region 求的撤销的节点.
		int cancelToNodeID = 0;

		if (nd.getHisCancelRole() == CancelRole.SpecNodes)
		{
			//指定的节点可以撤销,首先判断当前人员是否有权限.
			NodeCancels ncs = new NodeCancels();
			ncs.Retrieve(NodeCancelAttr.FK_Node, wn.getHisNode().getNodeID());
			if (ncs.size() == 0)
			{
				throw new RuntimeException("@流程设计错误, 您设置了当前节点(" + wn.getHisNode().getName() + ")可以让指定的节点人员撤销，但是您没有设置指定的节点.");
			}

			// 查询出来. 
			String sql1 = "SELECT FK_Node FROM WF_GenerWorkerList WHERE FK_Emp='" + WebUser.getNo() + "' AND IsPass=1 AND IsEnable=1 AND WorkID=" + wn.getHisWork().getOID() + " ORDER BY RDT DESC ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql1);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@撤销流程错误,您没有权限执行撤销发送.");
			}

			// 找到将要撤销到的NodeID.
			for (DataRow dr : dt.Rows)
			{
				for (NodeCancel nc : ncs.ToJavaList())
				{
					if (nc.getCancelTo() == Integer.parseInt(dr.getValue(0).toString()))
					{
						cancelToNodeID = nc.getCancelTo();
						break;
					}
				}

				if (cancelToNodeID != 0)
				{
					break;
				}
			}

			if (cancelToNodeID == 0)
			{
				throw new RuntimeException("@撤销流程错误,您没有权限执行撤销发送,没有找到可以撤销的节点.");
			}
		}

		if (nd.getHisCancelRole() == CancelRole.OnlyNextStep)
		{
			//如果仅仅允许撤销上一步骤.
			WorkNode wnPri = wn.GetPreviousWorkNode();

			GenerWorkerList wl = new GenerWorkerList();
			int num = wl.Retrieve(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo(), GenerWorkerListAttr.FK_Node, wnPri.getHisNode().getNodeID());
			if (num == 0)
			{
				throw new RuntimeException("@您不能执行撤消发送，因为当前工作不是您发送的或下一步工作已处理。");
			}
			cancelToNodeID = wnPri.getHisNode().getNodeID();
		}

		if (cancelToNodeID == 0)
		{
			throw new RuntimeException("@没有求出要撤销到的节点.");
		}

			///#endregion 求的撤销的节点.

		//********* 开始执行撤销. *********************
		Node cancelToNode = new Node(cancelToNodeID);
		WorkNode wnOfCancelTo = new WorkNode(this.WorkID, cancelToNodeID);

		// 调用撤消发送前事件。
		String msg = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneBefore, nd, wn.getHisWork(), null);


			///#region 删除当前节点数据。

		// 删除产生的工作列表。
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Delete(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

		// 删除工作信息,如果是按照ccflow格式存储的。
		if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
		{
			wn.getHisWork().Delete();
		}

		// 删除附件信息。
		DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + gwf.getFK_Node() + "' AND RefPKVal='" + this.WorkID + "'");

			///#endregion 删除当前节点数据。

		// 更新.
		gwf.setFK_Node(cancelToNode.getNodeID());
		gwf.setNodeName(cancelToNode.getName());
		//恢复上一步发送人
        DataTable dtPrevTrack = Dev2Interface.Flow_GetPreviousNodeTrack(this.WorkID,cancelToNode.getNodeID());
        if(dtPrevTrack != null && dtPrevTrack.Rows.size() > 0)
        {
        	gwf.setSender(dtPrevTrack.Rows.get(0).getValue("EmpFrom").toString());
        }
		if (cancelToNode.getIsEnableTaskPool() && Glo.getIsEnableTaskPool())
		{
			gwf.setTaskSta(TaskSta.Takeback);
		}
		else
		{
			gwf.setTaskSta(TaskSta.None);
		}

		gwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName());
		gwf.Update();

		if (cancelToNode.getIsEnableTaskPool() && Glo.getIsEnableTaskPool())
		{
			//设置全部的人员不可用。
			BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0,  IsEnable=-1 WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node());

			//设置当前人员可用。
			BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0,  IsEnable=1  WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node() + " AND FK_Emp='" + WebUser.getNo() + "'");
		}
		else
		{
			BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0  WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node());
		}

		//更新当前节点，到rpt里面。
		BP.DA.DBAccess.RunSQL("UPDATE " + this.getHisFlow().getPTable() + " SET FlowEndNode=" + gwf.getFK_Node() + " WHERE OID=" + this.WorkID);

		// 记录日志..
		wn.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), cancelToNode.getNodeID(), cancelToNode.getName(), "无");

		// 删除数据.
		if (wn.getHisNode().getIsStartNode())
		{
			DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + this.WorkID);
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + this.WorkID);
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + this.WorkID + " AND FK_Node=" + nd.getNodeID());
		}

		if (wn.getHisNode().getIsEval())
		{
			//如果是质量考核节点，并且撤销了。
			DBAccess.RunSQL("DELETE FROM WF_CHEval WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND WorkID=" + this.WorkID);
		}


			///#region 恢复工作轨迹，解决工作抢办。
		if (cancelToNode.getIsStartNode() == false && cancelToNode.getIsEnableTaskPool() == false)
		{
			WorkNode ppPri = wnOfCancelTo.GetPreviousWorkNode();
			GenerWorkerList wl = new GenerWorkerList();
			wl.Retrieve(GenerWorkerListAttr.FK_Node, wnOfCancelTo.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID, this.WorkID);
			// BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + backtoNodeID + " AND WorkID=" + this.WorkID);
			RememberMe rm = new RememberMe();
			rm.Retrieve(RememberMeAttr.FK_Node, wnOfCancelTo.getHisNode().getNodeID(), RememberMeAttr.FK_Emp, ppPri.getHisWork().getRec());

			String[] empStrs = rm.getObjs().split("[@]", -1);
			for (String s : empStrs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				if (wl.getFK_Emp().equals(s))
				{
					continue;
				}
				GenerWorkerList wlN = new GenerWorkerList();
				wlN.Copy(wl);
				wlN.setFK_Emp(s);

				WFEmp myEmp = new WFEmp(s);
				wlN.setFK_EmpText(myEmp.getName());

				wlN.Insert();
			}
		}
			///#region 如果是开始节点, 检查此流程是否有子线程，如果有则删除它们。
		if (nd.getIsStartNode())
		{
			//要检查一个是否有 子流程，如果有，则删除它们。
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.WorkID);
			if (gwfs.size() > 0)
			{
				for (GenerWorkFlow item : gwfs.ToJavaList())
				{
					//删除每个子线程.
					BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(item.getFK_Flow(), item.getWorkID(), true);
				}
			}
		}

			///#endregion

		//调用撤消发送后事件。
		msg += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneAfter, nd, wn.getHisWork(), null);

		if (wnOfCancelTo.getHisNode().getIsStartNode())
		{
			if (BP.Web.WebUser.getIsWap())
			{
				if (wnOfCancelTo.getHisNode().getHisFormType() != NodeFormType.SDKForm)
				{
					return "@撤消发送执行成功." + msg;
				}
				else
				{
					return "@撤销成功." + msg;
				}
			}
			else
			{
				switch (wnOfCancelTo.getHisNode().getHisFormType())
				{
					case FoolForm:
					case FreeForm:
						return "@撤消执行成功." + msg;
					default:
						return "撤销成功" + msg;
				}
			}
		}
		else
		{
			// 更新是否显示。
			//  DBAccess.RunSQL("UPDATE WF_ForwardWork SET IsRead=1 WHERE WORKID=" + this.WorkID + " AND FK_Node=" + cancelToNode.NodeID);
			switch (wnOfCancelTo.getHisNode().getHisFormType())
			{
				case FoolForm:
				case FreeForm:
					return "@撤消执行成功. " + msg;
				default:
					return "撤销成功:" + msg;
			}
		}
	}
	/** 
	 撤消分流点
	 1, 把分流节点的人员设置成待办。
	 2，删除所有该分流点发起的子线程。
	 
	 @param gwf
	 @return 
	*/
	private String DoUnSendFeiLiu(GenerWorkFlow gwf)
	{
		//首先要检查，当前的处理人是否是分流节点的处理人？如果是，就要把，未走完的所有子线程都删除掉。
		GenerWorkerList gwl = new GenerWorkerList();
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
		if (i == 0)
		{
			throw new RuntimeException("@您不能执行撤消发送，因为当前工作不是您发送的。");
		}

		//处理事件.
		Node nd = new Node(gwf.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(gwf.getWorkID());
		wk.RetrieveFromDBSources();

		String msg = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneBefore, nd, wk, null);

		// 记录日志..
		WorkNode wn = new WorkNode(wk, nd);
		wn.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), gwf.getFK_Node(), gwf.getNodeName(), "");

		// 删除分合流记录。
		if (nd.getIsStartNode())
		{
			DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + this.WorkID);
		}

		//删除上一个节点的数据。
		for (Node ndNext : nd.getHisToNodes().ToJavaList())
		{
			i = DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + this.WorkID + " AND FK_Node=" + ndNext.getNodeID());
			if (i == 0)
			{
				continue;
			}

			if (ndNext.getHisRunModel() == RunModel.SubThread)
			{
				//如果到达的节点是子线程,就查询出来发起的子线程。
				GenerWorkFlows gwfs = new GenerWorkFlows();
				gwfs.Retrieve(GenerWorkFlowAttr.FID, this.WorkID);
				for (GenerWorkFlow en : gwfs.ToJavaList())
				{
					BP.WF.Dev2Interface.Flow_DeleteSubThread(gwf.getFK_Flow(), en.getWorkID(), "合流节点撤销发送前，删除子线程.");
				}
				continue;
			}

			// 删除工作记录。
			Works wks = ndNext.getHisWorks();
			if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
			{
				wks.Delete(GenerWorkerListAttr.FID, this.WorkID);
			}
		}

		//设置当前节点。
		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node() + " AND IsPass=1");
		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerFH SET FK_Node=" + gwf.getFK_Node() + " WHERE FID=" + this.WorkID);

		// 设置当前节点的状态.
		Node cNode = new Node(gwf.getFK_Node());
		Work cWork = cNode.getHisWork();
		cWork.setOID(this.WorkID);
		msg += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneAfter, nd, wk, null);
		if (cNode.getIsStartNode())
		{
			if (BP.Web.WebUser.getIsWap())
			{
				return "@撤消执行成功." + msg;
			}
			else
			{
				if (!this.getHisFlow().getFK_FlowSort().equals("00"))
				{
					return "@撤消执行成功." + msg;
				}
				else
				{
					return "@撤消执行成功." + msg;
				}
			}
		}
		else
		{
			// 更新是否显示。
			// DBAccess.RunSQL("UPDATE WF_ForwardWork SET IsRead=1 WHERE WORKID=" + this.WorkID + " AND FK_Node=" + cNode.NodeID);
			if (BP.Web.WebUser.getIsWap() == false)
			{
				if (!this.getHisFlow().getFK_FlowSort().equals("00"))
				{
					return "@撤消执行成功." + msg;
				}
				else
				{
					return "@撤消执行成功." + msg;
				}
			}
			else
			{
				return "@撤消执行成功." + msg;
			}
		}
	}
	/** 
	 分合流的撤销发送.
	 
	 @param gwf
	 @return 
	*/
	private String DoUnSendInFeiLiuHeiliu(GenerWorkFlow gwf)
	{
		//首先要检查，当前的处理人是否是分流节点的处理人？如果是，就要把，未走完的所有子线程都删除掉。
		GenerWorkerList gwl = new GenerWorkerList();

		//删除合流节点的处理人.
		gwl.Delete(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

		//查询已经走得分流节点待办.
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, this.UnSendToNode, GenerWorkerListAttr.FK_Emp, WebUser.getNo());
		if (i == 0)
		{
			throw new RuntimeException("@您不能执行撤消发送，因为当前分流工作不是您发送的。");
		}

		// 更新分流节点，让其出现待办.
		gwl.setIsPassInt(0);
		gwl.setIsRead(false);
		gwl.setRDT(BP.DA.DataType.getCurrentDataTime());
		gwl.setSDT(BP.DA.DataType.getCurrentDataTime()); //这里计算时间有问题.
		gwl.Update();

		// 把设置当前流程运行到分流流程上.
		gwf.setFK_Node(this.UnSendToNode);
		Node nd = new Node(this.UnSendToNode);
		gwf.setNodeName(nd.getName());
		gwf.setSender(BP.Web.WebUser.getNo());
		gwf.setSendDT(BP.DA.DataType.getCurrentDataTime());
		gwf.Update();


		Work wk = nd.getHisWork();
		wk.setOID(gwf.getWorkID());
		wk.RetrieveFromDBSources();

		String msg = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneBefore, nd, wk, null);

		// 记录日志..
		WorkNode wn = new WorkNode(wk, nd);
		wn.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), gwf.getFK_Node(), gwf.getNodeName(), "");

		// 删除分合流记录。
		if (nd.getIsStartNode())
		{
			DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + this.WorkID);
		}

		//删除上一个节点的数据。
		for (Node ndNext : nd.getHisToNodes().ToJavaList())
		{
			i = DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + this.WorkID + " AND FK_Node=" + ndNext.getNodeID());
			if (i == 0)
			{
				continue;
			}

			if (ndNext.getHisRunModel() == RunModel.SubThread)
			{
				//如果到达的节点是子线程,就查询出来发起的子线程。
				GenerWorkFlows gwfs = new GenerWorkFlows();
				gwfs.Retrieve(GenerWorkFlowAttr.FID, this.WorkID);
				for (GenerWorkFlow en : gwfs.ToJavaList())
				{
					BP.WF.Dev2Interface.Flow_DeleteSubThread(gwf.getFK_Flow(), en.getWorkID(), "合流节点撤销发送前，删除子线程.");
				}
				continue;
			}

			// 删除工作记录。
			Works wks = ndNext.getHisWorks();
			if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
			{
				wks.Delete(GenerWorkerListAttr.FID, this.WorkID);
			}
		}

		//设置当前节点。            
		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerFH SET FK_Node=" + gwf.getFK_Node() + " WHERE FID=" + this.WorkID);

		// 设置当前节点的状态.
		Node cNode = new Node(gwf.getFK_Node());
		Work cWork = cNode.getHisWork();
		cWork.setOID(this.WorkID);
		msg += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneAfter, nd, wk, null);
		if (cNode.getIsStartNode())
		{

			return "@撤消执行成功." + msg;

		}
		else
		{

			return "@撤消执行成功." + msg;

		}
	}
	/** 
	 撤消分流点
	 
	 @param gwf
	 @return 
	*/
	private String DoUnSendFeiLiu_bak(GenerWorkFlow gwf)
	{
		//首先要检查，当前的处理人是否是分流节点的处理人？如果是，就要把，未走完的所有子线程都删除掉。
		GenerWorkerList gwl = new GenerWorkerList();

		String sql = "SELECT FK_Node FROM WF_GenerWorkerList WHERE WorkID=" + this.WorkID + " AND FK_Emp='" + BP.Web.WebUser.getNo() + "' AND FK_Node=" + gwf.getFK_Node();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "@您不能执行撤消发送，因为当前工作不是您发送的。";
		}

		//处理事件.
		Node nd = new Node(gwf.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(gwf.getWorkID());
		wk.RetrieveFromDBSources();

		String msg = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneBefore, nd, wk, null);

		// 记录日志..
		WorkNode wn = new WorkNode(wk, nd);
		wn.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), gwf.getFK_Node(), gwf.getNodeName(), "");

		// 删除分合流记录。
		if (nd.getIsStartNode())
		{
			DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + this.WorkID);
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + this.WorkID);
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + this.WorkID + " AND FK_Node=" + nd.getNodeID());
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE FID=" + this.WorkID);
		}

		//删除上一个节点的数据。
		for (Node ndNext : nd.getHisToNodes().ToJavaList())
		{
			int i = DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + this.WorkID + " AND FK_Node=" + ndNext.getNodeID());
			if (i == 0)
			{
				continue;
			}

			// 删除工作记录。
			Works wks = ndNext.getHisWorks();
			if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
			{
				wks.Delete(GenerWorkerListAttr.FID, this.WorkID);
			}

			// 删除已经发起的流程。
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FID=" + this.WorkID + " AND FK_Node=" + ndNext.getNodeID());
		}

		//设置当前节点。
		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node() + " AND IsPass=1");
		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerFH SET FK_Node=" + gwf.getFK_Node() + " WHERE FID=" + this.WorkID);

		// 设置当前节点的状态.
		Node cNode = new Node(gwf.getFK_Node());
		Work cWork = cNode.getHisWork();
		cWork.setOID(this.WorkID);
		msg += nd.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneAfter, nd, wk, null);
		if (cNode.getIsStartNode())
		{
			if (BP.Web.WebUser.getIsWap())
			{
				return "@撤消执行成功." + msg;
			}
			else
			{
				if (!this.getHisFlow().getFK_FlowSort().equals("00"))
				{
					return "@撤消执行成功." + msg;
				}
				else
				{
					return "@撤消执行成功." + msg;
				}
			}
		}
		else
		{
			// 更新是否显示。
			// DBAccess.RunSQL("UPDATE WF_ForwardWork SET IsRead=1 WHERE WORKID=" + this.WorkID + " AND FK_Node=" + cNode.NodeID);
			if (BP.Web.WebUser.getIsWap() == false)
			{
				if (!this.getHisFlow().getFK_FlowSort().equals("00"))
				{
					return "@撤消执行成功." + msg;
				}
				else
				{
					return "@撤消执行成功." + msg;
				}
			}
			else
			{
				return "@撤消执行成功." + msg;
			}
		}
	}
	/** 
	 执行撤销发送
	 
	 @param gwf
	 @return 
	*/
	public final String DoUnSendHeiLiu_Main(GenerWorkFlow gwf)
	{
		Node currNode = new Node(gwf.getFK_Node());
		Node priFLNode = currNode.getHisPriFLNode();
		GenerWorkerList wl = new GenerWorkerList();
		int i = wl.Retrieve(GenerWorkerListAttr.FK_Node, priFLNode.getNodeID(), GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo());
		if (i == 0)
		{
			return "@不是您把工作发送到当前节点上，所以您不能撤消。";
		}

		WorkNode wn = this.GetCurrentWorkNode();
		WorkNode wnPri = new WorkNode(this.WorkID, priFLNode.getNodeID());

		// 记录日志..
		wnPri.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), wnPri.getHisNode().getNodeID(), wnPri.getHisNode().getName(), "无");

		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Delete(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, (new Integer(gwf.getFK_Node())).toString());

		if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
		{
			wn.getHisWork().Delete();
		}

		gwf.setFK_Node(wnPri.getHisNode().getNodeID());
		gwf.setNodeName(wnPri.getHisNode().getName());
		gwf.Update();

		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node());
		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerFH SET FK_Node=" + gwf.getFK_Node() + " WHERE FID=" + this.WorkID);

		ShiftWorks fws = new ShiftWorks();
		fws.Delete(ShiftWorkAttr.FK_Node, (new Integer(wn.getHisNode().getNodeID())).toString(), ShiftWorkAttr.WorkID, (new Long(this.WorkID)).toString());


			///#region 恢复工作轨迹，解决工作抢办。
		if (wnPri.getHisNode().getIsStartNode() == false)
		{
			WorkNode ppPri = wnPri.GetPreviousWorkNode();
			wl = new GenerWorkerList();
			wl.Retrieve(GenerWorkerListAttr.FK_Node, wnPri.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID, this.WorkID);
			// BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + backtoNodeID + " AND WorkID=" + this.WorkID);
			RememberMe rm = new RememberMe();
			rm.Retrieve(RememberMeAttr.FK_Node, wnPri.getHisNode().getNodeID(), RememberMeAttr.FK_Emp, ppPri.getHisWork().getRec());

			String[] empStrs = rm.getObjs().split("[@]", -1);
			for (String s : empStrs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				if (wl.getFK_Emp().equals(s))
				{
					continue;
				}
				GenerWorkerList wlN = new GenerWorkerList();
				wlN.Copy(wl);
				wlN.setFK_Emp(s);

				WFEmp myEmp = new WFEmp(s);
				wlN.setFK_EmpText(myEmp.getName());

				wlN.Insert();
			}
		}
			///#endregion 恢复工作轨迹，解决工作抢办。

		// 删除以前的节点数据.
		wnPri.DeleteToNodesData(priFLNode.getHisToNodes());
		if (wnPri.getHisNode().getIsStartNode())
		{
			if (BP.Web.WebUser.getIsWap())
			{
				if (wnPri.getHisNode().getHisFormType() != NodeFormType.SDKForm)
				{
					return "@撤消执行成功.";
				}
				else
				{
					return "@撤销成功.";
				}
			}
			else
			{
				if (wnPri.getHisNode().getHisFormType() != NodeFormType.SDKForm)
				{
					return "@撤消执行成功.";
				}
				else
				{
					return "@撤销成功.";
				}
			}
		}
		else
		{
			// 更新是否显示。

///#warning
			// DBAccess.RunSQL("UPDATE WF_ForwardWork SET IsRead=1 WHERE WORKID=" + this.WorkID + " AND FK_Node=" + wnPri.HisNode.NodeID);
			if (BP.Web.WebUser.getIsWap() == false)
			{
				return "@撤消执行成功.";
			}
			else
			{
				return "@撤消执行成功.";
			}
		}
	}
	public final String DoUnSendSubFlow(GenerWorkFlow gwf)
	{
		WorkNode wn = this.GetCurrentWorkNode();
		WorkNode wnPri = wn.GetPreviousWorkNode();

		GenerWorkerList wl = new GenerWorkerList();
		int num = wl.Retrieve(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo(), GenerWorkerListAttr.FK_Node, wnPri.getHisNode().getNodeID());
		if (num == 0)
		{
			return "@您不能执行撤消发送，因为当前工作不是您发送的。";
		}

		// 处理事件。
		String msg = wn.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneBefore, wn.getHisNode(), wn.getHisWork(), null);

		// 删除工作者。
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Delete(GenerWorkerListAttr.WorkID, this.WorkID, GenerWorkerListAttr.FK_Node, (new Integer(gwf.getFK_Node())).toString());

		if (this.getHisFlow().getHisDataStoreModel() == BP.WF.Template.DataStoreModel.ByCCFlow)
		{
			wn.getHisWork().Delete();
		}

		gwf.setFK_Node(wnPri.getHisNode().getNodeID());
		gwf.setNodeName(wnPri.getHisNode().getName());
		gwf.Update();

		BP.DA.DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 WHERE WorkID=" + this.WorkID + " AND FK_Node=" + gwf.getFK_Node());
		ShiftWorks fws = new ShiftWorks();
		fws.Delete(ShiftWorkAttr.FK_Node, (new Integer(wn.getHisNode().getNodeID())).toString(), ShiftWorkAttr.WorkID, (new Long(this.WorkID)).toString());


			///#region 判断撤消的百分比条件的临界点条件
		if (!wn.getHisNode().getPassRate().equals(0))
		{
			java.math.BigDecimal all = new java.math.BigDecimal(BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(*) NUM FROM dbo.WF_GenerWorkerList WHERE FID=" + this.FID + " AND FK_Node=" + wnPri.getHisNode().getNodeID()));
			java.math.BigDecimal ok = new java.math.BigDecimal(BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(*) NUM FROM dbo.WF_GenerWorkerList WHERE FID=" + this.FID + " AND IsPass=1 AND FK_Node=" + wnPri.getHisNode().getNodeID()));
			//java.math.BigDecimal rate = all.multiply(100).divide(ok);
			java.math.BigDecimal rate = ok.divide(all.multiply(new java.math.BigDecimal(100)));
			if (wn.getHisNode().getPassRate().compareTo(rate) <= 0)
			{
				DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND WorkID=" + this.FID);
			}
			else
			{
				DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=3 WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND WorkID=" + this.FID);
			}
		}

			///#endregion

		// 处理事件。
		msg += wn.getHisFlow().DoFlowEventEntity(EventListOfNode.UndoneAfter, wn.getHisNode(), wn.getHisWork(), null);

		// 记录日志..
		wn.AddToTrack(ActionType.UnSend, WebUser.getNo(), WebUser.getName(), wn.getHisNode().getNodeID(), wn.getHisNode().getName(), "无");

		if (wnPri.getHisNode().getIsStartNode())
		{
			if (BP.Web.WebUser.getIsWap())
			{
				return "@撤消执行成功." + msg;
			}
			else
			{
				if (!this.getHisFlow().getFK_FlowSort().equals("00"))
				{
					return "@撤消执行成功." + msg;
				}
				else
				{
					return "@撤消执行成功." + msg;
				}
			}
		}
		else
		{
			// 更新是否显示。
			//  DBAccess.RunSQL("UPDATE WF_ForwardWork SET IsRead=1 WHERE WORKID=" + this.WorkID + " AND FK_Node=" + wnPri.HisNode.NodeID);
			if (BP.Web.WebUser.getIsWap() == false)
			{
				if (!this.getHisFlow().getFK_FlowSort().equals("00"))
				{
					return "@撤消执行成功." + msg;
				}
				else
				{
					return "@撤消执行成功." + msg;
				}
			}
			else
			{
				return "@撤消执行成功." + msg;
			}
		}
	}
}