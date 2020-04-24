package BP.Sys;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;


/** 
 EnCfgs
*/
public class DictDtl extends EntityMyPK
{
	/// <summary>
	/// 组织编号
	/// </summary>
	public String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.OrgNo, value);
	}

	public String getDictMyPK() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.DictMyPK);
	}
	public final void setDictMyPK(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.DictMyPK, value);
	}
	public String getBH() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.BH);
	}
	public final void setBH(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.BH, value);
	}
	public String getName() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.Name, value);
	}
	public String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.ParentNo, value);
	}

	public String getIdx() throws Exception
	{
		return this.GetValStrByKey(DictDtlAttr.Idx);
	}
	public final void setIdx(String value) throws Exception
	{
		this.SetValByKey(DictDtlAttr.Idx, value);
	}
     //   #endregion 属性.


	//	#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/// <summary>
	/// 系统字典表
	/// </summary>
	public DictDtl()
	{
	}
	/// <summary>
	/// EnMap
	/// </summary>
	public Map getEnMap()
	{

		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_DictDtl", "系统字典表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		//DictMyPK+"_"+BH
		map.AddMyPK();

		map.AddTBString(DictDtlAttr.DictMyPK, null, "外键表ID", true, false, 0, 200, 20);
		//map.AddTBString(DictDtlAttr.TableID, null, "TableID", true, false, 0, 200, 20);

		map.AddTBString(DictDtlAttr.BH, null, "BH", true, false, 0, 200, 20);
		map.AddTBString(DictDtlAttr.Name, null, "名称", true, false, 0, 200, 20);
		map.AddTBString(DictDtlAttr.ParentNo, null, "父节点ID", true, false, 0, 200, 20);

		//用户注销组织的时候，方便删除数据.
		map.AddTBString(DictDtlAttr.OrgNo, null, "OrgNo", true, false, 0, 200, 20);
		map.AddTBInt(DictAttr.Idx, 0, "顺序号", false, false);
		this.set_enMap(map);
		return this.get_enMap();
	}
       // #endregion

	/// <summary>
	/// 更新的操作
	/// </summary>
	/// <returns></returns>
	protected boolean beforeUpdate() throws Exception
{
	return super.beforeUpdate();
}

	protected void afterInsertUpdateAction() throws Exception
{
	super.afterInsertUpdateAction();
}


	protected  boolean beforeInsert() throws Exception
{
	if (SystemConfig.getCCBPMRunModel()!=CCBPMRunModel.Single)
		this.setOrgNo(BP.Web.WebUser.getOrgNo());

	return super.beforeInsert();
}
}