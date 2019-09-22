package BP.WF;

/** 
 挂起方式
*/
public enum HungUpWay
{
	/** 
	 永久挂起
	*/
	Forever,
	/** 
	 在指定的日期解除
	*/
	SpecDataRel;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static HungUpWay forValue(int value)
	{
		return values()[value];
	}
}