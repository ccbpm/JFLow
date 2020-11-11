package bp.wf;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.template.*;
import java.util.*;

/** 
 会签任务状态
*/
public enum HuiQianTaskSta
{
	/** 
	 无
	*/
	None,
	/** 
	 会签中
	*/
	HuiQianing,
	/** 
	 会签完成
	*/
	HuiQianOver;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static HuiQianTaskSta forValue(int value) throws Exception
	{
		return values()[value];
	}
}