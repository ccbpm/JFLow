package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
  独立表单树
*/
public class SysFormTree extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
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
	*/
	public final String getParentNo()
	{
		return this.GetValStringByKey(SysFormTreeAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(SysFormTreeAttr.ParentNo, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		map.AddTBInt(SysFormTreeAttr.Idx, 0, "Idx", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 系统方法.

	@Override
	protected boolean beforeDelete()
	{
		if (!DataType.IsNullOrEmpty(this.No))
		{
			DeleteChild(this.No);
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
		for (SysFormTree item : formTrees)
		{
			MapData md = new MapData();
			md.FK_FormTree = item.No;
			md.Delete();
			DeleteChild(item.No);
		}
	}
	public final String DoCreateSameLevelNodeIt(String name)
	{
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.No = BP.DA.DBAccess.GenerOID().toString();
		en.Name = name;
		en.Insert();
		return en.No;
	}
	public final String DoCreateSubNodeIt(String name)
	{
		SysFormTree en = new SysFormTree();
		en.Copy(this);
		en.No = BP.DA.DBAccess.GenerOID().toString();
		en.setParentNo(this.No);
		en.Name = name;
		en.Insert();
		return en.No;
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