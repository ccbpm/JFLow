package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 子流程集合
*/
public class SubFlows extends EntitiesMyPK
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SubFlow();
	}

		///


		///构造方法
	/** 
	 子流程集合
	*/
	public SubFlows()
	{
	}
	 /** 
	 子流程集合.
	 
	 @param fk_node 节点
	 * @throws Exception 
	 */
	public SubFlows(int fk_node) throws Exception
	{
		this.Retrieve(SubFlowYanXuAttr.FK_Node, fk_node);
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SubFlow> ToJavaList()
	{
		return (List<SubFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SubFlow> Tolist()
	{
		ArrayList<SubFlow> list = new ArrayList<SubFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SubFlow)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}