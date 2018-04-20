package BP.En;

import BP.Tools.StringHelper;

/**
 * 多个树实体
 */
public abstract class EntityMultiTree extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// /#region 属性
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
		return this.GetValStringByKey(EntityMultiTreeAttr.No);
	}
	
	public final void setNo(String value)
	{
		this.SetValByKey(EntityMultiTreeAttr.No, value);
	}
	
	/**
	 * 树结构编号
	 */
	public final String getTreeNo()
	{
		return this.GetValStringByKey(EntityMultiTreeAttr.TreeNo);
	}
	
	public final void setTreeNo(String value)
	{
		this.SetValByKey(EntityMultiTreeAttr.TreeNo, value);
	}
	
	/**
	 * 名称
	 */
	public final String getName()
	{
		return this.GetValStringByKey(EntityMultiTreeAttr.Name);
	}
	
	public final void setName(String value)
	{
		this.SetValByKey(EntityMultiTreeAttr.Name, value);
	}
	
	/**
	 * 父节点编号
	 */
	public final String getParentNo()
	{
		return this.GetValStringByKey(EntityMultiTreeAttr.ParentNo);
	}
	
	public final void setParentNo(String value)
	{
		this.SetValByKey(EntityMultiTreeAttr.ParentNo, value);
	}
	
	/**
	 * 是否是目录
	 */
	public final boolean getIsDir()
	{
		return this.GetValBooleanByKey(EntityMultiTreeAttr.IsDir);
	}
	
	public final void setIsDir(boolean value)
	{
		this.SetValByKey(EntityMultiTreeAttr.IsDir, value);
	}
	
	/**
	 * 顺序号
	 */
	public final int getIdx()
	{
		return this.GetValIntByKey(EntityMultiTreeAttr.Idx);
	}
	
	public final void setIdx(int value)
	{
		this.SetValByKey(EntityMultiTreeAttr.Idx, value);
	}
	
	/**
	 * 级别
	 */
	public final int getGrade()
	{
		return this.getTreeNo().length() / 2;
	}
	
	// 需要重写.
	/**
	 * 关联的主题字段. 比如在流程表单树中, 就是流程编号字段，需要实体类重写.
	 */
	public abstract String getRefObjField();
	
	// 需要重写.
	
	// 构造函数
	/**
	 * 主键
	 */
	@Override
	public String getPK()
	{
		return EntityMultiTreeAttr.No;
	}
	
	/**
	 * 树结构编号
	 */
	public EntityMultiTree()
	{
	}
	
	/**
	 * 树结构编号
	 * 
	 * @param no
	 *            编号
	 * @throws Exception 
	 */
	public EntityMultiTree(String no) throws Exception
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
	
	
	// /#endregion
	
	
	// /#region 业务逻辑处理
	/**
	 * 重新设置treeNo
	 */
	public final void ResetTreeNo()
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
	public final EntityMultiTree DoCreateSameLevelNode() throws Exception
	{
		Entity tempVar = this.CreateInstance();
		EntityMultiTree en = (EntityMultiTree) ((tempVar instanceof EntityMultiTree) ? tempVar
				: null);
		try
		{
			en.setNo((new Long(BP.DA.DBAccess.GenerOID(this.toString())))
					.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		} // en.GenerNewNoByKey(EntityMultiTreeAttr.No);
		en.setName("新建节点" + en.getNo());
		en.setParentNo(this.getParentNo());
		en.setIsDir(false);
		try
		{
			en.setTreeNo(this.GenerNewNoByKey(EntityMultiTreeAttr.TreeNo,
					EntityMultiTreeAttr.ParentNo, this.getParentNo()));
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// 给实体类赋值.
		en.SetValByKey(this.getRefObjField(),
				this.GetValStringByKey(this.getRefObjField()));
		
		en.Insert();
		return en;
	}
	
	/**
	 * 新建子节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final EntityMultiTree DoCreateSubNode() throws Exception
	{
		Entity tempVar = this.CreateInstance();
		EntityMultiTree en = (EntityMultiTree) ((tempVar instanceof EntityMultiTree) ? tempVar
				: null);
		try
		{
			en.setNo((new Long(BP.DA.DBAccess.GenerOID(this.toString())))
					.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		} // en.GenerNewNoByKey(EntityMultiTreeAttr.No);
		en.setName("新建节点" + en.getNo());
		en.setParentNo(this.getNo());
		en.setIsDir(false);
		
		// 给实体类赋值.
		en.SetValByKey(this.getRefObjField(),
				this.GetValStringByKey(this.getRefObjField()));
		
		try
		{
			en.setTreeNo(this.GenerNewNoByKey(EntityMultiTreeAttr.TreeNo,
					EntityMultiTreeAttr.ParentNo, this.getNo()));
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (en.getTreeNo().substring(en.getTreeNo().length() - 2).equals("01"))
		{
			en.setTreeNo(this.getTreeNo() + "01");
		}
		en.Insert();
		
		// 设置此节点是目录
		if (!this.getIsDir())
		{
			this.setIsDir(true);
			this.Update(EntityMultiTreeAttr.IsDir, true);
		}
		return en;
	}
	
	/**
	 * 上移
	 * 
	 * @return
	 */
	public final String DoUp()
	{
		try
		{
			this.DoOrderUp(EntityMultiTreeAttr.ParentNo, this.getParentNo(),
					this.getRefObjField(),
					this.GetValStringByKey(getRefObjField()),
					EntityMultiTreeAttr.Idx);
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
	public final String DoDown()
	{
		try
		{
			this.DoOrderDown(EntityMultiTreeAttr.ParentNo, this.getParentNo(),
					this.getRefObjField(),
					this.GetValStringByKey(getRefObjField()),
					EntityMultiTreeAttr.Idx);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}