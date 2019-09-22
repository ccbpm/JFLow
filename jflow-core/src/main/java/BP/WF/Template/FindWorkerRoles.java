package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.GPM.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 找人规则集合
*/
public class FindWorkerRoles extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FindWorkerRole();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FindWorkerRole> ToJavaList()
	{
		return (List<FindWorkerRole>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FindWorkerRole> Tolist()
	{
		ArrayList<FindWorkerRole> list = new ArrayList<FindWorkerRole>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FindWorkerRole)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}