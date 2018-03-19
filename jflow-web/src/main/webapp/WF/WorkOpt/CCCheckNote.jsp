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
function onSubmit(){
	var FK_Node = '<%=FK_Node%>';
	var WorkID = '<%=WorkID%>';
	var FID = '<%=FID%>';
	var FK_Flow = '<%=FK_Flow%>';
	
	var note = $.trim($("#TextBox").val()).replace(new RegExp("'", "g"), "%27");
	$.ajax({
		url:'CheckNote.do',
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		data : {
			FK_Flow : FK_Flow,
			FK_Node : FK_Node,
			WorkID : WorkID,
			FID : FID,
			Info : note
		},
		async: false ,
		error: function(data){},
		success: function(data){
			json = eval(data);
			alert(json.msg);
		}
	});
	window.close();
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
						<thead>
							<tr>
								<th class="table-check" nowrap="nowrap">
								<%
									FrmWorkCheck en = new FrmWorkCheck(FK_Node);
								%>
								<%=en.getFWCOpLabel() %>
								</th>
							</tr>
							 <tr>
				 		       <td valign="top" style="text-align:left">
				 		       		<%
								    	String note = Dev2Interface.GetCheckInfo(FK_Flow, WorkID, FK_Node);
								    	if(StringHelper.isNullOrEmpty(note)){
								    		Dev2Interface.WriteTrackWorkCheck(FK_Flow, FK_Node, WorkID, FID, "已阅", "阅知");
								    	}
								    %>
									<textarea name="TextBox" rows="2" cols="20" id="TextBox" style="height:94px;width:100%;"><%=note %></textarea>
							   </td>
				 		    </tr>
				 		    <tr>
								<td align="right">
								 	<input type="button" name="Btn_OK" value="确定" id="Btn_OK" onclick="onSubmit();" class="am-btn am-btn-primary am-btn-xs"/>
								    <input type="button" name="Btn_Cancel" value="取消" onclick="javascript:window.close();" id="Btn_Cancel" class="am-btn am-btn-primary am-btn-xs"/>
								</td>
							</tr>	
						</thead>
					<!-- </table> -->
				</form>
			</div>
		</div>
	</table>
</body>
</html>