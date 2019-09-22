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
 子公司人员
*/
public class Emp extends EntityNoName
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
		return this.GetValIntByKey(EmpAttr.UseSta);
	}
	public final void setUseSta(int value)
	{
		SetValByKey(EmpAttr.UseSta, value);
	}
	/** 
	 用户类型
	*/
	public final int getUserType()
	{
		return this.GetValIntByKey(EmpAttr.UserType);
	}
	public final void setUserType(int value)
	{
		SetValByKey(EmpAttr.UserType, value);
	}
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(EmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(EmpAttr.FK_Dept, value);
	}
	/** 
	 组织结构
	*/
	public final String getOrgNo()
	{
		return this.GetValStringByKey(EmpAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		SetValByKey(EmpAttr.OrgNo, value);
	}
	public final String getRootOfDept()
	{
		if (this.No.equals("admin"))
		{
			return "0";
		}

		return this.GetValStringByKey(EmpAttr.RootOfDept);
	}
	public final void setRootOfDept(String value)
	{
		SetValByKey(EmpAttr.RootOfDept, value);
	}
	public final String getRootOfFlow()
	{
		if (this.No.equals("admin"))
		{
			return "0";
		}

		return this.GetValStrByKey(EmpAttr.RootOfFlow);
	}
	public final void setRootOfFlow(String value)
	{
		SetValByKey(EmpAttr.RootOfFlow, value);
	}
	public final String getRootOfForm()
	{
		if (this.No.equals("admin"))
		{
			return "0";
		}

		return this.GetValStringByKey(EmpAttr.RootOfForm);
	}
	public final void setRootOfForm(String value)
	{
		SetValByKey(EmpAttr.RootOfForm, value);
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
	 子公司人员
	*/
	public Emp()
	{
	}
	/** 
	 子公司人员
	 
	 @param no
	*/
	public Emp(String no)
	{
		this.No = no;
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

		Map map = new Map("WF_Emp", "子公司人员");

		map.AddTBStringPK(EmpAttr.No, null, "帐号", true, true, 1, 50, 36);
		map.AddTBString(EmpAttr.Name, null, "名称", true,false, 0, 50, 20);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "主部门", new BP.Port.Depts(), false);
		map.AddDDLEntities(EmpAttr.OrgNo, null, "组织", new BP.WF.Port.Incs(), true);
		map.AddDDLSysEnum(EmpAttr.UseSta, 3, "用户状态", true, true, EmpAttr.UseSta, "@0=禁用@1=启用");

			//map.AddDDLEntities(EmpAttr.RootOfFlow, null, "流程权限节点", new BP.WF.Template.FlowSorts(), true);
			//map.AddDDLEntities(EmpAttr.RootOfForm, null, "表单权限节点", new BP.WF.Template.SysFormTrees(), true);
			//map.AddDDLEntities(EmpAttr.RootOfDept, null, "组织结构权限节点", new BP.WF.Port.Incs(), true);
		map.AddTBMyNum();


		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设置加密密码";
		rm.getHisAttrs().AddTBString("FrmID", null, "输入密码", true, false, 0, 100, 100);
		rm.Warning = "您确定要执行设置改密码吗？";
		rm.ClassMethodName = this.toString() + ".DoSetPassword";
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
		else
		{
			if (this.getUserType() == 1)
			{
				//为树目录更新OrgNo编号.
				BP.WF.Template.FlowSort fs = new Template.FlowSort();
				fs.No = this.getRootOfFlow(); //周朋需要对照翻译.
				if (fs.RetrieveFromDBSources() == 1)
				{
					fs.setOrgNo(this.getRootOfDept());
					fs.Update();

					//更新本级目录.
					BP.WF.Template.FlowSorts fsSubs = new Template.FlowSorts();
					fsSubs.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, fs.No);
					for (BP.WF.Template.FlowSort item : fsSubs)
					{
						item.setOrgNo(this.getRootOfDept());
						item.Update();
					}
				}
				BP.DA.DBAccess.RunSQL("UPDATE WF_FlowSort SET OrgNo='0' WHERE OrgNo NOT IN (SELECT RootOfDept FROM WF_Emp WHERE UserType=1 )");
			}
		}

		return super.beforeUpdateInsertAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 设置加密密码存储
	 
	 @param password
	 @return 
	*/
	public final String DoSetPassword(String password)
	{
		String str = BP.Tools.Cryptography.EncryptString(password);
		DBAccess.RunSQLReturnVal("UPDATE Port_Emp SET Pass='" + str + "' WHERE No='" " + this.getNo()+ " "'");
		return "设置成功..";
	}
	/** 
	 增加二级子公司人员.
	 
	 @param empID
	 @return 
	*/
	public final String DoAddAdminer(String empID)
	{
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.No = empID;
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@子公司人员增加失败，ID=" + empID + "不存在用户表，您增加的子公司人员必须存在与Port_Emp用户表.";
		}

		Emp Emp = new Emp();
		Emp.No = empID;
		if (Emp.RetrieveFromDBSources() == 1)
		{
			return "err@子公司人员【" + Emp.Name + "】已经存在，您不需要在增加.";
		}

		Emp.Copy(emp);
		Emp.setFK_Dept(WebUser.getFK_Dept());
		Emp.setRootOfDept(WebUser.getFK_Dept());
		Emp.setUserType(1);
		Emp.Save();

		return "增加成功,请关闭当前窗口查询到该子公司人员，设置他的权限。";

	}
}