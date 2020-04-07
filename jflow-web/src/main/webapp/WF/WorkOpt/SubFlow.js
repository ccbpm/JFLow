if (typeof FrmSubFlowSta == "undefined") {
    var FrmSubFlowSta = {}
    // 不可用
    FrmSubFlowSta.Disable = 0,
        // 可用
        FrmSubFlowSta.Enable = 1,
        // 只读
        FrmSubFlowSta.Readonly = 2

}
if (typeof SFShowCtrl == "undefined") {
    var SFShowCtrl = {}
    // 所有的子线程都可以看到
    SFShowCtrl.All = 0,
        // 仅仅查看我自己的
        SFShowCtrl.MySelf = 1

}
function SubFlow_Init() {
    //查询出来所有子流程的数据.
    var sf = new Entity("BP.WF.Template.FrmSubFlow", GetQueryString("FK_Node"));
    var node = new Entity("BP.WF.Node", GetQueryString("FK_Node"));
    if (sf.SFDefInfo.trim().Length == 0)
        return;

    var html = "";

    html += "<table width='100%'>";
    html += "<tr>";
    html += "<th class='TitleExt'>发起人</th>";
    html += "<th class='TitleExt'>标题</th>";
    html += "<th class='TitleExt'>停留节点</th>";
    html += "<th class='TitleExt'>状态</th>";
    html += "<th class='TitleExt'>处理人</th>";
    html += "<th class='TitleExt'>处理时间</th>";
    html += "<th class='TitleExt'>信息</th>";
    html += "</tr>";

    var emps = null;
    var strs = sf.SFDefInfo.split(',');
    var str;
    var tdHtml = "";
    var webUser = new WebUser();

    var workID = GetQueryString("WorkID");
    var flowNo = GetQueryString("FK_Flow");
    var nodeID = GetQueryString("FK_Node");

    var gwf = null;

    var subFlows = new Entities("BP.WF.Template.SubFlowHands");
    subFlows.Retrieve("FK_Node", nodeID, "SubFlowType", 0, "Idx");

    //要兼容旧版本.
    if (subFlows.length == 0 && sf.SFDefInfo != "") {

        for (var idx = 0; idx < strs.length; idx++) {
            var flowNo = strs[idx];
            if (flowNo == null || flowNo == "")
                continue;

            var en = new Entity("BP.WF.Template.SubFlowHand");
            en.FK_Node = nodeID;
            en.SubFlowNo = flowNo;

            en.SetPKVal(flowNo + "_" + nodeID + "_0");
            en.Insert();
        }
    }


    //遍历子流程配置。
    var pworkID = GetQueryString("WorkID");
    for (var i = 0; i < subFlows.length; i++) {

        var subFlow = subFlows[i];

        if (sf.SFSta == FrmSubFlowSta.Enable && GetQueryString("DoType") != "View") {

            if (subFlow.SubFlowModel == 0 || subFlow.SubFlowModel == null) { //下级子流程.
                tdHtml = "<div style='float:left'><img src='../Img/Max.gif' />&nbsp;" + subFlow.SubFlowName + "</div> <div style='float:right'>[<a href=\"javascript:OpenIt('../MyFlow.htm?IsStartSameLevelFlow=0&FK_Flow=" + subFlow.SubFlowNo + "&PWorkID=" + workID + "&PNodeID=" + nodeID + "&PFlowNo=" + flowNo + "&PFID=" + GetQueryString("FID") + "')\"  >" + sf.SFCaption + "</a>]</style>";
            }

            if (subFlow.SubFlowModel == 1) { //平级子流程.

                if (gwf == null)
                    gwf = new Entity("BP.WF.GenerWorkFlow", workID);

                //如果当前的流程不是子流程，就不处理.
                if (gwf.PWorkID == 0) {
                    tdHtml = "<div style='float:left'><img src='../Img/Max.gif' />&nbsp;" + subFlow.SubFlowName + "</div> <div style='float:right'>为子流程的时候才能启动(" + subFlow.SubFlowName + ")]</style>";
                } else {
                    pworkID = gwf.PWorkID;
                    //传递启动该子流程的流程的信息 IsSameLevel = 1;SLWorkID=workId 
                    tdHtml = "<div style='float:left'><img src='../Img/Max.gif' />&nbsp;" + subFlow.SubFlowName + "</div> <div style='float:right'>[<a href=\"javascript:OpenIt('../MyFlow.htm?FK_Flow=" + subFlow.SubFlowNo + "&PWorkID=" + gwf.PWorkID + "&PNodeID=" + gwf.PNodeID + "&PFlowNo=" + gwf.PFlowNo + "&PFID=" + gwf.PFID + "&IsStartSameLevelFlow=1&SLWorkID=" + workID + "&SLNodeID=" + nodeID + "&SLFlowNo=" + flowNo + "')\"  >" + sf.SFCaption + "</a>]</style>";
                }
            }
        }

        if (sf.SFSta == FrmSubFlowSta.Readonly || GetQueryString("DoType") == "View")
            tdHtml = "<div style='float:left'><img src='../Img/Max.gif' />&nbsp;" + subFlow.SubFlowName + "</div></style>";

        html += "<tr>";
        html += "<td class='TRSum' colspan=7 >" + tdHtml + "</td>";
        html += "</tr>";

        //该流程的子流程信息.
        var gwfs = new Entities("BP.WF.GenerWorkFlows");
        if (sf.SFShowCtrl == SFShowCtrl.All)
            gwfs.Retrieve("PWorkID", pworkID, "FK_Flow", subFlow.SubFlowNo); //流程.          
        else
            gwfs.Retrieve("PWorkID", pworkID, "FK_Flow", subFlow.SubFlowNo, "Starter", webUser.No); //流程.

        for (var j = 0; j < gwfs.length; j++) {

            var item = gwfs[j];
            if (item.WFState == 0)
                continue;
            //平级子流程，获取平级的workID
            var slWorkID = GetPara(item.AtPara, "SLWorkID");
            if (slWorkID != null && slWorkID != undefined && slWorkID != GetQueryString("WorkID"))
                continue;

            html += "<tr>";
            if (item.StarterName == null || item.StarterName == "")
                html += "<td nowrap>&nbsp;</td>";
            else
                html += "<td nowrap>" + item.StarterName + "</td>";

            if (item.TodoEmps.indexOf("" + webUser.No + "," + webUser.Name + ";") >= 0) {
                html += "<td  style='word-break:break-all;' title='" + item.Title + "'>";
                html += "<a href=\"javascript:OpenIt('../MyFlow.htm?WorkID=" + item.WorkID + "&FK_Flow=" + item.FK_Flow + "&IsCheckGuide=1&Frms=" + item.Paras_Frms + "&FK_Node=" + item.FK_Node + "&PNodeID=" + item.PNodeID + "&PWorkID=" + item.PWorkID + "')\" ><img src='../Img/Dot.png' width='9px' />&nbsp;" + item.Title + "</a></td>";
            } else {
                if (sf.SFOpenType == 0) {
                    html += "<td  style='word-break:break-all;' title='" + item.Title + "'>";
                    html += "<a href=\"javascript:OpenIt('../WFRpt.htm?WorkID=" + item.WorkID + "&FK_Flow=" + item.FK_Flow + "&PWorkID=" + item.PWorkID + "&PFlowNo=" + item.PFlowNo + "&PNodeID=" + item.PNodeID + "')\" ><img src='../Img/Dot.png' width='9px' />&nbsp;" + item.Title + "</a></td>";
                } else {
                    html += "<td style='word-break:break-all;' title='" + item.Title + "'>";
                    html += "<a href=\"javascript:OpenIt('../WorkOpt/FoolFrmTrack.htm?WorkID=" + item.WorkID + "&FK_Flow=" + item.FK_Flow + "')\" ><img src='../Img/Dot.png' width='9px' />&nbsp;" + item.Title + "</a></td>";
                }
            }
            //到达节点名称.
            if (item.NodeName == null || item.NodeName == "")
                html += "<td nowrap>&nbsp;</td>";
            else
                html += "<td nowrap>" + item.NodeName + "</td>";
            //流程的状态 
            if (item.WFStateText == null || item.WFStateText == "")
                html += "<td nowrap>&nbsp;</td>";
            else
                html += "<td nowrap>" + item.WFStateText + "</td>";

            var emps = item.TodoEmps.split(';');
            var myemps = "";

            for (var idx = 0; idx < emps.length; idx++) {

                var empstrs = emps[idx];
                if (empstrs == null)
                    continue;

                if (empstrs == '' || empstrs.length == 0 || empstrs == null)
                    continue;

                empstrs = emps[idx].split(',');
                myemps += "" + empstrs[1] + ",";
            }


            //到达人员.
            html += "<td title='" + item.TodoEmps + "'>" + myemps + "</td>";

            //日期.
            if (item.RDT == null || item.RDT == "")
                html += "<td nowrap>&nbsp;</td>";
            else
                html += "<td nowrap>" + item.RDT + "</td>";

            //流程备注.
            if (item.FlowNote == null)
                html += "<td title='" + item.FlowNote + "'></td>";
            else
                html += "<td title='" + item.FlowNote + "'>" + item.FlowNote + "</td>";

            html += "</tr>";

        }
    }
    html += "</table>";
    return html;
}

function GenerSpace(spaceNum) {
    if (spaceNum <= 0)
        return "";

    var strs = "";
    while (spaceNum != 0) {
        strs += "&nbsp;&nbsp;";
        spaceNum--;
    }
    return strs;
}