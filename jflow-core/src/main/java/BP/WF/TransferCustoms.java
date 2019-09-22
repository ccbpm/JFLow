package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.Template.*;
import java.util.*;

/** 
 自定义运行路径
*/
public class TransferCustoms extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TransferCustom();
	}
	/** 
	 自定义运行路径
	*/
	public TransferCustoms()
	{
	}
	/** 
	 自定义运行路径
	 
	 @param workid 工作ID
	*/
	public TransferCustoms(long workid)
	{
		this.Retrieve(TransferCustomAttr.WorkID, workid, TransferCustomAttr.Idx);
	}
	/** 
	 自定义运行路径
	 
	 @param nodeID 节点ID
	 @param workid 工作ID
	*/
	public TransferCustoms(int nodeID, long workid)
	{
		this.Retrieve(TransferCustomAttr.WorkID, workid, TransferCustomAttr.FK_Node, nodeID, TransferCustomAttr.Idx);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TransferCustom> ToJavaList()
	{
		return (List<TransferCustom>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TransferCustom> Tolist()
	{
		ArrayList<TransferCustom> list = new ArrayList<TransferCustom>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((TransferCustom)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}