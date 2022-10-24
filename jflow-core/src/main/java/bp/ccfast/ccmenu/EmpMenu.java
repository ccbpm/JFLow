package bp.ccfast.ccmenu;

import bp.en.*;

/** 
 人员菜单功能
*/
public class EmpMenu extends EntityMM
{

		///#region 属性
	/** 
	 人员
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	 {
		this.SetValByKey(EmpMenuAttr.FK_Emp, value);
	}
	/** 
	 菜单
	*/
	public final String getFKMenu()
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Menu);
	}
	public final void setFKMenu(String value)
	 {
		this.SetValByKey(EmpMenuAttr.FK_Menu, value);
	}
	/** 
	 是否选中
	*/
	public final boolean isChecked()
	{
		return this.GetValBooleanByKey(EmpMenuAttr.IsChecked);
	}
	public final void setChecked(boolean value)
	 {
		this.SetValByKey(EmpMenuAttr.IsChecked, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 人员菜单功能
	*/
	public EmpMenu()  {
	}
	/** 
	 人员菜单功能
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_EmpMenu", "人员菜单对应");

			// map.AddTBStringPK(EmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 3900, 20);
		map.AddTBStringPK(EmpMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(EmpMenuAttr.FK_Emp, null, "菜单功能", new bp.port.Emps(), true);
		map.AddTBString(EmpMenuAttr.FK_App, null, "系统编号", false, false, 0, 50, 20);

		map.AddBoolean(EmpMenuAttr.IsChecked, true, "是否选中", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}