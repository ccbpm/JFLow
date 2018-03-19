package cn.jflow.common.model;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.Converter;
import org.hamcrest.BaseMatcher;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.EntitiesNoName;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Template.BtnLab;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.BatchRole;
import BP.WF.Work;
import BP.WF.Works;
import BP.Web.WebUser;

public class PageLoadFullModel extends BaseModel {
	public PageLoadFullModel(HttpServletRequest request,
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
		return MapExtXmlList.PageLoadFull;
	}

	public String Lab = null;
	// 需要拼接的字符串
	public StringBuilder Pub1 = null;

	// /#endregion 属性。

	public void init() {
		BindPageLoadFull();
	}

	/**
	 * BindPageLoadFull
	 */
	public final void BindPageLoadFull() {
		this.Pub1 = new StringBuilder();

		MapExt me = new MapExt();
		me.setMyPK(this.getFK_MapData() + "_" + this.getExtType());
		me.RetrieveFromDBSources();

		this.Pub1
				.append(BaseModel
						.AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDGroupTitle("主表SQL设置"
				+ BP.WF.Glo.GenerHelpCCForm("帮助", "", "")));

		this.Pub1.append(BaseModel.AddTREnd());

		TextBox tb = new TextBox();
		tb.setId("TB_" + MapExtAttr.Tag);
		tb.setText(me.getTag());
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setRows(10);
		tb.setColumns(70);
		tb.addAttr("width", "99%");

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDBegin());
		this.Pub1.append(tb);
		this.Pub1.append(BaseModel.AddBR());
		this.Pub1
				.append("说明:填充主表的sql,表达式里支持@变量与约定的公用变量。 <br>比如: SELECT No,Name,Tel FROM Port_Emp WHERE No='@WebUser.No' , 如果列名与开始表单字段名相同，就会自动给值。");
		this.Pub1.append(BaseModel.AddTDEnd());
		this.Pub1.append(BaseModel.AddTREnd());

		MapDtls dtls = new MapDtls(this.getFK_MapData());
		if (dtls.size() != 0) {
			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddTDGroupTitle("明细表SQL设置"));
			this.Pub1.append(BaseModel.AddTREnd());

			String[] sqls = me.getTag1().split("[*]", -1);
			for (MapDtl dtl : dtls.ToJavaList()) {
				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTD("明细表:[" + dtl.getNo()
						+ "]&nbsp;" + dtl.getName()));
				this.Pub1.append(BaseModel.AddTREnd());

				tb = new TextBox();
				tb.setId("TB_" + dtl.getNo());
				for (String sql : sqls) {
					if (StringHelper.isNullOrEmpty(sql)) {
						continue;
					}
					String key = sql.substring(0, sql.indexOf('='));
					if (key.equals(dtl.getNo())) {
						tb.setText(sql.substring(sql.indexOf('=') + 1));
						break;
					}
				}

				tb.setTextMode(TextBoxMode.MultiLine);
				tb.setRows(10);
				tb.setColumns(70);
				tb.addAttr("width", "99%");

				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTDBegin());
				this.Pub1.append(tb);
				this.Pub1.append(BaseModel.AddBR());
				this.Pub1.append("说明:结果集合填充从表");
				this.Pub1.append(BaseModel.AddTREnd());
			}
		}

		Button btn = new Button();
		// LinkBtn btn = new LinkBtn(false, NamesOfBtn.Save, "保存");

		btn.setText("保存");
		btn.addAttr("onclick", "btn_SavePageLoadFull_Click();");

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD(btn));
		this.Pub1.append(BaseModel.AddTREnd());
		this.Pub1.append(BaseModel.AddTableEnd());
		return;
	}
}
