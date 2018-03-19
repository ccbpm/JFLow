package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.Sys.MapDtlAttr;

/** 
 明细s
 
*/
public class MapDtlExts extends EntitiesNoName
{

		
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


		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapDtlExt> ToJavaList()
	{
		return (java.util.List<MapDtlExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapDtlExt> Tolist()
	{
		java.util.ArrayList<MapDtlExt> list = new java.util.ArrayList<MapDtlExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDtlExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}