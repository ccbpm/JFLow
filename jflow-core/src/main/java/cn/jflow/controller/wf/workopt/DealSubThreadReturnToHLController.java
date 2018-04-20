package cn.jflow.controller.wf.workopt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.ReturnWork;
import BP.WF.ReturnWorkAttr;

@Controller
@RequestMapping("/WF/dstrToHL")
public class DealSubThreadReturnToHLController extends BaseController{
	
	
	//@ResponseBody
	@RequestMapping(value = "/Btn_Send_Click", method = RequestMethod.GET)
	public void Btn_Send_Click(HttpServletRequest request,HttpServletResponse response) {
		String TB_Doc=request.getParameter("TB_Doc");
		ReturnWork rw = new ReturnWork();
		rw.Retrieve(ReturnWorkAttr.ReturnToNode, this.getFK_Node(),
				ReturnWorkAttr.WorkID, this.getWorkID());

		String info = Dev2Interface.Node_ReturnWork(this.getFK_Flow(),
				this.getWorkID(), this.getFID(), this.getFK_Node(),
				rw.getReturnNode(), TB_Doc, false);
		//提示信息.
		Glo.ToMsg(info,response);
		
		//return info;
	}
	
	@ResponseBody
	@RequestMapping(value = "/Btn_Del_Click", method = RequestMethod.POST)
	public String Btn_Del_Click(HttpServletRequest request,HttpServletResponse response) {
		Dev2Interface.Flow_DeleteSubThread(this.getFK_Flow(), this.getWorkID(),"手工删除");
		//提示信息.
		return "该工作删除成功...";
	}
	
	//@ResponseBody
	@RequestMapping(value = "/Btn_Shift_Click", method = RequestMethod.GET)
	public void  Btn_Shift_Click(HttpServletRequest request,HttpServletResponse response) {
		String shiftNo = request.getParameter("TB_ShiftNo"); 
		String TB_Doc=request.getParameter("TB_Doc");
		if (StringHelper.isNullOrEmpty(shiftNo)) {
			Glo.ToMsg("请选择移交人",response);
		}
		if(StringHelper.isNullOrEmpty(TB_Doc)) {
			Glo.ToMsg("请填写处理人信息",response);
		}

		String result = Dev2Interface.Node_Shift(this.getFK_Flow(),
				this.getFK_Node(), this.getWorkID(), this.getFID(), shiftNo,
				TB_Doc);

	  //提示信息.
		Glo.ToMsg(result,response);
	}
	

}
