<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String ShowType = request.getParameter("ShowType");
	if (ShowType == null) {
		ShowType = "FrmLib";
	}
	String FK_MapData = request.getParameter("FK_MapData");
	String FK_Flow = request.getParameter("FK_Flow");
	Integer FK_Node = Integer.valueOf(request.getParameter("FK_Node"));
	FlowFrmsModel flowFrms = new FlowFrmsModel(request, response,FK_Flow,FK_MapData,ShowType,FK_Node);
	flowFrms.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=flowFrms.Title %> </title>

<script type="text/javascript">
	function New() {
		window.location.href = window.location.href;
	}

	function WinField(fk_mapdata, nodeid, fk_flow) {
		var url = "../MapDef/Sln.jsp?FK_MapData=" + fk_mapdata + "&FK_Node="
				+ nodeid + '&FK_Flow=' + fk_flow;
		WinOpen(url);
	}

	function WinFJ(fk_mapdata, nodeid, fk_flow) {
		var url = "../MapDef/Sln.jsp?FK_MapData=" + fk_mapdata + "&FK_Node="
				+ nodeid + '&FK_Flow=' + fk_flow + '&DoType=FJ';
		WinOpen(url);
	}
	
	  function WinDtl(fk_mapdata, nodeid, fk_flow) {
        var url = "../MapDef/Sln.jsp?FK_MapData=" + fk_mapdata + "&FK_Node=" + nodeid + '&FK_Flow=' + fk_flow + '&DoType=Dtl';
        WinOpen(url);
    }

	function AddIt(fk_mapdata, fk_node, fk_flow) {
		var url = 'FlowFrms.jsp?DoType=Add&FK_MapData=' + fk_mapdata
				+ '&FK_Node=' + fk_node + '&FK_Flow=' + fk_flow;
		window.location.href = url;
	}
	function DelIt(fk_mapdata, fk_node, fk_flow) {
		if (window.confirm('您确定要移除吗？') == false)
			return;
		var url = 'FlowFrms.jsp?DoType=Del&FK_MapData=' + fk_mapdata
				+ '&FK_Node=' + fk_node + '&FK_Flow=' + fk_flow;
		window.location.href = url;
	}
	function btn_SavePowerOrders_Click(){
		var url = "<%=basePath%>WF/Admin/btn_SavePowerOrders_Click.do?FK_Node=<%=FK_Node%>&FK_Flow=<%=FK_Flow%>&FK_MapData=<%=FK_MapData%>";
		$("#form1").attr("action",url);
		$("#form1").submit();
	}
	function btn_SaveFrmSort_Click(){
		var url = "<%=basePath%>WF/Admin/btn_SaveFrmSort_Click.do?FK_Node=<%=FK_Node%>&FK_Flow=<%=FK_Flow%>&FK_MapData=<%=FK_MapData%>";
		$("#form1").attr("action",url);
		$("#form1").submit();
	}
	function btn_SaveFlowFrms_Click(){
		var chk_value =""; 
		$('input[type="checkbox"]:checked').each(function(){
			chk_value+=($(this).attr('id')+";");
		}); 
		var url = "<%=basePath%>WF/Admin/btn_SaveFlowFrms_Click.do?FK_Node=<%=FK_Node%>&FK_Flow=<%=FK_Flow%>&FK_MapData=<%=FK_MapData%>&chk_value="+chk_value;
		$("#form1").attr("action",url);
		$("#form1").submit();
	}
	function btn_SaveFrm_Click(btnName){
		if(btnName=="Btn_Delete"){
			if(confirm('您确认删除吗？')){
				var url = "<%=basePath%>WF/Admin/btn_SaveFrm_Click.do?btnName="+btnName+"&FK_Node=<%=FK_Node%>&FK_Flow=<%=FK_Flow%>&FK_MapData=<%=FK_MapData%>";
				$("#form1").attr("action",url);
				$("#form1").submit();
			}else{
			 Window.close();
			}
		}else{
		var url = "<%=basePath%>WF/Admin/btn_SaveFrm_Click.do?btnName="+btnName+"&FK_Node=<%=FK_Node%>&FK_Flow=<%=FK_Flow%>&FK_MapData=<%=FK_MapData%>";
		$("#form1").attr("action", url);
			$("#form1").submit();
		}
	}
	
// 	function btn_do_click(){
// 		$("#BodyHtml").val($("#form_id").html());
// 		document.getElementById('form_id').submit();
// 	}
</script>
</head>
<body>
	<form method="post"
		action="FlowFrms.jsp?ShowType=<%=ShowType%>&amp;FK_Flow=<%=FK_Flow%>&amp;FK_Node=<%=FK_Node%>&amp;Lang=CH"
		id="form1">
		<div>
		<input type="hidden" name="FormHtml" id="FormHtml">
			<table width='100%' height='100%' align=center border="0px">
				<tr>
					<td align=left valign=top width='20%'  border="0px">
					<%=flowFrms.Left.toString()%>
					</td>
					<td align="Left" valign=top width='80%'  border="0px">
					<%=flowFrms.Pub1.toString()%>
					</td>
				</tr>
			</table>
		</div>
	</form>
<%--  <form id="form_id" method="post" action="<%=basePath %>WF/Comm/doRefClick.do"> --%>
<!--     <input type="hidden" name="BodyHtml" id="BodyHtml"> -->
<!-- 	<table width='100%' height='100%' align=center border="0px"> -->
<!-- 		<tr> -->
<!-- 			<td align=left valign=top width='20%' border="0px"> -->
<%-- 			<%=flowFrms.Left.toString() %> </td> --%>
<!-- 			<td align="Left" valign=top width='80%' border="0px"> -->
<%-- 			<%=flowFrms.Left.toString() %></td> --%>
<!-- 		</tr> -->
<!-- 	</table> -->
<!-- 	</form> -->
</body>
</html>