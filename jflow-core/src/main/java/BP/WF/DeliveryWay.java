package BP.WF;

/** 
 投递方式
*/
public enum DeliveryWay
{
	/** 
	 按岗位(以部门为纬度)
	*/
	ByStation(0),
	/** 
	 本部门内的人员
	*/
	FindSpecDeptEmpsInStationlist(19),
	/** 
	 按部门
	*/
	ByDept(1),
	/** 
	 按SQL
	*/
	BySQL(2),
	/** 
	 按本节点绑定的人员
	*/
	ByBindEmp(3),
	/** 
	 由上一步发送人选择
	*/
	BySelected(4),
	/** 
	 按表单选择人员
	*/
	ByPreviousNodeFormEmpsField(5),
	/** 
	 与上一节点的人员相同
	*/
	ByPreviousNodeEmp(6),
	/** 
	 与开始节点的人员相同
	*/
	ByStarter(7),
	/** 
	 与指定节点的人员相同
	*/
	BySpecNodeEmp(8),
	/** 
	 按岗位与部门交集计算
	*/
	ByDeptAndStation(9),
	/** 
	 按岗位计算(以部门集合为纬度)
	*/
	ByStationAndEmpDept(10),
	/** 
	 按指定节点的人员或者指定字段作为人员的岗位计算
	*/
	BySpecNodeEmpStation(11),
	/** 
	 按SQL确定子线程接受人与数据源.
	*/
	BySQLAsSubThreadEmpsAndData(12),
	/** 
	 按明细表确定子线程接受人.
	*/
	ByDtlAsSubThreadEmps(13),
	/** 
	 仅按岗位计算
	*/
	ByStationOnly(14),
	/** 
	 FEE计算.
	*/
	ByFEE(15),
	/** 
	 按绑定部门计算,该部门一人处理标识该工作结束(子线程).
	*/
	BySetDeptAsSubthread(16),
	/** 
	 按SQL模版计算
	*/
	BySQLTemplate(17),
	/** 
	 从人员到人员
	*/
	ByFromEmpToEmp(18),
	/** 
	 按照岗位计算-范围内的
	*/
	ByStationForPrj(20),
	/** 
	 按照选择模式计算.
	*/
	BySelectedForPrj(21),
	/**
	 按照设置的组织计算
	 */
	BySelectedOrgs(22),
	/**
	 按照部门领导计算
	 */
	ByDeptLeader(23),
	/** 
	 按照ccflow的BPM模式处理
	*/
	ByCCFlowBPM(100);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, DeliveryWay> mappings;
	private static java.util.HashMap<Integer, DeliveryWay> getMappings()
	{
		if (mappings == null)
		{
			synchronized (DeliveryWay.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, DeliveryWay>();
				}
			}
		}
		return mappings;
	}

	private DeliveryWay(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static DeliveryWay forValue(int value)
	{
		return getMappings().get(value);
	}
}