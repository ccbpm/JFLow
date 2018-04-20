package BP.WF.Template;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Port.Station;
import BP.WF.Port.Stations;

/** 
 抄送到岗位
 
*/
public class CCStations extends EntitiesMM
{
	/** 
	 他的工作岗位
	 * @throws Exception 
	 
	*/
	public final Stations getHisStations() throws Exception
	{
		Stations ens = new Stations();
		for (CCStation ns : this.ToJavaList())
		{
			ens.AddEntity(new Station(ns.getFK_Station()));
		}
		return ens;
	}
	/** 
	 他的工作节点
	 * @throws Exception 
	 
	*/
	public final Nodes getHisNodes() throws Exception
	{
		Nodes ens = new Nodes();
		for (CCStation ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;

	}
	/** 
	 抄送到岗位
	 
	*/
	public CCStations()
	{
	}
	/** 
	 抄送到岗位
	 
	 @param nodeID 节点ID
	 * @throws Exception 
	*/
	public CCStations(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCStationAttr.FK_Node, nodeID);
		qo.DoQuery();
	}
	/** 
	 抄送到岗位
	 
	 @param StationNo StationNo 
	 * @throws Exception 
	*/
	public CCStations(String StationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCStationAttr.FK_Station, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CCStation();
	}
	/** 
	 工作岗位对应的节点
	 
	 @param stationNo 工作岗位编号
	 @return 节点s
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(String stationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCStationAttr.FK_Station, stationNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (CCStation en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	 * @throws Exception 
	*/
	public final Stations GetHisStations(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCStationAttr.FK_Node, nodeID);
		qo.DoQuery();

		Stations ens = new Stations();
		for (CCStation en : this.ToJavaList())
		{
			ens.AddEntity(new Station(en.getFK_Station()));
		}
		return ens;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<CCStation> ToJavaList()
	{
		return (java.util.List<CCStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<CCStation> Tolist()
	{
		java.util.ArrayList<CCStation> list = new java.util.ArrayList<CCStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CCStation)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}