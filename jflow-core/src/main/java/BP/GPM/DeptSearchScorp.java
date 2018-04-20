package BP.GPM;

import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;

/** 
 部门查询权限 的摘要说明。
*/
public class DeptSearchScorp extends Entity
{

	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}

	/** 
	 工作人员ID
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(DeptSearchScorpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		SetValByKey(DeptSearchScorpAttr.FK_Emp, value);
	}
	public final String getFK_DeptT()
	{
		return this.GetValRefTextByKey(DeptSearchScorpAttr.FK_Dept);
	}
	/** 
	部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(DeptSearchScorpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(DeptSearchScorpAttr.FK_Dept, value);
	}
	/** 
	 工作人员岗位
	*/
	public DeptSearchScorp()
	{
	}
	/** 
	 工作人员部门对应
	 @param _empoid 工作人员ID
	 @param wsNo 部门编号 	
	 * @throws Exception 
	*/
	public DeptSearchScorp(String _empoid, String wsNo) throws Exception
	{
		this.setFK_Emp(_empoid);
		this.setFK_Dept(wsNo);
		if (this.Retrieve() == 0)
		{
			this.Insert();
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_DeptSearchScorp");
		map.setEnDesc("部门查询权限");
		map.Java_SetEnType(EnType.Dot2Dot);

		map.AddTBStringPK(DeptSearchScorpAttr.FK_Emp, null, "操作员", true, true, 1, 50, 11);
		map.AddDDLEntitiesPK(DeptSearchScorpAttr.FK_Dept, null, "部门", new Depts(), true);
			// map.AddDDLEntitiesPK(DeptSearchScorpAttr.FK_Emp, null, "操作员", new Emps(), true);
		this.set_enMap(map);
		return this.get_enMap();
	}
	

}