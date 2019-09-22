package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
 表数据来源类型
*/
public enum SrcType
{
	/** 
	 本地的类
	*/
	BPClass(0),
	/** 
	 通过ccform创建表
	*/
	CreateTable(1),
	/** 
	 表或视图
	*/
	TableOrView(2),
	/** 
	 SQL查询数据
	*/
	SQL(3),
	/** 
	 WebServices
	*/
	WebServices(4),
	/** 
	 hand
	*/
	Handler(5),
	/** 
	 JS请求数据.
	*/
	JQuery(6);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, SrcType> mappings;
	private static java.util.HashMap<Integer, SrcType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (SrcType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, SrcType>();
				}
			}
		}
		return mappings;
	}

	private SrcType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static SrcType forValue(int value)
	{
		return getMappings().get(value);
	}
}