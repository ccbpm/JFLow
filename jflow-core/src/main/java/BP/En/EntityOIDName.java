package BP.En;

import BP.DA.*;

/** 
 用于 OID Name 属性的实体继承。	
*/
public abstract class EntityOIDName extends EntityOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 主键值
	*/
	@Override
	public String getPK()
	{
		return "OID";
	}
	@Override
	public String getPKField()
	{
		return "OID";
	}
	/** 
	 构造
	*/
	protected EntityOIDName()
	{
	}
	/** 
	 构造
	 
	 @param oid OID
	*/
	protected EntityOIDName(int oid)
	{
		super(oid);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性方法
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey(EntityOIDNameAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(EntityOIDNameAttr.Name, value);
	}
	/** 
	 按照名称查询。
	 
	 @return 返回查询出来的个数
	*/
	public final int RetrieveByName()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere("Name", this.getName());
		return qo.DoQuery();
	}
	protected final int LoadDir(String dir)
	{


		return 1;
	}

	@Override
	protected boolean beforeUpdate()
	{
		if (this.getEnMap().getIsAllowRepeatName() == false)
		{
			if (this.getPKCount() == 1)
			{
				if (this.ExitsValueNum("Name", this.getName()) >= 2)
				{
					throw new RuntimeException("@更新失败[" + this.getEnMap().getEnDesc() + "] OID=[" + this.getOID() + "]名称[" + getName() + "]重复.");
				}
			}
		}
		return super.beforeUpdate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}