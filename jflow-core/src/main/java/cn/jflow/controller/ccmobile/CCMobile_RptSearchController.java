package cn.jflow.controller.ccmobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.CCMobile_RptSearch;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/CCMobile/RptSearch")
@ResponseBody
public class CCMobile_RptSearchController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost(HttpServletRequest request) 
	{
		CCMobile_RptSearch  AttrHandler = new CCMobile_RptSearch();
		super.ProcessRequest(AttrHandler);
	}
	@Override
	public Class <CCMobile_RptSearch>getCtrlType() {
		return CCMobile_RptSearch.class;
	}
}

