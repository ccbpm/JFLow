package BP.Sys;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 用户日志s
 */
public class UserLogs extends EntitiesMyPK
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	public UserLogs()
	{
	}
	
	/**
	 * @param emp
	 * @throws Exception 
	 */
	public UserLogs(String emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(UserLogAttr.FK_Emp, emp);
		qo.DoQuery();
	}
	
	// 重写
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new UserLog();
	}
}