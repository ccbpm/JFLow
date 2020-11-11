package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

public enum PicType
{
	/** 
	 自动签名
	*/
	Auto,
	/** 
	 手动签名
	*/
	ShouDong;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PicType forValue(int value) throws Exception
	{
		return values()[value];
	}
}