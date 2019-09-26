package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 岗位菜单
*/
public class StationMenu extends EntityMM
{

		///#region 属性
	/** 
	 菜单
	 * @throws Exception 
	*/
	public final String getFK_Menu() throws Exception
	{
		return this.GetValStringByKey(StationMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value) throws Exception
	{
		this.SetValByKey(StationMenuAttr.FK_Menu, value);
	}
	/** 
	 岗位
	 * @throws Exception 
	*/
	public final String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(StationMenuAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		this.SetValByKey(StationMenuAttr.FK_Station, value);
	}
	/** 
	 是否选中
	 * @throws Exception 
	*/
	public final String getIsChecked() throws Exception
	{
		return this.GetValStringByKey(StationMenuAttr.IsChecked);
	}
	public final void setIsChecked(String value) throws Exception
	{
		this.SetValByKey(StationMenuAttr.IsChecked, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 岗位菜单
	*/
	public StationMenu()
	{
	}
	/** 
	 岗位菜单
	 
	 @param mypk
	 * @throws Exception 
	*/
	public StationMenu(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 岗位菜单
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_StationMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("岗位菜单");
		map.setEnType(EnType.Sys);

			//map.AddTBStringPK(StationMenuAttr.FK_Station, null, "岗位", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(StationMenuAttr.FK_Station, null, "岗位", new Stations(), true);
		map.AddTBStringPK(StationMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddBoolean(StationMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}