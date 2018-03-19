package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_FoolFormDesigner;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/FoolFormDesigner")
@ResponseBody
public  class FoolFormDesignerController extends HttpHandlerBase {
	/**
	* 默认执行的方法
	* @return 
	*/
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost()
	{
		WF_Admin_FoolFormDesigner foolFormDesignerController = new WF_Admin_FoolFormDesigner();
		super.ProcessRequest(foolFormDesignerController);
	}
	
	@Override
    public Class<WF_Admin_FoolFormDesigner> getCtrlType() {
    	return WF_Admin_FoolFormDesigner.class;
    }
}
