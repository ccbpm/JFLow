<%@page import="cn.jflow.common.model.S11001Model"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	//String path = request.getContextPath();
	//String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	S11001Model s1 = new S11001Model(
			request, response);
	s1.Page_Load();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
 <script type="text/javascript">
	function Btn_Send_Click(){
		$.ajax({
			cache: true,
			type: "POST",
			dataType : 'html',
			url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Send_Click.do",
			data:$('#form1').serialize(),
		    success: function(data) {
		    	//alert(data);
		    	$("#mess").empty();
				
		    	$("#mess").append(data);
		    	var colors='blue';
		    	if(data.indexOf(colors) >= 0){
		    		$("#Btn_Save").attr('disabled',"true");
		    		$("#Btn_Send").attr('disabled',"true");
		    	}
		    }
		});
	}
	
	function Btn_Save_Click(){
		
		$.ajax({
			cache: true,
			type: "POST",
			dataType : 'html',
			url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Save_Click.do",
			data:$('#form1').serialize(),
		    success: function(data) {
		    	window.location.reload();
		    }
		});
	}
	function Btn_Track_Click(){
		$.ajax({
			cache: true,
			type: "POST",
			dataType : 'html',
			url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Track_Click.do",
			data : $('#form1').serialize(),
			success : function(data) {
			   var wd= window.showModalDialog(data,"dialogWidth=500px;dialogHeight=400px")
			}
		});
	}
	
	 //重写执行发送的方法.
    function Send() {
    	Btn_Send_Click();
    }
	 //重写执行发送的方法.
    function Save() {
    	Btn_Save_Click();
    }
</script> 
</head>

<body>
	<div id="mess"></div>
	<form id="form1" action="" method="post">
		<input type="hidden" id="WorkID" name="WorkID" value="<%=s1.getWorkID()%>" /> 
		<input type="hidden" id="FK_Flow" name="FK_Flow" value="<%=s1.getFK_Flow()%>" /> 
		<input type="hidden" id="FID" name="FID" value="<%=s1.getFID()%>" />
		<input type="hidden" id="FK_Node" name="FK_Node" value="<%=s1.getFK_Node()%>" />
		<%
			int nodeid = Integer.parseInt(request.getParameter("FK_Node"));
			Node nd = new Node(nodeid);
		%>

	<!-- 嵌入式表单部分........................... -->
<table style="width:100%; text-align:left; background-color:White">
<tr>
<td>
<!-- 把工具栏从sdk组建库里引入进来, 该工具栏目上面的所有按钮都可以通过节点属性按钮区域配置. -->
		 <jsp:include page="/WF/SDKComponents/Toolbar.jsp"></jsp:include>
    
</td>
</tr>
<tr>
<td  class="Title">
<fieldset>
<legend><font color=green ><b>请假基本信息</b></font></legend>
<table border="1" width="100%">
    <tr>
				<td>请假人帐号:</td>
				<td><%=s1.TB_No%></td>
				<td>(只读)当前登录人登录帐号BP.Web.WebUser.No</td>
	</tr>
<td>(只读)当前登录人登录帐号BP.Web.WebUser.No</td>
</tr>


<tr>
<td>请假人名称:</td>
   				<td><%=s1.TB_Name%></td>
				<td>(只读)当前登录人名称BP.Web.WebUser.Name</td>
</tr>


<tr>
<td>请假人部门编号:</td>

    			<td><%=s1.TB_DeptNo%></td>
				<td>(只读)当前登录人部门编号BP.Web.WebUser.FK_Dept</td>
</tr>

<tr>
<td>请假人部门名称:</td>

			<td><%=s1.TB_DeptName%></td>
			<td>(只读)当前登录人部门名称BP.Web.WebUser.FK_DeptName</td>
</tr>


<tr>
<td>请假天数:</td>

			<td><%=s1.TB_QingJiaTianShu %></td>
			<td>请输入一个数字</td>
</tr>

<tr>
<td>请假原因:</td>

   			<td><%=s1.TB_QingJiaYuanYin %></td>
<td></td>
</tr>
</table>
</fieldset>
<!-- end 嵌入式表单部分........................... -->

 </td>
</tr>

				<tr>
					<td  class="Title"> 单附件</td>
				</tr>

				<tr>
				<td> 
    			<div style ="display:none">
        			<jsp:include page="/WF/SDKComponents/DocMainAth.jsp"></jsp:include>
        		</div>
    			</td>

				</tr>

				<tr>
					<td class="Title"> 多附件</td>
				</tr>

				<tr>
				<td> 
    
   					 <jsp:include page="/WF/SDKComponents/DocMultiAth.jsp"></jsp:include>
   					 
    			</td>
				</tr>


<!-- 审核组件 ....................  -->
<% 

     String str = request.getParameter("FK_Node") == null? "": request.getParameter("FK_Node");
    if (str != "11001")
    {
        /*如果不是开始节点，就不它显示审核按钮. */
     %>
    <tr>
   <td  class="Title" >审核组件</td>
    </tr>
<tr>
<td style=" width:100%; height:100%;"> 

     <jsp:include page="/WF/SDKComponents/frmcheck.jsp"></jsp:include>

</td>
</tr>
<%  } %>

<tr>
<td class="Title"> 流程轨迹图组件 </td>
</tr>

<tr>
<td>  
          
		 <jsp:include page="/WF/SDKComponents/TruakOnly.jsp"></jsp:include>
    </td>
</tr>

</table>
	</form>
</body>
</html>