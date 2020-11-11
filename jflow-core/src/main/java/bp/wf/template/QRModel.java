package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;
import java.io.*;

/** 
 二维码生成方式
*/
public enum QRModel
{
	/** 
	 不生成
	*/
	None,
	/** 
	 生成
	*/
	Gener;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static QRModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}