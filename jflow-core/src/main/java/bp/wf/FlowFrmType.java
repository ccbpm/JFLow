package bp.wf;

/** 
 流程表单类型
*/
public enum FlowFrmType
{
	/** 
	 完整版-2019年更早版本
	*/
	Ver2019Earlier(0),
	/** 
	 开发者表单
	*/
	DeveloperFrm(1),
	/** 
	 傻瓜表单
	*/
	FoolFrm(2),
	/** 
	 自定义表单
	*/
	SelfFrm(3),
	/** 
	 SDK表单
	*/
	SDKFrm(4);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, FlowFrmType> mappings;
	private static java.util.HashMap<Integer, FlowFrmType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (FlowFrmType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, FlowFrmType>();
				}
			}
		}
		return mappings;
	}

	private FlowFrmType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static FlowFrmType forValue(int value) 
	{
		return getMappings().get(value);
	}
}