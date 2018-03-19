package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 点对点s
 
*/
public class MapM2Ms extends EntitiesMyPK
{

		
	/** 
	 点对点s
	 
	*/
	public MapM2Ms()
	{
	}
	/** 
	 点对点s
	 
	 @param fk_mapdata s
	*/
	public MapM2Ms(String fk_mapdata)
	{
		this.Retrieve(MapM2MAttr.FK_MapData, fk_mapdata, MapM2MAttr.GroupID);
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapM2M();
	}
	public static ArrayList<MapM2M> convertMapM2Ms(Object obj)
	{
		return (ArrayList<MapM2M>) obj;
	}

	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapM2M> ToJavaList()
	{
		return (java.util.List<MapM2M>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapM2M> Tolist()
	{
		java.util.ArrayList<MapM2M> list = new java.util.ArrayList<MapM2M>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapM2M)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}