package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程关联人员s
*/
public class FlowRefEmps extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowRefEmp();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 流程关联人员集合
	*/
	public FlowRefEmps()
	{
	}
	/** 
	 流程关联人员集合.
	 
	 @param FlowNo
	*/
	public FlowRefEmps(String fk_flow)
	{
		this.Retrieve(FlowRefEmpAttr.FK_Flow, fk_flow);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}