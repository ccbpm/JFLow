package cn.jflow.model.wf.rpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.UserRegedit;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;

public class SelectMValModel {

	private HttpServletRequest _request = null;

	public UiFatory Pub1 = null;

	public SelectMValModel(HttpServletRequest request,
			HttpServletResponse response, String basePath) {
		this._request = request;

		this.Pub1 = new UiFatory();
	}

	public final String getMyPK() {
		return WebUser.getNo() + this.getEnsName() + "_SearchAttrs";
	}

	public final String getAttrKey() {
		if (_request.getParameter("AttrKey") == null)
			return "";
		return _request.getParameter("AttrKey");
	}

	public final String getFK_Flow() {
		if (_request.getParameter("FK_Flow") == null)
			return "";
		return _request.getParameter("FK_Flow");
	}

	public final String getEnName() {
		if (_request.getParameter("EnName") == null)
			return "";
		return _request.getParameter("EnName");
	}

	public final String getEnsName() {
		if (_request.getParameter("EnsName") == null)
			return "";
		return _request.getParameter("EnsName");
	}

	public Entity getHisEn() {
		return this.getHisEns().getGetNewEntity();
	}

	public Entities getHisEns() {
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		return ens;
	}

	public void init() {
		try {
			UserRegedit ur = new UserRegedit();
			ur.setMyPK(this.getMyPK());
			if (ur.RetrieveFromDBSources() == 0) {
				ur.setMyPK(this.getMyPK());
				ur.setFK_Emp(WebUser.getNo());
				ur.setCfgKey(this.getEnsName() + "_SearchAttrs");
				ur.Insert();
			}

			Entity en = this.getHisEn();
			Attr attr = en.getEnMap().GetAttrByKey(this.getAttrKey());
			String cfgVal = ur.getMVals();
			cfgVal = StringHelper.isEmpty(cfgVal, "");

			AtPara ap = new AtPara(cfgVal);
			cfgVal = ap.GetValStrByKey(this.getAttrKey());
			cfgVal = StringHelper.isEmpty(cfgVal, "");

			if (attr.getIsEnum()) {
				this.BindEnum(ur, attr, cfgVal);
			} else {
				this.BindEns(ur, attr, cfgVal);
			}
		} catch (Exception e) {
			this.Pub1.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}

	public void BindEnum(UserRegedit ur, Attr attr, String cfgVal) {
		this.Pub1.append(BaseModel.AddTable());
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("IDX"));
		this.Pub1
				.append(BaseModel
						.AddTDTitle("<input type=checkbox text='选择全部' name=checkedAll onclick='SelectAll()' >选择全部"));
		this.Pub1.append(BaseModel.AddTREnd());

		SysEnums ses = new SysEnums(attr.getUIBindKey());
		int idx = 0;
		boolean is1 = false;
		for (SysEnum item : ses.ToJavaList()) {
			idx++;
			this.Pub1.append(BaseModel.AddTR(is1));
			is1 = !is1;
			this.Pub1.append(BaseModel.AddTDIdx(idx));

			CheckBox cb = this.Pub1.creatCheckBox("CB_" + item.getIntKey());
			cb.setText(item.getLab());

			cb.setChecked(cfgVal.contains(String.valueOf(item.getIntKey())));

			this.Pub1.append("\n<TD nowrap = 'nowrap'>");
			this.Pub1.append(cb);
			this.Pub1.append("</TD>");
			this.Pub1.append(BaseModel.AddTREnd());
		}
		this.Pub1.append(BaseModel.AddTableEndWithHR());

		Button btn = this.Pub1.creatButton("Btn_Save1");
		btn.setText(" OK ");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "onSave();");
		this.Pub1.append(btn);
	}

	public void BindEns(UserRegedit ur, Attr attr, String cfgVal) {
		int idx = 0;
		boolean is1 = false;

		Button btn = this.Pub1.creatButton("Btn_Save");
		btn.setText(" OK ");
		btn.addAttr("onclick", "onSave();");
		btn.setCssClass("Btn");

		this.Pub1.append(BaseModel.AddTable("width='90%'"));
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("IDX"));
		this.Pub1
				.append(BaseModel
						.AddTDTitle("<input type=checkbox   text='选择全部' name=checkedAll onclick='SelectAll()' >选择全部"));
		this.Pub1.append(BaseModel.AddTREnd());
		if (attr.getUIBindKey().equals("BP.Port.Depts")) {
			DataTable dt;
			if (WebUser.getNo().equals("admin")) {
				dt = DBAccess
						.RunSQLReturnTable("SELECT No,Name FROM Port_Dept ");
			} else {
				dt = DBAccess
						.RunSQLReturnTable("SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='"
								+ WebUser.getNo()
								+ "'  AND FK_Flow='"
								+ this.getFK_Flow() + "')");
			}

			for (DataRow dr : dt.Rows) {
				idx++;
				this.Pub1.append(BaseModel.AddTR(is1));
				is1 = !is1;
				this.Pub1.append(BaseModel.AddTDIdx(idx));

				CheckBox cb = this.Pub1.creatCheckBox("CB_"
						+ dr.getValue(0).toString());
				cb.setText(dr.getValue(1).toString());
				cb.setChecked(cfgVal.contains("." + dr.getValue(0).toString()
						+ "."));

				this.Pub1.append(BaseModel.AddTD(cb));
				this.Pub1.append(BaseModel.AddTREnd());
			}

			this.Pub1.append(BaseModel.AddTableEndWithHR());
			this.Pub1.append(btn);
			return;
		}
		
		Entities ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
		ens.RetrieveAll();
		
		for (Entity item : ens.ToJavaListEn()) {
			idx++;
			this.Pub1.append(BaseModel.AddTR(is1));
			is1 = !is1;
			this.Pub1.append(BaseModel.AddTDIdx(idx));

			CheckBox cb = this.Pub1.creatCheckBox("CB_"
					+ item.GetValByKey(attr.getUIRefKeyValue()));
			cb.setText(item.GetValStrByKey(attr.getUIRefKeyText()));
			cb.setChecked(cfgVal.contains("."+item.GetValStrByKey(attr
					.getUIRefKeyValue()+".")));

			this.Pub1.append(BaseModel.AddTD(cb));
			this.Pub1.append(BaseModel.AddTREnd());
		}
		
		this.Pub1.append(BaseModel.AddTableEndWithHR());
		this.Pub1.append(btn);
	}
}
