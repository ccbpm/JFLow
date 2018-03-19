package BP.WF;

/** 
 抄送类型
 
*/
public enum SelectorModel
{
	/** 
	 不抄送
	 
	*/
	None,
	/** 
	 按人员
	 
	*/
	AsEmps,
	/** 
	 按岗位
	 
	*/
	AsStation,
	/** 
	 按节点
	 
	*/
	AsNode,
	/** 
	 按部门
	 
	*/
	AsDept,
	/** 
	 按照部门与岗位
	 
	*/
	AsDeptAndStation;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SelectorModel forValue(int value)
	{
		return values()[value];
	}
}