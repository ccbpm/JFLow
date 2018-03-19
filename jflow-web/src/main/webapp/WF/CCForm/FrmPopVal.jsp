<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	FrmPopValModel frmPop = new FrmPopValModel(request, response);
	frmPop.init();
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
/* 设置选框 cb1.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')"; */
// function SetSelected(cb, ids) {
//     alert(ids);
//     var arrmp = ids.split(',');
//     var arrObj = document.all;
//     var isCheck = false;
//     if (cb.checked)
//         isCheck = true;
//     else
//         isCheck = false;
//     alert(arrObj.length);
//     for (var i = 0; i < arrObj.length; i++) {
//         if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
//             for (var idx = 0; idx <= arrmp.length; idx++) {
//                 if (arrmp[idx] == '')
//                     continue;
//                 var cid = arrObj[i].name + ',';
//                 var ctmp = arrmp[idx] + ',';
//                 if (cid.indexOf(ctmp) > 1) {
//                     arrObj[i].checked = isCheck;
//                                         alert(arrObj[i].name + ' is checked ');
//                                         alert(cid + ctmp);
//                 }
//             }
//         }
//     }
// }

function SetSelected(cb_selectAll,group) {
    var arrObj = document.all;
    if (cb_selectAll.checked) {
        for (var i = 0; i < arrObj.length; i++) {
        	if(group!='01'){
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
        	if(!group!='01'){
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
    var url = "<%=basePath%>WF/CCForm/btn_Click.do?FK_MapExt=<%=frmPop.getFK_MapExt()%>&RefPK=0&CtrlVal=<%=frmPop.get_CtrlVal()%>&btnName="+ btnName;
	$("#form1").attr("action", url);
	$("#form1").submit();
	}
</script>
</head>
<body>
	<form id="form1" method="post" action="FrmPopVal.jsp?FK_MapExt=<%=frmPop.getFK_MapExt()%>&amp;RefPK=0&amp;CtrlVal=<%=frmPop.get_CtrlVal()%>">
		<input type="hidden" id="FormHtml" name="FormHtml" value="">
		<div>
		<%=frmPop.Pub1.ListToString()%>
		</div>
	</form>
</body>

</html>