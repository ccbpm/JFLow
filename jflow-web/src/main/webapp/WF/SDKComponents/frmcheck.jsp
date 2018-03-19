<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="BP.WF.NodeFormType"%>
<%@page import="BP.WF.Template.FrmWorkCheck"%>
<%@page import="BP.WF.Entity.*"%>
<%@page import="BP.WF.Data.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.Tools.*"%>
<%@page import="cn.jflow.common.model.MyFlowModel"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<%
    String enName = "ND" + request.getParameter("FK_Node");
    String src = pageContext.getServletContext().getContextPath()+"/WF/WorkOpt/WorkCheck.jsp?FID=" +request.getParameter("FID");
    src += "&WorkID=" + request.getParameter("WorkID");
    src += "&FK_Node=" + request.getParameter("FK_Node");
    src += "&FK_Flow=" + request.getParameter("FK_Flow");
    src += "&IsHidden==" + request.getParameter("IsHidden");
    String srcTrack = pageContext.getServletContext().getContextPath()+"/WF/WorkOpt/TrackChart.jsp?FK_Flow="+request.getParameter("FK_Flow");
    srcTrack += "&FK_Node=" + request.getParameter("FK_Node");
    srcTrack += "&WorkID=" + request.getParameter("WorkID");
    BP.WF.Template.FrmWorkCheck wc = new BP.WF.Template.FrmWorkCheck(Integer.parseInt((request.getParameter("FK_Node"))));
    
    int nodeID = Integer.parseInt(request.getParameter("FK_Node"));
    BP.WF.Template.FrmWorkCheck frmWorkCheck = new BP.WF.Template.FrmWorkCheck(nodeID);
    if (wc.getHisFrmWorkCheckSta() == BP.WF.Template.FrmWorkCheckSta.Disable == false)
    {
%>

<% } %>


<div id="tt" class="easyui-tabs"  style="width: <%=frmWorkCheck.getFWC_Wstr()%>; height:<%= frmWorkCheck.getFWC_Hstr() %>;">
    <div title="审核信息" id='CheckInfo'   >
        <iframe id='F' src='<%=src%>' frameborder="0" style=' padding: 0px; border: 0px; margin:0px; height:99%'
            leftmargin='0' topmargin='0' width='100%'   >
        </iframe>

    </div>
</div>
</body>
</html>