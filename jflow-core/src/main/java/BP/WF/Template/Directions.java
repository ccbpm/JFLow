package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

 /** 
  节点方向
 */
public class Directions extends En.Entities
{
	/** 
	 节点方向
	*/
	public Directions()
	{
	}
	/** 
	 方向
	 
	 @param flowNo
	*/
	public Directions(String flowNo)
	{
		this.Retrieve(DirectionAttr.FK_Flow, flowNo);
	}
	/** 
	 节点方向
	 
	 @param NodeID 节点ID
	*/
	public Directions(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DirectionAttr.Node,NodeID);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Direction();
	}
	/** 
	 此节点的转向方向集合
	 
	 @param nodeID 此节点的ID
	 @param isLifecyle 是不是判断在节点的生存期内		 
	 @return 转向方向集合(ToNodes) 
	*/
	public final Nodes GetHisToNodes(int nodeID, boolean isLifecyle)
	{
		Nodes nds = new Nodes();
		QueryObject qo = new QueryObject(nds);
		qo.AddWhereInSQL(NodeAttr.NodeID,"SELECT ToNode FROM WF_Direction WHERE Node=" + nodeID);
		qo.DoQuery();
		return nds;
	}
	/** 
	 转向此节点的集合的Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	*/
	public final Nodes GetHisFromNodes(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DirectionAttr.ToNode,nodeID);
		qo.DoQuery();
		Nodes ens = new Nodes();
		for (Direction en : this)
		{
			ens.AddEntity(new Node(en.getNode()));
		}
		return ens;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Direction> ToJavaList()
	{
		return (List<Direction>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Direction> Tolist()
	{
		ArrayList<Direction> list = new ArrayList<Direction>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Direction)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}