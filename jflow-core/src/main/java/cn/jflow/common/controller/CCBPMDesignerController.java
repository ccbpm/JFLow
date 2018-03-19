package cn.jflow.common.controller;

import java.io.PrintWriter;
import java.util.Hashtable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.Log;
import BP.Sys.OSModel;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin/CCBPMDesigner1")
public class CCBPMDesignerController extends BaseController{
	
	@RequestMapping(value="designer", method = RequestMethod.POST)
	@ResponseBody
	public void execute()
	{
		String doType = getRequest().getParameter("DoType");
		String msg = "";
		PrintWriter out = null;
		
		if("Default_Init".equals(doType))//初始化登录界面.
		{
			msg = default_Init();
		} else if ("Logout".equals(doType))//获得枚举列表的JSON.
		{
			 BP.WF.Dev2Interface.Port_SigOut();
             return;
		}else if("LoginInit".equals(doType))//登录初始化..
		{
			if (BP.DA.DBAccess.IsExitsObject("WF_Emp") == false)
			{
                msg="url@=../DBInstall.jsp";
			}
			String userNo = getRequest().getParameter("UserNo");
            String sid = getRequest().getParameter("SID");
            if (sid != null && "admin".equals(userNo))
            {
                BP.WF.Dev2Interface.Port_Login(userNo, sid);
                msg = "url@Default.htm?UserNo=" + userNo + "&SessionID=" + ContextHolderUtils.getSession().getId();
            }
		}else if("Login".equals(doType))//登录
		{
			 msg = this.Login();
		}else{
			 msg = "err@没有判断的标记:" + doType;
		}
		
		try{
			out  = getResponse().getWriter();
			out.write(msg);
		}catch(Exception e)
		{
			Log.DebugWriteError(msg);
		}finally 
		{
			if(null != out)
			{
				out.close();
			}
		}
	}
	
	/**
	 * 初始化登录界面.
	 * @return
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String default_Init()
    {
        //让admin登录
    	if (StringHelper.isNullOrEmpty(WebUser.getNo()) || !"admin".equals(WebUser.getNo()))
            return "url@Login.htm?DoType=Logout";

		Hashtable ht = new Hashtable();
        if (Glo.getOSModel() == OSModel.OneOne)
        {
        	ht.put("OSModel", "0");
        }else{
        	ht.put("OSModel", "1");
        }

        try
        {
            // 执行升级
            String str = Glo.UpdataCCFlowVer();
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
        {
        	return "err@用户名或密码错误.";
        }
        String pass= this.GetValFromFrmByKey("TB_Pass");
        if (emp.getPass().equals(pass)==false)
        {
        	return "err@用户名或密码错误.";
        }
            
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
        {
            return null;
        }
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
