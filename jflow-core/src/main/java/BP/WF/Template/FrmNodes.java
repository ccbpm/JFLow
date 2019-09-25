package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 节点表单s
*/
public class FrmNodes extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
	/** 
	 他的工作节点
	 * @throws Exception 
	*/
	public final Nodes getHisNodes() throws Exception
	{
		Nodes ens = new Nodes();
		for (FrmNode ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法..
	/** 
	 节点表单
	*/
	public FrmNodes()
	{
	}
	/** 
	 节点表单
	 
	 @param NodeID 节点ID
	 * @throws Exception 
	*/
	public FrmNodes(String fk_flow, int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmNodeAttr.FK_Flow, fk_flow);
		qo.addAnd();
		qo.AddWhere(FrmNodeAttr.FK_Node, nodeID);

		qo.addOrderBy(FrmNodeAttr.Idx);
		qo.DoQuery();
	}
	/** 
	 节点表单
	 
	 @param NodeNo NodeNo 
	 * @throws Exception 
	*/
	public FrmNodes(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmNodeAttr.FK_Node, nodeID);
		qo.addOrderBy(FrmNodeAttr.Idx);
		qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法..

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共方法.
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmNode();
	}
	/** 
	 节点表单s
	 
	 @param sts 节点表单
	 @return 
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(Nodes sts) throws Exception
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Node st : sts.ToJavaList())
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
	 节点表单
	 
	 @param NodeNo 工作节点编号
	 @return 节点s
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(String NodeNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmNodeAttr.FK_Node, NodeNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (FrmNode en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Frm()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmNodeAttr.FK_Frm, nodeID);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (FrmNode en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公共方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmNode> ToJavaList()
	{
		return (List<FrmNode>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNode> Tolist()
	{
		ArrayList<FrmNode> list = new ArrayList<FrmNode>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNode)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}