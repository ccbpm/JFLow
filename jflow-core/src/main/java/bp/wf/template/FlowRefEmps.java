package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.template.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 流程关联人员s
*/
public class FlowRefEmps extends EntitiesMyPK
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowRefEmp();
	}

		///


		///构造方法
	/** 
	 流程关联人员集合
	*/
	public FlowRefEmps()
	{
	}
	/** 
	 流程关联人员集合.
	 
	 @param FlowNo
	 * @throws Exception 
	*/
	public FlowRefEmps(String fk_flow) throws Exception
	{
		this.Retrieve(FlowRefEmpAttr.FK_Flow, fk_flow);
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowRefEmp> ToJavaList()
	{
		return (List<FlowRefEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowRefEmp> Tolist()
	{
		ArrayList<FlowRefEmp> list = new ArrayList<FlowRefEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowRefEmp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}