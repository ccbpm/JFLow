package BP.WF.Template;

public enum DayOfWeek
{
	// 摘要:
	//     表示星期日。
	Sunday(0),
	//
	// 摘要:
	//     表示星期一。
	Monday(1),
	//
	// 摘要:
	//     表示星期二。
	Tuesday(2),
	//
	// 摘要:
	//     表示星期三。
	Wednesday(3),
	//
	// 摘要:
	//     表示星期四。
	Thursday(4),
	//
	// 摘要:
	//     表示星期五。
	Friday(5),
	//
	// 摘要:
	//     表示星期六。
	Saturday(6);

	private int intValue;
	private static java.util.HashMap<Integer, DayOfWeek> mappings;
	private synchronized static java.util.HashMap<Integer, DayOfWeek> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, DayOfWeek>();
		}
		return mappings;
	}

	private DayOfWeek(int value)
	{
		intValue = value;
		DayOfWeek.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static DayOfWeek forValue(int value)
	{
		return getMappings().get(value);
	}
}