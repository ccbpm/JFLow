<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
	<%@ page import="cn.jflow.model.wf.rpt.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	
	DealSubThreadReturnToHLModel dst=new DealSubThreadReturnToHLModel( request,response);
	dst.init();

%>
<link rel='stylesheet' type='text/css' href='<%=basePath%>WF/Comm/Style/Table.css'  />
<link rel='stylesheet' type='text/css' href='<%=basePath%>WF/Comm/Style/Table0.css'  />
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript">


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

function ChooseEmp() {

    var hValue = $("#TB_ShiftNo").val();
    var url = 'SelectUser.jsp?OID=123&CtrlVal=' + hValue;
    var v = window.showModalDialog(url, 'dfg', 'dialogHeight: 450px; dialogWidth: 650px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
    if (v == null || v == '' || v == 'NaN') {
        return false;
    }

    var arr = v.split('~');
    var emparr = arr[0].split(',');
    $("#TB_ShiftNo").val(arr[0]);

    if (emparr.length > 1) {
        alert('输入的移交人（' + arr[1] + '）不正确，移交人只能选择一个，请重新选择！');
        return false;
    }
    $("#TB_ShiftName").val(arr[1]);
    return false;
}

function ss(){
	
	alert(11);
}


var FK_Node='<%=dst.getFK_Node()%>';
var WorkID='<%=dst.getWorkID()%>';
var FID='<%=dst.getFID()%>';
var FK_Flow='<%=dst.getFK_Flow()%>';



function btn_Send_Click(){
	var TB_ShiftNo=$("#TB_ShiftNo").val();
	var TB_Doc=$("#TB_Doc").val();
	location.href="<%=basePath%>WF/dstrToHL/Btn_Send_Click.do?FK_Flow="+ FK_Flow + "&FK_Node=" + FK_Node + "&WorkID=" + WorkID+ "&FID=" + FID + "&TB_ShiftNo=" + TB_ShiftNo + "&TB_Doc="+ TB_Doc;
}
function btn_Shift_Click(){
	var TB_ShiftNo=$("#TB_ShiftNo").val();
	var TB_Doc=$("#TB_Doc").val();
	if(TB_ShiftNo==null || TB_ShiftNo==''){
		alert("请选择移交人");
		return false;
	}
	if(TB_Doc==null || TB_Doc==''){
		alert("请填写处理人信息");
		return false;
	}
	location.href="<%=basePath%>WF/dstrToHL/Btn_Shift_Click.do?FK_Flow="
				+ FK_Flow + "&FK_Node=" + FK_Node + "&WorkID=" + WorkID
				+ "&FID=" + FID + "&TB_ShiftNo=" + TB_ShiftNo + "&TB_Doc="
				+ TB_Doc;
}

function btn_Del_Click(){
	var url="<%=basePath%>WF/dstrToHL/Btn_Del_Click.do?FK_Flow="+ FK_Flow + "&FK_Node=" + FK_Node + "&WorkID=" + WorkID;
	$.ajax({  
	      type:'post',  
	      url:url,  
	      cache:false,  
	      success:function(data){
	    	  alert(data);
	       },  
	       error:function(){
	    	   alert("出错了！");
	       }  
	 });
}
</script>
</head>
<body bgcolor="silver"  leftmargin="0" onkeypress="NoSubmit(event);" topmargin="0">
<div style="text-align: center; position: absolute; background-color: white; height: 100%;left: 15%; right: 15%">
 <table class="style1" style="width: 100%;">
        <caption>您好:<%=dst.getWebUser() %>,退回给子线程处理人.</caption>
        <tr> 
            <td style="text-align: left">
                <%=dst.getMsgInfo()%>
            </td>
        </tr>
        <tr>
            <td>
                移交其他人处理
            </td>
        </tr>
        <tr>
            <td>
                移交人
               <input name="ShiftName" type="text" size="40" id="TB_ShiftName" />
                <input type="hidden" name="ShiftNo" id="TB_ShiftNo" />
                <input type="submit" name="Btn_ChooseEmp" value="选择人" onclick="return ChooseEmp();" id="Btn_ChooseEmp" />

            </td>
        </tr>
        <tr>
            <td>
                处理人信息
            </td>
        </tr>
        <tr>
            <td>
            	<textarea name="TB_Doc" rows="2" cols="20" id="TB_Doc" style="height:100px;width:98%;"></textarea>
            </td>
        </tr>
        <tr>
            <td style="text-align: center">
                <input type="button" name="Btn_Send" value="退回给子线程处理人" id="Btn_Send"  onclick="btn_Send_Click();"/>
                <input type="button" name="Btn_Shfit" value="移交给其他人处理" id="Btn_Shfit"  onclick="btn_Shift_Click();"/>
                <input type="button" name="Btn_Del" value="删除子线程" id="Btn_Del"  onclick="btn_Del_Click();"/>
            </td>

        </tr>
    </table>

</div>
</body>
</html>