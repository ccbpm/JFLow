package BP.WF.Template;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 流程集合
*/
public class FlowSheets extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询
	/** 
	 查询出来全部的在生存期间内的流程
	 
	 @param FlowSort 流程类别
	 @param IsCountInLifeCycle 是不是计算在生存期间内 true 查询出来全部的 
	*/
	public final void Retrieve(String FlowSort)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BP.WF.Template.FlowAttr.FK_FlowSort, FlowSort);
		qo.addOrderBy(BP.WF.Template.FlowAttr.No);
		qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 工作流程
	*/
	public FlowSheets()
	{
	}
	/** 
	 工作流程
	 
	 @param fk_sort
	*/
	public FlowSheets(String fk_sort)
	{
		this.Retrieve(BP.WF.Template.FlowAttr.FK_FlowSort, fk_sort);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 得到实体
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowSheet();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowSheet> ToJavaList()
	{
		return (List<FlowSheet>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowSheet> Tolist()
	{
		ArrayList<FlowSheet> list = new ArrayList<FlowSheet>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FlowSheet)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}