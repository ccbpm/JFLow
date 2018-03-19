package cn.jflow.common.model;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataTable;
import BP.Sys.SFDBSrc;

public class DTSBTableModel extends BaseModel{
	public DTSBTableModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		// TODO Auto-generated constructor stub
	}

	/*public DTSBTableModel(HttpServletRequest request, String basePath) {
		//this.basePath = basePath;
		this._request = request;
	}*/
	public boolean RB_DTSWay0;
	public boolean RB_DTSWay1;
	public boolean RB_DTSField0;
	public boolean RB_DTSField1;
	public boolean RB_DTSTime0;
	public boolean RB_DTSTime1;
	public boolean RB_DTSTime2;
	public HashMap<String, String> DDL_DBSrc =  new HashMap<String, String>();
	public HashMap<String, String> DDL_Table =  new HashMap<String, String>();
	
	public final String getFK_Flow() {
		String str = get_request().getParameter("FK_Flow");
		if (isNullOrEmpty(str)) {
			return "001";
		}
		return str;
	}

	public void init() {
		BP.WF.Flow fl = new BP.WF.Flow(this.getFK_Flow());
		
		// 设置状态.
		if (fl.getDTSWay().equals(BP.WF.Template.FlowDTSWay.None)) {
			RB_DTSWay0 = true;
		} else {
			RB_DTSWay1 = true;
		}

		// 绑定数据源.
		BP.Sys.SFDBSrcs srcs = new BP.Sys.SFDBSrcs();
		srcs.RetrieveAll();
		for(int i=0;i<srcs.size();i++){
			DDL_DBSrc.put(fl.getDTSDBSrc(),""+srcs.get(i).getRow().get("name"));
		}
		//BP.Web.Controls.Glo.DDL_BindEns(this.DDL_DBSrc, srcs, fl.getDTSDBSrc());

		// 设置自动.
		//this.DDL_DBSrc.AutoPostBack = true;
		//this.DDL_DBSrc.SelectedIndexChanged += new EventHandler(DDL_DBSrc_SelectedIndexChanged);

		// 绑定表.
		BP.Sys.SFDBSrc src = new SFDBSrc(fl.getDTSDBSrc());
		DataTable dt = src.GetTables();
		dt = RemoveView(dt); // 去除视图
		
		for(int i=0;i<dt.Rows.size();i++){
			DDL_Table.put(dt.Rows.get(i).get("no")+"",dt.Rows.get(i).get("name")+"");
		}
		//BP.Web.Controls.Glo.DDL_BindDataTable(this.DDL_Table, dt, fl.DTSBTable);
		// 设置自动.
		//this.DDL_Table.AutoPostBack = true;
		//this.DDL_Table.SelectedIndexChanged += new EventHandler(DDL_Table_SelectedIndexChanged);

		// 绑定字段同步的方式.
		if (fl.getDTSField().equals(BP.WF.Template.DTSField.SameNames)) {
			RB_DTSField0 = true;
		} else {
			RB_DTSField1 = true;
		}

		// 绑定同步的时间.
		if (fl.getDTSTime().equals(BP.WF.Template.FlowDTSTime.AllNodeSend)) {
			RB_DTSTime0 = true;
		}
		if (fl.getDTSTime().equals(BP.WF.Template.FlowDTSTime.SpecNodeSend)) {
			RB_DTSTime1 = true;
		}
		if (fl.getDTSTime().equals(BP.WF.Template.FlowDTSTime.WhenFlowOver)) {
			RB_DTSTime2 = true;
		}
	}

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

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Trim' when arguments
	// are used.
	// ------------------------------------------------------------------------------------
	public static String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	// ------------------------------------------------------------------------------------
	// This method is used for string equality comparisons when the option
	// 'Use helper 'stringsEqual' method to handle null strings' is selected
	// (The Java String 'equals' method can't be called on a null instance).
	// ------------------------------------------------------------------------------------
	public static boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}
	
	private DataTable RemoveView(DataTable dt)
	{

		for (int i = dt.Rows.size() - 1; i >= 0; i--)
		{
			if (dt.Rows.get(i).get("xtype").toString().toUpperCase().equals("V"))
			{
				dt.Rows.remove(i);
			}
		}

		return dt;
	}
}
