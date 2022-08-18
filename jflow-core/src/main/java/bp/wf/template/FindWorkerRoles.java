package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

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
	public Entity getGetNewEntity() {
		return new FindWorkerRole();
	}

		///#endregion


		///#region 构造方法
	/** 
	 找人规则集合
	*/
	public FindWorkerRoles() throws Exception {
	}
	/** 
	 找人规则集合
	 
	 param nodeID
	*/
	public FindWorkerRoles(int nodeID) throws Exception {
		this.Retrieve(FindWorkerRoleAttr.FK_Node, nodeID, FindWorkerRoleAttr.Idx);
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FindWorkerRole> ToJavaList() {
		return (java.util.List<FindWorkerRole>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FindWorkerRole> Tolist()  {
		ArrayList<FindWorkerRole> list = new ArrayList<FindWorkerRole>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FindWorkerRole)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}