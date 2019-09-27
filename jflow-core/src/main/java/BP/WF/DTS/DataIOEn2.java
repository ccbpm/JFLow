package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.DA.DBUrlType;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DTS.DoType;
import BP.DTS.FF;
import BP.DTS.RunTimeType;

public abstract class DataIOEn2
{

	/** 
	 获取在 DTS 中的编号。
	 
	 @return 
	*/
	public final String GetNoInDTS()
	{

		return null;
	}

	/** 
	 选择sql .
	 
	*/
	public String SELECTSQL=null;
	/** 
	 数据同步类型．
	 
	*/
	public DoType HisDoType = DoType.UnName;
	/** 
	 运行类型时间
	 
	*/
	public RunTimeType HisRunTimeType = RunTimeType.UnName;
	/** 
	 标题
	 
	*/
	public String Title="未命名数据同步";
	/** 
	 WHERE .
	 
	*/
	public String FromWhere=null;
	/** 
	 FFs
	 
	*/
	public BP.DTS.FFs FFs=null;
	/** 
	 从Table .
	 
	*/
	public String FromTable=null;
	/** 
	 到Table.
	 
	*/
	public String ToTable=null;
	/** 
	 从DBUrl.
	 
	*/
	public DBUrlType FromDBUrl;
	/** 
	 到DBUrl.
	 
	*/
	public DBUrlType ToDBUrl;
	/** 
	 更新语句
	 
	*/
	public String UPDATEsql;
	/** 
	 备注
	 
	*/
	public String Note="无";

	public String DefaultEveryMonth="99";
	public String DefaultEveryDay="99";
	public String DefaultEveryHH="99";
	public String DefaultEveryMin="99";
	/** 
	 类别
	 
	*/
	public String FK_Sort="0";


	/** 
	 调度
	 
	*/
	public DataIOEn2()
	{
	}

	public final int ToDBUrlRunSQL(String sql)
	{
		return DBAccess.RunSQL(sql);

	}
	
	/** 
	 是否存在?
	 
	 @param sql 要判断的sql
	 @return 
	*/
	public final boolean ToDBUrlIsExit(String sql)
	{
		return DBAccess.IsExits(sql);

	}


	/** 
	 执行，用于子类的重写。
	 
	*/
	public void Do()
	{
		if (this.HisDoType==DoType.UnName)
		{
			throw new RuntimeException("@没有说明同步的类型,请在基础信息里面设置同步的类型(构造函数．)．");
		}

		if (this.HisDoType==DoType.DeleteInsert)
		{
			this.DeleteInsert();
		}

		if (this.HisDoType==DoType.Inphase)
		{
			this.Inphase();
		}

		if (this.HisDoType==DoType.Incremental)
		{
			this.Incremental();
		}
	}

	/** 
	 增量调度：
	 比如： 纳税人的纳税信息。
	 特点：1， 数据与时间成增量的增加。
		   2， 月份以前的数据不变化。
	 
	*/
	public final void Incremental()
	{
//            
//			 * 实现步骤：
//			 * 1，组成sql.
//			 * 2，执行更新。
//			 *  
//			 * 
		this.DoBefore(); // 调用，更新前的业务逻辑处理。

			///#region  得到要更新的数据源。
		DataTable FromDataTable= this.GetFromDataTable();

			///#region 开始执行更新。
		String isExitSql="";
		String InsertSQL="";
		//遍历 数据源表.
		for(DataRow FromDR : FromDataTable.Rows)
		{
				///#region 判断是否存在．
			// 判断是否存在，如果存在continue. 不存在就 insert.  
			isExitSql="SELECT * FROM "+this.ToTable+" WHERE ";
			for(FF ff : this.FFs)
			{
				if (ff.IsPK==false)
				{
					continue;
				}
				isExitSql+= ff.ToField +"='"+FromDR.getValue(ff.FromField)+ "' AND ";
			}

			isExitSql=isExitSql.substring(0,isExitSql.length()-5);

			if (DBAccess.IsExits(isExitSql)) //如果不存在就 insert.
			{
				continue;
			}

			InsertSQL="INSERT INTO "+this.ToTable +"(";
			for(FF ff : this.FFs)
			{
				InsertSQL+=ff.ToField.toString()+",";
			}
			InsertSQL=InsertSQL.substring(0,InsertSQL.length()-1);
			InsertSQL+=") values(";
			for(FF ff : this.FFs)
			{
				if(ff.DataType==DataType.AppString||ff.DataType==DataType.AppDateTime)
				{
					InsertSQL+="'"+FromDR.getValue(ff.FromField).toString()+"',";
				}
				else
				{
					InsertSQL+=FromDR.getValue(ff.FromField).toString()+",";
				}
			}
			InsertSQL=InsertSQL.substring(0,InsertSQL.length()-1);
			InsertSQL+=")";
			DBAccess.RunSQL(InsertSQL);


		}

		this.DoAfter(); // 调用，更新之后的业务处理。
	}
	/** 
	 增量调度以前要执行的方法。
	 
	*/
	protected void DoBefore()
	{
	}
	/** 
	 增量调度之后要执行的方法。
	 
	*/
	protected void DoAfter()
	{
	}

	/** 
	 删除之后插入, 用于数据量不太大,更新频率不太频繁的数据处理.
	 
	*/
	public final void DeleteInsert()
	{
		this.DoBefore(); //调用业务处理。
		// 得到源表.
		DataTable FromDataTable= this.GetFromDataTable();
		this.DeleteObjData();

			///#region  遍历源表 插入操作
		String InsertSQL="";
		for(DataRow FromDR : FromDataTable.Rows)
		{

			InsertSQL="INSERT INTO "+this.ToTable +"(";
			for(FF ff : this.FFs)
			{
				InsertSQL+=ff.ToField.toString()+",";
			}
			InsertSQL=InsertSQL.substring(0,InsertSQL.length()-1);
			InsertSQL+=") values(";
			for(FF ff : this.FFs)
			{
				if(ff.DataType==DataType.AppString||ff.DataType==DataType.AppDateTime)
				{
					InsertSQL+="'"+FromDR.getValue(ff.FromField).toString()+"',";
				}
				else
				{
					InsertSQL+=FromDR.getValue(ff.FromField).toString()+",";
				}
			}
			InsertSQL=InsertSQL.substring(0,InsertSQL.length()-1);
			InsertSQL+=")";
			DBAccess.RunSQL(InsertSQL);

		}
		this.DoAfter(); // 调用业务处理。

	}
	public final void DeleteObjData()
	{
		switch(this.ToDBUrl)
		{
			case AppCenterDSN:
				DBAccess.RunSQL("DELETE FROM  " + this.ToTable);
				break;
		
			default:
				break;
		}
	}


	/** 
	 得到数据源。
	 
	 @return 
	*/
	public final DataTable GetToDataTable()
	{
		String sql="SELECT * FROM "+this.ToTable;
		DataTable FromDataTable = new DataTable();
		FromDataTable = DBAccess.RunSQLReturnTable(sql);

		return FromDataTable;

	}
	/** 
	 得到数据源。
	 
	 @return 数据源 
	*/
	public final DataTable GetFromDataTable()
	{
		String FromSQL="SELECT ";
		for(FF ff : this.FFs)
		{
			//对日期型的判断
			if(ff.DataType==DataType.AppDateTime)
			{
				FromSQL+=" CASE  "+ " when datalength( CONVERT(VARCHAR,datepart(month," + ff.FromField + " )))=1 then datename(year," + ff.FromField + " )+'-'+('0'+CONVERT(NVARCHAR,datepart(month," + ff.FromField + " ))) " + " else "+ " datename(year," + ff.FromField + " )+'-'+CONVERT(VARCHAR,datepart(month," + ff.FromField + " )) " + " END "+ " AS "+ff.FromField+" , ";
			}
			else
			{
				FromSQL+=ff.FromField+",";
			}
		}

		FromSQL=FromSQL.substring(0,FromSQL.length()-1);
		FromSQL+=" from "+ this.FromTable;
		FromSQL+=this.FromWhere;
		DataTable FromDataTable = new DataTable();
		FromDataTable = DBAccess.RunSQLReturnTable(FromSQL);
		return FromDataTable;
	}

	/** 
	 同步更新.
	 
	*/
	public final void Inphase()
	{
			///#region 得到源表
		this.DoBefore();

		String FromSQL="SELECT ";
		for(FF ff : this.FFs)
		{
			//对日期型的判断
			if(ff.DataType==DataType.AppDateTime)
			{
				FromSQL+=" CASE  "+ " when datalength( CONVERT(VARCHAR,datepart(month," + ff.FromField + " )))=1 then datename(year," + ff.FromField + " )+'-'+('0'+CONVERT(NVARCHAR,datepart(month," + ff.FromField + " ))) " + " else "+ " datename(year," + ff.FromField + " )+'-'+CONVERT(VARCHAR,datepart(month," + ff.FromField + " )) " + " END "+ " AS "+ff.FromField+" , ";
			}
			else
			{
				FromSQL+=ff.FromField+",";
			}
		}
		FromSQL=FromSQL.substring(0,FromSQL.length()-1);
		FromSQL+=" from "+ this.FromTable;
		FromSQL+=this.FromWhere;
		DataTable FromDataTable = new DataTable();
		FromDataTable = DBAccess.RunSQLReturnTable(FromSQL);

			///#region 得到目的表(字段只包含主键)
		String ToSQL="SELECT ";
		for(FF ff : this.FFs)
		{
			if (ff.IsPK==false)
			{
				continue;
			}
			ToSQL+=ff.ToField+",";
		}
		ToSQL=ToSQL.substring(0,ToSQL.length()-1);
		ToSQL+=" FROM "+ this.ToTable;
		DataTable ToDataTable = new DataTable();
		ToDataTable = DBAccess.RunSQLReturnTable(ToSQL);

		String SELECTSQL="";
		String InsertSQL="";
		String UpdateSQL="";
		String DeleteSQL="";
		//int i=0;
		//int j=0;
		int result=0;

			///#region  遍历源表
		for(DataRow FromDR : FromDataTable.Rows)
		{
			UpdateSQL="UPDATE  "+this.ToTable+" SET ";
			for(FF ff : this.FFs)
			{
				switch(ff.DataType)
				{
					case DataType.AppDateTime:
					case DataType.AppString:
						UpdateSQL+= ff.ToField+ "='"+FromDR.getValue(ff.FromField).toString()+"',";
						break;
					case DataType.AppFloat:
					case DataType.AppInt:
					case DataType.AppMoney:
					case DataType.AppDate:
					case DataType.AppDouble:
						UpdateSQL+= ff.ToField+ "="+FromDR.getValue(ff.FromField).toString()+",";
						break;
					default:
						throw new RuntimeException("没有涉及到的数据类型.");
				}
			}
			UpdateSQL=UpdateSQL.substring(0,UpdateSQL.length()-1);
			UpdateSQL+=" WHERE ";
			for(FF ff : this.FFs)
			{
				if (ff.IsPK==false)
				{
					continue;
				}
				UpdateSQL+= ff.ToField +"='"+FromDR.getValue(ff.FromField)+ "' AND ";
			}

			UpdateSQL=UpdateSQL.substring(0,UpdateSQL.length()-5);
			result = DBAccess.RunSQL(UpdateSQL);

			if(result==0)
			{
				//插入操作
				InsertSQL="INSERT INTO "+this.ToTable +"(";
				for(FF ff : this.FFs)
				{
					InsertSQL+=ff.ToField.toString()+",";
				}
				InsertSQL=InsertSQL.substring(0,InsertSQL.length()-1);
				InsertSQL+=") values(";
				for(FF ff : this.FFs)
				{
					if(ff.DataType==DataType.AppString||ff.DataType==DataType.AppDateTime)
					{
						InsertSQL+="'"+FromDR.getValue(ff.FromField).toString()+"',";
					}
					else
					{
						InsertSQL+=FromDR.getValue(ff.FromField).toString()+",";
					}
				}
				InsertSQL=InsertSQL.substring(0,InsertSQL.length()-1);
				InsertSQL+=")";
				DBAccess.RunSQL(InsertSQL);

			}

		}

			///#region    遍历目的表 如果该条记录存在,continue,如果该条记录不存在,则根据主键删除目的表的对应数据
		for(DataRow ToDR : ToDataTable.Rows)
		{
			SELECTSQL="SELECT ";
			for(FF ff : this.FFs)
			{
				if (ff.IsPK==false)
				{
					continue;
				}
				SELECTSQL+=ff.FromField+",";
			}
			SELECTSQL=SELECTSQL.substring(0,SELECTSQL.length()-1);
			SELECTSQL+=" FROM "+this.FromTable+" WHERE ";
			for(FF ff : this.FFs)
			{
				if (ff.IsPK==false)
				{
					continue;
				}
				if(ff.DataType==DataType.AppDateTime)
				{
					SELECTSQL+=" case "+ " when datalength( CONVERT(VARCHAR,datepart(month,"+ff.FromField+" )))=1 then datename(year,"+ff.FromField+" )+'-'+('0'+CONVERT(VARCHAR,datepart(month,"+ff.FromField+" ))) "+ " else "+ " datename(year,"+ff.FromField+" )+'-'+CONVERT(VARCHAR,datepart(month,"+ff.FromField+" )) "+ " END "+ "='"+ToDR.getValue(ff.ToField).toString()+"' AND ";
				}
				else
				{
					if(ff.DataType==DataType.AppString)
					{
						SELECTSQL+=ff.FromField+"='"+ToDR.getValue(ff.ToField).toString()+"' AND ";
					}
					else
					{
						SELECTSQL+=ff.FromField+"="+ToDR.getValue(ff.ToField).toString()+" AND ";
					}
				}
			}
			SELECTSQL=SELECTSQL.substring(0,SELECTSQL.length()-5);
			//SELECTSQL+=this.FromWhere;
			result=0;
			result = DBAccess.RunSQLReturnCOUNT(SELECTSQL);

			if(result!=1)
			{
				//delete
				DeleteSQL = "delete FROM  " + this.ToTable + " WHERE ";
				for(FF ff : this.FFs)
				{
					if (ff.IsPK==false)
					{
						continue;
					}
					if(ff.DataType==DataType.AppString)
					{
						DeleteSQL+=ff.ToField+"='"+ToDR.getValue(ff.ToField).toString()+"' AND ";
					}
					else
					{
						DeleteSQL+=ff.ToField+"="+ToDR.getValue(ff.ToField).toString()+" AND ";
					}
				}
				DeleteSQL=DeleteSQL.substring(0,DeleteSQL.length()-5);
				DBAccess.RunSQL(DeleteSQL);
				continue;
			}
			else if(result>1)
			{
				throw new RuntimeException("目的数据异常错误＋表名；关键字"+this.ToTable+"关键字"+ToDR.getValue(0).toString());
			}
		}

		if(this.UPDATEsql!=null)
		{
			switch(this.ToDBUrl)
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
