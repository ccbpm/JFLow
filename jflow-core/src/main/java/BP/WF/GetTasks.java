package BP.WF;

import BP.En.*;
import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import java.util.*;

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
	*/
	public GetTasks(String fk_flow)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GetTaskAttr.FK_Flow, fk_flow);
		qo.addAnd();
		qo.AddWhereLen(GetTaskAttr.CheckNodes, " >= ", 3, SystemConfig.AppCenterDBType);
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GetTask> ToJavaList()
	{
		return (List<GetTask>)this;
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
			list.add((GetTask)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}