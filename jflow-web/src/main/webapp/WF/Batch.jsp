<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%
	String fk_node = request.getParameter("FK_Node") == null?"0":request.getParameter("FK_Node");
	int FK_Node = Integer.valueOf(fk_node);

	String FK_MapData = "ND" + FK_Node;
	int ListNum = 12;
	String Key = request.getParameter("Key") == null?"":request.getParameter("Key");
	String DoType = request.getAttribute("DoType") == null?"":request.getAttribute("DoType").toString();
	String normMsg = request.getAttribute("normMsg") == null?"":request.getAttribute("normMsg").toString();

	BatchModel model = new BatchModel(basePath, FK_Node, FK_MapData, ListNum, Key, DoType, normMsg);
	model.init();
%>
<style type="text/css">
<!--
body,td{
 font-family:微软雅黑;
 cursor:default;
 font-size:12px;
}
a{
 font-size:12px;
 color:blue;
 line-height:160%;
 text-decoration:none;
}
a:link{
 font-size:12px;
 color:#006699;
 line-height:160%;
 text-decoration:none;
}
a:active{
 font-size:12px;color:#990000;
 line-height:160%;
}
a:hover{
 font-size:12px; color:#3366ff;
 line-height:160%;
 text-decoration:none;
}
li{
 list-style-type:square; 
 margin:0px;
 padding:0px;
 height:15px;
}
.liOver{
 background-color:#dddddd;
 border:1px solid #000000;
}
.liout{
 background-color:#f1f1f1;
 border:1px solid #f1f1f1;
}
li span.lidown{
 background-color:#00ff00;
 border:1px solid #999999;
}
-->
</style>
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
function BatchGroup() {
    var btn = document.getElementById("Btn_Group");
    if (btn) {
        btn.click();
    }
}
function Send_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	if(vailCheckBox()){
		alert("您没有选择工作！");
		return false;
	}
	if(confirm("您确定要执行吗？")){	
		//$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>WF/BatchSend.do?FK_Node="+FK_Node+"&FK_MapData="+FK_MapData+"&ListNum="+ListNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function Group_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	if(vailCheckBox()){
		alert("您没有选择工作！");
		return false;
	}
	
	if(confirm("您确定要执行吗？")){	
		var url = "<%=basePath%>WF/BatchGroup.do?FK_Node="+FK_Node+"&FK_MapData="+FK_MapData+"&ListNum="+ListNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function Return_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	if(vailCheckBox()){
		alert("您没有选择工作！");
		return false;
	}
	if(confirm("您确定要执行吗？")){	
		var url = "<%=basePath%>WF/BatchReturn.do?FK_Node="+FK_Node+"&FK_MapData="+FK_MapData+"&ListNum="+ListNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function Delete_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	if(vailCheckBox()){
		alert("您没有选择工作！");
		return false;
	}
	if(confirm("您确定要执行吗？")){	
		var url = "<%=basePath%>WF/BatchDelete.do?FK_Node=" + FK_Node
					+ "&FK_MapData=" + FK_MapData + "&ListNum=" + ListNum;
			$("#form1").attr("action", url);
			$("#form1").submit();
		}
	}
	function vailCheckBox() {
		var vail = true;
		$.ajax({
			url : "LoadWorId.do",
			type : 'post',
			dataType : 'json',
			async : false,
			success : function(data) {
				if (data != "") {
					var emps = eval(data);
					for ( var emp in emps) {
						var value = emps[emp].workid;
						var obj = $("#CB_" + value);
						if (obj.length <= 0)
							continue;
						if (obj.attr("checked")) {
							vail = false;
						}
					}
				}
			}
		});
		return vail;
	}
</script>
</head>
<body style="margin: 1px;">
<form method="post" action="" id="form1">
	<!-- <input type="hidden" id="FormHtml" name="FormHtml" value=""></input> -->
	<%=model.ui.ListToString()%>
</form>
</body>
</html>