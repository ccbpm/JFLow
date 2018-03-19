package BP.Pub;

import BP.En.SimpleNoNameFix;

/**
 * 月份
 */
public class YF extends SimpleNoNameFix
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
		return "Pub_YF";
	}
	
	/**
	 * 描述
	 */
	@Override
	public String getDesc()
	{
		return "月份"; // "月份";
	}
	
	// 构造方法
	public YF()
	{
	}
	
	/**
	 * _No
	 * 
	 * @param _No
	 */
	public YF(String _No)
	{
		super(_No);
	}
}