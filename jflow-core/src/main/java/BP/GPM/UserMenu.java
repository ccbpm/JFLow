package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 用户菜单
 
*/
public class UserMenu extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_Menu()
	{
		return this.GetValStringByKey(UserMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value)
	{
		this.SetValByKey(UserMenuAttr.FK_Menu, value);
	}
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(UserMenuAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(UserMenuAttr.FK_Emp, value);
	}
	public final String getIsChecked()
	{
		return this.GetValStringByKey(UserMenuAttr.IsChecked);
	}
	public final void setIsChecked(String value)
	{
		this.SetValByKey(UserMenuAttr.IsChecked, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 用户菜单
	 
	*/
	public UserMenu()
	{
	}
	/** 
	 用户菜单
	 
	 @param mypk
	 * @throws Exception 
	*/
	public UserMenu(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 用户菜单
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_UserMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("用户菜单");
		map.setEnType(EnType.Sys);

		map.AddTBStringPK(UserMenuAttr.FK_Emp, null, "用户", false, false, 0, 50, 20);
		map.AddTBStringPK(UserMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);

		map.AddBoolean(UserMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}