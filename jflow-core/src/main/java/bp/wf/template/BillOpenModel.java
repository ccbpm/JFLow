package bp.wf.template;

/** 
 生成的文件打开方式
*/
public enum BillOpenModel
{
	/** 
	 下载保存
	*/
	DownLoad(0),
	/** 
	 在线WebOffice打开
	*/
	WebOffice(1);

	public static final int SIZE = Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, BillOpenModel> mappings;
	private static java.util.HashMap<Integer, BillOpenModel> getMappings()
	{
		if (mappings == null)
		{
			synchronized (BillOpenModel.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, BillOpenModel>();
				}
			}
		}
		return mappings;
	}

	private BillOpenModel(int value) 
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static BillOpenModel forValue(int value)
	{
		return getMappings().get(value);
	}
}