<%@page import="BP.DA.DataColumn"%>
<%@page import="BP.Sys.MapAttr"%>
<%@page import="BP.DA.DataTable"%>
<%@page import="BP.WF.HttpHandler.WF_RptDfine"%>
<%@page import="BP.En.QueryObject"%>
<%@page import="BP.Sys.GEEntitys"%>
<%@page import="BP.Sys.MapAttrs"%>
<%@page import="BP.Sys.MapData"%>
<%@page import="BP.Sys.UserRegedit"%>
<%@page import="BP.WF.Template.CCList"%>
<%@page import="BP.WF.Template.CCSta"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.WF.Flow"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.WorkFlow"%>
<%@page import="BP.DA.DataType"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.DA.Log"%>
<%@page import="BP.Port.Emp"%>
<%@page import="BP.WF.Entity.GenerWorkerListAttr"%>
<%@page import="BP.WF.Entity.GenerWorkerList"%>
<%@page import="BP.WF.Node"%>
<%@page import="BP.WF.Port.WFEmps"%>
<%@page import="BP.WF.Port.WFEmp"%>
<%@page import="BP.WF.XML.EventListDtlList"%>
<%@page import="BP.Sys.FrmEvents"%>
<%@page import="BP.Sys.GEDtl"%>
<%@page import="BP.Sys.GEDtls"%>
<%@page import="BP.WF.Data.Bill"%>
<%@page import="BP.Tools.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="cn.jflow.system.ui.core.*"%>
<%@page import="cn.jflow.system.ui.uc.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	
	String fk_flow = request.getParameter("FK_Flow");
	String refNo = request.getParameter("RefNo");
	String ensName = request.getParameter("EnsName");
	String fk_emp = request.getParameter("FK_Emp");
	long workId = request.getParameter("WorkID")==null?0:Long.parseLong(request.getParameter("WorkID"));
	String actionType = request.getParameter("ActionType");
	String nodeId = request.getParameter("NodeID");
	String myPK = request.getParameter("MyPK");
	String refOidStr = request.getParameter("RefOID");
    if (refOidStr == null)
    	refOidStr = request.getParameter("OID");
    if (refOidStr == null)
   		refOidStr =  "0";
     
    int refOid = Integer.parseInt(refOidStr);
     
	int fk_node = 0;
	
	if (actionType == null)
	{
		actionType = request.getParameter("DoType");
	}

	if (StringHelper.isNullOrEmpty(actionType) && request.getParameter("SID") != null)
	{
		actionType = "Track";
	}
	
	if(StringHelper.isNullOrEmpty(nodeId)){
		String node = request.getParameter("FK_Node");
		if(!StringHelper.isNullOrEmpty(node)){
			fk_node = Integer.parseInt(node);
		}
	}else{
		fk_node = Integer.parseInt(nodeId);
	}
	
	PageBase base = new PageBase(request, response);
	try{
		//String url = request.getQueryString();
		//if (url.contains("DTT=") == false)
		//{
			//this.Response.Redirect(url + "&DTT=" + DateTime.Now.ToString("mmDDhhmmss"), true);
			//return;
		//}
		String str = "";
		if (actionType.equals("PutOne")) //把任务放入任务池.
		{
			 Dev2Interface.Node_TaskPoolPutOne(workId);
			 base.WinClose("ss");
		}
		else if (actionType.equals("DoAppTask")) // 申请任务.
		{
				BP.WF.Dev2Interface.Node_TaskPoolTakebackOne(workId);
				base.WinClose("ss");
				return;
		} else if (actionType.equals("DoOpenCC"))
		{
				String fid = request.getParameter("FID"); 
				String Sta = request.getParameter("Sta");
				if (Sta.equals("0"))
				{
					CCList cc1 = new CCList();
					cc1.setMyPK(myPK);
					cc1.Retrieve();
					cc1.setHisSta(CCSta.Read);
					cc1.Update();
				}
				response.sendRedirect("./WorkOpt/OneWork/Track.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workId + "&FID=" + fid);
				return;
		}
		else if (actionType.equals("DelCC")) //删除抄送.
		{
				CCList cc = new CCList();
				cc.setMyPK(myPK);
				cc.Retrieve();
				cc.setHisSta(CCSta.Del);
				cc.Update();
				base.WinClose();
		}
		else if (actionType.equals("DelSubFlow")) //删除进程。
		{
				try
				{
					BP.WF.Dev2Interface.Flow_DeleteSubThread(fk_flow, workId, "手工删除");
					base.WinClose();
				}
				catch (RuntimeException ex)
				{
					base.WinCloseWithMsg(ex.getMessage());
				}
		}
		else if (actionType.equals("DownBill"))
		{
				Bill b = new Bill(myPK);
				b.DoOpen();
		}
		else if (actionType.equals("DelDtl"))
		{
				GEDtls dtls = new GEDtls(ensName);
				GEDtl dtl = (GEDtl)dtls.getGetNewEntity();
				dtl.setOID(Integer.parseInt(refNo));
				if (dtl.RetrieveFromDBSources() == 0)
				{
					base.WinClose();
					return;
				}
				FrmEvents fes = new FrmEvents(ensName); //获得事件.
	
				// 处理删除前事件.
				try
				{
					fes.DoEventNode(EventListDtlList.DtlItemDelBefore, dtl);
				}
				catch (RuntimeException ex)
				{
					base.WinCloseWithMsg(ex.getMessage());
					return;
				}
				dtl.Delete();
	
				// 处理删除后事件.
				try
				{
					fes.DoEventNode(EventListDtlList.DtlItemDelAfter, dtl);
				}
				catch (RuntimeException ex)
				{
					base.WinCloseWithMsg(ex.getMessage());
					return;
				}
				base.WinClose();
		}
		else if (actionType.equals("EmpDoUp"))
		{
				WFEmp ep = new WFEmp(refNo);
				ep.DoUp();
	
				WFEmps emps111 = new WFEmps();
			  //  emps111.RemoveCash();
				emps111.RetrieveAll();
				base.WinClose();
		}
		else if (actionType.equals("EmpDoDown"))
		{
				WFEmp ep1 = new WFEmp(refNo);
				ep1.DoDown();
	
				WFEmps emps11441 = new WFEmps();
			  //  emps11441.RemoveCash();
				emps11441.RetrieveAll();
				base.WinClose();
	
		}
		else if (actionType.equals("Track")) //通过一个串来打开一个工作.
		{
				String mySid = request.getParameter("SID");
				String[] mystrs = mySid.split("_");
	
				long myWorkID = Integer.parseInt(mystrs[1]);
				String fkEmp = mystrs[0];
				int fkNode = Integer.parseInt(mystrs[2]);
				Node mynd = new Node();
				mynd.setNodeID(fkNode);
				mynd.RetrieveFromDBSources();
	
				String fkFlow = mynd.getFK_Flow();
				String myurl = "./WorkOpt/OneWork/Track.jsp?FK_Node=" + mynd.getNodeID() + "&WorkID=" + myWorkID + "&fk_flow=" + fkFlow;
				WebUser.SignInOfGener(new Emp(fkEmp), true);
				
				out.println("<script> window.location.href='" + myurl + "'</script> *^_^*  <br><br>正在进入系统请稍后，如果长时间没有反应，请<a href='" + myurl + "'>点这里进入。</a>");
				return;
		}
		else if (actionType.equals("OF")) //通过一个串来打开一个工作.
		{
				String sid = request.getParameter("SID");
				String[] strs = sid.split("_");
				GenerWorkerList wl = new GenerWorkerList();
				int i = wl.Retrieve(GenerWorkerListAttr.FK_Emp, strs[0], GenerWorkerListAttr.WorkID, strs[1], GenerWorkerListAttr.FK_Node, strs[2]);
				if (i == 0)
				{
					out.println("<h2>提示</h2>此工作已经被别人处理或者此流程已删除。");
					return;
				}
	
				BP.Port.Emp empOF = new BP.Port.Emp(wl.getFK_Emp());
				WebUser.SignInOfGener(empOF, true);
				String u = "MyFlow.htm?fk_flow=" + wl.getFK_Flow() + "&WorkID=" + wl.getWorkID()+"&FK_Node="+wl.getFK_Node()+"&FID="+wl.getFID();
				out.println("<script> window.location.href='" + u + "'</script> *^_^*  <br><br>正在进入系统请稍后，如果长时间没有反应，请<a href='" + u + "'>点这里进入。</a>");
				return;
		} 
		else if (actionType.equals("ExitAuth"))
		{
				Emp emp = new Emp(fk_emp);
				//首先退出，再进行登录
				WebUser.Exit();
				WebUser.SignInOfGenerLang(emp,WebUser.getSysLang());
				base.WinClose();
				return;
		}
		else if (actionType.equals("LogAs"))
		{
				BP.WF.Port.WFEmp wfemp = new WFEmp(fk_emp);
				if (wfemp.getAuthorIsOK() == false)
				{
					base.WinCloseWithMsg("授权失败");
					return;
				}
				Emp emp1 = new Emp(fk_emp);
				
				WebUser.SignInOfGener(emp1, WebUser.getSysLang(), WebUser.getNo(), true, false);
				base.WinClose();
				return;
		} 
		else if (actionType.equals("TakeBack")) // 取消授权。
		{
				WFEmp myau = new WFEmp(WebUser.getNo());
				Log.DefaultLogWriteLineInfo("取消授权:" + WebUser.getNo() + "取消了对(" + myau.getAuthor() + ")的授权。");
				myau.setAuthor("");
				myau.setAuthorWay(0);
				myau.Update();
				base.WinClose();
				return;
		}
		else if (actionType.equals("AutoTo")) // 执行授权。
		{
				WFEmp au = new WFEmp();
				au.setNo(WebUser.getNo());
				au.RetrieveFromDBSources();
				au.setAuthorDate(DataType.getCurrentData());
				au.setAuthor(fk_emp);
				au.setAuthorWay(1);
				au.Save();
				Log.DefaultLogWriteLineInfo("执行授权:" + WebUser.getNo() + "执行了对(" + au.getAuthor() + ")的授权。");
				base.WinClose();
				return;
		}
		else if (actionType.equals("UnSend")) // 执行撤消发送。
		{
				try
				{
				   	String str1= BP.WF.Dev2Interface.Flow_DoUnSend(fk_flow, workId);
				   	session.setAttribute("info", str1);
				   	response.sendRedirect("MyFlowInfo.jsp?FK_Flow=" + fk_flow + "&WorkID=" + workId);
					return;
				}
				catch (RuntimeException ex)
				{
					session.setAttribute("info", "@执行撤消失败。@失败信息" + ex.getMessage());
					//this.Alert(ex.getMessage());
					base.WinCloseWithMsg(ex.getMessage());
					response.sendRedirect("MyFlowInfo.jsp?fk_flow=" + fk_flow + "&WorkID=" + workId + "&FK_Type=warning");
					return;
				}
			// response.sendRedirect("MyFlow.aspx?WorkID=" + this.WorkID + "&fk_flow=" + this.fk_flow, true);
		}
		else if (actionType.equals("SetBillState"))
		{
		}
		else if (actionType.equals("WorkRpt"))
		{
				BP.WF.Data.Bill bk1 = new BP.WF.Data.Bill(request.getParameter("OID"));
				Node nd = new Node(bk1.getFK_Node());
				response.sendRedirect("WFRpt.jsp?WorkID=" + bk1.getWorkID() + "&FID=" + bk1.getFID() + "&fk_flow=" + nd.getFK_Flow() + "&NodeId=" + bk1.getFK_Node());
				//this.WinOpen();
				//base.WinClose();
		}
		else if (actionType.equals("PrintBill"))
		{
				//Bill bk2 = new Bill(this.Request.QueryString["OID"]);
				//Node nd2 = new Node(bk2.FK_Node);
				//response.sendRedirect("NodeRefFunc.aspx?NodeId=" + bk2.FK_Node + "&FlowNo=" + nd2.fk_flow + "&NodeRefFuncOID=" + bk2.FK_NodeRefFunc + "&WorkFlowID=" + bk2.WorkID);
				////base.WinClose();
			//删除流程中第一个节点的数据，包括待办工作
		}
		else if (actionType.equals("DeleteFlow"))
		{
				//调用DoDeleteWorkFlowByReal方法
				WorkFlow wf = new WorkFlow(new Flow(fk_flow), workId);
				wf.DoDeleteWorkFlowByReal(true);
				Glo.ToMsg("流程删除成功",response);
		}
		else if (actionType.equals("Focus"))
		{
			 BP.WF.Dev2Interface.Flow_Focus(workId);	
			 base.WinClose("ss");
			return;
		} else if (actionType.equals("DownFlowSearchExcel")) {
			if (StringHelper.isNullOrEmpty(fk_flow)) {
				throw new Exception("@参数FK_Flow不能为空");
			}
			String searchType = request.getParameter("SearchType");
			if (StringHelper.isNullOrEmpty(searchType)) {
				searchType = "My";
			}
			String rptmd = "ND" + fk_flow + "Rpt";
			String rptNo = rptmd + searchType;
			UserRegedit ur = new UserRegedit();
			ur.setMyPK(WebUser.getNo() + rptNo + "_SearchAttrs");
			ur.RetrieveFromDBSources();
			MapData md = new MapData(rptNo);
			MapAttrs attrs = new MapAttrs(rptNo);
			GEEntitys ges = new GEEntitys(rptNo);
			QueryObject qo = new QueryObject(ges);
			if ("My".equals(searchType)) {
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
			} else if ("MyDept".equals(searchType)) {
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
			} else if ("MyJoin".equals(searchType)) {
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%"
						+ WebUser.getNo() + "%");
			} else if ("Adminer".equals(searchType)) {

			} else {
				throw new Exception("err@" + searchType + "标记错误.");
			}
			WF_RptDfine wr = new WF_RptDfine();
			qo = wr.InitQueryObject(qo, md, ges.getGetNewEntity().getEnMap().getAttrs(), attrs, ur);
			DataTable dt = qo.DoQueryToTable();
			DataTable myDT = new DataTable();
			for (MapAttr attr : MapAttrs.convertMapAttrs(attrs)) {
				if ("MyNum".equals(attr.getKeyOfEn())) {
					continue;
				}
				Class<?> t = null;
				switch (attr.getLGType()) {
				case Normal:
					switch (attr.getMyDataType()) {
					case BP.DA.DataType.AppInt:
						t = Integer.class;
						break;
					case BP.DA.DataType.AppFloat:
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppMoney:
						t = Double.class;
						break;
					default:
						t = String.class;
					}
					break;
				default:
					t = String.class;
				}
				myDT.Columns.Add(new DataColumn(attr.getName(), t));
				// myDT.Columns[attr.Name].ExtendedProperties.Add("width", attr.UIWidthInt);
				// myDT.Columns.Add("width", attr.getUIWidthInt());
				myDT.Columns.get(attr.getName()).ExtendedProperties.Add("width", attr.getUIWidthInt());
				if (attr.getIsNum() && attr.getLGType() == BP.En.FieldTypeS.Normal && "OID,FID,PWorkID,FlowEndNode,PNodeID".indexOf(attr.getKeyOfEn()) == -1) {
					// myDT.Columns[attr.Name].ExtendedProperties.Add("sum", attr.IsSum);
					// myDT.Columns.Add("sum", attr.getIsSum());
					myDT.Columns.get(attr.getName()).ExtendedProperties.Add("sum", attr.getIsSum());
				}
			}
			for (BP.DA.DataRow dr : dt.Rows) {
				BP.DA.DataRow myDR = myDT.NewRow();
				for (MapAttr attr : MapAttrs.convertMapAttrs(attrs)) {
					if ("MyNum".equals(attr.getKeyOfEn())) {
						continue;
					}
					switch (attr.getLGType()) {
					case Normal:
						switch (attr.getMyDataType()) {
						case BP.DA.DataType.AppString:
						case BP.DA.DataType.AppDate:
						case BP.DA.DataType.AppDateTime:
						case BP.DA.DataType.AppInt:
						case BP.DA.DataType.AppFloat:
						case BP.DA.DataType.AppDouble:
						case BP.DA.DataType.AppMoney:
							myDR.setValue(attr.getName(), dr.getValue(attr.getField()));
							break;
						case BP.DA.DataType.AppBoolean:
							if ("0".equals(dr.getValue(attr.getField()))) {
								myDR.setValue(attr.getName(), "否");
							} else {
								myDR.setValue(attr.getName(), "是");
							}
							break;
						}
						break;
					case Enum:
						BP.Sys.SysEnum sem = new BP.Sys.SysEnum();
						sem.Retrieve(BP.Sys.SysEnumAttr.EnumKey, attr.getKeyOfEn(), BP.Sys.SysEnumAttr.IntKey, dr.getValue(attr.getField()));
						myDR.setValue(attr.getName(), sem.getLab());
						break;
					case FK:
						try {
							String tabName = attr.getUIBindKey();
							if ("FK_NY".equals(attr.getKeyOfEn())) {
								tabName = "Pub_NY";
							} else if ("FK_Dept".equals(attr.getKeyOfEn())) {
								tabName = "Port_Dept";
							}
							// !! SELECT * FROM BP.Port.Emps WHERE NO='admin'
							DataTable drDt = BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM " + tabName + " WHERE NO='" + dr.getValue(attr.getField()) + "'");
							if (drDt.Rows.size() > 0) {
								myDR.setValue(attr.getName(), drDt.Rows.get(0).getValue("NAME").toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case WinOpen:
						break;
					}
				}
				myDT.Rows.add(myDR);
			}
			BP.WF.Flow flow = new BP.WF.Flow(fk_flow);
			String name = null;
			if ("My".equals(searchType)) {
				name = "我发起的流程";
			} else if ("MyDept".equals(searchType)) {
				name = "部门发起的流程";
			} else if ("MyJoin".equals(searchType)) {
				name = "我审批的流程";
			} else if ("Adminer".equals(searchType)) {
				name = "高级查询";
			}
			name += "（" + flow.getName() + "）";
			java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy年MM月dd日");
			String filename = cn.jflow.common.util.ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/") + "/DataUser/UploadFile/" + name + "_" + df.format(System.currentTimeMillis()) + ".xls";
			cn.jflow.controller.wf.comm.Utilities.NpoiFuncs.DataTableToExcel(request, response, myDT, filename, name, BP.Web.WebUser.getName(), true, true, true);
		} else {
				throw new RuntimeException("actionType error" + actionType);
		}
	}
	catch (RuntimeException ex)
	{
		base.ToErrorPage("执行其间如下异常：<BR>" + ex.getMessage());
	}
	
%>