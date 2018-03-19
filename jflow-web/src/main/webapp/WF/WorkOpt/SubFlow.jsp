<%@page import="BP.WF.WFState"%>
<%@page import="BP.WF.GenerWorkFlowAttr"%>
<%@page import="BP.WF.Entity.GenerWorkFlows"%>
<%@page import="BP.WF.Entity.GenerWorkFlow"%>
<%@page import="BP.WF.Node"%>
<%@page import="cn.jflow.common.model.WorkCheckM"%>
<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.Sys.FrmWorkShowkz"%>
<%
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	int FK_Node=Integer.valueOf(request.getParameter("FK_Node"));
 	long FID=Long.valueOf(StringHelper.isEmpty(request.getParameter("FID"), "0"));
 	String FK_Flow=request.getParameter("FK_Flow");
 	String s = request.getParameter("Paras");
 	 
    
 	long WorkID=0;
    String workid = request.getParameter("OID");
    if (workid == null)
        workid = request.getParameter("WorkID");
    WorkID = Long.valueOf(workid);
    
%>
<!DOCTYPE>
<html>
<head>
	<script type="text/javascript">
	    function Del(fk_flow, workid) {
	        if (window.confirm('您确定要删除吗？') == false)
	            return;
	    }
	    function OpenIt(url) {
	    	debugger;
	        var newWindow = window.open(url, 'card', 'width=950,top=50,left=50,height=600,scrollbars=yes,resizable=yes,toolbar=false,location=false');
	        newWindow.focus();
	        return;
	    }
	</script>
	<link href="<%=basePath%>WF/Comm/Style/Table.css" rel="stylesheet" type="text/css" /><link href="../Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>WF/Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
</head>
<body leftMargin=0 topMargin=0 >
   <%
   
    BP.DA.SB sb=new BP.DA.SB();  //没有找到sb这个类，  后面还调用了N多的sb，所以，有可能是sb这个类没有生成，或者..是残留的垃圾.?
    
    //查询出来所有子流程的数据.
	BP.Sys.FrmSubFlow sf = new BP.Sys.FrmSubFlow(FK_Node);

	Node nd = new Node(FK_Node);

	sb.AddTable(" width='100%' ");
	if (sf.getSFCaption().length() !=0)
	{
		sb.AddCaption(sf.getSFCaption()); //标题可以为空
	}

	if (sf.getSFDefInfo().trim().length() == 0)
	{
		return;
	}

	sb.AddTR();
	sb.AddTDTitle("标题");
	sb.AddTDTitle("停留节点");
	sb.AddTDTitle("状态");
	sb.AddTDTitle("处理人");
	sb.AddTDTitle("处理时间");
	sb.AddTDTitle("信息");
	sb.AddTREnd();

	//有要启动的子流程, 生成启动子流程的连接.
	String html = "";
	String[] strs = sf.getSFDefInfo().split("[,]", -1);
	for (String str : strs)
	{
		if ( str==null || str=="" )
		{
			continue;
		}

		if (str.length() != 3)
		{
			continue;
		}

		//输出标题.
		BP.WF.Flow fl = new BP.WF.Flow(str);
		html = "<div style='float:left'><img src='../Img/Min.gif' />&nbsp;" + fl.getName() + "</div> <div style='float:right'><a href=\"javascript:OpenIt('../MyFlow.htm?FK_Flow=" + fl.getNo() + "&PWorkID=" +WorkID + "&PNodeID=" + sf.getNodeID() + "&PFlowNo=" + nd.getFK_Flow() + "&PFID="+FID+"')\"  >[启动流程]</a></style>";
		sb.AddTR();
		sb.AddTD(" class=TRSum colspan=6", html);
		sb.AddTREnd();

		//该流程的子流程信息.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		if (sf.getSFShowCtrl() == FrmWorkShowkz.All.getValue())
        {
            gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, WorkID, GenerWorkFlowAttr.FK_Flow, str); //流程.
        }
        else
        {
            gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, WorkID,
                GenerWorkFlowAttr.FK_Flow, str, GenerWorkFlowAttr.Starter, BP.Web.WebUser.getNo()); //流程.
        }

		for (GenerWorkFlow item : GenerWorkFlows.convertGenerWorkFlows(gwfs))
		{

            if (item.getWFState() == WFState.Blank)
                continue;
            
			sb.AddTR();
			sb.AddTD("style='word-break:break-all;'", "<a href=\"javascript:OpenIt('../WFRpt.jsp?WorkID=" + item.getWorkID() + "&FK_Flow=" + item.getFK_Flow() + "')\" ><img src='../Img/dot.png' width='9px' />&nbsp;" + item.getTitle() + "</a>");

			sb.AddTD(item.getNodeName()); //到达节点名称.

			if (item.getWFState().getValue() == 3)
			{
				sb.AddTD("已完成");
			}
			else
			{
				sb.AddTD("未完成");
			}

			sb.AddTD(item.getTodoEmps()); //到达人员.
			//sb.AddTD(BP.DA.DataType.ParseSysDate2DateTimeFriendly(item.getRDT())); //日期.
			sb.AddTD(item.getRDT()); 
			sb.AddTD(item.getFlowNote()); //流程备注.
			sb.AddTREnd();

			//加载他下面的子流程.
			InsertSubFlows(item.getFK_Flow(), item.getFK_Node(), item.getWorkID(), 1, sb);
		}
	}
	sb.AddTableEnd();
   %>
   <%= sb.toStr() %>

<div id="DelMsg"></div>
</body>
</html>

   <%!
   public final void InsertSubFlows(String flowNo, int fid, long workid, int layer, BP.DA.SB sb)
	{
		//该流程的子流程信息, 并按照流程排序.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, workid, GenerWorkFlowAttr.FK_Flow); //流程.

		if (gwfs.size() == 0)
		{
			return;
		}

		String myFlowNo = "";
		for (GenerWorkFlow item : GenerWorkFlows.convertGenerWorkFlows(gwfs))
		{
            if (item.getWFState() == WFState.Blank)
                continue;
            
			if (myFlowNo.contains(item.getFK_Flow())==false)
			{
				myFlowNo = myFlowNo + "," + item.getFK_Flow();

				//输出流程.
				BP.WF.Flow fl = new BP.WF.Flow(item.getFK_Flow());
				sb.AddTR();
				sb.AddTD(" class=TRSum colspan=6", "<div style='float:left'>" + DataType.GenerSpace(layer * 2) + "<img src='/WF/Img/Max.gif' />&nbsp;" + fl.getName() + "</div>");
				sb.AddTREnd();
			}

			sb.AddTR();
			sb.AddTD("style='word-break:break-all;'", DataType.GenerSpace(layer * 2) + "<a href=\"javascript:OpenIt('/WF/WFRpt.jsp?WorkID=" + item.getWorkID() + "&FK_Flow=" + item.getFK_Flow() + "')\" ><img src='/wf/img/dot.png' width='9px' />&nbsp;" + item.getTitle() + "</a>");
			sb.AddTD(item.getNodeName()); //到达节点名称.
			if (item.getWFState() == WFState.Complete)
			{
				sb.AddTD("已完成");
			}
			else
			{
				sb.AddTD("未完成");
			}

			sb.AddTD(item.getTodoEmps()); //到达人员.
			//sb.AddTD(BP.DA.DataType.ParseSysDate2DateTimeFriendly(item.RDT)); //日期.
			sb.AddTD(item.getRDT()); //日期.
			sb.AddTD(item.getFlowNote()); //流程备注.
			sb.AddTREnd();

			//加载他下面的子流程.
			InsertSubFlows(item.getFK_Flow(), item.getFK_Node(), item.getWorkID(), layer + 1, sb);
		}
	} 
   %>
