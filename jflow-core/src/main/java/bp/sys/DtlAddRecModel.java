package bp.sys;

import bp.*;

/** 
 明细表存盘方式
*/
public enum DtlAddRecModel
{
	/** 
	 自动初始化空白行
	*/
	ByBlank,
	/** 
	 用按钮增加行
	*/
	ByButton;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlAddRecModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}