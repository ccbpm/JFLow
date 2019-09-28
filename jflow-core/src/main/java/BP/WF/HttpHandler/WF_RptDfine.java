package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Rpt.*;
import BP.WF.Template.*;
import BP.Web.Controls.*;
import BP.WF.*;
import java.util.*;
import java.io.*;
import java.math.*;

/** 
 页面功能实体
*/
public class WF_RptDfine extends WebContralBase
{

		///#region 属性.
	/** 
	 查询类型
	*/
	public final String getSearchType()
	{
		String val = this.GetRequestVal("SearchType");

		if (val == null || val.equals(""))
		{
			val = this.GetRequestVal("GroupType");
		}
		return val;
	}



	/** 
	 分析类型
	*/
	public final String getGroupType()
	{
		return this.GetRequestVal("GroupType");
	}

	public final boolean getIsContainsNDYF()
	{
		return this.GetRequestValBoolen("IsContainsNDYF");
	}

	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		String str = this.GetRequestVal("FK_Dept");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final void setFK_Dept(String value)
	{
		String val = value;
		if (val.equals("all"))
		{
			return;
		}

		if (this.getFK_Dept() == null)
		{
			this.setFK_Dept(value);
			return;
		}
	}


		///#endregion 属性.

	/** 
	 构造函数
	*/
	public WF_RptDfine()
	{
	}

	/** 
	 流程列表
	 
	 @return 
	*/
	public final String Flowlist_Init()
	{
		DataSet ds = new DataSet();
		String sql = "SELECT No,Name,ParentNo FROM WF_FlowSort ORDER BY ParentNo, Idx";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sort";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		ds.Tables.add(dt);

		sql = "SELECT No,Name,FK_FlowSort FROM WF_Flow WHERE IsCanStart=1 ORDER BY FK_FlowSort, Idx";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Flows";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
		}
		ds.Tables.add(dt);

		return BP.Tools.Json.DataSetToJson(ds, false);
	}


		///#region 功能列表
	/** 
	 功能列表
	 
	 @return 
	*/
	public final String Default_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("My", "我发起的流程");
		ht.put("MyJoin", "我审批的流程");

		RptDfine rd = new RptDfine(this.getFK_Flow());
		Paras ps = new Paras();


			///#region 增加本部门发起流程的查询.
		if (rd.getMyDeptRole() == 0)
		{
			/*如果仅仅部门领导可以查看: 检查当前人是否是部门领导人.*/
			if (DBAccess.IsExitsTableCol("Port_Dept", "Leader") == true)
			{
				ps.SQL = "SELECT Leader FROM Port_Dept WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
				ps.Add("No", WebUser.getFK_Dept());
				//string sql = "SELECT Leader FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'";
				String strs = DBAccess.RunSQLReturnStringIsNull(ps, null);
				if (strs != null && strs.contains(WebUser.getNo()) == true)
				{
					ht.put("MyDept", "我本部门发起的流程");
				}
			}
		}

		if (rd.getMyDeptRole() == 1)
		{
			/*如果部门下所有的人都可以查看: */
			ht.put("MyDept", "我本部门发起的流程");
		}

		if (rd.getMyDeptRole() == 2)
		{
			/*如果部门下所有的人都可以查看: */
			ht.put("MyDept", "我本部门发起的流程");
		}

			///#endregion 增加本部门发起流程的查询.

		Flow fl = new Flow(this.getFK_Flow());
		ht.put("FlowName", fl.Name);

		String advEmps = SystemConfig.AppSettings["AdvEmps"];
		if (advEmps != null && advEmps.contains(WebUser.getNo()) == true)
		{
			ht.put("Adminer", "高级查询");
		}
		else
		{
			String data = fl.GetParaString("AdvSearchRight");
			data = "," + data + ",";
			if (data.contains(WebUser.getNo() + ",") == true)
			{
				ht.put("Adminer", "高级查询");
			}
		}

		return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
	}

		///#endregion


		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}

		///#endregion 执行父类的重写方法.


		///#region MyStartFlow.htm 我发起的流程
	public final String FlowSearch_Init()
	{
		if (DataType.IsNullOrEmpty(this.getFK_Flow()))
		{
			return "err@参数FK_Flow不能为空";
		}

		String pageSize = GetRequestVal("pageSize");
		String fcid = "";
		DataSet ds = new DataSet();
		HashMap<String, String> vals = null;
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getSearchType();

		//报表信息，包含是否显示关键字查询RptIsSearchKey，过滤条件枚举/下拉字段RptSearchKeys，时间段查询方式RptDTSearchWay，时间字段RptDTSearchKey
		MapData md = new MapData();
		md.No = rptNo;
		if (md.RetrieveFromDBSources() == 0)
		{
			/*如果没有找到，就让其重置一下.*/
			BP.WF.Rpt.RptDfine rd = new RptDfine(this.getFK_Flow());

			if (this.getSearchType().equals("My"))
			{
				rd.DoReset(this.getSearchType(), "我发起的流程");
			}

			if (this.getSearchType().equals("MyJoin"))
			{
				rd.DoReset(this.getSearchType(), "我审批的流程");
			}

			if (this.getSearchType().equals("MyDept"))
			{
				rd.DoReset(this.getSearchType(), "本部门发起的流程");
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
		ur.AutoMyPK = false;
		ur.setMyPK( WebUser.getNo() + rptNo + cfgfix;

		if (ur.RetrieveFromDBSources() == 0)
		{
			ur.setMyPK( WebUser.getNo() + rptNo + cfgfix;
			ur.FK_Emp = WebUser.getNo();
			ur.CfgKey = rptNo + cfgfix;

			ur.Insert();
		}

		vals = ur.GetVals();
		md.SetPara("RptDTSearchWay", (int)md.RptDTSearchWay);
		md.SetPara("RptDTSearchKey", md.RptDTSearchKey);
		md.SetPara("RptIsSearchKey", md.RptIsSearchKey);

		md.SetPara("T_SearchKey", ur.SearchKey);

		if (md.RptDTSearchWay != DTSearchWay.None)
		{
			ar = new MapAttr(rptNo, md.RptDTSearchKey);
			md.SetPara("T_DateLabel", ar.Name);

			if (md.RptDTSearchWay == DTSearchWay.ByDate)
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

		//判断是否含有导出至模板的模板文件，如果有，则显示导出至模板按钮RptExportToTmp
		String tmpDir = BP.Sys.SystemConfig.PathOfDataUser + "TempleteExpEns\\" + rptNo;
		if ((new File(tmpDir)).isDirectory())
		{
			if (Directory.GetFiles(tmpDir, "*.xls*").Length > 0)
			{
				md.SetPara("T_RptExportToTmp", "1");
			}
		}


			///#region //增加显示列信息
		DataRow row = null;
		DataTable dt = new DataTable("Sys_MapAttr");
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);

		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo, MapAttrAttr.Idx);

		for (MapAttr attr : attrs)
		{
			row = dt.NewRow();
			row.set("No", attr.KeyOfEn);
			row.set("Name", attr.Name);
			row.set("Width", attr.UIWidthInt);
			row.set("UIContralType", attr.UIContralType);

			if (attr.HisAttr.IsFKorEnum)
			{
				row.set("No", attr.KeyOfEn + "Text");
			}

			dt.Rows.add(row);
		}

		ds.Tables.add(dt);

			///#endregion


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
		dt.Columns.Add("W", String.class);
		String[] ctrls = md.RptSearchKeys.split("[*]", -1);
		DataTable dtNoName = null;

		for (String ctrl : ctrls)
		{
			//增加判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示context.Request.QueryString[ctrl]
			if (DataType.IsNullOrEmpty(ctrl) || !DataType.IsNullOrEmpty(HttpContextHelper.RequestParams(ctrl)))
			{
				continue;
			}

			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ctrl);
			ar = tempVar instanceof MapAttr ? (MapAttr)tempVar : null;
			if (ar == null)
			{
				continue;
			}

			row = dt.NewRow();
			row.set("Id", ctrl);
			row.set("Name", ar.Name);
			row.set("DataType", ar.MyDataType);
			row.set("W", ar.UIWidth); //宽度.

			switch (ar.UIContralType)
			{
				case UIContralType.DDL:
					row.set("Type", "combo");
					fcid = "DDL_" + ar.KeyOfEn;
					if (vals.containsKey(fcid))
					{
						if (vals.get(fcid).equals("mvals"))
						{
							AtPara ap = new AtPara(ur.MVals);
							row.set("DefaultValue", ap.GetValStrByKey(ar.KeyOfEn));
						}
						else
						{
							row.set("DefaultValue", vals.get(fcid));
						}
					}

					switch (ar.LGType)
					{
						case FieldTypeS.FK:
							Entities ens = ar.HisAttr.HisFKEns;
							ens.RetrieveAll();
							EntitiesTree treeEns = ens instanceof EntitiesTree ? (EntitiesTree)ens : null;

							if (treeEns != null)
							{
								row.set("Type", "combotree");
								dtNoName = ens.ToDataTableField();
								dtNoName.TableName = ar.KeyOfEn;
								ds.Tables.add(dtNoName);

								row.set("ValueField", "No");
								row.set("TextField", "Name");
								row.set("ParentField", "ParentNo");
							}
							else
							{
								EntitiesTree treeSimpEns = ens instanceof EntitiesTree ? (EntitiesTree)ens : null;

								if (treeSimpEns != null)
								{
									row.set("Type", "combotree");
									dtNoName = ens.ToDataTableField();
									dtNoName.TableName = ar.KeyOfEn;
									ds.Tables.add(dtNoName);

									row.set("ValueField", "No");
									row.set("TextField", "Name");
									row.set("ParentField", "ParentNo");
								}
								else
								{
									dtNoName = GetNoNameDataTable(ar.KeyOfEn);
									dtNoName.Rows.add("all", "全部");

									for (Entity en : ens)
									{
										dtNoName.Rows.add(en.GetValStringByKey(ar.HisAttr.UIRefKeyValue), en.GetValStringByKey(ar.HisAttr.UIRefKeyText));
									}

									ds.Tables.add(dtNoName);

									row.set("ValueField", "No");
									row.set("TextField", "Name");
								}
							}
							break;
						case FieldTypeS.Enum:
							dtNoName = GetNoNameDataTable(ar.KeyOfEn);
							dtNoName.Rows.add("all", "全部");

							SysEnums enums = new SysEnums(ar.UIBindKey);

							for (SysEnum en : enums)
							{
								dtNoName.Rows.add(en.IntKey.toString(), en.Lab);
							}

							ds.Tables.add(dtNoName);

							row.set("ValueField", "No");
							row.set("TextField", "Name");
							break;
						default:
							break;
					}
					break;
				default:
					break;
			}

			dt.Rows.add(row);
		}

		ds.Tables.add(dt);

			///#endregion


			///#region //增加第一页数据
		GEEntitys ges = new GEEntitys(rptNo);
		QueryObject qo = new QueryObject(ges);

		switch (this.getSearchType())
		{
			case "My": //我发起的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
				break;
			case "MyDept": //我部门发起的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
				break;
			case "MyJoin": //我参与的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");
				break;
			case "Adminer":
				break;
			default:
				return "err@" + this.getSearchType() + "标记错误.";
		}

		qo = InitQueryObject(qo, md, ges.GetNewEntity.getEnMap().getAttrs(), attrs, ur);

		qo.AddWhere(" AND  WFState > 1 ");
		qo.AddWhere(" AND FID = 0 ");

		md.SetPara("T_total", qo.GetCount());
		qo.DoQuery("OID", DataType.IsNullOrEmpty(pageSize) ? SystemConfig.PageSize : Integer.parseInt(pageSize), 1);
		ds.Tables.add(ges.ToDataTableField("MainData"));
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

			///#endregion

		return BP.Tools.Json.DataSetToJson(ds, false);
	}


	public final String FlowSearch_Done()
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
		ur.setMyPK( WebUser.getNo() + rptNo + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		ur.SearchKey = searchKey;
		ur.DTFrom_Data = dtFrom;
		ur.DTTo_Data = dtTo;
		ur.Vals = vals;
		ur.MVals = mvals;
		ur.Update();

		DataSet ds = new DataSet();
		MapData md = new MapData(rptNo);
		MapAttrs attrs = new MapAttrs(rptNo);
		GEEntitys ges = new GEEntitys(rptNo);
		QueryObject qo = new QueryObject(ges);

		switch (this.getSearchType())
		{
			case "My": //我发起的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
				break;
			case "MyDept": //我部门发起的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
				break;
			case "MyJoin": //我参与的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");
				break;
			case "Adminer":
				break;
			default:
				return "err@" + this.getSearchType() + "标记错误.";
		}


		qo = InitQueryObject(qo, md, ges.GetNewEntity.getEnMap().getAttrs(), attrs, ur);
		qo.AddWhere(" AND  WFState > 1 "); //排除空白，草稿数据.


		md.SetPara("T_total", qo.GetCount());
		qo.DoQuery("OID", DataType.IsNullOrEmpty(pageSize) ? SystemConfig.PageSize : Integer.parseInt(pageSize), pageIdx);
		ds.Tables.add(ges.ToDataTableField("MainData"));
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	/** 
	 导出
	 
	 @return 
	*/
	public final String FlowSearch_Exp()
	{
		String vals = this.GetRequestVal("vals");
		String searchKey = GetRequestVal("key");
		String dtFrom = GetRequestVal("dtFrom");
		String dtTo = GetRequestVal("dtTo");
		String mvals = GetRequestVal("mvals");


		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getSearchType();
		UserRegedit ur = new UserRegedit();
		ur.setMyPK( WebUser.getNo() + rptNo + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		ur.SearchKey = searchKey;
		ur.DTFrom_Data = dtFrom;
		ur.DTTo_Data = dtTo;
		ur.Vals = vals;
		ur.MVals = mvals;
		ur.Update();


		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo, MapAttrAttr.Idx);

		DataSet ds = new DataSet();
		MapData md = new MapData(rptNo);
		//MapAttrs attrs = new MapAttrs(rptNo);
		GEEntitys ges = new GEEntitys(rptNo);
		QueryObject qo = new QueryObject(ges);

		String title = "数据导出";
		switch (this.getSearchType())
		{
			case "My": //我发起的.
				title = "我发起的流程";
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
				break;
			case "MyDept": //我部门发起的.
				title = "我部门发起的流程";
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
				break;
			case "MyJoin": //我参与的.
				title = "我参与的流程";
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");
				break;
			case "Adminer":
				break;
			default:
				return "err@" + this.getSearchType() + "标记错误.";
		}


		qo = InitQueryObject(qo, md, ges.GetNewEntity.getEnMap().getAttrs(), attrs, ur);
		qo.AddWhere(" AND  WFState > 1 "); //排除空白，草稿数据.
		qo.addOrderByDesc("OID");
		Attrs attrsa = new Attrs();
		for (MapAttr attr : attrs)
		{
			attrsa.Add(attr.HisAttr);
		}

		String filePath = ExportDGToExcel(qo.DoQueryToTable(), ges.GetNewEntity, title, attrsa);


		return filePath;
	}
	/** 
	 流程分組分析 1.获取查询条件 2.获取分组的枚举或者外键值 3.获取分析的信息列表进行求和、求平均
	 
	 @return 
	*/
	public final String FlowGroup_Init()
	{
		if (DataType.IsNullOrEmpty(this.getFK_Flow()))
		{
			return "err@参数FK_Flow不能为空";
		}

		String fcid = "";
		DataSet ds = new DataSet();
		HashMap<String, String> vals = null;
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getGroupType();

		//报表信息，包含是否显示关键字查询RptIsSearchKey，过滤条件枚举/下拉字段RptSearchKeys，时间段查询方式RptDTSearchWay，时间字段RptDTSearchKey
		MapData md = new MapData();
		md.No = rptNo;
		if (md.RetrieveFromDBSources() == 0)
		{
			/*如果没有找到，就让其重置一下.*/
			BP.WF.Rpt.RptDfine rd = new RptDfine(this.getFK_Flow());

			if (this.getGroupType().equals("My"))
			{
				rd.DoReset(this.getGroupType(), "我发起的流程");
			}

			if (this.getGroupType().equals("MyJoin"))
			{
				rd.DoReset(this.getGroupType(), "我审批的流程");
			}

			if (this.getGroupType().equals("MyDept"))
			{
				rd.DoReset(this.getGroupType(), "本部门发起的流程");
			}

			if (this.getGroupType().equals("Adminer"))
			{
				rd.DoReset(this.getGroupType(), "高级查询");
			}

			md.RetrieveFromDBSources();
		}

		MapAttr ar = null;

		//查询条件的信息表
		String cfgfix = "_SearchAttrs";
		UserRegedit ur = new UserRegedit();
		ur.AutoMyPK = false;
		ur.setMyPK( WebUser.getNo() + rptNo + cfgfix;

		if (ur.RetrieveFromDBSources() == 0)
		{
			ur.setMyPK( WebUser.getNo() + rptNo + cfgfix;
			ur.FK_Emp = WebUser.getNo();
			ur.CfgKey = rptNo + cfgfix;

			ur.Insert();
		}

		//分组条件存储的信息表
		cfgfix = "_GroupAttrs";
		UserRegedit groupUr = new UserRegedit();
		groupUr.AutoMyPK = false;
		groupUr.setMyPK( WebUser.getNo() + rptNo + cfgfix;

		if (groupUr.RetrieveFromDBSources() == 0)
		{
			groupUr.setMyPK( WebUser.getNo() + rptNo + cfgfix;
			groupUr.FK_Emp = WebUser.getNo();
			groupUr.CfgKey = rptNo + cfgfix;

			groupUr.Insert();
		}



		vals = ur.GetVals();
		md.SetPara("RptDTSearchWay", (int)md.RptDTSearchWay);
		md.SetPara("RptDTSearchKey", md.RptDTSearchKey);
		md.SetPara("RptIsSearchKey", md.RptIsSearchKey);
		md.SetPara("T_SearchKey", ur.SearchKey);

		if (md.RptDTSearchWay != DTSearchWay.None)
		{
			ar = new MapAttr(rptNo, md.RptDTSearchKey);
			md.SetPara("T_DateLabel", ar.Name);

			if (md.RptDTSearchWay == DTSearchWay.ByDate)
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

		//判断是否含有导出至模板的模板文件，如果有，则显示导出至模板按钮RptExportToTmp
		String tmpDir = BP.Sys.SystemConfig.PathOfDataUser + "TempleteExpEns\\" + rptNo;
		if ((new File(tmpDir)).isDirectory())
		{
			if (Directory.GetFiles(tmpDir, "*.xls*").Length > 0)
			{
				md.SetPara("T_RptExportToTmp", "1");
			}
		}


			///#region //显示的内容
		DataRow row = null;
		DataTable dt = new DataTable("Group_MapAttr");
		dt.Columns.Add("Field", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Checked", String.class);


		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo, MapAttrAttr.Idx);

		for (MapAttr attr : attrs)
		{
			if (attr.UIContralType == UIContralType.DDL)
			{
				DataRow dr = dt.NewRow();
				dr.set("Field", attr.KeyOfEn);
				dr.set("Name", attr.HisAttr.Desc);

				// 根据状态 设置信息.
				if (groupUr.Vals.indexOf(attr.KeyOfEn) != -1)
				{
					dr.set("Checked", "true");
				}

				if (groupUr.Vals.indexOf(attr.KeyOfEn) != -1)
				{
					dr.set("Checked", "true");
				}

				dt.Rows.add(dr);
			}
		}
		ds.Tables.add(dt);

			///#endregion


			///#region //分析的内容
		dt = new DataTable("Analysis_MapAttr");
		dt.Columns.Add("Field", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Checked", String.class);

		//如果不存在分析项手动添加一个分析项
		DataRow dtr = dt.NewRow();
		dtr.set("Field", "Group_Number");
		dtr.set("Name", "数量");
		dtr.set("Checked", "true");
		dt.Rows.add(dtr);

		DataTable ddlDt = new DataTable();
		ddlDt.TableName = "Group_Number";
		ddlDt.Columns.Add("No");
		ddlDt.Columns.Add("Name");
		ddlDt.Columns.Add("Selected");
		DataRow ddlDr = ddlDt.NewRow();
		ddlDr.set("No", "SUM");
		ddlDr.set("Name", "求和");
		ddlDr.set("Selected", "true");
		ddlDt.Rows.add(ddlDr);
		ds.Tables.add(ddlDt);


		for (MapAttr attr : attrs)
		{
			if (attr.IsPK || attr.IsNum == false)
			{
				continue;
			}
			if (attr.UIContralType == UIContralType.TB == false)
			{
				continue;
			}
			if (attr.UIVisible == false)
			{
				continue;
			}
			if (attr.HisAttr.MyFieldType == FieldType.FK)
			{
				continue;
			}
			if (attr.HisAttr.MyFieldType == FieldType.Enum)
			{
				continue;
			}
			if (attr.KeyOfEn.equals("OID") || attr.KeyOfEn.equals("WorkID") || attr.KeyOfEn.equals("MID"))
			{
				continue;
			}

			dtr = dt.NewRow();
			dtr.set("Field", attr.KeyOfEn);
			dtr.set("Name", attr.HisAttr.Desc);


			// 根据状态 设置信息.
			if (groupUr.Vals.indexOf(attr.KeyOfEn) != -1)
			{
				dtr.set("Checked", "true");
			}
			dt.Rows.add(dtr);

			ddlDt = new DataTable();
			ddlDt.Columns.Add("No");
			ddlDt.Columns.Add("Name");
			ddlDt.Columns.Add("Selected");
			ddlDt.TableName = attr.KeyOfEn;

			ddlDr = ddlDt.NewRow();
			ddlDr.set("No", "SUM");
			ddlDr.set("Name", "求和");
			if (groupUr.Vals.indexOf("@" + attr.KeyOfEn + "=SUM") != -1)
			{
				ddlDr.set("Selected", "true");
			}
			ddlDt.Rows.add(ddlDr);

			ddlDr = ddlDt.NewRow();
			ddlDr.set("No", "AVG");
			ddlDr.set("Name", "求平均");
			if (groupUr.Vals.indexOf("@" + attr.KeyOfEn + "=AVG") != -1)
			{
				ddlDr.set("Selected", "true");
			}
			ddlDt.Rows.add(ddlDr);

			if (this.getIsContainsNDYF())
			{
				ddlDr = ddlDt.NewRow();
				ddlDr.set("No", "AMOUNT");
				ddlDr.set("Name", "求累计");
				if (groupUr.Vals.indexOf("@" + attr.KeyOfEn + "=AMOUNT") != -1)
				{
					ddlDr.set("Selected", "true");
				}
				ddlDt.Rows.add(ddlDr);
			}

			ds.Tables.add(ddlDt);

		}
		ds.Tables.add(dt);

			///#endregion


			///#region //增加枚举/外键字段信息
		attrs = new MapAttrs(rptNo);
		dt = new DataTable("FilterCtrls");
		dt.Columns.Add("Field", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Type", String.class);
		dt.Columns.Add("DataType", Integer.class);
		dt.Columns.Add("DefaultValue", String.class);
		dt.Columns.Add("ValueField", String.class);
		dt.Columns.Add("TextField", String.class);
		dt.Columns.Add("ParentField", String.class);
		dt.Columns.Add("W", String.class);
		String[] ctrls = md.RptSearchKeys.split("[*]", -1);
		DataTable dtNoName = null;

		for (String ctrl : ctrls)
		{
			//增加判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示context.Request.QueryString[ctrl]
			if (DataType.IsNullOrEmpty(ctrl) || !DataType.IsNullOrEmpty(HttpContextHelper.RequestParams(ctrl)))
			{
				continue;
			}

			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ctrl);
			ar = tempVar instanceof MapAttr ? (MapAttr)tempVar : null;
			if (ar == null)
			{
				continue;
			}

			row = dt.NewRow();
			row.set("Field", ctrl);
			row.set("Name", ar.Name);
			row.set("DataType", ar.MyDataType);
			row.set("W", ar.UIWidth); //宽度.

			switch (ar.UIContralType)
			{
				case UIContralType.DDL:
					row.set("Type", "combo");
					fcid = "DDL_" + ar.KeyOfEn;
					if (vals.containsKey(fcid))
					{
						if (vals.get(fcid).equals("mvals"))
						{
							AtPara ap = new AtPara(ur.MVals);
							row.set("DefaultValue", ap.GetValStrByKey(ar.KeyOfEn));
						}
						else
						{
							row.set("DefaultValue", vals.get(fcid));
						}
					}

					switch (ar.LGType)
					{

						case FieldTypeS.FK:
							Entities ens = ar.HisAttr.HisFKEns;
							ens.RetrieveAll();
							EntitiesTree treeEns = ens instanceof EntitiesTree ? (EntitiesTree)ens : null;

							if (treeEns != null)
							{
								row.set("Type", "combotree");
								dtNoName = ens.ToDataTableField();
								dtNoName.TableName = ar.KeyOfEn;
								ds.Tables.add(dtNoName);

								row.set("ValueField", "No");
								row.set("TextField", "Name");
								row.set("ParentField", "ParentNo");
							}
							else
							{
								EntitiesTree treeSimpEns = ens instanceof EntitiesTree ? (EntitiesTree)ens : null;

								if (treeSimpEns != null)
								{
									row.set("Type", "combotree");
									dtNoName = ens.ToDataTableField();
									dtNoName.TableName = ar.KeyOfEn;
									ds.Tables.add(dtNoName);

									row.set("ValueField", "No");
									row.set("TextField", "Name");
									row.set("ParentField", "ParentNo");
								}
								else
								{
									dtNoName = GetNoNameDataTable(ar.KeyOfEn);
									dtNoName.Rows.add("all", "全部");

									for (Entity en : ens)
									{
										dtNoName.Rows.add(en.GetValStringByKey(ar.HisAttr.UIRefKeyValue), en.GetValStringByKey(ar.HisAttr.UIRefKeyText));
									}

									ds.Tables.add(dtNoName);

									row.set("ValueField", "No");
									row.set("TextField", "Name");
								}
							}
							break;

						case FieldTypeS.Enum:
							dtNoName = GetNoNameDataTable(ar.KeyOfEn);
							dtNoName.Rows.add("all", "全部");

							SysEnums enums = new SysEnums(ar.UIBindKey);

							for (SysEnum en : enums)
							{
								dtNoName.Rows.add(en.IntKey.toString(), en.Lab);
							}

							ds.Tables.add(dtNoName);

							row.set("ValueField", "No");
							row.set("TextField", "Name");
							break;
						default:
							break;
					}
					break;

				default:
					break;
			}

			dt.Rows.add(row);
		}

		ds.Tables.add(dt);
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

			///#endregion

		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	public final String FlowGropu_Done()
	{

		if (!this.getGroupType().equals("My") && this.getGroupType().equals("MyJoin") && this.getGroupType().equals("MyDept") && this.getGroupType().equals("Adminer"))
		{
			return "info@<img src='../Img/Pub/warning.gif' /><b><font color=red>" + this.getGroupType() + "标记错误.</font></b>";
		}
		DataSet ds = new DataSet();
		ds = FlowGroupDoneSet();
		if (ds == null)
		{
			return "info@<img src='../Img/Pub/warning.gif' /><b><font color=red> 您没有选择显示的内容</font></b>";
		}

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 
	 
	 
	 @return 
	*/
	public final DataSet FlowGroupDoneSet()
	{
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getGroupType();
		DataSet ds = new DataSet();
		MapData md = new MapData(rptNo);
		MapAttrs attrs = new MapAttrs(rptNo);
		GEEntitys ges = new GEEntitys(rptNo);
		GEEntity en = new GEEntity(rptNo);
		Map map = en.EnMapInTime;



		UserRegedit groupUr = new UserRegedit(WebUser.getNo(), rptNo + "_GroupAttrs");
		//分组的参数
		String groupVals = groupUr.Vals;
		//查询条件
		//分组
		String groupKey = "";
		Attrs AttrsOfNum = new Attrs(); //列
		String Condition = ""; //处理特殊字段的条件问题。

		//根据注册表信息获取里面的分组信息
		String StateNumKey = groupVals.substring(groupVals.indexOf("@StateNumKey") + 1);
		String[] statNumKeys = StateNumKey.split("[@]", -1);
		for (String ct : statNumKeys)
		{
			if (ct.split("[=]", -1).length != 2)
			{
				continue;
			}
			String[] paras = ct.split("[=]", -1);

			//判断paras[0]的类型
			int dataType = 2;
			if (paras[0].equals("Group_Number"))
			{
				AttrsOfNum.Add(new Attr("Group_Number", "Group_Number", 1, DataType.AppInt, false, "数量"));
			}
			else
			{
				Attr attr = GetAttrByKey(attrs, paras[0]);
				AttrsOfNum.Add(attr);
				dataType = attr.MyDataType;
			}

			if (paras[0].equals("Group_Number"))
			{
				groupKey += " count(*) \"" + paras[0] + "\",";
			}
			else
			{
				switch (paras[1])
				{
					case "SUM":
						if (dataType == 2)
						{
							groupKey += " SUM(" + paras[0] + ") \"" + paras[0] + "\",";
						}
						else
						{
							groupKey += " round ( SUM(" + paras[0] + "), 4) \"" + paras[0] + "\",";
						}
						break;
					case "AVG":
						groupKey += " round (AVG(" + paras[0] + "), 4)  \"" + paras[0] + "\",";
						break;
					case "AMOUNT":
						if (dataType == 2)
						{
							groupKey += " SUM(" + paras[0] + ") \"" + paras[0] + "\",";
						}
						else
						{
							groupKey += " round ( SUM(" + paras[0] + "), 4) \"" + paras[0] + "\",";
						}
						break;
					default:
						throw new RuntimeException("没有判断的情况.");
				}

			}

		}
		boolean isHaveLJ = false; // 是否有累计字段。
		if (StateNumKey.indexOf("AMOUNT@") != -1)
		{
			isHaveLJ = true;
		}

		if (groupKey.equals(""))
		{
			return null;
		}

		/* 如果包含累计数据，那它一定需要一个月份字段。业务逻辑错误。*/
		groupKey = groupKey.substring(0, groupKey.length() - 1);
		BP.DA.Paras ps = new Paras();
		// 生成 sql.
		String selectSQL = "SELECT ";
		String groupBy = " GROUP BY ";
		Attrs AttrsOfGroup = new Attrs();

		String SelectedGroupKey = groupVals.substring(0, groupVals.indexOf("@StateNumKey")); // 为保存操作状态的需要。
		if (!DataType.IsNullOrEmpty(SelectedGroupKey))
		{
			boolean isSelected = false;
			String[] SelectedGroupKeys = SelectedGroupKey.split("[@]", -1);
			for (String key : SelectedGroupKeys)
			{
				if (key.contains("=") == true)
				{
					continue;
				}
				selectSQL += key + " \"" + key + "\",";
				groupBy += key + ",";
				// 加入组里面。
				AttrsOfGroup.Add(GetAttrByKey(attrs, key), false, false);

			}
		}

		String groupList = this.GetRequestVal("GroupList");
		if (!DataType.IsNullOrEmpty(SelectedGroupKey))
		{
			/* 如果是年月 分组， 并且如果内部有 累计属性，就强制选择。*/
			if (groupList.indexOf("FK_NY") != -1 && isHaveLJ)
			{
				selectSQL += "FK_NY,";
				groupBy += "FK_NY,";
				SelectedGroupKey += "@FK_NY";
				// 加入组里面。
				AttrsOfGroup.Add(GetAttrByKey(attrs, "FK_NY"), false, false);
			}
		}

		groupBy = groupBy.substring(0, groupBy.length() - 1);

		if (groupBy.equals(" GROUP BY"))
		{
			return null;
		}



		String orderByReq = this.GetRequestVal("OrderBy");

		String orderby = "";

		if (orderByReq != null && (selectSQL.contains(orderByReq) || groupKey.contains(orderByReq)))
		{
			orderby = " ORDER BY " + orderByReq;
			String orderWay = this.GetRequestVal("OrderWay");
			if (!DataType.IsNullOrEmpty(orderWay) && !orderWay.equals("Up"))
			{
				orderby += " DESC ";
			}
		}

		//查询语句
		QueryObject qo = new QueryObject(ges);

		switch (this.getGroupType())
		{
			case "My": //我发起的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());
				break;
			case "MyDept": //我部门发起的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FK_Dept, WebUser.getFK_Dept());
				break;
			case "MyJoin": //我参与的.
				qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");
				break;
			case "Adminer":
				break;
			default:
				return null;
		}

		//查询注册信息表
		UserRegedit ur = new UserRegedit();
		ur.setMyPK( WebUser.getNo() + rptNo + "_SearchAttrs";
		ur.RetrieveFromDBSources();
		qo = InitQueryObject(qo, md, ges.GetNewEntity.getEnMap().getAttrs(), attrs, ur);
		qo.AddWhere(" AND  WFState > 1 "); //排除空白，草稿数据.

		DataTable dt2 = qo.DoGroupQueryToTable(selectSQL + groupKey, groupBy, orderby);

		DataTable dt1 = dt2.Clone();

		dt1.Columns.Add("IDX", Integer.class);


			///#region 对他进行分页面

		int myIdx = 0;
		for (DataRow dr : dt2.Rows)
		{
			myIdx++;
			DataRow mydr = dt1.NewRow();
			mydr.set("IDX", myIdx);
			for (DataColumn dc : dt2.Columns)
			{
				mydr.set(dc.ColumnName, dr.get(dc.ColumnName));
			}
			dt1.Rows.add(mydr);
		}

			///#endregion


			///#region 处理 Int 类型的分组列。
		DataTable dt = dt1.Clone();
		dt.TableName = "GroupSearch";
		dt.Rows.Clear();
		for (Attr attr : AttrsOfGroup)
		{
			dt.Columns[attr.Key].DataType = String.class;
		}
		for (DataRow dr : dt1.Rows)
		{
			dt.ImportRow(dr);
		}

			///#endregion

		// 处理这个物理表 , 如果有累计字段, 就扩展它的列。
		if (isHaveLJ)
		{
			// 首先扩充列.
			for (Attr attr : AttrsOfNum)
			{
				if (StateNumKey.indexOf(attr.Key + "=AMOUNT") == -1)
				{
					continue;
				}

				switch (attr.MyDataType)
				{
					case DataType.AppInt:
						dt.Columns.Add(attr.Key + "Amount", Integer.class);
						break;
					default:
						dt.Columns.Add(attr.Key + "Amount", BigDecimal.class);
						break;
				}
			}

			String sql = "";
			String whereOFLJ = "";
			AtPara ap = new AtPara(ur.Vals);
			/** #region 获得查询数据.
			*/
			for (String str : ap.getHisHT().keySet())
			{
				Object val = ap.GetValStrByKey(str);
				if (val.equals("all"))
				{
					continue;
				}
				if (!str.equals("FK_NY"))
				{
					whereOFLJ += " " + str + " =" + SystemConfig.getAppCenterDBVarStr() + str + "   AND ";
				}

			}

			// 添加累计汇总数据.
			for (DataRow dr : dt.Rows)
			{
				for (Attr attr : AttrsOfNum)
				{
					if (StateNumKey.indexOf(attr.Key + "=AMOUNT") == -1)
					{
						continue;
					}

					//形成查询sql.
					if (whereOFLJ.length() > 0)
					{
						sql = "SELECT SUM(" + attr.Key + ") FROM " + ges.GetNewEntity.EnMap.PhysicsTable + whereOFLJ + " AND ";
					}
					else
					{
						sql = "SELECT SUM(" + attr.Key + ") FROM " + ges.GetNewEntity.EnMap.PhysicsTable + " WHERE ";
					}

					for (Attr attr1 : AttrsOfGroup)
					{
						switch (attr1.getKey())
						{
							case "FK_NY":
								sql += " FK_NY <= '" + dr.get("FK_NY") + "' AND FK_ND='" + dr.get("FK_NY").toString().substring(0, 4) + "' AND ";
								break;
							case "FK_Dept":
								sql += attr1.Key + "='" + dr.get(attr1.getKey()) + "' AND ";
								break;
							case "FK_SJ":
							case "FK_XJ":
								sql += attr1.Key + " LIKE '" + dr.get(attr1.getKey()) + "%' AND ";
								break;
							default:
								sql += attr1.Key + "='" + dr.get(attr1.getKey()) + "' AND ";
								break;
						}
					}

					sql = sql.substring(0, sql.length() - "AND ".length());
					if (attr.MyDataType == DataType.AppInt)
					{
						dr.set(attr.Key + "Amount", DBAccess.RunSQLReturnValInt(sql, 0));
					}
					else
					{
						dr.set(attr.Key + "Amount", DBAccess.RunSQLReturnValDecimal(sql, 0, 2));
					}
				}
			}
		}

		// 为表扩充外键
		for (Attr attr : AttrsOfGroup)
		{
			dt.Columns.Add(attr.Key + "T", String.class);
		}
		for (Attr attr : AttrsOfGroup)
		{
			if (attr.UIBindKey.indexOf(".") == -1)
			{
				/* 说明它是枚举类型 */
				SysEnums ses = new SysEnums(attr.UIBindKey);
				for (DataRow dr : dt.Rows)
				{
					int val = 0;
					try
					{
						val = Integer.parseInt(dr.get(attr.getKey()).toString());
					}
					catch (java.lang.Exception e)
					{
						dr.set(attr.Key + "T", " ");
						continue;
					}

					for (SysEnum se : ses)
					{
						if (se.IntKey == val)
						{
							dr.set(attr.Key + "T", se.Lab);
						}
					}
				}
				continue;
			}
			for (DataRow dr : dt.Rows)
			{
				Entity myen = attr.HisFKEn;
				String val = dr.get(attr.getKey()).toString();
				myen.SetValByKey(attr.UIRefKeyValue, val);
				try
				{
					myen.Retrieve();
					dr.set(attr.Key + "T", myen.GetValStrByKey(attr.UIRefKeyText));
				}
				catch (java.lang.Exception e2)
				{
					if (val == null || val.length() <= 1)
					{
						dr.set(attr.Key + "T", val);
					}
					else if (val.substring(0, 2).equals("63"))
					{
						try
						{
							BP.Port.Dept Dept = new BP.Port.Dept(val);
							dr.set(attr.Key + "T", Dept.Name);
						}
						catch (java.lang.Exception e3)
						{
							dr.set(attr.Key + "T", val);
						}
					}
					else
					{
						dr.set(attr.Key + "T", val);
					}
				}
			}
		}
		ds.Tables.add(dt);
		ds.Tables.add(AttrsOfNum.ToMapAttrs.ToDataTableField("AttrsOfNum"));
		ds.Tables.add(AttrsOfGroup.ToMapAttrs.ToDataTableField("AttrsOfGroup"));


		return ds;
	}

	/** 
	 执行导出
	 
	 @return 
	*/
	public final String FlowGroup_Exp()
	{
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getGroupType();
		String desc = "";

		if (this.getGroupType().equals("My"))
		{
			desc = "我发起的流程";
		}
		else if (this.getGroupType().equals("MyJoin"))
		{
			desc = "我审批的流程";
		}
		else if (this.getGroupType().equals("MyDept"))
		{
			desc = "本部门发起的流程";
		}
		else if (this.getGroupType().equals("Adminer"))
		{
			desc = "高级查询";
		}
		else
		{
			return "info@<img src='../Img/Pub/warning.gif' /><b><font color=red>" + this.getGroupType() + "标记错误.</font></b>";
		}

		DataSet ds = new DataSet();
		ds = FlowGroupDoneSet();
		if (ds == null)
		{
			return "info@<img src='../Img/Pub/warning.gif' /><b><font color=red> 您没有选择显示的内容</font></b>";
		}

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.getNo(), rptNo + "_GroupAttrs");



		String filePath = ExportGroupExcel(ds, desc, ur.Vals);


		return filePath;
	}

	/** 
	 初始化数据
	 
	 @return 
	*/
	public final String FlowContrastDtl_Init()
	{
		if (DataType.IsNullOrEmpty(this.getFK_Flow()))
		{
			return "err@参数FK_Flow不能为空";
		}

		String pageSize = GetRequestVal("pageSize");
		String fcid = "";
		DataSet ds = new DataSet();
		HashMap<String, String> vals = null;
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getSearchType();

		GEEntitys ges = new GEEntitys(rptNo);

		//属性集合.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo, MapAttrAttr.Idx);
		DataRow row = null;
		DataTable dt = new DataTable("Sys_MapAttr");
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);
		for (MapAttr attr : attrs)
		{
			row = dt.NewRow();
			row.set("No", attr.KeyOfEn);
			row.set("Name", attr.Name);
			row.set("Width", attr.UIWidthInt);
			row.set("UIContralType", attr.UIContralType);

			if (attr.HisAttr.IsFKorEnum)
			{
				row.set("No", attr.KeyOfEn + "Text");
			}

			dt.Rows.add(row);
		}

		ds.Tables.add(dt);

		//查询结果
		QueryObject qo = new QueryObject(ges);


		var strs = HttpContextHelper.RequestParamKeys; // this.context.Request.Form.ToString().Split('&');
		for (String str : strs)
		{
			if (str.indexOf("FK_Flow") != -1 || str.indexOf("SearchType") != -1)
			{
				continue;
			}

			String[] mykey = str.split("[=]", -1);
			String key = mykey[0];

			if (key.equals("OID") || key.equals("MyPK"))
			{
				continue;
			}

			if (key.equals("FK_Dept"))
			{
				this.setFK_Dept(mykey[1]);
				continue;
			}
			boolean isExist = false;
			for (MapAttr attr : attrs)
			{
				if (attr.KeyOfEn.toUpperCase().equals(key.toUpperCase()))
				{
					isExist = true;
					break;
				}
			}

			if (isExist == false)
			{
				continue;
			}

			if (mykey[1].equals("mvals"))
			{
				//如果用户多项选择了，就要找到它的选择项目.

				UserRegedit sUr = new UserRegedit();
				sUr.setMyPK( WebUser.getNo() + rptNo + "_SearchAttrs";
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.MVals;
				AtPara ap = new AtPara(cfgVal);
				String instr = ap.GetValStrByKey(key);
				String val = "";
				if (instr == null || instr.equals(""))
				{
					if (key.equals("FK_Dept") || key.equals("FK_Unit"))
					{
						if (key.equals("FK_Dept"))
						{
							val = WebUser.getFK_Dept();
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
					qo.AddWhereIn(mykey[0], instr);
				}
			}
			else
			{
				qo.AddWhere(mykey[0], mykey[1]);
			}
			qo.addAnd();
		}

		if (this.getFK_Dept() != null && (this.GetRequestVal("FK_Emp") == null || this.GetRequestVal("FK_Emp").equals("all")))
		{
			if (this.getFK_Dept().length() == 2)
			{
				qo.AddWhere("FK_Dept", " = ", "all");
				qo.addAnd();
			}
			else
			{
				if (this.getFK_Dept().length() == 8)
				{
					qo.AddWhere("FK_Dept", " = ", this.getFK_Dept());
				}
				else
				{
					qo.AddWhere("FK_Dept", " like ", this.getFK_Dept() + "%");
				}

				qo.addAnd();
			}
		}

		qo.AddHD();

		dt = qo.DoQueryToTable();
		dt.TableName = "Group_Dtls";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}


	/** 
	 执行导出
	 
	 @return 
	*/
	public final String FlowGroupDtl_Exp()
	{
		if (DataType.IsNullOrEmpty(this.getFK_Flow()))
		{
			return "err@参数FK_Flow不能为空";
		}

		String pageSize = GetRequestVal("pageSize");
		String fcid = "";
		HashMap<String, String> vals = null;
		String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + this.getSearchType();

		GEEntitys ges = new GEEntitys(rptNo);

		//属性集合.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo, MapAttrAttr.Idx);


		//查询结果
		QueryObject qo = new QueryObject(ges);

		//string[] strs = this.context.Request.Form.ToString().Split('&');

		var strs = HttpContextHelper.RequestParamKeys;
		for (String str : strs)
		{
			if (str.indexOf("FK_Flow") != -1 || str.indexOf("SearchType") != -1)
			{
				continue;
			}

			String[] mykey = str.split("[=]", -1);
			String key = mykey[0];

			if (key.equals("OID") || key.equals("MyPK"))
			{
				continue;
			}

			if (key.equals("FK_Dept"))
			{
				this.setFK_Dept(mykey[1]);
				continue;
			}
			boolean isExist = false;
			for (MapAttr attr : attrs)
			{
				if (attr.KeyOfEn.toUpperCase().equals(key.toUpperCase()))
				{
					isExist = true;
					break;
				}
			}

			if (isExist == false)
			{
				continue;
			}

			if (mykey[1].equals("mvals"))
			{
				//如果用户多项选择了，就要找到它的选择项目.

				UserRegedit sUr = new UserRegedit();
				sUr.setMyPK( WebUser.getNo() + rptNo + "_SearchAttrs";
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.MVals;
				AtPara ap = new AtPara(cfgVal);
				String instr = ap.GetValStrByKey(key);
				String val = "";
				if (instr == null || instr.equals(""))
				{
					if (key.equals("FK_Dept") || key.equals("FK_Unit"))
					{
						if (key.equals("FK_Dept"))
						{
							val = WebUser.getFK_Dept();
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
					qo.AddWhereIn(mykey[0], instr);
				}
			}
			else
			{
				qo.AddWhere(mykey[0], mykey[1]);
			}
			qo.addAnd();
		}

		if (this.getFK_Dept() != null && (this.GetRequestVal("FK_Emp") == null || this.GetRequestVal("FK_Emp").equals("all")))
		{
			if (this.getFK_Dept().length() == 2)
			{
				qo.AddWhere("FK_Dept", " = ", "all");
				qo.addAnd();
			}
			else
			{
				if (this.getFK_Dept().length() == 8)
				{
					qo.AddWhere("FK_Dept", " = ", this.getFK_Dept());
				}
				else
				{
					qo.AddWhere("FK_Dept", " like ", this.getFK_Dept() + "%");
				}

				qo.addAnd();
			}
		}

		qo.AddHD();

		DataTable dt = qo.DoQueryToTable();
		Attrs newAttrs = new Attrs();
		for (MapAttr attr : attrs)
		{
			if (attr.KeyOfEn.toUpperCase().equals("OID"))
			{
				continue;
			}

			newAttrs.Add(attr.HisAttr);
		}

		String filePath = ExportDGToExcel(dt, ges.GetNewEntity, rptNo, newAttrs);


		return filePath;
	}

	/** 
	 通过一个key 得到它的属性值。
	 
	 @param key key
	 @return attr
	*/
	public final Attr GetAttrByKey(MapAttrs mapAttrs, String key)
	{
		for (MapAttr attr : mapAttrs)
		{
			if (attr.KeyOfEn.toUpperCase().equals(key.toUpperCase()))
			{
				return attr.HisAttr;
			}
		}
		return null;

	}

	/** 
	 初始化QueryObject
	 
	 @param qo
	 @return 
	*/
	public final QueryObject InitQueryObject(QueryObject qo, MapData md, Attrs attrs, MapAttrs rptAttrs, UserRegedit ur)
	{
		HashMap<String, String> kvs = null;
		ArrayList<String> keys = new ArrayList<String>();
		String cfg = "_SearchAttrs";
		String searchKey = "";
		String val = null;

		kvs = ur.GetVals();

		if (!this.getSearchType().equals("Adminer"))
		{
			qo.addAnd();
		}


			///#region 关键字查询
		if (md.RptIsSearchKey)
		{
			searchKey = ur.SearchKey;
		}

		if (DataType.IsNullOrEmpty(searchKey))
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
				switch (attr.MyFieldType)
				{
					case FieldType.Enum:
					case FieldType.FK:
					case FieldType.PKFK:
						continue;
					default:
						break;
				}

				if (attr.MyDataType != DataType.AppString)
				{
					continue;
				}

				if (attr.MyFieldType == FieldType.RefText)
				{
					continue;
				}

				if (attr.Key.equals("FK_Dept"))
				{
					continue;
				}

				i++;

				if (i == 1)
				{
					qo.addLeftBracket();
					if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals("?"))
					{
						qo.AddWhere(attr.Key, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
					}
					else
					{
						qo.AddWhere(attr.Key, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
					}
					continue;
				}

				qo.addOr();

				if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals("?"))
				{
					qo.AddWhere(attr.Key, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
				}
				else
				{
					qo.AddWhere(attr.Key, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
				}
			}

			qo.MyParas.Add("SKey", searchKey);
			qo.addRightBracket();
		}

			///#endregion


			///#region Url传参条件
		for (Attr attr : attrs)
		{
			if (DataType.IsNullOrEmpty(HttpContextHelper.RequestParams(attr.getKey())))
			{
				continue;
			}

			qo.addAnd();
			qo.addLeftBracket();

			val = HttpContextHelper.RequestParams(attr.getKey());

			switch (attr.MyDataType)
			{
				case DataType.AppBoolean:
					qo.AddWhere(attr.Key, (boolean)Integer.parseInt(val));
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
				case DataType.AppString:
					qo.AddWhere(attr.Key, val);
					break;
				case DataType.AppDouble:
				case DataType.AppFloat:
				case DataType.AppMoney:
					qo.AddWhere(attr.Key, Double.parseDouble(val));
					break;
				case DataType.AppInt:
					qo.AddWhere(attr.Key, Integer.parseInt(val));
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

			///#endregion


			///#region 过滤条件
		for (MapAttr attr1 : rptAttrs)
		{
			Attr attr = attr1.HisAttr;
			//此处做判断，如果在URL中已经传了参数，则不算SearchAttrs中的设置
			if (keys.contains(attr.getKey()))
			{
				continue;
			}

			if (attr.MyFieldType == FieldType.RefText)
			{
				continue;
			}

			String selectVal = "";
			String cid = "";

			switch (attr.UIContralType)
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
				case UIContralType.DDL:
					cid = "DDL_" + attr.Key;

					if (kvs.containsKey(cid) == false || DataType.IsNullOrEmpty(kvs.get(cid)))
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
						/* 如果是多选值 */
						AtPara ap = new AtPara(ur.MVals);
						String instr = ap.GetValStrByKey(attr.getKey());

						if (DataType.IsNullOrEmpty(instr))
						{
							if (attr.Key.equals("FK_Dept") || attr.Key.equals("FK_Unit"))
							{
								if (attr.Key.equals("FK_Dept"))
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
							qo.AddWhereIn(attr.Key, "(" + instr + ")");
							qo.addRightBracket();
							continue;
						}
					}

					qo.addAnd();
					qo.addLeftBracket();

					if (attr.UIBindKey.equals("BP.Port.Depts") || attr.UIBindKey.equals("BP.Port.Units")) //判断特殊情况。
					{
						qo.AddWhere(attr.Key, " LIKE ", selectVal + "%");
					}
					else
					{
						qo.AddWhere(attr.Key, selectVal);
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

			///#endregion


			///#region 日期处理
		if (md.RptDTSearchWay != DTSearchWay.None)
		{
			String dtKey = md.RptDTSearchKey;
			String dtFrom = ur.GetValStringByKey(UserRegeditAttr.DTFrom).trim();
			String dtTo = ur.GetValStringByKey(UserRegeditAttr.DTTo).trim();

			if (DataType.IsNullOrEmpty(dtFrom) == true)
			{
				if (md.RptDTSearchWay == DTSearchWay.ByDate)
				{
					dtFrom = "1900-01-01";
				}
				else
				{
					dtFrom = "1900-01-01 00:00";
				}
			}

			if (DataType.IsNullOrEmpty(dtTo) == true)
			{
				if (md.RptDTSearchWay == DTSearchWay.ByDate)
				{
					dtTo = "2999-01-01";
				}
				else
				{
					dtTo = "2999-12-31 23:59";
				}
			}

			if (md.RptDTSearchWay == DTSearchWay.ByDate)
			{
				qo.addAnd();
				qo.addLeftBracket();
				qo.SQL = dtKey + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = dtKey + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}

			if (md.RptDTSearchWay == DTSearchWay.ByDateTime)
			{
				qo.addAnd();
				qo.addLeftBracket();
				qo.SQL = dtKey + " >= '" + dtFrom + " 00:00'";
				qo.addAnd();
				qo.SQL = dtKey + " <= '" + dtTo + " 23:59'";
				qo.addRightBracket();
			}
		}

			///#endregion

		return qo;
	}

	private DataTable GetNoNameDataTable(String tableName)
	{
		DataTable dt = new DataTable(tableName);
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);

		return dt;
	}

		///#endregion MyStartFlow.htm 我发起的流程

	public final String MyDeptFlow_Init()
	{
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "RptMyDept";

		DataSet ds = new DataSet();

		//字段描述.
		MapAttrs attrs = new MapAttrs(fk_mapdata);
		DataTable dtAttrs = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.add(dtAttrs);

		//数据.
		GEEntitys ges = new GEEntitys(fk_mapdata);

		//设置查询条件.
		QueryObject qo = new QueryObject(ges);
		qo.AddWhere(BP.WF.Data.GERptAttr.FlowStarter, WebUser.getNo());

		//查询.
		// qo.DoQuery(BP.WF.Data.GERptAttr.OID, 15, this.PageIdx);

		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			DataTable dt = qo.DoQueryToTable();
			dt.TableName = "dt";
			ds.Tables.add(dt);
		}
		else
		{
			qo.DoQuery();
			ds.Tables.add(ges.ToDataTableField("dt"));
		}

		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	public final String MyJoinFlow_Init()
	{
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "RptMyJoin";

		DataSet ds = new DataSet();

		//字段描述.
		MapAttrs attrs = new MapAttrs(fk_mapdata);
		DataTable dtAttrs = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.add(dtAttrs);

		//数据.
		GEEntitys ges = new GEEntitys(fk_mapdata);

		//设置查询条件.
		QueryObject qo = new QueryObject(ges);
		qo.AddWhere(BP.WF.Data.GERptAttr.FlowEmps, " LIKE ", "%" + WebUser.getNo() + "%");

		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			DataTable dt = qo.DoQueryToTable();
			dt.TableName = "dt";
			ds.Tables.add(dt);
		}
		else
		{
			qo.DoQuery();
			ds.Tables.add(ges.ToDataTableField("dt"));
		}
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
}