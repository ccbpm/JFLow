package cn.jflow.system.uc;

import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;

public class UCSys {

	public static void UIEn1ToM_OneLine(Entities ens, String showVal, String showText,
			Entities selectedEns, String selecteVal, UiFatory ui, UiFatory UCSys) {
		UCSys.append("<table border=0 width='500px'>");
		boolean is1 = false;
		for (Entity en : ens.ToJavaListEn()) {
			UCSys.append(BaseModel.AddTR(is1));
			is1 = !is1;

			CheckBox cb = ui.creatCheckBox("CB_" + en.GetValStrByKey(showVal));
			cb.setText(en.GetValStringByKey(showText));

			UCSys.append("\n<TD nowrap = 'nowrap'>");
			UCSys.append(cb);
			UCSys.append("</TD>");
			// this.UCSys.append(BaseModel.AddTD(cb));
			UCSys.append(BaseModel.AddTREnd());
		}
		UCSys.append(BaseModel.AddTableEnd());

		// 设置选择的 ens .
		for (Entity en : selectedEns.ToJavaListEn()) {
			String key = en.GetValStrByKey(selecteVal);
			CheckBox bp = (CheckBox) ui.GetUIByID("CB_" + key);
			bp.setChecked(true);
		}
	}

	public static void UIEn1ToM(Entities ens, String showVal, String showText,
			Entities selectedEns, String selecteVal, UiFatory ui, UiFatory UCSys) {
		UCSys.append(BaseModel
				.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
		int i = 0;
		boolean is1 = false;
		for (Entity en : ens.ToJavaListEn()) {
			i++;
			if (i == 4)
				i = 1;

			if (i == 1) {
				UCSys.append(BaseModel.AddTR(is1));
				is1 = !is1;
			}

			CheckBox cb = ui.creatCheckBox("CB_"
					+ en.GetValStringByKey(showVal));
			cb.setText(en.GetValStringByKey(showText));

			UCSys.append("\n<TD nowrap = 'nowrap'>");
			UCSys.append(cb);
			UCSys.append("</TD>");
			if (i == 3)
				UCSys.append(BaseModel.AddTREnd());
		}

		switch (i) {
		case 1:
			UCSys.append(BaseModel.AddTD());
			UCSys.append(BaseModel.AddTD());// "<TD>&nbsp;</TD>");
			UCSys.append(BaseModel.AddTREnd());// ("</TR>");
			break;
		case 2:
			UCSys.append(BaseModel.AddTD());
			UCSys.append(BaseModel.AddTREnd());
			break;
		default:
			break;
		}
		UCSys.append(BaseModel.AddTableEnd());

		// 设置选择的 ens .
		for (Entity en : selectedEns.ToJavaListEn()) {
			String key = en.GetValStringByKey(selecteVal);
			try {
				CheckBox bp = (CheckBox) ui.GetUIByID("CB_" + key);
				bp.setChecked(true);
			} catch (Exception e) {
			}
		}
	}

	public static void UIEn1ToMGroupKey_Line(Entities ens, String showVal,
			String showText, Entities selectedEns, String selecteVal,
			String groupKey, UiFatory ui, UiFatory UCSys) {
		UCSys.append(BaseModel
				.AddTable("<TABLE class='Table' cellSpacing='1' cellPadding='1'  border='1' width='100%' >"));

		Attr attr = ens.getGetNewEntity().getEnMap().GetAttrByKey(groupKey);
		String val = null;
		Entity seEn = null;
		if (attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum) { // 检查是否是 enum
																// 类型。
			SysEnums eens = new SysEnums(attr.getKey());
			for (SysEnum se : eens.ToJavaList()) {
				UCSys.append(BaseModel.AddTR());
				UCSys.append("<TD class='GroupTitle' >" + se.getLab() + "</TD>");
				UCSys.append(BaseModel.AddTREnd());
				for (Entity en : ens.ToJavaListEn()) {
					if (en.GetValIntByKey(attr.getKey()) != se.getIntKey())
						continue;

					UCSys.append(BaseModel.AddTR());
					val = en.GetValStrByKey(showVal);
					CheckBox cb = ui.creatCheckBox("CB_" + val + "_"
							+ se.getIntKey());// edited by liuxc,2015.1.6

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					cb.setText(en.GetValStrByKey(showText));
					UCSys.append("\n<TD nowrap = 'nowrap'>");
					UCSys.append(cb);
					UCSys.append("</TD>");
					// this.UCSys.append(BaseModel.AddTD(cb));
					UCSys.append(BaseModel.AddTREnd());
				}
			}
		} else {
			Entities groupEns = ClassFactory.GetEns(attr.getUIBindKey());
			groupEns.RetrieveAll();
			String gVal = null;
			for (Entity group : groupEns.ToJavaListEn()) {
				gVal = group.GetValStringByKey(attr.getUIRefKeyValue());
				UCSys.append("<TR>");
				UCSys.append("<TD class='GroupTitle' >"
						+ group.GetValStringByKey(attr.getUIRefKeyText())
						+ "</TD>");
				UCSys.append(BaseModel.AddTREnd());

				for (Entity en : ens.ToJavaListEn()) {
					if (!(en.GetValStringByKey(attr.getKey())).equals(gVal))
						continue;

					UCSys.append("<TR>");
					val = en.GetValStringByKey(showVal);
					CheckBox cb = ui.creatCheckBox("CB_" + val + "_" + gVal); // edited
																				// by
																				// liuxc,2015.1.6
					cb.setText(en.GetValStringByKey(showText));

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					UCSys.append("<TD nowrap = 'nowrap'>");
					UCSys.append(cb);
					UCSys.append("</TD>");
					UCSys.append(BaseModel.AddTREnd());
				}
			}
		}
		UCSys.append(BaseModel.AddTableEnd());
	}

	public static void UIEn1ToMGroupKey(Entities ens, String showVal, String showText,
			Entities selectedEns, String selecteVal, String groupKey,
			UiFatory ui, UiFatory UCSys) {
		UCSys.append("<TABLE class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'>");

		String val = null;
		Entity seEn = null;
		Attr attr = ens.getGetNewEntity().getEnMap().GetAttrByKey(groupKey);
		if (attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum) { // 检查是否是 enum 类型

			SysEnums eens = new SysEnums(attr.getKey());
			for (SysEnum se : eens.ToJavaList()) {
				UCSys.append("<TR>");
				UCSys.append("<TD class='GroupTitle' colspan=3 >");

				CheckBox cb1 = ui.creatCheckBox("CB_SE_" + se.getIntKey());
				cb1.setText(se.getLab());
				UCSys.append(cb1);
				UCSys.append("</TD>");
				UCSys.append(BaseModel.AddTREnd());

				int i = 0;
				boolean is1 = false;
				String ctlIDs = "";
				for (Entity en : ens.ToJavaListEn()) {
					if (en.GetValIntByKey(attr.getKey()) != se.getIntKey())
						continue;

					i++;
					if (i == 4)
						i = 1;
					if (i == 1) {
						UCSys.append(BaseModel.AddTR(is1));
						is1 = !is1;
					}

					val = en.GetValStringByKey(showVal);
					CheckBox cb = ui.creatCheckBox("CB_" + val + "_"
							+ se.getIntKey());
					ctlIDs += cb.getId() + ",";

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					cb.setText(en.GetValStringByKey(showText));
					// cb.AccessKey = se.IntKey.ToString();
					UCSys.append("<TD nowrap = 'nowrap'>");
					UCSys.append(cb);
					UCSys.append("</TD>");
					if (i == 3)
						UCSys.append(BaseModel.AddTREnd());
				}

				if (ctlIDs.length() > 0)
					ctlIDs = ctlIDs.substring(0, ctlIDs.length() - 1);
				cb1.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");

				switch (i) {
				case 1:
					UCSys.append(BaseModel.AddTD());
					UCSys.append(BaseModel.AddTD());// "<TD>&nbsp;</TD>");
					UCSys.append(BaseModel.AddTREnd());// ("</TR>");
					break;
				case 2:
					UCSys.append(BaseModel.AddTD());
					UCSys.append(BaseModel.AddTREnd());
					break;
				default:
					break;
				}
			}
		} else {
			Entities groupEns = ClassFactory.GetEns(attr.getUIBindKey());
			groupEns.RetrieveAll();
			for (Entity group : groupEns.ToJavaListEn()) {
				UCSys.append("<TR>");
				UCSys.append("<TD class='GroupTitle' colspan=3>");

				CheckBox cb1 = ui.creatCheckBox("CB_EN_"
						+ group.GetValStrByKey(attr.getUIRefKeyValue()));
				cb1.setText(group.GetValStrByKey(attr.getUIRefKeyText()));
				// cb1.Attributes["onclick"] = "SetSelected(this,'" +
				// group.GetValStringByKey(attr.UIRefKeyValue) + "')";
				UCSys.append(cb1);
				UCSys.append("</TD>");
				UCSys.append(BaseModel.AddTREnd());

				String ctlIDs = "";
				int i = 0;
				String gVal = group.GetValStrByKey(attr.getUIRefKeyValue());
				for (Entity en : ens.ToJavaListEn()) {
					if (!(en.GetValStrByKey(attr.getKey())).equals(gVal))
						continue;
					i++;
					if (i == 4)
						i = 1;
					if (i == 1)
						UCSys.append("<TR>");

					val = en.GetValStringByKey(showVal);
					CheckBox cb = ui.creatCheckBox("CB_" + val + "_" + gVal);// edited
																				// by
																				// liuxc,2015.1.6

					seEn = selectedEns.GetEntityByKey(selecteVal, val);
					if (seEn != null)
						cb.setChecked(true);

					cb.setText(en.GetValStrByKey(showText));
					UCSys.append("<TD nowrap = 'nowrap'>");
					UCSys.append(cb);
					UCSys.append("</TD>");

					ctlIDs += cb.getId() + ",";
					if (i == 3)
						UCSys.append(BaseModel.AddTREnd());
				}

				if (ctlIDs.length() > 0)
					ctlIDs = ctlIDs.substring(0, ctlIDs.length() - 1);
				cb1.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");

				switch (i) {
				case 1:
					UCSys.append(BaseModel.AddTD());
					UCSys.append(BaseModel.AddTD());// "<TD>&nbsp;</TD>");
					UCSys.append(BaseModel.AddTREnd());// ("</TR>");
					break;
				case 2:
					UCSys.append(BaseModel.AddTD());
					UCSys.append(BaseModel.AddTREnd());
					break;
				default:
					break;
				}

			}
		}
		UCSys.append(BaseModel.AddTableEnd());
	}
	
	 private void DataPanelCards(Entities ens, String ctrlId, String showtext1, Boolean showDtl)
     {
		 System.err.println("1122222");
     }
}
