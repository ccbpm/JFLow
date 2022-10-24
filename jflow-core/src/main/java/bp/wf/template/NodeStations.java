package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 节点工作岗位s
*/
public class NodeStations extends EntitiesMyPK
{

		///#region 构造函数.
	/** 
	 节点工作岗位
	*/
	public NodeStations()  {
	}
	/** 
	 节点工作岗位
	 
	 param nodeID 节点ID
	*/
	public NodeStations(int nodeID) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeStationAttr.FK_Node, nodeID);
		qo.DoQuery();
	}
	/** 
	 节点工作岗位
	 
	 param StationNo StationNo
	*/
	public NodeStations(String StationNo) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeStationAttr.FK_Station, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new NodeStation();
	}

		///#endregion 构造函数.


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<NodeStation> ToJavaList() {
		return (java.util.List<NodeStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeStation> Tolist()  {
		ArrayList<NodeStation> list = new ArrayList<NodeStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeStation)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}