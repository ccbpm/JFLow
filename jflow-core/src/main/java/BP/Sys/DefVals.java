package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 默认值s
*/
public class DefVals extends EntitiesMyPK
{
	/** 
	 默认值
	*/
	public DefVals()
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