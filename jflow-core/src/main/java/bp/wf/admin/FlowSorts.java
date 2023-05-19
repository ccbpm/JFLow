package bp.wf.admin;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.Flow;

import java.util.*;

/** 
 流程目录
*/
public class FlowSorts extends EntitiesNoName
{

		///#region 构造.
	/** 
	 流程目录s
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

	/** 
	 
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception {
		if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(FlowSortAttr.OrgNo, "=", bp.web.WebUser.getOrgNo());
			qo.addAnd();
			qo.AddWhere(FlowSortAttr.ParentNo, "!=", "0");
			qo.addOrderBy("Idx");
			return qo.DoQuery();
//			return this.Retrieve(FlowSortAttr.OrgNo, bp.web.WebUser.getOrgNo(), FlowSortAttr.Idx);
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowSortAttr.ParentNo, "!=", "0");
		qo.addOrderBy("Idx");
		int i = qo.DoQuery();

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

			qo = new QueryObject(this);
			qo.AddWhere(FlowSortAttr.ParentNo, "!=", "");
			qo.addOrderBy("Idx");
			i = qo.DoQuery();
		}
		return i;
	}

	@Override
	public int RetrieveAllFromDBSource() throws Exception {
		if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(FlowSortAttr.OrgNo, "=", bp.web.WebUser.getOrgNo());
			qo.addAnd();
			qo.AddWhere(FlowSortAttr.ParentNo, "!=", "0");
			qo.addOrderBy("Idx");
			return qo.DoQuery();
//			return this.Retrieve(FlowSortAttr.OrgNo, bp.web.WebUser.getOrgNo(), FlowSortAttr.Idx);
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowSortAttr.ParentNo, "!=", "0");
		qo.addOrderBy("Idx");
		int i = qo.DoQuery();

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

			qo = new QueryObject(this);
			qo.AddWhere(FlowSortAttr.ParentNo, "!=", "");
			qo.addOrderBy("Idx");
			i = qo.DoQuery();
		}
		return i;
	}

	///#endregion 构造.



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}