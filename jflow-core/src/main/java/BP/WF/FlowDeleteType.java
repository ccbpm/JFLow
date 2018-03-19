package BP.WF;

public enum FlowDeleteType {
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

	
	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowDeleteType forValue(int value)
	{
		return values()[value];
	}
	

}
