//回调函数
function callBack(jsonData, scope) {
    if (jsonData) {
        var pushData = eval('(' + jsonData + ')');
        var grid = $("#maingrid").ligerGrid({
            columns: [
                   { display: '标题', name: 'Title', width: 340, align: 'left', render: function (rowdata, rowindex) {
                       var title = "<a href=javascript:WinOpenIt('../WF/MyFlow.aspx?FK_Flow=" + rowdata.FK_Flow
                       + "&FK_Node=" + rowdata.FK_Node + "&FID=" + rowdata.FID +"&T="+ dateNow+ "&WorkID=" + rowdata.WorkID
                       + "&IsRead=0','" + rowdata.MyPK
                       + "');><img align='middle' alt='' src='Img/Menu/Mail_UnRead.png' border=0 id='" + rowdata.MyPK
                       + "'/>" + rowdata.Title + "</a>";
                       return title;
                   }
                   },
                   { display: '流程名称', name: 'FlowName' },
                   { display: '当前节点', name: 'NodeName' },
                   { display: '发起人', name: 'StarterName' },
                   { display: '发起日期', name: 'RDT' },
                   { display: '接受日期', name: 'SDTOfFlow' },
                   { display: '期限', name: 'SDTOfNode' },
                   { display: '状态', name: 'WFState', render: function (rowdata, rowindex) {
                       var datePattern = /^(\d{4})-(\d{1,2})-(\d{1,2})$/;
                       if (datePattern.test(rowdata.SDTOfNode)) {
                           var d1 = new Date(dateNow.replace(/-/g, "/"));
                           var d2 = new Date(rowdata.SDTOfFlow.replace(/-/g, "/"));

                           if (Date.parse(d1) <= Date.parse(d2)) {
                               return "正常";
                           }
                           return "<font color=red>逾期</font>";
                       }
                   } }
                   ],
            pageSize: 20,
            data: pushData,
            rownumbers: true,
            height: "99%",
            width: "99%",
            columnWidth: 100,
            onReload: LoadGrid,
            onDblClickRow: function (rowdata, rowindex) {
                WinOpenIt("../WF/MyFlow.aspx?FK_Flow=" + rowdata.FK_Flow
                       + "&FK_Node=" + rowdata.FK_Node + "&FID=" + rowdata.FID + "&WorkID=" + rowdata.WorkID
                       + "&IsRead=0&T="+dateNow, rowdata.MyPK);
            }
        });
    }
    else {
        $.ligerDialog.warn('加载数据出错，请关闭后重试！');
    }
    $("#pageloading").hide();
}
//打开窗体
function WinOpenIt(url, imgId) {
    var isReadImg = document.getElementById(imgId);
    if (isReadImg) isReadImg.src = "Img/Menu/Mail_Read.png";
    var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    newWindow.focus();
    return;
}
//加载挂起列表
function LoadGrid() {
    $("#pageloading").show();
    Application.data.getHungUpList(callBack, this);
}
var dateNow = "";
$(function () {
    dateNow = "";
    var date = new Date();
    dateNow += date.getFullYear(); //年
    dateNow += date.getMonth() + 1; //月 月比实际月份要少1
    dateNow += date.getDate(); //日
    dateNow += date.getHours(); //HH
    dateNow += date.getMinutes(); //MM
    dateNow += date.getSeconds(); //SS

    LoadGrid();
});