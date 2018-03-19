<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import ="cn.jflow.model.designer.FhlFlowModel" %>
<%@ include file="/WF/head/head1.jsp" %>
<html>
<%
	FhlFlowModel ffm = new FhlFlowModel(request,response);
	ffm.pageLoad();
%>
<head>
<script language="JavaScript" src="<%=basePath %>WF/Comm/JScript.js" type="text/javascript" ></script>
    <link href="<%=basePath %>WF/Comm/Style/Table.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath %>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath %>DataUser/Style/MyFlow.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		function onCloseWindow()
		{
			if (navigator.userAgent.indexOf("Firefox") > 0) {
				window.location.href = 'about:blank ';
			} else {
				window.opener = null;
				window.open('', '_self', '');
				window.close();
			}
		}
		
		
		function threadReturnWork(url)
		{
			window.location.href = url;
		}
	</script>
</head>
<body>
	<table  style=" text-align:left; width:100%">
	<caption>您好:<%=BP.WF.Glo.GenerUserImgSmallerHtml(BP.Web.WebUser.getNo(),BP.Web.WebUser.getName()) %></caption>
	<tr>
		<td >
		<div id="tabForm">
    		<div id="topBar">
    			<div style ="display:none">
        			<jsp:include page="../SDKComponents/Toolbar.jsp"></jsp:include>
        		</div>
        		<%=ffm.toolBar1.get_content() %>
    		</div>
    		
		</div>
 		<div id="divCCForm" >
        	<%=ffm.enModel.Pub.toString() %>
    	</div>

		</td>
	</tr>
</table>
</body>
</html>