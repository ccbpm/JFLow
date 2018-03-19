package BP.WF.Template;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Port.Emp;
import BP.WF.Port.Emps;

/** 
 节点人员
 
*/
public class NodeEmps extends EntitiesMM
{
	/** 
	 他的到人员
	 
	*/
	public final Emps getHisEmps()
	{
		Emps ens = new Emps();
		for (NodeEmp ns : this.ToJavaList())
		{
			ens.AddEntity(new Emp(ns.getFK_Emp()));
		}
		return ens;
	}
	/** 
	 他的工作节点
	 
	*/
	public final Nodes getHisNodes()
	{
		Nodes ens = new Nodes();
		for (NodeEmp ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;

	}
	/** 
	 节点人员
	 
	*/
	public NodeEmps()
	{
	}
	/** 
	 节点人员
	 
	 @param NodeID 节点ID
	*/
	public NodeEmps(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 节点人员
	 
	 @param EmpNo EmpNo 
	*/
	public NodeEmps(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Emp, EmpNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeEmp();
	}
	/** 
	 取到一个到人员集合能够访问到的节点s
	 
	 @param sts 到人员集合
	 @return 
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
	 到人员对应的节点
	 
	 @param EmpNo 到人员编号
	 @return 节点s
	*/
	public final Nodes GetHisNodes(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Emp, EmpNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (NodeEmp en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的 Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	*/
	public final Emps GetHisEmps(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Node, nodeID);
		qo.DoQuery();

		Emps ens = new Emps();
		for (NodeEmp en : this.ToJavaList())
		{
			ens.AddEntity(new Emp(en.getFK_Emp()));
		}
		return ens;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<NodeEmp> ToJavaList()
	{
		return (java.util.List<NodeEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<NodeEmp> Tolist()
	{
		java.util.ArrayList<NodeEmp> list = new java.util.ArrayList<NodeEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeEmp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}