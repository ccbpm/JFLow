package BP.WF.UnitTesting;

/** 
 数据库类型
*/
public enum EditState
{
	/** 
	 已经完成
	*/
	Passed(0),
	/** 
	 编辑中
	*/
	Editing(1),
	/** 
	 未完成
	*/
	UnOK(2);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, EditState> mappings;
	private static java.util.HashMap<Integer, EditState> getMappings()
	{
		if (mappings == null)
		{
			synchronized (EditState.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, EditState>();
				}
			}
		}
		return mappings;
	}

	private EditState(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static EditState forValue(int value)
	{
		return getMappings().get(value);
	}
}