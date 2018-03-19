package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_AttrFlow;
import BP.WF.HttpHandler.WF_Admin_AttrNode;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/AttrNode")
@ResponseBody
public class AttrNodeController extends HttpHandlerBase{
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_Admin_AttrNode  AttrHandler = new WF_Admin_AttrNode();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <WF_Admin_AttrNode>getCtrlType() {
		return WF_Admin_AttrNode.class;
	}
}
