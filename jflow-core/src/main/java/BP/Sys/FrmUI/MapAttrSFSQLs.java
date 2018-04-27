package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrSFSQLs extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrSFSQLs()
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
	public final List<MapAttrSFSQL> ToJavaList()
	{
		return (List<MapAttrSFSQL>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrSFSQL> Tolist()
	{
		ArrayList<MapAttrSFSQL> list = new ArrayList<MapAttrSFSQL>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrSFSQL)this.get(i));
		}
		return list;
	}
}
