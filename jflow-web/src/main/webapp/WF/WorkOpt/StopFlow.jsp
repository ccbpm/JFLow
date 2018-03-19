<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />

<%
	int FK_Node = Integer.valueOf(request.getParameter("FK_Node"));
	long FID = Long.valueOf(request.getParameter("FID"));
	long WorkID = Long.valueOf(request.getParameter("WorkID"));
	String FK_Flow = request.getParameter("FK_Flow");
	String userNo = WebUser.getNo();
	String userName = WebUser.getName();
%>
<script type="text/javascript">
	function onCancel(){
		var url = "<%=basePath%>WF/MyFlow.htm?FK_Node=<%=FK_Node %>&WorkID=<%=WorkID %>&FID=<%=FID %>&FK_Flow=<%=FK_Flow %>";
		window.location.href = url;
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
	function stopFlow(){
		if($("#TextBox1").val().length==0){
			alert('请输入强制终止流程的原因');
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<form method="post" onsubmit="return stopFlow();"
		action="<%=basePath%>WF/WorkOpt/StopFlow.do?DoType=StopFlow&FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>"
		id="form1" class="am-form">
		<div
			style="position: absolute; background-color: white; height: 100%; left: 15%; right: 15%">
			<table border=1px align=center width='100%'>
				<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></div></Caption>
				<tr>
					<td valign=top>
						<table class="am-table am-table-striped am-table-hover table-main" style="width: 100%">

							<tr>
								<th valign=top colspan=2>结束流程(请输入结束流程的原因)</th>
							</tr>

							<tr>
								<td valign=top>说明:
									<ul style="margin: 3px">
										<li>1.流程结束标识流程已经走完,以后的节点不再执行.</li>
										<li>2.走完后的流程在已完成的流程中可以查询到.</li>
										<li>3.要想恢复流程向下运行需求告知管理员.</li>
									</ul>
								</td>
								<td valign=top style="width: 300px" colspan=2><textarea
										name="TextBox1" rows="2" cols="20" id="TextBox1"
										style="height: 110px; width: 100%;"></textarea></td>
							</tr>
						</table> </td>
				</tr>
			</table>
			
			<div class="am-margin">
						<button type="submit" name="Btn_OK" id="Btn_OK" 
								 class="am-btn am-btn-primary am-btn-xs">确定</button>
								<!-- <input type="submit" name="Btn_OK"
									value="确定" id="Btn_OK"
									onclick="if(document.getElementById('TEXTBOX1').value == ''){alert('请输入强制终止流程的原因');return false;}" /> -->
									<button type="button" name="Btn_Cancel" 
									onclick="onCancel();" id="Btn_Cancel" class="am-btn am-btn-primary am-btn-xs">取消</button>
									<!-- <input type="submit" name="Btn_Cancel" value="取消"
									onclick="window.location.go(-1);" id="Btn_Cancel" /></td> --></div>
		</div>
	</form>
</body>
</html>

