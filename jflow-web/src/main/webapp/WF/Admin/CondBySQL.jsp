<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	CondBySQLModel con = new CondBySQLModel(request, response);
	con.Page_Load();
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
	function save()
	{
		var sql=document.getElementById("TB_SQL").value;
		var btnId="Btn_Save";
		if(sql==null || sql== "")
			{
				alert("请输入SQL语句");
				return ;
			}
		var MyPK='<%=con.getMyPK()%>';
		var ToNodeID='<%=con.getToNodeID()%>';
		var HisCondType='<%=con.getHisCondType()%>';
		var FK_Flow='<%=con.getFK_Flow()%>';
		var FK_MainNode='<%=con.getFK_MainNode()%>';
		var FK_Node='<%=con.getFK_Node()%>';
		location.href="<%=basePath%>des/condBySQL_btn_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&sql=" + sql + "&ToNodeID=" + ToNodeID+"&btnId="+btnId;
	}
	function del()
	{
		var sql=document.getElementById("TB_SQL").value;
		var btnId="Btn_Delete";
		if(sql==null || sql== "")
			{
				alert("请输入SQL语句");
				return ;
			}
		var MyPK='<%=con.getMyPK()%>';
		var ToNodeID='<%=con.getToNodeID()%>';
		var HisCondType='<%=con.getHisCondType()%>';
		var FK_Flow='<%=con.getFK_Flow()%>';
		var FK_MainNode='<%=con.getFK_MainNode()%>';
		var FK_Node='<%=con.getFK_Node()%>';
		location.href="<%=basePath%>des/condBySQL_btn_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&sql=" + sql + "&ToNodeID=" + ToNodeID+"&btnId="+btnId;
	}
</script>
</head>
<body onLoad="load()">
	<input type="hidden" id="success" value="${success }"/>
	<%=con.ui.ListToString()%>
</body>
</html>