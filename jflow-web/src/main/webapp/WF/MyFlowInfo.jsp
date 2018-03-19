<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%
	MyFlowInfoModel flowInfoModel = new MyFlowInfoModel(request, response);
	flowInfoModel.initFlowInfo();
%>
<style>
fieldset img{
	vertical-align: middle;
	padding: 0px 3px;
}
</style>
<script type="text/javascript">
	if (window.opener && !window.opener.closed) {
		if (window.opener.name == "main") {
			window.opener.location.href = window.opener.location.href;
			window.opener.top.leftFrame.location.href = window.opener.top.leftFrame.location.href;
		}
	}
</script>
</head>
<body bgcolor="silver">
	<div style="text-align: center; position: absolute; background-color: white; height: 100%;left: 15%; right: 15%">
	
	<!-- 内容 -->
	<!-- 表格数据 -->
	<table border=0px align='center' width='100%'>
		<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),
					WebUser.getName())%></strong></div></Caption>
		<td>
		<div class="divCenter1" style="width:500px;text-align:left;font-size:14px">
			<section class="am-panel am-panel-default">
				<div class="am-panel-hd">
					<h3 class="am-panel-title"><img src="<%=basePath %>WF/Img/info.png" align="middle">操作提示</h3>
				</div>
				<main class="am-panel-bd"> <%
				 	if (WebUser.getIsWap()) {
				%>
				<fieldset class="am-box" style="border:0px;">
					<%=flowInfoModel.getMsg()%>
				</fieldset>
				<BR>
				<%
					} else {
				%>
				<fieldset class="am-box" style="border:0px;">
					<%=flowInfoModel.getMsg()%>
				</fieldset>
				<BR>
				<%
					}
				%> </main>
			</section>
		</div>
		</td>
	</table>
	</div>
</body>
</html>