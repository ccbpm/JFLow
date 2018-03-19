package BP.GPM;

import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;

/** 
 部门岗位对应 的摘要说明。
*/
public class DeptStation extends Entity
{
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;

	}
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(DeptStationAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(DeptStationAttr.FK_Dept, value);
	}
	public final String getFK_StationT()
	{
		return this.GetValRefTextByKey(DeptStationAttr.FK_Station);
	}
	/** 
	岗位
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(DeptStationAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		SetValByKey(DeptStationAttr.FK_Station, value);
	}

	/** 
	 工作部门岗位对应
	*/
	public DeptStation()
	{
	}
	/** 
	 工作人员岗位对应
	 @param deptid 部门
	 @param stationid 岗位编号 	
	*/
	public DeptStation(String deptid, String stationid)
	{
		this.setFK_Dept(deptid);
		this.setFK_Station(stationid);
		if (this.Retrieve(DeptStationAttr.FK_Dept, this.getFK_Dept(), DeptStationAttr.FK_Station, this.getFK_Station()) == 0)
		{
			this.Insert();
		}
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

		Map map = new Map("Port_DeptStation");
		map.setEnDesc("部门岗位对应");
		map.Java_SetEnType(EnType.Dot2Dot); //实体类型，admin 系统管理员表，PowerAble 权限管理表,也是用户表,你要想把它加入权限管理里面请在这里设置。。

		map.AddTBStringPK(DeptStationAttr.FK_Dept, null, "部门", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(DeptStationAttr.FK_Station, null, "岗位", new Stations(), true);
		map.AddSearchAttr(DeptStationAttr.FK_Station);

		this.set_enMap(map);
		return this.get_enMap();
	}
}