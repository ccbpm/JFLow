package BP.GPM;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 部门人员信息 
*/
public class DeptEmps extends EntitiesMyPK
{

	/** 
	 工作部门人员信息
	*/
	public DeptEmps()
	{
	}

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptEmp();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptEmp> ToJavaList()
	{
		return (java.util.List<DeptEmp>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptEmp> Tolist()
	{
		java.util.ArrayList<DeptEmp> list = new java.util.ArrayList<DeptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptEmp)this.get(i));
		}
		return list;
	}
}