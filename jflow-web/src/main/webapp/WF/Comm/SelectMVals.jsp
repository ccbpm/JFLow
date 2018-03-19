<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	SelectMValModel mval = new SelectMValModel(request, response, basePath);
	mval.init();
%>
<script type="text/javascript">
function SelectAll(cb_selectAll) {
    var arrObj = document.all;
    if (cb_selectAll.checked) {
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
</script>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
	<form method="post" action="" class="am-form" id="form1">
		<div>
			<%=mval.Pub1.ListToString() %>
		</div>
	</form>
</body>
<script type="text/javascript">
function onSave(){
	var keys = "";
	var param = window.location.search;
	$.ajax({
		url:'SelectSave.do'+param,
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		data:$('#form1').serialize(),
		async: false ,
		error: function(data){},
		success: function(data){
			json = eval(data);
			if(json.success){
				keys = json.msg;
			}else{
			}
		}
	});
	if (window.opener != undefined) {
        window.top.returnValue = keys;
    } else {
        window.returnValue = keys;
    }
    window.close();
}
</script>
</html>