package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 映射基础s
 
*/
public class MapDatas extends EntitiesNoName
{

	
	/** 
	 映射基础s
	 
	*/
	public MapDatas()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapData();
	}
	
	public static ArrayList<MapData> convertMapDatas(Object obj)
	{
		return (ArrayList<MapData>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<MapData> ToJavaList()
	{
		return (java.util.List<MapData>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapData> Tolist()
	{
		java.util.ArrayList<MapData> list = new java.util.ArrayList<MapData>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapData)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}