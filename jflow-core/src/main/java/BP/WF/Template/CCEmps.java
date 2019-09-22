package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点到人员
*/
public class CCEmps extends EntitiesMM
{
	/** 
	 他的到人员
	*/
	public final Emps getHisEmps()
	{
		Emps ens = new Emps();
		for (CCEmp ns : this)
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
		for (CCEmp ns : this)
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;

	}
	/** 
	 节点到人员
	*/
	public CCEmps()
	{
	}
	/** 
	 节点到人员
	 
	 @param NodeID 节点ID
	*/
	public CCEmps(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCEmpAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 节点到人员
	 
	 @param EmpNo EmpNo 
	*/
	public CCEmps(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCEmpAttr.FK_Emp, EmpNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CCEmp();
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
		for (Emp st : sts)
		{
			tmp = this.GetHisNodes(st.No);
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
	 到人员对应的节点
	 
	 @param EmpNo 到人员编号
	 @return 节点s
	*/
	public final Nodes GetHisNodes(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCEmpAttr.FK_Emp, EmpNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (CCEmp en : this)
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
		qo.AddWhere(CCEmpAttr.FK_Node, nodeID);
		qo.DoQuery();

		Emps ens = new Emps();
		for (CCEmp en : this)
		{
			ens.AddEntity(new Emp(en.getFK_Emp()));
		}
		return ens;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CCEmp> ToJavaList()
	{
		return (List<CCEmp>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CCEmp> Tolist()
	{
		ArrayList<CCEmp> list = new ArrayList<CCEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CCEmp)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}