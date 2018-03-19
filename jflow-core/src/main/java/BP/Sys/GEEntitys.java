package BP.Sys;

import BP.En.EntitiesOID;
import BP.En.Entity;

/**
 * 通用实体s
 */
public class GEEntitys extends EntitiesOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 重载基类方法
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
	
	// 方法
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
	public GEEntitys()
	{
	}
	
	/**
	 * 通用实体ID
	 * 
	 * @param fk_mapdtl
	 */
	public GEEntitys(String fk_mapdata)
	{
		this.FK_MapData = fk_mapdata;
	}
}