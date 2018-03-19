package BP.Pub;

import BP.En.SimpleNoNameFix;

/**
 * 年月
 */
public class NY extends SimpleNoNameFix
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 实现基本的方方法
	/**
	 * 物理表
	 */
	@Override
	public String getPhysicsTable()
	{
		return "Pub_NY";
	}
	
	/**
	 * 描述
	 */
	@Override
	public String getDesc()
	{
		return "年月"; // "年月";
	}
	
	// 构造方法
	
	public NY()
	{
	}
	
	public NY(String _No)
	{
		super(_No);
	}
}