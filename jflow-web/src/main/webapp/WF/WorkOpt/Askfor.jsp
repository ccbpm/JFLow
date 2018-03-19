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
%>
<script type="text/javascript">
function ShowIt() {
	var hidd_value = $("#CHID_SelectedEmps").val();
    var url = 'SelectUser.jsp?OID=123&CtrlVal=' + hidd_value;
    var v = window.showModalDialog(url, 'dfg', 'dialogHeight: 450px; dialogWidth: 650px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
    if (v == null || v == '' || v == 'NaN') {
        return false;
    }
    
    var arr = v.split('~');
    var emparr = arr[0].split(',');

    if (emparr.length > 1) {
        alert('输入的加签人（' + arr[1] + '）不正确，加签人只能选择一个，请重新选择！');
        return false;
    }
    $("#TB_Worker").val(arr[1]);
    $("#CHID_SelectedEmps").val(arr[0]);
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
	
	//var askFor = $("#TB_Worker").val();
	var note = $.trim($("#TB_Note").val()).replace(new RegExp("'", "g"), "%27");
	
	var empNo = '<%=WebUser.getNo()%>';
	var askFor = $("#CHID_SelectedEmps").val();
	if(askFor == empNo){
		alert("不能让自己加签！");
		return false;
	}
	
	if(!checkEmp()){
		alert("输入的加签人（" + askFor + "）不正确。加签人只能选择一个！");
		return false;
	}
	
	var size = $("input[name='RB_Button']:checked").size();
	if(size == 0){
		alert("请选择处理方式！");
		return false;
	}
	
	var toEmp =  $("input[name='RB_Button']:checked").val(); 
	var url = "<%=basePath%>WF/WorkOpt/Askfor.do?FK_Node="+FK_Node+"&WorkID="+WorkID+"&FID="+FID+"&FK_Flow="+FK_Flow+"&AskFor="+askFor+"&ToEmp="+toEmp+"&Info="+note;
	$("#form1").attr("action", url);
	$("#form1").submit();
	
}
function checkEmp(){
	var vali = false;
	var askFor = $("#CHID_SelectedEmps").val();
	$.ajax({
		url:'CheckAskfor.do',
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		data : {
			AskFor : askFor
		},
		async: false ,
		error: function(data){},
		success: function(data){
			json = eval(data);
			if(json.success){
				vali = true;
			}else{
				vali = false;
			}
		}
	});
	return vali;
}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<table border=1px align=center width='100%'>
		<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></div></Caption>
		
			<!-- <div class="divCenter2"> -->
				<form method="post" action="" class="am-form" id="form1">
					<!-- <table class="am-table am-table-striped am-table-hover table-main"> -->
						<thead>
							<tr>
								<th class="table-check" colspan="3" nowrap="nowrap">工作加签</th>
							</tr>
							<tr>
								<td nowrap="nowrap">加签人:</TD>
							   	<td valign="top" nowrap="nowrap">
							   		<input name="TB_Worker" type="text" style="width: 425px;height:25px;padding: 0" id="TB_Worker" readonly="readonly"/>
							   		<input type="hidden" name="HID_SelectedEmps" id="CHID_SelectedEmps" value="" />
							   	</td>
							   	<td ><input type="button" name="Worker" id="Worker" value="选择加签人" onclick="ShowIt();" class="am-btn am-btn-primary am-btn-xs"/></TD>
				 		    </tr>
				 		    <tr>
				 		       <td colspan="3" nowrap="nowrap">
				 		    	加签原因说明：<br>
									<textarea name="TB_Note" rows="5" cols="70" id="TB_Note">您好： 现把工作向您请示. <%=WebUser.getName() %></textarea>
							   </td>
				 		    </tr>
				 		    <tr>
				 		    	<td nowrap="nowrap" >处理方式</td>
				 		    	<td colspan="2" nowrap="nowrap"> 
				 		    	  <input id="RB_0" type="radio" name="RB_Button" value="RB_0" checked="checked" /><label for="RB_0">对方加签后,直接发送到下一步骤.</label>
				 		    	  <input id="RB_1" type="radio" name="RB_Button" value="RB_1" /><label for="RB_1">对方加签后在转发给我,由我发送到下一步骤.</label>
				 		    	</td>
				 		    </tr>
				 		    <tr>
								<!-- <td nowrap="nowrap" >&nbsp;</td> -->
								<td colspan="3" nowrap="nowrap" >
									<input type="button" name="Btn_Submit" value="提交" id="Btn_Submit" onclick="onSubmit();" class="am-btn am-btn-primary am-btn-xs"/>
									<input type="button" name="Btn_Cancel" value="取消" id="Btn_Cancel" onclick="onCancel();" class="am-btn am-btn-primary am-btn-xs"/>
								</td>
							</tr>
						</thead>
					<!-- </table> -->
					<%
		 				String sql = "SELECT  * FROM ND"+Integer.parseInt(FK_Flow)+"Track WHERE ActionType=24 AND WorkID="+WorkID+" AND (EmpFrom='"+WebUser.getNo()+"' OR EmpTo='"+WebUser.getNo()+"')";
		 		    	DataTable dt = DBAccess.RunSQLReturnTable(sql);
		 		    	if (dt.Rows.size() != 0){
	 				%>
	 				<fieldset>
	 				  <%
	 				 	for (DataRow dr : dt.Rows){
	 				  %>
	 				    <br>节点：<%=dr.getValue(TrackAttr.NDFromT) %><hr>
	 				                信息：<%=DataType.ParseText2Html(dr.getValue(TrackAttr.Msg).toString()) %><br>
	 				  <%} %>
	 			    </fieldset>
	 				<%}%>
				</form>
			<!-- </div> -->
		</table>
</body>
</html>