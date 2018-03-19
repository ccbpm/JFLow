package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.FieldTypeS;
import BP.Sys.FrmLab;
import BP.Sys.MapAttr;
import BP.Sys.MapData;
import BP.Sys.SFTable;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import cn.jflow.common.model.TempObject;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/MapDef")
public class ImpTableFieldController {

	@RequestMapping(value = "/ImpTableFieldSave", method = RequestMethod.POST)
	public void ImpTableFieldSave(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, BaseWebControl> ctrlMap = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		String fkMapData = object.getFK_MapData();

		String colname = "";

		MapData md = new MapData();
		md.setNo(fkMapData);
		
		md.RetrieveFromDBSources();

		String msg = "导入字段信息:";
		boolean isLeft = true;
		float maxEnd = md.getMaxEnd(); // 底部.
		for (Map.Entry<String, BaseWebControl> ctrl : ctrlMap.entrySet()) {

			if (StringHelper.isNullOrEmpty(ctrl.getKey())
					|| !ctrl.getKey().startsWith("HID_Idx_")) {
				continue;
			}

			// hid = (HiddenField)((ctrl instanceof HiddenField) ? ctrl : null);

			colname = ctrl.getKey()
					.substring((new String("HID_Idx_")).length());

			MapAttr ma = new MapAttr();
			ma.setKeyOfEn(colname);

			ma.setName(request.getParameter("TB_Desc_" + colname));
			ma.setFK_MapData(fkMapData);
			ma.setMyDataType(Integer.parseInt(request
					.getParameter("DDL_DBType_" + colname)));
			ma.setMaxLen(Integer.parseInt(request.getParameter("TB_Len_"
					+ colname)));
			// ma.LGType =
			// request.getParameter("DDL_LogicType_" + colname);

			ma.setUIBindKey(request.getParameter("TB_BindKey_" + colname));
			ma.setMyPK(fkMapData + "_" + ma.getKeyOfEn());
			ma.setLGType(FieldTypeS.Normal);

			if (!ma.getUIBindKey().equals("")) {
				SysEnums se = new SysEnums();
				se.Retrieve(SysEnumAttr.EnumKey, ma.getUIBindKey());
				if (se.size() > 0) {
					ma.setMyDataType(BP.DA.DataType.AppInt);
					ma.setLGType(BP.En.FieldTypeS.Enum);
					ma.setUIContralType(BP.En.UIContralType.DDL);
				}

				SFTable tb = new SFTable();
				tb.setNo(ma.getUIBindKey());
				if (tb.getIsExits()) {
					ma.setMyDataType(BP.DA.DataType.AppString);
					ma.setLGType(BP.En.FieldTypeS.FK);
					ma.setUIContralType(BP.En.UIContralType.DDL);
				}
			}

			if (ma.getMyDataType() == BP.DA.DataType.AppBoolean) {
				ma.setUIContralType(BP.En.UIContralType.CheckBok);
			}
			if (ma.getIsExits()) {
				continue;
			}
			ma.Insert();

			msg += " <br>字段:" + ma.getKeyOfEn() + "" + ma.getName() + "加入成功.";
			FrmLab lab = null;
			if (isLeft == true) {
				maxEnd = maxEnd + 40;
				// 是否是左边
				lab = new FrmLab();
				lab.setMyPK(BP.DA.DBAccess.GenerGUID());
				lab.setFK_MapData(fkMapData);
				lab.setText(ma.getName());
				lab.setX(40);
				lab.setY(maxEnd);
				lab.Insert();

				ma.setX(lab.getX() + 80);
				ma.setY(maxEnd);
				ma.Update();
			} else {
				lab = new FrmLab();
				lab.setMyPK(BP.DA.DBAccess.GenerGUID());
				lab.setFK_MapData(fkMapData);
				lab.setText(ma.getName());
				lab.setX(350);
				lab.setY(maxEnd);
				lab.Insert();

				ma.setX(lab.getX() + 80);
				ma.setY(maxEnd);
				ma.Update();
			}
			isLeft = !isLeft;
		}

		// 重新设置.
		md.ResetMaxMinXY();
		Glo.ToMsg(msg, response);

		// PubClass.WinClose("OK");
	}

	@RequestMapping(value = "/ImpTableFieldNext", method = RequestMethod.POST)
	public void ImpTableFieldNext(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, BaseWebControl> ctrlMap = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		String selectedColumns = "";
		for (Map.Entry<String, BaseWebControl> ctrl : ctrlMap.entrySet()) {
			if (!ctrl.getKey().contains("CB_")) {
				continue;
			}
			CheckBox checkBox = (CheckBox) ctrl.getValue();

			if (ctrl.getKey().equals("CB_CheckAll") || !checkBox.getChecked()) {
				continue;
			}

			selectedColumns += ctrl.getKey().substring(
					(new String("CB_Col_")).length())
					+ ",";
		}

		String tempVar = request.getParameter("STable");
		try {
			response.sendRedirect(String
					.format("%1$s?Step=3&FK_MapData=%2$s&FK_SFDBSrc=%3$s&STable=%4$s&SColumns=%5$s",
							Glo.getCCFlowAppPath()+"WF/MapDef/ImpTableField.jsp", object.getFK_MapData(),
							request.getParameter("FK_SFDBSrc"),
							(tempVar != null) ? tempVar : "LB_Table",
							selectedColumns));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
