package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_AttrFlow;
import BP.WF.HttpHandler.WF_RptDfine;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/RptDfine")
@ResponseBody
public class WFRptDfineController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_RptDfine  AttrHandler = new WF_RptDfine();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <WF_RptDfine>getCtrlType() {
		return WF_RptDfine.class;
	}
}
