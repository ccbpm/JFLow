package bp.wf.port;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.port.*;
import bp.web.*;
import bp.wf.*;
import java.util.*;
import java.time.*;

public enum AlertWay
{
	/** 
	 不提示
	*/
	None,
	/** 
	 手机短信
	*/
	SMS,
	/** 
	 邮件
	*/
	Email,
	/** 
	 手机短信+邮件
	*/
	SMSAndEmail,
	/** 
	 内部消息
	*/
	AppSystemMsg;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AlertWay forValue(int value) throws Exception
	{
		return values()[value];
	}
}