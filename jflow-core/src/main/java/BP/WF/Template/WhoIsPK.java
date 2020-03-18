package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 谁是主键？
*/
public enum WhoIsPK
{
	/** 
	 工作ID是主键
	*/
	OID,
	/** 
	 流程ID是主键
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

	public static WhoIsPK forValue(int value)
	{
		return values()[value];
	}
}