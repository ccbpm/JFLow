package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 公文属性控制s
*/
public class BtnLabExtWebOffices extends Entities
{
	/** 
	 公文属性控制s
	*/
	public BtnLabExtWebOffices()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BtnLabExtWebOffice();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<BtnLabExtWebOffice> ToJavaList()
	{
		return (java.util.List<BtnLabExtWebOffice>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<BtnLabExtWebOffice> Tolist()
	{
		java.util.ArrayList<BtnLabExtWebOffice> list = new java.util.ArrayList<BtnLabExtWebOffice>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BtnLabExtWebOffice)this.get(i));
		}
		return list;
	}
}