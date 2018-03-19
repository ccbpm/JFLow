package BP.En;

/**
 * 具有编号名称的基类实体
 */
public abstract class EntityNoName extends EntityNo
{
	// 属性
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 名称
	 */
	public String getName()
	{
		return this.GetValStringByKey(EntityNoNameAttr.Name);
	}
	
	public final void setName(String value)
	{
		this.SetValByKey(EntityNoNameAttr.Name, value);
	}
	
	// public string NameE
	// {
	// get
	// {
	// return this.GetValStringByKey("NameE");
	// }
	// set
	// {
	// this.SetValByKey("NameE", value);
	// }
	// }
	
	// /#endregion
	
	// /#region 构造函数
	/** 
	 
	 
	*/
	public EntityNoName()
	{
	}
	
	/**
	 * @param _No
	 */
	protected EntityNoName(String _No)
	{
		super(_No);
	}
	
	// /#endregion
	
	// /#region 业务逻辑处理
	/**
	 * 检查名称的问题.
	 * 
	 * @return
	 */
	@Override
	protected boolean beforeInsert()
	{
		if (this.getNo().trim().length() == 0)
		{
			if (this.getEnMap().getIsAutoGenerNo())
			{
				this.setNo(this.getGenerNewNo());
			} else
			{
				throw new RuntimeException("@没有给[" + this.getEnDesc() + " , "
						+ this.getName() + "]设置主键.");
			}
		}
		
		if (!this.getEnMap().getIsAllowRepeatName())
		{
			if (this.getPKCount() == 1)
			{
				try
				{
					if (this.ExitsValueNum("Name", this.getName()) >= 1)
					{
						throw new RuntimeException("@插入失败["
								+ this.getEnMap().getEnDesc() + "] 编号["
								+ this.getNo() + "]名称[" + getName() + "]重复.");
					}
				} catch (NumberFormatException e)
				{
					
					e.printStackTrace();
				} catch (Exception e)
				{
					
					e.printStackTrace();
					return false;
				}
			}
		}
		return super.beforeInsert();
	}
	
	@Override
	protected boolean beforeUpdate()
	{
		if (!this.getEnMap().getIsAllowRepeatName())
		{
			if (this.getPKCount() == 1)
			{
				try
				{
					if (this.ExitsValueNum("Name", this.getName()) >= 2)
					{
						throw new RuntimeException("@更新失败["
								+ this.getEnMap().getEnDesc() + "] 编号["
								+ this.getNo() + "]名称[" + getName() + "]重复.");
					}
				} catch (NumberFormatException e)
				{
					
					e.printStackTrace();
				} catch (Exception e)
				{
					
					e.printStackTrace();
					return false;
				}
			}
		}
		return super.beforeUpdate();
	}
}