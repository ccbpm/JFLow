package cn.jflow.controller.wf.admin.AttrFlow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.controller.wf.workopt.BaseController;

public class API extends BaseController{
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	 public final void Demo_StartFlow()
	{
		String flowNo =request.getParameter("FK_Flow");

		//创建WorkID.
		long workid=BP.WF.Dev2Interface.Node_CreateBlankWork(flowNo);

		//BP.WF.Dev2Interface.Node_StartWork(flowNo,
	}


}

