package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 多对多的类型
 
*/
public enum M2MType
{
	/** 
	 一对多
	 
	*/
	M2M,
	/** 
	 一对多对多
	 
	*/
	M2MM;

	public int getValue()
	{
		return this.ordinal();
	}

	public static M2MType forValue(int value)
	{
		return values()[value];
	}
}