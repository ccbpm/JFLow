<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	CondStationModel condModel=new CondStationModel(request,response);
	condModel.Page_Load();
%>
<script type="text/javascript">
function load() {
	var success = document.getElementById("success").value;
	if (success == "" || success == null) {
		return;
	} else {
		alert(success);
	}

}

	function btn_save()
	{
		var btnId="Btn_Save";
		var str="";
		var MyPK='<%=condModel.getMyPK()%>';
		var ToNodeID='<%=condModel.getToNodeID()%>';
		var HisCondType='<%=condModel.getHisCondType()%>';
		var FK_Flow='<%=condModel.getFK_Flow()%>';
		var FK_MainNode='<%=condModel.getFK_MainNode()%>';
		var FK_Node='<%=condModel.getFK_Node()%>';
		var FK_Attr='<%=condModel.getFK_Attr()%>';
		$("input[class='']:checkbox:checked").each(function() {
			str += $(this).attr("id")+",";
		})
		location.href="<%=basePath%>des/condStation_btn_Save_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&FK_Attr=" + FK_Attr + "&ToNodeID=" + ToNodeID+"&btnId="+btnId+"&str="+str;
	}
	function btn_del()
	{
		var btnId="Btn_Delete";
		var str="";
		var MyPK='<%=condModel.getMyPK()%>';
		var ToNodeID='<%=condModel.getToNodeID()%>';
		var HisCondType='<%=condModel.getHisCondType()%>';
		var FK_Flow='<%=condModel.getFK_Flow()%>';
		var FK_MainNode='<%=condModel.getFK_MainNode()%>';
		var FK_Node='<%=condModel.getFK_Node()%>';
		var FK_Attr='<%=condModel.getFK_Attr()%>';
		$("input[class='']:checkbox:checked").each(function() {
			str += $(this).attr("id")+",";
		})
		location.href="<%=basePath%>des/condStation_btn_Save_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&FK_Attr=" + FK_Attr + "&ToNodeID=" + ToNodeID+"&btnId="+btnId+"&str="+str;
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
		//全选
		$("#CB_s_d1").click(function() {
				$("input[name='CB_01']").attr("checked", "true");
		})
		
		
		//选择岗位及子岗位
	   function SetSelected(obj,ids){
		  id=ids.split(",");
		  for(var i=0;i<id.length;i++){
			  $("input[name='"+id[i]+"']").attr("checked", "true");
		  }
	   }
	var spanName=new Array("　　　　参数：","　　节点编号：","　　表单字段：","　操作员编号：");
	//指定的操作员下拉选择框切换事件
	function changeSpanText(obj){
		var v=obj.value;
		$("#LBL1").html(spanName[v]);
		if(v=='0'){
			$("#TB_SpecOperPara").attr("disabled","disabled");
			$("#TB_SpecOperPara").val('');
		}else{
			$("#TB_SpecOperPara").removeAttr("disabled");
		}
	}

</script>
</head>
<body onLoad="load()">
<input type="hidden" id="success" value="${success }"/>
<form method="post" action="" class="am-form" id="form1">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			    <%=condModel.Pub1.ListToString() %>
				<BR>指定的操作员：
				<select name="DDL_SpecOperWay" id="DDL_SpecOperWay" onchange="changeSpanText(this);" style="width: 300px;" 　>
					<option selected="selected" value="0">当前操作员</option>
					<option value="1">指定节点的操作员</option>
					<option value="2">指定表单字段作为操作员</option>
					<option value="3">指定操作员编号</option>
				</select>
				<BR> <BR> 
				
					<span id="LBL1">　　　　参数：</span>
					<input type="text" id="TB_SpecOperPara" name="TB_SpecOperPara" style="width: 300px;" disabled="disabled" />
					
					&nbsp;&nbsp;多个值请用英文“逗号”来分隔。
					<BR> <BR>&nbsp;&nbsp;
					
					<a href="javascript:btn_save()" id="Btn_Save" name="Btn_Save"
					class="easyui-linkbutton" iconCls="icon-save">保存</a>&nbsp;&nbsp;
					<a href="javascript:btn_del()" id="Btn_Delete" name="Btn_Delete"
					class="easyui-linkbutton" onclick=" return confirm('您确定要删除吗？');"
					iconCls="icon-delete">删除</a>

			</div>
		</div>
	</form>
	
</body>
</html>