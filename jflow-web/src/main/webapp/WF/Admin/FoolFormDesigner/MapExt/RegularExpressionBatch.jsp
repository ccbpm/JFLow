<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%@page import="cn.jflow.model.wf.mapdef.mapext.*"%>
<%
RegularExpressionBatchModel resb=new RegularExpressionBatchModel(request,response);
resb.init();
%>
<script language=javascript>
function Esc() {
    if (event.keyCode == 27)
        window.close();
    return true;
}
function WinOpen(url, name) {
    window.open(url, name, 'height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
    //window.open(url, 'xx');
}
function TROver(ctrl) {
    ctrl.style.backgroundColor = 'LightSteelBlue';
}

function TROut(ctrl) {
    ctrl.style.backgroundColor = 'white';
}
/*隐藏与显示.*/
function ShowHidden(ctrlID) {

    var ctrl = document.getElementById(ctrlID);
    if (ctrl.style.display == "block") {
        ctrl.style.display = 'none';
    } else {
        ctrl.style.display = 'block';
    }
}


function Save() {



    //获得选择的字段.
    var arrObj = document.all;

    var fields = '';

    for (var i = 0; i < arrObj.length; i++) {

        if (typeof arrObj[i].type == "undefined")
            continue;
        if (arrObj[i].type != 'checkbox')
            continue;

        var cb = arrObj[i];

        if (cb.check == false)
            continue;

        fields += arrObj[i].name + ',';
    }

    alert('字段被选择:' + fields);

    if (fields == '') {
        alert('请选择要批量设置的字段');
        return;
    }

    var url = 'RegularExpression.htm?s=3&FK_MapData=<%=resb.getFK_MapData()%>&RefNo=' + fields + '&ExtType=RegularExpression&OperAttrKey=ND207_QingJiaYuanYin&DoType=templete';
    var b = window.showModalDialog(url, 'ass', 'dialogHeight: 500px; dialogWidth: 700px;center: yes; help: no');


}
</script>

<style type="text/css">
       body{
           margin: 0 auto;
       }
</style>

</head>
<body onkeypress="Esc();" class="easyui-layout"  topmargin="0" leftmargin="0">
	<form method="post" action="" id="form1">
		<input type="hidden" id="success" value="${success }" />
		<div data-options="region:'center',title:''" style="padding: 5px;">
			<div style='width: 100%'>

				<table style="width: 100%">
					<caption>批量设置控件的正则表达式</caption>
					<tr>
						<th>序号</th>
						<th>字段/名称</th>
						<th>类型</th>
						<th>表达式</th>
						<th>操作</th>
					</tr>
					<c:forEach items="<%=resb.getAttrList() %>" var="attr" varStatus="i">
					<tr>
						<td>${i.count }</td>
						<td><input  type="checkbox" id="${attr.getKeyOfEn()}"   value="${attr.getKeyOfEn()}  - ${attr.getKeyOfEn()}"  name="${attr.getKeyOfEn() }" /> ${attr.getKeyOfEn()}  - ${attr.name}</td>
						<td>${attr.getMyDataTypeStr() }</td>
						<td>无</td>
						<td></td>
					</tr>
					</c:forEach>
					<tr>
						<td colspan="5"> <input type="button"  value="批量设置"  onclick="Save()" /></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</body>
</html>