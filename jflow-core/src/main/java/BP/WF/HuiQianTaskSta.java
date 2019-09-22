package BP.WF;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Template.*;
import java.util.*;

/** 
 会签任务状态
*/
public enum HuiQianTaskSta
{
	/** 
	 无
	*/
	None,
	/** 
	 会签中
	*/
	HuiQianing,
	/** 
	 会签完成
	*/
	HuiQianOver;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static HuiQianTaskSta forValue(int value)
	{
		return values()[value];
	}
}