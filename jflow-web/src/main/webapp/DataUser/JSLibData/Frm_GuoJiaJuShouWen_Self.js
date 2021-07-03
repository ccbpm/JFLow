window.onload = function(){
debugger;
 var FK_Node = GetQueryString("FK_Node");
 if(FK_Node == '1701' && !document.getElementById("TB_LaiWenHao").value){
 	$("#TB_LaiWenHao").val("【】");
 }

$("#banlifankui").css("display","block");
        $("#openimg").attr("src", "../WF/Img/Min.gif");
        
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
    
    /*--  TB_ZiHao  DDL_OPINION CB_Han TB_NianFen TB_BianHao〔〕
    -- */
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + "〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    
    
    //正文号
     $("#TB_ZiHao").change(function() { 
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + "〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    });
    $("#TB_NianFen").change(function() { 
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + "〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    });
    $("#TB_BianHao").change(function() { 
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + "〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    });
    
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
	var TB_ZiHao=$("#TB_ZiHao").val();
	var TB_NianFen=$("#TB_NianFen").val();
    var TB_BianHao=$("#TB_BianHao").val();
	if (TB_ZiHao==null||TB_ZiHao==""||TB_NianFen==null||TB_NianFen==""||TB_BianHao==null||TB_BianHao==""){
		alert("收文号不能为空！");
		return false;
	}
   var TB_LaiWenJiGuan=$("#TB_LaiWenJiGuan").val();
	if (TB_LaiWenJiGuan==null||TB_LaiWenJiGuan==""){
		alert("来文机关不能为空！");
		return false;
	}
    
    var TB_LaiWenHao=$("#TB_LaiWenHao").val();
	if (TB_LaiWenHao==null||TB_LaiWenHao==""){
		alert("来文号不能为空！");
		return false;
	}
    
    var TB_ZhuTi=$("#TB_ZhuTi").val();
	if (TB_ZhuTi==null||TB_ZhuTi==""){
		alert("主题不能为空！");
		return false;
	}
    
    
    
	return true;
}

//设置选择承办按钮
function chengban(){
var WorkID = GetQueryString("WorkID");
var FK_Node = GetQueryString("FK_Node");
	SelectOpenIt(0,FK_Node+'_011_0','011',WorkID,FK_Node,'017',0);
}