package bp.wf.admin;

import bp.en.*;

import java.util.*;

/** 
 流程集合
*/
public class Flows extends EntitiesNoName
{

		///#region 查询
	/** 
	 查询出来全部的在生存期间内的流程
	 * param IsCountInLifeCycle 是不是计算在生存期间内 true 查询出来全部的
	 * @param FlowSort 流程类别
	 * @return

	 */
	@Override
	public final int Retrieve(String FlowSort) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(bp.wf.template.FlowAttr.FK_FlowSort, FlowSort);
		qo.addOrderBy(bp.wf.template.FlowAttr.No);
		qo.DoQuery();
		return 0;
	}

		///#endregion


		///#region 构造方法
	/** 
	 工作流程
	*/
	public Flows()
	{
	}
	/** 
	 工作流程
	 
	 @param fk_sort
	*/
	public Flows(String fk_sort) throws Exception {
		this.Retrieve(bp.wf.template.FlowAttr.FK_FlowSort, fk_sort);
	}

		///#endregion


		///#region 得到实体
	/** 
	 得到它的 Entity 
	*/

	public Entity getGetNewEntity()
	{
		return new Flow();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Flow> ToJavaList()
	{

		return (List<Flow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Flow> Tolist()
	{
		ArrayList<Flow> list = new ArrayList<Flow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Flow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}