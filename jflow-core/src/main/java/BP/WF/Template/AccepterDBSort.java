package BP.WF.Template;


/** 
 选择的数据类别
 
*/
public enum AccepterDBSort
{
	/** 
	 人员
	 
	*/
	Emp,
	/** 
	 部门
	 
	*/
	Dept,
	/** 
	 岗位
	 
	*/
	Station,
	/** 
	 权限组
	 
	*/
	Group;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AccepterDBSort forValue(int value)
	{
		return values()[value];
	}
}