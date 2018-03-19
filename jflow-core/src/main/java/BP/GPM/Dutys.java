package BP.GPM;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 职务
*/
public class Dutys extends EntitiesNoName
{
	/** 
	 职务s
	*/
	public Dutys()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Duty();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Duty> ToJavaList()
	{
		return (java.util.List<Duty>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Duty> Tolist()
	{
		java.util.ArrayList<Duty> list = new java.util.ArrayList<Duty>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Duty)this.get(i));
		}
		return list;
	}

}