package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.TempLateModel;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/WF/Template")
public class TemplateController extends BaseController {
	
	@RequestMapping(value = "/loadJson", method = RequestMethod.POST)
	@ResponseBody
	public String loadJson(HttpServletRequest request,HttpServletResponse response) {
		String LoadType=request.getParameter("LoadType");
		String Type=request.getParameter("Type");
	    String FK_Flow = request.getParameter("FK_Flow");
		String path1=request.getSession().getServletContext().getRealPath("/DataUser/OfficeTemplate");
		String path2=request.getSession().getServletContext().getRealPath("/DataUser/OfficeOverTemplate");
		String path3=request.getSession().getServletContext().getRealPath("/DataUser/OfficeSeal");
		String path4=request.getSession().getServletContext().getRealPath("/DataUser/FlowDesc");
		String path5=request.getSession().getServletContext().getRealPath("/DataUser/OfficeFile" + FK_Flow);
		TempLateModel tempLateModel=new TempLateModel(path1,path2,path3,path4,path5,request,response);
		return tempLateModel.init("true");
	}
}
