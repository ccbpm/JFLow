package BP.Sys;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 对比状态存储s
 */
public class Contrasts extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 对比状态存储s
	 */
	public Contrasts()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Contrast();
	}
}