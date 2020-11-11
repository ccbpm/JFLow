package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.wf.*;
import bp.wf.Glo;

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
	public int RetrieveAll() throws Exception
	{
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			return this.Retrieve(FlowSortAttr.OrgNo, WebUser.getOrgNo(), FlowSortAttr.Idx);
		}

		int i = super.RetrieveAll(FlowSortAttr.Idx);
		if (i == 0)
		{
			FlowSort fs = new FlowSort();
			fs.setName("公文类");
			fs.setNo("01");
			fs.Insert();

			fs = new FlowSort();
			fs.setName("办公类");
			fs.setNo("02");
			fs.Insert();
			i = super.RetrieveAll();
		}

		return i;
	}



		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowSort> ToJavaList()
	{
		return (List<FlowSort>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowSort> Tolist()
	{
		ArrayList<FlowSort> list = new ArrayList<FlowSort>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowSort)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}