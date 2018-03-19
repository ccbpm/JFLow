<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String RefNo = request.getParameter("RefNo");
%>
<style>
tbody {
    display: table-row-group;
    vertical-align: middle;
    border-color: inherit;
}
li {
    display: list-item;
    text-align: -webkit-match-parent;
}
a {
     color:#0066CC;
     text-decoration:none;
   }
a:hover
   {
     color:#0084C5;
     text-decoration:underline;
   }
</style>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<script language=javascript>
	function DoAutoTo(fk_emp, empName) {
		if (window.confirm('您确定要把您的工作授权给[' + fk_emp + ']吗？') == false)
			return;
		var url = 'Do.jsp?DoType=AutoTo&FK_Emp=' + fk_emp;
		WinShowModalDialog(url, '');
		alert('授权成功，请别忘记收回。');
		window.location.href = 'Tools.jsp';
	}

	function ExitAuth(fk_emp) {
		if (window.confirm('您确定要退出授权登录模式吗？') == false)
			return;

		var url = 'Do.jsp?DoType=ExitAuth&FK_Emp=' + fk_emp;
		WinShowModalDialog(url, '');
		window.location.href = 'Tools.jsp';
	}

	function TakeBack(fk_emp) {
		if (window.confirm('您确定要取消对[' + fk_emp + ']的授权吗？') == false)
			return;

		var url = 'Do.jsp?DoType=TakeBack';
		WinShowModalDialog(url, '');
		alert('您已经成功的取消。');
		window.location.reload();
	}

	function LogAs(fk_emp) {
		if (window.confirm('您确定要以[' + fk_emp + ']授权方式登录吗？') == false)
			return;

		var url = 'Do.jsp?DoType=LogAs&FK_Emp=' + fk_emp;
		WinShowModalDialog(url, '');
		alert('登录成功，现在您可以以[' + fk_emp + ']处理工作。');
		window.location.href = 'EmpWorks.jsp';
	}

	function CHPass() {
		var url = 'Do.jsp?DoType=TakeBack';
		// WinShowModalDialog(url,'');
		alert('密码修改成功，请牢记您的新密码。');
	}
	function loadPage(id, url) {
		$("#" + id).load(url);
	}
	$(function(){  
		loadPage("aaa","ToolsWap.jsp?RefNo=Per");
	});
	$(function(){
		$('ul li').bind('click',function(){
			$(this).siblings().children("a").css("color","black");
			$(this).siblings().children("a").css("font-size",12);
			$(this).siblings().children("a").css("font-weight","normal");
			//console.log($(this).children("a"));
			$(this).children("a").css("color","#026ac1");
			$(this).children("a").css("font-size",14);
			$(this).children("a").css("font-weight","bolder");
		}); 
	}); 
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<!-- <div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>设置</small>
			</div>
		</div> -->

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form" method="post" action="Tools.jsp" id="form1"
					onkeypress="NoSubmit(event);">
					<table width='100%' align='left'>
						<caption class="CaptionMsg">系统设置</caption>
						<tr>
							<td valign=top width='20%' align='center' style="font-size:14px;height:100%;color:#026ac1;border-width: 1px; border-color: #C2D5E3;border-right-style:solid !important">
								<%
									BP.WF.XML.Tools tools = new BP.WF.XML.Tools();
									tools.RetrieveAll();

									if (tools.size() == 0)
										return;
									String refno = RefNo;
									//System.out.println(refno);
									if (refno == null||"".equals(refno))
										refno = "Per";
									
								%>
										<%
											//System.out.println(WebUser.getIsWap());
											if (WebUser.getIsWap()) {
										%>
										<th><a href='Home.jsp'><img src='/WF/Img/Home.gif'
												border="0" />Home</a></th>
										<%
											}
										%>
											<ul style="line-height:28px;">
												<%
													for (BP.WF.XML.Tool tool : tools.ToJavaList()) {
														if (tool.getNo().equals(refno)) {
												%>
												<li><a href="#"
													onclick="loadPage('aaa','ToolsWap.jsp?RefNo=<%=tool.getNo()%>')"><%=tool.getName()%></a></li>
												<%
													} else {
												%>
												<li><a href="#"
													onclick="loadPage('aaa','ToolsWap.jsp?RefNo=<%=tool.getNo()%>')"><%=tool.getName()%></a>

												</li>
												<%
													}
													}

													if (WebUser.getNo().equals("admin")) {
												%>
												<li><a href="#"
													onclick="loadPage('aaa','Holiday.jsp')">节假日设置</a></li>
												<!-- <li> <a href='Holiday.jsp' >节假日设置</a></li> -->
												<%
													}
												%>
											</ul>
							</td>
							<td valign=top align='left' width='80%' id="aaa"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>