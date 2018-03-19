<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	ActionEventModel ae = new ActionEventModel(request,response,basePath);
	ae.init();
%>
<script type="text/javascript">
    function DoDel(nodeid, xmlEvent) {
        if (window.confirm('您确认要删除吗?') == false)
            return; 
        parent.window.location.href = "<%=basePath%>WF/Admin/AttrNode/Action.jsp?NodeID=" + nodeid + "&DoType=Del&RefXml=" + xmlEvent + "&tk=" + Math.random();
    }
</script>
</head>
<body class="easyui-layout">
   	<form method="post" action="" class="am-form" id="form1">
   		<input type="hidden" id="FormHtml" name="FormHtml" value="">
	    <div data-options="region:'center',border:false">
	          <%=ae.Pub1.ListToString() %>
	    </div>
    </form>
</body>
<script type="text/javascript">
function onSave(){
	var param = window.location.search;
	
	$("#FormHtml").val($("#form1").html());
	var url = "<%=basePath%>DES/ActionEvent.do"+param;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
</script>
</html>