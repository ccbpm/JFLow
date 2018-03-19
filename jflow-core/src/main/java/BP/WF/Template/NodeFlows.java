package BP.WF.Template;

import java.util.ArrayList;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Port.Emp;
import BP.WF.Port.Emps;

/**
 * 节点调用子流程
 */
public class NodeFlows extends EntitiesMM
{
	/**
	 * 他的调用子流程
	 */
	public final Emps getHisEmps()
	{
		Emps ens = new Emps();
		for (NodeFlow ns : convertNodeFlows(this))
		{
			ens.AddEntity(new Emp(ns.getFK_Flow()));
		}
		return ens;
	}
	
	/**
	 * 他的工作节点
	 */
	public final Nodes getHisNodes()
	{
		Nodes ens = new Nodes();
		for (NodeFlow ns : convertNodeFlows(this))
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;
		
	}
	
	/**
	 * 节点调用子流程
	 */
	public NodeFlows()
	{
	}
	
	/**
	 * 节点调用子流程
	 * 
	 * @param NodeID
	 *            节点ID
	 */
	public NodeFlows(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	
	/**
	 * 节点调用子流程
	 * 
	 * @param EmpNo
	 *            EmpNo
	 */
	public NodeFlows(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Flow, EmpNo);
		qo.DoQuery();
	}
	
	/**
	 * 得调用它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeFlow();
	}
	
	/**
	 * 取调用一个调用子流程集合能够访问调用的节点s
	 * 
	 * @param sts
	 *            调用子流程集合
	 * @return
	 */
	public final Nodes GetHisNodes(Emps sts)
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Emp st : sts.ToJavaList())
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
	 * 调用子流程对应的节点
	 * 
	 * @param EmpNo
	 *            调用子流程编号
	 * @return 节点s
	 */
	public final Nodes GetHisNodes(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Flow, EmpNo);
		qo.DoQuery();
		
		Nodes ens = new Nodes();
		for (NodeFlow en : convertNodeFlows(this))
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	
	/**
	 * 转向此节点的集合的 Nodes
	 * 
	 * @param nodeID
	 *            此节点的ID
	 * @return 转向此节点的集合的Nodes (FromNodes)
	 */
	public final Emps GetHisEmps(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Node, nodeID);
		qo.DoQuery();
		
		Emps ens = new Emps();
		for (NodeFlow en : convertNodeFlows(this))
		{
			ens.AddEntity(new Emp(en.getFK_Flow()));
		}
		return ens;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<NodeFlow> convertNodeFlows(Object obj)
	{
		return (ArrayList<NodeFlow>) obj;
	}
}