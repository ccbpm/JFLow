package BP.Sys;

/** 
数据提取类型

*/
public enum ExcelType
{
	/** 
	 普通文件数据提取
	 
	*/
	NormalFile(0),
	/** 
	 流程附件数据提取
	 
	*/
	FlowAttachment(1);

	private int intValue;
	private static java.util.HashMap<Integer, ExcelType> mappings;
	private synchronized static java.util.HashMap<Integer, ExcelType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, ExcelType>();
		}
		return mappings;
	}

	private ExcelType(int value)
	{
		intValue = value;
		ExcelType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static ExcelType forValue(int value)
	{
		return getMappings().get(value);
	}
}
