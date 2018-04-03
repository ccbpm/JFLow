package cn.jflow.controller.wf.comm;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;
import BP.WF.Glo;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/Comm")
public class EnsController {

	private String getEnsName() {
		return ContextHolderUtils.getRequest().getParameter("EnsName");
	}

	private HashMap<String, BaseWebControl> controls(
			HttpServletRequest request) {
		return HtmlUtils.httpParser(request.getParameter("FormHtml"), request);
	}

	public final Entities getHisEns() {
		return BP.En.ClassFactory.GetEns(this.getEnsName());
	}

	@RequestMapping(value = "/DelEns", method = RequestMethod.POST)
	public final void DelEns(HttpServletRequest request,
			HttpServletResponse response) {
		BP.En.Entities dtls = this.getHisEns();
		QueryObject qo = new QueryObject(dtls);
		qo.DoQuery(dtls.getGetNewEntity().getPK(),
				BP.Sys.SystemConfig.getPageSize(), BaseModel.getPageIdx(),
				false);
		String msg1 = "";
		for (BP.En.Entity dtl : dtls.ToJavaListEn()) {
			Object cb = request.getParameter("IDX_" + dtl.getPKVal());
			try {
				if (cb == null) {
					continue;
				} else {
					dtl.Delete();
				}
			} catch (RuntimeException ex) {
				msg1 += "@删除期间错误:" + ex.getMessage();
			}
		}

		if (!msg1.equals("")) {
			BaseModel.Alert(msg1);
		}
		
		try {
			response.sendRedirect(Glo.getCCFlowAppPath()
					+ "WF/Comm/Ens.htm?EnsName=" + this.getEnsName()
					+ "&PageIdx=" + BaseModel.getPageIdx());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/SaveCloseEns", method = RequestMethod.POST)
	public final void SaveCloseEns(HttpServletRequest request,
			HttpServletResponse response) {
		SaveEns(request, response);
		BaseModel.WinClose();
	}

	@RequestMapping(value = "/SaveEns", method = RequestMethod.POST)
	public final void SaveEns(HttpServletRequest request,
			HttpServletResponse response) {

		String msg = null;
		Entities dtls = BP.En.ClassFactory.GetEns(getEnsName());
		Entity en = dtls.getGetNewEntity();
		QueryObject qo = new QueryObject(dtls);
		qo.DoQuery(en.getPK(), SystemConfig.getPageSize(),
		BaseModel.getPageIdx(), false);
		Map map = dtls.getGetNewEntity().getEnMap();
		for (Entity dtl : dtls.ToJavaListEn()) {
			dtl = BaseModel.Copy(request, dtl, dtl.getPKVal().toString(),
					dtl.getEnMap(), controls(request));
			
			try {
				dtl.Update();
			} catch (RuntimeException ex) {
				msg += "<hr>" + ex.getMessage();
			}
		}

		// BP.Sys.MapDtl
		en = BaseModel.Copy(request, en, "0", en.getEnMap(),
				controls(request));
		if (en.getIsBlank() == false) {
			if (en.getIsNoEntity()) {
				if (en.getEnMap().getIsAutoGenerNo()) {
					try {
						en.SetValByKey("No", en.GenerNewNoByKey("No"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			try {
				if (en.getPKVal().toString().equals("0")) {
				} else {
					en.Insert();
				}
			} catch (RuntimeException ex) {
				// msg += "<hr>" + ex.Message;
			}
		}

		try {
			if (msg != null) {
				request.getSession().setAttribute("info1", msg);
				response.sendRedirect(Glo.getCCFlowAppPath()
						+ "WF/Comm/Ens.htm?EnsName=" + this.getEnsName()
						+ "&PageIdx=" + BaseModel.getPageIdx());
			} else {
				response.sendRedirect(Glo.getCCFlowAppPath()
						+ "WF/Comm/Ens.htm?EnsName=" + this.getEnsName()
						+ "&PageIdx=" + BaseModel.getPageIdx());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
