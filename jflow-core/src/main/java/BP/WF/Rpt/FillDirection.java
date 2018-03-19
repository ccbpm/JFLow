package BP.WF.Rpt;

public enum FillDirection
{
	Vertical(1),

	Horizontal(2);

	private int intValue;
	private static java.util.HashMap<Integer, FillDirection> mappings;
	private synchronized static java.util.HashMap<Integer, FillDirection> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, FillDirection>();
		}
		return mappings;
	}

	private FillDirection(int value)
	{
		intValue = value;
		FillDirection.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static FillDirection forValue(int value)
	{
		return getMappings().get(value);
	}
}