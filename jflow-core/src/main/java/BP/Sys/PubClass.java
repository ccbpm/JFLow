package BP.Sys;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DBCheckLevel;
import BP.DA.DBType;
import BP.DA.DBUrlType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.Difference.ContextHolderUtils;
import BP.Difference.SystemConfig;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.GroupField;
import BP.Sys.MapAttr;
import BP.Sys.MapData;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Tools.StringUtils;

/**
 * PageBase 的摘要说明。
 */
public class PubClass {

	private static final String Color = null;

	public static String ToHtmlColor(String colorName) throws Exception {
		try {
			if (colorName.startsWith("#")) {
				colorName = colorName.replace("#", "");
				// update by dgq 六位颜色不需要转换
				if (colorName.length() == 6 || colorName.length() == 3) {
					return "#" + colorName;
				} else if (colorName.length() == 8) {
					return "#" + colorName.substring(2, 8);
				} else {
					return "#" + colorName;
				}
			} else {
				return colorName;
			}
		} catch (java.lang.Exception e) {
			return "black";
		}
	}

	/**
	 * 处理字段
	 * 
	 * @param fd
	 * @return
	 */
	public static String DealToFieldOrTableNames(String fd) {
		String keys = "~!@#$%^&*()+{}|:<>?`=[];,./～！＠＃￥％……＆×（）——＋｛｝｜：“《》？｀－＝［］；＇，．／";
		char[] cc = keys.toCharArray();
		for (char c : cc) {
			fd = fd.replace((new Character(c)).toString(), "");
		}
		String s = fd.substring(0, 1);
		try {
			int a = Integer.parseInt(s);
			fd = "F" + fd;
		} catch (java.lang.Exception e) {
		}
		return fd;
	}

	private static String _KeyFields = null;

	public static String getKeyFields() {
		if (_KeyFields == null) {
			_KeyFields = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfData() + "/Sys/FieldKeys.txt");
		}
		return _KeyFields;
	}

	public static boolean IsNum(String str) {
		boolean strResult;
		String cn_Regex = "^[\\u4e00-\\u9fa5]+$";

		Pattern pattern = Pattern.compile(cn_Regex);
		Matcher matcher = pattern.matcher(str);

		if (matcher.find()) {
			strResult = true;
		} else {
			strResult = false;
		}
		return strResult;
	}

	public static boolean IsCN(String str) {
		boolean strResult;
		String cn_Regex = "^[\\u4e00-\\u9fa5]+$";
		Pattern pattern = Pattern.compile(cn_Regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			strResult = true;
		} else {
			strResult = false;
		}
		return strResult;
	}

	public static boolean IsImg(String ext) {
		ext = ext.replace(".", "").toLowerCase();
		if (ext.equals("gif")) {
			return true;
		} else if (ext.equals("jpg")) {
			return true;
		} else if (ext.equals("bmp")) {
			return true;
		} else if (ext.equals("png")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按照比例数小
	 * 
	 * @param ObjH
	 *            目标高度
	 * @param factH
	 *            实际高度
	 * @param factW
	 *            实际宽度
	 * @return 目标宽度
	 */
	public static int GenerImgW_del(int ObjH, int factH, int factW, int isZeroAsWith) {
		if (factH == 0 || factW == 0) {
			return isZeroAsWith;
		}

		int d = ObjH / (factH * factW);

		try {
			return d;
		} catch (RuntimeException ex) {
			throw new RuntimeException(d + ex.getMessage());
		}
	}

	/**
	 * 按照比例数小
	 * 
	 * @param ObjH
	 *            目标高度
	 * @param factH
	 *            实际高度
	 * @param factW
	 *            实际宽度
	 * @return 目标宽度
	 */
	public static int GenerImgH(int ObjW, int factH, int factW, int isZeroAsWith) {
		if (factH == 0 || factW == 0) {
			return isZeroAsWith;
		}

		int d = ObjW / (factW * factH);

		try {
			return d;
		} catch (RuntimeException ex) {
			throw new RuntimeException(d + ex.getMessage());
		}
	}

	public static String GenerLabelStr(String title) {
		String path = BP.Sys.Glo.getRequest().getRemoteHost();
		if (path.equals("") || path.equals("/")) {
			path = "..";
		}

		String str = "";
		str += "<TABLE  height='100%' cellPadding='0' background='" + path + "/Images/DG_bgright.gif'>";
		str += "<TBODY>";
		str += "<TR   >";
		str += "<TD  >";
		str += "<IMG src='" + path + "/Images/DG_Title_Left.gif' border='0'></TD>";
		str += "<TD style='font-size:14px'  vAlign='bottom' noWrap background='" + path
				+ "/Images/DG_Title_BG.gif'>&nbsp;";
		str += " &nbsp;<b>" + title + "</b>&nbsp;&nbsp;";
		str += "</TD>";
		str += "<TD>";
		str += "<IMG src='" + path + "/Images/DG_Title_Right.gif' border='0'></TD>";
		str += "</TR>";
		str += "</TBODY>";
		str += "</TABLE>";
		return str;
		// return str;
	}

	/**
	 * 将汉字转换成拼音
	 * 
	 * @param str
	 *            要转换的汉字
	 * @return 返回的拼音
	 */
	public final String Chs2Pinyin(String str) {
		return BP.Tools.chs2py.convert(str);
	}

	public static String GenerTablePage(DataTable dt, String title) {

		String str = "<Table id='tb' class=Table >";

		str += "<caption>" + title + "</caption>";

		// 标题
		str += "<TR>";
		for (DataColumn dc : dt.Columns) {
			str += "<TD class='DGCellOfHeader" + BP.Web.WebUser.getStyle() + "' nowrap >" + dc.ColumnName + "</TD>";
		}
		str += "</TR>";

		// 内容
		for (DataRow dr : dt.Rows) {
			str += "<TR>";

			for (DataColumn dc : dt.Columns) {
				str += "<TD nowrap=true >&nbsp;" + dr.getValue(dc.ColumnName) + "</TD>";
			}
			str += "</TR>";
		}
		str += "</Table>";
		return str;
	}

	/**
	 * 产生临时文件名称
	 * 
	 * @param hz
	 * @return
	 * @throws Exception
	 */
	public static String GenerTempFileName(String hz) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddhhmmss");
		return BP.Web.WebUser.getNo() + formatter.format(new Date()) + "." + hz;
	}

	public static void DeleteTempFiles() {
		File file = new File(SystemConfig.getPathOfTemp());
		File[] strs = file.listFiles();

		for (File s : strs) {
			s.delete();
		}
	}

	/**
	 * 重新建立索引
	 * @throws Exception 
	 */
	public static void ReCreateIndex() throws Exception {
		ArrayList<Entity> als = ClassFactory.GetObjects("BP.En.Entity");
		String sql = "";
		for (Entity en : als) {
			if (en.getEnMap().getEnType() == EnType.View) {
				continue;
			}
			sql += "IF EXISTS( SELECT name  FROM  sysobjects WHERE  name='" + en.getEnMap().getPhysicsTable()
					+ "') <BR> DROP TABLE " + en.getEnMap().getPhysicsTable() + "<BR>";
			sql += "CREATE TABLE " + en.getEnMap().getPhysicsTable() + " ( <BR>";
			sql += "";
		}

	}

	
	/**
	 * 检查所有的物理表
	 */
	public static void CheckAllPTable(String nameS) {
		ArrayList<Entities> al = BP.En.ClassFactory.GetObjects("BP.En.Entities");
		for (Entities ens : al) {
			if (!ens.toString().contains(nameS)) {
				continue;
			}
			try {
				Entity en =  ens.getNewEntity();
				en.CheckPhysicsTable();
			} catch (java.lang.Exception e) {

			}

		}

	}
	/**
	 * 获取datatable.
	 *
	 * @param uiBindKey
	 * @return
	 * @throws Exception
	 */
	public static DataTable GetDataTableByUIBineKey(String uiBindKey) throws Exception{
		return GetDataTableByUIBineKey(uiBindKey,null);
	}
	/**
	 * 获取datatable.
	 * 
	 * @param uiBindKey
	 * @return
	 * @throws Exception
	 */
	public static DataTable GetDataTableByUIBineKey(String uiBindKey,Hashtable ht) throws Exception {

		DataTable dt = new DataTable();
		if (uiBindKey.contains(".")) {
			Entities ens = BP.En.ClassFactory.GetEns(uiBindKey);
			if (ens == null)
				ens = BP.En.ClassFactory.GetEns(uiBindKey);

			if (ens == null)
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			if (ens == null)
				throw new Exception("类名错误:" + uiBindKey + ",不能转化成ens.");

			ens.RetrieveAllFromDBSource();
			dt = ens.ToDataTableField(uiBindKey);
			return dt;
		}

		// added by liuxc,2017-09-11,增加动态SQL查询类型的处理，此种类型没有固定的数据表或视图
		SFTable sf = new SFTable();
		sf.setNo(uiBindKey);
		if (sf.RetrieveFromDBSources() != 0) {
			if (sf.getSrcType() == SrcType.Handler || sf.getSrcType() == SrcType.JQuery)
				return null;
			dt = sf.GenerHisDataTable(ht);
		}
		if (dt == null)
			dt = new DataTable();

		// #region 把列名做成标准的.
		for (DataColumn col : dt.Columns) {
			String colName = col.ColumnName.toLowerCase();

			if (colName.equals("no") == true)
				col.ColumnName = "No";

			if (colName.equals("name") == true)
				col.ColumnName = "Name";

			if (colName.equals("parentno") == true)
				col.ColumnName = "ParentNo";

		}
		// #endregion 把列名做成标准的.

		dt.TableName = uiBindKey;
		return dt;
	}

	/**
	 * 获取数据源
	 * 
	 * @param uiBindKey
	 *            绑定的外键或者枚举
	 * @return
	 * @throws Exception
	 */
	public static DataTable GetDataTableByUIBineKeyForCCFormDesigner(String uiBindKey) throws Exception {
		int topNum = 40;

		DataTable dt = new DataTable();
		if (uiBindKey.contains(".")) {
			Entities ens = BP.En.ClassFactory.GetEns(uiBindKey);
			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}

			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}
			if (ens == null) {
				throw new RuntimeException("类名错误:" + uiBindKey + ",不能转化成ens.");
			}

			BP.En.QueryObject qo = new QueryObject(ens);
			return qo.DoQueryToTable(topNum);
		} else {
			String sql = "";

			switch (SystemConfig.getAppCenterDBType()) {
			case Oracle:
				sql = "SELECT No,Name FROM " + uiBindKey + " where rowNum <= " + topNum;
				break;
			case MSSQL:
				sql = "SELECT top " + topNum + " No,Name FROM " + uiBindKey;
				break;
			default:
				sql = "SELECT  No,Name FROM " + uiBindKey;
				break;
			}
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = uiBindKey;
			return dt;
		}
	}

	// 系统调度
	public static String GenerDBOfOreacle() throws Exception {
		ArrayList<Entity> als = ClassFactory.GetObjects("BP.En.Entity");
		String sql = "";
		for (Entity en : als) {
			sql += "IF EXISTS( SELECT name  FROM  sysobjects WHERE  name='" + en.getEnMap().getPhysicsTable()
					+ "') <BR> DROP TABLE " + en.getEnMap().getPhysicsTable() + "<BR>";
			sql += "CREATE TABLE " + en.getEnMap().getPhysicsTable() + " ( <BR>";
			sql += "";
		}
		return sql;
	}

	public static String DBRpt(DBCheckLevel level) throws Exception {
		// 取出全部的实体
		ArrayList<Entities> als = ClassFactory.GetObjects("BP.En.Entities");
		String msg = "";
		for (Entities ens : als) {
			try {
				msg += DBRpt1(level, ens);
			} catch (RuntimeException ex) {
				msg += "<hr>" + ens.toString() + "体检失败:" + ex.getMessage();
			}
		}

		MapDatas mds = new MapDatas();
		mds.RetrieveAllFromDBSource();
		for (Object md : mds) {
			try {
				try {
					((MapData) md).getHisGEEn().CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
				PubClass.AddComment(((MapData) md).getHisGEEn());
			} catch (RuntimeException ex) {
				msg += "<hr>" + ((MapData) md).getNo() + "体检失败:" + ex.getMessage();
			}
		}

		MapDtls dtls = new MapDtls();
		dtls.RetrieveAllFromDBSource();
		for (Object dtl : dtls) {
			try {
				try {
					((MapDtl) dtl).getHisGEDtl().CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
				PubClass.AddComment(((MapDtl) dtl).getHisGEDtl());
			} catch (RuntimeException ex) {
				msg += "<hr>" + ((MapDtl) dtl).getNo() + "体检失败:" + ex.getMessage();
			}
		}

		// 检查处理必要的基础数据 Pub_Day .
		String sql = "";
		String sqls = "";
		sql = "SELECT count(*) Num FROM Pub_Day";
		try {
			String d = "";
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 1; i <= 31; i++) {

					if (i <= 9)
						d = PubClass.addZeroForNum(String.valueOf(i), 2);
					else
						d = String.valueOf(i);
					sqls += "@INSERT INTO Pub_Day(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		} catch (java.lang.Exception e) {
		}

		sql = "SELECT count(*) Num FROM Pub_YF";
		try {
			String d1 = "";
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 1; i <= 12; i++) {
					if (i <= 9)
						d1 = PubClass.addZeroForNum(String.valueOf(i), 2);
					else
						d1 = String.valueOf(i);
					sqls += "@INSERT INTO Pub_YF(No,Name)VALUES('" + d1.toString() + "','" + d1.toString() + "')";
				}
			}
		} catch (java.lang.Exception e2) {
		}

		sql = "SELECT count(*) Num FROM Pub_ND";
		try {
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 2010; i < 2015; i++) {
					String d = (new Integer(i)).toString();
					sqls += "@INSERT INTO Pub_ND(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		} catch (java.lang.Exception e3) {

		}
		sql = "SELECT count(*) Num FROM Pub_NY";
		try {
			String d2 = "";
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 2010; i < 2015; i++) {

					for (int yf = 1; yf <= 12; yf++) {
						if (i <= 9)
							d2 = (new Integer(i)).toString() + "-" + PubClass.addZeroForNum(String.valueOf(i), 2);
						else
							d2 = (new Integer(i)).toString() + "-" + String.valueOf(i);

						sqls += "@INSERT INTO Pub_NY(No,Name)VALUES('" + d2 + "','" + d2 + "')";
					}
				}
			}
		} catch (java.lang.Exception e4) {
		}

		try {
			DBAccess.RunSQLs(sqls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 检查处理必要的基础数据。
		return msg;
	}

	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	/**
	 * 为表增加注释
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String AddComment() throws Exception {
		// 取出全部的实体
		ArrayList<Entities> als = ClassFactory.GetObjects("BP.En.Entities");
		String msg = "";
		Entity en = null;
		for (Entities ens : als) {
			if (ens == null)
				continue;
			try {
				en = ens.getNewEntity();
				if (en.getEnMap().getEnType() == EnType.View || en.getEnMap().getEnType() == EnType.ThirdPartApp) {
					continue;
				}
			} catch (java.lang.Exception e) {
				continue;
			}
			// 增加en为空的判断
			if (en.getEnMap().getPhysicsTable() != null && !en.getEnMap().getPhysicsTable().equals("null")) {
				msg += AddComment(en);
			}

		}
		return msg;
	}

	public static String AddComment(Entity en) throws Exception {

		if (DBAccess.IsExitsObject(en.getEnMap().getPhysicsTable()) == false)
			return "表不存在.";

		try {
			switch (SystemConfig.getAppCenterDBType()) {
			case Oracle:
				AddCommentForTable_Ora(en);
				break;
			case MySQL:
				AddCommentForTable_MySql(en);
				break;
			case DM:
				AddCommentForTable_Ora(en);
				break;
			default:
				AddCommentForTable_MS(en);
				break;
			}
			return "";
		} catch (RuntimeException ex) {
			return "<hr>" + en.toString() + "体检失败:" + ex.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void AddCommentForTable_Ora(Entity en) throws Exception {

		en.RunSQL("comment on table " + en.getEnMap().getPhysicsTable() + " IS '" + en.getEnDesc() + "'");
		SysEnums ses = new SysEnums();
		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyFieldType()) {
			case PK:
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '"
						+ attr.getDesc() + " - 主键'");
				break;
			case Normal:
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '"
						+ attr.getDesc() + "'");
				break;
			case Enum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '"
						+ attr.getDesc() + ",枚举类型:" + ses.ToDesc() + "'");
				break;
			case PKEnum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '"
						+ attr.getDesc() + ", 主键:枚举类型:" + ses.ToDesc() + "'");
				break;
			case FK:
				Entity myen = attr.getHisFKEn(); 
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '"
						+ attr.getDesc() + ", 外键:对应物理表:" + myen.getEnMap().getPhysicsTable() + ",表描述:"
						+ myen.getEnDesc() + "'");
				break;
			case PKFK:
				Entity myen1 = attr.getHisFKEn(); 
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '"
						+ attr.getDesc() + ", 主外键:对应物理表:" + myen1.getEnMap().getPhysicsTable() + ",表描述:"
						+ myen1.getEnDesc() + "'");
				break;
			default:
				break;
			}
		}
	}

	public static void AddCommentForTable_MySql(Entity en) throws Exception {
		String database = SystemConfig.getAppCenterDBDatabase();
		if(StringUtils.isEmpty(en.getEnMap().getPhysicsTable())) return;
		en.RunSQL("alter table " + database + "." + en.getEnMap().getPhysicsTable() + " comment = '" + en.getEnDesc()
				+ "'");
		// 获取当前实体对应表的所有字段结构信息
		DataTable cols = DBAccess.RunSQLReturnTable(
				"select column_name,column_default,is_nullable,character_set_name,column_type from information_schema.columns where table_schema = '"
						+ database + "' and table_name='" + en.getEnMap().getPhysicsTable() + "'");
		SysEnums ses = new SysEnums();
		String sql = "";
		DataRow row = null;

		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			row = cols.select(String.format("column_name='%1$s'", attr.getField())).get(0);
			sql = String.format("ALTER TABLE %1$s.%2$s CHANGE COLUMN %3$s %3$s %4$s%5$s%6$s%7$s COMMENT ", database,
					en.getEnMap().getPhysicsTable(), attr.getField(),
					row.getValue("column_type").toString().toUpperCase(),
					row.getValue("character_set_name").equals("utf8") ? " CHARACTER SET 'utf8'" : "",
					row.getValue("is_nullable").equals("YES") ? " NULL" : " NOT NULL",
					row.getValue("column_default").equals("NULL") ? " DEFAULT NULL" : (row.getValue("").equals("") ? ""
							: " DEFAULT " + row.getValue((""))));

			switch (attr.getMyFieldType()) {
			case PK:
				en.RunSQL(sql + String.format("'%1$s - 主键'", attr.getDesc()));
				break;
			case Normal:
				en.RunSQL(sql + String.format("'%1$s'", attr.getDesc()));
				break;
			case Enum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				en.RunSQL(sql + String.format("'%1$s,枚举类型:%2$s'", attr.getDesc(), ses.ToDesc()));
				break;
			case PKEnum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				en.RunSQL(sql + String.format("'%1$s,主键:枚举类型:%2$s'", attr.getDesc(), ses.ToDesc()));
				break;
			case FK:
				Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				en.RunSQL(sql + String.format("'%1$s,外键:对应物理表:%2$s,表描述:%3$s'", attr.getDesc(),
						myen.getEnMap().getPhysicsTable(), myen.getEnDesc()));
				break;
			case PKFK:
				Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				en.RunSQL(sql + String.format("'%1$s,主外键:对应物理表:%2$s,表描述:%3$s'", attr.getDesc(),
						myen1.getEnMap().getPhysicsTable(), myen1.getEnDesc()));
				break;
			default:
				break;
			}
		}
	}

	private static void AddColNote(Entity en, String table, String col, String note) {
		return;
		
	}

	/**
	 * 为表增加解释
	 * 
	 * @param en
	 * @throws Exception
	 */
	public static void AddCommentForTable_MS(Entity en) throws Exception {

		if (en.getEnMap().getEnType() == EnType.View || en.getEnMap().getEnType() == EnType.ThirdPartApp) {
			return;
		}
		try {
			String sql = "execute  sp_dropextendedproperty 'MS_Description','user',dbo,'table','"
					+ en.getEnMap().getPhysicsTable() + "'";
			en.RunSQL(sql);
		} catch (RuntimeException ex) {
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String sql = "execute  sp_addextendedproperty 'MS_Description', '" + en.getEnDesc()
					+ "', 'user', dbo, 'table', '" + en.getEnMap().getPhysicsTable() + "'";
			en.RunSQL(sql);
		} catch (RuntimeException ex) {

		} catch (Exception e) {
			e.printStackTrace();
		}

		SysEnums ses = new SysEnums();
		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getKey().equals(attr.getDesc())) {
				continue;
			}

			switch (attr.getMyFieldType()) {
			case Normal:
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc());
				
				break;
			case Enum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(),
						attr.getDesc() + ",枚举类型:" + ses.ToDesc());
				break;
			case PKEnum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(),
						attr.getDesc() + ",主键:枚举类型:" + ses.ToDesc());
				
				break;
			case FK:
				Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ", 外键:对应物理表:"
						+ myen.getEnMap().getPhysicsTable() + ",表描述:" + myen.getEnDesc());
				
				break;
			case PKFK:
				Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ", 主外键:对应物理表:"
						+ myen1.getEnMap().getPhysicsTable() + ",表描述:" + myen1.getEnDesc());
				
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 产程系统报表，如果出现问题，就写入日志里面。
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String DBRpt1(DBCheckLevel level, Entities ens) throws Exception {
		Entity en = ens.getNewEntity();
		if (en.getEnMap().getEnDBUrl().getDBUrlType() != DBUrlType.AppCenterDSN) {
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.ThirdPartApp) {
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.View) {
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.Ext) {
			return null;
		}

		// 检测物理表的字段。
		try {
			en.CheckPhysicsTable();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		PubClass.AddComment(en);

		String msg = "";
		// if (level == DBLevel.High)
		// {
		// try
		// {
		// DBAccess.RunSQL("update pub_emp set AuthorizedAgent='1' WHERE
		// AuthorizedAgent='0' ");
		// }
		// catch
		// {
		// }
		// }
		String table = en.getEnMap().getPhysicsTable();
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		if (fkAttrs.size() == 0) {
			return msg;
		}
		int num = 0;
		String sql;
		// string msg="";
		for (Attr attr : fkAttrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			String enMsg = "";
			try {
				// 更新他们，去掉左右空格，因为外键不能包含左右空格。
				if (level == DBCheckLevel.Middle || level == DBCheckLevel.High) {
					// 如果是高中级别,就去掉左右空格
					if (attr.getMyDataType() == DataType.AppString) {
						DBAccess.RunSQL("UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + attr.getField()
								+ " = rtrim( ltrim(" + attr.getField() + ") )");
					}
				}

				// 处理关联表的情况.
				Entities refEns = attr.getHisFKEns(); // ClassFactory.GetEns(attr.UIBindKey);
				Entity refEn = refEns.getNewEntity();

				// 取出关联的表。
				String reftable = refEn.getEnMap().getPhysicsTable();
				// sql="SELECT COUNT(*) FROM "+en.getEnMap().getPhysicsTable()+"
				// WHERE "+attr.Key+" is null or len("+attr.Key+") < 1 ";
				// 判断外键表是否存在。

				sql = "SELECT COUNT(*) FROM  sysobjects  WHERE  name = '" + reftable + "'";
				// num=DA.DBAccess.RunSQLReturnValInt(sql,0);
				if (!DBAccess.IsExitsObject(reftable)) {
					// 报告错误信息
					enMsg += "<br>@检查实体：" + en.getEnDesc() + ",字段 " + attr.getKey() + " , 字段描述:" + attr.getDesc()
							+ " , 外键物理表:" + reftable + "不存在:" + sql;
				} else {
					Attr attrRefKey = refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()); // 去掉主键的左右
																								// 空格．
					if (attrRefKey.getMyDataType() == DataType.AppString) {
						if (level == DBCheckLevel.Middle || level == DBCheckLevel.High) {
							// 如果是高中级别,就去掉左右空格
							DBAccess.RunSQL("UPDATE " + reftable + " SET " + attrRefKey.getField() + " = rtrim( ltrim("
									+ attrRefKey.getField() + ") )");
						}
					}

					Attr attrRefText = refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyText()); // 去掉主键
																								// Text
																								// 的左右
																								// 空格．

					if (level == DBCheckLevel.Middle || level == DBCheckLevel.High) {
						// 如果是高中级别,就去掉左右空格
						DBAccess.RunSQL("UPDATE " + reftable + " SET " + attrRefText.getField() + " = rtrim( ltrim("
								+ attrRefText.getField() + ") )");
					}

				}
				// 外键的实体是否为空
				switch (SystemConfig.getAppCenterDBType()) {
				case Oracle:
					sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField()
							+ " is null or length(" + attr.getField() + ") < 1 ";
					break;
				default:
					sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField()
							+ " is null or len(" + attr.getField() + ") < 1 ";
					break;
				}

				num = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				if (num == 0) {
				} else {
					enMsg += "<br>@检查实体：" + en.getEnDesc() + ",物理表:" + en.getEnMap().getPhysicsTable() + "出现"
							+ attr.getKey() + "," + attr.getDesc() + "不正确,共有[" + num + "]行记录没有数据。" + sql;
				}
				// 是否能够对应到外键
				// 是否能够对应到外键。
				sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField()
						+ " NOT IN ( SELECT " + refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()).getField()
						+ " FROM " + reftable + "	 ) ";
				num = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				if (num == 0) {
				} else {
					// 如果是高中级别.
					String delsql = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField()
							+ " NOT IN ( SELECT " + refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()).getField()
							+ " FROM " + reftable + "	 ) ";
					// int i =DBAccess.RunSQL(delsql);
					enMsg += "<br>@" + en.getEnDesc() + ",物理表:" + en.getEnMap().getPhysicsTable() + "出现" + attr.getKey()
							+ "," + attr.getDesc() + "不正确,共有[" + num + "]行记录没有关联到数据，请检查物理表与外键表。" + sql
							+ "如果您想删除这些对应不上的数据请运行如下SQL: " + delsql + " 请慎重执行.";
				}

				// 判断 主键
				// DBAccess.IsExits("");
			} catch (RuntimeException ex) {
				enMsg += "<br>@" + ex.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!enMsg.equals("")) {
				msg += "<BR><b>-- 检查[" + en.getEnDesc() + "," + en.getEnMap().getPhysicsTable() + "]出现如下问题,类名称:"
						+ en.toString() + "</b>";
				msg += enMsg;
			}
		}
		return msg;
	}

	// 转化格式 chen
	/**
	 * 将某控件中的数据转化为Excel文件
	 * 
	 * @param ctl
	 */

	// public static void ToExcel(System.Web.UI.Control ctl, String filename)
	// {
	// HttpContext.Current.Response.Charset ="GB2312";
	// HttpContext.Current.Response.AppendHeader("Content-Disposition","attachment;filename="+
	// filename +".xls");
	// HttpContext.Current.Response.ContentEncoding
	// =System.Text.Encoding.GetEncoding("GB2312");
	// HttpContext.Current.Response.ContentType ="application/ms-excel";
	// //"application/ms-excel";
	// //image/JPEG;text/HTML;image/GIF;application/ms-msword
	// ctl.Page.EnableViewState =false;
	// System.IO.StringWriter tw = new System.IO.StringWriter();
	// System.Web.UI.HtmlTextWriter hw = new System.Web.UI.HtmlTextWriter (tw);
	// ctl.RenderControl(hw);
	// HttpContext.Current.Response.Write(tw.toString());
	// HttpContext.Current.Response.End();
	// }
	/**
	 * 将某控件中的数据转化为Word文件
	 * 
	 * @param ctl
	 * @throws IOException
	 */
	// public static void ToWord(System.Web.UI.Control ctl, String filename)
	// {
	// filename = HttpUtility.UrlEncode(filename);
	// HttpContext.Current.Response.Charset ="GB2312";
	// HttpContext.Current.Response.AppendHeader("Content-Disposition","attachment;filename="+
	// filename +".doc");
	// HttpContext.Current.Response.ContentEncoding
	// =System.Text.Encoding.GetEncoding("GB2312");
	// HttpContext.Current.Response.ContentType ="application/ms-msword";
	// //image/JPEG;text/HTML;image/GIF;application/ms-excel
	// ctl.Page.EnableViewState =false;
	// System.IO.StringWriter tw = new System.IO.StringWriter();
	// System.Web.UI.HtmlTextWriter hw = new System.Web.UI.HtmlTextWriter (tw);
	// ctl.RenderControl(hw);
	// HttpContext.Current.Response.Write(tw.toString());
	// }

	public static void OpenExcel(String filepath, String tempName) throws IOException {
		/*
		 * tempName = HttpUtility.UrlEncode(tempName);
		 * HttpContext.Current.Response.Charset = "GB2312";
		 * HttpContext.Current.Response.AppendHeader("Content-Disposition",
		 * "attachment;filename=" + tempName);
		 * HttpContext.Current.Response.ContentEncoding =
		 * System.Text.Encoding.GetEncoding("GB2312");
		 * HttpContext.Current.Response.ContentType = "application/ms-excel";
		 * HttpContext.Current.Response.WriteFile(filepath);
		 * HttpContext.Current.Response.End();
		 * HttpContext.Current.Response.Close();
		 */

		// 设置文件MIME类型
		HttpServletResponse response = ContextHolderUtils.getResponse();
		HttpServletRequest request = ContextHolderUtils.getRequest();

		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition",
				"inline;filename=" + new String(tempName.getBytes("UTF-8"), "ISO-8859-1"));

		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		try {
			File file = new File(filepath);
			// System.out.println("文件路径:"+filepath);
			if (!file.exists()) {
				throw new Exception("找不到指定文件：" + filepath);
			}
			// 开始下载
			fis = new FileInputStream(file);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] b = new byte[8192];
			int data = 0;
			while ((data = fis.read(b)) != -1) {
				bos.write(b, 0, data);
			}
			// 刷新流
			bos.flush();
		} catch (Exception e) {
		} finally {
			if (bos != null)
				bos.close();
			if (fis != null)
				fis.close();
		}
	}

	public static String toUtf8String(String s) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					// exceptionUtil.error("将文件名中的汉字转为UTF8编码的串时错误，输入的字符串为：" +
					// s);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	// 不同浏览器编码问题 add by qin 15.10.20
	public static String toUtf8String(HttpServletRequest request, String s) {
		String agent = request.getHeader("User-Agent");
		try {
			boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
			if (isFireFox) {
				s = new String(s.getBytes("UTF-8"), "ISO8859-1");
			} else {
				s = toUtf8String(s);
				if ((agent != null && agent.indexOf("MSIE") != -1)) {
					// see http://support.microsoft.com/default.aspx?kbid=816868
					if (s.length() > 150) {
						// 根据request的locale 得出可能的编码
						s = new String(s.getBytes("UTF-8"), "ISO8859-1");
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void DownloadFile(String filepath, String tempName) throws Exception {

		// 设置文件MIME类型
		HttpServletResponse response = ContextHolderUtils.getResponse();
		HttpServletRequest request = ContextHolderUtils.getRequest();

		tempName = toUtf8String(request, tempName);

		File myfile = new File(filepath);
		if (myfile.exists() == false)
			throw new Exception("err@文件名不存在:" + filepath);

		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + tempName);
		response.setHeader("Connection", "close");
		// 读取目标文件，通过response将目标文件写到客户端
		// 读取文件
		InputStream in = new FileInputStream(myfile);
		OutputStream out = response.getOutputStream();
		// 写文件
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		in.close();
		out.close();
	}

	public static void DownloadFileByBuffer(String filepath, String tempName) throws IOException {

		// 设置文件MIME类型
		HttpServletResponse response = ContextHolderUtils.getResponse();
		HttpServletRequest request = ContextHolderUtils.getRequest();

		tempName = toUtf8String(request, tempName);

		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + tempName);
		response.setHeader("Connection", "close");
		// 读取目标文件，通过response将目标文件写到客户端
		// 读取文件
		InputStream in = new FileInputStream(new File(filepath));
		BufferedInputStream bis = new BufferedInputStream(in);
		OutputStream out = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(out);
		// 写文件
		int b;
		while ((b = bis.read()) != -1) {
			bos.write(b);
			bos.flush();
		}
		in.close();
		bis.close();
		out.close();
		bos.close();
	}

	/// <summary>
	/// 从别的网站服务器上下载文件
	/// </summary>
	/// <param name="filepath"></param>
	/// <param name="tempName"></param>
	public static void DownloadHttpFile(String filepath, String tempName, HttpServletResponse response)
			throws Exception {
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + tempName);
		// 打开网络连接
		String filePth = filepath.replace("\\", "/").trim();
		if (filepath.indexOf("/") == 0) {
			filepath = filepath.substring(filepath.length() - 1);
		}
		if (!SystemConfig.getAttachWebSite().trim().endsWith("/")) {
			filepath = SystemConfig.getAttachWebSite().trim() + "/" + filepath;
		} else {
			filepath = SystemConfig.getAttachWebSite().trim() + filepath;
		}
		FileInputStream fis = new FileInputStream(filepath);
		byte[] data = new byte[8192];
		ServletOutputStream os = response.getOutputStream();
		int i;
		while ((i = fis.read(data, 0, 8192)) != -1) {
			os.write(data, 0, i);
		}
		os.flush();
		fis.close();
		os.close();

	}

	public static void OpenWordDocV2(String filepath, String tempName) throws IOException {
		// 设置文件MIME类型
		HttpServletResponse response = ContextHolderUtils.getResponse();
		HttpServletRequest request = ContextHolderUtils.getRequest();

		response.setContentType("application/msword;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition",
				"inline;filename=" + new String(filepath.getBytes("GB2312"), "ISO-8859-1"));

		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				throw new Exception("找不到指定文件：" + filepath);
			}
			// 开始下载
			fis = new FileInputStream(file);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] b = new byte[8192];
			int data = 0;
			while ((data = fis.read(b)) != -1) {
				bos.write(b, 0, data);
			}
			// 刷新流
			bos.flush();
		} catch (Exception e) {
		} finally {
			if (bos != null)
				bos.close();
			if (fis != null)
				fis.close();
		}
	}

	/**
	 * 转换
	 * 
	 * @param ht
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static DataTable HashtableToDataTable(Hashtable ht) {
		DataTable dt = new DataTable();
		dt.TableName = "Hashtable";
		Set<String> keys = ht.keySet();
		for (String key : keys) {
			dt.Columns.Add(key, String.class);
		}

		DataRow dr = dt.NewRow();
		for (String key : keys) {
			dr.setValue(key, ht.get(key));
		}
		dt.Rows.add(dr);
		return dt;
	}

	// public static void To(String url)
	// {
	// System.Web.HttpContext.Current.Response.Redirect(url,true);
	// }
	public static void Print(String url) {
		try {
			ContextHolderUtils.getResponse().getWriter()
					.write("<script language='JavaScript'> var newWindow =window.open('" + url
							+ "','p','width=0,top=10,left=10,height=1,scrollbars=yes,resizable=yes,toolbar=yes,location=yes,menubar=yes') ; newWindow.focus(); </script> ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过request 设置Entity值
	 * 
	 * @param en
	 * @param reqest
	 * @return
	 * @throws Exception 
	 */
	public static BP.En.Entity copyFromRequest(BP.En.Entity en, HttpServletRequest reqest) throws Exception {
		ArrayList<String> requestKeys = new ArrayList<String>();
		Enumeration enu = reqest.getParameterNames();
		while (enu.hasMoreElements()) {
			// 判断是否有内容，hasNext()
			String key = (String) enu.nextElement();
			requestKeys.add(key);
		}

		// 给每个属性值.
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr item : attrs) {
			String relKey = null;
			switch (item.getUIContralType()) {
			case TB:
				relKey = "TB_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			case CheckBok:
				relKey = "CB_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			case DDL:
				relKey = "DDL_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			case RadioBtn:
				relKey = "RB_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			default:
				break;
			}

			if (requestKeys.contains(relKey)) {

				int index = requestKeys.indexOf(relKey);
				// 说明已经找到了这个字段信息。
				String myK = requestKeys.get(index);

				if (myK == null || myK.equals("")) {
					continue;
				}

				if (item.getUIContralType() == UIContralType.CheckBok) {
					String val = reqest.getParameter(myK);
					if (val.equals("on") || val.equals("1")) {
						en.SetValByKey(item.getKey(), 1);
					} else {
						en.SetValByKey(item.getKey(), 0);
					}
				} else {
					String value = reqest.getParameter(myK).trim();
					en.SetValByKey(item.getKey(), value);
				}
				continue;
			} else {
				if (null != relKey && relKey.contains("CB_")) {
					en.SetValByKey(item.getKey(), 0);
				}
			}
		}
		return en;
	}

	public static void WinClose() {
		String clientscript = "<script language='javascript'> window.close(); </script>";
		try {
			ContextHolderUtils.getResponse().setContentType("text/html; charset=utf-8");
			ContextHolderUtils.getResponse().getWriter().write(clientscript);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void WinOpen(HttpServletResponse response, String url) throws IOException {
		java.text.DateFormat df = new java.text.SimpleDateFormat("MMddHHmmss");
		PubClass.WinOpen(response, url, "", "msg" + df.format(new java.util.Date()), 300, 300);
	}

	public static void WinOpen(HttpServletResponse response, String url, int w, int h) throws IOException {
		java.text.DateFormat df = new java.text.SimpleDateFormat("MMddHHmmss");
		PubClass.WinOpen(response, url, "", "msg" + df.format(new java.util.Date()), w, h);
	}

	public static void WinOpen(HttpServletResponse response, String url, String title, String winName, int width,
			int height) throws IOException {
		PubClass.WinOpen(response, url, title, winName, width, height, 100, 200);
	}

	public static void WinOpen(HttpServletResponse response, String url, String title, int width, int height)
			throws IOException {
		PubClass.WinOpen(response, url, title, "ActivePage", width, height, 100, 200);
	}

	public static void WinOpen(HttpServletResponse response, String url, String title, String winName, int width,
			int height, int top, int left) throws IOException {
		url = url.replace("<", "[");
		url = url.replace(">", "]");
		url = url.trim();
		title = title.replace("<", "[");
		title = title.replace(">", "]");
		title = title.replace("\"", "‘");
		if (top == 0 && left == 0) {
			response.getWriter()
					.write("<script language='JavaScript'> var newWindow =window.open('" + url + "','" + winName
							+ "','width=" + width + ",top=" + top
							+ ",scrollbars=yes,resizable=yes,toolbar=false,location=false') ; </script> ");
			response.getWriter().flush();
		} else {
			response.getWriter()
					.write("<script language='JavaScript'> var newWindow =window.open('" + url + "','" + winName
							+ "','width=" + width + ",top=" + top + ",left=" + left + ",height=" + height
							+ ",scrollbars=yes,resizable=yes,toolbar=false,location=false');</script>");
			response.getWriter().flush();
		}
	}

	public static void ResponseWriteBlueMsg(String msg) throws Exception {

		throw new Exception("");
	}

	/**
	 * 切换到信息也面。
	 * 
	 * @param mess
	 */
	public static void ToErrorPage(String mess, HttpServletResponse response) {
		BP.Sys.Glo.getRequest().getSession().setAttribute("info", mess);
		try {
			response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/ToErrorPage.htm?d=" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切换到信息也面。
	 * 
	 * @param mess
	 */
	public static void ToMsgPage(String mess, HttpServletResponse response) {
		mess = mess.replace("@", "<BR>@");
		BP.Sys.Glo.getRequest().getSession().setAttribute("info", mess);
		try {
			response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/InfoPage.jsp?d=" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 不用page 参数，show message
	 * 
	 * @param mess
	 */
	public static void Alert(String mess, HttpServletResponse response) {
		String script = "<script language=JavaScript>alert('" + mess + "');</script>";
		try {
			response.setCharacterEncoding("gbk");
			response.getWriter().write(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void ResponseWriteScript(String script) {
		script = "<script language=JavaScript> " + script + "</script>";
		try {
			ContextHolderUtils.getResponse().getWriter().write(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	public static BP.En.Entity CopyFromRequest(BP.En.Entity en) throws Exception {
		return CopyFromRequest(en, Glo.getRequest());
	}

	public static BP.En.Entity CopyFromRequest(BP.En.Entity en, HttpServletRequest reqest) throws Exception {
		String allKeys = ";";

		// 获取传递来的所有的checkbox ids 用于设置该属性为falsse.
		String checkBoxIDs = reqest.getParameter("CheckBoxIDs");
		if (checkBoxIDs != null) {
			String[] strs = checkBoxIDs.split(",");
			for (String str : strs) {
				if (str == null || str.equals(""))
					continue;

				// 设置该属性为false.
				en.SetValByKey(str.replace("CB_", ""), 0);
			}
		}

		for (Iterator iter = Glo.getRequest().getParameterMap().keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (key == null || key.equals(""))
				continue;
			// 获得实际的值, 具有特殊标记的，系统才赋值.
			String attrKey = key;
			if (key.startsWith("TB_"))
				attrKey = attrKey.replace("TB_", "");
			else if (key.startsWith("CB_"))
				attrKey = attrKey.replace("CB_", "");
			else if (key.startsWith("DDL_"))
				attrKey = attrKey.replace("DDL_", "");
			else if (key.startsWith("RB_"))
				attrKey = attrKey.replace("RB_", "");
			else
				continue;
			String val = reqest.getParameter(key);

			// 其他的属性.
			en.SetValByKey(attrKey, val);
		}

		return en;
	}
	
	public static Entity CopyFromRequestByPost(BP.En.Entity en, HttpServletRequest reqest) throws Exception {

		String allKeys = ";";
		// 获取传递来的所有的checkbox ids 用于设置该属性为false.
		String checkBoxIDs = Glo.getRequest().getParameter("CheckBoxIDs");
		if (checkBoxIDs != null) {
			String[] strs = checkBoxIDs.split(",");
			for (String str : strs) {
				if (str == null || "".equals(str))
					continue;

				if (str.contains("CBPara")) {
					/* 如果是参数字段. */
					en.getRow().SetValByKey(str.replace("CBPara_", ""), 0);
				} else {
					// 设置该属性为false.
					en.getRow().SetValByKey(str.replace("CB_", ""), 0);
				}

			}
		}

		for (Iterator iter = Glo.getRequest().getParameterMap().keySet().iterator(); iter.hasNext();) {
			String myK = (String) iter.next();
			allKeys += myK + ";";
		}

		// 给每个属性值.
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr item : attrs) {
			String relKey = null;
			switch (item.getUIContralType()) {
			case TB:
				relKey = "TB_" + item.getKey();
				break;
			case CheckBok:
				relKey = "CB_" + item.getKey();
				break;
			case DDL:
				relKey = "DDL_" + item.getKey();
				break;
			case RadioBtn:
				relKey = "RB_" + item.getKey();
				break;
			default:
				break;
			}

			if (relKey == null) {
				continue;
			}

			if (allKeys.contains(relKey + ";")) {
				// 说明已经找到了这个字段信息。
				for (Iterator iter = Glo.getRequest().getParameterMap().keySet().iterator(); iter.hasNext();) {
					String myK = (String) iter.next();
					if (myK == null || "".equals(myK)) {
						continue;
					}
					String val = Glo.getRequest().getParameter(myK);
					if (myK.endsWith(relKey)) {
						if (item.getUIContralType() == UIContralType.CheckBok) {
							if (myK.indexOf("CB_") == 0 || myK.indexOf("CBPara_") == 0) {
								en.SetValByKey(item.getKey(), 1);
							}
						} else {
							en.SetValByKey(item.getKey(), val);
						}
					}
				}
				continue;
			}
		}
		return en;
	}

}