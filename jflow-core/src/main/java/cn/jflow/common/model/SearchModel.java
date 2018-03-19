package cn.jflow.common.model;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.ToolBar;
import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Rpt.MapRpt;

public class SearchModel extends BaseModel {

	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();

	private String rptNo = null;
	private String fkFlow = null;
	private Entities HisEns = null;
	private ToolBar toolBar;
	private StringBuilder pub2 = new StringBuilder();
	private StringBuffer UCSys1 = new StringBuffer();
	private MapRpt currMapRpt;

	public SearchModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.request.set(request);
		this.response.set(response);
	}

	public HttpServletRequest getRequest() {
		return request.get();
	}

	public HttpServletResponse getResponse() {
		return response.get();
	}

	public String FK_MapData;
	
	
	
	
	public String getFK_MapData() {
		return FK_MapData;
	}

	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}

	public String initToolBar() {
		StringBuffer htmlStr = new StringBuffer();
		//currMapRpt = new MapRpt(getRptNo(), getFkFlow());
		currMapRpt = new MapRpt(getRptNo());
		Entity en = ClassFactory.GetEns(getRptNo()).getGetNewEntity();
		Flow fl = new Flow(currMapRpt.getFK_Flow());
		this.setFK_MapData("ND"+Integer.parseInt(currMapRpt.getFK_Flow())+"Rpt");
		toolBar = new ToolBar(getRequest(), getResponse());
		toolBar.InitToolbarOfMapRpt(fl, currMapRpt, getRptNo(), en, 1);
		toolBar.AddLinkBtn(true, NamesOfBtn.Export.getCode());

		DDL ddl = new DDL();
		ddl.setId("GoTo");
		ddl.Items.add(new ListItem("查询", "Search"));
		// ddl.Items.add(new ListItem("高级查询", "SearchAdv"));
		ddl.Items.add(new ListItem("分组分析", "Group"));
		ddl.Items.add(new ListItem("交叉报表", "D3"));
		ddl.Items.add(new ListItem("对比分析", "Contrast"));
		ddl.SetSelectItem(getPageID());
		toolBar.AddDDL(ddl);
		ddl.addAttr("onchange", "ddl_SelectedIndexChanged_GoTo()");

		toolBar.GetLinkBtnByID(NamesOfBtn.Search).setHref(
				"ToolBar1_ButtonClick('" + NamesOfBtn.Search.getCode() + "')");
		toolBar.GetLinkBtnByID(NamesOfBtn.Export).setHref(
				"ToolBar1_ButtonClick('" + NamesOfBtn.Export.getCode() + "')");

		// 处理按钮.
		this.SetDGData();

		htmlStr.append(toolBar.toString());
		return htmlStr.toString();
	}

	public String initPub() {
		StringBuffer htmlStr = new StringBuffer();
		htmlStr.append(UCSys1);
		htmlStr.append(pub2);
		return htmlStr.toString();
	}

	public Entities SetDGData() {
		return SetDGData(getPageIdx());
	}

	public Entities SetDGData(int pageIdx) {
		Entities ens = getHisEns();
		Entity en = ens.getGetNewEntity();
		QueryObject qo = new QueryObject(ens);
		qo = toolBar.GetnQueryObject(ens, en);
		// 执行数据分页查询，并绑定分页控件
		BindPageIdxEasyUi(pub2, qo.GetCount(), getPageID() + ".jsp?RptNo="
				+ getRptNo() + "&EnsName=" + getRptNo() + "&FK_Flow="
				+ getFK_Flow(), pageIdx, SystemConfig.getPageSize(),
				"'first','prev','sep','manual','sep','next','last'", false);

		qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), pageIdx);
		// 检查是否显示按关键字查询，如果是就把关键标注为红色.
		if (en.getEnMap().IsShowSearchKey) {
			String keyVal = toolBar.GetTBByID("TB_Key").getText().trim();

			if (keyVal.length() >= 1) {
				Attrs attrs = en.getEnMap().getAttrs();

				for (Object obj : ens.subList(0, ens.size())) {
					Entity myen = (Entity) obj;
					for (Attr attr : attrs) {
						if (attr.getIsFKorEnum())
							continue;

						if (attr.getIsPK())
							continue;

						switch (attr.getMyDataType()) {
						case DataType.AppRate:
						case DataType.AppMoney:
						case DataType.AppInt:
						case DataType.AppFloat:
						case DataType.AppDouble:
						case DataType.AppBoolean:
							continue;
						default:
							break;
						}
						myen.SetValByKey(
								attr.getKey(),
								myen.GetValStrByKey(attr.getKey())
										.replace(
												keyVal,
												"<font color=red>" + keyVal
														+ "</font>"));
					}
				}
			}
		}
		BindEns(ens, null);
		return ens;
	}

	public void BindEns(Entities ens, String ctrlId) {
		// #region 定义变量.
		Entity myen = ens.getGetNewEntity();
		Attrs attrs = myen.getEnMap().getAttrs();

		// MapData md = new MapData(getRptNo());
		// String pk = myen.getPK();
		// String clName = myen.toString();

		UCSys1.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%;line-height:22px'"));
		// #region 生成表格标题
		UCSys1.append(AddTR());
		UCSys1.append(AddTDGroupTitle2("序"));
		UCSys1.append(AddTDGroupTitle2("标题"));

		for (Attr attr : attrs) {
			if (attr.getIsRefAttr() || "Title".equals(attr.getKey())
					|| "MyNum".equals(attr.getKey()))
				continue;

			UCSys1.append(AddTDGroupTitle2(attr.getDesc()));
		}

		UCSys1.append(AddTREnd());

		// #region 用户界面属性设置
		int pageidx = getPageIdx() - 1;
		int idx = SystemConfig.getPageSize() * pageidx;

		// #region 数据输出.
		for (Object obj : ens.subList(0, ens.size())) {
			// #region 输出字段。
			Entity en = (Entity) obj;
			idx++;
			UCSys1.append(AddTR());
			UCSys1.append(AddTDIdx(idx));
			UCSys1.append(AddTD("<a href=\"javascript:WinOpen('"
					+ Glo.getCCFlowAppPath() + "WF/WFRpt.jsp?FK_Flow="
					+ currMapRpt.getFK_Flow() + "&WorkID="
					+ en.GetValStrByKey("OID") + "','tdr');\" >"
					+ en.GetValByKey("Title") + "</a>"));

			String sql = "SELECT myPk,ndfrom FROM ND"+Integer.parseInt(currMapRpt.getFK_Flow())+"Track where workid="+en.GetValStrByKey("OID")+" order by rdt desc" ;
			DataTable dt = DBAccess.RunSQLReturnTable_200705_SQL(sql, new Paras());
			for (Attr attr : attrs) {
				String key = attr.getKey();
				if (attr.getIsRefAttr() || "MyNum".equals(key)
						|| "Title".equals(key))
					continue;
				
				//流程信息增加连接 到流程最后的一个节点的表单详细   FlowNote
				if ("FlowNote".equals(key))
				{
					if(dt.Rows.size()!=0){
						UCSys1.append(AddTD("<a href=\"javascript:WinOpen('"
								+ Glo.getCCFlowAppPath() + "WF/WFRpt.jsp?FK_Flow="
								+ currMapRpt.getFK_Flow() + "&WorkID="
								+ en.GetValStrByKey("OID") +"&DoType=View&MyPK="+dt.Rows.get(0).get("mypk")
								+ "&FK_Node="+dt.Rows.get(0).get("ndfrom")+"','tdr');\" >"
								+ "表单详情</a>"));
					}else
					{
						UCSys1.append(AddTDNum("表单详情"));
					}
				}
				
				if (attr.getUIContralType() == UIContralType.DDL) {
					String s = en.GetValRefTextByKey(key);
					if (StringHelper.isNullOrEmpty(s)) {
						if ("FK_NY".equals(key)) {
							s = en.GetValStringByKey(key);
						} else {
							s = en.GetValStringByKey(key);
						}
					}
					UCSys1.append(AddTD(s));
					continue;
				}

				String str="";
				str = en.GetValStrByKey(key);
				if(key.equals("XingBie"))
				{
					if(en.GetValStrByKey(key).equals("0"))
					{
						str="男";
					}else
					{
						str="女";
					}
				}

				switch (attr.getMyDataType()) {
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (StringHelper.isNullOrEmpty(str))
						str = "&nbsp;";
					UCSys1.append(AddTD(str));
					break;
				case DataType.AppString:
					if (StringHelper.isNullOrEmpty(str))
						str = "&nbsp;";
					if (attr.getUIHeight() != 0)
						UCSys1.append(AddTDDoc(str, str));
					else
						UCSys1.append(AddTD(str));
					break;
				case DataType.AppBoolean:
					if ("1".equals(str))
						UCSys1.append(AddTD("是"));
					else
						UCSys1.append(AddTD("否"));
					break;
				case DataType.AppFloat:
				case DataType.AppInt:
				case DataType.AppRate:
				case DataType.AppDouble:
					UCSys1.append(AddTDNum(str));
					break;
				case DataType.AppMoney:
					UCSys1.append(AddTDNum((new BigDecimal(str)).setScale(2,
							java.math.BigDecimal.ROUND_HALF_UP).doubleValue()
							+ ""));
					break;
				default:
					try {
						throw new Exception("no this case ...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			UCSys1.append(AddTREnd());
		}
		// #region 计算一下是否可以求出合计,主要是判断是否有数据类型在这个Entities中。
		boolean IsHJ = false;
		for (Attr attr : attrs) {
			String key = attr.getKey();
			if (attr.getMyFieldType() == FieldType.RefText
					|| "Title".equals(key) || "MyNum".equals(key))
				continue;

			if (!attr.getUIVisible())
				continue;

			if (attr.getUIContralType() == UIContralType.DDL)
				continue;

			if ("OID".equals(key) || "MID".equals(key) || "FID".equals(key)
					|| "PWorkID".equals(key)
					|| "WORKID".equals(key.toUpperCase()))
				continue;

			switch (attr.getMyDataType()) {
			case DataType.AppDouble:
			case DataType.AppFloat:
			case DataType.AppInt:
			case DataType.AppMoney:
				IsHJ = true;
				break;
			default:
				break;
			}
		}
		// #region 输出合计。
		if (IsHJ) {
			UCSys1.append("<TR class='Sum' >");
			UCSys1.append(AddTD());
			UCSys1.append(AddTD("合计"));
			for (Attr attr : attrs) {
				String key = attr.getKey();
				if ("MyNum".equals(key))
					continue;

				if (attr.getUIContralType() == UIContralType.DDL) {
					UCSys1.append(AddTD());
					continue;
				}

				if (!attr.getUIVisible())
					continue;

				if ("OID".equals(key) || "MID".equals(key)
						|| "WORKID".equals(key.toUpperCase())
						|| "FID".equals(key)) {
					UCSys1.append(AddTD());
					continue;
				}

				switch (attr.getMyDataType()) {
				case DataType.AppDouble:
					UCSys1.append(AddTDNum(ens.GetSumDecimalByKey(key)));
					break;
				case DataType.AppFloat:
					UCSys1.append(AddTDNum(ens.GetSumDecimalByKey(key)));
					break;
				case DataType.AppInt:
					UCSys1.append(AddTDNum(ens.GetSumDecimalByKey(key)));
					break;
				case DataType.AppMoney:
					UCSys1.append(AddTDJE(ens.GetSumDecimalByKey(key)));
					break;
				default:
					UCSys1.append(AddTD());
					break;
				}
			}
			/* 结束循环 */
			UCSys1.append(AddTD());
			UCSys1.append(AddTREnd());
		}
		UCSys1.append(AddTableEnd());
	}

	public Entities getHisEns() {
		if (HisEns == null) {
			if (rptNo != null) {
				HisEns = ClassFactory.GetEns(rptNo);
			} else {
				HisEns = ClassFactory.GetEns(getRptNo());
			}
		}
		return HisEns;
	}

	public String getRptNo() {
		if (rptNo == null) {
			rptNo = getRequest().getParameter("RptNo");
			if (StringHelper.isNullOrEmpty(rptNo)) {
				rptNo = "ND68MyRpt";
			}
		}
		return rptNo;
	}

	public String getFkFlow() {
		if (fkFlow == null) {
			fkFlow = getRequest().getParameter("FK_Flow");
			if (StringHelper.isNullOrEmpty(fkFlow)) {
				fkFlow = "068";
			}
		}
		return fkFlow;
	}
	
	@Override
	public String getEnsName() {
		String ensName = super.getEnsName();
		if (StringHelper.isNullOrEmpty(ensName)) {
			ensName = super.getEnName();
		}

		return ensName;
	}
	// #region 属性.
	// / <summary>
	// / 编号名称
	// / </summary>
	// private HttpServletRequest req;
	// private HttpServletResponse res;
	// public UiFatory Pub2 = null;
	// public ToolBar ToolBar1 = null;
	// public UiFatory UCSys1 = null;
	// public UiFatory Pub1 = null;
	//
	// public SearchModel(HttpServletRequest request, HttpServletResponse
	// response) {
	// this.req = request;
	// this.res = response;
	// }
	//
	// private String RptNo;
	//
	// public String getRptNo() {
	// String s = req.getParameter("RptNo");
	// if (StringHelper.isNullOrEmpty(s)) {
	// return "ND68MyRpt";
	// }
	// return s;
	// }
	//
	// public void setRptNo(String rptNo) {
	// RptNo = rptNo;
	// }
	//
	// // public string RptNo
	// // {
	// // get
	// // {
	// // string s = this.Request.QueryString["RptNo"];
	// // if (string.IsNullOrEmpty(s))
	// // {
	// // return "ND68MyRpt";
	// // }
	// // return s;
	// // }
	// // }
	// private String FK_Flow;
	//
	// public String getFK_Flow() {
	// String s = req.getParameter("FK_Flow");
	// if (StringHelper.isNullOrEmpty(s))
	// return "068";
	// return s;
	// }
	//
	// public void setFK_Flow(String fK_Flow) {
	// FK_Flow = fK_Flow;
	// }
	//
	// // public string FK_Flow
	// // {
	// // get
	// // {
	// // string s = this.Request.QueryString["FK_Flow"];
	// // if (string.IsNullOrEmpty(s))
	// // return "068";
	// // return s;
	// // }
	// // }
	// public Entities _HisEns = null;
	// private Entities HisEns;
	//
	// public Entities getHisEns() {
	// if (_HisEns == null) {
	// if (this.getRptNo() != null) {
	// if (this._HisEns == null)
	// _HisEns = BP.En.ClassFactory.GetEns(this.getRptNo());
	// }
	// }
	// return _HisEns;
	// }
	//
	// public void setHisEns(Entities hisEns) {
	// HisEns = hisEns;
	// }
	//
	// // public Entities HisEns
	// // {
	// // get
	// // {
	// // if (_HisEns == null)
	// // {
	// // if (this.RptNo != null)
	// // {
	// // if (this._HisEns == null)
	// // _HisEns = BP.En.ClassFactory.GetEns(this.RptNo);
	// // }
	// // }
	// // return _HisEns;
	// // }
	// // }
	// public MapRpt currMapRpt = null;
	//
	// // #endregion 属性.
	//
	// public void Page_Load() {
	// Pub1 = new UiFatory();
	// Pub2 = new UiFatory();
	// UCSys1 = new UiFatory();
	// ToolBar1 = new ToolBar(req, res, Pub1);
	// // #region 处理查询权限， 此处不要修改，以Search.ascx为准.
	// // this.Page.RegisterClientScriptBlock("sss",
	// // "<link href='" + BP.WF.Glo.CCFlowAppPath + "WF/Comm/Style/Table" +
	// // BP.Web.WebUser.Style + ".css' rel='stylesheet' type='text/css' />");
	// currMapRpt = new MapRpt(this.getRptNo(), this.getFK_Flow());
	// Entity en = this.getHisEns().getGetNewEntity();
	// Flow fl = new Flow(this.currMapRpt.getFK_Flow());
	//
	// // 初始化查询工具栏.
	// this.ToolBar1.InitToolbarOfMapRpt(fl, currMapRpt, this.getRptNo(), en,
	// 1);
	// this.ToolBar1.AddLinkBtn(true, NamesOfBtn.Export.getCode());
	//
	// // 增加转到.
	// this.ToolBar1.append("&nbsp;");
	// DDL ddl = Pub1.creatDDL("GoTo");
	// // ddl.ID = "GoTo";
	// ddl.Items.add(new ListItem("查询", "Search"));
	// // ddl.Items.Add(new ListItem("高级查询", "SearchAdv"));
	// ddl.Items.add(new ListItem("分组分析", "Group"));
	// ddl.Items.add(new ListItem("交叉报表", "D3"));
	// ddl.Items.add(new ListItem("对比分析", "Contrast"));
	// ddl.SetSelectItem(this.getPageID());
	// this.ToolBar1.append(ddl);
	// // ddl.AutoPostBack = true;
	// ddl.addAttr("onchange", "ddl_SelectedIndexChanged_GoTo()");
	// // ddl.SelectedIndexChanged += new
	// // EventHandler(ddl_SelectedIndexChanged_GoTo);
	//
	// this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Search).addAttr("onclick",
	// "ToolBar1_ButtonClick1()"); // += new
	// // System.EventHandler(this.ToolBar1_ButtonClick);
	// this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Export).addAttr("onclick",
	// "ToolBar1_ButtonClick2()"); // += new
	// // System.EventHandler(this.ToolBar1_ButtonClick);
	//
	// // #endregion 处理查询权限
	//
	// // 处理按钮.
	// this.SetDGData();
	// }
	//
	// private String _pageID = null;
	//
	// public String getPageID() {
	// if (StringHelper.isNullOrEmpty(_pageID)) {
	// String url = req.getRequestURL().toString();
	// int i = url.lastIndexOf("/") + 1;
	// int i2 = url.indexOf(".jsp") - 6;
	// try {
	// url = url.substring(i);
	// _pageID = url.substring(0, url.indexOf(".jsp"));
	//
	// } catch (RuntimeException ex) {
	// throw new RuntimeException(ex.getMessage() + url + " i=" + i
	// + " i2=" + i2);
	// }
	// }
	// return _pageID;
	// }
	//
	// // void ddl_SelectedIndexChanged_GoTo(object sender, EventArgs e)
	// // {
	// // DDL ddl = sender as DDL;
	// // string item = ddl.SelectedItemStringVal;
	// //
	// // string tKey = DateTime.Now.ToString("MMddhhmmss");
	// // this.Response.Redirect(item + ".aspx?RptNo=" + this.RptNo +
	// "&FK_Flow=" +
	// // this.FK_Flow+"&T="+tKey,true);
	// // }
	//
	// public Entities SetDGData() {
	// return this.SetDGData(BaseModel.getPageIdx());
	// }
	//
	// public Entities SetDGData(int pageIdx) {
	// // #region 执行数据分页查询，并绑定分页控件.
	//
	// Entities ens = this.getHisEns();
	// Entity en = ens.getGetNewEntity();
	// QueryObject qo = new QueryObject(ens);
	// qo = this.ToolBar1.GetnQueryObject(ens, en);
	//
	// // this.Pub2.Clear();
	// // this.UCSys1
	//
	// // BaseModel.BindPageIdxEasyUi(new StringBuilder(Pub2.ListToString()),
	// // qo.GetCount(),
	// // getPageID() + ".jsp?RptNo=" + getRptNo() + "&EnsName="
	// // + getRptNo() + "&FK_Flow=" + getFK_Flow(), pageIdx,
	// // SystemConfig.getPageSize(),
	// // "'first','prev','sep','manual','sep','next','last'", false);
	// Pub2.append("    <style type='text/css'>"
	// + "        #eupage table,#eupage td" + "        {"
	// + "            border: 0;" + "            padding: 0;"
	// + "            text-align: inherit;"
	// + "            background-color: inherit;"
	// + "            color: inherit;"
	// + "            font-size: inherit;" + "        }"
	// + "    </style>");
	//
	// Pub2.append(String
	// .format("<div id='eupage' class='easyui-pagination' data-options=\"total: %1$s,pageSize: %2$s,pageNumber: %3$s,showPageList: false,showRefresh: false,layout: [%4$s],beforePageText: '第&nbsp;',afterPageText: '&nbsp;/ {pages} 页',displayMsg: '显示 {from} 到 {to} 条，共 {total} 条'\"></div>",
	// qo.GetCount(), SystemConfig.getPageSize(), pageIdx,
	// "'first','prev','sep','manual','sep','next','last'"));
	//
	// Pub2.append("<script type='text/javascript'>");
	// Pub2.append(String
	// .format("$('#eupage').pagination({	onSelectPage:function(pageNumber, pageSize){		location.href='%1$s&PageIdx=' + pageNumber	}});",
	// getPageID() + ".jsp?RptNo=" + getRptNo() + "&EnsName="
	// + getRptNo() + "&FK_Flow=" + getFK_Flow()));
	// Pub2.append("</script>");
	// qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), pageIdx);
	// // #endregion 执行数据分页查询，并绑定分页控件.
	// //
	// // #region 检查是否显示按关键字查询，如果是就把关键标注为红色.
	//
	// if (en.getEnMap().IsShowSearchKey) {
	// String keyVal = this.ToolBar1.GetTBByID("TB_Key").getText();
	//
	// if (keyVal.length() >= 1) {
	// Attrs attrs = en.getEnMap().getAttrs();
	//
	// for (Entity myen : Entities.convertEntities(ens)) {
	// for (Attr attr : attrs) {
	// if (attr.getIsFKorEnum())
	// continue;
	//
	// if (attr.getIsPK())
	// continue;
	//
	// switch (attr.getMyDataType()) {
	// case DataType.AppRate:
	// case DataType.AppMoney:
	// case DataType.AppInt:
	// case DataType.AppFloat:
	// case DataType.AppDouble:
	// case DataType.AppBoolean:
	// continue;
	// default:
	// break;
	// }
	//
	// myen.SetValByKey(
	// attr.getKey(),
	// myen.GetValStrByKey(attr.getKey())
	// .replace(
	// keyVal,
	// "<font color=red>" + keyVal
	// + "</font>"));
	// }
	// }
	// }
	// }
	// // #endregion 检查是否显示按关键字查询，如果是就把关键标注为红色.
	//
	// // 处理entity的GuestNo 列的问题。
	//
	// // if (en.getEnMap().getAttrs().Contains(NDXRptBaseAttr.ex
	// // foreach (Entity en in ens)
	// // {
	// // }
	//
	// // 绑定数据.
	// this.BindEns(ens, null);
	//
	// // #region 生成翻页的js，暂不用
	// // int ToPageIdx = this.PageIdx + 1;
	// // int PPageIdx = this.PageIdx - 1;
	// // this.UCSys1.Add("<SCRIPT language=javascript>");
	// // this.UCSys1.Add("\t\n document.onkeydown = chang_page;");
	// // this.UCSys1.Add("\t\n function chang_page() { ");
	// // if (this.PageIdx == 1)
	// // {
	// //
	// this.UCSys1.Add("\t\n if (event.keyCode == 37 || event.keyCode == 33) alert('已经是第一页');");
	// // }
	// // else
	// // {
	// //
	// this.UCSys1.Add("\t\n if (event.keyCode == 37  || event.keyCode == 38 || event.keyCode == 33) ");
	// // this.UCSys1.Add("\t\n     location='" + this.PageID + ".aspx?RptNo="
	// // + this.RptNo + "&FK_Flow=" + this.currMapRpt.FK_Flow + "&PageIdx=" +
	// // PPageIdx + "';");
	// // }
	//
	// // if (this.PageIdx == maxPageNum)
	// // {
	// //
	// this.UCSys1.Add("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) alert('已经是最后一页');");
	// // }
	// // else
	// // {
	// //
	// this.UCSys1.Add("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) ");
	// // this.UCSys1.Add("\t\n     location='" + this.PageID + ".aspx?RptNo="
	// // + this.RptNo + "&FK_Flow=" + this.currMapRpt.FK_Flow + "&PageIdx=" +
	// // ToPageIdx + "';");
	// // }
	//
	// // this.UCSys1.Add("\t\n } ");
	// // this.UCSys1.Add("</SCRIPT>");
	// // #endregion 生成翻页的js
	//
	// return ens;
	// }
	//
	// private String GenerEnUrl(Entity en, Attrs attrs) {
	// String url = "";
	// for (Attr attr : attrs) {
	// switch (attr.getUIContralType()) {
	// case TB:
	// if (attr.getIsPK())
	// url += "&" + attr.getKey() + "="
	// + en.GetValStringByKey(attr.getKey());
	// break;
	// case DDL:
	// url += "&" + attr.getKey() + "="
	// + en.GetValStringByKey(attr.getKey());
	// break;
	// }
	// }
	// return url;
	// }
	//
	// // / <summary>
	// // / 绑定实体集合.
	// // / </summary>
	// // / <param name="ens"></param>
	// // / <param name="ctrlId"></param>
	// public void BindEns(Entities ens, String ctrlId) {
	// // #region 定义变量.
	// MapData md = new MapData(this.getRptNo());
	// // if (this.Page.Title == "")
	// // this.Page.Title = md.Name;
	//
	// // this.UCSys1.Controls.Clear();
	// Entity myen = ens.getGetNewEntity();
	// String pk = myen.getPK();
	// String clName = myen.toString();
	// Attrs attrs = myen.getEnMap().getAttrs();
	// // #endregion 定义变量.
	//
	// this.UCSys1
	// .append(BaseModel
	// .AddTable1("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%;line-height:22px'"));
	//
	// // #region 生成表格标题
	// this.UCSys1.append(BaseModel.AddTR());
	// this.UCSys1.append(BaseModel.AddTDGroupTitle(
	// "style='text-align:center'", "序"));
	// this.UCSys1.append(BaseModel.AddTDGroupTitle("标题"));
	//
	// for (Attr attr : attrs) {
	// if (attr.getIsRefAttr() || attr.getKey() == "Title"
	// || attr.getKey() == "MyNum")
	// continue;
	//
	// this.UCSys1.append(BaseModel.AddTDGroupTitle(attr.getDesc()));
	// }
	//
	// this.UCSys1.append(BaseModel.AddTREnd());
	// // #endregion 生成表格标题
	//
	// // #region 用户界面属性设置
	//
	// int pageidx = BaseModel.getPageIdx() - 1;
	// int idx = SystemConfig.getPageSize() * pageidx;
	// // #endregion 用户界面属性设置
	// //
	// // #region 数据输出.
	//
	// for (Entity en : Entities.convertEntities(ens)) {
	// // #region 输出字段。
	//
	// idx++;
	// this.UCSys1.append(BaseModel.AddTR());
	// this.UCSys1.append(BaseModel.AddTDIdx(idx));
	// UCSys1.append(BaseModel.AddTD("<a href=\"javascript:WinOpen('"
	// + Glo.getCCFlowAppPath() + "WF/WFRpt.jsp?FK_Flow="
	// + currMapRpt.getFK_Flow() + "&WorkID="
	// + en.GetValStrByKey("OID") + "','tdr');\" >"
	// + en.GetValByKey("Title") + "</a>"));
	//
	// for (Attr attr : attrs) {
	// if (attr.getIsRefAttr() || attr.getKey() == "MyNum"
	// || attr.getKey() == "Title")
	// continue;
	//
	// if (attr.getUIContralType() == UIContralType.DDL) {
	// String s = en.GetValRefTextByKey(attr.getKey());
	// if (StringHelper.isNullOrEmpty(s)) {
	// if (attr.getKey() != null) {
	// if (attr.getKey().equals("FK_NY")) {
	// s = en.GetValStringByKey(attr.getKey());
	// break;
	// }
	// s = en.GetValStringByKey(attr.getKey());
	// break;
	// }
	// // switch ()
	// // {
	// // case "FK_NY": // 2012-01
	// // s = en.GetValStringByKey(attr.Key);
	// // break;
	// // default: //其他的情况，把编码输出出来.
	// // s = en.GetValStringByKey(attr.Key);
	// // break;
	// // }
	// }
	// this.UCSys1.append(BaseModel.AddTD(s));
	// continue;
	// }
	//
	// String str = en.GetValStrByKey(attr.getKey());
	//
	// switch (attr.getMyDataType()) {
	// case DataType.AppDate:
	// case DataType.AppDateTime:
	// if (str == "" || str == null)
	// str = "&nbsp;";
	// this.UCSys1.append(BaseModel.AddTD(str));
	// break;
	// case DataType.AppString:
	// if (str == "" || str == null)
	// str = "&nbsp;";
	//
	// if (attr.getUIHeight() != 0)
	// this.UCSys1.append(BaseModel.AddTDDoc(str, str));
	// else
	// this.UCSys1.append(BaseModel.AddTD(str));
	// break;
	// case DataType.AppBoolean:
	// if (str == "1")
	// this.UCSys1.append(BaseModel.AddTD("是"));
	// else
	// this.UCSys1.append(BaseModel.AddTD("否"));
	// break;
	// case DataType.AppFloat:
	// case DataType.AppInt:
	// case DataType.AppRate:
	// case DataType.AppDouble:
	// this.UCSys1.append(BaseModel.AddTDNum((new BigDecimal(str))
	// .setScale(2, java.math.BigDecimal.ROUND_HALF_UP)
	// .doubleValue()
	// + ""));
	// break;
	// case DataType.AppMoney:
	// this.UCSys1.append(BaseModel.AddTDNum((new BigDecimal(str))
	// .setScale(2, java.math.BigDecimal.ROUND_HALF_UP)
	// .doubleValue()
	// + ""));
	// break;
	// default:
	// try {
	// throw new Exception("no this case ...");
	// } catch (Exception e) {
	// 
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// this.UCSys1.append(BaseModel.AddTREnd());
	// // #endregion 输出字段。
	// }
	// // #endregion 数据输出.
	// //
	// // #region 计算一下是否可以求出合计,主要是判断是否有数据类型在这个Entities中。
	//
	// boolean IsHJ = false;
	// for (Attr attr : attrs) {
	// if (attr.getMyFieldType() == FieldType.RefText
	// || attr.getKey() == "Title" || attr.getKey() == "MyNum")
	// continue;
	//
	// if (attr.getUIVisible() == false)
	// continue;
	//
	// if (attr.getUIContralType() == UIContralType.DDL)
	// continue;
	//
	// if (attr.getKey() == "OID" || attr.getKey() == "MID"
	// || attr.getKey() == "FID" || attr.getKey() == "PWorkID"
	// || attr.getKey().toUpperCase() == "WORKID")
	// continue;
	//
	// switch (attr.getMyDataType()) {
	// case DataType.AppDouble:
	// case DataType.AppFloat:
	// case DataType.AppInt:
	// case DataType.AppMoney:
	// IsHJ = true;
	// break;
	// default:
	// break;
	// }
	// }
	// // #endregion 计算一下是否可以求出合计,主要是判断是否有数据类型在这个Entities中。
	// //
	// // #region 输出合计。
	//
	// if (IsHJ) {
	// this.UCSys1.append("<TR class='Sum' >");
	// this.UCSys1.append(BaseModel.AddTD());
	// this.UCSys1.append(BaseModel.AddTD("合计"));
	// for (Attr attr : attrs) {
	// if (attr.getKey() == "MyNum")
	// continue;
	//
	// if (attr.getUIContralType() == UIContralType.DDL) {
	// this.UCSys1.append(BaseModel.AddTD());
	// continue;
	// }
	//
	// if (attr.getUIVisible() == false)
	// continue;
	//
	// if (attr.getKey() == "OID" || attr.getKey() == "MID"
	// || attr.getKey().toUpperCase() == "WORKID"
	// || attr.getKey() == "FID") {
	// this.UCSys1.append(BaseModel.AddTD());
	// continue;
	// }
	//
	// switch (attr.getMyDataType()) {
	// case DataType.AppDouble:
	// this.UCSys1.append(BaseModel.AddTDNum(ens
	// .GetSumDecimalByKey(attr.getKey())));
	// break;
	// case DataType.AppFloat:
	// this.UCSys1.append(BaseModel.AddTDNum(ens
	// .GetSumDecimalByKey(attr.getKey())));
	// break;
	// case DataType.AppInt:
	// this.UCSys1.append(BaseModel.AddTDNum(ens
	// .GetSumDecimalByKey(attr.getKey())));
	// break;
	// case DataType.AppMoney:
	// this.UCSys1.append(BaseModel.AddTDJE(ens
	// .GetSumDecimalByKey(attr.getKey())));
	// break;
	// default:
	// this.UCSys1.append(BaseModel.AddTD());
	// break;
	// }
	// }
	// /* 结束循环 */
	// this.UCSys1.append(BaseModel.AddTD());
	// this.UCSys1.append(BaseModel.AddTREnd());
	// }
	//
	// // #endregion
	//
	// this.UCSys1.append(BaseModel.AddTableEnd());
	// }

	// private void ToolBar1_ButtonClick(object sender, System.EventArgs e)
	// {
	// try
	// {
	// var btn = (LinkBtn)sender;
	//
	// switch (btn.ID)
	// {
	// case NamesOfBtn.Export: //数据导出.
	// case NamesOfBtn.Excel: //数据导出
	// MapData md = new MapData(this.RptNo);
	// Entities ens = md.HisEns;
	// Entity en = ens.GetNewEntity;
	// QueryObject qo = new QueryObject(ens);
	// qo = this.ToolBar1.GetnQueryObject(ens, en);
	//
	// DataTable dt = qo.DoQueryToTable();
	// DataTable myDT = new DataTable();
	// MapAttrs attrs = new MapAttrs(this.RptNo);
	//
	// foreach (MapAttr attr in attrs)
	// {
	// myDT.Columns.Add(new DataColumn(attr.Name, typeof(string)));
	// }
	//
	// foreach (DataRow dr in dt.Rows)
	// {
	// DataRow myDR = myDT.NewRow();
	// foreach (MapAttr attr in attrs)
	// {
	// myDR[attr.Name] = dr[attr.Field];
	// }
	// myDT.Rows.Add(myDR);
	// }
	//
	// string file = "";
	// try
	// {
	// ExportDGToExcel(myDT, en.EnDesc);
	// }
	// catch (Exception ex)
	// {
	// throw new
	// Exception("数据没有正确导出可能的原因之一是:系统管理员没正确的安装Excel组件，请通知他，参考安装说明书解决。@系统异常信息：" +
	// ex.Message);
	// }
	//
	// this.SetDGData();
	// return;
	// default:
	// this.PageIdx = 1;
	// this.SetDGData(1);
	// this.ToolBar1.SaveSearchState(this.RptNo, null);
	// return;
	// }
	// }
	// catch (Exception ex)
	// {
	// if (!(ex is System.Threading.ThreadAbortException))
	// {
	// this.ResponseWriteRedMsg(ex);
	// //在这里显示错误
	// }
	// }
	// }
}
