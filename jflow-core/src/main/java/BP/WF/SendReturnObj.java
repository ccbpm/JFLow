package BP.WF;

/**
 * 工作发送返回对象
 * 
 */
public class SendReturnObj {
	/**
	 * 消息标记
	 * 
	 */
	public String MsgFlag = null;

	/**
	 * 消息标记描述
	 * 
	 */
	public final String getMsgFlagDesc() {
		if (MsgFlag == null) {
			throw new RuntimeException("@没有标记");
		}

		if (MsgFlag.equals(SendReturnMsgFlag.VarAcceptersID)) {
			return "接受人ID";
		}

		else if (MsgFlag.equals(SendReturnMsgFlag.VarAcceptersName)) {
			return "接受人名称";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.VarAcceptersNID)) {
			return "接受人ID集合";
		}

		else if (MsgFlag.equals(SendReturnMsgFlag.VarCurrNodeID)) {
			return "当前节点ID";
		}

		else if (MsgFlag.equals(SendReturnMsgFlag.VarCurrNodeName)) {
			return "接受人集合的名称(用逗号分开)";
		}

		else if (MsgFlag.equals(SendReturnMsgFlag.VarToNodeID)) {
			return "到达节点ID";
		}

		else if (MsgFlag.equals(SendReturnMsgFlag.VarToNodeName)) {
			return "到达节点名称";
		}

		else if (MsgFlag.equals(SendReturnMsgFlag.VarTreadWorkIDs)) {
			return "子线程的WorkIDs";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.BillInfo)) {
			return "单据信息";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.CCMsg)) {
			return "抄送信息";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.CondInfo)) {
			return "条件信息";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.CurrWorkOver)) {
			return "当前的工作已经完成";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.EditAccepter)) {
			return "编辑接受者";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.End)) {
			return "当前的流程已经结束";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.FenLiuInfo)) {
			return "分流信息";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.FlowOver)) {
			return "当前流程已经完成";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.FlowOverByCond)) {
			return "符合完成条件，流程完成.";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.HeLiuOver)) {
			return "分流完成";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.MacthFlowOver)) {
			return "符合工作流程完成条件";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.NewFlowUnSend)) {
			return "新建流程";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.OverCurr)) {
			return "当前流程完成";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.Rpt)) {
			return "报表";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.SendSuccessMsg)) {
			return "发送成功信息";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.SendSuccessMsgErr)) {
			return "发送错误";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.SendWhen)) {
			return "发送时";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.ToEmps)) {
			return "到达人员";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.UnSend)) {
			return "撤消发送";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.ToEmpExt)) {
			return "到人员的扩展信息";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.VarWorkID)) {
			return "工作ID";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.IsStopFlow)) {
			return "流程是否结束";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.WorkRpt)) {
			return "工作报告";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.WorkStartNode)) {
			return "启动节点";
		}
		else if (MsgFlag.equals(SendReturnMsgFlag.AllotTask)) {
			return "分配任务";
		} else {
			return "信息";
		}
	}

	/**
	 * 消息类型
	 * 
	 */
	public SendReturnMsgType HisSendReturnMsgType = SendReturnMsgType.Info;
	/**
	 * 消息内容
	 * 
	 */
	public String MsgOfText = null;
	/**
	 * 消息内容Html
	 * 
	 */
	public String MsgOfHtml = null;

	/**
	 * 发送消息
	 * 
	 */
	public SendReturnObj() {
	}
}