package bp.ccfast.ccmenu;

import bp.en.*;

/** 
 权限组岗位
*/
public class GroupStation extends EntityMM
{

		///#region 属性
	public final String getFKStation()
	{
		return this.GetValStringByKey(GroupStationAttr.FK_Station);
	}
	public final void setFKStation(String value)
	 {
		this.SetValByKey(GroupStationAttr.FK_Station, value);
	}
	public final String getFKGroup()
	{
		return this.GetValStringByKey(GroupStationAttr.FK_Group);
	}
	public final void setFKGroup(String value)
	 {
		this.SetValByKey(GroupStationAttr.FK_Group, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 权限组岗位
	*/
	public GroupStation()  {
	}

	/** 
	 权限组岗位
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupStation", "权限组岗位");
		map.setEnType(EnType.Sys);

		map.AddTBStringPK(GroupStationAttr.FK_Group, null, "权限组", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupStationAttr.FK_Station, null, "岗位", new bp.port.Stations(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}