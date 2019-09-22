package BP.En;

import BP.DA.*;

/** 
 NoEntity 的摘要说明。
*/
public abstract class EntityMyPK extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	@Override
	public String getPK()
	{
		return "MyPK";
	}
	/** 
	 集合类名称
	*/
	public final String getMyPK()
	{
		return this.GetValStringByKey(EntityMyPKAttr.MyPK);
	}
	public final void setMyPK(String value)
	{
		this.SetValByKey(EntityMyPKAttr.MyPK, value);
	}
	/** 
	 
	 
	 @return 
	*/
	public String InitMyPKVals()
	{
	   return this.getMyPK();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	public EntityMyPK()
	{

	}
	/** 
	 class Name 
	 
	 @param _MyPK _MyPK
	*/
	protected EntityMyPK(String _MyPK)
	{
		this.setMyPK(_MyPK);
		this.Retrieve();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}