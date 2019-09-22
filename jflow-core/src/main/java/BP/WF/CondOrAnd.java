package BP.WF;

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

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CondOrAnd forValue(int value)
	{
		return values()[value];
	}
}