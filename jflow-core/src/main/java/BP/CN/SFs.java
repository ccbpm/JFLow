package BP.CN;

import java.util.ArrayList;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 省份s
 */
public class SFs extends EntitiesNoName
{
	// 省份.
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new SF();
	}
	
	public static ArrayList<SF> convertSFs(Object obj)
	{
		return (ArrayList<SF>) obj;
	}
	
	// 构造方法
	/**
	 * 省份s
	 */
	public SFs()
	{
	}
}
