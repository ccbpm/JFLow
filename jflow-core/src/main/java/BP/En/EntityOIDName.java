package BP.En;

import BP.DA.*;

/** 
 用于 OID Name 属性的实体继承。	
*/
public abstract class EntityOIDName extends EntityOID
{

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
	 * @throws Exception 
	*/
	protected EntityOIDName(int oid) throws Exception
	{
		super(oid);
	}

		///#endregion


		///#region 属性方法
	/** 
	 名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(EntityOIDNameAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(EntityOIDNameAttr.Name, value);
	}
	/** 
	 按照名称查询。
	 
	 @return 返回查询出来的个数
	 * @throws Exception 
	*/
	public final int RetrieveByName() throws Exception
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
	protected boolean beforeUpdate() throws NumberFormatException, Exception
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

		///#endregion
}