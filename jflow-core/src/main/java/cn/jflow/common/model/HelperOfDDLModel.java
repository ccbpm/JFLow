package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.util.ConvertTools;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.Port.Depts;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;

public class HelperOfDDLModel extends BaseModel {

	private boolean IsPostBack;

	public StringBuilder UCSys1;

	public StringBuilder UCSys2;

	private String PK;

	public DDL DropDownList1;

	public HelperOfDDLModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		UCSys1 = new StringBuilder();
		UCSys2 = new StringBuilder();
		DropDownList1 = new DDL();
	}

	/*
	 * public final AttrOfOneVSM getAttrOfOneVSM() { Entity en =
	 * ClassFactory.GetEn(this.getEnsName());
	 * 
	 * for(AttrOfOneVSM attr : en.EnMap.AttrsOfOneVSM) { if
	 * (attr.EnsOfMM.toString().equals(this.getAttrKey())) { return attr; } }
	 * for (AttrOfOneVSM attr: en.getEnMap().getAttrsOfOneVSM()) {
	 * if(attr.getEnsOfM().toString().equals(this.getAttrKey())){ return attr; }
	 * } throw new RuntimeException("错误没有找到属性． "); }
	 */
	/**
	 * 一的工作类
	 */

	/*
	 * basemodel 里面定义了 public final String getEnsName() { return
	 * this.Request.QueryString["EnsName"]; }
	 */

	/*
	 * 对比一下 和下面 , 看看那翻译的对不对 public final String getAttrKey() { return
	 * this.Request.QueryString["AttrKey"];
	 * 
	 * }
	 */
	public final String getAttrKey() {
		return getParameter("AttrKey");
	}

	public final String getPK() {
		/*
		 * if (ViewState["PK"]==null) { if
		 * (this.Request.QueryString["PK"]!=null) {
		 * ViewState["PK"]=this.Request.QueryString["PK"]; } else { Entity
		 * mainEn=BP.En.ClassFactory.GetEn(this.getEnsName());
		 * ViewState["PK"]=this.Request.QueryString[mainEn.PK]; } }
		 */
		if (PK == null) {
			if (getParameter("PK") != null) {
				PK = getParameter("PK");
			}
		} else {
			Entity mainEn = BP.En.ClassFactory.GetEn(this.getEnsName());
			PK = getParameter(mainEn.getPK());
		}

		return PK;
	}

	private boolean IsLine;

	public final boolean getIsLine() {
		try {
			return IsLine;
		} catch (java.lang.Exception e) {
			return false;
		}
	}

	public final void setIsLine(boolean value) {
		IsLine = value;
	}

	// protected final void Page_Load(Object sender, System.EventArgs e) {
	public void loadPage() {
		// if (this.IsPostBack == false) {
		// this.GenerLabel(this.Label1,"数据快速选择");
		// Entities ens =
		// BP.En.ClassFactory.GetEns(this.Request.QueryString["EnsName"]);
		Entities ens = ClassFactory.GetEns(getParameter("EnsName"));
		// Entity en = ens.GetNewEntity; // =
		// BP.En.ClassFactory.GetEns(this.Request.QueryString["EnsName"] );
		Entity en = ens.getGetNewEntity();
		// Map map = en.EnMap;
		Map map = en.getEnMap();
		/*
		 * for (Attr attr : map.Attrs) { // map if (attr.MyFieldType ==
		 * FieldType.FK || attr.MyFieldType == FieldType.Enum) {
		 * this.DropDownList1.Items.Add(new ListItem(attr.Desc, attr.getKey()));
		 * } }
		 */
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.FK
					|| attr.getMyFieldType() == FieldType.Enum) {
				// this.DropDownList1_SelectedIndexChanged(attr.getDesc(),
				// attr.getKey());
				this.DropDownList1.Items.add(new ListItem(attr.getDesc(), attr
						.getKey()));
			}

		}
		this.DropDownList1.Items.add(new ListItem("无", "None"));
		this.DropDownList1.setName("selectName");
		this.DropDownList1.setId("selectName");
		this.DropDownList1.addAttr("onchange", "onchangee()");

		// }

		try {

			this.SetDataV2();
		} catch (RuntimeException ex) {
			/*
			 * if (ex.getMessage().Contains("枚举操作") ||
			 * ex.getMessage().Contains("集合已修改")) { Thread.sleep(3000);
			 * this.Response.Redirect(this.Request.RawUrl); return; }
			 */
			if (ex.getMessage().contains("枚举类型")
					|| ex.getMessage().contains("集合已修改")) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendRedirect(get_request().getRequestURI().toString());
				return;
			}
		}

	}

	public final String getRefKey() {
		// return this.Request.QueryString["RefKey"];
		return getParameter("RefKey");
	}

	public final String getRefText() {
		// return this.Request.QueryString["RefText"];
		return getParameter("RefText");
	}

	public final void SetDataV2() {
		// this.UCSys1.Clear();
		// Entities ens =
		// BP.En.ClassFactory.GetEns(this.Request.QueryString["EnsName"]);
		Entities ens = BP.En.ClassFactory.GetEns(getParameter("EnsName"));
		ens.RetrieveAll();

		// Entity en = ens.GetNewEntity;
		Entity en = ens.getGetNewEntity();
		String space = "";
		// .getSelectedValue()
		if (this.DropDownList1.getSelectedItemStringVal().equals("None")) {

			// boolean isGrade = ens.IsGradeEntities;
			boolean isGrade = ens.getIsGradeEntities();
			if (isGrade) {
				this.UCSys1.append("<a name='top' ></a>");
				int num = ens.GetCountByKey("Grade", 2);
				if (num > 1) {
					int i = 0;
					/*
					 * this.UCSys1.AddTable(); this.UCSys1.AddTR();
					 * this.UCSys1.AddTDTitle("序号"); this.UCSys1.AddTDTitle(
					 * "<img src='../../images/Home.gif' border=0 />数据选择导航");
					 * this.UCSys1.AddTREnd();
					 */
					this.UCSys1.append(AddTable());
					this.UCSys1.append(AddTR());
					this.UCSys1.append(AddTDTitle("序号"));
					this.UCSys1
							.append(AddTDTitle("<img src='../../images/Home.gif' border=0 />数据选择导航"));
					this.UCSys1.append(AddTREnd());
					for (Entity myen : ens.ToJavaListEn()) {
						if (myen.GetValIntByKey("Grade") != 2) {
							continue;
						}

						i++;
						/*
						 * this.UCSys1.AddTR(); this.UCSys1.AddTDIdx(i);
						 * this.UCSys1.AddTD("<a href='#ID" +
						 * myen.GetValStringByKey(this.getRefKey()) +
						 * "' >&nbsp;&nbsp;" +
						 * myen.GetValStringByKey(this.getRefKey()) +
						 * "&nbsp;&nbsp;" +
						 * myen.GetValStringByKey(this.getRefText()) + "</a>");
						 * this.UCSys1.AddTREnd();
						 */
						this.UCSys1.append(AddTR());
						this.UCSys1.append(AddTDIdx(i));
						this.UCSys1.append(AddTD("<a href='#ID"
								+ myen.GetValStringByKey(this.getRefKey())
								+ "' >&nbsp;&nbsp;"
								+ myen.GetValStringByKey(this.getRefKey())
								+ "&nbsp;&nbsp;"
								+ myen.GetValStringByKey(this.getRefText())
								+ "</a>"));
						this.UCSys1.append(AddTREnd());
					}
					// this.UCSys1.AddTableEnd();
					this.UCSys1.append(AddTableEnd());
				}
			}

			/*
			 * this.UCSys1.AddTable(); this.UCSys1.AddTR();
			 * this.UCSys1.AddTDTitle("IDX"); this.UCSys1.AddTDTitle("");
			 * this.UCSys1.AddTREnd();
			 */
			this.UCSys1.append(AddTable());
			this.UCSys1.append(AddTR());
			this.UCSys1.append(AddTDTitle("IDX"));
			this.UCSys1.append(AddTDTitle(""));
			this.UCSys1.append(AddTREnd());
			boolean is1 = false;

			int idx = 0;
			// for (Entity myen : ens) {
			for (Entity myen : ens.ToJavaListEn()) {
				idx++;
				/*
				 * is1 = this.UCSys1.AddTR(is1); this.UCSys1.AddTDIdx(idx);
				 */
				// is1=this.UCSys1.append(AddTR(is1));
				this.UCSys1.append(AddTDIdx(idx));
				// RadioBtn rb = new RadioBtn();
				// rb.GroupName = "s";
				RadioButton rb = new RadioButton();
				rb.setGroupName("s");
				if (isGrade) {
					int grade = myen.GetValIntByKey("Grade");
					space = "";
					// space.PadLeft(grade - 1, '-')
					space = ConvertTools
							.padLeft(String.valueOf(grade), -1, "-");

					space = space.replace("-", "&nbsp;&nbsp;&nbsp;");
					// this.UCSys1.AddTD(space);
					switch (grade) {
					case 2:
						rb.setText("<a href='#top' name='ID"
								+ myen.GetValStringByKey(this.getRefKey())
								+ "' ><Img src='../../images/Top.gif' border=0 /></a><b><font color=green>"
								+ myen.GetValStringByKey(this.getRefKey())
								+ myen.GetValStringByKey(this.getRefText())
								+ "</font></b>");
						break;
					case 3:
						rb.setText("<b>"
								+ myen.GetValStringByKey(this.getRefKey())
								+ myen.GetValStringByKey(this.getRefText())
								+ "</b>");
						break;
					default:
						rb.setText(myen.GetValStringByKey(this.getRefKey())
								+ myen.GetValStringByKey(this.getRefText()));
						break;
					}
				} else {
					rb.setText(myen.GetValStringByKey(this.getRefText()));
				}
				// rb.ID = "RB_" + myen.GetValStringByKey(this.getRefKey());
				rb.setId("RB_" + myen.GetValStringByKey(this.getRefKey()));
				rb.setName("RB_Group");
				String clientscript = "window.returnValue = '"
						+ myen.GetValStringByKey(this.getRefKey())
						+ "';window.close();";
				// rb.Attributes["onclick"] = clientscript;
				rb.addAttr("onclick", clientscript);
				// this.UCSys1.Add(rb);
				// this.UCSys1.AddBR();
				/*
				 * this.UCSys1.AddTD(rb); this.UCSys1.AddTREnd();
				 */
				this.UCSys1.append(AddTD(rb));
				this.UCSys1.append(AddTREnd());
			}
			// this.UCSys1.AddTableEnd();
			this.UCSys1.append(AddTableEnd());
			return;
		}

		String key = this.DropDownList1.getSelectedItemStringVal();
		// Attr attr = en.EnMap.GetAttrByKey(key);
		Attr attr = en.getEnMap().GetAttrByKey(key);
		// if (attr.MyFieldType == FieldType.Enum || attr.MyFieldType ==
		// FieldType.PKEnum) {
		if (attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum) {
			SysEnums ses = new SysEnums(attr.getKey());
			// this.UCSys1.AddTable(); //("<TABLE border=1 >");
			this.UCSys1.append(AddTable());
			// for (SysEnum se : ses) {
			for (SysEnum se : ses.ToJavaList()) {
				/*
				 * this.UCSys1.Add("<TR><TD class='Toolbar'>");
				 * this.UCSys1.Add(se.Lab); this.UCSys1.Add("</TD></TR>");
				 * this.UCSys1.Add("<TR><TD>"); this.UCSys1.AddTable();
				 */
				this.UCSys1.append("<TR><TD class='Toolbar'>");
				this.UCSys1.append(se.getLab());
				this.UCSys1.append("</TD></TR>");
				this.UCSys1.append("<TR><TD>");
				this.UCSys1.append(AddTable());
				int i = -1;
				// for (Entity myen : ens){
				for (Entity myen : ens.ToJavaListEn()) {
					// if (myen.GetValIntByKey(attr.getKey()) != se.IntKey) {
					if (myen.GetValIntByKey(attr.getKey()) != se.getIntKey()) {
						continue;
					}

					i++;
					if (i == 3) {
						i = 0;
					}
					if (i == 0) {
						// this.UCSys1.Add("<TR>");
						this.UCSys1.append("<TR>");
					}

					// RadioBtn rb = new RadioBtn();
					RadioButton rb = new RadioButton();
					// rb.GroupName = "dsfsd";
					rb.setName("RB_Group");
					rb.setText(myen.GetValStringByKey(this.getRefText()));
					// rb.ID = "RB_" + myen.GetValStringByKey(this.getRefKey());
					rb.setId("RB_" + myen.GetValStringByKey(this.getRefKey()));
					String clientscript = "window.returnValue = '"
							+ myen.GetValStringByKey(this.getRefKey())
							+ "';window.close();";
					// rb.Attributes["ondblclick"] = clientscript;
					// rb.Attributes["onclick"] = clientscript;
					rb.addAttr("onclick", clientscript);
					this.UCSys1.append(rb);

					if (i == 2) {
						// this.UCSys1.Add("</TR>");
						this.UCSys1.append("</TR>");
					}
				}
				// this.UCSys1.Add("</TABLE>");
				// this.UCSys1.Add("</TD></TR>");
				this.UCSys1.append("</TABLE>");
				this.UCSys1.append("</TD></TR>");
			}
			// this.UCSys1.Add("</TABLE>");
			this.UCSys1.append("</TABLE>");
			return;
		}

		if (attr.getKey().equals("FK_Dept")) {
			Depts depts = new Depts();
			depts.RetrieveAll();

			/*
			 * this.UCSys1.AddTR(); this.UCSys1.AddTDToolbar("一级分组");
			 * this.UCSys1.AddTREnd(); this.UCSys1.AddTR();
			 * this.UCSys1.AddTDBegin(); this.UCSys1.AddTable();
			 */
			this.UCSys1.append(AddTR());
			this.UCSys1.append(AddTDToolbar("一级分组"));
			this.UCSys1.append(AddTREnd());
			this.UCSys1.append(AddTR());
			this.UCSys1.append(AddTDBegin());
			this.UCSys1.append(AddTable());
			// 显示导航信息
			int i = 0;
			// int span = 2;
			// for (BP.Port.Dept Dept : Depts){
			for (BP.Port.Dept Dept : depts.ToJavaList()) {
				// if (Dept.Grade == 2 || Dept.Grade == 1) {
				if (Dept.getGrade() == 2 || Dept.getGrade() == 1) {
					i++;
					/*
					 * this.UCSys1.Add("<TR>"); this.UCSys1.AddTDIdx(i);
					 * this.UCSys1.AddTD("<a href='#ID_2" + Dept.No +
					 * "' >&nbsp;&nbsp;" + Dept.No + "&nbsp;&nbsp;" +
					 * Dept.getName() + "</a><BR>"); this.UCSys1.Add("</TR>");
					 */
					this.UCSys1.append("<TR>");
					this.UCSys1.append(AddTDIdx(i));
					this.UCSys1.append("<a href='#ID_2" + Dept.getNo()
							+ "' >&nbsp;&nbsp;" + Dept.getNo() + "&nbsp;&nbsp;"
							+ Dept.getName() + "</a><BR>");
					this.UCSys1.append("</TR>");
				}
			}
			/*
			 * this.UCSys1.AddTableEnd(); this.UCSys1.AddTDEnd();
			 * this.UCSys1.AddTREnd(); this.UCSys1.AddTR();
			 * this.UCSys1.AddTDToolbar("二级分组"); this.UCSys1.AddTREnd();
			 * this.UCSys1.AddTDBegin(); this.UCSys1.AddTable();
			 */

			this.UCSys1.append(AddTableEnd());
			this.UCSys1.append(AddTDEnd());
			this.UCSys1.append(AddTREnd());
			this.UCSys1.append(AddTR());
			this.UCSys1.append(AddTDToolbar("二级分组"));
			this.UCSys1.append(AddTREnd());
			this.UCSys1.append(AddTDBegin());
			this.UCSys1.append(AddTable());
			// 显示导航信息
			// int i = 0;
			// int span = 2;
			i = 0;
			// for (BP.Port.Dept Dept : Depts)
			for (BP.Port.Dept Dept : depts.ToJavaList()) {
				i++;
				/*
				 * this.UCSys1.Add("<TR>"); this.UCSys1.AddTDIdx(i);
				 */
				this.UCSys1.append("<TR>");
				this.UCSys1.append(AddTDIdx(i));
				// if (Dept.Grade == 2) {
				if (Dept.getGrade() == 2) {
					// this.UCSys1.AddTD("&nbsp;&nbsp;<a name='ID_2" +
					// Dept.getNo() + "' >" + Dept.getNo() +
					// "</A>&nbsp;&nbsp;<a href='#ID" + Dept.getNo() + "' ><b>"
					// + Dept.getName() +
					// "</b></a><A HREF='#top'><Img src='../../images/Top.gif' border=0 /></a><BR>");
					this.UCSys1
							.append(AddTD("&nbsp;&nbsp;<a name='ID_2"
									+ Dept.getNo()
									+ "' >"
									+ Dept.getNo()
									+ "</A>&nbsp;&nbsp;<a href='#ID"
									+ Dept.getNo()
									+ "' ><b>"
									+ Dept.getName()
									+ "</b></a><A HREF='#top'><Img src='../../images/Top.gif' border=0 /></a><BR>"));
				} else {
					// this.UCSys1.AddTD("&nbsp;&nbsp;" + Dept.getNo() +
					// "&nbsp;&nbsp;<a href='#ID" + Dept.No + "' name='#ID_2" +
					// Dept.getNo() + "' >" + Dept.getName() + "</a><BR>");
					this.UCSys1.append(AddTD("&nbsp;&nbsp;" + Dept.getNo()
							+ "&nbsp;&nbsp;<a href='#ID" + Dept.getNo()
							+ "' name='#ID_2" + Dept.getNo() + "' >"
							+ Dept.getName() + "</a><BR>"));
				}

				// this.UCSys1.Add("</TR>");
				this.UCSys1.append("</TR>");
			}
			/*
			 * this.UCSys1.Add("</Table>"); this.UCSys1.Add("</TD></TR>");
			 */
			this.UCSys1.append("</Table>");
			this.UCSys1.append("</TD></TR>");
			// ============ 数据
			for (BP.Port.Dept groupen : depts.ToJavaList()) {
				/*
				 * this.UCSys1.Add("<TR><TD class='Toolbar' >");
				 * this.UCSys1.Add("<a href='#ID_2" + groupen.No + "' name='ID"
				 * + groupen.No +
				 * "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
				 * + groupen.GetValStringByKey(attr.UIRefKeyText));
				 * this.UCSys1.Add("</TD></TR>"); this.UCSys1.Add("<TR><TD>");
				 * this.UCSys1.AddTable();
				 */
				this.UCSys1.append("<TR><TD class='Toolbar' >");
				this.UCSys1
						.append("<a href='#ID_2"
								+ groupen.getNo()
								+ "' name='ID"
								+ groupen.getNo()
								+ "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
								+ groupen.GetValStringByKey(attr
										.getUIRefKeyText()));
				this.UCSys1.append("</TD></TR>");
				this.UCSys1.append("<TR><TD>");
				this.UCSys1.append(AddTable());
				i = -1;
				for (Entity myen : ens.ToJavaListEn()) {
					if (myen.GetValStringByKey(attr.getKey()) != groupen
							.GetValStringByKey(attr.getUIRefKeyValue())) {
						continue;
					}

					i++;
					if (i == 3) {
						i = 0;
					}

					if (i == 0) {
						// this.UCSys1.Add("<TR>");
						this.UCSys1.append("<TR>");
					}

					// RadioBtn rb = new RadioBtn();
					RadioButton rb = new RadioButton();
					rb.setGroupName("dsfsd");
					rb.setText(myen.GetValStringByKey(this.getRefText()));
					// rb.ID = "RB_" + myen.GetValStringByKey(this.getRefKey());
					rb.setId("RB_" + myen.GetValStringByKey(this.getRefKey()));
					String clientscript = "window.returnValue = '"
							+ myen.GetValStringByKey(this.getRefKey())
							+ "';window.close();";
					// rb.Attributes["ondblclick"] = clientscript;
					// rb.Attributes["onclick"] = clientscript;
					rb.addAttr("onclick", clientscript);
					// this.UCSys1.AddTD(rb);
					this.UCSys1.append(AddTD());

					if (i == 2) {
						// this.UCSys1.Add("</TR>");
						this.UCSys1.append("</TR>");
					}
				}
				/*
				 * this.UCSys1.Add("</Table>"); this.UCSys1.Add("</TD></TR>");
				 */
				this.UCSys1.append("</Table>");
				this.UCSys1.append("</TD></TR>");
			}
			// this.UCSys1.Add("</TABLE>");
			this.UCSys1.append("</TABLE>");
		} else {
			Entities groupens = ClassFactory.GetEns(attr.getUIBindKey());
			groupens.RetrieveAll();

			// this.UCSys1.AddTable(); //("<TABLE border=1 >");
			this.UCSys1.append(AddTable());
			int size = groupens.size();
			if (size > 19) {
				/*
				 * this.UCSys1.Add(
				 * "<TR><TD class='Toolbar' ><img src='../../images/Home.gif' border=0 />数据选择导航&nbsp;&nbsp;&nbsp;<font size='2'>提示:点分组连接就可到达分组数据</font></TD></TR>"
				 * ); this.UCSys1.Add("<TR><TD>"); this.UCSys1.AddTable();
				 */
				this.UCSys1
						.append("<TR><TD class='Toolbar' ><img src='../../images/Home.gif' border=0 />数据选择导航&nbsp;&nbsp;&nbsp;<font size='2'>提示:点分组连接就可到达分组数据</font></TD></TR>");
				this.UCSys1.append("<TR><TD>");
				this.UCSys1.append(AddTable());
				// 显示导航信息
				int i = 0;
				// int span = 2;
				for (Entity groupen : ens.ToJavaListEn()) {
					i++;
					/*
					 * this.UCSys1.AddTR(); this.UCSys1.AddTDIdx(i);
					 * this.UCSys1.AddTD("<a href='#ID" +
					 * groupen.GetValStringByKey(attr.UIRefKeyValue) +
					 * "' >&nbsp;&nbsp;" +
					 * groupen.GetValStringByKey(attr.UIRefKeyValue) +
					 * "&nbsp;&nbsp;" +
					 * groupen.GetValStringByKey(attr.UIRefKeyText) +
					 * "</a><BR>"); this.UCSys1.AddTREnd();
					 */
					this.UCSys1.append(AddTR());
					this.UCSys1.append(AddTDIdx(i));
					this.UCSys1
							.append(AddTD("<a href='#ID"
									+ groupen.GetValStringByKey(attr
											.getUIRefKeyValue())
									+ "' >&nbsp;&nbsp;"
									+ groupen.GetValStringByKey(attr
											.getUIRefKeyValue())
									+ "&nbsp;&nbsp;"
									+ groupen.GetValStringByKey(attr
											.getUIRefKeyText()) + "</a><BR>"));
					this.UCSys1.append(AddTREnd());
				}
				/*
				 * this.UCSys1.Add("</Table>"); this.UCSys1.Add("</TD></TR>");
				 */
				this.UCSys1.append("</Table>");
				this.UCSys1.append("</TD></TR>");
			}

			for (Entity groupen : groupens.ToJavaListEn()) {
				/*
				 * this.UCSys1.Add("<TR><TD class='Toolbar' >");
				 * this.UCSys1.Add("<a href='#top' name='ID" +
				 * groupen.GetValStringByKey(attr.UIRefKeyValue) +
				 * "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
				 * + groupen.GetValStringByKey(attr.UIRefKeyText));
				 * this.UCSys1.Add("</TD></TR>"); this.UCSys1.Add("<TR><TD>");
				 * this.UCSys1.AddTable();
				 */

				this.UCSys1.append("<TR><TD class='Toolbar' >");
				this.UCSys1
						.append("<a href='#top' name='ID"
								+ groupen.GetValStringByKey(attr
										.getUIRefKeyValue())
								+ "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
								+ groupen.GetValStringByKey(attr
										.getUIRefKeyText()));
				this.UCSys1.append("</TD></TR>");
				this.UCSys1.append("<TR><TD>");
				this.UCSys1.append(AddTable());
				int i = -1;
				// for (Entity myen : ens) {
				for (Entity myen : ens.ToJavaListEn()) {
					if (myen.GetValStringByKey(attr.getKey()) != groupen
							.GetValStringByKey(attr.getUIRefKeyValue())) {
						continue;
					}

					i++;
					if (i == 3) {
						i = 0;
					}

					if (i == 0) {
						// this.UCSys1.AddTR();
						this.UCSys1.append(AddTR());
					}

					// RadioBtn rb = new RadioBtn();
					RadioButton rb = new RadioButton();
					/*
					 * rb.GroupName = "dsfsd";
					 * rb.setText(myen.GetValStringByKey(this.getRefText()));
					 * rb.ID = "RB_" + myen.GetValStringByKey(this.getRefKey());
					 */
					rb.setGroupName("dsfsd");
					rb.setText(myen.GetValStringByKey(this.getRefText()));
					rb.setId("RB_" + myen.GetValStringByKey(this.getRefKey()));
					String clientscript = "window.returnValue = '"
							+ myen.GetValStringByKey(this.getRefKey())
							+ "';window.close();";
					// rb.Attributes["ondblclick"] = clientscript;
					// rb.Attributes["onclick"] = clientscript;
					rb.addAttr("onclick", clientscript);
					// this.UCSys1.AddTD(rb);
					this.UCSys1.append(AddTD(rb));
					if (i == 2) {
						// this.UCSys1.AddTREnd();
						this.UCSys1.append(AddTREnd());
					}
				}

				// this.UCSys1.AddTableEnd();
				// this.UCSys1.Add("</TD></TR>");
				this.UCSys1.append(AddTableEnd());
				this.UCSys1.append("</TD></TR>");
			}
			// this.UCSys1.AddTableEnd();
			this.UCSys1.append(AddTableEnd());
		}
	}

	
	// /#region 操作
	public final void EditMEns() {
		// this.WinOpen(this.Request.ApplicationPath+"/Comm/UIEns.aspx?EnsName="+this.AttrOfOneVSM.EnsOfM.ToString());
	}
	/*
	 * public final void Save() {
	 * 
	 * AttrOfOneVSM attr = this.getAttrOfOneVSM(); //Entities ensOfMM =
	 * attr.EnsOfMM; Entities ensOfMM = attr.getEnsOfMM(); QueryObject qo = new
	 * QueryObject(ensOfMM); //qo.AddWhere(attr.AttrOfOneInMM,this.getPK());
	 * qo.AddWhere(attr.getAttrOfOneInMM(),this.getPK()); qo.DoQuery();
	 * ensOfMM.Delete(); // 删除以前保存得数据。
	 * 
	 * AttrOfOneVSM attrOM = this.getAttrOfOneVSM(); //Entities ensOfM =
	 * attrOM.EnsOfM; Entities ensOfM = attrOM.getEnsOfM();
	 * ensOfM.RetrieveAll(); //for(Entity en : ensOfM) { for(Entity en :
	 * Entities.convertEntities(ensOfM)) { //String pk =
	 * en.GetValStringByKey(attr.AttrOfMValue); String pk =
	 * en.GetValStringByKey(attr.getAttrOfMValue());
	 * 
	 * CheckBox cb = (CheckBox)this.UCSys1.FindControl("CB_"+ pk); if
	 * (cb.getChecked()==false) { continue; }
	 * 
	 * Entity en1 =ensOfMM.GetNewEntity;
	 * en1.SetValByKey(attr.AttrOfOneInMM,this.getPK());
	 * en1.SetValByKey(attr.AttrOfMInMM, pk); en1.Insert(); Entity en1
	 * =ensOfMM.getGetNewEntity();
	 * en1.SetValByKey(attr.getAttrOfOneInMM(),this.getPK());
	 * en1.SetValByKey(attr.getAttrOfMInMM(), pk); en1.Insert(); }
	 * 
	 * // Entity enP =
	 * BP.En.ClassFactory.GetEn(this.Request.QueryString["EnsName"]); Entity enP
	 * = BP.En.ClassFactory.GetEn(getParameter("EnsName")); // if
	 * (enP.gEnMap.EnType!=EnType.View) { if
	 * (enP.getEnMap().getEnType()!=EnType.View) { // enP.SetValByKey(enP.PK,
	 * this.getPK()); // =this.PK; enP.SetValByKey(enP.getPK(), this.getPK());
	 * enP.Retrieve(); //查询。 enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。 } }
	 */

	/*
	 * protected final void Btn_OK_Click(Object sender, EventArgs e) { Entities
	 * ens = BP.En.ClassFactory.GetEns(this.Request.QueryString["EnsName"]);
	 * ens.RetrieveAll(); for (Entity en : ens) { RadioBtn rb =
	 * (RadioBtn)this.UCSys1.FindControl("RB_" +
	 * en.GetValStringByKey(this.getRefKey())); if (rb.Checked == false) {
	 * continue; }
	 * 
	 * String val=en.GetValStringByKey(this.getRefKey()); String
	 * ddl=this.Request.QueryString["DDLID"];
	 * 
	 * if (ddl != null) { // // ddl = ddl.Replace("DDL_"); String
	 * mainEns=this.Request.QueryString["MainEns"];
	 * 
	 * BP.Sys.UserRegedit ur = new UserRegedit(BP.Web.WebUser.getNo(), mainEns +
	 * "_SearchAttrs"); String cfgval = ur.Vals; int idx = cfgval.indexOf(ddl +
	 * "="); String start = cfgval.substring(0, idx);
	 * 
	 * String end = cfgval.substring(idx); end =
	 * end.substring(end.indexOf("@"));
	 * 
	 * ur.Vals = start + val + end; ur.Update(); }
	 * 
	 * 
	 * String clientscript =
	 * "<script language='javascript'> window.returnValue = '" + val +
	 * "'; window.close(); </script>"; this.Page.Response.Write(clientscript);
	 * return; } }
	 */
	/*
	 * protected final void DropDownList1_SelectedIndexChanged(Object sender,
	 * EventArgs e) { this.SetDataV2(); }
	 */

}
