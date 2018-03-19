<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="cn.jflow.common.model.TempLateModel" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String load=request.getParameter("load");
	String LoadType=request.getParameter("LoadType");
	String Type=request.getParameter("Type");
    String FK_Flow = request.getParameter("FK_Flow");
	String path1=request.getSession().getServletContext().getRealPath("/DataUser/OfficeTemplate");
	String path2=request.getSession().getServletContext().getRealPath("/DataUser/OfficeOverTemplate");
	String path3=request.getSession().getServletContext().getRealPath("/DataUser/OfficeSeal");
	String path4=request.getSession().getServletContext().getRealPath("/DataUser/FlowDesc");
	String path5=request.getSession().getServletContext().getRealPath("/DataUser/OfficeFile" + FK_Flow);
	TempLateModel tempLateModel=new TempLateModel(path1,path2,path3,path4,path5,request,response);
	tempLateModel.init(load);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/jBox/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<link href="<%=basePath%>WF/Scripts/jBox/Skins/Blue/jbox.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
	function getSelected() {
		var row = $('#maingrid').datagrid('getSelected');
		return row;
	}
	function pageLoadding(msg) {
		$.jBox.tip(msg, 'loading');
	}
	function loaddingOut(msg) {
		$.jBox.tip(msg, 'success');
	}
</script>
<body>
	<form id="form1" runat="server">
	<div data-options="region:'center',iconCls:'icon-ok'">
		<table class="easyui-datagrid"  style="width:780px;height:280px" id="maingrid"
			data-options="collapsible : false,rownumbers : true,nowrap : true,singleSelect:true,striped : true,collapsible:true,url:'../Template/loadJson.do?LoadType=<%=LoadType %>&Type=<%=Type %>',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'name',width:530">名称</th>
				<th data-options="field:'type',width:120,align:'center'">类型</th>
				<th data-options="field:'size',width:100,align:'center'">大小(KB)</th>
			</tr>
		</thead>
	</table>
	</div>
	</form>
</body>
</html>