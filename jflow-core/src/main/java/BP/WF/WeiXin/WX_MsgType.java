package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 消息类型
*/
public enum WX_MsgType
{
	text(0),
	image(1),
	voice(2),
	video(3),
	file(4),
	news(5),
	mapnews(6);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, WX_MsgType> mappings;
	private static java.util.HashMap<Integer, WX_MsgType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (WX_MsgType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, WX_MsgType>();
				}
			}
		}
		return mappings;
	}

	private WX_MsgType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static WX_MsgType forValue(int value)
	{
		return getMappings().get(value);
	}
}