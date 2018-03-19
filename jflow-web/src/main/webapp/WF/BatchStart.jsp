<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String FK_Flow = request.getParameter("FK_Flow")==null?"":request.getParameter("FK_Flow");
	
	String FK_MapData = "ND" + Integer.valueOf(FK_Flow+"01");
	int RowNum = 12;
	
	String Key = request.getParameter("Key")==null?"":request.getParameter("Key");
	String DoType = request.getAttribute("DoType")==null?"":request.getAttribute("DoType").toString();
	String normMsg = request.getAttribute("normMsg")==null?"":request.getAttribute("normMsg").toString();
	
	BatchStartModel model = new BatchStartModel(basePath, Key, RowNum, FK_MapData, FK_Flow, DoType, normMsg);
	model.init();
%>
<script type="text/javascript">
function SelectAllBS(ctrl) {
    var arrObj = document.all;
    if (ctrl.checked) {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                if (arrObj[i].name.indexOf('IDX_') > 0)
                    arrObj[i].checked = true;
            }
        }
    } else {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                if (arrObj[i].name.indexOf('IDX_') > 0)
                    arrObj[i].checked = false;
            }
        }
    }
}
function Send_Click(){
	var FK_Flow = '<%=FK_Flow%>';
	var FK_MapData = '<%=FK_MapData%>';
	var RowNum = '<%=RowNum%>';
	
	if(confirm("您确定要执行吗？")){	
		if(vailCheckBox()){
			alert("您没有选择工作！");
			return false;
		}
		
		//$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>WF/BatchStartSend.do?FK_Flow="+FK_Flow+"&FK_MapData="+FK_MapData+"&RowNum="+RowNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function vailCheckBox(){
	var vail = true;
	var RowNum = <%=RowNum%>;
	for(var i = 1;i <= RowNum; i++) {
		 var obj = $("#CB_IDX_"+i);
		 if(obj.length <= 0)continue;
         if(obj.attr("checked")){
         	vail = false;
         }
	}
	return vail;
}
function Upload_Click(){
	var str = $("#fileupload").val();
    if(str.length==0){
        alert("请选择xls或者xlsx文件！");
        return false;
    }
	
	var FK_Flow = '<%=FK_Flow%>';
	$.ajaxFileUpload({
		url:"BatchStartUpload.do",
		secureuri:false,
		dataType:"json",
		data:{"FK_Flow":FK_Flow,},//一同上传的数据  
		fileElementId:"fileupload",
		error: function(data, status, e){},
		success:function(data, status){
			var json = jQuery.parseJSON(data);  
			if(json.success){
				alert(json.msg);
				$("#fileupload").val("");
			}else{
				alert(json.msg);
				$("#fileupload").val("");
			}
		}
 	});
}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<!-- <div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>批量发起</small>
			</div>
		</div> -->
		<!-- 数据 -->
		<div class="am-g">
			<div class="am-u-sm-12">
				<form method="post" action="" class="am-form" id="form1" enctype="multipart/form-data">
					<%=model.ui.ListToString()%>
				</form>
			</div>
		</div>
	</div>
</body>
</html>