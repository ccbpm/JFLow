package BP.WF.Template;

import BP.En.EntitiesOID;
import BP.En.Entity;
 


/** 
 节点集合
 
*/
public class NodeSimples extends EntitiesOID
{
 
  
	/** 
	 节点集合.
	 
	 @param FlowNo
	 * @throws Exception 
	*/
	public NodeSimples(String fk_flow) throws Exception
	{
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
	}
	 
	/** 
	 节点集合
	 
	*/
	public NodeSimples()
	{
	}

	 
	public final java.util.List<NodeSimple> ToJavaList()
	{
		return (java.util.List<NodeSimple>)(Object)this;
	}
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeSimple();
	}
	
   
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}