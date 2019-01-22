package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 节点集合
 
*/
public class NodeExts extends Entities
{

		
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

		///#endregion
	public final java.util.List<NodeExt> ToJavaList()
	{
		return (java.util.List<NodeExt>)(Object)this;
	}
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeExt();
	}
}