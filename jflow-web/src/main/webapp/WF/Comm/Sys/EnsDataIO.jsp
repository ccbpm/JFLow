<%@page import="BP.WF.Glo"%>
<%@page import="cn.jflow.model.wf.comm.sys.Sys_EnsDataIOModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WF/head/head1.jsp"%>
<%  Sys_EnsDataIOModel ensDataIOModel = new Sys_EnsDataIOModel(request, response);
	ensDataIOModel.pageLoad();
%>
<!DOCTYPE html>
<html>
<head>
<base target=_self  />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据导入导出</title>
<link href="../Style/Table0.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
function Btn_Step(step){
	document.getElementById('form').action="Btn_Up.do?EnsName=<%=ensDataIOModel.getEnsName()%>&Step="+step;
	document.getElementById('form').submit();
}

function Btn_DataIO_Click(){
	if(!confirm('您确定要执行吗？ 如果执行现有的数据将会被清空，Excel中的数据导入进去。')){
		return;
	}
	document.getElementById('form').action="Btn_DataIO_Click.do?EnsName=<%=ensDataIOModel.getEnsName()%>";
	document.getElementById('form').submit();
}

function Btn_UpdateIO_Click(){
	if(!confirm('您确定要执行吗？ 如果执行现有的数据将会按照主键更新。')){
		return;
	}
	document.getElementById('form').action="Btn_UpdateIO_Click.do?EnsName=<%=ensDataIOModel.getEnsName()%>";
	document.getElementById('form').submit();
}
</script>
</head>
<body topmargin="0" leftmargin="0">

<form enctype="multipart/form-data" id="form" method="post">
<table style="width:100%;">
<caption><a href="EnsAppCfg.jsp?EnsName=<%=ensDataIOModel.getEnsName() %>&DoType=Adv">基本配置</a>-
<a href="EnsAppCfg.jsp?EnsName=<%=ensDataIOModel.getEnsName() %>&DoType=SelectCols">选择列</a>
  - 数据导入导出</caption>
	<tr>
		<td>
			<%=ensDataIOModel.getPub()%>
		<td>
	</tr>
</table>
</form>
</body>
</html>