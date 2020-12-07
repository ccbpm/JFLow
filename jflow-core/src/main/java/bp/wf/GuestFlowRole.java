package bp.wf;

/** 
 是否外部用户参与流程(非组织结构人员参与的流程)
*/
public enum GuestFlowRole
{
	/** 
	不参与
	*/
	None(0),
	/** 
	 开始节点参与
	*/
	StartNodeJoin(1),
	/** 
	 中间节点参与
	*/
	MiddleNodeJoin(2);
	
	private int intValue;
	private static java.util.HashMap<Integer, GuestFlowRole> mappings;
	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return intValue;
	}
	public static GuestFlowRole forValue(int value) throws Exception
	{
		return values()[value];
	}
	private GuestFlowRole(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}
	private static java.util.HashMap<Integer, GuestFlowRole> getMappings()
	{
		if (mappings == null)
		{
			synchronized (GuestFlowRole.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, GuestFlowRole>();
				}
			}
		}
		return mappings;
	}
}