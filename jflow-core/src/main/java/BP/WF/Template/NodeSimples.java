package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点集合
*/
public class NodeSimples extends EntitiesOIDName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeSimple();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 节点集合
	*/
	public NodeSimples()
	{
	}
	/** 
	 节点集合.
	 
	 @param FlowNo
	*/
	public NodeSimples(String fk_flow)
	{
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeSimple> ToJavaList()
	{
		return (List<NodeSimple>)this;
	}
	/** 
	 转化成list 为了翻译成java的需要
	 
	 @return List
	*/
	public final ArrayList<BP.WF.Template.NodeSimple> Tolist()
	{
		ArrayList<BP.WF.Template.NodeSimple> list = new ArrayList<BP.WF.Template.NodeSimple>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BP.WF.Template.NodeSimple)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}