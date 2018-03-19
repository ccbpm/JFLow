<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.Port.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/WF/";
	
	String fkFlow = request.getParameter("FK_Flow");
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery-1.4.2.min.js"></script>
</head>
<body>
WebOffice/Default.jsp
<hr>
fkFlow = <%=fkFlow %>
</body>
</html>
