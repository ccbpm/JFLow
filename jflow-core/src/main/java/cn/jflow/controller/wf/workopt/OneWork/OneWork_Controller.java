package cn.jflow.controller.wf.workopt.OneWork;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Tools.StringHelper;
import BP.WF.HttpHandler.WF_WorkOpt_OneWork;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/WorkOpt/OneWork")
@ResponseBody
public class OneWork_Controller extends HttpHandlerBase{
	
   /**
    * 默认执行的方法
    * @return 
    */
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequestPost()
    {
		WF_WorkOpt_OneWork  OneWork = new WF_WorkOpt_OneWork();
		super.ProcessRequest(OneWork);
    }
    
    @Override
    public Class<WF_WorkOpt_OneWork> getCtrlType() {
    	return WF_WorkOpt_OneWork.class;
    }
}
