package cn.jflow.common.model;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.Sys.SystemConfig;
import BP.Sys.MapExtXmlList;

public class InitScriptModel extends BaseModel {
	public InitScriptModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	// /#region 属性。
	public final String getFK_MapData() {
		return getParameter("FK_MapData");
	}

	public final String getOperAttrKey() {
		return getParameter("OperAttrKey");
	}

	public final String getExtType() {
		return MapExtXmlList.TBFullCtrl;
	}

	public String Lab = null;
	// 需要拼接的字符串
	public StringBuilder Pub1 = null;

	// /#endregion 属性。

	public void init() {

		this.Pub1 = new StringBuilder();
		
		this.Pub1.append(BaseModel.AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:80%'"));
		this.Pub1.append(BaseModel.AddTR());
		/*this.Pub1.append(BaseModel.AddTDGroupTitle("内置JavaScript脚本"
				+ BP.WF.Glo.GenerHelpCCForm("帮助", "", "")));*/
		this.Pub1.append(BaseModel.AddCaption("内置JavaScript脚本"+ BP.WF.Glo.GenerHelpCCForm("帮助", "", "")));
		this.Pub1.append(BaseModel.AddTREnd());

		TextBox tb = new TextBox();
		tb.setId("TB_Doc");
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setRows(20);
		tb.setColumns(70);
		tb.addAttr("width", "95%");

		String path = SystemConfig.getPathOfDataUser() + "JSLibData/"
				+ this.getFK_MapData() + "_Self.js";

		File file = new File(path);

		if (file.isFile() && file.exists()) {
			tb.setText(BP.DA.DataType.ReadTextFile(path));
		}

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDBegin());
		this.Pub1.append(tb);
		this.Pub1.append(BaseModel.AddBR());
		this.Pub1
				.append("编写说明：该内容是读取写入文件到:"
						+ path
						+ ",</br>您也可以通过js的编辑工具，编辑后放入该位置，表单在运行的时候会自动加载该文件，使用ccform的内置函数请点击右上角帮助。");
		this.Pub1.append(BaseModel.AddTDEnd());
		this.Pub1.append(BaseModel.AddTREnd());

		Button btn = new Button();
		// LinkBtn btn = new LinkBtn(false, NamesOfBtn.Save, "保存");
		btn.setText("保存");
		btn.addAttr("onclick", "btn_Save_Click();");
		// btn.Click += new EventHandler(btn_Save_Click);

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD(btn));
		this.Pub1.append(BaseModel.AddTREnd());
		this.Pub1.append(BaseModel.AddTableEnd());
		String html = BP.DA.DataType.ReadTextFile2Html(BP.Sys.SystemConfig.getPathOfData() + "HelpDesc/InitScript.txt");
		this.Pub1.append(AddFieldSet("帮助", html));
	}
}
