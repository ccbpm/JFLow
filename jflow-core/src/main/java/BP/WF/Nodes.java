package BP.WF;

import BP.DA.Cash;
import BP.DA.Depositary;
import BP.En.EntitiesOID;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.WF.Template.NodeAttr;

/** 
 节点集合
 
*/
public class Nodes extends EntitiesOID
{

		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Node();
	}

		///#endregion


		
	/** 
	 节点集合
	 
	*/
	public Nodes()
	{
	}
	/** 
	 节点集合.
	 
	 @param FlowNo
	 * @throws Exception 
	*/
	public Nodes(String fk_flow) throws Exception
	{
		//   Nodes nds = new Nodes();
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		//this.AddEntities(NodesCash.GetNodes(fk_flow));
		return;
	}

		///#endregion


		///#region 查询方法
	/** 
	 RetrieveAll
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		Object tempVar = Cash.GetObj(this.toString(), Depositary.Application);
		Nodes nds = (Nodes)((tempVar instanceof Nodes) ? tempVar : null);
		if (nds == null)
		{
			nds = new Nodes();
			QueryObject qo = new QueryObject(nds);
			qo.AddWhereInSQL(NodeAttr.NodeID, " SELECT Node FROM WF_Direction ");
			qo.addOr();
			qo.AddWhereInSQL(NodeAttr.NodeID, " SELECT ToNode FROM WF_Direction ");
			qo.DoQuery();

			Cash.AddObj(this.toString(), Depositary.Application, nds);
			Cash.AddObj(this.getGetNewEntity().toString(), Depositary.Application, nds);
		}

		this.clear();
		this.AddEntities(nds);
		return this.size();
	}
	/** 
	 开始节点
	 * @throws Exception 
	*/
	public final void RetrieveStartNode() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeAttr.NodePosType, NodePosType.Start.getValue());
		qo.addAnd();
		qo.AddWhereInSQL(NodeAttr.NodeID, "SELECT FK_Node FROM WF_NodeStation WHERE FK_STATION IN (SELECT FK_STATION FROM Port_EmpSTATION WHERE FK_Emp='" + BP.Web.WebUser.getNo() + "')");

		qo.addOrderBy(NodeAttr.FK_Flow);
		qo.DoQuery();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Node> ToJavaList()
	{
		return (java.util.List<Node>)(Object)this;
	}
	/** 
	 转化成list 为了翻译成java的需要
	 
	 @return List
	*/
	public final java.util.ArrayList<BP.WF.Node> Tolist()
	{
		java.util.ArrayList<BP.WF.Node> list = new java.util.ArrayList<BP.WF.Node>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BP.WF.Node)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}