<%@page import="cn.jflow.common.model.EnsModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
		EnsModel em = new EnsModel(request, response);
		em.pageLoad();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"">
<title>详细信息</title>
	<link href="../Comm/Style/Table0.css"rel='stylesheet' type="text/css"/>
	<link href="../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="../Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script language="JavaScript" src="JScript.js"></script>
    <script language="JavaScript" src="ShortKey.js"></script>
    <script language="JavaScript" src="./JS/Calendar/WdatePicker.js" defer="defer"></script>
  
<script type="text/javascript">
function SelectAll() {
    var arrObj = document.all;
    if (document.forms[0].checkedAll.checked) {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                arrObj[i].checked = true;
            }
        }
    } else {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox')
                arrObj[i].checked = false;
        }
    }
}

function OpenAttrs(ensName) {
    var url = './Sys/EnsAppCfg.jsp?EnsName=' + ensName;
    var s = 'dialogWidth=680px;dialogHeight=480px;status:no;center:1;resizable:yes'.toString();
    val = window.showModalDialog(url, null, s);
    window.location.href = window.location.href;
   
}

function ShowEn(url, wName, h, w) {
    var s = "dialogWidth=" + parseInt(w) + "px;dialogHeight=" + parseInt(h) + "px;resizable:yes";
    var val = window.showModalDialog(url, null, s);
    window.location.href = window.location.href;
}

//点击删除
function Btn_Del(){
	if(!confirm("确认删除吗？")){
		return;
	}
	var param = window.location.search;
	$("#FormHtml").val($("#form1").html());
	var url = "<%=basePath%>/WF/Comm/DelEns.do"+param;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
//点击保存
function Btn_Save(){
   	var param = window.location.search;
		$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>/WF/Comm/SaveEns.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
		alert("保存成功.");
}

</script>
<style type="text/css">
.Style1
{
    width: 100%;
}
.Idx
{
    width: 10px;
}
</style>
</head>
<body topmargin="0" leftmargin="0" onkeypress="Esc()" onkeydown='DoKeyDown();'>
<form method="post" action="" class="am-form" id="form1" name="form1">
<input type="hidden" id="FormHtml" name="FormHtml" value="">
<table id="Table1" align="left" width="100%">

<tr>
	 <td class="ToolBar">
		<%=EnsModel.ToolBar1.toString() %>		
    </td>
</tr>
<tr>
    <td>
      	 <%=EnsModel.ucsys1.toString()%>
    </td>
</tr>
<tr>
    <td align="left">
      <%=EnsModel.pub.toString() %>
    </td>
</tr>
</table>
</form>
</body>
</html>