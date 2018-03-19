<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	FrmPopValDirModel frmPopv = new FrmPopValDirModel(request, response);
	request.getParameter("FK_MapExt");
	request.getParameter("GroupVal");
	frmPopv.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function() {
	var arrObj = document.all;
	var val = window.opener.document.getElementById('TB_WeiWenDuiXiang').value;
	var v=new Array();
	v= val.split(";");
	var t="";
	alert(v.length);
	for (i = 0; i < v.length; i++) {
		t = v[i];
		for (var i = 0; i < arrObj.length; i++) {
			alert(arrObj[i].nextSibling.nodeValue);
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                arrObj[i].checked = true;
            }
        }
	}
})

function SetSelected(cb_selectAll,group) {
    var arrObj = document.all;
    if (cb_selectAll.checked) {
        for (var i = 0; i < arrObj.length; i++) {
        	if(!group=='01'){
        		if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox' && arrObj[i].title==group) {
                    arrObj[i].checked = true;
                }
        	}else{
        		if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                    arrObj[i].checked = true;
                }
        	}
            
        }
    } else {
        for (var i = 0; i < arrObj.length; i++) {
        	if(!group=='01'){
        		if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox'&& arrObj[i].title==group){
               	 arrObj[i].checked = false;
               }
        	}else{
        		if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox'){
               	 arrObj[i].checked = false;
               }
        	}
            
               
        }
    }
}
function btn_Click(btnName){
	$("#FormHtml").val($("#form1").html());
    var url = "<%=basePath%>WF/CCForm/btn_Click.do?FK_MapExt=<%=frmPopv.getFK_MapExt()%>&RefPK=0&CtrlVal=<%=frmPopv.get_CtrlVal()%>&btnName="+ btnName;
	$("#form1").attr("action", url);
	$("#form1").submit();
	}
</script>
</head>
<body>
	<form id="form1" method="post" action="FrmPopVal.jsp?FK_MapExt=<%=frmPopv.getFK_MapExt()%>&amp;RefPK=0&amp;CtrlVal=<%=frmPopv.get_CtrlVal()%>">
		<input type="hidden" id="FormHtml" name="FormHtml" value="">

		<table width="100%">
			<tr>
				<td width="30%"><%=frmPopv.Left.ListToString() %></td>
				<td valign=top><%=frmPopv.Pub1.ListToString() %></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="button" onclick="btn_Click('ok')" value="确认选择"></td>
				
			</tr>
		</table>
		
	</form>
</body>

</html>