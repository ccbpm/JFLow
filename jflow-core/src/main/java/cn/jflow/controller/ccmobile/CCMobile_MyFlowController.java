package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.CCMobile_MyFlow;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile/MyFlow")
@ResponseBody
public class CCMobile_MyFlowController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile_MyFlow  AttrHandler = new CCMobile_MyFlow();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <CCMobile_MyFlow>getCtrlType() {
		return CCMobile_MyFlow.class;
	}
}

