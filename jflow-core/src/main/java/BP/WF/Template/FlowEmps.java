package BP.WF.Template;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Port.Station;
import BP.Port.Stations;
import BP.WF.Node;
import BP.WF.Nodes;

/** 
 流程岗位属性
 
*/
public class FlowEmps extends EntitiesMM
{
	/** 
	 他的人员
	 
	*/
	public final Stations getHisStations()
	{
		Stations ens = new Stations();
		for (FlowEmp ns : this.ToJavaList())
		{
			ens.AddEntity(new Station(ns.getFK_Emp()));
		}
		return ens;
	}
	/** 
	 他的工作流程
	 
	*/
	public final Nodes getHisNodes()
	{
		Nodes ens = new Nodes();
		for (FlowEmp ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Flow()));
		}
		return ens;

	}
	/** 
	 流程岗位属性
	 
	*/
	public FlowEmps()
	{
	}
	/** 
	 流程岗位属性
	 
	 @param NodeID 流程ID
	*/
	public FlowEmps(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowEmpAttr.FK_Flow, NodeID);
		qo.DoQuery();
	}
	/** 
	 流程岗位属性
	 
	 @param StationNo StationNo 
	*/
	public FlowEmps(String StationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowEmpAttr.FK_Emp, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowEmp();
	}
	/** 
	 取到一个人员集合能够访问到的流程s
	 
	 @param sts 人员集合
	 @return 
	*/
	public final Nodes GetHisNodes(Stations sts)
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Station st : sts.ToJavaList())
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
	 取到一个工作人员能够访问到的流程。
	 
	 @param empId 工作人员ID
	 @return 
	*/
	public final Nodes GetHisNodes_del(String empId)
	{
		Emp em = new Emp(empId);
		return this.GetHisNodes(em.getHisStations());
	}
	/** 
	 人员对应的流程
	 
	 @param stationNo 人员编号
	 @return 流程s
	*/
	public final Nodes GetHisNodes(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowEmpAttr.FK_Emp, stationNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (FlowEmp en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Flow()));
		}
		return ens;
	}
	/** 
	 转向此流程的集合的Nodes
	 
	 @param nodeID 此流程的ID
	 @return 转向此流程的集合的Nodes (FromNodes) 
	*/
	public final Stations GetHisStations(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowEmpAttr.FK_Flow, nodeID);
		qo.DoQuery();

		Stations ens = new Stations();
		for (FlowEmp en : this.ToJavaList())
		{
			ens.AddEntity(new Station(en.getFK_Emp()));
		}
		return ens;
	}



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FlowEmp> ToJavaList()
	{
		return (java.util.List<FlowEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FlowEmp> Tolist()
	{
		java.util.ArrayList<FlowEmp> list = new java.util.ArrayList<FlowEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowEmp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}