package bp.sys;

import bp.*;

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

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlSaveModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}