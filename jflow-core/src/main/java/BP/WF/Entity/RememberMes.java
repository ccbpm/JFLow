package BP.WF.Entity;

import BP.En.Entities;
import BP.En.Entity;

public class RememberMes extends Entities
{
	// 方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new RememberMe();
	}
	
	/**
	 * RememberMe
	 */
	public RememberMes()
	{
	}
}