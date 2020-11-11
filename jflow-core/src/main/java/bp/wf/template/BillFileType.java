package bp.wf.template;

/** 
 生成的类型
*/
public enum BillFileType
{
	/** 
	 Word
	*/
	Word(0),
	PDF(1),
	Excel(2),
	Html(3),
	RuiLang(5);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, BillFileType> mappings;
	private static java.util.HashMap<Integer, BillFileType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (BillFileType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, BillFileType>();
				}
			}
		}
		return mappings;
	}

	private BillFileType(int value) 
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static BillFileType forValue(int value)
	{
		return getMappings().get(value);
	}
}