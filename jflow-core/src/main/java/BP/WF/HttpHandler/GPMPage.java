package BP.WF.HttpHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import org.apache.http.protocol.HttpContext;

import BP.BPMN.Glo;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.QueryObject;
import BP.GPM.Dept;
import BP.Port.Emp;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.WFSta;
import BP.WF.Data.MyJoinFlows;
import BP.WF.Data.MyStartFlowAttr;
import BP.WF.Data.MyStartFlows;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.Web.WebUser;

public class GPMPage extends WebContralBase{
	 /**
	  * 初始化函数
	  * @param mycontext
	  */
    public GPMPage(HttpContext mycontext)
    {
        this.context = mycontext;
    }
    
    public GPMPage()
    {
    }
    
    //#region 组织结构维护.
    /// <summary>
    /// 初始化组织结构部门表维护.
    /// </summary>
    /// <returns></returns>
    public String Organization_Init() throws Exception
    {
        //根据用户判断当前人员是否为二级管理员
        BP.WF.Port.AdminEmp emp = new BP.WF.Port.AdminEmp(WebUser.getNo());
        //二级管理员
        BP.GPM.Depts depts = new BP.GPM.Depts();
        if (emp.getNo().equals("admin")==false && emp.getUserType() == 1)
        {
            depts.Retrieve("ParentNo",WebUser.getFK_Dept());
            depts.AddEntity(new Dept(WebUser.getFK_Dept()));
            return depts.ToJson();
        }

        depts.RetrieveAll();

        return depts.ToJson();
    }

	
}
