package bp.ccfast.ccmenu;

import bp.en.*;
import bp.en.Map;

import java.util.*;

/** 
 部门菜单
*/
public class DeptMenu extends EntityMM
{

		///#region 属性
	/** 
	 菜单
	*/
	public final String getFKMenu() {
		return this.GetValStringByKey(DeptMenuAttr.FK_Menu);
	}
	public final void setFKMenu(String value)  {
		this.SetValByKey(DeptMenuAttr.FK_Menu, value);
	}
	/** 
	 部门
	*/
	public final String getFK_Dept()  {
		return this.GetValStringByKey(DeptMenuAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) {
		this.SetValByKey(DeptMenuAttr.FK_Dept, value);
	}
	/** 
	 是否选中
	*/
	public final String isChecked() {
		return this.GetValStringByKey(DeptMenuAttr.IsChecked);
	}
	public final void setChecked(String value) {
		this.SetValByKey(DeptMenuAttr.IsChecked, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 部门菜单
	*/
	public DeptMenu()
	{
	}

	/** 
	 部门菜单
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_DeptMenu", "部门菜单");
		map.setEnType(EnType.Sys);

			//map.AddTBStringPK(DeptMenuAttr.FK_Station, null, "部门", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(DeptMenuAttr.FK_Dept, null, " 部门", new bp.port.Stations(), true);
		map.AddTBStringPK(DeptMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddBoolean(DeptMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}