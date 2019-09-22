package BP.Sys;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 Pop返回值类型
*/
public enum PopValFormat
{
	/** 
	 编号
	*/
	OnlyNo,
	/** 
	 名称
	*/
	OnlyName,
	/** 
	 编号与名称
	*/
	NoName;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PopValFormat forValue(int value)
	{
		return values()[value];
	}
}