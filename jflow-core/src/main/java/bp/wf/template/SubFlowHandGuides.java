package bp.wf.template;

import bp.en.*;
import java.util.*;

/** 
 手工启动子流程集合
*/
public class SubFlowHandGuides extends EntitiesMyPK
{
 
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SubFlowHandGuide();
	}
 
		 
	/** 
	 手工启动子流程集合
	*/
	public SubFlowHandGuides()
	{
	}
	 
		 
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SubFlowHandGuide> ToJavaList()
	{
		return (List<SubFlowHandGuide>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SubFlowHandGuide> Tolist()
	{
		ArrayList<SubFlowHandGuide> list = new ArrayList<SubFlowHandGuide>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SubFlowHandGuide)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}