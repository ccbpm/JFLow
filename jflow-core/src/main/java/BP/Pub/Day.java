package BP.Pub;

import BP.En.SimpleNoNameFix;

public class Day extends SimpleNoNameFix
{
	// 实现基本的方方法 
	 
	/**
	 * 物理表
	 */
	@Override
	public String getPhysicsTable()
	{
		return "Pub_Day";
	}
	
	/**
	 * 描述
	 */
	@Override
	public String getDesc()
	{
		return "日期"; // "日期";
	}
	
	// 构造方法
	public Day()
	{
	}
	
	/**
	 * _No
	 * 
	 * @param _No
	 * @throws Exception 
	 */
	public Day(String _No) throws Exception
	{
		super(_No);
	}
}