package BP.Sys;

/** 
 PopVal - 工作方式
 
*/
public enum PopValWorkModel
{
	/**
	 * 禁用
	 */
    None(0),
    
	/** 
	 自定义URL
	 
	*/
	SelfUrl(1),
	/** 
	 表格模式
	 
	*/
	TableOnly(2),
	/** 
	 表格分页模式
	 
	*/
	TablePage(3),
	/** 
	 分组模式
	 
	*/
	Group(4),
	/** 
	 树展现模式
	 
	*/
	Tree(5),
	/** 
	 双实体树
	 
	*/
	TreeDouble(6);

	private int intValue;
	private static java.util.HashMap<Integer, PopValWorkModel> mappings;
	private synchronized static java.util.HashMap<Integer, PopValWorkModel> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, PopValWorkModel>();
		}
		return mappings;
	}

	private PopValWorkModel(int value)
	{
		intValue = value;
		PopValWorkModel.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static PopValWorkModel forValue(int value)
	{
		return getMappings().get(value);
	}
}