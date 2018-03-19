package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_RptSearch;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/RptSearch")
@ResponseBody
public class WFRptSearchController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_RptSearch  AttrHandler = new WF_RptSearch();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <WF_RptSearch>getCtrlType() {
		return WF_RptSearch.class;
	}
}
