package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点工作岗位
*/
public class NodeStations extends EntitiesMM
{
	/** 
	 他的工作岗位
	*/
	public final Stations getHisStations()
	{
		Stations ens = new Stations();
		for (NodeStation ns : this.ToJavaList())
		{
			ens.AddEntity(new Station(ns.getFK_Station()));
		}
		return ens;
	}
	/** 
	 他的工作节点
	*/
	public final Nodes getHisNodes()
	{
		Nodes ens = new Nodes();
		for (NodeStation ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;

	}
	/** 
	 节点工作岗位
	*/
	public NodeStations()
	{
	}
	/** 
	 节点工作岗位
	 
	 @param nodeID 节点ID
	*/
	public NodeStations(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeStationAttr.FK_Node, nodeID);
		qo.DoQuery();
	}
	/** 
	 节点工作岗位
	 
	 @param StationNo StationNo 
	*/
	public NodeStations(String StationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeStationAttr.FK_Station, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeStation();
	}
	/** 
	 取到一个工作岗位集合能够访问到的节点s
	 
	 @param sts 工作岗位集合
	 @return 
	*/
	public final Nodes GetHisNodes(Stations sts)
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Station st : sts)
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
	 工作岗位对应的节点
	 
	 @param stationNo 工作岗位编号
	 @return 节点s
	*/
	public final Nodes GetHisNodes(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeStationAttr.FK_Station, stationNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (NodeStation en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	*/
	public final Stations GetHisStations(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeStationAttr.FK_Node, nodeID);
		qo.DoQuery();

		Stations ens = new Stations();
		for (NodeStation en : this.ToJavaList())
		{
			ens.AddEntity(new Station(en.getFK_Station()));
		}
		return ens;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeStation> ToJavaList()
	{
		return (List<NodeStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeStation> Tolist()
	{
		ArrayList<NodeStation> list = new ArrayList<NodeStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeStation)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}