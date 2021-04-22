package bp.wf.httphandler;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import bp.wf.*;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.*;

/**
 * 抄送处理类
 */
public class WF_MyView extends WebContralBase {
	/**
	 * 抄送处理类
	 */
	public WF_MyView() {

	}
	/// <summary>
    /// 表单查看
    /// </summary>
    /// <returns></returns>
    public String MyFrm_Init_Data()throws  Exception
    {
        //String sql = "SELECT FrmDB FROM ND" + Integer.parseInt(this.getFK_Flow())+ "Track WHERE MyPK='" + this.getMyPK() + "'";
        return DBAccess.GetBigTextFromDB("ND"+Integer.parseInt(this.getFK_Flow())+ "Track", "MyPK", this.getMyPK(), "FrmDB");
        //return DBAccess.RunSQLReturnStringIsNull(sql,"err@没有获得数据.");
    }

	/// 运行变量
	/**
	 * 从节点.
	 */
	public final String getFromNode() {
		return this.GetRequestVal("FromNode");
	}

	/**
	 * 是否抄送
	 */
	public final boolean getIsCC() {
		String str = this.GetRequestVal("Paras");

		if (DataType.IsNullOrEmpty(str) == false) {
			String myps = str;

			if (myps.contains("IsCC=1") == true) {
				return true;
			}
		}

		str = this.GetRequestVal("AtPara");
		if (DataType.IsNullOrEmpty(str) == false) {
			if (str.contains("IsCC=1") == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 轨迹ID
	 */
	public final String getTrackID() {
		return this.GetRequestVal("TrackeID");
	}

	/**
	 * 到达的节点ID
	 */
	public final int getToNode() {
		return this.GetRequestValInt("ToNode");
	}

	private int _FK_Node = 0;

	/**
	 * 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	 */
	public final int getFK_Node() {
		String fk_nodeReq = this.GetRequestVal("FK_Node"); // this.Request.Form["FK_Node"];
		if (DataType.IsNullOrEmpty(fk_nodeReq)) {
			fk_nodeReq = this.GetRequestVal("NodeID"); // this.Request.Form["NodeID"];
		}

		if (DataType.IsNullOrEmpty(fk_nodeReq) == false) {
			return Integer.parseInt(fk_nodeReq);
		}

		if (_FK_Node == 0) {
			if (this.getWorkID() != 0) {
				Paras ps = new Paras();
				ps.SQL = "SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr()
						+ "WorkID";
				ps.Add("WorkID", this.getWorkID());
				_FK_Node = DBAccess.RunSQLReturnValInt(ps, 0);
			} else {
				_FK_Node = Integer.parseInt(this.getFK_Flow() + "01");
			}
		}
		return _FK_Node;
	}

	private String _width = "";

	/**
	 * 表单宽度
	 */
	public final String getWidth() {
		return _width;
	}

	public final void setWidth(String value) throws Exception {
		_width = value;
	}

	private String _height = "";

	/**
	 * 表单高度
	 */
	public final String getHeight() {
		return _height;
	}

	public final void setHeight(String value) throws Exception {
		_height = value;
	}

	public String _btnWord = "";

	public final String getBtnWord() {
		return _btnWord;
	}

	public final void setBtnWord(String value) throws Exception {
		_btnWord = value;
	}

	private GenerWorkFlow _HisGenerWorkFlow = null;

	public final GenerWorkFlow getHisGenerWorkFlow() throws Exception {
		if (_HisGenerWorkFlow == null) {
			_HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
		}
		return _HisGenerWorkFlow;
	}

	private Node _currNode = null;

	public final Node getCurrND() throws Exception {
		if (_currNode == null) {
			_currNode = new Node(this.getFK_Node());
		}
		return _currNode;
	}

	private Flow _currFlow = null;

	public final Flow getCurrFlow() throws Exception {
		if (_currFlow == null) {
			_currFlow = new Flow(this.getFK_Flow());
		}
		return _currFlow;
	}

	/**
	 * 定义跟路径
	 */
	public String appPath = "/";

	///

	public final String InitToolBar() throws Exception {
		//打开表单只看轨迹 链接加上Track=1
		// Track为null的异常处理
		boolean isShowTrackBarOnly = false;
		if (!DataType.IsNullOrEmpty(this.GetRequestVal("Track"))) 
			 isShowTrackBarOnly= this.GetRequestVal("Track").toString().equals("1")?true:false;
		
		DataTable dt = new DataTable("ToolBar");
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("Oper");

		BtnLab btnLab = new BtnLab(this.getFK_Node());
		String tKey = DataType.getCurrentDateByFormart("MM-dd-hh:mm:ss");
		String toolbar = "";
		try {
			DataRow dr = dt.NewRow();
			if(isShowTrackBarOnly==false) {
				dr.setValue("No", "Close");
				dr.setValue("Name", "关闭");
				dr.setValue("Oper", "Close();");
				dt.Rows.add(dr);
			}

			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

			/// 根据流程权限控制规则获取可以操作的按钮功能
			String sql = "SELECT A.PowerFlag,A.EmpNo,A.EmpName FROM WF_PowerModel A WHERE PowerCtrlType =1" + " UNION "
					+ "SELECT A.PowerFlag,B.No,B.Name FROM WF_PowerModel A, Port_Emp B, Port_Deptempstation C WHERE A.PowerCtrlType = 0 AND B.No=C.FK_Emp AND A.StaNo = C.FK_Station";
			sql = "SELECT PowerFlag From(" + sql + ")D WHERE  D.EmpNo='" + WebUser.getNo() + "'";

			String powers = DBAccess.RunSQLReturnStringIsNull(sql, "");
			switch (gwf.getWFState()) {
			case Runing: // 运行时
				/* 删除流程. */
				if (powers.contains("FlowDataDelete") == true
						|| (bp.wf.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(),
								WebUser.getNo()) == true && btnLab.getDeleteEnable() != 0)) {
					dr = dt.NewRow();
					dr.setValue("No", "Delete");
					dr.setValue("Name", btnLab.getDeleteLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				/* 取回审批 */
				String para = "";
				sql = "SELECT NodeID FROM WF_Node WHERE CheckNodes LIKE '%" + gwf.getFK_Node() + "%'";
				int myNode = DBAccess.RunSQLReturnValInt(sql, 0);
				if (myNode != 0) {
					GetTask gt = new GetTask(myNode);
					if (gt.Can_I_Do_It()) {
						dr = dt.NewRow();
						dr.setValue("No", "TackBack");
						dr.setValue("Name", "取回审批");
						dr.setValue("Oper", "TackBack(" + gwf.getFK_Node() + "," + myNode + ")");
						dt.Rows.add(dr);

					}
				}

				/* 撤销发送 */
				if(SystemConfig.getCustomerNo().equals("GJTLJ")==true){
					//针对国家铁路局的撤销显示的问题------如果下一个节点已经打开就不能撤销

					Node nd = new Node(this.getFK_Node());
					String toNodes = nd.getHisToNDs();
					if(gwf.getFK_Node()!=nd.getNodeID()&& toNodes.indexOf("@"+gwf.getFK_Node())!=-1){
						//需要判断下一个节点是否已经打开，如果打开就不能撤销
						int i = DBAccess.RunSQLReturnValInt("SELECT SUM(IsRead) AS Num FROM WF_GenerWorkerList WHERE WorkID=" + this.getWorkID() + " AND FK_Node=" + gwf.getFK_Node(), 0);
						if(i==0){
							dr = dt.NewRow();
							dr.setValue("No", "UnSend");
							dr.setValue("Name", "撤销");
							dr.setValue("Oper", "UnSend()");
							dt.Rows.add(dr);
						}
					}

				}else{
					GenerWorkerLists workerlists = new GenerWorkerLists();
					QueryObject info = new QueryObject(workerlists);
					info.AddWhere(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
					info.addAnd();
					info.AddWhere(GenerWorkerListAttr.IsPass, "1");
					info.addAnd();
					info.AddWhere(GenerWorkerListAttr.IsEnable, "1");
					info.addAnd();
					info.AddWhere(GenerWorkerListAttr.WorkID, this.getWorkID());
					info.addOrderByDesc(GenerWorkerListAttr.RDT);
					if (info.DoQuery() > 0 || powers.contains("FlowDataUnSend") == true) {
						dr = dt.NewRow();
						dr.setValue("No", "UnSend");
						dr.setValue("Name", "撤销");
						dr.setValue("Oper", "UnSend()");
						dt.Rows.add(dr);
					}
				}


				// 流程结束
				if (powers.contains("FlowDataOver") == true) {
					dr = dt.NewRow();
					dr.setValue("No", "EndFlow");
					dr.setValue("Name", btnLab.getEndFlowLab());
					dr.setValue("Oper", "DoStop('" + btnLab.getEndFlowLab() + "','" + this.getFK_Flow() + "','"
							+ this.getWorkID() + "');");
					dt.Rows.add(dr);

				}

				// 催办
				if (powers.contains("FlowDataPress") == true || gwf.getEmps().contains(WebUser.getNo()) == true&&isShowTrackBarOnly==false) {
					dr = dt.NewRow();
					dr.setValue("No", "Press");
					dr.setValue("Name", "催办");
					dr.setValue("Oper", "Press();");
					dt.Rows.add(dr);
				}

				break;
			case Complete: // 完成.
			case Delete: // 逻辑删除..
				/* 恢复使用流程 */
				if (WebUser.getNo().equals("admin") == true || powers.contains("FlowDataRollback") == true) {
					dr = dt.NewRow();
					dr.setValue("No", "Rollback");
					dr.setValue("Name", "回滚");
					dr.setValue("Oper", "Rollback();");
					dt.Rows.add(dr);
				}

				break;
			// case WFState.HungUp: // 挂起.
			// /*撤销挂起*/
			// if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(WorkID,
			// WebUser.getNo()) == true)
			// toolbar += "<input name='UnHungUp' type='button' value='撤销挂起'
			// enable='true' onclick='UnHungUp()'/>";
			// break;
			default:
				break;
			}

			dr = dt.NewRow();
			dr.setValue("No", "Track");
			dr.setValue("Name", "轨迹");
			dr.setValue("Oper", "");
			dt.Rows.add(dr);

			/// 根据流程权限控制规则获取可以操作的按钮功能

			/// 加载流程查看器 - 按钮

			/* 打包下载zip */
			if (btnLab.getPrintZipMyView() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_zip");
				dr.setValue("Name", btnLab.getPrintZipLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlMyView() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_html");
				dr.setValue("Name", btnLab.getPrintHtmlLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载pdf */
			if (btnLab.getPrintPDFMyView() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_pdf");
				dr.setValue("Name", btnLab.getPrintPDFLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}
			/* 公文标签 */
			if (btnLab.getOfficeBtnEnable() == true && btnLab.getOfficeBtnLocal()==0) {
				dr = dt.NewRow();
				dr.setValue("No", "DocWord");
				dr.setValue("Name", btnLab.getOfficeBtnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/// 加载流程查看器 - 按钮

			/// 加载自定义的button.
			bp.wf.template.NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node(), NodeToolbarAttr.IsMyView, 1, NodeToolbarAttr.Idx);
			for (NodeToolbar bar : bars.ToJavaList()) {

				if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) == false
						&& bar.getTarget().toLowerCase().equals("javascript"))) {
					dr = dt.NewRow();
					dr.setValue("No", "NodeToolBar");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", bar.getUrl());
					dt.Rows.add(dr);
				} else {
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
							+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					dr = dt.NewRow();
					dr.setValue("No", "NodeToolBar");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", "WinOpen('" + urlr3 + "')");
					dt.Rows.add(dr);
				}
			}

			/// //加载自定义的button.

		} catch (RuntimeException ex) {
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
			toolbar = "err@" + ex.getMessage();
		}
		
		
		
		
		
		
		
		return bp.tools.Json.ToJson(dt);
	}

	/**
	 * 撤销
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyView_UnSend() throws Exception {
		// 获取用户当前所在的节点
		String currNode = "";
		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:
		case KingBase:
			currNode = "SELECT FK_Node FROM (SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo()
					+ "' Order by RDT DESC ) WHERE rownum=1";
			break;
		case MySQL:
		case PostgreSQL:
			currNode = "SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo()
					+ "' Order by RDT DESC LIMIT 1";
			break;
		case MSSQL:
			currNode = "SELECT TOP 1 FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo()
					+ "' Order by RDT DESC";
			break;
		default:
			break;
		}
		String unSendToNode = DBAccess.RunSQLReturnString(currNode);
		try {
			return bp.wf.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(),
					Integer.parseInt(unSendToNode), this.getFID());
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	public final boolean IsCanView(GenerWorkFlow gwf) throws Exception {
		// 是否可以处理当前工作？
		boolean isCanDoCurrWorker = gwf.getTodoEmps().contains(WebUser.getNo() + "," + WebUser.getName() + ";");
		if (isCanDoCurrWorker) {
			return true;
		}

		// 是否是工作参与人?
		boolean isWorker = gwf.getEmps().contains("@" + WebUser.getNo() + "," + WebUser.getName());
		if (isWorker == true) {
			return true;
		}

		if (WebUser.getNo().equals("admin") == true) {
			return true;
		}

		if (WebUser.getIsAdmin() == true && gwf.getOrgNo().equals(WebUser.getOrgNo()) == true) {
			return true;
		}

		// 处理流程控制权限.

		return true;
	}

	/**
	 * 初始化(处理分发)
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyView_Init() throws Exception {
		// 获得注册实体.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getWFState() == WFState.Delete) {
			return "err@当前流程已经删除无法查看.";
		}

		if (gwf.getWFState() == WFState.Blank) {
			return "err@当前流程还没有启动.";
		}

		if (gwf.getWFState() == WFState.Draft) {
			return "err@当前流程还是草稿.";
		}

		// 是否可以处理当前工作？
		boolean isCanDoCurrWorker = false;

		String toDoEmps = ";" + gwf.getTodoEmps();

		// 当前的流程还是运行中的，并且可以执行当前工作,如果是，就直接转到工作处理器.
		//如果当前流程是子线程，但是运行到的节点是合流点或者普通节点
		if(gwf.getFID()!=0){
			Node nd = new Node(gwf.getFK_Node());
			if(nd.getHisRunModel()==RunModel.SubThread && toDoEmps.contains(";" + WebUser.getNo() + ",")){
				WF_MyFlow handler = new WF_MyFlow();
				return handler.MyFlow_Init();
			}
		}
		if (gwf.getFID()==0 && gwf.getWFState() != WFState.Complete && toDoEmps.contains(";" + WebUser.getNo() + ",")) {
			WF_MyFlow handler = new WF_MyFlow();
			return handler.MyFlow_Init();
		}

		// 是否是工作参与人?
		boolean isWorker = gwf.getEmps().contains("@" + WebUser.getNo() + "," + WebUser.getName());
		if (isWorker == true || WebUser.getNo().equals("admin") == true || WebUser.getIsAdmin() == true) {
			// 可以查看工作,就执行以后的.
		} else {
			// 判断是否是抄送人员?
			CCList list = new CCList();
			boolean isExit = list.IsExit(CCListAttr.WorkID, this.getWorkID(), CCListAttr.CCTo, WebUser.getNo());
			// 如果是抄送人员.
			if (isExit == true) {
				// 是一个抄送人员.
				WF_MyCC mycc = new WF_MyCC();
				return mycc.MyCC_Init();
			}
		}

		if (IsCanView(gwf) == false) {
			return "err@您无权查看该工作.";
		}

		/// 处理表单类型.
		if (this.getCurrND().getHisFormType() == NodeFormType.SheetTree
				|| this.getCurrND().getHisFormType() == NodeFormType.SheetAutoTree) {

			/// 开始组合url.
			String toUrl = "";

			if (this.getIsMobile() == true) {
				if (gwf.getParasFrms().equals("") == false) {
					toUrl = "MyViewGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID()
							+ "&Frms=" + gwf.getParasFrms();
				} else {
					toUrl = "MyViewGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
				}
			} else {
				if (gwf.getParasFrms().equals("") == false) {
					toUrl = "MyViewTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID()
							+ "&Frms=" + gwf.getParasFrms();
				} else {
					toUrl = "MyViewTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
				}
			}

			String[] strs = this.getRequestParas().split("[&]", -1);
			for (String str : strs) {
				if (toUrl.contains(str) == true) {
					continue;
				}
				if (str.contains("DoType=") == true) {
					continue;
				}
				if (str.contains("DoMethod=") == true) {
					continue;
				}
				if (str.contains("HttpHandlerName=") == true) {
					continue;
				}
				if (str.contains("IsLoadData=") == true) {
					continue;
				}
				if (str.contains("IsCheckGuide=") == true) {
					continue;
				}

				toUrl += "&" + str;
			}
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				if (toUrl.contains(key + "=") == true)
					continue;
				toUrl += "&" + key + "=" + ContextHolderUtils.getRequest().getParameter(key);
			}

			/// 开始组合url.

			// 增加fk_node
			if (toUrl.contains("&FK_Node=") == false) {
				toUrl += "&FK_Node=" + this.getCurrND().getNodeID();
			}
			return "url@" + toUrl;
		}

		if (this.getCurrND().getHisFormType() == NodeFormType.SDKForm) {
			String url = getCurrND().getFormUrl();
			if (DataType.IsNullOrEmpty(url)) {
				return "err@设置读取状流程设计错误态错误,没有设置表单url.";
			}

			// 处理连接.
			url = this.MyView_Init_DealUrl(getCurrND(), url);

			// sdk表单就让其跳转.
			return "url@" + url;
		}

		/// 处理表单类型.

		// 求出当前节点frm的类型.
		NodeFormType frmtype = this.getCurrND().getHisFormType();
		if (frmtype != NodeFormType.RefOneFrmTree) {
			getCurrND().WorkID = this.getWorkID(); // 为获取表单ID ( NodeFrmID )提供参数.

			if (this.getCurrND().getNodeFrmID().contains(String.valueOf(this.getCurrND().getNodeID())) == false) {
				/* 如果当前节点引用的其他节点的表单. */
				String nodeFrmID = getCurrND().getNodeFrmID();
				String refNodeID = nodeFrmID.replace("ND", "");
				bp.wf.Node nd = new Node(Integer.parseInt(refNodeID));

				// 表单类型.
				frmtype = nd.getHisFormType();
			}
		}

		/// 内置表单类型的判断.
		/* 如果是傻瓜表单，就转到傻瓜表单的解析执行器上，为软通动力改造。 */

		if (frmtype == NodeFormType.FoolTruck) {
			String url = "MyViewGener.htm";

			// 处理连接.
			url = this.MyView_Init_DealUrl(getCurrND(), url);
			return "url@" + url;
		}

		if (frmtype == NodeFormType.WebOffice) {
			String url = "MyViewWebOffice.htm";

			// 处理连接.
			url = this.MyView_Init_DealUrl(getCurrND(), url);
			return "url@" + url;
		}

		if (frmtype == NodeFormType.FoolForm && this.getIsMobile() == false) {
			String url = "MyViewGener.htm";
			if (this.getIsMobile()) {
				url = "MyViewGener.htm";
			}

			// 处理连接.
			url = this.MyView_Init_DealUrl(getCurrND(), url);

			url = url.replace("DoType=MyView_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

		// 自定义表单
		if (frmtype == NodeFormType.SelfForm && this.getIsMobile() == false) {

			String url = "MyViewSelfForm.htm";

			// 处理连接.
			url = this.MyView_Init_DealUrl(getCurrND(), url);

			url = url.replace("DoType=MyView_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

		/// 内置表单类型的判断.

		String myurl = "MyViewGener.htm";

		// 处理连接.
		myurl = this.MyView_Init_DealUrl(getCurrND(), myurl);
		myurl = myurl.replace("DoType=MyView_Init&", "");
		myurl = myurl.replace("&DoWhat=StartClassic", "");

		return "url@" + myurl;
	}

	private String MyView_Init_DealUrl(bp.wf.Node currND) throws UnsupportedEncodingException, Exception {
		return MyView_Init_DealUrl(currND, null);
	}

	private String MyView_Init_DealUrl(bp.wf.Node currND, String url) throws UnsupportedEncodingException, Exception {
		if (url == null) {
			url = currND.getFormUrl();
		}

		String urlExt = this.getRequestParas();
		// 防止查询不到.
		urlExt = urlExt.replace("?WorkID=", "&WorkID=");
		if (urlExt.contains("&WorkID") == false) {
			urlExt += "&WorkID=" + this.getWorkID();
		} else {
			urlExt = urlExt.replace("&WorkID=0", "&WorkID=" + this.getWorkID());
			urlExt = urlExt.replace("&WorkID=&", "&WorkID=" + this.getWorkID() + "&");
		}

		// SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出.
		url = url.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

		if (urlExt.contains("&NodeID") == false) {
			urlExt += "&NodeID=" + currND.getNodeID();
		}

		if (urlExt.contains("FK_Node") == false) {
			urlExt += "&FK_Node=" + currND.getNodeID();
		}

		if (urlExt.contains("&FID") == false) {
			// urlExt += "&FID=" + currWK.getFID();
			urlExt += "&FID=" + this.getFID();
		}

		if (urlExt.contains("&UserNo") == false) {
			urlExt += "&UserNo=" + URLEncoder.encode(WebUser.getNo(), "UTF-8");
		}

		if (urlExt.contains("&SID") == false) {
			urlExt += "&SID=" + WebUser.getSID();
		}

		if (url.contains("?") == true) {
			url += "&" + urlExt;
		} else {
			url += "?" + urlExt;
		}

		Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (DataType.IsNullOrEmpty(key) == true)
				continue;

			if (url.contains(key) == true)
				continue;
			url += "&" + key + "=" + this.GetRequestVal(key);

		}

		url = url.replace("?&", "?");
		url = url.replace("&&", "&");
		return url;
	}

	/// 表单树操作
	/**
	 * 获取表单树数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FlowFormTree_Init() throws Exception {
		bp.wf.template.FlowFormTrees appFlowFormTree = new FlowFormTrees();

		// add root
		bp.wf.template.FlowFormTree root = new bp.wf.template.FlowFormTree();
		root.setNo("1");
		root.setParentNo("0");
		root.setName("目录");
		root.setNodeType("root");
		appFlowFormTree.AddEntity(root);

		/// 添加表单及文件夹

		// 节点表单
		bp.wf.Node nd = new bp.wf.Node(this.getFK_Node());

		FrmNodes frmNodes = new FrmNodes();
		frmNodes.Retrieve(FrmNodeAttr.FK_Node, this.getFK_Node(), FrmNodeAttr.Idx);

		// 文件夹
		// SysFormTrees formTrees = new SysFormTrees();
		// formTrees.RetrieveAll(SysFormTreeAttr.getName());

		// 所有表单集合. 为了优化效率,这部分重置了一下.
		MapDatas mds = new MapDatas();
		if (frmNodes.size() <= 3) {
			for (FrmNode fn : frmNodes.ToJavaList()) {
				MapData md = new MapData(fn.getFK_Frm());
				mds.AddEntity(md);
			}
		} else {
			mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());
		}

		String frms = this.GetRequestVal("Frms");
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (DataType.IsNullOrEmpty(frms) == true) {
			frms = gwf.getParasFrms();
		} else {
			gwf.setParasFrms(frms);
			gwf.Update();
		}

		for (FrmNode frmNode : frmNodes.ToJavaList()) {

			/// 增加判断是否启用规则.
			switch (frmNode.getFrmEnableRole()) {
			case Allways:
				break;
			case WhenHaveData: // 判断是否有数据.
				Object tempVar = mds.GetEntityByKey(frmNode.getFK_Frm());
				MapData md = tempVar instanceof MapData ? (MapData) tempVar : null;
				if (md == null) {
					continue;
				}
				long pk = this.getWorkID();
				switch (frmNode.getWhoIsPK()) {
				case FID:
					pk = this.getFID();
					break;
				case PWorkID:
					pk = this.getPWorkID();
					break;
				case CWorkID:
					pk = this.getCWorkID();
					break;
				case OID:
				default:
					pk = this.getWorkID();
					break;
				}
				if (DBAccess.RunSQLReturnValInt(
						"SELECT COUNT(*) as Num FROM " + md.getPTable() + " WHERE OID=" + pk) == 0) {
					continue;
				}
				break;
			case WhenHaveFrmPara: // 判断是否有参数.

				frms = frms.trim();
				frms = frms.replace(" ", "");
				frms = frms.replace(" ", "");

				if (DataType.IsNullOrEmpty(frms) == true) {
					continue;
					// return "err@当前表单设置为仅有参数的时候启用,但是没有传递来参数.";
				}

				if (frms.contains(",") == false) {
					if (!frmNode.getFK_Frm().equals(frms)) {
						continue;
					}
				}

				if (frms.contains(",") == true) {
					if (frms.contains(frmNode.getFK_Frm() + ",") == false) {
						continue;
					}
				}

				break;
			case ByFrmFields:
				throw new RuntimeException("@这种类型的判断，ByFrmFields 还没有完成。");

			case BySQL: // 按照SQL的方式.
				Object tempVar2 = frmNode.getFrmEnableExp();
				String mysql = tempVar2 instanceof String ? (String) tempVar2 : null;

				if (DataType.IsNullOrEmpty(mysql) == true) {
					MapData FrmMd = new MapData(frmNode.getFK_Frm());
					return "err@表单" + frmNode.getFK_Frm() + ",[" + FrmMd.getName() + "]在节点[" + frmNode.getFK_Node()
							+ "]启用方式按照sql启用但是您没有给他设置sql表达式.";
				}

				mysql = mysql.replace("@OID", String.valueOf(this.getWorkID()));
				mysql = mysql.replace("@WorkID", String.valueOf(this.getWorkID()));

				mysql = mysql.replace("@NodeID", String.valueOf(this.getFK_Node()));
				mysql = mysql.replace("@FK_Node", String.valueOf(this.getFK_Node()));

				mysql = mysql.replace("@FK_Flow", this.getFK_Flow());

				mysql = mysql.replace("@WebUser.No", WebUser.getNo());
				mysql = mysql.replace("@WebUser.Name", WebUser.getName());
				mysql = mysql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				// 替换特殊字符.
				mysql = mysql.replace("~", "'");

				if (DBAccess.RunSQLReturnValFloat(mysql) <= 0) {
					continue;
				}
				break;
			// @袁丽娜
			case ByStation:
				Object tempVar3 = frmNode.getFrmEnableExp();
				String exp = tempVar3 instanceof String ? (String) tempVar3 : null;
				String Sql = "SELECT FK_Station FROM Port_DeptEmpStation where FK_Emp='" + WebUser.getNo() + "'";
				String station = DBAccess.RunSQLReturnString(Sql);
				if (DataType.IsNullOrEmpty(station) == true) {
					continue;
				}
				String[] stations = station.split("[;]", -1);
				boolean isExit = false;
				for (String s : stations) {
					if (exp.contains(s) == true) {
						isExit = true;
						break;
					}
				}
				if (isExit == false) {
					continue;
				}
				break;
			// @袁丽娜
			case ByDept:
				Object tempVar4 = frmNode.getFrmEnableExp();
				exp = tempVar4 instanceof String ? (String) tempVar4 : null;
				Sql = "SELECT FK_Dept FROM Port_DeptEmp where FK_Emp='" + WebUser.getNo() + "'";
				String dept = DBAccess.RunSQLReturnString(Sql);
				if (DataType.IsNullOrEmpty(dept) == true) {
					continue;
				}
				String[] depts = dept.split("[;]", -1);
				isExit = false;
				for (String s : depts) {
					if (exp.contains(s) == true) {
						isExit = true;
						break;
					}
				}
				if (isExit == false) {
					continue;
				}

				break;
			case Disable: // 如果禁用了，就continue出去..
				continue;
			default:
				throw new RuntimeException("@没有判断的规则." + frmNode.getFrmEnableRole());
			}

			///

			/// 检查是否有没有目录的表单?
			boolean isHave = false;
			for (MapData md : mds.ToJavaList()) {
				if (md.getFK_FormTree().equals("")) {
					isHave = true;
					break;
				}
			}

			String treeNo = "0";
			if (isHave && mds.size() == 1) {
				treeNo = "00";
			} else if (isHave == true) {
				for (MapData md : mds.ToJavaList()) {
					if (!md.getFK_FormTree().equals("")) {
						treeNo = md.getFK_FormTree();
						break;
					}
				}
			}

			/// 检查是否有没有目录的表单?

			for (MapData md : mds.ToJavaList()) {
				if (!frmNode.getFK_Frm().equals(md.getNo())) {
					continue;
				}

				if (md.getFK_FormTree().equals("")) {
					md.setFK_FormTree(treeNo);
				}

				// 给他增加目录.
				if (appFlowFormTree.Contains("Name", md.getFK_FormTreeText()) == false) {
					bp.wf.template.FlowFormTree nodeFolder = new bp.wf.template.FlowFormTree();
					nodeFolder.setNo(md.getFK_FormTree());
					nodeFolder.setParentNo("1");
					nodeFolder.setName(md.getFK_FormTreeText());
					nodeFolder.setNodeType("folder");
					appFlowFormTree.AddEntity(nodeFolder);
				}

				// 检查必填项.
				boolean IsNotNull = false;
				FrmFields formFields = new FrmFields();
				QueryObject obj = new QueryObject(formFields);
				obj.AddWhere(FrmFieldAttr.FK_Node, this.getFK_Node());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.FK_MapData, md.getNo());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.IsNotNull, 1);
				obj.DoQuery();
				if (formFields != null && formFields.size() > 0) {
					IsNotNull = true;
				}

				bp.wf.template.FlowFormTree nodeForm = new bp.wf.template.FlowFormTree();
				nodeForm.setNo(md.getNo());
				nodeForm.setParentNo(md.getFK_FormTree());

				// 设置他的表单显示名字. 2019.09.30
				String frmName = md.getName();
				Entity fn = frmNodes.GetEntityByKey(FrmNodeAttr.FK_Frm, md.getNo());
				if (fn != null) {
					String str = fn.GetValStrByKey(FrmNodeAttr.FrmNameShow);
					if (DataType.IsNullOrEmpty(str) == false) {
						frmName = str;
					}
				}
				nodeForm.setName(frmName);
				nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
				nodeForm.setIsEdit(String.valueOf(frmNode.getIsEditInt())); // Convert.ToString(Convert.ToInt32(frmNode.IsEdit));
				nodeForm.setIsCloseEtcFrm(String.valueOf(frmNode.getIsCloseEtcFrmInt()));
				appFlowFormTree.AddEntity(nodeForm);
				break;
			}
		}

		///

		// 扩展工具，显示位置为表单树类型.

		// 增加到数据结构上去.
		TansEntitiesToGenerTree(appFlowFormTree, root.getNo(), "");

		return appendMenus.toString();
	}

	/**
	 * 将实体转为树形
	 * 
	 * @param ens
	 * @param rootNo
	 * @param checkIds
	 */
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();

	public final void TansEntitiesToGenerTree(Entities ens, String rootNo, String checkIds) throws Exception {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityTree root = tempVar instanceof EntityTree ? (EntityTree) tempVar : null;
		if (root == null) {
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");

		// attributes
		bp.wf.template.FlowFormTree formTree = root instanceof bp.wf.template.FlowFormTree
				? (bp.wf.template.FlowFormTree) root : null;
		if (formTree != null) {
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\""
					+ formTree.getIsEdit() + "\",\"IsCloseEtcFrm\":\"" + formTree.getIsCloseEtcFrm() + "\",\"Url\":\""
					+ url + "\"}");
		}
		appendMenus.append(",iconCls:\"icon-Wave\"");
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);

		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	private void AddChildren(EntityTree parentEn, Entities ens, String checkIds) throws Exception {
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);

		appendMenuSb.append("[");
		for (Entity en : ens.ToJavaListEn()) {
			EntityTree item = (EntityTree) en;
			if (item.getParentNo() != parentEn.getNo()) {
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ",")) {
				appendMenuSb.append(
						"{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":true");
			} else {
				appendMenuSb.append(
						"{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":false");
			}

			// attributes
			bp.wf.template.FlowFormTree formTree = item instanceof bp.wf.template.FlowFormTree
					? (bp.wf.template.FlowFormTree) item : null;
			if (formTree != null) {
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				if (SystemConfig.getSysNo().equals("YYT")) {
					ico = "icon-boat_16";
				}
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\""
						+ formTree.getIsEdit() + "\",\"IsCloseEtcFrm\":\"" + formTree.getIsCloseEtcFrm()
						+ "\",\"Url\":\"" + url + "\"}");
				// 图标
				if (formTree.getNodeType().equals("form|0")) {
					ico = "form0";
					if (SystemConfig.getSysNo().equals("YYT")) {
						ico = "icon-Wave";
					}
				}
				if (formTree.getNodeType().equals("form|1")) {
					ico = "form1";
					if (SystemConfig.getSysNo().equals("YYT")) {
						ico = "icon-Shark_20";
					}
				}
				if (formTree.getNodeType().contains("tools")) {
					ico = "icon-4";
					if (SystemConfig.getSysNo().equals("YYT")) {
						ico = "icon-Wave";
					}
				}
				appendMenuSb.append(",iconCls:\"");
				appendMenuSb.append(ico);
				appendMenuSb.append("\"");
			}
			// 增加它的子级.
			appendMenuSb.append(",\"children\":");
			AddChildren(item, ens, checkIds);
			appendMenuSb.append("},");
		}
		if (appendMenuSb.length() > 1) {
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);
	}

	///

	/**
	 * 产生一个工作节点
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String GenerWorkNode() throws Exception {
		String json = "";
		try {
			DataSet ds = new DataSet();

			long workID = this.getWorkID();
			if (this.getCurrND().getHisFormType() == NodeFormType.RefOneFrmTree) {
				// 获取绑定的表单
				FrmNode frmnode = new FrmNode(this.getFK_Node(), this.getCurrND().getNodeFrmID());
				switch (frmnode.getWhoIsPK()) {
				case FID:
					workID = this.getFID();
					break;
				case PWorkID:
					workID = this.getPWorkID();
					break;
				case P2WorkID:
					GenerWorkFlow gwff = new GenerWorkFlow(this.getPWorkID());
					workID = gwff.getPWorkID();
					break;
				case P3WorkID:
					String sqlId = "Select PWorkID From WF_GenerWorkFlow Where WorkID=(Select PWorkID From WF_GenerWorkFlow Where WorkID="
							+ this.getPWorkID() + ")";
					workID = DBAccess.RunSQLReturnValInt(sqlId, 0);
					break;
				default:
					break;
				}

			}

			ds = bp.wf.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getCurrND(), workID, this.getFID(),
					WebUser.getNo(), "1", true,this.getWorkID());

			// Node nd = new Node(this.FK_Node);
			// if (nd.HisFormType == NodeFormType.SheetTree)
			// {
			// /*把树形表单的表单信息加载到ds里面.*/
			// }
			// 把他转化小写,适应多个数据库.
			// wf_generWorkFlowDt = DBAccess.ToLower(wf_generWorkFlowDt);
			// ds.Tables.add(wf_generWorkFlowDt);
			// ds.WriteXml("c:\\xx.xml");

			/// 如果是移动应用就考虑多表单的问题.
			if (getCurrND().getHisFormType() == NodeFormType.SheetTree && this.getIsMobile() == true) {
				/* 如果是表单树并且是，移动模式. */

				FrmNodes fns = new FrmNodes();
				QueryObject qo = new QueryObject(fns);

				qo.AddWhere(FrmNodeAttr.FK_Node, getCurrND().getNodeID());
				qo.addAnd();
				qo.AddWhere(FrmNodeAttr.FrmEnableRole, "!=", FrmEnableRole.Disable.getValue());
				qo.addOrderBy("Idx");
				qo.DoQuery();

				// 把节点与表单的关联管理放入到系统.
				ds.Tables.add(fns.ToDataTableField("FrmNodes"));
			}

			/// 如果是移动应用就考虑多表单的问题.

			if (WebUser.getSysLang().equals("CH") == true) {
				return bp.tools.Json.ToJson(ds);
			}

			/// 处理多语言.
			if (WebUser.getSysLang().equals("CH") == false) {
				Langues langs = new Langues();
				langs.Retrieve(LangueAttr.Model, LangueModel.CCForm, LangueAttr.Sort, "Fields", LangueAttr.Langue,
						WebUser.getSysLang()); // 查询语言.
			}

			/// 处理多语言.

			return bp.tools.Json.ToJson(ds);

		} catch (RuntimeException ex) {
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
			return "err@" + ex.getMessage();
		}
	}
	public final String MyFrm_InitToolBar() throws Exception {
		//打开表单只看轨迹 链接加上Track=1
		// Track为null的异常处理
		boolean isShowTrackBarOnly = false;
		if (!DataType.IsNullOrEmpty(this.GetRequestVal("Track")))
			isShowTrackBarOnly= this.GetRequestVal("Track").toString().equals("1")?true:false;

		DataTable dt = new DataTable("ToolBar");
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("Oper");

		BtnLab btnLab = new BtnLab(this.getFK_Node());
		try {
			DataRow dr = dt.NewRow();
			if(isShowTrackBarOnly==false) {
				dr.setValue("No", "Close");
				dr.setValue("Name", "关闭");
				dr.setValue("Oper", "Close();");
				dt.Rows.add(dr);
			}

			dr = dt.NewRow();
			dr.setValue("No", "Track");
			dr.setValue("Name", "轨迹");
			dr.setValue("Oper", "");
			dt.Rows.add(dr);

			/// 根据流程权限控制规则获取可以操作的按钮功能

			/// 加载流程查看器 - 按钮

			/* 打包下载zip */
			if (btnLab.getPrintZipMyView() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_zip");
				dr.setValue("Name", btnLab.getPrintZipLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlMyView() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_html");
				dr.setValue("Name", btnLab.getPrintHtmlLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}


			/* 打包下载pdf */
			if (btnLab.getPrintPDFMyView() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_pdf");
				dr.setValue("Name", btnLab.getPrintPDFLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}
			if (btnLab.getPrintDocMyView() == true) {
				String urlr = "./WorkOpt/PrintDoc.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

				dr = dt.NewRow();
				dr.setValue("No", "PrintDoc");
				dr.setValue("Name", btnLab.getPrintDocLab());
				dr.setValue("Oper", "WinOpen('" + urlr + "','dsdd');");
				dt.Rows.add(dr);

			}
			/* 公文标签 */
			if (btnLab.getOfficeBtnEnable() == true && btnLab.getOfficeBtnLocal()==0) {
				dr = dt.NewRow();
				dr.setValue("No", "DocWord");
				dr.setValue("Name", btnLab.getOfficeBtnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

		} catch (RuntimeException ex) {
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return bp.tools.Json.ToJson(dt);
	}
}