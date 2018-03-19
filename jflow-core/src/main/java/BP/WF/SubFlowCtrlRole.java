package BP.WF;

/** 
 子流程控制方式
*/
public enum SubFlowCtrlRole
{
	/** 
	 不显示
	*/
	None,
	/** 
	 可以删除子流程
	*/
	CanDel,
	/** 
	 不可以删除
	*/
	NotCanDel;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SubFlowCtrlRole forValue(int value)
	{
		return values()[value];
	}
}