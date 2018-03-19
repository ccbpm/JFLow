package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.CCMobile_Setting;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile/Setting")
@ResponseBody
public class CCMobile_SettingController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile_Setting  AttrHandler = new CCMobile_Setting();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <CCMobile_Setting>getCtrlType() {
		return CCMobile_Setting.class;
	}
}