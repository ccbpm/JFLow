<%@page import="cn.jflow.common.model.TestFlowModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%
	TestFlowModel testFlowModel = new TestFlowModel(request, response);
	testFlowModel.loadPage();
%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<title>感谢您选择驰骋工作流程引擎-流程设计&测试界面</title>
<script type="text/javascript" language="javascript">
	function Del(mypk, fk_flow, refoid) {
		if (window.confirm('Are you sure?') == false)
			return;

		var url = 'Do.jsp?DoType=Del&MyPK=' + mypk + '&RefOID=' + refoid;
		var b = window
				.showModalDialog(url, 'ass',
						'dialogHeight: 400px; dialogWidth: 600px;center: yes; help: no');
		window.location.href = window.location.href;
	}
	function WinOpen(url) {
		var b = window
				.open(
						url,
						'ass',
						'width=700,top=50,left=50,height=500,scrollbars=yes,resizable=yes,toolbar=false,location=false');
		b.focus();
	}
	function WinOpen(url, w, h, name) {
		var b = window
				.open(
						url,
						name,
						'width='
								+ w
								+ ',height='
								+ h
								+ ',scrollbars=yes,resizable=yes,toolbar=false,location=false,center: yes');
	}
	function WinOpenWAP_Cross(url) {
		var b = window
				.open(
						url,
						'ass',
						'width=50,top=50,left=50,height=20,scrollbars=yes,resizable=yes,toolbar=false,location=false');
	}
</script>
<script language="javascript">
	function ShowIt(m) {
		var url = '../Comm/Method.jsp?M=' + m;
		var a = window
				.showModalDialog(
						url,
						'OneVs',
						'dialogHeight: 400px; dialogWidth: 500px; dialogTop: 100px; dialogLeft: 110px; center: yes; help: no');
	}
	function Open(no) {
		if (window.confirm('您确定要该流程编号为:' + no + '的数据吗？') == false)
			return;
		var url = '../Comm/RefMethod.jsp?Index=3&EnsName=BP.WF.Template.FlowSheets&No='
				+ no;
		var a = window
				.showModalDialog(
						url,
						'OneVs',
						'dialogHeight: 400px; dialogWidth: 500px; dialogTop: 100px; dialogLeft: 110px; center: yes; help: no');
	}

	function SelectAll(cb_selectAll) {
		var arrObj = document.all;
		if (cb_selectAll.checked) {
			for (var i = 0; i < arrObj.length; i++) {
				if (typeof arrObj[i].type != "undefined"
						&& arrObj[i].type == 'checkbox') {
					arrObj[i].checked = true;
				}
			}
		} else {
			for (var i = 0; i < arrObj.length; i++) {
				if (typeof arrObj[i].type != "undefined"
						&& arrObj[i].type == 'checkbox')
					arrObj[i].checked = false;
			}
		}
	}
</script>
</head>
<body style="background-color:Silver; margin-top:0px;text-align:center;"	topmargin="0" leftmargin="0">
<center>
	<form id="form1" class="am-form">
		<div>
			<%=testFlowModel.pub.toString()%>
		</div>
	</form>
</center>
</body>
</html>

