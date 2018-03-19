package BP.Sys;

import BP.DA.Depositary;
import BP.En.EntitySimpleTree;
import BP.En.Map;
import BP.WF.DotNetToJavaStringHelper;

/** 
独立表单树

*/
public class FrmTree extends EntitySimpleTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性.
	/** 
	 是否是目录
	 
	*/
	public final boolean getIsDir()
	{
		return this.GetValBooleanByKey(FrmTreeAttr.IsDir);
	}
	public final void setIsDir(boolean value)
	{
		this.SetValByKey(FrmTreeAttr.IsDir, value);
	}
	/** 
	 序号
	 
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(FrmTreeAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(FrmTreeAttr.Idx, value);
	}
	/** 
	 父节点编号
	 
	*/
	public final String getParentNo()
	{
		return this.GetValStringByKey(FrmTreeAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(FrmTreeAttr.ParentNo, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	/** 
	 独立表单树
	 
	*/
	public FrmTree()
	{
	}
	/** 
	 独立表单树
	 
	 @param _No
	*/
	public FrmTree(String _No)
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

		map.AddTBStringPK(FrmTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(FrmTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(FrmTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		map.AddTBString(FrmTreeAttr.DBSrc, null, "DBSrc", false, false, 0, 100, 30);

		map.AddTBInt(FrmTreeAttr.IsDir, 0, "是否是目录?", false, false);		 
		map.AddTBInt(FrmTreeAttr.Idx, 0, "Idx", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 系统方法.

	@Override
	protected boolean beforeDelete()
	{
		if (!DotNetToJavaStringHelper.isNullOrEmpty(this.getNo()))
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
		FrmTrees formTrees = new FrmTrees();
		formTrees.RetrieveByAttr(FrmTreeAttr.ParentNo, parentNo);
		for (FrmTree item : formTrees.ToJavaList())
		{
			MapData md = new MapData();
			md.setFK_FormTree(item.getNo());
			md.Delete();
			DeleteChild(item.getNo());
		}
	}
	public final FrmTree DoCreateSameLevelNode()
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(BP.DA.DBAccess.GenerOID()));
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	public final FrmTree DoCreateSubNode()
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(BP.DA.DBAccess.GenerOID())) ;
		en.setParentNo(this.getNo());
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	public final void DoUp()
	{
		this.DoOrderUp(FrmTreeAttr.ParentNo, this.getParentNo(), FrmTreeAttr.Idx);
	}
	public final void DoDown()
	{
		this.DoOrderDown(FrmTreeAttr.ParentNo, this.getParentNo(), FrmTreeAttr.Idx);
	}
}
