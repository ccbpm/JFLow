<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<%
	String val="";
	DataTable dt=null;
	String sql="";
	String strs=request.getParameter("CtrlVal");
	if(strs!=null){
	String[] str=strs.split(";");
	String s="";
	for(int i=0;i<str.length;i++){
		String[] st=str[i].split(",");
		s+="'"+st[0]+"',";
	}
	sql="select BillNo as 申请单号,XingMing as 姓名,(select Lab from dbo.Sys_Enum where EnumKey='DuiXiangLeiXing' and IntKey=DuiXiangLeiXing)as 对象类型,ShenFenZhengHao as 身份证号, (select name from TX_QX where TX_QX.No = ND124Rpt.FK_QX1) as 区县,(select name from TX_JD where TX_JD.No = ND124Rpt.FK_QX) as 街道,(select name from TX_SQ where TX_SQ.No = ND124Rpt.FK_JD) as 社区,ZhuZhi as 联系地址,LianXiDianHua as 联系电话 from ND124Rpt where WFSta=1 and BillNo not in("+s.substring(0,s.length()-1)+")";
	dt=BP.DA.DBAccess.RunSQLReturnTable(sql);
	}else{
		sql="select BillNo as 申请单号,XingMing as 姓名,(select Lab from dbo.Sys_Enum where EnumKey='DuiXiangLeiXing' and IntKey=DuiXiangLeiXing)as 对象类型,ShenFenZhengHao as 身份证号, (select name from TX_QX where TX_QX.No = ND124Rpt.FK_QX1) as 区县,(select name from TX_JD where TX_JD.No = ND124Rpt.FK_QX) as 街道,(select name from TX_SQ where TX_SQ.No = ND124Rpt.FK_JD) as 社区,ZhuZhi as 联系地址,LianXiDianHua as 联系电话 from ND124Rpt where WFSta=1";
		dt=BP.DA.DBAccess.RunSQLReturnTable(sql);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Pop返回值</title>
<script type="text/javascript">
	function SetSelected(cb_selectAll) {
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
	function submint() {
		var st = window.opener.document.getElementById('TB_WeiWenDuiXiang').value;
		var r = document.getElementsByName("CB_02");
		var s="";
		var str="";
		for (var i = 0; i < r.length; i++) {
			if (r[i].checked) {
				//val += r[i].nextSibling.nodeValue + ';\r\n';
				var s1=document.getElementById("BN_"+i).innerHTML+ ',';
				var s2=document.getElementById("XM_"+i).innerHTML+ ',';
				var s3=document.getElementById("DXLX_"+i).innerHTML+ ',';
				var s4=document.getElementById("SFZH_"+i).innerHTML+ ',';
				var s5=document.getElementById("FQ_"+i).innerHTML+ ',';
				var s6=document.getElementById("FJ_"+i).innerHTML+ ',';
				var s7=document.getElementById("FQ1_"+i).innerHTML+ ',';
				var s7=document.getElementById("ZZ_"+i).innerHTML+ ',';
				var s8=document.getElementById("LXDH_"+i).innerHTML+ ';\r\n';
				st+=s1+s2+s3+s4+s5+s6+s7+s8;
			}
		}
		var str1 = st.split(";");
		for(var i=0;i<str1.length;i++){
			var s=str1[i].split(",");
			str+=s[0]+",";
		}
		if (window.opener != undefined) {
			window.top.returnValue = st;
		} else {
			window.returnValue = st;
		}
		window.opener.document.getElementById('TB_YinCangYu').value=str.substr(0,str.length-4);
		var num = st.split(";");
		window.opener.document.getElementById('TB_WeiWenRenShu').value = num.length - 1;
		window.close();
	}

	//关闭
	function CloseWin() {
		var val = window.opener.document.getElementById('TB_WeiWenDuiXiang').value;
		if (window.opener != undefined) {
			window.top.returnValue = val;
		} else {
			window.returnValue = val;
		}
		var str = val.split(";");
		var s="";
		for(var i=0;i<str.length;i++){
			var st=str[i].split(",");
			s+=st[0]+",";
		}
		window.opener.document.getElementById('TB_YinCangYu').value=s.substr(0,s.length-3);
		window.opener.document.getElementById('TB_WeiWenRenShu').value = str.length - 1;
		window.close();
	}
</script>
</head>
<body>
	<div class="am-g">
		<form action="" method="post" id="form1">
			<div class="am-u-sm-6">
				<table class="am-table">
					<tr>
						<td nowrap="nowrap"><label class="am-checkbox"> <input
								type="checkbox" id="CBs_01" name="CBs_01"
								onclick="SetSelected(this)" data-am-ucheck>
						</label></td>
						<td nowrap="nowrap">申请单号</td>
						<td nowrap="nowrap">姓名</td>
						<td nowrap="nowrap">证件类型</td>
						<td nowrap="nowrap">身份证号</td>
						<td nowrap="nowrap">区县</td>
						<td nowrap="nowrap">街道</td>
						<td nowrap="nowrap">社区</td>
						<td nowrap="nowrap">联系地址</td>
						<td nowrap="nowrap">电话</td>
					</tr>
					<%
					for(int i=0;i<dt.Rows.size();i++)
					{
						DataRow dr=dt.Rows.get(i);
					%>
					<tr>
					<td nowrap="nowrap"><label class="am-checkbox"> <input
								type="checkbox" id="CB_02" name="CB_02"
								 data-am-ucheck>
						</label></td>
						<td><span id="BN_<%=i%>"><%=dr.getValue(0)==null?"":dr.getValue(0).toString()%></span></td>
						<td><span id="XM_<%=i%>"><%=dr.getValue(1)==null?"":dr.getValue(1).toString()%></span></td>
						<td><span id="DXLX_<%=i%>"><%=dr.getValue(2)==null?"":dr.getValue(2).toString()%></span></td>
						<td><span id="SFZH_<%=i%>"><%=dr.getValue(3)==null?"":dr.getValue(3).toString()%></span></td>
						<td><span id="FQ_<%=i%>"><%=dr.getValue(4)==null?"":dr.getValue(4).toString()%></span></td>
						<td><span id="FJ_<%=i%>"><%=dr.getValue(5)==null?"":dr.getValue(5).toString()%></span></td>
						<td><span id="FQ1_<%=i%>"><%=dr.getValue(6)==null?"":dr.getValue(6).toString()%></span></td>
						<td><span id="ZZ_<%=i%>"><%=dr.getValue(7)==null?"":dr.getValue(7).toString()%></span></td>
						<td><span id="LXDH_<%=i%>"><%=dr.getValue(8)==null?"":dr.getValue(8).toString()%></span></td>
						</tr>
					<%
						}
					%>
				</table>
			</div>
		</form>
	</div>
	<div class="am-btn-group">
		<button onclick="submint();" type="button"
			class="am-btn am-btn-primary am-round">
			<i class="am-icon-check"></i> 选择
		</button>
		<button onclick="CloseWin();" type="button"
			class="am-btn am-btn-primary am-round">
			<i class="am-icon-remove"></i> 取消
		</button>
	</div>
	<%-- <form action="" method="post" id="form1">
		<table border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="checkbox" id="CBs_01" name="CBs_01"
					onclick="SetSelected(this)">全部选择</td>
				<!-- 				<td>对象编号</td> -->
				<!-- 				<td>对象名称</td> -->
				<!-- 				<td>身份证号</td> -->
			</tr>
			<%
				for (DataRow dr : dt.Rows) {
			%>
			<tr>
				<td><input type="checkbox" id="CB_02" name="CB_02"><%=dr.getValue("BillNo").toString()%>,<%=dr.getValue("FlowStarter").toString()%>,<%=dr.getValue("ShenFenZhengHao").toString()%></td>
								<td><%=dr.getValue("BillNo").toString()%></td>
								<td><%=dr.getValue("FlowStarter").toString()%></td>
								<td><%=dr.getValue("ShenFenZhengHao").toString()%></td>
			</tr>
			<%
				}
			%>
		</table>
		<input type="submit" onclick="submint();" value="OK"> <input
			type="button" onclick="CloseWin();" value="Close">
	</form> --%>
</body>
</html>