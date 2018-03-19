package BP.Sys;
/** 
 组织解构数据来源
*/
public enum OSDBSrc
{
	/** 
	 数据库.
	*/
	Database,
	/** 
	 WebServices
	*/
	WebServices;

	public int getValue()
	{
		return this.ordinal();
	}

	public static OSDBSrc forValue(int value)
	{
		return values()[value];
	}
}