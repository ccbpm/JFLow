<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String title = "流程检查";
	StringBuilder infor = new StringBuilder();
	String fk_flow = request.getParameter("FK_Flow");
	String do_type= request.getParameter("DoType");
	String ref_no = request.getParameter("RefNo");
	
    if (StringHelper.isNullOrEmpty(ref_no)){
    	 ref_no = request.getParameter("No");
    }
	// 是否为空
	if(StringHelper.isNullOrEmpty(do_type)){
		infor.append(BaseModel.AddMsgOfInfo("错误标记", "DoType为空！"));
	}else if(do_type.equals("FlowCheck")){
		Flow fl = new Flow(ref_no);
		infor.append(BaseModel.AddFieldSet(fl.getName()+"流程检查信息"));

        title = fl.getName()+"流程检查";
        infor.append(fl.DoCheck().replace("@", "<BR>@"));
        String replaceStr = infor.toString();
        replaceStr = replaceStr.replace("@错误", "<font color=red><b>@错误</b></font>");
        replaceStr = replaceStr.replace("@警告", "<font color=yellow><b>@警告</b></font>");
        infor = new StringBuilder(replaceStr.replace("@信息", "<font color=black><b>@信息</b></font>"));

        infor.append(BaseModel.AddFieldSetEnd());
	}else{
		infor.append(BaseModel.AddMsgOfInfo("错误标记", do_type));
	}
	
%>

<%@ include file="/WF/head/head2.jsp"%>
<title><%=title %></title>

</head>
<body >
	<form id="form1">
    <div  style='width:80%' >
        <%=infor.toString() %>
    </div>
    </form>
</body>
</html>
