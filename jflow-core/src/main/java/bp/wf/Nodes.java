package bp.wf;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.port.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.wf.port.*;
import java.util.*;
import java.math.*;

/** 
 节点集合
*/
public class Nodes extends EntitiesOID
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Node();
	}

		///


		///构造方法
	/** 
	 节点集合
	*/
	public Nodes()
	{
	}
	/** 
	 节点集合.
	 
	 @param FlowNo
	 * @throws Exception 
	*/
	public Nodes(String fk_flow) throws Exception
	{
		//   Nodes nds = new Nodes();
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		//this.AddEntities(NodesCash.GetNodes(fk_flow));
		return;
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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
	public final ArrayList<bp.wf.Node> Tolist()
	{
		ArrayList<bp.wf.Node> list = new ArrayList<bp.wf.Node>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((bp.wf.Node)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}