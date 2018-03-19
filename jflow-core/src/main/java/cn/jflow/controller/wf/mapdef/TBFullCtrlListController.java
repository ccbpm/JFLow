package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DataTable;
import BP.En.FieldTypeS;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapExt;

@Controller
@RequestMapping(value = "/WF/TBFullCtrlList")
public class TBFullCtrlListController {

	// /#region 属性。

	public String FK_MapData = "";

	public String OperAttrKey = "";

	public String MyPK = "";

	public String RefNo = "";

	public String Lab = null;

	@ResponseBody
	@RequestMapping(value = "/Btn_Save_Click", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response, String TB_SQL) {
		MyPK = request.getParameter("MyPK");

		MapExt myme = new MapExt(this.MyPK);
		MapAttrs attrs = new MapAttrs(myme.getFK_MapData());
		String info = "";
		for (MapAttr attr : MapAttrs.convertMapAttrs(attrs)) {
			if (attr.getLGType() == FieldTypeS.Normal) {
				continue;
			}
			if (attr.getUIIsEnable() == false) {
				continue;
			}
			if (TB_SQL.trim().equals("")) {
				continue;
			}
			try {
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(TB_SQL);
				if (TB_SQL.contains("@Key") == false) {
					throw new RuntimeException("缺少@Key参数。");
				}
				if (dt.Columns.contains("No") == false
						|| dt.Columns.contains("Name") == false) {
					throw new RuntimeException(
							"在您的sql表单公式中必须有No,Name两个列，来绑定下拉框。");
				}

			}

			catch (RuntimeException ex) {
				// this.Alert("SQL ERROR: " + ex.getMessage());
				return "{\"msg\":\"SQL ERROR: " + ex.getMessage() + "\"}";
			}
			info += ("$" + attr.getKeyOfEn() + ":" + TB_SQL);
		}

		myme.setTag(info);
		myme.Update();
		return "{\"msg\":\"保存成功\"}";
	}
}
