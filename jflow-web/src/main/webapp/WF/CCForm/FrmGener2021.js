
//定义全局的变量
var pageData = {};//全局的参数变量
var frmData = {}; // 表单数据
var isReadonly = false;//表单方案是只读时的变化
webUser = typeof webUser == "undefined" || webUser==null? new WebUser() : webUser;//用户信息

var UserICon = getConfigByKey("UserICon", '../../DataUser/Siganture/'); //获取签名图片的地址
var UserIConExt = getConfigByKey("UserIConExt", '.jpg');  //签名图片的默认后缀

var currentUrl = window.location.href;

//初始化函数
$(function () {
    UserICon = UserICon.replace("@basePath", basePath);
    //增加css样式
    $('head').append('<link href="../../DataUser/Style/GloVarsCSS.css" rel="stylesheet" type="text/css" />');

    //初始化参数.
    initPageParam();
    //构造表单.
    GenerFrm(); //表单数据.

    //时间轴的表单增加打印单据按钮
    var wfNode = frmData["WF_Node"];
    var fromWorkOpt = GetQueryString("FromWorkOpt");
    if (fromWorkOpt == 2 && wfNode[0].PrintDocEnable == 1) {
        var PrintDocHtml = "<input type=button name='PrintDoc' value='打印单据' enable=true onclick='printDoc()' />";
        $("#topToolBar").append(PrintDocHtml);
    }
});
//打印单据
function printDoc() {
    WinOpen("../WorkOpt/PrintDoc.htm?FK_Node=" + pageData.FK_Node + "&FID=" + pageData.FID + "&WorkID=" + pageData.OID + "&FK_Flow=" + pageData.FK_Flow + "&s=" + Math.random() + "', 'dsdd'");
}


//设置不可以用.
function SetReadonly() {
    //设置保存按钮不可以用.
    $("#Btn_Save").attr("disabled", true);
    $('#CCForm').find('input,textarea,select').attr('disabled', false);
    $('#CCForm').find('input,textarea,select').attr('readonly', true);
    $('#CCForm').find('input,textarea,select').attr('disabled', true);
}



//从表在新建或者在打开行的时候，如果 EditModel 配置了使用卡片的模式显示一行数据的时候，就调用此方法. // IsSave 弹出页面关闭时是否要删除从表
function DtlFrm(ensName, refPKVal, pkVal, frmType, InitPage, FK_MapData, FK_Node, FID, IsSave, H) {
    // model=1 自由表单, model=2傻瓜表单.
    var pathName = document.location.pathname;
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    if (projectName.startsWith("/WF")) {
        projectName = "";
    }
    if (H == undefined || H < 600)
        H = 600;
    if (H > 1000)
        H = 1000;

    var url = projectName + '/WF/CCForm/DtlFrm.htm?EnsName=' + ensName + '&RefPKVal=' + refPKVal + "&FrmType=" + frmType + '&OID=' + pkVal + "&FK_MapData=" + FK_MapData + "&FK_Node=" + FK_Node + "&FID=" + FID + "&IsSave=" + IsSave;
    if (typeof ((parent && parent.OpenBootStrapModal) || OpenBootStrapModal) === "function") {
        OpenBootStrapModal(url, "editSubGrid", '编辑', 1000, H, "icon-property", false, function () { }, null, function () {
            if (typeof InitPage === "function") {
                InitPage.call();
            } else {
                alert("请手动刷新表单");
            }
        }, "editSubGridDiv", null, false);
    } else {
        window.open(url);
    }
}




var frmData = null;
function GenerFrm() {
    var urlParam = currentUrl.substring(currentUrl.indexOf('?') + 1, currentUrl.length);
    urlParam = urlParam.replace('&DoType=', '&DoTypeDel=xx');

    //隐藏保存按钮.
    if (currentUrl.indexOf('&IsReadonly=1') > 1 || currentUrl.indexOf('&IsEdit=0') > 1) {
        $("#Btn").hide();
    }
    var handler = new HttpHandler("BP.WF.HttpHandler.WF_CCForm");
    handler.AddUrlData(urlParam);
    handler.AddJson(pageData);
    var data = handler.DoMethodReturnString("FrmGener_Init");

    if (data.indexOf('err@') == 0) {
        layer.alert('装载表单出错,请查看控制台console,或者反馈给管理员.<br/>' + data);
        console.log(data);
        return;
    }

    try {
        frmData = JSON.parse(data);
    }
    catch (err) {
        alert(" frmData数据转换JSON失败:" + data);
        console.log(data);
        return;
    }

    //处理附件的问题 
    if (frmData.Sys_FrmAttachment.length != 0) {
        if (currentUrl.indexOf("/CCBill/") != -1) {
            Skip.addJs("../CCForm/Ath.js");
            Skip.addJs("../CCForm/JS/FileUpload/fileUpload.js");
            Skip.addJs("../Scripts/jquery-form.js");
            Skip.addJs("../../DataUser/OverrideFiles/Ath.js");
            $('head').append("<link href='../CCForm/JS/FileUpload/css/fileUpload.css' rel='stylesheet' type='text/css' />");
        } else {
            Skip.addJs("./Ath.js");
            Skip.addJs("./JS/FileUpload/fileUpload.js");
            Skip.addJs("../Scripts/jquery-form.js");
            Skip.addJs("../../DataUser/OverrideFiles/Ath.js");
            $('head').append("<link href='./JS/FileUpload/css/fileUpload.css' rel='stylesheet' type='text/css' />");
        }

    }



    //获取没有解析的外部数据源
    var uiBindKeys = frmData["UIBindKey"];
    if (uiBindKeys.length != 0) {
        //获取外部数据源 handler/JavaScript
        var operdata;
        for (var i = 0; i < uiBindKeys.length; i++) {
            var sfTable = new Entity("BP.Sys.SFTable", uiBindKeys[i].No);
            var srcType = sfTable.SrcType;
            if (srcType != null && srcType != "") {
                //Handler 获取外部数据源
                if (srcType == 5) {
                    var selectStatement = sfTable.SelectStatement;
                    if (plant == "CCFLow")
                        selectStatement = basePath + "/DataUser/SFTableHandler.ashx" + selectStatement;
                    else
                        selectStatement = basePath + "/DataUser/SFTableHandler" + selectStatement;
                    operdata = DBAccess.RunDBSrc(selectStatement, 1);
                }
                //JavaScript获取外部数据源
                if (srcType == 6) {
                    operdata = DBAccess.RunDBSrc(sfTable.FK_Val, 2);
                }
                frmData[uiBindKeys[i].No] = operdata;
            }
        }

    }

    //获得sys_mapdata.
    var mapData = frmData["Sys_MapData"][0];
    //根据表单类型不同生成表单.

    var isTest = GetQueryString("IsTest");
    var isFloolFrm = false;
    var isDevelopForm = false;
    if (isTest == "1") {

        var frmType = GetQueryString("FrmType");
        if (frmType == 'Develop') {
            $('head').append('<link href="../../DataUser/Style/ccbpm.css" rel="stylesheet" type="text/css" />');
            $('head').append('<link href="../../DataUser/Style/MyFlowGenerDevelop.css" rel="Stylesheet" />');
            if (currentUrl.indexOf("/CCBill/") != -1)
                Skip.addJs("../CCForm/FrmDevelop2021.js?ver=1");
            else
                Skip.addJs("./FrmDevelop2021.js?ver=1");
            isDevelopForm = true;
            GenerDevelopFrm(frmData, mapData.No); //开发者表单.

        }
        else {
            $('head').append('<link href="../../DataUser/Style/FoolFrmStyle/Default.css" rel="stylesheet" type="text/css" />');
            if (currentUrl.indexOf("/CCBill/") != -1)
                Skip.addJs("../CCForm/FrmFool2021.js?ver=" + Math.random());
            else
                Skip.addJs("./CCForm/FrmFool2021.js?ver=" + Math.random());
            GenerFoolFrm(frmData);
            isFloolFrm = true;
        }

    } else {
        if (mapData.FrmType == 0 || mapData.FrmType == 10) {
            $('head').append('<link href="../../DataUser/Style/FoolFrmStyle/Default.css" rel="stylesheet" type="text/css" />');
            if (currentUrl.indexOf("/CCBill/") != -1)
                Skip.addJs("../CCForm/FrmFool2021.js?ver=" + Math.random());
            else
                Skip.addJs("./CCForm/FrmFool2021.js?ver=" + Math.random());
            isFloolFrm = true;
            GenerFoolFrm(frmData);
        }
        if (mapData.FrmType == 8) {
            $('head').append('<link href="../../DataUser/Style/ccbpm.css" rel="stylesheet" type="text/css" />');
            $('head').append('<link href="../../DataUser/Style/MyFlowGenerDevelop.css" rel="Stylesheet" />');
            if (currentUrl.indexOf("/CCBill/") != -1)
                Skip.addJs("../CCForm/FrmDevelop2021.js?ver=1");
            else
                Skip.addJs("./FrmDevelop2021.js?ver=1");
            GenerDevelopFrm(frmData, mapData.No); //开发者表单.
            isDevelopForm = true;

        }
       
    }

    //表单名称.
    document.title = mapData.Name;
    var w = mapData.FrmW;
    if (isFloolFrm == true) {
        $('#ContentDiv').width(w);
        $('#ContentDiv').css("margin-left", "auto").css("margin-right", "auto");
    }

    // 加载JS文件 改变JS文件的加载方式 解决JS在资源中不显示的问题.
    var enName = frmData.Sys_MapData[0].No;
    loadScript("../../DataUser/JSLibData/" + enName + "_Self.js?t=" + Math.random());
    loadScript("../../DataUser/JSLibData/" + enName + ".js?t=" + Math.random());

    //如果是富文本编辑器
    if ($(".rich").length > 0) {
        var images_upload_url = "";
        var directory = "ND" + pageData.FK_Flow;
        var handlerUrl = "";
        if (plant == "CCFlow")
            handlerUrl = basePath + "/WF/Comm/Handler.ashx";
        else
            handlerUrl = basePath + "/WF/Comm/Sys/ProcessRequest.do";

        images_upload_url = handlerUrl + '?DoType=HttpHandler&DoMethod=RichUploadFile';
        images_upload_url += '&HttpHandlerName=BP.WF.HttpHandler.WF_Comm_Sys&Directory=' + directory;
        layui.extend({
            tinymce: '../Scripts/layui/ext/tinymce/tinymce'
        }).use('tinymce', function () {
            var tinymce = layui.tinymce;
            $(".rich").each(function (i, item) {
                var id = item.id;
                tinymce.render({
                    elem: "#" + id
                    , height: 200
                    , images_upload_url: images_upload_url
                });
            })

        });

    }

    //3.装载表单数据与修改表单元素风格.
    LoadFrmDataAndChangeEleStyle(frmData);

    layui.form.render();
    //4.解析表单的扩展功能
    AfterBindEn_DealMapExt(frmData);

    $.each($(".ccdate"), function (i, item) {
        var format = $(item).attr("data-info");
        if (format.indexOf("HH") != -1) {
            layui.laydate.render({
                elem: '#' + item.id,
                format: $(item).attr("data-info"), //可任意组合
                type: 'datetime',
                done: function (value, date, endDate) {
                    var data = $(this.elem).data();
                    $(this.elem).val(value);
                    if (data && data.ReqDay != null && data.ReqDay != undefined)
                        ReqDays(data.ReqDay);
                }
            });
        } else {
            layui.laydate.render({
                elem: '#' + item.id,
                format: $(item).attr("data-info"), //可任意组合
                done: function (value, date, endDate) {
                    var data = $(this.elem).data();
                    $(this.elem).val(value);
                    if (data && data.ReqDay != null && data.ReqDay != undefined)
                        ReqDays(data.ReqDay);
                }
            });
        }

    })

    

    //星级评分事件
    var scoreDiv = $(".score-star");
    $.each(scoreDiv, function (idex, item) {
        var divId = $(item).attr("id");
        var KeyOfEn = divId.substring(3);//获取字段值
        $("#Star_" + KeyOfEn + " img").click(function () {
            var index = $(this).index() + 1;
            $("#Star_" + KeyOfEn + " img:lt(" + index + ")").attr("src", "../Style/Img/star_2.png");
            $("#SP_" + KeyOfEn + " strong").html(index + "  分");
            $("#TB_" + KeyOfEn).val(index);//给评分的隐藏input赋值
            index = index - 1;
            $("#Star_" + KeyOfEn + " img:gt(" + index + ")").attr("src", "../Style/Img/star_1.png");
        });
    });

}


//打开从表的从表
function DtlFoolFrm(dtl, refPK, refOID) {

    var url = 'DtlFoolFrm.htm?EnsDtl=' + dtl + '&RefPK=' + refPK + '&RefOID=' + refOID;
    alert('这里没有实现打开iurl ' + url);

    //引入了刘贤臣写的东西，一直缺少东西.可否改进一下，弄个稳定的？ @代国强.
    OpenEasyUiDialog(url, "eudlgframe", "编辑", 600, 450, "icon-edit", true, null, null, null, function () {
        //   window.location.href = window.location.href;
    });

}

//保存
function Save(scope) {

    //保存从表数据
    $("[name=Dtl]").each(function (i, obj) {
        var contentWidow = obj.contentWindow;
        if (contentWidow != null && contentWidow.SaveAll != undefined && typeof (contentWidow.SaveAll) == "function") {
            IsSaveTrue = contentWidow.SaveAll();

        }
    });

    //审核组件
    if ($("#WorkCheck_Doc").length == 1) {
        //保存审核信息
        SaveWorkCheck();
    }

  
    var handler = new HttpHandler("BP.WF.HttpHandler.WF_CCForm");
    handler.AddPara("OID", pageData.OID);
    var params = getFormData(true, true);
    $.each(params.split("&"), function (i, o) {
        var param = o.split("=");
        if (param.length == 2 && validate(param[1])) {
            handler.AddPara(param[0], decodeURIComponent(param[1], true));
        } else {
            handler.AddPara(param[0], "");
        }
    });
    handler.AddPara("FK_MapData", pageData.FK_MapData);
    var data = handler.DoMethodReturnString("FrmGener_Save");

    if (data.indexOf('err@') == 0) {
        $('#Message').html(data.substring(4, data.length));
        $('.Message').show();
        return false;
    }

    if (scope != "btnsave")
        window.location.href = window.location.href;
    return true;


}


/**
 * 初始化获取网页数据
 */
function initPageParam() {
    pageData.FK_Flow = GetQueryString("FK_Flow");
    pageData.FK_Node = GetQueryString("FK_Node");
    pageData.FK_Node = pageData.FK_Node == null || pageData.FK_Node == undefined ? 0 : pageData.FK_Node;
    pageData.FID = GetQueryString("FID") == null ? 0 : GetQueryString("FID");
    var oid = GetQueryString("WorkID");
    if (oid == null || oid==undefined)
        oid = GetQueryString("OID");
    pageData.OID = oid;
    pageData.WorkID = oid;
    pageData.Paras = GetQueryString("Paras");
    pageData.IsReadonly = GetQueryString("IsReadonly"); 
    pageData.IsStartFlow = GetQueryString("IsStartFlow"); 
    pageData.FK_MapData = GetQueryString("FK_MapData");
}




//设置表单元素不可用
function setFormEleDisabled() {
    //文本框等设置为不可用
    $('#divCCForm textarea').attr('disabled', 'disabled');
    $('#divCCForm select').attr('disabled', 'disabled');
    $('#divCCForm input[type!=button]').attr('disabled', 'disabled');
}








var appPath = "/";
if (plant == "JFlow")
    appPath = "./../../";
var DtlsCount = " + dtlsCount + "; //应该加载的明细表数量


//处理URL，MainTable URL 参数 替换问题
function dealWithUrl(src) {
    var src = src.replace(new RegExp(/(：)/g), ':');

    //替换
    src = src.replace('@OID', pageData.OID);
    src = src.replace('@WorkID', pageData.OID);

    var params = '&FID=' + pageData.FID;
    params += '&WorkID=' + pageData.OID;
    if (src.indexOf("?") > 0) {
        var params = getQueryStringFromUrl(src);
        if (params != null && params.length > 0) {
            $.each(params, function (i, param) {
                if (param.indexOf('@') >= 0) {//是需要替换的参数
                    paramArr = param.split('=');
                    if (paramArr.length == 2 && paramArr[1].indexOf('@') == 0) {
                        if (paramArr[1].indexOf('@WebUser.') == 0) {
                            params[i] = paramArr[0].substring(1) + "=" + frmData.MainTable[0][paramArr[1].substr('@WebUser.'.length)];
                        }
                        if (frmData.MainTable[0][paramArr[1].substr(1)] != undefined) {
                            params[i] = paramArr[0].substring(1) + "=" + frmData.MainTable[0][paramArr[1].substr(1)];
                        }

                        //使用URL中的参数
                        var pageParams = getQueryString();
                        var pageParamObj = {};
                        $.each(pageParams, function (i, pageParam) {
                            if (pageParam.indexOf('@') == 0) {
                                var pageParamArr = pageParam.split('=');
                                pageParamObj[pageParamArr[0].substring(1, pageParamArr[0].length)] = pageParamArr[1];
                            }
                        });
                        var result = "";
                        //通过MAINTABLE返回的参数
                        for (var ele in frmData.MainTable[0]) {
                            if (paramArr[0].substring(1) == ele) {
                                result = frmData.MainTable[0][ele];
                                break;
                            }
                        }
                        //通过URL参数传过来的参数
                        for (var pageParam in pageParamObj) {
                            if (pageParam == paramArr[0].substring(1)) {
                                result = pageParamObj[pageParam];
                                break;
                            }
                        }

                        if (result != '') {
                            params[i] = paramArr[0].substring(1) + "=" + unescape(result);
                        }
                    }
                }
            });
            src = src.substr(0, src.indexOf('?')) + "?" + params.join('&');
        }
    }
    else {
        src += "?q=1";
    }
    return src;
}


//20160106 by 柳辉
//获取页面参数
//sArgName表示要获取哪个参数的值
function GetPageParas(sArgName) {

    var sHref = window.location.href;
    var args = sHref.split("?");
    var retval = "";
    if (args[0] == sHref) /*参数为空*/ {
        return retval; /*无需做任何处理*/
    }
    var str = args[1];
    args = str.split("&");
    for (var i = 0; i < args.length; i++) {
        str = args[i];
        var arg = str.split("=");
        if (arg.length <= 1) continue;
        if (arg[0] == sArgName) retval = arg[1];
    }
    return retval;
}

function SaveDtlData(scope) {
    if (IsChange == false)
        return true;

    return Save(scope);
}

function Change(id) {
    IsChange = true;
    var tagElement = window.parent.document.getElementById("HL" + id);
    if (tagElement) {
        var tabText = tagElement.innerText;
        var lastChar = tabText.substring(tabText.length - 1, tabText.length);
        if (lastChar != "*") {
            tagElement.innerHTML = tagElement.innerText + '*';
        }
    }

    if (typeof self.parent.TabFormExists != 'undefined') {
        var bExists = self.parent.TabFormExists();
        if (bExists) {
            self.parent.ChangTabFormTitle();
        }
    }
}





//地图
function figure_Template_Map(MapID, UIIsEnable) {
    var mainTable = frmData.MainTable[0];
    var AtPara = "";
    //通过MAINTABLE返回的参数
    for (var ele in mainTable) {
        if (ele == "AtPara" && mainTable != '') {
            AtPara = mainTable[ele];
            break;
        }
    }

    var url = basePath + "/WF/CCForm/Map.htm?WorkID=" + pageData.WorkID + "&FK_Node=" + pageData.FK_Node + "&KeyOfEn=" + MapID + "&UIIsEnable=" + UIIsEnable + "&Paras=" + AtPara;
    OpenBootStrapModal(url, "eudlgframe", "地图", 800, 500, null, false, function () { }, null, function () {

    });
}
