package bp.wf;

import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.da.*;
import bp.wf.template.*;
import bp.*;

public class ShiftWork
{
	/** 
	 工作移交
	 
	 param workID 工作ID
	 param toEmp 要移交的人
	 param msg 移交信息
	 @return 执行结果
	*/
	public static String Node_Shift_ToEmp(long workID, String toEmp, String msg) throws Exception {
		if (toEmp.equals(WebUser.getNo()) == true)
		{
			throw new RuntimeException("err@您不能移交给您自己。");
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		if (gwf.getWFSta() == WFSta.Complete)
		{
			throw new RuntimeException("err@流程已经完成，您不能执行移交了。");
		}

		int i = 0;
		//人员.
		Emp emp = new Emp(toEmp);
		Node nd = new Node(gwf.getFK_Node());
		Work work = nd.getHisWork();
		work.setOID(workID);
		if (nd.getTodolistModel() == TodolistModel.Order || nd.getTodolistModel() == TodolistModel.Teamup || nd.getTodolistModel() == TodolistModel.TeamupGroupLeader)
		{
			/*如果是队列模式，或者是协作模式, 就直接把自己的gwl 信息更新到被移交人身上. */

			//检查被移交人是否在当前的待办列表里否？
			GenerWorkerList gwl = new GenerWorkerList();
			i = gwl.Retrieve(GenerWorkerListAttr.FK_Emp, emp.getUserID(), GenerWorkerListAttr.FK_Node, nd.getNodeID(), GenerWorkerListAttr.WorkID, workID);
			if (i == 1)
			{
				return "err@移交失败，您所移交的人员(" + emp.getUserID() + " " + emp.getName() + ")已经在代办列表里.";
			}

			//把自己的待办更新到被移交人身上.
			String sql = "UPDATE WF_GenerWorkerlist SET IsRead=0, FK_Emp='" + emp.getUserID() + "', FK_EmpText='" + emp.getName() + "' WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Node=" + gwf.getFK_Node() + " AND WorkID=" + workID;
			int myNum = DBAccess.RunSQL(sql);


				///#region 判断是否是,admin的移交.
			if (myNum == 0)
			{
				//说明移交人是 admin，执行的.
				GenerWorkerLists mygwls = new GenerWorkerLists();
				mygwls.Retrieve(GenerWorkerListAttr.WorkID, workID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), null);
				if (mygwls.size() == 0)
				{
					throw new RuntimeException("err@系统错误，没有找到待办.");
				}

				//把他们都删除掉.
				mygwls.Delete(GenerWorkerListAttr.WorkID, workID, GenerWorkerListAttr.FK_Node, gwf.getFK_Node());

				//取出来第1个，把人员信息改变掉.
				for (GenerWorkerList item : mygwls.ToJavaList())
				{
					item.setFK_Emp(WebUser.getNo());
					item.setFK_EmpText(WebUser.getName());

					item.setFK_Dept(WebUser.getFK_Dept());
					item.setFK_DeptT(WebUser.getFK_DeptName());

					item.setRead(false);

					item.Insert(); //执行插入.
					break;
				}
			}

				///#endregion 判断是否是,admin的移交.

			//记录日志.
			Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName() , nd.getNodeID(), nd.getName(), toEmp, emp.getName(), msg, null);

			//移交后事件
			String atPara1 = "@SendToEmpIDs=" + emp.getUserID();
			String info = "@" + ExecEvent.DoNode(EventListNode.ShitAfter, nd, work, null, atPara1);

			//处理移交后发送的消息事件,发送消息.
			PushMsgs pms1 = new PushMsgs();
			pms1.Retrieve(PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Event, EventListNode.ShitAfter, null);
			for (PushMsg pm : pms1.ToJavaList())
			{
				pm.DoSendMessage(nd, nd.getHisWork(), null, null, null, emp.getUserID());
			}

			gwf.setWFState(WFState.Shift);
			gwf.setTodoEmpsNum(1);
			gwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName() + ";");
			gwf.Update();
			return "移交成功." + info;
		}

		//非协作模式.
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.WorkID, gwf.getWorkID(), null);
		gwls.Delete(GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.WorkID, gwf.getWorkID());

		for (GenerWorkerList item : gwls.ToJavaList())
		{
			item.setFK_Emp(emp.getUserID());
			item.setFK_EmpText(emp.getName());
			item.setEnable(true);
			item.Insert();
			break;
		}

		gwf.setWFState(WFState.Shift);
		gwf.setTodoEmpsNum(1);
		gwf.setTodoEmps(emp.getUserID() + "," + emp.getName() + ";");
		gwf.Update();

		//记录日志.
		Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName() , nd.getNodeID(), nd.getName(), toEmp, emp.getName(), msg, null);

		String inf1o = "@工作移交成功。@您已经成功的把工作移交给：" + emp.getUserID() + " , " + emp.getName();
		//移交后事件
		String atPara = "@SendToEmpIDs=" + emp.getUserID();
		WorkNode wn = new WorkNode(work, nd);
		inf1o += "@" + ExecEvent.DoNode(EventListNode.ShitAfter, wn, null, atPara);
		return inf1o;
	}
	/** 
	 工作移交
	 
	 param workID 工作ID
	 param toEmps 要移交的多个人,比如:zhangsan,lisi
	 param msg
	 @return 执行信息.err@说明执行错误.
	*/
	public static String Node_Shift_ToEmps(long workID, String toEmps, String msg) throws Exception {
		if (toEmps.equals(WebUser.getNo()) == true)
		{
			throw new RuntimeException("err@您不能移交给您自己。");
		}

		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		if (gwf.getWFSta() == WFSta.Complete)
		{
			throw new RuntimeException("err@流程已经完成，您不能执行移交了。");
		}

		//定义变量,查询出来当前的人员列表.
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.WorkID, workID, null);
		//定义变量.
		GenerWorkerList gwl = null;

		int i = 0;
		//人员.
		Node nd = new Node(gwf.getFK_Node());
		Work work = nd.getHisWork();
		work.setOID(workID);

		String info = null;
		String[] strs = toEmps.split("[,]", -1);
		String empNames = "";
		for (String toEmp : strs)
		{
			Emp emp = new Emp(toEmp);
			if (nd.getTodolistModel() == TodolistModel.Order || nd.getTodolistModel() == TodolistModel.Teamup || nd.getTodolistModel() == TodolistModel.TeamupGroupLeader)
			{
				/*如果是队列模式，或者是协作模式, 就直接把自己的gwl 信息更新到被移交人身上. */

				//检查被移交人是否在当前的待办列表里否？
				i = gwl.Retrieve(GenerWorkerListAttr.FK_Emp, emp.getUserID(), GenerWorkerListAttr.FK_Node, nd.getNodeID(), GenerWorkerListAttr.WorkID, workID);
				if (i == 1)
				{
					info += "err@移交失败，您所移交的人员(" + emp.getUserID() + " " + emp.getName() + ")已经在代办列表里.";
					continue;
				}

				if (i == 0)
				{
					return "";
				}

				//写入移交数据.
				gwl = (GenerWorkerList)gwls.get(0);
				gwl.setFK_Emp(emp.getUserID());
				gwl.setFK_EmpText(emp.getName());
				gwl.setIsPassInt(0);
				gwl.Insert();

				//记录日志.
				Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName() , nd.getNodeID(), nd.getName(), toEmp, emp.getName(), msg, null);

				//移交后事件
				String atPara1 = "@SendToEmpIDs=" + emp.getUserID();
				info += "@" + ExecEvent.DoNode(EventListNode.ShitAfter, nd, work, null, atPara1);

				//处理移交后发送的消息事件,发送消息.
				PushMsgs pms1 = new PushMsgs();
				pms1.Retrieve(PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Event, EventListNode.ShitAfter, null);
				for (PushMsg pm : pms1.ToJavaList())
				{
					pm.DoSendMessage(nd, nd.getHisWork(), null, null, null, emp.getUserID());
				}

				info += "info@成功移交给:" + emp.getUserID() + "," + emp.getName();
				continue;
			}

			//非协作模式.
			//写入移交数据.
			gwl = (GenerWorkerList)gwls.get(0);
			gwl.setFK_Emp(emp.getUserID());
			gwl.setFK_EmpText(emp.getName());
			gwl.setIsPassInt(0);
			gwl.Insert();
		}

		//重新查询.
		gwls.Retrieve(GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.WorkID, workID, null);

		//工作处理人员.
		String todoEmps = "";
		for (GenerWorkerList mygwl : gwls.ToJavaList())
		{
			todoEmps += mygwl.getFK_Emp() + "," + mygwl.getFK_EmpText() + ";";
		}

		//更新主表信息.
		gwf.setWFState(WFState.Shift);
		gwf.setTodoEmpsNum(gwls.size());
		gwf.setTodoEmps(todoEmps); //处理人员.
		gwf.Update();

		//删除自己的待办.
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE WorkID=" + gwf.getWorkID() + " AND FK_Node=" + gwf.getFK_Node() + " AND FK_Emp='" + WebUser.getNo() + "'");

		//记录日志.
		Glo.AddToTrack(ActionType.Shift, nd.getFK_Flow(), workID, gwf.getFID(), nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName() , nd.getNodeID(), nd.getName(), toEmps, "移交给多个人", msg, null);

		//移交后事件.
		String atPara = "@SendToEmpIDs=" + toEmps;
		WorkNode wn = new WorkNode(work, nd);
		info += "@" + ExecEvent.DoNode(EventListNode.ShitAfter, wn, null, atPara);
		return info;
	}
	/** 
	 撤消移交
	 
	 @return 
	*/
	public static String DoUnShift(long workid) throws Exception {

		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), null);
		if (wls.size() == 0)
		{
			return "移交失败没有当前的工作。";
		}

		Node nd = new Node(gwf.getFK_Node());
		Work wk1 = nd.getHisWork();
		wk1.setOID(workid);
		wk1.Retrieve();

		// 记录日志.
		WorkNode wn = new WorkNode(wk1, nd);
		wn.AddToTrack(ActionType.UnShift, WebUser.getNo(), WebUser.getName() , nd.getNodeID(), nd.getName(), "撤消移交");

		//删除撤销信息.
		DBAccess.RunSQL("DELETE FROM WF_ShiftWork WHERE WorkID=" + workid + " AND FK_Node=" + gwf.getFK_Node());

		//更新流程主表字段信息
		gwf.setWFState(WFState.Runing);
		gwf.Update();

		if (wls.size() == 1)
		{
			GenerWorkerList wl = (GenerWorkerList)wls.get(0);
			wl.setFK_Emp(WebUser.getNo());
			wl.setFK_EmpText(WebUser.getName());
			wl.setEnable(true);
			wl.setIsPass(false);
			wl.Update();
			return "@撤消移交成功。";
		}

		GenerWorkerList mywl = null;
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.getFK_Emp().equals(WebUser.getNo()))
			{
				wl.setFK_Emp(WebUser.getNo());
				wl.setFK_EmpText(WebUser.getName());
				wl.setEnable(true);
				wl.setIsPass(false);
				wl.Update();
				mywl = wl;
			}
			else
			{
				wl.Delete();
			}
		}
		if (mywl != null)
		{
			return "@撤消移交成功";
		}

		GenerWorkerList wk = (GenerWorkerList)wls.get(0);
		GenerWorkerList wkNew = new GenerWorkerList();
		wkNew.Copy(wk);
		wkNew.setFK_Emp(WebUser.getNo());
		wkNew.setFK_EmpText(WebUser.getName());
		wkNew.setEnable(true);
		wkNew.setIsPass(false);
		wkNew.Insert();
		return "@撤消移交成功";
	}
}