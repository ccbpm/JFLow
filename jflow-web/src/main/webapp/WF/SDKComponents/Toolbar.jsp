<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="BP.WF.NodeFormType"%>
<%@page import="BP.WF.BatchRole"%>
<%@page import="BP.WF.ActionType"%>
<%@page import="BP.WF.Template.NodeToolbar"%>
<%@page import="BP.WF.Template.NodeToolbarAttr"%>
<%@page import="BP.WF.Template.NodeToolbars"%>
<%@page import="BP.WF.Template.ShowWhere"%>
<%@page import="BP.WF.Flow"%>
<%@page import="BP.WF.PrintDocEnable"%>
<%@page import="BP.WF.CCRole"%>
<%@page import="BP.WF.WFState"%>
<%@page import="BP.WF.Template.BtnLab"%>
<%@page import="java.util.Date"%>
<%@page import="BP.WF.Entity.*"%>
<%@page import="BP.WF.Data.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.Tools.*"%>
<%@page import="cn.jflow.common.model.MyFlowModel"%>
<%
String ccflowAppPath = BP.WF.Glo.getCCFlowAppPath();
 
 int fk_node = Integer.valueOf(request.getParameter("FK_Node"));
long workID = Long.valueOf(request.getParameter("WorkID"));
long fid = Long.valueOf(request.getParameter("FID"));
String fk_flow = request.getParameter("FK_Flow");
String isToolbar = request.getParameter("IsToobar");

boolean toolbar = true;
if (!StringHelper.isNullOrEmpty(isToolbar) && isToolbar == "0")
    toolbar = false;

String paras = request.getParameter("AtPara");

boolean isCC = false;
if (paras != null && paras.contains("IsCC=1"))
    isCC = true;

paras = request.getParameter("Paras");
if (paras != null && paras.contains("IsCC=1"))
    isCC = true;

BtnLab btn = new BtnLab(fk_node);
BP.WF.Node node = new BP.WF.Node(fk_node);

BP.WF.GenerWorkFlow gwf = new BP.WF.GenerWorkFlow();
gwf.setWorkID(workID);
gwf.RetrieveFromDBSources();

WFState workState = gwf.getWFState();

String msg = null;
boolean isInfo = false;

MyFlowModel myFlow = new MyFlowModel(request, response,true);
myFlow.initToolbarToJsp();

%>
<script type="text/javascript" language="javascript" src="<%=ccflowAppPath%>WF/SDKComponents/Base/SDKData.js"></script>
<script type="text/javascript" language="javascript" src="<%=ccflowAppPath%>WF/DataUser/PrintTools/LodopFuncs.js"></script>
<script type="text/javascript">
	//初始化按钮样式，主要用于第三方项目集成
	$(document).ready(function(){
		$('input[type=button]').attr("class","btn btn-primary");
		$('input[type=button]').css("margin-left","5px");
		$('input[type=button]').css("margin-right","5px");
	}); 
	
	// 退回，获取配置的退回信息的字段.
	function ReturnWork(url, field) {
	    var urlTemp;
	    if (field == '' || field == null) {
	        urlTemp = url;
	    }
	    else {
	        urlTemp = url + '&Info=' + ReqTB(field);
	    }
	    OpenUrlLocation(urlTemp, "退回页面")
	}

	
	
    function ShowUrl(obj) {

        var strTimeKey = "";
        var date = new Date();
        strTimeKey += date.getFullYear(); //年
        strTimeKey += date.getMonth() + 1; //月 月比实际月份要少1
        strTimeKey += date.getDate(); //日
        strTimeKey += date.getHours(); //HH
        strTimeKey += date.getMinutes(); //MM
        strTimeKey += date.getSeconds(); //SS
        
        var btnID = obj.id;

         if (btnID == 'Btn_Return') {
             OpenUrlLocation('<%=ccflowAppPath%>WF/WorkOpt/ReturnWork.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Track') {
             WinOpen('<%=ccflowAppPath%>WF/WorkOpt/OneWork/Track.jsp?FID=<%=fid%>&FK_Flow=<%=fk_flow%>&WorkID=<%=workID%>','流程轨迹',800,500);
             return;
         }

         if (btnID == 'Btn_SelectAccepter') {
             OpenUrlNewWindows('<%=ccflowAppPath%>/WF/WorkOpt/Accepter.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Askfor') {
             OpenUrlLocation( '<%=ccflowAppPath%>WF/WorkOpt/Askfor.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Shift') {
             OpenUrlLocation('<%=ccflowAppPath%>WF/WorkOpt/Forward.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_CC') {//Btn_CC
             WinOpen( '<%=ccflowAppPath%>WF/WorkOpt/CC.jsp?1=2&FID=<%=fid%>&WorkID=<%=workID%>&FK_Node=<%=fk_node%>&FK_Flow=<%=fk_flow%>&s=<%=new Date()%>', '抄送', 800, 650);
             return;
         }

         if (btnID == 'Btn_Delete') {
              OpenUrlLocation('<%=ccflowAppPath%>WF/DeleteWorkFlow.jsp?1=2');
             return;
         }

     if (btnID == 'Btn_CheckNote') {
         OpenUrlLocation('<%=ccflowAppPath%>WF/WorkOpt/CCCheckNote.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Office') {//Btn_Office
            WinOpen( '<%=ccflowAppPath%>WF/WorkOpt/WebOffice.jsp?1=2&FID=<%=fid%>&WorkID=<%=workID%>&FK_Node=<%=fk_node%>&FK_Flow=<%=fk_flow%>&s=<%=new Date()%>', '公文正文', 800, 650);
             return;
         }

         if (btnID == 'Btn_Read') {
            Application.data.ReadCC("<%=node.getNodeID()%>", "<%=workID%>",ReadCCResult, this);
		}

		if (btnID == 'Btn_Print') {
			printFrom();
			return;
		}
	}
	function DeleteResult(json) {

	}
	function ReadCCResult(json) {
		if (json != "true") {
			alert('已阅失败!');
		} else {
			alert('已阅成功!');
			window.opener.document.location.reload();
			window.close();
		}
	}
	
	 //按钮.
    function FocusBtn(btn, workid) {
        if (btn.value == '关注') {
            btn.value = '取消关注';
        }
        else {
            btn.value = '关注';
        }
        $.ajax({ url: "<%=ccflowAppPath%>WF/Do.jsp?ActionType=Focus&WorkID=" + workid, async: false });
    }


	var LODOP;
	function printFrom() {
		LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
		LODOP.PRINT_INIT("打印表单");
		LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", document.getElementById("divCCForm").innerHTML);
		LODOP.SET_PRINT_STYLEA(0, "HOrient", 3);
		LODOP.SET_PRINT_STYLEA(0, "VOrient", 3);
		LODOP.PREVIEW();

	}

	function ShowFlowMessage() {
		
	}
	
	function WinOpen(url, winName, width, height) {

		// 生成参数.
		_GetParas();

		// 把IsEUI处理一下，让对方的功能界面接收到此参数进行个性化处理.
		url = url + _paras + '&IsEUI=1';
		var newWindow = window.open( url,winName,'width=' + width + ',height=' + height 
			+ ',top=100,left=300,scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
		newWindow.focus();
		return;
	}

	function DelCase() {
		var strTimeKey = "";
		var date = new Date();
		strTimeKey += date.getFullYear(); //年
		strTimeKey += date.getMonth() + 1; //月 月比实际月份要少1
		strTimeKey += date.getDate(); //日
		strTimeKey += date.getHours(); //HH
		strTimeKey += date.getMinutes(); //MM
		strTimeKey += date.getSeconds(); //SS

		Application.data.delcase(<%=fk_flow%>, <%=fk_node%>, <%=workID%>, "", function(js) {
			if (js) {
				var str = js;
				if (str == "删除成功") {
					$.messager.alert('提示', '删除成功！');
				} else {
					$.messager.alert('提示', str);
				}
			}
		}, this);
	}
	function OpenUrlLocation(url) {
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		
		window.location.href = url;
	}
	
	function OpenUrlNewWindows(url){
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		WinOpen(url,'接受人',800,500);
		}
	
	function showModelDialog(url){
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		window.name = "dialogPage";
		window.showModalDialog(url, "dialogPage");
	}
	function OpenUrl(divID, title, url, w, h) {

		// 生成参数.
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		<%
        Object sdkwinopentype = BP.Sys.SystemConfig.getAppSettings().get("SDKWinOpenType");
        if (sdkwinopentype!=null && sdkwinopentype.equals("1"))
           {%>
	// 把IsEUI处理一下，让对方的功能界面接收到此参数进行个性化处理.
		try {
			window.parent.OpenJboxIfream(title, url, w, h);
		} catch (e) {
			OpenWindow(url, title, w, h);
		}
	<%} else {%>
		OpenWindow(url, title, w, h);
	<%}%>
	}

	$(function() {
		var html = $('#flowMessage').text();
		if (html != "" && html != null && html.length > 6) {
			ShowFlowMessage();
		}
	});

	function OpenWindow(url, title, w, h) {
	}

	var _paras = "";
	function _GetParas() {
		_paras = "";
		//获取其他参数
		var sHref = window.location.href;
		var args = sHref.split("?");
		var retval = "";
		if (args[0] != sHref) /*参数不为空*/
		{
			var str = args[1];
			args = str.split("&");
			for (var i = 0; i < args.length; i++) {
				str = args[i];
				var arg = str.split("=");
				if (arg.length <= 1)
					continue;

				//不包含就添加
				if (_paras.indexOf(arg[0]) == -1) {
					// xiaozhoupeng add 20150121 Start 解决公文获取wrokID=0的问题
					var value = 0;
					if ("WorkID" == arg[0]) {
						value = <%=workID%>
					} else if ("FID" == arg[0]) {
						value = <%=fid%>
					} else {
						value = arg[1];
					}
					_paras += "&" + arg[0] + "=" + value;
					if (_paras.indexOf('WorkID') == -1) {
						_paras += "&WorkID=" +
						<%=workID%> ;
					}
				}
			}
		}
	}

	function closeWin() {
		if (window.dialogArguments && window.dialogArguments.window) {
			window.dialogArguments.window.location = window.dialogArguments.window.location;
		}
		if (window.opener) {
			if (window.opener.name && window.opener.name == "main") {
				window.opener.location.href = window.opener.location.href;
				window.opener.top.leftFrame.location.href = window.opener.top.leftFrame.location.href;
			}
		}
		window.close();
	}
</script>

<%= myFlow.getToolbar().get_content() %>

<%-- <%
	if (toolbar) {
		
%>
<%
	if (!isCC && workState != WFState.Complete) {
%>
<%
if(node.getHisFormType() == NodeFormType.SelfForm){
	String sendJs = "";
	  //如果是自定义表单.
	if (node.getIsEndNode())
	{
		
		//如果当前节点是结束节点.
		if (btn.getSendEnable() && node.getHisBatchRole() != BatchRole.Group)
		{
			//如果启用了发送按钮.
			// 获取发送的JS
			sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
			%>
			<!--发送-->
			<input type="button" onclick="<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>"  />
			<%
		}
	}else{
			if (btn.getSendEnable() && node.getHisBatchRole() != BatchRole.Group)
			{
				//如果启用了发送按钮.
				if (btn.getSelectAccepterEnable() == 2)
				{
					//如果启用了选择人窗口的模式是【选择既发送】.
					%>
					<!--接受人-->
					<input type="button" onclick="javascript:OpenSelectAccepter('<%=fk_flow %>','<%=fk_node %>','<%=workID %>','<%=fid %>')" id="button" value="接受人" />
					<%
					if (node.getHisFormType() == NodeFormType.DisableIt)
					{
						sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+" Send();";
					}
					else
					{
						sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
					}
					%>
					<!--发送-->
					<input type="button" onclick="<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" />
					<%
				}
				else
				{
					if (btn.getSendJS().trim().length() > 2)
					{
						sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
					}
					else
					{
						if (node.getHisFormType() == NodeFormType.DisableIt) {
							//this.Btn_Send.OnClientClick = "this.disabled=true;"; //this.disabled='disabled'; return true;";
							sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+" Send();";
						}
						else
						{
							sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
						}
					}
					
					%>
					<!--发送-->
					<input type="button" onclick="<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" />
					<%
				}
		}
		//处理保存按钮.
		if (btn.getSaveEnable()) {
		%>
		<input type="button" onclick="SaveSelfFrom();" id="Btn_Save"  value='<%=btn.getSaveLab()%>' />
		<%
		}
	}
}else{
	if (workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
		// 获取发送的JS
		String sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+" Send();";
%>
<!--发送-->
<input type="button" onclick="if(SysCheckFrm()==false) return false;<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" class="am-btn am-btn-primary am-btn-xs" />
<input type="button" onclick="Send();" id="Btn_Send" value="<%=btn.getSendLab()%>"  />
<%
	}
%>
<!-- 保存-->
<%
	if (btn.getSaveEnable() && workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
<input type="button" onclick="if(SysCheckFrm()==false) return false;Save()" id="Btn_Save" 
	value='<%=btn.getSaveLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<input type="button" onclick="Save()" id="Btn_Save" 
	value='<%=btn.getSaveLab()%>' />
<%
	}
}
%>
<!-- 退回-->
<%
	if (!node.getIsStartNode() && btn.getReturnEnable() && workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Return"
	name="Btn_Return" value='<%=btn.getReturnLab()%>' />
<%
	}
%>
<!-- 接受人-->
<%
	if (btn.getSelectAccepterEnable() != 0 && btn.getSelectAccepterEnable() != 2
					&& workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
	<input type="button" onclick="ShowUrl(this)" id="Btn_SelectAccepter" value='<%=btn.getSelectAccepterLab()%>' />
<%
	}
%>
<!-- 移交-->

	// 如果不是退回类型的开始节点就不显示移交按钮
	if(node.getIsStartNode() && workState == WFState.ReturnSta){
		
		<input type="button" onclick="ShowUrl(this)" id="Btn_Shift" value='<%=btn.getShiftLab()%>' class='am-btn am-btn-primary am-btn-xs' />
		<%
	}else 
		<%	if (btn.getShiftEnable() && workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp
					/* && !node.getIsStartNode() */) {
%>
		<input type="button" onclick="ShowUrl(this)" id="Btn_Shift" value='<%=btn.getShiftLab()%>' />
<%
	}
%>
<!-- 删除-->
<%
		if (btn.getDeleteEnable() != 0
					&& workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
			<input type="button" onclick="ShowUrl(this)" id="Btn_Delete" value='<%=btn.getDeleteLab()%>'  />
<%
		}
%>
<!-- 加签-->
<%
		if (btn.getAskforEnable() && workState != WFState.Complete && workState != WFState.Fix && workState != WFState.HungUp) {
%>
			<input type="button" onclick="ShowUrl(this)" id="Btn_Askfor" value='<%=btn.getAskforLab()%>' />
<!-- 抄送-->
<%
		}
	if (btn.getCCRole().getValue() == CCRole.HandAndAuto.getValue() || btn.getCCRole().getValue() == CCRole.HandCC.getValue()) {
		if (workState != WFState.Complete && workState != WFState.Fix && workState != WFState.HungUp) {
%>
			<input type="button" onclick="ShowUrl(this)" id="Btn_CC" value='<%=btn.getCCLab()%>'/>
<%
		}

	}
%>
<!-- 查询 -->
<%

if (btn.getSearchEnable()){
	%>
    <input type="button" value="<%=btn.getSearchLab()%>" enable=true onclick="WinOpen('<%=ccflowAppPath%>WF/Rpt/Search.jsp?RptNo=ND<%=Integer.parseInt(fk_flow)%>MyRpt&FK_Flow=<%=fk_flow %>','dsd0')" />
<%
}
	} else {
			/* 如果是抄送. */
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Read" value="已阅" />
<%
	FrmWorkCheck fwc = new FrmWorkCheck(fk_node);
			if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable) {
				/*如果没有启用,就显示出来可以审核的窗口. */
				String url = "";
%>
<input type="button" value='填写审核意见' id="Btn_CheckNote"
	onclick="ShowUrl(this)"  />
<%
	}
		}
%>

<!-- 轨迹-->
<%
	if (btn.getTrackEnable()) {
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Track"
	value='<%=btn.getTrackLab()%>' />
<%
	}
%>
<%
	}
%>
<!-- 打印-->
<%
	if (btn.getPrintDocEnable()) {
		// xiaozhoupeng 20150122 add Start 打印rtf单据模板
		if (node.getHisPrintDocEnable().ordinal() == PrintDocEnable.PrintRTF
				.ordinal()) {
			String urlr = ccflowAppPath + "WF/WorkOpt/PrintDoc.jsp?FK_Node="
					+ fk_node + "&FID=" + fid + "&WorkID=" + workID
					+ "&FK_Flow=" + fk_flow + "&s="
					+ DataType.dateToStr(new Date(), "yyMMddhhmmss");
%>
<input type="button" 
	value="<%=btn.getPrintDocLab()%>" enable=true
	onclick="WinOpen('<%=urlr%>','dsdd');" />
<%
	}
		// xiaozhoupeng 20150122 add End
		if (node.getHisPrintDocEnable().ordinal() == PrintDocEnable.PrintHtml
				.ordinal()) {
%>
<object id="LODOP_OB"
	classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width="0"
	height="0">
	<embed id="LODOP_EM" type="application/x-print-lodop" width="0"
		height="0"
		pluginspage="<%=ccflowAppPath%>DataUser/PrintTools/install_lodop32.exe"></embed>
</object>
<input type="button" onclick="ShowUrl(this)" id="Btn_Print"
	value='<%=btn.getPrintDocLab()%>' />
<%
	}
%>
<%
	}
%>
<!-- 公文-->
<%
	if (btn.getWebOfficeEnable() == 1) {
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Office"
	value='<%=btn.getWebOfficeLab()%>' />
<%
	}
%>
<!-- 关注 -->
<%
	if (btn.getFocusEnable() == true) {
		
		 if (gwf.getParas_Focus() == true){
%>
	<input type=button  value='取消关注' enable=true onclick="FocusBtn(this,'<%=workID %>'); " />
<%
		 }else{
%>	
<input type=button  value='<%=btn.getFocusLab() %>' enable=true onclick="FocusBtn(this,'<%=workID %>');" />
<%
		}
	}
%> --%>




<%
	//加载自定义的button.
	NodeToolbars bars = new NodeToolbars();
	bars.Retrieve(NodeToolbarAttr.FK_Node, fk_node);
	for (NodeToolbar bar : bars.ToJavaList()) {
		
		if (bar.getShowWhere() == ShowWhere.Toolbar){
			StringBuilder urlr3 = new StringBuilder();
			urlr3.append(bar.getUrl()).append("?1=2").append("&FK_Node=").append(fk_node).append("&FID=").append(fid).append("&WorkID=").append(workID).append("&FK_Flow=").append(fk_flow);
			%>
			<input type="button"  value="<%=bar.getTitle() %>" enable=true onclick="WinOpen('<%=urlr3.toString()%>');" />
			<%
		}
	}
%>
<%
try {
	%>
		 <div>
			<%
				// WorkFlow workFlow = new WorkFlow(node.getFK_Flow(), workID);
				if (workState != WFState.Complete) {
					switch (workState) {
					
					case AskForReplay: // 返回加签的信息.
					%><h2>加签信息:</h2><%
					String mysql = "SELECT * FROM ND"
							+ Integer.valueOf(node.getFK_Flow())
							+ "Track WHERE WorkID=" + workID + " AND "
							+ TrackAttr.ActionType + "="
							+ ActionType.ForwardAskfor.ordinal();
					DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(mysql);
					for (DataRow dr : mydt.Rows) {
						String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
						String worker = dr.getValue(TrackAttr.EmpFrom).toString();
						String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
						String rdt = dr.getValue(TrackAttr.RDT).toString();
						%>
						<div>
						--------------<%=workerName %>在<%=rdt %>加签--------------<hr/> 
						内容：<br/>
						<%=BP.DA.DataType.ParseText2Html(msgAskFor) %><br/><hr/>
						</div>
						<%
						isInfo = true;
					}
						break;
						/**
						修改为form显示。此加签暂时注释
					case Askfor: //加签.
						%><h2>加签信息:</h2><%
						String sql = "SELECT * FROM ND"
								+ Integer.valueOf(node.getFK_Flow())
								+ "Track WHERE WorkID=" + workID + " AND "
								+ TrackAttr.ActionType + "="
								+ ActionType.AskforHelp.getValue();
						DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						for (DataRow dr : dt.Rows) {
							String msgAskFor = dr.getValue(TrackAttr.Msg.toLowerCase()).toString();
							String worker = dr.getValue(TrackAttr.EmpFrom.toLowerCase()).toString();
							String workerName = dr.getValue(TrackAttr.EmpFromT.toLowerCase()).toString();
							String rdt = dr.getValue(TrackAttr.RDT.toLowerCase()).toString();
							%>
							<div>
							--------------<%=workerName %>在<%=rdt %>加签--------------<hr/> 
							内容：<br/>
							<%=BP.DA.DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + "<br> --<a href='./WorkOpt/AskForRe.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid + "' >回复加签意见</a> --"%><br/><hr/>
							</div>
							<%
							isInfo = true;
						 }
						break;
						*/
					case ReturnSta:
						%>
						<h2>退回信息:</h2>
						<%
						/* 如果工作节点退回了*/
						ReturnWorks rws = new ReturnWorks();
						rws.Retrieve(ReturnWorkAttr.ReturnToNode, fk_node,ReturnWorkAttr.WorkID, workID, ReturnWorkAttr.RDT);
						if (rws.size() != 0) {
							String msgInfo = "";
							for (ReturnWork rw : ReturnWorks.convertReturnWorks(rws)) {
							%>
								<div>
								----------------<%=rw.getReturnerName()%>在<%=rw.getRDT()%>退回----------------<hr/> 
								内容：<br /><%=rw.getNoteHtml()%><br/><hr/>
								</div>
							<%
							}
							isInfo = true;
						}
						break;
					case Shift:
						/* 判断移交过来的。 */
						ShiftWorks fws = new ShiftWorks();
						BP.En.QueryObject qo = new BP.En.QueryObject(fws);
						qo.AddWhere(ShiftWorkAttr.WorkID, workID);
						qo.addAnd();
						qo.AddWhere(ShiftWorkAttr.FK_Node, fk_node);
						qo.addOrderBy(ShiftWorkAttr.RDT);
						qo.DoQuery();
						if (fws.size() >= 1) {
							%><h2>移交信息:</h2><%
							for (ShiftWork fw : ShiftWorks.convertShiftWorks(fws)) {
								%>
								<div>
								----------------<%=fw.getFK_EmpName()%>在<%=fw.getRDT()%>移交----------------<hr/> 
								内容：<br /><%=fw.getNoteHtml()%><br/><hr/>
								</div>
							<%
							isInfo = true;
							}
						}
						break;
					}

				}
			%>
			
			</div>
	<%
} catch (Exception e) {

     Flow fl = new Flow(fk_flow);
     GERpt rpt = fl.getHisGERpt();
     rpt.setOID(workID);
     rpt.Retrieve();

     if (rpt != null){
         workState = rpt.getWFState();
     }
     throw e;
}
%>


