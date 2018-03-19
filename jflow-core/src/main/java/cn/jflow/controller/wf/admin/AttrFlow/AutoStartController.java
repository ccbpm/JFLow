package cn.jflow.controller.wf.admin.AttrFlow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.WF.Flow;
import BP.WF.FlowRunWay;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/AutoStart")
@Scope("request")
public class AutoStartController extends BaseController {
	/*
	 * protected final void Btn_Save_Click(Object sender, EventArgs e) { Save();
	 * 
	 * }
	 */
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String FK_Flow,String xzgz,String TB_SpecEmp,
			String TB_DataModel) {
		//执行保存.
		Flow en = new Flow(FK_Flow);
		if ("RB_HandWork".equals(xzgz))
		{
			en.setHisFlowRunWay(FlowRunWay.HandWork);
		}

		if ("RB_SpecEmp".equals(xzgz))
		{
			en.setHisFlowRunWay(FlowRunWay.SpecEmp);
			en.setRunObj(TB_SpecEmp);
		}

		if ("RB_DataModel".equals(xzgz))
		{
			en.setRunObj(TB_DataModel);
			en.setHisFlowRunWay(FlowRunWay.DataModel);
		}

		if ("RB_InsertModel".equals(xzgz))
		{
			en.setHisFlowRunWay(FlowRunWay.InsertModel);
		}
		int res = en.DirectUpdate();

		if(res<0){
			return "{\"msg\":\"保存失败\"}";
		}
		return "{\"msg\":\"保存成功\"}";
	}
}