package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.FieldTypeS;
import BP.En.TBType;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Web.WebUser;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class SysMapEnUIModel extends SysMapEnUCModel{
	

	public String EnsName;

	public String PK;

	public SysMapEnUIModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public void init()
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();
		en.setPKVal(this.PK);
		en.RetrieveFromDBSources();


		BindColumn4(en, this.getEnsName());
		this.Title = en.getEnDesc();
	}

    public Entity HisEn = null;
    public GroupField currGF = new GroupField();
    public MapDtls dtls;
    private GroupFields gfs;
    public String Title;
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
					mytbLine.setRows(8);
					mytbLine.addAttr("style","width:100%;padding: 0px;margin: 0px;");
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());
					if (mytbLine.getEnabled() == false)
						mytbLine.addAttr("class","TBReadonly");

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
					mytbLine.setTextMode(TextBoxMode.MultiLine);
					mytbLine.setRows(8);
					mytbLine.setCols(30);
					mytbLine.addAttr("style","width:100%;padding: 0px;margin: 0px;");
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());
					if (mytbLine.getEnabled() == false)
						mytbLine.addAttr("class","TBReadonly");

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
				tb.setCols( 60);
				tb.setId("TB_" + attr.getKeyOfEn());
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
							tb.addAttr("onfocus","WdatePicker();");

						Pub1.append(this.AddTD("colspan=" + colspanOfCtl, tb));
						break;
					case BP.DA.DataType.AppDateTime:
						Pub1.append(this.AddTDDesc(attr.getName()));
						tb.setShowType(TBType.DateTime);
						tb.setText(en.GetValStringByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable())
							tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");

						this.AddTD("colspan=" + colspanOfCtl, tb);
						break;
					case BP.DA.DataType.AppBoolean:
						Pub1.append(this.AddTDDesc(""));
						CheckBox cb = new CheckBox();
						cb.setText(attr.getName());
						cb.setId("CB_" + attr.getKeyOfEn());
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
					ddle.BindSysEnum(attr.getKeyOfEn());
					ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					ddle.setEnabled(attr.getUIIsEnable());
					Pub1.append(this.AddTD("colspan=" + colspanOfCtl, ddle));
				}
				else if(attr.getLGType()==FieldTypeS.FK){
					Pub1.append(this.AddTDDesc(attr.getName()));
					DDL ddl1 = new DDL();
					ddl1.setId("DDL_" + attr.getKeyOfEn());
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
				}else{
					
				}
//				switch (attr.getLGType())
//				{
//				case FieldTypeS.Normal:
//					tb.Enabled = attr.UIIsEnable;
//					switch (attr.MyDataType)
//					{
//					case BP.DA.DataType.AppString:
//						this.AddTDDesc(attr.Name);
//						if (attr.IsSigan)
//						{
//							this.AddTD("colspan=" + colspanOfCtl, "<img src='/DataUser/Siganture/" + WebUser.getNo() + ".jpg' border=0 onerror=\"this.src='../Data/Siganture/UnName.jpg'\"/>");
//						}
//						else
//						{
//							tb.ShowType = TBType.TB;
//							try
//							{
//								tb.Text = en.GetValStrByKey(attr.KeyOfEn);
//							}
//							catch
//							{
//								tb.Text = attr.KeyOfEn;
//							}
//							this.AddTD("colspan=" + colspanOfCtl, tb);
//						}
//						break;
//					case BP.DA.DataType.AppDate:
//						this.AddTDDesc(attr.Name);
//						tb.ShowType = TBType.Date;
//						tb.Text = en.GetValStrByKey(attr.KeyOfEn);
//						if (attr.UIIsEnable)
//							tb.Attributes["onfocus"] = "WdatePicker();";
//
//						this.AddTD("colspan=" + colspanOfCtl, tb);
//						break;
//					case BP.DA.DataType.AppDateTime:
//						this.AddTDDesc(attr.Name);
//						tb.ShowType = TBType.DateTime;
//						tb.Text = en.GetValStringByKey(attr.KeyOfEn);
//						if (attr.UIIsEnable)
//							tb.Attributes["onfocus"] = "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});";
//
//						this.AddTD("colspan=" + colspanOfCtl, tb);
//						break;
//					case BP.DA.DataType.AppBoolean:
//						this.AddTDDesc("");
//						CheckBox cb = new CheckBox();
//						cb.Text = attr.Name;
//						cb.ID = "CB_" + attr.KeyOfEn;
//						cb.Checked = attr.DefValOfBool;
//						cb.Enabled = attr.UIIsEnable;
//						cb.Checked = en.GetValBooleanByKey(attr.KeyOfEn);
//						this.AddTD("colspan=" + colspanOfCtl, cb);
//						break;
//					case BP.DA.DataType.AppDouble:
//					case BP.DA.DataType.AppFloat:
//					case BP.DA.DataType.AppInt:
//						this.AddTDDesc(attr.Name);
//						tb.ShowType = TBType.Num;
//						tb.Text = en.GetValStrByKey(attr.KeyOfEn);
//						this.AddTD("colspan=" + colspanOfCtl, tb);
//						break;
//
//					case BP.DA.DataType.AppMoney:
//					case BP.DA.DataType.AppRate:
//						this.AddTDDesc(attr.Name);
//						tb.ShowType = TBType.Moneny;
//						tb.Text = decimal.Parse(en.GetValStrByKey(attr.KeyOfEn)).ToString("0.00");
//						this.AddTD("colspan=" + colspanOfCtl, tb);
//						break;
//					default:
//						break;
//					}
//					tb.Attributes["width"] = "100%";
//					switch (attr.MyDataType)
//					{
//					case BP.DA.DataType.AppString:
//					case BP.DA.DataType.AppDateTime:
//					case BP.DA.DataType.AppDate:
//						if (tb.Enabled)
//							tb.Attributes["class"] = "TB";
//						else
//							tb.Attributes["class"] = "TBReadonly";
//						break;
//					default:
//						if (tb.Enabled)
//							tb.Attributes["class"] = "TBNum";
//						else
//							tb.Attributes["class"] = "TBNumReadonly";
//						break;
//					}
//					break;
//				case FieldTypeS.Enum:
//					this.AddTDDesc(attr.Name);
//					DDL ddle = new DDL();
//					ddle.ID = "DDL_" + attr.KeyOfEn;
//					ddle.BindSysEnum(attr.KeyOfEn);
//					ddle.SetSelectItem(en.GetValStrByKey(attr.KeyOfEn));
//					ddle.Enabled = attr.UIIsEnable;
//					this.AddTD("colspan=" + colspanOfCtl, ddle);
//					break;
//				case FieldTypeS.FK:
//					this.AddTDDesc(attr.Name);
//					DDL ddl1 = new DDL();
//					ddl1.ID = "DDL_" + attr.KeyOfEn;
//					try
//					{
//						EntitiesNoName ens = attr.HisEntitiesNoName;
//						ens.RetrieveAll();
//						ddl1.BindEntities(ens);
//						ddl1.SetSelectItem(en.GetValStrByKey(attr.KeyOfEn));
//					}
//					catch
//					{
//					}
//					ddl1.Enabled = attr.UIIsEnable;
//					this.AddTD("colspan=" + colspanOfCtl, ddl1);
//					break;
//				default:
//					break;
//				}
				

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


	
}
