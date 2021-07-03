var subFlowNode = {};
 
/*
 * 1. 该JS文件被嵌入到了MyFlowGener.htm 的工作处理器中. 2. 开发者可以重写该文件处理通用的应用,比如通用的函数.
 * 
 */

// 转化拼音的方法
function StrToPinYin(str) {

	var handler = new HttpHandler("BP.WF.HttpHandler.WF_Admin_FoolFormDesigner");
	handler.AddPara("name", str);
	handler.AddPara("flag", "false");
	data = handler.DoMethodReturnString("ParseStringToPinyin");
	return data;
}

/*
 * 
 * 1. beforeSave、beforeSend、 beforeReturn、 beforeDelete 2
 * .MyFlowGener、MyFlowTree的固定方法，禁止删除 3.主要写保存前、发送前、退回前、删除前事件 4.返回值为 true、false
 * 
 */

// 保存前事件
function beforeSave(saveType) {
return true;

       
}

// 发生前事件
function beforeSend() {
	 var FlowNo=GetQueryString("FK_Flow");
        
	return true;
}

// 退回前事件
function beforeReturn() {
	return true;
}

// 删除前事件
function beforeDelete() {
	return true;
}

// 抄送阅读页面增加关闭前事件
function beforeCCClose() {
	return true;
}
// 发送 退回 移交等执行成功后转到 指定页面
var interval;
// 关闭弹出窗刷新页面
function WindowCloseReloadPage(msg) {
	if ($('#returnWorkModal:hidden').length == 0
			&& $('#returnWorkModal').length > 0) {
		$('#returnWorkModal').modal('hide');
	}

	// 增加msg的模态窗口
	// 初始化退回窗口的SRC.
	var html = '<div class="modal fade" id="msgModal" data-backdrop="static">'
			+ '<div class="modal-dialog">'
			+ '<div class="modal-content" style="border-radius: 0px;">'
			+ '<div class="modal-header" style="background:#f2f2f2;">'
			+ '<button type="button" class="close" id="btnMsgModalOK1" aria-hidden="true" style="color: #0000007a;display: none;">&times;</button>'
			+ '<h4 class="modal-title" style="color:#000;">提示信息</h4>'
			+ '</div>'
			+ '<div class="modal-body" style="text-align: left; word-wrap: break-word;">'
			+ '<div style="width:100%; border: 0px; height: 200px;overflow-y:auto" id="msgModalContent" name="iframePopModalForm"></div>'
			+ '<div style="text-align: right;">'
			+ ' <button type="button" id="btnMsgModalOK" class="btn" data-dismiss="modal">确定(30秒)</button >'
			+ '</div>' + '</div>' + '</div><!-- /.modal-content -->'
			+ '</div><!-- /.modal-dialog -->' + '</div>';

	$('body').append($(html));
	if (msg == null || msg == undefined)
		msg = "";
	msg = msg.replace("@查看<img src='/WF/Img/Btn/PrintWorkRpt.gif' >", '')

	$("#msgModalContent").html(msg.replace(/@/g, '<br/>').replace(/null/g, ''));
	var trackA = $('#msgModalContent a:contains("工作轨迹")');
	var trackImg = $('#msgModalContent img[src*="PrintWorkRpt.gif"]');
	trackA.remove();
	trackImg.remove();

	$('#btnMsgModalOK').bind(
			'click',
			function() {
				var id = window.parent.nthTabs.getActiveId();
				var idlist = id.split("TLJ");
				// console.log("==="+idlist);
				if (idlist.length > 0) {
					$('#' + idlist[1], parent.document).attr('src',
							$('#' + idlist[1], parent.document).attr('src'));
				}
				window.parent.nthTabs.delTab(id);
			});
	$('#btnMsgModalOK1').bind('click', function() {
		// 提示消息有错误，页面不跳转
		var msg = $("#msgModalContent").html();
		if (msg.indexOf("err@") == -1) {
			window.close();
		} else {
			setToobarEnable();
			$("#msgModal").modal("hidden");
		}

		if (window.parent != null && window.parent != undefined)
			window.parent.close();
		opener.window.focus();
	});

	$("#msgModal").modal().show();

	interval = setInterval("clock()", 1000);

}