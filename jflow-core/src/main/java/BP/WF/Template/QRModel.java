package BP.WF.Template;


/** 
 生成的类型
 
*/
public enum QRModel
{
	//不生成
	None(0),
	//生成
	Gener(1);

	private int intValue;
	private static java.util.HashMap<Integer, QRModel> mappings;
	private synchronized static java.util.HashMap<Integer, QRModel> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, QRModel>();
		}
		return mappings;
	}

	private QRModel(int value)
	{
		intValue = value;
		QRModel.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static QRModel forValue(int value)
	{
		return getMappings().get(value);
	}
}