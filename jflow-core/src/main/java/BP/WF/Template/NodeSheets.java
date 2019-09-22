package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点集合
*/
public class NodeSheets extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 节点集合
	*/
	public NodeSheets()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	public Entity getGetNewEntity()
	{
		return new NodeSheet();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeSheet> ToJavaList()
	{
		return (List<NodeSheet>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeSheet> Tolist()
	{
		ArrayList<NodeSheet> list = new ArrayList<NodeSheet>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeSheet)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}