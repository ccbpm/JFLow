package cn.jflow.controller.wf.appclassic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_AppClassic;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/AppClassic")
@ResponseBody
public class AppClassic_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequest()
    {
		WF_AppClassic  CCFromHandler = new WF_AppClassic();
    	super.ProcessRequest(CCFromHandler);
    }
    
    @Override
    public Class<WF_AppClassic> getCtrlType() {
    	
    	return WF_AppClassic.class;
    }
}
