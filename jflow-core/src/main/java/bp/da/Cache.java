package bp.da;

import java.io.File;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.tools.ConvertTools;

/**
 * Cache 的摘要说明。
 */
public class Cache {

    public static final Hashtable<String, Object> CS_Cache = new Hashtable<>();

    public Cache() {
    }

    private static String bsCacheKey = SystemConfig.getRedisCacheKey("BSCache");
    private static Hashtable<String, Object> _BS_Cache = new Hashtable<>();

    public static Hashtable<String, Object> getBS_Cache() {
        if (SystemConfig.getRedisIsEnable())
            _BS_Cache = ContextHolderUtils.getRedisUtils().hget(false, bsCacheKey);
        if (_BS_Cache == null) {
            _BS_Cache = new Hashtable();
        }
        return _BS_Cache;
    }

    // Bill_Cache 单据模板Cache.
    private static String billCacheKey = SystemConfig.getRedisCacheKey("BillCache");
    private static Hashtable<String, Object> _Bill_Cache = new Hashtable<>();

    public static Hashtable<String, Object> getBill_Cache() {
        if (SystemConfig.getRedisIsEnable())
            _Bill_Cache = ContextHolderUtils.getRedisUtils().hget(false, billCacheKey);
        if (_Bill_Cache == null) {
            _Bill_Cache = new Hashtable();
        }
        return _Bill_Cache;
    }

    // BS_Cache
    public static void ClearCache() {
        if (SystemConfig.getRedisIsEnable()) {
            ContextHolderUtils.getRedisUtils().del(false, bsCacheKey);
            ContextHolderUtils.getRedisUtils().del(false, sqlCacheKey);
            ContextHolderUtils.getRedisUtils().del(false, ensDataCacheKey);
            ContextHolderUtils.getRedisUtils().del(false, mapCacheKey);
            ContextHolderUtils.getRedisUtils().del(false, ensCacheExtKey);
            ContextHolderUtils.getRedisUtils().del(false, billCacheKey);
        }
        if (_BS_Cache != null) {
            _BS_Cache.clear();
        }
        if (_SQL_Cache != null) {
            _SQL_Cache.clear();
        }
        if (_EnsData_Cache != null) {
            _EnsData_Cache.clear();
        }
        if (_Map_Cache != null) {
            _Map_Cache.clear();
        }
        if (_EnsData_Cache_Ext != null) {
            _EnsData_Cache_Ext.clear();
        }
        if (_Bill_Cache != null) {
            _Bill_Cache.clear();
        }
    }

    public static void ClearCache(String enName) {
        if (_BS_Cache != null) {
            if (_BS_Cache.containsKey(enName) == true)
                _BS_Cache.remove(enName);
        }

        if (_SQL_Cache != null) {
            if (_SQL_Cache.containsKey(enName) == true)
                _SQL_Cache.remove(enName);
        }


        if (_EnsData_Cache != null) {
            if (_EnsData_Cache.containsKey(enName) == true)
                _EnsData_Cache.remove(enName);
        }

        if (_Map_Cache != null) {
            if (_Map_Cache.containsKey(enName) == true)
                _Map_Cache.remove(enName);
        }

        if (_EnsData_Cache_Ext != null) {
            if (_EnsData_Cache_Ext.containsKey(enName) == true)
                _EnsData_Cache_Ext.remove(enName);
        }

        if (_Bill_Cache != null) {
            if (_Bill_Cache.containsKey(enName) == true)
                _Bill_Cache.remove(enName);
        }
    }

    // SQL Cache
    private static String sqlCacheKey = SystemConfig.getRedisCacheKey("SQLCache");
    private static Hashtable<String, Object> _SQL_Cache = new Hashtable<>();

    public static Hashtable<String, Object> getSQL_Cache() {
        if (SystemConfig.getRedisIsEnable())
            _SQL_Cache = ContextHolderUtils.getRedisUtils().hget(false, sqlCacheKey);
        if (_SQL_Cache == null) {
            _SQL_Cache = new Hashtable<String, Object>();
        }
        return _SQL_Cache;
    }

    public static SQLCache GetSQL(String clName) {
        SQLCache tempVar = (SQLCache) getSQL_Cache().get(clName);
        return (SQLCache) ((tempVar instanceof SQLCache) ? tempVar : null);
    }

    public static void SetSQL(String clName, SQLCache csh) {
        if (clName == null || csh == null) {
            throw new RuntimeException("clName.  csh 参数有一个为空。");
        }
        getSQL_Cache().put(clName, csh);
        if (SystemConfig.getRedisIsEnable())
            ContextHolderUtils.getRedisUtils().hset(false, sqlCacheKey, clName, csh);
    }

    public static void ClearSQL(String clName) {
        if (clName == null) {
            throw new RuntimeException("clName参数有一个为空。");
        }
        getSQL_Cache().remove(clName);
        if (SystemConfig.getRedisIsEnable())
            ContextHolderUtils.getRedisUtils().hdel(false, sqlCacheKey, clName);
    }


    // EnsData Cache
    private static String ensDataCacheKey = SystemConfig.getRedisCacheKey("EnsDataCache");
    private static Hashtable<String, Object> _EnsData_Cache = new Hashtable<>();

    public static Hashtable<String, Object> getEnsData_Cache() {
        if (SystemConfig.getRedisIsEnable())
            _EnsData_Cache = ContextHolderUtils.getRedisUtils().hget(false, ensDataCacheKey);
        if (_EnsData_Cache == null) {
            _EnsData_Cache = new Hashtable<String, Object>();
        }
        return _EnsData_Cache;
    }

    public static Entities GetEnsData(String clName) {
        Entities tempVar = (Entities) getEnsData_Cache().get(clName);
        Entities ens = (Entities) ((tempVar instanceof Entities) ? tempVar : null);
        if (ens == null)
            return null;
        if (ens.size() == 0)
            return null;
        return ens;
    }

    public static void EnsDataSet(String clName, Entities obj) {
        if (obj.size() == 0) {

            // throw new Exception(clName +
            // "设置个数为0 ， 请确定这个缓存实体，是否有数据？sq=select * from " +
            // obj.getNewEntity().getEnMap().getPhysicsTable());
        }
        getEnsData_Cache().put(clName, obj);
        if (SystemConfig.getRedisIsEnable())
            ContextHolderUtils.getRedisUtils().hset(false, ensDataCacheKey, clName, obj);
    }

    public static void remove(String clName) {
        getEnsData_Cache().remove(clName);
        if (SystemConfig.getRedisIsEnable())
            ContextHolderUtils.getRedisUtils().hdel(false, ensDataCacheKey, clName);
    }

    // EnsData Cache 扩展 临时的Cache 文件。
    private static String ensCacheExtKey = SystemConfig.getRedisCacheKey("EnsDataCacheExt");
    private static Hashtable<String, Object> _EnsData_Cache_Ext = new Hashtable<>();

    public static Hashtable<String, Object> getEnsData_Cache_Ext() {
        if (SystemConfig.getRedisIsEnable())
            _EnsData_Cache_Ext = ContextHolderUtils.getRedisUtils().hget(false, ensCacheExtKey);
        if (_EnsData_Cache_Ext == null) {
            _EnsData_Cache_Ext = new Hashtable<String, Object>();
        }
        return _EnsData_Cache_Ext;
    }

    /**
     * 为部分数据做的缓冲处理
     * <p>
     * param clName
     *
     * @return
     */
    public static Entities GetEnsDataExt(String clName) {
        // 判断是否失效了。
        if (SystemConfig.isTempCacheFail()) {
            getEnsData_Cache_Ext().clear();
            if (SystemConfig.getRedisIsEnable())
                ContextHolderUtils.getRedisUtils().del(false, ensCacheExtKey);
            return null;
        }

        try {
            Entities ens;
            Entities tempVar = (Entities) getEnsData_Cache_Ext().get(clName);
            ens = (Entities) ((tempVar instanceof Entities) ? tempVar : null);
            return ens;
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    /**
     * 为部分数据做的缓冲处理
     * <p>
     * param clName
     * param obj
     */
    public static void SetEnsDataExt(String clName, Entities obj) {
        if (clName == null || obj == null) {
            throw new RuntimeException("clName.  obj 参数有一个为空。");
        }
        getEnsData_Cache_Ext().put(clName, obj);
        if (SystemConfig.getRedisIsEnable())
            ContextHolderUtils.getRedisUtils().hset(false, ensCacheExtKey, clName, obj);
    }

    private static String mapCacheKey = SystemConfig.getRedisCacheKey("MapCache");
    private static Hashtable<String, Object> _Map_Cache = new Hashtable<String, Object>();

    public static Hashtable<String, Object> getMap_Cache() {
        if (SystemConfig.getRedisIsEnable())
            _Map_Cache = ContextHolderUtils.getRedisUtils().hget(false, mapCacheKey);
        if (_Map_Cache == null) {
            _Map_Cache = new Hashtable<String, Object>();
        }
        return _Map_Cache;
    }

    public static Map GetMap(String clName) {
        try {
            Map tempVar = (Map) getMap_Cache().get(clName);
            return (Map) ((tempVar instanceof Map) ? tempVar : null);
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    public static void SetMap(String clName, Map map) {
        if (clName == null)
            return;

        if (map == null) {
            getMap_Cache().remove(clName);
            return;
        }

        getMap_Cache().put(clName, map);
    }

    private static final String tsMapCacheKey = SystemConfig.getRedisCacheKey("MapCacheTS");
    private static ConcurrentHashMap<String, Map> _Map_CacheTS = null;

    public static ConcurrentHashMap<String, Map> getMapCacheTS() {
        if (SystemConfig.getRedisIsEnable())
            _Map_Cache = ContextHolderUtils.getRedisUtils().hget(false, tsMapCacheKey);

        if (_Map_CacheTS == null)
            _Map_CacheTS = new ConcurrentHashMap<>();
        return _Map_CacheTS;
    }

    public static Map GetMapTS(String clName) {
        try {
            Map tempVar = (Map) getMapCacheTS().get(clName);
            return (Map) ((tempVar instanceof Map) ? tempVar : null);
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    public static void SetMapTS(String clName, Map map) {
        if (clName == null)
            return;
        if (map == null) {
            getMapCacheTS().remove(clName);
            return;
        }
        getMapCacheTS().put(clName, map);

    }

    /**
     * 是否存map.
     *
     * @param clName
     * @return
     */
    public static boolean IsExitMapTS(String clName) {
        if (clName == null) {
            throw new RuntimeException("clName.不能为空。");
        }

        return getMapCacheTS().containsKey(clName);
    }

    // 取出对象

    /**
     * 从 Cache 里面取出对象.
     */
    public static Object GetObj(String key, Depositary where) {
        if (where == Depositary.None) {
            throw new RuntimeException("您没有把(" + key + ")放到session or application 里面不能找出他们.");
        }
        //if (SystemConfig.isBSsystem()) {
        if (where == Depositary.Application) {
            return getBS_Cache().get(key);
        } else {
            return ContextHolderUtils.getSession().getAttribute(key);
        }
		/*} else {
			return CS_Cache.get(key);
		}*/
    }

    public static Object GetObj(String key) {
        //if (SystemConfig.isBSsystem()) {
        Object obj = getBS_Cache().get(key); // Cache.GetObjFormApplication(key,
        // null);
        if (obj == null) {
            obj = Cache.GetObjFormSession(key);
        }
        return obj;
		/*} else {
			return CS_Cache.get(key);
		}*/
    }

    /**
     * 删除 like 名称的缓存对象。
     * <p>
     * param likeKey
     *
     * @return
     */
    public static int DelObjFormApplication(String likeKey) {
        int i = 0;
        //if (SystemConfig.isBSsystem()) {
        String willDelKeys = "";
        for (Object key : getBS_Cache().keySet()) {
            if (!key.toString().contains(likeKey)) {
                continue;
            }
            willDelKeys += "@" + key;
        }

        String[] strs = willDelKeys.split("(@)", -1);
        for (String s : strs) {
            if (DataType.IsNullOrEmpty(s) == true) {
                continue;
            }
            getBS_Cache().remove(s);
            i++;
        }
		/*} else {
			String willDelKeys = "";
			for (Object key : CS_Cache.keySet()) {
				if (!key.toString().contains(likeKey)) {
					continue;
				}
				willDelKeys += "@" + key;
			}

			String[] strs = willDelKeys.split("(@)", -1);
			for (String s : strs) {
				if (DataType.IsNullOrEmpty(s) == true) {
					continue;
				}
				CS_Cache.remove(s);
				i++;
			}
		}*/

        return i;
    }

    public static Object GetObjFormApplication(String key, Object isNullAsVal) {
        //if (SystemConfig.isBSsystem()) {
        Object obj = getBS_Cache().get(key); // BP.Glo.HttpContextCurrent.Cache(key);
        if (obj == null) {
            return isNullAsVal;
        } else {
            return obj;
        }
		/*} else {
			Object obj = CS_Cache.get(key);
			if (obj == null) {
				return isNullAsVal;
			} else {
				return obj;
			}
		}*/
    }

    public static Object GetObjFormSession(String key) {
        //if (SystemConfig.isBSsystem()) {
        try {
            /*
             * warning return BP.Glo.getHttpContextCurrent().Session(key);
             */
            return ContextHolderUtils.getSession().getAttribute(key);
        } catch (java.lang.Exception e) {
            return null;
        }
		/*} else {
			return CS_Cache.get(key);
		}*/
    }

    // remove Obj

    /**
     * RemoveObj
     * <p>
     * param key
     * param where
     */
    public static void RemoveObj(String key, Depositary where) {
        if (!Cache.IsExits(key, where)) {
            return;
        }

        //if (SystemConfig.isBSsystem()) {
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
		/*} else {
			CS_Cache.remove(key);
		}*/
    }

    // 放入对象
    public static void RemoveObj(String key) {

        getBS_Cache().remove(key);
        if (SystemConfig.getRedisIsEnable())
            ContextHolderUtils.getRedisUtils().hdel(false, bsCacheKey, key);
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
        // if (Cache.IsExits(key, where))
        // return;

        //if (SystemConfig.isBSsystem()) {
        if (where == Depositary.Application) {
            getBS_Cache().put(key, obj);
        } else {
            /*
             * warning BP.Glo.getHttpContextCurrent().Session(key) = obj;
             */
            //ContextHolderUtils.getSession().setAttribute(key, obj);
        }
		/*} else {
			if (CS_Cache.containsKey(key)) {
				CS_Cache.put(key, obj);
			} else {
				CS_Cache.put(key, obj);
			}
			ContextHolderUtils.getRedisUtils().hset(false,billCacheKey,key,obj);
		}*/
    }

    // 判断对象是不是存在

    /**
     * 判断对象是不是存在
     */
    public static boolean IsExits(String key, Depositary where) {
        //if (SystemConfig.isBSsystem()) {
        if (where == Depositary.Application) {
            return true;
        } else {
            return true;
        }
		/*} else {
			return CS_Cache.containsKey(key);
		}*/
    }

    public static String GetBillStr(String cfile, boolean isCheckCache) throws Exception {
        String val = (String) ((getBill_Cache().get(cfile) instanceof String) ? getBill_Cache().get(cfile) : null);
        if (isCheckCache == true) {
            val = null;
        }

        if (DataType.IsNullOrEmpty(val)) {
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
            _Bill_Cache.put(cfile, val);
            if (SystemConfig.getRedisIsEnable())
                ContextHolderUtils.getRedisUtils().hset(false, billCacheKey, cfile, val);
        }
        return val.substring(0);
    }

    public static String[] GetBillParas(String cfile, String ensStrs, Entities ens) throws Exception {
        String[] paras = (String[]) ((getBill_Cache().get(cfile + "Para") instanceof String[])
                ? getBill_Cache().get(cfile + "Para") : null);
        if (paras != null) {
            return paras;
        }

        Attrs attrs = new Attrs();
        for (Entity en : Entities.convertEntities(ens)) {
            String perKey = en.toString();

            Attrs enAttrs = en.getEnMap().getAttrs();
            for (Attr attr : enAttrs.convertAttrs(enAttrs)) {
                Attr attrN = new Attr();
                attrN.setKey(perKey + "." + attr.getKey());

                // attrN.Key = attrN.getKey().replace("\\f2","");
                // attrN.Key = attrN.getKey().replace("\\f3", "");

                if (attr.getItIsRefAttr()) {
                    attrN.setField(perKey + "." + attr.getKey() + "Text");
                }
                attrN.setMyDataType(attr.getMyDataType());
                attrN.setMyFieldType(attr.getMyFieldType());
                attrN.setUIBindKey(attr.getUIBindKey());
                attrN.setField(attr.getField());
                attrs.Add(attrN);
            }
        }

        paras = Cache.GetBillParas_Gener(cfile, attrs);
        _Bill_Cache.put(cfile + "Para", paras);
        return paras;
    }

    public static String[] GetBillParas(String cfile, String ensStrs, Entity en) throws Exception {
        String[] paras = (String[]) ((getBill_Cache().get(cfile + "Para") instanceof String[])
                ? getBill_Cache().get(cfile + "Para") : null);
        if (paras != null) {
            return paras;
        }

        paras = Cache.GetBillParas_Gener(cfile, en.getEnMap().getAttrs());
        _Bill_Cache.put(cfile + "Para", paras);
        return paras;
    }

    public static String[] GetBillParas_Gener(String cfile, Attrs attrs) throws Exception {
        // Attrs attrs = en.getEnMap().getAttrs();
        String[] paras = new String[300];
        String Billstr = Cache.GetBillStr(cfile, true);
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