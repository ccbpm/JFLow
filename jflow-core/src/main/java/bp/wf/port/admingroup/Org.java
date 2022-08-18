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
			//uac.IsDelete = false;
			//uac.IsInsert = false;
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

		rm = new RefMethod();
		rm.Title = "在集团下新增组织";
		rm.ClassMethodName = this.toString() + ".AddOrg";
		rm.getHisAttrs().AddTBString("no", null, "组织编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("name", null, "组织名称", true, false, 0, 100, 100);

		rm.getHisAttrs().AddTBString("adminer", null, "管理员编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("adminName", null, "管理员名称", true, false, 0, 100, 100);

		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "把指定的部门设置为组织";
		rm.ClassMethodName = this.toString() + ".AddOrgByDept";
		rm.getHisAttrs().AddTBString("adminer", null, "部门编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发布菜单权限";
		rm.ClassMethodName = this.toString() + ".AddClearUserRegedit";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
	/** 
	 清除缓存
	 
	 @return 
	*/
	public final String AddClearUserRegedit() throws Exception {
		DBAccess.RunSQL("DELETE FROM Sys_UserRegedit WHERE OrgNo='" + WebUser.getOrgNo() + "' AND CfgKey='Menus'");
		return "执行成功.";
	}
	/** 
	 在集团下新增组织
	 
	 param orgNo
	 param orgName
	 @return 
	*/
	public final String AddOrg(String orgNo, String orgName, String adminerNo, String adminerName)throws Exception
	{

		Dept dept = new Dept();
		dept.setNo(orgNo);
		if (dept.RetrieveFromDBSources() != 0)
		{
			return "err@部门编号已经存在.";
		}
		//取出来根目录.
		dept.Retrieve(DeptAttr.ParentNo, "0");

		bp.port.Emp emp = new bp.port.Emp();
		emp.setNo(adminerNo);
		if (emp.RetrieveFromDBSources() != 0)
		{
			return "err@管理员编号已经存在.";
		}

		try
		{

			//增加部门信息.
			Dept mydept = new Dept();
			mydept.setParentNo(dept.getNo());
			mydept.setNo(orgNo);
			mydept.setName(orgName);
			mydept.setOrgNo(orgNo);
			mydept.Insert();

			//增加组织.
			Org org = new Org();
			org.Copy(mydept);
			org.setAdminer(emp.getNo());
			org.setAdminerName(emp.getName());
			org.Insert();



				///#region 创建子部门.
			mydept.setNo(DBAccess.GenerGUID(0, null, null));
			mydept.setName("部门1");
			mydept.setParentNo(orgNo);
			mydept.Insert();

			mydept.setNo(DBAccess.GenerGUID(0, null, null));
			mydept.setName("部门2");
			mydept.setParentNo(orgNo);
			mydept.Insert();

				///#endregion 创建子部门.

			//增加人员。
			emp.setName (adminerName);
			emp.setFK_Dept  (orgNo);
			emp.setOrgNo( orgNo);
			emp.setIsPass ("123");
			emp.Insert();

			//增加管理员.
			OrgAdminer oa = new OrgAdminer();
			oa.setFK_Emp(emp.getNo());
			oa.setOrgNo(org.getNo());
			oa.setMyPK(oa.getOrgNo() + "_" + emp.getName());
			oa.Insert();

			//增加流程树.
			bp.wf.template.FlowSort fs = new bp.wf.template.FlowSort();
			fs.setNo(org.getNo());
			fs.setParentNo("100");
			fs.setName(org.getName());
			fs.setOrgNo(org.getNo());
			fs.DirectInsert();
			EntityTree en = fs.DoCreateSubNode("办公流程");
			en.SetValByKey("OrgNo", org.getNo());
			en.Update();

			en = fs.DoCreateSubNode("人力资源流程");
			en.SetValByKey("OrgNo", org.getNo());
			en.Update();

			//增加表单树.
			bp.sys.FrmTree ft = new bp.sys.FrmTree();
			ft.setNo(org.getNo());
			ft.setParentNo("100");
			ft.setName(org.getName());
			ft.setOrgNo(org.getNo());
			ft.DirectInsert();

			en = ft.DoCreateSubNode("办公表单");
			en.SetValByKey("OrgNo", org.getNo());
			en.Update();

			en = ft.DoCreateSubNode("人力表单");
			en.SetValByKey("OrgNo", org.getNo());
			en.Update();

			return "增加成功...";
		}
		catch (RuntimeException ex)
		{
			//删除数据.
			dept.Delete("OrgNo", orgNo);
			emp.Delete("OrgNo", orgNo);

			return ex.getMessage();
		}
	}
	public final String AddOrgByDept(String deptNo) throws Exception {
		Dept en = new Dept();
		en.setNo(deptNo);
		if (en.RetrieveFromDBSources() == 0)
		{
			return "err@";
		}

		return "ok.";
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {

		this.SetValByKey("FlowNums", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_Flow WHERE OrgNo='" + WebUser.getOrgNo() + "'"));
		this.SetValByKey("FrmNums", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Sys_MapData WHERE OrgNo='" + WebUser.getOrgNo() + "'"));

		this.SetValByKey("Users", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Port_Emp WHERE OrgNo='" + WebUser.getOrgNo() + "'"));
		this.SetValByKey("Depts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM Port_Dept WHERE OrgNo='" + WebUser.getOrgNo() + "'"));
		this.SetValByKey("GWFS", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_GenerWorkFlow WHERE OrgNo='" + WebUser.getOrgNo() + "' AND WFState!=3"));
		this.SetValByKey("GWFSOver", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) AS a FROM WF_GenerWorkFlow WHERE OrgNo='" + WebUser.getOrgNo() + "' AND WFState=3"));

		//map.AddTBInt("FlowNums", 0, "流程数", true, true);
		//map.AddTBInt("FrmNums", 0, "表单数", true, true);
		//map.AddTBInt("Users", 0, "用户数", true, true);
		//map.AddTBInt("Depts", 0, "部门数", true, true);
		//map.AddTBInt("GWFS", 0, "运行中流程", true, true);
		//map.AddTBInt("GWFSOver", 0, "结束的流程", true, true);

		return super.beforeUpdateInsertAction();
	}

	public final String DeleteOrg() throws Exception {
		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@只有admin帐号才可以执行。";
		}

		//流程类别.
		bp.wf.template.FlowSorts fss = new bp.wf.template.FlowSorts();
		fss.Retrieve(OrgAdminerAttr.OrgNo, this.getNo(), null);
		for (bp.wf.template.FlowSort en : fss.ToJavaList())
		{
			Flows fls = new Flows();
			fls.Retrieve(bp.wf.template.FlowAttr.FK_FlowSort, en.getNo(), null);

			if (fls.size() != 0)
			{
				return "err@在流程目录：" + en.getName() + "有[" + fls.size() + "]个流程没有删除。";
			}
		}

		//表单类别.
		bp.sys.FrmTrees ftTrees = new bp.sys.FrmTrees();
		ftTrees.Retrieve(bp.sys.FrmTreeAttr.OrgNo, this.getNo(), null);
		for (bp.wf.template.FlowSort en : fss.ToJavaList())
		{
			bp.sys.MapDatas mds = new bp.sys.MapDatas();
			mds.Retrieve(bp.sys.MapDataAttr.FK_FormTree, en.getNo(), null);

			if (!mds.isEmpty())
			{
				return "err@在表单目录：" + en.getName() + "有[" + mds.size() + "]个表单没有删除。";
			}
		}

		OrgAdminers oas = new OrgAdminers();
		oas.Delete(OrgAdminerAttr.OrgNo, this.getNo());

		bp.wf.template.FlowSorts fs = new bp.wf.template.FlowSorts();
		fs.Delete(OrgAdminerAttr.OrgNo, this.getNo());

		fss.Delete(OrgAdminerAttr.OrgNo, this.getNo()); //删除流程目录.
		ftTrees.Delete(bp.sys.FrmTreeAttr.OrgNo, this.getNo()); //删除表单目录。

		this.Delete();
		return "info@成功注销组织,请关闭窗口刷新页面.";
	}

	public final String ChangeAdminer(String adminer) throws Exception {
		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@非admin管理员，您无法执行该操作.";
		}

		bp.port.Emp emp = new bp.port.Emp();
		emp.setUserID (adminer);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@管理员编号错误.";
		}

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

	public final String DoCheck() throws Exception {
		String err = "";


			///#region 组织结构信息检查.
		//检查orgNo的部门是否存在？
		Dept dept = new Dept();
		dept.setNo(this.getNo());
		if (dept.RetrieveFromDBSources() == 0)
		{
			return "err@部门组织结构树上缺少[" + this.getNo() + "]的部门.";
		}

		if (this.getName().equals(dept.getName()) == false)
		{
			this.setName(dept.getName());
			err += "info@部门名称与组织名称已经同步.";
		}

		Dept deptParent = new Dept();
		deptParent.setNo(this.getParentNo());
		if (deptParent.RetrieveFromDBSources() == 0)
		{
			return "err@部门组织结构树上父级缺少[" + this.getNo() + "]的部门.";
		}

		if (this.getParentName().equals(deptParent.getName()) == false)
		{
			this.setParentName(deptParent.getName());
			err += "info@父级部门名称与组织名称已经同步.";
		}
		this.Update(); //执行更新.

		//设置子集部门，的OrgNo.
		if (DBAccess.IsView("Port_Dept") == false)
		{
			SetSubDeptOrgNo(this.getNo());
		}


			///#endregion 组织结构信息检查.


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


			///#region 检查表单树.
		//表单根目录.
		bp.sys.FrmTree ftRoot = new bp.sys.FrmTree();
		ftRoot.Retrieve(bp.wf.template.FlowSortAttr.ParentNo, "0");

		//设置表单树权限.
		bp.sys.FrmTree ft = new bp.sys.FrmTree();
		ft.setNo(this.getNo());
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName(this.getName());
			ft.setName("表单树");
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.DirectInsert();

			//创建两个目录.
			Object tempVar5 = ft.DoCreateSubNode(null);
			bp.sys.FrmTree mySubFT = tempVar5 instanceof bp.sys.FrmTree ? (bp.sys.FrmTree)tempVar5 : null;
			mySubFT.setName("表单目录1");
			mySubFT.setOrgNo(this.getNo());
			mySubFT.DirectUpdate();


			Object tempVar6 = ft.DoCreateSubNode(null);
			mySubFT = tempVar6 instanceof bp.sys.FrmTree ? (bp.sys.FrmTree)tempVar6 : null;
			mySubFT.setName("表单目录2");
			mySubFT.setOrgNo(this.getNo());
			mySubFT.DirectUpdate();

		}
		else
		{
			ft.setName(this.getName());
			ft.setName("表单树"); //必须这个命名，否则找不到。
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.DirectUpdate();
		}

			///#endregion 检查表单树.

		if (DataType.IsNullOrEmpty(err) == true)
		{
			return "系统正确";
		}

		//检查表单树.
		return "err@" + err;
	}
	/** 
	 设置
	 
	 param no
	*/
	public final void SetSubDeptOrgNo(String no) throws Exception {
		//同步当前部门与当前部门的子集部门，设置相同的orgNo.
		Depts subDepts = new Depts();
		subDepts.Retrieve(DeptAttr.ParentNo, no, null);
		for (Dept subDept : subDepts.ToJavaList())
		{
			//判断当前部门是否是组织？
			Org org = new Org();
			org.setNo(subDept.getNo());
			if (org.RetrieveFromDBSources() == 1)
			{
				continue; //说明当前部门是组织.
			}

			subDept.setOrgNo(this.getNo());
			subDept.Update();

			//递归调用.
			SetSubDeptOrgNo(subDept.getNo());
		}
	}
	/** 
	 检查组织结构信息.
	 
	 @return 
	*/
	public final String CheckOrgInfo() throws Exception {
		/*
		 * 检查内容如下：
		 * 1. 与org里面的部门是否存在？
		 */



		return "";
	}


	//public string SetSubOrg(string userNo)
	//{
	//    BP.WF.Port.Admin2Group.Dept dept = new WF.Port.Admin2Group.Dept(this.No);
	//    return dept.SetSubOrg(userNo);
	//}

}