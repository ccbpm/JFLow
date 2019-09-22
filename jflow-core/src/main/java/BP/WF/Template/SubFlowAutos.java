package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 自动触发子流程集合
*/
public class SubFlowAutos extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SubFlowAuto();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 自动触发子流程集合
	*/
	public SubFlowAutos()
	{
	}
	/** 
	 自动触发子流程集合.
	 
	 @param fk_node 节点
	*/
	public SubFlowAutos(int fk_node)
	{
		this.Retrieve(SubFlowYanXuAttr.FK_Node, fk_node, SubFlowYanXuAttr.SubFlowType, SubFlowType.AutoSubFlow.getValue(), SubFlowYanXuAttr.Idx);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SubFlowAuto> ToJavaList()
	{
		return (List<SubFlowAuto>)this;
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
			list.add((SubFlowAuto)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}