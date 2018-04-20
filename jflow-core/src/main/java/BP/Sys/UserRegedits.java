package BP.Sys;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 用户注册表s
 */
public class UserRegedits extends EntitiesMyPK
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	public UserRegedits()
	{
	}
	
	/**
	 * @param emp
	 * @throws Exception 
	 */
	public UserRegedits(String emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(UserRegeditAttr.FK_Emp, emp);
		qo.DoQuery();
	}
	
	// 重写
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new UserRegedit();
	}
}