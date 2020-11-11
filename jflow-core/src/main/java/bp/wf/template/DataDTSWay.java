package bp.wf.template;

import bp.wf.*;

/** 
 数据同步方案
*/
public enum DataDTSWay
{
	/** 
	 不同步
	*/
	None,
	/** 
	 同步
	*/
	Syn;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DataDTSWay forValue(int value) throws Exception
	{
		return values()[value];
	}
}