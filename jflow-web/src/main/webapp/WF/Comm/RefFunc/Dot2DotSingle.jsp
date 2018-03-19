<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	Dot2DotSingleModel dot = new Dot2DotSingleModel(request, response);
	dot.Page_Load();
%>
<style>
.easyui-layout{
width: auto;
height: 498px;
}

</style>
<script language="javascript" type="text/javascript">
function SetSelected(cb, ids) {
    var arrmp = ids.split(",");
    var arrObj = document.all;
    var isCheck = false;
    if (cb.checked)
        isCheck = true;
    else
        isCheck = false;
    for (var i = 0; i < arrObj.length; i++) {
        if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
            for (var idx = 0; idx < arrmp.length; idx++) {
                if (arrmp[idx] != "" && arrmp[idx] != null){
                    var cid = arrObj[i].name;
                    //alert(isCheck);
                    var ctmp = arrmp[idx];
                    if (cid.indexOf(ctmp) >= 0) {
                        arrObj[i].checked = isCheck;
                    }
                    //                    alert(arrObj[i].name + ' is checked ');
                    //                    alert(cid + ctmp);
                }
            }
        }
    }
}
	function selectAll(cb_selectAll) {
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
	
	function BPToolBar1_ButtonClick()
	{
		var btnId="Btn_Save";
		var DDL_Group=$("#DDL_Group").get(0).selectedIndex;
		var EnName='<%=dot.getEnName()%>';
		var PK='<%=dot.getPK()%>';
		var AttrKey='<%=dot.getAttrKey()%>';
		var ShowWay = $("#DDL_Group").val();
		$("#FormHtml").val($("#form1").html());
		$.ajax({
				cache: false,
				type: "POST",
				url:"<%=basePath%>WF/MapDef/getStr.do?btnId=" + btnId
					+ "&DDL_Group=" + DDL_Group + "&EnName=" + EnName + "&PK="
					+ PK + "&AttrKey=" + AttrKey +"&ShowWay="+ShowWay, //把表单数据发送到ajax.jsp
			data : $('#form1').serialize(), //要发送的是ajaxFrm表单中的数据
			async : false,
			error : function(request) {
				alert("发送请求失败！");
			},
			success : function(data) {
				alert("保存成功"); //将返回的结果显示到ajaxDiv中
				window.opener.location.reload();
				return false;
			}
		});
		return false;
	}
	 function load() {
		var success = document.getElementById("success").value;
		if (success == "" || success == null) {
			return;
		} else {
			//alert(success);
		}

	} 
	function DDL_Group_SelectedIndexChanged(){
		$("#listUC table:first-child").remove();
		var url = window.location.href;
		var num=$("#DDL_Group").val();
		url = url.replace("&ShowWay=StaGrade","").replace("&ShowWay=FK_Dept","").replace("&ShowWay=None","");
		if(num=="None"){
			url += "&ShowWay=None";
		}else if(num=="StaGrade"){
			url += "&ShowWay=StaGrade";
		}else if(num=="FK_StationType"){
			url += "&ShowWay=FK_StationType";
		}else{
			url += "&ShowWay=FK_Dept";
		}
		window.location.href=url;
		//window.location.reload(true)
		<%-- $.ajax({
			cache: false,
			type: "POST",
			url:"<%=basePath%>WF/MapDef/reload.do",
			data : {url:url}, //要发送的是ajaxFrm表单中的数据
			async : false,
			error : function(request) {
				alert("发送请求失败！");
			},
			success : function(data) {
				alert("保存成功");
				//return false;
			}
		}); --%>
	}
</script>
</head>
<body class="easyui-layout" onload="load()">
	<input type="hidden" value="${success }" id="success" />
	<form method="post" action="" id="form1"
		onsubmit="return BPToolBar1_ButtonClick()">
		<input type="hidden" id="FormHtml" name="FormHtml" value="">
		<div class="easyui-layout" data-options="fit:true">
			<div
				data-options="region:'north',noheader:true,split:false,border:false"
				style="height: 30px; padding: 2px; background-color: #E0ECFF">
				<%=dot.ToolBar1.toString()%>
			</div>
			<div id="listUC" data-options="region:'center',noheader:true,border:false"">
				<%=dot.UCSys1.ListToString()%>
			</div>
		</div>
	</form>
</body>
</html>