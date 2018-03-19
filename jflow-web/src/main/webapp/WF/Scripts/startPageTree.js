var strTimeKey = "";
//发起流程
function StartFlow(url) {

}
//打开流程图
//function OpenFlowPicture(flowNo, flowName) {
//    var pictureUrl = "../WF/Chart.aspx?FK_Flow=" + flowNo + "&DoType=Chart&T=" + strTimeKey;
//    var win = $.ligerDialog.open({
//        height: 500, width: 800, url: pictureUrl, showMax: true, isResize: true, modal: true, title: flowName + "流程图", slide: false, buttons: [{
//            text: '关闭', onclick: function (item, Dialog, index) {
//                win.hide();
//            }
//        }]
//    });
//}
//发起工作流
function StartListUrl(url) {
    var v = window.showModalDialog(url, 'sd', 'dialogHeight: 550px; dialogWidth: 650px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
    if (v == null || v == "")
        return;
}
//打开窗体
function WinOpenIt(tabid, text, url) {
    if (ccflow.config.IsWinOpenStartWork == 1) {
        window.parent.f_addTab(tabid + strTimeKey, text, url);
    } else {
        var winWidth = 850;
        var winHeight = 680;
        if (screen && screen.availWidth) {
            winWidth = screen.availWidth;
            winHeight = screen.availHeight;
        }
        //var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        window.showModalDialog(url, "", "scrollbars=yes;resizable=yes;center=yes;dialogWidth=" + winWidth + ";dialogHeight=" + winHeight + ";dialogTop=50px;dialogLeft=50px;");
    }
}
function WinOpenWindow(url) {
    var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    newWindow.focus();
}
//输入标题框
//function ShowTitleDiv(tabid, text, url) {
//    //执行命令
//    Application.data.createEmptyCase(tabid, "", function (js, scope) {
//        if (js == "addform") {
//            if (ccflow.config.IsWinOpenStartWork == 1) {
//                WinOpenIt(tabid, text, url);
//            } else {
//                window.parent.f_addTab(tabid + strTimeKey, text, url);
//            }
//        } else {
//            //打开层
//            $.ligerDialog.open({
//                target: $("#divTitle"),
//                title: '新建- ' + text + "流程",
//                width: 510,
//                height: 180,
//                isResize: true,
//                modal: true,
//                buttons: [{ text: '确定', onclick: function (i, d) {
//                    var title = $("#TB_Title").val();

//                    if (title == "") {
//                        $.ligerDialog.warn('标题不允许为空！');
//                        return;
//                    }
//                    //执行命令
//                    Application.data.createEmptyCase(tabid, title, function (js, scope) {
//                        $("#TB_Title").val("");
//                        d.hide();
//                        WinOpenIt(tabid, text, js);
//                    }, this);
//                }
//                }, { text: '取消', onclick: function (i, d) {
//                    $("#TB_Title").val("");
//                    d.hide();
//                }
//                }]
//            });
//        }
//    }, this);
//}
//显示历史发起
//function ShowHistoryData(flowNo, flowName) {
//    //打开层
//    $.ligerDialog.open({
//        target: $("#showHistory"),
//        title: flowName + '-历史发起列表',
//        width: 810,
//        height: 500,
//        isResize: true,
//        modal: true,
//        buttons: [{ text: '关闭', onclick: function (i, d) {
//            d.hide();
//        }
//        }]
//    });
//    LoadHistoryGrid(flowNo);
//}
//加载历史发起数据
//function LoadHistoryGrid(flowNo) {
//    $("#pageloading").show();
//    Application.data.getHistoryStartFlow(flowNo, function (json) {
//        if (json) {
//            var pushData = eval('(' + json + ')');
//            var grid = $("#historyGrid").ligerGrid({
//                columns: [
//                   { display: '标题', name: 'Title', width: 320, align: 'left', render: function (rowdata, rowindex) {
//                       var h = "../WF/WFRpt.jsp?WorkID=" + rowdata.OID + "&FK_Flow=" + flowNo + "&FID=" + rowdata.FID + "&T=" + strTimeKey;
//                       return "<a href='javascript:void(0);' onclick=WinOpenWindow('" + h + "')>" + rowdata.Title + "</a>";
//                   }
//                   },
//                   { display: '发起时间', name: 'FlowStartRDT' },
//                   { display: '参与人', name: 'FlowEmps', width: 300 }
//                   ],
//                pageSize: 20,
//                data: pushData,
//                rownumbers: true,
//                width: 780,
//                height: 430,
//                columnWidth: 100,
//                onDblClickRow: function (rowdata, rowindex) {
//                    WinOpenWindow("../WF/WFRpt.jsp?WorkID=" + rowdata.OID + "&FK_Flow=" + flowNo + "&FID=" + rowdata.FID + "&T=" + strTimeKey);
//                }
//            });
//        }
//        $("#pageloading").hide();
//    }, this);
//}
//回调函数
//function callBack(jsonData, scope) {
//    if (jsonData) {
//        var pushData = eval('(' + jsonData + ')');
//        var grid = $("#maingrid").ligerGrid({
//            columns: [
//                   { display: '名称', name: 'Name', width: 380, align: 'left', render: function (rowdata, rowindex) {
//                       var h = "";
//                       if (rowdata.StartListUrl) {
//                           h = rowdata.StartListUrl + "?FK_Flow=" + rowdata.No + "&FK_Node=" + rowdata.No + "01&T=" + strTimeKey;
//                           return "<a href='javascript:void(0);' onclick=StartListUrl('" + h + "')>" + rowdata.Name + "</a>";
//                       }
//                       h = "../WF/MyFlow.aspx?FK_Flow=" + rowdata.No + "&FK_Node=" + rowdata.No + "01&T=" + strTimeKey;
//                       return "<a href='javascript:void(0);' onclick=ShowTitleDiv('" + rowdata.No + "','" + rowdata.Name + "','" + h + "')>" + rowdata.Name + "</a>";
//                       
//                   }
//                   },
//                   { display: '批量发起', name: 'IsBatchStart', render: function (rowdata, rowindex, value) {
//                       var h = "";
//                       if (rowdata.IsBatchStart == "1") {
//                           h = "../WF/BatchStart.aspx?FK_Flow=" + rowdata.No;
//                           h = "<a href='javascript:void(0);' onclick=StartListUrl('" + h + "')>批量发起</a>";
//                       }
//                       return h;

//                   }
//                   },
//                   { display: '流程图', name: 'RoleType', render: function (rowdata, rowindex, value) {
//                       return "<a href='javascript:void(0);' onclick=OpenFlowPicture('" + rowdata.No + "','" + rowdata.Name + "')>打开</a>";
//                   }
//                   }, {
//                       display: '历史发起', name: 'HistoryFlow', width: 180, render: function (rowdata, rowindex) {
//                           return "<a href='javascript:void(0);' onclick=ShowHistoryData('" + rowdata.No + "','" + rowdata.Name + "')>查看</a>";
//                       }
//                   },
//                   { display: '描述', name: 'Note', width: 280, render: function (rowdata, rowindex) {
//                       if (rowdata.Note == null || rowdata.Note == "") {
//                           return "无";
//                       }
//                       return rowdata.Note;
//                   }
//                   }
//                   ],
//            pageSize: 20,
//            data: pushData,
//            rownumbers: true,
//            height: "99%",
//            width: "99%",
//            columnWidth: 120,
//            onReload: LoadGrid,
//            groupColumnName: 'FK_FlowSortText',
//            groupColumnDisplay: '类型',
//            onDblClickRow: function (rowdata, rowindex) {
//                OpenFlowPicture(rowdata.No, rowdata.Name);
//                //WinOpenIt( "../WF/MyFlow.aspx?FK_Flow=" + rowdata.No + "&FK_Node=" + rowdata.No + "01&T=" + strTimeKey);
//            }
//        });
//    }
//    else {
//        $.ligerDialog.warn('加载数据出错，请关闭后重试！');
//    }
//    $("#pageloading").hide();
//}
function callBack(jsonData, scope) {
    if (jsonData) {

        
        var grid = $('#maingrid').treegrid({
            fitColumns: true,
            idField: 'No',
            treeField: 'Name',
            url: '/AppDemoLigerUI/Base/DataService.aspx?method=startflowTree',
            method: 'get',

            columns: [[

                    { field: 'Name', title: '名称', width: 380, align: 'left', formatter: function (value, rec) {
                        if (rec.FK_FlowSort == null)
                            return value;
                        var h = "";
                        if (rec.StartListUrl) {
                            h = rec.StartListUrl + "?FK_Flow=" + rec.No + "&FK_Node=" + rec.No + "01&T=" + strTimeKey;
                            return "<a href='javascript:void(0);' onclick=StartListUrl('" + h + "')>" + rec.Name + "</a>";
                        }
                        h = "../WF/MyFlow.aspx?FK_Flow=" + rec.No + "&FK_Node=" + rec.No + "01&T=" + strTimeKey;
                        return "<a href='javascript:void(0);' onclick=ShowEasyUiTitleDiv('" + rec.No + "','" + rec.Name + "','" + h + "')>" + rec.Name + "</a>";
                    }
                    },
                    { field: 'IsBatchStart', title: '批量发起', formatter: function (value, rec) {
                        if (rec.FK_FlowSort == null)
                            return value;
                        var h = "";
                        if (rec.IsBatchStart == "1") {
                            h = "../WF/BatchStart.aspx?FK_Flow=" + rec.No;
                            h = "<a href='javascript:void(0);' onclick=StartListUrl('" + h + "')>批量发起</a>";
                        }
                        return h;
                    }
                    },
                    { field: 'RoleType', title: '流程图', formatter: function (value, rec) {
                        if (rec.FK_FlowSort == null)
                            return value;
                        return "<a href='javascript:void(0);' onclick=OpenEasyUiFlowPicture('" + rec.No + "','" + rec.Name + "')>打开</a>";
                    }
                    },
                    { field: 'HistoryFlow', title: '历史发起', width: 180, formatter: function (value, rec) {
                        if (rec.FK_FlowSort == null)
                            return value;
                        return "<a href='javascript:void(0);' onclick=ShowEasyUiHistoryData('" + rec.No + "','" + rec.Name + "')>查看</a>";
                    }
                    },
                    { field: 'Note', title: '描述', width: 280, formatter: function (value, rec) {
                        if (rec.FK_FlowSort == null)
                            return value;
                        if (rec.Note == null || rec.Note == "") {
                            return "无";
                        }
                        return rec.Note;
                    }
                    }
                ]],
            onLoadSuccess: function (row, data) {
                $('#maingrid').treegrid('expandAll');
            }

        });
    }
    else {
        $.messager.alert('提示', '加载数据出错，请关闭后重试！');
    }
    $("#pageloading").hide();
}

//加载历史发起数据
function LoadEasyUiHistoryGrid(flowNo) {
    $("#pageloading").show();
    Application.data.getHistoryStartFlow(flowNo, function (json) {
        if (json) {
            var grid = $("#historyGrid").datagrid({
                pagination: true,
                nowrap: true,
                fitColumns: true,
                autoRowHeight: false,
                striped: true,
                collapsible: false,
                url: '/AppDemoLigerUI/Base/DataService.aspx?method=historystartflow&FK_Flow=' + flowNo,
                columns: [[
            { title: '标题', field: 'Title', width: 320, align: 'left', formatter: function (value, rec) {
                var h = "../WF/WFRpt.jsp?WorkID=" + rec.OID + "&FK_Flow=" + flowNo + "&FID=" + rec.FID + "&T=" + strTimeKey;
                return "<a href='javascript:void(0);' onclick=WinOpenWindow('" + h + "')>" + rec.Title + "</a>";
            }
            },
            { title: '发起时间', field: 'FlowStartRDT' },
            { title: '参与人', field: 'FlowEmps', width: 300 }
            ]],
                rownumbers: true,
                width: 780,
                height: 430
            });
        }
        $("#pageloading").hide();
    }, this)
}
//打开流程图
function OpenEasyUiFlowPicture(flowNo, flowName) {
    var pictureUrl = "/DataUser/FlowDesc/" + flowNo + "." + flowName + "/Flow.png";
    //$.jBox("iframe:" + pictureUrl, {
    //    title: flowName + "流程图",
    //    width: 800,
    //    height: 500,
    //    buttons: { '关闭': true }
    //});
    document.getElementById("FlowPic").src = pictureUrl;
    $("#flowPicDiv").dialog({
        height: 500,
        width: 800,
        showMax: true,
        isResize: true,
        modal: true,
        title: flowName + "流程图",
        slide: false,
        buttons: [{ text: '关闭', handler: function () {
            $('#flowPicDiv').dialog('close');
        }
        }]
    });
}
//显示历史发起
function ShowEasyUiHistoryData(flowNo, flowName) {
    //打开层
    $("#showHistory").dialog({
        title: flowName + '-历史发起列表',
        width: 810,
        height: 500,
        modal: true,
        buttons: [{ text: '关闭', handler: function () {
            $('#showHistory').dialog('close');
        }
        }]
    });
    LoadEasyUiHistoryGrid(flowNo);
}
//输入标题框
function ShowEasyUiTitleDiv(tabid, text, url) {
    //执行命令
    Application.data.createEmptyCase(tabid, "", function (js, scope) {
        if (js == "addform") {
            if (ccflow.config.IsWinOpenStartWork == 0) {
                WinOpenIt(tabid, text, url);
            } else {
                window.parent.f_addTab(tabid + strTimeKey, text, url);
            }
        } else {
            //打开层
            $('#divTitle').show();
            $("#divTitle").dialog({
                title: '新建- ' + text + "流程",
                width: 510,
                height: 180,
                resizable: true,
                buttons: [{ text: '确定', handler: function () {
                    var title = $("#TB_Title").val();

                    if (title == "") {
                        $.messager.alert('提示', '标题不允许为空！');
                        return;
                    }
                    //执行命令
                    Application.data.createEmptyCase(tabid, title, function (js, scope) {
                        $("#TB_Title").val("");
                        $('#divTitle').dialog('close');
                        WinOpenIt(tabid, text, js);
                    });
                }
                }, { text: '取消', handler: function (i, d) {
                    $("#TB_Title").val("");
                    //d.hide();
                    $('#divTitle').dialog('close');
                }
                }]
            });
        }
    }, this);
}
//加载发起流程列表
function LoadGrid() {
    $("#pageloading").show();
    $("#divTitle").hide();
    Application.data.getStartFlowTree(callBack, this);
}

$(function () {
    strTimeKey = "";
    var date = new Date();
    strTimeKey += date.getFullYear(); //年
    strTimeKey += date.getMonth() + 1; //月 月比实际月份要少1
    strTimeKey += date.getDate(); //日
    strTimeKey += date.getHours(); //HH
    strTimeKey += date.getMinutes(); //MM
    strTimeKey += date.getSeconds(); //SS
    LoadGrid();
});