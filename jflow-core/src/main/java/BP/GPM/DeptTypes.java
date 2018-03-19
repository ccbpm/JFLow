package BP.GPM;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 部门类型
*/
public class DeptTypes extends EntitiesNoName
{
	/** 
	 部门类型s
	*/
	public DeptTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptType();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptType> ToJavaList()
	{
		return (java.util.List<DeptType>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptType> Tolist()
	{
		java.util.ArrayList<DeptType> list = new java.util.ArrayList<DeptType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptType)this.get(i));
		}
		return list;
	}

}