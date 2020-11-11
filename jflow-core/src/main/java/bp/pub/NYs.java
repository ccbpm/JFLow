package bp.pub;

import bp.en.EntitiesNoName;
import bp.en.Entity;

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
	public Entity getGetNewEntity()
	{
		return new NY();
	}
}