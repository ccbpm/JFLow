package BP.Web.Controls;

import BP.Web.*;

/** 
 DDLShowType
*/
public enum DDLShowType
{
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

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DDLShowType forValue(int value)
	{
		return values()[value];
	}
}