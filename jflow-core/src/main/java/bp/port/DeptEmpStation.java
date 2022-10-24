package bp.port;

import bp.en.*;


/** 
 部门岗位人员对应 的摘要说明。
*/
public class DeptEmpStation extends EntityMyPK
{
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 人员
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(DeptEmpStationAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{SetValByKey(DeptEmpStationAttr.FK_Emp, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
	}
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(DeptEmpStationAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{SetValByKey(DeptEmpStationAttr.FK_Dept, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
	}
	public final String getFK_StationT() {
			//return this.GetValRefTextByKey(DeptEmpStationAttr.FK_Station);

		return this.GetValStringByKey(DeptEmpStationAttr.FK_Station);
	}
	/** 
	岗位
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(DeptEmpStationAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{SetValByKey(DeptEmpStationAttr.FK_Station, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
	}

	public final String getOrgNo()
	{
		return this.GetValStringByKey(DeptEmpStationAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		SetValByKey(DeptEmpStationAttr.OrgNo, value);
	}
		///#endregion


		///#region 构造函数
	/** 
	 工作部门岗位人员对应
	*/
	public DeptEmpStation()  {
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_DeptEmpStation", "部门岗位人员对应");


		map.AddTBStringPK("MyPK", null, "主键MyPK", false, true, 1, 150, 10);

		map.AddTBString(DeptEmpStationAttr.FK_Dept, null, "部门", false, false, 1, 50, 1);
		map.AddTBString(DeptEmpStationAttr.FK_Station, null, "岗位", false, false, 1, 50, 1);
		map.AddTBString(DeptEmpStationAttr.FK_Emp, null, "操作员", false, false, 1, 50, 1);
		map.AddTBString(DeptEmpAttr.OrgNo, null, "组织编码", false, false, 0, 50, 50);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 更新删除前做的事情
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
		return super.beforeUpdateInsertAction();
	}
}