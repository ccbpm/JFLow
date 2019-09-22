package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 子线程组件控件状态
*/
public enum FrmThreadSta
{
	/** 
	 不可用
	*/
	Disable,
	/** 
	 启用
	*/
	Enable;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmThreadSta forValue(int value)
	{
		return values()[value];
	}
}