package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin")
@ResponseBody
public class WFAdminController extends HttpHandlerBase{
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_Admin  admin = new WF_Admin();
		super.ProcessRequest(admin);
	}
	@Override
	public Class getCtrlType() {
		return WF_Admin.class;
	}

}
