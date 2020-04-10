package BP.Frm;

import java.util.HashMap;

/** 
 实体类型
*/
public enum SearchDataRole
{
	/**
	 *  只查询自己创建的
	 */
	ByOnlySelf(0),
	/**
	 * 查询本部门创建的包含兼职部门
	 */
	ByDept(1),
	/**
	 * 查询本部门（包含兼职部门）及子级部门
	 */
	ByDeptAndSSubLevel(2),
	/**
	 *根据岗位设定的部门的集合
	 */
	ByStationDept(3);

	public static final int SIZE = Integer.SIZE;

	private int intValue;
	private static HashMap<Integer, SearchDataRole> mappings;
	private static HashMap<Integer, SearchDataRole> getMappings()
	{
		if (mappings == null)
		{
			synchronized (SearchDataRole.class)
			{
				if (mappings == null)
				{
					mappings = new HashMap<Integer, SearchDataRole>();
				}
			}
		}
		return mappings;
	}

	private SearchDataRole(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static SearchDataRole forValue(int value)
	{
		return getMappings().get(value);
	}
}