package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 轨迹图标组件控件状态
*/
public enum FTCSta
{
	/** 
	 不可用
	*/
	Disable,
	/** 
	 只读
	*/
	ReadOnly,
	/** 
	 可以设置人员
	*/
	SetWorkers;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FTCSta forValue(int value)
	{
		return values()[value];
	}
}