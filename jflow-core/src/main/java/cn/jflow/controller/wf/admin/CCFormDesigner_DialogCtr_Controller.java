package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_CCFormDesigner_DialogCtr;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/CCFormDesigner/DialogCtr")
@ResponseBody
public class CCFormDesigner_DialogCtr_Controller extends HttpHandlerBase{

	
	@RequestMapping(value="/ProcessRequest")
	public void processRequest()
	{
		WF_Admin_CCFormDesigner_DialogCtr ccformDiaLog = new WF_Admin_CCFormDesigner_DialogCtr();
		super.ProcessRequest(ccformDiaLog);
	}
	
	@Override
	public Class getCtrlType() {
		return WF_Admin_CCFormDesigner_DialogCtr.class;
	}
}
