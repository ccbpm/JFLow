package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrStrings extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrStrings()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrString();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrString> ToJavaList()
	{
		return (List<MapAttrString>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrString> Tolist()
	{
		ArrayList<MapAttrString> list = new ArrayList<MapAttrString>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrString)this.get(i));
		}
		return list;
	}
}
