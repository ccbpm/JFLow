package BP.WF.Template;


/** 
 关系类型
 
*/
public enum CondOrAnd
{
	/** 
	 关系集合里面的所有条件都成立.
	 
	*/
	ByAnd,
	/** 
	 关系集合里的只有一个条件成立.
	 
	*/
	ByOr;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CondOrAnd forValue(int value)
	{
		return values()[value];
	}
}