package BP.En;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DBUrl;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Para;
import BP.DA.Paras;
import BP.Sys.SystemConfig;

//
//简介：负责存取数据的类
//创建时间：2002-10
//最后修改时间：2002-10
//

public class EntityDBAccess
{
	// 对实体的基本操作
	/**
	 * 删除
	 * 
	 * @param en
	 * @return
	 */
	public static int Delete(Entity en)
	{
		if (en.getEnMap().getEnType() == EnType.View)
		{
			return 0;
		}
		
		switch (en.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				return DBAccess.RunSQL(en.getSQLCash().Delete,
						SqlBuilder.GenerParasPK(en));
			default:
				throw new RuntimeException("@没有设置类型。");
		}
	}
	
	public static int Update(Entity en)
	{
		try
		{
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					switch (SystemConfig.getAppCenterDBType())
					{
						case Oracle:
							return DBAccess.RunSQL(en.getSQLCash().Update,
									SqlBuilder.GenerParas(en, null));
						case Access:
							return DBAccess.RunSQL(SqlBuilder.UpdateOfMSAccess(
									en, null));
						default:
							return DBAccess.RunSQL(SqlBuilder.Update(en, null));
					}
					// case DBAccessOfMSMSSQL:
					// return DBAccessOfMSMSSQL.RunSQL(SqlBuilder.Update(en,
					// null));
					// case DBAccessOfOracle:
					// return DBAccessOfOracle.RunSQL(SqlBuilder.Update(en,
					// null));
				default:
					throw new RuntimeException("@没有设置类型。");
			}
		} catch (RuntimeException ex)
		{
			if (BP.Sys.SystemConfig.getIsDebug())
			{
				try
				{
					en.CheckPhysicsTable();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			throw ex;
		}
		
	}
	
	/**
	 * 更新
	 * 
	 * @param en
	 *            产生要更新的语句
	 * @param keys
	 *            要更新的属性(null,认为更新全部)
	 * @return sql
	 */
	public static int Update(Entity en, String[] keys)
	{
		if (en.getEnMap().getEnType() == EnType.View)
		{
			return 0;
		}
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
							return DBAccess.RunSQL(en.getSQLCash()
									.GetUpdateSQL(en, keys), SqlBuilder
									.GenerParas(en, keys));
						case Informix:
							return DBAccess.RunSQL(en.getSQLCash()
									.GetUpdateSQL(en, keys), SqlBuilder
									.GenerParas_Update_Informix(en, keys));
						case Access:
							return DBAccess.RunSQL(SqlBuilder.UpdateOfMSAccess(
									en, keys));
						default:
							// return
							// DBAccess.RunSQL(en.SQLCash.GetUpdateSQL(en,
							// keys),
							// SqlBuilder.GenerParas(en, keys));
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
								return DBAccess.RunSQL(en.getSQLCash()
										.GetUpdateSQL(en, keys), ps);
							} else
							{
								return DBAccess.RunSQL(en.getSQLCash()
										.GetUpdateSQL(en, keys), SqlBuilder
										.GenerParas(en, keys));
							}
							
					}
					// case DBAccessOfMSMSSQL:
					// return DBAccessOfMSMSSQL.RunSQL(SqlBuilder.Update(en,
					// keys));
					// case DBAccessOfOracle:
					//
					// return DBAccessOfOracle.RunSQL(SqlBuilder.Update(en,
					// keys));
				default:
					throw new RuntimeException("@没有设置类型。");
			}
		} catch (RuntimeException ex)
		{
			if (BP.Sys.SystemConfig.getIsDebug())
			{
				try
				{
					en.CheckPhysicsTable();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			throw ex;
		}
	}
	
	/**
	 * 增加
	 * 
	 * @param en
	 * @return
	 */
	public static int Insert_del(Entity en)
	{
		if (en.getEnMap().getEnType() == EnType.Ext)
		{
			throw new RuntimeException("@实体[" + en.getEnDesc()
					+ "]是扩展类型，不能执行插入。");
		}
		
		if (en.getEnMap().getEnType() == EnType.View)
		{
			throw new RuntimeException("@实体[" + en.getEnDesc()
					+ "]是视图类型，不能执行插入。");
		}
		
		try
		{
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					return DBAccess.RunSQL(SqlBuilder.Insert(en));
					// case DBAccessOfMSMSSQL :
					// return DBAccessOfMSMSSQL.RunSQL(SqlBuilder.Insert(en));
					// case DBAccessOfOracle :
					// return DBAccessOfOracle.RunSQL(SqlBuilder.Insert(en));
				default:
					throw new RuntimeException("@没有设置类型。");
			}
		} catch (RuntimeException ex)
		{
			try
			{
				en.CheckPhysicsTable();
			} catch (Exception e)
			{
				e.printStackTrace();
			} // 检查物理表。
			throw ex;
		}
	}
	
	// 产生序列号码方法
	
	public static int RetrieveV2(Entity en, String sql, Paras paras)
	{
		try
		{
			DataTable dt = new DataTable();
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					dt = DBAccess.RunSQLReturnTable(sql, paras);
					break;
				// case DBAccessOfMSMSSQL:
				// dt = DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
				// break;
				// case DBAccessOfOracle:
				// dt = DBAccessOfOracle.RunSQLReturnTable(sql);
				// break;
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
			return i;
		} catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	
	public static int Retrieve(Entity en, String sql, Paras paras)
	{
		
		DataTable dt;
		switch (en.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				dt = DBAccess.RunSQLReturnTable(sql, paras);
				break;
			// case DBAccessOfMSMSSQL:
			// dt = DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
			// break;
			// case DBAccessOfOracle:
			// dt = DBAccessOfOracle.RunSQLReturnTable(sql);
			// break;
			default:
				throw new RuntimeException("@没有设置DB类型。");
		}
		
		if (null == dt || null == dt.Rows || dt.Rows.size() == 0)
		{
			return 0;
		}
		Attrs attrs = en.getEnMap().getAttrs();
		
		EntityDBAccess.fullDate(dt, en, attrs);
		
		int i = dt.Rows.size();
		// dt.dispose();
		return i;
	}
	
	public static int Retrieve2017(Entity en, String sql, Paras paras)
	{
		
		DataTable dt;
		switch (en.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				dt = DBAccess.RunSQLReturnTable(sql, paras);
				break;
			// case DBAccessOfMSMSSQL:
			// dt = DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
			// break;
			// case DBAccessOfOracle:
			// dt = DBAccessOfOracle.RunSQLReturnTable(sql);
			// break;
			default:
				throw new RuntimeException("@没有设置DB类型。");
		}
		
		if (null == dt || null == dt.Rows || dt.Rows.size() == 0)
		{
			return 0;
		}
		Attrs attrs = en.getEnMap().getAttrs();
		EntityDBAccess.fullDate2017(dt, en, attrs);
		int i = dt.Rows.size();
		// dt.dispose();
		return i;
	}
	
	/**
	 * 查询
	 * 
	 * @param en
	 *            实体
	 * @param sql
	 *            组织的查询语句
	 * @return
	 */
	public static int Retrieve(Entity en, String sql)
	{
		try
		{
			DataTable dt = new DataTable();
			switch (en.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					dt = DBAccess.RunSQLReturnTable(sql);
					break;
				// case DBAccessOfMSMSSQL:
				// dt = DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
				// break;
				// case DBAccessOfOracle:
				// dt = DBAccessOfOracle.RunSQLReturnTable(sql);
				// break;
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
			// dt.dispose();
			return i;
		} catch (RuntimeException ex)
		{
			throw ex;
		}
	}
	
	private static void fullDate(DataTable dt, Entity en, Attrs attrs)
	{
		
		if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.Oracle )
		{
			for (Attr attr : attrs)
			{			
				en.getRow().SetValByKey(attr.getKey(), dt.Rows.get(0).getValue(attr.getKey().toUpperCase()));
			 }
			
			return;
		}
		
		
		
		
		for (Attr attr : attrs)
		{ 			
			en.getRow().SetValByKey(attr.getKey(), dt.Rows.get(0).getValue(attr.getKey()));
		 
		}
	}
	
	private static void fullDate2017(DataTable dt, Entity en, Attrs attrs)
	{
		for (Attr attr : attrs)
		{
			en.getRow().SetValByKey_2017(attr.getKey(),
					dt.Rows.get(0).getValue(attr.getKey()));
			/*
			 * warning en.getRow().SetValByKey(attr.getKey(),
			 * dt.Rows[0][attr.getKey()]);
			 */
		}
	}
	
	public static int Retrieve(Entities ens, String sql)
	{
		try
		{
			DataTable dt = new DataTable();
			switch (ens.getGetNewEntity().getEnMap().getEnDBUrl()
					.getDBUrlType())
			{
				case AppCenterDSN:
					dt = DBAccess.RunSQLReturnTable(sql);
					break;
				// case DBAccessOfMSMSSQL:
				// dt = DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
				// break;
				// case DBAccessOfOracle:
				// dt = DBAccessOfOracle.RunSQLReturnTable(sql);
				// break;
				// case DBAccessOfOLE:
				// dt = DBAccessOfOLE.RunSQLReturnTable(sql);
				// break;
				default:
					throw new RuntimeException("@没有设置DB类型。");
			}
			
			if (dt.Rows.size() == 0)
			{
				return 0;
			}
			
			Map enMap = ens.getGetNewEntity().getEnMap();
			Attrs attrs = enMap.getAttrs();
			
			// Entity en1 = ens.GetNewEntity;
			for (DataRow dr : dt.Rows)
			{
				Entity en = ens.getGetNewEntity();
				// Entity en = en1.CreateInstance();
				for (Attr attr : attrs)
				{
					en.getRow().SetValByKey(attr.getKey(),
							dr.getValue(attr.getKey()));
					/*
					 * warning en.getRow().SetValByKey(attr.getKey(),
					 * dr[attr.getKey()]);
					 */
				}
				ens.AddEntity(en);
			}
			int i = dt.Rows.size();
			// dt.dispose();
			return i;
			// return dt.Rows.Count;
		} catch (RuntimeException ex)
		{
			// ens.GetNewEntity.CheckPhysicsTable();
			throw new RuntimeException("@在["
					+ ens.getGetNewEntity().getEnDesc() + "]查询时出现错误:"
					+ ex.getMessage());
		}
	}
	
	public static int Retrieve(Entities ens, String sql, Paras paras,
			String[] fullAttrs)
	{
		DataTable dt = null;
		switch (ens.getGetNewEntity().getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				dt = DBAccess.RunSQLReturnTable(sql, paras);
				break;
			// case DBAccessOfMSMSSQL:
			// dt = DBAccessOfMSMSSQL.RunSQLReturnTable(sql);
			// break;
			// case DBAccessOfOracle:
			// dt = DBAccessOfOracle.RunSQLReturnTable(sql);
			// break;
			// case DBAccessOfOLE:
			// dt = DBAccessOfOLE.RunSQLReturnTable(sql);
			// break;
			default:
				throw new RuntimeException("@没有设置DB类型。");
		}
		
		// if(dt==null){
		// return 0;
		// }
		
		if (dt.Rows.size() == 0)
		{
			return 0;
		}
		
		// 设置查询.
		QueryObject.InitEntitiesByDataTable(ens, dt, fullAttrs);
		
		int i = dt.Rows.size();
		// dt.dispose();
		return i;
		// return dt.Rows.Count;
	}
}