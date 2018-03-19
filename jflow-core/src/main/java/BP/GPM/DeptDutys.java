package BP.GPM;

import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 部门职务 
*/
public class DeptDutys extends Entities
{

	/** 
	 部门职务
	*/
	public DeptDutys()
	{
	}
	/** 
	 工作人员与职务集合
	*/
	public DeptDutys(String DutyNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptDutyAttr.FK_Duty, DutyNo);
		qo.DoQuery();
	}

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptDuty();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptDuty> ToJavaList()
	{
		return (java.util.List<DeptDuty>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptDuty> Tolist()
	{
		java.util.ArrayList<DeptDuty> list = new java.util.ArrayList<DeptDuty>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptDuty)this.get(i));
		}
		return list;
	}

}