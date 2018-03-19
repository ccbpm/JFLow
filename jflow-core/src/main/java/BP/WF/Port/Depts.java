package BP.WF.Port;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
部门集合
*/
public class Depts extends EntitiesNoName
{
	/** 
	 查询全部。
	 @return 
	*/
	@Override
	public int RetrieveAll()
	{
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			return super.RetrieveAll();
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptAttr.No, " = ", BP.Web.WebUser.getFK_Dept());
		qo.addOr();
		qo.AddWhere(DeptAttr.ParentNo, " = ", BP.Web.WebUser.getFK_Dept());
		return qo.DoQuery();
	}
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Dept();
	}
	/** 
	 create ens
	*/
	public Depts()
	{
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