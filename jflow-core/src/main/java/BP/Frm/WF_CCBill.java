package BP.Frm;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.CommonUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.*;
import BP.Web.*;
import BP.En.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;

/** 
 页面功能实体
*/
public class WF_CCBill extends WebContralBase {

	///#region 构造方法.

	/**
	 * 构造函数
	 */
	public WF_CCBill() {
	}

	///#endregion 构造方法.

	/**
	 * 发起列表.
	 *
	 * @return
	 * @throws Exception
	 */
	public final String Start_Init() throws Exception {
		//获得发起列表. 
		DataSet ds = BP.Frm.Dev2Interface.DB_StartBills(WebUser.getNo());

		//返回组合
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 草稿列表
	 *
	 * @return
	 * @throws Exception
	 */
	public final String Draft_Init() throws Exception {
		//草稿列表.
		DataTable dt = BP.Frm.Dev2Interface.DB_Draft(this.getFrmID(), WebUser.getNo());

		//返回组合
		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	/**
	 * 单据初始化
	 *
	 * @return
	 * @throws Exception
	 */
	public final String MyBill_Init() throws Exception {
		//获得发起列表. 
		DataSet ds = BP.Frm.Dev2Interface.DB_StartBills(WebUser.getNo());

		//返回组合
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 执行
	 *
	 * @return 返回执行结果
	 * @throws Exception
	 */
	public final String DoMethod_ExeSQL() throws Exception {
		MethodFunc func = new MethodFunc(this.getMyPK());
		String doc = func.getMethodDoc_SQL();

		GEEntity en = new GEEntity(func.getFrmID(), this.getWorkID());
		doc = BP.WF.Glo.DealExp(doc, en, null); //替换里面的内容.

		try {
			DBAccess.RunSQLs(doc);
			if (func.getMsgSuccess().equals("")) {
				func.setMsgSuccess("执行成功.");
			}

			return func.getMsgSuccess();
		} catch (RuntimeException ex) {
			if (func.getMsgErr().equals("")) {
				func.setMsgErr("执行失败(DoMethod_ExeSQL).");
			}
			return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
		}
	}

	/**
	 * 执行SQL
	 *
	 * @return
	 * @throws Exception
	 */
	public final String DoMethodPara_ExeSQL() throws Exception {
		MethodFunc func = new MethodFunc(this.getMyPK());
		String doc = func.getMethodDoc_SQL();

		GEEntity en = new GEEntity(func.getFrmID(), this.getWorkID());


		///#region 替换参数变量.
		if(doc.contains("@") == true){
			MapAttrs attrs = new MapAttrs();
			attrs.Retrieve(MapAttrAttr.FK_MapData, this.getMyPK());
			for (MapAttr item : attrs.ToJavaList()) {
				if(doc.contains("@")==false)
					break;
				if (item.getUIContralType() == UIContralType.TB) {
					doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("TB_" + item.getKeyOfEn()));
					continue;
				}

				if (item.getUIContralType() == UIContralType.DDL) {
					doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("DDL_" + item.getKeyOfEn()));
					continue;
				}


				if (item.getUIContralType() == UIContralType.CheckBok) {
					doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("CB_" + item.getKeyOfEn()));
					continue;
				}

				if (item.getUIContralType() == UIContralType.RadioBtn) {
					doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("RB_" + item.getKeyOfEn()));
					continue;
				}
			}
		}

		doc = BP.WF.Glo.DealExp(doc, en, null); //替换里面的内容.

		///#endregion 替换参数变量.


		///#region 开始执行SQLs.
		try {
			DBAccess.RunSQLs(doc);
			if (func.getMsgSuccess().equals("")) {
				func.setMsgSuccess("执行成功.");
			}

			return func.getMsgSuccess();
		} catch (RuntimeException ex) {
			if (func.getMsgErr().equals("")) {
				func.setMsgErr("执行失败.");
			}

			return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
		}

	}


	///#region 单据处理.

	/**
	 * 创建空白的WorkID.
	 *
	 * @return
	 * @throws Exception
	 */
	public final String MyBill_CreateBlankBillID() throws Exception {
		String billNo = this.GetRequestVal("BillNo");
		return String.valueOf(BP.Frm.Dev2Interface.CreateBlankBillID(this.getFrmID(), WebUser.getNo(), null, billNo));
	}

	/**
	 * 创建空白的DictID.
	 *
	 * @return
	 * @throws Exception
	 */
	public final String MyDict_CreateBlankDictID() throws Exception {
		String billNo = this.GetRequestVal("BillNo");
		return String.valueOf(BP.Frm.Dev2Interface.CreateBlankDictID(this.getFrmID(), WebUser.getNo(), null, billNo));
	}

	/**
	 * 执行保存
	 *
	 * @return
	 * @throws Exception
	 */
	public final String MyBill_SaveIt() throws Exception {
		//执行保存.
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
		Object tempVar = BP.Sys.PubClass.CopyFromRequest(rpt);
		rpt = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;



		Hashtable ht = GetMainTableHT();
		for (Object item : ht.keySet()) {
			if (item != null)
				rpt.SetValByKey(item.toString(), ht.get(item));
		}

		rpt.setOID(this.getWorkID());
		rpt.SetValByKey("BillState", BillState.Editing.getValue());
		rpt.Update();

		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}

	public String MyBill_Submit() throws Exception {
		//执行保存.
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());


		Hashtable ht = GetMainTableHT();


		for (Object item : ht.keySet()) {
			rpt.SetValByKey(item.toString(), ht.get(item));
		}

		rpt.setOID(this.getWorkID());
		rpt.SetValByKey("BillState", BillState.Over.getValue());
		rpt.Update();

		String str = BP.Frm.Dev2Interface.SubmitWork(this.getFrmID(), this.getWorkID());
		return str;
	}

	/**
	 * 执行保存
	 *
	 * @return
	 * @throws Exception
	 */
	public final String MyDict_SaveIt() throws Exception {
		//执行保存.
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
		Object tempVar = BP.Sys.PubClass.CopyFromRequest(rpt);
		rpt = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;

		Hashtable ht = GetMainTableHT();
		for (Object item : ht.keySet()) {
			if (item != null)
				rpt.SetValByKey(item.toString(), ht.get(item));
		}

		rpt.setOID(this.getWorkID());
		rpt.SetValByKey("BillState", BillState.Editing.getValue());
		rpt.Update();

		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}

	/// <summary>
	/// 执行保存
	/// </summary>
	/// <returns></returns>
	public String MyDict_Submit() throws Exception {
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
		Object tempVar = BP.Sys.PubClass.CopyFromRequest(rpt);
		rpt = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;

		Hashtable ht = GetMainTableHT();
		for (Object item : ht.keySet()) {
			rpt.SetValByKey(item.toString(), ht.get(item));
		}

		rpt.setOID(this.getWorkID());
		rpt.SetValByKey("BillState", BillState.Over.getValue());
		rpt.Update();

		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}

	public final String GetFrmEntitys() throws Exception {
		GEEntitys rpts = new GEEntitys(this.getFrmID());
		QueryObject qo = new QueryObject(rpts);
		qo.AddWhere("BillState", " != ", 0);
		qo.DoQuery();
		return BP.Tools.Json.ToJson(rpts.ToDataTableField());
	}

	private Hashtable GetMainTableHT() throws UnsupportedEncodingException {
		Hashtable htMain = new Hashtable();
		Enumeration enu = getRequest().getParameterNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (key == null) {
				continue;
			}

			if (key.contains("TB_")) {
				if (htMain.containsKey(key.replace("TB_", "")) == false) {
					htMain.put(key.replace("TB_", ""), URLDecoder.decode(this.GetRequestVal(key), "UTF-8"));
				}
				continue;
			}

			if (key.contains("DDL_")) {
				htMain.put(key.replace("DDL_", ""), URLDecoder.decode(this.GetRequestVal(key), "UTF-8"));
				continue;
			}

			if (key.contains("CB_")) {
				htMain.put(key.replace("CB_", ""), URLDecoder.decode(this.GetRequestVal(key), "UTF-8"));
				continue;
			}

			if (key.contains("RB_")) {
				htMain.put(key.replace("RB_", ""), URLDecoder.decode(this.GetRequestVal(key), "UTF-8"));
				continue;
			}
		}
		return htMain;
	}

	public final String MyBill_SaveAsDraft() throws Exception {
		String str = BP.Frm.Dev2Interface.SaveWork(this.getFrmID(), this.getWorkID());
		return str;
	}

	//删除单据
	public final String MyBill_Delete() throws Exception {
		return BP.Frm.Dev2Interface.MyBill_Delete(this.getFrmID(), this.getWorkID());
	}

	public final String MyBill_Deletes() throws Exception {
		return BP.Frm.Dev2Interface.MyBill_DeleteDicts(this.getFrmID(), this.GetRequestVal("WorkIDs"));
	}

	//删除实体
	public final String MyDict_Delete() throws Exception {
		return BP.Frm.Dev2Interface.MyDict_Delete(this.getFrmID(), this.getWorkID());
	}

	public final String MyEntityTree_Delete() throws Exception {
		return BP.Frm.Dev2Interface.MyEntityTree_Delete(this.getFrmID(), this.GetRequestVal("BillNo"));
	}

	/**
	 * 复制单据数据
	 *
	 * @return
	 * @throws Exception
	 */
	public final String MyBill_Copy() throws Exception {
		return BP.Frm.Dev2Interface.MyBill_Copy(this.getFrmID(), this.getWorkID());
	}

	///#endregion 单据处理.


	///#region 获取查询条件
	public final String Search_ToolBar() throws Exception {
		DataSet ds = new DataSet();

		DataTable dt = new DataTable();

		//根据FrmID获取Mapdata
		MapData md = new MapData(this.getFrmID());
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		//获取字段属性
		MapAttrs attrs = new MapAttrs(this.getFrmID());


		///#region //增加枚举/外键字段信息

		dt.Columns.Add("Field", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.TableName = "Attrs";
		ds.Tables.add(dt);

		String[] ctrls = md.getRptSearchKeys().split("[*]", -1);
		DataTable dtNoName = null;

		MapAttr mapattr;
		DataRow dr = null;
		for (String ctrl : ctrls) {
			//增加判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示
			if (DataType.IsNullOrEmpty(ctrl) || !DataType.IsNullOrEmpty(this.GetRequestVal(ctrl))) {
				continue;
			}

			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ctrl);
			mapattr = tempVar instanceof MapAttr ? (MapAttr) tempVar : null;
			dr = dt.NewRow();
			dr.setValue("Field", mapattr.getKeyOfEn());
			dr.setValue("Name", mapattr.getName());
			dr.setValue("Width", mapattr.getUIWidth());
			dt.Rows.add(dr);

			Attr attr = mapattr.getHisAttr();
			if (mapattr == null) {
				continue;
			}

			if (attr.getKey().equals("FK_Dept"))
				continue;

			if (attr.getIsEnum() == true) {
				SysEnums ses = new SysEnums(mapattr.getUIBindKey());
				DataTable dtEnum = ses.ToDataTableField();
				dtEnum.TableName = mapattr.getKeyOfEn();
				ds.Tables.add(dtEnum);
				continue;
			}
			if (attr.getIsFK() == true) {
				Entities ensFK = attr.getHisFKEns();
				ensFK.RetrieveAll();

				DataTable dtEn = ensFK.ToDataTableField();
				dtEn.TableName = attr.getKey();
				ds.Tables.add(dtEn);
			}
			//绑定SQL的外键
			if (attr.getUIDDLShowType() == BP.En.DDLShowType.BindSQL
					&& DataType.IsNullOrEmpty(attr.getUIBindKey())==false
					&& ds.Tables.contains(attr.getKey()) == false) {
				//获取SQl
				DataTable dtSQl = BP.Sys.PubClass.GetDataTableByUIBineKey(attr.getUIBindKey());
				for (DataColumn col : dtSQl.Columns) {
					String colName = col.ColumnName.toLowerCase();
					switch (colName) {
						case "no":
						case "NO":
							col.ColumnName = "No";
							break;
						case "name":
						case "NAME":
							col.ColumnName = "Name";
							break;
						case "parentno":
						case "PARENTNO":
							col.ColumnName = "ParentNo";
							break;
						default:
							break;
					}
				}
				dtSQl.TableName = attr.getKey();
				if (ds.Tables.contains(attr.getKey()) == false) {
					ds.Tables.add(dtSQl);
				}

			}

		}


		//数据查询权限除只查看自己创建的数据外增加部门的查询条件
		SearchDataRole searchDataRole = SearchDataRole.forValue(md.GetParaInt("SearchDataRole"));
		if (searchDataRole != SearchDataRole.ByOnlySelf)
		{
			DataTable dd = GetDeptDataTable(searchDataRole, md);
			if(dd.Rows.size() ==0 && md.GetParaInt("SearchDataRoleByDeptStation")==1)
				dd = GetDeptAndSubLevel();
			if(dd.Rows.size() != 0)
			{
				//增加部门的查询条件
				if (dt.Rows.contains("FK_Dept") == false)
				{
					dr = dt.NewRow();
					dr.setValue("Field","FK_Dept");
					dr.setValue("Name","部门");
					dr.setValue("Width",120);
					dt.Rows.add(dr);
				}

				dd.TableName = "FK_Dept";
				ds.Tables.add(dd);

			}
		}



		return BP.Tools.Json.ToJson(ds);

	}

	private DataTable GetDeptDataTable(SearchDataRole searchDataRole,MapData md) throws Exception
	{
		//增加部门的外键
		DataTable dt = new DataTable();
		String sql = "";
		if (searchDataRole == SearchDataRole.ByDept)
		{
			sql = "SELECT D.No,D.Name From Port_Dept D,Port_DeptEmp E WHERE D.No=E.FK_Dept AND E.FK_Emp='" + WebUser.getNo() + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
		}
		if (searchDataRole == SearchDataRole.ByDeptAndSSubLevel)
		{
			dt = GetDeptAndSubLevel();
		}
		if (searchDataRole == SearchDataRole.ByStationDept)
		{
			sql = "SELECT D.No,D.Name From Port_Dept D WHERE D.No IN(SELECT F.FK_Dept FROM Frm_StationDept F,Port_DeptEmpStation P Where F.FK_Station = P.FK_Station AND F.FK_Frm='" + md.getNo() + "' AND P.FK_Emp='" + WebUser.getNo() + "')";
			dt = DBAccess.RunSQLReturnTable(sql);
		}
		for(DataColumn col : dt.Columns)
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

				default:
					break;
			}

		}
		return dt;
	}

	private DataTable GetDeptAndSubLevel()throws Exception
	{
		//获取本部门和兼职部门
		String sql = "SELECT D.No,D.Name From Port_Dept D,Port_DeptEmp E WHERE D.No=E.FK_Dept AND E.FK_Emp='" + WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		DataTable dd = dt.copy();
		for(DataRow dr : dd.Rows)
		{
			GetSubLevelDeptByParentNo(dt, dr.getValue(0).toString());
		}
		return dt;
	}

	private void GetSubLevelDeptByParentNo( DataTable dt,String parentNo)
	{
		String sql = "SELECT No,Name FROM Port_Dept Where ParentNo='" + parentNo + "'";
		DataTable dd = DBAccess.RunSQLReturnTable(sql);

		for(DataRow dr : dd.Rows)
		{
			if (dt.Rows.contains(dr.getValue(0).toString()) == true)
				continue;
			dt.Rows.add(dr);

			GetSubLevelDeptByParentNo(dt, dr.getValue(0).toString());

		}
	}


	///#endregion 查询条件


	public final String Search_Init() throws Exception {
		DataSet ds = new DataSet();


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


		for (MapAttr attr : mapattrs.ToJavaList()) {
			String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
			if (searchVisable.equals("0")) {
				continue;
			}
			if (attr.getUIVisible() == false) {
				continue;
			}
			row = dt.NewRow();
			row.setValue("KeyOfEn", attr.getKeyOfEn());
			row.setValue("Name", attr.getName());
			row.setValue("Width", attr.getUIWidthInt());
			row.setValue("UIContralType", attr.getUIContralType().getValue());
			row.setValue("LGType", attr.getLGType().getValue());
			row.setValue("AtPara", attr.GetValStringByKey("AtPara"));
			dt.Rows.add(row);
		}
		ds.Tables.add(dt);

		///#endregion 查询显示的列


		///#region 查询语句
		MapData md = new MapData(this.getFrmID());

		//取出来查询条件.
		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK(WebUser.getNo() + "_" + this.getFrmID() + "_SearchAttrs");
		ur.RetrieveFromDBSources();

		GEEntitys rpts = new GEEntitys(this.getFrmID());

		Attrs attrs = rpts.getNewEntity().getEnMap().getAttrs();

		QueryObject qo = new QueryObject(rpts);


		///#region 关键字字段.
		String keyWord = ur.getSearchKey();
		boolean isFirst = true; //是否第一次拼接SQL

		if (md.GetParaBoolen("IsSearchKey") && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1) {
			Attr attrPK = new Attr();
			for (Attr attr : attrs) {
				if (attr.getIsPK()) {
					attrPK = attr;
					break;
				}
			}
			int i = 0;
			String enumKey = ","; //求出枚举值外键.
			for (Attr attr : attrs) {
				switch (attr.getMyFieldType()) {
					case Enum:
						enumKey = "," + attr.getKey() + "Text,";
						break;
					case FK:
						continue;
					default:
						break;
				}

				if (attr.getMyDataType() != DataType.AppString) {
					continue;
				}

				//排除枚举值关联refText.
				if (attr.getMyFieldType() == FieldType.RefText) {
					if (enumKey.contains("," + attr.getKey() + ",") == true) {
						continue;
					}
				}

				if (attr.getKey().equals("FK_Dept")) {
					continue;
				}

				i++;
				if (i == 1) {
					isFirst = false;
					/* 第一次进来。 */
					qo.addLeftBracket();
					if (SystemConfig.getAppCenterDBVarStr().equals("@")
							|| SystemConfig.getAppCenterDBVarStr().equals(":")) {
						qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
					} else {
						qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
					}
					continue;
				}
				qo.addOr();

				if (SystemConfig.getAppCenterDBVarStr().equals("@")
						|| SystemConfig.getAppCenterDBVarStr().equals(":")) {
					qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
				} else {
					qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
				}

			}
			qo.getMyParas().Add("SKey", keyWord);
			qo.addRightBracket();
		} else if (DataType.IsNullOrEmpty(md.GetParaString("RptStringSearchKeys")) == false){
			String field = "";//字段名
			String fieldValue = "";//字段值
			int idx = 0;

			//获取查询的字段
			String[] searchFields = md.GetParaString("RptStringSearchKeys").split("[*]");
			for(String str : searchFields)
			{
				if (DataType.IsNullOrEmpty(str) == true)
					continue;

				//字段名
				String[] items  = str.split(",");
				if (items.length==2 && DataType.IsNullOrEmpty(items[0]) == true)
					continue;
				field = items[0];
				//字段名对应的字段值
				fieldValue = ur.GetParaString(field);
				if (DataType.IsNullOrEmpty(fieldValue) == true)
					continue;
				idx++;
				if (idx == 1)
				{
					isFirst = false;
					/* 第一次进来。 */
					qo.addLeftBracket();
					if (SystemConfig.getAppCenterDBVarStr().equals("@")
							|| SystemConfig.getAppCenterDBVarStr().equals(":"))
						qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr()+ field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr()+ field + "+'%'"));
					else
						qo.AddWhere(field, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
					qo.getMyParas().Add(field, fieldValue);
					continue;
				}
				qo.addAnd();

				if (SystemConfig.getAppCenterDBVarStr().equals("@")
						|| SystemConfig.getAppCenterDBVarStr().equals(":"))
					qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr()+ field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr()+ field + "+'%'"));
				else
					qo.AddWhere(field, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
				qo.getMyParas().Add(field, fieldValue);


			}
			if (idx != 0)
				qo.addRightBracket();
		}

		///#endregion 关键字段查询


		///#region 时间段的查询
		if (md.GetParaInt("DTSearchWay") != DTSearchWay.None.getValue() && DataType.IsNullOrEmpty(ur.getDTFrom()) == false) {
			String dtFrom = ur.getDTFrom();
			String dtTo = ur.getDTTo();

			//按日期查询
			if (md.GetParaInt("DTSearchWay") == DTSearchWay.ByDate.getValue()) {
				if (isFirst == false)
					qo.addAnd();
				else
					isFirst = false;
				qo.addLeftBracket();
				dtTo += " 23:59:59";
				qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
				qo.addRightBracket();
			}

			if (md.GetParaInt("DTSearchWay") == DTSearchWay.ByDateTime.getValue()) {
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = DateUtils.addDay(DateUtils.parse(dtFrom, "yyyy-MM-dd"), -1) + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1) {
					dtTo += " 24:00";
				}

				if (isFirst == false)
					qo.addAnd();
				else
					isFirst = false;
				qo.addLeftBracket();
				qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
				qo.addRightBracket();
			}
		}

		///#endregion 时间段的查询


		///#region 外键或者枚举的查询

		//获得关键字.
		AtPara ap = new AtPara(ur.getVals());
		for (String str : ap.getHisHT().keySet()) {
			String val = ap.GetValStrByKey(str);
			if (val.equals("all")) {
				continue;
			}
			if (isFirst == false)
				qo.addAnd();
			else
				isFirst = false;
			qo.addLeftBracket();


			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
				Object typeVal = BP.Sys.Glo.GenerRealType(attrs, str, ap.GetValStrByKey(str));
				qo.AddWhere(str, typeVal);

			} else {
				qo.AddWhere(str, ap.GetValStrByKey(str));
			}

			qo.addRightBracket();
		}

		///#endregion 外键或者枚举的查询

		//#region 设置隐藏字段的过滤查询
		FrmBill frmBill = new FrmBill(this.getFrmID());
		String hidenField = frmBill.GetParaString("HidenField");

		if(DataType.IsNullOrEmpty(hidenField) == false)
		{
			hidenField = hidenField.replace("[%]", "%");
			for(String field : hidenField.split(";")){
			if (field == "")
				continue;
			if (field.split(",").length != 3)
				throw new Exception("单据" + frmBill.getName() + "的过滤设置规则错误：" + hidenField + ",请联系管理员检查");
			String[] str = field.split(",");
			if (isFirst == false)
				qo.addAnd();
			else
				isFirst = false;
			qo.addLeftBracket();

			String val = str[2].replace("WebUser.No", WebUser.getNo());
			val = val.replace("WebUser.Name", WebUser.getName());
			val = val.replace("WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
			val = val.replace("WebUser.FK_DeptName", WebUser.getFK_DeptName());
			val = val.replace("WebUser.FK_Dept", WebUser.getFK_Dept());

			//获得真实的数据类型.
			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
				Object valType = BP.Sys.Glo.GenerRealType(attrs,
						str[0], val);
				qo.AddWhere(str[0], str[1], valType.toString());
			}
			else
			{
				qo.AddWhere(str[0], str[1], val);
			}
			qo.addRightBracket();
			continue;
		}

		}

        //#endregion 设置隐藏字段的查询
		///#endregion 查询语句

		if (isFirst == false)
			qo.addAnd();
		qo.AddWhere("BillState", "!=", 0);

		if(SearchDataRole.forValue(md.GetParaInt("SearchDataRole")) !=SearchDataRole.SearchAll){
			if(SearchDataRole.forValue(md.GetParaInt("SearchDataRole")) == SearchDataRole.ByOnlySelf && DataType.IsNullOrEmpty(hidenField) == true
					||(md.GetParaInt("SearchDataRoleByDeptStation")==0 && DataType.IsNullOrEmpty(ap.GetValStrByKey("FK_Dept"))==true))
			{
				qo.addAnd();
				qo.AddWhere("Starter", "=", WebUser.getNo());
			}
		}


		//获得行数.
		ur.SetPara("RecCount", qo.GetCount());
		ur.Save();

		//获取配置信息
		String fieldSet = frmBill.getFieldSet();
		String oper = "";
		if (DataType.IsNullOrEmpty(fieldSet) == false)
		{
			String ptable = rpts.getNewEntity().getEnMap().getPhysicsTable();
			dt = new DataTable("Search_FieldSet");
			dt.Columns.Add("Field");
			dt.Columns.Add("Type");
			dt.Columns.Add("Value");
			DataRow dr;
			String[] strs = fieldSet.split("@");
			for(String str : strs)
			{
				if(DataType.IsNullOrEmpty(str))
					continue;
				String[] item = str.split("=");
				if (item.length == 2)
				{
					if (item[1].contains(",") == true)
					{
						String[] ss = item[1].split(",");
						for(String s : ss)
						{
							dr = dt.NewRow();
							dr.setValue("Field",attrs.GetAttrByKey(s).getDesc());
							dr.setValue("Type",item[0]);
							dt.Rows.add(dr);

							oper += item[0] + "(" + ptable + "." + s + ") AS "+item[0]+s + ",";
						}
					}
					else
					{
						dr = dt.NewRow();
						dr.setValue("Field",attrs.GetAttrByKey(item[1]).getDesc());
						dr.setValue("Type",item[0]);
						dt.Rows.add(dr);

						oper += item[0] + "(" + ptable + "." + item[1] + ")"+item[0]+item[1] + ",";
					}
				}
			}
			oper = oper.substring(0, oper.length() - 1);
			DataTable dd = qo.GetSumOrAvg(oper);

			for (int i = 0; i < dt.Rows.size(); i++)
			{
				DataRow ddr = dt.Rows.get(i);
				ddr.setValue("Value",dd.Rows.get(0).getValue(i));
			}
			ds.Tables.add(dt);
		}

		if (DataType.IsNullOrEmpty(ur.getOrderBy()) == false && DataType.IsNullOrEmpty(ur.getOrderWay()) == false) {
			qo.DoQuery("OID", this.getPageSize(), this.getPageIdx(), ur.getOrderBy(), ur.getOrderWay());
		} else {
			qo.DoQuery("OID", this.getPageSize(), this.getPageIdx());
		}

		DataTable mydt = rpts.ToDataTableField();
		mydt.TableName = "DT";

		ds.Tables.add(mydt); //把数据加入里面.

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 初始化
	 *
	 * @return
	 * @throws Exception
	 */
	public final String GenerBill_Init() throws Exception {
		GenerBills bills = new GenerBills();
		bills.Retrieve(GenerBillAttr.Starter, WebUser.getNo());
		return bills.ToJson();
	}

	/**
	 * 查询初始化
	 *
	 * @return
	 * @throws Exception
	 */
	public final String SearchData_Init() throws Exception {
		DataSet ds = new DataSet();
		String sql = "";

		String tSpan = this.GetRequestVal("TSpan");
		if (tSpan.equals("")) {
			tSpan = null;
		}


		///#region 1、获取时间段枚举/总数.
		SysEnums ses = new SysEnums("TSpan");
		DataTable dtTSpan = ses.ToDataTableField();
		dtTSpan.TableName = "TSpan";
		ds.Tables.add(dtTSpan);

		GenerBill gb = new GenerBill();
		gb.CheckPhysicsTable();

		sql = "SELECT TSpan as No, COUNT(WorkID) as Num FROM Frm_GenerBill WHERE FrmID='" + this.getFrmID() + "'  AND Starter='" + WebUser.getNo() + "' AND BillState >= 1 GROUP BY TSpan";

		DataTable dtTSpanNum = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow drEnum : dtTSpan.Rows) {
			String no = drEnum.get("IntKey").toString();
			for (DataRow dr : dtTSpanNum.Rows) {
				if (dr.getValue("No").toString().equals(no)) {
					drEnum.setValue("Lab", drEnum.get("Lab").toString() + "(" + dr.getValue("Num") + ")");
					break;
				}
			}
		}

		///#endregion


		///#region 2、处理流程类别列表.
		sql = " SELECT  A.BillState as No, B.Lab as Name, COUNT(WorkID) as Num FROM Frm_GenerBill A, Sys_Enum B ";
		sql += " WHERE A.BillState=B.IntKey AND B.EnumKey='BillState' AND  A.Starter='" + WebUser.getNo() + "' AND BillState >=1";
		if (tSpan.equals("-1") == false) {
			sql += "  AND A.TSpan=" + tSpan;
		}

		sql += "  GROUP BY A.BillState, B.Lab  ";

		DataTable dtFlows = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dtFlows.Columns.get(0).ColumnName = "No";
			dtFlows.Columns.get(1).ColumnName = "Name";
			dtFlows.Columns.get(2).ColumnName = "Num";
		}
		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);

		///#endregion


		///#region 3、处理流程实例列表.
		String sqlWhere = "";
		sqlWhere = "(1 = 1)AND Starter = '" + WebUser.getNo() + "' AND BillState >= 1";
		if (tSpan.equals("-1") == false) {
			sqlWhere += "AND (TSpan = '" + tSpan + "') ";
		}

		if (this.getFK_Flow() != null) {
			sqlWhere += "AND (FrmID = '" + this.getFrmID() + "')  ";
		} else {
			// sqlWhere += ")";
		}
		sqlWhere += "ORDER BY RDT DESC";

		String fields = " WorkID,FrmID,FrmName,Title,BillState, Starter, StarterName,Sender,RDT ";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			sql = "SELECT " + fields + " FROM (SELECT * FROM Frm_GenerBill WHERE " + sqlWhere + ") WHERE rownum <= 50";
		} else if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
			sql = "SELECT  TOP 50 " + fields + " FROM Frm_GenerBill WHERE " + sqlWhere;
		} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			sql = "SELECT  " + fields + " FROM Frm_GenerBill WHERE " + sqlWhere + " LIMIT 50";
		}

		DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			mydt.Columns.get(0).ColumnName = "WorkID";
			mydt.Columns.get(1).ColumnName = "FrmID";
			mydt.Columns.get(2).ColumnName = "FrmName";
			mydt.Columns.get(3).ColumnName = "Title";
			mydt.Columns.get(4).ColumnName = "BillState";
			mydt.Columns.get(5).ColumnName = "Starter";
			mydt.Columns.get(6).ColumnName = "StarterName";
			mydt.Columns.get(7).ColumnName = "Sender";
			mydt.Columns.get(8).ColumnName = "RDT";
		}

		mydt.TableName = "Frm_Bill";
		if (mydt != null) {
			mydt.Columns.Add("TDTime");

		}


		ds.Tables.add(mydt);

		return BP.Tools.Json.ToJson(ds);
	}


	///#region 单据导出
	public final String Search_Exp() throws Exception {
		FrmBill frmBill = new FrmBill(this.getFrmID());
		GEEntitys rpts = new GEEntitys(this.getFrmID());


		MapAttrs mapAttrs = new MapAttrs();
		Attrs attrs = new Attrs();
		mapAttrs.Retrieve(MapAttrAttr.FK_MapData, this.getEnsName(), MapAttrAttr.Idx);

		for (MapAttr attr : mapAttrs.ToJavaList())
			attrs.Add(attr.getHisAttr());


		String name = "数据导出";
		String filename = frmBill.getName() + "_" + BP.DA.DataType.getCurrentDataTimeCNOfLong() + ".xls";
		String filePath = ExportDGToExcel(Search_Data(), rpts.getNewEntity(), name, attrs);

		return filePath;
	}

	public final DataTable Search_Data() throws Exception {
		DataSet ds = new DataSet();


		///#region 查询语句

		MapData md = new MapData(this.getFrmID());


		//取出来查询条件.
		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK(WebUser.getNo() + "_" + this.getFrmID() + "_SearchAttrs");
		ur.RetrieveFromDBSources();

		GEEntitys rpts = new GEEntitys(this.getFrmID());

		Attrs attrs = rpts.getNewEntity().getEnMap().getAttrs();

		QueryObject qo = new QueryObject(rpts);


		///#region 关键字字段.
		String keyWord = ur.getSearchKey();
		boolean isFirst = true; //是否第一次拼接SQL

		if (md.GetParaBoolen("IsSearchKey") && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1) {
			Attr attrPK = new Attr();
			for (Attr attr : attrs) {
				if (attr.getIsPK()) {
					attrPK = attr;
					break;
				}
			}
			int i = 0;
			String enumKey = ","; //求出枚举值外键.
			for (Attr attr : attrs) {
				switch (attr.getMyFieldType()) {
					case Enum:
						enumKey = "," + attr.getKey() + "Text,";
						break;
					case FK:
						continue;
					default:
						break;
				}

				if (attr.getMyDataType() != DataType.AppString) {
					continue;
				}

				//排除枚举值关联refText.
				if (attr.getMyFieldType() == FieldType.RefText) {
					if (enumKey.contains("," + attr.getKey() + ",") == true) {
						continue;
					}
				}

				if (attr.getKey().equals("FK_Dept")) {
					continue;
				}

				i++;
				if (i == 1) {
					isFirst = false;
					/* 第一次进来。 */
					qo.addLeftBracket();
					if (SystemConfig.getAppCenterDBVarStr().equals("@")
							|| SystemConfig.getAppCenterDBVarStr().equals(":")) {
						qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
					} else {
						qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
					}
					continue;
				}
				qo.addOr();

				if (SystemConfig.getAppCenterDBVarStr().equals("@")
						|| SystemConfig.getAppCenterDBVarStr().equals(":")) {
					qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
				} else {
					qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
				}

			}
			qo.getMyParas().Add("SKey", keyWord);
			qo.addRightBracket();
		} else if (DataType.IsNullOrEmpty(md.GetParaString("RptStringSearchKeys")) == false){
			String field = "";//字段名
			String fieldValue = "";//字段值
			int idx = 0;

			//获取查询的字段
			String[] searchFields = md.GetParaString("RptStringSearchKeys").split("[*]");
			for(String str : searchFields)
			{
				if (DataType.IsNullOrEmpty(str) == true)
					continue;

				//字段名
				String[] items  = str.split(",");
				if (items.length==2 && DataType.IsNullOrEmpty(items[0]) == true)
					continue;
				field = items[0];
				//字段名对应的字段值
				fieldValue = ur.GetParaString(field);
				if (DataType.IsNullOrEmpty(fieldValue) == true)
					continue;
				idx++;
				if (idx == 1)
				{
					isFirst = false;
					/* 第一次进来。 */
					qo.addLeftBracket();
					if (SystemConfig.getAppCenterDBVarStr().equals("@")
							|| SystemConfig.getAppCenterDBVarStr().equals(":"))
						qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr()+ field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr()+ field + "+'%'"));
					else
						qo.AddWhere(field, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
					qo.getMyParas().Add(field, fieldValue);
					continue;
				}
				qo.addAnd();

				if (SystemConfig.getAppCenterDBVarStr().equals("@")
						|| SystemConfig.getAppCenterDBVarStr().equals(":"))
					qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr()+ field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr()+ field + "+'%'"));
				else
					qo.AddWhere(field, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
				qo.getMyParas().Add(field, fieldValue);


			}
			if (idx != 0)
				qo.addRightBracket();
		}

		///#endregion 关键字段查询


		///#region 时间段的查询
		if (md.GetParaInt("DTSearchWay") != DTSearchWay.None.getValue() && DataType.IsNullOrEmpty(ur.getDTFrom()) == false) {
			String dtFrom = ur.getDTFrom();
			String dtTo = ur.getDTTo();

			//按日期查询
			if (md.GetParaInt("DTSearchWay") == DTSearchWay.ByDate.getValue()) {
				if (isFirst == false)
					qo.addAnd();
				else
					isFirst = false;
				qo.addLeftBracket();
				dtTo += " 23:59:59";
				qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
				qo.addRightBracket();
			}

			if (md.GetParaInt("DTSearchWay") == DTSearchWay.ByDateTime.getValue()) {
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = DateUtils.addDay(DateUtils.parse(dtFrom, "yyyy-MM-dd"), -1) + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1) {
					dtTo += " 24:00";
				}

				if (isFirst == false)
					qo.addAnd();
				else
					isFirst = false;
				qo.addLeftBracket();
				qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
				qo.addRightBracket();
			}
		}

		///#endregion 时间段的查询


		///#region 外键或者枚举的查询

		//获得关键字.
		AtPara ap = new AtPara(ur.getVals());
		for (String str : ap.getHisHT().keySet()) {
			String val = ap.GetValStrByKey(str);
			if (val.equals("all")) {
				continue;
			}
			if (isFirst == false)
				qo.addAnd();
			else
				isFirst = false;
			qo.addLeftBracket();


			if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
				Object typeVal = BP.Sys.Glo.GenerRealType(attrs, str, ap.GetValStrByKey(str));
				qo.AddWhere(str, typeVal);

			} else {
				qo.AddWhere(str, ap.GetValStrByKey(str));
			}

			qo.addRightBracket();
		}

		///#endregion 外键或者枚举的查询

		//#region 设置隐藏字段的过滤查询
		FrmBill frmBill = new FrmBill(this.getFrmID());
		String hidenField = frmBill.GetParaString("HidenField");

		if(DataType.IsNullOrEmpty(hidenField) == false)
		{
			hidenField = hidenField.replace("[%]", "%");
			for(String field : hidenField.split(";")){
				if (field == "")
					continue;
				if (field.split(",").length != 3)
					throw new Exception("单据" + frmBill.getName() + "的过滤设置规则错误：" + hidenField + ",请联系管理员检查");
				String[] str = field.split(",");
				if (isFirst == false)
					qo.addAnd();
				else
					isFirst = false;
				qo.addLeftBracket();

				String val = str[2].replace("WebUser.No", WebUser.getNo());
				val = val.replace("WebUser.Name", WebUser.getName());
				val = val.replace("WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
				val = val.replace("WebUser.FK_DeptName", WebUser.getFK_DeptName());
				val = val.replace("WebUser.FK_Dept", WebUser.getFK_Dept());

				//获得真实的数据类型.
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					Object valType = BP.Sys.Glo.GenerRealType(attrs,
							str[0], val);
					qo.AddWhere(str[0], str[1], valType.toString());
				}
				else
				{
					qo.AddWhere(str[0], str[1], val);
				}
				qo.addRightBracket();
				continue;
			}

		}

		//#endregion 设置隐藏字段的查询
		///#endregion 查询语句

		if (isFirst == false)
			qo.addAnd();
		qo.AddWhere("BillState", "!=", 0);

		if(SearchDataRole.forValue(md.GetParaInt("SearchDataRole")) !=SearchDataRole.SearchAll){
			if(SearchDataRole.forValue(md.GetParaInt("SearchDataRole")) == SearchDataRole.ByOnlySelf && DataType.IsNullOrEmpty(hidenField) == true
					||(md.GetParaInt("SearchDataRoleByDeptStation")==0 && DataType.IsNullOrEmpty(ap.GetValStrByKey("FK_Dept"))==true))
			{
				qo.addAnd();
				qo.AddWhere("Starter", "=", WebUser.getNo());
			}
		}
		///#endregion 查询语句
		qo.addOrderBy("OID");
		return qo.DoQueryToTable();

	}

	///#endregion  执行导出


	///#region 单据导入
	public final String ImpData_Done() throws Exception {
		HttpServletRequest request = getRequest();
		if (CommonFileUtils.getFilesSize(request, "File_Upload") == 0) {
			return "err@请选择要导入的数据信息。";
		}

		String fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!prefix.equals("xls") && !prefix.equals("xlsx")) {

			return "err@上传的文件必须是Excel文件.";
		}

		String errInfo = "";
		String ext = ".xls";
		if (fileName.contains(".xlsx")) {
			ext = ".xlsx";
		}


		//设置文件名
		String fileNewName = DateUtils.format(new Date(), "yyyyMMddHHmmss") + ext;

		//文件存放路径
		String filePath = SystemConfig.getPathOfTemp() + "/" + fileNewName;
		File file = new File(filePath);
		CommonFileUtils.upload(request, "File_Upload", file);
		//从excel里面获得数据表.
		DataTable dt = BP.DA.DBLoad.GetTableByExt(filePath);

		//删除临时文件
		(new File(filePath)).delete();

		if (dt.Rows.size() == 0) {
			return "err@无导入的数据";
		}

		//获得entity.
		FrmBill bill = new FrmBill(this.getFrmID());
		GEEntitys rpts = new GEEntitys(this.getFrmID());
		GEEntity en = new GEEntity(this.getFrmID());


		String noColName = ""; //实体列的编号名称.
		String nameColName = ""; //实体列的名字名称.

		BP.En.Map map = en.getEnMap();
		Attr attr = map.GetAttrByKey("BillNo");
		noColName = attr.getDesc();
		String codeStruct = bill.getEnMap().getCodeStruct();
		attr = map.GetAttrByKey("Title");
		nameColName = attr.getDesc();

		//定义属性.
		Attrs attrs = map.getAttrs();

		int impWay = this.GetRequestValInt("ImpWay");


		///#region 清空方式导入.
		//清空方式导入.
		int count = 0; //导入的行数
		int changeCount = 0; //更新的行数
		String successInfo = "";
		if (impWay == 0) {
			rpts.ClearTable();
			GEEntity myen = new GEEntity(this.getFrmID());

			for (DataRow dr : dt.Rows) {
				String no = dr.getValue(noColName).toString();
				String name = dr.getValue(nameColName).toString();
				myen.setOID(0);

				if (!DataType.IsNullOrEmpty(codeStruct)) {
					if(DataType.IsNullOrEmpty(no)){
						no =myen.GenerNewNoByKey(noColName);
					}else{
						no = StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
					}

				}

				if(DataType.IsNullOrEmpty((no)) == false){
					myen.SetValByKey("BillNo", no);
					if (myen.Retrieve("BillNo", no) == 1) {
						errInfo += "err@编号[" + no + "][" + name + "]重复.";
						continue;
					}
				}
				//给实体赋值
				errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 0);
				count++;
				successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
			}
		}


		///#endregion 清空方式导入.


		///#region 更新方式导入
		if (impWay == 1 || impWay == 2) {
			for (DataRow dr : dt.Rows) {
				String no = dr.getValue(noColName).toString();
				String name = dr.getValue(nameColName).toString();
				//判断是否是自增序列，序列的格式
				if (!DataType.IsNullOrEmpty(codeStruct)) {
					no = StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
				}
				GEEntity myen = rpts.getNewEntity() instanceof GEEntity ? (GEEntity) rpts.getNewEntity() : null;
				myen.SetValByKey("BillNo", no);
				if (myen.Retrieve("BillNo", no) == 1) {
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

		///#endregion

		return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
	}

	private String SetEntityAttrVal(String no, DataRow dr, Attrs attrs, GEEntity en, DataTable dt, int saveType) throws Exception {
		if (saveType == 0) {
			String OID = MyDict_CreateBlankDictID();
			en.setOID(Long.parseLong(OID));
			en.RetrieveFromDBSources();
		}

		String errInfo = "";
		//按照属性赋值.
		for (Attr item : attrs) {
			if (item.getKey().equals("BillNo")) {
				en.SetValByKey(item.getKey(), no);
				continue;
			}
			if (item.getKey().equals("Title")) {
				en.SetValByKey(item.getKey(), dr.getValue(item.getDesc()).toString());
				continue;
			}

			if (dt.Columns.contains(item.getDesc()) == false) {
				continue;
			}

			//枚举处理.
			if (item.getMyFieldType() == FieldType.Enum) {
				String val = dr.getValue(item.getDesc()).toString();

				SysEnum se = new SysEnum();
				int i = se.Retrieve(SysEnumAttr.EnumKey, item.getUIBindKey(), SysEnumAttr.Lab, val);

				if (i == 0) {
					errInfo += "err@枚举[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
					continue;
				}

				en.SetValByKey(item.getKey(), se.getIntKey());
				continue;
			}

			//外键处理.
			if (item.getMyFieldType() == FieldType.FK) {
				String val = dr.getValue(item.getDesc()).toString();
				Entity attrEn = item.getHisFKEn();
				int i = attrEn.Retrieve("Name", val);
				if (i == 0) {
					errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
					continue;
				}

				if (i != 1) {
					errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]重复..";
					continue;
				}

				//把编号值给他.
				en.SetValByKey(item.getKey(), attrEn.GetValByKey("No"));
				continue;
			}

			//boolen类型的处理..
			if (item.getMyDataType() == DataType.AppBoolean) {
				String val = dr.getValue(item.getDesc()).toString();
				if (val.equals("是") || val.equals("有")) {
					en.SetValByKey(item.getKey(), 1);
				} else {
					en.SetValByKey(item.getKey(), 0);
				}
				continue;
			}

			String myval = dr.getValue(item.getDesc()).toString();
			en.SetValByKey(item.getKey(), myval);
		}

		en.SetValByKey("BillState", BillState.Editing.getValue());
		en.Update();

		return errInfo;
	}


	///#endregion


	///#region 执行父类的重写方法.

	/**
	 * 默认执行的方法
	 *
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		switch (this.getDoType()) {
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + CommonUtils.getRequest().getRequestURI());
	}

	///#endregion 执行父类的重写方法.


	///#region 获得demo信息.
	public final String MethodDocDemoJS_Init() throws Exception {
		MethodFunc func = new MethodFunc(this.getMyPK());
		return func.getMethodDoc_JavaScript_Demo();
	}

	public final String MethodDocDemoSQL_Init() throws Exception {
		MethodFunc func = new MethodFunc(this.getMyPK());
		return func.getMethodDoc_SQL_Demo();
	}


}