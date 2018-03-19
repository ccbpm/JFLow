<%@page import="BP.WF.Glo"%>
<%@page import="BP.WF.DoWhatList" %>
<%@page import="cn.jflow.common.model.PortModel"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	PortModel portModel= new PortModel(request, response);
	portModel.loadPage();
// 	String basePath = Glo.getCCFlowAppPath();
// 	// 判断模式
// 	String doWhat = portModel.getDoWhat();
// 	if(DoWhatList.JiSu.equals(doWhat)){
// 		// 极速模式
// 		out.print("<script>location='App/Simple/Default.jsp'</script>");
// 	}else if(DoWhatList.Start5.equals(doWhat)){
// 		// 经典模式
// 		out.print("<script>location='App/Amaz/Default.jsp'</script>");
// 	}
%>
