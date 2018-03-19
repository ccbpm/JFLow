package BP.WF.MS;

import BP.En.Entities;
import BP.En.Entity;

/** 
 章节s
*/
public class ZhiDuDtls extends Entities
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ZhiDuDtl();
	}
	/** 
	 章节
	*/
	public ZhiDuDtls()
	{
	}
		///#endregion
}