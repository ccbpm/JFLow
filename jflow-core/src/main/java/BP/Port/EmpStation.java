package BP.Port;

import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;

/**
 * 人员岗位 的摘要说明。
 */
public class EmpStation extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 基本属性
	/**
	 * UI界面上的访问控制
	 */
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
		
	}
	
	/**
	 * 工作人员ID
	 */
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(EmpStationAttr.FK_Emp);
	}
	
	public final void setFK_Emp(String value)
	{
		SetValByKey(EmpStationAttr.FK_Emp, value);
	}
	
	public final String getFK_StationT()
	{
		return this.GetValRefTextByKey(EmpStationAttr.FK_Station);
	}
	
	/**
	 * 工作岗位
	 */
	public final String getFK_Station()
	{
		return this.GetValStringByKey(EmpStationAttr.FK_Station);
	}
	
	public final void setFK_Station(String value)
	{
		SetValByKey(EmpStationAttr.FK_Station, value);
	}
	
	// 构造函数
	/**
	 * 工作人员岗位
	 */
	public EmpStation()
	{
	}
	
	/**
	 * 工作人员工作岗位对应
	 * 
	 * @param fk_emp
	 *            工作人员ID
	 * @param fk_station
	 *            工作岗位编号
	 */
	public EmpStation(String fk_emp, String fk_station)
	{
		this.setFK_Emp(fk_emp);
		this.setFK_Station(fk_station);
		this.Retrieve();
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("Port_EmpStation");
		map.setEnDesc("人员岗位");
		map.setEnType(EnType.Dot2Dot);
		
		map.AddDDLEntitiesPK(EmpStationAttr.FK_Emp, null, "操作员", new Emps(),
				true);
		map.AddDDLEntitiesPK(EmpStationAttr.FK_Station, null, "工作岗位",
				new Stations(), true);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}