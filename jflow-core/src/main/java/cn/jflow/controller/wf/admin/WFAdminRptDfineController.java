package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_RptDfine;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/RptDfine")
@ResponseBody
public class WFAdminRptDfineController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_Admin_RptDfine  AttrHandler = new WF_Admin_RptDfine();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <WF_Admin_RptDfine>getCtrlType() {
		return WF_Admin_RptDfine.class;
	}
}
