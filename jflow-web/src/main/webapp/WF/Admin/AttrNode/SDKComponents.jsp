<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="BP.WF.RunModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import= "java.util.ArrayList"%>
<%@page import= "java.util.List"%>
<%@page import="cn.jflow.controller.wf.workopt.BaseController"%>
<%@page import="BP.WF.Template.*"%>
<%@page import="BP.WF.NodeFormType"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.DA.*"%>  
<%@include file  = "/WF/head/head1.jsp" %>
<body>
<table style="width:100%;height:100%;">
<caption> SDK 表单组件属性</caption>
<tr>
<td style=" width:20%;"  valign=top nowarp=true >

<% 
    String ctrlType = request.getParameter("CtrlType");
    String nodeID=request.getParameter("FK_Node");
    int fk_node = Integer.parseInt(nodeID);

    String src = "../../Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Entity.FrmWorkCheck&PK="+nodeID;
    if (ctrlType == "DocMultiAth")
        src = "";

    Node nd = new Node(fk_node);
     %>
     <ul>
        <li> <a href="../../Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Entity.FrmWorkCheck&PK=<%=nodeID %>" target="Doc" >审核组件</a> </li>
        <li> <a href="../../MapDef/Attachment.jsp?FK_MapData=ND<%=nodeID %>&Ath=DocMultiAth&FK_Node=<%=nodeID %>" target="Doc">多附件</a> </li>
        <li> <a href="../../MapDef/Attachment.jsp?FK_MapData=ND<%=nodeID %>&Ath=DocMainAth&FK_Node=<%=nodeID %>" target="Doc">单附件</a> </li>
        <li> <a href="SubFlows.jsp?FK_Node=<%=nodeID %>" target="Doc">父子流程</a> </li>

        <li> <a href="../../Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Template.BtnLab&PK=<%=nodeID %>" target="Doc">工具栏</a> </li>
        
        <li> <a href="../../Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Entity.FrmWorkCheck&PK=<%=nodeID %>" target="Doc">轨迹</a> </li>

        <%--<li> <a href="../../Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Entity.FrmWorkCheck&PK=<%=nodeID %>" target="Doc">接受人</a> </li>--%>

        <% if (nd.getHisNodeWorkType() == BP.WF.NodeWorkType.WorkHL || nd.getHisNodeWorkType() == BP.WF.NodeWorkType.WorkFHL)
           { %>
          <li> <a href="../../Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Entity.FrmWorkCheck&PK=<%=nodeID %>" target="Doc">子线程</a> </li>
         <%} %>
     </ul>
 </td>




<td style="width:100%;height:500px" >
 <iframe src="<%=src %>" style="width:100%;height:100%;" name="Doc" > 
 </iframe> 
 </td>

</tr>
</table>
</body>
</html>