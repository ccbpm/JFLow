package BP.Sys;


/** 
 数据源类型
*/
public enum CCBPMRunModel
{
	/**
	 单机版
	*/
	Single(0),
	/**
	 集团模式
	*/
	GroupInc(1),
	/**
	 多租户模式
	*/
	SAAS(2);
	public static final int SIZE = Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, CCBPMRunModel> mappings;
	private static java.util.HashMap<Integer, CCBPMRunModel> getMappings()
	{
		if (mappings == null)
		{
			synchronized (CCBPMRunModel.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, CCBPMRunModel>();
				}
			}
		}
		return mappings;
	}

	private CCBPMRunModel(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static CCBPMRunModel forValue(int value)
	{
		return getMappings().get(value);
	}
}