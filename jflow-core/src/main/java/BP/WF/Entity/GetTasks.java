package BP.WF.Entity;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;

/**
 * 取回任务集合
 */
public class GetTasks extends BP.En.Entities
{
	
	public static ArrayList<GetTask> convertGetTasks(Object obj)
	{
		return (ArrayList<GetTask>) obj;
	}
	public List<GetTask> ToJavaList()
	{
		return (List<GetTask>)(Object)this;
	}
	public GetTasks()
	{
	}
	
	/**
	 * 取回任务集合
	 */
	public GetTasks(String fk_flow)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GetTaskAttr.FK_Flow, fk_flow);
		qo.addAnd();
		qo.AddWhereLen(GetTaskAttr.CheckNodes, " >= ", 3,
				SystemConfig.getAppCenterDBType());
		qo.DoQuery();
	}
	
	/**
	 * 获得实体
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new GetTask();
	}
}