package BP.WF;

/** 
 方向条件控制规则
*/
public enum CondModel
{
	/** 
	 按照用户设置的方向条件计算
	*/
	ByLineCond,
	/** 
	 按照用户选择计算
	*/
	ByUserSelected,
	/** 
	 发送按钮旁下拉框选择
	*/
	SendButtonSileSelect;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CondModel forValue(int value)
	{
		return values()[value];
	}
}