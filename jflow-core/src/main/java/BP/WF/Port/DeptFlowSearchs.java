package BP.WF.Port;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 流程部门数据查询权限 
*/
public class DeptFlowSearchs extends EntitiesMyPK
{

		
	/** 
	 流程部门数据查询权限
	*/
	public DeptFlowSearchs()
	{
	}
	/** 
	 流程部门数据查询权限
	 @param FK_Emp FK_Emp
	*/
	public DeptFlowSearchs(String FK_Emp)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptFlowSearchAttr.FK_Emp, FK_Emp);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptFlowSearch();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptFlowSearch> ToJavaList()
	{
		return (java.util.List<DeptFlowSearch>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptFlowSearch> Tolist()
	{
		java.util.ArrayList<DeptFlowSearch> list = new java.util.ArrayList<DeptFlowSearch>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptFlowSearch)this.get(i));
		}
		return list;
	}
}