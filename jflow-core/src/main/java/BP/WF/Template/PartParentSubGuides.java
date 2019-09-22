package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 配件s
*/
public class PartParentSubGuides extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new PartParentSubGuide();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 配件集合
	*/
	public PartParentSubGuides()
	{
	}
	/** 
	 配件集合.
	 
	 @param FlowNo
	*/
	public PartParentSubGuides(String fk_flow)
	{
		this.Retrieve(PartAttr.FK_Flow, fk_flow);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<PartParentSubGuide> ToJavaList()
	{
		return (List<PartParentSubGuide>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<PartParentSubGuide> Tolist()
	{
		ArrayList<PartParentSubGuide> list = new ArrayList<PartParentSubGuide>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((PartParentSubGuide)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}