package BP.En;

import BP.DA.*;

/** 
 OID实体,只有一个实体这个实体只有一个主键属性。
*/
public abstract class EntityOID extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 是否是自动增长列
	*/
	public boolean getIsInnKey()
	{
		return false;
	}
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return "OID";
	}
	/** 
	 OID, 如果是空的就返回 0 . 
	*/
	public final int getOID()
	{
		try
		{
			return this.GetValIntByKey(EntityOIDAttr.OID);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	public final void setOID(int value)
	{
		this.SetValByKey(EntityOIDAttr.OID, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 构造一个空实例
	*/
	protected EntityOID()
	{
	}
	/** 
	 根据OID构造实体
	 
	 @param oid oid
	*/
	protected EntityOID(int oid)
	{
		this.SetValByKey(EntityOIDAttr.OID, oid);
		this.Retrieve();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region override 方法
	@Override
	public int DirectInsert()
	{
		this.setOID(DBAccess.GenerOID());
		return super.DirectInsert();
	}
	public final void InsertAsNew()
	{
		this.setOID(0);
		this.Insert();
	}
	@Override
	public boolean getIsExits()
	{
		if (this.getOID() == 0)
		{
			return false;
		}

		try
		{
				// 生成数据库判断语句。
			String selectSQL = "SELECT " + this.getPKField() + " FROM " + this.getEnMap().getPhysicsTable() + " WHERE OID=" + this.getHisDBVarStr() + "v";
			Paras ens = new Paras();
			ens.Add("v", this.getOID());

				// 从数据库里面查询，判断有没有。
			switch (this.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					return DBAccess.IsExits(selectSQL, ens);
				default:
					throw new RuntimeException("没有设计到。" + this.getEnMap().getEnDBUrl().getDBType());
			}
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}

			/* DEL BY PENG 2008-04-27
			// 生成数据库判断语句。
			string selectSQL="SELECT "+this.PKField + " FROM "+ this.EnMap.PhysicsTable + " WHERE " ;
			switch(this.EnMap.EnDBUrl.DBType )
			{
				case DBType.MSSQL:
					selectSQL +=SqlBuilder.GetKeyConditionOfMS(this);
					break;
				case DBType.Access:
					selectSQL +=SqlBuilder.GetKeyConditionOfOLE(this);
					break;
				case DBType.Oracle:
					selectSQL +=SqlBuilder.GetKeyConditionOfOracle(this);
					break; 
				default:
					throw new Exception("没有设计到。"+this.EnMap.EnDBUrl.DBType);
			}

			// 从数据库里面查询，判断有没有。
			switch(this.EnMap.EnDBUrl.DBUrlType )
			{
				case DBUrlType.AppCenterDSN:
					return DBAccess.IsExits( selectSQL) ;
				case DBUrlType.DBAccessOfMSSQL:
					return DBAccessOfMSSQL.IsExits( selectSQL) ;
				case DBUrlType.DBAccessOfOLE:
					return DBAccessOfOLE.IsExits( selectSQL) ;
				case DBUrlType.DBAccessOfOracle:
					return DBAccessOfOracle.IsExits( selectSQL) ;
				default:
					throw new Exception("没有设计到。"+this.EnMap.EnDBUrl.DBType);				
			}
			*/
	}
	/** 
	 删除之前的操作。
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete()
	{
		if (super.beforeDelete() == false)
		{
			return false;
		}
		try
		{
			if (this.getOID() < 0)
			{
				throw new RuntimeException("@实体[" + this.getEnDesc() + "]没有被实例化，不能Delete().");
			}
			return true;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@[" + this.getEnDesc() + "].beforeDelete err:" + ex.getMessage());
		}
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		return super.beforeUpdateInsertAction();
	}

	/** 
	 beforeInsert 之前的操作。
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		if (this.getOID() == -999)
		{
			return super.beforeInsert();
		}

		if (this.getOID() > 0)
		{
			throw new RuntimeException("@[" + this.getEnDesc() + "], 实体已经被实例化 oid=[" + this.getOID() + "]，不能Insert.");
		}

		if (this.getIsInnKey())
		{
			this.setOID(-1);
		}
		else
		{
			this.setOID(BP.DA.DBAccess.GenerOID());
		}

		return super.beforeInsert();

	}
	/** 
	 beforeUpdate
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdate()
	{
		if (super.beforeUpdate() == false)
		{
			return false;
		}

		/*
		if (this.OID <= 0 )
			throw new Exception("@实体["+this.EnDesc+"]没有被实例化，不能Update().");
			*/
		return true;
	}
	protected String getSerialKey()
	{
		return "OID";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region public 方法
	/** 
	 作为一个新的实体保存。
	*/
	public final void SaveAsNew()
	{
		try
		{
			this.setOID(DBAccess.GenerOIDByKey32(this.getSerialKey()));
			this.RunSQL(SqlBuilder.Insert(this));
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
	}
	/** 
	 按照指定的OID Insert.
	*/
	public final void InsertAsOID(int oid)
	{
		this.SetValByKey("OID", oid);
		try
		{
			this.RunSQL(SqlBuilder.Insert(this));
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
	}
	public final void InsertAsOID(long oid)
	{
		try
		{
			//先设置一个标记值，为的是不让其在[beforeInsert]产生oid.
			this.SetValByKey("OID", -999);

			//调用方法.
			this.beforeInsert();

			//设置主键.
			this.SetValByKey("OID", oid);

			this.RunSQL(SqlBuilder.Insert(this));

			this.afterInsert();
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
	}
	/** 
	 按照指定的OID 保存
	 
	 @param oid
	*/
	public final void SaveAsOID(int oid)
	{
		this.SetValByKey("OID", oid);
		if (this.getIsExits() == false)
		{
			this.InsertAsOID(oid);
		}
		this.Update();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}