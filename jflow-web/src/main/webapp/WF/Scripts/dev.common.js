//定义全局ajax
$.ajaxSetup({
	complete:function(XMLHttpRequest,textStatus){
        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，  
        if(sessionstatus=="timeout"){   
        	//如果超时就处理 ，指定要跳转的页面  
        	var tmpPath = XMLHttpRequest.getResponseHeader("basePath");
            if(tmpPath){
            	window.location.replace(tmpPath);   
            } else {
            	var local = window.location;    
        		var basePath = local.protocol+"//"+local.host+"/";
            	window.location.replace(basePath); 
            }   
        }
    }   
});