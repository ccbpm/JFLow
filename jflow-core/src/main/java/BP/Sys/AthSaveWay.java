package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 文件保存方式
*/
public enum AthSaveWay
{
	/** 
	 IIS服务器
	*/
	IISServer,
	/** 
	 保存到数据库
	*/
	DB,
	/** 
	 ftp
	*/
	FTPServer;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AthSaveWay forValue(int value)
	{
		return values()[value];
	}
}