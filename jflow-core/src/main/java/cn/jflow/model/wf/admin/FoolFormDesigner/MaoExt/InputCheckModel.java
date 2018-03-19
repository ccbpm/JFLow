package cn.jflow.model.wf.admin.FoolFormDesigner.MaoExt;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import cn.jflow.common.model.BaseModel;
import cn.jflow.model.designer.ListSelectionMode;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.RadioButton;

public class InputCheckModel extends BaseModel {

	public boolean RB_0C;
	
	public boolean getRB_0C() {
		if(null==this.get_request().getParameter("RB_0C")||"RB_0".equals(this.get_request().getParameter("RB_0C"))){
			return true;
		}
		return false;
	}

	public void setRB_0C(boolean rB_0C) {
		this.RB_0C = rB_0C;
	}

	public StringBuffer Pub1;

	public String getPub1() {
		return Pub1.toString();
	}

	public InputCheckModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	public final String getFK_MapData() {
		return this.get_request().getParameter("FK_MapData");
	}

	public final String getOperAttrKey() {
		return this.get_request().getParameter("OperAttrKey");
	}

	public final String getExtType() {
		return MapExtXmlList.InputCheck;
	}

	public final String getMyPK() {
		String s = this.get_request().getParameter("MyPK");
		if (null==s||"".equals(s)) {
			s = this.get_request().getParameter("PK");
		}
		if (null==s||"".equals(s)) {
			s = null;
		}
		return s;
	}

	public final String getRefNo() {
		String s = this.get_request().getParameter("RefNo");
		if (null==s||"".equals(s)) {
			s = this.get_request().getParameter("No");
		}
		return s;
	}

	public final String getLab() {
		return "脚本验证";
	}

	///#endregion 属性。

	private String temFile = "s@xa";

	public void Page_Load() {
		//this.Title = this.getLab();

		//			switch (this.DoType)
		//ORIGINAL LINE: case "Del":
		Pub1 =  new StringBuffer();
		if (this.getDoType().equals("Del")) {
			MapExt mm = new MapExt();
			mm.setMyPK(this.getMyPK());
			mm.Retrieve();
			mm.Delete();
			try {
				getResponse().sendRedirect("InputCheck.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo="
						+ this.getRefNo());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else {}

		if (this.getMyPK() != null || this.getDoType().equals("New")) {
			Edit_InputCheck();
			return;
		}

		MapExts mes = new MapExts();
		mes.Retrieve(MapExtAttr.ExtType, this.getExtType(), MapExtAttr.FK_MapData, this.getFK_MapData());
		this.MapJS(mes);
	}

	public final void Edit_InputCheck() {
		MapExt me = null;
		if (this.getMyPK() == null) {
			me = new MapExt();
			this.Pub1.append(AddEasyUiPanelInfoBegin("新建:" + this.getLab(), "icon-new"));
		} else {
			me = new MapExt(this.getMyPK());
			this.Pub1.append(AddEasyUiPanelInfoBegin("编辑:" + this.getLab(), "icon-edit"));
		}
		me.setFK_MapData(this.getFK_MapData());
		temFile = me.getTag();

		this.Pub1.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
		MapAttr attr = new MapAttr(this.getRefNo());
		this.Pub1.append(AddTRGroupTitle(2, attr.getKeyOfEn() + " - " + attr.getName()));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("函数库来源:"));
		this.Pub1.append(AddTDBegin());

		RadioButton rb = new RadioButton();
		rb.setText("ccflow系统js函数库.");
		rb.setId("RB_0");
		rb.setAutoPostBack(true);
		if (me.getDoWay() == 0) {
			rb.setChecked(true);
		} else {
			rb.setChecked(false);
		}
		rb.setGroupName("s");
		rb.addAttr("onchange", "rb_CheckedChanged()");
		if(this.getRB_0C())
			rb.setChecked(true);
		else
			rb.setChecked(false);
		this.Pub1.append(Add(rb));

		rb = new RadioButton();
		rb.AutoPostBack = true;
		rb.setText("我自定义的函数库.");
		rb.addAttr("onchange", "rb_CheckedChanged()");
		rb.setGroupName("s");
		rb.setId("RB_1");
		rb.AutoPostBack = true;
		if (me.getDoWay() == 1) {
			rb.setChecked(true);
		} else {
			rb.setChecked(false);
		}
		if(this.getRB_0C())
			rb.setChecked(false);
		else
			rb.setChecked(true);
		this.Pub1.append(Add(rb));
		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDGroupTitle("colspan=2", "函数列表"));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTR());

		ListBox lb = new ListBox();
		lb.addAttr("style", "width:100%");
		lb.setAutoPostBack(false);
		lb.setId("LB1");
		lb = rb_CheckedChanged(lb);
		this.Pub1.append(AddTD("colspan=2", lb));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTRSum());

		LinkButton btn = new LinkButton();
		btn.setName("保存");
		btn.setText("保存");
		btn.addAttr("onclick", "btn_SaveInputCheck_Click()");

		this.Pub1.append(AddTD(btn));
		this.Pub1.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-back'\" href='InputCheck.jsp?FK_MapData="
				+ this.getFK_MapData() + "&ExtType=" + this.getExtType() + "'>返回</a>"));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
		this.Pub1.append(AddEasyUiPanelInfoEnd());
	}

	private ListBox rb_CheckedChanged(ListBox lb) {//Object sender, EventArgs e
		String path = BP.Sys.SystemConfig.getPathOfData() + "JSLib/";
		//// = this.Pub1.append(GetRadioButtonByID("RB_0"));
		if (this.getRB_0C() == false) {
			path = BP.Sys.SystemConfig.getPathOfDataUser() + "JSLib/";
		}

		//Object tempVar = null;////= this.Pub1.append(FindControl("LB1"));
		//ListBox lb = new ListBox();//(ListBox) ((tempVar instanceof ListBox) ? tempVar : null);
		lb.Items.clear();
		lb.setAutoPostBack(false);
		lb.setSelectionMode(ListSelectionMode.Multiple);
		lb.addAttr("rows", "10");

		String file = temFile;
		if (isNullOrEmpty(temFile) == false) {
			file = file.substring(file.lastIndexOf('\\') + 4);
			file = file.replace(".js", "");
		} else {
			file = "!!!";
		}

		MapExts mes = new MapExts();
		mes.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getOperAttrKey(), MapExtAttr.ExtType, this.getExtType());

		/*String[] dirs = System.IO.Directory.GetDirectories(path);
		for (String dir : dirs) {
			String[] strs = Directory.GetFiles(dir);
			for (String s : strs) {
				if (s.contains(".js") == false) {
					continue;
				}

				ListItem li = new ListItem(s.replace(path, "").replace(".js", ""), s);

				if (s.contains(file)) {
					li.setSelected(true);
				}

				lb.Items.add(li);
			}
		}*/
		File dirs = new File(path);
		File[] tempList = dirs.listFiles();;
		//System.out.println("该目录下对象个数：" + tempList.length);
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				//System.out.println("文     件：" + tempList[i]);
				if (tempList[i].getName().contains(".js") == false) {
					continue;
				}

				ListItem li = new ListItem(tempList[i].getName().replace(path, "").replace(".js", ""), tempList[i].getPath());

				if (tempList[i].getName().contains(file)) {
					li.setSelected(true);
				}

				lb.Items.add(li);
			}
			if (tempList[i].isDirectory()) {
				File dirsT = new File(tempList[i].getPath());
				File[] tempListT = dirsT.listFiles();
				for(int j = 0; j < tempListT.length; j++){
					if (tempListT[j].getName().contains(".js") == false) {
						continue;
					}

					ListItem li = new ListItem(tempListT[j].getName().replace(path, "").replace(".js", ""), tempListT[j].getPath());

					if (tempListT[j].getName().contains(file)) {
						li.setSelected(true);
					}

					lb.Items.add(li);
				}
			}
		}
		return lb;
	}

	public final void MapJS(MapExts ens) {
		this.Pub1.append(AddTableNormal());
		this.Pub1.append(AddTRGroupTitle(5, this.getLab()));

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDGroupTitleCenter("字段"));
		this.Pub1.append(AddTDGroupTitleCenter("类型"));
		this.Pub1.append(AddTDGroupTitleCenter("验证函数中文名"));
		this.Pub1.append(AddTDGroupTitleCenter("显示"));
		this.Pub1.append(AddTDGroupTitleCenter("操作"));
		this.Pub1.append(AddTREnd());

		MapAttrs attrs = new MapAttrs(this.getFK_MapData());

		for (MapAttr attr : attrs.ToJavaList()) {
			if (attr.getUIVisible() == false) {
				continue;
			}

			MapExt myEn = null;

			for (MapExt en : ens.ToJavaList()) {
				if (en.getAttrOfOper().equals(attr.getKeyOfEn())) {
					myEn = en;
					break;
				}
			}

			if (myEn == null) {
				this.Pub1.append(AddTRTX());
				this.Pub1.append(AddTD(attr.getKeyOfEn() + "-" + attr.getName()));
				this.Pub1.append(AddTD("无"));
				this.Pub1.append(AddTD("无"));
				this.Pub1.append(AddTD("无"));
				this.Pub1.append(AddTDBegin());
				this.Pub1.append(AddEasyUiLinkButton("编辑", "InputCheck.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType()
						+ "&RefNo=" + attr.getMyPK() + "&OperAttrKey=" + attr.getKeyOfEn() + "&DoType=New", "icon-edit"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			} else {
				this.Pub1.append(AddTRTX());
				this.Pub1.append(AddTD(attr.getKeyOfEn() + "-" + attr.getName()));

				if (myEn.getDoWay() == 0) {
					this.Pub1.append(AddTD("系统函数"));
				} else {
					this.Pub1.append(AddTD("自定义函数"));
				}

				String file = myEn.getTag();
				file = file.substring(file.lastIndexOf('\\') + 4);
				file = file.replace(".js", "");

				this.Pub1.append(AddTDA(
						"InputCheck.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + myEn.getMyPK() + "&RefNo="
								+ attr.getMyPK() + "&OperAttrKey=" + attr.getKeyOfEn(), file));
				this.Pub1.append(AddTD(myEn.getTag2() + "=" + myEn.getTag1() + "(this);"));
				this.Pub1.append(AddTDBegin());
				this.Pub1.append(AddEasyUiLinkButton("删除",
						"javascript:DoDel('" + myEn.getMyPK() + "','" + this.getFK_MapData() + "','" + this.getExtType() + "');", "icon-delete"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			}
		}

		this.Pub1.append(AddTableEnd());
	}

	public final String AddEasyUiLinkButton(String text, String url, String iconCls) {
		boolean isPlain = false;
		String target = "";
		boolean disabled = false;
		if (isNullOrEmpty(url)) {
			return "javascript:void(0)";
		}

		return String.format(
				"<a class='easyui-linkbutton' href='%1$s' data-options=\"plain:%2$s,iconCls:'%3$s',disabled:%6$s\" target='%5$s'>%4$s</a>&nbsp;", url
						.replace("'", "\""), (new Boolean(isPlain)).toString().toLowerCase(), (iconCls != null) ? iconCls : "", text,
				disabled ? "_self" : target, (new Boolean(disabled)).toString().toLowerCase());
	}

	//----------------------------------------------------------------------------------------
	//		Copyright © 2006 - 2010 Tangible Software Solutions Inc.
	//		This class can be used by anyone provided that the copyright notice remains intact.
	//
	//		This class is used to simulate some .NET string functions in Java.
	//----------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'IsNullOrEmpty'.
	//------------------------------------------------------------------------------------
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (2 parameter version).
	//------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (4 parameter version).
	//------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray, int startindex, int count) {
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length && index - startindex < count; index++) {
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimEnd'.
	//------------------------------------------------------------------------------------
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

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimStart'.
	//------------------------------------------------------------------------------------
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

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Trim' when arguments are used.
	//------------------------------------------------------------------------------------
	public static String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	//------------------------------------------------------------------------------------
	//	This method is used for string equality comparisons when the option
	//	'Use helper 'stringsEqual' method to handle null strings' is selected
	//	(The Java String 'equals' method can't be called on a null instance).
	//------------------------------------------------------------------------------------
	public static boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}

}
