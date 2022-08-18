package bp.wf.dts;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.web.*;
import bp.wf.*;

/** 
 处理延期的任务 的摘要说明
*/
public class DTS_DealDeferredWork extends Method
{
	/** 
	 处理延期的任务
	*/
	public DTS_DealDeferredWork()throws Exception
	{
		this.Title = "处理逾期的任务";
		this.Help = "需要每天执行一次,对于已经逾期的工作,按照逾期的规则处理。";
		this.GroupName = "流程自动执行定时任务";

	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
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
	*/
	@Override
	public Object Do()throws Exception
	{
		String sql = "SELECT * FROM WF_EmpWorks WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE OutTimeDeal >0 ) AND SDT <'" + DataType.getCurrentDate() + "' ORDER BY FK_Emp";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		String msg = "";
		String dealWorkIDs = "";
		for (DataRow dr : dt.Rows)
		{

			String FK_Emp = dr.getValue("FK_Emp").toString();
			String fk_flow = dr.getValue("FK_Flow").toString();
			int fk_node = Integer.parseInt(dr.getValue("FK_Node").toString());
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			long fid = Long.parseLong(dr.getValue("FID").toString());

			// 方式两个人同时处理一件工作, 一个人处理后，另外一个人还可以处理的情况.
			if (dealWorkIDs.contains("," + workid + ","))
			{
				continue;
			}
			dealWorkIDs += "," + workid + ",";

			if (!WebUser.getNo().equals(FK_Emp))
			{
				Emp emp = new Emp(FK_Emp);
				WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			}

			Node nd = new Node();
			nd.setNodeID(fk_node);
			nd.Retrieve();

			// 首先判断是否有启动的表达式, 它是是否自动执行的总阀门。
			if (DataType.IsNullOrEmpty(nd.getDoOutTimeCond()) == false)
			{
				Node nodeN = new Node(nd.getNodeID());
				Work wk = nodeN.getHisWork();
				wk.setOID(workid);
				wk.Retrieve();
				Object tempVar = nd.getDoOutTimeCond();
				String exp = tempVar instanceof String ? (String)tempVar : null;
				if (Glo.ExeExp(exp, wk) == false)
				{
					continue; // 不能通过条件的设置.
				}
			}

			switch (nd.getHisOutTimeDeal())
			{
				case None:
					break;
				case AutoTurntoNextStep: //自动转到下一步骤.
					if (DataType.IsNullOrEmpty(nd.getDoOutTime()))
					{
						/*如果是空的,没有特定的点允许，就让其它向下执行。*/
						msg += Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText();
					}
					else
					{
						int nextNode = Dev2Interface.Node_GetNextStepNode(fk_flow, workid);
						if (nd.getDoOutTime().contains(String.valueOf(nextNode))) //如果包含了当前点的ID,就让它执行下去.
						{
							msg += Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText();
						}
					}
					break;
				case AutoJumpToSpecNode: //自动的跳转下一个节点.
					if (DataType.IsNullOrEmpty(nd.getDoOutTime()))
					{
						throw new RuntimeException("@设置错误,没有设置要跳转的下一步节点.");
					}
					int nextNodeID = Integer.parseInt(nd.getDoOutTime());
					msg += Dev2Interface.Node_SendWork(fk_flow, workid, null, null, nextNodeID, null).ToMsgOfText();
					break;
				case AutoShiftToSpecUser: //移交给指定的人员.
					msg += Dev2Interface.Node_Shift(workid, nd.getDoOutTime(), "来自ccflow的自动消息:(" + WebUser.getName() + ")工作未按时处理(" + nd.getName() + "),现在移交给您。");
					break;
				case SendMsgToSpecUser: //向指定的人员发消息.
					Dev2Interface.Port_SendMsg(nd.getDoOutTime(), "来自ccflow的自动消息:(" + WebUser.getName() + ")工作未按时处理(" + nd.getName() + ")", "感谢您选择ccflow.", "SpecEmp" + workid);
					break;
				case DeleteFlow: //删除流程.
					msg += Dev2Interface.Flow_DoDeleteFlowByReal(workid, true);
					break;
				case RunSQL:
					msg += DBAccess.RunSQL(nd.getDoOutTime());
					break;
				default:
					throw new RuntimeException("@错误没有判断的超时处理方式." + nd.getHisOutTimeDeal());
			}
		}
		Emp emp1 = new Emp("admin");
		WebUser.SignInOfGener(emp1, "CH", false, false, null, null);
		return msg;

	}
}