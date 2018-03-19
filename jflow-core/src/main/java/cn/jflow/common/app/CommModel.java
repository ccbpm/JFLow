package cn.jflow.common.app;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.ToolBar;
import BP.DA.AtPara;
import BP.DA.DataType;
import BP.En.AddAllLocation;
import BP.En.Attr;
import BP.En.AttrOfSearch;
import BP.En.AttrSearch;
import BP.En.AttrSearchs;
import BP.En.Attrs;
import BP.En.AttrsOfSearch;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.RefMethod;
import BP.En.RefMethods;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Port.Dept;
import BP.Port.Depts;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Sys.DTSearchWay;
import BP.Sys.XML.Search;
import BP.Sys.XML.SearchAttr;
import BP.Sys.XML.Searchs;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Comm.UIRowStyleGlo;
import BP.Web.WebUser;
import BP.XML.XmlEn;

public class CommModel {

	private static String basePath = Glo.getCCFlowAppPath();

	/*
	 * 构造toolbar map 实体属性集合 page ensName 实体集合类名称
	 */
	public static void InitByMap(Map map, int page, String ensName,
			UiFatory ui, ToolBar toolBar) throws Exception {
		UserRegedit ur = new UserRegedit(WebUser.getNo(), ensName
				+ "_SearchAttrs");

		InitByMap(map.IsShowSearchKey, map.DTSearchWay, map.getAttrsOfSearch(),
				map.getSearchAttrs(), null, page, ur, ensName, ui, toolBar);

		/*String cfgKey = ur.getVals();// 当前实体的枚举值与外键条件查询项组合
		String[] keys = cfgKey.split("@");// 组合之间@分隔
		java.util.Map<String, Object> tempContollers = ui.getTmpMap();// 获取所有toolbar组件map
		for (java.util.Map.Entry<String, Object> entry : tempContollers
				.entrySet()) {// 设置默认值
			String tempId = entry.getKey();
			if (null == tempId)
				continue;
			if ("TB_Key".equals(tempId)) {
				TextBox tb = (TextBox) tempContollers.get(tempId);
				tb.setText("");
			} else if ("TB_S_From".equals(tempId)) {
				TextBox tb = (TextBox) tempContollers.get(tempId);
				switch (map.DTSearchWay) {// 日期查询
				case ByDate:
					tb.setText(ur.getDTFrom_Data());
					tb.addAttr("onfocus", "WdatePicker();");
					break;
				case ByDateTime:
					tb.setText(ur.getDTFrom_Datatime());
					tb.addAttr("onfocus",
							"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
					break;
				case None:
				default:
					break;
				}
			} else if ("TB_S_To".equals(tempId)) {
				TextBox tb = (TextBox) tempContollers.get(tempId);
				switch (map.DTSearchWay) {// 日期查询
				case ByDate:
					tb.setText(ur.getDTTo_Data());
					tb.addAttr("onfocus", "WdatePicker();");
					break;
				case ByDateTime:
					tb.setText(ur.getDTTo_Datatime());
					tb.addAttr("onfocus",
							"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
					break;
				case None:
				default:
					break;
				}
			} else {
				if (tempId.indexOf("DDL_") >= 0 && cfgKey.indexOf(tempId) >= 0) {
					for (String key : keys) {
						if (key.length() > 3 && key.indexOf(tempId) >= 0) {
							String[] vals = key.split("=");
							DDL ddl = (DDL) tempContollers.get(tempId);
							boolean isHave = ddl.SetSelectItem(vals[1]);
							if (!isHave) {
								 没有有找到要选择的人员 
								try {
									Attr attr = map.GetAttrByKey(vals[0]
											.replace("DDL_", ""));
									ddl.SetSelectItem(vals[1], attr);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
		}*/
		String two = "";
		String three = "";
	    AttrSearchs temp = map.getSearchAttrs();
        for(AttrSearch attr : temp.subList(0, temp.size())){
        	 if(!StringHelper.isNullOrEmpty(attr.reationGrandChildKey)){//三层级联
        		 DDL bigDDL = toolBar.GetDDLByKey("DDL_" + attr.Key) == null?null:toolBar.GetDDLByKey("DDL_" + attr.Key);
        		 DDL smallDDL = toolBar.GetDDLByKey("DDL_" + attr.reationChildKey) == null?null:toolBar.GetDDLByKey("DDL_" + attr.reationChildKey);
        		 DDL litSmallDDL = toolBar.GetDDLByKey("DDL_" + attr.reationGrandChildKey) == null?null:toolBar.GetDDLByKey("DDL_" + attr.reationGrandChildKey);
        		 three += "\t\n onThreeCasca(\"" + bigDDL.getId() + "\",\""+ smallDDL.getId() +"\",\""+ litSmallDDL.getId() +"\",\""+ensName+"\",\""+attr.reationChildKey+"\",\""+attr.reationGrandChildKey+"\");";
        		 
        	 }else if(!StringHelper.isNullOrEmpty(attr.reationChildKey)){//二层级联
        		 if(StringHelper.isNullOrEmpty(attr.reationParentKey)){
        			 DDL bigDDL = toolBar.GetDDLByKey("DDL_" + attr.Key) == null?null:toolBar.GetDDLByKey("DDL_" + attr.Key);
           		   	 DDL smallDDL = toolBar.GetDDLByKey("DDL_" + attr.reationChildKey) == null?null:toolBar.GetDDLByKey("DDL_" + attr.reationChildKey);
           		   	 two += "\t\n onCasca(\"" + bigDDL.getId() + "\",\""+ smallDDL.getId() +"\",\""+ensName+"\",\""+attr.reationChildKey+"\");";
        		 }
        	 }
        }
        if(two.length()>0 || three.length()>0){//说明有级联下拉框
        	StringBuilder script = new StringBuilder();
        	script.append("\t\n <script type='text/javascript'>");
        	//script.append("\t\n$(document).ready(function(){");
        	script.append(two);
        	script.append(three);
        	//script.append("\t\n}); ");
        	script.append("\t\n </script>");
        	ui.append(script.toString());
        }
	}

	/*
	 * 构造toolbar isShowKey 是否按关键字查询 默认为true sw 按日期查询方式 None(默认) ByDate
	 * ByDateTime attrsOfSearch 非枚举值与外键条件查询(已经被抛弃了) attrsOfFK 枚举值与外键条件查询 attrD1
	 * 属性集合 page ur 注册表信息 就是多项组合查询保存的值
	 */
	public static void InitByMap(boolean isShowKey, DTSearchWay searchWay,
			AttrsOfSearch attrsOfSearch, AttrSearchs attrsOfFK, Attrs attrD1,
			int page, UserRegedit ur, String ensName, UiFatory ui,
			ToolBar toolBar) throws Exception {

		// int keysNum = 0;
		if (isShowKey) {// 关键字查询
			toolBar.AddLab("Lab_Key", "关键字:&nbsp;");
			TextBox tb = ui.creatTextBox("TB_Key");
			tb.setCols(13);
			ui.append(tb);
			// keysNum++;
		}
		ui.append("&nbsp;");
		for (AttrOfSearch attr : attrsOfSearch.subList(0, attrsOfSearch.size())) {// 非枚举值与外键条件查询(已抛弃)
			String key = attr.getKey();
			if (attr.getIsHidden()) {// 是否隐藏
				continue;
			}
			toolBar.AddLab("Lab_" + key, attr.getLab()+":");
			//keysNum++;
//			if (attr.getSymbolEnable()) {
//				DDL ddl = ui.creatDDL("DDL_" + key);
//				ddl.setSelfShowType(DDLShowType.Ens); // attr.UIDDLShowType;
//				ddl.setSelfBindKey("BP.Sys.Operators");
//				ddl.setSelfEnsRefKey("No");
//				ddl.setSelfEnsRefKeyText("Name");
//				ddl.setSelfDefaultVal(attr.getDefaultSymbol());
//				ddl.setSelfAddAllLocation(AddAllLocation.None);
//				ddl.setSelfIsShowVal(false); // /不让显示编号
//				// ddl.ID="DDL_"+attr.Key;
//				// ddl.SelfBind();
//				ddl.SelfBind();
//				ui.append(ddl);
//			}
//			if (attr.getDefaultVal().length() >= 8) {
//				Date mydt = DataType
//						.ParseSysDate2DateTime(attr.getDefaultVal());
//
//				DDL ddl1 = ui.creatDDL("DDL_" + key + "_Year");
//				ddl1.setSelfShowType(DDLShowType.Ens);
//				ddl1.setSelfBindKey("BP.Pub.NDs");
//				ddl1.setSelfEnsRefKey("No");
//				ddl1.setSelfEnsRefKeyText("Name");
//				ddl1.setSelfDefaultVal(DataType.dateToStr(mydt, "yyyy"));
//				ddl1.setSelfAddAllLocation(AddAllLocation.None);
//				ddl1.setSelfIsShowVal(false); // /不让显示编号
//				ddl1.SelfBind();
//				ui.append(ddl1);
//
//				DDL ddl2 = ui.creatDDL("DDL_" + key + "_Month");
//				ddl2.setSelfShowType(DDLShowType.Ens);
//				ddl2.setSelfBindKey("BP.Pub.YFs");
//				ddl2.setSelfEnsRefKey("No");
//				ddl2.setSelfEnsRefKeyText("Name");
//				ddl2.setSelfDefaultVal(DataType.dateToStr(mydt, "MM"));
//				ddl2.setSelfAddAllLocation(AddAllLocation.None);
//				ddl2.setSelfIsShowVal(false); // /不让显示编号
//				ddl2.SelfBind();
//				ui.append(ddl2);
//
//				DDL ddl3 = ui.creatDDL("DDL_" + key + "_Day");
//				ddl3.setSelfShowType(DDLShowType.Ens);
//				ddl3.setSelfBindKey("BP.Pub.Days");
//				ddl3.setSelfEnsRefKey("No");
//				ddl3.setSelfEnsRefKeyText("Name");
//				ddl3.setSelfDefaultVal(DataType.dateToStr(mydt, "dd"));
//				ddl3.setSelfAddAllLocation(AddAllLocation.None);
//				ddl3.setSelfIsShowVal(false); // /不让显示编号
//				ddl3.SelfBind();
//				ui.append(ddl3);
//			} else {
				TextBox tb = ui.creatTextBox("TB_" + key);
				tb.setText(attr.getDefaultVal());
				tb.addAttr("size", String.valueOf(attr.getTBWidth()));
				ui.append(tb);
//			}
		}
		ui.append("&nbsp;");
		switch (searchWay) {// 日期查询
		case ByDate:
			Label lab1_1 = ui.creatLabel("Lab_From");
			lab1_1.setText("日期从:");
			ui.append(lab1_1);

			TextBox tbDT1_1 = ui.creatTextBox("TB_S_From");
			tbDT1_1.setShowType(TBType.Date);
			tbDT1_1.addAttr("onfocus", "WdatePicker();");
			ui.append(tbDT1_1);

			Label lab1_2 = ui.creatLabel("Lab_To");
			lab1_2.setText("到:");
			ui.append(lab1_2);

			TextBox tbDT1_2 = ui.creatTextBox("TB_S_To");
			tbDT1_2.setShowType(TBType.Date);
			tbDT1_2.addAttr("onfocus", "WdatePicker();");
			ui.append(tbDT1_2);
			break;
		case ByDateTime:
			Label lab2_1 = ui.creatLabel("Lab_From");
			lab2_1.setText("日期从:");
			ui.append(lab2_1);

			TextBox tbDT2_1 = ui.creatTextBox("TB_S_From");
			tbDT2_1.setShowType(TBType.DateTime);
			tbDT2_1.addAttr("onfocus",
					"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
			ui.append(tbDT2_1);

			Label lab2_2 = ui.creatLabel("Lab_To");
			lab2_2.setText("到:");
			ui.append(lab2_2);

			TextBox tbDT2_2 = ui.creatTextBox("TB_S_To");
			tbDT2_2.setShowType(TBType.DateTime);
			tbDT2_2.addAttr("onfocus",
					"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
			ui.append(tbDT2_2);
			break;
		case None:
		default:
			break;
		}
		ui.append("&nbsp;");
		for (AttrSearch attr1 : attrsOfFK.subList(0, attrsOfFK.size())) {// 枚举值与外键条件查询
			Attr attr = attr1.HisAttr;
			String key = attr.getKey();
			String desc = attr.getDesc();
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			DDL ddl = ui.creatDDL("DDL_" + key);
			// keysNum++;
			if (attr.getMyFieldType() == FieldType.Enum) {
				ddl.BindSysEnum(attr.getUIBindKey(), false,
						AddAllLocation.TopAndEndWithMVal);
				ddl.Items.get(0).setText(">>" + desc);
				ddl.addAttr("onclick", "DDL_mvals_OnChange(this,'" + ensName
						+ "','" + key + "')");
			}else if(attr.getMyFieldType() == FieldType.BindTable){
				if(!StringHelper.isNullOrEmpty(attr1.reationGrandChildKey)){//三层级联
					ddl.BindTable(attr.getUIBindKey(), attr.getUIRefKeyValue(), attr.getUIRefKeyText(), false,
							AddAllLocation.TopAndEnd);
					ddl.Items.get(0).setText(">>" + desc);
					ddl.addAttr("onchange", "onThreeCasca('DDL_"+attr.getKey()+"','DDL_" + attr1.reationChildKey + "','DDL_" + attr1.reationGrandChildKey
							+ "','"+ensName+"','" + attr1.reationChildKey + "','" + attr1.reationGrandChildKey + "')");
				}else if(!StringHelper.isNullOrEmpty(attr1.reationChildKey)){//二层级联
					if(!StringHelper.isNullOrEmpty(attr1.reationParentKey)){
						ddl.addAttr("onchange", "onTwoCasca('DDL_"+attr1.reationParentKey+"','DDL_"+attr.getKey()+"','DDL_" + attr1.reationChildKey
								+ "','"+ensName+"','" + attr.getKey() + "','" + attr1.reationChildKey + "')");
					}else{
						ddl.BindTable(attr.getUIBindKey(), attr.getUIRefKeyValue(), attr.getUIRefKeyText(), false,
								AddAllLocation.TopAndEnd);
						ddl.Items.get(0).setText(">>" + desc);
						ddl.addAttr("onchange", "onCasca('DDL_"+attr.getKey()+"','DDL_" + attr1.reationChildKey
								+ "','"+ensName+"','" + attr1.reationChildKey + "')");
					}
				}else if(!StringHelper.isNullOrEmpty(attr1.reationParentKey)){
				}else{
					ddl.BindTable(attr.getUIBindKey(), attr.getUIRefKeyValue(), attr.getUIRefKeyText(), false,
							AddAllLocation.TopAndEndWithMVal);
					ddl.Items.get(0).setText(">>" + desc);
					ddl.addAttr("onclick", "DDL_mvals_OnChange(this,'" + ensName
							+ "','" + key + "')");
				}
			}else {// 实体类的操作
				ListItem liMvals = new ListItem("*多项组合..", "mvals");// 为多项组合做准备
				liMvals.addAttr("color", "green");
				liMvals.addAttr("style", "color:green");
				if ("BP.Port.Depts".equals(attr.getUIBindKey())) {// 如果是部门
					ddl.Items.clear();
					Depts depts = new Depts();
					depts.RetrieveAll();
					for (Object obj : depts.subList(0, depts.size())) {
						Dept dept = (Dept) obj;
						ListItem li = new ListItem(dept.getName(), dept.getNo());
						ddl.Items.add(li);
					}
					if (depts.size() > SystemConfig.getMaxDDLNum())
						toolBar.AddLab(
								"lD",
								"<a href=\"javascript:onDDLSelectedMore('DDL_"
										+ key
										+ "', '"
										+ ensName
										+ "', 'BP.Port.Depts', 'No','Name')\" >...</a>");

					if (ddl.Items.size() >= 3)
						ddl.Items.add(liMvals);

					ddl.addAttr("onchange", "DDL_mvals_OnChange(this,'"
							+ ensName + "','" + key + "')");
				} else {
					ddl.Items.clear();
					if (attr.getMyDataType() == DataType.AppBoolean) {
						// ddl.setSelfShowType(DDLShowType.Boolean);
						// ddl.SelfBind();

						ddl.Items.add(new ListItem(">>" + desc, "all"));
						ddl.Items.add(new ListItem("是", "1"));
						ddl.Items.add(new ListItem("否", "0"));
						break;
					}
					Entities ens = attr.getHisFKEns();
					ens.RetrieveAll();
					if(!StringHelper.isNullOrEmpty(attr1.reationGrandChildKey)){//三层级联
						ddl.Items.add(new ListItem(">>" + desc, "all"));
						for (Object obj : ens.subList(0, ens.size())) {
							Entity en = (Entity) obj;
							ddl.Items.add(new ListItem(en.GetValStrByKey("Name"),
									en.GetValStrByKey("No")));
						}
						ddl.addAttr("onchange", "onThreeCasca('DDL_"+attr.getKey()+"','DDL_" + attr1.reationChildKey + "','DDL_" + attr1.reationGrandChildKey
								+ "','"+ensName+"','" + attr1.reationChildKey + "','" + attr1.reationGrandChildKey + "')");
					}else if(!StringHelper.isNullOrEmpty(attr1.reationChildKey)){//二层级联
						if(!StringHelper.isNullOrEmpty(attr1.reationParentKey)){
							ddl.addAttr("onchange", "onTwoCasca('DDL_"+attr1.reationParentKey+"','DDL_"+attr.getKey()+"','DDL_" + attr1.reationChildKey
									+ "','"+ensName+"','" + attr.getKey() + "','" + attr1.reationChildKey + "')");
						}else{
							ddl.Items.add(new ListItem(">>" + desc, "all"));
							for (Object obj : ens.subList(0, ens.size())) {
								Entity en = (Entity) obj;
								ddl.Items.add(new ListItem(en.GetValStrByKey("Name"),
										en.GetValStrByKey("No")));
							}
							ddl.addAttr("onchange", "onCasca('DDL_"+attr.getKey()+"','DDL_" + attr1.reationChildKey
									+ "','"+ensName+"','" + attr1.reationChildKey + "')");
						}
					}else if(!StringHelper.isNullOrEmpty(attr1.reationParentKey)){
					}else{
						ddl.Items.add(new ListItem(">>" + desc, "all"));
						for (Object obj : ens.subList(0, ens.size())) {
							Entity en = (Entity) obj;
							ddl.Items.add(new ListItem(en.GetValStrByKey("Name"),
									en.GetValStrByKey("No")));
						}
						if (ddl.Items.size() >= 4)
							ddl.Items.add(liMvals);

						ddl.addAttr("onchange", "DDL_mvals_OnChange(this,'"
								+ ensName + "','" + key + "')");
					}
				}
			}
			ui.append(ddl);
			ui.append("&nbsp;");
		}
		toolBar.AddLab(
				"search",
				"<input type=button id='Btn_Search' class='Btn' name='Btn_Search' onclick=\"javascript:onSearch();\"  value='查询(S)'  />");
		toolBar.AddLab(
				"reset",
				"<input type=button id='Btn_Reset' class='Btn' name='Btn_Reset' onclick=\"javascript:onReset();\"  value='重置(S)'  />");
	}

	/*
	 * 构造查询条件
	 */
	public static QueryObject GetnQueryObject(Entities ens, Entity en,
			HttpServletRequest request) {
		Map map = en.getEnMap();
		QueryObject qo = InitQueryObjectByEns(ens, map.IsShowSearchKey,
				map.DTSearchWay, map.DTSearchKey, map.getAttrs(),
				map.getAttrsOfSearch(), map.getSearchAttrs(), request);
		String pkStr = en.getPK();
		if ("No".equals(pkStr)) {
			qo.addOrderBy("No");
		} else if ("OID".equals(pkStr)) {
			qo.addOrderBy("OID");
		}
		return qo;
	}

	public static QueryObject InitQueryObjectByEns(Entities ens,
			boolean IsShowSearchKey, DTSearchWay searchWay, String dtKey,
			Attrs attrs, AttrsOfSearch attrsOfSearch, AttrSearchs searchAttrs,
			HttpServletRequest request) {
		QueryObject qo = new QueryObject(ens);
		String keyVal = "";
		if (IsShowSearchKey) {
			keyVal = StringHelper.isEmpty(request.getParameter("TB_Key"), "");
//			else {
//				UserRegedit ur = new UserRegedit();
//				QueryObject urObj = new QueryObject(ur);
//				urObj.AddWhere("MyPK", WebUser.getNo() + ens.toString()
//						+ "_SearchAttrs");
//				urObj.DoQuery();
//
//				keyVal = ur.getSearchKey();
//			}
			// request.getSession().setAttribute("SKey", keyVal);
		}

		if (keyVal.length() >= 1) {
			int i = 0;
			for (Attr attr : attrs) {
				if (attr.getIsFKorEnum())
					continue;

				if (attr.getMyFieldType() == FieldType.RefText)
					continue;

				if ("FK_Dept".equals(attr.getKey()))
					continue;

				switch (attr.getMyDataType()) {
					case DataType.AppRate:
					case DataType.AppMoney:
					case DataType.AppInt:
					case DataType.AppFloat:
					case DataType.AppDouble:
					case DataType.AppBoolean:
					case DataType.AppDate:
					case DataType.AppDateTime:
						continue;
					default:
						break;
				}

				i++;
				if (i == 1) {
					/* 第一次进来。 */
					qo.addLeftBracket();
					switch (SystemConfig.getAppCenterDBType()) {
						case Oracle:
						case Informix:
							qo.AddWhere(attr.getKey(), " LIKE ", " '%'||"
									+ SystemConfig.getAppCenterDBVarStr()
									+ "SKey||'%'");
							break;
						case MySQL:
							qo.AddWhere(attr.getKey(), " LIKE ", "CONCAT('%',"
									+ SystemConfig.getAppCenterDBVarStr()
									+ "SKey,'%')");
							break;
						default:
							qo.AddWhere(attr.getKey(), " LIKE ", " '%'+"
									+ SystemConfig.getAppCenterDBVarStr()
									+ "SKey+'%'");
							break;
					}
					continue;
				}
				qo.addOr();
				switch (SystemConfig.getAppCenterDBType()) {
					case Oracle:
					case Informix:
						qo.AddWhere(attr.getKey(), " LIKE ",
								"'%'||" + SystemConfig.getAppCenterDBVarStr()
										+ "SKey||'%'");
						break;
					case MySQL:
						qo.AddWhere(attr.getKey(), " LIKE ", "CONCAT('%',"
								+ SystemConfig.getAppCenterDBVarStr() + "SKey,'%')");
						break;
					default:
						qo.AddWhere(attr.getKey(), " LIKE ",
								"'%'+" + SystemConfig.getAppCenterDBVarStr()
										+ "SKey+'%'");
						break;
				}
			}
			qo.getMyParas().Add("SKey", keyVal);
			qo.addRightBracket();
		} else {
			qo.addLeftBracket();
			qo.AddWhere("", "all");
			qo.addRightBracket();
		}
		 //普通属性
		String opkey = ""; // 操作符号。
		for (AttrOfSearch attr : attrsOfSearch) {
			if (attr.getIsHidden()) {
				qo.addAnd();
				qo.addLeftBracket();
				qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(),
						attr.getDefaultValRun());
				qo.addRightBracket();
				continue;
			}

//			if (attr.getSymbolEnable()) {
//				DDL ddl = GetDDLByKey("DDL_" + attr.getKey());
//				if (ddl == null) {
//					continue;
//				}
//				opkey = ddl.getSelectedItemStringVal();
//				if ("all".equals(opkey))
//					continue;
//			} else {
//				opkey = attr.getDefaultSymbol();
//			}
			opkey = attr.getDefaultSymbol();
			
			String str = StringHelper.isEmpty(request.getParameter("TB_" + attr.getKey()), "");
			if(!"".equals(str)){
				qo.addAnd();
				qo.addLeftBracket();
				qo.AddWhere(attr.getRefAttrKey(), opkey,
						request.getParameter("TB_" + attr.getKey()));
				qo.addRightBracket();
			}

//			if (attr.getDefaultVal().length() >= 8) {
//				String date = "2005-09-01";
//				try {
//					/* 就可能是年月日。 */
//					String y = this.GetDDLByKey(
//							"DDL_" + attr.getKey() + "_Year")
//							.getSelectedItemStringVal();
//					String m = this.GetDDLByKey(
//							"DDL_" + attr.getKey() + "_Month")
//							.getSelectedItemStringVal();
//					String d = this
//							.GetDDLByKey("DDL_" + attr.getKey() + "_Day")
//							.getSelectedItemStringVal();
//					date = y + "-" + m + "-" + d;
//
//					if (opkey == "<=") {
//						Date dt = DateUtils.addDay(
//								DataType.ParseSysDate2DateTime(date), 1);
//						date = DateUtils
//								.format(dt, DataType.getSysDataFormat());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				qo.AddWhere(attr.getRefAttrKey(), opkey, date);
//			} else {
//				qo.AddWhere(attr.getRefAttrKey(), opkey,
//						request.getParameter("TB_" + attr.getKey()));
//			}
//			qo.addRightBracket();
		}
		// 外键
		for (AttrSearch attr1 : searchAttrs) {
			Attr attr = attr1.HisAttr;

			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			String selectVal  = StringHelper.isEmpty(request.getParameter("DDL_" + attr.getKey()), "");
			if (selectVal.length() == 0)
				continue;

			if ("all".equals(selectVal))
				continue;

			if ("mvals".equals(selectVal)) {
				UserRegedit sUr = new UserRegedit();
				sUr.setMyPK(WebUser.getNo() + ens.toString() + "_SearchAttrs");
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.getMVals();
				AtPara ap = new AtPara(cfgVal);
				String key = attr.getKey();
				String instr = ap.GetValStrByKey(key);
				if (instr == null || "".equals(instr)) {
					if ("FK_Dept".equals(key)) {
						selectVal = WebUser.getFK_Dept();
					} else {
						continue;
					}
				} else {
					//instr = instr.replace("..", ".");
					instr = instr.replace(".", ",");

					String ary = "";
					String array[] = instr.split(",");
					for(String str : array){
						if(ary.length()>0){
							ary += ",'"+str+"'";
						}else{
							ary = "'"+str+"'";
						}
					}
					
					qo.addAnd();
					qo.addLeftBracket();
					qo.AddWhereIn(key, "(" + ary + ")");
					qo.addRightBracket();
					continue;
				}
			}

			qo.addAnd();
			qo.addLeftBracket();

			if ("BP.Port.Depts".equals(attr.getUIBindKey())
					|| "BP.Port.Units".equals(attr.getUIBindKey())) // 判断特殊情况。
				qo.AddWhere(attr.getKey(), " LIKE ", selectVal + "%");
			else
				qo.AddWhere(attr.getKey(), selectVal);
			qo.addRightBracket();
		}
		switch (searchWay) {// 日期查询
		case ByDate:
			String dtFrom1 = StringHelper.isEmpty(
					request.getParameter("TB_S_From"), "");
			String dtTo1 = StringHelper.isEmpty(
					request.getParameter("TB_S_To"), "");
			if(!"".equals(dtFrom1)){
				qo.addAnd();
				qo.addLeftBracket();
				qo.setSQL(dtKey + " >= '" + dtFrom1 + " 00:01'");
				if(!"".equals(dtTo1)){
					qo.addAnd();
					qo.setSQL(dtKey + " <= '" + dtTo1 + " 23:59'");
					qo.addRightBracket();
				}else{
					qo.addRightBracket();
				}
			}else{
				if(!"".equals(dtTo1)){
					qo.addAnd();
					qo.addLeftBracket();
					qo.setSQL(dtKey + " <= '" + dtTo1 + " 23:59'");
					qo.addRightBracket();
				}
			}
			break;
		case ByDateTime:
			String dtFrom2 = StringHelper.isEmpty(
					request.getParameter("TB_S_From"), "");
			String dtTo2 = StringHelper.isEmpty(
					request.getParameter("TB_S_To"), "");
			if(!"".equals(dtFrom2)){
				qo.addAnd();
				qo.addLeftBracket();
				qo.setSQL(dtKey + " >= '" + dtFrom2 + "'");
				if(!"".equals(dtTo2)){
					qo.addAnd();
					qo.setSQL(dtKey + " <= '" + dtTo2 + "'");
					qo.addRightBracket();
				}else{
					qo.addRightBracket();
				}
			}else{
				if(!"".equals(dtTo2)){
					qo.addAnd();
					qo.addLeftBracket();
					qo.setSQL(dtKey + " <= '" + dtTo2 + "'");
					qo.addRightBracket();
				}
			}
			break;
		case None:
		default:
			break;

		}
		return qo;
	}

	public static void DataPanelDtl(StringBuilder sb, Entities ens, int pageIdx) {

		Entity myen = ens.getGetNewEntity();

		Attrs attrs = myen.getEnMap().getAttrs();
		// 获取ConfigEns.xml配置文件 信息(业务需要)
		Attrs selectedAttrs = myen.getEnMap().GetChoseAttrs(ens);
		// 获取Search.xml配置文件 信息(业务需要)
		Searchs cfgs = new Searchs();
		cfgs.RetrieveBy(SearchAttr.For, ens.toString());

		// 生成标题
		sb.append("<table border='1' width='100%' align=left cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#C0C0C0'>");
		sb.append(BaseModel.AddTR());
		sb.append(BaseModel.AddTDTitle("序"));
		// 生成需要显示的标题栏
		for (Attr attrT : selectedAttrs) {
			if (!attrT.getUIVisible())
				continue;
			if ("MyNum".equals(attrT.getKey()))
				continue;
			sb.append(BaseModel.AddTDTitle(attrT.getDesc()));
		}

		// 当前页起始记录
		int idx = SystemConfig.getPageSize() * (pageIdx - 1);

		// #region 用户界面属性设置
		int WinCardH = 600;
		int WinCardW = 820;
		String FocusField = null;// 焦点字段名称
		boolean IsEnableDouclickGlo = true;// 是否启用双击
		boolean IsEnableRefFunc = true;// 是否显示相关功能
		boolean IsEnableFocusField = true;// 是否启用焦点字段
		//boolean isShowOpenICON = true;// 是否启用 OpenICON
		UIRowStyleGlo tableStyle = UIRowStyleGlo.MouseAndAlternately;// 界面风格
		try {
			IsEnableDouclickGlo =true;// ens
					//.GetEnsAppCfgByKeyBoolen("IsEnableDouclickGlo"); // 是否启用双击
			IsEnableRefFunc =true;// ens.GetEnsAppCfgByKeyBoolen("IsEnableRefFunc"); // 是否显示相关功能。
			IsEnableFocusField =false;// ens
					//.GetEnsAppCfgByKeyBoolen("IsEnableFocusField"); // 是否启用焦点字段。
			//isShowOpenICON = ens.GetEnsAppCfgByKeyBoolen("IsEnableOpenICON"); // 是否启用 OpenICON
																				
			tableStyle = UIRowStyleGlo.forValue(0); // 界面风格。

			 WinCardH = 500;// ens.GetEnsAppCfgByKeyInt("WinCardH", 500); // 弹出窗口高度
		     WinCardW = 800;//ens.GetEnsAppCfgByKeyInt("WinCardW", 820); // 弹出窗口宽度
			
			if (IsEnableFocusField)
				FocusField = ""; //ens.GetEnsAppCfgByKeyString("FocusField");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (IsEnableRefFunc)
			sb.append(BaseModel.AddTDTitle());// 为操作增加一列

		sb.append(BaseModel.AddTREnd());// 标题栏结束

		String urlExt = "";
		boolean is1 = false;
		for (Entity en : ens.ToJavaListEn()) {
			String url = BaseModel.GenerEnUrl(en, attrs);// 获取当前列实体属性值
			urlExt = "\"javascript:ShowEn('" + basePath
					+ "WF/App/Comm/Creat.jsp?EnsName=" + ens.toString()
					+ "&PK=" + en.GetValByKey(myen.getPK()) + url + "', 'cd','"
					+ WinCardH + "','" + WinCardW + "');\"";

			switch (tableStyle) {
			case None:
				if (IsEnableDouclickGlo)
					sb.append(BaseModel.AddTR("ondblclick=" + urlExt));
				else
					sb.append(BaseModel.AddTR());
				break;
			case Mouse:
				if (IsEnableDouclickGlo)
					sb.append(BaseModel.AddTRTX("ondblclick=" + urlExt));
				else
					sb.append(BaseModel.AddTRTX());
				break;
			case Alternately:
			case MouseAndAlternately:
				if (IsEnableDouclickGlo)
					sb.append(BaseModel.AddTR(is1, "ondblclick=" + urlExt));
				else
					sb.append(BaseModel.AddTR(is1));
				is1 = !is1;
				break;
			default:
				break;
			}

			idx++;
			sb.append(BaseModel.AddTDIdx(idx));

			// 生成需要展示的列表数据
			for (Attr attr : selectedAttrs) {
				if (!attr.getUIVisible())
					continue;

				if ("MyNum".equals(attr.getKey()))
					continue;

				DataPanelDtlAdd(sb, en, attr, cfgs, url, urlExt, FocusField);
			}

			if (IsEnableRefFunc) {
				String str = "&nbsp;";

				// #region 带参数的方法
				RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
				for (RefMethod func : myreffuncs) {
					if (func.Visable == false || func.IsForEns == false)
						continue;
					str += "<a style='cursor:pointer;' nowrap='true' onclick=\"javascript:RefMethod1('"
							+ basePath
							+ "', '"
							+ func.Index
							+ "', '"
							+ func.Warning
							+ "', '"
							+ func.Target
							+ "', '"
							+ ens.toString()
							+ "','"
							+ url
							+ "') \"  > "
							+ func.GetIcon(basePath) + "" + func.Title + "</a>";
				}
				// #endregion

				// #region 加入他的明细
				EnDtls enDtls = en.getEnMap().getDtls();
				for (EnDtl enDtl : enDtls) {
					str += "[<a onclick=\"javascript:EditDtl1('" + basePath
							+ "', '" + myen.ToStringAtParas() + "',  '"
							+ enDtl.getEnsName() + "', '" + enDtl.getRefKey()
							+ "', '" + url + "&IsShowSum=1')\" >"
							+ enDtl.getDesc() + "</a>]";
				}
				// #endregion

				sb.append("<TD class='TD' style='cursor:pointer;' nowrap=true  >"
						+ str + "</TD>");

			} else {
				//sb.append("<TD class='TD' style='cursor:hand;' nowrap=true></TD>");
				// if (isShowOpenICON)
				// sb.append("<TD class='TD' style='cursor:hand;' nowrap=true><a href="
				// + urlExt +
				// " ><img src='"+getBasePath()+"WF/Img/Btn/open.gif' border=0/></a></TD>");
			}
			sb.append(BaseModel.AddTREnd());
		}

		// #region 求合计代码写在这里
		if(myen.getEnMap().IsCount){
			String NoShowSum = StringHelper.isEmpty(SystemConfig.GetConfigXmlEns("NoShowSum", ens.toString()), "");//配置当前对象不需要合计列
			boolean IsHJ = false;//是否有求合计列
	        for(Attr attr : selectedAttrs){
		        if (attr.getMyFieldType() == FieldType.RefText)
		        	continue;
		
		        if (attr.getUIContralType() == UIContralType.DDL)
		        	continue;
		
		        if (NoShowSum.indexOf("@" + attr.getKey() + "@") != -1)
		        	continue;
		
		        if ("OID".equals(attr.getKey()) || "MID".equals(attr.getKey()) || "WORKID".equals(attr.getKey().toUpperCase()))
		        	continue;
		        
	            switch (attr.getMyDataType()){
	                case DataType.AppDouble:
	                case DataType.AppFloat:
	                case DataType.AppInt:
	                case DataType.AppMoney:
	                    IsHJ = true;
	                    break;
	                default:
	                    break;
	            }
	        }
	        if (IsHJ){
	            sb.append("<tr class='TRSum' >");
	            sb.append(BaseModel.AddTD("合计"));
	            for(Attr attr : selectedAttrs){
	            	 if (!attr.getUIVisible())
	                     continue;

	                 if ("MyNum".equals(attr.getKey()))
	                     continue;
	                 
	                 if (attr.getMyDataType() == DataType.AppBoolean){
	                	 sb.append(BaseModel.AddTD());
	                     continue;
	                 }

	                 if (attr.getUIContralType() == UIContralType.DDL){
	                 	 sb.append(BaseModel.AddTD());
	                     continue;
	                 }
	                 
	                 if ("OID".equals(attr.getKey()) || "MID".equals(attr.getKey()) || "WORKID".equals(attr.getKey().toUpperCase())){
	                 	 sb.append(BaseModel.AddTD());
	                     continue;
	                 }

	                 if (NoShowSum.indexOf("@" + attr.getKey() + "@") != -1){
	                     /*不需要显示它他们的合计。*/
	                 	 sb.append(BaseModel.AddTD());
	                     continue;
	                 }
	                 
	                 switch (attr.getMyDataType()){
		                 case DataType.AppDouble:
		                 	sb.append(BaseModel.AddTDNum(String.valueOf(ens.GetSumDoubleByKey(attr.getKey()))));
		                     break;
		                 case DataType.AppFloat:
		                 	sb.append(BaseModel.AddTDNum(String.valueOf(ens.GetSumFloatByKey(attr.getKey()))));
		                     break;
		                 case DataType.AppInt:
		                 	sb.append(BaseModel.AddTDNum(String.valueOf(ens.GetSumIntByKey(attr.getKey()))));
		                     break;
		                 case DataType.AppMoney:
		                 	sb.append(BaseModel.AddTDJE(ens.GetSumDecimalByKey(attr.getKey())));
		                     break;
		                 default:
		                 	sb.append(BaseModel.AddTD());
		                     break;
	                 }
	            }
	            sb.append(BaseModel.AddTREnd()); 
	        }
		}
		// #endregion

		sb.append(BaseModel.AddTableEnd());
	}

	public static void DataPanelDtlAdd(StringBuilder sb, Entity en, Attr attr,
			Searchs cfgs, String url, String cardUrl, String focusField) {

		if (attr.getUIContralType() == UIContralType.DDL) {
			sb.append(BaseModel.AddTD(en.GetValRefTextByKey(attr.getKey())));
			return;
		}

		/*if (attr.getUIHeight() != 0){//大文本
			sb.append(BaseModel.AddTDDoc("...",	 "..."));
			return; 
		}*/

		String str = en.GetValStrByKey(attr.getKey());
		if (null != focusField && focusField.equals(attr.getKey()))
			str = "<a href=" + cardUrl + ">" + str + "</a>";

		String cfgurl = "";
		str = StringHelper.isEmpty(str, "&nbsp;");
		switch (attr.getMyDataType()) {
		case DataType.AppDate:
		case DataType.AppDateTime:
			sb.append(BaseModel.AddTD(str));
			break;
		case DataType.AppString:
			if (attr.getUIHeight() != 0) {//大文本
				sb.append(BaseModel.AddTDDoc(str, str));
			} else {
//				if (attr.getKey().indexOf("ail") == -1)
					sb.append(BaseModel.AddTD(str));
//				else
//					sb.append(BaseModel.AddTD("<a href=\"javascript:mailto:"
//							+ str + "\"' >" + str + "</a>"));
			}
			break;
		case DataType.AppBoolean:
			if ("1".equals(str))
				sb.append(BaseModel.AddTD("是"));
			else
				sb.append(BaseModel.AddTD("否"));
			break;
		case DataType.AppFloat:
		case DataType.AppInt:
		case DataType.AppRate:
		case DataType.AppDouble:
			for (XmlEn xml : cfgs.subList(0, cfgs.size())) {
				Search pe = (Search) xml;
				if (pe.getAttr().equals(attr.getKey())) {
					cfgurl = pe.getURL();
					Attrs attrs = en.getEnMap().getAttrs();
					for (Attr attr1 : attrs)
						cfgurl = cfgurl.replace("@" + attr1.getKey(),
								en.GetValStringByKey(attr1.getKey()));

					break;
				}
			}
			if ("".equals(cfgurl)) {
				sb.append(BaseModel.AddTDNum(str));
			} else {
				cfgurl = cfgurl.replace("@Keys", url);
				sb.append(BaseModel.AddTDNum("<a href=\"javascript:WinOpen('"
						+ cfgurl + "','dtl1');\" >" + str + "</a>"));
			}
			break;
		case DataType.AppMoney:
			cfgurl = "";
			for (XmlEn xml : cfgs.subList(0, cfgs.size())) {
				Search pe = (Search) xml;
				if (pe.getAttr().equals(attr.getKey())) {
					cfgurl = pe.getURL();
					Attrs attrs = en.getEnMap().getAttrs();
					for (Attr attr2 : attrs)
						cfgurl = cfgurl.replace("@" + attr2.getKey(),
								en.GetValStringByKey(attr2.getKey()));
					break;
				}
			}
			if ("".equals(cfgurl)) {
				sb.append(BaseModel.AddTDJE(new BigDecimal(str)));
			} else {
				cfgurl = cfgurl.replace("@Keys", url);
				sb.append(BaseModel.AddTDNum("<a href=\"javascript:WinOpen('"
						+ cfgurl
						+ "','dtl1');\" >"
						+ (new BigDecimal(str)).setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue()
						+ "</a>"));
			}
			break;
		default:
			break;
		}
	}

	public static void BindPageIdxEasyUi(StringBuilder sb, int totalRecords,
			int pageIdx, int pageSize, String layout, boolean showParentPanel) {
		sb.append("    <style type='text/css'>"
				+ "        #eupage table,#eupage td" + "        {"
				+ "            border: 0;" + "            padding: 0;"
				+ "            text-align: inherit;"
				+ "            background-color: inherit;"
				+ "            color: inherit;"
				+ "            font-size: inherit;" + "        }"
				+ "    </style>");

		if (showParentPanel) {
			sb.append("<div class='easyui-panel'>");
		}

		sb.append(String
				.format("<div id='eupage' class='easyui-pagination' data-options=\"total: %1$s,pageSize: %2$s,pageNumber: %3$s,showPageList: false,showRefresh: false,layout: [%4$s],beforePageText: '第&nbsp;',afterPageText: '&nbsp;/ {pages} 页',displayMsg: '显示 {from} 到 {to} 条，共 {total} 条'\"></div>",
						totalRecords, pageSize, pageIdx, layout));

		if (showParentPanel) {
			sb.append("</div>");
		}

		sb.append("<script type='text/javascript'>");
		sb.append("$('#eupage').pagination({ onSelectPage:function(pageNumber, pageSize){ onSearch(pageNumber); }});");
		sb.append("</script>");
	}
	
	/*
	 * 保存查询状态
	 */
	public static void SaveSearchState(String ensName, String key, HttpServletRequest request){
			
			UserRegedit ur = new UserRegedit();
			ur.setMyPK(WebUser.getNo() + ensName + "_SearchAttrs");
			ur.RetrieveFromDBSources();
			ur.setFK_Emp(WebUser.getNo());
			ur.setCfgKey("SearchAttrs");
			ur.setSearchKey(key);

			if (StringHelper.isNullOrEmpty(key)){
				try{
					ur.setSearchKey(StringHelper.isEmpty(request.getParameter("TB_Key"), ""));
				}catch (Exception e){}
			}

			//查询时间.
			try {
				ur.setDTFrom_Datatime(StringHelper.isEmpty(request.getParameter("TB_S_From"), ""));
				ur.setDTTo_Datatime(StringHelper.isEmpty(request.getParameter("TB_S_To"), ""));
			} catch (Exception e) {}

			
			ur.setFK_Emp(WebUser.getNo());
			ur.setCfgKey(ensName + "_SearchAttrs");
			ur.Save();
		}
}
