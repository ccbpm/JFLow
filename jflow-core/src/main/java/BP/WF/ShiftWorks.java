package BP.WF;

import BP.En.Entities;
import BP.En.Entity;

/** 
 移交记录s 
*/
public class ShiftWorks extends Entities
{
		
	/** 
	 移交记录s
	*/
	public ShiftWorks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ShiftWork();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<ShiftWork> ToJavaList()
	{
		return (java.util.List<ShiftWork>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<ShiftWork> Tolist()
	{
		java.util.ArrayList<ShiftWork> list = new java.util.ArrayList<ShiftWork>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ShiftWork)this.get(i));
		}
		return list;
	}
}