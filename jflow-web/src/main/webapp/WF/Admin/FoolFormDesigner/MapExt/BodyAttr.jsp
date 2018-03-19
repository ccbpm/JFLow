<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%@page import="cn.jflow.model.wf.mapdef.mapext.*"%>
<%
BodyAttrModel bam=new BodyAttrModel(request,response);
bam.init();
%>
<script type="text/javascript">
function Esc() {
    if (event.keyCode == 27)
        window.close();
    return true;
}
function WinOpen(url, name) {
    window.open(url, name, 'height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
    //window.open(url, 'xx');
}
function TROver(ctrl) {
    ctrl.style.backgroundColor = 'LightSteelBlue';
}

function TROut(ctrl) {
    ctrl.style.backgroundColor = 'white';
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

function btn_save(){
	var FK_MapData='<%=bam.getFK_MapData()%>';
	var TB_Attr=$("#TB_Attr").val();
	var url='<%=basePath%>wf/mapdef/mapext/bodyattr/btn_Save_Click.do';
	$.ajax({  
	      type:'post',  
	      url:url,  
	      data:{FK_MapData:FK_MapData,TB_Attr:TB_Attr},  
	      cache:false,  
	      success:function(data){
	    	  if(data="success"){
	    		 alert("保存成功！");
	    	  }
	       },  
	       error:function(){
	    	   alert("出错了！");
	       }  
	 });
}




</script>

</head>
<body onkeypress="Esc();" class="easyui-layout"  topmargin="0" leftmargin="0">
	<form method="post" action="" id="form1">
		<input type="hidden" id="success" value="${success }" />
		<div data-options="region:'center',title:''" style="padding: 5px;">
			<div style='width: 100%'>

				<table style="width: 100%">
					<caption>表单body属性</caption>
					<tr>
						<td>  <textarea name="TB_Attr" rows="2" cols="20" id="TB_Attr" style="height:88px;width:522px;"><%=bam.getText() %></textarea></td>
					</tr>
					<tr>
						<td>  <input type="button" name="Btn_Save" value="保存" id="Btn_Save" onclick="btn_save();"/></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</body>
</html>