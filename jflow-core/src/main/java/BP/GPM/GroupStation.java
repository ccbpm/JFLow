package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 权限组岗位
*/
public class GroupStation extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_Station()
	{
		return this.GetValStringByKey(GroupStationAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		this.SetValByKey(GroupStationAttr.FK_Station, value);
	}
	public final String getFK_Group()
	{
		return this.GetValStringByKey(GroupStationAttr.FK_Group);
	}
	public final void setFK_Group(String value)
	{
		this.SetValByKey(GroupStationAttr.FK_Group, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 权限组岗位
	*/
	public GroupStation()
	{
	}
	/** 
	 权限组岗位
	 
	 @param mypk
	*/
	public GroupStation(String no)
	{
		this.Retrieve();
	}
	/** 
	 权限组岗位
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupStation");
		map.DepositaryOfEntity = Depositary.None;
		map.DepositaryOfMap = Depositary.Application;
		map.EnDesc = "权限组岗位";
		map.EnType = EnType.Sys;

		map.AddTBStringPK(GroupStationAttr.FK_Group, null, "权限组", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupStationAttr.FK_Station, null, "岗位", new Stations(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}