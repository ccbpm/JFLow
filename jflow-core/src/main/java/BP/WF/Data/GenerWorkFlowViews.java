package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 流程实例s
*/
public class GenerWorkFlowViews extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new GenerWorkFlowView();
	}
	/** 
	 流程实例集合
	*/
	public GenerWorkFlowViews()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GenerWorkFlowView> ToJavaList()
	{
		return (List<GenerWorkFlowView>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GenerWorkFlowView> Tolist()
	{
		ArrayList<GenerWorkFlowView> list = new ArrayList<GenerWorkFlowView>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerWorkFlowView)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}