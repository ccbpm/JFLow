package bp.wf.template;
import bp.en.*;
/** 
 节点集合
*/
public class NodeExts extends Entities
{
	private static final long serialVersionUID = 1L;

	///构造方法
	/** 
	 节点集合
	*/
	public NodeExts()
	{
	}

	public NodeExts(String fk_flow) throws Exception
	{
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		return;
	}

		///

	@Override
	public Entity getGetNewEntity()
	{
		return new NodeExt();
	}
}