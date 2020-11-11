package bp.sys;

import bp.*;

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
	ForFID,
	/** 
	 父工作ID
	*/
	ForPWorkID,

	ForP2WorkID,

	ForP3WorkID;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlOpenType forValue(int value) throws Exception
	{
		return values()[value];
	}
}