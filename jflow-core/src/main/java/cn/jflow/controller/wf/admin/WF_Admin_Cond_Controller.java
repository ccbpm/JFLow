package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_Cond;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/Cond")
@ResponseBody
public class WF_Admin_Cond_Controller extends HttpHandlerBase {

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() {
		WF_Admin_Cond AttrHandler = new WF_Admin_Cond();
		super.ProcessRequest(AttrHandler);
	}

	@Override
	public Class<WF_Admin_Cond> getCtrlType() {
		return WF_Admin_Cond.class;
	}

}
