<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.jflow.model.wf.comm.sys.EnumListModel" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	EnumListModel  elm=new EnumListModel(request, response);
	elm.Page_Load(request, response);
	String info=request.getParameter("info");
	String RefNo=request.getParameter("RefNo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>属性设置</title>
<link href="../Style/Table0.css" rel="stylesheet" type="text/css" />
<link href="../../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="../../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="../../Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
<script src="../../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#form1").resize();
	var info ='<%=info%>';
	if (info == "" || info == null || info == "null") {
		return;
	}else {
		alert(info);
	}
	
});


//编辑
function btn_Click(){
	$("#form1").attr("action", '<%=basePath%>WF/comm/enumList/btn_Click.do?RefNo=<%=RefNo%>'); 
	$("#form1").submit();
}

//新建
function btn_New_Click(){
	$("#form1").attr("action", '<%=basePath%>WF/comm/enumList/btn_New_Click.do?DoType=New'); 
	$("#form1").submit();
}


</script>
</head>
<body class="easyui-layout">
 <form method="post" action="./EnumList.jsp?t=0.1868460534606129" id="form1">
 <div data-options="region:'center'">
 <%=elm.getUcSys1().toString()%>
 </div>
</form>
</body>
</html>