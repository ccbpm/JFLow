package bp.ta;

import bp.en.EntitiesMyPK;
import bp.en.Entity;

import java.util.*;

/** 
 工作人员s
*/
public class WorkerLists extends EntitiesMyPK
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new WorkerList();
	}
	/** 
	 工作人员
	*/
	public WorkerLists()
	{
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<WorkerList> ToJavaList()
	{
		return (java.util.List<WorkerList>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<WorkerList> Tolist()
	{
		ArrayList<WorkerList> list = new ArrayList<WorkerList>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((WorkerList)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
