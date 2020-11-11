package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.wf.*;
import bp.wf.Glo;

import java.util.*;

/** 
  独立表单树
*/
public class SysFormTree extends EntityTree
{

		///属性.
	/** 
	 是否是目录
	 * @throws Exception 
	*/
	public final boolean getIsDir() throws Exception
	{
		return this.GetValBooleanByKey(SysFormTreeAttr.IsDir);
	}
	public final void setIsDir(boolean value) throws Exception
	{
		this.SetValByKey(SysFormTreeAttr.IsDir, value);
	}
	
	
	/** 
	 组织编号
	 * @throws Exception 
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(SysFormTreeAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(SysFormTreeAttr.OrgNo, value);
	}

		/// 属性.


		///构造方法
	/** 
	 独立表单树
	*/
	public SysFormTree()
	{
	}
	/** 
	 独立表单树
	 
	 @param _No
	 * @throws Exception 
	*/
	public SysFormTree(String _No) throws Exception
	{
		super(_No);
	}

		///


		///系统方法.
	/** 
	 独立表单树Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FormTree", "表单树");
		map.setCodeStruct("2");

		map.setDepositaryOfEntity( Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(SysFormTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(SysFormTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(SysFormTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		map.AddTBInt(SysFormTreeAttr.Idx, 0, "Idx", false, false);
		map.AddTBString(SysFormTreeAttr.OrgNo, null, "OrgNo", false, false, 0, 50, 30);

		this.set_enMap(map);
		return this.get_enMap();
	}

		/// 系统方法.

	/** 
	 组织编号
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(WebUser.getOrgNo());
		}
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeDelete() throws Exception
	{
		if (!DataType.IsNullOrEmpty(this.getNo()))
		{
			DeleteChild(this.getNo());
		}
		return super.beforeDelete();
	}
	/** 
	 删除子项
	 
	 @param parentNo
	 * @throws Exception 
	*/
	private void DeleteChild(String parentNo) throws Exception
	{
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveByAttr(SysFormTreeAttr.ParentNo, parentNo);
		for (SysFormTree item : formTrees.ToJavaList())
		{
			MapData md = new MapData();
			md.setFK_FormTree(item.getNo());
			md.Delete();
			DeleteChild(item.getNo());
		}
	}
	public final String DoCreateSameLevelNodeIt(String name) throws Exception
	{
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.setNo(String.valueOf(DBAccess.GenerOID()));
		en.setName( name);
		en.Insert();
		return en.getNo();
	}
	public final String DoCreateSubNodeIt(String name) throws Exception
	{
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.setNo(String.valueOf(DBAccess.GenerOID()));
		en.setParentNo(this.getNo());
		en.setName(name);
		en.Insert();
		return en.getNo();
	}
	
	public String DoUp() throws Exception
	{
		 this.DoOrderUp(SysFormTreeAttr.ParentNo, this.getParentNo(), SysFormTreeAttr.Idx);
		 return "";
	}
	public String DoDown() throws Exception
	{
		 this.DoOrderDown(SysFormTreeAttr.ParentNo, this.getParentNo(), SysFormTreeAttr.Idx);
		 return "";
	}
}