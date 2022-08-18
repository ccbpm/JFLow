package bp.wf;

import bp.*;

/** 
 子线程启动方式
*/
public enum SubFlowStartWay
{
	/** 
	 不启动
	*/
	None,
	/** 
	 按表单字段
	*/
	BySheetField,
	/** 
	 按从表数据
	*/
	BySheetDtlTable;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static SubFlowStartWay forValue(int value)
	{return values()[value];
	}
}