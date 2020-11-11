package bp.gpm;

import bp.en.*;
import bp.en.Map;
import bp.port.*;

/** 
 部门岗位对应 的摘要说明。
*/
public class DeptStation extends Entity
{
	private static final long serialVersionUID = 1L;
	///基本属性
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;

	}
	/** 
	 部门
	*/
	public final String getFK_Dept()throws Exception
	{
		return this.GetValStringByKey(DeptStationAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(DeptStationAttr.FK_Dept, value);
	}
	public final String getFK_StationT()throws Exception
	{
		return this.GetValRefTextByKey(DeptStationAttr.FK_Station);
	}
	/** 
	岗位
	*/
	public final String getFK_Station()throws Exception
	{
		return this.GetValStringByKey(DeptStationAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		SetValByKey(DeptStationAttr.FK_Station, value);
	}

		///


		///扩展属性


		///


		///构造函数
	/** 
	 工作部门岗位对应
	*/
	public DeptStation()
	{
	}
	/** 
	 工作人员岗位对应
	 
	 @param deptNo 部门
	 @param stationNo 岗位编号 	
	 * @throws Exception 
	*/
	public DeptStation(String deptNo, String stationNo) throws Exception
	{
		this.setFK_Dept(deptNo);
		this.setFK_Station(stationNo);
		if (this.Retrieve(DeptStationAttr.FK_Dept, this.getFK_Dept(), DeptStationAttr.FK_Station, this.getFK_Station()) == 0)
		{
			this.Insert();
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_DeptStation", "部门岗位对应");
		map.setEnType(EnType.Dot2Dot); //实体类型，admin 系统管理员表，PowerAble 权限管理表,也是用户表,你要想把它加入权限管理里面请在这里设置。。

		map.AddTBStringPK(DeptStationAttr.FK_Dept, null, "部门", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(DeptStationAttr.FK_Station, null, "岗位", new Stations(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}