package cn.jflow.controller.wf.ccform;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

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
    public final void ProcessRequestPost(HttpServletRequest request)
    {
		WF_CCForm  ccFormHandler = new WF_CCForm();
		if (request instanceof DefaultMultipartHttpServletRequest) {
			ccFormHandler.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
		}
		super.ProcessRequest(ccFormHandler);
    }
    
    @Override
    public Class<WF_CCForm> getCtrlType() {
    	return WF_CCForm.class;
    }
}
