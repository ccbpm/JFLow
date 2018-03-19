package BP.Sys;

import BP.DA.*;
import BP.En.*;

public enum DtlSaveModel
{
	/** 
	 失去焦点自动存盘
	 
	*/
	AutoSave,
	/** 
	 由保存按钮触发存盘
	 
	*/
	HandSave;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlSaveModel forValue(int value)
	{
		return values()[value];
	}
}