package BP.WF;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 自定义运行路径
 
*/
public class TransferCustoms extends EntitiesMyPK
{

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

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<TransferCustom> ToJavaList()
	{
		return (java.util.List<TransferCustom>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<TransferCustom> Tolist()
	{
		java.util.ArrayList<TransferCustom> list = new java.util.ArrayList<TransferCustom>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TransferCustom)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}