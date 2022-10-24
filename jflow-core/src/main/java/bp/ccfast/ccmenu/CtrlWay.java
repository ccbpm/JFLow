package bp.ccfast.ccmenu;


/** 
 控制方式
*/
public enum CtrlWay
{
	/** 
	 游客
	*/
	Guest,
	/** 
	 任何人
	*/
	AnyOne,
	/** 
	 按岗位
	*/
	ByStation,
	/** 
	 按部门
	*/
	ByDept,
	/** 
	 按人员
	*/
	ByEmp,
	/** 
	 按sql
	*/
	BySQL;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static CtrlWay forValue(int value) 
	{return values()[value];
	}
}