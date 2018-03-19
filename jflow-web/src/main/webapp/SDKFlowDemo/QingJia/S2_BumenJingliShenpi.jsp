<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	S2_BumenJingliShenpiModel ssm = new S2_BumenJingliShenpiModel(
			request, response);
	ssm.Page_Load();
%>
<%@ include file="/WF/head/head1.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">
	.a{
		height: 93px;
		width:682px;
		}
</style>
<script>
$(document).ready(function(){     //使用jquery的ready方法似的加载运行   
	if (window.screen) {              //判断浏览器是否支持window.screen判断浏览器是否支持screen   
	 var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽   
	 var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高   
	window.moveTo(0, 0);           //把window放在左上脚   
	 window.resizeTo(myw, myh);     //把当前窗体的长宽跳转为myw和myh   
	   }   
});

function Btn_Save_Click2(){
	$.ajax({
		cache: true,
		type: "POST",
		dataType : 'html',
		url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Save_Click2.do",
		data:$('#form1').serialize(),
	    success: function(data) {
	    	window.location.reload();
	    }
	});
}

function Btn_Track_Click2(){
	$.ajax({
		cache: true,
		type: "POST",
		dataType : 'html',
		url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Track_Click2.do",
		data : $('#form1').serialize(),
		success : function(data) {
		   var wd= window.showModalDialog(data,"dialogWidth=500px;dialogHeight=400px")
		}
	});
}
function Btn_Send_Click2(){
	$.ajax({
		cache: true,
		type: "POST",
		dataType : 'html',
		url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Send_Click2.do",
		data:$('#form1').serialize(),
	    success: function(data) {
	    	//alert(data);
	    	$("#mess").empty();
	    	$("#mess").append(data);
	    	var colors='blue';
	    	if(data.indexOf(colors) >= 0){
	    		$("#Btn_Save").attr('disabled',"true");
	    		$("#Btn_Send").attr('disabled',"true");
	    		$("#Btn_Return").attr('disabled',"true");
	    		$("#TB_BMNote").attr('disabled',"true");
	    	}
	    }
	});
}
function Btn_Return_Click2(){
	$.ajax({
		cache: true,
		type: "POST",
		dataType : 'html',
		url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Return_Click2.do",
		data : $('#form1').serialize(),
		success : function(data) {
		window.open(data,'_self') 
		}
	});
}
</script>
</head>
<body>
	<div id="mess" ></div>
	<form id="form1"  method="post">
		<input type="hidden" id="WorkID" name="WorkID" value="<%=ssm.getWorkID()%>" /> 
		<input type="hidden" id="FK_Flow" name="FK_Flow" value="<%=ssm.getFK_Flow()%>" /> 
		<input type="hidden" id="FID" name="FID" value="<%=ssm.getFID()%>" />
		<input type="hidden" id="FK_Node" name="FK_Node" value="<%=ssm.getFK_Node()%>" />
		<%
			int nodeid = Integer.parseInt(request.getParameter("FK_Node"));
			Node nd = new Node(nodeid);
		%>
		<h2>
			请假流程 - 当前节点:<%=nd.getName()%></h2>
		<br> 登录者信息:<%=WebUser.getNo()%>,<%=WebUser.getName()%>
		部门编号:<%=WebUser.getFK_Dept()%>,部门名称<%=WebUser.getFK_DeptName()%>
		<div>
			<fieldset>
				<legend>
					<font color="blue"><b>请假基本信息</b></font>
				</legend>
				<table border="1" width="700px">
					<tr>
						<th>项目</th>
						<th>数据</th>
						<th>说明</th>
					</tr>

					<tr>
						<td>请假人帐号:</td>
						<td><%=ssm.TB_No%>
						</td>
						<td>(只读)</td>
					</tr>

					<tr>
						<td>请假人名称:</td>
						<td> <%=ssm.TB_Name%></td>
						<td>(只读)</td>
					</tr>

					<tr>
						<td>请假人部门编号:</td>
						<td><%=ssm.TB_DeptNo%></td>
						<td>(只读)</td>
					</tr>

					<tr>
						<td>请假人部门名称:</td>
						<td><%=ssm.TB_DeptName%></td>
						<td>(只读)</td>
					</tr>

					<tr>
						<td>请假天数:</td>
						<td><%=ssm.TB_QingJiaTianShu%></td>
						<td>(只读)</td>
					</tr>

					<tr>
						<td>请假原因:</td>
						<td><%=ssm.TB_QingJiaYuanYin%></td>
						<td>(只读)</td>
					</tr>
				</table>
			</fieldset>

			<fieldset>
				<legend>
					<font color="blue"><b>部门经理审核信息</b></font>
				</legend>
				<%=ssm.TB_BMNote %>
			</fieldset>

			<fieldset>
				<legend>
					<font color="blue"><b>功能操作区域</b></font>
				</legend>
				<input type="button" id="Btn_Send" value="发送" onclick="Btn_Send_Click2()" /> 
				<input type="button" id="Btn_Save"value="保存" onclick="Btn_Save_Click2()" /> 
				<input type="button" id="Btn_Track" value="流程图" onclick="Btn_Track_Click2()" />
				<input type="button" id="Btn_Return" value="退回" onclick="Btn_Return_Click2()" />
			</fieldset>

			<fieldset>
				<legend>URL传值</legend>
				<font color='blue' size="2px"> <%=request.getRequestURL() + "?" + request.getQueryString()%></font>
			</fieldset>
		</div>
	</form>
</body>
</html>