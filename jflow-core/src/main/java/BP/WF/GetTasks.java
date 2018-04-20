package BP.WF;

import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;

/** 
 取回任务集合
 
*/
public class GetTasks extends BP.En.Entities
{
	public GetTasks()
	{
	}
	/** 
	 取回任务集合
	 * @throws Exception 
	 
	*/
	public GetTasks(String fk_flow) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GetTaskAttr.FK_Flow, fk_flow);
		qo.addAnd();
		qo.AddWhereLen(GetTaskAttr.CheckNodes, " >= ", 3, SystemConfig.getAppCenterDBType());
		qo.DoQuery();
	}
	/** 
	 获得实体
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GetTask();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GetTask> ToJavaList()
	{
		return (java.util.List<GetTask>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GetTask> Tolist()
	{
		java.util.ArrayList<GetTask> list = new java.util.ArrayList<GetTask>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GetTask)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}