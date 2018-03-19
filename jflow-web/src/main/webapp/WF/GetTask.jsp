<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
<%
	String PageSmall = null;
	String PageID = Glo.getCurrPageID();
	if (PageID.toLowerCase().contains("smallsingle")) {
		PageSmall = "SmallSingle";
	} else if (PageID.toLowerCase().contains("small")) {
		PageSmall = "Small";
	} else {
		PageSmall = "";
	}

	String workId = request.getParameter("WorkID") == null?"0":request.getParameter("WorkID");
	long WorkID = Long.valueOf(workId);
	String fk_node = request.getParameter("FK_Node") == null?"0":request.getParameter("FK_Node");
	int FK_Node = Integer.valueOf(fk_node);
	String toNode = request.getParameter("ToNode") == null?"0":request.getParameter("ToNode");
	int ToNode = Integer.valueOf(toNode);
	String FK_Flow = request.getParameter("FK_Flow") == null?"":request.getParameter("FK_Flow");
	String DoType = request.getParameter("DoType") == null?"":request.getParameter("DoType");

	GetTaskModel model = new GetTaskModel(basePath, WorkID, FK_Node,
			ToNode, FK_Flow, DoType, PageID, PageSmall);
	model.init();
%>
<script type="text/javascript">
function Tackback(fk_flow, fk_node, toNode, workid) {
    if (confirm('您确定要执行取回操作吗？')){
    	var url = '<%=basePath%>WF/GetTask.jsp?DoType=Tackback&FK_Flow='
					+ fk_flow + '&FK_Node=' + fk_node + '&ToNode=' + toNode
					+ '&WorkID=' + workid;
			window.location.href = url;
		}
	}
</script>
</head>
<body style="margin: 1px;">
<table border=1px align=center width='100%'>
	<%=model.Pub1%>
</table>
</body>
</html>