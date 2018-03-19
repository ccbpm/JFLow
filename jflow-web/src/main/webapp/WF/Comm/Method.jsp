<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<%
	MethodModel methodModel = new MethodModel(request, response);
	methodModel.loadPage();
%>
<title>清空窗口</title>
<base target="_self" />

<script type="text/javascript">
	function btn_do_click(){
		document.getElementById('form_id').submit();
		
	}
	document.getElementById("BodyHtml").value = document.body.innerHTML;
</script>
</head>
<body onkeypress="javascript:Esc();" leftmargin="0" topmargin="0">
	<form id="form_id" action="<%=basePath %>WF/Comm/doClick.do" method="post">
	<input type="hidden" name="BodyHtml" id="BodyHtml">
	<input type="hidden" name="M" value="<%=request.getParameter("M") %>">
		<br />
		<table style="width: 80%; align: center" align=center>
			<tr>
				<td><%=methodModel.pub.toString() %></td>
			</tr>
		</table>
	</form>
</body>
</html>