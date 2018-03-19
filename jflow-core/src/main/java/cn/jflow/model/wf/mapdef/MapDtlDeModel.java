package cn.jflow.model.wf.mapdef;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.EditType;
import BP.En.EntitiesNoName;
import BP.En.FieldTypeS;
import BP.Sys.GroupField;
import BP.Sys.GroupFieldAttr;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.WF.Glo;
import BP.Web.WebUser;

public class MapDtlDeModel extends BaseModel{
	
	private StringBuilder pub;
	private String title;
	
	public StringBuilder getPub() {
		return pub;
	}
	
	public String getTitle() {
		return title;
	}
	
	private void appendPub(String str){
		pub.append(str);
	}

	public MapDtlDeModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		pub = new StringBuilder();
	}
	
	
	public final String getMyPK() {
		return getParameter("FK_MapDtl");
	}
	public final String getDoType() {
		return getParameter("DoType");
	}
	public final String getFK_MapExt() {
		return getParameter("FK_MapExt");
	}
	public final String getKey()
	{
		return getParameter("Key");
	}
	public final String getFK_MapDtl()
	{
		return getParameter("FK_MapDtl");
	}
	///#endregion 属性
	
	public void pageLoad( )
	{
		title = "从表设计";

		MapData.setIsEditDtlModel(true);
		MapData md = new MapData(this.getFK_MapData());
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		if (!dtl.getIsView())
		{
			return;
		}
		
		//#region 查看有几个分组？从表的字段多于1个分组就需要把他们设置成一个分组就可以，否则排序出现问题.
		String sqlGroup = "SELECT COUNT(DISTINCT(GroupID)) FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapDtl() + "'";
		if (DBAccess.RunSQLReturnValInt(sqlGroup) != 1)
		{
			GroupField gf = new GroupField();
			if(!gf.RetrieveByAttrAnd(GroupFieldAttr.EnName, this.getFK_MapDtl(), GroupFieldAttr.Lab, dtl.getName()))
			{
				gf.setEnName(this.getFK_MapDtl());
				gf.setLab(dtl.getName());
				gf.Insert();
			}
			sqlGroup = "UPDATE Sys_MapAttr SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getFK_MapDtl() + "'";
			DBAccess.RunSQL(sqlGroup);
		}

		MapAttrs attrs = new MapAttrs(this.getMyPK());
		MapAttrs attrs2 = new MapAttrs();
		MapExts mes = new MapExts(this.getMyPK());
		StringBuilder LinkFields = new StringBuilder(",");
		if (mes.size() != 0)
		{
			for (MapExt me : mes.ToJavaList())
			{
				if (me.getExtType().equals(MapExtXmlList.Link)) {
					LinkFields.append(me.getAttrOfOper()).append(",");
				}
			}

			appendPub("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");
		}
		
		String t = DataType.dateToStr(new Date(), "MM-dd-hh:mm:ss");
		if (attrs.size() == 0)
		{
			dtl.IntMapAttrs();
		}


		title = md.getName() + " - 设计明细";
		appendPub((AddTable("class='Table' border='0' ID='Tab' cellspacing='0' cellpadding='0' ")));
		//     appendPub(AddCaptionLeftTX("<a href='MapDef.aspx?MyPK=" + md.No + "' ><img src='../Img/Btn/Back.gif' border=0/>" + this.ToE("Back","返回") + ":" + md.Name + "</a> - <img src='../Img/Btn/Table.gif' border=0/>" + dtl.Name + " - <a href=\"javascript:AddF('" + this.MyPK + "');\" ><img src='../Img/Btn/New.gif' border=0/>" + "新建字段" + "</a> ");
		appendPub(dtl.getMTR());

		///#region 输出标题.
		appendPub(AddTR());
		if (dtl.getIsShowIdx())
		{
			appendPub(AddTH(""));
		}

		for (MapAttr attr : attrs.ToJavaList())
		{
			if (!attr.getUIVisible())
			{
				continue;
			}

			appendPub("<TH style='width:" + attr.getUIWidthInt() + "px'>");
			appendPub("<a href=\"javascript:Up('" + this.getMyPK() + "','" + attr.getMyPK() + "','" + t + "');\" ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Btn/Left.gif' class=Arrow alt='向左移动' border=0/></a>");
			if (attr.getHisEditType() == EditType.UnDel || attr.getHisEditType() == EditType.Edit)
			{
				switch (attr.getLGType())
				{
					case Normal:
						appendPub("<a href=\"javascript:Edit('" + this.getMyPK() + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\"  alt='" + attr.getKeyOfEn() + "'>" + attr.getName() + "</a>");
						break;
					case Enum:
						appendPub("<a href=\"javascript:EditEnum('" + this.getMyPK() + "','" + attr.getMyPK() + "');\" alt='" + attr.getKeyOfEn() + "' >" + attr.getName() + "</a>");
						break;
					case FK:
						appendPub("<a href=\"javascript:EditTable('" + this.getMyPK() + "','" + attr.getMyPK() + "');\"  alt='" + attr.getKeyOfEn() + "'>" + attr.getName() + "</a>");
						break;
				}
			}
			else
			{
				appendPub(attr.getName());
			}
			//  appendPub("[<a href=\"javascript:Insert('" + this.MyPK + "','" + attr.Idx + "');\" ><img src='../Img/Btn/Insert.gif' border=0/>插入</a>]");
			appendPub("<a href=\"javascript:Down('" + this.getMyPK() + "','" + attr.getMyPK() + "','" + t + "');\" ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Btn/Right.gif' class=Arrow alt='向右移动' border=0/></a>");
			appendPub("</TH>");
		}

		if (dtl.getIsEnableAthM())
		{
			appendPub(AddTH("<a href=\"javascript:Attachment('" + dtl.getNo() + "');\"><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Set.gif' border=0 width='16px' /></a>"));
		}

		if (dtl.getIsEnableM2M())
		{
			appendPub(AddTH("<a href=\"javascript:MapM2M('" + dtl.getNo() + "');\"><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Set.gif' border=0 width='16px' /></a>"));
		}

		if (dtl.getIsEnableM2MM())
		{
			appendPub(AddTH("<a href=\"javascript:window.showModalDialog('MapM2MM.aspx?NoOfObj=M2MM&FK_MapData=" + this.getFK_MapDtl() + "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Set.gif' border=0 width='16px' /></a>"));
		}

		if (dtl.getIsEnableLink())
		{
			appendPub(AddTDTitle(dtl.getLinkLabel()));
		}

		//Pub1.AddTDTitle("&nbsp;");

		appendPub(AddTREnd());
		///#endregion 输出标题.

		///#region 输出行.
		int size = dtl.getRowsOfList();
		for (int i = 1; i <= size; i++)
		{
			appendPub(AddTR());
			if (dtl.getIsShowIdx())
			{
				appendPub(AddTDIdx(i));
			}
			for (MapAttr attr : attrs.ToJavaList())
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}

				///#region 是否输出超连接.
				if (attr.getUIIsEnable() == false && LinkFields.toString().contains("," + attr.getKeyOfEn() + ","))
				{
					Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.Link);
					MapExt meLink = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
					String url = meLink.getTag();
					if (url.contains("?") == false)
					{
						url = url + "?a3=2";
					}
					url = url + "&WebUserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&EnName=" + this.getFK_MapDtl();
					if (url.contains("@AppPath"))
					{
						url = url.replace("@AppPath", Glo.getCCFlowAppPath());
					}
					if (url.contains("@"))
					{
						if (attrs2.size() == 0)
						{
							attrs2 = new MapAttrs(this.getFK_MapDtl());
						}
						for (MapAttr item : attrs2.ToJavaList())
						{
							url = url.replace("@" + item.getKeyOfEn(), item.getDefVal());
							if (url.contains("@") == false)
							{
								break;
							}
						}
					}
					appendPub(AddTD("<a href='" + url + "' target='" + meLink.getTag1() + "' >" + attr.getDefVal() + "</a>"));
					continue;
				}
			///#endregion 是否输出超连接.

			///#region 输出字段.
				switch (attr.getLGType())
				{
					case Normal:
						if (attr.getMyDataType() == BP.DA.DataType.AppBoolean)
						{
							CheckBox cb = new CheckBox();
							cb.setChecked(attr.getDefValOfBool());
							cb.setEnabled(attr.getUIIsEnable());
							cb.setText(attr.getName());
							appendPub(AddTD(cb));
							break;
						}
						TextBox tb = new TextBox();
						tb.setId("TB_" + attr.getKeyOfEn() + "_" + i);
						tb.setText(attr.getDefVal());
						tb.setReadOnly(!attr.getUIIsEnable());
						switch (attr.getMyDataType())
						{
							case BP.DA.DataType.AppString:
								tb.addAttr("style","width:" + attr.getUIWidth() + "px;border: none;");
								if (attr.getUIHeight() > 25)
								{
									tb.setTextMode(TextBoxMode.MultiLine);
									tb.addAttr("Height", attr.getUIHeight() + "px");
									tb.setRows(attr.getUIHeightInt() / 25);
								}
								break;
							case BP.DA.DataType.AppDateTime:
								tb.addAttr("style","width:" + attr.getUIWidth() + "px;border: none;");
								if (attr.getUIIsEnable())
								{
									tb.attributes.put("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
									//tb.Attributes["class"] = "TBcalendar";
								}
								break;
							case BP.DA.DataType.AppDate:
								tb.addAttr("style","width:" + attr.getUIWidth() + "px;border: none;");
								if (attr.getUIIsEnable())
								{
									tb.attributes.put("onfocus", "WdatePicker();");
									//  tb.Attributes["class"] = "TBcalendar";
								}
								break;
							default:
								tb.addAttr("style","width:" + attr.getUIWidth() + "px;border: none;");
								if (tb.getReadOnly() == false)
								{
									// OnKeyPress="javascript:return VirtyNum(this);"
									//tb.Attributes["OnKeyDown"] = "javascript:return VirtyNum(this);";

									if (attr.getMyDataType() == DataType.AppInt)
									{
										tb.attributes.put("OnKeyDown", "javascript:return VirtyInt(this);");
									}
									else
									{
										tb.attributes.put("OnKeyDown", "javascript:return VirtyNum(this);");
									}
									tb.addAttr("onkeyup", "javascript:C" + i + "();C" + attr.getKeyOfEn() + "();");
									tb.setCssClass("TBNum");
								}
								else
								{
									// tb.Attributes["onpropertychange"] += "C" + attr.KeyOfEn + "();";
									tb.setCssClass("TBNumReadonly");
								}
								break;
						}
						appendPub(AddTD(tb.toString()));
						break;
					case Enum:
						DDL ddl = new DDL();
						ddl.setId("DDL_" + attr.getKeyOfEn() + "_" + i);
						try
						{
							ddl.BindSysEnum(attr.getKeyOfEn());
							ddl.SetSelectItem(attr.getDefVal());
						}
						catch (RuntimeException ex)
						{
							Alert(ex.getMessage());
							return;
						}
						ddl.setEnabled(attr.getUIIsEnable());
						appendPub(AddTDCenter(ddl));
						break;
					case FK:
						DDL ddl1 = new DDL();
						ddl1.setId("DDL_" + attr.getKeyOfEn() + "_" + i);
						try
						{
							EntitiesNoName ens = attr.getHisEntitiesNoName();
							ens.RetrieveAll();
							ddl1.BindEntities(ens);
							if (ddl1.SetSelectItem(attr.getDefVal()) == false)
							{
								ddl1.Items.add(0, new ListItem("请选择", attr.getDefVal()));
								ddl1.SetSelectItemByIndex(0);
							}
						}
						catch (java.lang.Exception e)
						{
						}
						ddl1.setEnabled(attr.getUIIsEnable());
						appendPub(AddTDCenter(ddl1));
						break;
					default:
						break;
				}
			///#endregion s输出字段.
			}

		///#region 输出附件,m2m
			if (dtl.getIsEnableAthM())
			{
				appendPub(AddTD("<a href=\"javascript:EnableAthM('" + this.getFK_MapDtl() + "');\" ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/AttachmentM.png' border=0 width='16px' /></a>"));
			}

			if (dtl.getIsEnableM2M())
			{
				appendPub(AddTD("<a href=\"javascript:window.showModalDialog('../CCForm/M2M.aspx?NoOfObj=M2M&IsTest=1&OID=0&FK_MapData=" + this.getFK_MapDtl() + "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='"+Glo.getCCFlowAppPath()+"WF/Img/M2M.png' border=0 width='16px' /></a>"));
			}

			if (dtl.getIsEnableM2MM())
			{
				appendPub(AddTD("<a href=\"javascript:window.showModalDialog('../CCForm/M2MM.aspx?NoOfObj=M2MM&IsTest=1&OID=0&FK_MapData=" + this.getFK_MapDtl() + "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='"+Glo.getCCFlowAppPath()+"WF/Img/M2MM.png' border=0 width='16px' /></a>"));
			}

			if (dtl.getIsEnableLink())
			{
				appendPub(AddTD("<a href='" + dtl.getLinkUrl() + "' target='" + dtl.getLinkTarget() + "' >" + dtl.getLinkLabel() + "</a>"));
			}
			///#endregion 输出附件,m2m

			//Pub1.AddTD("&nbsp;");
			appendPub(AddTREnd());
		}
		///#endregion 输出行.

		///#region 合计.
		if (dtl.getIsShowSum())
		{
			appendPub(AddTRSum());
			if (dtl.getIsShowIdx())
			{
				appendPub(AddTD("合计"));
			}

			for (MapAttr attr : attrs.ToJavaList())
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}
				if (attr.getIsNum() && attr.getLGType() == FieldTypeS.Normal)
				{
					TextBox tb = new TextBox();
					tb.setId("TB_" + attr.getKeyOfEn());
					tb.setText(attr.getDefVal());
					tb.setShowType(attr.getHisTBType());
					tb.setReadOnly(true);
					tb.attributes.put("style", "background-color:#FFFFFF;font-weight:bold;");
					tb.setCssClass("TBNumReadonly");
					appendPub(AddTD(tb));
				}
				else
				{
					appendPub(AddTD());
				}
			}
			if (dtl.getIsEnableAthM())
			{
				appendPub(AddTD());
			}

			if (dtl.getIsEnableM2M())
			{
				appendPub(AddTD());
			}

			if (dtl.getIsEnableM2MM())
			{
				appendPub(AddTD());
			}

			if (dtl.getIsEnableLink())
			{
				appendPub(AddTD());
			}

		//    Pub1.AddTD("&nbsp;");
			appendPub(AddTREnd());
		}
		appendPub(AddTableEnd());
		///#endregion 合计.
		
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(pub.toString(), false);

		///#region 处理设计时自动填充从表.
		if (this.getKey() != null)
		{
			MapExt me = new MapExt(this.getFK_MapExt());
			String[] strs = me.getTag1().split("[$]", -1);
			for (String str : strs)
			{
				if (str.contains(this.getFK_MapDtl()) == false)
				{
					continue;
				}

				String[] ss = str.split("[:]", -1);

				String sql = ss[1];
				sql = sql.replace("@Key", this.getKey());
				sql = sql.replace("@key", this.getKey());
				sql = sql.replace("@val", this.getKey());
				sql = sql.replace("@Val", this.getKey());

				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				int idx = 0;
				for (DataRow dr : dt.Rows)
				{
					idx++;
					for (DataColumn dc : dt.Columns)
					{
						String val = dr.getValue(dc.ColumnName).toString();
						try
						{
							TextBox tb = (TextBox) controls.get("TB_" + dc.ColumnName + "_" + idx);
							tb.setText(val);
							updateCtrlToPub(tb);
						}
						catch (java.lang.Exception e2)
						{ }

						try
						{
							DDL ddl = (DDL) controls.get("DDL_" + dc.ColumnName + "_" + idx);
							ddl.SetSelectItem(val);
							updateCtrlToPub(ddl);
						}
						catch (java.lang.Exception e3)
						{
						}
					}
				}
			}
		}
		// #endregion 处理设计时自动填充从表.

		// #region 处理拓展属性.
		int size_2 = dtl.getRowsOfList();
		for (int i = 1; i <= size_2; i++)
		{
			ArrayList<MapExt> exts = MapExts.convertMapExts(mes);
			int exts_size = exts.size();
			for (int j = 0; j < exts_size; j++) {
				MapExt me = exts.get(j);
				// 自动填充.
				if(me.getExtType().equals(MapExtXmlList.DDLFullCtrl)){

					DDL ddlOper = (DDL) controls.get("DDL_" + me.getAttrOfOper());
					
					if (ddlOper == null)
					{
						continue;
					}
					ddlOper.attributes.put("onchange", "DDLFullCtrl(this.value,\'" + ddlOper.getId() + "\', \'" + me.getMyPK() + "\')");
					updateCtrlToPub(ddlOper);
				}else if(me.getExtType().equals(MapExtXmlList.ActiveDDL)){
					DDL ddlPerant = (DDL) controls.get("DDL_" + me.getAttrOfOper() + "_" + i);
					if (ddlPerant == null)
					{
						me.Delete();
						continue;
					}

					DDL ddlChild = (DDL) controls.get("DDL_" + me.getAttrsOfActive() + "_" + i);
					if (ddlChild == null)
					{
						me.Delete();
						continue;
					}
					ddlPerant.attributes.put("onchange", "DDLAnsc(this.value,\'" + ddlChild.getId() + "\', \'" + me.getMyPK() + "\')");
					updateCtrlToPub(ddlPerant);
					if (ddlPerant.Items.size() == 0)
					{
						continue;
					}

					String val = ddlPerant.getSelectedItemStringVal();

					String valC1 = ddlChild.getSelectedItemStringVal();

					DataTable dt = DBAccess.RunSQLReturnTable(me.getDoc().replace("@Key", val));

					ddlChild.Items.clear();
					for (DataRow dr : dt.Rows)
					{
						ddlChild.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
					}
					ddlChild.SetSelectItem(valC1);
					updateCtrlToPub(ddlChild);
				}else if(me.getExtType().equals(MapExtXmlList.AutoFullDLL)){
					DDL ddlFull = (DDL) controls.get("DDL_" + me.getAttrOfOper() + "_" + i);
					if (ddlFull == null)
					{
						me.Delete();
						continue;
					}

					String valOld = ddlFull.getSelectedItemStringVal();
					ddlFull.Items.clear();
					String fullSQL = me.getDoc().replace("WebUser.No", WebUser.getNo());
					fullSQL = fullSQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					fullSQL = fullSQL.replace("@WebUser.Name", WebUser.getName());

//						if (fullSQL.contains("@"))
//						{
//							//Attrs attrsFull = mydtl.getEnMap().getAttrs();
//							//foreach (Attr attr in attrsFull)
//							//{
//							//    if (fullSQL.Contains("@") == false)
//							//        break;
//							//    fullSQL = fullSQL.Replace("@" + attr.Key, mydtl.GetValStrByKey(attr.Key));
//							//}
//						}
					ddlFull.Bind(DBAccess.RunSQLReturnTable(fullSQL), "No", "Name");
					ddlFull.SetSelectItem(valOld);
					updateCtrlToPub(ddlFull);
				}else if(me.getExtType().equals(MapExtXmlList.TBFullCtrl)){
					TextBox tbAuto = (TextBox) controls.get("TB_" + me.getAttrOfOper() + "_" + i);
					if (tbAuto == null)
					{
						me.Delete();
						continue;
					}
					tbAuto.attributes.put("onkeyup","DoAnscToFillDiv(this,this.value,\'" + tbAuto.getId() + "\', \'" + me.getMyPK() + "\');");
					tbAuto.attributes.put("AUTOCOMPLETE","OFF");
					updateCtrlToPub(tbAuto);
					if (!me.getTag().equals(""))
					{
						// 处理下拉框的选择范围的问题 
						String[] strs = me.getTag().split("[$]", -1);
						for (String str : strs)
						{
							String[] myCtl = str.split("[:]", -1);
							String ctlID = myCtl[0];
							DDL ddlC = (DDL) controls.get("DDL_" + ctlID + "_" + i);
							if (ddlC == null)
							{
								continue;
							}

							String sql = myCtl[1].replace("~", "'");
							sql = sql.replace("WebUser.No", WebUser.getNo());
							sql = sql.replace("@WebUser.Name", WebUser.getName());
							sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
							sql = sql.replace("@Key", tbAuto.getText().trim());
							DataTable dt = DBAccess.RunSQLReturnTable(sql);
							String valC = ddlC.getSelectedItemStringVal();
							ddlC.Items.clear();
							for (DataRow dr : dt.Rows)
							{
								ddlC.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
							}
							ddlC.SetSelectItem(valC);
							updateCtrlToPub(ddlC);
						}
					}
				}else if(me.getExtType().equals(MapExtXmlList.InputCheck)){
					TextBox tbCheck = (TextBox) controls.get("TB_" + me.getAttrOfOper() + "_" + i);
					if (tbCheck != null)
					{
						tbCheck.addAttr(me.getTag2(), " rowPK=" + i + ";" + me.getTag1() + "(this);");
						updateCtrlToPub(tbCheck);
					}
					else
					{
						me.Delete();
					}
				}else if(me.getExtType().equals(MapExtXmlList.PopVal)){ //弹出窗.
					TextBox tb = (TextBox) controls.get("TB_" + me.getAttrOfOper() + "_" + i);
					if (tb == null) {
						continue;
					}
					//tb.Attributes["ondblclick"] = "return ReturnValCCFormPopVal(this,'" + me.MyPK + "','33');";
					tb.attributes.put("ondblclick", "ReturnValCCFormPopVal(this,'" + me.getMyPK() + "','33');");
					updateCtrlToPub(tb);
				} 
				
			}
		}
		///#endregion 处理拓展属性.

		///#region 输出自动计算公式
		appendPub("\n <script language='JavaScript'>");
		MapExts exts = new MapExts(dtl.getNo());
		for (MapExt ext : exts.ToJavaList())
		{
			if (!ext.getExtType().equals(MapExtXmlList.AutoFull))
			{
				continue;
			}
			int rows = dtl.getRowsOfList();
			for (int i = 1; i <= rows; i++)
			{
				String top = "\n function C" + i + "() { \n ";
				String script = "";
				for (MapAttr attr : attrs.ToJavaList())
				{
					if (attr.getUIVisible() == false)
					{
						continue;
					}
					if (attr.getIsNum() == false)
					{
						continue;
					}

					if (attr.getLGType() != FieldTypeS.Normal)
					{
						continue;
					}

					if (ext.getTag().equals("1") && ! ext.getDoc().equals(""))
					{
						script += this.GenerAutoFull((new Integer(i)).toString(), attrs, ext);
					}
				}
				String end = " \n  } ";
				appendPub(top + script + end);
			}
		}
		appendPub("\n</script>");

		// 输出合计算计公式
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getUIVisible() == false)
			{
				continue;
			}

			if (attr.getLGType() != FieldTypeS.Normal)
			{
				continue;
			}

			if (attr.getIsNum() == false)
			{
				continue;
			}

			if (attr.getMyDataType() == DataType.AppBoolean)
			{
				continue;
			}

			String top = "\n<script language='JavaScript'> function C" + attr.getKeyOfEn() + "() { \n ";
			String end = "\n } </script>";
			appendPub(top + this.GenerSum(attr, dtl) + " ; \t\n" + end);
		}
		///#endregion 输出自动计算公式

	}
	/** 
	 生成列的计算
	 
	 @param pk
	 @param attrs
	 @param attr
	 @return 
	*/
	public final String GenerAutoFull(String pk, MapAttrs attrs, MapExt ext)
	{
		try
		{
			StringBuilder left = new StringBuilder();
			left.append("\n  document.forms[0].").append("TB_").append(ext.getAttrOfOper()).append("_").append(pk).append(".value = ");
			String right = ext.getDoc();
			String tempPub = pub.toString();
			for (MapAttr mattr : attrs.ToJavaList())
			{
				String tbID = "TB_" + mattr.getKeyOfEn() + "_" + pk;
				
				if (!tempPub.contains(tbID))
				{
					continue;
				}
				right = right.replace("@" + mattr.getName(), " parseFloat( document.forms[0]." + tbID + ".value.replace( ',' ,  '' ) ) ");
				right = right.replace("@" + mattr.getKeyOfEn(), " parseFloat( document.forms[0]." + tbID + ".value.replace( ',' ,  '' ) ) ");
			}
			StringBuilder s = new StringBuilder(left.toString());
			s.append(right);
			s .append("\t\n  document.forms[0].").append("TB_").append(ext.getAttrOfOper()).append("_").append(pk).append(".value= VirtyMoney(document.forms[0].").append("TB_").append(ext.getAttrOfOper()).append("_").append(pk + ".value ) ;");
			return s.append(" C").append(ext.getAttrOfOper()).append("();").toString();
		}
		catch (RuntimeException ex)
		{
			Alert(ex.getMessage());
			return "";
		}
	}
	public final String GenerSum(MapAttr mattr, MapDtl dtl)
	{
		if (dtl.getIsShowSum() == false)
		{
			return "";
		}

		if (mattr.getMyDataType() == DataType.AppBoolean)
		{
			return "";
		}

		String left = "\n  document.forms[0]." + ("TB_" + mattr.getKeyOfEn() + ".value = ");
		StringBuilder right = new StringBuilder();
		int rows = dtl.getRowsOfList();
		for (int i = 1; i <= rows; i++)
		{
			String tbID = "TB_" + mattr.getKeyOfEn() + "_" + i;
			String tempPub = pub.toString();
			if (!tempPub.contains(tbID))
			{
				continue;
			}

			if (i == 0)
			{
				right.append(" parseFloat( document.forms[0].").append(tbID).append(".value.replace( ',' ,  '' ) )  ");
			}
			else
			{
				right.append(" +parseFloat( document.forms[0].").append(tbID).append(".value.replace( ',' ,  '' ) )  ");
			}
		}

		String s = left + right + " ;";
		switch (mattr.getMyDataType())
		{
			case BP.DA.DataType.AppMoney:
			case BP.DA.DataType.AppRate:
				return s += "\t\n  document.forms[0]." + "TB_" + mattr.getKeyOfEn() + ".value= VirtyMoney(document.forms[0]." + "TB_" + mattr.getKeyOfEn()+ ".value ) ;";
			default:
				return s;
		}
	}
	
	/**
	 * 更新组件到pub
	 * @param control
	 */
	private void updateCtrlToPub(BaseWebControl control){
		pub = new StringBuilder(HtmlUtils.setCtrlHtml(control, pub.toString()));
	}
}
