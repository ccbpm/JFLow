<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
	function GenTemplete() {
		var title = $("#title").val();
		var num = $("#num").val();
		$("#form").attr("action","<%=basePath%>WF/ManagerModel.jsp?num="+num+"&title="+title);
		$("#form").submit();
	}
</script>
</head>
<body>
	<!-- <div class="am-u-md-6" style="width: 100%"> -->
		<form class="am-form" id="form" method="post" action="">
			<table border=1px align=center width='100%'>
				<Caption ><div class='' >新建模块</div></Caption>

				<tr>
					<td>模块标题</td>
					<td>
						<input type="text" id="title" class="am-input-sm" style="width:100%">
					</td>
					<td>标题名称用逗号分隔!注意逗号是中文逗号!</td>
				</tr>
				<tr>
					<td>模块数量</td>
					<td>
						<input type="text" id="num" class="am-input-sm" style="width:100%">
					</td>
					<td>数量与标题数量对应</td>
				</tr>
			<tr><td colspan="3">
				<A href="javascript:void(0)" onclick="GenTemplete()" id="ri"
					target="right"><button type="button"
						class="am-btn am-btn-success" style="background: #4D77A7;color: #FFF;padding: 2px 3px 2px 3px;margin: 3px 2px 3px 2px;">生成模块</button></A>
				</td>
			</tr>
			</table>
		</form>
	<!-- </div> -->
</body>
</html>