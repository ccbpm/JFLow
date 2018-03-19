<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%@page import="cn.jflow.model.designer.ActionModel"%>
<%
	ActionModel am = new ActionModel(request,response,basePath);
	am.init();
%>
<script type="text/javascript">
    var showMsg = <%=am.getHaveMsg() %>;

    $(document).ready(function () {
        var currEventGroup = '<%= am.getCurrentEventGroup() %>';

        $("#eventAccordion").accordion("select", currEventGroup);

        var urlParams = "?NodeID=<%=am.getNodeID() %>&MyPK=<%=am.getMyPK() %>&FK_MapData=<%=am.getFK_MapData() %>&Event=<%=am.getEvent() %>&FK_Flow=<%=am.getFK_Flow() %>&tk=";
        //alert(urlParams);
        $("#src1").attr("src","ActionEvent.jsp" + urlParams + Math.random());

        if(showMsg){
            $("#src2").attr("src","ActionPush2Curr.jsp" + urlParams + Math.random());
            $("#src3").attr("src","ActionPush2Spec.jsp" + urlParams + Math.random());
        }
    });
</script>
</head>
<body class="easyui-layout">
    <form method="post" action="" class="am-form" id="form1">
    <div data-options="region:'center',title:'<%=am.getTitle() %>',border:false">
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'east',noheader:true" style="width: 200px;">
                <div id="eventAccordion" class="easyui-accordion" data-options="fit:true,border:false,animate:false">
                    <%=am.Pub1.toString() %>
                </div>
            </div>
            <div data-options="region:'center',title:'<%=am.getCurrentEvent() %>'" style="padding: 5px;">
                <%=am.Pub2.toString() %>
            </div>
    </div>
    </div>
    </form>
</body>
</html>