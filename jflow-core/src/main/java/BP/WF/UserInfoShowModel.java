package BP.WF;

/** 
 用户信息显示格式
 
*/
public enum UserInfoShowModel
{
	/** 
	 用户ID,用户名
	 
	*/
	UserIDUserName(0),
	/** 
	 用户ID
	 
	*/
	UserIDOnly(1),
	/** 
	 用户名
	 
	*/
	UserNameOnly(2);

	private int intValue;
	private static java.util.HashMap<Integer, UserInfoShowModel> mappings;
	private synchronized static java.util.HashMap<Integer, UserInfoShowModel> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, UserInfoShowModel>();
		}
		return mappings;
	}

	private UserInfoShowModel(int value)
	{
		intValue = value;
		UserInfoShowModel.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static UserInfoShowModel forValue(int value)
	{
		return getMappings().get(value);
	}
}