package bp.wf.template;

import bp.da.DBAccess;
import bp.difference.SystemConfig;
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

		if (bp.web.WebUser.getNo().equals("admin") == true)
			return this.RetrieveAll(FlowSortAttr.Idx);

		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
			return this.Retrieve(FlowSortAttr.OrgNo, bp.web.WebUser.getOrgNo(), FlowSortAttr.Idx);

		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
			return this.RetrieveAll(FlowSortAttr.Idx);

		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			return this.Retrieve(FlowSortAttr.OrgNo, bp.web.WebUser.getOrgNo(), FlowSortAttr.Idx);

		int i = super.RetrieveAll(FlowSortAttr.Idx);
		if (i == 0)
		{
			InitData();
			return this.RetrieveAll();
		}
		return i;
	}

	/**
	 初始化数据.
	 */
	private void InitData() throws Exception {
		FlowSort fs = new FlowSort();
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			fs.setName("流程树");
			fs.setNo(bp.web.WebUser.getOrgNo());
			fs.setParentNo("100");
			fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
			fs.Save();

			fs = new FlowSort();
			fs.setName("公文类");
			fs.setNo(DBAccess.GenerGUID());
			fs.setParentNo(bp.web.WebUser.getOrgNo());
			fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
			fs.Insert();

			fs = new FlowSort();
			fs.setName("办公类");
			fs.setNo(DBAccess.GenerGUID());
			fs.setParentNo(bp.web.WebUser.getOrgNo());
			fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
			fs.Insert();
			return;
		}

		fs = new FlowSort();
		fs.setName("流程树");
		fs.setNo("100");
		fs.setParentNo("0");
		fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
		fs.Insert();

		fs = new FlowSort();
		fs.setName("办公类");
		fs.setNo("01");
		fs.setParentNo("100");
		fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
		fs.Insert();

		fs = new FlowSort();
		fs.setName("公文类");
		fs.setNo("01");
		fs.setParentNo("100");
		fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
		fs.Insert();

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