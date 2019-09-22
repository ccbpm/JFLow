package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 明细s
*/
public class MapDtlExts extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 明细s
	*/
	public MapDtlExts()
	{
	}
	/** 
	 明细s
	 
	 @param fk_mapdata s
	*/
	public MapDtlExts(String fk_mapdata)
	{
		this.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata, MapDtlAttr.FK_Node, 0, MapDtlAttr.No);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDtlExt();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapDtlExt> ToJavaList()
	{
		return (List<MapDtlExt>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDtlExt> Tolist()
	{
		ArrayList<MapDtlExt> list = new ArrayList<MapDtlExt>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((MapDtlExt)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}