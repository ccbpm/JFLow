package BP.GPM;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 部门岗位人员对应 
*/
public class DeptEmpStations extends EntitiesMyPK
{

	/** 
	 工作部门岗位人员对应
	*/
	public DeptEmpStations()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptEmpStation();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptEmpStation> ToJavaList()
	{
		return (java.util.List<DeptEmpStation>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptEmpStation> Tolist()
	{
		java.util.ArrayList<DeptEmpStation> list = new java.util.ArrayList<DeptEmpStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptEmpStation)this.get(i));
		}
		return list;
	}
}