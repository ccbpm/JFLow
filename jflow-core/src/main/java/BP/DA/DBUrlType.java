package BP.DA;

/**
 * 　连接到哪个库上． 他们存放在 web.config 的列表内．
 */
public enum DBUrlType
{
	/**
	 * 主要的应用程序
	 */
	AppCenterDSN,
	/**
	 * DBAccessOfOracle
	 */
	DBAccessOfOracle,
	/**
	 * DBAccessOfOracle1
	 */
	DBAccessOfOracle1,
	/**
	 * DBAccessOfMSMSSQL
	 */
	DBAccessOfMSMSSQL,
	/**
	 * access��l�ӣ�
	 */
	DBAccessOfOLE,
	/**
	 * DBAccessOfODBC
	 */
	DBAccessOfODBC;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static DBUrlType forValue(int value)
	{
		return values()[value];
	}
}