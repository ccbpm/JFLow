package BP.En;

import BP.DA.DBAccess;

/**
 * MID实体,只有一个实体这个实体只有一个主键属性。
 */
public abstract class EntityMID extends Entity
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public int Save()
	{
		
		if (this.Update() == 0)
		{
			try
			{
				this.setMID(BP.DA.DBAccess.GenerOID());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			this.Insert();
			this.Retrieve();
		}
		return this.getMID();
	}
	
	// public override int Save()
	// {
	//
	// if (this.IsExits)
	// return this.Update();
	// else
	// {
	// this.Insert();
	// return 1;
	// }
	//
	// // if (this.Update()==0)
	// // this.Insert();
	// //
	// // return base.Save ();
	// }
	
	/**
	 * 是否存在
	 * 
	 * @return
	 */
	public final boolean IsExitCheckByPKs()
	{
		return false;
	}
	
	// 属性
	/**
	 * MID, 如果是空的就返回 0 .
	 */
	public final int getMID()
	{
		try
		{
			return this.GetValIntByKey(EntityMIDAttr.MID);
		} catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	
	public final void setMID(int value)
	{
		this.SetValByKey(EntityMIDAttr.MID, value);
	}
	
	// 构造函数
	/**
	 * 构造一个空实例
	 */
	protected EntityMID()
	{
	}
	
	/**
	 * 根据MID构造实体
	 * 
	 * @param MID
	 *            MID
	 */
	protected EntityMID(int mid)
	{
		this.SetValByKey(EntityMIDAttr.MID, getMID());
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EntityMIDAttr.MID, mid);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("没有查询到MID=" + mid + "的实例。");
		}
		// this.Retrieve();
	}
	
	// override 方法
	
	@Override
	public int Retrieve()
	{
		if (this.getMID() == 0)
		{
			return super.Retrieve();
		} else
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere("MID", this.getMID());
			if (qo.DoQuery() == 0)
			{
				throw new RuntimeException("没有此记录:MID=" + this.getMID());
			} else
			{
				return 1;
			}
		}
	}
	
	/**
	 * 删除之前的操作。
	 * 
	 * @return
	 */
	@Override
	protected boolean beforeDelete()
	{
		if (!super.beforeDelete())
		{
			return false;
		}
		
		try
		{
			if (this.getMID() < 0)
			{
				throw new RuntimeException("@实体[" + this.getEnDesc()
						+ "]没有被实例化，不能Delete().");
			}
			return true;
			
		} catch (RuntimeException ex)
		{
			throw new RuntimeException("@[" + this.getEnDesc()
					+ "].beforeDelete err:" + ex.getMessage());
		}
	}
	
	@Override
	public int DirectInsert()
	{
		this.setMID(DBAccess.GenerOID());
		// EnDA.Insert(this);
		try
		{
			return this.RunSQL(SqlBuilder.Insert(this));
		} catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		
	}
}