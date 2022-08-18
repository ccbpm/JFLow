package bp.wf;

import bp.*;

/** 
 抄送规则
*/
public enum CCRole
{
	/** 
	 不能抄送
	*/
	UnCC,
	/** 
	 手工抄送
	*/
	HandCC,
	/** 
	 自动抄送
	*/
	AutoCC,
	/** 
	 手工与自动并存
	*/
	HandAndAuto,
	/** 
	 按字段
	*/
	BySysCCEmps,
	/** 
	 在发送前打开
	*/
	WhenSend;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static CCRole forValue(int value)
	{return values()[value];
	}
}