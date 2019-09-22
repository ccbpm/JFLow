package BP.Frm;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.WF.HttpHandler.*;
import BP.NetPlatformImpl.*;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 页面功能实体
*/
public class WF_CCBill extends DirectoryPageBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法.
	/** 
	 构造函数
	*/
	public WF_CCBill()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法.

	/** 
	 发起列表.
	 
	 @return 
	*/
	public final String Start_Init()
	{
		//获得发起列表. 
		DataSet ds = BP.Frm.Dev2Interface.DB_StartBills(WebUser.getNo());

		//返回组合
		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	/** 
	 草稿列表
	 
	 @return 
	*/
	public final String Draft_Init()
	{
		//草稿列表.
		DataTable dt = BP.Frm.Dev2Interface.DB_Draft(this.getFrmID(), WebUser.getNo());

		//返回组合
		return BP.Tools.Json.DataTableToJson(dt, false);
	}
	/** 
	 单据初始化
	 
	 @return 
	*/
	public final String MyBill_Init()
	{
		//获得发起列表. 
		DataSet ds = BP.Frm.Dev2Interface.DB_StartBills(WebUser.getNo());

		//返回组合
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	public final String DoMethod_ExeSQL()
	{
		MethodFunc func = new MethodFunc(this.getMyPK());
		String doc = func.getMethodDoc_SQL();

		GEEntity en = new GEEntity(func.getFrmID(), this.getWorkID());
		doc = BP.WF.Glo.DealExp(doc, en, null); //替换里面的内容.

		try
		{
			DBAccess.RunSQLs(doc);
			if (func.getMsgSuccess().equals(""))
			{
				func.setMsgSuccess("执行成功.");
			}

			return func.getMsgSuccess();
		}
		catch (RuntimeException ex)
		{
			if (func.getMsgErr().equals(""))
			{
				func.setMsgErr("执行失败(DoMethod_ExeSQL).");
			}
			return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
		}
	}
	/** 
	 执行SQL
	 
	 @return 
	*/
	public final String DoMethodPara_ExeSQL()
	{
		MethodFunc func = new MethodFunc(this.getMyPK());
		String doc = func.getMethodDoc_SQL();

		GEEntity en = new GEEntity(func.getFrmID(), this.getWorkID());
		doc = BP.WF.Glo.DealExp(doc, en, null); //替换里面的内容.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 替换参数变量.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, this.getMyPK());
		for (MapAttr item : attrs)
		{
			if (item.UIContralType == UIContralType.TB)
			{
				doc = doc.replace("@" + item.KeyOfEn, this.GetRequestVal("TB_" + item.KeyOfEn));
				continue;
			}

			if (item.UIContralType == UIContralType.DDL)
			{
				doc = doc.replace("@" + item.KeyOfEn, this.GetRequestVal("DDL_" + item.KeyOfEn));
				continue;
			}


			if (item.UIContralType == UIContralType.CheckBok)
			{
				doc = doc.replace("@" + item.KeyOfEn, this.GetRequestVal("CB_" + item.KeyOfEn));
				continue;
			}

			if (item.UIContralType == UIContralType.RadioBtn)
			{
				doc = doc.replace("@" + item.KeyOfEn, this.GetRequestVal("RB_" + item.KeyOfEn));
				continue;
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 替换参数变量.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 开始执行SQLs.
		try
		{
			DBAccess.RunSQLs(doc);
			if (func.getMsgSuccess().equals(""))
			{
				func.setMsgSuccess("执行成功.");
			}

			return func.getMsgSuccess();
		}
		catch (RuntimeException ex)
		{
			if (func.getMsgErr().equals(""))
			{
				func.setMsgErr("执行失败.");
			}

			return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 开始执行SQLs.

		return "err@" + func.getMethodDocTypeOfFunc() + ",执行的类型没有解析.";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 单据处理.
	/** 
	 创建空白的WorkID.
	 
	 @return 
	*/
	public final String MyBill_CreateBlankBillID()
	{
		return String.valueOf(BP.Frm.Dev2Interface.CreateBlankBillID(this.getFrmID(), WebUser.getNo(), null));
	}
	/** 
	 创建空白的DictID.
	 
	 @return 
	*/
	public final String MyDict_CreateBlankDictID()
	{
		return String.valueOf(BP.Frm.Dev2Interface.CreateBlankDictID(this.getFrmID(), WebUser.getNo(), null));
	}
	/** 
	 执行保存
	 
	 @return 
	*/
	public final String MyBill_SaveIt()
	{
		//执行保存.
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
		Object tempVar = BP.Sys.PubClass.CopyFromRequest(rpt);
		rpt = tempVar instanceof GEEntity ? (GEEntity)tempVar : null;

		Hashtable ht = GetMainTableHT();
		for (String item : ht.keySet())
		{
			rpt.SetValByKey(item, ht.get(item));
		}

		rpt.OID = this.getWorkID();
		rpt.SetValByKey("BillState", BillState.Editing.getValue());
		rpt.Update();

		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}
	/** 
	 执行保存
	 
	 @return 
	*/
	public final String MyDict_SaveIt()
	{
		//  throw new Exception("dddssds");
		//执行保存.
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
		Object tempVar = BP.Sys.PubClass.CopyFromRequest(rpt);
		rpt = tempVar instanceof GEEntity ? (GEEntity)tempVar : null;

		Hashtable ht = GetMainTableHT();
		for (String item : ht.keySet())
		{
			rpt.SetValByKey(item, ht.get(item));
		}

		rpt.OID = this.getWorkID();
		rpt.SetValByKey("BillState", BillState.Editing.getValue());
		rpt.Update();

		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}

	public final String GetFrmEntitys()
	{
		GEEntitys rpts = new GEEntitys(this.getFrmID());
		QueryObject qo = new QueryObject(rpts);
		qo.AddWhere("BillState", " != ", 0);
		qo.DoQuery();
		return BP.Tools.Json.ToJson(rpts.ToDataTableField());
	}
	private Hashtable GetMainTableHT()
	{
		Hashtable htMain = new Hashtable();
		for (String key : HttpContextHelper.RequestParamKeys)
		{
			if (key == null)
			{
				continue;
			}

			if (key.contains("TB_"))
			{

				String val = HttpContextHelper.RequestParams(key);
				if (htMain.containsKey(key.replace("TB_", "")) == false)
				{
					val = HttpUtility.UrlDecode(val, Encoding.UTF8);
					htMain.put(key.replace("TB_", ""), val);
				}
				continue;
			}

			if (key.contains("DDL_"))
			{
				htMain.put(key.replace("DDL_", ""), HttpContextHelper.RequestParams(key));
				continue;
			}

			if (key.contains("CB_"))
			{
				htMain.put(key.replace("CB_", ""), HttpContextHelper.RequestParams(key));
				continue;
			}

			if (key.contains("RB_"))
			{
				htMain.put(key.replace("RB_", ""), HttpContextHelper.RequestParams(key));
				continue;
			}
		}
		return htMain;
	}

	public final String MyBill_SaveAsDraft()
	{
		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}
	//删除单据
	public final String MyBill_Delete()
	{
		return BP.Frm.Dev2Interface.MyBill_Delete(this.getFrmID(), this.getWorkID());
	}

	public final String MyBill_Deletes()
	{
		return BP.Frm.Dev2Interface.MyBill_DeleteDicts(this.getFrmID(), this.GetRequestVal("WorkIDs"));
	}

	//删除实体
	public final String MyDict_Delete()
	{
		return BP.Frm.Dev2Interface.MyDict_Delete(this.getFrmID(), this.getWorkID());
	}

	public final String MyEntityTree_Delete()
	{
		return BP.Frm.Dev2Interface.MyEntityTree_Delete(this.getFrmID(), this.GetRequestVal("BillNo"));
	}
	/** 
	 复制单据数据
	 
	 @return 
	*/
	public final String MyBill_Copy()
	{
		return BP.Frm.Dev2Interface.MyBill_Copy(this.getFrmID(), this.getWorkID());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 单据处理.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 获取查询条件
	public final String Search_ToolBar()
	{
		DataSet ds = new DataSet();

		DataTable dt = new DataTable();

		//根据FrmID获取Mapdata
		MapData md = new MapData(this.getFrmID());
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		//获取字段属性
		MapAttrs attrs = new MapAttrs(this.getFrmID());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region //增加枚举/外键字段信息

		dt.Columns.Add("Field", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.TableName = "Attrs";

		String[] ctrls = md.RptSearchKeys.split("[*]", -1);
		DataTable dtNoName = null;

		MapAttr mapattr;
		DataRow dr = null;
		for (String ctrl : ctrls)
		{
			//增加判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示
			if (DataType.IsNullOrEmpty(ctrl) || !DataType.IsNullOrEmpty(HttpContextHelper.RequestParams(ctrl)))
			{
				continue;
			}

			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ctrl);
			mapattr = tempVar instanceof MapAttr ? (MapAttr)tempVar : null;
			dr = dt.NewRow();
			dr.set("Field", mapattr.KeyOfEn);
			dr.set("Name", mapattr.Name);
			dr.set("Width", mapattr.UIWidth);
			dt.Rows.add(dr);

			Attr attr = mapattr.HisAttr;
			if (mapattr == null)
			{
				continue;
			}

			if (attr.IsEnum == true)
			{
				SysEnums ses = new SysEnums(mapattr.UIBindKey);
				DataTable dtEnum = ses.ToDataTableField();
				dtEnum.TableName = mapattr.KeyOfEn;
				ds.Tables.add(dtEnum);
				continue;
			}
			if (attr.IsFK == true)
			{
				Entities ensFK = attr.HisFKEns;
				ensFK.RetrieveAll();

				DataTable dtEn = ensFK.ToDataTableField();
				dtEn.TableName = attr.Key;
				ds.Tables.add(dtEn);
			}
			//绑定SQL的外键
			if (attr.UIDDLShowType == BP.Web.Controls.DDLShowType.BindSQL)
			{
				//获取SQl
				String sql = attr.UIBindKey;
				sql = BP.WF.Glo.DealExp(sql, null, null);
				DataTable dtSQl = DBAccess.RunSQLReturnTable(sql);
				for (DataColumn col : dtSQl.Columns)
				{
					String colName = col.ColumnName.toLowerCase();
					switch (colName)
					{
						case "no":
							col.ColumnName = "No";
							break;
						case "name":
							col.ColumnName = "Name";
							break;
						case "parentno":
							col.ColumnName = "ParentNo";
							break;
						default:
							break;
					}
				}
				dtSQl.TableName = attr.Key;
				if (ds.Tables.Contains(attr.Key) == false)
				{
					ds.Tables.add(dtSQl);
				}

			}

		}

		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 查询条件


	public final String Search_Init()
	{
		DataSet ds = new DataSet();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询显示的列
		MapAttrs mapattrs = new MapAttrs();
		mapattrs.Retrieve(MapAttrAttr.FK_MapData, this.getFrmID(), MapAttrAttr.Idx);

		DataRow row = null;
		DataTable dt = new DataTable("Attrs");
		dt.Columns.Add("KeyOfEn", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);
		dt.Columns.Add("LGType", Integer.class);
		dt.Columns.Add("AtPara", String.class);

		//设置标题、单据号位于开始位置


		for (MapAttr attr : mapattrs)
		{
			String searchVisable = attr.atPara.GetValStrByKey("SearchVisable");
			if (searchVisable.equals("0"))
			{
				continue;
			}
			if (attr.UIVisible == false)
			{
				continue;
			}
			row = dt.NewRow();
			row.set("KeyOfEn", attr.KeyOfEn);
			row.set("Name", attr.Name);
			row.set("Width", attr.UIWidthInt);
			row.set("UIContralType", attr.UIContralType);
			row.set("LGType", attr.LGType);
			row.set("AtPara", attr.GetValStringByKey("AtPara"));
			dt.Rows.add(row);
		}
		ds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 查询显示的列

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询语句
		MapData md = new MapData(this.getFrmID());

		//取出来查询条件.
		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK( WebUser.getNo() + "_" + this.getFrmID() + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		GEEntitys rpts = new GEEntitys(this.getFrmID());

		Attrs attrs = rpts.GetNewEntity.EnMap.Attrs;

		QueryObject qo = new QueryObject(rpts);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 关键字字段.
		String keyWord = ur.SearchKey;

		if (md.GetParaBoolen("IsSearchKey") && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
		{
			Attr attrPK = new Attr();
			for (Attr attr : attrs)
			{
				if (attr.IsPK)
				{
					attrPK = attr;
					break;
				}
			}
			int i = 0;
			String enumKey = ","; //求出枚举值外键.
			for (Attr attr : attrs)
			{
				switch (attr.MyFieldType)
				{
					case FieldType.Enum:
						enumKey = "," + attr.Key + "Text,";
						break;
					case FieldType.FK:
						continue;
					default:
						break;
				}

				if (attr.MyDataType != DataType.AppString)
				{
					continue;
				}

				//排除枚举值关联refText.
				if (attr.MyFieldType == FieldType.RefText)
				{
					if (enumKey.contains("," + attr.Key + ",") == true)
					{
						continue;
					}
				}

				if (attr.Key.equals("FK_Dept"))
				{
					continue;
				}

				i++;
				if (i == 1)
				{
					/* 第一次进来。 */
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
			qo.MyParas.Add("SKey", keyWord);
			qo.addRightBracket();
		}
		else
		{
			qo.AddHD();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 关键字段查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 时间段的查询
		if (md.GetParaInt("DTSearchWay") != (int)DTSearchWay.None && DataType.IsNullOrEmpty(ur.DTFrom) == false)
		{
			String dtFrom = ur.DTFrom; // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
			String dtTo = ur.DTTo; // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

			//按日期查询
			if (md.GetParaInt("DTSearchWay") == (int)DTSearchWay.ByDate)
			{
				qo.addAnd();
				qo.addLeftBracket();
				dtTo += " 23:59:59";
				qo.SQL = md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}

			if (md.GetParaInt("DTSearchWay") == (int)DTSearchWay.ByDateTime)
			{
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = LocalDateTime.parse(dtFrom).AddDays(-1).toString("yyyy-MM-dd") + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}

				qo.addAnd();
				qo.addLeftBracket();
				qo.SQL = md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 时间段的查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 外键或者枚举的查询

		//获得关键字.
		AtPara ap = new AtPara(ur.Vals);
		for (String str : ap.getHisHT().keySet())
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}
			qo.addAnd();
			qo.addLeftBracket();


			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
				var typeVal = BP.Sys.Glo.GenerRealType(attrs, str, ap.GetValStrByKey(str));
				qo.AddWhere(str, typeVal);

			}
			else
			{
				qo.AddWhere(str, ap.GetValStrByKey(str));
			}

			qo.addRightBracket();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 外键或者枚举的查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 查询语句

		qo.addAnd();
		qo.AddWhere("BillState", "!=", 0);
		//获得行数.
		ur.SetPara("RecCount", qo.GetCount());
		ur.Save();

		if (DataType.IsNullOrEmpty(ur.OrderBy) == false && DataType.IsNullOrEmpty(ur.OrderWay) == false)
		{
			qo.DoQuery("OID", this.getPageSize(), this.getPageIdx(), ur.OrderBy, ur.OrderWay);
		}
		else
		{
			qo.DoQuery("OID", this.getPageSize(), this.getPageIdx());
		}

		DataTable mydt = rpts.ToDataTableField();
		mydt.TableName = "DT";

		ds.Tables.add(mydt); //把数据加入里面.

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String GenerBill_Init()
	{
		GenerBills bills = new GenerBills();
		bills.Retrieve(GenerBillAttr.Starter, WebUser.getNo());
		return bills.ToJson();
	}
	/** 
	 查询初始化
	 
	 @return 
	*/
	public final String SearchData_Init()
	{
		DataSet ds = new DataSet();
		String sql = "";

		String tSpan = this.GetRequestVal("TSpan");
		if (tSpan.equals(""))
		{
			tSpan = null;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 1、获取时间段枚举/总数.
		SysEnums ses = new SysEnums("TSpan");
		DataTable dtTSpan = ses.ToDataTableField();
		dtTSpan.TableName = "TSpan";
		ds.Tables.add(dtTSpan);

		GenerBill gb = new GenerBill();
		gb.CheckPhysicsTable();

		sql = "SELECT TSpan as No, COUNT(WorkID) as Num FROM Frm_GenerBill WHERE FrmID='" + this.getFrmID() + "'  AND Starter='" + WebUser.getNo() + "' AND BillState >= 1 GROUP BY TSpan";

		DataTable dtTSpanNum = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow drEnum : dtTSpan.Rows)
		{
			String no = drEnum.get("IntKey").toString();
			for (DataRow dr : dtTSpanNum.Rows)
			{
				if (dr.get("No").toString().equals(no))
				{
					drEnum.set("Lab", drEnum.get("Lab").toString() + "(" + dr.get("Num") + ")");
					break;
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 2、处理流程类别列表.
		sql = " SELECT  A.BillState as No, B.Lab as Name, COUNT(WorkID) as Num FROM Frm_GenerBill A, Sys_Enum B ";
		sql += " WHERE A.BillState=B.IntKey AND B.EnumKey='BillState' AND  A.Starter='" + WebUser.getNo() + "' AND BillState >=1";
		if (tSpan.equals("-1") == false)
		{
			sql += "  AND A.TSpan=" + tSpan;
		}

		sql += "  GROUP BY A.BillState, B.Lab  ";

		DataTable dtFlows = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtFlows.Columns[0].ColumnName = "No";
			dtFlows.Columns[1].ColumnName = "Name";
			dtFlows.Columns[2].ColumnName = "Num";
		}
		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 3、处理流程实例列表.
		String sqlWhere = "";
		sqlWhere = "(1 = 1)AND Starter = '" + WebUser.getNo() + "' AND BillState >= 1";
		if (tSpan.equals("-1") == false)
		{
			sqlWhere += "AND (TSpan = '" + tSpan + "') ";
		}

		if (this.getFK_Flow() != null)
		{
			sqlWhere += "AND (FrmID = '" + this.getFrmID() + "')  ";
		}
		else
		{
			// sqlWhere += ")";
		}
		sqlWhere += "ORDER BY RDT DESC";

		String fields = " WorkID,FrmID,FrmName,Title,BillState, Starter, StarterName,Sender,RDT ";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sql = "SELECT " + fields + " FROM (SELECT * FROM Frm_GenerBill WHERE " + sqlWhere + ") WHERE rownum <= 50";
		}
		else if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			sql = "SELECT  TOP 50 " + fields + " FROM Frm_GenerBill WHERE " + sqlWhere;
		}
		else if (SystemConfig.getAppCenterDBType() == DBType.MySQL || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			sql = "SELECT  " + fields + " FROM Frm_GenerBill WHERE " + sqlWhere + " LIMIT 50";
		}

		DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			mydt.Columns[0].ColumnName = "WorkID";
			mydt.Columns[1].ColumnName = "FrmID";
			mydt.Columns[2].ColumnName = "FrmName";
			mydt.Columns[3].ColumnName = "Title";
			mydt.Columns[4].ColumnName = "BillState";
			mydt.Columns[5].ColumnName = "Starter";
			mydt.Columns[6].ColumnName = "StarterName";
			mydt.Columns[7].ColumnName = "Sender";
			mydt.Columns[8].ColumnName = "RDT";
		}

		mydt.TableName = "Frm_Bill";
		if (mydt != null)
		{
			mydt.Columns.Add("TDTime");
			for (DataRow dr : mydt.Rows)
			{
				//   dr["TDTime"] =  GetTraceNewTime(dr["FK_Flow"].ToString(), int.Parse(dr["WorkID"].ToString()), int.Parse(dr["FID"].ToString()));
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		ds.Tables.add(mydt);

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 查询.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 单据导出
	public final String Search_Exp()
	{
		FrmBill frmBill = new FrmBill(this.getFrmID());
		GEEntitys rpts = new GEEntitys(this.getFrmID());

		String name = "数据导出";
		String filename = frmBill.Name + "_" + BP.DA.DataType.getCurrentDataTime()CNOfLong + ".xls";
		String filePath = ExportDGToExcel(Search_Data(), rpts.GetNewEntity, null, null, filename);


		return filePath;
	}

	public final DataTable Search_Data()
	{
		DataSet ds = new DataSet();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询语句

		MapData md = new MapData(this.getFrmID());


		//取出来查询条件.
		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK( WebUser.getNo() + "_" + this.getFrmID() + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		GEEntitys rpts = new GEEntitys(this.getFrmID());

		Attrs attrs = rpts.GetNewEntity.EnMap.Attrs;

		QueryObject qo = new QueryObject(rpts);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 关键字字段.
		String keyWord = ur.SearchKey;

		if (md.GetParaBoolen("IsSearchKey") && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
		{
			Attr attrPK = new Attr();
			for (Attr attr : attrs)
			{
				if (attr.IsPK)
				{
					attrPK = attr;
					break;
				}
			}
			int i = 0;
			String enumKey = ","; //求出枚举值外键.
			for (Attr attr : attrs)
			{
				switch (attr.MyFieldType)
				{
					case FieldType.Enum:
						enumKey = "," + attr.Key + "Text,";
						break;
					case FieldType.FK:

						continue;
					default:
						break;
				}

				if (attr.MyDataType != DataType.AppString)
				{
					continue;
				}

				//排除枚举值关联refText.
				if (attr.MyFieldType == FieldType.RefText)
				{
					if (enumKey.contains("," + attr.Key + ",") == true)
					{
						continue;
					}
				}

				if (attr.Key.equals("FK_Dept"))
				{
					continue;
				}

				i++;
				if (i == 1)
				{
					/* 第一次进来。 */
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
			qo.MyParas.Add("SKey", keyWord);
			qo.addRightBracket();

		}
		else
		{
			qo.AddHD();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 关键字段查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 时间段的查询
		if (md.GetParaInt("DTSearchWay") != (int)DTSearchWay.None && DataType.IsNullOrEmpty(ur.DTFrom) == false)
		{
			String dtFrom = ur.DTFrom; // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
			String dtTo = ur.DTTo; // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

			//按日期查询
			if (md.GetParaInt("DTSearchWay") == (int)DTSearchWay.ByDate)
			{
				qo.addAnd();
				qo.addLeftBracket();
				dtTo += " 23:59:59";
				qo.SQL = md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}

			if (md.GetParaInt("DTSearchWay") == (int)DTSearchWay.ByDateTime)
			{
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = LocalDateTime.parse(dtFrom).AddDays(-1).toString("yyyy-MM-dd") + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}

				qo.addAnd();
				qo.addLeftBracket();
				qo.SQL = md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 时间段的查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 外键或者枚举的查询

		//获得关键字.
		AtPara ap = new AtPara(ur.Vals);
		for (String str : ap.getHisHT().keySet())
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}
			qo.addAnd();
			qo.addLeftBracket();

			//获得真实的数据类型.
			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
				var typeVal = BP.Sys.Glo.GenerRealType(attrs, str, ap.GetValStrByKey(str));
				qo.AddWhere(str, typeVal);
			}
			else
			{
				qo.AddWhere(str, ap.GetValStrByKey(str));
			}

			qo.addRightBracket();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 外键或者枚举的查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 查询语句
		qo.addOrderBy("OID");
		return qo.DoQueryToTable();

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion  执行导出

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 单据导入
	public final String ImpData_Done()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles();
		if (HttpContextHelper.RequestFilesCount == 0)
		{
			return "err@请选择要导入的数据信息。";
		}

		String errInfo = "";

		String ext = ".xls";
		String fileName = (new File(HttpContextHelper.RequestFiles(0).FileName)).getName();
		if (fileName.contains(".xlsx"))
		{
			ext = ".xlsx";
		}


		//设置文件名
		String fileNewName = LocalDateTime.now().toString("yyyyMMddHHmmssff") + ext;

		//文件存放路径
		String filePath = BP.Sys.SystemConfig.PathOfTemp + "\\" + fileNewName;
		HttpContextHelper.UploadFile(HttpContextHelper.RequestFiles(0), filePath);

		//从excel里面获得数据表.
		DataTable dt = BP.DA.DBLoad.ReadExcelFileToDataTable(filePath);

		//删除临时文件
		(new File(filePath)).delete();

		if (dt.Rows.size() == 0)
		{
			return "err@无导入的数据";
		}

		//获得entity.
		FrmBill bill = new FrmBill(this.getFrmID());
		GEEntitys rpts = new GEEntitys(this.getFrmID());
		GEEntity en = new GEEntity(this.getFrmID());


		String noColName = ""; //实体列的编号名称.
		String nameColName = ""; //实体列的名字名称.

		BP.En.Map map = en.EnMap;
		Attr attr = map.GetAttrByKey("BillNo");
		noColName = attr.Desc;
		String codeStruct = bill.getEnMap().CodeStruct;
		attr = map.GetAttrByKey("Title");
		nameColName = attr.Desc;

		//定义属性.
		Attrs attrs = map.Attrs;

		int impWay = this.GetRequestValInt("ImpWay");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 清空方式导入.
		//清空方式导入.
		int count = 0; //导入的行数
		int changeCount = 0; //更新的行数
		String successInfo = "";
		if (impWay == 0)
		{
			rpts.ClearTable();
			GEEntity myen = new GEEntity(this.getFrmID());

			for (DataRow dr : dt.Rows)
			{
				String no = dr.get(noColName).toString();
				String name = dr.get(nameColName).toString();
				myen.OID = 0;

				//判断是否是自增序列，序列的格式
				if (!DataType.IsNullOrEmpty(codeStruct))
				{
					no = tangible.StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
				}


				myen.SetValByKey("BillNo", no);
				if (myen.Retrieve("BillNo", no) == 1)
				{
					errInfo += "err@编号[" + no + "][" + name + "]重复.";
					continue;
				}

				//给实体赋值
				errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 0);
				count++;
				successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 清空方式导入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 更新方式导入
		if (impWay == 1 || impWay == 2)
		{
			for (DataRow dr : dt.Rows)
			{
				String no = dr.get(noColName).toString();
				String name = dr.get(nameColName).toString();
				//判断是否是自增序列，序列的格式
				if (!DataType.IsNullOrEmpty(codeStruct))
				{
					no = tangible.StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
				}
				GEEntity myen = rpts.GetNewEntity instanceof GEEntity ? (GEEntity)rpts.GetNewEntity : null;
				myen.SetValByKey("BillNo", no);
				if (myen.Retrieve("BillNo", no) == 1)
				{
					//给实体赋值
					errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 1);
					changeCount++;
					successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的更新成功</span><br/>";
					continue;
				}


				//给实体赋值
				errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 0);
				count++;
				successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
	}

	private String SetEntityAttrVal(String no, DataRow dr, Attrs attrs, GEEntity en, DataTable dt, int saveType)
	{
		if (saveType == 0)
		{
			String OID = MyDict_CreateBlankDictID();
			en.OID = Long.parseLong(OID);
			en.RetrieveFromDBSources();
		}

		String errInfo = "";
		//按照属性赋值.
		for (Attr item : attrs)
		{
			if (item.Key.equals("BillNo"))
			{
				en.SetValByKey(item.Key, no);
				continue;
			}
			if (item.Key.equals("Title"))
			{
				en.SetValByKey(item.Key, dr.get(item.Desc).toString());
				continue;
			}

			if (dt.Columns.Contains(item.Desc) == false)
			{
				continue;
			}

			//枚举处理.
			if (item.MyFieldType == FieldType.Enum)
			{
				String val = dr.get(item.Desc).toString();

				SysEnum se = new SysEnum();
				int i = se.Retrieve(SysEnumAttr.EnumKey, item.UIBindKey, SysEnumAttr.Lab, val);

				if (i == 0)
				{
					errInfo += "err@枚举[" + item.Key + "][" + item.Desc + "]，值[" + val + "]不存在.";
					continue;
				}

				en.SetValByKey(item.Key, se.IntKey);
				continue;
			}

			//外键处理.
			if (item.MyFieldType == FieldType.FK)
			{
				String val = dr.get(item.Desc).toString();
				Entity attrEn = item.HisFKEn;
				int i = attrEn.Retrieve("Name", val);
				if (i == 0)
				{
					errInfo += "err@外键[" + item.Key + "][" + item.Desc + "]，值[" + val + "]不存在.";
					continue;
				}

				if (i != 1)
				{
					errInfo += "err@外键[" + item.Key + "][" + item.Desc + "]，值[" + val + "]重复..";
					continue;
				}

				//把编号值给他.
				en.SetValByKey(item.Key, attrEn.GetValByKey("No"));
				continue;
			}

			//boolen类型的处理..
			if (item.MyDataType == DataType.AppBoolean)
			{
				String val = dr.get(item.Desc).toString();
				if (val.equals("是") || val.equals("有"))
				{
					en.SetValByKey(item.Key, 1);
				}
				else
				{
					en.SetValByKey(item.Key, 0);
				}
				continue;
			}

			String myval = dr.get(item.Desc).toString();
			en.SetValByKey(item.Key, myval);
		}

		en.SetValByKey("BillState", BillState.Editing.getValue());
		en.Update();

		return errInfo;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 获得demo信息.
	public final String MethodDocDemoJS_Init()
	{
		MethodFunc func = new MethodFunc(this.getMyPK());
		return func.getMethodDoc_JavaScript_Demo();
	}
	public final String MethodDocDemoSQL_Init()
	{
		MethodFunc func = new MethodFunc(this.getMyPK());
		return func.getMethodDoc_SQL_Demo();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 获得demo信息.

}