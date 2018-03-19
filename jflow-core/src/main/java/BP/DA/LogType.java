package BP.DA;

// enum LogType
/**
 * 信息类型
 */
public enum LogType
{
	/**
	 * 调试
	 */
	Debug(1),
	/**
	 * 提示
	 */
	Info(1),
	/**
	 * 警告
	 */
	Warning(2),
	/**
	 * 错误
	 */
	Error(3);
	
	private int intValue;
	private static java.util.HashMap<Integer, LogType> mappings;
	
	private synchronized static java.util.HashMap<Integer, LogType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, LogType>();
		}
		return mappings;
	}
	
	private LogType(int value)
	{
		intValue = value;
		LogType.getMappings().put(value, this);
	}
	
	public int getValue()
	{
		return intValue;
	}
	
	public static LogType forValue(int value)
	{
		return getMappings().get(value);
	}
}