package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataType;
import BP.En.EditType;
import BP.En.FieldTypeS;
import BP.En.TBType;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapExtXmlList;
import BP.Sys.SignType;
import BP.Tools.StringHelper;
import BP.WF.XML.DefVals;
import BP.WF.XML.SysDataTypes;
import BP.Web.WebUser;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;

public class EditFModel extends BaseModel {

	private int GroupField;

	private String DoType;

	private int FType;

	private String IDX;

	public int getGroupField() {
		String s = request.getParameter("GroupField");
		if (StringHelper.isNullOrEmpty(s))
			return 0;
		return Integer.parseInt(s);
	}

	public void setGroupField(int groupField) {
		GroupField = groupField;
	}

	public int getFType() {
		return Integer.parseInt(request.getParameter("FType"));
	}

	public void setFType(int fType) {
		FType = fType;
	}

	public String getIDX() {
		return request.getParameter("IDX");
	}

	public void setIDX(String iDX) {
		IDX = iDX;
	}

	public String Title;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	private HttpServletRequest request;

	private HttpServletResponse response;

	public UiFatory Pub1 = new UiFatory();

	public EditFModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.request = request;
		this.response = response;
	}

	public void init() {
		if (this.getMyPK() == null) {
			throw new RuntimeException("Mypk==null");
		}

		this.setTitle("编辑字段");

		
		//		switch (this.DoType)
		//ORIGINAL LINE: case "Add":
		if (this.getDoType().equals("Add")) {
			this.Add();
		}
		//ORIGINAL LINE: case "Edit":
		else if (this.getDoType().equals("Edit")) {
			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getRefNo());
			attr.RetrieveFromDBSources();
			attr.setMyDataType(getFType());
			switch (attr.getMyDataType()) {
			case BP.DA.DataType.AppString:
				this.EditString(attr);
				break;
			case BP.DA.DataType.AppDateTime:
			case BP.DA.DataType.AppDate:
			case BP.DA.DataType.AppInt:
			case BP.DA.DataType.AppFloat:
			case BP.DA.DataType.AppDouble:
			case BP.DA.DataType.AppMoney:
				this.EditInt(attr);
				break;
			case BP.DA.DataType.AppBoolean:
				this.EditBool(attr);
				break;
			default:
				throw new RuntimeException("为考虑的类型" + getFType());
			}
		} else {}
	}

	public final void Add() {
		MapAttr attr = new MapAttr();
		attr.setMyDataType(getFType());
		attr.setFK_MapData(this.getMyPK());
		attr.setUIIsEnable(true);
		switch (this.getFType()) {
		case DataType.AppString:
			this.EditString(attr);
			break;
		case DataType.AppInt:
		case DataType.AppDateTime:
		case DataType.AppDate:
		case DataType.AppFloat:
		case DataType.AppDouble:
		case DataType.AppMoney:
			this.EditInt(attr);
			break;
		case DataType.AppBoolean:
			this.EditBool(attr);
			break;
		default:
			break;
		}
	}

	private int idx = 1;

	public final void EditBeforeAdd(MapAttr mapAttr) {
		this.Pub1.append(AddTable("width='100%'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("ID"));
		this.Pub1.append(AddTDTitle("项目"));
		this.Pub1.append(AddTDTitle("采集"));
		this.Pub1.append(AddTDTitle("备注"));
		this.Pub1.append(AddTREnd());

		if (mapAttr.getIsTableAttr()) {
			// if here is table attr, It's will let use can change data type. 
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD("改变数据类型"));
			DDL ddlType = Pub1.creatDDL("DDL_DTType");
			//			ddlType.setId("DDL_DTType");
			//			ddlType.setName("DDL_DTType");
			SysDataTypes xmls = new SysDataTypes();
			xmls.RetrieveAll();
			ddlType.Bind(xmls, "No", "Name");
			ddlType.SetSelectItem(mapAttr.getMyDataTypeS());

			//			ddlType.AutoPostBack = true;

			//			ddlType.SelectedIndexChanged += new EventHandler(ddlType_SelectedIndexChanged);

			ddlType.addAttr("onchange", "ddlType_SelectedIndexChanged()");
			this.Pub1.append(AddTD(ddlType));
			this.Pub1.append(AddTD(""));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段中文名称"));
		TextBox tb = Pub1.creatTextBox("TB_Name");
		tb.addAttr("onblur", "displayResult()");
		//		tb.setId("TB_Name");
		//		tb.setName("TB_Name");
		tb.setText(mapAttr.getName());
		tb.addAttr("style", "width:100%");

		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段英文名"));

		tb = Pub1.creatTextBox("TB_KeyOfEn");
		//		tb.setId("TB_KeyOfEn");
		//		tb.setName("TB_KeyOfEn");
		tb.setText(mapAttr.getKeyOfEn());
		if (this.getRefNo() != null) {
			tb.setEnabled(false);
		}

		tb.addAttr("onkeyup", "return IsDigit(this);");
		tb.addAttr("readonly", "readonly");
		this.Pub1.append(AddTD(tb));

		if (StringHelper.isNullOrEmpty(mapAttr.getKeyOfEn())) {
			this.Pub1.append(AddTD("字母/数字/下划线组合"));
		} else {
			this.Pub1.append(AddTD("<a href=\"javascript:clipboardData.setData('Text','" + mapAttr.getKeyOfEn()
					+ "');alert('已经copy到粘帖版上');\" ><img src='../Img/Btn/Copy.GIF' class='ICON' />复制字段名</a></TD>"));
		}

		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("默认值"));
		tb = Pub1.creatTextBox("TB_DefVal");
		//		tb.setId("TB_DefVal");
		//		tb.setName("TB_DefVal");
		tb.setText(mapAttr.getDefValReal());

		switch (this.getFType()) {
		case BP.DA.DataType.AppDouble:
		case BP.DA.DataType.AppInt:
		case BP.DA.DataType.AppFloat:
			this.Pub1.append(AddTDNum(tb.toString()));
			tb.setShowType(TBType.Num);
			tb.setText(mapAttr.getDefVal());
			if (tb.getText().equals("")) {
				tb.setText("0");
			}
			break;
		case BP.DA.DataType.AppMoney:
		case BP.DA.DataType.AppRate:
			this.Pub1.append(AddTDNum(tb.toString()));
			tb.setShowType(TBType.Moneny);
			break;
		default:
			this.Pub1.append(AddTD(tb));
			break;
		}

		tb.setShowType(mapAttr.getHisTBType());
		switch (this.getFType()) {
		case DataType.AppDateTime:
		case DataType.AppDate:
			CheckBox cb = Pub1.creatCheckBox("CB_DefVal");
			cb.setText("默认系统当前日期");
			//				cb.setId("CB_DefVal");
			//				cb.setName("CB_DefVal");
			if (mapAttr.getDefValReal().equals("@RDT")) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}
			//				cb.AutoPostBack = true;

			//				cb.CheckedChanged += new EventHandler(cb_CheckedChanged_rdt);
			cb.addAttr("onchange", "cb_CheckedChanged_rdt()");
			this.Pub1.append(AddTD(cb));
			break;
		case DataType.AppString:
			DDL ddl = Pub1.creatDDL("DDL_SelectDefVal");
			//				ddl.AutoPostBack = true;

			DefVals vals = new DefVals();
			vals.Retrieve("Lang", WebUser.getSysLang());
			for (BP.WF.XML.DefVal def : vals.ToJavaList()) {
				ddl.Items.add(new ListItem(def.getName(), def.getVal()));
			}

			//ddl.Items.Add(new ListItem("选择系统约定默认值", ""));
			//ddl.Items.Add(new ListItem("操作员编号", "WebUser.No"));
			//ddl.Items.Add(new ListItem("操作员名称", "@WebUser.Name"));
			//ddl.Items.Add(new ListItem("隶属部门编号", "@WebUser.FK_Dept"));
			//ddl.Items.Add(new ListItem("隶属部门名称", "@WebUser.FK_DeptName"));

			//ddl.Items.Add(new ListItem("当前日期-1", "@yyyy年mm月dd日"));
			//ddl.Items.Add(new ListItem("当前日期-2", "@yy年mm月dd日"));

			//ddl.Items.Add(new ListItem("当前年度", "@FK_ND"));
			//ddl.Items.Add(new ListItem("当前月份", "@FK_YF"));

			//				ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged_DefVal);
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged_DefVal()");
			ddl.SetSelectItem(mapAttr.getDefValReal());
			//				ddl.setId("DDL_SelectDefVal");
			//				ddl.setName("DDL_SelectDefVal");
			this.Pub1.append(AddTD(ddl));
			break;
		default:
			this.Pub1.append(AddTD("&nbsp;"));
			break;
		}
		this.Pub1.append(AddTREnd());

		///#region 是否可以为空.
		switch (this.getFType()) {
		case BP.DA.DataType.AppDouble:
		case BP.DA.DataType.AppInt:
		case BP.DA.DataType.AppFloat:
		case BP.DA.DataType.AppMoney:
		case BP.DA.DataType.AppRate:
			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD("是否可以为空"));
			DDL ddlIsNull = Pub1.creatDDL("DDL_IsNull");
			ddlIsNull.Items.add(new ListItem("不能为空,按照默认值计算.", "0"));
			ddlIsNull.Items.add(new ListItem("可以为空,与默认值无关.", "1"));
			//				ddlIsNull.setId("DDL_IsNull");
			//				ddlIsNull.setName("DDL_IsNull");

			if (mapAttr.getMinLen() == 0) {
				ddlIsNull.SetSelectItem(0);
			} else {
				ddlIsNull.SetSelectItem(1);
			}

			this.Pub1.append(AddTD("colspan=2", ddlIsNull));
			this.Pub1.append(AddTREnd());
			break;
		default:
			break;
		}
		///#endregion 是否可以为空.

		//		RadioButton rb = new RadioButton();
		if (MapData.getIsEditDtlModel() == false) {
			//this.Pub1.AddTR();
			//this.Pub1.AddTD("界面上是否可见");
			//this.Pub1.Add("<TD>");
			//rb = new RadioButton();
			//rb.ID = "RB_UIVisible_0";
			//rb.Text = "不 可 见";
			//rb.GroupName = "s1";
			//if (mapAttr.UIVisible)
			//    rb.Checked = false;
			//else
			//    rb.Checked = true;
			//this.Pub1.Add(rb);

			//rb = new RadioButton();
			//rb.ID = "RB_UIVisible_1";
			//rb.Text = "可见 ";
			//rb.GroupName = "s1";

			//if (mapAttr.UIVisible)
			//    rb.Checked = true;
			//else
			//    rb.Checked = false;
			//this.Pub1.Add(rb);
			//this.Pub1.Add("</TD>");
			//this.Pub1.AddTD("控制是否显示在页面上");
			//this.Pub1.AddTREnd();
		}

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否可编辑"));
		this.Pub1.append("<TD>");

		RadioButton rb = Pub1.creatRadioButton("RB_UIIsEnable_0");
		//		rb.setId("RB_UIIsEnable_0");
		//		rb.setName("RB_UIIsEnable");
		rb.setText("不可编辑");
		rb.setGroupName("RB_UIIsEnable");
		rb.setChecked(!mapAttr.getUIIsEnable());

		this.Pub1.append(rb);
		rb = Pub1.creatRadioButton("RB_UIIsEnable_1");
		//		rb.setId("RB_UIIsEnable_1");
		//		rb.setName("RB_UIIsEnable");
		rb.setText("可编辑");
		rb.setGroupName("RB_UIIsEnable");
		rb.setChecked(mapAttr.getUIIsEnable());

		this.Pub1.append(rb);
		this.Pub1.append("</TD>");
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		///#region 是否可界面可见
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否界面可见")); //是否界面可见
		this.Pub1.append(AddTDBegin());
		rb = Pub1.creatRadioButton("RB_UIVisible_0");
		//		rb.setId("RB_UIVisible_0");
		//		rb.setName("RB_UIVisible");
		rb.setText("不可见"); // 界面不可见
		rb.setGroupName("RB_UIVisible");
		if (mapAttr.getUIVisible()) {
			rb.setChecked(false);
		} else {
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		if (mapAttr.getIsTableAttr()) {
			rb.setEnabled(false);
		}

		rb = Pub1.creatRadioButton("RB_UIVisible_1");
		//		rb.setId("RB_UIVisible_1");
		//		rb.setName("RB_UIVisible");
		rb.setText("界面可见"); // 界面可见;
		rb.setGroupName("RB_UIVisible");

		if (mapAttr.getUIVisible()) {
			rb.setChecked(true);
		} else {
			rb.setChecked(false);
		}

		if (mapAttr.getIsTableAttr()) {
			rb.setEnabled(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD("不可见则为隐藏字段."));
		//   this.Pub1.AddTD("控制该它在表单的界面里是否可见");
		this.Pub1.append(AddTREnd());
		///#endregion 是否可界面可见

	}

	public final void EditBeforeEnd(MapAttr mapAttr) {

		//		///#region 合并单元格数
		//		this.Pub1.append(AddTR1());
		//		this.Pub1.append(AddTDIdx(idx++));
		//		this.Pub1.append(AddTD("合并单元格数"));
		//		DDL ddl1 = Pub1.creatDDL("DDL_ColSpan");
		////		ddl1.setId("DDL_ColSpan");
		////		ddl1.setName("DDL_ColSpan");
		//		for (int i = 1; i < 12; i++)
		//		{
		//			ddl1.Items.add(new ListItem((new Integer(i)).toString(), (new Integer(i)).toString()));
		//		}
		//		ddl1.SetSelectItem(String.valueOf(mapAttr.getColSpan()));
		//		this.Pub1.append(AddTD(ddl1));
		//
		//		this.Pub1.append(AddTD("对傻瓜表单有效"));
		//		this.Pub1.append(AddTREnd());
		//		///#endregion 合并单元格数

		///#region 字段分组
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段分组"));
		DDL ddlGroup = Pub1.creatDDL("DDL_GroupID");
		//		ddlGroup.setId("DDL_GroupID");
		//		ddlGroup.setName("DDL_GroupID");
		GroupFields gfs = new GroupFields(mapAttr.getFK_MapData());
		ddlGroup.Bind(gfs, GroupFieldAttr.OID, GroupFieldAttr.Lab);
		if (mapAttr.getGroupID() == 0) {
			mapAttr.setGroupID(this.getGroupField());
		}

		ddlGroup.SetSelectItem(mapAttr.getGroupID());

		this.Pub1.append(AddTD("colspan=3", ddlGroup));
		this.Pub1.append(AddTREnd());
		///#endregion 字段分组

		///#region 是否是数字签名字段
		if (mapAttr.getUIIsEnable() == false && mapAttr.getMyDataType() == DataType.AppString && mapAttr.getLGType() == FieldTypeS.Normal) {
			this.Pub1.append(AddTR1());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD("签名模式"));
			DDL ddl = Pub1.creatDDL("DDL_SignType");
			//			ddl.setId("DDL_SignType");
			//			ddl.setName("DDL_SignType");
			ddl.Items.add(new ListItem("无", "0"));
			ddl.Items.add(new ListItem("图片签名", "1"));
			ddl.Items.add(new ListItem("CA签名", "2"));
			ddl.SetSelectItem(mapAttr.getSignType().getValue());

			//CheckBox cb = new CheckBox();
			//cb.ID = "CB_IsSigan";
			//cb.Text = "是否是数字签名字段";
			//cb.Checked = mapAttr.IsSigan;

			this.Pub1.append(AddTD("colspan=2", ddl));
			if (mapAttr.getSignType() == SignType.CA) {
				TextBox sigan = Pub1.creatTextBox("TB_SiganField");
				//				sigan.setId("TB_SiganField");
				//				sigan.setName("TB_SiganField");
				sigan.setText(mapAttr.getPara_SiganField());
				this.Pub1.append(AddTD(sigan));
			} else if (mapAttr.getSignType() == SignType.Pic) {
				DDL ddlPic = Pub1.creatDDL("DDL_PicType");
				//				ddlPic.setId("DDL_PicType");
				//				ddlPic.setName("DDL_PicType");
				ddlPic.Items.add(new ListItem("自动签名", "0"));
				ddlPic.Items.add(new ListItem("手动签名", "1"));
				ddlPic.SetSelectItem(mapAttr.getPicType().getValue());
				this.Pub1.append(AddTD(ddlPic));
			} 
//			else {
//				this.Pub1.append(AddTD());
//			}
			this.Pub1.append(AddTREnd());
		}

		//yuqinghai  add
		///#region 字段分组
		//isItem = this.Pub1.AddTR(isItem);
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字体大小"));

		DDL ddlFont = Pub1.creatDDL("DDL_FontSize");
		ddlFont.Items.add(new ListItem("默认", "0"));
		for (int i = 10; i < 28; i++) {
			ddlFont.Items.add(new ListItem(i + "px", (new Integer(i)).toString()));
		}

		ddlFont.SetSelectItem(mapAttr.getPara_FontSize());
		this.Pub1.append(AddTD(ddlFont));

		//是否必填字段.

		this.Pub1.append("<TD>");
		RadioButton rb = Pub1.creatRadioButton("RB_UIIsInput_0");
		rb.setText("非必填字段");
		rb.setGroupName("si");
		rb.setChecked(!mapAttr.getUIIsInput());
		this.Pub1.append(rb);

		RadioButton rb1 = Pub1.creatRadioButton("RB_UIIsInput_1");
		rb1.setText("必填");
		rb1.setGroupName("si");
		rb1.setChecked(mapAttr.getUIIsInput());
		this.Pub1.append(rb1);
		this.Pub1.append("</TD>");

		this.Pub1.append(AddTREnd());
		///#endregion 字段分组

		///#region 扩展设置.
		if (this.getRefNo() != null) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD("<a href=\"javascript:WinOpen('./PopVal.jsp?FK_MapData=" + mapAttr.getFK_MapData() + "&RefNo="
					+ mapAttr.getKeyOfEn() + "&MyPK=" + MapExtXmlList.PopVal + "_" + mapAttr.getMyPK() + "')\">设置开窗返回值</a>"));
			String html = "<a href=\"javascript:WinOpen('./RegularExpression.jsp?FK_MapData=" + mapAttr.getFK_MapData() + "&RefNo="
					+ mapAttr.getKeyOfEn() + "&OperAttrKey=" + mapAttr.getMyPK() + "')\">正则表达式</a>";
			html += " - <a href=\"javascript:WinOpen('./TBFullCtrl.jsp?FK_MapData=" + mapAttr.getFK_MapData() + "&RefNo=" + mapAttr.getKeyOfEn()
					+ "&MyPK=" + mapAttr.getFK_MapData() + "_" + MapExtXmlList.TBFullCtrl + "_" + mapAttr.getKeyOfEn() + "')\">文本框自动完成</a>";
			html += " - <a href=\"javascript:WinOpen('./AotuGenerNo.jsp?FK_MapData=" + mapAttr.getFK_MapData() + "&RefNo=" + mapAttr.getKeyOfEn()
					+ "')\">自动生成编号</a>";

			this.Pub1.append(AddTD(html));
			this.Pub1.append(AddTD("<a href=\"javascript:WinOpen('./AutoFull.jsp?FK_MapData=" + mapAttr.getFK_MapData() + "&ExtType=AutoFull&RefNo="
					+ mapAttr.getMyPK() + "')\">自动计算</a> - <a href=\"javascript:WinOpen('../Admin/FoolFormDesigner/MapExt/InputCheck.jsp?FK_MapData="
					+ mapAttr.getFK_MapData() + "&RefNo=" + mapAttr.getMyPK() + "')\">脚本验证</a>"));
			this.Pub1.append(AddTREnd());
		}

		///#endregion 字段分组

		this.Pub1.append(AddTRSum());
		this.Pub1.append("<TD colspan=4 align=center>");
		Button btn = Pub1.creatButton("Btn_Save");
		//		btn.setId("Btn_Save");
		//		btn.setName("Btn_Save");
		btn.setCssClass("Btn");
		btn.setText(" 保存 ");

		//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_Save')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndClose");
		//		btn.setId("Btn_SaveAndClose");
		//		btn.setName("Btn_SaveAndClose");
		btn.setCssClass("Btn");
		btn.setText("保存并关闭"); // "保存并关闭";

		//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_SaveAndClose')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndNew");
		btn.setCssClass("Btn");
		//		btn.setId("Btn_SaveAndNew");
		//		btn.setName("Btn_SaveAndNew");
		btn.setText("保存新建"); // "保存新建";

		//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_SaveAndNew')");
		this.Pub1.append(btn);

		if (this.getRefNo() != null) {
			//btn = new Button();
			//btn.ID = "Btn_AutoFull";
			//btn.Text ="扩展设置";
			//btn.Attributes["onclick"] = "javascript:WinOpen('AutoFull.jsp?RefNo=" + this.RefNo + "&FK_MapData=" + mapAttr.FK_MapData + "',''); return false;";

			this.Pub1.append("<input type=button class=Btn value='扩展设置' onclick=\"javascript:WinOpen('AutoFull.jsp?RefNo=" + this.getRefNo()
					+ "&FK_MapData=" + mapAttr.getFK_MapData() + "',''); return false;\" />");

			if (mapAttr.getHisEditType() == EditType.Edit) {
				btn = Pub1.creatButton("Btn_Del");
				//				btn.setId("Btn_Del");
				//				btn.setName("Btn_Del");
				btn.setCssClass("Btn");
				btn.setText("删除");

				//				btn.Click += new EventHandler(btn_Save_Click);
				btn.addAttr("onclick", "btn_Save_Click('Btn_Del')");
				//				btn.Attributes["onclick"] = " return confirm('您确认吗？');";
				this.Pub1.append(btn);
			}

			String myUrl = "EleBatch.jsp?KeyOfEn=" + mapAttr.getKeyOfEn() + "&FK_MapData=" + mapAttr.getFK_MapData() + "&EleType=MapAttr";
			this.Pub1.append("<a href='" + myUrl + "' target='M" + mapAttr.getKeyOfEn() + "' ><img src='../Img/Btn/Apply.gif' border=0>批处理</a>");

		}

		String url = "Do.jsp?DoType=AddF&MyPK=" + mapAttr.getFK_MapData() + "&IDX=" + mapAttr.getIdx();
		btn = Pub1.creatButton("Btn_New");
		//		btn.setId("Btn_New");
		//		btn.setName("Btn_New");
		btn.setCssClass("Btn");
		btn.setText("新建");

		//		btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onclick", "btn_Click('Btn_New')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_Back");
		//		btn.setId("Btn_Back");
		//		btn.setName("Btn_Back");
		btn.setCssClass("Btn");
		btn.setText("返回");

		//		btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onclick", "btn_Click('Btn_Back')");
		this.Pub1.append(btn);

		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEndWithBR());
	}

	public final void EditString(MapAttr mapAttr) {
		this.EditBeforeAdd(mapAttr);
		//		TextBox tb =new TextBox();
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("最小长度"));
		TextBox tb = Pub1.creatTextBox("TB_MinLen");
		//		tb.setId("TB_MinLen");
		//		tb.setName("TB_MinLen");
		tb.setCssClass("TBNum");
		tb.setText(String.valueOf(mapAttr.getMinLen()));
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("最大长度"));
		tb = Pub1.creatTextBox("TB_MaxLen");
		//		tb.setId("TB_MaxLen");
		//		tb.setName("TB_MaxLen");
		tb.setCssClass("TBNum");
		tb.setText(String.valueOf(mapAttr.getMaxLen()));

		//DDL cb = new DDL();
		//cb.ID = "DDL_TBType";
		//cb.Items.Add(new ListItem("单行文本框", "0"));
		//cb.Items.Add(new ListItem("多行文本框", "1"));
		//cb.Items.Add(new ListItem("Sina编辑框", "2"));
		//cb.Items.Add(new ListItem("FCKEditer编辑框", "3"));

		this.Pub1.append(AddTD(tb));

		//		DDL ddlBig = Pub1.creatDDL("DDL_TBModel");
		//
		//		ddlBig.BindSysEnum("TBModel", mapAttr.getTBModel());
		//   //		ddlBig.AutoPostBack = true;
		//
		//   //		ddlBig.SelectedIndexChanged += new EventHandler(ddlBig_SelectedIndexChanged);
		//		ddlBig.addAttr("onchange", "ddlBig_SelectedIndexChanged()");
		//		this.Pub1.append(AddTD(ddlBig));

		//		this.Pub1.append(AddTREnd());

		//
		//		this.Pub1.append(AddTR1());
		//		this.Pub1.append(AddTDIdx(idx++));
		//		this.Pub1.append(AddTD("文本框宽度"));
		//		tb = Pub1.creatTextBox("TB_UIWidth");
		//		tb.setId("TB_UIWidth");
		//		tb.setName("TB_UIWidth");
		//		tb.setCssClass("TBNum");
		//		tb.setText(String.valueOf(mapAttr.getUIWidth()));
		//		this.Pub1.append(AddTD(tb));
		//		this.Pub1.append(AddTDB("决定列的行数"));
		//		this.Pub1.append(AddTREnd());
		//
		//		this.Pub1.append(AddTR());
		//		this.Pub1.append(AddTDIdx(idx++));
		//		this.Pub1.append(AddTD("高度"));
		//		tb = Pub1.creatTextBox("TB_UIHeight");
		//		tb.setId("TB_UIHeight");
		//		tb.setName("TB_UIHeight");
		//		tb.setCssClass("TBNum");
		//		tb.setText(String.valueOf(mapAttr.getUIHeight()));
		//		this.Pub1.append(AddTD(tb));
		//		this.Pub1.append(AddTD(""));
		//		this.Pub1.append(AddTREnd());
		MapDtl mdtl = new MapDtl();
		mdtl.setNo(String.valueOf(mapAttr.getFK_MapData()));
		if (mdtl.RetrieveFromDBSources() != 0) {
			this.Pub1.append(AddTR1());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD("文本框宽度"));
			tb = Pub1.creatTextBox("TB_UIWidth");
			//		tb.setId("TB_UIWidth");
			//		tb.setName("TB_UIWidth");
			//		tb.setCssClass("TBNum");
			tb.setText(String.valueOf(mapAttr.getUIWidth()));
			this.Pub1.append(AddTD(tb));
			this.Pub1.append(AddTDB("决定列的宽度"));
			this.Pub1.append(AddTREnd());
		}

		//        this.Pub1.append(AddTR());
		//		this.Pub1.append(AddTDIdx(idx++));
		//		this.Pub1.append(AddTD("文本框行数"));
		//		
		////		tb = Pub1.creatTextBox("TB_UIWidth");
		//  //		tb.setId("TB_UIWidth");
		//  //		tb.setName("TB_UIWidth");
		////		tb.setCssClass("TBNum");
		//		tb.setText(String.valueOf(mapAttr.getUIWidth()));
		//		this.Pub1.append(AddTD(tb));
		//		this.Pub1.append(AddTDB("决定列的行数"));
		//		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("文本框行数"));

		DDL ddl = Pub1.creatDDL("DDL_UIRows");

		for (int i = 1; i < 30; i++) {
			ddl.Items.add(new ListItem((new Integer(i)).toString(), (new Integer(i)).toString()));
		}
		ddl.SetSelectItem(mapAttr.getUIRows());

		//tb = new TB();
		//tb.ID = "TB_UIHeight";
		//tb.CssClass = "TBNum";
		//tb.Text = mapAttr.UIHeight.ToString();

		this.Pub1.append(AddTD(ddl));

		this.Pub1.append(AddTD("高度:23个像素是一行"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));

		CheckBox cb = Pub1.creatCheckBox("CB_IsSupperText");
		cb.setText("是否是超大文本？");

		this.Pub1.append(AddTD(cb));
		cb = Pub1.creatCheckBox("CB_IsRichText");
		cb.setText("是否启用富文本编辑器？");
		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		//        #region 合并单元格数
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("横跨的列数"));
		DDL ddl1 = Pub1.creatDDL("DDL_ColSpan");

		ddl1.Items.add(new ListItem("1", "1"));
		ddl1.Items.add(new ListItem("3", "3"));
		ddl1.Items.add(new ListItem("4", "4"));

		ddl1.SetSelectItem(mapAttr.getColSpan());
		this.Pub1.append(AddTD(ddl1));
		this.Pub1.append(AddTD("对傻瓜表单有效"));
		this.Pub1.append(AddTREnd());
		//       #endregion 合并单元格数
		this.EditBeforeEnd(mapAttr);
	}

	public final void EditInt(MapAttr mapAttr) {
		this.EditBeforeAdd(mapAttr);

		//		TextBox tb = new TextBox();
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("文本框宽度"));
		TextBox tb = Pub1.creatTextBox("TB_UIWidth");
		//		tb.setId("TB_UIWidth");
		//		tb.setName("TB_UIWidth");
		tb.setCssClass("TBNum");
		tb.setText(String.valueOf(mapAttr.getUIWidth()));
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTDB("对从表有效"));
		this.Pub1.append(AddTREnd());
		this.EditBeforeEnd(mapAttr);
	}

	public final void EditBool(MapAttr mapAttr) {
		this.Pub1.append(AddTable("width='100%'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("ID")); // 项目
		this.Pub1.append(AddTDTitle("项目")); // 项目
		this.Pub1.append(AddTDTitle("采集")); // 值
		this.Pub1.append(AddTDTitle("描述")); // 描述
		this.Pub1.append(AddTREnd());

		if (mapAttr.getIsTableAttr()) {
			// if here is table attr, It's will let use can change data type. 
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD("改变数据类型"));
			DDL ddlType = Pub1.creatDDL("DDL_DTType");
			//			ddlType.setId("DDL_DTType");
			//			ddlType.setName("DDL_DTType");
			SysDataTypes xmls = new SysDataTypes();
			xmls.RetrieveAll();
			ddlType.Bind(xmls, "No", "Name");
			ddlType.SetSelectItem(mapAttr.getMyDataTypeS());

			//			ddlType.AutoPostBack = true;

			//			ddlType.SelectedIndexChanged += new EventHandler(ddlType_SelectedIndexChanged);
			ddlType.addAttr("onchange", "ddlType_SelectedIndexChanged()");
			this.Pub1.append(AddTD(ddlType));
			this.Pub1.append(AddTD(""));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段中文名"));
		TextBox tb = Pub1.creatTextBox("TB_Name");
		tb.addAttr("onChange", "displayResult()");
		//		tb.setId("TB_Name");
		//		tb.setName("TB_Name");
		tb.setText(mapAttr.getName());
		tb.addAttr("style", "width:100%");

		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段英文名"));
		tb = Pub1.creatTextBox("TB_KeyOfEn");
		//		tb.setId("TB_KeyOfEn");
		//		tb.setName("TB_KeyOfEn");
		tb.setText(mapAttr.getKeyOfEn());

		if (this.getRefNo() != null) {
			tb.setEnabled(false);
		}

		this.Pub1.append(AddTD(tb));

		if (StringHelper.isNullOrEmpty(mapAttr.getKeyOfEn())) {
			this.Pub1.append(AddTD("字母/数字/下划线组合"));
		} else {
			this.Pub1.append(AddTD("<a href=\"javascript:clipboardData.setData('Text','" + mapAttr.getKeyOfEn()
					+ "');alert('已经copy到粘帖版上');\" ><img src='../Img/Btn/Copy.GIF' class='ICON' />复制字段名</a></TD>"));
		}

		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("默认值"));
		CheckBox cb = Pub1.creatCheckBox("CB_DefVal");
		//		cb.setId("CB_DefVal");
		//		cb.setName("CB_DefVal");
		cb.setText("请选择");
		cb.setChecked(mapAttr.getDefValOfBool());

		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否可编辑"));
		this.Pub1.append("<TD>");

		RadioButton rb = Pub1.creatRadioButton("RB_UIIsEnable_0");
		//		rb.setId("RB_UIIsEnable_0");
		//		rb.setName("RB_UIIsEnable_0");
		rb.setText("不可编辑");
		rb.setGroupName("RB_UIIsEnable");
		rb.setChecked(!mapAttr.getUIIsEnable());
		this.Pub1.append(rb);

		rb = Pub1.creatRadioButton("RB_UIIsEnable_1");
		//		rb.setId("RB_UIIsEnable_1");
		//		rb.setName("RB_UIIsEnable_1");
		rb.setText("可编辑");
		rb.setGroupName("RB_UIIsEnable");
		rb.setChecked(mapAttr.getUIIsEnable());
		this.Pub1.append(rb);

		this.Pub1.append("</TD>");
		this.Pub1.append(AddTD());
		// this.Pub1.AddTD(this.ToE("IsReadonly", "是否只读"));
		this.Pub1.append(AddTREnd());

		///#region 是否可单独行显示
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("呈现方式")); //呈现方式;
		this.Pub1.append(AddTDBegin());
		rb = Pub1.creatRadioButton("RB_UIIsLine_0");
		//		rb.setId("RB_UIIsLine_0");
		//		rb.setName("RB_UIIsLine_0");
		rb.setText("两列显示"); // 两行
		rb.setGroupName("RB_UIIsLine");
		if (mapAttr.getUIIsLine()) {
			rb.setChecked(false);
		} else {
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		rb = Pub1.creatRadioButton("RB_UIIsLine_1");
		//		rb.setId("RB_UIIsLine_1");
		//		rb.setName("RB_UIIsLine_1");
		rb.setText("整行显示"); // "一行";
		rb.setGroupName("RB_UIIsLine");

		if (mapAttr.getUIIsLine()) {
			rb.setChecked(true);
		} else {
			rb.setChecked(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD("对傻瓜表单有效"));

		//this.Pub1.AddTD("控制该它在表单的显示方式");
		this.Pub1.append(AddTREnd());
		///#endregion 是否可编辑

		///#region 是否可界面可见
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否界面可见")); //是否界面可见
		this.Pub1.append(AddTDBegin());
		rb = Pub1.creatRadioButton("RB_UIVisible_0");
		//		rb.setId("RB_UIVisible_0");
		//		rb.setName("RB_UIVisible_0");
		rb.setText("不可见"); // 界面不可见
		rb.setGroupName("RB_UIVisible");
		if (mapAttr.getUIVisible()) {
			rb.setChecked(false);
		} else {
			rb.setChecked(true);
		}

		this.Pub1.append((rb));
		rb = Pub1.creatRadioButton("RB_UIVisible_1");
		//		rb.setId("RB_UIVisible_1");
		//		rb.setName("RB_UIVisible_1");
		rb.setText("界面可见"); // 界面可见;
		rb.setGroupName("RB_UIVisible");

		if (mapAttr.getUIVisible()) {
			rb.setChecked(true);
		} else {
			rb.setChecked(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD("不可见则为隐藏字段."));
		//this.Pub1.AddTD("控制该它在表单的界面里是否可见");
		this.Pub1.append(AddTREnd());
		///#endregion 是否可界面可见

		this.EditBeforeEnd(mapAttr);
	}

	public final String getGetCaption() {
		if (this.getDoType().equals("Add")) {
			return "增加新字段向导 - <a href='Do.jsp?DoType=ChoseFType&GroupField=" + this.getGroupField() + "' > 返回类型选择 </a> - " + "编辑";
		} else {
			return "<a href='Do.jsp?DoType=ChoseFType&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo() + "&GroupField=" + this.getGroupField()
					+ "'>返回类型选择</a> - " + "编辑";
		}
	}

}
