<%@page import="cn.jflow.model.wf.mapdef.ActionModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	ActionModel am=new ActionModel(request,response);
	am.page_Load();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=am.title %></title>
	<link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>WF/Comm/Style/Table.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
    
    <script type="text/javascript">
        function DoDel(fk_mapdata, xmlEvent) {
            if (window.confirm('您确认要删除吗?') == false)
                return;
            window.location.href = '<%=basePath%>WF/Admin/FoolFormDesigner/Action.jsp?FK_MapData=' + fk_mapdata + '&DoType=Del&RefXml=' + xmlEvent;
        }
        function btn_Click(){
        	var url="<%=basePath %>WF/MapDef/ActionBtn_Click.do?FK_MapData=<%=am.getFK_MapData()%>&MyPK=<%=am.getMyPK()%>&Event=<%=am.getEvent()%>";
        	$("#form1").attr('action', url);
    		$("#form1").submit();
        }
    </script>
</head>
<body>
	<form id="form1" method="post">
		<table width='80%' align=center>
			<%=am.pub3 %>
			<tr>
				<td valign=top><%=am.pub1 %></td>
				<td valign=top><%=am.pub2 %></td>
			 </tr>
		</table>
	</form>
</body>
</html>