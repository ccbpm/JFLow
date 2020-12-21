package bp.wf.httphandler;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.StringHelper;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import bp.wf.*;
import java.util.*;
import java.net.URLDecoder;
import java.time.*;

/**
 * 流程处理类
 */
public class WF_MyFlow extends WebContralBase {

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

	private long _workID = 0;

	public final void setWorkID(long value) {
		_workID = value;
	}

	@Override
	public final long getWorkID() {
		if (_workID != 0) {
			return _workID;
		}

		String str = this.GetRequestVal("WorkID");
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}
		return Integer.parseInt(str);
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

	public final void setCurrND(Node value) {
		_currNode = value;
	}

	private Flow _currFlow = null;

	public final Flow getCurrFlow() throws Exception {
		if (_currFlow == null) {
			_currFlow = new Flow(this.getFK_Flow());
		}
		return _currFlow;
	}

	public final void setCurrFlow(Flow value) {
		_currFlow = value;
	}

	/**
	 * 定义跟路径
	 */
	public String appPath = "/";

	public final String Focus() throws Exception {
		bp.wf.Dev2Interface.Flow_Focus(this.getWorkID());
		return "设置成功.";
	}

	/**
	 * 确认
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Confirm() throws Exception {
		bp.wf.Dev2Interface.Flow_Confirm(this.getWorkID());
		return "设置成功.";
	}

	/**
	 * 删除子流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DelSubFlow() throws Exception {
		bp.wf.Dev2Interface.Flow_DeleteSubThread(this.getWorkID(), "手工删除");
		return "删除成功.";
	}

	/**
	 * 加载前置导航数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String StartGuide_Init() throws Exception {
		String josnData = "";
		// 流程编号
		String fk_flow = this.GetRequestVal("FK_Flow");
		// 查询的关键字
		String skey = this.GetRequestVal("Keys");
		try {
			// 获取流程实例
			Flow fl = new Flow(fk_flow);
			// 获取设置的前置导航的sql
			Object tempVar = fl.getStartGuidePara2();
			String sql = tempVar instanceof String ? (String) tempVar : null;
			// 判断是否有查询条件
			if (!DataType.IsNullOrEmpty(skey)) {
				Object tempVar2 = fl.getStartGuidePara1();
				sql = tempVar2 instanceof String ? (String) tempVar2 : null;
				sql = sql.replace("@Key", skey);
			}
			sql = sql.replace("~", "'");
			// 替换约定参数
			sql = sql.replace("@WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

			if (sql.contains("@") == true) {
				Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
				while (enu.hasMoreElements()) {
					String key = (String) enu.nextElement();
					if (DataType.IsNullOrEmpty(key) == false && sql.contains(key) == true) {
						sql = sql.replace("@" + key, this.GetRequestVal(key));
					}
				}

			}

			// 获取数据
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			// 判断前置导航的类型
			switch (fl.getStartGuideWay()) {
			case BySQLOne:
			case BySystemUrlOneEntity:
				josnData = bp.tools.Json.ToJson(dt);
				break;
			case BySQLMulti:
				josnData = bp.tools.Json.ToJson(dt);
				break;
			default:
				break;
			}
			return josnData;
		} catch (RuntimeException ex) {
			return "err@:" + ex.getMessage().toString();
		}
	}

	/**
	 * 没有WorkID
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyFlow_Init_NoWorkID() throws Exception {
		String isStartSameLevelFlow = this.GetRequestVal("IsStartSameLevelFlow");

		/// 判断是否可以否发起流程.
		try {
			if (bp.wf.Dev2Interface.Flow_IsCanStartThisFlow(this.getFK_Flow(), WebUser.getNo(), this.getPFlowNo(),
					this.getPNodeID(), this.getPWorkID()) == false) {
				/* 是否可以发起流程？ */
				throw new RuntimeException("err@您(" + WebUser.getNo() + ")没有发起或者处理该流程的权限.");
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException("err@" + ex.getMessage());
		}

		/* 如果是开始节点, 先检查是否启用了流程限制。 */
		if (bp.wf.Glo.CheckIsCanStartFlow_InitStartFlow(this.getCurrFlow()) == false) {
			/* 如果启用了限制就把信息提示出来. */
			String msg = bp.wf.Glo.DealExp(this.getCurrFlow().getStartLimitAlert(), null, null);
			return "err@" + msg;
		}

		/// 判断是否可以否发起流程

		/// 判断前置导航.

		// 生成workid.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), null, null, WebUser.getNo(), null,
				this.getPWorkID(), this.getPFID(), this.getPFlowNo(), this.getPNodeID(), null, 0, null, null,
				isStartSameLevelFlow);

		String hostRun = this.getCurrFlow().GetValStrByKey(FlowAttr.HostRun);
		if (DataType.IsNullOrEmpty(hostRun) == false) {
			hostRun += "/WF/";
		}

		this.setWorkID(workid); // 给workid赋值.

		switch (this.getCurrFlow().getStartGuideWay()) {
		case None:
			break;
		case SubFlowGuide:
		case SubFlowGuideEntity:
			return "url@" + hostRun + "StartGuide.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
		case ByHistoryUrl: // 历史数据.
			if (this.getCurrFlow().getIsLoadPriData() == true) {
				return "err@流程配置错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。";
			}
			return "url@" + hostRun + "StartGuide.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
		case BySystemUrlOneEntity:
			return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySystemUrlOneEntity&WorkID=" + workid
					+ this.getRequestParasOfAll();
		case BySQLOne:
			return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySQLOne&WorkID=" + workid
					+ this.getRequestParasOfAll();
		case BySQLMulti:
			return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySQLMulti&WorkID=" + workid
					+ this.getRequestParasOfAll();
		case BySelfUrl: // 按照定义的url.
			return "url@" + this.getCurrFlow().getStartGuidePara1() + this.getRequestParasOfAll() + "&WorkID=" + workid;
		case ByFrms: // 选择表单.
			return "url@" + hostRun + "./WorkOpt/StartGuideFrms.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID="
					+ workid;
		case ByParentFlowModel: // 选择父流程 @yuanlina.
			return "url@" + hostRun + "./WorkOpt/StartGuideParentFlowModel.htm?FK_Flow=" + this.getCurrFlow().getNo()
					+ "&WorkID=" + workid;
		default:
			break;
		}

		/// 判断前置导航

		return null; // 生成了workid.
	}

	/**
	 * 初始化(处理分发)
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyFlow_Init() throws Exception {
		if (this.getWorkID() == 0) {
			String val = MyFlow_Init_NoWorkID();
			if (val != null) {
				return val;
			}
		}

		// 定义变量.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		if (gwf.RetrieveFromDBSources() == 0) {
			return ("err@该流程ID{" + this.getWorkID() + "}不存在，或者已经被删除.");
		}

		// 手动启动子流程的标志 0父子流程 1 同级子流程
		String isStartSameLevelFlow = this.GetRequestVal("IsStartSameLevelFlow");

		/// 做权限判断.
		 //授权人
        String auther = this.GetRequestVal("Auther");
        if (DataType.IsNullOrEmpty(auther) == false)
        {
          //  BP.Web.WebUser.IsAuthorize = true;
            WebUser.setAuth(auther);
            WebUser.setAuthName( DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'"));
        }
        else
        {
           // BP.Web.WebUser.IsAuthorize = true;
            WebUser.setAuth("");
            WebUser.setAuthName(""); // BP.DA.DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'");
        }
        
		// 判断是否有执行该工作的权限.
		String todEmps = ";" + gwf.getTodoEmps();
		boolean isCanDo = false;
		if (String.valueOf(gwf.getFK_Node()).endsWith("01") == true) {
			isCanDo = true; // 开始节点不判断权限.
		} else {
			isCanDo = todEmps.contains(";" + WebUser.getNo() + ",");
			if (isCanDo == false) {
				isCanDo = Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());
			}
		}

		if (isCanDo == false) {
			return "err@您[" + WebUser.getNo() + "," + WebUser.getName() + "]不能执行当前工作, 当前工作已经运转到[" + gwf.getNodeName()
					+ "],处理人[" + gwf.getTodoEmps() + "]。";
		}

		String frms = this.GetRequestVal("Frms");
		if (DataType.IsNullOrEmpty(frms) == false) {
			gwf.setParasFrms(frms);
			gwf.Update();
		}

		/// 做权限判断.

		/// 处理打开既阅读.
		// 判断当前节点是否是打开即阅读
		// 获取当前节点信息
		this.setCurrND(new Node(gwf.getFK_Node()));
		if (this.getCurrND().getIsOpenOver() == true) {
			// 如果是结束节点执行流程结束功能
			if (this.getCurrND().getIsStartNode() == false) {
				// 如果启用审核组件
				if (this.getCurrND().getFrmWorkCheckSta() == FrmWorkCheckSta.Enable) {
					// 判断一下审核意见是否有默认值
					NodeWorkCheck workCheck = new NodeWorkCheck("ND" + this.getCurrND().getNodeID());
					String msg = bp.wf.Glo.getDefValWFNodeFWCDefInfo(); // 设置默认值;
					if (workCheck.getFWCIsFullInfo() == true) {
						msg = workCheck.getFWCDefInfo();
					}
					bp.wf.Dev2Interface.WriteTrackWorkCheck(gwf.getFK_Flow(), this.getCurrND().getNodeID(),
							gwf.getWorkID(), gwf.getFID(), msg, workCheck.getFWCOpLabel());
				}

				bp.wf.Dev2Interface.Node_SendWork(gwf.getFK_Flow(), gwf.getWorkID());
				return "url@" + "./MyView.htm?WorkID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "&FK_Node="
						+ gwf.getFK_Node() + "&PWorkID=" + gwf.getPWorkID() + "&FID=" + gwf.getFID();
			}
		}

		/// 处理打开既阅读.

		/// 前置导航数据拷贝到第一节点
		if (this.GetRequestVal("IsCheckGuide") != null) {
			String key = this.GetRequestVal("KeyNo");
			DataTable dt = bp.wf.Glo.StartGuidEnties(this.getWorkID(), this.getFK_Flow(), this.getFK_Node(), key);

			/* 如果父流程编号，就要设置父子关系。 */
			if (dt != null && dt.Rows.size() > 0 && dt.Columns.contains("PFlowNo") == true) {
				String pFlowNo = dt.Rows.get(0).getValue("PFlowNo").toString();
				int pNodeID = Integer.parseInt(dt.Rows.get(0).getValue("PNodeID").toString());
				long pWorkID = Long.parseLong(dt.Rows.get(0).getValue("PWorkID").toString());
				String pEmp = ""; // dt.Rows.get(0).getValue("PEmp"].ToString();
				if (DataType.IsNullOrEmpty(pEmp)) {
					pEmp = WebUser.getNo();
				}

				// 设置父子关系.
				bp.wf.Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), pWorkID);
			}
		}

		///

		/// 启动同级子流程的信息存储
		if (isStartSameLevelFlow != null && isStartSameLevelFlow.equals("1") == true) {
			String slFlowNo = GetRequestVal("SLFlowNo");
			int slNode = GetRequestValInt("SLNodeID");
			long slWorkID = GetRequestValInt("SLWorkID");
			gwf.SetPara("SLFlowNo", slFlowNo);
			gwf.SetPara("SLNodeID", slNode);
			gwf.SetPara("SLWorkID", slWorkID);
			gwf.SetPara("SLEmp", WebUser.getNo());
			gwf.Update();
		}

		/// 启动同级子流程的信息存储

		if (this.getCurrND().getIsStartNode()) {
			/* 如果是开始节点, 先检查是否启用了流程限制。 */
			if (bp.wf.Glo.CheckIsCanStartFlow_InitStartFlow(this.getCurrFlow()) == false) {
				/* 如果启用了限制就把信息提示出来. */
				String msg = bp.wf.Glo.DealExp(this.getCurrFlow().getStartLimitAlert(), null, null);
				return "err@" + msg;
			}
		}

		/// 处理表单类型.
		if (this.getCurrND().getHisFormType() == NodeFormType.SheetTree
				|| this.getCurrND().getHisFormType() == NodeFormType.SheetAutoTree) {

			/// 开始组合url.
			String toUrl = "";
			if (this.getIsMobile() == true) {
				if (gwf.getParasFrms().equals("") == false) {
					toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID()
							+ "&Frms=" + gwf.getParasFrms();
				} else {
					toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
				}
			} else {
				if (gwf.getParasFrms().equals("") == false) {
					toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
							+ WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo="
							+ gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID()
							+ "&Frms=" + gwf.getParasFrms();
				} else {
					toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo="
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

			// 如果是开始节点.
			if (getCurrND().getIsStartNode() == true) {
				if (toUrl.contains("PrjNo") == true && toUrl.contains("PrjName") == true) {
					String sql = "UPDATE " + this.getCurrFlow().getPTable() + " SET PrjNo='"
							+ this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName")
							+ "' WHERE OID=" + this.getWorkID();
					DBAccess.RunSQL(sql);
				}
			}
			return "url@" + toUrl;
		}

		if (this.getCurrND().getHisFormType() == NodeFormType.SDKForm) {
			String url = getCurrND().getFormUrl();
			if (DataType.IsNullOrEmpty(url)) {
				return "err@设置读取状流程设计错误态错误,没有设置表单url.";
			}

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);

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

		if (frmtype == NodeFormType.FoolTruck) {
			String url = "MyFlowGener.htm";

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);
			return "url@" + url;
		}

		if (frmtype == NodeFormType.FoolForm && this.getIsMobile() == false) {
			/* 如果是傻瓜表单，就转到傻瓜表单的解析执行器上。 */
			String url = "MyFlowGener.htm";
			if (this.getIsMobile()) {
				url = "MyFlowGener.htm";
			}

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

		// 自定义表单
		if (frmtype == NodeFormType.SelfForm && this.getIsMobile() == false) {
			String url = "MyFlowSelfForm.htm";

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

		/// 内置表单类型的判断.

		String myurl = "MyFlowGener.htm";

		// 处理连接.
		myurl = this.MyFlow_Init_DealUrl(getCurrND(), myurl);
		myurl = myurl.replace("DoType=MyFlow_Init&", "");
		myurl = myurl.replace("&DoWhat=StartClassic", "");

		return "url@" + myurl;
	}

	private String MyFlow_Init_DealUrl(bp.wf.Node currND) throws Exception {
		return MyFlow_Init_DealUrl(currND, null);
	}

	private String MyFlow_Init_DealUrl(bp.wf.Node currND, String url) throws Exception {
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
			urlExt += "&UserNo=" + URLDecoder.decode(WebUser.getNo(), "UTF-8");
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
			String str = (String) enu.nextElement();
			if (DataType.IsNullOrEmpty(str) == true) {
				continue;
			}
			if (url.contains(str + "=") == true) {
				continue;
			}
			url += "&" + str + "=" + this.GetRequestVal(str);

		}

		url = url.replace("?&", "?");
		url = url.replace("&&", "&");
		return url;
	}

	/**
	 * 构造函数
	 */
	public WF_MyFlow() {

	}

	/**
	 * 结束流程.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyFlow_StopFlow() throws Exception {
		try {
			String str = bp.wf.Dev2Interface.Flow_DoFlowOver(this.getWorkID(), "流程成功结束");
			if (DataType.IsNullOrEmpty(str) == true) {
				return "流程成功结束";
			}
			return str;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 删除流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyFlow_DeleteFlowByReal() throws Exception {
		try {
			String str = bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(this.getWorkID());
			if (DataType.IsNullOrEmpty(str) == true) {
				return "流程删除成功";
			}
			return str;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 保存发送参数.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String SaveParas() throws Exception {
		bp.wf.Dev2Interface.Flow_SaveParas(this.getWorkID(), this.GetRequestVal("Paras"));
		return "保存成功";
	}

	/**
	 * 初始化toolbar.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String InitToolBar() throws Exception {
		DataSet ds = new DataSet();
		// 创建一个DataTable，返回按钮信息
		DataTable dt = new DataTable("ToolBar");
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("Oper");
		dt.Columns.Add("Role", Integer.class);
		dt.Columns.Add("Icon");

		/// 处理是否是加签，或者是否是会签模式.
		boolean isAskForOrHuiQian = false;
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		Node nd = new Node(this.getFK_Node());
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (String.valueOf(this.getFK_Node()).endsWith("01") == false) {
			if (gwf.getWFState() == WFState.Askfor) {
				isAskForOrHuiQian = true;
			}

			/* 判断是否是加签状态，如果是，就判断是否是主持人，如果不是主持人，就让其 isAskFor=true ,屏蔽退回等按钮. */
			/** 说明：针对于组长模式的会签，协作模式的会签加签人仍可以加签 */
			if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing) {
				// 初次打开会签节点时
				if (DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true) {
					if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false) {
						isAskForOrHuiQian = true;
					}
				}

				// 执行会签后的状态
				if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader
						&& btnLab.getHuiQianLeaderRole() == HuiQianLeaderRole.OnlyOne) {
					if (!gwf.getHuiQianZhuChiRen().equals(WebUser.getNo())
							&& gwf.GetParaString("AddLeader").contains(WebUser.getNo() + ",") == false) {
						isAskForOrHuiQian = true;
					}
				} else {
					if (gwf.getHuiQianZhuChiRen().contains(WebUser.getNo() + ",") == false
							&& gwf.GetParaString("AddLeader").contains(WebUser.getNo() + ",") == false) {
						isAskForOrHuiQian = true;
					}
				}

			}
		}

		/// 处理是否是加签，或者是否是会签模式，.

		DataRow dr = dt.NewRow();

		String toolbar = "";
		try {

			/// 是否是会签？.
			if (isAskForOrHuiQian == true && SystemConfig.getCustomerNo().equals("LIMS")) {
				return "";
			}

			if (isAskForOrHuiQian == true) {
				dr.setValue("No", "Send");
				dr.setValue("Name", "确定/完成");
				dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send(false, "
						+ nd.getFormType().getValue() + "); ");
				dt.Rows.add(dr);
				if (btnLab.getPrintZipEnable() == true) {
					dr = dt.NewRow();
					dr.setValue("No", "PackUp");
					dr.setValue("Name", btnLab.getPrintZipLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				if (btnLab.getTrackEnable()) {
					dr = dt.NewRow();
					dr.setValue("No", "Track");
					dr.setValue("Name", btnLab.getTrackLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				return bp.tools.Json.ToJson(dt);
			}

			/// 是否是会签.

			/// 是否是抄送.
			if (this.getIsCC()) {
				dr = dt.NewRow();
				dr.setValue("No", "Track");
				dr.setValue("Name", "流程运行轨迹");
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

				// 判断审核组件在当前的表单中是否启用，如果启用了.
				NodeWorkCheck fwc = new NodeWorkCheck(this.getFK_Node());
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable) {
					dr = dt.NewRow();
					/* 如果不等于启用, */
					dr.setValue("No", "CCWorkCheck");
					dr.setValue("Name", "填写审核意见");
					dr.setValue("Oper", "");
					dt.Rows.add(dr);

					// toolbar += "<input type=button value='填写审核意见' enable=true
					// onclick=\"WinOpen('" + appPath +
					// "WF/WorkOpt/CCCheckNote.htm?WorkID=" + this.WorkID +
					// "&FK_Flow=" + this.FK_Flow + "&FID=" + this.getFID() +
					// "&FK_Node=" + this.FK_Node + "&s=" + tKey + "','ds'); \"
					// />";
				}
				return toolbar;
			}

			/// 是否是抄送.

			/// 如果当前节点启用了协作会签.
			if (btnLab.getHuiQianRole() == HuiQianRole.Teamup) {
				dr = dt.NewRow();
				dr.setValue("No", "SendHuiQian");
				dr.setValue("Name", "会签发送");
				dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send(true, "
						+ nd.getFormType().getValue() + ");");
				dt.Rows.add(dr);

			}

			/// 如果当前节点启用了协作会签

			/// 加载流程控制器 - 按钮
			if (this.getCurrND().getHisFormType() == NodeFormType.SelfForm) {
				/* 如果是嵌入式表单. */
				if (getCurrND().getIsEndNode()) {
					/* 如果当前节点是结束节点. */
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						dr = dt.NewRow();
						/* 如果启用了发送按钮. */
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper", btnLab.getSendJS() + " if (SysCheckFrm()==false) return false;Send(false, "
								+ nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				} else {
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if ( SysCheckFrm()==false) return false; Send(false, "
										+ nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				}

				/* 处理保存按钮. */
				if (btnLab.getSaveEnable()) {
					dr = dt.NewRow();
					dr.setValue("No", "Save");
					dr.setValue("Name", btnLab.getSaveLab());
					dr.setValue("Oper", "if(SysCheckFrm()==false) return false;SaveOnly();SaveEnd("
							+ nd.getFormType().getValue() + ");");
					dt.Rows.add(dr);
				}
			}

			//根据会签子流程的情况，设置显示按钮的数据
			boolean  isShowSend = true;
			boolean  isShowSubThread = false;
			// 发起会签子流程
			if (nd.getIsSendDraftSubFlow() == true) {
				//看有没有发起的会签子流程
				GenerWorkFlows gwfs = new GenerWorkFlows();
				gwfs.Retrieve(GenerWorkFlowAttr.PWorkID,this.getWorkID(),GenerWorkFlowAttr.PFlowNo,this.getFK_Flow(),GenerWorkFlowAttr.WFState);
				if(gwfs.size()==0){
					isShowSend = true;
					isShowSubThread = true;
				}
				else{
					GenerWorkFlow subGwf = (GenerWorkFlow)gwfs.get(0);
					if(subGwf.getWFState() == WFState.Draft){
						isShowSend = true;
						isShowSubThread = true;
					}else if(subGwf.getWFState() == WFState.Complete){
						isShowSend = true;
						isShowSubThread = false;
					}else{
						isShowSend = true;
						isShowSubThread = false;
					}
				}
			}
			if(isShowSend==false && isShowSubThread==false){
				if (btnLab.getTrackEnable())
				{
					dr = dt.NewRow();
					dr.setValue("No", "Track");
					dr.setValue("Name", btnLab.getTrackLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);

				}
				bp.wf.template.NodeToolbars bars = new NodeToolbars();
				bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node(), NodeToolbarAttr.IsMyFlow, 1, NodeToolbarAttr.Idx);
				for (NodeToolbar bar : bars.ToJavaList())
				{


					if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) == false && bar.getTarget().toLowerCase().equals("javascript")))
					{
						dr = dt.NewRow();
						dr.setValue("No", "NodeToolBar");
						dr.setValue("Name", bar.getTitle());
						dr.setValue("Oper", bar.getUrl());
						//判断按钮图片路径是否有值
						String IconPath = bar.getIconPath();
						if (DataType.IsNullOrEmpty(IconPath))
						{
							dr.setValue("Icon", bar.getRow().GetValByKey("WebPath"));
						}
						else
						{
							dr.setValue("Icon", IconPath);
						}
						dt.Rows.add(dr);
					}
					else
					{
						String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

						dr = dt.NewRow();
						dr.setValue("No", "NodeToolBar");
						dr.setValue("Name", bar.getTitle());
						dr.setValue("Oper", "WinOpen('" + urlr3 + "')");
						//判断按钮图片路径是否有值
						String IconPath = bar.getIconPath();
						if (DataType.IsNullOrEmpty(IconPath))
						{
							dr.setValue("Icon", bar.getRow().GetValByKey("WebPath"));
						}
						else
						{
							dr.setValue("Icon", IconPath);
						}
						dt.Rows.add(dr);

					}
				}
				ds.Tables.add(dt);
				return bp.tools.Json.ToJson(ds);
			}

			if (this.getCurrND().getHisFormType() != NodeFormType.SelfForm && isShowSend==true) {
				/* 启用了其他的表单. */
				if (getCurrND().getIsEndNode()) {
					/* 如果当前节点是结束节点. */
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						/* 如果启用了选择人窗口的模式是【选择既发送】. */
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(false, "
										+ nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);

					}
				} else {
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						/*
						 * 如果启用了发送按钮. 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
						 */
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(false, "
										+ nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				}

				/* 处理保存按钮. */
				if (btnLab.getSaveEnable()) {
					dr = dt.NewRow();
					dr.setValue("No", "Save");
					dr.setValue("Name", btnLab.getSaveLab());
					dr.setValue("Oper", "if (SysCheckFrm() == false) return false; SaveOnly();SaveEnd("
							+ nd.getFormType().getValue() + "); ");
					dt.Rows.add(dr);
				}
			}


			if (btnLab.getWorkCheckEnable()) {
				dr = dt.NewRow();
				dr.setValue("No", "workcheckBtn");
				dr.setValue("Name", btnLab.getWorkCheckLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr); // 审核
			}

			if(isShowSubThread == true){
				dr = dt.NewRow();
				dr.setValue("No","StartThread");
				dr.setValue("Name","发起子流程");
				dr.setValue("Oper","StartThread()");
				dt.Rows.add(dr);/*发起会签子流程*/
			}


			if (btnLab.getThreadEnable()) {
				/* 如果要查看子线程. */
				dr = dt.NewRow();
				dr.setValue("No", "Thread");
				dr.setValue("Name", btnLab.getThreadLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getShowParentFormEnable() && this.getPWorkID() != 0) {
				/* 如果要查看父流程. */
				dr = dt.NewRow();
				dr.setValue("No", "ParentForm");
				dr.setValue("Name", btnLab.getShowParentFormLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getTCEnable() == true) {
				/* 流转自定义.. */
				dr = dt.NewRow();
				dr.setValue("No", "TransferCustom");
				dr.setValue("Name", btnLab.getTCLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHelpRole() != 0) {
				dr = dt.NewRow();
				dr.setValue("No", "Help");
				dr.setValue("Name", btnLab.getHelpLab());
				dr.setValue("Oper", "HelpAlter()");
				dr.setValue("Role", btnLab.getHelpRole());
				dt.Rows.add(dr);
			}

			if (btnLab.getJumpWayEnable() && 1 == 2) {
				/* 跳转 */
				dr = dt.NewRow();
				dr.setValue("No", "JumpWay");
				dr.setValue("Name", btnLab.getJumpWayLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}
			//启用退回按钮，如果是子流程的开始节点可以退回，其他的流程的开始节点不可以退回
			if (btnLab.getReturnEnable()&&(nd.getIsStartNode()==false ||(nd.getIsStartNode()==true && gwf.getPWorkID()!=0))){
				/* 退回 */
				dr = dt.NewRow();
				dr.setValue("No", "Return");
				dr.setValue("Name", btnLab.getReturnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHungEnable()) {
				/* 挂起 */
				dr = dt.NewRow();
				dr.setValue("No", "Hung");
				dr.setValue("Name", btnLab.getHungLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getShiftEnable()) {
				/* 移交 */
				dr = dt.NewRow();
				dr.setValue("No", "Shift");
				dr.setValue("Name", btnLab.getShiftLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto)) {

				// 抄送
				dr = dt.NewRow();
				dr.setValue("No", "CC");
				dr.setValue("Name", btnLab.getCCLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getDeleteEnable() != 0) {
				dr = dt.NewRow();
				dr.setValue("No", "Delete");
				dr.setValue("Name", btnLab.getDeleteLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getEndFlowEnable() && this.getCurrND().getIsStartNode() == false) {
				dr = dt.NewRow();
				dr.setValue("No", "EndFlow");
				dr.setValue("Name", btnLab.getEndFlowLab());
				dr.setValue("Oper", "DoStop('" + btnLab.getEndFlowLab() + "','" + this.getFK_Flow() + "','"
						+ this.getWorkID() + "')");
				dt.Rows.add(dr);

			}

			// @李国文.
			if (btnLab.getPrintDocEnable() == true) {
				String urlr =  "./WorkOpt/PrintDoc.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

				dr = dt.NewRow();
				dr.setValue("No", "PrintDoc");
				dr.setValue("Name", btnLab.getPrintDocLab());
				dr.setValue("Oper", "WinOpen('" + urlr + "','dsdd');");
				dt.Rows.add(dr);

			}

			if (btnLab.getTrackEnable()) {
				dr = dt.NewRow();
				dr.setValue("No", "Track");
				dr.setValue("Name", btnLab.getTrackLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getSearchEnable()) {
				dr = dt.NewRow();
				dr.setValue("No", "Search");
				dr.setValue("Name", btnLab.getSearchLab());
				dr.setValue("Oper", "WinOpen('./RptDfine/Default.htm?RptNo=ND" + Integer.parseInt(this.getFK_Flow())
						+ "MyRpt&FK_Flow=" + this.getFK_Flow() + "&SearchType=My)");
				dt.Rows.add(dr);
			}

			if (btnLab.getBatchEnable()) {
				String urlr = appPath + "WF/Batch.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

				/* 批量处理 */
				dr = dt.NewRow();
				dr.setValue("No", "Batch");
				dr.setValue("Name", btnLab.getBatchLab());
				dr.setValue("Oper", "To('" + urlr + "');");
				dt.Rows.add(dr);

			}

			if (btnLab.getAskforEnable()) {
				/* 加签 */
				dr = dt.NewRow();
				dr.setValue("No", "Askfor");
				dr.setValue("Name", btnLab.getAskforLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader) {
				/* 会签 */
				dr = dt.NewRow();
				dr.setValue("No", "HuiQian");
				dr.setValue("Name", btnLab.getHuiQianLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			// 原始会签主持人可以增加组长
			if (btnLab.getHuiQianRole() != HuiQianRole.None && btnLab.getAddLeaderEnable() == true) {
				/* 增加组长 */
				dr = dt.NewRow();
				dr.setValue("No", "AddLeader");
				dr.setValue("Name", btnLab.getAddLeaderLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.Button) {
				/* 公文正文 */
				dr = dt.NewRow();
				dr.setValue("No", "WebOffice");
				dr.setValue("Name", btnLab.getWebOfficeLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			// 需要翻译.
			if (this.getCurrFlow().getIsResetData() == true && this.getCurrND().getIsStartNode()) {
				/* 启用了数据重置功能 */
				dr = dt.NewRow();
				dr.setValue("No", "ReSet");
				dr.setValue("Name", "数据重置");
				dr.setValue("Oper", "resetData();");
				dt.Rows.add(dr);
			}

			if (btnLab.getCHRole() != 0) {
				/* 节点时限设置 */
				dr = dt.NewRow();
				dr.setValue("No", "CH");
				dr.setValue("Name", btnLab.getCHLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getNoteEnable() != 0) {
				/* 备注设置 */
				dr = dt.NewRow();
				dr.setValue("No", "Note");
				dr.setValue("Name", btnLab.getNoteLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getPRIEnable() != 0) {
				/* 优先级设置 */
				dr = dt.NewRow();
				dr.setValue("No", "PR");
				dr.setValue("Name", btnLab.getPRILab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 关注 */
			if (btnLab.getFocusEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "Focus");
				if (getHisGenerWorkFlow().getParasFocus() == true) {
					dr.setValue("Name", "取消关注");
				} else {
					dr.setValue("Name", btnLab.getFocusLab());
				}
				dr.setValue("Oper", "FocusBtn(this,'" + this.getWorkID() + "');");
				dt.Rows.add(dr);
			}

			/* 分配工作 */
			if (btnLab.getAllotEnable() == true) {
				/* 分配工作 */
				dr = dt.NewRow();
				dr.setValue("No", "Allot");
				dr.setValue("Name", btnLab.getAllotLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 确认 */
			if (btnLab.getConfirmEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "Confirm");
				if (getHisGenerWorkFlow().getParasConfirm() == true) {
					dr.setValue("Name", "取消确认");
				} else {
					dr.setValue("Name", btnLab.getConfirmLab());
				}

				dr.setValue("Oper", "ConfirmBtn(this,'" + this.getWorkID() + "');");
				dt.Rows.add(dr);
			}

			// 需要翻译.

			/* 打包下载zip */
			if (btnLab.getPrintZipEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_zip");
				dr.setValue("Name", btnLab.getPrintZipLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_html");
				dr.setValue("Name", btnLab.getPrintHtmlLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载pdf */
			if (btnLab.getPrintPDFEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_pdf");
				dr.setValue("Name", btnLab.getPrintPDFLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (this.getCurrND().getIsStartNode() == true) {
				if (this.getCurrFlow().getIsDBTemplate() == true) {
					dr = dt.NewRow();
					dr.setValue("No", "DBTemplate");
					dr.setValue("Name", "模版");
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}
			}

			/* 公文标签 */
			if (btnLab.getOfficeBtnEnable() == true && btnLab.getOfficeBtnLocal() == 0) {
				dr = dt.NewRow();
				dr.setValue("No", "DocWord");
				dr.setValue("Name", btnLab.getOfficeBtnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			///

			/// 加载自定义的button.
			bp.wf.template.NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node(), NodeToolbarAttr.IsMyFlow, 1, NodeToolbarAttr.Idx);
			for (NodeToolbar bar : bars.ToJavaList()) {

				if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) == false
						&& bar.getTarget().toLowerCase().equals("javascript"))) {
					dr = dt.NewRow();
					dr.setValue("No", "NodeToolBar");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", bar.getUrl());
					// 判断按钮图片路径是否有值
					String IconPath = bar.getIconPath();
					if (DataType.IsNullOrEmpty(IconPath)) {
						dr.setValue("Icon", bar.getRow().GetValByKey("WebPath"));
					} else {
						dr.setValue("Icon", IconPath);
					}
					dt.Rows.add(dr);
				} else {
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
							+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

					dr = dt.NewRow();
					dr.setValue("No", "NodeToolBar");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", "WinOpen('" + urlr3 + "')");
					// 判断按钮图片路径是否有值
					String IconPath = bar.getIconPath();
					if (DataType.IsNullOrEmpty(IconPath)) {
						dr.setValue("Icon", bar.getRow().GetValByKey("WebPath"));
					} else {
						dr.setValue("Icon", IconPath);
					}
					dt.Rows.add(dr);

				}
			}
			ds.Tables.add(dt);

			/// //加载自定义的button.

			/// 增加按钮旁的下拉框

			// 增加转向下拉框数据.
			if (nd.getCondModel() == DirCondModel.SendButtonSileSelect) {
				if (nd.getIsStartNode() == true || (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == true)&& isShowSend==true) {
					/* 如果当前不是主持人,如果不是主持人，就不让他显示下拉框了. */

					/* 如果当前节点，是可以显示下拉框的. */
					// Nodes nds = nd.HisToNodes;

					bp.wf.template.NodeSimples nds = nd.getHisToNodeSimples();

					DataTable dtToNDs = new DataTable("ToNodes");
					dtToNDs.Columns.Add("No", String.class); // 节点ID.
					dtToNDs.Columns.Add("Name", String.class); // 到达的节点名称.
					dtToNDs.Columns.Add("IsSelectEmps", String.class); // 是否弹出选择人的对话框？
					dtToNDs.Columns.Add("IsSelected", String.class); // 是否选择？
					dtToNDs.Columns.Add("DeliveryParas", String.class); // 自定义URL

					/// 增加到达延续子流程节点。
					if (nd.getSubFlowYanXuNum() >= 0) {
						SubFlowYanXus ygflows = new SubFlowYanXus(this.getFK_Node());
						for (SubFlowYanXu item : ygflows.ToJavaList()) {
							dr = dtToNDs.NewRow();
							dr.setValue("No", item.getSubFlowNo() + "01");
							dr.setValue("Name", "启动:" + item.getSubFlowName());
							dr.setValue("IsSelectEmps", "1");
							dr.setValue("IsSelected", "0");
							dtToNDs.Rows.add(dr);
						}
					}

					/// 增加到达延续子流程节点。

					/// 到达其他节点.
					// 上一次选择的节点.
					int defalutSelectedNodeID = 0;
					if (nds.size() > 1) {
						String mysql = "";
						// 找出来上次发送选择的节点.
						if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
							mysql = "SELECT  top 1 NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + this.getFK_Node()
									+ " AND ActionType=1 ORDER BY WorkID DESC";
						} else if (SystemConfig.getAppCenterDBType() == DBType.Oracle
								|| SystemConfig.getAppCenterDBType() == DBType.KingBase) {
							mysql = "SELECT * FROM ( SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + this.getFK_Node()
									+ " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
						} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
							mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + this.getFK_Node()
									+ " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1";
						} else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
							mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + this.getFK_Node()
									+ " AND ActionType=1 ORDER BY WorkID  DESC limit 1";
						}

						// 获得上一次发送到的节点.
						defalutSelectedNodeID = DBAccess.RunSQLReturnValInt(mysql, 0);
					}

					/// 为天业集团做一个特殊的判断.
					if (SystemConfig.getCustomerNo().equals("TianYe") && nd.getName().contains("董事长") == true) {
						/* 如果是董事长节点, 如果是下一个节点默认的是备案. */
						for (NodeSimple item : nds.ToJavaList()) {
							if (item.getName().contains("备案") == true && item.getName().contains("待") == false) {
								defalutSelectedNodeID = item.getNodeID();
								break;
							}
						}
					}

					/// 为天业集团做一个特殊的判断.
					// 是否增加退回的节点
					int returnNode = 0;
					if (gwf.getWFState() == WFState.ReturnSta && nd.GetParaInt("IsShowReturnNodeInToolbar") == 1) {
						String mysql = "";
						ReturnWorks returnWorks = new ReturnWorks();
						QueryObject qo = new QueryObject(returnWorks);
						qo.AddWhere(ReturnWorkAttr.WorkID, this.getWorkID());
						qo.addAnd();
						qo.AddWhere(ReturnWorkAttr.ReturnToNode, this.getFK_Node());
						qo.addAnd();
						qo.AddWhere(ReturnWorkAttr.ReturnToEmp, WebUser.getNo());
						qo.addOrderByDesc(ReturnWorkAttr.RDT);
						qo.DoQuery();
						if (returnWorks.size() != 0) {
							ReturnWork returnWork = (ReturnWork) returnWorks.get(0);
							dr = dtToNDs.NewRow();
							dr.setValue("No", returnWork.getReturnNode());
							dr.setValue("Name", "提交退回人");
							dr.setValue("IsSelected", "2");
							dr.setValue("IsSelectEmps", "0");
							dtToNDs.Rows.add(dr);
							returnNode = returnWork.getReturnNode();
						}
					}

					for (bp.wf.template.NodeSimple item : nds.ToJavaList()) {
						if (item.getNodeID() == returnNode)
							continue;
						dr = dtToNDs.NewRow();
						dr.setValue("No", item.getNodeID());
						dr.setValue("Name", item.getName());

						if (item.getHisDeliveryWay() == DeliveryWay.BySelected) {
							dr.setValue("IsSelectEmps", "1");
						} else if (item.getHisDeliveryWay() == DeliveryWay.BySelfUrl) {
							dr.setValue("IsSelectEmps", "2");
							dr.setValue("DeliveryParas", item.getDeliveryParas());
						} else if (item.getHisDeliveryWay() == DeliveryWay.BySelectedEmpsOrgModel) {
							dr.setValue("IsSelectEmps", "3");
						} else {
							dr.setValue("IsSelectEmps", "0"); // 是不是，可以选择接受人.
						}

						// 设置默认选择的节点.
						if (defalutSelectedNodeID == item.getNodeID()) {
							dr.setValue("IsSelected", "1");
						} else {
							dr.setValue("IsSelected", "0");
						}

						dtToNDs.Rows.add(dr);
					}

					/// 到达其他节点。

					// 增加一个下拉框, 对方判断是否有这个数据.
					ds.Tables.add(dtToNDs);
				}
			}

			/// 增加按钮旁的下拉框

			/// 当前节点的流程信息
			dt = nd.ToDataTableField("WF_Node");
			dt.Columns.Add("IsBackTrack", Integer.class);
			dt.Rows.get(0).setValue("IsBackTrack", 0);
			if (gwf.getWFState() == WFState.ReturnSta) {
				// 当前节点是退回状态，是否原路返回
				Paras ps = new Paras();
				ps.SQL = "SELECT ReturnNode,Returner,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID="
						+ SystemConfig.getAppCenterDBVarStr() + "WorkID  ORDER BY RDT DESC";
				ps.Add(ReturnWorkAttr.WorkID, this.getWorkID());
				DataTable mydt = DBAccess.RunSQLReturnTable(ps);
				// 说明退回并原路返回
				if (mydt.Rows.size() == 0)
					throw new Exception("err@没有找到退回信息..");

				if (mydt.Rows.size() > 0)
					dt.Rows.get(0).setValue("IsBackTrack", mydt.Rows.get(0).getValue(3));
			}
			ds.Tables.add(dt);

			///
		} catch (RuntimeException ex) {
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
			new RuntimeException("err@" + ex.getMessage());
		}
		return bp.tools.Json.ToJson(ds);
	}

	/**
	 * 工具栏
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String InitToolBarForVue() throws Exception {
		// 创建一个DataTable，返回按钮信息
		DataTable dt = new DataTable();
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("Oper");
		dt.Columns.Add("Role", Integer.class);

		/// 处理是否是加签，或者是否是会签模式.
		boolean isAskForOrHuiQian = false;
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (String.valueOf(this.getFK_Node()).endsWith("01") == false) {
			if (gwf.getWFState() == WFState.Askfor) {
				isAskForOrHuiQian = true;
			}

			/* 判断是否是加签状态，如果是，就判断是否是主持人，如果不是主持人，就让其 isAskFor=true ,屏蔽退回等按钮. */
			/** 说明：针对于组长模式的会签，协作模式的会签加签人仍可以加签 */
			if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing) {
				// 初次打开会签节点时
				if (DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true) {
					if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false) {
						isAskForOrHuiQian = true;
					}
				}

				// 执行会签后的状态
				if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader
						&& btnLab.getHuiQianLeaderRole() == HuiQianLeaderRole.OnlyOne) {
					if (!gwf.getHuiQianZhuChiRen().equals(WebUser.getNo())
							&& gwf.GetParaString("AddLeader").contains(WebUser.getNo() + ",") == false) {
						isAskForOrHuiQian = true;
					}
				} else {
					if (gwf.getHuiQianZhuChiRen().contains(WebUser.getNo() + ",") == false
							&& gwf.GetParaString("AddLeader").contains(WebUser.getNo() + ",") == false) {
						isAskForOrHuiQian = true;
					}
				}

			}
		}

		/// 处理是否是加签，或者是否是会签模式，.
		DataRow dr = dt.NewRow();

		String toolbar = "";
		try {

			/// 是否是会签？.
			if (isAskForOrHuiQian == true && SystemConfig.getCustomerNo().equals("LIMS")) {
				return "";
			}

			if (isAskForOrHuiQian == true) {
				dr.setValue("No", "Send");
				dr.setValue("Name", "确定/完成");
				dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAllSend()");
				dt.Rows.add(dr);
				if (btnLab.getPrintZipEnable() == true) {
					dr = dt.NewRow();
					dr.setValue("No", "PackUp");
					dr.setValue("Name", btnLab.getPrintZipLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				if (btnLab.getTrackEnable()) {
					dr = dt.NewRow();
					dr.setValue("No", "Track");
					dr.setValue("Name", btnLab.getTrackLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				return bp.tools.Json.ToJson(dt);
			}

			/// 是否是会签.

			/// 是否是抄送.
			if (this.getIsCC()) {
				dr = dt.NewRow();
				dr.setValue("No", "Track");
				dr.setValue("Name", "流程运行轨迹");
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

				// 判断审核组件在当前的表单中是否启用，如果启用了.
				NodeWorkCheck fwc = new NodeWorkCheck(this.getFK_Node());
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable) {
					dr = dt.NewRow();
					/* 如果不等于启用, */
					dr.setValue("No", "CCWorkCheck");
					dr.setValue("Name", "填写审核意见");
					dr.setValue("Oper", "");
					dt.Rows.add(dr);

					// toolbar += "<input type=button value='填写审核意见' enable=true
					// onclick=\"WinOpen('" + appPath +
					// "WF/WorkOpt/CCCheckNote.htm?WorkID=" + this.WorkID +
					// "&FK_Flow=" + this.FK_Flow + "&FID=" + this.getFID() +
					// "&FK_Node=" + this.FK_Node + "&s=" + tKey + "','ds'); \"
					// />";
				}
				return toolbar;
			}

			/// 是否是抄送.

			/// 如果当前节点启用了协作会签.
			if (btnLab.getHuiQianRole() == HuiQianRole.Teamup) {
				dr = dt.NewRow();
				dr.setValue("No", "SendHuiQian");
				dr.setValue("Name", "会签发送");
				dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send(true);");
				dt.Rows.add(dr);

			}

			/// 如果当前节点启用了协作会签

			/// 加载流程控制器 - 按钮
			if (this.getCurrND().getHisFormType() == NodeFormType.SelfForm) {
				/* 如果是嵌入式表单. */
				if (getCurrND().getIsEndNode()) {
					/* 如果当前节点是结束节点. */
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						dr = dt.NewRow();
						/* 如果启用了发送按钮. */
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if (SendSelfFrom()==false) return false; this.disabled=true;");
						dt.Rows.add(dr);
					}
				} else {
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if ( SendSelfFrom()==false) return false; this.disabled=true;");
						dt.Rows.add(dr);
					}
				}

				/* 处理保存按钮. */
				if (btnLab.getSaveEnable()) {
					dr = dt.NewRow();
					dr.setValue("No", "Save");
					dr.setValue("Name", btnLab.getSaveLab());
					dr.setValue("Oper", "SaveSelfFrom();");
					dt.Rows.add(dr);
				}
			}

			if (this.getCurrND().getHisFormType() != NodeFormType.SelfForm) {
				/* 启用了其他的表单. */
				if (getCurrND().getIsEndNode()) {
					/* 如果当前节点是结束节点. */
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						/* 如果启用了选择人窗口的模式是【选择既发送】. */
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send();");
						dt.Rows.add(dr);

					}
				} else {
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group) {
						/*
						 * 如果启用了发送按钮. 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
						 */
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper",
								btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send();");
						dt.Rows.add(dr);
					}
				}

				/* 处理保存按钮. */
				if (btnLab.getSaveEnable()) {
					dr = dt.NewRow();
					dr.setValue("No", "Save");
					dr.setValue("Name", btnLab.getSaveLab());
					dr.setValue("Oper", "if (SysCheckFrm() == false) return false; Save(); ");
					dt.Rows.add(dr);
				}
			}

			if (btnLab.getWorkCheckEnable()) {
				dr = dt.NewRow();
				dr.setValue("No", "workcheckBtn");
				dr.setValue("Name", btnLab.getWorkCheckLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr); // 审核
			}

			if (btnLab.getThreadEnable()) {
				/* 如果要查看子线程. */
				dr = dt.NewRow();
				dr.setValue("No", "Thread");
				dr.setValue("Name", btnLab.getThreadLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getShowParentFormEnable() && this.getPWorkID() != 0) {
				/* 如果要查看父流程. */
				dr = dt.NewRow();
				dr.setValue("No", "ParentForm");
				dr.setValue("Name", btnLab.getShowParentFormLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getTCEnable() == true) {
				/* 流转自定义.. */
				dr = dt.NewRow();
				dr.setValue("No", "TransferCustom");
				dr.setValue("Name", btnLab.getTCLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHelpRole() != 0) {
				dr = dt.NewRow();
				dr.setValue("No", "Help");
				dr.setValue("Name", btnLab.getHelpLab());
				dr.setValue("Oper", "HelpAlter()");
				dr.setValue("Role", btnLab.getHelpRole());
				dt.Rows.add(dr);
			}

			if (btnLab.getJumpWayEnable() && 1 == 2) {
				/* 跳转 */
				dr = dt.NewRow();
				dr.setValue("No", "JumpWay");
				dr.setValue("Name", btnLab.getJumpWayLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getReturnEnable()) {
				/* 退回 */
				dr = dt.NewRow();
				dr.setValue("No", "Return");
				dr.setValue("Name", btnLab.getReturnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHungEnable()) {
				/* 挂起 */
				dr = dt.NewRow();
				dr.setValue("No", "Hung");
				dr.setValue("Name", btnLab.getHungLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getShiftEnable()) {
				/* 移交 */
				dr = dt.NewRow();
				dr.setValue("No", "Shift");
				dr.setValue("Name", btnLab.getShiftLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto)) {

				// 抄送
				dr = dt.NewRow();
				dr.setValue("No", "CC");
				dr.setValue("Name", btnLab.getCCLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getDeleteEnable() != 0) {
				dr = dt.NewRow();
				dr.setValue("No", "Delete");
				dr.setValue("Name", btnLab.getDeleteLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getEndFlowEnable() && this.getCurrND().getIsStartNode() == false) {
				dr = dt.NewRow();
				dr.setValue("No", "EndFlow");
				dr.setValue("Name", btnLab.getEndFlowLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			// @李国文.
			if (btnLab.getPrintDocEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PrintDoc");
				dr.setValue("Name", btnLab.getPrintDocLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getTrackEnable()) {
				dr = dt.NewRow();
				dr.setValue("No", "Track");
				dr.setValue("Name", btnLab.getTrackLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getSearchEnable()) {
				dr = dt.NewRow();
				dr.setValue("No", "Search");
				dr.setValue("Name", btnLab.getSearchLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getBatchEnable()) {
				/* 批量处理 */
				dr = dt.NewRow();
				dr.setValue("No", "Batch");
				dr.setValue("Name", btnLab.getBatchLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getAskforEnable()) {
				/* 加签 */
				dr = dt.NewRow();
				dr.setValue("No", "Askfor");
				dr.setValue("Name", btnLab.getAskforLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader) {
				/* 会签 */
				dr = dt.NewRow();
				dr.setValue("No", "HuiQian");
				dr.setValue("Name", btnLab.getHuiQianLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			// 原始会签主持人可以增加组长
			if (((DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true
					&& gwf.getTodoEmps().contains(WebUser.getNo()) == true)
					|| gwf.getHuiQianZhuChiRen().contains(WebUser.getNo()) == true)
					&& btnLab.getAddLeaderEnable() == true) {
				/* 增加组长 */
				dr = dt.NewRow();
				dr.setValue("No", "AddLeader");
				dr.setValue("Name", btnLab.getAddLeaderLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.Button) {
				/* 公文正文 */
				dr = dt.NewRow();
				dr.setValue("No", "WebOffice");
				dr.setValue("Name", btnLab.getWebOfficeLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			// 需要翻译.
			if (this.getCurrFlow().getIsResetData() == true && this.getCurrND().getIsStartNode()) {
				/* 启用了数据重置功能 */
				dr = dt.NewRow();
				dr.setValue("No", "ReSet");
				dr.setValue("Name", "数据重置");
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getCHRole() != 0) {
				/* 节点时限设置 */
				dr = dt.NewRow();
				dr.setValue("No", "CH");
				dr.setValue("Name", btnLab.getCHLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getNoteEnable() != 0) {
				/* 备注设置 */
				dr = dt.NewRow();
				dr.setValue("No", "Note");
				dr.setValue("Name", btnLab.getNoteLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getPRIEnable() != 0) {
				/* 优先级设置 */
				dr = dt.NewRow();
				dr.setValue("No", "PR");
				dr.setValue("Name", btnLab.getPRILab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 关注 */
			if (btnLab.getFocusEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "Focus");
				if (getHisGenerWorkFlow().getParasFocus() == true) {
					dr.setValue("Name", "取消关注");
				} else {
					dr.setValue("Name", btnLab.getFocusLab());
				}
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 分配工作 */
			if (btnLab.getAllotEnable() == true) {
				/* 分配工作 */
				dr = dt.NewRow();
				dr.setValue("No", "Allot");
				dr.setValue("Name", btnLab.getAllotLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 确认 */
			if (btnLab.getConfirmEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "Confirm");
				if (getHisGenerWorkFlow().getParasConfirm() == true) {
					dr.setValue("Name", "取消确认");
				} else {
					dr.setValue("Name", btnLab.getConfirmLab());
				}

				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			// 需要翻译.

			/* 打包下载zip */
			if (btnLab.getPrintZipEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_zip");
				dr.setValue("Name", btnLab.getPrintZipLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_html");
				dr.setValue("Name", btnLab.getPrintHtmlLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载pdf */
			if (btnLab.getPrintPDFEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_pdf");
				dr.setValue("Name", btnLab.getPrintPDFLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (this.getCurrND().getIsStartNode() == true) {
				if (this.getCurrFlow().getIsDBTemplate() == true) {
					dr = dt.NewRow();
					dr.setValue("No", "DBTemplate");
					dr.setValue("Name", "模版");
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}
			}

			/* 公文标签 */
			if (btnLab.getOfficeBtnEnable() == true) {
				dr = dt.NewRow();
				dr.setValue("No", "Btn_Office");
				dr.setValue("Name", btnLab.getOfficeBtnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			///

			/// 加载自定义的button.
			bp.wf.template.NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node());
			for (NodeToolbar bar : bars.ToJavaList()) {
				if (bar.getShowWhere() != ShowWhere.Toolbar) {
					continue;
				}

				if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) == false
						&& bar.getTarget().toLowerCase().equals("javascript"))) {
					dr = dt.NewRow();
					dr.setValue("No", "Btn_Office");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", bar.getUrl());
					dt.Rows.add(dr);
				} else {
					dr = dt.NewRow();
					dr.setValue("No", "Btn_Office");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", bar.getUrl());
					dt.Rows.add(dr);

				}
			}

			/// //加载自定义的button.

		} catch (RuntimeException ex) {
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
			new RuntimeException("err@" + ex.getMessage());
		}
		return bp.tools.Json.ToJson(dt);
	}

	/**
	 * 获取主表的方法.
	 * 
	 * @return
	 */
	private java.util.Hashtable GetMainTableHT() throws Exception {
		java.util.Hashtable htMain = new java.util.Hashtable();
		Enumeration enu = getRequest().getParameterNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (key == null) {
				continue;
			}
			String val = URLDecoder.decode(this.GetRequestVal(key), "UTF-8");
			key = key.replace("TB_", "");
			key = key.replace("DDL_", "");
			key = key.replace("CB_", "");
			key = key.replace("RB_", "");
			if (htMain.containsKey(key) == true)
				htMain.replace(key, val);
			else
				htMain.put(key, val);
		}
		return htMain;
	}

	/**
	 * 删除流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DeleteFlow() throws Exception {
		try {
			return bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(this.getWorkID(), true);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}
	
	public final void SetAuth() throws Exception {
		// 授权人.
		String auther = this.GetRequestVal("Auther");
		 

		if (DataType.IsNullOrEmpty(auther) == false) {
			// BP.Web.WebUser.IsAuthorize = true;
			WebUser.setAuth(auther);
			WebUser.setAuthName(DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'"));
		} else {
			// BP.Web.WebUser.IsAuthorize = true;
			WebUser.setAuth("");
			WebUser.setAuthName(""); // BP.DA.DBAccess.RunSQLReturnString("SELECT
										// Name FROM Port_Emp WHERE No='" +
										// auther + "'");
		}
		
	}

	/**
	 * 发送
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Send() throws Exception {
		try {
			Hashtable ht = this.GetMainTableHT();
			SendReturnObjs objs = null;
			String msg = "";

			// 判断当前流程工作的GenerWorkFlow是否存在
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			int i = gwf.RetrieveFromDBSources();
			if (i == 0)
				return "该流程的工作已删除,请联系管理员";

			// 授权人.
			String auther = this.GetRequestVal("Auther");
			Boolean IsReturnNode = this.GetRequestValBoolen("IsReturnNode");
			
			this.SetAuth();
			 

			objs = bp.wf.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), ht, null, this.getToNode(),
					null, WebUser.getNo(), WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName(), null,
					this.getFID(), this.getPWorkID(), IsReturnNode);

			msg = objs.ToMsgOfHtml();
			bp.wf.Glo.setSessionMsg(msg);

			// #region 处理授权 @lizhen.

			if (DataType.IsNullOrEmpty(auther) == false) {
				gwf.SetPara("Auth", bp.web.WebUser.getAuthName() + "授权给"+WebUser.getName());
				gwf.Update();
			}
			// #endregion 处理授权 @lizhen.

			// 当前节点.
			Node currNode = new Node(this.getFK_Node());

			/// 处理发送后转向.
			/* 处理转向问题. */
			switch (currNode.getHisTurnToDeal()) {
			case SpecUrl:
				String myurl = currNode.getTurnToDealDoc();
				if (myurl.contains("?") == false) {
					myurl += "?1=1";
				}
				Attrs myattrs = currNode.getHisWork().getEnMap().getAttrs();
				Work hisWK = currNode.getHisWork();
				for (Attr attr : myattrs) {
					if (myurl.contains("@") == false) {
						break;
					}
					myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
				}
				myurl = myurl.replace("@WebUser.No", WebUser.getNo());
				myurl = myurl.replace("@WebUser.Name", WebUser.getName());
				myurl = myurl.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				if (myurl.contains("@")) {
					bp.wf.Dev2Interface.Port_SendMsg("admin",
							getCurrFlow().getName() + "在" + getCurrND().getName() + "节点处，出现错误",
							"流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl,
							"Err" + getCurrND().getNo() + "_" + this.getWorkID(), SMSMsgType.Err, this.getFK_Flow(),
							this.getFK_Node(), this.getWorkID(), this.getFID());
					throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
				}

				if (myurl.contains("PWorkID") == false) {
					myurl += "&PWorkID=" + this.getWorkID();
				}

				myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&UserNo="
						+ WebUser.getNo() + "&SID=" + WebUser.getSID();
				return "TurnUrl@" + myurl;
			case TurnToByCond:
				// TurnTos tts = new TurnTos(this.FK_Flow);
				// if (tts.size() == 0)
				// {
				// BP.WF.Dev2Interface.Port_SendMsg("admin", currFlow.Name + "在"
				// + currND.Name + "节点处，出现错误", "您没有设置节点完成后的转向条件。", "Err" +
				// currND.No + "_" + this.WorkID, SMSMsgType.Err, this.FK_Flow,
				// this.FK_Node, this.WorkID, this.getFID());
				// throw new Exception("@您没有设置节点完成后的转向条件。");
				// }

				// foreach (TurnTo tt in tts)
				// {
				// tt.HisWork = currNode.HisWork;
				// if (tt.IsPassed == true)
				// {
				// string url = tt.TurnToURL.Clone().ToString();
				// if (url.Contains("?") == false)
				// url += "?1=1";
				// Attrs attrs = currNode.HisWork.getEnMap().getAttrs();
				// Work hisWK1 = currNode.HisWork;
				// foreach (Attr attr in attrs)
				// {
				// if (url.Contains("@") == false)
				// break;
				// url = url.replace("@" + attr.getKey(),
				// hisWK1.GetValStrByKey(attr.getKey()));
				// }
				// if (url.Contains("@"))
				// throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);

				// url += "&PFlowNo=" + this.FK_Flow + "&FromNode=" +
				// this.FK_Node + "&PWorkID=" + this.WorkID + "&UserNo=" +
				// WebUser.getNo() + "&SID=" + WebUser.getSID();
				// return "url@" + url;
				// }
				// }
				return msg;
			default:
				msg = msg.replace("@WebUser.No", WebUser.getNo());
				msg = msg.replace("@WebUser.Name", WebUser.getName());
				msg = msg.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				return msg;
			}

			///

		} catch (RuntimeException ex) {
			if (ex.getMessage().indexOf("url@") == 0) {
				return ex.getMessage();
			}

			// 清楚上次选择的节点信息.
			if (DataType.IsNullOrEmpty(this.getHisGenerWorkFlow().getParasToNodes()) == false) {
				this.getHisGenerWorkFlow().setParasToNodes("");
				this.getHisGenerWorkFlow().Update();
			}

			if (ex.getMessage().contains("请选择下一步骤工作") == true || ex.getMessage().contains("用户没有选择发送到的节点") == true) {
				if (this.getCurrND().getCondModel() == DirCondModel.ByUserSelected) {
					/* 如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
					return "SelectNodeUrl@./WorkOpt/ToNodes.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node="
							+ this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();

				}

				// if (this.currND.CondModel != CondModel.SendButtonSileSelect)
				// {
				// currND.CondModel = CondModel.SendButtonSileSelect;
				// currND.Update();
				// }

				return "err@下一个节点的接收人规则是，当前节点选择来选择，在当前节点属性里您没有启动接受人按钮，系统自动帮助您启动了，请关闭窗口重新打开。" + ex.getMessage();
			}

			// 绑定独立表单，表单自定义方案验证错误弹出窗口进行提示.
			if (ex.getMessage().contains("提交前检查到如下必填字段填写不完整") == true || ex.getMessage().contains("您没有上传附件") == true
					|| ex.getMessage().contains("您没有上传图片附件") == true) {
				return "err@" + ex.getMessage().replace("@@", "@").replace("@", "<BR>@");
			}

			// 防止发送失败丢失接受人，导致不能出现下拉方向选择框. @杜.
			if (this.getHisGenerWorkFlow() != null) {
				// 如果是会签状态.
				if (this.getHisGenerWorkFlow().getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing) {
					// 如果是主持人.
					if (this.getHisGenerWorkFlow().getHuiQianZhuChiRen().equals(WebUser.getNo())) {
						if (this.getHisGenerWorkFlow().getTodoEmps().contains(WebUser.getNo() + ",") == false) {
							this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps()
									+ WebUser.getNo() + "," + WebUser.getName() + ";");
							this.getHisGenerWorkFlow().Update();
						}
					} else {
						// 非主持人.
						String empStr = WebUser.getNo() + "," + WebUser.getName() + ";";
						if (this.getHisGenerWorkFlow().getTodoEmps().contains(empStr) == false) {
							this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps() + empStr); // WebUser.getNo()
																														// +","+WebUser.getName()
																														// +
																														// ";";
							this.getHisGenerWorkFlow().Update();
						}
					}
				}

				if (this.getHisGenerWorkFlow().getHuiQianTaskSta() != HuiQianTaskSta.HuiQianing) {
					String empStr = WebUser.getNo() + "," + WebUser.getName() + ";";
					if (this.getHisGenerWorkFlow().getTodoEmps().contains(empStr) == false) {
						this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps() + empStr);
						this.getHisGenerWorkFlow().Update();
					}
				}
			}

			// 如果错误，就写标记.
			String msg = ex.getMessage();
			if (msg.indexOf("err@") == -1 && msg.indexOf("url@") != 0) {
				msg = "err@" + msg;
			}
			return msg;
		}
	}

	/**
	 * 批量发送
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String StartGuide_MulitSend() throws Exception {
		// 获取设置的数据源
		Flow fl = new Flow(this.getFK_Flow());
		String key = this.GetRequestVal("Key");
		String SKey = this.GetRequestVal("Keys");
		String sql = "";
		// 判断是否有查询条件
		Object tempVar = fl.getStartGuidePara2();
		sql = tempVar instanceof String ? (String) tempVar : null;
		if (!DataType.IsNullOrEmpty(key)) {
			Object tempVar2 = fl.getStartGuidePara1();
			sql = tempVar2 instanceof String ? (String) tempVar2 : null;
			sql = sql.replace("@Key", key);
		}
		// 替换变量
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		// 获取选中的数据源
		DataRow[] drArr = dt.Select("No in(" + StringHelper.trimEnd(SKey, ',') + ")");

		// 获取Nos
		String Nos = "";
		for (int i = 0; i < drArr.length; i++) {
			DataRow row = drArr[i];
			Nos += row.get("No") + ",";
		}
		return StringHelper.trimEnd(Nos, ',');
	}

	/**
	 * 保存
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Save() throws Exception {
		try {
			String str = bp.wf.Dev2Interface.Node_SaveWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(),
					this.GetMainTableHT(), null, this.getFID(), this.getPWorkID());

			if (this.getPWorkID() != 0) {
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				bp.wf.Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), this.getPWorkID(), gwf.getPEmp(),
						gwf.getPNodeID());
			}
			return str;
		} catch (RuntimeException ex) {
			return "err@保存失败:" + ex.getMessage();
		}
	}

	public final String MyFlowSelfForm_Init() throws Exception {
		return this.GenerWorkNode();
	}

	public final String SaveFlow_ToDraftRole() throws Exception {

		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		if (this.getWorkID() != 0) {
			wk.setOID(this.getWorkID());
			wk.RetrieveFromDBSources();
		}

		// 获取表单树的数据
		bp.wf.WorkNode workNode = new WorkNode(this.getWorkID(), this.getFK_Node());
		Work treeWork = workNode.CopySheetTree();
		if (treeWork != null) {
			wk.Copy(treeWork);
			wk.Update();
		}

		// 获取该节点是是否是绑定表单方案, 如果流程节点中的字段与绑定表单的字段相同时赋值
		// if (nd.FormType == NodeFormType.SheetTree || nd.FormType ==
		// NodeFormType.RefOneFrmTree)
		// {
		// FrmNodes nds = new FrmNodes(this.FK_Flow, this.FK_Node);
		// foreach (FrmNode item in nds)
		// {
		// if (item.FrmEnableRole == FrmEnableRole.Disable)
		// continue;
		// if (item.FK_Frm.Equals("ND"+this.FK_Node) == true)
		// continue;
		// GEEntity en = null;
		// try
		// {
		// en = new GEEntity(item.FK_Frm);
		// en.setPKVal(this.WorkID;
		// if (en.RetrieveFromDBSources() == 0)
		// {
		// continue;
		// }
		// }
		// catch (Exception ex)
		// {
		// continue;
		// }

		// Attrs frmAttrs = en.getEnMap().getAttrs();
		// Attrs wkAttrs = wk.getEnMap().getAttrs();
		// foreach (Attr wkattr in wkAttrs)
		// {
		// if (wkattr.getKey().Equals(GERptAttr.OID) ||
		// wkattr.getKey().Equals(GERptAttr.FID) ||
		// wkattr.getKey().Equals(GERptAttr.CDT)
		// || wkattr.getKey().Equals(GERptAttr.RDT) ||
		// wkattr.getKey().Equals(GERptAttr.MD5) ||
		// wkattr.getKey().Equals(GERptAttr.Emps)
		// || wkattr.getKey().Equals(GERptAttr.FK_Dept) ||
		// wkattr.getKey().Equals(GERptAttr.PRI) ||
		// wkattr.getKey().Equals(GERptAttr.Rec)
		// || wkattr.getKey().Equals(GERptAttr.Title) ||
		// wkattr.getKey().Equals(Data.GERptAttr.FK_NY) ||
		// wkattr.getKey().Equals(Data.GERptAttr.FlowEmps)
		// || wkattr.getKey().Equals(Data.GERptAttr.FlowStarter) ||
		// wkattr.getKey().Equals(Data.GERptAttr.FlowStartRDT) ||
		// wkattr.getKey().Equals(Data.GERptAttr.WFState))
		// {
		// continue;
		// }

		// foreach (Attr attr in frmAttrs)
		// {
		// if (wkattr.getKey().Equals(attr.getKey()))
		// {
		// wk.SetValByKey(wkattr.getKey(), en.GetValStrByKey(attr.getKey()));
		// break;
		// }

		// }

		// }

		// }
		// wk.Update();
		// }

		/// 为开始工作创建待办.
		if (nd.getIsStartNode() == true) {
			GenerWorkFlow gwf = new GenerWorkFlow();
			Flow fl = new Flow(this.getFK_Flow());
			if (fl.getDraftRole() == DraftRole.None && this.GetRequestValInt("SaveType") != 1) {
				return "保存成功";
			}

			// 规则设置为写入待办，将状态置为运行中，其他设置为草稿.
			WFState wfState = WFState.Blank;
			if (fl.getDraftRole() == DraftRole.SaveToDraftList) {
				wfState = WFState.Draft;
			}
			if (fl.getDraftRole() == DraftRole.SaveToTodolist) {
				wfState = WFState.Runing;
			}

			// 设置标题.
			String title = bp.wf.WorkFlowBuessRole.GenerTitle(fl, wk);

			// 修改RPT表的标题
			wk.SetValByKey(bp.wf.data.GERptAttr.Title, title);
			wk.Update();

			gwf.setWorkID(this.getWorkID());
			int count = gwf.RetrieveFromDBSources();

			gwf.setTitle(title); // 标题.
			if (count == 0) {
				gwf.setFlowName(fl.getName());
				gwf.setFK_Flow(this.getFK_Flow());
				gwf.setFK_FlowSort(fl.getFK_FlowSort());
				gwf.setSysType(fl.getSysType());

				gwf.setFK_Node(this.getFK_Node());
				gwf.setNodeName(nd.getName());
				gwf.setWFState(wfState);

				gwf.setFK_Dept(WebUser.getFK_Dept());
				gwf.setDeptName(WebUser.getFK_DeptName());
				gwf.setStarter(WebUser.getNo());
				gwf.setStarterName(WebUser.getName());
				gwf.setRDT(DataType.getCurrentDataTimess());
				gwf.Insert();

				// 产生工作列表.
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.setWorkID(this.getWorkID());
				gwl.setFK_Emp(WebUser.getNo());
				gwl.setFK_EmpText(WebUser.getName());

				gwl.setFK_Node(gwf.getFK_Node());
				gwl.setFK_NodeText(nd.getName());
				gwl.setFID(0);

				gwl.setFK_Flow(gwf.getFK_Flow());
				gwl.setFK_Dept(WebUser.getFK_Dept());
				gwl.setFK_DeptT(WebUser.getFK_DeptName());

				gwl.setSDT("无");
				gwl.setDTOfWarning(DataType.getCurrentDataTimess());
				gwl.setIsEnable(true);

				gwl.setIsPass(false);
				// gwl.Sender = WebUser.getNo();
				gwl.setPRI(gwf.getPRI());
				gwl.Insert();
			} else {
				gwf.setWFState(wfState);
				gwf.DirectUpdate();
			}

		}

		/// 为开始工作创建待办
		return "保存到待办";
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
			if (item.getParentNo().equals(parentEn.getNo()) == false) {
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
		DataSet ds = new DataSet();
		long workID = this.getWorkID(); // 表单的主表.

		/// 判断当前的节点类型,获得表单的ID.
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

		/// 判断当前的节点类型,获得表单的ID.

		try {
			ds = bp.wf.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getCurrND(), workID, this.getFID(),
					WebUser.getNo(), this.getWorkID());

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

	// 发起会签子流程
	public String MyFlow_StartThread() throws Exception {
		Node nd = new Node(this.getFK_Node());

		// 查询出来该流程实例下的所有草稿子流程.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID(), GenerWorkFlowAttr.WFState, 1);

		// 子流程配置信息.
		SubFlowHandGuide sf = null;
		SendReturnObjs returnObjs;
		String msgHtml = "";

		// 开始发送子流程.

		for (GenerWorkFlow gwfSubFlow : gwfs.ToJavaList()) {
			// 获得配置信息.
			if (sf == null || sf.getFK_Flow().equals(gwfSubFlow.getFK_Flow()) == false) {
				String pkval = this.getFK_Flow() + "_" + gwfSubFlow.getFK_Flow() + "_0";
				sf = new SubFlowHandGuide();
				sf.setMyPK(pkval);
				sf.RetrieveFromDBSources();
			}

			// 把草稿移交给当前人员. - 更新控制表.
			gwfSubFlow.setStarter(WebUser.getNo());
			gwfSubFlow.setStarterName(WebUser.getName());
			gwfSubFlow.Update();

			// 把草稿移交给当前人员. - 更新工作人员列表.
			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET FK_Emp='" + WebUser.getNo() + "',FK_EmpText='"
					+ bp.web.WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.getWorkID());
					// 更新track表.
					// DBAccess.RunSQL("UPDATE ND"+int.Parse(gwfSubFlow.FK_Flow)
					// +"Track SET FK_Emp='" + WebUser.No + "',FK_EmpText='" +
					// WebUser.Name + "' WHERE WorkID=" + gwfSubFlow.WorkID);

			// 启动子流程. 并把两个字段，写入子流程.
			returnObjs = bp.wf.Dev2Interface.Node_SendWork(gwfSubFlow.getFK_Flow(), gwfSubFlow.getWorkID(), null, null);
			msgHtml += returnObjs.ToMsgOfHtml() + "</br>";
		}
		return "启动的子流程信息如下:</br>" + msgHtml;
	}
	
	public final String MyFlow_IsCanStartThisFlow() throws Exception
	{

			///判断是否可以否发起流程.
		try
		{
			if (bp.wf.Dev2Interface.Flow_IsCanStartThisFlow(this.getFK_Flow(), WebUser.getNo(), this.getPFlowNo(), this.getPNodeID(), this.getPWorkID()) == false)
			{
				/*是否可以发起流程？ */
				throw new RuntimeException("err@您(" + WebUser.getNo() + ")没有发起或者处理该流程的权限.");
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("err@" + ex.getMessage());
		}

		 return "可以发起流程";
	}
}