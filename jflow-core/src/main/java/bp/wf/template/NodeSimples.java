package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.port.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

/** 
 节点集合
*/
public class NodeSimples extends Entities
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeSimple();
	}

		///


		///构造方法
	/** 
	 节点集合
	*/
	public NodeSimples()
	{
	}
	/** 
	 节点集合.
	 
	 @param FlowNo
	 * @throws Exception 
	*/
	public NodeSimples(String fk_flow) throws Exception
	{
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeSimple> ToJavaList()
	{
		return (List<NodeSimple>)(Object)this;
	}
	/** 
	 转化成list 为了翻译成java的需要
	 
	 @return List
	*/
	public final ArrayList<bp.wf.template.NodeSimple> Tolist()
	{
		ArrayList<bp.wf.template.NodeSimple> list = new ArrayList<bp.wf.template.NodeSimple>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((bp.wf.template.NodeSimple)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}