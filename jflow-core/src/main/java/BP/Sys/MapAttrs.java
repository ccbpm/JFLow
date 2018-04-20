package BP.Sys;

import java.util.ArrayList;

import BP.DA.*;
import BP.En.*;

/** 
 实体属性s
 
*/
public class MapAttrs extends EntitiesMyPK
{

		
	/** 
	 实体属性s
	 
	*/
	public MapAttrs()
	{
	}
	/** 
	 实体属性s
	 * @throws Exception 
	 
	*/
	public MapAttrs(String fk_map) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MapAttrAttr.FK_MapData, fk_map);
		qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
		qo.DoQuery();
	}
	@SuppressWarnings("unchecked")
	public static ArrayList<MapAttr> convertMapAttrs(Object obj)
	{
		return (ArrayList<MapAttr>) obj;
	}
	public final int SearchMapAttrsYesVisable(String fk_map) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MapAttrAttr.FK_MapData, fk_map);
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.UIVisible, 1);
		qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
	   // qo.addOrderBy(MapAttrAttr.Idx);
		return qo.DoQuery();
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttr();
	}
	public final int getWithOfCtl()
	{
		int i = 0;
		for (MapAttr item : convertMapAttrs(this))
		{
			if (item.getUIVisible() == false)
			{
				continue;
			}

			i += item.getUIWidthInt();
		}
		return i;
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapAttr> ToJavaList()
	{
		return (java.util.List<MapAttr>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapAttr> Tolist()
	{
		java.util.ArrayList<MapAttr> list = new java.util.ArrayList<MapAttr>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttr)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}