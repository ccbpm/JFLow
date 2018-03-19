package cn.jflow.controller.wf.admin.FlowFrm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.WF.Node;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/FrmEnableRole")
public class FrmEnableRoleController extends BaseController {

	@RequestMapping(value = "/Save", method = RequestMethod.POST)
	public void SaveFlowFrms(HttpServletRequest request,
			HttpServletResponse response,String FK_Node,String FK_MapData,String xxx,String TB_SQL) {

			Node nd = new Node(FK_Node);
			BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(), FK_MapData);

			if (xxx.equals("RB_0"))
			{
				fn.setFrmEnableRole(BP.WF.Template.FrmEnableRole.Allways);
			}

			if (xxx.equals("RB_1"))
			{
				fn.setFrmEnableRole(BP.WF.Template.FrmEnableRole.WhenHaveData);
			}

			if (xxx.equals("RB_2"))
			{
				fn.setFrmEnableRole(BP.WF.Template.FrmEnableRole.WhenHaveFrmPara);
			}

			if (xxx.equals("RB_3"))
			{
				fn.setFrmEnableRole(BP.WF.Template.FrmEnableRole.ByFrmFields);
			}

			if (xxx.equals("RB_4"))
			{
				fn.setFrmEnableRole(BP.WF.Template.FrmEnableRole.BySQL);
				fn.setFrmEnableExp(TB_SQL);
			}
			fn.Update();
		}
	}
