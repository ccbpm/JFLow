package cn.jflow.controller.wf.admin.AttrFlow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.WF.Flow;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.StartGuideWay;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/StartGuideAd")
@Scope("request")
public class StartGuideAdController extends BaseController {
	/**
	 * 保存.
	 * 
	 * @param sender
	 * @param e
	 */
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response, String TB_ByHistoryUrl,
			String TB_SelfURL, String TB_BySQLOne1, String TB_BySQLOne2,
			String xz, String FK_Flow) {

		String flowNo = FK_Flow;
		Flow en = new Flow(flowNo);
		en.RetrieveFromDBSources();

		if ("RB_None".equals(xz)) {
			en.setStartGuideWay(StartGuideWay.None);
		}

		if ("RB_ByHistoryUrl".equals(xz)) {
			en.setStartGuidePara1(TB_ByHistoryUrl);
			en.setStartGuidePara2("");
			en.setStartGuideWay(StartGuideWay.ByHistoryUrl);
		}

		if ("RB_SelfUrl".equals(xz)) {
			en.setStartGuidePara1(TB_SelfURL);
			en.setStartGuidePara2("");
			en.setStartGuideWay(StartGuideWay.BySelfUrl);
		}

		// 单挑模式.
		if ("RB_BySQLOne".equals(xz)) {
			en.setStartGuidePara1(TB_BySQLOne1); // 查询语句.
			en.setStartGuidePara2(TB_BySQLOne2); // 列表语句.
			en.setStartGuideWay(StartGuideWay.BySystemUrlOneEntity);
		}

		FrmNodes fns = new FrmNodes(Integer.parseInt(Integer.parseInt(FK_Flow) + "01"));
		if (fns.size() >= 2) {
			if ("RB_FrmList".endsWith(xz)) {
				//en.setStartGuideWay(StartGuideWay.ByParas);
			}
		}

		en.Update();
		en.DirectUpdate();
		return "{\"msg\":\"保存成功\"}";
	}
}