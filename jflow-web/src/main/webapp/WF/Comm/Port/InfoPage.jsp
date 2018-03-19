<%@page import="cn.jflow.system.ui.uc.MyFlow"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.Web.*"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
</head>
<body>
	<%
		response.addHeader("P3P", "CP=CAO PSA OUR");
		// 获取提示消息
		Object msg = session.getAttribute("info");
	    if (msg == null)
	        msg = application.getAttribute("info"+WebUser.getNo());
	    if (msg == null){
	        msg = "@提示信息丢失。"; // "@没有找到信息，请在在途工作中找到它。";
	    }
	    
		MyFlow flow = new MyFlow(request, response);
		flow.AddMsgOfInfo("提示信息", msg.toString());
	
		// 清空session
		session.setAttribute("info", null);
	%>
	<%=flow.get_content()%>
</body>
</html>
