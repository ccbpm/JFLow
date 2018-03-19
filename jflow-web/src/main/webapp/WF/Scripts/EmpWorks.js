//加载待办列表
function LoadGrid() {
    var strTimeKey = "";
    var date = new Date();
    strTimeKey += date.getFullYear(); //年
    strTimeKey += date.getMonth() + 1; //月 月比实际月份要少1
    strTimeKey += date.getDate(); //日
    strTimeKey += date.getHours(); //HH
    strTimeKey += date.getMinutes(); //MM
    strTimeKey += date.getSeconds(); //SS
    var month = date.getMonth() + 1;
    var dateNow = date.getFullYear() + "-" + month + "-" + date.getDate();

    var grid = $("#maingrid").datagrid({
        title: '待办列表',
        height: "auto",
        width: "auto",
        nowrap: true,
        fitColumns: true,
        autoRowHeight: false,
        singleSelect: true,
        striped: true,
        collapsible: false,
        url: 'Base/DataService.aspx?method=getempworks',
        pagination: false,
        rownumbers: true,
        columns: [[
                   { title: '标题', field: 'Title', width: 360, align: 'left', formatter: function (value, rec) {
                       var title = "";

                       if (rec.IsRead == 0) {
                           title = "<a href=\"javascript:WinOpenIt('../WF/MyFlow.aspx?FK_Flow=" + rec.FK_Flow + "&FK_Node=" + rec.FK_Node
                           + "&FID=" + rec.FID + "&WorkID=" + rec.WorkID + "&AtPara=" + rec.AtPara + "&IsRead=0&T=" + strTimeKey
                           + "','" + rec.WorkID + "','" + rec.FlowName + "');\" ><img align='middle' alt='' id='" + rec.WorkID
                           + "' src='Img/Menu/Mail_UnRead.png' border=0 width=20 height=20 />" + rec.Title + "</a>";
                       } else {
                           title = "<a href=\"javascript:WinOpenIt('../WF/MyFlow.aspx?FK_Flow=" + rec.FK_Flow + "&FK_Node=" + rec.FK_Node
                           + "&FID=" + rec.FID + "&T=" + strTimeKey + "&WorkID=" + rec.WorkID + "&AtPara=" + rec.AtPara + "','" + rec.WorkID
                           + "','" + rec.FlowName + "');\"  ><img align='middle' border=0 width=20 height=20 id='" + rec.WorkID + "' alt='' src='Img/Menu/Mail_Read.png'/>" + rec.Title + "</a>";
                       }
                       return title;
                   }
                   },
                   { title: '流程名称', field: 'FlowName' },
                   { title: '当前节点', field: 'NodeName' },
                   { title: '发起人', field: 'StarterName' },
                   { title: '发起日期', field: 'RDT' },
                   { title: '接受日期', field: 'ADT' },
                   { title: '期限', field: 'SDT' },
                   { title: '状态', field: 'FlowState', formatter: function (value, rec) {
                       //var datePattern = /^(\d{4})-(\d{1,2})-(\d{1,2})$/;
                       if (rec.SDTOfNode != "") {
                           var d1 = new Date();
                           var d2 = new Date(rec.SDTOfNode.replace(/-/g, "/"));
                           // if ((Date.parse(d1) > Date.parse(d2))) {

                           if (d1 > d2) {
                               return "<font color=red>逾期</font>";
                           }
                           return "正常";
                       }
                   }
                   }
                   ]]
    });
//    var p = $('#maingrid').datagrid('getPager');
//    $(p).pagination({
//        pageSize: 20,
//        pageList: [20, 50, 100],
//        beforePageText: '第',
//        afterPageText: '页    共 {pages} 页',
//        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
//    });
    $("#pageloading").hide();
}
//打开窗体
function WinOpenIt(url, workId, text) {
    var isReadImg = document.getElementById(workId);
    if (isReadImg) isReadImg.src = "Img/Menu/Mail_Read.png";
    if (ccflow.config.IsWinOpenEmpWorks.toUpperCase() == "TRUE") {
        var winWidth = 850;
        var winHeight = 680;
        if (screen && screen.availWidth) {
            winWidth = screen.availWidth;
            winHeight = screen.availHeight - 36;
        }
        //var newWindow = window.open(url, "_blank", "height=" + winHeight + ",width=" + winWidth + ",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no");
        //newWindow.focus();
        try {
            var vReturnValue = window.showModalDialog(url, "_blank", "scrollbars=yes;resizable=yes;center=yes;dialogWidth=" + winWidth + ";dialogHeight=" + winHeight + ";dialogTop=0px;dialogLeft=0px;");

        } catch (ex) {
            window.open(url, "_blank", "height=" + winHeight + ",width=" + winWidth + ",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no");
        }
        LoadGrid();

    } else {
        window.parent.f_addTab(workId, text, url);
    }
}

$(function () {
    $("#pageloading").show();
    LoadGrid();
});