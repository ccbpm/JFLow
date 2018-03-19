package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.Button;
import BP.WF.*;
import BP.En.*;
import BP.DA.*;
import BP.Sys.*;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;

public class FrmDtlModel extends EnModel {

	public Button Btn_Save;

	public FrmDtlModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	// /#start 属性
	public final int getFK_Node() {
		try {
			String fK_Node = getParameter("FK_Node");
			return Integer.parseInt(fK_Node);
		} catch (java.lang.Exception e) {
			return 101;
		}
	}

	public final int getOID() {
		try {
			String oID = getParameter("OID");
			return Integer.parseInt(oID);
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public final String getFK_MapData() {
		String s = getParameter("FK_MapData");
		if (s == null) {
			return "ND101";
		}
		return s;
	}

	public final boolean getIsReadonly() {
		String isReadonly = getParameter("IsReadonly");
		if (isReadonly.equals("1")) {
			return true;
		}
		return false;
	}

	// /#end 属性

	public void Page_Load() {
		MapDtl dtl = new MapDtl(this.getFK_MapData());
		GEDtl dtlEn = dtl.getHisGEDtl();
		dtlEn.SetValByKey("OID", this.getOID());
		dtlEn.RetrieveFromDBSources();
		

		MapAttrs mattrs = new MapAttrs(dtl.getNo());
		for (MapAttr mattr : mattrs.ToJavaList()) {
			if (mattr.getDefValReal().contains("@") == false) {
				continue;
			}
			dtlEn.SetValByKey(mattr.getKeyOfEn(), mattr.getDefVal());
		}

		// this.Btn_Save.UseSubmitBehavior = false;
		// this.Btn_Save.OnClientClick = "this.disabled=true;";
		// //this.disabled='disabled'; return true;";

		BindCCForm(dtlEn, this.getFK_MapData(), this.getIsReadonly(), 500,true);

		Btn_Save = new Button();
		Btn_Save.setId("Btn_Save");
		Btn_Save.setName("Btn_Save");
		Btn_Save.setText("保存");
		Btn_Save.setVisible(false);
		Btn_Save.addAttr("onclick", "Save()");

		if (this.getIsReadonly()) {
			this.Btn_Save.setVisible(false);
			this.Btn_Save.setEnabled(false);
		} else {
			this.Btn_Save.setVisible(true);
			this.Btn_Save.setEnabled(true);
		}
	}

	/*
	 * public final void Btn_Save_Click() { try {
	 * 
	 * MapDtl dtl = new MapDtl(this.getFK_MapData()); GEDtl dtlEn =
	 * dtl.getHisGEDtl(); dtlEn.SetValByKey("OID", this.getOID()); int i =
	 * dtlEn.RetrieveFromDBSources(); Object tempVar = this.UCEn1.Copy(dtlEn);
	 * dtlEn = (GEDtl)((tempVar instanceof GEDtl) ? tempVar : null);
	 * dtlEn.SetValByKey(GEDtlAttr.RefPK, this.getWorkID());
	 * 
	 * if (i == 0) { dtlEn.setOID(0); dtlEn.Insert(); } else { dtlEn.Update(); }
	 * 
	 * this.Response.Redirect("FrmDtl.jsp?WorkID=" + dtlEn.getRefPK() +
	 * "&FK_MapData=" + this.getFK_MapData() + "&IsReadonly=" +
	 * this.getIsReadonly() + "&OID=" + dtlEn.getOID(), true);
	 * 
	 * //if (fes.Contains(FrmEventAttr.FK_Event, FrmEventList.SaveAfter) == true
	 * // || fes.Contains(FrmEventAttr.FK_Event, FrmEventList.SaveBefore) ==
	 * true) //{ // /*如果包含保存
	 */
	// //
	// /FrmDtl.aspx?FK_MapData=ND11699Dtl1&WorkID=2078&OID=7365&IsReadonly=False
	// this.Response.Redirect(this.Request.RawUrl, true);
	// //this.Response.Redirect("FrmDtl.aspx?WorkID=" + this.WorkID +
	// "&FK_MapData=" + this.FK_MapData + "&IsReadonly="+this.IsReadonly, true);
	// }
	/*
	 * } catch (RuntimeException ex) { this.UCEn1.AddMsgOfWarning("error:",
	 * ex.getMessage()); } }
	 */
}
