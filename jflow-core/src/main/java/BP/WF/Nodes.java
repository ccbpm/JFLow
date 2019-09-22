package BP.WF;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.Port.*;
import java.util.*;
import java.math.*;

/** 
 节点集合
*/
public class Nodes extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Node();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 节点集合
	*/
	public Nodes()
	{
	}
	/** 
	 节点集合.
	 
	 @param FlowNo
	*/
	public Nodes(String fk_flow)
	{
		//   Nodes nds = new Nodes();
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		//this.AddEntities(NodesCash.GetNodes(fk_flow));
		return;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询方法
	/** 
	 RetrieveAll
	 
	 @return 
	*/
	@Override
	public int RetrieveAll()
	{
		Object tempVar = Cash.GetObj(this.toString(), Depositary.Application);
		Nodes nds = tempVar instanceof Nodes ? (Nodes)tempVar : null;
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

		this.Clear();
		this.AddEntities(nds);
		return this.size();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Node> ToJavaList()
	{
		return (List<Node>)(Object)this;
	}
	/** 
	 转化成list 为了翻译成java的需要
	 
	 @return List
	*/
	public final ArrayList<BP.WF.Node> Tolist()
	{
		ArrayList<BP.WF.Node> list = new ArrayList<BP.WF.Node>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BP.WF.Node)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}