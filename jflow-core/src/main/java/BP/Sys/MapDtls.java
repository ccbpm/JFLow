package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 明细s
*/
public class MapDtls extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 明细s
	*/
	public MapDtls()
	{
	}
	/** 
	 明细s
	 
	 @param fk_mapdata s
	*/
	public MapDtls(String fk_mapdata)
	{
		if (fk_mapdata == null)
		{
			throw new RuntimeException("fk_mapdata 传的值为空,不能查询.");
		}

		//zhoupeng 注销掉，为了这样多的过滤条件？
	   // this.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata, MapDtlAttr.FK_Node, 0, MapDtlAttr.No);
		this.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDtl();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapDtl> ToJavaList()
	{
		return (List<MapDtl>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDtl> Tolist()
	{
		ArrayList<MapDtl> list = new ArrayList<MapDtl>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((MapDtl)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}