package cn.jflow.controller.wf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Setting;
import BP.WF.HttpHandler.Base.HttpHandlerBase;
@Controller
@RequestMapping("/WF/Setting")
@ResponseBody
public class Settingcontroller extends HttpHandlerBase {
	
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_Setting  AttrHandler = new WF_Setting();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <WF_Setting>getCtrlType() {
		return WF_Setting.class;
	}
}
