package BP.WF.MS;

import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;

/** 
 职责与权限
*/
public class DutyAndPower extends EntityNoName
{
		
	/** 
	 制度编号
	*/
	public final String getFK_Main()
	{
		return this.GetValStringByKey(DutyAndPowerAttr.FK_Main);
	}
	public final void setFK_Main(String value)
	{
		this.SetValByKey(DutyAndPowerAttr.FK_Main, value);
	}
	/** 
	 岗位
	*/
	public final String getStationName()
	{
		return this.GetValStringByKey(DutyAndPowerAttr.StationName);
	}
	public final void setStationName(String value)
	{
		this.SetValByKey(DutyAndPowerAttr.StationName, value);
	}
	/** 
	 职责
	*/
	public final String getDuty()
	{
		return this.GetValStringByKey(DutyAndPowerAttr.Duty);
	}
	public final void setDuty(String value)
	{
		this.SetValByKey(DutyAndPowerAttr.Duty, value);
	}
	/** 
	 权限
	*/
	public final String getPowerOfRight()
	{
		return this.GetValStringByKey(DutyAndPowerAttr.PowerOfRight);
	}
	public final void setPowerOfRight(String value)
	{
		this.SetValByKey(DutyAndPowerAttr.PowerOfRight, value);
	}
		///#endregion

		
	/** 
	 Main
	*/
	public DutyAndPower()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("MS_DutyAndPower", "职责与权限");
		map.Java_SetEnType(EnType.Admin);

		map.AddTBStringPK(DutyAndPowerAttr.No, null, "编号", true, true, 10, 10, 10);
		map.AddTBString(DutyAndPowerAttr.Name, null, "名称", true, true, 0, 500, 5);
		map.AddTBString(DutyAndPowerAttr.FK_Main, null, "制度编号", true, true, 0, 200, 4);
		map.AddTBString(DutyAndPowerAttr.StationName, null, "岗位", true, true, 0, 500, 4);
		map.AddTBString(DutyAndPowerAttr.Duty, null, "职责", true, true, 0, 1000, 4);
		map.AddTBString(DutyAndPowerAttr.PowerOfRight, null, "权限", true, true, 0, 1000, 4);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}