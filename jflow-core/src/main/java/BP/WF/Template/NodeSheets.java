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

		///#region 构造方法
	/** 
	 节点集合
	*/
	public NodeSheets()
	{
	}

		///#endregion

	@Override
	public Entity getNewEntity()
	{
		return new NodeSheet();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeSheet> ToJavaList()
	{
		return (List<NodeSheet>)(Object)this;
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
			list.add((NodeSheet)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}