window.onload = function(){

$("#banlifankui").css("display","block");
        $("#openimg").attr("src", "../WF/Img/Min.gif");
        
         var FK_Node = GetQueryString("FK_Node");
    
     //生成文号
 /*if(FK_Node == '2501'){
 	$("#biaotii").css("display","block");
    $("#biaotis").css("display","none");
 }else{
 	$("#biaotii").css("display","none");
    $("#biaotis").css("display","block");
    document.getElementById('biaotin').innerHTML = $("#TB_BiaoTi").val();
 }*/
 var tea=$("#biaotin").text();
  $("#biaotin").text(tea+$("#TB_SuoShuSi").val());  
 //显示办理意见
    var textarea = $('#TB_BanLiFanKui').val();
    document.getElementById('banlifankui').innerHTML = textarea;
    
    
    
    
    //弹出窗口
   window.wxc = window.wxc || {};
	window.wxc.xcConfirm = function(popHtml, type, options) {
	    var btnType = window.wxc.xcConfirm.btnEnum;
		var eventType = window.wxc.xcConfirm.eventEnum;
		var popType = {
			input: {
				title: "填写意见",
				icon: "",
				btn: btnType.ok
			}
		};
		var itype = type ? type instanceof Object ? type : popType[type] || {} : {};//格式化输入的参数:弹窗类型
		var config = $.extend(true, {
			//属性
			title: "", //自定义的标题
			icon: "", //图标
			btn: btnType.ok, //按钮,默认单按钮
			//事件
			onOk: $.noop,//点击确定的按钮回调
			onCancel: $.noop,//点击取消的按钮回调
			onClose: $.noop//弹窗关闭的回调,返回触发事件
		}, itype, options);
		
		var $txt = $("<p>").html(popHtml);//弹窗文本dom
		var $tt = $("<span>").addClass("tt").text(config.title);//标题
		var icon = config.icon;
		var $icon = icon ? $("<div>").addClass("bigIcon").css("backgroundPosition",icon) : "";
		var btn = config.btn;//按钮组生成参数
		
		var popId = creatPopId();//弹窗索引
		
		var $box = $("<div>").addClass("xcConfirm");//弹窗插件容器
		var $layer = $("<div>").addClass("xc_layer");//遮罩层
		var $popBox = $("<div>").addClass("popBox");//弹窗盒子
		var $ttBox = $("<div>").addClass("ttBox");//弹窗顶部区域
		var $txtBox = $("<div>").addClass("txtBox");//弹窗内容主体区
		var $btnArea = $("<div>").addClass("btnArea");//按钮区域
		
		var $ok = $("<a>").addClass("sgBtn").addClass("ok").text("确定");//确定按钮
		var $cancel = $("<a>").addClass("sgBtn").addClass("cancel").text("取消");//取消按钮
		var $input = $("<input>").addClass("inputBox");//输入框
		var $clsBtn = $("<a>").addClass("clsBtn");//关闭按钮
		
		//建立按钮映射关系
		var btns = {
			ok: $ok,
			cancel: $cancel
		};
		
		init();
		
		function init(){
			//处理特殊类型input
			if(popType["input"] === itype){
				$txt.append($input);
			}
			
			creatDom();
			bind();
		}
		
		function creatDom(){
			$popBox.append(
				$ttBox.append(
					$clsBtn
				).append(
					$tt
				)
			).append(
				$txtBox.append($icon).append($txt)
			).append(
				$btnArea.append(creatBtnGroup(btn))
			);
			$box.attr("id", popId).append($layer).append($popBox);
			$("body").append($box);
		}
		
		function bind(){
			//点击确认按钮
			$ok.click(doOk);
			
			//回车键触发确认按钮事件
			$(window).bind("keydown", function(e){
				if(e.keyCode == 13) {
					if($("#" + popId).length == 1){
						doOk();
					}
				}
			});
			
			//点击取消按钮
			$cancel.click(doCancel);
			
			//点击关闭按钮
			$clsBtn.click(doClose);
		}

		//确认按钮事件
		function doOk(){
			var $o = $(this);
			var v = $.trim($input.val());
			if ($input.is(":visible"))
		        config.onOk(v);
		    else
		        config.onOk();
			$("#" + popId).remove(); 
			config.onClose(eventType.ok);
		}
		
		//取消按钮事件
		function doCancel(){
			var $o = $(this);
			config.onCancel();
			$("#" + popId).remove(); 
			config.onClose(eventType.cancel);
		}
		
		//关闭按钮事件
		function doClose(){
			$("#" + popId).remove();
			config.onClose(eventType.close);
			$(window).unbind("keydown");
		}
		
		//生成按钮组
		function creatBtnGroup(tp){
			var $bgp = $("<div>").addClass("btnGroup");
			$.each(btns, function(i, n){
				if( btnType[i] == (tp & btnType[i]) ){
					$bgp.append(n);
				}
			});
			return $bgp;
		}

		//重生popId,防止id重复
		function creatPopId(){
			var i = "pop_" + (new Date()).getTime()+parseInt(Math.random()*100000);//弹窗索引
			if($("#" + i).length > 0){
				return creatPopId();
			}else{
				return i;
			}
		}
	};
	
	//按钮类型
	window.wxc.xcConfirm.btnEnum = {
		ok: parseInt("0001",2), //确定按钮
		cancel: parseInt("0010",2), //取消按钮
		okcancel: parseInt("0011",2) //确定&&取消
	};
	
	//触发事件类型
	window.wxc.xcConfirm.eventEnum = {
		ok: 1,
		cancel: 2,
		close: 3
	};
	
	//弹窗类型
	window.wxc.xcConfirm.typeEnum = {
		input: "input"
	};
    var rong=$("#TB_ZhengWenNeiRong").val();
    $("#contentId").val(rong);
    
     var FK_Node = GetQueryString("FK_Node");
    
    if(FK_Node == "2504"||type=="1"){
      var imagefileqf= document.getElementById('texthtml');
      
       $("#contentId").css("display","none");
      
      imagefileqf.innerHTML=$("#TB_ZhengWenNeiRong").val();
    }else{
    CKEDITOR.replace('contentId',{   
	 height: '250px', 
	 width: '100%',  
	});
    }
   
   
}


    
//填写意见弹窗
function AddMessage(){
	var txt=  "";
					window.wxc.xcConfirm(txt, "input",{
						onOk:function(name){
							if (name!=null && name!="")

{
	 var user = new WebUser();
    console.log(user);
    
    var myDate = new Date();
var time = myDate.toLocaleString( );
//获取输入框
var textarea = $('#TB_BanLiFanKui').val();
 
var vals = '<div style="border-bottom:1px dashed #000;width:800px;padding-top:20px;"><label style="color:#000;padding-left:20px">'+name+'</lable><br />'
+'<label style="padding-left:495px;color:#000;"> ------  '+user.Name+'  ' +time+'</label></div><br />';

textarea = textarea+vals;

document.getElementById('banlifankui').innerHTML = textarea;

$('#TB_BanLiFanKui').val(textarea);

}
						}
					});


}

//办理意见展开
function openbanli(){
	if($("#banlifankui").css("display") == "none"){
    	$("#banlifankui").css("display","block");
        $("#openimg").attr("src", "../WF/Img/Min.gif");
    }else{
    	$("#banlifankui").css("display","none");
         $("#openimg").attr("src", "../WF/Img/Max.gif");
    }
}

//验证必填项
function CheckSend(){
	var TB_BiaoTi=$("#TB_BiaoTi").val();
	if (TB_BiaoTi==null||TB_BiaoTi==""){
		alert("主题不能为空！");
		return false;
	}
    
    var TB_ZhuSong=$("#TB_ZhuSong").val();
	if (TB_ZhuSong==null||TB_ZhuSong==""){
		alert("主送不能为空！");
		return false;
	}
    
    var TB_ChaoSong=$("#TB_ChaoSong").val();
	if (TB_ChaoSong==null||TB_ChaoSong==""){
		alert("抄送不能为空！");
		return false;
	}
    
    var TB_Tel=$("#TB_Tel").val();
	if (TB_Tel==null||TB_Tel==""){
		alert("电话不能为空！");
		return false;
	}
   
    return true;

}
var dataurl="";
function sendFile(){
    var fk_bill = GetQueryString("FK_Bill");
    var nodeID = GetQueryString("FK_Node");
    var workID = GetQueryString("WorkID");
    var flowNo = GetQueryString("FK_Flow");
    var fid = GetQueryString("FID");
    printType = GetQueryString("PrintType");
    //初始化页面信息
    var handler = new HttpHandler("BP.WF.HttpHandler.WF_WorkOpt");
    handler.AddUrlData();
    var data = handler.DoMethodReturnString("PrintDoc_Init");
    if (data.indexOf('err@') == 0) {
        alert(data);
        return;
    }
    data=data.replace('rtf@', '');
    dataurl=data.replace('url@', '');
    //alert(dataurl);  
  
    $("#send_item").css('display','block');
    $('#iframe_send_item').attr('src', $('#iframe_send_item').attr('src')); 
}

// 打印通知公告。
function Print() {
	var workID = GetQueryString("WorkID");
	var url = "/AppTLJ/PrintTemplate/TongZhiGongGao/Default.html?WorkID="
		+ workID;
	window.open(url);
}
