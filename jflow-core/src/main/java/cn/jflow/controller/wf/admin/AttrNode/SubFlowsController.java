package cn.jflow.controller.wf.admin.AttrNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Sys.FrmSubFlow;
import BP.Sys.FrmSubFlowSta;
import BP.Tools.StringHelper;
import BP.WF.Entity.FrmWorkShowModel;
import BP.WF.Flow;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/SubFlows")
@Scope("request")
public class SubFlowsController extends BaseController
{
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String TB_SFCaption,String TB_SFDefInfo,
			String SF_H,String SF_W,String SFSta,String SFShowModel,
			String FK_Node) {
		
		FrmSubFlow frmSubFlow = new FrmSubFlow(this.getFK_Node());

		//显示方式
		if ("RB_Table".equals(SFShowModel))
		{
			frmSubFlow.setSFShowModel(FrmWorkShowModel.Table);
		}
		if ("RB_Free".equals(SFShowModel))
		{
			frmSubFlow.setSFShowModel(FrmWorkShowModel.Free);
		}

		//控件状态 禁用
		if ("RB_Disable".equals(SFSta))
		{
			frmSubFlow.setSFSta(FrmSubFlowSta.Disable);
		}

		if ("RB_Enable".equals(SFSta))
		{
			frmSubFlow.setSFSta(FrmSubFlowSta.Enable);
		}

		if ("RB_Readonly".equals(SFSta))
		{
			frmSubFlow.setSFSta(FrmSubFlowSta.Readonly);
		}

		//标题
		frmSubFlow.setSFCaption(TB_SFCaption.trim());

		//可手工启动的子流程
		if (StringHelper.isAllWhitespace(TB_SFDefInfo.trim())==false)
		{
			String[] flows = TB_SFDefInfo.trim().split(",", -1);
			String errorMsg = "";
			for (String flowNo : flows) //101,,,,102也是错误格式
			{
				if (isNullOrEmpty(flowNo))
				{
					continue;
				}

				Flow flEn = new Flow();
				flEn.setNo(flowNo);
				if (flEn.getIsExits() == false)
				{
					errorMsg = "@流程编号[" + flowNo + "]不存在";
				}
			}

			if (StringHelper.isAllWhitespace(errorMsg) == false)
			{
				BP.Sys.PubClass.Alert(errorMsg, response);
				return "{\"msg\":\""+errorMsg+"\"}";
			}
			frmSubFlow.setSFDefInfo(TB_SFDefInfo.trim());
		}

		//高度.
		frmSubFlow.setSF_H(Float.parseFloat(SF_H.trim()));
		frmSubFlow.setSF_W(Float.parseFloat(SF_W.trim()));
		frmSubFlow.Update();
		return "{\"msg\":\"保存成功\"}";
	}
	public static boolean isNullOrEmpty(String string)
	{
		return string == null || string.equals("");
	}
}