package BP.WF.Port;

import BP.DA.DBAccess;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.WF.Cryptography;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.Web.WebUser;

/**
 * 管理员
 * @author Administrator
 *
 */
public class AdminEmp extends EntityNoName {
	public boolean getIsAdmin()
    {
       if ("admin".equals(this.getNo()))
          return true;

       if (this.getUserType() == 1 && this.getUseSta() == 1)
          return true;

          return false; 
    }
    /*
     * 用户类型
     */
    public int getUserType()
    {
        return this.GetValIntByKey(AdminEmpAttr.UserType);
    }
    public void setUserType(Integer value)
    {
        SetValByKey(AdminEmpAttr.UserType, value);
    }
    public String getFK_Dept()
    {
        return this.GetValStringByKey(AdminEmpAttr.FK_Dept);
    }
    public void setFK_Dept(String value)
    {
    	SetValByKey(AdminEmpAttr.FK_Dept, value);
    }
        
    public String getRootOfDept()
    {
        if ("admin".equals(this.getNo()))
        return "0";

        return this.GetValStringByKey(AdminEmpAttr.RootOfDept);
    }
    public void setRootOfDept(String value)
    {
        SetValByKey(AdminEmpAttr.RootOfDept, value);
    }
    
    public String getRootOfFlow()
    {
    	if ("admin".equals(this.getNo()))
    		return "0";

       return this.GetValStrByKey(AdminEmpAttr.RootOfFlow);
    }
    public void setRootOfFlow(String value)
    {
    	SetValByKey(AdminEmpAttr.RootOfFlow, value);
    }
    
    public String getRootOfForm()
    {
    	if ("admin".equals(this.getNo()))
            return "0";
            return this.GetValStringByKey(AdminEmpAttr.RootOfForm);
    }
    public void setRootOfForm(String value)
    {
    	SetValByKey(AdminEmpAttr.RootOfForm, value);
    }
    /** 组织结构
	 
    */
	public final String getOrgNo()
	{
		return this.GetValStringByKey(AdminEmpAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		SetValByKey(AdminEmpAttr.OrgNo, value);
	}
	/** 
	 用户状态
	*/
	public final int getUseSta()
	{
		return this.GetValIntByKey(WFEmpAttr.UseSta);
	}
	public final void setUseSta(int value)
	{
		SetValByKey(WFEmpAttr.UseSta, value);
	}
    /*
     *  管理员
     */
    public AdminEmp() { }
    /*
     * 管理员
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
        catch(Exception e)
        {
            this.CheckPhysicsTable();
        }
    }
    
    /*
     * 重写基类方法
     * 
     */
    public BP.En.Map getEnMap() {
    {
            if (this.get_enMap() != null)
                return this.get_enMap();

            Map map = new Map("WF_Emp", "管理员");

            map.AddTBStringPK(AdminEmpAttr.No, null, "帐号", true, true, 1, 50, 36);
            map.AddTBString(AdminEmpAttr.Name, null, "名称", true,false, 0, 50, 20);
            map.AddDDLEntities(AdminEmpAttr.FK_Dept, null, "主部门", new BP.Port.Depts(), false);
            map.AddDDLEntities(AdminEmpAttr.OrgNo, null, "组织", new BP.WF.Port.Incs(), true);

            map.AddDDLSysEnum(AdminEmpAttr.UseSta, 3, "用户状态", true, true, AdminEmpAttr.UseSta, "@0=禁用@1=启用");
            map.AddDDLSysEnum(AdminEmpAttr.UserType, 3, "用户状态", true, true, AdminEmpAttr.UserType, "@0=普通用户@1=管理员用户");

            map.AddDDLEntities(AdminEmpAttr.RootOfFlow, null, "流程权限节点", new BP.WF.Template.FlowSorts(), true);
            map.AddDDLEntities(AdminEmpAttr.RootOfForm, null, "表单权限节点", new BP.WF.Template.SysFormTrees(), true);
            map.AddDDLEntities(AdminEmpAttr.RootOfDept, null, "组织结构权限节点", new BP.GPM.Depts(), true);

            //查询条件.
            map.AddSearchAttr(AdminEmpAttr.UseSta);
            map.AddSearchAttr(AdminEmpAttr.UserType);
            map.AddSearchAttr(AdminEmpAttr.OrgNo);


            RefMethod rm = new RefMethod();
			rm.Title = "增加管理员";
			//  rm.GroupName = "高级设置";
			rm.getHisAttrs().AddTBString("FrmID", null, "管理员编号ID", true, false, 0, 100, 100);

			rm.ClassMethodName = this.toString() + ".DoAddAdminer";
			rm.Icon = "/WF/Img/Btn/Copy.GIF";
			map.AddRefMethod(rm);

			rm = new RefMethod();
			rm.Title = "设置加密密码";
			rm.getHisAttrs().AddTBString("FrmID", null, "输入密码", true, false, 0, 100, 100);
			rm.Warning = "您确定要执行设置改密码吗？";
			rm.ClassMethodName = this.toString() + ".DoSetPassword";
		   // rm.Icon = "../../WF/Img/Btn/Copy.GIF";
			map.AddRefMethod(rm);

            this.set_enMap(map);
            return this.get_enMap();
        }
    }
    /*
     * 更新，插入之前的工作。
     */
    @Override
    protected  boolean beforeUpdateInsertAction()
    {
    	if ("admin".equals(this.getNo()))
        {
            this.setRootOfDept("0");
            this.setRootOfFlow("0");
            this.setRootOfForm("0");
        }else{
        	if (this.getUserType() == 1)
			{
				//为树目录更新OrgNo编号.
				FlowSort fs = new FlowSort();
				fs.setNo(this.getRootOfFlow());//周朋@于庆海需要对照翻译.
				if (fs.RetrieveFromDBSources() == 1)
				{
					fs.setOrgNo(this.getRootOfDept());
					fs.Update();

					//更新本级目录.
					FlowSorts fsSubs = new FlowSorts();
					fsSubs.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, fs.getNo());
					for (FlowSort item : fsSubs.ToJavaListFs())
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
    /** 设置加密密码存储
	 
	 @param password
	 @return 
*/
	public final String DoSetPassword(String password)
	{
		String str = Cryptography.EncryptString(password);
		DBAccess.RunSQL("UPDATE Port_Emp SET Pass='" + str + "' WHERE No='" + this.getNo() + "'");
		return "设置成功..";
	}
	/** 
	 增加二级管理员.
	 
	 @param empID
	 @return 
	*/
	public final String DoAddAdminer(String empID)
	{
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo(empID);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@管理员增加失败，ID="+empID+"不存在用户表，您增加的管理员必须存在与Port_Emp用户表.";
		}

		AdminEmp adminEmp = new AdminEmp();
		
		if (adminEmp.Retrieve(AdminEmpAttr.No,empID,AdminEmpAttr.UserType,1) == 1)
		{
			return "err@管理员【" + adminEmp.getName() + "】已经存在，您不需要在增加.";
		}

		adminEmp.Copy(emp);
		adminEmp.setUserType(1);
		adminEmp.setUseSta(1);
		adminEmp.Save();

		return "增加成功,请关闭当前窗口查询到该管理员，设置他的权限。";

	}
}
