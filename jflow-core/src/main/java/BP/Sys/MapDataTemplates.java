package BP.Sys;

import java.util.ArrayList;

import BP.DA.*;
import BP.En.*;


/** 
 映射基础s
 
*/
public class MapDataTemplates extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 映射基础s
	 
	*/
	public MapDataTemplates()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDataTemplate();
	}
	
	public static ArrayList<MapDataTemplate> convertMapDatas(Object obj)
	{
		return (ArrayList<MapDataTemplate>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<MapDataTemplate> ToJavaList()
	{
		return (java.util.List<MapDataTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 s
	 @return List
	*/
	public final java.util.ArrayList<MapDataTemplate> Tolist()
	{
		java.util.ArrayList<MapDataTemplate> list = new java.util.ArrayList<MapDataTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataTemplate)this.get(i));
		}
		return list;
	}
  
}