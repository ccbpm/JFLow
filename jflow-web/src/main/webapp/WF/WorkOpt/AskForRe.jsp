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
	
	var note = $.trim($("#TB_Doc").val()).replace(new RegExp("'", "g"), "%27");
	var url = "<%=basePath%>WF/WorkOpt/AskforRe.do?FK_Node="+FK_Node+"&WorkID="+WorkID+"&FID="+FID+"&FK_Flow="+FK_Flow+"&Info="+note;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<table border=1px align="center" width='100%'>
		<Caption ><div class='' >您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></div></Caption>
		
		<div class="am-g">
			<div class="am-u-sm-12">
				<form method="post" action="" class="am-form" id="form1">
					<!-- <table class="am-table am-table-striped am-table-hover table-main"> -->
						<thead>
							<tr>
								<th class="table-check" nowrap="nowrap">填写回复意见</th>
							</tr>
							 <tr>
				 		       <td valign="top" style="text-align:left">
				 		       		<%
				 		       			//获得加签意见.
				 		            	GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
				 		       			String text = gwf.getParas_AskForReply();
				 		       		%>
				 		       		<textarea name="TB_Doc" rows="10" cols="50" id="TB_Doc" placeholder="填写回复意见"><%=text %></textarea><br>
				 		       		<input type="button" name="Btn_Submit" value="提交加签意见" id="Btn_Submit" onclick="onSubmit();" class="am-btn am-btn-primary am-btn-xs"/>
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