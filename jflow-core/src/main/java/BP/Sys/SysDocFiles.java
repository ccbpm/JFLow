package BP.Sys;

import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 文件管理者
 */
public class SysDocFiles extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SysDocFiles()
	{
	}
	
	public SysDocFiles(String _tableName, String _key) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysDocFileAttr.EnName, _tableName);
		qo.addAnd();
		qo.AddWhere(SysDocFileAttr.RefKey, _key);
		qo.DoQuery();
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new SysDocFile();
	}
}