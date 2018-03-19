package BP.WF.Template;

import BP.DA.Depositary;
import BP.En.EntitySimpleTree;
import BP.En.Map;
import BP.Sys.MapData;
import BP.Tools.StringHelper;

/** 
  独立表单树
 
*/
public class SysFormTree extends EntitySimpleTree
{

	/** 
	 是否是目录
	 
	*/
	public final boolean getIsDir()
	{
		return this.GetValBooleanByKey(SysFormTreeAttr.IsDir);
	}
	public final void setIsDir(boolean value)
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
	 
	*//*
	public final String getParentNo()
	{
		return this.GetValStringByKey(SysFormTreeAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(SysFormTreeAttr.ParentNo, value);
	}*/
	/** 
	 子文件夹
	*/
	public final SysForms getHisSubFormSorts()
	{
		try
		{
			SysForms formSorts = new SysForms();
			formSorts.RetrieveByAttr(SysFormTreeAttr.ParentNo, this.getNo());
			return formSorts;
		}
		catch (java.lang.Exception e)
		{
		}
		return null;
	}
	/** 
	 类别下所包含表单
	*/
	public final SysForms getHisForms()
	{
		try
		{
			SysForms flowSorts = new SysForms();
			flowSorts.RetrieveByAttr(SysFormTreeAttr.FK_FormSort, this.getNo());
			return flowSorts;
		}
		catch (java.lang.Exception e)
		{
		}
		return null;
	}
	/** 
	 数据源
	 
	*/
	public final String getDBSrc()
	{
		return this.GetValStringByKey(SysFormTreeAttr.DBSrc);
	}
	public final void setDBSrc(String value)
	{
		this.SetValByKey(SysFormTreeAttr.DBSrc, value);
	}

		///#endregion 属性.


		
	/** 
	 独立表单树
	 
	*/
	public SysFormTree()
	{
	}
	/** 
	 独立表单树
	 
	 @param _No
	*/
	public SysFormTree(String _No)
	{
		super(_No);
	}

		///#endregion


		///#region 系统方法.
	/** 
	 独立表单树Map
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FormTree", "表单树");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(SysFormTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(SysFormTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(SysFormTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		//map.AddTBString(SysFormTreeAttr.DBSrc, "local", "数据源", false, false, 0, 100, 30);

		map.AddTBInt(SysFormTreeAttr.IsDir, 0, "是否是目录?", false, false);
		map.AddTBInt(SysFormTreeAttr.Idx, 0, "Idx", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion 系统方法.

	@Override
	protected boolean beforeDelete()
	{
		if (!StringHelper.isNullOrEmpty(this.getNo()))
		{
			DeleteChild(this.getNo());
		}
		return super.beforeDelete();
	}
	/** 
	 删除子项
	 
	 @param parentNo
	*/
	private void DeleteChild(String parentNo)
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
	public final SysFormTree DoCreateSameLevelNode()
	{
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.setNo(BP.DA.DBAccess.GenerOID()+"");
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	public final SysFormTree DoCreateSubNode()
	{
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.setNo(BP.DA.DBAccess.GenerOID()+"");
		en.setParentNo(this.getNo());
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	public final void DoUp()
	{
		this.DoOrderUp(SysFormTreeAttr.ParentNo, this.getParentNo(), SysFormTreeAttr.Idx);
	}
	public final void DoDown()
	{
		this.DoOrderDown(SysFormTreeAttr.ParentNo, this.getParentNo(), SysFormTreeAttr.Idx);
	}
}