package BP.DA;

/**
 * 数据库部署类型
 */
public enum DBModel
{
	/**
	 * 独立（集中模式）
	 */
	Single(0),
	/**
	 * 域模式
	 */
	Domain(1);
	
	private int intValue;
	private static java.util.HashMap<Integer, DBModel> mappings;
	
	private synchronized static java.util.HashMap<Integer, DBModel> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, DBModel>();
		}
		return mappings;
	}
	
	private DBModel(int value)
	{
		intValue = value;
		DBModel.getMappings().put(value, this);
	}
	
	public int getValue()
	{
		return intValue;
	}
	
	public static DBModel forValue(int value)
	{
		return getMappings().get(value);
	}
}