package BP.WF.Template;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.WF.Node;
import BP.WF.Nodes;

/** 
 属性
 
*/
public class FlowNodes extends EntitiesMM
{
	/** 
	 他的工作节点
	 * @throws Exception 
	 
	*/
	public final Nodes getHisNodes() throws Exception
	{
		Nodes ens = new Nodes();
		for (FlowNode ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;
	}
	/** 
	 流程抄送节点
	 
	*/
	public FlowNodes()
	{
	}
	/** 
	 流程抄送节点
	 
	 @param NodeID 节点ID
	 * @throws Exception 
	*/
	public FlowNodes(int NodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowNodeAttr.FK_Flow, NodeID);
		qo.DoQuery();
	}
	/** 
	 流程抄送节点
	 
	 @param NodeNo NodeNo 
	 * @throws Exception 
	*/
	public FlowNodes(String NodeNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowNodeAttr.FK_Node, NodeNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowNode();
	}
	/** 
	 流程抄送节点s
	 
	 @param sts 流程抄送节点
	 @return 
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(Nodes sts) throws Exception
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Node st : sts.ToJavaList())
		{
			tmp = this.GetHisNodes(st.getNo());
			for (Node nd : tmp.ToJavaList())
			{
				if (nds.Contains(nd))
				{
					continue;
				}
				nds.AddEntity(nd);
			}
		}
		return nds;
	}
	/** 
	 流程抄送节点
	 
	 @param NodeNo 工作节点编号
	 @return 节点s
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(String NodeNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowNodeAttr.FK_Node, NodeNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (FlowNode en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Flow()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowNodeAttr.FK_Flow, nodeID);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (FlowNode en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FlowNode> ToJavaList()
	{
		return (java.util.List<FlowNode>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FlowNode> Tolist()
	{
		java.util.ArrayList<FlowNode> list = new java.util.ArrayList<FlowNode>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowNode)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}