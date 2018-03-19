<%@page import="BP.Web.WebUser"%>
<%@page import="cn.jflow.common.model.M2mmModel"%>
<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page
	import="cn.jflow.common.model.SingleAttachmentUploadModel"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	M2mmModel m2mmModel = new M2mmModel(request, response);
	m2mmModel.loadData();
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>多对多</title>
<style type="text/css">
body {
	font-size: smaller;
}
</style>
<script language="javascript">
	var isChange = false;
	function SaveM2M() {

		if (isChange == false)
			return;
		var btn = document.getElementById('Button1');
		btn.click();
		isChange = false;
	}
	function TROver(ctrl) {
		ctrl.style.backgroundColor = 'LightSteelBlue';
	}
	function TROut(ctrl) {
		ctrl.style.backgroundColor = 'white';
	}
	function Del(id, ens) {
		if (window.confirm('您确定要执行删除吗？') == false)
			return;
		var url = 'Do.jsp?DoType=DelDtl&OID=' + id + '&EnsName=' + ens;
		var b = window
				.showModalDialog(url, 'ass',
						'dialogHeight: 400px; dialogWidth: 600px;center: yes; help: no');
		window.location.href = window.location.href;
	}
</script>
<style type="text/css">
.HBtn {
	width: 1px;
	height: 1px;
	display: none;
}

UL {
	padding-left: 12px;
}

li {
	font-size: 16px;
}

table {
	border: none;
}

.Left {
	border: none;
	background-color: Silver;
}
</style>
</style>
<script src="<%=basePath%>WF/Comm/JS/Calendar.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Comm/JScript.js" type="text/javascript"></script>
<link href="<%=basePath%>WF/Comm/Style/Table<%=WebUser.getStyle() %>.css" rel='stylesheet' type='text/css' />
<base target="_self" />
<script language="javascript">
	function M2MMSave() {
		$.ajax({
			cache : true,
			type : "POST",
			url : "WF/CCForm/M2MMSave.do",
			data : $('#m2mm_form').serialize(),
		});
	}
</script>
</head>
<body topmargin="0" leftmargin="0" onkeypress="Esc()" style="font-size: smaller">
	<form id="m2mm_form" method="post">
		<input type="hidden" name="FK_MapData" value="<%=m2mmModel.getFK_MapData()%>"/>
		<input type="hidden" name="OID" value="<%=m2mmModel.getOID()%>"/>
		<input type="hidden" name="NoOfObj" value="<%=m2mmModel.getNoOfObj()%>"/>
		<input type="hidden" name="OperObj" value="<%=m2mmModel.getOperObj()%>"/>
		<table>
        <tr>
            <td valign="top" style='width: 30%' class="Left">
               <%=m2mmModel.Left.toString() %>
            </td>
            <td valign="top">
                <%=m2mmModel.Pub1.toString() %>
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td colspan="1">
                <input type="button" id="Button1" class="Btn" value="保存" <%=m2mmModel.getVisible() %> <%=m2mmModel.getEnabled() %> onclick="M2MMSave();" />
            </td>
        </tr>
    </table>
		
	</form>

</body>
</html>
