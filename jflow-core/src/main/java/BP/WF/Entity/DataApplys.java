package BP.WF.Entity;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 追加时间申请s
 */
public class DataApplys extends Entities
{
	// 构造
	/**
	 * 追加时间申请s
	 */
	public DataApplys()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new DataApply();
	}
}