package BP.Sys;

import Oracle.ManagedDataAccess.Client.*;
import MySql.*;
import MySql.Data.*;
import MySql.Data.Common.*;
import MySql.Data.MySqlClient.*;
import BP.DA.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 运行平台
*/
public enum Plant
{
	/** 
	 默认不打开.
	*/
	CSharp,
	/** 
	 打开
	*/
	Java;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static Plant forValue(int value)
	{
		return values()[value];
	}
}