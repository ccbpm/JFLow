<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="cn.jflow.common.model.ThreadDtlModel" %>
<%@ include file="/WF/head/head1.jsp" %>
<%
	ThreadDtlModel tdm = new ThreadDtlModel(request,response);
	tdm.pageLoad(request,response);
%>
<html>
<head>

</head>
<body>
<table  style=" text-align:left; width:100%">
<caption>您好:<%=BP.WF.Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName()) %> --子线程信息</caption>
<tr>
<td style="text-align:center">
<div style="text-align:center">
	<%=tdm.pub1.toString() %>
</div>
</td>
</tr>
</table>
</body>
</html>