package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 抄送到岗位
*/
public class CCStations extends EntitiesMM
{
	/** 
	 他的工作岗位
	*/
	public final Stations getHisStations()
	{
		Stations ens = new Stations();
		for (CCStation ns : this)
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
		for (CCStation ns : this)
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
	*/
	public CCStations(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCStationAttr.FK_Node, nodeID);
		qo.DoQuery();
	}
	/** 
	 抄送到岗位
	 
	 @param StationNo StationNo 
	*/
	public CCStations(String StationNo)
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
	*/
	public final Nodes GetHisNodes(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCStationAttr.FK_Station, stationNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (CCStation en : this)
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
		qo.AddWhere(CCStationAttr.FK_Node, nodeID);
		qo.DoQuery();

		Stations ens = new Stations();
		for (CCStation en : this)
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
	public final List<CCStation> ToJavaList()
	{
		return (List<CCStation>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CCStation> Tolist()
	{
		ArrayList<CCStation> list = new ArrayList<CCStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CCStation)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}