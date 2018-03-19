package cn.jflow.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.DBSrcType;
import BP.Sys.SFDBSrc;
import BP.Sys.SFTableAttr;
import BP.Sys.SystemConfig;
import BP.WF.Glo;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class SFGuideModel {
	private HttpServletRequest req;
	private HttpServletResponse res;
	public UiFatory Pub1 = null;

	public SFGuideModel(HttpServletRequest request, HttpServletResponse response) {
		this.req = request;
		this.res = response;
		regs.put(0, new String[] { "id", "no", "pk" });
		regs.put(1, new String[] { "name", "title" });
		regs.put(2, new String[] { "parentid", "parentno" });
	}

	private int Step;

	public int getStep() {
		try {
			return Integer.parseInt(req
					.getParameter("Step"));
		} catch (Exception e) {
			return 1;
		}
	}

	public void setStep(int step) {
		Step = step;
	}

	private String LB_Table;

	public String getLB_Table() {
		return req.getParameter("LB_Table") == null ? "" : req
				.getParameter("LB_Table");
	}

	public void setLB_Table(String lB_Table) {
		LB_Table = lB_Table;
	}

	private String DDL_ColText;

	public String getDDL_ColText() {
		return req.getParameter("DDL_ColText") == null ? "" : req
				.getParameter("DDL_ColText");
	}

	public void setDDL_ColText(String dDL_ColText) {
		DDL_ColText = dDL_ColText;
	}

	private String DDL_ColValue;

	public String getDDL_ColValue() {
		return req.getParameter("DDL_ColValue") == null ? "" : req
				.getParameter("DDL_ColValue");
	}

	public void setDDL_ColValue(String dDL_ColValue) {
		DDL_ColValue = dDL_ColValue;
	}

	private String DDL_ColParentNo;

	public String getDDL_ColParentNo() {
		return req.getParameter("DDL_ColParentNo") == null ? "" : req
				.getParameter("DDL_ColParentNo");
	}

	public void setDDL_ColParentNo(String dDL_ColParentNo) {
		DDL_ColParentNo = dDL_ColParentNo;
	}

	private String TB_SelectStatement;

	public String getTB_SelectStatement() {
		return req.getParameter("TB_SelectStatement") == null ? "" : req
				.getParameter("TB_SelectStatement");
	}

	public void setTB_SelectStatement(String tB_SelectStatement) {
		TB_SelectStatement = tB_SelectStatement;
	}

	private String DDL_SFTableType;

	public String getDDL_SFTableType() {
		return req.getParameter("DDL_SFTableType") == null ? "" : req
				.getParameter("DDL_SFTableType");
	}

	public void setDDL_SFTableType(String dDL_SFTableType) {
		DDL_SFTableType = dDL_SFTableType;
	}

	private String FK_SFDBSrc;

	public String getFK_SFDBSrc() {
		return req.getParameter("FK_SFDBSrc");
	}

	public void setFK_SFDBSrc(String fK_SFDBSrc) {
		FK_SFDBSrc = fK_SFDBSrc;
	}

	private String str;

	public String getStr() {
		return req.getParameter("str") == null ? "" : req.getParameter("str");
	}

	public void setStr(String str) {
		this.str = str;
	}

	private String DoType;

	public String getDoType() {
		return req.getParameter("DoType") == null ? "" : req
				.getParameter("DoType");
	}

	public void setDoType(String doType) {
		DoType = doType;
	}

	private String MyPK;

	public String getMyPK() {
		return req.getParameter("MyPK") == null ? "" : req.getParameter("MyPK");
	}

	public void setMyPK(String myPK) {
		MyPK = myPK;
	}
	public final String getIdx() {
		return "1";
	}
	public final String getGroupField() {
		return "GroupField";
	}


	// / <summary>
	// / 判断表符合规则的数据
	// / </summary>
	private Map<Integer, String[]> regs = new HashMap<Integer, String[]>();

	public void Page_Load() {
		// #region Step = 1
		
		Step = getStep();

		Pub1 = new UiFatory();
		
//		if(this.getDoType().equals("New")){
//			this.setStep(1);
//		}
		
		if (Step == 1) {
			BP.Sys.SFDBSrcs ens = new BP.Sys.SFDBSrcs();
			ens.RetrieveAll();
			
			Pub1.append(BaseModel
					.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("", "第1步：选择下拉框数据来源类型"));
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(BaseModel.AddUL("class='navlist'"));

		/*	for (int i = 0; i < ens.size(); i++) {
				BP.Sys.SFDBSrc item = (SFDBSrc) ens.get(i);
				Pub1.append(BaseModel
						.AddLi("<div><a href='SFGuide.jsp?Step=2&FK_SFDBSrc="
								+ item.getNo() + "'><span class='nav'>"
								+ item.getNo() + "  -  " + item.getName()
								+ "</span></a></div>"));
			}*/

			/*Pub1.append(BaseModel
					.AddLi("<div><a href=\"javascript:WinOpen('../RefFunc/UIEn.jsp?EnsName=BP.Sys.SFDBSrcs')\" ><img src='../../Img/New.gif' align='middle' /><span class='nav'>新建数据源</span></a></div>"));
			Pub1.append(BaseModel
					.AddLi("<div><a href='SFGuide.jsp?Step=12&FK_SFDBsrc=local'><img src='../../Img/New.gif' align='middle' /><span>创建本地编码字典表</span></a></div>"));
			*/
			String path=Glo.getCCFlowAppPath();
			this.Pub1.append(BaseModel.AddLi("<a href='"+path+"WF/MapDef/SFTable.jsp?DoType=New&MyPK=" + this.getMyPK() + "&Idx=" + this.getIdx() + "&FromApp=SL' ><b><img src='"+path+"WF/Comm/Sys/Img/Table.png' border=0 style='width:17px;height:17px;' />外键型表或视图</b></a> -  比如：岗位、税种、行业、科目，本机上一个表组成一个下拉框。"));
			this.Pub1.append(BaseModel.AddLi("<a href='"+path+"WF/MapDef/SFSQL.jsp?DoType=New&MyPK=" + this.getMyPK() + "&Idx=" + this.getIdx() + "&FromApp=SL'><b><img src='"+path+"WF/Comm/Sys/Img/View.png' border=0  style='width:17px;height:17px;' />外部表SQL数据源</b></a> -  比如：配置一个SQL通过数据库连接或获取的外部数据，组成一个下拉框。"));
			this.Pub1.append(BaseModel.AddLi("<a href='"+path+"WF/MapDef/SFWS.jsp?DoType=AddSFWebServeces&MyPK=" + this.getMyPK() + "&FType=Class&Idx=" + this.getIdx() + "&GroupField=" + this.getGroupField() + "&FromApp=SL'><b><img src='"+path+"WF/Comm/Sys/Img/WS.png' border=0 />WebServices数据</b></a> -  比如：通过调用Webservices接口获得数据，组成一个下拉框。"));


			
			Pub1.append(BaseModel.AddULEnd());
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());
			Pub1.append(BaseModel.AddTableEnd());
		}
		// #endregion
		//
		// #region Step = 2

		if (Step == 2) {
			SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());

			Pub1.append("<div class='easyui-layout' data-options=\"fit:true\"><form id=\"form1\" method=\"POST\">");
			Pub1.append("<input type=\"hidden\" id=\"FormHtml\" name=\"FormHtml\" value=\"\">");
			Pub1.append(String
					.format("<div data-options=\"region:'west',split:true,title:'选择 {0} 表/视图'\" style='width:250px;'>",
							src.getNo()));

			ListBox lb = Pub1.createListBox("LB_Table");
			lb.BindByTableNoName(src.GetTables());
			lb.addAttr("width", "100%");
			lb.addAttr("height", "100%");
			lb.addAttr("onclick", "lb_SelectedIndexChanged()");
			Pub1.append(lb);

			Pub1.append(BaseModel.AddDivEnd());

			Pub1.append("<div data-options=\"region:'center',title:'第2步：请填写基础信息'\" style='padding:5px;'>");
			Pub1.append(BaseModel
					.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
			DBSrcType dbType = src.getDBSrcType();
			if (dbType == DBSrcType.Localhost) {
				switch (SystemConfig.getAppCenterDBType()) {
					case MSSQL:
						dbType = DBSrcType.SQLServer;
						break;
					case Oracle:
						dbType = DBSrcType.Oracle;
						break;
					case MySQL:
						dbType = DBSrcType.MySQL;
						break;
					case Informix:
						dbType = DBSrcType.Informix;
						break;
					default:
						throw new RuntimeException("没有涉及到的连接测试类型...");
				}
			}

			String islocal = Boolean
					.toString(src.getDBSrcType() == DBSrcType.Localhost); // ().ToString().ToLower();

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:100px'",
					"值(编号)："));
			DDL ddl = Pub1.creatDDL("DDL_ColValue");
			ddl.addAttr("onchange",
					"generateSQL('" + src.getNo() + "','" + src.getDBName()
							+ "'," + islocal + ")");
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(ddl);
			Pub1.append("&nbsp;编号列，比如：类别编号");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle("标签(名称)："));
			ddl = Pub1.creatDDL("DDL_ColText");
			ddl.addAttr("onchange",
					"generateSQL('" + src.getNo() + "','" + src.getDBName()
							+ "'," + islocal + ")");
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(ddl);
			Pub1.append("&nbsp;显示的列，比如：类别名称");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle("父结点值(字段)："));
			ddl = Pub1.creatDDL("DDL_ColParentNo");
			ddl.addAttr("onchange",
					"generateSQL('" + src.getNo() + "','" + src.getDBName()
							+ "'," + islocal + ")");
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(ddl);
			Pub1.append("&nbsp;如果是树类型实体，该列设置有效，比如：上级类别编号");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle("字典表类型："));

			ddl = Pub1.creatDDL("DDL_CodeStruct");
			ddl.SelfBindSysEnum(SFTableAttr.CodeStruct);
			ddl.addAttr("onchange",
					"generateSQL('" + src.getNo() + "','" + src.getDBName()
							+ "'," + islocal + ")");
			Pub1.append(BaseModel.AddTD(ddl));
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle("查询语句："));
			TextBox tb = Pub1.creatTextBox("TB_SelectStatement");
			tb.setTextMode(TextBoxMode.MultiLine);
			tb.setColumns(60);
			tb.setRows(10);
			tb.addAttr("style", "width:99%");
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("<br />&nbsp;说明：查询语句可以修改，但请保证查询语句的准确性及有效性！");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTableEnd());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddSpace(1));

			LinkButton btn = Pub1.creatLinkButton(false,
					NamesOfBtn.Next.getCode(), "下一步");
			btn.setHref("btn_Click()");
			Pub1.append(btn);
			Pub1.append(BaseModel.AddSpace(1));

			Pub1.append("<a href='" + req.getRequestURL()
					+ "?DoType=New&MyPK=ND101"
					+ "' class='easyui-linkbutton'>上一步</a>");

			Pub1.append(BaseModel.AddDivEnd());
			Pub1.append("</form>");
			Pub1.append(BaseModel.AddDivEnd());

			if (lb.Items.size() > 0) {
				lb.setSelectedIndex(0);
				ShowSelectedTableColumns();
			}
		}
		// #endregion
		//
		// #region Step = 12

		if (Step == 12) {
			Pub1.append(BaseModel
					.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("colspan='2'", "第2步：创建"));
			Pub1.append(BaseModel.AddTREnd());

			TextBox tb = Pub1.creatTextBox("TB_No");
			// tb.ID = "TB_No";
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:100px'",
					"表英文名称："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;必须以字母或者下画线开头");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			tb = Pub1.creatTextBox("TB_" + SFTableAttr.Name);
			// tb.ID = "TB_" + SFTableAttr.Name;
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("", "表中文名称："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;显示的标签");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			tb = Pub1.creatTextBox("TB_" + SFTableAttr.TableDesc);
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("", "描述："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;表描述");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTableEnd());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddSpace(1));

			LinkButton btn = Pub1.creatLinkButton(false,
					NamesOfBtn.Apply.getCode(), "执行创建");
			btn.setHref("btn_Create_local_Click()");
			Pub1.append(btn);
			Pub1.append(BaseModel.AddSpace(1));

			Pub1.append("<a href='" + req.getRequestURL()+ "' class='easyui-linkbutton'>上一步</a>");
		}
		// #endregion
		
		if (Step == 22) {
			Pub1.append(BaseModel
					.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("colspan='2'", "第2步：创建"));
			Pub1.append(BaseModel.AddTREnd());
			
			TextBox tb = new TextBox();
			tb.setId("TB_No");
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:100px'", "WebService数据源编号："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;必须以字母或者下画线开头，比如：HR,CRM");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());
			
			tb = new TextBox();
			tb.setId("TB_"+SFTableAttr.Name);
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("", "WebService数据源名称："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;显示的数据源名称,比如:人力资源系统,客户关系管理系统。");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());
			
			tb = new TextBox();
			tb.setId("TB_"+SFTableAttr.TableDesc);
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:300px'", "WebService连接Url："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;WebService地址,比如:http://127.0.0.1/CCFormTester.asmx。");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());
			
			tb = new TextBox();
			tb.setId("TB_"+SFTableAttr.SrcTable);
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:300px'", "WebService接口名称 ："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;比如：GetEmps。");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());
			
			tb = new TextBox();
			tb.setId("TB_"+SFTableAttr.SelectStatement);
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:300px'", "WebService接口参数 ："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle("格式说明"));
			Pub1.append(BaseModel.AddTDDoc("如：WorkId=@WorkID&FK_Flow=@FK_Flow&FK_Node=@FK_Node&SearchType=1，带@的参数值在运行时自动使用发起流程的相关参数值替换，而不带@的参数值使用后面的赋值；参数个数与WebServices接口方法的参数个数一致，且顺序一致，且值均为字符类型。"));
			Pub1.append(BaseModel.AddTREnd());


			Pub1.append(BaseModel.AddTableEnd());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddSpace(1));
			
			LinkButton btn = Pub1.creatLinkButton(false,
					NamesOfBtn.Apply.getCode(), "执行创建");
			btn.setHref("btn_Create_WebService_Click()");
			Pub1.append(btn);
			Pub1.append(BaseModel.AddSpace(1));

			Pub1.append("<a href='" + req.getRequestURL()+ "' class='easyui-linkbutton'>上一步</a>");
		}
		
		
		//
		// #region Step = 3

		if (Step == 3) {
			Pub1.append(BaseModel
					.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("colspan='2'", "第3步：创建"));
			Pub1.append(BaseModel.AddTREnd());

			TextBox tb = Pub1.creatTextBox("TB_No");
			// tb.ID = "TB_No";
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("style='width:100px'",
					"表英文名称："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;必须以字母或者下画线开头");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			tb = Pub1.creatTextBox("TB_" + SFTableAttr.Name);
			// tb.ID = "TB_" + SFTableAttr.Name;
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("", "表中文名称："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;显示的标签");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			tb = Pub1.creatTextBox("TB_" + SFTableAttr.TableDesc);
			// tb.ID = "TB_" + SFTableAttr.TableDesc;
			Pub1.append(BaseModel.AddTR());
			Pub1.append(BaseModel.AddTDGroupTitle1("", "描述："));
			Pub1.append(BaseModel.AddTDBegin());
			Pub1.append(tb);
			Pub1.append("&nbsp;表描述");
			Pub1.append(BaseModel.AddTDEnd());
			Pub1.append(BaseModel.AddTREnd());

			Pub1.append(BaseModel.AddTableEnd());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddBR());
			Pub1.append(BaseModel.AddSpace(1));

			LinkButton btn = Pub1.creatLinkButton(false,
					NamesOfBtn.Apply.getCode(), "执行创建");
			// btn.addAttr("onclick", "btn_Create_Click()");
			btn.setHref("btn_Create_Click()");
			// btn.Click += new EventHandler(btn_Create_Click);
			Pub1.append(btn);
			Pub1.append(BaseModel.AddSpace(1));

			Pub1.append("<a href='" + req.getRequestURL()
					+ "?Step=2&FK_SFDBSrc=local"
					+ "' class='easyui-linkbutton'>上一步</a>");
		}
		// #endregion
	}
	// / <summary>
	// / 加载选中表的所有列信息
	// / </summary>
	public void ShowSelectedTableColumns() {
		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
		ListBox box = (ListBox) this.Pub1.GetUIByID("LB_Table");
		DataTable colTables = src.GetColumns(box.getSelectedItemStringVal());

		colTables.Columns.Add("text", "String");

		List<String> cols = new ArrayList<String>();
		String type;
		int length = 0;
		for (int i = 0; i < LengthTypes.length; i++) {
			DataRow dr = colTables.Rows.get(i);
			// }
			// for (DataRow dr : colTables.Rows)
			// {
			cols.add(dr.getValue("name").toString());
			type = dr.getValue("type").toString().toLowerCase();
			length = Integer.parseInt(dr.getValue("length").toString());

			dr.setValue(
					"text",
					dr.getValue("name")
							+ " ("
							+ (LengthTypes[i].contains(type) ? (String
									.format("{0}{1}",
											type,
											(length == -1 || length == 0) ? (MaxTypes[i]
													.contains(type) ? "(max)"
													: "") : String.format(
													"({0})", length)))
									: type) + ")");
			// dr.setValue("text",dr["name"] + " (" +
			// (LengthTypes.Contains(type) ?
			// (string.Format("{0}{1}", type,
			// (length == -1 || length == 0) ?
			// (MaxTypes.Contains(type) ? "(max)" : "")
			// : string.Format("({0})", length))) : type) + ")");
		}

		// 自动判断是否符合规则
		String regColValue = "";
		String regColText = "";
		String regColParentNo = "";
		for (int i = 0; i < cols.size(); i++) {
			String[] arry = regs.get(0);
			for (String str : arry) {
				if (cols.get(i).toLowerCase().equals(str)) {
					regColValue = cols.get(i);
					continue;
				}
			}
			arry = regs.get(1);
			for (String str : arry) {
				if (cols.get(i).toLowerCase().equals(str)) {
					regColText = cols.get(i);
					continue;
				}
			}
			arry = regs.get(2);
			for (String str : arry) {
				if (cols.get(i).toLowerCase().equals(str)) {
					regColParentNo = cols.get(i);
					continue;
				}
			}
		}
		// for (int i = 0; i < cols.size(); i++) {
		// for (int j = 0; j < regs.size(); j++) {
		// }
		// }
		// for (int i = 0; i < cols.size(); i++) {
		// for (int j = 0; j < regs.size(); j++) {
		// }
		// }
		// var regColValue = cols.FirstOrDefault(o =>
		// regs.get(0).Contains(o.ToLower()));
		// var regColText = cols.FirstOrDefault(o =>
		// regs.get(1).Contains(o.ToLower()));
		// var regColParentNo = cols.FirstOrDefault(o =>
		// regs.get(2).Contains(o.ToLower()));

		DDL ddl = (DDL) this.Pub1.GetUIByID("DDL_ColValue");
		ddl.Items.clear();
		ddl.Bind(colTables, "name", "text");

		if (regColValue != null)
			ddl.SetSelectItem(regColValue);

		ddl = (DDL) this.Pub1.GetUIByID("DDL_ColText");
		ddl.Items.clear();
		ddl.Bind(colTables, "name", "text");

		if (regColText != null)
			ddl.SetSelectItem(regColText);

		ddl = (DDL) this.Pub1.GetUIByID("DDL_ColParentNo");
		ddl.Items.clear();
		ddl.Bind(colTables, "name", "text");

		if (regColParentNo != null)
			ddl.SetSelectItem(regColParentNo);

		((TextBox) Pub1.GetUIByID("TB_SelectStatement")).setText("");
		((DDL) Pub1.GetUIByID("DDL_SFTableType"))
				.SetSelectItem((regColValue != null && regColText != null && regColParentNo != null) ? "1"
						: "0");
	}

	private String[] LengthTypes = new String[] { "char", "nchar", "varchar",
			"nvarchar", "varbinary" };
	private String[] MaxTypes = new String[] { "nvarchar", "varbinary",
			"varchar" }; // 如:nvarchar(max) 则maxLength为-1
}
