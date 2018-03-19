
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/jquery-1.7.2.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/CommonUnite.js"
	type="text/javascript"></script>
<link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Comm/Style/Tabs.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="<%=basePath%>WF/Comm/JScript.js"
	type="text/javascript"></script>
</head>