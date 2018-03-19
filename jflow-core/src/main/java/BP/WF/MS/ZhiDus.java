package BP.WF.MS;

import BP.En.Entities;
import BP.En.Entity;

/** 
 制度s
*/
public class ZhiDus extends Entities
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ZhiDu();
	}
	/** 
	 制度
	*/
	public ZhiDus()
	{
	}
		///#endregion
}