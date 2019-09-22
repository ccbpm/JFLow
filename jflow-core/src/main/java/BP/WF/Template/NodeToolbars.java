package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 工具栏集合
*/
public class NodeToolbars extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeToolbar();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 工具栏集合
	*/
	public NodeToolbars()
	{
	}
	/** 
	 工具栏集合.
	 
	 @param fk_node
	*/
	public NodeToolbars(String fk_node)
	{
		this.Retrieve(NodeToolbarAttr.FK_Node, fk_node);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List /// <summary>
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeToolbar> ToJavaList()
	{
		return (List<NodeToolbar>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeToolbar> Tolist()
	{
		ArrayList<NodeToolbar> list = new ArrayList<NodeToolbar>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeToolbar)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}