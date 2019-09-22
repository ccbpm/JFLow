package BP.Sys;

import BP.En.*;
import java.util.*;

public enum ExcelFieldDataType
{
	String(0),
	Int(1),
	Float(2),
	Date(3),
	DateTime(4),
	ForeignKey(5),
	Enum(6);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, ExcelFieldDataType> mappings;
	private static java.util.HashMap<Integer, ExcelFieldDataType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (ExcelFieldDataType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, ExcelFieldDataType>();
				}
			}
		}
		return mappings;
	}

	private ExcelFieldDataType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static ExcelFieldDataType forValue(int value)
	{
		return getMappings().get(value);
	}
}