package BP.En;

import BP.DA.*;

/** 
 NoEntity 的摘要说明。
*/
public abstract class EntityMyPK extends Entity
{

		///#region 基本属性
	@Override
	public String getPK()
	{
		return "MyPK";
	}
	/** 
	 集合类名称
	 * @throws Exception 
	*/
	public final String getMyPK() throws Exception
	{
		return this.GetValStringByKey(EntityMyPKAttr.MyPK);
	}
	public final void setMyPK(String value) throws Exception
	{
		this.SetValByKey(EntityMyPKAttr.MyPK, value);
	}
	/** 
	 
	 
	 @return 
	 * @throws Exception 
	*/
	public String InitMyPKVals() throws Exception
	{
	   return this.getMyPK();
	}

		///#endregion


		///#region 构造
	public EntityMyPK()
	{

	}
	/** 
	 class Name 
	 
	 @param _MyPK _MyPK
	 * @throws Exception 
	*/
	protected EntityMyPK(String _MyPK) throws Exception
	{
		this.setMyPK(_MyPK);
		this.Retrieve();
	}

		///#endregion
}