package bp.wf;

import bp.*;

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
	 累加表单
	*/
	FoolTruck(10),
	/** 
	 节点单表单
	*/
	RefOneFrmTree(11),
	/** 
	 开发者表单
	*/
	Develop(12),
	/** 
	 开发者表单
	*/
	ChapterFrm(13),
	/** 
	 禁用(对多表单流程有效)
	*/
	DisableIt(100);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, NodeFormType> mappings;
	private static java.util.HashMap<Integer, NodeFormType> getMappings()  {
		if (mappings == null)
		{
			synchronized (NodeFormType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, NodeFormType>();
				}
			}
		}
		return mappings;
	}

	private NodeFormType(int value)
	{intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()  {
		return intValue;
	}

	public static NodeFormType forValue(int value)
	{return getMappings().get(value);
	}
}