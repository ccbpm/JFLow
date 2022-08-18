package bp.ccfast.ccmenu;

import bp.en.*;

/** 
 岗位菜单
*/
public class StationMenu extends EntityMM
{

		///#region 属性
	/** 
	 菜单
	*/
	public final String getFKMenu()
	{
		return this.GetValStringByKey(StationMenuAttr.FK_Menu);
	}
	public final void setFKMenu(String value)
	 {
		this.SetValByKey(StationMenuAttr.FK_Menu, value);
	}
	/** 
	 岗位
	*/
	public final String getFKStation()
	{
		return this.GetValStringByKey(StationMenuAttr.FK_Station);
	}
	public final void setFKStation(String value)
	 {
		this.SetValByKey(StationMenuAttr.FK_Station, value);
	}
	/** 
	 是否选中
	*/
	public final String isChecked()
	{
		return this.GetValStringByKey(StationMenuAttr.IsChecked);
	}
	public final void setChecked(String value)
	 {
		this.SetValByKey(StationMenuAttr.IsChecked, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 岗位菜单
	*/
	public StationMenu()  {
	}

	/** 
	 岗位菜单
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_StationMenu", "岗位菜单");
		map.setEnType(EnType.Sys);

			//map.AddTBStringPK(StationMenuAttr.FK_Station, null, "岗位", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(StationMenuAttr.FK_Station, null, "岗位", new bp.port.Stations(), true);
		map.AddTBStringPK(StationMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddBoolean(StationMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}