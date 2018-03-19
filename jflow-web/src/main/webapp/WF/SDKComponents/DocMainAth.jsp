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
    String src = pageContext.getServletContext().getContextPath()+"/WF/CCForm/docmainath.jsp?FID=" + request.getParameter("FID");
    src += "&WorkID=" + request.getParameter("WorkID");
    src += "&FK_Node=" +request.getParameter("FK_Node");
    src += "&FK_Flow=" +request.getParameter("FK_Flow");
    src += "&FK_FrmAttachment=ND" +request.getParameter("FK_Node") + "_DocMultiAth";
    src += "&RefPKVal="+request.getParameter("WorkID");
    src += "&PKVal=" + request.getParameter("WorkID");
    src += "&Ath=DocMainAth" ;
    src += "&FK_MapData=" + enName;
    src += "&Paras=" + request.getParameter("Paras");
%>
<iframe id='Fa' src='<%=src%>' frameborder="0" leftmargin='0' topmargin='0' height="50" scrolling="no"
   style="align:left;width:100%; margin-left:1px;" scrolling="auto"></iframe>
</body>
</html>