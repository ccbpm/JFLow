package BP.WF.Port;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 工作人员
*/
public class Emps extends EntitiesNoName
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Emp();
	}
	/** 
	 工作人员s
	*/
	public Emps()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Emp> ToJavaList()
	{
		return (java.util.List<Emp>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Emp> Tolist()
	{
		java.util.ArrayList<Emp> list = new java.util.ArrayList<Emp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Emp)this.get(i));
		}
		return list;
	}
}