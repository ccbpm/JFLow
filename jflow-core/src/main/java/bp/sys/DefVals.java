package bp.sys;

import bp.en.*;


/** 
 默认值s
*/
public class DefVals extends EntitiesMyPK
{
	/** 
	 默认值
	*/
	public DefVals()throws Exception
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DefVal();
	}
}