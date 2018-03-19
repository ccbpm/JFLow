package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.En.QueryObject;
import BP.Tools.StringHelper;
import BP.WF.BatchRole;
import BP.WF.Node;
import BP.WF.Template.BtnLab;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.ShowWhere;

public class DefaultModel extends BaseModel {

	public long WorkID;
	public String CWorkID;
	public Node currND = null;
	public String page;
	public boolean IsPostBack;
	private boolean mm3IsVisible;
	public String title;
	private StringBuilder mm3 ;
	public StringBuilder toolBars;
	public DefaultModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		mm3 = new StringBuilder();
		toolBars=new StringBuilder();
	}
	
	public String getMm3() {
		if(mm3IsVisible){
			return mm3.toString();
		}
		return "";
	}
	

	/*
	 * 这个BaseModel里面有 public final String getFK_Flow() { String s =
	 * this.Request.QueryString["FK_Flow"]; if
	 * (StringHelper.isNullOrEmpty(s)) { s =
	 * this.Request.QueryString["PFlowNo"]; } return s; }
	 */
	public final String getFromNode() {
		// return this.Request.QueryString["FromNode"];
		return getParameter("FromNode");
	}

	/*
	 * 这个方法baseModel里面有 public final long getWorkID() { if (ViewState["WorkID"]
	 * == null) { if (this.Request.QueryString["WorkID"] == null) { return 0; }
	 * else { return Long.parseLong(this.Request.QueryString["WorkID"]); } }
	 * else { return Long.parseLong(ViewState["WorkID"].toString()); } }
	 */
	public final void setWorkID(long value) {
		// ViewState["WorkID"] = value;
		WorkID = value;
	}

	private int _FK_Node = 0;

	/**
	 * 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	 */
	public int getFK_Node() {
		// String fk_nodeReq = this.Request.QueryString["FK_Node"];
		String fk_nodeReq = getParameter("FK_Node");
		if (StringHelper.isNullOrEmpty((fk_nodeReq))) {
			// fk_nodeReq = this.Request.QueryString["NodeID"];
			fk_nodeReq = getParameter("NOdeID");
		}

		if (StringHelper.isNullOrEmpty(fk_nodeReq) == false) {
			return Integer.parseInt(fk_nodeReq);
		}

		if (_FK_Node == 0) {
			// if (this.Request.QueryString["WorkID"] != null) {
			if (getParameter("WordID") != null) {
				String sql = "SELECT FK_Node from  WF_GenerWorkFlow where WorkID="
						+ this.getWorkID();
				_FK_Node 
				= DBAccess.RunSQLReturnValInt(sql, 0);
				if (_FK_Node == 0) {
					_FK_Node = Integer.parseInt(this.getFK_Flow() + "01");
				}
			} else {
				_FK_Node = Integer.parseInt(this.getFK_Flow() + "01");
			}
		}
		return _FK_Node;
	}
	public final int getPWorkID() {
		try {
			String s = getParameter("PWorkID");
			if (StringHelper.isNullOrEmpty(s) == true) {
				s=getParameter("PworkId");
			}
			if (StringHelper.isNullOrEmpty(s) == true) {
				s = "0";
			}
			return Integer.parseInt(s);
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public final long getCWorkID() {
		if(CWorkID==null){
			if(getParameter("CWorkID")==null){
				return 0;
			} else {
				return Long.parseLong(getParameter("CWorkID"));
			}
		} else {
			return Long.parseLong(CWorkID.toString());
		}
	}

	public final void setCWorkID(long value) {
		CWorkID=Long.toString(value);
	}

	
	public void Page_Load() {
		this.currND=new Node(getFK_Node());
		title = "第" + this.currND.getStep() + "步:" + this.currND.getName();
		if (IsPostBack == false) {
			InitToolsBar();
		}
	}

	/**
	 * 初始化工具栏
	 */
	private void InitToolsBar() {
		String toolsDefault = "";
		String extMenuHTML = "";
		int toolCount = 0;
		int alowToolCount = 8;
		BtnLab btnLab = new BtnLab(this.getFK_Node());
	
		// 发送
		if (btnLab.getSendEnable() && currND.getHisBatchRole() != BatchRole.Group) {
			toolsDefault += "<a id=\"send\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-send'\" onclick=\"EventFactory('send')\">"
					+ btnLab.getSendLab() + "</a>";
			toolCount++;
		}
		// 保存
		if (btnLab.getSaveEnable()) {
			toolsDefault += "<a id=\"save\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-save'\" onclick=\"EventFactory('save')\">"
					+ btnLab.getSaveLab() + "</a>";
			toolCount++;
		}
		// 子线程
		if (btnLab.getThreadEnable()) {
			toolsDefault += "<a id=\"childline\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-childline'\" onclick=\"EventFactory('childline')\">"
					+ btnLab.getThreadLab() + "</a>";
			toolCount++;
		}
		// 跳转
		if (btnLab.getJumpWayEnable()) {
			toolsDefault += "<a id=\"jumpNode\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-redo'\"  onclick=\"EventFactory('jumpway')\">"
					+ btnLab.getJumpWayLab() + "</a>";
			toolCount++;
		}
		// 退回
		if (btnLab.getReturnEnable()) {
			toolsDefault += "<a id=\"turnBack\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-back'\" onclick=\"EventFactory('backcase')\">"
					+ btnLab.getReturnLab() + "</a>";
			toolCount++;
		}
		// 抄送
		if (btnLab.getCCRole()!=null) {
			toolsDefault += "<a id=\"A1\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-ccsmall'\" onclick=\"EventFactory('CC')\">"
					+ btnLab.getCCLab() + "</a>";
			toolCount++;
		}
		// 移交
		if (btnLab.getShiftEnable()) {
			toolsDefault += "<a id=\"ShiftLab\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-transfer'\" onclick=\"EventFactory('Shift')\">"
					+ btnLab.getShiftLab() + "</a>";
			toolCount++;
		}
		// 删除
		if (btnLab.getDeleteEnable() != 0) {
			toolsDefault += "<a id=\"DeleteLab\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-delete'\"  onclick=\"EventFactory('Del')\">"
					+ "删除" + "</a>";
			toolCount++;
		}
		// 结束
		if (btnLab.getEndFlowEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-no'\" onclick=\"EventFactory('endflow')\">"
						+ btnLab.getEndFlowLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"EndFlow\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-no'\" onclick=\"EventFactory('endflow')\">"
						+ btnLab.getEndFlowLab() + "</a>";
			}
			toolCount++;
		}
		// 打印
		if (btnLab.getPrintDocEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-print'\" onclick=\"EventFactory('printdoc')\">"
						+ btnLab.getPrintDocLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"PrintDoc\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-print'\" onclick=\"EventFactory('printdoc')\">"
						+ btnLab.getPrintDocLab() + "</a>";
			}
			toolCount++;
		}
		// 轨迹
		if (btnLab.getTrackEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-flowmap'\" onclick=\"EventFactory('showchart')\">"
						+ btnLab.getTrackLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"Track\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-flowmap'\" onclick=\"EventFactory('showchart')\">"
						+ btnLab.getTrackLab() + "</a>";
			}
			toolCount++;
		}
		// 挂起
		if (btnLab.getHungEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-hungup'\" onclick=\"EventFactory('hungup')\">"
						+ btnLab.getHungLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"Hung\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-hungup'\" onclick=\"EventFactory('hungup')\">"
						+ btnLab.getHungLab() + "</a>";
			}
			toolCount++;
		}
		// 接收人
		if (btnLab.getSelectAccepterEnable() == 1) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-person'\" onclick=\"EventFactory('selectaccepter')\">"
						+ btnLab.getSelectAccepterLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"SelectAccepter\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-person'\" onclick=\"EventFactory('selectaccepter')\">"
						+ btnLab.getSelectAccepterLab() + "</a>";
			}
			toolCount++;
		}
		// 查询
		if (btnLab.getSearchEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-search'\" onclick=\"EventFactory('search')\">"
						+ btnLab.getSearchLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"Search\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-search'\" onclick=\"EventFactory('search')\">"
						+ btnLab.getSearchLab() + "</a>";
			}
			toolCount++;
		}
		// 审核
		if (btnLab.getWorkCheckEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-note'\" onclick=\"EventFactory('workcheck')\">"
						+ btnLab.getWorkCheckLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"WorkCheck\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-note'\" onclick=\"EventFactory('workcheck')\">"
						+ btnLab.getWorkCheckLab() + "</a>";
			}
			toolCount++;
		}
		// 批量审核
		if (btnLab.getBatchEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-note'\" onclick=\"EventFactory('batchworkcheck')\">"
						+ btnLab.getBatchLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"BatchWorkCheck\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-note'\" onclick=\"EventFactory('batchworkcheck')\">"
						+ btnLab.getBatchLab() + "</a>";
			}
			toolCount++;
		}
		// 加签
		if (btnLab.getAskforEnable()) {
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-tag'\" onclick=\"EventFactory('askfor')\">"
						+ btnLab.getAskforLab() + "</div>";
			} else {
				toolsDefault += "<a id=\"Askfor\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-tag'\" onclick=\"EventFactory('askfor')\">"
						+ btnLab.getAskforLab() + "</a>";
			}
			toolCount++;
		}
		// 超出范围增加到菜单里面
		if (toolCount > alowToolCount) {
			// extMenuHTML +=
			// "<div data-options=\"plain:true,iconCls:'icon-tag'\" onclick=\"addTab('ycfj','已传附件','/app/function/office/iofficefj.aspx?WorkID="
			// + this.WorkID + "&UserNo=" + WebUser.getNo() + "&FID=" + this.FID +
			// "');\">已传附件</div>";
			// 项目备案数据
			// extMenuHTML +=
			// "<div data-options=\"plain:true,iconCls:'icon-tag'\" onclick=\"addTab('basj','备案数据','/DataBak.aspx.aspx?WorkID="
			// + this.WorkID + "&UserNo=" + WebUser.getNo() + "&FID=" + this.FID +
			// "');\">备案数据</div>";
		} else {
			// toolsDefault +=
			// "<a id=\"ycfj\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-tag'\" onclick=\"addTab('ycfj','已传附件','/app/function/office/iofficefj.aspx?WorkID="
			// + this.WorkID + "&UserNo=" + WebUser.getNo() + "&FID=" + this.FID +
			// "');\">已传附件</a>";
			// 一键签名
			// toolsDefault +=
			// "<a id=\"ycfj\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-note'\" onclick=\"EventFactory('Sign')\">一键签名</a>";
			// 备案数据
			// toolsDefault +=
			// "<a id=\"DataBak\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-tag'\" onclick=\"addTab('basj','备案数据','/DataBak.aspx.aspx?WorkID="
			// + this.WorkID + "&UserNo=" + WebUser.getNo() + "&FID=" + this.FID +
			// "');\">备案数据</a>";
		}
		// 扩展工具，显示位置为工具栏类型
		NodeToolbars extToolBars = new NodeToolbars();
		QueryObject info = new QueryObject(extToolBars);
		info.AddWhere(NodeToolbarAttr.FK_Node, this.getFK_Node());
		info.addAnd();
		//info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Toolbar.getValue());
		info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Toolbar.getValue());
		info.DoQuery();

		for(NodeToolbar item :extToolBars.ToJavaList()){
			String url = "";
			if (StringHelper.isNullOrEmpty(item.getUrl())) {
				continue;
			}

			String urlExt = this.getRequestParas();
			// urlExt = "WorkID=" + this.WorkID + "&FK_Flow=" + this.FK_Flow +
			// "&FK_Node=" + this.FK_Node + "&UserNo=" + WebUser.getNo() + "&FID=" +
			// this.FID + "&SID=" + WebUser.SID + "&CWorkID=" + this.CWorkID;
			url = item.getUrl();
			if (url.contains("?")) {
				url += urlExt;
			} else {
				url += "?" + urlExt;
			}
			// 超出范围增加到菜单里面
			if (toolCount > alowToolCount) {
				extMenuHTML += "<div data-options=\"plain:true,iconCls:'icon-new'\" onclick=\"WinOpenPage('"
						+ item.getTarget()
						+ "','"
						+ url
						+ "','"
						+ item.getTitle()
						+ "')\">" + item.getTitle() + "</div>";
			} else {
				toolsDefault += "<a target=\""
						+ item.getTarget()
						+ "\" href=\""
						+ url
						+ "\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-new'\">"
						+ item.getTitle() + "</a>";
				toolCount++;
			}
		}

		// 判断是否出现添加菜单
		if (toolCount > alowToolCount) {
			mm3IsVisible = true;
			
			toolsDefault += "<a href=\"javascript:void(0)\" id=\"mb3\" class=\"easyui-menubutton\" data-options=\"menu:'#mm3',plain:true,iconCls:'icon-add'\"></a>";
		}
		toolsDefault += "<a id=\"closeWin\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-no'\" onclick=\"EventFactory('closeWin')\">关闭</a>";
		// 添加内容
		//this.toolBars.InnerHtml = toolsDefault;
		toolBars.append(toolsDefault);
		//this.mm3.InnerHtml = extMenuHTML;
		mm3.append(extMenuHTML);
	}

	public final String getRequestParas() {
		String urlExt = "";
		String rawUrl =get_request().getRequestURL().toString();
		rawUrl = "&" + rawUrl.substring(rawUrl.indexOf('?') + 1);
		String[] paras = rawUrl.split("[&]", -1);
		for (String para : paras) {
			if (para == null || para.equals("") || para.contains("=") == false) {
				continue;
			}
			urlExt += "&" + para;
		}
		return urlExt;
	}
}
