<%@page import="BP.WF.Glo"%>
<%@page import="BP.En.UAC"%>
<%@page import="cn.jflow.common.model.CommSearchModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	CommSearchModel csm = new CommSearchModel(request, response);
	String errmsg = "";
	//根据用户初始化样式
	UAC uac = csm.getHisEn().getHisUAC();
	if (!uac.IsView) {
		errmsg = "您没有查看[" + csm.getHisEn().getEnDesc() + "]数据的权限.";
	} else {
		if (csm.getIsReadonly()) {
			uac.IsDelete = false;
			uac.IsInsert = false;
			uac.IsUpdate = false;
		}

		if (request.getParameter("PageIdx") == null)
			csm.setPageIdx(1);
		else
			csm.setPageIdx(Integer.parseInt(request
					.getParameter("PageIdx")));

		if (!uac.IsView)
			errmsg = "@对不起，您没有查看的权限！";
	}
	csm.init();
//String basePath = Glo.getCCFlowAppPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target=_self  />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>驰骋技术</title>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%@ include file="/WF/head/head1.jsp"%>
<script type="text/javascript">
document.onkeydown = function () {
    if (window.event && window.event.keyCode == 13) {
        window.event.returnValue = false;
    }
}
</script>
</head>
<body onkeypress="Esc()" onkeydown="DoKeyDown();" topmargin="0" leftmargin="0">
<span style="color: red;"><%=errmsg %></span>
	<table id="Table1" align="left" CellSpacing="1" CellPadding="1" border="0" width="100%">
		<caption> <%=csm.getLable1() %></caption> 
		<TR>
			<TD class="ToolBar">
			<form id="search_from">
				<input type="hidden" id="FormHtml" name="FormHtml" value="">
				<div id="toolbar_div">
					<%=csm.getToolBar() %>
				</div>
             </form>
                  </TD>
		</TR>

		<TR align="justify" height="350px" valign=top  >
			<TD  width='100%'  >
				<div id="uc_sys_01">
				<%=csm.getUCSys1() %>
				</div>
			</TD>
		</TR>

		<TR>
			<TD ><FONT face="宋体" size=2 >
				<div id="uc_sys_02">
				<%=csm.getUCSys2() %>
				<div>
			</TD>
		</TR>
	</table>
<script language="javascript" type="text/javascript">
	function ShowEn(url, wName, h, w) {
		var s = "dialogWidth=" + parseInt(w) + "px;dialogHeight="
				+ parseInt(h) + "px;resizable:yes";
		var val = window.open(url, null, s);
		window.location.href = window.location.href;
	}
	function RefMethod1(path, index, warning, target, ensName, keys) {
	    if (warning != null && warning != '' && warning != 'null' &&  warning !== undefined) {
	        if (confirm(warning)){
	        	 var url = path+"WF/Comm/RefMethod.jsp?Index=" + index + "&EnsName=" + ensName + keys;
	        	 if (target == null || target == '')
	        	 	var b = WinOpen(url, 'remmed');
	        	 else
	        	    var a = WinOpen(url, target);
	        	// window.location.href = window.location.href;
	        }
	    }else{
	    	 var url = path+"WF/WorkOpt/OneWork/Track.jsp?Index=" + index + "&EnsName=" + ensName + keys;
	    	 if (target == null || target == '')
	    	 	var b = WinOpen(url, 'remmed');
	    	 else
	    	 	var a = WinOpen(url, target);
	    	// window.location.href = window.location.href;
	    }
	}
	function ImgClick() {
	}
	function OpenAttrs(ensName) {
		var url = './Sys/EnsAppCfg.jsp?EnsName=' + ensName;
		var s = 'dialogWidth=680px;dialogHeight=480px;status:no;center:1;resizable:yes'
				.toString();
		val = window.open(url, null, s);
		window.location.href = window.location.href;
	}
	function DDL_mvals_OnChange(ctrl, ensName, attrKey) {
		var idx_Old = ctrl.selectedIndex;
		if (ctrl.options[ctrl.selectedIndex].value != 'mvals')
			return;
		if (attrKey == null)
			return;

		var url = 'SelectMVals.jsp?EnsName=' + ensName + '&AttrKey='
				+ attrKey;
		var val = window
				.showModalDialog(url, 'dg',
						'dialogHeight: 450px; dialogWidth: 450px; center: yes; help: no');
		if (val == '' || val == null) {
			// if (idx_Old==ctrl.options.cont
			ctrl.selectedIndex = 0;
			//    ctrl.options[0].selected = true;
		}
	}
	
	function Search() {
	$("#FormHtml").val($("#toolbar_div").html());
		$.ajax({
			cache: true,
			type: "POST",
	        url:"<%=basePath%>WF/Comm/SearchUI.do?EnsName=<%=csm.getEnsName()%>",
			data:$('#search_from').serialize(),
		    success: function(data) {
				var obj=eval("("+data+")");
				$("#uc_sys_01").html(obj.uc_sys_01);
 				$("#uc_sys_02").html(obj.uc_sys_02);
		    },
		    error:function(data){
		    	alert('error');
		    }
	    });
	}
	
	 function DoExp() {
		 $("#FormHtml").val($("#toolbar_div").html());
         var url = window.location.href;
         url = url + "&DoType=Exp";
     	$.ajax({
			cache: true,
			type: "POST",
	        url:"<%=basePath%>WF/Comm/ExpExel.do?EnsName=<%=csm.getEnsName()%>&DoType=Exp",
			data:$('#search_from').serialize(),
		    success: function(data) {
				 window.open(data);
		    },
		    error:function(data){
		    	alert('error');
		    }
	    });
    
	 }
    </script>
</body>
</html>