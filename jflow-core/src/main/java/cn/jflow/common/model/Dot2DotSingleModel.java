package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.Attr;
import BP.En.AttrOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UAC;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.ToolBar;

public class Dot2DotSingleModel {
	private HttpServletRequest req = null;
	private HttpServletResponse res = null;
	public UiFatory UCSys1 = null;
	public ToolBar ToolBar1 = null;

	public Dot2DotSingleModel(HttpServletRequest request,
			HttpServletResponse response) {
		this.req = request;
		this.res = response;
	}

	// #region 属性.
	private BP.En.AttrOfOneVSM AttrOfOneVSM;

	public AttrOfOneVSM getAttrOfOneVSM() throws Exception {
		Entity en = ClassFactory.GetEn(this.getEnName());
		for (AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM()) {
			String odsd=attr.getEnsOfMM().toString();
			String odsdsas=this.getAttrKey();
			
			if (attr.getEnsOfMM().toString().equals(this.getAttrKey())) {
				return attr;
			}
		}
		throw new Exception("错误没有找到属性． ");
	}

	public void setAttrOfOneVSM(AttrOfOneVSM attrOfOneVSM) {
		AttrOfOneVSM = attrOfOneVSM;
	}

	
	/**
	 * 一的工作类
	 */
	private String EnsName;

	public String getEnsName() {
		return req.getParameter("EnsName");
	}

	public void setEnsName(String ensName) {
		EnsName = ensName;
	}

	private String EnName;

	public String getEnName() {
		return req.getParameter("EnName");
	}

	public void setEnName(String enName) {
		EnName = enName;
	}

	
	private String AttrKey;

	public String getAttrKey() {
		return req.getParameter("AttrKey");
	}

	public void setAttrKey(String attrKey) {
		AttrKey = attrKey;
	}

	
	private String PK;

	public String getPK() {
		String pk = req.getParameter("PK");
		if (pk == null)
			pk = req.getParameter("No");// this.Request.QueryString["No"];

		if (pk == null)
			pk = req.getParameter("RefNo");// this.Request.QueryString["RefNo"];

		if (pk == null)
			pk = req.getParameter("OID");// this.Request.QueryString["OID"];

		if (pk == null)
			pk = req.getParameter("MyPK");// this.Request.QueryString["MyPK"];

		if (pk != null) {
			pk = pk;
		} else {
			Entity mainEn = BP.En.ClassFactory.GetEn(this.getEnName());
			// ViewState["PK"] = this.Request.QueryString[mainEn.PK];
			pk = req.getParameter(mainEn.getPK());
		}
		return pk;
	}

	public void setPK(String pK) {
		PK = pK;
	}

	private DDL DDL_Group;

	public DDL getDDL_Group() {
		return this.ToolBar1.GetDDLByKey("DDL_Group");
	}

	public void setDDL_Group(DDL dDL_Group) {
		DDL_Group = dDL_Group;
	}


	private boolean IsLine;

	public boolean isIsLine() {
		try {
			return IsLine;
		} catch (Exception e) {
			return false;
		}
	}

	public void setIsLine(boolean isLine) {
		IsLine = isLine;
	}

	private String MainEnName;

	public String getMainEnName() {
		return MainEnName;
	}

	public void setMainEnName(String mainEnName) {
		MainEnName = mainEnName;
	}

	private String MainEnPKVal;

	public String getMainEnPKVal() {
		return MainEnPKVal;
	}

	public void setMainEnPKVal(String mainEnPKVal) {
		MainEnPKVal = mainEnPKVal;
	}

	private boolean IsTreeShowWay;

	public boolean isIsTreeShowWay() {
		if (req.getParameter("IsTreeShowWay") != null)
			return true;
		return false;
	}

	public void setIsTreeShowWay(boolean isTreeShowWay) {
		IsTreeShowWay = isTreeShowWay;
	}

	// / <summary>
	// / 显示方式
	// / </summary>
//	private String ShowWay;

	public String getShowWay() {
//		if (ShowWay == null){
//			ShowWay = "None";
//		}
//		return ShowWay;
		String str = req.getParameter("ShowWay")==null ? "" : req.getParameter("ShowWay");// this.Request.QueryString["ShowWay"];
		if (str == null || "".equals(str))
			return "None";
//			str = this.getSelectedValue();
		return str;
	}
//
//	public void setShowWay(String showWay) {
//		ShowWay = showWay;
//	}

	
	private String MainEnPK;

	public String getMainEnPK() {
		return MainEnPK;
	}

	public void setMainEnPK(String mainEnPK) {
		MainEnPK = mainEnPK;
	}

	
	private Entity _MainEn = null;
	private Entity MainEn;

	public Entity getMainEn() {
		if (_MainEn == null)
			_MainEn = ClassFactory.GetEn(req.getParameter("EnsName"));
		return _MainEn;
	}

	public void setMainEn(Entity mainEn) {
		_MainEn = mainEn;
	}

	

	public int ErrMyNum = 0;
	private LinkButton Btn_Save;

	public LinkButton getBtn_Save() {
		return this.ToolBar1.GetLinkBtnByID("Btn_Save");
	}

	public void setBtn_Save(LinkButton btn_Save) {
		Btn_Save = btn_Save;
	}

	// public LinkBtn Btn_Save
	
	private LinkButton Btn_SaveAndClose;

	public LinkButton getBtn_SaveAndClose() {
		return this.ToolBar1.GetLinkBtnByID("Btn_SaveAndClose");
	}

	public void setBtn_SaveAndClose(LinkButton btn_SaveAndClose) {
		Btn_SaveAndClose = btn_SaveAndClose;
	}

	
	// #endregion 属性.
	//
	// #region Page_Load
//	private String SelectedValue;
//	public String getSelectedValue() {
//		if("1".equals(SelectedValue))
//		{
//			return "StaGrade";
//		}
//		if("2".equals(SelectedValue))
//		{
//			return "None";
//		}
//		if("3".equals(SelectedValue))
//		{
//			return "FK_Dept";
//		}
//		return SelectedValue;
//	}
//	public void setSelectedValue(String selectedValue) {
//		SelectedValue = selectedValue;
//	}
	public void Page_Load() throws Exception {
//		String no=req.getParameter("num");
//		this.setSelectedValue(no);
//		this.ShowWay=no;
		UCSys1 = new UiFatory();
		ui = new UiFatory();
		ToolBar1 = new ToolBar(req, res, ui);
		try {
			// #region 处理可能来自于 父实体 的业务逻辑。
			Entity enP = ClassFactory.GetEn(this.getEnName());
			// this.Page.Title = enP.getEnDesc();
			this.setMainEnName(enP.getEnDesc());
			this.setMainEnPKVal(this.getPK());
			this.setMainEnPK(enP.getPK());
			if (enP.getEnMap().getEnType() != EnType.View) {
				try {
					enP.SetValByKey(enP.getPK(), this.getPK());// =this.PK;
					enP.Retrieve(); // 查询。
					enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			MainEn = enP;
			// #endregion
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		AttrOfOneVSM ensattr = this.getAttrOfOneVSM();
		this.ToolBar1.AddLab("lab_desc", "分组:");
		DDL ddl = ui.creatDDL("DDL_Group");
		this.ToolBar1.append(ddl);
		ddl.Items.clear();
		ddl.addAttr("onchange", "DDL_Group_SelectedIndexChanged()");
		Entity open = ensattr.getEnsOfM().getGetNewEntity();
		Map map = open.getEnMap();
		int len = 19;

		// 如果最长的 标题 〉 15 长度。就用一行显示。
		if (len > 20)
			this.setIsLine(true);
		else
			this.setIsLine(false);

		// 先加入enum 类型。
		for (Attr attr : map.getAttrs()) {
			/* map */
			if (attr.getIsFKorEnum() == false)
				continue;
			this.getDDL_Group().Items.add(new ListItem(attr.getDesc(), attr
					.getKey()));
		}

		this.getDDL_Group().Items.add(new ListItem("无", "None"));
		for (ListItem li : ddl.Items) {
			if (li.getValue().equals(this.getShowWay()))
				li.setSelected(true);
		}
		this.ToolBar1.AddSpt("spt");

		CheckBox cb = ui.creatCheckBox("checkedAll");
		cb.addAttr("onclick", "selectAll(this);");
		cb.setText("选择全部");
		this.ToolBar1.append(cb);

		// #region 处理保存权限.
		UAC uac = ensattr.getEnsOfMM().getGetNewEntity().getHisUAC();
		if (uac.IsInsert == true || uac.IsUpdate == true) {
			this.ToolBar1.AddSpace(4);
			this.ToolBar1.AddLinkBtn("Btn_Save", "保存");
			try {
				this.ToolBar1.GetLinkBtnByID("Btn_Save").addAttr("onclick",
						"return BPToolBar1_ButtonClick()");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// #endregion 处理保存权限. 
		this.SetDataV2();
	}

	// #endregion Page_Load
	//
	// #region 方法
	public void SetDataV2() throws Exception {
		AttrOfOneVSM attrOM = this.getAttrOfOneVSM();
		Entities ensOfM = attrOM.getEnsOfM();
		ensOfM.RetrieveAll();

		try {
			Entities ensOfMM = attrOM.getEnsOfMM();
			QueryObject qo = new QueryObject(ensOfMM);
			qo.AddWhere(attrOM.getAttrOfOneInMM(), this.getPK());
			qo.DoQuery();
       	 
            if ("None".equals(this.getShowWay())){
           	 if (this.IsLine){
           		 this.UIEn1ToM_OneLine(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM());
           	 }else{
           		 this.UIEn1ToM(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM());
           	 }
            }else{
           	  if (this.IsLine){
           		  this.UIEn1ToMGroupKey_Line(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM(), this.getShowWay());
           	  }else{
           		  this.UIEn1ToMGroupKey(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM(), this.getShowWay());
           	  }
            }
		} catch (Exception ex) {
			try {
				ensOfM.getGetNewEntity().CheckPhysicsTable();
			} catch (Exception ex1) {
				BP.DA.Log.DefaultLogWriteLineError(ex1.getMessage());
			}

			// this.UCSys1.ClearViewState();
			ErrMyNum++;
			if (ErrMyNum > 3) {
//				this.UCSys1.append(BaseModel.AddMsgOfWarning("error",
//						ex.getMessage()));
				ex.printStackTrace();
				return;
			}
			this.SetDataV2();
		}
	}

	// #endregion
	//
	// #region Web 窗体设计器生成的代码
	protected void OnInit() {
		//
		// CODEGEN: 该调用是 ASP.NET Web 窗体设计器所必需的。
		//
		InitializeComponent();
		// base.OnInit(e);
	}

	// / <summary>
	// / 设计器支持所需的方法 - 不要使用代码编辑器修改
	// / 此方法的内容。
	// / </summary>
	private void InitializeComponent() {

	}

	public void UIEn1ToM_OneLine(Entities ens, String showVal, String showText,
			Entities selectedEns, String selecteVal) {
		//this.Controls.Clear();
        UCSys1.append("<table border=0 width='500px'>");
        boolean is1 = false;
        for (Entity en : Entities.convertEntities(ens))
        {
            UCSys1.append(BaseModel.AddTR(is1)); //("<TR>");
            is1 = !is1;
            CheckBox cb = ui.creatCheckBox("CB_" + en.GetValStrByKey(showVal));// new CheckBox();
//            cb.ID = "CB_" + en.GetValStrByKey(showVal);
            cb.setText(en.GetValStringByKey(showText));
            UCSys1.append("<td>");
            UCSys1.append(cb);
            UCSys1.append("</td>");
            UCSys1.append(BaseModel.AddTREnd());
        }
        UCSys1.append(BaseModel.AddTableEnd());

        // 设置选择的 ens .
        for (Entity en :Entities.convertEntities(selectedEns))
        {
            String key = en.GetValStrByKey(selecteVal);
            CheckBox bp = (CheckBox)this.ui.GetUIByID("CB_" + key);
            bp.setChecked(true);
        }
	}

	public void UIEn1ToM(Entities ens, String showVal, String showText,
			Entities selectedEns, String selecteVal) {
		this.UCSys1
				.append(BaseModel
						.AddTable1("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
		int i = 0;
		boolean is1 = false;
		for (Entity en : Entities.convertEntities(ens)) {
			i++;
			if (i == 4)
				i = 1;

			if (i == 1) {
				this.UCSys1.append(BaseModel.AddTR(is1));
				is1 = !is1;
			}

			CheckBox cb = this.ui.creatCheckBox("CB_"
					+ en.GetValStringByKey(showVal));
			cb.setText(en.GetValStringByKey(showText));

			this.UCSys1.append("\n<TD nowrap = 'nowrap'>");
			this.UCSys1.append(cb);
			this.UCSys1.append("</TD>");
			if (i == 3)
				this.UCSys1.append(BaseModel.AddTREnd());
		}

		switch (i) {
		case 1:
			this.UCSys1.append(BaseModel.AddTD());
			this.UCSys1.append(BaseModel.AddTD());// "<TD>&nbsp;</TD>");
			this.UCSys1.append(BaseModel.AddTREnd());// ("</TR>");
			break;
		case 2:
			this.UCSys1.append(BaseModel.AddTD());
			this.UCSys1.append(BaseModel.AddTREnd());
			break;
		default:
			break;
		}
		this.UCSys1.append(BaseModel.AddTableEnd());

		// 设置选择的 ens .
		for (Entity en : Entities.convertEntities(selectedEns)) {
			String key = en.GetValStringByKey(selecteVal);
			try {
				CheckBox bp = (CheckBox) this.ui.GetUIByID("CB_" + key);
				bp.setChecked(true);
			} catch (Exception e) {
			}
		}
	}

	public void UIEn1ToMGroupKey_Line(Entities ens, String showVal,
			String showText, Entities selectedEns, String selecteVal,
			String groupKey) {
		this.UCSys1
				.append(BaseModel
						.AddTable1("class='Table' cellSpacing='1' cellPadding='1'  border='1' width='100%'"));

		Attr attr = ens.getGetNewEntity().getEnMap().GetAttrByKey(groupKey);
		String val = null;
		Entity seEn = null;
		if (attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum) { // 检查是否是 enum
																// 类型。
			SysEnums eens = new SysEnums(attr.getKey());
			for (SysEnum se : SysEnums.convertSysEnums(eens)) {
				this.UCSys1.append(BaseModel.AddTR());
				this.UCSys1.append("<TD class='GroupTitle' >" + se.getLab()
						+ "</TD>");
				this.UCSys1.append(BaseModel.AddTREnd());
				for (Entity en : Entities.convertEntities(ens)) {
					if (en.GetValIntByKey(attr.getKey()) != se.getIntKey())
						continue;

					this.UCSys1.append(BaseModel.AddTR());
					val = en.GetValStrByKey(showVal);
					CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_"
							+ se.getIntKey());// edited by liuxc,2015.1.6

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					cb.setText(en.GetValStrByKey(showText));
					this.UCSys1.append("\n<TD nowrap = 'nowrap'>");
					this.UCSys1.append(cb);
					this.UCSys1.append("</TD>");
					// this.UCSys1.append(BaseModel.AddTD(cb));
					this.UCSys1.append(BaseModel.AddTREnd());
				}
			}
		} else {
			Entities groupEns = ClassFactory.GetEns(attr.getUIBindKey());
			groupEns.RetrieveAll();
			String gVal = null;
			for (Entity group : Entities.convertEntities(groupEns)) {
				gVal = group.GetValStringByKey(attr.getUIRefKeyValue());
				this.UCSys1.append("<TR>");
				this.UCSys1.append("<TD class='GroupTitle' >"
						+ group.GetValStringByKey(attr.getUIRefKeyText())
						+ "</TD>");
				this.UCSys1.append(BaseModel.AddTREnd());

				for (Entity en : Entities.convertEntities(ens)) {
					if (!(en.GetValStringByKey(attr.getKey())).equals(gVal))
						continue;

					this.UCSys1.append("<TR>");
					val = en.GetValStringByKey(showVal);
					CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_"
							+ gVal); // edited by liuxc,2015.1.6
					cb.setText(en.GetValStringByKey(showText));

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					this.UCSys1.append("<TD nowrap = 'nowrap'>");
					this.UCSys1.append(cb);
					this.UCSys1.append("</TD>");
					this.UCSys1.append(BaseModel.AddTREnd());
				}
			}
		}
		this.UCSys1.append(BaseModel.AddTableEnd());
	}

	public UiFatory ui = null;

	public void UIEn1ToMGroupKey(Entities ens, String showVal, String showText,
			Entities selectedEns, String selecteVal, String groupKey) {
		
		this.UCSys1.append(BaseModel.AddTable1("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%;'"));

		String val = null;
		Entity seEn = null;
		Attr attr = ens.getGetNewEntity().getEnMap().GetAttrByKey(groupKey);
		if (attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum) { // 检查是否是 enum 类型

			SysEnums eens = new SysEnums(attr.getKey());
			for (SysEnum se : SysEnums.convertSysEnums(eens)) {
				this.UCSys1.append("<TR>");
				this.UCSys1.append("<TD class='GroupTitle' colspan=3 >");

				CheckBox cb1 = this.ui.creatCheckBox("CB_SE_" + se.getIntKey());
				cb1.setText(se.getLab());
				this.UCSys1.append(cb1);
				this.UCSys1.append("</TD>");
				this.UCSys1.append(BaseModel.AddTREnd());

				int i = 0;
				boolean is1 = false;
				String ctlIDs = "";
				for (Entity en : Entities.convertEntities(ens)) {
					if (en.GetValIntByKey(attr.getKey()) != se.getIntKey())
						continue;

					i++;
					if (i == 4)
						i = 1;
					if (i == 1) {
						this.UCSys1.append(BaseModel.AddTR(is1));
						is1 = !is1;
					}

					val = en.GetValStringByKey(showVal);
					CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_"
							+ se.getIntKey());
					ctlIDs += cb.getId() + ",";

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					cb.setText(en.GetValStringByKey(showText));
					// cb.AccessKey = se.IntKey.ToString();
					this.UCSys1.append("<TD nowrap = 'nowrap'>");
					this.UCSys1.append(cb);
					this.UCSys1.append("</TD>");
					if (i == 3)
						this.UCSys1.append(BaseModel.AddTREnd());
				}

				if (ctlIDs.length() > 0)
					ctlIDs = ctlIDs.substring(0, ctlIDs.length() - 1);
				cb1.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");

				switch (i) {
				case 1:
					this.UCSys1.append(BaseModel.AddTD());
					this.UCSys1.append(BaseModel.AddTD());// "<TD>&nbsp;</TD>");
					this.UCSys1.append(BaseModel.AddTREnd());// ("</TR>");
					break;
				case 2:
					this.UCSys1.append(BaseModel.AddTD());
					this.UCSys1.append(BaseModel.AddTREnd());
					break;
				default:
					break;
				}
			}
		} else {
			Entities groupEns = ClassFactory.GetEns(attr.getUIBindKey());
			groupEns.RetrieveAll();
			for (Entity group : Entities.convertEntities(groupEns)) {
				this.UCSys1.append("<TR>");
				this.UCSys1.append("<TD class='GroupTitle' colspan=3>");

				CheckBox cb1 = this.ui.creatCheckBox("CB_EN_"
						+ group.GetValStrByKey(attr.getUIRefKeyValue()));
				cb1.setText(group.GetValStrByKey(attr.getUIRefKeyText()));
				// cb1.Attributes["onclick"] = "SetSelected(this,'" +
				// group.GetValStringByKey(attr.UIRefKeyValue) + "')";
				this.UCSys1.append(cb1);
				this.UCSys1.append("</TD>");
				this.UCSys1.append(BaseModel.AddTREnd());

				String ctlIDs = "";
				int i = 0;
				String gVal = group.GetValStrByKey(attr.getUIRefKeyValue());
				for (Entity en : Entities.convertEntities(ens)) {
					if (!(en.GetValStrByKey(attr.getKey())).equals(gVal))
						continue;
					i++;
					if (i == 4)
						i = 1;
					if (i == 1)
						this.UCSys1.append("<TR>");

					val = en.GetValStringByKey(showVal);
					CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_"
							+ gVal);// edited by liuxc,2015.1.6

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					cb.setText(en.GetValStrByKey(showText));
					this.UCSys1.append("<TD nowrap = 'nowrap'>");
					this.UCSys1.append(cb);
					this.UCSys1.append("</TD>");

					ctlIDs += cb.getId() + ",";
					if (i == 3)
						this.UCSys1.append(BaseModel.AddTREnd());
				}

				if (ctlIDs.length() > 0)
					ctlIDs = ctlIDs.substring(0, ctlIDs.length() - 1);
				cb1.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");

				switch (i) {
				case 1:
					this.UCSys1.append(BaseModel.AddTD());
					this.UCSys1.append(BaseModel.AddTD());// "<TD>&nbsp;</TD>");
					this.UCSys1.append(BaseModel.AddTREnd());// ("</TR>");
					break;
				case 2:
					this.UCSys1.append(BaseModel.AddTD());
					this.UCSys1.append(BaseModel.AddTREnd());
					break;
				default:
					break;
				}

			}
		}

		this.UCSys1.append(BaseModel.AddTableEnd());
	}
}
