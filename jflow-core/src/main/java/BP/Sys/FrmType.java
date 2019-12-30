package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 表单类型 @0=傻瓜表单@1=自由表单@3=嵌入式表单@4=Word表单@5=Excel表单@6=VSTOForExcel@7=Entity
*/
public enum FrmType
{
	/** 
	 傻瓜表单
	*/
	FoolForm(0),
	/** 
	 自由表单
	*/
	FreeFrm(1),
	/** 
	 URL表单(自定义)
	*/
	Url(3),
	/** 
	 Word类型表单
	*/
	WordFrm(4),
	/** 
	 Excel类型表单
	*/
	ExcelFrm(5),
	/** 
	 VSTOExccel模式.
	*/
	VSTOForExcel(6),
	/** 
	 实体类
	*/
<<<<<<< .mine
	Entity(7),
	/** 
	 开发者表单
	*/
	Develop(8);
=======
	Entity(7),
	/**
	 * 开发者表单
	 */
	Develop(8);
>>>>>>> .r2518

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, FrmType> mappings;
	private static java.util.HashMap<Integer, FrmType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (FrmType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, FrmType>();
				}
			}
		}
		return mappings;
	}

	private FrmType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static FrmType forValue(int value)
	{
		return getMappings().get(value);
	}
}