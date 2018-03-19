package BP.Sys;

import BP.En.EntitiesOID;
import BP.En.Entity;

/**
 * 默认值s
 */
public class GEEntityWordFrms extends EntitiesOID
{
	@Override
	public String toString()
	{
		// if (this.FK_MapData == null)
		// throw new Exception("@没有能 FK_MapData 给值。");
		return this.FK_MapData;
	}
	
	/**
	 * 主键
	 */
	public String FK_MapData = null;
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		// if (this.FK_MapData == null)
		// throw new Exception("@没有能 FK_MapData 给值。");
		
		if (this.FK_MapData == null)
		{
			return new GEEntity();
		}
		return new GEEntity(this.FK_MapData);
	}
	
	/**
	 * 通用实体ID
	 */
	public GEEntityWordFrms()
	{
	}
	
	/**
	 * 通用实体ID
	 * 
	 * @param fk_mapdtl
	 */
	public GEEntityWordFrms(String fk_mapdata)
	{
		this.FK_MapData = fk_mapdata;
	}
}