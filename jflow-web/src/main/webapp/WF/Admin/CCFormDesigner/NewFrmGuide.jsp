<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="cn.jflow.model.wf.admin.FoolFormDesigner.NewFrmGuideModel" %>
<%@ page import="BP.Web.WebUser" %>
<%@ include file="/WF/head/head1.jsp"%>
<%
	//String path = request.getContextPath();
	//String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	NewFrmGuideModel fm=new NewFrmGuideModel(request,response);
    fm.Page_Load();
	//int i=0;
%>
<html>
<head>
<title>ccbpm表单创建向导</title>
<link href="../../../DataUser/Style/table0.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery.form.min.js"></script>	
<script type="text/javascript">
	function Back() {
		window.location.href = 'NewFrmGuide.jsp';
	}
</script>
<style type="text/css">
.title {
	text-decoration: none;
	font-size: 16px;
	color: #ffffff;
	background: cornflowerblue;
	padding: 5px 10px;
	margin: 0 5px;
	border-radius: 3px;
	box-shadow: 0px 1px 2px;
}

.con-list {
	line-height: 30px;
	font-size: 13px;
}

fieldset {
	border: 1px solid #c7ced3;
	margin-bottom: 20px;
}

.link-img {
	float: right;
	padding-bottom: 10px;
	margin-right: 10px;
}

table caption {
	border: 1px solid #C2D5E3;
	border-bottom: none;
	line-height: 30px !important;
}
</style>
<script>
	function tb_TextChanged(){
		var param = window.location.href.split("?")[1];
		var url = '<%=basePath%>WF/NewFrmGuide/tb_TextChanged.do?'+param;
		$.ajax({
			url:url,
			type:'post', //数据发送方式
			async: true ,
			dataType:"json",
			data:$('#form1').serialize(),
			error: function(data){
			},
			success: function(data){
				$("#TB_No").val(data.TB_No);
				$("#TB_PTable").val(data.TB_PTable);
			}
		});
	}
	function BindStep1_Click(){
		var param = window.location.href.split("?")[1];
		var url = '<%=basePath%>WF/NewFrmGuide/BindStep1_Click.do?'+param;
		$("#form1").ajaxSubmit({
            type: "post",
            url: url,
            //dataType: "json",
            //data: { "filename": $("#fuldDoc").val() },
            success: function (data) {
                alert(data);
            },
            error: function (data) {
            	alert(data);
            }
        });
		return false;
	}
	/* function showResponse(responseText, statusText) {
         alert(responseText); 
    }  */ 
</script>
</head>
<body>
	<form id="form1" action="<%=basePath%>WF/NewFrmGuide/BindStep1_Click.do"  method="post">
		<!-- <uc1:Pub ID="Pub1" runat="server" /> -->
		<%=fm.Pub1.toString()%>
		<input type="submit" style="display:none"/>
	</form>
	<script type="text/javascript">
		window.onload = function() {
			var tableWidth = document.getElementsByTagName("table")[0].offsetWidth;
			document.getElementsByTagName("table")[0].Width = tableWidth;
		}
	</script>
</body>
</html>