package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.port.*;
import bp.*;
import java.util.*;

/** 
 消息状态
*/
public enum MsgSta
{
	/** 
	 未开始
	*/
	UnRun,
	/** 
	 成功
	*/
	RunOK,
	/** 
	 失败
	*/
	RunError,
	/** 
	 禁止发送
	*/
	Disable;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static MsgSta forValue(int value)
	{return values()[value];
	}
}