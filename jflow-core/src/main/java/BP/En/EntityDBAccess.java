package BP.En;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.Sys.SystemConfig;

//
//简介：负责存取数据的类
//创建时间：2002-10
//最后修改时间：2002-10
//

public class EntityDBAccess {

	// 对实体的基本操作

	private static void fullDate(DataTable dt, Entity en, Attrs attrs) {

		if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.Oracle) {
			for (Attr attr : attrs) {
				en.getRow().SetValByKey(attr.getKey(), dt.Rows.get(0).getValue(attr.getKey().toUpperCase()));
			}

			return;
		}

		for (Attr attr : attrs) {
			en.getRow().SetValByKey(attr.getKey(), dt.Rows.get(0).getValue(attr.getKey()));

		}
	}

	public static int Retrieve(Entity en, String sql, Paras paras) {

		DataTable dt = DBAccess.RunSQLReturnTable(sql, paras);

		if (null == dt || null == dt.Rows || dt.Rows.size() == 0) {
			return 0;
		}

		Attrs attrs = en.getEnMap().getAttrs();

		EntityDBAccess.fullDate(dt, en, attrs);

		int i = dt.Rows.size();
		// dt.dispose();
		return i;
	}

	/**
	 * 更新
	 * 
	 * @param en
	 *            产生要更新的语句
	 * @param keys
	 *            要更新的属性(null,认为更新全部)
	 * @return sql
	 * @throws Exception
	 */
	public static int Update(Entity en, String[] keys) throws Exception {
		return DBAccess.RunSQL(en.getSQLCash().GetUpdateSQL(en, keys), SqlBuilder.GenerParas(en, keys));
	}

}