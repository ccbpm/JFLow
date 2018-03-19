package cn.jflow.common.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityMultiTree;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;
import BP.Sys.AppType;
import BP.Sys.FrmType;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.SendReturnObjs;
import BP.WF.WorkNode;
import BP.WF.Data.GERpt;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CondModel;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.ShowWhere;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.FlowFormTree;
import BP.WF.Template.FlowFormTrees;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTreeAttr;
import BP.WF.Template.SysFormTrees;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.DeliveryWay;
import BP.Web.WebUser;

public class FormTreeBaseModel extends BaseModel {

	public FormTreeBaseModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public final String getUTF8ToString(String param) {
		// return
		// HttpUtility.UrlDecode(Request[param],System.Text.Encoding.UTF8);
		// 这个地方是这样改吗
		String param2 = getParameter(param);
		try {
			param2 = URLDecoder.decode(param2, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return param2;
	}

	public final String getDoFunc() {
		return getUTF8ToString("DoFunc");
	}

	public final String getCFlowNo() {
		return getUTF8ToString("CFlowNo");
	}

	public final String getWorkIDs() {
		return getUTF8ToString("WorkIDs");
	}

	public final String getFK_Flow() {
		return getUTF8ToString("FK_Flow");
	}

	public final int getFK_Node() {
		String fk_node = getUTF8ToString("FK_Node");
		if (!StringHelper.isNullOrEmpty(fk_node)) {
			return Integer.parseInt(getUTF8ToString("FK_Node"));
		}
		return 0;
	}

	public final long getWorkID() {
		return Long.parseLong(getUTF8ToString("WorkID"));
	}

	public final long getFID() {
		return Long.parseLong(getUTF8ToString("FID"));
	}

	public final void Page_Load() {
		if (StringHelper.isNullOrEmpty(WebUser.getNo())) {
			return;
		}

		String method = "";
		// 返回值
		String s_responsetext = "";

		/*
		 * if (!StringHelper.isNullOrEmpty(Request["method"])) { method =
		 * Request["method"].toString(); }
		 */
		if (!StringHelper.isNullOrEmpty(getParameter("method"))) {
			method = getParameter("method");
		}

		if (method.equals("getapptoolbar")) {
			s_responsetext = GetAppToolBar();

		} else if (method.equals("getflowformtree")) { // 获取表单树
			s_responsetext = GetFlowFormTree();

		} else if (method.equals("saveblank")) {
			s_responsetext = SaveBlank();

		} else if (method.equals("checkaccepter")) { // 接受人检查
			s_responsetext = CheckAccepterOper();

		} else if (method.equals("sendcase")) { // 执行发送
			s_responsetext = SendCase();

		} else if (method.equals("sendcasetonode")) { // 执行发送到指定节点
			s_responsetext = SendCaseToNode();

		} else if (method.equals("unsendcase")) { // 撤销发送
			s_responsetext = UnSendCase();

		} else if (method.equals("")) { // 保存
			s_responsetext = Send();

		} else if (method.equals("delcase")) { // 删除流程
			s_responsetext = Delcase();

		} else if (method.equals("signcase")) { // 流程签名
			s_responsetext = Signcase();

		} else if (method.equals("endcase")) { // 结束流程
			s_responsetext = EndCase();
		}

		if (StringHelper.isNullOrEmpty(s_responsetext)) {
			s_responsetext = "";
		}

		// 组装ajax字符串格式,返回调用客户端
		/*
		 * get_response.Charset = "UTF-8"; Response.ContentEncoding =
		 * System.Text.Encoding.UTF8; Response.ContentType = "text/html";
		 * Response.Expires = 0; Response.Write(s_responsetext); Response.End();
		 */
		HttpServletResponse response = get_response();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		// 这个地方怎么翻译Response.Expires = 0;
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(s_responsetext);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String SaveBlank() {
		try {
			Node node = new Node();
			node.setNodeID(this.getFK_Node());
			node.RetrieveFromDBSources();

			if (node.getIsStartNode()) {
				GenerWorkFlow flow = new GenerWorkFlow();
				flow.setWorkID(this.getWorkID());
				int i = flow.RetrieveFromDBSources();
				if (i <= 0) {
					try {
						BP.WF.Dev2Interface.Node_CreateStartNodeWork(
								this.getFK_Flow(), null, null, WebUser.getNo(),
								null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (RuntimeException ex) {
			return "创建草稿出现异常:" + ex.getMessage();
		}
		return "true";
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	public final String Send() {
		return "";
	}

	/**
	 * 结束流程
	 * 
	 * @return
	 */
	public final String EndCase() {
		String flowId = getUTF8ToString("flowId");
		long workID = Long.parseLong(getUTF8ToString("workId"));

		return BP.WF.Dev2Interface.Flow_DoFlowOverByCoercion(flowId,
				this.getFK_Node(), workID, this.getFID(), "");
	}

	/**
	 * 删除流程
	 */
	public final String Delcase() {
		String returnstr = "";
		/*
		 * String flowId = getUTF8ToString("flowId"); int fk_Node =
		 * Integer.parseInt(getUTF8ToString("nodeId")); long workID =
		 * Long.parseLong(getUTF8ToString("workId")); long fId =
		 * Long.parseLong(getUTF8ToString("fId"));
		 */
		String flowId = getParameter("flowId");
		int fk_Node = Integer.parseInt(getParameter("nodeId"));
		long workID = Long.parseLong(getParameter("workId"));
		long fId = Long.parseLong(getParameter("fId"));

		Node currND = new Node(fk_Node);

		return Dev2Interface.Flow_DoDeleteFlowByReal(flowId, workID, false);
	}

	/**
	 * 签名流程
	 */
	public final String Signcase() {
		/*
		 * String flowId = getUTF8ToString("flowId"); int fk_Node =
		 * Integer.parseInt(getUTF8ToString("nodeId")); long workID =
		 * Long.parseLong(getUTF8ToString("workId")); long fId =
		 * Long.parseLong(getUTF8ToString("fId"));
		 */
		String flowId = getParameter("flowId");
		int fk_Node = Integer.parseInt(getParameter("nodeId"));
		long workID = Long.parseLong(getParameter("workId"));
		long fId = Long.parseLong(getParameter("fId"));
		if (fId > 0) {
			workID = fId;
		}
		// String yj = getUTF8ToString("yj");
		String yj = getParameter("yj");
		if (yj == null || yj.equals("")) {
			yj = "已阅";
		}

		BP.DA.Paras ps = new BP.DA.Paras();
		ps.Add("FK_Node", fk_Node);

		DataTable Sys_FrmSln = BP.DA.DBAccess
				.RunSQLReturnTable(
						"select Sys_FrmSln.FK_MapData,Sys_FrmSln.KeyOfEn,Sys_FrmSln.IsSigan,Sys_MapAttr.MyDataType from Sys_FrmSln,Sys_MapAttr where Sys_FrmSln.UIIsEnable=1 and Sys_FrmSln.IsNotNull=1 and Sys_FrmSln.FK_Node="
								+ SystemConfig.getAppCenterDBVarStr()
								+ "FK_Node and Sys_FrmSln.KeyOfEn=Sys_MapAttr.KeyOfEn and Sys_FrmSln.FK_MapData=Sys_MapAttr.FK_MapData",
						ps);
		boolean IsSign = false;
		for (DataRow DR : Sys_FrmSln.Rows) {
			// 这样翻译对ma String PTableField = DR["KeyOfEn"].toString();
			String PTableField = DR.getValue("KeyOfEn").toString();
			String autotext = "";
			if (DR.getValue("IsSigan").toString().equals("1")) {
				autotext = WebUser.getNo();
			} else if (DR.getValue("MyDataType").toString().equals("6")) { // 时间字段
				// autotext = DateUtils.getCurrentDate("yyyy-MM-dd");
				// 获取当前时间,并转化为字符串
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				autotext = sdf.format(new Date());

			} else if (DR.getValue("MyDataType").toString().equals("1")) { // 意见字段
				PTableField = PTableField.toUpperCase();
				if (PTableField.endsWith("YJ") || PTableField.endsWith("YJ1")
						|| PTableField.endsWith("YJ2")
						|| PTableField.endsWith("YJ3")
						|| PTableField.endsWith("YJ4")
						|| PTableField.endsWith("YJ5")
						|| PTableField.endsWith("YJ6")
						|| PTableField.endsWith("YJ7")
						|| PTableField.endsWith("YJ8")
						|| PTableField.endsWith("YJ9")) {
					autotext = yj;
				} else {
					continue;
				}
			} else {
				continue;
			}
			String PTable = BP.DA.DBAccess
					.RunSQLReturnString("select PTable from Sys_MapData where No='"
							+ DR.getValue("FK_MapData").toString() + "'");
			if (PTable != null) {
				int HavData = BP.DA.DBAccess
						.RunSQLReturnValInt("select count(OID) from " + PTable
								+ " where oid=" + (new Long(workID)).toString());
				if (HavData == 0) {
					BP.DA.DBAccess.RunSQL("insert into " + PTable + "(oid,"
							+ PTableField + ") values("
							+ (new Long(workID)).toString() + ",'" + autotext
							+ "')");
				} else {
					BP.DA.DBAccess.RunSQL("update " + PTable + " set "
							+ PTableField + "='" + autotext + "' where oid="
							+ (new Long(workID)).toString());
				}
				IsSign = true;
			}
		}
		if (IsSign) {
			return "签名完毕";
		} else {
			return "没有签名可签";
		}
	}

	/**
	 * 获取工具栏
	 * 
	 * @return
	 */
	private String GetAppToolBar() {
		int fk_Node = Integer.parseInt(getUTF8ToString("nodeId"));
		BtnLab btnLab = new BtnLab(fk_Node);
		StringBuilder toolsBar = new StringBuilder();
		toolsBar.append("{");

		// 系统工具
		toolsBar.append("tools:[");
		// Send,Save,Thread,Return,CC,Shift,Del,EndFLow，RptTrack,HungUp"
		// 发送
		if (btnLab.getSendEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Send',btnlabel:'" + btnLab.getSendLab() + "'");
			toolsBar.append("},");
		}
		// 保存
		if (btnLab.getSaveEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Save',btnlabel:'" + btnLab.getSaveLab() + "'");
			toolsBar.append("},");
		}
		// 子线程
		if (btnLab.getThreadEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Thread',btnlabel:'" + btnLab.getThreadLab()
					+ "'");
			toolsBar.append("},");
		}

		// 退回
		if (btnLab.getReturnEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Return',btnlabel:'" + btnLab.getReturnLab()
					+ "'");
			toolsBar.append("},");
		}
		// 抄送
		if ( btnLab.getCCRole()  !=  null) {
			toolsBar.append("{");
			toolsBar.append("no:'CC',btnlabel:'" + btnLab.getCCLab() + "'");
			toolsBar.append("},");
		}
		// 移交
		if (btnLab.getShiftEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Shift',btnlabel:'" + btnLab.getShiftLab()
					+ "'");
			toolsBar.append("},");
		}
		// 删除
		if (btnLab.getDeleteEnable() != 0) {
			toolsBar.append("{");
			toolsBar.append("no:'Del',btnlabel:'" + btnLab.getDeleteLab() + "'");
			toolsBar.append("},");
		}
		// 结束
		if (btnLab.getEndFlowEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'EndFLow',btnlabel:'" + btnLab.getEndFlowLab()
					+ "'");
			toolsBar.append("},");
		}
		// 打印
		if (btnLab.getPrintDocEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Rpt',btnlabel:'" + btnLab.getPrintDocLab()
					+ "'");
			toolsBar.append("},");
		}
		// 轨迹
		if (btnLab.getTrackEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'Track',btnlabel:'" + btnLab.getTrackLab()
					+ "'");
			toolsBar.append("},");
		}
		// 挂起
		if (btnLab.getHungEnable()) {
			toolsBar.append("{");
			toolsBar.append("no:'HungUp',btnlabel:'" + btnLab.getHungLab()
					+ "'");
			toolsBar.append("},");
		}
		if (toolsBar.length() > 8) {
			toolsBar.deleteCharAt(toolsBar.length() - 1);
		}
		toolsBar.append("]");
		// 扩展工具
		NodeToolbars extToolBars = new NodeToolbars();
		extToolBars.RetrieveByAttr(NodeToolbarAttr.FK_Node, fk_Node);
		toolsBar.append(",extTools:[");
		if (extToolBars.size() > 0) {
			for (NodeToolbar item : extToolBars.ToJavaList()) {
				toolsBar.append("{OID:'" + item.getOID() + "',Title:'"
						+ item.getTitle() + "',Target:'" + item.getTarget()
						+ "',Url:'" + item.getUrl() + "'},");
			}
			toolsBar.deleteCharAt(toolsBar.length() - 1);
		}
		toolsBar.append("]");
		toolsBar.append("}");
		return toolsBar.toString();
	}

	/**
	 * 检查节点是否是启用接收人选择器
	 * 
	 * @return
	 */
	private String CheckAccepterOper() {
		int tempToNodeID = 0;
		// 获取到当前节点
		Node _HisNode = new Node(this.getFK_Node());

		// 如果到达的点为空
		Nodes nds = _HisNode.getHisToNodes();
		if (nds.size() == 0) {
			// 当前点是最后的一个节点，不能使用此功能
			return "end";
		} else if (nds.size() == 1) {
			// Node toND = (Node) ((nds[0] instanceof Node) ? nds[0]
			Node toND = (Node) ((nds.get(0) instanceof Node) ? nds.get(0)
					: null);
			tempToNodeID = toND.getNodeID();
		} else {
			for (Node mynd : nds.ToJavaList()) {
				GERpt _wk = _HisNode.getHisFlow().getHisGERpt();
				_wk.setOID(this.getWorkID());
				_wk.Retrieve();
				_wk.ResetDefaultVal();

				// /过滤不能到达的节点.
				Cond cond = new Cond();
				int i = cond.Retrieve(CondAttr.FK_Node, _HisNode.getNodeID(),
						CondAttr.ToNodeID, mynd.getNodeID());
				if (i == 0) {
					continue; // 没有设置方向条件，就让它跳过去。
				}
				cond.setWorkID(this.getWorkID());
				cond.en = _wk;
				if (cond.getIsPassed() == false) {
					continue;
				}
				// 过滤不能到达的节点.
				tempToNodeID = mynd.getNodeID();
			}
		}
		// 不存在下一个节点,检查是否配置了有用户选择节点
		if (tempToNodeID == 0) {
			try {
				// 检查必填项
				WorkNode workeNode = new WorkNode(this.getWorkID(),
						this.getFK_Node());
				workeNode.CheckFrmIsNotNull();
			} catch (RuntimeException ex) {
				return "error:" + ex.getMessage();
			}
			// 按照用户选择计算
			if (_HisNode.getCondModel().equals(CondModel.ByUserSelected)) {
				return "byuserselected";
			}
			return "notonode";
		}

		// 判断到达的节点是否是按接受人选择
		Node toNode = new Node(tempToNodeID);
		if (toNode.getHisDeliveryWay().equals(DeliveryWay.BySelected)) {
			return "byselected";
		}
		return "nodata";
	}

	/**
	 * 执行发送
	 * 
	 * @return
	 */
	private String SendCase() {
		String resultMsg = "";
		try {
			if (Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(),
					this.getFK_Node(), this.getWorkID(), WebUser.getNo()) == false) {
				resultMsg = "error|您好：" + WebUser.getNo() + ", "
						+ WebUser.getName() + "当前的工作已经被处理，或者您没有执行此工作的权限。";
			}
			SendReturnObjs returnObjs = Dev2Interface.Node_SendWork(
					this.getFK_Flow(), this.getWorkID());
			resultMsg = returnObjs.ToMsgOfHtml();
			if (resultMsg.indexOf("@<a") > 0) {
				String kj = resultMsg.substring(0, resultMsg.indexOf("@<a"));
				resultMsg = resultMsg.substring(resultMsg.indexOf("@<a"))
						+ "<br/><br/>" + kj;
			}
			// 撤销单据
			int docindex = resultMsg
					.indexOf("@<img src='../../Img/FileType/doc.gif' />");
			if (docindex != -1) {
				String kj = resultMsg.substring(0, docindex);
				String kp = "";
				int nextdocindex = resultMsg.indexOf("@", docindex + 1);
				if (nextdocindex != -1) {
					kp = resultMsg.substring(nextdocindex);
				}
				resultMsg = kj + kp;
			}
			// 撤销 撤销本次发送
			int UnSendindex = resultMsg
					.indexOf("@<a href='../../MyFlowInfo.jsp?DoType=UnSend");
			if (UnSendindex != -1) {
				String kj = resultMsg.substring(0, UnSendindex);
				String kp = "";
				int nextUnSendindex = resultMsg.indexOf("@", UnSendindex + 1);
				if (nextUnSendindex != -1) {
					kp = resultMsg.substring(nextUnSendindex);
				}
				resultMsg = kj
						+ "<a href='javascript:UnSend();'><img src='../../Img/UnDo.gif' border=0/>撤销本次发送</a>"
						+ kp;
			}

			resultMsg = resultMsg.replace("指定特定的处理人处理", "指定人员");
			resultMsg = resultMsg.replace("发手机短信提醒他(们)", "短信通知");
			resultMsg = resultMsg.replace("撤销本次发送", "撤销案件");
			resultMsg = resultMsg.replace("新建流程", "发起案件");
			resultMsg = resultMsg.replace("。", "");
			resultMsg = resultMsg.replace("，", "");

			resultMsg = resultMsg.replace("@下一步",
					"<br/><br/>&nbsp;&nbsp;&nbsp;下一步");
			resultMsg = "success|<br/>"
					+ resultMsg.replace("@", "&nbsp;&nbsp;&nbsp;");

			// /处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
			// 这里有两种情况
			// * 1，从中间的节点，通过批量处理，也就是说合并审批处理的情况，这种情况子流程需要执行到下一步。
			// 2，从流程已经完成，或者正在运行中，也就是说合并审批处理的情况.
			try {
				// 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
				BP.WF.Glo.DealBuinessAfterSendWork(this.getFK_Flow(),
						this.getWorkID(), this.getDoFunc(), getWorkIDs(),
						this.getCFlowNo(), 0, null);
			} catch (RuntimeException ex) {
				resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
				return resultMsg;
			}
			// 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
		}
		return resultMsg;
	}

	/**
	 * 执行发送到指定节点
	 * 
	 * @return
	 */
	private String SendCaseToNode() {
		int ToNode = Integer.parseInt(getUTF8ToString("ToNode"));
		String resultMsg = "";
		try {
			if (Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(),
					this.getFK_Node(), this.getWorkID(), WebUser.getNo()) == false) {
				return resultMsg = "error|您好：" + WebUser.getNo() + ", "
						+ WebUser.getName() + "当前的工作已经被处理，或者您没有执行此工作的权限。";
			}
			SendReturnObjs returnObjs = Dev2Interface.Node_SendWork(
					this.getFK_Flow(), this.getWorkID(), ToNode, null);
			resultMsg = returnObjs.ToMsgOfHtml();
			if (resultMsg.indexOf("@<a") > 0) {
				String kj = resultMsg.substring(0, resultMsg.indexOf("@<a"));
				resultMsg = resultMsg.substring(resultMsg.indexOf("@<a"))
						+ "<br/><br/>" + kj;
			}
			// 撤销单据
			int docindex = resultMsg
					.indexOf("@<img src='../../Img/FileType/doc.gif' />");
			if (docindex != -1) {
				String kj = resultMsg.substring(0, docindex);
				String kp = "";
				int nextdocindex = resultMsg.indexOf("@", docindex + 1);
				if (nextdocindex != -1) {
					kp = resultMsg.substring(nextdocindex);
				}
				resultMsg = kj + kp;
			}
			// 撤销 撤销本次发送
			int UnSendindex = resultMsg
					.indexOf("@<a href='MyFlowInfo.jsp?DoType=UnSend");
			if (UnSendindex != -1) {
				String kj = resultMsg.substring(0, UnSendindex);
				String kp = "";
				int nextUnSendindex = resultMsg.indexOf("@", UnSendindex + 1);
				if (nextUnSendindex != -1) {
					kp = resultMsg.substring(nextUnSendindex);
				}
				resultMsg = kj
						+ "<a href='javascript:UnSend();'><img src='../../Img/UnDo.gif' border=0/>撤销本次发送</a>"
						+ kp;
			}

			resultMsg = resultMsg.replace("指定特定的处理人处理", "指定人员");
			resultMsg = resultMsg.replace("发手机短信提醒他(们)", "短信通知");
			resultMsg = resultMsg.replace("撤销本次发送", "撤销案件");
			resultMsg = resultMsg.replace("新建流程", "发起案件");
			resultMsg = resultMsg.replace("。", "");
			resultMsg = resultMsg.replace("，", "");

			resultMsg = resultMsg.replace("@下一步",
					"<br/><br/>&nbsp;&nbsp;&nbsp;下一步");
			resultMsg = "success|<br/>"
					+ resultMsg.replace("@", "&nbsp;&nbsp;&nbsp;");

			// /处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
			// 这里有两种情况
			// * 1，从中间的节点，通过批量处理，也就是说合并审批处理的情况，这种情况子流程需要执行到下一步。
			// 2，从流程已经完成，或者正在运行中，也就是说合并审批处理的情况.
			try {
				// 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
				BP.WF.Glo.DealBuinessAfterSendWork(this.getFK_Flow(),
						this.getWorkID(), this.getDoFunc(), getWorkIDs(),
						this.getCFlowNo(), 0, null);
			} catch (RuntimeException ex) {
				resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
				return resultMsg;
			}
			// 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
		} catch (RuntimeException ex) {
			resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
		}
		return resultMsg;
	}

	/**
	 * 撤销发送
	 * 
	 * @return
	 */
	private String UnSendCase() {
		try {
			String FK_Flow = getUTF8ToString("FK_Flow");
			String WorkID = getUTF8ToString("WorkID");
			String str1 = BP.WF.Dev2Interface.Flow_DoUnSend(FK_Flow,
					Long.parseLong(WorkID));
			return "true";
		} catch (RuntimeException ex) {
			return "{message:'执行撤消失败，失败信息" + ex.getMessage() + "'}";
		}
	}
	/**
	 * 获取表单树
	 * 
	 * @return
	 */
	private FlowFormTrees appFlowFormTree = new FlowFormTrees();

	private String GetFlowFormTree() {
		String flowId = getUTF8ToString("flowId");
		String nodeId = getUTF8ToString("nodeId");

		// add root
		FlowFormTree root = new FlowFormTree();
		root.setNo("00");
		root.setParentNo("0");
		root.setName("目录");
		root.setNodeType("root");
		appFlowFormTree.clear();
		appFlowFormTree.AddEntity(root);

		// /#region 添加表单及文件夹
		// 节点表单
		//String tfModel = (String) SystemConfig.getAppSettings().get("TreeFrmModel");
		//Node nd = new Node(this.getFK_Node());


		FrmNodes frmNodes = new FrmNodes();
		QueryObject qo = new QueryObject(frmNodes);
		qo.AddWhere(FrmNodeAttr.FK_Node, nodeId);
		qo.addAnd();
		qo.AddWhere(FrmNodeAttr.FK_Flow, flowId);
		qo.addOrderBy(FrmNodeAttr.Idx);
		qo.DoQuery();
		//如果配置了启用关键字段，一下会判断绑定的独立表单中的关键字段是否有数据，没有就不会被显示
		// add  by  海南  zqp
		/*if (tfModel.equals("1")) {
			//针对合流点与分合流节点有效
			//获取独立表单的字段
			MapDatas mdes = new MapDatas();
			String mypks = "";
			if (nd.getIsStartNode() == false) {
				qo.addOrderBy(FrmNodeAttr.Idx);
				qo.DoQuery();
				for(int i=0;i<frmNodes.size();i++)	{
					FrmNode fn =(FrmNode) frmNodes.get(i);
					if (fn.getHisFrmType() == FrmType.FoolForm || fn.getHisFrmType() == FrmType.FreeFrm) {
						mdes.Retrieve(MapDataAttr.No, fn.getFK_Frm());
						//根据设置的关键字段是否有值，进行判断
						for(int j=0;j<mdes.size();j++){	
							MapData md=(MapData) mdes.get(j);
							Paras ps = new Paras();
							ps.SQL = "SELECT " + fn.getGuanJianZiDuan() + " FROM " + md.getPTable() + " WHERE " + " OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
							if (this.getFID() == 0) {
								ps.Add("OID", this.getWorkID());
							}
							else {
								ps.Add("OID", this.getFID());
							}
							try {
								DataTable dtmd = BP.DA.DBAccess.RunSQLReturnTable(ps);
								String dtVal = String.valueOf(dtmd.Rows.get(0).get("" + fn.getGuanJianZiDuan() + ""));
								if (StringHelper.isNullOrEmpty(dtVal)) {
									mypks = mypks + "'" + md.getNo() + "',";
								}
							}
							catch (RuntimeException ex) {
								mypks = mypks + "'" + md.getNo() + "',";
							}
						}
					}
				}
				mypks = StringHelper.trimEnd(mypks, ',');
				if (!StringHelper.isNullOrEmpty(mypks)) {
					//添加查询条件
					qo = new QueryObject(frmNodes);
					qo.AddWhere(FrmNodeAttr.FK_Node, this.getFK_Node());
					qo.addAnd();
					qo.AddWhere(FrmNodeAttr.FK_Flow, this.getFK_Flow());
					qo.addAnd();
					qo.AddWhere(FrmNodeAttr.FK_Frm + " not in(" + mypks + ")");
					qo.addOrderBy(FrmNodeAttr.Idx);
					qo.DoQuery();
				}

			}
			else {
				qo.addOrderBy(FrmNodeAttr.Idx);
				qo.DoQuery();
			}
		}
		else {
			qo.addOrderBy(FrmNodeAttr.Idx);
			qo.DoQuery();
		}*/

		// 文件夹
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Name);
		// 所有表单集合
		MapDatas mds = new MapDatas();
		// mds.Retrieve(MapDataAttr.AppType, (int) AppType.Application);
		mds.Retrieve(MapDataAttr.AppType, AppType.Application.getValue());
		for (FrmNode frmNode : frmNodes.ToJavaList()) {
			for (MapData md : mds.ToJavaList()) {
				if (!frmNode.getFK_Frm().equals(md.getNo())) {
					continue;
				}

				for (SysFormTree formTree : formTrees.ToJavaList()) {
					if (!md.getFK_FormTree().equals(formTree.getNo())) {
						continue;
					}

					if (!appFlowFormTree.Contains("No", formTree.getNo())) {
						FlowFormTree nodeFolder = new FlowFormTree();
						nodeFolder.setNo(formTree.getNo());
						nodeFolder.setParentNo(root.getNo());
						nodeFolder.setName(formTree.getName());
						nodeFolder.setNodeType("folder");
						appFlowFormTree.AddEntity(nodeFolder);
					}
				}
				// 检查必填项
				boolean IsNotNull = false;
				FrmFields formFields = new FrmFields();
				QueryObject obj = new QueryObject(formFields);
				obj.AddWhere(FrmFieldAttr.FK_Node, nodeId);
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.FK_MapData, md.getNo());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.IsNotNull, "1");
				obj.DoQuery();
				if (formFields != null && formFields.size() > 0) {
					IsNotNull = true;
				}

				FlowFormTree nodeForm = new FlowFormTree();
				nodeForm.setNo(md.getNo());
				nodeForm.setParentNo(md.getFK_FormTree());
				nodeForm.setName(md.getName());
				nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
				nodeForm.setIsEdit(frmNode.getIsEdit() ? "1" : "0");
				appFlowFormTree.AddEntity(nodeForm);
			}
		}
		// 扩展工具，显示位置为表单树类型
		NodeToolbars extToolBars = new NodeToolbars();
		QueryObject info = new QueryObject(extToolBars);
		info.AddWhere(NodeToolbarAttr.FK_Node, nodeId);
		info.addAnd();
		info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Tree);
		info.DoQuery();

		for (NodeToolbar item : extToolBars.ToJavaList()) {
			String url = "";
			if (StringHelper.isNullOrEmpty(item.getUrl())) {
				continue;
			}

			url = item.getUrl();

			FlowFormTree formTree = new FlowFormTree();
			formTree.setNo(String.valueOf(item.getOID()));
			formTree.setParentNo("01");
			formTree.setName(item.getTitle());
			// 这个地方怎么回事====================
			formTree.setNodeType("tools|0");
			if (!StringHelper.isNullOrEmpty(item.getTarget())
					&& item.getTarget().toUpperCase().equals("_BLANK")) {
				formTree.setNodeType("tools|1");
			}

			formTree.setUrl(url);
			appFlowFormTree.AddEntity(formTree);
		}
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

	public final void TansEntitiesToGenerTree(Entities ens, String rootNo,
			String checkIds) {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityMultiTree root = (EntityMultiTree) ((tempVar instanceof EntityMultiTree) ? tempVar
				: null);
		if (root == null) {
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");

		// attributes
		FlowFormTree formTree = (FlowFormTree) ((root instanceof FlowFormTree) ? root
				: null);
		if (formTree != null) {
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");

			appendMenus.append(",\"attributes\":{\"NodeType\":\""
					+ formTree.getNodeType() + "\",\"IsEdit\":\""
					+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
		}
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityMultiTree parentEn, Entities ens,
			String checkIds) {
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);
		;

		appendMenuSb.append("[");
		for (Entity obj : ens.ToJavaListEn()) {
			EntityMultiTree item = (EntityMultiTree) obj;
			if (!item.getParentNo().equals(parentEn.getNo())) {
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ",")) {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":true");
			} else {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":false");
			}

			FlowFormTree formTree = (FlowFormTree) ((item instanceof FlowFormTree) ? item
					: null);
			if (formTree != null) {
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\""
						+ formTree.getNodeType() + "\",\"IsEdit\":\""
						+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");

				// 图标
				if (formTree.getNodeType().equals("form|0")) {
					ico = "form0";
				}
				if (formTree.getNodeType().equals("form|1")) {
					ico = "form1";
				}
				if (formTree.getNodeType().contains("tools")) {
					ico = "icon-4";
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
}
