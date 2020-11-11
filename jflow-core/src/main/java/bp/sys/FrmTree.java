package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import bp.*;
import java.util.*;

/** 
  独立表单树
*/
public class FrmTree extends EntityTree
{

		///属性.

	/** 
	 父节点编号
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(FrmTreeAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(FrmTreeAttr.OrgNo, value);
	}

		/// 属性.


		///构造方法
	/** 
	 独立表单树
	*/
	public FrmTree()
	{
	}
	/** 
	 独立表单树
	 
	 @param _No
	 * @throws Exception 
	*/
	public FrmTree(String _No) throws Exception
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

		map.IndexField = FrmTreeAttr.ParentNo;


		map.AddTBStringPK(FrmTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(FrmTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(FrmTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		map.AddTBString(FrmTreeAttr.OrgNo, null, "组织编号", false, false, 0, 50, 30);
		map.AddTBInt(FrmTreeAttr.Idx, 0, "Idx", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		/// 系统方法.

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
		FrmTrees formTrees = new FrmTrees();
		formTrees.Retrieve(FrmTreeAttr.ParentNo, parentNo);
		for (FrmTree item : formTrees.ToJavaList())
		{
			MapData md = new MapData();
			md.setFK_FormTree(item.getNo());
			md.Delete();
			DeleteChild(item.getNo());
		}
	}
	public final FrmTree DoCreateSameLevelNode() throws Exception
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(DBAccess.GenerOID()));
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	public final FrmTree DoCreateSubNode() throws Exception
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(DBAccess.GenerOID()));
		en.setParentNo(this.getNo());
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	/** 
	 创建子目录 @lilzhen
	 
	 @param dirName 要创建的子目录名字
	 @return 返回子目录编号
	*/
	public final String CreateSubNode(String dirName) throws Exception
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(DBAccess.GenerOID()));
		en.setParentNo(this.getNo());
		en.setName(dirName);
		en.Insert();
		return en.getNo();
	}
	/** 
	 上移 @lizhen
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoUp() throws Exception
	{
		this.DoOrderUp(FrmTreeAttr.ParentNo, this.getParentNo(), FrmTreeAttr.Idx);
		return "移动成功";
	}
	/** 
	 下移@lizhen
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDown() throws Exception
	{
		this.DoOrderDown(FrmTreeAttr.ParentNo, this.getParentNo(), FrmTreeAttr.Idx);
		return "移动成功";
	}
}