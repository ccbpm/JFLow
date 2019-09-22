package BP.DTS;

import BP.DA.DBAccess;
import BP.DA.DBUrlType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;

/**
 * EnMap 的摘要说明。
 */
public abstract class DataIOEn
{
	
	/**
	 * 获取在 DTS 中的编号。
	 * 
	 * @return
	 */
	public final String GetNoInDTS()
	{
		// DTS.SysDTS dts =new SysDTS();
		// QueryObject qo = new QueryObject(dts);
		// qo.AddWhere(DTSAttr.RunText,this.ToString());
		// if (qo.DoQuery()==0)
		// throw new Exception("没有取道调度的编号.");
		// else
		// return dts.No;
		
		return null;
	}
	
	/**
	 * 执行它 在线程中。
	 */
	public final void DoItInThread()
	{
		// ThreadStart ts = new ThreadStart(this.Do);
		// Thread thread = new Thread(ts);
		// thread.start();
	}
	
	// 基本属性.
	/**
	 * 选择sql .
	 */
	public String SELECTSQL = null;
	/**
	 * 数据同步类型．
	 */
	public DoType HisDoType = DoType.UnName;
	/**
	 * 运行类型时间
	 */
	public RunTimeType HisRunTimeType = RunTimeType.UnName;
	/**
	 * 标题
	 */
	public String Title = "未命名数据同步";
	/**
	 * WHERE .
	 */
	public String FromWhere = null;
	/**
	 * FFs
	 */
	public FFs FFs = null;
	/**
	 * 从Table .
	 */
	public String FromTable = null;
	/**
	 * 到Table.
	 */
	public String ToTable = null;
	/**
	 * 从DBUrl.
	 */
	public DBUrlType FromDBUrl = DBUrlType.forValue(0);
	/**
	 * 到DBUrl.
	 */
	public DBUrlType ToDBUrl = DBUrlType.forValue(0);
	/**
	 * 更新语句
	 */
	public String UPDATEsql;
	/**
	 * 备注
	 */
	public String Note = "无";
	
	public String DefaultEveryMonth = "99";
	public String DefaultEveryDay = "99";
	public String DefaultEveryHH = "99";
	public String DefaultEveryMin = "99";
	/**
	 * 类别
	 */
	public String FK_Sort = "0";
	
	public DataIOEn()
	{
	}
	
	public final void Directly(String fromSQL, String toPTable, String pk)
	{
		this.Directly(fromSQL, toPTable);
		this.ToDBUrlRunSQL("CREATE INDEX " + toPTable + "ID ON " + toPTable
				+ " (" + pk + ")");
	}
	
	public final void Directly(String fromSQL, String toPTable, String pk1,
			String pk2)
	{
		this.Directly(fromSQL, toPTable);
		this.ToDBUrlRunSQL("CREATE INDEX " + toPTable + "ID ON " + toPTable
				+ " (" + pk1 + "," + pk2 + ")");
	}
	
	public final void Directly(String fromSQL, String toPTable, String pk1,
			String pk2, String pk3)
	{
		this.Directly(fromSQL, toPTable);
		this.ToDBUrlRunSQL("CREATE INDEX " + toPTable + "ID ON " + toPTable
				+ " (" + pk1 + "," + pk2 + "," + pk3 + ")");
	}
	
	public final void Directly(String fromSQL, String toPTable)
	{
		BP.DA.DataTable dt = this.FromDBUrlRunSQLReturnTable(fromSQL);
		String sql = null;
		sql = "INSERT INTO " + toPTable + "(";
		for (BP.DA.DataColumn dc : dt.Columns)
		{
			sql += dc.ColumnName + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ") VALUES (";
		try
		{
			this.ToDBUrlRunSQL(" drop table " + toPTable);
		} catch (java.lang.Exception e)
		{
		}
		
		String createTable = "CREATE TABLE " + toPTable + " (";
		for (DataColumn dc : dt.Columns)
		{
			if (dc.DataType.toString().equals("System.String"))
			{
				createTable += dc.ColumnName + " nvarchar (700) NULL  ,";
			} else if (dc.DataType.toString().equals("System.Int16")
					|| dc.DataType.toString().equals("System.Int32")
					|| dc.DataType.toString().equals("System.Int64"))
			{
				createTable += dc.ColumnName + " int NULL,";
			} else if (dc.DataType.toString().equals("System.Decimal"))
			{
				createTable += dc.ColumnName + " decimal NULL,";
			} else
			{
				createTable += dc.ColumnName + " float NULL,";
			}
		}
		createTable = createTable.substring(0, createTable.length() - 1);
		createTable += ")";
		this.ToDBUrlRunSQL(createTable);
		
		String sql2 = null;
		String errormsg = "";
		for (DataRow dr : dt.Rows)
		{
			sql2 = sql;
			for (DataColumn dc : dt.Columns)
			{
				/*
				 * warning sql2+="'"+dr.getValue(dc.ColumnName)+"',";
				 */
				sql2 += "'" + dr.getValue(dc.ColumnName) + "',";
			}
			sql2 = sql2.substring(0, sql2.length() - 1) + ")";
			try
			{
				this.ToDBUrlRunSQL(sql2);
			} catch (RuntimeException ex)
			{
				errormsg += ex.getMessage();
			}
		}
		if (!errormsg.equals(""))
		{
			throw new RuntimeException(" data output error: " + errormsg);
		}
		
	}
	
	public final DataTable FromDBUrlRunSQLReturnTable(String selectSql)
	{
		// 得到数据源泉．
		DataTable dt = new DataTable();
		switch (this.FromDBUrl)
		{
			case AppCenterDSN:
				try
				{
					dt = DBAccess.RunSQLReturnTable(selectSql);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				break;
			// case DBAccessOfMSMSSQL:
			// dt=DBAccessOfMSMSSQL.RunSQLReturnTable(selectSql);
			// break;
			// case DBAccessOfODBC:
			// dt=DBAccessOfODBC.RunSQLReturnTable(selectSql);
			// break;
			// case DBAccessOfOLE:
			// dt=DBAccessOfOLE.RunSQLReturnTable(selectSql);
			// break;
			// case DBAccessOfOracle:
			// dt=DBAccessOfOracle.RunSQLReturnTable(selectSql);
			// break;
			// case DBUrlType.DBAccessOfOracle1:
			// dt=DBAccessOfOracle1.RunSQLReturnTable( selectSql );
			// break;
			default:
				break;
		}
		return dt;
	}
	
	public final int ToDBUrlRunSQL(String sql)
	{
		switch (this.ToDBUrl)
		{
			case AppCenterDSN:
				try
				{
					return DBAccess.RunSQL(sql);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				// case DBAccessOfMSMSSQL:
				// return DBAccessOfMSMSSQL.RunSQL(sql);
				// case DBAccessOfODBC:
				// return DBAccessOfODBC.RunSQL(sql);
				// case DBAccessOfOLE:
				// return DBAccessOfOLE.RunSQL(sql);
				// case DBAccessOfOracle:
				// return DBAccessOfOracle.RunSQL(sql);
			default:
				throw new RuntimeException("@ error it");
		}
	}
	
	public final int ToDBUrlRunDropTable(String table)
	{
		switch (this.ToDBUrl)
		{
		// case AppCenterDSN:
		// return DBAccess.RunSQLDropTable(table);
		// case DBAccessOfMSMSSQL:
		// return DBAccessOfMSMSSQL.RunSQL(table);
		// case DBAccessOfODBC:
		// return DBAccessOfODBC.RunSQL(table);
		// case DBAccessOfOLE:
		// return DBAccessOfOLE.RunSQL(table);
		// case DBAccessOfOracle:
		// return DBAccessOfOracle.RunSQLTRUNCATETable(table);
			default:
				throw new RuntimeException("@ error it");
		}
	}
	
	public final boolean ToDBUrlIsExit(String sql)
	{
		switch (this.ToDBUrl)
		{
			case AppCenterDSN:
				try
				{
					return DBAccess.IsExits(sql);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				// case DBAccessOfMSMSSQL:
				// return DBAccessOfMSMSSQL.IsExits(sql);
				// case DBAccessOfODBC:
				// return DBAccessOfODBC.IsExits(sql);
				// case DBAccessOfOLE:
				// return DBAccessOfOLE.IsExits(sql);
				// case DBAccessOfOracle:
				// return DBAccessOfOracle.IsExits(sql);
			default:
				throw new RuntimeException("@ error it");
		}
	}
	
	// 方法， New 2005-01-29
	
	public void Do() throws Exception
	{
		if (this.HisDoType == DoType.UnName)
		{
			throw new RuntimeException("@没有说明同步的类型,请在基础信息里面设置同步的类型(构造函数．)．");
		}
		
		if (this.HisDoType == DoType.DeleteInsert)
		{
			this.DeleteInsert();
		}
		
		if (this.HisDoType == DoType.Inphase)
		{
			this.Inphase();
		}
		
		if (this.HisDoType == DoType.Incremental)
		{
			this.Incremental();
		}
	}
	
	public final void Incremental() throws Exception
	{
		this.DoBefore();
		DataTable FromDataTable = this.GetFromDataTable();
		String isExitSql = "";
		String InsertSQL = "";
		for (DataRow FromDR : FromDataTable.Rows)
		{
			isExitSql = "SELECT * FROM " + this.ToTable + " WHERE ";
			for (FF ff : this.FFs)
			{
				if (!ff.IsPK)
				{
					continue;
				}
				isExitSql += ff.ToField + "='" + FromDR.getValue(ff.FromField)
						+ "' AND ";
			}
			
			isExitSql = isExitSql.substring(0, isExitSql.length() - 5);
			
			try
			{
				if (DBAccess.IsExits(isExitSql)) // 如果不存在就 insert.
				{
					continue;
				}
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
			InsertSQL = "INSERT INTO " + this.ToTable + "(";
			for (FF ff : this.FFs)
			{
				InsertSQL += ff.ToField.toString() + ",";
			}
			InsertSQL = InsertSQL.substring(0, InsertSQL.length() - 1);
			InsertSQL += ") values(";
			for (FF ff : this.FFs)
			{
				if (ff.DataType == DataType.AppString
						|| ff.DataType == DataType.AppDateTime)
				{
					InsertSQL += "'" + FromDR.getValue(ff.FromField).toString()
							+ "',";
				} else
				{
					InsertSQL += FromDR.getValue(ff.FromField).toString() + ",";
				}
				
			}
			InsertSQL = InsertSQL.substring(0, InsertSQL.length() - 1);
			InsertSQL += ")";
			switch (this.ToDBUrl)
			{
				case AppCenterDSN:
					try
					{
						DBAccess.RunSQL(InsertSQL);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				// case DBAccessOfMSMSSQL:
				// DBAccessOfOLE.RunSQL(InsertSQL);
				// break;
				// case DBAccessOfOLE:
				// DBAccessOfOLE.RunSQL(InsertSQL);
				// break;
				// case DBAccessOfOracle:
				// DBAccessOfOracle.RunSQL(InsertSQL);
				// break;
				// case DBAccessOfODBC:
				// DBAccessOfODBC.RunSQL(InsertSQL);
				// break;
				/*
				 * warning case DA.DBUrlType.AppCenterDSN:
				 * DBAccess.RunSQL(InsertSQL); break; case
				 * DA.DBUrlType.DBAccessOfMSMSSQL:
				 * DBAccessOfOLE.RunSQL(InsertSQL); break; case
				 * DA.DBUrlType.DBAccessOfOLE: DBAccessOfOLE.RunSQL(InsertSQL);
				 * break; case DA.DBUrlType.DBAccessOfOracle:
				 * DBAccessOfOracle.RunSQL(InsertSQL); break; case
				 * DA.DBUrlType.DBAccessOfODBC:
				 * DBAccessOfODBC.RunSQL(InsertSQL); break;
				 */
				default:
					break;
			}
			// 执行插入操作
			
		}
		// 结束,开始执行更新
		
		this.DoAfter(); // 调用，更新之后的业务处理。
	}
	
	protected void DoBefore()
	{
	}
	
	protected void DoAfter()
	{
	}
	
	public final void DeleteInsert() throws Exception
	{
		this.DoBefore(); // 调用业务处理。
		DataTable FromDataTable = this.GetFromDataTable();
		this.DeleteObjData();
		String InsertSQL = "";
		for (DataRow FromDR : FromDataTable.Rows)
		{
			
			InsertSQL = "INSERT INTO " + this.ToTable + "(";
			for (FF ff : this.FFs)
			{
				InsertSQL += ff.ToField.toString() + ",";
			}
			InsertSQL = InsertSQL.substring(0, InsertSQL.length() - 1);
			InsertSQL += ") values(";
			for (FF ff : this.FFs)
			{
				if (ff.DataType == DataType.AppString
						|| ff.DataType == DataType.AppDateTime)
				{
					/*
					 * warning
					 * InsertSQL+="'"+FromDR[ff.FromField].toString()+"',";
					 */
					InsertSQL += "'" + FromDR.getValue(ff.FromField).toString()
							+ "',";
				} else
				{
					/*
					 * warning InsertSQL+=FromDR[ff.FromField].toString()+",";
					 */
					InsertSQL += FromDR.getValue(ff.FromField).toString() + ",";
				}
			}
			InsertSQL = InsertSQL.substring(0, InsertSQL.length() - 1);
			InsertSQL += ")";
			
			switch (this.ToDBUrl)
			{
				case AppCenterDSN:
					try
					{
						DBAccess.RunSQL(InsertSQL);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				// case DBAccessOfMSMSSQL:
				// DBAccessOfMSMSSQL.RunSQL(InsertSQL);
				// break;
				// case DBAccessOfOLE:
				// DBAccessOfOLE.RunSQL(InsertSQL);
				// break;
				// case DBAccessOfOracle:
				// DBAccessOfOracle.RunSQL(InsertSQL);
				// break;
				// case DBAccessOfODBC:
				// DBAccessOfODBC.RunSQL(InsertSQL);
				// break;
				/*
				 * warning case DA.DBUrlType.AppCenterDSN:
				 * DBAccess.RunSQL(InsertSQL); break; case
				 * DA.DBUrlType.DBAccessOfMSMSSQL:
				 * DBAccessOfMSMSSQL.RunSQL(InsertSQL); break; case
				 * DA.DBUrlType.DBAccessOfOLE: DBAccessOfOLE.RunSQL(InsertSQL);
				 * break; case DA.DBUrlType.DBAccessOfOracle:
				 * DBAccessOfOracle.RunSQL(InsertSQL); break; case
				 * DA.DBUrlType.DBAccessOfODBC:
				 * DBAccessOfODBC.RunSQL(InsertSQL); break;
				 */
				default:
					break;
			}
			
		}
		
		this.DoAfter(); // 调用业务处理。
		
	}
	
	public final void DeleteObjData() throws Exception
	{
		
		switch (this.ToDBUrl)
		{
			case AppCenterDSN:
				DBAccess.RunSQL("DELETE FROM  " + this.ToTable);
				break;
		 
			// case DBAccessOfOLE:
			// DBAccessOfOLE.RunSQL("DELETE FROM  " + this.ToTable);
			// break;
			// case DBAccessOfOracle:
			// DBAccessOfOracle.RunSQL("DELETE  FROM " + this.ToTable);
			// break;
			// case DBAccessOfODBC:
			// DBAccessOfODBC.RunSQL("DELETE FROM  " + this.ToTable);
			// break;
			/*
			 * warning case DA.DBUrlType.AppCenterDSN:
			 * DBAccess.RunSQL("DELETE FROM  " + this.ToTable); break; case
			 * DA.DBUrlType.DBAccessOfMSMSSQL: DBAccess.RunSQL("DELETE  FROM " +
			 * this.ToTable); break; case DA.DBUrlType.DBAccessOfOLE:
			 * DBAccessOfOLE.RunSQL("DELETE FROM  " + this.ToTable); break; case
			 * DA.DBUrlType.DBAccessOfOracle:
			 * DBAccessOfOracle.RunSQL("DELETE  FROM " + this.ToTable); break;
			 * case DA.DBUrlType.DBAccessOfODBC:
			 * DBAccessOfODBC.RunSQL("DELETE FROM  " + this.ToTable); break;
			 */
			default:
				break;
		}
	}
	
	public final DataTable GetToDataTable() throws Exception
	{
		String sql = "SELECT * FROM " + this.ToTable;
		DataTable FromDataTable = new DataTable();
		switch (this.ToDBUrl)
		{
			case AppCenterDSN:
				FromDataTable = DBAccess.RunSQLReturnTable(sql);
				break;
			 
			// case DBAccessOfOLE:
			// FromDataTable=DBAccessOfOLE.RunSQLReturnTable(sql);
			// break;
			// case DBAccessOfOracle:
			// FromDataTable=DBAccessOfOracle.RunSQLReturnTable(sql);
			// break;
			// case DBAccessOfODBC:
			// FromDataTable=DBAccessOfODBC.RunSQLReturnTable(sql);
			// break;
			/*
			 * warning case DA.DBUrlType.AppCenterDSN:
			 * FromDataTable=DBAccess.RunSQLReturnTable(sql); break; case
			 * DA.DBUrlType.DBAccessOfMSMSSQL:
			 * FromDataTable=DBAccess.RunSQLReturnTable(sql); break; case
			 * DA.DBUrlType.DBAccessOfOLE:
			 * FromDataTable=DBAccessOfOLE.RunSQLReturnTable(sql); break; case
			 * DA.DBUrlType.DBAccessOfOracle:
			 * FromDataTable=DBAccessOfOracle.RunSQLReturnTable(sql); break;
			 * case DA.DBUrlType.DBAccessOfODBC:
			 * FromDataTable=DBAccessOfODBC.RunSQLReturnTable(sql); break;
			 */
			default:
				throw new RuntimeException("the to dburl error DBUrlType ");
		}
		
		return FromDataTable;
		
	}
	
	public final DataTable GetFromDataTable() throws Exception
	{
		String FromSQL = "SELECT ";
		for (FF ff : this.FFs)
		{
			if (ff.DataType == DataType.AppDateTime)
			{
				FromSQL += " CASE  "
						+ " when datalength( CONVERT(NVARCHAR,datepart(month,"
						+ ff.FromField + " )))=1 then datename(year,"
						+ ff.FromField
						+ " )+'-'+('0'+CONVERT(NVARCHAR,datepart(month,"
						+ ff.FromField + " ))) " + " else " + " datename(year,"
						+ ff.FromField
						+ " )+'-'+CONVERT(NVARCHAR,datepart(month,"
						+ ff.FromField + " )) " + " END " + " AS "
						+ ff.FromField + " , ";
			} else
			{
				FromSQL += ff.FromField + ",";
			}
		}
		
		FromSQL = FromSQL.substring(0, FromSQL.length() - 1);
		FromSQL += " from " + this.FromTable;
		FromSQL += this.FromWhere;
		DataTable FromDataTable = new DataTable();
		switch (this.FromDBUrl)
		{
			case AppCenterDSN:
				FromDataTable = DBAccess.RunSQLReturnTable(FromSQL);
				break;
			 
			// case DBAccessOfOLE:
			// FromDataTable=DBAccessOfOLE.RunSQLReturnTable(FromSQL);
			// break;
			// case DBAccessOfOracle:
			// FromDataTable=DBAccessOfOracle.RunSQLReturnTable(FromSQL);
			// break;
			// case DBAccessOfODBC:
			// FromDataTable=DBAccessOfODBC.RunSQLReturnTable(FromSQL);
			// break;
			/*
			 * warning case DA.DBUrlType.AppCenterDSN:
			 * FromDataTable=DBAccess.RunSQLReturnTable(FromSQL); break; case
			 * DA.DBUrlType.DBAccessOfMSMSSQL:
			 * FromDataTable=DBAccess.RunSQLReturnTable(FromSQL); break; case
			 * DA.DBUrlType.DBAccessOfOLE:
			 * FromDataTable=DBAccessOfOLE.RunSQLReturnTable(FromSQL); break;
			 * case DA.DBUrlType.DBAccessOfOracle:
			 * FromDataTable=DBAccessOfOracle.RunSQLReturnTable(FromSQL); break;
			 * case DA.DBUrlType.DBAccessOfODBC:
			 * FromDataTable=DBAccessOfODBC.RunSQLReturnTable(FromSQL); break;
			 */
			default:
				throw new RuntimeException("the from dburl error DBUrlType ");
		}
		return FromDataTable;
	}
	
	public final void Inphase() throws Exception
	{
		this.DoBefore();
		
		String FromSQL = "SELECT ";
		for (FF ff : this.FFs)
		{
			if (ff.DataType == DataType.AppDateTime)
			{
				FromSQL += " CASE  "
						+ " when datalength( CONVERT(NVARCHAR,datepart(month,"
						+ ff.FromField + " )))=1 then datename(year,"
						+ ff.FromField
						+ " )+'-'+('0'+CONVERT(NVARCHAR,datepart(month,"
						+ ff.FromField + " ))) " + " else " + " datename(year,"
						+ ff.FromField
						+ " )+'-'+CONVERT(NVARCHAR,datepart(month,"
						+ ff.FromField + " )) " + " END " + " AS "
						+ ff.FromField + " , ";
			} else
			{
				FromSQL += ff.FromField + ",";
			}
		}
		FromSQL = FromSQL.substring(0, FromSQL.length() - 1);
		FromSQL += " from " + this.FromTable;
		FromSQL += this.FromWhere;
		DataTable FromDataTable = new DataTable();
		switch (this.FromDBUrl)
		{
			case AppCenterDSN:
				FromDataTable = DBAccess.RunSQLReturnTable(FromSQL);
				break;
		 
			// case DBAccessOfOLE:
			// FromDataTable=DBAccessOfOLE.RunSQLReturnTable(FromSQL);
			// break;
			// case DBAccessOfOracle:
			// FromDataTable=DBAccessOfOracle.RunSQLReturnTable(FromSQL);
			// break;
			// case DBAccessOfODBC:
			// FromDataTable=DBAccessOfODBC.RunSQLReturnTable(FromSQL);
			// break;
			/*
			 * warning case DA.DBUrlType.AppCenterDSN:
			 * FromDataTable=DBAccess.RunSQLReturnTable(FromSQL); break; case
			 * DA.DBUrlType.DBAccessOfMSMSSQL:
			 * FromDataTable=DBAccess.RunSQLReturnTable(FromSQL); break; case
			 * DA.DBUrlType.DBAccessOfOLE:
			 * FromDataTable=DBAccessOfOLE.RunSQLReturnTable(FromSQL); break;
			 * case DA.DBUrlType.DBAccessOfOracle:
			 * FromDataTable=DBAccessOfOracle.RunSQLReturnTable(FromSQL); break;
			 * case DA.DBUrlType.DBAccessOfODBC:
			 * FromDataTable=DBAccessOfODBC.RunSQLReturnTable(FromSQL); break;
			 */
			default:
				break;
		}
		
		// 得到目的表(字段只包含主键)
		String ToSQL = "SELECT ";
		for (FF ff : this.FFs)
		{
			if (!ff.IsPK)
			{
				continue;
			}
			ToSQL += ff.ToField + ",";
		}
		ToSQL = ToSQL.substring(0, ToSQL.length() - 1);
		ToSQL += " FROM " + this.ToTable;
		DataTable ToDataTable = new DataTable();
		switch (this.ToDBUrl)
		{
			case AppCenterDSN:
				ToDataTable = DBAccess.RunSQLReturnTable(ToSQL);
				break;
			 
			// case DBAccessOfOLE:
			// ToDataTable=DBAccessOfOLE.RunSQLReturnTable(ToSQL);
			// break;
			// case DBAccessOfOracle:
			// ToDataTable=DBAccessOfOracle.RunSQLReturnTable(ToSQL);
			// break;
			// case DBAccessOfODBC:
			// ToDataTable=DBAccessOfODBC.RunSQLReturnTable(ToSQL);
			// break;
			/*
			 * warning case DA.DBUrlType.AppCenterDSN:
			 * ToDataTable=DBAccess.RunSQLReturnTable(ToSQL); break; case
			 * DA.DBUrlType.DBAccessOfMSMSSQL:
			 * ToDataTable=DBAccess.RunSQLReturnTable(ToSQL); break; case
			 * DA.DBUrlType.DBAccessOfOLE:
			 * ToDataTable=DBAccessOfOLE.RunSQLReturnTable(ToSQL); break; case
			 * DA.DBUrlType.DBAccessOfOracle:
			 * ToDataTable=DBAccessOfOracle.RunSQLReturnTable(ToSQL); break;
			 * case DA.DBUrlType.DBAccessOfODBC:
			 * ToDataTable=DBAccessOfODBC.RunSQLReturnTable(ToSQL); break;
			 */
			default:
				break;
		}
		
		String SELECTSQL = "";
		String InsertSQL = "";
		String UpdateSQL = "";
		String DeleteSQL = "";
		// int i=0;
		// int j=0;
		int result = 0;
		
		// 遍历源表
		for (DataRow FromDR : FromDataTable.Rows)
		{
			UpdateSQL = "UPDATE  " + this.ToTable + " SET ";
			for (FF ff : this.FFs)
			{
				switch (ff.DataType)
				{
					case DataType.AppDateTime:
					case DataType.AppString:
						UpdateSQL += ff.ToField + "='"
								+ FromDR.getValue(ff.FromField).toString()
								+ "',";
						/*
						 * warning UpdateSQL+= ff.ToField+
						 * "='"+FromDR[ff.FromField].toString()+"',";
						 */
						break;
					case DataType.AppFloat:
					case DataType.AppInt:
					case DataType.AppMoney:
					case DataType.AppRate:
					case DataType.AppDate:
					case DataType.AppDouble:
						UpdateSQL += ff.ToField + "="
								+ FromDR.getValue(ff.FromField).toString()
								+ ",";
						/*
						 * warning UpdateSQL+= ff.ToField+
						 * "="+FromDR[ff.FromField].toString()+",";
						 */
						break;
					default:
						throw new RuntimeException("没有涉及到的数据类型.");
				}
			}
			UpdateSQL = UpdateSQL.substring(0, UpdateSQL.length() - 1);
			UpdateSQL += " WHERE ";
			for (FF ff : this.FFs)
			{
				if (!ff.IsPK)
				{
					continue;
				}
				UpdateSQL += ff.ToField + "='" + FromDR.getValue(ff.FromField)
						+ "' AND ";
				/*
				 * warning UpdateSQL+= ff.ToField +"='"+FromDR[ff.FromField]+
				 * "' AND ";
				 */
			}
			
			UpdateSQL = UpdateSQL.substring(0, UpdateSQL.length() - 5);
			switch (this.ToDBUrl)
			{
				case AppCenterDSN:
					result = DBAccess.RunSQL(UpdateSQL);
					break;
				  
				default:
					break;
			}
			if (result == 0)
			{
				// 插入操作
				InsertSQL = "INSERT INTO " + this.ToTable + "(";
				for (FF ff : this.FFs)
				{
					InsertSQL += ff.ToField.toString() + ",";
				}
				InsertSQL = InsertSQL.substring(0, InsertSQL.length() - 1);
				InsertSQL += ") values(";
				for (FF ff : this.FFs)
				{
					if (ff.DataType == DataType.AppString
							|| ff.DataType == DataType.AppDateTime)
					{
						InsertSQL += "'"
								+ FromDR.getValue(ff.FromField).toString()
								+ "',";
						/*
						 * warning
						 * InsertSQL+="'"+FromDR[ff.FromField].toString()+"',";
						 */
					} else
					{
						InsertSQL += FromDR.getValue(ff.FromField).toString()
								+ ",";
						/*
						 * warning
						 * InsertSQL+=FromDR[ff.FromField].toString()+",";
						 */
					}
				}
				InsertSQL = InsertSQL.substring(0, InsertSQL.length() - 1);
				InsertSQL += ")";
				switch (this.ToDBUrl)
				{
					case AppCenterDSN:
						DBAccess.RunSQL(InsertSQL);
						break;
					 
					default:
						break;
				}
			}
			
		}
		
		// 遍历目的表 如果该条记录存在,continue,如果该条记录不存在,则根据主键删除目的表的对应数据
		for (DataRow ToDR : ToDataTable.Rows)
		{
			SELECTSQL = "SELECT ";
			for (FF ff : this.FFs)
			{
				if (!ff.IsPK)
				{
					continue;
				}
				SELECTSQL += ff.FromField + ",";
			}
			SELECTSQL = SELECTSQL.substring(0, SELECTSQL.length() - 1);
			SELECTSQL += " FROM " + this.FromTable + " WHERE ";
			for (FF ff : this.FFs)
			{
				if (!ff.IsPK)
				{
					continue;
				}
				if (ff.DataType == DataType.AppDateTime)
				{
					SELECTSQL += " case "
							+ " when datalength( CONVERT(NVARCHAR,datepart(month,"
							+ ff.FromField + " )))=1 then datename(year,"
							+ ff.FromField
							+ " )+'-'+('0'+CONVERT(VARCHAR,datepart(month,"
							+ ff.FromField + " ))) " + " else "
							+ " datename(year," + ff.FromField
							+ " )+'-'+CONVERT(VARCHAR,datepart(month,"
							+ ff.FromField + " )) " + " END " + "='"
							+ ToDR.getValue(ff.ToField).toString() + "' AND ";
					/*
					 * warning SELECTSQL+=" case "+
					 * " when datalength( CONVERT(NVARCHAR,datepart(month,"
					 * +ff.FromField+" )))=1 then datename(year,"+ff.FromField+
					 * " )+'-'+('0'+CONVERT(VARCHAR,datepart(month,"
					 * +ff.FromField+" ))) "+ " else "+
					 * " datename(year,"+ff.FromField
					 * +" )+'-'+CONVERT(VARCHAR,datepart(month,"
					 * +ff.FromField+" )) "+ " END "+
					 * "='"+ToDR[ff.ToField].toString()+"' AND ";
					 */
				} else
				{
					if (ff.DataType == DataType.AppString)
					{
						SELECTSQL += ff.FromField + "='"
								+ ToDR.getValue(ff.ToField).toString()
								+ "' AND ";
						/*
						 * warning
						 * SELECTSQL+=ff.FromField+"='"+ToDR[ff.ToField].
						 * toString()+"' AND ";
						 */
					} else
					{
						SELECTSQL += ff.FromField + "="
								+ ToDR.getValue(ff.ToField).toString()
								+ " AND ";
						/*
						 * warning
						 * SELECTSQL+=ff.FromField+"="+ToDR[ff.ToField].toString
						 * ()+" AND ";
						 */
					}
				}
			}
			SELECTSQL = SELECTSQL.substring(0, SELECTSQL.length() - 5);
			// SELECTSQL+=this.FromWhere;
			result = 0;
			switch (this.FromDBUrl)
			{
			// case AppCenterDSN:
			// result=DBAccess.RunSQLReturnCOUNT(SELECTSQL);
			// break;
			// case DBAccessOfMSMSSQL:
			// result=DBAccess.RunSQLReturnCOUNT(SELECTSQL);
			// break;
			// case DBAccessOfOLE:
			// result=DBAccessOfOLE.RunSQLReturnCOUNT(SELECTSQL);
			// break;
			// case DBAccessOfOracle:
			// result=DBAccessOfOracle.RunSQL(SELECTSQL);
			// break;
			// case DBAccessOfODBC:
			// result=DBAccessOfODBC.RunSQLReturnCOUNT(SELECTSQL);
			// break;
			/*
			 * warning case DA.DBUrlType.AppCenterDSN:
			 * result=DBAccess.RunSQLReturnCOUNT(SELECTSQL); break; case
			 * DA.DBUrlType.DBAccessOfMSMSSQL:
			 * result=DBAccess.RunSQLReturnCOUNT(SELECTSQL); break; case
			 * DA.DBUrlType.DBAccessOfOLE:
			 * result=DBAccessOfOLE.RunSQLReturnCOUNT(SELECTSQL); break; case
			 * DA.DBUrlType.DBAccessOfOracle:
			 * result=DBAccessOfOracle.RunSQL(SELECTSQL); break; case
			 * DA.DBUrlType.DBAccessOfODBC:
			 * result=DBAccessOfODBC.RunSQLReturnCOUNT(SELECTSQL); break;
			 */
				default:
					break;
			}
			
			if (result != 1)
			{
				// delete
				DeleteSQL = "delete FROM  " + this.ToTable + " WHERE ";
				for (FF ff : this.FFs)
				{
					if (!ff.IsPK)
					{
						continue;
					}
					if (ff.DataType == DataType.AppString)
					{
						DeleteSQL += ff.ToField + "='"
								+ ToDR.getValue(ff.ToField).toString()
								+ "' AND ";
						/*
						 * warning
						 * DeleteSQL+=ff.ToField+"='"+ToDR[ff.ToField].toString
						 * ()+"' AND ";
						 */
					} else
					{
						DeleteSQL += ff.ToField + "="
								+ ToDR.getValue(ff.ToField).toString()
								+ " AND ";
					}
				}
				DeleteSQL = DeleteSQL.substring(0, DeleteSQL.length() - 5);
				switch (this.ToDBUrl)
				{
					case AppCenterDSN:
						DBAccess.RunSQL(DeleteSQL);
						break;
					 
					default:
						break;
				}
				continue;
			} else if (result > 1)
			{
				throw new RuntimeException("目的数据异常错误＋表名；关键字" + this.ToTable
						+ "关键字" + ToDR.getValue(0).toString());
			}
		}
		
		if (this.UPDATEsql != null)
		{
			switch (this.ToDBUrl)
			{
				case AppCenterDSN:
					DBAccess.RunSQL(UPDATEsql);
					break;
			 
				default:
					break;
			}
		}
		this.DoAfter();
	}
}