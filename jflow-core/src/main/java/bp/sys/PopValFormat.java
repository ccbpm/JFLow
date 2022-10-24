package bp.sys;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.difference.*;
import bp.*;
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

	public int getValue() {
		return this.ordinal();
	}

	public static PopValFormat forValue(int value)
	{return values()[value];
	}
}