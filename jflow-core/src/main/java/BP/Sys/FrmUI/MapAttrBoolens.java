package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrBoolens extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrBoolens()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrBoolen();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrBoolen> ToJavaList()
	{
		return (List<MapAttrBoolen>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrBoolen> Tolist()
	{
		ArrayList<MapAttrBoolen> list = new ArrayList<MapAttrBoolen>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrBoolen)this.get(i));
		}
		return list;
	}
}
