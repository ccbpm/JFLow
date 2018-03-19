package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrNums extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrNums()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrNum();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrNum> ToJavaList()
	{
		return (List<MapAttrNum>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrNum> Tolist()
	{
		ArrayList<MapAttrNum> list = new ArrayList<MapAttrNum>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrNum)this.get(i));
		}
		return list;
	}
}
