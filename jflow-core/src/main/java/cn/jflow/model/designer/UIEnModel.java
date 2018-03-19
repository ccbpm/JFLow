package cn.jflow.model.designer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.EnType;
import BP.En.Entity;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;

public class UIEnModel extends BaseModel{

	public HttpServletRequest request;
	
	public HttpServletResponse response;
	public RefLeftModel RefLeft1;
	public UIEnsModel UIEn1;
	
	public String basePath;
	
	public UIEnModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.request=request;
		this.response=response;
	}
	

	public UIEnModel(HttpServletRequest request, HttpServletResponse response,RefLeftModel RefLeft1,UIEnsModel UIEn1) {
		super(request, response);
		this.request=request;
		this.response=response;
		this.RefLeft1=RefLeft1;
		this.UIEn1=UIEn1;
	}
	
	/** 
	 是否隐藏左侧功能栏
	 
	*/
	public final boolean getHiddenLeft() {
		return this.RefLeft1.getItemCount()== 0;
	}
	/** 
	 是否隐藏上部菜单栏
	 
	*/
	public final boolean getHiddenTop() {
		Entity en = null;
		try {
			en = this.UIEn1.getGetEnDa();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return en == null ? true : en.getEnMap().getEnType()== EnType.View;
	}

	public void init()
	{
		String enName = request.getParameter("EnName");
		if (StringHelper.isNullOrEmpty(enName))
			enName = request.getParameter("EnsName");

		if (enName.contains(".") == false)
		{
			try {
				response.sendRedirect("SysMapEn.jsp?EnsName=" + enName + "&PK=" + request.getParameter("PK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}
