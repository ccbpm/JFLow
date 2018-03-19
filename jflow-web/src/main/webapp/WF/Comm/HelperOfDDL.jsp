<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

	<%
		HelperOfDDLModel helperOfDDLModel=new HelperOfDDLModel(request, response);
		helperOfDDLModel.loadPage();
	%>
	
<%@ include file="/WF/head/head1.jsp"%>
<title>感谢您选择</title>
<script type="text/javascript">
	function Btn_OK_Click(){
		$("#BodyHtml").val($("#form2").html());
		document.getElementById('form2').action="<%=basePath%>Wf/Comm/Btn_OK_Click.do?EnsName=<%=helperOfDDLModel.getEnsName()%>";
		document.getElementById('form2').submit();
	}
	function onchangee(){
		$.ajax({
			
	        url:"<%=basePath%>Wf/Comm/selectKuang.do",
	        type:"post",
	        dataType:"html",
	        data: {
	        	EnsName:'<%=helperOfDDLModel.getEnsName()%>', 
	        	selectName:$("#selectName").val(),
	        	RefKey:'No',
	        	RefText:'Name'
	        },
	        success: function (date) {
	            $("#form2").html(date);
	        }
	    });
	}

</script>
</head>
<body class="Body<%=WebUser.getStyle()%>"   leftMargin="0"  topMargin="0" >
    <input type="hidden" name="BodyHtml" id="BodyHtml">
   	<form id="Form1" method="post">
			<table align="left" cellSpacing="1" cellPadding="1" width="100%" border="1" class="Table" >
				<tr>
					<td Class="toolbar" width="90%">
                           <strong>选择分组</strong>
						   <%=helperOfDDLModel.DropDownList1.toString() %>
					 		提示:您可点击选中并返回。
					</td>
				</tr>
			
	</form>
	
	 
	<tr>
	<td>
	
    <form id="form2" method="post" >
		<%= helperOfDDLModel.UCSys1.toString() %>
		<%= helperOfDDLModel.UCSys2.toString() %>
    </form>
    
    </td>
    </tr>
    </table>
</body>
</html>