<%@page import="cn.jflow.common.util.ContextHolderUtils"%>
<%
	response.sendRedirect(request.getContextPath()
			+"/WF/Admin/XAP/Designer.jsp"
			+"?UserNo="+BP.Web.WebUser.getNo()
			+"&SID="+BP.Web.WebUser.getSID());
%>
