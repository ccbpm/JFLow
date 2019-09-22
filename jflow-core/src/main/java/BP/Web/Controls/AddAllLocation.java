package BP.Web.Controls;

import BP.Web.*;

/** 
 AddAllLocation
*/
public enum AddAllLocation
{
	/** 
	 显示上方
	*/
	Top,
	/** 
	 显示下方
	*/
	End,
	/** 
	 显示上方和下方
	*/
	TopAndEnd,
	/** 
	 不显示
	*/
	None,
	/** 
	 多选
	*/
	TopAndEndWithMVal;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AddAllLocation forValue(int value)
	{
		return values()[value];
	}
}