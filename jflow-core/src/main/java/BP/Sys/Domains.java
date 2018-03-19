package BP.Sys;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 域s
 */
public class Domains extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	public Domains()
	{
	}
	
	// 重写
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Domain();
	}
}