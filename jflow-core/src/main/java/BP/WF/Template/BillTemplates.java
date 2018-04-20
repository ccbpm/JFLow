package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.WF.Node;

/** 
 单据模板s
*/
public class BillTemplates extends EntitiesNoName
{

		
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BillTemplate();
	}
	/** 
	 单据模板
	*/
	public BillTemplates()
	{
	}
	/** 
	 按节点查询
	 @param nd
	 * @throws Exception 
	*/
	public BillTemplates(Node nd) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BillTemplateAttr.NodeID, nd.getNodeID());
		if (nd.getIsStartNode())
		{
			qo.addOr();
			qo.AddWhere("No", "SLHZ");
		}
		qo.DoQuery();
	}
	/** 
	 按流程查询
	 @param fk_flow 流程编号
	 * @throws Exception 
	*/
	public BillTemplates(String fk_flow) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(BillTemplateAttr.NodeID, "SELECT NodeID FROM WF_Node WHERE fk_flow='" + fk_flow + "'");
		qo.DoQuery();
	}
	/** 
	 按节点查询
	 @param fk_node 节点ID
	 * @throws Exception 
	*/
	public BillTemplates(int fk_node) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BillTemplateAttr.NodeID, fk_node);
		qo.DoQuery();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<BillTemplate> ToJavaList()
	{
		return (java.util.List<BillTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<BillTemplate> Tolist()
	{
		java.util.ArrayList<BillTemplate> list = new java.util.ArrayList<BillTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BillTemplate)this.get(i));
		}
		return list;
	}
}