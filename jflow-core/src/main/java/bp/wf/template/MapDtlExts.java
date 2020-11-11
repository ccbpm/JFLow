package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 明细s
*/
public class MapDtlExts extends EntitiesNoName
{

		///构造
	/** 
	 明细s
	*/
	public MapDtlExts()
	{
	}
	/** 
	 明细s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public MapDtlExts(String fk_mapdata) throws Exception
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


		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapDtlExt> ToJavaList()
	{
		return (List<MapDtlExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDtlExt> Tolist()
	{
		ArrayList<MapDtlExt> list = new ArrayList<MapDtlExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDtlExt)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}