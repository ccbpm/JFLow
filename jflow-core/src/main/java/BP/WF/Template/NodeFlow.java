package BP.WF.Template;

import java.util.ArrayList;

import BP.DA.Depositary;
import BP.En.EntityMM;
import BP.En.Map;
import BP.WF.Flows;

/**
 * 节点调用子流程 节点的调用子流程有两部分组成. 记录了从一个节点调用其他的多个节点. 也记录了调用这个节点的其他的节点.
 */
public class NodeFlow extends EntityMM
{
	// 基本属性
	/**
	 * 节点
	 */
	public final String getFK_Node()
	{
		return this.GetValStringByKey(NodeFlowAttr.FK_Node);
	}
	
	public final void setFK_Node(String value)
	{
		this.SetValByKey(NodeFlowAttr.FK_Node, value);
	}
	
	/**
	 * 调用子流程
	 */
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(NodeFlowAttr.FK_Flow);
	}
	
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(NodeFlowAttr.FK_Flow, value);
	}
	
	public final String getFK_FlowT()
	{
		return this.GetValRefTextByKey(NodeFlowAttr.FK_Flow);
	}
	
	// 构造方法
	/**
	 * 节点调用子流程
	 */
	public NodeFlow()
	{
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<NodeFlow> convertNodeFlows(Object obj)
	{
		return (ArrayList<NodeFlow>) obj;
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("WF_NodeFlow");
		map.setEnDesc("节点调用子流程");
		
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		
		map.AddTBIntPK(NodeFlowAttr.FK_Node, 0, "节点", true, true);
		// map.AddDDLEntitiesPK(NodeFlowAttr.FK_Node, null, "节点", new
		// NodeSheets(), true);
		map.AddDDLEntitiesPK(NodeFlowAttr.FK_Flow, null, "子流程", new Flows(),
				true);
		
		// map.AddSearchAttr(NodeFlowAttr.FK_Node);
		map.AddSearchAttr(NodeFlowAttr.FK_Flow);
		this.set_enMap(map);
		return this.get_enMap();
	}
}