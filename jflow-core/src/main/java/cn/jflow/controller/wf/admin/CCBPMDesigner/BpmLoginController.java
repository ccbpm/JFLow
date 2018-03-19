package cn.jflow.controller.wf.admin.CCBPMDesigner;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/WF/admin/ccbpmDesigner1")
public class BpmLoginController extends BaseController{

	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		// 检查是否是安装了数据库如果没有就让其安装.
		if (BP.DA.DBAccess.IsExitsObject("WF_Emp") == false) {
			try {
				mv.setViewName("redirect:" + "/WF/Admin/DBInstall.jsp");
				//response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/DBInstall.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return mv;
		}

		String doType=request.getParameter("DoType");
		if("Logout".equals(doType)){
			//退出.
			BP.WF.Dev2Interface.Port_SigOut();
		}
		
		if("Login".equals(doType)){
			String user = request.getParameter("TB_UserName").trim();
			String pass = request.getParameter("TB_Password").trim();
			try {
				if (WebUser.getNo() != null) {
					BP.WF.Dev2Interface.Port_SigOut();
				}

				BP.Port.Emp em = new BP.Port.Emp(user);
				if (em.CheckPass(pass)) {
					BP.WF.Dev2Interface.Port_Login(user);
					WebUser.setToken ("");//this.Session.SessionID;
					//response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/CCBPMDesigner/Default.jsp?SID="+BP.WF.Dev2Interface.Port_GetSID(user));
					mv.setViewName("redirect:" + Glo.getCCFlowAppPath()+"WF/Admin/CCBPMDesigner/Default.jsp?SID="+BP.WF.Dev2Interface.Port_GetSID(user));
					return mv;
				}
				else {
					//PubClass.Alert("用户名密码错误，注意密码区分大小写，请检查是否按下了CapsLock.。", ContextHolderUtils.getResponse());
					//response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/CCBPMDesigner/Login.jsp");
					mv.setViewName("redirect:" + Glo.getCCFlowAppPath()+"WF/Admin/CCBPMDesigner/Login.jsp?y=f");
					return mv;
				}
			}catch (RuntimeException ex) {
				//PubClass.Alert("用户名密码错误，注意密码区分大小写，请检查是否按下了CapsLock.。", ContextHolderUtils.getResponse());
				mv.setViewName("redirect:" + Glo.getCCFlowAppPath()+"WF/Admin/CCBPMDesigner/Login.jsp?y=f");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mv;
	}
	
	
}
