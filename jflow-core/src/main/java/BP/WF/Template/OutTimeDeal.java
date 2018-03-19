package BP.WF.Template;


/** 
 待办工作超时处理方式
 
*/
public enum OutTimeDeal
{
	/** 
	 不处理
	 
	*/
	None,
	/** 
	 自动的转向下一步骤
	 
	*/
	AutoTurntoNextStep,
	/** 
	 自动跳转到指定的点
	 
	*/
	AutoJumpToSpecNode,
	/** 
	 自动移交到指定的人员
	 
	*/
	AutoShiftToSpecUser,
	/** 
	 向指定的人员发送消息
	 
	*/
	SendMsgToSpecUser,
	/** 
	 删除流程
	 
	*/
	DeleteFlow,
	/** 
	 执行SQL
	 
	*/
	RunSQL;
	///// <summary>
	///// 到达指定的日期，仍未处理，自动向下发送.
	///// </summary>
	//WhenToSpecDataAutoSend

	public int getValue()
	{
		return this.ordinal();
	}

	public static OutTimeDeal forValue(int value)
	{
		return values()[value];
	}
}