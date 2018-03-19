package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrDTs extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrDTs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrDT();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrDT> ToJavaList()
	{
		return (List<MapAttrDT>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrDT> Tolist()
	{
		ArrayList<MapAttrDT> list = new ArrayList<MapAttrDT>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrDT)this.get(i));
		}
		return list;
	}
}
