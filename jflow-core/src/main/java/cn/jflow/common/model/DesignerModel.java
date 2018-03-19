package cn.jflow.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.Port.Emp;
import BP.Web.WebUser;

public class DesignerModel extends BaseModel {

	protected HttpServletRequest resquest;
	protected HttpServletResponse response;
	protected String basePath;

	public DesignerModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		super(request, response);
		this.resquest = request;
		this.response = response;
		this.basePath = basePath;
	}

	public void init() throws IOException {
	
		if (DBAccess.IsExitsObject("WF_Flow")==false)
		{
			this.get_response().sendRedirect("../DBInstall.jsp");
			return;
		}
	}

}
