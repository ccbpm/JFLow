package BP.Sys;


/** 
 表单类型 0=傻瓜表单@1=自由表单@2=Silverlight表单(已取消)@3=嵌入式表单@4=Word表单@5=Excel表单
 
*/
public enum FrmType
{
	/** 
	 傻瓜表单
	 
	*/
	FoolForm(0),
	/** 
	 自由表单
	 
	*/
	FreeFrm(1),
	/** 
	 Silverlight表单(已取消)
	 
	*/
	Silverlight(2),
	/** 
	 URL表单(自定义)
	 
	*/
	Url(3),
	/** 
	 Excel类型表单
	*/
	ExcelFrm(5),
	/** 
	 Word类型表单
	*/
	WordFrm(4),
	/** 
	 VSTOExccel模式.
	*/
	VSTOForExcel(6),
	/** 
	 公文表单
	*/
	WebOffice(7);

	private int intValue;
	private static java.util.HashMap<Integer, FrmType> mappings;
	private synchronized static java.util.HashMap<Integer, FrmType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, FrmType>();
		}
		return mappings;
	}

	private FrmType(int value)
	{
		intValue = value;
		FrmType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static FrmType forValue(int value)
	{
		return getMappings().get(value);
	}
}