package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.*;
import bp.wf.*;
import bp.wf.Glo;

/** 
  独立表单树
*/
public class SysFormTree extends EntityTree
{

		///#region 属性.
	/** 
	 是否是目录
	*/
	public final boolean isDir() throws Exception
	{
		return this.GetValBooleanByKey(SysFormTreeAttr.IsDir);
	}
	public final void setDir(boolean value)  throws Exception
	 {
		this.SetValByKey(SysFormTreeAttr.IsDir, value);
	}
	/** 
	 序号
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(SysFormTreeAttr.Idx);
	}
	public final void setIdx(int value)
	 {
		this.SetValByKey(SysFormTreeAttr.Idx, value);
	}
	/** 
	 父节点编号
	*/
	public final String getParentNo()
	{
		return this.GetValStringByKey(SysFormTreeAttr.ParentNo);
	}
	public final void setParentNo(String value)
	 {
		this.SetValByKey(SysFormTreeAttr.ParentNo, value);
	}
	/** 
	 组织编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStringByKey(SysFormTreeAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(SysFormTreeAttr.OrgNo, value);
	}

		///#endregion 属性.


		///#region 构造方法
	/** 
	 独立表单树
	*/
	public SysFormTree()  {
	}
	/** 
	 独立表单树
	 
	 param _No
	*/
	public SysFormTree(String _No) throws Exception {
		super(_No);
	}

		///#endregion


		///#region 系统方法.
	/** 
	 独立表单树Map
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FormTree", "表单树");
		map.setCodeStruct("2");

		map.setDepositaryOfEntity( Depositary.None);
		map.setDepositaryOfMap( Depositary.Application);

		map.AddTBStringPK(SysFormTreeAttr.No, null, "编号", true, true, 1, 10, 40);
		map.AddTBString(SysFormTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(SysFormTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 40);
		map.AddTBInt(SysFormTreeAttr.Idx, 0, "Idx", false, false);
		map.AddTBString(SysFormTreeAttr.OrgNo, null, "组织编号", false, false, 0, 50, 30);

		this.set_enMap(map);
		return this.get_enMap();
	}
	public final String DoCreateSameLevelFormNodeMy(String name) throws Exception {
		EntityTree en = this.DoCreateSameLevelNode(name);
		en.setName (name);
		en.Update();
		return en.getNo();
	}
	/** 
	 创建下级目录.
	 
	 param name
	 @return 
	*/
	public final String DoCreateSubFormNodeMy(String name) throws Exception {
		EntityTree en = this.DoCreateSubNode(name);
		en.setName (name);
		en.Update();
		return en.getNo();
	}

		///#endregion 系统方法.

	/** 
	 组织编号
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
			this.setNo(String.valueOf(DBAccess.GenerOID(this.toString())));
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeDelete() throws Exception {
		String sql = "SELECT COUNT(*) as Num FROM Sys_MapData WHERE FK_FormTree='" + this.getNo() + "'";
		int num = DBAccess.RunSQLReturnValInt(sql);
		if (num != 0)
		{
			throw new RuntimeException("err@您不能删除该目录，下面有表单。");
		}


		if (!DataType.IsNullOrEmpty(this.getNo()))
		{
			DeleteChild(this.getNo());
		}

		return super.beforeDelete();
	}
	/** 
	 删除子项
	 
	 param parentNo
	*/
	private void DeleteChild(String parentNo) throws Exception {
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
	public final String DoCreateSameLevelNodeIt(String name) throws Exception {
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.setNo(DBAccess.GenerGUID(10));
		en.setName(name);
		en.Insert();
		return en.getNo();
	}
	public final String DoCreateSubNodeIt(String name) throws Exception {
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.setNo(DBAccess.GenerGUID(10));
		en.setParentNo(this.getNo());
		en.setName(name);
		en.Insert();
		return en.getNo();
	}
	public final String DoUp() throws Exception {
		this.DoOrderUp(SysFormTreeAttr.ParentNo, this.getParentNo(), SysFormTreeAttr.Idx);
		return null;
	}
	public final String DoDown() throws Exception {
		this.DoOrderDown(SysFormTreeAttr.ParentNo, this.getParentNo(), SysFormTreeAttr.Idx);
		return null;
	}
}