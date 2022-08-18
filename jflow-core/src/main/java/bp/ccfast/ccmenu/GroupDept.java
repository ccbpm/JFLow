package bp.ccfast.ccmenu;

import bp.en.*;
import bp.port.*;

/** 
 权限组部门
*/
public class GroupDept extends EntityMM
{

		///#region 属性
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(GroupDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)  throws Exception
	 {
		this.SetValByKey(GroupDeptAttr.FK_Dept, value);
	}
	public final String getFKGroup() throws Exception
	{
		return this.GetValStringByKey(GroupDeptAttr.FK_Group);
	}
	public final void setFKGroup(String value)  throws Exception
	 {
		this.SetValByKey(GroupDeptAttr.FK_Group, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 权限组部门
	*/
	public GroupDept()  {
	}

	/** 
	 权限组Dept
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupDept", "权限组部门");

		map.AddTBStringPK(GroupDeptAttr.FK_Group, null, "权限组", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupDeptAttr.FK_Dept, null, "部门", new Depts(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}