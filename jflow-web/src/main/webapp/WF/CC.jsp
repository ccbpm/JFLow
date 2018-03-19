<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%
	String FK_Flow = request.getParameter("FK_Flow")==null ? "":request.getParameter("FK_Flow");
	String DoType = request.getParameter("DoType");
	String GroupBy = request.getParameter("GroupBy") == null? "": request.getParameter("GroupBy");
	if (StringHelper.isNullOrEmpty(GroupBy)) {
		if ("CC".equals(DoType)) {
			GroupBy = "Rec";
		} else {
			GroupBy = "FlowName";
		}
	}
	String Sta = request.getParameter("Sta") == null ? "0" : request.getParameter("Sta");
%>
<script type="text/javascript">
function GroupBarClick(appPath, rowIdx) {
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
    var i = 0;
    for (i = 0; i <= 5000; i++) {
        if (document.getElementById(rowIdx + '_' + i) == null)
            continue;
        if (sta == 'block') {
            document.getElementById(rowIdx + '_' + i).style.display = '';
        } else {
            document.getElementById(rowIdx + '_' + i).style.display = sta;
        }
    }
}
function DoDelCC(mypk) {
    var url = '<%=basePath%>WF/Do.jsp?DoType=DelCC&MyPK=' + mypk;
    var v = window.showModalDialog(url, 'sd', 'dialogHeight: 10px; dialogWidth: 10px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
}
function WinOpenIt(appPath,ccid, fk_flow, fk_node, workid, fid, sta) {
    var url = '';
    if (sta == '0') {
        url = '<%=basePath%>WF/Do.jsp?DoType=DoOpenCC&FK_Flow=' + fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workid + '&FID=' + fid + '&Sta=' + sta + '&MyPK=' + ccid;
    }
    else {
        url = '<%=basePath%>WF/WorkOpt/OneWork/Track.jsp?FK_Flow=' + fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workid + '&FID=' + fid + '&Sta=' + sta + '&MyPK=' + ccid;
    }

    var newWindow = window.open(url, 'z', 'help:1;resizable:1;Width:680px;Height:420px');
    newWindow.focus();
    return;
}
function SetImg(appPath, id) {
    document.getElementById('I'+id).src ='<%=basePath%>WF/Img/CCSta/1.png';
}
</script>
</head>
<body>
	<!-- 表格数据 -->
	<table border=1px align=center width='100%'>
		<Caption ><div class='CaptionMsg' >抄送列表</div></Caption>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12" id="listbox">
				<jsp:include page="CCInfo.jsp">
					<jsp:param value="<%=FK_Flow %>" name="FK_Flow"/>
					<jsp:param value="<%=GroupBy %>" name="GroupBy"/>
					<jsp:param value="<%=Sta %>" name="Sta"/>
				</jsp:include>
			</div>
		</div>

	</table>
</body>
<script type="text/javascript">
	/* function reLoad(){
		window.location.reload(true);
		window.parent.frames["left"].location.reload(true);  
	}
	setInterval("reLoad()",3000); */
function reLoad(){
	var Sta = '<%=Sta%>';
	var FK_Flow = '<%=FK_Flow%>';
	var GroupBy = '<%=GroupBy%>';
	$.ajax({
		url : "CCS.do",
		type : 'post',
		dataType : 'html',
		data : {
			Sta : Sta,
			FK_Flow : FK_Flow,
			GroupBy : GroupBy
		},
		async : false,
		success : function(data) {
			$("#listbox").html(data);
		}
	});
}
setInterval("reLoad()",3000);
</script>
</html>