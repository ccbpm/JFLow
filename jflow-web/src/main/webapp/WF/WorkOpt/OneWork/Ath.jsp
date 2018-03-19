<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String FK_Flow=request.getParameter("FK_Flow");
	String OID=request.getParameter("WorkID").toString();
	int oid=0;
	String FID=request.getParameter("FID");
	int fid=0;
	String FK_Node=request.getParameter("FK_Node").toString();
	int fk_node=0;
	if(OID!=null && OID.length()>0){
		oid=Integer.parseInt(OID.toString());
	}
	if(FID!=null && FID.length()>0){
		fid=Integer.parseInt(FID);
	}
	if(FK_Node!=null && FK_Node.length()>0){
		fk_node=Integer.parseInt(FK_Node);
	}
	AthModel am=new AthModel(request,response,basePath,FK_Flow,oid,fid,fk_node);
	am.init();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程附件</title>
</head>
<body>
 <div id="aa" data-options="region:'center'" style="padding: 5px">
 	<%=am.Pub1.toString() %>
 </div>
</body> 
</html>
