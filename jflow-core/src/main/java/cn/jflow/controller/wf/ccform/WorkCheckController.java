package cn.jflow.controller.wf.ccform;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Tools.StringHelper;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Template.CCSta;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/WorkOpt")
@Scope("request")
public class WorkCheckController extends BaseController {
	@RequestMapping(value = "/WorkCheckSave", method = RequestMethod.POST)
	private void execute(HttpServletRequest request,
			HttpServletResponse response) {
		String dotype = getDoType();
		// 查看时取消保存
		if (!StringHelper.isNullOrEmpty(dotype) && dotype.equals("View")) {
			return;
		}

		// 内容为空，取消保存
		if (StringHelper.isNullOrEmpty(getTB_Doc())) {
			return;
		}
		// 加入审核信息.
		String msg = getTB_Doc();

		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getNodeID());

		// 处理人大的需求，需要把审核意见写入到FlowNote里面去.
		String sql = "UPDATE WF_GenerWorkFlow SET FlowNote='" + msg
				+ "' WHERE WorkID=" + this.getWorkID();
		BP.DA.DBAccess.RunSQL(sql);

		// 判断是否是抄送?
		if (this.getIsCC()) {
			// 写入审核信息，有可能是update数据。
			BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(),
					this.getNodeID(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());

			// 设置抄送状态 - 已经审核完毕.
			BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(),
					this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
		} else {
			BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(),
					this.getNodeID(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());
		}

		try {
			response.sendRedirect("WorkCheck.jsp?s=2&WorkID=" + this.getWorkID()
					+ "&FK_Node=" + this.getNodeID() + "&FK_Flow="
					+ this.getFK_Flow() + "&FID=" + this.getFID() + "&Paras="
					+ ContextHolderUtils.getRequest().getParameter("Paras"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 执行审批.
		// BP.PubClass.Alert("保存成功...");

		// 关闭窗口.
		// BP.PubClass.WinClose();
	}

	@RequestMapping(value = "/ddd", method = RequestMethod.POST)
	public void btn_Click(HttpServletRequest request,
			HttpServletResponse response, boolean IsCC, String WorkID)
			throws IOException {

		// 查看时取消保存
		if (getDoType() != null && "View".equals(getDoType()))
			return;

		// 内容为空，取消保存
		if (StringHelper.isNullOrEmpty(request.getParameter("TB_Doc")))
			return;
		// 加入审核信息.
		String msg = request.getParameter("TB_Doc");

		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getNodeID());

		// 处理人大的需求，需要把审核意见写入到FlowNote里面去.
		String sql = "UPDATE WF_GenerWorkFlow SET FlowNote='" + msg
				+ "' WHERE WorkID=" + WorkID;
		BP.DA.DBAccess.RunSQL(sql);

		// 判断是否是抄送?
		if (IsCC) {
			// 写入审核信息，有可能是update数据。
			BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(),
					this.getNodeID(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());

			// 设置抄送状态 - 已经审核完毕.
			BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(),
					this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
		} else {
			BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(),
					this.getNodeID(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());
		}

		response.sendRedirect("WorkCheck.jsp?s=2&OID=" + this.getWorkID()
				+ "&FK_Node=" + this.getNodeID() + "&FK_Flow="
				+ this.getFK_Flow() + "&FID=" + this.getFID() + "&Paras="
				+ request.getParameter("Paras"));
		// 执行审批.
		// BP.Sys.PubClass.Alert("保存成功...");

		// 关闭窗口.
		// BP.Sys.PubClass.WinClose();

	}

}
