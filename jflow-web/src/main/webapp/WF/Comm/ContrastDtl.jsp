<%@page import="cn.jflow.common.model.ContrastDtlModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	ContrastDtlModel cdm = new ContrastDtlModel(request, response);
	cdm.Page_Load();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"">
<title>详细信息</title>
<link href="Style/Table0.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
<script language="JavaScript" src="JScript.js"></script>
<script language="javascript">
	function ShowEn(url, wName) {
		val = window.showModalDialog(url,wName,'dialogHeight: 550px; dialogWidth: 650px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
	}
</script>
<base target="_self" />
<body class="easyui-layout">
	<form id="Form1" method="post" runat="server">
		<div data-options="region:'center',border:false,title:'<%=cdm.getShowTitle()%>'">
			<%=cdm.UCSys1%>
		</div>
	</form>
</body>
</html>