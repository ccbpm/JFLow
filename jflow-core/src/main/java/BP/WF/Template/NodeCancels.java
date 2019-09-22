package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 可撤销的节点
*/
public class NodeCancels extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造与属性.
	/** 
	 他的撤销到
	*/
	public final Nodes getHisNodes()
	{
		Nodes ens = new Nodes();
		for (NodeCancel ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getCancelTo()));
		}
		return ens;
	}
	/** 
	 可撤销的节点
	*/
	public NodeCancels()
	{
	}
	/** 
	 可撤销的节点
	 
	 @param NodeID 节点ID
	*/
	public NodeCancels(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeCancelAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 可撤销的节点
	 
	 @param NodeNo NodeNo 
	*/
	public NodeCancels(String NodeNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeCancelAttr.CancelTo, NodeNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeCancel();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造与属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共方法.
	/** 
	 可撤销的节点s
	 
	 @param sts 可撤销的节点
	 <Cancels></Cancels>
	*/
	public final Nodes GetHisNodes(Nodes sts)
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Node st : sts)
		{
			tmp = this.GetHisNodes(st.getNo());
			for (Node nd : tmp)
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
	 可撤销的节点
	 
	 @param NodeNo 撤销到编号
	 <Cancels>节点s</Cancels>
	*/
	public final Nodes GetHisNodes(String NodeNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeCancelAttr.CancelTo, NodeNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (NodeCancel en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的Nodes
	 
	 @param nodeID 此节点的ID
	 <Cancels>转向此节点的集合的Nodes (FromNodes)</Cancels> 
	*/
	public final Nodes GetHisNodes(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeCancelAttr.FK_Node, nodeID);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (NodeCancel en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getCancelTo()));
		}
		return ens;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公共方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeCancel> ToJavaList()
	{
		return (List<NodeCancel>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeCancel> Tolist()
	{
		ArrayList<NodeCancel> list = new ArrayList<NodeCancel>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeCancel)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}