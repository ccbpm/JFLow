package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 任务
 
*/
public class Tasks extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Task();
	}
	/** 
	 任务
	 
	*/
	public Tasks()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Task> ToJavaList()
	{
		return (java.util.List<Task>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<Task> Tolist()
	{
		java.util.ArrayList<Task> list = new java.util.ArrayList<Task>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Task)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}