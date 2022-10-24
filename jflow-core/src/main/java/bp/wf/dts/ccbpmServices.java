package bp.wf.dts;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.web.*;

/**
 ccbpm服务
 */
public class ccbpmServices extends Method
{
	/**
	 ccbpm服务
	 */
	public ccbpmServices()throws Exception
	{
		this.Title = "ccbpm流程服务 ";
		this.Help = "1,自动发送邮件. 2,自动发起流程. 3,自动执行节点任务..";
		this.Help += "所有自动任务的综合.";

		this.GroupName = "流程自动执行定时任务";
	}
	@Override
	public void Init()  {
	}
	/**
	 当前的操纵员是否可以执行这个方法
	 */
	@Override
	public boolean getIsCanDo()  {
		return true;
	}
	/**
	 开始执行方法.

	 @return
	  * @throws Exception
	 */
	@Override
	public Object Do() throws Exception
	{

		//执行自动任务.
		AutoRun_WhoExeIt myen = new AutoRun_WhoExeIt();
		myen.Do();

		//扫描触发式自动发起流程表......
		//自动发起流程.
		AutoRunWF_Task wf_task = new AutoRunWF_Task();
		wf_task.Do();

		//自动发起流程.
		AutoRunStratFlows fls = new AutoRunStratFlows();
		fls.Do();

		//扫描消息表,想外发送消息....
		DoSendMsg();

		//扫描逾期流程数据，处理逾期流程.
		DTS_DealDeferredWork en = new DTS_DealDeferredWork();
		en.Do();

		//同步待办时间戳
		DTS_GenerWorkFlowTimeSpan en2 = new DTS_GenerWorkFlowTimeSpan();
		en2.Do();

		//更新WF_GenerWorkerFlow.TodoSta状态.
		DTS_GenerWorkFlowTodoSta en3 = new DTS_GenerWorkFlowTodoSta();
		en3.Do();

		return "执行完成...";
	}
	/**
	 逾期流程
	 * @throws Exception
	 */
	private void DoOverDueFlow() throws Exception
	{
		//特殊处理天津的需求.
		if (SystemConfig.getCustomerNo().equals(""))
		{
			DoTianJinSpecFunc();
		}

		/// 流程逾期
		//判断是否有流程逾期的消息设置
		DataTable dt = null;
		String sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE a.SDTOfFlow<='" + DataType.getCurrentDataTime() + "' ";
		sql += " AND WFState=2 and b.OutTimeDeal!=0";
		sql += " AND a.FK_Node=b.NodeID";
		dt = DBAccess.RunSQLReturnTable(sql);
		// 遍历循环,逾期表进行处理.
		for (DataRow dr : dt.Rows)
		{
			String fk_flow = dr.getValue("FK_Flow") + "";
			int fk_node = Integer.parseInt(dr.getValue("FK_Node") + "");
			long workid = Long.parseLong(dr.getValue("WorkID") + "");
			String title = dr.getValue("Title") + "";
			//判断流程是否设置逾期消息
			PushMsg pushMsg = new PushMsg();
			int count = pushMsg.Retrieve(PushMsgAttr.FK_Flow, fk_flow, PushMsgAttr.FK_Node, 0, PushMsgAttr.FK_Event, EventListNode.FlowOverDue);
			if (count != 0)
			{
				Node node = new Node(fk_node);
				pushMsg.DoSendMessage(node, node.getHisWork(), null, null, null, null);
			}
			continue;
		}

		/// 流程逾期


		/// 流程预警
		sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE a.SDTOfFlowWarning<='" + DataType.getCurrentDataTime() + "' ";
		sql += " AND WFState=2 and b.OutTimeDeal!=0";
		sql += " AND a.FK_Node=b.NodeID";
		dt = DBAccess.RunSQLReturnTable(sql);
		// 遍历循环,预警表进行处理.
		for (DataRow dr : dt.Rows)
		{
			String fk_flow = dr.getValue("FK_Flow") + "";
			int fk_node = Integer.parseInt(dr.getValue("FK_Node") + "");
			long workid = Long.parseLong(dr.getValue("WorkID") + "");
			String title = dr.getValue("Title") + "";
			//判断流程是否设置逾期消息
			PushMsg pushMsg = new PushMsg();
			int count = pushMsg.Retrieve(PushMsgAttr.FK_Flow, fk_flow, PushMsgAttr.FK_Node, 0, PushMsgAttr.FK_Event, EventListNode.FlowWarning);
			if (count != 0)
			{
				Node node = new Node(fk_node);
				pushMsg.DoSendMessage(node, node.getHisWork(), null, null, null, null);
			}
			continue;
		}

		///  流程预警
		DataTable generTab = null;


		///节点预警
		sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE a.SDTOfNode>='" + DataType.getCurrentDataTime() + "' ";
		sql += " AND WFState=2 and b.OutTimeDeal!=0";
		sql += " AND a.FK_Node=b.NodeID";
		generTab = DBAccess.RunSQLReturnTable(sql);
		for (DataRow row : generTab.Rows)
		{
			String fk_flow = row.getValue("FK_Flow") + "";
			int fk_node = Integer.parseInt(row.getValue("FK_Node") + "");
			long workid = Long.parseLong(row.getValue("WorkID") + "");
			String title = row.getValue("Title") + "";
			String compleateTime = row.getValue("SDTOfNode") + "";
			String starter = row.getValue("Starter") + "";
			Node node = new Node(fk_node);
			if (node.isStartNode())
			{
				continue;
			}
			PushMsgs pushMsgs = new PushMsgs();
			int count = pushMsgs.Retrieve(PushMsgAttr.FK_Flow, node.getFK_Flow(), PushMsgAttr.FK_Node, node.getNodeID(), PushMsgAttr.FK_Event, EventListNode.NodeWarning);
			int maxHour = 0;
			int minHour = 0;
			if (count != 0)
			{
				for (PushMsg pushMsg : pushMsgs.ToJavaList())
				{
					if (pushMsg.GetParaInt("NoticeType") == 0)
					{
						pushMsg.DoSendMessage(node, node.getHisWork(), null, null, null, null);
						continue;
					}
					else
					{
						if (pushMsg.GetParaInt("NoticeHour") > maxHour)
						{
							maxHour = pushMsg.GetParaInt("NoticeHour");
						}
						if (pushMsg.GetParaInt("NoticeHour") < minHour)
						{
							minHour = pushMsg.GetParaInt("NoticeHour");
						}
					}
				}

				//计算当天时间和节点应完成日期的时间差
				int hours = DataType.SpanDays(compleateTime);
				int noticeHour = 0;
				if (hours > minHour) //如果小于最新提醒天数则不发消息
				{
					for (PushMsg pushMsg : pushMsgs.ToJavaList())
					{
						if (pushMsg.GetParaInt("NoticeType") == 1)
						{
							noticeHour = pushMsg.GetParaInt("NoticeHour");
							if (noticeHour == hours)
							{
								pushMsg.DoSendMessage(node, node.getHisWork(), null, null, null, null);
								continue;
							}
						}
					}
				}

			}
		}

		///  节点预警


		///找到要节点逾期的数据.

		sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE a.SDTOfNode<='" + DataType.getCurrentDataTime() + "' ";
		sql += " AND WFState=2 and b.OutTimeDeal!=0";
		sql += " AND a.FK_Node=b.NodeID";
		generTab = DBAccess.RunSQLReturnTable(sql);


		/// 找到要逾期的数据.

		// 遍历循环,逾期表进行处理.
		String msg = "";
		String info = "";
		for (DataRow row : generTab.Rows)
		{
			String fk_flow = row.getValue("FK_Flow") + "";
			int fk_node = Integer.parseInt(row.getValue("FK_Node") + "");
			long workid = Long.parseLong(row.getValue("WorkID") + "");
			String title = row.getValue("Title") + "";
			String compleateTime = row.getValue("SDTOfNode") + "";
			String starter = row.getValue("Starter") + "";

			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, fk_node);

			boolean isLogin = false;
			for (GenerWorkerList item : gwls.ToJavaList())
			{
				if (item.isEnable() == false)
				{
					continue;
				}

				bp.port.Emp emp = new Emp(item.getFK_Emp());
				WebUser.SignInOfGener(emp);
				isLogin = true;
			}

			if (isLogin == false)
			{
				bp.port.Emp emp = new Emp("admin");
				WebUser.SignInOfGener(emp);
			}

			try
			{
				Node node = new Node(fk_node);
				if (node.isStartNode())
				{
					continue;
				}

				///启动逾期消息设置
				PushMsgs pushMsgs = new PushMsgs();
				int count = pushMsgs.Retrieve(PushMsgAttr.FK_Flow, node.getFK_Flow(), PushMsgAttr.FK_Node, node.getNodeID(), PushMsgAttr.FK_Event, EventListNode.NodeOverDue);
				int maxDay = 0;
				int minDay = 0;
				if (count != 0)
				{
					for (PushMsg pushMsg : pushMsgs.ToJavaList())
					{
						if (pushMsg.GetParaInt("NoticeType") == 0)
						{
							pushMsg.DoSendMessage(node, node.getHisWork(), null, null, null, null);
							continue;
						}
						else
						{
							if (pushMsg.GetParaInt("NoticeDay") > maxDay)
							{
								maxDay = pushMsg.GetParaInt("NoticeDay");
							}
							if (pushMsg.GetParaInt("NoticeDay") < minDay)
							{
								minDay = pushMsg.GetParaInt("NoticeDay");
							}
						}
					}

					//计算当天时间和节点应完成日期的时间差
					int days = DataType.SpanDays(DataType.getCurrentDate(), compleateTime);
					int noticeDay = 0;
					if (days > minDay) //如果小于最新提醒天数则不发消息
					{
						for (PushMsg pushMsg : pushMsgs.ToJavaList())
						{
							if (pushMsg.GetParaInt("NoticeType") == 1)
							{
								noticeDay = pushMsg.GetParaInt("NoticeDay");
								if (noticeDay == days || days > maxDay)
								{
									pushMsg.DoSendMessage(node, node.getHisWork(), null, null, null, null);
									continue;
								}
							}
						}
					}

				}

				/// 启动逾期消息设置

				//获得该节点的处理内容.
				String doOutTime = node.GetValStrByKey(NodeAttr.DoOutTime);
				switch (node.getHisOutTimeDeal())
				{
					case None: //逾期不处理.
						continue;
					case AutoJumpToSpecNode: //跳转到指定的节点.
						try
						{

							int jumpNode = Integer.parseInt(doOutTime);
							Node jumpToNode = new Node(jumpNode);

							//设置默认同意.
							bp.wf.Dev2Interface.WriteTrackWorkCheck(jumpToNode.getFK_Flow(), node.getNodeID(), workid, 0, "同意（预期自动审批）", null,null);

							//执行发送.
							info = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, null, null, jumpToNode.getNodeID(), null).ToMsgOfText();

							msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'自动跳转'," + info;

							bp.da.Log.DefaultLogWriteLine(LogType.Info, msg);

						}
						catch (RuntimeException ex)
						{
							msg = "流程 '" + node.getFlowName() + "',WorkID=" + workid + ",标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'自动跳转',跳转异常:" + ex.getMessage();
							bp.da.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case AutoShiftToSpecUser: //走动移交给.
						// 判断当前的处理人是否是.
						Emp empShift = new Emp(doOutTime);
						try
						{
							bp.wf.Dev2Interface.Node_Shift(workid, empShift.getNo(), "流程节点已经逾期,系统自动移交");

							msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'移交到指定的人',已经自动移交给'" + empShift.getName() + ".";
							bp.da.Log.DefaultLogWriteLine(LogType.Info, msg);
						}
						catch (RuntimeException ex)
						{
							msg = "流程 '" + node.getFlowName() + "' ,标题:'" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'移交到指定的人',移交异常：" + ex.getMessage();
							bp.da.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case AutoTurntoNextStep:
						try
						{
							GenerWorkerList workerList = new GenerWorkerList();
							workerList.RetrieveByAttrAnd(GenerWorkerListAttr.WorkID, workid, GenerWorkFlowAttr.FK_Node, fk_node);

							WebUser.SignInOfGener(workerList.getHisEmp());
							WorkNode firstwn = new WorkNode(workid, fk_node);
							String sendIfo = firstwn.NodeSend().ToMsgOfText();
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'自动发送到下一节点',发送消息为:" + sendIfo;
							bp.da.Log.DefaultLogWriteLine(LogType.Info, msg);
						}
						catch (RuntimeException ex)
						{
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'自动发送到下一节点',发送异常:" + ex.getMessage();
							bp.da.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case DeleteFlow:
						info = bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workid, true);
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'删除流程'," + info;
						bp.da.Log.DefaultLogWriteLine(LogType.Info, msg);
						break;
					case RunSQL:
						try
						{
							bp.wf.Work wk = node.getHisWork();
							wk.setOID(workid);
							wk.Retrieve();

							doOutTime = bp.wf.Glo.DealExp(doOutTime, wk, null);

							//替换字符串
							doOutTime.replace("@OID", workid + "");
							doOutTime.replace("@FK_Flow", fk_flow);
							doOutTime.replace("@FK_Node", String.valueOf(fk_node));
							doOutTime.replace("@Starter", starter);
							if (doOutTime.contains("@"))
							{
								msg = "流程 '" + node.getFlowName() + "',标题:  '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'执行SQL'.有未替换的SQL变量.";
								bp.da.Log.DefaultLogWriteLine(LogType.Info, msg);
								break;
							}

							//执行sql.
							DBAccess.RunSQL(doOutTime);
						}
						catch (RuntimeException ex)
						{
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'执行SQL'.运行SQL出现异常:" + ex.getMessage();
							bp.da.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case SendMsgToSpecUser:
						try
						{
							Emp myemp = new Emp(doOutTime);

							boolean boo = bp.wf.Dev2Interface.WriteToSMS(myemp.getNo(), DataType.getCurrentDateByFormart("yyyy-MM-dd HH:mm:ss"), "系统发送逾期消息", "您的流程:'" + title + "'的完成时间应该为'" + compleateTime + "',流程已经逾期,请及时处理!", "系统消息", workid);
							if (boo)
							{
								msg = "'" + title + "'逾期消息已经发送给:'" + myemp.getName() + "'";
							}
							else
							{
								msg = "'" + title + "'逾期消息发送未成功,发送人为:'" + myemp.getName() + "'";
							}
							bp.da.Log.DefaultLogWriteLine(LogType.Info, msg);
						}
						catch (RuntimeException ex)
						{
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'超时处理规则为'执行SQL'.运行SQL出现异常:" + ex.getMessage();
							bp.da.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					default:
						msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() + "'没有找到相应的超时处理规则.";
						bp.da.Log.DefaultLogWriteLine(LogType.Error, msg);
						break;
				}
			}
			catch (RuntimeException ex)
			{
				bp.da.Log.DefaultLogWriteLine(LogType.Error, ex.toString());
			}
		}
		bp.da.Log.DefaultLogWriteLine(LogType.Info, "结束扫描逾期流程数据.");
	}
	/**
	 特殊处理天津的流程
	 当指定的节点，到了10号，15号自动向下发送.
	 */
	private void DoTianJinSpecFunc() throws Exception
	{
		int day = Integer.parseInt(DataType.getCurrentDay());
		if (day == 10 || day == 15)
		{
			/* 一个是10号自动审批，一个是15号自动审批. */
		}
		else
		{
			return;
		}


		///#region 找到要逾期的数据.
		DataTable generTab = null;
		String sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE  ";
		sql += "   a.FK_Node=b.NodeID  ";

		if (day == 10)
		{
			sql += "   AND  b.NodeID=13304 ";
		}

		if (day == 15)
		{
			sql += "AND b.NodeID=13302 ";
		}

		generTab = DBAccess.RunSQLReturnTable(sql);

		///#endregion 找到要逾期的数据.

		// 遍历循环,逾期表进行处理.
		String msg = "";
		for (DataRow row : generTab.Rows)
		{
			String fk_flow = row.getValue("FK_Flow") + "";
			String fk_node = row.getValue("FK_Node") + "";
			long workid = Long.parseLong(row.getValue("WorkID") + "");
			String title = row.getValue("Title") + "";
			String compleateTime = row.getValue("SDTOfNode") + "";
			String starter = row.getValue("Starter") + "";
			try
			{
				Node node = new Node(Integer.parseInt(fk_node));

				try
				{
					GenerWorkerList workerList = new GenerWorkerList();
					workerList.RetrieveByAttrAnd(GenerWorkerListAttr.WorkID, workid, GenerWorkFlowAttr.FK_Node, fk_node);

					WebUser.SignInOfGener(workerList.getHisEmp());

					WorkNode firstwn = new WorkNode(workid, Integer.parseInt(fk_node));
					String sendIfo = firstwn.NodeSend().ToMsgOfText();
					msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
							"'超时处理规则为'自动发送到下一节点',发送消息为:" + sendIfo;

					//输出消息.
					Log.DefaultLogWriteLine(LogType.Info, msg);
				}
				catch (RuntimeException ex)
				{
					msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
							"'超时处理规则为'自动发送到下一节点',发送异常:" + ex.getMessage();
					Log.DefaultLogWriteLine(LogType.Error, msg);
				}
			}
			catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLine(LogType.Error, ex.toString());
			}
		}
		Log.DefaultLogWriteLine(LogType.Info, "结束扫描逾期流程数据.");
	}
	/**
	 发送消息
	 * @throws Exception
	 */
	private void DoSendMsg() throws Exception
	{
		int idx = 0;

		///发送消息
		SMSs sms = new SMSs();
		QueryObject qo = new QueryObject(sms);
		sms.Retrieve(SMSAttr.EmailSta, MsgSta.UnRun.getValue());
		for (SMS sm : sms.ToJavaList())
		{
			if (sm.getEmail().length() == 0)
			{
				sm.setHisEmailSta(MsgSta.RunOK);
				sm.Update();
				continue;
			}
//			try
//			{
//				sm.SendEmailNowAsync(sm.getEmail(), sm.getTitle(), sm.getDocOfEmail());
//			}
//			catch (RuntimeException ex)
//			{
//				bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
//			}
		}

		/// 发送消息
	}

}