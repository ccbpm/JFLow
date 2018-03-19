<%@page import="BP.WF.Template.StartGuideWay"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.WF.Flow"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	
	 String errMsg = request.getParameter("errMsg")==null?"":request.getParameter("errMsg");
	 if(null != errMsg && "" != errMsg){
		 out.println("<script>alert('"+errMsg+"');</script>");
	 }
	
	String FK_Flow = request.getParameter("FK_Flow")==null?"":request.getParameter("FK_Flow");
	String SKey = request.getParameter("SKey")==null?"":request.getParameter("SKey");
	String DoType = request.getParameter("DoType")==null?"":request.getParameter("DoType");
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>简单导航</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/Table0.css" />

<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js" ></script>
<script type="text/javascript">
function NoSubmit(ev) {
    //if (window.event.srcElement.tagName == "TEXTAREA")return true;

    if (ev.keyCode == 13) {
        window.event.keyCode = 9;
        ev.keyCode = 9;
        return true;
    }
    return true;
}
function onSearch(){
	var key = $.trim($("#TB_Key").val());
	var FK_Flow = '<%=FK_Flow%>';
	
	var url = "<%=basePath%>/WF/StartGuide.jsp?FK_Flow="+FK_Flow+"&SKey="+ key;
	window.location.href = url;
}
function onSubmit(){
	var FK_Flow = '<%=FK_Flow%>';
	
	var cWorkID = "";
	$('input:checkbox').each(function(){
		 if($(this).attr('checked')){
			  if(cWorkID.length >0){
				  cWorkID += "," + $(this).val();
             }else{
            	 cWorkID += $(this).val();
             }
		  }
	});
	if(cWorkID.length <= 0){
		alert("您没有选择项目！");
		return false;
	}
	//alert(cWorkID);
	
	var url = "<%=basePath%>WF/StartGuide.do?FK_Flow="+FK_Flow+"&ToEmp="+cWorkID;
	$("#form1").attr("action", url);
	$("#form1").submit();
	
}
</script>
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
	<form method="post"	action="" id="form1">
		<div id="mainPanel" region="center" border="true" border="false" class="mainPanel">
            <% 
            	if("".equals(DoType)){
            		String sql = "";
            		String key = SKey;
            		Flow fl = new Flow(FK_Flow);
            		if("".equals(SKey) || null == SKey){
            			sql = fl.getStartGuidePara2();
            			sql = sql.replace("~", "'");
            		}else{
            			sql = fl.getStartGuidePara1();
            			sql = sql.replace("@Key", key);
            			sql = sql.replace("~", "'");
            		}
            		 sql = sql.replace("@WebUser.No", WebUser.getNo());
                     sql = sql.replace("@WebUser.Name", WebUser.getName());
                     sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
                     sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
                     
                     DataTable dt = DBAccess.RunSQLReturnTable(sql);
            %>
            	<b>请输入关键字:</b>
            	<input name="TB_Key" type="text" id="TB_Key" value="<%=SKey %>" />
            	<img name="imgbtn1" title="查询" id="imgbtn1" onclick="onSearch();" src="<%=basePath%>WF/Img/Searchss.gif">  
            	<img name="imgbtn1" title="启动流程" id="imgbtn2" onclick="onSubmit();" src="<%=basePath%>WF/Img/Start.gif">
            	<hr>
            	<%
            		StartGuideWay startGuidWay = fl.getStartGuideWay();
            	    //if(StartGuideWay.BySystemUrlOne == startGuidWay || StartGuideWay.BySystemUrlOneEntity == startGuidWay){
            		if(StartGuideWay.BySystemUrlOneEntity == startGuidWay){
            	%>
		            	<Table width='100%' >
		            		<TR>
		            			<TH>IDX</TH>
		            			<%
		            				for(DataColumn dc : dt.Columns){
			            				String name = dc.ColumnName.toLowerCase();
	           							if (!"pflowno".equals(name) && !"pworkID".equals(name)
           										&& !"no".equals(name) && !"name".equals(name)) {
		            			%>
		            						<TH><%=dc.ColumnName %></TH>
		            					<%} %>
		            			<%} %>
		            		</TR>
		            	    <%
		            	    	// 输出数据.
		                    	int idx1 = 0; 
		            	    	String url1 = basePath+"WF/MyFlow.htm?FK_Flow="+FK_Flow+"&FK_Node="+Integer.valueOf(FK_Flow)+"01&WorkID=0&IsCheckGuide=1";
		            	    	for(DataRow dr : dt.Rows){
		            	    		idx1++;
		            	    %>
		            	    	<TR>
		            	    		<TD class="Idx" nowrap="nowrap"><%=idx1 %></TD>
		            	    		<%
		            	    		 	String paras = url1 + "";
		            	    		 	for(DataColumn dc : dt.Columns){
		            	    		 		paras += "&" + dc.ColumnName + "=" + dr.getValue(dc.ColumnName);
		            	    		 	}
		            	    		 	int i = 0;
		            	    			for(DataColumn dc : dt.Columns){
		            	    				String name = dc.ColumnName.toLowerCase();
		            	    				if (!"pflowno".equals(name) && !"pworkID".equals(name)
	           										&& !"no".equals(name) && !"name".equals(name)) {
		            	    					String val = dr.getValue(dc.ColumnName)==null?"":dr.getValue(dc.ColumnName).toString();
		            	    					i++;
		            	    					 //输出连接.
		            	                        if (1==i){
		            	                        	if(fl.getStartGuideWay() == StartGuideWay.ByHistoryUrl){
		            	                        		String myurl = url1+"&CopyFormWorkID="+dr.getValue("OID")+"&CopyFormNode="+Integer.valueOf(FK_Flow)+ "01";
		            	    		%>
			            	    						<TD nowrap="nowrap">
			            	    							<a href="<%=myurl%>"><%=val %></a>
			            	    						</TD>
		            	    						<%}else{ %>
			            	    						<TD nowrap="nowrap">
			            	    							<a href="<%=paras%>"><%=val %></a>
			            	    						</TD>
		            	    						<%}%>
		            	    		           <%}else{ %>
		            	    		           		<TD nowrap="nowrap"><%=val %></TD>
		            	    		           <%} %>
		            	    		       <%} %>
		            	    		  <%}%>
		            	    	</TR>
		            	       <%} %>
		            	</Table>
	            	<%
            	 }else if(StartGuideWay.ByHistoryUrl == startGuidWay){
            	 			if(dt.Rows.size() == 0){
            	 				String url = basePath+"WF/MyFlow.htm?FK_Flow="+FK_Flow+"&FK_Node="+Integer.valueOf(FK_Flow)+"01&WorkID=0&IsCheckGuide=1";
            	 				response.sendRedirect(url);
            	 			}else{
            	 	%>		
		            	 		<Table width='100%' >
				            		<TR>
				            			<TH>IDX</TH>
				            			<%
				            				for(DataColumn dc : dt.Columns){
					            				String name = dc.ColumnName.toLowerCase();
			           							if (!"pflowno".equals(name) && !"pworkID".equals(name)
		           										&& !"no".equals(name) && !"name".equals(name)) {
				            			%>
				            						<TH><%=dc.ColumnName %></TH>
				            					<%} %>
				            			<%} %>
				            		</TR>
				            	    <%
				            	    	// 输出数据.
				                    	int idx2 = 0; 
				            	    	String url2 = basePath+"WF/MyFlow.htm?FK_Flow="+FK_Flow+"&FK_Node="+Integer.valueOf(FK_Flow)+"01&WorkID=0&IsCheckGuide=1";
				            	    	for(DataRow dr : dt.Rows){
				            	    		idx2++;
				            	    %>
				            	    	<TR>
				            	    		<TD class="Idx" nowrap="nowrap"><%=idx2 %></TD>
				            	    		<%
				            	    		 	String paras = url2 + "";
				            	    		 	for(DataColumn dc : dt.Columns){
				            	    		 		paras += "&" + dc.ColumnName + "=" + dr.getValue(dc.ColumnName);
				            	    		 	}
				            	    		 	int i = 0;
				            	    			for(DataColumn dc : dt.Columns){
				            	    				String name = dc.ColumnName.toLowerCase();
				            	    				if (!"pflowno".equals(name) && !"pworkID".equals(name)
			           										&& !"no".equals(name) && !"name".equals(name)) {
				            	    					String val = dr.getValue(dc.ColumnName)==null?"":dr.getValue(dc.ColumnName).toString();
				            	    					i++;
				            	    					 //输出连接.
				            	                        if (1==i){
				            	                        	if(fl.getStartGuideWay() == StartGuideWay.ByHistoryUrl){
				            	                        		String myurl = url2+"&CopyFormWorkID="+dr.getValue("OID")+"&CopyFormNode="+Integer.valueOf(FK_Flow)+ "01";
				            	    		%>
					            	    						<TD nowrap="nowrap">
					            	    							<a href="<%=myurl%>"><%=val %></a>
					            	    						</TD>
				            	    						<%}else{ %>
					            	    						<TD nowrap="nowrap">
					            	    							<a href="<%=paras%>"><%=val %></a>
					            	    						</TD>
				            	    						<%}%>
				            	    		           <%}else{ %>
				            	    		           		<TD nowrap="nowrap"><%=val %></TD>
				            	    		           <%} %>
				            	    		       <%} %>
				            	    		  <%}%>
				            	    	</TR>
				            	       <%} %>
				            	</Table>
            	 	<%		}
            	 }else{
            	 			String pksVal = "no";
            	 	        String pksLab = "name";
            	 	%>
            	 			<Table width='100%' >
	            	 			<TR>
					            	<TH>IDX</TH>
					            	<TH>选择全部</TH>
	            	 				<%
			            				for(DataColumn dc : dt.Columns){
				            				String name = dc.ColumnName.toLowerCase();
		           							if ("ctitle".equals(name) || "cworkID".equals(name)){
		           							  	pksVal = "CWorkID";
		           	                        	pksLab = "CTitle";
		           							}else if("no".equals(name) || "name".equals(name)){
		           							 	pksVal = "no";
		           	                        	pksLab = "name";
		           							}else{
			            			%>
			            						<TH><%=dc.ColumnName %></TH>
			            					<%} %>
			            			<%} %>
	            	 			</TR>
	            	 			<%
	            	 			 	// 输出数据.
	            	            	int idx3 = 0;
	            	 				for(DataRow dr : dt.Rows){
		            	    			idx3++;
	            	 			%>
	            	 				<TR>
				            	    	<TD class="Idx" nowrap="nowrap"><%=idx3 %></TD>
				            	        <TD>
				            	        	<input id="CB_<%=dr.getValue(pksVal) %>" type="checkbox" value="<%=dr.getValue(pksVal) %>" name="CB_<%=dr.getValue(pksVal) %>" /><label for="CB_<%=dr.getValue(pksVal) %>"><%=dr.getValue(pksLab) %></label>
				            	        </TD>
				            	        <%
				            	        	for(DataColumn dc : dt.Columns){
				            	        		String name = dc.ColumnName.toLowerCase();
			            	    				if (!"ctitle".equals(name) && !"ctorkID".equals(name)
		           										&& !"no".equals(name) && !"name".equals(name)) {
			            	    					String val = dr.getValue(dc.ColumnName)==null?"":dr.getValue(dc.ColumnName).toString();
				            	        %>
				            	        			<TD nowrap="nowrap"><%=val %></TD>
				            	        	  <%} %>
				            	        <%} %>
				            	    </TR>
	            	 			<%} %>
            	 			</Table>
            	<%} %>
            <%} %>
		</div>
	</form>
</body>
</html>
