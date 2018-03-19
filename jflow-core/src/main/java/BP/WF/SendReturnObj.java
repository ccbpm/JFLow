package BP.WF;

/** 
 工作发送返回对象
 
*/
public class SendReturnObj
{
	/** 
	 消息标记
	 
	*/
	public String MsgFlag = null;
	/** 
	 消息标记描述
	 
	*/
	public final String getMsgFlagDesc()
	{
		if (MsgFlag == null)
		{
			throw new RuntimeException("@没有标记");
		}


//		switch (MsgFlag)
//ORIGINAL LINE: case SendReturnMsgFlag.VarAcceptersID:
		if (MsgFlag.equals(SendReturnMsgFlag.VarAcceptersID))
		{
				return "接受人ID";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarAcceptersName:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarAcceptersName))
		{
				return "接受人名称";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarAcceptersNID:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarAcceptersNID))
		{
				return "接受人ID集合";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarCurrNodeID:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarCurrNodeID))
		{
				return "当前节点ID";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarCurrNodeName:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarCurrNodeName))
		{
				return "接受人集合的名称(用逗号分开)";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarToNodeID:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarToNodeID))
		{
				return "到达节点ID";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarToNodeName:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarToNodeName))
		{
				return "到达节点名称";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarTreadWorkIDs:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarTreadWorkIDs))
		{
				return "子线程的WorkIDs";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.BillInfo:
		else if (MsgFlag.equals(SendReturnMsgFlag.BillInfo))
		{
				return "单据信息";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.CCMsg:
		else if (MsgFlag.equals(SendReturnMsgFlag.CCMsg))
		{
				return "抄送信息";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.CondInfo:
		else if (MsgFlag.equals(SendReturnMsgFlag.CondInfo))
		{
				return "条件信息";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.CurrWorkOver:
		else if (MsgFlag.equals(SendReturnMsgFlag.CurrWorkOver))
		{
				return "当前的工作已经完成";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.EditAccepter:
		else if (MsgFlag.equals(SendReturnMsgFlag.EditAccepter))
		{
				return "编辑接受者";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.End:
		else if (MsgFlag.equals(SendReturnMsgFlag.End))
		{
				return "当前的流程已经结束";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.FenLiuInfo:
		else if (MsgFlag.equals(SendReturnMsgFlag.FenLiuInfo))
		{
				return "分流信息";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.FlowOver:
		else if (MsgFlag.equals(SendReturnMsgFlag.FlowOver))
		{
				return "当前流程已经完成";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.FlowOverByCond:
		else if (MsgFlag.equals(SendReturnMsgFlag.FlowOverByCond))
		{
				return "符合完成条件，流程完成.";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.HeLiuOver:
		else if (MsgFlag.equals(SendReturnMsgFlag.HeLiuOver))
		{
				return "分流完成";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.MacthFlowOver:
		else if (MsgFlag.equals(SendReturnMsgFlag.MacthFlowOver))
		{
				return "符合工作流程完成条件";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.NewFlowUnSend:
		else if (MsgFlag.equals(SendReturnMsgFlag.NewFlowUnSend))
		{
				return "新建流程";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.OverCurr:
		else if (MsgFlag.equals(SendReturnMsgFlag.OverCurr))
		{
				return "当前流程完成";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.Rpt:
		else if (MsgFlag.equals(SendReturnMsgFlag.Rpt))
		{
				return "报表";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.SendSuccessMsg:
		else if (MsgFlag.equals(SendReturnMsgFlag.SendSuccessMsg))
		{
				return "发送成功信息";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.SendSuccessMsgErr:
		else if (MsgFlag.equals(SendReturnMsgFlag.SendSuccessMsgErr))
		{
				return "发送错误";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.SendWhen:
		else if (MsgFlag.equals(SendReturnMsgFlag.SendWhen))
		{
				return "发送时";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.ToEmps:
		else if (MsgFlag.equals(SendReturnMsgFlag.ToEmps))
		{
				return "到达人员";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.UnSend:
		else if (MsgFlag.equals(SendReturnMsgFlag.UnSend))
		{
				return "撤消发送";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.ToEmpExt:
		else if (MsgFlag.equals(SendReturnMsgFlag.ToEmpExt))
		{
				return "到人员的扩展信息";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.VarWorkID:
		else if (MsgFlag.equals(SendReturnMsgFlag.VarWorkID))
		{
				return "工作ID";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.IsStopFlow:
		else if (MsgFlag.equals(SendReturnMsgFlag.IsStopFlow))
		{
				return "流程是否结束";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.WorkRpt:
		else if (MsgFlag.equals(SendReturnMsgFlag.WorkRpt))
		{
				return "工作报告";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.WorkStartNode:
		else if (MsgFlag.equals(SendReturnMsgFlag.WorkStartNode))
		{
				return "启动节点";
		}
//ORIGINAL LINE: case SendReturnMsgFlag.AllotTask:
		else if (MsgFlag.equals(SendReturnMsgFlag.AllotTask))
		{
				return "分配任务";
		}
		else
		{
				throw new RuntimeException("@没有判断的标记...");
		}
	}
	/** 
	 消息类型
	 
	*/
	public SendReturnMsgType HisSendReturnMsgType = SendReturnMsgType.Info;
	/** 
	 消息内容
	 
	*/
	public String MsgOfText = null;
	/** 
	 消息内容Html
	 
	*/
	public String MsgOfHtml = null;
	/** 
	 发送消息
	 
	*/
	public SendReturnObj()
	{
	}
}