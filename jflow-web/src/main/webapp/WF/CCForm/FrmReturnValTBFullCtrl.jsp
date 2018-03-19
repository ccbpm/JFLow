<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cn.jflow.common.model.FrmReturnValTBFullCtrlModel"%>
<%@ include file="/WF/head/head1.jsp"%>
	
<%  FrmReturnValTBFullCtrlModel frmModel = new FrmReturnValTBFullCtrlModel(request, response);
	frmModel.loadModel();
	String FK_MapExt = request.getParameter("FK_MapExt");
	String CtrlVal = request.getParameter("CtrlVal");
%>
<script type="text/javascript" language="javascript">
	function btn_Search_Click(tb){
		var key = $("#TB_Key").val();
		var url = ("FrmReturnValTBFullCtrl.jsp?FK_MapExt=" + tb + "&CtrlVal=" + key);
		window.location.href=url;
	}
	function btn_Click(){
		var CtrlVal = getValue();  
		var url = "<%=basePath%>WF/FrmReturnValTBFullCtrl/Btn_Save_Click.do?FK_MapExt=<%=FK_MapExt%>&CtrlVal=" + CtrlVal ;
		$.ajax({
			url:url,
			type:'post', //数据发送方式
			dataType:'json', //接受数据格式
			//data:{FK_MapExt:FK_MapExt,CtrlVal:CtrlVal},
			async: false ,
			error: function(data){
				alert("保存失败");
			},
			success: function(data){
				alert("保存成功");
			}
		});
		
		$("#TB_CaoZuoYuan",window.opener.document).val($("table tr:eq(2) td:eq(1)").text());
		$("#TB_MingCheng",window.opener.document).val($("table tr:eq(2) td:eq(2)").text());
		$("#DDL_FK_Dept",window.opener.document).val($("table tr:eq(2) td:eq(3)").text());
		window.close();
	}
    function getValue(){  
        // method 1   
        var radio = document.getElementsByName("sd");  
        for (i=0; i<radio.length; i++) {  
            if (radio[i].checked) {  
                alert(radio[i].value);  
            	return radio[i].value;
            }  
        }  
    }  
</script>
<body >
     	<%=frmModel.Pub.toString() %>
</body>
</html>