<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<link rel='stylesheet' type='text/css' href='<%=basePath%>WF/Comm/track/trackStyle.css'/>
<%
	TruakModel tm = new TruakModel(request, response);
	tm.init();
	String FK_Flow = request.getParameter("FK_Flow")==null?"0":request.getParameter("FK_Flow");
	String OID = request.getParameter("WorkID").toString();
	int oid = 0;
	String FID = request.getParameter("FID")==null?"0":request.getParameter("FID");
	int fid = 0;
	String FK_Node = request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node");
	int fk_node = 0;
	if (OID != null && OID.length() > 0) {
		oid = Integer.parseInt(OID.toString());
	}
	if (FID != null && FID.length() > 0) {
		fid = Integer.parseInt(FID);
	}
	if (FK_Node != null && FK_Node.length() > 0) {
		fk_node = Integer.parseInt(FK_Node);
	}
	AthModel am = new AthModel(request, response, basePath, FK_Flow,
			oid, fid, fk_node);
	am.init();
	String DoType = request.getParameter("DoType");
	String FK_Node1 = request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node");
	String FK_Flow1 = request.getParameter("FK_Flow")==null?"0":request.getParameter("FK_Flow");
	String WorkID = request.getParameter("WorkID")==null?"0":request.getParameter("WorkID");
	int workID = 0;
	String FID1 = request.getParameter("FID")==null?"0":request.getParameter("FID");
	int fid1 = 0;
	if (WorkID != null && WorkID.length() > 0) {
		workID = Integer.parseInt(WorkID);
	}
	if (FID != null && FID.length() > 0) {
		fid = Integer.parseInt(FID);
	}
	OpModel op = new OpModel(request, response, basePath, DoType,
			FK_Node1, FK_Flow1, workID, fid1);
	op.init();
%>
</head>
<script type="text/javascript">
	function onSilverlightError(sender, args) {
		var appSource = "";
		if (sender != null && sender != 0) {
			appSource = sender.getHost().Source;
		}

		var errorType = args.ErrorType;
		var iErrorCode = args.ErrorCode;

		if (errorType == "ImageError" || errorType == "MediaError") {
			return;
		}
		var errMsg = "Silverlight 应用程序中未处理的错误 " + appSource + "\n";

		errMsg += "代码: " + iErrorCode + "    \n";
		errMsg += "类别: " + errorType + "       \n";
		errMsg += "消息: " + args.ErrorMessage + "     \n";

		if (errorType == "ParserError") {
			errMsg += "文件: " + args.xamlFile + "     \n";
			errMsg += "行: " + args.lineNumber + "     \n";
			errMsg += "位置: " + args.charPosition + "     \n";
		} else if (errorType == "RuntimeError") {
			if (args.lineNumber != 0) {
				errMsg += "行: " + args.lineNumber + "     \n";
				errMsg += "位置: " + args.charPosition + "     \n";
			}
			errMsg += "方法名称: " + args.methodName + "     \n";
		}
		alert(errMsg);
	}

	function appLoad() {
		var xamlObject = document.getElementById("silverlightControl");
		if (xamlObject != null) {
		}
	}
</script>
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
	function DoFunc(doType, workid, fk_flow, fk_node) {

		if (doType == 'Del' || doType == 'Reset') {
			if (confirm('您确定要执行吗？') == false)
				return;
		}

		var url = '';
		if (doType == 'HungUp' || doType == 'UnHungUp') {
			url = './../HungUpOp.jsp?WorkID=' + workid + '&FK_Flow=' + fk_flow
					+ '&FK_Node=' + fk_node;
			var str = window
					.showModalDialog(url, '',
							'dialogHeight: 350px; dialogWidth:500px;center: no; help: no');
			if (str == undefined)
				return;
			if (str == null)
				return;
			//this.close();
			window.location.href = window.location.href;
			return;
		}
		url = '<%=basePath%>WF/WorkOpt/OneWork/OP.jsp?DoType=' + doType + '&WorkID=' + workid + '&FK_Flow='
				+ fk_flow + '&FK_Node=' + fk_node;
		window.location.href = url;
	}
	function Takeback(workid, fk_flow, fk_node, toNode) {
		if (confirm('您确定要执行吗？') == false)
			return;
		var url = '../../GetTask.jsp?DoType=Tackback&FK_Flow=' + fk_flow
				+ '&FK_Node=' + fk_node + '&ToNode=' + toNode + '&WorkID='
				+ workid;
		window.location.href = url;
	}
	function UnSend(fk_flow, workID, fid) {

//            var url = "CancelWork.aspx?WorkID=" + workID + "&FK_Flow=" + fk_flow+"&FID="+fid+"&FK_Node="+"";
//            WinShowModalDialog_Accepter(url);

		if (confirm('您确定要执行撤销吗?') == false)
			return;

		<%-- var url = "<%=basePath%>WF/WorkOpt/OneWork/OP.jsp?DoType=UnSend&FK_Node=<%=FK_Node%>&FK_Flow=" + fk_flow
 				+ "&WorkID=" + workID + "&FID=" + fid; --%>
// 		$.post(url, null, function(msg) {
// 			$('#winMsg').html(msg);
// 			$('#winMsg').window('open');
// 		});
		var url = "<%=basePath%>WF/Do.jsp?DoType=UnSend&FK_Flow=" + fk_flow
			+ "&WorkID=" + workID + "&FID=" + fid+"&PageID=002";
		var v = window.showModalDialog(url, 'sd', 'dialogHeight: 300px; dialogWidth: 700px;center: yes; help: no');
	}
</script>
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
			document.getElementById('ImgUpDown').src = "../Img/arrow_down.gif";
		} else {
			document.getElementById('ImgUpDown').src = "../Img/arrow_up.gif";
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
		document.getElementById('Img' + rowIdx).src = '../Img/' + alt + '.gif';
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

	$(document).ready(function() {
		$("table.Table tr:gt(0)").hover(function() {
			$(this).addClass("tr_hover");
		}, function() {
			$(this).removeClass("tr_hover");
		});
		<%-- $('#tab1').load('<%=basePath %>WF/Admin/CCFlowDesigner/Truck.html?FID=<%=fid %>&FK_Flow=<%=FK_Flow %>&WorkID=<%=WorkID %>'); --%>
		showDiv('2');
	});

	function WinOpen(url, winName) {
		var newWindow = window
				.open(url,winName,'height=800,width=1030,top=' + (window.screen.availHeight - 800) / 2
						+ ',left=' + (window.screen.availWidth - 1030) / 2
						+ ',scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
		newWindow.focus();
		return;
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
	
	
	function showDiv(v){
		if(v==1){
			 $("#d1").addClass("selected");  
			 $("#d2").removeClass("selected");  
			 $("#lcrz_div").css("display","none");  
			 $("#d3").removeClass("selected");  
			 $("#d4").removeClass("selected");  
			 var adminUrl='<%=basePath %>WF/Admin/CCFlowDesigner/Truck.html?FID=<%=fid %>&FK_Flow=<%=FK_Flow %>&WorkID=<%=WorkID %>';
			 $('#mainFrame').attr("src", adminUrl);
			 $("#mainFrame").height(document.body.scrollHeight-50);
		}
		else if(v==2){
			 $('#mainFrame').attr("src",'');
			 $("#d2").addClass("selected");  
			 $("#lcrz_div").css("display","block");
			 $("#d1").removeClass("selected");  
			 $("#d3").removeClass("selected");  
			 $("#d4").removeClass("selected");
		}
		else if(v==3){
			 $("#d2").removeClass("selected");  
			 $("#lcrz_div").css("display","none");
			 $("#d1").removeClass("selected");  
			 $("#d3").addClass("selected");  
			 $("#d4").removeClass("selected");
			 var adminUrl='<%=basePath %>WF/WorkOpt/OneWork/Ath.jsp?FK_Node=<%=FK_Node %>&WorkID=<%=WorkID %>&FK_Flow=<%=FK_Flow %>&FID=<%=fid %>';
			 $('#mainFrame').attr("src", adminUrl);
			 $("#mainFrame").height(document.body.scrollHeight-50);
		}
		else if(v==4){
			 $("#d2").removeClass("selected");  
			 $("#lcrz_div").css("display","none");
			 $("#d1").removeClass("selected");  
			 $("#d3").removeClass("selected");  
			 $("#d4").addClass("selected");  
			 var adminUrl='<%=basePath %>WF/WorkOpt/OneWork/OP.jsp?FK_Node=<%=FK_Node%>&WorkID=<%=WorkID %>&FK_Flow=<%=FK_Flow %>&FID=<%=fid %>';
			 $('#mainFrame').attr("src", adminUrl);
			 $("#mainFrame").height(document.body.scrollHeight-50);
		}
		else if(v==5){ 
			 var adminUrl='<%=basePath %>WF/WorkOpt/OneWork/FlowBBS.htm?FK_Node=<%=FK_Node%>&WorkID=<%=WorkID %>&FK_Flow=<%=FK_Flow %>&FID=<%=fid %>';
			 WinOpen(adminUrl);
		}
	}
</script>
<script type="text/javascript">
    window.onload = function () {
        var op = $("#ContentPlaceHolder1_TruakUC1_HiddenField1").val();
        $('#flowNote').append(op);

        $(".main .year .list").each(function (e, target) {
            var $target = $(target),
	        $ul = $target.find("ul");
            $target.height($ul.outerHeight()), $ul.css("position", "absolute");
        });
        $(".main .year>h2>a").click(function (e) {
            e.preventDefault();
            $(this).parents(".year").toggleClass("close");
        });
    }
</script>
<body class="easyui-layout">
	<div data-options="region:'north',split:false,noheader:true,border:false" style="height: 35px;overflow-y:hidden;">
        <ul class="hornavlist">
            <li><div id="d1"><a href="javascript:showDiv('1');" ><span class='nav'>轨迹图</span></a></div>
			</li> 	
			<li><div id="d2"><a href="javascript:showDiv('2');" ><span class='nav'>流程日志</span></a></div>
			</li> 	
			<li><div id="d3"><a href="javascript:showDiv('3');" ><span class='nav'>流程附件</span></a></div>
			</li> 	
			<li><div id="d4"><a href="javascript:showDiv('4');" ><span class='nav'>操作</span></a></div>
			</li> 	
			<li><div id="d4"><a href="javascript:showDiv('5');" ><span class='nav'>流程评论</span></a></div>
			</li> 	
        </ul>
    </div>
    <div data-options="region:'center',noheader:true" style="overflow-y:auto;">
        <div class="easyui-layout" data-options="fit:true" style="overflow-y:auto;">
        
	        <div id="aa" data-options="region:'center'" style="padding: 5px;border: 0;">
		        <iframe id="mainFrame" name="mainFrame" style="overflow:visible;"
					scrolling="yes" frameborder="no" width="100%" ></iframe>
	      	</div>
	      
	        <!-- 流程日志 -->
	        <div data-options="region:'center',fit:true" id="flowNote" >
	            <div id="lcrz_div" style="display: none;padding-left:20%; vertical-align: top; height:inherit; overflow: auto;">
					<%=tm.Pub1.toString()%>
					<%=tm.UCEn1.Pub.toString()%>
					<div style="clear:both;"></div>
				</div>
	        </div> 
	    </div>
    </div>

		<%--   <!--  <!-- 轨迹图 
           <div id="lcgjt_div" style="display: none;"><iframe id="mainFrame" name="mainFrame" style="overflow:visible;"
			scrolling="yes" frameborder="no" width="100%" ></iframe></div> -->
			
			<!-- 流程附件 -->
			<div id="lcfj_div" style="display: none;">
				<%=am.Pub1.toString()%>
			</div>
			
			<!-- 操作 -->
			<div id="caozuo_div" style="display: none;">
				<%=op.Pub2.toString()%>
			</div> --%>
</body>
</html>