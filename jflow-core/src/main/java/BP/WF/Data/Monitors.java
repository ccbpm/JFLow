package BP.WF.Data;

import BP.En.Entities;
import BP.En.Entity;

/** 
 流程监控s
*/
public class Monitors extends Entities
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Monitor();
	}
	/** 
	 流程监控集合
	*/
	public Monitors()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Monitor> ToJavaList()
	{
		return (java.util.List<Monitor>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Monitor> Tolist()
	{
		java.util.ArrayList<Monitor> list = new java.util.ArrayList<Monitor>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Monitor)this.get(i));
		}
		return list;
	}
}