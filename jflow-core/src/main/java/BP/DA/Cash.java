package BP.DA;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import BP.Difference.ContextHolderUtils;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.SQLCash;
import BP.En.SqlBuilder;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.ConvertTools;
import BP.Tools.StringHelper;

/**
 * Cash 的摘要说明。
 */
public class Cash {
	
	public static Hashtable<String, String> EnumKeySQL_Cash;

	public static String getCaseWhenSQL(String mTable, String key, String field, String enumKey, int def, String tag)
			throws Exception {
		String mykey = mTable + "" + key + "" + field + "" + enumKey + def;

		if (EnumKeySQL_Cash == null)
			EnumKeySQL_Cash = new Hashtable();

		String sql = EnumKeySQL_Cash.get(mykey);
		if (sql != null)
			return sql;

		SysEnums ses = null;
		if (DataType.IsNullOrEmpty(tag) == true) {

			ses = new SysEnums(enumKey);
			if (ses.size() == 0) {
				BP.Sys.XML.EnumInfoXml xml = new BP.Sys.XML.EnumInfoXml(enumKey);
				ses.RegIt(enumKey, xml.getVals());
			}
		} else {
			ses = new SysEnums();
			ses.RegIt(enumKey, tag);
			// ses.LoadIt(enumKey);
		}

		sql = " CASE " + mTable + field;
		for (SysEnum se1 : ses.ToJavaList()) {
			sql += " WHEN " + se1.getIntKey() + " THEN '" + se1.getLab() + "'";
		}

		SysEnum se = (SysEnum) ses.GetEntityByKey(SysEnumAttr.IntKey, def);
		if (se == null) {
			sql += " END \"" + key + "Text\"";
		} else {
			sql += " WHEN NULL THEN '" + se.getLab() + "' END \"" + key + "Text\"";
		}
		EnumKeySQL_Cash.put(mykey, sql);

		return sql;

	}
	
	static {
		if (!SystemConfig.getIsBSsystem()) {
			CS_Cash = new Hashtable<String, Object>();
		}
	}
	public static Hashtable<String, Object> CS_Cash;

	// Bill_Cash 单据模板cash.
	private static Hashtable _Bill_Cash;

	public static Hashtable getBill_Cash() {
		if (_Bill_Cash == null) {
			_Bill_Cash = new Hashtable();
		}
		return _Bill_Cash;
	}

	// BS_Cash
	private static Hashtable<String, Object> _BS_Cash;

	public static Hashtable<String, Object> getBS_Cash() {
		if (_BS_Cash == null) {
			_BS_Cash = new Hashtable<String, Object>();
		}
		return _BS_Cash;
	}

	public static void ClearCash() {
		if (_BS_Cash != null)
			_BS_Cash.clear();

		if (_SQL_Cash != null)
			_SQL_Cash.clear();

		if (_EnsData_Cash != null)
			_EnsData_Cash.clear();

		if (_Map_Cash != null)
			_Map_Cash.clear();

		if (_EnsData_Cash_Ext != null)
			_EnsData_Cash_Ext.clear();

		if (_Bill_Cash != null)
			_Bill_Cash.clear();

	}

	// SQL cash
	private static Hashtable<String, Object> _SQL_Cash;

	public static Hashtable<String, Object> getSQL_Cash() {
		if (_SQL_Cash == null) {
			_SQL_Cash = new Hashtable<String, Object>();
		}
		return _SQL_Cash;
	}

	public static SQLCash GetSQL(String clName) {
		SQLCash tempVar = (SQLCash) getSQL_Cash().get(clName);
		return (SQLCash) ((tempVar instanceof SQLCash) ? tempVar : null);
	}

	public static void SetSQL(String clName, SQLCash csh) {
		if (clName == null || csh == null) {
			throw new RuntimeException("clName.  csh 参数有一个为空。");
		}
		getSQL_Cash().put(clName, csh);
	}

	public static void InitSQL() throws Exception {
		ArrayList<Entity> al = ClassFactory.GetObjects("Entity");
		for (Entity en : al) {
			String sql = SqlBuilder.Retrieve(en);
		}
	}

	// EnsData cash
	private static Hashtable<String, Object> _EnsData_Cash;

	public static Hashtable<String, Object> getEnsData_Cash() {
		if (_EnsData_Cash == null) {
			_EnsData_Cash = new Hashtable<String, Object>();
		}
		return _EnsData_Cash;
	}

	public static Entities GetEnsData(String clName) {
		Entities tempVar = (Entities) getEnsData_Cash().get(clName);
		Entities ens = (Entities) ((tempVar instanceof Entities) ? tempVar : null);
		if (ens == null) {
			return null;
		}

		if (ens.size() == 0) {
			return null;
		}
		// throw new Exception(clName + "个数为0");
		return ens;
	}

	public static void EnsDataSet(String clName, Entities obj) {
		if (obj.size() == 0) {
			/**
			 * obj.RetrieveAll();
			 */

			// throw new Exception(clName +
			// "设置个数为0 ， 请确定这个缓存实体，是否有数据？sq=select * from " +
			// obj.GetNewEntity.getEnMap().getPhysicsTable());
		}

		getEnsData_Cash().put(clName, obj);
	}

	public static void remove(String clName) {
		getEnsData_Cash().remove(clName);
	}

	// EnsData cash 扩展 临时的cash 文件。
	private static Hashtable<String, Object> _EnsData_Cash_Ext;

	public static Hashtable<String, Object> getEnsData_Cash_Ext() {
		if (_EnsData_Cash_Ext == null) {
			_EnsData_Cash_Ext = new Hashtable<String, Object>();
		}
		return _EnsData_Cash_Ext;
	}

	/**
	 * 为部分数据做的缓冲处理
	 * 
	 * @param clName
	 * @return
	 */
	public static Entities GetEnsDataExt(String clName) {
		// 判断是否失效了。
		if (SystemConfig.getIsTempCashFail()) {
			getEnsData_Cash_Ext().clear();
			return null;
		}

		try {
			Entities ens;
			Entities tempVar = (Entities) getEnsData_Cash_Ext().get(clName);
			ens = (Entities) ((tempVar instanceof Entities) ? tempVar : null);
			return ens;
		} catch (java.lang.Exception e) {
			return null;
		}
	}

	/**
	 * 为部分数据做的缓冲处理
	 * 
	 * @param clName
	 * @param obj
	 */
	public static void SetEnsDataExt(String clName, Entities obj) {
		if (clName == null || obj == null) {
			throw new RuntimeException("clName.  obj 参数有一个为空。");
		}
		getEnsData_Cash_Ext().put(clName, obj);
	}

	// map cash
	private static Hashtable<String, Object> _Map_Cash;

	public static Hashtable<String, Object> getMap_Cash() {
		if (_Map_Cash == null) {
			_Map_Cash = new Hashtable<String, Object>();
		}
		return _Map_Cash;
	}

	public static Map GetMap(String clName) {
		try {
			Map tempVar = (Map) getMap_Cash().get(clName);
			return (Map) ((tempVar instanceof Map) ? tempVar : null);
		} catch (java.lang.Exception e) {
			return null;
		}
	}

	public static void SetMap(String clName, Map map) {
		if (clName == null)
			return;

		// throw new Exception("clName.不能为空。");
		if (map == null) {
			getMap_Cash().remove(clName);
			return;
		}

		getMap_Cash().put(clName, map);
	}

	// 取出对象
	/**
	 * 从 Cash 里面取出对象.
	 */
	public static Object GetObj(String key, Depositary where) {
		if (where == Depositary.None) {
			throw new RuntimeException("您没有把(" + key + ")放到session or application 里面不能找出他们.");
		}
		if (SystemConfig.getIsBSsystem()) {
			if (where == Depositary.Application)
			// return BP.Glo.HttpContextCurrent.Cache(key);
			{
				return getBS_Cash().get(key); // BP.Glo.HttpContextCurrent.Cache(key);
			} else {
				/*
				 * warning return BP.Glo.getHttpContextCurrent().Session(key);
				 */
				return ContextHolderUtils.getSession().getAttribute(key);
			}
		} else {
			return CS_Cash.get(key);
		}
	}

	public static Object GetObj(String key) {
		if (SystemConfig.getIsBSsystem()) {
			Object obj = getBS_Cash().get(key); // Cash.GetObjFormApplication(key,
												// null);
			if (obj == null) {
				obj = Cash.GetObjFormSession(key);
			}
			return obj;
		} else {
			return CS_Cash.get(key);
		}
	}

	/**
	 * 删除 like 名称的缓存对象。
	 * 
	 * @param likeKey
	 * @return
	 */
	public static int DelObjFormApplication(String likeKey) {
		int i = 0;
		if (SystemConfig.getIsBSsystem()) {
			String willDelKeys = "";
			for (String key : getBS_Cash().keySet()) {
				if (!key.contains(likeKey)) {
					continue;
				}
				willDelKeys += "@" + key;
			}

			String[] strs = willDelKeys.split("(@)", -1);
			for (String s : strs) {
				if (s == null || s.equals("")) {
					continue;
				}
				getBS_Cash().remove(s);
				i++;
			}
		} else {
			String willDelKeys = "";
			for (String key : CS_Cash.keySet()) {
				if (!key.contains(likeKey)) {
					continue;
				}
				willDelKeys += "@" + key;
			}

			String[] strs = willDelKeys.split("(@)", -1);
			for (String s : strs) {
				if (s == null || s.equals("")) {
					continue;
				}
				CS_Cash.remove(s);
				i++;
			}
		}

		return i;
	}

	public static Object GetObjFormApplication(String key, Object isNullAsVal) {
		if (SystemConfig.getIsBSsystem()) {
			Object obj = getBS_Cash().get(key); // BP.Glo.HttpContextCurrent.Cache(key);
			if (obj == null) {
				return isNullAsVal;
			} else {
				return obj;
			}
		} else {
			Object obj = CS_Cash.get(key);
			if (obj == null) {
				return isNullAsVal;
			} else {
				return obj;
			}
		}
	}

	public static Object GetObjFormSession(String key) {
		if (SystemConfig.getIsBSsystem()) {
			try {
				/*
				 * warning return BP.Glo.getHttpContextCurrent().Session(key);
				 */
				return ContextHolderUtils.getSession().getAttribute(key);
			} catch (java.lang.Exception e) {
				return null;
			}
		} else {
			return CS_Cash.get(key);
		}
	}

	// remove Obj
	/**
	 * RemoveObj
	 * 
	 * @param key
	 * @param where
	 */
	public static void RemoveObj(String key, Depositary where) {
		if (!Cash.IsExits(key, where)) {
			return;
		}

		if (SystemConfig.getIsBSsystem()) {
			if (where == Depositary.Application) {
				/*
				 * warning BP.Glo.getHttpContextCurrent().Cache.remove(key);
				 */
			} else {
				/*
				 * warning BP.Glo.getHttpContextCurrent().Session.remove(key);
				 */
				ContextHolderUtils.getSession().removeAttribute(key);
			}
		} else {
			CS_Cash.remove(key);
		}
	}

	// 放入对象
	public static void RemoveObj(String key) {
		getBS_Cash().remove(key);
	}

	public static void AddObj(String key, Depositary where, Object obj) {
		if (key == null) {
			throw new RuntimeException("您需要为obj=" + obj.toString() + ",设置为主键值。key");
		}

		if (obj == null) {
			throw new RuntimeException("您需要为obj=null  设置为主键值。key=" + key);
		}

		if (where == Depositary.None) {
			throw new RuntimeException("您没有把(" + key + ")放到 session or application 里面设置他们.");
		}
		// if (Cash.IsExits(key, where))
		// return;

		if (SystemConfig.getIsBSsystem()) {
			if (where == Depositary.Application) {
				getBS_Cash().put(key, obj);
			} else {
				/*
				 * warning BP.Glo.getHttpContextCurrent().Session(key) = obj;
				 */
				ContextHolderUtils.getSession().setAttribute(key, obj);
			}
		} else {
			if (CS_Cash.containsKey(key)) {
				CS_Cash.put(key, obj);
			} else {
				CS_Cash.put(key, obj);
			}
		}
	}

	// 判断对象是不是存在
	/**
	 * 判断对象是不是存在
	 */
	public static boolean IsExits(String key, Depositary where) {
		if (SystemConfig.getIsBSsystem()) {
			if (where == Depositary.Application) {
				/*
				 * warning if (BP.Glo.getHttpContextCurrent().Cache(key) ==
				 * null) { return false; } else { return true; }
				 */
				return true;
			} else {
				/*
				 * warning if (BP.Glo.getHttpContextCurrent().Session(key) ==
				 * null) { return false; } else { return true; }
				 */
				return true;
			}
		} else {
			return CS_Cash.containsKey(key);
		}
	}

	public static String GetBillStr(String cfile, boolean isCheckCash) {
		String val = (String) ((getBill_Cash().get(cfile) instanceof String) ? getBill_Cash().get(cfile) : null);
		if (isCheckCash == true) {
			val = null;
		}

		if (StringHelper.isNullOrEmpty(val)) {
			String file = null;
			if (cfile.contains(":")) {
				file = cfile;
			} else {
				file = SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + cfile;
			}

			 
			try {
				val = ConvertTools.StreamReaderToStringConvert(file, "us-ascii");
			} catch (Exception ex) {
				throw new RuntimeException("@读取单据模板时出现错误。cfile=" + cfile + " @Ex=" + ex.getMessage());
			}
			_Bill_Cash.put(cfile, val);
		}
		return val.substring(0);
	}

	public static String[] GetBillParas(String cfile, String ensStrs, Entities ens) throws Exception {
		String[] paras = (String[]) ((getBill_Cash().get(cfile + "Para") instanceof String[])
				? getBill_Cash().get(cfile + "Para") : null);
		if (paras != null) {
			return paras;
		}

		Attrs attrs = new Attrs();
		for (Entity en : Entities.convertEntities(ens)) {
			String perKey = en.toString();

			Attrs enAttrs = en.getEnMap().getAttrs();
			for (Attr attr : Attrs.convertAttrs(enAttrs)) {
				Attr attrN = new Attr();
				attrN.setKey(perKey + "." + attr.getKey());

				// attrN.Key = attrN.Key.replace("\\f2","");
				// attrN.Key = attrN.Key.replace("\\f3", "");

				if (attr.getIsRefAttr()) {
					attrN.setField(perKey + "." + attr.getKey() + "Text");
				}
				attrN.setMyDataType(attr.getMyDataType());
				attrN.setMyFieldType(attr.getMyFieldType());
				attrN.setUIBindKey(attr.getUIBindKey());
				attrN.setField(attr.getField());
				attrs.Add(attrN);
			}
		}

		paras = Cash.GetBillParas_Gener(cfile, attrs);
		_Bill_Cash.put(cfile + "Para", paras);
		return paras;
	}

	public static String[] GetBillParas(String cfile, String ensStrs, Entity en) throws Exception {
		String[] paras = (String[]) ((getBill_Cash().get(cfile + "Para") instanceof String[])
				? getBill_Cash().get(cfile + "Para") : null);
		if (paras != null) {
			return paras;
		}

		paras = Cash.GetBillParas_Gener(cfile, en.getEnMap().getAttrs());
		_Bill_Cash.put(cfile + "Para", paras);
		return paras;
	}

	public static String[] GetBillParas_Gener(String cfile, Attrs attrs) {
		// Attrs attrs = en.getEnMap().getAttrs();
		String[] paras = new String[300];
		String Billstr = Cash.GetBillStr(cfile, true);
		char[] chars = Billstr.toCharArray();
		String para = "";
		int i = 0;
		boolean haveError = false;
		String msg = "";
		for (char c : chars) {
			if (c == '>') {
				// 首先解决空格的问题.
				String real = para.toString();
				if (attrs != null && real.contains(" ")) {
					real = real.replace(" ", "");
					Billstr = Billstr.replace(para, real);
					para = real;
					haveError = true;
				}

				// 解决特殊符号
				if (attrs != null && real.contains("\\") && real.contains("ND") == false) {
					haveError = true;
					String findKey = null;
					int keyLen = 0;
					for (Attr attr : attrs) {
						if (real.contains(attr.getKey())) {
							if (keyLen <= attr.getKey().length()) {
								keyLen = attr.getKey().length();
								findKey = attr.getKey();
							}
						}
					}

					if (findKey == null) {
						msg += "@参数:<font color=red><b>(" + real + ")</b></font>可能拼写错误。";
						continue;
					}

					if (real.contains(findKey + ".NYR") == true) {
						real = findKey + ".NYR";
					} else if (real.contains(findKey + ".RMB") == true) {
						real = findKey + ".RMB";
					} else if (real.contains(findKey + ".RMBDX") == true) {
						real = findKey + ".RMBDX";
					} else {
						real = findKey;
					}

					Billstr = Billstr.replace(para, real);
					// msg += "@参数:<font color=red><b>(" + para +
					// ")</b></font>不符合规范。";
					// continue;
				}

				// paras.SetValue(para, i);
				paras[i] = para;
				i++;
			}

			if (c == '<') {
				para = ""; // 如果遇到了 '<' 开始记录
			} else {
				if ((new Character(c)).toString().equals("")) {
					continue;
				}
				para += (new Character(c)).toString();
			}
		}

		if (haveError) {
			String myfile = SystemConfig.getPathOfDataUser() + "/CyclostyleFile/" + cfile;
			if (!new File(myfile).exists()) {
				myfile = cfile;
			}

			// throw new Exception("@没有文件:"+myfile);
			try {
				ConvertTools.StreamWriteConvert(Billstr, myfile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// StreamWriter wr = new StreamWriter(myfile, false,
			// Encoding.ASCII);
			// wr.Write(Billstr);
			// wr.Close();
		}

		if (!msg.equals("")) {
			// String s =
			// "@帮助信息:用记事本打开它模板,找到红色的字体.
			// 把尖括号内部的非法字符去了,例如:《|f0|fs20RDT.NYR|lang1033|kerning2》，修改后事例：《RDT.NYR》。@注意把双引号代替单引号，竖线代替反斜线。";
			// throw new
			// Exception("@单据模板（"+cfile+"）如下标记出现错误，系统无法修复它，需要您手工的删除标记或者用记事本打开查找到这写标记修复他们.@"
			// + msg + s);
		}
		return paras;
	}
}