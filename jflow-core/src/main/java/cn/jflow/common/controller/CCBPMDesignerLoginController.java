package cn.jflow.common.controller;

import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.Log;
import BP.Sys.OSModel;
import BP.Tools.StringHelper;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin/CCBPMDesigner")
public class CCBPMDesignerLoginController extends BaseController {
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected void execute(HttpServletRequest request,HttpServletResponse response) 
	{		
		String doType = request.getParameter("DoType");
		String msg="";
		PrintWriter out =null;
    	if(("DefaultInit").equals(getDoType()))//登陆初始化页面
    	{
    		msg = this.DefaultInit();
    	}else if(("Logout").equals(getDoType()))//获得枚举列表的JSON
    	{
    		 BP.WF.Dev2Interface.Port_SigOut();
    	}else if(("LoginInit").equals(getDoType()))//登录初始化..
    	{
    		if (BP.DA.DBAccess.IsExitsObject("WF_Emp") == false)
                msg="url@=../DBInstall.jsp";
    	}else if(("Login").equals(getDoType()))
    	{
    		  msg = this.Login();
    	}else
    	{
    		msg = "err@没有判断的标记:" + this.getDoType();
    	}
 		try{
 			out = response.getWriter();
 			out.write(msg);
        }
        catch (Exception ex)
        {
            msg = "login err@" + ex.getMessage();
            Log.DebugWriteError(msg);
        }finally
		{
			if(null != out)
			{
				out.close();
			}
		}
       
    }
    /// <summary>
    /// 初始化登录界面.
    /// </summary>
    /// <returns></returns>
    public String DefaultInit()
    {
        //让admin登录
        if (StringHelper.isNullOrEmpty(BP.Web.WebUser.getNo()) || BP.Web.WebUser.getNo() != "admin")
            return "url@Login.htm?DoType=Logout";

        Hashtable ht = new Hashtable();
       if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
            ht.put("OSModel", "0");
        else
            ht.put("OSModel", "1");

        try
        {
            // 执行升级
            String str = BP.WF.Glo.UpdataCCFlowVer();

            if (str == null)
                str = "";

            ht.put("Msg", str);
        }
        catch (Exception ex)
        {
            ht.put("Msg", ex.getMessage());
        }
        //生成Json.
        return BP.Tools.Json.ToJson(ht, false);
    }

    public String Login()
    {
        BP.Port.Emp emp = new BP.Port.Emp();
        emp.setNo(this.GetValFromFrmByKey("TB_UserNo"));

        if (emp.RetrieveFromDBSources()==0)
            return "err@用户名或密码错误.";
        String pass= this.GetValFromFrmByKey("TB_Pass");
        if (emp.getPass().equals(pass)==false)
            return "err@用户名或密码错误.";
        //让其登录.
        BP.WF.Dev2Interface.Port_Login(emp.getNo(),true);
        return "SID=" + emp.getSID() + "&UserNo=" + emp.getNo();
    }
    /*
     * 获得表单的属性
     */
   
    public String GetValFromFrmByKey(String key)
    {
        String val = getRequest().getParameter(key);
        if (val == null)
            return null;
        val = val.replace("'", "~");
        return val;
    }
    public int GetValIntFromFrmByKey(String key)
    {
        return Integer.parseInt(this.GetValFromFrmByKey(key));
    }
    public boolean GetValBoolenFromFrmByKey(String key)
    {
        String val = this.GetValFromFrmByKey(key);
        if (val == null || "".equals(val))
            return false;
        return true;
    }
		
}
