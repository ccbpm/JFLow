package BP.WF.DTS;

import BP.DA.*;
import BP.Web.WebUser;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.time.*;
import java.util.Date;

/** 
 Method 的摘要说明
*/
public class AutoRunOverTimeFlow extends Method
{
	/** 
	 不带有参数的方法
	*/
	public AutoRunOverTimeFlow()
	{
		this.Title = "处理逾期的任务";
		this.Help = "扫描并处理逾期的任务，按照节点配置的预期规则";
		this.GroupName = "流程自动执行定时任务";

	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 找到要逾期的数据.
		DataTable generTab = null;
		String sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE a.SDTOfNode<='" + DataType.getCurrentDataTime() + "' ";
		sql += " AND WFState=2 and b.OutTimeDeal!=0";
		sql += " AND a.FK_Node=b.NodeID";
		generTab = DBAccess.RunSQLReturnTable(sql);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 找到要逾期的数据.

		// 遍历循环,逾期表进行处理.
		String msg = "";
		String info = "";
		for (DataRow row : generTab.Rows)
		{
			String fk_flow = row.get("FK_Flow") + "";
			int fk_node = Integer.parseInt(row.get("FK_Node") + "");
			long workid = Long.parseLong(row.get("WorkID") + "");
			String title = row.get("Title") + "";
			String compleateTime = row.get("SDTOfNode") + "";
			String starter = row.get("Starter") + "";

			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, fk_node);

			boolean isLogin = false;
			for (GenerWorkerList item : gwls.ToJavaList())
			{
				if (item.getIsEnable() == false)
				{
					continue;
				}

				BP.Port.Emp emp = new Emp(item.getFK_Emp());
				WebUser.SignInOfGener(emp);
				isLogin = true;
			}

			if (isLogin == false)
			{
				BP.Port.Emp emp = new Emp("admin");
				WebUser.SignInOfGener(emp);
			}


			try
			{
				Node node = new Node(fk_node);
				if (node.getIsStartNode())
				{
					continue;
				}

				//获得该节点的处理内容.
				String doOutTime = node.GetValStrByKey(NodeAttr.DoOutTime);
				switch (node.getHisOutTimeDeal())
				{
					case None: //逾期不处理.
						continue;
					case AutoJumpToSpecNode: //跳转到指定的节点.
						try
						{
							//if (doOutTime.Contains(",") == false)
							//    throw new Exception("@系统设置错误，不符合设置规范,格式为: NodeID,EmpNo  现在设置的为:"+doOutTime);

							int jumpNode = Integer.parseInt(doOutTime);
							Node jumpToNode = new Node(jumpNode);

							//设置默认同意.
							BP.WF.Dev2Interface.WriteTrackWorkCheck(jumpToNode.getFK_Flow(), node.getNodeID(), workid, 0, "同意（预期自动审批）", null);

							//执行发送.
							info = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid, null, null, jumpToNode.getNodeID(), null).ToMsgOfText();

							// info = BP.WF.Dev2Interface.Flow_Schedule(workid, jumpToNode.NodeID, emp.No);
							msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'自动跳转'," + info;


							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);

						}
						catch (RuntimeException ex)
						{
							msg = "流程 '" + node.getFlowName() + "',WorkID=" + workid + ",标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'自动跳转',跳转异常:" + ex.getMessage();
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case AutoShiftToSpecUser: //走动移交给.
						// 判断当前的处理人是否是.
						Emp empShift = new Emp(doOutTime);
						try
						{
							BP.WF.Dev2Interface.Node_Shift(workid, empShift.getNo(), "流程节点已经逾期,系统自动移交");

							msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'移交到指定的人',已经自动移交给'" + empShift.getName() + ".";
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
						}
						catch (RuntimeException ex)
						{
							msg = "流程 '" + node.getFlowName() + "' ,标题:'" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'移交到指定的人',移交异常：" + ex.getMessage();
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
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
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'自动发送到下一节点',发送消息为:" + sendIfo;
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
						}
						catch (RuntimeException ex)
						{
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'自动发送到下一节点',发送异常:" + ex.getMessage();
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case DeleteFlow:
						info = BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow, workid, true);
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
							  "'超时处理规则为'删除流程'," + info;
						SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
						break;
					case RunSQL:
						try
						{
							BP.WF.Work wk = node.getHisWork();
							wk.setOID(workid);
							wk.Retrieve();

							doOutTime = BP.WF.Glo.DealExp(doOutTime, wk, null);

							//替换字符串
							doOutTime.replace("@OID", workid + "");
							doOutTime.replace("@FK_Flow", fk_flow);
							doOutTime.replace("@FK_Node", String.valueOf(fk_node));
							doOutTime.replace("@Starter", starter);
							if (doOutTime.contains("@"))
							{
								msg = "流程 '" + node.getFlowName() + "',标题:  '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
									  "'超时处理规则为'执行SQL'.有未替换的SQL变量.";
								SetText(msg);
								BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
								break;
							}

							//执行sql.
							DBAccess.RunSQL(doOutTime);
						}
						catch (RuntimeException ex)
						{
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'执行SQL'.运行SQL出现异常:" + ex.getMessage();
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					case SendMsgToSpecUser:
						try
						{
							Emp myemp = new Emp(doOutTime);

							boolean boo = BP.WF.Dev2Interface.WriteToSMS(myemp.getNo(), DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"), "系统发送逾期消息", "您的流程:'" + title + "'的完成时间应该为'" + compleateTime + "',流程已经逾期,请及时处理!", "系统消息");
							if (boo)
							{
								msg = "'" + title + "'逾期消息已经发送给:'" + myemp.getName() + "'";
							}
							else
							{
								msg = "'" + title + "'逾期消息发送未成功,发送人为:'" + myemp.getName() + "'";
							}
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
						}
						catch (RuntimeException ex)
						{
							msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
								  "'超时处理规则为'执行SQL'.运行SQL出现异常:" + ex.getMessage();
							SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
						}
						break;
					default:
						msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName() +
							  "'没有找到相应的超时处理规则.";
						SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
						break;
				}
			}
			catch (RuntimeException ex)
			{
				SetText("流程逾期出现异常:" + ex.getMessage());
				BP.DA.Log.DefaultLogWriteLine(LogType.Error, ex.toString());

			}
		}
		return generInfo;
	}

	public String generInfo = "";
	public final void SetText(String msg)
	{
		generInfo += "\t\n" + msg;
	}
}