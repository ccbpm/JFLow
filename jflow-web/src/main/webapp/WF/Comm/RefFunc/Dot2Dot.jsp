<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	RefLeftModel RefLeft = new RefLeftModel(request, basePath);
	RefLeft.init();

	Dot2DotModel Dot2Dot = new Dot2DotModel(request, response, basePath);
	Dot2Dot.init();
%>
<base target=_self  />
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
			        <%=Dot2Dot.toolBar.toString() %>
			    </div>
			    <div data-options="region:'center',noheader:true,border:false">
			        <%=Dot2Dot.UCSys.ListToString() %>
			    </div>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
function onChange(){
	var url = window.location.href;
	if(url.indexOf("ShowWay=")>0){
		url = url.replace("&ShowWay=", "&1=");
	}
	var DDL_Group = $("#DDL_Group").val();
	window.location.href = url + "&ShowWay=" + DDL_Group;
	//alert(DDL_Group);
}
function onSave(){
	var param = window.location.search;
	var DDL_Group = $("#DDL_Group").val();
	
	$("#FormHtml").val($("#form1").html());
	//alert($("#form1").serialize());
	var url = "<%=basePath%>DES/Dot2DotSave.do"+param+"&DDL_Group="+DDL_Group;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
</script>
</html>