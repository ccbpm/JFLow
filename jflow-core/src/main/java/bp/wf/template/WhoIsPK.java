package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 谁是主键？
*/
public enum WhoIsPK
{
	/** 
	 流程ID是主键 
	*/
	OID,
	/** 
	  FID是主键(干流程的WorkID)
	*/
	FID,
	/** 
	 父流程ID是主键
	*/
	PWorkID,
	/** 
	 延续流程ID是主键
	*/
	CWorkID,
	/** 
	 爷爷流程ID是主键
	*/
	P2WorkID,
	/** 
	 太爷爷流程ID是主键
	*/
	P3WorkID;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WhoIsPK forValue(int value) throws Exception
	{
		return values()[value];
	}
}