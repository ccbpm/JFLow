package cn.jflow.controller.wf.comm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Comm_DataUser_Test;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/DataUser")
@ResponseBody
public class WF_Comm_DataUser_test_Controller extends HttpHandlerBase {

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() {
		WF_Comm_DataUser_Test CommHandler = new WF_Comm_DataUser_Test();
		super.ProcessRequest(CommHandler);
	}

	@Override
	public Class<WF_Comm_DataUser_Test> getCtrlType() {
		return WF_Comm_DataUser_Test.class;
	}

}
