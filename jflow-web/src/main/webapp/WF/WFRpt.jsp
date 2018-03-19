<%@page import="cn.jflow.common.model.BaseModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="BP.Sys.SystemConfig"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="cn.jflow.common.model.WfrptModel"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	
	String Width = "0";
	String Height = "";
	String BtnWord = "";
	
	String FK_Flow = request.getParameter("FK_Flow");
	if (FK_Flow == null){
		out.print("FK_Flow 不能为空！");
		return;
	}
	
	int FK_Node=Integer.valueOf(request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node"));
	
	String DoType = request.getParameter("DoType")==null?"":request.getParameter("DoType");
	
	int StartNodeID = Integer.parseInt(FK_Flow+"01");
	
	String workId = request.getParameter("WorkID")==null?"0":request.getParameter("WorkID");
	long WorkID=Long.valueOf(workId);
	
	String cWorkId = request.getParameter("CWorkID")==null?"0":request.getParameter("CWorkID");
	long CWorkID=Long.valueOf(cWorkId);
	
	String nodeId = request.getParameter("NodeID")==null?"0":request.getParameter("NodeID");
	int NodeID=Integer.valueOf(nodeId);
	
	String fid = request.getParameter("FID")==null?"0":request.getParameter("FID");
	int FID=Integer.valueOf(fid);
	
	String MyPK = request.getParameter("MyPK")==null?"":request.getParameter("MyPK");
	if(StringHelper.isNullOrEmpty(MyPK)){
		MyPK = request.getParameter("PK")==null?"":request.getParameter("PK");
	}
	
	String CCID = request.getParameter("CCID")==null?"":request.getParameter("CCID");
	
	String ViewWork = request.getParameter("ViewWork")==null?"":request.getParameter("ViewWork");
	
	if("".equals(DoType) && "".equals(ViewWork)){
		//response.sendRedirect(basePath+"WF/WorkOpt/OneWork/Track.jsp?FK_Flow="+Dev2Interface.TurnFlowMarkToFlowNo(FK_Flow)+"&FK_Node="+FK_Node+ "&WorkID=" + WorkID);
		response.sendRedirect(basePath+"WF/WorkOpt/OneWork/OneWork.htm?FK_Flow="+Dev2Interface.TurnFlowMarkToFlowNo(FK_Flow)+"&FK_Node="+FK_Node+ "&WorkID=" + WorkID);
	}
	
	WfrptModel model = new WfrptModel(basePath, FK_Flow, FK_Node,DoType, StartNodeID, WorkID, CWorkID,
			 NodeID,  FID,  MyPK,  ViewWork,  Width,  Height,  BtnWord,  CCID, request,  response);
	model.init();
%>
<!DOCTYPE HTML>
<html>
<head>
<base target="_self">
<title><%=SystemConfig.getSysName() %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<style type="text/css">
.ActionType {
	width: 16px;
	height: 16px;
}
</style>
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/Table.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/Table0.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/FormThemes/MyFlow.css" />

<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/FormThemes/WinOpenEUI.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Scripts/easyUI/themes/icon.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Scripts/easyUI/themes/default/easyui.css"  />

<%
	int ccs_links_size = model.csslinks.size();
	for(int i = 0; i < ccs_links_size; i++){
		String ccsLink = model.csslinks.get(i);
		%>
		<link rel="stylesheet" type="text/css" href="<%=ccsLink%>" />
		<%
	}
%>

<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/easyloader.js" ></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/CCForm/MapExt.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/DataUser/PrintTools/LodopFuncs.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/SDKComponents/Base/SDKData.js"></script>

<%
	int script_size = model.scripts.size();
	for(int i = 0; i < script_size; i++){
		String script = model.scripts.get(i);
		%>
		<script type="text/javascript" src="<%=script%>"></script>
		<%
	}
%>

<script type="text/javascript">
function NoSubmit(ev) {
    //if (window.event.srcElement.tagName == "TEXTAREA")return true;

    if (ev.keyCode == 13) {
        window.event.keyCode = 9;
        ev.keyCode = 9;
        return true;
    }
    return true;
}
function chageURL() {
    var url = "<%=basePath %>/WF/WorkOpt/WebOffice.jsp?FK_Node=<%=FK_Node %>&FID=<%=FID %>&WorkID=<%=WorkID %>&FK_Flow=<%=FK_Flow %>";
    $("#officeIfream").attr("src", url);
}
function WinOpen(url, winName) {
    var newWindow = window.open(url, winName, 'width=700,height=400,top=100,left=300,scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
    newWindow.focus();
    return;
}

var LODOP; //声明为全局变量 
function printFrom() {
    var url = "<%=basePath %>WF/PrintSample.jsp?FK_Flow=<%=FK_Flow%>&FK_Node=<%=FK_Node %>&FID=<%=FID %>&WorkID=<%=WorkID %>&AtPara=";

    LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
    LODOP.PRINT_INIT("打印表单");
    // LODOP.ADD_PRINT_URL(30, 20, 746, "100%", location.href);
    LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", document.getElementById("divCCForm").innerHTML);
    // LODOP.ADD_PRINT_URL(0, 0, "100%", "100%", url);
    LODOP.SET_PRINT_STYLEA(0, "HOrient", 3);
    LODOP.SET_PRINT_STYLEA(0, "VOrient", 3);
    //		LODOP.SET_SHOW_MODE("MESSAGE_GETING_URL",""); //该语句隐藏进度条或修改提示信息
    //		LODOP.SET_SHOW_MODE("MESSAGE_PARSING_URL","");//该语句隐藏进度条或修改提示信息
    //  LODOP.PREVIEW();
    LODOP.PREVIEW();
    return false;
}

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

function ReinitIframe(frmID, tdID) {
	try {
			var iframe = document.getElementById(frmID);
			var tdF = document.getElementById(tdID);
			iframe.height = iframe.contentWindow.document.body.scrollHeight;
			iframe.width = iframe.contentWindow.document.body.scrollWidth;
			if (tdF.width < iframe.width) {
				tdF.width = iframe.width;
			} else {
				iframe.width = tdF.width;
			}
			tdF.height = iframe.height;
			return;
		} catch (ex) {
			return;
		}
		return;
}
</script>
</head>
<body class="am-collapse admin-sidebar-sub am-in" topmargin="0"
	leftmargin="0" onkeypress="NoSubmit(event);" style='width: 100%; height: 100%;background-color: white'>
	<div id="loading-mask" class="loddingMask" >
        <div id="pageloading"  class="pageloading">
            <img alt="" src="<%=basePath %>WF/Img/loading.gif" align="middle" />
            	请稍候...
        </div>
    </div>
	<form method="post"	action="" id="form1">
		<div id="mainPanel" region="center" border="true" border="false" class="mainPanel">
			<%--<div style="width: 0px; height: 0px">
    			<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width="0"  height="0">
        			<embed id="LODOP_EM" type="application/x-print-lodop" width="0" height="0" pluginspage="/DataUser/PrintTools/install_lodop32.exe"></embed>
    			</object>
			</div>
			 <div style="width: <%=model.getWidth() %>px; margin: 0 auto; background: white; text-align: left;">
			    <input type="button" name="Btn_Print" value="打印" onclick="return printFrom();" id="Btn_Print" class="Btn" />
			</div> --%>
			<div style="width: 100%; margin: 0 auto; background: white; border-top: 1px solid #4D77A7;">
			    <br />
			</div>
			<%=model.ui.ListToString() %>
		</div>
	</form>
</body>
</body>
<script type="text/javascript">
$(document).ready(function(){
	 $('#loading-mask').fadeOut();
	
	 var screenHeight = document.documentElement.clientHeight;
     var topBarHeight = 40;
     var allHeight = topBarHeight;
     if ("<%=BtnWord %>" == "2")
         allHeight = allHeight + 30;
     var frmHeight = "<%=Height %>";
     if (screenHeight > parseFloat(frmHeight) + allHeight) {
         $("#divCCForm").height(screenHeight - allHeight);
     }
});
</script>
</html>
