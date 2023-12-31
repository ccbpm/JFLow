﻿/*
 * 说明: &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 菜单API  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
 * 1. 该API 是被发起、待办、在途三个菜单列表页面引入，并获取的数据的API。
 * 2. 其中需要  config.js 获得 ccbpmHostDevelopAPI 定义的服务器IP. 
 * 3. 需要cookies 中的sid 校验码(token).
 * 4. 获取流程发起列表: DB_Start()
 * 5. 获得待办 DB_Todolist()
 * 6. 获得在途 DB_Runing()
 * 7. 打开表单 OpenForm() 发起、待办、在途三个列表都需要打开表单.
 */

//获得发起列表.
function DB_Start() {
    var myurl = ccbpmHostDevelopAPI + "DB_Start?token=" + GetToken() + "&domain=" + domain;
    var json = RunUrlReturnJSON(myurl);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 1.获得待办. 返回的Json数据源.
 * 2.列: Title,WorkID,FK_Flow,FK_Node .
 * 3.获得该数据源,调用
 * */
function DB_Todolist() {
    var myurl = ccbpmHostDevelopAPI + "DB_Todolist?token=" + GetToken() + "&domain=" + domain + "&t=" + new Date().getTime();
    var json = RunUrlReturnJSON(myurl);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

//获得在途.
function DB_Runing() {
    var myurl = ccbpmHostDevelopAPI + "DB_Runing?token=" + GetToken() + "&domain=" + domain;
    var json = RunUrlReturnJSON(myurl);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

//获得草稿.
function DB_Draft() {
    var myurl = ccbpmHostDevelopAPI + "DB_Draft?token=" + GetToken() + "&domain=" + domain;
    var json = RunUrlReturnJSON(myurl);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

//获得流程注册表信息,返回没有完成的数据.
function DB_GenerWorkFlow(flowNo) {
    var myurl = ccbpmHostDevelopAPI + "DB_GenerWorkFlow?token=" + GetToken() + "&flowNo=" + flowNo;
    var json = RunUrlReturnJSON(myurl);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 打开表单， 如果是仅仅传入的是FlowNo就是启动流程.
 * @param {any} flowNo
 * @param {any} nodeID
 * @param {any} workid
 * @param {any} fid
 * @param {any} paras
 */
function OpenForm(flowNo, nodeID, workid, fid, paras) {
    var url = GenerFrmUrl(flowNo, nodeID, workid, fid, paras);
    // 打开工作处理器.
    OpenMyFlow(url);
    //   window.open(url);
}

/**
 * 获得表单的 URL.
 * 该表单的URL存储在开始节点表单方案里.
 * @param {流程编号} flowNo
 * @param {节点ID默认为0} nodeID
 * @param {实例的ID} workID
 * @param {默认为:0} fid
 * @param {参数:格式为 &KeHuBianHao=001&KeHuMingCheng=新疆天业} paras
 */
function GenerFrmUrl(flowNo, nodeID = 0, workid = 0, fid = 0, paras = "") {

    // ccbpmHostDevelopAPI 变量是定义在 /config.js 的服务地址. 访问必须两个参数DoWhat,SID.
    //首先获得表单的URL.
    var myUrl = ccbpmHostDevelopAPI + "GenerFrmUrl?token=" + GetToken() + "&workID=" + workid + "&flowNo=" + flowNo + "&nodeID=" + nodeID + "&fid=" + fid;
    var frmUrl = RunUrlReturnString(myUrl);
    frmUrl += paras;

    //如果包含了通用的工作处理器.
    if (frmUrl.indexOf("WF/MyFlow.htm") >= 0) {
        frmUrl = host + JSON.parse(frmUrl).data;;
    }

    return frmUrl;
}

/*   ******************************************************************* 开发接口JS: *******************************************************************
 * 1. 该文件里提供了一些高级开发接口,
 * 2. 比如：创建WorkID,执行发送,催办. 批量删除.
 * 3. 每个接口都有明确的注释.
 */

/**
 * 创建空白的WorkID.
 * @param {校验码(登录时候产生的)} sid
 * @param {流程编号} flowNo
 */
function Node_CreateBlankWorkID(flowNo) {
    var url = ccbpmHostDevelopAPI + "Node_CreateBlankWorkID?token=" + GetToken() + "&flowNo=" + flowNo;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

function Node_SetDraft(flowNo, workID) {
    var url = ccbpmHostDevelopAPI + "Node_SetDraft?token=" + GetToken() + "&flowNo=" + flowNo + "&workID=" + workID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

function Node_IsCanDealWork(workID) {
    var url = ccbpmHostDevelopAPI + "Node_Node_IsCanDealWork?token=" + GetToken() + "&workID=" + workID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}
/**
 * 保存表单数据到流程实例中
 * @param {any} workid
 * @param {any} paras @Key1=val1@Key2=val2
 */
function Node_SaveParas(workid, paras) {
    //@mhj  这里要对参数格式执行校验,不符合不让保存.
    var url = ccbpmHostDevelopAPI + "Node_SaveParas?token=" + GetToken() + "&paras=" + paras + "&workID=" + workid;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}
/**
 * 执行发送
 * @param {工作实例ID} workid
 * @param {要达到的节点,为0不指定节点,由节点配置自动计算} toNodeID
 * @param {要发送的人员，为null,不指定人员，由流程配置自动计算} toEmps
 * @param {参数，格式为:@Key1=val1@Key2=val2 } paras
 */
function Node_SendWork(workid, toNodeID, toEmps, paras = "") {
    if (paras == null || paras == undefined)
        paras = "";
    paras = paras.replace('@', '&');

    var url = ccbpmHostDevelopAPI + "Node_SendWork?token=" + GetToken();
    url += "&workID=" + workid + "&toNodeID=" + toNodeID;
    url += "&toEmps=" + toEmps + "&1=2" + paras;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 获得可以退回的节点
 * @param {校验码} sid
 * @param {流程编号} flowNo
 * @param {工作实例ID} workid
 * @param {FID} fid
 */
function DB_GenerWillReturnNodes(flowNo, workid, fid = 0) {

    var url = ccbpmHostDevelopAPI + "DB_GenerWillReturnNodes?token=" + GetToken() + "&flowNo=" + flowNo;
    url += "&workID=" + workid + "&fid=" + fid;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 批处理：获得批处理的节点.
 */
function Batch_Init() {
    var url = ccbpmHostDevelopAPI + "Batch_Init?token=" + GetToken() + "&domain=" + domain;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

function WorkCheckModel_Init(nodeID) {
    var url = ccbpmHostDevelopAPI + "WorkCheckModel_Init?token=" + GetToken() + "&nodeID=" + nodeID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

function Node(nodeID) {
    var url = ccbpmHostDevelopAPI + "En_Node?token=" + GetToken() + "&nodeID=" + nodeID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}
function Flow(flowNo) {
    var url = ccbpmHostDevelopAPI + "En_Flow?token=" + GetToken() + "&no=" + flowNo;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}
function Batch_InitDDL(nodeID) {
    var url = ccbpmHostDevelopAPI + "Batch_InitDDL?token=" + GetToken() + "&nodeID=" + nodeID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}
function WorkCheckModel_Send(nodeID, CheckNote, ToNode, ToEmps) {
    var url = ccbpmHostDevelopAPI + "WorkCheckModel_Send?token=" + GetToken() + "&nodeID=" + nodeID + "&toNode=" + ToNode + "&toEmps=" + ToEmps + "&checkNote=" + CheckNote;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

function Batch_Delete(WorkIDs) {
    var url = ccbpmHostDevelopAPI + "Batch_Delete?token=" + GetToken() + "&workIDs=" + WorkIDs;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 退回
 * @param {校验码} sid
 * @param {工作实例ID} workid
 * @param {退回到节点ID} returnToNodeID
 * @param {退回给人员} returnToEmp
 * @param {退回意见} msg
 * @param {是否原路返回?} isBackToThisNode
 */
function Node_ReturnWork(workid, returnToNodeID, returnToEmp, msg, isBackToThisNode = false) {
    var url = ccbpmHostDevelopAPI + "Node_ReturnWork?token=" + GetToken();
    url += "&workID=" + workid;
    url += "&returnToNodeID=" + returnToNodeID;
    url += "&returnToEmp=" + returnToEmp;
    url += "&msg=" + msg;

    if (isBackToThisNode == true)
        url += "&IsBackToThisNode=1";
    else
        url += "&IsBackToThisNode=0";
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 设置流程标题
 * @param  workID 工作ID.
 * @param  title 流程标题
 */
function Flow_SetTitle(workID, title) {
    var url = ccbpmHostDevelopAPI + "Flow_SetTitle?token=" + GetToken();
    url += "&workID=" + workID;
    url += "&title=" + title;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 催办
 * @param {要执行的实例,多个实例用逗号分开比如：1001,1002,1003} workidStrs
 */
function Flow_DoPress(workidStrs, msg) {
    var url = ccbpmHostDevelopAPI + "Flow_DoPress?token=" + GetToken();
    url += "&workIDs=" + workidStrs;
    url += "&msg=" + msg;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 撤销发送,如果产生失败就会返回 err@+失败信息.
 * @param {要执行的实例,多个实例用逗号分开比如：1001,1002,1003} workidStrs
 */
function Flow_DoUnSend(workidStrs) {

    var url = ccbpmHostDevelopAPI + "Flow_DoUnSend?token=" + GetToken();
    url += "&workIDs=" + workidStrs;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 删除流程
 * @param {要删除的实例,多个实例用逗号分开比如：1001,1002,1003} workidStrs
 * @param {是否删除子流程} isDeleteSubFlows
 */
function Flow_BatchDeleteByReal(workidStrs, isDeleteSubFlows = true) {

    var url = ccbpmHostDevelopAPI + "Flow_BatchDeleteByReal?token=" + GetToken();
    url += "&workIDs=" + workidStrs;

    if (isDeleteSubFlows == false)
        url += "&IsDeleteSubFlows=0";
    else
        url += "&IsDeleteSubFlows=1";
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}
/**
 * 恢复删除
 * @param {any} workidStrs
 */
function Flow_BatchDeleteByFlagAndUnDone(workidStrs) {

    var url = ccbpmHostDevelopAPI + "Flow_BatchDeleteByFlagAndUnDone?token=" + GetToken();
    url += "&workIDs=" + workidStrs;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 设置流程结束
 * @param {要执行的实例,多个实例用逗号分开比如：1001,1002,1003} workidStrs
 */
function Flow_DoFlowOver(workidStrs) {

    var url = ccbpmHostDevelopAPI + "Flow_DoFlowOver?token=" + GetToken();
    url += "&workIDs=" + workidStrs;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 批量设置抄送查看完毕
 * @param {any} workidStrs
 */

function CC_BatchCheckOver(workidStrs) {

    var url = ccbpmHostDevelopAPI + "CC_BatchCheckOver?token=" + GetToken();
    url += "&workIDs=" + workidStrs;

    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 删除草稿
 * @param {要执行的实例,多个实例用逗号分开比如：1001,1002,1003} workidStrs
 */
function Flow_DeleteDraft(workidStrs) {

    var url = ccbpmHostDevelopAPI + "Flow_DeleteDraft?token=" + GetToken();
    url += "&workIDs=" + workidStrs;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 移交
 * @param {工作ID} workID
 * @param {要移交到的人} toEmpNo
 * @param {移交消息} msg
 */
function Node_Shift(workID, toEmpNo, msg) {

    var url = ccbpmHostDevelopAPI + "Node_Shift?token=" + GetToken();
    url += "&workID=" + workID;
    url += "&toEmpNo=" + toEmpNo;
    url += "&msg=" + msg;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 增加人员
 * @param {工作ID} workID
 * @param {增加的人员ID} empID
 */
function Node_AddTodolist(workID, empID) {

    var url = ccbpmHostDevelopAPI + "Node_AddTodolist?token=" + GetToken();
    url += "&workID=" + workID;
    url += "&empNo=" + empID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}

/**
 * 流程实例的注册信息
 * @param {实例ID} workID
 * @returns 流程实例的注册信息
 */
function Flow_GenerWorkFlow(workID) {

    var url = ccbpmHostDevelopAPI + "Flow_GenerWorkFlow?token=" + GetToken();
    url += "&workID=" + workID;
    var json = RunUrlReturnJSON(url);
    if (json.code == 500) {
        alert(json.msg);
        return;
    }
    var data = json.data;
    var information = JSON.parse(data)
    return information;
}



