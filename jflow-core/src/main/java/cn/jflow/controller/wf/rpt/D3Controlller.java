package cn.jflow.controller.wf.rpt;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
import BP.Sys.DTSearchWay;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.ToolBar;

@Controller
@RequestMapping("/WF/Rpt")
public class D3Controlller {
	
	private ToolBar toolBar1;
	public HashMap<String, BaseWebControl> ctls;
	public StringBuilder Left=new StringBuilder();
	public StringBuilder Right = new StringBuilder();
	public Entity currEn=null;
	private UserRegedit ur = null;
	
	
	@RequestMapping(value ="/ddl_SelectedIndexChanged_GoTo", method = RequestMethod.POST)
	public void ddl_SelectedIndexChanged_GoTo(HttpServletRequest request,HttpServletResponse response) {
		String rptNo = request.getParameter("RptNo");
		String Fk_Flow=request.getParameter("FK_Flow");
		Object sender = null;
		DDL ddl = (DDL)((sender instanceof DDL) ? sender : null);
		String item = ddl.getSelectedItemStringVal();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMddhhmmss" );
		String tKey = sdf.format(new java.util.Date());
		try {
			response.sendRedirect(item + ".jsp?RptNo=" +rptNo + "&FK_Flow=" + Fk_Flow + "&T=" + tKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/cb_CheckedChanged", method = RequestMethod.POST)
	public void cb_CheckedChanged(HttpServletRequest request,HttpServletResponse response) {
		this.BindDG(request);
	}
	@RequestMapping(value = "/ddl_SelectedIndexChanged", method = RequestMethod.POST)
	public void ddl_SelectedIndexChanged(HttpServletRequest request,HttpServletResponse response) {
		this.BindDG(request);
	}
	@RequestMapping(value ="/ToolBar1_ButtonClick", method = RequestMethod.POST)
	public void ToolBar1_ButtonClick(HttpServletRequest request,HttpServletResponse response) {
		String rptNo = request.getParameter("RptNo");
		String Fk_Flow=request.getParameter("FK_Flow");
		String btnName = request.getParameter("btnName");
		//switch (btn.ID) {
		switch(NamesOfBtn.getEnumByCode(btnName)){
			case Help:
				break;
			case Excel:
				DataTable dt = this.BindDG(request);
				return;
			default:
				//this.toolBar1.SaveSearchState(rptNo, Fk_Flow);
				this.BindDG(request);
				return;
		}
	}
	
	public  String getRptNo(HttpServletRequest request) {
		//String s = this.Request.QueryString["RptNo"];
		String s = request.getParameter("RptNo");
		if(StringHelper.isNullOrEmpty(s)) {
			return "ND68MyRpt";
		}
		return s;
	}
	public final  DataTable BindDG(HttpServletRequest request) {
		this.ur = new UserRegedit(WebUser.getNo(), this.getRptNo(request) + "_D3");
		this.currEn = this.getHisEns(request).getGetNewEntity();
		//处理数据源是否正确.
		ctls = HtmlUtils.httpParser(Left.toString(), false);
		if (this.getDDL_D1().Items.size() <= 1 || this.getDDL_Num().Items.size() == 0) {
			this.Right.setLength(0);
			this.toolBar1.setIsVisible(false);
			//this.Right.append(AddMsgGreen("提示:", "<h2>没有足够的纬度或者没有数据分析项目。</h2>"));
		this.Right.append(BaseModel.AddMsgOfInfo("提示:", "<h2>没有足够的纬度或者没有数据分析项目。</h2>"));
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
				sUr.setMyPK(WebUser.getNo() + this.getRptNo(request) + "_SearchAttrs");
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


//					switch (likeKey.Field)
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
		this.Right.append(BaseModel.AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
		this.Right.append(BaseModel.AddTR());
		this.Right.append(BaseModel.AddTDGroupTitle(leftMsg));
		if (sesD1 != null) {
			for (SysEnum se : sesD1.ToJavaList()) {
				this.Right.append(BaseModel.AddTDGroupTitle(se.getLab()));
			}
		}
		if (ensD1 != null) {
			if (ensD1.size() == 0) {
				ensD1.RetrieveAll();
			}
			for (Entity en : ensD1.ToJavaListEn()) {
				this.Right.append(BaseModel.AddTDGroupTitle(en.GetValStrByKey("Name")));
			}
		}
		this.Right.append(BaseModel.AddTREnd());
		if (sesD2 != null) {
			for (SysEnum se : sesD2.ToJavaList()) {
				this.Right.append(BaseModel.AddTR());
				this.Right.append(BaseModel.AddTDGroupTitle(se.getLab()));

				if (sesD1 != null) {
					for (SysEnum seD1 :  sesD1.ToJavaList()) {
						this.Right.append(BaseModel.AddTD("onclick='' ", this.GetIt(dt,String.valueOf( seD1.getIntKey()),String.valueOf(se.getIntKey()))));
					}
				}

				if (ensD1 != null) {
					for (Entity enD1 : ensD1.ToJavaListEn()) {
						this.Right.append(BaseModel.AddTD(this.GetIt(dt, enD1.GetValStrByKey("No"), String.valueOf(se.getIntKey()))));
					}
				}
				this.Right.append(BaseModel.AddTREnd());
			}
		}

		if (ensD2 != null) {
			for (Entity en : ensD2.ToJavaListEn()) {
				this.Right.append(BaseModel.AddTR());
				this.Right.append(BaseModel.AddTDGroupTitle(en.GetValStrByKey("Name")));

				if (sesD1 != null) {
					for (SysEnum seD1 : sesD1.ToJavaList()) {
						this.Right.append(BaseModel.AddTD(this.GetIt(dt, String.valueOf(seD1.getIntKey()), en.GetValStrByKey("No"))));
					}
				}
				if (ensD1 != null) {
					for (Entity enD1 : ensD1.ToJavaListEn()) {
						this.Right.append(BaseModel.AddTD(this.GetIt(dt, enD1.GetValStrByKey("No"), en.GetValStrByKey("No"))));
					}
				}
				this.Right.append(BaseModel.AddTREnd());
			}
		}
		this.Right.append(BaseModel.AddTableEnd());
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
		paras.append("@W=" + request.getParameter("TB_W"));
		paras.append("@H=" + request.getParameter("TB_H"));

		ur.setCfgKey(this.getRptNo(request) + "_D3");
		ur.setMyPK( WebUser.getNo() + "_" + ur.getCfgKey());
		ur.setFK_Emp(WebUser.getNoOfSessionID());
		ur.setParas(paras.toString());
		ur.Save();
		return null;
	
		
	}
	public final DDL getDDL_D1() {
	//	return this.Left.GetDDLByID("DDL_D1");
		 return (DDL) ctls.get("DDL_D1");
	}
	public final DDL getDDL_Num() {
		//return this.Left.GetDDLByID("DDL_Num");
		 return (DDL) ctls.get("DDL_Num");
	}
	public final DDL getDDL_D2() {
		//return this.Left.GetDDLByID("DDL_D2");
		 return (DDL) ctls.get("DDL_D2");
	}
	public Entities _HisEns = null;
	public final Entities getHisEns(HttpServletRequest request) {
		if (_HisEns == null) {
			if (this.getRptNo(request) != null) {
				if (this._HisEns == null) {
					_HisEns = BP.En.ClassFactory.GetEns(this.getRptNo(request));
				}
			}
		}
		return _HisEns;
	}
	public final DDL getDDL_FXWay() {
		//return this.Left.GetDDLByID("DDL_FXWay");
		 return (DDL) ctls.get("DDL_FXWay");
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
	public final CheckBox getCB_IsImg() {
		//return this.Left.GetCBByID("CB_IsImg");
	    return (CheckBox) ctls.get("CB_IsImg");
	}

	public final CheckBox getCB_IsNull() {
		//return this.Left.GetCBByID("CB_IsNull");
		 return (CheckBox) ctls.get("CB_IsNull");
	}

	public final CheckBox getCB_IsRate() {
		//return this.Left.GetCBByID("CB_IsRate");
		 return (CheckBox) ctls.get("CB_IsRate");
	}
	public final DDL getDDL_D1_Order() {
		//return this.Left.GetDDLByID("DDL_D1_Order");
		 return (DDL) ctls.get("DDL_D1_Order");
	}
	public final DDL getDDL_D2_Order() {
		//return this.Left.GetDDLByID("DDL_D2_Order");
		 return (DDL) ctls.get("DDL_D2_Order");
	}

}
