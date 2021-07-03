
/*
1. 该页面,是被引用到 /WF/MyFlowGener.htm, /WF/CCForm/FrmGener.htm 里面的.
2. 这里方法大多是执行后，返回json ,可以被页面控件调用. 
*/
function funDemo() {
    alert("我被执行了。");
    return;
}
//公文按钮
function gongwen() {
	
	$("#iframe_item").attr('src',$("#iframe_item").attr('src'));
	$("#wps_item").css('display','block');
	
//	window.location.href="../DataUser/linux/src/wps/wps2.html"; 
    return;
}
//发文、会签取的自动编号 type=1综合司发文;   type=2国家局发文;  type=3司局发文; type=4签报单;
function getBianHao(type,frmtable){
	var zihao = $("#TB_ZiHao").val();
    var nianfen = $("#TB_NianFen").val();
    
	//综合司发文
	if(type ==1){
	    var zongfa = $("#DDL_ZongFa").val();
	    var han=	$("#CB_Han").is(":checked");
	    if(han){
			han = 1;
		}else{
	    	han = 0;
	    }
	    var sql = "SELECT MAX(bianhao)+1 as BianHao FROM "+frmtable+" WHERE " +
	    		"ZIHAO='"+zihao+"' AND HAN='"+han+"' AND NIANFEN='"+nianfen+"' AND ZONGFA='"+zongfa+"'";
	}
	//国家局发文
	if(type ==2){
	    var OPINION = $("#DDL_OPINION").val();
	    var han=	$("#CB_Han").is(":checked");
	    if(han){
			han = 1;
		}else{
	    	han = 0;
	    }
	    var sql = "SELECT MAX(bianhao)+1 as BianHao FROM "+frmtable+" WHERE " +
	    		"ZIHAO='"+zihao+"' AND HAN='"+han+"' AND NIANFEN='"+nianfen+"' AND OPINION='"+OPINION+"'";
	}
	//司局发文
	if(type ==3){
	    var BuMenJianXie = $("#TB_BuMenJianXie").val();
	    var han=	$("#CB_Han").is(":checked");
	    if(han){
			han = 1;
		}else{
	    	han = 0;
	    }
	    var sql = "SELECT MAX(bianhao)+1 as BianHao FROM "+frmtable+" WHERE " +
	    		"ZIHAO='"+zihao+"' AND HAN='"+han+"' AND NIANFEN='"+nianfen+"' AND BUMENJIANXIE='"+BuMenJianXie+"'";
	}
	//签报
  if(type ==4) {
		
	    var sql = "SELECT MAX(bianhao)+1 as BianHao FROM "+frmtable+" WHERE " +
	    		"ZIHAO='"+zihao+"' AND NIANFEN='"+nianfen+"'";
	}
	var bh=DBAccess.RunSQLReturnTable(sql);
	var bianhao=1;
	if(bh.length>0){
		if(bh[0].BianHao !=0 && bh[0].BianHao!=null && bh[0].BianHao!=""){
			bianhao=parseInt(bh[0].BianHao);
	    }
	}
	$("#TB_BianHao").val(bianhao);
}

function LookFaWen() {
	var WorkID = GetQueryString("WorkID");
	var FK_Node = GetQueryString("FK_Node");
	var sql = "SELECT WorkID,FK_Flow,FK_Node,Title FROM wf_generworkflow where WorkID in (SELECT Tag1 FROM sys_frmeledb WHERE RefPKVal ="+WorkID+" and EleID='GuanLianWenJian')";
	var gwf=DBAccess.RunSQLReturnTable(sql);
	var data="";
	if(gwf.length>0){
			
	for(var i = 0;i < gwf.length;i++){
		var url="./MyViewGener.htm?WorkID="+gwf[i].WorkID+"&FK_Flow="+gwf[i].FK_Flow+"&FK_Node="+gwf[i].FK_Node;
		if(FK_Node=='1301'){
			data +=" <a  style='cursor: pointer;' href='"+url+"'>"+gwf[i].Title+"</a> <span  onclick='DeletePoPVal("+gwf[i].WorkID+")'style='cursor: pointer;color: black;'>×</span>" +
			"</br>";
		}else{
		data +=" <a  style='cursor: pointer;' href='"+url+"'>"+gwf[i].Title+"</a> </br>";
		}
	}
	document.getElementById("GuanLianWenJian_mtags").innerHTML = data;
	return;
	}
	
	document.getElementById("GuanLianWenJian_mtags").innerHTML = data;
    return;
}


function DeletePoPVal(workid) {
	DBAccess.RunSQL("DELETE FROM  sys_frmeledb where Tag1= '"+workid+"'");
	LookFaWen();
}

//FK_MapData,附件属性，RefPK,FK_Node
function afterDtlImp(FK_MapData, frmAth, newOID, FK_Node, oldOID,oldFK_MapData) {
    //处理从表附件导入的事件.

}

function CompareData() {
    if ($("#TB_StartTime").val() > $("#TB_EndTime").val()) {
        alert("开始时间不能大于结束时间");
        return false;
    }
    return true;
}

function HeJi() {
    var bmhj = $("#BMHJ").val();
    var jthj = $("#JTHJ").val();
    var bzhj = $("#BZHJ").val();
    $("#HeJj").val(bmhj + jthj + bzhj);


}

function GetShiJian(keyOfEn) {
    alert($("#TB_" + keyOfEn + "_0").val());
}


function IsSaveDtl() {
    var regInput = true;
    //获取页面的所有IFrame
    var frames = $("#divCCForm").find("iframe");
    var dtlFrames = $.grep(frames, function (frame,idx) {
        if (frame.id.indexOf("Dtl_") == 0)
            return frame;
    });

    //循环从表IFrame，如果有未填的返回false
    $.each(dtlFrames, function (idx, dtlFrame) {
        var mustInput = $(this).contents().find(".errorInput");

        if (mustInput.length > 0) {
            regInput = false;
            return;
        }
    });

return regInput;
}    
/***  计算两个请假天数 begin ***/
function DateDiffExt() {

    var d1 = $("#TB_QJSJQ").val();

    d1 = d1.substring(0, 10);
    var d2 = $("#TB_QJSJZ").val();
    d2 = d2.substring(0, 10);

    var days = DateDiff(d1, d2);
    //请假天数.
    $("#TB_QJTS").val(days);

}

function DateDiff(date1, date2) {

    var regexp = /^(\d{1,4})[-|\.]{1}(\d{1,2})[-|\.]{1}(\d{1,2})$/;
    var monthDays = [0, 3, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1];
    regexp.test(date1);
    var date1Year = RegExp.$1;
    var date1Month = RegExp.$2;
    var date1Day = RegExp.$3;

    regexp.test(date2);
    var date2Year = RegExp.$1;
    var date2Month = RegExp.$2;
    var date2Day = RegExp.$3;

    if (validatePeriod(date1Year, date1Month, date1Day, date2Year, date2Month, date2Day)) {

        firstDate = new Date(date1Year, date1Month, date1Day);
        secondDate = new Date(date2Year, date2Month, date2Day);

        result = Math.floor((secondDate.getTime() - firstDate.getTime()) / (1000 * 3600 * 24));
        for (j = date1Year; j <= date2Year; j++) {
            if (isLeapYear(j)) {
                monthDays[1] = 2;
            } else {
                monthDays[1] = 3;
            }
            for (i = date1Month - 1; i < date2Month; i++) {
                result = result - monthDays[i];
            }
        }
        return result;
    }

    alert('对不起第一个时间必须小于第二个时间，谢谢！');

}

//判断是否为闰年
function isLeapYear(year) {
    if (year % 4 == 0 && ((year % 100 != 0) || (year % 400 == 0))) {
        return true;
    }
    return false;
}
//判断前后两个日期
function validatePeriod(fyear, fmonth, fday, byear, bmonth, bday) {
    if (fyear < byear) {
        return true;
    } else if (fyear == byear) {
        if (fmonth < bmonth) {
            return true;
        } else if (fmonth == bmonth) {
            if (fday <= bday) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    } else {
        return false;
    }
}

/***  计算两个请假天数 end ***/
/*** 自定义打印 ***/
function printFromToPDF(){
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
    data=data.replace('url@', '');
    //弹出框显示
    initModalSelf(data);
    $('#returnWorkModal').modal().show();
    
}
function initModalSelf(htmlData) {

    //初始化退回窗口的SRC.
    var html = '<div style=" height:auto;" class="modal fade" id="returnWorkModal" data-backdrop="static">' +
        '<div class="modal-dialog" style="border-radius:0px;width:920px;">'
        + '<div class="modal-content" id="viewModal" style="border-radius:0px;height:560px;">'
        + '<div class="modal-header" style="background-color:#f2f2f2;margin-bottom:8px">'
        + '<button id="ClosePageBtn" type="button" style="color:#000000;float: right;background: transparent;border: none;" data-dismiss="modal" aria-hidden="true">&times;</button>'
        + '<button id="MaxSizeBtn" type="button" style="color:#000000;float: right;background: transparent;border: none;" aria-hidden="true" >□</button>'
        + '<h4 class="modal-title" id="modalHeader" style="color:#000000;">提示信息</h4>'
        + '</div>'
        + '<div class="modal-body" style="margin:0px;padding:0px;height:260px">'
        + '<span>生成成功，文件位置：</span>'
        + '<span id="modal-bodyMain"></span>'
        + '</div>'
        + '</div><!-- /.modal-content -->'
        + '</div><!-- /.modal-dialog -->'
        + '</div>';

    if ($("#returnWorkModal").length == 0)
        $('body').append($(html));

    if ($("#returnWorkModal .modal-footer").length == 1)
        $("#returnWorkModal .modal-footer").remove();

    $("#returnWorkModal").on('hide.bs.modal', function () {
        setToobarEnable();
    });
    $("#MaxSizeBtn").click(function () {

        //按百分比自适应
        SetPageSize(100, 100);
    });
   
    var isFrameCross = window.location.href.indexOf(basePath)==-1 ? 1 : 0;

    $("#modal-bodyMain").html(htmlData);
}

/*** 自定义打印end ***/