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

		///#region 查询
	/** 
	 查询出来全部的在生存期间内的流程
	 
	 @param FlowSort 流程类别
	 @param IsCountInLifeCycle 是不是计算在生存期间内 true 查询出来全部的 
	 * @throws Exception 
	*/
	public final int Retrieve(String FlowSort) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BP.WF.Template.FlowAttr.FK_FlowSort, FlowSort);
		qo.addOrderBy(BP.WF.Template.FlowAttr.No);
		return qo.DoQuery();
	}

		///#endregion


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
	 * @throws Exception 
	*/
	public FlowSheets(String fk_sort) throws Exception
	{
		this.Retrieve(BP.WF.Template.FlowAttr.FK_FlowSort, fk_sort);
	}

		///#endregion


		///#region 得到实体
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FlowSheet();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowSheet> ToJavaList()
	{
		return (List<FlowSheet>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowSheet> Tolist()
	{
		ArrayList<FlowSheet> list = new ArrayList<FlowSheet>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowSheet)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}