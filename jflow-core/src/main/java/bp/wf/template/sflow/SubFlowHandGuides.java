package bp.wf.template.sflow;

import bp.en.*;
import java.util.*;

/** 
 手工启动子流程集合
*/
public class SubFlowHandGuides extends EntitiesMyPK
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new SubFlowHandGuide();
	}

		///#endregion


		///#region 构造方法
	/** 
	 手工启动子流程集合
	*/
	public SubFlowHandGuides() throws Exception {
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<SubFlowHandGuide> ToJavaList() {
		return (java.util.List<SubFlowHandGuide>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SubFlowHandGuide> Tolist()  {
		ArrayList<SubFlowHandGuide> list = new ArrayList<SubFlowHandGuide>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SubFlowHandGuide)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}