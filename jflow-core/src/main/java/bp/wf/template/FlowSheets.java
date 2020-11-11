package bp.wf.template;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.data.*;
import bp.wf.*;
import java.util.*;
import java.time.*;

/** 
 流程集合
*/
public class FlowSheets extends EntitiesNoName
{

		///查询
	/** 
	 查询出来全部的在生存期间内的流程
	 
	 @param FlowSort 流程类别
	 @param IsCountInLifeCycle 是不是计算在生存期间内 true 查询出来全部的 
	 * @return 
	 * @throws Exception 
	*/
	public final int Retrieve(String FlowSort) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(bp.wf.template.FlowAttr.FK_FlowSort, FlowSort);
		qo.addOrderBy(bp.wf.template.FlowAttr.No);
		return qo.DoQuery();
	}

		///


		///构造方法
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
		this.Retrieve(bp.wf.template.FlowAttr.FK_FlowSort, fk_sort);
	}

		///


		///得到实体
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowSheet();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}