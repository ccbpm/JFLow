package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 权限组人员
 
*/
public class GroupEmp extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(GroupEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(GroupEmpAttr.FK_Emp, value);
	}
	public final String getFK_Group()
	{
		return this.GetValStringByKey(GroupEmpAttr.FK_Group);
	}
	public final void setFK_Group(String value)
	{
		this.SetValByKey(GroupEmpAttr.FK_Group, value);
	}

		///#region 构造方法
	/** 
	 权限组人员
	 
	*/
	public GroupEmp()
	{
	}
	/** 
	 权限组人员
	 
	 @param mypk
	 * @throws Exception 
	*/
	public GroupEmp(String no) throws Exception
	{
		this.Retrieve();
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
		Map map = new Map("GPM_GroupEmp");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("权限组人员");
		map.setEnType(EnType.App);

		map.AddTBStringPK(GroupEmpAttr.FK_Group, null, "权限组", true, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupEmpAttr.FK_Emp, null, "人员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}