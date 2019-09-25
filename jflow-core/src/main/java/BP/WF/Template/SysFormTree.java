package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
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
	 序号
	 * @throws Exception 
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(SysFormTreeAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(SysFormTreeAttr.Idx, value);
	}
	/** 
	 父节点编号
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStringByKey(SysFormTreeAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
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
	 * @throws Exception 
	*/
	public SysFormTree(String _No) throws Exception
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
		en.setName(name);
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
	
}