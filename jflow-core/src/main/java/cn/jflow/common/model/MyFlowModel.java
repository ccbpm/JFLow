package cn.jflow.common.model;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import cn.jflow.controller.wf.MyFlowController;
import cn.jflow.model.designer.UCEnModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import cn.jflow.system.ui.core.ToolBar;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Sys.FrmEventList;
import BP.Sys.FrmType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.BatchRole;
import BP.WF.CCRole;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.FormRunType;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.PrintDocEnable;
import BP.WF.ReturnWork;
import BP.WF.ReturnWorkAttr;
import BP.WF.ReturnWorks;
import BP.WF.RunModel;
import BP.WF.ShiftWork;
import BP.WF.ShiftWorkAttr;
import BP.WF.ShiftWorks;
import BP.WF.StartWorkAttr;
import BP.WF.SubFlowCtrlRole;
import BP.WF.TrackAttr;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.WorkFlow;
import BP.WF.WorkNode;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.Btn;
import BP.WF.Template.BtnLab;
import BP.WF.Template.DraftRole;
import BP.WF.Template.Frm;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Template.Frms;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.ShowWhere;
import BP.WF.Template.WebOfficeWorkModel;
import BP.Web.UserWorkDev;
import BP.Web.WebUser;

public class MyFlowModel extends EnModel{
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private Work currWK;
	
	private Node currND;
	
	private GenerWorkFlow gwf;
	
	private long cWorkID;
	
	public String fk_flow;
	
	public String fk_node;
	
	public String fid;
	
	public long workId;
	
	private MapData mapData;
	
	private boolean isContinue = true;
	
	public String selfFromPub;
	
	private ToolBar toolbar = null;
	
	private Flow currFlow = null;
	
	private String tKey = DataType.getCurrentDateByFormart("yyMMddhhmmss");
	
	private String small = null;
	
	public EnModel UCEn1= null;
	
	public StringBuffer pub1=new StringBuffer();
	
	public StringBuffer FlowMsg=new StringBuffer();
	
	public StringBuffer pub2=new StringBuffer();
	
	public StringBuffer Pub3=new StringBuffer();
	
	protected ToolBar ToolBar1;
	
	protected ToolBar ToolBar2;
	
	public boolean getIsContinue(){
		return isContinue;
	}
	
	public boolean getHisFormType() {
		return currND.getHisFormType() == NodeFormType.SelfForm;
	}

	public MyFlowModel(HttpServletRequest request, HttpServletResponse response) {
		super(request,response);
		UCEn1 = new EnModel(request, response);
		//toolbar = new ToolBar(request, response);
		this.request = request;
		this.response = response;
	}
	
	public MyFlowModel(HttpServletRequest request, HttpServletResponse response,boolean isToolBar) {
		super(request,response);
		toolbar = new ToolBar(request, response);
		this.request = request;
		this.response = response;
	}
	
	public ToolBar getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolBar toolbar) {
		this.toolbar = toolbar;
	}

	public String DealUrl(Node currND)
	{
		String url = currND.getFormUrl();
		String urlExt = request.getQueryString();

		//防止查询不到.
		urlExt = urlExt.replace("?WorkID=", "&WorkID=");
		if (urlExt.contains("&WorkID") == false)
		{
			urlExt += "&WorkID=" + this.workId;
		}
		else
		{
			urlExt = urlExt.replace("&WorkID=0", "&WorkID=" + this.workId);
			urlExt = urlExt.replace("&WorkID=&", "&WorkID=" + this.workId + "&");
		}
		//SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出. 
//		url = url.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

		urlExt += "&CWorkID=" + cWorkID;

		if (urlExt.contains("&NodeID") == false)
		{
			urlExt += "&NodeID=" + currND.getNodeID();
		}

		if (urlExt.contains("FK_Node") == false)
		{
			urlExt += "&FK_Node=" + currND.getNodeID();
		}

		if (urlExt.contains("&FID") == false)
		{
			urlExt += "&FID=" + currWK.getFID();
		}

		if (urlExt.contains("&UserNo") == false)
		{
			urlExt += "&UserNo=" + WebUser.getNo();
		}

		if (urlExt.contains("&SID") == false)
		{
			urlExt += "&SID=" + WebUser.getSID();
		}

		if (url.contains("?") == true)
		{
			url += "&" + urlExt;
		}
		else
		{
			url += "?" + urlExt;
		}
			url = url.replace("?&", "?");
		return url;
	}

	public String FrmID="";
	/**
	 * 初始化流程逻辑
	 */
	public void initFlow() {//初始化变量.
		
//		String doType = request.getParameter("DoType");
		String workIdStr = request.getParameter("WorkID");
		String isRead = request.getParameter("IsRead");
		fid = request.getParameter("FID");
		fk_flow = request.getParameter("FK_Flow");
		fk_node = request.getParameter("FK_Node");
		String fk_emp=request.getParameter("fk_emp");
		
		
		this.currFlow = new Flow(fk_flow);
		
		boolean isCC = request.getQueryString().contains("IsCC=1");
		
		if(StringHelper.isNullOrEmpty(isRead)){
			isRead="";
		}
		if(StringHelper.isNullOrEmpty(fid)){
			fid = "0";
		}
		
		if(StringHelper.isNullOrEmpty(fk_node) || fk_node.equals("0")){
			fk_node= fk_flow+"01";
		}
		
		fk_node = String.valueOf(Integer.parseInt(fk_node));
		
		currND= new Node(fk_node);		
		mapData = new MapData(currND.getNodeFrmID());
		mapData.Retrieve();
		this.FrmID = currND.getNodeFrmID();
		
		// end接受变量.
		// 检查是否可以发起该流程？
		if (Glo.CheckIsCanStartFlow_InitStartFlow(currFlow) == false) {
			this.toMsg("@您违反了该流程的【" + currFlow.getStartLimitRole() + "】限制规则。" + currFlow.getStartLimitAlert(), "INfo");
			return;
		}

		// region 校验用户是否被禁用。
		try {
			String no = WebUser.getNo();
			if (StringHelper.isNullOrEmpty(no))
			{
				String userNo = request.getParameter("UserNo");
				try {
					Dev2Interface.Port_Login(userNo, request.getParameter("SID"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (RuntimeException ex)
		{
			this.toMsg("@登录信息WebUser.No丢失，请重新登录。" + ex.getMessage(), "Info");
			return;
		}
		try
		{
			String name = WebUser.getName();
		}
		catch (RuntimeException ex)
		{
			this.toMsg("@登录信息WebUser.Name丢失，请重新登录。错误信息:" + ex.getMessage(), "Info");
			isContinue = false;
			return;
		}
		if (BP.WF.Glo.getIsEnableCheckUseSta() == true)
		{
			try {
				if (BP.WF.Glo.CheckIsEnableWFEmp() == false)
				{
					WebUser.Exit();
					this.toMsg("<font color=red>您的帐号已经被禁用，如果有问题请与管理员联系。</font>", "Info");
					//BP.Web.WebUser.Exit();
					isContinue = false;
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//endreigion
			// 工作id 为空重新创建
			//if(StringHelper.isNullOrEmpty(workIdStr) || workIdStr.equals("0")){
			//	workIdStr = String.valueOf(Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null));
				//Dev2Interface.Node_SendWork(fk_flow, workid);
			//}
			
		workId = workIdStr == null ? 0 : Long.parseLong(workIdStr);
		
		//region 判断是否有IsRead
	     try {
	         if (isCC){
	             if (isRead.equals("0"))
	                 BP.WF.Dev2Interface.Node_CC_SetRead(Integer.valueOf(fk_node), workId, WebUser.getNo());
	         }else{
	             if (isRead.equals("0"))
	                 Dev2Interface.Node_SetWorkRead(Integer.valueOf(fk_node), workId);
	         }
	     }
	     catch (Exception ex)
	     {
	         this.toMsg("设置读取状态错误。"+ex.getMessage(), "info");
	         isContinue = false;
	         return;
	     }
		
	  	// 判断前置导航.
	   
		if (currND.getIsStartNode() && isCC == false) {
			if (Dev2Interface.Flow_IsCanStartThisFlow(fk_flow,WebUser.getNo()) == false) {
				// 是否可以发起流程？
				this.toMsg("您(" + BP.Web.WebUser.getNo() + ")没有发起或者处理该流程的权限.",
						"Err");
				return;
			}
		}

		String IsCheckGuide = request.getParameter("IsCheckGuide");
		if (workId == 0 && currND.getIsStartNode() && IsCheckGuide  == null)
		{
			switch (currFlow.getStartGuideWay())
			{
				case None:
					break;
				case SubFlowGuide:
				case SubFlowGuideEntity:
					try {
						response.sendRedirect("StartGuide.jsp?FK_Flow=" + fk_flow);
					} catch (IOException e) {
						e.printStackTrace();
					}
					isContinue = false;
					return;
				case ByHistoryUrl: // 历史数据.
					if (currFlow.getIsLoadPriData() )
					{
						this.toMsg("流程配置错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。", "Info");
						isContinue = false;
						return;
					}
					try {
						response.sendRedirect("StartGuide.jsp?FK_Flow=" + fk_flow);
					} catch (IOException e) {
						e.printStackTrace();
					}
					isContinue = false;
					return;
				case BySystemUrlOneEntity:
				case BySQLOne:
					try {
						response.sendRedirect("StartGuideEntities.jsp?FK_Flow=" + fk_flow);
					} catch (IOException e) {
						e.printStackTrace();
					}
					isContinue = false;
					return;
					
				case BySelfUrl:
					try {
						response.sendRedirect(currFlow.getStartGuidePara1());
					} catch (IOException e) {
						e.printStackTrace();
					}
					isContinue = false;
					return;
				case ByFrms:
					try {
						response.sendRedirect("../WF/WorkOpt/StartGuideFrms.jsp?FK_Flow=" + currFlow.getNo());
					} catch (IOException e) {
						e.printStackTrace();
					}
					isContinue = false;
					return;
				default:
					break;
			}
		}
		// end结束前置导航处理.
		
		// region 处理表单类型.
		if (currND.getHisFormType() == NodeFormType.SheetTree || currND.getHisFormType() == NodeFormType.SheetAutoTree)
		{
			//如果是多表单流程.
			String pFlowNo = request.getParameter("PFlowNo");
			String pWorkID = request.getParameter("PWorkID");
			String pNodeID = request.getParameter("PNodeID");
			String pEmp = request.getParameter("PEmp");
			if (StringHelper.isNullOrEmpty(pEmp))
			{
				pEmp = WebUser.getNo();
			}

			if (workId == 0)
			{
				if (StringHelper.isNullOrEmpty(pFlowNo))
				{
					try {
						workId = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else
				{
					workId = Dev2Interface.Node_CreateBlankWork(fk_flow);
				}

				currWK = currND.getHisWork();
				currWK.setOID(workId);
				currWK.Retrieve();
				workId = currWK.getOID();
			}else{
				gwf = new GenerWorkFlow();
				gwf.setWorkID(workId);
				gwf.RetrieveFromDBSources();
				cWorkID = gwf.getCWorkID();
				pFlowNo = gwf.getPFlowNo();
				pWorkID = String.valueOf(gwf.getPWorkID());
			}

			String toUrl = "";
			if (currND.getHisFormType() == NodeFormType.SheetTree ||currND.getHisFormType() == NodeFormType.SheetAutoTree)
			{
				toUrl = "./FlowFormTree/Default.jsp?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&UserNo=" + WebUser.getNo() + "&FID=" + fid + "&SID=" + WebUser.getSID() + "&CWorkID=" + cWorkID + "&PFlowNo=" + pFlowNo + "&PWorkID=" + pWorkID;
			}
			else
			{
				toUrl = "./WebOffice/Default.jsp?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&UserNo=" + WebUser.getNo() + "&FID=" + fid + "&SID=" + WebUser.getSID() + "&CWorkID=" + cWorkID + "&PFlowNo=" + pFlowNo + "&PWorkID=" + pWorkID;
			}

			String[] ps = request.getQueryString().split("&");
			int ps_size = ps.length;
			for(int p=0; p<ps_size; p++)
			{
				String s = ps[p];
				if (StringHelper.isNullOrEmpty(s))
				{
					continue;
				}
				if (toUrl.contains(s))
				{
					continue;
				}
				toUrl += "&" + s;
			}
			if (gwf == null) {
				gwf = new GenerWorkFlow();
				gwf.setWorkID(workId);
				gwf.RetrieveFromDBSources();
			}
			//设置url.
			if (gwf.getWFState() == WFState.Runing || gwf.getWFState() == WFState.Blank || gwf.getWFState() == WFState.Draft) {
				if (toUrl.contains("IsLoadData") == false) {
					toUrl += "&IsLoadData=1";
				}
				else {
					toUrl = toUrl.replace("&IsLoadData=0", "&IsLoadData=1");
				}
			}
			//SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出. 
			toUrl = toUrl.replace("@SDKFromServHost", (CharSequence) SystemConfig.getAppSettings().get("SDKFromServHost"));


			try {
				response.sendRedirect(toUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			isContinue = false;
			return;
		}
		
		/*if (currND.getHisFormType() == NodeFormType.SLForm)
		{
			if (workId == 0)
			{
				try {
					workId = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				currWK = currND.getHisWork();
				currWK.setOID(workId);
				currWK.Retrieve();
				workId = currWK.getOID();
			}
			String myflowSlUrl = "MyFlowSL.jsp?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&UserNo=" + WebUser.getNo() + "&CWorkID=" + cWorkID;
			try {
				response.sendRedirect(myflowSlUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			isContinue = false;
			return;
		}*/
		
		if (currND.getHisFormType() == NodeFormType.SDKForm)
		{

			if (workId == 0)
			{
				currWK = currFlow.NewWork();
				workId = currWK.getOID();
			}

			String url = currND.getFormUrl();
			if (StringHelper.isNullOrEmpty(url))
			{
				this.toMsg("设置读取状流程设计错误态错误。", "没有设置表单url。");
				isContinue = false;
				return;
			}
			//处理连接.
			url = this.DealUrl(currND);

			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			isContinue = false;
			return;
		}
		
		// region 判断是否有 workid
		boolean isAskFor = false;
		if (workId == 0) {
			currWK = currFlow.NewWork();
			workId = currWK.getOID();
			gwf = new GenerWorkFlow();

		}else{
			 currWK = currFlow.GenerWork(workId, currND,true);
			 gwf = new GenerWorkFlow();
			 gwf.setWorkID(workId);
			 gwf.RetrieveFromDBSources();
		}
		///#region 处理分合流的退回信息.
		if (currND.getHisRunModel() == RunModel.FL || currND.getHisRunModel() == RunModel.FHL) {
			if (gwf.getWFState() == WFState.ReturnSta) {
				//如果是退回的状态，就说明该信息是子线程退回到合流节点.
				try {
					response.sendRedirect(Glo.getCCFlowAppPath()+"WF/WorkOpt/DealSubThreadReturnToHL.jsp?FK_Flow="+fk_flow+"&FK_Node="+fk_node+"&WorkID="+workId+"&FID="+fid);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}///#endregion 处理分合流的退回信息

		/*currWK = this.currFlow.GenerWork(workId, currND, isPostBack);
		if (Glo.getIsEnableTaskPool() && gwf.getTaskSta() == TaskSta.Takeback) {
			//如果是任务池状态，并且被人取走，要检查取走的人是不是自己。
		}
*/
		String msg = "";
		switch (gwf.getWFState()) {
			case AskForReplay: // 返回加签的信息.
				if (isCC == false) {
					String mysql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workId + " AND " + TrackAttr.ActionType + "=" + ActionType.ForwardAskfor;
					DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(mysql);
					for (DataRow dr : mydt.Rows) {
						String msgAskFor = String.valueOf(dr.get(TrackAttr.Msg));
						String worker = String.valueOf(dr.get(TrackAttr.EmpFrom));
						String workerName =String.valueOf(dr.get(TrackAttr.EmpFromT));
						String rdt =String.valueOf(dr.get(TrackAttr.RDT));

						//提示信息.
						BaseModel.AddMsgOfInfo(worker + "," + workerName + "回复信息:", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt);
					}
				}
				break;
			case Askfor: //加签.
				if (isCC == false) {
					String sql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workId + " AND " + TrackAttr.ActionType + "=" + ActionType.AskforHelp.getValue();
					DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					for (DataRow dr : dt.Rows) {
						String msgAskFor = String.valueOf(dr.get(TrackAttr.Msg.toLowerCase()));
						String worker = String.valueOf(dr.get(TrackAttr.EmpFrom.toLowerCase()));
						String workerName = String.valueOf(dr.get(TrackAttr.EmpFromT.toLowerCase()));
						String rdt = String.valueOf(dr.get(TrackAttr.RDT.toLowerCase()));

						//提示信息.
						pub2.append(BaseModel.AddMsgOfInfo(worker + "," + workerName + "请求加签:", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + " --<a href='./WorkOpt/AskForRe.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workId + "&FID=" +fid + "' >回复加签意见</a> --"));
					}
					isAskFor = true;
				}
				break;
			case ReturnSta:
				// 如果工作节点退回了
				ReturnWorks rws = new ReturnWorks();
				rws.Retrieve(ReturnWorkAttr.ReturnToNode, fk_node, ReturnWorkAttr.WorkID, workId, ReturnWorkAttr.RDT);
				if (rws.size() != 0) {
					String msgInfo = "";
					for(int i=0;i<rws.size();i++){
						ReturnWork rw=(ReturnWork) rws.get(i);
						msgInfo += "<fieldset width='100%' ><legend>&nbsp; 来自节点:" + rw.getReturnNodeName() + " 退回人:" + rw.getReturnerName() + "  " + rw.getRDT() + "&nbsp;<a href='../DataUser/ReturnLog/" + fk_flow + "/" + rw.getMyPK() + ".htm' target=_blank>工作日志</a></legend>";
						msgInfo += rw.getBeiZhuHtml();
						msgInfo += "</fieldset>";
					}
					BaseModel.AddMsgOfInfo("流程退回提示", msgInfo);
				}
				break;
			case Shift:
				// 判断移交过来的。 
				ShiftWorks fws = new ShiftWorks();
				BP.En.QueryObject qo = new QueryObject(fws);
				qo.AddWhere(ShiftWorkAttr.WorkID, workId);
				qo.addAnd();
				qo.AddWhere(ShiftWorkAttr.FK_Node, fk_node);
				qo.addOrderBy(ShiftWorkAttr.RDT);
				qo.DoQuery();
				if (fws.size() >= 1) {
					BaseModel.AddFieldSet("移交历史信息");
					//for (ShiftWork fw : fws) {
					for (int j=0;j<fws.size();j++) {
						ShiftWork fw =(ShiftWork) fws.get(j);
						msg = "@移交人[" + fw.getFK_Emp() + "," + fw.getFK_EmpName() + "]。@接受人：" + fw.getToEmp() + "," + fw.getToEmpName() + "。<br>移交原因：-------------" + fw.getNoteHtml();
						if (fw.getFK_Emp() == WebUser.getNo()) {
							msg = "<b>" + msg + "</b>";
						}

						msg = msg.replace("@", "<br>@");
						BaseModel.Add(msg + "<hr>");
					}
					BaseModel.AddFieldSetEnd();
				}
				break;
			default:
				break;
		}

		if (currND.getHisFormType().getValue() == NodeFormType.SelfForm.getValue()){
			currWK.Save();
            if (workId == 0)
            	workId = currWK.getOID();

            String url = this.DealUrl(currND);

            selfFromPub = "<iframe ID='SelfForm' src='" +Glo.getCCFlowAppPath()+ url + "' frameborder=0  style='width:100%; height:800px' leftMargin='0' topMargin='0' />";
            selfFromPub += "\t\n</iframe>";
            String appPath = BP.WF.Glo.getCCFlowAppPath();
    		try {
    			// 初始化控件.
    			this.InitToolbar(isAskFor, appPath, gwf);
    			
    			this.BindWork(currND, currWK);
    			request.setAttribute("Work", currWK);
    		} catch (RuntimeException ex) {
    			System.out.println("异常："+ex.getMessage());
    			ex.printStackTrace();
    		}
            return;
		}
		// endregion 处理表单类型.
		
		
		if (!isCC && currND.getIsStartNode() == false && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node), workId, WebUser.getNo()) == false)
		{
			this.toMsg(" @当前的工作已经被处理，或者您没有执行此工作的权限。", "Info");
			isContinue = false;
			return;
		}
		if (WebUser.getUserWorkDev() == UserWorkDev.Mobile) {
			try {
				response.sendRedirect("/CCMobile/MyFlow.htm?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&FID=" +fid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		String appPath = BP.WF.Glo.getCCFlowAppPath();
		try {
			// 初始化控件.
			/*this.InitToolbar(isAskFor, appPath, gwf);*/
			
			this.BindWork(currND, currWK);
			request.setAttribute("Work", currWK);
		} catch (RuntimeException ex) {
			System.out.println("异常："+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	
	
	public void initToolbarToJsp(){
		String workIdStr = request.getParameter("WorkID");
		fid = request.getParameter("FID");
		fk_flow = request.getParameter("FK_Flow");
		fk_node = request.getParameter("FK_Node");
		this.currFlow = new Flow(fk_flow);
		if(StringHelper.isNullOrEmpty(fk_node) || fk_node.equals("0")){
				fk_node= fk_flow+"01";
			}
			
			fk_node = String.valueOf(Integer.parseInt(fk_node));
			
			currND= new Node(fk_node);	
			
		workId = workIdStr == null ? 0 : Long.parseLong(workIdStr);

			
		boolean isAskFor = false;
			if (workId == 0) {
				currWK = currFlow.NewWork();
				workId = currWK.getOID();
				gwf = new GenerWorkFlow();

			}else{
				// currWK = currFlow.GenerWork(workId, currND,true);
				 gwf = new GenerWorkFlow();
				 gwf.setWorkID(workId);
				 gwf.RetrieveFromDBSources();
			}
			
			if(gwf.getWFState().getValue()==WFState.Askfor.getValue()){
				isAskFor = true;
			}
			this.InitToolbar(isAskFor, Glo.getCCFlowAppPath(), gwf);
	}
	
	
	
	/**
	 * 返回表单高度
	 * @return
	 */
	public String getFromH(){
		return String.valueOf(mapData.getFrmH());
	}
	
	/**
	 * 返回表单宽
	 * @return
	 */
	public String getFromW(){
		return String.valueOf(mapData.getFrmW());
	}
	
	public void toMsg(String msg, String type){
		try{
			request.getSession().setAttribute("info", msg);
			//application.setAttribute("info" + WebUser.getNo(), msg);
			Glo.setSessionMsg(msg);
			String url = "MyFlowInfo.jsp?FK_Flow=" + fk_flow + "&FK_Type=" + type + "&FK_Node=" + fk_node + "&WorkID=" + workId;
			response.sendRedirect(url);
		}catch(Exception ex){
			try{
				request.getSession().setAttribute("info", ex.getMessage());
				response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Comm/Port/ToErrorPage.jsp");
			}catch(IOException io){}
		}
	}
	
	public final void InitToolbar(boolean isAskFor, String appPath, GenerWorkFlow gwf)
	{
		this.toolbar.Add("&nbsp;&nbsp;");

		if (this.getIsNotCC())
		{
			toolbar.Add("<input type=button  value='流程运行轨迹' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/Track.jsp?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />");
			// 判断审核组件在当前的表单中是否启用，如果启用了.
			FrmWorkCheck fwc = new FrmWorkCheck(this.getFK_Node());
			if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable)
			{
				/*如果不等于启用, */
				toolbar.Add("<input type=button  value='填写审核意见' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/CCCheckNote.jsp?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />");
			}
			return;
		}

		//#region 加载流程控制器 - 按钮
		BtnLab btnLab = new BtnLab(getcurrND().getNodeID());

		if (this.getcurrND().getHisFormType() == NodeFormType.SelfForm)
		{
			
			/*处理保存按钮.*/
			if (btnLab.getSaveEnable() && isAskFor == false)
			{
				toolbar.Add("<input type=button id=Btn_"+NamesOfBtn.Save+  " value='" + btnLab.getSaveLab() + "' enable=true onclick=\"Save();\" />");
			}
			/*如果是嵌入式表单.*/
			if (getcurrND().getIsEndNode() && isAskFor == false)
			{
				/*如果当前节点是结束节点.*/
				if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
				{
					/*如果启用了发送按钮.*/
					toolbar.Add("<input type=button id=\"Btn_"+NamesOfBtn.Send +"\"  value='" + btnLab.getSendLab() + "' enable=true onclick=\"if (SendSelfFrom()==false) return false;this.disabled=true;Send();"+btnLab.getSendJS()+" \" />");
					this.getBtn_Send().setUseSubmitBehavior(false);
					this.getBtn_Send().addAttr("onclick", btnLab.getSendJS() + "if (SendSelfFrom()==false) return false;this.disabled=true;");
				}
			}
			else
			{
				if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group && isAskFor == false)
				{
					/*如果启用了发送按钮.*/
					if (btnLab.getSelectAccepterEnable() == 2)
					{
						/*如果启用了选择人窗口的模式是【选择既发送】.*/
						toolbar.Add("<input type=button  value='" + btnLab.getSendLab() + "' enable=true onclick=\"if( SendSelfFrom()==false) return false;this.disabled=true; javascript:OpenSelectAccepter('" + fk_flow + "','" + fk_node + "','" + workId + "','" + fid + "');" + btnLab.getSendJS()+" \" />");
						toolbar.AddBtn(NamesOfBtn.Send.toString(), btnLab.getSendLab());
						getBtn_Send().setStyle(" style = \"display:none; \"");
						this.getBtn_Send().setUseSubmitBehavior(false);

						if (this.getcurrND().getHisFormType() == NodeFormType.DisableIt)
						{
							this.getBtn_Send().addAttr("onclick", btnLab.getSendJS() + "this.disabled=true;");
						}
						else
						{
							this.getBtn_Send().addAttr("onclick",btnLab.getSendJS() + "if( SendSelfFrom()==false) return false;this.disabled=true;");
						}
					}
					else
					{
						toolbar.Add("<input type=button id=\"Btn_"+NamesOfBtn.Send +"\"  value='" + btnLab.getSendLab() + "' enable=true onclick=\"if(SendSelfFrom()==false) return false;this.disabled=true; Send();"+btnLab.getSendJS()+" \" />");
						this.getBtn_Send().setUseSubmitBehavior(false);
						if (btnLab.getSendJS().trim().length() > 2)
						{
							this.getBtn_Send().addAttr("onclick", btnLab.getSendJS() + ";if(SendSelfFrom()==false) return false;this.disabled=true;");
						}
						else
						{
							this.getBtn_Send().setUseSubmitBehavior(false);
							if (this.getcurrND().getHisFormType() == NodeFormType.DisableIt)
							{
								this.getBtn_Send().addAttr("onclick", "this.disabled=true;");
							}
							else
							{
								this.getBtn_Send().addAttr("onclick", "if(SendSelfFrom()==false) return false;this.disabled=true;");
							}
						}
					}
				}
			}

			
		}
		else
		{
			
			/*处理保存按钮.*/
			if (btnLab.getSaveEnable() && isAskFor == false)
			{
				toolbar.Add("<input type=button id=Btn_"+NamesOfBtn.Save+  " value='" + btnLab.getSaveLab() + "' enable=true onclick=\"Save();\" />");
				this.getBtn_Save().setUseSubmitBehavior(false);
				this.getBtn_Save().addAttr("onclick", "if(SysCheckFrm()==false) return false;this.disabled=true;SaveDtlAll();KindEditerSync();");
			}
			/*启用了其他的表单.*/
			if (getcurrND().getIsEndNode() && isAskFor == false)
			{
				/*如果当前节点是结束节点.*/
				if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
				{
					/*如果启用了选择人窗口的模式是【选择既发送】.*/
					toolbar.Add("<input type=button id=\"Btn_"+NamesOfBtn.Send +"\"  value='" + btnLab.getSendLab() + "' enable=true onclick=\"if(SysCheckFrm()==false) return false;this.disabled=true;SaveDtlAll();KindEditerSync(); Send();"+btnLab.getSendJS()+" \" />");
					this.getBtn_Send().setUseSubmitBehavior(false);
					this.getBtn_Send().addAttr("onclick", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;this.disabled=true;SaveDtlAll();KindEditerSync();");
				}
			}
			else
			{
				
				if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group && isAskFor == false)
				{
					/*如果启用了发送按钮.
					 * 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
					 */
					if (btnLab.getSelectAccepterEnable() == 2)
					{
						/*如果启用了选择人窗口的模式是【选择既发送】.*/
						toolbar.Add("<input type=button  value='" + btnLab.getSendLab() + "' enable=true onclick=\"if(SysCheckFrm()==false) return false;KindEditerSync();if (OpenSelectAccepter('" + fk_flow + "','" + fk_node + "','" + workId + "','" + fid + "')==false) return false; \" />");
						getBtn_Send().setStyle(" style = \"display:none; \"");
						this.getBtn_Send().setUseSubmitBehavior(false);

						if (this.getcurrND().getHisFormType() == NodeFormType.DisableIt)
						{
							this.getBtn_Send().addAttr("onclick", btnLab.getSendJS() + "this.disabled=true;");
						}
						else
						{
							this.getBtn_Send().addAttr("onclick", btnLab.getSendJS() + "if(SysCheckFrm()==false) return false;this.disabled=true;SaveDtlAll();KindEditerSync();");
						}
					}
					else
					{
						toolbar.Add("<input type=button id=\"Btn_"+NamesOfBtn.Send +"\"  value='" + btnLab.getSendLab() + "' enable=true onclick=\"if(SysCheckFrm()==false) return false;KindEditerSync();Send();"+btnLab.getSendJS()+" \" />");
						this.getBtn_Send().setUseSubmitBehavior(false);
						if (btnLab.getSendJS().trim().length() > 2)
						{
							this.getBtn_Send().addAttr("onclick",btnLab.getSendJS() + ";if(SysCheckFrm()==false) return false;this.disabled=true;SaveDtlAll();KindEditerSync();");
						}
						else
						{
							this.getBtn_Send().setUseSubmitBehavior(false);
							if (this.getcurrND().getHisFormType() == NodeFormType.DisableIt)
							{
								this.getBtn_Send().addAttr("onclick", "this.disabled=true;");
							}
							else
							{
								this.getBtn_Send().addAttr("onclick", "if(SysCheckFrm()==false) return false;this.disabled=true;SaveDtlAll();KindEditerSync();");
							}
						}
					}
				}
			}

			
		}

		if (btnLab.getWorkCheckEnable() && isAskFor == false)
		{
			/*审核*/
			String urlr1 = appPath + "WF/WorkOpt/WorkCheck.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getWorkCheckLab() + "' enable=true onclick=\"WinOpen('" + urlr1 + "','dsdd'); \" />");
		}

		if (btnLab.getThreadEnable())
		{
			/*如果要查看子线程.*/
			String ur2 = appPath + "WF/WorkOpt/ThreadDtl.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getThreadLab() + "' enable=true onclick=\"WinOpen('" + ur2 + "'); \" />");
		}

		if (btnLab.getTCEnable() == true && isAskFor == false)
		{
			/*流转自定义..*/
			String ur3 = appPath + "WF/WorkOpt/TransferCustom.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getTCLab() + "' enable=true onclick=\"To('" + ur3 + "'); \" />");
		}

		if (btnLab.getJumpWayEnable() && isAskFor == false)
		{
			/*如果没有焦点字段*/
			String urlr = appPath + "WF/JumpWay.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getJumpWayLab() + "' enable=true onclick=\"To('" + urlr + "'); \" />");
		}

		if (btnLab.getReturnEnable ()&& isAskFor == false && this.getcurrND().getIsStartNode() == false)
		{
			/*如果没有焦点字段*/
			String urlr = appPath + "WF/WorkOpt/ReturnWork.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getReturnLab() + "' enable=true onclick=\"ReturnWork('" + urlr + "','" + btnLab.getReturnField() + "'); \" />");
		}

		if (btnLab.getHungEnable())
		{
			/*挂起*/
			String urlr = appPath + "WF/WorkOpt/HungUp.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getHungLab() + "' enable=true onclick=\"WinOpen('" + urlr + "'); \" />");
		}

		if (btnLab.getShiftEnable() && isAskFor == false)
		{
			/*移交*/
			toolbar.Add("<input type=button id=\"Btn_Shift\"  value='" + btnLab.getShiftLab() + "' enable=true onclick=\"ShowUrl(this);\" />");
		}

		if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto))
		{
			/* 抄送 */
			toolbar.Add("<input type=button  value='" + btnLab.getCCLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/CC.jsp?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&s=" + tKey + "','ds'); \" />");
		}

		if (btnLab.getDeleteEnable() != 0 && isAskFor == false)
		{
			/*流程删除规则 */
			switch (this.getcurrND().getHisDelWorkFlowRole())
			{
				case None: //不删除
					break;
				case ByUser: //需要交互.
				case DeleteAndWriteToLog:
				case DeleteByFlag:
					String urlrDel = appPath + "WF/MyFlowInfo.jsp?DoType=DeleteFlow&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					toolbar.Add("<input type=button id=\"Btn_Delete\" value='" + btnLab.getDeleteLab() + "' enable=true onclick=\"ShowUrl(this); \" />");
					break;
				case DeleteReal: // 不需要交互，直接干净的删除.
					toolbar.Add("<input type=button id=\"Btn_Delete\" value='" + btnLab.getDeleteLab() + "' enable=true onclick=\"ShowUrl(this); \" />");
					break;
				default:
					break;
			}
		}

		if (btnLab.getEndFlowEnable() && this.getcurrND().getIsStartNode() == false && isAskFor == false)
		{
			toolbar.Add("<input type=button  value='" + btnLab.getEndFlowLab() + "' enable=true onclick=\"To('./WorkOpt/StopFlow.jsp?&DoType=StopFlow&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey + "'); \" />");
		}

		if (btnLab.getPrintDocEnable() && isAskFor == false)
		{
			/*如果不是加签 */
			if (this.getcurrND().getHisPrintDocEnable() == PrintDocEnable.PrintRTF)
			{
				String urlr = appPath + "WF/WorkOpt/PrintDoc.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar.Add("<input type=button  value='" + btnLab.getPrintDocLab() + "' enable=true onclick=\"WinOpen('" + urlr + "','dsdd'); \" />");
			}

			if (this.getcurrND().getHisPrintDocEnable() == PrintDocEnable.PrintWord)
			{
				String urlr = appPath + "WF/Rpt/RptDoc.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&IsPrint=1&s=" + tKey;
				toolbar.Add("<input type=button  value='" + btnLab.getPrintDocLab() + "' enable=true onclick=\"WinOpen('" + urlr + "','dsdd'); \" />");
			}

			if (this.getcurrND().getHisPrintDocEnable() == PrintDocEnable.PrintHtml)
			{
				toolbar.Add("<input type=button  value='" + btnLab.getPrintDocLab() + "' enable=true onclick=\"printFrom(); \" />");
			}
		}

		if (btnLab.getTrackEnable() && isAskFor == false)
		{
			toolbar.Add("<input type=button  value='" + btnLab.getTrackLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/Track.jsp?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />");
		}

		switch (btnLab.getSelectAccepterEnable())
		{
			case 1:
				if (isAskFor == false)
				{
					toolbar.Add("<input type=button  value='" + btnLab.getSelectAccepterLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/Accepter.jsp?WorkID=" + this.getWorkID() + "&FK_Node=" + getcurrND().getNodeID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&s=" + tKey + "','dds'); \" />");
				}
				break;
			case 2:
				//  toolbar.Add("<input type=button  value='" + btnLab.SelectAccepterLab + "' enable=true onclick=\"WinOpen('" + appPath + "WF/Accepter.jsp?WorkID=" + this.WorkID + "&FK_Node=" + currND.NodeID + "&FK_Flow=" + this.FK_Flow + "&FID=" + this.FID + "&s=" + tKey + "','dds'); \" />");
				break;
			default:
				break;
		}

		if (btnLab.getSearchEnable() && isAskFor == false)
		{
			toolbar.Add("<input type=button  value='" + btnLab.getSearchLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/Rpt/Search.jsp?RptNo=ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt&&EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey + "','dsd0'); \" />");
		}

		if (btnLab.getBatchEnable() && isAskFor == false)
		{
			/*批量处理*/
			String urlr = appPath + "WF/Batch.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getBatchLab() + "' enable=true onclick=\"To('" + urlr + "'); \" />");
		}

		if (btnLab.getAskforEnable() && gwf != null && gwf.getWFState() != WFState.Askfor)
		{
			/*加签 */
			String urlr3 = appPath + "WF/WorkOpt/Askfor.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getAskforLab() + "' enable=true onclick=\"To('" + urlr3 + "'); \" />");
		}

		if (btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.Button)
		{
			/*公文正文 */
			String urlr = appPath + "WF/WorkOpt/WebOffice.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + (new Date());
			toolbar.Add("<input type=button id=\"Btn_Office\" value='" + btnLab.getWebOfficeLab() + "' enable=true onclick=\"ShowUrl(this); \" />");
		}

		if (this.currFlow.getIsResetData() == true && this.getcurrND().getIsStartNode())
		{
			/* 启用了数据重置功能 */
			String urlr3 = appPath + "WF/MyFlow.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&IsDeleteDraft=1&s=" + tKey;
			toolbar.Add("<input type=button  value='数据重置' enable=true onclick=\"To('" + urlr3 + "','ds'); \" />");
		}

		if (btnLab.getSubFlowCtrlRole() != SubFlowCtrlRole.None)
		{
			/* 子流程 */
			String urlr3 = appPath + "WF/WorkOpt/SubFlow.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getSubFlowLab() + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />");
		}

		if (btnLab.getCHEnable() == true)
		{
			/* 节点时限设置 */
			String urlr3 = appPath + "WF/WorkOpt/CH.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getCHLab() + "' enable=true onclick=\"WinShowModalDialog('" + urlr3 + "'); \" />");
		}

		if (btnLab.getPRIEnable() == true)
		{
			/* 优先级设置 */
			String urlr3 = appPath + "WF/WorkOpt/PRI.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
			toolbar.Add("<input type=button  value='" + btnLab.getPRILab() + "' enable=true onclick=\"WinShowModalDialog('" + urlr3 + "'); \" />");
		}

		/* 关注 */
		if (btnLab.getFocusEnable() == true)
		{
			if (gwf.getParas_Focus() == true)
			{
				toolbar.Add("<input type=button  value='取消关注' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" />");
			}
			else
			{
				toolbar.Add("<input type=button  value='" + btnLab.getFocusLab() + "' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" />");
			}
		}

		//加载自定义的button.
		BP.WF.Template.NodeToolbars bars = new NodeToolbars();
		bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node());
		for (NodeToolbar bar : bars.ToJavaList())
		{
			if (bar.getShowWhere() == ShowWhere.Toolbar)
			{
				if (!StringHelper.isNullOrEmpty(bar.getTarget()) && bar.getTarget().toLowerCase().equals("javascript"))
				{
					toolbar.Add("<input type=button  value='" + bar.getTitle() + "' enable=true onclick=\"" + bar.getUrl() + "\" />");
				}
				else
				{
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					toolbar.Add("<input type=button  value='" + bar.getTitle() + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />");
				}
			}
		}


	}

	public final BP.WF.Node getcurrND()
	{
		if (currND == null)
		{
			currND = new BP.WF.Node(request.getParameter("FK_Node"));
		}
		return currND;
	}
	public final void setcurrND(BP.WF.Node value)
	{
		currND = value;
	}
	/** 
	 是否抄送
	*/
	public final boolean getIsNotCC()
	{

		if (StringHelper.isNullOrEmpty(request.getParameter("Paras")) == false)
		{
			String myps = request.getParameter("Paras");
			if (myps.contains("IsCC=1") == true)
			{
				return true;
			}
		}
		if (StringHelper.isNullOrEmpty(request.getParameter("AtPara")) == false)
		{
			String myps = request.getParameter("AtPara");

			if (myps.contains("IsCC=1") == true)
			{
				return true;
			}
		}
		return false;
	}
	/** 
	 当前的工作ID
	*/
	public final long getWorkID()
	{
			if (request.getParameter("WorkID") == null)
			{
				return workId;
			}
			else
			{
				return Long.parseLong(request.getParameter("WorkID"));
			}
	}
	public final void setWorkID(long value)
	{
		workId = value;
	}
	/** 
	 当前的流程编号
	*/
	public final String getFK_Flow()
	{
		String s = request.getParameter("FK_Flow");
		if (StringHelper.isNullOrEmpty(s))
		{
			throw new RuntimeException("@流程编号参数错误...");
		}

		return BP.WF.Dev2Interface.TurnFlowMarkToFlowNo(s);
	}
	/** 
	 FID
	*/
	public final long getFID()
	{
		try
		{
			return Integer.parseInt( request.getParameter("FID"));
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	/** 
	 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	*/
	public final int getFK_Node()
	{
		String fk_nodeReq = request.getParameter("FK_Node");
		if (StringHelper.isNullOrEmpty(fk_nodeReq))
		{
			fk_nodeReq = request.getParameter("NodeID");
		}

		if (StringHelper.isNullOrEmpty(fk_nodeReq) == false)
		{
			return Integer.parseInt(fk_nodeReq);
		}

		if ("0".equals(fk_node))
		{
			if (request.getParameter("WorkID") != null)
			{
				String sql = "SELECT FK_Node from  WF_GenerWorkFlow where WorkID=" + this.getWorkID();
				fk_node = String.valueOf(DBAccess.RunSQLReturnValInt(sql));
			}
			else
			{
				fk_node = Integer.parseInt(this.getFK_Flow()) + "01";
			}
		}
		return Integer.parseInt(fk_node);
	}
	/** 
	 发送
	*/
	protected final Btn getBtn_Send()
	{
		return (Btn) BaseModel.GetBtnByID(NamesOfBtn.Send.toString());
	}
	protected final Button getBtn_Delete()
	{
		return BaseModel.GetBtnByID(NamesOfBtn.Delete.toString());
	}
	private void ToolBar1_ButtonClick(Object sender)
	{
		String id = "";
		Btn btn = (Btn)((sender instanceof Btn) ? sender : null);
		if (btn != null)
		{
			id = btn.getId();
		}
		String btnId = btn.getId();
		
		if ("Btn_Reject".equals(btnId) || "Btn_KillSubFlow".equals(btnId))
		{
			try
			{
				WorkFlow wkf = new WorkFlow(this.getFK_Flow(), this.getWorkID());
				if ("Btn_KillSubFlow".equals(btnId))
				{
					this.toMsg("删除流程信息:<hr>" + wkf.DoDeleteWorkFlowByReal(true), "info");
				}
				else
				{
					String msg = wkf.DoReject(this.getFID(),this.getFK_Node(), "");
					this.toMsg(msg, "info");
				}
				return;
			}
			catch (RuntimeException ex)
			{
				this.toMsg(ex.getMessage(), "info");
				return;
			}
		} else if (NamesOfBtn.Delete.equals(btnId) || "Btn_Del".equals(btnId))
		{
			// 这是彻底删除的不需要交互。
			String delMsg = BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
			this.toMsg("删除流程提示<hr>" + delMsg, "info");
		} else if (NamesOfBtn.Save.equals(btnId) || "Btn_Del".equals(btnId))
		{
			try {
				MyFlowController.Send(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (StringHelper.isNullOrEmpty(request.getParameter("WorkID")))
			{
				// this.Response.Redirect(this.PageID + ".jsp?FID=" + this.FID + "&WorkID=" + this.WorkID + "&FK_Node=" + this.FK_Node + "&FK_Flow=" + this.FK_Flow + "&FromNode=" + this.FromNode+"&PWorkID="+this.PWorkID, true);
				return;
			}
		} else if ("Btn_ReturnWork".equals(btnId))
		{
			this.BtnReturnWork();
		}else if (NamesOfBtn.Shift.equals(btnId))
		{
			this.DoShift();
		} else if ("Btn_WorkerList".equals(btnId)){
			if (getWorkID() == 0)
			{
				throw new RuntimeException("没有指定当前的工作,不能查看工作者列表.");
			}
		} else if ("Btn_PrintWorkRpt".equals(btnId))
		{
			if (getWorkID() == 0)
			{
				throw new RuntimeException("没有指定当前的工作,不能打印工作报告.");
			}
			try {
				PubClass.WinOpen(response,"WFRpt.jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + getWorkID(), "工作报告", 800, 600);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else if(NamesOfBtn.Send.equals(btnId)){
			try {
				MyFlowController.Send(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 保存
	*/
	protected final Btn getBtn_Save()
	{
		Btn btn = (Btn) BaseModel.GetBtnByID(NamesOfBtn.Save.toString());
		if (btn == null)
		{
			btn = new Btn();
		}
		return btn;
	}
	
	public final void BtnReturnWork()
	{
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();
		//wk = (Work)this.UCEn1.Copy(wk);

		String msg = BP.WF.Glo.DealExp(nd.getFocusField(), wk, null);
		try {
			response.sendRedirect("./WorkOpt/ReturnWork.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + wk.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	public final void DoShift()
	{
		//GenerWorkFlow gwf = new GenerWorkFlow();
		//if (gwf.Retrieve(GenerWorkFlowAttr.WorkID, this.WorkID) == 0)
		//{
		//    this.Alert("工作还没有发出，您不能移交。");
		//    return;
		//}

		String msg = "";
		BP.WF.Node nd = new BP.WF.Node(gwf.getFK_Node());
		if (!nd.getFocusField().equals(""))
		{
			Work wk = nd.getHisWork();
			wk.setOID(this.getWorkID());
			wk.Retrieve();
			msg = BP.WF.Glo.DealExp(nd.getFocusField(), wk, null);
			// wk.Update(nd.FocusField, msg);
		}
		String url = "./WorkOpt/Forward.jsp?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + msg;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	BindWork
	*/
	public final void BindWork(Node nd, Work wk)
	{
		if (nd.getHisFlow().getIsMD5() && nd.getIsStartNode() == false && wk.IsPassCheckMD5() == false)
		{
			this.toMsg("<font color=red>数据已经被非法篡改，请通知管理员解决问题。</font>", "Info");
			return;
		}
		currWK = wk;
		switch (nd.getHisNodeWorkType())
		{
			case StartWorkFL:
			case WorkFHL:
			case WorkFL:
			case WorkHL:
				if (this.getFID() != 0 && this.getFID() != this.getWorkID())
				{
					/* 这种情况是分流节点向退回到了分河流。*/
					this.FlowMsg.append(AddFieldSet("分流节点退回信息"));
		
					ReturnWork rw = new ReturnWork();
					rw.Retrieve(ReturnWorkAttr.WorkID, this.getWorkID(), ReturnWorkAttr.ReturnToNode, nd.getNodeID());
					this.FlowMsg.append(rw.getBeiZhuHtml());
					this.FlowMsg.append(AddHR());
					//this.UCEn1.addb
					TextBox tb = new TextBox();
					tb.setId("TB_Doc");
					tb.setTextMode(TextBoxMode.MultiLine);
					tb.setRows(7);
					tb.setColumns(50);
					this.UCEn1.Add(tb);
		
					this.UCEn1.AddBR();
					Btn btn = new Btn();
					btn.setId("Btn_Reject");
					btn.setText("驳回工作");
					//btn.Click += new EventHandler(ToolBar1_ButtonClick);
					this.FlowMsg.append(Add(btn));
		
					btn = new Btn();
					btn.setId("Btn_KillSubFlow");
					btn.setText("终止工作");
					//btn.Click += new EventHandler(ToolBar1_ButtonClick);
					this.FlowMsg.append(Add(btn));
					this.FlowMsg.append(AddFieldSetEnd()); // ("分流节点退回信息");
		
					//this.ToolBar1.Controls.Clear();//.Clear();
					//this.Response.Write("<script language='JavaScript'> DoSubFlowReturn('" + this.FID + "','" + wk.OID + "','" + nd.NodeID + "');</script>");
					//this.Response.Write("<javascript ></javascript>");
					return;
				}
				break;
			default:
				break;
		}

		if (nd.getIsStartNode())
		{
			/*判断是否来与子流程.*/
			if (StringHelper.isNullOrEmpty(request.getParameter("FromNode")) == false)
			{
				if (this.getPWorkID() == 0)
				{
					throw new RuntimeException("流程设计错误，调起子流程时，没有接受到PWorkID参数。");
				}
		
				/*** 如果来自于主流程 */
				//int FromNode = int.Parse(this.Request.QueryString["FromNode"]);
				//BP.WF.Node FromNode_nd = new BP.WF.Node(FromNode);
				//Work fromWk = FromNode_nd.HisWork;
				//fromWk.OID = this.PWorkID;
				//fromWk.RetrieveFromDBSources();
				//wk.Copy(fromWk);
				//   wk.FID = this.FID;
			}
		
			if (("SetParentFlow").equals(this.getDoFunc()))
			{
				/*如果需要设置父流程信息。*/
				String cFlowNo = this.getCFlowNo();
				String[] workids = this.getWorkIDs().split("[,]", -1);
				int count = workids.length;
				//this.Pub1.AddFieldSet("分组审阅", "一共选择了(" + count + ")个子流程被合并审阅,分别是:" + this.WorkIDs);//ID提示没有意义
				this.UCEn1.AddFieldSet("分组审阅", "一共选择了(" + count + ")个子流程被合并审阅。");
			}
		}
	
		// 处理传递过来的参数。
		Enumeration enu =  request.getParameterNames();
		while (enu.hasMoreElements())
		{
			String key = (String)enu.nextElement();
			wk.SetValByKey(key, request.getParameter(key));
		}
	
		wk.ResetDefaultVal();
		wk.DirectUpdate(); //需要把默认值保存里面去，不然，就会导致当前默认信息存储不了。
	
		NodeFormType ft = nd.getHisFormType();
		if (BP.Web.WebUser.getIsWap())
		{
			ft = NodeFormType.FoolForm;
		}
	
		switch (nd.getHisFormType())
		{
			case FreeForm:
			case DisableIt:
			case WebOffice:
			case FoolForm:
				Frms frms = nd.getHisFrms();
				if (nd.getHisFormType().getValue() == NodeFormType.FreeForm.getValue())
				{
					/* 仅仅只有节点表单的情况。 */
					/* 添加保存表单函数，以便自定义按钮调用，执行表单的保存前后事件。 */
					this.UCEn1.Add("\t\n<script type='text/javascript'>");
					this.UCEn1.Add("\t\n function SaveFormData() {");
					this.UCEn1.Add("\t\n     var btn = document.getElementById('" + getBtn_Save().getClientID() + "');");
					this.UCEn1.Add("\t\n     if (btn) {");
					this.UCEn1.Add("\t\n         btn.click();");
					this.UCEn1.Add("\t\n      }");
					this.UCEn1.Add("\t\n  }");
					this.UCEn1.Add("\t\n</script>");
					/* 自由表单 */
		
					MapData map = new MapData(nd.getNodeFrmID());
					setWidth(map.getMaxRight() + map.getMaxLeft() * 2 + 10 + "");
					if (Float.parseFloat(getWidth()) < 500)
					{
						setWidth("900");
					}
		
					setHeight(map.getMaxEnd() > map.getFrmH() ? map.getMaxEnd() + "" : map.getFrmH() + "");
					if (Float.parseFloat(getHeight()) <= 800)
					{
						setHeight("800");
					}
		
					this.UCEn1.Add("<div id=divCCForm style='width:" + getWidth() + "px;height:" + getHeight() + "px' >");
					//是否要重新装载数据.
					boolean isLoadData = false;
					//设置url.
					if (gwf.getWFState() == WFState.Runing || gwf.getWFState() == WFState.Blank || gwf.getWFState() == WFState.Draft)
					{
						isLoadData = true;
					}
		
					//this.UCEn1.IsLoadData = isLoadData;
					this.UCEn1.BindCCForm(wk, nd.getNodeFrmID(), false, 0, isLoadData);
					if (wk.getWorkEndInfo().length() > 2)
					{
						this.Pub3.append(Add(wk.getWorkEndInfo()));
					}
					this.UCEn1.Add("</div>");
		
				}
				else if (nd.getHisFormType().getValue() == NodeFormType.FoolForm.getValue())
				{
					/* 仅仅只有节点表单的情况。 */
					/*傻瓜表单*/
					MapData map = new MapData("ND" + getFK_Node());
		
					if (map.getTableWidth().contains("px"))
					{
						setWidth(map.getTableWidth().replace("px", ""));
					}
					else
					{
						setWidth(map.getTableWidth() + "");
					}
					if (map.getTableWidth().equals("100%"))
					{
						setWidth("900");
					}
					int labCol = 80;
					int ctrlCol = 260;
					int width1 = (labCol + ctrlCol) * map.getTableCol() / 2;
					setWidth(width1 + "");
		
					setHeight(map.getMaxEnd() + "");
					this.UCEn1.Add("<div id=divCCForm style='width:" + getWidth() + "px;height:" + getHeight() + "px;overflow-x:scroll;' >");
					this.UCEn1.BindColumn4(wk, nd.getNodeFrmID()); //, false, false, null);
					if (wk.getWorkEndInfo().length() > 2)
					{
						this.Pub3.append(Add(wk.getWorkEndInfo()));
					}
					this.UCEn1.Add("</div>");
				}
				else
				{
					/* 节点表单与独立表单混合存在的情况。  */
					//隐藏保存按钮
					/**
					 *  公文表单报错，暂时注释
					if (this.toolbar.IsExit(NamesOfBtn.Save) == true)
					{
						this.getBtn_Save().setVisible(false);
					}
					*/
					
					// 让其直接update，来接受外部传递过来的信息。
					if (nd.getHisFormType() != NodeFormType.DisableIt)
					{
						wk.DirectUpdate();
					}
		
					/*涉及到多个表单的情况...*/
					if (nd.getHisFormType() != NodeFormType.DisableIt)
					{
						Frm myfrm = new Frm();
						myfrm.setNo("ND" + nd.getNodeID());
						myfrm.setName(wk.getEnDesc());
						//myfrm.HisFormType = nd.HisFormType;
						myfrm.setHisFormRunType(FormRunType.values()[nd.getHisFormType().getValue()]);
						FrmNode fnNode = new FrmNode();
						fnNode.setFK_Frm(myfrm.getNo());
					//    fnNode.IsEdit = true;
						fnNode.setIsPrint(false);
						switch (nd.getHisFormType())
						{
							case FoolForm:
								fnNode.setHisFrmType(FrmType.FoolForm);
								break;
							case FreeForm:
								fnNode.setHisFrmType(FrmType.FreeFrm);
								break;
							case SelfForm:
								fnNode.setHisFrmType(FrmType.Url);
								break;
							case WebOffice:
								fnNode.setHisFrmType(FrmType.WebOffice);
								break;
							default:
								throw new RuntimeException("出现了未判断的异常。");
						}
						myfrm.HisFrmNode = fnNode;
						frms.AddEntity(myfrm, 0);
					}
		
					long fid = this.getFID();
					if (this.getFID() == 0)
					{
						fid = this.getWorkID();
					}
		
					if (frms.size() == 1)
					{
						/* 仅仅只有一个独立表单的情况。 */
						Frm frm = (Frm)frms.get(0);
						FrmNode fn = frm.HisFrmNode;
						String src = "";
		
						//src = "./CCForm/" + fn.FrmUrl + ".jsp?FK_MapData=" + frm.No + "&FID=" + fid + "&IsEdit=" + fn.IsEditInt + "&IsPrint=" + fn.IsPrintInt + "&FK_Node=" + nd.NodeID + "&WorkID=" + this.WorkID;
						//this.UCEn1.Add("\t\n <DIV id='" + frm.No + "' style='width:" + frm.FrmW + "px; height:" + frm.FrmH + "px;text-align: left;' >");
						//this.UCEn1.Add("\t\n <iframe ID='F" + frm.No + "' src='" + src + "' frameborder=0  style='position:absolute;width:" + frm.FrmW + "px; height:" + frm.FrmH + "px;text-align: left;'  leftMargin='0'  topMargin='0'  /></iframe>");
						//this.UCEn1.Add("\t\n </DIV>");
		
						//this.UCEn1.Add("\t\n<script type='text/javascript'>");
						//this.UCEn1.Add("\t\n function SaveDtlAll(){}");
						//this.UCEn1.Add("\t\n</script>");
		
						//this.Page.RegisterClientScriptBlock("sg", "<link href='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/Tab.css' rel='stylesheet' type='text/css' />");
						//this.Page.RegisterClientScriptBlock("s2g4", "<script language='JavaScript' src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/jquery.min.js' ></script>");
						//this.Page.RegisterClientScriptBlock("sdf24j", "<script language='JavaScript' src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/jquery.idTabs.min.js' ></script>");
						//this.Page.RegisterClientScriptBlock("sdsdf24j", "<script language='JavaScript' src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/TabClick.js' ></script>");
		
						String urlExtFrm = (String) request.getParameterNames().nextElement();
						if (urlExtFrm.contains("WorkID") == false)
						{
							urlExtFrm += "&WorkID=" + this.getWorkID();
						}
		
						if (urlExtFrm.contains("NodeID") == false)
						{
							urlExtFrm += "&NodeID=" + nd.getNodeID();
						}
		
						if (urlExtFrm.contains("FK_Node") == false)
						{
							urlExtFrm += "&FK_Node=" + nd.getNodeID();
						}
		
						if (urlExtFrm.contains("UserNo") == false)
						{
							urlExtFrm += "&UserNo=" + WebUser.getNo();
						}
		
						if (urlExtFrm.contains("SID") == false)
						{
							urlExtFrm += "&SID=" + WebUser.getSID();
						}
		
						//设置url.
						if (gwf.getWFState() == WFState.Runing || gwf.getWFState() == WFState.Blank || gwf.getWFState() == WFState.Draft)
						{
							if (urlExtFrm.contains("IsLoadData") == false)
							{
								urlExtFrm += "&IsLoadData=1";
							}
							else
							{
								urlExtFrm = urlExtFrm.replace("&IsLoadData=1", "&IsLoadData=0");
							}
						}
		
						//#endregion 载入相关文件.
						src = fn.getFrmUrl() + ".jsp?FK_MapData=" + frm.getNo() + "&FID=" + fid + "&IsEdit=" + fn.getIsEditInt() + "&IsPrint=" + fn.getIsPrintInt() + urlExtFrm;
		
						setWidth(frm.getFrmW() + "");
		
						this.UCEn1.Add("\t\n<div  id='usual2'  class='usual' style='width:" + frm.getFrmW() + "px;height:auto;margin:0 auto;background-color:white;'>"); //begain.
		
						//#region 输出标签.
						this.UCEn1.Add("\t\n <ul  class='abc' style='background:red;border-color: #800000;border-width: 10px;' >");
						this.UCEn1.Add("\t\n<li><a ID='HL" + frm.getNo() + "' href=\"#" + frm.getNo() + "\" onclick=\"TabClick('" + frm.getNo() + "','" + src + "');\" >" + frm.getName() + "</a></li>");
						this.UCEn1.Add("\t\n </ul>");
						//#endregion 输出标签.
		
						//#region 输出表单 iframe 内容.
						this.UCEn1.Add("\t\n <DIV id='" + frm.getNo() + "' style='width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;' >");
						this.UCEn1.Add("\t\n <iframe ID='F" + frm.getNo() + "' Onblur=\"OnTabChange('" + frm.getNo() + "',this);\" src='" + src + "' frameborder=0  style='width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;'  leftMargin='0'  topMargin='0'   /></iframe>");
						this.UCEn1.Add("\t\n </DIV>");
						//#endregion 输出表单 iframe 内容.
		
						this.UCEn1.Add("\t\n</div>"); // end  usual2
		
						// 设置选择的默认值.
						this.UCEn1.Add("\t\n<script type='text/javascript'>");
						this.UCEn1.Add("\t\n  $(\"#usual2 ul\").idTabs(\"" + frm.getNo() + "\");");
		
						this.UCEn1.Add("\t\n function SaveDtlAll(){");
						this.UCEn1.Add("\t\n   var tabText = document.getElementById('HL" + frm.getNo() + "').innerText;");
						this.UCEn1.Add("\t\n   var scope = document.getElementById('F" + frm.getNo() + "');");
						this.UCEn1.Add("\t\n   var lastChar = tabText.substring(tabText.length - 1, tabText.length);");
						this.UCEn1.Add("\t\n   if (lastChar == \"*\") {");
						this.UCEn1.Add("\t\n   var contentWidow = scope.contentWindow;");
						this.UCEn1.Add("\t\n   contentWidow.SaveDtlData();");
						this.UCEn1.Add("\t\n   }");
						this.UCEn1.Add("\t\n}");
		
						this.UCEn1.Add("\t\n</script>");
		
					}
					else
					{
						/* 节点表单与独立表单混合存在。 */
						//this.Page.RegisterClientScriptBlock("sg", "<link href='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/Tab.css' rel='stylesheet' type='text/css' />");
						//this.Page.RegisterClientScriptBlock("s2g4", "<script language='JavaScript' src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/jquery.min.js' ></script>");
						//this.Page.RegisterClientScriptBlock("sdf24j", "<script language='JavaScript' src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/jquery.idTabs.min.js' ></script>");
						//this.Page.RegisterClientScriptBlock("sdsdf24j", "<script language='JavaScript' src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Style/Frm/TabClick.js' ></script>");
						
						String urlExtFrm = (String)request.getParameterNames().nextElement();
						if (urlExtFrm.contains("WorkID") == false)
						{
							urlExtFrm += "&WorkID=" + this.getWorkID();
						}
		
						if (urlExtFrm.contains("NodeID") == false)
						{
							urlExtFrm += "&NodeID=" + nd.getNodeID();
						}
		
						if (urlExtFrm.contains("FK_Node") == false)
						{
							urlExtFrm += "&FK_Node=" + nd.getNodeID();
						}
		
						if (urlExtFrm.contains("UserNo") == false)
						{
							urlExtFrm += "&UserNo=" + WebUser.getNo();
						}
		
						if (urlExtFrm.contains("SID") == false)
						{
							urlExtFrm += "&SID=" + WebUser.getSID();
						}
		
						//设置url.
						if (gwf.getWFState().getValue() == WFState.Runing.getValue() || gwf.getWFState().getValue() == WFState.Blank.getValue() || gwf.getWFState().getValue() == WFState.Draft.getValue())
						{
							if (urlExtFrm.contains("IsLoadData") == false)
							{
								urlExtFrm += "&IsLoadData=1";
							}
							else
							{
								urlExtFrm = urlExtFrm.replace("&IsLoadData=1", "&IsLoadData=0");
							}
						}
		
						Frm frmFirst = null;
						for (Frm frm : frms.ToJavaList())
						{
							if (frmFirst == null)
							{
								frmFirst = frm;
							}
		
							if (frmFirst.getFrmW() < frm.getFrmW())
							{
								frmFirst = frm;
							}
						}
		
						this.UCEn1.Clear();
						this.UCEn1.Add("<div  style='clear:both' ></div>");
						this.UCEn1.Add("\t\n<div  id='usual2' class='usual' style='width:" + frmFirst.getFrmW() + "px;height:auto;margin:0 auto;background-color:white;'>");
						setWidth(frmFirst.getFrmW() + "");
		
						//added by liuxc,修改了多个excel表单放于一个页面中的逻辑，2015.02.07
						String excelFrmNos = "";
						int excelFrmIdx = 0;
						int excelFrmCount = 0;
						for (Frm frm : frms.ToJavaList())
						{
							if (frm.HisFrmNode.getHisFrmType().getValue() != FrmType.ExcelFrm.getValue())
							{
								continue;
							}
							excelFrmNos += frm.getNo() + ",";
							excelFrmCount++;
						}
						//end added
		
						//#region 输出标签.
						this.UCEn1.Add("\t\n <ul  class='abc' style='background:red;border-color: #800000;border-width: 10px;' >");
						for (Frm frm : frms.ToJavaList())
						{
							FrmNode fn = frm.HisFrmNode;
		
							//2015.02.07,added by liuxc
							if (fn.getHisFrmType().getValue() == FrmType.ExcelFrm.getValue())
							{
								if (excelFrmIdx >= 1)
								{
									continue;
								}
		
								excelFrmIdx++;
							}
							//end added
		
							String src = "";
							//src = fn.FrmUrl + ".jsp?FK_MapData=" + frm.No + "&IsEdit=" + fn.IsEditInt + "&IsPrint=" + fn.IsPrintInt + urlExtFrm;
							//this.UCEn1.Add("\t\n<li><a ID='HL" + frm.No + "' href=\"#" + frm.No + "\" onclick=\"TabClick('" + frm.No + "','" + src + "');\" >" + frm.Name + "</a></li>");
		
							//2015.02.07,edited by liuxc
							src = fn.getFrmUrl() + ".jsp?FK_MapData=" + (fn.getHisFrmType() == FrmType.ExcelFrm ? StringHelper.trimEnd(excelFrmNos, ',') : frm.getNo()) + "&IsEdit=" + fn.getIsEditInt() + "&IsPrint=" + fn.getIsPrintInt() + urlExtFrm;
							this.UCEn1.Add("\t\n<li><a ID='HL" + frm.getNo() + "' href=\"#" + frm.getNo() + "\" onclick=\"TabClick('" + frm.getNo() + "','" + src + "');\" >" + (fn.getHisFrmType() == FrmType.ExcelFrm ? String.format("Excel表单[%1$s]",excelFrmCount) : frm.getName()) + "</a></li>");
						}
						this.UCEn1.Add("\t\n </ul>");
						//#endregion 输出标签.
		
						//#region 输出表单 iframe 内容.
						excelFrmIdx = 0;
						for (Frm frm : frms.ToJavaList())
						{
							FrmNode fn = frm.HisFrmNode;
		
							//2015.02.07,added by liuxc
							if (fn.getHisFrmType().getValue() == FrmType.ExcelFrm.getValue())
							{
								if (excelFrmIdx >= 1)
								{
									continue;
								}
		
								excelFrmIdx++;
							}
							//end added
		
							this.UCEn1.Add("\t\n <DIV id='" + frm.getNo() + "' style='width:" + frmFirst.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;margin:0px;padding:0px;' >");
							this.UCEn1.Add("\t\n <iframe ID='F" + frm.getNo() + "' Onblur=\"OnTabChange('" + frm.getNo() + "',this);\" src='loading.htm' frameborder=0  style='margin:0px;padding:0px;width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;' /></iframe>");
							this.UCEn1.Add("\t\n </DIV>");
						}
						//#endregion 输出表单 iframe 内容.
		
						this.UCEn1.Add("\t\n</div>"); // end  usual2
		
						// 设置选择的默认值.
						this.UCEn1.Add("\t\n<script type='text/javascript'>");
						this.UCEn1.Add("\t\n  $(\"#usual2 ul\").idTabs(\"" + ((Frm)frms.get(0)).getNo() + "\");");
						this.UCEn1.Add("\t\n function SaveDtlAll(){}");
		
						this.UCEn1.Add("\t\n function SaveDtlAll(){");
						this.UCEn1.Add("\t\n   var tabText = document.getElementById('HL' + currentTabId).innerText;");
						this.UCEn1.Add("\t\n   var scope = document.getElementById('F' + currentTabId);");
						this.UCEn1.Add("\t\n   var lastChar = tabText.substring(tabText.length - 1, tabText.length);");
						this.UCEn1.Add("\t\n   if (lastChar == \"*\") {");
						this.UCEn1.Add("\t\n   var contentWidow = scope.contentWindow;");
						this.UCEn1.Add("\t\n   contentWidow.SaveDtlData();");
						this.UCEn1.Add("\t\n   }");
						this.UCEn1.Add("\t\n}");
		
						this.UCEn1.Add("\t\n</script>");
					}
				}
				return;
			case SelfForm:
				wk.Save();
				if (this.getWorkID() == 0)
				{
					this.setWorkID(wk.getOID());
				}
		
				String url = this.DealUrl(getcurrND());
		
				//#region 在这里增加 url.
				String info = "<iframe ID='SelfForm' src='" + url + "' frameborder=0  style='width:100%; height:800px' leftMargin='0' topMargin='0' />";
				info += "\t\n</iframe>";
				this.UCEn1.Add(info);
		
				return;
			case SDKForm:
			default:
				throw new RuntimeException("@没有涉及到的扩充。" + nd.getHisFormType() + " 节点表单类型.");
			}	
	}				
	/** 
	父流程ID.
	*/
	public final int getPWorkID()
	{
		try
		{
			String s = request.getParameter("PWorkID");
			if (StringHelper.isNullOrEmpty(s) == true)
			{
				s = request.getParameter("PWorkID");
			}
			if (StringHelper.isNullOrEmpty(s) == true)
			{
				s = "0";
			}
			return Integer.parseInt(s);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	/** 
	 执行功能
	*/
	public final String getDoFunc()
	{
		return request.getParameter("DoFunc");
	}
	/** 
	 子流程编号
	*/
	public final String getCFlowNo()
	{
		return request.getParameter("CFlowNo");
	}
	/** 
	 工作IDs
	*/
	public final String getWorkIDs()
	{
		return request.getParameter("WorkIDs");
	}
	
	private String _width = "";
	public final String getWidth()
	{
		return _width;
	}
	public final void setWidth(String value)
	{
		_width = value;
	}
	
	private String _height = "";
	public final String getHeight()
	{
		return _height;
	}
	public final void setHeight(String value)
	{
		_height = value;
	}
}