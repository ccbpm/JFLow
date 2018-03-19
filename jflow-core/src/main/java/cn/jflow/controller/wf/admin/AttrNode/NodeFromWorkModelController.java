package cn.jflow.controller.wf.admin.AttrNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.Template.BtnLab;
import BP.WF.Template.WebOfficeWorkModel;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/NodeFromWorkModel")
@Scope("request")
public class NodeFromWorkModelController extends BaseController
{

	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	@ResponseBody
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String xxy,String x22xy,String xxx,String DDL_Frm,
			String TB_CustomURL,String TB_FormURL,String x22axy,String RB_Doc,String FK_Node) {
		try {
			Node nd = new Node(FK_Node);
			//使用ccbpm内置的节点表单
			if ("RB_FixFrm".equals(xxx))
			{
				if ("RB_Frm_0".equals(x22xy))
				{
					nd.setFormType(NodeFormType.FreeForm);
					nd.DirectUpdate();
				}
				else
				{
					nd.setFormType(NodeFormType.FoolForm);
					nd.DirectUpdate();
				}
				if ("RB_CurrentForm".equals(xxy))
				{
					nd.setNodeFrmID("");
					nd.DirectUpdate();
				}
				if ("RB_OtherForms".equals(xxy))
				{
					nd.setNodeFrmID("ND" + DDL_Frm);
					nd.DirectUpdate();
				}
			}
			//使用嵌入式表单
			if ("RB_SelfForm".equals(xxx))
			{
				nd.setFormType(NodeFormType.SelfForm);
				nd.setFormUrl(TB_CustomURL);
				nd.DirectUpdate();
			}
			//使用SDK表单
			if ("RB_SDKForm".equals(xxx))
			{
				nd.setFormType(NodeFormType.SDKForm);
				nd.setFormUrl(TB_FormURL);
				nd.DirectUpdate();
			}
			//绑定多表单
			if ("RB_SheetTree".equals(xxx))
			{
				if ("RB_tree".equals(x22axy))
				{
					nd.setFormType(NodeFormType.SheetTree);
					nd.DirectUpdate();
				}
				else
				{
					nd.setFormType(NodeFormType.DisableIt);
					nd.DirectUpdate();
				}
			}

			//如果公文表单选择了
			if ("RB_WebOffice".equals(xxx))
			{
				nd.setFormType(NodeFormType.WebOffice);
				nd.Update();

				//按钮标签.
				BtnLab btn = new BtnLab(this.getNodeID());

				if ("RB_WebOffice_Frm2".equals(RB_Doc))
				{
					btn.setWebOfficeWorkModel(WebOfficeWorkModel.FrmFirst);
				}
				else
				{
					btn.setWebOfficeWorkModel(WebOfficeWorkModel.WordFirst);
				}

				btn.Update();
			}
		} catch (Exception e) {
			return "{\"msg\":\"保存失败\"}";
		}
		return "{\"msg\":\"保存成功\"}";

	}
}