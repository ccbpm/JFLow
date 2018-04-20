package BP.Sys;

import BP.En.Entities;
import BP.En.Entity;

/** 
 父子流程s
 
*/
public class FrmSubFlows extends Entities
{
		
	/** 
	 父子流程s
	 
	*/
	public FrmSubFlows()
	{
	}
	/** 
	 父子流程s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmSubFlows(String fk_mapdata) throws Exception
	{
		if ( SystemConfig.getIsDebug())
		{
			this.Retrieve("No", fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash("No", (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmSubFlow();
	}
		///#endregion
}