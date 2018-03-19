package cn.jflow.controller.wf.ccform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_CCForm;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/CCForm")
@ResponseBody
public class CCForm_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequestPost()
    {
		WF_CCForm  ccFormHandler = new WF_CCForm();
		super.ProcessRequest(ccFormHandler);
    }
    
    @Override
    public Class<WF_CCForm> getCtrlType() {
    	return WF_CCForm.class;
    }
}
