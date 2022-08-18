package bp.wf.template;

import bp.en.*;
import bp.sys.*;
import java.util.*;

/** 
 流程类别
*/
public class FlowSorts extends EntitiesTree
{
	/** 
	 流程类别s
	*/
	public FlowSorts()  {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()  {
		return new FlowSort();
	}
	/** 
	 
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception {
		if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			return this.Retrieve(FlowSortAttr.OrgNo, bp.web.WebUser.getOrgNo(), FlowSortAttr.Idx);
		}

		int i = super.RetrieveAll(FlowSortAttr.Idx);
		if (i == 0)
		{
			FlowSort fs = new FlowSort();
			fs.setName("流程树");
			fs.setNo("100");
			fs.setParentNo("0");
			fs.Insert();

			fs = new FlowSort();
			fs.setName("公文类");
			fs.setNo("01");
			fs.setParentNo("100");
			fs.Insert();

			fs = new FlowSort();
			fs.setName("办公类");
			fs.setNo("02");
			fs.setParentNo("100");
			fs.Insert();
			i = super.RetrieveAll(FlowSortAttr.Idx);
		}
		return i;
	}



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FlowSort> ToJavaList() {
		return (java.util.List<FlowSort>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowSort> Tolist()  {
		ArrayList<FlowSort> list = new ArrayList<FlowSort>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowSort)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}