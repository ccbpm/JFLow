<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.jflow.model.wf.admin.findworker.*"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
   		NodeCCRoleModel  nodecc=new NodeCCRoleModel(request, response);
   		nodecc.pageLoad();
        BP.WF.Node nd = new BP.WF.Node(nodecc.getFK_Node());
        
        BP.WF.Template.CCStations nss = new BP.WF.Template.CCStations();
        nss.Retrieve(BP.WF.Template.CCStationAttr.FK_Node, nodecc.getFK_Node());

        BP.WF.Template.CCDepts ndepts = new BP.WF.Template.CCDepts();
        ndepts.Retrieve(BP.WF.Template.CCDeptAttr.FK_Node, nodecc.getFK_Node());

        BP.WF.Template.CCEmps nEmps = new BP.WF.Template.CCEmps();
        nEmps.Retrieve(BP.WF.Template.CCEmpAttr.FK_Node, nodecc.getFK_Node());
    %> 
<script type="text/javascript"> 
    var FK_Node='<%=nodecc.getFK_Node()%>';
    function btn_save(v){
	
	 var DDL_CCRole=$("#DDL_CCRole").val();
	 var DDL_CCWriteTo=$("#DDL_CCWriteTo").val();
	 var TB_title=$("#TB_title_MB").val();
	 var TB_content=$("#TB_content_MB").val();
	 var CC_GW =$("input[name='CC_GW']").is(":checked");
	 var CC_DBM=$("input[name='CC_DBM']").is(":checked");
	 var CC_RY=$("input[name='CC_RY']").is(":checked");
	 var CC_SQL=$("input[name='CC_SQL']").is(":checked");
	 var CC_SQL_TEXT=$("CC_SQL_TEXT").val();
	 
	 
	 var url="<%=basePath%>WF/NodeCcRole/btn_Save.do?";
		$.ajax({
		      type:'post',  
		      url:url,  
		      data:{FK_Node:FK_Node,DDL_CCRole:DDL_CCRole,DDL_CCWriteTo:DDL_CCWriteTo,TB_title:TB_title,TB_content:TB_content,CC_GW:CC_GW,CC_DBM:CC_DBM,CC_RY:CC_RY,CC_SQL:CC_SQL,CC_SQL_TEXT:CC_SQL_TEXT},
		      cache:false,  
		      success:function(data){
		    	  if(data="success"){
		    		 alert("保存成功!");
		    	  }
		       },  
		       error:function(){
		    	   alert("出错了！");
		       }  
		 }); 
 }

</script>

</head>
<body onkeypress="Esc();" topmargin="0" leftmargin="0"   style="font-size:smaller">
<form method="post" action="" id="form1">

		<div>
			<table style="width: 100%;">
				<caption>抄送设置</caption>
				<tr>
					<th colspan="2">基本设置</th>
				</tr>
				<tr>
					<td class="style1">抄送规则</td>
					<td><select name="DDL_CCRole" id="DDL_CCRole">
							<c:forEach items="<%=nodecc.getCsgzList() %>" var="csgz">
								<option value="${csgz.value }" ${csgz.selected }>${csgz.lab }</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td class="style1">抄送写入规则</td>
					<td><select name="DDL_CCWriteTo" id="DDL_CCWriteTo">
							<c:forEach items="<%=nodecc.getCsxrgzList() %>" var="csxrgz">
								<option value="${csxrgz.value }" ${csxrgz.selected }>${csxrgz.lab }</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td class="style1">标题模板</td>
					<td><input name="TB_title_MB" type="text" value="<%=nodecc.getTitle() %>"
						id="TB_title_MB" style="width: 421px;" /></td>
				</tr>
				<tr>
					<td class="style1">内容模版</td>
					<td><textarea name="TB_content_MB" rows="2" cols="20"
							id="TB_content_MB" style="height: 90px; width: 421px;"><%=nodecc.getContent() %></textarea></td>
				</tr>
				<tr>
					<th colspan="2">自动抄送人范围</th>
				</tr>
				<tr>
					<td class="style1"><input id="CC_GW" type="checkbox" <%=nodecc.getGwChecked() %>
						name="CC_GW" /><label for="CC_GW">抄送到岗位</label></td>
					<td><a
						href="javascript:WinOpen('<%=basePath%>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.CCs&EnName=BP.WF.Template.CC&AttrKey=BP.WF.Template.CCStations&NodeID=<%=nodecc.getFK_Node() %>&r=0607102346&ShowWay=None')">
							请选择岗位(<%=nss.size() %>)</a></td>
				</tr>
				<tr>
					<td class="style1"><input id="CC_DBM" type="checkbox" <%=nodecc.getBmChecked() %>
						name="CC_DBM" /><label for="CC_DBM">抄送到部门</label></td>
					<td><a
						href="javascript:WinOpen('<%=basePath%>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.CCs&EnName=BP.WF.Template.CC&AttrKey=BP.WF.Template.CCDepts&NodeID=<%=nodecc.getFK_Node() %>&r=0607102346&ShowWay=None')">
							请选择部门(<%=ndepts.size() %>)</a></td>
				</tr>
				<tr>
					<td class="style1"><input id="CC_RY" type="checkbox" <%=nodecc.getRyChecked() %>
						name="CC_RY" /><label for="CC_RY">抄送到人员</label></td>
					<td><a
						href="javascript:WinOpen('<%=basePath%>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.CCs&EnName=BP.WF.Template.CC&AttrKey=BP.WF.Template.CCEmps&NodeID=<%=nodecc.getFK_Node() %>&r=0607102346&ShowWay=FK_Dept')">
							请选择人员(<%=nEmps.size() %>)</a></td>
				</tr>
				<tr>
					<td class="style1"><input id="CC_SQL" type="checkbox" <%=nodecc.getSqlChecked() %>
						name="CC_SQL" /><label for="CC_SQL">按照SQL设置范围</label></td>
					<td>
						<textarea name="CC_SQL_TEXT" rows="2" cols="20" id="CC_SQL_TEXT"style="width: 421px;"><%= nodecc.getSqlText() %></textarea>
					</td>
				</tr>
			</table>
			<input type="button" name="Btn_Save" value="保存" id="Btn_Save" onclick="btn_save('1');"/>
			<!-- <input type="button" name="Btn_SaveAndClose" value="保存并关闭" id="Btn_SaveAndClose" />  -->
			<!-- <input type="button" name="Btn_Close" value="关闭" id="Btn_Close" /> -->
		</div>
	</form>

</body>
</html>