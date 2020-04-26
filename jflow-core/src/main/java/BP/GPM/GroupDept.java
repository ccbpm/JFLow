package BP.GPM;

import BP.DA.*;
import BP.En.Map;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 权限组部门
*/
public class GroupDept extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(GroupDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		this.SetValByKey(GroupDeptAttr.FK_Dept, value);
	}
	public final String getFK_Group() throws Exception
	{
		return this.GetValStringByKey(GroupDeptAttr.FK_Group);
	}
	public final void setFK_Group(String value) throws Exception
	{
		this.SetValByKey(GroupDeptAttr.FK_Group, value);
	}
		///#endregion

		///#region 构造方法
	/** 
	 权限组部门
	*/
	public GroupDept()
	{
	}
	/** 
	 权限组部门
	 
	 @param no
	*/
	public GroupDept(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 权限组Dept
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupDept");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("权限组部门");
		map.setEnType(EnType.Sys);

		map.AddTBStringPK(GroupDeptAttr.FK_Group, null, "权限组", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupDeptAttr.FK_Dept, null, "部门", new Depts(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}