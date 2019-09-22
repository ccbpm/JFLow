package BP.DA;

import BP.Pub.*;
import BP.Sys.*;
import java.io.*;
import java.time.*;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region enum LogType
/** 
 信息类型
*/
public enum LogType
{
	/** 
	 提示
	*/
	Info(1),
	/** 
	 警告
	*/
	Warning(2),
	/** 
	 错误
	*/
	Error(3),
	/*
	 * debug
	 * */
	Debug(4);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, LogType> mappings;
	private static java.util.HashMap<Integer, LogType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (LogType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, LogType>();
				}
			}
		}
		return mappings;
	}

	private LogType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
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