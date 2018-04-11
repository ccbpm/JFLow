package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.CCMobile_CCForm;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile/CCForm")
@ResponseBody
public class CCMobile_CCFormController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile_CCForm  AttrHandler = new CCMobile_CCForm();
		super.ProcessRequest(AttrHandler);
	}
	
	
	@Override
	public Class <CCMobile_CCForm>getCtrlType() {
		return CCMobile_CCForm.class;
	}
}

