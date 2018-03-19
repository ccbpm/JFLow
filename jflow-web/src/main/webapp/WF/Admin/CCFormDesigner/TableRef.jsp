<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import = "cn.jflow.model.wf.admin.FoolFormDesigner.TableRef" %>
<%@ page import = "BP.Sys.*" %>
<%@ page import = "BP.Sys.DBSrcType" %>

<%@ include file="/WF/head/head1.jsp"%>
<html>
	<head>
	<%
		TableRef tr = new TableRef(request, response);
	%>	

	</head>
<body>
   <table width=100%>
		<tr>
		<th>序</th>
		<th>引用表单</th>
		<th>字段英文</th>
		<th>字段中文</th>
		</tr>
		<%
		    String RefNo = request.getParameter("RefNo");
		    MapAttrs mapAttrs = new MapAttrs();
		    mapAttrs.RetrieveByAttr(MapAttrAttr.UIBindKey, RefNo);
		    int idx = 0;
		    for(MapAttr attr:mapAttrs.ToJavaList())
		    {
		        idx++;
		     %>
		<tr>
		<td><%=idx%></td>
		<td><%=attr.getFK_MapData()%></td>
		<td><%=attr.getKeyOfEn()%></td>
		<td><%=attr.getName()%></td>
		</tr>
		<%
		    } %>
		
		</table>
</body>
</html>

