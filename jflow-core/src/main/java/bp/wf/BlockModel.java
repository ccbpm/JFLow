package bp.wf;

/** 
 阻塞模式
*/
public enum BlockModel
{
	/** 
	 不阻塞
	*/
	None,
	/** 
	 当前节点的有未完成的子线程
	*/
	CurrNodeAll,
	/** 
	 按照约定的格式阻塞.
	*/
	SpecSubFlow,
	/** 
	 按照配置的sql阻塞,返回大于等于1表示阻塞,否则不阻塞.
	*/
	BySQL,
	/** 
	 按照表达式阻塞，表达式类似方向条件的表达式.
	*/
	ByExp,
	/** 
	 为父流程时，指定的子流程未运行到指定节点，则阻塞
	*/
	SpecSubFlowNode,
	/** 
	 为平级子流程时，指定的子流程未运行到指定节点，则阻塞
	*/
	SameLevelSubFlow;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static BlockModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}