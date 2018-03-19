package BP.WF;

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
	 当前节点的所有未完成的子线程
	 
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
	ByExp;

	public int getValue()
	{
		return this.ordinal();
	}

	public static BlockModel forValue(int value)
	{
		return values()[value];
	}
}