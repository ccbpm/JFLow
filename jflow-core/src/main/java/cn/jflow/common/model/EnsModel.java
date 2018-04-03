package cn.jflow.common.model;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.ToolBar;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.Sys.EnCfg;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Comm.UIRowStyleGlo;
import BP.Web.GroupXml;

public class EnsModel extends BaseModel{
	
	public static ToolBar ToolBar1;
	
	public static StringBuilder pub;
	
	public static StringBuilder ucsys1;
	
	public static StringBuilder ucsys2;
	

	public String title;
	
	public String getTitle() {
		return title;
	}
	
	public String getPub() {
		return pub.toString();
	}
	
	public String getUcsys1() {
		return ucsys1.toString();
	}
	
	public String getUcsys2() {
		return ucsys2.toString();
	}
	
	public String getToolBar1() {
		return ToolBar1.toString();
	}

	public EnsModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		
		pub = new StringBuilder();
		
		ucsys1 = new StringBuilder();
		
		ToolBar1 = new ToolBar(request, response, new UiFatory());
	}
	
	
	public final String getGroupKey()
	{
		return (String)((getParameter("GroupKey") instanceof String) ? getParameter("GroupKey") : null);
	}
	public final String getEnsName()
	{
		String str = getParameter("EnsName");
		if (str == null)
		{
			return "BP.Edu.BTypes";
		}
		return str;
	}
	public final Entity getHisEn()
	{
		return this.getHisEns().getGetNewEntity();
	}
	public final Entities getHisEns()
	{
		Entities ens = BP.En.ClassFactory.GetEns(this.getEnsName());
		return ens;
	}
	///#endregion 属性

	public final void pageLoad()
	{
		HttpSession session = get_request().getSession();
		Object obj = (String) session.getAttribute("info1");
		String info  = (String)((obj instanceof String) ? obj : null);
		if (info != null)
		{
			if (info.length() > 2)
			{
				ResponseWriteRedMsg(info);
				session.setAttribute("info1", "");
				session.setAttribute("info", null);
			}
		}

		this.ToolBar1.AddLinkBtn(NamesOfBtn.Save, "保存(S)");
		this.ToolBar1.AddLinkBtn(NamesOfBtn.Delete,"删除(D)");

		//this.ToolBar1.GetLinkBtnByID("Btn_Save").addAttr("onclick", "Btn_Save()");
		//this.ToolBar1.GetLinkBtnByID("Btn_Save").setHref("Btn_Save()");
		//   this.ToolBar1.GetLinkBtnByID("Btn_New").Click += new EventHandler(ToolBar1_ButtonClick);
		this.ToolBar1.GetLinkBtnByID("Btn_Delete").addAttr("onclick", "Btn_Del()");
		this.ToolBar1.GetLinkBtnByID("Btn_Delete").setHref("Btn_Del()");

		// this.ToolBar1.Add("<input type=button value='设置' onclick=\"OpenAttrs('" + this.EnsName + "')\"  >");
		//this.ToolBar1.AddLab("sw", "<input type=button  id='ToolBar1$Btn_P' name='ToolBar1$Btn_P'  onclick=\"javascript:OpenAttrs('" + this.EnsName + "');\"  value='设置(P)'  />");

		this.Bind();
    	Entity en = BP.En.ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();
		if (this.getGroupKey() == null)
		{
			//this.Pub1.Add(  this.GenerCaption(en.EnDesc + en.EnMap.TitleExt) );
		}
		else
		{
			GroupXml xml = new GroupXml(this.getGroupKey());
			String[] strs = xml.getName().split("[@]", -1);

			pub.append(MenuSelfBegin());
			for (String str : strs)
			{
				if (str == null)
				{
					continue;
				}

				String[] ss = str.split("[=]", -1);
				if (ss.length == 0)
				{
					continue;
				}

				String url = getPageID() + ".jsp?EnsName=" + ss[0] + "&GroupKey=" + this.getGroupKey();

				if (str.contains(this.getEnsName()) == true)
				{
					pub.append(MenuSelfItemS(url, str, "_self"));
				} else {
					pub.append(MenuSelfItem(url, str, "_self"));
				}
			}
			pub.append(MenuSelfEnd());

		}
	}

	public final void Bind()
	{
		///#region 生成标题
		Entity en = this.getHisEn();
		Map map = en.getEnMap();
		EnCfg cfg = new EnCfg(en.toString());
		Attrs attrs = map.getAttrs();
		if (attrs.size()>=4)
		{
			ucsys1.append("<table border=0 cellpadding='0'  style='border-collapse: collapse;width:100%' cellspacing='0'  >");
		}
		else
		{
			ucsys1.append("<table border=0 cellpadding='0'  style='border-collapse: collapse;width:50%' cellspacing='0'  >");
		}

		ucsys1.append(AddTR());
		CheckBox cb = new CheckBox();
		String str1 = "<INPUT id='checkedAll' onclick='SelectAll()' type='checkbox' name='checkedAll'>";
		ucsys1.append(AddTDGroupTitle(str1));
		for (Attr attr : attrs)
		{
			if (attr.getUIVisible() == false)
			{
				continue;
			}
			ucsys1.append(AddTDGroupTitle(StringHelper.isEmpty(attr.getDesc(), "")));
		}

		if (map.IsHaveFJ)
		{
			ucsys1.append(AddTDGroupTitle("附件"));
		}
		ucsys1.append(AddTDGroupTitle());
		ucsys1.append(AddTREnd());
		///#endregion 生成标题
		this.title = en.getEnDesc();
		Entities ens = this.getHisEns();
		QueryObject qo = new QueryObject(ens);

		///#region 用户界面属性设置- del
		//BP.Web.Comm.UIRowStyleGlo tableStyle = (UIRowStyleGlo)ens.GetEnsAppCfgByKeyInt("UIRowStyleGlo"); // 界面风格。
		//bool IsEnableDouclickGlo = ens.GetEnsAppCfgByKeyBoolen("IsEnableDouclickGlo"); // 是否启用双击
		//bool IsEnableRefFunc = ens.GetEnsAppCfgByKeyBoolen("IsEnableRefFunc"); // 是否显示相关功能。
		//bool IsEnableFocusField = ens.GetEnsAppCfgByKeyBoolen("IsEnableFocusField"); //是否启用焦点字段。
		//bool isShowOpenICON = ens.GetEnsAppCfgByKeyBoolen("IsEnableOpenICON"); //是否启用 OpenICON 。
		//string FocusField = null;
		//if (IsEnableFocusField)
		//    FocusField = ens.GetEnsAppCfgByKeyString("FocusField");

		//int WinCardH = ens.GetEnsAppCfgByKeyInt("WinCardH"); // 弹出窗口高度
		//int WinCardW = ens.GetEnsAppCfgByKeyInt("WinCardW"); // 弹出窗口宽度.
		///#endregion 用户界面属性设置

		///#region 用户界面属性设置 - del
		UIRowStyleGlo tableStyle = UIRowStyleGlo.None; //(UIRowStyleGlo)ens.GetEnsAppCfgByKeyInt("UIRowStyleGlo"); // 界面风格。
		boolean IsEnableDouclickGlo = true; // ens.GetEnsAppCfgByKeyBoolen("IsEnableDouclickGlo"); // 是否启用双击
		boolean IsEnableRefFunc = false; // ens.GetEnsAppCfgByKeyBoolen("IsEnableRefFunc"); // 是否显示相关功能。
		boolean IsEnableFocusField = false; // ens.GetEnsAppCfgByKeyBoolen("IsEnableFocusField"); //是否启用焦点字段。
		boolean isShowOpenICON = true; // ens.GetEnsAppCfgByKeyBoolen("IsEnableOpenICON"); //是否启用 OpenICON 。
		String FocusField = null;
		if (IsEnableFocusField)
		{
			FocusField = ""; //ens.GetEnsAppCfgByKeyString("FocusField");
		}
		int WinCardH =800; // ens.GetEnsAppCfgByKeyInt("WinCardH"); // 弹出窗口高度
		int WinCardW =820;// ens.GetEnsAppCfgByKeyInt("WinCardW"); // 弹出窗口宽度.
		///#endregion 用户界面属性设置 - del


		///#region 生成翻页
		try
		{
			ucsys2 = new StringBuilder();
			BindPageIdx(ucsys2, qo.GetCount(), SystemConfig.getPageSize(), getPageIdx(), "Ens.htm?EnsName=" + this.getEnsName());
			qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), getPageIdx(), false);
		}
		catch (RuntimeException ex)
		{
			ens.getGetNewEntity().CheckPhysicsTable();
			return;
		}
		///#endregion 生成翻页

		en.setPKVal("0");
		ens.AddEntity(en);
		DDL ddl = new DDL();
		boolean is1 = false;

		///#region 生成数据
		int i = 0;
		for (Entity dtl : ens.ToJavaListEn())
		{
			String urlExt = "\"javascript:ShowEn('"+Glo.getCCFlowAppPath()+"WF/Comm/RefFunc/UIEn.jsp?EnsName=" + ens.toString() + "&PK=" + dtl.getPKVal() + "');\"";
			//String urlExt = Glo.getCCFlowAppPath()+"WF/Comm/RefFunc/UIEn.jsp?EnsName=" + ens.toString() + "&PK=" + dtl.getPKVal() + "', 'cd');\"";
			i++;
			if (dtl.getPKVal().equals("0"))
			{
				this.ucsys1.append(AddTRSum());
				this.ucsys1.append(AddTDIdx("<b>*</b>"));
			}
			else
			{
				this.ucsys1.append(AddTR(is1, "ondblclick=" + urlExt));
				cb = new CheckBox();
				cb.setId("IDX_" + dtl.getPKVal());
				cb.setText((new Integer(i)).toString());
				this.ucsys1.append(AddTDIdx(cb.toString()));
			}
			for (Attr attr : attrs)
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}

				if (attr.getKey().equals("OID"))
				{
					continue;
				}

				String val = dtl.GetValByKey(attr.getKey()).toString();
				switch (attr.getUIContralType())
				{
					case  TB:
						TextBox tb = new TextBox();
						tb.LoadMapAttr(attr);
						tb.setId("TB_" + attr.getKey() + "_" + dtl.getPKVal());
					
						switch (attr.getMyDataType())
						{
							case DataType.AppMoney:
							case DataType.AppRate:
								val = new DecimalFormat("0.00").format(Double.parseDouble(val));
								tb.setText(val);
								break;
							default:
								tb.setText(val);
								break;
						}

						if (attr.getIsNum() && attr.getIsFKorEnum() == false)
						{
							if (tb.getEnabled())
							{
								// OnKeyPress="javascript:return VirtyNum(this);"
								//  tb.Attributes["OnKeyDown"] = "javascript:return VirtyNum(this);";
								// tb.Attributes["onkeyup"] += "javascript:C" + dtl.PKVal + "();C" + attr.Key + "();";
								tb.setCssClass("TBNum");
							}
							else
							{
								//   tb.Attributes["onpropertychange"] += "C" + attr.Key + "();";
								tb.setCssClass("TBNumReadonly");
							}
						}
						this.ucsys1.append(AddTD(tb));
						break;
					case  DDL:
						if (attr.getUIIsReadonly())
						{
							ddl = new DDL();
							ddl.LoadMapAttr(attr);
							ddl.setId("DDL_" + attr.getKey() + "_" + dtl.getPKVal());
							//  this.ucsys1.append(AddTD(ddl);
							ddl.SetSelectItem(val);
							this.ucsys1.append(AddTD(ddl.toString()));
						}
						else
						{
							this.ucsys1.append(AddTD(dtl.GetValRefTextByKey(attr.getKey())));
						}
						break;
					case  CheckBok:
						cb = new CheckBox();
						cb.setId("CB_" + attr.getKey() + "_" + dtl.getPKVal());
						//cb.Text = attr.Name;
						if (val.equals("1"))
						{
							cb.setChecked(true);
						}
						else
						{
							cb.setChecked(false);
						}
						this.ucsys1.append(AddTDCenter(cb.toString()));
						break;
					default:
						break;
				}
			}
			if (map.IsHaveFJ)
			{
				String ext = dtl.GetValStrByKey("MyFileExt");
				if (ext == null || ext.length() > 1)
				{
					this.ucsys1.append(AddTD("<a href='" + cfg.getFJWebPath() + "/" + dtl.getPKVal() + "." + ext + "' target=_blank ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/FileType/" + dtl.GetValStrByKey("MyFileExt") + ".gif' border=0/>" + dtl.GetValStrByKey("MyFileName") + "</a>"));
				}
				else
				{
					this.ucsys1.append(AddTD());
				}
			}
			if (isShowOpenICON)
			{
				this.ucsys1.append("<TD class='TD' style='cursor:pointer;'nowrap=true><a href=" + urlExt + " ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Btn/open.gif' border=0/></a></TD>");
			}
			else
			{
				this.ucsys1.append(AddTD());
			}
			this.ucsys1.append(AddTREnd());
		}

		///#region 生成合计
//		if (false) { //			this.ucsys1.append(AddTRSum());
//			this.ucsys1.append(AddTD("colspan=1", "合计"));
//			for (Attr attr : attrs)
//			{
//				if (attr.getUIVisible() == false)
//				{
//					continue;
//				}
//
//				if (attr.getIsNum() && attr.getIsFKorEnum() == false)
//				{
//					TextBox tb = new TextBox();
//					tb.setId("TB_" + attr.getKey());
//					tb.setText(attr.getDefaultVal().toString());
//					tb.setShowType(attr.getHisTBType());
//					tb.setReadOnly(true);
//					tb.addAttr("style", "font-weight:bold");
//					tb.setBackColor("#FFFFFF");
//
//					switch (attr.getMyDataType())
//					{
//						case DataType.AppRate:
//						case DataType.AppMoney:
////							tb.TextExtMoney = ens.GetSumDecimalByKey(attr.getKey());
//							tb.setText(String.valueOf(ens.GetSumDecimalByKey(attr.getKey())));
//							break;
//						case DataType.AppInt:
////							tb.TextExtInt = ens.GetSumIntByKey(attr.getKey());
//							tb.setText(String.valueOf(ens.GetSumIntByKey(attr.getKey())));
//							break;
//						case DataType.AppFloat:
////							tb.TextExtFloat = ens.GetSumFloatByKey(attr.getKey());
//							tb.setText(String.valueOf(ens.GetSumFloatByKey(attr.getKey())));
//							break;
//						default:
//							break;
//					}
//					this.ucsys1.append(AddTD(tb.toString()));
//				}
//				else
//				{
//					this.ucsys1.append(AddTD());
//				}
//			}
//
//			if (map.IsHaveFJ)
//			{
//				this.ucsys1.append(AddTD());
//			}
//
//			this.ucsys1.append(AddTD());
//			this.ucsys1.append(AddTREnd());
//		}
		///#endregion 生成合计

		///#endregion 生成数据

		this.ucsys1.append(AddTableEnd());
	}
}
