package BP.Sys;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 序列号s
 */
public class Serials extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 序列号s
	 */
	public Serials()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Serial();
	}
}