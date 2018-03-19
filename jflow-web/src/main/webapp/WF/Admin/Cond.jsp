<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	CondModel condModel = new CondModel(request, response,basePath);
	condModel.Page_Load();
%>
<script type="text/javascript">
function load() {
	var success = document.getElementById("success").value;
	if (success == "" || success == null) {
		return;
	} else {
		alert(success);
		location.href="<%=basePath%>WF/Admin/Cond.jsp?CondType=1&FK_Flow=001&FK_MainNode=101&FK_Node=101&FK_Attr=&DirType=&ToNodeID=101";
	}

}
function SelectAll(cb_selectAll) {
    var arrObj = document.all;
    if (cb_selectAll.checked) {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                arrObj[i].checked = true;
            }
        }
    } else {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox')
                arrObj[i].checked = false;
        }
    }
}
function ddl_SelectedIndexChanged() {
	var FK_Flow='<%=condModel.getFK_Flow()%>';
	var DDL_Node=$("#DDL_Node").val();
	var FK_MainNode='<%=condModel.getFK_MainNode()%>';
	var HisCondType='<%=condModel.getHisCondType()%>';
	var FK_Attr=$('#DDL_Attr').val();
	var ToNodeID='<%=condModel.getToNodeID()%>';
	location.href="<%=basePath%>des/cond_ddl_SelectedIndexChanged.do?FK_Flow="+ FK_Flow+ "&FK_Node="+ DDL_Node+ "&FK_MainNode="+ FK_MainNode+ "&CondType="+ HisCondType+ "&FK_Attr="+ FK_Attr + "&ToNodeID=" + ToNodeID + "";
}
function onAnd(){
			//var obj = window.event?event.srcElement:evt.target;
  	var btnId="Btn_SaveAnd";
  	var FK_Flow='<%=condModel.getFK_Flow()%>';
	var DDL_Node=$("#DDL_Node").val();
	var FK_MainNode='<%=condModel.getFK_MainNode()%>';
	var HisCondType='<%=condModel.getHisCondType()%>';
	var FK_Attr=$("#DDL_Attr").val();
	var ToNodeID='<%=condModel.getToNodeID()%>';
	var ddlNode=$("#DDL_Node").val();
	var DDL_Oper=$("#DDL_Oper").val();
	var str;
	var tb=$("#TB_Val");
	if(tb.length > 0)
		{
			str=$("#TB_Val").val();
			if(str==null || str=="")
				{
				alert("您没有设置条件，请在值文本框中输入值。");
				return;
				}else
					{
					str=$("#TB_Val").val();
					}
			
		}else
			{
			str=$("#DDL_Val option:selected").val();
			}
	var url="<%=basePath%>des/cond_btn_Save_Click.do?FK_Flow="+ FK_Flow+ "&tb="+str+"&FK_Node="+ DDL_Node+ "&FK_MainNode="+ FK_MainNode+ "&HisCondType="+ HisCondType+ "&FK_Attr="+ FK_Attr + "&ToNodeID=" + ToNodeID + "&DDL_Node="+ddlNode+"&DDL_Oper="+DDL_Oper+"&btnId="+btnId+"";
	location.href=url;
	//$("#form1").attr("action", url);
	//$("#form1").submit();
			
}
function onOr(){
			//var obj = window.event?event.srcElement:evt.target;
		  	var btnId="Btn_SaveOr";
		  	var FK_Flow='<%=condModel.getFK_Flow()%>';
			var DDL_Node=$("#DDL_Node").val();
			var FK_MainNode='<%=condModel.getFK_MainNode()%>';
			var HisCondType='<%=condModel.getHisCondType()%>';
			var FK_Attr=$("#DDL_Attr").val();
			var ToNodeID='<%=condModel.getToNodeID()%>';
			var ddlNode=$("#DDL_Node").val();
			var DDL_Oper=$("#DDL_Oper").val();
			var str;
			var tb=$("#TB_Val");
			if(tb.length > 0)
				{
					str=$("#TB_Val").val();
					if(str==null || str=="")
						{
						alert("您没有设置条件，请在值文本框中输入值。");
						return;
						}else
							{
							str=$("#TB_Val").val();
							}
					
				}else
					{
					str=$("#DDL_Val option:selected").val();
					}
			location.href="<%=basePath%>des/cond_btn_Save_Click.do?FK_Flow="+ FK_Flow+ "&FK_Node="+ DDL_Node+ "&FK_MainNode="+ FK_MainNode+ "&HisCondType="+ HisCondType+ "&FK_Attr="+ FK_Attr + "&ToNodeID=" + ToNodeID + "&DDL_Node="+ddlNode+"&DDL_Oper="+DDL_Oper+"&tb="+str+"&btnId="+btnId+"&DoType=";
}
function deletetable()
{
	var btnId="Btn_Delete";
	var FK_Flow='<%=condModel.getFK_Flow()%>';
	var DDL_Node=$("#DDL_Node").val();
	var FK_MainNode='<%=condModel.getFK_MainNode()%>';
	var HisCondType='<%=condModel.getHisCondType()%>';
	var FK_Attr=$("#DDL_Attr").val();
	var ToNodeID='<%=condModel.getToNodeID()%>';
	var ddlNode=$("#DDL_Node").val();
	var DDL_Oper=$("#DDL_Oper").val();
	location.href="<%=basePath%>des/cond_btn_Save_Click.do?FK_Flow="+ FK_Flow+ "&FK_Node="+ DDL_Node+ "&FK_MainNode="+ FK_MainNode+ "&HisCondType="+ HisCondType+ "&FK_Attr="+ FK_Attr + "&ToNodeID=" + ToNodeID + "&DDL_Node="+ddlNode+"&DDL_Oper="+DDL_Oper+"&btnId="+btnId+"";
	}
</script>
</head>
<body onLoad="load()">
<input type="hidden" id="success" value="${success }"/>
<form method="post" action="" class="am-form" id="form1">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			    <%=condModel.ui.ListToString()%>
			</div>
		</div>
	</form>
	
</body>
</html>