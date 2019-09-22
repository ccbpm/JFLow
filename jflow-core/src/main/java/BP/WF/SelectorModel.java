package BP.WF;

public enum SelectorModel
{
	/** 
	 岗位
	*/
	Station,
	/** 
	 部门
	*/
	Dept,
	/** 
	 操作员
	*/
	Emp,
	/** 
	 SQL
	*/
	SQL,
	/** 
	 SQL模版计算
	*/
	SQLTemplate,
	/** 
	 通用的人员选择器.
	*/
	GenerUserSelecter,
	/** 
	 按部门与岗位的交集
	*/
	DeptAndStation,
	/** 
	 自定义链接
	*/
	Url,
	/** 
	 通用部门岗位人员选择器
	*/
	AccepterOfDeptStationEmp,
	/** 
	 按岗位智能计算(操作员所在部门)
	*/
	AccepterOfDeptStationOfCurrentOper;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SelectorModel forValue(int value)
	{
		return values()[value];
	}
}