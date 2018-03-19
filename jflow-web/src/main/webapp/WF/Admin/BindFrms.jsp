<%@page import="BP.WF.Glo"%>
<%@page import="cn.jflow.model.wf.admin.BindFrmsModel"%>
<%@ include file="/WF/head/head1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	BindFrmsModel bindFrmsModel = new BindFrmsModel(request, response);
	bindFrmsModel.loadPage();
%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>绑定表单</title>
<link href="../Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
<link href="../Comm/Style/Tabs.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="../Comm/JScript.js"
	type="text/javascript"></script>
<base target="_self" />
<script type="text/javascript">
	function Esc() {
		if (event.keyCode == 27)
			window.close();
		return true;
	}
</script>
<script type="text/javascript">
	function New() {
	    window.location.href = window.location.href;
	}
	
	function BindFrms(nodeid, fk_flow) {
	    var url = "BindFrms.jsp?FK_Node=" + nodeid + '&FK_Flow='+ fk_flow +'&DoType=SelectedFrm';
	    window.location.href = url;
	}
	
	function WinField(fk_mapdata, nodeid, fk_flow) {
	    var url = "../MapDef/Sln.jsp?FK_MapData=" + fk_mapdata + "&FK_Node=" + nodeid + '&FK_Flow=' + fk_flow;
	    WinOpen(url);
	}
	
	function WinFJ(fk_mapdata, nodeid, fk_flow) {
	    var url = "../MapDef/Sln.jsp?FK_MapData=" + fk_mapdata + "&FK_Node=" + nodeid + '&FK_Flow=' + fk_flow + '&DoType=FJ';
	    WinOpen(url);
	}
	
	function WinDtl(fk_mapdata, nodeid, fk_flow) {
	    var url = "../MapDef/Sln.jsp?FK_MapData=" + fk_mapdata + "&FK_Node=" + nodeid + '&FK_Flow=' + fk_flow + '&DoType=Dtl';
	    WinOpen(url);
	}
	
	function ToolbarExcel(fk_mapdata, nodeid, fk_flow) {
	    var pk = fk_mapdata + '_' + nodeid + '_' + fk_flow;
	    var url = "../Comm/RefFunc/UIEn.jsp?EnName=BP.Sys.ToolbarExcelSln&PK=" + pk;
	    WinOpen(url);
	}
	
	function ToolbarWord(fk_mapdata, nodeid, fk_flow) {
	    var pk = fk_mapdata + '_' + nodeid + '_' + fk_flow;
	    var url = "../Comm/RefFunc/UIEn.jsp?EnName=BP.Sys.ToolbarWordSln&PK=" + pk;
	    WinOpen(url);
	}
	
	function AddIt(fk_mapdata, fk_node, fk_flow) {
	    var url = 'FlowFrms.jsp?DoType=Add&FK_MapData=' + fk_mapdata + '&FK_Node=' + fk_node + '&FK_Flow=' + fk_flow;
	    window.location.href = url;
	}
	function DelIt(fk_mapdata, fk_node, fk_flow) {
	    if (window.confirm('您确定要移除吗？') == false)
	        return;
	    var url = 'FlowFrms.jsp?DoType=Del&FK_MapData=' + fk_mapdata + '&FK_Node=' + fk_node + '&FK_Flow=' + fk_flow;
	    window.location.href = url;
	}
	
	function SaveFlowFrmsClick(){
		document.getElementById('form').action="SaveFlowFrms.do?FK_Flow=<%=bindFrmsModel.getFK_Flow()%>&FK_Node=<%=bindFrmsModel.getFK_Node()%>";
		document.getElementById('form').submit();
		
	}
	
	function SavePowerOrdersClick(){ 
		document.getElementById('form').action="SavePowerOrders.do?FK_Flow=<%=bindFrmsModel.getFK_Flow()%>&FK_Node=<%=bindFrmsModel.getFK_Node()%>";
		document.getElementById('form').submit();
	}
	
	function btn_SavePowerOrders_Click(){
		var url = "<%=basePath%>WF/Admin/SavePowerOrders.do?FK_Node=<%=bindFrmsModel.getFK_Node()%>&FK_Flow=<%=bindFrmsModel.getFK_Flow()%>&FK_MapData=<%=bindFrmsModel.getFK_MapData()%>";
		$("#form").attr("action",url);
		$("#form").submit();
	}
</script>
</head>
<body>
	<form id="form" method="post">
		<%=bindFrmsModel.getPub1()%>
	</form>
	
</body>
</html>