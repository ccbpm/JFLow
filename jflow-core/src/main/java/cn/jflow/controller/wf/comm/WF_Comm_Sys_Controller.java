package cn.jflow.controller.wf.comm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.WF_Comm;
import BP.WF.HttpHandler.WF_Comm_Sys;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Comm/Sys")
@ResponseBody
public class WF_Comm_Sys_Controller extends HttpHandlerBase {

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) {
		WF_Comm_Sys CommSysHandler = new WF_Comm_Sys();
		if (request instanceof DefaultMultipartHttpServletRequest) {
			CommSysHandler.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
		}
		super.ProcessRequest(CommSysHandler);
	}

	@Override
	public Class<WF_Comm_Sys> getCtrlType() {
		return WF_Comm_Sys.class;
	}

}
