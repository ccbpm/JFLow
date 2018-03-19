<%@page import="cn.jflow.common.model.FormTreeBaseModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	FormTreeBaseModel ftb=new FormTreeBaseModel(request,response);
	ftb.Page_Load();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="<%=basePath%>WF/Scripts/FlowFormTreeData.js" type="text/javascript"></script>
</head>
<body>
    <form id="form1" runat="server">
	    <div>
	    
	    </div>
    </form>
</body>
</html>