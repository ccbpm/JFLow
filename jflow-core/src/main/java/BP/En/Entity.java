package BP.En;

import java.io.File;
import java.io.Serializable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import BP.DA.AtPara;
import BP.DA.Cash;
import BP.DA.CashEntity;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.DA.LogType;
import BP.DA.Paras;
import BP.Sys.EnVer;
import BP.Sys.EnVerDtl;
import BP.Sys.GloVar;
import BP.Sys.MapAttr;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.SysDocFile;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.Tools.StringUtils;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.Web.WebUser;
import BP.XML.XmlEn;

/**
 * Entity 鐨勬憳瑕佽鏄庛��
 */
public abstract class Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 涓庣紦瀛樻湁鍏崇殑鎿嶄綔
	private Entities _GetNewEntities = null;

	@SuppressWarnings("rawtypes")
	public Entities getGetNewEntities() {

		if (_GetNewEntities == null) {
			String str = this.toString();
			String ensName = str + "s";

			_GetNewEntities = ClassFactory.GetEns(ensName);
			if (_GetNewEntities != null)
				return _GetNewEntities;

			ArrayList al = ClassFactory.GetObjects("BP.En.Entities");
			for (Object o : al) {
				Entities ens = (Entities) ((o instanceof Entities) ? o : null);

				if (ens == null) {
					continue;
				}
				if (ens.getGetNewEntity().toString() != null && ens.getGetNewEntity().toString().equals(str)) {
					_GetNewEntities = ens;
					return _GetNewEntities;
				}
			}
			throw new RuntimeException("@no ens" + this.toString());
		}
		return _GetNewEntities;
	}

	public String getClassID() {
		return this.toString();
	}

	// 妫�鏌ヤ竴涓睘鎬у�兼槸鍚﹀瓨鍦ㄤ簬瀹炰綋闆嗗悎涓�
	/**
	 * 妫�鏌ヤ竴涓睘鎬у�兼槸鍚﹀瓨鍦ㄤ簬瀹炰綋闆嗗悎涓� 杩欎釜鏂规硶缁忓父鐢ㄥ埌鍦╞eforeinsert涓��
	 * 
	 * @param key
	 *            瑕佹鏌ョ殑key.
	 * @param val
	 *            瑕佹鏌ョ殑key.瀵瑰簲鐨剉al
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	protected final int ExitsValueNum(String key, String val) throws NumberFormatException, Exception {
		Paras ps = new Paras();
		ps.Add("p", val);

		String sql = "SELECT COUNT( " + key + " ) FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + key + "="
				+ this.getHisDBVarStr() + "p";
		return Integer.parseInt(DBAccess.RunSQLReturnVal(sql, ps).toString());
	}

	/**
	 * 鍙栧嚭浠栫殑鏄庣粏闆嗗悎銆�
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public final ArrayList<Entities> GetDtlsDatasOfArrayList() throws Exception {
		ArrayList<Entities> al = new ArrayList<Entities>();
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			al.add(this.GetDtlEnsDa(dtl.getEns()));
		}
		return al;
	}

	public final ArrayList<Entities> GetDtlsDatasOfList() throws Exception {
		ArrayList<Entities> al = new ArrayList<Entities>();
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			al.add(this.GetDtlEnsDa(dtl));
		}
		return al;
	}

	// 鍏充簬鏄庣粏鐨勬搷浣�
	/**
	 * 寰楀埌浠栫殑鏁版嵁瀹炰綋
	 * 
	 * @param EnsName
	 *            绫诲悕绉�
	 * @return
	 * @throws Exception
	 */
	public final Entities GetDtlEnsDa(String EnsName) throws Exception {
		Entities ens = ClassFactory.GetEns(EnsName);
		return GetDtlEnsDa(ens);
	}

	public final Entities GetDtlEnsDa(EnDtl dtl) throws Exception {

		try {
			QueryObject qo = new QueryObject(dtl.getEns());
			qo.AddWhere(dtl.getRefKey(), this.getPKVal().toString());
			qo.DoQuery();
			return dtl.getEns();
		} catch (RuntimeException e) {
			throw new RuntimeException("@鍦ㄥ彇[" + this.getEnDesc() + "]鐨勬槑缁嗘椂鍑虹幇閿欒銆俒" + dtl.getDesc() + "],涓嶅湪浠栫殑闆嗗悎鍐呫��");
		}
	}

	/**
	 * 鍙栧嚭浠栫殑鏁版嵁瀹炰綋
	 * 
	 * @param ens
	 *            闆嗗悎
	 * @return 鎵ц鍚庣殑瀹炰綋淇℃伅
	 * @throws Exception
	 */
	public final Entities GetDtlEnsDa(Entities ens) throws Exception {
		for (EnDtl dtl : this.getEnMap().getDtls()) {
			if (dtl.getEns().getClass() == ens.getClass()) {
				QueryObject qo = new QueryObject(dtl.getEns());
				qo.AddWhere(dtl.getRefKey(), this.getPKVal().toString());
				qo.DoQuery();
				return dtl.getEns();
			}
		}
		throw new RuntimeException(
				"@鍦ㄥ彇[" + this.getEnDesc() + "]鐨勬槑缁嗘椂鍑虹幇閿欒銆俒" + ens.getGetNewEntity().getEnDesc() + "],涓嶅湪浠栫殑闆嗗悎鍐呫��");
	}

	/**
	 * 杞寲鎴�
	 * 
	 * @return
	 */
	public final String ToStringAtParas() {
		String str = "";
		for (Attr attr : this.getEnMap().getAttrs()) {
			str += "@" + attr.getKey() + "=" + this.GetValByKey(attr.getKey());
		}
		return str;
	}

	public final DataTable ToEmptyTableField() {
		return ToEmptyTableField(null);
	}

	public final DataTable ToEmptyTableField(Entity en) {
		DataTable dt = new DataTable();

		if (en == null)
			en = this;

		dt.TableName = en.getEnMap().getPhysicsTable();

		for (Attr attr : en.getEnMap().getAttrs()) {
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			case DataType.AppInt:
				dt.Columns.Add(new DataColumn(attr.getKey(), Integer.class, true));
				break;
			case DataType.AppFloat:
				dt.Columns.Add(new DataColumn(attr.getKey(), Float.class, true));
				break;
			case DataType.AppBoolean:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			case DataType.AppDouble:
				dt.Columns.Add(new DataColumn(attr.getKey(), Double.class, true));
				break;
			case DataType.AppMoney:
				dt.Columns.Add(new DataColumn(attr.getKey(), Double.class, true));
				break;
			case DataType.AppDate:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			case DataType.AppDateTime:
				dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
				break;
			default:
				throw new RuntimeException("@bulider insert sql error: 娌℃湁杩欎釜鏁版嵁绫诲瀷");
			}
		}
		return dt;
	}

	/**
	 * 鍖哄垎澶у皬鍐�
	 * 
	 * @param tableName
	 * @return
	 */
	public final DataTable ToDataTableField(String tableName) {

		DataTable dt = this.ToEmptyTableField();
		dt.TableName = tableName;

		// 澧炲姞鍙傛暟鍒�.
		if (this.getRow().containsKey("AtPara") == true) {
			/* 濡傛灉鍖呭惈杩欎釜瀛楁,灏辫鏄庝粬鏈夊弬鏁�,鎶婂弬鏁颁篃瑕佸紕鎴愪竴涓垪. */
			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet()) {
				if (dt.Columns.contains(key) == true)
					continue;

				dt.Columns.Add(key, System.class);
			}
		}

		DataRow dr = dt.NewRow();
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyDataType() == DataType.AppBoolean) {
				if (this.GetValIntByKey(attr.getKey()) == 1)
					dr.setValue(attr.getKey(), "1");
				else
					dr.setValue(attr.getKey(), "0");
				continue;
			}

			/*
			 * 濡傛灉鏄閿� 灏辫鍘绘帀宸﹀彸绌烘牸銆�
			 */
			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK) {
				dr.setValue(attr.getKey(), this.GetValByKey(attr.getKey()).toString().trim());
				continue;
			}

			String obj = this.GetValStrByKey(attr.getKey());
			if (obj == null && attr.getIsNum())
				obj = "0";

			dr.setValue(attr.getKey(), obj);

		}

		/* 濡傛灉鍖呭惈杩欎釜瀛楁 */
		if (this.getRow().containsKey("AtPara") == true) {

			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet())
				dr.setValue(key, ap.getHisHT().get(key));
		}

		dt.Rows.add(dr);
		return dt;

	}

	// 鍏充簬database 鎿嶄綔
	public int RunSQL(String sql) {
		Paras ps = new Paras();
		ps.SQL = sql;
		return this.RunSQL(ps);
	}

	/**
	 * 鍦ㄦ瀹炰綋鏄繍琛宻ql 杩斿洖缁撴灉闆嗗悎
	 * 
	 * @param sql
	 *            瑕佽繍琛岀殑sql
	 * @return 鎵ц鐨勭粨鏋�
	 * @throws Exception
	 */
	public final int RunSQL(Paras ps) {
		return DBAccess.RunSQL(ps);
	}

	public final int RunSQL(String sql, Paras paras) {
		return DBAccess.RunSQL(sql, paras);
	}

	/**
	 * 鍦ㄦ瀹炰綋鏄繍琛宻ql 杩斿洖缁撴灉闆嗗悎
	 * 
	 * @param sql
	 *            瑕佽繍琛岀殑 select sql
	 * @return 鎵ц鐨勬煡璇㈢粨鏋�
	 * @throws Exception
	 */
	public DataTable RunSQLReturnTable(String sql) {
		return DBAccess.RunSQLReturnTable(sql);

	}

	// 浜庣紪鍙锋湁鍏崇郴鐨勫鐞嗐��
	/**
	 * 杩欎釜鏂规硶鏄负涓嶅垎绾у瓧鍏革紝鐢熸垚涓�涓紪鍙枫�傛牴鎹埗璁㈢殑 灞炴��.
	 * 
	 * @param attrKey
	 *            灞炴��
	 * @return 浜х敓鐨勫簭鍙�
	 * @throws Exception
	 */
	public final String GenerNewNoByKey(String attrKey) throws Exception {
		try {
			String sql = null;
			Map map = this.getEnMap();

			Attr attr = map.GetAttrByKey(attrKey);
			if (!attr.getUIIsReadonly()) {
				throw new RuntimeException("@闇�瑕佽嚜鍔ㄧ敓鎴愮紪鍙风殑鍒�(" + attr.getKey() + ")蹇呴』涓哄彧璇汇��");
			}

			String field = map.GetFieldByKey(attrKey);
			switch (map.getEnDBUrl().getDBType()) {
			case MSSQL:
				sql = "SELECT CONVERT(INT, MAX(CAST(" + field + " as int)) )+1 AS No FROM " + map.getPhysicsTable();
				break;
			case Oracle:
				sql = "SELECT MAX(" + field + ") +1 AS No FROM " + map.getPhysicsTable();
				break;
			case MySQL:
				sql = "SELECT CONVERT(MAX(CAST(" + field + " AS SIGNED INTEGER)),SIGNED) +1 AS No FROM "
						+ map.getPhysicsTable();
				break;
			case Informix:
				sql = "SELECT MAX(" + field + ") +1 AS No FROM " + map.getPhysicsTable();
				break;
			case Access:
				sql = "SELECT MAX( [" + field + "]) +1 AS  No FROM " + map.getPhysicsTable();
				break;
			default:
				throw new RuntimeException("error");
			}
			String str = (new Integer(DBAccess.RunSQLReturnValInt(sql, 1))).toString();
			if (str.equals("0") || str.equals("")) {
				str = "1";
			}
			return StringUtils.leftPad(str, Integer.parseInt(map.getCodeStruct()), "0");

		} catch (RuntimeException ex) {
			this.CheckPhysicsTable();
			throw ex;
		}
	}

	/**
	 * 鎸夌収涓�鍒椾骇鐢熼『搴忓彿鐮併��
	 * 
	 * @param attrKey
	 *            瑕佷骇鐢熺殑鍒�
	 * @param attrGroupKey
	 *            鍒嗙粍鐨勫垪鍚�
	 * @param FKVal
	 *            鍒嗙粍鐨勪富閿�
	 * @return
	 * @throws Exception
	 */
	public final String GenerNewNoByKey(int nolength, String attrKey, String attrGroupKey, String attrGroupVal)
			throws Exception {
		if (attrGroupKey == null || attrGroupVal == null) {
			throw new RuntimeException("@鍒嗙粍瀛楁attrGroupKey attrGroupVal 涓嶈兘涓虹┖");
		}

		Map map = this.getEnMap();

		Paras ps = new Paras();

		String sql = "";
		String field = map.GetFieldByKey(attrKey);

		switch (map.getEnDBUrl().getDBType()) {
		case MSSQL:
			sql = "SELECT CONVERT(bigint, MAX([" + field + "]))+1 AS Num FROM " + map.getPhysicsTable() + " WHERE "
					+ attrGroupKey + "='" + attrGroupVal + "'";
			break;
		case Oracle:
			ps.Add("groupKey", attrGroupKey);
			ps.Add("groupVal", attrGroupVal);
			ps.Add("f", attrKey);
			sql = "SELECT MAX( :f )+1 AS No FROM " + map.getPhysicsTable() + " WHERE " + this.getHisDBVarStr()
					+ "groupKey=" + this.getHisDBVarStr() + "groupVal ";
			break;
		case Informix:
			sql = "SELECT MAX( :f )+1 AS No FROM " + map.getPhysicsTable() + " WHERE " + this.getHisDBVarStr()
					+ "groupKey=" + this.getHisDBVarStr() + "groupVal ";
			break;
		case MySQL:
			sql = "SELECT MAX(" + field + ") +1 AS Num FROM " + map.getPhysicsTable() + " WHERE " + attrGroupKey + "='"
					+ attrGroupVal + "'";
			break;
		case Access:
			sql = "SELECT MAX([" + field + "]) +1 AS Num FROM " + map.getPhysicsTable() + " WHERE " + attrGroupKey
					+ "='" + attrGroupVal + "'";
			break;
		default:
			throw new RuntimeException("error");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		String str = "1";
		if (dt.Rows.size() != 0) {
			if (dt.Rows.get(0).getValue(0) == null) {
				str = "1";
			} else {
				str = dt.Rows.get(0).getValue(0).toString();
			}
		}
		return StringUtils.leftPad(str, nolength, '0');
	}

	public final String GenerNewNoByKey(String attrKey, String attrGroupKey, String attrGroupVal)
			throws NumberFormatException, Exception {
		return this.GenerNewNoByKey(Integer.parseInt(this.getEnMap().getCodeStruct()), attrKey, attrGroupKey,
				attrGroupVal);
	}

	/**
	 * 鎸夌収涓ゅ垪鏌ョ敓椤哄簭鍙风爜銆�
	 * 
	 * @param attrKey
	 * @param attrGroupKey1
	 * @param attrGroupKey2
	 * @param attrGroupVal1
	 * @param attrGroupVal2
	 * @return
	 * @throws Exception
	 */
	public final String GenerNewNoByKey(String attrKey, String attrGroupKey1, String attrGroupKey2,
			Object attrGroupVal1, Object attrGroupVal2) throws Exception {
		String f = this.getEnMap().GetFieldByKey(attrKey);
		Paras ps = new Paras();
		// ps.Add("f", f);

		String sql = "";
		switch (this.getEnMap().getEnDBUrl().getDBType()) {
		case Oracle:
		case Informix:
			sql = "SELECT   MAX(" + f + ") +1 AS No FROM " + this.getEnMap().getPhysicsTable();
			break;
		case MSSQL:
			sql = "SELECT CONVERT(INT, MAX(" + this.getEnMap().GetFieldByKey(attrKey) + ") )+1 AS No FROM "
					+ this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetFieldByKey(attrGroupKey1)
					+ "='" + attrGroupVal1 + "' AND " + this.getEnMap().GetFieldByKey(attrGroupKey2) + "='"
					+ attrGroupVal2 + "'";
			break;
		case Access:
			sql = "SELECT CONVERT(INT, MAX(" + this.getEnMap().GetFieldByKey(attrKey) + ") )+1 AS No FROM "
					+ this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetFieldByKey(attrGroupKey1)
					+ "='" + attrGroupVal1 + "' AND " + this.getEnMap().GetFieldByKey(attrGroupKey2) + "='"
					+ attrGroupVal2 + "'";
			break;
		default:
			break;
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		String str = "1";
		if (dt.Rows.size() != 0) {
			str = dt.Rows.get(0).getValue(0).toString();
		}
		return StringUtils.leftPad(str, Integer.parseInt(this.getEnMap().getCodeStruct()), '0');
	}

	// 鏋勯�犳柟娉�
	public Entity() {
	}

	// 鎺掑簭鎿嶄綔
	protected final void DoOrderUp(String groupKeyAttr, String groupKeyVal, String idxAttr) {
		// string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + " WHERE " + groupKeyAttr + "='" + groupKeyVal
				+ "' ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows) {
			idx++;
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (pkval.equals(myNo)) {
				isMeet = true;
				break;
			}

			if (!isMeet) {
				beforeNo = myNo;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");

		}
		DBAccess.RunSQL(
				"UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo + "'");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval + "'");

	}

	protected final void DoOrderUp(String groupKeyAttr, String groupKeyVal, String gKey2, String gVal2,
			String idxAttr) {
		// string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + " WHERE (" + groupKeyAttr + "='" + groupKeyVal
				+ "' AND " + gKey2 + "='" + gVal2 + "') ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows) {
			idx++;
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (pkval.equals(myNo)) {
				isMeet = true;
				break;
			}

			if (!isMeet) {
				beforeNo = myNo;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo
					+ "'  AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "') ");
		}
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo
				+ "'  AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "')");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval
				+ "'  AND   (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "')");
	}

	protected final void DoOrderDown(String groupKeyAttr, String groupKeyVal, String idxAttr) {

		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + " WHERE " + groupKeyAttr + "='" + groupKeyVal
				+ "' order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;
		for (DataRow dr : dt.Rows) {
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (isMeet) {
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo)) {
				isMeet = true;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");
		}

		DBAccess.RunSQL(
				"UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo + "'");
		DBAccess.RunSQL("UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval + "'");
	}

	protected final void DoOrderDown(String groupKeyAttr, String groupKeyVal, String gKeyAttr2, String gKeyVal2,
			String idxAttr) {
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + " WHERE (" + groupKeyAttr + "='" + groupKeyVal
				+ "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' ) order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;
		for (DataRow dr : dt.Rows) {
			myNo = dr.getValue(pk).toString();
			/*
			 * warning myNo = dr[pk].toString();
			 */
			if (isMeet) {
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo)) {
				isMeet = true;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo
					+ "' AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' ) ");
		}

		DBAccess.RunSQL("UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo
				+ "' AND (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' )");
		DBAccess.RunSQL("UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval
				+ "' AND (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' )");
	}

	// 鐩存帴鎿嶄綔
	/**
	 * 鐩存帴鏇存柊
	 * 
	 * @throws Exception
	 */
	public final int DirectUpdate() throws Exception {
		return EntityDBAccess.Update(this, null);
	}

	/**
	 * 鐩存帴鐨処nsert
	 * 
	 * @throws Exception
	 */
	public int DirectInsert() throws Exception {
		try {

			return this.RunSQL(this.getSQLCash().getInsert(), SqlBuilder.GenerParas(this, null));

		} catch (RuntimeException ex) {
			this.roll();
			if (SystemConfig.getIsDebug()) {
				try {
					this.CheckPhysicsTable();
				} catch (Exception ex1) {
					throw new RuntimeException(ex.getMessage() + " == " + ex1.getMessage());
				}
			}
			throw ex;
		}

		// this.RunSQL(this.SQLCash.Insert, SqlBuilder.GenerParas(this, null));
	}

	/**
	 * 鐩存帴鐨凞elete
	 * 
	 * @throws Exception
	 */
	public final void DirectDelete() throws Exception {
		DBAccess.RunSQL(this.getSQLCash().getDelete(), SqlBuilder.GenerParasPK(this));
	}

	public void DirectSave() throws Exception {
		if (this.getIsExits()) {
			this.DirectUpdate();
		} else {
			this.DirectInsert();
		}
	}

	// Retrieve
	/**
	 * 鎸夌収灞炴�ф煡璇�
	 * 
	 * @param attr
	 *            灞炴�у悕绉�
	 * @param val
	 *            鍊�
	 * @return 鏄惁鏌ヨ鍒�
	 * @throws Exception
	 */
	public final boolean RetrieveByAttrAnd(String attr1, Object val1, String attr2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr1, val1);
		qo.addAnd();
		qo.AddWhere(attr2, val2);

		if (qo.DoQuery() >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 鎸夌収灞炴�ф煡璇�
	 * 
	 * @param attr
	 *            灞炴�у悕绉�
	 * @param val
	 *            鍊�
	 * @return 鏄惁鏌ヨ鍒�
	 * @throws Exception
	 */
	public final boolean RetrieveByAttrOr(String attr1, Object val1, String attr2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr1, val1);
		qo.addOr();
		qo.AddWhere(attr2, val2);

		if (qo.DoQuery() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 鎸夌収灞炴�ф煡璇�
	 * 
	 * @param attr
	 *            灞炴�у悕绉�
	 * @param val
	 *            鍊�
	 * @return 鏄惁鏌ヨ鍒�
	 * @throws Exception
	 */
	public final boolean RetrieveByAttr(String attr, Object val) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr, val);
		if (qo.DoQuery() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 浠嶥BSources鐩存帴鏌ヨ
	 * 
	 * @return 鏌ヨ鐨勪釜鏁�
	 * @throws Exception
	 */
	public int RetrieveFromDBSources() throws Exception {

		try {
			return DBAccess.RunSQLReturnResultSet(this.getSQLCash().getSelect(), SqlBuilder.GenerParasPK(this), this,
					this.getEnMap().getAttrs());

		} catch (java.lang.Exception e) {
			try {
				this.CheckPhysicsTable();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return DBAccess.RunSQLReturnResultSet(this.getSQLCash().getSelect(), SqlBuilder.GenerParasPK(this), this,
					this.getEnMap().getAttrs());

		}
	}

	/**
	 * 鏌ヨ
	 * 
	 * @param key
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public final int Retrieve(String key, Object val) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		return qo.DoQuery();
	}

	public final int Retrieve(String key1, Object val1, String key2, Object val2) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		return qo.DoQuery();
	}

	public final int Retrieve(String key1, Object val1, String key2, Object val2, String key3, Object val3)
			throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4, Object val4)
			throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		qo.addAnd();
		qo.AddWhere(key4, val4);
		return qo.DoQuery();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int Retrieve() throws Exception {

		// 濡傛灉鏄病鏈夋斁鍏ョ紦瀛樼殑瀹炰綋.
		try {

			int i = DBAccess.RunSQLReturnResultSet(this.getSQLCash().getSelect(), SqlBuilder.GenerParasPK(this), this,
					this.getEnMap().getAttrs());
			if (i > 0)
				return i;

		} catch (RuntimeException ex) {

			String msg = ex.getMessage() == null ? "" : ex.getMessage();
			if (msg.contains("鏃犳晥") || msg.contains("field list")) {
				try {

					this.CheckPhysicsTable();
					if (BP.DA.DBAccess.IsExits(this.getEnMap().getPhysicsTable()) == true)
						return this.RetrieveFromDBSources();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw new RuntimeException(msg + "@鍦‥ntity(" + this.toString() + ")鏌ヨ鏈熼棿鍑虹幇閿欒@" + ex.getStackTrace());
		}

		String msg = "";
		String pk = this.getPK();

		if (pk.equals("OID")) {
			msg += "[ 涓婚敭=OID 鍊�=" + this.GetValStrByKey("OID") + " ]";
		} else if (pk.equals("No")) {
			msg += "[ 涓婚敭=No 鍊�=" + this.GetValStrByKey("No") + " ]";
		} else if (pk.equals("MyPK")) {
			msg += "[ 涓婚敭=MyPK 鍊�=" + this.GetValStrByKey("MyPK") + " ]";
		} else if (pk.equals("NodeID")) {
			msg += "[ 涓婚敭=NodeID 鍊�=" + this.GetValStrByKey("NodeID") + " ]";
		} else if (pk.equals("WorkID")) {
			msg += "[ 涓婚敭=WorkID 鍊�=" + this.GetValStrByKey("WorkID") + " ]";
		} else {
			Hashtable ht = this.getPKVals(); /*
									 * warning for (String key : ht.keySet())
									 */
			Set<String> keys = ht.keySet();
			for (String key : keys) {
				msg += "[ 涓婚敭=" + key + " 鍊�=" + ht.get(key) + " ]";
			}

			throw new RuntimeException("@娌℃湁[" + this.getEnMap().getEnDesc() + "  " + this.getEnMap().getPhysicsTable()
					+ ", 绫籟" + this.toString() + "], 鐗╃悊琛╗" + this.getEnMap().getPhysicsTable() + "] 瀹炰緥銆�" + msg);

		}

//		throw new RuntimeException("@娌℃湁[" + this.getEnMap().getEnDesc() + "  " + this.getEnMap().getPhysicsTable()
//				+ ", 绫籟" + this.toString() + "], 鐗╃悊琛╗" + this.getEnMap().getPhysicsTable() + "] 瀹炰緥銆侾K = "
//				+ this.GetValByKey(this.getPK()));
		return 0;

	}

	/**
	 * 鎸変富閿煡璇紝杩斿洖鏌ヨ鍑烘潵鐨勪釜鏁般�� 濡傛灉鏌ヨ鍑烘潵鐨勬槸澶氫釜瀹炰綋锛岄偅鎶婄涓�涓疄浣撶粰鍊笺��
	 * 
	 * @return 鏌ヨ鍑烘潵鐨勪釜鏁�
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int Retrieve_Old() throws Exception {

		// 濡傛灉鏄病鏈夋斁鍏ョ紦瀛樼殑瀹炰綋.
		try {

			int num = EntityDBAccess.Retrieve(this, this.getSQLCash().getSelect(), SqlBuilder.GenerParasPK(this));

			if (num <= 0) {
				String msg="";
				if (this.getPK().equals("OID")) {
					msg += "[ 涓婚敭=OID 鍊�=" + this.GetValStrByKey("OID") + " ]";
				} else if (this.getPK().equals("No")) {
					msg += "[ 涓婚敭=No 鍊�=" + this.GetValStrByKey("No") + " ]";
				} else if (this.getPK().equals("MyPK")) {
					msg += "[ 涓婚敭=MyPK 鍊�=" + this.GetValStrByKey("MyPK") + " ]";
				} else if (this.getPK().equals("ID")) {
					msg += "[ 涓婚敭=ID 鍊�=" + this.GetValStrByKey("ID") + " ]";
				} else {
					Hashtable ht = this.getPKVals();
					Set<String> keys = ht.keySet();
					for (String key : keys) {
						msg += "[ 涓婚敭=" + key + " 鍊�=" + ht.get(key) + " ]";
					}
				}
				Log.DefaultLogWriteLine(LogType.Error,
						"@娌℃湁[" + this.getEnMap().getEnDesc() + "  " + this.getEnMap().getPhysicsTable() + ", 绫籟"
								+ this.toString() + "], 鐗╃悊琛╗" + this.getEnMap().getPhysicsTable() + "] 瀹炰緥銆侾K = "
								+ this.GetValByKey(this.getPK())+msg);
			}
			return 1;
		} catch (RuntimeException ex) {
			String msg = ex.getMessage() == null ? "" : ex.getMessage();
			if (msg.contains("鏃犳晥") || msg.contains("field list")) {
				try {

					this.CheckPhysicsTable();

					if (BP.DA.DBAccess.IsExits(this.getEnMap().getPhysicsTable()) == false)
						return this.Retrieve();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw new RuntimeException(msg + "@鍦‥ntity(" + this.toString() + ")鏌ヨ鏈熼棿鍑虹幇閿欒@" + ex.getStackTrace());
		}
	}

	/**
	 * 鍒ゆ柇鏄笉鏄瓨鍦ㄧ殑鏂规硶.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getIsExits() throws Exception {

		try {

			if (this.getPK().contains(",")) {
				Attrs attrs = this.getEnMap().getAttrs();

				// 璇存槑澶氫釜涓婚敭
				QueryObject qo = new QueryObject(this);
				String[] pks = this.getPK().split("[,]", -1);

				boolean isNeedAddAnd = false;
				for (String pk : pks) {
					if (StringHelper.isNullOrEmpty(pk)) {
						continue;
					}

					if (isNeedAddAnd) {
						qo.addAnd();
					} else {
						isNeedAddAnd = true;
					}

					Attr attr = attrs.GetAttrByKey(pk);
					switch (attr.getMyDataType()) {
					case DataType.AppBoolean:
					case DataType.AppInt:
						qo.AddWhere(pk, this.GetValIntByKey(attr.getKey()));
						break;
					case DataType.AppDouble:
					case DataType.AppMoney:
					case DataType.AppRate:
						qo.AddWhere(pk, this.GetValDecimalByKey(attr.getKey()));
						break;
					default:
						qo.AddWhere(pk, this.GetValStringByKey(attr.getKey()));
						break;
					}

				}

				if (qo.DoQueryToTable().Rows.size() == 0) {
					return false;
				}

				return true;
			}

			Object obj = this.getPKVal();
			if (obj == null || obj.toString().equals("")) {
				return false;
			}

			if (this.getIsOIDEntity()) {
				if (obj.toString().equals("0")) {
					return false;
				}
			}

			// 鐢熸垚鏁版嵁搴撳垽鏂鍙ャ��
			String selectSQL = "SELECT count( " + this.getPK() + ") as Num FROM " + this.getEnMap().getPhysicsTable()
					+ " WHERE ";
			switch (SystemConfig.getAppCenterDBType()) {
			case MSSQL:
				selectSQL += SqlBuilder.GetKeyConditionOfMS(this);
				break;
			case Oracle:
				selectSQL += SqlBuilder.GetKeyConditionOfOraForPara(this);
				break;
			case Informix:
				selectSQL += SqlBuilder.GetKeyConditionOfInformixForPara(this);
				break;
			case MySQL:
				selectSQL += SqlBuilder.GetKeyConditionOfMySQL(this);
				break;
			case Access:
				selectSQL += SqlBuilder.GetKeyConditionOfOLE(this);
				break;
			default:
				throw new RuntimeException("@娌℃湁璁捐鍒般��" + this.getEnMap().getEnDBUrl().getDBUrlType());
			}

			Paras ps = SqlBuilder.GenerParasPK(this);
			ps.SQL = selectSQL;
			int val = DBAccess.RunSQLReturnValInt(ps);
			if (val == 0)
				return false;

			return true;

		} catch (RuntimeException ex) {

			this.CheckPhysicsTable();
			throw ex;
		}
	}

	/**
	 * 鎸夌収涓婚敭鏌ヨ锛屾煡璇㈠嚭鏉ョ殑缁撴灉涓嶈祴缁欏綋鍓嶇殑瀹炰綋銆�
	 * 
	 * @return 鏌ヨ鍑烘潵鐨勪釜鏁�
	 * @throws Exception
	 */
	public final DataTable RetrieveNotSetValues() throws Exception {
		return this.RunSQLReturnTable(SqlBuilder.Retrieve(this));
	}

	/**
	 * 杩欎釜琛ㄩ噷鏄惁瀛樺湪
	 * 
	 * @param pk
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public final boolean IsExit(String pk, Object val) throws Exception {

		if (pk.equals("OID")) {
			if (Integer.parseInt(val.toString()) == 0) {
				return false;
			} else {
				return true;
			}
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(" + pk + ") FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + pk + "="
				+ ps.getDBStr() + "PK";
		ps.Add("PK", val);
		int num = DBAccess.RunSQLReturnValInt(ps);
		if (num == 0)
			return false;
		return true;
	}

	public final boolean IsExit(String pk1, Object val1, String pk2, Object val2) throws Exception {

		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(" + pk1 + ") as Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + pk1 + "="
				+ ps.getDBStr() + "PK1 AND " + pk2 + "=" + ps.getDBStr() + "PK2";

		ps.Add("PK1", val1);
		ps.Add("PK2", val2);

		int num = DBAccess.RunSQLReturnValInt(ps);
		if (num == 0)
			return false;
		return true;
	}

	public final boolean IsExit(String pk1, Object val1, String pk2, Object val2, String pk3, Object val3)
			throws Exception {

		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(" + pk1 + ") as Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + pk1 + "="
				+ ps.getDBStr() + "PK1 AND " + pk2 + "=" + ps.getDBStr() + "PK2 AND  " + pk3 + "=" + ps.getDBStr()
				+ "PK3";

		ps.Add("PK1", val1);
		ps.Add("PK2", val2);
		ps.Add("PK3", val3);

		int num = DBAccess.RunSQLReturnValInt(ps);
		if (num == 0)
			return false;
		return true;
	}

	/**
	 * 鍒犻櫎涔嬪墠瑕佸仛鐨勫伐浣�
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean beforeDelete() throws Exception {
		return true;
	}

	/**
	 * 鎶婄紦瀛樺垹闄�
	 * 
	 * @throws Exception
	 */
	public final void DeleteDataAndCash() throws Exception {
		this.Delete();
		this.DeleteFromCash();
	}

	public final void DeleteFromCash() throws Exception {
		// 鍒犻櫎缂撳瓨.
		CashEntity.Delete(this.toString(), this.getPKVal().toString());
		// 鍒犻櫎鏁版嵁.
		this.getRow().clear();
	}

	public final int Delete() throws Exception {
		if (!this.beforeDelete()) {
			return 0;
		}

		int i = 0;
		try {
			i = DBAccess.RunSQL(this.getSQLCash().getDelete(), SqlBuilder.GenerParasPK(this));
		} catch (RuntimeException ex) {
			Log.DebugWriteInfo(ex.getMessage());
			throw ex;
		}

		this.afterDelete();

		return i;
	}

	/**
	 * 鍒犻櫎鎸囧畾鐨勬暟鎹�
	 * 
	 * @param attr
	 * @param val
	 * @throws Exception
	 */
	public final int Delete(String attr, Object val) {
		Paras ps = new Paras();
		ps.Add(attr, val);

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE "
				+ this.getEnMap().GetAttrByKey(attr).getField() + " =" + this.getHisDBVarStr() + attr, ps);

	}

	public final int Delete(String attr1, Object val1, String attr2, Object val2) {
		Paras ps = new Paras();
		ps.Add(attr1, val1);
		ps.Add(attr2, val2);

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + attr1 + " ="
				+ this.getHisDBVarStr() + attr1 + " AND " + attr2 + " =" + this.getHisDBVarStr() + attr2, ps);

	}

	public final int Delete(String attr1, Object val1, String attr2, Object val2, String attr3, Object val3) {
		Paras ps = new Paras();
		ps.Add(attr1, val1);
		ps.Add(attr2, val2);
		ps.Add(attr3, val3);

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + attr1 + " ="
				+ this.getHisDBVarStr() + attr1 + " AND " + attr2 + " =" + this.getHisDBVarStr() + attr2 + " AND "
				+ attr3 + " =" + this.getHisDBVarStr() + attr3, ps);

	}

	public final int Delete(String attr1, Object val1, String attr2, Object val2, String attr3, Object val3,
			String attr4, Object val4) {
		Paras ps = new Paras();
		ps.Add(attr1, val1);
		ps.Add(attr2, val2);
		ps.Add(attr3, val3);
		ps.Add(attr4, val4);

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + attr1 + " ="
				+ this.getHisDBVarStr() + attr1 + " AND " + attr2 + " =" + this.getHisDBVarStr() + attr2 + " AND "
				+ attr3 + " =" + this.getHisDBVarStr() + attr3 + " AND " + attr4 + " =" + this.getHisDBVarStr() + attr4,
				ps);

	}

	protected void afterDelete() throws Exception {

		return;
	}

	// 鍙傛暟瀛楁
	private AtPara getAtPara() {

		Object tempVar = this.getRow().GetValByKey("_ATObj_");
		AtPara at = (AtPara) ((tempVar instanceof AtPara) ? tempVar : null);
		if (at != null) {
			return at;
		}
		try {
			String atParaStr = this.GetValStringByKey("AtPara");
			if (StringHelper.isNullOrEmpty(atParaStr)) {
				// 娌℃湁鍙戠幇鏁版嵁锛屽氨鎵ц鍒濆鍖�.
				this.InitParaFields();

				// 閲嶆柊鑾峰彇涓�娆°��
				atParaStr = this.GetValStringByKey("AtPara");
				if (StringHelper.isNullOrEmpty(atParaStr)) {
					atParaStr = "";
				}

				at = new AtPara(atParaStr);
				this.SetValByKey("_ATObj_", at);
				return at;
			}
			at = new AtPara(atParaStr);
			this.SetValByKey("_ATObj_", at);
			return at;
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@鑾峰彇鍙傛暟AtPara鏃跺嚭鐜板紓甯�" + ex.getMessage() + "锛屽彲鑳芥槸鎮ㄦ病鏈夊姞鍏ョ害瀹氱殑鍙傛暟瀛楁AtPara. " + ex.getMessage());
		}
	}

	/**
	 * 鍒濆鍖栧弬鏁板瓧娈�(闇�瑕佸瓙绫婚噸鍐�)
	 * 
	 * @return
	 */
	protected void InitParaFields() {
	}

	/**
	 * 鑾峰彇鍙傛暟
	 * 
	 * @param key
	 * @return
	 */
	public final String GetParaString(String key) {
		return getAtPara().GetValStrByKey(key);
	}

	/**
	 * 鑾峰彇鍙傛暟
	 * 
	 * @param key
	 * @param isNullAsVal
	 * @return
	 */
	public final String GetParaString(String key, String isNullAsVal) {
		String str = getAtPara().GetValStrByKey(key);
		if (StringHelper.isNullOrEmpty(str)) {
			return isNullAsVal;
		}
		return str;
	}

	/**
	 * 鑾峰彇鍙傛暟Init鍊�
	 * 
	 * @param key
	 * @return
	 */
	public final int GetParaInt(String key) {
		return getAtPara().GetValIntByKey(key);
	}

	public final int GetParaInt(String key, int isNullAsVal) {
		return getAtPara().GetValIntByKey(key, isNullAsVal);
	}

	public final float GetParaFloat(String key) {
		return getAtPara().GetValFloatByKey(key);
	}

	/**
	 * 鑾峰彇鍙傛暟boolen鍊�
	 * 
	 * @param key
	 * @return
	 */
	public final boolean GetParaBoolen(String key) {
		return getAtPara().GetValBoolenByKey(key);
	}

	/**
	 * 鑾峰彇鍙傛暟boolen鍊�
	 * 
	 * @param key
	 * @param IsNullAsVal
	 * @return
	 */
	public final boolean GetParaBoolen(String key, boolean IsNullAsVal) {
		return getAtPara().GetValBoolenByKey(key, IsNullAsVal);
	}

	/**
	 * 璁剧疆鍙傛暟
	 * 
	 * @param key
	 * @param obj
	 */
	public final void SetPara(String key, String obj) {
		if (getAtPara() != null) {
			this.getRow().remove("_ATObj_");
		}

		String atParaStr = this.GetValStringByKey("AtPara");
		if (!atParaStr.contains("@" + key + "=")) {
			atParaStr += "@" + key + "=" + obj;
			this.SetValByKey("AtPara", atParaStr);
			return;
		} else {
			AtPara at = new AtPara(atParaStr);
			at.SetVal(key, obj);
			this.SetValByKey("AtPara", at.GenerAtParaStrs());
			return;
		}
	}

	public final void SetPara(String key, int obj) {
		SetPara(key, (new Integer(obj)).toString());
	}

	public final void SetPara(String key, float obj) {
		SetPara(key, (new Float(obj)).toString());
	}

	public final void SetPara(String key, boolean obj) {
		if (!obj) {
			SetPara(key, "0");
		} else {
			SetPara(key, "1");
		}
	}

	// 閫氱敤鏂规硶
	/**
	 * 鑾峰彇瀹炰綋
	 * 
	 * @param key
	 */
	public final Object GetRefObject(String key) {
		return this.getRow().GetValByKey("_" + key);

	}

	/**
	 * 璁剧疆瀹炰綋
	 * 
	 * @param key
	 * @param obj
	 */
	public final void SetRefObject(String key, Object obj) {
		if (obj == null) {
			return;
		}

		this.getRow().SetValByKey("_" + key, obj);
	}

	// insert
	/**
	 * 鍦ㄦ彃鍏ヤ箣鍓嶈鍋氱殑宸ヤ綔銆�
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean beforeInsert() throws Exception {
		return true;
	}

	protected boolean roll() {
		return true;
	}

	/**
	 * Insert .
	 * 
	 * @throws Exception
	 */
	public int Insert() throws Exception {

		if (!this.beforeInsert()) {
			return 0;
		}

		if (!this.beforeUpdateInsertAction()) {
			return 0;
		}

		int i = 0;
		try {
			i = this.DirectInsert();
		} catch (RuntimeException ex) {
			this.CheckPhysicsTable();
			throw ex;
		}

		this.afterInsert();
		this.afterInsertUpdateAction();

		return i;
	}

	protected void afterInsert() throws Exception {

		// added by liuxc,2016-02-19,鏂板缓鏃讹紝鏂板涓�涓増鏈褰�
		if (this.getEnMap().IsEnableVer == false)
			return;

		// 澧炲姞鐗堟湰涓�1鐨勭増鏈巻鍙茶褰�
		String enName = this.toString();
		String rdt = BP.DA.DataType.getCurrentDataTime();

		// edited by
		// liuxc,2017-03-24,澧炲姞鍒ゆ柇锛屽鏋滅浉鍚屼富閿殑鏁版嵁鏇捐鍒犻櫎鎺夛紝鍐嶆琚鍔犳椂锛屼細寤剁画琚垹闄ゆ椂鐨勭増鏈紝鍘熸湁閫昏緫鎶ラ敊
		EnVer ver = new EnVer();
		ver.setMyPK(enName + "_" + this.getPKVal());

		if (ver.RetrieveFromDBSources() == 0) {
			ver.setNo(enName);
			ver.setPKValue(this.getPKVal().toString());
			ver.setName(this.getEnMap().getEnDesc());
		} else {
			ver.setEVer(ver.getEVer() + 1);
		}

		ver.setRDT(rdt);
		ver.setRec(BP.Web.WebUser.getName());
		ver.Save();

		// 淇濆瓨瀛楁鏁版嵁.
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs.ToJavaList()) {
			if (attr.getIsRefAttr()) {
				continue;
			}

			EnVerDtl dtl = new EnVerDtl();
			dtl.setEnVerPK(ver.getMyPK());
			dtl.setEnVer(ver.getEVer());
			dtl.setEnName(ver.getNo());
			dtl.setAttrKey(attr.getKey());
			dtl.setAttrName(attr.getDesc());
			// dtl.OldVal = this.GetValStrByKey(attr.Key); //绗竴涓増鏈椂锛屾棫鍊兼病鏈�
			dtl.setRDT(rdt);
			dtl.setRec(BP.Web.WebUser.getName());
			dtl.setNewVal(this.GetValStrByKey(attr.getKey()));
			dtl.setMyPK(ver.getMyPK() + "_" + attr.getKey() + "_" + dtl.getEnVer());
			dtl.Insert();
		}

	}

	/**
	 * 鍦ㄦ洿鏂颁笌鎻掑叆涔嬪悗瑕佸仛鐨勫伐浣�.
	 * 
	 * @throws Exception
	 */
	protected void afterInsertUpdateAction() throws Exception {
		return;
	}

	/**
	 * 浠庝竴涓壇鏈笂copy. 鐢ㄤ簬涓や釜鏁版�у熀鏈浉杩戠殑 瀹炰綋 copy.
	 * 
	 * @param fromEn
	 */
	public void Copy(Entity fromEn) {
		for (Attr attr : this.getEnMap().getAttrs()) {
			// if (attr.IsPK)
			// continue;

			Object obj = fromEn.GetValByKey(attr.getKey());
			if (obj == null) {
				continue;
			}

			this.SetValByKey(attr.getKey(), obj);
		}
	}

	/**
	 * 浠庝竴涓壇鏈笂
	 * 
	 * @param fromRow
	 */
	public void Copy(Row fromRow) {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			try {
				this.SetValByKey(attr.getKey(), fromRow.GetValByKey(attr.getKey()));
			} catch (java.lang.Exception e) {
			}
		}
	}

	public void Copy(XmlEn xmlen) {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			Object obj = null;
			try {
				obj = xmlen.GetValByKey(attr.getKey());
			} catch (java.lang.Exception e) {
				continue;
			}

			if (obj == null || obj.toString().equals("")) {
				continue;
			}
			this.SetValByKey(attr.getKey(), xmlen.GetValByKey(attr.getKey()));
		}
	}

	/**
	 * 澶嶅埗 Hashtable
	 * 
	 * @param ht
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void Copy(Hashtable ht) {
		/*
		 * warning for (String k : ht.keySet())
		 */
		Set<String> keys = ht.keySet();
		for (String k : keys) {
			Object obj = null;
			try {
				obj = ht.get(k);
			} catch (java.lang.Exception e) {
				continue;
			}

			if (obj == null || obj.toString().equals("")) {
				continue;
			}
			this.SetValByKey(k, obj);
		}
	}

	public void Copy(DataRow dr) {
		for (Attr attr : this.getEnMap().getAttrs()) {
			try {
				this.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
				/*
				 * warning this.SetValByKey(attr.getKey(), dr[attr.getKey()]);
				 */
			} catch (java.lang.Exception e) {
			}
		}
	}

	public final String Copy(String refDoc) {
		for (Attr attr : this.get_enMap().getAttrs()) {
			refDoc = refDoc.replace("@" + attr.getKey(), this.GetValStrByKey(attr.getKey()));
		}
		return refDoc;
	}

	public final void Copy() {
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (!attr.getIsPK()) {
				continue;
			}

			if (attr.getMyDataType() == DataType.AppInt) {
				this.SetValByKey(attr.getKey(), 0);
			} else {
				this.SetValByKey(attr.getKey(), "");
			}
		}

		try {
			this.SetValByKey("No", "");
		} catch (java.lang.Exception e) {
		}
	}

	// verify
	/**
	 * 鏍￠獙鏁版嵁
	 * 
	 * @return
	 */
	public final boolean verifyData() {
		return true;
	}

	// 鏇存柊锛屾彃鍏ヤ箣鍓嶇殑宸ヤ綔銆�
	protected boolean beforeUpdateInsertAction() throws Exception {
		switch (this.getEnMap().getEnType()) {
		case View:
		case XML:
		case Ext:
			return false;
		default:
			break;
		}

		this.verifyData();
		return true;
	}

	// 鏇存柊锛屾彃鍏ヤ箣鍓嶇殑宸ヤ綔銆�

	// 鏇存柊鎿嶄綔
	/**
	 * 鏇存柊
	 * 
	 * @return
	 * @throws Exception
	 */
	public int Update() throws Exception {
		return this.Update(null);
	}

	/**
	 * 浠呬粎鏇存柊涓�涓睘鎬�
	 * 
	 * @param key1
	 *            key1
	 * @param val1
	 *            val1
	 * @return 鏇存柊鐨勪釜鏁�
	 * @throws Exception
	 */
	public final int Update(String key1, Object val1) throws Exception {
		this.SetValByKey(key1, val1);
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);

		key1 = key1 + "," + key2;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3)
			throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);

		key1 = key1 + "," + key2 + "," + key3;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4,
			Object val4) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		key1 = key1 + "," + key2 + "," + key3 + "," + key4;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4,
			Object val4, String key5, Object val5) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		this.SetValByKey(key5, val5);

		key1 = key1 + "," + key2 + "," + key3 + "," + key4 + "," + key5;
		return this.Update(key1.split("[,]", -1));
	}

	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4,
			Object val4, String key5, Object val5, String key6, Object val6) throws Exception {
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		this.SetValByKey(key5, val5);
		this.SetValByKey(key6, val6);
		key1 = key1 + "," + key2 + "," + key3 + "," + key4 + "," + key5 + "," + key6;
		return this.Update(key1.split("[,]", -1));
	}

	protected boolean beforeUpdate() throws Exception {
		return true;
	}

	/**
	 * 鏇存柊瀹炰綋
	 * 
	 * @throws Exception
	 */
	public final int Update(String[] keys) throws Exception {
		String str = "";
		try {
			str = "@鏇存柊涔嬪墠鍑虹幇閿欒 ";
			if (!this.beforeUpdate()) {
				return 0;
			}

			str = "@鏇存柊鎻掑叆涔嬪墠鍑虹幇閿欒";
			if (!this.beforeUpdateInsertAction()) {
				return 0;
			}

			str = "@鏇存柊鏃跺嚭鐜伴敊璇�";
			int i = EntityDBAccess.Update(this, keys);
			str = "@鏇存柊涔嬪悗鍑虹幇閿欒";

			this.afterUpdate();
			str = "@鏇存柊鎻掑叆涔嬪悗鍑虹幇閿欒";
			this.afterInsertUpdateAction();
			return i;
		} catch (RuntimeException ex) {

			if (ex.getMessage().contains("灏嗘埅鏂瓧绗︿覆") && ex.getMessage().contains("缂哄皯")) {
				// 璇存槑瀛楃涓查暱搴︽湁闂.
				try {
					this.CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 姣旇緝鍙傛暟閭ｄ釜瀛楁闀垮害鏈夐棶棰�
				String errs = "";
				for (Attr attr : this.getEnMap().getAttrs()) {
					if (attr.getMyDataType() != BP.DA.DataType.AppString) {
						continue;
					}

					if (attr.getMaxLength() < this.GetValStrByKey(attr.getKey()).length()) {
						errs += "@鏄犲皠閲岄潰鐨�" + attr.getKey() + "," + attr.getDesc() + ", 鐩稿浜庤緭鍏ョ殑鏁版嵁:{"
								+ this.GetValStrByKey(attr.getKey()) + "}, 澶暱銆�";
					}
				}

				if (!errs.equals("")) {
					throw new RuntimeException(
							"@鎵ц鏇存柊[" + this.toString() + "]鍑虹幇閿欒@閿欒瀛楁:" + errs + " <br>娓呬綘鍦ㄦ彁浜や竴娆°��" + ex.getMessage());
				} else {
					throw ex;
				}
			}

			Log.DefaultLogWriteLine(LogType.Error, ex.getMessage());
			if (SystemConfig.getIsDebug()) {
				throw new RuntimeException("@[" + this.getEnDesc() + "]鏇存柊鏈熼棿鍑虹幇閿欒:" + str + ex.getMessage());
			} else {
				throw ex;
			}
		}
	}

	protected void afterUpdate() {
		return;
	}

	// 瀵规枃浠剁殑澶勭悊. add by qin 15/10/31
	public final void SaveBigTxtToDB(String saveToField, String bigTxt) throws Exception {
		String temp = BP.Sys.SystemConfig.getPathOfTemp() + "/" + this.getEnMap().getPhysicsTable() + this.getPKVal()
				+ ".tmp";
		DataType.WriteFile(temp, bigTxt);

		// 鍐欏叆鏁版嵁搴�.
		SaveFileToDB(saveToField, temp);
	}

	/**
	 * 淇濆瓨鏂囦欢鍒版暟鎹簱
	 * 
	 * @param saveToField
	 *            瑕佷繚瀛樼殑瀛楁
	 * @param filefullName
	 *            鏂囦欢璺緞
	 * @throws Exception
	 */
	public final void SaveFileToDB(String saveToField, String filefullName) throws Exception {
		try {
			BP.DA.DBAccess.SaveFileToDB(filefullName, this.getEnMap().getPhysicsTable(), this.getPK(),
					this.getPKVal().toString(), saveToField);
		} catch (RuntimeException ex) {
			// 涓轰簡闃叉浠讳綍鍙兘鍑虹幇鐨勬暟鎹涪澶遍棶棰橈紝鎮ㄥ簲璇ュ厛浠旂粏妫�鏌ユ鑴氭湰锛岀劧鍚庡啀鍦ㄦ暟鎹簱璁捐鍣ㄧ殑涓婁笅鏂囦箣澶栬繍琛屾鑴氭湰銆�
			String sql = "";
			if (BP.DA.DBAccess.getAppCenterDBType().equals(DBType.MSSQL)) {
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Image NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType().equals(DBType.Oracle)) {
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Blob NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType().equals(DBType.MySQL)) {
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " MediumBlob NULL ";
			}

			BP.DA.DBAccess.RunSQL(sql);

			throw new RuntimeException("@淇濆瓨鏂囦欢鏈熼棿鍑虹幇閿欒锛屾湁鍙兘璇ュ瓧娈垫病鏈夎鑷姩鍒涘缓锛岀幇鍦ㄥ凡缁忔墽琛屽垱寤轰慨澶嶆暟鎹〃锛岃閲嶆柊鎵ц涓�娆�." + ex.getMessage());
		}
	}

	/**
	 * 浠庤〃鐨勫瓧娈甸噷璇诲彇鏂囦欢
	 * 
	 * @param saveToField
	 *            瀛楁
	 * @param filefullName
	 *            鏂囦欢璺緞,濡傛灉涓虹┖鎬庝笉淇濆瓨鐩存帴杩斿洖鏂囦欢娴侊紝濡傛灉涓嶄负绌哄垯鍒涘缓鏂囦欢銆�
	 * @return 杩斿洖鏂囦欢娴�
	 * @throws IOException
	 */
	public final byte[] GetFileFromDB(String saveToField, String filefullName) throws IOException {
		BP.DA.DBAccess.GetFileFromDB(filefullName, this.getEnMap().getPhysicsTable(), this.getPK(),
				this.getPKVal().toString(), saveToField);
		return null;
	}

	/**
	 * 浠庤〃鐨勫瓧娈甸噷璇诲彇string
	 * 
	 * @param imgFieldName
	 *            瀛楁鍚�
	 * @return 澶ф枃鏈暟鎹�
	 * @throws IOException
	 */
	public final String GetBigTextFromDB(String imgFieldName) throws IOException {
		String tempFile = BP.Sys.SystemConfig.getPathOfTemp() + "/" + this.getEnMap().getPhysicsTable()
				+ this.getPKVals() + ".tmp";

		File file = new File(tempFile);
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return BP.DA.DBAccess.GetTextFileFromDB(tempFile, this.getEnMap().getPhysicsTable(), this.getPK(),
				this.getPKVal().toString(), imgFieldName);
	}

	/**
	 * 浠庤〃鐨勫瓧娈甸噷璇诲彇string
	 * 
	 * @param imgFieldName
	 *            瀛楁鍚�
	 * @return 澶ф枃鏈暟鎹�
	 * @throws IOException
	 */
	public final String GetBigTextFromDB(String imgFieldName, String codeType) throws IOException {
		String tempFile = BP.Sys.SystemConfig.getPathOfTemp() + "/" + this.getEnMap().getPhysicsTable()
				+ this.getPKVals() + ".tmp";

		File file = new File(tempFile);
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return BP.DA.DBAccess.GetTextFileFromDB(tempFile, this.getEnMap().getPhysicsTable(), this.getPK(),
				this.getPKVal().toString(), imgFieldName, codeType);
	}

	// 瀵规枃浠剁殑澶勭悊. add by qin 15/10/31

	public int Save() throws Exception {

		if (this.getPK().equals("OID")) {
			if (this.GetValIntByKey("OID") == 0) {
				// this.SetValByKey("OID",EnDA.GenerOID());
				this.Insert();
				return 1;
			}
			this.Update();

			return 1;
		}
		if (this.getPK().equals("MyPK") || this.getPK().equals("No") || this.getPK().equals("WorkID")
				|| this.getPK().equals("NodeID")) {
			// 鑷姩鐢熸垚鐨凪YPK锛屾彃鍏ュ墠鑾峰彇涓婚敭
			String pk = this.GetValStrByKey(this.getPK());
			if (pk.equals("") || pk == null) {
				this.Insert();
				return 1;
			}

			int i = this.Update();
			if (i == 0) {
				this.Insert();
				return 1;
			}

			return i;

		}

		if (this.Update() == 0) {
			this.Insert();
		}
		return 1;
	}

	/**
	 * 寤虹珛鐗╃悊琛�
	 */
	protected final void CreatePhysicsTable() {

		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:
			DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfOra_OK(this));
			break;
		case MSSQL:
			DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfMS(this));
			break;
		case MySQL:
			DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfMySQL(this));
			break;
		default:
			throw new RuntimeException("@鏈垽鏂殑鏁版嵁搴撶被鍨嬨��");
		}
		this.CreateIndexAndPK();
	}

	private void CreateIndexAndPK() {

		// 寤虹珛涓婚敭.
		int pkconut = this.getPKCount();

		if (pkconut == 1) {
			DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), this.getPK(),
					this.getEnMap().getEnDBUrl().getDBType());

			DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), this.getPK());
		} else if (pkconut == 2) {
			String pk0 = this.getPKs()[0];
			String pk1 = this.getPKs()[1];
			DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), pk0, pk1, this.getEnMap().getEnDBUrl().getDBType());
			DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1);

		} else if (pkconut == 3) {

			String pk0 = this.getPKs()[0];
			String pk1 = this.getPKs()[1];
			String pk2 = this.getPKs()[2];
			DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2,
					this.getEnMap().getEnDBUrl().getDBType());
			DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2);

		}
	}

	private void CheckPhysicsTable_SQL() throws Exception {

		String table = this.get_enMap().getPhysicsTable();
		DBType dbtype = this.get_enMap().getEnDBUrl().getDBType();
		String sqlFields = "";
		String sqlYueShu = "";

		sqlFields = "SELECT column_name as FName,data_type as FType,CHARACTER_MAXIMUM_LENGTH as FLen from information_schema.columns where table_name='"
				+ this.getEnMap().getPhysicsTable() + "'";
		sqlYueShu = "SELECT b.name, a.name FName from sysobjects b join syscolumns a on b.id = a.cdefault where a.id = object_id('"
				+ this.getEnMap().getPhysicsTable() + "') ";

		DataTable dtAttr = DBAccess.RunSQLReturnTable(sqlFields);
		DataTable dtYueShu = DBAccess.RunSQLReturnTable(sqlYueShu);

		// 淇琛ㄥ瓧娈点��
		Attrs attrs = this.get_enMap().getAttrs();

		for (Attr attr : attrs) {
			if (attr.getIsRefAttr()) {
				continue;
			}

			String FType = "";
			String Flen = "";

			// 鍒ゆ柇鏄惁瀛樺湪.
			boolean isHave = false;
			for (DataRow dr : dtAttr.Rows) {
				if (dr.getValue("FName").toString().toLowerCase().equals(attr.getField().toLowerCase())) {
					isHave = true;
					FType = (String) ((dr.getValue("FType") instanceof String) ? dr.getValue("FType") : null);
					Flen = dr.getValue("FLen") == null ? "0" : dr.getValue("FLen").toString();
					break;
				}

			}

			if (isHave == false) {
				// 涓嶅瓨鍦ㄦ鍒� , 灏卞鍔犳鍒椼��
				switch (attr.getMyDataType()) {
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					int len = attr.getMaxLength();
					if (len == 0) {
						len = 200;
					}
					// throw new Exception("灞炴�х殑鏈�灏忛暱搴︿笉鑳戒负0銆�");
					if (dbtype == DBType.Access && len >= 254) {

						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
								+ "  Memo DEFAULT '" + attr.getDefaultVal() + "' NULL");

					} else {

						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
								+ " NVARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");

					}
					continue;
				case DataType.AppInt:
					if (attr.getIsPK()) {
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
								+ " INT DEFAULT '" + attr.getDefaultVal() + "' NOT NULL");
					} else

					{
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
								+ " INT DEFAULT '" + attr.getDefaultVal() + "'   NULL");
					}
					continue;
				case DataType.AppBoolean:

					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " INT DEFAULT '" + attr.getDefaultVal() + "'   NULL");

					continue;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppRate:
				case DataType.AppDouble:

					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");

					continue;
				default:
					throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
				}
			}

			if (isHave == false)
				continue;

			// 妫�鏌ョ被鍨嬫槸鍚﹀尮閰�.
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				if (FType.toLowerCase().contains("char")) {
					if (attr.getIsPK())
						continue;

					// 绫诲瀷姝ｇ‘锛屾鏌ラ暱搴�
					if (Flen == null) {
						throw new RuntimeException("" + attr.getKey() + " -" + sqlFields);
					}
					int len = Integer.parseInt(Flen);
					if (len < attr.getMaxLength()) {
						try {
							if (attr.getMaxLength() >= 4000) {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column  "
										+ attr.getKey() + " NVARCHAR(4000)");
							} else {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column  "
										+ attr.getKey() + " NVARCHAR(" + attr.getMaxLength() + ")");
							}
						} catch (java.lang.Exception e) {

							// 濡傛灉绫诲瀷涓嶅尮閰嶏紝灏卞垹闄ゅ畠鍦ㄩ噸鏂板缓, 鍏堝垹闄ょ害鏉燂紝鍦ㄥ垹闄ゅ垪锛屽湪閲嶅缓銆�
							for (DataRow dr : dtYueShu.Rows) {
								if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {

									DBAccess.RunSQL(
											"alter table " + table + " drop constraint " + dr.getValue(0).toString());
								}
							}

							// 鍦ㄦ墽琛屼竴閬�.
							if (attr.getMaxLength() >= 4000) {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column "
										+ attr.getKey() + " NVARCHAR(4000)");
							} else {
								DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " ALTER column  "
										+ attr.getKey() + " NVARCHAR(" + attr.getMaxLength() + ")");
							}
						}
					}
				} else {
					// 濡傛灉绫诲瀷涓嶅尮閰嶏紝灏卞垹闄ゅ畠鍦ㄩ噸鏂板缓, 鍏堝垹闄ょ害鏉燂紝鍦ㄥ垹闄ゅ垪锛屽湪閲嶅缓銆�
					for (DataRow dr : dtYueShu.Rows) {
						if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
							DBAccess.RunSQL("alter table " + table + " drop constraint " + dr.getValue(0).toString());
						}

					}

					DBAccess.RunSQL(
							"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " drop column " + attr.getField());
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " NVARCHAR(" + attr.getMaxLength() + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
					continue;
				}
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				if (!FType.equals("int")) {
					// 濡傛灉绫诲瀷涓嶅尮閰嶏紝灏卞垹闄ゅ畠鍦ㄩ噸鏂板缓, 鍏堝垹闄ょ害鏉燂紝鍦ㄥ垹闄ゅ垪锛屽湪閲嶅缓銆�
					for (DataRow dr : dtYueShu.Rows) {
						if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
							DBAccess.RunSQL("alter table " + table + " drop constraint " + dr.getValue(0).toString());
						}
						/*
						 * warning if
						 * (dr["FName"].toString().toLowerCase().equals
						 * (attr.getKey().toLowerCase())) {
						 * DBAccess.RunSQL("alter table " + table +
						 * " drop constraint " + dr.getValue(0).toString()); }
						 */
					}
					DBAccess.RunSQL(
							"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " drop column " + attr.getField());
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " INT DEFAULT '" + attr.getDefaultVal() + "' NULL");
					continue;
				}
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				if (!FType.equals("float")) {
					// 濡傛灉绫诲瀷涓嶅尮閰嶏紝灏卞垹闄ゅ畠鍦ㄩ噸鏂板缓, 鍏堝垹闄ょ害鏉燂紝鍦ㄥ垹闄ゅ垪锛屽湪閲嶅缓銆�
					for (DataRow dr : dtYueShu.Rows) {
						if (dr.getValue("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase())) {
							DBAccess.RunSQL("alter table " + table + " drop constraint " + dr.getValue(0).toString());
						}

					}
					DBAccess.RunSQL(
							"ALTER TABLE " + this.getEnMap().getPhysicsTable() + " drop column " + attr.getField());
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
					continue;
				}
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

	}

	/**
	 * 妫�鏌ョ墿鐞嗚〃
	 * 
	 * @throws Exception
	 */
	public final void CheckPhysicsTable() throws Exception {

		this.set_enMap(this.getEnMap());

		Map map = this.getEnMap();

		if (map.getEnType() == EnType.View || map.getEnType() == EnType.XML || map.getEnType() == EnType.ThirdPartApp
				|| map.getEnType() == EnType.Ext) {
			return;
		}

		if (DBAccess.IsExitsObject(this.get_enMap().getPhysicsTable()) == false) {
			// 濡傛灉鐗╃悊琛ㄤ笉瀛樺湪灏辨柊寤虹珛涓�涓墿鐞嗚〃銆�
			this.CreatePhysicsTable();
			return;
		}

		switch (SystemConfig.getAppCenterDBType()) {
		case MSSQL:
			this.CheckPhysicsTable_SQL();
			break;
		case Oracle:
			this.CheckPhysicsTable_Ora();
			break;
		case MySQL:
			this.CheckPhysicsTable_MySQL();
			break;
		case Informix:
			this.CheckPhysicsTable_Informix();
			break;
		default:
			break;
		}

		this.CreateIndexAndPK();

	}

	private void CheckPhysicsTable_Informix() throws Exception {
		// 妫�鏌ュ瓧娈垫槸鍚﹀瓨鍦�
		String sql = "SELECT *  FROM " + this.getEnMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		// 濡傛灉涓嶅瓨鍦�.
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			if (dt.Columns.contains(attr.getKey())) {
				continue;
			}

			if (attr.getKey().equals("AID")) {
				// 鑷姩澧為暱鍒�
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT  Identity(1,1)");
				continue;
			}

			// 涓嶅瓨鍦ㄦ鍒� , 灏卞鍔犳鍒椼��
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				int len = attr.getMaxLength();
				if (len == 0) {
					len = 200;
				}

				if (len >= 254) {
					DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " add " + attr.getField()
							+ " lvarchar(" + len + ") default '" + attr.getDefaultVal() + "'");
				} else {
					DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " add " + attr.getField()
							+ " varchar(" + len + ") default '" + attr.getDefaultVal() + "'");
				}
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT8 DEFAULT " + attr.getDefaultVal() + " ");
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " FLOAT DEFAULT  " + attr.getDefaultVal() + " ");
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

		// 妫�鏌ュ瓧娈甸暱搴︽槸鍚︾鍚堟渶浣庤姹�
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat
					|| attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney
					|| attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppRate) {
				continue;
			}

			dt = new DataTable();
			sql = "select c.*  from syscolumns c inner join systables t on c.tabid = t.tabid where t.tabname = lower('"
					+ this.getEnMap().getPhysicsTable().toLowerCase() + "') and c.colname = lower('" + attr.getKey()
					+ "') and c.collength < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0) {
				continue;
			}
			for (DataRow dr : dt.Rows) {
				try {
					if (attr.getMaxLength() >= 255) {
						this.RunSQL("alter table " + dr.getValue("owner") + "." + this.getEnMap().getPhysicsTableExt()
								+ " modify " + attr.getField() + " lvarchar(" + attr.getMaxLength() + ")");
						/*
						 * warning this.RunSQL("alter table " + dr["owner"] +
						 * "." + this.getEnMap().getPhysicsTableExt() +
						 * " modify " + attr.getField() + " lvarchar(" +
						 * attr.getMaxLength() + ")");
						 */
					} else {
						this.RunSQL("alter table " + dr.getValue("owner") + "." + this.getEnMap().getPhysicsTableExt()
								+ " modify " + attr.getField() + " varchar(" + attr.getMaxLength() + ")");
						/*
						 * warning this.RunSQL("alter table " + dr["owner"] +
						 * "." + this.getEnMap().getPhysicsTableExt() +
						 * " modify " + attr.getField() + " varchar(" +
						 * attr.getMaxLength() + ")");
						 */
					}
				} catch (RuntimeException ex) {
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}

		// 妫�鏌ユ灇涓剧被鍨嬪瓧娈垫槸鍚︽槸INT 绫诲瀷
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}

			// SUNXD 20170714
			// 鐢变簬ALL_TAB_COLUMNS琛ㄤ腑鏈夊彲鑳戒細鍑虹幇鐢ㄦ埛鍚�(owner)涓嶄竴鏍凤紝琛ㄥ悕(table_name)涓�鏍风殑鏁版嵁锛屽鑷充細鍘讳慨鏀瑰叾瀹冪敤鎴蜂笅鐨勮〃
			// 澧炲姞鏌ヨ鏉′欢owner = 褰撳墠绯荤粺閰嶇疆鐨勮繛鎺ョ敤鎴�(SystemConfig.getUser().toUpperCase())

			sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
					+ "' AND TABLE_NAME='" + this.getEnMap().getPhysicsTableExt().toLowerCase() + "' AND COLUMN_NAME='"
					+ attr.getField().toLowerCase() + "'";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null) {
				Log.DefaultLogWriteLineError("@娌℃湁妫�娴嬪埌瀛楁:" + attr.getKey());
			}

			if (val.indexOf("CHAR") != -1) {
				// 濡傛灉瀹冩槸 varchar 瀛楁
				// SUNXD 20170714
				// 鐢变簬ALL_TAB_COLUMNS琛ㄤ腑鏈夊彲鑳戒細鍑虹幇鐢ㄦ埛鍚�(owner)涓嶄竴鏍凤紝琛ㄥ悕(table_name)涓�鏍风殑鏁版嵁锛屽鑷充細鍘讳慨鏀瑰叾瀹冪敤鎴蜂笅鐨勮〃
				// 澧炲姞鏌ヨ鏉′欢owner =
				// 褰撳墠绯荤粺閰嶇疆鐨勮繛鎺ョ敤鎴�(SystemConfig.getUser().toUpperCase())
//				sql = "SELECT OWNER FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
//						+ "' AND  upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase()
//						+ "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
//				String OWNER = DBAccess.RunSQLReturnString(sql);
				try {
					this.RunSQL("alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField()
							+ " NUMBER ");
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError("杩愯sql 澶辫触:alter table  " + this.getEnMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}

		// 妫�鏌ユ灇涓剧被鍨嬫槸鍚﹀瓨鍦�.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			if (attr.UITag == null) {
				continue;
			}
			
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs) {
				if (s.equals("") || s == null) {
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}

		this.CreateIndexAndPK();
	}

	private void CheckPhysicsTable_MySQL() throws Exception {

		// 妫�鏌ュ瓧娈垫槸鍚﹀瓨鍦�
		String sql = "SELECT *  FROM " + this.get_enMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		Map map = this.get_enMap();
		sql = "SELECT character_maximum_length as Len, table_schema as OWNER, column_Name FROM information_schema.columns WHERE TABLE_SCHEMA='"
				+ BP.Sys.SystemConfig.getAppCenterDBDatabase() + "' AND table_name ='" + map.getPhysicsTable() + "'";

		DataTable dtScheam = this.RunSQLReturnTable(sql);

		// 濡傛灉涓嶅瓨鍦�.
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			if (attr.getIsPK())
				continue;

			// 宸茬粡鍖呭惈姝ゅ垪.
			if (dt.Columns.get(attr.getKey().toLowerCase()) != null) {

				if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat
						|| attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney
						|| attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppRate) {
					continue;
				}

				// 鏈�澶ч暱搴�.
				int maxLen = attr.getMaxLength();
				for (DataRow dr : dtScheam.Rows) {

					String name = dr.getValue("column_Name").toString();
					if (name.equals(attr.getKey()) == false)
						continue;
					
					if(dr.getValue("Len") == null || dr.getValue("Len").toString().length() == 0){
						continue;
					}

					String len = dr.getValue("Len").toString();
					
					if (len.equals("") || len==null)
						continue;
					
					
					int lenInt = Integer.parseInt(len);
					if (lenInt >= maxLen)
						continue;
					// 闇�瑕佷慨鏀�.
					this.RunSQL("alter table " + dr.getValue("OWNER") + "." + this.get_enMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NVARCHAR(" + attr.getMaxLength() + ")");
				}
				continue;
			}

			// 涓嶅瓨鍦ㄦ鍒� , 灏卞鍔犳鍒椼��
			switch (attr.getMyDataType()) {
			case DataType.AppString:
				int len = attr.getMaxLength();
				if (len == 0)
					len = 200;

				if (len > 3000) {
					DBAccess.RunSQL(
							"ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " TEXT ");
				} else {
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
							+ " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
				}
				break;
			case DataType.AppDate:
			case DataType.AppDateTime:
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " VARCHAR(20)  NULL");
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT DEFAULT " + attr.getDefaultVal() + " NULL");
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " FLOAT (50,2)  DEFAULT " + attr.getDefaultVal());
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

		// this.CreateIndexAndPK();
	}

	private void CheckPhysicsTable_Ora() throws Exception {
		// 妫�鏌ュ瓧娈垫槸鍚﹀瓨鍦�
		String sql = "SELECT *  FROM " + this.getEnMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		// 濡傛灉涓嶅瓨鍦�.
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			if (dt.Columns.contains(attr.getKey())) {
				continue;
			}

			if (attr.getKey().equals("AID")) {
				// 鑷姩澧為暱鍒�
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT  Identity(1,1)");
				continue;
			}

			// 涓嶅瓨鍦ㄦ鍒� , 灏卞鍔犳鍒椼��
			switch (attr.getMyDataType()) {
			case DataType.AppString:
			case DataType.AppDate:
			case DataType.AppDateTime:
				int len = attr.getMaxLength();
				if (len == 0) {
					len = 200;
				}
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
				break;
			case DataType.AppInt:
			case DataType.AppBoolean:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " INT DEFAULT " + attr.getDefaultVal() + "  NULL");
				break;
			case DataType.AppFloat:
			case DataType.AppMoney:
			case DataType.AppRate:
			case DataType.AppDouble:
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField()
						+ " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
				break;
			default:
				throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}

		// 妫�鏌ュ瓧娈甸暱搴︽槸鍚︾鍚堟渶浣庤姹�
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat
					|| attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney
					|| attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppRate) {
				continue;
			}

			
			dt = new DataTable();
			// SUNXD 20170714
			// 鐢变簬ALL_TAB_COLUMNS琛ㄤ腑鏈夊彲鑳戒細鍑虹幇鐢ㄦ埛鍚�(owner)涓嶄竴鏍凤紝琛ㄥ悕(table_name)涓�鏍风殑鏁版嵁锛屽鑷充細鍘讳慨鏀瑰叾瀹冪敤鎴蜂笅鐨勮〃
			// 澧炲姞鏌ヨ鏉′欢owner = 褰撳墠绯荤粺閰嶇疆鐨勮繛鎺ョ敤鎴�(SystemConfig.getUser().toUpperCase())
			sql = "SELECT DATA_LENGTH AS LEN, OWNER FROM ALL_TAB_COLUMNS WHERE OWNER = '"
					+ SystemConfig.getUser().toUpperCase() + "' AND upper(TABLE_NAME)='"
					+ this.getEnMap().getPhysicsTableExt().toUpperCase() + "' AND UPPER(COLUMN_NAME)='"
					+ attr.getField().toUpperCase() + "' AND DATA_LENGTH < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0) {
				continue;
			}
			for (DataRow dr : dt.Rows) {
				try {
					this.RunSQL("alter table " + dr.getValue("OWNER") + "." + this.getEnMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " varchar2(" + attr.getMaxLength() + ")");
					/*
					 * warning this.RunSQL("alter table " + dr["OWNER"] + "." +
					 * this.getEnMap().getPhysicsTableExt() + " modify " +
					 * attr.getField() + " varchar2(" + attr.getMaxLength() +
					 * ")");
					 */
				} catch (RuntimeException ex) {
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}

		// 妫�鏌ユ灇涓剧被鍨嬪瓧娈垫槸鍚︽槸INT 绫诲瀷
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			// SUNXD 20170714
			// 鐢变簬ALL_TAB_COLUMNS琛ㄤ腑鏈夊彲鑳戒細鍑虹幇鐢ㄦ埛鍚�(owner)涓嶄竴鏍凤紝琛ㄥ悕(table_name)涓�鏍风殑鏁版嵁锛屽鑷充細鍘讳慨鏀瑰叾瀹冪敤鎴蜂笅鐨勮〃
			// 澧炲姞鏌ヨ鏉′欢owner = 褰撳墠绯荤粺閰嶇疆鐨勮繛鎺ョ敤鎴�(SystemConfig.getUser().toUpperCase())

			sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
					+ "' AND upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase()
					+ "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null) {
				Log.DefaultLogWriteLineError("@娌℃湁妫�娴嬪埌瀛楁eunm" + attr.getKey());
			}
			if (val.indexOf("CHAR") != -1) {
				// 濡傛灉瀹冩槸 varchar 瀛楁

				// SUNXD 20170714
				// 鐢变簬ALL_TAB_COLUMNS琛ㄤ腑鏈夊彲鑳戒細鍑虹幇鐢ㄦ埛鍚�(owner)涓嶄竴鏍凤紝琛ㄥ悕(table_name)涓�鏍风殑鏁版嵁锛屽鑷充細鍘讳慨鏀瑰叾瀹冪敤鎴蜂笅鐨勮〃
				// 澧炲姞鏌ヨ鏉′欢owner =
				// 褰撳墠绯荤粺閰嶇疆鐨勮繛鎺ョ敤鎴�(SystemConfig.getUser().toUpperCase())
				/*sql = "SELECT A.OWNER FROM ALL_TAB_COLUMNS WHERE OWNER = '" + SystemConfig.getUser().toUpperCase()
						+ "' AND upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase()
						+ "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
				String OWNER = DBAccess.RunSQLReturnString(sql);*/
				try {
					this.RunSQL("alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField()
							+ " NUMBER ");
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError("杩愯sql 澶辫触:alter table  " + this.getEnMap().getPhysicsTableExt()
							+ " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}

		// 妫�鏌ユ灇涓剧被鍨嬫槸鍚﹀瓨鍦�.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs) {
			if (attr.getMyDataType() != DataType.AppInt) {
				continue;
			}
			if (attr.UITag == null) {
				continue;
			}
			
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs) {
				if (s.equals("") || s == null) {
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}
		this.CreateIndexAndPK();
	}

	public final void AutoFull() throws Exception {
		if (null == this.getPKVal() || this.getPKVal().equals("0") || this.getPKVal().equals("")) {
			return;
		}

		if (!this.getEnMap().getIsHaveAutoFull()) {
			return;
		}

		Attrs attrs = this.getEnMap().getAttrs();
		
		ArrayList<Attr> al = new ArrayList<Attr>();
		for (Attr attr : attrs) {
			if (attr.AutoFullDoc == null || attr.AutoFullDoc.length() == 0) {
				continue;
			}

			// 杩欎釜浠ｇ爜闇�瑕佹彁绾埌鍩虹被涓幓銆�
			switch (attr.autoFullWay) {
			case Way0:
				continue;
			case Way1_JS:
				al.add(attr);
				break;
			case Way2_SQL:
				String sql = attr.AutoFullDoc;
				sql = sql.replace("~", "'");

				sql = sql.replace("WebUser.No", WebUser.getNo());
				sql = sql.replace("@WebUser.Name", WebUser.getName());
				sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				if (sql.contains("@")) {
					Attrs attrs1 = this.getEnMap().getAttrs();
					for (Attr a1 : attrs1) {
						if (!sql.contains("@")) {
							break;
						}

						if (!sql.contains("@" + a1.getKey())) {
							continue;
						}

						if (a1.getIsNum()) {
							sql = sql.replace("@" + a1.getKey(), this.GetValStrByKey(a1.getKey()));
						} else {
							sql = sql.replace("@" + a1.getKey(), "'" + this.GetValStrByKey(a1.getKey()) + "'");
						}
					}
				}

				sql = sql.replace("''", "'");
				String val = "";
				try {
					val = DBAccess.RunSQLReturnString(sql);
				} catch (RuntimeException ex) {
					throw new RuntimeException("@瀛楁(" + attr.getKey() + "," + attr.getDesc()
							+ ")鑷姩鑾峰彇鏁版嵁鏈熼棿閿欒(鏈夊彲鑳芥槸鎮ㄥ啓鐨剆ql璇彞浼氳繑鍥炲鍒楀琛岀殑table,鐜板湪鍙涓�鍒椾竴琛岀殑table鎵嶈兘濉厖锛岃妫�鏌ql.):"
							+ sql.replace("'", "鈥�") + " @Tech Info:" + ex.getMessage().replace("'", "鈥�") + "@鎵ц鐨剆ql:"
							+ sql);
				}

				if (attr.getIsNum()) {
					// 濡傛灉鏄暟鍊肩被鍨嬬殑灏卞皾璇曠潃杞崲鏁板�硷紝杞崲涓嶄簡灏辫窇鍑哄紓甯镐俊鎭��
					try {
						java.math.BigDecimal d = new java.math.BigDecimal(val);
						/*
						 * warning java.math.BigDecimal d =
						 * java.math.BigDecimal.Parse(val);
						 */
					} catch (java.lang.Exception e) {
						throw new RuntimeException(val);
					}
				}
				this.SetValByKey(attr.getKey(), val);
				break;
			case Way3_FK:
				try {
					String sqlfk = "SELECT @Field FROM @Table WHERE No=@AttrKey";
					String[] strsFK = attr.AutoFullDoc.split("[@]", -1);
					for (String str : strsFK) {
						if (str == null || str.length() == 0) {
							continue;
						}

						String[] ss = str.split("[=]", -1);
						if (ss[0].equals("AttrKey")) {
							String tempV = this.GetValStringByKey(ss[1]);
							if (tempV.equals("") || tempV == null) {
								if (!this.getEnMap().getAttrs().Contains(ss[1])) {
									throw new RuntimeException("@鑷姩鑾峰彇鍊间俊鎭笉瀹屾暣,Map 涓凡缁忎笉鍖呭惈Key=" + ss[1] + "鐨勫睘鎬с��");
								}

								// throw new
								// Exception("@鑷姩鑾峰彇鍊间俊鎭笉瀹屾暣,Map 涓凡缁忎笉鍖呭惈Key=" +
								// ss[1] + "鐨勫睘鎬с��");
								sqlfk = sqlfk.replace('@' + ss[0], "'@xxx'");
								Log.DefaultLogWriteLineWarning(
										"@鍦ㄨ嚜鍔ㄥ彇鍊兼湡闂村嚭鐜伴敊璇�:" + this.toString() + " , " + this.getPKVal() + "娌℃湁鑷姩鑾峰彇鍒颁俊鎭��");
							} else {
								sqlfk = sqlfk.replace('@' + ss[0], "'" + this.GetValStringByKey(ss[1]) + "'");
							}
						} else {
							sqlfk = sqlfk.replace('@' + ss[0], ss[1]);
						}
					}

					sqlfk = sqlfk.replace("''", "'");
					this.SetValByKey(attr.getKey(), DBAccess.RunSQLReturnStringIsNull(sqlfk, null));
				} catch (RuntimeException ex) {
					throw new RuntimeException(
							"@鍦ㄥ鐞嗚嚜鍔ㄥ畬鎴愶細澶栭敭[" + attr.getKey() + ";" + attr.getDesc() + "],鏃跺嚭鐜伴敊璇�傚紓甯镐俊鎭細" + ex.getMessage());
				}
				break;
			case Way4_Dtl:
				if (this.getPKVal().equals("0")) {
					continue;
				}

				String mysql = "SELECT @Way(@Field) FROM @Table WHERE RefPK =" + this.getPKVal();
				String[] strs = attr.AutoFullDoc.split("[@]", -1);
				for (String str : strs) {
					if (str == null || str.length() == 0) {
						continue;
					}

					String[] ss = str.split("[=]", -1);
					mysql = mysql.replace('@' + ss[0], ss[1]);
				}

				String v = DBAccess.RunSQLReturnString(mysql);
				if (v == null) {
					v = "0";
				}
				this.SetValByKey(attr.getKey(), new java.math.BigDecimal(v));
				/*
				 * warning this.SetValByKey(attr.getKey(),
				 * java.math.BigDecimal.Parse(v));
				 */

				break;
			default:
				throw new RuntimeException("鏈秹鍙婂埌鐨勭被鍨嬨��");
			}
		}

		// 澶勭悊JS鐨勮绠椼��
		for (Attr attr : al) {
			/*
			 * warning String doc = attr.AutoFullDoc.clone().toString();
			 */
			String doc = new String(attr.AutoFullDoc);
			for (Attr a : attrs) {
				if (a.getKey().equals(attr.getKey())) {
					continue;
				}

				doc = doc.replace("@" + a.getKey(), this.GetValStrByKey(a.getKey()).toString());
				doc = doc.replace("@" + a.getDesc(), this.GetValStrByKey(a.getKey()).toString());
			}

			try {
				java.math.BigDecimal d = DataType.ParseExpToDecimal(doc);
				this.SetValByKey(attr.getKey(), d);
			} catch (RuntimeException ex) {
				Log.DefaultLogWriteLineError("@(" + this.toString() + ")鍦ㄥ鐞嗚嚜鍔ㄨ绠梴" + this.getEnDesc() + "}锛�"
						+ this.getPK() + "=" + this.getPKVal() + "鏃讹紝灞炴�" + attr.getKey() + "]锛岃绠楀唴瀹筟" + doc + "]锛屽嚭鐜伴敊璇細"
						+ ex.getMessage());
				throw new RuntimeException("@(" + this.toString() + ")鍦ㄥ鐞嗚嚜鍔ㄨ绠梴" + this.getEnDesc() + "}锛�" + this.getPK()
						+ "=" + this.getPKVal() + "鏃讹紝灞炴�" + attr.getKey() + "]锛岃绠楀唴瀹筟" + doc + "]锛屽嚭鐜伴敊璇細"
						+ ex.getMessage());
			}
		}

	}

	public final String ToJson() {
		return ToJson(true);
	}

	/**
	 * 鎶婁竴涓疄浣撹浆鍖栨垚Json.
	 * 
	 * @return 杩斿洖涓�涓猻tring json涓�.
	 */
	public final String ToJson(Boolean isInParaFields) {
		Hashtable<String, Object> ht = getRow();

		// 濡傛灉涓嶅寘鍚弬鏁板瓧娈�.
		if (isInParaFields == false) {
			ht.put("EnName", this.toString());
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}

		if (ht.containsKey("AtPara") == true) {
			/* 濡傛灉鍖呭惈杩欎釜瀛楁 */
			AtPara ap = getAtPara();
			for (String key : ap.getHisHT().keySet()) {
				ht.put(key, ap.getHisHT().get(key));
			}

			// 鎶婂弬鏁板睘鎬хЩ闄�.
			ht.remove("_ATObj_");
		}
		return BP.Tools.Json.ToJson(ht, false);
	}

	public final AtPara getatPara() {
		Object tempVar = this.getRow().GetValByKey("_ATObj_");
		AtPara at = (AtPara) ((tempVar instanceof AtPara) ? tempVar : null);
		if (at != null) {
			return at;
		}

		try {
			String atParaStr = this.GetValStringByKey("AtPara");
			if (DataType.IsNullOrEmpty(atParaStr)) {
				// 娌℃湁鍙戠幇鏁版嵁锛屽氨鎵ц鍒濆鍖�.
				this.InitParaFields();

				// 閲嶆柊鑾峰彇涓�娆°��
				atParaStr = this.GetValStringByKey("AtPara");
				if (DataType.IsNullOrEmpty(atParaStr)) {
					atParaStr = "";
				}

				at = new AtPara(atParaStr);
				this.SetValByKey("_ATObj_", at);
				return at;
			}
			at = new AtPara(atParaStr);
			this.SetValByKey("_ATObj_", at);
			return at;
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@鑾峰彇鍙傛暟AtPara鏃跺嚭鐜板紓甯�" + ex.getMessage() + "锛屽彲鑳芥槸鎮ㄦ病鏈夊姞鍏ョ害瀹氱殑鍙傛暟瀛楁AtPara. " + ex.getMessage());
		}
	}

	/**
	 * 鎶奺ntity鐨勫疄浣撳睘鎬ц皟搴﹀埌en閲岄潰鍘�.
	 * 
	 * @param fk_mapdata
	 * @return
	 */
	public MapData DTSMapToSys_MapData(String fk_mapdata) {

		if (DataType.IsNullOrEmpty(fk_mapdata)) {
			fk_mapdata = this.getClassIDOfShort();
		}

		Map map = this.getEnMap();

		// 鑾峰緱鐭殑绫诲悕绉�.
		// region 鏇存柊涓昏〃淇℃伅.
		MapData md = new MapData();

		try {
			md.setNo(fk_mapdata);
			if (md.RetrieveFromDBSources() == 0)
				md.Insert();

			md.setEnPK(this.getPK());
			md.setEnsName(this.getClassID());
			md.setName(map.getEnDesc());
			md.setPTable(map.getPhysicsTable());
			md.Update();
			// endregion 鏇存柊涓昏〃淇℃伅.

			// 鍚屾灞炴�� mapattr.
			DTSMapToSys_MapData_InitMapAttr(map.getAttrs(), fk_mapdata);

			// region 鍚屾浠庤〃.
			EnDtls dtls = map.getDtls();
			for (EnDtl dtl : dtls.ToJavaList()) {
				MapDtl mdtl = new MapDtl();

				Entity enDtl = dtl.getEns().getGetNewEntity();

				mdtl.setNo(enDtl.getClassIDOfShort());
				if (mdtl.RetrieveFromDBSources() == 0)
					mdtl.Insert();

				mdtl.setName(enDtl.getEnDesc());
				mdtl.setFK_MapData(fk_mapdata);
				mdtl.setPTable(enDtl.getEnMap().getPhysicsTable());
				mdtl.setRefPK(dtl.getRefKey()); // 鍏宠仈鐨勪富閿�.
				mdtl.Update();

				// 鍚屾瀛楁.
				DTSMapToSys_MapData_InitMapAttr(enDtl.getEnMap().getAttrs(), enDtl.getClassIDOfShort());
				// endregion 鍚屾浠庤〃.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md;
	}

	private void DTSMapToSys_MapData_InitMapAttr(Attrs attrs, String fk_mapdata) throws Exception {

		for (Attr attr : attrs.ToJavaList()) {
			if (attr.getIsRefAttr())
				continue;

			MapAttr mattr = new MapAttr();
			mattr.setKeyOfEn(attr.getKey());
			mattr.setFK_MapData(fk_mapdata);
			mattr.setMyPK(mattr.getFK_MapData() + "_" + mattr.getKeyOfEn());
			mattr.RetrieveFromDBSources();

			mattr.setName(attr.getDesc());
			mattr.setDefVal(attr.getDefaultVal().toString());
			mattr.setKeyOfEn(attr.getField());

			mattr.setMaxLen(attr.getMaxLength());
			mattr.setMinLen(attr.getMinLength());
			mattr.setUIBindKey(attr.getUIBindKey());
			mattr.setUIIsLine(attr.UIIsLine);
			mattr.setUIHeight(0);

			if (attr.getMaxLength() > 3000)
				mattr.setUIHeight(10);

			mattr.setUIWidth(attr.getUIWidth());
			mattr.setMyDataType(attr.getMyDataType());

			mattr.setUIRefKey(attr.getUIRefKeyValue());

			mattr.setUIRefKeyText(attr.getUIRefKeyText());
			mattr.setUIVisible(attr.getUIVisible());

			switch (attr.getMyFieldType()) {
			case Enum:
			case PKEnum:
				mattr.setUIContralType(attr.getUIContralType());
				mattr.setLGType(FieldTypeS.Enum);
				mattr.setUIIsEnable(attr.getUIIsReadonly());
				break;
			case FK:
			case PKFK:
				mattr.setUIContralType(attr.getUIContralType());
				mattr.setLGType(FieldTypeS.FK);
				// attr.MyDataType = (int)FieldType.FK;
				mattr.setUIRefKey("No");
				mattr.setUIRefKeyText("Name");
				mattr.setUIIsEnable(attr.getUIIsReadonly());
				break;
			default:
				mattr.setUIContralType(UIContralType.TB);
				mattr.setLGType(FieldTypeS.Normal);
				mattr.setUIIsEnable(!attr.getUIIsReadonly());
				switch (attr.getMyDataType()) {
				case DataType.AppBoolean:
					mattr.setUIContralType(UIContralType.CheckBok);
					mattr.setUIIsEnable(attr.getUIIsReadonly());
					break;
				case DataType.AppDate:
					// if (this.Tag == "1")
					// attr.DefaultVal = DataType.CurrentData;
					break;
				case DataType.AppDateTime:
					// if (this.Tag == "1")
					// attr.DefaultVal = DataType.CurrentData;
					break;
				default:
					break;
				}
				break;
			}

			mattr.Save();

		}
	}

	public String getClassIDOfShort() {
		String clsID = this.getClassID();
		return clsID.substring(clsID.lastIndexOf('.') + 1);
	}

	public final String getHisDBVarStr() {
		return SystemConfig.getAppCenterDBVarStr();
	}

	/**
	 * 浠栫殑璁块棶鎺у埗.
	 */
	protected UAC _HisUAC = null;

	/**
	 * 寰楀埌 uac 鎺у埗.
	 * 
	 * @return
	 * @throws Exception
	 */
	public UAC getHisUAC() throws Exception {
		if (_HisUAC == null) {

			_HisUAC = new UAC();

			if (BP.Web.WebUser.getNo().equals("admin")) {
				_HisUAC.IsAdjunct = false;
				_HisUAC.IsDelete = true;
				_HisUAC.IsInsert = true;
				_HisUAC.IsUpdate = true;
				_HisUAC.IsView = true;
			}

		}
		return _HisUAC;
	}

	public String toString() {
		return this.getClass().getName();
	}

	// CreateInstance
	/**
	 * 鍒涘缓涓�涓疄渚�
	 * 
	 * @return 鑷韩鐨勫疄渚�
	 */
	public final Entity CreateInstance() {
		/*
		 * warning Object tempVar =
		 * this.getClass().Assembly.CreateInstance(this.toString());
		 */
		Object tempVar = null;
		try {
			tempVar = this.getClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Entity) ((tempVar instanceof Entity) ? tempVar : null);
		// return ClassFactory.GetEn(this.ToString());
	}

	private final void ResetDefaultValRowValues() throws Exception {

		if (this.get_enMap() == null)
			return;

		Attrs attrs = this.get_enMap().getAttrs();
		
		for (Attr attr : attrs) {

			String key = attr.getKey();

			String v = this.GetValStringByKey(key, null);

			if (v == null || v.indexOf('@') == -1)
				continue;

			// 璁剧疆榛樿鍊�.
			if (v.equals("@WebUser.No")) {

				this.SetValByKey(key, WebUser.getNo());

				continue;
			} else if (v.equals("@WebUser.Name")) {

				this.SetValByKey(key, WebUser.getName());

				continue;
			} else if (v.equals("@WebUser.FK_Dept")) {

				this.SetValByKey(key, WebUser.getFK_Dept());

				continue;
			} else if (v.equals("@WebUser.FK_DeptName")) {

				this.SetValByKey(key, WebUser.getFK_DeptName());

				continue;
			} else if (v.equals("@WebUser.FK_DeptNameOfFull")) {

				this.SetValByKey(key, WebUser.getFK_DeptNameOfFull());

				continue;
			} else if (v.equals("@RDT")) {
				if (attr.getMyDataType() == DataType.AppDate) {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
				}

				if (attr.getMyDataType() == DataType.AppDateTime) {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
				}
				continue;
			} else {
				continue;
			}

		}
	}
	
	public final void ResetDefaultVal()throws Exception{
		ResetDefaultVal(this.toString(),null,0);
	}

	// 鏂规硶
	/**
	 * 閲嶆柊璁剧疆榛樹俊鎭�.
	 * 
	 * @throws Exception
	 */
	public final void ResetDefaultVal(String fk_mapdata,String fk_flow,int fk_node) throws Exception {

		ResetDefaultValRowValues();

		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if(attr.getIsRefAttr())
				this.SetValRefTextByKey(attr.getKey(), "");
			
			FrmField frmField = new FrmField();
			int i = 0;
			if(fk_node!=0 && fk_node!=999999)
				i = frmField.Retrieve(FrmFieldAttr.FK_MapData,fk_mapdata,FrmFieldAttr.FK_Flow,Integer.parseInt(fk_flow),FrmFieldAttr.FK_Node,fk_node,FrmFieldAttr.KeyOfEn,attr.getKey());
			
			//鑾峰彇榛樿鍊�
			String v = attr.getDefaultValOfReal();
			if(i==1)
				v = frmField.getDefVal();
			
			if (v== null ||( v != null && v.contains("@") == false))
				continue;
		  
			// 鍚湁鐗瑰畾鍊兼椂鍙栨秷閲嶆柊璁惧畾榛樿鍊�
			String myval = this.GetValStringByKey(attr.getKey()); 

			// 璁剧疆榛樿鍊�.
			if (v.equals("@WebUser.No")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getNo());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getNo());
					}
				}
				continue;
			}
			if (v.equals("@WebUser.Name")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getName());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getName());
					}
				}
				continue;
			} 
			if (v.equals("@WebUser.FK_Dept")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
					}
				}
				continue;
			} 
			if (v.equals("@WebUser.FK_DeptName")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
					}
				}
				continue;
			} 
			if (v.equals("@WebUser.FK_DeptNameOfFull") || v.equals("@WebUser.FK_DeptFullName")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
					}
				}
				continue;
			}
			if (v.equals("@RDT")) {
				if (attr.getUIIsReadonly()) {
					if (attr.getMyDataType() == DataType.AppDate || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
					}

					if (attr.getMyDataType() == DataType.AppDateTime || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
					}
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						if (attr.getMyDataType() == DataType.AppDate) {
							this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
						} else {
							this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
						}
					}
				}
				continue;
			}
			if (v.equals("@FK_ND")){
	             if (attr.getUIIsReadonly() == true)
	             {
	                 this.SetValByKey(attr.getKey(), DataType.getCurrentYear());
	             }
	             else
	             {
	                 if (DataType.IsNullOrEmpty(myval) || myval == v)
	                     this.SetValByKey(attr.getKey(), DataType.getCurrentYear());
	             }
	             continue;
			}
		   if(v.equals("@yyyy骞磎m鏈坉d鏃�") ||v.equals("@yyyy骞磎m鏈坉d鏃H鏃秏m鍒�")
				   || v.equals("@yy骞磎m鏈坉d鏃�")||v.equals("@yy骞磎m鏈坉d鏃H鏃秏m鍒�") ){

             if (attr.getUIIsReadonly() == true)
             {
                 this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart(v.replace("@", "")));
             }
             else
             {
                 if (DataType.IsNullOrEmpty(myval) || myval == v)
                     this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart(v.replace("@", "")));
             }
             continue;
		   }else{
			   GloVar gloVar = new GloVar();
			   gloVar.setNo(v);
               int count = gloVar.RetrieveFromDBSources();
               if (count == 1)
               {
                   //鎵цSQL鑾峰彇榛樿鍊�
                   String sql = gloVar.getVal();
                   sql = BP.WF.Glo.DealExp(sql, null, null);
                   if (DataType.IsNullOrEmpty(myval) || myval == v){
                	   try{
                		  v =  DBAccess.RunSQLReturnString(sql); 
                		  this.SetValByKey(attr.getKey(),v);
                	   }catch(Exception e){
                		   this.SetValByKey(attr.getKey(),e.getMessage()+sql);
                		   
                	   }
                	   
                   }
               }
               continue;
		   }
			
		}
	}

	/**
	 * 鎶婃墍鏈夌殑鍊奸兘璁剧疆鎴愰粯璁ゅ�硷紝浣嗘槸涓婚敭闄ゅ銆�
	 * 
	 * @throws Exception
	 */
	public final void ResetDefaultValAllAttr() throws Exception {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (!attr.getUIIsReadonly() && attr.getDefaultValOfReal() != null) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			String tempVar = attr.getDefaultValOfReal();
			String v = (String) ((tempVar instanceof String) ? tempVar : null);
			if (v == null) {
				this.SetValByKey(attr.getKey(), "");
				continue;
			}

			if (!v.contains("@") || v.equals("0") || v.equals("0.00")) {
				this.SetValByKey(attr.getKey(), v);
				continue;
			}

			// 璁剧疆榛樿鍊�.
			if (v.equals("WebUser.No")) {
				this.SetValByKey(attr.getKey(), WebUser.getNo());
				continue;
			} else if (v.equals("@WebUser.Name")) {
				this.SetValByKey(attr.getKey(), WebUser.getName());
				continue;
			} else if (v.equals("@WebUser.FK_Dept")) {
				this.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
				continue;
			} else if (v.equals("@WebUser.FK_DeptName")) {
				this.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
				continue;
			} else if (v.equals("@WebUser.FK_DeptNameOfFull")) {
				this.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
				continue;
			} else if (v.equals("@RDT")) {
				if (attr.getMyDataType() == DataType.AppDate) {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
				} else {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
				}
				continue;
			} else {
				continue;
			}
		}
	}

	private Map _tmpEnMap = null;

	/**
	 * Map
	 */
	protected final Map get_enMap() {

		if (_tmpEnMap != null) {
			return _tmpEnMap;
		}

		Map obj = Cash.GetMap(this.toString());
		if (obj == null) {
			if (_tmpEnMap == null) {
				return null;
			} else {
				return _tmpEnMap;
			}
		} else {
			_tmpEnMap = obj;
		}
		return _tmpEnMap;
	}

	protected final void set_enMap(Map value) {
		if (value == null) {
			_tmpEnMap = null;
			return;
		}

		Map mp = (Map) value;
		if (mp == null || mp.getDepositaryOfMap() == Depositary.None) {
			_tmpEnMap = mp;
			return;
		}
		Cash.SetMap(this.toString(), mp);
		_tmpEnMap = mp;
	}

	public final void setMap(Map value) {
		set_enMap(value);
	}

	/**
	 * 瀛愮被闇�瑕佺户鎵�
	 */
	public abstract Map getEnMap();

	/**
	 * 鍔ㄦ�佺殑鑾峰彇map
	 */
	public Map getEnMapInTime() {
		_tmpEnMap = null;
		Cash.SetMap(this.getClass().getName(), null);
		return this.getEnMap();
	}

	/**
	 * 瀹炰綋鐨� map 淇℃伅銆�
	 * 
	 * //public abstract void EnMap();
	 */
	private Row _row = null;

	public final Row getRow() {
		if (this._row == null) {

			// this._row.LoadAttrs(this.getEnMap().getAttrs());

			try {
				this._row = this.getSQLCash().getRow();
			} catch (Exception e) {
				this._row.LoadAttrs(this.getEnMap().getAttrs());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// this._row.LoadAttrs(this.getEnMap().getAttrs());
		}
		return this._row;
	}

	public final void setRow(Row value) {
		this._row = value;
	}

	// 涓巗ql鎿嶄綔鏈夊叧
	protected SQLCash _SQLCash = null;

	public SQLCash getSQLCash() throws Exception {
		if (_SQLCash == null) {
			_SQLCash = BP.DA.Cash.GetSQL(this.toString());
			if (_SQLCash == null) {
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL(this.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}

	public void setSQLCash(SQLCash value) {
		_SQLCash = value;
	}

	// 娓呴櫎缂撳瓨SQLCase.
	public void clearSQLCash() {
		BP.DA.Cash.getSQL_Cash().remove(this.toString());
		_SQLCash = null;
	}

	// 鍏充簬灞炴�х殑鎿嶄綔銆�
	/**
	 * 璁剧疆object绫诲瀷鐨勫��
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValByKey(String attrKey, String val) {
		if (val == null) {
			val = "";
		}

		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, int val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, long val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, float val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, java.math.BigDecimal val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, Object val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByDesc(String attrDesc, Object val) {
		if (val == null) {
			throw new RuntimeException("@涓嶈兘璁剧疆灞炴�" + attrDesc + "]null 鍊笺��");
		}
		this.getRow().SetValByKey(this.getEnMap().GetAttrByDesc(attrDesc).getKey(), val);
	}

	/**
	 * 璁剧疆鍏宠仈绫诲瀷鐨勫��
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValRefTextByKey(String attrKey, Object val) {
		this.SetValByKey(attrKey + "Text", val);
	}

	/**
	 * 璁剧疆bool绫诲瀷鐨勫��
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValByKey(String attrKey, boolean val) {
		if (val) {
			this.SetValByKey(attrKey, 1);
		} else {
			this.SetValByKey(attrKey, 0);
		}
	}

	/**
	 * 璁剧疆榛樿鍊�
	 */
	public final void SetDefaultVals() {
		for (Attr attr : this.getEnMap().getAttrs()) {
			this.SetValByKey(attr.getKey(), attr.getDefaultVal());
		}
	}

	/**
	 * 璁剧疆鏃ユ湡绫诲瀷鐨勫��
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetDateValByKey(String attrKey, String val) {
		try {
			this.SetValByKey(attrKey, DataType.StringToDateStr(val));
		} catch (RuntimeException ex) {
			throw new RuntimeException("@涓嶅悎娉曠殑鏃ユ湡鏁版嵁鏍煎紡:key=[" + attrKey + "],value=" + val + " " + ex.getMessage());
		}
	}

	// 鍙栧�兼柟娉�
	/**
	 * 鍙栧緱Object
	 * 
	 * @param attrKey
	 * @return
	 */
	public final Object GetValByKey(String attrKey) {
		return this.getRow().GetValByKey(attrKey);

	}

	/**
	 * GetValDateTime
	 * 
	 * @param attrKey
	 * @return
	 */
	public final java.util.Date GetValDateTime(String attrKey) {
		return DataType.ParseSysDateTime2DateTime(this.GetValStringByKey(attrKey));
	}

	/**
	 * 鍦ㄧ‘瀹� attrKey 瀛樺湪 map 鐨勬儏鍐典笅鎵嶈兘浣跨敤瀹�
	 * 
	 * @param attrKey
	 * @return
	 */
	public final String GetValStrByKey(String key) {
		Object value = this.getRow().GetValByKey(key);
		if (null == value) {
			return "";
		}

		return value.toString();
	}

	public final String GetValStrByKey(String key, String isNullAs) {

		Object obj = this.getRow().get(key);
		if (obj == null)
			return isNullAs;

		return obj.toString();
	}

	/**
	 * 鍙栧緱String
	 * 
	 * @param attrKey
	 * @return
	 */
	public final String GetValStringByKey(String attrKey) {

		String val = GetValStrByKey(attrKey, null);
		if (val == null)
			throw new RuntimeException("@鑾峰彇鍊兼湡闂村嚭鐜板涓嬪紓甯革細  " + attrKey + " 鎮ㄦ病鏈夊湪绫诲鍔犺繖涓睘鎬э紝EnName=" + this.toString());

		return val;
	}

	public final String GetValStringByKey(String attrKey, String isNullAsVal) {

		String val = GetValStrByKey(attrKey, null);
		if (val == null)
			return isNullAsVal;
		return val;
	}

	/**
	 * 鍙栧嚭澶у潡鏂囨湰
	 * 
	 * @return
	 */
	public final String GetValDocText() {
		String s = this.GetValStrByKey("Doc");
		if (s.trim().length() != 0) {
			return s;
		}

		// s = SysDocFile.GetValTextV2(this.toString(),
		// this.getPKVal().toString());
		this.SetValByKey("Doc", s);
		return s;
	}

	public final String GetValDocHtml() {
		String s = this.GetValHtmlStringByKey("Doc");
		if (s.trim().length() != 0) {
			return s;
		}

		s = SysDocFile.GetValHtmlV2(this.toString(), this.getPKVal().toString());
		this.SetValByKey("Doc", s);
		return s;
	}

	/**
	 * 鍙栧埌Html 淇℃伅銆�
	 * 
	 * @param attrKey
	 *            attr
	 * @return html.
	 */
	public final String GetValHtmlStringByKey(String attrKey) {
		return DataType.ParseText2Html(this.GetValStringByKey(attrKey));
	}

	public final String GetValHtmlStringByKey(String attrKey, String defval) {
		return DataType.ParseText2Html(this.GetValStringByKey(attrKey, defval));
	}

	/**
	 * 鍙栧緱鏋氫妇鎴栬�呭閿殑鏍囩 濡傛灉鏄灇涓惧氨鑾峰彇鏋氫妇鏍囩. 濡傛灉鏄閿氨鑾峰彇涓哄閿殑鍚嶇О.
	 * 
	 * @param attrKey
	 * @return
	 */
	public final String GetValRefTextByKey(String attrKey) {

		return GetValStringByKey(attrKey + "Text");

	}

	public long GetValInt64ByKey(String key) {
		String val = this.GetValStringByKey(key, "0");
		if(DataType.IsNullOrEmpty(val))
			val = "0";	
		return Long.parseLong(val);
	}

	public final int GetValIntByKey(String key, int IsZeroAs) {
		int i = this.GetValIntByKey(key);
		if (i == 0) {
			i = IsZeroAs;
		}
		return i;
	}

	/**
	 * 鏍规嵁key 寰楀埌int val
	 * 
	 * @param key
	 * @return
	 */
	public int GetValIntByKey(String key) {
		String val = this.GetValStringByKey(key);
		if (DataType.IsNullOrEmpty(val))
			return 0;

		if (val.endsWith(".0") == true)
			val = val.replace(".0", "");

		return Integer.parseInt(val);
	}

	/**
	 * 鏍规嵁key 寰楀埌 bool val
	 * 
	 * @param key
	 * @return
	 */
	public final boolean GetValBooleanByKey(String key) {
			
		 String s = this.GetValStrByKey(key);
         if (DataType.IsNullOrEmpty(s))
             s = this.getEnMap().GetAttrByKey(key).getDefaultVal().toString();

         if (s == "0")
             return false;
         if (s == "1")
             return true;

         if (s.toUpperCase().equals("FALSE"))
             return false;
         if (s.toUpperCase().equals("TRUE") )
             return true;

         if (DataType.IsNullOrEmpty(s))
             return false;

         if (Integer.parseInt(s) == 0)
             return false;

         return true;
         
         
	}

	public final String GetValBoolStrByKey(String key) {
		if (GetValBooleanByKey(key) == false) {
			return "鍚�";
		} else {
			return "鏄�";
		}
	}

	 public float GetValFloatByKey(String key, int blNum)
     {
         String val = this.GetValStringByKey(key);
         if (DataType.IsNullOrEmpty(val))                
             return blNum;

         return Float.parseFloat(val);
     }
	/**
	 * 鏍规嵁key 寰楀埌flaot val
	 * 
	 * @param key
	 * @return
	 */
	public final float GetValFloatByKey(String key) {

		String val = this.GetValStringByKey(key);
		if (val == null)
			return 0;
		return Float.parseFloat(val);
	}

	/**
	 * 鏍规嵁key 寰楀埌flaot val
	 * 
	 * @param key
	 * @return
	 */
	public final java.math.BigDecimal GetValDecimalByKey(String key) {

		BigDecimal bd = new BigDecimal(this.GetValStrByKey(key));
		return bd.setScale(4, BigDecimal.ROUND_HALF_UP);

	}

	public final double GetValDoubleByKey(String key) {

		String val = this.GetValStrByKey(key);
		if (val == null)
			return 0;
		return Double.parseDouble(val);
	}

	public final boolean getIsBlank() {

		if (this._row == null) {
			return true;
		}

		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {

			if (attr.getUIIsReadonly() && !attr.getIsFKorEnum()) {
				continue;
			}

			String str = this.GetValStrByKey(attr.getKey());
			if (str.equals("") || attr.getDefaultVal().toString().equals(str) || str == null) {
				continue;
			}

			if (attr.getMyDataType() == DataType.AppDate && attr.getDefaultVal() == null) {
				if (DataType.getCurrentDateByFormart("yyyy-MM-dd").equals(str)) {
					continue;
				} else {
					return true;
				}
			}

			if (attr.getDefaultVal().toString().equals(str) && !attr.getIsFK()) {
				continue;
			}

			if (attr.getIsEnum()) {
				if (attr.getDefaultVal().toString().equals(str)) {
					continue;
				} else {
					return false;
				}
				/*
				 * warning continue;
				 */
			}

			if (attr.getIsNum()) {
				/*
				 * warning if (java.math.BigDecimal.Parse(str) !=
				 * java.math.BigDecimal.Parse(attr.getDefaultVal().toString()))
				 */
				if ((new BigDecimal(str)).compareTo(new BigDecimal(attr.getDefaultVal().toString())) != 0) {
					return false;
				} else {
					continue;
				}
			}

			if (attr.getIsFKorEnum()) {
				// if (attr.DefaultVal == null || attr.DefaultVal == "")
				// continue;

				if (!attr.getDefaultVal().toString().equals(str)) {
					return false;
				} else {
					continue;
				}
			}

			if (!attr.getDefaultVal().toString().equals(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 鑾峰彇鎴栬�呰缃� 鏄笉鏄┖鐨勫疄浣�.
	 */
	public final boolean getIsEmpty() {
		if (this._row == null)
			return true;

		if (this.getPKVal() == null || this.getPKVal().toString().equals("0")
				|| this.getPKVal().toString().equals("")) {
			return true;
		}
		return false;

	}

	public final void setIsEmpty() {
		this._row = null;
	}

	/**
	 * 瀵硅繖涓疄浣撶殑鎻忚堪
	 */
	public final String getEnDesc() {
		return this.getEnMap().getEnDesc();
	}

	/**
	 * 鍙栧埌涓诲仴鍊笺�傚鏋滃畠鐨勪富鍋ヤ笉鍞竴锛屽氨杩斿洖绗竴涓�笺�� 鑾峰彇鎴栬缃�
	 */
	public final Object getPKVal() {
		return this.GetValByKey(this.getPK());
	}

	public final void setPKVal(Object value) {
		this.SetValByKey(this.getPK(), value);
	}

	/**
	 * 濡傛灉鍙湁涓�涓富閿�,灏辫繑鍥濸K,濡傛灉鏈夊涓氨杩斿洖绗竴涓�.PK
	 */
	public final int getPKCount() {
		if (this.getPK().equals("OID") || this.getPK().equals("No") || this.getPK().equals("MyPK")
				|| this.getPK().equals("NodeID") || this.getPK().equals("WorkID")) {
			return 1;
		}

		int i = 0;
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				i++;
			}
		}
		if (i == 0) {
			throw new RuntimeException("@娌℃湁缁欍��" + this.getEnDesc() + "锛�" + this.getEnMap().getPhysicsTable() + "銆戝畾涔変富閿��");
		} else {
			return i;
		}
	}

	/**
	 * 鏄笉鏄疧IDEntity
	 */
	public final boolean getIsOIDEntity() {
		if (this.getPK().equals("OID")) {
			return true;
		}
		return false;
	}

	/**
	 * 鏄笉鏄疧IDEntity
	 */
	public final boolean getIsNoEntity() {
		if (this.getPK().equals("No")) {
			return true;
		}
		return false;
	}

	/**
	 * 鏄惁鏄疶reeEntity
	 */
	public final boolean getIsTreeEntity() {
		return this.getEnMap().getAttrs().Contains("ParentNo");

	}

	/**
	 * 濡傛灉鍙湁涓�涓富閿�,灏辫繑鍥濸K,濡傛灉鏈夊涓氨杩斿洖绗竴涓�.PK
	 */
	public String getPK() {

		String pks = "";
		for (Attr attr : this.getEnMap().getAttrs()) {
			/*
			 * if (attr.getKey().equals("No")) { return "No"; } else if
			 * (attr.getKey().equals("OID")) { return "OID"; } else if
			 * (attr.getKey().equals("MyPK")) { return "MyPK"; }
			 */
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				pks += attr.getKey() + ",";
			}
		}
		if (pks.equals("")) {
			throw new RuntimeException("@娌℃湁缁欍��" + this.getEnDesc() + "锛�" + this.getEnMap().getPhysicsTable() + "銆戝畾涔変富閿��");
		}
		pks = pks.substring(0, pks.length() - 1);
		return pks;
	}

	/**
	 * 濡傛灉鍙湁涓�涓富閿�,灏辫繑鍥濸K,濡傛灉鏈夊涓氨杩斿洖绗竴涓�.PK
	 */
	public final String[] getPKs() {
		String[] strs1 = new String[this.getPKCount()];
		int i = 0;
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				strs1[i] = attr.getKey();
				i++;
			}
		}
		return strs1;
	}

	/**
	 * 鍙栧埌涓诲仴鍊笺��
	 */
	public final java.util.Hashtable getPKVals() {
		java.util.Hashtable ht = new java.util.Hashtable();
		String[] strs = this.getPKs();
		for (String str : strs) {
			ht.put(str, this.GetValStringByKey(str));
		}
		return ht;
	}

}