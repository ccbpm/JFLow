package bp.sys;

import bp.*;

/** 
 按日期查询方式
*/
public enum DTSearchWay
{
	/** 
	 不设置
	*/
	None,
	/** 
	 按日期
	*/
	ByDate,
	/** 
	 按日期时间
	*/
	ByDateTime;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DTSearchWay forValue(int value) throws Exception
	{
		return values()[value];
	}
}