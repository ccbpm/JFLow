﻿//定义全局的变量
var pageData = {};//全局的参数变量
var flowData = {}; // 流程数据
var isReadonly = true;
if (typeof webUser == "undefined" || webUser == null)
    webUser = new WebUser();

var UserICon = getConfigByKey("UserICon", '../DataUser/Siganture/'); //获取签名图片的地址
var UserIConExt = getConfigByKey("UserIConExt", '.jpg');  //签名图片的默认后缀
$(function () {
    UserICon = UserICon.replace("@basePath", basePath);

    //增加css样式
    $('head').append('<link href="../DataUser/Style/GloVarsCSS.css" rel="stylesheet" type="text/css" />');

    var nodeID = GetQueryString("FK_Node");
    var workID = GetQueryString("WorkID");

    if (nodeID > 1) {

        var nd = new Entity("BP.WF.Node", nodeID);
        //   var node = new Entity("BP.WF.Node", nodeID);
        //   node.FormType = FormSlnType.SelfForm;
        //   node.FormUrl = document.getElementById("TB_CustomURL").value;
        ////   node.Update();

        var url = "";
        /** 如果是嵌入模式的表单.*/
        if (nd.FormType == FormSlnType.SelfForm) {
            window.location.href = "/WF/MyView.htm?FK_Flow=" + nd.FK_Flow + "&NodeID=" + nd.NodeID + "&WorkID=" + workID;
            return;
        }

        /** sdk模式.*/
        if (nd.FormType == FormSlnType.SDKForm) {
            url = node.FormUrl;
            if (url.indexOf('?') == -1)
                url += "?1=1";
            url += "&WorkID=" + workID + "&FK_Node=" + nd.NodeID;
            window.location.href = url;
            return;
        }
    }
        //初始化表单参数
        initPageData();

        //初始化表单数据
        GenerWorkNode();
    })

/**
 * 初始化表单数据
 */
function initPageData() {
    pageData = {
        FK_Flow: GetQueryString("FK_Flow"),
        FK_Node: GetQueryString("FK_Node"),
        FID: GetQueryString("FID") == null ? 0 : GetQueryString("FID"),
        WorkID: GetQueryString("WorkID"),
        OID: pageData.WorkID,
        Paras: GetQueryString("Paras"),
        IsReadonly: 1,
        IsStartFlow: GetQueryString("IsStartFlow"),
        IsMobile: IsMobile()//是不是移动端
    }
}
/**
 * 获取表单数据
 */
function GenerWorkNode() {
    //判断当前节点是不是绑定多表单
    if (pageData.FK_Node != null || pageData.FK_Node != undefined) {
        var node = new Entity("BP.WF.Node", pageData.FK_Node);
        if (node.FormType == 5) {
            SetHref(GetHrefUrl().replace("MyFrm.htm", "MyFrmTree.htm"));
            return;
        }
        if (node.FormType == 11) { //绑定单表单
            var mapData = new Entity("BP.Sys.MapData", node.NodeFrmID);
            if (mapData.FrmType == 10) {
                SetHref(GetHrefUrl().replace("MyFrm.htm", "CCForm/ChapterFrmView.htm") + "&FrmID=" + mapData.No);
                return;
            }
        }


    }
    var index = 0;
    var href = GetHrefUrl();
    var urlParam = href.substring(href.indexOf('?') + 1, href.length);
    urlParam = urlParam.replace('&DoType=', '&DoTypeDel=xx');

    var handler = new HttpHandler("BP.WF.HttpHandler.WF_MyView");
    handler.AddUrlData(urlParam);
    var data = handler.DoMethodReturnString("GenerWorkNode"); //执行保存方法.

    if (data.indexOf('err@') == 0) {
        layer.alert(data);
        console.log(data);
        layer.close(index);
        return;
    }

    try {

        flowData = JSON.parse(data);

    } catch (err) {
        layer.alert(" GenerWorkNode转换JSON失败,请查看控制台日志,或者联系管理员.");
        console.log(flowData);
        layer.close(index);
        return;
    }

    //获得当前表单的数据，然后把该数据替换掉 frmData的数据.
    handler = new HttpHandler("BP.WF.HttpHandler.WF_MyView");
    handler.AddUrlData(urlParam);
    var data = handler.DoMethodReturnString("MyFrm_Init_Data"); //执行保存方法.
    if (data.indexOf('err@') == 0) {
        alert(data);
        return;
    }
    var frmDB;
    try {
        if (data != null && data != undefined && data != "") {
            frmDB = JSON.parse(data);
            //设置主表数据.
            flowData.MainTable[0] = frmDB;

        }

    } catch (err) {
        alert("err@ GenerWorkNode转换JSON失败,请查日志.");
        return;
    }
    if (webUser == null)
        webUser = new WebUser();
    //处理附件的问题 
    if (flowData.Sys_FrmAttachment.length != 0) {
        Skip.addJs("./CCForm/Ath.js");
        Skip.addJs("./CCForm/JS/FileUpload/fileUpload.js");
        Skip.addJs("./Scripts/jquery-form.js");
        Skip.addJs("../DataUser/OverrideFiles/Ath.js");
        $('head').append("<link href='./CCForm/JS/FileUpload/css/fileUpload.css' rel='stylesheet' type='text/css' />");
    }

    //获取没有解析的外部数据源
    var uiBindKeys = flowData["UIBindKey"];
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
                    if (plant == 'CCFlow')
                        selectStatement = basePath + "/DataUser/SFTableHandler.ashx" + selectStatement;
                    else
                        selectStatement = basePath + "/DataUser/SFTableHandler/" + selectStatement;
                    operdata = DBAccess.RunDBSrc(selectStatement, 1);
                }
                //JavaScript获取外部数据源
                if (srcType == 6) {
                    operdata = DBAccess.RunDBSrc(sfTable.FK_Val, 2);
                }
                flowData[uiBindKeys[i].No] = operdata;
            }
        }

    }
    var node = flowData.WF_Node[0];
    //修改网页标题.
    document.title = node.FlowName + ',' + node.Name;

    //解析表单
    BindFrm();
    /* //加载JS文件 改变JS文件的加载方式 解决JS在资源中不显示的问题.
     var enName = flowData.Sys_MapData[0].No;
     loadScript("../DataUser/JSLibData/" + pageData.FK_Flow + ".js?t=" + Math.random());
     loadScript("../DataUser/JSLibData/" + enName + "_Self.js?t=" + Math.random());
     loadScript("../DataUser/JSLibData/" + enName + ".js?t=" + Math.random());*/

    layer.close(index);
}

/**
 * 解析表单数据
 */
function BindFrm() {
    var node = flowData.WF_Node[0];
    var flow = flowData.WF_Flow[0];
    var flowDevModel = flow.FlowDevModel;
    flowDevModel = flowDevModel == null || flowDevModel == undefined || flowDevModel == "" ? 0 : parseInt(flowDevModel);
    var isFool = true;
    //根据流程设计模式解析
    switch (flowDevModel) {
        case FlowDevModel.Prefessional: //专业模式 
            //根据节点的表单方案解析
            switch (parseInt(node.FormType)) {
                case 0: //傻瓜表单
                case 10://累加表单
                    $('head').append('<link href="../DataUser/Style/FoolFrmStyle/Default.css" rel="stylesheet" type="text/css" />');
                    Skip.addJs("./CCForm/FrmFool.js?ver=" + Math.random());
                    GenerFoolFrm(flowData);
                    break;
                case 12://开发者表单
                    $('head').append('<link href="../DataUser/Style/ccbpm.css" rel="stylesheet" type="text/css" />');
                    $('head').append('<link href="../DataUser/Style/MyFlowGenerDevelop.css" rel="Stylesheet" />');
                    Skip.addJs("./CCForm/FrmDevelop2021.js?ver=1");
                    GenerDevelopFrm(flowData, flowData.Sys_MapData[0].No);
                    isFool = false;
                    break;
                case 5://树形表单
                    //GenerTreeFrm(flowData);
                    break;
                case 11://表单库单表单
                    var frmNode = flowData.WF_FrmNode;
                    var mapData = flowData.Sys_MapData[0]
                    if (frmNode != null && frmNode != undefined) {
                        frmNode = frmNode[0];
                        if (mapData.FrmType == 0) { //傻瓜表单
                            $('head').append('<link href="../DataUser/Style/FoolFrmStyle/Default.css" rel="stylesheet" type="text/css" />');
                            Skip.addJs("./CCForm/FrmFool.js?ver=" + Math.random());
                            GenerFoolFrm(flowData);
                        }
                        if (mapData.FrmType == 8) {//开发者表单
                            Skip.addJs("./CCForm/FrmDevelop2021.js?ver=" + Math.random());
                            $('head').append('<link href="../DataUser/Style/ccbpm.css" rel="stylesheet" type="text/css" />');
                            $('head').append('<link href="../DataUser/Style/MyFlowGenerDevelop.css" rel="Stylesheet" />');
                            GenerDevelopFrm(flowData, flowData.Sys_MapData[0].No);
                            isFool = false;
                        }
                    }
                    break;
                case 3://自定义表单
                    var url = node.FormUrl;
                    if (url.indexOf("@basePath") != -1)
                        url = url.replace('@basePath', basePath);

                    if (url == "")
                        url = basePath + "/DataUser/DefaultSelfFormUrl.htm";
                    else if (url.indexOf("http") == -1)
                        url = basePath + url;

                    if (url.indexOf('?') == -1) {
                        url = url + "?1=2";
                    }


                    url += "&WorkID=" + GetQueryString("WorkID")
                        + "&FK_Flow=" + GetQueryString("FK_Flow")
                        + "&FID=" + GetQueryString("FID")
                        + "&IsPass=1"
                        + "&FK_Node=" + GetQueryString("FK_Node") + "&IsReadonly=1";

                    var html = "<iframe ID='SelfForm' src='"
                        + url
                        + "' frameborder=0  style='width:100%; height:80vh;' leftMargin='0' topMargin='0'"
                        + 'scrolling="no"  />';

                    $('#CCForm').append(html);
                    break;
                default:
                    layer.alert("节点表单方案:[" + node.FormType + "]不存在或者暂未解析，请联系管理员")
                    break;
            }
            break;
        case FlowDevModel.JiJian://极简模式(傻瓜表单)
            var mapData = flowData.Sys_MapData[0];
            if (mapData.FrmType == 0) { //傻瓜表单
                $('head').append('<link href="../DataUser/Style/FoolFrmStyle/Default.css" rel="stylesheet" type="text/css" />');
                Skip.addJs("./CCForm/FrmFool.js?ver=" + Math.random());
                GenerFoolFrm(flowData);
            }
            if (mapData.FrmType == 8) {//开发者表单
                Skip.addJs("./CCForm/FrmDevelop2021.js?ver=" + Math.random());
                $('head').append('<link href="../DataUser/Style/ccbpm.css" rel="stylesheet" type="text/css" />');
                $('head').append('<link href="../DataUser/Style/MyFlowGenerDevelop.css" rel="Stylesheet" />');
                GenerDevelopFrm(flowData, flowData.Sys_MapData[0].No);
                isFool = false;
            }
            break;
        case FlowDevModel.FoolTruck://累加模式
            Skip.addJs("./CCForm/FrmFool.js?ver=" + Math.random());
            GenerFoolFrm(flowData); //傻瓜表单.
            break;
        case FlowDevModel.RefOneFrmTree://表单库单表单
            var frmNode = flowData.WF_FrmNode;
            if (frmNode != null && frmNode != undefined) {
                frmNode = frmNode[0];
                if (frmNode.FrmType == 0) { //傻瓜表单
                    $('head').append('<link href="../DataUser/Style/FoolFrmStyle/Default.css" rel="stylesheet" type="text/css" />');
                    Skip.addJs("./CCForm/FrmFool.js?ver=" + Math.random());
                    GenerFoolFrm(flowData);
                }
                if (frmNode.FrmType == 8) {//开发者表单
                    Skip.addJs("./CCForm/FrmDevelop2021.js?ver=" + Math.random());
                    $('head').append('<link href="../DataUser/Style/MyFlowGenerDevelop.css" rel="Stylesheet" />');
                    GenerDevelopFrm(flowData, flowData.Sys_MapData[0].No);
                    isFool = false;
                }
            }
            break;
        case FlowDevModel.FrmTree://表单库多表单
            //GenerTreeFrm(flowData);
            break;
        default:
            layer.alert("流程设计模式:[" + getFlowDevModelText(flow.FlowDevModel) + "]不存在或者暂未解析，请联系管理员")
            break;
    }


    //调整页面宽度
    var w = flowData.Sys_MapData[0].FrmW;//设置的页面宽度
    //傻瓜表单的名称居中的问题
    if ($(".form-unit-title img").length > 0) {
        var width = $(".form-unit-title img")[0].width;
        $(".form-unit-title center h4 b").css("margin-left", "-" + width + "px");
    }
    if (isFool == true && pageData.IsMobile == false) {
        $('#ContentDiv').width(w);
        $('#ContentDiv').css("margin-left", "auto").css("margin-right", "auto");
    }

    //星级评分事件
    setScore(isReadonly);


    //3.装载表单数据与修改表单元素风格.
    LoadFrmDataAndChangeEleStyle(flowData);
    layui.form.render();

    //加载JS文件 改变JS文件的加载方式 解决JS在资源中不显示的问题.
    var enName = flowData.Sys_MapData[0].No;
    if (flowData.Sys_MapData[0].IsEnableJs == 1)
        Skip.addJs("../DataUser/JSLibData/" + enName + "_Self.js?t=" + Math.random());


    //4.解析表单的扩展功能
    AfterBindEn_DealMapExt(flowData);


}


//获得所有的checkbox 的id组成一个string用逗号分开, 以方便后台接受的值保存.
function GenerCheckIDs() {

    var checkBoxIDs = "";
    var arrObj = document.all;

    for (var i = 0; i < arrObj.length; i++) {

        if (arrObj[i].type != 'checkbox')
            continue;

        var cid = arrObj[i].id;
        if (cid == null || cid == "" || cid == '')
            continue;

        checkBoxIDs += arrObj[i].id + ',';
    }
    return checkBoxIDs;
}
//流程设计模式.
var FlowDevModel = {
    //专业模式
    Prefessional: 0,
    //极简模式（傻瓜表单）
    JiJian: 1,
    //累加模式
    FoolTruck: 2,
    //绑定单表单
    RefOneFrmTree: 3,
    //绑定多表单
    FrmTree: 4,
    //SDK表单
    SDKFrm: 5,
    /// 嵌入式表单
    SelfFrm: 6,
    /// 物联网流程
    InternetOfThings: 7,
    /// 决策树流程
    Tree: 8
}
function getFlowDevModelText(model) {
    switch (model) {
        case FlowDevModel.Prefessional:
            return "专业模式";
        case FlowDevModel.JiJian:
            return "专业模式";
        case FlowDevModel.FoolTruck:
            return "累加模式";
        case FlowDevModel.RefOneFrmTree:
            return "绑定表单库的单表单";
        case FlowDevModel.FrmTree:
            return "绑定表单库的多表单";
        case FlowDevModel.SDKFrm:
            return "SDK表单";
        case FlowDevModel.SelfFrm:
            return "嵌入式表单";
        case FlowDevModel.InternetOfThings:
            return "物联网流程";
        case FlowDevModel.Tree:
            return "决策树流程";
        default:
            return model;
    }
}

//阅读并关闭.
function Close() {
    if (window.top.vm && typeof window.top.vm.closeCurrentTabs == "function")
        window.top.vm.closeCurrentTabs(window.top.vm.selectedTabsIndex);
    else
        window.close();
}






