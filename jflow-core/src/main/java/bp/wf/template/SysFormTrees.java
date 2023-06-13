package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.*;
import bp.web.WebUser;
import bp.wf.*;
import java.util.*;

/** 
 独立表单树
*/
public class SysFormTrees extends EntitiesTree
{
	/** 
	 独立表单树s
	*/
	public SysFormTrees()  {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()  {
		return new SysFormTree();
	}
	@Override
	public int RetrieveAll() throws Exception {

		if (WebUser.getNo().equals("admin") == true)
			return this.RetrieveAll(FlowSortAttr.Idx);

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
			return this.Retrieve(FlowSortAttr.OrgNo, WebUser.getOrgNo(), FlowSortAttr.Idx);

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
			return this.RetrieveAll(FlowSortAttr.Idx);

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			return this.Retrieve(FlowSortAttr.OrgNo,  WebUser.getOrgNo(), FlowSortAttr.Idx);

		int i = super.RetrieveAll();
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
		SysFormTree fs = new SysFormTree();
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			fs.setName("流程树");
			fs.setNo(bp.web.WebUser.getOrgNo());
			fs.setParentNo("100");
			fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
			fs.Save();

			fs = new SysFormTree();
			fs.setName("公文类");
			fs.setNo(DBAccess.GenerGUID());
			fs.setParentNo(bp.web.WebUser.getOrgNo());
			fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
			fs.Insert();

			fs = new SysFormTree();
			fs.setName("办公类");
			fs.setNo(DBAccess.GenerGUID());
			fs.setParentNo(bp.web.WebUser.getOrgNo());
			fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
			fs.Insert();
			return;
		}

		fs = new SysFormTree();
		fs.setName("流程树");
		fs.setNo("100");
		fs.setParentNo("0");
		fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
		fs.Insert();

		fs = new SysFormTree();
		fs.setName("办公类");
		fs.setNo("01");
		fs.setParentNo("100");
		fs.SetValByKey("OrgNo", bp.web.WebUser.getOrgNo());
		fs.Insert();

		fs = new SysFormTree();
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
	public final java.util.List<SysFormTree> ToJavaList() {
		return (java.util.List<SysFormTree>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SysFormTree> Tolist()  {
		ArrayList<SysFormTree> list = new ArrayList<SysFormTree>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysFormTree)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}