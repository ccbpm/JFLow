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
	 P2WorkID,
	*/
	P2WorkID,
	/** 
	 P3WorkID
	*/
	P3WorkID,
	/**
	 * 根流程的WORKID
	 */
	RootFlowWorkID;

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