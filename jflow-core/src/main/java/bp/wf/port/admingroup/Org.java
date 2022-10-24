package bp.wf.port.admingroup;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.web.*;
import bp.wf.port.Dept;
import bp.wf.port.DeptAttr;
import bp.wf.port.Depts;
import bp.wf.port.admin2group.*;
import bp.*;
import bp.wf.*;
import bp.wf.port.*;
import java.util.*;

/** 
 独立组织
*/
public class Org extends EntityNoName
{

		///#region 属性
	/** 
	 父级组织编号
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(OrgAttr.ParentNo);
	}
	public final void setParentNo(String value)  throws Exception
	 {
		this.SetValByKey(OrgAttr.ParentNo, value);
	}
	/** 
	 父级组织名称
	*/
	public final String getParentName() throws Exception
	{
		return this.GetValStrByKey(OrgAttr.ParentName);
	}
	public final void setParentName(String value)  throws Exception
	 {
		this.SetValByKey(OrgAttr.ParentName, value);
	}
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
	public Org()  {
	}
	/** 
	 独立组织
	 
	 param no 编号
	*/
	public Org(String no) throws Exception {
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
		/*if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			uac.IsUpdate = true;
			uac.IsInsert = true;
			return uac;
		}*/
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
		map.setAdjunctType(AdjunctType.None);
			// map.EnType = EnType.View; //独立组织是一个视图.

		map.AddTBStringPK(OrgAttr.No, null, "编号(与部门编号相同)", true, false, 1, 30, 40);
		map.AddTBString(OrgAttr.Name, null, "组织名称", true, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.ParentNo, null, "父级组织编号", false, false, 0, 60, 200, true);
		map.AddTBString(OrgAttr.ParentName, null, "父级组织名称", false, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.Adminer, null, "主要管理员(创始人)", true, true, 0, 60, 200, true);
		map.AddTBString(OrgAttr.AdminerName, null, "管理员名称", true, true, 0, 60, 200, true);

		map.AddTBInt("FlowNums", 0, "流程数", true, true);
		map.AddTBInt("FrmNums", 0, "表单数", true, true);
		map.AddTBInt("Users", 0, "用户数", true, true);
		map.AddTBInt("Depts", 0, "部门数", true, true);

		map.AddTBInt("GWFS", 0, "运行中流程", true, true);
		map.AddTBInt("GWFSOver", 0, "结束的流程", true, true);

		map.AddTBInt(OrgAttr.Idx, 0, "排序", true, false);

		RefMethod rm = new RefMethod();
		rm.Title = "检查正确性";
		rm.ClassMethodName = this.toString() + ".DoCheck";
			//rm.HisAttrs.AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "修改主管理员";
		rm.ClassMethodName = this.toString() + ".ChangeAdminer";
		rm.getHisAttrs().AddTBString("adminer", null, "新主管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "取消独立组织";
		rm.ClassMethodName = this.toString() + ".DeleteOrg";
		rm.Warning = "您确定要取消独立组织吗？系统将要删除该组织以及该组织的管理员，但是不删除部门数据.";
		map.AddRefMethod(rm);

		//管理员.
		map.AddDtl(new OrgAdminers(), OrgAdminerAttr.OrgNo, null, DtlEditerModel.DtlSearch, "icon-people");

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {

		this.SetValByKey("FlowNums", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_Flow WHERE OrgNo='" + this.getNo() + "'"));
		this.SetValByKey("FrmNums", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Sys_MapData WHERE OrgNo='" + this.getNo() + "' AND No NOT like 'ND%'"));

		this.SetValByKey("Users", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Port_Emp WHERE OrgNo='" + this.getNo() + "'"));
		this.SetValByKey("Depts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Port_Dept WHERE OrgNo='" + this.getNo()+ "'"));
		this.SetValByKey("GWFS", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_GenerWorkFlow WHERE OrgNo='" + this.getNo() + "' AND WFState NOT IN(0,3,7)"));
		this.SetValByKey("GWFSOver", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_GenerWorkFlow WHERE OrgNo='" + this.getNo()+ "' AND WFState=3"));
		return super.beforeUpdateInsertAction();
	}

	protected  boolean beforeInsert() throws Exception {
		//判断当前的组织是否存在
		Org org = new Org();
		org.setNo(this.getNo());
		if (org.RetrieveFromDBSources() != 0)
			throw new Exception("err@组织编号:" + this.getNo() + "已存在,请重新输入组织编号");
		if (DataType.IsNullOrEmpty(this.getAdminer()) == false) {
			bp.port.Emp emp = new bp.port.Emp();
			emp.setNo(this.getAdminer());
			if (emp.RetrieveFromDBSources() != 0 && DataType.IsNullOrEmpty(emp.getOrgNo()) == false
					&& emp.getOrgNo().equals(this.getNo()) == false)
				throw new Exception("err@该人员:" + this.getAdminer() + "已经在其他组织中存在");
			//二级管理员不存在。插入一条信息
			if (emp.RetrieveFromDBSources() == 0) {
				//增加人员。
				emp.setName(this.getAdminerName());
				emp.setFK_Dept(this.getNo());
				emp.setOrgNo(this.getNo());
				emp.setIsPass("123");
				emp.Insert();
			}
		}

		return true;
	}
	public final String DeleteOrg() throws Exception {
		if (WebUser.getNo().equals("admin") == false)
			return "err@只有admin帐号才可以执行。";

		//流程类别.
		bp.wf.template.FlowSorts fss = new bp.wf.template.FlowSorts();
		fss.Retrieve(OrgAdminerAttr.OrgNo, this.getNo(), null);
		for (bp.wf.template.FlowSort en : fss.ToJavaList())
		{
			Flows fls = new Flows();
			fls.Retrieve(bp.wf.template.FlowAttr.FK_FlowSort, en.getNo(), null);
			if (fls.size() != 0)
				return "err@在流程目录：" + en.getName() + "有[" + fls.size() + "]个流程没有删除。";
		}

		//表单类别.
		bp.sys.FrmTrees ftTrees = new bp.sys.FrmTrees();
		ftTrees.Retrieve(bp.sys.FrmTreeAttr.OrgNo, this.getNo(), null);
		for (bp.wf.template.FlowSort en : fss.ToJavaList())
		{
			bp.sys.MapDatas mds = new bp.sys.MapDatas();
			mds.Retrieve(bp.sys.MapDataAttr.FK_FormTree, en.getNo(), null);
			if (!mds.isEmpty())
				return "err@在表单目录：" + en.getName() + "有[" + mds.size() + "]个表单没有删除。";
		}

		OrgAdminers oas = new OrgAdminers();
		oas.Delete(OrgAdminerAttr.OrgNo, this.getNo());

		bp.wf.template.FlowSorts fs = new bp.wf.template.FlowSorts();
		fs.Delete(OrgAdminerAttr.OrgNo, this.getNo());

		fss.Delete(OrgAdminerAttr.OrgNo, this.getNo()); //删除流程目录.
		ftTrees.Delete(bp.sys.FrmTreeAttr.OrgNo, this.getNo()); //删除表单目录。
		//更新到admin的组织下.
		String sqls = "UPDATE Port_Emp SET OrgNo='" + bp.web.WebUser.getOrgNo() + "' WHERE  OrgNo='" + this.getNo() + "'";
		sqls += "@UPDATE Port_Dept SET OrgNo='" + bp.web.WebUser.getOrgNo() + "' WHERE  OrgNo='" + this.getNo() + "'";
		sqls += "@UPDATE Port_DeptEmp SET OrgNo='" + bp.web.WebUser.getOrgNo() + "' WHERE  OrgNo='" + this.getNo() + "'";
		sqls += "@UPDATE Port_DeptEmpStation SET OrgNo='" + bp.web.WebUser.getOrgNo() + "' WHERE  OrgNo='" + this.getNo() + "'";
		DBAccess.RunSQLs(sqls);
		this.Delete();
		return "info@成功注销组织,请关闭窗口刷新页面.";
	}
	/// <summary>
	/// 更改管理员（admin才能操作）
	/// </summary>
	/// <param name="adminer"></param>
	/// <returns></returns>
	public final String ChangeAdminer(String adminer) throws Exception {
		if (WebUser.getNo().equals("admin") == false)
			return "err@非admin管理员，您无法执行该操作.";

		bp.port.Emp emp = new bp.port.Emp();
		emp.setUserID (adminer);
		if (emp.RetrieveFromDBSources() == 0)
			return "err@管理员编号错误.";

		String old = this.getAdminer();
		this.setAdminer(emp.getUserID());
		this.setAdminerName(emp.getName());
		this.Update();

		//检查超级管理员是否存在？
		OrgAdminer oa = new OrgAdminer();
		oa.setFK_Emp(old);
		oa.setOrgNo(this.getNo());
		oa.Delete(OrgAdminerAttr.FK_Emp, old, OrgAdminerAttr.OrgNo, this.getNo());

		//插入到管理员.
		oa.setFK_Emp(emp.getUserID());
		oa.Save();

		//检查超级管理员是否存在？
		return "修改成功,请关闭当前记录重新打开.";
	}
	/// <summary>
	/// 调用admin2Group的检查.
	/// 1. 是否出现错误.
	/// 1. 数据是否完整.
	/// </summary>
	/// <returns></returns>
	public final String DoCheck() throws Exception {
		bp.wf.port.admin2group.Org org = new bp.wf.port.admin2group.Org(this.getNo());
		return org.DoCheck();
	}

}