package BP.Pub;

import BP.En.Entity;
import BP.En.SimpleNoNameFixs;

/**
 * NDs
 */
public class NYs extends SimpleNoNameFixs
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 年月集合
	 */
	public NYs()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new NY();
	}
}