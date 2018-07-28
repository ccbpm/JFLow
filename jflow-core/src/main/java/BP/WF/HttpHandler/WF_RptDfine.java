package BP.WF.HttpHandler;

import org.apache.commons.lang.StringUtils;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.EntitiesSimpleTree;
import BP.En.EntitiesTree;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.Sys.DTSearchWay;
import BP.Sys.GEEntitys;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Sys.UserRegeditAttr;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Rpt.RptDfine;
import BP.Web.WebUser;

public class WF_RptDfine extends WebContralBase{
	
	/**
	 * 构造函数
	 */
	public WF_RptDfine()
	{
	
	}
	
	
	public final String getSearchType()
	{
		return this.GetRequestVal("SearchType");
	}
	/** 流程列表
	 
	 @return 
	 */
	public final String Flowlist_Init()
	{
		DataSet ds = new DataSet();
		String sql = "SELECT No,Name,ParentNo FROM WF_FlowSort ORDER BY ParentNo, Idx";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sort";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		ds.Tables.add(dt);

		sql = "SELECT No,Name,FK_FlowSort FROM WF_Flow ORDER BY FK_FlowSort, Idx";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Flows";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
		}
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}
	
	/** 功能列表
	 
	 @return 
	 * @throws Exception 
*/
	public final String Default_Init() throws Exception
	{
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("My", "我发起的流程");
		ht.put("MyJoin", "我参与的流程");

		RptDfine rd = new RptDfine(this.getFK_Flow());

		///#region 增加本部门发起流程的查询.
		if (rd.getMyDeptRole() == 0)
		{
			//如果仅仅部门领导可以查看: 检查当前人是否是部门领导人.
			if (DBAccess.IsExitsTableCol("Port_Dept", "Leader") == true)
			{
				String sql = "SELECT Leader FROM Port_Dept WHERE No='" + BP.Web.WebUser.getFK_Dept() + "'";
				String strs = DBAccess.RunSQLReturnStringIsNull(sql, null);
				if (strs != null && strs.contains(BP.Web.WebUser.getNo()) == true)
				{
					ht.put("MyDept", "我本部门发起的流程");
				}
			}
		}

		if (rd.getMyDeptRole() == 1)
		{
			//如果部门下所有的人都可以查看: 
			ht.put("MyDept", "我本部门发起的流程");
		}

		if (rd.getMyDeptRole() == 2)
		{
			//如果部门下所有的人都可以查看: 
			ht.put("MyDept", "我本部门发起的流程");
		}
		///#endregion 增加本部门发起流程的查询.

		if (BP.Web.WebUser.getIsAdmin())
		{
			ht.put("Adminer", "高级查询");
		}
		
		Flow fl = new Flow(this.getFK_Flow());
		ht.put("FlowName",fl.getName());

		return BP.Tools.Json.ToJsonEntitiesNoNameModel(ht);
	}
	
	public final String FlowSearch_Init() throws Exception
	{
		if (StringUtils.isEmpty(this.getFK_Flow()))
		{
			return "err@参数FK_Flow不能为空";
		}

		String pageSize = GetRequestVal("pageSize");
		String fcid = "";
		DataSet ds = new DataSet();
		java.util.HashMap<String, String> vals = null;
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getSearchType();

		//报表信息，包含是否显示关键字查询RptIsSearchKey，过滤条件枚举/下拉字段RptSearchKeys，时间段查询方式RptDTSearchWay，时间字段RptDTSearchKey
		MapData md = new MapData();
		md.setNo(rptNo);
		if (md.RetrieveFromDBSources() == 0)
		{
			//如果没有找到，就让其重置一下.
			BP.WF.Rpt.RptDfine rd = new RptDfine(this.getFK_Flow());

			if (this.getSearchType().equals("My"))
			{
				rd.DoReset(this.getSearchType(), "我发起的流程");
			}

			if (this.getSearchType().equals("MyJoin"))
			{
				rd.DoReset(this.getSearchType(), "我参与的流程");
			}

			if (this.getSearchType().equals("Adminer"))
			{
				rd.DoReset(this.getSearchType(), "高级查询");
			}

			md.RetrieveFromDBSources();
		}

		MapAttr ar = null;

		String cfgfix = "_SearchAttrs";
		UserRegedit ur = new UserRegedit();
		ur.setAutoMyPK(false);
		ur.setMyPK(WebUser.getNo() + rptNo + cfgfix);

		if (ur.RetrieveFromDBSources() == 0)
		{
			ur.setMyPK(WebUser.getNo() + rptNo + cfgfix);
			ur.setFK_Emp(WebUser.getNo());
			ur.setCfgKey(rptNo + cfgfix);

			ur.Insert();
		}

		vals = ur.GetVals();
		md.SetPara("T_SearchKey", ur.getSearchKey());

		if (md.getRptDTSearchWay() != DTSearchWay.None)
		{
			ar = new MapAttr(rptNo, md.getRptDTSearchKey());
			md.SetPara("T_DateLabel", ar.getName());

			if (md.getRptDTSearchWay() == DTSearchWay.ByDate)
			{
				md.SetPara("T_DTFrom", ur.GetValStringByKey(UserRegeditAttr.DTFrom));
				md.SetPara("T_DTTo", ur.GetValStringByKey(UserRegeditAttr.DTTo));
			}
			else
			{
				md.SetPara("T_DTFrom", ur.GetValStringByKey(UserRegeditAttr.DTFrom));
				md.SetPara("T_DTTo", ur.GetValStringByKey(UserRegeditAttr.DTTo));
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region //增加显示列信息
		DataRow row = null;
		DataTable dt = new DataTable("Sys_MapAttr");
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);

		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo, MapAttrAttr.Idx);

		for (MapAttr attr : attrs.ToJavaList())
		{
			row = dt.NewRow();
			row.setValue("No", attr.getKeyOfEn());
			row.setValue("Name",attr.getName());
			row.setValue("Width",attr.getUIWidthInt());

			if (attr.getHisAttr().getIsFKorEnum())
			{
				row.setValue("No",attr.getKeyOfEn() + "Text");
			}

			dt.Rows.add(row);
		}

		ds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region //增加枚举/外键字段信息
		attrs = new MapAttrs(rptNo);
		dt = new DataTable("FilterCtrls");
		dt.Columns.Add("Id", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Type", String.class);
		dt.Columns.Add("DataType", Integer.class);
		dt.Columns.Add("DefaultValue", String.class);
		dt.Columns.Add("ValueField", String.class);
		dt.Columns.Add("TextField", String.class);
		dt.Columns.Add("ParentField", String.class);
		String[] ctrls = md.getRptSearchKeys().split("[*]", -1);
		DataTable dtNoName = null;

		for (String ctrl : ctrls)
		{
			//增加判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示
			if (StringUtils.isEmpty(ctrl) || !DotNetToJavaStringHelper.isNullOrEmpty(this.getRequest().getParameter(ctrl)))
			{
				continue;
			}

			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ctrl);
			ar = (MapAttr)((tempVar instanceof MapAttr) ? tempVar : null);
			if (ar == null)
			{
				continue;
			}

			row = dt.NewRow();
			row.setValue("Id", ctrl);
			row.setValue("Name", ar.getName());
			row.setValue("DataType", ar.getMyDataType());
			row.setValue("W", ar.getUIWidth());
           

			//判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示

			switch (ar.getUIContralType())
			{
				case DDL:
					row.setValue("Type", "combo");
					fcid = "DDL_" + ar.getKeyOfEn();

					if (vals.containsKey(fcid))
					{
						if (vals.get(fcid).equals("mvals"))
						{
							AtPara ap = new AtPara(ur.getMVals());
							row.setValue("DefaultValue", ap.GetValStrByKey(ar.getKeyOfEn()));
						}
						else
						{
							row.setValue("DefaultValue", vals.get(fcid));
						}
					}

					switch (ar.getLGType())
					{
						//case DDLShowType.BindSQL:
						//    string sql = ar.UIBindKey;

						//    if (sql.Contains("@Web"))
						//    {
						//        sql = sql.Replace("@WebUser.getNo()", WebUser.getNo());
						//        sql = sql.Replace("@WebUser.Name", WebUser.Name);
						//        sql = sql.Replace("@WebUser.FK_Dept", WebUser.FK_Dept);
						//        sql = sql.Replace("@WebUser.FK_DeptName", WebUser.FK_DeptName);
						//    }

						//    if (sql.Contains("@"))
						//        throw new Exception("不允许使用除@WebUser之外的变量");

						//    dtNoName = DBAccess.RunSQLReturnTable(sql);
						//    dtNoName.TableName = ar.getKeyOfEn();
						//    ds.Tables.add(dtNoName);

						//    row["ValueField"] = "No";
						//    row["TextField"] = "Name";
						//    break;
						//case DDLShowType.Boolean:
						//    dtNoName = GetNoNameDataTable(ar.getKeyOfEn());
						//    dtNoName.Rows.add("all", "全部");
						//    dtNoName.Rows.add("1", "是");
						//    dtNoName.Rows.add("0", "否");
						//    ds.Tables.add(dtNoName);

						//    row["ValueField"] = "No";
						//    row["TextField"] = "Name";
						//    break;
						case FK:
							Entities ens = ar.getHisAttr().getHisFKEns();
							ens.RetrieveAll();
							EntitiesTree treeEns = (EntitiesTree)((ens instanceof EntitiesTree) ? ens : null);

							if (treeEns != null)
							{
								row.setValue("Type", "combotree");
								dtNoName = ens.ToDataTableField(ar.getKeyOfEn());
								//dtNoName.TableName = ar.getKeyOfEn();
								ds.Tables.add(dtNoName);

								row.setValue("ValueField", "No");
								row.setValue("TextField", "Name");
								row.setValue("ParentField", "ParentNo");
							}
							else
							{
								EntitiesSimpleTree treeSimpEns = (EntitiesSimpleTree)((ens instanceof EntitiesSimpleTree) ? ens : null);

								if (treeSimpEns != null)
								{
									row.setValue("Type", "combotree");
									dtNoName = ens.ToDataTableField();
									dtNoName.TableName = ar.getKeyOfEn();
									ds.Tables.add(dtNoName);

									row.setValue("ValueField", "No");
									row.setValue("TextField", "Name");
									row.setValue("ParentField", "ParentNo");
								}
								else
								{
									dtNoName = GetNoNameDataTable(ar.getKeyOfEn());
									dtNoName.Rows.AddDatas("all", "全部");

									for (Entity en : ens.ToJavaListEn())
									{
										dtNoName.Rows.AddDatas(en.GetValStringByKey(ar.getHisAttr().getUIRefKeyValue()), en.GetValStringByKey(ar.getHisAttr().getUIRefKeyText()));
									}

									ds.Tables.add(dtNoName);

									row.setValue("ValueField", "No");
									row.setValue("TextField", "Name");
								}
							}
							break;
						//case DDLShowType.Gender:
						//    dtNoName = GetNoNameDataTable(ar.getKeyOfEn());
						//    dtNoName.Rows.add("all", "全部");
						//    dtNoName.Rows.add("1", "男");
						//    dtNoName.Rows.add("0", "女");
						//    ds.Tables.add(dtNoName);

						//    row["ValueField"] = "No";
						//    row["TextField"] = "Name";
						//    break;
						case Enum:
							dtNoName = GetNoNameDataTable(ar.getKeyOfEn());
							dtNoName.Rows.AddDatas("all", "全部");

							SysEnums enums = new SysEnums(ar.getUIBindKey());

							for (SysEnum en : enums.ToJavaList())
							{
								dtNoName.Rows.AddDatas(en.getIntKey(), en.getLab());
							}

							ds.Tables.add(dtNoName);

							row.setValue("ValueField", "No");
							row.setValue("TextField", "Name");
							break;
						default:
							break;
					}
					break;
				//case UIContralType.CheckBok:
				//    row["Type"] = "checkbox";
				//    fcid = "CB_" + ar.getKeyOfEn();

				//    if (vals.ContainsKey(fcid))
				//        row["DefaultValue"] = Convert.ToBoolean(int.Parse(vals[fcid]));
				//    break;
				default:
					break;
			}

			dt.Rows.add(row);
		}

		ds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region //增加第一页数据
		GEEntitys ges = new GEEntitys(rptNo);
		QueryObject qo = new QueryObject(ges);

//C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
//		switch (this.getSearchType())
//ORIGINAL LINE: case "My":
		if (this.getSearchType().equals("My")) //我发起的.
		{
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
		}
//ORIGINAL LINE: case "MyDept":
		else if (this.getSearchType().equals("MyDept")) //我部门发起的.
		{
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
		}
//ORIGINAL LINE: case "MyJoin":
		else if (this.getSearchType().equals("MyJoin")) //我参与的.
		{
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");
		}
//ORIGINAL LINE: case "Adminer":
		else if (this.getSearchType().equals("Adminer"))
		{
		}
		else
		{
				return "err@" + this.getSearchType() + "标记错误.";
		}

		qo = InitQueryObject(qo, md, ges.getGetNewEntity().getEnMap().getAttrs(), attrs, ur);

		qo.AddWhere(" AND  WFState > 1 ");

		md.SetPara("T_total", qo.GetCount());
		qo.DoQuery("OID", StringUtils.isEmpty(pageSize) ? SystemConfig.getPageSize() : Integer.parseInt(pageSize), 1);
		ds.Tables.add(ges.ToDataTableField("MainData"));
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

		return BP.Tools.Json.ToJson(ds);
	}
	
	private DataTable GetNoNameDataTable(String tableName)
	{
		DataTable dt = new DataTable(tableName);
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);

		return dt;
	}
	
	public final QueryObject InitQueryObject(QueryObject qo, MapData md, Attrs attrs, MapAttrs rptAttrs, UserRegedit ur) throws Exception
	{
		java.util.HashMap<String, String> kvs = null;
		java.util.ArrayList<String> keys = new java.util.ArrayList<String>();
		String cfg = "_SearchAttrs";
		String searchKey = "";
		String val = null;

		kvs = ur.GetVals();

		if ( ! this.getSearchType().equals("Adminer"))
		{
			qo.addAnd();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关键字查询
		if (md.getRptIsSearchKey())
		{
			searchKey = ur.getSearchKey();
		}

		if (StringUtils.isEmpty(searchKey))
		{
			qo.addLeftBracket();
			qo.AddWhere("abc", "all");
			qo.addRightBracket();
		}
		else
		{
			int i = 0;

			for (Attr attr : attrs)
			{
				switch (attr.getMyFieldType())
				{
					case Enum:
					case FK:
					case PKFK:
						continue;
					default:
						break;
				}

				if (attr.getMyDataType() != DataType.AppString)
				{
					continue;
				}

				if (attr.getMyFieldType() == FieldType.RefText)
				{
					continue;
				}

				if (attr.getKey().equals("FK_Dept"))
				{
					continue;
				}

				i++;

				if (i == 1)
				{
					qo.addLeftBracket();
					if (SystemConfig.getAppCenterDBVarStr().equals("@")|| SystemConfig.getAppCenterDBVarStr().equals(":"))
					{
						qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
					}
					else
					{
						qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
					}
					continue;
				}

				qo.addOr();

				if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals(":"))
				{
					qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
				}
				else
				{
					qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
				}
			}

			qo.getMyParas().Add("SKey", searchKey);
			qo.addRightBracket();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Url传参条件
		for (Attr attr : attrs)
		{
			if (DotNetToJavaStringHelper.isNullOrEmpty(this.getRequest().getParameter(attr.getKey())))
			{
				continue;
			}

			qo.addAnd();
			qo.addLeftBracket();

			val = this.getRequest().getParameter(attr.getKey());

			switch (attr.getMyDataType())
			{
				case DataType.AppBoolean:
					qo.AddWhere(attr.getKey(), toStrInt(val));
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
				case DataType.AppString:
					qo.AddWhere(attr.getKey(), val);
					break;
				case DataType.AppDouble:
				case DataType.AppFloat:
				case DataType.AppMoney:
					qo.AddWhere(attr.getKey(), Double.parseDouble(val));
					break;
				case DataType.AppInt:
					qo.AddWhere(attr.getKey(), Integer.parseInt(val));
					break;
				default:
					break;
			}

			qo.addRightBracket();

			if (keys.contains(attr.getKey()) == false)
			{
				keys.add(attr.getKey());
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 过滤条件
		for (MapAttr attr1 : rptAttrs.ToJavaList())
		{
			Attr attr = attr1.getHisAttr();
			//此处做判断，如果在URL中已经传了参数，则不算SearchAttrs中的设置
			if (keys.contains(attr.getKey()))
			{
				continue;
			}

			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			String selectVal = "";
			String cid = "";

			switch (attr.getUIContralType())
			{
				//case UIContralType.TB:
				//    switch (attr.MyDataType)
				//    {
				//        case DataType.AppDate:
				//        case DataType.AppDateTime:
				//            if (attr.MyDataType == DataType.AppDate)
				//                cid = "D_" + attr.Key;
				//            else
				//                cid = "DT_" + attr.Key;

				//            if (kvs.ContainsKey(cid) == false || string.IsNullOrWhiteSpace(kvs[cid]))
				//                continue;

				//            selectVal = kvs[cid];

				//            qo.addAnd();
				//            qo.addLeftBracket();
				//            qo.AddWhere(attr.Key, selectVal);
				//            qo.addRightBracket();
				//            break;
				//        default:
				//            cid = "TB_" + attr.Key;

				//            if (kvs.ContainsKey(cid) == false || string.IsNullOrWhiteSpace(kvs[cid]))
				//                continue;

				//            selectVal = kvs[cid];

				//            qo.addAnd();
				//            qo.addLeftBracket();
				//            qo.AddWhere(attr.Key, " LIKE ", "%" + selectVal + "%");
				//            qo.addRightBracket();
				//            break;
				//    }
				//    break;
				case DDL:
					cid = "DDL_" + attr.getKey();

					if (kvs.containsKey(cid) == false || StringUtils.isEmpty(kvs.get(cid)))
					{
						continue;
					}

					selectVal = kvs.get(cid);

					if (selectVal.equals("all") || selectVal.equals("-1"))
					{
						continue;
					}

					if (selectVal.equals("mvals"))
					{
						// 如果是多选值 
						AtPara ap = new AtPara(ur.getMVals());
						String instr = ap.GetValStrByKey(attr.getKey());

						if (DotNetToJavaStringHelper.isNullOrEmpty(instr))
						{
							if (attr.getKey().equals("FK_Dept") || attr.getKey().equals("FK_Unit"))
							{
								if (attr.getKey().equals("FK_Dept"))
								{
									selectVal = WebUser.getFK_Dept();
								}
							}
							else
							{
								continue;
							}
						}
						else
						{
							instr = instr.replace("..", ".");
							instr = instr.replace(".", "','");
							instr = instr.substring(2);
							instr = instr.substring(0, instr.length() - 2);

							qo.addAnd();
							qo.addLeftBracket();
							qo.AddWhereIn(attr.getKey(), "(" + instr + ")");
							qo.addRightBracket();
							continue;
						}
					}

					qo.addAnd();
					qo.addLeftBracket();

					if (attr.getUIBindKey().equals("BP.Port.Depts") || attr.getUIBindKey().equals("BP.Port.Units")) //判断特殊情况。
					{
						qo.AddWhere(attr.getKey(), " LIKE ", selectVal + "%");
					}
					else
					{
						qo.AddWhere(attr.getKey(), selectVal);
					}

					qo.addRightBracket();
					break;
				//case UIContralType.CheckBok:
				//    cid = "CB_" + attr.Key;

				//    if (kvs.ContainsKey(cid) == false || string.IsNullOrWhiteSpace(kvs[cid]))
				//        continue;

				//    selectVal = kvs[cid];

				//    qo.addAnd();
				//    qo.addLeftBracket();
				//    qo.AddWhere(attr.Key, int.Parse(selectVal));
				//    qo.addRightBracket();
				//    break;
				default:
					break;
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 日期处理
		if (md.getRptDTSearchWay() != DTSearchWay.None)
		{
			String dtKey = md.getRptDTSearchKey();
			String dtFrom = ur.GetValStringByKey(UserRegeditAttr.DTFrom).trim();
			String dtTo = ur.GetValStringByKey(UserRegeditAttr.DTTo).trim();

			if (DotNetToJavaStringHelper.isNullOrEmpty(dtFrom) == true)
			{
				if (md.getRptDTSearchWay() == DTSearchWay.ByDate)
				{
					dtFrom = "1900-01-01";
				}
				else
				{
					dtFrom = "1900-01-01 00:00";
				}
			}

			if (DotNetToJavaStringHelper.isNullOrEmpty(dtTo) == true)
			{
				if (md.getRptDTSearchWay() == DTSearchWay.ByDate)
				{
					dtTo = "2999-01-01";
				}
				else
				{
					dtTo = "2999-12-31 23:59";
				}
			}

			if (md.getRptDTSearchWay() == DTSearchWay.ByDate)
			{
				qo.addAnd();
				qo.addLeftBracket();
				qo.setSQL(dtKey + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(dtKey + " <= '" + dtTo + "'");
				qo.addRightBracket();
			}

			if (md.getRptDTSearchWay() == DTSearchWay.ByDateTime)
			{
				qo.addAnd();
				qo.addLeftBracket();
				qo.setSQL(dtKey + " >= '" + dtFrom + " 00:00'");
				qo.addAnd();
				qo.setSQL(dtKey + " <= '" + dtTo + " 23:59'");
				qo.addRightBracket();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

		return qo;
	}
	
	public boolean toStrInt(String str){
		try {
			int i = Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public final String FlowSearch_Done() throws Exception
	{
		String vals = this.GetRequestVal("vals");
		String searchKey = GetRequestVal("key");
		String dtFrom = GetRequestVal("dtFrom");
		String dtTo = GetRequestVal("dtTo");
		String mvals = GetRequestVal("mvals");
		String pageSize = GetRequestVal("pageSize");
		int pageIdx = Integer.parseInt(GetRequestVal("pageIdx"));

		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getSearchType();
		UserRegedit ur = new UserRegedit();
		ur.setMyPK(WebUser.getNo() + rptNo + "_SearchAttrs");
		ur.RetrieveFromDBSources();

		ur.setSearchKey(searchKey);
		ur.setDTFrom_Data(dtFrom);
		ur.setDTTo_Data(dtTo);
		ur.setVals(vals);
		ur.setMVals(mvals);
		ur.Update();

		DataSet ds = new DataSet();
		MapData md = new MapData(rptNo);
		MapAttrs attrs = new MapAttrs(rptNo);
		GEEntitys ges = new GEEntitys(rptNo);
		QueryObject qo = new QueryObject(ges);

//C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
//		switch (this.getSearchType())
//ORIGINAL LINE: case "My":
		if (this.getSearchType().equals("My")) //我发起的.
		{
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
		}
//ORIGINAL LINE: case "MyDept":
		else if (this.getSearchType().equals("MyDept")) //我部门发起的.
		{
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
		}
//ORIGINAL LINE: case "MyJoin":
		else if (this.getSearchType().equals("MyJoin")) //我参与的.
		{
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");
		}
//ORIGINAL LINE: case "Adminer":
		else if (this.getSearchType().equals("Adminer"))
		{
		}
		else
		{
				return "err@" + this.getSearchType() + "标记错误.";
		}


		qo = InitQueryObject(qo, md, ges.getGetNewEntity().getEnMap().getAttrs(), attrs, ur);
		qo.AddWhere(" AND  WFState > 1 "); //排除空白，草稿数据.


		md.SetPara("T_total", qo.GetCount());
		qo.DoQuery("OID", StringUtils.isEmpty(pageSize) ? SystemConfig.getPageSize() : Integer.parseInt(pageSize), pageIdx);
		ds.Tables.add(ges.ToDataTableField("MainData"));
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		return BP.Tools.Json.ToJson(ds);
	}
}

