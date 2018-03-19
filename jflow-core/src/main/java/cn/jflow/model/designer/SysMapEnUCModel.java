package cn.jflow.model.designer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrOfOneVSM;
import BP.En.Attrs;
import BP.En.AttrsOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.FieldTypeS;
import BP.En.RefMethod;
import BP.En.RefMethods;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.SystemConfig;
import BP.Sys.XML.AttrDesc;
import BP.Sys.XML.AttrDescAttr;
import BP.Sys.XML.AttrDescs;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class SysMapEnUCModel extends BaseModel{


	public GroupField currGF = new GroupField();
	public MapDtls dtls;
	private GroupFields gfs;
	public int rowIdx = 0;
	public boolean isLeftNext = true;
	
	private List<BaseWebControl> Controls;
	
	private boolean isPostBack; 
	


	public boolean isPostBack() {
		return isPostBack;
	}
	
	


	public void setPostBack(boolean isPostBack) {
		this.isPostBack = isPostBack;
	}


	public StringBuffer Pub1=null;

	public SysMapEnUCModel(HttpServletRequest request,
					HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuffer();
	}


	/**
	 * @param en
	 * @param enName
	 */
	public void BindColumn2_del(Entity en, String enName)
	{
		this.HisEn = en;
		currGF = new GroupField();
		MapAttrs mattrs = new MapAttrs(enName);
		gfs = new GroupFields(enName);
		dtls = new MapDtls(enName);
		this.
		Pub1.append("<table width='100%' class=Table >");
		for (GroupField gf: gfs.ToJavaList())
		{
			currGF = gf;
			Pub1.append(this.AddTR());
			if (gfs.size() == 1)
				Pub1.append(this.AddTD("colspan=2 class=GroupField valign='top' align=left ", "<div style='text-align:left; float:left'>&nbsp;" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			else
				Pub1.append(this.AddTD("colspan=2 class=GroupField valign='top' align=left  onclick=\"GroupBarClick('" + gf.getIdx() + "')\"  ", "<div style='text-align:left; float:left'>&nbsp;<img src='./Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "' border=0 />" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			Pub1.append(this.AddTREnd());

			boolean isHaveH = false;
			int i = -1;
			int idx = -1;
			isLeftNext = true;
			rowIdx = 0;
			for (MapAttr attr: mattrs.ToJavaList())
			{

				if (attr.getGroupID() != gf.getOID())
				{
					if (gf.getIdx() == 0 && attr.getGroupID() == 0)
					{

					}
					else
						continue;
				}

				if (attr.getHisAttr().getIsRefAttr() || attr.getUIVisible() == false)
					continue;

				if (isLeftNext == true)
					this.InsertObjects2Col(true);

				rowIdx++;
				Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "'"));

				// 显示的顺序号.
				idx++;
				if (attr.getIsBigDoc() || attr.getUIIsLine())
				{

					if (attr.getUIIsEnable())
						Pub1.append("<TD  colspan=2 width='100%' valign=top align=left>" + attr.getName() + "<br>");
					else
						Pub1.append("<TD  colspan=2 width='100%' valign=top class=TBReadonly>" + attr.getName() + "<br>");

					TextBox mytbLine = new TextBox();
					if (attr.getIsBigDoc())
						mytbLine.setTextMode(TextBoxMode.MultiLine);

					mytbLine.setId("TB_" + attr.getKeyOfEn());
					mytbLine.setName("TB_" + attr.getKeyOfEn());
					if (attr.getIsBigDoc())
					{
						//  mytbLine.Rows = 8;
						mytbLine.addAttr("width", "100%");
						// mytbLine.Columns = 100;
					}

					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());
					
					Pub1.append(mytbLine);
					Pub1.append(AddTDEnd());
					Pub1.append(AddTREnd());
					rowIdx++;
					continue;
				}

				int colspanOfCtl = 1;
				TextBox tb = new TextBox();
				tb.addAttr("width", "100%");
				tb.setCols(60);
				tb.setId("TB_" + attr.getKeyOfEn());
				tb.setName("TB_" + attr.getKeyOfEn());
				BaseWebControl ctl = tb;

				if(attr.getLGType()==FieldTypeS.Normal){
					tb.setEnabled(attr.getUIIsEnable());
					switch (attr.getMyDataType())
					{
					case BP.DA.DataType.AppString:
						tb.setShowType(TBType.TB);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						break;
					case BP.DA.DataType.AppDate:
						tb.setShowType( TBType.Date);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						//if (attr.UIIsEnable)
						//    tb.Attributes["onfocus"] = "WdatePicker();";

						break;
					case BP.DA.DataType.AppDateTime:
						tb.setShowType(TBType.DateTime);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						//if (attr.UIIsEnable)
						//    tb.Attributes["onfocus"] = "CalendarHM();";

						break;
					case BP.DA.DataType.AppBoolean:
						CheckBox cb = new CheckBox();
						cb.setText(attr.getName());
						cb.setId("CB_" + attr.getKeyOfEn());
						cb.setName("CB_" + attr.getKeyOfEn());
						cb.setChecked(attr.getDefValOfBool());
						cb.setEnabled(attr.getUIIsEnable());
						cb.setChecked(en.GetValBooleanByKey(attr.getKeyOfEn()));
						Pub1.append(this.AddTD("colspan=2", cb));
						continue;
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppFloat:
					case BP.DA.DataType.AppInt:
						tb.setShowType(TBType.Num);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						break;
					case BP.DA.DataType.AppMoney:
					case BP.DA.DataType.AppRate:
						tb.setShowType(TBType.Moneny);
						tb.setText(""+Float.parseFloat(en.GetValStrByKey(attr.getKeyOfEn())));
						break;
					default:
						break;
					}
					tb.addAttr("width", "100%");
					switch (attr.getMyDataType())
					{
					case BP.DA.DataType.AppString:
					case BP.DA.DataType.AppDateTime:
					case BP.DA.DataType.AppDate:
						if (tb.getEnabled())
							tb.addAttr("class", "TB");
						else
							tb.addAttr("class", "TBReadonly");
						break;
					default:
						if (tb.getEnabled())
							tb.addAttr("class", "TBNum");
						else
							tb.addAttr("class", "TBNumReadonly");
						break;
					}
				}
				else if(attr.getLGType()==FieldTypeS.Enum){
					DDL ddle = new DDL();
					ddle.setId("DDL_" + attr.getKeyOfEn());
					ddle.setName("DDL_" + attr.getKeyOfEn());
					ddle.BindSysEnum(attr.getKeyOfEn());
					ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					ddle.setEnabled(attr.getUIIsEnable());
					ctl = ddle;
				}
				else if(attr.getLGType()==FieldTypeS.FK){
					DDL ddl1 = new DDL();
					ddl1.setId("DDL_" + attr.getKeyOfEn());
					ddl1.setName("DDL_" + attr.getKeyOfEn());
					try
					{
						EntitiesNoName ens = attr.getHisEntitiesNoName();
						ens.RetrieveAll();
						ddl1.BindEntities(ens);
						ddl1.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					}
					catch(Exception e)
					{
					}
					ddl1.setEnabled(attr.getUIIsEnable());
					ctl = ddl1;
					// this.AddTD("colspan=" + colspanOfCtl, ddl1);
				}
				

				String desc = attr.getName().replace("：", "");
				desc = desc.replace(":", "");
				desc = desc.replace(" ", "");

				if (desc.length() >= 5)
				{
					Pub1.append("<TD colspan=2 class=TBReadonly>" + desc + "<br>");
					Pub1.append(ctl);
					Pub1.append(this.AddTREnd());
				}
				else
				{
					Pub1.append(this.AddTDDesc(desc));
					Pub1.append(this.AddTD(ctl));
					Pub1.append(this.AddTREnd());
				}
			}
			//  this.InsertObjects(false);
		}
		Pub1.append(this.AddTableEnd());


		String js = "\t\n<script type='text/javascript' >";
		for (MapDtl dtl: dtls.ToJavaList())
		{
			js += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.getNo() + "','TD" + dtl.getNo() + "')\", 200);";
		}
		js += "\t\n</script>";
		Pub1.append(js);


		js = "\t\n<script type='text/javascript' >";
		js += "\t\n function SaveDtl(dtl) { ";
		js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveDtlData(); ";
		js += "\t\n } ";
		js += "\t\n</script>";
		Pub1.append(js);
	}

	public void BindColumn2_bak(Entity en, String enName)
	{
		this.HisEn = en;
		currGF = new GroupField();
		MapAttrs mattrs = new MapAttrs(enName);
		gfs = new GroupFields(enName);
		dtls = new MapDtls(enName);
		Pub1.append("<table width=100% class=Table>");
		for (GroupField gf: gfs.ToJavaList())
		{
			currGF = gf;
			Pub1.append(this.AddTR());
			if (gfs.size() == 1)
				Pub1.append(this.AddTD("colspan=2 class=GroupField valign='top' align=left ", "<div style='text-align:left; float:left'>&nbsp;" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			else
				Pub1.append(this.AddTD("colspan=2 class=GroupField valign='top' align=left  onclick=\"GroupBarClick('" + gf.getIdx() + "')\"  ", "<div style='text-align:left; float:left'>&nbsp;<img src='./Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "' border=0 />" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));

			Pub1.append(this.AddTREnd());

			boolean isHaveH = false;
			int i = -1;
			int idx = -1;
			isLeftNext = true;
			rowIdx = 0;
			for (MapAttr attr: mattrs.ToJavaList())
			{

				if (attr.getGroupID() != gf.getOID())
				{
					if (gf.getIdx() == 0 && attr.getGroupID() == 0)
					{

					}
					else
						continue;
				}

				if (attr.getHisAttr().getIsRefAttr() || attr.getUIVisible() == false)
					continue;

				if (isLeftNext == true)
					this.InsertObjects2Col(true);

				rowIdx++;
				Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "'"));

//				#region 加入字段
				// 显示的顺序号.
				idx++;
				if (attr.getIsBigDoc() || attr.getUIIsLine())
				{

					if (attr.getUIIsEnable())
						Pub1.append("<TD  colspan=2 width='100%' valign=top align=left>" + attr.getName() + "<br>");
					else
						Pub1.append("<TD  colspan=2 width='100%' valign=top class=TBReadonly>" + attr.getName() + "<br>");

					TextBox mytbLine = new TextBox();
					if (attr.getIsBigDoc())
						mytbLine.setTextMode(TextBoxMode.MultiLine);

					mytbLine.setId("TB_" + attr.getKeyOfEn());
					mytbLine.setName("TB_" + attr.getKeyOfEn());
					if (attr.getIsBigDoc())
					{
						mytbLine.setRows(8);
						mytbLine.setCols(100);
					}

					// mytbLine.Attributes["width"] = "100%";

					//mytbLine.Attributes["style"] = "width:100%;padding: 0px;margin: 0px;";
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());

					//if (mytbLine.Enabled == false)
					//    mytbLine.Attributes["class"] = "TBReadonly";

					Pub1.append(mytbLine);
					Pub1.append(this.AddTDEnd());
					Pub1.append(this.AddTREnd());
					rowIdx++;
					continue;
				}

				int colspanOfCtl = 1;
				TextBox tb = new TextBox();
				tb.addAttr("width", "100%");
				tb.setCols(60);
				tb.setId("TB_" + attr.getKeyOfEn());
				tb.setName("TB_" + attr.getKeyOfEn());
				BaseWebControl ctl = tb;

				if(attr.getLGType()==FieldTypeS.Normal){
					tb.setEnabled(attr.getUIIsEnable());
					switch (attr.getMyDataType())
					{
					case BP.DA.DataType.AppString:
						tb.setShowType(TBType.TB);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						break;
					case BP.DA.DataType.AppDate:
						tb.setShowType(TBType.Date);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						//if (attr.UIIsEnable)
						//    tb.Attributes["onfocus"] = "WdatePicker();";

						break;
					case BP.DA.DataType.AppDateTime:
						tb.setShowType(TBType.DateTime);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						//if (attr.UIIsEnable)
						//    tb.Attributes["onfocus"] = "CalendarHM();";

						break;
					case BP.DA.DataType.AppBoolean:
						CheckBox cb = new CheckBox();
						cb.setText(attr.getName());
						cb.setId("CB_" + attr.getKeyOfEn());
						cb.setName("CB_" + attr.getKeyOfEn());
						cb.setChecked(attr.getDefValOfBool());
						cb.setEnabled(attr.getUIIsEnable());
						cb.setChecked(en.GetValBooleanByKey(attr.getKeyOfEn()));
						Pub1.append(this.AddTD("colspan=2", cb));
						continue;
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppFloat:
					case BP.DA.DataType.AppInt:
						tb.setShowType(TBType.Num);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						break;
					case BP.DA.DataType.AppMoney:
					case BP.DA.DataType.AppRate:
						tb.setShowType(TBType.Moneny);
						tb.setText(""+Float.parseFloat(en.GetValStrByKey(attr.getKeyOfEn())));
						break;
					default:
						break;
					}
					tb.addAttr("width", "100%");
					switch (attr.getMyDataType())
					{
					case BP.DA.DataType.AppString:
					case BP.DA.DataType.AppDateTime:
					case BP.DA.DataType.AppDate:
						if (tb.getEnabled())
							tb.addAttr("class", "TB");
						else
							tb.addAttr("class","TBReadonly");
						break;
					default:
						if (tb.getEnabled())
							tb.addAttr("class","TBNum");
						else
							tb.addAttr("class","TBNumReadonly");
						break;
					}
				}
				else if(attr.getLGType()==FieldTypeS.Enum){
					DDL ddle = new DDL();
					ddle.setId("DDL_" + attr.getKeyOfEn());
					ddle.setName("DDL_" + attr.getKeyOfEn());
					ddle.BindSysEnum(attr.getKeyOfEn());
					ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					ddle.setEnabled(attr.getUIIsEnable());
					ctl = ddle;
				}
				else if(attr.getLGType()==FieldTypeS.FK){
					DDL ddl1 = new DDL();
					ddl1.setId("DDL_" + attr.getKeyOfEn());
					ddl1.setName("DDL_" + attr.getKeyOfEn());
					try
					{
						EntitiesNoName ens = attr.getHisEntitiesNoName();
						ens.RetrieveAll();
						ddl1.BindEntities(ens);
						ddl1.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					}
					catch(Exception e)
					{
					}
					ddl1.setEnabled(attr.getUIIsEnable());
					ctl = ddl1;
					// this.AddTD("colspan=" + colspanOfCtl, ddl1);
				}

				String desc = attr.getName().replace("：", "");
				desc = desc.replace(":", "");
				desc = desc.replace(" ", "");

				if (desc.length() >= 5)
				{
					Pub1.append("<TD colspan=2 class=TBReadonly>" + desc + "<br>");
					Pub1.append(ctl);
					Pub1.append(this.AddTREnd());
				}
				else
				{
					Pub1.append(this.AddTDDesc(desc));
					Pub1.append(this.AddTD(ctl));
					Pub1.append(this.AddTREnd());
				}
			}
			//  this.InsertObjects(false);
		}
		Pub1.append(this.AddTableEnd());


		String js = "\t\n<script type='text/javascript' >";
		for (MapDtl dtl: dtls.ToJavaList())
		{
			js += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.getNo() + "','TD" + dtl.getNo() + "')\", 200);";
		}
		js += "\t\n</script>";
		Pub1.append(js);


		js = "\t\n<script type='text/javascript' >";
		js += "\t\n function SaveDtl(dtl) { ";
		js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveDtlData(); ";
		js += "\t\n } ";
		js += "\t\n</script>";
		Pub1.append(js);
	}

	public void InsertObjects2Col(boolean isJudgeRowIdx)
	{
		for (MapDtl dtl: dtls.ToJavaList())
		{
			if (dtl.IsUse)
				continue;

			if (isJudgeRowIdx)
			{
				if (dtl.getRowIdx() != rowIdx)
					continue;
			}

			if (dtl.getGroupID() == 0 && rowIdx == 0)
			{
				dtl.setGroupID(Integer.parseInt(""+currGF.getOID()));
				dtl.setRowIdx(0);
				dtl.Update();
			}
			else if (dtl.getGroupID() == currGF.getOID())
			{

			}
			else
			{
				continue;
			}
			dtl.IsUse = true;
			rowIdx++;
			// myidx++;
			Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			String src = null;////request.ApplicationPath + "WF/Dtl.aspx?EnsName=" + dtl.No + "&RefPKVal=" + this.HisEn.PKVal + "&IsWap=1";
			Pub1.append("<TD colspan=2 class=FDesc ID='TD" + dtl.getNo() + "'><a href='" + src + "'>" + dtl.getName() + "</a></TD>");
			// this.Add("<iframe ID='F" + dtl.No + "' frameborder=0 Onblur=\"SaveDtl('" + dtl.No + "');\" style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' height='10px' scrolling=no  /></iframe>");
			//this.AddTDEnd();
			Pub1.append(this.AddTREnd());
		}
	}
	/// <summary>
	/// 绑定他
	/// </summary>
	/// <param name="en"></param>
	/// <param name="enName"></param>
	public void BindColumn4(Entity en, String enName)
	{
		this.HisEn = en;
		currGF = new GroupField();
		MapAttrs mattrs = new MapAttrs(enName);
		gfs = new GroupFields(enName);
		dtls = new MapDtls(enName);

		Pub1.append("<table class=TableFrom id=tabForm width='900px'  >");
		for (GroupField gf: gfs.ToJavaList())
		{
			currGF = gf;
			Pub1.append(this.AddTR());
			if (gfs.size() == 1)
				Pub1.append(this.AddTD("colspan=4 class=GroupField valign='top' align=left ", "<div style='text-align:left; float:left'>&nbsp;" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			else
				Pub1.append(this.AddTD("colspan=4 class=GroupField valign='top' align=left onclick=\"GroupBarClick('" + gf.getIdx() + "')\"", "<div style='text-align:left; float:left'>&nbsp;<img src='./Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'   border=0 />" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			Pub1.append(this.AddTREnd());

			boolean isHaveH = false;
			int i = -1;
			int idx = -1;
			isLeftNext = true;
			rowIdx = 0;
			for (MapAttr attr: mattrs.ToJavaList())
			{

				if (attr.getGroupID() != gf.getOID())
				{
					if (gf.getIdx() == 0 && attr.getGroupID() == 0)
					{

					}
					else
						continue;
				}

				if (attr.getHisAttr().getIsRefAttr() || attr.getUIVisible() == false)
					continue;

				if (isLeftNext == true)
					this.InsertObjects(true);

				// 显示的顺序号.
				idx++;
				if (attr.getIsBigDoc() && attr.getUIIsLine())
				{
					if (isLeftNext == false)
					{
						Pub1.append(this.AddTD());
						Pub1.append(this.AddTD());
						Pub1.append(this.AddTREnd());
						rowIdx++;
					}
					rowIdx++;
					Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "'"));
					if (attr.getUIIsEnable())
						Pub1.append("<TD  colspan=4 width='100%' valign=top align=left>" + attr.getName());
					else
						Pub1.append("<TD  colspan=4 width='100%' valign=top class=TBReadonly>" + attr.getName());


					TextBox mytbLine = new TextBox();
					mytbLine.setTextMode(TextBoxMode.MultiLine);
					mytbLine.setId("TB_" + attr.getKeyOfEn());
					mytbLine.setName("TB_" + attr.getKeyOfEn());
					mytbLine.setRows(8);
					mytbLine.addAttr("style", "width:100%;padding: 0px;margin: 0px;");
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());
					if (mytbLine.getEnabled() == false)
						mytbLine.addAttr("class", "TBReadonly");

					Pub1.append(mytbLine);
					Pub1.append(this.AddTDEnd());
					Pub1.append(this.AddTREnd());
					rowIdx++;
					isLeftNext = true;
					continue;
				}

				if (attr.getIsBigDoc())
				{
					if (isLeftNext)
					{
						rowIdx++;
						Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
					}

					Pub1.append("<TD class=FDesc colspan=2>");
					Pub1.append(attr.getName());
					TextBox mytbLine = new TextBox();
					mytbLine.setId("TB_" + attr.getKeyOfEn());
					mytbLine.setName("TB_" + attr.getKeyOfEn());
					mytbLine.setTextMode(TextBoxMode.MultiLine);
					mytbLine.setRows(8);
					mytbLine.setCols(30);
					mytbLine.addAttr("style", "width:100%;padding: 0px;margin: 0px;");
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());
					if (mytbLine.getEnabled() == false)
						mytbLine.addAttr("class", "TBReadonly");

					Pub1.append(mytbLine);
					Pub1.append(this.AddTDEnd());

					if (isLeftNext == false)
					{
						Pub1.append(this.AddTREnd());
						rowIdx++;
					}

					isLeftNext = !isLeftNext;
					continue;
				}

				//计算 colspanOfCtl .
				int colspanOfCtl = 1;
				if (attr.getUIIsLine())
					colspanOfCtl = 3;

				if (attr.getUIIsLine())
				{
					if (isLeftNext == false)
					{
						Pub1.append(this.AddTD());
						Pub1.append(this.AddTD());
						Pub1.append(this.AddTREnd());
						rowIdx++;
					}
					isLeftNext = true;
				}

				if (isLeftNext)
				{
					rowIdx++;
					Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
				}

				TextBox tb = new TextBox();
				tb.addAttr("width","100%");
				tb.setCols(60);
				tb.setId("TB_" + attr.getKeyOfEn());
				tb.setName("TB_" + attr.getKeyOfEn());

				if(attr.getLGType()==FieldTypeS.Normal){
					tb.setEnabled(attr.getUIIsEnable());
					switch (attr.getMyDataType())
					{
					case BP.DA.DataType.AppString:
						Pub1.append(this.AddTDDesc(attr.getName()));
						if (attr.getIsSigan())
						{
							Pub1.append(this.AddTD("colspan=" + colspanOfCtl, "<img src='/DataUser/Siganture/" + WebUser.getNo() + ".jpg' border=0 onerror=\"this.src='../Data/Siganture/UnName.jpg'\"/>"));
						}
						else
						{
							tb.setShowType(TBType.TB);
							try
							{
								tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
							}
							catch(Exception e)
							{
								tb.setText(attr.getKeyOfEn());
							}
							Pub1.append(this.AddTD("colspan=" + colspanOfCtl, tb));
						}
						break;
					case BP.DA.DataType.AppDate:
						Pub1.append(this.AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Date);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable())
							tb.addAttr("onfocus", "WdatePicker();");

						Pub1.append(this.AddTD("colspan=" + colspanOfCtl, tb));
						break;
					case BP.DA.DataType.AppDateTime:
						Pub1.append(this.AddTDDesc(attr.getName()));
						tb.setShowType(TBType.DateTime);
						tb.setText(en.GetValStringByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable())
							tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");

						Pub1.append(this.AddTD("colspan=" + colspanOfCtl, tb));
						break;
					case BP.DA.DataType.AppBoolean:
						Pub1.append(this.AddTDDesc(""));
						CheckBox cb = new CheckBox();
						cb.setText(attr.getName());
						cb.setId("CB_" + attr.getKeyOfEn());
						cb.setName("CB_" + attr.getKeyOfEn());
						cb.setChecked(attr.getDefValOfBool());
						cb.setEnabled(attr.getUIIsEnable());
						cb.setChecked(en.GetValBooleanByKey(attr.getKeyOfEn()));
						Pub1.append(this.AddTD("colspan=" + colspanOfCtl, cb));
						break;
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppFloat:
					case BP.DA.DataType.AppInt:
						Pub1.append(this.AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Num);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						Pub1.append(this.AddTD("colspan=" + colspanOfCtl, tb));
						break;

					case BP.DA.DataType.AppMoney:
					case BP.DA.DataType.AppRate:
						Pub1.append(this.AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Moneny);
						tb.setText(""+Float.parseFloat(en.GetValStrByKey(attr.getKeyOfEn())));
						Pub1.append(this.AddTD("colspan=" + colspanOfCtl, tb));
						break;
					default:
						break;
					}
					tb.addAttr("width","100%");
					switch (attr.getMyDataType())
					{
					case BP.DA.DataType.AppString:
					case BP.DA.DataType.AppDateTime:
					case BP.DA.DataType.AppDate:
						if (tb.getEnabled())
							tb.addAttr("class","TB");
						else
							tb.addAttr("class","TBReadonly");
						break;
					default:
						if (tb.getEnabled())
							tb.addAttr("class","TBNum");
						else
							tb.addAttr("class","TBNumReadonly");
						break;
					}
				}
				else if(attr.getLGType()==FieldTypeS.Enum){
					Pub1.append(this.AddTDDesc(attr.getName()));
					DDL ddle = new DDL();
					ddle.setId("DDL_" + attr.getKeyOfEn());
					ddle.setName("DDL_" + attr.getKeyOfEn());
					ddle.BindSysEnum(attr.getKeyOfEn());
					ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					ddle.setEnabled(attr.getUIIsEnable());
					Pub1.append(this.AddTD("colspan=" + colspanOfCtl, ddle));
				}
				else if(attr.getLGType()==FieldTypeS.FK){
					Pub1.append(this.AddTDDesc(attr.getName()));
					DDL ddl1 = new DDL();
					ddl1.setId("DDL_" + attr.getKeyOfEn());
					ddl1.setName("DDL_" + attr.getKeyOfEn());
					try
					{
						EntitiesNoName ens = attr.getHisEntitiesNoName();
						ens.RetrieveAll();
						ddl1.BindEntities(ens);
						ddl1.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					}
					catch(Exception e)
					{
					}
					ddl1.setEnabled(attr.getUIIsEnable());
					Pub1.append(this.AddTD("colspan=" + colspanOfCtl, ddl1));
				}



				if (colspanOfCtl == 3)
				{
					isLeftNext = true;
					Pub1.append(this.AddTREnd());
					continue;
				}

				if (isLeftNext == false)
				{
					isLeftNext = true;
					Pub1.append(this.AddTREnd());
					continue;
				}
				isLeftNext = false;

			}
			// 最后处理补充上它。
			if (isLeftNext == false)
			{
				Pub1.append(this.AddTD());
				Pub1.append(this.AddTD());
				Pub1.append(this.AddTREnd());
			}
			this.InsertObjects(false);

		}
		Pub1.append(this.AddTableEnd());

		String js = "\t\n<script type='text/javascript' >";
		for (MapDtl dtl: dtls.ToJavaList())
		{
			js += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.getNo() + "','TD" + dtl.getNo() + "')\", 200);";
		}
		js += "\t\n</script>";
		Pub1.append(js);


		js = "\t\n<script type='text/javascript' >";
		js += "\t\n function SaveDtl(dtl) { ";
		js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveDtlData(); ";
		js += "\t\n } ";
		js += "\t\n</script>";
		Pub1.append(js);
	}

	public void InsertObjects(boolean isJudgeRowIdx)
	{
		for (MapDtl dtl: dtls.ToJavaList())
		{
			if (dtl.IsUse)
				continue;

			if (isJudgeRowIdx)
			{
				if (dtl.getRowIdx() != rowIdx)
					continue;
			}

			if (dtl.getGroupID() == 0 && rowIdx == 0)
			{
				dtl.setGroupID(Integer.parseInt(""+currGF.getOID()));
				dtl.setRowIdx(0);
				dtl.Update();
			}
			else if (dtl.getGroupID() == currGF.getOID())
			{

			}
			else
			{
				continue;
			}
			dtl.IsUse = true;
			rowIdx++;
			// myidx++;
			Pub1.append(this.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			Pub1.append("<TD colspan=4 ID='TD" + dtl.getNo() + "' height='50px'>");
			String src = this.get_request().getRealPath("/") + "WF/Dtl.aspx?EnsName=" + dtl.getNo() + "&RefPKVal=" + this.HisEn.getPKVal();
			Pub1.append("<iframe ID='F" + dtl.getNo() + "' frameborder=0 Onblur=\"SaveDtl('" + dtl.getNo()+ "');\" style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' height='10px' scrolling=no  /></iframe>");
			Pub1.append(this.AddTDEnd());
			Pub1.append(this.AddTREnd());
		}
	}

	public static String GetRefstrs(String keys, Entity en, Entities hisens)
	{
		String refstrs = "";
		String path = null;//System.Web.HttpContext.Current.Request.ApplicationPath;
		int i = 0;

		AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
		if (oneVsM.size() > 0)
		{
			for(AttrOfOneVSM vsM:oneVsM)
			{
				//  string url = path + "/Comm/UIEn1ToM.aspx?EnsName=" + en.ToString() + "&AttrKey=" + vsM.EnsOfMM.ToString() + keys;
				String url = "UIEn1ToM.aspx?EnsName=" + en.toString() + "&AttrKey=" + vsM.getEnsOfMM().toString() + keys;
				try
				{
					try
					{
						i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'");
					}
					catch(Exception e)
					{
						i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "=" + en.getPKVal());
					}
				}
				catch (Exception ex)
				{
					vsM.getEnsOfMM().getGetNewEntity().CheckPhysicsTable();
					ex.printStackTrace();
				}

				if (i == 0)
					refstrs += "[<a href=\"javascript:WinShowModalDialog('" + url + "','onVsM'); \"  >" + vsM.getDesc() + "</a>]";
				else
					refstrs += "[<a href=\"javascript:WinShowModalDialog('" + url + "','onVsM'); \"  >" + vsM.getDesc() + "-" + i + "</a>]";
			}
		}

		RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
		if (myreffuncs.size() > 0)
		{
			for(RefMethod func:myreffuncs)
			{
				if (func.Visable == false)
					continue;

				// string url = path + "/Comm/RefMethod.aspx?Index=" + func.Index + "&EnsName=" + hisens.ToString() + keys;
				String url = "../Comm/RefMethod.aspx?Index=" + func.Index + "&EnsName=" + hisens.toString() + keys;
				if (func.Warning == null)
				{
					if (func.Target == null)
						refstrs += "[" + func.GetIcon(path) + "<a href='" + url + "' ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>]";
					else
						refstrs += "[" + func.GetIcon(path) + "<a href=\"javascript:WinOpen('" + url + "','" + func.Target + "')\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>]";
				}
				else
				{
					if (func.Target == null)
						refstrs += "[" + func.GetIcon(path) + "<a href=\"javascript: if ( confirm('" + func.Warning + "') ) { window.location.href='" + url + "' }\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>]";
					else
						refstrs += "[" + func.GetIcon(path) + "<a href=\"javascript: if ( confirm('" + func.Warning + "') ) { WinOpen('" + url + "','" + func.Target + "') }\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>]";
				}
			}
		}

		EnDtls enDtls = en.getEnMap().getDtls();
		//  string path = this.Request.ApplicationPath;
		if (enDtls.size() > 0)
		{
			for(EnDtl enDtl:enDtls)
			{
				//string url = path + "/Comm/UIEnDtl.aspx?EnsName=" + enDtl.EnsName + "&Key=" + enDtl.RefKey + "&Val=" + en.PKVal.ToString() + "&MainEnsName=" + en.ToString() + keys;
				String url = path + "/Comm/UIEnDtl.aspx?EnsName=" + enDtl.getEnsName() + "&RefKey=" + enDtl.getRefKey() + "&RefVal=" + en.getPKVal().toString() + "&MainEnsName=" + en.toString();
				try
				{
					try
					{
						i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
					}
					catch(Exception e)
					{
						i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
					}
				}
				catch (Exception ex)
				{
					enDtl.getEns().getGetNewEntity().CheckPhysicsTable();
					ex.printStackTrace();
				}

				if (i == 0)
					refstrs += "[<a href=\"javascript:WinOpen('" + url + "', 'dtl" + enDtl.getRefKey() + "'); \" >" + enDtl.getDesc() + "</a>]";
				else
					refstrs += "[<a href=\"javascript:WinOpen('" + url + "', 'dtl" + enDtl.getRefKey() + "'); \"  >" + enDtl.getDesc() + "-" + i + "</a>]";
			}
		}
		return refstrs;
	}

	public void AddContral()
	{
		
		this.Pub1.append("<td class='FDesc' nowrap width=1% ></td>");
		this.Pub1.append("<td class='TD' nowrap ></TD>");
	}
	public void AddContral(String desc, CheckBox cb)
	{
		this.Pub1.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
		this.Pub1.append("<td class='TD' nowrap >");
		this.Pub1.append(cb);
		this.Pub1.append("</td>");
	}
	public void AddContral(String desc, CheckBox cb, int colspan)
	{
	
		this.Pub1.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
		this.Pub1.append("<td class='TD' nowrap colspan='" + colspan + "'>");
		this.Pub1.append(cb);
		this.Pub1.append("</td>");
	}
	//		public void AddContral(string desc, string val)
	public void AddContral(String desc, String val)
	{
		Pub1.append("<TD class='FDesc' > " + desc + "</TD>");
		Pub1.append("<TD class='TD' > " + val + "</TD>");
	}
	public void AddContral(String desc, TextBox tb, String helpScript)
	{
		if (tb.getReadOnly())
		{
			if ("TBNum".equals(tb.attributes.get("Class")))
				tb.addAttr("Class", "TBNumReadonly"); 
			else
				tb.addAttr("Class", "TBReadonly");
		}

		//if (tb.ReadOnly == false)
		//    desc += "<font color=red><b>*</b></font>";

		//  tb.Attributes["style"] = "width=100%;height=100%";
		if (tb.getTextMode() == TextBoxMode.MultiLine)
		{
			AddContralDoc(desc, tb);
			return;
		}

		tb.addAttr("Width","80%");

		Pub1.append("<td class='FDesc' nowrap width=1% >" + desc + "</td>");
		Pub1.append("<td class='TD' nowrap  >" + helpScript);
		Pub1.append(tb);
		Pub1.append(this.AddTDEnd()); // ("</td>");
	}
	public void AddContral(String desc, TextBox tb, String helpScript, int colspan)
	{
		if (tb.getReadOnly())
		{
			if ("TBNum".equals(tb.attributes.get("Class")))
				tb.addAttr("Class","TBNumReadonly");
			else
				tb.addAttr("Class","TBReadonly");
		}

		//if (tb.ReadOnly == false)
		// desc += "<font color=red><b>*</b></font>";

		//tb.Attributes["style"] = "width=100%;height=100%";
		if (tb.getTextMode() == TextBoxMode.MultiLine)
		{
			AddContralDoc(desc, tb);
			return;
		}

		//   tb.Attributes["Width"] = "30%";

		Pub1.append("<td class='FDesc' nowrap width=1% >" + desc + "</td>");

		if (colspan < 3)
		{
			Pub1.append("<td class='TD' nowrap colspan=" + colspan + " width='30%' >" + helpScript);
		}
		else
		{
			Pub1.append("<td class='TD' nowrap colspan=" + colspan + " width='80%' >" + helpScript);
		}

		Pub1.append(tb);
		Pub1.append(this.AddTDEnd()); // ("</td>");
	}
	public void AddContral(String desc, TextBox tb, int colSpanOfCtl)
	{
		if (tb.getReadOnly())
		{
			if ("TBNum".equals(tb.attributes.get("Class")))
				tb.addAttr("Class", "TBNumReadonly");
			else
				tb.addAttr("Class", "TBReadonly");
		}

		//if (tb.ReadOnly == false)
		//    desc += "<font color=red><b>*</b></font>";


		// tb.Attributes["style"] = "width=100%;height=100%";
		if (tb.getTextMode() == TextBoxMode.MultiLine)
		{
			AddContralDoc(desc, tb, colSpanOfCtl);
			return;
		}

		Pub1.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");

		if (colSpanOfCtl < 3)
			Pub1.append("<td class='TD' nowrap colspan=" + colSpanOfCtl + " width='30%' >");
		else
			Pub1.append("<td class='TD' nowrap colspan=" + colSpanOfCtl + " width='80%' >");

		Pub1.append(tb);
		Pub1.append(this.AddTDEnd());
	}
	/// <summary>
	/// 增加空件
	/// </summary>
	/// <param name="desc"></param>
	/// <param name="tb"></param>
	public void AddContral(String desc, TextBox tb)
	{
		if (tb.getReadOnly())
		{
			if ("TBNum".equals(tb.attributes.get("Class")))
				tb.addAttr("Class","TBNumReadonly");
			else
				tb.addAttr("Class","TBReadonly");
		}

		//if (tb.ReadOnly == false)
		//    desc += "<font color=red><b>*</b></font>";

		tb.addAttr("style","width=100%");
		if (tb.getTextMode() == TextBoxMode.MultiLine)
		{
			AddContralDoc(desc, tb);
			return;
		}

		Pub1.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");

		Pub1.append("<td class='TD' nowrap width='30%'>");
		Pub1.append(tb);
		Pub1.append(this.AddTDEnd()); // ("</td>");
	}
	//		public void AddContralDoc(string desc, TB tb)
	public void AddContralDoc(String desc, TextBox tb)
	{
		//if (desc.Length>
		Pub1.append("<td class='FDesc'  colspan='2' nowrap height='100px' width='50%' >" + desc + "<br>");
		if (tb.getReadOnly())
			tb.addAttr("Class","TBReadonly");
		Pub1.append(tb);
		Pub1.append("</td>");
	}
	public void AddContralDoc(String desc, TextBox tb, int colspanOfctl)
	{
		//if (desc.Length>
		Pub1.append("<td class='FDesc'  colspan='" + colspanOfctl + "' nowrap height='100px' width='500px' >" + desc + "<br>");
		if (tb.getReadOnly())
			tb.addAttr("Class","TBReadonly");
		tb.addAttr("style","");
		Pub1.append(tb);
		Pub1.append("</td>");
	}
	//		public void AddContralDoc(string desc, int colspan, TB tb)
	public void AddContralDoc(String desc, int colspan, TextBox tb)
	{
		Pub1.append("<td class='FDesc'  colspan='" + colspan + "' nowrap width=1%  height='100px'  >" + desc + "<br>");
		if (tb.getReadOnly())
			tb.setEnsName("TBReadonly");
		Pub1.append(tb);
		Pub1.append("</td>");
	}

	
	private boolean isReadonly;
	
	private boolean isShowDtl;
	
	public boolean isReadonly() {
		return isReadonly;
	}


	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}


	


	public boolean isShowDtl() {
		return isShowDtl;
	}


	public void setShowDtl(boolean isShowDtl) {
		this.isShowDtl = isShowDtl;
	}


	//	public boolean IsReadonly
//	{
//		get
//		{
//			return (bool)this.ViewState["IsReadonly"];
//		}
//		set
//		{
//			ViewState["IsReadonly"] = value;
//		}
//	}
//	public bool IsShowDtl
//	{
//		get
//		{
//			return (bool)this.ViewState["IsShowDtl"];
//		}
//		set
//		{
//			ViewState["IsShowDtl"] = value;
//		}
//	}
	public void SetValByKey(String key, String val)
	{
		TextBox tb = new TextBox();
		tb.setId("TB_" + key);
		tb.setName("TB_" + key);
		tb.setText(val);
		tb.setVisible(false);
		this.Controls.add(tb);
		Pub1.append(tb);
	}
	public Object GetValByKey(String key)
	{
		UiFatory uf=new UiFatory();
		TextBox en = (TextBox)uf.GetUIByID("TB_" + key);
		return en.getText();
	}
	public void BindAttrs(Attrs attrs) throws Exception
	{
		//this.HisEn =en;
		boolean isReadonly = false;
//		this.IsReadonly = false;
//		this.IsShowDtl = false;
//		this.Controls.Clear();
//		this.Attributes["visibility"] = "hidden";
		//this.Height=0;
		//this.Width=0;
		this.Controls.clear();
		Pub1.append("<table width='100%' id='a1' border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
		boolean isLeft = true;
		Object val = null;
		boolean isAddTR = true;
		for(Attr attr:attrs)
		{
			if (attr.getUIVisible() == false)
				continue;

			if ("MyNum".equals(attr.getKey()))
				continue;

			if (isLeft && isAddTR)
			{
				Pub1.append(this.AddTR());
			}

			isAddTR = true;
			val = attr.getDefaultVal();
			if (attr.getUIContralType() == UIContralType.TB)
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					this.SetValByKey(attr.getKey(), val.toString());
					isAddTR = false;
					continue;
				}
				else if (attr.getMyFieldType() == FieldType.MultiValues)
				{
					/* 如果是多值的.*/
					
					ListBox lb = new ListBox(attr);
					lb.setVisible(true);
					int i=128;
					lb.setHeight(i);
					lb.setSelectionMode(ListSelectionMode.Multiple);
					Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
					ens.RetrieveAll();
					this.Controls.add(lb);
					Pub1.append(lb);
				}
				else
				{
					if (attr.getUIVisible() == false)
					{

						TextBox tb = new TextBox();
						tb.LoadMapAttr(attr);
						tb.setId("TB_" + attr.getKey());
						tb.setName("TB_" + attr.getKey());
						tb.addAttr("Visible","false");
						this.Controls.add(tb);
						Pub1.append(tb);
						//this.AddContral(attr.Desc,area);
						//this.SetValByKey(attr.Key, val.ToString() );
						continue;
					}
					else
					{
						if (attr.getUIHeight() != 0)
						{
							TextBox area = new TextBox();
							area.LoadMapAttr(attr);
							area.setId("TB_" + attr.getKey());
							area.setName("TB_" + attr.getKey());
							area.setText(val.toString());
							area.setRows(8);
							area.setTextMode(TextBoxMode.MultiLine);
							if (isReadonly)
								area.setEnabled(false);
							this.AddContral(attr.getDesc(), area);
						}
						else
						{
							TextBox tb = new TextBox();
							tb.LoadMapAttr(attr);

							tb.setId("TB_" + attr.getKey());
							tb.setName("TB_" + attr.getKey());
							if (isReadonly)
								tb.setEnabled(false);
							switch (attr.getMyDataType())
							{
							case DataType.AppMoney:
								tb.setText(""+Float.parseFloat(val.toString()));
								break;
							default:
								tb.setText(val.toString());
								break;
							}
							tb.addAttr("width","100%");
							this.AddContral(attr.getDesc(), tb);
						}
					}
				}
			}
			else if (attr.getUIContralType() == UIContralType.CheckBok)
			{
				CheckBox cb = new CheckBox();
				if ("1".equals(attr.getDefaultVal().toString()))
					cb.setChecked(true);
				else
					cb.setChecked(false);

				if (isReadonly)
					cb.setEnabled(false);
				else
					cb.setEnabled(attr.getUIVisible());

				cb.setId("CB_" + attr.getKey());
				cb.setName("CB_" + attr.getKey());
				this.AddContral(attr.getDesc(), cb);
			}
			else if (attr.getUIContralType() == UIContralType.DDL)
			{
				if (isReadonly || !attr.getUIIsReadonly())
				{
					/* 如果是 DDLIsEnable 的, 就要找到. */
					if (attr.getMyFieldType() == FieldType.Enum)
					{
						/* 如果是 enum 类型 */
						int enumKey = 0;
						try
						{
							enumKey = Integer.parseInt(val.toString());
						}
						catch (Exception ex)
						{
							throw new Exception("默认值错误：" + attr.getKey() + " = " + val.toString());
						}

						BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);


						//DDL ddl = new DDL(attr,text,en.Lab,false);
						DDL ddl = new DDL();
						ddl.Items.add(new ListItem(enEnum.getLab(), val.toString()));
						ddl.Items.get(0).setSelected(true);
						ddl.setEnabled(false);
						ddl.setId("DDL_" + attr.getKey());
						ddl.setName("DDL_" + attr.getKey());
						this.AddContral(attr.getDesc(), ddl, true);
						//this.Controls.Add(ddl);
					}
					else
					{
						/* 如果是 ens 类型 */
						Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
						Entity en1 = ens.getGetNewEntity();
						en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
						String lab = "";
						try
						{
							en1.Retrieve();
							lab = en1.GetValStringByKey(attr.getUIRefKeyText());
						}
						catch(Exception e)
						{
							if (SystemConfig.getIsDebug() == false)
							{
								lab = "" + val.toString();
							}
							else
							{
								lab = "" + val.toString();
								//lab="没有关联到值"+val.ToString()+"Class="+attr.UIBindKey+"EX="+ex.Message;
							}
						}
						DDL ddl = new DDL(attr, val.toString(), lab, false, this.get_request().getRealPath("/"));
						ddl.setId("DDL_" + attr.getKey());
						ddl.setName("DDL_" + attr.getKey());
						this.AddContral(attr.getDesc(), ddl, true);
						//this.Controls.Add(ddl);
					}
				}
				else
				{
					/* 可以使用的情况. */
					DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
					ddl1.setId("DDL_" + attr.getKey());
					ddl1.setName("DDL_" + attr.getKey());
					this.AddContral(attr.getDesc(), ddl1, true);
					//	this.Controls.Add(ddl1);
				}
			}
			else if (attr.getUIContralType() == UIContralType.RadioBtn)
			{
				//					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
				//					foreach(SysEnum en in enums)
				//					{
				//						return ;
				//					}
			}

			if (isLeft == false)
				Pub1.append(this.AddTREnd());

			isLeft = !isLeft;
		} // 结束循环.

		Pub1.append("</TABLE>");
	}
	//		public void BindReadonly(Entity en )
	public void BindReadonly(Entity en)
	{
		this.HisEn = en;
		//this.IsReadonly = isReadonly;
		//this.IsShowDtl = isShowDtl;
//		this.Attributes["visibility"] = "hidden";
		this.Controls.clear();
		this.AddTable(); //("<table   width='100%' id='AutoNumber1'  border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
		boolean isLeft = true;
		Object val = null;
		boolean isAddTR = true;
		for(Attr attr:en.getEnMap().getAttrs())
		{
			if (isLeft && isAddTR)
			{
				Pub1.append("<tr>");
			}
			isAddTR = true;
			val = en.GetValByKey(attr.getKey());
			if (attr.getUIContralType() == UIContralType.TB)
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					this.AddContral(attr.getDesc(), val.toString());
					isAddTR = false;
					continue;
				}
				else if (attr.getMyFieldType() == FieldType.MultiValues)
				{
					/* 如果是多值的.*/
					ListBox lb = new ListBox(attr);
					lb.setVisible(true);
					lb.setHeight(128);
					lb.setSelectionMode(ListSelectionMode.Multiple);
					Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
					ens.RetrieveAll();
					this.Controls.add(lb);
					Pub1.append(lb);
				}
				else
				{
					if (attr.getUIVisible() == false)
					{
						this.SetValByKey(attr.getKey(), val.toString());
						continue;
					}
					else
					{

						if (attr.getUIHeight() != 0)
						{
							this.AddContral(attr.getDesc(), val.toString());
						}
						else
						{

							switch (attr.getMyDataType())
							{
							case DataType.AppMoney:
								//this.AddContral(attr.Desc, val.ToString().ToString("0.00")  );
								break;
							default:
								this.AddContral(attr.getDesc(), val.toString());
								break;
							}
						}
					}

				}
			}
			else if (attr.getUIContralType() == UIContralType.CheckBok)
			{
				if (en.GetValBooleanByKey(attr.getKey()))
					this.AddContral(attr.getDesc(), "是");
				else
					this.AddContral(attr.getDesc(), "否");
			}
			else if (attr.getUIContralType() == UIContralType.DDL)
			{
				this.AddContral(attr.getDesc(), val.toString());
			}
			else if (attr.getUIContralType() == UIContralType.RadioBtn)
			{
				//					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
				//					foreach(SysEnum en in enums)
				//					{
				//						return ;
				//					}
			}

			if (isLeft == false)
				Pub1.append(this.AddTREnd());

			isLeft = !isLeft;
		} // 结束循环.

		Pub1.append("</TABLE>");



		if (en.IsExit(en.getPK(), en.getPKVal()) == false)
			return;

		String refstrs = "";
		if (en.equals(""))
		{
			refstrs += "";
			return;
		}

		String keys = "&PK=" + en.getPKVal().toString();
		for(Attr attr:en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Enum ||
					attr.getMyFieldType() == FieldType.FK ||
					attr.getMyFieldType() == FieldType.PK ||
					attr.getMyFieldType() == FieldType.PKEnum ||
					attr.getMyFieldType() == FieldType.PKFK)
				keys += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
		}
		Entities hisens = en.getGetNewEntities();
		SimpleDateFormat sdf=new SimpleDateFormat("ddhhmmss");
		keys += "&r=" + sdf.format(new Date());
		refstrs = GetRefstrs(keys, en, en.getGetNewEntities());
		if (refstrs != "")
			refstrs += "<hr>";
		Pub1.append(refstrs);
	}
	/// <summary>
	/// 
	/// </summary>
	/// <param name="en"></param>
	/// <param name="isReadonly"></param>
	/// <param name="isShowDtl"></param>
	//		public void Bind3Item(Entity en, bool isReadonly, bool isShowDtl)
	public void Bind3Item(Entity en, boolean isReadonly, boolean isShowDtl)
	{
		AttrDescs ads = new AttrDescs(en.toString());
		this.HisEn = en;
		this.isReadonly = isReadonly;
		this.isShowDtl = isShowDtl;
		this.Controls.clear();
//		this.Attributes["visibility"] = "hidden";
		this.Controls.clear();
		Pub1.append("<table   width='100%' id='AutoNumber1'  border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
		boolean isLeft = true;
		Object val = null;
		Attrs attrs = en.getEnMap().getAttrs();
		for(Attr attr:attrs)
		{

			if ("MyNum".equals(attr.getKey()))
				continue;

			val = en.GetValByKey(attr.getKey());
			if (attr.getUIContralType() == UIContralType.TB)
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					continue;
				}
				else if (attr.getMyFieldType() == FieldType.MultiValues)
				{
					/* 如果是多值的.*/
					ListBox lb = new ListBox(attr);
					lb.setVisible(true);

					lb.setHeight(128);
					lb.setSelectionMode(ListSelectionMode.Multiple);
					Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
					ens.RetrieveAll();
					Pub1.append(this.AddTR());
					this.Controls.add(lb);
					Pub1.append(lb);
				}
				else
				{
					if (attr.getUIVisible() == false)
					{
						this.SetValByKey(attr.getKey(), val.toString());
						continue;
					}
					else
					{
						if (attr.getUIHeight() != 0)
						{
							/* doc 文本类型。　*/
							TextBox area = new TextBox();
							area.LoadMapAttr(attr);
							area.setId("TB_" + attr.getKey());
							area.setName("TB_" + attr.getKey());
							area.setText(val.toString());
							area.setRows(8);
							area.setCols(30);

							area.setTextMode(TextBoxMode.MultiLine);
							area.addAttr("height","100px");
							//area.Attributes["width"]="100px";
							area.setIsHelpKey("0");

							area.addAttr("class", "TextArea1");

							if (isReadonly)
								area.setEnabled(false);

							Pub1.append(this.AddTR());
							Pub1.append("<TD colspan=3 class='FDesc' >" + attr.getDesc() + "</TD>");
							Pub1.append(this.AddTREnd());

							Pub1.append(this.AddTR());
							Pub1.append("<TD colspan=3 class='TD' height='250' >");
							Pub1.append(area);
							Pub1.append("</TD>");
							Pub1.append(this.AddTREnd());
							continue;
						}
						else
						{
							TextBox tb = new TextBox();
							tb.setId("TB_" + attr.getKey());
							tb.setName("TB_" + attr.getKey());
							tb.setIsHelpKey("0");

							if (isReadonly || attr.getUIIsReadonly())
								tb.setEnabled(false);
							switch (attr.getMyDataType())
							{
							case DataType.AppMoney:
								tb.setText(""+Float.parseFloat(val.toString()));
								break;
							default:
								tb.setText(val.toString());
								break;
							}
							tb.addAttr("width","100%");
							Pub1.append(this.AddTR());
							this.AddContral(attr.getDesc(), tb);

						}
					}
				}
			}
			else if (attr.getUIContralType() == UIContralType.CheckBok)
			{
				CheckBox cb = new CheckBox();
				cb.setChecked(en.GetValBooleanByKey(attr.getKey()));

				if (isReadonly || !attr.getUIIsReadonly())
					cb.setEnabled(false);
				else
					cb.setEnabled(attr.getUIVisible());


				cb.setId("CB_" + attr.getKey());
				cb.setName("CB_" + attr.getKey());
				Pub1.append(this.AddTR());
				this.AddContral(attr.getDesc(), cb);
			}
			else if (attr.getUIContralType() == UIContralType.DDL)
			{
				if (isReadonly || !attr.getUIIsReadonly())
				{
					/* 如果是 DDLIsEnable 的, 就要找到. */
					if (attr.getMyFieldType() == FieldType.Enum)
					{
						/* 如果是 enum 类型 */
						int enumKey = Integer.parseInt(val.toString());
						BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);

						//DDL ddl = new DDL(attr,text,en.Lab,false);
						DDL ddl = new DDL();
						ddl.Items.add(new ListItem(enEnum.getLab(), val.toString()));
						ddl.Items.get(0).setSelected(true);
						ddl.setEnabled(false);
						ddl.setId("DDL_" + attr.getKey());
						ddl.setName("DDL_" + attr.getKey());
						Pub1.append(this.AddTR());
						this.AddContral(attr.getDesc(), ddl, false);
						//this.Controls.Add(ddl);
					}
					else
					{
						/* 如果是 ens 类型 */
						Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
						Entity en1 = ens.getGetNewEntity();
						en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
						String lab = "";
						try
						{
							en1.Retrieve();
							lab = en1.GetValStringByKey(attr.getUIRefKeyText());
						}
						catch(Exception e)
						{
							if (SystemConfig.getIsDebug() == false)
							{
								lab = "" + val.toString();
							}
							else
							{
								lab = "" + val.toString();
								//lab="没有关联到值"+val.ToString()+"Class="+attr.UIBindKey+"EX="+ex.Message;
							}
						}

						DDL ddl = new DDL(attr, val.toString(), lab, false, this.get_request().getRealPath("/"));
						ddl.setId("DDL_" + attr.getKey());
						ddl.setName("DDL_" + attr.getKey());
						Pub1.append(this.AddTR());
						this.AddContral(attr.getDesc(), ddl, false);
						//this.Controls.Add(ddl);
					}
				}
				else
				{
					/* 可以使用的情况. */
					DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
					ddl1.setId("DDL_" + attr.getKey());
					ddl1.setName("DDL_" + attr.getKey());
					//ddl1.SelfBindKey = ens.ToString();
					//ddl1.SelfEnsRefKey = attr.UIRefKeyValue;
					//ddl1.SelfEnsRefKeyText = attr.UIRefKeyText;

					Pub1.append(this.AddTR());
					this.AddContral(attr.getDesc(), ddl1, true);
				}
			}
			else if (attr.getUIContralType() == UIContralType.RadioBtn)
			{

			}

			AttrDesc ad1 = (AttrDesc)ads.GetEnByKey(AttrDescAttr.Attr, attr.getKey());
			if (ad1 == null)
				Pub1.append(this.AddTD("class='Note'", "&nbsp;"));
			else
				Pub1.append(this.AddTD("class='Note'", ad1.getDesc()));

			Pub1.append(this.AddTREnd());
		} //结束循环.

		//#region 查看是否包含 MyFile字段如果有就认为是附件。
		if (en.getEnMap().getAttrs().Contains("MyFileName"))
		{
			/* 如果包含这二个字段。*/
			String fileName = en.GetValStringByKey("MyFileName");
			String filePath = en.GetValStringByKey("MyFilePath");
			String fileExt = en.GetValStringByKey("MyFileExt");

			String url = "";
			if (fileExt != "")
			{
				// 系统物理路径。
				String path = this.get_request().getRealPath("/").toLowerCase();
				String path1 = filePath.toLowerCase();
				path1 = path1.replace(path, "");
				url = "&nbsp;&nbsp;<a href='../" + path1 + "/" + en.getPKVal() + "." + fileExt + "' target=_blank ><img src='../Images/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
			}
			
			Pub1.append(this.AddTR());
			Pub1.append(this.AddTD("align=right nowrap=true class='FDesc'", "附件或图片:"));
			TextBox file=new TextBox();
			file.setTextMode(TextBoxMode.Files);
			file.setId("file");
			file.addAttr("style", "width:60%");
			Pub1.append("<TD colspan=2  class='FDesc' >");
			Pub1.append(file);
			Pub1.append(url);
			if (fileExt != "")
			{
				Button btn1 = new Button();
				btn1.setText("移除");
				btn1.setCssClass("Btn");

				btn1.setId("Btn_DelFile");
				btn1.setName("Btn_DelFile");
				btn1.addAttr("class", "Btn1");

				btn1.addAttr("onclick", btn1.attributes.get("onclick")+" return confirm('此操作要执行移除附件或图片，是否继续？');");
				Pub1.append(btn1);
			}
			Pub1.append("</TD>");

			Pub1.append(this.AddTREnd());
		}

		Pub1.append(this.AddTR());
		Pub1.append("<TD align=center colspan=3 >");


		Button btn = new Button();
		btn.setCssClass("Btn");

		if (en.getHisUAC().IsInsert)
		{
			btn = new Button();
			btn.setId("Btn_New");
			btn.setName("Btn_New");
			btn.setText("  新 建  ");
			btn.setCssClass("Btn");

			btn.addAttr("class", "Btn1");

			Pub1.append(btn);
			Pub1.append("&nbsp;");
		}

		if (en.getHisUAC().IsUpdate)
		{
			btn = new Button();
			btn.setId("Btn_Save");
			btn.setName("Btn_Save");
			btn.setCssClass("Btn");
			btn.setText("  保  存  ");
			btn.addAttr("class", "Btn1");

			Pub1.append(btn);
			Pub1.append("&nbsp;");
		}


		if (en.getHisUAC().IsDelete)
		{
			btn = new Button();
			btn.setId("Btn_Del");
			btn.setName("Btn_Del");
			btn.setCssClass("Btn");
			btn.setText("  删  除  ");
			btn.addAttr("class", "Btn1");

			btn.addAttr("onclick", " return confirm('您确定要执行删除吗？');");
			Pub1.append(btn);
			Pub1.append("&nbsp;");
		}

		Pub1.append("&nbsp;<input class='Btn' type=button onclick='javascript:window.close()' value='  关  闭  ' />");

		Pub1.append("</TD>");
		Pub1.append(this.AddTREnd());

		Pub1.append(this.AddTableEnd());

		if (isShowDtl == false)
			return;


		if (en.IsExit(en.getPK(), en.getPKVal()) == false)
			return;

		String refstrs = "";
		if (en.equals(""))
		{
			refstrs += "";
			return;
		}
		Pub1.append("<HR>");

		String keys = "&PK=" + en.getPKVal().toString();
		for(Attr attr:en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Enum ||
					attr.getMyFieldType() == FieldType.FK ||
					attr.getMyFieldType() == FieldType.PK ||
					attr.getMyFieldType() == FieldType.PKEnum ||
					attr.getMyFieldType() == FieldType.PKFK)
				keys += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
		}
		Entities hisens = en.getGetNewEntities();
		SimpleDateFormat sdf=new SimpleDateFormat("ddhhmmss");
		keys += "&r=" + sdf.format(new Date());
		refstrs = GetRefstrs(keys, en, en.getGetNewEntities());
		if (refstrs != "")
			refstrs += "<hr>";

		Pub1.append(refstrs);
	}
//	private void btn_Click(object sender, EventArgs e)
//	{
//	}
	public Entity GetEnData(Entity en) throws Exception
	{
		try
		{
			for(Attr attr:en.getEnMap().getAttrs())
			{
				if (attr.getMyFieldType() == FieldType.RefText)
					continue;

				if ("MyNum".equals(attr.getKey()))
					continue;
				
				if(attr.getUIContralType()==UIContralType.TB){
					if (attr.getUIVisible())
					{
						if (attr.getUIHeight() == 0)
						{
							en.SetValByKey(attr.getKey(), this.GetTBByID("TB_" + attr.getKey()).getText());
							continue;
						}
						else
						{
							if (this.IsExit("TB_" + attr.getKey()))
							{
								en.SetValByKey(attr.getKey(), this.GetTBByID("TB_" + attr.getKey()).getText());
								continue;
							}

							if (this.IsExit("TBH_" + attr.getKey()))
							{
								UiFatory uf=new UiFatory();
								TextBox input =(TextBox)uf.GetUIByID("TBH_" + attr.getKey());
								en.SetValByKey(attr.getKey(), input.getText());
								continue;
							}

							if (this.IsExit("TBF_" + attr.getKey()))
							{
								//FredCK.FCKeditorV2.FCKeditor fck = (FredCK.FCKeditorV2.FCKeditor)this.FindControl("TB_" + attr.Key);
								//en.SetValByKey(attr.Key, fck.Value);
								continue;
							}
						}
					}
					else
					{
						en.SetValByKey(attr.getKey(), this.GetValByKey(attr.getKey()));
					}
				}
				else if(attr.getUIContralType()==UIContralType.DDL){
					en.SetValByKey(attr.getKey(), this.GetDDLByKey("DDL_" + attr.getKey()).getSelectedItem().getValue());
				}
				else if(attr.getUIContralType()==UIContralType.CheckBok){
					en.SetValByKey(attr.getKey(), this.GetCBByKey("CB_" + attr.getKey()).getChecked());
				}
				else if(attr.getUIContralType()==UIContralType.RadioBtn){
					
				}

			}
		}
		catch (Exception ex)
		{
			throw new Exception("GetEnData error :" + ex.getMessage());
		}
		return en;
	}

	public DDL GetDDLByKey(String key)
	{
		UiFatory ui=new UiFatory();
		return (DDL)ui.GetUIByID(key);
	
	}
	//		public CheckBox GetCBByKey(string key)
	public CheckBox GetCBByKey(String key)
	{
		UiFatory uf=new UiFatory();
		return (CheckBox)uf.GetUIByID(key);
	}

	protected void Page_Load()
	{
		if (this.isPostBack())
		{
			this.BindColumn4(this.HisEn, this.getEnName());
			//  this.BindColumn4(this.HisEn, this.IsReadonly, this.IsShowDtl);

		}
	}
	public Entity HisEn = null;

	public static String GetRefstrs1(String keys, Entity en, Entities hisens)
	{
		String refstrs = "";

//		#region 加入一对多的实体编辑
		AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
		if (oneVsM.size() > 0)
		{
			for(AttrOfOneVSM vsM:oneVsM)
			{
				String url = "UIEn1ToM.aspx?EnsName=" + en.toString() + "&AttrKey=" + vsM.getEnsOfMM().toString() + keys;
				int i = 0;
				try
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'");
				}
				catch(Exception e)
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "=" + en.getPKVal());
				}

				if (i == 0)
					refstrs += "[<a href='" + url + "'  >" + vsM.getDesc() + "</a>]";
				else
					refstrs += "[<a href='" + url + "'  >" + vsM.getDesc() + "-" + i + "</a>]";

			}
		}

		//			SysUIEnsRefFuncs reffuncs = en.GetNewEntities.HisSysUIEnsRefFuncs ;
		//			if ( reffuncs.Count > 0  )
		//			{
		//				foreach(SysUIEnsRefFunc en1 in reffuncs)
		//				{
		//					string url="RefFuncLink.aspx?RefFuncOID="+en1.OID.ToString()+"&MainEnsName="+hisens.ToString()+keys;
		//					refstrs+="[<a href='"+url+"' >"+en1.Name+"</a>]";
		//				}
		//			}

//		#region 加入他的明细
		EnDtls enDtls = en.getEnMap().getDtls();
		if (enDtls.size() > 0)
		{
			for(EnDtl enDtl:enDtls)
			{
				String url = "UIEnDtl.aspx?EnsName=" + enDtl.getEnsName() + "&RefKey=" + enDtl.getRefKey() + "&RefVal=" + en.getPKVal().toString() + "&MainEnsName=" + en.toString() + keys;
				int i = 0;
				try
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
				}
				catch(Exception e)
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
				}

				if (i == 0)
					refstrs += "[<a href='" + url + "'  >" + enDtl.getDesc() + "</a>]";
				else
					refstrs += "[<a href='" + url + "'  >" + enDtl.getDesc() + "-" + i + "</a>]";
			}
		}

		return refstrs;
	}

	//#region Web 窗体设计器生成的代码
//	protected void OnInit(EventArgs e)
//	{
//		//
//		// CODEGEN: 该调用是 ASP.NET Web 窗体设计器所必需的。
//		//
//		InitializeComponent();
//		base.OnInit(e);
//	}

	/// <summary>
	///		设计器支持所需的方法 - 不要使用代码编辑器
	///		修改此方法的内容。
	/// </summary>
	private void InitializeComponent()
	{

	}
	
    public TextBox GetTBByID(String key) throws Exception
    {
        try
        {
        	UiFatory uf=new UiFatory();
            return (TextBox)uf.GetUIByID(key);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " 请确认：TB AND TextBox " + key);
        }
    }
    
    public boolean IsExit(String ctlID)
    {
        for(BaseWebControl ctl:this.Controls)
        {
            if (ctl.getId() == null)
                continue;

            if (ctl.getId() == ctlID)
                return true;
        }
        return false;
    }
    
    public void AddContral(String desc, DDL ddl, boolean isRefBtn)
    {
        Pub1.append("<td class='FDesc' nowrap width=1% > " + desc + "</td><td class=TD nowrap>");
        this.Controls.add(ddl);
        Pub1.append(ddl);
        if (ddl.getEnabled())
        {
            if (ddl.getSelfBindKey().indexOf(".") == -1)
            {
            	Pub1.append(this.AddTDEnd());
            }
            else
            {
                if (isRefBtn && ddl.Items.size() > 15)
                {
                    String srip = "javascript:HalperOfDDL('" + ddl.getAppPath() + "','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + null + "' ); ";//ddl.ClientID.ToString()
                    Pub1.append("<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.getId() + "' ></td>");
                }
                else
                {
                	Pub1.append(this.AddTDEnd());
                }
            }
        }
        else
        {
        	Pub1.append(this.AddTDEnd());
        }
    }
}
