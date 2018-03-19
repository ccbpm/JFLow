<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" cn.jflow.model.wf.rpt.HungUpModel"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Web.WebUser"%>
<script type="text/javascript" src="../../WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<link href="../../DataUser/Style/table0.css" rel="stylesheet" type="text/css" /><link rel="shortcut icon" href="../Img/ccbpm.ico" type="image/x-icon" />
<link href="../Comm/Style/Table.css" rel="stylesheet" type="text/css" />
<link href="../Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
<link href="../Comm/JS/Calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="../Comm/JS/Calendar/WdatePicker.js" type="text/javascript" ></script>




<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	String isCheckForever = request.getParameter("isCheckForever");
	String FK_Flow = (String) request.getParameter("FK_Flow");
	String WorkID = request.getParameter("WorkID");
	String FK_Node = request.getParameter("FK_Node");
	String FID = request.getParameter("FID");
	String title = request.getParameter("title");
	String DTOfUnHungUpPlan = request.getParameter("DTOfUnHungUpPlan");
	String isHungUp = request.getParameter("isHungUp");
	
	HungUpModel hum=new HungUpModel(request,response);
	hum.init();
%>
<script type="text/javascript" language="JavaScript">
function hungUpSubmit(obj){
	var RB_HungWay = $('input:radio:checked').val();
	var TB_RelData = $("#TB_RelData").val();
	var TB_Note = $("#TB_Note").val();
	var btn_id = obj.id;
	if(btn_id == "Btn_Cancel"){
		<%-- var url = "<%=basePath%>WF/MyFlow<%=Glo.getFromPageType()%>.jsp?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>"; --%>
		//window.location.href = url;
		window.close();
	}else{
		 	if(confirm('确定要执行挂起操作吗?')){ 
				<%-- var url = "<%=basePath%>WF/WorkOpt/doHungUp.do?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>&RB_HungWay="
						+ RB_HungWay
						+ "&TB_RelData="
						+ TB_RelData
						+ "&TB_Note="
						+ TB_Note;
				$("#form1").attr("action", url);
				$("#form1").submit(); --%>
				
				$.ajax({
					url:'<%=basePath%>WF/WorkOpt/ajaxHungUp.do?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>&RB_HungWay='+RB_HungWay,
					type:'post', //数据发送方式
					data:$('#form1').serialize(),
					async: false ,
					error: function(data){},
					success: function(data){
						if(data=="success"){
							alert("操作成功！");
							window.close();
						}else{
							alert("操作失败！");
						}
					}
				});
				
			}
		}
	}
	function NoSubmit(ev) {
		if (window.event.srcElement.tagName == "TEXTAREA")
			return true;
		if (ev.keyCode == 13) {
			window.event.keyCode = 9;
			ev.keyCode = 9;
			return true;
		}
		return true;
	}
</script>
</head>
<body>
<form method="post" action="" id="form1">
<table border=1px width='100%'>
	<Caption>
		<div class=''>
			您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%> -  工作挂起与取消</div>
	</Caption>
	<tr>
		<td>
			<%=hum.Pub1.ListToString() %>
		
		</td>
	</tr>
</table>
</form>
</body>
</html>
