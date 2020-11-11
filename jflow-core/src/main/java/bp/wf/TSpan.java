package bp.wf;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.template.*;
import java.util.*;

/** 
 时间段
*/
public enum TSpan
{
	/** 
	 本周
	*/
	ThisWeek,
	/** 
	 上周
	*/
	NextWeek,
	/** 
	 上上周
	*/
	TowWeekAgo,
	/** 
	 更早
	*/
	More;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TSpan forValue(int value) throws Exception
	{
		return values()[value];
	}
}