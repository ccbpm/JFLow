<%@page import="BP.WF.Template.CCListAttr"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.DA.DataRow"%>
<%@page import="BP.DA.DataType"%>
<%@page import="BP.DA.DataTable"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.Tools.StringHelper"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
%>
<%
	String FK_Flow = request.getParameter("FK_Flow")==null ? "":request.getParameter("FK_Flow");
	String GroupBy = request.getParameter("GroupBy") == null? "": request.getParameter("GroupBy");
	String Sta = request.getParameter("Sta") == null ? "0" : request.getParameter("Sta");
	
	if("".equals(Sta)){
		Sta = request.getAttribute("Sta") == null?"":request.getAttribute("Sta").toString();
	}
	if("".equals(FK_Flow)){
		FK_Flow = request.getAttribute("FK_Flow") == null?"":request.getAttribute("FK_Flow").toString();
	}
	if("".equals(GroupBy)){
		GroupBy = request.getAttribute("GroupBy") == null?"":request.getAttribute("GroupBy").toString();
	}
%>
<%!
	StringBuffer infoCtrl;
	public void Add(String str) {
		infoCtrl.append(str);
	}

	public void AddTable(String attr) {
		this.Add("<Table class='am-table am-table-striped am-table-hover table-main'" + attr + " >");
	}

	public void AddTableEnd() {
		this.Add("</Table>");
	}

	public void AddCaption(String str) {
		this.Add("\n<th class='table-title' colspan='6'>" + str + "</th>");
	}

	public void AddTR() {
		this.Add("\n<TR>");
	}

	public void AddTR(String attr) {
		this.Add("\n<TR " + attr + " >");
	}

	public void AddTREnd() {
		this.Add("\n</TR>");
	}

	public void AddTRSum() {
		this.Add("\n<TR>");
	}

	public void AddTDTitle(String str) {
		this.Add("\n<TH class='table-title'>" + str + "</TH>");
	}

	public void AddTD(String attr, String str) {
		this.Add("\n<TD " + attr + " >" + str + "</TD>");
	}

	public void AddTDIdx(int idx) {
		this.Add("\n<TD nowrap>" + idx + "</TD>");
	}

	public void AddTDB(String attr, String str) {
		this.Add("\n<TD  " + attr + " nowrap=true ><b>" + str + "</b></TD>");
	}

	public void AddTDBigDoc(String str) {
		this.Add("\n<TD valign=top>" + str + "</TD>");
	}

	public void AddTDB(String str) {
		this.Add("\n<TD  nowrap=true ><b>" + str + "</b></TD>");
	}

	public void AddTD(String str) {
		if (null == str || "".equals(str))
			this.Add("\n<TD  nowrap >&nbsp;</TD>");
		else
			this.Add("\n<TD  nowrap >" + str + "</TD>");
	}

	public String GenerMenu(String basePath, String FK_Flow) {
		String msg = "<a href='CC.jsp?Sta=-1&FK_Flow="
				+ FK_Flow + "' >全部</a> - " + "<a href='CC.jsp?Sta=0&FK_Flow=" + FK_Flow + "' >未读</a> - "
				+ "<a href='CC.jsp?Sta=1&FK_Flow=" + FK_Flow
				+ "' >已读</a> - " + "<a href='CC.jsp?Sta=2&FK_Flow=" + FK_Flow + "' >删除</a>";
		return msg;
	}

	public void Bind(DataTable dt, String basePath, String FK_Flow,
			String GroupBy, String Sta) {
		String groupVals = "";
		for (DataRow dr : dt.Rows) {
			if (groupVals.contains("@" + dr.getValue(GroupBy) + ","))
				continue;
			groupVals += "@" + dr.getValue(GroupBy) + ",";
		}
		int colspan = 9;
		//this.AddTable("align=left");
		this.AddCaption("<img src='" + basePath + "WF/Img/CCSta/CC.gif' >"
				+ GenerMenu(basePath, FK_Flow));
		this.AddTR();
		this.AddTDTitle("ID");
		this.AddTDTitle("流程标题");
		this.AddTDTitle("内容");

		if (!"FlowName".equals(GroupBy))
			this.AddTDTitle("<a href='CC.jsp?GroupBy=FlowName&DoType=CC&Sta=" + Sta
					+ "&FK_Flow=" + FK_Flow + "' >流程</a>");

		if (!"NodeName".equals(GroupBy))
			this.AddTDTitle("<a href='CC.jsp?GroupBy=NodeName&DoType=CC&Sta=" + Sta
					+ "&FK_Flow=" + FK_Flow + "' >节点</a>");

		if (!"Rec".equals(GroupBy))
			this.AddTDTitle("<a href='CC.jsp?GroupBy=Rec&DoType=CC&Sta=" + Sta
					+ "&FK_Flow=" + FK_Flow + "' >抄送人</a>");

		if ("1".equals(Sta))
			this.AddTDTitle("删除");
		this.AddTREnd();

		int i = 0;
		String[] gVals = groupVals.split("@");
		int gIdx = 0;
		for (String g : gVals) {
			if (StringHelper.isNullOrEmpty(g))
				continue;
			gIdx++;
			this.AddTR();
			this.AddTD(
					"colspan='" + colspan
							+ "' class='Sum' onclick=\"GroupBarClick('"
							+ basePath + "','" + gIdx + "')\" ",
					"<div style='text-align:left; float:left' ><img src='"
							+ basePath + "WF/Img/Min.gif' alert='Min' id='Img"
							+ gIdx + "'   border=0 />&nbsp;<b>"
							+ g.replace(",", "") + "</b>");
			this.AddTREnd();
			for (DataRow dr : dt.Rows) {
				if (!g.equals(dr.getValue(GroupBy) + ","))
					continue;
				this.AddTR("ID='" + gIdx + "_" + i + "'");
				i++;
				int sta = Integer.parseInt(dr.getValue("Sta").toString());
				this.AddTDIdx(i);
				if (0 == sta) {
					this.AddTDB(
							"Class='TTD' onclick=\"SetImg('" + basePath + "','"
									+ dr.getValue("MyPK") + "')\"",
							"<a href=\"javascript:WinOpenIt('"
									+ basePath
									+ "','"
									+ dr.getValue("MyPK")
									+ "','"
									+ dr.getValue("FK_Flow")
									+ "','"
									+ dr.getValue("FK_Node")
									+ "','"
									+ dr.getValue(CCListAttr.WorkID)
									+ "','"
									+ dr.getValue("FID")
									+ "','"
									+ dr.getValue("Sta")
									+ "');\" ><img src='"
									+ basePath
									+ "WF/Img/CCSta/0.png' id='I"
									+ dr.getValue("MyPK")
									+ "' class=Icon >"
									+ dr.getValue("Title")
									+ "</a><br>日期:"
									+ dr.getValue("RDT").toString()
											.substring(5));
				} else {
					this.AddTD(
							"Class='TTD'",
							"<a href=\"javascript:WinOpenIt('"
									+ basePath
									+ "','"
									+ dr.getValue("MyPK")
									+ "','"
									+ dr.getValue("FK_Flow")
									+ "','"
									+ dr.getValue("FK_Node")
									+ "','"
									+ dr.getValue(CCListAttr.WorkID)
									+ "','"
									+ dr.getValue("FID")
									+ "','"
									+ dr.getValue("Sta")
									+ "');\" ><img src='"
									+ basePath
									+ "WF/Img/CCSta/"
									+ dr.getValue("Sta")
									+ ".png' class=Icon >"
									+ dr.getValue("Title")
									+ "</a><br>日期:"
									+ dr.getValue("RDT").toString()
											.substring(5));
				}

				this.AddTDBigDoc(DataType.ParseText2Html(dr.getValue("Doc")
						.toString()));

				if (!"FlowName".equals(GroupBy)) {
					if (0 == sta) {
						this.AddTDB(dr.getValue("FlowName").toString());
					} else {
						this.AddTD(dr.getValue("FlowName").toString());
					}
				}
				if (!"NodeName".equals(GroupBy)) {
					if (0 == sta) {
						this.AddTDB(dr.getValue("NodeName").toString());
					} else {
						this.AddTD(dr.getValue("NodeName").toString());
					}
				}
				if (!"Rec".equals(GroupBy))
					this.AddTD(dr.getValue("Rec").toString());

				if ("1".equals(Sta))
					this.AddTD("<a href=\"javascript:DoDelCC('"
							+ dr.getValue("MyPK") + "');\"><img src='"
							+ basePath + "WF/Img/Btn/Delete.gif' /></a>");

				this.AddTREnd();
			}
		}
		this.AddTRSum();
		this.AddTD("colspan=" + colspan, "&nbsp;");
		this.AddTREnd();
		//this.AddTableEnd();
	}
%>
<form class="am-form">
<%
	infoCtrl = new StringBuffer();
	if ("-1".equals(Sta)) {
		this.Bind(Dev2Interface.DB_CCList(WebUser.getNo()), basePath,
				FK_Flow, GroupBy, Sta);
	} else if ("0".equals(Sta)) {
		this.Bind(Dev2Interface.DB_CCList_UnRead(WebUser.getNo()),
				basePath, FK_Flow, GroupBy, Sta);
	} else if ("1".equals(Sta)) {
		this.Bind(Dev2Interface.DB_CCList_Read(WebUser.getNo()),
				basePath, FK_Flow, GroupBy, Sta);
	} else {
		this.Bind(Dev2Interface.DB_CCList_Delete(WebUser.getNo()),
				basePath, FK_Flow, GroupBy, Sta);
	}
%>
		<%=infoCtrl.toString()%>
		
</form>
<!-- <ul class="am-pagination am-pagination-right">
					<li class="am-disabled"><a href="#">&laquo;</a></li>
					<li class="am-active"><a href="#">1</a></li>
					<li><a href="#">2</a></li>
					<li><a href="#">3</a></li>
					<li><a href="#">4</a></li>
					<li><a href="#">5</a></li>
					<li><a href="#">&raquo;</a></li>
				</ul> -->
