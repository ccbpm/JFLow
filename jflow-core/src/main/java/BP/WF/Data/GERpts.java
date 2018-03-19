package BP.WF.Data;

import BP.En.Entity;

/** 
 报表s
*/
public class GERpts extends BP.En.EntitiesOID
{
	/** 
	 报表s
	*/
	public GERpts()
	{
	}

	/** 
	 获得一个实例.
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GERpt();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<GERpt> ToJavaList()
	{
		return (java.util.List<GERpt>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<GERpt> Tolist()
	{
		java.util.ArrayList<GERpt> list = new java.util.ArrayList<GERpt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GERpt)this.get(i));
		}
		return list;
	}
}