package cn.jflow.controller.appace;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Tools.StringHelper;
import BP.WF.HttpHandler.AppACE;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/AppACE")
@ResponseBody
public class AppACE_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequestPost()
    {
		AppACE  appACEHandler = new AppACE();
    	super.ProcessRequest(appACEHandler);
    }
    
    @Override
    public Class<AppACE> getCtrlType() {
    	return AppACE.class;
    }
}
