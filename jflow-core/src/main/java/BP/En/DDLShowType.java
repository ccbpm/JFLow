package BP.En;

public enum DDLShowType {
	/** 
	 None
	 
	*/
	None,
	/** 
	 Gender
	 
	*/
	Gender,
	/** 
	 Boolean
	 
	*/
	Boolean,
	/** 
	 
	  
	*/
	SysEnum,
	/** 
	 Self
	 
	*/
	Self,
	/** 
	 实体集合
	 
	*/
	Ens,
	/** 
	 与Table 相关联
	 
	*/
	BindSQL;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DDLShowType forValue(int value)
	{
		return values()[value];
	}

}
