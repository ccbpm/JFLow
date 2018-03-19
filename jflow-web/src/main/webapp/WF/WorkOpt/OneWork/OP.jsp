<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String DoType=request.getParameter("DoType");
	String FK_Node=request.getParameter("FK_Node");
	String FK_Flow=request.getParameter("FK_Flow");
	String WorkID=request.getParameter("WorkID");
	int workID=0;
	String FID=request.getParameter("FID");
	int fid=0;
	if(WorkID!=null && WorkID.length()>0){
		workID=Integer.parseInt(WorkID);
	}
	if(FID!=null && FID.length()>0){
		fid=Integer.parseInt(FID);
	}
	OpModel op=new OpModel(request,response,basePath,DoType,FK_Node,FK_Flow,workID,fid);
	op.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>操作</title>

<script type="text/javascript">
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
	
    //删除流程.
    function DeleteFlowInstance(flowNo, workid) {
        var url = '../DeleteFlowInstance.htm?FK_Flow=' + flowNo + '&WorkID=' + workid;
        WinOpen(url);
    }
	
	
	function DoFunc(doType, workid, fk_flow, fk_node) {

		if (doType == 'Del' || doType == 'Reset') {
			if (confirm('您确定要执行吗？') == false)
				return;
		}

		var url = '';
		if (doType == 'HungUp' || doType == 'UnHungUp') {
			url = '<%=basePath%>WF/WorkOpt/HungUp.jsp?WorkID=' + workid + '&FK_Flow=' + fk_flow
					+ '&FK_Node=' + fk_node;
			var str = window
					.showModalDialog(url, '',
							'dialogHeight: 350px; dialogWidth:500px;center: no; help: no');
			if (str == undefined)
				return;
			if (str == null)
				return;
			//this.close();
			window.location.href = window.location.href;
			return;
		}
		url = '<%=basePath%>WF/WorkOpt/OneWork/OP.jsp?DoType=' + doType + '&WorkID=' + workid + '&FK_Flow='
				+ fk_flow + '&FK_Node=' + fk_node;
		window.location.href = url;
	}
	function Takeback(workid, fk_flow, fk_node, toNode) {
		if (confirm('您确定要执行吗？') == false)
			return;
		var url = '<%=basePath%>WF/GetTask.jsp?DoType=Tackback&FK_Flow=' + fk_flow
				+ '&FK_Node=' + fk_node + '&ToNode=' + toNode + '&WorkID='
				+ workid;
		window.location.href = url;
	}
	function UnSend(fk_flow, workID, fid) {
		if (confirm('您确定要执行撤销吗?') == false)
			return;

		var url = "<%=basePath%>WF/WorkOpt/OneWork/OP.jsp?DoType=UnSend&FK_Node=&FK_Flow=" + fk_flow
				+ "&WorkID=" + workID;
		$.post(url, null, function(msg) {
			$('#winMsg').html(msg);
			$('#winMsg').window('open');
		});
	}
</script>

</head>
<body>
<div id="aa" data-options="region:'center'" style="padding: 5px">
	<%=op.Pub2.toString() %>
</div>
</body>
</html>
