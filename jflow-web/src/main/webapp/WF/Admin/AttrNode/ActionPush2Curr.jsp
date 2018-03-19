<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ include file="/WF/head/head1.jsp"%>
<%
	ActionPush2CurrModel apcm=new ActionPush2CurrModel(request,response);
	apcm.init();
%>

<script type="text/javascript">
	
        function DoDel(nodeid, xmlEvent) {
            if (window.confirm('您确认要删除吗?') == false)
                return;
            parent.window.location.href = 'Action.jsp?NodeID=' + nodeid + '&DoType=Del&RefXml=' + xmlEvent + '&tk=' + Math.random();
        }
        
        function onSave(){
        	
        	var param = window.location.search;
    		$("#FormHtml").val($("#form5").html());
    		var url = "<%=basePath%>DES/ActionPushSave.do"+param;
    		$("#form5").attr("action", url);
    		$("#form5").submit();
        }
</script>
</head>
<body class="easyui-layout">
	<form method="post" action="" class="am-form" id="form5">
		<input type="hidden" id="FormHtml" name="FormHtml" value="">
		<div data-options="region:'center',border:false">
			<%=apcm.Pub1.toString() %>
		</div>
	</form>
</body>
</html>