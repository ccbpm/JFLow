package BP.Sys;

import BP.DA.*;
import BP.En.*;

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

	public int getValue()
	{
		return this.ordinal();
	}

	public static DTSearchWay forValue(int value)
	{
		return values()[value];
	}
}