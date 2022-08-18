package bp.wf;

import bp.en.*;
import bp.web.*;
import bp.da.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.data.*;
import bp.difference.*;
import bp.*;
import java.math.*;

public enum HungupSta
{
	/** 
	 申请
	*/
	Apply,
	/** 
	 同意
	*/
	Agree,
	/** 
	 拒绝
	*/
	Reject;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static HungupSta forValue(int value) 
	{return values()[value];
	}
}