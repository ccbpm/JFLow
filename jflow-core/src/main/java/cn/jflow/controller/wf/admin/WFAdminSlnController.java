package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_Sln;
import BP.WF.HttpHandler.Base.HttpHandlerBase;


@Controller
@RequestMapping("/WF/Admin/Sln")
@ResponseBody
public  class WFAdminSlnController extends HttpHandlerBase {
	/**
	* 默认执行的方法
	* @return 
	*/
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost()
	{
		WF_Admin_Sln en = new WF_Admin_Sln();
		super.ProcessRequest(en);
	}
	
	@Override
    public Class<WF_Admin_Sln> getCtrlType() {
    	return WF_Admin_Sln.class;
    }
}
