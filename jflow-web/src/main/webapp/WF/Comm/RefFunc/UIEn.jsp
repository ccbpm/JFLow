<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	RefLeftModel rlm = new RefLeftModel(request,basePath);
	rlm.init();
	
	/* UIEnModel uem=new UIEnModel(request,response);
	uem.init(); */
	
	UIEnsModel uiem = new UIEnsModel(request,response);
	uiem.init();
	
	UIEnModel uem = new UIEnModel(request,response,rlm,uiem);
	uem.init();
	
	String ensName = request.getParameter("EnsName");
%>
<base target=_self  />
<script src="../Gener.js" type="text/javascript"></script>
<script type="text/javascript">

	closeWhileEscUp();

	function SelectAll(cb_selectAll) {
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
	
	function selectTab(tabTitle) {
		$('#nav-tab').tabs('select', tabTitle);
	}
	
	var currShow;

	function ShowEn(url, wName, h, w) {
		var s = "dialogWidth=" + parseInt(w) + "px;dialogHeight=" + parseInt(h)
				+ "px;resizable:yes";
		var val = window.showModalDialog(url, null, s);
		window.location.href = window.location.href;
	}

	function ImgClick() {
	}

	function OpenAttrs(ensName) {
		var url = '../Sys/EnsAppCfg.jsp?EnsName=' + ensName;
		var s = 'dialogWidth=680px;dialogHeight=480px;status:no;center:1;resizable:yes'
				.toString();
		val = window.showModalDialog(url, null, s);
		window.location.href = window.location.href;
	}

	//在右侧框架中显示指定url的页面
	function OpenUrlInRightFrame(ele, url) {
		if (ele != null && ele != undefined) {
			//if (currShow == $(ele).text()) return;

			currShow = $(ele).parents('li').text();//有回车符

			$.each($(ele).parents('ul').children('li'), function(i, e) {
				$(e).children('div').css('font-weight',
						$(e).text() == currShow ? 'bold' : 'normal');
			});

			$('#rightFrame').empty();
			$('#rightFrame').append(
					'<iframe scrolling="auto" frameborder="0"  src="' + url
							+ '" style="width:100%;height:100%;"></iframe>');
		}
	}
	
	//点击保存
	function onSave(){
		//移除disable,后台方可取值
		$("#DDL_MyDataType").removeAttr("disabled");
		$("#DDL_FK_Dept").removeAttr("disabled");
		$("#DDL_LGType").removeAttr("disabled");
		var length = $('#nav-tab').length;
		var tab;
		var index;
		if(length>0){
			//console.info('length大于0');
			//window.alert("length大于0的");
	    	tab = $('#nav-tab').tabs('getSelected');
        	index = $('#nav-tab').tabs('getTabIndex',tab);
		}else{
			index="";
		}
		// 截取当前url中“?”后面的字符串
		var param = window.location.search;
		var url = "<%=basePath%>DES/save.do"+param+"&index="+index;
		// url:http://localhost:8080/jflow-web/DES/save.do?EnsName=BP.WF.Template.NodeSheets&PK=102&EnName=BP.WF.Template.NodeSheet&tab=0&index=4
		
		//alert('url:'+url);
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	
	//点击保存并关闭
	function onSaveOrClose(){
		//移除disable,后台方可取值
		$("#DDL_MyDataType").removeAttr("disabled");
		$("#DDL_FK_Dept").removeAttr("disabled");
		$("#DDL_LGType").removeAttr("disabled");
		var param = window.location.search;
		var url = "<%=basePath%>DES/saveorclose.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	
	//点击删除
	function onDelete(){
		if(!confirm("确认删除吗？")){
			return;
		}
		var param = window.location.search;
		$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>DES/Delete.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	
	//点击新建
	function onNew(){
		var  str = "<%=ensName%>";
		if(str==null || str==""){
			var param = window.location.search;
			window.location = "UIEn.jsp"+param.substring(0, param.indexOf('&'));
		} else{
			window.location = "UIEn.jsp?EnsName="+"<%=ensName%>";
		}
	}
	
	//点击保存新建
	function onSaveAndNew(){
		var param = window.location.search;
		var url = "<%=basePath%>DES/SaveAndNew.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	function WinOpen(url) {
        var newWindow = window.open(url, 'z', 'height=500,width=600,top=100,left=200,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        newWindow.focus();
        return;
    }

	window.onload=function(){
		if(<%=uem.getHiddenLeft()%>){
			 $('body').layout('remove','west');
		}
		if(<%=uem.getHiddenTop()%>){
			 $('body').layout('remove','north');
		}
		
		var tabts = $("#rightFrame a.tabs-inner");

		$.each(tabts, function(i) {
			$(this).attr('title',$("#rightFrame div[data-g='" + $(this).text() + "']").attr('data-gd'));
		});

		//选中上次保存之前当前打开的标签
		
		var urlParams = location.search.substr(1).split('&');
		var index ;
		$.each(urlParams, function() {
			var a = this.split('=');
			if (a[0] == 'tab') {
				index = a[1];
			}
		});
		
		$('#nav-tab').tabs('select', index);
		
		$("#leftFrame, #rightFrame").show();
	}
	//初始化加载，解决一些兼容问题
	$(document).ready(function(){
		$("#rightFrame").resize();
	}); 
</script>
<body class="easyui-layout" leftmargin="0" topmargin="0" onkeypress="javascript:Esc();">
	<form method="post" action="" class="am-form" id="form1">
		<div id="leftFrame" data-options="region:'west',title:'功能列表',split:true" style="width:200px;padding:5px;">
			<%=rlm.Pub1.toString() %>
		</div>
		<div id="rightFrame" data-options="region:'center',noheader:true" style="display:none;">
			<div class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north',noheader:true,split:false,border:false" style="height: 30px;
			        padding: 2px; background-color: #E0ECFF">
			        <%=uiem.ToolBar1.toString() %>
			    </div>
				<div data-options="region:'center',noheader:true,border:false">
					<%=uiem.UCEn1.pub.toString() %>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
