package cn.jflow.controller.wf;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Tools.StringHelper;
import BP.WF.HttpHandler.WF_MyFlow;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/MyFlow")
@ResponseBody
public class MyFlow_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequestPost()
    {
		WF_MyFlow  myFlowHandler = new WF_MyFlow();
		super.ProcessRequest(myFlowHandler);
    }
    
    @Override
    public Class<WF_MyFlow> getCtrlType() {
    	return WF_MyFlow.class;
    }
}
