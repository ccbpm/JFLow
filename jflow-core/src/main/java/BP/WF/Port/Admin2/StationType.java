package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;

/** 
  岗位类型
*/
public class StationType extends EntityNoName
{
	/** 
	 组织编号
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(StationAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(StationAttr.OrgNo, value);
	}

		///#region 构造方法
	/** 
	 岗位类型
	*/
	public StationType()
	{
	}
	/** 
	 岗位类型
	 
	 @param _No
	*/
	public StationType(String _No) throws Exception
	{
		super(_No);
	}
		///#endregion

	/** 
	 岗位类型Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_StationType");
		map.setEnDesc("岗位类型");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(StationTypeAttr.No, null, "编号", true, true, 1, 50, 2);
		map.AddTBString(StationTypeAttr.Name, null, "名称", true, false, 0, 100, 20);
		map.AddTBString(StationAttr.OrgNo, null, "OrgNo", true, true, 0, 60, 250);
		map.AddTBInt(StationAttr.Idx, 0, "顺序", true, false);

			//增加隐藏查询条件.
		map.AddHidden(StationAttr.OrgNo, "=", BP.Web.WebUser.getOrgNo());

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.GroupInc)
		{
			throw new RuntimeException("err@非GroupInc模式，不能插入. ");
		}

		this.setOrgNo(BP.Web.WebUser.getOrgNo());
		return super.beforeInsert();
	}
}