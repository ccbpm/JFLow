package cn.jflow.controller.wf.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.WF_Admin_AttrFlow;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/AttrFlow")
@ResponseBody
public class AttrFlowController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		WF_Admin_AttrFlow  AttrHandler = new WF_Admin_AttrFlow();
		
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <WF_Admin_AttrFlow>getCtrlType() {
		return WF_Admin_AttrFlow.class;
	}
}
