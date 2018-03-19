package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.XML.RegularExpression;
import BP.Sys.XML.RegularExpressions;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.XML.XmlEn;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class RegularExpressionModel extends BaseModel{
	public String fkMapData;
	public String fkMapDtl;
	public String OperAttrKey;
	public String DoType;
	public String RefNo;
	public String PageTitle;
	public StringBuffer Pub1;
	
	public MapDtl dtl;

	public MapDtl getDtl() {
		return dtl;
	}

	public final String getExtType()
	{
		return MapExtXmlList.RegularExpression;
	}
	
	public RegularExpressionModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	// /#region 属性.
	public  String getFK_MapData() {
		return getParameter("FK_MapData");
	}
	
	public  String getFK_MapDtl() {
		return getParameter("FK_MapDtl");
	}
	public  String getOperAttrKey() {
		return getParameter("OperAttrKey");
	}
	public  String getDoType() {
		return getParameter("DoType");
	}
	public  String getRefNo() {
		return getParameter("RefNo");
	}
	public void load() {
		fkMapData = this.getFK_MapData();
		fkMapDtl = getFK_MapDtl();
		OperAttrKey = getOperAttrKey();
		DoType = getDoType();
		RefNo = getRefNo();
		Pub1 = new StringBuffer();
		
		if (this.DoType!=null&&this.DoType.equals("templete")) //选择模版
		{
			this.BindReTemplete();
			return;
		}

		this.PageTitle = "为字段["+this.RefNo+"]设置正则表达式.";

		//this.Pub1.AddTable();
		this.Pub1.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
		this.Pub1.append(AddCaptionLeft("为字段[" + this.RefNo + "]设置正则表达式." + BP.WF.Glo.GenerHelpCCForm("正则表达式", "http://ccform.mydoc.io/?v=5769&t=36728", "ss")));

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDGroupTitle("序"));
		this.Pub1.append(AddTDGroupTitle("事件"));
		this.Pub1.append(AddTDGroupTitle("事件内容"));
		this.Pub1.append(AddTDGroupTitle("提示信息"));
		this.Pub1.append(AddTREnd());

		///#region 绑定事件
		int idx = 1;
		idx = BindRegularExpressionEditExt(idx, "onblur");
		idx = BindRegularExpressionEditExt(idx, "onchange");

		idx = BindRegularExpressionEditExt(idx, "onclick");
		idx = BindRegularExpressionEditExt(idx, "ondblclick");

		idx = BindRegularExpressionEditExt(idx, "onkeypress");
		idx = BindRegularExpressionEditExt(idx, "onkeyup");
		idx = BindRegularExpressionEditExt(idx, "onsubmit");
		///#endregion

		this.Pub1.append(AddTableEnd());

		//Button btn = new Button();
		//LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
		//btn.ID = "Btn_Save";
		//btn.Text = "保存";
		//btn.Click += new EventHandler(BindRegularExpressionEdit_Click);
		//btn.setHref("BindRegularExpressionEdit_Click");
		String btn = "<input type=\"button\" onclick=\"BindRegularExpressionEdit_Click()\" value=\"保存\">";
		this.Pub1.append(btn);

		//LinkButton mybtn = new LinkButton(false, NamesOfBtn.Excel.toString(), "加载模版");
		//mybtn.Click += new EventHandler(Excel_Click);
		//mybtn.setHref("Excel_Click");
		String mybtn = "<input type=\"button\" onclick=\"Excel_Click()\" value=\"加载模版\">";
		this.Pub1.append(mybtn);
	}
	
	public final void BindReTemplete()
	{
		this.Pub1.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
		this.Pub1.append(AddCaptionLeft("使用事件模版,能够帮助您快速的定义表单字段事件" + BP.WF.Glo.GenerHelpCCForm("选择模版", "http://ccform.mydoc.io/?v=5769&t=36729", "")));

		//this.Pub1.append(AddTR();
		//this.Pub1.append(AddTDGroupTitle("colspan=2", "事件模版-点击名称选用它");
		//this.Pub1.append(AddTREnd();

		this.Pub1.append(AddTR());
		ListBox lb = new ListBox();
		//lb.Style["width"] = "100%";
		lb.addAttr("width","100%");
		//lb.AutoPostBack = false;
		lb.setId("LBReTemplete");
		lb.setHeight(250);

		RegularExpressions res = new RegularExpressions();
		res.RetrieveAll();
		for (XmlEn item : RegularExpression.convertXmlEns(res))
		{
			RegularExpression as = (RegularExpression) item;
			ListItem li = new ListItem(as.getName() + "->" + as.getNote(), as.getNo());
			lb.Items.add(li);
		}
		this.Pub1.append(AddTD("colspan=2", lb.toString().replaceAll("multiple", "multiplex")));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		//LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
		//btn.Click += new EventHandler(btn_SaveReTemplete_Click);
		//btn.setHref("btn_SaveReTemplete_Click");
		String btn = "<input type=\"button\" onclick=\"btn_SaveReTemplete_Click()\" value=\"保存\">";

		this.Pub1.append(AddTDBegin("colspan=2"));
		//this.Pub1.append(AddTD("colspan=1 width='80'", btn);
		this.Pub1.append(btn);
		this.Pub1.append(AddSpace(2));
		this.Pub1.append(Add("<input type='button' value='返回' onclick=back('../Admin/FoolFormDesigner/MapExt/RegularExpression.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&OperAttrKey=" + this.getOperAttrKey() + "&DoType=New')>"));
		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());

	}
	
	public final int BindRegularExpressionEditExt(int idx, String myEvent)
	{
		// 查询.
		MapExt me = new MapExt();
		me.setFK_MapData(this.getFK_MapData());
		me.setTag(myEvent);
		me.setAttrOfOper(this.RefNo);
		me.setMyPK(me.getFK_MapData() + "_" + this.RefNo + "_" + this.getExtType() + "_" + myEvent);
		me.RetrieveFromDBSources();

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx));
		this.Pub1.append(AddTD("style='font-size:12px'", myEvent));

		TextBox tb = new TextBox();
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setId("TB_Doc_" + myEvent);
		tb.setText(me.getDoc());
		tb.setColumns(50);
		tb.setRows(1);
		//tb.Style.Add("width", "99%");
		tb.addAttr("width", "99%");
		this.Pub1.append(AddTD(tb));

		tb = new TextBox();
		tb.setId("TB_Tag1_" + myEvent);
		tb.setText(me.getTag1());
		tb.setColumns(20);
		tb.setRows(3);
		//tb.Style.Add("width", "99%");
		tb.addAttr("width", "99%");
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTREnd());
		idx = idx + 1;
		return idx;
	}

}
