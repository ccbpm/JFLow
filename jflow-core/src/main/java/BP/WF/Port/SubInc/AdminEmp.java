package BP.WF.Port.SubInc;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 管理员
*/
public class AdminEmp extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	public final boolean getIsAdmin()
	{
		if (this.No.equals("admin"))
		{
			return true;
		}

		if (this.getUserType() == 1 && this.getUseSta() == 1)
		{
			return true;
		}

		return false;
	}

	/** 
	 用户状态
	*/
	public final int getUseSta()
	{
		return this.GetValIntByKey(AdminEmpAttr.UseSta);
	}
	public final void setUseSta(int value)
	{
		SetValByKey(AdminEmpAttr.UseSta, value);
	}
	/** 
	 用户类型
	*/
	public final int getUserType()
	{
		return this.GetValIntByKey(AdminEmpAttr.UserType);
	}
	public final void setUserType(int value)
	{
		SetValByKey(AdminEmpAttr.UserType, value);
	}
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(AdminEmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(AdminEmpAttr.FK_Dept, value);
	}
	/** 
	 组织结构
	*/
	public final String getOrgNo()
	{
		return this.GetValStringByKey(AdminEmpAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		SetValByKey(AdminEmpAttr.OrgNo, value);
	}
	public final String getRootOfDept()
	{
		if (this.No.equals("admin"))
		{
			return "0";
		}

		return this.GetValStringByKey(AdminEmpAttr.RootOfDept);
	}
	public final void setRootOfDept(String value)
	{
		SetValByKey(AdminEmpAttr.RootOfDept, value);
	}
	public final String getRootOfFlow()
	{
		if (this.No.equals("admin"))
		{
			return "0";
		}

		return this.GetValStrByKey(AdminEmpAttr.RootOfFlow);
	}
	public final void setRootOfFlow(String value)
	{
		SetValByKey(AdminEmpAttr.RootOfFlow, value);
	}
	public final String getRootOfForm()
	{
		if (this.No.equals("admin"))
		{
			return "0";
		}

		return this.GetValStringByKey(AdminEmpAttr.RootOfForm);
	}
	public final void setRootOfForm(String value)
	{
		SetValByKey(AdminEmpAttr.RootOfForm, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	@Override
	public En.UAC getHisUAC()
	{
		UAC uac = new En.UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 管理员
	*/
	public AdminEmp()
	{
	}
	/** 
	 管理员
	 
	 @param no
	*/
	public AdminEmp(String no)
	{
		this.setNo(no);
		try
		{
			if (this.RetrieveFromDBSources() == 0)
			{
				Emp emp = new Emp(no);
				this.Copy(emp);
				this.Insert();
			}
		}
		catch (java.lang.Exception e)
		{
			this.CheckPhysicsTable();
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

		Map map = new Map("WF_Emp", "管理员");

		map.AddTBStringPK(AdminEmpAttr.No, null, "帐号", true, true, 1, 50, 110);
		map.AddTBString(AdminEmpAttr.Name, null, "名称", true, false, 0, 50, 110);
		map.AddDDLEntities(AdminEmpAttr.FK_Dept, null, "主部门", new BP.Port.Depts(), false);
		map.AddDDLEntities(AdminEmpAttr.OrgNo, null, "组织", new BP.WF.Port.Incs(), true);

		map.AddDDLSysEnum(AdminEmpAttr.UseSta, 3, "用户状态", true, true, AdminEmpAttr.UseSta, "@0=禁用@1=启用");
		map.AddDDLSysEnum(AdminEmpAttr.UserType, 3, "用户状态", true, true, AdminEmpAttr.UserType, "@0=普通用户@1=管理员用户");

		map.AddDDLEntities(AdminEmpAttr.RootOfFlow, null, "流程权限节点", new BP.WF.Template.FlowSorts(), false);
		map.AddDDLEntities(AdminEmpAttr.RootOfForm, null, "表单权限节点", new BP.WF.Template.SysFormTrees(), false);
		map.AddDDLEntities(AdminEmpAttr.RootOfDept, null, "组织结构权限节点", new BP.WF.Port.Incs(), false);

		map.AddTBMyNum();

			//查询条件.
		map.AddSearchAttr(AdminEmpAttr.UseSta);
		map.AddSearchAttr(AdminEmpAttr.UserType);
		map.AddSearchAttr(AdminEmpAttr.OrgNo);


		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设置加密密码";
		rm.getHisAttrs().AddTBString("FrmID", null, "输入密码", true, false, 0, 100, 100);
			//rm.getHisAttrs().AddTBString("FrmwID", null, "ewww", true, false, 0, 100, 100);
		rm.Warning = "您确定要执行设置改密码吗？";
		rm.ClassMethodName = this.toString() + ".DoSetPassword";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "增加管理员";
		rm.getHisAttrs().AddTBString("emp", null, "管理员帐号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("OrgNo", null, "可管理的组织结构代码", true, false, 0, 100, 100);
		rm.RefMethodType = RefMethodType.Func;
		rm.ClassMethodName = this.toString() + ".DoAdd";
		map.AddRefMethod(rm);




		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		if (this.No.equals("admin"))
		{
			this.setRootOfDept("0");
			this.setRootOfFlow("0");
			this.setRootOfForm("0");
		}

		return super.beforeUpdateInsertAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String DoAdd(String empNo, String orgNo)
	{

		BP.Port.Emp emp = new BP.Port.Emp();
		emp.No = empNo;
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@管理员增加失败，ID=" + empNo + "不存在用户表，您增加的管理员必须存在与Port_Emp用户表.";
		}

		BP.Port.Dept dept = new BP.Port.Dept();
		dept.No = orgNo;
		if (dept.RetrieveFromDBSources() == 0)
		{
			return "err@orgNo错误, 不存在 Port_Dept 里面。";
		}

		BP.WF.Port.Inc inc = new BP.WF.Port.Inc();
		inc.No = orgNo;
		if (inc.RetrieveFromDBSources() == 0)
		{
			return "err@orgNo错误, 不存在 Port_Inc 里面。";
		}

		//求根目录流程树.
		BP.WF.Template.FlowSort fsRoot = new WF.Template.FlowSort();
		fsRoot.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo,"0");


		BP.WF.Template.FlowSort fs = new WF.Template.FlowSort();
		fs.No = "Inc" + orgNo;
		if (fs.RetrieveFromDBSources() == 1)
		{
			return "err@该组织已经初始化过流程树目录.";
		}

		fs.Name = dept.Name + "-流程树";
		fs.ParentNo = fsRoot.No;
		fs.setOrgNo(dept.No);
		fs.Insert();


		//求根目录流程树.
		BP.Sys.FrmTree frmRoot = new BP.Sys.FrmTree();
		frmRoot.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");

		BP.Sys.FrmTree frmTree = new BP.Sys.FrmTree();
		frmTree.No = "Inc" + orgNo;
		if (frmTree.RetrieveFromDBSources() == 1)
		{
			return "err@该组织已经初始化过表单树目录.";
		}

		frmTree.ParentNo = frmRoot.No;
		frmTree.Name = dept.Name + "-表单树";
		frmTree.OrgNo = dept.No;
		frmTree.Insert();


		AdminEmp ae = new AdminEmp();
		ae.No = empNo;
		if (ae.RetrieveFromDBSources() == 1)
		{
			if (ae.getIsAdmin() == true)
			{
				return "err@该管理员已经存在,请删除该管理员重新增加delete from wf_emp where no='" + empNo + "'";
			}
			ae.Delete();
		}



		ae.Copy(emp);

		ae.setUserType(1);
		ae.setUseSta(1);
		ae.setRootOfDept(orgNo);
		ae.setRootOfFlow("Inc" + orgNo);
		ae.setRootOfForm("Inc" + orgNo);
		ae.Insert();

		return "info@管理员增加成功.";
	}

	/** 
	 设置加密密码存储
	 
	 @param password
	 @return 
	*/
	public final String DoSetPassword(String password)
	{
		String str = BP.Tools.Cryptography.EncryptString(password);
		DBAccess.RunSQLReturnVal("UPDATE Port_Emp SET Pass='" + str + "' WHERE No=' " + this.getNo()+ " '");
		return "设置成功..";
	}

}