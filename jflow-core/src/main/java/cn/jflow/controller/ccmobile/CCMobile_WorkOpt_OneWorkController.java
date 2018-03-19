package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.CCMobile_WorkOpt_OneWork;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile/WorkOpt/OneWork")
@ResponseBody
public class CCMobile_WorkOpt_OneWorkController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile_WorkOpt_OneWork  AttrHandler = new CCMobile_WorkOpt_OneWork();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <CCMobile_WorkOpt_OneWork>getCtrlType() {
		return CCMobile_WorkOpt_OneWork.class;
	}
}

