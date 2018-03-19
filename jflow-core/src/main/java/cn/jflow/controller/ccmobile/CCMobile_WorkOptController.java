package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.CCMobile_WorkOpt;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile/WorkOpt")
@ResponseBody
public class CCMobile_WorkOptController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile_WorkOpt  AttrHandler = new CCMobile_WorkOpt();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <CCMobile_WorkOpt>getCtrlType() {
		return CCMobile_WorkOpt.class;
	}
}

