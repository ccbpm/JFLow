function xzxk(){
	var iframe = window.document.getElementById("Dtl_Frm_ShouLiShenChaBiaoDtl1"); 

	iframe.contentWindow.Option(1);
	
	}
//执行取不到从表的按钮，因为先执行的下面代码，后加载的从表
window.onload=function(){
	var divdtl = window.document.getElementById("dtlDiv");
	if(divdtl != null){
	var chirlds = window.document.getElementById("dtlDiv").childNodes;
	for (var i = 0;i<chirlds.size;i++){
		if(chirlds[i].nodeName = '#text'){
			
		}
	}
	};
	$("#dtlDiv img").each(function (){
		alert($(this).attr('title'));
		if($(this).attr('title')== "保存"){
			
		}
			//$(this).("display","block"); //标题输入框
	});
}