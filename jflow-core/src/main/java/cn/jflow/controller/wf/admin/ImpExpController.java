package cn.jflow.controller.wf.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.WF_Admin_FoolFormDesigner_ImpExp;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/FoolFormDesigner/ImpExp")
@ResponseBody
public class ImpExpController  extends HttpHandlerBase{
	
	/**
	* 默认执行的方法
	* @return 
	*/
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request)
	{
		WF_Admin_FoolFormDesigner_ImpExp impExtController = new WF_Admin_FoolFormDesigner_ImpExp();
		if (request instanceof DefaultMultipartHttpServletRequest) {
			impExtController.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
		}
		super.ProcessRequest(impExtController);
	}
	
	@Override
	public Class<WF_Admin_FoolFormDesigner_ImpExp> getCtrlType() {
		return WF_Admin_FoolFormDesigner_ImpExp.class;
	}
}


