package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.*;
import BP.WF.*;
import BP.WF.Port.*;
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
	public final void setParentName(String value)throws Exception
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
	public final void setAdminer(String value)throws Exception
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
	public final void setAdminerName(String value)throws Exception
	{
		this.SetValByKey(OrgAttr.AdminerName, value);
	}
		///#endregion

		///#region 构造函数
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
		///#endregion

		///#region 重写方法
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
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Org", "独立组织");
		map.setAdjunctType(AdjunctType.None);
			// map.EnType = EnType.View; //独立组织是一个视图.

		map.AddTBStringPK(OrgAttr.No, null, "编号(与部门编号相同)", true, false, 1, 30, 40);
		map.AddTBString(OrgAttr.Name, null, "组织名称", true, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.ParentNo, null, "父级组织编号", true, false, 0, 60, 200, true);
		map.AddTBString(OrgAttr.ParentName, null, "父级组织名称", true, false, 0, 60, 200, true);

		map.AddTBString(OrgAttr.Adminer, null, "管理员登录帐号", true, true, 0, 60, 200, true);
		map.AddTBString(OrgAttr.AdminerName, null, "管理员名称", true, true, 0, 60, 200, true);

		RefMethod rm = new RefMethod();
		rm.Title = "检查正确性";
		rm.ClassMethodName = this.toString() + ".DoCheck";
			//rm.HisAttrs.AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "设置二级管理员";
			//rm.Warning = "设置为子公司后，系统就会在流程树上分配一个目录节点.";
			//rm.ClassMethodName = this.ToString() + ".SetSubOrg";
			//rm.HisAttrs.AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion

	public final String DoCheck() throws Exception
	{
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
		BP.WF.Template.FlowSort fs = new BP.WF.Template.FlowSort();
		fs.setNo(this.getNo());
		if (fs.RetrieveFromDBSources() == 1)
		{
			fs.setOrgNo(this.getNo());
			fs.Update();
		}
		else
		{
			//获得根目录节点.
			BP.WF.Template.FlowSort root = new BP.WF.Template.FlowSort();
			int i = root.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");

			//设置流程树权限.
			fs.setNo(this.getNo());
			fs.setName(this.getName());
			fs.setParentNo(root.getNo());
			fs.setOrgNo(this.getNo());
			fs.setIdx(999);
			fs.Insert();

			//创建下一级目录.
			BP.En.EntityTree tempVar = fs.DoCreateSubNode();
			BP.WF.Template.FlowSort en = tempVar instanceof BP.WF.Template.FlowSort ? (BP.WF.Template.FlowSort)tempVar : null;
			en.setName("公文类");
			en.setOrgNo(this.getNo());
			en.setDoDomain("GongWen");
			en.Update();

			BP.En.EntityTree tempVar2 = fs.DoCreateSubNode();
			en = tempVar2 instanceof BP.WF.Template.FlowSort ? (BP.WF.Template.FlowSort)tempVar2 : null;
			en.setName("办公类");
			en.setOrgNo(this.getNo());
			en.Update();

			BP.En.EntityTree tempVar3 = fs.DoCreateSubNode();
			en = tempVar3 instanceof BP.WF.Template.FlowSort ? (BP.WF.Template.FlowSort)tempVar3 : null;
			en.setName("财务类");
			en.setOrgNo(this.getNo());
			en.Update();

			BP.En.EntityTree tempVar4 = fs.DoCreateSubNode();
			en = tempVar4 instanceof BP.WF.Template.FlowSort ? (BP.WF.Template.FlowSort)tempVar4 : null;
			en.setName("人力资源类");
			en.setOrgNo(this.getNo());
			en.Update();

		}
			///#endregion 检查流程树.

			///#region 检查表单树.
		//表单根目录.
		BP.Sys.FrmTree ftRoot = new BP.Sys.FrmTree();
		ftRoot.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");

		//设置表单树权限.
		BP.Sys.FrmTree ft = new BP.Sys.FrmTree();
		ft.setNo(this.getNo());
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName(this.getName());
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.Insert();

			//创建两个目录.
			ft.DoCreateSubNode();
			ft.DoCreateSubNode();
		}
		else
		{
			ft.setName(this.getName());
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.Update();
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
	 
	 @param no
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


	//public string SetSubOrg(string userNo)
	//{
	//    BP.WF.Port.Admin2.Dept dept = new WF.Port.Admin2.Dept(this.No);
	//    return dept.SetSubOrg(userNo);
	//}

}