<%@page import="BP.Tools.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="cn.jflow.common.model.GroupModel"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	GroupModel gm = new GroupModel(request, response);
	
	String fkFlow = gm.getFK_Flow();
	String rptNo = gm.getRptNo();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>分组分析 - 共享任务测试</title>
<style type="text/css">
html, body {
	height: 100%;
	margin: 0 auto;
}

.MyTable {
	text-align: left;
}
</style>
<link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Comm/Charts/css/style_3.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Comm/Charts/css/prettify.css" rel="stylesheet" type="text/css" />

<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/EasyUIUtility.js" type="text/javascript"></script>

<script src="<%=basePath%>WF/Comm/Charts/js/prettify.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Comm/Charts/js/json2_3.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Comm/Charts/js/FusionCharts.js" type="text/javascript"></script>
<script src="<%=basePath%>WF/Comm/Charts/js/FusionChartsExportComponent.js" type="text/javascript"></script>
<script language="JavaScript" src="<%=basePath%>WF/Comm/JScript.js"
	type="text/javascript"></script>
<script type="text/javascript">
        function OpenUrl(wintitle, url) {
            var dlg = $('#win').dialog({ title: wintitle, onClose: function () { $('#winFrame').attr('src', ''); } });
            dlg.dialog('open');
            $('#winFrame').attr('src', "../../Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Rpt.MapRpts&EnName=BP.WF.Rpt.MapRpt&AttrKey=BP.WF.Rpt." + rpt + "&No=" + rptNo);
        }

        function WinOpen(url) {
            var newWindow = window.open(url, 'z', 'scroll:1;status:1;help:1;resizable:1;dialogWidth:680px;dialogHeight:420px');
            newWindow.focus();
            return;
        }

        function WinOpen(url, winName) {
            var newWindow = window.open(url, winName, 'width=700,height=400,top=100,left=300,scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
            newWindow.focus();
            return;
        }

    </script>

</head>
<body class="easyui-layout">
	<form method="post" id="form1" style="height: 100%">
		<div id="mainDiv"
			data-options="region:'center',title:'分组分析 - <%=gm.getTitle()%>'"
			style="padding: 5px">
			<div class="easyui-layout" data-options="fit:true">
				<div id="div_toolbar" data-options="region:'north',noheader:true,split:false" style="padding: 2px; height: auto; background-color: #E0ECFF; line-height: 30px">
					<%=gm.getToolBar()%>
				</div>
				<div data-options="region:'west',title:'分组条件'" style="padding: 5px; width: 200px;">
					<div id="div_check_box_list" class="easyui-panel" title="显示内容" style="padding: 5px; margin-bottom: 5px">
						<%=gm.getCheckBoxList1()%>
					</div>
					<div id="div_uc_sys_02" class="easyui-panel" title="分析项目" style="padding: 5px; margin-bottom: 5px">
						<%=gm.getUCSys2()%>
					</div>
					<div class="easyui-panel" title="图表" style="padding: 5px; margin-bottom: 5px">
						<table class="Table" cellpadding="0" cellspacing="0" border="0"
							style="width: 100%">
							<tr>
								<td colspan="2">
										<%=gm.getCB_IsShowPict()%>
								</td>
							</tr>
							<tr>
								<td style="width: 80px">高度:</td>
								<td>
										<%=gm.getTB_H()%>
								</td>
							</tr>
							<tr>
								<td>宽度:</td>
								<td>
										<%=gm.getTB_W()%>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: right">
										<%=gm.getLbtnApply()%>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div data-options="region:'center'" style="padding: 5px;">
					<%=gm.getUCSys() %>
				</div>
		</div>
	</form>
<script type="text/javascript">
	function ddl_SelectedIndexChanged_Goto(){
		var item = $("#GoTo").val();
	    window.location="<%=basePath%>WF/RptDfine/" + item + ".jsp?RptNo=<%=rptNo%>&FK_Flow=<%=fkFlow%>";
	}
	function changeSelectedCheckBox(){
		State_Changed();
	}
	function State_Changed(event){
		$('#form1').attr("action", "<%=basePath%>WF/RptDfine/Group.jsp?RptNo=<%=rptNo%>&OrderBy=<%=gm.getOrderBy()%>&OrderWay=<%=gm.getOrderWay()%>&NumKey=<%=gm.getNumKey()%>&FK_Flow=<%=fkFlow%>&isPostBack=true");
		$('#form1').submit();
	}
	function ToolBar1_ButtonClick(btnName){
		$('#form1').attr("action", "<%=basePath%>WF/RptDfine/Group.jsp?RptNo=<%=rptNo%>&OrderBy=<%=gm.getOrderBy()%>&OrderWay=<%=gm.getOrderWay()%>&NumKey=<%=gm.getNumKey()%>&FK_Flow=<%=fkFlow%>&isPostBack=true&btnName=" + btnName);
		$('#form1').submit();
	}
	function lbtnApply_Click(){
		State_Changed();
	}
</script>
</body>
</html>