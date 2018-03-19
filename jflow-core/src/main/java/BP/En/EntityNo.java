package BP.En;

/**
 * NoEntity 的摘要说明。
 */
public abstract class EntityNo extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 提供的属性
	@Override
	public String getPK()
	{
		return "No";
	}
	
	/**
	 * 编号
	 */
	public String getNo()
	{
		return this.GetValStringByKey(EntityNoNameAttr.No);
	}
	
	public void setNo(String value)
	{
		this.SetValByKey(EntityNoNameAttr.No, value);
	}
	
	// 与编号有关的逻辑操作(这个属性只与dict EntityNo, 基类有关系。)
	/**
	 * Insert 之前的操作。
	 * 
	 * @return
	 */
	@Override
	protected boolean beforeInsert()
	{
		
		Attr attr = this.getEnMap().GetAttrByKey("No");
		if (attr.getUIVisible() && attr.getUIIsReadonly()
				&& this.getEnMap().getIsAutoGenerNo()
				&& this.getNo().length() == 0)
		{
			this.setNo(this.getGenerNewNo());
		}
		
		return super.beforeInsert();
		// //if (this.EnMap.IsAutoGenerNo && (this.No == "" || this.No == null
		// || this.No == "自动生成"))
		// //{
		// // this.No = this.GenerNewNo;
		// //}
		// if (!this.EnMap.IsAllowRepeatNo)
		// {
		// string field = attr.Field;
		
		// Paras ps = new Paras();
		// ps.Add("no", No);
		// string sql = "SELECT " + field + " FROM " + this.getEnMap().getPhysicsTable() +
		// " WHERE " + field + "=:no";
		// if (DBAccess.IsExits(sql, ps))
		// throw new Exception("@[" + this.EnMap.EnDesc + " , " +
		// this.getEnMap().getPhysicsTable() + "] 编号[" + No + "]重复。");
		// }
		
		// // 是不是检查编号的长度。
		// if (this.EnMap.IsCheckNoLength)
		// {
		// if (this.No.Length!=this.EnMap.CodeLength )
		// throw new
		// Exception("@ ["+this.EnMap.EnDesc+"]编号["+this.No+"]错误，长度不符合系统要求，必须是["+this.EnMap.CodeLength.ToString()+"]位，而现在有长度是["+this.No.Length.ToString()+"]位。");
		// }
		// return base.beforeInsert();
	}
	
	// 构造涵数
	/**
	 * 事例化一个实体
	 */
	public EntityNo()
	{
	}
	
	/**
	 * 通过编号得到实体。
	 * 
	 * @param _no
	 *            编号
	 */
	public EntityNo(String _no)
	{
	
		if (_no == null || _no.equals(""))
		{
			throw new RuntimeException(this.getEnDesc() + "@对表["
					+ this.getEnDesc() + "]进行查询前必须指定编号。");
		}
		
		this.setNo(_no);
		if (this.Retrieve() == 0)
		{
			throw new RuntimeException("@没有"
					+ this.get_enMap().getPhysicsTable() + ", No = " + getNo()
					+ "的记录。");
		}
	}
	
	@Override
	public int Save()
	{
		// 如果包含编号。
		try
		{
			if (this.getIsExits())
			{
				return this.Update();
			} else
			{
				if (this.getEnMap().getIsAutoGenerNo()
						&& this.getEnMap().GetAttrByKey("No").getUIIsReadonly())
				{
					this.setNo(this.getGenerNewNo());
				}
				
				this.Insert();
				return 0;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		
		// return base.Save();
	}
	
	// 提供的查寻方法
	/**
	 * 生成一个编号
	 */
	public final String getGenerNewNo()
	{
		try
		{
			return this.GenerNewNoByKey("No");
		} catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 按 No 查询。
	 * 
	 * @return
	 */
	public final int RetrieveByNo()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EntityNoAttr.No, this.getNo());
		return qo.DoQuery();
	}
	
	/**
	 * 按 No 查询。
	 * 
	 * @param _No
	 *            No
	 * @return
	 */
	public final int RetrieveByNo(String _No)
	{
		this.setNo(_No);
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EntityNoAttr.No, this.getNo());
		return qo.DoQuery();
	}
}