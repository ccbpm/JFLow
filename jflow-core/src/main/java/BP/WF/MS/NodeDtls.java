package BP.WF.MS;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 制度章节
*/
public class NodeDtls extends EntitiesMM
{
	/** 
	 制度章节
	*/
	public NodeDtls()
	{
	}
	/** 
	 制度章节
	 
	 @param NodeID 节点ID
	 * @throws Exception 
	*/
	public NodeDtls(int NodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeDtlAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 得调用它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeDtl();
	}
}