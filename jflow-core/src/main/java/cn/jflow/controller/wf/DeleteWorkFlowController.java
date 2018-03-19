package cn.jflow.controller.wf;

import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.controller.wf.workopt.BaseController;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.DelWorkFlowRole;

@Controller
@RequestMapping("/WF")
@Scope("request")
public class DeleteWorkFlowController extends BaseController {
	
	@RequestMapping(value = "/DeleteWorkFlow", method = RequestMethod.POST)
	public void execute(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		
		if (request.getParameter("BtnID").equals("Btn_Cancel")) {
			response.sendRedirect("MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID="
					+ this.getWorkID() + "&FK_Node=" + this.getFK_Node());
			return;
		} 

		try {
			String info = request.getParameter("TB_Doc")==null?"":request.getParameter("TB_Doc");
			DelWorkFlowRole role = DelWorkFlowRole.forValue(Integer.valueOf(request.getParameter("DDL1")));
			String rInfo = "";
			switch (role) {
			case DeleteAndWriteToLog:
				rInfo = Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), info, true);
				break;
			case DeleteByFlag:
				rInfo = Dev2Interface.Flow_DoDeleteFlowByFlag(this.getFK_Flow(), this.getWorkID(), info, true);
				break;
			case DeleteReal:
				rInfo = Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
				break;
			default:
				throw new RuntimeException("@没有涉及到的删除情况。");
			}
			Glo.ToMsg(rInfo, response);
		} catch (Exception ex) {
			Glo.ToMsg(ex.getMessage(), response);
		}

	}

}
