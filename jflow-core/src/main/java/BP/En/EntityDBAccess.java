package BP.En;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Pub.*;
import BP.Sys.*;

/*
简介：负责存取数据的类
创建时间：2002-10
最后修改时间：2002-10
*/


public class EntityDBAccess
{

		///#region 对实体的基本操作
	/** 
	 删除
	 
	 @param en
	 @return 
	 * @throws Exception 
	*/
	public static int Delete(Entity en) throws Exception
	{
		if (en.getEnMap().getEnType() == EnType.View)
		{
			return 0;
		}

		switch (en.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN :
				return DBAccess.RunSQL(en.getSQLCash().Delete, SqlBuilder.GenerParasPK(en));
			default :
				throw new RuntimeException("@没有设置类型。");
		}
	}
	/** 
	 更新
	 
	 @param en 产生要更新的语句
	 @param keys 要更新的属性(null,认为更新全部)
	 @return sql
	 * @throws Exception 
	*/
	public static int Update(Entity en, String[] keys) throws Exception
	{
		if (en.getEnMap().getEnType() == EnType.View)
		{
			return 0;
		}

		BP.DA.Paras paras = SqlBuilder.GenerParas(en, keys);
		String sql = en.getSQLCash().GetUpdateSQL(en, keys);
		try
		{
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					switch (SystemConfig.getAppCenterDBType())
					{
						case MSSQL:
						case Oracle:
						case MySQL:
						case PostgreSQL:
							return DBAccess.RunSQL(sql, paras);
						case Informix:
							return DBAccess.RunSQL(en.getSQLCash().GetUpdateSQL(en, keys), SqlBuilder.GenerParas_Update_Informix(en, keys));
						case Access:
							return DBAccess.RunSQL(SqlBuilder.UpdateOfMSAccess(en, keys));
						default:
							//return DBAccess.RunSQL(en.SQLCash.GetUpdateSQL(en, keys),
							//    SqlBuilder.GenerParas(en, keys));
							if (keys != null)
							{
								Paras ps = new Paras();
								Paras myps = SqlBuilder.GenerParas(en, keys);
								for (Para p : myps)
								{
									for (String s : keys)
									{
										if (p.ParaName.equals(s))
										{
											ps.Add(p);
											break;
										}
									}
								}
								return DBAccess.RunSQL(en.getSQLCash().GetUpdateSQL(en, keys), ps);
							}
						 
								return DBAccess.RunSQL(en.getSQLCash().GetUpdateSQL(en, keys), SqlBuilder.GenerParas(en, keys));
							 
						 
					}
				//case DBUrlType.DBAccessOfMSSQL1:
				//    return DBAccessOfMSSQL1.RunSQL(SqlBuilder.Update(en, keys));
				//case DBUrlType.DBAccessOfMSSQL2:
				//    return DBAccessOfMSSQL2.RunSQL(SqlBuilder.Update(en, keys));
				//case DBUrlType.DBAccessOfOracle1:
				//    return DBAccessOfOracle1.RunSQL(SqlBuilder.Update(en, keys));
				//case DBUrlType.DBAccessOfOracle2:
				//    return DBAccessOfOracle2.RunSQL(SqlBuilder.Update(en, keys));
				default:
					throw new RuntimeException("@没有设置类型。");
			}
		}
		catch (RuntimeException ex)
		{
			if (SystemConfig.getIsDebug())
			{
				en.CheckPhysicsTable();
			}
			throw ex;
		}
	}

		///#endregion


		///#region 产生序列号码方法


		///#endregion

	public static int RetrieveV2(Entity en, String sql, Paras paras) throws Exception
	{
		try
		{
			DataTable dt = new DataTable();
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					dt = DBAccess.RunSQLReturnTable(sql, paras);
					break;
				//case DBUrlType.DBAccessOfMSSQL1:
				//    dt = DBAccessOfMSSQL1.RunSQLReturnTable(sql);
				//    break;
				//case DBUrlType.DBAccessOfMSSQL2:
				//    dt = DBAccessOfMSSQL2.RunSQLReturnTable(sql);
				//    break;
				//case DBUrlType.DBAccessOfOracle1:
				//    dt = DBAccessOfOracle1.RunSQLReturnTable(sql);
				//    break;
				//case DBUrlType.DBAccessOfOracle2:
				//    dt = DBAccessOfOracle2.RunSQLReturnTable(sql);
				//    break;
				default:
					throw new RuntimeException("@没有设置DB类型。");
			}

			if (dt.Rows.size() == 0)
			{
				return 0;
			}
			Attrs attrs = en.getEnMap().getAttrs();
			EntityDBAccess.fullDate(dt, en, attrs);
			int i = dt.Rows.size();
			//dt..();
			return i;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	public static int Retrieve(Entity en, String sql, Paras paras) throws Exception
	{
		DataTable dt;
		switch (en.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				dt = DBAccess.RunSQLReturnTable(sql, paras);
				break;
			//case DBUrlType.DBAccessOfMSSQL1:
			//    dt = DBAccessOfMSSQL1.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfMSSQL2:
			//    dt = DBAccessOfMSSQL2.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfOracle1:
			//    dt = DBAccessOfOracle1.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfOracle2:
			//    dt = DBAccessOfOracle2.RunSQLReturnTable(sql);
			//    break;
			default:
				throw new RuntimeException("@没有设置DB类型。");
		}

		if (dt.Rows.size() == 0)
		{
			return 0;
		}
		Attrs attrs = en.getEnMap().getAttrs();
		EntityDBAccess.fullDate(dt, en, attrs);
		int num = dt.Rows.size();
		//dt.Dispose();
		return num;
	}
	/** 
	 查询
	 
	 @param en 实体
	 @param sql 组织的查询语句
	 @return 
	 * @throws Exception 
	*/
	public static int Retrieve(Entity en, String sql) throws Exception
	{
		try
		{
			DataTable dt = new DataTable();
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					dt = DBAccess.RunSQLReturnTable(sql);
					break;
				//case DBUrlType.DBAccessOfMSSQL1:
				//    dt = DBAccessOfMSSQL1.RunSQLReturnTable(sql);
				//    break;
				//case DBUrlType.DBAccessOfMSSQL2:
				//    dt = DBAccessOfMSSQL2.RunSQLReturnTable(sql);
				//    break;
				//case DBUrlType.DBAccessOfOracle1:
				//    dt = DBAccessOfOracle1.RunSQLReturnTable(sql);
				//    break;
				//case DBUrlType.DBAccessOfOracle2:
				//    dt = DBAccessOfOracle2.RunSQLReturnTable(sql);
				//    break;
				default:
					throw new RuntimeException("@没有设置DB类型。");
			}

			if (dt.Rows.size() == 0)
			{
				return 0;
			}
			Attrs attrs = en.getEnMap().getAttrs();
			EntityDBAccess.fullDate(dt, en, attrs);
			int i = dt.Rows.size();
			//dt.Dispose();
			return i;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	private static void fullDate(DataTable dt, Entity en, Attrs attrs) throws Exception
	{
		for (Attr attr : attrs)
		{
			en.getRow().SetValByKey(attr.getKey(), dt.Rows.get(0).getValue(attr.getKey()));
		}
	}
	public static int Retrieve(Entities ens, String sql) throws Exception
	{
		try
		{
			DataTable dt = new DataTable();
			switch (ens.getNewEntity().getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					dt = DBAccess.RunSQLReturnTable(sql);
					break;
				default:
					throw new RuntimeException("@没有设置DB类型。");
			}

			if (dt.Rows.size() == 0)
			{
				return 0;
			}

			Map enMap = ens.getNewEntity().getEnMap();
			Attrs attrs = enMap.getAttrs();

			//Entity  en1 = ens.GetNewEntity;
			for (DataRow dr : dt.Rows)
			{
				Entity en = ens.getNewEntity();
				//Entity  en = en1.CreateInstance();
				for (Attr attr : attrs)
				{
					en.getRow().SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
				}
				ens.AddEntity(en);
			}
			int i = dt.Rows.size();
			//dt.Dispose();
			return i;
			//return dt.Rows.size();
		}
		catch (RuntimeException ex)
		{
			// ens.GetNewEntity.CheckPhysicsTable();
			throw new RuntimeException("@在[" + ens.getNewEntity().getEnDesc() + "]查询时出现错误:" + ex.getMessage());
		}
	}
	public static int Retrieve(Entities ens, String sql, Paras paras, String[] fullAttrs) throws Exception
	{
		DataTable dt = null;
		switch (ens.getNewEntity().getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				dt = DBAccess.RunSQLReturnTable(sql, paras);
				break;
			//case DBUrlType.DBAccessOfMSSQL1:
			//    dt = DBAccessOfMSSQL1.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfMSSQL2:
			//    dt = DBAccessOfMSSQL2.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfOracle1:
			//    dt = DBAccessOfOracle1.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfOracle2:
			//    dt = DBAccessOfOracle2.RunSQLReturnTable(sql);
			//    break;
			//case DBUrlType.DBAccessOfOLE:
			//    dt = DBAccessOfOLE.RunSQLReturnTable(sql);
			//    break;
			default:
				throw new RuntimeException("@没有设置DB类型。");
		}

		if (dt.Rows.size() == 0)
		{
			return 0;
		}

		//设置查询.
		QueryObject.InitEntitiesByDataTable(ens, dt, fullAttrs);

		int i = dt.Rows.size();
		//dt.Dispose();
		return i;
		//return dt.Rows.size();
	}
}