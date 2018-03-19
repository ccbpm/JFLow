<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
</head>
<%@ include file="/WF/head/head1.jsp"%>
<%
	CondByUrlModel condModel=new CondByUrlModel(request,response);
condModel.Page_Load();
String success=request.getParameter("success");
%>
<script type="text/javascript">
function load() {
	var success = document.getElementById("success").value;
	if (success == "" || success == null) {
		return;
	} else {
		alert(success);
	}

}
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
	function btn_Click()
	{
		var exp=document.getElementById("TB_Para").value;
		if(exp==null || exp=="")
			{
				alert("URL不能为空!");
				return;
			}
		if(exp < 1)
		{
		alert("URL不能小于1!");
		return;
		}
		var btnId="Btn_Save";
		var MyPK='<%=condModel.getMyPK()%>';
		var ToNodeID='<%=condModel.getToNodeID()%>';
		var HisCondType='<%=condModel.getHisCondType()%>';
		var FK_Flow='<%=condModel.getFK_Flow()%>';
		var FK_MainNode='<%=condModel.getFK_MainNode()%>';
		var FK_Node='<%=condModel.getFK_Node()%>';
		location.href="<%=basePath%>des/CondByurl_btn_Save_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&exp=" + exp + "&ToNodeID=" + ToNodeID+"&btnId="+btnId;
	}
	function btn_del()
	{
		var exp=document.getElementById("TB_Para").value;
		if(exp==null || exp=="")
			{
				alert("URL不能为空!");
				return;
			}
		if(exp < 0)
			{
			alert("URL不能小于0!");
			return;
			}
		var btnId="Btn_Delete";
		var MyPK='<%=condModel.getMyPK()%>';
		var ToNodeID='<%=condModel.getToNodeID()%>';
		var HisCondType='<%=condModel.getHisCondType()%>';
		var FK_Flow='<%=condModel.getFK_Flow()%>';
		var FK_MainNode='<%=condModel.getFK_MainNode()%>';
		var FK_Node='<%=condModel.getFK_Node()%>';
		location.href="<%=basePath%>des/CondByurl_btn_Save_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&exp=" + exp + "&ToNodeID=" + ToNodeID+"&btnId="+btnId;
	}
</script>
<body onLoad="load()">
<input type="hidden" id="success" value="${success }"/>
<form method="post" action="" class="am-form" id="form1">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			    <%=condModel.ui.ListToString() %>
			</div>
		</div>
	</form>
		
</body>
</html>