package cn.jflow.controller.wf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF;
import BP.WF.HttpHandler.WF_Admin_CCFormDesigner;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF")
@ResponseBody
public class WF_Controller extends HttpHandlerBase{

	/**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequest()
    {
		WF  wf = new WF();
    	super.ProcessRequest(wf);
    }
    
    @Override
    public Class<WF> getCtrlType() {
    	
    	return WF.class;
    }
}
