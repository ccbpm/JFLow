package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 自动触发子流程集合
*/
public class SubFlowAutos extends EntitiesMyPK
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SubFlowAuto();
	}

		///


		///构造方法
	/** 
	 自动触发子流程集合
	*/
	public SubFlowAutos()
	{
	}
	/** 
	 自动触发子流程集合.
	 
	 @param fk_node 节点
	 * @throws Exception 
	*/
	public SubFlowAutos(int fk_node) throws Exception
	{
		this.Retrieve(SubFlowYanXuAttr.FK_Node, fk_node, SubFlowYanXuAttr.SubFlowType, SubFlowType.AutoSubFlow.getValue(), SubFlowYanXuAttr.Idx);
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SubFlowAuto> ToJavaList()
	{
		return (List<SubFlowAuto>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SubFlowAuto> Tolist()
	{
		ArrayList<SubFlowAuto> list = new ArrayList<SubFlowAuto>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SubFlowAuto)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}