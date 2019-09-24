package BP.En;

import BP.DA.*;

/** 
 树实体
*/
public abstract class EntityTree extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final boolean getIsRoot() throws Exception
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
	 唯一标示
	 * @throws Exception 
	*/
	public final String getNo() throws Exception
	{
		return this.GetValStringByKey(EntityTreeAttr.No);
	}
	public final void setNo(String value) throws Exception
	{
		this.SetValByKey(EntityTreeAttr.No, value);
	}
	/** 
	 名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(EntityTreeAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(EntityTreeAttr.Name, value);
	}
	/** 
	 父节点编号
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStringByKey(EntityTreeAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(EntityTreeAttr.ParentNo, value);
	}
	/** 
	 图标
	 * @throws Exception 
	*/
	public final String getICON() throws Exception
	{
		return this.GetValStringByKey(EntityTreeAttr.ICON);
	}
	public final void setICON(String value) throws Exception
	{
		this.SetValByKey(EntityTreeAttr.ICON, value);
	}
	/** 
	 顺序号
	 * @throws Exception 
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(EntityTreeAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(EntityTreeAttr.Idx, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return EntityTreeAttr.No;
	}
	/** 
	 树结构编号
	*/
	public EntityTree()
	{
	}
	/** 
	 树结构编号
	 
	 @param no 编号
	 * @throws Exception 
	*/
	public EntityTree(String no) throws Exception
	{
		if (DataType.IsNullOrEmpty(no))
		{
			throw new RuntimeException(this.getEnDesc() + "@对表[" + this.getEnDesc() + "]进行查询前必须指定编号。");
		}

		this.setNo(no);
		if (this.Retrieve() == 0)
		{
			throw new RuntimeException("@没有" + this.get_enMap().getPhysicsTable() + ", No = " + this.getNo() + "的记录。");
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 业务逻辑处理
	/** 
	 重新设置treeNo
	*/
	public final void ResetTreeNo()
	{
	}
	/** 
	 检查名称的问题.
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getEnMap().getIsAllowRepeatName() == false)
		{
			if (this.getPKCount() == 1)
			{
				if (this.ExitsValueNum("Name", this.getName()) >= 1)
				{
					throw new RuntimeException("@插入失败[" + this.getEnMap().getEnDesc() + "] 编号[" + this.getNo() + "]名称[" + getName() + "]重复.");
				}
			}
		}

		if (DataType.IsNullOrEmpty(this.getNo()))
		{
			this.setNo(this.GenerNewNoByKey("No"));
		}
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdate() throws NumberFormatException, Exception
	{
		if (this.getEnMap().getIsAllowRepeatName() == false)
		{
			if (this.getPKCount() == 1)
			{
				if (this.ExitsValueNum("Name", this.getName()) >= 2)
				{
					throw new RuntimeException("@更新失败[" + this.getEnMap().getEnDesc() + "] 编号[" + this.getNo() + "]名称[" + getName() + "]重复.");
				}
			}
		}
		return super.beforeUpdate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 可让子类调用的方法
	/** 
	 新建同级节点
	 
	 @return 
	 * @throws Exception 
	*/
	public EntityTree DoCreateSameLevelNode() throws Exception
	{
		BP.En.Entity tempVar = this.CreateInstance();
		EntityTree en = tempVar instanceof EntityTree ? (EntityTree)tempVar : null;
		en.setNo(String.valueOf(BP.DA.DBAccess.GenerOID(this.toString()))); // en.GenerNewNoByKey(EntityTreeAttr.No);
		en.setName("新建节点" + en.getNo());
		en.setParentNo(this.getParentNo());
	   // en.MenuType = this.MenuType;
	  //  en.IsDir = false;
	   // en.TreeNo = this.GenerNewNoByKey(EntityTreeAttr.TreeNo, EntityTreeAttr.ParentNo, this.ParentNo);
		en.Insert();
		return en;
	}
	/** 
	 新建子节点
	 
	 @return 
	 * @throws Exception 
	*/
	public EntityTree DoCreateSubNode() throws Exception
	{
		BP.En.Entity tempVar = this.CreateInstance();
		EntityTree en = tempVar instanceof EntityTree ? (EntityTree)tempVar : null;
		en.setNo(String.valueOf(BP.DA.DBAccess.GenerOID(this.toString()))); // en.GenerNewNoByKey(EntityTreeAttr.No);
		en.setName("新建节点" + en.getNo());
		en.setParentNo(this.getNo());
	  //  en.MenuType = this.MenuType + 1;
		//en.IsDir = false;
		//en.TreeNo = this.GenerNewNoByKey(EntityTreeAttr.TreeNo, EntityTreeAttr.ParentNo, this.getNo());
		//if (en.TreeNo.Substring(en.TreeNo.Length - 2) == "01")
		//    en.TreeNo = this.TreeNo + "10";
		en.Insert();

		//// 设置此节点是目录
		//if (this.IsDir == false)
		//{
		//    this.Retrieve();
		//    this.IsDir = true;
		//    this.Update();
		//}
		return en;
	}
	/** 
	 上移
	 
	 @return 
	 * @throws Exception 
	*/
	public String DoUp() throws Exception
	{
		this.DoOrderUp(EntityTreeAttr.ParentNo, this.getParentNo(), EntityTreeAttr.Idx);
		return "执行成功.";
	}
	/** 
	 下移
	 
	 @return 
	 * @throws Exception 
	*/
	public String DoDown() throws Exception
	{
		this.DoOrderDown(EntityTreeAttr.ParentNo, this.getParentNo(), EntityTreeAttr.Idx);
		return "执行成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}