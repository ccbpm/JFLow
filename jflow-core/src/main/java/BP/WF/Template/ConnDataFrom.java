package BP.WF.Template;


/** 
 条件数据源
 
*/
public enum ConnDataFrom
{
	/** 
	 表单数据
	 
	*/
	NodeForm,
	/** 
	 岗位数据
	 
	*/
	Stas,
	/** 
	 Depts
	 
	*/
	Depts,
	/** 
	 按sql计算.
	 
	*/
	SQL,
	/** 
	 按参数
	 
	*/
	Paras,
	/** 
	 按Url.
	 
	*/
	Url,
	/// 按sql模版计算.
    SQLTemplate,
    StandAloneFrm;

	public int getValue()
	{
		return this.ordinal();
	}

	public static ConnDataFrom forValue(int value)
	{
		return values()[value];
	}
}