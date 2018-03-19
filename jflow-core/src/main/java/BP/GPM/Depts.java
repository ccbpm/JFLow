package BP.GPM;

import BP.En.EntitiesTree;
import BP.En.Entity;

/** 
得到集合
*/
public class Depts extends EntitiesTree
{
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Dept();
	}
	/** 
	 部门集合
	*/
	public Depts()
	{

	}
	/** 
	 部门集合
	 @param parentNo 父部门No
	*/
	public Depts(String parentNo)
	{
		this.Retrieve(DeptAttr.ParentNo, parentNo);
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Dept> ToJavaList()
	{
		return (java.util.List<Dept>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Dept> Tolist()
	{
		java.util.ArrayList<Dept> list = new java.util.ArrayList<Dept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Dept)this.get(i));
		}
		return list;
	}
}