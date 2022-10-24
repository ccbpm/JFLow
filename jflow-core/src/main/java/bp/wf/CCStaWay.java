package bp.wf;


/** 
 抄送到岗位计算方式
*/
public enum CCStaWay
{
	/** 
	 仅按岗位计算
	*/
	StationOnly,
	/** 
	 按岗位智能计算(当前节点的人员岗位)
	*/
	StationSmartCurrNodeWorker,
	/** 
	 按岗位智能计算(接受节点的人员岗位)
	*/
	StationSmartNextNodeWorker,
	/** 
	 按岗位与部门的交集
	*/
	StationAdndDept,
	/** 
	 按直线部门找岗位下的人员(当前节点)
	*/
	StationDeptUpLevelCurrNodeWorker,
	/** 
	 按直线部门找岗位下的人员
	*/
	StationDeptUpLevelNextNodeWorker;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static CCStaWay forValue(int value)
	{return values()[value];
	}
}