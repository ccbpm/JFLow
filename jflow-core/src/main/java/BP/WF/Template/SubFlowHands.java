package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 手工启动子流程集合
*/
public class SubFlowHands extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new SubFlowHand();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 手工启动子流程集合
	*/
	public SubFlowHands()
	{
	}
	/** 
	 手工启动子流程集合
	 
	 @param fk_node 节点ID
	 * @throws Exception 
	*/
	public SubFlowHands(int fk_node) throws Exception
	{
		this.Retrieve(SubFlowYanXuAttr.FK_Node, fk_node, SubFlowYanXuAttr.SubFlowType, SubFlowType.HandSubFlow.getValue(), SubFlowYanXuAttr.Idx);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SubFlowHand> ToJavaList()
	{
		return (List<SubFlowHand>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SubFlowHand> Tolist()
	{
		ArrayList<SubFlowHand> list = new ArrayList<SubFlowHand>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SubFlowHand)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}