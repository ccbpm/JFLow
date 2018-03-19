package BP.WF;

import BP.En.Entities;
import BP.En.Entity;

/** 
 消息s
*/
public class SMSs extends Entities
{
	/** 
	 获得实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SMS();
	}
	public SMSs()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<SMS> ToJavaList()
	{
		return (java.util.List<SMS>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<SMS> Tolist()
	{
		java.util.ArrayList<SMS> list = new java.util.ArrayList<SMS>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SMS)this.get(i));
		}
		return list;
	}
}