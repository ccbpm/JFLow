package bp.wf;

import bp.*;

/** 
 抄送类型
*/
public enum CCType
{
	/** 
	 不抄送
	*/
	None,
	/** 
	 按人员
	*/
	AsEmps,
	/** 
	 按岗位
	*/
	AsStation,
	/** 
	 按节点
	*/
	AsNode,
	/** 
	 按部门
	*/
	AsDept,
	/** 
	 按照部门与岗位
	*/
	AsDeptAndStation;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static CCType forValue(int value) 
	{return values()[value];
	}
}