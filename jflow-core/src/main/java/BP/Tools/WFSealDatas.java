package BP.Tools;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 用户日志s
 */
public class WFSealDatas extends EntitiesMyPK
{
	// 构造
	public WFSealDatas()
	{
	}
	
	/**
	 * @param emp
	 */
	public WFSealDatas(String workID, String node)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(WFSealDataAttr.OID, workID);
		qo.AddWhere(WFSealDataAttr.FK_Node, node);
		qo.DoQuery();
	}
	
	// 重写
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new WFSealData();
	}
}
