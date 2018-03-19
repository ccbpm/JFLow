package BP.Sys;

import BP.En.EntitiesOID;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 文件管理者
 */
public class SysFileManagers extends EntitiesOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 文件管理者
	 */
	public SysFileManagers()
	{
	}
	
	/**
	 * 文件管理者
	 * 
	 * @param EnName
	 * @param refval
	 */
	public SysFileManagers(String EnName, String refval)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysFileManagerAttr.EnName, EnName);
		qo.addAnd();
		qo.AddWhere(SysFileManagerAttr.RefVal, refval);
		qo.DoQuery();
	}
	
	/**
	 * 文件管理者
	 * 
	 * @param EnName
	 */
	public SysFileManagers(String EnName)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysFileManagerAttr.EnName, EnName);
		qo.DoQuery();
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new SysFileManager();
	}
	
	public SysFileManager GetSysFileByAttrFileNo(String key)
	{
		for (Object en : this)
		{
			if (((SysFileManager) en).getAttrFileNo().equals(key))
			{
				return (BP.Sys.SysFileManager) en;
			}
		}
		return null;
	}
}