package BP.Sys;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;


/** 
 EnCfgs
*/
public class Dict extends EntityMyPK
{

	/// <summary>
	/// 组织编号
	/// </summary>
	public String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(DictAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(DictAttr.OrgNo, value);
	}
	public String getTableID() throws Exception
	{
		return this.GetValStrByKey(DictAttr.TableID);
	}
	public final void setTableID(String value) throws Exception
	{
		this.SetValByKey(DictAttr.TableID, value);
	}
	public String getTableName() throws Exception
	{
		return this.GetValStrByKey(DictAttr.TableName);
	}
	public final void setTableName(String value) throws Exception
	{
		this.SetValByKey(DictAttr.TableName, value);
	}
	public String getDictType() throws Exception
{
	return this.GetValStrByKey(DictAttr.DictType);
}
	public final void setDictType(String value) throws Exception
	{
		this.SetValByKey(DictAttr.DictType, value);
	}
	public String getIdx() throws Exception
	{
		return this.GetValStrByKey(DictAttr.Idx);
	}
	public final void setIdx(String value) throws Exception
	{
		this.SetValByKey(DictAttr.Idx, value);
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
	public Dict()
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
			Map map = new Map("Sys_Dict", "系统字典表");
			map.Java_SetDepositaryOfEntity(Depositary.None);
			map.Java_SetDepositaryOfMap(Depositary.Application);
			map.Java_SetEnType(EnType.Sys);

			// OrgNo+"_"+TableID;
			map.AddMyPK();

			map.AddTBString(DictAttr.TableID, null, "表ID", true, false, 0, 200, 20);
			map.AddTBString(DictAttr.TableName, null, "名称", true, false, 0, 200, 20);
			map.AddDDLSysEnum(DictAttr.DictType, 0, "数据类型", true, false, DictAttr.DictType,
					"@0=编号名称@1=树结构");

			map.AddTBString(DictAttr.OrgNo, null, "OrgNo", true, false, 0, 200, 20);
			map.AddTBInt(DictAttr.Idx, 0, "顺序号", false, false);

			RefMethod rm = new RefMethod();
			rm.Title = "编辑数据";
			rm.ClassMethodName = this.toString() + ".DoEdit";
			rm.refMethodType = RefMethodType.RightFrameOpen;
			rm.IsForEns = false;
			map.AddRefMethod(rm);

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

	/// <summary>
	/// 编辑数据
	/// </summary>
	/// <returns></returns>
	public String DoEdit() throws Exception
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/SysDictEditData.htm?FK_Dict=" + this.getMyPK();
	}
	protected  boolean beforeInsert() throws Exception
{
	if (SystemConfig.getCCBPMRunModel()!=0)
		this.setOrgNo(BP.Web.WebUser.getOrgNo());

	return super.beforeInsert();
}
}