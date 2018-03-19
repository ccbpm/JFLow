package BP.Sys;

import BP.DA.*;
import BP.En.*;

public enum DtlOpenType
{
	/** 
	 对人员开放
	 
	*/
	ForEmp,
	/** 
	 对工作开放
	 
	*/
	ForWorkID,
	/** 
	 对流程开放
	 
	*/
	ForFID;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlOpenType forValue(int value)
	{
		return values()[value];
	}
}