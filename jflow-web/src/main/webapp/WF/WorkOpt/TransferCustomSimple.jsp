<%@page import="org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="cn.jflow.common.model.TransferCustomSimpleModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.WF.Template.NodeAttr"%>
<%@page import="BP.WF.Template.TransferCustoms"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	TransferCustomSimpleModel tcsm = new TransferCustomSimpleModel(request,response);
	tcsm.pageLoad(request,response);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>流转自定义(Dom插入版)</title>
  <link href="../Scripts/easyUI15/themes/icon.css" rel="stylesheet" type="text/css" />
    <link href="../Scripts/easyUI15/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <script src="../Scripts/easyUI15/jquery.min.js" type="text/javascript"></script>
    <script src="../Scripts/easyUI15/jquery.easyui.min.js" type="text/javascript"></script>
    <link href="../Comm/JS/Calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript" src="../Comm/JS/Calendar/WdatePicker.js" defer="defer"
        type="text/javascript"></script>
</head>
<body>
<form id="form1" runat="server">
    <style type="text/css">
        .table
        {
            border: 1px outset #C0C0C0;
            padding: inherit;
            margin: 0;
            border-collapse: collapse;
        }
        th
        {
            border-width: 1px;
            border-color: #C2D5E3;
            border-style: solid;
            line-height: 25px;
            color: 0a0a0a;
            white-space: nowrap;
            padding: 0 2px;
            background-color: #e0ecff;
            font-size: 14px;
            text-align: left;
            font-size: 12px;
            font-weight: normal;
            line-height: 26px;
        }
        td
        {
            border-right-style: none;
            border-left-style: none;
            border-style: solid;
            padding: 4px;
            text-align: left;
            color: #333333;
            font-size: 12px;
            border-width: 1px;
            border-color: #C2D5E3;
        }
        .Idx
        {
            font-size: 12px;
            border: 1px solid #ccc;
            text-align: center;
            font-weight: bold;
            width: 20px;
            line-height: 26px;
        }
        .TBcalendar
        {
            background-position: left center;
            border-style: none none dotted none;
            border-width: 1px;
            border-color: #003366;
        }
    </style>
    <table class="table" cellpadding="0" cellspacing="0" style="width: 100%">
        <thead>
            <tr>
                <th style="width: 80px; text-align: center">
                    编号
                </th>
                <th style="width: 140px">
                    步骤
                </th>
                <th style="width: 240px">
                    处理人
                </th>
                <th>
                    预计处理日期
                </th>
            </tr>
        </thead>
        <tbody>
            <%
                String flowNo = request.getParameter("flowNo");
                if (StringHelper.isNullOrEmpty(flowNo)) flowNo = "002";
                long workid = Dev2Interface.Node_CreateBlankWork(flowNo);
                GenerWorkFlow gwf = new GenerWorkFlow(workid);
                TransferCustoms tcs = new TransferCustoms (workid);
                GenerWorkerLists gwls = new GenerWorkerLists(workid);
                Nodes nodes = new Nodes();
                nodes.Retrieve(NodeAttr.FK_Flow, flowNo, "Step");
                TransferCustom tc = null;
                String es = "";
                String load = "";
                
 //               String host = request.getRequestURL().Scheme + "://" +request.getRequestURL().Authority;
                String host = BP.Sys.Glo.getRequest().getRemoteHost();
//                String host = SystemConfig.GetValByKey("SendEmailHost", "smtp.gmail.com"); smtp服务器
                for(Node node : nodes.ToJavaList())
                {
                    if (node.getIsStartNode() == true)
                    {
            %>
            <tr>
                <td class="Idx">
                    <input type="hidden" id="hidWorkId" value="<%=workid %>" />
                    <script language="javascript" type="text/javascript">
                        var isLoad = false;

                        function saveCfg(cmb, newPlan) {
                            if(isLoad) {
                                return;
                             }
                            var ids = cmb.id.split('_');
							var method = "savecfg";
                            var nodeId=ids[1];
                            var empNos=$(cmb).combobox('getValues').toString();
                            var empNames=$(cmb).combobox('getText');
                            var step=ids[2];
                            var plan=newPlan ? newPlan : $('#plan_' + ids[1]).val();
                            
                            $.ajax({
                                type:'post',
                                dataType:'json',
                                contentType: 'application/json; charset=utf-8',
                                url:"<%=basePath%>WF/WorkOpt/TransferCustomSimple.do?method=savecfg&workId=<%=workid %>&nodeId="
                                		+nodeId+"&empNos="+empNos+"&empNames="+empNames+"&step="+step+"&plan="+plan, 
                                error: function (XMLHttpRequest, errorThrown) {
                                    alert('错误：' + XMLHttpRequest);
                                },
                                success: function (msg) {
                                    var data = $.parseJSON(msg);
                                    if(!data.success){
                                        alert(data.msg);
                                    }
                                }
                            });
                        }
                    </script>
                    <%=node.getNodeID() %>
                </td>
                <td>
                    <%=node.getName() %>
                </td>
                <td>
                    <%=gwf.getStarterName()%>
                </td>
                <td>
                    <%=gwf.getRDT() %>
                </td>
            </tr>
            <%
                        continue;
                    }

                    GenerWorkerList gwl =(GenerWorkerList)gwls.GetEntityByKey(GenerWorkerListAttr.FK_Node,node.getNodeID());

                    if (gwl == null)
                    {
                        /* 还没有到达的节点. */

                    tc =(TransferCustom)tcs.GetEntityByKey(GenerWorkerListAttr.FK_Node, node.getNodeID());

                        if (tc == null)
                            tc = new TransferCustom();

                        es = "[";

                        if (!StringHelper.isNullOrEmpty(tc.getWorker()))
                        {
                            for(String e : tc.getWorker().split(","))
                                es += "'" + e + "',";
                        }
						if(",".equals(es.substring(es.length() - 1, es.length()))){
							es = es.substring(0, es.length() - 1);
   						}
                        es += "]";

                        load = es.length() > 2 ? ",onLoadSuccess:function(){ isLoad = true; $(this).combobox('setValues', " + es + "); isLoad = false; }" : "";
                    
            %>
            <tr>
                <td class="Idx">
                    <%=node.getNodeID() %>
                </td>
                <td>
                    <%=node.getName() %>
                </td>
                <td>
                    <input id="emps_<%=node.getNodeID() %>_<%=node.getStep() %>" class="easyui-combobox" style="width: 220px"
                        data-options="url:'<%=basePath%>/WF/WorkOpt/TransferCustomSimple.jsp?method=findemps&workId=<%=workid %>&nodeId=<%=node.getNodeID() %>',
                    method:'get',
                    onChange:function(){
                        var ids = this.id.split('_');
                        saveCfg(this);                        
                    },
                    multiple:true,
                    valueField:'no',
                    textField:'name',
                    groupField:'dept'<%=load %>" 
                    />
                </td>
                <td>
                    <input id="plan_<%=node.getNodeID() %>" type="text" class="Wdate" style="width: 100px;"
                        onfocus="WdatePicker({dchanged:function(){saveCfg($('input[id*=\'emps_'+this.id.split('_')[1]+'_\'')[0], $dp.cal.getNewDateStr())}})"
                        value="<%=tc.getPlanDT() %>" />
                </td>
            </tr>
            <%
                        continue;
                    }

                    //已经走完节点
            %>
            <tr>
                <td class="Idx">
                    <%=node.getNodeID() %>
                </td>
                <td>
                    <%=node.getName() %>
                </td>
                <td>
                    <%=gwl.getFK_EmpText()%>
                </td>
                <td>
                    <%=gwl.getRDT()%>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    </form>
</body>
</html>