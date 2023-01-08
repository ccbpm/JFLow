package bp.da;

import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;

public class CashFrmTemplate {

    ///对实体的操作.

    /**
     * 放入表单
     * <p>
     * param frmID 表单ID
     * param ds 表单模版
     *
     * @throws Exception
     */
    public static void Put(String frmID, DataSet ds) throws Exception {
        if (ds == null) return;
        String json = bp.tools.Json.ToJson(ds);
        ContextHolderUtils.getRedisUtils().set(false, SystemConfig.getRedisCacheKey(frmID), json);
    }

    /**
     * 移除
     * <p>
     * param frmID 表单ID
     */
    public static void Remove(String frmID) {
        ContextHolderUtils.getRedisUtils().del(false, SystemConfig.getRedisCacheKey(frmID));
    }

    /**
     * 获得表单DataSet模式的模版数据
     * <p>
     * param frmID 表单ID
     *
     * @return 表单模版
     */
    public static DataSet GetFrmDataSetModel(String frmID) throws Exception {
        Object result = ContextHolderUtils.getRedisUtils().get(false, SystemConfig.getRedisCacheKey(frmID));
        if (result != null) {
            DataSet ds = bp.tools.Json.ToDataSet(result.toString());
            return ds;
        }
        return null;
    }

    /**
     * 获得表单json模式的模版数据
     * <p>
     * param frmID 表单ID
     *
     * @return json
     */

    /// 对实体的操作.

}