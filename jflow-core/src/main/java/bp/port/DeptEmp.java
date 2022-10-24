package bp.port;

import bp.en.*;

/** 
 部门人员信息 的摘要说明。
*/
public class DeptEmp extends EntityMyPK
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
		return this.GetValStringByKey(DeptEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{SetValByKey(DeptEmpAttr.FK_Emp, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp());
	}
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(DeptEmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{SetValByKey(DeptEmpAttr.FK_Dept, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp());
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(DeptEmpAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{SetValByKey(DeptEmpAttr.OrgNo, value);
	}


	/**
	 工作部门人员信息
	*/
	public DeptEmp()  {
	}
	/**
	 查询

	 param deptNo 部门编号
	 param empNo 人员编号
	*/
	public DeptEmp(String deptNo, String empNo) throws Exception {
		this.setFK_Dept(deptNo);
		this.setFK_Emp(empNo);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp());
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
			return this.get_enMap();

		Map map = new Map("Port_DeptEmp", "部门人员信息");
		map.IndexField = DeptEmpAttr.FK_Dept;


		map.AddMyPK();
		map.AddTBString(DeptEmpAttr.FK_Dept, null, "部门", false, false, 1, 50, 1);
		map.AddDDLEntities(DeptEmpAttr.FK_Emp, null, "操作员", new bp.port.Emps(), false);
		map.AddTBString(DeptEmpAttr.OrgNo, null, "组织编码", false, false, 0, 50, 50);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
	/**
	 更新前做的事情
	 return
	*/
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp());
		return super.beforeUpdateInsertAction();
	}
}