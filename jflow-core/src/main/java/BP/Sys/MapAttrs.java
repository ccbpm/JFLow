package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 实体属性s
*/
public class MapAttrs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 实体属性s
	*/
	public MapAttrs()
	{
	}
	/** 
	 实体属性s
	*/
	public MapAttrs(String fk_map)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MapAttrAttr.FK_MapData, fk_map);
		qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
		qo.DoQuery();
	}
	public final int SearchMapAttrsYesVisable(String fk_map)
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
		for (MapAttr item : this)
		{
			if (item.getUIVisible() == false)
			{
				continue;
			}

			i += item.getUIWidthInt();
		}
		return i;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttr> ToJavaList()
	{
		return (List<MapAttr>)this;
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
			list.add((MapAttr)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}