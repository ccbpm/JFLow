package bp.wf.data.admingroup;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.*;
import bp.wf.*;
import bp.wf.data.*;
import java.util.*;

/** 
 流程实例s
*/
public class GenerWorkFlowViews extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new GenerWorkFlowView();
	}
	/** 
	 流程实例集合
	*/
	public GenerWorkFlowViews()  {
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GenerWorkFlowView> ToJavaList() {
		return (java.util.List<GenerWorkFlowView>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GenerWorkFlowView> Tolist()  {
		ArrayList<GenerWorkFlowView> list = new ArrayList<GenerWorkFlowView>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerWorkFlowView)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}