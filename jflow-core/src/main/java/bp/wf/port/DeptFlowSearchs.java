package bp.wf.port;

import bp.en.*;
import java.util.*;

/** 
 流程部门数据查询权限 
*/
public class DeptFlowSearchs extends EntitiesMyPK
{

		///构造
	/** 
	 流程部门数据查询权限
	*/
	public DeptFlowSearchs()
	{
	}
	/** 
	 流程部门数据查询权限
	 
	 @param FK_Emp FK_Emp
	 * @throws Exception 
	*/
	public DeptFlowSearchs(String FK_Emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptFlowSearchAttr.FK_Emp, FK_Emp);
		qo.DoQuery();
	}

		///


		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptFlowSearch();
	}

		///


		///查询方法

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<DeptFlowSearch> ToJavaList()
	{
		return (List<DeptFlowSearch>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DeptFlowSearch> Tolist()
	{
		ArrayList<DeptFlowSearch> list = new ArrayList<DeptFlowSearch>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptFlowSearch)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}