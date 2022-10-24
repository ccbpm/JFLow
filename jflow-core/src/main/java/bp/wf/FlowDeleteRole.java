package bp.wf;

import bp.*;

/** 
 流程删除规则
*/
public enum FlowDeleteRole
{
	/** 
	 超级管理员可以删除
	*/
	AdminOnly,
	/** 
	 分级管理员可以删除
	*/
	AdminAppOnly,
	/** 
	 发起人可以删除
	*/
	ByMyStarter,
	/** 
	 节点启动删除按钮的操作员
	*/
	ByNodeSetting;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static FlowDeleteRole forValue(int value)
	{return values()[value];
	}
}