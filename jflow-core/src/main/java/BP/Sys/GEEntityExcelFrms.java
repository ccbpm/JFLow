package BP.Sys;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 默认值s
 */
public class GEEntityExcelFrms extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 查询.
	 * 
	 * @param EnsName
	 * @param key
	 * @param FK_Emp
	 * @throws Exception 
	 */
	public final void Retrieve(String EnsName, String key, int FK_Emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DefValAttr.AttrKey, key);
		qo.addAnd();
	//	qo.AddWhere(DefValAttr.EnsName, EnsName);
		qo.addAnd();
		qo.AddWhere(DefValAttr.FK_Emp, FK_Emp);
		qo.DoQuery();
	}
	
	/**
	 * 查询
	 * 
	 * @param EnsName
	 * @param key
	 * @throws Exception 
	 */
	public final void Retrieve(String EnsName, String key) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DefValAttr.AttrKey, key);
		qo.addAnd();
		//qo.AddWhere(DefValAttr.EnsName, EnsName);
		qo.DoQuery();
	}
	
	/**
	 * 默认值s
	 */
	public GEEntityExcelFrms()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new DefVal();
	}
}