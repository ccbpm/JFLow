package BP.WF.Template;

import java.util.ArrayList;
import java.util.List;

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
	*/
	 public TransferCustoms(Long workid)
     {
         this.Retrieve(TransferCustomAttr.WorkID, workid, TransferCustomAttr.Idx);
     }
	 /** 
	 自定义运行路径
	*/
     public TransferCustoms(int nodeID, Long workid)
     {
         this.Retrieve(TransferCustomAttr.WorkID, workid, TransferCustomAttr.FK_Node, nodeID, TransferCustomAttr.Idx);
     }
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TransferCustom> ToJavaList()
	{
		return (List<TransferCustom>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TransferCustom> Tolist()
	{
		ArrayList<TransferCustom> list = new ArrayList<TransferCustom>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TransferCustom)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}