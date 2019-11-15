package BP.Pub;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * NDs
 */
public class NYs extends EntitiesNoName
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
	public Entity getNewEntity()
	{
		return new NY();
	}
}