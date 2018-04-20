package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Tools.StringHelper;
import BP.WF.HttpHandler.WF_WorkOpt;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/WorkOpt")
@ResponseBody
public class WorkOpt_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequestPost()
    {
		WF_WorkOpt  workOptHandler = new WF_WorkOpt();
		super.ProcessRequest(workOptHandler);
    }
    
    @Override
    public Class<WF_WorkOpt> getCtrlType() {
    	return WF_WorkOpt.class;
    }
}
