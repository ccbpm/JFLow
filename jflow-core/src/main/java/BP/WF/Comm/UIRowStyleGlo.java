package BP.WF.Comm;

import java.util.HashMap;
import java.util.Map;

public enum UIRowStyleGlo
{
	// <summary>
	// 娌℃湁椋庢牸
	// </summary>
	None(0),
	// <summary>
	// 浜ゆ浛椋庢牸
	// </summary>
	Alternately(1),
	// <summary>
	// 榧犳爣绉诲姩
	// </summary>
	Mouse(2),
	// <summary>
	// 榧犳爣绉诲姩骞朵氦镟?
	// </summary>
	MouseAndAlternately(3);
	
	private int intValue;
	private static Map<Integer, UIRowStyleGlo> mappings;
	
	private synchronized static Map<Integer, UIRowStyleGlo> getMappings()
	{
		if (mappings == null)
		{
			mappings = new HashMap<Integer, UIRowStyleGlo>();
		}
		return mappings;
	}
	
	private UIRowStyleGlo(int value)
	{
		intValue = value;
		UIRowStyleGlo.getMappings().put(value, this);
	}
	
	public int getValue()
	{
		return intValue;
	}
	
	public static UIRowStyleGlo forValue(int value)
	{
		return getMappings().get(value);
	}
}