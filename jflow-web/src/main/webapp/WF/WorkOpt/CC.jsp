<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />

<%
	String FK_Flow = (String) request.getParameter("FK_Flow");
	String WorkID = request.getParameter("WorkID");
	String FK_Node = request.getParameter("FK_Node");
	String FID = request.getParameter("FID");
	String errmsg = "";
	Flow fl = new Flow(FK_Flow);
	String sql = "SELECT Title FROM " + fl.getPTable() + " WHERE OID="
			+ WorkID;
	if (!StringHelper.isNullOrEmpty(FID) && !"0".equals(FID)) {
		sql = "SELECT Title FROM " + fl.getPTable() + " WHERE OID="
				+ FID;
	}
	String title = DBAccess.RunSQLReturnStringIsNull(sql, null);
	if (StringHelper.isNullOrEmpty(title)) {
		//errmsg = "系统出现异常，请联系管理员。";
		title = WebUser.getNo() + "在 "
				+ DataType.getCurrentDataCNOfShort() + "发起";
	}
%>
<script type="text/javascript">
	function ShowIt() {
		var tbValue = $("#TB_Accepter").val();
		var url = 'SelectUser.jsp?OID=123&CtrlVal=' + tbValue;
		var v = window
				.showModalDialog(
						url,
						null,
						'dialogHeight: 450px; dialogWidth: 650px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
		if (v == null || v == '' || v == 'NaN') {
			return;
		}
		var arr = v.split('~');
		$("#TB_Accepter").val(arr[0]);
		$("#TB_Accepter_name").val(arr[1]);
		
	}
	function ccSubmit(){
		var accepters = $("#TB_Accepter").val();
		if(accepters == "" || accepters == null){
			alert("接受人不能为空");
			return;
		}
		var title = $("#TB_Title").val();
		if(title == "" || title == null){
			alert("标题不能为空");
			return;
		}
		var doc = $("#TB_Doc").val();
		var url = "<%=basePath%>WF/WorkOpt/CC.do?WorkID=<%=WorkID%>&FK_Node=<%=FK_Node%>&FK_Flow=<%=FK_Flow%>&FID=<%=FID%>&TB_Accepter="
				+ accepters + "&TB_Title=" + title + "&doc=" + doc;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
</script>
</head>
<body>
	<table border=1px align=center width='100%'>
		<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></div></Caption>

	<!-- <div class="divCenter2"> -->
			<form method="post" action="" class="am-form" id="form1">
				<!-- <table class="am-table am-table-striped am-table-hover table-main"> -->
					<thead>
						<tr>
							<th class="table-check" colspan="3">
								请选择或者输入人员(多个人员用逗号隔开),然后点发送按钮...</th>
						</tr>
						<tr>
							<td nowrap="nowrap" class="am-text-center">接受人：</td>
							<td valign="top" nowrap="nowrap">
								<div>
									<input name="TB_Accepter_name" type="text"
										id="TB_Accepter_name"
										style="width: 500px; height: 25px; padding: 0" /> <input
										name="TB_Accepter" type="hidden" id="TB_Accepter" />
								</div>
							</td>
							<td><input type="button" name="mybtn" id="mybtn"
								value="选择接受人" onclick="ShowIt();"
								class="am-btn am-btn-primary am-btn-xs" /></td>
						</tr>
						<tr>
							<td nowrap="nowrap" class="am-text-center">标题：</td>
							<td colspan="2" nowrap="nowrap"><input name="TB_Title"
								type="text" value="<%=title%>" id="TB_Title"
								style="width: 500px; height: 40px; padding: 0" /></td>
						</tr>
						<tr>
							<td nowrap="nowrap" class="am-text-center">消息内容：</td>
							<td colspan="2" nowrap="nowrap"><textarea name="TB_Doc"
									rows="12" cols="20" id="TB_Doc" style="width: 500px;"></textarea>
							</td>
						</tr>
						<tr>
							<td></td>
							<td colspan="2" nowrap="nowrap"><input type="button"
								name="btn" id="btn" value="执行抄送" onclick="ccSubmit();"
								class="am-btn am-btn-primary am-btn-xs" /></td>
						</tr>
					</thead>
				<!-- </table> -->
			</form>
		</table>
</body>
</html>
