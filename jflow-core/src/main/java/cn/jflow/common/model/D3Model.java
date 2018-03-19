package cn.jflow.common.model;


import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UIContralType;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Sys.DTSearchWay;
import BP.Sys.MapAttrs;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Data.NDXRptBaseAttr;
import BP.WF.Rpt.MapRpt;
import BP.Web.WebUser;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.ToolBar;

public class D3Model extends BaseModel {
	
	public ToolBar toolBar1;
	private String NumKey = null;
	private String OrderBy=null;
	private String OrderWay=null;
	private String IsContinueNDYF=null;
	private String CfgVal;
	public StringBuilder Left=new StringBuilder();
	public StringBuilder Right = new StringBuilder();
	public String IsPostBack;
	public HashMap<String, BaseWebControl> ctls;

	
		public D3Model(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		 toolBar1 = new ToolBar(get_request(), get_response());
		 
			
	}
		public boolean getIsPostBack() {
			if(IsPostBack == null){
				IsPostBack = getParameter("isPostBack");
			}
			if(IsPostBack == null || "".equals(IsPostBack)){
				return false;
			}else if("true".equals(IsPostBack.toLowerCase())){
				return true;
			}else{
				return false;
			}
		}
		// 编号名称
		public  String getRptNo() {
			//String s = this.Request.QueryString["RptNo"];
			String s = getParameter("RptNo");
			if (StringHelper.isNullOrEmpty(s)) {
				return "ND68MyRpt";
			}
			return s;
		}
		public  String getFK_Flow() {
			//String s = this.Request.QueryString["FK_Flow"];
			String s = getParameter("Fk_Flow");
			if (StringHelper.isNullOrEmpty(s)) {
				return "068";
			}
			return s;
		}
		public Entities _HisEns = null;
		public final Entities getHisEns() {
			if (_HisEns == null) {
				if (this.getRptNo() != null) {
					if (this._HisEns == null) {
						_HisEns = BP.En.ClassFactory.GetEns(this.getRptNo());
					}
				}
			}
			return _HisEns;
		}
		public  MapRpt currMapRpt = null;
		
		//key
		public final String getKey() {
			try {
				return this.toolBar1.GetTBByID("TB_Key").getText();
			}
			catch (java.lang.Exception e) {
				return null;
			}
		}
		public UserRegedit ur = null;
	
		 //是否分页
		public final boolean getIsFY() {
			//String str = this.Request.QueryString["IsFY"];
			String str= this.getParameter("IsFY");
			if (str == null || str.equals("0")) {
				return false;
			}
			return true;
		}
		public final String getNumKey() {
			//String str = this.Request.QueryString["NumKey"];
			String str  = this.getParameter("NumKey");
			if (str == null) {
				return (String)((NumKey instanceof String) ? NumKey : null);
			}
			else {
				return str;
			}
		}
		public final void setNumKey(String value) {
			NumKey = value;
		}
		public final String getOrderBy() {
			//String str = this.Request.QueryString["OrderBy"];
			String str= this.getParameter("OrderBy");
			if (str == null) {
			//	return (String)((ViewState["OrderBy"] instanceof String) ? ViewState["OrderBy"] : null);
				return (String)((OrderBy instanceof String) ? OrderBy : null);
			}
			else {
				return str;
			}
		}
		public final void setOrderBy(String value) {
			OrderBy = value;
		}
		public final String getOrderWay() {
			///String str = this.Request.QueryString["OrderWay"];
			String str= getParameter("OrderBy");
			if (str == null) {
				return (String)((OrderWay instanceof String) ? OrderWay : null);
			}
			else {
				return str;
			}
		}
		public final void setOrderWay(String value) {
			OrderWay = value;
		}
	
		public final boolean getIsShowSum() {
			//String i = this.Request.QueryString["IsShowSum"];
			String i = this.getParameter("IsShowSum");
			if (i.equals("1")) {
				return true;
			}
			else {
				return false;
			}
		}
		public final boolean getIsContainsNDYF() {
			if (this.IsContinueNDYF.toString().toUpperCase().equals("TRUE")) {
				return true;
			}
			else {
				return false;
			}
		}
		public final String getCfgVal() {
			return "";
				//return this.ViewState["CfgVal"].ToString();
		}
		public final void setCfgVal(String value) {
			this.CfgVal = value;
		}
		public final CheckBox getCB_IsImg() {
			//return this.Left.GetCBByID("CB_IsImg");
		    return (CheckBox) ctls.get("CB_IsImg");
		}
		/** 
		 过滤为空的数据.
		 
		*/
		public final CheckBox getCB_IsNull() {
			//return this.Left.GetCBByID("CB_IsNull");
			 return (CheckBox) ctls.get("CB_IsNull");
		}

		public final CheckBox getCB_IsRate() {
			//return this.Left.GetCBByID("CB_IsRate");
			 return (CheckBox) ctls.get("CB_IsRate");
		}
		public final DDL getDDL_Num() {
			//return this.Left.GetDDLByID("DDL_Num");
			 return (DDL) ctls.get("DDL_Num");
		}
		public final DDL getDDL_FXWay() {
			//return this.Left.GetDDLByID("DDL_FXWay");
			 return (DDL) ctls.get("DDL_FXWay");
		}
		public final DDL getDDL_D1() {
		//	return this.Left.GetDDLByID("DDL_D1");
			 return (DDL) ctls.get("DDL_D1");
		}
		public final DDL getDDL_D1_Order() {
			//return this.Left.GetDDLByID("DDL_D1_Order");
			 return (DDL) ctls.get("DDL_D1_Order");
		}

		public final DDL getDDL_D2() {
			//return this.Left.GetDDLByID("DDL_D2");
			 return (DDL) ctls.get("DDL_D2");
		}
		public final DDL getDDL_D2_Order() {
			//return this.Left.GetDDLByID("DDL_D2_Order");
			 return (DDL) ctls.get("DDL_D2_Order");
		}
		public MapAttrs currMapAttrs = null;
		public Entity currEn = null;

		
		public final void Page_Load() {
			this.ur = new UserRegedit(WebUser.getNo(), this.getRptNo() + "_D3");
			this.currMapAttrs = new MapAttrs(this.getRptNo());
			this.currEn = this.getHisEns().getGetNewEntity();
			currMapRpt = new MapRpt(this.getRptNo());
			Entity en = this.getHisEns().getGetNewEntity();
			Flow fl = new Flow(this.currMapRpt.getFK_Flow());
			//初始化查询工具栏.
			this.toolBar1.InitToolbarOfMapRpt(fl, currMapRpt, this.getRptNo(), en, 1);
			//this.toolBar1.AddLinkBtn(BP.Web.Controls.NamesOfBtn.Export);
			this.toolBar1.AddLinkBtn(NamesOfBtn.Export);

			//增加转到.
			this.toolBar1.Add("&nbsp;");
			DDL ddl = new DDL();
			ddl.setId("GoTo"); 
			ddl.Items.add(new ListItem("查询", "Search"));
		 //   ddl.Items.Add(new ListItem("高级查询", "SearchAdv"));
			ddl.Items.add(new ListItem("分组分析", "Group"));
			ddl.Items.add(new ListItem("交叉报表", "D3"));
			ddl.Items.add(new ListItem("对比分析", "Contrast"));
			ddl.SetSelectItem(getPageID());
			this.toolBar1.AddDDL(ddl);
			//ddl.AutoPostBack = true;
		/*	ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged_GoTo);
			this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Search).Click += new System.EventHandler(this.ToolBar1_ButtonClick);
			this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Export).Click += new System.EventHandler(this.ToolBar1_ButtonClick);
		 */
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged_Goto()");
			toolBar1.GetLinkBtnByID(NamesOfBtn.Search).setHref("ToolBar1_ButtonClick('"+NamesOfBtn.Search.getCode()+"')");
			toolBar1.GetLinkBtnByID(NamesOfBtn.Export).setHref("ToolBar1_ButtonClick('"+NamesOfBtn.Export.getCode()+"')");
			//处理left.
			this.InitLeft();

			if (this.getIsPostBack() == false) {
				this.BindDG();
			}
		}
		
	/*	private void ddl_SelectedIndexChanged_GoTo(HttpServletRequest request,HttpServletResponse resopnse) {
			DDL ddl = (DDL)((sender instanceof DDL) ? sender : null);
			String item = ddl.getSelectedItemStringVal();

			String tKey = new java.util.Date().ToString("MMddhhmmss");
			this.Response.Redirect(item + ".aspx?RptNo=" + this.getRptNo() + "&FK_Flow=" + this.getFK_Flow() + "&T=" + tKey, true);
		}*/
		/** 
		 初始化left.
		 
		*/
		public final void InitLeft() {
			this.currEn = this.getHisEns().getGetNewEntity();
			this.ur = new UserRegedit(WebUser.getNo(), this.getRptNo() + "_D3");
			this.Left.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
			String paras = this.ur.getParas();
			this.Left.append(AddTR());
			this.Left.append(AddTDGroupTitle("colspan=2", "选项"));
			this.Left.append(AddTREnd());

			DDL ddl = new DDL();
			ddl.setId( "DDL_Num");
			//ddl.AutoPostBack = true;
			//ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged()");


			ListItem li = null;
			this.Left.append(AddTR());
			this.Left.append(AddTD("分析数据："));
			Attrs attrs = this.currEn.getEnMap().getAttrs();
			attrs.AddTBInt("MyNum", 1, "流程数量", true, true);
			for (Attr attr : attrs) {
				if (attr.getUIContralType() != UIContralType.TB) {
					continue;
				}

				if (attr.getUIVisible() == false) {
					continue;
				}

				if (attr.getIsNum() == false) {
					continue;
				}

				//去掉特殊的字段.
				String key = attr.getKey();
				if(key.equals(NDXRptBaseAttr.OID)
						|| key.equals("MID")
						|| key.equals(NDXRptBaseAttr.PWorkID)
						|| key.equals(NDXRptBaseAttr.FID)
						|| key.equals(NDXRptBaseAttr.FlowEndNode)
						|| key.equals("WorkID")){
					continue;
				} 

				li = new ListItem(attr.getDesc(), attr.getKey());
				if (paras.contains("@Num=" + attr.getKey())) {
					li.setSelected(true); 
				}
				ddl.Items.add(li);
			}
			this.Left.append(AddTD(ddl));
			/*this.Left.AddTREnd();
			this.Left.AddTR();
			this.Left.AddTD("分析方式：");*/
			this.Left.append(AddTREnd());
			this.Left.append(AddTR());
			this.Left.append(AddTD("分析方式："));
			ddl = new DDL();
			ddl.setId("DDL_FXWay");
			//ddl.AutoPostBack = true;

			li = new ListItem("求和", "SUM");
			if (paras.contains("@FXWay=SUM")) {
				li.setSelected(true);
			}
			ddl.Items.add(li);

			li = new ListItem("求平均", "AVG");
			if (paras.contains("@FXWay=AVG")) {
				li.setSelected(true); 
			}
			ddl.Items.add(li);
			//ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged()");

			this.Left.append(AddTD(ddl));
			this.Left.append(AddTREnd());


			this.Left.append(AddTR());
			CheckBox cb = new CheckBox();
			//cb.AutoPostBack = true;
			cb.setId("CB_IsRate");
			cb.setText("显示百分比");
			if (paras.contains("@IsRate=1")) {
				cb.setChecked(true);
			}
			else {
				cb.setChecked(false);
			}

			//cb.CheckedChanged += new EventHandler(cb_CheckedChanged);
			cb.addAttr("onchange", "cb_CheckedChanged()");

			this.Left.append(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsImg");
			//cb.AutoPostBack = true;
			if (paras.contains("@IsImg=1")) {
				cb.setChecked(true);
			}
			else {
				cb.setChecked(false);
			}

			cb.setText("显示图形");
	
			//cb.CheckedChanged += new EventHandler(cb_CheckedChanged);
			ddl.addAttr("onchange", "cb_CheckedChanged()");

			this.Left.append(AddTD(cb));
			this.Left.append(AddTREnd());


			AtPara ps = new AtPara(paras);
			this.Left.append(AddTR());
			this.Left.append(AddTDBegin());
			TextBox tb = new TextBox();
		
			tb.addAttr("style", "width:40px;");
			//tb.setWidth(40);
			tb.setId( "TB_W");
			//tb.BorderWidth = 1;
			tb.addAttr("style", "border:1px;");
			//tb.BorderStyle = BorderStyle.Outset;
			tb.setText(ps.GetValStrByKey("W"));
			if (StringHelper.isNullOrEmpty(tb.getText())) {
				tb.setText("500");
			}

			this.Left.append("宽：");
			this.Left.append(tb);
			this.Left.append(AddTDEnd());

			this.Left.append(AddTDBegin());
			this.Left.append("高：");
			tb = new TextBox();
			tb.addAttr("style", "width:40px;");
			//tb.setWidth(40);
			tb.setId( "TB_H");
			//tb.BorderWidth = 1;
			tb.addAttr("style", "border:1px;");
			//tb.BorderStyle = BorderStyle.Outset;
			tb.setText(ps.GetValStrByKey("H"));
			if (StringHelper.isNullOrEmpty(tb.getText())) {
				tb.setText("300");
			}

			this.Left.append(tb);
			this.Left.append(AddTDEnd());
			this.Left.append(AddTREnd());


			this.Left.append(AddTR());
			cb = new CheckBox();
			//cb.AutoPostBack = true;
			cb.setId("CB_IsNull");
			cb.setText("过滤为null值的数据");
			if (paras.contains("@IsNull=1")) {
				cb.setChecked(true);
			}
			else {
				cb.setChecked(false);
			}
			//cb.CheckedChanged += new EventHandler(cb_CheckedChanged);
			cb.addAttr("onchange", "cb_CheckedChanged()");
			this.Left.append(AddTD("colspan=2", cb));
			this.Left.append(AddTREnd());
			this.Left.append(AddTR());
			this.Left.append(AddTDGroupTitle("colspan='2'", "横纬度 - <a href=# >横竖互换</a>"));
			this.Left.append(AddTREnd());

			ddl = new DDL();
			ddl.setId("DDL_D1");
			//ddl.AutoPostBack = true;
	
			//ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
			cb.addAttr("onchange", "ddl_SelectedIndexChanged()");
			this.Left.append(AddTR());
			this.Left.append(AddTD("纬度项目："));
			for (Attr attr : attrs) {
				if (attr.getUIContralType() == UIContralType.DDL) {
					li = new ListItem(attr.getDesc(),attr.getKey());

					if (paras.contains("@D1=" + attr.getKey())) {
						li.setSelected(true);
					}

					ddl.Items.add(new ListItem(attr.getDesc(), attr.getKey()));
				}
			}
			this.Left.append(AddTD(ddl));
			this.Left.append(AddTREnd());

			ddl = new DDL();
			ddl.setId("DDL_D1_Order");
			//ddl.AutoPostBack = true;
			ddl.Items.add(new ListItem("升序", "Up"));
			ddl.Items.add(new ListItem("降序", "Desc"));
			if (paras.contains("@D1_Order=Up")) {
				li.setSelected(true);
			}
			else {
				li.setSelected(false);
			}

			this.Left.append(AddTR());
			this.Left.append(AddTD("排序方式："));
			this.Left.append(AddTD(ddl));
			this.Left.append(AddTREnd());
			this.Left.append(AddTR());
			this.Left.append(AddTDGroupTitle("colspan='2'", "纵纬度"));
			this.Left.append(AddTREnd());

			this.Left.append(AddTR());
			this.Left.append(AddTD("数据项目："));
			ddl = new DDL();
			ddl.setId("DDL_D2");
			//ddl.AutoPostBack = true;
		//	ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged()");
			for (Attr attr : attrs) {
				if (attr.getUIContralType() == UIContralType.DDL) {
					li = new ListItem(attr.getDesc(), attr.getKey());
					if (paras.contains("@D2=" + attr.getKey())) {
						li.setSelected(true);
					}
					ddl.Items.add(new ListItem(attr.getDesc(), attr.getKey()));
				}
			}
			this.Left.append(AddTD(ddl));
			this.Left.append(AddTREnd());

			ddl = new DDL();
			ddl.setId("DDL_D2_Order");
			//ddl.AutoPostBack = true;
			//ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
			ddl.addAttr("onchange", "ddl_SelectedIndexChanged()");
			ddl.Items.add(new ListItem("升序", ""));
			ddl.Items.add(new ListItem("降序", "DESC"));
			if (paras.contains("@D2_Order=Up")) {
				li.setSelected(true);
			}
			else {
				li.setSelected(false);
			}
			this.Left.append(AddTR());
			this.Left.append(AddTD("排序方式："));
			this.Left.append(AddTD(ddl));
			this.Left.append(AddTREnd());
			this.Left.append(AddTableEnd());
		}
		public final  DataTable BindDG() {
			//处理数据源是否正确.
			ctls = HtmlUtils.httpParser(Left.toString(), false);
			if (this.getDDL_D1().Items.size() <= 1 || this.getDDL_Num().Items.size() == 0) {
				this.Right.setLength(0);
				this.toolBar1.setIsVisible(false);
				//this.Right.append(AddMsgGreen("提示:", "<h2>没有足够的纬度或者没有数据分析项目。</h2>"));
			this.Right.append(AddMsgOfInfo("提示:", "<h2>没有足够的纬度或者没有数据分析项目。</h2>"));
				return null;
			}

			//不能让两个维度选择一致.
			if (this.getDDL_D1().getSelectedItemStringVal() == this.getDDL_D2().getSelectedItemStringVal()) {
				//if (this.getDDL_D1().SelectedIndex == 0) {
				if (this.getDDL_D1().getSelectedItemIntVal()== 0) {
					//this.getDDL_D2().SelectedIndex = 1;
					this.getDDL_D2().SetSelectItemByIndex(1);
				}
				else {
					//this.getDDL_D2().SelectedIndex = 0;
					this.getDDL_D2().SetSelectItemByIndex(0);
				}
			}

			Attrs attrs = this.currEn.getEnMap().getAttrs();
			SysEnums sesD1 = null;
			Entities ensD1 = null;

			SysEnums sesD2 = null;
			Entities ensD2 = null;
			Map map = this.currEn.getEnMap();
			Attr attrD1 = attrs.GetAttrByKey(this.getDDL_D1().getSelectedItemStringVal());
			if (attrD1.getIsEnum()) {
				sesD1 = new SysEnums(attrD1.getUIBindKey());
			}
			else {
				ensD1 = attrD1.getHisFKEns();
				if (ensD1.size() == 0) {
					ensD1.RetrieveAll();
				}
			}

			Attr attrD2 = attrs.GetAttrByKey(this.getDDL_D2().getSelectedItemStringVal());
			if (attrD2.getIsEnum()) {
				sesD2 = new SysEnums(attrD2.getUIBindKey());
			}
			else {
				ensD2 = attrD2.getHisFKEns();
				if (ensD2.size() == 0) {
					ensD2.RetrieveAll();
				}
			}
			String Condition = ""; //处理特殊字段的条件问题。
			Paras myps = new BP.DA.Paras();
			String sql = "SELECT " + attrD1.getKey() + "," + attrD2.getKey() + ", " + this.getDDL_FXWay().getSelectedItemStringVal() + "(" + this.getDDL_Num().getSelectedItemStringVal() + ") FROM " + map.getPhysicsTable();
			// 找到 WHERE 数据。
			String where = " WHERE ";
			String whereOfLJ = " WHERE "; // 累计的where.
			String url = "";
			
			HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(toolBar1.toString(), false);

			//HtmlUtils.httpParser("", get_request());
			for (java.util.Map.Entry<String, BaseWebControl> item : controls.entrySet()) {
		//	for (Control item : this.ToolBar1.Controls) {
				if (item.getKey() == null) {
					continue;
				}
				if (item.getKey().indexOf("DDL_") == -1) {
					continue;
				}
				if (item.getKey().indexOf("DDL_Form_") == 0 || item.getKey().indexOf("DDL_To_") == 0) {
					continue;
				}

				String key = item.getKey().substring((new String("DDL_")).length());
				DDL ddl = (DDL)item;
				if (ddl.getSelectedItemStringVal().equals("all")) {
					continue;
				}

				String val = ddl.getSelectedItemStringVal();
				if (val == null) {
					continue;
				}

				if (val.equals("mvals")) {
					UserRegedit sUr = new UserRegedit();
					sUr.setMyPK(WebUser.getNo() + this.getRptNo() + "_SearchAttrs");
					sUr.RetrieveFromDBSources();

					// 如果是多选值 
					String cfgVal = sUr.getMVals();
					AtPara ap = new AtPara(cfgVal);
					String instr = ap.GetValStrByKey(key);
					if (instr == null || instr.equals("")) {
						if (key.equals("FK_Dept") || key.equals("FK_Unit")) {
							if (key.equals("FK_Dept")) {
								val = WebUser.getFK_Dept();
								//ddl.SelectedIndex = 0;
								ddl.SetSelectItemByIndex(0);
							}

							if (key.equals("FK_Unit")) {
								//  val = WebUser.FK_Unit;
								//ddl.SelectedIndex = 0;
								ddl.SetSelectItemByIndex(0);
							}
						}
						else {
							continue;
						}
					}
					else {
						instr = instr.replace("..", ".");
						instr = instr.replace(".", "','");
						instr = instr.substring(2);
						instr = instr.substring(0, instr.length() - 2);
						where += " " + key + " IN (" + instr + ")  AND ";
						continue;
					}
				}

				if (key.equals("FK_Dept")) {
					if (val.length() == 8) {
						where += " FK_Dept =" + SystemConfig.getAppCenterDBVarStr() + "V_Dept    AND ";
					}
					else {
						switch (SystemConfig.getAppCenterDBType()) {
							case Oracle:
							case Informix:
								where += " FK_Dept LIKE '%'||:V_Dept||'%'   AND ";
								break;
							case MSSQL:
							default:
								where += " FK_Dept LIKE  " + SystemConfig.getAppCenterDBVarStr() + "V_Dept+'%'   AND ";
								break;
						}
					}
					myps.Add("V_Dept", val);
				}
				else {
					where += " " + key + " =" + SystemConfig.getAppCenterDBVarStr() + key + "   AND ";
					if (!key.equals("FK_NY")) {
						whereOfLJ += " " + key + " =" + SystemConfig.getAppCenterDBVarStr() + key + "   AND ";
					}

					myps.Add(key, val);
				}
			}
			try {
				String key = this.toolBar1.GetTBByID("TB_Key").getText().trim();
				if (key.length() > 1) {
					String whereLike = "";

					boolean isAddAnd = false;
					for (Attr likeKey : attrs) {
						if (likeKey.getIsNum()) {
							continue;
						}
						if (likeKey.getIsRefAttr()) {
							continue;
						}

	
//						switch (likeKey.Field)
	//ORIGINAL LINE: case "MyFileExt":
						if (likeKey.getField().equals("MyFileExt") || likeKey.getField().equals("MyFilePath") || likeKey.getField().equals("WebPath")) {
								continue;
						}
						else {
						}


						if (isAddAnd == false) {
							isAddAnd = true;
							whereLike += "      " + likeKey.getField() + " LIKE '%" + key + "%' ";
						}
						else {
							whereLike += "   AND   " + likeKey.getField() + " LIKE '%" + key + "%'";
						}
					}
					whereLike += "          ";
					where += whereLike;
				}
			}
			catch (java.lang.Exception e) {
			}
			if (map.DTSearchWay != DTSearchWay.None) {
				String dtFrom = this.toolBar1.GetTBByID("TB_S_From").getText().trim();
				String dtTo = this.toolBar1.GetTBByID("TB_S_To").getText().trim();
				String field = map.DTSearchKey;
				if (map.DTSearchWay == DTSearchWay.ByDate) {
					where += "( " + field + ">='" + dtFrom + " 01:01' AND " + field + "<='" + dtTo + " 23:59')     ";
				}
				else {
					where += "(";
					where += field + " >='" + dtFrom + "' AND " + field + "<='" + dtTo + "'";
					where += ")";
				}
			}
			if (where.equals(" WHERE ")) {
				where = "" + Condition.replace("and", "");
				whereOfLJ = "" + Condition.replace("and", "");
			}
			else {
				where = where.substring(0, where.length() - (new String(" AND ")).length()) + Condition;
				whereOfLJ = whereOfLJ.substring(0, whereOfLJ.length() - (new String(" AND ")).length()) + Condition;
			}
			sql += where + " GROUP BY  " + attrD1.getKey() + "," + attrD2.getKey();
			myps.SQL = sql;
			DataTable dt = DBAccess.RunSQLReturnTable(myps);

			String leftMsg = this.getDDL_FXWay().getSelectedItem().getText() + ":" + this.getDDL_Num().getSelectedItem().getText();
			this.Right.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
			this.Right.append(AddTR());
			this.Right.append(AddTDGroupTitle(leftMsg));
			if (sesD1 != null) {
				for (SysEnum se : sesD1.ToJavaList()) {
					this.Right.append(AddTDGroupTitle(se.getLab()));
				}
			}
			if (ensD1 != null) {
				if (ensD1.size() == 0) {
					ensD1.RetrieveAll();
				}
				for (Entity en : ensD1.ToJavaListEn()) {
					this.Right.append(AddTDGroupTitle(en.GetValStrByKey("Name")));
				}
			}
			this.Right.append(AddTREnd());
			if (sesD2 != null) {
				for (SysEnum se : sesD2.ToJavaList()) {
					this.Right.append(AddTR());
					this.Right.append(AddTDGroupTitle(se.getLab()));

					if (sesD1 != null) {
						for (SysEnum seD1 :  sesD1.ToJavaList()) {
							this.Right.append(AddTD("onclick='' ", this.GetIt(dt,String.valueOf( seD1.getIntKey()),String.valueOf(se.getIntKey()))));
						}
					}

					if (ensD1 != null) {
						for (Entity enD1 : ensD1.ToJavaListEn()) {
							this.Right.append(AddTD(this.GetIt(dt, enD1.GetValStrByKey("No"), String.valueOf(se.getIntKey()))));
						}
					}
					this.Right.append(AddTREnd());
				}
			}

			if (ensD2 != null) {
				for (Entity en : ensD2.ToJavaListEn()) {
					this.Right.append(AddTR());
					this.Right.append(AddTDGroupTitle(en.GetValStrByKey("Name")));

					if (sesD1 != null) {
						for (SysEnum seD1 : sesD1.ToJavaList()) {
							this.Right.append(AddTD(this.GetIt(dt, String.valueOf(seD1.getIntKey()), en.GetValStrByKey("No"))));
						}
					}
					if (ensD1 != null) {
						for (Entity enD1 : ensD1.ToJavaListEn()) {
							this.Right.append(AddTD(this.GetIt(dt, enD1.GetValStrByKey("No"), en.GetValStrByKey("No"))));
						}
					}
					this.Right.append(AddTREnd());
				}
			}
			this.Right.append(AddTableEnd());
			StringBuilder paras = null;
			if (this.getCB_IsImg().getChecked()) {
				 paras = new StringBuilder("@IsImg=1");
			}
			else {
				 paras = new StringBuilder("@IsImg=0");
			}

			if (this.getCB_IsRate().getChecked()) {
				paras.append("@IsRate=1");
			}
			else {
				paras.append("@IsRate=0");
			}

			if (this.getCB_IsNull().getChecked()) {
				paras.append("@IsNull=1");
			}
			else {
				paras.append("@IsNull=0");
			}

			paras.append("@Num=" + this.getDDL_Num().getSelectedItemStringVal());
			paras.append("@FXWay=" + this.getDDL_FXWay().getSelectedItemStringVal());
			paras.append("@D1=" + this.getDDL_D1().getSelectedItemStringVal());
			paras.append("@D1_Order=" + this.getDDL_D1_Order().getSelectedItemStringVal());
			paras.append("@D2=" + this.getDDL_D2().getSelectedItemStringVal());
			paras.append("@D2_Order=" + this.getDDL_D2_Order().getSelectedItemStringVal());
			paras.append("@W=" + getParameter("TB_W"));
			paras.append("@H=" + getParameter("TB_H"));

			ur.setCfgKey(this.getRptNo() + "_D3");
			ur.setMyPK( WebUser.getNo() + "_" + ur.getCfgKey());
			ur.setFK_Emp(WebUser.getNoOfSessionID());
			ur.setParas(paras.toString());
			ur.Save();
			return null;
		}
		public final java.math.BigDecimal GetIt(DataTable dt, String d1, String d2) {
			for (DataRow dr : dt.Rows) {
				if (dr.getValue(0).toString().equals(d1) && dr.getValue(1).toString().equals(d2)) {
					java.math.BigDecimal bs=new BigDecimal(dr.getValue(2).toString());
					return bs;
				}
			}
			return BigDecimal.valueOf(0);
		}

	
	}

