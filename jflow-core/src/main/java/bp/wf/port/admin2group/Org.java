package bp.wf.port.admin2group;

import bp.da.*;
import bp.en.*;
import bp.wf.template.SysFormTree;
import bp.wf.template.SysFormTrees;
import bp.web.*;

/** 
 独立组织
*/
public class Org extends EntityNoName
{
		///#region 属性
	/**
	 父节点编号
	*/
	public final String getAdminer() throws Exception
	{
		return this.GetValStrByKey(OrgAttr.Adminer);
	}
	public final void setAdminer(String value)  throws Exception
	 {
		this.SetValByKey(OrgAttr.Adminer, value);
	}
	/** 
	 管理员名称
	*/
	public final String getAdminerName() throws Exception
	{
		return this.GetValStrByKey(OrgAttr.AdminerName);
	}
	public final void setAdminerName(String value)  throws Exception
	 {
		this.SetValByKey(OrgAttr.AdminerName, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 独立组织
	*/
	public Org() {
	}
	/** 
	 独立组织
	 
	 param no 编号
	*/
	public Org(String no)
	{
		super(no);
	}

		///#endregion


		///#region 重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Org", "独立组织");
			// map.EnType = EnType.View; //独立组织是一个视图.

		map.AddTBStringPK(OrgAttr.No, null, "编号(与部门编号相同)", true, false, 1, 50, 40);
		map.AddTBString(OrgAttr.Name, null, "组织名称", true, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.Adminer, null, "主要管理员(创始人)", true, true,
				0, 60, 200, true);
		map.AddTBString(OrgAttr.AdminerName, null, "管理员名称", true, true, 0, 60, 200, true);

		map.AddTBInt("FlowNums", 0, "流程数", true, true);
		map.AddTBInt("FrmNums", 0, "表单数", true, true);
		map.AddTBInt("Users", 0, "用户数", true, true);
		map.AddTBInt("Depts", 0, "部门数", true, true);

		map.AddTBInt("GWFS", 0, "运行中流程", true, true);
		map.AddTBInt("GWFSOver", 0, "结束的流程", true, true);

		map.AddTBInt(OrgAttr.Idx, 0, "排序", true, false);

		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "检查正确性";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.Icon = "icon-check";
		//rm.HisAttrs.AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "增加管理员";
		rm.Icon = "icon-user";
		rm.ClassMethodName = this.toString() + ".AddAdminer";
		rm.getHisAttrs().AddTBString("adminer", null, "管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);
		//管理员.
		map.AddDtl(new OrgAdminers(), OrgAdminerAttr.OrgNo, null, DtlEditerModel.DtlSearch, "icon-people");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
	public String DoOrganization()
	{
		return "/GPM/Organization.htm";
	}
	public String DoEmps()
	{
		return "/WF/Comm/Search.htm?EnsName=BP.Port.Emps";
	}

	public String DoStationTypes()
	{
		return "/WF/Comm/Ens.htm?EnsName=BP.Port.StationTypes";
	}
	public String DoStations()
	{
		return "/WF/Comm/Search.htm?EnsName=BP.Port.Stations";
	}
	/** 
	 清除缓存
	 
	 @return 
	*/
	public final String AddClearUserRegedit() throws Exception {
		DBAccess.RunSQL("DELETE FROM Sys_UserRegedit WHERE OrgNo='" + this.getNo() + "' AND CfgKey='Menus'");
		return "执行成功.";
	}


	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {

		this.SetValByKey("FlowNums", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_Flow WHERE OrgNo='" + this.getNo() + "'"));
		this.SetValByKey("FrmNums", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Sys_MapData WHERE OrgNo='" + this.getNo() + "' AND No NOT like 'ND%'"));

		this.SetValByKey("Users", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Port_Emp WHERE OrgNo='" + this.getNo() + "'"));
		this.SetValByKey("Depts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Port_Dept WHERE OrgNo='" + this.getNo() + "'"));
		this.SetValByKey("GWFS", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_GenerWorkFlow WHERE OrgNo='" + this.getNo() + "' AND WFState NOT IN(0,3,7)"));
		this.SetValByKey("GWFSOver", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_GenerWorkFlow WHERE OrgNo='" + this.getNo() + "' AND WFState=3"));
		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterUpdate() throws Exception {
		Dept dept = new Dept(this.getNo());
		dept.setName(this.getName());
		dept.Update();
		super.afterUpdate();
	}

	public final String AddAdminer(String adminer) throws Exception {

		bp.port.Emp emp = new bp.port.Emp();
		emp.setNo(adminer);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@管理员编号错误.";
		}

		//检查超级管理员是否存在？
		OrgAdminer oa = new OrgAdminer();
		oa.setFK_Emp(adminer);
		oa.setEmpName(emp.getName());

		oa.setOrgNo(this.getNo());
		oa.setMyPK(  this.getNo() + "_" + oa.getFK_Emp());
		if (oa.RetrieveFromDBSources() == 1)
			return "err@管理员已经存在.";
		oa.Delete(OrgAdminerAttr.FK_Emp, adminer, OrgAdminerAttr.OrgNo, this.getNo());

		//插入到管理员.
		oa.setFK_Emp(emp.getUserID());
		oa.Save();

		//如果不在同一个组织.就给他一个兼职部门.
		bp.port.DeptEmps depts = new bp.port.DeptEmps();
		depts.Retrieve("OrgNo", this.getNo(), "FK_Emp", adminer);
		if (depts.size() == 0)
		{
			bp.port.DeptEmp de = new bp.port.DeptEmp();
			de.setFK_Dept(this.getNo());
			de.setFK_Emp(adminer);
			de.setMyPK(this.getNo() + "_" + adminer);
			de.setOrgNo(this.getNo());
			de.Save();
		}

		//检查超级管理员是否存在？
		return "管理员增加成功,请关闭当前记录重新打开,请给管理员[" + emp.getNo() + "," + emp.getName() + "]分配权限";
	}
	private void SetOrgNo(String deptNo) throws Exception {
		DBAccess.RunSQL("UPDATE Port_Emp SET OrgNo='" + this.getNo() + "' WHERE FK_Dept='" + deptNo + "'");
		DBAccess.RunSQL("UPDATE Port_DeptEmp SET OrgNo='" + this.getNo() + "' WHERE FK_Dept='" + deptNo + "'");
		DBAccess.RunSQL("UPDATE Port_DeptEmpStation SET OrgNo='" + this.getNo() + "' WHERE FK_Dept='" + deptNo + "'");

		Depts depts = new Depts();
		depts.Retrieve(DeptAttr.ParentNo, deptNo);
		String sql="";
		for (Dept item : depts.ToJavaList())
		{
			//如果部门下组织不能检查更新
			sql="SELECT COUNT(*) From Port_Org Where No='"+item.getNo()+"'";
			if(DBAccess.RunSQLReturnValInt(sql)==1)
				continue;
			DBAccess.RunSQL("UPDATE Port_Emp SET OrgNo='" + this.getNo() + "' WHERE FK_Dept='" + item.getNo() + "'");
			DBAccess.RunSQL("UPDATE Port_DeptEmp SET OrgNo='" + this.getNo() + "' WHERE FK_Dept='" + item.getNo() + "'");
			DBAccess.RunSQL("UPDATE Port_DeptEmpStation SET OrgNo='" + this.getNo() + "' WHERE FK_Dept='" + item.getNo() + "'");
			if (item.getOrgNo().equals(this.getNo()) == false)
			{
				item.setOrgNo(this.getNo());
				item.Update();
			}
			//递归调用.
			SetOrgNo(item.getNo());
		}
	}
	public final String DoCheck() throws Exception {
		String err = "";

			///#region 组织结构信息检查.
		//检查orgNo的部门是否存在？
		Dept dept = new Dept();
		dept.setNo(this.getNo());
		if (dept.RetrieveFromDBSources() == 0)
			return "err@部门组织结构树上缺少[" + this.getNo() + "]的部门.";

		if (this.getName().equals(dept.getName()) == false)
		{
			this.setName(dept.getName());
			err += "info@部门名称与组织名称已经同步.";
		}
		this.Update(); //执行更新.

		//设置子集部门，的OrgNo.
		if (DBAccess.IsView("Port_Dept") == false)
		{
			this.SetOrgNo(this.getNo());
		}

			///#region 检查流程树.
		bp.wf.template.FlowSort fs = new bp.wf.template.FlowSort();
		fs.setNo(this.getNo());
		if (fs.RetrieveFromDBSources() == 1)
		{
			fs.setOrgNo(this.getNo());
			fs.setName("流程树");
			fs.DirectUpdate();
		}
		else
		{
			//获得根目录节点.
			bp.wf.template.FlowSort root = new bp.wf.template.FlowSort();
			int i = root.Retrieve(bp.wf.template.FlowSortAttr.ParentNo, "0");

			//设置流程树权限.
			fs.setNo(this.getNo());
			fs.setName(this.getName());
			fs.setName("流程树");
			fs.setParentNo(root.getNo());
			fs.setOrgNo(this.getNo());
			fs.setIdx(999);
			fs.DirectInsert();

			//创建下一级目录.
			bp.en.EntityTree tempVar = fs.DoCreateSubNode(null);
			bp.wf.template.FlowSort en = tempVar instanceof bp.wf.template.FlowSort ? (bp.wf.template.FlowSort)tempVar : null;

			en.setName("发文流程");
			en.setOrgNo(this.getNo());
			en.setDomain("FaWen");
			en.DirectUpdate();

			bp.en.EntityTree tempVar2 = fs.DoCreateSubNode(null);
			en = tempVar2 instanceof bp.wf.template.FlowSort ? (bp.wf.template.FlowSort)tempVar2 : null;
			en.setName("收文流程");
			en.setOrgNo(this.getNo());
			en.setDomain("ShouWen");
			en.DirectUpdate();

			bp.en.EntityTree tempVar3 = fs.DoCreateSubNode(null);
			en = tempVar3 instanceof bp.wf.template.FlowSort ? (bp.wf.template.FlowSort)tempVar3 : null;
			en.setName("业务流程");
			en.setOrgNo(this.getNo());
			en.setDomain("Work");
			en.DirectUpdate();
			bp.en.EntityTree tempVar4 = fs.DoCreateSubNode(null);
			en = tempVar4 instanceof bp.wf.template.FlowSort ? (bp.wf.template.FlowSort)tempVar4 : null;
			en.setName("会议流程");
			en.setOrgNo(this.getNo());
			en.setDomain("Meet");
			en.DirectUpdate();
		}
		///#endregion 检查流程树.
		//  #region 检查表单树.
		//表单根目录.
		bp.wf.template.SysFormTree ftRoot = new SysFormTree();
		int val = ftRoot.Retrieve(bp.wf.template.FlowSortAttr.ParentNo, "0");
		if (val == 0)
		{
			val = ftRoot.Retrieve(bp.wf.template.FlowSortAttr.No, "100");
			if (val == 0)
			{
				ftRoot.setNo( "100");
				ftRoot.setName("表单库");
				ftRoot.setParentNo( "0");
				ftRoot.Insert();

			}
			else
			{
				ftRoot.setParentNo("0");
				ftRoot.setName("表单库");
				ftRoot.Update();
			}
		}

		//设置表单树权限.
		bp.wf.template.SysFormTree ft = new bp.wf.template.SysFormTree();
		ft.setNo(this.getNo());
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName(this.getName());
			ft.setName("表单树(" + this.getName() + ")");
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.DirectInsert();

			//创建两个目录.
			bp.wf.template.SysFormTree mySubFT = (SysFormTree)ft.DoCreateSubNode();
			mySubFT.setName( "表单目录1");
			mySubFT.setOrgNo(this.getNo());
			mySubFT.DirectUpdate();

			mySubFT = (SysFormTree)ft.DoCreateSubNode();
			mySubFT.setName("表单目录2");
			mySubFT.setOrgNo(this.getNo());
			mySubFT.DirectUpdate();
		}
		else
		{
			ft.setName(this.getName());
			ft.setName("表单树(" + this.getName() + ")"); //必须这个命名，否则找不到。
			ft.setParentNo (ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx (999);
			ft.DirectUpdate();

			//检查数量.
			bp.wf.template.SysFormTrees frmSorts = new SysFormTrees();
			frmSorts.Retrieve("OrgNo", this.getNo());
			if (frmSorts.size() <= 1)
			{
				//创建两个目录.
				bp.wf.template.SysFormTree mySubFT = (SysFormTree)ft.DoCreateSubNode();
				mySubFT.setName("表单目录1");
				mySubFT.setOrgNo( this.getNo());
				mySubFT.DirectUpdate();

				mySubFT = (SysFormTree)ft.DoCreateSubNode();
				mySubFT.setName ("表单目录2");
				mySubFT.setOrgNo(this.getNo());
				mySubFT.DirectUpdate();
			}
		}
           // #endregion 检查表单树.

		//  #region 删除无效的数据.
		String sqls = "";
		if (DBAccess.IsView("Port_DeptEmp") == false)
		{
			sqls += "@DELETE FROM Port_DeptEmp WHERE FK_Dept not in (select no from port_dept)";
			sqls += "@DELETE FROM Port_DeptEmp WHERE FK_Emp not in (select no from port_Emp)";
		}
		if (DBAccess.IsView("Port_DeptEmpStation") == false)
		{
			sqls += "@DELETE FROM Port_DeptEmpStation WHERE FK_Dept not in (select no from port_dept)";
			sqls += "@DELETE FROM Port_DeptEmpStation WHERE FK_Emp not in (select no from port_Emp)";
			sqls += "@DELETE FROM Port_DeptEmpStation WHERE FK_Station not in (select no from port_Station)";
		}
		//删除无效的管理员,
		if (DBAccess.IsView("Port_OrgAdminer") == false)
		{
			sqls += "@DELETE from Port_OrgAdminer where OrgNo not in (select No from port_dept)";
			sqls += "@DELETE from Port_OrgAdminer where FK_Emp not in (select No from port_emp)";
		}
		//删除无效的组织.
		if (DBAccess.IsView("Port_Org") == false)
			sqls += "@DELETE from Port_Org where No not in (select No from port_dept)";
		DBAccess.RunSQLs(sqls);
          //  #endregion 删除无效的数据.

          //  #region 检查人员信息..
		/*String sql = "SELECT * FROM Port_Emp WHERE OrgNo NOT IN (SELECT No from Port_Dept )";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
			err += " 人员表里有:" + dt.Rows.size() + "笔 组织编号有丢失. 请处理:" + sql;

		sql = "SELECT * FROM Port_Emp WHERE FK_DEPT NOT IN (SELECT No from Port_Dept )";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
			err += " 人员表里有:" + dt.Rows.size() + "笔数据部门编号丢失. 请处理:" + sql;*/
          //  #endregion 检查组织编号信息.
			///#endregion 检查表单树.

		/*if (DataType.IsNullOrEmpty(err) == true)
			return "系统正确";*/
		//检查表单树.
		return "";
	}


}