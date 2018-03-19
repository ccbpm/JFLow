package BP.WF;

/** 
 表单运行类型
*/
public enum FormRunType
{
	/** 
	 傻瓜表单.
	*/
	FixForm(0),
	/** 
	 自由表单.
	*/
	FreeForm(1),
	/** 
	 SL表单
	*/
	SLFrom(2),
	/** 
	 嵌入式表单.
	*/
	SelfForm(3),
	/** 
	 excel表单
	*/
	ExclForm(4),
	/** 
	 word表单
	*/
	WordForm(5),
	/** 
	 动态表单树
	*/
	SheetAutoTree(6),
	/** 
	 公文表单
	*/
	WebOffice(7);

	private int intValue;
	private static java.util.HashMap<Integer, FormRunType> mappings;
	private static java.util.HashMap<Integer, FormRunType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (FormRunType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, FormRunType>();
				}
			}
		}
		return mappings;
	}

	private FormRunType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static FormRunType forValue(int value)
	{
		return getMappings().get(value);
	}
}