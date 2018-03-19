package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrSFTables extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrSFTables()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrSFTable();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrSFTable> ToJavaList()
	{
		return (List<MapAttrSFTable>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrSFTable> Tolist()
	{
		ArrayList<MapAttrSFTable> list = new ArrayList<MapAttrSFTable>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrSFTable)this.get(i));
		}
		return list;
	}
}
