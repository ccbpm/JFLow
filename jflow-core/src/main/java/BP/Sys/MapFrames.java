package BP.Sys;

import java.util.ArrayList;

import BP.DA.*;
import BP.En.*;

/** 
 框架s
 
*/
public class MapFrames extends EntitiesMyPK
{
 
	/** 
	 框架s
	 
	*/
	public MapFrames()
	{
	}
	/** 
	 框架s
	 
	 @param fk_mapdata s
	*/
	public MapFrames(String fk_mapdata)
	{
		this.Retrieve(MapFrameAttr.FK_MapData, fk_mapdata);
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFrame();
	}
	
	
	public static ArrayList<MapFrame> convertMapExts(Object obj)
	{
		return (ArrayList<MapFrame>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapFrame> ToJavaList()
	{
		return (java.util.List<MapFrame>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapFrame> Tolist()
	{
		java.util.ArrayList<MapFrame> list = new java.util.ArrayList<MapFrame>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrame)this.get(i));
		}
		return list;
	}
}