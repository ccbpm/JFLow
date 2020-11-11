package bp.sys;

import bp.en.*;
import java.util.*;


/** 
 实体属性s
*/
public class MapAttrs extends EntitiesMyPK
{

	private static final long serialVersionUID = 1L;
	///构造
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
	public final int getWithOfCtl() throws Exception
	{
		int i = 0;
		for (MapAttr item : this.ToJavaList())
		{
			if (item.getUIVisible() == false)
			{
				continue;
			}

			i += item.getUIWidthInt();
		}
		return i;
	}

	
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
	public final ArrayList<MapAttr> Tolist()
	{
		ArrayList<MapAttr> list = new ArrayList<MapAttr>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttr)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}