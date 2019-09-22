package BP.WF;

import BP.WF.*;
import BP.Web.*;
import BP.En.*;
import BP.DA.*;

public enum WorkProgress
{
	/** 
	 正常运行
	*/
	Runing,
	/** 
	 预警
	*/
	Alert,
	/** 
	 逾期
	*/
	Timeout;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WorkProgress forValue(int value)
	{
		return values()[value];
	}
}