package cn.jflow.common.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.TextBox;
import BP.DA.DBType;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Port.Station;
import BP.Port.Stations;
import BP.Sys.SystemConfig;
import BP.Tools.DateUtils;
import BP.WF.Glo;
import BP.WF.Flow;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.WF.Flows;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.NodeEmp;
import BP.WF.NodeFormType;
import BP.WF.WorkAttr;
import BP.WF.Works;
import BP.Web.WebUser;

public class FlowSearchModel extends BaseModel {

	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	private String basePath;
	private final int PageSize = 600;
	private String pageSmall = null;

	public FlowSearchModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		this.request.set(request);
		this.response.set(response);
	}

	public String init() {
		StringBuffer htmlStr = new StringBuffer();
		String doType = getDoType();
		String FK_Flow = getRequest().getParameter("FK_Flow");
		String FK_Node = getRequest().getParameter("FK_Node");
		if (FK_Node == null) {
			FK_Node = "0";
		}else{
			FK_Node = String.valueOf(Integer.parseInt(FK_Node));
		}
		basePath = Glo.getCCFlowAppPath();
		boolean isWap = WebUser.getIsWap();
		if ("Bill".equals(doType)) {
			return BindBill(FK_Flow);
		} else if ("MyWork".equals(doType)) {
			return BindMyWork(FK_Node);
		}
		if (FK_Flow != null) {
			if (isWap) {
				return BindFlowWap(FK_Flow);
			} else {
				return BindFlow(FK_Flow);
			}
		}
		if (!"0".equals(FK_Node)) {
			return null;
		}
		if (isWap) {
			return BindWap();
		}
		int colspan = 5;
		//htmlStr.append(AddTable("align='left'"));
		if (isWap)
			htmlStr.append(AddCaption("<img src='"
					+ basePath
					+ "WF/Img/Home.gif' ><a href='Home.jsp' >Home</a> - <img src='"
					+ basePath + "WF/Img/Search.gif' >-流程查询"));
		else
			htmlStr.append(AddCaption("<img src='"
					+ basePath
					+ "WF/Img/Search.gif' >流程查询/分析<span style='float:right'><a href=\"javascript:WinOpen('"
					+ basePath + "WF/KeySearch.jsp',900,900); \">关键字查询</a>"
							+ "|<a href=\"javascript:WinOpen('"
					+ basePath
					+ "WF/Comm/Search.jsp?EnsName=BP.WF.Data.GenerWorkFlowViews',900,900); \">综合查询</a>|<a href=\"javascript:WinOpen('"
					+ basePath
					+ "WF/Comm/Group.jsp?EnsName=BP.WF.Data.GenerWorkFlowViews',900,900); \">综合分析</a>"
					+ "|<a href=\"javascript:WinOpen('"
					+ basePath
					+ "WF/Comm/Search.jsp?EnsName=BP.WF.Entity.WorkFlowDeleteLogs',900,900); \">删除日志</a>"));

		htmlStr.append(AddTR());
		htmlStr.append(AddTDTitle("序"));
		htmlStr.append(AddTDTitle("流程名称"));
		htmlStr.append(AddTDTitle("单据"));
		htmlStr.append(AddTDTitle("流程查询-分析"));
		htmlStr.append(AddTREnd());

		Flows fls = new Flows();
		fls.RetrieveAll();
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();
		String search = "查询";
		String bill = "单据";
		String FX = "分析";
		int idx = 1;
		int gIdx = 0;
		for (int i = 0; i < fss.size(); i++) {
			FlowSort fs = (FlowSort) fss.get(i);
			if ("0".equals(fs.getParentNo())) {
				continue;
			}

			gIdx++;
			htmlStr.append(AddTR());
			htmlStr.append(AddTDB(
					"colspan=" + colspan
							+ " onclick=\"GroupBarClick('" + gIdx
							+ "')\" ",
					"<div style='text-align:left; float:left' ><img src='"+basePath+"WF/Img/Min.gif' alert='Min' id='Img"
							+ gIdx
							+ "' border=0 />&nbsp;<b>"
							+ fs.getName()
							+ "</b>"));

			htmlStr.append(AddTREnd());
			for (int j = 0; j < fls.size(); j++) {
				Flow fl = (Flow) fls.get(j);
				String no = fl.getNo();
				String name = fl.getName();
				if (!fs.getNo().equals(fl.getFK_FlowSort())) {
					continue;
				}

				htmlStr.append(AddTR("ID='" + gIdx + "_" + idx + "'"));
				htmlStr.append(AddTDIdx(idx++));

				if (!isWap)
					htmlStr.append(AddTD(
							"<a href=\"javascript:WinOpen('"+basePath+"WF/track/ChartTrack.jsp?FK_Flow="
									+ no + "&DoType=Chart','sd');\">" + name
									+ "</a>"));
				else
					htmlStr.append(AddTD(name));

				if (fl.getNumOfBill() == 0) {
					htmlStr.append(AddTD("无"));
				} else {
					String src = basePath
							+ "WF/Rpt/Bill.jsp?EnsName=BP.WF.Bills&FK_Flow="
							+ no;
					htmlStr.append(AddTD("<a href=\"javascript:WinOpen('" + src
							+ "');\"  ><img src='" + basePath
							+ "WF/Img/Btn/Word.gif' border=0/>" + bill + "</a>"));
				}
				htmlStr.append(AddTDBegin());
				String src2 = basePath + "WF/Rpt/Search.jsp?RptNo=ND" + Integer.parseInt(no)
						+ "MyRpt&FK_Flow=" + no;
				htmlStr.append("<a href=\"javascript:WinOpen('" + src2
						+ "');\" >" + search + "</a>");
				src2 = basePath + "WF/RptDfine/Group.jsp?FK_Flow=" + no
						+ "&DoType=Dept";
				htmlStr.append(" - <a href=\"javascript:WinOpen('" + src2
						+ "');\" >" + FX + "</a>");
				htmlStr.append(AddTDEnd());
				htmlStr.append(AddTREnd());
			}
		}
		//htmlStr.append(AddTableEnd());

		return htmlStr.toString();
	}

	public String BindBill(String FK_Flow) {
		StringBuffer htmlStr = new StringBuffer();
		Flow fl1 = new Flow(FK_Flow);
		//htmlStr.append(AddTable());
		htmlStr.append(AddCaption("您的位置:单据查询 <a href='FlowSearch.jsp' >返回</a> => "
				+ fl1.getName()));
		htmlStr.append(AddTR());
		htmlStr.append("<TD>");
		String src = "" + basePath
				+ "WF/Comm/Search.jsp?EnsName=BP.WF.Bills&FK_Flow=" + FK_Flow;
		htmlStr.append("<iframe ID='f23' frameborder='0' style='padding:0px;border:0px;' leftMargin='0' topMargin='0' src='"
				+ src
				+ "' height='100%' width='100%' scrolling='no'/></iframe>");
		htmlStr.append(AddTDEnd());
		htmlStr.append(AddTREnd());
		//htmlStr.append(AddTableEnd());
		return htmlStr.toString();

		// String ens = "BP.WF.Bills";
		// Bills bills = new Bills();
		// QueryObject qo = new QueryObject(bills);
		// qo.AddWhere(BillAttr.FK_Flow, this.FK_Flow);
		// qo.addAnd();
		// qo.addLeftBracket();
		// qo.AddWhere(BillAttr.FK_Emp, WebUser.getNo());
		// qo.addOr();
		// qo.AddWhere(BillAttr.FK_Starter, WebUser.getNo());
		// qo.addRightBracket();
		// qo.DoQuery();
		//
		// if (this.FK_Flow != null)
		// {
		// Flow fl = new Flow(this.FK_Flow);
		// htmlStr.append(AddTable("width=100%");
		// htmlStr.append(AddCaption("您的位置：单据查询 <a href='FlowSearch" +
		// this.PageSmall + ".aspx' >返回</a> => " + fl.Name);
		// }
		// else
		// {
		// htmlStr.append(AddTable();
		// }
		//
		// htmlStr.append(AddTR();
		// htmlStr.append(AddTDTitle("ID");
		// htmlStr.append(AddTDTitle("标题");
		// htmlStr.append(AddTDTitle("发起");
		// htmlStr.append(AddTDTitle("发起日期");
		// htmlStr.append(AddTDTitle("发起人部门");
		// htmlStr.append(AddTDTitle("单据名称");
		// htmlStr.append(AddTDTitle("打印人");
		// htmlStr.append(AddTDTitle("打印日期");
		// htmlStr.append(AddTDTitle("月份");
		// htmlStr.append(AddTREnd();
		//
		// int i = 0;
		// bool is1 = false;
		// foreach (BP.WF.Data.Bill bill in bills)
		// {
		// htmlStr.append(AddTR(is1);
		// i++;
		// htmlStr.append(AddTDIdx(i);
		// htmlStr.append(AddTD(bill.Title);
		// htmlStr.append(AddTD(bill.FK_StarterT);
		// htmlStr.append(AddTD(bill.RDT);
		// htmlStr.append(AddTD(bill.FK_DeptT);
		// htmlStr.append(AddTDA("javascript:WinOpen('" + bill.Url + "')",
		// "<img src='" + BP.WF.Glo.CCFlowAppPath +
		// "WF/Img/Btn/Word.gif' border=0 />" + bill.FK_BillText);
		// htmlStr.append(AddTD(bill.FK_EmpT);
		// htmlStr.append(AddTD(bill.RDT);
		// htmlStr.append(AddTD(bill.FK_NY);
		// htmlStr.append(AddTREnd();
		// }
		// htmlStr.append(AddTableEnd();
		// return null;
	}

	public String BindMyWork(String FK_Node) {
		StringBuilder pub2 = new StringBuilder();
		StringBuffer pub1 = new StringBuffer();
		Node nd = new Node(FK_Node);
		Works wks = nd.getHisWorks();
		QueryObject qo = new QueryObject(wks);
		qo.AddWhere(WorkAttr.Rec, WebUser.getNo());
		qo.addAnd();
		if (SystemConfig.getAppCenterDBType() == DBType.Access)
			qo.AddWhere("Mid(RDT,1,10) >='" + getDT_F()
					+ "' AND Mid(RDT,1,10) <='" + getDT_T() + "' ");
		else
			qo.AddWhere("" + SystemConfig.getAppCenterDBSubstringStr()
					+ "(RDT,1,10) >='" + getDT_F() + "' AND "
					+ BP.Sys.SystemConfig.getAppCenterDBSubstringStr()
					+ "(RDT,1,10) <='" + getDT_T() + "' ");

		BindPageIdx(pub2, qo.GetCount(), this.PageSize, getPageIdx(),
				"FlowSearch" + getPageSmall() + ".jsp?FK_Node=" + FK_Node);
		qo.DoQuery("OID", this.PageSize, getPageIdx());

		// 生成页面数据。
		Attrs attrs = nd.getHisWork().getEnMap().getAttrs();
		List<Attr> attrList = attrs.subList(0, attrs.size());
		int colspan = 2;
		for (Attr attr : attrList) {
			if (!attr.getUIVisible()) {
				continue;
			}
			colspan++;
		}
		if (!"".equals(getPageSmall()))
			pub1.append(AddBR());

		//pub1.append(AddTable("align='center'"));
		// pub1.append(AddTR();
		// pub1.append(Add("<TD class=TitleTop colspan=" + colspan + "></TD>");
		// pub1.append(AddTREnd();

		pub1.append(AddTR());
		pub1.append(("<TD align='left' colspan='" + colspan
				+ "'><img src='" + basePath
				+ "WF/Img/EmpWorks.gif'><b><a href='FlowSearch"
				+ getPageSmall() + ".jsp'>流程查询</a>-<a href='FlowSearch"
				+ getPageSmall() + ".jsp?FK_Flow=" + nd.getFK_Flow() + "'>"
				+ nd.getFlowName() + "</a>-" + nd.getName() + "</b></TD>"));
		pub1.append(AddTREnd());

		pub1.append(AddTR());
		pub1.append(("<TD colspan='" + colspan + "' class='TD'>发生日期从:"));

		TextBox tb = new TextBox();
		tb.setId("TB_F");
		tb.setCols(10);
		tb.setText(getDT_F());
		tb.addAttr("onfocus", "WdatePicker();");
		pub1.append(tb.toString());

		pub1.append("到:");
		tb = new TextBox();
		tb.setId("TB_T");
		tb.setText(getDT_T());
		tb.setCols(7);
		tb.addAttr("onfocus", "WdatePicker();");
		pub1.append(tb.toString());

		Button btn = new Button();
		btn.setText("查 询");
		btn.setCssClass("Btn");
		btn.setId("Btn_Search");
		btn.addAttr("onclick", "");
		pub1.append(btn.toString());

		btn = new Button();
		btn.setText("导出Excel");
		btn.setCssClass("Btn");
		btn.setId("Btn_Excel");
		btn.addAttr("onclick", "");
		pub1.append(btn.toString());
		pub1.append(AddTDEnd());
		pub1.append(AddTREnd());

		pub1.append(AddTR());
		pub1.append(AddTDTitle("序"));
		for (Attr attr : attrList) {
			if (!attr.getUIVisible())
				continue;
			pub1.append(AddTDTitle(attr.getDesc()));
		}
		pub1.append(AddTDTitle("操作"));
		pub1.append(AddTREnd());

		int idx = 0;
		boolean is1 = false;
		for (Object obj : wks.subList(0, wks.size())) {
			Entity en = (Entity) obj;
			idx++;
			pub1.append(AddTR(is1));
			pub1.append(AddTD(idx));
			for (Attr attr : attrList) {
				if (!attr.getUIVisible())
					continue;
				switch (attr.getMyDataType()) {
				case DataType.AppBoolean:
					pub1.append(AddTD(en.GetValBoolStrByKey(attr.getKey())));
					break;
				case DataType.AppFloat:
				case DataType.AppDouble:
					pub1.append(AddTD(en.GetValFloatByKey(attr.getKey())));
					break;
				case DataType.AppInt:
					if (attr.getUIContralType() == UIContralType.DDL) {
						pub1.append(AddTD(en.GetValRefTextByKey(attr.getKey())));
					} else {
						pub1.append(AddTD(en.GetValIntByKey(attr.getKey())));
					}
					break;
				case DataType.AppMoney:
					pub1.append(AddTDMoney(en.GetValDecimalByKey(attr.getKey())));
					break;
				default:
					pub1.append(AddTD(en.GetValStrByKey(attr.getKey())));
					break;
				}
			}
			pub1.append(AddTD("<a href=\"./../WF/WFRpt.jsp?WorkID="
					+ en.GetValIntByKey("OID")
					+ "&FID="
					+ en.GetValByKey("FID")
					+ "&FK_Flow="
					+ nd.getFK_Flow()
					+ "\" target=bk >报告</a>-<a href=\"./../WF/Chart.aspx?WorkID="
					+ en.GetValIntByKey("OID") + "&FID="
					+ en.GetValByKey("FID") + "&FK_Flow=" + nd.getFK_Flow()
					+ "\" target=bk >轨迹</a>"));
			pub1.append(AddTREnd());
		}

		pub1.append(AddTRSum());
		pub1.append(AddTD(""));
		for (Attr attr : attrList) {
			if (!attr.getUIVisible())
				continue;
			switch (attr.getMyDataType()) {
			case DataType.AppFloat:
			case DataType.AppInt:
			case DataType.AppDouble:
				pub1.append(AddTDB(wks.GetSumDecimalByKey(attr.getKey())
						.toString()));
				break;
			case DataType.AppMoney:
				pub1.append(AddTDB(""
						+ wks.GetSumDecimalByKey(attr.getKey())
								.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue()));
				break;
			default:
				pub1.append(AddTD());
				break;
			}
		}
		pub1.append(AddTD());
		pub1.append(AddTREnd());
		//pub1.append(AddTableEnd());
		return pub2.toString() + pub1.toString();
	}

	public String BindFlowWap(String FK_Flow) {
		StringBuffer htmlStr = new StringBuffer();
		Flow fl = new Flow(FK_Flow);
		int colspan = 4;

		//htmlStr.append(AddTable("width='600px' "));
		htmlStr.append(AddTR());
		htmlStr.append("<TD colspan='" + colspan + "'></TD>");
		htmlStr.append(AddTREnd());

		htmlStr.append(AddTR());
		htmlStr.append("<TD colspan='" + colspan
				+ "' align='left'><img src='" + basePath
				+ "WF/Img/Start.gif' > <b><a href='FlowSearch.jsp' >返回</a> - "
				+ fl.getName() + "</b></TD>");
		htmlStr.append(AddTREnd());

		htmlStr.append(AddTR());
		htmlStr.append(AddTDTitle("步骤"));
		htmlStr.append(AddTDTitle("节点"));
		htmlStr.append(AddTDTitle("可执行否?"));
		htmlStr.append(AddTREnd());

		Nodes nds = new Nodes(FK_Flow);
		Stations sts = WebUser.getHisStations();
		for (Object obj : nds.subList(0, nds.size())) {
			Node nd = (Node) obj;
			boolean isCan = false;
			for (Object obj1 : sts.subList(0, sts.size())) {
				Station st = (Station) obj1;
				if (nd.getHisStas().contains("@" + st.getNo())) {
					isCan = true;
					break;
				}
			}

			if (isCan == false) {
				NodeEmp ne = new NodeEmp();
				ne.setFK_Emp(WebUser.getNo());
				ne.setFK_Node(nd.getNodeID());
				if (ne.getIsExits())
					isCan = true;
			}

			htmlStr.append(AddTR());
			htmlStr.append(AddTDIdx(nd.getStep()));
			if (isCan)
				htmlStr.append(AddTD("<a href='FlowSearch"
						+ this.getPageSmall() + ".jsp?FK_Node="
						+ nd.getNodeID() + "'>" + nd.getName() + "</a>"));
			else
				htmlStr.append(AddTD(nd.getName()));

			if (isCan) {
				htmlStr.append(AddTD("可执行"));
			} else {
				htmlStr.append(AddTD("不可执行"));
			}
			htmlStr.append(AddTREnd());
		}

		htmlStr.append(AddTRSum());
		htmlStr.append(AddTD("colspan=" + colspan, "&nbsp;"));
		htmlStr.append(AddTREnd());
		//htmlStr.append(AddTableEnd());
		return htmlStr.toString();
	}

	public String BindFlow(String FK_Flow) {
		StringBuffer htmlStr = new StringBuffer();
		Flow fl = new Flow(FK_Flow);
		int colspan = 4;

		if (!"".equals(getPageSmall()))
			htmlStr.append(AddBR());

		//htmlStr.append(AddTable());
		htmlStr.append(AddTR());
		htmlStr.append("<TD colspan='" + colspan + "'></TD>");
		htmlStr.append(AddTREnd());

		htmlStr.append(AddTR());
		htmlStr.append("<TD colspan='" + colspan
				+ "' align='left'><img src='" + basePath
				+ "WF/Img/Start.gif' > <b><a href='FlowSearch" + getPageSmall()
				+ ".jsp' >返回</a> - " + fl.getName() + "</b></TD>");
		htmlStr.append(AddTREnd());

		htmlStr.append(AddTR());
		htmlStr.append("<TD colspan='" + colspan + "'></TD>");
		htmlStr.append(AddTREnd());

		htmlStr.append(AddTR());
		htmlStr.append(AddTDTitle("节点步骤"));
		htmlStr.append(AddTDTitle("节点"));
		htmlStr.append(AddTDTitle("可执行否?"));
		htmlStr.append(AddTDTitle("操作"));
		htmlStr.append(AddTREnd());

		Nodes nds = new Nodes(FK_Flow);
		Stations sts = WebUser.getHisStations();
		for (Object obj : nds.subList(0, nds.size())) {
			Node nd = (Node) obj;
			htmlStr.append(AddTR());
			htmlStr.append(AddTD(nd.getStep()));
			htmlStr.append(AddTD(nd.getName()));

			boolean isCan = false;
			if (nd.getHisFormType() == NodeFormType.SDKForm
					|| nd.getHisFormType() == NodeFormType.SelfForm) {
				isCan = false;
			} else {
				for (Object obj1 : sts.subList(0, sts.size())) {
					Station st = (Station) obj1;
					if (nd.getHisStas().contains("@" + st.getNo())) {
						isCan = true;
						break;
					}
				}
			}

			if (isCan) {
				htmlStr.append(AddTD("可执行"));
				htmlStr.append(AddTD("<a href='FlowSearch"
						+ this.getPageSmall() + ".jsp?FK_Node="
						+ nd.getNodeID() + "'>查询</a>"));
			} else {
				htmlStr.append(AddTD("不可执行"));
				htmlStr.append(AddTD());
			}
			htmlStr.append(AddTREnd());
		}
		htmlStr.append(AddTRSum());
		htmlStr.append(AddTD("colspan=" + colspan, "&nbsp;"));
		htmlStr.append(AddTREnd());
		//htmlStr.append(AddTableEnd());
		return htmlStr.toString();
	}

	public String BindWap() {
		StringBuffer htmlStr = new StringBuffer();
		htmlStr.append(AddFieldSet("<a href='Home.jsp' ><img src='" + basePath
				+ "WF/Img/Home.gif' >Home</a>"));
		Flows fls = new Flows();
		fls.RetrieveAll();
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();

		htmlStr.append(AddUL());
		for (int i = 0; i < fss.size(); i++) {
			FlowSort fs = (FlowSort) fss.get(i);
			htmlStr.append(AddBR(fs.getName()));
			for (int j = 0; j < fls.size(); j++) {
				Flow fl = (Flow) fls.get(j);
				if (!fs.getNo().equals(fl.getFK_FlowSort())) {
					continue;
				}
				String src2 = basePath + "WF/Rpt/Search.jsp?EnsName=ND"
						+ Integer.parseInt(fl.getNo()) + "Rpt&FK_Flow=" + fl.getNo() + "&IsWap=1";
				htmlStr.append(AddLi("<a href='" + src2 + "' >" + fl.getName()
						+ "</a>"));
			}
		}
		htmlStr.append(AddULEnd());
		htmlStr.append(AddFieldSetEnd());
		return htmlStr.toString();
	}

	public String getDT_F() {
		Object f = getRequest().getSession().getAttribute("DF");
		if (f == null) {
			String d = DateUtils.format(DateUtils.addDay(new Date(), -30),
					"yyyy-MM-dd");
			getRequest().getSession().setAttribute("DF", d);
			return d;
		}
		return f.toString();
	}

	public String getDT_T() {
		Object f = getRequest().getSession().getAttribute("DT");
		if (f == null) {
			String d = DateUtils.getCurrentDate("yyyy-MM-dd");
			getRequest().getSession().setAttribute("DT", d);
			return d;
		}
		return f.toString();
	}

	public String getPageSmall() {
		if (pageSmall == null) {
			if (getPageID().toLowerCase().contains("smallsingle"))
				pageSmall = "SmallSingle";
			else if (getPageID().toLowerCase().contains("small"))
				pageSmall = "Small";
			else
				pageSmall = "";
		}
		return pageSmall;
	}

	public HttpServletRequest getRequest() {
		return request.get();
	}

	public HttpServletResponse getResponse() {
		return response.get();
	}

}
