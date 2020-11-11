package bp.wf;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.template.*;
import java.util.*;

/** 
 任务状态
*/
public enum TaskSta
{
	/** 
	 无
	*/
	None,
	/** 
	 共享
	*/
	Sharing,
	/** 
	 已经取走
	*/
	Takeback;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TaskSta forValue(int value) throws Exception
	{
		return values()[value];
	}
}