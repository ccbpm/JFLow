package BP.WF;

/** 
 流程启动类型
 
*/
public enum FlowRunWay
{
	/** 
	 手工启动
	 
	*/
	HandWork,
	/** 
	 指定人员按时启动
	 
	*/
	SpecEmp,
	/** 
	 数据集按时启动
	 
	*/
	DataModel,
	/** 
	 触发式启动
	 
	*/
	InsertModel;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowRunWay forValue(int value)
	{
		return values()[value];
	}
}