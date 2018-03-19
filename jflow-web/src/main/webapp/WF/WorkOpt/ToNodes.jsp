<%@ page language="java" isELIgnored="false" import="java.util.*" pageEncoding="utf-8"%>
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
	 String FK_Flow = request.getParameter("FK_Flow")==null?"":request.getParameter("FK_Flow");
	 
	 String WorkIDs = request.getParameter("WorkIDs")==null?"":request.getParameter("WorkIDs");
	 String CFlowNo = request.getParameter("CFlowNo")==null?"":request.getParameter("CFlowNo");
	 String DoFunc = request.getParameter("DoFunc")==null?"":request.getParameter("DoFunc");
 	
%>
<script type="text/javascript">
function SetUnEable(ctrl) {
    SetEnable(true);
}
function RBSameSheet(ctrl) {
    if (ctrl.checked) {
        SetEnable(false);
    }
    else {
        SetEnable(true);
    }
}
function SetEnable(enable) {
    /*var arrObj = document.all;
    for (var i = 0; i < arrObj.length; i++) {
        if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
            arrObj[i].disabled = enable;
        }
    }*/
	$('input:checkbox').each(function(){
		$(this).attr("disabled", enable);
	});
}
function SetRBSameSheetCheck() {
    /*var arrObj = document.all;
    for (var i = 0; i < arrObj.length; i++) {
        if (typeof arrObj[i].type != "undefined" && arrObj[i].id.valueOf('RB_SameSheet') != -1) {
            arrObj[i].checked = true;
            break;
        }
    }*/
    var obj = $("#RB_SameSheet");
    if(obj.length > 0){
    	obj.attr("checked",true);
    }
}
function onCancel(){
	var FK_Node = '<%=FK_Node%>';
	var WorkID = '<%=WorkID%>';
	var FID = '<%=FID%>';
	var FK_Flow = '<%=FK_Flow%>';
	
	var url = "<%=basePath%>WF/MyFlow.htm?FK_Node="+FK_Node+"&WorkID="+WorkID+"&FID="+FID+"&FK_Flow="+FK_Flow;
	window.location.href = url;
}
function onSubmit(){
	var FK_Node = '<%=FK_Node%>';
	var WorkID = '<%=WorkID%>';
	var FID = '<%=FID%>';
	var FK_Flow = '<%=FK_Flow%>';
	
	var WorkIDs = '<%=WorkIDs%>';
    var CFlowNo = '<%=CFlowNo%>';
	var DoFunc = '<%=DoFunc%>';
	
	var toNodes = SubmitValue();
	if(toNodes.length <= 0){
		alert("发送出现错误，您没有选择到达的节点！");
		return false;
	}
	//alert(toNodes);
	
	var url = "<%=basePath%>WF/WorkOpt/ToNodes.do?FK_Node="+FK_Node+"&WorkID="+WorkID+"&FID="+FID+"&FK_Flow="+FK_Flow+"&WorkIDs="+WorkIDs+"&CFlowNo="+CFlowNo+"&DoFunc="+DoFunc+"&ToEmp="+toNodes;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<table border=1px align=center width='100%'>
		<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></div></Caption>
		
		<div class="am-g">
			<div class="am-u-sm-12">
				<form method="post" action="" class="am-form" id="form1">
					<!-- <table class="am-table am-table-striped am-table-hover table-main"> -->
						<tr>
							<!-- <td width="20%"></td> -->
							<td>
								<%
									//获得当前节点到达的节点.
									Nodes nds = Dev2Interface.WorkOpt_GetToNodes(FK_Flow, FK_Node, WorkID, FID);
								 	//检查是否有异表单。
					            	boolean isSubYBD = false; //异表单
					            	boolean isFirstRow = true;
					            	for(Node mynd : nds.ToJavaList()){
					            		if(mynd.getNodeID() == 0){
								%>
									<span class="BPRadioButton0">
										<input id="RB_SameSheet" type="radio" name="RB_Button" value="RB_SameSheet" checked="checked" onclick="RBSameSheet(this);" />
										<label for="RB_SameSheet"><b>可以分发启动的异表单节点</b></label>
									</span><br>
										<% 
											isSubYBD = true;continue;} 
											if(isSubYBD){
										%>
											&nbsp;&nbsp;&nbsp;&nbsp;
											<input id="CB_<%=mynd.getNodeID() %>" type="checkbox" value="<%=mynd.getNodeID()%>"  name="CB_<%=mynd.getNodeID() %>" />
											<label for="CB_<%=mynd.getNodeID() %>"><%=mynd.getName() %></label>
											<% 
											 	if(mynd.getHisDeliveryWay() == DeliveryWay.BySelected){
											%>
												- <a href="javascript:WinShowModalDialog_Accepter('AccepterOfGener.htm?FK_Flow=<%=FK_Flow %>&FK_Node=<%=FK_Node %>&ToNode=<%=mynd.getNodeID() %>&WorkID=<%=WorkID %>&FID=<%=FID %>&type=1')" >选择接受人</a>
										    <%} %>
										    	<br>
										<%continue;}else{ %>
											<span class="BPRadioButton0">
												<input id="RB_<%=mynd.getNodeID() %>" type="radio" value="<%=mynd.getNodeID()%>" <%if(isFirstRow){ %>checked="checked"<%} %> name="RB_Button" onclick="SetUnEable(this);" />
												<label for="RB_<%=mynd.getNodeID() %>"><%=mynd.getName() %></label>
											</span>
											<% 
											 	if(mynd.getHisDeliveryWay() == DeliveryWay.BySelected){
											%>
												- <a href="javascript:WinShowModalDialog_Accepter('AccepterOfGener.htm?FK_Flow=<%=FK_Flow %>&FK_Node=<%=FK_Node %>&ToNode=<%=mynd.getNodeID() %>&WorkID=<%=WorkID %>&FID=<%=FID %>&type=1')" >选择接受人</a>
										    <%} %>
										    	<br>
										<%} %>
								<%} %>
								<HR>
								<input type="button" name="To" value="  执 行  " id="To" onclick="onSubmit();" class="am-btn am-btn-primary am-btn-xs"/>&nbsp;
								<input type="button" name="Btn_Cancel" value="取消/返回" id="Btn_Cancel" onclick="onCancel();" class="am-btn am-btn-primary am-btn-xs"/>
							</td>
						</tr>
					<!-- </table> -->	
				</form>
			</div>
		</div>
	</table>
</body>
<script type="text/javascript">
function SubmitValue(){
	var toNodes = "";
	var size = $("input[name='RB_Button']:checked").size();
	if(size > 0){
		toNodes = $("input[name='RB_Button']:checked").val(); 
		if("RB_SameSheet" == toNodes)toNodes= "";
		if(toNodes.length <= 0){
			$('input:checkbox').each(function(){
				  if($(this).attr('checked')){
					  if(toNodes.length >0){
						  toNodes += "," + $(this).val();
	                  }else{
	                	  toNodes += $(this).val();
	                  }
				  }
			});
		}
	}
	return toNodes; 
}
</script>
</html>