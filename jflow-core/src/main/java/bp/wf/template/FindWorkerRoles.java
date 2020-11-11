package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 找人规则集合
*/
public class FindWorkerRoles extends EntitiesOID
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FindWorkerRole();
	}

		///


		///构造方法
	/** 
	 找人规则集合
	*/
	public FindWorkerRoles()
	{
	}
	/** 
	 找人规则集合
	 
	 @param nodeID
	 * @throws Exception 
	*/
	public FindWorkerRoles(int nodeID) throws Exception
	{
		this.Retrieve(FindWorkerRoleAttr.FK_Node, nodeID, FindWorkerRoleAttr.Idx);
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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
	public final ArrayList<FindWorkerRole> Tolist()
	{
		ArrayList<FindWorkerRole> list = new ArrayList<FindWorkerRole>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FindWorkerRole)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}