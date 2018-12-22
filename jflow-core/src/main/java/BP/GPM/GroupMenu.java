package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 权限组菜单
 
*/
public class GroupMenu extends EntityMM
{
		///#region 属性
	/** 
	 菜单
	 
	*/
	public final String getFK_Menu()
	{
		return this.GetValStringByKey(GroupMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value)
	{
		this.SetValByKey(GroupMenuAttr.FK_Menu, value);
	}
	/** 
	 权限组
	 
	*/
	public final String getFK_Group()
	{
		return this.GetValStringByKey(GroupMenuAttr.FK_Group);
	}
	public final void setFK_Group(String value)
	{
		this.SetValByKey(GroupMenuAttr.FK_Group, value);
	}
	/** 
	 是否选中
	 
	*/
	public final String getIsChecked()
	{
		return this.GetValStringByKey(GroupMenuAttr.IsChecked);
	}
	public final void setIsChecked(String value)
	{
		this.SetValByKey(GroupMenuAttr.IsChecked, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 权限组菜单
	 
	*/
	public GroupMenu()
	{
	}
	/** 
	 权限组菜单
	 
	 @param mypk
	 * @throws Exception 
	*/
	public GroupMenu(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 权限组菜单
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("权限组菜单");
		map.setEnType(EnType.Sys);

		map.AddTBStringPK(GroupMenuAttr.FK_Group, null, "权限组", false, false, 0, 50, 20);
		map.AddTBStringPK(GroupMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddBoolean(GroupMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}