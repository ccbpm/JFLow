<%@page import="cn.jflow.common.model.GroupDtlModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
String fkFlow = request.getParameter("FK_Flow");
String rptNo = request.getParameter("RptNo");
GroupDtlModel gdm = new GroupDtlModel(request, response);
gdm.pageLoad();
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>数据挖掘->详细信息</title>
	<link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath %>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath %>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath %>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="<%=basePath %>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
	<script src="<%=basePath %>WF/Comm/JS/Calendar/WdatePicker.js" type="text/javascript"></script>
	 <script type="text/javascript">
        function WinOpen(url) {
            var newWindow = window.open(url, 'df', 'width=700,height=400,top=100,left=300,scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
            newWindow.focus();
            return;
        }
  	 </script>
</head>
<body class="easyui-layout">
	<form id="form1" runat="server">
	    <div data-options="region:'center',border:false">
	        <%=gdm.Pub1 %>
	    </div>
    </form>
</body>
</html>