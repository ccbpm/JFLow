package BP.WF.Data;

import BP.En.Entity;
import BP.En.QueryObject;
/** 
 报表集合
*/
public class MyDeptEmps extends BP.En.EntitiesNoName
{
	/** 
	 报表集合
	*/
	public MyDeptEmps()
	{
	}

	@Override
	public Entity getGetNewEntity()
	{
		return new MyDeptEmp();
	}
	@Override
	public int RetrieveAll()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyDeptEmpAttr.FK_Dept, BP.Web.WebUser.getFK_Dept());
		return qo.DoQuery();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<MyDeptEmp> ToJavaList()
	{
		return (java.util.List<MyDeptEmp>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<MyDeptEmp> Tolist()
	{
		java.util.ArrayList<MyDeptEmp> list = new java.util.ArrayList<MyDeptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptEmp)this.get(i));
		}
		return list;
	}
}