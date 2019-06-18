package BP.WF.HttpHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HttpContext;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.QueryObject;
import BP.Sys.AthCtrlWay;
import BP.Sys.AthUploadWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImgAthDBs;
import BP.Sys.FrmSubFlowAttr;
import BP.Sys.FrmType;
import BP.Sys.FrmWorkCheckAttr;
import BP.Sys.GEDtl;
import BP.Sys.GEDtls;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.Dev2Interface;
import BP.WF.DoWhatList;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.GenerWorkFlow;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerListAttr;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.RunModel;
import BP.WF.SMS;
import BP.WF.SMSMsgType;
import BP.WF.Track;
import BP.WF.WFSta;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.WorkFlow;
import BP.WF.Works;
import BP.WF.Data.Bill;
import BP.WF.Data.GERpt;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Port.WFEmp;
import BP.WF.Port.WFEmpAttr;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CCList;
import BP.WF.Template.CCSta;
import BP.WF.Template.FTCAttr;
import BP.WF.Template.FlowSortAttr;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.FrmNodeComponent;
import BP.WF.Template.FrmThreadAttr;
import BP.WF.Template.FrmTrackAttr;
import BP.Web.WebUser;

public class WF extends WebContralBase {
	/**
	 * 初始化数据
	 * 
	 * @param mycontext
	 */
	public WF(HttpContext mycontext) {
		this.context = mycontext;
	}

	/**
	 * 初始化实体 无参构造器
	 */
	public WF() {
	}
	public String HasSealPic() throws Exception
    {
        String no = GetRequestVal("No");
        if (DataType.IsNullOrEmpty(no))
            return "";

        String path = SystemConfig.getPathOfDataUser() + "Siganture/"+ no + ".jpg";
        
        File file = new File(path);
        //如果文件存在
        if (file.exists() == true)
            return "";
        else
        {
            //如果不存在，就返回名称
            BP.Port.Emp emp = new BP.Port.Emp(no);
            return emp.getName();
        }
    }
	/**
	 * 入口函数
	 */
	public String DoDefaultMethod() {
		String msg = "";
		try {
			if ("LoginExit".equals(getDoType())) // 退出安全登录.
			{
				BP.WF.Dev2Interface.Port_SigOut();
			} else if ("AuthExit".equals(getDoType())) {
				msg = this.AuthExitAndLogin(this.getNo(), BP.Web.WebUser.getAuth());
			} else {
				msg = "err@没有判断的标记:" + this.getDoType();
			}
		} catch (Exception ex) {
			msg = "err@" + ex.getMessage();
		}
		return msg;
	}

	public boolean getIsReusable() {
		return false;
	}
	
	/// <summary>
    /// 执行的方法.
    /// </summary>
    /// <returns></returns>
    public String Do_Init()
    {
        String at = this.GetRequestVal("ActionType");
        if (DataType.IsNullOrEmpty(at))
            at = this.GetRequestVal("DoType");
        if (DataType.IsNullOrEmpty(at) && this.getSID() != null)
            at = "Track";
        try
        {
            switch (at)
            {

                case "Focus": //把任务放入任务池.
                    BP.WF.Dev2Interface.Flow_Focus(this.getWorkID());
                    return "info@Close";
                case "PutOne": //把任务放入任务池.
                    BP.WF.Dev2Interface.Node_TaskPoolPutOne(this.getWorkID());
                    return "info@Close";
                case "DoAppTask": // 申请任务.
                    BP.WF.Dev2Interface.Node_TaskPoolTakebackOne(this.getWorkID());
                    return "info@Close";
                case "DoOpenCC":

                    String Sta = this.GetRequestVal("Sta");
                    if (Sta == "0")
                    {
                        BP.WF.Template.CCList cc1 = new BP.WF.Template.CCList();
                        cc1.setMyPK(this.getMyPK());
                        cc1.Retrieve();
                        cc1.setHisSta(CCSta.Read);
                        cc1.Update();
                    }
                    return "url@./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();
                case "DelCC": //删除抄送.
                    CCList cc = new CCList();
                    cc.setMyPK(this.getMyPK());
                    cc.Retrieve();
                    cc.setHisSta(CCSta.Del);
                    cc.Update();
                    return "info@Close";
                case "DelSubFlow": //删除进程。
                    try
                    {
                        BP.WF.Dev2Interface.Flow_DeleteSubThread(this.getFK_Flow(), this.getWorkID(), "手工删除");
                        return "info@Close";
                    }
                    catch (Exception ex)
                    {
                        return "err@" + ex.getMessage();
                    }
                case "DownBill":
                    Bill b = new Bill(this.getMyPK());
                    b.DoOpen();
                    break;
                case "DelDtl":
                    GEDtls dtls = new GEDtls(this.getEnsName());
                    GEDtl dtl = (GEDtl)dtls.getGetNewEntity();
                    dtl.setOID(this.getRefOID());
                    if (dtl.RetrieveFromDBSources() == 0)
                    {
                        return "info@Close";
                    }
                    FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.

                    // 处理删除前事件.
                    try
                    {
                        fes.DoEventNode(BP.WF.XML.EventListDtlList.DtlItemDelBefore, dtl);
                    }
                    catch (Exception ex)
                    {
                        return "err@" + ex.getMessage();
                    }
                    dtl.Delete();

                    // 处理删除后事件.
                    try
                    {
                        fes.DoEventNode(BP.WF.XML.EventListDtlList.DtlItemDelAfter, dtl);
                    }
                    catch (Exception ex)
                    {
                        return "err@" + ex.getMessage();
                    }
                    return "info@Close";
                case "EmpDoUp":
                    BP.WF.Port.WFEmp ep = new BP.WF.Port.WFEmp(this.GetRequestVal("RefNo"));
                    ep.DoUp();

                    BP.WF.Port.WFEmps emps111 = new BP.WF.Port.WFEmps();
                    //  emps111.RemoveCash();
                    emps111.RetrieveAll();
                    return "info@Close";
                case "EmpDoDown":
                    BP.WF.Port.WFEmp ep1 = new BP.WF.Port.WFEmp(this.GetRequestVal("RefNo"));
                    ep1.DoDown();

                    BP.WF.Port.WFEmps emps11441 = new BP.WF.Port.WFEmps();
                    //  emps11441.RemoveCash();
                    emps11441.RetrieveAll();
                    return "info@Close";
                case "Track": //通过一个串来打开一个工作.
                    String mySid = this.getSID(); // this.Request.QueryString["SID"];
                    String[] mystrs = mySid.split("_");

                    int myWorkID = Integer.parseInt(mystrs[1]);
                    String fk_emp = mystrs[0];
                    int fk_node = Integer.parseInt(mystrs[2]);
                    Node mynd = new Node();
                    mynd.setNodeID(fk_node);
                    mynd.RetrieveFromDBSources();

                    String fk_flow = mynd.getFK_Flow();
                    String myurl = "./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Node=" + mynd.getNodeID() + "&WorkID=" + myWorkID + "&FK_Flow=" + fk_flow;
                    BP.Web.WebUser.SignInOfGener(new BP.Port.Emp(fk_emp));

                    return "url@" + myurl;
                case "OF": //通过一个串来打开一个工作.
                    String sid = this.getSID();
                    String[] strs = sid.split("_");
                    GenerWorkerList wl = new GenerWorkerList();
                    int i = wl.Retrieve(GenerWorkerListAttr.FK_Emp, strs[0],
                        GenerWorkerListAttr.WorkID, strs[1],
                        GenerWorkerListAttr.IsPass, 0);

                    if (i == 0)
                    {
                        return "info@此工作已经被别人处理或者此流程已删除";
                    }

                    BP.Port.Emp empOF = new BP.Port.Emp(wl.getFK_Emp());
                    BP.Web.WebUser.SignInOfGener(empOF);
                    String u = "MyFlow.htm?FK_Flow=" + wl.getFK_Flow() + "&WorkID=" + wl.getWorkID() + "&FK_Node=" + wl.getFK_Node() + "&FID=" + wl.getFID();
                    return "url@" + u;
                case "ExitAuth":
                    BP.Port.Emp emp = new BP.Port.Emp(this.getFK_Emp());
                    //首先退出，再进行登录
                    BP.Web.WebUser.Exit();
                    BP.Web.WebUser.SignInOfGener(emp, WebUser.getSysLang());
                    return "info@Close";
                case "LogAs":
                    BP.WF.Port.WFEmp wfemp = new BP.WF.Port.WFEmp(this.getFK_Emp());
                    if (wfemp.getAuthorIsOK() == false)
                    {
                        return "err@授权失败";
                    }
                    BP.Port.Emp emp1 = new BP.Port.Emp(this.getFK_Emp());
                    BP.Web.WebUser.SignInOfGener(emp1, "CH", false, false, wfemp.getAuthor(), WebUser.getName());
                    return "info@Close";
                case "TakeBack": // 取消授权。
                    BP.WF.Port.WFEmp myau = new BP.WF.Port.WFEmp(WebUser.getNo());
                    BP.DA.Log.DefaultLogWriteLineInfo("取消授权:" + WebUser.getNo() + "取消了对(" + myau.getAuthor() + ")的授权。");
                    myau.setAuthor("");
                    myau.setAuthorWay(0);
                    myau.Update();
                    return "info@Close";
                case "AutoTo": // 执行授权。
                    BP.WF.Port.WFEmp au = new BP.WF.Port.WFEmp();
                    au.setNo(WebUser.getNo());
                    au.RetrieveFromDBSources();
                    au.setAuthorDate(BP.DA.DataType.getCurrentDate());
                    au.setAuthor(this.getFK_Emp());
                    au.setAuthorWay(1);
                    au.Save();
                    BP.DA.Log.DefaultLogWriteLineInfo("执行授权:" + WebUser.getNo() + "执行了对(" + au.getAuthor() + ")的授权。");
                    return "info@Close";
                case "UnSend": //执行撤消发送。
                    String url = "./WorkOpt/UnSend.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();
                    return "url@" + url;
                case "SetBillState":
                    break;
                case "WorkRpt":
                    //  Bill bk1 = new Bill(this.OID);
                    //  Node nd = new Node(bk1.FK_Node);
                    // this.Response.Redirect("WFRpt.htm?WorkID=" + bk1.WorkID + "&FID=" + bk1.FID + "&FK_Flow=" + nd.FK_Flow + "&NodeId=" + bk1.FK_Node, false);
                    //this.WinOpen();
                    //this.WinClose();
                    break;
                case "PrintBill":
                    //Bill bk2 = new Bill(this.Request.QueryString["OID"]);
                    //Node nd2 = new Node(bk2.FK_Node);
                    //this.Response.Redirect("NodeRefFunc.aspx?NodeId=" + bk2.FK_Node + "&FlowNo=" + nd2.FK_Flow + "&NodeRefFuncOID=" + bk2.FK_NodeRefFunc + "&WorkFlowID=" + bk2.WorkID);
                    ////this.WinClose();
                    break;
                //删除流程中第一个节点的数据，包括待办工作
                case "DeleteFlow":
                    //调用DoDeleteWorkFlowByReal方法
                    WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID());
                    wf.DoDeleteWorkFlowByReal(true);
                    return BP.WF.Glo.getCCFlowAppPath() + "WF/MyFlowInfo.htm?Msg=流程删除成功";
                case "DownFlowSearchExcel":    //下载流程查询结果，转到下面的逻辑，不放在此try..catch..中
                    break;
                case "DownFlowSearchToTmpExcel":    //导出到模板
                    break;
                default:
                    throw new Exception("没有判断的at标记:" + at);
            }
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
        //此处之所以再加一个switch，是因为在下载文件逻辑中，调用Response.End()方法，如果此方法放在try..catch..中，会报线程中止异常
        switch (at)
        {
            case "DownFlowSearchExcel":
                //  DownMyStartFlowExcel();
                break;
            case "DownFlowSearchToTmpExcel":    //导出到模板
                // DownMyStartFlowToTmpExcel();
                break;
        }
        return "";
    }

	/**
	 * 运行 <param name="UserNo">人员编号</param> <param name="fk_flow">流程编号</param>
	 * <returns>运行中的流程</returns>
	 * 
	 * @throws Exception
	 */
	public String Runing_Init() throws Exception {
		AppACE page = new AppACE(context);
		return page.Runing_Init();
	}
	
	/**
	 * 已完成
	 * @return
	 * @throws Exception 
	 */
	public String Complete_Init() throws Exception 
    {
		/* 如果不是删除流程注册表. */
        Paras ps = new Paras();
        ps.SQL = "SELECT  * FROM WF_GenerWorkFlow  WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' and WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
        DataTable dt= BP.DA.DBAccess.RunSQLReturnTable(ps);
        //添加oracle的处理
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            dt.Columns.get("PRI").ColumnName = "PRI";
            dt.Columns.get("WORKID").ColumnName = "WorkID";
            dt.Columns.get("FID").ColumnName = "FID";
            dt.Columns.get("WFSTATE").ColumnName = "WFState";
            dt.Columns.get("WFSTA").ColumnName = "WFSta";
            dt.Columns.get("WEEKNUM").ColumnName = "WeekNum";
            dt.Columns.get("TSPAN").ColumnName = "TSpan";
            dt.Columns.get("TOTOSTA").ColumnName = "TodoSta";
            dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
            dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
            dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
            dt.Columns.get("TITLE").ColumnName = "Title";
            dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
            dt.Columns.get("SYSTYPE").ColumnName = "SysType";
            dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
            dt.Columns.get("STARTER").ColumnName = "Starter";
            dt.Columns.get("SENDER").ColumnName = "Sender";
            dt.Columns.get("SENDDT").ColumnName = "SendDT";
            dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
            dt.Columns.get("SDTOFFLOW").ColumnName = "SDTOfFlow";
            dt.Columns.get("RDT").ColumnName = "RDT";
            dt.Columns.get("PWORKID").ColumnName = "PWorkID";
            dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
            dt.Columns.get("PFID").ColumnName = "PFID";
            dt.Columns.get("PEMP").ColumnName = "PEmp";
            dt.Columns.get("NODENAME").ColumnName = "NodeName";
            dt.Columns.get("MYNUM").ColumnName = "MyNum";
            dt.Columns.get("GUID").ColumnName = "Guid";
            dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
            dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
            dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
            dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
            dt.Columns.get("FK_NY").ColumnName = "FK_NY";
            dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
            dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
            dt.Columns.get("FK_FKLOW").ColumnName = "FK_Flow";
            dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
            dt.Columns.get("EMPS").ColumnName = "Emps";
            dt.Columns.get("DOMAIN").ColumnName = "Domain";
            dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
            dt.Columns.get("BILLNO").ColumnName = "BillNo";
        }
        return BP.Tools.Json.ToJson(dt);
    }
	

	/// <summary>
	/// 打开表单
	/// </summary>
	/// <returns></returns>
	public String Runing_OpenFrm() throws Exception {
		int nodeID = this.getFK_Node();
		GenerWorkFlow gwf = null;
		if (nodeID == 0) {
			gwf = new GenerWorkFlow(this.getWorkID());
			nodeID = gwf.getFK_Node();
		}
		Node nd = null;
		Track tk = new Track();
		tk.setFK_Flow(this.getFK_Flow());

		tk.setWorkID(this.getWorkID());
		if (this.getMyPK() != null) {
			tk = new Track(this.getFK_Flow(), this.getMyPK());
			nd = new Node(tk.getNDFrom());
		} else {
			nd = new Node(nodeID);
		}

		nd.WorkID = this.getWorkID(); // 为求当前表单ID获得参数，而赋值.

		Flow fl = new Flow(this.getFK_Flow());

		long workid = 0;

		if (nd.getHisRunModel() == RunModel.SubThread) {
			workid = tk.getFID();
			if (workid == 0) {
				if (gwf == null)
					gwf = new GenerWorkFlow(this.getWorkID());

				workid = gwf.getFID();
			}
		} else
			workid = tk.getWorkID();

		long fid = this.getFID();
		if (this.getFID() == 0)
			fid = tk.getFID();

		if (fid > 0)
			workid = fid;

		String urlExt = "";
		DataTable ndrpt = DBAccess
				.RunSQLReturnTable("SELECT PFlowNo,PWorkID FROM " + fl.getPTable() + " WHERE OID=" + workid);
		if (ndrpt.Rows.size() == 0)
			urlExt = "&PFlowNo=0&PWorkID=0&IsToobar=0&IsHidden=true";
		else
			urlExt = "&PFlowNo=" + ndrpt.Rows.get(0).getValue("PFlowNo") + "&PWorkID="
					+ ndrpt.Rows.get(0).getValue("PWorkID") + "&IsToobar=0&IsHidden=true";
		urlExt += "&From=CCFlow&TruckKey=" + tk.GetValStrByKey("MyPK") + "&DoType=" + this.getDoType() + "&UserNo="
				+ WebUser.getNo() + "&SID=" + WebUser.getSID();

		if (nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType() == NodeFormType.SelfForm) {
			// added by liuxc,2016-01-25.
			if (nd.getFormUrl().contains("?"))
				return "urlForm@" + nd.getFormUrl() + "&IsReadonly=1&WorkID=" + workid + "&FK_Node=" + nd.getNodeID()
						+ "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt;

			return "urlForm@" + nd.getFormUrl() + "?IsReadonly=1&WorkID=" + workid + "&FK_Node=" + nd.getNodeID()
					+ "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt;
		}

		if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.SheetAutoTree)
			return "url@./MyFlowTreeReadonly.htm?3=4&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&OID="
					+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID()
					+ "&PK=OID&PKVal=" + this.getWorkID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1";

		// if (nd.getHisFormType() == NodeFormType.SheetTree ||
		// nd.getHisFormType() == NodeFormType.SheetAutoTree)
		// return "url@../../../MyFlowTreeReadonly.htm?3=4&FK_MapData=" +
		// nd.getNodeFrmID() + "&OID=" + wk.getOID() + "&FK_Flow=" +
		// this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" +
		// wk.getOID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1";

		Work wk = nd.getHisWork();
		wk.setOID(workid);
		if (wk.RetrieveFromDBSources() == 0) {
			GERpt rtp = nd.getHisFlow().getHisGERpt();
			rtp.setOID(workid);
			if (rtp.RetrieveFromDBSources() == 0) {
				String info = "打开(" + nd.getName() + ")错误";
				info += "当前的节点数据已经被删除！！！<br> 造成此问题出现的原因如下。";
				info += "1、当前节点数据被非法删除。";
				info += "2、节点数据是退回人与被退回人中间的节点，这部分节点数据查看不支持。";
				info += "技术信息:表" + wk.getEnMap().getPhysicsTable() + " WorkID=" + workid;
				return "err@" + info;
			}
			wk.setRow(rtp.getRow());
		}

		gwf = new GenerWorkFlow();
		gwf.setWorkID(wk.getOID());

		if (nd.getHisFlow().getIsMD5() && wk.IsPassCheckMD5() == false) {
			String err = "打开(" + nd.getName() + ")错误";
			err += "当前的节点数据已经被篡改，请报告管理员。";
			return "err@" + err;
		}

		if (nd.getHisFormType() == NodeFormType.FreeForm) {
			MapData md = new MapData(nd.getNodeFrmID());
			if (md.getHisFrmType() != FrmType.FreeFrm) {
				md.setHisFrmType(FrmType.FreeFrm);
				md.Update();
			}
		} else {
			MapData md = new MapData(nd.getNodeFrmID());
			if (md.getHisFrmType() != FrmType.FoolForm) {
				md.setHisFrmType(FrmType.FoolForm);
				md.Update();
			}
		}

		// 加入是累加表单的标志，目的是让附件可以看到.
		String endUrl = "";
		if (nd.getHisFormType() == NodeFormType.FoolTruck)
			endUrl = "&FormType=10&FromWorkOpt=" + this.GetRequestVal("FromWorkOpt");

		return "url@./CCForm/Frm.htm?FK_MapData=" + nd.getNodeFrmID() + "&OID=" + wk.getOID() + "&FK_Flow="
				+ this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + wk.getOID()
				+ "&IsEdit=0&IsLoadData=0&IsReadonly=1" + endUrl;

		// return "url@./MyFlowTreeReadonly.htm?3=3" + this.getRequestParas();
	}

	/**
	 * 挂起列表 <param name="userNo">用户编号</param> <param name="fk_flow">流程编号</param>
	 * <returns>挂起列表</returns>
	 * 
	 * @throws Exception
	 */
	public String HungUpList_Init() throws Exception {
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerHungUpList();
		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 草稿
	 * 
	 * @return
	 * @throws Exception
	 */
	public String Draft_Init() throws Exception {
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerDraftDataTable();

		// 转化大写.
		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	/**
	 * 获得会签列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQianList_Init() throws Exception {
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node AND B.IsPass=90 AND B.FK_Emp='"
				+ BP.Web.WebUser.getNo() + "'";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";

			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";

			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
		}
		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 获得授权人的待办.
	 * 
	 * @return 111111
	 */
	public String Todolist_Author() {
		DataTable dt = null;
		try {
			dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(this.getNo(), this.getFK_Node());
		} catch (Exception e) {
			Log.DebugWriteError("WF Todolist_Author():" + e.getMessage());
			e.printStackTrace();
		}

		// 转化大写的toJson.
		return BP.Tools.Json.DataTableToJson(dt, true);
	}

	/**
	 * 获得待办.
	 * 
	 * @return
	 */
	public String Todolist_Init() {
		DataTable dt = null;

		try {
			dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(BP.Web.WebUser.getNo(), this.getFK_Node());
		} catch (Exception e) {
			Log.DebugWriteError("WF Todolist_Init():" + e.getMessage());
			e.printStackTrace();
		}

		return BP.Tools.Json.DataTableToJson(dt, false, false, true);
	}

	/**
	 * 返回当前会话信息.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String LoginInit() throws Exception {
		Hashtable ht = new Hashtable();

		if (BP.Web.WebUser.getNo() == null)
			ht.put("UserNo", "");
		else
			ht.put("UserNo", BP.Web.WebUser.getNo());

		if (BP.Web.WebUser.getIsAuthorize())
			ht.put("Auth", BP.Web.WebUser.getAuth());
		else
			ht.put("Auth", "");
		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	/**
	 * 执行登录.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String LoginSubmit() throws Exception {
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo(this.GetValFromFrmByKey("TB_UserNo"));

		if (emp.RetrieveFromDBSources() == 0)
			return "err@用户名或密码错误.";
		String pass = this.GetValFromFrmByKey("TB_Pass");
		if (emp.getPass().equals(pass) == false)
			return "err@用户名或密码错误.";

		// 让其登录.
		String sid = BP.WF.Dev2Interface.Port_Login(emp.getNo());
		return sid;
	}

	/**
	 * 执行授权登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String LoginAs() throws Exception {
		BP.WF.Port.WFEmp wfemp = new BP.WF.Port.WFEmp(this.getNo());
		if (wfemp.getAuthorIsOK() == false)
			return "err@授权登录失败！";
		BP.Port.Emp emp1 = new BP.Port.Emp(this.getNo());
		try {
			BP.Web.WebUser.SignInOfGener(emp1, "CH", false, false, BP.Web.WebUser.getNo(), BP.Web.WebUser.getName());
		} catch (UnsupportedEncodingException e) {
			Log.DebugWriteError("WF LoginAs():" + e.getMessage());
			e.printStackTrace();
		}
		return "success@授权登录成功！";
	}

	/**
	 * 退出登录
	 * 
	 * @param UserNo
	 * @param Author
	 * @return
	 */
	public String AuthExitAndLogin(String UserNo, String Author) {
		String msg = "suess@退出成功！";
		try {
			BP.Port.Emp emp = new BP.Port.Emp(UserNo);
			// 首先退出
			BP.Web.WebUser.Exit();
			// 再进行登录
			BP.Port.Emp emp1 = new BP.Port.Emp(Author);
			BP.Web.WebUser.SignInOfGener(emp1, "CH", false, false, null, null);
		} catch (Exception ex) {
			msg = "err@退出时发生错误。" + ex.getMessage();
		}
		return msg;
	}

	/**
	 * 获取授权人列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String AuthorList_Init() throws Exception {
		
		/*
		DataTable dt = BP.DA.DBAccess
				.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + BP.Web.WebUser.getNo() + "'");
		return BP.Tools.FormatToJson.ToJson(dt); */
		
		  Paras ps = new Paras();
          ps.SQL = "SELECT No,Name FROM WF_Emp WHERE AUTHOR=" + SystemConfig.getAppCenterDBVarStr() + "AUTHOR";
          ps.Add("AUTHOR", BP.Web.WebUser.getNo());
          DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

          if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
          {
              dt.Columns.get("NO").setColumnName("No");
              dt.Columns.get("NAME").setColumnName("Name");
             // dt.Columns["NAME"].ColumnName = "Name";
          }
          return BP.Tools.Json.ToJson(dt); 
		
	}

	/**
	 * 当前登陆人是否有授权
	 * 
	 * @return
	 * @throws Exception
	 */
	public String IsHaveAuthor() throws Exception {
		DataTable dt = BP.DA.DBAccess
				.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + BP.Web.WebUser.getNo() + "'");
		WFEmp em = new WFEmp();
		em.Retrieve(WFEmpAttr.Author, BP.Web.WebUser.getNo());

		if (dt.Rows.size() > 0 && BP.Web.WebUser.getIsAuthorize() == false)
			return "suess@有授权";
		else
			return "err@没有授权";
	}

	/**
	 * 获得发起列表 yqh add
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Start_Init() throws Exception {
		//定义容器.
        DataSet ds = new DataSet();

        //流程类别.
        FlowSorts fss = new FlowSorts();
        fss.RetrieveAll();

        DataTable dtSort = fss.ToDataTableField("Sort");
        dtSort.TableName = "Sort";
        ds.Tables.add(dtSort);

        //获得能否发起的流程.
        DataTable dtStart = Dev2Interface.DB_StarFlows(WebUser.getNo());
        dtStart.TableName = "Start";
        ds.Tables.add(dtStart);

        //返回组合
        return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 获得发起列表 yqh add
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Start_Init2016() throws Exception {
		DataSet ds = new DataSet();

		// 流程类别.
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();

		DataTable dtSort = fss.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		// 获得能否发起的流程.
		// DataTable dtStart =
		// Dev2Interface.DB_GenerCanStartFlowsOfDataTable("zhoupeng");
		DataTable dtStart = Dev2Interface.DB_GenerCanStartFlowsOfDataTable(BP.Web.WebUser.getNo());
		dtStart.TableName = "Start";

		// String str=BP.Tools.Json.ToJson(dtStart);

		ds.Tables.add(dtStart);

		// 返回组合
		String st1 = BP.Tools.Json.ToJson(ds);

		return st1;
	}

	/**
	 * 初始化共享任务 yqh add
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String TaskPoolSharing_Init() throws Exception {
		DataTable dt = BP.WF.Dev2Interface.DB_TaskPool();

		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	/**
	 * 申请任务.
	 * 
	 * @return
	 */
	public String TaskPoolSharing_Apply() {
		boolean b = false;
		try {
			b = BP.WF.Dev2Interface.Node_TaskPoolTakebackOne(this.getWorkID());
		} catch (Exception e) {
			Log.DebugWriteError(e.getMessage());
			e.printStackTrace();
		}
		if (b == true)
			return "申请成功.";
		else
			return "err@申请失败...";
	}

	/**
	 * 我申请下来的任务 yqh add
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String TaskPoolApply_Init() throws Exception {
		DataTable dt = BP.WF.Dev2Interface.DB_TaskPoolOfMyApply();

		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	/**
	 * 获得发起列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FlowSearch_Init() throws Exception {
		DataSet ds = new DataSet();

		// 流程类别.
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();

		DataTable dtSort = fss.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		// 获得能否发起的流程.
		DataTable dtStart = DBAccess
				.RunSQLReturnTable("SELECT No,Name, FK_FlowSort FROM WF_Flow ORDER BY FK_FlowSort,Idx");
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);

		// 返回组合
		return BP.Tools.Json.ToJson(ds);
	}

	/// <summary>
	/// 方法
	/// </summary>
	/// <returns></returns>
	public String HandlerMapExt() {
		try {

			WF_CCForm wf = new WF_CCForm(context);
			return wf.HandlerMapExt();

		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 执行撤销
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Runing_UnSend() throws Exception {
		try {
			 //获取撤销到的节点
            int unSendToNode = this.GetRequestValInt("UnSendToNode");
			return BP.WF.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(),unSendToNode,this.getFID());
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 执行催办
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Runing_Press() throws Exception {
		try {
			return BP.WF.Dev2Interface.Flow_DoPress(this.getWorkID(), this.GetRequestVal("Msg"), false);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 获得抄送列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String CC_Init() throws Exception {
		String sta = this.GetRequestVal("Sta");
		if (sta == null || sta.equals("")) {
			sta = "-1";
		}

		int pageSize = 6; // int.Parse(pageSizeStr);

		String pageIdxStr = this.GetRequestVal("PageIdx");
		if (pageIdxStr == null) {
			pageIdxStr = "1";
		}
		int pageIdx = Integer.parseInt(pageIdxStr);

		// 实体查询.
		// BP.WF.SMSs ss = new BP.WF.SMSs();
		// BP.En.QueryObject qo = new BP.En.QueryObject(ss);

		DataTable dt = null;
		if (sta.equals("-1")) {
			dt = BP.WF.Dev2Interface.DB_CCList(BP.Web.WebUser.getNo());
		}
		if (sta.equals("0")) {
			dt = BP.WF.Dev2Interface.DB_CCList_UnRead(BP.Web.WebUser.getNo());
		}
		if (sta.equals("1")) {
			dt = BP.WF.Dev2Interface.DB_CCList_Read(BP.Web.WebUser.getNo());
		}
		if (sta.equals("2")) {
			dt = BP.WF.Dev2Interface.DB_CCList_Delete(BP.Web.WebUser.getNo());
		}

		// int allNum = qo.GetCount();
		// qo.DoQuery(BP.WF.SMSAttr.MyPK, pageSize, pageIdx);

		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 删除草稿.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Draft_Delete() throws Exception {
		return BP.WF.Dev2Interface.Flow_DoDeleteDraft(this.getFK_Flow(), this.getWorkID(), false);
	}

	/**
	 * 我的关注流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Focus_Init() throws Exception {
		String flowNo = this.GetRequestVal("FK_Flow");

		int idx = 0;
		// 获得关注的数据.
		DataTable dt = BP.WF.Dev2Interface.DB_Focus(flowNo, BP.Web.WebUser.getNo());
		SysEnums stas = new SysEnums("WFSta");
		String[] tempArr;
		for (DataRow dr : dt.Rows) {
			int wfsta = Integer.parseInt(dr.getValue("WFSta").toString());
			// edit by liuxc,2016-10-22,修复状态显示不正确问题
			SysEnum tempVar = (SysEnum) stas.GetEntityByKey(SysEnumAttr.IntKey, wfsta);
			String wfstaT = tempVar.getLab();
			String currEmp = "";

			if (wfsta != WFSta.Complete.getValue()) {
				// edit by liuxc,2016-10-24,未完成时，处理当前处理人，只显示处理人姓名
				for (String emp : dr.getValue("ToDoEmps").toString().split("[;]", -1)) {
					tempArr = emp.split("[,]", -1);

					currEmp += tempArr.length > 1 ? tempArr[1] : tempArr[0] + ",";
				}

				currEmp = DotNetToJavaStringHelper.trimEnd(currEmp, ',');

				// currEmp = dr["ToDoEmps"].ToString();
				// currEmp = currEmp.TrimEnd(';');
			}
			dr.setValue("ToDoEmps", currEmp);
			dr.setValue("FlowNote", wfstaT);
			dr.setValue("AtPara",
					(wfsta == BP.WF.WFSta.Complete.getValue() ? DotNetToJavaStringHelper
							.trimEnd(DotNetToJavaStringHelper.trimStart(dr.getValue("Sender").toString(), '('), ')')
							.split("[,]", -1)[1] : ""));
		}

		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	/// <summary>
	/// 取消关注
	/// </summary>
	/// <returns></returns>
	public final String Focus_Delete() throws Exception {
		BP.WF.Dev2Interface.Flow_Focus(this.getWorkID());
		return "执行成功";
	}

	/**
	 * 流程单表单查看
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FrmView_Init() throws Exception {
		Node nd = new Node(this.getFK_Node());
		nd.WorkID = this.getWorkID(); // 为求当前表单ID获得参数，而赋值.

		MapData md = new MapData();
		md.setNo(nd.getNodeFrmID());
		if (md.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("装载错误，该表单ID=" + md.getNo() + "丢失，请修复一次流程重新加载一次.");
		}

		// 获得表单模版.
		DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo(), null);

		///把主从表数据放入里面.
		// .工作数据放里面去, 放进去前执行一次装载前填充事件.
		BP.WF.Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		// 重设默认值.
		wk.ResetDefaultVal();

		DataTable mainTable = wk.ToDataTableField("MainTable");
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

		// 加入WF_Node.
		DataTable WF_Node = nd.ToDataTableField("WF_Node");
		myds.Tables.add(WF_Node);

		//加入组件的状态信息, 在解析表单的时候使用.
		BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
		if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID())) {
			// 说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.
			int refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));

			BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

			fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H));
			fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W));
			fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X));
			fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y));

			fnc.SetValByKey(FrmSubFlowAttr.SF_H, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_H));
			fnc.SetValByKey(FrmSubFlowAttr.SF_W, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_W));
			fnc.SetValByKey(FrmSubFlowAttr.SF_X, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_X));
			fnc.SetValByKey(FrmSubFlowAttr.SF_Y, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_Y));

			fnc.SetValByKey(FrmThreadAttr.FrmThread_H, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_H));
			fnc.SetValByKey(FrmThreadAttr.FrmThread_W, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_W));
			fnc.SetValByKey(FrmThreadAttr.FrmThread_X, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_X));
			fnc.SetValByKey(FrmThreadAttr.FrmThread_Y, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_Y));

			fnc.SetValByKey(FrmTrackAttr.FrmTrack_H, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_H));
			fnc.SetValByKey(FrmTrackAttr.FrmTrack_W, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_W));
			fnc.SetValByKey(FrmTrackAttr.FrmTrack_X, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_X));
			fnc.SetValByKey(FrmTrackAttr.FrmTrack_Y, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_Y));

			fnc.SetValByKey(FTCAttr.FTC_H, refFnc.GetValFloatByKey(FTCAttr.FTC_H));
			fnc.SetValByKey(FTCAttr.FTC_W, refFnc.GetValFloatByKey(FTCAttr.FTC_W));
			fnc.SetValByKey(FTCAttr.FTC_X, refFnc.GetValFloatByKey(FTCAttr.FTC_X));
			fnc.SetValByKey(FTCAttr.FTC_Y, refFnc.GetValFloatByKey(FTCAttr.FTC_Y));
		}

		myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));
		//加入组件的状态信息, 在解析表单的时候使用.

		//增加附件信息.
		BP.Sys.FrmAttachments athDescs = new FrmAttachments();
		athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, nd.getNodeFrmID());
		if (athDescs.size() != 0) {
			FrmAttachment athDesc = (FrmAttachment) ((athDescs.get(0) instanceof FrmAttachment) ? athDescs.get(0)
					: null);

			// 查询出来数据实体.
			BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
			if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID) {
				String pWorkID = DBAccess.RunSQLReturnValInt(
						"SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID(), 0) + "";
				if (pWorkID == null || pWorkID.equals("0")) {
					pWorkID = this.getWorkID() + "";
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Inherit) {
					// 继承模式
					BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
					qo.addOr();
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Interwork) {
					// 共享模式
					dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
				}
			} else if (athDesc.getHisCtrlWay() == AthCtrlWay.WorkID) {
				// 继承模式
				BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
				qo.addOrderBy("RDT");
				qo.DoQuery();
			}

			// 增加一个数据源.
			myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB"));
		}	

		//把外键表加入DataSet
		List<DataTable> dtMapAttrList = myds.getTables();
		DataTable dtMapAttr = null;
		for (DataTable dt : dtMapAttrList) {
			if (dt.getTableName().equals("Sys_MapAttr"))
				dtMapAttr = dt;
		}
		MapExts mes = md.getMapExts();
		MapExt me = new MapExt();
		
		DataTable ddlTable = new DataTable();
        ddlTable.Columns.Add("No");
        
		for (DataRow dr : dtMapAttr.Rows) {
			String lgType = dr.getValue("LGType").toString();
			String uiBindKey = dr.getValue("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
				continue; // 为空就continue.

			if (lgType.equals("1") == true)
				continue; // 枚举值就continue;

			String uiIsEnable = dr.getValue("UIIsEnable").toString();
			if (uiIsEnable.equals("0") == true && lgType.equals("1") == true)
				continue; // 如果是外键，并且是不可以编辑的状态.

			if (uiIsEnable.equals("0") == true && lgType.equals("0") == true)
				continue; // 如果是外部数据源，并且是不可以编辑的状态.

			
			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();

			//处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper,
					keyOfEn);
			me = (MapExt) ((tempVar instanceof MapExt) ? tempVar : null);
			if (me != null) {
				Object tempVar2 = me.getDoc();
				String fullSQL = (String) ((tempVar2 instanceof String) ? tempVar2 : null);
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
				// 重构新表
				DataTable dt_FK_Dll = new DataTable();
				dt_FK_Dll.TableName = keyOfEn; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				dt_FK_Dll.Columns.Add("No", String.class);
				dt_FK_Dll.Columns.Add("Name", String.class);
				for (DataRow dllRow : dt.Rows) {
					DataRow drDll = dt_FK_Dll.NewRow();
					drDll.setValue2017("No", dllRow.getValue("No"));
					drDll.setValue2017("Name", dllRow.getValue("Name"));
					dt_FK_Dll.Rows.AddRow(drDll);
				}
				myds.Tables.add(dt_FK_Dll);
				continue;
			}
			//处理下拉框数据范围.

			// 判断是否存在.
			if (myds.Tables.contains(uiBindKey) == true) {
				continue;
			}
			
			DataTable mydt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
			
			if(mydt!=null)
				myds.Tables.add(mydt);
			else{
				 DataRow ddldr = ddlTable.NewRow();
                 ddldr.setValue("No",uiBindKey);
                 ddlTable.Rows.add(ddldr);
			}

			
		}
		 ddlTable.TableName = "UIBindKey";
         myds.Tables.add(ddlTable);
		//把外键表加入DataSet

		// 图片附件
		nd.WorkID = this.getWorkID(); // 为求当前表单ID获得参数，而赋值.
		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(nd.getNodeFrmID(), this.getWorkID() + "");
		if (imgAthDBs != null && imgAthDBs.size() > 0) {
			DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
			myds.Tables.add(dt_ImgAth);
		}

		return BP.Tools.Json.ToJson(myds);
	}

	/**
	 * 初始化
	 * 
	 * @return
	 */
	public final String TodolistOfAuth_Init() {
		return "err@尚未重构完成.";


	}

	/// #region 处理page接口.
	/**
	 * 执行的内容
	 * 
	 */
	public final String getDoWhat() {
		return this.GetRequestVal("DoWhat");
	}

	/**
	 * 当前的用户
	 * 
	 */
	public final String getUserNo() {
		return this.GetRequestVal("UserNo");
	}

	/**
	 * 用户的安全校验码(请参考集成章节)
	 * 
	 */
	public final String getSID() {
		return this.GetRequestVal("SID");
	}

	public final String Port_Init() throws Exception {

		
		//登录校验.
		if (BP.Web.WebUser.getNo().equals(this.getUserNo()) == false) {

			//if (this.getUserNo() == null || this.getSID() == null || this.getDoWhat() == null) {
				if (this.getUserNo() == null  || this.getDoWhat() == null) {
				/// #region 安全性校验.
				return "err@必要的参数没有传入，请参考接口规则。";
			}

			if (BP.WF.Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false) {
				return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
			}

			if (BP.Web.WebUser.getNo().equals(this.getUserNo()) == false) {
				BP.WF.Dev2Interface.Port_SigOut();
				try {
					BP.WF.Dev2Interface.Port_Login(this.getUserNo(), this.getSID());
				} catch (RuntimeException ex) {
					return "err@安全校验出现错误:" + ex.getMessage();
				}
			}
		}
        if(this.getDoWhat().equals("PortLogin") == true){
            return "登陆成功";
        }
		/// #endregion 安全性校验.

		/// #region 生成参数串.
		String paras = "";
		Enumeration<String> paraNames = getRequest().getParameterNames();
		for (Enumeration<String> e = paraNames; e.hasMoreElements();) {
			String str = e.nextElement().toString();
			String val = this.GetRequestVal(str);
			if (val.indexOf('@') != -1) {
				return "err@您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。";
			}

			// switch (str)
			// ORIGINAL LINE: case DoWhatList.DoNode:
			if (str.equals(DoWhatList.DoNode) || str.equals(DoWhatList.Emps) || str.equals(DoWhatList.EmpWorks)
					|| str.equals(DoWhatList.FlowSearch) || str.equals(DoWhatList.Login)
					|| str.equals(DoWhatList.MyFlow) || str.equals(DoWhatList.MyWork) || str.equals(DoWhatList.Start)
					|| str.equals(DoWhatList.Start5) || str.equals(DoWhatList.StartSimple)
					|| str.equals(DoWhatList.FlowFX) || str.equals(DoWhatList.DealWork) || str.equals("FK_Flow")
					|| str.equals("WorkID") || str.equals("FK_Node") || str.equals("SID")) {
			} else {
				paras += "&" + str + "=" + val;
			}
		}
		String nodeID = this.getFK_Flow() + "01";

		/// #endregion 生成参数串.

		// 发起流程.
		if (this.getDoWhat().equals("StartClassic") == true) {
			if (this.getFK_Flow() == null) {
				return "url@./AppClassic/Home.htm";
			} else {
				return "url@./AppClassic/Home.htm?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID;
			}
		}

		// 打开工作轨迹。
		if (this.getDoWhat().equals(DoWhatList.OneWork) == true) {
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为 Null 。");
			}
			return "url@WFRpt.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras;
		}

		// 发起页面.
		if (this.getDoWhat().equals(DoWhatList.Start) == true) {
			if (this.getFK_Flow() == null) {
				return "url@Start.htm";
			} else {
				return "url@MyFlow.htm?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID;
			}
		}

		// 处理工作.
		if (this.getDoWhat().equals(DoWhatList.DealWork) == true) {
			if (DataType.IsNullOrEmpty(this.getFK_Flow()) || this.getWorkID() == 0) {
				return "err@参数 FK_Flow 或者 WorkID 为Null 。";
			}

			return "url@MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras;
		}

		// 请求在途.
		if (this.getDoWhat().equals(DoWhatList.Runing) == true) {
			return "url@Runing.htm?FK_Flow=" + this.getFK_Flow();
		}

		// 请求待办。
		if (this.getDoWhat().equals(DoWhatList.EmpWorks) == true || this.getDoWhat().equals("Todolist") == true) {
			if (DataType.IsNullOrEmpty(this.getFK_Flow())) {
				return "url@Todolist.htm";
			} else {
				return "url@Todolist.htm?FK_Flow=" + this.getFK_Flow();
			}
		}

		// 请求流程查询。
		if (this.getDoWhat().equals(DoWhatList.FlowSearch) == true) {
			if (DataType.IsNullOrEmpty(this.getFK_Flow())) {
				return "url@./RptSearch/Default.htm";
			} else {
				return "url@./RptDfine/FlowSearch.htm?2=1&FK_Flow=001&EnsName=ND" + Integer.parseInt(this.getFK_Flow())
						+ "Rpt" + paras;
			}
		}

		// 流程查询小页面.
		if (this.getDoWhat().equals(DoWhatList.FlowSearchSmall) == true) {
			if (this.getFK_Flow() == null) {
				return "url@./RptSearch/Default.htm";
			} else {
				return "url./Comm/Search.htm?EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras;
			}
		}

		// 打开消息.
		if (this.getDoWhat().equals(DoWhatList.DealMsg) == true) {
			String guid = this.GetRequestVal("GUID");
			BP.WF.SMS sms = new SMS();
			sms.setMyPK(guid);
			sms.Retrieve();

			// 判断当前的登录人员.
			if (BP.Web.WebUser.getNo() != sms.getSendToEmpNo()) {
				BP.WF.Dev2Interface.Port_Login(sms.getSendToEmpNo());
			}

			BP.DA.AtPara ap = new AtPara(sms.getAtPara());
			if (sms.getMsgType() == SMSMsgType.SendSuccess) { // 发送成功的提示.

				if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork( ap.GetValInt64ByKey("WorkID"), BP.Web.WebUser.getNo()) == true) {
					return "url@MyFlow.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID="
							+ ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
				} else {
					return "url@WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID="
							+ ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
				}
			} else { // 其他的情况都是查看工作报告.
				return "url@WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID="
						+ ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
			}
		}

		return "err@没有约定的标记:DoWhat=" + this.getDoWhat();
	}
	
	/**
	 * 批处理审批
	 * @return
	 * @throws Exception 
	 */
    public String Batch_Init() throws Exception
    {
        String fk_node = GetRequestVal("FK_Node");

        //没有传FK_Node
        if (DataType.IsNullOrEmpty(fk_node))
        {
            String sql = "SELECT a.NodeID, a.Name,a.FlowName, COUNT(WorkID) AS NUM  FROM WF_Node a, WF_EmpWorks b WHERE A.NodeID=b.FK_Node AND B.FK_Emp='" + WebUser.getNo() + "' AND b.WFState NOT IN (7) AND a.BatchRole!=0 GROUP BY A.NodeID, a.Name,a.FlowName ";
            DataTable dt = DBAccess.RunSQLReturnTable(sql);
            return BP.Tools.Json.ToJson(dt);
        }


        return "";
    }

    public String BatchList_Init() throws Exception
    {
        DataSet ds = new DataSet();

        String FK_Node = GetRequestVal("FK_Node");

        //获取节点信息
        BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
        Flow fl = nd.getHisFlow();
        ds.Tables.add(nd.ToDataTableField("WF_Node"));
        
        String sql = "";

        if (nd.getHisRunModel() == RunModel.SubThread)
        {
            sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable()
                      + " a , WF_EmpWorks b WHERE a.OID=B.FID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID()
                      + " AND b.FK_Emp='" + WebUser.getNo() + "'";
        }
        else
        {
            sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable()
                    + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID()
                    + " AND b.FK_Emp='" + WebUser.getNo() + "'";
        }

        //获取待审批的流程信息集合
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        dt.TableName = "Batch_List";
        ds.Tables.add(dt);

        //获取按钮权限
        BtnLab btnLab = new BtnLab(this.getFK_Node());

        ds.Tables.add(btnLab.ToDataTableField("Sys_BtnLab"));

        //获取报表数据
        String inSQL = "SELECT WorkID FROM WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "' AND WFState!=7 AND FK_Node=" + this.getFK_Node();
        Works wks = nd.getHisWorks();
        wks.RetrieveInSQL(inSQL);

        ds.Tables.add(wks.ToDataTableField("WF_Work"));

        //获取字段属性
        MapAttrs attrs = new MapAttrs("ND"+this.getFK_Node());

        //获取实际中需要展示的列
        String batchParas = nd.getBatchParas();
        MapAttrs realAttr = new MapAttrs();
        if (DataType.IsNullOrEmpty(batchParas) == false)
        {
            String[] strs = batchParas.split(",");
            for(String str : strs)
            {
                if (DataType.IsNullOrEmpty(str)
                    || str.contains("@PFlowNo") == true)
                    continue;

                for(MapAttr attr : attrs.ToJavaList())
                {
                    if (str != attr.getKeyOfEn())
                        continue;
                    realAttr.AddEntity(attr);
                }
            }
        }

        ds.Tables.add(realAttr.ToDataTableField("Sys_MapAttr"));

        return BP.Tools.Json.ToJson(ds);
    }

    /**
     *  批量发送
     * @return
     * @throws Exception 
     */
   
    public String Batch_Send() throws Exception
    {
        BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
        String[] strs = nd.getBatchParas().split(",");

        MapAttrs attrs = new MapAttrs("ND"+this.getFK_Node());

        //获取数据
        String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"+WebUser.getNo()+"' and FK_Node='"+this.getFK_Node()+"'";

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        int idx = -1;
        String msg = "";
        for(DataRow dr : dt.Rows)
        {
            idx++;
            if (idx == nd.getBatchListCount())
                break;
            long workid = Long.parseLong(dr.getValue("WorkID").toString());
            String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
            if (cb.equals("on"))
                cb = "1";
            else cb = "0";
            if (cb.equals("0")) //没有选中
                continue;
            //#region 给字段赋值
            Hashtable ht = new Hashtable();
            for(String str : strs)
            {
                if (DataType.IsNullOrEmpty(str))
                    continue;
                for(MapAttr attr : attrs.ToJavaList())
                {
                    if (str != attr.getKeyOfEn())
                        continue;
                   

                    if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
                    {

                        String val = this.GetValFromFrmByKey("TB_" + workid + "_" + attr.getKeyOfEn(), null);
                        ht.put(str, val);
                        continue;
                    }


                    if (attr.getUIContralType() == BP.En.UIContralType.TB && attr.getUIIsEnable() == true)
                    {
                        String val = this.GetValFromFrmByKey("TB_" + workid + "_" + attr.getKeyOfEn(), null);
                        ht.put(str, val);
                        continue;
                    }

                    if (attr.getUIContralType() == BP.En.UIContralType.DDL && attr.getUIIsEnable() == true)
                    {
                        String val = this.GetValFromFrmByKey("DDL_" + workid + "_" + attr.getKeyOfEn());
                        ht.put(str, val);
                        continue;
                    }

                    if (attr.getUIContralType() == BP.En.UIContralType.CheckBok && attr.getUIIsEnable() == true)
                    {
                        String val = this.GetValFromFrmByKey("CB_" + +workid + "_" + attr.getKeyOfEn(), "-1");
                        if (val == "-1")
                            ht.put(str, 0);
                        else
                            ht.put(str, 1);
                        continue;
                    }
                }
            }
            //给字段赋值
            //获取审核意见的值
            String checkNote = this.GetValFromFrmByKey("TB_" + workid + "_WorkCheck_Doc", null);
            if (DataType.IsNullOrEmpty(checkNote) == false)
                BP.WF.Dev2Interface.WriteTrackWorkCheck(nd.getFK_Flow(), nd.getNodeID(), workid, Long.parseLong(dr.getValue("FID").toString()), checkNote, WebUser.getName());
            
            msg += "@对工作(" + dr.getValue("Title") + ")处理情况如下";
            BP.WF.SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(nd.getFK_Flow(), workid, ht);
            msg += objs.ToMsgOfHtml();
            msg += "<br/>";
        }

        if (msg == "")
            msg = "没有选择需要处理的工作";
        return msg;
    }

    /// <summary>
    /// 批量退回 待定
    /// </summary>
    /// <returns></returns>
    public String Batch_Return() throws Exception
    {
        BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
        //获取数据
        String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"+WebUser.getNo()+"' and FK_Node='"+this.getFK_Node()+"'";

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        int idx = -1;
        String msg = "";
        for (DataRow dr : dt.Rows)
        {
            idx++;
            if (idx == nd.getBatchListCount())
                break;
            long workid = Long.parseLong(dr.getValue("WorkID").toString());
            String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
            if (cb.equals("on"))
                cb = "1";
            else cb = "0";
            if (cb.equals("0")) //没有选中
                continue;

            msg += "@对工作(" + dr.getValue("Title") + ")处理情况如下。<br>";
            BP.WF.SendReturnObjs objs = null;// BP.WF.Dev2Interface.Node_ReturnWork(nd.FK_Flow, workid,fid,this.FK_Node,"批量退回");
            msg += objs.ToMsgOfHtml();
            msg += "<hr>";

        }
        return "工作在完善中";
    }



    /// <summary>
    /// 批量删除
    /// </summary>
    /// <returns></returns>
    public String Batch_Delete() throws Exception
    {
    	BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());

        //获取数据
        String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"+WebUser.getNo()+"' and FK_Node='"+this.getFK_Node()+"'";

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        int idx = -1;
        String msg = "";
        for (DataRow dr : dt.Rows)
        {
            idx++;
            if (idx == nd.getBatchListCount())
                break;
            long workid = Long.parseLong(dr.getValue("WorkID").toString());
            String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
            if (cb.equals("on"))
                cb = "1";
            else cb = "0";
            if (cb.equals("0")) //没有选中
                continue;

            msg += "@对工作(" + dr.getValue("Title")+ ")处理情况如下。<br>";
            String mes = BP.WF.Dev2Interface.Flow_DoDeleteFlowByFlag(nd.getFK_Flow(), workid, "批量退回", true);
            msg += mes;
            msg += "<hr>";

        }
        if (msg == "")
            msg = "没有选择需要处理的工作";

        return "批量删除成功" + msg;
    }

    public String PCAndMobileUrl(){
        Hashtable ht = new Hashtable();
        ht.put("PCUrl", SystemConfig.getAppSettings().get("HostURL").toString());
        ht.put("MobileUrl", SystemConfig.getAppSettings().get("MobileURL").toString());
        return BP.Tools.Json.ToJson(ht);
    }

}
