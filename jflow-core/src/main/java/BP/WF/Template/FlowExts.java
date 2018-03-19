package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 流程集合
 
*/
public class FlowExts extends EntitiesNoName
{

		///#region 查询
	/** 
	 查询出来全部的在生存期间内的流程
	 
	 @param FlowSort 流程类别
	 @param IsCountInLifeCycle 是不是计算在生存期间内 true 查询出来全部的 
	*/
	public final int Retrieve(String FlowSort)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BP.WF.Template.FlowAttr.FK_FlowSort, FlowSort);
		qo.addOrderBy(BP.WF.Template.FlowAttr.No);
		return qo.DoQuery();
	}

		///#endregion


		
	/** 
	 工作流程
	 
	*/
	public FlowExts()
	{
	}
	/** 
	 工作流程
	 
	 @param fk_sort
	*/
	public FlowExts(String fk_sort)
	{
		this.Retrieve(BP.WF.Template.FlowAttr.FK_FlowSort, fk_sort);
	}

		///#endregion


		///#region 得到实体
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowExt();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FlowExt> ToJavaList()
	{
		return (java.util.List<FlowExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FlowExt> Tolist()
	{
		java.util.ArrayList<FlowExt> list = new java.util.ArrayList<FlowExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}