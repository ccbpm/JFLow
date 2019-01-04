package BP.Sys;

public enum EditModel {

	/**
	 * 表格模式
	 */
	TableModel(0),
	/**
	 * 傻瓜表单模式
	 */
	FoolModel(1),
	
	/**
	 * 自由表单模式
	 */
	FreeModel(2);
	
	private int intValue;
	private static java.util.HashMap<Integer, EditModel> mappings;
	private synchronized static java.util.HashMap<Integer, EditModel> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, EditModel>();
		}
		return mappings;
	}

	private EditModel(int value)
	{
		intValue = value;
		EditModel.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static EditModel forValue(int value)
	{
		return getMappings().get(value);
	}
}
