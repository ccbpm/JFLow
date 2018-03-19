package BP.WF.Template;

/** 
 草稿规则
 
*/
public enum DraftRole
{
	/** 
	 不设置草稿
	 
	*/
	None,
	/** 
	 保存到待办
	 
	*/
	SaveToTodolist,
	/** 
	 保存到草稿箱
	 
	*/
	SaveToDraftList;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DraftRole forValue(int value)
	{
		return values()[value];
	}
}