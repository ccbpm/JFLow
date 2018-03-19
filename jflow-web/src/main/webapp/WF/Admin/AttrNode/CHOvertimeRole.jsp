<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WF/head/head2.jsp"%>
<%@taglib uri='http://java.sun.com/jstl/core_rt' prefix='c'%>
<%@page import= "java.util.*"%>
<%
   CHOvertimeRoleModel Role = new CHOvertimeRoleModel(request, response,basePath);
	Role.Page_Load();
	List  listId = Role.listId;
	List  listName = Role.listName;
	%>

<script type="text/javascript">
window.onload = function() {
	var listId =<%=listId%>;
	var listName =<%=listName%>;
	var tou = "<li style='color: Gray'>要跳转到的节点:<select id='TB_AppointNode' name='TB_AppointNode' class='required'>";
	var wei = "</select>";
	var open="";
	for(var i =0;i<listId.length;i++){	
		open += "<option value='"+listId[i]+"' >"+listName[i]+"</option> ";
	}
	var opens = tou+open+wei;
	var testdiv = document.getElementById("testdiv");
	testdiv.innerHTML= opens;
	$("#TB_AppointNode").val(<%=Role.getAppointNode()%>);
}
/*隐藏与显示.*/
function ShowHidden(ctrlID) {
   var ctrl = document.getElementById(ctrlID);
   if (ctrl.style.display == "block") {   
       ctrl.style.display = 'none';
   } else {
       ctrl.style.display = 'block';
   }
}

//保存按钮点击触发事件
function btn_Save_Click(){
	 var FK_Node = '<%=Role.getFK_Node() %>';
	  var RolekType="";
	  var blockContent="";
	 if($("#RB_None").attr("checked")=="checked"){
		 RolekType="None";
		 RoleContent="";
		
	}else if($("#RB_down").attr("checked")=="checked"){
		RolekType="down";
		RoleContent="";
		
	}else if($("#RB_delete").attr("checked")=="checked"){
		RolekType="delete";
		RoleContent="";
		
	}else if($("#RB_AppointNode").attr("checked")=="checked"){
		RolekType="AppointNode";
		RoleContent=document.getElementById("TB_AppointNode").value;
	}else if($("#RB_Transfer").attr("checked")=="checked"){
		RolekType="Transfer";
		RoleContent=document.getElementById("TB_Transfer").value;
	}else if($("#RB_SQL").attr("checked")=="checked"){
		RolekType="BySQL";
		RoleContent=document.getElementById("TB_SQL").value;
	}else if($("#RB_EMP").attr("checked")=="checked"){
		RolekType="EMP";
		RoleContent=document.getElementById("TB_EMP").value;
	}else {
		alert("请选择转向条件");
		return;
	}	
	 if(document.getElementById("TB_IsEval").checked) {
	 var TB_IsEval=document.getElementById("TB_IsEval").value;
	
	 } 
	location.href="<%=basePath%>CHOvertime/CHOvertime_btn_Save.do?FK_Node="+ FK_Node+ "&RolekType="+ RolekType+"&RoleContent="+RoleContent+"&TB_IsEval="+TB_IsEval;
}

</script>
</head>
<body>
	
			<table style="width: 100%;">
				<caption>超时处理规则</caption>

				<tr>
					<td>
						<fieldset>
							<legend>
								<input id="RB_None" type="radio" name="xxx" <%=Role.radioCheck1 %>/><label
									style="cursor: pointer;" for="RB_None">不处理</label>
							</legend>
							<ul>
								<li style="color: Gray">超时的时候一直处理超时的状态。</li>
							</ul>
						</fieldset>	
						<fieldset>
							<legend>
								<input id="RB_down" type="radio" name="xxx" <%=Role.radioCheck2 %>/><label
									style="cursor: pointer;" for="RB_down">自动向下运动</label>
							</legend>
							<ul>
								<li style="color: Gray">超时了当前节点自动运动到下一个环节，如果要控制特定的条件下不向下运动，就需要在当前节点的发送前事件里编写相关的业务逻辑。</li>
							</ul>
						</fieldset>	
						<fieldset>
							<legend>
								<input id="RB_AppointNode" type="radio" name="xxx" <%=Role.radioCheck3 %>/><label
									style="cursor: pointer;" for="RB_AppointNode">跳转到指定的节点</label>
							</legend>
							
							<ul>
								
								<div id = "testdiv">
								</div>
								</li>
							</ul>
						</fieldset>	
						<fieldset>
							<legend>
								<input id="RB_Transfer" type="radio" name="xxx" <%=Role.radioCheck4 %>/><label
									style="cursor: pointer;" for="RB_transfer">移交给指定的人员</label>
							</legend>					
							<a href="javascript:ShowHidden('shift');" > 请输入要移交的工作人员：</a>
							<div id="shift" style="display: none">
								<ul>
									<li>接受输入的必须是人员的工作帐号。</li>
									<li>如果有多个人元用半角的逗号分开，比如: zhangsan,lisi。</li>
									<li>超时后就自动的移交给指定的工作人员。</li>
								</ul>
							</div>
							<br>
							<input id="TB_Transfer" type="text" style="width:700px;" value="<%=Role.Transfer %>"></input>

						</fieldset>


						<fieldset>
							<legend>
								<input id="RB_EMP" type="radio" name="xxx" <%=Role.radioCheck5 %>/><label
									style="cursor: pointer;" for="RB_EMP">向指定的人员发信息,如果设置为空就向当前人发信息.</label>
							</legend>							
							<a href="javascript:ShowHidden('emps');"> 请输入要发送的工作人员：</a>
							<div id="emps" style="display: none">
								<ul>
									<li>接受输入的必须是人员的工作帐号。</li>
									<li>如果有多个人元用半角的逗号分开，比如: zhangsan,lisi。</li>
									<li>超时后，系统就会向这些人员发送消息。</li>
								</ul>
							</div>
							<br>
							<input id="TB_EMP" type="text" style="width:700px;" value="<%=Role.Emp %>"></input>
						</fieldset>

						<fieldset>
							<legend>
								<input id="RB_delete" type="radio" name="xxx" <%=Role.radioCheck6 %>/><label
									style="cursor: pointer;" for="RB_delete">删除流程</label>
							</legend>							
							<ul style="color: Gray">
								<li>超时后就自动删除当前的流程。</li>
							</ul>
						</fieldset>
						<fieldset>						
								<legend>
								<input id="RB_SQL" type="radio" name="xxx" <%=Role.radioCheck7 %>/><label
									style="cursor: pointer;" for="RB_sql">执行SQL</label>
							</legend>

							<a href="javascript:ShowHidden('sql');"> 请输入要执行的SQL：</a>
							<div id="sql" style="display: none">
								<ul>
									<li>当前的的sql支持ccbpm的表达式.</li>
									<li>执行相关的SQL，处理相关的业务逻辑。</li>
								</ul>
							</div>
							<br>
							<input id="TB_SQL" type="text" style="Width:700px;" value="<%=Role.SQL %>"></input>
						</fieldset>


						<fieldset>
							<legend>其他选项 </legend>
							<input id="TB_IsEval"  type="checkbox" style="cursor: pointer;" for="TB_IsEval" <%=Role.IsEval%>>是否质量考核点</input>							
						</fieldset>
						<!-- <input ID="Btn_Save"  type="button" onclick="Btn_Save_Click();" value="保存" /> -->
						<a href="javascript:btn_Save_Click();" id="Btn_Save" name="Btn_Save" class="easyui-linkbutton l-btn" iconcls="icon-save"> 保存</a>									
					</td>
					<td valign="top"></td>


				</tr>
			</table>		
</body>
</html>