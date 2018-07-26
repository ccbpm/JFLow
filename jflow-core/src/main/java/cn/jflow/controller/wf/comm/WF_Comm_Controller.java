package cn.jflow.controller.wf.comm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.WF_Comm;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Comm")
@ResponseBody
public class WF_Comm_Controller extends HttpHandlerBase {

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) {
		WF_Comm CommHandler = new WF_Comm();
		if (request instanceof DefaultMultipartHttpServletRequest) {
			CommHandler.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
		}
		super.ProcessRequest(CommHandler);
	}

	@Override
	public Class<WF_Comm> getCtrlType() {
		return WF_Comm.class;
	}

}
