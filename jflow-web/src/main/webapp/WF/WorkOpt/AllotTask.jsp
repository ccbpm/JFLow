<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%
	int FK_Node=Integer.valueOf(request.getParameter("NodeID"));
 	long FID=Long.valueOf(request.getParameter("FID") == null ? "0" :request.getParameter("FID"));
 	long WorkID=Long.valueOf(request.getParameter("WorkID") == null? "0" :request.getParameter("WorkID"));
 	String FK_Flow=request.getParameter("FK_Flow");
 	String userNo = WebUser.getNo();
 	String userName = WebUser.getName();
 	
 	StringBuilder html = new StringBuilder();
	String fk_emp = request.getParameter("FK_Emp");
	String sid = request.getParameter("SID");

	if (fk_emp != null)
	{
		if (!WebUser.CheckSID(fk_emp, sid))
		{
	return;
		}

		Emp emp = new Emp(fk_emp);
		 WebUser.SignInOfGenerLang(emp, null);
	}
	GenerWorkerLists wls = new GenerWorkerLists(WorkID, FK_Node, true);

	if (WebUser.getIsWap())
	{
		html.append("<fieldset width='100%' ><legend><a href='./WAP/Home.jsp' ><img src='/WF/Img/Home.gif' border=0/>主页</a> - 工作分配</legend>");
	
	}
	else
	{
		html.append("<fieldset width='100%' ><legend>&nbsp;工作分配&nbsp;</legend>");
		//this.AddFieldSet("工作分配");
	}
	
	String ids = "";
	//this.AddUL();
	html.append("<ul>");
	for (GenerWorkerList wl : GenerWorkerLists.convertGenerWorkerLists(wls))
	{
		if(!WebUser.getNo().equals(wl.getFK_Emp()))
		{
			ids += "," + "CB_" + wl.getFK_Emp();
			html.append("<li>");
			AllotTaskController.addCheckBox(wl,html);
			html.append("</li>");
		}
	
	}
	html.append("</ul>");
	AllotTaskController.addCheckBox(html);
	AllotTaskController.addButton(html, ids);
	html.append("<br><br>帮助:系统会记住本次的工作指定，下次您在发送时间它会自动把工作投递给您本次指定的人。");
	html.append("</fieldset>");
	//System.out.print(html);
%>
<script language="javascript" type="text/javascript">
	function RSize() {
		if (document.body.scrollWidth > (window.screen.availWidth - 100)) {
			window.dialogWidth = (window.screen.availWidth - 100).toString()
					+ "px"
		} else {
			window.dialogWidth = (document.body.scrollWidth + 50).toString()
					+ "px"
		}

		if (document.body.scrollHeight > (window.screen.availHeight - 70)) {
			window.dialogHeight = (window.screen.availHeight - 50).toString()
					+ "px"
		} else {
			window.dialogHeight = (document.body.scrollHeight + 115).toString()
					+ "px"
		}

		window.dialogLeft = ((window.screen.availWidth - document.body.clientWidth) / 2)
				.toString()
				+ "px"
		window.dialogTop = ((window.screen.availHeight - document.body.clientHeight) / 2)
				.toString()
				+ "px"
	}

	function SetSelected(cb) {
		$("input[type='checkbox']").attr('checked', cb.checked);
	}

	function checkSubmit(ids) {
		var arrmp = ids.split(',');
		var is_his_click = false;
		for (var idx = 0; idx <= arrmp.length; idx++) {
			if (arrmp[idx] == '')
				continue;
			var ckb = document.getElementById(arrmp[idx]);
			if (ckb && ckb.checked) {
				is_his_click = true;
				break;
			}
		}
		if (is_his_click == false) {
			alert("当前工作中你没有分配给任何人，此工作将不能被其他人所执行！");
		} else {
			document.getElementById("btn_submit").click();
		}
	}
</script>
</head>
<body>
	<div class="admin-content">
				<form method="post"
					action="AllotTask.do?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>"
					id="form1" class="am-form">
					<div
						style="text-align: center; position: absolute; background-color: white; height: 100%; left: 15%; right: 15%">

						<table border=1px align=center width='100%'>
							<Caption ><div class='' >您好:<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),
					WebUser.getName())%></div></Caption>
							<%-- <th class="table-title">您好:<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),
					WebUser.getName())%>
							</th> --%>
							<tr>
								<td nowrap="nowrap"><%=html.toString()%></td>
							</tr>
						</table>
						<input type="submit" hidden id="btn_submit" />
					</div>
				</form>
	</div>
</body>
</html>

