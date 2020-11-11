package bp.wf;

import bp.wf.*;
import bp.web.*;
import bp.en.*;
import bp.da.*;

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

	public static WorkProgress forValue(int value) throws Exception
	{
		return values()[value];
	}
}