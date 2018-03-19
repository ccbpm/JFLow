function addModal(e){
	title = e.title?e.title:'提示';
	url = e.url?e.url:'';
	content = e.content?e.content:'';
	top.dialog({
		title: title,
		url:url,
		content:content,
	    okValue: '确定',
	    ok: function () {
	        this.title('提交中…');
	        return false;
	    },
	    cancelValue: '取消',
	    cancel: function () {}
	}).width(600).showModal();
};
function alertMessage(message){
	var msg = $.messager.show(message, {placement: 'top',type:'info',icon:'info-sign'});
	/*var md = top.dialog({
		id : '_platform_message',
	    content: message
	});
	md.show();
	setTimeout(function () {
	    md.close().remove();
	}, 2000);*/
};
function clearBlank(){//当失去焦点时，去除输入框前后空格
	$("input[type='text']").blur(function(){
		$(this).val($(this).val().replace(/^\s+|\s+$/g,""));
	});
}
$(function(){
	//当失去焦点时，去除输入框前后空格
	clearBlank();
	
});

