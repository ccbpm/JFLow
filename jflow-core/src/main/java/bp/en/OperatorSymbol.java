package bp.en;

import bp.en.*;
import bp.web.*;
import bp.*;

public enum OperatorSymbol
{
	/** 
	 大于
	*/
	DaYu,
	/** 
	 等于
	*/
	DengYu,
	/** 
	 小于
	*/
	XiaoYu,
	/** 
	 相似
	*/
	Like;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static OperatorSymbol forValue(int value) throws Exception
	{
		return values()[value];
	}
}