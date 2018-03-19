<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ include file="/WF/head/head2.jsp"%> --%>
<%@page import="cn.jflow.common.app.*"%>
<%@page import="cn.jflow.common.model.*"%>
<%@page import="cn.jflow.model.designer.*"%>
<%@page import="cn.jflow.controller.wf.workopt.AllotTaskController" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	EnModel enModel = new EnModel(request, response);
	enModel.loadMyFlowEn();
%>
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>WF/Style/style_oss.css" />
<script type="text/javascript" src="<%=basePath%>WF/Scripts/main.js"></script>
<script type="text/javascript" src="<%=basePath%>DataUser/PrintTools/LodopFuncs.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/SDKComponents/Base/SDKData.js"></script>
<%
	int script_size = enModel.getScripts().size();
	for(int i = 0; i < script_size; i++){
		String script = enModel.getScripts().get(i);
		%>
		<script type="text/javascript" src="<%=basePath+script%>"></script>
		<%
	}
%>
<%
	int ccs_links_size = enModel.getCCSLinks().size();
	for(int i = 0; i < ccs_links_size; i++){
		String ccsLink = enModel.getCCSLinks().get(i);
		%>
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath+ccsLink%>" />
		<%
	}
%>
<script type="text/javascript">
	var kvs = null;
	function GenerPageKVs() {
	    var ddls = null;
	    ddls = parent.document.getElementsByTagName("select");
	    kvs = "";
	    for (var i = 0; i < ddls.length; i++) {
	        var id = ddls[i].name;
	
	        if (id.indexOf('DDL_') == -1) {
	            continue;
	        }
	        var myid = id.substring(id.indexOf('DDL_') + 4);
	        kvs += '~' + myid + '=' + ddls[i].value;
	    }
	
	    ddls = document.getElementsByTagName("select");
	    for (var i = 0; i < ddls.length; i++) {
	        var id = ddls[i].name;
	
	        if (id.indexOf('DDL_') == -1) {
	            continue;
	        }
	        var myid = id.substring(id.indexOf('DDL_') + 4);
	        kvs += '~' + myid + '=' + ddls[i].value;
	    }
	    return kvs;
	}
	
	function UploadChange(btn) {
	    document.getElementById(btn).click();
	}
	
	function HidShowSta() {
	    if (document.getElementById('RptTable').style.display == "none") {
	        document.getElementById('RptTable').style.display = "block";
	        document.getElementById('ImgUpDown').src = "<%=basePath%>WF/Img/ArrDown.gif";
	    }
	    else {
	        document.getElementById('ImgUpDown').src = "<%=basePath%>WF/Img/AttUp.gif";
	        document.getElementById('RptTable').style.display = "none";
	    }
	}
	function GroupBarClick(rowIdx) {
	    var alt = document.getElementById('Img' + rowIdx).alert;
	    var sta = 'block';
	    if (alt == 'Max') {
	        sta = 'block';
	        alt = 'Min';
	    } else {
	        sta = 'none';
	        alt = 'Max';
	    }
	    document.getElementById('Img' + rowIdx).src = '<%=basePath%>WF/Img/' + alt + '.gif';
	    document.getElementById('Img' + rowIdx).alert = alt;
	    var i = 0
	    for (i = 0; i <= 40; i++) {
	        if (document.getElementById(rowIdx + '_' + i) == null)
	            continue;
	        if (sta == 'block') {
	            document.getElementById(rowIdx + '_' + i).style.display = '';
	        } else {
	            document.getElementById(rowIdx + '_' + i).style.display = sta;
	        }
	    }
	}
	function NoSubmit(ev) {
	    if (window.event.srcElement.tagName == "TEXTAREA")
	        return true;
	
	    if (ev.keyCode == 13) {
	        window.event.keyCode = 9;
	        // alert(' code=: ' + ev.keyCode + ' tagName:' + window.event.srcElement.tagName);
	        ev.keyCode = 9;
	        // alert('ok');
	        //alert(ev.keyCode);
	        return true;
	        //                event.keyCode = 9;
	        //                ev.keyCode = 9;
	        //                window.event.keyCode = 9;
	    }
	    return true;
	}
	function TBHelp(id,basePath,enName,KeyOfEn){
		var newWindow = window.showModalDialog(basePath+'WF/Comm/HelperOfTBEUI.jsp?AttrKey='+KeyOfEn+'&WordsSort=3&FK_MapData='+enName+'&id='+id+'', '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
		if(newWindow==null||newWindow==''||newWindow=='undefined')return;
		document.getElementById(id).value = newWindow;
	  	 return;
	}
	function TBHelp(ctrl, enName) {
        //alert(ctrl + "-" + enName);
        var explorer = window.navigator.userAgent;
        var str = "";
        var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName + "&AttrKey=" + ctrl + "&WordsSort=0" + "&FK_MapData=" + enName + "&id=" + ctrl;
        if (explorer.indexOf("Chrome") >= 0) {
            window.open(url, "sd", "left=200,height=500,top=150,width=600,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no");
        }
        else {
            str = window.showModalDialog(url, 'sd', 'dialogHeight: 500px; dialogWidth:600px; dialogTop: 150px; dialogLeft: 200px; center: no; help: no');
            if (str == undefined)
                return;
            ctrl = ctrl.replace("WorkCheck", "TB");
            $("*[id$=" + ctrl + "]").focus().val(str);
        }
    }
</script>
<%=enModel.scriptsBlock.toString() %>
</head>

<body>
	<%=enModel.Pub.toString().replace(".aspx", ".jsp") %>
</body>
</html>