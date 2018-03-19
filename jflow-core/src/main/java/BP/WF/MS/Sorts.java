package BP.WF.MS;

import BP.En.Entities;
import BP.En.Entity;

/** 
 目录s
*/
public class Sorts extends Entities
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Sort();
	}
	/** 
	 目录
	*/
	public Sorts()
	{
	}
		///#endregion
}