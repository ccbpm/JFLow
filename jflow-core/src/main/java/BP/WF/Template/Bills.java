package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 单据s
*/
public class Bills extends EntitiesMyPK
{

		
	/** 
	 单据s
	*/
	public Bills()
	{
	}
	/** 
	 单据
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Bill();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Bill> ToJavaList()
	{
		return (java.util.List<Bill>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Bill> Tolist()
	{
		java.util.ArrayList<Bill> list = new java.util.ArrayList<Bill>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Bill)this.get(i));
		}
		return list;
	}
}