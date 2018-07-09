﻿
function InitBar(optionKey) {

    var html = "表单方案:";
    html += "<select id='changBar' onchange='changeOption()'>";
    html += "<option value=" + FormType.FoolForm + ">0.内置傻瓜表单</option>";
    html += "<option value=" + FormType.FreeForm + ">1.内置自由表单</option>";
    html += "<option value=" + FormType.SelfForm + " >2.嵌入式表单</option>";
    html += "<option value=" + FormType.RefOneFrmTree + " >3.绑定表单库的表单</option>";
    html += "<option value=" + FormType.SheetTree + " >4.绑定多表单(表单树)</option>";
   
    html += "<option value=" + FormType.SDKForm + " >5.使用SDK表单(我自定义的表单)</option>";
    html += "<option value=" + FormType.WebOffice + " >6.绑定公文表单</option>";
    html += "<option value=" + FormType.FoolTruck + " >7.内置累加傻瓜模式表单</option>";
    html += "</select >";

    html += "<input  id='Btn_Save' type=button onclick='Save()' value='保存' />";
    html += "<input  id='Btn_SaveAndClose' type=button onclick='SaveAndClose()' value='保存并关闭' />";

  //  html += "<input type=button onclick='OldVer()' value='使用旧版本' />";

    html += "<input  id='Btn_Help' type=button onclick='Help()' value='视频帮助' />";
    html += "<input  id='Btn_Help' type=button onclick='HelpOnline()' value='在线帮助' />";


    document.getElementById("bar").innerHTML = html;
    $("#changBar option[value='" + optionKey + "']").attr("selected", "selected");
}

function OldVer() {

    var nodeID = GetQueryString("FK_Node");
    var flowNo = GetQueryString("FK_Flow");
    var url = '../NodeFromWorkModel.htm?FK_Flow=' + flowNo + '&FK_Node=' + nodeID;
    window.location.href = url;
}


//打开傻瓜表单设计器.
function DFoolFrm() {

    var nodeID = GetQueryString("FK_Node");
    var node = new Entity("BP.WF.Node", nodeID);
    var url = '../../FoolFormDesigner/Designer.htm?FK_Flow=' + node.FK_Flow + '&FK_Node=' + nodeID + "&FK_MapData=ND" + nodeID;
    window.open(url);

    //OpenEasyUiDialogExt(url, '傻瓜表单设计器', 800, 500, false);
}

//打开自由表单设计器.
function DFreeFrm() {
    var nodeID = GetQueryString("FK_Node");
    var node = new Entity("BP.WF.Node", nodeID);
    var url = '../../CCFormDesigner/FormDesigner.htm?FK_Flow=' + node.FK_Flow + '&FK_Node=' + nodeID + "&FK_MapData=ND" + nodeID;
    OpenEasyUiDialogExt(url, '傻瓜表单设计器', 1100, 600, false);
}

function HelpOnline() {
    var url = "http://ccbpm.mydoc.io";
    window.open(url);
}

function Help() {

    var url = window.location.href;

    var nodeID = GetQueryString("FK_Node");
    var obj = document.getElementById("changBar");
    var sele = obj.options;
    var index = obj.selectedIndex;
    var optionKey = optionKey = sele[index].value;

    var roleName = "";
    switch (parseInt(optionKey)) {
        case FormType.FoolForm:
            url = "0.FoolForm.htm";
            break;
        case FormType.FreeForm:
            url = "1.FreeForm.htm";
            break;
        case FormType.SelfForm:
            url = "2.SelfForm.htm";
            break;
        case FormType.SDKForm:
            url = "3.SDKForm.htm";
            break;
        case FormType.SLForm:
            url = "4.SLForm.htm";
            break;
        case FormType.SheetTree:
            url = "5.SheetTree.htm";
            break;
        case FormType.SheetAutoTree:
            url = "6.SheetAutoTree.htm";
            break;
        case FormType.WebOffice:
            url = "7.WebOffice.htm";
            break;
        case FormType.ExcelForm:
            url = "8.ExcelForm.htm";
            break;
        case FormType.WordForm:
            url = "9.WordForm.htm";
            break;
        case FormType.FoolTruck:
            url = "10.FoolTruck.htm";
            break;
        case FormType.RefOneFrmTree:
            alert('该视频尚未提供');
            return;
            //  url = "11.RefOneFrmTree.htm";
            break;
        case FormType.DisableIt:
            url = "100.DisableIt.htm";
            break;
        default:
            url = "0.FoolForm.htm";
            break;
    }

    //if (url.indexOf
    //var url = "http://ccbpm.mydoc.io";
    //window.open(url);
}


function changeOption() {
    var nodeID = GetQueryString("FK_Node");
    var obj = document.getElementById("changBar");
    var sele = obj.options;
    var index = obj.selectedIndex;
    var optionKey = optionKey = sele[index].value;
    
    var roleName = "";
    switch (parseInt(optionKey)) {
        case FormType.FoolForm:
            url = "0.FoolForm.htm";
            break;
        case FormType.FreeForm:
            url = "1.FreeForm.htm";
            break;
        case FormType.SelfForm:
            url = "2.SelfForm.htm";
            break;
        case FormType.SDKForm:
            url = "3.SDKForm.htm";
            break;
        case FormType.SLForm:
            url = "4.SLForm.htm";
            break;
        case FormType.SheetTree:
            url = "5.SheetTree.htm";
            break;
        case FormType.SheetAutoTree:
            url = "6.SheetAutoTree.htm";
            break;
        case FormType.WebOffice:
            url = "7.WebOffice.htm";
            break;
        case FormType.ExcelForm:
            url = "8.ExcelForm.htm";
            break;
        case FormType.WordForm:
            url = "9.WordForm.htm";
            break;
        case FormType.FoolTruck:
            url = "10.FoolTruck.htm";
            break;
        case FormType.RefOneFrmTree:
            url = "11.RefOneFrmTree.htm";
            break;
        case FormType.DisableIt:
            url = "100.DisableIt.htm";
            break;
        default:
            url = "0.FoolForm.htm";
            break;
    }
    window.location.href = url + "?FK_Node=" + nodeID;
}


function SaveAndClose() {

    Save();
    window.close();
}

//打开窗体.
function OpenEasyUiDialogExt(url, title, w, h, isReload) {

    OpenEasyUiDialog(url, "eudlgframe", title, w, h, "icon-property", true, null, null, null, function () {
        if (isReload == true) {
            window.location.href = window.location.href;
        }
    });
}