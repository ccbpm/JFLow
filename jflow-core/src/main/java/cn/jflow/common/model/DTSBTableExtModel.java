package cn.jflow.common.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.SFDBSrc;
import BP.WF.Flow;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.RadioButton;

public class DTSBTableExtModel extends BaseModel {
	public DTSBTableExtModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public StringBuffer Pub1;

	public final String getFK_Flow() {
		return get_request().getParameter("FK_Flow");
	}

	public final String getFK_DBSrc() {
		return get_request().getParameter("FK_DBSrc");
	}

	public final String getTableName() {
		return get_request().getParameter("TableName");
	}

	public void init() {
		Pub1 = new StringBuffer();
		String rpt = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		MapAttrs attrs = new MapAttrs(rpt);

		Flow fl = new Flow(this.getFK_Flow());
		fl.RetrieveFromDBSources();

		fl.setNo(this.getFK_Flow());
		if (isNullOrEmpty(this.getTableName()) == true) {
			this.Pub1.append(AddFieldSet("配置错误",
					"请关闭该窗口，在流程属性里配置业务表名，然后点保存按钮，之后打开该功能界面。"));
			return;
		}

		// 获得数据表列.
		SFDBSrc src = new SFDBSrc(this.getFK_DBSrc());
		DataTable dt = src.GetColumns(this.getTableName());

		for (DataRow dr : dt.Rows) {
			dr.setValue("no", dr.getValue("Name"));
			dr.setValue("name", dr.getValue("name") + " " + dr.getValue("type")
					+ "(" + dr.getValue("length") + ")");
		}

		this.Pub1.append(AddTable());
		this.Pub1.append(AddCaptionLeft("请设置流程字段与业务表字段的同步映射"));
		this.Pub1.append(AddTR());

		String textCenter = " style='text-align:center;'";

		this.Pub1.append(AddTDTitle(textCenter, "序"));
		this.Pub1.append(AddTDTitle(textCenter, "是否同步"));
		this.Pub1.append(AddTDTitle(textCenter, "类型"));
		this.Pub1.append(AddTDTitle(textCenter, "业务表(" + fl.getDTSBTable()
				+ ")"));
		this.Pub1.append(AddTREnd());

		int idx = 0;
		Hashtable<String, String> ht = new Hashtable<String, String>();
		if (isNullOrEmpty(fl.getDTSFields())) {
			fl.setDTSFields("@");
		}

		String[] fieldArr = fl.getDTSFields().split("@");
		String[] lcArr;
		String[] ywArr;
		if (fieldArr.length > 0) {
			lcArr = fieldArr[0].split(",");
			ywArr = fieldArr[1].split(",");
			for (int i = 0; i < lcArr.length; i++) {
				ht.put(lcArr[i], ywArr[i]);
			}
		}

		// /#region 锁定workid 在第一行上.

		// guid workid必须选择一项
		RadioButton rb_workId = new RadioButton();
		rb_workId.setId("rb_workId");
		rb_workId.setGroupName("RB_KEY");
		rb_workId.setText("OID - WorkID");

		RadioButton rb_guid = new RadioButton();
		rb_guid.setId("rb_guid");
		rb_guid.setGroupName("RB_KEY");
		rb_guid.setText("GUID");

		Set<Entry<String, String>> entrySet = ht.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		Entry<String, String> entry;
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getKey().toString().toUpperCase().equals("OID")) {
				rb_workId.checked = true;
			} else {
				rb_guid.checked = true;
			}
		}

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx("1"));

		this.Pub1.append(AddTDBegin());
		this.Pub1.append(Add(rb_workId));
		this.Pub1.append(Add(rb_guid));
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD("主键<img src='../../Img/PRI/2.png' border=0/>"));

		DDL ddl = new DDL();
		// ddl.set(250);
		ddl.setId("DDL_OID");
		ddl.Bind(dt, "name", "name");
		ddl.SetSelectItem(fl.getDTSBTablePK());

		this.Pub1.append(AddTD(textCenter, ddl));
		this.Pub1.append(AddTREnd());
		// /#endregion 锁定workid 在第一行上.

		boolean is1 = false;
		for (MapAttr attr : attrs.ToJavaList()) {
			if (attr.getKeyOfEn().toUpperCase().equals("OID")
					|| attr.getKeyOfEn().toUpperCase().equals("GUID")) {
				continue;
			}

			idx++;

			/* is1 = this.Pub1.append(AddTR(is1)); */

			this.Pub1.append(AddTDIdx(idx));

			CheckBox cb = new CheckBox();
			cb.setId("CB_" + attr.getKeyOfEn());
			cb.setText(attr.getKeyOfEn() + " - " + attr.getName());

			/*
			 * for (Entry de : ht.entrySet().iterator()) { if
			 * (de.getKey().toString().equals(attr.getKeyOfEn())) { cb.checked =
			 * true; } }
			 */
			/*
			 * while(ht.entrySet().iterator().hasNext()) { if
			 * (ht.entrySet().toString().equals(attr.getKeyOfEn())) { cb.checked
			 * = true; } }
			 */
			Iterator its = ht.keySet().iterator();
			while (its.hasNext()) {
				// 从ht中取
				String key = (String) its.next();
				Object value = ht.get(key);
				// 放进hm中
				if (key.equals(attr.getKeyOfEn())) {
					cb.checked = true;
				}
			}

			this.Pub1.append(AddTD(cb));
			this.Pub1.append(AddTD(attr.getMyDataTypeStr()));

			ddl = new DDL();
			ddl.setId("DDL_" + attr.getKeyOfEn());
			// ddl.setW(250);

			ddl.Bind(dt, "name", "name");

			if (cb.checked == true) {
				try {
					ddl.SetSelectItem(ht.get(attr.getKeyOfEn()).toString());
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}
			}

			// 类似的默认选中 区分大小写 方法if(ddl.SetSelectItem(attr.KeyOfEn)){cb.Checked =
			// true;}不适用
			for (DataRow dr : dt.Rows) {
				if (attr.getKeyOfEn().toUpperCase()
						.equals(dr.getValue(0).toString().toUpperCase())) {
					ddl.SetSelectItem(dr.getValue(0).toString());
					cb.checked = true;
					break;
				}
			}

			this.Pub1.append(AddTD(textCenter, ddl));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTableEnd());

		this.Pub1.append(AddBR());
		this.Pub1.append(AddBR());

		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setText("保存");

		// event wireups:
		btn.addAttr("onclick", "btn_Save_Click()");

		Button btnClose = new Button();
		btnClose.setId("Btn_Close");
		btnClose.setText("取消");
		// event wireups:
		btnClose.addAttr("onclick", "btnClose_Click()");

		this.Pub1.append(Add(btn));
		this.Pub1.append(Add(btnClose));

		this.Pub1.append(AddBR());
		this.Pub1.append(AddBR());
		this.Pub1.append(AddBR());
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'IsNullOrEmpty'.
	// ------------------------------------------------------------------------------------
	public boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (2
	// parameter version).
	// ------------------------------------------------------------------------------------
	public String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (4
	// parameter version).
	// ------------------------------------------------------------------------------------
	public String join(String separator, String[] stringarray, int startindex,
			int count) {
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
	public String trimEnd(String string, Character... charsToTrim) {
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
	public String trimStart(String string, Character... charsToTrim) {
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

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Trim' when
	// arguments are used.
	// ------------------------------------------------------------------------------------
	public String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	// ------------------------------------------------------------------------------------
	// This method is used for string equality comparisons when the option
	// 'Use helper 'stringsEqual' method to handle null strings' is selected
	// (The Java String 'equals' method can't be called on a null instance).
	// ------------------------------------------------------------------------------------
	public boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}
}
