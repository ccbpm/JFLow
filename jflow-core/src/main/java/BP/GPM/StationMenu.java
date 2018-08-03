package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 岗位菜单
 
*/
public class StationMenu extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 菜单
	 
	*/
	public final String getFK_Menu()
	{
		return this.GetValStringByKey(StationMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value)
	{
		this.SetValByKey(StationMenuAttr.FK_Menu, value);
	}
	/** 
	 岗位
	 
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(StationMenuAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		this.SetValByKey(StationMenuAttr.FK_Station, value);
	}
	/** 
	 是否选中
	 
	*/
	public final String getIsChecked()
	{
		return this.GetValStringByKey(StationMenuAttr.IsChecked);
	}
	public final void setIsChecked(String value)
	{
		this.SetValByKey(StationMenuAttr.IsChecked, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

		map.AddTBStringPK(StationMenuAttr.FK_Station, null, "岗位", false, false, 0, 50, 20);
		map.AddTBStringPK(StationMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddBoolean(StationMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}