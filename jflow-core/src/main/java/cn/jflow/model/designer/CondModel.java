package cn.jflow.model.designer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.En.QueryObject;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.StartWorkAttr;
import BP.WF.WorkAttr;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondOrAnd;
import BP.WF.Template.Conds;
import BP.WF.Template.ConnDataFrom;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;

public class CondModel {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public UiFatory ui = null;
	public String basePath = "";

	public CondModel(HttpServletRequest req, HttpServletResponse res,
			String basePath) {
		this.request = req;
		this.response = res;
		this.basePath = basePath;
	}

	// #region 属性
	// / <summary>
	// / 主键
	// / </summary>
	private String MyPK;

	public String getMyPK() {
		return request.getParameter("MyPK");
	}

	public void setMyPK(String myPK) {
		MyPK = myPK;
	}

	// / <summary>
	// / 流程编号
	// / </summary>
	private String FK_Flow;

	public String getFK_Flow() {
		return request.getParameter("FK_Flow");
	}

	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}

	private String FK_Attr;

	public String getFK_Attr() {
		String s = request.getParameter("FK_Attr") == null ? "" : request
				.getParameter("FK_Attr");
		if (s == null || "".equals(s) || s.equals("null")) {
			try {
				s = this.getDDL_Attr().getSelectedItemStringVal();
			} catch (Exception e) {
				return null;
			}
		}
		if ("".equals(s))
			return null;
		return s;
	}

	public void setFK_Attr(String fK_Attr) {
		FK_Attr = fK_Attr;
	}

	// / <summary>
	// / 节点
	// / </summary>
	private int FK_Node;

	public int getFK_Node() {
		try {
			return Integer.parseInt(request.getParameter("FK_Node"));
		} catch (Exception e) {
			return this.getFK_MainNode();
		}
	}

	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}

	private int FK_MainNode;

	public int getFK_MainNode() {
		// suxd
		// 问题：原始判断(request.getParameter("FK_MainNode") == null)成立时，
		// 如果request.getParameter("FK_MainNode")==""时Integer.parseInt("")会报错
		// 解决：判断request.getParameter("FK_MainNode") == null 替换为
		// StringUtils.isEmpty(request.getParameter("FK_MainNode"))
		return StringUtils.isEmpty(request.getParameter("FK_MainNode")) ? 0
				: Integer.parseInt(request.getParameter("FK_MainNode"));
	}

	public void setFK_MainNode(int fK_MainNode) {
		FK_MainNode = fK_MainNode;
	}

	private int ToNodeID;

	public int getToNodeID() {
		try {
			return request.getParameter("ToNodeID") == null ? 0 : Integer
					.parseInt(request.getParameter("ToNodeID"));
		} catch (Exception e) {
			return 0;
		}
	}

	public void setToNodeID(int toNodeID) {
		ToNodeID = toNodeID;
	}

	// / <summary>
	// / 执行类型
	// / </summary>
	private int HisCondType;

	public int getHisCondType() {
		return request.getParameter("CondType") == null ? 0 : Integer
				.parseInt(request.getParameter("CondType").trim());
	}

	public void setHisCondType(int hisCondType) {
		HisCondType = hisCondType;
	}

	private String GetOperVal;

	public String getGetOperVal() {
		if (ui.GetUIByID("TB_Val") != null) {
			return ((TextBox) ui.GetUIByID("TB_Val")).getText();
		}
		return ((DDL) ui.GetUIByID("DDL_Val")).getSelectedItemStringVal();
	}

	public void setGetOperVal(String getOperVal) {
		GetOperVal = getOperVal;
	}

	// public string GetOperVal
	// {
	// get
	// {
	// if (this.IsExit("TB_Val"))
	// return this.GetTBByID("TB_Val").Text;
	// return this.GetDDLByID("DDL_Val").SelectedItemStringVal;
	// }
	// }
	private String GetOperValText;

	public String getGetOperValText() {
		if (ui.GetUIByID("TB_Val") != null) {
			return ((TextBox) ui.GetUIByID("TB_Val")).getText();
		}
		return ((DDL) ui.GetUIByID("DDL_Val")).getSelectedItem().getText();
	}

	public void setGetOperValText(String getOperValText) {
		GetOperValText = getOperValText;
	}

	// public string GetOperValText
	// {
	// get
	// {
	// if (this.IsExit("TB_Val"))
	// return this.GetTBByID("TB_Val").Text;
	// return this.GetDDLByID("DDL_Val").SelectedItem.Text;
	// }
	// }

	public void Page_Load() {
		ui = new UiFatory();
		String DoType = request.getParameter("DoType") == null ? "" : request
				.getParameter("DoType");
		if (DoType.equals("Del")) {
			Cond nd = new Cond(this.getMyPK());
			nd.Delete();
			try {
				response.sendRedirect("Cond.jsp?CondType=" + getHisCondType()
						+ "&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode="
						+ nd.getNodeID() + "&FK_Node=" + this.getFK_MainNode()
						+ "&ToNodeID=" + nd.getToNodeID());
			} catch (IOException e) {
				e.printStackTrace();
			}
			// this.Response.Redirect("Cond.aspx?CondType=" +
			// (int)this.HisCondType + "&FK_Flow=" + this.FK_Flow +
			// "&FK_MainNode=" + nd.NodeID + "&FK_Node=" + this.FK_MainNode +
			// "&ToNodeID=" + nd.ToNodeID, true);
			return;
		}

		this.BindCond();
		if (this.getFK_Attr() == null)
			this.setFK_Attr(this.getDDL_Attr().getSelectedItemStringVal());
	}

	/**
	 * 
	 */
	public void BindCond() {
		String msg = "";
		String note = "";
		int idx = 0;

		Cond cond = new Cond();
		cond.setMyPK(this.getMyPK());
		if (cond.RetrieveFromDBSources() == 0) {
			if (this.getFK_Attr() != null)
				cond.setFK_Attr(this.getFK_Attr());
			if (this.getFK_MainNode() != 0)
				cond.setNodeID(this.getFK_MainNode());
			if (this.getFK_Node() != 0)
				cond.setFK_Node(this.getFK_Node());
			if (this.getFK_Flow() != null)
				cond.setFK_Flow(this.getFK_Flow());
		}
		// this.AddTable("border=0 widht='500px'");
		ui.append(BaseModel
				.AddTable("class='Table' cellpadding='2' cellspacing='2' style='width:100%;'"));
		// ui.append(BaseModel.AddCaptionMsg("流程完成条件"));
		ui.append(BaseModel.AddTR());
		ui.append(BaseModel.AddTH("序号"));
		ui.append(BaseModel.AddTH("项目"));
		ui.append(BaseModel.AddTH("采集"));
		ui.append(BaseModel.AddTH("描述"));
		ui.append(BaseModel.AddTREnd());

		ui.append(BaseModel.AddTR());
		ui.append(BaseModel.AddTD("align='center'", idx++));
		ui.append(BaseModel.AddTD("节点"));
		Nodes nds = new Nodes(cond.getFK_Flow());
		Nodes ndsN = new Nodes();
		for (int i = 0; i < nds.size(); i++) {
			Node mynd = (Node) nds.get(i);
			ndsN.AddEntity(mynd);
		}
		// for (Node mynd : nds)
		// {
		// ndsN.AddEntity(mynd);
		// }
		DDL ddl = ui.creatDDL("DDL_Node");
		// ddl.ID = "DDL_Node";
		ddl.BindEntities(ndsN, "NodeID", "Name");
		ddl.SetSelectItem(cond.getFK_Node());
		// ddl.AutoPostBack = true;
		ddl.addAttr("onchange", "ddl_SelectedIndexChanged()");
		// ddl.SelectedIndexChanged += new
		// EventHandler(ddl_SelectedIndexChanged);
		ui.append(BaseModel.AddTD(ddl));
		ui.append(BaseModel.AddTD("节点"));
		ui.append(BaseModel.AddTREnd());

		// 属性/字段
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData,
				"ND" + ddl.getSelectedItemStringVal());

		MapAttrs attrNs = new MapAttrs();
		for (int i = 0; i < attrs.size(); i++) {
			MapAttr attr = (MapAttr) attrs.get(i);
			if (attr.getIsBigDoc())
				continue;

			if (attr.getKeyOfEn() != null) {
				if (attr.getKeyOfEn().equals("Title")) {
				} else if (attr.getKeyOfEn().equals("FK_Emp")) {
				} else if (attr.getKeyOfEn().equals("MyNum")) {
				} else if (attr.getKeyOfEn().equals("FK_NY")) {
				} else if (attr.getKeyOfEn().equals(WorkAttr.Emps)) {
				} else if (attr.getKeyOfEn().equals(WorkAttr.OID)) {
				} else if (attr.getKeyOfEn().equals(StartWorkAttr.Rec)) {
				} else if (attr.getKeyOfEn().equals(StartWorkAttr.FID)) {
				} else {
				}
			}
			// switch (attr.getKeyOfEn())
			// {
			// case "Title":
			// //case "RDT":
			// //case "CDT":
			// case "FK_Emp":
			// case "MyNum":
			// case "FK_NY":
			// case WorkAttr.Emps:
			// case WorkAttr.OID:
			// case StartWorkAttr.Rec:
			// case StartWorkAttr.FID:
			// continue;
			// default:
			// break;
			// }
			attrNs.AddEntity(attr);
		}
		// for (MapAttr attr : attrs)
		// {
		// if (attr.IsBigDoc)
		// continue;
		//
		// switch (attr.KeyOfEn)
		// {
		// case "Title":
		// //case "RDT":
		// //case "CDT":
		// case "FK_Emp":
		// case "MyNum":
		// case "FK_NY":
		// case WorkAttr.Emps:
		// case WorkAttr.OID:
		// case StartWorkAttr.Rec:
		// case StartWorkAttr.FID:
		// continue;
		// default:
		// break;
		// }
		// attrNs.AddEntity(attr);
		// }
		ddl = ui.creatDDL("DDL_Attr");
		// ddl.setID = "DDL_Attr";
		if (attrNs.size() == 0) {
			Node nd = new Node(cond.getFK_Node());
			nd.RepareMap();
			ui.append(BaseModel.AddTR());
			ui.append(BaseModel.AddTD(idx++));
			ui.append(BaseModel.AddTD(""));
			ui.append(BaseModel.AddTD("colspan=2", "节点没有找到合适的条件"));
			ui.append(BaseModel.AddTREnd());
			ui.append(BaseModel.AddTableEnd());
			return;
		} else {
			ddl.BindEntities(attrNs, MapAttrAttr.MyPK, MapAttrAttr.Name);
			// ddl.AutoPostBack = true;
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged()");
			// ddl.SelectedIndexChanged += new
			// EventHandler(ddl_SelectedIndexChanged);
			ddl.SetSelectItem(cond.getFK_Attr());
		}

		ui.append(BaseModel.AddTR());
		ui.append(BaseModel.AddTD(idx++));
		ui.append(BaseModel.AddTD("属性/字段"));
		ui.append(BaseModel.AddTD(ddl));
		ui.append(BaseModel.AddTD(""));
		ui.append(BaseModel.AddTREnd());

		MapAttr attrS = new MapAttr(this.getDDL_Attr()
				.getSelectedItemStringVal());
		ui.append(BaseModel.AddTR());
		ui.append(BaseModel.AddTD(idx++));
		ui.append(BaseModel.AddTD("操作符"));
		ddl = ui.creatDDL("DDL_Oper");
		// ddl.ID = "DDL_Oper";
		switch (attrS.getLGType()) {
		case Enum:
		case FK:
			ddl.Items.add(new ListItem("=", "="));
			ddl.Items.add(new ListItem("<>", "<>"));
			break;
		case Normal:
			switch (attrS.getMyDataType()) {
			case BP.DA.DataType.AppString:
			case BP.DA.DataType.AppDate:
			case BP.DA.DataType.AppDateTime:
				ddl.Items.add(new ListItem("=", "="));
				ddl.Items.add(new ListItem("LIKE", "LIKE"));
				ddl.Items.add(new ListItem("<>", "<>"));
				break;
			case BP.DA.DataType.AppBoolean:
				ddl.Items.add(new ListItem("=", "="));
				break;
			default:
				ddl.Items.add(new ListItem("=", "="));
				ddl.Items.add(new ListItem(">", ">"));
				ddl.Items.add(new ListItem(">=", ">="));
				ddl.Items.add(new ListItem("<", "<"));
				ddl.Items.add(new ListItem("<=", "<="));
				ddl.Items.add(new ListItem("<>", "<>"));
				break;
			}
			break;
		default:
			break;
		}

		if (cond != null) {
			try {
				ddl.SetSelectItem(cond.getOperatorValueInt());
			} catch (Exception e) {
			}
		}
		ui.append(BaseModel.AddTD(ddl));
		ui.append(BaseModel.AddTD(""));
		ui.append(BaseModel.AddTREnd());
		switch (attrS.getLGType()) {
		case Enum:
			ui.append(BaseModel.AddTR());
			ui.append(BaseModel.AddTD(idx++));
			ui.append(BaseModel.AddTD("值"));
			ddl = ui.creatDDL("DDL_Val");
			// ddl.sID = "DDL_Val";
			ddl.BindSysEnum(attrS.getUIBindKey());
			if (cond != null) {
				try {
					ddl.SetSelectItem(cond.getOperatorValueInt());
				} catch (Exception e) {
				}
			}
			ui.append(BaseModel.AddTD(ddl));
			ui.append(BaseModel.AddTD(""));
			ui.append(BaseModel.AddTREnd());
			break;
		case FK:
			ui.append(BaseModel.AddTR());
			ui.append(BaseModel.AddTD(idx++));
			ui.append(BaseModel.AddTD("值"));

			ddl = ui.creatDDL("DDL_Val");
			// ddl.ID = "DDL_Val";
			ddl.BindEntities(attrS.getHisEntitiesNoName());
			if (cond != null) {
				try {
					ddl.SetSelectItem(cond.getOperatorValueStr());
				} catch (Exception e) {
				}
			}
			ui.append(BaseModel.AddTD(ddl));
			ui.append(BaseModel.AddTD(""));
			ui.append(BaseModel.AddTREnd());
			break;
		default:
			if (attrS.getMyDataType() == BP.DA.DataType.AppBoolean) {
				ui.append(BaseModel.AddTR());
				ui.append(BaseModel.AddTD(idx++));
				ui.append(BaseModel.AddTD("值"));
				ddl = ui.creatDDL("DDL_Val");
				// ddl.ID = "DDL_Val";
				ddl.BindAppYesOrNo(0);
				if (cond != null) {
					try {
						ddl.SetSelectItem(cond.getOperatorValueInt());
					} catch (Exception e) {
					}
				}
				ui.append(BaseModel.AddTD(ddl));
				ui.append(BaseModel.AddTD());
				ui.append(BaseModel.AddTREnd());
			} else {
				ui.append(BaseModel.AddTR());
				ui.append(BaseModel.AddTD(idx++));
				ui.append(BaseModel.AddTD("值"));
				TextBox tb = ui.creatTextBox("TB_Val");
				// tb.ID = "TB_Val";
				if (cond != null)
					tb.setText(cond.getOperatorValueStr());
				ui.append(BaseModel.AddTD(tb));
				ui.append(BaseModel.AddTD());
				ui.append(BaseModel.AddTREnd());
			}
			break;
		}

		Conds conds = new Conds();
		QueryObject qo = new QueryObject(conds);
		qo.AddWhere(CondAttr.NodeID, this.getFK_MainNode());
		qo.addAnd();
		qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.NodeForm.getValue());
		qo.addAnd();
		qo.AddWhere(CondAttr.CondType, this.getHisCondType());

		if (this.getToNodeID() != 0) {
			qo.addAnd();
			qo.AddWhere(CondAttr.ToNodeID, this.getToNodeID());
		}
		int num = qo.DoQuery();

		ui.append(BaseModel.AddTableEnd());
		ui.append(BaseModel.AddBR());
		ui.append(BaseModel.AddSpace(1));

		LinkButton btn = ui.creatLinkButton(false, "Btn_SaveAnd", "保存为And条件");
		btn.SetDataOption("iconCls", "icon-save");
		btn.setHref("onAnd()");
		// btn.Click += new EventHandler(btn_Save_Click);
		ui.append(btn);
		ui.append(BaseModel.AddSpace(1));

		btn = ui.creatLinkButton(false, "Btn_SaveOr", "保存为Or条件");
		btn.SetDataOption("iconCls", "icon-save");
		btn.setHref("onOr()");
		// btn.Click += new EventHandler(btn_Save_Click);
		ui.append(btn);
		ui.append(BaseModel.AddSpace(1));

		btn = ui.creatLinkButton(false, NamesOfBtn.Delete.getCode(), "全部删除");
		btn.attributes.put("onclick", " return confirm('您确定要全部删除吗？');");
		btn.setHref("deletetable()");
		// btn.Click += new EventHandler(btn_Save_Click);
		ui.append(btn);

		ui.append(BaseModel.AddBR());
		ui.append(BaseModel.AddBR());

		if (num == 0)
			return;

		// #region 条件
		ui.append(BaseModel
				.AddTable("class='Table' cellpadding='2' cellspacing='2' style='width:100%;'"));
		ui.append(BaseModel.AddTR());
		ui.append(BaseModel.AddTD("class='GroupTitle'", "序"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "节点"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "字段的英文名"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "字段的中文名"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "操作符"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "值"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "运算关系"));
		ui.append(BaseModel.AddTD("class='GroupTitle'", "操作"));
		ui.append(BaseModel.AddTREnd());

		int i = 0;
		for (int j = 0; j < conds.size(); j++) {
			Cond mync = (Cond) conds.get(i);
			// mync.setOperatorValueT(request.getParameter("tb"));
			if (mync.getHisDataFrom() != ConnDataFrom.NodeForm)
				continue;

			i++;

			ui.append(BaseModel.AddTR());
			ui.append(BaseModel.AddTDIdx(i));
			// this.AddTD(mync.HisDataFrom.ToString());
			ui.append(BaseModel.AddTD(mync.getFK_NodeT()));
			ui.append(BaseModel.AddTD(mync.getAttrKey()));
			ui.append(BaseModel.AddTD(mync.getAttrName()));
			ui.append(BaseModel.AddTDCenter(mync.getFK_Operator()));
			ui.append(BaseModel.AddTD(mync.getOperatorValueStr()));
			if (mync.getCondOrAnd().getValue() == CondOrAnd.ByAnd.getValue())
				ui.append(BaseModel.AddTD("AND"));
			else
				ui.append(BaseModel.AddTD("OR"));

			// if (num > 1)
			// this.AddTD(mync.HisConnJudgeWayT);
			ui.append(BaseModel.AddTD("<a href='"
					+ basePath
					+ "WF/Admin/Cond.jsp?MyPK="
					+ mync.getMyPK()
					+ "&CondType="
					+ getHisCondType()
					+ "&FK_Flow="
					+ this.getFK_Flow()
					+ "&FK_Attr="
					+ mync.getFK_Attr()
					+ "&FK_MainNode="
					+ mync.getNodeID()
					+ "&OperatorValue="
					+ mync.getOperatorValueStr()
					+ "&FK_Node="
					+ mync.getFK_Node()
					+ "&DoType=Del&ToNodeID="
					+ mync.getToNodeID()
					+ "' class='easyui-linkbutton' data-options=\"iconCls:'icon-remove'\" onclick=\"return confirm('确定删除此条件吗?')\">删除</a>"));
			// ui.append(BaseModel.AddTD("<a href='Cond.jsp?MyPK="
			// + mync.getMyPK()
			// + "&CondType="
			// + this.getHisCondType()
			// + "&FK_Flow="
			// + this.getFK_Flow()
			// + "&FK_Attr="
			// + mync.getFK_Attr()
			// + "&FK_MainNode="
			// + mync.getNodeID()
			// + "&OperatorValue="
			// + mync.getOperatorValueStr()
			// + "&FK_Node="
			// + mync.getFK_Node()
			// + "&DoType=Del&ToNodeID="
			// + mync.getToNodeID()
			// +
			// "' class='easyui-linkbutton' data-options=\"iconCls:'icon-remove'\" onclick=\"return confirm('确定删除此条件吗?')\">删除</a>"));
			ui.append(BaseModel.AddTREnd());
		}
		ui.append(BaseModel.AddTableEnd());
		ui.append(BaseModel.AddBR());
		ui.append("<div class=\"panel\" style=\"display: block; width: 1150px;\"><div class=\"panel-header\" style=\"width: 1138px;\"><div class=\"panel-title panel-with-icon\">说明</div><div class=\"panel-icon icon-tip\"></div><div class=\"panel-tool\"></div></div><div class=\"easyui-panel panel-body\" style=\"height: 14px; padding: 10px; width: 1128px;\" data-options=\"iconCls:'icon-tip',fit:true\" title=\"\"> 在上面的条件集合中ccflow仅仅支持要么是And,要么是OR的两种情形,高级的开发就需要事件来支持条件转向,或者采用其他的方式。</div></div></div>");
	}

	private DDL DDL_Node;

	public DDL getDDL_Node() {
		return (DDL) ui.GetUIByID("DDL_Node");
	}

	public void setDDL_Node(DDL dDL_Node) {
		DDL_Node = dDL_Node;
	}

	private Label Lab_Msg;

	public Label getLab_Msg() {
		return (Label) ui.GetUIByID("Lab_Msg");
	}

	public void setLab_Msg(Label lab_Msg) {
		Lab_Msg = lab_Msg;
	}

	private Label Lab_Note;

	public Label getLab_Note() {
		return (Label) ui.GetUIByID("Lab_Note");
	}

	public void setLab_Note(Label lab_Note) {
		Lab_Note = lab_Note;
	}

	// / <summary>
	// / 属性
	// / </summary>
	private DDL DDL_Attr;

	public DDL getDDL_Attr() {
		return (DDL) ui.GetUIByID("DDL_Attr");
	}

	public void setDDL_Attr(DDL dDL_Attr) {
		DDL_Attr = dDL_Attr;
	}

	private DDL DDL_Oper;

	public DDL getDDL_Oper() {
		return (DDL) ui.GetUIByID("DDL_Oper");
	}

	public void setDDL_Oper(DDL dDL_Oper) {
		DDL_Oper = dDL_Oper;
	}

	private DDL DDL_ConnJudgeWay;

	public DDL getDDL_ConnJudgeWay() {
		return (DDL) ui.GetUIByID("DDL_ConnJudgeWay");
	}

	public void setDDL_ConnJudgeWay(DDL dDL_ConnJudgeWay) {
		DDL_ConnJudgeWay = dDL_ConnJudgeWay;
	}

	// private HttpServletRequest req;
	// private HttpServletResponse res;
	// public CondModel()
	// {
	// }
	// private String basePath;
	// public String getBasePath() {
	// return basePath;
	// }
	// public void setBasePath(String basePath) {
	// this.basePath = basePath;
	// }
	// public CondModel(String basePath,HttpServletRequest
	// req,HttpServletResponse res)
	// {
	// this.req=req;
	// this.res=res;
	// this.setBasePath(basePath);
	// }
	// //#region 属性
	// /// <summary>
	// /// 主键
	// /// </summary>
	// private String MyPK;
	// public String getMyPK() {
	// return req.getParameter("MyPK");
	// }
	// public void setMyPK(String myPK) {
	// MyPK = myPK;
	// }
	// // public new string MyPK
	// // {
	// // get
	// // {
	// // return this.Request.QueryString["MyPK"];
	// // }
	// // }
	// /// <summary>
	// /// 流程编号
	// /// </summary>
	// private String FK_Flow;
	// public String getFK_Flow() {
	// return req.getParameter("FK_Flow");
	// }
	// public void setFK_Flow(String fK_Flow) {
	// FK_Flow = fK_Flow;
	// }
	// // public string FK_Flow
	// // {
	// // get
	// // {
	// // return this.Request.QueryString["FK_Flow"];
	// // }
	// // }
	// private String FK_Attr;
	// public String getFK_Attr() {
	// String s =req.getParameter("FK_Attr");//
	// this.Request.QueryString["FK_Attr"];
	// // if (s == null || s == "")
	// // s = ViewState["FK_Attr"] as string;
	//
	// if (s == null || s == "")
	// {
	// try
	// {
	// s = this.getDDL_Attr().getSelectedItemStringVal();
	// }
	// catch(Exception e)
	// {
	// return null;
	// }
	// }
	// if (s == "")
	// return null;
	// return s;
	// }
	// public void setFK_Attr(String fK_Attr) {
	// FK_Attr=fK_Attr;
	// // ViewState["FK_Attr"] = value;
	// }
	// // public string FK_Attr
	// // {
	// // get
	// // {
	// // string s = this.Request.QueryString["FK_Attr"];
	// // if (s == null || s == "")
	// // s = ViewState["FK_Attr"] as string;
	// //
	// // if (s == null || s == "")
	// // {
	// // try
	// // {
	// // s = this.DDL_Attr.SelectedItemStringVal;
	// // }
	// // catch
	// // {
	// // return null;
	// // }
	// // }
	// // if (s == "")
	// // return null;
	// // return s;
	// // }
	// // set
	// // {
	// // ViewState["FK_Attr"] = value;
	// // }
	// // }
	// /// <summary>
	// /// 节点
	// /// </summary>
	// private int FK_Node;
	// public int getFK_Node() {
	// try
	// {
	// return
	// req.getParameter("FK_Node")==null?0:Integer.parseInt(req.getParameter("FK_Node"));
	// }
	// catch(Exception e)
	// {
	// return this.getFK_MainNode();
	// }
	// }
	// public void setFK_Node(int fK_Node) {
	// FK_Node = fK_Node;
	// }
	// // public int FK_Node
	// // {
	// // get
	// // {
	// // try
	// // {
	// // return int.Parse(this.Request.QueryString["FK_Node"]);
	// // }
	// // catch
	// // {
	// // return this.FK_MainNode;
	// // }
	// // }
	// // }
	// private int FK_MainNode;
	// public int getFK_MainNode() {
	// return
	// req.getParameter("FK_MainNode")==null?0:Integer.parseInt(req.getParameter("FK_MainNode"));
	// }
	// public void setFK_MainNode(int fK_MainNode) {
	// FK_MainNode = fK_MainNode;
	// }
	// // public int FK_MainNode
	// // {
	// // get
	// // {
	// // return int.Parse(this.Request.QueryString["FK_MainNode"]);
	// // }
	// // }
	// private int ToNodeID;
	// public int getToNodeID() {
	// try
	// {
	// return
	// req.getParameter("ToNodeID")==null?0:Integer.parseInt(req.getParameter("ToNodeID"));
	// }
	// catch(Exception e)
	// {
	// return 0;
	// }
	// }
	// public void setToNodeID(int toNodeID) {
	// ToNodeID = toNodeID;
	// }
	// // public int ToNodeID
	// // {
	// // get
	// // {
	// // try
	// // {
	// // return int.Parse(this.Request.QueryString["ToNodeID"]);
	// // }
	// // catch
	// // {
	// // return 0;
	// // }
	// // }
	// // }
	// /// <summary>
	// /// 执行类型
	// /// </summary>
	// private int HisCondType;
	// public int getHisCondType() {
	// return
	// req.getParameter("CondType")==null?0:Integer.parseInt(req.getParameter("CondType"));
	// }
	// public void setHisCondType(int hisCondType) {
	// HisCondType = hisCondType;
	// }
	// // public CondType HisCondType
	// // {
	// // get
	// // {
	// // return (CondType)int.Parse(this.Request.QueryString["CondType"]);
	// // }
	// // }
	// private String GetOperVal;
	// public String getGetOperVal() {
	// if((TextBox)str.GetUIByID("TB_Val")==null)
	// {
	//
	// return ((TextBox)str.GetUIByID("TB_Val")).getText();
	// }
	// return ((DDL)str.GetUIByID("DDL_Val")).getSelectedItemStringVal();
	// }
	// public void setGetOperVal(String getOperVal) {
	// GetOperVal = getOperVal;
	// }
	// private String GetOperValText;
	// public String getGetOperValText() {
	// if((TextBox)str.GetUIByID("TB_Val") ==null)
	// {
	//
	// return ((TextBox)str.GetUIByID("TB_Val")).getText();
	// }
	// return ((DDL)str.GetUIByID("DDL_Val")).getSelectedItem().getText();
	// }
	// public void setGetOperValText(String getOperValText) {
	// GetOperValText = getOperValText;
	// }
	// public UiFatory str=null;
	// //#endregion 属性
	//
	// public void Page_Load() throws NumberFormatException, IOException
	// {
	// str=new UiFatory();
	// String DoType =
	// req.getParameter("DoType")==null?"":req.getParameter("DoType");
	// if (DoType.equals("Del"))
	// {
	// Cond nd = new Cond(this.getMyPK());
	// nd.Delete();
	// res.sendRedirect("Cond.jsp?CondType=" + getHisCondType() + "&FK_Flow=" +
	// this.getFK_Flow() + "&FK_MainNode=" + nd.getNodeID() + "&FK_Node=" +
	// this.getFK_MainNode() + "&ToNodeID=" + nd.getToNodeID());
	// //this.Response.Redirect("Cond.aspx?CondType=" + (int)this.HisCondType +
	// "&FK_Flow=" + this.FK_Flow + "&FK_MainNode=" + nd.NodeID + "&FK_Node=" +
	// this.FK_MainNode + "&ToNodeID=" + nd.ToNodeID, true);
	// return;
	// }
	// this.BindCond();
	// if (this.getFK_Attr() == null)
	// this.setFK_Attr(this.getDDL_Attr().getSelectedItemStringVal());
	// }
	// public void BindCond()
	// {
	// String msg = "";
	// String note = "";
	//
	// Cond cond = new Cond();
	// cond.setMyPK(this.getMyPK());
	// if (cond.RetrieveFromDBSources() == 0)
	// {
	// if (this.getFK_Attr() != null)
	// cond.setFK_Attr(this.getFK_Attr());
	// if (this.getFK_MainNode() != 0)
	// cond.setNodeID(this.getFK_MainNode());
	// if (this.getFK_Node() != 0)
	// cond.setFK_Node(this.getFK_Node());
	// if (this.getFK_Flow() != null)
	// cond.setFK_Flow(this.getFK_Flow());
	// }
	// //this.AddTable("border=0 widht='500px'");
	// str.append(BaseModel.AddTable("class='Table' cellpadding='2' cellspacing='2' style='width:100%;'"));
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("class='GroupTitle' style='width:80px'",
	// "项目"));
	// str.append(BaseModel.AddTD("class='GroupTitle' style='width:200px'",
	// "采集"));
	// str.append(BaseModel.AddTD("class='GroupTitle'", "描述"));
	// str.append(BaseModel.AddTREnd());
	//
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("节点"));
	// Nodes nds = new Nodes(cond.getFK_Flow());
	// Nodes ndsN = new Nodes();
	// for (int i = 0; i < nds.size(); i++) {
	// Node mynd=(Node) nds.get(i);
	// ndsN.AddEntity(mynd);
	// }
	// // for (Node mynd : nds)
	// // {
	// // ndsN.AddEntity(mynd);
	// // }
	// DDL ddl = str.creatDDL("DDL_Node");
	// // ddl.setId("DDL_Node");
	// ddl.BindEntities(ndsN, "NodeID", "Name");
	// ddl.SetSelectItem(cond.getFK_Node());
	// //ddl.AutoPostBack = true;
	// ddl.addAttr("onchange",
	// "ddl_SelectedIndexChanged()");//aSelectedIndexChanged += new
	// EventHandler(ddl_SelectedIndexChanged);
	// str.append(BaseModel.AddTD(ddl));
	// str.append(BaseModel.AddTD("节点"));
	// str.append(BaseModel.AddTREnd());
	//
	// // 属性/字段
	// MapAttrs attrs = new MapAttrs();
	// attrs.Retrieve(MapAttrAttr.FK_MapData, "ND" +
	// ddl.getSelectedItemStringVal());
	//
	// MapAttrs attrNs = new MapAttrs();
	// for (int i = 0; i < attrs.size(); i++) {
	// MapAttr attr=(MapAttr) attrs.get(i);
	// if (attr.getIsBigDoc())
	// continue;
	// if(attr.getKeyOfEn()!=null || !attr.getKeyOfEn().equals(""))
	// {
	// if(attr.getKeyOfEn()=="Title")
	// {
	//
	// }
	// if(attr.getKeyOfEn()=="FK_Emp")
	// {
	//
	// }
	// if(attr.getKeyOfEn()=="MyNum")
	// {
	//
	// }
	// if(attr.getKeyOfEn()=="FK_NY")
	// {
	//
	// }
	// if(attr.getKeyOfEn()==WorkAttr.Emps)
	// {
	//
	// }
	// if(attr.getKeyOfEn()==WorkAttr.OID)
	// {
	//
	// }
	// if(attr.getKeyOfEn()==StartWorkAttr.Rec)
	// {
	//
	// }
	// if(attr.getKeyOfEn()==StartWorkAttr.FID)
	// {
	//
	// }
	// }
	// // switch (attr.getKeyOfEn())
	// // {
	// // case "Title":
	// // //case "RDT":
	// // //case "CDT":
	// // case "FK_Emp":
	// // case "MyNum":
	// // case "FK_NY":
	// // case WorkAttr.Emps:
	// // case WorkAttr.OID:
	// // case StartWorkAttr.Rec:
	// // case StartWorkAttr.FID:
	// // continue;
	// // default:
	// // break;
	// // }
	// attrNs.AddEntity(attr);
	// }
	// // for (MapAttr attr : attrs)
	// // {
	// // if (attr.IsBigDoc)
	// // continue;
	// //
	// // switch (attr.KeyOfEn)
	// // {
	// // case "Title":
	// // //case "RDT":
	// // //case "CDT":
	// // case "FK_Emp":
	// // case "MyNum":
	// // case "FK_NY":
	// // case WorkAttr.Emps:
	// // case WorkAttr.OID:
	// // case StartWorkAttr.Rec:
	// // case StartWorkAttr.FID:
	// // continue;
	// // default:
	// // break;
	// // }
	// // attrNs.AddEntity(attr);
	// // }
	// ddl = str.creatDDL("DDL_Attr");
	// // ddl.setId("DDL_Attr");
	// if (attrNs.size() == 0)
	// {
	// Node nd = new Node(cond.getFK_Node());
	// nd.RepareMap();
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD(""));
	// str.append(BaseModel.AddTD("colspan=2", "节点没有找到合适的条件"));
	// str.append(BaseModel.AddTREnd());
	// str.append(BaseModel.AddTableEnd());
	// return;
	// }
	// else
	// {
	// ddl.BindEntities(attrNs, MapAttrAttr.MyPK, MapAttrAttr.Name);
	// // ddl.AutoPostBack = true;
	// ddl.addAttr("onchange",
	// "ddl_SelectedIndexChanged()");//ddl.SelectedIndexChanged += new
	// EventHandler(ddl_SelectedIndexChanged);
	// ddl.SetSelectItem(cond.getFK_Attr());
	// }
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("属性/字段"));
	// str.append(BaseModel.AddTD(ddl));
	// str.append(BaseModel.AddTD(""));
	// str.append(BaseModel.AddTREnd());
	//
	// MapAttr attrS = new
	// MapAttr(this.getDDL_Attr().getSelectedItemStringVal());
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("操作符"));
	// ddl = str.creatDDL("DDL_Oper");
	// // ddl.setId("DDL_Oper");
	// switch (attrS.getLGType())
	// {
	// case Enum:
	// case FK:
	// ddl.Items.add(new ListItem("=", "="));
	// ddl.Items.add(new ListItem("<>", "<>"));
	// break;
	// case Normal:
	// switch (attrS.getMyDataType())
	// {
	// case BP.DA.DataType.AppString:
	// case BP.DA.DataType.AppDate:
	// case BP.DA.DataType.AppDateTime:
	// ddl.Items.add(new ListItem("=", "="));
	// ddl.Items.add(new ListItem("LIKE", "LIKE"));
	// ddl.Items.add(new ListItem("<>", "<>"));
	// break;
	// case BP.DA.DataType.AppBoolean:
	// ddl.Items.add(new ListItem("=", "="));
	// break;
	// default:
	// ddl.Items.add(new ListItem("=", "="));
	// ddl.Items.add(new ListItem(">", ">"));
	// ddl.Items.add(new ListItem(">=", ">="));
	// ddl.Items.add(new ListItem("<", "<"));
	// ddl.Items.add(new ListItem("<=", "<="));
	// ddl.Items.add(new ListItem("<>", "<>"));
	// break;
	// }
	// break;
	// default:
	// break;
	// }
	//
	// if (cond != null)
	// {
	// try
	// {
	// ddl.SetSelectItem(cond.getOperatorValueInt());
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// str.append(BaseModel.AddTD(ddl));
	// str.append(BaseModel.AddTD(""));
	// str.append(BaseModel.AddTREnd());
	// switch (attrS.getLGType())
	// {
	// case Enum:
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("值"));
	// ddl = str.creatDDL("DDL_Val");
	// // ddl.setId("DDL_Val");
	// ddl.BindSysEnum(attrS.getUIBindKey());
	// if (cond != null)
	// {
	// try
	// {
	// ddl.SetSelectItem(cond.getOperatorValueInt());
	// }
	// catch(Exception e)
	// {
	// }
	// }
	// str.append(BaseModel.AddTD(ddl));
	// str.append(BaseModel.AddTD(""));
	// str.append(BaseModel.AddTREnd());
	// break;
	// case FK:
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("值"));
	//
	// ddl = str.creatDDL("DDL_Val");
	// // ddl.setId("DDL_Val");
	// ddl.BindEntities(attrS.getHisEntitiesNoName());
	// if (cond != null)
	// {
	// try
	// {
	// ddl.SetSelectItem(cond.getOperatorValueStr());
	// }
	// catch(Exception e)
	// {
	// }
	// }
	// // ui.put("DDL_Val", ddl);
	// str.append(BaseModel.AddTD(ddl));
	// str.append(BaseModel.AddTD(""));
	// str.append(BaseModel.AddTREnd());
	// break;
	// default:
	// if (attrS.getMyDataType() == BP.DA.DataType.AppBoolean)
	// {
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("值"));
	// ddl = str.creatDDL("DDL_Val");
	// // ddl.setId("DDL_Val");
	// ddl.BindAppYesOrNo(0);
	// if (cond != null)
	// {
	// try
	// {
	// ddl.SetSelectItem(cond.getOperatorValueInt());
	// }
	// catch(Exception e)
	// {
	// }
	// }
	// // ui.put("DDL_Val", ddl);
	// str.append(BaseModel.AddTD(ddl));
	// str.append(BaseModel.AddTD());
	// str.append(BaseModel.AddTREnd());
	// }
	// else
	// {
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTD("值"));
	// TextBox tb = str.creatTextBox("TB_Val");
	// // tb.setId("TB_Val");
	// if (cond != null)
	// tb.setText(cond.getOperatorValueStr());
	// str.append(BaseModel.AddTD(tb));
	// str.append(BaseModel.AddTD());
	// str.append(BaseModel.AddTREnd());
	// // ui.put("TB_Val", tb);
	// }
	// break;
	// }
	//
	//
	// Conds conds = new Conds();
	// QueryObject qo = new QueryObject(conds);
	// qo.AddWhere(CondAttr.NodeID, this.getFK_MainNode());
	// qo.addAnd();
	// qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.Form.getValue());
	// qo.addAnd();
	// qo.AddWhere(CondAttr.CondType, getHisCondType());
	//
	// if (this.getToNodeID() != 0)
	// {
	// qo.addAnd();
	// qo.AddWhere(CondAttr.ToNodeID, this.getToNodeID());
	// }
	// int num = qo.DoQuery();
	//
	// str.append(BaseModel.AddTableEnd());
	// str.append(BaseModel.AddBR());
	// str.append(BaseModel.AddSpace(1));
	//
	// LinkButton btn = str.creatLinkButton(false, "Btn_SaveAnd", "保存为And条件");
	// btn.SetDataOption("iconCls", "icon-save");
	// btn.setHref("onAnd()");//setHref("btn_Save_Click()");//setClick += new
	// EventHandler(btn_Save_Click);
	// str.append(btn);
	// str.append(BaseModel.AddSpace(1));
	//
	// btn = str.creatLinkButton(false, "Btn_SaveOr", "保存为Or条件");
	// btn.SetDataOption("iconCls", "icon-save");
	// //btn.setHref("btn_Save_Click(this)");
	// btn.setHref("onOr()");//btn.Click += new EventHandler(btn_Save_Click);
	// str.append(btn);
	// str.append(BaseModel.AddSpace(1));
	//
	// btn = str.creatLinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
	// btn.addAttr("onclick",
	// " return confirm('您确定要删除吗？');");//Attributes["onclick"] =
	// " return confirm('您确定要删除吗？');";
	// btn.setHref("deletetable()");
	// str.append(btn);
	//
	// str.append(BaseModel.AddBR());
	// str.append(BaseModel.AddBR());
	//
	// if (num == 0)
	// return;
	//
	// //#region 条件
	// str.append(BaseModel.AddTable("class='Table' cellpadding='2' cellspacing='2' style='width:100%;'"));
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTDTitleGroup("序"));
	// str.append(BaseModel.AddTDTitleGroup("节点"));
	// str.append(BaseModel.AddTDTitleGroup("字段的英文名"));
	// str.append(BaseModel.AddTDTitleGroup("字段的中文名"));
	// str.append(BaseModel.AddTDTitleGroup("操作符"));
	// str.append(BaseModel.AddTDTitleGroup("值"));
	// str.append(BaseModel.AddTDTitleGroup("标签"));
	// str.append(BaseModel.AddTDTitleGroup("运算关系"));
	// str.append(BaseModel.AddTDTitleGroup("操作"));
	// str.append(BaseModel.AddTREnd());
	//
	// int i = 0;
	// for (int j = 0; j < conds.size(); j++) {
	// Cond mync=(Cond) conds.get(j);
	// if (mync.getHisDataFrom() != ConnDataFrom.Form)
	// continue;
	//
	// i++;
	//
	// str.append(BaseModel.AddTR());
	// str.append(BaseModel.AddTDIdx(i));
	// // this.AddTD(mync.HisDataFrom.ToString());
	// str.append(BaseModel.AddTD(mync.getFK_NodeT()));
	// str.append(BaseModel.AddTD(mync.getAttrKey()));
	// str.append(BaseModel.AddTD(mync.getAttrName()));
	// str.append(BaseModel.AddTDCenter(mync.getFK_Operator()));
	// str.append(BaseModel.AddTD(mync.getOperatorValueStr()));
	// str.append(BaseModel.AddTD(mync.getOperatorValueT()));
	//
	// if (mync.getCondOrAnd() == CondOrAnd.ByAnd)
	// str.append(BaseModel.AddTD("AND"));
	// else
	// str.append(BaseModel.AddTD("OR"));
	//
	// //if (num > 1)
	// // this.AddTD(mync.HisConnJudgeWayT);
	// str.append(BaseModel.AddTD("<a href='"+basePath+"WF/Admin/Cond.jsp?MyPK="
	// + mync.getMyPK() + "&CondType=" + getHisCondType() + "&FK_Flow=" +
	// this.FK_Flow + "&FK_Attr=" + mync.getFK_Attr() + "&FK_MainNode=" +
	// mync.getNodeID() + "&OperatorValue=" + mync.getOperatorValueStr() +
	// "&FK_Node=" + mync.getFK_Node() + "&DoType=Del&ToNodeID=" +
	// mync.getToNodeID() +
	// "' class='easyui-linkbutton' data-options=\"iconCls:'icon-remove'\" onclick=\"return confirm('确定删除此条件吗?')\">删除</a>"));
	// str.append(BaseModel.AddTREnd());
	// }
	// str.append(BaseModel.AddTableEnd());
	// str.append(BaseModel.AddBR());
	// BaseModel base=new BaseModel(req, res);
	// str.append(base.AddEasyUiPanelInfo("说明","在上面的条件集合中ccflow仅仅支持要么是And,要么是OR的两种情形,高级的开发就需要事件来支持条件转向,或者采用其他的方式。"));
	//
	// //#endregion
	// }
	// private DDL DDL_Node;
	// public DDL getDDL_Node() {
	// DDL_Node=(DDL) str.GetUIByID("DDL_Node");
	// return DDL_Node;
	// }
	// public void setDDL_Node(DDL dDL_Node) {
	// DDL_Node = dDL_Node;
	// }
	// // public DDL DDL_Node
	// // {
	// // get
	// // {
	// // return this.GetDDLByID("DDL_Node");
	// // }
	// // }
	// private Label Lab_Msg;
	// public Label getLab_Msg() {
	// Lab_Msg=(Label) str.GetUIByID("Lab_Msg");
	// return Lab_Msg;
	// }
	// public void setLab_Msg(Label lab_Msg) {
	// Lab_Msg = lab_Msg;
	// }
	// // public Label Lab_Msg
	// // {
	// // get
	// // {
	// // return this.GetLabelByID("Lab_Msg");
	// // }
	// // }
	// private Label Lab_Note;
	// public Label getLab_Note() {
	// Lab_Note=(Label) str.GetUIByID("Lab_Note");
	// return Lab_Note;
	// }
	// public void setLab_Note(Label lab_Note) {
	// Lab_Note = lab_Note;
	// }
	// // public Label Lab_Note
	// // {
	// // get
	// // {
	// // return this.GetLabelByID("Lab_Note");
	// // }
	// // }
	// /// <summary>
	// /// 属性
	// /// </summary>
	// private DDL DDL_Attr;
	// public DDL getDDL_Attr() {
	// DDL_Attr=(DDL) str.GetUIByID("DDL_Attr");
	// return DDL_Attr;
	// }
	// public void setDDL_Attr(DDL dDL_Attr) {
	// DDL_Attr = dDL_Attr;
	// }
	// // public DDL DDL_Attr
	// // {
	// // get
	// // {
	// // return this.GetDDLByID("DDL_Attr");
	// // }
	// // }
	// private DDL DDL_Oper;
	// public DDL getDDL_Oper() {
	// DDL_Oper=(DDL) str.GetUIByID("DDL_Oper");
	// return DDL_Oper;
	// }
	// public void setDDL_Oper(DDL dDL_Oper) {
	// DDL_Oper = dDL_Oper;
	// }
	// // public DDL DDL_Oper
	// // {
	// // get
	// // {
	// // return this.GetDDLByID("DDL_Oper");
	// // }
	// // }
	// private DDL DDL_ConnJudgeWay;
	// public DDL getDDL_ConnJudgeWay() {
	// DDL_ConnJudgeWay=(DDL) str.GetUIByID("DDL_ConnJudgeWay");
	// return DDL_ConnJudgeWay;
	// }
	// public void setDDL_ConnJudgeWay(DDL dDL_ConnJudgeWay) {
	// DDL_ConnJudgeWay = dDL_ConnJudgeWay;
	// }
}
