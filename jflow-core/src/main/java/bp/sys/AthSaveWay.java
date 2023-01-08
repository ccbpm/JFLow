package bp.sys;


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
	FTPServer,
	/**
	 阿里云OSS
	 */
	OSS;

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