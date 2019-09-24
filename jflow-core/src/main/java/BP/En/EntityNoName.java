package BP.En;

import BP.DA.*;

/** 
 具有编号名称的基类实体
*/
public abstract class EntityNoName extends EntityNo
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 名称
	 * @throws Exception 
	*/
	public String getName() throws Exception
	{
		return this.GetValStringByKey(EntityNoNameAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(EntityNoNameAttr.Name, value);
	}
	//public string NameE
	//{
	//    get
	//    {
	//        return this.GetValStringByKey("NameE");
	//    }
	//    set
	//    {
	//        this.SetValByKey("NameE", value);
	//    }
	//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 
	*/
	public EntityNoName()
	{
	}
	/** 
	 
	 
	 @param _No
	 * @throws Exception 
	*/
	protected EntityNoName(String _No) throws Exception
	{
		super(_No);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 业务逻辑处理
	/** 
	 检查名称的问题.
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getNo().trim().length() == 0)
		{
			if (this.getEnMap().getIsAutoGenerNo())
			{
				this.setNo(this.getGenerNewNo());
			}
			else
			{
				throw new RuntimeException("@没有给[" + this.getEnDesc() + " " + this.toString() + " , " + this.getName() + "]设置主键,能执行插入.");
			}
		}

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


}