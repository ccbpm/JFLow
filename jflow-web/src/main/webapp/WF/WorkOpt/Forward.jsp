<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%
	String errMsg = request.getParameter("errMsg")==null?"":request.getParameter("errMsg");
	 if(null != errMsg && "" != errMsg){
		 out.println("<script>alert('"+errMsg+"');</script>");
	 }
	
	 String workId = request.getParameter("WorkID")==null?"0":request.getParameter("WorkID");
	 long WorkID=Long.valueOf(workId);
	 String  fid = request.getParameter("FID")==null?"0":request.getParameter("FID");
	 long FID=Long.valueOf(fid);
	 String fk_node = request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node");
	 int FK_Node=Integer.valueOf(fk_node);
	 String FK_Flow = request.getParameter("FlowNo")==null?request.getParameter("FK_Flow"):request.getParameter("FlowNo");
	 String FK_Dept = request.getParameter("FK_Dept")==null?WebUser.getFK_Dept():request.getParameter("FK_Dept");
	 String info = request.getParameter("Info")==null?"":request.getParameter("Info");
%>
<script type="text/javascript">
<%-- function TBHelp(ctrl, enName, attrKey) {
    var explorer = window.navigator.userAgent;
    var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName + "&AttrKey=" + attrKey;
    var str = "";
/*     if (explorer.indexOf("Chrome") >= 0) {//谷歌的
        window.open(url, "sd", "left=200,height=500,top=150,width=400,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no");
    }
    else {//IE,火狐的
        str = window.showModalDialog(url, "sd", "dialogHeight:500px;dialogWidth:400px;dialogTop:150px;dialogLeft:200px;center:no;help:no");
        if (str == undefined) return;
        $("*[id$=" + ctrl + "]").focus().val(str);
    } */
    str = window.showModalDialog(url, "sd", "dialogHeight:500px;dialogWidth:400px;dialogTop:150px;dialogLeft:200px;center:no;help:no");
    if (str == undefined) return;
   // $("*[id$=" + ctrl + "]").focus().val(str);
    document.getElementById(ctrl).value = str;
} --%>


function TBHelp(ctrl, enName) {
    var explorer = window.navigator.userAgent;
    var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName + "&AttrKey=" + ctrl + "&WordsSort=2" + "&FK_MapData=" + enName + "&id=" + ctrl;
    var str = "";
    if (explorer.indexOf("Chrome") >= 0) {//谷歌的
        window.open(url, "sd", "left=200,height=500,top=150,width=600,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no");
    }
    else {//IE,火狐的
        str = window.showModalDialog(url, "sd", "dialogHeight:500px;dialogWidth:600px;dialogTop:150px;dialogLeft:200px;center:no;help:no");
        if (str == undefined) return;
        ctrl = ctrl.replace("Forward", "TB");
        $("*[id$=" + ctrl + "]").focus().val(str);
    }
}

function onSelect(){
	var FK_Dept = $.trim($("#DDL_Dept").val());
	
	var FK_Node = '<%=FK_Node%>';
	var WorkID = '<%=WorkID%>';
	var FID = '<%=FID%>';
	var FK_Flow = '<%=FK_Flow%>';

	var url = "<%=basePath%>WF/WorkOpt/Forward.jsp?FK_Node="+FK_Node+"&WorkID="+WorkID+"&FID="+FID+"&FK_Flow="+FK_Flow+"&FK_Dept="+FK_Dept;
	window.location.href = url;
}
function onCancel(){
	var FK_Node = '<%=FK_Node%>';
	var WorkID = '<%=WorkID%>';
	var FID = '<%=FID%>';
	var FK_Flow = '<%=FK_Flow%>';
	
	var url = "<%=basePath%>WF/MyFlow<%=Glo.getFromPageType()%>.jsp?FK_Node="+FK_Node+"&WorkID="+WorkID+"&FID="+FID+"&FK_Flow="+FK_Flow;
	window.location.href = url;
}
function onSubmit(){
	if(confirm("您确定要执行吗？")){
		var FK_Node = '<%=FK_Node%>';
		var WorkID = '<%=WorkID%>';
		var FID = '<%=FID%>';
		var FK_Flow = '<%=FK_Flow%>';
		var FK_Dept = '<%=FK_Dept%>';
		
		var size = $("input[name='RB_Button']:checked").size();
		if(size == 0){
			alert("请选择要移交的人员！");
			return false;
		}
		
		var info = $.trim($("#TB_Doc").val()).replace(new RegExp("'", "g"), "%27");
		if(info.length == 0){
			alert("您必须输入移交原因！");
			return false;
		}
		
		var toEmp =  $("input[name='RB_Button']:checked").val();
		var url = "<%=basePath%>WF/WorkOpt/Forward.do?FK_Node=" + FK_Node
					+ "&WorkID=" + WorkID + "&FID=" + FID + "&FK_Flow="
					+ FK_Flow + "&FK_Dept=" + FK_Dept + "&ToEmp=" + toEmp
					+ "&Info=" + info;
			$("#form1").attr("action", url);
			$("#form1").submit();
		}
	}
</script>
</head>
<body>
	<table border=1px align=center width='100%'>
		<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></div></Caption>

		<div class="am-g">
			<!-- <div class="divCenter2"> -->
				<form method="post" action="" class="am-form" id="form1">
					<!-- <table class="am-table am-table-striped am-table-hover table-main"> -->
						<thead>
							<tr>
								<td valign="top" style="text-align: left">
									<div>
										<select
											style="height:35px;font-size: 13px"
											name="DDL_Dept" id="DDL_Dept" onchange="onSelect();">
											<%
												Depts depts = new Depts();
																																																																										            	depts.RetrieveAllFromDBSource();
																																																																										            	for(Dept dept : Depts.convertDepts(depts)){
																																																																										            		String sel = "";
																																																																										            		if(FK_Dept.equals(dept.getNo())){
																																																																										            			sel="selected=\"selected\"";
																																																																										            		}
											%>
											<option value="<%=dept.getNo()%>" <%=sel%>><%=dept.getName()%></option>
											<%
												}
											%>
										</select><span id="ds">&nbsp;请选择移交人，输入移交原因，点移交按钮执行工作移交。</span>
									</div>
								</td>
							</tr>
							<tr>
								<td bgcolor="#FFFFFF" style="text-align: left" valign="top">
									<table
										class="am-table am-table-striped am-table-hover table-main"
										cellpadding='2' cellspacing='2'>
										<%
											String sql = "SELECT No,Name FROM Port_Emp WHERE FK_Dept='" + FK_Dept + "'";
																																														DataTable dt = DBAccess.RunSQLReturnTable(sql);
																																														int colIdx = -1;
																																														for(DataRow dr : dt.Rows){
																																														String no = dr.getValue("No").toString();
																																														String name = dr.getValue("Name").toString();
																																														if(WebUser.getNo().equals(no))continue;
																																																																		            	 colIdx++;
																																																																		                 if (colIdx == 0){
										%>
										<tr>
											<%
												}
											%>
											<td><input id="RB_<%=no%>" type="radio" name="RB_Button"
												value="RB_<%=no%>" /> <label for="RB_<%=no%>"><%=no%>
													<%=name%></label></td>
											<%
												if (colIdx == 2){
																																																																									                    colIdx = -1;
											%>
										</tr>
										<%
											} 
																																																																		             }
										%>
									</table> <script type="text/javascript">
													
												<%// 已经非配或者自动分配的任务。
							            	GenerWorkerLists wls = new GenerWorkerLists();
							            	wls.Retrieve(GenerWorkerListAttr.WorkID, WorkID, GenerWorkerListAttr.IsEnable, 1, GenerWorkerListAttr.IsPass, 0);
							            	
							            	int nodeID = 0;
							            	for(GenerWorkerList wl : GenerWorkerLists.convertGenerWorkerLists(wls)){
							            		String str = "var cb = $('#RB_"+wl.getFK_Emp()+"');"
							            		           + "if(cb.length>0)cb.attr('checked','checked');";
							            		out.println(str);
							            		nodeID = wl.getFK_Node();
							            	}%>
													
												</script>

									<div style='float: left; display: block; width: 100%'>
										<a href="javascript:TBHelp('Forward_Doc','ND<%=fk_node%>')"> <img
											src='<%=basePath%>WF/Img/Emps.gif' align='middle' border=0 />选择词汇
										</a>&nbsp;&nbsp;
									</div> <textarea id="TB_Doc" name="TB_Doc" rows="10" cols="70"></textarea>
									<script type="text/javascript">
												<%if(nodeID>0){Node nd = new Node(nodeID);
								            	if(!"".equals(nd.getFocusField())){
								            		String str = "$('#TB_Doc').val("+info+");";
								            		out.println(str);
								            	}}%>
													
												</script>
								</td>
							</tr>
							<tr>
								<td valign="top" style="text-align: left"><input
									type="button" id="Btn_Forward" name="Btn_Forward" value="移交"
									onclick="onSubmit();" class="am-btn am-btn-primary am-btn-xs" />
									<input type="button" id="Btn_Cancel" name="Btn_Cancel"
									value="取消" onclick="onCancel();"
									class="am-btn am-btn-primary am-btn-xs" /></td>
							</tr>
						</thead>
					<!-- </table> -->

				</form>
			<!-- </div> -->
		</div>
	</table>

</body>
</html>