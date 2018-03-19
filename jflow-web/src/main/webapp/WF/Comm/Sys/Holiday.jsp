<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	HolidayModel holi = new HolidayModel(request,response);
	holi.loadPage();
%>
<body>
<script>
function Btn_Save_Click(){
	$.ajax({
		cache:true,
		type:"POST",
		url:"<%=basePath%>WF/Holiday/Btn_Save_Click.do",
		data:$("#form1").serialize(),
		success:function(data){
			alert("保存成功");
		},
		error:function(data){
			alert("保存失败");
		}
	});
}
</script>
<form id="form1" runat="server">

    <center>
     <table border=1  style="width: 98%">
     <caption>节假日设置(请在属于节假日的日期里打勾)</caption>
     <tr>
     <th>序</th>
     <th>月份</th>
     <th>周日</th>
     <th>周一</th>
     <th>周二</th>
     <th>周三</th>
     <th>周四</th>
     <th>周五</th>
     <th>周六</th>
     </tr>
	 <%=holi.Pub1.toString() %>
     </table>
     <hr />
         <input type="button" ID="Btn_Save" runat="server" value="保存"  onclick="Btn_Save_Click()" />
     </center>
</form>
</body>