package bp.wf;

import java.util.ArrayList;
import java.util.List;

import bp.en.Entity;
import bp.en.QueryObject;

/** 
 取回任务集合
*/
public class GetTasks extends bp.en.Entities
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
		qo.AddWhereLen(GetTaskAttr.CheckNodes, " >= ", 3, bp.difference.SystemConfig.getAppCenterDBType());
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


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GetTask> ToJavaList()
	{
		return (List<GetTask>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GetTask> Tolist()
	{
		ArrayList<GetTask> list = new ArrayList<GetTask>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GetTask)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}