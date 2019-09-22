package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Sys.*;
import java.util.*;
import java.time.*;

/** 
 实体类型
*/
public enum EntityType
{
	CCFrom(0),
	FrmBill(1),
	FrmDict(2),
	EntityTree(3);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, EntityType> mappings;
	private static java.util.HashMap<Integer, EntityType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (EntityType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, EntityType>();
				}
			}
		}
		return mappings;
	}

	private EntityType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static EntityType forValue(int value)
	{
		return getMappings().get(value);
	}
}