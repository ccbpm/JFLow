package cn.jflow.controller.wf.ccform;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DataColumn;
import BP.DA.DataTable;
import BP.En.QueryObject;
import BP.Sys.FrmEvents;
import BP.Sys.GEDtl;
import BP.Sys.GEDtls;
import BP.Sys.GEEntitys;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.UserRegedit;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.WorkFlow;
import BP.WF.Entity.GenerWorkerList;
import BP.WF.Entity.GenerWorkerListAttr;
import BP.WF.Template.CCList;
import BP.WF.Template.CCSta;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/CCForm")
@Scope("request")
public class DoController extends BaseController {

	@RequestMapping(value = "/Do", method = RequestMethod.POST)
	private void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.addHeader("P3P", "CP=CAO PSA OUR");
		response.addHeader("Cache-Control", "no-store");
		response.addHeader("Expires", "0");
		response.addHeader("Pragma", "no-cache");
//		System.out.println(request.getServletPath());
		String url = request.getRequestURI();
		if (!url.contains("DTT=")) {
			// this.Response.Redirect(url + "&DTT=" +
			// DateTime.Now.ToString("mmDDhhmmss"), true);
			// return;
		}

		try {
			String str = "";
			// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
			// string member and was converted to Java 'if-else' logic:
			// switch (this.ActionType)
			// ORIGINAL LINE: case "DoOpenCC":
			if (this.getActionType().equals("DoOpenCC")) {
				String fk_flow1 = this.getFK_Flow();
				String fk_node1 = String.valueOf(this.getFK_Node());
				String workid1 = String.valueOf(this.getWorkID());
				String fid1 = String.valueOf(this.getFID());
				String Sta = this.getSta();
				if (Sta.equals("0")) {
					CCList cc1 = new CCList();
					cc1.setMyPK(this.getMyPK());
					cc1.Retrieve();
					cc1.setHisSta(CCSta.Read);
					cc1.Update();
				}
				response.sendRedirect(Glo.getCCFlowAppPath()
						+ "WF/WorkOpt/OneWork/Track.jsp?FK_Flow=" + fk_flow1
						+ "&FK_Node=" + fk_node1 + "&WorkID=" + workid1
						+ "&FID=" + fid1);
				return;
			}
			// ORIGINAL LINE: case "DelCC":
			else if (this.getActionType().equals("DelCC")) // 删除抄送.
			{
				CCList cc = new CCList();
				cc.setMyPK(this.getMyPK());
				cc.Retrieve();
				cc.setHisSta(CCSta.Del);
				cc.Update();
				this.winClose(response);
			}
			// ORIGINAL LINE: case "DelSubFlow":
			else if (this.getActionType().equals("DelSubFlow")) // 删除进程。
			{
				try {
					WorkFlow wf14 = new WorkFlow(this.getFK_Flow(),
							this.getWorkID());
					wf14.DoDeleteWorkFlowByReal(true);
					this.winClose(response);
				} catch (RuntimeException ex) {
					this.winCloseWithMsg(response, ex.getMessage());
				}
			}
			// ORIGINAL LINE: case "DownBill":
			else if (this.getActionType().equals("DownBill")) {
				BP.WF.Data.Bill b = new BP.WF.Data.Bill(this.getMyPK());
				b.DoOpen();
			}
			// ORIGINAL LINE: case "DelDtl":
			else if (this.getActionType().equals("DelDtl")) {
				GEDtls dtls = new GEDtls(this.getEnsName());
				GEDtl dtl = (GEDtl) dtls.getGetNewEntity();
				dtl.setOID(this.getRefOID());
				if (dtl.RetrieveFromDBSources() == 0) {
					this.winClose(response);
					return;
				}

				FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
				// 处理删除前事件.
				try {
					if (fes.size() > 0) {
						String r = fes.DoEventNode(
								BP.WF.XML.EventListDtlList.DtlItemDelBefore,
								dtl);
						if (r == null || r.equals("false") || r.equals("0")) {
							this.winClose(response);
							return;
						}
					}
				} catch (RuntimeException ex) {
					ex.printStackTrace();
					this.winCloseWithMsg(response, ex.getMessage());

					return;
				}
				dtl.Delete();

				// 处理删除后事件.
				try {
					if (fes.size() > 0) {
						fes.DoEventNode(BP.WF.XML.EventListDtlList.DtlItemDelAfter,
								dtl);
					}
				} catch (RuntimeException ex) {
					this.winCloseWithMsg(response, ex.getMessage());
					return;
				}
				this.winClose(response);
			}
			// ORIGINAL LINE: case "EmpDoUp":
			else if (this.getActionType().equals("EmpDoUp")) {
				BP.WF.Port.WFEmp ep = new BP.WF.Port.WFEmp(this.getRefNo());
				ep.DoUp();

				BP.WF.Port.WFEmps emps111 = new BP.WF.Port.WFEmps();
				// emps111.RemoveCash();
				emps111.RetrieveAll();
				this.winClose(response);
			}
			// ORIGINAL LINE: case "EmpDoDown":
			else if (this.getActionType().equals("EmpDoDown")) {
				BP.WF.Port.WFEmp ep1 = new BP.WF.Port.WFEmp(this.getRefNo());
				ep1.DoDown();

				BP.WF.Port.WFEmps emps11441 = new BP.WF.Port.WFEmps();
				// emps11441.RemoveCash();
				emps11441.RetrieveAll();
				this.winClose(response);
			}
			// ORIGINAL LINE: case "OF":
			else if (this.getActionType().equals("OF")) {
				String sid = this.getSID();
				String[] strs = sid.split("[_]", -1);
				GenerWorkerList wl = new GenerWorkerList();
				int i = wl.Retrieve(GenerWorkerListAttr.FK_Emp, strs[0],
						GenerWorkerListAttr.WorkID, strs[1],
						GenerWorkerListAttr.FK_Node, strs[2]);
				if (i == 0) {
					this.wirteMsg(response, "<h2>提示</h2>此工作已经被别人处理或者此流程已删除。");
					return;
				}
				BP.Port.Emp emp155 = new BP.Port.Emp(wl.getFK_Emp());

				WebUser.SignInOfGener(emp155, true);
				String u = "MyFlow.htm?FK_Flow=" + wl.getFK_Flow() + "&WorkID="
						+ wl.getWorkID();
				if (this.getIsWap() != 0) {
					u = "./.../WAP/" + u;
				}
				this.wirteMsg(
						response,
						"<script> window.location.href='"
								+ u
								+ "'</script> *^_^*  <br><br>正在进入系统请稍后，如果长时间没有反应，请<a href='"
								+ u + "'>点这里进入。</a>");
				return;
			}
			// ORIGINAL LINE: case "ExitAuth":
			else if (this.getActionType().equals("ExitAuth")) {
				BP.Port.Emp emp = new BP.Port.Emp(this.getFK_Emp());
				WebUser.SignInOfGenerLang(emp, WebUser.getSysLang());
				this.winClose(response);
				return;
			}
			// ORIGINAL LINE: case "LogAs":
			else if (this.getActionType().equals("LogAs")) {
				BP.WF.Port.WFEmp wfemp = new BP.WF.Port.WFEmp(this.getFK_Emp());
				if (!wfemp.getAuthorIsOK()) {
					this.winCloseWithMsg(response, "授权失败");
					return;
				}
				BP.Port.Emp emp1 = new BP.Port.Emp(this.getFK_Emp());
				WebUser.SignInOfGener(emp1, WebUser.getSysLang(),
						WebUser.getNo(), true, false);
				this.winClose(response);
				return;
			}
			// ORIGINAL LINE: case "TakeBack":
			else if (this.getActionType().equals("TakeBack")) // 取消授权。
			{
				BP.WF.Port.WFEmp myau = new BP.WF.Port.WFEmp(WebUser.getNo());
				BP.DA.Log.DefaultLogWriteLineInfo("取消授权:" + WebUser.getNo()
						+ "取消了对(" + myau.getAuthor() + ")的授权。");
				myau.setAuthor("");
				myau.setAuthorWay(0);
				myau.Update();
				this.winClose(response);
				return;
			}
			// ORIGINAL LINE: case "AutoTo":
			else if (this.getActionType().equals("AutoTo")) // 执行授权。
			{
				BP.WF.Port.WFEmp au = new BP.WF.Port.WFEmp();
				au.setNo(WebUser.getNo());
				au.RetrieveFromDBSources();
				au.setAuthorDate(BP.DA.DataType.getCurrentData());
				au.setAuthor(this.getFK_Emp());
				au.setAuthorWay(1);
				au.Save();
				BP.DA.Log.DefaultLogWriteLineInfo("执行授权:" + WebUser.getNo()
						+ "执行了对(" + au.getAuthor() + ")的授权。");
				this.winClose(response);
				return;
			}
			// ORIGINAL LINE: case "UnSend":
			else if (this.getActionType().equals("UnSend")) // 执行撤消发送。
			{
				try {
					String str1 = BP.WF.Dev2Interface.Flow_DoUnSend(
							this.getFK_Flow(), this.getWorkID());
					request.getSession().putValue("info", str1);
					response.sendRedirect("MyFlowInfo" + this.getPageID()
							+ ".jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID="
							+ this.getWorkID());
					return;
				} catch (RuntimeException ex) {
					request.getSession().putValue("info",
							"@执行撤消失败。@失败信息" + ex.getMessage());
					this.printAlert(response, ex.getMessage());
					// this.WinCloseWithMsg(ex.Message);
					response.sendRedirect("MyFlowInfo" + this.getPageID()
							+ ".jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID="
							+ this.getWorkID() + "&FK_Type=warning");
					return;
				}
				// this.Response.Redirect("MyFlow.htm?WorkID=" + this.WorkID +
				// "&FK_Flow=" + this.FK_Flow, true);
			}
			// ORIGINAL LINE: case "SetBillState":
			else if (this.getActionType().equals("SetBillState")) {
			}
			// ORIGINAL LINE: case "WorkRpt":
			else if (this.getActionType().equals("WorkRpt")) {
				BP.WF.Data.Bill bk1 = new BP.WF.Data.Bill(this.getOID());
				Node nd = new Node(bk1.getFK_Node());
				response.sendRedirect("WFRpt.jsp?WorkID=" + bk1.getWorkID()
						+ "&FID=" + bk1.getFID() + "&FK_Flow="
						+ nd.getFK_Flow() + "&NodeId=" + bk1.getFK_Node());
				// this.WinOpen();
				// this.winClose(response);
			}
			// ORIGINAL LINE: case "PrintBill":
			else if (this.getActionType().equals("PrintBill")) {
				// Bill bk2 = new
				// Bill(ContextHolderUtils.getRequest().getParameter("OID"]);
				// Node nd2 = new Node(bk2.FK_Node);
				// this.Response.Redirect("NodeRefFunc.jsp?NodeId=" +
				// bk2.FK_Node + "&FlowNo=" + nd2.FK_Flow + "&NodeRefFuncOID=" +
				// bk2.FK_NodeRefFunc + "&WorkFlowID=" + bk2.WorkID);
				// //this.winClose(response);
				// 删除流程中第一个节点的数据，包括待办工作
			}
			// ORIGINAL LINE: case "DeleteFlow":
			else if (this.getActionType().equals("DeleteFlow")) {
				String fk_flow = this.getFK_Flow();
				long workid = this.getWorkID();
				// 调用DoDeleteWorkFlowByReal方法
				WorkFlow wf = new WorkFlow(new Flow(fk_flow), workid);
				wf.DoDeleteWorkFlowByReal(true);
				BP.WF.Glo.ToMsg("流程删除成功", response);
				// this.ToWFMsgPage("流程删除成功");
			}else if(this.getActionType().equals("DownFlowSearchExcel")){
				
			}else if (this.getActionType().equals("DownFlowSearchToTmpExcel")) {
				
			}else {
				throw new RuntimeException("ActionType error"
						+ this.getActionType());
			}
		} catch (RuntimeException ex) {
			// String rInfo = "执行其间如下异常：<BR>" + ex.getMessage();
			// ModelAndView mv = new ModelAndView("/WF/ShowMessage");
			// mv.addObject("showMessage", rInfo);
			// return mv;
			ex.printStackTrace();
		}		
	}
	
}
