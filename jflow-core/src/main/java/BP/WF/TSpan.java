package BP.WF;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Template.*;
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

	public static TSpan forValue(int value)
	{
		return values()[value];
	}
}