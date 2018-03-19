package cn.jflow.controller.wf.comm.reffunc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Comm_RefFunc;
import BP.WF.HttpHandler.Base.HttpHandlerBase;
@Controller
@RequestMapping("/WF/Comm/RefFunc")
@ResponseBody
public class Dot2DotTreeDeptMedelCintroller extends HttpHandlerBase {
	
	@RequestMapping(value = "/ProcessRequest")
    public final void ProcessRequestPost()
    {
		WF_Comm_RefFunc  wcf = new WF_Comm_RefFunc();
		super.ProcessRequest(wcf);
    }
    
    @Override
    public Class<WF_Comm_RefFunc> getCtrlType() {
    	return WF_Comm_RefFunc.class;
    }
	
	
	
	
}
