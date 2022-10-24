package bp.ccbill;


/** 
 单据状态
*/
public enum BillState
{
	/** 
	 空白
	*/
	None(0),
	/** 
	 草稿
	*/
	Draft(1),
	/** 
	 编辑中
	*/
	Editing(2),
	/** 
	 归档
	*/
	Over(100);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, BillState> mappings;
	private static java.util.HashMap<Integer, BillState> getMappings()  {
		if (mappings == null)
		{
			synchronized (BillState.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, BillState>();
				}
			}
		}
		return mappings;
	}

	private BillState(int value)
	{intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static BillState forValue(int value)
	{return getMappings().get(value);
	}
}