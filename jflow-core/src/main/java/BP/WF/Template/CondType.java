package BP.WF.Template;


/** 
 条件类型
 
*/
public enum CondType
{
	/** 
	 节点完成条件
	 
	*/
	Node,
	/** 
	 流程条件
	 
	*/
	Flow,
	/** 
	 方向条件
	 
	*/
	Dir,
	/** 
	 启动子流程
	 
	*/
	SubFlow;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CondType forValue(int value)
	{
		return values()[value];
	}
}