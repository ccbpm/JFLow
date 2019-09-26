package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 权限组人员
*/
public class GroupEmp extends EntityMM
{

		///#region 属性
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(GroupEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(GroupEmpAttr.FK_Emp, value);
	}
	public final String getFK_Group() throws Exception
	{
		return this.GetValStringByKey(GroupEmpAttr.FK_Group);
	}
	public final void setFK_Group(String value) throws Exception
	{
		this.SetValByKey(GroupEmpAttr.FK_Group, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 权限组人员
	*/
	public GroupEmp()
	{
	}
	 
	/** 
	 权限组人员
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupEmp","权限组人员");
	  
		map.AddTBStringPK(GroupEmpAttr.FK_Group, null, "权限组", true, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupEmpAttr.FK_Emp, null, "人员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}