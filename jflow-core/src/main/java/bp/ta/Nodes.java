package bp.ta;

import bp.en.EntitiesTree;
import bp.en.Entity;
import java.util.*;

/** 
 模板节点
*/
public class Nodes extends EntitiesTree
{
	/** 
	 模板节点s
	*/
	public Nodes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Node();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Node> ToJavaList()
	{
		return (java.util.List<Node>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Node> Tolist()
	{
		ArrayList<Node> list = new ArrayList<Node>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Node)this.get(i));
		}
		return list;
	}
	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
