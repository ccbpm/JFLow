package bp.wf;

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
	SelectSQLModel,
	/** 
	 触发式启动
	*/
	WF_TaskTableInsertModel;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowRunWay forValue(int value) throws Exception
	{
		return values()[value];
	}
}