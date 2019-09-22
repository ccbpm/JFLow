package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 流程类别
*/
public class FlowSorts extends EntitiesTree
{
	/** 
	 流程类别s
	*/
	public FlowSorts()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowSort();
	}
	@Override
	public int RetrieveAll()
	{
		int i = super.RetrieveAll(FlowSortAttr.Idx);
		if (i == 0)
		{
			FlowSort fs = new FlowSort();
			fs.Name = "公文类";
			fs.No = "01";
			fs.Insert();

			fs = new FlowSort();
			fs.Name = "办公类";
			fs.No = "02";
			fs.Insert();
			i = super.RetrieveAll();
		}

		return i;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowSort> ToJavaList()
	{
		return (List<FlowSort>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowSort> Tolist()
	{
		ArrayList<FlowSort> list = new ArrayList<FlowSort>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FlowSort)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}