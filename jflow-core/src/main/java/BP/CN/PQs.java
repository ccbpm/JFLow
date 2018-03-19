package BP.CN;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 片区
 */
public class PQs extends EntitiesNoName
{
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new PQ();
	}
	
	// 构造方法
	/**
	 * 片区s
	 */
	public PQs()
	{
	}
}
