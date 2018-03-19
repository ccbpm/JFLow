package BP.WF;

/** 
 节点表单类型
 
*/
public enum NodeFormType
{
	/** 
	 傻瓜表单.
	 
	*/
	FoolForm(0),
	/** 
	 自由表单.
	 
	*/
	FreeForm(1),
	/** 
	 嵌入式表单.
	 
	*/
	SelfForm(2),
	/** 
	 SDKForm
	 
	*/
	SDKForm(3),
	/** 
	 SL表单
	 
	*/
	SLForm(4),
	/** 
	 表单树
	 
	*/
	SheetTree(5),
	/** 
	 动态表单树
	 
	*/
	SheetAutoTree(6),
	/** 
	 公文表单
	 
	*/
	WebOffice(7),
	/** 
	 Excel表单
	 
	*/
	ExcelForm(8),
	/** 
	 Word表单
	 
	*/
	WordForm(9),
	/**
	 * 
	 */
	FoolTruck(10),
	
	/**
	 * 独立流程表单
	 */
	RefOneFrmTree(11),
	
	/** 
	 禁用(对多表单流程有效)
	 
	*/
	DisableIt(100);

	private int intValue;
	private static java.util.HashMap<Integer, NodeFormType> mappings;
	private synchronized static java.util.HashMap<Integer, NodeFormType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, NodeFormType>();
		}
		return mappings;
	}

	private NodeFormType(int value)
	{
		intValue = value;
		NodeFormType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static NodeFormType forValue(int value)
	{
		return getMappings().get(value);
	}
}