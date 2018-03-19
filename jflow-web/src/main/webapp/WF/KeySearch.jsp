<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
%>
<head>
<title>驰骋工作流程管理系统</title>
<script language="JavaScript" src="<%=basePath%>WF/Comm/JScript.js" type="text/javascript"></script>
<script language="JavaScript" src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js" defer="defer" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<link href="<%=basePath%>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
    function OpenIt(fk_flow, fk_node, workid) {
        var url = '<%=basePath%>WF/WorkOpt/OneWork/Track.jsp?WorkID=' + workid + '&FK_Flow=' + fk_flow + '&FK_Node=' + fk_node;
        var newWindow = window.open(url, 'card', 'width=700,top=50,left=50,height=500,scrollbars=yes,resizable=yes,toolbar=false,location=false');
        newWindow.focus();
        return;
    }
    function NoSubmit(ev) {
        if (window.event.srcElement.tagName == "TEXTAREA")
            return true;
        if (ev.keyCode == 13) {
            window.event.keyCode = 9;
            ev.keyCode = 9;
            return true;
        }
        return true;
    }
    function buttonClick(btnName){
    	var textBox1 = $("#TextBox1").val();
    	if(textBox1 == null || textBox1 == ""){
    		alert("关键词不能为空！");
    		return;
    	}
    	var date = new Date();
    	var isCheck = $("#CheckBox1").attr("checked");
    	var url = "<%=basePath%>WF/search/KeySearch.do?textBox1="+textBox1+"&btnName="+btnName+"&isCheck="+isCheck+"&s="+date;
		//$("#form1").attr("action",url);
		//$("#form1").submit();
		
		 $.ajax({
		      type:'post',  
		      url:url,  
		      cache:false,  
		      success:function(data){
		    	  $("#htmlInfo").html(data);
		       },  
		       error:function(){
		    	   alert("出错了！");
		       }  
		 }); 
    }
</script>
</head>
<body topmargin="0" leftmargin="0">
	<form method="post"  action="" id="form1">
	<div id="mainPanel"  region="center" border="true" border="false" class="mainPanel">
	
		<table width="100%" border="0">
			<caption class="CaptionMsgLong">全文检索</caption>
			<tr>
				<td style="text-align: center;"><b>&nbsp;输入任何关键字:</b>
				<input name="TextBox1" style="width: 300px"
						type="text" id="TextBox1" value="${text }"/>
				<span style="color: #0033CC; font-weight: bold;">
				<input id="CheckBox1" type="checkbox" name="CheckBox1" ${isCheck }/>仅查询我参与的流程</span><br />
				
				<br/>
				<button name="Btn_ByWorkID" id="Btn_ByWorkID" type="button" onclick="buttonClick('Btn_ByWorkID')">按工作ID查</button>
			<button name="Btn_ByTitle" id="Btn_ByTitle" type="button" onclick="buttonClick('Btn_ByTitle')" >按流程标题字段关键字查</button>
				</td>
			</tr>
		</table>
		<div id="htmlInfo"></div>
		${htmlStr }
		</div>
	</form>
</body>
</html>
