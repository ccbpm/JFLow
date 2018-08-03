//当前项目路径
var basePath = basePath();

function basePath()
{
	var rowUrl = window.document.location.href;	
	var path=rowUrl.substring(0,rowUrl.indexOf('/WF')+1)
	if(rowUrl.indexOf("/WF")==-1){
		path=rowUrl.substring(0,rowUrl.indexOf('/jflow-web')+11);
		
	}	
	return path;
	
	
	
	
	var curWwwPath = window.document.location.href;		
	var pathName =  window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0,pos);
	var projectName = pathName.substring(0,pathName.substr(1).indexOf('/WF')+1);
	
	var path= localhostPaht + projectName ;
	
	
	
}



//For .net 后台的调用的url ,  java的与.net的不同.
var plant = "JFlow";
var url = window.location.href;
var Handler =  url.substring(0,url.lastIndexOf('/')+1)+"ProcessRequest.do";
var MyFlow = url.substring(0,url.lastIndexOf('/')+1)+"MyFlow/ProcessRequest.do";
/*var Handler ;*/

//公共方法
function Handler_AjaxQueryData(param, callback, scope, method, showErrMsg) {
    if (!method) method = 'GET';
    $.ajax({
        type: method, //使用GET或POST方法访问后台
        dataType: "text", //返回json格式的数据
        contentType: "application/json; charset=utf-8",
        url: Handler, //要访问的后台地址
        data: param, //要发送的数据
        async: true,
        cache: false,
        complete: function () { }, //AJAX请求完成时隐藏loading提示
        error: function (XMLHttpRequest, errorThrown) {
            callback(XMLHttpRequest);
        },
        success: function (msg) {//msg为返回的数据，在这里做数据绑定
            var data = msg;
            callback(data, scope);
        }
    });
}

//公共方法
function Handler_AjaxPostData(param, callback, scope) {
    $.post(Handler, param, callback);
}