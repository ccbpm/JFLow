package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 框架s
*/
public class MapFrames extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrame> ToJavaList()
	{
		return (List<MapFrame>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrame> Tolist()
	{
		ArrayList<MapFrame> list = new ArrayList<MapFrame>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrame)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}