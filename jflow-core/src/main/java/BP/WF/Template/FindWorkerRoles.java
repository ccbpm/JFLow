package BP.WF.Template;

import java.util.List;

import BP.En.EntitiesOID;
import BP.En.Entity;

/** 
 找人规则集合
 
*/
public class FindWorkerRoles extends EntitiesOID
{

		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FindWorkerRole();
	}

		///#endregion


		
	/** 
	 找人规则集合
	 
	*/
	public FindWorkerRoles()
	{
	}
	/** 
	 找人规则集合
	 
	 @param nodeID
	*/
	public FindWorkerRoles(int nodeID)
	{
		this.Retrieve(FindWorkerRoleAttr.FK_Node, nodeID, FindWorkerRoleAttr.Idx);
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FindWorkerRole> ToJavaList()
	{
		return (List<FindWorkerRole>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FindWorkerRole> Tolist()
	{
		java.util.ArrayList<FindWorkerRole> list = new java.util.ArrayList<FindWorkerRole>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FindWorkerRole)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}