<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WF/head/head1.jsp"%>
<%
	CondPRIModel PRI=new CondPRIModel(request,response);
PRI.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="" id="form1" method="post">
<div>
<%=PRI.Pub1.ListToString()%>
</div>
</form>
</body>
</html>