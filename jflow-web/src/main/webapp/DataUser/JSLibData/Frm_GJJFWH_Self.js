window.onload = function(){
LookFaWen();
$("#banlifankui").css("display","block");
        $("#openimg").attr("src", "../WF/Img/Min.gif");

 //设置表单元素的显示和隐藏
 var user = new WebUser();
    console.log(user);

 var FK_Node = GetQueryString("FK_Node");
    
     //生成文号
 if(FK_Node == '1513' ||FK_Node == '1213'){
 wenhao();
 }
 
 //发起合法性
  if(FK_Node == '1506'){
  //合法性审查
 // var val1 =  $("input[name='RB_HFXXZ']:checked").val();
  //if(val1 == 0){
 //	SendHeFaXing();
 //	}
 }
 
  //公平性
 if($("#TB_GongPingXingYanZheng").val == 1){
 	$("#GPX").css("display","table-row");
 }
 
    
    if(FK_Node == '1501'||FK_Node == '1201'){
    //拟稿节点
    if(FK_Node == '1501' ){
    if(!document.getElementById("TB_ZiHao").value){
    $("#TB_ZiHao").val("国铁");
    }
    }else{
    if(!document.getElementById("TB_ZiHao").value){
    $("#TB_ZiHao").val("国铁综");
    }
    }
     	$("#TB_BiaoTi").css("display","block"); //标题输入框
        $("#TB_BiaoTiA").css("display","none"); //标题a标签
        $("#zhengwenanniu").css("display","table-row"); //起草正文按钮
        $("#zhengwen").css("display","none"); //主管部门审核和拟稿处事审核栏
        //显示关联文件
        //document.getElementById('GuanLian').innerHTML = document.getElementById('GuanLianWenJian_mtags').innerHTML;
    }else{
     $("#TB_BiaoTiA").text($("#TB_BiaoTi").val()); //a标签设置内容
     $("#zhengwenanniu").css("display","none"); //正文按钮
     $("#TB_BiaoTiA").css("display","block"); //标题a标签
        $("#zhengwen").css("display","table-row"); //正文
        //显示关联文件
         //document.getElementById('GuanLian').innerHTML = $('#TB_GuanLianNeiRong').val();
         
         //if(document.getElementById('GPXData').innerHTML == ''){
         //	$("#GPX").css("display","none");
         //}
         
    }
    
    
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
    
    /*--  TB_ZiHao  DDL_OPINION CB_Han TB_NianFen TB_BianHao
    -- */
    if($("#CB_Han").is(":checked")){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
    
    
    
    //正文号
     $("#TB_ZiHao").change(function() { 
    	if($("#CB_Han").is(":checked")){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
    });
    
    $("#DDL_OPINION").change(function() { 
    	if($("#CB_Han").is(":checked")){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
    });
    $("#CB_Han").change(function() {
    if($("#CB_Han").is(":checked")){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
    });
    $("#TB_NianFen").change(function() { 
    	if($("#CB_Han").is(":checked")){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
    });
    $("#TB_BianHao").change(function() { 
    	if($("#CB_Han").is(":checked")){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
    });
    
}

function GL(){
	$("#GuanLianWenJian_mtags").dblclick();
}

//设置选择会签按钮
function huiqian(){
var WorkID = GetQueryString("WorkID");
var FK_Node = GetQueryString("FK_Node");
	SelectOpenIt(0,FK_Node+'_006_0','006',WorkID,FK_Node,'015',0);
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


//发文编号自动生成

function wenhao(){

	var zihao=$("#TB_ZiHao").val();
	var bumen=$("#DDL_OPINION").find("option:selected").text();
    var han=	$("#CB_Han").is(":checked");
    if(han){
		han = 1;
	}else{
    	han = 0;
    }
    debugger;
    var nianfen = $("#TB_NianFen").val();
    var sql="select BianHao from frm_gjjfwh where ZiHao='"+zihao+"' and NianFen='"+nianfen+"' and Han = "+han+" and BuMenJianXie = '"+bumen+"' and BianHao !=''";
	var bh=DBAccess.RunSQLReturnTable(sql);
	var bianhao=1001;
	if(bh.length>0){
	
	bianhao=parseInt(bh[0].BianHao)+1;
	}
	$("#TB_BianHao").val(bianhao);
    if($("#CB_Han").val()){
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"函〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号" );
    }else{
    	$("#TB_ZhengWenHao").val($("#TB_ZiHao").val() + $("#DDL_OPINION").find("option:selected").text() +"〔"+ $("#TB_NianFen").val() +"〕"+ $("#TB_BianHao").val()+"号");
    }
}

//验证必填项
function CheckSend(){
var FK_Node = GetQueryString("FK_Node");
	debugger;
    //秘级
  var val1 =  $("input[name='RB_SFXZ']:checked").val();
  if(val1 == undefined){
  	alert("请设置文件秘级！");
		return false;
  }
  if(val1 == 0){
  	alert("秘级文件不能在系统中流转，请重新检查！");
		return false;
  }
  //信息公开形式
  var val2 =  $("input[name='RB_XXGKXS']:checked").val();
  if(val2 == undefined){
  	alert("请设置信息公开形式！");
		return false;
  }
  //合法性审查
  var val3 =  $("input[name='RB_HFXXZ']:checked").val();
  if(val3 == undefined){
  	alert("请设置合法性审查！");
		return false;
  }
  
    
    
	var biaoti=$("#TB_BiaoTi").val();
	var tel=$("#TB_Tel").val();
	if (biaoti==null||biaoti==""||tel==null||tel==""){
		alert("标题和电话不能为空！");
		return false;
	}
    var zhusong=$("#TB_ZhuSong").val();
	var chaosong=$("#TB_ChaoSong").val();
	if (zhusong==null||zhusong==""||chaosong==null||chaosong==""){
		alert("主送和抄送不能为空！");
		return false;
	}
    
    var TB_FenShu=$("#TB_FenShu").val();
    if (TB_FenShu==null||TB_FenShu==""){
		alert("印制份数不能为空！");
		return false;
	}

    if((FK_Node == '1501'|| FK_Node == 1201) && window.location.hostname != "172.16.7.1" ){
    var childWindow = $("#iframe_item")[0].contentWindow; 
  	var Zcontent = childWindow.getText();
     if(Zcontent==null||Zcontent==""){
    	alert("请起草正文！");
		return false;
    }
    }
    
    
    if(FK_Node == '1513'||FK_Node == '1213'){
    //var zihao=$("#TB_ZiHao").val();
	//var bumen=$("#DDL_OPINION").find("option:selected").text();
    //var han=	$("#CB_Han").is(":checked");
    //if(han){
	//	han = 1;
	//}else{
   // 	han = 0;
   // }
  //  debugger;
   // var nianfen = $("#TB_NianFen").val();
   /// 
  //  var bianhao= $("#TB_BianHao").val()
  // var sql="select BianHao from frm_gjjfwh where ZiHao='"+zihao+"' and NianFen='"+nianfen+"' and Han = "+han+" and BuMenJianXie = '"+bumen+" and BianHao=" + bianhao;
//	var bh=DBAccess.RunSQLReturnTable(sql);
 //   if(bh.length>0){
 //   	alert("编号"+bianhao+"已存在请重新设置！");
 //       return false;
 //   }
    }
    
    
	return true;
}

//发起文印
function SendWinYin(){
 var FK_Node = GetQueryString("FK_Node");
  var FK_Flow = GetQueryString("FK_Flow");
    var WorkID = GetQueryString("WorkID");
    
    
    //验证是否存在文印流程
    var winyinsql = "SELECT WorkId FROM wf_generworkflow WHERE PWorkId = '"+WorkID+"' AND FlowName = '文印单'";
    var wenyindata=DBAccess.RunSQLReturnTable(winyinsql);
    if(wenyindata.length > 0){
    	alert("已存在文印流程，加印功能开发中!");
        return;
    }
    
	var url= window.location.protocol+"//"+window.location.host+ "/WF/MyFlow.htm?FK_Flow=016"+"&PWorkID="+WorkID+"&PFlowNo="+FK_Flow+"&PNodeID="+FK_Node;
    
    console.log(url);
    
    var tab=window.parent.nthTabs.getTabList();
         var work= "#QCWYTLJ";
         for (var i=0;i<tab.length;i++){ 
             if(tab[i].id==work){
              window.parent.nthTabs.setActTab(tab[i].id);
              return;
             }
         }
    
    window.parent.nthTabs.addTab({id:"QCWYTLJ",title:"起草",url:url}).setActTab("#QCWYTLJ");
    
    
}

//发起公平性
function SendGongPingXing(){
 var FK_Node = GetQueryString("FK_Node");
  var FK_Flow = GetQueryString("FK_Flow");
    var WorkID = GetQueryString("WorkID");
    
    
    
    
	var url= window.location.protocol+"//"+window.location.host+ "/WF/MyFlow.htm?FK_Flow=023"+"&PWorkID="+WorkID+"&PFlowNo="+FK_Flow+"&PNodeID="+FK_Node;
    
    console.log(url);
    
    var tab=window.parent.nthTabs.getTabList();
         var work= "#QCGPXTLJ";
         for (var i=0;i<tab.length;i++){ 
             if(tab[i].id==work){
              window.parent.nthTabs.setActTab(tab[i].id);
              return;
             }
         }
    
    window.parent.nthTabs.addTab({id:"QCGPXTLJ",title:"起草",url:url}).setActTab("#QCGPXTLJ");
}

//发起合法性
function SendHeFaXing(){
 var FK_Node = GetQueryString("FK_Node");
  var FK_Flow = GetQueryString("FK_Flow");
    var WorkID = GetQueryString("WorkID");
    
    //验证是否存在合法性流程
    var winyinsql = "SELECT WorkId FROM wf_generworkflow WHERE PWorkId = '"+WorkID+"' AND FlowName = '合法性审查'";
    var wenyindata=DBAccess.RunSQLReturnTable(winyinsql);
    if(wenyindata.length > 0){
    	alert("已存在合法性审查流程，请勿重复发起!");
        return;
    }
    
    
	var url= window.location.protocol+"//"+window.location.host+ "/WF/MyFlow.htm?FK_Flow=024"+"&PWorkID="+WorkID+"&PFlowNo="+FK_Flow+"&PNodeID="+FK_Node;
    
    console.log(url);
    
    var tab=window.parent.nthTabs.getTabList();
         var work= "#HFXTLJ";
         for (var i=0;i<tab.length;i++){ 
             if(tab[i].id==work){
              window.parent.nthTabs.setActTab(tab[i].id);
              return;
             }
         }
    
    window.parent.nthTabs.addTab({id:"HFXTLJ",title:"起草",url:url}).setActTab("#HFXTLJ");
}

function OpenGPX(){
	if($("#GPX").css("display") == "none"){
    	$("#GPX").css("display","table-row");
        $("#TB_GongPingXingYanZheng").val = 1;
    }else{
    	$("#GPX").css("display","none");
        $("#TB_GongPingXingYanZheng").val = 0;
    }
}