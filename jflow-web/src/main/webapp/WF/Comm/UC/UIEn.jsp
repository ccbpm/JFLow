<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<base target=_self  />
<%
	UIEnsModel uem=new UIEnsModel(request,response);
	uem.init();
%>
</head>
<body>
	<%=uem.pub.toString() %>
</body>
</html>