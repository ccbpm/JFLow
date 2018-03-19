package cn.jflow.common.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.EntitiesNoName;
import BP.En.FieldTypeS;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.DtlModel;
import BP.Sys.DtlOpenType;
import BP.Sys.DtlShowModel;
import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.WhenOverSize;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.XML.EventListDtlList;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class DtlControlModel extends BaseModel {

	public DtlControlModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Pub2 = new StringBuilder();
	public GEEntity _MainEn = null;
	public MapAttrs _MainMapAttrs = null;
	public String FK_MapData = null;

	public final String getFK_MapExt() {
		return this.get_request().getParameter("FK_MapExt");
	}

	public final String getKey() {
		return this.get_request().getParameter("Key");
	}

	/**
	 * 主表FK_MapData
	 */
	public final String getMainEnsName() {
		String str = this.get_request().getParameter("MainEnsName");
		if (str == null) {
			return "ND299";
		}
		return str;
	}

	public final int getBlankNum() {
		try {
			// return Integer.parseInt(ViewState["BlankNum").toString());
			return -3333;
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public final void setBlankNum(int value) {
		// ViewState["BlankNum",value;
	}

	public final String getRefPK() {
		String str = this.get_request().getParameter("RefPK");
		return str;
	}

	public final String getRefPKVal() {
		String str = this.get_request().getParameter("RefPKVal");
		// 小周鹏 20150610 修改 Start
		// if (str == null) {
		if (StringHelper.isNullOrEmpty(str)) {
			// 小周鹏 20150610 修改 End
			return "1";
		}
		return str;
	}

	/**
	 * 明细表数量.
	 */
	public final int getDtlCount() {
		// return Integer.parseInt(ViewState["DtlCount"].toString());
		return -3333;
	}

	public final void setDtlCount(int value) {
		// ViewState["DtlCount",value;
		// ////////////////////////////
	}

	/**
	 * 增加列的数量。
	 */
	public final int getaddRowNum() {
		try {
			int i = Integer.parseInt(this.get_request().getParameter(
					"addRowNum"));
			if (this.get_request().getParameter("IsCut") == null) {
				return i;
			} else {
				return i;
			}
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public int _allRowCount = 0;

	public final int getallRowCount() {
		int i = 0;
		try {
			i = Integer.parseInt(this.get_request().getParameter("rowCount"));
		} catch (java.lang.Exception e) {
			return 0;
		}
		return i;
	}

	public final int getIsWap() {
		if (this.get_request().getParameter("IsWap") == null)
			return 0;
		if (this.get_request().getParameter("IsWap").equals("1")) {
			return 1;
		}
		return 0;
	}

	public final boolean getIsEnable_del() {
		// ////////////////////////////////////
		// String s = (String)((this.ViewState["R"] instanceof String) ?
		// this.ViewState["R"] : null);
		// if (s == null || s.equals("0"))
		// {
		// return false;
		// }
		return true;
	}

	public final void setIsEnable_del(boolean value) {
		// /////////////////////////////////////////////
		// if (value)
		// {
		// this.ViewState["R","1";
		// }
		// else
		// {
		// this.ViewState["R","0";
		// }
	}

	public final GEEntity getMainEn() {
		if (_MainEn == null) {
			_MainEn = new GEEntity(this.FK_MapData, this.getRefPKVal());
		}
		return _MainEn;
	}

	public final MapAttrs getMainMapAttrs() {
		if (_MainMapAttrs == null) {
			_MainMapAttrs = new MapAttrs(this.FK_MapData);
		}
		return _MainMapAttrs;
	}

	public final void init() {
		MapDtl mdtl = new MapDtl(this.getEnsName());
		BP.WF.Node nd = null;
		// 20150615 xiaozhoupeng 添加，原因ccflow 代码更新 START
		if (this.getFK_Node() != 0&& !mdtl.getFK_MapData().equals("Temp") && this.getEnsName().contains("ND" + this.getFK_Node()) == false) {
			try{
				nd = new Node(this.getFK_Node());
				// 如果
				// * 1,传来节点ID, 不等于0.
				// * 2,不是节点表单. 就要判断是否是流程表单，如果是就要处理权限方案。
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), mdtl.getFK_MapData());
				int i = fn.Retrieve(FrmNodeAttr.FK_Frm, mdtl.getFK_MapData(), FrmNodeAttr.FK_Node, this.getFK_Node());
				if (i != 0 && fn.getFrmSln() != 0) {
					// 使用了自定义的方案.
					// * 并且，一定为dtl设定了自定义方案，就用自定义方案.
					// *
					MapDtl mymdtl = new MapDtl();
					mymdtl.setNo(this.getEnsName() + "_" + this.getFK_Node());
					if (mymdtl.RetrieveFromDBSources() == 1) {
						// /Dtl.aspx?EnsName=DtlDemo_FJDtl396340&RefPKVal=212&IsReadonly=0&FID=0&FK_Node=8701
						sendRedirect(Glo.getCCFlowAppPath()
								+ "WF/CCForm/Dtl2.jsp?EnsName=" + this.getEnsName() + "_" + this.getFK_Node() + "&RefPKVal="
								+ this.getRefPKVal() + "&IsReadonly="
								+ this.getIsReadonly() + "&FID=" + this.getFID()
								+ "&FK_Node=" + this.getFK_Node());
						return;
					}
				}
			}catch(RuntimeException ex){
				ex.printStackTrace();
			}
		}
		// 20150615 xiaozhoupeng 添加，原因ccflow 代码更新 END

		if (mdtl.getDtlModel() == DtlModel.FixRow) {
			try {
				this.get_response().sendRedirect(
						"DtlFixRow.jsp?1=2"
								+ this.get_request().getQueryString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// ///////////////////////////////////////////

		if (mdtl.getHisDtlShowModel() == DtlShowModel.Card) {
			try {
				this.get_response().sendRedirect(
						"DtlCard.jsp?EnsName=" + this.getEnsName()
								+ "&RefPKVal=" + this.getRefPKVal() + "&IsWap="
								+ this.getIsWap() + "&FK_Node="
								+ this.getFK_Node() + "&MainEnsName="
								+ this.getMainEnsName());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// ///////////////////////////////////////////////

		if (this.getIsReadonly()) {
			mdtl._IsReadonly = 1;
		}

		this.Bind(mdtl);
	}

	public final void Bind(MapDtl mdtl) {
		if (this.get_request().getParameter("IsTest") != null) {
			BP.DA.Cash.SetMap(this.getEnsName(), null);
		}

		GEDtls dtls = new GEDtls(this.getEnsName());
		this.FK_MapData = mdtl.getFK_MapData();

		GEEntity mainEn = null;

		MapAttrs attrs = new MapAttrs(this.getEnsName());
		MapAttrs attrs2 = new MapAttrs();
		int numOfCol = 0;

		float dtlWidth = mdtl.getW() - 20;
		this.Pub1.append("<Table border=0  style='width:" + dtlWidth + "px'>");
		this.Pub1.append(mdtl.getMTR());
		if (mdtl.getIsShowTitle()) {
			this.Pub1.append(BaseModel.AddTR());
			if (this.getIsWap() == 1) {
				Node nd = new Node(
						this.getFK_Node());
				String url = "../WAP/MyFlow.htm?WorkID=" + this.getRefPKVal()
						+ "&FK_Node=" + this.getFK_Node() + "&FK_Flow="
						+ nd.getFK_Flow();
				this.Pub1
						.append(BaseModel
								.AddTD("<img onclick=\"javascript:SaveDtlDataTo('"
										+ url
										+ "');\" src='../Wap/Img/Back.png' style='width:50px;height:16px' border=0/>"));
			} else {
				this.Pub1
						.append("<TD class='Idx' ><img src='../Img/Btn/Table.gif' onclick=\"return DtlOpt('"
								+ this.getRefPKVal()
								+ "','"
								+ this.getEnsName() + "');\" border=0/></TD>");
				numOfCol++;
			}

			for (MapAttr attr : attrs.ToJavaList()) {
				if (!attr.getUIVisible()) {
					continue;
				}

				if (attr.getIsPK()) {
					continue;
				}

				// 如果启用了分组，并且是当前的分组字段。
				if (mdtl.getIsEnableGroupField()
						&& mdtl.getGroupField() == attr.getKeyOfEn()) {
					continue;
				}

				// for lijian 增加了 @符号是一个换行符.
				this.Pub1.append(BaseModel.AddTDTitleExt(attr.getName()
						.replace("@", "<br>"))); // ("<TD class='FDesc' nowarp=true ><label>"
													// + attr.Name +
													// "</label></TD>");
				numOfCol++;
			}

			if (mdtl.getIsEnableAthM()) {
				this.Pub1.append(BaseModel.AddTDTitleExt(""));
				numOfCol++;
			}

			if (mdtl.getIsEnableM2M()) {
				this.Pub1.append(BaseModel.AddTDTitleExt(""));
				numOfCol++;
			}

			if (mdtl.getIsEnableM2MM()) {
				this.Pub1.append(BaseModel.AddTDTitleExt(""));
				numOfCol++;
			}

			if (mdtl.getIsDelete() && !this.getIsReadonly()) {
				this.Pub1
						.append("<TD class='TitleExt' nowarp=true ><img src='../Img/Btn/Save.gif' border=0 onclick='SaveDtlData();' ></TD>");
				numOfCol++;
			}

			if (mdtl.getIsEnableLink()) {
				this.Pub1.append(BaseModel.AddTDTitleExt(""));
				numOfCol++;
			}

			this.Pub1.append(BaseModel.AddTREnd());
		}//
			// /结束ShowTitle

		// //////////////////////////////////////

		QueryObject qo = null;
		try {
			qo = new QueryObject(dtls);
			switch (mdtl.getDtlOpenType()) {
			case ForEmp: // 按人员来控制.
				qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
				qo.addAnd();
				qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
				break;
			case ForWorkID: // 按工作ID来控制
				qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
				break;
			case ForFID: // 按流程ID来控制.
				if(this.getFID()==0){
					qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
				}else
				{
					qo.AddWhere(GEDtlAttr.FID, this.getFID());
				}
//				// 小周鹏 20150611 修改，原因：ccflow 代码更新 Start
//				qo.AddWhere(GEDtlAttr.FID, this.getFID());
//				// qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
//				// 小周鹏 20150611 修改，原因：ccflow 代码更新 End
				break;
			}
		} catch (java.lang.Exception e) {
			dtls.getGetNewEntity().CheckPhysicsTable();
		}

		// ///////////////////////////////////////////
		// /# 生成翻页
		if (mdtl.getIsEnableGroupField()
				|| mdtl.getHisWhenOverSize() == WhenOverSize.None
				|| mdtl.getHisWhenOverSize() == WhenOverSize.AddRow) {
			// 如果 是分组显示模式 .
			try {
				int num = qo.DoQuery();
				if (getallRowCount() == 0) {
					if (mdtl.getRowsOfList() >= num) {
						mdtl.setRowsOfList(mdtl.getRowsOfList());
						_allRowCount = mdtl.getRowsOfList();
					} else {
						mdtl.setRowsOfList(num);
						_allRowCount = num;

					}
				} else {
					mdtl.setRowsOfList(getallRowCount());
					_allRowCount = getallRowCount();
				}

				if (!this.getIsReadonly()) {
					int dtlCount = dtls.size();
					for (int i = 0; i < mdtl.getRowsOfList() - dtlCount; i++) {
						BP.Sys.GEDtl dt = new GEDtl(this.getEnsName());
						dt.ResetDefaultVal();
						dt.SetValByKey(GEDtlAttr.RefPK, this.getRefPKVal());
						dt.setOID(i);
						dtls.AddEntity(dt);
					}

					// if (num == mdtl.getRowsOfList())
					// {
					// BP.Sys.GEDtl dt1 = new GEDtl(this.EnsName);
					// dt1.ResetDefaultVal();
					// dt1.SetValByKey(GEDtlAttr.RefPK, this.RefPKVal);
					// dt1.OID = mdtl.getRowsOfList() + 1;
					// dtls.AddEntity(dt1);
					// }
				}
			} catch (java.lang.Exception e2) {
				dtls.getGetNewEntity().CheckPhysicsTable();
			}
		} else// ///////////////////////////////////////////////////////
		{
			// 如果不是分组显示模式 .
			this.Pub2.setLength(0);
			try {
				int count = qo.GetCount();
				if (getallRowCount() == 0) {
					if (mdtl.getRowsOfList() >= count) {
						mdtl.setRowsOfList(mdtl.getRowsOfList());
						_allRowCount = mdtl.getRowsOfList();
					} else {
						mdtl.setRowsOfList(count);
						_allRowCount = count;

					}
				} else {
					mdtl.setRowsOfList(getallRowCount());
					_allRowCount = getallRowCount();
				}

				this.setDtlCount(count);
				this.Pub2.setLength(0);
				this.Pub2.append(BindPageIdx(
						Pub2,
						count,
						mdtl.getRowsOfList(),
						getPageIdx(),
						"Dtl.jsp?EnsName=" + this.getEnsName() + "&RefPKVal="
								+ this.getRefPKVal() + "&IsWap="
								+ this.getIsWap() + "&IsReadonly="
								+ this.getIsReadonly() + "&MainEnsName="
								+ this.getMainEnsName()));
				int num = qo.DoQuery("OID", mdtl.getRowsOfList(),
						getPageIdx(), false);

				if (mdtl.getIsInsert() && !this.getIsReadonly()) {
					int dtlCount = dtls.size();
					for (int i = 0; i < mdtl.getRowsOfList() - dtlCount; i++) {
						BP.Sys.GEDtl dt = new GEDtl(this.getEnsName());
						dt.ResetDefaultVal();
						dt.SetValByKey(GEDtlAttr.RefPK, this.getRefPKVal());
						dt.setOID(i);
						dtls.AddEntity(dt);
					}

					// if (num == mdtl.getRowsOfList())
					// {
					// BP.Sys.GEDtl dt1 = new GEDtl(this.EnsName);
					// dt1.ResetDefaultVal();
					// dt1.SetValByKey(GEDtlAttr.RefPK, this.RefPKVal);
					// dt1.OID = mdtl.getRowsOfList() + 1;
					// dtls.AddEntity(dt1);
					// }
				}
			} catch (java.lang.Exception e3) {
				dtls.getGetNewEntity().CheckPhysicsTable();
			}
		}
		// /////////////////////////////////////////
		// /#endregion 生成翻页
		DDL ddl = new DDL();
		CheckBox cb = new CheckBox();

		// 行锁定.
		boolean isRowLock = mdtl.getIsRowLock();

		// /#region 生成数据
		int idx = 1;
		String ids = ",";
		int dtlsNum = dtls.size();
		MapExts mes = new MapExts(this.getEnsName());

		// 需要自动填充的下拉框IDs. 这些下拉框不需要自动填充数据。
		String autoFullDataDDLIDs = ",";
		String LinkFields = ",";
		for (MapExt me : mes.ToJavaList()) {
			if (me.getExtType().equals(MapExtXmlList.ActiveDDL)) {
				autoFullDataDDLIDs += me.getAttrsOfActive() + ",";
			} else if (me.getExtType().equals(MapExtXmlList.AutoFullDLL)) {
				autoFullDataDDLIDs += me.getAttrOfOper() + ",";
			} else if (me.getExtType().equals(MapExtXmlList.Link)) {
				LinkFields += me.getAttrOfOper() + ",";
			} else
				break;
		}

		// ///////////////////////////////////////////////////////////
		if (mdtl.getIsEnableGroupField()) {
			// 如果是分组显示模式, 就要特殊的处理显示.
			// 1， 求出分组集合。
			//

			String gField = mdtl.getGroupField();
			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, gField);
			MapAttr attrG = (MapAttr) ((tempVar instanceof MapAttr) ? tempVar
					: null);
			if (attrG == null) {
				this.Pub1.setLength(0);
				this.Pub1.append(BaseModel.AddFieldSetRed("err",
						"明细表设计错误,分组字段已经不存在明细表中，请联系管理员解决此问题。"));
				return;
			}

			if (attrG.getUIContralType() == UIContralType.DDL) {
				gField = gField + "Text";
			}

			// 求出分组集合.
			String tmp = "";
			for (BP.Sys.GEDtl dtl : dtls.ToJavaList()) {
				if (!tmp.contains("," + dtl.GetValStrByKey(gField) + ",")) {
					tmp += "," + dtl.GetValStrByKey(gField);
				}
			}
			String[] strs = tmp.split("[,]", -1);

			String groupStr = "";
			// 遍历-分组集合.
			for (String str : strs) {
				if (StringHelper.isNullOrEmpty(str)) {
					continue;
				}

				this.Pub1.append(BaseModel.AddTR());
				if (attrG.getUIContralType() == UIContralType.CheckBok) {
					if (str.equals("0")) {
						this.Pub1.append(BaseModel.AddTD("colspan=" + numOfCol,
								attrG.getName() + ":是"));
					} else {
						this.Pub1.append(BaseModel.AddTD("colspan=" + numOfCol,
								attrG.getName() + ":否"));
					}
				} else {
					if (!groupStr.contains(str + ",")) {
						this.Pub1.append(BaseModel.AddTD("colspan=" + numOfCol,
								str));
						groupStr += str + ",";
					}
				}
				this.Pub1.append(BaseModel.AddTREnd());

				for (BP.Sys.GEDtl dtl : dtls.ToJavaList()) {
					if (!str.equals(dtl.GetValStrByKey(gField))) {
						continue;
					}

					// in Java:
					// /#region 处理 IDX AddTR

					if (ids.contains("," + dtl.getOID() + ",")) {
						continue;
					}
					ids += dtl.getOID() + ",";
					this.Pub1.append(BaseModel.AddTR());
					if (mdtl.getIsShowIdx()) {
						this.Pub1.append(BaseModel.AddTDIdx(idx++));
					} else {
						this.Pub1.append(BaseModel.AddTD());
					}

					for (MapAttr attr : attrs.ToJavaList()) {
						if (!attr.getUIVisible()
								|| attr.getKeyOfEn().equals("OID")
								|| attr.getKeyOfEn() == attrG.getKeyOfEn()) {
							continue;
						}

						// //处理它的默认值.
						// if (attr.DefValReal.Contains("@") &&
						// attr.getUIIsEnable() == false)
						// dtl.SetValByKey(attr.getKeyOfEn(), attr.DefVal);

						String val = dtl.GetValByKey(attr.getKeyOfEn())
								.toString();
						if (!attr.getUIIsEnable()
								&& dtl.getOID() >= 100
								&& LinkFields.contains("," + attr.getKeyOfEn()
										+ ",")) {
							if (StringHelper.isNullOrEmpty(val)) {
								val = "...";
							}
							Object tempVar2 = mes.GetEntityByKey(
									MapExtAttr.ExtType, MapExtXmlList.Link,
									MapExtAttr.AttrOfOper, attr.getKeyOfEn());
							MapExt meLink = (MapExt) ((tempVar2 instanceof MapExt) ? tempVar2
									: null);

							Object tempVar3 = new String(meLink.getTag());
							String url = (String) ((tempVar3 instanceof String) ? tempVar3
									: null);
							if (!url.contains("?")) {
								url = url + "?a3=2";
							}

							url = url + "&WebUserNo=" + WebUser.getNo()
									+ "&SID=" + WebUser.getSID() + "&EnName="
									+ mdtl.getNo() + "&OID=" + dtl.getOID();
							if (url.contains("@AppPath")) {
								url = url.replace("@AppPath", "http://"
										+ this.get_request().getRemoteHost()
										+ this.get_request().getRequestURI());
							}

							if (url.contains("@")) {
								if (attrs2.size() == 0) {
									attrs2 = new MapAttrs(mdtl.getNo());
								}
								for (MapAttr item : MapAttrs
										.convertMapAttrs(attrs2)) {
									url = url.replace("@" + item.getKeyOfEn(),
											dtl.GetValStrByKey(item
													.getKeyOfEn()));
									if (!url.contains("@")) {
										break;
									}
								}
								if (url.contains("@")) {
									// 可能是主表也要有参数
									if (mainEn == null) {
										mainEn = this.getMainEn();
									}
									for (Attr attrM : mainEn.getEnMap()
											.getAttrs()) {
										url = url.replace("@" + attrM.getKey(),
												mainEn.GetValStrByKey(attrM
														.getKey()));
										if (!url.contains("@")) {
											break;
										}
									}
								}
							}
							this.Pub1.append(BaseModel.AddTD("<a href='" + url
									+ "' target='" + meLink.getTag1() + "' >"
									+ val + "</a>"));
							continue;
						}
						// switch qin 15/9/16 添加
						switch (attr.getUIContralType()) {
						case TB:
							TextBox tb = new TextBox();
							tb.setId("TB_" + attr.getKeyOfEnToLowerCase() + "_"
									+ dtl.getOID());
							// liuxc,2015.7.3,如果Enabled=false，则服务器控件将不能获取此控件的真实值
							// tb.setEnabled(attr.getUIIsEnable());//edited by

							if (!attr.getUIIsEnable()) {
								tb.addAttr("readonly", "true");
								tb.setCssClass("TBReadonly");
							} else {
								tb.attributes
										.put("onfocus", "SetChange(true);");
							}
							switch (attr.getMyDataType()) {
							case DataType.AppString:
								tb.addAttr("style",
										"width:" + attr.getUIWidth()
												+ "px;border-width:0px;");

								this.Pub1.append(BaseModel.AddTD("width='2px'",
										tb));
								tb.setText(val);
								// 一下注释代码多余 qin 15/9/16----687行
								// if (attr.getUIIsEnable() == false) {
								// tb.addAttr("readonly", "true");
								// tb.setCssClass("TBReadonly");
								// }

								if (attr.getUIHeight() > 25) {
									tb.setTextMode(TextBoxMode.MultiLine);
									tb.attributes.put("Height",
											attr.getUIHeight() + "px");
									tb.setRows(attr.getUIHeightInt() / 25);
								}
								break;
							case DataType.AppDate:
								float dateWidth = attr.getUIWidth() == 100 ? 85
										: attr.getUIWidth();
								tb.attributes.put("style", "width:" + dateWidth
										+ "px;border-width:0px;");
								if (!val.equals("0")) {
									val=val.substring(0, 10);
									tb.setText(val);
								}

								if (attr.getUIIsEnable()) {
									tb.attributes.put("onfouse",
											"WdatePicker();SetChange(false);");
									tb.attributes.put("onChange",
											"SetChange(true);");
									tb.attributes.put("class", "Wdate");
									// tb.CssClass = "easyui-datebox";
									// tb.Attributes["data-options"] =
									// "editable:false";

								} else {
									tb.setReadOnly(true);
								}
								this.Pub1.append(BaseModel.AddTD("width='2px'",
										tb));
								break;
							case DataType.AppDateTime:
								float dateTimeWidth = attr.getUIWidth() == 100 ? 125
										: attr.getUIWidth();
								tb.attributes.put("style", "width:"
										+ dateTimeWidth
										+ "px;border-width:0px;");
								if (!val.equals("0")) {
									tb.setText(val);
								}
								if (attr.getUIIsEnable()) {
									tb.attributes
											.put("onfocus",
													"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});SetChange(false);");
									// tb.CssClass = "easyui-datetimebox";
									// tb.Attributes["data-options"] =
									// "editable:false";

									tb.attributes.put("onChange",
											"SetChange(true);");
									tb.attributes.put("class", "Wdate");

								} else {
									tb.setReadOnly(true);
								}
								this.Pub1.append(BaseModel.AddTD("width='2px'",
										tb));
								break;
							case DataType.AppInt:
								tb.attributes.put("style",
										"width:" + attr.getUIWidth()
												+ "px;text-align:center;border-width:0px;");

								if (attr.getUIIsEnable() == false) {
									tb.attributes.put("class", "TBNumReadonly");
									tb.setReadOnly(true);
								}
								try {
									tb.setText(val);
								} catch (RuntimeException ex) {
									this.Alert(ex.getMessage() + " val =" + val);
									tb.setText("0");
								}

								this.Pub1.append(BaseModel.AddTD(tb));
								break;
							case DataType.AppMoney:
							case DataType.AppRate:
								tb.attributes.put("style",
										"width:" + attr.getUIWidth()
												+ "px;border-width:0px;");
								if (attr.getUIIsEnable() == false) {
									tb.attributes.put("class", "TBNumReadonly");
									tb.setReadOnly(true);
								}

								try {
									tb.setText(String.format("%.2f",
											Double.parseDouble(val)));
								} catch (RuntimeException ex) {
									this.Alert(ex.getMessage() + " val =" + val);
									tb.setText("0.00");
								}
								this.Pub1.append(BaseModel.AddTD(tb));
								break;
							default:
								tb.attributes
										.put("style",
												"width:"
														+ attr.getUIWidth()
														+ "px;text-align:right;border-width:0px;");
								tb.setText(val);

								this.Pub1.append(BaseModel.AddTD(tb));
								break;
							}

							if (attr.getIsNum()
									&& attr.getLGType() == FieldTypeS.Normal) {
								if (tb.getEnabled()) {
									// OnKeyPress="javascript:return VirtyNum(this);"
									if (attr.getMyDataType() == DataType.AppInt) {
										tb.addAttr("onkeyup",
												"C" + dtl.getOID() + "();C"
														+ attr.getKeyOfEn()
														+ "(); ");
										tb.addAttr("OnKeyPress",
												"javascript:return VirtyNum(this,'int');");

										tb.addAttr("onblur",
												"value=value.replace(/[^-?\\d]/g,'');C"
														+ dtl.getOID() + "();C"
														+ attr.getKeyOfEn()
														+ "();");
									} else {
										tb.addAttr("onkeyup",
												"C" + dtl.getOID() + "();C"
														+ attr.getKeyOfEn()
														+ "();");
										tb.addAttr("OnKeyPress",
												"javascript:return VirtyNum(this,'float');");

										tb.addAttr("onblur",
												"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');C"
														+ dtl.getOID() + "();C"
														+ attr.getKeyOfEn()
														+ "();");
									}

									tb.addAttr(
											"style",
											"height:"
													+ attr.getUIHeight()
													+ "px;"
													+ "width:"
													+ attr.getUIWidth()
													+ "px;text-align:right;border-width:0px;");
								} else {
									tb.addAttr("onpropertychange",
											"C" + attr.getKeyOfEn() + "();");
									tb.addAttr(
											"style",
											"height:"
													+ attr.getUIHeight()
													+ "px;"
													+ "width:"
													+ attr.getUIWidth()
													+ "px;text-align:right;border-width:0px;");
								}
							}
							break;
						case DDL:
							switch (attr.getLGType()) {
							case Enum:
								DDL myddl = new DDL();
								myddl.setId("DDL_" + attr.getKeyOfEn() + "_"
										+ dtl.getOID());
								myddl.setName("DDL_" + attr.getKeyOfEn() + "_"
										+ dtl.getOID());
								myddl.attributes.put("onchange",
										"SetChange (true);");
								if (attr.getUIIsEnable()) {
									try {
										myddl.BindSysEnum(attr.getKeyOfEn());
										myddl.SetSelectItem(val);
									} catch (RuntimeException ex) {
										// /////////////////////////////////////////////
										// BP.Sys.PubClass.Alert(ex.getMessage());
									}
								} else {
									myddl.Items
											.add(new ListItem(dtl
													.GetValRefTextByKey(attr
															.getKeyOfEn()), dtl
													.GetValStrByKey(attr
															.getKeyOfEn())));
								}
								myddl.setEnabled(attr.getUIIsEnable());
								this.Pub1.append(BaseModel.AddTDCenter(myddl));
								break;
							case FK:
								DDL ddl1 = new DDL();
								ddl1.setId("DDL_" + attr.getKeyOfEn() + "_"
										+ dtl.getOID());
								ddl1.setName("DDL_" + attr.getKeyOfEn() + "_"
										+ dtl.getOID());
								ddl1.attributes.put("onchange",
										"SetChange (true);");
								ddl1.attributes.put("onfocus",
										"SetChange (true);");
								if (attr.getUIIsEnable()) {
									// ddl1.Attributes["onchange","isChange=true;";
									EntitiesNoName ens = attr
											.getHisEntitiesNoName();
									ens.RetrieveAll();
									ddl1.BindEntities(ens);
									if (!ddl1.SetSelectItem(val)) {
										ddl1.Items.add(0, new ListItem("请选择",
												val));
									}
								} else {
									ddl1.Items
											.add(new ListItem(dtl
													.GetValRefTextByKey(attr
															.getKeyOfEn()), dtl
													.GetValStrByKey(attr
															.getKeyOfEn())));
								}
								ddl1.setEnabled(attr.getUIIsEnable());
								this.Pub1.append(BaseModel.AddTDCenter(ddl1));
								break;
							default:
								break;
							}
							break;
						case CheckBok:
							cb = new CheckBox();
							cb.setId("CB_" + attr.getKeyOfEnToLowerCase() + "_"
									+ dtl.getOID());
							cb.setText(attr.getName());
							if (val.equals("1")) {
								cb.setChecked(true);
							} else {
								cb.setChecked(false);
							}
							cb.attributes.put("onclick", "SetChange(true);");
							this.Pub1.append(BaseModel.AddTD(cb));
							break;
						default:
							break;
						}
					}

					if (mdtl.getIsEnableAthM()) {
						if (dtl.getOID() >= 100) {
							this.Pub1
									.append(BaseModel
											.AddTD("<a href=\"javascript:window.showModalDialog('AttachmentUpload.jsp?IsBTitle=1&PKVal="
													+ dtl.getOID()
													+ "&Ath=AthM&FK_MapData="
													+ mdtl.getNo()
													+ "&FK_FrmAttachment="
													+ mdtl.getNo()
													+ "_AthM','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='../Img/AttachmentM.png' border=0 width='16px') /></a>"));
						} else {
							this.Pub1.append(BaseModel.AddTD(""));
						}
					}

					if (mdtl.getIsEnableM2M()) {
						if (dtl.getOID() >= 100) {
							this.Pub1
									.append(BaseModel
											.AddTD("<a href=\"javascript:window.showModalDialog('M2M.jsp?IsOpen=1&NoOfObj=M2M&OID="
													+ dtl.getOID()
													+ "&FK_MapData="
													+ mdtl.getNo()
													+ "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='../Img/M2M.png' border=0 width='16px' /></a>"));
						} else {
							this.Pub1.append(BaseModel.AddTD(""));
						}
					}

					if (mdtl.getIsEnableM2MM()) {
						if (dtl.getOID() >= 100) {
							this.Pub1
									.append(BaseModel
											.AddTD("<a href=\"javascript:window.showModalDialog('M2MM.jsp?IsOpen=1&NoOfObj=M2MM&OID="
													+ dtl.getOID()
													+ "&FK_MapData="
													+ mdtl.getNo()
													+ "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='../Img/M2M.png' border=0 width='16px' /></a>"));
						} else {
							this.Pub1.append(BaseModel.AddTD(""));
						}
					}

					if (mdtl.getIsDelete() && !this.getIsReadonly()
							&& dtl.getOID() >= 100) {
						if (isRowLock && dtl.getIsRowLock()) {
							this.Pub1
									.append(BaseModel
											.AddTD("<img src='../Img/Btn/Lock.png' class=ICON />")); // 如果当前记录是锁定的，并且启动了锁定设置.
						} else {
							this.Pub1
									.append("<TD border=0><img src='../Img/Btn/Delete.gif' onclick=\"javascript:Del('"
											+ dtl.getOID()
											+ "','"
											+ this.getEnsName()
											+ "','"
											+ this.getRefPKVal()
											+ "','"
											+ this.getPageIdx()
											+ "')\" /></TD>");
						}
					} else if (mdtl.getIsDelete()) {
						if (!this.getIsReadonly()) {
							this.Pub1
									.append("<TD class=TD border=0>&nbsp;</TD>");
						}

					}
					this.Pub1.append(BaseModel.AddTREnd());
				}
			}
		} else {
			for (GEDtl dtl : dtls.ToJavaList()) {
				if (ids.contains("," + dtl.getOID() + ",")) {
					continue;
				}

				ids += dtl.getOID() + ",";
				this.Pub1.append(BaseModel.AddTR());

				if (mdtl.getIsShowIdx()) {
					this.Pub1.append(BaseModel.AddTDIdx(idx++));
				} else {
					this.Pub1.append(BaseModel.AddTD());
				}

				for (MapAttr attr : attrs.ToJavaList()) {
					if (!attr.getUIVisible() || attr.getKeyOfEn().equals("OID")) {
						continue;
					}

					String val = dtl.GetValByKey(attr.getKeyOfEn()).toString();
					if (!attr.getUIIsEnable()
							&& dtl.getOID() >= 100
							&& LinkFields.contains("," + attr.getKeyOfEn()
									+ ",")) {
						if (StringHelper.isNullOrEmpty(val)) {
							val = "...";
						}
						Object tempVar4 = mes.GetEntityByKey(
								MapExtAttr.ExtType, MapExtXmlList.Link,
								MapExtAttr.AttrOfOper, attr.getKeyOfEn());
						MapExt meLink = (MapExt) ((tempVar4 instanceof MapExt) ? tempVar4
								: null);

						Object tempVar5 = new String(meLink.getTag());
						String url = (String) ((tempVar5 instanceof String) ? tempVar5
								: null);
						if (!url.contains("?")) {
							url = url + "?a3=2";
						}

						url = url + "&WebUserNo=" + WebUser.getNo() + "&SID="
								+ WebUser.getSID() + "&EnName=" + mdtl.getNo()
								+ "&OID=" + dtl.getOID();
						if (url.contains("@AppPath")) {
							url = url.replace("@AppPath", "http://"
									+ this.get_request().getRemoteHost()
									+ this.get_request().getRequestURI());
						}
						if (url.contains("@")) {
							if (attrs2.size() == 0) {
								attrs2 = new MapAttrs(mdtl.getNo());
							}
							for (MapAttr item : MapAttrs
									.convertMapAttrs(attrs2)) {
								url = url.replace("@" + item.getKeyOfEn(),
										dtl.GetValStrByKey(item.getKeyOfEn()));
								if (!url.contains("@")) {
									break;
								}
							}
							if (url.contains("@")) {
								// 可能是主表也要有参数
								if (mainEn == null) {
									mainEn = this.getMainEn();
								}
								for (Attr attrM : mainEn.getEnMap().getAttrs()) {
									url = url.replace("@" + attrM.getKey(),
											mainEn.GetValStrByKey(attrM
													.getKey()));
									if (!url.contains("@")) {
										break;
									}
								}
							}
						}
						this.Pub1.append(BaseModel.AddTD("<a href='" + url
								+ "' target='" + meLink.getTag1() + "' >" + val
								+ "</a>"));
						continue;
					}

					switch (attr.getUIContralType()) {
					case TB:
						TextBox tb = new TextBox();
						tb.setId("TB_" + attr.getKeyOfEnToLowerCase() + "_"
								+ dtl.getOID());
						tb.setName("TB_" + attr.getKeyOfEnToLowerCase() + "_"
								+ dtl.getOID());
						tb.setEnabled(attr.getUIIsEnable());
						if (!attr.getUIIsEnable()) {
							tb.addAttr("readonly", "true");
							tb.setCssClass("TBReadonly");
						} else {
							tb.attributes.put("onfocus", "SetChange (true);");
						}
						
						if (attr.getIsNum()
								&& attr.getLGType() == FieldTypeS.Normal) {
							if (tb.getEnabled()) {
								tb.addAttr("onchange",
										"C" + attr.getKeyOfEn() + "()");
								// OnKeyPress="javascript:return VirtyNum(this);"
								if (attr.getMyDataType() == DataType.AppInt) {
									tb.addAttr(
											"onkeyup",
											"C" + dtl.getOID() + "();C"
													+ attr.getKeyOfEn()
													+ "(); ");
									tb.addAttr("OnKeyPress",
													"javascript:return  VirtyNum(this,'int');");
									tb.addAttr("onblur",
													"value=value.replace(/[^-?\\d]/g,'');C"
															+ dtl.getOID()
															+ "();C"
															+ attr.getKeyOfEn()
															+ "();");
								} else {
									tb.addAttr(
											"onkeyup",
											"C" + dtl.getOID() + "();C"
													+ attr.getKeyOfEn()
													+ "(); ");
									tb.addAttr("OnKeyPress",
													"javascript:return  VirtyNum(this,'float');");
									tb.addAttr("onblur",
													"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');C"
															+ dtl.getOID()
															+ "();C"
															+ attr.getKeyOfEn()
															+ "();");
								}
								tb.addAttr("style","width:"
														+ attr.getUIWidth()
														+ "px;text-align:right;border-width:0px;");
							} else {
								tb.addAttr("onpropertychange", "C"+ attr.getKeyOfEn() + "();");
								tb.addAttr("style",
												"width:"
														+ attr.getUIWidth()
														+ "px;text-align:right;border-width:0px;");
							}
						}

						switch (attr.getMyDataType()) {
						case DataType.AppString:
							tb.attributes.put("style", "height:" + attr.getUIHeight() + "px;" + "width:" + attr.getUIWidth() + "px;border-width:0px;");
							tb.setText(val);
							if (!attr.getUIIsEnable()) {
								tb.addAttr("readonly", "true");
								tb.setCssClass("TBReadonly");
							}

							if (attr.getUIHeight() > 25) {
								tb.setTextMode(TextBoxMode.MultiLine);
								tb.attributes.put("Height", attr.getUIHeight() + "px");
								tb.setRows(attr.getUIHeightInt() / 25);
							}
							this.Pub1 .append(BaseModel.AddTD("width='2px'", tb));
							break;
						case DataType.AppDate:
							float dateWidth = attr.getUIWidth() == 100 ? 85 : attr.getUIWidth();

							tb.attributes.put("style", "width:" + dateWidth + "px;" + "border-width:0px;");
								if(val.length() > 11){
									val=val.substring(0, 10);
								}
								
								tb.setText(val);
								
							//tb.attributes.put("readonly", "readonly");
							if (attr.getUIIsEnable()) {
								tb.attributes.put("onfocus", "WdatePicker();SetChange(false);");
								tb.attributes.put("onChange", "SetChange(true);");
								tb.attributes.put("class", "Wdate");

								// tb.CssClass = "easyui-datebox";
								// tb.addAttr("data-options","editable:false";
							} else {
								tb.setReadOnly(true);
							}
							this.Pub1 .append(BaseModel.AddTD("width='2px'", tb));
							break;
						case DataType.AppDateTime:
							float dateTimeWidth = attr.getUIWidth() == 100 ? 125 : attr.getUIWidth();

							tb.attributes.put("style", "width:" + dateTimeWidth + "px;border-width:0px;");
							if (!val.equals("0")) {
								tb.setText(val);
							}
							if (attr.getUIIsEnable()) {
								tb.attributes.put("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});SetChange(false);");
								// tb.CssClass = "easyui-datetimebox";
								// tb.attributes.put("data-options","editable:false";
								tb.attributes.put("onChange", "SetChange(true);");
								tb.attributes.put("class", "Wdate");
							} else {
								tb.setReadOnly(true);
							}
							this.Pub1
									.append(BaseModel.AddTD("width='2px'", tb));
							break;
						case DataType.AppInt:
							tb.attributes.put("style", "width:" + attr.getUIWidth() + "px;text-align:right;border-width:0px;");
							if (!attr.getUIIsEnable()) {
								tb.attributes.put("class", "TBNumReadonly");
								tb.setReadOnly(true);
							}
							try {
								tb.setText(val);
							} catch (RuntimeException ex) {
								// this.Alert(ex.getMessage() + " val =" + val);
								tb.setText("0");
							}
							this.Pub1.append(BaseModel.AddTD(tb));
							break;
						case DataType.AppMoney:
						case DataType.AppRate:
							tb.attributes.put("style", "width:" + attr.getUIWidth() + "px;text-align:right;border-width:0px;");
							if (!attr.getUIIsEnable()) {
								tb.attributes.put("class", "TBNumReadonly");
								tb.setReadOnly(true);
							}

							try {
								DecimalFormat mformat = new DecimalFormat("0.00");
								tb.setText(mformat.format(Double.valueOf(val)));
							} catch (RuntimeException ex) {
								// this.Alert(ex.getMessage() + " val =" + val);
								tb.setText("0.00");
							}
							this.Pub1.append(BaseModel.AddTD(tb));
							break;
						default:
							tb.attributes.put("style", "width:" + attr.getUIWidth() + "px;text-align:right;border-width:0px;");
							tb.setText(val);
							this.Pub1.append(BaseModel.AddTD(tb));
							break;
						}
					
						break;
					case DDL:
						switch (attr.getLGType()) {
						case Enum:
							DDL myddl = new DDL();
							myddl.setId("DDL_" + attr.getKeyOfEn() + "_" + dtl.getOID());
							myddl.setName("DDL_" + attr.getKeyOfEn() + "_" + dtl.getOID());
							myddl.attributes.put("onchange","SetChange( true);");
							if (attr.getUIIsEnable()) {
								try {
									myddl.BindSysEnum(attr.getKeyOfEn());
									myddl.SetSelectItem(val);
								} catch (RuntimeException ex) {
									// BP.Sys.PubClass.Alert(ex.getMessage());
								}
							} else {
								myddl.Items.add(new ListItem(dtl .GetValRefTextByKey(attr.getKeyOfEn()), dtl.GetValStrByKey(attr.getKeyOfEn())));
							}
							myddl.setEnabled(attr.getUIIsEnable());
							this.Pub1.append(BaseModel.AddTDCenter(myddl));
							break;
						case FK:
							DDL ddl1 = new DDL();
							ddl1.setId("DDL_" + attr.getKeyOfEn() + "_"+ dtl.getOID());
							ddl1.setName("DDL_" + attr.getKeyOfEn() + "_"+ dtl.getOID());
							ddl1.attributes.put("onchange", "SetChange(true);");
							ddl1.attributes.put("onfocus", "SetChange(true);");
							if (attr.getUIIsEnable()) {
								// ddl1.Attributes["onchange","isChange=true;";
								EntitiesNoName ens = attr
										.getHisEntitiesNoName();
								ens.RetrieveAll();
								ddl1.BindEntities(ens);
								if (!ddl1.SetSelectItem(val)) {
									ddl1.Items.add(0, new ListItem("请选择", val));
								}
							} else {
								ddl1.Items.add(new ListItem(dtl
										.GetValRefTextByKey(attr.getKeyOfEn()),
										dtl.GetValStrByKey(attr.getKeyOfEn())));
							}
							ddl1.setEnabled(attr.getUIIsEnable());
							this.Pub1.append(BaseModel.AddTDCenter(ddl1));
							break;
						default:
							break;
						}
						break;
					case CheckBok:
						cb = new CheckBox();
						cb.setId("CB_" + attr.getKeyOfEnToLowerCase() + "_"
								+ dtl.getOID());
						cb.setText(attr.getName());
						if (val.equals("1")) {
							cb.setChecked(true);
						} else {
							cb.setChecked(false);
						}
						// cb.Attributes["onchecked","alert('ss'); isChange= true; ";
						cb.attributes.put("onclick", "SetChange(true);");
						this.Pub1.append(BaseModel.AddTD(cb));
						break;
					default:
						break;
					}
				}

				if (mdtl.getIsEnableAthM()) {
					if (dtl.getOID() >= 100) {
						this.Pub1
								.append(BaseModel
										.AddTD("<a href=\"javascript:window.showModalDialog('AttachmentUpload.jsp?IsBTitle=1&PKVal="
												+ dtl.getOID()
												+ "&Ath=AthM&FK_MapData="
												+ mdtl.getNo()
												+ "&FK_FrmAttachment="
												+ mdtl.getNo()
												+ "_AthM','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='../Img/AttachmentM.png' border=0 width='16px' /></a>"));
					} else {
						this.Pub1.append(BaseModel.AddTD(""));
					}
				}

				if (mdtl.getIsEnableM2M()) {
					if (dtl.getOID() >= 100) {
						this.Pub1
								.append(BaseModel
										.AddTD("<a href=\"javascript:window.showModalDialog('M2M.jsp?IsOpen=1&NoOfObj=M2M&OID="
												+ dtl.getOID()
												+ "&FK_MapData="
												+ mdtl.getNo()
												+ "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='../Img/M2M.png' border=0 width='16px' /></a>"));
					} else {
						this.Pub1.append(BaseModel.AddTD());
					}
				}

				if (mdtl.getIsEnableM2MM()) {
					if (dtl.getOID() >= 100) {
						this.Pub1
								.append(BaseModel
										.AddTD("<a href=\"javascript:window.showModalDialog('M2MM.jsp?IsOpen=1&NoOfObj=M2MM&OID="
												+ dtl.getOID()
												+ "&FK_MapData="
												+ mdtl.getNo()
												+ "','m2m','dialogHeight: 500px; dialogWidth: 600px;center: yes; help: no')\"><img src='../Img/M2M.png' border=0 width='16px' /></a>"));
					} else {
						this.Pub1.append(BaseModel.AddTD(""));
					}
				}

				if (mdtl.getIsEnableLink()) {
					Object tempVar6 = new String(mdtl.getLinkUrl());
					String url = (String) ((tempVar6 instanceof String) ? tempVar6
							: null);
					if (!url.contains("?")) {
						url = url + "?a3=2";
					}
					url = url.replace("*", "@");

					if (url.contains("OID=")) {
						url = url + "&WebUserNo=" + WebUser.getNo() + "&SID="
								+ WebUser.getSID() + "&EnName=" + mdtl.getNo();
					} else {
						url = url + "&WebUserNo=" + WebUser.getNo() + "&SID="
								+ WebUser.getSID() + "&EnName=" + mdtl.getNo()
								+ "&OID=" + dtl.getOID();
					}

					if (url.contains("@AppPath")) {
						url = url.replace("@AppPath", "http://"
								+ this.get_request().getRemoteHost()
								+ this.get_request().getRequestURI());
					}

					url = BP.WF.Glo.DealExp(url, dtl, null);
					url = url.replace("@OID", String.valueOf(dtl.getOID()));
					url = url.replace("@FK_Node",
							(new Integer(this.getFK_Node())).toString());
					url = url.replace("'", "");

					if (dtl.getOID() >= 100) {
						this.Pub1.append(BaseModel.AddTD("<a href=\"" + url
								+ "\" target='" + mdtl.getLinkTarget() + "' >"
								+ mdtl.getLinkLabel() + "</a>"));
					} else {
						this.Pub1.append(BaseModel.AddTD(""));
					}
				}

				if (mdtl.getIsDelete() && !this.getIsReadonly()
						&& dtl.getOID() >= 100) {
					if (isRowLock && dtl.getIsRowLock()) {
						this.Pub1
								.append(BaseModel
										.AddTD("<img src='../Img/Btn/Lock.png' class=ICON />")); // 如果当前记录是锁定的，并且启动了锁定设置.
					} else {
						this.Pub1
								.append("<TD border=0><img src='../Img/Btn/Delete.gif' onclick=\"javascript:Del('"
										+ dtl.getOID()
										+ "','"
										+ this.getEnsName()
										+ "','"
										+ this.getRefPKVal()
										+ "','"
										+ this.getPageIdx() + "')\" /></TD>");
					}
				} else if (mdtl.getIsDelete()) {
					if (!this.getIsReadonly()) {
						this.Pub1.append("<TD class=TD border=0>&nbsp;</TD>");
					}
				}
				this.Pub1.append(BaseModel.AddTREnd());

				// Java:
				// /#endregion 增加rows
			}
			if (mdtl.getIsInsert() && !this.getIsReadonly()) {
				this.Pub1.append(BaseModel.AddTR());
				DDL myAdd = new DDL();
				// myAdd. setAutoPostBack(true);
				myAdd.Items.add(new ListItem("+", "+"));
				for (int i = 1; i < 10; i++) {
					myAdd.Items.add(new ListItem((new Integer(i)).toString(),
							(new Integer(i)).toString()));
				}
				String url = BP.WF.Glo.getCCFlowAppPath()
						+ "WF/CCForm/Dtl2.jsp?EnsName=" + this.getEnsName()
						+ "&RefPKVal=" + this.getRefPKVal() + "&PageIdx="
						+ this.getPageIdx() + "&IsCut=0&IsWap="
						+ this.getIsWap() + "&FK_Node=" + this.getFK_Node()
						+ "&Key=" + this.get_request().getParameter("Key");

				myAdd.attributes.put("onchange", "addRow(this,\'" + url + "\',"
						+ _allRowCount + ");");
				this.Pub1.append(BaseModel.AddTD(myAdd));
				for (MapAttr attr : attrs.ToJavaList()) {
					if (!attr.getUIVisible() || attr.getKeyOfEn().equals("OID")) {
						continue;
					}

					// //处理它的默认值.
					this.Pub1.append(BaseModel.AddTD(""));
				}

				if (mdtl.getIsDelete()) {
					if (!this.getIsReadonly()) {
						this.Pub1.append("<TD class=TD border=0>&nbsp;</TD>");
					}
				}
				this.Pub1.append(BaseModel.AddTREnd());

			}
		}

		
		// /#region 拓展属性
		if (!this.getIsReadonly() && mes.size() != 0) {

			this.Pub1
					.append("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");

			ArrayList<GEDtl> geDtls = GEDtls.convertGEDtls(dtls);

			HashMap<String, BaseWebControl> ctrlMap = null;
			if (null != geDtls && !geDtls.isEmpty()) {
				ctrlMap = HtmlUtils.httpParser(this.Pub1.toString(), false);
			}

			for (BP.Sys.GEDtl mydtl : geDtls) {
				// ddl.ID = "DDL_" + attr.getKeyOfEn() + "_" + dtl.getOID();
				for (MapExt me : mes.ToJavaList()) {

					if (me.getExtType().equals(MapExtXmlList.DDLFullCtrl)) {// 自动填充.

						BaseWebControl ddlCtl = ctrlMap.get("DDL_"
								+ me.getAttrOfOper() + "_" + mydtl.getOID());
						if (ddlCtl == null) {
							continue;
						}
						DDL ddlOper = (DDL) ddlCtl;
						ddlOper.attributes.put("onchange",
								"DDLFullCtrl(this.value,\'" + ddlCtl.getId()
										+ "\', \'" + me.getMyPK() + "\')");
						setHtmlByCtrl(ddlOper);

					} else if (me.getExtType().equals(MapExtXmlList.ActiveDDL)) {
						BaseWebControl ddlCtl = ctrlMap.get("DDL_"
								+ me.getAttrOfOper() + "_" + mydtl.getOID());
						if (ddlCtl == null) {
							continue;
						}
						DDL ddlPerant = (DDL) ddlCtl;
						String val, valC;
						DataTable dt;
						// preprocessor in Java:
						// /#warning 此处需要优化
						String ddlC = "DDL_" + me.getAttrsOfActive() + "_"
								+ mydtl.getOID();
						if (ddlPerant.toString().contains("SetChange(true)")) {
							ddlPerant.attributes.put("onchange",
									"  DDLAnsc(this.value, \'" + ddlC
											+ "\', \'" + me.getMyPK() + "\')");
						} else {
							ddlPerant.attributes.put("onchange",
									" SetChange (true); DDLAnsc(this.value, \'"
											+ ddlC + "\', \'" + me.getMyPK()
											+ "\')");
						}
						setHtmlByCtrl(ddlPerant);

						ddlCtl = ctrlMap.get("DDL_" + me.getAttrsOfActive()
								+ "_" + mydtl.getOID());

						if (ddlCtl == null) {
							continue;
						}

						DDL ddlChild = (DDL) ddlCtl;
						val = ddlPerant.getSelectedItemStringVal();
						if (ddlChild.Items.size() == 0) {
							valC = mydtl.GetValStrByKey(me.getAttrsOfActive());
						} else {
							valC = ddlChild.getSelectedItemStringVal();
						}

						String mysql = me.getDoc().replace("@Key", val);
						if (mysql.contains("@") && mydtl.getOID() >= 100) {
							mysql = BP.WF.Glo.DealExp(mysql, mydtl, null);
						} else {
							continue;
							// mysql =
							// mysql.replace("WebUser.No",WebUser.getNo());
							// mysql =
							// mysql.replace("@WebUser.getName()",WebUser.getName());
							// mysql =
							// mysql.replace("@WebUser.FK_Dept",WebUser.getFK_Dept());
						}

						dt = DBAccess.RunSQLReturnTable(mysql);

						ddlChild.Bind(dt, "No", "Name");
						if (ddlChild.SetSelectItem(valC) == false) {
							ddlChild.Items.add(0, new ListItem("请选择" + valC,
									valC));
							ddlChild.SetSelectItemByIndex(0);
						}
						if (!ddlChild.toString().contains("SetChange (true)")) {
							ddlChild.attributes.put("onchange", " SetChange (true);");
						}
						setHtmlByCtrl(ddlChild);
					} else if (me.getExtType()
							.equals(MapExtXmlList.AutoFullDLL))
					// 自动填充下拉框的范围.
					{
						BaseWebControl ddlCtl = ctrlMap.get("DDL_"
								+ me.getAttrOfOper() + "_" + mydtl.getOID());

						if (ddlCtl == null) {
							continue;
						}
						DDL ddlFull = (DDL) ddlCtl;

						String valOld = mydtl
								.GetValStrByKey(me.getAttrOfOper());
						// string valOld =ddlFull.SelectedItemStringVal;

						String fullSQL = me.getDoc().replace(
								"WebUser.No", WebUser.getNo());
						fullSQL = fullSQL.replace("@WebUser.getName()",
								WebUser.getName());
						fullSQL = fullSQL.replace("@WebUser.FK_Dept",
								WebUser.getFK_Dept());
						fullSQL = fullSQL.replace("@WebUser.FK_DeptName",
								WebUser.getFK_DeptName());
						fullSQL = fullSQL.replace("@Key", this.get_request()
								.getParameter("Key"));

						if (fullSQL.contains("@")) {
							Attrs attrsFull = mydtl.getEnMap().getAttrs();
							for (Attr attr : attrsFull) {
								if (fullSQL.contains("@") == false) {
									break;
								}
								if (fullSQL.contains("@" + attr.getKey() + ";") == false)
									continue;
								fullSQL = fullSQL.replace("@" + attr.getKey(),
										mydtl.GetValStrByKey(attr.getKey()));
							}
						}

						if (fullSQL.contains("@")) {
							// 从主表中取数据
							Attrs attrsFull = this.getMainEn().getEnMap()
									.getAttrs();
							for (Attr attr : attrsFull) {
								if (fullSQL.contains("@") == false) {
									break;
								}

								if (fullSQL.contains("@" + attr.getKey()) == false) {
									continue;
								}

								fullSQL = fullSQL.replace(
										"@" + attr.getKey(),
										this.getMainEn().GetValStrByKey(
												attr.getKey()));
							}
						}

						// 宋洪刚 解决如果是退回状态，并且设置自动填充没有值情况下则不进行自动填充！
						DataTable autoFullTable = DBAccess
								.RunSQLReturnTable(fullSQL);
						boolean isHaveValue = (valOld == null || valOld
								.equals("")) ? true : false;
						if (autoFullTable.Rows.size() > 0 || isHaveValue) {

							ddlFull.Items.clear();
							ddlFull.Bind(autoFullTable, "No", "Name");
							if (ddlFull.SetSelectItem(valOld) == false) {
								ddlFull.Items.add(0, new ListItem("请选择"
										+ valOld, valOld));
								ddlFull.SetSelectItemByIndex(0);
							}
						}
						ddlFull.attributes
								.put("onchange", " SetChange (true);");
						setHtmlByCtrl(ddlFull);
					} else if (me.getExtType().equals(MapExtXmlList.TBFullCtrl))// 自动填充.
					{
						BaseWebControl tbCtl = ctrlMap.get("TB_"
								+ me.getAttrOfOperToLowerCase() + "_"
								+ mydtl.getOID());
						if (tbCtl == null) {
							continue;
						}

						TextBox tbAuto = (TextBox) tbCtl;
						if (tbAuto == null) {
							continue;
						}
						tbAuto.attributes.put( "onkeyup", " SetChange (true); DoAnscToFillDiv(this,this.value,\'"
										+ tbAuto.getId() + "\', \'"
										+ me.getMyPK() + "\');");
						tbAuto.attributes.put("AUTOCOMPLETE", "OFF");
						if (!me.getTag().equals("")) {
							// 处理下拉框的选择范围的问题
							String[] strs = me.getTag().split("[$]", -1);
							for (String str : strs) {
								String[] myCtl = str.split("[:]", -1);
								String ctlID = myCtl[0];

								BaseWebControl ddlCtl = ctrlMap.get("DDL_"
										+ ctlID + "_" + mydtl.getOID());
								if (ddlCtl == null) {
									continue;
								}
								DDL ddlC1 = (DDL) ddlCtl;

								String sql = myCtl[1].replace("~", "'");
								sql = sql.replace("WebUser.No",
										WebUser.getNo());
								sql = sql.replace("@WebUser.getName()",
										WebUser.getName());
								sql = sql.replace("@WebUser.FK_Dept",
										WebUser.getFK_Dept());
								sql = sql.replace("@Key", tbAuto.getText()
										.trim());
								DataTable dt = DBAccess.RunSQLReturnTable(sql);
								String valC1 = ddlC1.getSelectedItemStringVal();
								ddlC1.Items.clear();
								for (DataRow dr : dt.Rows) {
									ddlC1.Items.add(new ListItem(dr.getValue(1)
											.toString(), dr.getValue(0)
											.toString()));
								}
								ddlC1.SetSelectItem(valC1);
							}
						}
					} else if (me.getExtType().equals(MapExtXmlList.InputCheck)) {
						BaseWebControl tbCtl = ctrlMap.get("TB_"
								+ me.getAttrOfOperToLowerCase() + "_"
								+ mydtl.getOID());

						if (tbCtl == null) {
							continue;
						}

						TextBox tbCheck = (TextBox) tbCtl;

						if (tbCheck != null) {
							tbCheck.addAttr(
									me.getTag2(),
									" rowPK=" + mydtl.getOID() + "; "
											+ me.getTag1() + "(this);");
						}
					} else if (me.getExtType().equals(MapExtXmlList.PopVal))// 弹出窗.
					{
						BaseWebControl tbCtl = ctrlMap.get("TB_"
								+ me.getAttrOfOperToLowerCase() + "_"
								+ mydtl.getOID());

						if (tbCtl == null) {
							continue;
						}

						TextBox tb = (TextBox) tbCtl;
						tb.attributes.put("onfocus", "SetChange(false);");
						tb.attributes.put(
								"ondblclick",
								" SetChange(false);ReturnVal(this,'"
										+ me.getDoc() + "','sd');");
					} else if (me.getExtType().equals(MapExtXmlList.Link)) // 超链接.
					{
						// TB tb = this.Pub1.GetTBByID("TB_" +
						// me.getAttrOfOper() + "_" + mydtl.getOID());
						// tb.attributes.put("ondblclick"," isChange=true; ReturnVal(this,'"
						// + me.Doc + "','sd');";
					} else if (me.getExtType().equals(
							MapExtXmlList.RegularExpression)) // 正则表达式,对数据控件处理
					{
						BaseWebControl tbCtl = ctrlMap.get("TB_"
								+ me.getAttrOfOperToLowerCase() + "_"
								+ mydtl.getOID());
						if (tbCtl == null) {
							continue;
						}
						TextBox tbExp = (TextBox) tbCtl;
						if (tbExp == null || me.getTag().equals("onsubmit")) {
							continue;
						}
						// 验证输入的正则格式
						String regFilter = me.getDoc();
						if (regFilter.lastIndexOf("/g") < 0
								&& regFilter.lastIndexOf('/') < 0) {
							regFilter = "'" + regFilter + "'";
						}
						// 处理事件
						tbExp.addAttr("" + me.getTag() + "",
								"return txtTest_Onkeyup(this," + regFilter
										+ ",'" + me.getTag1() + "')");

					}
				}
			}
		}
		
		// /#endregion 拓展属性

		
		// /#region 生成合计
		if (mdtl.getIsShowSum() && dtls.size() > 1) {
			this.Pub1.append(BaseModel.AddTRSum());
			this.Pub1.append(BaseModel.AddTD());
			for (MapAttr attr : attrs.ToJavaList()) {
				// 分组字段或隐藏字段不显示 [liold 140602]
				if (!attr.getUIVisible()) {
					continue;
				}
				if (attr.getField().equals(mdtl.getGroupField())
						&& mdtl.getIsEnableGroupField()) {
					continue;
				}

				if (attr.getIsNum() && attr.getLGType() == FieldTypeS.Normal) {
					TextBox tb = new TextBox();
					tb.setId("TB_" + attr.getKeyOfEnToLowerCase());
					tb.setText(attr.getDefVal());
					// tb.ShowType = attr.HisTBType;
					tb.setReadOnly(true);
					tb.setFont("Bold");
					tb.setBackColor("infobackground");
					switch (attr.getMyDataType()) {
					case DataType.AppRate:
					case DataType.AppMoney:
						DecimalFormat mformat = new DecimalFormat("0.00");
						tb.setText(mformat.format(dtls.GetSumDecimalByKey(attr
								.getKeyOfEn())));
						tb.attributes.put("style", "width:" + attr.getUIWidth()
								+ "px;text-align:right;border:none");
						break;
					case DataType.AppInt:
						tb.setText(String.valueOf(dtls.GetSumIntByKey(attr
								.getKeyOfEn())));
						tb.attributes.put("style", "width:" + attr.getUIWidth()
								+ "px;text-align:right;border:none");
						break;
					case DataType.AppFloat:
						tb.setText(String.valueOf(dtls.GetSumFloatByKey(attr
								.getKeyOfEn())));
						tb.attributes.put("style", "width:" + attr.getUIWidth()
								+ "px;text-align:right;border:none");
						break;
					default:
						break;
					}
					this.Pub1.append(BaseModel.AddTD("align=right", tb));
				} else {
					this.Pub1.append(BaseModel.AddTD());
				}
			}
			if (mdtl.getIsEnableAthM()) {
				this.Pub1.append(BaseModel.AddTD());
			}

			if (mdtl.getIsEnableM2M()) {
				this.Pub1.append(BaseModel.AddTD());
			}

			if (mdtl.getIsEnableM2MM()) {
				this.Pub1.append(BaseModel.AddTD());
			}

			if (!mdtl.getIsReadonly()) {
				this.Pub1.append(BaseModel.AddTD());
			}

			this.Pub1.append(BaseModel.AddTREnd());
		}
		
		// /#endregion 生成合计

		
		// /#endregion 生成数据

		this.Pub1.append(BaseModel.AddTableEnd());

		
		// /#region 生成 自动计算行
		if (!this.getIsReadonly()) {
			// 输出自动计算公式
			this.Pub1.append("\n<script language='JavaScript'>");
			MapExts exts = new MapExts(mdtl.getNo());
			for (GEDtl dtl : dtls.ToJavaList()) {
				String top = "\n function C" + dtl.getOID() + "() { \n ";
				String script = "";
				String end = " \n  } ";
				// 添加函数
				if (exts == null || exts.size() == 0) {
					this.Pub1.append(top + script + end);
					continue;
				}
				for (MapExt ext : exts.ToJavaList()) {
					if (!ext.getExtType().equals(MapExtXmlList.AutoFull)) {
						this.Pub1.append(top + script + end);
						continue;
					}

					for (MapAttr attr : attrs.ToJavaList()) {
						if (!attr.getUIVisible()) {
							continue;
						}
						if (!attr.getIsNum()) {
							continue;
						}
						if (attr.getLGType() != FieldTypeS.Normal) {
							continue;
						}

						if (ext.getTag().equals("1")
								&& !ext.getDoc().equals("")) {
							script += this.GenerAutoFull(
									String.valueOf(dtl.getOID()), attrs, ext);
						}
					}
					this.Pub1.append(top + script + end);
				}
			}
			this.Pub1.append("\n</script>");

			// 输出合计算计公式
			for (MapAttr attr : attrs.ToJavaList()) {
				if (!attr.getUIVisible()) {
					continue;
				}

				if (attr.getLGType() != FieldTypeS.Normal) {
					continue;
				}

				if (!attr.getIsNum()) {
					continue;
				}

				String top = "\n<script language='JavaScript'> function C"
						+ attr.getKeyOfEn() + "() { \n ";
				String end = "\n  isChange =true ;  } </script>";
				this.Pub1.append(top + this.GenerSum(attr, dtls) + " ; \t\n"
						+ end);
			}
		}
		
		// /#endregion
	}

	private boolean isAddDDLSelectIdxChange = false;

	// ///////////注意啦，这是一个controller
	private void myAdd_SelectedIndexChanged(Object sender) {
		DDL ddl = (DDL) ((sender instanceof DDL) ? sender : null);
		String val = ddl.getSelectedItemStringVal();
		String url = "";
		isAddDDLSelectIdxChange = true;
		this.Save();
		try {
			int addRow = Integer.parseInt(ddl.getSelectedItemStringVal()
					.replace("+", "").replace("-", ""));
			_allRowCount += addRow;
		} catch (java.lang.Exception e) {

		}

		if (val.contains("+")) {

			url = BP.WF.Glo.getCCFlowAppPath()
					+ "WF/CCForm/Dtl2.jsp?EnsName="
					+ this.getEnsName()
					+ "&RefPKVal="
					+ this.getRefPKVal()
					+ "&PageIdx="
					+ this.getPageIdx()
					+ "&rowCount="
					+ _allRowCount
					+ "&AddRowNum="
					+ ddl.getSelectedItemStringVal().replace("+", "")
							.replace("-", "") + "&IsCut=0&IsWap="
					+ this.getIsWap() + "&FK_Node=" + this.getFK_Node()
					+ "&Key=" + this.get_request().getParameter("Key");
		} else {
			url = BP.WF.Glo.getCCFlowAppPath()
					+ "Dtl2.jsp?EnsName="
					+ this.getEnsName()
					+ "&RefPKVal="
					+ this.getRefPKVal()
					+ "&PageIdx="
					+ this.getPageIdx()
					+ "&rowCount="
					+ _allRowCount
					+ "&AddRowNum="
					+ ddl.getSelectedItemStringVal().replace("+", "")
							.replace("-", "") + "&IsWap=" + this.getIsWap()
					+ "&FK_Node=" + this.getFK_Node() + "&Key="
					+ this.get_request().getParameter("Key");
		}

		try {
			ContextHolderUtils.getResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void Save() {
		MapDtl mdtl = new MapDtl(this.getEnsName());
		GEDtls dtls = new GEDtls(this.getEnsName());
		FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
		// 20150615 xiaozhoupeng 添加，原因ccflow 代码更新 START
		// GEEntity mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
		GEEntity mainEn = null;
		// 20150615 xiaozhoupeng 添加，原因ccflow 代码更新 End

		
		// /#region 从表保存前处理事件.
		if (fes.size() > 0) {
			try {
				// 20150615 xiaozhoupeng 添加，原因ccflow 代码更新 START
				mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				// 20150615 xiaozhoupeng 添加，原因ccflow 代码更新 END

				String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd,
						mainEn);
				if (msg != null) {
					// this.Alert(msg);
				}
			} catch (RuntimeException ex) {
				// this.Alert(ex.getMessage());
				return;
			}
		}
		
		// /#endregion 从表保存前处理事件.

		QueryObject qo = new QueryObject(dtls);
		switch (mdtl.getDtlOpenType()) {
		case ForEmp:
			qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
			qo.addAnd();
			qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
			break;
		case ForWorkID:
			qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
			break;
		case ForFID:
			// 小周鹏 20150611 修改，原因：ccflow 代码更新 Start
			qo.AddWhere(GEDtlAttr.FID, this.getFID());
			// qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
			// 小周鹏 20150611 修改，原因：ccflow 代码更新 End
			break;
		}

		int num = qo.DoQuery("OID", mdtl.getRowsOfList(), this.getPageIdx(),
				false);
		int dtlCount = dtls.size();
		if (getallRowCount() == 0) {
			mdtl.setRowsOfList(mdtl.getRowsOfList() + this.getaddRowNum());
		} else {
			mdtl.setRowsOfList(getallRowCount());
		}
		for (int i = 0; i < mdtl.getRowsOfList() - dtlCount; i++) {
			BP.Sys.GEDtl dt = new GEDtl(this.getEnsName());
			dt.ResetDefaultVal();
			dt.setOID(i);
			dtls.AddEntity(dt);
		}

		// if (num == mdtl.getRowsOfList())
		// {
		// BP.Sys.GEDtl dt1 = new GEDtl(this.EnsName);
		// dt1.ResetDefaultVal();
		// dt1.OID = mdtl.getRowsOfList() + 1;
		// dtls.AddEntity(dt1);
		// }

		Map map = dtls.getGetNewEntity().getEnMap();
		boolean isTurnPage = false;
		String err = "";
		int idx = 0;

		// 判断是否有事件.
		boolean isHaveBefore = false;
		boolean isHaveEnd = false;
		Object tempVar = fes.GetEntityByKey(FrmEventAttr.FK_Event,
				EventListDtlList.DtlItemSaveBefore);
		FrmEvent fe_Before = (FrmEvent) ((tempVar instanceof FrmEvent) ? tempVar
				: null);
		if (fe_Before == null) {
			isHaveBefore = false;
		} else {
			isHaveBefore = true;
		}

		Object tempVar2 = fes.GetEntityByKey(FrmEventAttr.FK_Event,
				EventListDtlList.DtlItemSaveAfter);
		FrmEvent fe_End = (FrmEvent) ((tempVar2 instanceof FrmEvent) ? tempVar2
				: null);
		if (fe_End == null) {
			isHaveEnd = false;
		} else {
			isHaveEnd = true;
		}

		// ...................................
		boolean isRowLock = mdtl.getIsRowLock();
		for (GEDtl dtl : dtls.ToJavaList()) {
			idx++;
			try {
				// this.Pub1.Copy(dtl, String.valueOf(dtl.getOID()), map);

				// 如果是行锁定,就不执行.
				if (isRowLock && dtl.getIsRowLock()) {
					continue;
				}

				if (dtl.getOID() < mdtl.getRowsOfList() + 2) {
					int myOID = (int) dtl.getOID();
					dtl.setOID(0);
					if (dtl.getIsBlank()) {
						continue;
					}

					dtl.setOID(myOID);
					if (dtl.getOID() == mdtl.getRowsOfList() + 1) {
						isTurnPage = true;
					}
					// 小周鹏20150610修改，原因：ccflow 代码更新 Start
					if (mdtl.getDtlOpenType() == DtlOpenType.ForFID) {
						dtl.setRefPK(String.valueOf(getFID()));
						;
					} else {
						dtl.setRefPK(getRefPKVal());
						;
					}

					if (this.getFID() == 0) {
						dtl.setFID(Integer.parseInt(getRefPKVal()));
					} else {
						dtl.setFID(getFID());
					}
					// dtl.setRefPK(this.getRefPKVal());
					//
					// if (this.getFID() != 0) {
					// dtl.setFID(this.getFID());
					// }
					// 小周鹏20150610修改，原因：ccflow 代码更新 End
					if (isHaveBefore) {
						try {
							String r = fes.DoEventNode(
									EventListDtlList.DtlItemSaveBefore, dtl);
							if (r.equals("false") || r.equals("0")) {
								continue;
							}
							err += r;
						} catch (RuntimeException ex) {
							err += ex.getMessage();
							continue;
						}
					}
					try {
						dtl.InsertAsOID(DBAccess.GenerOID("Dtl"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (this.getFID() != 0) {
						dtl.setFID(this.getFID());
					}
					if (isHaveBefore) {
						try {
							err += fes.DoEventNode(
									EventListDtlList.DtlItemSaveBefore, dtl);
						} catch (RuntimeException ex) {
							err += ex.getMessage();
							continue;
						}
					}
					dtl.Update();
				}

				if (isHaveEnd) {
					// 如果有保存后的事件。
					try {
						fes.DoEventNode(EventListDtlList.DtlItemSaveAfter, dtl);
					} catch (RuntimeException ex) {
						err += ex.getMessage();
					}
				}
			} catch (RuntimeException ex) {
				dtl.CheckPhysicsTable();
				err += "Row: " + idx + " Error \r\n" + ex.getMessage();
			}
		}

		if (!err.equals("")) {
			BP.DA.Log.DefaultLogWriteLineInfo(err);
			// this.Alert(err);
			return;
		}

		if (isAddDDLSelectIdxChange) {
			return;
		}

		
		// /#region 从表保存后处理事件。
		if (fes.size() > 0) {
			try {
				String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd,
						mainEn);
				if (msg != null) {
					// this.Alert(msg);
				}
			} catch (RuntimeException ex) {
				// this.Alert(ex.getMessage());
				return;
			}
		}
		
		// /#endregion 处理事件.

		if (isTurnPage) {
			int pageNum = 0;
			int count = this.getDtlCount() + 1;
			java.math.BigDecimal pageCountD = java.math.BigDecimal
					.valueOf(count / mdtl.getRowsOfList()); // 页面个数。

			DecimalFormat mformat = new DecimalFormat("0.0000");
			String[] strs = mformat.format(pageCountD).split("[.]", -1);
			if (Integer.parseInt(strs[1]) > 0) {
				pageNum = Integer.parseInt(strs[0]) + 1;
			} else {
				pageNum = Integer.parseInt(strs[0]);
			}
			try {
				ContextHolderUtils.getResponse().sendRedirect(
						BP.WF.Glo.getCCFlowAppPath()
								+ "WF/CCForm/Dtl2.jsp?EnsName="
								+ this.getEnsName() + "&RefPKVal="
								+ this.getRefPKVal() + "&PageIdx=" + pageNum
								+ "&IsWap=" + this.getIsWap() + "&FK_Node="
								+ this.getFK_Node() + "&FID=" + this.getFID()
								+ "&Key=" + this.getKey());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ContextHolderUtils.getResponse().sendRedirect(
						BP.WF.Glo.getCCFlowAppPath()
								+ "WF/CCForm/Dtl2.jsp?EnsName="
								+ this.getEnsName() + "&RefPKVal="
								+ this.getRefPKVal() + "&PageIdx="
								+ this.getPageIdx() + "&IsWap="
								+ this.getIsWap() + "&FK_Node="
								+ this.getFK_Node() + "&FID=" + this.getFID()
								+ "&Key=" + this.getKey());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public final void ExpExcel() {
		// MapDtl mdtl = new MapDtl(this.getEnsName());
		// // this.setTitle(mdtl.getName());
		// GEDtls dtls = new GEDtls(this.getEnsName());
		// QueryObject qo = new QueryObject(dtls);
		// switch (mdtl.getDtlOpenType()) {
		// case ForEmp:
		// qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
		// // qo.addAnd();
		// // qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
		// break;
		// case ForWorkID:
		// qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
		// break;
		// case ForFID:
		// qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
		// break;
		// }
		// qo.DoQuery();
		//
		// // this.ExportDGToExcelV2(dtls, this.Title + ".xls");
		// // DataTable dt = dtls.ToDataTableDesc();
		// // this.GenerExcel(dtls.ToDataTableDesc(), mdtl.Name + ".xls");
		//
		// this.GenerExcel_pri_Text(dtls.ToDataTableDesc(), mdtl.getName() + "@"
		// + WebUser.getNo() + "@" + DataType.getCurrentData() + ".xls");

		// this.ExportDGToExcelV2(dtls, this.Title + ".xls");
		// dtls.getGetNewEntity().CheckPhysicsTable();
		// ContextHolderUtils.getResponse().sendRedirect("Dtl.jsp?EnsName=" +
		// this.EnsName + "&RefPKVal=" + this.RefPKVal, true);
	}

	/**
	 * 生成列的计算
	 * 
	 * @param pk
	 * @param attrs
	 * @param attr
	 * @return
	 */
	public final String GenerAutoFull(String pk, MapAttrs attrs, MapExt ext) {
		try {
			// if (pk.equals("0"))
			// {
			// return null;
			// }
			String leftTbId = "TB_" + ext.getAttrOfOperToLowerCase() + "_" + pk;
			String left = "\n  document.forms[0]." + leftTbId + ".value = ";
			String right = ext.getDoc();
			
			for (MapAttr mattr : attrs.ToJavaList()) {
				if (!mattr.getUIVisible()) {
					continue;
				}
				if (!mattr.getIsNum())
					continue;

				if (mattr.getLGType() != FieldTypeS.Normal)
					continue;
				String tbID = "TB_" + mattr.getKeyOfEnToLowerCase() + "_" + pk;

				// right = right.Replace("@" + mattr.Name,
				// " parseFloat( document.forms[0]." + tb.ClientID +
				// ".value.replace( ',' ,  '' ) ) ");
				// right = right.Replace("@" + mattr.KeyOfEn,
				// " parseFloat( document.forms[0]." + tb.ClientID +
				// ".value.replace( ',' ,  '' ) ) ");
				right = right.replace("@" + mattr.getName(),"parseFloat(replaceAll(document.forms[0]." + tbID+ ".value,',' ,  ''))");			
				right = right.replace("@" + mattr.getKeyOfEn()," parseFloat( replaceAll(document.forms[0]." + tbID+ ".value, ',' ,  '' ) ) ");
			}
			right = "(" + right + ").toFixed(2);";
			String s = left + right ;
			s += "\t\n  document.forms[0]." + leftTbId+ ".value=VirtyMoney(document.forms[0]." + leftTbId+ ".value) ;";
			return s += " C" + ext.getAttrOfOper() + "();";
		} catch (RuntimeException ex) {
			return null;
		}
	}

	public final String GenerSum(MapAttr mattr, GEDtls dtls) {
		if (dtls.size() <= 1) {
			return "";
		}

		String clientID = "TB_" + mattr.getKeyOfEnToLowerCase();

		String left = "\n  document.forms[0]." + clientID + ".value = ";
		String right = "";
		int i = 0;
		for (GEDtl dtl : dtls.ToJavaList()) {
			String tbID = "TB_" + mattr.getKeyOfEnToLowerCase() + "_"
					+ dtl.getOID();

			if (i == 0) {
				right += " parseVal2Float('" + tbID + "')";
			} else {
				right += " +parseVal2Float('" + tbID + "')";
			}
			i++;
		}
		String s = left + right + " ;";
		switch (mattr.getMyDataType()) {
		case BP.DA.DataType.AppMoney:
		case BP.DA.DataType.AppRate:
			return s += "\t\n  document.forms[0]." + clientID
					+ ".value= VirtyMoney(document.forms[0]." + clientID
					+ ".value ) ;";
		default:
			return s;
		}
	}

	/**
	 * 修改替换指定 view
	 * 
	 * @param control
	 */
	private void setHtmlByCtrl(BaseWebControl control) {
		String replacedHtml = HtmlUtils.setCtrlHtml(control,
				this.Pub1.toString());
		Pub1 = new StringBuilder(replacedHtml);
	}
}