<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	RefLeftModel RefLeft = new RefLeftModel(request, basePath);
	RefLeft.init();

	DtlModel dtl = new DtlModel(request, response, basePath);
	dtl.init();
%>
<script type="text/javascript">
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
//在右侧框架中显示指定url的页面
function OpenUrlInRightFrame(ele, url) {
    if (ele != null && ele != undefined) {
        //if (currShow == $(ele).text()) return;

        currShow = $(ele).parents('li').text();//有回车符
        
        $.each($(ele).parents('ul').children('li'), function (i, e) {
            $(e).children('div').css('font-weight', $(e).text() == currShow ? 'bold' : 'normal');
        });

        $('#rightFrame').empty();
        $('#rightFrame').append('<iframe scrolling="no" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>');
    }
}
</script>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
	<form method="post" action="" class="am-form" id="form1">
		<div id="leftFrame" data-options="region:'west',title:'功能列表',split:true" style="width: 200px; padding: 5px">
			<%=RefLeft.Pub1.toString() %>
		</div>
		<input type="hidden" id="FormHtml" name="FormHtml" value="">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north',noheader:true,split:false,border:false" style="height: 30px;
			        padding: 2px; background-color: #E0ECFF">
			        <%=dtl.toolBar.toString() %>
			    </div>
			    <div data-options="region:'center',noheader:true,border:false">
			    	<%=dtl.UCSys1.ListToString() %>
			    	<%=dtl.UCSys2.toString() %>
			    </div>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
//var fields = JSON.stringify($("input[name^='TB_Url_879']").serializeArray());
function onSave(){
	var param = window.location.search;
	$("#FormHtml").val($("#form1").html());
	var url = "<%=basePath%>DES/DtlSave.do"+param;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
function onDel(){
	if(confirm("您确定要执行删除吗？")){	
	    
	    var size = $("input[name^='CB_']:checked").size();
	    if(size == 0){
			alert("请选择要删除的项！");
			return;
		}	
		var param = window.location.search;
		
		$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>DES/DtlDel.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	};
}
</script>
</html>