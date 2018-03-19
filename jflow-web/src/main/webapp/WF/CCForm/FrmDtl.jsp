<%@page import="cn.jflow.common.model.FrmDtlModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	
	FrmDtlModel fdm = new FrmDtlModel(request,response);
	fdm.Page_Load();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<style type="text/css">
.HBtn {
	/* display:none; */
	visibility: visible;
}
</style>

<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<script type="text/javascript"src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js"></script>
<script src="<%=basePath%>WF/Scripts/jquery-1.7.2.min.js"type="text/javascript"></script>

<script language="javascript">
    function Save(){
    	$("#bodyHtml").val($("#form1").html());
    	$.ajax({
				type: "POST",
				dataType : 'html',
				url : '<%=basePath%>WF/CCForm/FrmDtlSave.do',
				data : $('#form1').serialize(),
				success : function(data) {
					if (null != data || "" != data) {
						alert(data);
						location.reload();
					}
				}
		});
	}
</script>

<%
	int script_size = fdm.getScripts().size();
	for(int i = 0; i < script_size; i++){
		String script = fdm.getScripts().get(i);
	%>
<script type="text/javascript" src="<%=basePath+script%>"></script>
<%
	}
%>
<%
	int ccs_links_size = fdm.getCCSLinks().size();
	for(int i = 0; i < ccs_links_size; i++){
		String ccsLink = fdm.getCCSLinks().get(i);
%>
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath+ccsLink%>" />
<%
	}
%>
<%=fdm.scriptsBlock.toString() %>
</head>
<body>
	<form id="form1" action="" method="post">
		<input type="hidden" name="OID" value="<%=fdm.getOID()%>"> 
		<input type="hidden" name="FK_MapData" value="<%=fdm.getFK_MapData()%>">
		<input type="hidden" name="FK_Node" value="<%=fdm.getFK_Node()%>">
		<input type="hidden" id="bodyHtml" name="bodyHtml" />
		<div>
			<%=fdm.Btn_Save.toString() %>
			<%=fdm.Pub.toString() %>
		</div>
	</form>
</body>
</html>