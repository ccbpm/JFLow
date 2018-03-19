//回调函数
function callBack(jsonData, scope) {

    var grid = $('#maingrid').treegrid({
        fitColumns: true,
        idField: 'No',
        treeField: 'Name',
        url: '/AppDemoLigerUI/Base/DataService.aspx?method=flowsearch',
        method: 'get',
        columns: [[{ title: '名称', field: 'Name', width: 360, align: 'left', formatter: function (value, rec) {
            if (rec.Element == "flow") {
                var reVal = "../WF/Rpt/Search.aspx?RptNo=ND" + Number(rec.No) + "MyRpt";
                reVal = "<a href=javascript:WinOpen('" + reVal + "')  class='s12'>" + rec.Name + "</a>";
                return reVal;
            }
            return rec.Name;
        }
        }, { title: '单据', field: 'NumOfBill', formatter: function (value, rec) {
            if (rec.Element == "flow") {
                var reVal = "";
                if (rec.NumOfBill == 0) {
                    reVal = "无";
                }
                else {
                    reVal = "../WF/Rpt/Bill.aspx?EnsName=BP.WF.Bills&FK_Flow=" + rec.No + "&T=" + dateNow;
                    reVal = "<a href=\"javascript:WinOpen('" + reVal + "');\"  ><img src='Img/Menu/bill.png' align='middle' width='16' height='16' border=0/>单据</a>";
                }
                return reVal;
            }
        }
        }, { title: '流程查询-分析', field: 'opt', formatter: function (value, rec) {
            if (rec.Element == "flow") {
                var reVal = "../WF/Rpt/Search.aspx?RptNo=ND" + Number(rec.No) + "MyRpt";
                reVal = "<a href=javascript:WinOpen('" + reVal + "')  class='s12'>查询</a>";
                var reVal1 = "../WF/Rpt/Group.aspx?FK_Flow=" + rec.No + "&DoType=Dept";
                reVal1 = " - <a href=javascript:WinOpen('" + reVal1 + "')  class='s12'>分析</a>";
                return reVal + reVal1;
            }
        }
        }]],
        onLoadSuccess: function (row, data) {
            $('#maingrid').treegrid('expandAll');
        }
    });
    $("#pageloading").hide();
}
//打开流程图
//function OpenFlowPicture(flowNo, flowName) {
//    var pictureUrl = "../WF/Chart.aspx?FK_Flow=" + flowNo + "&DoType=Chart&T=" + dateNow;
//    var win = $.ligerDialog.open({
//        height: 500, width: 800, url: pictureUrl, showMax: true, isResize: true, modal: true, title: flowName + "流程图", slide: false, buttons: [{
//            text: '关闭', onclick: function (item, Dialog, index) {
//                win.hide();
//            }
//        }]
//    });
//}
//打开流程图
function OpenEasyUiFlowPicture(flowNo, flowName) {
    var pictureUrl = "/WF/Chart.aspx?FK_Flow=" + flowNo + "&DoType=Chart&T=" + dateNow;
    $("#opengrid").dialog({
        height: 500,
        width: 800,
        href: pictureUrl,
        showMax: true,
        isResize: true,
        modal: true,
        title: flowName + "流程图",
        slide: false,
        buttons: [{ text: '关闭', handler: function () {
            $('#opengrid').dialog('close');
        }
        }]
    });
}
function WinOpen(url) {
    var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    newWindow.focus();
    return;
}
//加载查询列表
function LoadGrid() {
    $("#pageloading").show();
    Application.data.flowSearch(callBack, this);
}
var dateNow = "";

$(function () {

    var date = new Date();
    dateNow += date.getFullYear(); //年
    dateNow += date.getMonth() + 1; //月 月比实际月份要少1
    dateNow += date.getDate(); //日
    dateNow += date.getHours(); //HH
    dateNow += date.getMinutes(); //MM
    dateNow += date.getSeconds(); //SS


    LoadGrid();
});