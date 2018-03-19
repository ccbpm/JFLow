package cn.jflow.controller.wf.admin.AttrFlow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.PubClass;
import BP.Sys.SFDBSrc;
import BP.WF.Flow;
import BP.WF.StartLimitRole;
import BP.WF.Template.DTSField;
import BP.WF.Template.FlowDTSTime;
import BP.WF.Template.FlowDTSWay;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/DTSBTable")
@Scope("request")
public class DTSBTableController extends BaseController {
	/*
	 * protected final void Btn_Save_Click(Object sender, EventArgs e) { Save();
	 * 
	 * }
	 */
	@ResponseBody
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response, String FK_Flow, String Way22,
			String DDL_DBSrc, String DDL_Table, String Way, String xx,
			String HiddenField) {
		Flow flow = new Flow(FK_Flow);

		// /#region 不同步
		if ("RB_DTSWay0".equals(Way22)) {
			flow.setDTSWay(FlowDTSWay.None);
		}
		// /#endregion

		// /#region 同步
		if ("RB_DTSWay1".equals(Way22)) {
			flow.setDTSDBSrc(DDL_DBSrc);
			flow.setDTSBTable(DDL_Table);

			DTSField field = DTSField.SameNames;

			if ("RB_DTSField1".equals(Way)) {
				field = DTSField.SpecField;
			}

			flow.setDTSField(field);

			FlowDTSTime time = FlowDTSTime.AllNodeSend;

			if ("RB_DTSTime1".equals(xx)) {
				time = FlowDTSTime.SpecNodeSend;
			}

			if ("RB_DTSTime2".equals(xx)) {
				time = FlowDTSTime.WhenFlowOver;
			}

			if (time == FlowDTSTime.SpecNodeSend) {
				String specNodes = trimEnd(HiddenField, ',');
				if (isNullOrEmpty(specNodes)) {
					//PubClass.Alert("没有设置要同步的节点", response);
					return "{\"msg\":\"没有设置要同步的节点\"}";
				} else {
					flow.setDTSSpecNodes(trimEnd(specNodes, ','));
				}
			}

			flow.setDTSTime(time);
			flow.setDTSWay(FlowDTSWay.Syn);

			// /#region 字段名相同
			SFDBSrc s = new SFDBSrc("local");
			if (field == DTSField.SameNames) {
				DataTable dt = s.GetColumns(flow.getPTable());

				s = new SFDBSrc(DDL_DBSrc); // this.src);
				DataTable ywDt = s.GetColumns(DDL_Table); // this.ywTableName);

				String str = "";
				String ywStr = "";
				for (DataRow ywDr : ywDt.Rows) {
					for (DataRow dr : dt.Rows) {
						if (ywDr.getValue("No")
								.toString()
								.toUpperCase()
								.equals(dr.getValue("No").toString()
										.toUpperCase())) {
							if (dr.getValue("No").toString().toUpperCase()
									.equals("OID")) {
								flow.setDTSBTablePK("OID");
							}
							str += dr.getValue("No").toString() + ",";
							ywStr += ywDr.getValue("No").toString() + ",";
						}
					}
				}

				if (!isNullOrEmpty(str)) {
					flow.setDTSFields(trimEnd(str, ',') + "@"
							+ trimEnd(ywStr, ','));
				} else {
					return "{\"msg\":\"未检测到业务主表【" + flow.getPTable() + "】与表【"
							+ DDL_Table + "】有相同的字段名.\"}"; // 不执行保存
				}
			} else // 按设置的字段匹配 检查在
			{
				try {
					s = new SFDBSrc("local");
					String str = flow.getDTSFields();

					String[] arr = str.split("@");

					String sql = "SELECT " + arr[0] + " FROM "
							+ flow.getPTable();

					s.RunSQL(sql);

					s = new SFDBSrc(DDL_DBSrc);

					sql = "SELECT " + arr[1] + ", " + flow.getDTSBTablePK()
							+ " FROM " + flow.getDTSBTable();

					s.RunSQL(sql);

				} catch (java.lang.Exception e) {
					// PubClass.Alert(ex.Message);
					return "{\"msg\":\"设置的字段有误.【" + flow.getDTSFields() + "】\"}"; // 不执行保存
				}
			}
			// /#endregion
		}
		// /#endregion

		int i = flow.Update();
		if (i > 0) {
			return "{\"msg\":\"保存成功\"}";
		} else {
			PubClass.Alert("保存失败", response);
			return "{\"msg\":\"保存失败\"}";
		}
	}

	// ----------------------------------------------------------------------------------------
	// Copyright © 2006 - 2010 Tangible Software Solutions Inc.
	// This class can be used by anyone provided that the copyright notice
	// remains intact.
	//
	// This class is used to simulate some .NET string functions in Java.
	// ----------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'IsNullOrEmpty'.
	// ------------------------------------------------------------------------------------
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (2 parameter
	// version).
	// ------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (4 parameter
	// version).
	// ------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray,
			int startindex, int count) {
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length
				&& index - startindex < count; index++) {
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'TrimEnd'.
	// ------------------------------------------------------------------------------------
	public static String trimEnd(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					lengthToKeep = index;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'TrimStart'.
	// ------------------------------------------------------------------------------------
	public static String trimStart(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					startingIndex = index + 1;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(startingIndex);
	}
}