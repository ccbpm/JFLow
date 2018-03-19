package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.CCMobile;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile")
@ResponseBody
public class CCMobileController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile  AttrHandler = new CCMobile();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <CCMobile>getCtrlType() {
		return CCMobile.class;
	}
}

