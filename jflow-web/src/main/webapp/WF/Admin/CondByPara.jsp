<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	CondByParaModel con = new CondByParaModel(request, response);
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
	function btn_Save_Click()
	{
		var exp=document.getElementById("TB_Para").value;
		var btnId="Btn_Save";
		if(exp==null || exp== "")
			{
				alert("请输入系统参数!");
				return ;
			}
		var MyPK='<%=con.getMyPK()%>';
		var ToNodeID='<%=con.getToNodeID()%>';
		var HisCondType='<%=con.getHisCondType()%>';
		var FK_Flow='<%=con.getFK_Flow()%>';
		var FK_MainNode='<%=con.getFK_MainNode()%>';
		var FK_Node='<%=con.getFK_Node()%>';
		location.href="<%=basePath%>des/condByPara_btn_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&exp=" + exp + "&ToNodeID=" + ToNodeID+"&btnId="+btnId;
	}
	function btn_Del_Click()
	{
		var exp=document.getElementById("TB_Para").value;
		var btnId="Btn_Delete";
		if(exp==null || exp== "")
			{
				alert("请输入系统参数!");
				return ;
			}
		var MyPK='<%=con.getMyPK()%>';
		var ToNodeID='<%=con.getToNodeID()%>';
		var HisCondType='<%=con.getHisCondType()%>';
		var FK_Flow='<%=con.getFK_Flow()%>';
		var FK_MainNode='<%=con.getFK_MainNode()%>';
		var FK_Node='<%=con.getFK_Node()%>';
		location.href="<%=basePath%>des/condByPara_btn_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&exp=" + exp + "&ToNodeID=" + ToNodeID+"&btnId="+btnId;
	
	}
</script>
</head>
<body onload="load()">
	<input type="hidden" id="success" value="${success }"/>
	<%=con.ui.ListToString() %>
</body>
</html>