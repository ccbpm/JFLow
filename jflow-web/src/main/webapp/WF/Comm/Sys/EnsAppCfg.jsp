<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
    <%
    EnsAppCfgModel ens=new EnsAppCfgModel(request,response);
    ens.init();
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target=_self  />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>属性设置</title>
    <link href="../Style/Table0.css" rel="stylesheet" type="text/css" />
    <base target=_self />
    <script type="text/javascript">
    function btn_Click(btnName) {
    	$("#FormHtml").val($("#form1").html());
    	var url = '<%=basePath%>WF/Comm/Sys/btn_Click.do?EnsName=<%=ens.getEnsName()%>&DoType=<%=ens.getDoType()%>&btnName='+btnName;
		$("#form1").attr("action",url);
		$("#form1").submit();
	}
    </script>
</head>
<body topmargin="0" leftmargin="0">
    <form id="form1" action="EnsAppCfg.jsp?EnsName=<%=ens.getEnsName() %>" method="post">
    <input id="FormHtml" name="FormHtml" type="hidden">
        <%=ens.getUcSys1() %>
    </form>
</body>
</html>