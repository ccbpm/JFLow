package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;

/** 
 审核组件s
*/
public class NodeWorkChecks extends Entities
{

		///构造
	/** 
	 审核组件s
	*/
	public NodeWorkChecks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeWorkCheck();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeWorkCheck> ToJavaList()
	{
		return (List<NodeWorkCheck>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeWorkCheck> Tolist()
	{
		ArrayList<NodeWorkCheck> list = new ArrayList<NodeWorkCheck>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeWorkCheck)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}