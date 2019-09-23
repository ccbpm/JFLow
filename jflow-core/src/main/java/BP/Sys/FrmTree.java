package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
  独立表单树
*/
public class FrmTree extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
	 
	/** 
	 父节点编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStringByKey(FrmTreeAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		this.SetValByKey(FrmTreeAttr.OrgNo, value);
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
	 * @throws Exception 
	*/
	public FrmTree(String _No) throws Exception
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

		map.IndexField = FrmTreeAttr.ParentNo;


		map.AddTBStringPK(FrmTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(FrmTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(FrmTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		map.AddTBString(FrmTreeAttr.OrgNo, null, "组织编号", false, false, 0, 100, 30);
		map.AddTBInt(FrmTreeAttr.Idx, 0, "Idx", false, false);

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
	public  FrmTree DoCreateSameLevelNode() throws Exception
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(BP.DA.DBAccess.GenerOID()));
		en.setName("新建节点");
		en.Insert();
		return en;
	}
	public  FrmTree DoCreateSubNode() throws Exception
	{
		FrmTree en = new FrmTree();
		en.Copy(this);
		en.setNo(String.valueOf(BP.DA.DBAccess.GenerOID()));
		en.setParentNo(this.getNo());
		en.setName("新建节点");
		en.Insert();
		return en;
	}
}