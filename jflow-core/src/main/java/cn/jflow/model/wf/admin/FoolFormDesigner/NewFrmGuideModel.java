package cn.jflow.model.wf.admin.FoolFormDesigner;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.Sys.SFDBSrc;
import BP.Sys.SysEnum;
import BP.Sys.FrmType;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.FileUpload;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;

public class NewFrmGuideModel extends BaseModel {
	
	public StringBuffer Pub1;

	public NewFrmGuideModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	///#region 参数
	public final int getStep() {
		try {
			return Integer.parseInt(this.getRequest().getParameter("Step"));
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public final int getFrmType() {
		try {
			return Integer.parseInt(this.getRequest().getParameter("FrmType"));
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	/**
	 * 数据源
	 */
	public final String getDBSrc() {
		String val = this.getRequest().getParameter("DBSrc");
		if (val == null || val.equals("")) {
			return "local";
		}
		return val;
	}

	/**
	 * 表单类别
	 */
	public final String getFK_FrmSort() {
		return this.getRequest().getParameter("FK_FrmSort");
	}

	///#endregion 参数

	public final void Page_Load() {
		Pub1 = new StringBuffer();
		if (this.getStep() == 0) {
			this.BindStep0();
		}

		if (this.getStep() == 1) {
			this.BindStep1();
		}
	}

	public final void BindStep0() {
		this.Pub1.append(AddTable(" style='width:100%;' "));

		this.Pub1.append(AddCaption("表单创建向导:请选择要创建的表单类型。"));

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDBegin());

		this.Pub1.append(AddFieldSet("<a class='title' href='?Step=1&FrmType=" + FrmType.FreeFrm.getValue() + "&FK_FrmSort=" + this.getFK_FrmSort()
				+ "&DBSrc=" + this.getDBSrc() + "' >创建自由表单</a>"));

		this.Pub1.append(Add("<div class='con-list' style='float:left'>"));
		this.Pub1.append(AddUL());
		this.Pub1.append(AddLi("自由表单是ccbpm推荐使用的表单."));
		this.Pub1.append(AddLi("他有丰富的界面元素,可以满足不同的应用需求."));
		this.Pub1.append(AddLi("采用了关系数据库存储格式，可以导出到xml存储，可以运行在任何设备上,实现与平台无关."));
		this.Pub1.append(AddLi("可以导入导出，格式不受影响，java , .net 移动终端都可以使用."));
		this.Pub1.append(AddULEnd());
		this.Pub1.append(Add("</div>"));

		this.Pub1.append(Add("<div style='float:right'>"));
		this.Pub1.append(Add("<a class='link-img' href='http://blog.csdn.net/jflows/article/details/50034329'  target='_blank'><img src='./Img/ziyouForm.png' width='400px' ></a>"));
		//this.Pub1.append(Add("<a href='http://ccflow.org/' ><img src='./Img/FrmType/FixFrm.png' width='400px' ></a>");
		this.Pub1.append(Add("</div>"));

		this.Pub1.append(AddFieldSetEnd());

		this.Pub1.append(AddFieldSet("<a class='title' href='?Step=1&FrmType=" + FrmType.FoolForm.getValue() + "&FK_FrmSort=" + this.getFK_FrmSort()
				+ "&DBSrc=" + this.getDBSrc() + "' >创建傻瓜表单</a>"));
		this.Pub1.append(Add("<div class='con-list' style='float:left'>"));
		this.Pub1.append(AddUL());
		this.Pub1.append(AddLi("傻瓜表单与自由表单就是展示格式不同,其他的与自由表单一样."));
		this.Pub1.append(AddLi("傻瓜表单有固定的列与行，格式简洁、清新、实用."));
		this.Pub1.append(AddULEnd());
		this.Pub1.append(Add("</div>"));

		this.Pub1.append(Add("<div style='float:right'>"));
		this.Pub1.append(Add("<a class='link-img' href='http://ccbpm.mydoc.io/?v=5404&t=17922' target='_blank' ><img src='./Img/shaguaForm.png' width='400px' ></a>"));
		this.Pub1.append(Add("</div>"));
		this.Pub1.append(AddFieldSetEnd());

		this.Pub1.append(AddFieldSet("<a class='title' href='?Step=1&FrmType=" + FrmType.ExcelFrm.getValue() + "&FK_FrmSort=" + this.getFK_FrmSort()
				+ "&DBSrc=" + this.getDBSrc() + "' >创建Excel表单</a>"));
		this.Pub1.append(Add("<div class='con-list' style='float:left'>"));
		this.Pub1.append(AddUL());
		this.Pub1.append(AddLi("Excel表单以excel表单模版为基础展现给用户，数据的展现与采集以excel文件为基础。"));
		this.Pub1.append(AddLi("您可以设置每个excel的单元格对应一个表的一个字段,"));
		this.Pub1.append(AddLi("用户在保存数据的时候可以保存到excel文件背后的数据表里。"));
		this.Pub1.append(AddLi("数据表可以用于综合分析，而excel文件用于数据展现。"));
		this.Pub1.append(AddLi("使用excel表单必须运行在IE浏览器上，需要支持activeX插件。"));
		this.Pub1.append(AddULEnd());
		this.Pub1.append(Add("</div>"));

		this.Pub1.append(Add("<div style='float:right'>"));
		this.Pub1.append(Add("<a class='link-img' href='http://ccbpm.mydoc.io/?v=5404&t=17922' target='_blank' ><img src='./Img/excelForm.jpg' width='400px' ></a>"));
		this.Pub1.append(Add("</div>"));
		this.Pub1.append(AddFieldSetEnd());

		this.Pub1.append(AddFieldSet("<a class='title' href='?Step=1&FrmType=" + FrmType.WordFrm.getValue() + "&FK_FrmSort=" + this.getFK_FrmSort()
				+ "&DBSrc=" + this.getDBSrc() + "' >创建Word表单</a>"));
		this.Pub1.append(Add("<div class='con-list' style='float:left'>"));
		this.Pub1.append(AddUL());
		this.Pub1.append(AddLi("Word表单以Word表单模版为基础展现给用户，数据的展现与采集以Word文件为基础。"));
		this.Pub1.append(AddLi("您可以设置每个Word的标签对应一个表的一个字段,"));
		this.Pub1.append(AddLi("用户在保存数据的时候可以保存到Word文件背后的数据表里。"));
		this.Pub1.append(AddLi("数据表可以用于综合分析，而excel文件用于数据展现。"));
		this.Pub1.append(AddLi("使用Word表单必须运行在IE浏览器上，需要支持activeX插件。"));
		this.Pub1.append(AddLi("Word表单多用于公文。"));
		this.Pub1.append(AddULEnd());
		this.Pub1.append(Add("</div>"));

		this.Pub1.append(Add("<div style='float:right'>"));
		this.Pub1.append(Add("<a class='link-img' href='http://ccbpm.mydoc.io/?v=5404&t=17922' target='_blank' ><img src='./Img/wordForm.jpg' width='400px' ></a>"));
		this.Pub1.append(Add("</div>"));
		this.Pub1.append(AddFieldSetEnd());

		this.Pub1.append(AddFieldSet("<a class='title' href='?Step=1&FrmType=" + FrmType.Url.getValue() + "&FK_FrmSort=" + this.getFK_FrmSort()
				+ "&DBSrc=" + this.getDBSrc() + "' >嵌入式表单</a>"));
		this.Pub1.append(Add("<div class='con-list' style='float:left'>"));
		this.Pub1.append(AddUL());
		this.Pub1.append(AddLi("自己编写一个表单jsp,aspx,php .... "));
		this.Pub1.append(AddLi("通过本功能把他注册到ccbpm的表单系统中,可以被其他的程序引用，比如流程引擎。"));
		this.Pub1.append(AddLi("如果要被驰骋工作流程引擎调用，自己定义的表单需要有一个约定的 Save javascript 函数,"));
		this.Pub1.append(AddLi("用于保存整个表单的数据，如果保存的时候异常，就抛出错误。"));
		this.Pub1.append(AddLi("驰骋工作流引擎在调用您的表单的时候，就需要传入一个数值类型的主键，参数名称为OID,"));
		this.Pub1.append(AddLi("该嵌入式表单获取这个参数做为主键处理。"));
		this.Pub1.append(AddLi("该表单用于特殊用户格式要求的表单，或者客户现在已有的表单。"));
		this.Pub1.append(AddULEnd());
		this.Pub1.append(Add("</div>"));

		this.Pub1.append(Add("<div style='float:right'>"));
		this.Pub1.append(Add("<a class='link-img' href='http://blog.csdn.net/jflows/article/details/50150457' target='_blank' ><img src='./Img/selfForm.png' width='400px' ></a>"));
		this.Pub1.append(Add("</div>"));
		this.Pub1.append(AddFieldSetEnd());

		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
	}

	public final void BindStep1() {
		this.Pub1.append(AddTable());
		this.Pub1.append(AddCaption("表单创建向导:填写表单基础信息"));

		int idx = 0;
		SysEnum se = new SysEnum(MapDataAttr.FrmType, this.getFrmType());
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("表单类型"));
		this.Pub1.append(AddTD(se.getLab()));
		this.Pub1.append(AddTD("返回上一步可以更改"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("数据源"));
		BP.Sys.SFDBSrc srcs = new SFDBSrc(this.getDBSrc());
		this.Pub1.append(AddTD(srcs.getName()));
		this.Pub1.append(AddTD("您可以把表单创建不同的数据源上."));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("创建路径"));
		//更改绑定样式 qin
		//BP.Sys.SysFormTrees trees = new SysFormTrees();

		//去除对应数据源根目录
		//trees.Retrieve(SysFormTreeAttr.DBSrc, this.DBSrc, SysFormTreeAttr.IsDir, "0");
		//DataTable dt = DBAccess.RunSQLReturnTable("SELECT No,Name,ParentNo FROM Sys_FormTree WHERE DBSrc='local' AND IsDir='0'");
		String sql = "SELECT No,Name,ParentNo FROM Sys_FormTree WHERE DBSrc='local' AND IsDir='0'";
		DDL ddl = new DDL();
		ddl.setId("DDL_FrmTree");
		////ddl.MakeTree(dt, "ParentNo", "0", "No", "Name", ddl, -1);
		ddl.BindSQL(sql, "No", "Name", "-1");
		//ddl.Bind(trees, this.DBSrc);
		this.Pub1.append(AddTD(ddl));
		this.Pub1.append(AddTD("表单类别."));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("<font color='Red'>*</font>表单名称"));
		TextBox tb = new TextBox();
		tb.setId("TB_Name");
		tb.setColumns(90);
		tb.addAttr("onchange", "tb_TextChanged()");
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("1到30个字符"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("<font color='Red'>*</font>表单编号"));
		tb = new TextBox();
		tb.setId("TB_No");
		tb.setColumns(90);
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("以字母或者下划线开头，不能包含中文或者其他特殊字符."));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("<font color='Red'>*</font>数据表"));
		tb = new TextBox();
		tb.setId("TB_PTable");
		tb.setColumns(90);

		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("只能以字母或者下划线开头，不能包含中文与其它特殊字符。"));
		this.Pub1.append(AddTREnd());

		
		///#region 快速填写.
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("快速填写"));
		this.Pub1.append(AddTDBegin());
		RadioButton rb = new RadioButton();
		rb.setId("RB0");
		rb.setText("生成全拼名称");
		rb.addAttr("onchange", "tb_TextChanged()");
		rb.setChecked(true);
		rb.AutoPostBack = true;
		rb.setGroupName("ss");
		this.Pub1.append(Add(rb));

		rb = new RadioButton();
		rb.setId("RB1");
		rb.setText("生成简拼名称");
		rb.addAttr("onchange", "tb_TextChanged()");
		rb.setGroupName("ss");
		rb.AutoPostBack = true;
		this.Pub1.append(Add(rb));
		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTD("注意:允许多个表单指定同一个表."));
		this.Pub1.append(AddTREnd());
		
		///#endregion 快速填写.

		
		///#region 表单生成方式.
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		if (this.getFrmType() != FrmType.Url.getValue()) {
			if (this.getFrmType() == FrmType.FreeFrm.getValue() || this.getFrmType() == FrmType.FoolForm.getValue()
					|| this.getFrmType() == FrmType.Silverlight.getValue()) {

				this.Pub1.append(AddTD("表单生成方式"));
				this.Pub1.append(AddTDBegin("colspan=2"));
				rb = new RadioButton();
				rb.setId("RB_FrmGenerMode_0");
				rb.setText("直接生成表单");
				rb.setChecked(true);
				rb.setGroupName("s2s");
				this.Pub1.append(Add(rb));
				this.Pub1.append(AddBR());

				rb = new RadioButton();
				rb.setId("RB_FrmGenerMode_1");
				rb.setText("从ccfrom云表单库中选择一个表单模版导入");
				rb.setGroupName("s2s");
				this.Pub1.append(Add(rb));
				this.Pub1.append(AddBR());

				rb = new RadioButton();
				rb.setId("RB_FrmGenerMode_2");
				rb.setText("从本机或其他数据库的的表导入表结构");
				rb.setGroupName("s2s");
				this.Pub1.append(Add(rb));
				this.Pub1.append(AddBR());
			}
			//ExcelFrm,WordFrm 只保留上传
			if (this.getFrmType() == FrmType.ExcelFrm.getValue() || this.getFrmType() == FrmType.WordFrm.getValue()) {
				this.Pub1.append(AddTD("<font color='Red'>*</font>上传" + se.getLab() + "（默认模版）"));
				this.Pub1.append(AddTDBegin("colspan=2"));

				FileUpload fUp = new FileUpload();
				fUp.setId("fUpFrm");
				fUp.addAttr("style","width:300;");

				this.Pub1.append(Add("&nbsp;&nbsp;&nbsp;"));
				this.Pub1.append(Add(fUp));

			}
			this.Pub1.append(AddTDEnd());
		} else {
			this.Pub1.append(AddTD("<font color='Red'>*</font>表单Url"));
			this.Pub1.append(AddTDBegin("colspan=2"));
			tb = new TextBox();
			tb.setId("TB_Url");
			tb.setColumns(40);

			this.Pub1.append(Add(tb));
			this.Pub1.append(Add("&nbsp;&nbsp;&nbsp;请正确填写表单链接,支持全局变量@Ho"));
			this.Pub1.append(AddTDEnd());
		}
		this.Pub1.append(AddTREnd());

		//  //ExcelFrm,WordFrm 只保留上传
		//if ((BP.Sys.FrmType)(this.FrmType) == BP.Sys.FrmType.ExcelFrm ||
		//    (BP.Sys.FrmType)(this.FrmType) == BP.Sys.FrmType.WordFrm)
		//{
		//    this.Pub1.append(AddTRend();
		//    this.Pub1.append(AddTREnd();
		//}

		
		///#endregion 表单生成方式.

		
		///#region 操作按钮放到table中，布局缩放不会乱
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTDBegin(" colspan='3' "));

		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setText("下一步");
		btn.addAttr("onclick", "BindStep1_Click()");
		this.Pub1.append(Add(btn));
		this.Pub1.append(Add("<input type='button' value='返回上一步' onclick='Back();' />"));

		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTREnd());
		
		///#endregion

		this.Pub1.append(AddTableEnd());
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
