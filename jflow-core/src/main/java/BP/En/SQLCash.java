package BP.En;

import java.util.*;

/** 
 缓存SQL
*/
public class SQLCash
{
	public String EnName = null;
	public String Insert = null;
	public String Update = null;
	public String Delete = null;
	public String Select = null;
	public SQLCash()
	{

	}
	public SQLCash(Entity en) throws Exception
	{
		this.EnName = en.toString();
		this.Insert = SqlBuilder.InsertForPara(en);
		this.Update = SqlBuilder.UpdateForPara(en, null);
		this.Delete = SqlBuilder.DeleteForPara(en);
		this.Select = SqlBuilder.RetrieveForPara(en);

		//switch (en.EnMap.EnDBUrl.DBType)
		//{
		//    case BP.DA.DBType.Access:
		//        break;
		//    default:

		//        break;
		//}
	}
	/** 
	 获取指定的key, 返回更新的语句。
	 
	 @param keys
	 @return 
	 * @throws Exception 
	*/
	public final String GetUpdateSQL(Entity en, String[] keys) throws Exception
	{
		if (keys == null)
		{
			return this.Update;
		}

		String mykey = "";
		for (String k : keys)
		{
			mykey += k;
		}

		Object tempVar = this.getUpdateSQLs().get(mykey);
		String sql = tempVar instanceof String ? (String)tempVar : null;
		if (sql == null)
		{
			getUpdateSQLs().put(mykey, SqlBuilder.UpdateForPara(en, keys));
		}

		Object tempVar2 = getUpdateSQLs().get(mykey);
		sql = tempVar2 instanceof String ? (String)tempVar2 : null;

		if (sql == null)
		{
			throw new RuntimeException("@error");
		}

		return sql;
	}


		///#region UpdateSQLs
	private Hashtable _UpdateSQLs;
	public final Hashtable getUpdateSQLs()
	{
		if (_UpdateSQLs == null)
		{
			_UpdateSQLs = new Hashtable();
		}
		return _UpdateSQLs;
	}

		///#endregion

}