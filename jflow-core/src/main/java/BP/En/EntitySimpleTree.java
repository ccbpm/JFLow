package BP.En;

import BP.Tools.StringHelper;

/**
 * 树实体
 */
public abstract class EntitySimpleTree extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 属性
	public final boolean getIsRoot()
	{
		if (this.getParentNo().equals("-1") || this.getParentNo().equals("0"))
		{
			return true;
		}
		
		if (this.getNo().equals(this.getParentNo()))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 唯一标示
	 */
	public final String getNo()
	{
		return this.GetValStringByKey(EntitySimpleTreeAttr.No);
	}
	
	public final void setNo(String value)
	{
		this.SetValByKey(EntitySimpleTreeAttr.No, value);
	}
	
	/**
	 * 名称
	 */
	public final String getName()
	{
		return this.GetValStringByKey(EntitySimpleTreeAttr.Name);
	}
	
	public final void setName(String value)
	{
		this.SetValByKey(EntitySimpleTreeAttr.Name, value);
	}
	
	/**
	 * 父节点编号
	 */
	public String getParentNo()
	{
		return this.GetValStringByKey(EntitySimpleTreeAttr.ParentNo);
	}
	
	public void setParentNo(String value)
	{
		this.SetValByKey(EntitySimpleTreeAttr.ParentNo, value);
	}
	
	// 构造函数
	/**
	 * 主键
	 */
	@Override
	public String getPK()
	{
		return EntitySimpleTreeAttr.No;
	}
	
	/**
	 * 树结构编号
	 */
	public EntitySimpleTree()
	{
	}
	
	/**
	 * 树结构编号
	 * 
	 * @param no
	 *            编号
	 * @throws Exception 
	 */
	public EntitySimpleTree(String no) throws Exception
	{
		if (StringHelper.isNullOrEmpty(no))
		{
			throw new RuntimeException(this.getEnDesc() + "@对表["
					+ this.getEnDesc() + "]进行查询前必须指定编号。");
		}
		
		this.setNo(no);
		if (this.Retrieve() == 0)
		{
			throw new RuntimeException("@没有"
					+ this.get_enMap().getPhysicsTable() + ", No = "
					+ this.getNo() + "的记录。");
		}
	}
}