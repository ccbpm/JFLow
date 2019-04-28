/*

1. 该JS文件被嵌入到了MyFlowGener.htm 的工作处理器中.
2. 开发者可以重写该文件处理通用的应用,比如通用的函数.

*/

function GuiDang() {

    if (window.confirm('您确定要归档吗?') == true)
        return true;

    return false;
}

function Node108() {

    if (window.confirm('您确定完成吗? 完成后就会发送到归档节点。') == true)
        return true;

    return false;
}




function DZ() {

    alert('sss');
    var url = 'pop.htm';

    window.open(url);
}

//行领导发送的时候提示发送到
function BankLeaderAlert() {

    return false;
}

//001流程的106节点 分行领导向下发送的时候，检查是否未完成的子流程？
function CheckSub003Flows() {

    //   alert('检查提交.');

    //检查当前人员是否是主办单位人员.
    var workID = GetQueryString("WorkID");

    //求出当前单位的主办人.
    var dbs = DBAccess.RunSQLReturnTable("SELECT BLDWName FROM ND2Rpt WHERE PWorkID=" + workID + " AND WFState!=3 ");
    if (dbs.length == 0)
        return true;

    var info = "";
    for (var i = 0; i < dbs.length; i++) {

        var db = dbs[i];
        info += "@" + db.BLDWName + "\t\n";
    }

    var msg=info + "\t\n还有以上[" + dbs.length + "]个单位没有完成工作，分行办公室人员不能提交。";

    if (window.confirm(msg) == true)
        return true;

    return false;
}


//检查子流程是否存在？
function CheckSubFlows() {

    //   alert('检查提交.');

    //检查当前人员是否是主办单位人员.
    var workID = GetQueryString("WorkID");

    //求出当前单位的主办人.
    var todoEmps = DBAccess.RunSQLReturnVal("SELECT TodoEmps FROM WF_GenerWorkFlow WHERE WorkID = ( SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID='" + workID + "')");

    //获得当前用户信息.
    var webUser = new WebUser();
    if (todoEmps.indexOf(webUser.No) == -1) {
        /*说明:当前人员不是主办单位的处理人. */
        return true;
    }

    var returnType = DBAccess.RunSQLReturnVal("SELECT ReturnType  FROM nd2rpt  WHERE OID='" + workID + "'");
    if (returnType == 1)
        return true;  //如果是办公室返回,就不提示.

    //检查是否有当前未完成的子流程？
    //求出当前单位的主办人.
    var dbs = DBAccess.RunSQLReturnTable("SELECT BLDWName FROM ND2Rpt WHERE FlowEndNode!=305 AND PWorkID = ( SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID='" + workID + "') AND  (WFState <> 3)  AND BLDWName not like '%主办%' ");


    var num = DBAccess.RunSQLReturnVal("SELECT COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow  WHERE FK_Node!=305 AND PWorkID = ( SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID='" + workID + "') AND  (WFState=2 or WFState=5)");
    if (num == 1)
        return true;  //自己就是最后一个人.

    //    if (dbs.length == 1)
    //        return true;  //自己就是最后一个人.

    var info = "";
    for (var i = 0; i < dbs.length; i++) {
        info += dbs[i].BLDWName + "\t\n";
    }

  //提问操作者是否需要提交.
    var msg = "\t\n您是该事项的主办单位，如下:\t\n " + info + " 共: " + (num - 1) + "个辅办单位没有完成，您确定要提交吗？";
    // msg += "如果您要提交，他们的工作将会设置未完成状态。";
    //提问操作者是否需要提交.
   // var msg = "\t\n您是该事项的主办单位，您有[" + info + "] "+(num - 1) + "个协办没有完成，您确定要提交吗？";
    // msg += "如果您要提交，他们的工作将会设置未完成状态。";

    if (window.confirm(msg) == false)
        return false;

    // alert("将要提交.");
    return true;
}

 