package BP.Sys;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 映射基础s
 */
public class MapDataExts extends EntitiesNoName
{
	// 构造
	/**
	 * 映射基础s
	 */
	public MapDataExts()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDataExt();
	}
}
