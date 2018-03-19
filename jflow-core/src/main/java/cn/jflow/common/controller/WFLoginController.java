package cn.jflow.common.controller;

import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/NewWF")
public class WFLoginController extends BaseController{
	HttpServletResponse response;
	HttpServletRequest request;
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	protected void execute(TempObject object, HttpServletRequest request,HttpServletResponse response) {
		String doType = request.getParameter("DoType");
		String msg="";
		PrintWriter out =null;	
		 try
         {
			 if(("LoginInit").equals(getDoType()))//处理login的初始化工作.
			 {
				 msg= this.LoginInit();
			 }else if(("LoginSubmit").equals(getDoType()))//处理login的初始化工作.
			 {
				 msg = this.LoginSubmit();
			 }else if(("LoginExit").equals(getDoType()))//安全退出登录
			 {
				 BP.WF.Dev2Interface.Port_SigOut();
			 }else
			 {
				 msg = "err@没有判断的标记:" + this.getDoType();
			 }
			 out = response.getWriter();
		     out.write(msg);
         }
         catch (Exception ex)
         {
             msg = "err@" + ex.getMessage();
         } 
     }
	/*
	 * 返回当前对话信息
	 */
    public String LoginInit()
    {
        Hashtable ht = new Hashtable();

        if (BP.Web.WebUser.getNo() == null)
            ht.put("UserNo", "");
        else
            ht.put("UserNo", BP.Web.WebUser.getNo());

        if (BP.Web.WebUser.getIsAuthorize())
            ht.put("Auth", BP.Web.WebUser.getAuth());
        else
            ht.put("Auth", "");

        return BP.Tools.Json.ToJson(ht, false);
    }
    /*
     * 执行登录
     */
    public String LoginSubmit()
    {
        BP.Port.Emp emp = new BP.Port.Emp();
        emp.setNo(this.GetValFromFrmByKey("TB_UserNo"));

        if (emp.RetrieveFromDBSources() == 0)
            return "err@用户名或密码错误.";
        String pass = this.GetValFromFrmByKey("TB_Pass");
        if (emp.getPass().equals(pass) == false)
            return "err@用户名或密码错误.";
        //让其登录.
       String sid= BP.WF.Dev2Interface.Port_Login(emp.getNo(), true);
       return sid;
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

