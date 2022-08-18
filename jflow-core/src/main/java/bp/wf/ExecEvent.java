package bp.wf;

import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.wf.template.*;
import bp.port.*;
import bp.*;

/** 
 执行事件
*/
public class ExecEvent
{
	/** 
	 执行表单
	 
	 param md
	 param eventType
	 param en
	 param atParas
	 @return 
	*/

	public static String DoFrm(MapData md, String eventType, Entity en) throws Exception {
		return DoFrm(md, eventType, en, null);
	}

//ORIGINAL LINE: public static string DoFrm(MapData md, string eventType, Entity en,string atParas = null)
	public static String DoFrm(MapData md, String eventType, Entity en, String atParas) throws Exception {

			///#region 首先执行通用的事件重载方法.
		if (EventListFrm.FrmLoadBefore.equals(eventType) == true)
		{
			OverrideFile.FrmEvent_LoadBefore(md.getNo(), en);
		}

		//装载之后.
		if (EventListFrm.FrmLoadAfter.equals(eventType) == true)
		{
			OverrideFile.FrmEvent_FrmLoadAfter(md.getNo(), en);
		}

		/**保存之前.
		*/
		if (EventListFrm.SaveBefore.equals(eventType) == true)
		{
			OverrideFile.FrmEvent_SaveBefore(md.getNo(), en);
		}

		//保存之后.
		if (EventListFrm.SaveAfter.equals(eventType) == true)
		{
			OverrideFile.FrmEvent_SaveAfter(md.getNo(), en);
		}

			///#endregion 首先执行通用的事件重载方法.

		String str = md.getFrmEvents().DoEventNode(eventType, en);

		String mystrs = null;
		if (md.getHisFEB() != null)
		{
			mystrs = md.getHisFEB().DoIt(eventType, en, atParas);
		}

		if (str == null)
		{
			return mystrs;
		}

		if (mystrs == null)
		{
			return str;
		}

		return str + "@" + mystrs;
	}
	/** 
	 执行节点事件
	 
	 param doType
	 param nd
	 param wk
	 param objs
	 param atParas
	 @return 
	*/

	public static String DoNode(String doType, Node nd, Work wk, SendReturnObjs objs) throws Exception {
		return DoNode(doType, nd, wk, objs, null);
	}

	public static String DoNode(String doType, Node nd, Work wk) throws Exception {
		return DoNode(doType, nd, wk, null, null);
	}

//ORIGINAL LINE: public static string DoNode(string doType, Node nd, Work wk, SendReturnObjs objs = null, string atParas = null)
	public static String DoNode(String doType, Node nd, Work wk, SendReturnObjs objs, String atParas) throws Exception {
		WorkNode wn = new WorkNode(wk, nd);
		return DoNode(doType, wn, objs, atParas);
	}
	/** 
	 执行事件
	 
	 param eventFlag
	 param wn
	*/

	public static String DoNode(String doType, WorkNode wn, SendReturnObjs objs) throws Exception {
		return DoNode(doType, wn, objs, null);
	}

	public static String DoNode(String doType, WorkNode wn) throws Exception {
		return DoNode(doType, wn, null, null);
	}

//ORIGINAL LINE: public static string DoNode(string doType, WorkNode wn, SendReturnObjs objs = null, string atPara = null)
	public static String DoNode(String doType, WorkNode wn, SendReturnObjs objs, String atPara) throws Exception {
		if (wn.getHisNode() == null)
		{
			return null;
		}
		if (objs == null)
		{
			objs = wn.HisMsgObjs;
		}

		//如果执行了节点发送成功时间. 
		if (doType.equals(EventListNode.SendSuccess) == true)
		{
			WorkNodePlus.SendDraftSubFlow(wn); //执行自动发送子流程草稿.
		}


			///#region 2021.5.30 gaoxin. 更新授权岗位:为中科软
		//更新授权岗位:为中科软. 如果是当前节点的处理人员是按照岗位绑定的，就需要吧授权岗，写入到 Emps里面去.
		if (doType.equals(EventListNode.SendSuccess) == true && bp.difference.SystemConfig.GetValByKeyBoolen("IsEnableAuthDeptStation",false) == true)
		{
			// 如果这些计算人员的方式有岗位的因素，就需要把当前人员授权岗增加上去.
			if (wn.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStation || wn.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStationOnly || wn.getHisNode().getHisDeliveryWay() == DeliveryWay.ByStationAndEmpDept || wn.getHisNode().getHisDeliveryWay() == DeliveryWay.ByDeptAndStation)
			{
				String sql = "SELECT A.FK_Dept, A.FK_Station FROM Port_DeptEmpStation A, WF_NodeStation B ";
				sql += " WHERE A.FK_Emp='" + WebUser.getNo() + "' AND A.FK_Station=B.FK_Station AND B.FK_Node=" + wn.getHisNode().getNodeID();
				DataTable dt = DBAccess.RunSQLReturnTable(sql);

				for (DataRow dr : dt.Rows)
				{
				   String strs = "@" + dr.getValue(0).toString() + "_" + dr.getValue(1).toString() + "@";
					if (wn.getHisGenerWorkFlow().getEmps().contains("@" + strs) == true)
					{
						continue;
					}

					wn.getHisGenerWorkFlow().setEmps(wn.getHisGenerWorkFlow().getEmps() + strs);
				}
			}
		}

			///#endregion 2021.5.30.

		//写入消息之前，删除所有的消息.
		if (bp.wf.Glo.isEnableSysMessage() == true)
		{
			try
			{
				switch (doType)
				{
					case EventListNode.SendWhen:
						if (wn.getHisNode().getTodolistModel() == TodolistModel.QiangBan)
						{
							DBAccess.RunSQL("DELETE FROM Sys_SMS WHERE WorkID=" + wn.getHisWork().getOID());
						}
						else
						{
							DBAccess.RunSQL("DELETE FROM Sys_SMS WHERE SendTo='" + WebUser.getNo() + "' AND WorkID=" + wn.getHisWork().getOID());
						}
						break;
					case EventListNode.ReturnAfter:
					case EventListNode.ShitAfter:
						DBAccess.RunSQL("DELETE FROM Sys_SMS WHERE WorkID=" + wn.getHisWork().getOID());
						break;
					default:
						break;
				}
			}
			catch (RuntimeException ex)
			{
				SMS sms = new SMS();
				sms.CheckPhysicsTable();
			}
		}

		int toNodeID = 0;
		if (wn.JumpToNode != null)
		{
			toNodeID = wn.JumpToNode.getNodeID();
		}

		String msg = null; //定义执行的消息.
		if (wn.getHisFlow().getFEventEntity() == null)
		{
		}

		if (wn.getHisFlow().getFEventEntity() != null)
		{
			wn.getHisFlow().getFEventEntity().SendReturnObjs = objs;
			msg = wn.getHisFlow().getFEventEntity().DoIt(doType, wn.getHisNode(), wn.getHisWork(), atPara, toNodeID, wn.JumpToEmp);
		}


			///#region 执行事件.
		FrmEvents fes = wn.getHisNode().getFrmEvents(); //获得当前的事件.
		if (!fes.isEmpty())
		{
			// 2019-08-27 取消节点事件 zl
			String msg1 = fes.DoEventNode(doType, wn.getHisWork(), atPara);
			if (msg != null && msg1 != null)
			{
				msg += msg1;
			}

			if (msg == null)
			{
				msg = msg1;
			}
		}

			///#endregion 执行事件.


			///#region 处理消息推送
		//有一些事件没有消息，直接 return ;
		switch (doType)
		{
			case EventListNode.WorkArrive:
			case EventListNode.SendSuccess:
			case EventListNode.ShitAfter:
			case EventListNode.ReturnAfter:
			case EventListNode.UndoneAfter:
			case EventListNode.AskerReAfter:
			case EventListFlow.FlowOverAfter: //流程结束后.
				break;
			default:
				return msg;
		}

		//执行消息的发送.
		PushMsgs pms = wn.getHisNode().getHisPushMsgs();
		if (pms.size() == 0)
		{
			return msg; //如果没有设置消息.
		}

		//撤销之后.
		if (doType.equals(EventListNode.UndoneAfter) == true)
		{
			AtPara ap = new AtPara(atPara);
			if (toNodeID == 0)
			{
				toNodeID = ap.GetValIntByKey("ToNode", 0);
			}
			if (toNodeID == 0)
			{
				return msg;
			}

			Node toNode = new Node(toNodeID);
			pms = toNode.getHisPushMsgs();
		}

		String msgAlert = ""; //生成的提示信息.
		for (PushMsg item : pms.ToJavaList())
		{
			if (!item.getFK_Event().equals(doType))
			{
				continue;
			}

			if (item.getSMSPushWay() == 0)
			{
				continue; // 如果都没有消息设置，就放过.
			}

			//执行发送消息.
			msgAlert += item.DoSendMessage(wn.getHisNode(), wn.getHisWork(), atPara, objs);
		}
		if(DataType.IsNullOrEmpty(msg)==true)
			return msgAlert;
		return msg + msgAlert;

			///#endregion 处理消息推送.
	}
	public static String DoFlow(String doType, Work wk, Node nd, String atPara) throws Exception {
		WorkNode wn = new WorkNode(wk, nd);
		return DoFlow(doType, wn, atPara);
	}
	/** 
	 执行流程事件
	 
	 param doType
	 param wn
	 param atPara
	 @return 
	*/
	public static String DoFlow(String doType, WorkNode wn, String atPara) throws Exception {
		if (wn.getHisNode() == null)
		{
			return null;
		}

		int toNodeID = 0;
		if (wn.JumpToNode != null)
		{
			toNodeID = wn.JumpToNode.getNodeID();
		}

		//执行的参数.
		bp.wf.CCBill_FlowEvent.DoFlow(doType, wn, atPara);


			///#region 写入消息之前,删除消息,不让其在提醒.
		if (bp.wf.Glo.isEnableSysMessage() == true)
		{
			switch (doType)
			{
				case EventListFlow.FlowOverAfter:
					DBAccess.RunSQL("DELETE FROM Sys_SMS WHERE (MsgType='" + EventListNode.SendSuccess + "' OR MsgType='" + EventListNode.ReturnAfter + "'  ) AND WorkID=" + wn.getHisWork().getOID());
					break;
				case EventListFlow.AfterFlowDel: //删除所有的消息，包括抄送.
					DBAccess.RunSQL("DELETE FROM Sys_SMS WHERE AtPara LIKE '%=" + wn.getHisWork().getOID() + "@' OR WorkID=" + wn.getHisWork().getOID());
					break;
				default:
					break;
			}
		}

			///#endregion 写入消息之前,删除消息,不让其在提醒.

		String msg = null;
		if (wn.getHisFlow().getFEventEntity() == null)
		{
		}
		if (wn.getHisFlow().getFEventEntity() != null)
		{
			wn.getHisFlow().getFEventEntity().SendReturnObjs = wn.HisMsgObjs;
			msg = wn.getHisFlow().getFEventEntity().DoIt(doType, wn.getHisNode(), wn.getHisWork(), null, toNodeID, wn.JumpToEmp);
		}


			///#region 执行事件.
		FrmEvents fes = wn.getHisFlow().getFrmEvents(); //获得当前的事件.
		if (!fes.isEmpty())
		{
			String msg1 = fes.DoEventNode(doType, wn.getHisWork(), atPara);
			if (msg1 != null && msg != null)
			{
				msg += msg1;
			}

			if (msg == null)
			{
				msg = msg1;
			}
		}

			///#endregion 执行事件.


			///#region 处理消息推送
		//有一些事件没有消息，直接 return ;
		switch (doType)
		{
			case EventListFlow.BeforeFlowDel:
			case EventListFlow.FlowOnCreateWorkID:
			case EventListFlow.FlowOverBefore:
			case EventListFlow.FlowOverAfter:
				break;
			default:
				return msg;
		}

		//执行消息的发送.
		PushMsgs pms = wn.getHisFlow().getPushMsgs();
		if (pms.size() == 0)
		{
			return msg;
		}



		String msgAlert = ""; //生成的提示信息.
		for (PushMsg item : pms.ToJavaList())
		{
			if (!item.getFK_Event().equals(doType))
			{
				continue;
			}

			if (item.getSMSPushWay() == 0)
			{
				continue; // 如果都没有消息设置，就放过.
			}

			//执行发送消息.
			msgAlert += item.DoSendMessage(wn.getHisNode(), wn.getHisWork(), atPara, wn.HisMsgObjs);
		}
		if(msg==null)
			return msgAlert;
		return msg + msgAlert;

			///#endregion 处理消息推送.
	}
}