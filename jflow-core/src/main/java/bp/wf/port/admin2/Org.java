package bp.wf.port.admin2;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.FrmTree;
import bp.sys.FrmTreeAttr;
import bp.sys.FrmTrees;
import bp.sys.MapDataAttr;
import bp.sys.MapDatas;
import bp.web.*;
import bp.wf.*;
import bp.wf.port.*;
import bp.wf.template.FlowAttr;
import bp.wf.template.FlowSort;
import bp.wf.template.FlowSortAttr;
import bp.wf.template.FlowSorts;

import java.util.*;

/** 
 独立组织
*/
public class Org extends EntityNoName
{

		///属性
	/** 
	 父级组织编号
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(OrgAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(OrgAttr.ParentNo, value);
	}
	/** 
	 父级组织名称
	*/
	public final String getParentName()throws Exception
	{
		return this.GetValStrByKey(OrgAttr.ParentName);
	}
	public final void setParentName(String value) throws Exception
	{
		this.SetValByKey(OrgAttr.ParentName, value);
	}
	/** 
	 父节点编号
	*/
	public final String getAdminer()throws Exception
	{
		return this.GetValStrByKey(OrgAttr.Adminer);
	}
	public final void setAdminer(String value) throws Exception
	{
		this.SetValByKey(OrgAttr.Adminer, value);
	}
	/** 
	 管理员名称
	*/
	public final String getAdminerName()throws Exception
	{
		return this.GetValStrByKey(OrgAttr.AdminerName);
	}
	public final void setAdminerName(String value) throws Exception
	{
		this.SetValByKey(OrgAttr.AdminerName, value);
	}

		///


		///构造函数
	/** 
	 独立组织
	*/
	public Org()
	{
	}
	/** 
	 独立组织
	 
	 @param no 编号
	*/
	public Org(String no) throws Exception
	{
		super(no);
	}

		///


		///重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 Map
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Org", "独立组织");
		map.setAdjunctType(AdjunctType.None);
			// map.setEnType( EnType.View; //独立组织是一个视图.

		map.AddTBStringPK(OrgAttr.No, null, "编号(与部门编号相同)", true, false, 1, 30, 40);
		map.AddTBString(OrgAttr.Name, null, "组织名称", true, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.ParentNo, null, "父级组织编号", true, false, 0, 60, 200, true);
		map.AddTBString(OrgAttr.ParentName, null, "父级组织名称", true, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.Adminer, null, "主要管理员(创始人)", true, true, 0, 60, 200, true);
		map.AddTBString(OrgAttr.AdminerName, null, "管理员名称", true, true, 0, 60, 200, true);

		map.AddTBInt(OrgAttr.Idx, 0, "排序", true, false);

		RefMethod rm = new RefMethod();
		rm.Title = "检查正确性";
		rm.ClassMethodName = this.toString() + ".DoCheck";
			//rm.getHisAttrs().AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
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

			//只有admin管理员,才能增加二级管理员.
		if (WebUser.getNo() != null && WebUser.getNo().equals("admin") == true)
		{
				//节点绑定人员. 使用树杆与叶子的模式绑定.
			map.getAttrsOfOneVSM().AddBranchesAndLeaf(new OrgAdminers(), new bp.port.Emps(), OrgAdminerAttr.OrgNo, OrgAdminerAttr.FK_Emp, "管理员", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, WebUser.getOrgNo());
		}

			//rm = new RefMethod();
			//rm.Title = "设置二级管理员";
			//rm.Warning = "设置为子公司后，系统就会在流程树上分配一个目录节点.";
			//rm.ClassMethodName = this.ToString() + ".SetSubOrg";
			//rm.getHisAttrs().AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	public final String DeleteOrg() throws Exception
	{
		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@只有admin帐号才可以执行。";
		}

		//流程类别.
		FlowSorts fss = new FlowSorts();
		fss.Retrieve(OrgAdminerAttr.OrgNo, this.getNo());
		for (FlowSort en : fss.ToJavaList())
		{
			Flows fls = new Flows();
			fls.Retrieve(FlowAttr.FK_FlowSort, en.getNo());

			if (fls.size() != 0)
			{
				return "err@在流程目录：" + en.getName() + "有[" + fls.size() + "]个流程没有删除。";
			}
		}

		//表单类别.
		FrmTrees ftTrees = new FrmTrees();
		ftTrees.Retrieve(FrmTreeAttr.OrgNo, this.getNo());
		for (FlowSort en : fss.ToJavaList())
		{
			MapDatas mds = new MapDatas();
			mds.Retrieve(MapDataAttr.FK_FormTree, en.getNo());

			if (mds.size() != 0)
			{
				return "err@在表单目录：" + en.getName() + "有[" + mds.size() + "]个表单没有删除。";
			}
		}

		OrgAdminers oas = new OrgAdminers();
		oas.Delete(OrgAdminerAttr.OrgNo, this.getNo());

		FlowSorts fs = new FlowSorts();
		fs.Delete(OrgAdminerAttr.OrgNo, this.getNo());

		fss.Delete(OrgAdminerAttr.OrgNo, this.getNo()); //删除流程目录.
		ftTrees.Delete(FrmTreeAttr.OrgNo, this.getNo()); //删除表单目录。

		this.Delete();
		return "info@成功注销组织,请关闭窗口刷新页面.";
	}

	public final String ChangeAdminer(String adminer) throws Exception
	{
		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@非admin管理员，您无法执行该操作.";
		}


		bp.port.Emp emp = new bp.port.Emp();
		emp.setNo(adminer);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@管理员编号错误.";
		}

		String old = this.getAdminer();

		this.setAdminer(emp.getNo());
		this.setAdminerName(emp.getName());
		this.Update();

		//检查超级管理员是否存在？
		OrgAdminer oa = new OrgAdminer();
		oa.setFK_Emp(old);
		oa.setOrgNo(this.getNo());
		oa.Delete(OrgAdminerAttr.FK_Emp, old, OrgAdminerAttr.OrgNo, this.getNo());

		//插入到管理员.
		oa.setFK_Emp(emp.getNo());
		oa.Save();

		//检查超级管理员是否存在？

		return "修改成功,请关闭当前记录重新打开.";
	}

	public final String DoCheck() throws Exception
	{
		String err = "";


			///组织结构信息检查.
		//检查orgNo的部门是否存在？
		Dept dept = new Dept();
		dept.setNo(this.getNo());
		if (dept.RetrieveFromDBSources() == 0)
		{
			return "err@部门组织结构树上缺少[" + this.getNo()+ "]的部门.";
		}

		if (this.getName().equals(dept.getName()) == false)
		{
			this.setName( dept.getName());
			err += "info@部门名称与组织名称已经同步.";
		}

		Dept deptParent = new Dept();
		deptParent.setNo(this.getParentNo());
		if (deptParent.RetrieveFromDBSources() == 0)
		{
			return "err@部门组织结构树上父级缺少[" + this.getNo()+ "]的部门.";
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


			/// 组织结构信息检查.


			///检查流程树.
		bp.wf.template.FlowSort fs = new bp.wf.template.FlowSort();
		fs.setNo(this.getNo());
		if (fs.RetrieveFromDBSources() == 1)
		{
			fs.setOrgNo(this.getNo());
			fs.DirectUpdate();
		}
		else
		{
			//获得根目录节点.
			bp.wf.template.FlowSort root = new bp.wf.template.FlowSort();
			int i = root.Retrieve(FlowSortAttr.ParentNo, "0");

			//设置流程树权限.
			fs.setNo(this.getNo());
			fs.setName( this.getName());
			fs.setParentNo( root.getNo());
			fs.setOrgNo(this.getNo());
			fs.setIdx( 999);
			fs.DirectInsert();

			//创建下一级目录.
			Object tempVar = fs.DoCreateSubNode();
			FlowSort en = tempVar instanceof FlowSort ? (FlowSort)tempVar : null;

			en.setName("发文流程");
			en.setOrgNo(this.getNo());
			en.setDomain("FaWen");
			en.DirectUpdate();

			Object tempVar2 = fs.DoCreateSubNode();
			en = tempVar2 instanceof FlowSort ? (FlowSort)tempVar2 : null;
			en.setName("收文流程");
			en.setOrgNo(this.getNo());
			en.setDomain("ShouWen");
			en.DirectUpdate();


			Object tempVar3 = fs.DoCreateSubNode();
			en = tempVar3 instanceof FlowSort ? (FlowSort)tempVar3 : null;
			en.setName("业务流程");
			en.setOrgNo(this.getNo());
			en.setDomain("Work");
			en.DirectUpdate();
			Object tempVar4 = fs.DoCreateSubNode();
			en = tempVar4 instanceof FlowSort ? (FlowSort)tempVar4 : null;
			en.setName("会议流程");
			en.setOrgNo(this.getNo());
			en.setDomain("Meet");
			en.DirectUpdate();
		}

			/// 检查流程树.


			///检查表单树.
		//表单根目录.
		FrmTree ftRoot = new FrmTree();
		ftRoot.Retrieve(FlowSortAttr.ParentNo, "0");

		//设置表单树权限.
		FrmTree ft = new FrmTree();
		ft.setNo(this.getNo());
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName( this.getName());
			ft.setParentNo( ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx( 999);
			ft.DirectInsert();

			//创建两个目录.
			Object tempVar5 = ft.DoCreateSubNode();
			FrmTree mySubFT = tempVar5 instanceof FrmTree ? (FrmTree)tempVar5 : null;
			mySubFT.setName("表单目录1");
			mySubFT.setOrgNo(this.getNo());
			mySubFT.DirectUpdate();


			Object tempVar6 = ft.DoCreateSubNode();
			mySubFT = tempVar6 instanceof FrmTree ? (FrmTree)tempVar6 : null;
			mySubFT.setName("表单目录2");
			mySubFT.setOrgNo(this.getNo());
			mySubFT.DirectUpdate();

		}
		else
		{
			ft.setName( this.getName());
			ft.setParentNo( ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx( 999);
			ft.DirectUpdate();
		}

			/// 检查表单树.

		if (DataType.IsNullOrEmpty(err) == true)
		{
			return "系统正确";
		}

		//检查表单树.
		return "err@" + err;
	}
	/** 
	 设置
	 
	 @param no
	 * @throws Exception 
	*/
	public final void SetSubDeptOrgNo(String no) throws Exception
	{
		//同步当前部门与当前部门的子集部门，设置相同的orgNo.
		Depts subDepts = new Depts();
		subDepts.Retrieve(DeptAttr.ParentNo, no);
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
			SetSubDeptOrgNo(no);
		}
	}
	/** 
	 检查组织结构信息.
	 
	 @return 
	*/
	public final String CheckOrgInfo()
	{
		/*
		 * 检查内容如下：
		 * 1. 与org里面的部门是否存在？
		 */



		return "";
	}



}