package bp.sys;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.difference.*;
import bp.*;
import java.util.*;

/** 
 选择模式
*/
public enum PopValSelectModel
{
	/** 
	 单选
	*/
	One,
	/** 
	 多选
	*/
	More;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static PopValSelectModel forValue(int value)
	{return values()[value];
	}
}