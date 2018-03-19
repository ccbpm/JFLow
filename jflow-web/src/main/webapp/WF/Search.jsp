<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	SearchCommModel search = new SearchCommModel(request, response, basePath);
	search.init();
%>
<script type="text/javascript">
//新建
function ShowEn(url, wName, h, w) {
	if("0" == h){
		h = "500";
	}
	if("0" == w){
		w = "820";
	}
	var s = "dialogWidth=" + parseInt(w) + "px;dialogHeight=" + parseInt(h) + "px;resizable:yes";
	windows.open(url);
	//val = window.showModalDialog(url, null, s);
	
	//win
	onSearch();
}
//设置
function OpenAttrs(ensName) {
	var url = '<%=basePath%>WF/Comm/Sys/EnsAppCfg.jsp?EnsName=' + ensName;
	var s = 'dialogWidth=680px;dialogHeight=480px;status:no;center:1;resizable:yes';
	val = window.showModalDialog(url, null, s);
	onSearch();
}
//多项组合选项
function DDL_mvals_OnChange(ctrl, ensName, attrKey) {
	//var value =  $(ctrl).find("option:selected").text();
	var value = $(ctrl).val();
	if ("mvals" == value && null != attrKey){
		var url = '<%=basePath%>WF/Comm/SelectMVals.jsp?EnsName=' + ensName + '&AttrKey='+ attrKey;
		var val = window.showModalDialog(url, 'dg',	'dialogHeight: 450px; dialogWidth: 450px; center: yes; help: no');
		if (val == '' || val == null) {
			ctrl.selectedIndex = 0;
		}
	}
}
function onCasca(bigDDL, smallDDL, ensName, key){
	var ddlValue = $.trim($("#"+bigDDL).val());
	$.ajax({
        url:"<%=basePath%>App/SearchCasca.do",
        type:'post',
        dataType:'json',
        data: {"Ddl_Value": ddlValue, "Ens_Name": ensName, "Attr_Key1": key},
        success: function (data) {
        	json = eval(data);
			if(json.success){
				var opt = json.attributes.data;
				if("" != opt && null != opt){
					$("#"+smallDDL).empty();
					$("#"+smallDDL).html(opt);
				}
			}else{
				alert(json.msg);
			}
        }
    });
}
function onTwoCasca(bigDDL, smallDDL, litSmallDDL, ensName, key1, key2){
	var ddlValue = $.trim($("#"+smallDDL).val());
	
	var type = "0";
	if("all" == ddlValue){
		type = "1";
		ddlValue = $.trim($("#"+bigDDL).val());
	}
	$.ajax({
        url:"<%=basePath%>App/SearchTwoCasca.do",
        type:'post',
        dataType:'json',
        data: {"Ddl_Value": ddlValue, "Ens_Name": ensName, "Attr_Key1": key1, "Attr_Key2": key2, "Ddl_type": type},
        success: function (data) {
        	json = eval(data);
			if(json.success){
				var opt = json.attributes.data;
				if("" != opt && null != opt){
					$("#"+litSmallDDL).empty();
					$("#"+litSmallDDL).html(opt);
				}
			}else{
				alert(json.msg);
			}
        }
    });
}
function onThreeCasca(bigDDL, smallDDL, litSmallDDL, ensName, key1, key2){
	var ddlValue = $.trim($("#"+bigDDL).val());
	$.ajax({
        url:"<%=basePath%>App/SearchThreeCasca.do",
        type:'post',
        dataType:'json',
        data: {"Ddl_Value": ddlValue, "Ens_Name": ensName, "Attr_Key1": key1, "Attr_Key2": key2},
        success: function (data) {
        	json = eval(data);
			if(json.success){
				var opt1 = json.attributes.data1;
				if("" != opt1 && null != opt1){
					$("#"+smallDDL).empty();
					$("#"+smallDDL).html(opt1);
				}
				var opt2 = json.attributes.data2;
				if("" != opt2 && null != opt2){
					$("#"+litSmallDDL).empty();
					$("#"+litSmallDDL).html(opt2);
				}
			}else{
				alert(json.msg);
			}
        }
    });
}
</script>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0" onload="onSearch()">
	<table align="left" CellSpacing="1" CellPadding="1" border="0"	width="100%">
		<caption>
			<%=search.label.getText()%>
		</caption>
		<tr>
			<td id="ToolBar" class="ToolBar" >
				<form id="form1">
					<%=search.toolBar.toString()%>
				</form>	
			</td>
		</tr>

		<tr align="justify" style="height: 350px" valign="top">
			<td width='100%'>
				<div id="UCSys1"></div>
				<br>
				<div id="UCSys2"></div>
			</td>
		</tr>
	</table>
</body>
<script type="text/javascript">
function onSearch(pageIdx) {
	if (typeof(pageIdx) == "undefined"){
		pageIdx = 1;
	} 
	var param = window.location.search;
	$.ajax({
		url : "<%=basePath%>App/SearchComm.do"+param+"&PageIdx="+pageIdx,
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		data:$('#form1').serialize(),
		async: false ,
		error: function(data){},
		success: function(data){
			json = eval(data);
			if(json.success){
				$("#UCSys1").html(json.attributes.UCSys1);
 				$("#UCSys2").html(json.attributes.UCSys2);
			}else{
				alert(json.msg);
			}
		}
	});
}
function onReset(){
	$('#form1')[0].reset();
}
</script>
</html>