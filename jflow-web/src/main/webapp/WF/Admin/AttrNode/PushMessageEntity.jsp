<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@page import="java.util.*"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	PushMessageEntityModel pme = new PushMessageEntityModel(request,response);
	pme.Page_Load(); 
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
	
    //保存
	function btn_Save_Click() {
		var myPK='<%=pme.getMyPk()%>';
		var fk_flow = '<%=pme.getFK_Flow()%>';
		var fk_node='<%=pme.getFK_Node()%>';	
		var FK_Event='<%=pme.getFK_Event()%>';
		var RB_SMS=$("input[name='sms']:checked").val();
		var SMS_Fields=$("#DDL_SMS_Fields").val();
		var smsStr=$("#TB_SMS").val();
		var MailPushWay=$("input[name='email']:checked").val();
		var MailTitle_Real=$("#TB_Email_Title").val();
		var MailDoc_Real=$("#TB_Email_Doc").val();
		var DDL_Email=$("#DDL_Email").val();
		
		var sms_array=new Array();  
		$('input[name*="CB_SMS_"]:checked').each(function(){  
			sms_array.push($(this).attr('id'));
		});  
		var smsArrayStr=sms_array.join(',');
		
		var mail_array=new Array(); 
		$('input[name*="CB_Email_"]:checked').each(function(){  
			mail_array.push($(this).attr('id'));
		});  
		var mailArrayStr=mail_array.join(',');  
		
		<%-- var url="<%=basePath%>pushMessageEntity/pushMessageEntity_Btn_Save.do?FK_Flow="+ fk_flow+ "&FK_Node="+ fk_node+ "&myPK="+myPK+"&FK_Event="+FK_Event+"&RB_SMS="+RB_SMS+"&SMS_Fields="+SMS_Fields+"&smsStr="+smsStr+"&MailPushWay="+MailPushWay+"&MailTitle_Real="+MailTitle_Real+"&MailDoc_Real="+MailDoc_Real+"&DDL_Email="+DDL_Email; --%>
		var url="<%=basePath%>pushMessageEntity/pushMessageEntity_Btn_Save.do";
		 $.ajax({
		      type:'post',  
		      url:url,  
		      data:{FK_Flow:fk_flow,FK_Node:fk_node,myPK:myPK,FK_Event:FK_Event,RB_SMS:RB_SMS,SMS_Fields:SMS_Fields,smsStr:smsStr,MailPushWay:MailPushWay,MailTitle_Real:MailTitle_Real,MailDoc_Real:MailDoc_Real,DDL_Email:DDL_Email,smsArrayStr:smsArrayStr,mailArrayStr:mailArrayStr},
		      cache:false,  
		     // dataType:'json',  
		      success:function(data){
		    	  if(data="success"){
		    		  redirect_to_index();
		    	  }
		       },  
		       error:function(){
		    	   alert("出错了！");
		       }  
		 }); 
    } 
	
    
   	//返回
	function  redirect_to_index(){
		var fk_flow = '<%=pme.getFK_Flow()%>';
		var fk_node='<%=pme.getFK_Node()%>';	
		location.href="<%=basePath%>pushMessageEntity/redirect_to_index.do?FK_Flow="+ fk_flow+ "&FK_Node="+ fk_node;
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
				<input type="hidden" id="myPK" name="myPK" value="<%=pme.getMyPk()%>">
				<table style="width:100%">
					<caption> <div style=" float:left">WorkArrive   -  消息实体</div> 
					  <div style="float:right"> <a href="http://ccbpm.mydoc.io"  target="_blank" >帮助</a> </div> </caption>
					<tr>
					<td>
					
					<fieldset> 
					<legend>启用短信设置</legend>
					<table style="width:100%;" >
					<tr>
					   <td colspan="2">
					    <input id="RB_SMS_0" type="radio" name="sms" value="0" <%=pme.getSmsChecked0() %> /><label for="RB_SMS_0">不发送</label>
					   </td>
				   </tr>
				    <tr>
					   <td colspan="2">
					    <input id="RB_SMS_1" type="radio" name="sms" value="1" <%=pme.getSmsChecked1() %> /><label for="RB_SMS_1"><%=pme.getSmsTsText() %></label>
					   </td>
					</tr>   
					<tr>
					   <td>
					    <input id="RB_SMS_2" type="radio" name="sms" value="2" <%=pme.getSmsChecked2() %> /><label for="RB_SMS_2">表单上的字段作为接收对象</label>
					   </td> 
					   <td>
					       <select name="DDL_SMS_Fields" id="DDL_SMS_Fields" style="width: 260px;">
					       		<c:forEach items="<%=pme.getSmsFiledsList() %>" var="f">
									<option value="${f.KeyOfEn }" ${f.smsSelected }>${f.KeyOfEn }  ; ${f.NAME }</option>
								</c:forEach>
							</select>(手机号/微信号/丁丁号/CCIM人员ID) 
						</td>
					</tr>
					<tr>
					   <td>	
						<input id="RB_SMS_3" type="radio" name="sms" value="3" <%=pme.getSmsChecked3() %> /><label for="RB_SMS_3">其他节点的处理人(未完成)</label>
					  </td>
					   <td>	
					   		<%=pme.getSmsSb() %>
					  </td>
					  
					</tr>
					 <tr>
   						<td colspan="2">
						 <fieldset style=" border:0px;">
						            <legend><a href="javascript:ShowHidden('sms')">短信发送内容模版:</a></legend>
						            <div id="sms" style="display:none;color:Gray">
						            <ul>
						             <li>cc会根据不同的事件设置不同的信息模版。</li>
						             <li>这些模版都是标准的提示，如果您要需要个性化的提示请修改该模版。</li>
						             <li>该参数支持cc表达式。</li>
						             <li>您可以使用@符号来编写您所需要的内容。</li>
						             <li>对于信息提示有两个系统参数分别是{Title}流程标题， {URL} 打开流程的连接。</li>
						             <li>cc在生成消息的时候会根据模版把信息替换下来，发送给用户。</li>
						            </ul>
						            </div>
						 </fieldset>
					
							<textarea name="TB_SMS" rows="3" cols="20" id="TB_SMS" style="width:95%;"><%=pme.getSmsContent() %></textarea>
					    </td>
					</tr>
					</table>
					
					</fieldset>
					 
					
					
					<!--------   邮件提醒.   -------------------------->
					
					<fieldset> 
					<legend>启用邮件提醒</legend>
					<table style="width:100%;" >
					<tr>
					   <td colspan="2">
					   <input id="RB_Email_0" type="radio" name="email" value="0" <%=pme.getEmailChecked0() %> /><label for="RB_Email_0">不发送</label>
					  	</td>
					</tr>
					<tr>
					   <td colspan="2">	
					    <input id="RB_Email_1" type="radio" name="email" value="1" <%=pme.getEmailChecked1() %>/><label for="RB_Email_1"><%=pme.getMailTsText() %></label>
					   	</td>
					</tr>
					<tr>
					   <td width="20%">	
					    <input id="RB_Email_2" type="radio" name="email" value="2" <%=pme.getEmailChecked2() %>/><label for="RB_Email_2">表单上的字段作为邮件</label>
					   </td>
					   <td>	
					       <select name="DDL_Email" id="DDL_Email" style="width: 260px;">
								<c:forEach items="<%=pme.getMailFiledsList() %>" var="f">
									<option value="${f.KeyOfEn }" ${f.emailSelected }>${f.KeyOfEn }  ; ${f.NAME }</option>
								</c:forEach>
							</select>
					   </td>
					</tr>
					<tr>
					   <td>	
					    <input id="RB_Email_3" type="radio" name="email" value="3" <%=pme.getEmailChecked3() %>/><label for="RB_Email_3">其他节点的处理人</label>
					   </td>
					   <td>	
					   	<%=pme.getMailSb() %>
					   </td>
					</tr>
					
					<tr>
					<td>
					
						 <fieldset style=" border:0px;">
						            <legend><a href="javascript:ShowHidden('titeemail')">邮件标题模版:</a></legend>
						            <div id="titeemail" style="display:none;color:Gray">
						            <ul>
						             <li>该参数支持cc表达式。</li>
						             <li>您可以使用@符号来编写您所需要的内容。</li>
						            </ul>
						            </div>
						 </fieldset>
					</td><td>	
						 <input name="TB_Email_Title" id="TB_Email_Title" type="text" value="<%=pme.getEmailTitle() %>" id="TB_Email_Title" style="width:95%;" />
					</td></tr>	
					<tr>
					<td>	
						 <fieldset style=" border:0px;">
						            <legend><a href="javascript:ShowHidden('st2ate')">邮件内容模版:</a></legend>
						            <div id="st2ate" style="display:none;color:Gray">
						            <ul>
						             <li>该参数支持cc表达式。</li>
						             <li>您可以使用@符号来编写您所需要的内容。</li>
						            </ul>
						            </div>
						 </fieldset>
						 </td><td>
						&nbsp;<textarea name="TB_Email_Doc" id="TB_Email_Doc" rows="3" cols="20" id="TB_Email_Doc" style="width:95%;"><%=pme.getEmailContent() %></textarea>
						    </td>
						</tr>
						</table>
						
						</fieldset>
						</td>
						
						
						  
						</tr>
						
						</table>
					
				 <input type="button" name="Btn_Save" value="保存" id="Btn_Save" onclick="btn_Save_Click()"/>
 				 <input type="button" value="返回" onclick="redirect_to_index();" />

				

			</div>
		</div>
	</form>
</body>
</html>