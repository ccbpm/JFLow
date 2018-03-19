package BP.GPM;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 岗位s
*/
public class Stations extends EntitiesNoName
{
	/** 
	 岗位
	*/
	public Stations()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BP.GPM.Station();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Station> ToJavaList()
	{
		return (java.util.List<Station>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Station> Tolist()
	{
		java.util.ArrayList<Station> list = new java.util.ArrayList<Station>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Station)this.get(i));
		}
		return list;
	}
}