<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.DA.DataRow"%>
<%@page import="BP.DA.DataTable"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="cn.jflow.common.model.ConditionLineModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	
	String CondType=request.getParameter("CondType");
	String FK_Flow=request.getParameter("FK_Flow");
	String FK_MainNode=request.getParameter("FK_MainNode");
	String FK_Node=request.getParameter("FK_Node");
	String FK_Attr=request.getParameter("FK_Attr");
	String DirType=request.getParameter("DirType");
	String ToNodeID=request.getParameter("ToNodeID");
	String CurrentCond="";
	ConditionLineModel clm=new ConditionLineModel(request,response);
	DataTable datatable=clm.Page_Load();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>方向条件设置</title>
<link href="<%=basePath%>WF/Comm/Style/CommStyle.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"
	type="text/javascript"></script>
<script language="javascript">
        var currShow;

        //在右侧框架中显示指定url的页面
        function OpenUrlInRightFrame(ele, url) {
            if (ele != null && ele != undefined) {
                //if (currShow == $(ele).text()) return;

                currShow = $(ele).parents('li').text(); //有回车符

                $.each($('ul.navlist'), function () {
                    $.each($(this).children('li'), function () {
                        $(this).children('div').css('font-weight', $(this).text() == currShow ? 'bold' : 'normal');
                    });
                });

                $('#context').attr('src', url);
            }
        }

        $(document).ready(function () {
            $('ul.navlist').find("a[id='a<%=ToNodeID%>']").click();
	});
</script>
</head>
<body class="easyui-layout">
	<form id="form1" runat="server">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'west',split:true,title:'节点方向'"
					style="width: 300px;">
					<ul class="navlist">

						<%
							if (datatable != null) {
								for (DataRow dr : datatable.Rows) {
						%>
						<li>
							<div>
								<a id='a<%=dr.getValue("ToNode")%>' href="javascript:void(0)"
									onclick="OpenUrlInRightFrame(this, 'Condition.jsp?CondType=<%=CondType%>&FK_Flow=<%=FK_Flow%>&FK_MainNode=<%=dr.getValue("Node")%>&FK_Node=<%=dr.getValue("Node")%>&FK_Attr=<%=FK_Attr%>&DirType=<%=dr.getValue("DirType")%>&ToNodeID=<%=dr.getValue("ToNode")%>')">
									<span class="nav">到:<%=dr.getValue("ToNodeName")%></span>
								</a>
							</div>
						</li>
						<%
							}
							}
						%>

						<li>
							<div>
								<a href="javascript:void(0)"
									onclick="OpenUrlInRightFrame(this, 'CondPRI.jsp?CondType=<%=CondType%>&FK_Flow=<%=FK_Flow%>&FK_MainNode=<%=FK_MainNode%>&FK_Node=<%=FK_Node%>&FK_Attr=<%=FK_Attr%>&DirType=<%=DirType%>&ToNodeID=<%=ToNodeID%>')">
									<span class="nav">优先级设置</span>
								</a>
							</div>
						</li>
					</ul>
				</div>
				<div data-options="region:'center',noheader:true"
					style="overflow-y: hidden">
					<iframe id="context" scrolling="auto" frameborder="0" src=""
						style="width: 100%; height: 100%;"></iframe>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
