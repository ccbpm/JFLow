package BP.En;

import BP.Tools.StringHelper;

/**
 * 树实体
 */
public abstract class EntityTree extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 属性
	public boolean getIsRoot()
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
	public String getNo()
	{
		return this.GetValStringByKey(EntityTreeAttr.No);
	}
	
	public void setNo(String value)
	{
		this.SetValByKey(EntityTreeAttr.No, value);
	}
	
	/**
	 * 树结构编号
	 */
	public String getTreeNo()
	{
		return this.GetValStringByKey(EntityTreeAttr.TreeNo);
	}
	
	public void setTreeNo(String value)
	{
		this.SetValByKey(EntityTreeAttr.TreeNo, value);
	}
	
	/**
	 * 名称
	 */
	public String getName()
	{
		return this.GetValStringByKey(EntityTreeAttr.Name);
	}
	
	public void setName(String value)
	{
		this.SetValByKey(EntityTreeAttr.Name, value);
	}
	
	/**
	 * 父节点编号
	 */
	public String getParentNo()
	{
		return this.GetValStringByKey(EntityTreeAttr.ParentNo);
	}
	
	public void setParentNo(String value)
	{
		this.SetValByKey(EntityTreeAttr.ParentNo, value);
	}
	
	/**
	 * 图标
	 */
	public String getICON()
	{
		return this.GetValStringByKey(EntityTreeAttr.ICON);
	}
	
	public void setICON(String value)
	{
		this.SetValByKey(EntityTreeAttr.ICON, value);
	}
	
	/**
	 * 是否是目录
	 */
	public boolean getIsDir()
	{
		return this.GetValBooleanByKey(EntityTreeAttr.IsDir);
	}
	
	public void setIsDir(boolean value)
	{
		this.SetValByKey(EntityTreeAttr.IsDir, value);
	}
	
	/**
	 * 顺序号
	 */
	public int getIdx()
	{
		return this.GetValIntByKey(EntityTreeAttr.Idx);
	}
	
	public void setIdx(int value)
	{
		this.SetValByKey(EntityTreeAttr.Idx, value);
	}
	
	/**
	 * 级别
	 */
	public int getGrade()
	{
		return this.getTreeNo().length() / 2;
	}
	
	// 构造函数
	/**
	 * 主键
	 */
	@Override
	public String getPK()
	{
		return EntityTreeAttr.No;
	}
	
	/**
	 * 树结构编号
	 */
	public EntityTree()
	{
	}
	
	/**
	 * 树结构编号
	 * 
	 * @param no
	 *            编号
	 * @throws Exception 
	 */
	public EntityTree(String no) throws Exception
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
	
	// 业务逻辑处理
	/**
	 * 重新设置treeNo
	 */
	public void ResetTreeNo()
	{
	}
	
	/**
	 * 检查名称的问题.
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	protected boolean beforeInsert() throws Exception
	{
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
				}
			}
		}
		
		if (StringHelper.isNullOrEmpty(this.getNo()))
		{
			try
			{
				this.setNo(this.GenerNewNoByKey("No"));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return super.beforeInsert();
	}
	
	@Override
	protected boolean beforeUpdate() throws Exception
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
				}
			}
		}
		return super.beforeUpdate();
	}
	
	// 可让子类调用的方法
	/**
	 * 新建同级节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	public EntityTree DoCreateSameLevelNode() throws Exception
	{
		EntityTree en = (EntityTree) this.CreateInstance();
		
		en.setNo((new Long(BP.DA.DBAccess.GenerOID(this.toString()))).toString());
		
		en.setName("新建节点" + en.getNo());
		en.setParentNo(this.getParentNo());
		en.Insert();
		return en;
	}
	
	/**
	 * 新建子节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	public EntityTree DoCreateSubNode() throws Exception
	{
		EntityTree en = (EntityTree) this.CreateInstance();
		
		en.setNo((new Long(BP.DA.DBAccess.GenerOID(this.toString()))).toString());
		
		en.setName("新建节点" + en.getNo());
		en.setParentNo(this.getNo());
		
		en.Insert();
		return en;
	}
	
	/**
	 * 上移
	 * 
	 * @return
	 */
	public String DoUp()
	{
		try
		{
			this.DoOrderUp(EntityTreeAttr.ParentNo, this.getParentNo(),
					EntityTreeAttr.Idx);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 下移
	 * 
	 * @return
	 */
	public String DoDown()
	{
		try
		{
			this.DoOrderDown(EntityTreeAttr.ParentNo, this.getParentNo(),
					EntityTreeAttr.Idx);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}