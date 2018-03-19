package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 父子流程控件状态
 
*/
public enum FrmSubFlowSta
{
	/** 
	 不可用
	 
	*/
	Disable,
	/** 
	 可用
	 
	*/
	Enable,
	/** 
	 只读
	 
	*/
	Readonly;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmSubFlowSta forValue(int value)
	{
		return values()[value];
	}
}