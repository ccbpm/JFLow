<%@page import="cn.jflow.model.wf.comm.sys.EnsCfgModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	EnsCfgModel cfgModel = new EnsCfgModel(request, response);
	cfgModel.pageLoad();
%>
<%@ include file="/WF/head/head1.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target=_self  />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>功能配置</title>
</head>
<body>
	<%=cfgModel.getUcSys1() %>
</body>
</html>