package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.*;
import java.util.*;

/** 
 系统字典表
*/
public class DictDtl extends EntityMyPK
{

		///属性.
	/** 
	 组织编号
	*/
	//public string OrgNo
	//{
	//    get
	//    {
	//        return this.GetValStrByKey(DictDtlAttr.OrgNo);
	//    }
	//    set
	//    {
	//        this.SetValByKey(DictDtlAttr.OrgNo, value);
	//    }
	//}
	public final String getFK_SFTable() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.FK_SFTable);
	}
	public final void setFK_SFTable(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.FK_SFTable, value);
	}
	public final String getBH() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.BH);
	}
	public final void setBH(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.BH, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.getName());
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.Name, value);
	}
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.ParentNo, value);
	}
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(DictDtlAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.Idx, value);
	}

		/// 属性.


		///构造方法
	/** 
	 访问权限
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 系统字典表
	*/
	public DictDtl()
	{
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SFTableDtl", "系统字典表");

			//FK_SFTable+"_"+BH
		map.AddMyPK();

		map.AddTBString(DictDtlAttr.FK_SFTable, null, "外键表ID", true, false, 0, 200, 20);
			//map.AddTBString(DictDtlAttr.TableID, null, "TableID", true, false, 0, 200, 20);

		map.AddTBString(DictDtlAttr.BH, null, "BH", true, false, 0, 200, 20);
		map.AddTBString(DictDtlAttr.Name, null, "名称", true, false, 0, 200, 20);
		map.AddTBString(DictDtlAttr.ParentNo, null, "父节点ID", true, false, 0, 200, 20);

			//用户注销组织的时候，方便删除数据.
			//map.AddTBString(DictDtlAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 20);
		map.AddTBInt(DictDtlAttr.Idx, 0, "顺序号", false, false);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	/** 
	 更新的操作
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		return super.beforeUpdate();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		super.afterInsertUpdateAction();
	}

	//protected override bool beforeInsert()
	//{
	//    if (SystemConfig.CCBPMRunModel != 0)
	//        this.setOrgNo(WebUser.getOrgNo();

	//    return base.beforeInsert();
	//}
}