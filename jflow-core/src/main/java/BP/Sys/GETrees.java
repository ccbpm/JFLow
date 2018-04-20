package BP.Sys;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 树结构实体s
 */
public class GETrees extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 物理表
	 */
	public String SFTable = null;
	public String Desc = null;
	
	/**
	 * GETrees
	 */
	public GETrees()
	{
	}
	
	public GETrees(String sftable, String tableDesc)
	{
		this.SFTable = sftable;
		this.Desc = tableDesc;
	}
	
	@Override
	public Entity getGetNewEntity()
	{
		return new GETree(this.SFTable, this.Desc);
	}
	
	@Override
	public int RetrieveAll() throws Exception
	{
		return this.RetrieveAllFromDBSource();
	}
}