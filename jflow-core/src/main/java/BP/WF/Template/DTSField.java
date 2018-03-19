package BP.WF.Template;

/** 
 要同步的字段计算方式
 
*/
public enum DTSField
{
	/** 
	 字段名相同
	 
	*/
	SameNames,
	/** 
	 设置的字段匹配
	 
	*/
	SpecField,
	/** 
	 以上两者都使用
	 
	*/
	Above;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DTSField forValue(int value)
	{
		return values()[value];
	}
}