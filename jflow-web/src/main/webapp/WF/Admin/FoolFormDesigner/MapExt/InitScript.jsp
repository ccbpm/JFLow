<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="cn.jflow.common.model.InitScriptModel" %>
<%@ include file="/WF/head/head2.jsp"%>
<%
	InitScriptModel ism = new InitScriptModel(request, response);
	   ism.init();
%>
<link href="<%=basePath%>WF/Comm/Style/CommStyle.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
<script type="text/javascript">
     function NoSubmit(ev) {
     if (window.event.srcElement.tagName == "TEXTAREA")
        return true;

     if (ev.keyCode == 13) {
        window.event.keyCode = 9;
        ev.keyCode = 9;
        return true;
     }
     return true;
    }
	function btn_Save_Click() {
		document.forms["formS"].submit();
	}
</script>
<base target="_self" />
<style type="text/css">
textarea{width:99%;}
Table{
 width: 750px;
}
</style>
</head>
<body>
	<form name="formS" id="form_id" method="post"
		action="<%=basePath%>/WF/mapdef/mapExt/scriptBtnSave.do">
		  <input type="hidden" name="FK_MapData" value="<%=request.getParameter("FK_MapData")%>">
		<%=ism.Pub1.toString()%>
	</form>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#Btn_Group').hide();
	});
</script>
</html>