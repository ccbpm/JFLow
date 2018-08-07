package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.WF;
import BP.WF.HttpHandler.WF_Admin_CCFormDesigner;
import BP.WF.HttpHandler.WF_Setting;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Setting")
@ResponseBody
public class Setting_Controller extends HttpHandlerBase{

	/**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequest(HttpServletRequest request)
    {
		WF_Setting  settingHandler = new WF_Setting();
		if (request instanceof DefaultMultipartHttpServletRequest) {
			settingHandler.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
		}
    	super.ProcessRequest(settingHandler);
    }
    
    @Override
    public Class<WF_Setting> getCtrlType() {
    	
    	return WF_Setting.class;
    }
}
