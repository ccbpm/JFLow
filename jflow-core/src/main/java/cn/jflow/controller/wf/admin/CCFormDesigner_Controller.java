package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_CCFormDesigner;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/CCFormDesigner")
@ResponseBody
public class CCFormDesigner_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequest()
    {
		WF_Admin_CCFormDesigner  CCFromHandler = new WF_Admin_CCFormDesigner();
    	super.ProcessRequest(CCFromHandler);
    }
    
    @Override
    public Class<WF_Admin_CCFormDesigner> getCtrlType() {
    	
    	return WF_Admin_CCFormDesigner.class;
    }
}
