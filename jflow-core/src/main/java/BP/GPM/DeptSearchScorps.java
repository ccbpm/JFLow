package BP.GPM;

import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 部门查询权限 
*/
public class DeptSearchScorps extends Entities
{

	/** 
	 部门查询权限
	*/
	public DeptSearchScorps()
	{
	}
	/** 
	 部门查询权限
	 @param FK_Emp FK_Emp
	 * @throws Exception 
	*/
	public DeptSearchScorps(String FK_Emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptSearchScorpAttr.FK_Emp, FK_Emp);
		qo.DoQuery();
	}

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptSearchScorp();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptSearchScorp> ToJavaList()
	{
		return (java.util.List<DeptSearchScorp>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptSearchScorp> Tolist()
	{
		java.util.ArrayList<DeptSearchScorp> list = new java.util.ArrayList<DeptSearchScorp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptSearchScorp)this.get(i));
		}
		return list;
	}

}