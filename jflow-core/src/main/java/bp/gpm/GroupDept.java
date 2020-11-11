package bp.gpm;

import bp.da.*;
import bp.en.*;
import bp.en.Map;

/** 
 权限组部门
*/
public class GroupDept extends EntityMM
{
	private static final long serialVersionUID = 1L;
	///属性
	public final String getFK_Dept()throws Exception
	{
		return this.GetValStringByKey(GroupDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		this.SetValByKey(GroupDeptAttr.FK_Dept, value);
	}
	public final String getFK_Group()throws Exception
	{
		return this.GetValStringByKey(GroupDeptAttr.FK_Group);
	}
	public final void setFK_Group(String value) throws Exception
	{
		this.SetValByKey(GroupDeptAttr.FK_Group, value);
	}

		///


		///构造方法
	/** 
	 权限组部门
	*/
	public GroupDept()
	{
	}
	/** 
	 权限组部门
	 
	 @param mypk
	 * @throws Exception 
	*/
	public GroupDept(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 权限组Dept
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupDept");
		map.setDepositaryOfEntity( Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("权限组部门");
		map.setEnType( EnType.Sys);

		map.AddTBStringPK(GroupDeptAttr.FK_Group, null, "权限组", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupDeptAttr.FK_Dept, null, "部门", new Depts(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
	
}