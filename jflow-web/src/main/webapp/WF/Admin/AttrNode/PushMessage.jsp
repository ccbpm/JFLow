<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@page import="java.util.*"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	PushMessageModel pm = new PushMessageModel(request,response);
	pm.Page_Load(); 
%>
<script type="text/javascript">

function getAllCheck()
{
	var str="";
	$("input[type='checkbox']:checked").each(function() 
		{
			str += $(this).attr("value")+",";
  		})
	return str;
}
	


	
//删除.
function Del(mypk, nodeid) {
    if (mypk == '') {
        alert("默认发送不允许删除，您可以修改。");
        return;
    }
    if (window.confirm('您确定要删除吗?') == false)
        return;
    var FK_Flow='<%=pm.getFK_Flow() %>';
    location.href = 'PushMessage.jsp?MyPK='+mypk+'&DoType=Del&FK_Flow='+FK_Flow+'&FK_Node='+nodeid;
}
	
function load() {
	var successStr = $("#success").val();
	if (successStr.length == 0) 
	{
		return;
	} else {
		alert(successStr);
	}
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

</script>
</head>
<body  onload="load();">
	<form method="post" action="" id="form1">
	<input type="hidden" id="success" value="${success }" />
		<div id="rightFrame" data-options="region:'center',noheader:true;scrolling:auto;">
			<div class="easyui-layout" data-options="fit:true;scrolling:auto;">
				<table style="width:100%">
					<caption>
					 <div style=" float:left"> 消息事件</div>   <div style="float:right"> <a href="http://ccbpm.mydoc.io"  target="_blank" >帮助</a> </div> </caption>
					<tr>
						<th>序号</th>
						<th>消息发生事件</th>
						<th>启用邮件</th>
						<th>邮件标题</th>
						<th>启用短信</th>
						<th>操作</th>
					</tr>
					<c:forEach items="<%=pm.getPushMesList() %>" var="msg" varStatus="i">
					<tr>
						<td>${i.count }</td>
						 <td><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&MyPK=${msg.mypk}&FK_Node=<%=pm.getFK_Node() %>&FK_Event=${msg.FK_Event }" > ${msg.FK_Event }</a></td>
						<%-- <td> ${msg.FK_Event }</td> --%>
						<td>${msg.MailPushWayText}</td>
						<td>${msg.MailTitle_Real }</td>
						<td>${msg.SMSPushWayText }</td>
						<td> <a href="javascript:Del('${msg.mypk }','<%=pm.getFK_Node() %>')" >删除</a>  </td>
					</tr>
					</c:forEach>
					 
					
				</table>


 			<fieldset style=" border:0px;">
         
              <input type="button" value="新建消息" id="Btn_Save" onclick="ShowHidden('state')" />
            <div id="state" style="display:none;color:Gray">
            <ul>
            
                <li><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&FK_Node=<%=pm.getFK_Node() %>&FK_Event=WorkArrive" > 工作到达</a> </li>
              
                <li><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&FK_Node=<%=pm.getFK_Node() %>&FK_Event=SendSuccess" > 节点发送成功时</a> </li>
              
                <li><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&FK_Node=<%=pm.getFK_Node() %>&FK_Event=ReturnAfter" > 当节点退回后</a> </li>
              
                <li><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&FK_Node=<%=pm.getFK_Node() %>&FK_Event=UndoneAfter" > 当节点撤销发送后</a> </li>
              
               <li><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&FK_Node=<%=pm.getFK_Node() %>&FK_Event=FlowOverAfter" > 流程结束后</a> </li>
               
               <li><a href="PushMessageEntity.jsp?FK_Flow=<%=pm.getFK_Flow() %>&FK_Node=<%=pm.getFK_Node() %>&FK_Event=AfterFlowDel" > 流程删除后</a></li>
            </ul>

            </div>
 		</fieldset>
				

			</div>
		</div>
	</form>
</body>
</html>