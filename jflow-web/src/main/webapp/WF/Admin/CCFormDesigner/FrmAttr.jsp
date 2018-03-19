<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	//根据不同表单类型，转向不同的表单属性设置界面.
	String frmID = request.getParameter("FrmID");
	BP.Sys.MapData md = new BP.Sys.MapData(frmID);
	
	if (md.getHisFrmType() == BP.Sys.FrmType.FreeFrm || md.getHisFrmType() == BP.Sys.FrmType.FoolForm)
	{
	    response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.MapDataExts&PK=" + frmID);
	    return;
	}
	
	if (md.getHisFrmType() == BP.Sys.FrmType.Url)
	{
		response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.MapDataURLs&PK=" + frmID);
	    return;
	}
	
	if (md.getHisFrmType() == BP.Sys.FrmType.WordFrm)
	{
		response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.MapDataWords&PK=" + frmID);
	    return;
	}
	
	if (md.getHisFrmType() == BP.Sys.FrmType.ExcelFrm)
	{
		response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.MapDataExcels&PK=" + frmID);
	    return;
	}
%>