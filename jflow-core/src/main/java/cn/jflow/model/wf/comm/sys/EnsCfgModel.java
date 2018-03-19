package cn.jflow.model.wf.comm.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.UIContralType;

public class EnsCfgModel extends BaseModel {

	private StringBuffer ucSys1;
	
	public StringBuffer getUcSys1() {
		return ucSys1;
	}

	public EnsCfgModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		ucSys1 = new StringBuffer();
	}

	public String getEnsName() {
		return this.get_request().getParameter("EnsName");
	}

	public void pageLoad() {
		Entity en = BP.En.ClassFactory.GetEn(this.getEnsName());
		ucSys1.append(AddTable());
		ucSys1.append(AddCaptionLeft(en.getEnDesc() + ":数据分析属性设置"));
		ucSys1.append(AddTR());
		ucSys1.append(AddTDTitle("属性"));
		ucSys1.append(AddTDTitle("中文名称"));
		ucSys1.append(AddTDTitle("可使用的分析方式"));
		ucSys1.append(AddTREnd());

		Map map = en.getEnMap();
		// boolean is1 = false;
		Attrs attrs = map.getAttrs();
		for (Attr attr : attrs) {
			if (attr.getIsNum() == false) {
				continue;
			}
			if (attr.getIsPK()) {
				continue;
			}
			if (attr.getUIContralType() == UIContralType.TB == false) {
				continue;
			}
			if (attr.getUIVisible() == false) {
				continue;
			}
			if (attr.getMyFieldType() == FieldType.FK) {
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum) {
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum) {
				continue;
			}
			if (attr.getKey().equals("OID") || attr.getKey().equals("WorkID")
					|| attr.getKey().equals("MID")
					|| attr.getKey().equals("FID")) {
				continue;
			}

			// is1 = ucSys1.append(AddTR(is1));
			ucSys1.append(AddTD(attr.getKey()));
			ucSys1.append(AddTD(attr.getDesc()));
			ucSys1.append(AddTDBegin());
			CheckBox cb = new CheckBox();
			cb.setId(attr.getKey() + "_AVG");
			cb.setText("求和");
			ucSys1.append(cb);

			cb = new CheckBox();
			cb.setId(attr.getKey() + "_SUM");
			cb.setText("求平均");
			ucSys1.append(cb);

			cb = new CheckBox();
			cb.setId(attr.getKey() + "_AMOUNT");
			cb.setText("求累计");
			ucSys1.append(cb);

			cb = new CheckBox();
			cb.setId(attr.getKey() + "_MAX");
			cb.setText("求最大");
			ucSys1.append(cb);

			cb = new CheckBox();
			cb.setId(attr.getKey() + "_MIN");
			cb.setText("求最小");
			ucSys1.append(cb);

			cb = new CheckBox();
			cb.setId(attr.getKey() + "_LSXS");
			cb.setText("离散系数");
			ucSys1.append(cb);

			ucSys1.append(AddTDEnd());
			ucSys1.append(AddTREnd());
		}
		ucSys1.append(AddTRSum());

		ucSys1.append(AddTD());
		ucSys1.append(AddTD());

		Button btn = new Button();
		btn.setText("保存设置");
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "Btn_Save");
		ucSys1.append(AddTD(btn));

		ucSys1.append(AddTREnd());
		ucSys1.append(AddTableEnd());
	}

	// private void btn_Click(Object sender, EventArgs e)
	// {
	// Entity en = BP.En.ClassFactory.GetEn(this.getEnsName());
	// Map map = en.EnMap;
	// String keys = "";
	// for (Attr attr : map.Attrs)
	// {
	// if (attr.IsNum == false)
	// {
	// continue;
	// }
	// if (attr.IsPK)
	// {
	// continue;
	// }
	// if (attr.UIContralType == UIContralType.TB == false)
	// {
	// continue;
	// }
	// if (attr.UIVisible == false)
	// {
	// continue;
	// }
	// if (attr.MyFieldType == FieldType.FK)
	// {
	// continue;
	// }
	// if (attr.MyFieldType == FieldType.Enum)
	// {
	// continue;
	// }
	// if (attr.MyFieldType == FieldType.Enum)
	// {
	// continue;
	// }
	// if (attr.getKey().equals("OID") || attr.getKey().equals("WorkID") ||
	// attr.getKey().equals("MID") || attr.getKey().equals("FID"))
	// {
	// continue;
	// }
	//
	//
	// String strs = "@" + attr.getKey() + "=";
	// CheckBox cb = ucSys1.append(GetCBByID(attr.getKey() + "_SUM");
	// if (cb.Checked)
	// {
	// strs += ".SUM.";
	// }
	//
	// cb = ucSys1.append(GetCBByID(attr.getKey() + "_AVG");
	// if (cb.Checked)
	// {
	// strs += ".AVG.";
	// }
	//
	// cb = ucSys1.append(GetCBByID(attr.getKey() + "_AMOUNT");
	// if (cb.Checked)
	// {
	// strs += ".AMOUNT.";
	// }
	//
	// cb = ucSys1.append(GetCBByID(attr.getKey() + "_MAX");
	// if (cb.Checked)
	// {
	// strs += ".MAX.";
	// }
	//
	// cb = ucSys1.append(GetCBByID(attr.getKey() + "_MIN");
	// if (cb.Checked)
	// {
	// strs += ".MIN.";
	// }
	//
	// cb = ucSys1.append(GetCBByID(attr.getKey() + "_LSXS"));
	// if (cb.Checked)
	// {
	// strs += ".LSXS.";
	// }
	// keys += strs;
	// }
	//
	// BP.Sys.EnCfg cfg = new BP.Sys.EnCfg();
	// cfg.No = this.getEnsName();
	// if (cfg.RetrieveFromDBSources() == 0)
	// {
	// cfg.Datan = keys;
	// cfg.Insert();
	// }
	// else
	// {
	// cfg.Datan = keys;
	// cfg.Update();
	// }
	// cfg.Retrieve();
	// cfg.Datan = keys;
	// this.Alert("设置成功。");
	// }

}
