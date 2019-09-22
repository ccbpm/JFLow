package BP.GPM.AD;

import BP.*;
import BP.Port.*;
import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.GPM.*;
import java.util.*;

/** 
 类型
*/
public enum TypeEnum
{
	/** 
	 组织单位
	*/
	OU(1),
	/** 
	 用户
	*/
	USER(2);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, TypeEnum> mappings;
	private static java.util.HashMap<Integer, TypeEnum> getMappings()
	{
		if (mappings == null)
		{
			synchronized (TypeEnum.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, TypeEnum>();
				}
			}
		}
		return mappings;
	}

	private TypeEnum(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static TypeEnum forValue(int value)
	{
		return getMappings().get(value);
	}
}