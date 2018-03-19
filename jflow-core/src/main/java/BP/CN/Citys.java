package BP.CN;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 城市
 */
public class Citys extends EntitiesNoName
{
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new City();
	}
	
	// 构造方法
	/**
	 * 城市s
	 */
	public Citys()
	{
	}
	
	/**
	 * 城市s
	 * 
	 * @param sf
	 *            省份
	 */
	public Citys(String sf)
	{
		this.Retrieve(CityAttr.FK_SF, sf);
	}
}