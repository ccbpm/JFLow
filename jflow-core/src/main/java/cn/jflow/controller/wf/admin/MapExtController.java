package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_FoolFormDesigner_MapExt;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/FoolFormDesigner/MapExt")
@ResponseBody
public class MapExtController extends HttpHandlerBase{

	/**
	* 默认执行的方法
	* @return 
	*/
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost()
	{
		WF_Admin_FoolFormDesigner_MapExt  mapExtController = new WF_Admin_FoolFormDesigner_MapExt();
		super.ProcessRequest(mapExtController);
	}

	@Override
    public Class<WF_Admin_FoolFormDesigner_MapExt> getCtrlType() {
    	return WF_Admin_FoolFormDesigner_MapExt.class;
    }
}
