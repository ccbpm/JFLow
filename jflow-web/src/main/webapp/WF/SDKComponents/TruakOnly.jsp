<%@page import="org.apache.geronimo.mail.util.Base64"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<%
    String enName = "ND" + request.getParameter("FK_Node");
    String srcTrack = pageContext.getServletContext().getContextPath()+"/WF/WorkOpt/OneWork/Track.jsp?FK_Flow="+request.getParameter("FK_Flow");
    srcTrack += "&FK_Node=" + request.getParameter("FK_Node");
    srcTrack += "&WorkID=" + request.getParameter("WorkID");
 %>
<iframe id='F' src='<%=srcTrack%>'
 frameborder="0" style=' padding: 0px; border: 0px; margin:0px; height:700px;' width='100%'>
        </iframe>
</body>
</html>